package kr.openbase.adcsmart.service.dto.fault;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoDataObj;

public class OBDtoFaultDataObj
{
	private Long		currValue;
	private Long		prevValue;
	private Long		maxValue;
	private ArrayList<OBDtoDataObj> history;
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultDataObj [currValue=%s, prevValue=%s, maxValue=%s, history=%s]", currValue, prevValue, maxValue, history);
	}
	public Long getCurrValue()
	{
		return currValue;
	}
	public void setCurrValue(Long currValue)
	{
		this.currValue = currValue;
	}
	public Long getPrevValue()
	{
		return prevValue;
	}
	public void setPrevValue(Long prevValue)
	{
		this.prevValue = prevValue;
	}
	public Long getMaxValue()
	{
		return maxValue;
	}
	public void setMaxValue(Long maxValue)
	{
		this.maxValue = maxValue;
	}
	public ArrayList<OBDtoDataObj> getHistory()
	{
		return history;
	}
	public void setHistory(ArrayList<OBDtoDataObj> history)
	{
		this.history = history;
	}
}
