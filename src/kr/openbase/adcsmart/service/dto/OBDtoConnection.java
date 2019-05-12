package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;

public class OBDtoConnection
{
	private ArrayList<OBDtoUsageConnection> connectionList;
	private ArrayList<OBDtoAdcConfigHistory> confEventList;
	@Override
	public String toString()
	{
		return "OBDtoConnection [connectionList=" + connectionList
				+ ", confEventList=" + confEventList + "]";
	}
	public ArrayList<OBDtoUsageConnection> getConnectionList()
	{
		return connectionList;
	}
	public void setConnectionList(ArrayList<OBDtoUsageConnection> connectionList)
	{
		this.connectionList = connectionList;
	}
	public ArrayList<OBDtoAdcConfigHistory> getConfEventList()
	{
		return confEventList;
	}
	public void setConfEventList(ArrayList<OBDtoAdcConfigHistory> confEventList)
	{
		this.confEventList = confEventList;
	}
}
