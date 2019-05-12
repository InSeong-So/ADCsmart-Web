package kr.openbase.adcsmart.service.dto.report;

public class OBDtoRptVServiceInfo
{
	private Integer adcIndex;
	private String 	adcName;
	private String 	vsName;
	private String 	vsIPAddress;
	private Integer	vsPort;
	private Integer	adcType;
	@Override
	public String toString()
	{
		return "OBDtoRptVServiceInfo [adcIndex=" + adcIndex + ", adcName="
				+ adcName + ", vsName=" + vsName + ", vsIPAddress="
				+ vsIPAddress + ", vsPort=" + vsPort + ", adcType=" + adcType
				+ "]";
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
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
	public Integer getAdcType()
	{
		return adcType;
	}
	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}
}
