package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

import java.util.ArrayList;

public class OBDtoVSvcStatusAlteon
{
	private	int		vsId;// alteon id
	private	String 	vsIP;
	private int		svcPort;// service port
	private	int		status;
	private ArrayList<OBDtoRSrvStatusAlteon> realSrvStatusList;
	
	public OBDtoVSvcStatusAlteon()
	{
		realSrvStatusList = new ArrayList<OBDtoRSrvStatusAlteon>();
	}
	@Override
	public String toString()
	{
		return "OBDtoVSvcStatusAlteon [vsId=" + vsId + ", vsIP=" + vsIP + ", svcPort=" + svcPort + ", status=" + status + ", realSrvStatusList=" + realSrvStatusList + "]";
	}
	public int getVsId()
	{
		return vsId;
	}
	public void setVsId(int vsId)
	{
		this.vsId = vsId;
	}
	public String getVsIP()
	{
		return vsIP;
	}
	public void setVsIP(String vsIP)
	{
		this.vsIP = vsIP;
	}
	public int getSvcPort()
	{
		return svcPort;
	}
	public void setSvcPort(int svcPort)
	{
		this.svcPort = svcPort;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public ArrayList<OBDtoRSrvStatusAlteon> getRealSrvStatusList()
	{
		return realSrvStatusList;
	}
	public void setRealSrvStatusList(ArrayList<OBDtoRSrvStatusAlteon> realSrvStatusList)
	{
		this.realSrvStatusList = realSrvStatusList;
	}
}
