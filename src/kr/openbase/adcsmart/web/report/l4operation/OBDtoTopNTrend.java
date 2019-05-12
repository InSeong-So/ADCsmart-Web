package kr.openbase.adcsmart.web.report.l4operation;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoUsageThroughput;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4PerformanceTrend;

public class OBDtoTopNTrend
{
	private Integer rank=0;
	private String name="";
	private String vsName="";
	private Long	maxValue=0L;
	private ArrayList<OBDtoTrendObject> iTemList;
//	private ArrayList<OBDtoTrendObject> first;
//	private ArrayList<OBDtoTrendObject> second;
//	private ArrayList<OBDtoTrendObject> third;
//	private ArrayList<OBDtoTrendObject> fourth;
//	private ArrayList<OBDtoTrendObject> fifth;
//	private ArrayList<OBDtoTrendObject> sixth;
//	private ArrayList<OBDtoTrendObject> seventh;
//	private ArrayList<OBDtoTrendObject> eighth;
//	private ArrayList<OBDtoTrendObject> nineth;
//	private ArrayList<OBDtoTrendObject> tenth;
	
	public static OBDtoTopNTrend toTrendObjectConnection(OBDtoRptL4PerformanceTrend iTem)
	{
		OBDtoTopNTrend retVal = new OBDtoTopNTrend();
		ArrayList<OBDtoTrendObject> trendList = new ArrayList<OBDtoTrendObject>();
		
		String name = iTem.getVsIPAddress()+":"+iTem.getVsPort()+"   " + iTem.getVsName();
		
		long maxVal = 0;
		for(OBDtoUsageThroughput usage:iTem.getUsage())
		{
			OBDtoTrendObject obj = new OBDtoTrendObject();
			obj.setName(name);
			obj.setxAxis(usage.getOccurTime());
			obj.setyAxis(usage.getPps());
			trendList.add(obj);
			
			if(maxVal<obj.getyAxis())
				maxVal = obj.getyAxis();
		}
		retVal.setRank(iTem.getRank());
		retVal.setName(name);
		retVal.setiTemList(trendList);
		retVal.setMaxValue(maxVal);
		retVal.setVsName(OBDtoRptL4PerformanceTrend.toVsName(iTem.getVsName()));
		return retVal;
	}
	
	public static OBDtoTopNTrend toTrendObjectThroughput(OBDtoRptL4PerformanceTrend iTem)
	{
		OBDtoTopNTrend retVal = new OBDtoTopNTrend();
		ArrayList<OBDtoTrendObject> trendList = new ArrayList<OBDtoTrendObject>();
		
		String name = iTem.getVsIPAddress()+":"+iTem.getVsPort()+"   " + iTem.getVsName();
		
		long maxVal = 0;
		for(OBDtoUsageThroughput usage:iTem.getUsage())
		{
			OBDtoTrendObject obj = new OBDtoTrendObject();
			obj.setName(name);
			obj.setxAxis(usage.getOccurTime());
			obj.setyAxis(usage.getBps());
			trendList.add(obj);
			
			if(maxVal<obj.getyAxis())
				maxVal = obj.getyAxis();
		}
		retVal.setRank(iTem.getRank());
		retVal.setName( name);
		retVal.setiTemList(trendList);
		retVal.setMaxValue(maxVal);
		retVal.setVsName(OBDtoRptL4PerformanceTrend.toVsName(iTem.getVsName()));
		return retVal;
	}
	
	
	@Override
	public String toString()
	{
		return "OBDtoTopNTrend [rank=" + rank + ", name=" + name + ", vsName="
				+ vsName + ", maxValue=" + maxValue + ", iTemList=" + iTemList
				+ "]";
	}

	public Integer getRank()
	{
		return rank;
	}

	public void setRank(Integer rank)
	{
		this.rank = rank;
	}

	public ArrayList<OBDtoTrendObject> getiTemList()
	{
		return iTemList;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setiTemList(ArrayList<OBDtoTrendObject> iTemList)
	{
		this.iTemList = iTemList;
	}

	public Long getMaxValue()
	{
		return maxValue;
	}

	public void setMaxValue(Long maxValue)
	{
		this.maxValue = maxValue;
	}

	public String getVsName()
	{
		return vsName;
	}

	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
}
