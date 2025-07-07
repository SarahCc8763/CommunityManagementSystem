package finalProj.domain.finance;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "finance_Expense")
public class Expense extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Integer expenseId;

    @Column(name = "category", length = 20)
    private String category;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "paid_on")
    private LocalDateTime paidOn;

    @Column(name = "paid_by", length = 20)
    private String paidBy;

    @Column(name = "receipt")
    private byte[] receipt;

    @Column(name = "vendor_id")
    private Integer vendorId;

    // getter Setter

    public Integer getExpenseId() {
        return expenseId;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(LocalDateTime paidOn) {
        this.paidOn = paidOn;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public byte[] getReceipt() {
        return receipt;
    }

    public void setReceipt(byte[] receipt) {
        this.receipt = receipt;
    }

}
