package kr.openbase.adcsmart.service.dto.fault;

import java.sql.Timestamp;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoElement;

public class OBDtoFaultCheckResultElement
{
	public static final  int	STATUS_NA	=0;
	public static final  int	STATUS_FAIL	=1;
	public static final  int	STATUS_SUCC	=2;
	public static final  int	STATUS_INFO	=3;// 값 변경 금지. 변경시에 리포트 모듈(jasper)도 같이 변경해야 함.

	private		OBDtoElement 			obj;
	private 	Integer					status;// 검사 결과.
	private     Timestamp               startTime;
	private     Timestamp               endTime;
	private     ArrayList<OBDtoFaultCheckResultContent> resultList;
	private     String          		solution;
	private     String                  msgCLI;
	private     Object                  extraInfo;
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultCheckResultElement [obj=%s, status=%s, startTime=%s, endTime=%s, resultList=%s, solution=%s, msgCLI=%s]", obj, status, startTime, endTime, resultList, solution, msgCLI);
	}
	public Object getExtraInfo()
	{
		return extraInfo;
	}
	public void setExtraInfo(Object extraInfo)
	{
		this.extraInfo = extraInfo;
	}
	public String getMsgCLI()
	{
		return msgCLI;
	}
	public void setMsgCLI(String msgCLI)
	{
		this.msgCLI = msgCLI;
	}
	public ArrayList<OBDtoFaultCheckResultContent> getResultList()
	{
		return resultList;
	}
	public void setResultList(ArrayList<OBDtoFaultCheckResultContent> resultList)
	{
		this.resultList = resultList;
	}
	public Timestamp getStartTime()
	{
		return startTime;
	}
	public void setStartTime(Timestamp startTime)
	{
		this.startTime = startTime;
	}
	public Timestamp getEndTime()
	{
		return endTime;
	}
	public void setEndTime(Timestamp endTime)
	{
		this.endTime = endTime;
	}
	public String getSolution()
	{
		return solution;
	}
	public void setSolution(String solution)
	{
		this.solution = solution;
	}
	public OBDtoElement getObj()
	{
		return obj;
	}
	public void setObj(OBDtoElement obj)
	{
		this.obj = obj;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
}
