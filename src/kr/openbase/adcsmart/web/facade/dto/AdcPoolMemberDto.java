package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberF5;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolMemberPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolMemberPASK;
import kr.openbase.adcsmart.service.utility.OBDefine;


public class AdcPoolMemberDto 
{
	private String index;
	private String ip;
	private Integer port;
	private Boolean isEnabled;	
	private Integer isChk;
	//PAS, PASK Real Server Id
	private Integer id; 
	// Alteon
	private String alteonNodeId;
	private String inter;
	private String retry;
	private String backup;
	private String weight;
	private String maxcon;
	private String time;
	private String port_;
	private Integer memStatus;
	private Integer ratio;
	
	// Alteon WEB SET
	public static AdcPoolMemberDto toAdcPoolMemberDto(OBDtoAdcPoolMemberAlteon memberFromSvc) 
	{
		AdcPoolMemberDto member = new AdcPoolMemberDto();
		member.setIp(memberFromSvc.getIpAddress());
		member.setPort(memberFromSvc.getPort());
		member.setIsEnabled(memberFromSvc.getState() == OBDefine.STATE_ENABLE);		
		member.setAlteonNodeId(memberFromSvc.getAlteonNodeID());
		
		if(memberFromSvc.getExtra() != null)
		{
		    if(!memberFromSvc.getExtra().contains(":"))
		    {
		        member.setPort_(memberFromSvc.getExtra().substring(1, memberFromSvc.getExtra().length()-1));
		        return member;
		    }
		        		        
			String extra[] = memberFromSvc.getExtra().split(",");
			String port_[] = extra[0].split(":"); 
			String inter[] = extra[1].split(":");  
			String retry[] = extra[2].split(":");  
			String backup[] = extra[3].split(":");
			String weight[] = extra[4].split(":");
			String maxcon[] = extra[5].split(":"); 
			String time[] = extra[6].split(":"); 
			
			if(extra[0].split(":").length == 2)
			{
				member.setPort_(port_[1]);
			}
			else
			{
				member.setPort_("");
			}
			if(extra[1].split(":").length == 2)
			{
				member.setInter(inter[1]);
			}
			else
			{
				member.setInter("");
			}
			if(extra[2].split(":").length == 2)
			{
				member.setRetry(retry[1]);
			}
			else
			{
				member.setRetry("");
			}
			if(extra[3].split(":").length == 2)
			{
				member.setBackup(backup[1]);
			}
			else
			{
				member.setBackup("");
			}
			if(extra[4].split(":").length == 2)
			{
				member.setWeight(weight[1]);
			}
			else
			{
				member.setWeight("");
			}
			if(extra[5].split(":").length == 2)
			{
				member.setMaxcon(maxcon[1]);
			}
			else
			{
				member.setMaxcon("");
			}
			if(extra[6].split(":").length == 2)
			{
				member.setTime(time[1]);
			}
			else
			{
				member.setTime("");
			}	
		}
		return member;
	}
	
	public static List<AdcPoolMemberDto> toAdcPoolMemberAlteonDto(List<OBDtoAdcPoolMemberAlteon> memberFromSvcs) 
	{

		List<AdcPoolMemberDto> members = new ArrayList<AdcPoolMemberDto>();
		for (OBDtoAdcPoolMemberAlteon e: memberFromSvcs)
			members.add(toAdcPoolMemberDto(e));
		
		return members;
	}
	// Alteon SVC SET
	public static OBDtoAdcPoolMemberAlteon toOBDtoAdcPoolMemberAlteon(AdcPoolMemberDto member) 
	{
		OBDtoAdcPoolMemberAlteon memberFromSvc = new OBDtoAdcPoolMemberAlteon();
		memberFromSvc.setIpAddress(member.getIp());
		memberFromSvc.setPort(member.getPort());
		memberFromSvc.setState(member.getIsEnabled() ? OBDefine.STATE_ENABLE : OBDefine.STATE_DISABLE);
		memberFromSvc.setAlteonNodeID(member.getAlteonNodeId());
		return memberFromSvc;
	}
	
	public static List<OBDtoAdcPoolMemberAlteon> toOBDtoAdcPoolMemberAlteon(List<AdcPoolMemberDto> members) 
	{
		List<OBDtoAdcPoolMemberAlteon> membersFromSvc = new ArrayList<OBDtoAdcPoolMemberAlteon>();
		for (AdcPoolMemberDto e: members)
			membersFromSvc.add(toOBDtoAdcPoolMemberAlteon(e));
		
		return membersFromSvc;
	}
	
