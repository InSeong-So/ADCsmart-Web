package kr.openbase.adcsmart.service.dto.dashboard;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemLog;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;

public class OBDtoDashboardFaultStatus
{
	private ArrayList<OBDtoDataObj> countHistory;
	private ArrayList<OBDtoAdcSystemLog> logList;
	@Override
	public String toString()
	{
		return "OBDtoDashboardFaultStatus [countHistory=" + countHistory
				+ ", logList=" + logList + "]";
	}
	public ArrayList<OBDtoDataObj> getCountHistory()
	{
		return countHistory;
	}
	public void setCountHistory(ArrayList<OBDtoDataObj> countHistory)
	{
		this.countHistory = countHistory;
	}
	public ArrayList<OBDtoAdcSystemLog> getLogList()
	{
		return logList;
	}
	public void setLogList(ArrayList<OBDtoAdcSystemLog> logList)
	{
		this.logList = logList;
	}
}
