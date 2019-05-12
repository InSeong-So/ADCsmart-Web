package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoTrafficMapMember
{
	private String index;// vsvc에 할당된 멤버의 index 정보..
	private Date  occurTime;	
	private String ipAddress;
	private Integer port;
	private String addPort;//for alteon
	private Long bpsIn;// Byte per second. 
	private Long ppsIn;// packet per second.
	private Long bpsTot;
	private Long bpsOut;// 
	private Long ppsOut;
	private Long ppsTot;
	private Long curConns;
	private Long maxConns;
	private Long totConns;
	private Integer status;
	
	@Override
    public String toString()
    {
        return "OBDtoTrafficMapMember [index=" + index + ", occurTime="
                + occurTime + ", ipAddress=" + ipAddress + ", port=" + port
                + ", addPort=" + addPort + ", bpsIn=" + bpsIn + ", ppsIn="
                + ppsIn + ", bpsTot=" + bpsTot + ", bpsOut=" + bpsOut
                + ", ppsOut=" + ppsOut + ", ppsTot=" + ppsTot + ", curConns="
                + curConns + ", maxConns=" + maxConns + ", totConns="
                + totConns + ", status=" + status + "]";
    }
	
	public Date getOccurTime()
	{
		return occurTime;
	}
	public String getIndex()
	{
		return index;
	}

	public void setIndex(String index)
	{
		this.index = index;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Long getBpsTot()
	{
		return bpsTot;
	}

	public void setBpsTot(Long bpsTot)
	{
		this.bpsTot = bpsTot;
	}

	public Long getPpsTot()
	{
		return ppsTot;
	}

	public void setPpsTot(Long ppsTot)
	{
		this.ppsTot = ppsTot;
	}

	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public Integer getPort()
	{
		return port;
	}
	public void setPort(Integer port)
	{
		this.port = port;
	}
	public Long getBpsIn()
	{
		return bpsIn;
	}
	public void setBpsIn(Long bpsIn)
	{
		this.bpsIn = bpsIn;
	}
	public Long getPpsIn()
	{
		return ppsIn;
	}
	public void setPpsIn(Long ppsIn)
	{
		this.ppsIn = ppsIn;
	}
	public Long getBpsOut()
	{
		return bpsOut;
	}
	public void setBpsOut(Long bpsOut)
	{
		this.bpsOut = bpsOut;
	}
	public Long getPpsOut()
	{
		return ppsOut;
	}
	public void setPpsOut(Long ppsOut)
	{
		this.ppsOut = ppsOut;
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

    public String getAddPort()
    {
        return addPort;
    }

    public void setAddPort(String addPort)
    {
        this.addPort = addPort;
    }	
}
