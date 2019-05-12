package kr.openbase.adcsmart.service.dto.fault;

import java.util.ArrayList;

public class OBDtoFaultCpuDataObj
{
	private Long		currValue;
	private Long		prevValue;
	private Long		maxValue;
	private ArrayList<OBDtoFaultCpuHistory> history;
	
	private Long		avgUsageValue;
	private Long		maxUsageValue;
	private Long		avgConnsValue;
	private Long		maxConnsValue;	
	private Long		spSessionMax;
	private String		spSessionMaxUnit;
	
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
	public ArrayList<OBDtoFaultCpuHistory> getHistory()
	{
		return history;
	}
	public void setHistory(ArrayList<OBDtoFaultCpuHistory> history)
	{
		this.history = history;
	}
	public Long getAvgUsageValue()
	{
		return avgUsageValue;
	}
	public void setAvgUsageValue(Long avgUsageValue)
	{
		this.avgUsageValue = avgUsageValue;
	}
	public Long getMaxUsageValue()
	{
		return maxUsageValue;
	}
	public void setMaxUsageValue(Long maxUsageValue)
	{
		this.maxUsageValue = maxUsageValue;
	}
	public Long getAvgConnsValue()
	{
		return avgConnsValue;
	}
	public void setAvgConnsValue(Long avgConnsValue)
	{
		this.avgConnsValue = avgConnsValue;
	}
	public Long getMaxConnsValue()
	{
		return maxConnsValue;
	}
	public void setMaxConnsValue(Long maxConnsValue)
	{
		this.maxConnsValue = maxConnsValue;
	}
	public Long getSpSessionMax()
	{
		return spSessionMax;
	}
	public void setSpSessionMax(Long spSessionMax)
	{
		this.spSessionMax = spSessionMax;
	}
	public String getSpSessionMaxUnit()
	{
		return spSessionMaxUnit;
	}
	public void setSpSessionMaxUnit(String spSessionMaxUnit)
	{
		this.spSessionMaxUnit = spSessionMaxUnit;
	}	
}
