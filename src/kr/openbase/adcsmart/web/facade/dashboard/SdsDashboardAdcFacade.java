/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dashboard;

/*
 * 코딩 가이드:
 * 1. public 함수의 Exception은 무조건 OBException, Exception 두종류로  throw 한다.
 * 2. 내부에서 exception의 try-catch는 가급적이면 하지 않는다. 필요할 경우에만 추가한다.
 * 3. AdcFacade.java 파일을 참조한다.
 *
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.OBAdcManagement;
import kr.openbase.adcsmart.service.OBDashboardAdc;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardAdcSummary;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardFaultStatus;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardSlbChangeStatus;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardTraffic;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardTrafficStatus;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardVSConnHistory;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardVSSummary;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBDashboardAdcImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonFaultStatus;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonSlbChangeStatus;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonSummary;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonTraffic;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonTrafficStatus;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoDashboardTop5Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author paul
 *
 */
@Component
public class SdsDashboardAdcFacade 
{

	private static transient Logger log = LoggerFactory.getLogger(SdsDashboardAdcFacade.class);
//	private OBDashboardSds dashboard;
	private OBDashboardAdc dashboardAdc;
	private OBAdcManagement managementAdc;
	
	public SdsDashboardAdcFacade() 
	{
		dashboardAdc = new OBDashboardAdcImpl();
		managementAdc = new OBAdcManagementImpl();
	}
	
