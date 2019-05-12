package kr.openbase.adcsmart.service.dto.report;

public class OBDtoRptAdcInfo
{
	private	Integer 	index;
	private 	String		iPAddress;
	private 	String 	name;
	private	String 	hostName;
	private	String  	model;
	private	String		osVersion;
	private	String		license;
	private 	String  	serialNum;
	private 	String  	macAddress;
	private 	String  	activeStandby;
	private 	String  	runTime;//일..
	@Override
	public String toString()
	{
		return "OBDtoRptAdcInfo [index=" + index + ", iPAddress=" + iPAddress
				+ ", name=" + name + ", hostName=" + hostName + ", model="
				+ model + ", osVersion=" + osVersion + ", license=" + license
				+ ", serialNum=" + serialNum + ", macAddress=" + macAddress
				+ ", activeStandby=" + activeStandby + ", runTime=" + runTime
				+ "]";
	}
	public Integer getIndex()
	{
		return index;
	}
	public void setIndex(Integer index)
	{
		this.index = index;
	}
	public String getiPAddress()
	{
		return iPAddress;
	}
	public void setiPAddress(String iPAddress)
	{
		this.iPAddress = iPAddress;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getHostName()
	{
		return hostName;
	}
	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}
	public String getModel()
	{
		return model;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	public String getOsVersion()
	{
		return osVersion;
	}
	public void setOsVersion(String osVersion)
	{
		this.osVersion = osVersion;
	}
	public String getLicense()
	{
		return license;
	}
	public void setLicense(String license)
	{
		this.license = license;
	}
	public String getSerialNum()
	{
		return serialNum;
	}
	public void setSerialNum(String serialNum)
	{
		this.serialNum = serialNum;
	}
	public String getMacAddress()
	{
		return macAddress;
	}
	public void setMacAddress(String macAddress)
	{
		this.macAddress = macAddress;
	}
	public String getActiveStandby()
	{
		return activeStandby;
	}
	public void setActiveStandby(String activeStandby)
	{
		this.activeStandby = activeStandby;
	}
	public String getRunTime()
	{
		return runTime;
	}
	public void setRunTime(String runTime)
	{
		this.runTime = runTime;
	}
}
