package kr.openbase.adcsmart.service;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardAdcSummary;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardFaultStatus;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardSlbChangeStatus;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardTraffic;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardTrafficStatus;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardVSConnHistory;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardVSSummary;
import kr.openbase.adcsmart.service.utility.OBException;

/**
 * ADC 모니터링 대시보드.
 * 
 * @author root
 *
 */
public interface OBDashboardAdc
{
	/**
	 * ADC 별 connection/장애 정보.
	 * @param accountIndex
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardAdcSummary getAdcGroupSummary(Integer accountIndex) throws OBException;
	
	/**
	 * 지정된 object의 connection을 구한다.
	 * @param accountIndex
	 * @param object
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDataObj> getAdcConnectionGraph(Integer accountIndex, OBDtoADCObject object, Date fromTime, Date toTime) throws OBException;

	/**
	 * 지정된 object의 throughput을 구한다.
	 * 
	 * @param accountIndex
	 * @param object
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDataObj> getAdcThroughputGraph(Integer accountIndex, OBDtoADCObject object, Date fromTime, Date toTime) throws OBException;
	
	/**
	 * 지정된 object을 대상으로 하는 Virtual server 현황을 제공한다.
	 * 
	 * @param accountIndex
	 * @param object	 
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardVSSummary getVSSummary(Integer accountIndex, OBDtoADCObject object) throws OBException;
	
	/**
	 * TOP 10 Virtual Server 트래픽 현황을 제공한다.
	 * 
	 * @param accountIndex
	 * @param object
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardTraffic> getVSConnectionListTop10(Integer accountIndex, OBDtoADCObject object, OBDtoOrdering orderOption) throws OBException;
	
	/**
	 * 장애 모니터링 현황을 제공한다. 최근 1주일 기준으로 장애 발생 건수(일단위) 및 최근 발생된 장애 내역을 제공한다.
	 * @param accountIndex
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	/**
	 * 
	 * @param object
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDataObj> getVirtualServerConnectionGraph(OBDtoDashboardTraffic object, Date fromTime, Date toTime) throws OBException;

	/**
	 * 
	 * @param vendor
	 * @param vsIndex
	 * @param port
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDataObj> getVirtualServerThroughputGraph(OBDtoDashboardTraffic object, Date fromTime, Date toTime) throws OBException;
	
	public OBDtoDashboardFaultStatus getFaultMonitoring(Integer accountIndex) throws OBException; 
	public OBDtoDashboardFaultStatus getFaultMonitoring(Integer accountIndex, Date fromTime, Date toTime) throws OBException;
	/**
	 * 기존 데시보드와 다르게 5개까지 지원
	 */
	public OBDtoDashboardFaultStatus getDynamicFaultMonitoring(Integer accountIndex, Date fromTime, Date toTime) throws OBException;	
	/**
	 * ADC 설정 변경 현황을 제공한다. 최근 1주일 기준 설정 변경 건수(일단위) 및 최근 변경 내역 목록을 제공한다.
	 * 
	 * @param accountIndex
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardSlbChangeStatus getSlbChangeMonitoring(Integer accountIndex) throws OBException;
	public OBDtoDashboardSlbChangeStatus getSlbChangeMonitoring(Integer accountIndex, Date fromTime, Date toTime) throws OBException;
	/**
	 * 기존 데시보드와 다르게 5개까지 지원
	 */
	public OBDtoDashboardSlbChangeStatus getDynamicSlbChangeMonitoring(Integer accountIndex, Date fromTime, Date toTime) throws OBException;
	
	/**
	 * 전체 ADC를 기준으로 발생된 트래픽 추이를 제공한다. "전체 ADC 트래픽 현황" 에서 사용함.
	 * 
	 * @param accountIndex
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDataObj> getTotalAdcTrafficGraph(Integer accountIndex) throws OBException;
	public ArrayList<OBDtoDataObj> getTotalAdcTrafficGraph(Integer accountIndex, Date fromTime, Date toTime) throws OBException;
	
	/**
	 * 전체 ADC를 기준으로 현재/전날의 트래픽 발생 현황을 제공한다. "전체트래픽/전일트래픽" 테이블에서 사용함.
	 * 
	 * @param accountIndex
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardTrafficStatus getTotalAdcTrafficInfo(Integer accountIndex) throws OBException;
	/**
	 * ADC 장비를 기준으로 TOP5 트래픽 발생 장비 정보를 제공한다. "TOP 5 ADC 트래픽"에서 사용.
	 * 
	 * @param accountIndex
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardTraffic> getAdcTrafficListTop5(Integer accountIndex) throws OBException;
	/**
	 * Dynamic 용 TOP10	
	 */
	public ArrayList<OBDtoDashboardTraffic> getAdcTrafficListTop10(Integer accountIndex) throws OBException;

	/**
	 * TOP 5 Vritual Server IP에 대한 최근 25시간 발생 추이 데이터를 제공한다. " TOP 5 Virtual Server Connection"에서 사용.
	 * 
	 * @param accountIndex
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	
	public ArrayList<OBDtoDashboardVSConnHistory> getVSConnectionGraphListTop5(Integer accountIndex) throws OBException;
	public ArrayList<OBDtoDashboardVSConnHistory> getVSConnectionGraphListTop5(Integer accountIndex, Date fromTime, Date toTime) throws OBException;
	
	/**
	 * 현재 Vritual Server 트래픽 TOP 5 데이터를 구한다. "Virtual Server IP"에서 사용.
	 * 
	 * @param accountIndex
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardTraffic> getVSTrafficListTop5(Integer accountIndex) throws OBException;
	

}
