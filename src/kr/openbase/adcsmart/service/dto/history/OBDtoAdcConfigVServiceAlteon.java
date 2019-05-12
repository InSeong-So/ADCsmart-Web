package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;

import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;

public class OBDtoAdcConfigVServiceAlteon implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int servicePortChange;
	private int realPortChange;
	private int poolChange; //virtual server에 할당된 pool이 바뀌었거나, 바뀌지 않았지만 pool의 속성이 달라졌을 때 표시
	
	private OBDtoAdcVService serviceOld;
	private OBDtoAdcVService serviceNew;
	
	private OBDtoAdcConfigPoolAlteon poolConfig;

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigVServiceAlteon [change=" + change
				+ ", servicePortChange=" + servicePortChange
				+ ", realPortChange=" + realPortChange + ", poolChange="
				+ poolChange + ", serviceOld=" + serviceOld + ", serviceNew="
				+ serviceNew + ", poolConfig=" + poolConfig + "]";
	}
	public int getChange()
	{
		return change;
	}
	public void setChange(int change)
	{
		this.change = change;
	}
	public int getServicePortChange()
	{
		return servicePortChange;
	}
	public void setServicePortChange(int servicePortChange)
	{
		this.servicePortChange = servicePortChange;
	}
	public int getRealPortChange()
	{
		return realPortChange;
	}
	public void setRealPortChange(int realPortChange)
	{
		this.realPortChange = realPortChange;
	}
	public int getPoolChange()
	{
		return poolChange;
	}
	public void setPoolChange(int poolChange)
	{
		this.poolChange = poolChange;
	}
	public OBDtoAdcVService getServiceOld()
	{
		return serviceOld;
	}
	public void setServiceOld(OBDtoAdcVService serviceOld)
	{
		this.serviceOld = serviceOld;
	}
	public OBDtoAdcVService getServiceNew()
	{
		return serviceNew;
	}
	public void setServiceNew(OBDtoAdcVService serviceNew)
	{
		this.serviceNew = serviceNew;
	}
	public OBDtoAdcConfigPoolAlteon getPoolConfig()
	{
		return poolConfig;
	}
	public void setPoolConfig(OBDtoAdcConfigPoolAlteon poolConfig)
	{
		this.poolConfig = poolConfig;
	}
}