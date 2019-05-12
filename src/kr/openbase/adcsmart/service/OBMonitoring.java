package kr.openbase.adcsmart.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcAlertLog;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPerformance;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPortStatus;
import kr.openbase.adcsmart.service.dto.OBDtoAdcScope;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSysRescStatus;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemLog;
import kr.openbase.adcsmart.service.dto.OBDtoConnectionData;
import kr.openbase.adcsmart.service.dto.OBDtoConnectionInfo;
import kr.openbase.adcsmart.service.dto.OBDtoMultiDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoNetworkMap;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoServiceMonitoringChart;
import kr.openbase.adcsmart.service.dto.OBDtoTrafficMapVServiceMembers;
import kr.openbase.adcsmart.service.dto.OBDtoVirtualServerSummary;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultGroupMemberPerfInfo;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbFilterInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalAdc;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalAdcCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalGroup;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalGroupCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalReal;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalRealCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalService;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalServiceCondition;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.ServiceMapVsDescDto;

public interface OBMonitoring {
	/**
	 * ADC 장비의 performance 추이 데이터를 제공한다.
	 * 
	 * @param adcIndex
	 * @param beginTime
	 * @param endTime
	 * @return OBDtoAdcPerformance
	 * @throws OBException
	 */
	public OBDtoAdcPerformance getAdcPerformance(Integer adcIndex, Date beginTime, Date endTime) throws OBException;

	/**
	 * 지정된 조건의 장비에서 발생된 장애 모니터링 로그를 제공한다.
	 * 
	 * @param adcIndex   : null 불가. adc 장비의 index.
	 * @param searchKey  : null 가능. 검색 키워드. ADC 이름. 세부 내용을 기준으로 제공한다.
	 * @param beginTime  : null 가능. 로그 시작 시간. null일 경우에는 endTime 기준 7일을 기준으로 한다.
	 * @param endTime    : null 가능. 로그 종료 시간. null일 경우에는 현재 시각으로 간주한다.
	 * @param beginIndex : 로그 시작 인덱스. null 가능. null일 경우에는 0으로 간주함.
	 * @param endIndex   : 로그 종료 인덱스. null 일 경우에는 모든 로그로 간주함. 로그는 최대 10000개 까지만
	 *                   제공한다.
	 * @return ArrayList<OBDtoAdcSystemLog>
	 * @throws OBException
	 */
//	public ArrayList<OBDtoAdcSystemLog> getAdcSystemFaultLog(Integer adcIndex, String searchKey, Date beginTime, Date endTime, Integer beginIndex, Integer endIndex) throws OBException;

	/**
	 * getAlertLogCount 경보 모니터링 경보 로그 개수 제공 (개별 ADC, ADC그룹, 전체ADC)
	 * 
	 * @param adcObject, searchOption
	 * @return Integer
	 * @throws OBException
	 */
	public Integer getAlertLogCount(OBDtoADCObject adcObject, OBDtoSearch searchOption, Integer accountIndex)
			throws OBException;

	/**
	 * getAlertLog 경보 모니터링 경보로그제공 (개별 ADC, ADC그룹, 전체ADC)
	 * 
	 * @param adcObject, searchOption, orderOption
	 * @return ArrayList<OBDtoAdcSystemLog>
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcAlertLog> getAlertLog(OBDtoADCObject adcObject, OBDtoSearch searchOption,
			OBDtoOrdering orderOption, Integer accountIndex) throws OBException;

	/**
	 * getFaultLogCount 장애 모니터링 경보 로그 개수 제공 (개별 ADC, ADC그룹, 전체ADC)
	 * 
	 * @param adcObject, searchOption
	 * @return Integer
	 * @throws OBException
	 */
	public Integer getAdcSystemFaultLogCount(OBDtoADCObject adcObject, OBDtoSearch searchOption, Integer accountIndex)
			throws OBException;

