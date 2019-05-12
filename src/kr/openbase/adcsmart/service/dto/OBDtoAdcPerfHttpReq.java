package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoAdcPerfHttpReq
{
	private Date	occurTime;
	private Long	httpReqPerSecond;
	@Override
	public String toString()
	{
		return "OBDtoAdcPerfHttpReq [occurTime=" + occurTime
				+ ", httpReqPerSecond=" + httpReqPerSecond + "]";
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Long getHttpReqPerSecond()
	{
		return httpReqPerSecond;
	}
	public void setHttpReqPerSecond(Long httpReqPerSecond)
	{
		this.httpReqPerSecond = httpReqPerSecond;
	}
}
