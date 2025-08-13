package finalProj.dto.facilities.reservations;

import java.time.LocalDate;
import java.time.LocalTime;

public class AvailableReservationSlotResponse {
	
	//這是輸出某1公設可預訂的slot，給前端表列哪些時段可以預約
	
	private LocalDate date;
	private LocalTime time;
	private Integer maxUsers; // 可為 null
	private Integer reservedUsers; // 可為 null
	private boolean available;

	public AvailableReservationSlotResponse(LocalDate date, LocalTime time, Integer maxUsers, Integer reservedUsers,
			boolean available) {
		this.date = date;
		this.time = time;
		this.maxUsers = maxUsers;
		this.reservedUsers = reservedUsers;
		this.available = available;
	}

	// getters and setters
	
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public Integer getMaxUsers() {
		return maxUsers;
	}

	public void setMaxUsers(Integer maxUsers) {
		this.maxUsers = maxUsers;
	}

	public Integer getReservedUsers() {
		return reservedUsers;
	}

	public void setReservedUsers(Integer reservedUsers) {
		this.reservedUsers = reservedUsers;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}


}
