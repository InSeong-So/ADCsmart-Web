package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;

public class OBDtoCpu
{
	private Long maxValue;
	private Long minValue;
	private Long avgValue;
	private ArrayList<OBDtoUsageCpu> cpuList;
	private ArrayList<OBDtoAdcConfigHistory> confEventList;
	@Override
	public String toString()
	{
		return "OBDtoCpu [maxValue=" + maxValue + ", minValue=" + minValue
				+ ", avgValue=" + avgValue + ", cpuList=" + cpuList
				+ ", confEventList=" + confEventList + "]";
	}
	public Long getMaxValue()
	{
		return maxValue;
	}
	public void setMaxValue(Long maxValue)
	{
		this.maxValue = maxValue;
	}
	public Long getMinValue()
	{
		return minValue;
	}
	public void setMinValue(Long minValue)
	{
		this.minValue = minValue;
	}
	public Long getAvgValue()
	{
		return avgValue;
	}
	public void setAvgValue(Long avgValue)
	{
		this.avgValue = avgValue;
	}
	public ArrayList<OBDtoUsageCpu> getCpuList()
	{
		return cpuList;
	}
	public void setCpuList(ArrayList<OBDtoUsageCpu> cpuList)
	{
		this.cpuList = cpuList;
	}
	public ArrayList<OBDtoAdcConfigHistory> getConfEventList()
	{
		return confEventList;
	}
	public void setConfEventList(ArrayList<OBDtoAdcConfigHistory> confEventList)
	{
		this.confEventList = confEventList;
	}
}
