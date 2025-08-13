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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "point_accounts")
public class PointAccountsBean {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Integer accountId;
	
	@ManyToOne
	@JoinColumn(name = "community_id", referencedColumnName = "id")
	@JsonBackReference("community-pointAccounts")
	private Community community;
	
	@OneToOne
	@JoinColumn(name = "unit_id", referencedColumnName = "units_id")
	@JsonBackReference("units-pointAccounts")
	private Units unit;

	@Column(name = "total_balance", nullable = false)
	private Integer totalBalance = 0;

	@Column(name = "expired_at", nullable = false)
	private LocalDateTime expiredAt;

	@Column(name = "is_active", nullable = false, columnDefinition = "bit default 1")
	private Boolean isActive = true;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();

	// ---------- Getters & Setters ----------

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
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

	public Integer getTotalBalance() {
		return totalBalance;
	}

	public void setTotalBalance(Integer totalBalance) {
		this.totalBalance = totalBalance;
	}

	public LocalDateTime getExpiredAt() {
		return expiredAt;
	}

	public void setExpiredAt(LocalDateTime expiredAt) {
		this.expiredAt = expiredAt;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	// ---------- toString ----------

	@Override
    public String toString() {
        return "PointAccountsBean{" +
                "accountId=" + accountId +
                ", communityId=" + (community != null ? community.getCommunityId() : null) +
                ", unitId=" + (unit != null ? unit.getUnitsId() : null) +
                ", totalBalance=" + totalBalance +
                ", expiredAt=" + expiredAt +
                ", isActive=" + isActive +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
