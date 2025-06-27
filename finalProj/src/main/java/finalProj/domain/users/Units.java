package finalProj.domain.users;

import java.math.BigDecimal;

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
@Table(name = "units")
public class Units {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "units_id")
    private Integer unitsId;

    @Column(nullable = false, length = 10)
    private String unit;

    @Column(nullable = false, length = 10)
    private String floor;

    @Column(length = 10)
    private String building;

    @Column(nullable = false, precision = 6, scale = 2)
    private BigDecimal ping;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer point = 0;

    @ManyToOne
    @JoinColumn(name = "community_id", referencedColumnName = "id")
    private Community community;

    // --- Getters and Setters ---
    public Integer getUnitsId() {
        return unitsId;
    }

    public void setUnitsId(Integer unitsId) {
        this.unitsId = unitsId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public BigDecimal getPing() {
        return ping;
    }

    public void setPing(BigDecimal ping) {
        this.ping = ping;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    // Optional: toString()
    @Override
    public String toString() {
        return "Units [unitsId=" + unitsId + ", unit=" + unit + ", floor=" + floor + ", building=" + building
                + ", ping=" + ping + ", point=" + point + ", community=" + community + ", getUnitsId()=" + getUnitsId()
                + ", getUnit()=" + getUnit() + ", getFloor()=" + getFloor() + ", getBuilding()=" + getBuilding()
                + ", getPing()=" + getPing() + ", getPoint()=" + getPoint() + ", getCommunity()=" + getCommunity()
                + "]";
    }

}
