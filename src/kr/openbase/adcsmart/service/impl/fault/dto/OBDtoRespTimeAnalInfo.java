package kr.openbase.adcsmart.service.impl.fault.dto;

public class OBDtoRespTimeAnalInfo
{
	private Integer endPointAvgTime;
	private Integer endPointMinTime;
	private Integer endPointMaxTime;
	private Integer dataCenterAvgTime;
	private Integer dataCenterMinTime;
	private Integer dataCenterMaxTime;
	@Override
	public String toString()
	{
		return String.format("OBDtoRespTimeAnalInfo [endPointAvgTime=%s, endPointMinTime=%s, endPointMaxTime=%s, dataCenterAvgTime=%s, dataCenterMinTime=%s, dataCenterMaxTime=%s]", endPointAvgTime, endPointMinTime, endPointMaxTime, dataCenterAvgTime, dataCenterMinTime, dataCenterMaxTime);
	}
	public Integer getEndPointAvgTime()
	{
		return endPointAvgTime;
	}
	public void setEndPointAvgTime(Integer endPointAvgTime)
	{
		this.endPointAvgTime = endPointAvgTime;
	}
	public Integer getEndPointMinTime()
	{
		return endPointMinTime;
	}
	public void setEndPointMinTime(Integer endPointMinTime)
	{
		this.endPointMinTime = endPointMinTime;
	}
	public Integer getEndPointMaxTime()
	{
		return endPointMaxTime;
	}
	public void setEndPointMaxTime(Integer endPointMaxTime)
	{
		this.endPointMaxTime = endPointMaxTime;
	}
	public Integer getDataCenterAvgTime()
	{
		return dataCenterAvgTime;
	}
	public void setDataCenterAvgTime(Integer dataCenterAvgTime)
	{
		this.dataCenterAvgTime = dataCenterAvgTime;
	}
	public Integer getDataCenterMinTime()
	{
		return dataCenterMinTime;
	}
	public void setDataCenterMinTime(Integer dataCenterMinTime)
	{
		this.dataCenterMinTime = dataCenterMinTime;
	}
	public Integer getDataCenterMaxTime()
	{
		return dataCenterMaxTime;
	}
	public void setDataCenterMaxTime(Integer dataCenterMaxTime)
	{
		this.dataCenterMaxTime = dataCenterMaxTime;
	}
}
