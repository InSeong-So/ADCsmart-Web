package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberF5;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolMemberPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolMemberPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;
import kr.openbase.adcsmart.service.utility.OBDefine;


public class AdcPoolDto
{
	private String index;
	private String name;
	private String loadBalancingType;
	private String healthCheckType;
	private AdcPASHealthCheckDto adcPASHealthCheck;
	private AdcPASKHealthCheckDto adcPASKHealthCheck;
	private AdcALTEONHealthCheckDto adcALTEONHealthCheck;

	private List<AdcPoolMemberDto> members;
	private List<AdcPoolMemberDto> eventMembers;

	// Alteon WEB set data
	private String alteonId;
	
	public static AdcPoolDto toAdcPoolDto(OBDtoAdcPoolAlteon poolFromSvc)
	{
		if (poolFromSvc == null)
			return null;
		
		AdcPoolDto pool = new AdcPoolDto();
		pool.setIndex(poolFromSvc.getIndex());
		pool.setName(poolFromSvc.getName());
		pool.setLoadBalancingType(poolFromSvc.getLbMethod());
		pool.setHealthCheckType(poolFromSvc.getHealthCheck());
		pool.setAdcALTEONHealthCheck(AdcALTEONHealthCheckDto.toAdcALTEONHealthCheckDto(poolFromSvc.getHealthCheckV2()));
		pool.setMembers(AdcPoolMemberDto.toAdcPoolMemberAlteonDto(poolFromSvc.getMemberList()));
		pool.setAlteonId(poolFromSvc.getAlteonId());
		return pool;
	}

	public static List<AdcPoolDto> toAdcPoolAlteonDto(List<OBDtoAdcPoolAlteon> poolsFromSvc)
	{
		ArrayList<AdcPoolDto> pools = new ArrayList<AdcPoolDto>();
		for (OBDtoAdcPoolAlteon e : poolsFromSvc)
			pools.add(toAdcPoolDto(e));

		return pools;
	}
	
	// Alteon SVC set data
	public static OBDtoAdcPoolAlteon toOBDtoAdcPoolAlteon(AdcPoolDto pool)
	{
		if (pool == null)
			return null;
		
		OBDtoAdcPoolAlteon poolFromSvc = new OBDtoAdcPoolAlteon();
		poolFromSvc.setIndex(pool.getIndex());
		poolFromSvc.setName(pool.getName());
		if (pool.getLoadBalancingType().equals("Round Robin"))
			poolFromSvc.setLbMethod(OBDefine.LB_METHOD_ROUND_ROBIN);
		else if (pool.getLoadBalancingType().equals("Least Connections"))
			poolFromSvc.setLbMethod(OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER);
		else if (pool.getLoadBalancingType().equals("Hash"))
			poolFromSvc.setLbMethod(OBDefine.LB_METHOD_HASH);
		else if (pool.getLoadBalancingType().equals("NOT_ALLOWED"))
			poolFromSvc.setLbMethod(OBDefine.COMMON_NOT_ALLOWED);
		else
			throw new TypeNotPresentException(pool.getLoadBalancingType(), null);
		
//		if (pool.getHealthCheckType().equals("NONE"))
//			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK_NONE);
//		else if (pool.getHealthCheckType().equals("TCP"))
//			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK_TCP);
//		else if (pool.getHealthCheckType().equals("HTTP"))
//			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK_HTTP);
//		else if (pool.getHealthCheckType().equals("HTTPS"))
//			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK_HTTPS);
//		else if (pool.getHealthCheckType().equals("UDP"))
//			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK_UDP);
//		else if (pool.getHealthCheckType().equals("ICMP"))
//			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK_ICMP);
//		else if (pool.getHealthCheckType().equals("GATEWAY_ICMP"))
//			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK_GATEWAY_ICMP);
//		else if (pool.getHealthCheckType().equals("ARP"))
//			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK_ARP);
//		else if (pool.getHealthCheckType().equals("LINK"))
//			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK_LINK);
//		else if (pool.getHealthCheckType().equals("NOT_ALLOWED"))
//			poolFromSvc.setHealthCheck(OBDefine.COMMON_NOT_ALLOWED);
//		else
//			throw new TypeNotPresentException(pool.getHealthCheckType(), null);
		/*poolFromSvc.setHealthCheckV2(pool.getAdcALTEONHealthCheck());*/
		poolFromSvc.setHealthCheckV2(AdcALTEONHealthCheckDto.toOBDtoAdcHealthCheckALTEON(pool.getAdcALTEONHealthCheck()));
		poolFromSvc.setMemberList(new ArrayList<OBDtoAdcPoolMemberAlteon>(AdcPoolMemberDto.toOBDtoAdcPoolMemberAlteon(pool.getMembers())));
		if (pool.getAlteonId() != null)
			poolFromSvc.setAlteonId(pool.getAlteonId());
		
		return poolFromSvc;
	}
	
