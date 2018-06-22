package utils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.sun.glass.ui.Size;

import dao.MySqlDao;

public class ConPool {
	/**
	 * 空闲连接列表
	 */
	private List<MyCon> freeCons = new ArrayList<MyCon>();
	/**
	 * 繁忙连接列表
	 */
	private List<MyCon> buzyCons = new ArrayList<MyCon>();
	/**
	 * 最大连接数
	 */
	private int maxConnectNum = 10;
	/**
	 * 最小连接数
	 */
	private int minConnectNum = 2;
	/**
	 * 当前连接数
	 */
	private int currentConnectNum = 0;
	/**
	 * 最大等待时长
	 */
	private int maxWaitTime = 6000;
	/**
	 * 饿汉式单利模式，线程安全，效率较高，但浪费内存
	 */
	private static ConPool conPool = new ConPool();
	
	private ConPool(){
		while(this.minConnectNum > this.currentConnectNum){
			this.freeCons.add(this.createCon());
		}
	}
	
	public static ConPool getInstance(){
		return conPool;
	}
	
	/**
	 * 获取一个空闲的数据库连接，如果没有，则新建数据库连接
	 * @return
	 */
	public MyCon getCon(){
		MyCon myCon = this.getFreeCon();
		if(myCon != null){
			return myCon;
		}
		else{
			return this.getNewCon();
		}
	}
	
	/**
	 * 获取一个空闲连接
	 * @return
	 */
	private MyCon getFreeCon(){
		if(freeCons.size() > 0){
			MyCon con = freeCons.remove(0);
			con.setState(MyCon.BUZY);
			this.buzyCons.add(con);
			return con;
		}
		else{
			return null;
		}		
	}
	
	private MyCon getNewCon(){
		//一直阻塞，直到有连接资源可以使用
		int currentWaitTime = 0;
		while(this.currentConnectNum >= this.maxConnectNum){
			try {
				Thread.sleep(1000);
				currentWaitTime += 1000;
				if(currentWaitTime > maxWaitTime){
					throw new Exception("等待超时!!!");
				}
			} catch (Exception e) {
				return null;
			}
		}
		
		MyCon myCon = this.createCon();
		myCon.setState(MyCon.BUZY);
		this.buzyCons.add(myCon);
		return myCon;
	}
	
	private MyCon createCon(){
		try {
			Connection connection = MySqlDao.getConnection();
			MyCon myCon = new MyCon(connection);
			this.currentConnectNum++;
			return myCon;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 将连接状态设置为空闲状态
	 * @param con
	 */
	public void setFree(MyCon con){
		this.buzyCons.remove(con);
		con.setState(MyCon.FREE);
		this.freeCons.add(con);
	}
	
	@Override
	public String toString(){
		return "当前连接数 ： " + this.currentConnectNum + " 空闲连接数 ： " + this.freeCons.size() + " 繁忙连接数 ： " + this.buzyCons.size();
	}
}
