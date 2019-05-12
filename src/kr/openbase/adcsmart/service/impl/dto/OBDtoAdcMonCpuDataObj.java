package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;

public class OBDtoAdcMonCpuDataObj
{
	private Long		currValue;
	private Long		prevValue;
	
	private ArrayList<OBDtoAdcMonCpuHistory> history;
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
	public ArrayList<OBDtoAdcMonCpuHistory> getHistory()
	{
		return history;
	}
	public void setHistory(ArrayList<OBDtoAdcMonCpuHistory> history)
	{
		this.history = history;
	}
}
