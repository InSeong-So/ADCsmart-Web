package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoAdcPerfSslTrans
{
	private Date	occurTime;
	private Long	sslTransPerSecond;
	@Override
	public String toString()
	{
		return "OBDtoAdcPerfSslTrans [occurTime=" + occurTime
				+ ", sslTransPerSecond=" + sslTransPerSecond + "]";
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Long getSslTransPerSecond()
	{
		return sslTransPerSecond;
	}
	public void setSslTransPerSecond(Long sslTransPerSecond)
	{
		this.sslTransPerSecond = sslTransPerSecond;
	}
}
