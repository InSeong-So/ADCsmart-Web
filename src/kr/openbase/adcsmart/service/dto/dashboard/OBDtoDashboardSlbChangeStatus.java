package kr.openbase.adcsmart.service.dto.dashboard;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;

public class OBDtoDashboardSlbChangeStatus
{
	private ArrayList<OBDtoDataObj> changeHistory;
	private ArrayList<OBDtoAdcConfigHistory> logList;
	@Override
	public String toString()
	{
		return "OBDtoDashboardSlbChangeStatus [changeHistory=" + changeHistory
				+ ", logList=" + logList + "]";
	}
	public ArrayList<OBDtoDataObj> getChangeHistory()
	{
		return changeHistory;
	}
	public void setChangeHistory(ArrayList<OBDtoDataObj> changeHistory)
	{
		this.changeHistory = changeHistory;
	}
	public ArrayList<OBDtoAdcConfigHistory> getLogList()
	{
		return logList;
	}
	public void setLogList(ArrayList<OBDtoAdcConfigHistory> logList)
	{
		this.logList = logList;
	}
}
