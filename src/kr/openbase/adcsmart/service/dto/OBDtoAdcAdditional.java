package kr.openbase.adcsmart.service.dto;

import kr.openbase.adcsmart.service.utility.OBDefine;

public class OBDtoAdcAdditional
{
	private Integer adcIndex=0;
	private Integer syncState=OBDefine.STATE_DISABLE;
	private String peerIP="";
	private Integer vrrpIPVer=0;
	private Integer vrrpPriority=0;
	private Integer vrrpShare=0;
	private Integer vrrpTrackVrs=0;
	private Integer vrrpTrackIfs=0;
	private Integer vrrpTrackPorts=0;
	private Integer vrrpTrackL4pts=0;
	private Integer vrrpTrackReals=0;
	private Integer vrrpTrackHsrp=0;
	private Integer vrrpTrackHsrv=0;
	@Override
	public String toString()
	{
		return "OBDtoAdcAdditional [adcIndex=" + adcIndex + ", syncState="
				+ syncState + ", peerIP=" + peerIP + ", vrrpIPVer=" + vrrpIPVer
				+ ", vrrpPriority=" + vrrpPriority + ", vrrpShare=" + vrrpShare
				+ ", vrrpTrackVrs=" + vrrpTrackVrs + ", vrrpTrackIfs="
				+ vrrpTrackIfs + ", vrrpTrackPorts=" + vrrpTrackPorts
				+ ", vrrpTrackL4pts=" + vrrpTrackL4pts + ", vrrpTrackReals="
				+ vrrpTrackReals + ", vrrpTrackHsrp=" + vrrpTrackHsrp
				+ ", vrrpTrackHsrv=" + vrrpTrackHsrv + "]";
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public Integer getSyncState()
	{
		return syncState;
	}
	public void setSyncState(Integer syncState)
	{
		this.syncState = syncState;
	}
	public String getPeerIP()
	{
		return peerIP;
	}
	public void setPeerIP(String peerIP)
	{
		this.peerIP = peerIP;
	}
	public Integer getVrrpIPVer()
	{
		return vrrpIPVer;
	}
	public void setVrrpIPVer(Integer vrrpIPVer)
	{
		this.vrrpIPVer = vrrpIPVer;
	}
	public Integer getVrrpPriority()
	{
		return vrrpPriority;
	}
	public void setVrrpPriority(Integer vrrpPriority)
	{
		this.vrrpPriority = vrrpPriority;
	}
	public Integer getVrrpShare()
	{
		return vrrpShare;
	}
	public void setVrrpShare(Integer vrrpShare)
	{
		this.vrrpShare = vrrpShare;
	}
	public Integer getVrrpTrackVrs()
	{
		return vrrpTrackVrs;
	}
	public void setVrrpTrackVrs(Integer vrrpTrackVrs)
	{
		this.vrrpTrackVrs = vrrpTrackVrs;
	}
	public Integer getVrrpTrackIfs()
	{
		return vrrpTrackIfs;
	}
	public void setVrrpTrackIfs(Integer vrrpTrackIfs)
	{
		this.vrrpTrackIfs = vrrpTrackIfs;
	}
	public Integer getVrrpTrackPorts()
	{
		return vrrpTrackPorts;
	}
	public void setVrrpTrackPorts(Integer vrrpTrackPorts)
	{
		this.vrrpTrackPorts = vrrpTrackPorts;
	}
	public Integer getVrrpTrackL4pts()
	{
		return vrrpTrackL4pts;
	}
	public void setVrrpTrackL4pts(Integer vrrpTrackL4pts)
	{
		this.vrrpTrackL4pts = vrrpTrackL4pts;
	}
	public Integer getVrrpTrackReals()
	{
		return vrrpTrackReals;
	}
	public void setVrrpTrackReals(Integer vrrpTrackReals)
	{
		this.vrrpTrackReals = vrrpTrackReals;
	}
	public Integer getVrrpTrackHsrp()
	{
		return vrrpTrackHsrp;
	}
	public void setVrrpTrackHsrp(Integer vrrpTrackHsrp)
	{
		this.vrrpTrackHsrp = vrrpTrackHsrp;
	}
	public Integer getVrrpTrackHsrv()
	{
		return vrrpTrackHsrv;
	}
	public void setVrrpTrackHsrv(Integer vrrpTrackHsrv)
	{
		this.vrrpTrackHsrv = vrrpTrackHsrv;
	}	
}
