package kr.openbase.adcsmart.service.dto.report;

import java.util.ArrayList;


public class OBDtoRptL4PerformanceSummary
{
	private ArrayList<OBDtoRptL4PerformanceInfo> top10ThroughputList;
	private ArrayList<OBDtoRptL4PerformanceInfo> top10ConnectionList;
	@Override
	public String toString()
	{
		return "OBDtoRptL4PerformanceSummary [top10ThroughputList="
				+ top10ThroughputList + ", top10ConnectionList="
				+ top10ConnectionList + "]";
	}
	public ArrayList<OBDtoRptL4PerformanceInfo> getTop10ThroughputList()
	{
		return top10ThroughputList;
	}
	public void setTop10ThroughputList(
			ArrayList<OBDtoRptL4PerformanceInfo> top10ThroughputList)
	{
		this.top10ThroughputList = top10ThroughputList;
	}
	public ArrayList<OBDtoRptL4PerformanceInfo> getTop10ConnectionList()
	{
		return top10ConnectionList;
	}
	public void setTop10ConnectionList(
			ArrayList<OBDtoRptL4PerformanceInfo> top10ConnectionList)
	{
		this.top10ConnectionList = top10ConnectionList;
	}	
}
