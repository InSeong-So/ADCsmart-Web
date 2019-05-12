package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.impl.f5.DtoVlanTunnelFilter;
import kr.openbase.adcsmart.service.utility.OBDefine;

public class VirtualSvrF5Dto extends VirtualSvrDto 
{
	private Integer servicePort;
	private AdcPoolDto pool;
	private String profileIndex;
	private DtoVlanTunnelFilter vlanFilter;
	
	public static VirtualSvrF5Dto toVirtualSvrDto(OBDtoAdcVServerF5 vsFromSvc) 
	{
		VirtualSvrF5Dto virtualSvr = new VirtualSvrF5Dto();
		if(vsFromSvc.getIndex()!=null)
			virtualSvr.setIndex(vsFromSvc.getIndex());
		if(vsFromSvc.getAdcIndex()!=null)
			virtualSvr.setAdcIndex(vsFromSvc.getAdcIndex());
		if(vsFromSvc.getName()!=null)
			virtualSvr.setName(vsFromSvc.getName());
		if(vsFromSvc.getvIP()!=null)
			virtualSvr.setVirtualIp(vsFromSvc.getvIP());
		if(vsFromSvc.getApplyTime()!=null)
			virtualSvr.setLastUpdateTime(vsFromSvc.getApplyTime());
		if(vsFromSvc.getStatus()!=null)
		{
			switch (vsFromSvc.getStatus()) 
			{
			case OBDefine.STATUS_BLOCK:
				virtualSvr.setStatus("block");	break;
			case OBDefine.STATUS_DISABLE:
				virtualSvr.setStatus("disable");	break;
			case OBDefine.STATUS_AVAILABLE:
				virtualSvr.setStatus("available");	break;
			case OBDefine.STATUS_UNAVAILABLE:
				virtualSvr.setStatus("unavailable");	break;
			default:
				virtualSvr.setStatus("unknown");
			}
		}
		
		if(vsFromSvc.getServicePort()!=null)
			virtualSvr.setServicePort(vsFromSvc.getServicePort());
		if(vsFromSvc.getPool()!=null)
			virtualSvr.setPool(AdcPoolDto.toAdcPoolDto(vsFromSvc.getPool()));
		if(vsFromSvc.getPersistence()!=null)
			virtualSvr.setProfileIndex(vsFromSvc.getPersistence());
		
		if(vsFromSvc.getVlanFilter()!=null)
			virtualSvr.setVlanFilter(vsFromSvc.getVlanFilter());
		return virtualSvr;
	}
	
	public static List<VirtualSvrF5Dto> toVirtualSvrF5Dto(List<OBDtoAdcVServerF5> vssFromSvc) 
	{
		List<VirtualSvrF5Dto> vs = new ArrayList<VirtualSvrF5Dto>();
		for (OBDtoAdcVServerF5 e : vssFromSvc)
			vs.add(toVirtualSvrDto(e));
		
		return vs;
	}

	public Integer getServicePort() 
	{
		return servicePort;
	}

	public void setServicePort(Integer servicePort) 
	{
		this.servicePort = servicePort;
	}

	public AdcPoolDto getPool() 
	{
		return pool;
	}

	public void setPool(AdcPoolDto pool) 
	{
		this.pool = pool;
	}

	public String getProfileIndex() 
	{
		return profileIndex;
	}

	public void setProfileIndex(String profileIndex) 
	{
		this.profileIndex = profileIndex;
	}

	public DtoVlanTunnelFilter getVlanFilter() 
	{
		return vlanFilter;
	}

	public void setVlanFilter(DtoVlanTunnelFilter vlanFilter) 
	{
		this.vlanFilter = vlanFilter;
	}

	@Override
	public String toString() 
	{
		return "VirtualSvrF5Dto [servicePort=" + servicePort + ", pool=" + pool
				+ ", profileIndex=" + profileIndex + ", vlanFilter="
				+ vlanFilter + "]";
	}	
}
