package kr.openbase.adcsmart.service.snmp.alteon.dto;

import java.util.ArrayList;

public class DtoSnmpNetworkClassAlteon
{
	private String id;
	private String name;
	private ArrayList<DtoSnmpNetworkClassNetAlteon> netList;
	
	@Override
	public String toString()
	{
		return "DtoSnmpNetworkClassAlteon [id=" + id + ", name=" + name
				+ ", netList=" + netList + "]";
	}

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public ArrayList<DtoSnmpNetworkClassNetAlteon> getNetList()
	{
		return netList;
	}
	public void setNetList(ArrayList<DtoSnmpNetworkClassNetAlteon> netList)
	{
		this.netList = netList;
	}
}
