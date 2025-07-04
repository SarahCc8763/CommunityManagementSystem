package finalProj.domain.finance;

import java.time.LocalDate;
import java.time.LocalDateTime;
//import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BillingPeriod extends BaseEntity {

    // 繳交期別流水號
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "billing_period_id")
    private Integer billingPeriodId;

    // 期別名稱
    @Column(name = "period_name", nullable = false, unique = true, length = 50)
    private String periodName;

    // 期別代碼
    @Column(name = "period_code", nullable = false, unique = true, length = 20)
    private String periodCode;

    // 期別開始日
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    // 期別結束日
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    // 預設繳費截止日
    @Column(name = "due_date", length = 20)
    private LocalDateTime dueDate;

    // ----------------------------------

    public Integer getBillingPeriodId() {
        return billingPeriodId;
    }

    public void setBillingPeriodId(Integer billingPeriodId) {
        this.billingPeriodId = billingPeriodId;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getPeriodCode() {
        return periodCode;
    }

    public void setPeriodCode(String periodCode) {
        this.periodCode = periodCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

}
