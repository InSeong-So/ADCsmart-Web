package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoNtpCfgAlteon
{
	private		int		state;
	private		String	server;
	private		String	timeZone;
	private		int		interval;
	@Override
	public String toString()
	{
		return "OBDtoNtpCfgAlteon [state=" + state + ", server=" + server + ", timeZone=" + timeZone + ", interval=" + interval + "]";
	}
	public int getState()
	{
		return state;
	}
	public void setState(int state)
	{
		this.state = state;
	}
	public String getServer()
	{
		return server;
	}
	public void setServer(String server)
	{
		this.server = server;
	}
	public String getTimeZone()
	{
		return timeZone;
	}
	public void setTimeZone(String timeZone)
	{
		this.timeZone = timeZone;
	}
	public int getInterval()
	{
		return interval;
	}
	public void setInterval(int interval)
	{
		this.interval = interval;
	}
}
