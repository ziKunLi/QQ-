package service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dao.BaseJDBCDao;
import enity.Group;
import enity.UserGroup;
import utils.StringUtil;

public class UserGroupService {

	/**
	 * 获取到用户所拥有的群
	 * @param userId
	 * @return
	 */
	public List<Group> getGroups(String userId){
		//根据用户ID获取到用户所拥有的群ID
		List<UserGroup> userGroups = 
				BaseJDBCDao.select(UserGroup.class, "select * from user_group where user_id = " + userId);
		if(userGroups == null){
			return null;
		}
		List<Group> groups = new ArrayList<Group>();
		for(int i = 0; i < userGroups.size(); i++){
			List<Group> group = 
					BaseJDBCDao.select(Group.class, "select * from group where group_id = " + userGroups.get(i).getGroupId());
			groups.addAll(group);
		}
		
		return groups;
	}
	
	/**
	 * 添加群
	 * @param userId
	 * @param groupId
	 * @param groupName
	 * @return
	 */
	public String addGroup(String userId,String groupId, String groupName){
		if(!StringUtil.isRightId(userId)||StringUtil.isNull(groupName)){
			return "添加失败，输入有误！";
		}
		List<Group> groups = ServiceHelper.selectGroups(groupId);
		if(groups == null||groups.size() == 0){
			return "该群不存在";
		}
		else{
			UserGroup userGroup = new UserGroup();
			userGroup.setGroupId(groupId);
			userGroup.setId(UUID.randomUUID().toString());
			userGroup.setType(0);
			userGroup.setUserId(userId);
			return "添加成功";
		}
	}
}
