package kr.openbase.adcsmart.web.controller.dashboard;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dashboard.dto.OBDtoDashboardInfo;
import kr.openbase.adcsmart.service.dashboard.dto.OBDtoWidgetInfo;
import kr.openbase.adcsmart.service.dashboard.dto.OBDtoWidgetItemInfo;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardStatusNotification;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoGroupHistory;
import kr.openbase.adcsmart.service.dto.fault.OBDtoAdcCurrentSession;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCpuHistory;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultHWStatus;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dashboard.DashboardFacade;
import kr.openbase.adcsmart.web.facade.dto.AdcConnectionInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcCpuInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcMemoryInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcThroughputInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardAdcSummaryDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardStatusSummaryDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardVsSummaryDto;
import kr.openbase.adcsmart.web.facade.dto.VsStatusDataDto;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonFaultStatus;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonSlbChangeStatus;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonTraffic;
import kr.openbase.adcsmart.web.json.WidgetJsonAdapter;
import kr.openbase.adcsmart.web.util.NumberUtil;
import kr.openbase.adcsmart.web.util.OBDateTimeWeb;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@Controller
@Scope(value = "prototype")
public class DashBoardAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(DashBoardAction.class);
	private Integer widgetOrder;
	private ArrayList<OBDtoDashboardInfo> dashboardInfoList;			// 초기 페이지 load 시 DashboardList 데이터
	private ArrayList<OBDtoDashboardInfo> dashboardLastList;			// 초기 페이지 load 시 최근이용한 DashboardList 데이터
	private ArrayList<OBDtoWidgetItemInfo> widgetItemList;				// 초기 페이지 load 시 WidgetItemList 데이터
	private OBDtoWidgetItemInfo widgetItem;
	private OBDtoDashboardInfo dashboardInfo;							// Dashboard load 시 DashboardInfo 데이터
	private ArrayList<OBDtoWidgetInfo> widgetList;						// Widget List Dto
	private OBDtoWidgetInfo widgetInfo;									// Widget 추가시 사용하는 Widget Info Dto
	private String dashboardIndex;										// Dash board 정보 조회를 위한 DashboardIndex
	private String dashboardName;
	private String widgetListString;
	private OBDtoADCObject widgetTarget;	
	private ArrayList<OBDtoADCObject> widgetTargetList;
	private Integer hour;
	private Date startTime;
	private Date endTime;
	private OBDtoAdcInfo adcInfo;
	private OBDtoOrdering orderOption;
	
	private AdcMemoryInfoDto adcMemoryInfo;					            // Memory Chart Data
	private AdcCpuInfoDto adcCpuInfo;						            // CPU Chart Data
	private List<VsStatusDataDto> vsStatusInfo;							// VsStatusInfo Chart Data
	private List<OBDtoDataObj> allAdcTrafficInfo;						// AllAdcTrafficInfo Chart Data
	private AdcConnectionInfoDto adcConnectionInfo;						// ADC ConcurrentSession History Chart Data (삭제대기)
	private AdcThroughputInfoDto adcThroughputInfo;						// ADC Throughtput History Chart Data
	private ArrayList<OBDtoFaultBpsConnInfo> vsConcurrentInfo;			// Vs ConcurrentSession History Chart Data	
	private ArrayList<OBDtoFaultBpsConnInfo> vsThroughputInfo;			// Vs Throughtput History Chart Data
	private OBDtoAdcCurrentSession sessionHistory;						// ADC ConcurrentSession History Chart Data (FLB포함)
	private ArrayList<OBDtoFaultBpsConnInfo> GroupBpsConnHistoryInfo;	// FLB Group History Chart Data
	
	private List<OBDtoAdcmonTraffic> top10VSTrafficList;				// Top10 Virtual Server list
	private Integer	accountIndex;
	private List<OBDtoAdcmonTraffic> top10AdcTrafficList;				// 전체 ADC 트랙픽 TOP5 리스트
	private List<SdsDashboardAdcSummaryDto> adcSummaryList;				// ADC 요약 리스트
	private List<SdsDashboardVsSummaryDto> vsSummaryList;				// VS 요약 리스트
	private Integer faultMaxDays;										// 장애 모니터링 통계 일자
	private SdsDashboardStatusSummaryDto statusSummary;					// ADC요약, 장애 모니터링 통계, Virtual Server 통계 데이터 
	private OBDtoAdcmonFaultStatus dashFaultStatus;						// 장애 모니터링 최근 장애 내역
	private OBDtoAdcmonSlbChangeStatus dashSlbChangeStatus;				// ADC 설정 변경 현황	
	private ArrayList<OBDtoDataObj> ResponseTimeInfo;		            // 응답시간 추이
	private OBDtoFaultHWStatus faultHWStatus;							// ADC 장비 모니터링 위젯
	private OBDtoFaultCpuHistory cpuSpConns;							// CPU SP별 사용률, connection
	private List<OBDtoDashboardStatusNotification> notifcationList;     // ADC, VS, Member 상태 정보 List
	
	private ArrayList<OBDtoGroupHistory> serviceGroupConcurrentInfo;    // 서비스 그룹 ConcurrentSession History Chart Data
	private ArrayList<OBDtoGroupHistory> adcGroupMemoryInfo;            // ADC 그룹 Memory Chart Data
	private ArrayList<OBDtoGroupHistory> adcGroupCpuInfo;               // ADC 그룹 CPU Chart Data
	private ArrayList<OBDtoGroupHistory> serviceGroupResponseTimeInfo;  // 서비스 그룹 응답시간 History Chart Data
	private ArrayList<OBDtoGroupHistory> serviceGroupThroughputInfo;    // 서비스 그룹 Throughtput History Chart Data
	private Integer monitoringPeriod;
	private Integer intervalMonitor;
	
	@Autowired
	private DashboardFacade dashboardFacade;
	
	public DashBoardAction()
	{
		widgetList = new ArrayList<OBDtoWidgetInfo>();	
		
		OBDtoSystemEnvAdditional env = null;
		int interval = 0;
        try
        {
            env = new OBEnvManagementImpl().getAdditionalConfig();
            interval = env.getIntervalAdcConfSync();
        }
        catch(OBException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		monitoringPeriod = (int) (interval * 2.5);
	}
	public String loadLastDashboardInfo() throws OBException
	{
		try
		{
			dashboardLastList = dashboardFacade.getDashboardInfoLastList();
			intervalMonitor = monitoringPeriod;
			log.debug("dashboardLastList : {}", dashboardLastList);
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage()); 
		}
		return SUCCESS;
	}	
	
	public String loadHeader() throws OBException 
	{
		try
		{
			dashboardInfoList = dashboardFacade.getDashboardInfoList();
			widgetItemList = dashboardFacade.getWidgetItemList();
			dashboardLastList = dashboardFacade.getDashboardInfoLastList();
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage()); 
		}
		return SUCCESS;
	}
	public String loadDashboardContent() throws OBException 
	{
		return SUCCESS;
	}
	
	public String loadDashboardInfo() throws OBException
	{
		try
		{
			dashboardInfo = dashboardFacade.getDashboardInfo(dashboardIndex);
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage()); 
		}
		return SUCCESS;
	}
	
	public String loadWidgetTargetInfo() throws OBException
	{
		try
		{
			widgetTargetList = dashboardFacade.getInvolvedObjectList(widgetTarget, session.getSessionDto());
			log.debug("widgetTargetList : {}", widgetTargetList);
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage()); 
		}
		return SUCCESS;
	}
	public String loadWidgetTargetInfoAlteon() throws OBException
	{
		try
		{
			widgetTargetList = dashboardFacade.getInvolvedObjectListAlteon(widgetTarget, session.getSessionDto());
			log.debug("widgetTargetList : {}", widgetTargetList);
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage()); 
		}
		return SUCCESS;
	}
	
	public String loadWidgetTargetFlbGroupInfo() throws OBException
	{
		try
		{
			widgetTargetList = dashboardFacade.getFlbGroupList(widgetTarget, session.getSessionDto());
			log.debug("widgetTargetList : {}", widgetTargetList);
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage()); 
		}
		return SUCCESS;
	}
	
	public String saveDashboardInfo() throws OBException
	{
		try
		{
			OBDtoDashboardInfo dashBoardInfoObj = new OBDtoDashboardInfo();
			dashBoardInfoObj.setIndexKey(dashboardIndex);
			dashBoardInfoObj.setName(dashboardName);
			dashBoardInfoObj.setWidgetList(convertWidgetToJson(widgetListString));
			
			if(dashboardIndex.equals("0"))
			{
				dashboardFacade.addDashboardInfo(dashBoardInfoObj, session.getSessionDto());
			}
			else
			{
				dashboardFacade.setDashboardInfo(dashboardIndex, dashBoardInfoObj, session.getSessionDto());
			}			
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage()); 
		}		
		return SUCCESS;
	}
	
	public String deleteDashboardInfo() throws OBException
	{
		try
		{			
			dashboardFacade.delDashboardInfo(dashboardIndex, session.getSessionDto());					
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage()); 
		}		
		return SUCCESS;
	}
	
	public String modifyWidgetInfo() throws OBException
	{
		try
		{	
			widgetItemList = dashboardFacade.getWidgetItemList();
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage()); 
		}		
		return SUCCESS;
	}
	
	public String loadDashboardListInfo() throws OBException
	{
		try
		{	
			dashboardInfoList = dashboardFacade.getDashboardInfoList();
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage()); 
		}		
		return SUCCESS;
	}
	/*
	 * Widget Action
	 * 1. 장애 모니터링 통계
	 * 2. 장애 모니터링 현황 Chart
	 * 3. 최근장애내역
	 * 4. ADC 설정 변경 현황(1주일) Chart
	 * 5. 최근변경내역
	 * 6. ADC 상태 통계 화면
	 * 7. VS 상태 통계 화면
	 * 8. ADC Concurrent session 추이 Chart
	 * 9. ADC Throughtput 추이 Chart
	 * 10. CPU Chart
	 * 11. Memory Chart
	 * 12. AllAdcTraffic Chart
	 * 13. VS Status Chart
	 * 14. ADC 요약 List
	 * 15. VS Summary List
	 * 16. TOP 10 VS List
	 * 17. TOP 5 ADC List
	 * 18. ADC 장비 모니터링
	 * 19. 응답시간 추이
	 * 20. Concurrent session 추이 Chart (Detail)
	 * 21. Concurrent session 추이 Chart (FLB)
	 * 22.23. CPU SP별 사용률, Connection 정보
	 * 24. ADC 상태 알리미
	 * 25. VS 상태 알리미
	 * 26. Member 상태 알리미
	 */
	
	// 1. 장애 모니터링 통계
	public String loadFaultMonitoring() throws OBException
	{
		try 
		{
			Integer accountIndex = session.getAccountIndex();
//			faultMaxDays = 7;
//			if (null != accountIndex && null != faultMaxDays)
			if (null != accountIndex)
			{
				statusSummary = dashboardFacade.getStatusSummary(accountIndex, widgetInfo.getMoreInfoIndex());
			}
			log.debug("{}", statusSummary);
		} 
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());			
		}
		return SUCCESS;
	}
	
	// 2.장애 모니터링 현황(1주일) Chart & 3. 장애 모니터링 현황 최근장애내역
	public String loadFaultMonitoringList() throws OBException 
	{
		try 
		{
			accountIndex = session.getAccountIndex();
			startTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.getDateWithDayOffset(-6, null)));
			endTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.now()));

			dashFaultStatus = dashboardFacade.getFaultMonitoring(accountIndex, startTime, endTime);					

			log.debug("{}", dashFaultStatus);			
		}
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());			
		}	
		return SUCCESS;
	}
	
	// 4. ADC 설정 변경 현황(1주일) Chart & 5. ADC 설정 변경 현황(1주일) 최근변경 내역
	public String loadAdcMonitoringList() throws OBException 
	{
		try
		{
			accountIndex = session.getAccountIndex();
			startTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.getDateWithDayOffset(-6, null)));
			endTime = OBDateTimeWeb.toDate(OBDateTimeWeb.toTimestamp(OBDateTimeWeb.now()));
			if(null != accountIndex)
			{								
				dashSlbChangeStatus = dashboardFacade.getSlbChangeMonitoring(accountIndex, startTime, endTime);
				
				log.debug("{}", dashSlbChangeStatus);
			}
		}
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());			
		}		
		return SUCCESS;
	} 
	
	// 6. ADC 상태 통계
	public String loadAdcMonitoring() throws OBException
	{
		try 
		{
			Integer accountIndex = session.getAccountIndex();
			if (null != accountIndex)
			{
				statusSummary = dashboardFacade.getStatusSummary(accountIndex, 0);
			}
			log.debug("{}", statusSummary);
		} 
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());			
		}
		return SUCCESS;
	}
	
	// 7. VS Servcer 모니터링 통계
	public String loadVsMonitoring() throws OBException
	{
		try 
		{
			Integer accountIndex = session.getAccountIndex();
			if (null != accountIndex)
			{
				statusSummary = dashboardFacade.getStatusSummary(accountIndex, 0);
			}
			log.debug("{}", statusSummary);
		} 
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());			
		}
		return SUCCESS;
	}
	// 8. ADC Concurrent session 추이 Chart
	public String loadAdcConcurrentSessionInfo() throws OBException 
	{
		try 
		{
			setSearchTimeInterval();
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setFromTime(startTime);
			searchOption.setToTime(endTime);
			if (null != widgetTarget && null != widgetTarget.getIndex()) 
			{
				if (widgetTarget.getCategory() < OBDtoADCObject.CATEGORY_VS)
				{
					adcConnectionInfo = dashboardFacade.getConnectionInfo(widgetTarget, startTime, endTime);
				}
				else if (widgetTarget.getCategory() == 7)
				{
				    serviceGroupConcurrentInfo = dashboardFacade.getVsConcurrentInfoSeviceGroup(widgetTarget, searchOption);
				}
				else
				{
					vsConcurrentInfo = dashboardFacade.getVsConcurrentInfo(widgetTarget, searchOption);
				}
			}
			log.debug("{}", adcConnectionInfo);		
		} 
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());			
		}
		return SUCCESS;
	}
	// 9. ADC Throughput 추이 Chart
	public String loadAdcThroughputInfo() throws OBException 
	{
		try 
		{
			setSearchTimeInterval();
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setFromTime(startTime);
			searchOption.setToTime(endTime);
			if (null != widgetTarget && null != widgetTarget.getIndex()) 
			{
				if (widgetTarget.getCategory() < OBDtoADCObject.CATEGORY_VS)
				{
					adcThroughputInfo = dashboardFacade.getThroughputInfo(widgetTarget, startTime, endTime);
				}
				else if (widgetTarget.getCategory() == 7)
				{
				    serviceGroupThroughputInfo = dashboardFacade.getVsThroughputInfoGroup(widgetTarget, searchOption);
				}
				else
				{
					vsThroughputInfo = dashboardFacade.getVsThroughputInfo(widgetTarget, searchOption);
				}
			}		
			log.debug("{}", adcThroughputInfo);
		} 
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());			
		}
		return SUCCESS;
	}
	
	// 10. CPU Chart Data
	public String loadCpuHistoryInfo() throws OBException
	{
		try
		{
			setSearchTimeInterval();
	        if(widgetTarget.getCategory() == OBDtoADCObject.CATEGORY_GROUP)
            {
                adcGroupCpuInfo = dashboardFacade.getAdcCpuGroupInfo(widgetTarget, startTime, endTime);
            }
	        else if(null != widgetTarget && null != widgetTarget.getIndex()) 
			{			
				adcCpuInfo = dashboardFacade.getAdcCpuInfo(widgetTarget, startTime, endTime);
			}

		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}
		
	// 11. Memory Chart Data
	public String loadMemoryHistoryInfo() throws OBException
	{
		try 
		{
			setSearchTimeInterval();
			if(widgetTarget.getCategory() == OBDtoADCObject.CATEGORY_GROUP)
            {
                adcGroupMemoryInfo = dashboardFacade.getAdcMemoryGroupInfo(widgetTarget, startTime, endTime);
            }
            else if(null != widgetTarget && null != widgetTarget.getIndex()) 
			{			
				adcMemoryInfo = dashboardFacade.getAdcMemoryInfo(widgetTarget, startTime, endTime);
			}
			
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}
	// 12. AllAdcTraffic Chart Data
	public String loadAllAdcTrafficInfo() throws OBException
	{
		try 
		{
			accountIndex = session.getAccountIndex();
			setSearchTimeInterval();
			allAdcTrafficInfo = dashboardFacade.getAllAdcTrafficInfo(accountIndex, startTime, endTime);
			log.debug("{}", allAdcTrafficInfo);
		} 
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}
	
	// 13. VS Status Chart Data
	public String loadVsStatusDataInfo() throws OBException
	{
		try 
		{
			setSearchTimeInterval();
			if (null != widgetTarget && null != widgetTarget.getIndex()) 
			{
				vsStatusInfo = dashboardFacade.getVsStatusInfo(widgetTarget, startTime, endTime);
			}
			log.debug("{}", vsStatusInfo);
		} 
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}
	
	// 14. ADC Summary List
	public String loadAdcSummaryList() throws OBException
	{
		try
		{
			Integer accountIndex = session.getAccountIndex();
			if (null != accountIndex &&
				null != widgetTarget && null != widgetTarget.getIndex()) 
			{				
				adcSummaryList = dashboardFacade.getAdcSummaryList(accountIndex, widgetTarget, widgetInfo.getMoreInfoIndex(), 0, 8, 2);
			}
			
			log.debug("{}", adcSummaryList);
			
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			 throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}		
		return SUCCESS;
	}	 
	
	// 15. VS Summary List	
	public String loadVsSummaryList() throws OBException
	{
		try
		{
			Integer accountIndex = session.getAccountIndex();
			if (null != accountIndex &&
				null != widgetTarget && null != widgetTarget.getIndex()) 
			{				
				vsSummaryList = dashboardFacade.getVsSummaryList(accountIndex, widgetTarget, widgetInfo.getMoreInfoIndex(), 0, 2, 2);
			}
			log.debug("{}", vsSummaryList);
			
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			 throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}
	
	// 16. Top10 Virtual Server List
	public String loadTop10VSInfo() throws OBException
	{
		try
		{
			accountIndex = session.getAccountIndex();
			OBDtoOrdering orderOption = new OBDtoOrdering();
			orderOption.setOrderType(widgetInfo.getMoreInfoIndex());
			
			top10VSTrafficList = dashboardFacade.getTop10VSTrafficList(accountIndex, widgetTarget, orderOption);
			
			log.debug("adcTop10VSTrafficList: {}", top10VSTrafficList);
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			 throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}
	
	// 17. Top5 ADC List
	public String loadTop5AdcInfo() throws OBException
	{
		try
		{
			accountIndex = session.getAccountIndex();
			top10AdcTrafficList = dashboardFacade.getTop10AdcList(accountIndex);
			log.debug("top5AdcTrafficList: {}", top10AdcTrafficList);
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			 throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}		
		return SUCCESS;
	}
	
	//18. ADC 장비 모니터링
	// Select ADC Info Get
	public String loadSelectedAdcInfo() throws OBException
	{
		try
		{
			widgetTarget = dashboardFacade.getAdcInfo(widgetTarget);				
			log.debug("{}", widgetTarget);			
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			 throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}		
	// 장비 모니터링 Info Get
	public String loadApplianceMapContent() throws OBException
	{
		try
		{
		    adcInfo = dashboardFacade.getAdcInfoExt(widgetTarget.getIndex());
			faultHWStatus = dashboardFacade.getHWStatus(widgetTarget);				
			log.debug("{}", faultHWStatus);								
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}	
		return SUCCESS;		
	}
	
	//19. 응답시간 추이	
	public String loadResponseTimeHistoryInfo() throws OBException
	{
		try
		{
			if (null != widgetTarget && null != widgetTarget.getIndex())
			{
				setSearchTimeInterval();
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(startTime);
				searchOption.setToTime(endTime);
				if (widgetTarget.getCategory() == 7)
				{
				    serviceGroupResponseTimeInfo = dashboardFacade.getResponseTimeHistoryGroup(widgetTarget, searchOption);
				}
				else
				{
				    ResponseTimeInfo = dashboardFacade.getResponseTimeHistory(widgetTarget, searchOption);
				}
				
				log.debug("{}", ResponseTimeInfo);
			}
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			 throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}
	// 20. ADC Concurrent session 추이 Chart (Detail)
	public String loadConcurrentSessionDetailInfo() throws OBException 
	{
		try 
		{
			setSearchTimeInterval();
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setFromTime(startTime);
			searchOption.setToTime(endTime);
			if (null != widgetTarget && null != widgetTarget.getIndex()) 
			{					
				sessionHistory = dashboardFacade.getAdcConcurrentInfo(widgetTarget, searchOption);				
			}
			log.debug("{}", adcConnectionInfo);		
		} 
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());			
		}
		return SUCCESS;
	}
	// 21. ADC Concurrent session 추이 Chart (FLB)
	public String loadConcurrentSessionflbInfo() throws OBException 
	{
		try 
		{
			setSearchTimeInterval();
			OBDtoSearch searchOption = new OBDtoSearch();
			searchOption.setFromTime(startTime);
			searchOption.setToTime(endTime);
			if (null != widgetTarget && null != widgetTarget.getStrIndex()) 
			{					
				GroupBpsConnHistoryInfo = dashboardFacade.getFLBConcurrentSessionInfo(widgetTarget.getStrIndex(), searchOption);				
			}
			log.debug("{}", adcConnectionInfo);		
		} 
		catch (OBException e)
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());			
		}
		return SUCCESS;
	}
	
	// 22. 23. CPU SP별 사용률, Connection 정보
	public String loadCpuSpConnectionInfo() throws OBException
	{
		try
		{
		    setSearchTimeInterval();
            OBDtoSearch searchOption = new OBDtoSearch();
            searchOption.setFromTime(startTime);
            searchOption.setToTime(endTime);
			if (null != widgetTarget && null != widgetTarget.getStrIndex()) 
			{				
				cpuSpConns = dashboardFacade.getCpuSpConnectionInfo(widgetTarget, searchOption);
			
				if(null != cpuSpConns)
				{
    				widgetTarget = dashboardFacade.getAdcInfo(widgetTarget);
    				//TODO 맥스값 수정해야 함
    				cpuSpConns.setSpSessionMax(widgetTarget.getStatus().longValue() );
    				cpuSpConns.setSpSessionMaxUnit(NumberUtil.toStringWithUnit(widgetTarget.getStatus().longValue(), ""));
    				log.debug("{}", cpuSpConns);
				}
			}			
		}
		catch(OBException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}	
		return SUCCESS;		
	}
	
	// 24. ADC 상태 알리미
    public String loadAdcStatusNotificationInfo() throws OBException
    {
        try
        {
            if (null != widgetTarget && null != widgetTarget.getStrIndex()) 
            {               
                notifcationList = dashboardFacade.getAdcStatusNotification(widgetTarget);                
            }           
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }   
        return SUCCESS;     
    }
    
    // 25. VS 상태 알리미
    public String loadVsStatusNotificationInfo() throws OBException
    {
        try
        {
            if (null != widgetTarget && null != widgetTarget.getStrIndex()) 
            {               
                notifcationList = dashboardFacade.getVsStatusNotification(widgetTarget);                
            }           
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }   
        return SUCCESS;     
    }
    
    // 26. Member 상태 알리미
    public String loadMemberStatusNotificationInfo() throws OBException
    {
        try
        {
            if (null != widgetTarget && null != widgetTarget.getStrIndex()) 
            {               
                notifcationList = dashboardFacade.getMemberStatusNotification(widgetTarget);                
            }           
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }   
        return SUCCESS;     
    }
	
	
	private ArrayList<OBDtoWidgetInfo> convertWidgetToJson(String elementString) 
	{
		if (StringUtils.isEmpty(elementString))
			return null;
		
		try
		{
		widgetList.clear();
		Gson gson = new GsonBuilder().registerTypeAdapter(OBDtoWidgetInfo.class, new WidgetJsonAdapter()).create();
		JsonParser parser = new JsonParser();
		JsonArray jarray = parser.parse(elementString).getAsJsonArray();
		for (JsonElement e : jarray)
		{
			OBDtoWidgetInfo wInfo = gson.fromJson(e, OBDtoWidgetInfo.class);
			widgetList.add(wInfo);
		}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return widgetList;
	}
	
	private void setSearchTimeInterval()
	{
		endTime = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(endTime);
		if (null != hour) {
			calendar.add(Calendar.HOUR_OF_DAY, -hour);
		} else {
			calendar.add(Calendar.HOUR_OF_DAY, -12);
		}
		startTime = calendar.getTime();
		log.debug("startTime: " + startTime.toString() + "endTime: " + endTime.toString());
	}
	
	public ArrayList<OBDtoDashboardInfo> getDashboardLastList()
	{
		return dashboardLastList;
	}
	
	public void setDashboardLastList(ArrayList<OBDtoDashboardInfo> dashboardLastList)
	{
		this.dashboardLastList = dashboardLastList;
	}
	
	public String loadWidget1() throws OBException
	{
		return SUCCESS;
	}
	
	public String loadAllChartWidget() throws OBException
	{
		return SUCCESS;
	}
	
	public String loadWidget3() throws OBException
	{
		return SUCCESS;
	}
	
	public Integer getWidgetOrder()
	{
		return widgetOrder;
	}
	
	public ArrayList<OBDtoWidgetItemInfo> getWidgetItemList()
	{
		return widgetItemList;
	}
	
	public void setWidgetItemList(ArrayList<OBDtoWidgetItemInfo> widgetItemList)
	{
		this.widgetItemList = widgetItemList;
	}

	public void setWidgetOrder(Integer widgetOrder)
	{
		this.widgetOrder = widgetOrder;
	}

	public ArrayList<OBDtoDashboardInfo> getDashboardInfoList()
	{
		return dashboardInfoList;
	}

	public void setDashboardInfoList(ArrayList<OBDtoDashboardInfo> dashboardInfoList)
	{
		this.dashboardInfoList = dashboardInfoList;
	}

	public OBDtoDashboardInfo getDashboardInfo()
	{
		return dashboardInfo;
	}

	public void setDashboardInfo(OBDtoDashboardInfo dashboardInfo)
	{
		this.dashboardInfo = dashboardInfo;
	}
	
	public ArrayList<OBDtoWidgetInfo> getWidgetList()
	{
		return widgetList;
	}

	public void setWidgetList(ArrayList<OBDtoWidgetInfo> widgetList)
	{
		this.widgetList = widgetList;
	}

	public String getDashboardIndex()
	{
		return dashboardIndex;
	}

	public void setDashboardIndex(String dashboardIndex)
	{
		this.dashboardIndex = dashboardIndex;
	}
	
	public String getDashboardName()
	{
		return dashboardName;
	}
	
	public void setDashboardName(String dashboardName)
	{
		this.dashboardName = dashboardName;
	}
	
	public String getWidgetListString()
	{
		return widgetListString;
	}
	
	public void setWidgetListString(String widgetListString)
	{
		this.widgetListString = widgetListString;
	}
	
	public OBDtoWidgetInfo getWidgetInfo()
	{
		return widgetInfo;
	}

	public void setWidgetInfo(OBDtoWidgetInfo widgetInfo)
	{
		this.widgetInfo = widgetInfo;
	}
	
	public OBDtoADCObject getWidgetTarget()
	{
		return widgetTarget;
	}

	public void setWidgetTarget(OBDtoADCObject widgetTarget)
	{
		this.widgetTarget = widgetTarget;
	}

	public ArrayList<OBDtoADCObject> getWidgetTargetList()
	{
		return widgetTargetList;
	}

	public void setWidgetTargetList(ArrayList<OBDtoADCObject> widgetTargetList)
	{
		this.widgetTargetList = widgetTargetList;
	}
	
	public Integer getHour()
	{
		return hour;
	}

	public void setHour(Integer hour)
	{
		this.hour = hour;
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
	
	public AdcMemoryInfoDto getAdcMemoryInfo()
	{
		return adcMemoryInfo;
	}

	public void setAdcMemoryInfo(AdcMemoryInfoDto adcMemoryInfo)
	{
		this.adcMemoryInfo = adcMemoryInfo;
	}		

	public AdcCpuInfoDto getAdcCpuInfo()
	{
		return adcCpuInfo;
	}

	public void setAdcCpuInfo(AdcCpuInfoDto adcCpuInfo)
	{
		this.adcCpuInfo = adcCpuInfo;
	}		
	
	public Integer getAccountIndex()
	{
		return accountIndex;
	}
	
	public void setAccountIndex(Integer accountIndex)
	{
		this.accountIndex = accountIndex;
	}
	
	public List<OBDtoAdcmonTraffic> getTop10VSTrafficList()
	{
		return top10VSTrafficList;
	}

	public void setTop10VSTrafficList(List<OBDtoAdcmonTraffic> top10vsTrafficList)
	{
		top10VSTrafficList = top10vsTrafficList;
	}

	public List<OBDtoAdcmonTraffic> getTop10AdcTrafficList()
	{
		return top10AdcTrafficList;
	}

	public void setTop10AdcTrafficList(List<OBDtoAdcmonTraffic> top10AdcTrafficList)
	{
		this.top10AdcTrafficList = top10AdcTrafficList;
	}
	
	public OBDtoWidgetItemInfo getWidgetItem()
	{
		return widgetItem;
	}

	public void setWidgetItem(OBDtoWidgetItemInfo widgetItem)
	{
		this.widgetItem = widgetItem;
	}

	public List<SdsDashboardAdcSummaryDto> getAdcSummaryList()
	{
		return adcSummaryList;
	}

	public void setAdcSummaryList(List<SdsDashboardAdcSummaryDto> adcSummaryList)
	{
		this.adcSummaryList = adcSummaryList;
	}

	public Integer getFaultMaxDays()
	{
		return faultMaxDays;
	}

	public void setFaultMaxDays(Integer faultMaxDays)
	{
		this.faultMaxDays = faultMaxDays;
	}

	public SdsDashboardStatusSummaryDto getStatusSummary()
	{
		return statusSummary;
	}

	public void setStatusSummary(SdsDashboardStatusSummaryDto statusSummary)
	{
		this.statusSummary = statusSummary;
	}
	
	public OBDtoAdcmonFaultStatus getDashFaultStatus()
	{
		return dashFaultStatus;
	}

	public void setDashFaultStatus(OBDtoAdcmonFaultStatus dashFaultStatus)
	{
		this.dashFaultStatus = dashFaultStatus;
	}
	
	public List<VsStatusDataDto> getVsStatusInfo()
	{
		return vsStatusInfo;
	}

	public OBDtoAdcmonSlbChangeStatus getDashSlbChangeStatus()
	{
		return dashSlbChangeStatus;
	}

	public void setDashSlbChangeStatus(OBDtoAdcmonSlbChangeStatus dashSlbChangeStatus)
	{
		this.dashSlbChangeStatus = dashSlbChangeStatus;
	}
	
	public void setVsStatusInfo(List<VsStatusDataDto> vsStatusInfo)
	{
		this.vsStatusInfo = vsStatusInfo;
	}

	public List<OBDtoDataObj> getAllAdcTrafficInfo()
	{
		return allAdcTrafficInfo;
	}

	public void setAllAdcTrafficInfo(List<OBDtoDataObj> allAdcTrafficInfo)
	{
		this.allAdcTrafficInfo = allAdcTrafficInfo;
	}
	
	public List<SdsDashboardVsSummaryDto> getVsSummaryList()
	{
		return vsSummaryList;
	}

	public void setVsSummaryList(List<SdsDashboardVsSummaryDto> vsSummaryList)
	{
		this.vsSummaryList = vsSummaryList;
	}	

	public AdcConnectionInfoDto getAdcConnectionInfo()
	{
		return adcConnectionInfo;
	}

	public void setAdcConnectionInfo(AdcConnectionInfoDto adcConnectionInfo)
	{
		this.adcConnectionInfo = adcConnectionInfo;
	}

	public AdcThroughputInfoDto getAdcThroughputInfo()
	{
		return adcThroughputInfo;
	}

	public void setAdcThroughputInfo(AdcThroughputInfoDto adcThroughputInfo)
	{
		this.adcThroughputInfo = adcThroughputInfo;
	}	
	public ArrayList<OBDtoDataObj> getResponseTimeInfo()
    {
        return ResponseTimeInfo;
    }
    public void setResponseTimeInfo(ArrayList<OBDtoDataObj> responseTimeInfo)
    {
        ResponseTimeInfo = responseTimeInfo;
    }
    public OBDtoFaultHWStatus getFaultHWStatus()
	{
		return faultHWStatus;
	}
	public void setFaultHWStatus(OBDtoFaultHWStatus faultHWStatus)
	{
		this.faultHWStatus = faultHWStatus;
	}
	public ArrayList<OBDtoFaultBpsConnInfo> getVsThroughputInfo()
    {
        return vsThroughputInfo;
    }
    public void setVsThroughputInfo(ArrayList<OBDtoFaultBpsConnInfo> vsThroughputInfo)
    {
        this.vsThroughputInfo = vsThroughputInfo;
    }
    public OBDtoAdcCurrentSession getSessionHistory()
	{
		return sessionHistory;
	}
	public void setSessionHistory(OBDtoAdcCurrentSession sessionHistory)
	{
		this.sessionHistory = sessionHistory;
	}			
	public ArrayList<OBDtoFaultBpsConnInfo> getGroupBpsConnHistoryInfo()
	{
		return GroupBpsConnHistoryInfo;
	}
	public void setGroupBpsConnHistoryInfo(ArrayList<OBDtoFaultBpsConnInfo> groupBpsConnHistoryInfo)
	{
		GroupBpsConnHistoryInfo = groupBpsConnHistoryInfo;
	}		
	public OBDtoFaultCpuHistory getCpuSpConns()
	{
		return cpuSpConns;
	}
	public void setCpuSpConns(OBDtoFaultCpuHistory cpuSpConns)
	{
		this.cpuSpConns = cpuSpConns;
	}	
	public OBDtoOrdering getOrderOption()
    {
        return orderOption;
    }
    public void setOrderOption(OBDtoOrdering orderOption)
    {
        this.orderOption = orderOption;
    }    
    public OBDtoAdcInfo getAdcInfo()
    {
        return adcInfo;
    }
    public void setAdcInfo(OBDtoAdcInfo adcInfo)
    {
        this.adcInfo = adcInfo;
    }    
    public List<OBDtoDashboardStatusNotification> getNotifcationList()
    {
        return notifcationList;
    }
    public void setNotifcationList(List<OBDtoDashboardStatusNotification> notifcationList)
    {
        this.notifcationList = notifcationList;
    }    
    public ArrayList<OBDtoFaultBpsConnInfo> getVsConcurrentInfo()
    {
        return vsConcurrentInfo;
    }
    public void setVsConcurrentInfo(ArrayList<OBDtoFaultBpsConnInfo> vsConcurrentInfo)
    {
        this.vsConcurrentInfo = vsConcurrentInfo;
    }
    public ArrayList<OBDtoGroupHistory> getServiceGroupConcurrentInfo()
    {
        return serviceGroupConcurrentInfo;
    }
    public void setServiceGroupConcurrentInfo(ArrayList<OBDtoGroupHistory> serviceGroupConcurrentInfo)
    {
        this.serviceGroupConcurrentInfo = serviceGroupConcurrentInfo;
    }
    public ArrayList<OBDtoGroupHistory> getAdcGroupMemoryInfo()
    {
        return adcGroupMemoryInfo;
    }
    public void setAdcGroupMemoryInfo(ArrayList<OBDtoGroupHistory> adcGroupMemoryInfo)
    {
        this.adcGroupMemoryInfo = adcGroupMemoryInfo;
    }
    public ArrayList<OBDtoGroupHistory> getAdcGroupCpuInfo()
    {
        return adcGroupCpuInfo;
    }
    public void setAdcGroupCpuInfo(ArrayList<OBDtoGroupHistory> adcGroupCpuInfo)
    {
        this.adcGroupCpuInfo = adcGroupCpuInfo;
    }
    public ArrayList<OBDtoGroupHistory> getServiceGroupResponseTimeInfo()
    {
        return serviceGroupResponseTimeInfo;
    }
    public void setServiceGroupResponseTimeInfo(ArrayList<OBDtoGroupHistory> serviceGroupResponseTimeInfo)
    {
        this.serviceGroupResponseTimeInfo = serviceGroupResponseTimeInfo;
    }
    public ArrayList<OBDtoGroupHistory> getServiceGroupThroughputInfo()
    {
        return serviceGroupThroughputInfo;
    }
    public void setServiceGroupThroughputInfo(ArrayList<OBDtoGroupHistory> serviceGroupThroughputInfo)
    {
        this.serviceGroupThroughputInfo = serviceGroupThroughputInfo;
    }
    public Integer getMonitoringPeriod()
    {
        return monitoringPeriod;
    }
    public void setMonitoringPeriod(Integer monitoringPeriod)
    {
        this.monitoringPeriod = monitoringPeriod;
    }
    public Integer getIntervalMonitor()
    {
        return intervalMonitor;
    }
    public void setIntervalMonitor(Integer intervalMonitor)
    {
        this.intervalMonitor = intervalMonitor;
    }
    @Override
    public String toString()
    {
        return "DashBoardAction [widgetOrder=" + widgetOrder
                + ", dashboardInfoList=" + dashboardInfoList
                + ", dashboardLastList=" + dashboardLastList
                + ", widgetItemList=" + widgetItemList + ", widgetItem="
                + widgetItem + ", dashboardInfo=" + dashboardInfo
                + ", widgetList=" + widgetList + ", widgetInfo=" + widgetInfo
                + ", dashboardIndex=" + dashboardIndex + ", dashboardName="
                + dashboardName + ", widgetListString=" + widgetListString
                + ", widgetTarget=" + widgetTarget + ", widgetTargetList="
                + widgetTargetList + ", hour=" + hour + ", startTime="
                + startTime + ", endTime=" + endTime + ", adcInfo=" + adcInfo
                + ", orderOption=" + orderOption + ", adcMemoryInfo="
                + adcMemoryInfo + ", adcCpuInfo=" + adcCpuInfo
                + ", vsStatusInfo=" + vsStatusInfo + ", allAdcTrafficInfo="
                + allAdcTrafficInfo + ", adcConnectionInfo="
                + adcConnectionInfo + ", adcThroughputInfo="
                + adcThroughputInfo + ", vsConcurrentInfo=" + vsConcurrentInfo
                + ", vsThroughputInfo=" + vsThroughputInfo
                + ", sessionHistory=" + sessionHistory
                + ", GroupBpsConnHistoryInfo=" + GroupBpsConnHistoryInfo
                + ", top10VSTrafficList=" + top10VSTrafficList
                + ", accountIndex=" + accountIndex + ", top10AdcTrafficList="
                + top10AdcTrafficList + ", adcSummaryList=" + adcSummaryList
                + ", vsSummaryList=" + vsSummaryList + ", faultMaxDays="
                + faultMaxDays + ", statusSummary=" + statusSummary
                + ", dashFaultStatus=" + dashFaultStatus
                + ", dashSlbChangeStatus=" + dashSlbChangeStatus
                + ", ResponseTimeInfo=" + ResponseTimeInfo + ", faultHWStatus="
                + faultHWStatus + ", cpuSpConns=" + cpuSpConns
                + ", notifcationList=" + notifcationList
                + ", serviceGroupConcurrentInfo=" + serviceGroupConcurrentInfo
                + ", adcGroupMemoryInfo=" + adcGroupMemoryInfo
                + ", adcGroupCpuInfo=" + adcGroupCpuInfo
                + ", serviceGroupResponseTimeInfo="
                + serviceGroupResponseTimeInfo
                + ", serviceGroupThroughputInfo=" + serviceGroupThroughputInfo
                + ", monitoringPeriod=" + monitoringPeriod
                + ", intervalMonitor=" + intervalMonitor + ", dashboardFacade="
                + dashboardFacade + "]";
    }
}