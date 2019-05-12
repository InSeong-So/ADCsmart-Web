package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

import java.util.ArrayList;

public class OBDtoIPInfoAlteon
{
	private 	ArrayList<OBDtoIPAddrInfoAlteon> ipList;
	private		ArrayList<OBDtoGateWayInfoAlteon> gatewayList;
	
	public OBDtoIPInfoAlteon()
	{
		ipList = new ArrayList<OBDtoIPAddrInfoAlteon>();
		gatewayList = new ArrayList<OBDtoGateWayInfoAlteon>();
	}
	
	@Override
	public String toString()
	{
		return "OBDtoIPInfoAlteon [ipList=" + ipList + ", gatewayList=" + gatewayList + "]";
	}
	public ArrayList<OBDtoIPAddrInfoAlteon> getIpList()
	{
		return ipList;
	}
	public void setIpList(ArrayList<OBDtoIPAddrInfoAlteon> ipList)
	{
		this.ipList = ipList;
	}
	public ArrayList<OBDtoGateWayInfoAlteon> getGatewayList()
	{
		return gatewayList;
	}
	public void setGatewayList(ArrayList<OBDtoGateWayInfoAlteon> gatewayList)
	{
		this.gatewayList = gatewayList;
	}
}
