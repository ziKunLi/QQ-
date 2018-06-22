package enity;

import java.util.Date;

import annotation.Column;
import annotation.Id;
import annotation.Table;

@Table(name = "user")
public class User {
	
	/**
	 * 用户ID默认为11位的电话号码
	 */
	@Id(name = "user_id", type ="char", length=11)
	private String id;
	
	@Column(name = "password", type = "varchar")
	private String password;
	
	@Column(name = "nick_name", type = "varchar")
	private String nickName;
	
	@Column(name = "create_date", type = "datetime", length = 4)
	private Date createDate;
	
	@Column(name = "is_freeze", type = "bit", length = 1)
	private boolean isFreeze;
	
	@Column(name = "type",type = "int", length = 4)
	private int type;

	public String getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public boolean isFreeze() {
		return isFreeze;
	}

	public int getType() {
		return type;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public void setFreeze(boolean isFreeze) {
		this.isFreeze = isFreeze;
	}

	public void setType(int type) {
		this.type = type;
	}
}
