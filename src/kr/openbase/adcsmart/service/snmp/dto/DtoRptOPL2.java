package kr.openbase.adcsmart.service.snmp.dto;

import java.util.ArrayList;

public class DtoRptOPL2
{
	private ArrayList<DtoRptLinkStatus> linkupList;
	private ArrayList<DtoRptPortStatus> portStatusList;
	private ArrayList<DtoRptVlanStatus> vlanList;
	private DtoRptStpStatus stpInfo;
	private ArrayList<DtoRptTrunkStatus> trunkList;
	@Override
	public String toString()
	{
		return "DtoRptOPL2 [linkupList=" + linkupList + ", portStatusList="
				+ portStatusList + ", vlanList=" + vlanList + ", stpInfo="
				+ stpInfo + ", trunkList=" + trunkList + "]";
	}
	public ArrayList<DtoRptLinkStatus> getLinkupList()
	{
		return linkupList;
	}
	public ArrayList<DtoRptTrunkStatus> getTrunkList()
	{
		return trunkList;
	}
	public void setTrunkList(ArrayList<DtoRptTrunkStatus> trunkList)
	{
		this.trunkList = trunkList;
	}
	public DtoRptStpStatus getStpInfo()
	{
		return stpInfo;
	}
	public void setStpInfo(DtoRptStpStatus stpInfo)
	{
		this.stpInfo = stpInfo;
	}
	public void setLinkupList(ArrayList<DtoRptLinkStatus> linkupList)
	{
		this.linkupList = linkupList;
	}
	public ArrayList<DtoRptPortStatus> getPortStatusList()
	{
		return portStatusList;
	}
	public void setPortStatusList(ArrayList<DtoRptPortStatus> portStatusList)
	{
		this.portStatusList = portStatusList;
	}
	public ArrayList<DtoRptVlanStatus> getVlanList()
	{
		return vlanList;
	}
	public void setVlanList(ArrayList<DtoRptVlanStatus> vlanList)
	{
		this.vlanList = vlanList;
	}
}
