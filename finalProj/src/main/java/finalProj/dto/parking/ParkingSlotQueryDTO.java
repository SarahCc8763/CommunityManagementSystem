package finalProj.dto.parking;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ParkingSlotQueryDTO {
	
	private Integer communityId;
	
    private String typeName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
    private Date eventStart;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
    private Date eventEnd;
    
    private Integer limit;

	public Integer getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Integer communityId) {
		this.communityId = communityId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Date getEventStart() {
		return eventStart;
	}

	public void setEventStart(Date eventStart) {
		this.eventStart = eventStart;
	}

	public Date getEventEnd() {
		return eventEnd;
	}

	public void setEventEnd(Date eventEnd) {
		this.eventEnd = eventEnd;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}

