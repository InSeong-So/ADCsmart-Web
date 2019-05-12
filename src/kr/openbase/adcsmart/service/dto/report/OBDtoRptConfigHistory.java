package kr.openbase.adcsmart.service.dto.report;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;

public class OBDtoRptConfigHistory
{
	private ArrayList<OBDtoAdcConfigHistory> historyList;

	@Override
	public String toString()
	{
		return "OBDtoRptConfigHistory [getHistoryList()=" + getHistoryList()
				+ "]";
	}

	public ArrayList<OBDtoAdcConfigHistory> getHistoryList()
	{
		return historyList;
	}

	public void setHistoryList(ArrayList<OBDtoAdcConfigHistory> historyList)
	{
		this.historyList = historyList;
	}
}