	/**
	 * getFaultLog 장애 모니터링 경보로그제공 (개별 ADC, ADC그룹, 전체ADC)
	 * 
	 * @param adcObject, searchOption, orderOption
	 * @return ArrayList<OBDtoAdcSystemLog>
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcSystemLog> getAdcSystemFaultLog(OBDtoADCObject adcObject, OBDtoSearch searchOption,
			OBDtoOrdering orderOption, Integer accountIndex) throws OBException;

	/**
	 * getFaultLog 장애 모니터링 경보로그를 출력한다. (개별 ADC, ADC그룹, 전체ADC)
	 * 
	 * @param adcObject, searchOption, orderOption
	 * @return ArrayList<OBDtoAdcSystemLog>
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcSystemLog> getAdcSystemFaultLogExOrdering(OBDtoADCObject adcObject,
			OBDtoSearch searchOption, Integer accountIndex) throws OBException;

	/**
	 * 지정된 장비의 네트워크 맵 구성을 위한 virtual server의 상태 및 pool member의 상태 정보를 제공한다.
	 * 
	 * @param adcIndex   : 장비의 index 정보. null 불가.
	 * @param status     : null 가능. 지정된 status만 조회하고자 할 경우에 사용. null일 경우에는 상태에 관계없이
	 *                   조회하여 제공한다.
	 * @param searchKeys : null 가능. 지정된 검색어에 해당되는 정보만 추출하고자 할 경우에 사용. virtual 서버의
	 *                   이름, IP주소를 대상으로 검색한다.
	 * @param fromIndex  : 제공되는 데이터의 시작 index. null일 경우에는 0으로 간주한다.
	 * @param endIndex   : 제공되는 데이터의 종료 index. null일 경우에는 전체 데이터를 제공한다. fromIndex는
	 *                   항상 endIndex보다 작거나 같아야 한다.
	 * @return ArrayList<OBDtoNetworkMap>
	 * @throws OBException
	 */
	public ArrayList<OBDtoNetworkMap> getNetworkMapsNew(Integer adcIndex, Integer lbClass, Integer status,
			String searchKeys, Integer fromIndex, Integer endIndex, Integer accountIndex, String accountRole,
			OBDtoOrdering orderOption) throws OBException;

	/**
	 * 지정된 장비의 virtual server/service 에 대한 요약 정보를 제공한다. F5의 경우에는 virtual server,
	 * Alteon인 경우에는 virtual service 별 통계 정보를 제공한다.
	 * 
	 * @param adcIndex : 장비의 index 정보. null 불가.
	 * @return OBDtoVirtualServerSummary
	 * @throws OBException
	 */
	public OBDtoVirtualServerSummary getVirtualServerStatusSummary(Integer adcIndex) throws OBException;

//	/**
//	 * alteon용 virtual service별 요약 정보를 제공한다.
//	 * 
//	  * @param adcIndex : 장비의 index 정보. null 불가.
//	 * @return OBDtoVirtualServerSummary
//	 * @throws OBException
//	 */
//	public OBDtoVirtualServerSummary getVirtualServiceStatusSummaryAlteon(Integer adcIndex) throws OBException;
//	/**
//	 * 지정된 장비의 virtual 서버의 상세 정보를 제공한다.(alteon 장비의 경우)
//	 * @param adcIndex : 장비의 index 정보. null 불가.
//	 * @param vsIndex : virtual server의 index 정보.  null 불가.
//	 * @return OBDtoAdcVServerAlteon
//	 * @throws OBException
//	 */
//	public OBDtoAdcVServerAlteon getVirtualServerInfoAlteon(Integer adcIndex, String vsIndex) throws OBException;

//	/**
//	 * 지정된 장비의 virtual 서버의 상세 정보를 제공한다.(alteon 장비의 경우)
//	 * @param adcIndex : 장비의 index 정보. null 불가.
//	 * @param vsIndex : virtual server의 index 정보.  null 불가.
//	 * @return OBDtoAdcVServerF5
//	 * @throws OBException
//	 */	
//	public OBDtoAdcVServerF5 getVirtualServerInfoF5(Integer adcIndex, String vsIndex) throws OBException;

