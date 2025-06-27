package finalProj.domain.finance;

import java.time.LocalDateTime;

import finalProj.domain.users.Users;

import jakarta.persistence.*;

@Entity
@Table(name = "Invoice_Response")
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

    @Column(name = "users_id")
    private Integer usersId;

    // 表格關係
    @ManyToOne
    @JoinColumn(name = "users_id", insertable = false, updatable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "invoice_id", insertable = false, updatable = false)
    private Invoice invoice;

    // getter setter
    public Integer getInvoiceResponseId() {
        return invoiceResponseId;
    }

    public Integer getUsersId() {
        return usersId;
    }

    public void setUsersId(Integer usersId) {
        this.usersId = usersId;
    }

    public void setInvoiceResponseIdd(Integer invoiceResponseId) {
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

}