	//OBDtoDashboardAdcSummary
	/**
	 * ADC 별 connection/장애 정보.
	 * @param accountIndex
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoAdcInfo> getAdcInfoListInGroup(Integer groupIndex, Integer accountIndex) throws OBException
	{
		ArrayList<OBDtoAdcInfo> retVal = managementAdc.getAdcInfoListInGroup(groupIndex, accountIndex);
			
		log.debug("retVal : {}", retVal);	
		return retVal;
	}
	
	public ArrayList<OBDtoAdcGroup> getAdcGroupList(Integer groupIndex) throws OBException
	{
		ArrayList<OBDtoAdcGroup> retVal = managementAdc.getAdcGroupList(groupIndex);
		
		log.debug("retVal : {}", retVal);	
		return retVal;
	}
	public OBDtoAdcInfo getAdcInfo(Integer adcIndex) throws OBException
	{
		OBDtoAdcInfo retVal = managementAdc.getAdcInfo(adcIndex);
		
		log.debug("retVal : {}", retVal);	
		return retVal;
	}
	
	public OBDtoAdcmonSummary getAdcGroupSummaryConversion(Integer accountIndex) throws OBException, Exception
	{
		OBDtoDashboardAdcSummary retVal = dashboardAdc.getAdcGroupSummary(accountIndex);
		
		log.debug("retVal : {}", retVal);		
		return new OBDtoAdcmonSummary(retVal);
	}
	public OBDtoADCObject getAdcObject(Integer category,Integer index,String name ,Integer vendor, Integer status ,String  desciption) throws OBException, Exception
	{
		OBDtoADCObject selectedObj = new OBDtoADCObject();
		
		selectedObj.setCategory(category);
		selectedObj.setIndex(index);
		selectedObj.setName(name);
		selectedObj.setVendor(vendor);
		selectedObj.setDesciption(desciption);
		
		return selectedObj;
	}
	
	
	
	public OBDtoDashboardAdcSummary getAdcGroupSummary(Integer accountIndex) throws OBException, Exception 
	{
		try 
		{
//			List<DashboardAdcSummaryDto> dashAdcSummaryList = null;			
//			List<OBDtoDashboardAdcSummary> adcGroupsFromSvc = null;
			
			log.debug("accountIndex:{}", accountIndex);
			OBDtoDashboardAdcSummary retVal = dashboardAdc.getAdcGroupSummary(accountIndex);
						
			log.debug("{}", retVal);
			
			return retVal;
			
		} 
		catch (OBException e) 
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	//Group Connection 팝업
	/**
	 * 지정된 object에서 발생된 connection 내역을 제공한다.
	 * @param accountIndex
	 * @param object
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	public List<OBDtoDataObj> getAdcConnectionGraph(Integer accountIndex, OBDtoADCObject object, Date startTime, Date endTime) throws OBException, Exception
	{		
		ArrayList<OBDtoDataObj> retVal = dashboardAdc.getAdcConnectionGraph(accountIndex, object, startTime, endTime);
		log.debug("retVal : {}", retVal);
		return retVal;
	}
	
	//Group Throughput 팝업
	/**
	 * 지정된 object에서 발생된 장애 건수를 제공한다.
	 * 
	 * @param accountIndex
	 * @param object
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	public List<OBDtoDataObj> getAdcThroughputGraph(Integer accountIndex, OBDtoADCObject object, Date startTime, Date endTime) throws OBException, Exception
	{	
		ArrayList<OBDtoDataObj> retVal = dashboardAdc.getAdcThroughputGraph(accountIndex, object, startTime, endTime);
		log.debug("retVal : {}", retVal);
		return retVal;
	}
	
	//VS ConnectionGraph 팝업 - 그룹 하위 Depth
	/**	
	 * 
	 * @param OBDtoDashboardTraffic object
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	
	public ArrayList<OBDtoDataObj> getVirtualServerConnectionGraph(OBDtoDashboardTraffic object, Date fromTime, Date toTime) throws OBException, Exception
	{
		ArrayList<OBDtoDataObj> retVal = dashboardAdc.getVirtualServerConnectionGraph(object, fromTime, toTime);
		log.debug("retVal : {}", retVal);
		return retVal;
	}
	
	//VS ThroughputGraph 팝업 - 그룹 하위 Depth
	/**	
	 * 
	 * @param OBDtoDashboardTraffic object
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	
	public ArrayList<OBDtoDataObj> getVirtualServerThroughputGraph(OBDtoDashboardTraffic object, Date fromTime, Date toTime) throws OBException, Exception
	{
		ArrayList<OBDtoDataObj> retVal = dashboardAdc.getVirtualServerThroughputGraph(object, fromTime, toTime);
		log.debug("retVal : {}", retVal);
		return retVal;
	}
	

	
	private OBDtoDashboardVSSummary getVSInitValue()
	{
		OBDtoDataObj obj = new OBDtoDataObj();
		obj.setDiff(0L);
		obj.setValue(0L);
		
		OBDtoDashboardVSSummary retVal = new OBDtoDashboardVSSummary();
		
		retVal.setAvail(obj);
		retVal.setDisable(obj);
		retVal.setTotal(obj);
		retVal.setUnavail(obj);
		retVal.setUnavailLessNDays(obj);
		retVal.setUnavailOverNDays(obj);
		
		return retVal;
	}
	//
	/**
	 * 지정된 object을 대상으로 하는 Virtual server 현황을 제공한다.
	 * 
	 * @param accountIndex
	 * @param object	 
	 * @return
	 * @throws OBException
	 */
	public OBDtoDashboardVSSummary getTotalVSSummary(Integer accountIndex, OBDtoADCObject object) throws OBException, Exception
	{
		if(object==null)
		{
			return getVSInitValue();
		}
		OBDtoDashboardVSSummary retVal  = dashboardAdc.getVSSummary(accountIndex, object);
		
//		OBDtoDashboardVSSummary retVal = new OBDtoDashboardVSSummary(); 
//		OBDtoDataObj obj = new OBDtoDataObj();
//		obj.setDiff(123L);
//		obj.setValue(12345L);
//		retVal.setAvail(obj);
//		retVal.setDisable(obj);
//		retVal.setTotal(obj);
//		retVal.setUnavail(obj);
//		retVal.setUnavailLessNDays(obj);
//		retVal.setUnavailOverNDays(obj);
		return retVal;
	}
	
	//
	/**
	 * TOP 10 Virtual Server 트래픽 현황을 제공한다.
	 * 
	 * @param accountIndex
	 * @param object
	 * @return
	 * @throws OBException
	 */
	
