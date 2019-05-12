package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultSessionInfo;

public class FaultSessionInfoDto
{
	private String 		srcIP;// client ip
	private String 		dstIP;// vip 
	private String 		srcPort;// client port
	private String 		dstPort;// vip port
	private String 		protocol;
	private String 		realIP;// real server ip
	private String 		realPort;// real server port
	private Integer 	agingTime;
	private String 	spNumber;//해당 SP. for alteon.
	@Override
	public String toString()
	{
		return "FaultSessionInfoDto [srcIP=" + srcIP + ", dstIP=" + dstIP + ", srcPort=" + srcPort + ", dstPort=" + dstPort + ", protocol=" + protocol + ", realIP=" + realIP + ", realPort=" + realPort + ", agingTime=" + agingTime + ", spNumber=" + spNumber + ", getRealIP()=" + getRealIP() + ", getRealPort()=" + getRealPort() + ", getSrcIP()=" + getSrcIP() + ", getDstIP()=" + getDstIP() + ", getSrcPort()=" + getSrcPort() + ", getDstPort()=" + getDstPort() + ", getProtocol()=" + getProtocol() + ", getAgingTime()=" + getAgingTime() + ", getSpNumber()=" + getSpNumber() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	public String getRealIP()
	{
		return realIP;
	}
	public void setRealIP(String realIP)
	{
		this.realIP = realIP;
	}
	public String getRealPort()
	{
		return realPort;
	}
	public void setRealPort(String realPort)
	{
		this.realPort = realPort;
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
	public String getProtocol()
	{
		return protocol;
	}
	public void setProtocol(String protocol)
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
	
	public static FaultSessionInfoDto toFaultSessionInfo(OBDtoFaultSessionInfo sessionInfoFromSvc)
	{
		FaultSessionInfoDto info = new FaultSessionInfoDto();
		
		info.setSrcIP(sessionInfoFromSvc.getSrcIP());
		info.setDstPort(sessionInfoFromSvc.getDstPort());
		info.setSrcPort(sessionInfoFromSvc.getSrcPort());
		info.setDstIP(sessionInfoFromSvc.getDstIP());
		info.setRealIP(sessionInfoFromSvc.getRealIP());
		info.setRealPort(sessionInfoFromSvc.getRealPort());
		info.setAgingTime(sessionInfoFromSvc.getAgingTime());
		info.setSpNumber(sessionInfoFromSvc.getSpNumber());
		
		switch(sessionInfoFromSvc.getProtocol().intValue())
		{
		case 6:
			info.setProtocol("TCP");
			break;
		case 17:
			info.setProtocol("UDP");
			break;
		case 1:
			info.setProtocol("ICMP");
			break;
		default:
			info.setProtocol("unknown");
			break;
		}
		return info;
	}	
	public static List<FaultSessionInfoDto> toFaultSessionInfoList(List<OBDtoFaultSessionInfo> sessionInfoFromSvc)
	{
		List<FaultSessionInfoDto> retVal = new ArrayList<FaultSessionInfoDto>();
		for(OBDtoFaultSessionInfo e : sessionInfoFromSvc)
		{
			retVal.add(FaultSessionInfoDto.toFaultSessionInfo(e));
		}
		return retVal;
	}
}
