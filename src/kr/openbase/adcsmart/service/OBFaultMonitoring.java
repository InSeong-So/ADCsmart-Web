/**
 * 장애 모니터링 관련 인터페이스.
 */
package kr.openbase.adcsmart.service;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoTargetObject;
import kr.openbase.adcsmart.service.dto.OBDtoTrafficMapVServiceMembers;
import kr.openbase.adcsmart.service.dto.fault.OBDtoAdcCurrentSession;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCpuDataObj;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCpuHistory;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultDataObj;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultGroupMemberPerfInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultGroupPerfInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultHWStatus;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultPreBpsConnChart;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultSessionInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultSvcPerfInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoL2SearchInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoL2SearchOption;
import kr.openbase.adcsmart.service.dto.fault.OBDtoSessionSearchOption;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcMonCpuDataObj;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBFaultMonitoring
{
	////////////////////////////////////////////////L2 검색 모니터링 인터페이스.///////////////////////////////////////////////////////
	/**
	 * L2 정보를 ADC로부터 추출 후 DB에 저장후 저장된 결과를 조건에 맞게 제공한다.
	 * 
	 * @param object
	 * @param searchKeyList
	 * @param pagingOption
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoL2SearchInfo> searchL2InfoList(OBDtoADCObject object, ArrayList<OBDtoL2SearchOption> searchKeyList, OBDtoSearch pagingOption) throws OBException;
	
	/**
	 * L2 정보를 페이징, 정렬 조건에 맞게 제공한다.
	 * @param object
	 * @param pagingOption
	 * @param orderOption
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoL2SearchInfo> searchL2InfoListBySort(OBDtoADCObject object, OBDtoSearch pagingOption, OBDtoOrdering orderOption) throws OBException;

	/**
	 * L2 정보의 총 개수를 제공한다.
	 * @param object
	 * @return
	 * @throws OBException
	 */
	public Integer getL2InfoTotalCount(OBDtoADCObject object) throws OBException;
	
	/**
	 * 로컬 디스크에 저장된 정보를 삭제한다.
	 * @param object
	 * @throws OBException
	 */
	public void cleanLocalL2List(OBDtoADCObject object) throws OBException;
	
	///////////////////////////////////////////////////////세션 검색 모니터링 인터페이스.///////////////////////////////////////////////////////
	/**
	 * 세션 검색 모니터링을 위한 인터페이스.
	 * @param object. adc 장비의 index
	 * @param searchKey. 검색 키워드. 
	 * @param pagingOption. paging을 위한 옵션.
	 * @return
	 * @throws OBException
	 */
//	public ArrayList<OBDtoFaultSessionInfo> searchSessionInfoList(OBDtoADCObject object, ArrayList<OBDtoSessionSearchOption> searchKey, OBDtoSearch pagingOption ,OBDtoOrdering orderOption) throws OBException;

	public Integer searchSessionInfoList(OBDtoADCObject object, ArrayList<OBDtoSessionSearchOption> searchKey, OBDtoSearch pagingOption ,OBDtoOrdering orderOption) throws OBException;


	/** 
	 * 세션 검색 모니터링 인터페이스. 정렬시에 사용한다.
	 * @param adcIndex
	 * @param pagingOption
	 * @param orderOption
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoFaultSessionInfo> searchSessionInfoListBySort(OBDtoADCObject object, OBDtoSearch pagingOption, OBDtoOrdering orderOption) throws OBException;
	
	/**
	 * 세션 검색 모니터링 인터페이스 리스트개수를 구한다.
	 * @param object
	 * @param pagingOption
	 * @param orderOption
	 * @return
	 * @throws OBException
	 */
	public Integer getSessionListCount(OBDtoADCObject object, OBDtoSearch pagingOption) throws OBException;
	
	/**
	 * 로컬 디스크에 저장된 데이터를 삭제한다.
	 * @param object
	 * @return
	 * @throws OBException
	 */
	public void cleanLocalSessionList(OBDtoADCObject object) throws OBException;
	
	/////////////////////////////////////////////////////////서비스 성능 모니터링 인터페이스 ///////////////////////////////////////////////////////
	/**
	 * 실시간 bps, connection 정보 추출.
	 * @param object
	 * @param vsIndex
	 * @param svcPort
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultBpsConnInfo getRealTimeBpsConnInfo(OBDtoADCObject object, String vsIndex, Integer svcPort, OBDtoFaultBpsConnInfo prevInfo) throws OBException;
	
	/**
	 * 성능 정보 추출.
	 * @param object
	 * @param searchOption
	 * @param orderOption
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoFaultSvcPerfInfo> getSvcPerfInfoList(OBDtoADCObject object, OBDtoSearch searchOption, OBDtoOrdering orderOption, Integer accountIndex, String accountRole) throws OBException;

	/**
	 * 그룹 성능 정보를 구한다.
	 * 그룹 기본 식별 항목, connection, bps 정보
	 * @param object
	 * @param searchOption
	 * @param orderOption
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoFaultGroupPerfInfo> getGroupPerformanceList(OBDtoTargetObject object, OBDtoSearch searchOption, OBDtoOrdering orderOption) throws OBException;
	/**
	 * 성능 정보 개수를 제공한다. 지정된 검색 조건에 따른 성능 정보 개수를 제공한다.
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public Integer getSvcPerfInfoTotalCount(OBDtoADCObject object, OBDtoSearch searchOption, Integer accountIndex, String accountRole) throws OBException;
	
	/**
	 * 지정한 ADC 범위에서 group 개수를 구한다. single adc만 구현돼 있다.
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public Integer getGroupCount(OBDtoTargetObject object, OBDtoSearch searchOption) throws OBException; 

	/**
	 * 지정한 group의 member수를 구한다.
	 * @param groupIndex
	 * @return
	 * @throws OBException
	 */
	public Integer getGroupMemberCount(String groupIndex) throws OBException;
	
	   /**
     * 지정된 service 멤버에 대한 이력 데이터와 이전 데이터를 제공한다.
     * 
     * @param object. service 멤버의 index 제공.  카테고리 index = CATEGORY_VSRV_MEM 사용.
     * @param searchOption
     * @return
     * @throws OBException
     */
    public OBDtoFaultPreBpsConnChart getSvcPerfVSrvMemberHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
    
    /**
     * 지정된 service 멤버에 대한 이력 데이터와 이전 데이터를 MAX, AVG, Current 제공한다.
     * 
     * @param object. service 멤버의 index 제공.  카테고리 index = CATEGORY_VSRV_MEM 사용.
     * @param searchOption
     * @return
     * @throws OBException
     */
    public OBDtoFaultPreBpsConnChart getSvcPerfVSrvMemberMaxAvgHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
	
	/**
	 * service member의 개수를 제공한다.
	 * @param object. service index 정보. 카테고리 index = CATEGORY_VS or CATEGORY_VSVC 사용.
	 * @param searchOption. 검색 조건.
	 * @return
	 * @throws OBException
	 */
	public Integer getSvcPerfVSrvMemberTotalCount(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;

	/**
	 * 특정 group 멤버들의 bps/connection 추이를 구한다.
	 * 
	 * @param groupIndex
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultGroupMemberPerfInfo getGroupMemberPerformanceList(String groupIndex, OBDtoSearch searchOption, OBDtoOrdering orderOption) throws OBException;

	
	/**
	 * 지정된 service/server에 할당된 멤버 정보를 제공한다.
	 * @param object. service index 정보. 카테고리 index = CATEGORY_VS or CATEGORY_VSVC 사용.
	 * @return
	 * @throws OBException
	 */
	public OBDtoTrafficMapVServiceMembers 	getSvcPerfVSrvMemberInfo(OBDtoADCObject object, OBDtoSearch searchOption, OBDtoOrdering orderOption) throws OBException;
	
	/**
	 * bps/connection 추이 정보를 추출한다.
	 * @param object
	 * @param vsIndex
	 * @param svcPort
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultPreBpsConnChart getBpsConnHistory(OBDtoADCObject object, String vsIndex, Integer svcPort, OBDtoSearch searchOption) throws OBException;
	/**
     * bps/connection Max Avg current 추이 정보를 추출한다.
     * @param object
     * @param vsIndex
     * @param svcPort
     * @param searchOption
     * @return
     * @throws OBException
     */
    public OBDtoFaultPreBpsConnChart getBpsConnMaxAvgHistory(OBDtoADCObject object, String vsIndex, Integer svcPort, OBDtoSearch searchOption) throws OBException;
	/**
	 * 
	 * @param groupIndex
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoFaultBpsConnInfo> getGroupBpsConnHistory(String groupIndex, OBDtoSearch searchOption) throws OBException;
	/**
	 * 
	 * @param groupIndex
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoFaultBpsConnInfo> getGroupMemberBpsConnHistory(String realDbIndex, OBDtoSearch searchOption) throws OBException;
	/**
	 * 응답 시간 추이 정보 및 전일 추이 정보를 추출한다.
	 * @param object
	 * @param vsIndex
	 * @param svcPort
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultPreBpsConnChart getResponseTimeHistory(OBDtoADCObject object, String vsIndex, Integer svcPort, OBDtoSearch searchOption) throws OBException;
	
	   /**
     * 응답 시간 추이 Current, Max, Avg 정보를 추출한다.
     * @param object
     * @param vsIndex
     * @param svcPort
     * @param searchOption
     * @return
     * @throws OBException
     */
    public OBDtoFaultPreBpsConnChart getResponseTimeMaxAvgHistory(OBDtoADCObject object, String vsIndex, Integer svcPort, OBDtoSearch searchOption) throws OBException;
	
	/////////////////////////////////////////////////////////장비 모니터링 인터페이스 ///////////////////////////////////////////////////////
	
	/**
	 * 장비 모니터링 인터페이스-장비의 상태 정보 제공.
	 * @param object
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultHWStatus getADCMonHWStatus(OBDtoADCObject object) throws OBException;
	/**
	 * 세션 이력 정보 제공.
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultDataObj getADCMonSessionHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
	public OBDtoAdcCurrentSession getADCMonSessionHistoryNew(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
	
	/**
	 * Power Supply 동작 이력 정보 제공.
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
//	public OBDtoFaultDataObj getADCMonPowerSupplyHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
	
	/**
	 * CPU 동작 이력 정보 제공.
	 * 
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public OBDtoAdcMonCpuDataObj getADCMonCpuData(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
	/**
	 * CPU 동작 이력 정보 제공.
	 * 
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public OBDtoAdcMonCpuDataObj getADCMonCpuHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;	
	
	
	/**
	 * CPU SP별 history
	 * 
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultCpuDataObj getADCMonCpuSPHistory(OBDtoADCObject object, OBDtoSearch searchOption, Integer cpuNum) throws OBException;
	
	/**
	 * CPU SP Connection 정보 제공
	 * @param object
	 * @return
	 * @throws OBException
	 */	
	public OBDtoFaultCpuHistory getADCMonCpuSpConnectionInfo(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
	
	/**
	 * 메모리 상태 이력 정보 제공.
	 * 
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultDataObj getADCMonMemoryHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
	
	/**
	 * HDD 상태 이력 정보 제공.
	 * 
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
//	public OBDtoFaultDataObj getADCMonHddHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
	
	/**
	 * Bps 상태 이력 정보 제공.
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultDataObj getADCMonBpsHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
	
	/**
	 * 온도 상태 이력 정보 제공.
	 * 
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
//	public OBDtoFaultDataObj getADCMonTemperatureHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;

	/**
	 * Fan 상태 이력 정보 제공.
	 * 
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
//	public OBDtoFaultDataObj getADCMonFanHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
	
	/**
	 * Link 상태 이력 정보 제공.
	 * 
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
//	public OBDtoFaultDataObj getADCMonLinkHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
	
	/**
	 * Pkt Error 상태 이력 정보 제공.
	 * 
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultDataObj getADCMonPktErrHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;

	/**
	 * Pkt drop 상태 이력 정보 제공.
	 * 
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultDataObj getADCMonPktDropHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
	
	/**
	 * http request 이력 정보 제공. 
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultDataObj getADCMonHttpReqestHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;

	/**
	 * ssl transaction 이력 정보 제공.
	 * @param object
	 * @param searchOption
	 * @return
	 * @throws OBException
	 */
	public OBDtoFaultDataObj getADCMonSSLTransactionHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException;
}
