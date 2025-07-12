package finalProj.dto.facilities.transfer;

import java.time.LocalDateTime;

public class PointTransferRequest {
	
	private Integer fromUnitId;
	private Integer toUnitId;
	private Integer amount;	
	
	
	public Integer getFromUnitId() {
		return fromUnitId;
	}

	public void setFromUnitId(Integer fromUnitId) {
		this.fromUnitId = fromUnitId;
	}

	public Integer getToUnitId() {
		return toUnitId;
	}

	public void setToUnitId(Integer toUnitId) {
		this.toUnitId = toUnitId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}


	
}
