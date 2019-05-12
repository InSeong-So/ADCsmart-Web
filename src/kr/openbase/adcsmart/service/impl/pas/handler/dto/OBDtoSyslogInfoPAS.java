package kr.openbase.adcsmart.service.impl.pas.handler.dto;

public class OBDtoSyslogInfoPAS
{
	private	int		status		=0;
	private	int		index 		=0;
	private	String 	ipAddress	="";
	private String	facility	="";
	private String	level		="";
	@Override
	public String toString()
	{
		return "OBDtoSyslogInfoPAS [status=" + status + ", index=" + index
				+ ", ipAddress=" + ipAddress + ", facility=" + facility
				+ ", level=" + level + "]";
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public int getIndex()
	{
		return index;
	}
	public void setIndex(int index)
	{
		this.index = index;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public String getFacility()
	{
		return facility;
	}
	public void setFacility(String facility)
	{
		this.facility = facility;
	}
	public String getLevel()
	{
		return level;
	}
	public void setLevel(String level)
	{
		this.level = level;
	}
}
