package kr.openbase.adcsmart.web.report.admin;

import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.web.report.impl.OBDtoReportTextHdr;

public class ChartMultiXYDto 
{
	private Date x;
	private ArrayList<Long> y;
	
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
	public ArrayList<Long> getY() 
	{
		return y;
	}
	public void setY(ArrayList<Long> y) 
	{
		this.y = y;
	}
	
	@Override
	public String toString() 
	{
		return "ChartXYDto [x=" + x + ", y=" + y + "]";
	}
	
}
