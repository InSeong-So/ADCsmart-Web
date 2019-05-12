package kr.openbase.adcsmart.service.snmp.pas.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcHealthCheckPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;

public class OBDtoAdcConfigSlbPAS
{
	private ArrayList<OBDtoAdcVServerPAS> vsList		=null;// = new ArrayList<OBAdcVirtualServer>();
	private ArrayList<OBDtoAdcHealthCheckPAS> healthList=null;// health check 목록..

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigSlbPAS [vsList=" + vsList + ", healthList="
				+ healthList + "]";
	}

	public ArrayList<OBDtoAdcHealthCheckPAS> getHealthList()
	{
		return healthList;
	}
	public void setHealthList(ArrayList<OBDtoAdcHealthCheckPAS> healthList)
	{
		this.healthList = healthList;
	}
	public ArrayList<OBDtoAdcVServerPAS> getVsList()
	{
		return vsList;
	}
	public void setVsList(ArrayList<OBDtoAdcVServerPAS> vsList)
	{
		this.vsList = vsList;
	}
}
