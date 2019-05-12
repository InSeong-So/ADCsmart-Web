package kr.openbase.adcsmart.service.dto.report;

import java.util.ArrayList;

public class OBDtoRptSysInfoL2
{
	private		OBDtoRptSysInfo 	linkup;
	private 	ArrayList<String>   portNameList;
	private		OBDtoRptSysInfo 	portStatus;
	private		OBDtoRptSysInfo 	vlanInfo;
	private		OBDtoRptSysInfo 	stpInfo;
	private		OBDtoRptSysInfo 	trunkInfo;
	@Override
	public String toString()
	{
		return "OBDtoRptSysInfoL2 [linkup=" + linkup + ", portNameList="
				+ portNameList + ", portStatus=" + portStatus + ", vlanInfo="
				+ vlanInfo + ", stpInfo=" + stpInfo + ", trunkInfo="
				+ trunkInfo + "]";
	}
	public ArrayList<String> getPortNameList()
	{
		return portNameList;
	}
	public void setPortNameList(ArrayList<String> portNameList)
	{
		this.portNameList = portNameList;
	}
	public OBDtoRptSysInfo getLinkup()
	{
		return linkup;
	}
	public void setLinkup(OBDtoRptSysInfo linkup)
	{
		this.linkup = linkup;
	}
	public OBDtoRptSysInfo getPortStatus()
	{
		return portStatus;
	}
	public void setPortStatus(OBDtoRptSysInfo portStatus)
	{
		this.portStatus = portStatus;
	}
	public OBDtoRptSysInfo getVlanInfo()
	{
		return vlanInfo;
	}
	public void setVlanInfo(OBDtoRptSysInfo vlanInfo)
	{
		this.vlanInfo = vlanInfo;
	}
	public OBDtoRptSysInfo getStpInfo()
	{
		return stpInfo;
	}
	public void setStpInfo(OBDtoRptSysInfo stpInfo)
	{
		this.stpInfo = stpInfo;
	}
	public OBDtoRptSysInfo getTrunkInfo()
	{
		return trunkInfo;
	}
	public void setTrunkInfo(OBDtoRptSysInfo trunkInfo)
	{
		this.trunkInfo = trunkInfo;
	}
}
