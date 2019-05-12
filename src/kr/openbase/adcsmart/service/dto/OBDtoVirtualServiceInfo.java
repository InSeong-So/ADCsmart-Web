package kr.openbase.adcsmart.service.dto;

public class OBDtoVirtualServiceInfo
{
	private Integer adcType;
	private Integer adcIndex;
	private String  ipAddress;
	private Integer svcPort;
	private String  svcIndex;
	private String  svcName;
	
	@Override
	public String toString()
	{
		return String.format("OBDtoVirtualServiceInfo [adcType=%s, adcIndex=%s, ipAddress=%s, svcPort=%s, svcIndex=%s, svcName=%s]", adcType, adcIndex, ipAddress, svcPort, svcIndex, svcName);
	}
	public String getSvcName()
	{
		return svcName;
	}
	public void setSvcName(String svcName)
	{
		this.svcName = svcName;
	}
	public Integer getAdcType()
	{
		return adcType;
	}
	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public Integer getSvcPort()
	{
		return svcPort;
	}
	public void setSvcPort(Integer svcPort)
	{
		this.svcPort = svcPort;
	}
	public String getSvcIndex()
	{
		return svcIndex;
	}
	public void setSvcIndex(String svcIndex)
	{
		this.svcIndex = svcIndex;
	}
}
