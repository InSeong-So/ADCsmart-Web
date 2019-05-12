package kr.openbase.adcsmart.service.snmp.dto;

public class DtoRptSyslog
{
	private String host;
	private String facility;
	private int severity;
	@Override
	public String toString()
	{
		return "DtoRptSyslog [host=" + host + ", facility=" + facility
				+ ", severity=" + severity + "]";
	}
	public String getHost()
	{
		return host;
	}
	public void setHost(String host)
	{
		this.host = host;
	}
	public String getFacility()
	{
		return facility;
	}
	public void setFacility(String facility)
	{
		this.facility = facility;
	}
	public int getSeverity()
	{
		return severity;
	}
	public void setSeverity(int severity)
	{
		this.severity = severity;
	}
}
