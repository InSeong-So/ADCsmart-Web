package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

import java.sql.Timestamp;

public class OBDtoAdcTimeAlteon
{
	private Timestamp applyTime;
	private Timestamp bootTime;
	private Timestamp saveTime;
	
	@Override
	public String toString()
	{
		return "DtoAdcTimeAlteon [applyTime=" + applyTime + ", bootTime="
				+ bootTime + ", saveTime=" + saveTime + "]";
	}
	public Timestamp getApplyTime()
	{
		return applyTime;
	}
	public void setApplyTime(Timestamp applyTime)
	{
		this.applyTime = applyTime;
	}
	public Timestamp getBootTime()
	{
		return bootTime;
	}
	public void setBootTime(Timestamp bootTime)
	{
		this.bootTime = bootTime;
	}
	public Timestamp getSaveTime()
	{
		return saveTime;
	}
	public void setSaveTime(Timestamp saveTime)
	{
		this.saveTime = saveTime;
	}
}
