package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoAdcSummary
{
	private String groupName;
	private ArrayList<OBDtoAdcSummaryStatus> adcList;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcSummary [groupName=" + groupName + ", adcList="
				+ adcList + "]";
	}
	
	public String getGroupName()
	{
		return groupName;
	}
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
	public ArrayList<OBDtoAdcSummaryStatus> getAdcList()
	{
		return adcList;
	}
	public void setAdcList(ArrayList<OBDtoAdcSummaryStatus> adcList)
	{
		this.adcList = adcList;
	}
}
