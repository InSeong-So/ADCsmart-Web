package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;

public class OBDtoAdcSysRescStatus
{
	private ArrayList<OBDtoAdcSystemResources> rescList;
	private ArrayList<OBDtoAdcConfigHistory> confEventList;
	@Override
	public String toString()
	{
		return "OBDtoAdcSysRescStatus [rescList=" + rescList
				+ ", confEventList=" + confEventList + "]";
	}
	public ArrayList<OBDtoAdcSystemResources> getRescList()
	{
		return rescList;
	}
	public void setRescList(ArrayList<OBDtoAdcSystemResources> rescList)
	{
		this.rescList = rescList;
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
