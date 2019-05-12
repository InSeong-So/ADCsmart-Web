package kr.openbase.adcsmart.service.snmp.dto;

public class DtoRptNtp
{
	private String name;
	private String interval;
	private String timezone;
	private Integer enabled;

	@Override
	public String toString()
	{
		return "DtoRptNtp [name=" + name + ", interval=" + interval
				+ ", timezone=" + timezone + ", enabled=" + enabled + "]";
	}
	public Integer getEnabled()
	{
		return enabled;
	}
	public void setEnabled(Integer enabled)
	{
		this.enabled = enabled;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getInterval()
	{
		return interval;
	}
	public void setInterval(String interval)
	{
		this.interval = interval;
	}
	public String getTimezone()
	{
		return timezone;
	}
	public void setTimezone(String timezone)
	{
		this.timezone = timezone;
	}
}
