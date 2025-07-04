package finalProj.dto;

import java.time.LocalDateTime;
import java.util.List;

import finalProj.domain.faq.Faq;

public class FaqDto {

    private Integer id;
    private String question;
    private String answer;
    private String category;
    private LocalDateTime LastModified;
    private List<String> keywords;

    public FaqDto(Faq faq, List<String> keywords) {
        id = faq.getId();
        question = faq.getQuestion();
        answer = faq.getAnswer();
        category = faq.getCategory().getName();
        LastModified = faq.getLastModified();
        this.keywords = keywords;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getLastModified() {
        return LastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        LastModified = lastModified;
    }

    @Override
    public String toString() {
        return "FaqDto [id=" + id + ", question=" + question + ", answer=" + answer + ", category=" + category
                + ", LastModified=" + LastModified + ", keywords=" + keywords + "]";
    }

}
