package service;

import java.util.List;

import dao.BaseJDBCDao;
import enity.Group;
import enity.User;
import utils.StringUtil;

/**
 * ��������������ݿ�İ����࣬��Ҫ�ǽ��һЩ�������໥���öԷ�����ķ������⣨��ʵҲ�����漰�����ݿ������ˣ�
 * @author NewBies
 *
 */
public class ServiceHelper {

	/**
	 * �����û���Ҳ���������жϺ����Ƿ����
	 * @param userId
	 * @return
	 */
	public static List<User> selectUsers(String userId){
		if(StringUtil.isNull(userId)){
			return null;
		}
		else if(userId.length() < 11){
			return BaseJDBCDao.select(User.class, "select * from user where user_id like %" + userId + "%");
		}
		else if(userId.length() > 11){
			return null;
		}
		else{
			return BaseJDBCDao.select(User.class, "select * from user where user_id = " + userId);
		}
	}
	
	public static List<Group> selectGroups(String groupId){
		if(StringUtil.isNull(groupId)){
			return null;
		}
		else {
			return BaseJDBCDao.select(Group.class, "select * from group where group_id = " + groupId);
		}
	}
}
