package kr.openbase.adcsmart.web.report.admin;

import java.util.Date;

import kr.openbase.adcsmart.web.report.impl.OBDtoReportTextHdr;

public class ChartXYDto 
{
	private Date x;
	private Long y;

	private OBDtoReportTextHdr textHdr;
	
	public OBDtoReportTextHdr getTextHdr()
	{
		return textHdr;
	}
	public void setTextHdr(OBDtoReportTextHdr textHdr)
	{
		this.textHdr = textHdr;
	}
	public Date getX() 
	{
		return x;
	}
	public void setX(Date x) 
	{
		this.x = x;
	}
	public Long getY() 
	{
		return y;
	}
	public void setY(Long y) 
	{
		this.y = y;
	}
	
	@Override
	public String toString() 
	{
		return "ChartXYDto [x=" + x + ", y=" + y + "]";
	}
	
}
