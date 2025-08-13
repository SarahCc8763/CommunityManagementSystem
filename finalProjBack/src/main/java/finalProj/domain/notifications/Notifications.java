package finalProj.domain.notifications;

import java.time.LocalDateTime;

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
@Table(name = "notifications")
public class Notifications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notifications_id")
    private Integer notificationsId;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "url", length = 500)
    private String url;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    @ManyToOne
    @JoinColumn(name = "community_id", referencedColumnName = "id")
    private Community community;

    // Getters and Setters

    public Integer getNotificationsId() {
        return notificationsId;
    }

    public void setNotificationsId(Integer notificationsId) {
        this.notificationsId = notificationsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
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
		return "Notifications [notificationsId=" + notificationsId + ", title=" + title + ", description=" + description
				+ ", url=" + url + ", createdTime=" + createdTime + ", community=" + community
				+ ", getNotificationsId()=" + getNotificationsId() + ", getTitle()=" + getTitle()
				+ ", getDescription()=" + getDescription() + ", getUrl()=" + getUrl() + ", getCreatedTime()="
				+ getCreatedTime() + ", getCommunity()=" + getCommunity() + "]";
	}    
    
}
