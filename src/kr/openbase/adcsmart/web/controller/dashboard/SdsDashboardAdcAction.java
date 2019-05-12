package kr.openbase.adcsmart.web.controller.dashboard;
/*
 * 코딩 가이드. 
 * 	1. public 함수의 Exception은 무조건 OBException만 throw 한다.
 *  2. public 함수내에서는 try-catch를 추가하면 catch로 OBException, Exception을 받는다.
 *  3. try-catch로 Exception을 받았을 경우에는 OBException으로 throw 한다. 
 *    참고: public String retrieveAdcGroupToAdcsMap() throws OBException
 *  4. private 함수는 OBException, Exception 두종류를 throw한다. 
 *    참고: private void prepareAdcGroupMapAndGroupToAdcsMap(String searchKey)
 *  5. 내부 처리 도중 사용자에게 메세지를 알릴 필요가 있을 경우에는 다음과 같이 두단계로 진행한다. 
 *    5.1 isSuccessful = false;
 *    5.2 message = OBMessageWeb.LOGIN_ALIVE_TIMEOUT;
 *    5.3. 참고: String addAdc() ....
 *
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcGroup;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardAdcSummary;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardTraffic;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardVSSummary;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dashboard.SdsDashboardAdcFacade;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonFaultStatus;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonSlbChangeStatus;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonSummary;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonTraffic;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonTrafficStatus;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoDashboardTop5Data;
import kr.openbase.adcsmart.web.util.OBDateTimeWeb;

@Controller
@Scope(value = "prototype")
public class SdsDashboardAdcAction extends BaseAction 
{
	private transient Logger log = LoggerFactory.getLogger(SdsDashboardAdcAction.class);
	@Autowired 
	private SdsDashboardAdcFacade dashboardAdcFacade;
	
	@Value("#{settings['dashboard.refreshTime']}") 
	private Integer 							refreshTime;
	private String 								lastUpdatedTime;	
	
	private OBDtoDashboardAdcSummary 			adcSummarys;
	private OBDtoAdcmonSummary 					adcSummarysConversion;				//그룹 Connection, Throughtput 단위 환산
	private List<OBDtoDataObj> 					adcConnectionPopupGraph;
	private List<OBDtoDataObj> 					adcThroughputPopupGraph;
	private List<OBDtoDataObj>					vsConnectionPopupGraph;
	private List<OBDtoDataObj>					vsThroughputPopupGraph;
	private List<OBDtoAdcGroup>					adcGroupList;						//ADC 그룹상세 정보 
	private OBDtoAdcInfo						adcInfo;							//ADC 상세정보
	private List<OBDtoAdcInfo>					adcInfoList;						//ADC 상세정보 List
	private List<OBDtoADCObject> 				adcObjList;
	private OBDtoADCObject 						adcObj;								// 현재 선택한 adc object.
	private OBDtoDashboardVSSummary 			adcTotalVSSummary;					//전체 Virtual Server상황 개수
	private List<OBDtoDashboardTraffic> 		adcTop10VSTrafficList;				//Top 10 Virtual Server list	
	private List<OBDtoAdcmonTraffic> 			adcTop10VSTrafficListConversion;	//Top 10 Virtual Server list (단위변환 DTO)
	private OBDtoDashboardAdcSummary			currSelectedItem=null;				// adcSummarys에서 현재 선택된 목록..
	private OBDtoAdcmonSummary					currSelectedItemConversion=null;	// 그룹 Connection, Throughtput 단위 환산
	private List<OBDtoDashboardTraffic>			trafficObj;
	
	private OBDtoAdcmonFaultStatus 				dashFaultStatus;					//장애 모니터링 현황
	private List<OBDtoAdcmonTraffic> 			dashTop5AdcTrafficList;				//전체 ADC 트랙픽 TOP5 리스트
	private List<OBDtoDataObj> 					dashTotalAdcTrafficGraph;			//전체 ADC 트래픽 현황
	private OBDtoAdcmonTrafficStatus 			dashAdcTrafficStatus; 	
		
	private OBDtoAdcmonSlbChangeStatus 			dashSlbChangeStatus;				//ADC 설정 변경 현황
	private List<OBDtoDashboardTop5Data> 		dashTop5VSConnecionList;			//TOP 5 Vritual Server IP에 대한 최근 25시간 발생 추이 데이터를 제공한다. "Virtual Server IP"에서 사용.
	private List<OBDtoAdcmonTraffic>			dashTop5VSList;
	private Integer 							hour;
	private Integer 							accountIndex;
	private Date 								startTime;
	private Date 								endTime;
	private Date 								startDay;
	private Date 								endDay;

	private Integer 							curCategory=0; 						//0: 전체, 1: 그룹, 2: 개별 adc, 3:virtual server, 4:virtual service
	private Integer 							curIndex=0;
	private String 								curName="";
	private Integer 							curVendor=0;
	private String								popUpcontents = "";					//팝업종류-VSconnection, VSthroughput , ADCconnection, ADCthroughput
	private String								vsIndex="";
	private String								vsIp="";
	private String								vsPort="";
	private boolean								refreshes = false;
	
	/**
	 * 검색조건이 없을때 6일전부터 현재일 23시59분59초로 설정하여 사용한다.
	 */
	public SdsDashboardAdcAction() throws OBException
	{
		startTime = new Date();										
		endTime = new Date(startTime.getTime());
		adjustPeriod();
	}
	private void adjustPeriod() throws OBException
	{
		startTime = OBDateTimeWeb.initTimeOfDate(startTime, false);
		endTime = OBDateTimeWeb.initTimeOfDate(endTime, true);
	}
	
	public String loadPopupContent() throws Exception
	{
		try
		{	
			accountIndex = session.getAccountIndex();
			
			if(curCategory==1)//그룹
			{
				adcGroupList = dashboardAdcFacade.getAdcGroupList(curIndex);
				adcInfoList = dashboardAdcFacade.getAdcInfoListInGroup(curIndex, accountIndex);
			}
			else if(curCategory==2)//ADC 개별
			{
				adcInfo = dashboardAdcFacade.getAdcInfo(curIndex);
			}
			
			log.debug("{}", adcGroupList);
			log.debug("{}", adcInfoList);
			log.debug("{}", adcInfo);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		}

		return SUCCESS;
	}
	//ADC 별 connection/장애 정보
	public String loadAdcSdsHeader() throws Exception 
	{		
		try
		{
			log.debug("loadAdcSdsContain");
			accountIndex = session.getAccountIndex();
			
			adcSummarysConversion = dashboardAdcFacade.getAdcGroupSummaryConversion(session.getAccountIndex()); 
			
			if(adcObj==null)
			{
				adcObj = adcSummarysConversion.getObjectInfo();		// 리스트의 첫번째 object로 할당.
			}
			
			if(currSelectedItemConversion==null)
			{
				currSelectedItemConversion =  adcSummarysConversion;
			}
			
			adcTotalVSSummary = dashboardAdcFacade.getTotalVSSummary(accountIndex, adcObj);
			adcTop10VSTrafficListConversion = dashboardAdcFacade.getTop10VSTrafficConversion(accountIndex, adcObj);
		} 
		catch (Exception e) 
		{	
			e.printStackTrace();
			throw e;
		}

		return SUCCESS;
	}
	
	
	public String loadAdcSdsHeaderRefresh() throws Exception 
	{		
		try
		{			
			accountIndex = session.getAccountIndex();
			adcSummarysConversion = dashboardAdcFacade.getAdcGroupSummaryConversion(session.getAccountIndex()); 
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		}

		return SUCCESS;
	}	
	//전체 Virtual Server 현황 - refresh
	public String loadVSSummary() throws Exception
	{
		try
		{
			log.debug("accountIndex:{}", new Object[]{accountIndex});
			accountIndex = session.getAccountIndex();
			adcSummarysConversion = dashboardAdcFacade.getAdcGroupSummaryConversion(session.getAccountIndex()); 
			if(currSelectedItemConversion==null)
			{
				currSelectedItemConversion =  adcSummarysConversion;
			}
		
			OBDtoADCObject selectedObj = new OBDtoADCObject();
			selectedObj.setCategory(curCategory);
			selectedObj.setIndex(curIndex);
			selectedObj.setName(curName);
			selectedObj.setVendor(curVendor);
			selectedObj.setDesciption("");
			selectedObj.setStatus(0);		
			
			adcTotalVSSummary = dashboardAdcFacade.getTotalVSSummary(accountIndex, selectedObj);			
			adcTop10VSTrafficListConversion = dashboardAdcFacade.getTop10VSTrafficConversion(accountIndex, selectedObj);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}	
	// 장애 모니터링 현황(1주일) Chart & Grid Content
	public String loadFaultMonitoringChartContent() throws Exception 
	{
		return SUCCESS;
	}
	public String loadFaultMonitoringGridContent() throws Exception 
	{
		try 
		{
			accountIndex = session.getAccountIndex();
			startDay = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.getDateWithDayOffset(-6, null)));
			endDay = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.now()));

			dashFaultStatus = dashboardAdcFacade.getFaultMonitoring(accountIndex, startTime, endTime);					

			log.debug("{}", dashFaultStatus);			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;
		}		
		return SUCCESS;
	}
	// 전체 ADC 트래픽 현황 Chart & Grid Content
	public String loadAdcTrafficChartContent() throws Exception
	{	
		return SUCCESS;
	}
	public String loadAdcTrafficGridContent() throws Exception
	{
		try
		{
			accountIndex = session.getAccountIndex();
			dashAdcTrafficStatus = dashboardAdcFacade.getTotalAdcTrafficStatus(accountIndex);				//전체 ADC를 기준으로 현재/전날의 트래픽 발생 현황을 제공
			dashTop5AdcTrafficList = dashboardAdcFacade.getAdcTrafficListTop5(accountIndex);				//ADC 장비를 기준으로 TOP5 트래픽 발생 장비 정보를 제공
		
			log.debug("{}", dashAdcTrafficStatus);
			log.debug("{}", dashTop5AdcTrafficList);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		}
	
		return SUCCESS;
	}	
	// ADC 설정 변경 현황(1주일) Chart & Grid Content
	public String loadHistoryMonitoringChartContent() throws Exception 
	{
		return SUCCESS;
	}
	public String loadHistoryMonitoringGridContent() throws Exception 
	{
		try
		{
			accountIndex = session.getAccountIndex();
			startDay = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.getDateWithDayOffset(-6, null)));
			endDay = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.now()));
			if(null != accountIndex)
			{								
				dashSlbChangeStatus = dashboardAdcFacade.getSlbChangeMonitoring(accountIndex, startDay, endDay);
				
				log.debug("{}", dashSlbChangeStatus);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw e;			
		}
		
		return SUCCESS;
	}	
	// Top5 Virtual Server Connection Chart & Grid Content
	public String loadTop5VsMonitoringChartContent() throws Exception 
	{
		return SUCCESS;		
	}
	public String loadTop5VsMonitoringGridContent() throws Exception 
	{
		try
		{
			accountIndex = session.getAccountIndex();
			if(null != accountIndex)
			{				
				dashTop5VSList= dashboardAdcFacade.getVSTrafficListTop5(accountIndex);		//TOP 5 Virtaul List를 제공 - IP, PORT,CONNECTION, THROUGHTPUT
			}			
			log.debug("{}", dashTop5VSList);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;		
	}
	
	//전체 Chart Data Load
	public String loadAdcSdsTrafficInfo() throws Exception 
	{
		try
		{
			accountIndex = session.getAccountIndex();
			log.debug("loadAdcSdsContain");
			
			startTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.getDateWithHourOffset(-24, null)));
			endTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.now()));
			
			startDay = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.getDateWithDayOffset(-6, null)));
			endDay = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.now()));
			
			dashTop5VSConnecionList = dashboardAdcFacade.getVSConnectionGraphListTop5(accountIndex, startTime, endTime);	//TOP 5 Vritual Server IP에 대한 최근 25시간 발생 추이 데이터를 제공
			dashTotalAdcTrafficGraph  = dashboardAdcFacade.getTotalAdcTrafficGraph(accountIndex, startTime, endTime);	//전체 ADC를 기준으로 발생된 트래픽 추이를 제공
			dashFaultStatus = dashboardAdcFacade.getFaultMonitoring(accountIndex, startDay, endDay);
			dashSlbChangeStatus = dashboardAdcFacade.getSlbChangeMonitoring(accountIndex, startDay, endDay);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		}		
		return SUCCESS;
	}
	
	public String loadAdcPopupPage()throws Exception
	{
		return SUCCESS;
	}
	public String loadAdcPopupContents()throws Exception
	{

		return SUCCESS;
	}	

	public String loadAdcPopupInfo() throws Exception // ADC/Group Connection, Throughput 팝업 ChartData (runJson)
	{
		try
		{
			setSearchTimeInterval();
			accountIndex = session.getAccountIndex();
				
			OBDtoADCObject selectedObj = new OBDtoADCObject();			
			selectedObj = dashboardAdcFacade.getAdcObject(curCategory, curIndex, curName, curVendor, 0, "");
		
			String popUp = getPopUpcontents();		
			
			if(!refreshes)
			{
				startTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.getDateWithHourOffset(-24, null)));
				endTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.now()));
			}
			
