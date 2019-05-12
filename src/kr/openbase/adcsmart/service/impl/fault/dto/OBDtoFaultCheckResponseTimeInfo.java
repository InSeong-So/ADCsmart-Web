package kr.openbase.adcsmart.service.impl.fault.dto;

public class OBDtoFaultCheckResponseTimeInfo
{
	private Integer currTime = 0;// msec 
	private Integer prevTime = 0;
	private Integer avgTime = 0;
	private Integer currRate = 0;// 비율. current, previous, average 중에 큰값을 100으로 한 비율
	private Integer prevRate = 0;
	private Integer avgRate = 0;
	
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultCheckResponseTimeInfo [currTime=%s, prevTime=%s, avgTime=%s, currRate=%s, prevRate=%s, avgRate=%s]", currTime, prevTime, avgTime, currRate, prevRate, avgRate);
	}
	public Integer getCurrTime()
	{
		return currTime;
	}
	public void setCurrTime(Integer currTime)
	{
		this.currTime = currTime;
	}
	public Integer getPrevTime()
	{
		return prevTime;
	}
	public void setPrevTime(Integer prevTime)
	{
		this.prevTime = prevTime;
	}
	public Integer getAvgTime()
	{
		return avgTime;
	}
	public void setAvgTime(Integer avgTime)
	{
		this.avgTime = avgTime;
	}
	public Integer getCurrRate()
	{
		return currRate;
	}
	public void setCurrRate(Integer currRate)
	{
		this.currRate = currRate;
	}
	public Integer getPrevRate()
	{
		return prevRate;
	}
	public void setPrevRate(Integer prevRate)
	{
		this.prevRate = prevRate;
	}
	public Integer getAvgRate()
	{
		return avgRate;
	}
	public void setAvgRate(Integer avgRate)
	{
		this.avgRate = avgRate;
	}
}
