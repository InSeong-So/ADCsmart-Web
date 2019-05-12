package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;

public class OBDtoPoolNodeInfo
{
	private Integer adcIndex;
	private String  adcName;
	private String  adcIPAddress;
	private ArrayList<String> 	unUsedPoolNameList;
	private ArrayList<Integer> 	unUsedPoolIDList;
	private ArrayList<String> 	unUsedNodeNameList;
	private ArrayList<Integer> 	unUsedNodeIDList;
	@Override
	public String toString()
	{
		return "OBDtoPoolNodeInfo [adcIndex=" + adcIndex + ", adcName=" + adcName + ", adcIPAddress=" + adcIPAddress + ", unUsedPoolNameList=" + unUsedPoolNameList + ", unUsedPoolIDList=" + unUsedPoolIDList + ", unUsedNodeNameList=" + unUsedNodeNameList + ", unUsedNodeIDList=" + unUsedNodeIDList + "]";
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
	public String getAdcIPAddress()
	{
		return adcIPAddress;
	}
	public void setAdcIPAddress(String adcIPAddress)
	{
		this.adcIPAddress = adcIPAddress;
	}
	public ArrayList<String> getUnUsedPoolNameList()
	{
		return unUsedPoolNameList;
	}
	public void setUnUsedPoolNameList(ArrayList<String> unUsedPoolNameList)
	{
		this.unUsedPoolNameList = unUsedPoolNameList;
	}
	public ArrayList<Integer> getUnUsedPoolIDList()
	{
		return unUsedPoolIDList;
	}
	public void setUnUsedPoolIDList(ArrayList<Integer> unUsedPoolIDList)
	{
		this.unUsedPoolIDList = unUsedPoolIDList;
	}
	public ArrayList<String> getUnUsedNodeNameList()
	{
		return unUsedNodeNameList;
	}
	public void setUnUsedNodeNameList(ArrayList<String> unUsedNodeNameList)
	{
		this.unUsedNodeNameList = unUsedNodeNameList;
	}
	public ArrayList<Integer> getUnUsedNodeIDList()
	{
		return unUsedNodeIDList;
	}
	public void setUnUsedNodeIDList(ArrayList<Integer> unUsedNodeIDList)
	{
		this.unUsedNodeIDList = unUsedNodeIDList;
	}

}