//			endTime = OBDateTimeWeb.initTimeOfDate(endTime, true);
			
			if(popUp.equals("adcConnection"))
			{
				//Connection 팝업 ChartData
				adcConnectionPopupGraph = dashboardAdcFacade.getAdcConnectionGraph(accountIndex, selectedObj, startTime, endTime);
				log.debug("{}", adcConnectionPopupGraph);
			}
			else if(popUp.equals("adcThroughput"))
			{
				//Throughput 팝업 ChartData
				adcThroughputPopupGraph = dashboardAdcFacade.getAdcThroughputGraph(accountIndex, selectedObj, startTime, endTime);
				log.debug("{}", adcThroughputPopupGraph);
			}					
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		}	
		return SUCCESS;
	}
	public String loadVsPopupPage()throws Exception // ADC/VS Connection, Throughput 팝업 ChartData (runJson)
	{
		return SUCCESS;
	}
	public String loadVsPopupContents()throws Exception
	{
		return SUCCESS;
	}
	public String loadVsPopupInfo() throws Exception
	{
		try
		{	
			setSearchTimeInterval();
			accountIndex = session.getAccountIndex();
			OBDtoDashboardTraffic object = new OBDtoDashboardTraffic();
			
			object.setVendor(curVendor);
			// vsIndex 설정.
			object.setIndex(vsIndex);
			
			if(!refreshes)
			{
				startTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.getDateWithHourOffset(-24, null)));
				endTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.now()));
			}
			
