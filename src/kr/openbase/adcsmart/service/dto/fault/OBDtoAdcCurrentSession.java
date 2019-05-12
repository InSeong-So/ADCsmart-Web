package kr.openbase.adcsmart.service.dto.fault;

import java.util.ArrayList;

/**
 * FLB, SLB current connection 추이
 * @author ykkim
*/
public class OBDtoAdcCurrentSession
{
	private Long		current;
	private Long		yesterdayAvg;
	private int         lbClass; //세션 데이터 구성: ALL, SLB, FLB / OBDefine.ADC_LB_CLASS
	private ArrayList<OBDtoFlbSlbSession> history; //시간마다 FLB/SLB connection 값
	
	@Override
	public String toString()
	{
		return "OBDtoAdcCurrentSession [current=" + current + ", yesterdayAvg="
				+ yesterdayAvg + ", lbClass=" + lbClass + ", history="
				+ history + "]";
	}
	public Long getCurrent()
	{
		return current;
	}
	public void setCurrent(Long current)
	{
		this.current = current;
	}
	public Long getYesterdayAvg()
	{
		return yesterdayAvg;
	}
	public void setYesterdayAvg(Long yesterdayAvg)
	{
		this.yesterdayAvg = yesterdayAvg;
	}
	public ArrayList<OBDtoFlbSlbSession> getHistory()
	{
		return history;
	}
	public void setHistory(ArrayList<OBDtoFlbSlbSession> history)
	{
		this.history = history;
	}
	public int getLbClass()
	{
		return lbClass;
	}
	public void setLbClass(int lbClass)
	{
		this.lbClass = lbClass;
	}
}
