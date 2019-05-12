package kr.openbase.adcsmart.service.dto.report;

public class OBDtoRptL4PerformanceInfo
{
	private static final int NAME_MIN_LENGTH = 28;
	private static final int DOT_LENGTH = 4;
	
	private String 	objName;
	private Integer	rank;
	private String 	adcName;
	private Integer adcType;
	private String 	vsName;
	private String 	vsIPAddress;
	private Integer vsPort;
	private Long 	avgBps;
	private Long 	avgConnections;
	
	
	public static String toVsName(String vsName)
	{
		String ret = "";
		if(vsName.length()>NAME_MIN_LENGTH)
		{
			ret = vsName.substring(0,(NAME_MIN_LENGTH-DOT_LENGTH))+" ...";
		}
		else
		{
			ret = vsName;
		}
		return ret;
	}
	public static String toAdcName(String adcName)
	{
		String ret = "";
		if(adcName.length()>NAME_MIN_LENGTH)
		{
			ret = adcName.substring(0,(NAME_MIN_LENGTH-DOT_LENGTH))+" ...";
		}
		else
		{
			ret = adcName;
		}
		return ret;
	}
	
	@Override
	public String toString()
	{
		return "OBDtoRptL4PerformanceInfo [objName=" + objName + ", rank="
				+ rank + ", adcName=" + adcName + ", adcType=" + adcType
				+ ", vsName=" + vsName + ", vsIPAddress=" + vsIPAddress
				+ ", vsPort=" + vsPort + ", avgBps=" + avgBps
				+ ", avgConnections=" + avgConnections + "]";
	}
	public String getObjName()
	{
		return objName;
	}
	public void setObjName(String objName)
	{
		this.objName = objName;
	}
	public Integer getAdcType()
	{
		return adcType;
	}
	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}
	public Integer getVsPort()
	{
		return vsPort;
	}
	public void setVsPort(Integer vsPort)
	{
		this.vsPort = vsPort;
	}
	public Integer getRank()
	{
		return rank;
	}
	public void setRank(Integer rank)
	{
		this.rank = rank;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getVsName()
	{
		return vsName;
	}
	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
	public String getVsIPAddress()
	{
		return vsIPAddress;
	}
	public void setVsIPAddress(String vsIPAddress)
	{
		this.vsIPAddress = vsIPAddress;
	}
	public Long getAvgBps()
	{
		return avgBps;
	}
	public void setAvgBps(Long avgBps)
	{
		this.avgBps = avgBps;
	}
	public Long getAvgConnections()
	{
		return avgConnections;
	}
	public void setAvgConnections(Long avgConnections)
	{
		this.avgConnections = avgConnections;
	}
}
