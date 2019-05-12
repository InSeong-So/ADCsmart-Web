package kr.openbase.adcsmart.service.dto.dashboard;

import kr.openbase.adcsmart.service.dto.OBDtoDataObj;

public class OBDtoDashboardVSSummary
{
	private OBDtoDataObj total;// 전체 개수
	private OBDtoDataObj avail;// 정상 개수
	private OBDtoDataObj unavail;// 단절 개수
	private OBDtoDataObj disable;// 꺼짐 개수.
	private OBDtoDataObj unavailLessNDays;// N일 미만 단절
	private OBDtoDataObj unavailOverNDays;// N일 이상 단절
	
	public OBDtoDataObj getTotal()
	{
		return total;
	}
	public void setTotal(OBDtoDataObj total)
	{
		this.total = total;
	}
	public OBDtoDataObj getAvail()
	{
		return avail;
	}
	public void setAvail(OBDtoDataObj avail)
	{
		this.avail = avail;
	}
	public OBDtoDataObj getUnavail()
	{
		return unavail;
	}
	public void setUnavail(OBDtoDataObj unavail)
	{
		this.unavail = unavail;
	}
	public OBDtoDataObj getDisable()
	{
		return disable;
	}
	public void setDisable(OBDtoDataObj disable)
	{
		this.disable = disable;
	}
	public OBDtoDataObj getUnavailLessNDays()
	{
		return unavailLessNDays;
	}
	public void setUnavailLessNDays(OBDtoDataObj unavailLessNDays)
	{
		this.unavailLessNDays = unavailLessNDays;
	}
	public OBDtoDataObj getUnavailOverNDays()
	{
		return unavailOverNDays;
	}
	public void setUnavailOverNDays(OBDtoDataObj unavailOverNDays)
	{
		this.unavailOverNDays = unavailOverNDays;
	}
	@Override
	public String toString()
	{
		return "OBDtoDashboardVSSummary [total=" + total + ", avail=" + avail
				+ ", unavail=" + unavail + ", disable=" + disable
				+ ", unavailLessNDays=" + unavailLessNDays
				+ ", unavailOverNDays=" + unavailOverNDays + "]";
	}
}
