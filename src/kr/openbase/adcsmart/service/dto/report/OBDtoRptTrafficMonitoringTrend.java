package kr.openbase.adcsmart.service.dto.report;

import java.util.ArrayList;

public class OBDtoRptTrafficMonitoringTrend
{
	private String adcName;
	private Integer adcIndex;
	private ArrayList<OBDtoRptTrafficMonitoringTrendVService> serviceList;
	@Override
	public String toString()
	{
		return "OBDtoRptTrafficMonitoringTrend [adcName=" + adcName
				+ ", adcIndex=" + adcIndex + ", serviceList=" + serviceList
				+ "]";
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public ArrayList<OBDtoRptTrafficMonitoringTrendVService> getServiceList()
	{
		return serviceList;
	}
	public void setServiceList(
			ArrayList<OBDtoRptTrafficMonitoringTrendVService> serviceList)
	{
		this.serviceList = serviceList;
	}
}
