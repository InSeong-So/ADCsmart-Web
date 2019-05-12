package kr.openbase.adcsmart.service.dto.report;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoConnectionInfo;

public class OBDtoRptTrafficMonitoringTrendVService
{
	private String name;
	private ArrayList<OBDtoConnectionInfo> connList;
	@Override
	public String toString()
	{
		return "OBDtoRptTrafficMonitoringTrendVService [name=" + name
				+ ", connList=" + connList + "]";
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public ArrayList<OBDtoConnectionInfo> getConnList()
	{
		return connList;
	}
	public void setConnList(ArrayList<OBDtoConnectionInfo> connList)
	{
		this.connList = connList;
	}
}
