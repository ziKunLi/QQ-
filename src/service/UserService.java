package service;

import java.util.Date;
import java.util.List;

import dao.BaseJDBCDao;
import enity.User;
import utils.StringUtil;

public class UserService {
	
	
	/**
	 * 添加用户，就是注册用户
	 * @return
	 */
	public boolean addUser(String userId,String nickName, String passWord){
		try {
			if(StringUtil.isRightId(userId)&&!StringUtil.isNull(nickName)&&!StringUtil.isNull(passWord)){
				User user = new User();
				user.setId(userId);
				user.setNickName(nickName);
				user.setPassword(passWord);
				user.setFreeze(false);
				user.setCreateDate(new Date());
				user.setType(1);
				return BaseJDBCDao.add(user);
			}
			else{
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 模糊查询用户
	 * @param userIdOrName
	 * @return
	 */
	public List<User> selectUser(String userIdOrName){
		List<User> usersById =
				BaseJDBCDao.select(User.class, "select * from user where user_id like %" + userIdOrName + "%");
		List<User> usersByName = 
				BaseJDBCDao.select(User.class, "select * from user where nick_name like %" + userIdOrName + "%");
		usersById.addAll(usersByName);
		return usersById;
	}

	
	/**
	 * 判断密码是否正确
	 * @param userId
	 * @param password
	 * @return
	 */
	public int isRightPassword(String userId, String password){
		if(StringUtil.isRightId(userId)&&StringUtil.isRightId(password)){
			try{
				List<User> users = BaseJDBCDao.select(User.class, "select * from user where user_id = " + userId);
				if(users.get(0).getPassword().equals(password)){
					return 1;
				}
				else{
					return 2;
				}
			}catch(Exception e){
				return 0;
			}
		}
		else {
			return 0;
		}
	}
}
