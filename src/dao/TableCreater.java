package dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import annotation.Column;
import annotation.Id;
import annotation.Table;
import enity.Group;
import enity.User;
import enity.UserFriend;
import enity.UserGroup;
import utils.ConPool;
import utils.MyCon;

public class TableCreater {
	
	public static void main(String[] args){
		System.out.println(createTable(Group.class));
		System.out.println(createTable(User.class));
		System.out.println(createTable(UserFriend.class));
		System.out.println(createTable(UserGroup.class));
	}
	
	public static boolean createTable(Class<?> clazz){
		StringBuffer sql = new StringBuffer();
		sql.append("Create table ");
		Table table = (Table)clazz.getAnnotation(Table.class);
		if(table == null){
			return false;
		}
		else{
			sql.append(table.name() + "(\n");
		}
		
		Field[] fields = clazz.getDeclaredFields();
	    for(Field field : fields){
	       	//判断该字段上是否有指定的注解
	       	if(!field.isAnnotationPresent(Id.class)&&!field.isAnnotationPresent(Column.class)){
	       		continue;
	       	}
	       	if(field.isAnnotationPresent(Id.class)){
	       		Id id = field.getAnnotation(Id.class);
	       		sql.append("\t" + id.name() + " " + id.type() + " (" +id.length() +") primary key,\n");
	       	}
	       	else if(field.isAnnotationPresent(Column.class)){
	       		Column column = field.getAnnotation(Column.class);
	       		sql.append("\t" + column.name() + " " +column.type());
	       		if(!column.type().toLowerCase().equals("date")){
	       			sql.append(" (" + column.length() + ")");
	       		}
	       		if(!column.isNull()){
	       			sql.append(" not null,\n");
	       		}
	       	}
	    }
	    sql.setLength(sql.length() - 2);
       	sql.append("\n\t\t)");
	    System.out.println(sql);
		return execute(sql.toString());
	}
	
	/**
	 * 执行非查询的sql语句
	 * @param sql
	 * @return
	 */
	private static boolean execute(String sql) {

		try {
			// 获取一个连接对象
			MyCon myCon = ConPool.getInstance().getCon();
			System.out.println(myCon);
			Connection connection = myCon.getConnection();
			System.out.println(connection);
			Statement statement = connection.createStatement();
			statement.execute(sql);
			// 连接使用完毕将其设为空闲
			ConPool.getInstance().setFree(myCon);
		} catch (SQLException e) {
			
			return false;
		}
		return true;
	}
}