	// F5 WEB SET
	public static AdcPoolMemberDto toAdcPoolMemberDto(OBDtoAdcPoolMemberF5 memberFromSvc) 
	{
		AdcPoolMemberDto member = new AdcPoolMemberDto();
		member.setIp(memberFromSvc.getIpAddress());
		member.setPort(memberFromSvc.getPort());
//		member.setIsEnabled(memberFromSvc.getState() == OBDefine.STATE_ENABLE);
//		member.setMemStatus(memberFromSvc.getState());
		member.setRatio(memberFromSvc.getRatio());
		if(memberFromSvc.getState().equals(OBDefine.MEMBER_STATE.ENABLE))
		{
		    member.setMemStatus(OBDefine.MEMBER_STATE.ENABLE);
		}
		else if(memberFromSvc.getState().equals(OBDefine.MEMBER_STATE.DISABLE))
        {
            member.setMemStatus(OBDefine.STATE_DISABLE);
        }
		else
		{
		    member.setMemStatus(OBDefine.MEMBER_STATE.FORCED_OFFLINE);
		}		
		
		member.setIsChk(0);
		return member;
	}
	
	public static List<AdcPoolMemberDto> toAdcPoolMemberF5Dto(List<OBDtoAdcPoolMemberF5> memberFromSvcs) 
	{
		List<AdcPoolMemberDto> members = new ArrayList<AdcPoolMemberDto>();
		if (memberFromSvcs == null)
			return members;
		
		for (OBDtoAdcPoolMemberF5 e: memberFromSvcs)
			members.add(toAdcPoolMemberDto(e));
		
		return members;
	}
	// F5 SVC SET
	public static OBDtoAdcPoolMemberF5 toOBDtoAdcPoolMemberF5(AdcPoolMemberDto member) 
	{
		OBDtoAdcPoolMemberF5 memberFromSvc = new OBDtoAdcPoolMemberF5();
		memberFromSvc.setIpAddress(member.getIp());
		memberFromSvc.setPort(member.getPort());
//		memberFromSvc.setState(member.getIsEnabled() ? OBDefine.STATE_ENABLE : OBDefine.STATE_DISABLE);
	        
//		if(member.getIsEnabled() == true)
//		{
//		    memberFromSvc.setState(OBDefine.STATE_ENABLE);
//		}
//		else if(member.getIsEnabled() == false)
//		{
//		    memberFromSvc.setState(OBDefine.STATE_DISABLE); 
//		}
//		else
//		{
//		    memberFromSvc.setState(OBDefine.STATE_FORCEDOFFLINE);
//		}
		
//		memberFromSvc.setRatio(member.getRatio());
		if(member.getMemStatus().equals(OBDefine.MEMBER_STATE.ENABLE))
        {
            memberFromSvc.setState(OBDefine.STATE_ENABLE);
        }
        else if(member.getMemStatus().equals(OBDefine.MEMBER_STATE.DISABLE))
        {
            memberFromSvc.setState(OBDefine.STATE_DISABLE); 
        }
        else   
        {
            memberFromSvc.setState(OBDefine.MEMBER_STATE.FORCED_OFFLINE);
        }
		
		return memberFromSvc;
	}
	
	public static List<OBDtoAdcPoolMemberF5> toOBDtoAdcPoolMemberF5(List<AdcPoolMemberDto> members) 
	{
		List<OBDtoAdcPoolMemberF5> membersFromSvc = new ArrayList<OBDtoAdcPoolMemberF5>();
		for (AdcPoolMemberDto e: members)
			membersFromSvc.add(toOBDtoAdcPoolMemberF5(e));
		
		return membersFromSvc;
	}	
	
	// PAS WEB SET
	public static AdcPoolMemberDto toAdcPoolMemberDto(OBDtoAdcPoolMemberPAS memberFromSvc) 
	{
		AdcPoolMemberDto member = new AdcPoolMemberDto();
		member.setIp(memberFromSvc.getIpAddress());
		member.setPort(memberFromSvc.getPort());
		member.setIsEnabled(memberFromSvc.getState() == OBDefine.STATE_ENABLE);
		member.setId(memberFromSvc.getId());
		return member;
	}
	
	public static List<AdcPoolMemberDto> toAdcPoolMemberPASDto(List<OBDtoAdcPoolMemberPAS> memberFromSvcs) 
	{
		List<AdcPoolMemberDto> members = new ArrayList<AdcPoolMemberDto>();
		if (memberFromSvcs == null)
			return members;
		
		for (OBDtoAdcPoolMemberPAS e: memberFromSvcs)
			members.add(toAdcPoolMemberDto(e));
		
		return members;
	}
	// PAS SVC SET
	public static OBDtoAdcPoolMemberPAS toOBDtoAdcPoolMemberPAS(AdcPoolMemberDto member) 
	{
		OBDtoAdcPoolMemberPAS memberFromSvc = new OBDtoAdcPoolMemberPAS();
		memberFromSvc.setId(member.getId());
		memberFromSvc.setIpAddress(member.getIp());
		memberFromSvc.setPort(member.getPort());
		memberFromSvc.setState(member.getIsEnabled() ? OBDefine.STATE_ENABLE : OBDefine.STATE_DISABLE);
		return memberFromSvc;
	}
	
