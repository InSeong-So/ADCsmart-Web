package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;

public class OBDtoAdcConfigVServerF5 implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change;
	private int IpChange;
	private int portChange;
	private int poolChange;
	private int persistenceChange;
	private int vlanFilterChange;
	private int useynChange;
	
	private OBDtoAdcVServerF5 vsOld;
	private OBDtoAdcVServerF5 vsNew;
	private OBDtoAdcConfigPoolF5 poolConfig;

	private ArrayList<OBDtoAdcConfigNodeF5> nodeConfigList;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigVServerF5 [change=" + change + ", IpChange=" + IpChange + ", portChange=" + portChange + ", poolChange=" + poolChange + ", persistenceChange=" + persistenceChange + ", vlanFilterChange=" + vlanFilterChange + ", useynChange=" + useynChange + ", vsOld=" + vsOld + ", vsNew=" + vsNew + ", poolConfig=" + poolConfig + ", nodeConfigList=" + nodeConfigList + "]";
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
	public int getPortChange()
	{
		return portChange;
	}
	public void setPortChange(int portChange)
	{
		this.portChange = portChange;
	}
	public int getPoolChange()
	{
		return poolChange;
	}
	public void setPoolChange(int poolChange)
	{
		this.poolChange = poolChange;
	}
	public int getPersistenceChange()
	{
		return persistenceChange;
	}
	public void setPersistenceChange(int persistenceChange)
	{
		this.persistenceChange = persistenceChange;
	}
	public OBDtoAdcVServerF5 getVsOld()
	{
		return vsOld;
	}
	public void setVsOld(OBDtoAdcVServerF5 vsOld)
	{
		this.vsOld = vsOld;
	}
	public OBDtoAdcVServerF5 getVsNew()
	{
		return vsNew;
	}
	public void setVsNew(OBDtoAdcVServerF5 vsNew)
	{
		this.vsNew = vsNew;
	}
	public ArrayList<OBDtoAdcConfigNodeF5> getNodeConfigList()
	{
		return nodeConfigList;
	}
	public void setNodeConfigList(ArrayList<OBDtoAdcConfigNodeF5> nodeConfigList)
	{
		this.nodeConfigList = nodeConfigList;
	}
	public OBDtoAdcConfigPoolF5 getPoolConfig()
	{
		return poolConfig;
	}
	public void setPoolConfig(OBDtoAdcConfigPoolF5 poolConfig)
	{
		this.poolConfig = poolConfig;
	}
	public int getUseynChange()
	{
		return useynChange;
	}
	public void setUseynChange(int useynChange)
	{
		this.useynChange = useynChange;
	}
	public int getVlanFilterChange()
	{
		return vlanFilterChange;
	}
	public void setVlanFilterChange(int vlanFilterChange)
	{
		this.vlanFilterChange = vlanFilterChange;
	}
}