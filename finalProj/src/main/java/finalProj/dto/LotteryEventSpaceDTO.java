package finalProj.dto;

import java.util.Objects;

public class LotteryEventSpaceDTO {
	private Integer id;
	private Integer parkingSlotId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParkingSlotId() {
		return parkingSlotId;
	}

	public void setParkingSlotId(Integer parkingSlotId) {
		this.parkingSlotId = parkingSlotId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof LotteryEventSpaceDTO))
			return false;
		LotteryEventSpaceDTO that = (LotteryEventSpaceDTO) o;
		return Objects.equals(parkingSlotId, that.parkingSlotId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(parkingSlotId);
	}
}
