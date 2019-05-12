package kr.openbase.adcsmart.web.report.l4operation;

import java.util.Date;

public class OBDtoTrendObject
{
	private String name;
	private Date	xAxis;
	private Long	yAxis;
	@Override
	public String toString()
	{
		return "OBDtoTrendObject [name=" + name + ", xAxis=" + xAxis
				+ ", yAxis=" + yAxis + "]";
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Date getxAxis()
	{
		return xAxis;
	}
	public void setxAxis(Date xAxis)
	{
		this.xAxis = xAxis;
	}
	public Long getyAxis()
	{
		return yAxis;
	}
	public void setyAxis(Long yAxis)
	{
		this.yAxis = yAxis;
	}
}
