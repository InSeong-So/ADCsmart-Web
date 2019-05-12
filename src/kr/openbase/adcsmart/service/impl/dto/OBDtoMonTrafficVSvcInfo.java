package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

public class OBDtoMonTrafficVSvcInfo
{
	public static final  int	SLBLAYER_SERVER	 =1;
	public static final  int	SLBLAYER_SERVICE  	 =2;

	private Timestamp occurTime;
	private String  objIndex;
	
	private Integer objType;
	private String  vsvcIndex; //virtual service index;
	private String  vsIndex;   // virtual server index;
	
	private Integer adcIndex;
	private String vsNameID;// id if alten, name if f5
	private Integer svcPort;
	
	private Long curConns	=-1L;
	private Long maxConns	=-1L;
	private Long totConns	=-1L;
	private Long pktsIn		=-1L;
	private Long pktsOut	=-1L;
	private Long bytesIn	=-1L;
	private Long bytesOut	=-1L;
	private Long filterTot  =-1L;
	
	@Override
    public String toString()
    {
        return "OBDtoMonTrafficVSvcInfo [occurTime=" + occurTime
                + ", objIndex=" + objIndex + ", objType=" + objType
                + ", vsvcIndex=" + vsvcIndex + ", vsIndex=" + vsIndex
                + ", adcIndex=" + adcIndex + ", vsNameID=" + vsNameID
                + ", svcPort=" + svcPort + ", curConns=" + curConns
                + ", maxConns=" + maxConns + ", totConns=" + totConns
                + ", pktsIn=" + pktsIn + ", pktsOut=" + pktsOut + ", bytesIn="
                + bytesIn + ", bytesOut=" + bytesOut + ", filterTot="
                + filterTot + "]";
    }
	public Integer getObjType()
	{
		return objType;
	}
	public void setObjType(Integer objType)
	{
		this.objType = objType;
	}
	public Timestamp getOccurTime()
	{
		return occurTime;
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
	public String getVsvcIndex()
	{
		return vsvcIndex;
	}
	public void setVsvcIndex(String vsvcIndex)
	{
		this.vsvcIndex = vsvcIndex;
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
    public Long getFilterTot()
    {
        return filterTot;
    }
    public void setFilterTot(Long filterTot)
    {
        this.filterTot = filterTot;
    }
}
