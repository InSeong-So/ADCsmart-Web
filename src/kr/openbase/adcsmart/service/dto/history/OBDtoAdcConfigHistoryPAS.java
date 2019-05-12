package kr.openbase.adcsmart.service.dto.history;

import java.util.ArrayList;

public class OBDtoAdcConfigHistoryPAS
{
	private Integer	adcIndex;
	private String	vsIndex;
	private boolean		isRecoverable; 
	
	private OBDtoAdcConfigInfoPAS vsConfigInfoOld;
	private OBDtoAdcConfigInfoPAS vsConfigInfoNew;
	
	private ArrayList<OBDtoAdcConfigHistory2> vsHistoryList;

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigHistoryPAS [adcIndex=" + adcIndex + ", vsIndex=" + vsIndex + ", isRecoverable=" + isRecoverable + ", vsConfigInfoOld=" + vsConfigInfoOld + ", vsConfigInfoNew=" + vsConfigInfoNew + ", vsHistoryList=" + vsHistoryList + "]";
	}
	
	public Integer getAdcIndex()
	{
		return adcIndex;
	}

	public boolean isRecoverable()
	{
		return isRecoverable;
	}
	public void setRecoverable(boolean isRecoverable)
	{
		this.isRecoverable = isRecoverable;
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
	public OBDtoAdcConfigInfoPAS getVsConfigInfoOld()
	{
		return vsConfigInfoOld;
	}
	public void setVsConfigInfoOld(OBDtoAdcConfigInfoPAS vsConfigInfoOld)
	{
		this.vsConfigInfoOld = vsConfigInfoOld;
	}
	public OBDtoAdcConfigInfoPAS getVsConfigInfoNew()
	{
		return vsConfigInfoNew;
	}
	public void setVsConfigInfoNew(OBDtoAdcConfigInfoPAS vsConfigInfoNew)
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
