package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeDetail;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class AdcNodeDetailDto
{
//	OBDtoAdcNodeF5 nodeInfo;
	
	private Integer	type; // group : 1, real : 0
	private String index;
	private String ipAddress;
	private Integer state;
	private Integer status;
	private String name;
	private Integer ratio;
	private Integer groupIndex;
	private String groupName;
	private ArrayList <String> vserverAllowed;
	private ArrayList <String> vserverNotAllowed;
	private String vsAllowList;
	private String vsNotAllowList;
	private Long session;
	private String adcName;
	private String sessionUnit; 
	private String alteonID;
	private Integer adcIndex;
	private Integer adcType;
	private Integer adcStatus;
	private Integer adcMode;
	
	
	public static ArrayList<AdcNodeDetailDto> toNodeF5DetailDto(List<OBDtoAdcNodeDetail> nodeF5DetailFromSvc) throws Exception 
    {
        ArrayList<AdcNodeDetailDto> nodeF5Details = new ArrayList<AdcNodeDetailDto>();
        for (OBDtoAdcNodeDetail e : nodeF5DetailFromSvc)
        {
            nodeF5Details.add(toNodeF5DetailDto(e));

        }   
        return nodeF5Details;
    }
	
	public static AdcNodeDetailDto toNodeF5DetailDto(OBDtoAdcNodeDetail nodeF5DetailFromSvc) throws Exception
    {
        if (nodeF5DetailFromSvc == null)
        {
            return null;
        }
        
        AdcNodeDetailDto nodeF5Detail = new AdcNodeDetailDto();
        /*
        OBDtoAdcNodeF5 nodeInfo = new OBDtoAdcNodeF5();
        nodeInfo.setIndex(nodeF5DetailFromSvc.getNodeInfo().getIndex());
        nodeInfo.setIpAddress(nodeF5DetailFromSvc.getNodeInfo().getIpAddress());
        nodeInfo.setName(nodeF5DetailFromSvc.getNodeInfo().getName());
        nodeInfo.setRatio(nodeF5DetailFromSvc.getNodeInfo().getRatio());
        nodeInfo.setState(nodeF5DetailFromSvc.getNodeInfo().getState());  
        if(nodeF5DetailFromSvc.getNodeInfo().getIpAddress().equals("220.73.174.130"))
        	nodeInfo.setGroupIndex(3);
        else if(nodeF5DetailFromSvc.getNodeInfo().getIpAddress().equals("220.65.227.145"))
        	nodeInfo.setGroupIndex(3);
        else if(nodeF5DetailFromSvc.getNodeInfo().getIpAddress().equals("219.252.41.100"))
        	nodeInfo.setGroupIndex(4);
        else if(nodeF5DetailFromSvc.getNodeInfo().getIpAddress().equals("218.239.228.85"))
        	nodeInfo.setGroupIndex(4);
        else if(nodeF5DetailFromSvc.getNodeInfo().getIpAddress().equals("218.152.188.138"))
        	nodeInfo.setGroupIndex(4);
        else
        	nodeInfo.setGroupIndex(0);
//        	nodeInfo.setGroupIndex(nodeF5DetailFromSvc.getNodeInfo().getGroupIndex());
//        nodeInfo.setGroupIndex(nodeF5DetailFromSvc.getNodeInfo().getGroupIndex());
        nodeF5Detail.setNodeInfo(nodeInfo);
        */
        
//        if(nodeF5DetailFromSvc.getIpAddress().equals("220.73.174.130"))
//        {
//        	nodeF5Detail.setGroupIndex(6);
//        	nodeF5Detail.setType(1);
//        }
//        else if(nodeF5DetailFromSvc.getIpAddress().equals("220.65.227.145"))
//        {
//        	nodeF5Detail.setType(1);
//        	nodeF5Detail.setGroupIndex(6);
//        }
//        else if(nodeF5DetailFromSvc.getIpAddress().equals("219.252.41.100"))
//        {
//        	nodeF5Detail.setType(1);
//        	nodeF5Detail.setGroupIndex(6);
//        }
//        else if(nodeF5DetailFromSvc.getIpAddress().equals("218.239.228.85"))
//        {
//        	nodeF5Detail.setType(1);
//        	nodeF5Detail.setGroupIndex(7);
//        }
//        else if(nodeF5DetailFromSvc.getIpAddress().equals("218.152.188.138"))
//        {
//        	nodeF5Detail.setType(1);
//        	nodeF5Detail.setGroupIndex(8);
//        }
//        else
//        {
//        	nodeF5Detail.setType(0);
//        	nodeF5Detail.setGroupIndex(0);
//        }
        nodeF5Detail.setType(nodeF5DetailFromSvc.getType());
        nodeF5Detail.setIndex(nodeF5DetailFromSvc.getIndex());
        nodeF5Detail.setIpAddress(nodeF5DetailFromSvc.getIpAddress());
        nodeF5Detail.setName(nodeF5DetailFromSvc.getName());
        nodeF5Detail.setRatio(nodeF5DetailFromSvc.getRatio());
        nodeF5Detail.setState(nodeF5DetailFromSvc.getState());
        nodeF5Detail.setStatus(nodeF5DetailFromSvc.getStatus());
        nodeF5Detail.setGroupIndex(nodeF5DetailFromSvc.getGroupIndex());    
        nodeF5Detail.setGroupName(nodeF5DetailFromSvc.getGroupName());
        
        nodeF5Detail.setVserverAllowed(nodeF5DetailFromSvc.getVserverAllowed());
        nodeF5Detail.setVserverNotAllowed(nodeF5DetailFromSvc.getVserverNotAllowed());
        
//        System.out.println(nodeF5DetailFromSvc.getVserverAllowed());
        
        List<String> alloweds = new ArrayList<String>();
        List<String> notAlloweds = new ArrayList<String>();
        alloweds.addAll(nodeF5DetailFromSvc.getVserverAllowed());
        notAlloweds.addAll(nodeF5DetailFromSvc.getVserverNotAllowed());  
      
        String[] allowListString = alloweds.toArray(new String[alloweds.size()]);
        String[] notAllowListString = notAlloweds.toArray(new String[notAlloweds.size()]);
        String allowList = "";
        String notAllowList = "";
        for(String allowed: allowListString)
        {
//            System.out.println(line);
            allowList += allowed + '\n';
        }
        for(String notAllowed: notAllowListString)
        {
            notAllowList += notAllowed + '\n';
        }
        nodeF5Detail.setVsAllowList(allowList);
        nodeF5Detail.setVsNotAllowList(notAllowList);
        
        nodeF5Detail.setSession(nodeF5DetailFromSvc.getSession());
        nodeF5Detail.setAdcName(nodeF5DetailFromSvc.getAdcName());
        nodeF5Detail.setAlteonID(nodeF5DetailFromSvc.getAlteonID());
        nodeF5Detail.setAdcIndex(nodeF5DetailFromSvc.getAdcIndex());
        nodeF5Detail.setAdcType(nodeF5DetailFromSvc.getAdcType());
        nodeF5Detail.setAdcStatus(nodeF5DetailFromSvc.getAdcStatus());
        nodeF5Detail.setAdcMode(nodeF5DetailFromSvc.getAdcMode());
        nodeF5Detail.setAlteonID(nodeF5DetailFromSvc.getAlteonID());
        
//        String allowListString = "";
        
//        for (String e : adcsFromSvc)
//        {
//            allowListString += e.getAdcName() + "\n";
//        }
//        
//        nodeF5Detail.setVsAllowList(allowListString);
        
        
        return nodeF5Detail;
    }	
	
	public Integer getType() 
	{
		return type;
	}
	public void setType(Integer type) 
	{
		this.type = type;
	}
	public String getIndex() 
	{
		return index;
	}
	public void setIndex(String index) 
	{
		this.index = index;
	}
	public String getIpAddress() 
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) 
	{
		this.ipAddress = ipAddress;
	}
	public Integer getState() 
	{
		return state;
	}
	public void setState(Integer state) 
	{
		this.state = state;
	}
	public Integer getStatus() 
	{
		return status;
	}
	public void setStatus(Integer status) 
	{
		this.status = status;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}

	public Integer getRatio() 
	{
		return ratio;
	}
	public void setRatio(Integer ratio)
	{
		this.ratio = ratio;
	}	
	public Integer getGroupIndex() 
	{
		return groupIndex;
	}
	public void setGroupIndex(Integer groupIndex) 
	{
		this.groupIndex = groupIndex;
	}

	public ArrayList<String> getVserverAllowed()
	{
		return vserverAllowed;
	}
	public void setVserverAllowed(ArrayList<String> vserverAllowed)
	{
		this.vserverAllowed = vserverAllowed;
	}
	public ArrayList<String> getVserverNotAllowed()
	{
		return vserverNotAllowed;
	}
	public void setVserverNotAllowed(ArrayList<String> vserverNotAllowed)
	{
		this.vserverNotAllowed = vserverNotAllowed;
	}
	public String getVsAllowList() 
	{
		return vsAllowList;
	}
	public void setVsAllowList(String vsAllowList) 
	{
		this.vsAllowList = vsAllowList;
	}
	public String getVsNotAllowList() 
	{
		return vsNotAllowList;
	}
	public void setVsNotAllowList(String vsNotAllowList) 
	{
		this.vsNotAllowList = vsNotAllowList;
	}
	public String getGroupName() 
	{
		return groupName;
	}
	public void setGroupName(String groupName) 
	{
		this.groupName = groupName;
	}

    public Long getSession()
    {
        return session;
    }

    public void setSession(Long session)
    {
        this.session = session;
    }

    public String getAdcName()
    {
        return adcName;
    }

    public void setAdcName(String adcName)
    {
        this.adcName = adcName;
    }

    public String getSessionUnit()
    {
        return OBUtility.convertKmg(session.longValue());
    }

    public void setSessionUnit(String sessionUnit)
    {
        this.sessionUnit = sessionUnit;
    }

    public String getAlteonID()
    {
        return alteonID;
    }

    public void setAlteonID(String alteonID)
    {
        this.alteonID = alteonID;
    }

    public Integer getAdcIndex()
    {
        return adcIndex;
    }

    public void setAdcIndex(Integer adcIndex)
    {
        this.adcIndex = adcIndex;
    }

    public Integer getAdcType()
    {
        return adcType;
    }

    public void setAdcType(Integer adcType)
    {
        this.adcType = adcType;
    }

    public Integer getAdcStatus()
    {
        return adcStatus;
    }

    public void setAdcStatus(Integer adcStatus)
    {
        this.adcStatus = adcStatus;
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
        return "AdcNodeDetailDto [type=" + type + ", index=" + index
                + ", ipAddress=" + ipAddress + ", state=" + state + ", status="
                + status + ", name=" + name + ", ratio=" + ratio
                + ", groupIndex=" + groupIndex + ", groupName=" + groupName
                + ", vserverAllowed=" + vserverAllowed + ", vserverNotAllowed="
                + vserverNotAllowed + ", vsAllowList=" + vsAllowList
                + ", vsNotAllowList=" + vsNotAllowList + ", session=" + session
                + ", adcName=" + adcName + ", sessionUnit=" + sessionUnit
                + ", alteonID=" + alteonID + ", adcIndex=" + adcIndex
                + ", adcType=" + adcType + ", adcStatus=" + adcStatus
                + ", adcMode=" + adcMode + "]";
    }    
}
