package kr.openbase.adcsmart.service.snmp.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;

public class DtoRptOPL7iRule
{
	private String name;
	private ArrayList<OBDtoAdcVServerF5> vsList;
	private int status;//used, not used.
	@Override
	public String toString()
	{
		return "DtoRptOPL7iRule [name=" + name + ", vsList=" + vsList
				+ ", status=" + status + "]";
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public ArrayList<OBDtoAdcVServerF5> getVsList()
	{
		return vsList;
	}
	public void setVsList(ArrayList<OBDtoAdcVServerF5> vsList)
	{
		this.vsList = vsList;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	
}
