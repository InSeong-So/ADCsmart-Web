package kr.openbase.adcsmart.service.dto.history;

import java.util.ArrayList;

public class OBDtoAdcConfigHistoryF5
{
	private Integer	adcIndex;
	private String	vsIndex;
	private boolean		isRecoverable; 
	
	private OBDtoAdcConfigInfoF5 vsConfigInfoOld;
	private OBDtoAdcConfigInfoF5 vsConfigInfoNew;

	private ArrayList<OBDtoAdcConfigHistory2> vsHistoryList;

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigHistoryF5 [adcIndex=" + adcIndex + ", vsIndex=" + vsIndex + ", isRecoverable=" + isRecoverable + ", vsConfigInfoOld=" + vsConfigInfoOld + ", vsConfigInfoNew=" + vsConfigInfoNew + ", vsHistoryList=" + vsHistoryList + "]";
	}
	public boolean isRecoverable()
	{
		return isRecoverable;
	}
	public void setRecoverable(boolean isRecoverable)
	{
		this.isRecoverable = isRecoverable;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}
	public OBDtoAdcConfigInfoF5 getVsConfigInfoOld()
	{
		return vsConfigInfoOld;
	}
	public void setVsConfigInfoOld(OBDtoAdcConfigInfoF5 vsConfigInfoOld)
	{
		this.vsConfigInfoOld = vsConfigInfoOld;
	}
	public OBDtoAdcConfigInfoF5 getVsConfigInfoNew()
	{
		return vsConfigInfoNew;
	}
	public void setVsConfigInfoNew(OBDtoAdcConfigInfoF5 vsConfigInfoNew)
	{
		this.vsConfigInfoNew = vsConfigInfoNew;
	}
	public ArrayList<OBDtoAdcConfigHistory2> getVsHistoryList()
	{
		return vsHistoryList;
	}
	public void setVsHistoryList(ArrayList<OBDtoAdcConfigHistory2> vsHistoryList)
	{
		this.vsHistoryList = vsHistoryList;
	}
}
