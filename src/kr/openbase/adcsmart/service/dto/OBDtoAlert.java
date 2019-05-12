/*
 * a 계정에 할당된 권한 자료 구조.
 */
package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoAlert
{
	private ArrayList<OBDtoAdcAlertLog> alertList;
	private int alertCount;
	private Integer type; //alert의 type. 전체/장애/경고
	
	@Override
	public String toString()
	{
		return "OBDtoAlert [alertList=" + alertList + ", alertCount="
				+ alertCount + ", type=" + type + "]";
	}

	public ArrayList<OBDtoAdcAlertLog> getAlertList()
	{
		return alertList;
	}
	public void setAlertList(ArrayList<OBDtoAdcAlertLog> alertList)
	{
		this.alertList = alertList;
	}
	public void setAlertCount(int alertCount)
	{
		this.alertCount = alertCount;
	}
	public int getAlertCount()
	{
		return alertCount;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
}