	public static List<OBDtoAdcPoolMemberPAS> toOBDtoAdcPoolMemberPAS(List<AdcPoolMemberDto> members) 
	{
		List<OBDtoAdcPoolMemberPAS> membersFromSvc = new ArrayList<OBDtoAdcPoolMemberPAS>();
		for (AdcPoolMemberDto e: members)
			membersFromSvc.add(toOBDtoAdcPoolMemberPAS(e));
		
		return membersFromSvc;
	}
	
	// PASK WEB SET
	public static AdcPoolMemberDto toAdcPoolMemberDto(OBDtoAdcPoolMemberPASK memberFromSvc) 
	{
			AdcPoolMemberDto member = new AdcPoolMemberDto();
			member.setIndex(memberFromSvc.getDbIndex());
			member.setIp(memberFromSvc.getIpAddress());
			
			// PAS-K 에서는 Port 가 없는 노드가 존재한다.
			// SVC 에서는 이러한 경우를 포트 '-1'로 처리하며, 웹에서는 0으로 보여주도록 하기위해 아래와 같이 처리한다.
			if(memberFromSvc.getPort() == -1)
			{
				member.setPort(0);
			}
			else
			{
				member.setPort(memberFromSvc.getPort());
			}
			member.setIsEnabled(memberFromSvc.getState() == OBDefine.STATE_ENABLE);
			member.setId(memberFromSvc.getId());			
			return member;
	}
		
	public static List<AdcPoolMemberDto> toAdcPoolMemberPASKDto(List<OBDtoAdcPoolMemberPASK> memberFromSvcs) 
	{
			List<AdcPoolMemberDto> members = new ArrayList<AdcPoolMemberDto>();
			if (memberFromSvcs == null)
				return members;
			
			for (OBDtoAdcPoolMemberPASK e: memberFromSvcs)
				members.add(toAdcPoolMemberDto(e));
			
			return members;
	}
	// PASK SVC SET
	public static OBDtoAdcPoolMemberPASK toOBDtoAdcPoolMemberPASK(AdcPoolMemberDto member) 
	{
			OBDtoAdcPoolMemberPASK memberFromSvc = new OBDtoAdcPoolMemberPASK();
			memberFromSvc.setDbIndex(member.getIndex());
			memberFromSvc.setId(member.getId());
			memberFromSvc.setIpAddress(member.getIp());
			
			// PAS-K 에서는 Port 가 없는 노드가 존재한다.
			// SVC 에서는 이러한 경우를 포트 '-1'로 처리하기 때문에 웹의 포트 0을 아래와같이 처리하여 SVC에 넘겨준다.
			if(member.getPort() == 0)
			{
				memberFromSvc.setPort(-1);
			}
			else
			{
				memberFromSvc.setPort(member.getPort());
			}			
			memberFromSvc.setState(member.getIsEnabled() ? OBDefine.STATE_ENABLE : OBDefine.STATE_DISABLE);
			return memberFromSvc;
	}
		
	public static List<OBDtoAdcPoolMemberPASK> toOBDtoAdcPoolMemberPASK(List<AdcPoolMemberDto> members) 
	{
			List<OBDtoAdcPoolMemberPASK> membersFromSvc = new ArrayList<OBDtoAdcPoolMemberPASK>();
			for (AdcPoolMemberDto e: members)
				membersFromSvc.add(toOBDtoAdcPoolMemberPASK(e));
			
			return membersFromSvc;
	}	
	
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
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
	public Boolean getIsEnabled()
	{
		return isEnabled;
	}
	public void setIsEnabled(Boolean isEnabled)
	{
		this.isEnabled = isEnabled;
	}
	public String getAlteonNodeId()
	{
		return alteonNodeId;
	}
	public void setAlteonNodeId(String alteonNodeId)
	{
		this.alteonNodeId = alteonNodeId;
	}	
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	
	public Integer getMemStatus()
    {
        return memStatus;
    }

    public void setMemStatus(Integer memStatus)
    {
        this.memStatus = memStatus;
    }

    public Integer getRatio()
    {
        return ratio;
    }

    public void setRatio(Integer ratio)
    {
        this.ratio = ratio;
    }

    public Integer getIsChk()
    {
        return isChk;
    }

    public void setIsChk(Integer isChk)
    {
        this.isChk = isChk;
    }

    @Override
    public String toString()
    {
        return "AdcPoolMemberDto [index=" + index + ", ip=" + ip + ", port="
                + port + ", isEnabled=" + isEnabled + ", isChk=" + isChk
                + ", id=" + id + ", alteonNodeId=" + alteonNodeId + ", inter="
                + inter + ", retry=" + retry + ", backup=" + backup
                + ", weight=" + weight + ", maxcon=" + maxcon + ", time=" + time
                + ", port_=" + port_ + ", memStatus=" + memStatus + ", ratio="
                + ratio + "]";
    }
}