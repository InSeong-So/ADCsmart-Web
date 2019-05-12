package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;

import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;

public class OBDtoAdcConfigVServerPAS implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int IpChange;
	private int protocolChange;
	private int portChange;
	private int stateChange;
	private int poolChange;
	
	private OBDtoAdcVServerPAS vsOld;
	private OBDtoAdcVServerPAS vsNew;

	private OBDtoAdcConfigPoolPAS poolConfig;

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigVServerPAS [change=" + change + ", IpChange="
				+ IpChange + ", protocolChange=" + protocolChange
				+ ", portChange=" + portChange + ", stateChange=" + stateChange
				+ ", poolChange=" + poolChange + ", vsOld=" + vsOld
				+ ", vsNew=" + vsNew + ", poolConfig=" + poolConfig + "]";
	}
	public int getChange()
	{
		return change;
	}
	public void setChange(int change)
	{
		this.change = change;
	}
	public int getIpChange()
	{
		return IpChange;
	}
	public void setIpChange(int ipChange)
	{
		IpChange = ipChange;
	}
	public int getProtocolChange()
	{
		return protocolChange;
	}
	public void setProtocolChange(int protocolChange)
	{
		this.protocolChange = protocolChange;
	}
	public int getPortChange()
	{
		return portChange;
	}
	public void setPortChange(int portChange)
	{
		this.portChange = portChange;
	}
	public int getStateChange()
	{
		return stateChange;
	}
	public void setStateChange(int stateChange)
	{
		this.stateChange = stateChange;
	}
	public int getPoolChange()
	{
		return poolChange;
	}
	public void setPoolChange(int poolChange)
	{
		this.poolChange = poolChange;
	}
	public OBDtoAdcVServerPAS getVsOld()
	{
		return vsOld;
	}
	public void setVsOld(OBDtoAdcVServerPAS vsOld)
	{
		this.vsOld = vsOld;
	}
	public OBDtoAdcVServerPAS getVsNew()
	{
		return vsNew;
	}
	public void setVsNew(OBDtoAdcVServerPAS vsNew)
	{
		this.vsNew = vsNew;
	}
	public OBDtoAdcConfigPoolPAS getPoolConfig()
	{
		return poolConfig;
	}
	public void setPoolConfig(OBDtoAdcConfigPoolPAS poolConfig)
	{
		this.poolConfig = poolConfig;
	}
}