package utils;

import java.sql.Connection;

/**
 * 对Connection对象进行封装，将其赋予状态
 * @author NewBies
 *
 */
public class MyCon {
	/**
	 * 连接空闲
	 */
	public static final int FREE = 1;
	/**
	 * 连接繁忙
	 */
	public static final int BUZY = 0;
	/**
	 * 连接关闭
	 */
	public static final int CLOSE = -1;
	/**
	 * 持有的数据库连接
	 */
	private Connection connection;
	/**
	 * 数据库当前连接状态，默认为空闲
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
