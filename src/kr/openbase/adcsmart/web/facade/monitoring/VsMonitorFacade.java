/**
 * 
 */
package kr.openbase.adcsmart.web.facade.monitoring;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBMonitoring;
import kr.openbase.adcsmart.service.dto.OBDtoConnectionData;
import kr.openbase.adcsmart.service.dto.OBDtoConnectionInfo;
import kr.openbase.adcsmart.service.dto.OBDtoTrafficMapMember;
import kr.openbase.adcsmart.service.dto.OBDtoTrafficMapVServiceMembers;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultGroupMemberPerfInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultRealInfo;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbFilterInfo;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;
import kr.openbase.adcsmart.service.impl.OBMonitoringImpl;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.FlbFilterInfoDto;
import kr.openbase.adcsmart.web.facade.dto.VsConfigEventDto;
import kr.openbase.adcsmart.web.facade.dto.VsConnectionDataDto;
import kr.openbase.adcsmart.web.facade.dto.VsConnectionInfoDto;
import kr.openbase.adcsmart.web.facade.dto.VsMemberTrafficInfoDto;
import kr.openbase.adcsmart.web.facade.dto.VsTrafficInfoDto;
import kr.openbase.adcsmart.web.util.NumberUtil;

/**
 * @author paul
 *
 */
@Component
public class VsMonitorFacade 
{

	private static transient Logger log = LoggerFactory.getLogger(VsMonitorFacade.class);
	
	private OBMonitoring monitoringSvc;
	
