package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoDashboardSdsVservStatus
{
	private Date occurTime;
	private Integer VsAvail;    //정상
	private Integer VsUnavail;  //단절
	private Integer VsDisable;  //꺼짐
	
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Integer getVsAvail()
	{
		return VsAvail;
	}
	public void setVsAvail(Integer vsAvail)
	{
		VsAvail = vsAvail;
	}
	public Integer getVsUnavail()
	{
		return VsUnavail;
	}
	public void setVsUnavail(Integer vsUnavail)
	{
		VsUnavail = vsUnavail;
	}
	public Integer getVsDisable()
	{
		return VsDisable;
	}
	public void setVsDisable(Integer vsDisable)
	{
		VsDisable = vsDisable;
	}
	@Override
	public String toString()
	{
		return "OBDtoDashboardSdsVservStatus [occurTime=" + occurTime
				+ ", VsAvail=" + VsAvail + ", VsUnavail=" + VsUnavail
				+ ", VsDisable=" + VsDisable + "]";
	}
}
