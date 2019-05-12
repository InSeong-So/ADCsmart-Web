package kr.openbase.adcsmart.service.dto.dashboard;

import kr.openbase.adcsmart.service.dto.OBDtoDataObj;

public class OBDtoDashboardTrafficStatus
{
	private OBDtoDataObj currTotal;// 최근 전체 트래픽
	private OBDtoDataObj prevTotal;// 전일 최근 트래픽.
	private OBDtoDataObj currMax;// 현재 최대.
	private OBDtoDataObj prevMax;// 전일 최대.
	private OBDtoDataObj curMin;// 현재 최소.
	private OBDtoDataObj prevMin;// 전일 최소.
	private OBDtoDataObj currAvg;// 현재 평균.
	private OBDtoDataObj prevAvg;// 전일 평균.
	@Override
	public String toString()
	{
		return "OBDtoDashboardTrafficStatus [currTotal=" + currTotal
				+ ", prevTotal=" + prevTotal + ", currMax=" + currMax
				+ ", prevMax=" + prevMax + ", curMin=" + curMin + ", prevMin="
				+ prevMin + ", currAvg=" + currAvg + ", prevAvg=" + prevAvg
				+ "]";
	}
	public OBDtoDataObj getCurrTotal()
	{
		return currTotal;
	}
	public void setCurrTotal(OBDtoDataObj currTotal)
	{
		this.currTotal = currTotal;
	}
	public OBDtoDataObj getPrevTotal()
	{
		return prevTotal;
	}
	public void setPrevTotal(OBDtoDataObj prevTotal)
	{
		this.prevTotal = prevTotal;
	}
	public OBDtoDataObj getCurrMax()
	{
		return currMax;
	}
	public void setCurrMax(OBDtoDataObj currMax)
	{
		this.currMax = currMax;
	}
	public OBDtoDataObj getPrevMax()
	{
		return prevMax;
	}
	public void setPrevMax(OBDtoDataObj prevMax)
	{
		this.prevMax = prevMax;
	}
	public OBDtoDataObj getCurMin()
	{
		return curMin;
	}
	public void setCurMin(OBDtoDataObj curMin)
	{
		this.curMin = curMin;
	}
	public OBDtoDataObj getPrevMin()
	{
		return prevMin;
	}
	public void setPrevMin(OBDtoDataObj prevMin)
	{
		this.prevMin = prevMin;
	}
	public OBDtoDataObj getCurrAvg()
	{
		return currAvg;
	}
	public void setCurrAvg(OBDtoDataObj currAvg)
	{
		this.currAvg = currAvg;
	}
	public OBDtoDataObj getPrevAvg()
	{
		return prevAvg;
	}
	public void setPrevAvg(OBDtoDataObj prevAvg)
	{
		this.prevAvg = prevAvg;
	}
}
