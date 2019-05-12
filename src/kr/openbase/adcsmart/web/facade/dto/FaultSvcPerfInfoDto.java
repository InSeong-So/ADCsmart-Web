package kr.openbase.adcsmart.web.facade.dto;

import kr.openbase.adcsmart.service.dto.OBDtoADCObject;

public class FaultSvcPerfInfoDto
{
	private OBDtoADCObject  adcObj;
	private String  vsIndex;
	private String  vsvcIndex;// for alteon
	private Integer vsStatus;
	private	String 	vsIP;
	private String	vsPort;
	private String 	vsName;
	private String responseTime;
	private String bpsIn;
	private String bpsOut;
	private	String bpsTotal;
	private String ppsIn;
	private String ppsOut;
	private	String ppsTotal;
	private String connCurr;
	private String connMax;
	private String connTotal;
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
	public String getResponseTime()
	{
		return responseTime;
	}
	public void setResponseTime(String responseTime)
	{
		this.responseTime = responseTime;
	}
	public String getBpsIn()
	{
		return bpsIn;
	}
	public void setBpsIn(String bpsIn)
	{
		this.bpsIn = bpsIn;
	}
	public String getBpsOut()
	{
		return bpsOut;
	}
	public void setBpsOut(String bpsOut)
	{
		this.bpsOut = bpsOut;
	}
	public String getBpsTotal()
	{
		return bpsTotal;
	}
	public void setBpsTotal(String bpsTotal)
	{
		this.bpsTotal = bpsTotal;
	}
	public String getPpsIn()
	{
		return ppsIn;
	}
	public void setPpsIn(String ppsIn)
	{
		this.ppsIn = ppsIn;
	}
	public String getPpsOut()
	{
		return ppsOut;
	}
	public void setPpsOut(String ppsOut)
	{
		this.ppsOut = ppsOut;
	}
	public String getPpsTotal()
	{
		return ppsTotal;
	}
	public void setPpsTotal(String ppsTotal)
	{
		this.ppsTotal = ppsTotal;
	}
	public String getConnCurr()
	{
		return connCurr;
	}
	public void setConnCurr(String connCurr)
	{
		this.connCurr = connCurr;
	}
	public String getConnMax()
	{
		return connMax;
	}
	public void setConnMax(String connMax)
	{
		this.connMax = connMax;
	}
	public String getConnTotal()
	{
		return connTotal;
	}
	public void setConnTotal(String connTotal)
	{
		this.connTotal = connTotal;
	}
	
	public String getVsvcIndex()
	{
		return vsvcIndex;
	}
	public void setVsvcIndex(String vsvcIndex)
	{
		this.vsvcIndex = vsvcIndex;
	}
	@Override
	public String toString()
	{
		return "FaultSvcPerfInfoDto [adcObj=" + adcObj + ", vsIndex=" + vsIndex + ", vsvcIndex=" + vsvcIndex + ", vsStatus=" + vsStatus + ", vsIP=" + vsIP + ", vsPort=" + vsPort + ", vsName=" + vsName + ", responseTime=" + responseTime + ", bpsIn=" + bpsIn + ", bpsOut=" + bpsOut + ", bpsTotal=" + bpsTotal + ", ppsIn=" + ppsIn + ", ppsOut=" + ppsOut + ", ppsTotal=" + ppsTotal + ", connCurr=" + connCurr + ", connMax=" + connMax + ", connTotal=" + connTotal + "]";
	}	
}