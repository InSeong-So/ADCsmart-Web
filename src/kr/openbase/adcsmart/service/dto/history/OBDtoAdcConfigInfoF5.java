package kr.openbase.adcsmart.service.dto.history;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;

public class OBDtoAdcConfigInfoF5
{
	private String		vsName;
	private String		vsIPAddress;
	private Integer 	vsServicePort;
	private String		vsPersistenceName;
	private Integer 	vsUseYN;
	private OBDtoAdcPoolF5 pool;
	private String		state;
	
	private Date		lastTime;
	private String		summary;//변경사항
	
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigInfoF5 [vsName=" + vsName + ", vsIPAddress=" + vsIPAddress + 
				", vsServicePort=" + vsServicePort + ", vsPersistenceName=" + vsPersistenceName + 
				", vsUseYN=" + vsUseYN + ", pool=" + pool + ", state=" + state + ", lastTime=" + 
				lastTime + ", summary=" + summary + "]";
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

	public Integer getVsServicePort()
	{
		return vsServicePort;
	}

	public void setVsServicePort(Integer vsServicePort)
	{
		this.vsServicePort = vsServicePort;
	}

	public String getVsPersistenceName()
	{
		return vsPersistenceName;
	}

	public void setVsPersistenceName(String vsPersistenceName)
	{
		this.vsPersistenceName = vsPersistenceName;
	}

	public Integer getVsUseYN()
	{
		return vsUseYN;
	}

	public void setVsUseYN(Integer vsUseYN)
	{
		this.vsUseYN = vsUseYN;
	}

	public OBDtoAdcPoolF5 getPool()
	{
		return pool;
	}

	public void setPool(OBDtoAdcPoolF5 pool)
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

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}
}
