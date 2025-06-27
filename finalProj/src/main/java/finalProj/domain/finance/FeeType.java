package finalProj.domain.finance;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.Table;

@Entity
@Table(name = "FeeType")

public class FeeType extends BaseEntity {

    // 費用類別流水號，主鍵
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fee_type_id")
    private Integer feeTypeId;

    // 費用類別名稱 管理費 清潔費
    @Column(nullable = false, length = 50)
    private String description;

    // 費用類別代碼 管理費ＭＧ 清潔費ＣＬ
    @Column(nullable = false, unique = true, length = 20, name = "fee_code")
    private String feeCode;

    // 一單位費用
    @Column(name = "amountPerUnit", precision = 8, scale = 2)
    private BigDecimal amountPerUnit;

    // 費用頻率 以月為單位１／６／１２
    @Column(length = 20)
    private String frequency;

    // 計費單位 坪數 戶數
    @Column(length = 20)
    private String unit;

    // ----------------------------------

    // 表格關係

    // ----------------------------------

    // Getters and Setters

    public Integer getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Integer feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public void setFeeTypeID(Integer feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFeeCode() {
        return feeCode;
    }

    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getAmountPerUnit() {
        return amountPerUnit;
    }

    public void setAmountPerUnit(BigDecimal amountPerUnit) {
        this.amountPerUnit = amountPerUnit;
    }

}
