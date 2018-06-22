package utils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.sun.glass.ui.Size;

import dao.MySqlDao;

public class ConPool {
	/**
	 * ���������б�
	 */
	private List<MyCon> freeCons = new ArrayList<MyCon>();
	/**
	 * ��æ�����б�
	 */
	private List<MyCon> buzyCons = new ArrayList<MyCon>();
	/**
	 * ���������
	 */
	private int maxConnectNum = 10;
	/**
	 * ��С������
	 */
	private int minConnectNum = 2;
	/**
	 * ��ǰ������
	 */
	private int currentConnectNum = 0;
	/**
	 * ���ȴ�ʱ��
	 */
	private int maxWaitTime = 6000;
	/**
	 * ����ʽ����ģʽ���̰߳�ȫ��Ч�ʽϸߣ����˷��ڴ�
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
	 * ��ȡһ�����е����ݿ����ӣ����û�У����½����ݿ�����
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
	 * ��ȡһ����������
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
		//һֱ������ֱ����������Դ����ʹ��
		int currentWaitTime = 0;
		while(this.currentConnectNum >= this.maxConnectNum){
			try {
				Thread.sleep(1000);
				currentWaitTime += 1000;
				if(currentWaitTime > maxWaitTime){
					throw new Exception("�ȴ���ʱ!!!");
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
	 * ������״̬����Ϊ����״̬
	 * @param con
	 */
	public void setFree(MyCon con){
		this.buzyCons.remove(con);
		con.setState(MyCon.FREE);
		this.freeCons.add(con);
	}
	
	@Override
	public String toString(){
		return "��ǰ������ �� " + this.currentConnectNum + " ���������� �� " + this.freeCons.size() + " ��æ������ �� " + this.buzyCons.size();
	}
}
