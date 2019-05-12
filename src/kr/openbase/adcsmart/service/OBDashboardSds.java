package kr.openbase.adcsmart.service;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoAdcGroup;
import kr.openbase.adcsmart.service.dto.OBDtoConnection;
import kr.openbase.adcsmart.service.dto.OBDtoCpu;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsAdcSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsIssueSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsMemberConnection;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsStatusSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservInfo;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservStatus;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservSummaryCount;
import kr.openbase.adcsmart.service.dto.OBDtoMemory;
import kr.openbase.adcsmart.service.dto.OBDtoThroughput;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoGroupHistory;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBDashboardSds
{
	/** 
	 * STATUS SUMMARY<br/>
	 * 위치 : SDS Dashboard(ADC 요약), 상단 가로 , 공통<br/>
	 * 기능 : ADC 상태별 카운트, Virtual Server의 상태별 카운트, 장애 해결/미해결 카운트 <br/>
	 * 
	 * @param accountIndex : 사용자 계정. null 불가.
	 * @return OBDtoStatusSummary2
	 * @throws OBException
	 */
	/**
	 * STATUS SUMMARY<br/>
	 * 위치 : SDS Dashboard(ADC 요약), 상단 가로 , 공통<br/>
	 * 기능 : ADC 상태별 카운트, Virtual Server의 상태별 카운트, 장애 해결/미해결 카운트 <br/>
	 *  
	 * @param accountIndex : 사용자 계정. null 불가.
	 * @param issueUnsolvedMaxDays : 장애 모니터 "최근 N일"의 N, 단위 日
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardSdsStatusSummary getStatusSummary(Integer accountIndex, Integer issueUnsolvedMaxDays) throws OBException;

	/** 
	 * ADC SUMMARY<br/>
	 * 위치 : SDS Dashboard(장애모니터), 좌측 세로, 공통<br/>
	 * 기능 : 전체 ADC 목록을  조회하여 group별로 분류한다. 각 group별로 ADC list를 구성하고, 각 gropu도 list로 묶는다. <br/>
	 *
	 * @param accountIndex : 사용자 계정. null 불가
	 * @return ArrayList<OBDtoAdcGroupDetail>
	 * 	adcAvailCount - 연결이 정상인 ADC 수, adcUnavailCount - 연결이 비정상인 ADC 수
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcGroup> getGroupAdcList(Integer accountIndex) throws OBException;
	
	/**
	 * sds_dashboard_list_1 : ADC 모니터링 목록<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상단 목록<br/>
	 * 기능 : 전체 ADC 목록 조회 <br/>
	 *   "ADC Status"조건에 맞고 계정에 할당된 ADC 전체 목록을 조회한다. <br/>
	 * @param accountIndex : 사용자 계정 인덱스, 접근 가능한 ADC 목록을 파악하는 데 필요, null 불가 
	 * @param adcStatus : 조회할 ADC Status<br/>
	 * 	  STATUS_ADC_ALL = 0<br/>
	 *    STATUS_ADC_AVAILABLE = 1<br/>
	 *    STATUS_ADC_UNAVAILABLE = 2<br/>
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoDashboardSdsAdcSummary> getAdcListAll(Integer accountIndex, Integer adcStatus, Integer faultUnsolvedLimitDays) throws OBException;

	/**
	 * 
	 * @param accountIndex
	 * @param adcStatus
	 * @param faultUnsolvedLimitDays
	 * @param orderType.ORDER_TYPE_ADCNAME, ORDER_TYPE_ADCIPADDRESS, ORDER_TYPE_ADCSTATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardSdsAdcSummary> getAdcListAll(Integer accountIndex, Integer adcStatus, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;

	/**
	 * sds_dashboard_list_1 : ADC 모니터링 목록<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상단 목록<br/>
	 * 기능 : 선택한 그룹의 ADC 목록 조회<br/>
	 *   "ADC Status"조건에 맞고 선택된 그룹에 속하면서 계정에 할당된 ADC 목록을 조회한다. <br/>
	 * @param accountIndex : 사용자 계정 인덱스, 접근 가능한 ADC 목록을 파악하는 데 필요, null 부가
	 * @param adcStatus : 조회할 ADC Status<br/>
	 * 	  STATUS_ADC_ALL = 0<br/>
	 *    STATUS_ADC_AVAILABLE = 1<br/>
	 *    STATUS_ADC_UNAVAILABLE = 2<br/>
	 * @param groupIndex : 선택한 그룹 인덱스, null 불가
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoDashboardSdsAdcSummary> getAdcListGroup(Integer accountIndex, Integer adcStatus, Integer groupIndex, Integer faultUnsolvedLimitDays) throws OBException;

	/**
	 * 
	 * @param accountIndex
	 * @param adcStatus
	 * @param groupIndex
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_ADCNAME, ORDER_TYPE_ADCIPADDRESS, ORDER_TYPE_ADCSTATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardSdsAdcSummary> getAdcListGroup(Integer accountIndex, Integer adcStatus, Integer groupIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;
	/**
	 * sds_dashboard_list_1 : ADC 모니터링 목록<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상단 목록<br/>
	 * 기능 : 선택한 그룹의 ADC 목록 조회<br/>
	 *   "ADC Status"조건에 맞고 선택된 ADC 1건을 조회한다. <br/>
	 * @param adcIndex : 선택한 ADC 인덱스, null 불가
	 * @param adcStatus : 조회할 ADC Status<br/>
	 * 	  STATUS_ADC_ALL = 0<br/>
	 *    STATUS_ADC_AVAILABLE = 1<br/>
	 *    STATUS_ADC_UNAVAILABLE = 2<br/>
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoDashboardSdsAdcSummary> getAdcListSingle(Integer adcStatus, Integer adcIndex, Integer faultUnsolvedLimitDays) throws OBException;
	
	/**
	 * 
	 * @param adcStatus
	 * @param adcIndex
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_ADCNAME, ORDER_TYPE_ADCIPADDRESS, ORDER_TYPE_ADCSTATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardSdsAdcSummary> getAdcListSingle(Integer adcStatus, Integer adcIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;
	/** 
	 * sds_dashboard_list_2 : VIRTUAL SERVER 모니터링 목록<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상단 목록<br/>
	 * 기능 : Virtual Server 전체 목록 조회<br/>
	 *  "Virtual Server Status"조건에 맞고, 계정에 할당된 ADC의  VirtualServer/VirtualService 전체 조회<br/>

	 * @param accountIndex : 계정 인덱스, null 불가
	 * @param vservStatus : 조회할 Virtual server/service 상태<br/>
	 *    STATUS_VS_ALL = 10<br/>
	 *    STATUS_VS_AVAILABLE = 11<br/>
	 *    STATUS_VS_UNAVAILABLE = 12<br/>
	 *    STATUS_VS_UNAVAILABLE_LONG = 13<br/>
	 *    STATUS_VS_DISABLE = 14<br/>
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListAllAdc(Integer accountIndex, Integer vservStatus, Integer faultUnsolvedLimitDays) throws OBException;

	/**
	 * 
	 * @param accountIndex
	 * @param vservStatus
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_SERVICEPORT, ORDER_TYPE_STATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_ADCNAME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListAllAdc(Integer accountIndex, Integer vservStatus, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;
	/** 
	 * sds_dashboard_list_2 : VIRTUAL SERVER 모니터링 목록<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상단 목록<br/>
	 * 기능 : Virtual Server 목록 조회 - 특정 ADC 그룹<br/>
	 *  "Virtual Server Status"조건에 맞고, 지정한 ADC그룹이면서 사용자계정에 할당된 ADC들의  VirtualServer/VirtualService 목록을 조회한다.<br/>
	 * 
	 * @param accountIndex : 계정 인덱스, null 불가
	 * @param vservStatus : 조회할 Virtual server/service 상태<br/>
	 *    STATUS_VS_ALL = 10<br/>
	 *    STATUS_VS_AVAILABLE = 11<br/>
	 *    STATUS_VS_UNAVAILABLE = 12<br/>
	 *    STATUS_VS_UNAVAILABLE_LONG = 13<br/>
	 *    STATUS_VS_DISABLE = 14<br/>
	 * @param groupIndex : 선택한 group 인덱스, null 불가<br/>
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListGroupAdc(Integer accountIndex, Integer vservStatus, Integer groupIndex, Integer faultUnsolvedLimitDays) throws OBException;

	/**
	 * 
	 * @param accountIndex
	 * @param vservStatus
	 * @param groupIndex
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_SERVICEPORT, ORDER_TYPE_STATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_ADCNAME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListGroupAdc(Integer accountIndex, Integer vservStatus, Integer groupIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;
	/** 
	 * sds_dashboard_list_2 : VIRTUAL SERVER 모니터링 목록<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상단 목록<br/>
	 * 기능 : Virtual Server 목록 조회 - 지정한 ADC <br/>
	 *  "Virtual Server Status"조건에 맞는, 지정한 ADC의 VirtualServer/VirtualService 목록 조회<br/>
	 *  
	 * @param vservStatus : 조회할 Virtual server/service 상태<br/>
	 *    STATUS_VS_ALL = 10<br/>
	 *    STATUS_VS_AVAILABLE = 11<br/>
	 *    STATUS_VS_UNAVAILABLE = 12<br/>
	 *    STATUS_VS_UNAVAILABLE_LONG = 13<br/>
	 *    STATUS_VS_DISABLE = 14<br/>
	 * @param adcIndex : 선택한 ADC 인덱스, null 불가<br/>
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListSingleAdc(Integer vservStatus, Integer adcIndex, Integer faultUnsolvedLimitDays) throws OBException;

	/**
	 * 
	 * @param vservStatus
	 * @param adcIndex
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_SERVICEPORT, ORDER_TYPE_STATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_ADCNAME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListSingleAdc(Integer vservStatus, Integer adcIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;
	/**
	 * 일정 기간(시스템 지정 일수)초과 단절이 지속된 virtual server를 전체 ADC에서 카운트한다.
	 * @param accountIndex : 계정 인덱스
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListAllAdcUnavailNDays(Integer accountIndex, Integer faultUnsolvedLimitDays) throws OBException;

	/**
	 * 
	 * @param accountIndex
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_SERVICEPORT, ORDER_TYPE_STATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_ADCNAME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListAllAdcUnavailNDays(Integer accountIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;

	/**
	 * 일정 기간(시스템 지정 일수)초과 단절이 지속된 virtual server를 전체 지정한 group의 ADC에서 카운트한다.
	 * @param accountIndex : 계정 인덱스
	 * @param groupIndex : ADC group 인덱스
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListGroupAdcUnavailNDays(Integer accountIndex, Integer groupIndex, Integer faultUnsolvedLimitDays) throws OBException;

	/**
	 * 
	 * @param accountIndex
	 * @param groupIndex
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_SERVICEPORT, ORDER_TYPE_STATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_ADCNAME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListGroupAdcUnavailNDays(Integer accountIndex, Integer groupIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;
	/**
	 * 일정 기간(시스템 지정 일수)초과 단절이 지속된 virtual server를 전체 지정한 ADC에서 카운트한다.
	 * @param adcIndex : ADC 인덱스
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListSingleAdcUnavailNDays(Integer adcIndex, Integer faultUnsolvedLimitDays) throws OBException;
	
	/**
	 * 
	 * @param adcIndex
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_SERVICEPORT, ORDER_TYPE_STATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_ADCNAME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardSdsVservSummary> getVservListSingleAdcUnavailNDays(Integer adcIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;
	/**
	 * 
	 * @param accountIndex
	 * @param vservStatus
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_SERVICEPORT, ORDER_TYPE_STATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_ADCNAME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardSdsVservSummaryCount getVservCountAllAdc(Integer accountIndex, Integer vservStatus, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;
	/**
	 * 
	 * @param accountIndex
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_SERVICEPORT, ORDER_TYPE_STATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_ADCNAME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardSdsVservSummaryCount getVservCountAllAdcUnavailNDays(Integer accountIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;
	/**
	 * 
	 * @param accountIndex
	 * @param groupIndex
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_SERVICEPORT, ORDER_TYPE_STATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_ADCNAME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardSdsVservSummaryCount getVservCountGroupAdc(Integer accountIndex, Integer vservStatus, Integer groupIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;
	/**
	 * 
	 * @param vservStatus
	 * @param adcIndex
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_SERVICEPORT, ORDER_TYPE_STATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_ADCNAME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardSdsVservSummaryCount getVservCountGroupAdcUnavailNDays(Integer accountIndex, Integer groupIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;
	/**
	 * 
	 * @param accountIndex
	 * @param vservStatus
	 * @param groupIndex
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_SERVICEPORT, ORDER_TYPE_STATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_ADCNAME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardSdsVservSummaryCount getVservCountSingleAdc(Integer vservStatus, Integer adcIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;
	/**
	 * 
	 * @param adcIndex
	 * @param faultUnsolvedLimitDays
	 * @param orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_SERVICEPORT, ORDER_TYPE_STATUS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_ADCNAME, ORDER_TYPE_PRODUCTNAME, ORDER_TYPE_VERSION, ORDER_TYPE_CPS, ORDER_TYPE_BPS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardSdsVservSummaryCount getVservCountSingleAdcUnavailNDays(Integer adcIndex, Integer faultUnsolvedLimitDays, Integer orderType, Integer orderDir) throws OBException;	
	/**
	 *  
	 * sds_dashboard_list_3 : 이슈(장애) 모니터링 목록<br/>
	  * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상단 목록<br/>
	  * 기능 : 이슈(장애) 목록 조회, 지정 일자 이내의 발생한 장애만 조회한다<br/>
	  *  계정에 할당된 ADC 전체에 대해서, "이슈(장애)상태"와 일치하고 일자 조건에 맞는 이슈(장애) 목록 조회<br/>
	 *  
	 * @param accountIndex : 계정 인덱스, null 불가
	 * @param issueStatus : 조회할 이슈(장애) 상태<br/>
	 * 	  STATUS_ISSUE_ALL = 20<br/>
	 *    STATUS_ISSUE_SOLVED = 21<br/>
	 *    STATUS_ISSUE_UNSOLVED = 22<br/>
	 * @param issueUnsolvedMaxDays<br/>
	 *    issueStatus==(STATUS_ISSUE_ALL||STATUS_ISSUE_SOLVED) 일 때만 유효한 값, 전체장애와 해결장애 카운트는 여기 주어진 일자 이내에서 한다. null 불가 <br/>
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoDashboardSdsIssueSummary> getIssueListAllAdc(Integer accountIndex, Integer issueStatus, Integer issueUnsolvedMaxDays) throws OBException;

	/**
	 * 
	 * @param accountIndex
	 * @param issueStatus
	 * @param issueUnsolvedMaxDays
	 * @param orderType. ORDER_TYPE_TYPE, ORDER_TYPE_ADCNAME, ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_SEVERITY, ORDER_TYPE_STATUS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardSdsIssueSummary> getIssueListAllAdc(Integer accountIndex, Integer issueStatus, Integer issueUnsolvedMaxDays, Integer orderType, Integer orderDir) throws OBException;
	
	/** 
	 * sds_dashboard_list_3 : 이슈(장애) 모니터링 목록<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상단 목록<br/>
	 * 기능 : 이슈(장애) 목록 조회, 지정한 일자 이내의 장애만 조회한다<br/>
	 * 계정에 할당된 ADC 중 특정 그룹에 속하고, "이슈(장애)상태"와  일치하며 일자 조건에 맞는 이슈(장애) 목록 조회<br/>
	 * @param accountIndex : 계정 인덱스, null 불가
	 * @param issueStatus : 조회할 이슈(장애) 상태<br/>
	 * 	  STATUS_ISSUE_ALL = 20<br/>
	 *    STATUS_ISSUE_SOLVED = 21<br/>
	 *    STATUS_ISSUE_UNSOLVED = 22<br/>
	 * @param groupIndex : ADC 그룹 인덱스, null 불가
	 * @param issueUnsolvedMaxDays<br/>
	 *    issueStatus==(STATUS_ISSUE_ALL||STATUS_ISSUE_SOLVED) 일 때만 유효한 값, 전체장애와 해결장애 카운트는 여기 주어진 일자 이내에서 한다. null 불가 <br/>
	 * @return 
	 *    OBDtoDashboardSdsIssueSummary.issueType 범례  <br/>
	 *    - 1 : ADC_FAULT_SYSTEM, adc reachable/unreachable  <br/>
	 *    - 2 : ADC_FAULT_VIRTSRV, virtual server up/down  <br/>
	 *    - 3 : ADC_FAULT_POOLMEMS, pool member up/down  <br/>
	 *    - 4 : ADC_FAULT_LINKS, link up/down  <br/>
	 * @throws OBException
	 */
//	public ArrayList<OBDtoDashboardSdsIssueSummary> getIssueListGroupAdc(Integer accountIndex, Integer issueStatus, Integer groupIndex, Integer issueUnsolvedMaxDays) throws OBException;

	/**
	 * 
	 * @param accountIndex
	 * @param issueStatus
	 * @param groupIndex
	 * @param issueUnsolvedMaxDays
	 * @param orderType. ORDER_TYPE_TYPE, ORDER_TYPE_ADCNAME, ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_SEVERITY, ORDER_TYPE_STATUS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardSdsIssueSummary> getIssueListGroupAdc(Integer accountIndex, Integer issueStatus, Integer groupIndex, Integer issueUnsolvedMaxDays, Integer orderType, Integer orderDir) throws OBException;

	/** 
	 * sds_dashboard_list_3 : 이슈(장애) 모니터링 목록<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상단 목록<br/>
	 * 기능 : 이슈(장애) 목록 조회, 지정한 일자 이내의 장애만 조회한다<br/>
	 *  특정 ADC에서 "이슈(장애)상태"와 일자 조건에 맞는 이슈(장애) 목록 조회<br/>
	 *  
	 * @param issueStatus : 조회할 이슈(장애) 상태<br/>
	 *    STATUS_ISSUE_ALL = 20<br/>
	 *    STATUS_ISSUE_SOLVED = 21<br/>
	 *    STATUS_ISSUE_UNSOLVED = 22<br/>
 	 * @param adcIndex : ADC 인덱스, null 불가
	 * @param issueUnsolvedMaxDays<br/>
	 *    issueStatus==(STATUS_ISSUE_ALL||STATUS_ISSUE_SOLVED) 일 때만 유효한 값, 전체장애와 해결장애 카운트는 여기 주어진 일자 이내에서 한다. null 불가 <br/>
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoDashboardSdsIssueSummary> getIssueListSingleAdc(Integer issueStatus, Integer adcIndex, Integer issueUnsolvedMaxDays) throws OBException;

	/**
	 * 
	 * @param issueStatus
	 * @param adcIndex
	 * @param issueUnsolvedMaxDays
	 * @param orderType. ORDER_TYPE_TYPE, ORDER_TYPE_ADCNAME, ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_OCCURTIME, ORDER_TYPE_SEVERITY, ORDER_TYPE_STATUS, ORDER_TYPE_CPUUSAGE, ORDER_TYPE_MEMUSAGE
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardSdsIssueSummary> getIssueListSingleAdc(Integer issueStatus, Integer adcIndex, Integer issueUnsolvedMaxDays, Integer orderType, Integer orderDir) throws OBException;
	
	//---------- 이후 부터는 중앙 contents중 상세 정보 부분---------------------
	
	/** 
	 * sds_dashboard_detail_1 <br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상세정보<br/>
	 * 기능 : ADC레벨의 connection 추이 조회<br/>
	 * 
	 * @param adcIndex : adc index, null 불가
	 * @param beginTime : 조회 구간의 시작시간, null이면  endTime에서 기본값만큼 이전
	 * @param endTime : 조회구간의 종료 시간, null이면 현재
	 * @return
	 * @throws OBException
	 */
	public OBDtoConnection getAdcConnections(Integer adcIndex, Date beginTime, Date endTime) throws OBException;
	
	/** 
	 * sds_dashboard_detail_2<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상세정보<br/>
	 * 기능 : ADC레벨의 throughput 추이 조회<br/>
	 * 
	 * @param adcIndex : adc index, null 불가
	 * @param beginTime : 조회 구간의 시작시간, null이면  endTime에서 기본값만큼 이전
	 * @param endTime : 조회구간의 종료 시간, null이면 현재
	 * @return
	 * @throws OBException
	 */
	public OBDtoThroughput getAdcThroughput(Integer adcIndex, Date beginTime, Date endTime) throws OBException;

	/** 
	 * sds_dashboard_detail_3<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상세정보<br/>
	 * 기능 : ADC 레벨의  CPU 사용 추이 조회<br/>
	 * 
	 * @param adcIndex : adc index, null 불가
	 * @param beginTime : 조회 구간의 시작시간, null이면  endTime에서 기본값만큼 이전
	 * @param endTime : 조회구간의 종료 시간, null이면 현재
	 * @return
	 * @throws OBException
	 */
	public OBDtoCpu getAdcUsageCpu(Integer adcIndex, Date beginTime, Date endTime) throws OBException;

	   /** 
     * sds_dashboard_detail_3<br/>
     * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상세정보<br/>
     * 기능 : ADC 레벨의  CPU 그룹 사용 추이 조회<br/>
     * 
     * @param adcGroupIndex : adc index, null 불가
     * @param beginTime : 조회 구간의 시작시간, null이면  endTime에서 기본값만큼 이전
     * @param endTime : 조회구간의 종료 시간, null이면 현재
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoGroupHistory> getAdcUsageCpuGroup(Integer adcGroupIndex, Date beginTime, Date endTime) throws OBException;
	
	/** 
	 * sds_dashboard_detail_4<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상세정보<br/>
	 * 기능 : ADC 레벨의  메모리 사용 추이 조회<br/>
	 * 
	 * @param adcIndex : adc index, null 불가
	 * @param beginTime : 조회 구간의 시작시간, null이면  endTime에서 기본값만큼 이전
	 * @param endTime : 조회구간의 종료 시간, null이면 현재
	 * @return
	 * @throws OBException
	 */
	public OBDtoMemory getAdcUsageMem(Integer adcIndex, Date beginTime, Date endTime) throws OBException;
	
	   /** 
     * sds_dashboard_detail_4<br/>
     * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상세정보<br/>
     * 기능 : ADC 그룹의  메모리 사용 추이 조회<br/>
     * 
     * @param adcIndex : adc Group index, null 불가
     * @param beginTime : 조회 구간의 시작시간, null이면  endTime에서 기본값만큼 이전
     * @param endTime : 조회구간의 종료 시간, null이면 현재
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoGroupHistory> getAdcUsageMemGroup(Integer adcGroupIndex, Date beginTime, Date endTime) throws OBException;
	
	/** 
	 * sds_dashboard_detail_5<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상세정보<br/>
	 * 기능 : Virtual Server 레벨의 connection 추이 조회. Alteon이면 virtual service단위<br/>
	 * 
	 * @param recordType : 상세조회하려고 누른 레코드가 virtual server/virtual service 인지 식별, null 불가<br/>
	 *  - 0 : virtual server <br/>
	 *  - 1 : virtual service <br/>
	 * @param adcIndex : adc index, null 불가
	 * @param vservIndex : virtual server index
	 * @param vservPort : virtual service port, F5는 null, Alteon은  recordType=0에서 null, recordType=1일때만 non-null값을 준다. 
	 * @param beginTime : 조회 구간의 시작시간, null이면  endTime에서 기본값만큼 이전
	 * @param endTime : 조회구간의 종료 시간, null이면 현재
	 * @return
	 * @throws OBException
	 */
	public OBDtoConnection getVservConnections(Integer recordType, Integer adcIndex, String vservIndex, Integer vservPort, Date beginTime, Date endTime) throws OBException;
	
	/** 
	 * sds_dashboard_detail_6<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상세정보<br/>
	 * 기능 : Virtual Server 레벨의  throughput 추이 조회. Alteon이면 virtual service단위<br/>
	 * 
	 * @param recordType : 상세조회하려고 누른 레코드가 virtual server/virtual service 인지 식별, null 불가<br/>
	 *  - 0 : virtual server <br/>
	 *  - 1 : virtual service <br/>
	 * @param adcIndex : adc index, null 불가
	 * @param vservIndex : virtual server index 
	 * @param vservPort : virtual service port, F5는 null, Alteon은  recordType=0에서 null, recordType=1일때만 non-null값을 준다.
	 * @param beginTime : 조회 구간의 시작시간, null이면  endTime에서 기본값만큼 이전
	 * @param endTime : 조회구간의 종료 시간, null이면 현재
	 * @return
	 * @throws OBException
	 */
	public OBDtoThroughput getVservThroughput(Integer recordType, Integer adcIndex, String vservIndex, Integer vservPort, Date beginTime, Date endTime) throws OBException;

	/** 
	 * sds_dashboard_detail_7<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상세정보<br/>
	 * 기능 : ADC 상세 정보 조회<br/>
	 * 
	 * @param adcIndex : 중앙 contents중 ADC 목록(sds_dashboard_list_1)에서 선택한 ADC의 index
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardSdsAdcInfo getAdcInfo(Integer adcIndex, Integer faultUnsolvedLimitDays) throws OBException;
	/** 
	 * sds_dashboard_detail_8<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상세정보<br/>
	 * 기능 : virtual server 상세 정보 조회, Alteon이면 virtual service 정보도 같이 표시<br/>

	 * @param recordType : 상세조회하려고 누른 레코드가 virtual server/virtual service 인지 식별, null 불가<br/>
	 *  - 0 : virtual server <br/>
	 *  - 1 : virtual service <br/>
	 * @param vservIndex : 중앙 contents중 Virtual Server 목록에서 선택한 virtual server의 index
	 *   Alteon이면 virtual service일 수도 있음
	 * @param port : Alteon일 경우 선택한 대상이  virtual service이면 port를 전달. virtual server이면 port null
	 * @param adcIndex : virtual server/service가 속한 adc index, virtual server 조회지만 adc레벨 테이블에서 구해오는 정보도 있어서 필요함
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardSdsVservInfo getVservInfo(Integer recordType, String vservIndex, Integer port, Integer adcIndex) throws OBException;
	/** 
	 * sds_dashboard_detail_9<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상세정보<br/>
	 * 기능 : virtual server 멤버들의 트래픽 처리량 조회<br/>
	 *
	 * @param vservIndex : virtual server index, null 불가
	 * @param vservPort : virtual service port, Alteon의 virtual service일 경우만 값을 준다. F5는 null
	 * @param adcIndex : virtual server/service가 속한 adc index, adc 유형 확인 용
	 * @return
	 */
	public ArrayList<OBDtoDashboardSdsMemberConnection> getVservMemberConnections(String vservIndex, Integer vservPort, Integer adcIndex) throws OBException;

	/** 
	 * sds_dashboard_detail_10<br/>
	 * 위치 : SDS Dashboard(장애모니터), 중앙 contents 중 상세정보<br/>
	 * 기능 : ADC의 virtual server 상태별 분포를 기간내 조회한다. Alteon이면 virtual service단위<br/>
	 * 
	 * @param adcIndex : adc index, null 불가
	 * @param beginTime : 조회 구간의 시작시간, null이면  endTime에서 기본값만큼 이전
	 * @param endTime : 조회구간의 종료 시간, null이면 현재
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardSdsVservStatus> getVservStatus(Integer adcIndex, Date beginTime, Date endTime) throws OBException;
	
	/**
	 * 지정한 alert 목록의 상태를 '미해결'-->'해결'로 바꾼다.
	 * @param faultIndexList : 장애 인덱스
	 * @throws OBException
	 */
	public void setFaultSolved(ArrayList<Long> faultIndexList) throws OBException;
}