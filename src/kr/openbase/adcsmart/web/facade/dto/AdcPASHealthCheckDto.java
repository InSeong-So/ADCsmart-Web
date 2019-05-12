package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcHealthCheckPAS;
import kr.openbase.adcsmart.service.utility.OBDefine;

public class AdcPASHealthCheckDto
{
	private String 	dbIndex;
	private Integer	id;
	private String type; //healthcheck type, icmp, tcp, http
	private Integer port;
	private Integer interval;
	private Integer timeout;
	private Integer state = OBDefine.STATE_ENABLE;
	
	public static AdcPASHealthCheckDto toAdcPASHealthCheckDto(OBDtoAdcHealthCheckPAS healthFromSvc)
	{
		if (healthFromSvc == null)
		{
			return null;
		}
		AdcPASHealthCheckDto health = new AdcPASHealthCheckDto();
		health.setDbIndex(healthFromSvc.getDbIndex());
		health.setId(healthFromSvc.getId());
		health.setInterval(healthFromSvc.getInterval());
		health.setPort(healthFromSvc.getPort());
		health.setState(healthFromSvc.getState());
		health.setTimeout(healthFromSvc.getTimeout());
		health.setType(health.getStringHelth(healthFromSvc.getType()));
		return health;
	}
	
	public static List<AdcPASHealthCheckDto> toAdcPASHealthCheckDto(List<OBDtoAdcHealthCheckPAS> healthsFromSvc)
	{
		ArrayList<AdcPASHealthCheckDto> healths = new ArrayList<AdcPASHealthCheckDto>();
		for (OBDtoAdcHealthCheckPAS e : healthsFromSvc)
		{
			healths.add(toAdcPASHealthCheckDto(e));
		}

		return healths;
	}
	
	public static OBDtoAdcHealthCheckPAS toOBDtoAdcHealthCheckPAS(AdcPASHealthCheckDto health)
	{
		if (health == null || health.getType().equals("NONE")) //사실상 health가 null인 경우는 없고, 선택 안 하면 NONE(지정안함)인데, 그러면 null로 줘서 없음을 표시한다.
		{
			return null;
		}
		OBDtoAdcHealthCheckPAS healthFromSvc = new OBDtoAdcHealthCheckPAS();
		healthFromSvc.setDbIndex(health.getDbIndex());
		healthFromSvc.setId(health.getId());
		healthFromSvc.setInterval(health.getInterval());
		healthFromSvc.setPort(health.getPort());
		healthFromSvc.setState(health.getState());
		healthFromSvc.setTimeout(health.getTimeout());
		healthFromSvc.setType(health.getIntegerHealth(health.getType()));
		
		return healthFromSvc;		
	}
	public Integer getIntegerHealth(String health)
	{
		if(health==null) //null이 올 수 없지만 만약을 대비
		{			
			return OBDefine.HEALTH_CHECK.NONE;
		}
		
		if (health.equals("NONE"))
			return OBDefine.HEALTH_CHECK.NONE;
		else if (health.equals("TCP"))
			return OBDefine.HEALTH_CHECK.TCP;
		else if (health.equals("HTTP"))
			return OBDefine.HEALTH_CHECK.HTTP;
		else if (health.equals("HTTPS"))
			return OBDefine.HEALTH_CHECK.HTTPS;
		else if (health.equals("UDP"))
			return OBDefine.HEALTH_CHECK.UDP;
		else if (health.equals("ICMP"))
			return OBDefine.HEALTH_CHECK.ICMP;
		else if (health.equals("GATEWAY_ICMP"))
			return OBDefine.HEALTH_CHECK.GATEWAY_ICMP;
		else if (health.equals("ARP"))
			return OBDefine.HEALTH_CHECK.ARP;
		else if (health.equals("LINK"))
			return OBDefine.HEALTH_CHECK.LINK;
		else if (health.equals("NOT_ALLOWED"))
			return OBDefine.COMMON_NOT_ALLOWED;
		else
			throw new TypeNotPresentException(health, null);
	}
	
	public String getStringHelth(Integer health)
	{
		if(health==null) //null이 올 수 없지만 만약을 대비
		{			
			return "";
		}
		String result = "";
		switch (health)
		{
			case OBDefine.HEALTH_CHECK.NONE:
				result = "NONE";	break;
			case OBDefine.HEALTH_CHECK.TCP:
				result ="TCP";	break;
			case OBDefine.HEALTH_CHECK.HTTP:
				result ="HTTP";	break;
			case OBDefine.HEALTH_CHECK.HTTPS:
				result ="HTTPS";	break;
			case OBDefine.HEALTH_CHECK.UDP:
				result ="UDP";	break;
			case OBDefine.HEALTH_CHECK.ICMP:
				result ="ICMP";	break;
			case OBDefine.HEALTH_CHECK.GATEWAY_ICMP:
				result ="GATEWAY_ICMP";	break;
			case OBDefine.HEALTH_CHECK.ARP:
				result ="ARP";	break;
			case OBDefine.HEALTH_CHECK.LINK:
				result ="LINK";	break;
			case OBDefine.COMMON_NOT_ALLOWED:
				result ="NOT_ALLOWED"; break;
			default:
				result ="";
		}
		return result;
	}
	
	@Override
	public String toString()
	{
		return "AdcPASHealthCheckDto [dbIndex=" + dbIndex + ", id=" + id
				+ ", type=" + type + ", port=" + port + ", interval="
				+ interval + ", timeout=" + timeout + ", state=" + state + "]";
	}
	public String getDbIndex() 
	{
		return dbIndex;
	}
	public void setDbIndex(String dbIndex) 
	{
		this.dbIndex = dbIndex;
	}
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public Integer getPort()
	{
		return port;
	}
	public void setPort(Integer port)
	{
		this.port = port;
	}
	public Integer getInterval()
	{
		return interval;
	}
	public void setInterval(Integer interval)
	{
		this.interval = interval;
	}
	public Integer getTimeout()
	{
		return timeout;
	}
	public void setTimeout(Integer timeout)
	{
		this.timeout = timeout;
	}
	public Integer getState()
	{
		return state;
	}
	public void setState(Integer state)
	{
		this.state = state;
	}
}