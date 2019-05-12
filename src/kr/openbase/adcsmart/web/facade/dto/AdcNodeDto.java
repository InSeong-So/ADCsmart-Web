package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcNodePAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcNodePASK;
import kr.openbase.adcsmart.service.utility.OBDefine;

public class AdcNodeDto {
	private String index;
	private Integer id;
	private String ip;
	private String name;
	private Integer port;
	private String state;	
	// Alteon	

	private String alteonId;
	private String alteonNodeId;
	private String port_;
	private String inter;
	private String retry;
	private String backup;
	private String weight;
	private String maxcon;
	private String time;
	private Integer ratio;
	
	
	public static AdcNodeDto toAdcNodeDto(OBDtoAdcNodeAlteon nodeFromSvc) 
	{
		AdcNodeDto node = new AdcNodeDto();
		node.setIndex(nodeFromSvc.getIndex());
		node.setIp(nodeFromSvc.getIpAddress());
		node.setState(nodeFromSvc.getState() == OBDefine.STATE_ENABLE ? "enable" : "disable");
		node.setName(nodeFromSvc.getName());
		node.setAlteonId(nodeFromSvc.getAlteonId());
		if(nodeFromSvc.getExtra() != null)
		{		    
		    if(!nodeFromSvc.getExtra().contains(":"))
            {
		        node.setPort_(nodeFromSvc.getExtra().substring(1, nodeFromSvc.getExtra().length()-1));
                return node;
            }
			String extra[] = nodeFromSvc.getExtra().split(",");
			String port_[] = extra[0].split(":");
			String inter[] = extra[1].split(":");  
			String retry[] = extra[2].split(":");  
			String backup[] = extra[3].split(":");
			String weight[] = extra[4].split(":");
			String maxcon[] = extra[5].split(":"); 
			String time[] = extra[6].split(":");   
			 
			if(port_.length==2)
			{
				node.setPort_(port_[1]);
			}
			if(inter.length==2)
			{
				node.setInter(inter[1]);
			}
			if(retry.length==2)
			{
				node.setRetry(retry[1]);
			}
			if(backup.length==2)
			{
				node.setBackup(backup[1]);
			}
			if(weight.length==2)
			{
				node.setWeight(weight[1]);
			}
			if(maxcon.length==2)
			{
				node.setMaxcon(maxcon[1]);
			}
			if(time.length==2)
			{
				node.setTime(time[1]);
			}
	
		}
		return node;
	}

	public static List<AdcNodeDto> toAdcNodeAlteonDto(List<OBDtoAdcNodeAlteon> nodesFromSvc) 
	{
		ArrayList<AdcNodeDto> nodes = new ArrayList<AdcNodeDto>();
		for (OBDtoAdcNodeAlteon e : nodesFromSvc)
			nodes.add(toAdcNodeDto(e));

		return nodes;
	}
	
	public static AdcNodeDto toAdcNodeDto(OBDtoAdcNodeF5 nodeFromSvc) 
	{
		AdcNodeDto node = new AdcNodeDto();
		node.setIndex(nodeFromSvc.getIndex());
		node.setIp(nodeFromSvc.getIpAddress());
//		node.setState(nodeFromSvc.getState() == OBDefine.STATE_ENABLE ? "enable" : "disable");
		node.setRatio(nodeFromSvc.getRatio());
		if(nodeFromSvc.getState() == OBDefine.MEMBER_STATE.ENABLE)
		{
		    node.setState("enable");
		}
		else if(nodeFromSvc.getState() == OBDefine.MEMBER_STATE.DISABLE)
        {
            node.setState("disable");
        }
		else
        {
            node.setState("forced");
        }
		return node;
	}

	public static List<AdcNodeDto> toAdcNodeF5Dto(List<OBDtoAdcNodeF5> nodesFromSvc) 
	{
		ArrayList<AdcNodeDto> nodes = new ArrayList<AdcNodeDto>();
		for (OBDtoAdcNodeF5 e : nodesFromSvc)
			nodes.add(toAdcNodeDto(e));

		return nodes;
	}
	
	public static AdcNodeDto toAdcNodeDto(OBDtoAdcNodePAS nodeFromSvc) 
	{
		AdcNodeDto node = new AdcNodeDto();
		node.setIndex(nodeFromSvc.getDbIndex());
		node.setIp(nodeFromSvc.getIpAddress());
		node.setState(nodeFromSvc.getState() == OBDefine.STATE_ENABLE ? "enable" : "disable");
		return node;
	}

