package kr.openbase.adcsmart.web.facade.monitoring;

import java.util.ArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBFaultMonitoring;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoTargetObject;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultGroupMemberPerfInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultGroupPerfInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultRealInfo;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMonitoringImpl;
import kr.openbase.adcsmart.web.facade.dto.FaultGroupMemberPerfInfoDto;
import kr.openbase.adcsmart.web.facade.dto.FaultGroupPerfInfoDto;
import kr.openbase.adcsmart.web.facade.dto.FaultRealInfoDto;
import kr.openbase.adcsmart.web.util.NumberUtil;

@Component
public class FlbGroupPerformanceMonitorFacade
{
	private static transient Logger log = LoggerFactory.getLogger(FlbGroupPerformanceMonitorFacade.class);

	private OBFaultMonitoring faultMonitoringSvc;
	
	public FlbGroupPerformanceMonitorFacade()
	{
		faultMonitoringSvc = new OBFaultMonitoringImpl();
	}
	// FLB 그룹 Total Count Get
	public Integer getGroupCount(OBDtoTargetObject object, OBDtoSearch searchOption) throws Exception
	{
		return faultMonitoringSvc.getGroupCount(object, searchOption);
	}
	// FLB 그룹 List Get
	public ArrayList<FaultGroupPerfInfoDto> getGroupPerformanceList(OBDtoTargetObject object, OBDtoSearch searchOption, OBDtoOrdering orderOption) throws Exception
	{
		ArrayList<FaultGroupPerfInfoDto> GroupPerfInfoList = new ArrayList<FaultGroupPerfInfoDto>();
		ArrayList<OBDtoFaultGroupPerfInfo> GroupPerfInfoListSvc =
				faultMonitoringSvc.getGroupPerformanceList(object, searchOption, orderOption);
		log.debug("{}", GroupPerfInfoListSvc);
		if (!CollectionUtils.isEmpty(GroupPerfInfoListSvc)) 
		{
			for (OBDtoFaultGroupPerfInfo GroupPerfSvc : GroupPerfInfoListSvc) 
			{
				GroupPerfInfoList.add(convertGroupPerfInfo(GroupPerfSvc));
			}
		}		
		return GroupPerfInfoList;
	}	
	// FLB 그룹 List Convert
	private FaultGroupPerfInfoDto convertGroupPerfInfo(OBDtoFaultGroupPerfInfo GroupPerfSvc)
	{
		FaultGroupPerfInfoDto groupPerfInfo = new FaultGroupPerfInfoDto();		
		groupPerfInfo.setObject(GroupPerfSvc.getObject());
		groupPerfInfo.setGroupDbIndex(GroupPerfSvc.getGroupDbIndex());
		groupPerfInfo.setGroupId(GroupPerfSvc.getGroupId());
		groupPerfInfo.setGroupName(GroupPerfSvc.getGroupName());
		groupPerfInfo.setMemberCount(GroupPerfSvc.getMemberCount());
		groupPerfInfo.setGroupStatus(GroupPerfSvc.getGroupStatus());		
		
		if(GroupPerfSvc.getBpsIn()==null || GroupPerfSvc.getBpsIn() < 0)
			groupPerfInfo.setBpsIn("-");
		else
			groupPerfInfo.setBpsIn(NumberUtil.toStringWithUnit(GroupPerfSvc.getBpsIn(), ""));
		
		if(GroupPerfSvc.getBpsOut()==null || GroupPerfSvc.getBpsOut() < 0)
			groupPerfInfo.setBpsOut("-");
		else
			groupPerfInfo.setBpsOut(NumberUtil.toStringWithUnit(GroupPerfSvc.getBpsOut(), ""));
		
		if(GroupPerfSvc.getBpsTotal()==null || GroupPerfSvc.getBpsTotal() < 0)
			groupPerfInfo.setBpsTotal("-");
		else
			groupPerfInfo.setBpsTotal(NumberUtil.toStringWithUnit(GroupPerfSvc.getBpsTotal(), ""));
		
		if(GroupPerfSvc.getConnCurr()==null || GroupPerfSvc.getConnCurr() < 0)
			groupPerfInfo.setConnCurr("-");
		else
			groupPerfInfo.setConnCurr(NumberUtil.toStringWithUnit(GroupPerfSvc.getConnCurr(), ""));
		
		return groupPerfInfo;		
	}
	// FLB 그룹 History Data Get (Chart)
	public ArrayList<OBDtoFaultBpsConnInfo> getGroupBpsConnHistory(String groupIndex, OBDtoSearch searchOption) throws Exception
	{
		return faultMonitoringSvc.getGroupBpsConnHistory(groupIndex, searchOption);
	}
	
