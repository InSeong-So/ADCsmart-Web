package kr.openbase.adcsmart.web.facade.monitoring;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.OBFaultMonitoring;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoTrafficMapMember;
import kr.openbase.adcsmart.service.dto.OBDtoTrafficMapVServiceMembers;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultPreBpsConnChart;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultSvcPerfInfo;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMonitoringImpl;
import kr.openbase.adcsmart.web.facade.dto.FaultSvcPerfInfoDto;
import kr.openbase.adcsmart.web.facade.dto.VsMemberTrafficInfoDto;
import kr.openbase.adcsmart.web.facade.dto.VsTrafficInfoDto;
import kr.openbase.adcsmart.web.util.NumberUtil;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ServicePerfomanceMonitorFacade
{
	private static transient Logger log = LoggerFactory.getLogger(ServicePerfomanceMonitorFacade.class);
	
	private OBFaultMonitoring faultMonitoringSvc;
	
	public ServicePerfomanceMonitorFacade()
	{
		faultMonitoringSvc = new OBFaultMonitoringImpl();
	}
	
	public Integer getTotalCountTrafficInfoList(OBDtoADCObject object, OBDtoSearch searchOption, Integer accountIndex, String accountRole) throws Exception 
	{
		return faultMonitoringSvc.getSvcPerfInfoTotalCount(object, searchOption, accountIndex, accountRole);
	}
	
	public ArrayList<FaultSvcPerfInfoDto> getSvcPerfInfoList (OBDtoADCObject object, OBDtoSearch searchOption, OBDtoOrdering orderOption, Integer accountIndex, String accountRole) throws Exception
	{
		ArrayList<FaultSvcPerfInfoDto> SvcPerfInfoList = new ArrayList<FaultSvcPerfInfoDto>();
		ArrayList<OBDtoFaultSvcPerfInfo> SvcPerfInfoSvcList =
				faultMonitoringSvc.getSvcPerfInfoList(object, searchOption, orderOption, accountIndex, accountRole);	
		log.debug("{}", SvcPerfInfoSvcList);
		if (!CollectionUtils.isEmpty(SvcPerfInfoSvcList)) 
		{
			for (OBDtoFaultSvcPerfInfo SvcPerfSvc : SvcPerfInfoSvcList) 
			{
				SvcPerfInfoList.add(getSvcPerfInfo(SvcPerfSvc));
			}
		}		
		return SvcPerfInfoList;		
	}
	
	public Integer getTotalCountMemberInfo(OBDtoADCObject object, OBDtoSearch searchOption) throws Exception 
	{
		return faultMonitoringSvc.getSvcPerfVSrvMemberTotalCount(object, searchOption);
	}
	
	public VsTrafficInfoDto getVsMemberInfo(OBDtoADCObject object, OBDtoSearch searchOption, OBDtoOrdering orderOption) throws Exception
//	public VsTrafficInfoDto getVsMemberInfo(OBDtoADCObject object, OBDtoSearch searchOption, OBDtoOrdering orderOption, String vsIndex, Integer svrPort) throws Exception 
	{
		VsTrafficInfoDto vsTrafficInfoDto = null;
		
		OBDtoTrafficMapVServiceMembers trafficMapVsMembers = null;
		
		// alteon
		trafficMapVsMembers = faultMonitoringSvc.getSvcPerfVSrvMemberInfo(object, searchOption, orderOption);
		
		// exceptinAlteon
		//trafficMapVsMembers = faultMonitoringSvc.getSvcPerfVSrvMemberInfo(object, searchOption, orderOption);
		
//		if (object.getDesciption().equals("F5"))
//		{
//			trafficMapVsMembers = monitoringSvc.getTrafficMapsVServiceDetailF5(object.getIndex(), vsIndex);
//		}
//		else if (object.getDesciption().equals("PAS"))
//		{
//			trafficMapVsMembers = monitoringSvc.getTrafficMapsVServiceDetailPAS(object.getIndex(), vsIndex);
//		}
//		else if (object.getDesciption().equals("PASK"))
//		{
//			trafficMapVsMembers = monitoringSvc.getTrafficMapsVServiceDetailPASK(object.getIndex(), vsIndex);
//		}
//		else if (object.getDesciption().equals("Alteon"))
//		{
//			trafficMapVsMembers = monitoringSvc.getTrafficMapsVServiceDetailAlteon(object.getIndex(), vsIndex, svrPort);
//		}
//		else
//		{
//			return vsTrafficInfoDto;
//		}		
		
		log.debug("{}", trafficMapVsMembers);
		if (null != trafficMapVsMembers) 
		{
			vsTrafficInfoDto = getVsTrafficInfo(trafficMapVsMembers);
			List<OBDtoTrafficMapMember> trafficMapMemberList = trafficMapVsMembers.getMemberList();
			if (!CollectionUtils.isEmpty(trafficMapMemberList)) 
			{
				List<VsMemberTrafficInfoDto> vsMemberTrafficInfoList = new ArrayList<VsMemberTrafficInfoDto>();
				for (OBDtoTrafficMapMember trafficMapMember : trafficMapMemberList) 
				{
					vsMemberTrafficInfoList.add(getVsMemberTrafficInfo(trafficMapMember));
				}
				vsTrafficInfoDto.setVsMemberTrafficInfoList(vsMemberTrafficInfoList);
			}
		}
		
		return vsTrafficInfoDto;
	}
	
	// 멤버 성능 chart data
	public OBDtoFaultPreBpsConnChart getVsMemberHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws Exception
	{
		return faultMonitoringSvc.getSvcPerfVSrvMemberHistory(object, searchOption);		
	}
	
	// 멤버 성능 avg/max data
	public OBDtoFaultPreBpsConnChart getVsMemberMaxAvgHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws Exception
    {
//        return faultMonitoringSvc.getSvcPerfVSrvMemberMaxAvgHistory(object, searchOption);    
        
        OBDtoFaultPreBpsConnChart memberBpsConnMaxAvgData = faultMonitoringSvc.getSvcPerfVSrvMemberMaxAvgHistory(object, searchOption); 
        
        memberBpsConnMaxAvgData.setCurrBpsInUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getCurrBpsIn(), ""));
        memberBpsConnMaxAvgData.setCurrBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getCurrBpsOut(), ""));
        memberBpsConnMaxAvgData.setCurrBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getCurrBpsTot(), ""));
        memberBpsConnMaxAvgData.setCurrConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getCurrConnCurr(), ""));
        memberBpsConnMaxAvgData.setCurrRespTimeUnit(NumberUtil.toStringWithIntUnit(memberBpsConnMaxAvgData.getCurrRespTime(), ""));
        
        memberBpsConnMaxAvgData.setAvgBpsInUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getAvgBpsIn(), ""));
        memberBpsConnMaxAvgData.setAvgBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getAvgBpsOut(), ""));
        memberBpsConnMaxAvgData.setAvgBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getAvgBpsTot(), ""));
        memberBpsConnMaxAvgData.setAvgConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getAvgConnCurr(), ""));
        memberBpsConnMaxAvgData.setAvgRespTimeUnit(NumberUtil.toStringWithIntUnit(memberBpsConnMaxAvgData.getAvgRespTime(), ""));
        
        memberBpsConnMaxAvgData.setMaxBpsInUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getMaxBpsIn(), ""));
        memberBpsConnMaxAvgData.setMaxBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getMaxBpsOut(), ""));
        memberBpsConnMaxAvgData.setMaxBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getMaxBpsTot(), ""));
        memberBpsConnMaxAvgData.setMaxConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getMaxConnCurr(), ""));
        memberBpsConnMaxAvgData.setMaxRespTimeUnit(NumberUtil.toStringWithIntUnit(memberBpsConnMaxAvgData.getMaxRespTime(), ""));
        
        memberBpsConnMaxAvgData.setPreCurrBpsInUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getPreCurrBpsIn(), ""));
        memberBpsConnMaxAvgData.setPreCurrBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getPreCurrBpsOut(), ""));
        memberBpsConnMaxAvgData.setPreCurrBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getPreCurrBpsTot(), ""));
        memberBpsConnMaxAvgData.setPreCurrConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getPreCurrConnCurr(), ""));
        memberBpsConnMaxAvgData.setPreCurrRespTimeUnit(NumberUtil.toStringWithIntUnit(memberBpsConnMaxAvgData.getPreCurrRespTime(), ""));
        
        memberBpsConnMaxAvgData.setPreAvgBpsInUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getPreAvgBpsIn(), ""));
        memberBpsConnMaxAvgData.setPreAvgBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getPreAvgBpsOut(), ""));
        memberBpsConnMaxAvgData.setPreAvgBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getPreAvgBpsTot(), ""));
        memberBpsConnMaxAvgData.setPreAvgConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getPreAvgConnCurr(), ""));
        memberBpsConnMaxAvgData.setPreAvgRespTimeUnit(NumberUtil.toStringWithIntUnit(memberBpsConnMaxAvgData.getPreAvgRespTime(), ""));
        
        memberBpsConnMaxAvgData.setPreMaxBpsInUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getPreMaxBpsIn(), ""));
        memberBpsConnMaxAvgData.setPreMaxBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getPreMaxBpsOut(), ""));
        memberBpsConnMaxAvgData.setPreMaxBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getPreMaxBpsTot(), ""));
        memberBpsConnMaxAvgData.setPreMaxConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getPreMaxConnCurr(), ""));
        memberBpsConnMaxAvgData.setPreMaxRespTimeUnit(NumberUtil.toStringWithIntUnit(memberBpsConnMaxAvgData.getPreMaxRespTime(), ""));
        
        memberBpsConnMaxAvgData.setSubtractionCurrBpsInUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getSubtractionCurrBpsIn(), ""));
        memberBpsConnMaxAvgData.setSubtractionCurrBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getSubtractionCurrBpsOut(), ""));
        memberBpsConnMaxAvgData.setSubtractionCurrBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getSubtractionCurrBpsTot(), ""));
        memberBpsConnMaxAvgData.setSubtractionCurrConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getSubtractionCurrConnCurr(), ""));
        memberBpsConnMaxAvgData.setSubtractionCurrRespTimeUnit(NumberUtil.toStringWithIntUnit(memberBpsConnMaxAvgData.getSubtractionCurrRespTime(), ""));
        
        memberBpsConnMaxAvgData.setSubtractionAvgBpsInUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getSubtractionAvgBpsIn(), ""));
        memberBpsConnMaxAvgData.setSubtractionAvgBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getSubtractionAvgBpsOut(), ""));
        memberBpsConnMaxAvgData.setSubtractionAvgBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getSubtractionAvgBpsTot(), ""));
        memberBpsConnMaxAvgData.setSubtractionAvgConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getSubtractionAvgConnCurr(), ""));
        memberBpsConnMaxAvgData.setSubtractionAvgRespTimeUnit(NumberUtil.toStringWithIntUnit(memberBpsConnMaxAvgData.getSubtractionAvgRespTime(), ""));
        
        memberBpsConnMaxAvgData.setSubtractionMaxBpsInUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getSubtractionMaxBpsIn(), ""));
        memberBpsConnMaxAvgData.setSubtractionMaxBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getSubtractionMaxBpsOut(), ""));
        memberBpsConnMaxAvgData.setSubtractionMaxBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getSubtractionMaxBpsTot(), ""));
        memberBpsConnMaxAvgData.setSubtractionMaxConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(memberBpsConnMaxAvgData.getSubtractionMaxConnCurr(), ""));
        memberBpsConnMaxAvgData.setSubtractionMaxRespTimeUnit(NumberUtil.toStringWithIntUnit(memberBpsConnMaxAvgData.getSubtractionMaxRespTime(), ""));
        
        return memberBpsConnMaxAvgData;
    }
	
	private VsTrafficInfoDto getVsTrafficInfo(OBDtoTrafficMapVServiceMembers trafficMapVsMembers) 
	{
		VsTrafficInfoDto vsTrafficInfo = new VsTrafficInfoDto();
		
		vsTrafficInfo.setOccurredTime(trafficMapVsMembers.getOccurTime());
		vsTrafficInfo.setName(trafficMapVsMembers.getVsName());
		vsTrafficInfo.setIpAddress(trafficMapVsMembers.getVsIPAddress());
		vsTrafficInfo.setVsIndex(trafficMapVsMembers.getVsIndex());
		vsTrafficInfo.setPort(trafficMapVsMembers.getSrvPort());
		vsTrafficInfo.setStatus(trafficMapVsMembers.getStatus());
		
		if(trafficMapVsMembers.getBpsIn()==null || trafficMapVsMembers.getBpsIn()==-1)
			vsTrafficInfo.setInBps("-");
		else
			vsTrafficInfo.setInBps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getBpsIn(), ""));

		if(trafficMapVsMembers.getBpsOut()==null || trafficMapVsMembers.getBpsOut()==-1)
			vsTrafficInfo.setOutBps("-");
		else
			vsTrafficInfo.setOutBps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getBpsOut(), ""));

		if(trafficMapVsMembers.getBpsTot()==null || trafficMapVsMembers.getBpsTot()<0)
			vsTrafficInfo.setTotalBps("-");
		else
			vsTrafficInfo.setTotalBps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getBpsTot(), ""));

		if(trafficMapVsMembers.getPpsIn()==null || trafficMapVsMembers.getPpsIn()==-1)
			vsTrafficInfo.setInPps("-");
		else
			vsTrafficInfo.setInPps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getPpsIn(), ""));
		
		if(trafficMapVsMembers.getPpsOut()==null || trafficMapVsMembers.getPpsOut()==-1)
			vsTrafficInfo.setOutPps("-");
		else
			vsTrafficInfo.setOutPps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getPpsOut(), ""));
		
		if(trafficMapVsMembers.getPpsTot()==null || trafficMapVsMembers.getPpsTot()<0)
			vsTrafficInfo.setTotalPps("-");
		else
			vsTrafficInfo.setTotalPps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getPpsTot(), ""));
		
		if(trafficMapVsMembers.getCurConns()==null || trafficMapVsMembers.getCurConns()==-1)
			vsTrafficInfo.setActiveConnections("-");
		else
			vsTrafficInfo.setActiveConnections(NumberUtil.toStringWithUnit(trafficMapVsMembers.getCurConns(), ""));
		
		if(trafficMapVsMembers.getMaxConns()==null || trafficMapVsMembers.getMaxConns()==-1)
			vsTrafficInfo.setMaxConnections("-");
		else
			vsTrafficInfo.setMaxConnections(NumberUtil.toStringWithUnit(trafficMapVsMembers.getMaxConns(), ""));
		
		if(trafficMapVsMembers.getTotConns()==null || trafficMapVsMembers.getTotConns()==-1)
			vsTrafficInfo.setTotalConnections("-");
		else
			vsTrafficInfo.setTotalConnections(NumberUtil.toStringWithUnit(trafficMapVsMembers.getTotConns(), ""));
		
		return vsTrafficInfo;
	}
	
	private VsMemberTrafficInfoDto getVsMemberTrafficInfo(OBDtoTrafficMapMember trafficMapVsMembers)
	{
		VsMemberTrafficInfoDto vsTrafficInfo = new VsMemberTrafficInfoDto();

		vsTrafficInfo.setIndex(trafficMapVsMembers.getIndex());
		vsTrafficInfo.setOccurredTime(trafficMapVsMembers.getOccurTime());
		vsTrafficInfo.setIpAddress(trafficMapVsMembers.getIpAddress());
		vsTrafficInfo.setPort(trafficMapVsMembers.getPort());
		vsTrafficInfo.setAddPort(trafficMapVsMembers.getAddPort());
		vsTrafficInfo.setStatus(trafficMapVsMembers.getStatus());
		
		if(trafficMapVsMembers.getBpsIn()==null || trafficMapVsMembers.getBpsIn()==-1)
			vsTrafficInfo.setInBps("-");
		else
			vsTrafficInfo.setInBps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getBpsIn(), ""));

		if(trafficMapVsMembers.getBpsOut()==null || trafficMapVsMembers.getBpsOut()==-1)
			vsTrafficInfo.setOutBps("-");
		else
			vsTrafficInfo.setOutBps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getBpsOut(), ""));

		if(trafficMapVsMembers.getBpsTot()==null || trafficMapVsMembers.getBpsTot()<0)
			vsTrafficInfo.setTotalBps("-");
		else
			vsTrafficInfo.setTotalBps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getBpsTot(), ""));

		if(trafficMapVsMembers.getPpsIn()==null || trafficMapVsMembers.getPpsIn()==-1)
			vsTrafficInfo.setInPps("-");
		else
			vsTrafficInfo.setInPps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getPpsIn(), ""));
		
		if(trafficMapVsMembers.getPpsOut()==null || trafficMapVsMembers.getPpsOut()==-1)
			vsTrafficInfo.setOutPps("-");
		else
			vsTrafficInfo.setOutPps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getPpsOut(), ""));

		if(trafficMapVsMembers.getPpsTot()==null || trafficMapVsMembers.getPpsTot()<0)
			vsTrafficInfo.setTotalPps("-");
		else
			vsTrafficInfo.setTotalPps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getPpsTot(), ""));
		
		if(trafficMapVsMembers.getCurConns()==null || trafficMapVsMembers.getCurConns()==-1)
			vsTrafficInfo.setActiveConnections("-");
		else
			vsTrafficInfo.setActiveConnections(NumberUtil.toStringWithUnit(trafficMapVsMembers.getCurConns(), ""));
		
		if(trafficMapVsMembers.getMaxConns()==null || trafficMapVsMembers.getMaxConns()==-1)
			vsTrafficInfo.setMaxConnections("-");
		else
			vsTrafficInfo.setMaxConnections(NumberUtil.toStringWithUnit(trafficMapVsMembers.getMaxConns(), ""));
		
		if(trafficMapVsMembers.getTotConns()==null || trafficMapVsMembers.getTotConns()<0)
			vsTrafficInfo.setTotalConnections("-");
		else
			vsTrafficInfo.setTotalConnections(NumberUtil.toStringWithUnit(trafficMapVsMembers.getTotConns(), ""));
		
		return vsTrafficInfo;
	}
	
	// 서비스 성능 Chart data
	public OBDtoFaultPreBpsConnChart getBpsConnHistory(OBDtoADCObject object, String vsIndex, Integer svcPort, OBDtoSearch searchOption) throws Exception
	{
		return faultMonitoringSvc.getBpsConnHistory(object, vsIndex, svcPort, searchOption);		
	}
	
	// 서비스 성능 avg/max data
	public OBDtoFaultPreBpsConnChart getBpsConnMaxAvgHistory(OBDtoADCObject object, String vsIndex, Integer svcPort, OBDtoSearch searchOption) throws Exception
    {
	    OBDtoFaultPreBpsConnChart bpsConnMaxAvgData = faultMonitoringSvc.getBpsConnMaxAvgHistory(object, vsIndex, svcPort, searchOption);
	    
	    bpsConnMaxAvgData.setCurrBpsInUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getCurrBpsIn(), ""));
	    bpsConnMaxAvgData.setCurrBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getCurrBpsOut(), ""));
	    bpsConnMaxAvgData.setCurrBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getCurrBpsTot(), ""));
	    bpsConnMaxAvgData.setCurrConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getCurrConnCurr(), ""));
	    bpsConnMaxAvgData.setCurrRespTimeUnit(NumberUtil.toStringWithIntUnit(bpsConnMaxAvgData.getCurrRespTime(), ""));
	    
	    bpsConnMaxAvgData.setAvgBpsInUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getAvgBpsIn(), ""));
	    bpsConnMaxAvgData.setAvgBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getAvgBpsOut(), ""));
	    bpsConnMaxAvgData.setAvgBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getAvgBpsTot(), ""));
	    bpsConnMaxAvgData.setAvgConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getAvgConnCurr(), ""));
	    bpsConnMaxAvgData.setAvgRespTimeUnit(NumberUtil.toStringWithIntUnit(bpsConnMaxAvgData.getAvgRespTime(), ""));
	    
	    bpsConnMaxAvgData.setMaxBpsInUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getMaxBpsIn(), ""));
        bpsConnMaxAvgData.setMaxBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getMaxBpsOut(), ""));
        bpsConnMaxAvgData.setMaxBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getMaxBpsTot(), ""));
        bpsConnMaxAvgData.setMaxConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getMaxConnCurr(), ""));
        bpsConnMaxAvgData.setMaxRespTimeUnit(NumberUtil.toStringWithIntUnit(bpsConnMaxAvgData.getMaxRespTime(), ""));
        
        bpsConnMaxAvgData.setPreCurrBpsInUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getPreCurrBpsIn(), ""));
        bpsConnMaxAvgData.setPreCurrBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getPreCurrBpsOut(), ""));
        bpsConnMaxAvgData.setPreCurrBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getPreCurrBpsTot(), ""));
        bpsConnMaxAvgData.setPreCurrConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getPreCurrConnCurr(), ""));
        bpsConnMaxAvgData.setPreCurrRespTimeUnit(NumberUtil.toStringWithIntUnit(bpsConnMaxAvgData.getPreCurrRespTime(), ""));
        
        bpsConnMaxAvgData.setPreAvgBpsInUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getPreAvgBpsIn(), ""));
        bpsConnMaxAvgData.setPreAvgBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getPreAvgBpsOut(), ""));
        bpsConnMaxAvgData.setPreAvgBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getPreAvgBpsTot(), ""));
        bpsConnMaxAvgData.setPreAvgConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getPreAvgConnCurr(), ""));
        bpsConnMaxAvgData.setPreAvgRespTimeUnit(NumberUtil.toStringWithIntUnit(bpsConnMaxAvgData.getPreAvgRespTime(), ""));
        
        bpsConnMaxAvgData.setPreMaxBpsInUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getPreMaxBpsIn(), ""));
        bpsConnMaxAvgData.setPreMaxBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getPreMaxBpsOut(), ""));
        bpsConnMaxAvgData.setPreMaxBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getPreMaxBpsTot(), ""));
        bpsConnMaxAvgData.setPreMaxConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getPreMaxConnCurr(), ""));
        bpsConnMaxAvgData.setPreMaxRespTimeUnit(NumberUtil.toStringWithIntUnit(bpsConnMaxAvgData.getPreMaxRespTime(), ""));
        
        bpsConnMaxAvgData.setSubtractionCurrBpsInUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getSubtractionCurrBpsIn(), ""));
        bpsConnMaxAvgData.setSubtractionCurrBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getSubtractionCurrBpsOut(), ""));
        bpsConnMaxAvgData.setSubtractionCurrBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getSubtractionCurrBpsTot(), ""));
        bpsConnMaxAvgData.setSubtractionCurrConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getSubtractionCurrConnCurr(), ""));
        bpsConnMaxAvgData.setSubtractionCurrRespTimeUnit(NumberUtil.toStringWithIntUnit(bpsConnMaxAvgData.getSubtractionCurrRespTime(), ""));
        
        bpsConnMaxAvgData.setSubtractionAvgBpsInUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getSubtractionAvgBpsIn(), ""));
        bpsConnMaxAvgData.setSubtractionAvgBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getSubtractionAvgBpsOut(), ""));
        bpsConnMaxAvgData.setSubtractionAvgBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getSubtractionAvgBpsTot(), ""));
        bpsConnMaxAvgData.setSubtractionAvgConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getSubtractionAvgConnCurr(), ""));
        bpsConnMaxAvgData.setSubtractionAvgRespTimeUnit(NumberUtil.toStringWithIntUnit(bpsConnMaxAvgData.getSubtractionAvgRespTime(), ""));
        
        bpsConnMaxAvgData.setSubtractionMaxBpsInUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getSubtractionMaxBpsIn(), ""));
        bpsConnMaxAvgData.setSubtractionMaxBpsOutUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getSubtractionMaxBpsOut(), ""));
        bpsConnMaxAvgData.setSubtractionMaxBpsTotUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getSubtractionMaxBpsTot(), ""));
        bpsConnMaxAvgData.setSubtractionMaxConnCurrUnit(NumberUtil.toStringWithDataUnitSvc(bpsConnMaxAvgData.getSubtractionMaxConnCurr(), ""));
        bpsConnMaxAvgData.setSubtractionMaxRespTimeUnit(NumberUtil.toStringWithIntUnit(bpsConnMaxAvgData.getSubtractionMaxRespTime(), ""));
	    
        return bpsConnMaxAvgData;        
    }
	
	// 서비스 성능 응답시간 avg/max data
    public OBDtoFaultPreBpsConnChart getResponseTimeMaxAvgHistory(OBDtoADCObject object, String vsIndex, Integer svcPort, OBDtoSearch searchOption) throws Exception
    {
        return faultMonitoringSvc.getResponseTimeMaxAvgHistory(object, vsIndex, svcPort, searchOption);        
    }
	
    public OBDtoFaultPreBpsConnChart getResponseTimeHistory(OBDtoADCObject object, String vsIndex, Integer svcPort, OBDtoSearch searchOption) throws Exception
	{
		return faultMonitoringSvc.getResponseTimeHistory(object, vsIndex, svcPort, searchOption);		
	}
	
	public OBDtoFaultBpsConnInfo getRealTimeBpsConnInfo(OBDtoADCObject object, String vsIndex, Integer svcPort, OBDtoFaultBpsConnInfo prevInfo) throws Exception
	{		
		return faultMonitoringSvc.getRealTimeBpsConnInfo(object, vsIndex, svcPort, prevInfo);		
	}
	
	private FaultSvcPerfInfoDto getSvcPerfInfo(OBDtoFaultSvcPerfInfo SvcPerfSvc) 
	{
		FaultSvcPerfInfoDto SvcPerfInfo = new FaultSvcPerfInfoDto();
		
		SvcPerfInfo.setAdcObj(SvcPerfSvc.getAdcObj());
		SvcPerfInfo.setVsIndex(SvcPerfSvc.getVsIndex());
		SvcPerfInfo.setVsvcIndex(SvcPerfSvc.getVsvcIndex());
		SvcPerfInfo.setVsStatus(SvcPerfSvc.getVsStatus());
		SvcPerfInfo.setVsIP(SvcPerfSvc.getVsIP());
		SvcPerfInfo.setVsPort(SvcPerfSvc.getVsPort());
		SvcPerfInfo.setVsName(SvcPerfSvc.getVsName());
		
		if(SvcPerfSvc.getResponseTime()==null || SvcPerfSvc.getResponseTime()==-1)
		{
			SvcPerfInfo.setResponseTime("-");
		}
		else if(SvcPerfSvc.getResponseTime()==0)
		{
			SvcPerfInfo.setResponseTime("<1ms");
		}
		else
		{
			SvcPerfInfo.setResponseTime(NumberUtil.toStringWithIntUnit(SvcPerfSvc.getResponseTime(), ""));
		}
		
		if(SvcPerfSvc.getBpsIn()==null || SvcPerfSvc.getBpsIn()==-1)
			SvcPerfInfo.setBpsIn("-");
		else
			SvcPerfInfo.setBpsIn(NumberUtil.toStringWithUnit(SvcPerfSvc.getBpsIn(), ""));
		
		if(SvcPerfSvc.getBpsOut()==null || SvcPerfSvc.getBpsOut()==-1)
			SvcPerfInfo.setBpsOut("-");
		else
			SvcPerfInfo.setBpsOut(NumberUtil.toStringWithUnit(SvcPerfSvc.getBpsOut(), ""));
		
		if(SvcPerfSvc.getBpsTotal()==null || SvcPerfSvc.getBpsTotal()==-1)
			SvcPerfInfo.setBpsTotal("-");
		else
			SvcPerfInfo.setBpsTotal(NumberUtil.toStringWithUnit(SvcPerfSvc.getBpsTotal(), ""));
		
		if(SvcPerfSvc.getPpsIn()==null || SvcPerfSvc.getPpsIn()==-1)
			SvcPerfInfo.setPpsIn("-");
		else
			SvcPerfInfo.setPpsIn(NumberUtil.toStringWithUnit(SvcPerfSvc.getPpsIn(), ""));
		
		if(SvcPerfSvc.getPpsOut()==null || SvcPerfSvc.getPpsOut()==-1)
			SvcPerfInfo.setPpsOut("-");
		else
			SvcPerfInfo.setPpsOut(NumberUtil.toStringWithUnit(SvcPerfSvc.getPpsOut(), ""));
		
		if(SvcPerfSvc.getPpsTotal()==null || SvcPerfSvc.getPpsTotal()==-1)
			SvcPerfInfo.setPpsTotal("-");
		else
			SvcPerfInfo.setPpsTotal(NumberUtil.toStringWithUnit(SvcPerfSvc.getPpsTotal(), ""));
		
		if(SvcPerfSvc.getConnCurr()==null || SvcPerfSvc.getConnCurr()==-1)
			SvcPerfInfo.setConnCurr("-");
		else
			SvcPerfInfo.setConnCurr(NumberUtil.toStringWithUnit(SvcPerfSvc.getConnCurr(), ""));
		
		if(SvcPerfSvc.getConnMax()==null || SvcPerfSvc.getConnMax()==-1)
			SvcPerfInfo.setConnMax("-");
		else
			SvcPerfInfo.setConnMax(NumberUtil.toStringWithUnit(SvcPerfSvc.getConnMax(), ""));
		
		if(SvcPerfSvc.getConnTotal()==null || SvcPerfSvc.getConnTotal()==-1)
			SvcPerfInfo.setConnTotal("-");
		else
			SvcPerfInfo.setConnTotal(NumberUtil.toStringWithUnit(SvcPerfSvc.getConnTotal(), ""));	
		
		return SvcPerfInfo;
	}
}

