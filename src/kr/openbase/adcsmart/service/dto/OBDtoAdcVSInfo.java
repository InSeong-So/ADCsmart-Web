package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoAdcVSInfo
{
	private Integer 					adcIndex;
	private String 						adcName;
	private ArrayList <OBDtoVSInfo> 	vsInfoList;
	@Override
	public String toString()
	{
		return "OBDtoAdcVSInfo [adcIndex=" + adcIndex + ", adcName=" + adcName + ", vsInfoList=" + vsInfoList + "]";
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public ArrayList<OBDtoVSInfo> getVsInfoList()
	{
		return vsInfoList;
	}
	public void setVsInfoList(ArrayList<OBDtoVSInfo> vsInfoList)
	{
		this.vsInfoList = vsInfoList;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
}
