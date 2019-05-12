package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoDataObj
{
	private Date 	occurTime=null;
	private Long	value=0L; //현재 데이터.
	private Long	diff=0L;//증감 데이터. 

	@Override
	public String toString()
	{
		return "OBDtoDataObj [occurTime=" + occurTime + ", value=" + value
				+ ", diff=" + diff + "]";
	}

	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Long getValue()
	{
		return value;
	}
	public void setValue(Long value)
	{
		this.value = value;
	}
	public Long getDiff()
	{
		return diff;
	}
	public void setDiff(Long diff)
	{
		this.diff = diff;
	}
}
