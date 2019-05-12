package kr.openbase.adcsmart.service.impl.pask.handler.dto;

public class OBDtoNTPInfoPASK
{
	private int			status;
	private int			interval;
	private	String		primary;
	private	String		secondary;
	private	Integer		timeout;
	@Override
	public String toString()
	{
		return "OBDtoNTPInfoPASK [status=" + status + ", interval=" + interval + ", primary=" + primary + ", secondary=" + secondary + ", timeout=" + timeout + "]";
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public int getInterval()
	{
		return interval;
	}
	public void setInterval(int interval)
	{
		this.interval = interval;
	}
	public String getPrimary()
	{
		return primary;
	}
	public void setPrimary(String primary)
	{
		this.primary = primary;
	}
	public String getSecondary()
	{
		return secondary;
	}
	public void setSecondary(String secondary)
	{
		this.secondary = secondary;
	}
	public Integer getTimeout()
	{
		return timeout;
	}
	public void setTimeout(Integer timeout)
	{
		this.timeout = timeout;
	}
}