	public ArrayList<OBDtoAdcmonTraffic> getTop10VSTrafficConversion(Integer accountIndex, OBDtoADCObject object) throws OBException, Exception
	{
	    OBDtoOrdering ordering = new OBDtoOrdering(); // 사용하지 않는 구형 대시보드 코드이기 때문에 임시 조치 한다.
		ArrayList<OBDtoDashboardTraffic> trafficList = dashboardAdc.getVSConnectionListTop10(accountIndex, object, ordering);
		ArrayList<OBDtoAdcmonTraffic> retVal = new ArrayList<OBDtoAdcmonTraffic>();
		
		for(OBDtoDashboardTraffic traffic: trafficList)
		{
			retVal.add(new OBDtoAdcmonTraffic().toAdcmonTrafficContent(traffic));
		}
		return retVal;
	}
	public List<OBDtoDashboardTraffic> getTop10VSTrafficList(Integer accountIndex, OBDtoADCObject object) throws OBException, Exception
	{
		log.debug("accountIndex:{}, object[]", new Object[]{accountIndex, object});
		
		if(object==null)
		{
			return  new ArrayList<OBDtoDashboardTraffic>();
		}
		 OBDtoOrdering ordering = new OBDtoOrdering(); // 사용하지 않는 구형 대시보드 코드이기 때문에 임시 조치 한다.	
		List<OBDtoDashboardTraffic> retVal = dashboardAdc.getVSConnectionListTop10(accountIndex, object, ordering);
		
		
		log.debug("{}", retVal);
//			List<OBDtoDashboardTraffic> retVal = new ArrayList<OBDtoDashboardTraffic>();
//			OBDtoDashboardTraffic obj = new OBDtoDashboardTraffic();
//			obj.setIndex("aaaaa");
//			obj.setNameIp("1.1.1.1");
//			obj.setPort(80);
//			obj.setStatus(1);
//			obj.setVendor(1);
//			OBDtoDataObj connObj = new OBDtoDataObj();
//			connObj.setDiff(12345L);
//			connObj.setOccurTime(OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now())));
//			connObj.setValue(12345L);
//			obj.setConnection(connObj);
//			obj.setThroughput(connObj);
//			adcTop10VSTrafficListFromSvc.add(obj);
//			
//			OBDtoDashboardTraffic obj2 = new OBDtoDashboardTraffic();
//			obj2.setIndex("bbb");
//			obj2.setNameIp("2.2.2.2");
//			obj2.setPort(80);
//			obj2.setStatus(0);
//			obj2.setVendor(2);
//			OBDtoDataObj thrObj = new OBDtoDataObj();
//			thrObj.setDiff(12345L);
//			thrObj.setOccurTime(OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now())));
//			thrObj.setValue(12345L);
//			obj2.setConnection(connObj);
//			obj2.setThroughput(thrObj);
//			adcTop10VSTrafficListFromSvc.add(obj2);
		
		return retVal;
	}	
	
	//장애 모니터링 현황(1주일)
	/**
	 * 장애 모니터링 현황을 제공한다. 최근 1주일 기준으로 장애 발생 건수(일단위) 및 최근 발생된 장애 내역을 제공한다.
	 * @param accountIndex
	 * @return
	 * @throws OBException
	 */
	public OBDtoAdcmonFaultStatus getFaultMonitoring(Integer accountIndex, Date fromTime, Date toTime) throws OBException, Exception
	{
		OBDtoDashboardFaultStatus item = dashboardAdc.getFaultMonitoring(accountIndex, fromTime, toTime);
		return new OBDtoAdcmonFaultStatus().toAdcmonFaultStatus(item);
	}
	
