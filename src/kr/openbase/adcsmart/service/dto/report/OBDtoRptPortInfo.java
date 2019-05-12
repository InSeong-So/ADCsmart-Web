package kr.openbase.adcsmart.service.dto.report;

import java.util.ArrayList;
import java.util.Date;


public class OBDtoRptPortInfo
{
	private String portName;
	private Date beginTime;// 보고서 기간. 시작.
	private Date endTime;// 보고서 종료 시각.
	
	private ArrayList<OBDtoRptPortErrDiscard> errDiscardsList;

	@Override
	public String toString()
	{
		return "OBDtoRptPortInfo [portName=" + portName + ", beginTime="
				+ beginTime + ", endTime=" + endTime + ", errDiscardsList="
				+ errDiscardsList + "]";
	}

	public String getPortName()
	{
		return portName;
	}

	public void setPortName(String portName)
	{
		this.portName = portName;
	}

	public Date getBeginTime()
	{
		return beginTime;
	}

	public void setBeginTime(Date beginTime)
	{
		this.beginTime = beginTime;
	}

	public Date getEndTime()
	{
		return endTime;
	}

	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}

	public ArrayList<OBDtoRptPortErrDiscard> getErrDiscardsList()
	{
		return errDiscardsList;
	}

	public void setErrDiscardsList(ArrayList<OBDtoRptPortErrDiscard> errDiscardsList)
	{
		this.errDiscardsList = errDiscardsList;
	}
	
}
