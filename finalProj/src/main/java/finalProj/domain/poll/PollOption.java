
package finalProj.domain.poll;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "poll_option")
public class PollOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_option_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "poll_id")
    @JsonBackReference("poll-option")
    private Poll poll;

    @Column(name = "poll_option_text", nullable = false)
    private String text;

    @OneToMany(mappedBy = "option")
    @JsonIgnore
    private List<PollVote> votes;

    @Transient
    private Integer votesCount;

    public Integer getVotesCount() {
        if (votes == null)
            return 0;

        return (int) votes.stream()
                .filter(v -> Boolean.TRUE.equals(v.getIsChecked()))
                .count();
    }

    public void setVotesCount(Integer votesCount) {
        this.votesCount = votesCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "PollOption [id=" + id + ", poll=" + poll + ", text=" + text + ", votes=" + votes + "]";
    }

    public List<PollVote> getVotes() {
        return votes;
    }

    public void setVotes(List<PollVote> votes) {
        this.votes = votes;
    }
}