//			endTime = OBDateTimeWeb.initTimeOfDate(endTime, true);		
			String popUp = getPopUpcontents();
			if(popUp.equals("vsConnection"))
			{
				// Connection 팝업 ChartData
				vsConnectionPopupGraph = dashboardAdcFacade.getVirtualServerConnectionGraph(object, startTime, endTime);
				log.debug("{}",startTime);
				log.debug("{}", vsConnectionPopupGraph);				
			}
			else if(popUp.equals("vsThroughput"))
			{
//				Throughput 팝업 ChartData
				vsThroughputPopupGraph = dashboardAdcFacade.getVirtualServerThroughputGraph(object, startTime, endTime);
				log.debug("{}", vsThroughputPopupGraph);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw e;
		}
		return SUCCESS;
	}
	private void setSearchTimeInterval()
	{
		if ( null != startTime && null != endTime)
		{

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			calendar.add(Calendar.HOUR_OF_DAY, 24);
			calendar.add(Calendar.MILLISECOND, -1);
			
			endTime =calendar.getTime();
			
			Date curTime = new Date();
			
			if(endTime.getTime() > curTime.getTime())
				
			{
				endTime = curTime;
			}			
		} 
		else
		{
			endTime = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endTime);
			if (null != hour) 
			{
				calendar.add(Calendar.HOUR_OF_DAY, -hour);
			} 
			else 
			{
				calendar.add(Calendar.HOUR_OF_DAY, -1);
			}
			startTime = calendar.getTime();			
		}
		log.debug("startTime: " + startTime.toString() + ", endTime: " + endTime.toString());
	}
	
	public String getVsIp()
	{
		return vsIp;
	}
	public void setVsIp(String vsIp)
	{
		this.vsIp = vsIp;
	}
	public String getVsPort()
	{
		return vsPort;
	}
	public void setVsPort(String vsPort)
	{
		this.vsPort = vsPort;
	}
	public OBDtoDashboardAdcSummary getCurrSelectedItem()
	{
		return currSelectedItem;
	}

	public void setCurrSelectedItem(OBDtoDashboardAdcSummary currSelectedItem)
	{
		this.currSelectedItem = currSelectedItem;
	}
	
	public String loadRefreshTime() throws Exception 
	{
		return SUCCESS;
	}
	
	public String loadAlertTime() throws Exception 
	{
		return SUCCESS;
	}
	
	public Integer getRefreshTime() 
	{
		return refreshTime;
	}

	public void setRefreshTime(Integer refreshTime) 
	{
		this.refreshTime = refreshTime;
	}

	public String getLastUpdatedTime() 
	{
		lastUpdatedTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(String lastUpdatedTime) 
	{
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public void setAdcSummarys(OBDtoDashboardAdcSummary adcSummarys)
	{
		this.adcSummarys = adcSummarys;
	}
	
	public OBDtoDashboardAdcSummary getAdcSummarys()
	{
		return adcSummarys;
	}

	public List<OBDtoAdcmonTraffic> getDashTop5VSList()
	{
		return dashTop5VSList;
	}

	public void setDashTop5VSList(List<OBDtoAdcmonTraffic> dashTop5VSList)
	{
		this.dashTop5VSList = dashTop5VSList;
	}

	public List<OBDtoADCObject> getAdcObjList()
	{
		return adcObjList;
	}

	public void setAdcObjList(List<OBDtoADCObject> adcObjList)
	{
		this.adcObjList = adcObjList;
	}

	public OBDtoADCObject getAdcObj()
	{
		return adcObj;
	}

	public void setAdcObj(OBDtoADCObject adcObj)
	{
		this.adcObj = adcObj;
	}

	public OBDtoDashboardVSSummary getAdcTotalVSSummary()
	{
		return adcTotalVSSummary;
	}

	public void setAdcTotalVSSummary(OBDtoDashboardVSSummary adcTotalVSSummary)
	{
		this.adcTotalVSSummary = adcTotalVSSummary;
	}

	public List<OBDtoDashboardTraffic> getAdcTop10VSTrafficList()
	{
		return adcTop10VSTrafficList;
	}

	public void setAdcTop10VSTrafficList(List<OBDtoDashboardTraffic> adcTop10VSTrafficList)
	{
		this.adcTop10VSTrafficList = adcTop10VSTrafficList;
	}

	public OBDtoAdcmonFaultStatus getDashFaultStatus()
	{
		return dashFaultStatus;
	}

	public void setDashFaultStatus(OBDtoAdcmonFaultStatus dashFaultStatus)
	{
		this.dashFaultStatus = dashFaultStatus;
	}


	public List<OBDtoAdcmonTraffic> getDashTop5AdcTrafficList()
	{
		return dashTop5AdcTrafficList;
	}

	public void setDashTop5AdcTrafficList(List<OBDtoAdcmonTraffic> dashTop5AdcTrafficList)
	{
		this.dashTop5AdcTrafficList = dashTop5AdcTrafficList;
	}

	public List<OBDtoDataObj> getDashTotalAdcTrafficGraph()
	{
		return dashTotalAdcTrafficGraph;
	}

	public void setDashTotalAdcTrafficGraph(List<OBDtoDataObj> dashTotalAdcTrafficGraph)
	{
		this.dashTotalAdcTrafficGraph = dashTotalAdcTrafficGraph;
	}

	public OBDtoAdcmonTrafficStatus getDashAdcTrafficStatus()
	{
		return dashAdcTrafficStatus;
	}

	public void setDashAdcTrafficStatus(OBDtoAdcmonTrafficStatus dashAdcTrafficStatus)
	{
		this.dashAdcTrafficStatus = dashAdcTrafficStatus;
	}

	public OBDtoAdcmonSlbChangeStatus getDashSlbChangeStatus()
	{
		return dashSlbChangeStatus;
	}

	public void setDashSlbChangeStatus(OBDtoAdcmonSlbChangeStatus dashSlbChangeStatus)
	{
		this.dashSlbChangeStatus = dashSlbChangeStatus;
	}
	
	public List<OBDtoDashboardTop5Data> getDashTop5VSConnecionList()
	{
		return dashTop5VSConnecionList;
	}

	public void setDashTop5VSConnecionList(List<OBDtoDashboardTop5Data> dashTop5VSConnecionList)
	{
		this.dashTop5VSConnecionList = dashTop5VSConnecionList;
	}

	public Integer getAccountIndex()
	{
		return accountIndex;
	}

	public void setAccountIndex(Integer accountIndex)
	{
		this.accountIndex = accountIndex;
	}

	public Date getStartTime()
	{
		return startTime;
	}

	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	
	public Date getEndDay()
	{
		return endDay;
	}
	
	public void setEndDay(Date endDay)
	{
		this.endDay = endDay;
	}

	public Date getStartDay()
	{
		return startDay;
	}
	
	public void setStartDay(Date startDay)
	{
		this.startDay = startDay;
	}
	
	public Integer getCurCategory() 
	{
		return curCategory;
	}

	public void setCurCategory(Integer curCategory) 
	{
		this.curCategory = curCategory;
	}

	public Integer getCurIndex() 
	{
		return curIndex;
	}

	public void setCurIndex(Integer curIndex)
	{
		this.curIndex = curIndex;
	}

	public String getCurName() 
	{
		return curName;
	}

	public void setCurName(String curName) 
	{
		this.curName = curName;
	}

	public Integer getCurVendor() 
	{
		return curVendor;
	}

	public void setCurVendor(Integer curVendor) 
	{
		this.curVendor = curVendor;
	}		
	
	public List<OBDtoAdcmonTraffic> getAdcTop10VSTrafficListConversion() 
	{
		return adcTop10VSTrafficListConversion;
	}

	public void setAdcTop10VSTrafficListConversion(List<OBDtoAdcmonTraffic> adcTop10VSTrafficListConversion) 
	{
		this.adcTop10VSTrafficListConversion = adcTop10VSTrafficListConversion;
	}

	public OBDtoAdcmonSummary getAdcSummarysConversion() 
	{
		return adcSummarysConversion;
	}

	public void setAdcSummarysConversion(OBDtoAdcmonSummary adcSummarysConversion) 
	{
		this.adcSummarysConversion = adcSummarysConversion;
	}
	
	public List<OBDtoDataObj> getAdcConnectionPopupGraph()
	{
		return adcConnectionPopupGraph;
	}

	public void setAdcConnectionPopupGraph(List<OBDtoDataObj> adcConnectionPopupGraph)
	{
		this.adcConnectionPopupGraph = adcConnectionPopupGraph;
	}

	public List<OBDtoDataObj> getAdcThroughputPopupGraph()
	{
		return adcThroughputPopupGraph;
	}

	public void setAdcThroughputPopupGraph(List<OBDtoDataObj> adcThroughputPopupGraph)
	{
		this.adcThroughputPopupGraph = adcThroughputPopupGraph;
	}	

	public List<OBDtoDataObj> getVsConnectionPopupGraph() {
		return vsConnectionPopupGraph;
	}

	public void setVsConnectionPopupGraph(List<OBDtoDataObj> vsConnectionPopupGraph) {
		this.vsConnectionPopupGraph = vsConnectionPopupGraph;
	}


	public List<OBDtoDataObj> getVsThroughputPopupGraph() {
		return vsThroughputPopupGraph;
	}


	public void setVsThroughputPopupGraph(List<OBDtoDataObj> vsThroughputPopupGraph) {
		this.vsThroughputPopupGraph = vsThroughputPopupGraph;
	}	
	
	public List<OBDtoDashboardTraffic> getTrafficObj() {
		return trafficObj;
	}
	
	public List<OBDtoAdcGroup> getAdcGroupList()
	{
		return adcGroupList;
	}
	
	public void setAdcGroupList(List<OBDtoAdcGroup> adcGroupList)
	{
		this.adcGroupList = adcGroupList;
	}
	
	public OBDtoAdcInfo getAdcInfo()
	{
		return adcInfo;
	}
	
	public void setAdcInfo(OBDtoAdcInfo adcInfo)
	{
		this.adcInfo = adcInfo;
	}
	
	public List<OBDtoAdcInfo> getAdcInfoList()
	{
		return adcInfoList;
	}
	
	public void setAdcInfoList(List<OBDtoAdcInfo> adcInfoList)
	{
		this.adcInfoList = adcInfoList;
	}
	
	public void setTrafficObj(List<OBDtoDashboardTraffic> trafficObj) {
		this.trafficObj = trafficObj;
	}
	public String getPopUpcontents()
	{
		return popUpcontents;
	}

	public void setPopUpcontents(String popUpcontents)
	{
		this.popUpcontents = popUpcontents;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}
	public boolean isRefreshes()
	{
		return refreshes;
	}
	public void setRefreshes(boolean refreshes)
	{
		this.refreshes = refreshes;
	}
	@Override
	public String toString()
	{
		return "SdsDashboardAdcAction [dashboardAdcFacade="
				+ dashboardAdcFacade + ", refreshTime=" + refreshTime
				+ ", lastUpdatedTime=" + lastUpdatedTime + ", adcSummarys="
				+ adcSummarys + ", adcSummarysConversion="
				+ adcSummarysConversion + ", adcConnectionPopupGraph="
				+ adcConnectionPopupGraph + ", adcThroughputPopupGraph="
				+ adcThroughputPopupGraph + ", vsConnectionPopupGraph="
				+ vsConnectionPopupGraph + ", vsThroughputPopupGraph="
				+ vsThroughputPopupGraph + ", adcGroupList=" + adcGroupList
				+ ", adcInfo=" + adcInfo + ", adcInfoList=" + adcInfoList
				+ ", adcObjList=" + adcObjList + ", adcObj=" + adcObj
				+ ", adcTotalVSSummary=" + adcTotalVSSummary
				+ ", adcTop10VSTrafficList=" + adcTop10VSTrafficList
				+ ", adcTop10VSTrafficListConversion="
				+ adcTop10VSTrafficListConversion + ", currSelectedItem="
				+ currSelectedItem + ", currSelectedItemConversion="
				+ currSelectedItemConversion + ", trafficObj=" + trafficObj
				+ ", dashFaultStatus=" + dashFaultStatus
				+ ", dashTop5AdcTrafficList=" + dashTop5AdcTrafficList
				+ ", dashTotalAdcTrafficGraph=" + dashTotalAdcTrafficGraph
				+ ", dashAdcTrafficStatus=" + dashAdcTrafficStatus
				+ ", dashSlbChangeStatus=" + dashSlbChangeStatus
				+ ", dashTop5VSConnecionList=" + dashTop5VSConnecionList
				+ ", dashTop5VSList=" + dashTop5VSList + ", hour=" + hour
				+ ", accountIndex=" + accountIndex + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", startDay=" + startDay
				+ ", endDay=" + endDay + ", curCategory=" + curCategory
				+ ", curIndex=" + curIndex + ", curName=" + curName
				+ ", curVendor=" + curVendor + ", popUpcontents="
				+ popUpcontents + ", vsIndex=" + vsIndex + ", refreshes="
				+ refreshes + "]";
	}
}