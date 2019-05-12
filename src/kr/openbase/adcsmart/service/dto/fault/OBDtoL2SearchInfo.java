package kr.openbase.adcsmart.service.dto.fault;

public class OBDtoL2SearchInfo
{
	private Integer adcIndex;
	private String ipAddress;
	private String macAddress;
	private String vlanInfo;
	private String interfaceInfo;
	@Override
	public String toString()
	{
		return String.format("OBDtoL2SearchInfo [adcIndex=%s, ipAddress=%s, macAddress=%s, vlanInfo=%s, interfaceInfo=%s]", adcIndex, ipAddress, macAddress, vlanInfo, interfaceInfo);
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
	public String getMacAddress()
	{
		return macAddress;
	}
	public void setMacAddress(String macAddress)
	{
		this.macAddress = macAddress;
	}
	public String getVlanInfo()
	{
		return vlanInfo;
	}
	public void setVlanInfo(String vlanInfo)
	{
		this.vlanInfo = vlanInfo;
	}
	public String getInterfaceInfo()
	{
		return interfaceInfo;
	}
	public void setInterfaceInfo(String interfaceInfo)
	{
		this.interfaceInfo = interfaceInfo;
	}
}
