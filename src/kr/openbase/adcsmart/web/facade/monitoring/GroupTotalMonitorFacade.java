package kr.openbase.adcsmart.web.facade.monitoring;

import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBMonitoring;
import kr.openbase.adcsmart.service.dto.OBDtoAdcScope;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoServiceMonitoringChart;
import kr.openbase.adcsmart.service.impl.OBMonitoringImpl;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalAdc;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalAdcCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalGroup;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalGroupCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalReal;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalRealCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalService;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalServiceCondition;
import kr.openbase.adcsmart.service.utility.OBException;

@Component
public class GroupTotalMonitorFacade 
{
	private OBMonitoring monSvc;
	
	public GroupTotalMonitorFacade()
	{
		monSvc = new OBMonitoringImpl();
	}
	
	public Integer getTotalAdcListCount(OBDtoAdcScope adcScope, Integer accountIndex, OBDtoMonTotalAdcCondition adcCondition) throws OBException, Exception
	{
		return monSvc.getTotalAdcListCount(adcScope, accountIndex, adcCondition);
	}
	
//	public OBDtoMonTotalAdc getTotalAdcList(OBDtoAdcScope adcScope, Integer accountIndex, OBDtoMonTotalAdcCondition adcCondition, OBDtoSearch searchOption, OBDtoOrdering orderOption) throws OBException, Exception
	public OBDtoMonTotalAdc getTotalAdcList(OBDtoAdcScope adcScope, Integer accountIndex, OBDtoMonTotalAdcCondition condition, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException, Exception
	{
//		OBDtoAdcScope scope, OBDtoMonTotalAdcCondition condition, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir
		
//		OBDtoMonTotalAdcCondition adcCondition = new OBDtoMonTotalAdcCondition();
//		return monSvc.getTotalAdcList(adcScope, accountIndex, adcCondition, searchOption.getBeginIndex(), searchOption.getEndIndex(), orderOption.getOrderType(), orderOption.getOrderDirection());
		return monSvc.getTotalAdcList(adcScope, accountIndex, condition, beginIndex, endIndex, orderType, orderDir);
	}
	
	public OBDtoMonTotalAdc getTotalAdcListToExport(OBDtoAdcScope adcScope, Integer accountIndex, OBDtoMonTotalAdcCondition condition, Integer beginIndex, Integer endIndex) throws Exception 
	{
		return monSvc.getTotalAdcListToExport(adcScope, accountIndex, condition, beginIndex, endIndex);
	}
	
	// 서비스
	public Integer getTotalSvcListCount(OBDtoAdcScope adcScope, Integer accountIndex, String accountRole, OBDtoMonTotalServiceCondition svcCondition) throws OBException, Exception
	{
		return monSvc.getTotalServiceListCount(adcScope, accountIndex, accountRole, svcCondition);
	}
	
	public OBDtoMonTotalService getTotalSvcList(OBDtoAdcScope adcScope, Integer accountIndex, String accountRole, OBDtoMonTotalServiceCondition condition, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException, Exception
	{
		return monSvc.getTotalServiceList(adcScope, accountIndex, accountRole, condition, beginIndex, endIndex, orderType, orderDir);
	}
	
	// Vs Throughtput / ConcurrentSession 추이 Chart - multi
    public OBDtoServiceMonitoringChart getMultiBpsConnInfo(String vsIndexList, String vsNameList, OBDtoSearch searchOption) throws Exception 
    {
//        return monSvc.getVsThroughputHistory(vsIndexList, searchOption);      
        return monSvc.getServiceMonitoringChart(vsIndexList, vsNameList, searchOption);
    }
      
 // 서비스 성능 avg/max data
//    public OBDtoServiceMonitoringChart getMultiBpsConnMaxAvgInfo(String vsIndexList, String vsNameList, OBDtoSearch searchOption) throws Exception
//    {
//        OBDtoServiceMonitoringChart bpsConnCurAvgMaxData = monSvc.getServiceMonitoringChart(vsIndexList, vsNameList, searchOption);
//        
//        
//        bpsConnCurAvgMaxData.setBpsInfo(bpsInfo).setCurrBpsInUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getCurrBpsIn(), ""));
//        bpsConnMaxAvgData.setCurrConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getCurrConnCurr(), ""));
//        
//        bpsConnMaxAvgData.setAvgBpsInUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getAvgBpsIn(), ""));
//        bpsConnMaxAvgData.setAvgConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getAvgConnCurr(), ""));
//        
//        bpsConnMaxAvgData.setMaxBpsInUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getMaxBpsIn(), ""));
//        bpsConnMaxAvgData.setMaxConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getMaxConnCurr(), ""));
//        
//        return bpsConnMaxAvgData;
//    }
	public OBDtoMonTotalService getTotalSvcListToExport(OBDtoAdcScope adcScope, Integer accountIndex, String accountRole, OBDtoMonTotalServiceCondition condition, Integer beginIndex, Integer endIndex) throws OBException, Exception
	{
		return monSvc.getTotalServiceListToExport(adcScope, accountIndex, accountRole, condition, beginIndex, endIndex);
	}
	
	// 그룹
	public Integer getTotalGrpListCount(OBDtoAdcScope adcScope, Integer accountIndex, OBDtoMonTotalGroupCondition grpCondition) throws OBException, Exception
	{
		return monSvc.getTotalGroupListCount(adcScope, accountIndex, grpCondition);
	}
	
	public OBDtoMonTotalGroup getTotalGrpList(OBDtoAdcScope adcScope, Integer accountIndex, OBDtoMonTotalGroupCondition grpCondition, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException, Exception
	{
		return monSvc.getTotalGroupList(adcScope, accountIndex, grpCondition, beginIndex, endIndex, orderType, orderDir);
	}
	
	public OBDtoMonTotalGroup getTotalGrpListToExport(OBDtoAdcScope adcScope, Integer accountIndex, OBDtoMonTotalGroupCondition condition, Integer beginIndex, Integer endIndex) throws OBException, Exception
	{
		return monSvc.getTotalGroupListToExport(adcScope, accountIndex, condition, beginIndex, endIndex);
	}

	// Real
	public Integer getTotalRealListCount(OBDtoAdcScope adcScope, Integer accountIndex, OBDtoMonTotalRealCondition rsCondition) throws OBException, Exception
	{
		return monSvc.getTotalRealListCount(adcScope, accountIndex, rsCondition);
	}
	
	public OBDtoMonTotalReal getTotalRealList(OBDtoAdcScope adcScope, Integer accountIndex, OBDtoMonTotalRealCondition rsCondition, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir) throws OBException, Exception
	{
		return monSvc.getTotalRealList(adcScope, accountIndex, rsCondition, beginIndex, endIndex, orderType, orderDir);
	}
	
	public OBDtoMonTotalReal getTotalRealListToExport(OBDtoAdcScope adcScope, Integer accountIndex, OBDtoMonTotalRealCondition condition, Integer beginIndex, Integer endIndex) throws OBException, Exception
	{
		return monSvc.getTotalRealListToExport(adcScope, accountIndex, condition, beginIndex, endIndex);
	}
}
