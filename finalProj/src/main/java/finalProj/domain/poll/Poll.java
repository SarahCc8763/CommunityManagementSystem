
package finalProj.domain.poll;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.*;

import finalProj.domain.bulletin.Bulletin;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "poll")
public class Poll {

    @Id
    @Column(name = "poll_id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "poll_id", nullable = false)
    @JsonBackReference("bulletin-poll")
    private Bulletin bulletin;

    @Column(name = "poll_title", nullable = false)
    private String title;

    @Column(name = "poll_start")
    private LocalDateTime start;

    @Column(name = "poll_end")
    private LocalDateTime end;

    @Column(name = "poll_is_multiple")
    private Boolean isMultiple;

    @OneToMany(mappedBy = "poll")
    @JsonManagedReference("poll-option")
    private List<PollOption> options;

    @OneToMany(mappedBy = "poll")
    @JsonManagedReference("poll-vote")
    private List<PollVote> votes;

    public Bulletin getBulletin() {
        return bulletin;
    }

    public void setBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
    }

    public List<PollVote> getVotes() {
        return votes;
    }

    public void setVotes(List<PollVote> votes) {
        this.votes = votes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Boolean getIsMultiple() {
        return isMultiple;
    }

    public void setIsMultiple(Boolean isMultiple) {
        this.isMultiple = isMultiple;
    }

    public List<PollOption> getOptions() {
        return options;
    }

    public void setOptions(List<PollOption> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "Poll [bulletinId=" + bulletin + ", title=" + title + ", start=" + start + ", end=" + end
                + ", isMultiple=" + isMultiple + ", options=" + options + ", votes=" + votes + "]";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
