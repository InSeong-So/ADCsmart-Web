package kr.openbase.adcsmart.service.dto.dashboard;

import kr.openbase.adcsmart.service.dto.OBDtoDataObj;

public class OBDtoDashboardTraffic
{
	private Integer vendor;
	private String index;
	private String nameIp;//virtual server면 ip 또는 ADC 이름.
	private Integer	port;
	private Integer status;//정상, 꺼짐, 단절.
	private OBDtoDataObj connection;
	private OBDtoDataObj throughput;
	private OBDtoDataObj bpsIn;
	private OBDtoDataObj bpsOut;
	private OBDtoDataObj bpsTotal;
	private String adcName;
	private String adcIP;    
	
	@Override
    public String toString()
    {
        return "OBDtoDashboardTraffic [vendor=" + vendor + ", index=" + index + ", nameIp=" + nameIp + ", port=" + port + ", status=" + status + ", connection=" + connection + ", throughput=" + throughput + ", bpsIn=" + bpsIn + ", bpsOut=" + bpsOut + ", bpsTotal=" + bpsTotal + ", adcName=" + adcName + ", adcIP=" + adcIP + "]";
    }	
	
    public OBDtoDataObj getBpsIn()
    {
        return bpsIn;
    }


    public void setBpsIn(OBDtoDataObj bpsIn)
    {
        this.bpsIn = bpsIn;
    }


    public OBDtoDataObj getBpsOut()
    {
        return bpsOut;
    }


    public void setBpsOut(OBDtoDataObj bpsOut)
    {
        this.bpsOut = bpsOut;
    }


    public OBDtoDataObj getBpsTotal()
    {
        return bpsTotal;
    }


    public void setBpsTotal(OBDtoDataObj bpsTotal)
    {
        this.bpsTotal = bpsTotal;
    }


    public void setAdcIP(String adcIP)
    {
        this.adcIP = adcIP;
    }


    public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public String getNameIp()
	{
		return nameIp;
	}
	public void setNameIp(String nameIp)
	{
		this.nameIp = nameIp;
	}
	public Integer getVendor()
	{
		return vendor;
	}
	public void setVendor(Integer vendor)
	{
		this.vendor = vendor;
	}
	public Integer getPort()
	{
		return port;
	}
	public void setPort(Integer port)
	{
		this.port = port;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public OBDtoDataObj getConnection()
	{
		return connection;
	}
	public void setConnection(OBDtoDataObj connection)
	{
		this.connection = connection;
	}
	public OBDtoDataObj getThroughput()
	{
		return throughput;
	}
	public void setThroughput(OBDtoDataObj throughput)
	{
		this.throughput = throughput;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getAdcIP()
	{
		return adcIP;
	}
	public void setAdcIp(String adcIP)
	{
		this.adcIP = adcIP;
	}
}
