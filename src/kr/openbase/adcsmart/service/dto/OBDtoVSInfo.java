package kr.openbase.adcsmart.service.dto;

public class OBDtoVSInfo
{
	private String vsIndex;
	private String vsName;
	private String vsIPAddress;
	private Integer vsPort;
	@Override
	public String toString()
	{
		return "OBDtoVSInfo [vsIndex=" + vsIndex + ", vsName=" + vsName + ", vsIPAddress=" + vsIPAddress + ", vsPort=" + vsPort + "]";
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
	public Integer getVsPort()
	{
		return vsPort;
	}
	public void setVsPort(Integer vsPort)
	{
		this.vsPort = vsPort;
	}
}
