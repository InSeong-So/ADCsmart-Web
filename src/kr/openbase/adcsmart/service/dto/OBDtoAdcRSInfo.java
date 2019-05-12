package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoRSInfo;

public class OBDtoAdcRSInfo
{
	private Integer 					adcIndex;
	private String 						adcName;
	private ArrayList <OBDtoRSInfo> 	rsInfoList;
	@Override
	public String toString()
	{
		return "OBDtoAdcRSInfo [adcIndex=" + adcIndex + ", adcName=" + adcName + ", rsInfoList=" + rsInfoList + "]";
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public ArrayList<OBDtoRSInfo> getRsInfoList()
	{
		return rsInfoList;
	}
	public void setRsInfoList(ArrayList<OBDtoRSInfo> rsInfoList)
	{
		this.rsInfoList = rsInfoList;
	}

}
