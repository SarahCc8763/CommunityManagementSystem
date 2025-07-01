package finalProj.dto.community;

import java.util.List;

public class CommunityDTO {

	private String name;
	private String address;
	private List<String> functions;
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
	public List<String> getFunctions() {
		return functions;
	}
	public void setFunctions(List<String> functions) {
		this.functions = functions;
	}

	

}
