package finalProj.domain.finance;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import finalProj.domain.users.Users;

import jakarta.persistence.*;

@Entity
@Table(name = "finance_receipt")
public class Receipt extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private Integer receiptId;

    @Column(name = "receipt_num")
    private String receiptNum;

    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "debit_at")
    private LocalDateTime debitAt;

    @Column(name = "amount_pay")
    private BigDecimal amountPay;

    @Column(name = "installments", length = 10)
    private String installments;
    // --------------------------------

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Integer getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Integer receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(String receiptNum) {
        this.receiptNum = receiptNum;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    public LocalDateTime getDebitAt() {
        return debitAt;
    }

    public void setDebitAt(LocalDateTime debitAt) {
        this.debitAt = debitAt;
    }

    public BigDecimal getAmountPay() {
        return amountPay;
    }

    public void setAmountPay(BigDecimal amountPay) {
        this.amountPay = amountPay;
    }

    public String getInstallments() {
        return installments;
    }

    public void setInstallments(String installments) {
        this.installments = installments;
    }

    // public FeeType getFeeType() {
    // return feeType;
    // }

    // public void setFeeType(FeeType feeType) {
    // this.feeType = feeType;
    // }

    // public InvoiceEvaluation getInvoiceEvaluation() {
    // return invoiceEvaluation;
    // }

    // public void setInvoiceEvaluation(InvoiceEvaluation invoiceEvaluation) {
    // this.invoiceEvaluation = invoiceEvaluation;
    // }

    // public Invoice getInvoice() {
    // return invoice;
    // }

    // public void setInvoice(Invoice invoice) {
    // this.invoice = invoice;
    // }

    // -----------------------------

}