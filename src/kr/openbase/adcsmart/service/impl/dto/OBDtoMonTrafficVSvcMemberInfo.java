package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

public class OBDtoMonTrafficVSvcMemberInfo
{
//	public static final  int	TYPE_SERVER		 =1;
//	public static final  int	TYPE_SERVICE  	 =2;

	private Timestamp occurTime;
	private String  objIndex;
	
//	private Integer objType;
	private String  poolIndex; //virtual service index;
	private String  vsIndex;   // virtual server index;
	private String  poolMemberIndex;   // virtual server index;
	
	private Integer adcIndex;
	private String vsNameID;// id if alten, name if f5
	private Integer svcPort;
	
//	private String memberIndex	= "";
	private String memberNameID = "";
	private String memberIPAddress	="";
	private Integer memberPort =0;
	
	private Long curConns	=-1L;
	private Long maxConns	=-1L;
	private Long totConns	=-1L;
	private Long pktsIn		=-1L;
	private Long pktsOut	=-1L;
	private Long bytesIn	=-1L;
	private Long bytesOut	=-1L;
	@Override
	public String toString()
	{
		return "OBDtoMonTrafficVSvcMemberInfo [occurTime=" + occurTime + ", objIndex=" + objIndex + ", poolIndex=" + poolIndex + ", vsIndex=" + vsIndex + ", poolMemberIndex=" + poolMemberIndex + ", adcIndex=" + adcIndex + ", vsNameID=" + vsNameID + ", svcPort=" + svcPort + ", memberNameID=" + memberNameID + ", memberIPAddress=" + memberIPAddress + ", memberPort=" + memberPort + ", curConns=" + curConns + ", maxConns=" + maxConns + ", totConns=" + totConns + ", pktsIn=" + pktsIn + ", pktsOut=" + pktsOut + ", bytesIn=" + bytesIn + ", bytesOut=" + bytesOut + "]";
	}
	public Timestamp getOccurTime()
	{
		return occurTime;
	}
	public String getPoolIndex()
	{
		return poolIndex;
	}
	public void setPoolIndex(String poolIndex)
	{
		this.poolIndex = poolIndex;
	}
	public String getPoolMemberIndex()
	{
		return poolMemberIndex;
	}
	public void setPoolMemberIndex(String poolMemberIndex)
	{
		this.poolMemberIndex = poolMemberIndex;
	}
	public void setOccurTime(Timestamp occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getObjIndex()
	{
		return objIndex;
	}
	public void setObjIndex(String objIndex)
	{
		this.objIndex = objIndex;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getVsNameID()
	{
		return vsNameID;
	}
	public void setVsNameID(String vsNameID)
	{
		this.vsNameID = vsNameID;
	}
	public Integer getSvcPort()
	{
		return svcPort;
	}
	public void setSvcPort(Integer svcPort)
	{
		this.svcPort = svcPort;
	}
	public String getMemberNameID()
	{
		return memberNameID;
	}
	public void setMemberNameID(String memberNameID)
	{
		this.memberNameID = memberNameID;
	}
	public String getMemberIPAddress()
	{
		return memberIPAddress;
	}
	public void setMemberIPAddress(String memberIPAddress)
	{
		this.memberIPAddress = memberIPAddress;
	}
	public Integer getMemberPort()
	{
		return memberPort;
	}
	public void setMemberPort(Integer memberPort)
	{
		this.memberPort = memberPort;
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
	public Long getPktsIn()
	{
		return pktsIn;
	}
	public void setPktsIn(Long pktsIn)
	{
		this.pktsIn = pktsIn;
	}
	public Long getPktsOut()
	{
		return pktsOut;
	}
	public void setPktsOut(Long pktsOut)
	{
		this.pktsOut = pktsOut;
	}
	public Long getBytesIn()
	{
		return bytesIn;
	}
	public void setBytesIn(Long bytesIn)
	{
		this.bytesIn = bytesIn;
	}
	public Long getBytesOut()
	{
		return bytesOut;
	}
	public void setBytesOut(Long bytesOut)
	{
		this.bytesOut = bytesOut;
	}
}
