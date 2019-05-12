package kr.openbase.adcsmart.service.dto;

public class OBDtoVrrpInfo
{
	private Integer adcIndex=0;
	private Integer syncStatus=0;
	private String 	peerIP="";		//Peer ADC IP
	private Integer priority=0;
	private Integer trackVrs=0;		//Virtual Server 
	private Integer trackPorts=0;	//Physical Ports
	private Integer trackInt=0;		//IP Interface
	private Integer trackL4pts=0;	//SLB Ports 
	private Integer trackReals=0;	//Real Server 
	private Integer trackHsrp=0;	//HSRP
	private Integer trackHsrv=0;	//HSRP with VLAN 
	private Integer sharing=0;		//enable=1, disable=0
	@Override
	public String toString()
	{
		return "OBDtoVrrpInfo [adcIndex=" + adcIndex + ", syncStatus="
				+ syncStatus + ", peerIP=" + peerIP + ", trackPriority="
				+ priority + ", trackVrs=" + trackVrs + ", trackPorts="
				+ trackPorts + ", trackInt=" + trackInt + ", trackL4pts="
				+ trackL4pts + ", trackReals=" + trackReals + ", trackHsrp="
				+ trackHsrp + ", trackHsrv=" + trackHsrv + ", sharing="
				+ sharing + "]";
	}
	public String getPeerIP()
	{
		return peerIP;
	}
	public Integer getSyncStatus()
	{
		return syncStatus;
	}
	public void setSyncStatus(Integer syncStatus)
	{
		this.syncStatus = syncStatus;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public void setPeerIP(String peerIP)
	{
		this.peerIP = peerIP;
	}
	public Integer getPriority()
	{
		return priority;
	}
	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}
	public Integer getTrackVrs()
	{
		return trackVrs;
	}
	public void setTrackVrs(Integer trackVrs)
	{
		this.trackVrs = trackVrs;
	}
	public Integer getTrackPorts()
	{
		return trackPorts;
	}
	public void setTrackPorts(Integer trackPorts)
	{
		this.trackPorts = trackPorts;
	}
	public Integer getTrackInt()
	{
		return trackInt;
	}
	public void setTrackInt(Integer trackInt)
	{
		this.trackInt = trackInt;
	}
	public Integer getTrackL4pts()
	{
		return trackL4pts;
	}
	public void setTrackL4pts(Integer trackL4pts)
	{
		this.trackL4pts = trackL4pts;
	}
	public Integer getTrackReals()
	{
		return trackReals;
	}
	public void setTrackReals(Integer trackReals)
	{
		this.trackReals = trackReals;
	}
	public Integer getTrackHsrp()
	{
		return trackHsrp;
	}
	public void setTrackHsrp(Integer trackHsrp)
	{
		this.trackHsrp = trackHsrp;
	}
	public Integer getTrackHsrv()
	{
		return trackHsrv;
	}
	public void setTrackHsrv(Integer trackHsrv)
	{
		this.trackHsrv = trackHsrv;
	}
	public Integer getSharing()
	{
		return sharing;
	}
	public void setSharing(Integer sharing)
	{
		this.sharing = sharing;
	}	
}