	//ADC 설정 변경 현황(1주일)
	/**
	 * ADC 설정 변경 현황을 제공한다. 최근 1주일 기준 설정 변경 건수(일단위) 및 최근 변경 내역 목록을 제공한다.
	 * 
	 * @param accountIndex
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	public OBDtoAdcmonSlbChangeStatus getSlbChangeMonitoring(Integer accountIndex, Date fromTime, Date toTime) throws OBException, Exception
	{
		OBDtoDashboardSlbChangeStatus item= dashboardAdc.getSlbChangeMonitoring(accountIndex, fromTime, toTime);
		//return  OBDtoAdcmonSlbChangeStatus.toAdcmonSlbChangeStatus(item);
		return  new OBDtoAdcmonSlbChangeStatus().toAdcmonSlbChangeStatus(item);
	
	}
	
	//전체 ADC 트래픽 현황
	/**
	 * 전체 ADC를 기준으로 발생된 트래픽 추이를 제공한다. "전체 ADC 트래픽 현황 Chart" 에서 사용함.
	 * 
	 * @param accountIndex
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDataObj> getTotalAdcTrafficGraph(Integer accountIndex, Date fromTime, Date toTime) throws OBException, Exception
	{
		log.debug("accountIndex:{}", new Object[]{accountIndex});
		ArrayList<OBDtoDataObj> adcTrafficFromSvc = dashboardAdc.getTotalAdcTrafficGraph(accountIndex, fromTime, toTime);
		return adcTrafficFromSvc;		
	}
	
	/**
	 * 전체 ADC를 기준으로 현재/전날의 트래픽 발생 현황을 제공한다. "트래픽" 테이블에서 사용함.
	 * 
	 * @param accountIndex
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	public OBDtoAdcmonTrafficStatus getTotalAdcTrafficStatus(Integer accountIndex) throws OBException, Exception
	{
		OBDtoDashboardTrafficStatus retVal = dashboardAdc.getTotalAdcTrafficInfo(accountIndex);
		log.debug("{}", retVal);
		return new OBDtoAdcmonTrafficStatus().toAdcmonTrafficStatus(retVal);		
	}
	
	//
	/**
	 * TOP 5 Vritual Server IP에 대한 최근 25시간 발생 추이 데이터를 제공한다. " TOP 5 Virtual Server Connection Chart"에서 사용.
	 * 
	 * @param accountIndex
	 * @param fromTime
	 * @param toTime
	 * @return
	 * @throws OBException
	 */
	public ArrayList<OBDtoDashboardTop5Data> getVSConnectionGraphListTop5(Integer accountIndex, Date fromTime, Date toTime) throws OBException, Exception
	{
		log.debug("accountIndex:{}", new Object[]{accountIndex});
		
		ArrayList<OBDtoDashboardVSConnHistory> adcTop5VSConnectionFromSvc = dashboardAdc.getVSConnectionGraphListTop5(accountIndex, fromTime, toTime);
		log.debug("{}", adcTop5VSConnectionFromSvc);
		return new OBDtoDashboardTop5Data().toTop5Data(adcTop5VSConnectionFromSvc);
	}
		
	//
	/**
	 * ADC 장비를 기준으로 TOP5 트래픽 발생 장비 정보를 제공한다. "TOP 5 ADC 트래픽"에서 사용.
	 * 
	 * @param accountIndex
	 * @return
	 * @throws OBException
	 */
	
	public ArrayList<OBDtoAdcmonTraffic> getAdcTrafficListTop5(Integer accountIndex) throws OBException, Exception
	{
		ArrayList<OBDtoDashboardTraffic> iTemList = dashboardAdc.getAdcTrafficListTop5(accountIndex);
		ArrayList<OBDtoAdcmonTraffic> retVal = new  ArrayList<OBDtoAdcmonTraffic>();

		for(OBDtoDashboardTraffic item: iTemList)
		{
			retVal.add(new OBDtoAdcmonTraffic().toAdcmonTrafficContent(item));
		}
		return retVal;
	}
	
	//
	/**
	 * TOP 5 Vritual Server IP에 대한 최근 25시간 발생 추이 데이터를 제공한다. "Virtual Server IP"에서 사용.
	 * 
	 * @param accountIndex
	 * @return
	 * @throws OBException
	 */
	
	public ArrayList<OBDtoAdcmonTraffic> getVSTrafficListTop5(Integer accountIndex) throws OBException, Exception
	{
		log.debug("accountIndex:{}, startTime:{}, endTime:{}", new Object[]{accountIndex});
		ArrayList<OBDtoDashboardTraffic> iTemList = dashboardAdc.getVSTrafficListTop5(accountIndex);
		
		ArrayList<OBDtoAdcmonTraffic> retVal = new  ArrayList<OBDtoAdcmonTraffic>();
		for(OBDtoDashboardTraffic item: iTemList)
		{
			retVal.add(new OBDtoAdcmonTraffic().toAdcmonTrafficContent(item));
		}
		return retVal;
//		return new OBDtoAdcmonTraffic().toAdcmonTrafficContent2(iTemList);
	}	
}