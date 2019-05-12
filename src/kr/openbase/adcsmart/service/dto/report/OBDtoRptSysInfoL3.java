package kr.openbase.adcsmart.service.dto.report;

public class OBDtoRptSysInfoL3
{
	private		OBDtoRptSysInfo 	interfaceInfo;
	private		OBDtoRptSysInfo 	gatewayInfo;
	@Override
	public String toString()
	{
		return "OBDtoRptSysInfoL3 [interfaceInfo=" + interfaceInfo
				+ ", gatewayInfo=" + gatewayInfo + "]";
	}
	public OBDtoRptSysInfo getInterfaceInfo()
	{
		return interfaceInfo;
	}
	public void setInterfaceInfo(OBDtoRptSysInfo interfaceInfo)
	{
		this.interfaceInfo = interfaceInfo;
	}
	public OBDtoRptSysInfo getGatewayInfo()
	{
		return gatewayInfo;
	}
	public void setGatewayInfo(OBDtoRptSysInfo gatewayInfo)
	{
		this.gatewayInfo = gatewayInfo;
	}
}
