package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.utility.OBDefine;

public class VirtualSvrPASDto extends VirtualSvrDto
{
	private Integer servicePort;
	private Integer serviceProtocol;
	private AdcPoolDto pool;
	
	public static VirtualSvrPASDto toVirtualSvrDto(OBDtoAdcVServerPAS vsFromSvc)
	{
		VirtualSvrPASDto virtualSvr = new VirtualSvrPASDto();
		if(vsFromSvc.getDbIndex()!=null)
			virtualSvr.setIndex(vsFromSvc.getDbIndex());
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
	
		if(vsFromSvc.getState()!=null)
		{
			switch (vsFromSvc.getState())
			{
				case OBDefine.STATE_DISABLE:
					virtualSvr.setState("disable");	break;
				case OBDefine.STATE_ENABLE:
					virtualSvr.setState("enable");	break;
			}
		}
		if(vsFromSvc.getPool()!=null)
			virtualSvr.setPool(AdcPoolDto.toAdcPoolDto(vsFromSvc.getPool()));
		if(vsFromSvc.getSrvPort()!=null)
			virtualSvr.setServicePort(vsFromSvc.getSrvPort());
		if(vsFromSvc.getSrvProtocol()!=null)
			virtualSvr.setServiceProtocol(vsFromSvc.getSrvProtocol());
		
		/*virtualSvr.setPool(AdcPoolDto.toAdcPoolDto(vsFromSvc.getPool()));*/
		/*virtualSvr.setPool(AdcPoolDto.toAdcPoolDto(vsFromSvc.getPool()));*/
		
		return virtualSvr;
	}
	
	public static List<VirtualSvrPASDto> toVirtualSvrPasDto(List<OBDtoAdcVServerPAS> vssFromSvc)
	{
		List<VirtualSvrPASDto> vs = new ArrayList<VirtualSvrPASDto>();
		for (OBDtoAdcVServerPAS e : vssFromSvc)
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
	public Integer getServiceProtocol()
	{
		return serviceProtocol;
	}

	public void setServiceProtocol(Integer serviceProtocol)
	{
		this.serviceProtocol = serviceProtocol;
	}

	@Override
	public String toString()
	{
		return "VirtualSvrPASDto [servicePort=" + servicePort
				+ ", serviceProtocol=" + serviceProtocol + ", pool=" + pool
				+ "]";
	}	
}