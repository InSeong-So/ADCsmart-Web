package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.impl.f5.DtoVlanTunnelFilter;
import kr.openbase.adcsmart.web.json.AdcPoolMemberJsonAdapter;

public class F5VsAddDto
{
	private String index;
	private Integer adcIndex;
	private String name;
	private String ip;
	private Integer port;
	private String poolIndex;
	private String poolName;
	private List<AdcPoolMemberDto> members;
	private List<AdcPoolMemberDto> eventMembers;
	private String membersInString;
	private String eventMembersInString;
	private String loadBalancingType;
	private String healthCheckType;
	private String profileIndex;
	private DtoVlanTunnelFilter vlanTunnel;

	public static OBDtoAdcVServerF5 toOBDtoAdcVServer(F5VsAddDto vsAdd)
	{
		OBDtoAdcVServerF5 vsFromSvc = new OBDtoAdcVServerF5();
		vsFromSvc.setIndex(vsAdd.getIndex());
		vsFromSvc.setAdcIndex(vsAdd.getAdcIndex());
		vsFromSvc.setName(vsAdd.getName());
		vsFromSvc.setvIP(vsAdd.getIp());
		vsFromSvc.setServicePort(vsAdd.getPort());
		vsFromSvc.setPersistence(StringUtils.isEmpty(vsAdd.getProfileIndex()) ? null : vsAdd.getProfileIndex());

		AdcPoolDto pool = new AdcPoolDto();
		pool.setIndex(StringUtils.isEmpty(vsAdd.getPoolIndex()) ? null : vsAdd.getPoolIndex());
		pool.setName(vsAdd.getPoolName());
		pool.setLoadBalancingType(vsAdd.getLoadBalancingType());
		pool.setHealthCheckType(vsAdd.getHealthCheckType());
		pool.setMembers(vsAdd.getMembers());
		if (vsAdd.getEventMembers() != null) pool.setEventMembers(vsAdd.getEventMembers());
		else pool.setEventMembers(vsAdd.getMembers());

		vsFromSvc.setPool(AdcPoolDto.toOBDtoAdcPoolF5(pool));

		DtoVlanTunnelFilter vlan = new DtoVlanTunnelFilter();
		if (vsAdd.getVlanTunnel() != null)
		{
			vlan.setStatus(vsAdd.getVlanTunnel().getStatus());
		}
		if (vsAdd.getVlanTunnel() != null)
		{
			vlan.setVlanName(vsAdd.getVlanTunnel().getVlanName());
		}
		vsFromSvc.setVlanFilter(vlan);

		return vsFromSvc;
	}

	private void convertMembersToJSON()
	{
		if (StringUtils.isEmpty(membersInString)) return;

		members = new ArrayList<AdcPoolMemberDto>();
		Gson gson = new GsonBuilder().registerTypeAdapter(AdcPoolMemberDto.class, new AdcPoolMemberJsonAdapter())
				.create();
		JsonParser parser = new JsonParser();
		JsonArray jarray = parser.parse(membersInString).getAsJsonArray();
		for (JsonElement elem : jarray)
			members.add(gson.fromJson(elem, AdcPoolMemberDto.class));
	}

	private void convertEventMembersToJSON()
	{
		if (StringUtils.isEmpty(eventMembersInString)) return;

		eventMembers = new ArrayList<AdcPoolMemberDto>();
		Gson gson = new GsonBuilder().registerTypeAdapter(AdcPoolMemberDto.class, new AdcPoolMemberJsonAdapter())
				.create();
		JsonParser parser = new JsonParser();
		JsonArray jarray = parser.parse(eventMembersInString).getAsJsonArray();
		for (JsonElement elem : jarray)
			eventMembers.add(gson.fromJson(elem, AdcPoolMemberDto.class));
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

	public String getMembersInString()
	{
		return membersInString;
	}

	public void setMembersInString(String membersInString)
	{
		this.membersInString = membersInString;
		convertMembersToJSON();
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

	public String getProfileIndex()
	{
		return profileIndex;
	}

	public void setProfileIndex(String profileIndex)
	{
		this.profileIndex = profileIndex;
	}

	public DtoVlanTunnelFilter getVlanTunnel()
	{
		return vlanTunnel;
	}

	public void setVlanTunnel(DtoVlanTunnelFilter vlanTunnel)
	{
		this.vlanTunnel = vlanTunnel;
	}

	public List<AdcPoolMemberDto> getEventMembers()
	{
		return eventMembers;
	}

	public void setEventMembers(List<AdcPoolMemberDto> eventMembers)
	{
		this.eventMembers = eventMembers;
	}

	public String getEventMembersInString()
	{
		return eventMembersInString;
	}

	public void setEventMembersInString(String eventMembersInString)
	{
		this.eventMembersInString = eventMembersInString;
		convertEventMembersToJSON();
	}

	@Override
	public String toString()
	{
		return "F5VsAddDto [index=" + index + ", adcIndex=" + adcIndex + ", name=" + name + ", ip=" + ip + ", port="
				+ port + ", poolIndex=" + poolIndex + ", poolName=" + poolName + ", members=" + members
				+ ", eventMembers=" + eventMembers + ", membersInString=" + membersInString + ", eventMembersInString="
				+ eventMembersInString + ", loadBalancingType=" + loadBalancingType + ", healthCheckType="
				+ healthCheckType + ", profileIndex=" + profileIndex + ", vlanTunnel=" + vlanTunnel + "]";
	}
}