	/**
	 * 지정된 장비의 지정된 virtual server/service에서 사용중인 concurrent connections 개수를 조회하여
	 * 제공한다. F5는 virtual server 단위. alteon인 경우에는 virtual service단위.
	 * 
	 * @param adcIndex : 장비의 index 정보. null 불가.
	 * @param vsIndex  : virtual server index. null 불가.
	 * @param srvPort  : service 포트 정보. F5인 경우에는 null, Alteon인 경우에는 지정한다.
	 * @return OBDtoConnectionInfo
	 * @throws OBException
	 */
	public OBDtoConnectionData getVSRealTimeCurrConns(Integer adcIndex, String vsIndex, Integer srvPort)
			throws OBException;

//	/**
//	 * 지정된 장비의 지정된 virtual service에서 사용중인  concurrent connections 개수를 조회하여 제공한다. Alteon장비용.
//	 * 
//	 * @param adcIndex : 장비의 index 정보. null 불가.
//	 * @param vsID : virtual server의 id 정보. null 불가.
//	 * @param virtPort : virtual port 정보. null 불가.
//	 * @return OBDtoConnectionInfo
//	 * @throws OBException
//	 */
//	public OBDtoConnectionData getVSRealTimeCurrConnsAlteon(Integer adcIndex, Integer vsID, Integer virtPort) throws OBException;
//	
	/**
	 * 지정된 장비의 virtual server/service의 connection 이력을 제공한다. F5는 virtual server,
	 * Alteon은 virtual service의 이력을 제공한다.
	 * 
	 * @param adcIndex  : 장비의 index 정보. null 불가.
	 * @param vsIndex   : virtual server의 index. null 불가.
	 * @param srvPort   : service port. F5의 경우에는 null, Alteon인 경우에는 service port를
	 *                  입력한다.
	 * @param beginTime : 시작 시각. null일 경우에는 현재 시점대비 7일로 간주한다. beginTime은 항상
	 *                  endTime보다 같거나 이전 시간이어야 한다.
	 * @param endTime   : 조회 종료 시각. null일 경우에는 현재 시점으로 간주한다.
	 * @return ArrayList<OBDtoConnectionInfo>
	 * @throws OBException
	 */
	public OBDtoConnectionInfo getVSConnections(Integer adcIndex, String vsIndex, Integer srvPort, Date beginTime,
			Date endTime) throws OBException;

//	/**
//	 * alteon의 virtual service의 connection 추이를 제공한다.
//	 * 
//	 * @param adcIndex : 장비의 index 정보. null 불가.
//	 * @param vsID : virtual server의 id 정보. null 불가.
//	 * @param virtPort : virtual port 정보. null 불가.
//	 * @param beginTime : 시작 시각. null일 경우에는 현재 시점대비 7일로 간주한다. beginTime은 항상 endTime보다 같거나 이전 시간이어야 한다.
//	 * @param endTime : 조회 종료 시각. null일 경우에는 현재 시점으로 간주한다. 
//	 * @return ArrayList<OBDtoConnectionInfo> 
//	 * @throws OBException
//	 */
//	public OBDtoConnectionInfo getVSConnectionsAlteon(Integer adcIndex, Integer vsID, Integer virtPort, Date beginTime, Date endTime) throws OBException;
//	/**
//	 * 지정된 장비의 virtual server를 enable/disable 시킨다.
//	 * 
//	 * @param adcIndex : 장비의 index 정보. null 불가.
//	 * @param vsIndex : virtual server의 index 정보. null 불가.
//	 * @param status : 1: enabled, 0: disabled
//	 * @throws OBException
//	 */
//	public void changeVSStatus(Integer adcIndex, String vsIndex, Integer status) throws OBException;

	/**
	 * 할당된 장비를 대상으로 Traffic Map 구성을 위한 레코드 개수를 추출하여 제공한다.
	 * 
	 * @param adcIndex   : ADC 장비의 index. null 불가.
	 * @param searchKeys : null 가능. 지정된 검색어에 해당되는 정보만 추출하고자 할 경우에 사용. virtual 서버의
	 *                   이름, IP주소를 대상으로 검색한다.
	 * @return Integer
	 * @throws OBException
	 */
//	public Integer getTrafficMapsVServerTotalRecordCount(Integer adcIndex, String searchKeys) throws OBException;

	/**
	 * 지정된 ADC 장비의 Traffic Map(VServer) 구성을 위한 데이터를 추출하여 제공한다. F5 장비에 적용.
	 * 
	 * @param adcIndex   : ADC 장비의 index. null 불가.
	 * @param searchKeys : null 가능. 지정된 검색어에 해당되는 정보만 추출하고자 할 경우에 사용. virtual 서버의
	 *                   이름, IP주소를 대상으로 검색한다.
	 * @param fromIndex  : 제공되는 데이터의 시작 index. null일 경우에는 0으로 간주한다. fromIndex는 항상
	 *                   endIndex보다 작거나 같아야 한다.
	 * @param endIndex   : 제공되는 데이터의 종료 index. null일 경우에는 전체 데이터를 제공한다.
	 * @return ArrayList<OBDtoTrafficMapVServer>
	 * @throws OBException
	 */
//	public ArrayList<OBDtoTrafficMapVServer> getTrafficMapsVServerF5(Integer adcIndex, String searchKeys, Integer fromIndex, Integer endIndex) throws OBException;
//
//	public ArrayList<OBDtoTrafficMapVServer> getTrafficMapsVServerPAS(Integer adcIndex, String searchKeys, Integer fromIndex, Integer endIndex) throws OBException;
//
//	public ArrayList<OBDtoTrafficMapVServer> getTrafficMapsVServerPASK(Integer adcIndex, String searchKeys, Integer fromIndex, Integer endIndex) throws OBException;

