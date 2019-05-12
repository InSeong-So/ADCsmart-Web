package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidInfoHealthCheck
{
	private String slbHealthID="";
	private String slbHealthName="";
	private String slbHealthType="";
	private String slbHealthTimeOut="";
	private String slbHealthInterval="";
	private String slbHealthDestinationIp=""; //Alteon v29
	private String slbHealthLogicalExpression=""; //Alteon v29
	private String slbHealthState="";
	private String slbHealthSendMsg="";

	@Override
	public String toString()
	{
		return "DtoOidInfoHealthCheck [slbHealthID=" + slbHealthID
				+ ", slbHealthName=" + slbHealthName + ", slbHealthType="
				+ slbHealthType + ", slbHealthTimeOut=" + slbHealthTimeOut
				+ ", slbHealthInterval=" + slbHealthInterval
				+ ", slbHealthDestinationIp=" + slbHealthDestinationIp
				+ ", slbHealthLogicalExpression=" + slbHealthLogicalExpression
				+ ", slbHealthState=" + slbHealthState + ", slbHealthSendMsg="
				+ slbHealthSendMsg + "]";
	}
	public String getSlbHealthID()
	{
		return slbHealthID;
	}
	public void setSlbHealthID(String slbHealthID)
	{
		this.slbHealthID = slbHealthID;
	}
	public String getSlbHealthType()
	{
		return slbHealthType;
	}
	public void setSlbHealthType(String slbHealthType)
	{
		this.slbHealthType = slbHealthType;
	}
	public String getSlbHealthTimeOut()
	{
		return slbHealthTimeOut;
	}
	public void setSlbHealthTimeOut(String slbHealthTimeOut)
	{
		this.slbHealthTimeOut = slbHealthTimeOut;
	}
	public String getSlbHealthInterval()
	{
		return slbHealthInterval;
	}
	public void setSlbHealthInterval(String slbHealthInterval)
	{
		this.slbHealthInterval = slbHealthInterval;
	}
	public String getSlbHealthState()
	{
		return slbHealthState;
	}
	public void setSlbHealthState(String slbHealthState)
	{
		this.slbHealthState = slbHealthState;
	}
	public String getSlbHealthSendMsg()
	{
		return slbHealthSendMsg;
	}
	public void setSlbHealthSendMsg(String slbHealthSendMsg)
	{
		this.slbHealthSendMsg = slbHealthSendMsg;
	}
	public String getSlbHealthName()
	{
		return slbHealthName;
	}
	public void setSlbHealthName(String slbHealthName)
	{
		this.slbHealthName = slbHealthName;
	}
	public String getSlbHealthDestinationIp()
	{
		return slbHealthDestinationIp;
	}
	public void setSlbHealthDestinationIp(String slbHealthDestinationIp)
	{
		this.slbHealthDestinationIp = slbHealthDestinationIp;
	}
	public String getSlbHealthLogicalExpression()
	{
		return slbHealthLogicalExpression;
	}
	public void setSlbHealthLogicalExpression(String slbHealthLogicalExpression)
	{
		this.slbHealthLogicalExpression = slbHealthLogicalExpression;
	}
}
