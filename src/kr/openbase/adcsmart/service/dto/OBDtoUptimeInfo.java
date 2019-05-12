package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;

public class OBDtoUptimeInfo
{
	public final static Integer BOOT_TYPE_TELNET = 1;// reset from Telnet
	public final static Integer BOOT_TYPE_PANIC  = 2;// software PANIC
	public final static Integer BOOT_TYPE_RESET  = 3;// console RESET KEY
	public final static Integer BOOT_TYPE_POWER_CYCLE= 4;// power cycle
	
	private Timestamp	currentTime;
	private Integer 	bootType;
	
	@Override
	public String toString()
	{
		return String.format("OBDtoUptimeInfo [currentTime=%s, bootType=%s]", currentTime, bootType);
	}
	public Timestamp getCurrentTime()
	{
		return currentTime;
	}
	public void setCurrentTime(Timestamp currentTime)
	{
		this.currentTime = currentTime;
	}
	public Integer getBootType()
	{
		return bootType;
	}
	public void setBootType(Integer bootType)
	{
		this.bootType = bootType;
	}
}
