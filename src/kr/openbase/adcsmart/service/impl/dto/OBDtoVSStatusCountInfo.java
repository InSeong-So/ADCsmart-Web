package kr.openbase.adcsmart.service.impl.dto;

public class OBDtoVSStatusCountInfo
{
	private Integer vsCount;
	private Integer vsCountAvailable;
	private Integer vsCountUnavailable;
	private Integer vsCountBlocked; //검색 : task:blocked_cancel - 안 쓴다. 
	private Integer vsCountDisabled;
	@Override
	public String toString()
	{
		return String.format("OBDtoVSStatusCountInfo [vsCount=%s, vsCountAvailable=%s, vsCountUnavailable=%s, vsCountBlocked=%s, vsCountDisabled=%s]", vsCount, vsCountAvailable, vsCountUnavailable, vsCountBlocked, vsCountDisabled);
	}
	public Integer getVsCount()
	{
		return vsCount;
	}
	public void setVsCount(Integer vsCount)
	{
		this.vsCount = vsCount;
	}
	public Integer getVsCountAvailable()
	{
		return vsCountAvailable;
	}
	public void setVsCountAvailable(Integer vsCountAvailable)
	{
		this.vsCountAvailable = vsCountAvailable;
	}
	public Integer getVsCountUnavailable()
	{
		return vsCountUnavailable;
	}
	public void setVsCountUnavailable(Integer vsCountUnavailable)
	{
		this.vsCountUnavailable = vsCountUnavailable;
	}
	public Integer getVsCountBlocked()
	{
		return vsCountBlocked;
	}
	public void setVsCountBlocked(Integer vsCountBlocked)
	{
		this.vsCountBlocked = vsCountBlocked;
	}
	public Integer getVsCountDisabled()
	{
		return vsCountDisabled;
	}
	public void setVsCountDisabled(Integer vsCountDisabled)
	{
		this.vsCountDisabled = vsCountDisabled;
	}
}
