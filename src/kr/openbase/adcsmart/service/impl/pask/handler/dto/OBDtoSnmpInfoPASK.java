package kr.openbase.adcsmart.service.impl.pask.handler.dto;

public class OBDtoSnmpInfoPASK
{
	private	String		hostName	="";
	@Override
	public String toString()
	{
		return "OBDtoSnmpInfoPASK [hostName=" + hostName + "]";
	}
	public String getHostName()
	{
		return hostName;
	}
	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}
}
