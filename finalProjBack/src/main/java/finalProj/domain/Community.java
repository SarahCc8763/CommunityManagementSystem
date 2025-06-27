package finalProj.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "community")
public class Community {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer communityId; // 社區流水號

//	@JsonManagedReference("community")
//	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Ticket> tickets;

//	@JsonManagedReference("community")
//	@OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
//	private List<Users> users;

	@Column(name = "name")
	private String name; // 社區名稱

	@Column(name = "address")
	private String address; // 地址

	@Column(name = "create_time", insertable = false, updatable = false)
	private java.util.Date createTime; // 創建時間

	@Column(name = "[function]") // SQL Server 保留字，需用中括號轉義
	private Long function; // 使用功能

//	@Override
//	public String toString() {
//		return "Community [communityId=" + communityId + ", tickets=" + tickets + ", name=" + name + ", address="
//				+ address + ", createTime=" + createTime + ", function=" + function + "]";
//	}

	public Integer getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Integer communityId) {
		this.communityId = communityId;
	}

//	public List<Ticket> getTickets() {
//		return tickets;
//	}
//
//	public void setTickets(List<Ticket> tickets) {
//		this.tickets = tickets;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public java.util.Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

//	public List<Users> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<Users> users) {
//		this.users = users;
//	}

	public Long getFunction() {
		return function;
	}

	public void setFunction(Long function) {
		this.function = function;
	}

}