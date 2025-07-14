package finalProj.service.facilities.process.pointTopup;

import finalProj.dto.facilities.topup.TopupRequest;

public interface PointTopupService {
	
	String generateEcpayForm(TopupRequest request) throws Exception;
	
}
