package finalProj.domain.feedback;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedback_attachment")
public class FeedbackAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feedback_attachment_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    @JsonBackReference("feedback-attachment")
    private Feedback feedback;

    @Column(name = "feedback_attachment_file_name", nullable = false, length = 50)
    private String fileName;

    @Column(name = "feedback_attachment", nullable = false)
    @Lob
    private byte[] attachment;

    @Column(name = "feedback_attachment_mime_type", nullable = false, length = 20)
    private String mimeType;

    @Column(name = "feedback_attachment_file_size", nullable = false)
    private Integer fileSize;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "FeedbackAttachment{" +
                "id=" + id +
                ", fileName='" + fileName + "'" +
                ", mimeType='" + mimeType + "'" +
                ", fileSize=" + fileSize +
                ", feedbackId=" + (feedback != null ? feedback.getId() : null) +
                '}';
    }
}
