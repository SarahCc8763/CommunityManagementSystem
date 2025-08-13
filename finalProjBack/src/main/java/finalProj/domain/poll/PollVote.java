
package finalProj.domain.poll;

import com.fasterxml.jackson.annotation.JsonBackReference;

import finalProj.domain.users.Users;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "poll_vote")
public class PollVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poll_vote_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "poll_id")
    @JsonBackReference("poll-vote")
    private Poll poll;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference("pollVote-user")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "poll_option_id")
    private PollOption option;

    @Column(name = "poll_vote_is_checked")
    private Boolean isChecked;

    @Transient
    private Integer voteUser;

    @Transient
    private Integer pollId;

    public Integer getPollId() {
        return pollId;
    }

    public void setPollId(Integer pollId) {
        this.pollId = pollId;
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

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public PollOption getOption() {
        return option;
    }

    public void setOption(PollOption option) {
        this.option = option;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "PollVote{id=" + id + ", isChecked=" + isChecked + "}";
    }

    public Integer getVoteUser() {
        voteUser = user.getUsersId();
        return voteUser;
    }

    public void setVoteUser(Integer voteUser) {
        this.voteUser = voteUser;
    }
}
