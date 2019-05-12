package kr.openbase.adcsmart.web.facade.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.OBAdcManagement;
import kr.openbase.adcsmart.service.OBDashboardAdc;
import kr.openbase.adcsmart.service.OBDashboardSds;
import kr.openbase.adcsmart.service.OBDynamicDashboard;
import kr.openbase.adcsmart.service.OBFaultMonitoring;
import kr.openbase.adcsmart.service.dashboard.OBDynamicDashboardImpl;
import kr.openbase.adcsmart.service.dashboard.dto.OBDtoDashboardInfo;
import kr.openbase.adcsmart.service.dashboard.dto.OBDtoWidgetItemInfo;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoConnection;
import kr.openbase.adcsmart.service.dto.OBDtoCpu;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsAdcSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsStatusSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservStatus;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservSummary;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardStatusNotification;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoMemory;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoThroughput;
import kr.openbase.adcsmart.service.dto.OBDtoUsageConnection;
import kr.openbase.adcsmart.service.dto.OBDtoUsageCpu;
import kr.openbase.adcsmart.service.dto.OBDtoUsageMem;
import kr.openbase.adcsmart.service.dto.OBDtoUsageThroughput;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardFaultStatus;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardSlbChangeStatus;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardTraffic;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoGroupHistory;
import kr.openbase.adcsmart.service.dto.fault.OBDtoAdcCurrentSession;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCpuHistory;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultHWStatus;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBDashboardAdcImpl;
import kr.openbase.adcsmart.service.impl.OBDashboardSdsImpl;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMonitoringImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AdcConnectionDataDto;
import kr.openbase.adcsmart.web.facade.dto.AdcConnectionInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcCpuDataDto;
import kr.openbase.adcsmart.web.facade.dto.AdcCpuInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcMemoryDataDto;
import kr.openbase.adcsmart.web.facade.dto.AdcMemoryInfoDto;
import kr.openbase.adcsmart.web.facade.dto.AdcThroughputDataDto;
import kr.openbase.adcsmart.web.facade.dto.AdcThroughputInfoDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardAdcSummaryDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardStatusSummaryDto;
import kr.openbase.adcsmart.web.facade.dto.SdsDashboardVsSummaryDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.facade.dto.VsConfigEventDto;
import kr.openbase.adcsmart.web.facade.dto.VsStatusDataDto;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonFaultStatus;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonSlbChangeStatus;
import kr.openbase.adcsmart.web.facade.dto.adcmon.OBDtoAdcmonTraffic;
import kr.openbase.adcsmart.web.util.NumberUtil;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DashboardFacade
{
private static transient Logger log = LoggerFactory.getLogger(DashboardFacade.class);
	
	private OBDynamicDashboard dashboardSvc;
	private OBDashboardSds adcSummaryDashboard; 		// ADC요약 Dashboard 인터페이스
	private OBDashboardAdc adcMonitoringDashboard;		// ADC모니터링 Dashboard 인터페이스
	private OBFaultMonitoring faultMonitoringSvc;		// 장애 모니터링 인터페이스
	private OBAdcManagement adcManagementSvc;			// ADC 장비 모니터링 위젯에 필요 ( 선택된 ADC 정보 Get )
	
	public DashboardFacade()
	{
		dashboardSvc = new OBDynamicDashboardImpl();
		adcSummaryDashboard = new OBDashboardSdsImpl();
		adcMonitoringDashboard = new OBDashboardAdcImpl();
		faultMonitoringSvc = new OBFaultMonitoringImpl();
		adcManagementSvc = new OBAdcManagementImpl();
	}
	
	// 전체 대시보드 목록 정보를 제공한다. param : null
	public ArrayList<OBDtoDashboardInfo> getDashboardInfoList() throws Exception
	{
		ArrayList<OBDtoDashboardInfo> dashboardInfoList = new ArrayList<OBDtoDashboardInfo>();
		dashboardInfoList = dashboardSvc.getDashboardInfoList();
		log.debug("{}", dashboardInfoList);		
		return dashboardInfoList;		
	}
	// 최근 사용한 Dashboard 목록 조회
	public ArrayList<OBDtoDashboardInfo> getDashboardInfoLastList() throws Exception
	{
		ArrayList<OBDtoDashboardInfo> dashboardLastList = new ArrayList<OBDtoDashboardInfo>();
		dashboardLastList = dashboardSvc.getDashboardInfoLastList();
		log.debug("{}", dashboardLastList);
		return dashboardLastList;
	}	
	// 전체 Widget 정보를 제공한다. param : null
	public ArrayList<OBDtoWidgetItemInfo> getWidgetItemList() throws Exception
	{
		ArrayList<OBDtoWidgetItemInfo> widgetItemList = new ArrayList<OBDtoWidgetItemInfo>();
		widgetItemList = dashboardSvc.getWidgetItemList();
		log.debug("{}", widgetItemList);		
		return widgetItemList;	//서비스 진입점 차단
	}
	
	// 지정된 대시보드 정보 조회 , param : index
	public OBDtoDashboardInfo getDashboardInfo(String index) throws Exception
	{	
		OBDtoDashboardInfo dashboardInfo = new OBDtoDashboardInfo();	
		dashboardInfo = dashboardSvc.getDashboardInfo(index);
		log.debug("{}", dashboardInfo);		
		return dashboardInfo;
	}
	// Widget Add 할 때 Target 리스트 조회 , param : targetInfo
	public ArrayList<OBDtoADCObject> getInvolvedObjectList(OBDtoADCObject target, SessionDto session) throws Exception
	{
		ArrayList<OBDtoADCObject> targetInfo = new ArrayList<OBDtoADCObject>();
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(target.getName());
		targetInfo = dashboardSvc.getInvolvedObjectList(target, extraInfo);
		return targetInfo;
	}
	
	public ArrayList<OBDtoADCObject> getInvolvedVsList(OBDtoADCObject target, SessionDto session) throws Exception
    {
        ArrayList<OBDtoADCObject> targetInfo = new ArrayList<OBDtoADCObject>();
        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
        extraInfo.setExtraMsg1(target.getName());
        targetInfo = dashboardSvc.getInvolvedVsList(target, extraInfo);
        return targetInfo;
    }
	
	public ArrayList<OBDtoADCObject> getInvolvedMemberList(OBDtoADCObject target, SessionDto session) throws Exception
    {
        ArrayList<OBDtoADCObject> targetInfo = new ArrayList<OBDtoADCObject>();
        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
        extraInfo.setExtraMsg1(target.getName());
        targetInfo = dashboardSvc.getInvolvedMemberList(target, extraInfo);
        return targetInfo;
    }
		
	// Widget Add 할 때 Target 리스트 조회 , param : targetInfo
	public ArrayList<OBDtoADCObject> getInvolvedObjectListAlteon(OBDtoADCObject target, SessionDto session) throws Exception
	{
		ArrayList<OBDtoADCObject> targetInfo = new ArrayList<OBDtoADCObject>();
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(target.getName());
		targetInfo = dashboardSvc.getInvolvedObjectListAlteon(target, extraInfo);
		return targetInfo;
	}
	
	// Widget Add 할 때 Target 리스트 조회 , param : targetInfo
	public ArrayList<OBDtoADCObject> getFlbGroupList(OBDtoADCObject target, SessionDto session) throws Exception
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(target.getName());
		return dashboardSvc.getFlbGroupList(target, extraInfo);
	}
	// 신규 DashBoard 추가
	public void addDashboardInfo(OBDtoDashboardInfo info, SessionDto session) throws Exception
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(info.getName());
		dashboardSvc.addDashboardInfo(info, extraInfo);		
	}
	// 선택된 DashBoard 수정
	public void setDashboardInfo(String index, OBDtoDashboardInfo info, SessionDto session) throws Exception
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(info.getName());
		dashboardSvc.setDashboardInfo(index, info, extraInfo);		
	}
	// 선택된 DashBoard 삭제
	public void delDashboardInfo(String index, SessionDto session) throws Exception
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		dashboardSvc.delDashboardInfo(index, extraInfo);		
	}
	
	/*
	 * Widget Facade
	 * 1. 장애 모니터링 통계
	 * 2. ADC 설정 변경 현황(1주일) Chart
	 * 3. 최근장애내역
	 * 4. ADC 설정 변경 현황(1주일) Chart
	 * 5. 최근변경내역
	 * 6. ADC 상태 통계 화면
	 * 7. VS 상태 통계 화면
	 * 8. ADC Connection 추이 Chart
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
	// 1,6,7.  장애 모니터링 통계
	public SdsDashboardStatusSummaryDto getStatusSummary(Integer accountIndex, Integer faultMaxDays) throws Exception 
	{
		SdsDashboardStatusSummaryDto statusSummary = null;
		OBDtoDashboardSdsStatusSummary obDtoStatusSummary = adcSummaryDashboard.getStatusSummary(accountIndex, faultMaxDays);
		if (null != obDtoStatusSummary) 
		{
			statusSummary = SdsDashboardStatusSummaryDto.getSdsDashboardStatusSummary(obDtoStatusSummary);
		}
		return statusSummary;
	}
	
	// 2,3. 장애 모니터링 현황(1주일)
	public OBDtoAdcmonFaultStatus getFaultMonitoring(Integer accountIndex, Date fromTime, Date toTime) throws OBException, Exception
	{
//		OBDtoDashboardFaultStatus item = adcMonitoringDashboard.getFaultMonitoring(accountIndex, fromTime, toTime);
		OBDtoDashboardFaultStatus item = adcMonitoringDashboard.getDynamicFaultMonitoring(accountIndex, fromTime, toTime);	// 기존 데시보드와 다르게 5개까지 지원
		return new OBDtoAdcmonFaultStatus().toAdcmonFaultStatus(item);
	}
	
	// 4,5. ADC 설정 변경 현황(1주일)
	public OBDtoAdcmonSlbChangeStatus getSlbChangeMonitoring(Integer accountIndex, Date fromTime, Date toTime) throws OBException, Exception
	{
//		OBDtoDashboardSlbChangeStatus item= adcMonitoringDashboard.getSlbChangeMonitoring(accountIndex, fromTime, toTime);
		OBDtoDashboardSlbChangeStatus item= adcMonitoringDashboard.getDynamicSlbChangeMonitoring(accountIndex, fromTime, toTime); // 기존 데시보드와 다르게 5개까지 지원
		return  new OBDtoAdcmonSlbChangeStatus().toAdcmonSlbChangeStatus(item);
	
	}
	// 8. ADC ConcurrentSession 추이 Chart
	public AdcConnectionInfoDto getConnectionInfo(OBDtoADCObject widgetTarget, Date startTime, Date endTime) throws Exception 
	{
		log.debug("adcIndex:{}, startTime:{}, endTime:{}" , new Object[]{widgetTarget.getIndex()
				,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
				, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		AdcConnectionInfoDto adcConnectionInfoDto = null;		
		OBDtoConnection obDtoConnection = adcSummaryDashboard.getAdcConnections(widgetTarget.getIndex(), startTime, endTime);	
		log.debug("{}", obDtoConnection);
		if (null != obDtoConnection) 
		{
			List<OBDtoUsageConnection> obDtoUsageConnectionList = obDtoConnection.getConnectionList();
			if (!CollectionUtils.isEmpty(obDtoUsageConnectionList)) 
			{
				adcConnectionInfoDto = new AdcConnectionInfoDto();
				for (OBDtoUsageConnection obDtoUsageConnection : obDtoUsageConnectionList) 
				{
					adcConnectionInfoDto.getAdcConnectionDataList().add(AdcConnectionDataDto.getAdcConnectionData(obDtoUsageConnection));
				}
			}
			if (!CollectionUtils.isEmpty(obDtoConnection.getConfEventList())) 
			{
				if (null == adcConnectionInfoDto) 
				{
					adcConnectionInfoDto = new AdcConnectionInfoDto();
				}
				for (OBDtoAdcConfigHistory obDtoAdcConfigHistory : obDtoConnection.getConfEventList()) 
				{
					adcConnectionInfoDto.getVsConfigEventList().add(VsConfigEventDto.getVsConfigEvent(obDtoAdcConfigHistory));
				}
			}
		}
		return adcConnectionInfoDto;
	}	
	// 8. Vs ConcurrentSession 추이 - 개별 서비스

	public ArrayList<OBDtoFaultBpsConnInfo> getVsConcurrentInfo(OBDtoADCObject widgetTarget, OBDtoSearch searchOption) throws Exception 
	{
		return dashboardSvc.getVsConcurrentSessionHistoryByIndex(widgetTarget, searchOption);		
	}
	
	// 8. Vs ConcurrentSession 추이 - 서비스 그룹
	
	public ArrayList<OBDtoGroupHistory> getVsConcurrentInfoSeviceGroup(OBDtoADCObject widgetTarget, OBDtoSearch searchOption) throws Exception 
    {
        return dashboardSvc.getVsConcurrentSessionHistoryByIndexGroup(widgetTarget, searchOption);       
    }
	
	// 9. ADC Throughtput 추이 Chart
	public AdcThroughputInfoDto getThroughputInfo(OBDtoADCObject widgetTarget, Date startTime, Date endTime) throws Exception 
	{
		log.debug("adcIndex:{}, startTime:{}, endTime:{}"
				, new Object[]{widgetTarget.getIndex()
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		AdcThroughputInfoDto adcThroughputInfo = null;		
		OBDtoThroughput throughput = adcSummaryDashboard.getAdcThroughput(widgetTarget.getIndex(), startTime, endTime);
		log.debug("{}", throughput);
		if (null != throughput) 
		{
			List<OBDtoUsageThroughput> obDtoUsageThroughputList = throughput.getThroughputList();
			if (!CollectionUtils.isEmpty(obDtoUsageThroughputList)) 
			{
				adcThroughputInfo = new AdcThroughputInfoDto();
				for (OBDtoUsageThroughput obDtoUsageThroughput : obDtoUsageThroughputList) 
				{
					adcThroughputInfo.getAdcThroughputDataList().add(AdcThroughputDataDto.getAdcThroughputData(obDtoUsageThroughput));
				}
			}
			if (!CollectionUtils.isEmpty(throughput.getConfEventList())) 
			{
				if (null == adcThroughputInfo) 
				{
					adcThroughputInfo = new AdcThroughputInfoDto();
				}
				for (OBDtoAdcConfigHistory obDtoAdcConfigHistory : throughput.getConfEventList()) 
				{
					adcThroughputInfo.getVsConfigEventList().add(VsConfigEventDto.getVsConfigEvent(obDtoAdcConfigHistory));
				}
			}
		}
		return adcThroughputInfo;
	}
	// 9. Vs Throughtput 추이 Chart - service
	public ArrayList<OBDtoFaultBpsConnInfo> getVsThroughputInfo(OBDtoADCObject widgetTarget, OBDtoSearch searchOption) throws Exception 
	{
		return dashboardSvc.getVsThroughputHistoryByIndex(widgetTarget, searchOption);		
	}
	
	// 9. Vs Throughtput 추이 Chart - service group
    public ArrayList<OBDtoGroupHistory> getVsThroughputInfoGroup(OBDtoADCObject widgetTarget, OBDtoSearch searchOption) throws Exception 
    {
        return dashboardSvc.getVsThroughputHistoryByIndexGroup(widgetTarget, searchOption);      
    }
	
	// 10. CPU Histroy Chart Data
	public AdcCpuInfoDto getAdcCpuInfo(OBDtoADCObject widgetTarget, Date startTime, Date endTime) throws Exception
	{
		AdcCpuInfoDto adcCpuInfo = null;
		log.debug("adcIndex:{}, startTime:{}, endTime:{}"
				, new Object[]{widgetTarget.getIndex()
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
        OBDtoCpu obDtoCpu = adcSummaryDashboard.getAdcUsageCpu(widgetTarget.getIndex(), startTime, endTime);
        log.debug("{}", obDtoCpu);
        if (null != obDtoCpu) 
        {
            List<OBDtoUsageCpu> obDtoUsageCpuList = obDtoCpu.getCpuList();
            if (!CollectionUtils.isEmpty(obDtoUsageCpuList)) 
            {
                adcCpuInfo = new AdcCpuInfoDto();
                for (OBDtoUsageCpu obDtoUsageCpu : obDtoUsageCpuList) 
                {
                    adcCpuInfo.getAdcCpuDataList().add(AdcCpuDataDto.getAdcCpuData(obDtoUsageCpu));
                }
            }
        }

		return adcCpuInfo;
	}
	
	   // 10. CPU Histroy Chart Data
    public ArrayList<OBDtoGroupHistory> getAdcCpuGroupInfo(OBDtoADCObject widgetTarget, Date startTime, Date endTime) throws Exception
    {
        log.debug("adcIndex:{}, startTime:{}, endTime:{}"
                , new Object[]{widgetTarget.getIndex()
                        , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
                        , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
        ArrayList<OBDtoGroupHistory> adcCpuGroupListInfo = adcSummaryDashboard.getAdcUsageCpuGroup(widgetTarget.getIndex(), startTime, endTime);
        return adcCpuGroupListInfo;
    }
	
	// 11. Memory Histroy Chart Data
	public AdcMemoryInfoDto getAdcMemoryInfo(OBDtoADCObject widgetTarget, Date startTime, Date endTime) throws Exception
	{
	    AdcMemoryInfoDto adcMemoryInfo = null;
		log.debug("adcIndex:{}, startTime:{}, endTime:{}"
				, new Object[]{widgetTarget.getIndex()
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
	
	    OBDtoMemory obDtoMemory = adcSummaryDashboard.getAdcUsageMem(widgetTarget.getIndex(), startTime, endTime);
        log.debug("{}", obDtoMemory);
        if (null != obDtoMemory) 
        {
            List<OBDtoUsageMem> obDtoUsageMemList = obDtoMemory.getMemList();
            if (!CollectionUtils.isEmpty(obDtoUsageMemList)) 
            {
                adcMemoryInfo = new AdcMemoryInfoDto();
                for (OBDtoUsageMem obDtoUsageMem : obDtoUsageMemList) 
                {
                    adcMemoryInfo.getAdcMemoryDataList().add(AdcMemoryDataDto.getAdcMemoryData(obDtoUsageMem));
                }
            }
        }

		return adcMemoryInfo;
	}
	
	   // 11. Memory Histroy Chart Data
    public ArrayList<OBDtoGroupHistory> getAdcMemoryGroupInfo(OBDtoADCObject widgetTarget, Date startTime, Date endTime) throws Exception
    {

        log.debug("adcIndex:{}, startTime:{}, endTime:{}"
                , new Object[]{widgetTarget.getIndex()
                        , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
                        , new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
    
        ArrayList<OBDtoGroupHistory> adcMemoryGroupInfoList = adcSummaryDashboard.getAdcUsageMemGroup(widgetTarget.getIndex(), startTime, endTime);

        return adcMemoryGroupInfoList;
    }
	
	// 12. AllAdcTraffic Chart Data
	public ArrayList<OBDtoDataObj> getAllAdcTrafficInfo(Integer accountIndex, Date fromTime, Date toTime) throws Exception
	{		
		ArrayList<OBDtoDataObj> adcTrafficFromSvc = adcMonitoringDashboard.getTotalAdcTrafficGraph(accountIndex, fromTime, toTime);
		log.debug("{}", adcTrafficFromSvc);
		return adcTrafficFromSvc;		
	}
	
	// 13. VsStatus Chart Data
	public List<VsStatusDataDto> getVsStatusInfo(OBDtoADCObject widgetTarget, Date startTime, Date endTime) throws Exception 
	{
		log.debug("adcIndex:{}, startTime:{}, endTime:{}"
				, new Object[]{widgetTarget.getIndex()
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime)
						, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime)});
		List<VsStatusDataDto> vsStatusDataList = new ArrayList<VsStatusDataDto>();
		
		List<OBDtoDashboardSdsVservStatus> obDtoDashboardSdsVservStatusList = adcSummaryDashboard.getVservStatus(widgetTarget.getIndex(), startTime, endTime);
		log.debug("{}", obDtoDashboardSdsVservStatusList);
		if (!CollectionUtils.isEmpty(obDtoDashboardSdsVservStatusList)) 
		{
			for (OBDtoDashboardSdsVservStatus obDtoDashboardSdsVservStatus : obDtoDashboardSdsVservStatusList) 
			{
				vsStatusDataList.add(VsStatusDataDto.getVsStatusData(obDtoDashboardSdsVservStatus));
			}
		}
		return vsStatusDataList;
	}
	
	// 14. ADC Summary List
	public List<SdsDashboardAdcSummaryDto> getAdcSummaryList(Integer accountIndex, OBDtoADCObject widgetTarget, Integer status, Integer faultMaxDays, Integer orderType, Integer orderDir) throws Exception 
	{
		List<SdsDashboardAdcSummaryDto> adcSummaryList = null;
		
		List<OBDtoDashboardSdsAdcSummary> obDtoDashboardSdsAdcSummaryList = null;
		
		obDtoDashboardSdsAdcSummaryList = adcSummaryDashboard.getAdcListAll(accountIndex, status, faultMaxDays, orderType, orderDir);
		
		
		if (widgetTarget.getCategory().equals(0)) 
		{
			obDtoDashboardSdsAdcSummaryList = adcSummaryDashboard.getAdcListAll(accountIndex, status, faultMaxDays, orderType, orderDir);
		} 
		else if (widgetTarget.getCategory().equals(1)) 
		{
			if (null != widgetTarget.getIndex()) 
			{
				obDtoDashboardSdsAdcSummaryList = adcSummaryDashboard.getAdcListGroup(accountIndex, status, Integer.valueOf(widgetTarget.getIndex()), faultMaxDays, orderType, orderDir);
			}
		} 
		else if (widgetTarget.getCategory().equals(2)) 
		{
			if (null != widgetTarget.getIndex()) 
			{
				obDtoDashboardSdsAdcSummaryList = adcSummaryDashboard.getAdcListSingle(status, widgetTarget.getIndex(), faultMaxDays, orderType, orderDir);				
			}
		}
		log.debug("{}", obDtoDashboardSdsAdcSummaryList);
		
		if (!CollectionUtils.isEmpty(obDtoDashboardSdsAdcSummaryList)) 
		{
			adcSummaryList = new ArrayList<SdsDashboardAdcSummaryDto>();
			for (OBDtoDashboardSdsAdcSummary obDtoDashboardSdsAdcSummary : obDtoDashboardSdsAdcSummaryList) 
			{
				adcSummaryList.add(SdsDashboardAdcSummaryDto.getSdsDashboardAdcSummary(obDtoDashboardSdsAdcSummary));
			}
		}
		return adcSummaryList;
	}
	
	// 15. VS Summary List
	public List<SdsDashboardVsSummaryDto> getVsSummaryList(Integer accountIndex
			, OBDtoADCObject widgetTarget, Integer status, Integer faultMaxDays, Integer orderType, Integer orderDir) throws Exception {
		log.debug("accountIndex:{}, widgetTarget:{}, status:{}, faultMaxDays:{}"
				, new Object[]{accountIndex, widgetTarget, status, faultMaxDays});
		List<SdsDashboardVsSummaryDto> vsSummaryList = null;
		
		List<OBDtoDashboardSdsVservSummary> obDtoDashboardSdsVservSummaryList = null;
		if (widgetTarget.getCategory().equals(0))
		{
			if (13 == status) 
			{
				obDtoDashboardSdsVservSummaryList = adcSummaryDashboard.getVservListAllAdcUnavailNDays(accountIndex, faultMaxDays, orderType, orderDir);				
			} 
			else 
			{
				obDtoDashboardSdsVservSummaryList = adcSummaryDashboard.getVservListAllAdc(accountIndex, status, faultMaxDays, orderType, orderDir);	
			}
		} 
		else if (widgetTarget.getCategory().equals(1)) 
		{
			if (null != widgetTarget.getIndex())  
			{
				if (13 == status) 
				{
					obDtoDashboardSdsVservSummaryList = adcSummaryDashboard.getVservListGroupAdcUnavailNDays(accountIndex, Integer.valueOf(widgetTarget.getIndex()), faultMaxDays, orderType, orderDir);
				}
				else 
				{
					obDtoDashboardSdsVservSummaryList = adcSummaryDashboard.getVservListGroupAdc(accountIndex, status, Integer.valueOf(widgetTarget.getIndex()), faultMaxDays, orderType, orderDir);					
				}
			}
		}
		else if (widgetTarget.getCategory().equals(2)) 
		{
			if (null != widgetTarget.getIndex()) 
			{
				if (13 == status) 
				{
					obDtoDashboardSdsVservSummaryList = adcSummaryDashboard.getVservListSingleAdcUnavailNDays(widgetTarget.getIndex(), faultMaxDays, orderType, orderDir);					
				} 
				else 
				{
					obDtoDashboardSdsVservSummaryList = adcSummaryDashboard.getVservListSingleAdc(status, widgetTarget.getIndex(), faultMaxDays, orderType, orderDir);	
				}
			}
		}
		log.debug("{}", obDtoDashboardSdsVservSummaryList);

		if (!CollectionUtils.isEmpty(obDtoDashboardSdsVservSummaryList))
		{
			vsSummaryList = new ArrayList<SdsDashboardVsSummaryDto>();
			for (OBDtoDashboardSdsVservSummary obDtoDashboardSdsVservSummary : obDtoDashboardSdsVservSummaryList) 
			{
				vsSummaryList.add(SdsDashboardVsSummaryDto.getSdsDashboardVsSummary(obDtoDashboardSdsVservSummary));
			}
		}
		return vsSummaryList;
	}
	
	// 16. Top 10 Virtual Server list
	public ArrayList<OBDtoAdcmonTraffic> getTop10VSTrafficList(Integer accountIndex, OBDtoADCObject widgetTarget, OBDtoOrdering orderOption) throws OBException, Exception
	{
		ArrayList<OBDtoDashboardTraffic> top10TrafficList = adcMonitoringDashboard.getVSConnectionListTop10(accountIndex, widgetTarget, orderOption);
		ArrayList<OBDtoAdcmonTraffic> retVal = new ArrayList<OBDtoAdcmonTraffic>();
		
		for(OBDtoDashboardTraffic top10Traffic: top10TrafficList)
		{
			retVal.add(new OBDtoAdcmonTraffic().toAdcmonTrafficContent(top10Traffic));
		}		
		return retVal;
	}
	
	// 17. Top 5 ADC List
	public ArrayList<OBDtoAdcmonTraffic> getTop10AdcList(Integer accountIndex) throws OBException, Exception
	{
//		ArrayList<OBDtoDashboardTraffic> top5AdcList = adcMonitoringDashboard.getAdcTrafficListTop5(accountIndex);
		ArrayList<OBDtoDashboardTraffic> top5AdcList = adcMonitoringDashboard.getAdcTrafficListTop10(accountIndex);
		ArrayList<OBDtoAdcmonTraffic> retVal = new ArrayList<OBDtoAdcmonTraffic>();
		
		for(OBDtoDashboardTraffic top5Adc: top5AdcList)
		{
			retVal.add(new OBDtoAdcmonTraffic().toAdcmonTrafficContent(top5Adc));
		}		
		return retVal;
	}
	
	// 18. ADC 장비 모니터링
	public OBDtoADCObject getAdcInfo(OBDtoADCObject adcObject) throws OBException, Exception
	{
		OBDtoAdcInfo svcVal = adcManagementSvc.getAdcInfo(adcObject.getIndex());
		OBDtoADCObject retVal = new OBDtoADCObject();
		if(svcVal != null)
		{
			retVal.setExtraInfo(NumberUtil.extractNumbertoString(svcVal.getModel()));
			retVal.setVendor(svcVal.getAdcType());
			retVal.setStrIndex(svcVal.getSwVersion()); // OS Version 을 Index 에 Set 합니다.
			retVal.setStatus(svcVal.getSpSessionMax());
		}
		else
		{
			retVal.setExtraInfo(null);
			retVal.setVendor(null);
			retVal.setStrIndex(null); // OS Version 을 Index 에 Set 합니다.
			retVal.setStatus(null);
		}
		return retVal;		
	}
	
	public OBDtoFaultHWStatus getHWStatus(OBDtoADCObject adcObject) throws OBException, Exception
	{
		return faultMonitoringSvc.getADCMonHWStatus(adcObject);
	}
	
	public OBDtoAdcInfo getAdcInfoExt(Integer adcIndex) throws Exception
    {           
        OBDtoAdcInfo svc = adcManagementSvc.getAdcInfo(adcIndex);
        OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
        // 여기서는 모델명을 Integer 형으로 변경하여 사용하기 때문에. adcType에 임시 저장합니다.
        if (svc.getModel() != null && !svc.getModel().isEmpty())
        {
            if (svc.getModel().endsWith("p"))
            {
                adcInfo.setAdcType(NumberUtil.extractNumbertoString(svc.getModel())+1);
            }
            else
            {
                adcInfo.setAdcType(NumberUtil.extractNumbertoString(svc.getModel()));
            }
        }
        else
        {
            adcInfo.setAdcType(9999);
        }
        
        if(svc.getSwVersion() != null)
        {
            adcInfo.setSwVersion(svc.getSwVersion());
        }
        else
        {
            adcInfo.setSwVersion("");
        }
        adcInfo.setStatus(svc.getStatus());
        adcInfo.setRoleFlbYn(svc.getRoleFlbYn());   
        adcInfo.setSpSessionMax(svc.getSpSessionMax());
        return adcInfo;
    }
	
	//19. 응답시간 추이 - 단일 서비스
	public ArrayList<OBDtoDataObj> getResponseTimeHistory(OBDtoADCObject widgetTarget, OBDtoSearch searchOption) throws OBException, Exception
	{		
		return dashboardSvc.getResponseTimeHistoryByIndex(widgetTarget, searchOption);		
	}
	//19. 응답시간 추이 - 서비스 그룹
    public ArrayList<OBDtoGroupHistory> getResponseTimeHistoryGroup(OBDtoADCObject widgetTarget, OBDtoSearch searchOption) throws OBException, Exception
    {       
        return dashboardSvc.getResponseTimeHistoryByIndexGroup(widgetTarget, searchOption);      
    }
	
	// 20. ConcurrentSession 추이 Chart (Detail)
	public OBDtoAdcCurrentSession getAdcConcurrentInfo(OBDtoADCObject adcObject, OBDtoSearch searchOption) throws Exception
	{
		return faultMonitoringSvc.getADCMonSessionHistoryNew(adcObject, searchOption);	
	}
	// 21. ConcurrentSession 추이 Chart (FLB)
	public ArrayList<OBDtoFaultBpsConnInfo> getFLBConcurrentSessionInfo(String grouptIndex, OBDtoSearch searchOption) throws Exception
	{
		return faultMonitoringSvc.getGroupBpsConnHistory(grouptIndex, searchOption);	
	}
	// 22. CPU SP별 사용률, Connection 정보
	public OBDtoFaultCpuHistory getCpuSpConnectionInfo(OBDtoADCObject adcObject, OBDtoSearch searchOption) throws Exception
	{
		return faultMonitoringSvc.getADCMonCpuSpConnectionInfo(adcObject, searchOption);
	}
	
	// 24. ADC 상태 알리미
    public List<OBDtoDashboardStatusNotification> getAdcStatusNotification(OBDtoADCObject adcObject) throws Exception
    {
        ArrayList<OBDtoDashboardStatusNotification> adcNotification = dashboardSvc.getAdcStatusNotification(adcObject);
        log.debug("{}", adcNotification);
        return adcNotification;
    }
    
    // 25. VS 상태 알리미
    public List<OBDtoDashboardStatusNotification> getVsStatusNotification(OBDtoADCObject adcObject) throws Exception
    {
        ArrayList<OBDtoDashboardStatusNotification> vsNotification = dashboardSvc.getVsStatusNotification(adcObject);
        log.debug("{}", vsNotification);
        return vsNotification;
    }
    
 // 26. Member 상태 알리미
    public List<OBDtoDashboardStatusNotification> getMemberStatusNotification(OBDtoADCObject adcObject) throws Exception
    {
        ArrayList<OBDtoDashboardStatusNotification> memberNotification = dashboardSvc.getMemberStatusNotification(adcObject);
        log.debug("{}", memberNotification);
        return memberNotification;
    }
}