package kr.openbase.adcsmart.web.facade.monitoring;

import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBAdcManagement;
import kr.openbase.adcsmart.service.OBFaultMonitoring;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.fault.OBDtoAdcCurrentSession;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCpuDataObj;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCpuHistory;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultDataObj;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultHWStatus;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcMonCpuDataObj;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMonitoringImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.util.NumberUtil;

@Component
public class ApplianceMonitorFacade
{
	//private static transient Logger log = LoggerFactory.getLogger(ApplianceMonitorFacade.class);
	
	private OBFaultMonitoring faultMonitoringSvc;
	private OBAdcManagement adcManagementSvc;
	
	public ApplianceMonitorFacade()
	{
		faultMonitoringSvc = new OBFaultMonitoringImpl();
		adcManagementSvc = new OBAdcManagementImpl();		
	}	
	public OBDtoAdcInfo getAdcInfo(Integer adcIndex) throws Exception
	{				
		OBDtoAdcInfo svc = adcManagementSvc.getAdcInfo(adcIndex);
		OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
		// 여기서는 모델명을 Integer 형으로 변경하여 사용하기 때문에. adcType에 임시 저장합니다.
		
		if (svc.getModel() != null && !svc.getModel().isEmpty())
		{
    		if (svc.getModel().endsWith("p"))
    		{
    		    adcInfo.setAdcType(NumberUtil.extractNumbertoString(svc.getModel())+1);
    		}
    		else
    		{
    		    adcInfo.setAdcType(NumberUtil.extractNumbertoString(svc.getModel()));
    		}
		}
		else
		{
		    adcInfo.setAdcType(NumberUtil.extractNumbertoString(svc.getModel()));
		}
		
		if(svc.getSwVersion() != null)
		{
			adcInfo.setSwVersion(svc.getSwVersion());
		}
		else
		{
			adcInfo.setSwVersion("");
		}
		adcInfo.setStatus(svc.getStatus());
		adcInfo.setRoleFlbYn(svc.getRoleFlbYn());	
		adcInfo.setSpSessionMax(svc.getSpSessionMax());
		return adcInfo;
	}
	public OBDtoFaultHWStatus getHWStatus(OBDtoADCObject adcObject) throws OBException, Exception
	{
		return faultMonitoringSvc.getADCMonHWStatus(adcObject);
	}
	
	public OBDtoAdcCurrentSession getSessionHistoryInfo(OBDtoADCObject adcObject, OBDtoSearch searchOption) throws OBException, Exception
	{		
		//return faultMonitoringSvc.getADCMonSessionHistory(adcObject, searchOption);
		return faultMonitoringSvc.getADCMonSessionHistoryNew(adcObject, searchOption);		
	}
	
	public OBDtoFaultDataObj getBpsHistoryInfo(OBDtoADCObject adcObject, OBDtoSearch searchOption) throws OBException, Exception
	{				
		return faultMonitoringSvc.getADCMonBpsHistory(adcObject, searchOption);
	}
	
	public OBDtoAdcMonCpuDataObj getAdcCpuHistroyInfo(OBDtoADCObject adcObject, OBDtoSearch searchOption) throws OBException, Exception
	{				
		return faultMonitoringSvc.getADCMonCpuData(adcObject, searchOption);
	}
	
	public OBDtoAdcMonCpuDataObj getCpuHistroyInfo(OBDtoADCObject adcObject, OBDtoSearch searchOption) throws OBException, Exception
    {               
        return faultMonitoringSvc.getADCMonCpuHistory(adcObject, searchOption);
    }
	
	public OBDtoFaultCpuDataObj getCpuSPHistroyInfo(OBDtoADCObject adcObject, OBDtoSearch searchOption, Integer cpuNum) throws OBException, Exception
	{				
		return faultMonitoringSvc.getADCMonCpuSPHistory(adcObject, searchOption, cpuNum);
	}	
	
	public OBDtoFaultCpuHistory getCpuSpConnectionInfo(OBDtoADCObject adcObject, OBDtoSearch searchOption) throws OBException, Exception
	{
		return faultMonitoringSvc.getADCMonCpuSpConnectionInfo(adcObject, searchOption);
	}
	
	public OBDtoFaultDataObj getMemHistoryInfo(OBDtoADCObject adcObject, OBDtoSearch searchOption) throws OBException, Exception
	{				
		return faultMonitoringSvc.getADCMonMemoryHistory(adcObject, searchOption);
	}
	
	public OBDtoFaultDataObj getPktErrHistoryInfo(OBDtoADCObject adcObject, OBDtoSearch searchOption) throws OBException, Exception
	{				
		return faultMonitoringSvc.getADCMonPktErrHistory(adcObject, searchOption);
	}
	
	public OBDtoFaultDataObj getPktDropHistoryInfo(OBDtoADCObject adcObject, OBDtoSearch searchOption) throws OBException, Exception
	{				
		return faultMonitoringSvc.getADCMonPktDropHistory(adcObject, searchOption);
	}
	
	public OBDtoFaultDataObj getHTTPRequestHistoryInfo(OBDtoADCObject adcObject, OBDtoSearch searchOption) throws OBException, Exception
	{	
		return faultMonitoringSvc.getADCMonHttpReqestHistory(adcObject, searchOption);
	}
	
	public OBDtoFaultDataObj getSSLTransactionHistoryInfo(OBDtoADCObject adcObject, OBDtoSearch searchOption) throws OBException, Exception
	{	
		return faultMonitoringSvc.getADCMonSSLTransactionHistory(adcObject, searchOption);
	}	
}