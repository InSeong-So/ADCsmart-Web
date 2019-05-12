package kr.openbase.adcsmart.service.dto.fault;

public class OBDtoFaultSessionInfo
{
	private Integer     adcIndex;
	private String      adcName;
	private String 		srcIP;// client ip
	private String 		dstIP;// vip 
	private String  	realIP;// real server ip..
	private String 	    srcPort;
	private String 	    dstPort;
	private String      realPort;// real server port
	private String      damPort;// for alteon
	private Integer 	protocol;
	private Integer 	agingTime;
	private String 		spNumber;//해당 SP. for alteon.
	@Override
	public String toString()
	{
		return "OBDtoFaultSessionInfo [adcIndex=" + adcIndex + ", adcName=" + adcName + ", srcIP=" + srcIP + ", dstIP=" + dstIP + ", realIP=" + realIP + ", srcPort=" + srcPort + ", dstPort=" + dstPort + ", realPort=" + realPort + ", damPort=" + damPort + ", protocol=" + protocol + ", agingTime=" + agingTime + ", spNumber=" + spNumber + "]";
	}
	public String getDamPort()
	{
		return damPort;
	}
	public void setDamPort(String damPort)
	{
		this.damPort = damPort;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getSrcIP()
	{
		return srcIP;
	}
	public void setSrcIP(String srcIP)
	{
		this.srcIP = srcIP;
	}
	public String getDstIP()
	{
		return dstIP;
	}
	public void setDstIP(String dstIP)
	{
		this.dstIP = dstIP;
	}
	public String getRealIP()
	{
		return realIP;
	}
	public void setRealIP(String realIP)
	{
		this.realIP = realIP;
	}
	public String getSrcPort()
	{
		return srcPort;
	}
	public void setSrcPort(String srcPort)
	{
		this.srcPort = srcPort;
	}
	public String getDstPort()
	{
		return dstPort;
	}
	public void setDstPort(String dstPort)
	{
		this.dstPort = dstPort;
	}
	public String getRealPort()
	{
		return realPort;
	}
	public void setRealPort(String realPort)
	{
		this.realPort = realPort;
	}
	public Integer getProtocol()
	{
		return protocol;
	}
	public void setProtocol(Integer protocol)
	{
		this.protocol = protocol;
	}
	public Integer getAgingTime()
	{
		return agingTime;
	}
	public void setAgingTime(Integer agingTime)
	{
		this.agingTime = agingTime;
	}
	public String getSpNumber()
	{
		return spNumber;
	}
	public void setSpNumber(String spNumber)
	{
		this.spNumber = spNumber;
	}
}
