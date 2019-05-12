package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.utility.OBDefine;

public class VirtualSvrPASKDto extends VirtualSvrDto
{
	private String 	serviceIpView;
	private String  servicePortView;
	private boolean serviceConfigurable;
	
	private Integer servicePort;
	private Integer serviceProtocol;
	private String serviceSubInfo;
	private AdcPoolDto pool;
	
	public static VirtualSvrPASKDto toVirtualSvrDto(OBDtoAdcVServerPASK vsFromSvc)
	{
		VirtualSvrPASKDto virtualSvr = new VirtualSvrPASKDto();
		if(vsFromSvc.getDbIndex()!=null)
			virtualSvr.setIndex(vsFromSvc.getDbIndex());
		if(vsFromSvc.getAdcIndex()!=null)
			virtualSvr.setAdcIndex(vsFromSvc.getAdcIndex());
		if(vsFromSvc.getName()!=null)
			virtualSvr.setName(vsFromSvc.getName());
		if(vsFromSvc.getvIP()!=null)
			virtualSvr.setVirtualIp(vsFromSvc.getvIP());
		if(vsFromSvc.getvIPView()!=null)
			virtualSvr.setServiceIpView(vsFromSvc.getvIPView());
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
		if(vsFromSvc.getSrvPortView()!=null)
			virtualSvr.setServicePortView(vsFromSvc.getSrvPortView());
		if(vsFromSvc.getSrvProtocol()!=null)
		{
			virtualSvr.setServiceProtocol(vsFromSvc.getSrvProtocol());
		}
		if(vsFromSvc.getSubInfo()!=null)
		{
			virtualSvr.setServiceSubInfo(vsFromSvc.getSubInfo());
		}
		
			virtualSvr.setServiceConfigurable(vsFromSvc.isConfigurable());
		
		/*virtualSvr.setPool(AdcPoolDto.toAdcPoolDto(vsFromSvc.getPool()));*/
		/*virtualSvr.setPool(AdcPoolDto.toAdcPoolDto(vsFromSvc.getPool()));*/
		
		return virtualSvr;
	}
	
	public static List<VirtualSvrPASKDto> toVirtualSvrPASKDto(List<OBDtoAdcVServerPASK> vssFromSvc)
	{
		List<VirtualSvrPASKDto> vs = new ArrayList<VirtualSvrPASKDto>();
		for (OBDtoAdcVServerPASK e : vssFromSvc)
			vs.add(toVirtualSvrDto(e));
		
		return vs;
	}
	

	public String getServiceSubInfo()
	{
		return serviceSubInfo;
	}

	public void setServiceSubInfo(String serviceSubInfo)
	{
		this.serviceSubInfo = serviceSubInfo;
	}

	public String getServiceIpView()
	{
		return serviceIpView;
	}

	public void setServiceIpView(String serviceIpView)
	{
		this.serviceIpView = serviceIpView;
	}

	public String getServicePortView()
	{
		return servicePortView;
	}

	public void setServicePortView(String servicePortView)
	{
		this.servicePortView = servicePortView;
	}

	public boolean isServiceConfigurable()
	{
		return serviceConfigurable;
	}

	public void setServiceConfigurable(boolean serviceConfigurable)
	{
		this.serviceConfigurable = serviceConfigurable;
	}

	public Integer getServicePort() 
	{
		return servicePort;
	}

	public void setServicePort(Integer servicePort) {
		this.servicePort = servicePort;
	}

	public Integer getServiceProtocol() {
		return serviceProtocol;
	}

	public void setServiceProtocol(Integer serviceProtocol) {
		this.serviceProtocol = serviceProtocol;
	}

	public AdcPoolDto getPool() {
		return pool;
	}

	public void setPool(AdcPoolDto pool) {
		this.pool = pool;
	}

	@Override
	public String toString()
	{
		return "VirtualSvrPASKDto [serviceIpView=" + serviceIpView + ", servicePortView=" + servicePortView + ", serviceConfigurable=" + serviceConfigurable + ", servicePort=" + servicePort + ", serviceProtocol=" + serviceProtocol + ", serviceSubInfo=" + serviceSubInfo + ", pool=" + pool + "]";
	}
}
