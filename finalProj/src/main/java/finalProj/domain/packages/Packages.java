package finalProj.domain.packages;

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
@Table(name = "packages")
public class Packages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "packages_id")
    private Integer packagesId;

    @JsonBackReference("unitPackage")
    @ManyToOne
    @JoinColumn(name = "unit_id", referencedColumnName = "units_id")
    private Units unit;

    @Column(nullable = false)
    private Integer piece = 1;

    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;

    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;

    @Column(length = 30)
    private String status;

    @Column(length = 30)
    private String type;

    @Column(name = "sign", length = 500) // url
    private String sign;

    @Column(length = 30)
    private String place;

    @Column(name = "photo", length = 500) // url
    private String photo;

    @JsonBackReference("communityPackage")
    @ManyToOne
    @JoinColumn(name = "community_id", referencedColumnName = "id")
    private Community community;

    // --- Getters & Setters ---
    public Integer getPackagesId() {
        return packagesId;
    }

    public void setPackagesId(Integer packagesId) {
        this.packagesId = packagesId;
    }

    public Units getUnit() {
        return unit;
    }

    public void setUnit(Units unit) {
        this.unit = unit;
    }

    public Integer getPiece() {
        return piece;
    }

    public void setPiece(Integer piece) {
        this.piece = piece;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public LocalDateTime getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalDateTime pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Community getCommunity() {
        return community;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    // toString
    @Override
    public String toString() {
        return "Packages [packagesId=" + packagesId + ", unit=" + unit + ", piece=" + piece + ", arrivalTime="
                + arrivalTime + ", pickupTime=" + pickupTime + ", status=" + status + ", type=" + type + ", sign="
                + sign + ", place=" + place + ", photo=" + photo + ", community=" + community + ", getPackagesId()="
                + getPackagesId() + ", getUnit()=" + getUnit() + ", getPiece()=" + getPiece() + ", getArrivalTime()="
                + getArrivalTime() + ", getPickupTime()=" + getPickupTime() + ", getStatus()=" + getStatus()
                + ", getType()=" + getType() + ", getSign()=" + getSign() + ", getPlace()=" + getPlace()
                + ", getPhoto()=" + getPhoto() + ", getCommunity()=" + getCommunity() + "]";
    }

}
