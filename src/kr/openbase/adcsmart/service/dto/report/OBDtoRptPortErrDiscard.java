package kr.openbase.adcsmart.service.dto.report;

import java.util.Date;

public class OBDtoRptPortErrDiscard
{
	private Date occurTime;
	private Long	errors;
	private Long 	discards;
	@Override
	public String toString()
	{
		return "OBDtoRptPortErrDiscard [occurTime=" + occurTime + ", errors="
				+ errors + ", discards=" + discards + "]";
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Long getErrors()
	{
		return errors;
	}
	public void setErrors(Long errors)
	{
		this.errors = errors;
	}
	public Long getDiscards()
	{
		return discards;
	}
	public void setDiscards(Long discards)
	{
		this.discards = discards;
	}
}
