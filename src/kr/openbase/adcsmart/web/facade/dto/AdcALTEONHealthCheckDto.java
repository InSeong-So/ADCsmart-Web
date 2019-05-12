package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcHealthCheckAlteon;

public class AdcALTEONHealthCheckDto
{
	private String 	dbIndex;
	private String	id;
	private String  name;
	private String  type; //healthcheck type
	private String  destinationIp; //v29에서 type = logexp일때는 expression
	private String	extra;

	public static AdcALTEONHealthCheckDto toAdcALTEONHealthCheckDto(OBDtoAdcHealthCheckAlteon healthFromSvc)
	{
		if (healthFromSvc == null)
		{
			return null;
		}
		AdcALTEONHealthCheckDto health = new AdcALTEONHealthCheckDto();
		health.setDbIndex(healthFromSvc.getDbIndex());
		health.setId(healthFromSvc.getId());
		health.setName(healthFromSvc.getName());
		health.setType(healthFromSvc.getType());
		health.setDestinationIp(healthFromSvc.getDestinationIp());
		health.setExtra(healthFromSvc.getExtra());
		return health;
	}
	
	public static List<AdcALTEONHealthCheckDto> toAdcALTEONHealthCheckDto(List<OBDtoAdcHealthCheckAlteon> healthsFromSvc)
	{
		ArrayList<AdcALTEONHealthCheckDto> healths = new ArrayList<AdcALTEONHealthCheckDto>();
		for (OBDtoAdcHealthCheckAlteon e : healthsFromSvc)
		{
			healths.add(toAdcALTEONHealthCheckDto(e));
		}
		return healths;
	}
	
	public static OBDtoAdcHealthCheckAlteon toOBDtoAdcHealthCheckALTEON(AdcALTEONHealthCheckDto health)
	{
		if (health == null) //사실상 health가 null인 경우는 없고, 선택 안 하면 NONE(지정안함)인데, 그러면 null로 줘서 없음을 표시한다.
		{											  // PASK 는 type 이 아닌 id 를 보내기 때문에, 지정안함을 id = null 로 처리한다.
			return null;
		}
		OBDtoAdcHealthCheckAlteon healthFromSvc = new OBDtoAdcHealthCheckAlteon();
		healthFromSvc.setDbIndex(health.getDbIndex());
		healthFromSvc.setId(health.getId());
		healthFromSvc.setName(health.getName());
		healthFromSvc.setType(health.getType());
		healthFromSvc.setDestinationIp(health.getDestinationIp());
		healthFromSvc.setExtra(health.getExtra());
		return healthFromSvc;		
	}

	public String getDbIndex()
	{
		return dbIndex;
	}

	public void setDbIndex(String dbIndex)
	{
		this.dbIndex = dbIndex;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getDestinationIp()
	{
		return destinationIp;
	}

	public void setDestinationIp(String destinationIp)
	{
		this.destinationIp = destinationIp;
	}

	public String getExtra()
	{
		return extra;
	}

	public void setExtra(String extra)
	{
		this.extra = extra;
	}

	@Override
	public String toString()
	{
		return "AdcALTEONHealthCheckDto [dbIndex=" + dbIndex + ", id=" + id
				+ ", name=" + name + ", type=" + type + ", destinationIp="
				+ destinationIp + ", extra=" + extra + "]";
	}		
}