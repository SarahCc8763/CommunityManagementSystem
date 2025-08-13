package finalProj.domain.finance;

import java.time.LocalDateTime;

import finalProj.domain.users.Users;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "finance_invoice_response")
public class InvoiceResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_response_id")
    private Integer invoiceResponseId;

    @Column(name = "last_response_time")
    private LocalDateTime lastResponseTime;

    @Column(name = "account_code", length = 10)
    private String accountCode;

    @Column(name = "last_response", columnDefinition = "varchar(max)")
    private String lastResponse;

    @Column(name = "verified")
    private Boolean verified;

    @Column(name = "verified_time")
    private LocalDateTime verifiedTime;

    @Column(name = "verified_by")
    private Integer verifiedBy;

    // 表格關係
    @ManyToOne
    @JoinColumn(name = "users_id", insertable = false, updatable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "invoice_id", insertable = false, updatable = false)
    @JsonBackReference
    private Invoice invoice;

    // getter setter
    public Integer getInvoiceResponseId() {
        return invoiceResponseId;
    }

    public void setInvoiceResponseId(Integer invoiceResponseId) {
        this.invoiceResponseId = invoiceResponseId;
    }

    public LocalDateTime getLastResponseTime() {
        return lastResponseTime;
    }

    public void setLastResponseTime(LocalDateTime lastResponseTime) {
        this.lastResponseTime = lastResponseTime;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getLastResponse() {
        return lastResponse;
    }

    public void setLastResponse(String lastResponse) {
        this.lastResponse = lastResponse;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getVerifiedTime() {
        return verifiedTime;
    }

    public void setVerifiedTime(LocalDateTime verifiedTime) {
        this.verifiedTime = verifiedTime;
    }

    public Integer getVerifiedBy() {
        return verifiedBy;
    }

    public void setVerifiedBy(Integer verifiedBy) {
        this.verifiedBy = verifiedBy;
    }

}
