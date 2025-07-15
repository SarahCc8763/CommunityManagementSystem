package finalProj.dto.facilities.transfer;

public class UnitOptionDTO {

	private Integer id;
	private String label;
	private String userName;

	public UnitOptionDTO(Integer id, String label, String userName) {
		this.id = id;
		this.label = label;
		this.userName = userName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
