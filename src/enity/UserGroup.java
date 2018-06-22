package enity;

import annotation.Column;
import annotation.Id;
import annotation.Table;

@Table(name = "user_group")
public class UserGroup {

	@Id(name = "user_group_id", type = "char", length = 32)
	private String id;
	
	@Column(name = "user_id", type = "char", length = 11)
	private String userId;
	
	@Column(name = "group_id", type = "char", length = 32)
	private String groupId;
	
	@Column(name = "type", type = "int", length = 4)
	private int type;

	public String getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public String getGroupId() {
		return groupId;
	}

	public int getType() {
		return type;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
