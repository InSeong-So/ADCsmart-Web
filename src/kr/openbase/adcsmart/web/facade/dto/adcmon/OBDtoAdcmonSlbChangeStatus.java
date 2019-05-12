package kr.openbase.adcsmart.web.facade.dto.adcmon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoDashboardSlbChangeStatus;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

public class OBDtoAdcmonSlbChangeStatus
{
	private ArrayList<OBDtoDataObj> changeHistory;
	private ArrayList<OBDtoAdcmonConfigHistory> logList;
	@Override
	public String toString()
	{
		return "OBDtoAdcmonSlbChangeStatus [changeHistory=" + changeHistory
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
	public ArrayList<OBDtoAdcmonConfigHistory> getLogList()
	{
		return logList;
	}
	public void setLogList(ArrayList<OBDtoAdcmonConfigHistory> logList)
	{
		this.logList = logList;
	}
	
	public OBDtoAdcmonSlbChangeStatus toAdcmonSlbChangeStatus(OBDtoDashboardSlbChangeStatus item)
	{
		
		OBDtoAdcmonSlbChangeStatus retVal = new OBDtoAdcmonSlbChangeStatus();
		
		//ArrayList<OBDtoDataObj> changeHistory = item.getChangeHistory();
		retVal.setChangeHistory(item.getChangeHistory());
		
		
		ArrayList<OBDtoAdcConfigHistory> logListFrom = item.getLogList();
		ArrayList<OBDtoAdcmonConfigHistory> logListTo = new ArrayList<OBDtoAdcmonConfigHistory>();
		// logList 변환.
		int a =1;
		for(OBDtoAdcConfigHistory item1: logListFrom)
		{
			
			OBDtoAdcmonConfigHistory obj = new OBDtoAdcmonConfigHistory();
			
			Date Now = new Date();
			Date changeTime = item1.getOccurTime();
			long Difference = (( Now.getTime() - changeTime.getTime())/1000) ;

			if (Difference < OBDefineWeb.ONE_HOUR)
			{// 1시간 미만 . "00분" 표시.
				long min = (Difference/(60L));
				obj.setContent(Long.toString(min) + ' ' + OBDefineWeb.getDefineWeb(OBDefineWeb.MIN));
				obj.setRank(a++);
			}
			else if (Difference < OBDefineWeb.ONE_DAY)
			{// 24시간 미만.. "00시간"		
				long hour = (Difference/(60*60L));				
				obj.setContent(Long.toString(hour) + ' ' + OBDefineWeb.getDefineWeb(OBDefineWeb.HOUR));
				obj.setRank(a++);
			}
			else 
			{// 24시 이상. 발생 날짜로 표시함. 
				SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy.MM.dd" );
				String dTime = formatter.format ( changeTime );
				obj.setContent(dTime);
				obj.setRank(a++);
			}
			//툴팁 adcName, ip 정보
			obj.setAdcName(item1.getAdcName());
			obj.setVsIP(item1.getVsIp());
	
		logListTo.add(obj);
		}
		retVal.setLogList(logListTo);
		return retVal;
	}
}
