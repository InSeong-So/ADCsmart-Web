package kr.openbase.adcsmart.service.dto;

public class OBDtoDashboardSdsStatusSummary
{
	private Integer adc;
	private Integer adcAvail;
	private Integer adcUnavail;
	private Integer vs;
	private Integer vsAvail;
	private Integer vsUnavail; //단절 전체
	private Integer vsUnavailLessNDays; //단절 X일 이내
	private Integer vsUnavailOverNDays; //단절 X일 초과
	private Integer vsDisable; //꺼짐
	private Integer fault;
	private Integer faultSolved;  // 해결 장애
	private Integer faultUnsolved;// 미해결 장애
	private Integer faultWarn; // 경고 : 해결/미해결을 구별할 수 없는 장애 
	
	public OBDtoDashboardSdsStatusSummary()
	{
		adc = 0;
		adcAvail = 0;
		adcUnavail = 0;
		vs = 0;
		vsAvail = 0;
		vsUnavail = 0;
		vsUnavailOverNDays = 0;
		vsDisable = 0;
		fault = 0;
		faultSolved = 0;
		faultUnsolved = 0;
		faultWarn = 0;
	}
	
	@Override
	public String toString()
	{
		return "OBDtoDashboardSdsStatusSummary [adc=" + adc + ", adcAvail="
				+ adcAvail + ", adcUnavail=" + adcUnavail + ", vs=" + vs
				+ ", vsAvail=" + vsAvail + ", vsUnavail=" + vsUnavail
				+ ", vsUnavailLessNDays=" + vsUnavailLessNDays
				+ ", vsUnavailOverNDays=" + vsUnavailOverNDays + ", vsDisable="
				+ vsDisable + ", fault=" + fault + ", faultSolved="
				+ faultSolved + ", faultUnsolved=" + faultUnsolved
				+ ", faultWarn=" + faultWarn + "]";
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
	public Integer getVs()
	{
		return vs;
	}
	public void setVs(Integer vs)
	{
		this.vs = vs;
	}
	public Integer getVsAvail()
	{
		return vsAvail;
	}
	public void setVsAvail(Integer vsAvail)
	{
		this.vsAvail = vsAvail;
	}
	public Integer getVsUnavail()
	{
		return vsUnavail;
	}
	public void setVsUnavail(Integer vsUnavail)
	{
		this.vsUnavail = vsUnavail;
	}
	public Integer getVsDisable()
	{
		return vsDisable;
	}
	public void setVsDisable(Integer vsDisable)
	{
		this.vsDisable = vsDisable;
	}
	public Integer getFault()
	{
		return fault;
	}
	public void setFault(Integer fault)
	{
		this.fault = fault;
	}
	public Integer getFaultSolved()
	{
		return faultSolved;
	}
	public void setFaultSolved(Integer faultSolved)
	{
		this.faultSolved = faultSolved;
	}
	public Integer getFaultUnsolved()
	{
		return faultUnsolved;
	}
	public void setFaultUnsolved(Integer faultUnsolved)
	{
		this.faultUnsolved = faultUnsolved;
	}
	public Integer getVsUnavailLessNDays()
	{
		return vsUnavailLessNDays;
	}
	public void setVsUnavailLessNDays(Integer vsUnavailLessNDays)
	{
		this.vsUnavailLessNDays = vsUnavailLessNDays;
	}
	public Integer getVsUnavailOverNDays()
	{
		return vsUnavailOverNDays;
	}
	public void setVsUnavailOverNDays(Integer vsUnavailOverNDays)
	{
		this.vsUnavailOverNDays = vsUnavailOverNDays;
	}

	public Integer getFaultWarn()
	{
		return faultWarn;
	}

	public void setFaultWarn(Integer faultWarn)
	{
		this.faultWarn = faultWarn;
	}
	
}