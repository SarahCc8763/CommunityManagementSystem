package finalProj.dto.facilities.reservations;

import java.time.LocalDateTime;

public class PointTransactionsRecordDTO {
	private Integer transactionId; // 交易編號
	private String transactionType; // 類型：預約、取消、加值、轉入、轉出
	private Integer amount; // 點數（正負）
	private String transactionDescription; // 備註
	private LocalDateTime createdAt; // 發生時間
	// 附加欄位
	private Integer relatedReservationId; // 若為預約相關，可帶入 reservationId
	private String relatedUnitName; // 若是轉點，可顯示對象住戶門牌（轉出/轉入）
	
	public Integer getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getTransactionDescription() {
		return transactionDescription;
	}
	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public Integer getRelatedReservationId() {
		return relatedReservationId;
	}
	public void setRelatedReservationId(Integer relatedReservationId) {
		this.relatedReservationId = relatedReservationId;
	}
	public String getRelatedUnitName() {
		return relatedUnitName;
	}
	public void setRelatedUnitName(String relatedUnitName) {
		this.relatedUnitName = relatedUnitName;
	}
	
}
