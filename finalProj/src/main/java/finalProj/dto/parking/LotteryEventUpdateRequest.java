package finalProj.dto.parking;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

public class LotteryEventUpdateRequest {

	private Integer id;

	private String title;

	private String typeName;

	private Integer typeId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	private Date startedAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	private Date endedAt;

	// 承租開始時間
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
	private Date rentalStart;

	// 承租結束時間
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Taipei")
	private Date rentalEnd;

	private Integer usersId;

	private String usersName;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	private Date createdAt;

	private Set<LotteryEventSpaceDTO> parkingSlotIds;

	private Boolean status;

	public Date getRentalStart() {
		return rentalStart;
	}

	public void setRentalStart(Date rentalStart) {
		this.rentalStart = rentalStart;
	}

	public Date getRentalEnd() {
		return rentalEnd;
	}

	public void setRentalEnd(Date rentalEnd) {
		this.rentalEnd = rentalEnd;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getEndedAt() {
		return endedAt;
	}

	public void setEndedAt(Date endedAt) {
		this.endedAt = endedAt;
	}

	public Integer getUsersId() {
		return usersId;
	}

	public void setUsersId(Integer usersId) {
		this.usersId = usersId;
	}

	public Set<LotteryEventSpaceDTO> getParkingSlotIds() {
		return parkingSlotIds;
	}

	public void setParkingSlotIds(Set<LotteryEventSpaceDTO> parkingSlotIds) {
		this.parkingSlotIds = parkingSlotIds;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getUsersName() {
		return usersName;
	}

	public void setUsersName(String usersName) {
		this.usersName = usersName;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

}