	/**
	 * 
	 * @param adcIndex
	 * @param searchKeys
	 * @param fromIndex
	 * @param endIndex
	 * @param            orderType. OBDefine.ORDER_TYPE_VSIPADDRESS,
	 *                   ORDER_TYPE_PORT, ORDER_TYPE_VSNAME, ORDER_TYPE_BPS,
	 *                   ORDER_TYPE_PPS, ORDER_TYPE_CPS
	 * @param            orderDir. OBDefine.ORDER_DIR_ASCEND,
	 *                   OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoTrafficMapVServer> getTrafficMapsVServerF5(Integer adcIndex, String searchKeys, Integer fromIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
//
//	/**
//	 * 
//	 * @param adcIndex
//	 * @param searchKeys
//	 * @param fromIndex
//	 * @param endIndex
//	 * @param orderType. OBDefine.ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_PORT, ORDER_TYPE_VSNAME, ORDER_TYPE_BPS, ORDER_TYPE_PPS, ORDER_TYPE_CPS
//	 * @param orderDir. OBDefine.ORDER_DIR_ASCEND, OBDefine.ORDER_DIR_DESCEND
//	 * @return
//	 * @throws OBException
//	 */
//	public ArrayList<OBDtoTrafficMapVServer> getTrafficMapsVServerPAS(Integer adcIndex, String searchKeys, Integer fromIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;

	/**
	 * 
	 * @param adcIndex
	 * @param searchKeys
	 * @param fromIndex
	 * @param endIndex
	 * @param            orderType. OBDefine.ORDER_TYPE_VSIPADDRESS,
	 *                   ORDER_TYPE_PORT, ORDER_TYPE_VSNAME, ORDER_TYPE_BPS,
	 *                   ORDER_TYPE_PPS, ORDER_TYPE_CPS
	 * @param            orderDir. OBDefine.ORDER_DIR_ASCEND,
	 *                   OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoTrafficMapVServer> getTrafficMapsVServerPASK(Integer adcIndex, String searchKeys, Integer fromIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;
//
//	/**
//	 * 지정된 ADC 장비의 Traffic Map(VServer) 구성을 위한 데이터를 추출하여 제공한다. Alteon 장비에 적용.
//	 * 
//	 * @param adcIndex : ADC 장비의 index. null 불가.
//	 * @param searchKeys : null 가능. 지정된 검색어에 해당되는 정보만 추출하고자 할 경우에 사용. virtual 서버의 이름, IP주소를 대상으로 검색한다.
//	 * @param fromIndex : 제공되는 데이터의 시작 index. null일 경우에는 0으로 간주한다. fromIndex는 항상 endIndex보다 작거나 같아야 한다.
//	 * @param endIndex : 제공되는 데이터의 종료 index. null일 경우에는 전체 데이터를 제공한다. 
//	 * @return ArrayList<OBDtoTrafficMapVService> 
//	 * @throws OBException
//	 */
//	public ArrayList<OBDtoTrafficMapVService> getTrafficMapsVServiceAlteon(Integer adcIndex, String searchKeys, Integer fromIndex, Integer endIndex) throws OBException;

	/**
	 * 
	 * @param adcIndex
	 * @param searchKeys
	 * @param fromIndex
	 * @param endIndex
	 * @param            orderType. OBDefine.ORDER_TYPE_VSIPADDRESS,
	 *                   ORDER_TYPE_PORT, ORDER_TYPE_VSNAME, ORDER_TYPE_BPS,
	 *                   ORDER_TYPE_PPS, ORDER_TYPE_CPS
	 * @param            orderDir. OBDefine.ORDER_DIR_ASCEND,
	 *                   OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoTrafficMapVService> getTrafficMapsVServiceAlteon(Integer adcIndex, String searchKeys, Integer fromIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException;

	/**
	 * 지정된 virtual service에서 할당된 멤버들의 트래픽 사용량을 제공한다. F5용.
	 * 
	 * @param adcIndex : ADC 장비의 index. null 불가.
	 * @param vsIndex  : virtual server index. null 불가.
	 * @return OBDtoTrafficMapVService
	 * @throws OBException
	 */
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailF5(Integer adcIndex, String vsIndex)
			throws OBException;

	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailPAS(Integer adcIndex, String vsIndex)
			throws OBException;

	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailPASK(Integer adcIndex, String vsIndex)
			throws OBException;