	public static List<AdcNodeDto> toAdcNodePASDto(List<OBDtoAdcNodePAS> nodesFromSvc) 
	{
		ArrayList<AdcNodeDto> nodes = new ArrayList<AdcNodeDto>();
		for (OBDtoAdcNodePAS e : nodesFromSvc)
			nodes.add(toAdcNodeDto(e));

		return nodes;
	}
	
	public static AdcNodeDto toAdcNodeDto(OBDtoAdcNodePASK nodeFromSvc)
	{
		AdcNodeDto node = new AdcNodeDto();
		node.setIndex(nodeFromSvc.getDbIndex());
		node.setId(nodeFromSvc.getId());
		node.setIp(nodeFromSvc.getIpAddress());
		node.setName(nodeFromSvc.getName());
		// PAS-K 에서는 Port 가 없는 노드가 존재한다.
		// SVC 에서는 이러한 경우를 포트 '-1'로 처리하며, 웹에서는 0으로 보여주도록 하기위해 아래와 같이 처리한다.
		if (nodeFromSvc.getPort() == -1)
		{
			node.setPort(0);
		}
		else
		{
			node.setPort(nodeFromSvc.getPort());
		}
		node.setState(nodeFromSvc.getState() == OBDefine.STATE_ENABLE ? "enable" : "disable");
		return node;
	}

	public static List<AdcNodeDto> toAdcNodePASKDto(List<OBDtoAdcNodePASK> nodesFromSvc)
	{
		ArrayList<AdcNodeDto> nodes = new ArrayList<AdcNodeDto>();
		for (OBDtoAdcNodePASK e : nodesFromSvc)
			nodes.add(toAdcNodeDto(e));

		return nodes;
	}	

	public String getIndex()
	{
		return index;
	}
	
	public void setIndex(String index)
	{
		this.index = index;
	}
	
	public Integer getId()
	{
		return id;
	}
	
	public void setId(Integer id)
	{
		this.id = id;
	}
	
	public String getIp()
	{
		return ip;
	}
	
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public Integer getPort()
	{
		return port;
	}

	public void setPort(Integer port)
	{
		this.port = port;
	}
	
	public String getState()
	{
		return state;
	}
	
	public void setState(String state)
	{
		this.state = state;
	}
	
	public String getAlteonId()
	{
		return alteonId;
	}
	
	public void setAlteonId(String alteonId)
	{
		this.alteonId = alteonId;
	}
	
	public String getAlteonNodeId()
	{
		return alteonNodeId;
	}

	public void setAlteonNodeId(String alteonNodeId)
	{
		this.alteonNodeId = alteonNodeId;
	}

	public String getInter()
	{
		return inter;
	}

	public void setInter(String inter)
	{
		this.inter = inter;
	}

	public String getRetry()
	{
		return retry;
	}

	public void setRetry(String retry)
	{
		this.retry = retry;
	}

	public String getBackup()
	{
		return backup;
	}

	public void setBackup(String backup)
	{
		this.backup = backup;
	}

	public String getWeight()
	{
		return weight;
	}

	public void setWeight(String weight)
	{
		this.weight = weight;
	}

	public String getMaxcon()
	{
		return maxcon;
	}

	public void setMaxcon(String maxcon)
	{
		this.maxcon = maxcon;
	}

	public String getTime()
	{
		return time;
	}

	public void setTime(String time)
	{
		this.time = time;
	}

	public String getPort_()
	{
		return port_;
	}

	public void setPort_(String port_)
	{
		this.port_ = port_;
	}

	public Integer getRatio()
    {
        return ratio;
    }

    public void setRatio(Integer ratio)
    {
        this.ratio = ratio;
    }

    @Override
	public String toString() 
    {
		return "AdcNodeDto [index=" + index + ", id=" + id + ", ip=" + ip
				+ ", name=" + name + ", port=" + port + ", state=" + state
				+ ", alteonId=" + alteonId + ", alteonNodeId=" + alteonNodeId
				+ ", port_=" + port_ + ", inter=" + inter + ", retry=" + retry
				+ ", backup=" + backup + ", weight=" + weight + ", maxcon="
				+ maxcon + ", time=" + time + ", ratio=" + ratio + "]";
	}	
}