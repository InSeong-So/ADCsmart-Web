package kr.openbase.adcsmart.service.dto.fault;

import java.util.Date;

public class OBDtoFlbSlbSession
{
	private Date 	occurTime=null;
	private Long	flbSession=0L;
	private Long	slbSession=0L;
	@Override
	public String toString()
	{
		return "OBDtoFlbSlbSession [occurTime=" + occurTime + ", flbSession="
				+ flbSession + ", slbSession=" + slbSession + "]";
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Long getFlbSession()
	{
		return flbSession;
	}
	public void setFlbSession(Long flbSession)
	{
		this.flbSession = flbSession;
	}
	public Long getSlbSession()
	{
		return slbSession;
	}
	public void setSlbSession(Long slbSession)
	{
		this.slbSession = slbSession;
	}
}