	/**
	 * 
	 * @param adcIndex
	 * @param vsIndex
	 * @param          orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_PORT,
	 *                 ORDER_TYPE_VSNAME, ORDER_TYPE_BPS, ORDER_TYPE_PPS,
	 *                 ORDER_TYPE_CPS
	 * @param          orderDir. OBDefine.ORDER_DIR_ASCEND,
	 *                 OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBException
	 */
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailF5(Integer adcIndex, String vsIndex,
			Integer orderType, Integer orderDir) throws OBException;

	/**
	 * 
	 * @param adcIndex
	 * @param vsIndex
	 * @param          orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_PORT,
	 *                 ORDER_TYPE_VSNAME, ORDER_TYPE_BPS, ORDER_TYPE_PPS,
	 *                 ORDER_TYPE_CPS
	 * @param          orderDir. OBDefine.ORDER_DIR_ASCEND,
	 *                 OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBException
	 */
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailPAS(Integer adcIndex, String vsIndex,
			Integer orderType, Integer orderDir) throws OBException;

	/**
	 * 
	 * @param adcIndex
	 * @param vsIndex
	 * @param          orderType. ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_PORT,
	 *                 ORDER_TYPE_VSNAME, ORDER_TYPE_BPS, ORDER_TYPE_PPS,
	 *                 ORDER_TYPE_CPS
	 * @param          orderDir. OBDefine.ORDER_DIR_ASCEND,
	 *                 OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBException
	 */
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailPASK(Integer adcIndex, String vsIndex,
			Integer orderType, Integer orderDir) throws OBException;

	/**
	 * 지정된 virtual service에서 할당된 멤버들의 트래픽 사용량을 제공한다. Alteon용.
	 * 
	 * @param adcIndex : ADC 장비의 index. null 불가.
	 * @param vsIndex  : virtual server index. null 불가.
	 * @param srvPort  : service port. null 불가.
	 * @return OBDtoTrafficMapVService
	 * @throws OBException
	 */
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailAlteon(Integer adcIndex, String vsIndex,
			Integer srvPort) throws OBException;

	/**
	 * 
	 * @param adcIndex
	 * @param vsIndex
	 * @param srvPort
	 * @param          orderType. OBDefine.ORDER_TYPE_VSIPADDRESS, ORDER_TYPE_PORT,
	 *                 ORDER_TYPE_VSNAME, ORDER_TYPE_BPS, ORDER_TYPE_PPS,
	 *                 ORDER_TYPE_CPS
	 * @param          orderDir. OBDefine.ORDER_DIR_ASCEND,
	 *                 OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBException
	 */
	public OBDtoTrafficMapVServiceMembers getTrafficMapsVServiceDetailAlteon(Integer adcIndex, String vsIndex,
			Integer srvPort, Integer orderType, Integer orderDir) throws OBException;

	/**
	 * 요약 맵에서 알테온 FLB 그룹의 상세정보를 구한다.
	 * 
	 * @param adcIndex
	 * @param groupIndex
	 * @param orderType
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultGroupMemberPerfInfo getTrafficMapsFlbGroupDetailAlteon(Integer adcIndex, String groupInde)
			throws OBException;

	/**
	 * 물리적 인터페이스(포트)의 상태 정보를 제공한다.
	 * 
	 * @param adcIndex : ADC 장비의 index. null 불가.
	 * @return ArrayList<OBDtoAdcPortStatus>
	 * @throws OBException
	 */
//	public ArrayList<OBDtoAdcPortStatus> getPortStatus(Integer adcIndex) throws OBException;

	/**
	 * 
	 * @param adcIndex
	 * @param          orderType. OBDefine.ORDER_TYPE_NAME, OBDefine.ORDER_TYPE_BPS,
	 *                 OBDefine.ORDER_TYPE_PPS, OBDefine.ORDER_TYPE_STATUS
	 * @param          orderDir. OBDefine.ORDER_DIR_ASCEND,
	 *                 OBDefine.ORDER_DIR_DESCEND
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcPortStatus> getPortStatus(Integer adcIndex, Integer orderType, Integer orderDir)
			throws OBException;

	/**
	 * ADC 장비의 cpu, memory 사용율 추이를 제공한다.
	 * 
	 * @param adcIndex  : ADC 장비의 index. null 불가.
	 * @param beginTime : 조회 시작 시각. null일 경우에는 현재 시점 대비 7일로 간주한다.
	 * @param endTime   : 조회 종료 시각. null일 경우에는 녀재 시점으로 간주한다.
	 * @return OBDtoAdcSysRescStatus
	 * @throws OBException
	 */
	public OBDtoAdcSysRescStatus getAdcSysRescStatus(Integer adcIndex, Date beginTime, Date endTime) throws OBException;

