package kr.openbase.adcsmart.service.dto.history;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;

public class OBDtoAdcConfigInfoAlteon
{
	private String 	alteonId;
	private String		vsName;
	private String		vsIPAddress;
	private Integer 	vsUseYN;
	//private boolean	vrrpYN;
	private Integer		vrrpState; //vrrpYN을 대체
	private Integer		ifNum;
	private String		state;
	private ArrayList	<OBDtoAdcVService> vserviceList;
	
	private Date		lastTime;
	private String		summary;//변경사항

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigInfoAlteon [alteonId=" + alteonId + ", vsName=" 
				+ vsName + ", vsIPAddress=" + vsIPAddress + ", vsUseYN=" + vsUseYN 
				+ ", vrrpState=" + vrrpState + ", ifNum=" + ifNum + ", state=" 
				+ state + ", vserviceList=" + vserviceList + ", lastTime=" 
				+ lastTime + ", summary=" + summary + "]";
	}

	public String getAlteonId()
	{
		return alteonId;
	}
	public void setAlteonId(String alteonId)
	{
		this.alteonId = alteonId;
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
	public Integer getVsUseYN()
	{
		return vsUseYN;
	}
	public void setVsUseYN(Integer vsUseYN)
	{
		this.vsUseYN = vsUseYN;
	}
	public Integer getVrrpState()
	{
		return vrrpState;
	}
	public void setVrrpState(Integer vrrpState)
	{
		this.vrrpState = vrrpState;
	}
	public Integer getIfNum()
	{
		return ifNum;
	}
	public void setIfNum(Integer ifNum)
	{
		this.ifNum = ifNum;
	}
	public ArrayList<OBDtoAdcVService> getVserviceList()
	{
		return vserviceList;
	}
	public void setVserviceList(ArrayList<OBDtoAdcVService> vserviceList)
	{
		this.vserviceList = vserviceList;
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
