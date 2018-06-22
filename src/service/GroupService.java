package service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import dao.BaseJDBCDao;
import enity.Group;
import enity.UserGroup;
import utils.StringUtil;

public class GroupService {
	
	/**
	 * 创建群
	 * @param userId 创建者
	 * @param members 成员
	 */
	public boolean createGroup(String userId, String groupId,String name,List<String> members){
		if(!StringUtil.isRightId(userId)||StringUtil.isNull(name)||members == null||StringUtil.isNull(groupId)){
			return false;
		}
		Group group = new Group();
		group.setId(groupId);
		group.setOwnerId(userId);
		group.setCreateDate(new Date());
		group.setName(name);
		//向数据库添加群信息（创建群）
		if(!BaseJDBCDao.add(group)){
			return false;
		}
		//为该群添加成员
		for(int i = 0; i < members.size(); i++){
			UserGroup userGroup = new UserGroup();
			userGroup.setUserId(members.get(i));
			userGroup.setType(0);
			userGroup.setId(UUID.randomUUID().toString());
			userGroup.setGroupId(groupId);
			BaseJDBCDao.add(userGroup);
		}
		return true;
	}
	
	/**
	 * 获得群成员所有信息
	 * @param groupId
	 * @return
	 */
	public List<UserGroup> getMemberInfo(String groupId){
		if(StringUtil.isNull(groupId)){
			return null;
		}
		else{
			return BaseJDBCDao.select(UserGroup.class, "select * from user_group where group_id = " + groupId);
		}
	}
	
	/**
	 * 获得所有群成员的ID
	 * @param groupId
	 * @return
	 */
	public List<String> getMemberId(String groupId){
		List<UserGroup> userGroups = getMemberInfo(groupId);
		List<String> memberIds = new ArrayList<String>();
		for(int i = 0; i < memberIds.size(); i++){
			memberIds.add(userGroups.get(i).getId());
		}
		
		return memberIds;
	}
}
