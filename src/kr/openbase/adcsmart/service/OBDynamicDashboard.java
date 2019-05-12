package kr.openbase.adcsmart.service;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dashboard.dto.OBDtoDashboardInfo;
import kr.openbase.adcsmart.service.dashboard.dto.OBDtoWidgetItemInfo;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardStatusNotification;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoGroupHistory;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnInfo;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBDynamicDashboard
{
	/**
	 * 위젯 목록을 제공한다.
	 * 
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoWidgetItemInfo> getWidgetItemList() throws OBException;
	
	/**
	 * 지정된 대시보드 정보를 조회한다.
	 * @param index
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardInfo getDashboardInfo(String index) throws OBException;
	
	/**
	 * 신규 대시보드를 추가한다.
	 * @param info
	 * @param extraInfo
	 * @throws OBException
	 */
	public void addDashboardInfo(OBDtoDashboardInfo info, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * 지정된 대시보드를 삭제한다.
	 * @param index
	 * @param extraInfo
	 * @throws OBException
	 */
	public void delDashboardInfo(String index, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * 지정된 대시보드 정보를 수정한다.
	 * @param index
	 * @param extraInfo
	 * @throws OBException
	 */
	public void setDashboardInfo(String index, OBDtoDashboardInfo dashboardInfo, OBDtoExtraInfo extraInfo) throws OBException;

	/**
	 * 전체 대시보드 목록 정보를 제공한다.
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardInfo> getDashboardInfoList() throws OBException;
	
	/**
	 * 최근에 사용한 대시보드 목록을 제공한다. 최대 3개까지만 제공.
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardInfo> getDashboardInfoLastList() throws OBException;
	
	/**
	 * 지정된 객체에 포함된 하부 객체 목록을 제공한다.
	 * 
	 * @param target
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoADCObject> getInvolvedObjectList(OBDtoADCObject target, OBDtoExtraInfo extraInfo) throws OBException;
	public ArrayList<OBDtoADCObject> getInvolvedObjectListAlteon(OBDtoADCObject target, OBDtoExtraInfo extraInfo) throws OBException;
    public ArrayList<OBDtoADCObject> getInvolvedVsList(OBDtoADCObject target, OBDtoExtraInfo extraInfo) throws OBException;
    public ArrayList<OBDtoADCObject> getInvolvedMemberList(OBDtoADCObject target, OBDtoExtraInfo extraInfo) throws OBException;
	/**
	 * 지정된 ADC에 포함된 flb Group 목록을 제공한다.
	 * 
	 * @param target
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoADCObject> getFlbGroupList(OBDtoADCObject target, OBDtoExtraInfo extraInfo) throws OBException;
	
	
	/**
	 * 위젯 no.19 응답 시간 추이 Chart 위젯
	 * 
	 * @param target, searchOption
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDataObj> getResponseTimeHistoryByIndex(OBDtoADCObject target, OBDtoSearch searchOption) throws OBException;
	   /**
     * 위젯 no.19 응답 시간 추이 Chart 위젯 Group
     * 
     * @param target, searchOption
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoGroupHistory> getResponseTimeHistoryByIndexGroup(OBDtoADCObject target, OBDtoSearch searchOption) throws OBException;
    
	/**
	 * 위젯 no.8 Vs ConcurrentSession 추이 Chart
	 * 
	 * @param target, searchOption
	 * @return
	 * @throws OBException
	 */

	public ArrayList<OBDtoFaultBpsConnInfo> getVsConcurrentSessionHistoryByIndex(OBDtoADCObject target, OBDtoSearch searchOption) throws OBException;
	
	 /**
     * 위젯 no.8 Vs ConcurrentSession 추이 Chart Group
     * 
     * @param target, searchOption
     * @return
     * @throws OBException
     */
	public ArrayList<OBDtoGroupHistory> getVsConcurrentSessionHistoryByIndexGroup(OBDtoADCObject target, OBDtoSearch searchOption) throws OBException;

	   /**
     * 위젯 no.9 Vs Throughput 추이 Chart 
     * 
     * @param target, searchOption
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoFaultBpsConnInfo> getVsThroughputHistoryByIndex(OBDtoADCObject target, OBDtoSearch searchOption) throws OBException;
    
    /**
     * 위젯 no.9 Vs Throughput 추이 Chart Group
     * 
     * @param target, searchOption
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoGroupHistory> getVsThroughputHistoryByIndexGroup(OBDtoADCObject target, OBDtoSearch searchOption) throws OBException;
	
	   /**
     * 위젯 no.10 ADCStatusNotification
     * 
     * @param abcObject
     * @return
     * @throws OBException
     */
	public ArrayList<OBDtoDashboardStatusNotification> getAdcStatusNotification(OBDtoADCObject adcObject) throws Exception;
	
     /**
      * 위젯 no.11 VSStatusNotification
      * 
      * @param abcObject
      * @return
      * @throws OBException
      */
    public ArrayList<OBDtoDashboardStatusNotification> getVsStatusNotification(OBDtoADCObject adcObject) throws Exception;
      
     /**
     * 위젯 no.12 MemberStatusNotification
     * 
     * @param abcObject
     * @return
     * @throws OBException
     */
    public ArrayList<OBDtoDashboardStatusNotification> getMemberStatusNotification(OBDtoADCObject adcObject) throws Exception;
    
}
