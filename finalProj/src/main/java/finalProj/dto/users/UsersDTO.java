package finalProj.dto.users;

import java.util.List;

import finalProj.domain.users.Units;
import finalProj.domain.users.Users;

public class UsersDTO {

	private Integer usersId;

	private String name;

	private String email;

	private List<Units> unit;

	public UsersDTO(Users user) {
		this.usersId = user.getUsersId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.unit = user.getUnit();
	}

	public Integer getUsersId() {
		return usersId;
	}

	public void setUsersId(Integer usersId) {
		this.usersId = usersId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Units> getUnit() {
		return unit;
	}

	public void setUnit(List<Units> unit) {
		this.unit = unit;
	}

}
