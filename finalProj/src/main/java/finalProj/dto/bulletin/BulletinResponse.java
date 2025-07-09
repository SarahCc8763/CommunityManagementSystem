package finalProj.dto.bulletin;

import java.util.List;

import finalProj.domain.bulletin.Bulletin;

public class BulletinResponse {
    private Boolean success;
    private String message;
    private Long count;
    private List<Bulletin> list;

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

    public List<Bulletin> getList() {
        return list;
    }

    public void setList(List<Bulletin> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "BulletinResponse [success=" + success + ", message=" + message + ", count=" + count + ", list=" + list
                + "]";
    }

}
