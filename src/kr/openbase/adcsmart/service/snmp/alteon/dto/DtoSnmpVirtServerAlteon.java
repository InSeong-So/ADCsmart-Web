package kr.openbase.adcsmart.service.snmp.alteon.dto;

import java.util.ArrayList;

public class DtoSnmpVirtServerAlteon
{
	private String vsID;
	private String  vsIpAddress;
	private Integer vsState1;// 설정된 상태.
	private Integer vsStatus;// 운영중인 상태.
	private String  vsName;
	private ArrayList<String>  vsNwClassList;
	
	@Override
	public String toString()
	{
		return "DtoSnmpVirtServerAlteon [vsID=" + vsID + ", vsIpAddress="
				+ vsIpAddress + ", vsState1=" + vsState1 + ", vsStatus="
				+ vsStatus + ", vsName=" + vsName + ", vsNwClass=" + vsNwClassList
				+ "]";
	}
	public String getVsID()
	{
		return vsID;
	}
	public Integer getVsStatus()
	{
		return vsStatus;
	}
	public void setVsStatus(Integer vsStatus)
	{
		this.vsStatus = vsStatus;
	}
	public void setVsID(String vsID)
	{
		this.vsID = vsID;
	}
	public String getVsIpAddress()
	{
		return vsIpAddress;
	}
	public void setVsIpAddress(String vsIpAddress)
	{
		this.vsIpAddress = vsIpAddress;
	}
	public Integer getVsState1()
	{
		return vsState1;
	}
	public void setVsState1(Integer vsState)
	{
		this.vsState1 = vsState;
	}
	public String getVsName()
	{
		return vsName;
	}
	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
	public ArrayList<String> getVsNwClassList()
	{
		return vsNwClassList;
	}
	public void setVsNwClassList(ArrayList<String> vsNwClassList)
	{
		this.vsNwClassList = vsNwClassList;
	}
}
