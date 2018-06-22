package dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import annotation.Column;
import annotation.Id;
import annotation.Table;
import utils.ConPool;
import utils.MyCon;

public class BaseJDBCDao {
	
	/**
	 * ���һ������
	 * @param t
	 * @return
	 */
	public static <T> boolean add(T t){
		
		StringBuffer sql = new StringBuffer();  
        
        sql.append("insert into ");  
          
        Table tableName = (Table)t.getClass().getAnnotation(Table.class);  
        
        if(tableName == null){
        	return false;
        }
        else{
        	sql.append(tableName.name() + " values(");
        }
        
        Field[] fields = t.getClass().getDeclaredFields();
        for(Field field : fields){
        	//�жϸ��ֶ����Ƿ���ָ����ע��
        	if(!field.isAnnotationPresent(Id.class)&&!field.isAnnotationPresent(Column.class)){
        		continue;
        	}
        
        	sql.append(classCast(field, t) + ",");
        }
        sql.setLength(sql.length() - 1);
        sql.append(")");
		return execute(sql.toString());
	}
	
	/**
	 * ɾ��
	 * @param t
	 * @return
	 */
	public static <T> boolean delete(T t){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ");
		Table tableName = (Table)t.getClass().getAnnotation(Table.class);  
		if(tableName == null){
	      	return false;
	    }
		sql.append(tableName.name());
		Field[] fields = t.getClass().getDeclaredFields();
		for(Field field : fields){
			if(!field.isAnnotationPresent(Id.class)){
				continue;
			}
			else{
				sql.append(" where " + field.getAnnotation(Id.class).name() + " = " + classCast(field, t));
				break;
			}
		}
		System.out.println(sql);
		return execute(sql.toString());
	}
	
	/**
	 * ����
	 * @param t
	 * @return
	 */
	public static <T> boolean update(T t){
		StringBuffer sql = new StringBuffer();
		sql.append("update ");
		Table tableName = (Table)t.getClass().getAnnotation(Table.class);  
		if(tableName == null){
	      	return false;
	    }
		sql.append(tableName.name() + " \nset ");
		Field[] fields = t.getClass().getDeclaredFields();
		String key = "";
		for(Field field : fields){
			if(!field.isAnnotationPresent(Id.class)&&!field.isAnnotationPresent(Column.class)){
				continue;
			}
			else if(field.isAnnotationPresent(Id.class)){
				key = "\nwhere " + field.getAnnotation(Id.class).name() + " = " + classCast(field, t) + "";
			}
			else if(field.isAnnotationPresent(Column.class)){
				sql.append(field.getAnnotation(Column.class).name() + " = " + classCast(field, t) + ", ");
			}
		}
		sql.setLength(sql.length() - 2);
		sql.append(key);
		return execute(sql.toString());
	}
	
	/**
	 * ����
	 * @param clazz
	 * @param sql
	 * @return
	 */
	public static <T> List<T> select(Class<T> clazz, String sql){
		//ִ�в�ѯ���
		ResultSet resultSet = null;
		List<T> list = null;
		try {
			resultSet = ConPool.getInstance().getCon().getConnection().createStatement().executeQuery(sql);
			list = new ArrayList<T>();
			while(resultSet.next()){
				//ʵ��������
				T t = clazz.newInstance();
				Field[] fields = t.getClass().getDeclaredFields();
				for(int i = 0 ; i < fields.length; i++){
					//����ѯ������Ϣ��ӵ�������
					fields[i].setAccessible(true);
					fields[i].set(t, resultSet.getObject(i + 1));
				}
				
				list.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		
		}
		
		return list;
	}
	
	/**
	 * ִ�зǲ�ѯ��sql���
	 * @param sql
	 * @return
	 */
	private static boolean execute(String sql) {

		try {
			// ��ȡһ�����Ӷ���
			MyCon myCon = ConPool.getInstance().getCon();
			Connection connection = myCon.getConnection();
			Statement statement = connection.createStatement();
			statement.execute(sql);
			// ����ʹ����Ͻ�����Ϊ����
			ConPool.getInstance().setFree(myCon);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * ��ͨ������õ������Խ�������ת�������ض�Ӧ��sql�ַ���
	 * @param field
	 * @param object
	 * @return
	 */
	private static String classCast(Field field,Object object){
		
		field.setAccessible(true);
		String type = field.getType().toString();
		String result = "";
		try {
			
			if(type.equals(Date.class.toString())){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				result = "'" + format.format((Date)field.get(object)) + "'";
			}
			else if(type.equals(Boolean.class.toString())||type.equals("boolean")){
				result = field.get(object).toString();
				System.out.println("result: " + result);
			}
			else{
				result = "'" + field.get(object).toString() + "'";
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
