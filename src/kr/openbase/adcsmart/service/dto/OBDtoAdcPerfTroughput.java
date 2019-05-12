package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoAdcPerfTroughput
{
	private Date	occurTime;
	private Long	pps;
	private Long	bps;
	@Override
	public String toString()
	{
		return "OBDtoAdcPerfTroughput [occurTime=" + occurTime + ", pps=" + pps
				+ ", bps=" + bps + "]";
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Long getPps()
	{
		return pps;
	}
	public void setPps(Long pps)
	{
		this.pps = pps;
	}
	public Long getBps()
	{
		return bps;
	}
	public void setBps(Long bps)
	{
		this.bps = bps;
	}
}
