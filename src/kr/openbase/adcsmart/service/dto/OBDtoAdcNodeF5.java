package kr.openbase.adcsmart.service.dto;

import java.io.Serializable;

public class OBDtoAdcNodeF5 implements Serializable
{
	private static final long serialVersionUID = 10L;
	private String index;
	private String ipAddress;
	private Integer state;
	private Integer status;
	private String alteonID;                      // alteon 용도
	private String name;
	private Integer ratio;
	private Integer groupIndex;
	private Integer adcIndex;                      // 전체나 그룹일때 사용
	private Integer adcType;                       // 전체나 그룹일때 사용

	public OBDtoAdcNodeF5(OBDtoAdcNodeF5 obj)
	{
		super();
		this.index = obj.getIndex();
		this.ipAddress = obj.getIpAddress();
		this.state = obj.getState();
		this.status = obj.getStatus();
		this.name = obj.getName();
		this.ratio = obj.getRatio();
		this.groupIndex = obj.getGroupIndex();
		this.alteonID = obj.getAlteonID();
		this.adcIndex = obj.getAdcIndex();
		this.adcType = obj.getAdcType();
	}
	public OBDtoAdcNodeF5()
	{
	}

	@Override
    public String toString()
    {
        return "OBDtoAdcNodeF5 [index=" + index + ", ipAddress=" + ipAddress
                + ", state=" + state + ", status=" + status + ", alteonID="
                + alteonID + ", name=" + name + ", ratio=" + ratio
                + ", groupIndex=" + groupIndex + ", adcIndex=" + adcIndex
                + ", adcType=" + adcType + "]";
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
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((adcIndex == null) ? 0 : adcIndex.hashCode());
        result = prime * result + ((adcType == null) ? 0 : adcType.hashCode());
        result = prime * result + ((alteonID == null) ? 0 : alteonID.hashCode());
        result = prime * result + ((groupIndex == null) ? 0 : groupIndex.hashCode());
        result = prime * result + ((index == null) ? 0 : index.hashCode());
        result = prime * result + ((ipAddress == null) ? 0 : ipAddress.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((ratio == null) ? 0 : ratio.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        OBDtoAdcNodeF5 other = (OBDtoAdcNodeF5) obj;
        if(adcIndex == null)
        {
            if(other.adcIndex != null)
                return false;
        }
        else if(!adcIndex.equals(other.adcIndex))
            return false;
        if(adcType == null)
        {
            if(other.adcType != null)
                return false;
        }
        else if(!adcType.equals(other.adcType))
            return false;
        if(alteonID == null)
        {
            if(other.alteonID != null)
                return false;
        }
        else if(!alteonID.equals(other.alteonID))
            return false;
        if(groupIndex == null)
        {
            if(other.groupIndex != null)
                return false;
        }
        else if(!groupIndex.equals(other.groupIndex))
            return false;
        if(index == null)
        {
            if(other.index != null)
                return false;
        }
        else if(!index.equals(other.index))
            return false;
        if(ipAddress == null)
        {
            if(other.ipAddress != null)
                return false;
        }
        else if(!ipAddress.equals(other.ipAddress))
            return false;
        if(name == null)
        {
            if(other.name != null)
                return false;
        }
        else if(!name.equals(other.name))
            return false;
        if(ratio == null)
        {
            if(other.ratio != null)
                return false;
        }
        else if(!ratio.equals(other.ratio))
            return false;
        if(state == null)
        {
            if(other.state != null)
                return false;
        }
        else if(!state.equals(other.state))
            return false;
        if(status == null)
        {
            if(other.status != null)
                return false;
        }
        else if(!status.equals(other.status))
            return false;
        return true;
    }
    //순수하게 장비에서 받은 설정만 같은지 비교하는 함수. ip, name, state, ration
    public boolean configEquals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        OBDtoAdcNodeF5 other = (OBDtoAdcNodeF5) obj;
        
        if(ipAddress == null)
        {
            if(other.ipAddress != null)
                return false;
        }
        else if(!ipAddress.equals(other.ipAddress))
            return false;
        if(name == null)
        {
            if(other.name != null)
                return false;
        }
        else if(!name.equals(other.name))
            return false;
        if(ratio == null)
        {
            if(other.ratio != null)
                return false;
        }
        else if(!ratio.equals(other.ratio))
            return false;
        if(state == null)
        {
            if(other.state != null)
                return false;
        }
        else if(!state.equals(other.state))
            return false;
        
        return true;
    }
}
