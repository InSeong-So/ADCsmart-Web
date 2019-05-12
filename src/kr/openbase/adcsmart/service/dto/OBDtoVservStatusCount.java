package kr.openbase.adcsmart.service.dto;

public class OBDtoVservStatusCount
{
	private Integer vs;
	private Integer vsAvail;
	private Integer vsDisable;
	private Integer vsUnavail;
	private Integer vsUnavailLessNDays;
	private Integer vsUnavailOverNDays;
	public OBDtoVservStatusCount()
	{
		vs = 0;
		vsAvail = 0;
		vsDisable = 0;
		vsUnavail = 0;
		vsUnavailOverNDays = 0;
	}
	public Integer getVs()
	{
		return vs;
	}
	public void setVs(Integer vs)
	{
		this.vs = vs;
	}
	public Integer getVsAvail()
	{
		return vsAvail;
	}
	public void setVsAvail(Integer vsAvail)
	{
		this.vsAvail = vsAvail;
	}
	public Integer getVsDisable()
	{
		return vsDisable;
	}
	public void setVsDisable(Integer vsDisable)
	{
		this.vsDisable = vsDisable;
	}
	public Integer getVsUnavail()
	{
		return vsUnavail;
	}
	public void setVsUnavail(Integer vsUnavail)
	{
		this.vsUnavail = vsUnavail;
	}
	public Integer getVsUnavailLessNDays()
	{
		return vsUnavailLessNDays;
	}
	public void setVsUnavailLessNDays(Integer vsUnavailLessNDays)
	{
		this.vsUnavailLessNDays = vsUnavailLessNDays;
	}
	public Integer getVsUnavailOverNDays()
	{
		return vsUnavailOverNDays;
	}
	public void setVsUnavailOverNDays(Integer vsUnavailOverNDays)
	{
		this.vsUnavailOverNDays = vsUnavailOverNDays;
	}
	@Override
	public String toString()
	{
		return "OBDtoVservStatusCount [vs=" + vs + ", vsAvail=" + vsAvail
				+ ", vsDisable=" + vsDisable + ", vsUnavail=" + vsUnavail
				+ ", vsUnavailLessNDays=" + vsUnavailLessNDays
				+ ", vsUnavailOverNDays=" + vsUnavailOverNDays + "]";
	}
}
