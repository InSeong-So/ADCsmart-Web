package kr.openbase.adcsmart.service.dto;

import java.io.Serializable;

public class OBDtoAdcNodeAlteon implements Serializable
{
	private static final long serialVersionUID = 10L;
	private String index;
	private String alteonId;
	private String ipAddress;
	private String name;
	private Integer state;
	private Integer status;
	private Integer bakType;
	private String bakId;
	private String extra; //real server 상세 정보
	private Integer ratio; //
 
	@Override
	public String toString()
	{
		return "OBDtoAdcNodeAlteon [index=" + index + ", alteonId=" + alteonId
				+ ", ipAddress=" + ipAddress + ", name=" + name + ", state="
				+ state + ", status=" + status + ", bakType=" + bakType
				+ ", bakId=" + bakId + ", extra=" + extra + ", ratio=" + ratio
				+ "]";
	}
	public String getIndex()
	{
		return index;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public String getAlteonId()
	{
		return alteonId;
	}
	public void setAlteonId(String alteonId)
	{
		this.alteonId = alteonId;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Integer getState()
	{
		return state;
	}
	public void setState(Integer state)
	{
		this.state = state;
	}
	public String getExtra()
	{
		return extra;
	}
	public void setExtra(String extra)
	{
		this.extra = extra;
	}
	public Integer getBakType()
	{
		return bakType;
	}
	public void setBakType(Integer bakType)
	{
		this.bakType = bakType;
	}
	public void setBakId(String bakId)
	{
		this.bakId = bakId;
	}
	public String getBakId()
	{
		return bakId;
	}
	public Integer getRatio()
	{
		return ratio;
	}
	public void setRatio(Integer ratio)
	{
		this.ratio = ratio;
	}
}
