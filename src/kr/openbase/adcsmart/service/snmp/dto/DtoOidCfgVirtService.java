package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidCfgVirtService
{
	private String vsrvID;
	private String vsrvIndex;
	private String vsrvVirtPort;
	private String vsrvRealPort;
	private String vsrvPoolID;
	private String vsrvStatus;
	
	@Override
	public String toString()
	{
		return "DtoOidCfgVirtService [vsrvID=" + vsrvID + ", vsrvIndex="
				+ vsrvIndex + ", vsrvVirtPort=" + vsrvVirtPort
				+ ", vsrvRealPort=" + vsrvRealPort + ", vsrvPoolID="
				+ vsrvPoolID + ", vsrvStatus=" + vsrvStatus + "]";
	}
	public String getVsrvStatus()
	{
		return vsrvStatus;
	}
	public void setVsrvStatus(String vsrvStatus)
	{
		this.vsrvStatus = vsrvStatus;
	}
	public String getVsrvID()
	{
		return vsrvID;
	}
	public void setVsrvID(String vsrvID)
	{
		this.vsrvID = vsrvID;
	}
	public String getVsrvIndex()
	{
		return vsrvIndex;
	}
	public void setVsrvIndex(String vsrvIndex)
	{
		this.vsrvIndex = vsrvIndex;
	}
	public String getVsrvVirtPort()
	{
		return vsrvVirtPort;
	}
	public void setVsrvVirtPort(String vsrvVirtPort)
	{
		this.vsrvVirtPort = vsrvVirtPort;
	}
	public String getVsrvRealPort()
	{
		return vsrvRealPort;
	}
	public void setVsrvRealPort(String vsrvRealPort)
	{
		this.vsrvRealPort = vsrvRealPort;
	}
	public String getVsrvPoolID()
	{
		return vsrvPoolID;
	}
	public void setVsrvPoolID(String vsrvPoolID)
	{
		this.vsrvPoolID = vsrvPoolID;
	}
}
