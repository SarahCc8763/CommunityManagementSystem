package finalProj.controller.facilities.process;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin
@Controller
@RequestMapping("/ecpay")
public class EcpayPrintResultController {	

	@PostMapping("/result")
	public String ecpayResultPage(@RequestParam Map<String, String> params, Model model) {
		model.addAttribute("merchantTradeNo", params.get("MerchantTradeNo"));
		model.addAttribute("amount", params.get("TradeAmt"));
		model.addAttribute("paymentDate", params.get("PaymentDate"));
		model.addAttribute("paymentType", params.get("PaymentType"));
		model.addAttribute("rtnCode", params.get("RtnCode"));
		model.addAttribute("rtnMsg", params.get("RtnMsg"));
		return "ecpay-result"; // 會載入 resources/templates/ecpay-result.html
	}
}
