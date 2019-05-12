package kr.openbase.adcsmart.web.facade.fault;

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
import kr.openbase.adcsmart.service.dto.OBDtoVlanInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoObjectIndexInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoCpuMemStatus;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonL2Ports;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMngImpl;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktdumpInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktdumpStatusInfo;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;

@Component
public class FaultAnalysisFacade
{
	private static transient Logger log = LoggerFactory.getLogger(FaultAnalysisFacade.class);
	
	private OBFaultMng faultMngSvc;
	
	public FaultAnalysisFacade()
	{
		faultMngSvc = new OBFaultMngImpl();
	}
	
	//패킷 덤프된 목록 조회카운트.
	public Integer retrieveAnalysisListTotal(OBDtoADCObject object, OBDtoSearch searchObj) throws OBException, Exception
	{
		return faultMngSvc.getPktdumpInfoListTotalCount(object, searchObj);
	}
	
	//패킷 덤프된 목록 조회.
	public List<OBDtoPktdumpInfo> getAnalysisList(OBDtoADCObject object, OBDtoSearch searchObj, OBDtoOrdering orderObj) throws OBException, Exception
	{
		ArrayList<OBDtoPktdumpInfo> pktDumpInfoFromSvc = new ArrayList<OBDtoPktdumpInfo>();
		pktDumpInfoFromSvc = faultMngSvc.getPktdumpInfoList(object, searchObj, orderObj);
		return pktDumpInfoFromSvc;
	}
	
	//패킷 목록 삭제
	public void delPktDumps(ArrayList<Long> pktDumpIndices, SessionDto session) throws OBException, Exception
	{
//		OBDtoExtraInfo extraInfo = new OBDtoExtraInfo();
//		extraInfo.setExtraMsg1(pktDumpIndices.toString());
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		faultMngSvc.delPktDumpLog(pktDumpIndices, extraInfo);
		return;
	}
	
	//패킷 덤프를 실시한다. 여러 장비에 거쳐 한꺼번에 실시할 수 있다
	public ArrayList<OBDtoObjectIndexInfo> startPktdump(OBDtoADCObject object, OBDtoPktdumpInfo dumpInfo, SessionDto session) throws OBException, Exception
	{
		ArrayList<OBDtoObjectIndexInfo> pktDumpIndexFromSvc = new ArrayList<OBDtoObjectIndexInfo>();
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();	
		extraInfo.setExtraMsg1(dumpInfo.getFileName());
		pktDumpIndexFromSvc = faultMngSvc.startPktdump(object, dumpInfo, extraInfo);
		log.debug("{}", pktDumpIndexFromSvc);
		return pktDumpIndexFromSvc;		
	}	
	
	//진행중인 패킷 덤프 작업을 중지
	public void stopPktdump(ArrayList<Long> indexKeyList, SessionDto session) throws OBException, Exception
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		faultMngSvc.stopPktdump(indexKeyList, extraInfo);
	}
	
	//진행중인 패킷 덤프 작업을 취소
	public void cancelPktdump(ArrayList<Long> indexKeyList, SessionDto session) throws OBException, Exception
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		faultMngSvc.cancelPktdump(indexKeyList, extraInfo);
	}
	
	//현재 패킷 덤프하고 있는 상태 정보 조회
	public OBDtoPktdumpStatusInfo getPktdumpStatus(Long logKey) throws OBException, Exception	
	{
		OBDtoPktdumpStatusInfo pktDumpStatusInfoFromSvc = new OBDtoPktdumpStatusInfo();
		pktDumpStatusInfoFromSvc = faultMngSvc.getPktdumpStatus(logKey);
		return pktDumpStatusInfoFromSvc;
	}
	
	public boolean checkDumpFileExist(Long logKey)throws OBException, Exception
	{	
		return faultMngSvc.isSvcPktdumpFileAvailable(logKey); 
	}
	
	public String getDumpFileName(Long logKey)throws OBException, Exception
	{
		return faultMngSvc.getPktdumpFileName(logKey);
	}
	
	//현재 패킷 덤프하고 있는 상태 정보 조회. 여러개를 한꺼번에 조회
	public ArrayList<OBDtoPktdumpStatusInfo> getPktdumpStatusList(ArrayList<Long> logKeyList) throws OBException, Exception
	{
		ArrayList<OBDtoPktdumpStatusInfo> pktDumpStatusInfoListFromSvc = new ArrayList<OBDtoPktdumpStatusInfo>();
		pktDumpStatusInfoListFromSvc = faultMngSvc.getPktdumpStatusList(logKeyList);
		return pktDumpStatusInfoListFromSvc;
	}	
	// 포트 인터페이스의 이름 목록을 제공
	public ArrayList<OBDtoMonL2Ports> getPortInterfaceNameList(OBDtoADCObject object) throws OBException, Exception
	{
		return faultMngSvc.getPortInterfaceNameList(object);
	}
	
	public ArrayList<OBDtoVlanInfo> getVlanInterfaceNameList(OBDtoADCObject object) throws OBException, Exception
	{
		return faultMngSvc.getVlanInterfaceNameList(object);
	}
	
	//CPU 정보 (장애 점검 중에 발생된 cpu/memory 이력을 추출)
//	public ArrayList<OBDtoCpuMemStatus> getFaultAdcCpuMemoryList(ArrayList<Long> logKeyList) throws OBException, Exception 
	public OBDtoCpuMemStatus getFaultAdcCpuMemoryUsage(OBDtoADCObject object) throws OBException, Exception
	{
		OBDtoCpuMemStatus pktDumpCpuMemoryInfoFromSvc = faultMngSvc.getFaultAdcCpuMemoryUsageFromDB(object); 
		log.debug("{}", pktDumpCpuMemoryInfoFromSvc);
		return pktDumpCpuMemoryInfoFromSvc;
	}	
}

