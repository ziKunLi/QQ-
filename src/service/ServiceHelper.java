package service;

import java.util.List;

import dao.BaseJDBCDao;
import enity.Group;
import enity.User;
import utils.StringUtil;

/**
 * 各个服务操作数据库的帮助类，主要是解决一些服务中相互引用对方服务的方法问题（其实也就是涉及到数据库连表了）
 * @author NewBies
 *
 */
public class ServiceHelper {

	/**
	 * 查找用户，也可以用来判断好友是否存在
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
