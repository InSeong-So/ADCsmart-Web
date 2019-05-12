package kr.openbase.adcsmart.service.snmp.alteon.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.snmp.dto.DtoRptL3Gateway;
import kr.openbase.adcsmart.service.snmp.dto.DtoRptL3Int;

public class DtoRptOPL3
{
	private ArrayList<DtoRptL3Int> intList;
	private ArrayList<DtoRptL3Gateway> gwList;
	@Override
	public String toString()
	{
		return "DtoRptOPL3 [intList=" + intList + ", gwList=" + gwList + "]";
	}
	public ArrayList<DtoRptL3Int> getIntList()
	{
		return intList;
	}
	public void setIntList(ArrayList<DtoRptL3Int> intList)
	{
		this.intList = intList;
	}
	public ArrayList<DtoRptL3Gateway> getGwList()
	{
		return gwList;
	}
	public void setGwList(ArrayList<DtoRptL3Gateway> gwList)
	{
		this.gwList = gwList;
	}
}
