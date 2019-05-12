package kr.openbase.adcsmart.service.dto.history;

import java.io.Serializable;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;

public class OBDtoAdcConfigVServerAlteon implements Serializable
{
	private static final long serialVersionUID = 10L;
	private int change; 
	private int useYNChange;
	private int alteonIdChange;
	private int nameChange;
	private int vIPChange;
	private int vrrpYNChange;
	private int routerIndexChange;
	private int vrIndexChange;
	private int ifNumChange;
	private int serviceChange;
	private int golbalPoolChange;
	private int golbalNodeChange;

	private OBDtoAdcVServerAlteon vsOld;
	private OBDtoAdcVServerAlteon vsNew;
	
	private ArrayList<OBDtoAdcConfigVServiceAlteon> serviceConfigList; //바뀐 service 정보 목록
	private ArrayList<OBDtoAdcConfigPoolAlteon> poolConfigList;		//바뀐 pool 정보 목록
	private ArrayList<OBDtoAdcConfigNodeAlteon> nodeConfigList;		//바뀐 node 정보 목록
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigVServerAlteon [change=" + change
				+ ", useYNChange=" + useYNChange + ", alteonIdChange="
				+ alteonIdChange + ", nameChange=" + nameChange
				+ ", vIPChange=" + vIPChange + ", vrrpYNChange=" + vrrpYNChange
				+ ", routerIndexChange=" + routerIndexChange
				+ ", vrIndexChange=" + vrIndexChange + ", ifNumChange="
				+ ifNumChange + ", serviceChange=" + serviceChange
				+ ", golbalPoolChange=" + golbalPoolChange
				+ ", golbalNodeChange=" + golbalNodeChange + ", vsOld=" + vsOld
				+ ", vsNew=" + vsNew + ", serviceConfigList="
				+ serviceConfigList + ", poolConfigList=" + poolConfigList
				+ ", nodeConfigList=" + nodeConfigList + "]";
	}
	public int getChange()
	{
		return change;
	}
	public void setChange(int change)
	{
		this.change = change;
	}
	public int getUseYNChange()
	{
		return useYNChange;
	}
	public void setUseYNChange(int useYNChange)
	{
		this.useYNChange = useYNChange;
	}
	public int getAlteonIdChange()
	{
		return alteonIdChange;
	}
	public void setAlteonIdChange(int alteonIdChange)
	{
		this.alteonIdChange = alteonIdChange;
	}
	public int getNameChange()
	{
		return nameChange;
	}
	public void setNameChange(int nameChange)
	{
		this.nameChange = nameChange;
	}
	public int getvIPChange()
	{
		return vIPChange;
	}
	public void setvIPChange(int vIPChange)
	{
		this.vIPChange = vIPChange;
	}
	public int getVrrpYNChange()
	{
		return vrrpYNChange;
	}
	public void setVrrpYNChange(int vrrpYNChange)
	{
		this.vrrpYNChange = vrrpYNChange;
	}
	public int getRouterIndexChange()
	{
		return routerIndexChange;
	}
	public void setRouterIndexChange(int routerIndexChange)
	{
		this.routerIndexChange = routerIndexChange;
	}
	public int getVrIndexChange()
	{
		return vrIndexChange;
	}
	public void setVrIndexChange(int vrIndexChange)
	{
		this.vrIndexChange = vrIndexChange;
	}
	public int getIfNumChange()
	{
		return ifNumChange;
	}
	public void setIfNumChange(int ifNumChange)
	{
		this.ifNumChange = ifNumChange;
	}
	public int getServiceChange()
	{
		return serviceChange;
	}
	public void setServiceChange(int serviceChange)
	{
		this.serviceChange = serviceChange;
	}
	public int getGolbalPoolChange()
	{
		return golbalPoolChange;
	}
	public void setGolbalPoolChange(int golbalPoolChange)
	{
		this.golbalPoolChange = golbalPoolChange;
	}
	public int getGolbalNodeChange()
	{
		return golbalNodeChange;
	}
	public void setGolbalNodeChange(int golbalNodeChange)
	{
		this.golbalNodeChange = golbalNodeChange;
	}
	public OBDtoAdcVServerAlteon getVsOld()
	{
		return vsOld;
	}
	public void setVsOld(OBDtoAdcVServerAlteon vsOld)
	{
		this.vsOld = vsOld;
	}
	public OBDtoAdcVServerAlteon getVsNew()
	{
		return vsNew;
	}
	public void setVsNew(OBDtoAdcVServerAlteon vsNew)
	{
		this.vsNew = vsNew;
	}
	public ArrayList<OBDtoAdcConfigVServiceAlteon> getServiceConfigList()
	{
		return serviceConfigList;
	}
	public void setServiceConfigList(
			ArrayList<OBDtoAdcConfigVServiceAlteon> serviceConfigList)
	{
		this.serviceConfigList = serviceConfigList;
	}
	public ArrayList<OBDtoAdcConfigPoolAlteon> getPoolConfigList()
	{
		return poolConfigList;
	}
	public void setPoolConfigList(ArrayList<OBDtoAdcConfigPoolAlteon> poolConfigList)
	{
		this.poolConfigList = poolConfigList;
	}
	public ArrayList<OBDtoAdcConfigNodeAlteon> getNodeConfigList()
	{
		return nodeConfigList;
	}
	public void setNodeConfigList(ArrayList<OBDtoAdcConfigNodeAlteon> nodeConfigList)
	{
		this.nodeConfigList = nodeConfigList;
	}
}