package finalProj.domain.facilities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import finalProj.domain.community.Community;
import finalProj.domain.users.Units;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "point_transactions")
public class PointTransactionsBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;
    
    @ManyToOne
    @JoinColumn(name = "community_id", referencedColumnName = "id")
    @JsonBackReference("community-pointTransactions")
    private Community community;

    @ManyToOne
    @JoinColumn(name = "unit_id", referencedColumnName = "units_id")
    @JsonBackReference("units-pointTransactions")
    private Units unit;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "related_unit_id")
    private Integer relatedUnitId;
    
    @ManyToOne
    @JoinColumn(name = "related_reservation_id", referencedColumnName = "reservation_id")
    @JsonBackReference("facilityReservations-pointTransactions")
    private FacilityReservationsBean reservation;

    @Column(name = "transaction_description")
    private String transactionDescription;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    // ----------- Getter / Setter ------------

    public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public Community getCommunity() {
		return community;
	}

	public void setCommunity(Community community) {
		this.community = community;
	}

	public Units getUnit() {
		return unit;
	}

	public void setUnit(Units unit) {
		this.unit = unit;
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

	public Integer getRelatedUnitId() {
		return relatedUnitId;
	}

	public void setRelatedUnitId(Integer relatedUnitId) {
		this.relatedUnitId = relatedUnitId;
	}

	public FacilityReservationsBean getReservation() {
		return reservation;
	}

	public void setReservation(FacilityReservationsBean reservation) {
		this.reservation = reservation;
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
	
	// ----------- toString ------------
    @Override
    public String toString() {
        return "PointTransactionBean{" +
                "transactionId=" + transactionId +
                ", communityId=" + (community != null ? community.getCommunityId() : null) +
                ", unitId=" + (unit != null ? unit.getUnitsId() : null) +                
                ", changeType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", relatedUnitId=" + relatedUnitId +
                ", reservationId=" + (reservation != null ? reservation.getReservationId() : null) +
                ", transactionDescription='" + transactionDescription + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
