package kr.openbase.adcsmart.service.dto;

public class OBDtoAdcSummaryStatus
{
	private Integer adcIndex;
	private String adcName;
	private Integer status;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcSummaryStatus [adcIndex=" + adcIndex + ", adcName="
				+ adcName + ", status=" + status + "]";
	}
	
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
}
