package kr.openbase.adcsmart.service.dto;

public class OBDtoAdcStatusCount
{
	private Integer adc;
	private Integer adcAvail;
	private Integer adcUnavail;
	
	public OBDtoAdcStatusCount()
	{
		adc = 0;
		adcAvail = 0;
		adcUnavail = 0;
	}
	public Integer getAdc()
	{
		return adc;
	}
	public void setAdc(Integer adc)
	{
		this.adc = adc;
	}
	public Integer getAdcAvail()
	{
		return adcAvail;
	}
	public void setAdcAvail(Integer adcAvail)
	{
		this.adcAvail = adcAvail;
	}
	public Integer getAdcUnavail()
	{
		return adcUnavail;
	}
	public void setAdcUnavail(Integer adcUnavail)
	{
		this.adcUnavail = adcUnavail;
	}
	@Override
	public String toString()
	{
		return "OBDtoAdcStatusCount [adc=" + adc + ", adcAvail=" + adcAvail
				+ ", adcUnavail=" + adcUnavail + "]";
	}

}
