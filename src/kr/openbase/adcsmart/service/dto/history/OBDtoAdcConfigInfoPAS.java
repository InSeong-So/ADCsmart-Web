package kr.openbase.adcsmart.service.dto.history;

import java.util.Date;

import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;

public class OBDtoAdcConfigInfoPAS
{
	private String		vsName;
	private String		vsIPAddress;
	private Integer 	vsSrvProtocol;
	private Integer 	vsSrvPort;
	private Integer 	state;

	private OBDtoAdcPoolPAS pool;
	
	private Date		lastTime;
	private String		summary;//변경사항
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigInfoPAS [vsName=" + vsName + ", vsIPAddress="
				+ vsIPAddress + ", vsSrvProtocol=" + vsSrvProtocol
				+ ", vsSrvPort=" + vsSrvPort + ", state=" + state + ", pool="
				+ pool + ", lastTime=" + lastTime + ", summary=" + summary
				+ "]";
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
	public OBDtoAdcPoolPAS getPool()
	{
		return pool;
	}
	public void setPool(OBDtoAdcPoolPAS pool)
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
}