	/**
	 * 
	 * @param adcIndex
	 * @param groupIndex
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoFlbFilterInfo> getTrafficMapsFlbGroupFilterDetailAlteon(Integer adcIndex, String groupIndex)
			throws OBException;

	/** 인터페이스 화면 그래프, 인터페이스 화면 내보내기 **/

	// 내보내기
	public List<OBDtoAdcPortStatus> getPortStatusForDownload(Integer adcIndex, Date beginTime, Date endTime)
			throws OBException;

	public OBDtoMultiDataObj getStatisticsGraphData(Integer adcIndex, String interfaceName, Integer queryType,
			Date beginTime, Date endTime) throws OBException;

	public ArrayList<OBDtoMultiDataObj> getStatisticsGraphDataList(Integer adcIndex,
			ArrayList<String> interfaceNameList, Integer queryType, Date beginTime, Date endTime) throws OBException;

//---- ADC 전체모니터링 ------------------------------------------------------
	/**
	 * 필터와 검색어로 ADC 전체 모니터링 데이터를 구한다.
	 * 
	 * @param scope
	 * @param condition
	 * @param beginIndex
	 * @param endIndex
	 * @param orderType
	 * @param orderDir
	 * @return
	 * @throws OBException
	 */
	public OBDtoMonTotalAdc getTotalAdcList(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalAdcCondition condition, Integer beginIndex, Integer endIndex, Integer orderType,
			Integer orderDir) throws OBException;

	public Integer getTotalAdcListCount(OBDtoAdcScope scope, Integer accountIndex, OBDtoMonTotalAdcCondition condition)
			throws OBException;

	public OBDtoMonTotalAdc getTotalAdcListToExport(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalAdcCondition condition, Integer beginIndex, Integer endIndex) throws OBException;

	public OBDtoMonTotalService getTotalServiceList(OBDtoAdcScope scope, Integer accountIndex, String accountRole,
			OBDtoMonTotalServiceCondition condition, Integer beginIndex, Integer endIndex, Integer orderType,
			Integer orderDir) throws OBException;

	public Integer getTotalServiceListCount(OBDtoAdcScope scope, Integer accountIndex, String accountRole,
			OBDtoMonTotalServiceCondition condition) throws OBException;

	public OBDtoMonTotalService getTotalServiceListToExport(OBDtoAdcScope scope, Integer accountIndex,
			String accountRole, OBDtoMonTotalServiceCondition condition, Integer beginIndex, Integer endIndex)
			throws OBException;

	public OBDtoMonTotalGroup getTotalGroupList(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalGroupCondition condition, Integer beginIndex, Integer endIndex, Integer orderType,
			Integer orderDir) throws OBException;

	public Integer getTotalGroupListCount(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalGroupCondition condition) throws OBException;

	public OBDtoMonTotalGroup getTotalGroupListToExport(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalGroupCondition condition, Integer beginIndex, Integer endIndex) throws OBException;

	public OBDtoMonTotalReal getTotalRealList(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalRealCondition condition, Integer beginIndex, Integer endIndex, Integer orderType,
			Integer orderDir) throws OBException;

	public Integer getTotalRealListCount(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalRealCondition condition) throws OBException;

	public OBDtoMonTotalReal getTotalRealListToExport(OBDtoAdcScope scope, Integer accountIndex,
			OBDtoMonTotalRealCondition condition, Integer beginIndex, Integer endIndex) throws OBException;

	/**
	 * VSThrouhput 멀티 차트를 보여준다.
	 * 
	 * @param              ArrayList<String> target
	 * @param searchOption
	 * @return ArrayList<OBDtoGroupHistory>
	 * @throws OBException
	 */
	public OBDtoServiceMonitoringChart getServiceMonitoringChart(String target, String name, OBDtoSearch searchOption)
			throws OBException;

	public void saveVsDescription(ServiceMapVsDescDto descDto) throws Exception;
}
