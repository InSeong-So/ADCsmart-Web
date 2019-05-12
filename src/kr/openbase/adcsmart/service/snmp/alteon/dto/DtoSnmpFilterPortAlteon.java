package kr.openbase.adcsmart.service.snmp.alteon.dto;

import java.util.ArrayList;

public class DtoSnmpFilterPortAlteon
{
	private Integer physicalPortIndex;
	private ArrayList<Integer> filterIndexList ;

	@Override
	public String toString()
	{
		return "DtoSnmpFilterPortAlteon [physicalPortIndex="
				+ physicalPortIndex + ", filterIndexList=" + filterIndexList
				+ "]";
	}

	public Integer getPhysicalPortIndex()
	{
		return physicalPortIndex;
	}
	public void setPhysicalPortIndex(Integer physicalPortIndex)
	{
		this.physicalPortIndex = physicalPortIndex;
	}
	public ArrayList<Integer> getFilterIndexList()
	{
		return filterIndexList;
	}
	public void setFilterIndexList(ArrayList<Integer> filterIndexList)
	{
		this.filterIndexList = filterIndexList;
	}
}
