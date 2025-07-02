package finalProj.domain.users;

import com.fasterxml.jackson.annotation.JsonBackReference;

import finalProj.domain.community.Community;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "units_users")
public class UnitsUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unitsusers_id")
    private Integer unitsUsersId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "users_id", nullable = false)
    @JsonBackReference("unitsUsers")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "unit_id", referencedColumnName = "units_id", nullable = false)
    @JsonBackReference("unitsUsersList")
    private Units unit;

    @ManyToOne
    @JoinColumn(name = "community_id", referencedColumnName = "id")
    private Community community;

    // --- Getters and Setters ---
    public Integer getUnitsUsersId() {
        return unitsUsersId;
    }

    public void setUnitsUsersId(Integer unitsUsersId) {
        this.unitsUsersId = unitsUsersId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Units getUnit() {
        return unit;
    }

    public void setUnit(Units unit) {
        this.unit = unit;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    //  toString
	@Override
	public String toString() {
		return "UnitsUsers [unitsUsersId=" + unitsUsersId + ", user=" + user + ", unit=" + unit + ", community="
				+ community + ", getUnitsUsersId()=" + getUnitsUsersId() + ", getUser()=" + getUser() + ", getUnit()="
				+ getUnit() + ", getCommunity()=" + getCommunity() + "]";
	}
    
    
}
