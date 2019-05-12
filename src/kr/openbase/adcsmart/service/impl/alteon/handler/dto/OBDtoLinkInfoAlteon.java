package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoLinkInfoAlteon
{
	private	String 	aliasName;
	private	String	portName;
	private	int		status;
	@Override
	public String toString()
	{
		return "OBDtoLinkInfoAlteon [aliasName=" + aliasName + ", portName=" + portName + ", status=" + status + "]";
	}
	public String getAliasName()
	{
		return aliasName;
	}
	public void setAliasName(String aliasName)
	{
		this.aliasName = aliasName;
	}
	public String getPortName()
	{
		return portName;
	}
	public void setPortName(String portName)
	{
		this.portName = portName;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
}