	// F5 WEB set data
	public static AdcPoolDto toAdcPoolDto(OBDtoAdcPoolF5 poolFromSvc)
	{
		if (poolFromSvc == null)
			return null;
		
		AdcPoolDto pool = new AdcPoolDto();
		pool.setIndex(poolFromSvc.getIndex());
		pool.setName(poolFromSvc.getName());
		pool.setLoadBalancingType(poolFromSvc.getLbMethod());
		pool.setHealthCheckType(poolFromSvc.getHealthCheck());
		pool.setMembers(AdcPoolMemberDto.toAdcPoolMemberF5Dto(poolFromSvc.getMemberList()));
		pool.setEventMembers(AdcPoolMemberDto.toAdcPoolMemberF5Dto(poolFromSvc.getSelectMemberList()));
		return pool;
	}

	public static List<AdcPoolDto> toAdcPoolF5Dto(List<OBDtoAdcPoolF5> poolsFromSvc)
	{
		ArrayList<AdcPoolDto> pools = new ArrayList<AdcPoolDto>();
		for (OBDtoAdcPoolF5 e : poolsFromSvc)
			pools.add(toAdcPoolDto(e));

		return pools;
	}
	
	// F5 SVC set data
	public static OBDtoAdcPoolF5 toOBDtoAdcPoolF5(AdcPoolDto pool)
	{
		OBDtoAdcPoolF5 poolFromSvc = new OBDtoAdcPoolF5();
		poolFromSvc.setIndex(pool.getIndex());
		poolFromSvc.setName(pool.getName());
		if (pool.getLoadBalancingType().equals("Round Robin"))
			poolFromSvc.setLbMethod(OBDefine.LB_METHOD_ROUND_ROBIN);
		else if(pool.getLoadBalancingType().equals("Least Connections"))
			poolFromSvc.setLbMethod(OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER);
		else if (pool.getLoadBalancingType().equals("Hash"))
			poolFromSvc.setLbMethod(OBDefine.LB_METHOD_HASH);
		else if (pool.getLoadBalancingType().equals("NOT_ALLOWED"))
			poolFromSvc.setLbMethod(OBDefine.COMMON_NOT_ALLOWED);
		else
			throw new TypeNotPresentException(pool.getHealthCheckType(), null);
		
		if (pool.getHealthCheckType().equals("NONE"))
			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK.NONE);
		else if (pool.getHealthCheckType().equals("TCP"))
			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK.TCP);
		else if (pool.getHealthCheckType().equals("HTTP"))
			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK.HTTP);
		else if (pool.getHealthCheckType().equals("HTTPS"))
			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK.HTTPS);
		else if (pool.getHealthCheckType().equals("UDP"))
			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK.UDP);
		else if (pool.getHealthCheckType().equals("ICMP"))
			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK.ICMP);
		else if (pool.getHealthCheckType().equals("GATEWAY_ICMP"))
			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK.GATEWAY_ICMP);
		else if (pool.getHealthCheckType().equals("ARP"))
			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK.ARP);
		else if (pool.getHealthCheckType().equals("LINK"))
			poolFromSvc.setHealthCheck(OBDefine.HEALTH_CHECK.LINK);
		else if (pool.getHealthCheckType().equals("NOT_ALLOWED"))
			poolFromSvc.setHealthCheck(OBDefine.COMMON_NOT_ALLOWED);
		else
			throw new TypeNotPresentException(pool.getHealthCheckType(), null);
		
		poolFromSvc.setMemberList(new ArrayList<OBDtoAdcPoolMemberF5>(AdcPoolMemberDto.toOBDtoAdcPoolMemberF5(pool.getMembers())));
		poolFromSvc.setSelectMemberList(new ArrayList<OBDtoAdcPoolMemberF5>(AdcPoolMemberDto.toOBDtoAdcPoolMemberF5(pool.getEventMembers())));
		return poolFromSvc;
	}
	
	// PAS WEB set data
	public static AdcPoolDto toAdcPoolDto(OBDtoAdcPoolPAS poolFromSvc)
	{
		if (poolFromSvc == null)
			return null;
		
		AdcPoolDto pool = new AdcPoolDto();
		pool.setIndex(poolFromSvc.getDbIndex());
		pool.setName(poolFromSvc.getName());
		pool.setLoadBalancingType(poolFromSvc.getLbMethod());
		pool.setAdcPASHealthCheck(AdcPASHealthCheckDto.toAdcPASHealthCheckDto(poolFromSvc.getHealthCheckInfo()));
		pool.setMembers(AdcPoolMemberDto.toAdcPoolMemberPASDto(poolFromSvc.getMemberList()));
		return pool;
	}
	
	public static List<AdcPoolDto> toAdcPoolPASDto(List<OBDtoAdcPoolPAS> poolsFromSvc)
	{
		ArrayList<AdcPoolDto> pools = new ArrayList<AdcPoolDto>();
		for (OBDtoAdcPoolPAS e : poolsFromSvc)
			pools.add(toAdcPoolDto(e));

		return pools;
	}
	
	// PAS SVC set data
	public static OBDtoAdcPoolPAS toOBDtoAdcPoolPAS(AdcPoolDto pool)
	{
		
		OBDtoAdcPoolPAS poolFromSvc = new OBDtoAdcPoolPAS();
		poolFromSvc.setDbIndex(pool.getIndex());
		poolFromSvc.setName(pool.getName());
		
		poolFromSvc.setHealthCheckInfo(AdcPASHealthCheckDto.toOBDtoAdcHealthCheckPAS(pool.adcPASHealthCheck));
		if (pool.getLoadBalancingType().equals("Round Robin"))
			poolFromSvc.setLbMethod(OBDefine.LB_METHOD_ROUND_ROBIN);
		else if(pool.getLoadBalancingType().equals("Least Connections"))
			poolFromSvc.setLbMethod(OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER);
		else if (pool.getLoadBalancingType().equals("Hash"))
			poolFromSvc.setLbMethod(OBDefine.LB_METHOD_HASH);
		else if (pool.getLoadBalancingType().equals("NOT_ALLOWED"))
			poolFromSvc.setLbMethod(OBDefine.COMMON_NOT_ALLOWED);
		else
			throw new TypeNotPresentException(pool.getHealthCheckType(), null);
		
		
		poolFromSvc.setMemberList(new ArrayList<OBDtoAdcPoolMemberPAS>(AdcPoolMemberDto.toOBDtoAdcPoolMemberPAS(pool.getMembers())));
		return poolFromSvc;
	}
	
	// PASK WEB set data
		public static AdcPoolDto toAdcPoolDto(OBDtoAdcPoolPASK poolFromSvc)
		{
			if (poolFromSvc == null)
				return null;
			
			AdcPoolDto pool = new AdcPoolDto();
			pool.setIndex(poolFromSvc.getDbIndex());
			pool.setName(poolFromSvc.getName());
			pool.setLoadBalancingType(poolFromSvc.getLbMethod());
			pool.setAdcPASKHealthCheck(AdcPASKHealthCheckDto.toAdcPASKHealthCheckDto(poolFromSvc.getHealthCheckInfo()));
			pool.setMembers(AdcPoolMemberDto.toAdcPoolMemberPASKDto(poolFromSvc.getMemberList()));
			return pool;
		}
		
		public static List<AdcPoolDto> toAdcPoolPASKDto(List<OBDtoAdcPoolPASK> poolsFromSvc)
		{
			ArrayList<AdcPoolDto> pools = new ArrayList<AdcPoolDto>();
			for (OBDtoAdcPoolPASK e : poolsFromSvc)
				pools.add(toAdcPoolDto(e));

			return pools;
		}
		
	// PASK SVC set data
		public static OBDtoAdcPoolPASK toOBDtoAdcPoolPASK(AdcPoolDto pool)
		{
			
			OBDtoAdcPoolPASK poolFromSvc = new OBDtoAdcPoolPASK();
			poolFromSvc.setDbIndex(pool.getIndex());
			poolFromSvc.setName(pool.getName());
			
			poolFromSvc.setHealthCheckInfo(AdcPASKHealthCheckDto.toOBDtoAdcHealthCheckPASK(pool.adcPASKHealthCheck));
			if (pool.getLoadBalancingType().equals("Round Robin"))
				poolFromSvc.setLbMethod(OBDefine.LB_METHOD_ROUND_ROBIN);
			else if(pool.getLoadBalancingType().equals("Least Connections"))
				poolFromSvc.setLbMethod(OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER);
			else if (pool.getLoadBalancingType().equals("Hash"))
				poolFromSvc.setLbMethod(OBDefine.LB_METHOD_HASH);
			else if (pool.getLoadBalancingType().equals("NOT_ALLOWED"))
				poolFromSvc.setLbMethod(OBDefine.COMMON_NOT_ALLOWED);
			else
				throw new TypeNotPresentException(pool.getHealthCheckType(), null);
			
			
			poolFromSvc.setMemberList(new ArrayList<OBDtoAdcPoolMemberPASK>(AdcPoolMemberDto.toOBDtoAdcPoolMemberPASK(pool.getMembers())));
			return poolFromSvc;
		}
	
	// 로드발란싱, 헬스체크 Setter
	private void setLoadBalancingType(Integer loadBalancingType)
	{
		if(loadBalancingType==null) //null이 올 수 없지만 만약을 대비
		{
			setLoadBalancingType("");
			return;
		}
		switch (loadBalancingType)
		{
		case OBDefine.LB_METHOD_ROUND_ROBIN:
			setLoadBalancingType("Round Robin");	break;
		case OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER:
			setLoadBalancingType("Least Connections");	break;
		case OBDefine.LB_METHOD_HASH:
			setLoadBalancingType("Hash");	break;
		case OBDefine.COMMON_NOT_ALLOWED:
			setLoadBalancingType("NOT_ALLOWED");	break;
		default:
			setLoadBalancingType("");
		}
	}
	
	private void setHealthCheckType(Integer healthCheckType)
	{
		if(healthCheckType==null) //null이 올 수 없지만 만약을 대비
		{
			setHealthCheckType("");
			return;
		}
		switch (healthCheckType)
		{
		case OBDefine.HEALTH_CHECK.NONE:
			setHealthCheckType("NONE");	break;
		case OBDefine.HEALTH_CHECK.TCP:
			setHealthCheckType("TCP");	break;
		case OBDefine.HEALTH_CHECK.HTTP:
			setHealthCheckType("HTTP");	break;
		case OBDefine.HEALTH_CHECK.HTTPS:
			setHealthCheckType("HTTPS");	break;
		case OBDefine.HEALTH_CHECK.UDP:
			setHealthCheckType("UDP");	break;
		case OBDefine.HEALTH_CHECK.ICMP:
			setHealthCheckType("ICMP");	break;
		case OBDefine.HEALTH_CHECK.GATEWAY_ICMP:
			setHealthCheckType("GATEWAY_ICMP");	break;
		case OBDefine.HEALTH_CHECK.ARP:
			setHealthCheckType("ARP");	break;
		case OBDefine.HEALTH_CHECK.LINK:
			setHealthCheckType("LINK");	break;
		default:
			setHealthCheckType("NOT_ALLOWED");
		}
	}

	public String getIndex()
	{
		return index;
	}

	public void setIndex(String index)
	{
		this.index = index;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLoadBalancingType()
	{
		return loadBalancingType;
	}

	public void setLoadBalancingType(String loadBalancingType)
	{
		this.loadBalancingType = loadBalancingType;
	}

	public String getHealthCheckType()
	{
		return healthCheckType;
	}

	public void setHealthCheckType(String healthCheckType)
	{
		this.healthCheckType = healthCheckType;
	}

	public List<AdcPoolMemberDto> getMembers()
	{
		return members;
	}

	public void setMembers(List<AdcPoolMemberDto> members)
	{
		this.members = members;
	}

	public String getAlteonId()
	{
		return alteonId;
	}

	public void setAlteonId(String alteonId)
	{
		this.alteonId = alteonId;
	}

	public AdcPASHealthCheckDto getAdcPASHealthCheck()
	{
		return adcPASHealthCheck;
	}

	public void setAdcPASHealthCheck(AdcPASHealthCheckDto adcPASHealthCheck)
	{
		this.adcPASHealthCheck = adcPASHealthCheck;
	}	

	public AdcPASKHealthCheckDto getAdcPASKHealthCheck() {
		return adcPASKHealthCheck;
	}

	public void setAdcPASKHealthCheck(AdcPASKHealthCheckDto adcPASKHealthCheck) {
		this.adcPASKHealthCheck = adcPASKHealthCheck;
	}	
	
	public AdcALTEONHealthCheckDto getAdcALTEONHealthCheck()
	{
		return adcALTEONHealthCheck;
	}

	public void setAdcALTEONHealthCheck(AdcALTEONHealthCheckDto adcALTEONHealthCheck)
	{
		this.adcALTEONHealthCheck = adcALTEONHealthCheck;
	}
	
	public List<AdcPoolMemberDto> getEventMembers()
    {
        return eventMembers;
    }

    public void setEventMembers(List<AdcPoolMemberDto> eventMembers)
    {
        this.eventMembers = eventMembers;
    }

    @Override
    public String toString()
    {
        return "AdcPoolDto [index=" + index + ", name=" + name
                + ", loadBalancingType=" + loadBalancingType
                + ", healthCheckType=" + healthCheckType
                + ", adcPASHealthCheck=" + adcPASHealthCheck
                + ", adcPASKHealthCheck=" + adcPASKHealthCheck
                + ", adcALTEONHealthCheck=" + adcALTEONHealthCheck
                + ", members=" + members + ", eventMembers=" + eventMembers
                + ", alteonId=" + alteonId + "]";
    }
}