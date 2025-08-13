package finalProj.dto.faq;

import java.util.List;

public class FaqResponse {
    private Boolean success;
    private String message;
    private Long count;
    private List<FaqDto> list;

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

    public List<FaqDto> getList() {
        return list;
    }

    public void setList(List<FaqDto> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "FaqResponse [success=" + success + ", message=" + message + ", count=" + count + ", list=" + list + "]";
    }

}
