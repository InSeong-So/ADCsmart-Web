package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidRptOPGen
{
	private String hostName;
	private String modelName;
	private String osVersion;
	private String licenseKey;
	private String licenseType;
	private String serialNum;
	private String ipAddress;
	private String macAddress;
	private String upTime;
	private String activeStandby;
	@Override
	public String toString()
	{
		return "DtoOidRptOPGen [hostName=" + hostName + ", modelName="
				+ modelName + ", osVersion=" + osVersion + ", licenseKey="
				+ licenseKey + ", licenseType=" + licenseType + ", serialNum="
				+ serialNum + ", ipAddress=" + ipAddress + ", macAddress="
				+ macAddress + ", upTime=" + upTime + ", activeStandby="
				+ activeStandby + "]";
	}
	public String getHostName()
	{
		return hostName;
	}
	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}
	public String getModelName()
	{
		return modelName;
	}
	public void setModelName(String modelName)
	{
		this.modelName = modelName;
	}
	public String getOsVersion()
	{
		return osVersion;
	}
	public void setOsVersion(String osVersion)
	{
		this.osVersion = osVersion;
	}
	public String getLicenseKey()
	{
		return licenseKey;
	}
	public void setLicenseKey(String licenseKey)
	{
		this.licenseKey = licenseKey;
	}
	public String getLicenseType()
	{
		return licenseType;
	}
	public void setLicenseType(String licenseType)
	{
		this.licenseType = licenseType;
	}
	public String getSerialNum()
	{
		return serialNum;
	}
	public void setSerialNum(String serialNum)
	{
		this.serialNum = serialNum;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public String getMacAddress()
	{
		return macAddress;
	}
	public void setMacAddress(String macAddress)
	{
		this.macAddress = macAddress;
	}
	public String getUpTime()
	{
		return upTime;
	}
	public void setUpTime(String upTime)
	{
		this.upTime = upTime;
	}
	public String getActiveStandby()
	{
		return activeStandby;
	}
	public void setActiveStandby(String activeStandby)
	{
		this.activeStandby = activeStandby;
	}
}
