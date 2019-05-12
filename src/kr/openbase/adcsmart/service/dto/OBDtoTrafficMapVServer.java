package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoTrafficMapVServer
{
	private Date  occurTime;
	private String vsIndex;
	private String vsName;
	private String vsIPAddress;
	private Integer srvPort;
	private Integer status;
	private Long bpsIn;// Byte per second. 
	private Long ppsIn;// packet per second.
	private Long bpsSum;
	private Long bpsOut;// 
	private Long ppsOut;
	private Long ppsSum;
	private Long curConns;
	private Long maxConns;
	private Long totConns;
	private String dispName="";
	
	@Override
	public String toString()
	{
		return "OBDtoTrafficMapVServer [occurTime=" + occurTime + ", vsIndex="
				+ vsIndex + ", vsName=" + vsName + ", vsIPAddress="
				+ vsIPAddress + ", srvPort=" + srvPort + ", status=" + status
				+ ", bpsIn=" + bpsIn + ", ppsIn=" + ppsIn + ", bpsSum="
				+ bpsSum + ", bpsOut=" + bpsOut + ", ppsOut=" + ppsOut
				+ ", ppsSum=" + ppsSum + ", curConns=" + curConns
				+ ", maxConns=" + maxConns + ", totConns=" + totConns
				+ ", dispName=" + dispName + "]";
	}
	public String getDispName()
	{
		return dispName;
	}
	public void setDispName(String dispName)
	{
		this.dispName = dispName;
	}
	public Integer getSrvPort()
	{
		return srvPort;
	}
	public void setSrvPort(Integer srvPort)
	{
		this.srvPort = srvPort;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public Long getBpsSum()
	{
		return bpsSum;
	}
	public void setBpsSum(Long bpsSum)
	{
		this.bpsSum = bpsSum;
	}
	public Long getPpsSum()
	{
		return ppsSum;
	}
	public void setPpsSum(Long ppsSum)
	{
		this.ppsSum = ppsSum;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
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
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public Long getBpsIn()
	{
		return bpsIn;
	}
	public void setBpsIn(Long bpsIn)
	{
		this.bpsIn = bpsIn;
	}
	public Long getPpsIn()
	{
		return ppsIn;
	}
	public void setPpsIn(Long ppsIn)
	{
		this.ppsIn = ppsIn;
	}
	public Long getBpsOut()
	{
		return bpsOut;
	}
	public void setBpsOut(Long bpsOut)
	{
		this.bpsOut = bpsOut;
	}
	public Long getPpsOut()
	{
		return ppsOut;
	}
	public void setPpsOut(Long ppsOut)
	{
		this.ppsOut = ppsOut;
	}
	public Long getCurConns()
	{
		return curConns;
	}
	public void setCurConns(Long curConns)
	{
		this.curConns = curConns;
	}
	public Long getMaxConns()
	{
		return maxConns;
	}
	public void setMaxConns(Long maxConns)
	{
		this.maxConns = maxConns;
	}
	public Long getTotConns()
	{
		return totConns;
	}
	public void setTotConns(Long totConns)
	{
		this.totConns = totConns;
	}
}
