package kr.openbase.adcsmart.web.facade.monitoring;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.OBFaultMonitoring;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultSessionInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoSessionSearchOption;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMonitoringImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.FaultSessionInfoDto;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SessionMonitoringFacade
{
//	private static transient Logger log = LoggerFactory.getLogger(SessionMonitoringFacade.class);
	
	private OBFaultMonitoring faultMoniSvc;
	
	public SessionMonitoringFacade()
	{
		faultMoniSvc = new OBFaultMonitoringImpl();
	}
    public Integer getSessionListCount(OBDtoADCObject obj, OBDtoSearch searchObj) throws OBException
    {
         int result = 0;
        
         result = faultMoniSvc.getSessionListCount(obj, searchObj);
        
         return result;
    }
	public List<FaultSessionInfoDto> getFaultSessionList(OBDtoADCObject obj, OBDtoSearch searchObj, OBDtoOrdering orderObj) throws OBException
	{
		List<OBDtoFaultSessionInfo> listFromSvc = faultMoniSvc.searchSessionInfoListBySort(obj, searchObj, orderObj);		
		List<FaultSessionInfoDto> retVal = FaultSessionInfoDto.toFaultSessionInfoList(listFromSvc);		
		return retVal;
	}
	
	public void cleanLocalSessionList(OBDtoADCObject obj) throws OBException
	{
		faultMoniSvc.cleanLocalSessionList(obj);		
	}
//    public List<FaultSessionInfoDto> getFaultSessionSearchList(OBDtoADCObject obj, ArrayList<OBDtoSessionSearchOption> searchList, OBDtoSearch searchObj, OBDtoOrdering orderObj) throws OBException
//    {
//    	List<OBDtoFaultSessionInfo> listFromSvc = faultMoniSvc.searchSessionInfoList(obj, searchList, searchObj, orderObj);	
//    	List<FaultSessionInfoDto> retVal = FaultSessionInfoDto.toFaultSessionInfoList(listFromSvc);	
//    	log.debug("searchRowTotal:{}", retVal);
//    	return retVal;
//    }
    public Integer getFaultSessionSearchList(OBDtoADCObject obj, ArrayList<OBDtoSessionSearchOption> searchList, OBDtoSearch searchObj, OBDtoOrdering orderObj) throws OBException
    {
    	Integer retVal = faultMoniSvc.searchSessionInfoList(obj, searchList, searchObj, orderObj);	
    	return retVal;
    }
}