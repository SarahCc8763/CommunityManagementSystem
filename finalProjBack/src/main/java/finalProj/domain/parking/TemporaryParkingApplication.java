package finalProj.domain.parking;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import finalProj.domain.users.Units;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	// 多對一到拜訪戶號
	@JsonBackReference("community-temporaryParking")
	@ManyToOne
	@JoinColumn(name = "units_id", referencedColumnName = "units_id", nullable = false)
	private Units units;

	// 訪客姓名
	@Column(name = "visitor_name", nullable = false, length = 10)
	private String visitorName;

	// 訪客車號
	@Column(name = "license_plate", nullable = false, length = 10)
	private String licensePlate;

	// 多對一到種類
	@JsonBackReference("parkingType-temporaryParking")
	@ManyToOne
	@JoinColumn(name = "type_id", referencedColumnName = "id", nullable = false)
	private ParkingType parkingType;

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

	// 多對一到申請車位
	@JsonBackReference("parkingSlot-temporaryParking")
	@ManyToOne
	@JoinColumn(name = "parking_slot_id", referencedColumnName = "id", nullable = false)
	private ParkingSlot parkingSlot;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Units getUnits() {
		return units;
	}

	public void setUnits(Units units) {
		this.units = units;
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

	public ParkingType getParkingType() {
		return parkingType;
	}

	public void setParkingType(ParkingType parkingType) {
		this.parkingType = parkingType;
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

	public ParkingSlot getParkingSlot() {
		return parkingSlot;
	}

	public void setParkingSlot(ParkingSlot parkingSlot) {
		this.parkingSlot = parkingSlot;
	}

}
