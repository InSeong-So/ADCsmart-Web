package kr.openbase.adcsmart.service.dto.report;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoTrafficMapVServiceMembers;

public class OBDtoRptTrafficMonitoringStatus
{
	private String adcName;
	private Integer adcIndex;
	private ArrayList<OBDtoTrafficMapVServiceMembers> statusList;
	@Override
	public String toString()
	{
		return "OBDtoRptTrafficMonitoringStatus [adcName=" + adcName
				+ ", adcIndex=" + adcIndex + ", statusList=" + statusList + "]";
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
	public ArrayList<OBDtoTrafficMapVServiceMembers> getStatusList()
	{
		return statusList;
	}
	public void setStatusList(ArrayList<OBDtoTrafficMapVServiceMembers> statusList)
	{
		this.statusList = statusList;
	}
}
