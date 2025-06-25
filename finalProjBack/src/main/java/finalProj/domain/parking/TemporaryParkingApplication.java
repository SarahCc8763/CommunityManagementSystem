package finalProj.domain.parking;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// 臨停申請紀錄
@Entity
@Table(name = "temporary_parking_applications")
public class TemporaryParkingApplication {

	// 流水號
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	// 拜訪戶號 Id
	@Column(name = "units_id", nullable = false)
	private Integer unitsId;

	// 訪客姓名
	@Column(name = "visitor_name", nullable = false, length = 10)
	private String visitorName;

	// 訪客車號
	@Column(name = "license_plate", nullable = false, length = 10)
	private String licensePlate;

	// 種類 Id
	@Column(name = "parking_type_id", nullable = false)
	private Integer parkingTypeId;

	// 申請日期
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "request_time")
	private Date requestTime;

	// 起始時間
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "start_time", nullable = false)
	private Date startTime;

	// 結束時間
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Taipei")
	@Column(name = "end_time", nullable = false)
	private Date endTime;

	// 申請原因
	@Column(name = "purpose", nullable = false, length = 50)
	private String purpose;

	// 申請車位 Id
	@Column(name = "parking_slot_id", nullable = false)
	private Integer parkingSlotId;

	@Override
	public String toString() {
		return "TemporaryParkingApplication [id=" + id + ", unitsId=" + unitsId + ", visitorName=" + visitorName
				+ ", licensePlate=" + licensePlate + ", parkingTypeId=" + parkingTypeId + ", requestTime=" + requestTime
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", purpose=" + purpose + ", parkingSlotId="
				+ parkingSlotId + "]";
	}

	// Getters and Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUnitsId() {
		return unitsId;
	}

	public void setUnitsId(Integer unitsId) {
		this.unitsId = unitsId;
	}

	public String getVisitorName() {
		return visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Integer getParkingTypeId() {
		return parkingTypeId;
	}

	public void setParkingTypeId(Integer parkingTypeId) {
		this.parkingTypeId = parkingTypeId;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public Integer getParkingSlotId() {
		return parkingSlotId;
	}

	public void setParkingSlotId(Integer parkingSlotId) {
		this.parkingSlotId = parkingSlotId;
	}
}
