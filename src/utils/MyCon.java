package utils;

import java.sql.Connection;

/**
 * ��Connection������з�װ�����丳��״̬
 * @author NewBies
 *
 */
public class MyCon {
	/**
	 * ���ӿ���
	 */
	public static final int FREE = 1;
	/**
	 * ���ӷ�æ
	 */
	public static final int BUZY = 0;
	/**
	 * ���ӹر�
	 */
	public static final int CLOSE = -1;
	/**
	 * ���е����ݿ�����
	 */
	private Connection connection;
	/**
	 * ���ݿ⵱ǰ����״̬��Ĭ��Ϊ����
	 */
	private int state = FREE;
	
	public MyCon(Connection connection){
		this.connection = connection;
	}
	
	public Connection getConnection(){
		return this.connection;
	}
	
	public int getState(){
		return this.state;
	}
	
	public void setState(int state){
		this.state = state;
	}
}
