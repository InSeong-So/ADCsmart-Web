package kr.openbase.adcsmart.service.utility;

import java.util.Arrays;

public class OBDtoNetworkExt
{
	private String ifName;
	private String ipAddress;
	private byte [] ipAddr;
	private String macAddress;
	private byte [] macAddr;
	
	@Override
	public String toString()
	{
		return "OBDtoNetworkExt [ifName=" + ifName + ", ipAddress=" + ipAddress
				+ ", ipAddr=" + Arrays.toString(ipAddr) + ", macAddress="
				+ macAddress + ", macAddr=" + Arrays.toString(macAddr) + "]";
	}

	public String getIfName()
	{
		return ifName;
	}
	public void setIfName(String ifName)
	{
		this.ifName = ifName;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public byte[] getIpAddr()
	{
		return ipAddr;
	}
	public void setIpAddr(byte[] ipAddr)
	{
		this.ipAddr = ipAddr;
	}
	public String getMacAddress()
	{
		return macAddress;
	}
	public void setMacAddress(String macAddress)
	{
		this.macAddress = macAddress;
	}
	public byte[] getMacAddr()
	{
		return macAddr;
	}
	public void setMacAddr(byte[] macAddr)
	{
		this.macAddr = macAddr;
	}
}
