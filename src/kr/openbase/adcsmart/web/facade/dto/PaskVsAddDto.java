package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.web.json.AdcPoolMemberJsonAdapter;


public class PaskVsAddDto
{
	private String index;
	private Integer adcIndex;
	private String name;
	private String ip;
	private String ipView;
	private Integer port;
	private String portView;
	private Integer protocol;
	private String poolIndex;
	private String poolName;
	private List<AdcPoolMemberDto> members;
	private String membersInString;
	private String loadBalancingType;
	private String state;
	private Integer configurable;
	private String vipInfo;
	
	private String healthCheckDbIndex;
	private Integer healthCheckId;
	private String healthCheckName;
	private String healthCheckType;
	private Integer healthCheckPort;
	private Integer healthCheckInterval;
	private Integer healthCheckTimeout;
	private Integer healthCheckState;	
	private Integer adcMode;
	
	public static OBDtoAdcVServerPASK toOBDtoAdcVServer(PaskVsAddDto vsAdd)
	{
		OBDtoAdcVServerPASK vsFromSvc = new OBDtoAdcVServerPASK();
		vsFromSvc.setDbIndex(vsAdd.getIndex());
		vsFromSvc.setAdcIndex(vsAdd.getAdcIndex());
		vsFromSvc.setName(vsAdd.getName());
		vsFromSvc.setvIP(vsAdd.getIp());
		vsFromSvc.setvIPView(vsAdd.getIpView());
		vsFromSvc.setSrvPort(vsAdd.getPort());
		vsFromSvc.setSrvPortView(vsAdd.getPortView());
		vsFromSvc.setSrvProtocol(vsAdd.getProtocol());
		vsFromSvc.setSubInfo(vsAdd.getVipInfo());
		if(vsAdd.getConfigurable() != null)
		{
			if (vsAdd.getConfigurable() == 1)
			{
				vsFromSvc.setConfigurable(true);
			}
			else if (vsAdd.getConfigurable() == 0)
			{
				vsFromSvc.setConfigurable(false);
			}
		}
		if(vsAdd.getState() != null)
		{
			if (vsAdd.getState().equals("enable"))
			{
				vsFromSvc.setState(OBDefine.STATE_ENABLE);
			}
			else
			{
				vsFromSvc.setState(OBDefine.STATE_DISABLE);
			}
		}
		AdcPoolDto pool = new AdcPoolDto();
		pool.setIndex(StringUtils.isEmpty(vsAdd.getPoolIndex()) ? null : vsAdd.getPoolIndex());
		pool.setName(vsAdd.getPoolName());
		pool.setLoadBalancingType(vsAdd.getLoadBalancingType());
		pool.setMembers(vsAdd.getMembers());
		
		AdcPASKHealthCheckDto adcPASKHealthCheck = new AdcPASKHealthCheckDto();
		adcPASKHealthCheck.setDbIndex(vsAdd.healthCheckDbIndex);
		adcPASKHealthCheck.setId(vsAdd.healthCheckId);
		adcPASKHealthCheck.setName(vsAdd.healthCheckName);
		adcPASKHealthCheck.setType(vsAdd.healthCheckType);
		adcPASKHealthCheck.setPort(vsAdd.healthCheckPort);
		adcPASKHealthCheck.setInterval(vsAdd.healthCheckInterval);
		adcPASKHealthCheck.setTimeout(vsAdd.healthCheckTimeout);
		adcPASKHealthCheck.setState(vsAdd.healthCheckState);		
		pool.setAdcPASKHealthCheck(adcPASKHealthCheck);		
		
		vsFromSvc.setPool(AdcPoolDto.toOBDtoAdcPoolPASK(pool));
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
	

	public String getVipInfo()
	{
		return vipInfo;
	}

	public void setVipInfo(String vipInfo)
	{
		this.vipInfo = vipInfo;
	}

	public Integer getConfigurable()
	{
		return configurable;
	}

	public void setConfigurable(Integer configurable)
	{
		this.configurable = configurable;
	}

	public String getIpView()
	{
		return ipView;
	}

	public void setIpView(String ipView)
	{
		this.ipView = ipView;
	}

	public String getPortView()
	{
		return portView;
	}

	public void setPortView(String portView)
	{
		this.portView = portView;
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

	public Integer getHealthCheckId()
	{
		return healthCheckId;
	}

	public void setHealthCheckId(Integer healthCheckId)
	{
		this.healthCheckId = healthCheckId;
	}
	
	public String getHealthCheckDbIndex()
	{
		return healthCheckDbIndex;
	}

	public void setHealthCheckDbIndex(String healthCheckDbIndex)
	{
		this.healthCheckDbIndex = healthCheckDbIndex;
	}

	public String getHealthCheckName()
	{
		return healthCheckName;
	}

	public void setHealthCheckName(String healthCheckName)
	{
		this.healthCheckName = healthCheckName;
	}

	public Integer getHealthCheckPort()
	{
		return healthCheckPort;
	}

	public void setHealthCheckPort(Integer healthCheckPort)
	{
		this.healthCheckPort = healthCheckPort;
	}

	public Integer getHealthCheckInterval()
	{
		return healthCheckInterval;
	}

	public void setHealthCheckInterval(Integer healthCheckInterval)
	{
		this.healthCheckInterval = healthCheckInterval;
	}

	public Integer getHealthCheckTimeout()
	{
		return healthCheckTimeout;
	}

	public void setHealthCheckTimeout(Integer healthCheckTimeout)
	{
		this.healthCheckTimeout = healthCheckTimeout;
	}

	public Integer getHealthCheckState()
	{
		return healthCheckState;
	}

	public void setHealthCheckState(Integer healthCheckState)
	{
		this.healthCheckState = healthCheckState;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
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

    @Override
	public String toString()
	{
		return "PaskVsAddDto [index=" + index + ", adcIndex=" + adcIndex + ", name=" + name + ", ip=" + ip + ", ipView=" + ipView + ", port=" + port + ", portView=" + portView + ", protocol=" + protocol + ", poolIndex=" + poolIndex + ", poolName=" + poolName + ", members=" + members + ", membersInString=" + membersInString + ", loadBalancingType=" + loadBalancingType + ", state=" + state + ", configurable=" + configurable + ", vipInfo=" + vipInfo + ", healthCheckDbIndex=" + healthCheckDbIndex + ", healthCheckId=" + healthCheckId + ", healthCheckName=" + healthCheckName + ", healthCheckType=" + healthCheckType + ", healthCheckPort=" + healthCheckPort + ", healthCheckInterval=" + healthCheckInterval + ", healthCheckTimeout=" + healthCheckTimeout + ", healthCheckState=" + healthCheckState + "]";
	}
	
}
