package kr.openbase.adcsmart.service.impl.dto;

public class OBHostInfo
{
	private String ipAddress;
	private Integer port=0;
	@Override
	public String toString()
	{
		return String.format("OBHostInfo [ipAddress=%s, port=%s]", ipAddress, port);
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public Integer getPort()
	{
		return port;
	}
	public void setPort(Integer port)
	{
		this.port = port;
	}
}
