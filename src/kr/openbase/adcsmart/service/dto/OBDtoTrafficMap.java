package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;
import java.util.Date;

public class OBDtoTrafficMap
{
	private Date  occurTime;
	private String vsIndex;
	private String vsName;
	private String vsIPAddress;
	private Integer status;
	private Long bpsIn;// Byte per second. 
	private Long ppsIn;// packet per second. 
	private Long bpsOut;// 
	private Long ppsOut;
	private Long curConns;
	private Long maxConns;
	private Long totConns;	
	private ArrayList<OBDtoTrafficMapVServiceMembers> vserviceList;
	
	@Override
	public String toString()
	{
		return "OBDtoTrafficMap [occurTime=" + occurTime + ", vsIndex="
				+ vsIndex + ", vsName=" + vsName + ", vsIPAddress="
				+ vsIPAddress + ", status=" + status + ", bpsIn=" + bpsIn
				+ ", ppsIn=" + ppsIn + ", bpsOut=" + bpsOut + ", ppsOut="
				+ ppsOut + ", curConns=" + curConns + ", maxConns=" + maxConns
				+ ", totConns=" + totConns + "]";
	}
	
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
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
	public ArrayList<OBDtoTrafficMapVServiceMembers> getVserviceList()
	{
		return vserviceList;
	}
	public void setVserviceList(ArrayList<OBDtoTrafficMapVServiceMembers> vserviceList)
	{
		this.vserviceList = vserviceList;
	} 
}
