package enity;

import java.util.Date;
import java.util.List;

import annotation.Column;
import annotation.Id;
import annotation.Table;

@Table(name = "groups")
public class Group {
	
	@Id(name = "group_id", type = "varchar", length = 10)
	private String id;
	
	@Column(name = "group_name", type = "varchar")
	private String name;
	
	@Column(name = "owner_id", type = "char", length = 11)
	private String ownerId;
	
	@Column(name = "create_date", type = "datetime", length = 4)
	private Date createDate;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
