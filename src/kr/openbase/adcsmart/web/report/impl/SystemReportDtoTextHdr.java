package kr.openbase.adcsmart.web.report.impl;

public class SystemReportDtoTextHdr
{
	private String occurTime;// 보고서 생성 시간
	private String period;// 보고서 기간
	private String userName;// 사용자.
	private String targetAdc;// 보고서 대상.
	@Override
	public String toString()
	{
		return "OBDtoRptSysFltTextHdr [occurTime=" + occurTime + ", period=" + period + ", userName=" + userName + ", targetAdc=" + targetAdc + "]";
	}
	public String getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(String occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getPeriod()
	{
		return period;
	}
	public void setPeriod(String period)
	{
		this.period = period;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getTargetAdc()
	{
		return targetAdc;
	}
	public void setTargetAdc(String targetAdc)
	{
		this.targetAdc = targetAdc;
	}
}
