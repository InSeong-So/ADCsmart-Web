package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

import java.sql.Timestamp;

public class OBDtoGeneralAlteon
{
	private String 	swVersion;
	private	String	macAddr;
	private	String 	serialNum;
	private String	modelName;
	private String	ipAddr;
	private Timestamp	lastBootTime;
	private	Timestamp	lastApplyTime;
	private	Timestamp	lastSaveTime;
	private String 	upTime;
	@Override
	public String toString()
	{
		return "OBDtoGeneralAlteon [swVersion=" + swVersion + ", macAddr="
				+ macAddr + ", serialNum=" + serialNum + ", modelName="
				+ modelName + ", ipAddr=" + ipAddr + ", lastBootTime="
				+ lastBootTime + ", lastApplyTime=" + lastApplyTime
				+ ", lastSaveTime=" + lastSaveTime + ", upTime=" + upTime + "]";
	}
	public String getSwVersion()
	{
		return swVersion;
	}
	public void setSwVersion(String swVersion)
	{
		this.swVersion = swVersion;
	}
	public String getMacAddr()
	{
		return macAddr;
	}
	public void setMacAddr(String macAddr)
	{
		this.macAddr = macAddr;
	}
	public String getSerialNum()
	{
		return serialNum;
	}
	public void setSerialNum(String serialNum)
	{
		this.serialNum = serialNum;
	}
	public String getModelName()
	{
		return modelName;
	}
	public void setModelName(String modelName)
	{
		this.modelName = modelName;
	}
	public String getIpAddr()
	{
		return ipAddr;
	}
	public void setIpAddr(String ipAddr)
	{
		this.ipAddr = ipAddr;
	}
	public Timestamp getLastBootTime()
	{
		return lastBootTime;
	}
	public void setLastBootTime(Timestamp lastBootTime)
	{
		this.lastBootTime = lastBootTime;
	}
	public Timestamp getLastApplyTime()
	{
		return lastApplyTime;
	}
	public void setLastApplyTime(Timestamp lastApplyTime)
	{
		this.lastApplyTime = lastApplyTime;
	}
	public Timestamp getLastSaveTime()
	{
		return lastSaveTime;
	}
	public void setLastSaveTime(Timestamp lastSaveTime)
	{
		this.lastSaveTime = lastSaveTime;
	}
	public String getUpTime()
	{
		return upTime;
	}
	public void setUpTime(String upTime)
	{
		this.upTime = upTime;
	}
}
