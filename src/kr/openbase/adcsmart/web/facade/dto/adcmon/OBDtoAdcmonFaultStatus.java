package kr.openbase.adcsmart.web.facade.dto.adcmon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemLog;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardFaultStatus;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

public class OBDtoAdcmonFaultStatus
{
	private ArrayList<OBDtoDataObj> changeHistory;
	private ArrayList<OBDtoAdcmonSystemLog> logList;
	@Override
	public String toString()
	{
		return "OBDtoAdcmonFaultStatus [changeHistory=" + changeHistory
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
	public ArrayList<OBDtoAdcmonSystemLog> getLogList()
	{
		return logList;
	}
	public void setLogList(ArrayList<OBDtoAdcmonSystemLog> logList)
	{
		this.logList = logList;
	}
	public OBDtoAdcmonFaultStatus toAdcmonFaultStatus(OBDtoDashboardFaultStatus item)
	{
		
		OBDtoAdcmonFaultStatus retVal = new OBDtoAdcmonFaultStatus();
		
		//ArrayList<OBDtoDataObj> changeHistory = item.getChangeHistory();
		retVal.setChangeHistory(item.getCountHistory());
		
		
		ArrayList<OBDtoAdcSystemLog> logListFrom = item.getLogList();
		ArrayList<OBDtoAdcmonSystemLog> logListTo = new ArrayList<OBDtoAdcmonSystemLog>();
		// logList 변환.
		int a =1;
		for(OBDtoAdcSystemLog item1: logListFrom)
		{
			
			OBDtoAdcmonSystemLog obj = new OBDtoAdcmonSystemLog();
			
			Date Now = new Date();
			Date changeTime = item1.getOccurTime();
			long Difference = (( Now.getTime() - changeTime.getTime())/1000) ;
					
			if (Difference < OBDefineWeb.ONE_HOUR)
			{// 1시간 미만 . "00분" 표시.
				long min = (Difference/(60L));				
				obj.setContent(item1.getEvent());
				obj.setOccurred(min + " " + OBDefineWeb.getDefineWeb(OBDefineWeb.MIN));
				obj.setRank(a++);
				obj.setDateChk(true);
			}
			else if (Difference < OBDefineWeb.ONE_DAY)
			{// 24시간 미만.. "00시간"		
				long hour = (Difference/(60*60L));
				obj.setContent(item1.getEvent());
				obj.setOccurred(hour + " " + OBDefineWeb.getDefineWeb(OBDefineWeb.HOUR));
				obj.setRank(a++);
				obj.setDateChk(true);
			}
			else 
			{// 24시 이상. 발생 날짜로 표시함. 
				SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy.MM.dd" );
				String dTime = formatter.format ( changeTime );
				obj.setContent(item1.getEvent());
				obj.setOccurred(dTime);
				obj.setRank(a++);
				obj.setDateChk(false);
			}
	
		logListTo.add(obj);
		}
		retVal.setLogList(logListTo);
		return retVal;
	}
}

