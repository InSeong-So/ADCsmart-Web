package kr.openbase.adcsmart.service.dto.fault;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;

public class OBDtoFaultSvcPerfInfo
{
	private OBDtoADCObject  adcObj;
	private String  vsIndex;
	private String  vsvcIndex;// for alteon
	private Integer vsStatus;
	private	String 	vsIP;
	private String	vsPort;
	private String 	vsName;
	private Integer responseTime;
	private Long bpsIn;
	private Long bpsOut;
	private	Long bpsTotal;
	private Long ppsIn;
	private Long ppsOut;
	private	Long ppsTotal;
	private Long connCurr;
	private Long connMax;
	private Long connTotal;
	@Override
	public String toString()
	{
		return "OBDtoFaultSvcPerfInfo [adcObj=" + adcObj + ", vsIndex=" + vsIndex + ", vsvcIndex=" + vsvcIndex + ", vsStatus=" + vsStatus + ", vsIP=" + vsIP + ", vsPort=" + vsPort + ", vsName=" + vsName + ", responseTime=" + responseTime + ", bpsIn=" + bpsIn + ", bpsOut=" + bpsOut + ", bpsTotal=" + bpsTotal + ", ppsIn=" + ppsIn + ", ppsOut=" + ppsOut + ", ppsTotal=" + ppsTotal + ", connCurr=" + connCurr + ", connMax=" + connMax + ", connTotal=" + connTotal + "]";
	}
	public String getVsvcIndex()
	{
		return vsvcIndex;
	}
	public void setVsvcIndex(String vsvcIndex)
	{
		this.vsvcIndex = vsvcIndex;
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
	public Long getPpsTotal()
	{
		return ppsTotal;
	}
	public void setPpsTotal(Long ppsTotal)
	{
		this.ppsTotal = ppsTotal;
	}
	public OBDtoADCObject getAdcObj()
	{
		return adcObj;
	}
	public void setAdcObj(OBDtoADCObject adcObj)
	{
		this.adcObj = adcObj;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}
	public Integer getVsStatus()
	{
		return vsStatus;
	}
	public void setVsStatus(Integer vsStatus)
	{
		this.vsStatus = vsStatus;
	}
	public String getVsIP()
	{
		return vsIP;
	}
	public void setVsIP(String vsIP)
	{
		this.vsIP = vsIP;
	}
	public String getVsPort()
	{
		return vsPort;
	}
	public void setVsPort(String vsPort)
	{
		this.vsPort = vsPort;
	}
	public String getVsName()
	{
		return vsName;
	}
	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
	public Integer getResponseTime()
	{
		return responseTime;
	}
	public void setResponseTime(Integer responseTime)
	{
		this.responseTime = responseTime;
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
	public Long getBpsTotal()
	{
		return bpsTotal;
	}
	public void setBpsTotal(Long bpsTotal)
	{
		this.bpsTotal = bpsTotal;
	}
	public Long getConnCurr()
	{
		return connCurr;
	}
	public void setConnCurr(Long connCurr)
	{
		this.connCurr = connCurr;
	}
	public Long getConnMax()
	{
		return connMax;
	}
	public void setConnMax(Long connMax)
	{
		this.connMax = connMax;
	}
	public Long getConnTotal()
	{
		return connTotal;
	}
	public void setConnTotal(Long connTotal)
	{
		this.connTotal = connTotal;
	}
}
