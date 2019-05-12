package kr.openbase.adcsmart.web.facade.adcman;

import java.util.ArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBMonitoringFlb;
import kr.openbase.adcsmart.service.dto.OBDtoInterfaceSummary;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.flb.OBDtoFlbFilterInfo;
import kr.openbase.adcsmart.service.impl.OBMonitoringFlbAlteonImpl;
import kr.openbase.adcsmart.web.facade.dto.FlbFilterInfoDto;

@Component
public class FlbInfoFacade
{
	private static transient Logger log = LoggerFactory.getLogger(FlbInfoFacade.class);

	private OBMonitoringFlb flbMonSvc;

	public FlbInfoFacade()
	{
		flbMonSvc = new OBMonitoringFlbAlteonImpl();
	}
	// physical port Get
	
	public ArrayList<OBDtoInterfaceSummary> getPhysicalPortForFilter(int adcIndex) throws Exception
	{
		return flbMonSvc.getPhysicalPortForFilter(adcIndex);
	}	
	
	// Filter Count Get
	public Integer getFilterCount(int adcIndex, OBDtoSearch condition) throws Exception
	{
		return flbMonSvc.getFilterCount(adcIndex, condition);
	}
	
	// Filter List Get
	public ArrayList<FlbFilterInfoDto> getFilterInfo(int adcIndex, OBDtoSearch condition, OBDtoOrdering orderOption) throws Exception
	{
		ArrayList<FlbFilterInfoDto> FilterInfoList = new ArrayList<FlbFilterInfoDto>();
		ArrayList<OBDtoFlbFilterInfo> FilterInfoListSvc =
				flbMonSvc.getFilterInfo(adcIndex, condition, orderOption);
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
		FilterInfo.setPhysicalPortList(FilterInfo.getPhysicalPortList());
		
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
}
