package finalProj.domain.bulletin;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "bulletin_attachment")
public class BulletinAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulletin_attachment_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulletin_id", nullable = false)
    @JsonBackReference("bulletin-attachment")
    private Bulletin bulletin;

    @Column(name = "bulletin_attachment_file_name", nullable = false, length = 50)
    private String fileName;

    @Lob
    @Column(name = "bulletin_attachment", nullable = false)
    private byte[] fileData;

    @Column(name = "bulletin_attachment_mime_type", nullable = false, length = 20)
    private String mimeType;

    @Transient // 不存入資料庫，只作為接收 JSON 時用的欄位
    private String fileDataBase64;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Bulletin getBulletin() {
        return bulletin;
    }

    public void setBulletin(Bulletin bulletin) {
        this.bulletin = bulletin;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    // equals 和 hashCode 建議也實作一下
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BulletinAttachment))
            return false;
        BulletinAttachment that = (BulletinAttachment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BulletinAttachment [id=" + id + ", bulletin=" + bulletin + ", fileName=" + fileName + ", fileData="
                + Arrays.toString(fileData) + ", mimeType=" + mimeType + "]";
    }

    public String getFileDataBase64() {
        return fileDataBase64;
    }

    public void setFileDataBase64(String fileDataBase64) {
        this.fileDataBase64 = fileDataBase64;
    }

}
