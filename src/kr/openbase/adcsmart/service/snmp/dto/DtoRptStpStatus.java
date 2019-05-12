package kr.openbase.adcsmart.service.snmp.dto;

import java.util.ArrayList;

public class DtoRptStpStatus
{
	private int state;//enabled?disabled?
	private ArrayList<DtoRptStgStatus> stgList;
	@Override
	public String toString()
	{
		return "DtoRptStpStatus [state=" + state + ", stgList=" + stgList + "]";
	}
	public int getState()
	{
		return state;
	}
	public void setState(int state)
	{
		this.state = state;
	}
	public ArrayList<DtoRptStgStatus> getStgList()
	{
		return stgList;
	}
	public void setStgList(ArrayList<DtoRptStgStatus> stgList)
	{
		this.stgList = stgList;
	}	
}
