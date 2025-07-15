package finalProj.service.facilities.process.pointTopup;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import finalProj.dto.facilities.topup.TopupRequest;
import finalProj.util.EcpayUtils;

@Service
public class PointTopupServiceImpl implements PointTopupService {

	private static final String MERCHANT_ID = "2000132";
	private static final String HASH_KEY = "5294y06JbISpM5x9";
	private static final String HASH_IV = "v77hoKGq4kWxNNIS";
	private static final String ACTION_URL = "https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5";

	@Override
	public String generateEcpayForm(TopupRequest request) throws Exception {
		String merchantTradeNo = "TOPUP" + System.currentTimeMillis();
		String merchantTradeDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		String ngrokPath = "https://3c015cdc44cc.ngrok-free.app";

		Map<String, String> params = new LinkedHashMap<>();
		params.put("MerchantID", MERCHANT_ID);
		params.put("MerchantTradeNo", merchantTradeNo);
		params.put("MerchantTradeDate", merchantTradeDate);
		params.put("ChoosePayment", "Credit");// 信用卡付款
		params.put("PaymentType", "aio");
		params.put("TotalAmount", String.valueOf(request.getAmount()));
		params.put("TradeDesc", "GoodHouse");
		params.put("ItemName", "Topup_value" + request.getAmount());
		params.put("CustomField1", String.valueOf(request.getAccountId()));
		params.put("CustomField2", String.valueOf(request.getUnitId()));		
		params.put("ReturnURL", ngrokPath + "/ecpay/notify");
		params.put("OrderResultURL", ngrokPath + "/ecpay/result");
		params.put("NeedExtraPaidInfo", "Y");

		String checkMacValue = EcpayUtils.generateCheckMacValue(params, HASH_KEY, HASH_IV);
		params.put("CheckMacValue", checkMacValue);
		System.out.println("========== 綠界送出參數 ==========");
		params.forEach((k, v) -> System.out.println(k + " = " + v));
		System.out.println("==================================");
		return buildAutoSubmitForm(params);
	}

	private String buildAutoSubmitForm(Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<form id='ecpay-form' method='POST' action='").append(ACTION_URL).append("'>");
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append("<input type='hidden' name='").append(entry.getKey()).append("' value='").append(entry.getValue())
					.append("' />");
		}
		sb.append("</form>");
		sb.append("<script>document.getElementById('ecpay-form').submit();</script>");
		return sb.toString();
	}

}
