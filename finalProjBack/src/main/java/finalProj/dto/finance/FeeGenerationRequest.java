package finalProj.dto.finance;

import java.math.BigDecimal;

public class FeeGenerationRequest {

    private Integer billingPeriodId; // ✅ 必填
    private Integer feeTypeId; // ✅ 可填
    private Integer communityId; // ⬅ 篩選用
    private BigDecimal customRate; // ⬅ 自訂單價
    private Boolean previewOnly = false; // ⬅ 僅試算
    private String sourceNote; // ⬅ 來源說明（ex: "AUTO"）

    // Getters / Setters
    public Integer getBillingPeriodId() {
        return billingPeriodId;
    }

    public void setBillingPeriodId(Integer billingPeriodId) {
        this.billingPeriodId = billingPeriodId;
    }

    public Integer getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Integer feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public BigDecimal getCustomRate() {
        return customRate;
    }

    public void setCustomRate(BigDecimal customRate) {
        this.customRate = customRate;
    }

    public Boolean getPreviewOnly() {
        return previewOnly;
    }

    public void setPreviewOnly(Boolean previewOnly) {
        this.previewOnly = previewOnly;
    }

    public String getSourceNote() {
        return sourceNote;
    }

    public void setSourceNote(String sourceNote) {
        this.sourceNote = sourceNote;
    }
}