	public VsMonitorFacade() 
	{
		monitoringSvc = new OBMonitoringImpl();
	}
	
//	public Integer getTotalCountTrafficInfoList(AdcDto adc, String searchKey) throws Exception 
//	{
//		return monitoringSvc.getTrafficMapsVServerTotalRecordCount(adc.getIndex(), searchKey);
//	}
	
//	public List<VsTrafficInfoDto> getVsTrafficInfoList(AdcDto adc, String searchKey, Integer startIndex, Integer endIndex, Integer orderType, Integer orderDir) throws Exception
//	{
//		List<VsTrafficInfoDto> vsTrafficInfoList = new ArrayList<VsTrafficInfoDto>();
//		
//		if (adc.getType().equals("F5")) 
//		{
//			List<OBDtoTrafficMapVServer> trafficMapVsList =
//					monitoringSvc.getTrafficMapsVServerF5(adc.getIndex(), searchKey, startIndex, endIndex, orderType, orderDir);
//			log.debug("{}", trafficMapVsList);
//			if (!CollectionUtils.isEmpty(trafficMapVsList)) 
//			{
//				for (OBDtoTrafficMapVServer trafficMapVs : trafficMapVsList) 
//				{
//					vsTrafficInfoList.add(getVsTrafficInfo(trafficMapVs));
//				}
//			}
//		} 
//		else if (adc.getType().equals("PAS")) 
//		{
//			List<OBDtoTrafficMapVServer> trafficMapVsList = monitoringSvc.getTrafficMapsVServerPAS(adc.getIndex(), searchKey, startIndex, endIndex, orderType, orderDir);
//			log.debug("{}", trafficMapVsList);
//			if (!CollectionUtils.isEmpty(trafficMapVsList)) 
//			{
//				for (OBDtoTrafficMapVServer trafficMapVs : trafficMapVsList) 
//				{
//					vsTrafficInfoList.add(getVsTrafficInfo(trafficMapVs));
//				}
//			}
//		} 
//		else if (adc.getType().equals("PASK")) 
//		{
//			List<OBDtoTrafficMapVServer> trafficMapVsList = monitoringSvc.getTrafficMapsVServerPASK(adc.getIndex(), searchKey, startIndex, endIndex, orderType, orderDir);
//			log.debug("{}", trafficMapVsList);
//			if (!CollectionUtils.isEmpty(trafficMapVsList)) 
//			{
//				for (OBDtoTrafficMapVServer trafficMapVs : trafficMapVsList) 
//				{
//					vsTrafficInfoList.add(getVsTrafficInfo(trafficMapVs));
//				}
//			}
//		} 		
//		else if (adc.getType().equals("Alteon"))
//		{
//			List<OBDtoTrafficMapVService> trafficMapVsList = monitoringSvc.getTrafficMapsVServiceAlteon(adc.getIndex(), searchKey, startIndex, endIndex, orderType, orderDir);
//			log.debug("{}", trafficMapVsList);
//			if (!CollectionUtils.isEmpty(trafficMapVsList))
//			{
//				for (OBDtoTrafficMapVService trafficMapVs : trafficMapVsList)
//				{
//					vsTrafficInfoList.add(getVsTrafficInfo(trafficMapVs));
//				}
//			}
//		}
//		
//		return vsTrafficInfoList;
//	}
//	public List<VsTrafficInfoDto> getVsTrafficInfoList(AdcDto adc, String searchKey, Integer startIndex, Integer endIndex) throws Exception
//	{
//		List<VsTrafficInfoDto> vsTrafficInfoList = new ArrayList<VsTrafficInfoDto>();
//		
//		if (adc.getType().equals("F5")) 
//		{
//			List<OBDtoTrafficMapVServer> trafficMapVsList =
//					monitoringSvc.getTrafficMapsVServerF5(adc.getIndex(), searchKey, startIndex, endIndex);
//			log.debug("{}", trafficMapVsList);
//			if (!CollectionUtils.isEmpty(trafficMapVsList)) 
//			{
//				for (OBDtoTrafficMapVServer trafficMapVs : trafficMapVsList) 
//				{
//					vsTrafficInfoList.add(getVsTrafficInfo(trafficMapVs));
//				}
//			}
//		} 
//		else if (adc.getType().equals("PAS")) 
//		{
//			List<OBDtoTrafficMapVServer> trafficMapVsList = monitoringSvc.getTrafficMapsVServerPAS(adc.getIndex(), searchKey, startIndex, endIndex);
//			log.debug("{}", trafficMapVsList);
//			if (!CollectionUtils.isEmpty(trafficMapVsList)) 
//			{
//				for (OBDtoTrafficMapVServer trafficMapVs : trafficMapVsList) 
//				{
//					vsTrafficInfoList.add(getVsTrafficInfo(trafficMapVs));
//				}
//			}
//		} 
//		else if (adc.getType().equals("PASK")) 
//		{
//			List<OBDtoTrafficMapVServer> trafficMapVsList = monitoringSvc.getTrafficMapsVServerPASK(adc.getIndex(), searchKey, startIndex, endIndex);
//			log.debug("{}", trafficMapVsList);
//			if (!CollectionUtils.isEmpty(trafficMapVsList)) 
//			{
//				for (OBDtoTrafficMapVServer trafficMapVs : trafficMapVsList) 
//				{
//					vsTrafficInfoList.add(getVsTrafficInfo(trafficMapVs));
//				}
//			}
//		} 		
//		else if (adc.getType().equals("Alteon"))
//		{
//			List<OBDtoTrafficMapVService> trafficMapVsList = monitoringSvc.getTrafficMapsVServiceAlteon(adc.getIndex(), searchKey, startIndex, endIndex);
//			log.debug("{}", trafficMapVsList);
//			if (!CollectionUtils.isEmpty(trafficMapVsList))
//			{
//				for (OBDtoTrafficMapVService trafficMapVs : trafficMapVsList)
//				{
//					vsTrafficInfoList.add(getVsTrafficInfo(trafficMapVs));
//				}
//			}
//		}
//		
//		return vsTrafficInfoList;
//	}
	public VsTrafficInfoDto getVsTrafficInfo(AdcDto adc, String vsIndex, Integer port) throws Exception 
	{
		VsTrafficInfoDto vsTrafficInfoDto = null;
		
		OBDtoTrafficMapVServiceMembers trafficMapVsMembers = null;
		if(adc.getType().equals("F5")) 
		{
			trafficMapVsMembers = monitoringSvc.getTrafficMapsVServiceDetailF5(adc.getIndex(), vsIndex);
		} 
		else if(adc.getType().equals("PAS")) 
		{
			trafficMapVsMembers = monitoringSvc.getTrafficMapsVServiceDetailPAS(adc.getIndex(), vsIndex);
		} 
		else if(adc.getType().equals("PASK")) 
		{
			trafficMapVsMembers = monitoringSvc.getTrafficMapsVServiceDetailPASK(adc.getIndex(), vsIndex);
		} 
		else if(adc.getType().equals("Alteon"))
		{
			trafficMapVsMembers = monitoringSvc.getTrafficMapsVServiceDetailAlteon(adc.getIndex(), vsIndex, port);
		}
		else
		{
			return vsTrafficInfoDto;
		}
		
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

	public VsTrafficInfoDto getVsTrafficMemberInfo(AdcDto adc, String groupIndex) throws Exception 
	{
		VsTrafficInfoDto vsTrafficInfoDto = null;
		OBDtoFaultGroupMemberPerfInfo trafficMapVsMembers = null;
		trafficMapVsMembers = monitoringSvc.getTrafficMapsFlbGroupDetailAlteon(adc.getIndex(), groupIndex);
		
		log.debug("{}", trafficMapVsMembers);
		if (null != trafficMapVsMembers) 
		{
			vsTrafficInfoDto = getVsTrafficMemberInfo(trafficMapVsMembers);
			ArrayList<OBDtoFaultRealInfo> trafficMapMemberList = trafficMapVsMembers.getMemberList();
			if (!CollectionUtils.isEmpty(trafficMapMemberList)) 
			{
				ArrayList<VsMemberTrafficInfoDto> vsMemberTrafficInfoList = new ArrayList<VsMemberTrafficInfoDto>();
				for (OBDtoFaultRealInfo trafficMapMember : trafficMapMemberList) 
				{
					vsMemberTrafficInfoList.add(getVsMemberTrafficRealInfo(trafficMapMember));
				}
				vsTrafficInfoDto.setVsMemberTrafficInfoList(vsMemberTrafficInfoList);
			}	
		}
		
		return vsTrafficInfoDto;
	}
	
	//TODO
	public ArrayList<FlbFilterInfoDto> getVsTrafficFilterInfo(AdcDto adc, String groupIndex) throws Exception 
	{
		ArrayList<FlbFilterInfoDto> FilterInfoList = new ArrayList<FlbFilterInfoDto>();
		ArrayList<OBDtoFlbFilterInfo> FilterInfoListSvc =
				 monitoringSvc.getTrafficMapsFlbGroupFilterDetailAlteon(adc.getIndex(), groupIndex);
		log.debug("{}", FilterInfoListSvc);
		if (!CollectionUtils.isEmpty(FilterInfoListSvc)) 
		{
			for (OBDtoFlbFilterInfo FilterInfoSvc : FilterInfoListSvc) 
			{
				FilterInfoList.add(convertFilterInfoSvc(FilterInfoSvc));
			}
		}
		return FilterInfoList;		
	}
	
	private FlbFilterInfoDto convertFilterInfoSvc(OBDtoFlbFilterInfo FilterInfoSvc)
	{
		FlbFilterInfoDto FilterInfo = new FlbFilterInfoDto();
		FilterInfo.setAdcIndex(FilterInfoSvc.getAdcIndex());
		FilterInfo.setDbIndex(FilterInfoSvc.getDbIndex());
		FilterInfo.setFilterId(FilterInfoSvc.getFilterId());
		FilterInfo.setState(FilterInfoSvc.getState());
		FilterInfo.setName(FilterInfoSvc.getName());
		FilterInfo.setSrcIP(FilterInfoSvc.getSrcIP());
		FilterInfo.setSrcMask(FilterInfoSvc.getSrcMask());
		FilterInfo.setDstIP(FilterInfoSvc.getDstIP());
		FilterInfo.setDstMask(FilterInfoSvc.getDstMask());
		FilterInfo.setProtocol(getProtocolType(FilterInfoSvc.getProtocol()));
		FilterInfo.setSrcPortFrom(FilterInfoSvc.getSrcPortFrom());
		FilterInfo.setSrcPortTo(FilterInfoSvc.getSrcPortTo());
		FilterInfo.setDstPortFrom(FilterInfoSvc.getDstPortFrom());
		FilterInfo.setDstPortTo(FilterInfoSvc.getDstPortTo());
		FilterInfo.setAction(FilterInfoSvc.getAction());
		FilterInfo.setGroup(FilterInfoSvc.getGroup());
		FilterInfo.setRedirection(FilterInfoSvc.getRedirection());
		FilterInfo.setPhysicalPortList(FilterInfoSvc.getPhysicalPortList());
		
		return FilterInfo;
	}
	
	private String getProtocolType(Integer protocolSvc)
	{
		if(protocolSvc==null) //null이 올 수 없지만 만약을 대비
		{			
			return "NONE";
		}
		String result = "";
		switch (protocolSvc)
		{
			case 0:
				result = "any"; break;
			case 1:
				result ="icmp";	 break;
			case 6:
				result ="tcp";	 break;
			case 17:
				result ="udp"; break;			
			default:
				result ="-";
		}
		return result;
	}
	
	public VsConnectionInfoDto getVsConnectionInfo(AdcDto adc, String vsIndex, Integer port, Date startTime, Date endTime) throws Exception 
	{
		VsConnectionInfoDto vsConnectionInfo = null;
		
		OBDtoConnectionInfo connectionInfo = null;
		if (adc.getType().equals("F5")) 
		{
			connectionInfo = monitoringSvc.getVSConnections(adc.getIndex(), vsIndex, null, startTime, endTime);
		} 
		else if(adc.getType().equals("PAS")) 
		{
			connectionInfo = monitoringSvc.getVSConnections(adc.getIndex(), vsIndex, null, startTime, endTime);
		} 
		else if(adc.getType().equals("PASK")) 
		{
			connectionInfo = monitoringSvc.getVSConnections(adc.getIndex(), vsIndex, null, startTime, endTime);
		} 
		else if(adc.getType().equals("Alteon"))
		{
			connectionInfo = monitoringSvc.getVSConnections(adc.getIndex(), vsIndex, port, startTime, endTime);
		}
		else
		{
			return null;
		}
		
		log.debug("{}", connectionInfo);
		
		if (null != connectionInfo)
		{
			vsConnectionInfo = getVsConnectionInfo(connectionInfo);
		}
		
		return vsConnectionInfo;
	}
	
	public VsConnectionDataDto getRealTimeVsConnectionData(AdcDto adc, String vsIndex, Integer port) throws Exception 
	{
		VsConnectionDataDto vsConnectionData = null;
		
		OBDtoConnectionData connectionData = null;
		if(adc.getType().equals("F5")) 
		{
			connectionData = monitoringSvc.getVSRealTimeCurrConns(adc.getIndex(), vsIndex, null);
		}
		else if(adc.getType().equals("PAS"))
		{
			connectionData = monitoringSvc.getVSRealTimeCurrConns(adc.getIndex(), vsIndex, null);
		}
		else if(adc.getType().equals("PASK"))
		{
			connectionData = monitoringSvc.getVSRealTimeCurrConns(adc.getIndex(), vsIndex, null);
		}
		else if(adc.getType().equals("Alteon"))
		{
			connectionData = monitoringSvc.getVSRealTimeCurrConns(adc.getIndex(), vsIndex, port);
		}
		else
		{
			return null;
		}
		
		log.debug("{}", connectionData);
		if (null != connectionData)
		{
			vsConnectionData = getVsConnectionData(connectionData);
		}
		
		return vsConnectionData;
	}


	private VsTrafficInfoDto getVsTrafficMemberInfo(OBDtoFaultGroupMemberPerfInfo trafficMapVs) 
	{
		VsTrafficInfoDto vsTrafficInfo = new VsTrafficInfoDto();
		vsTrafficInfo.setIpAddress(trafficMapVs.getGroupInfo().getGroupId());
		vsTrafficInfo.setName(trafficMapVs.getGroupInfo().getGroupName());
		vsTrafficInfo.setPort(trafficMapVs.getGroupInfo().getMemberCount());

		if(trafficMapVs.getGroupInfo().getBpsIn()==null || trafficMapVs.getGroupInfo().getBpsIn()==-1)
			vsTrafficInfo.setInBps("-");
		else
			vsTrafficInfo.setInBps(NumberUtil.toStringWithUnit(trafficMapVs.getGroupInfo().getBpsIn(), ""));

		if(trafficMapVs.getGroupInfo().getBpsOut()==null || trafficMapVs.getGroupInfo().getBpsOut()==-1)
			vsTrafficInfo.setOutBps("-");
		else
			vsTrafficInfo.setOutBps(NumberUtil.toStringWithUnit(trafficMapVs.getGroupInfo().getBpsOut(), ""));

		if(trafficMapVs.getGroupInfo().getBpsTotal()==null || trafficMapVs.getGroupInfo().getBpsTotal()<0)
			vsTrafficInfo.setTotalBps("-");
		else
			vsTrafficInfo.setTotalBps(NumberUtil.toStringWithUnit(trafficMapVs.getGroupInfo().getBpsTotal(), ""));
		
		vsTrafficInfo.setInPps("-");
		vsTrafficInfo.setOutPps("-");
		vsTrafficInfo.setTotalPps("-");

		
		if(trafficMapVs.getGroupInfo().getConnCurr()==null ||trafficMapVs.getGroupInfo().getConnCurr()==-1)
			vsTrafficInfo.setActiveConnections("-");
		else
			vsTrafficInfo.setActiveConnections(NumberUtil.toStringWithUnit(trafficMapVs.getGroupInfo().getConnCurr(), ""));
		
		vsTrafficInfo.setMaxConnections("-");
		vsTrafficInfo.setTotalConnections("-");

		
		return vsTrafficInfo;
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
	
	private VsMemberTrafficInfoDto getVsMemberTrafficRealInfo(OBDtoFaultRealInfo trafficMapVsMembers)
	{
		VsMemberTrafficInfoDto vsTrafficInfo = new VsMemberTrafficInfoDto();
		
		vsTrafficInfo.setIpAddress(trafficMapVsMembers.getIpAddress());
		vsTrafficInfo.setStatus(trafficMapVsMembers.getStatus());
		
		if(trafficMapVsMembers.getBpsIn()==null || trafficMapVsMembers.getBpsIn()==-1)
			vsTrafficInfo.setInBps("-");
		else
			vsTrafficInfo.setInBps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getBpsIn(), ""));

		if(trafficMapVsMembers.getBpsOut()==null || trafficMapVsMembers.getBpsOut()==-1)
			vsTrafficInfo.setOutBps("-");
		else
			vsTrafficInfo.setOutBps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getBpsOut(), ""));

		if(trafficMapVsMembers.getBpsTotal()==null || trafficMapVsMembers.getBpsTotal()<0)
			vsTrafficInfo.setTotalBps("-");
		else
			vsTrafficInfo.setTotalBps(NumberUtil.toStringWithUnit(trafficMapVsMembers.getBpsTotal(), ""));

		vsTrafficInfo.setInPps("-");
		vsTrafficInfo.setOutPps("-");
		vsTrafficInfo.setTotalPps("-");
		
		if(trafficMapVsMembers.getConnCurr()==null || trafficMapVsMembers.getConnCurr()==-1)
			vsTrafficInfo.setActiveConnections("-");
		else
			vsTrafficInfo.setActiveConnections(NumberUtil.toStringWithUnit(trafficMapVsMembers.getConnCurr(), ""));
		
		vsTrafficInfo.setMaxConnections("-");
		vsTrafficInfo.setTotalConnections("-");

		
		return vsTrafficInfo;
	}
	
	private VsConnectionInfoDto getVsConnectionInfo(OBDtoConnectionInfo connectionInfo) throws Exception
	{
		VsConnectionInfoDto vsConnectionInfo = new VsConnectionInfoDto();
		
		vsConnectionInfo.setMaxDate(connectionInfo.getMaxDate());
		vsConnectionInfo.setMinDate(connectionInfo.getMinDate());
		vsConnectionInfo.setMaxConnections(NumberUtil.toStringWithUnit(connectionInfo.getMaxConns(), ""));
		vsConnectionInfo.setMinConnections(NumberUtil.toStringWithUnit(connectionInfo.getMinConns(), ""));
		vsConnectionInfo.setAvgConnections(NumberUtil.toStringWithUnit(connectionInfo.getAvgConns(), ""));
		
		if (!CollectionUtils.isEmpty(connectionInfo.getData())) 
		{
			for (OBDtoConnectionData connectionData : connectionInfo.getData())
			{
				vsConnectionInfo.getVsConnectionDataList().add(getVsConnectionData(connectionData));
			}
		}
		if (!CollectionUtils.isEmpty(connectionInfo.getConfEventList()))
		{
			for (OBDtoAdcConfigHistory adcConfigHistory : connectionInfo.getConfEventList())
			{
				vsConnectionInfo.getVsConfigEventList().add(VsConfigEventDto.getVsConfigEvent(adcConfigHistory));
			}
		}
		return vsConnectionInfo;
	}
	
	private VsConnectionDataDto getVsConnectionData(OBDtoConnectionData connectionData) throws Exception
	{
		VsConnectionDataDto vsConnectionData = new VsConnectionDataDto();
		
		vsConnectionData.setOccurredTime(connectionData.getOccurTime());
		vsConnectionData.setMaxConnections(connectionData.getMaxConns());
		vsConnectionData.setActiveConnections(connectionData.getCurConns());
		vsConnectionData.setTotalConnections(connectionData.getTotConns());
		
		return vsConnectionData;
	}
	
}
