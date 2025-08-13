package finalProj.controller.facilities.process;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import finalProj.domain.community.Community;
import finalProj.domain.facilities.PointAccountsBean;
import finalProj.domain.facilities.PointTransactionsBean;
import finalProj.domain.notifications.Notifications;
import finalProj.domain.notifications.UnitsNotifications;
import finalProj.domain.users.Units;
import finalProj.repository.notifications.NotificationsRepository;
import finalProj.repository.notifications.UnitsNotificationsRepository;
import finalProj.service.facilities.PointAccountsService;
import finalProj.service.facilities.PointTransactionsService;
import finalProj.util.EcpayUtils;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/ecpay")
public class EcpayCallbackController {

	@Autowired
	private PointAccountsService pointAccountsService;

	@Autowired
	private PointTransactionsService pointTransactionsService;

	@Autowired
    NotificationsRepository notificationsRepository;

    @Autowired
    UnitsNotificationsRepository unitsNotificationsRepository;

	private static final String HASH_KEY = "5294y06JbISpM5x9";
	private static final String HASH_IV = "v77hoKGq4kWxNNIS";

	@PostMapping("/notify")
	public ResponseEntity<String> handlePaymentNotify(HttpServletRequest request) {
		try {
			// ✅ Step 1: 將參數轉成 Map<String, String>
			Map<String, String> paramMap = new HashMap<>();
			request.getParameterMap().forEach((key, values) -> {
				if (values.length > 0 && values[0] != null && !values[0].isEmpty()) {
					paramMap.put(key, values[0]);
				}
			});

			System.out.println("===== Step 1: 實際收到參數（重建參數表）=====");
			paramMap.forEach((k, v) -> System.out.println(k + " = " + v));
			System.out.println("✅ 實際參與驗證欄位共：" + paramMap.size() + " 個");
			System.out.println("====================================");

			// ✅ Step 2: 擷取關鍵欄位
			String receivedCheckMacValue = paramMap.get("CheckMacValue");
			String merchantTradeNo = paramMap.get("MerchantTradeNo");
			int amount = Integer.parseInt(paramMap.get("TradeAmt"));
			String paymentDateStr = paramMap.get("PaymentDate");
			int accountId = Integer.parseInt(paramMap.get("CustomField1"));
			int unitId = Integer.parseInt(paramMap.get("CustomField2"));

			// ✅ Step 3: 驗證 CheckMacValue（需移除 CheckMacValue 本身）
			Map<String, String> forCheckMac = new HashMap<>(paramMap);
			forCheckMac.remove("CheckMacValue");

			String calculatedCheckMacValue = EcpayUtils.generateCheckMacValue(forCheckMac, HASH_KEY, HASH_IV);

			System.out.println("👉 收到的 CheckMacValue（原始）：" + receivedCheckMacValue);
			System.out.println("👉 計算出的 CheckMacValue（後端）：" + calculatedCheckMacValue);

			// ✅ Step 4: 儲存交易紀錄 + 加點
			LocalDateTime paymentTime = LocalDateTime.parse(paymentDateStr,
					DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
			System.out.println("📅 付款時間解析成功：" + paymentTime);

			PointAccountsBean account = pointAccountsService.findById(accountId);
			Units unit = account.getUnit();
			Community community = account.getCommunity();

			// 更新帳戶餘額
			account.setTotalBalance(account.getTotalBalance() + amount);
			account.setUpdatedAt(LocalDateTime.now());
			LocalDateTime expireAt = LocalDate.now()
					.plusMonths(1)
					.with(TemporalAdjusters.lastDayOfMonth())
					.atTime(23, 59, 59);
			account.setExpiredAt(expireAt);
			pointAccountsService.update(account);

			// 建議交易紀錄
			PointTransactionsBean tx = new PointTransactionsBean();
			tx.setUnit(unit);
			tx.setCommunity(community);
			tx.setTransactionType("topup");
			tx.setAmount(amount);
			tx.setTransactionDescription("ECPay付款：" + merchantTradeNo);
			tx.setCreatedAt(LocalDateTime.now());
			pointTransactionsService.create(tx);


			// === 建立儲值 Notifications ===
			Notifications notification = new Notifications();
			notification.setTitle("儲值成功");
			notification.setDescription("您已成功透過綠界儲值" + amount + "點");	
			notification.setCreatedTime(LocalDateTime.now());
			notification.setCommunity(unit.getCommunity());
			notificationsRepository.save(notification);
			
			UnitsNotifications unitsNotifications = new UnitsNotifications();
			unitsNotifications.setNotifications(notification);
			unitsNotifications.setUnits(unit);
			unitsNotificationsRepository.save(unitsNotifications);


			System.out.println("✅ Step 4 成功儲值：" + amount + " 點，帳戶 ID：" + accountId + "，訂單：" + merchantTradeNo);

			// ✅ Step 5: 回傳 1|OK 給綠界
			return ResponseEntity.ok("1|OK");

		} catch (Exception e) {
			System.out.println("❌ Step Exception 發生例外：" + e.getMessage());
			e.printStackTrace();
			return ResponseEntity.ok("0|Exception");
		}
	}

}
