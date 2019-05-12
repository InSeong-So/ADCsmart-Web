package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidCfgVirtServer
{
	private String vsID			="";
	private String vsIpAddress	="";
	private String vsName		="";
	private String vsStatus		="";
	private String vsState2		="";
	private String vsVirtPort	="";
	private String vsAvail		="";
	private String vsSrcNwClass ="";

	@Override
	public String toString()
	{
		return "DtoOidCfgVirtServer [vsID=" + vsID + ", vsIpAddress="
				+ vsIpAddress + ", vsName=" + vsName + ", vsStatus=" + vsStatus
				+ ", vsState2=" + vsState2 + ", vsVirtPort=" + vsVirtPort
				+ ", vsAvail=" + vsAvail + ", vsSrcNwClass=" + vsSrcNwClass
				+ "]";
	}
	public String getVsVirtPort()
	{
		return vsVirtPort;
	}
	public void setVsVirtPort(String vsVirtPort)
	{
		this.vsVirtPort = vsVirtPort;
	}
	public String getVsStatus()
	{
		return vsStatus;
	}
	public void setVsStatus(String vsStatus)
	{
		this.vsStatus = vsStatus;
	}
	public String getVsState2()
	{
		return vsState2;
	}
	public void setVsState2(String vsState2)
	{
		this.vsState2 = vsState2;
	}
	public String getVsID()
	{
		return vsID;
	}
	public void setVsID(String vsID)
	{
		this.vsID = vsID;
	}
	public String getVsIpAddress()
	{
		return vsIpAddress;
	}
	public void setVsIpAddress(String vsIpAddress)
	{
		this.vsIpAddress = vsIpAddress;
	}
	public String getVsName()
	{
		return vsName;
	}
	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
	public String getVsAvail()
	{
		return vsAvail;
	}
	public void setVsAvail(String vsAvail)
	{
		this.vsAvail = vsAvail;
	}
	public String getVsSrcNwClass()
	{
		return vsSrcNwClass;
	}
	public void setVsSrcNwClass(String vsSrcNwClass)
	{
		this.vsSrcNwClass = vsSrcNwClass;
	}
}
