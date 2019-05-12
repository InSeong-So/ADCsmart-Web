package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoAdcNodeDetail
{
//	OBDtoAdcNodeF5 nodeInfo;
	
	private Integer	type; // group : 1, real : 0
	private String index;
	private String ipAddress;
	private Integer state;
	private Integer status;
	private String name;
	private Integer ratio;
	private String alteonID;      // only alteon
	private Integer groupIndex;
	private String groupName;
	private ArrayList<String> OBDtoAdcNodeDetail;
	private ArrayList <String> vserverAllowed;
	private ArrayList <String> vserverNotAllowed;
	private Long session=0L;
	private String adcName;
	private Integer adcIndex;
	private Integer adcType;
	private Integer adcStatus;
	private Integer adcMode;
	
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
	public Integer getAdcType()
    {
        return adcType;
    }
    public void setAdcType(Integer adcType)
    {
        this.adcType = adcType;
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
	public ArrayList<String> getOBDtoAdcNodeDetail()
	{
		return OBDtoAdcNodeDetail;
	}
	public void setOBDtoAdcNodeF5Detail(ArrayList<String> oBDtoAdcNodeDetail)
	{
	    OBDtoAdcNodeDetail = oBDtoAdcNodeDetail;
	}
	public Integer getGroupIndex()
	{
		return groupIndex;
	}
	public void setGroupIndex(Integer groupIndex)
	{
		this.groupIndex = groupIndex;
	}
	public String getGroupName()
	{
		return groupName;
	}
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
	public String getAlteonID()
    {
        return alteonID;
    }
    public void setAlteonID(String alteonID)
    {
        this.alteonID = alteonID;
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
    public Integer getAdcIndex()
    {
        return adcIndex;
    }
    public void setAdcIndex(Integer adcIndex)
    {
        this.adcIndex = adcIndex;
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
        return "OBDtoAdcNodeDetail [type=" + type + ", index=" + index
                + ", ipAddress=" + ipAddress + ", state=" + state + ", status="
                + status + ", name=" + name + ", ratio=" + ratio
                + ", alteonID=" + alteonID + ", groupIndex=" + groupIndex
                + ", groupName=" + groupName + ", OBDtoAdcNodeDetail="
                + OBDtoAdcNodeDetail + ", vserverAllowed=" + vserverAllowed
                + ", vserverNotAllowed=" + vserverNotAllowed + ", session="
                + session + ", adcName=" + adcName + ", adcIndex=" + adcIndex
                + ", adcType=" + adcType + ", adcStatus=" + adcStatus
                + ", adcMode=" + adcMode + "]";
    }
}
