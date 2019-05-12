package kr.openbase.adcsmart.service.impl.pas.handler.dto;

public class OBDtoSnmpInfoPAS
{
	private	String		hostName	="";

	@Override
	public String toString()
	{
		return "OBDtoSnmpInfoPAS [hostName=" + hostName + "]";
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
