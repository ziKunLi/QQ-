package enity;

import annotation.Column;
import annotation.Id;
import annotation.Table;

@Table(name = "user_friend")
public class UserFriend {
	
	@Id(name = "user_friend_id", type = "varchar", length = 32)
	private String id;
	/**
	 * 好友ID
	 */
	@Column(name = "friend_id", type = "char", length = 11)
	private String friendId;
	/**
	 * 用户ID
	 */
	@Column(name = "user_id", type = "char", length = 11)
	private String userId;
	/**
	 * 好友备注
	 */
	@Column(name = "friend_node", type = "varchar")
	private String friendNode;
	
	public String getFriendId() {
		return friendId;
	}
	public String getUserId() {
		return userId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getId() {
		return id;
	}
	public String getFriendNode() {
		return friendNode;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setFriendNode(String friendNode) {
		this.friendNode = friendNode;
	}
}
