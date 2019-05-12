package kr.openbase.adcsmart.service.snmp.dto;

import java.util.ArrayList;

public class DtoRptVlanStatus
{

	private int index;
	private String name;
	private ArrayList<Integer> portList;//for alteon
	private ArrayList<String> portNameList;//for f5
	private int status;

	@Override
	public String toString()
	{
		return "DtoRptVlanStatus [index=" + index + ", name=" + name
				+ ", portList=" + portList + ", portNameList=" + portNameList
				+ ", status=" + status + "]";
	}
	public ArrayList<String> getPortNameList()
	{
		return portNameList;
	}
	public void setPortNameList(ArrayList<String> portNameList)
	{
		this.portNameList = portNameList;
	}
	public String getName()
	{
		return name;
	}
	public ArrayList<Integer> getPortList()
	{
		return portList;
	}
	public void setPortList(ArrayList<Integer> portList)
	{
		this.portList = portList;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public int getIndex()
	{
		return index;
	}
	public void setIndex(int index)
	{
		this.index = index;
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
