package finalProj.dto.feedback;

import java.util.List;

import finalProj.domain.feedback.Feedback;

public class FeedbackResponse {
    private Boolean success;
    private String message;
    private Long count;
    private List<Feedback> list;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<Feedback> getList() {
        return list;
    }

    public void setList(List<Feedback> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "FaqResponse [success=" + success + ", message=" + message + ", count=" + count + ", list=" + list + "]";
    }

}
