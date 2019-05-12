/*
 * ADC 장비의 시스템 정보.
 */
package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

public class OBDtoAdcSystemInfo
{
	private int adcIndex;
	private String model;
	private String swVersion;
	private String hostName;
	private Timestamp lastBootTime;
	private Timestamp lastSaveTime;
	private Timestamp lastApplyTime;
	private int status;
	private int adcType;

	public int getStatus()
	{
		return this.status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}

	public int getAdcIndex()
	{
		return this.adcIndex;
	}
	public void setAdcIndex(int adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	
	public String getModel()
	{
		return this.model;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	
	public String getSwVersion()
	{
		return this.swVersion;
	}
	public void setSwVersion(String swVersion)
	{
		this.swVersion = swVersion;
	}
	
//	public String getSerialNo()
//	{
//		return this.serialNo;
//	}
//	public void setSerialNo(String serialNo)
//	{
//		this.serialNo = serialNo;
//	}
	
	public String getHostName()
	{
		return this.hostName;
	}
	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}
	
	public Timestamp getLastBootTime()
	{
		return this.lastBootTime;
	}
	public void setLastBootTime(Timestamp lastBootTime)
	{
		this.lastBootTime = lastBootTime;
	}

	public Timestamp getLastApplyTime()
	{
		return this.lastApplyTime;
	}
	public void setLastApplyTime(Timestamp lastApplyTime)
	{
		this.lastApplyTime = lastApplyTime;
	}
	
	public Timestamp getLastSaveTime()
	{
		return this.lastSaveTime;
	}
	public void setLastSaveTime(Timestamp lastSaveTime)
	{
		this.lastSaveTime = lastSaveTime;
	}
	public int getAdcType()
	{
		return adcType;
	}
	public void setAdcType(int adcType)
	{
		this.adcType = adcType;
	}
	@Override
	public String toString()
	{
		return "OBDtoAdcSystemInfo [adcIndex=" + adcIndex + ", model=" + model
				+ ", swVersion=" + swVersion + ", hostName=" + hostName
				+ ", lastBootTime=" + lastBootTime + ", lastSaveTime="
				+ lastSaveTime + ", lastApplyTime=" + lastApplyTime
				+ ", status=" + status + ", adcType=" + adcType + "]";
	}

	
//	public String getOsType()
//	{
//		return this.osType;
//	}
//	public void setOsType(String osType)
//	{
//		this.osType = osType;
//	}
	
//	public int getCpuSize()
//	{
//		return this.cpuSize;
//	}
//	public void setCpuSize(int cpuSize)
//	{
//		this.cpuSize = cpuSize;
//	}
	
//	public int getMemSize()
//	{
//		return this.memSize;
//	}
//	public void setMemSize(int memSize)
//	{
//		this.memSize = memSize;
//	}
	
//	public int getDiskSize()
//	{
//		return this.diskSize;
//	}
//	public void setDiskSize(int diskSize)
//	{
//		this.diskSize = diskSize;
//	}
}
