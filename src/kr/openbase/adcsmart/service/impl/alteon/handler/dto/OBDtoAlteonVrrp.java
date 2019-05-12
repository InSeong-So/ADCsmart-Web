package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoAlteonVrrp
{
	private int vID;//virtual ID
	private int enableDisable;
	private int ipVer;
	private int vrID;//virtual router ID
	private int ifNum;// default: 100
	private int priority;// default: 100
	private String vip;
	private int share;// 1: share enable, 0: share disable. default: 1
	private int trackVrs;
	private int trackIfs;
	private int trackPorts;
	private int trackL4pts;
	private int trackReals;
	private int trackHsrp;
	private int trackHsrv;

	@Override
	public String toString() {
		return "OBDtoAlteonVrrp [vID=" + this.vID + ", enableDisable=" + this.enableDisable + ", ipVer="
				+ ipVer + ", vrID=" + this.vrID + ", ifNum=" + this.ifNum
				+ ", priority=" + this.priority + ", vip=" + this.vip + ", share=" + this.share + "]";
	}
	
	public void setTrackVrs(int trackVrs)
	{
		this.trackVrs=trackVrs;
	}
	public int getTrackVrs()
	{
		return this.trackVrs;
	}

	public void setTrackIfs(int trackIfs)
	{
		this.trackIfs=trackIfs;
	}
	public int getTrackIfs()
	{
		return this.trackIfs;
	}

	public void setTrackPorts(int trackPorts)
	{
		this.trackPorts=trackPorts;
	}
	public int getTrackPorts()
	{
		return this.trackPorts;
	}

	public void setTrackL4pts(int trackL4pts)
	{
		this.trackL4pts=trackL4pts;
	}
	public int getTrackL4pts()
	{
		return this.trackL4pts;
	}

	public void setTrackReals(int trackReals)
	{
		this.trackReals=trackReals;
	}
	public int getTrackReals()
	{
		return this.trackReals;
	}

	public void setTrackHsrp(int trackHsrp)
	{
		this.trackHsrp=trackHsrp;
	}
	public int getTrackHsrp()
	{
		return this.trackHsrp;
	}

	public void setTrackHsrv(int trackHsrv)
	{
		this.trackHsrv=trackHsrv;
	}
	public int getTrackHsrv()
	{
		return this.trackHsrv;
	}

	public void setShare(int share)
	{
		this.share=share;
	}
	public int getShare()
	{
		return this.share;
	}
	
	public void setVID(int vID)
	{
		this.vID=vID;
	}
	public int getVID()
	{
		return this.vID;
	}
	
	public void setEnableDisable(int enableDisable)
	{
		this.enableDisable=enableDisable;
	}
	public int getEnableDisable()
	{
		return this.enableDisable;
	}
	
	public void setIpVer(int ipVer)
	{
		this.ipVer=ipVer;
	}
	public int getIpVer()
	{
		return this.ipVer;
	}
	
	public void setVrID(int vrID)
	{
		this.vrID=vrID;
	}
	public int getVrID()
	{
		return this.vrID;
	}
	
	public void setIfNum(int ifNum)
	{
		this.ifNum=ifNum;
	}
	public int getIfNum()
	{
		return this.ifNum;
	}

	public void setPriority(int priority)
	{
		this.priority=priority;
	}
	public int getPriority()
	{
		return this.priority;
	}

	public void setVIP(String vip)
	{
		this.vip=vip;
	}
	public String getVIP()
	{
		return this.vip;
	}
}
