package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoSystemEnvNetwork
{
	private Date	occurTime;
	private String ipAddress;
	private String netmask;
	private String gateway;
	private String hostName;
	@Override
	public String toString()
	{
		return "OBDtoSystemEnvNetwork [occurTime=" + occurTime + ", ipAddress="
				+ ipAddress + ", netmask=" + netmask + ", gateway=" + gateway
				+ ", hostName=" + hostName + "]";
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public String getNetmask()
	{
		return netmask;
	}
	public void setNetmask(String netmask)
	{
		this.netmask = netmask;
	}
	public String getGateway()
	{
		return gateway;
	}
	public void setGateway(String gateway)
	{
		this.gateway = gateway;
	}
	public String getHostName()
	{
		return hostName;
	}
	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}
}
