package dto;

import java.util.List;

public class GroupInfo {
	private String groupId;
	
	private String groupName;
	
	private List<String> members;

	public String getGroupId() {
		return groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}
}
