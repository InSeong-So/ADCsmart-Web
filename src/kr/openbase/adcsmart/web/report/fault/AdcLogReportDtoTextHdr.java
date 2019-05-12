package kr.openbase.adcsmart.web.report.fault;

public class AdcLogReportDtoTextHdr
{
	private String title;// 장애 모니터링
	private String adcName;// ADC 이름
	private String occurTime;// 발생 건수
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(String occurTime)
	{
		this.occurTime = occurTime;
	}
}
