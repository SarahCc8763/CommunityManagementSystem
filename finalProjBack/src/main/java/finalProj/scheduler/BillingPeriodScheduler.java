package finalProj.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import finalProj.domain.finance.BillingPeriod;
import finalProj.repository.finance.BillingPeriodRepository;
import jakarta.transaction.Transactional;

@Component
public class BillingPeriodScheduler {

    @Autowired
    private BillingPeriodRepository billingPeriodRepository;

    // 每年 6 月與 12 月的 10 號早上 9:00 執行，建立下半年的月期別
    @Scheduled(cron = "0 0 9 10 6,12 ?")
    @Transactional
    public void generateHalfYearlyBillingPeriods() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int shortYear = year % 100;

        int startMonth = (now.getMonthValue() == 6) ? 7 : 1;

        for (int i = 0; i < 6; i++) {
            int month = startMonth + i;
            YearMonth ym = YearMonth.of(year, month);
            String periodCode = String.format("%02dM%d", shortYear, month);
            String periodName = String.format("%d 第%d月", year, month);

            if (!billingPeriodRepository.existsByPeriodCode(periodCode)) {
                BillingPeriod bp = new BillingPeriod();
                bp.setPeriodCode(periodCode);
                bp.setPeriodName(periodName);
                bp.setStartDate(ym.atDay(1));
                bp.setEndDate(ym.atEndOfMonth());
                bp.setDueDate(ym.atDay(2).atStartOfDay());
                bp.setCreatedAt(LocalDateTime.now());
                bp.setLastUpdated(LocalDateTime.now());
                bp.setCreatedBy(0); // 系統建立
                bp.setUpdatedBy(0);
                bp.setStatus(true);
                bp.setNote("系統自動產生");
                billingPeriodRepository.save(bp);
            }
        }
    }

    // 每年 1、5、9 月的 10 號早上 9:00 執行，建立季度期別
    @Scheduled(cron = "0 0 9 10 1,5,9 ?")
    @Transactional
    public void generateQuarterlyBillingPeriods() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int shortYear = year % 100;
        int month = now.getMonthValue();

        int quarterNumber = (month == 1) ? 1 : (month == 5) ? 2 : 3;
        int startMonth = (quarterNumber - 1) * 3 + 1;
        int endMonth = startMonth + 2;

        YearMonth startYm = YearMonth.of(year, startMonth);
        YearMonth endYm = YearMonth.of(year, endMonth);

        String periodCode = String.format("%02dQ%d", shortYear, quarterNumber);
        String periodName = String.format("%d 第%d季", year, quarterNumber);

        if (!billingPeriodRepository.existsByPeriodCode(periodCode)) {
            BillingPeriod bp = new BillingPeriod();
            bp.setPeriodCode(periodCode);
            bp.setPeriodName(periodName);
            bp.setStartDate(startYm.atDay(1));
            bp.setEndDate(endYm.atEndOfMonth());
            bp.setDueDate(startYm.atDay(2).atStartOfDay());
            bp.setCreatedAt(LocalDateTime.now());
            bp.setLastUpdated(LocalDateTime.now());
            bp.setCreatedBy(0);
            bp.setUpdatedBy(0);
            bp.setStatus(true);
            bp.setNote("系統自動產生");
            billingPeriodRepository.save(bp);
        }
    }
}
