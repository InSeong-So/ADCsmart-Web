package kr.openbase.adcsmart.service.dto.history;

import java.util.Date;

import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;

public class OBDtoAdcConfigInfoPASK
{
	private String		vsName;
	private String		vsIPAddress;
	private Integer 	vsSrvProtocol;
	private Integer 	vsSrvPort;
	private Integer 	state;
	private String		subInfo;
	private OBDtoAdcPoolPASK pool;
	
	private Date		lastTime;
	private String		summary;//변경사항

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigInfoPASK [vsName=" + vsName + ", vsIPAddress="
				+ vsIPAddress + ", vsSrvProtocol=" + vsSrvProtocol
				+ ", vsSrvPort=" + vsSrvPort + ", state=" + state
				+ ", subInfo=" + subInfo + ", pool=" + pool + ", lastTime="
				+ lastTime + ", summary=" + summary + "]";
	}
	public String getVsName()
	{
		return vsName;
	}
	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
	public String getVsIPAddress()
	{
		return vsIPAddress;
	}
	public void setVsIPAddress(String vsIPAddress)
	{
		this.vsIPAddress = vsIPAddress;
	}
	public Integer getVsSrvProtocol()
	{
		return vsSrvProtocol;
	}
	public void setVsSrvProtocol(Integer vsSrvProtocol)
	{
		this.vsSrvProtocol = vsSrvProtocol;
	}
	public Integer getVsSrvPort()
	{
		return vsSrvPort;
	}
	public void setVsSrvPort(Integer vsSrvPort)
	{
		this.vsSrvPort = vsSrvPort;
	}
	public Integer getState()
	{
		return state;
	}
	public void setState(Integer state)
	{
		this.state = state;
	}
	public OBDtoAdcPoolPASK getPool()
	{
		return pool;
	}
	public void setPool(OBDtoAdcPoolPASK pool)
	{
		this.pool = pool;
	}
	public Date getLastTime()
	{
		return lastTime;
	}
	public void setLastTime(Date lastTime)
	{
		this.lastTime = lastTime;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	public String getSubInfo()
	{
		return subInfo;
	}
	public void setSubInfo(String subInfo)
	{
		this.subInfo = subInfo;
	}
}
