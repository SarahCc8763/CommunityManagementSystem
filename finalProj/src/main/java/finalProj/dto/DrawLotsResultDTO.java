package finalProj.dto;

import java.util.List;

public class DrawLotsResultDTO {
	private Integer eventId;
	private int totalApplicants;
	private int totalSpaces;
	private List<WinnerDTO> winners;

	public DrawLotsResultDTO(Integer eventId, int totalApplicants, int totalSpaces, List<WinnerDTO> winners) {
		this.eventId = eventId;
		this.totalApplicants = totalApplicants;
		this.totalSpaces = totalSpaces;
		this.winners = winners;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public int getTotalApplicants() {
		return totalApplicants;
	}

	public void setTotalApplicants(int totalApplicants) {
		this.totalApplicants = totalApplicants;
	}

	public int getTotalSpaces() {
		return totalSpaces;
	}

	public void setTotalSpaces(int totalSpaces) {
		this.totalSpaces = totalSpaces;
	}

	public List<WinnerDTO> getWinners() {
		return winners;
	}

	public void setWinners(List<WinnerDTO> winners) {
		this.winners = winners;
	}

}
