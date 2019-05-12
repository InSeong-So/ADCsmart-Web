package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;
import java.util.Date;

public class OBDtoTrafficMapVServiceMembers
{
	private String objIndex;// vsrv index
	
	private Date  occurTime;
	private String vsIndex;
	private String vsName;
	private String vsIPAddress;
	private Integer srvPort;// for only alteon
	private Long bpsIn;// Byte per second. 
	private Long bpsOut;//
	private Long bpsTot;
	private Long ppsIn;// packet per second. 
	private Long ppsOut;
	private Long ppsTot;
	private Long curConns;
	private Long maxConns;
	private Long totConns;	
	private Integer status;
	private ArrayList<OBDtoTrafficMapMember> memberList;
	
	@Override
	public String toString()
	{
		return "OBDtoTrafficMapVServiceMembers [objIndex=" + objIndex + ", occurTime=" + occurTime + ", vsIndex=" + vsIndex + ", vsName=" + vsName + ", vsIPAddress=" + vsIPAddress + ", srvPort=" + srvPort + ", bpsIn=" + bpsIn + ", bpsOut=" + bpsOut + ", bpsTot=" + bpsTot + ", ppsIn=" + ppsIn + ", ppsOut=" + ppsOut + ", ppsTot=" + ppsTot + ", curConns=" + curConns + ", maxConns=" + maxConns + ", totConns=" + totConns + ", status=" + status + ", memberList=" + memberList + "]";
	}
	public String getObjIndex()
	{
		return objIndex;
	}

	public void setObjIndex(String objIndex)
	{
		this.objIndex = objIndex;
	}
	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Date getOccurTime()
	{
		return occurTime;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}

	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}

	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
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
	public Integer getSrvPort()
	{
		return srvPort;
	}
	public void setSrvPort(Integer srvPort)
	{
		this.srvPort = srvPort;
	}
	public Long getBpsIn()
	{
		return bpsIn;
	}
	public void setBpsIn(Long bpsIn)
	{
		this.bpsIn = bpsIn;
	}
	public Long getBpsOut()
	{
		return bpsOut;
	}
	public void setBpsOut(Long bpsOut)
	{
		this.bpsOut = bpsOut;
	}
	public Long getBpsTot()
	{
		return bpsTot;
	}
	public void setBpsTot(Long bpsTot)
	{
		this.bpsTot = bpsTot;
	}
	public Long getPpsIn()
	{
		return ppsIn;
	}
	public void setPpsIn(Long ppsIn)
	{
		this.ppsIn = ppsIn;
	}
	public Long getPpsOut()
	{
		return ppsOut;
	}
	public void setPpsOut(Long ppsOut)
	{
		this.ppsOut = ppsOut;
	}
	public Long getPpsTot()
	{
		return ppsTot;
	}
	public void setPpsTot(Long ppsTot)
	{
		this.ppsTot = ppsTot;
	}
	public Long getCurConns()
	{
		return curConns;
	}
	public void setCurConns(Long curConns)
	{
		this.curConns = curConns;
	}
	public Long getMaxConns()
	{
		return maxConns;
	}
	public void setMaxConns(Long maxConns)
	{
		this.maxConns = maxConns;
	}
	public Long getTotConns()
	{
		return totConns;
	}
	public void setTotConns(Long totConns)
	{
		this.totConns = totConns;
	}
	public ArrayList<OBDtoTrafficMapMember> getMemberList()
	{
		return memberList;
	}
	public void setMemberList(ArrayList<OBDtoTrafficMapMember> memberList)
	{
		this.memberList = memberList;
	}
}
