package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidCfgAdcAdditional
{
	private String confSyncState;
	private String confSyncPeerIP;
	
	private String vrrpID;
	private String vrrpAddr;
	private String vrrpIfIndex;
	private String vrrpPriority;
	private String vrrpShare;
	private String vrrpTrackVrs;
	private String vrrpTrackIfs;
	private String vrrpTrackPorts;
	private String vrrpTrackL4pts;
	private String vrrpTrackReals;
	private String vrrpTrackHsrp;
	private String vrrpTrackHsrv;
	private String vrrpState;
	@Override
	public String toString()
	{
		return "DtoOidCfgAdcAdditional [confSyncState=" + confSyncState
				+ ", confSyncPeerIP=" + confSyncPeerIP + ", vrrpID=" + vrrpID
				+ ", vrrpAddr=" + vrrpAddr + ", vrrpIfIndex=" + vrrpIfIndex
				+ ", vrrpPriority=" + vrrpPriority + ", vrrpShare=" + vrrpShare
				+ ", vrrpTrackVrs=" + vrrpTrackVrs + ", vrrpTrackIfs="
				+ vrrpTrackIfs + ", vrrpTrackPorts=" + vrrpTrackPorts
				+ ", vrrpTrackL4pts=" + vrrpTrackL4pts + ", vrrpTrackReals="
				+ vrrpTrackReals + ", vrrpTrackHsrp=" + vrrpTrackHsrp
				+ ", vrrpTrackHsrv=" + vrrpTrackHsrv + ", vrrpState="
				+ vrrpState + "]";
	}
	public String getVrrpID()
	{
		return vrrpID;
	}
	public void setVrrpID(String vrrpID)
	{
		this.vrrpID = vrrpID;
	}
	public String getVrrpAddr()
	{
		return vrrpAddr;
	}
	public void setVrrpAddr(String vrrpAddr)
	{
		this.vrrpAddr = vrrpAddr;
	}
	public String getVrrpIfIndex()
	{
		return vrrpIfIndex;
	}
	public void setVrrpIfIndex(String vrrpIfIndex)
	{
		this.vrrpIfIndex = vrrpIfIndex;
	}
	public String getVrrpState()
	{
		return vrrpState;
	}
	public void setVrrpState(String vrrpState)
	{
		this.vrrpState = vrrpState;
	}
	public String getConfSyncState()
	{
		return confSyncState;
	}
	public void setConfSyncState(String confSyncState)
	{
		this.confSyncState = confSyncState;
	}
	public String getConfSyncPeerIP()
	{
		return confSyncPeerIP;
	}
	public void setConfSyncPeerIP(String confSyncPeerIP)
	{
		this.confSyncPeerIP = confSyncPeerIP;
	}
	public String getVrrpPriority()
	{
		return vrrpPriority;
	}
	public void setVrrpPriority(String vrrpPriority)
	{
		this.vrrpPriority = vrrpPriority;
	}
	public String getVrrpShare()
	{
		return vrrpShare;
	}
	public void setVrrpShare(String vrrpShare)
	{
		this.vrrpShare = vrrpShare;
	}
	public String getVrrpTrackVrs()
	{
		return vrrpTrackVrs;
	}
	public void setVrrpTrackVrs(String vrrpTrackVrs)
	{
		this.vrrpTrackVrs = vrrpTrackVrs;
	}
	public String getVrrpTrackIfs()
	{
		return vrrpTrackIfs;
	}
	public void setVrrpTrackIfs(String vrrpTrackIfs)
	{
		this.vrrpTrackIfs = vrrpTrackIfs;
	}
	public String getVrrpTrackPorts()
	{
		return vrrpTrackPorts;
	}
	public void setVrrpTrackPorts(String vrrpTrackPorts)
	{
		this.vrrpTrackPorts = vrrpTrackPorts;
	}
	public String getVrrpTrackL4pts()
	{
		return vrrpTrackL4pts;
	}
	public void setVrrpTrackL4pts(String vrrpTrackL4pts)
	{
		this.vrrpTrackL4pts = vrrpTrackL4pts;
	}
	public String getVrrpTrackReals()
	{
		return vrrpTrackReals;
	}
	public void setVrrpTrackReals(String vrrpTrackReals)
	{
		this.vrrpTrackReals = vrrpTrackReals;
	}
	public String getVrrpTrackHsrp()
	{
		return vrrpTrackHsrp;
	}
	public void setVrrpTrackHsrp(String vrrpTrackHsrp)
	{
		this.vrrpTrackHsrp = vrrpTrackHsrp;
	}
	public String getVrrpTrackHsrv()
	{
		return vrrpTrackHsrv;
	}
	public void setVrrpTrackHsrv(String vrrpTrackHsrv)
	{
		this.vrrpTrackHsrv = vrrpTrackHsrv;
	}

}
