package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoSyslogCfgAlteon
{
	private		String	serverIP;
	private		String	severity;
	private		String	facility;
	@Override
	public String toString()
	{
		return "OBDtoSyslogCfgAlteon [serverIP=" + serverIP + ", severity=" + severity + ", facility=" + facility + "]";
	}
	public String getServerIP()
	{
		return serverIP;
	}
	public void setServerIP(String serverIP)
	{
		this.serverIP = serverIP;
	}
	public String getSeverity()
	{
		return severity;
	}
	public void setSeverity(String severity)
	{
		this.severity = severity;
	}
	public String getFacility()
	{
		return facility;
	}
	public void setFacility(String facility)
	{
		this.facility = facility;
	}	
}
