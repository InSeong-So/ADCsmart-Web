package kr.openbase.adcsmart.web.facade.fault;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBFaultMng;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoVirtualServiceInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckLog;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResult;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckSchedule;
import kr.openbase.adcsmart.service.impl.dto.OBDtoCpuMemStatus;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMngImpl;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckPacketLossInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckResponseTimeInfo;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;

@Component
public class FaultHistoryFacade
{
	private static transient Logger log = LoggerFactory.getLogger(FaultHistoryFacade.class);
	
	private OBFaultMng faultMngSvc;
	
	public FaultHistoryFacade()
	{
		faultMngSvc = new OBFaultMngImpl();
	}
	
	public Integer retrieveHistoryListTotal(OBDtoADCObject object, OBDtoSearch searchObj) throws OBException, Exception
	{
		return faultMngSvc.getFaultCheckLogListTotalCount(object, searchObj);
	}
	
	public List<OBDtoFaultCheckLog> getHistoryList(OBDtoADCObject object, OBDtoSearch searchObj, OBDtoOrdering orderObj) throws OBException, Exception
	{
		ArrayList<OBDtoFaultCheckLog> faultLogListFromSvc = new ArrayList<OBDtoFaultCheckLog>();
		faultLogListFromSvc = faultMngSvc.getFaultCheckLogList(object, searchObj, orderObj);
		log.debug("{}", faultLogListFromSvc);
		
		return faultLogListFromSvc;			
	}
	// 장애 진단 결과 삭제
	public void delHistorys(List<String> historyIndices, SessionDto session) throws OBException, Exception 
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(historyIndices.toString());
		faultMngSvc.deleteFaultCheckLogInfo(new ArrayList<String>(historyIndices), extraInfo);
	}
	
	// 장비의 진단결과 
	public OBDtoFaultCheckResult getFaultCheckLogDetail(long logKey) throws Exception
	{
		return faultMngSvc.getFaultCheckLogDetail(logKey);		
	}
	
	//CPU 정보 (장애 점검 중에 발생된 cpu/memory 이력을 추출)
	public ArrayList<OBDtoCpuMemStatus> getFaultAdcCpuMemoryList(long logKey) throws OBException, Exception 
	{
		ArrayList<OBDtoCpuMemStatus> faultAdcCpuMemoryFromSvc = faultMngSvc.getFaultAdcCpuMemoryHistory(logKey);
		log.debug("{}", faultAdcCpuMemoryFromSvc);
		return faultAdcCpuMemoryFromSvc;
	}
	
	//패킷 손실 분석용 데이터 분석
	public ArrayList<OBDtoFaultCheckPacketLossInfo> getFaultCheckPacketLossInfo(long logKey) throws OBException, Exception
	{
		ArrayList<OBDtoFaultCheckPacketLossInfo> faultCheckPacketLossInfoFromSvc = faultMngSvc.getFaultCheckPacketLossInfo(logKey);
		log.debug("{}", faultCheckPacketLossInfoFromSvc);
		return faultCheckPacketLossInfoFromSvc;
	}
	
	// 패킷 손실 분석 파일 유무 체크
	public String getDmpFileName(Long logKey) throws OBException, Exception
	{
		return faultMngSvc.getSvcPktdumpFileName(logKey);
	}	
	
	//패킷 손실 분석 파일
	public String getFaultCheckPacketLossInfoImgFileName(long logKey, Timestamp loginTime) throws OBException, Exception
	{
		return faultMngSvc.getFaultCheckPacketLossInfoImgFileName(logKey, loginTime);		
	}
	
	public ArrayList<OBDtoVirtualServiceInfo> getVServiceList(OBDtoADCObject object) throws OBException, Exception
	{
		return faultMngSvc.getVServiceList(object);
	}
	
	//응답 시간 분석 
	public OBDtoFaultCheckResponseTimeInfo getFaultCheckResponseTimeInfo(Long logKey) throws OBException, Exception
	{
		OBDtoFaultCheckResponseTimeInfo faultCheckResponseTimeInfoFromSvc = faultMngSvc.getFaultCheckResponseTimeInfo(logKey);
		log.debug("{}", faultCheckResponseTimeInfoFromSvc);
		return faultCheckResponseTimeInfoFromSvc;
	}
	
	//지정된 장비의 할당된 장애 점검 예약 리스트를 조회
	public ArrayList<OBDtoFaultCheckSchedule> getFaultCheckScheduleList(OBDtoADCObject object, OBDtoSearch searchObj, OBDtoOrdering orderObj) throws OBException, Exception	
	{		
		return faultMngSvc.getFaultCheckScheduleList(object, searchObj, orderObj);
	}
	
	//장애 점검 예약 정보를 조회한다. index. 장애 점검을 위한 로그 index.	
	public OBDtoFaultCheckSchedule getFaultCheckScheduleInfo(long index) throws OBException, Exception
	{
		return faultMngSvc.getFaultCheckScheduleInfo(index);
	}
	
	//예약정보 취소(삭제)
	public void deleteFaultScheduleInfo(OBDtoFaultCheckSchedule faultSchedule, SessionDto session) throws OBException, Exception
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(faultSchedule.getDescription());
		faultMngSvc.deleteFaultCheckScheduleInfo(faultSchedule.getIndex(), extraInfo);
	}
	
	public String getFaultAdcName(long logKey) throws OBException
	{
	    return faultMngSvc.getFaultAdcName(logKey);
	}
}

