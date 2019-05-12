package kr.openbase.adcsmart.service.dto.fault;

import java.util.ArrayList;

public class OBDtoFaultVlanInfo
{
	private String name;
	private ArrayList<Integer> interfaceList;
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultVlanInfo [name=%s, interfaceList=%s]", name, interfaceList);
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public ArrayList<Integer> getInterfaceList()
	{
		return interfaceList;
	}
	public void setInterfaceList(ArrayList<Integer> interfaceList)
	{
		this.interfaceList = interfaceList;
	}
}
