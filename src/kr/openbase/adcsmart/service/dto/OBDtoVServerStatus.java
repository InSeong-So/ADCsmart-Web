package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoVServerStatus
{
	private int alteonID;//for alteon
	private String vsName;//for f5
	private String vsIndex;
	private Integer status;// 0: unknown, 1: available, 2: unavailable, 3: Offline
	
	private ArrayList<OBDtoVServiceStatus> srvList;
	
	@Override
	public String toString()
	{
		return "OBDtoVServerStatus [alteonID=" + alteonID + ", vsName="
				+ vsName + ", vsIndex=" + vsIndex + ", status=" + status
				+ ", srvList=" + srvList + "]";
	}

	public ArrayList<OBDtoVServiceStatus> getSrvList()
	{
		return srvList;
	}

	public void setSrvList(ArrayList<OBDtoVServiceStatus> srvList)
	{
		this.srvList = srvList;
	}

	public int getAlteonID()
	{
		return alteonID;
	}

	public void setAlteonID(int alteonID)
	{
		this.alteonID = alteonID;
	}

	public String getVsName()
	{
		return vsName;
	}
	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
}
