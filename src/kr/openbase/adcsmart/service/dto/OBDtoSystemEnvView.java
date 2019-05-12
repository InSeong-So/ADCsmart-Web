package kr.openbase.adcsmart.service.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.utility.OBDefine;

public class OBDtoSystemEnvView
{
	private Date	occurTime;
	private Integer logViewCount = 20;// 목록 표시 개수.
	private Integer logViewPeriodType=6;//1: 최근 1시간,  3: 최근 3시간, 6: 최근 6시간, 12: 최근 12시간, 24: 최근 24시간.
	private Integer autoRefrash = 0;//0:disabled, 1: enabled
	private Integer autoLogoutTime = 1200;// 화면 자동 로그 아웃 시간.
	private Integer maxAdcLogExportCnt = OBDefine.SYSENV_MAX_ADCLOG_EXPORT;// ADC 로그 내보내기 최대 개수.
	
	@Override
	public String toString()
	{
		return "OBDtoSystemEnvView [occurTime=" + occurTime + ", logViewCount="
				+ logViewCount + ", logViewPeriodType=" + logViewPeriodType
				+ ", autoRefrash=" + autoRefrash + ", autoLogoutTime="
				+ autoLogoutTime + ", maxAdcLogExportCnt=" + maxAdcLogExportCnt
				+ "]";
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public Integer getMaxAdcLogExportCnt()
	{
		return maxAdcLogExportCnt;
	}
	public void setMaxAdcLogExportCnt(Integer maxAdcLogExportCnt)
	{
		this.maxAdcLogExportCnt = maxAdcLogExportCnt;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public Integer getLogViewCount()
	{
		return logViewCount;
	}
	public void setLogViewCount(Integer logViewCount)
	{
		this.logViewCount = logViewCount;
	}
	public Integer getLogViewPeriodType()
	{
		return logViewPeriodType;
	}
	public void setLogViewPeriodType(Integer logViewPeriodType)
	{
		this.logViewPeriodType = logViewPeriodType;
	}
	public Integer getAutoRefrash()
	{
		return autoRefrash;
	}
	public void setAutoRefrash(Integer autoRefrash)
	{
		this.autoRefrash = autoRefrash;
	}
	public Integer getAutoLogoutTime()
	{
		return autoLogoutTime;
	}
	public void setAutoLogoutTime(Integer autoLogoutTime)
	{
		this.autoLogoutTime = autoLogoutTime;
	}
}
