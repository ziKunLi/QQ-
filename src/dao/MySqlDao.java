package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * 获取数据库连接对象
 * @author NewBies
 *
 */
public class MySqlDao {
	
	public static Connection getConnection() throws Exception{
		String driverName = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/exercise";
		String userName = "root";
		String password = "123456";
		Class.forName(driverName);
		Connection connection = DriverManager.getConnection(url,userName,password);
		return connection;
	}
	
	public static PreparedStatement preparedStatement(String sql) throws Exception{
		return getConnection().prepareStatement(sql);
	}
	
	public static Statement getStatement() throws Exception{
		return getConnection().createStatement();
	}
}
