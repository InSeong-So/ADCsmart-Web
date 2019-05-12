/**
 * 장애 진단 관련 인터페이스.
 */
package kr.openbase.adcsmart.service;

import java.sql.Timestamp;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoVirtualServiceInfo;
import kr.openbase.adcsmart.service.dto.OBDtoVlanInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckLog;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResult;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckSchedule;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckStatus;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckTemplate;
import kr.openbase.adcsmart.service.dto.fault.OBDtoObjectIndexInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoScheduleDateTime;
import kr.openbase.adcsmart.service.impl.dto.OBDtoCpuMemStatus;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonL2Ports;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckPacketLossInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckResponseTimeInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktdumpInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktdumpStatusInfo;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBFaultMng
{
	public ArrayList<OBDtoVirtualServiceInfo> getVServiceList(OBDtoADCObject object) throws OBException;
	
	public ArrayList<String> getUsedClientIPList() throws OBException;
	
	/**
	 * 지정된 장비의 할당된 장애 점검 예약 리스트를 조회하낟.
	 * 
	 * @param object
	 * @param searchObj
	 * @param orderObj
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoFaultCheckSchedule> getFaultCheckScheduleList(OBDtoADCObject object, OBDtoSearch searchObj, OBDtoOrdering orderObj ) throws OBException;
	
	/**
	 * 장애 점검 예약 정보를 조회한다.
	 * @param index. 장애 점검을 위한 로그 index.
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultCheckSchedule getFaultCheckScheduleInfo(Long index) throws OBException;
	
	
	/**
	 * 진단 결과를 삭제한다.
	 * @param index. 진단 결과 삭제를 위한 Logkey 
	 * @return
	 * @throws OBException
	 */
	public void deleteFaultCheckScheduleInfo(Long index, OBDtoExtraInfo extraInfo) throws OBException;
	
	
	/**
	 * 진행중인 장애 검사를 취소한다.
	 * 
	 * @param checkKey
	 * @param extraInfo
	 * @return true: 진단 성공. false: 실패. 
	 * @throws OBException
	 */	
	public void cancelFaultCheck(Long checkKey, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 장애 진단을 시작한다.
	 * 
	 * @param object : 장애 진단할 대상 정보가 저장된다. 여러 장비의 장애 진단도 가능함.
	 * @param templateObj: 장애 진단 항목 정보.
	 * @param checkSpeed : 장애 진단 속도 정보.
	 * @param extraInfo : 사용자 정보.
	 * @return true: 진단 성공. false: 실패.
	 * @throws OBException
	 */
	public ArrayList<OBDtoObjectIndexInfo> startFaultCheck(OBDtoADCObject object, OBDtoFaultCheckTemplate templateObj, Integer checkSpeed, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	  * 장애 진단 예약을 등록한다. 
	 * @param object
	 * @param name
	 * @param templateObj
	 * @param timeObj
	 * @param checkSpeed
	 * @param extraInfo
	 * @return
	 * @throws OBException
	 */
	public void registerFaultCheckSchedule(OBDtoADCObject object, String name, String description, OBDtoFaultCheckTemplate templateObj, OBDtoScheduleDateTime timeObj, Integer checkSpeed, Integer scheduleType, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 *  장애 진단 템플릿을 저장한다. 동일 이름의 경우 경고 메세지 후 overwrite 한다.
	 *  
	 * @param templateObj
	 * @param extraInfo
	 * @return
	 * @throws OBException
	 */
	public void saveFaultCheckTemplate(OBDtoFaultCheckTemplate templateObj, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 지정된 템플릿을 삭제한다.
	 * @param templateObj
	 * @param extraInfo
	 * @throws OBException
	 */
	public void deleteFaultCheckTemplate(OBDtoFaultCheckTemplate templateObj, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * 등록된 템플릿 목록 제공한다.
	 * 
	 * @param searchObj. 현재는 사용하지 않음.
	 * @param orderObj. 현재는 사용하지 않음.
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoFaultCheckTemplate> getFaultCheckTemplateList(OBDtoSearch searchObj, OBDtoOrdering orderObj) throws OBException;
	
	/**
	 * 장애 점검을 위한 템플릿 정보를 조회한다.
	 * @param templateIndex
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultCheckTemplate getFaultCheckTemplateInfo(Long templateIndex) throws OBException;

	/**
	 * 장애 진단 로그 목록 개수를 제공한다.
	 * 
	 * @param object
	 * @param searchObj
	 * @return
	 * @throws OBException
	 */
	public Integer getFaultCheckLogListTotalCount(OBDtoADCObject object, OBDtoSearch searchObj) throws OBException;
		
	/**
	 * 장애 진단 로그 목록을 제공한다.
	 * 
	 * @param object
	 * @param searchObj
	 * @param orderObj
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoFaultCheckLog> getFaultCheckLogList(OBDtoADCObject object, OBDtoSearch searchObj, OBDtoOrdering orderObj ) throws OBException;
	
	/**
	 * 장애 진단 로그 정보를 추출한다.
	 * @param logKy
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultCheckLog getFaultCheckLogInfo(Long logKy) throws OBException;
	
	/**
	 * 지정된 예약을 삭제한다.
	 * @param index. 예약 취소를 위한 logkey ArrayList
	 * @return
	 * @throws OBException
	 */
	public void deleteFaultCheckLogInfo(ArrayList<String> indexList, OBDtoExtraInfo extraInfo) throws OBException;
		
	/**
	 * 장애 점검 중에 발생된 cpu/memory 이력을 추출한다.
	 * 
	 * @param object
	 * @param logKey
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoCpuMemStatus> getFaultAdcCpuMemoryHistory(Long logKey) throws OBException;
	
	/**
	 * 현재의 cpu/memory를 추출한다.
	 *  
	 * @param object
	 * @param logKey
	 * @return
	 * @throws OBException
	 */
	public OBDtoCpuMemStatus getFaultAdcCpuMemoryUsage(OBDtoADCObject object) throws OBException;
	
	public OBDtoCpuMemStatus getFaultAdcCpuMemoryUsageFromDB(OBDtoADCObject object) throws OBException;
	
	/**
	 * 현재 진행중인 장애 점검의 상태를 제공한다.
	 * 
	 * @param logKey
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultCheckStatus getFaultCheckStatus(Long logKey) throws OBException;
	
	/**
	 * 지정된 로그의 상세 정보를 제공한다.
	 * 
	 * @param checkKey
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultCheckResult getFaultCheckLogDetail(Long checkKey) throws OBException;
	
	/**
	 * 패킷 손실 분석용 데이터 분석.
	 * @param logKey
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoFaultCheckPacketLossInfo> getFaultCheckPacketLossInfo(Long logKey) throws OBException;
	
	/**
	 * 패킷 손실 분석  결과 이미지 파일의 이름을 제공한다.
	 * 
	 * @param logKey
	 * @return
	 * @throws OBException
	 */
	public String getFaultCheckPacketLossInfoImgFileName(Long logKey, Timestamp loginTime) throws OBException;

	/**
	 * 응답 시간 분석 
	 * 
	 * @param logKey
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultCheckResponseTimeInfo getFaultCheckResponseTimeInfo(Long logKey) throws OBException;
	
	/**
	 * 장애 점검의 서비스 분석에서 사용돼니 패킷 덤프 파일이 있는지 검사한다.
	 * 
	 * @param logKey
	 * @return
	 * @throws OBException
	 */
	public boolean isSvcPktdumpFileAvailable(Long logKey) throws OBException;
	
	/**
	 * 장애 점검의 서비스 분석에서 사용된 패킷 덤프 파일의 이름을 조회한다.
	 * 
	 * @param logKey
	 * @return 파일이 존재하지 않으면 ""를 리턴한다.
	 * @throws OBException
	 */
	public String getSvcPktdumpFileName(Long logKey) throws OBException;

	/**
	 * 패킷 덤프된 목록 조회카운트.
	 * @param object
	 * @param searchObj
	 * @return
	 * @throws OBException
	 */
	public Integer getPktdumpInfoListTotalCount(OBDtoADCObject object, OBDtoSearch searchObj) throws OBException;
	
	/**
	 * 패킷 덤프된 목록 조회.
	 * @param object
	 * @param searchObj
	 * @param orderObj
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoPktdumpInfo> getPktdumpInfoList(OBDtoADCObject object, OBDtoSearch searchObj, OBDtoOrdering orderObj ) throws OBException;
	
	/**
	 * 현재 패킷 덤프하고 있는 상태 정보 조회.
	 * 
	 * @param logKey
	 * @return
	 * @throws OBException
	 */
	public OBDtoPktdumpStatusInfo getPktdumpStatus(Long logKey) throws OBException;
	
	/**
	 * 현재 패킷 덤프하고 있는 상태 정보 조회. 여러개를 한꺼번에 조회한다.
	 * 
	 * @param logKeyList
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoPktdumpStatusInfo> getPktdumpStatusList(ArrayList<Long> logKeyList) throws OBException;

	/** 
	 * 패킷 덤프를 실시한다. 여러 장비에 거쳐 한꺼번에 실시할 수 있다.
	 * @param object
	 * @param dumpOption
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoObjectIndexInfo> startPktdump(OBDtoADCObject object, OBDtoPktdumpInfo dumpInfo, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * 포트 인터페이스의 이름 목록을 제공한다.
	 * 
	 * @param object
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoMonL2Ports> getPortInterfaceNameList(OBDtoADCObject object) throws OBException;
	
	/**
	 * vlan 이름 목록을 제공한다.
	 * @param object
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoVlanInfo> getVlanInterfaceNameList(OBDtoADCObject object) throws OBException;

	/** 진행중인 패킷 덤프 작업을 중지한다.
	 * 
	 * @param object
	 * @throws OBException
	 */
	public void stopPktdump(ArrayList<Long> indexKeyList, OBDtoExtraInfo extraInfo) throws OBException;

	/** 진행중인 패킷 덤프 작업을 취소한다. 
	 * 
	 * @param object
	 * @throws OBException
	 */
	public void cancelPktdump(ArrayList<Long> indexKeyList, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 덤프 파일을 삭제한다. 로그에서만 삭제하도록 한다.
	 * 
	 * @param indexKeyList
	 * @throws OBException
	 */
	public void delPktDumpLog(ArrayList<Long> indexKeyList, OBDtoExtraInfo extraInfo) throws OBException;
	
	/**
	 * 패킷 수집 기능에서 수집된 파일의 유효성을 검사한다.
	 * 
	 * @param logKey
	 * @return
	 * @throws OBException
	 */
	public boolean isPktdumpFileAvailable(Long logKey) throws OBException;
	
	/**
	 * 패킷 수집 기능에서 사용된 패킷 덤프 파일의 이름을 조회한다.
	 * 
	 * @param logKey
	 * @return 파일이 존재하지 않으면 ""를 리턴한다.
	 * @throws OBException
	 */
	public String getPktdumpFileName(Long logKey) throws OBException;
	
	/**
     * 지정된 로그의 adc이름을 조회한다.
     * 
     * @param logKey
     * @return 파일이 존재하지 않으면 ""를 리턴한다.
     * @throws OBException
     */
	public String getFaultAdcName(Long logKey) throws OBException;
}
