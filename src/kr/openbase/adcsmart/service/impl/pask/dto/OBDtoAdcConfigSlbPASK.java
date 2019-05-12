package kr.openbase.adcsmart.service.impl.pask.dto;

import java.util.ArrayList;

public class OBDtoAdcConfigSlbPASK
{
	private ArrayList<OBDtoAdcVServerPASK> 		vsList;// = new ArrayList<OBAdcVirtualServer>();
	private ArrayList<OBDtoRealServerInfoPASK> 	realList;
	private ArrayList<OBDtoAdcHealthCheckPASK> 	healthList;// health check 목록..
	@Override
	public String toString()
	{
		return "OBDtoAdcConfigSlbPASK [vsList=" + vsList + ", realList=" + realList + ", healthList=" + healthList + "]";
	}
	public ArrayList<OBDtoAdcVServerPASK> getVsList()
	{
		return vsList;
	}
	public void setVsList(ArrayList<OBDtoAdcVServerPASK> vsList)
	{
		this.vsList = vsList;
	}
	public ArrayList<OBDtoRealServerInfoPASK> getRealList()
	{
		return realList;
	}
	public void setRealList(ArrayList<OBDtoRealServerInfoPASK> realList)
	{
		this.realList = realList;
	}
	public ArrayList<OBDtoAdcHealthCheckPASK> getHealthList()
	{
		return healthList;
	}
	public void setHealthList(ArrayList<OBDtoAdcHealthCheckPASK> healthList)
	{
		this.healthList = healthList;
	}
}
