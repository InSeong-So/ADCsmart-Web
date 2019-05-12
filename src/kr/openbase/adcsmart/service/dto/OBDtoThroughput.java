package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;

public class OBDtoThroughput
{
	private ArrayList<OBDtoUsageThroughput> throughputList;
	private ArrayList<OBDtoAdcConfigHistory> confEventList;
	@Override
	public String toString()
	{
		return "OBDtoThroughput [throughputList=" + throughputList
				+ ", confEventList=" + confEventList + "]";
	}
	public ArrayList<OBDtoUsageThroughput> getThroughputList()
	{
		return throughputList;
	}
	public void setThroughputList(ArrayList<OBDtoUsageThroughput> throughputList)
	{
		this.throughputList = throughputList;
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
