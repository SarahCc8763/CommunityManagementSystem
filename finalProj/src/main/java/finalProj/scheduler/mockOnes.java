package finalProj.scheduler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import finalProj.domain.finance.BillingPeriod;
import finalProj.repository.finance.BillingPeriodRepository;
import jakarta.transaction.Transactional;

public class mockOnes {
    @Autowired
    private BillingPeriodRepository billingPeriodRepository;

    /**
     * 測試用：每 5 分鐘執行一次，產生下個月與下個季度的 BillingPeriod
     */
    @Scheduled(fixedRate = 5 * 60 * 1000) // 每 5 分鐘
    @Transactional
    public void testGenerateBillingPeriods() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();

        // 產生下一個月的 BillingPeriod
        LocalDate nextMonth = now.plusMonths(1);
        generateMonthlyBillingPeriod(year, nextMonth.getMonthValue());

        // 根據目前月份判斷下一個季度
        int currentQuarter = (now.getMonthValue() - 1) / 3 + 1;
        int nextQuarter = currentQuarter == 4 ? 1 : currentQuarter + 1;
        int quarterStartMonth = (nextQuarter - 1) * 3 + 1;
        generateQuarterlyBillingPeriod(year + (nextQuarter == 1 ? 1 : 0), nextQuarter, quarterStartMonth);
    }

    private void generateMonthlyBillingPeriod(int year, int month) {
        String code = String.format("%02dM%d", year % 100, month);
        if (billingPeriodRepository.existsByPeriodCode(code))
            return;

        BillingPeriod period = new BillingPeriod();
        period.setPeriodCode(code);
        period.setPeriodName(String.format("%d第%d月", year, month));

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime dueDate = start.plusDays(1).atStartOfDay();

        period.setStartDate(start);
        period.setEndDate(end);
        period.setDueDate(dueDate);
        period.setCreatedAt(LocalDateTime.now());
        period.setLastUpdated(LocalDateTime.now());
        period.setCreatedBy(0);
        period.setUpdatedBy(0);
        period.setStatus(true);
        period.setNote("系統自動產生(月)");

        billingPeriodRepository.save(period);
    }

    private void generateQuarterlyBillingPeriod(int year, int quarter, int startMonth) {
        String code = String.format("%02dQ%d", year % 100, quarter);
        if (billingPeriodRepository.existsByPeriodCode(code))
            return;

        BillingPeriod period = new BillingPeriod();
        period.setPeriodCode(code);
        period.setPeriodName(String.format("%d第%d季", year, quarter));

        LocalDate start = LocalDate.of(year, startMonth, 1);
        LocalDate end = start.plusMonths(2).with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime dueDate = start.plusDays(1).atStartOfDay();

        period.setStartDate(start);
        period.setEndDate(end);
        period.setDueDate(dueDate);
        period.setCreatedAt(LocalDateTime.now());
        period.setLastUpdated(LocalDateTime.now());
        period.setCreatedBy(0);
        period.setUpdatedBy(0);
        period.setStatus(true);
        period.setNote("系統自動產生(季)");

        billingPeriodRepository.save(period);
    }

}
