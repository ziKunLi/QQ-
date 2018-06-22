package service;

import java.util.List;
import java.util.UUID;

import dao.BaseJDBCDao;
import enity.UserFriend;
import utils.StringUtil;

public class UserFriendService {

	/**
	 * 根据用户ID添加好友
	 * @param userId
	 * @param friendId
	 * @return
	 */
	public boolean addFriend(String userId, String friendId, String friendNode){
		if(StringUtil.isRightId(userId)&&StringUtil.isRightId(friendId)&&StringUtil.isNull(friendNode)){
			UserFriend userFriend = new UserFriend();
			userFriend.setId(UUID.randomUUID().toString());
			userFriend.setUserId(userId);
			userFriend.setFriendId(friendId);
			userFriend.setFriendNode(friendNode);
			return BaseJDBCDao.add(userFriend);
		}	
		else{
			return false;
		}
	}
	
	/**
	 * 删除好友
	 * @param userId
	 * @param friendId
	 * @return
	 */
	public boolean deleteFriend(String userId, String friendId){
		if(StringUtil.isRightId(userId)&&StringUtil.isRightId(friendId)){
			try {
				List<UserFriend> userFriends = 
						BaseJDBCDao.select(UserFriend.class,
								"select * from user_friend where user_id = " + userId + " and friend_id = " + friendId);
				UserFriend userFriend = new UserFriend();
				userFriend.setId(userFriends.get(0).getId());
				return BaseJDBCDao.delete(userFriend);
			} catch (Exception e) {
				return false;
			}
		}
		else{
			return false;
		}
	}
	
	/**
	 * 获取用户的好友列表
	 * @param userId
	 * @return
	 */
	public List<UserFriend> getFriendList(String userId){
		if(StringUtil.isRightId(userId)){
			return BaseJDBCDao.select(UserFriend.class, "select * from user_friend where user_id = " + userId);
		}	
		else{
			return null;
		}
	}
}
