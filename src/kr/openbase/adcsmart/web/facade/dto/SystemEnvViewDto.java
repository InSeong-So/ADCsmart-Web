package kr.openbase.adcsmart.web.facade.dto;

import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvView;

public class SystemEnvViewDto {
	private Date	occurTime;
	private Integer logViewCount;// 목록 표시 개수.
	private Integer logViewPeriodType;//1: 최근 1시간,  3: 최근 3시간, 6: 최근 6시간, 12: 최근 12시간, 24: 최근 24시간.
	private Integer autoRefrash;//0:disabled, 1: enabled		
	private Integer autoLogoutTime;// 화면 자동 로그 아웃 시간.	
	
	public static OBDtoSystemEnvView toOBDtoSystemEnvView(SystemEnvViewDto eview) {
		OBDtoSystemEnvView envViewFromSvc = new OBDtoSystemEnvView();
		envViewFromSvc.setAutoLogoutTime(eview.getAutoLogoutTime());
		envViewFromSvc.setAutoRefrash(eview.getAutoRefrash());
		envViewFromSvc.setLogViewCount(eview.getLogViewCount());
		envViewFromSvc.setLogViewPeriodType(eview.getLogViewPeriodType());
		
		return envViewFromSvc;
		
	}
	
	public Date getOccurTime()
	{
		return occurTime;
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

	@Override
	public String toString() {
		return "SystemEnvViewDto [logViewCount=" + logViewCount
				+ ", logViewPeriodType=" + logViewPeriodType + ", autoRefrash="
				+ autoRefrash + ", autoLogoutTime=" + autoLogoutTime + "]";
	}	
	
}