	// FLB Member Total Count Get
	public Integer getGroupMemberCount(String groupIndex) throws Exception
	{
		return faultMonitoringSvc.getGroupMemberCount(groupIndex);
	}
	// FLB Member List Get
	public FaultGroupMemberPerfInfoDto getGroupMemberPerformanceList(String groupIndex, OBDtoSearch searchOption, OBDtoOrdering orderOption) throws Exception
	{
		FaultGroupMemberPerfInfoDto GroupMemberInfo = new FaultGroupMemberPerfInfoDto();
		ArrayList<FaultRealInfoDto> GroupMemberListInfo = new ArrayList<FaultRealInfoDto>();

		OBDtoFaultGroupMemberPerfInfo GroupMemberPerfInfoListSvc =
				faultMonitoringSvc.getGroupMemberPerformanceList(groupIndex, searchOption, orderOption);
		log.debug("{}", GroupMemberPerfInfoListSvc);		
		OBDtoFaultGroupPerfInfo GroupInfoSvc = GroupMemberPerfInfoListSvc.getGroupInfo();
		FaultGroupPerfInfoDto GroupInfo = convertGroupPerfInfo(GroupInfoSvc);
		GroupMemberInfo.setGroupInfo(GroupInfo);		
		if (!CollectionUtils.isEmpty(GroupMemberPerfInfoListSvc.getMemberList())) 
		{
			for (OBDtoFaultRealInfo GroupPerfSvc : GroupMemberPerfInfoListSvc.getMemberList()) 
			{
				GroupMemberListInfo.add(convertGroupMemberPerfInfo(GroupPerfSvc));
			}
			GroupMemberInfo.setMemberList(GroupMemberListInfo);			
		}		
		return GroupMemberInfo;
	}
	// FLB Member List Convert
	private FaultRealInfoDto convertGroupMemberPerfInfo(OBDtoFaultRealInfo GroupMemberPerfSvc)
	{
		FaultRealInfoDto groupMemberPerfInfo = new FaultRealInfoDto();		
		groupMemberPerfInfo.setDbIndex(GroupMemberPerfSvc.getDbIndex());
		groupMemberPerfInfo.setId(GroupMemberPerfSvc.getId());
		groupMemberPerfInfo.setName(GroupMemberPerfSvc.getName());
		groupMemberPerfInfo.setIpAddress(GroupMemberPerfSvc.getIpAddress());
		groupMemberPerfInfo.setStatus(GroupMemberPerfSvc.getStatus());
				
		if(GroupMemberPerfSvc.getBpsIn()==null || GroupMemberPerfSvc.getBpsIn() < 0)
			groupMemberPerfInfo.setBpsIn("-");
		else
			groupMemberPerfInfo.setBpsIn(NumberUtil.toStringWithUnit(GroupMemberPerfSvc.getBpsIn(), ""));
		
		if(GroupMemberPerfSvc.getBpsOut()==null || GroupMemberPerfSvc.getBpsOut() < 0)
			groupMemberPerfInfo.setBpsOut("-");
		else
			groupMemberPerfInfo.setBpsOut(NumberUtil.toStringWithUnit(GroupMemberPerfSvc.getBpsOut(), ""));
		
		if(GroupMemberPerfSvc.getBpsTotal()==null || GroupMemberPerfSvc.getBpsTotal() < 0)
			groupMemberPerfInfo.setBpsTotal("-");
		else
			groupMemberPerfInfo.setBpsTotal(NumberUtil.toStringWithUnit(GroupMemberPerfSvc.getBpsTotal(), ""));
		
		if(GroupMemberPerfSvc.getConnCurr()==null || GroupMemberPerfSvc.getConnCurr() < 0)
			groupMemberPerfInfo.setConnCurr("-");
		else
			groupMemberPerfInfo.setConnCurr(NumberUtil.toStringWithUnit(GroupMemberPerfSvc.getConnCurr(), ""));
		
		return groupMemberPerfInfo;		
	}
	// FLB Member History Data Get (Chart)
	public ArrayList<OBDtoFaultBpsConnInfo> getGroupMemberBpsConnHistory(String groupIndex, OBDtoSearch searchOption) throws Exception
	{
		return faultMonitoringSvc.getGroupMemberBpsConnHistory(groupIndex, searchOption);
	}
	
}
