package finalProj.domain.finance;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import finalProj.domain.users.Users;

@Entity
@Table(name = "Invoice")
public class Invoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(name = "amount_due")
    private BigDecimal amountDue; // 原本為 Integer，建議改為 BigDecimal 以支援金額小數

    @Column(name = "period_name", length = 20)
    private String periodName;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "payment_plan", length = 20)
    private String paymentPlan;

    // 👉 單位數（如坪數、戶數、車位數）
    @Column(name = "unit_count", precision = 8, scale = 2)
    private BigDecimal unitCount;

    // 👉 每單位費用（複製自 FeeType.amountPerUnit 以紀錄當時價格）
    @Column(name = "unit_price", precision = 8, scale = 2)
    private BigDecimal unitPrice;

    // 👉 總金額（unitCount * unitPrice，寫入時可由 Service 計算）
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // ---------------------------------------

    @ManyToOne
    @JoinColumn(name = "fee_type_id")
    private FeeType feeType;

    @ManyToOne
    @JoinColumn(name = "billing_period_id")
    private BillingPeriod billingPeriod;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    @Column(name = "paymentStatus", length = 20)
    private String paymentStatus;// UNPAID, PENDING, PAID

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getPaymentPlan() {
        return paymentPlan;
    }

    public void setPaymentPlan(String paymentPlan) {
        this.paymentPlan = paymentPlan;
    }

    public BigDecimal getUnitCount() {
        return unitCount;
    }

    public void setUnitCount(BigDecimal unitCount) {
        this.unitCount = unitCount;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public FeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(FeeType feeType) {
        this.feeType = feeType;
    }

    public BillingPeriod getBillingPeriod() {
        return billingPeriod;
    }

    public void setBillingPeriod(BillingPeriod billingPeriod) {
        this.billingPeriod = billingPeriod;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // ----------------------------------------

    // ---------------------------------------

}
