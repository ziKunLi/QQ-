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
	 * ��ȡ���û���ӵ�е�Ⱥ
	 * @param userId
	 * @return
	 */
	public List<Group> getGroups(String userId){
		//�����û�ID��ȡ���û���ӵ�е�ȺID
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
	 * ���Ⱥ
	 * @param userId
	 * @param groupId
	 * @param groupName
	 * @return
	 */
	public String addGroup(String userId,String groupId, String groupName){
		if(!StringUtil.isRightId(userId)||StringUtil.isNull(groupName)){
			return "���ʧ�ܣ���������";
		}
		List<Group> groups = ServiceHelper.selectGroups(groupId);
		if(groups == null||groups.size() == 0){
			return "��Ⱥ������";
		}
		else{
			UserGroup userGroup = new UserGroup();
			userGroup.setGroupId(groupId);
			userGroup.setId(UUID.randomUUID().toString());
			userGroup.setType(0);
			userGroup.setUserId(userId);
			return "��ӳɹ�";
		}
	}
}
