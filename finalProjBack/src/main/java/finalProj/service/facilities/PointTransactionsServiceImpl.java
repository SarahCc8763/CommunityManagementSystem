package finalProj.service.facilities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import finalProj.domain.facilities.PointTransactionsBean;
import finalProj.dto.facilities.reservations.PointTransactionsRecordDTO;
import finalProj.repository.facilities.PointTransactionsRepository;

@Service
@Transactional
public class PointTransactionsServiceImpl implements PointTransactionsService {

	@Autowired
	private PointTransactionsRepository pointTransactionsRepository;

	@Override
	public PointTransactionsBean create(PointTransactionsBean transaction) {
		if (transaction.getCreatedAt() == null) {
	        transaction.setCreatedAt(LocalDateTime.now());
	    }
		return pointTransactionsRepository.save(transaction);
	}
	

	@Override
	public List<PointTransactionsBean> findByUnit_UnitsId(Integer unitId) {
		return pointTransactionsRepository.findByUnit_UnitsId(unitId);
	}

	@Override
	public List<PointTransactionsRecordDTO> findDTOsByUnitId(Integer unitId) {
		 List<PointTransactionsBean> beans = pointTransactionsRepository.findByUnit_UnitsId(unitId);

		    return beans.stream().map(bean -> {
		    	PointTransactionsRecordDTO dto = new PointTransactionsRecordDTO();
		        dto.setTransactionId(bean.getTransactionId());
		        dto.setTransactionType(bean.getTransactionType());
		        dto.setAmount(bean.getAmount());
		        dto.setTransactionDescription(bean.getTransactionDescription());
		        dto.setCreatedAt(bean.getCreatedAt());
		        dto.setRelatedReservationId( bean.getReservation() != null ?  
		        		bean.getReservation().getReservationId() : null);

		        // 若有 relatedUnitId，轉成門牌或名稱顯示
//		        if (bean.getRelatedUnitId() != null) {
//		            Units relatedUnit = unitsRepository.findById(bean.getRelatedUnitId()).orElse(null);
//		            if (relatedUnit != null) {
//		                dto.setRelatedUnitName(relatedUnit.getDoorplate()); // 假設 doorplate 是門牌欄位
//		            }
//		        }

		        return dto;
		    }).collect(Collectors.toList());	
		
	}
}
