package finalProj.domain.parking;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// 抽籤車位表
@Entity
@Table(name = "lottery_event_spaces")
public class LotteryEventSpace {

	// 流水號
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	// 多對一到抽籤活動
	@JsonBackReference("lotteryEvents-lotteryEventSpace")
	@ManyToOne
	@JoinColumn(name = "lottery_events_id", referencedColumnName = "bulletin_id", nullable = false)
	private LotteryEvents lotteryEvents;

	// 多對一到車位資料
	@JsonBackReference("parkingSlot-lotteryEventSpace")
	@ManyToOne
	@JoinColumn(name = "parking_slot_id", referencedColumnName = "id")
	private ParkingSlot parkingSlot;

	// ---------------------------------------------------------------------------------------

	// 一對多到抽籤車位申請
	@JsonManagedReference("lotteryEventSpace-lotteryApply")
	@OneToMany(mappedBy = "lotteryEventSpace", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LotteryApply> lotteryApply;

	// ---------------------------------------------------------------------------------------
	
	@Override
	public String toString() {
		return "LotteryEventSpace [id=" + id + ", lotteryEvents=" + lotteryEvents + ", parkingSlot=" + parkingSlot
				+ ", lotteryApply=" + lotteryApply + "]";
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LotteryEvents getLotteryEvents() {
		return lotteryEvents;
	}

	public void setLotteryEvents(LotteryEvents lotteryEvents) {
		this.lotteryEvents = lotteryEvents;
	}

	public ParkingSlot getParkingSlot() {
		return parkingSlot;
	}

	public void setParkingSlot(ParkingSlot parkingSlot) {
		this.parkingSlot = parkingSlot;
	}

	public List<LotteryApply> getLotteryApply() {
		return lotteryApply;
	}

	public void setLotteryApply(List<LotteryApply> lotteryApply) {
		this.lotteryApply = lotteryApply;
	}

}
