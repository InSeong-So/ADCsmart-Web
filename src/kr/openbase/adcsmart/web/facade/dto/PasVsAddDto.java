package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.web.json.AdcPoolMemberJsonAdapter;


public class PasVsAddDto
{
	private String index;
	private Integer adcIndex;
	private String name;
	private String ip;
	private Integer port;
	private Integer protocol;
	private String poolIndex;
	private String poolName;
	private List<AdcPoolMemberDto> members;
	private String membersInString;
	private String loadBalancingType;
	private String healthCheckType;
	private Integer healthCheckId;	
	private String state;
	private Integer adcMode;
	private Integer configurable;
	
	public static OBDtoAdcVServerPAS toOBDtoAdcVServer(PasVsAddDto vsAdd)
	{
		OBDtoAdcVServerPAS vsFromSvc = new OBDtoAdcVServerPAS();
		vsFromSvc.setDbIndex(vsAdd.getIndex());
		vsFromSvc.setAdcIndex(vsAdd.getAdcIndex());
		vsFromSvc.setName(vsAdd.getName());
		vsFromSvc.setvIP(vsAdd.getIp());
		vsFromSvc.setSrvPort(vsAdd.getPort());
		vsFromSvc.setSrvProtocol(vsAdd.getProtocol());		
		
		if (vsAdd.getState().equals("enable"))
		{
			vsFromSvc.setState(OBDefine.STATE_ENABLE);
		}
		else
		{
			vsFromSvc.setState(OBDefine.STATE_DISABLE);
		}
		
		AdcPoolDto pool = new AdcPoolDto();
		pool.setIndex(StringUtils.isEmpty(vsAdd.getPoolIndex()) ? null : vsAdd.getPoolIndex());
		pool.setName(vsAdd.getPoolName());
		pool.setLoadBalancingType(vsAdd.getLoadBalancingType());
		pool.setMembers(vsAdd.getMembers());
		AdcPASHealthCheckDto adcPASHealthCheck = new AdcPASHealthCheckDto();
		adcPASHealthCheck.setType(vsAdd.healthCheckType);
		adcPASHealthCheck.setId(vsAdd.healthCheckId);
		pool.setAdcPASHealthCheck(adcPASHealthCheck);		
		
		vsFromSvc.setPool(AdcPoolDto.toOBDtoAdcPoolPAS(pool));
		return vsFromSvc;		
	}

	private void convertMembersToJSON()
	{
		if (StringUtils.isEmpty(membersInString))
			return;
		
		members = new ArrayList<AdcPoolMemberDto>();
		Gson gson = new GsonBuilder().registerTypeAdapter(AdcPoolMemberDto.class, new AdcPoolMemberJsonAdapter()).create();
		JsonParser parser = new JsonParser();
		JsonArray jarray = parser.parse(membersInString).getAsJsonArray();
		for (JsonElement elem : jarray)
			members.add(gson.fromJson(elem, AdcPoolMemberDto.class));
	}
	
	public String getMembersInString()
	{
		return membersInString;
	}
	public void setMembersInString(String membersInString)
	{
		this.membersInString = membersInString;
		convertMembersToJSON();
	}
	
	public String getIndex()
	{
		return index;
	}

	public void setIndex(String index)
	{
		this.index = index;
	}

	public Integer getAdcIndex()
	{
		return adcIndex;
	}

	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

	public Integer getPort()
	{
		return port;
	}

	public void setPort(Integer port)
	{
		this.port = port;
	}
	
	public Integer getProtocol()
	{
		return protocol;		
	}

	public void setProtocol(Integer protocol)
	{
		this.protocol = protocol;
	}

	public String getPoolIndex()
	{
		return poolIndex;
	}

	public void setPoolIndex(String poolIndex)
	{
		this.poolIndex = poolIndex;
	}

	public String getPoolName()
	{
		return poolName;
	}

	public void setPoolName(String poolName)
	{
		this.poolName = poolName;
	}

	public List<AdcPoolMemberDto> getMembers()
	{
		return members;
	}

	public void setMembers(List<AdcPoolMemberDto> members)
	{
		this.members = members;
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
	
	public Integer getHealthCheckId()
	{
		return healthCheckId;
	}

	public void setHealthCheckId(Integer healthCheckId)
	{
		this.healthCheckId = healthCheckId;
	}	
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getAdcMode()
    {
        return adcMode;
    }

    public void setAdcMode(Integer adcMode)
    {
        this.adcMode = adcMode;
    }

    public Integer getConfigurable()
    {
        return configurable;
    }

    public void setConfigurable(Integer configurable)
    {
        this.configurable = configurable;
    }

    @Override
    public String toString()
    {
        return "PasVsAddDto [index=" + index + ", adcIndex=" + adcIndex
                + ", name=" + name + ", ip=" + ip + ", port=" + port
                + ", protocol=" + protocol + ", poolIndex=" + poolIndex
                + ", poolName=" + poolName + ", members=" + members
                + ", membersInString=" + membersInString
                + ", loadBalancingType=" + loadBalancingType
                + ", healthCheckType=" + healthCheckType + ", healthCheckId="
                + healthCheckId + ", state=" + state + ", adcMode=" + adcMode
                + ", configurable=" + configurable + "]";
    }	
}