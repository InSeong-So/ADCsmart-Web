package kr.openbase.adcsmart.web.facade.fault;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBFaultMng;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckStatus;
import kr.openbase.adcsmart.service.impl.dto.OBDtoCpuMemStatus;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMngImpl;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;

@Component
public class FaultDiagnosisFacade
{
	private static transient Logger log = LoggerFactory.getLogger(FaultDiagnosisFacade.class);
	
	private OBFaultMng faultMngSvc;
	
	public FaultDiagnosisFacade()
	{
		faultMngSvc = new OBFaultMngImpl();
	}
	// 진단 정보 Get
	public OBDtoFaultCheckStatus getdiagnosisStateInfo(long logkey) throws Exception
	{		
		OBDtoFaultCheckStatus faultCheckStatusInfo = faultMngSvc.getFaultCheckStatus(logkey);
		log.debug("{}", faultCheckStatusInfo);		
		return faultCheckStatusInfo;		
	}
	// 실시간 System Usage Get
	public OBDtoCpuMemStatus getadcSystemUsageData(OBDtoADCObject adcObject) throws Exception
	{		
		OBDtoCpuMemStatus FaultAdcCpuMemoryInfo = faultMngSvc.getFaultAdcCpuMemoryUsage(adcObject);		
		log.debug("{}", FaultAdcCpuMemoryInfo);		
		return FaultAdcCpuMemoryInfo;
	}
	// 진단 취소 Set
	public void  cancelFaultCheck(Long logkey, SessionDto session) throws Exception
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		faultMngSvc.cancelFaultCheck(logkey, extraInfo);	
	}
}