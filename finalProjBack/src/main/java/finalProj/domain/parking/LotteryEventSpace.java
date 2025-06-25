package finalProj.domain.parking;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    // 活動 Id
    @Column(name = "lottery_events_id", nullable = false)
    private Integer lotteryEventsId;

    // 登記車位 Id
    @Column(name = "parking_slot_id", nullable = false)
    private Integer parkingSlotId;

    @Override
	public String toString() {
		return "LotteryEventSpace [id=" + id + ", lotteryEventsId=" + lotteryEventsId + ", parkingSlotId="
				+ parkingSlotId + "]";
	}

	// Getters and Setters
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLotteryEventsId() {
		return lotteryEventsId;
	}

	public void setLotteryEventsId(Integer lotteryEventsId) {
		this.lotteryEventsId = lotteryEventsId;
	}

	public Integer getParkingSlotId() {
		return parkingSlotId;
	}

	public void setParkingSlotId(Integer parkingSlotId) {
		this.parkingSlotId = parkingSlotId;
	}
}
