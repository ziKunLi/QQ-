package message;

import java.util.List;

import dto.GroupInfo;
import enity.UserFriend;

public class InitMessage {
	
	private String type;
	
	private List<UserFriend> friends;
	
	private List<GroupInfo> groups;
		
	public String getType() {
		return type;
	}

	public List<UserFriend> getFriends() {
		return friends;
	}

	public List<GroupInfo> getGroups() {
		return groups;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setFriends(List<UserFriend> friends) {
		this.friends = friends;
	}

	public void setGroups(List<GroupInfo> groups) {
		this.groups = groups;
	}
}
