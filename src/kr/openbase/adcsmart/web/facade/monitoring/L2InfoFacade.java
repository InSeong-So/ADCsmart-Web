package kr.openbase.adcsmart.web.facade.monitoring;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBFaultMonitoring;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.fault.OBDtoL2SearchInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoL2SearchOption;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMonitoringImpl;
import kr.openbase.adcsmart.service.utility.OBException;

@Component
public class L2InfoFacade
{
	private static transient Logger log = LoggerFactory.getLogger(L2InfoFacade.class);
	
	private OBFaultMonitoring faultMonitoringSvc;
	
	public L2InfoFacade()
	{
		faultMonitoringSvc = new OBFaultMonitoringImpl();
	}
	
	// L2 정보의 총 개수를 제공
	public Integer loadL2InfoListTotal(OBDtoADCObject object) throws OBException, Exception
	{
		return faultMonitoringSvc.getL2InfoTotalCount(object);
	}
	
	// L2 정보를 ADC로부터 추출 후 DB에 저장후 저장된 결과를 조건에 맞게 제공
	public ArrayList<OBDtoL2SearchInfo> getL2InfoList(OBDtoADCObject object, ArrayList<OBDtoL2SearchOption> searchKeyList, OBDtoSearch pagingOption) throws OBException, Exception
	{
		ArrayList<OBDtoL2SearchInfo> l2InfoListFromSvc = new ArrayList<OBDtoL2SearchInfo>();
		l2InfoListFromSvc = faultMonitoringSvc.searchL2InfoList(object, searchKeyList, pagingOption);
		
		log.debug("l2InfoListFromSvc: {}", l2InfoListFromSvc);
		return l2InfoListFromSvc;
	}
	
	// L2 정보를 ADC로부터 추출 후 DB에 저장후 저장된 결과를 조건에 맞게 제공
	public void cleanLocalL2List(OBDtoADCObject object) throws OBException, Exception
	{
		faultMonitoringSvc.cleanLocalL2List(object);
	}
	
	// L2 정보를 페이징, 정렬 조건에 맞게 제공한다.
	public ArrayList<OBDtoL2SearchInfo> getL2InfoListBySort(OBDtoADCObject object, OBDtoSearch pagingOption, OBDtoOrdering orderOption) throws OBException, Exception
	{
		ArrayList<OBDtoL2SearchInfo> l2InfoListFromSvc = new ArrayList<OBDtoL2SearchInfo>();
		l2InfoListFromSvc = faultMonitoringSvc.searchL2InfoListBySort(object, pagingOption, orderOption);
		
		log.debug("l2InfoListFromSvc: {}", l2InfoListFromSvc);
		return l2InfoListFromSvc;
	}	
}