package kr.openbase.adcsmart.service.dto.fault;

import java.util.ArrayList;
import java.util.Date;

public class OBDtoFaultCheckResult
{
	private 	Long		checkKey;
	private     Date   		startTime;
	private     Date   		endTime;
	private     Integer     elapseTime;// unit:sec
	private		String		vsvcName;
	private		String		vsvcIPAddress;
	private		Integer		vsvcPort;
	
	private		ArrayList<OBDtoFaultCheckResultElement> hwResults;
	private 	ArrayList<OBDtoFaultCheckResultElement> l23Results;
	private 	ArrayList<OBDtoFaultCheckResultElement> l47Results;
	private 	ArrayList<OBDtoFaultCheckResultElement> svcResults;
	
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultCheckResult [checkKey=%s, startTime=%s, endTime=%s, elapseTime=%s, vsvcName=%s, vsvcIPAddress=%s, vsvcPort=%d, hwResults=%s, l23Results=%s, l47Results=%s, svcResults=%s]", checkKey, startTime, endTime, elapseTime, vsvcName, vsvcIPAddress, vsvcPort, hwResults, l23Results, l47Results, svcResults);
	}
	public Long getCheckKey()
	{
		return checkKey;
	}
	public Date getStartTime()
	{
		return startTime;
	}
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}
	public Date getEndTime()
	{
		return endTime;
	}
	public void setEndTime(Date endTime)
	{
		this.endTime = endTime;
	}
	public Integer getElapseTime()
	{
		return elapseTime;
	}
	public void setElapseTime(Integer elapseTime)
	{
		this.elapseTime = elapseTime;
	}
	public void setCheckKey(Long checkKey)
	{
		this.checkKey = checkKey;
	}	
	public String getVsvcName()
	{
		return vsvcName;
	}
	public void setVsvcName(String vsvcName)
	{
		this.vsvcName = vsvcName;
	}
	public String getVsvcIPAddress()
	{
		return vsvcIPAddress;
	}
	public void setVsvcIPAddress(String vsvcIPAddress)
	{
		this.vsvcIPAddress = vsvcIPAddress;
	}
	public Integer getVsvcPort()
	{
		return vsvcPort;
	}
	public void setVsvcPort(Integer vsvcPort)
	{
		this.vsvcPort = vsvcPort;
	}
	public ArrayList<OBDtoFaultCheckResultElement> getHwResults()
	{
		return hwResults;
	}
	public void setHwResults(ArrayList<OBDtoFaultCheckResultElement> hwResults)
	{
		this.hwResults = hwResults;
	}
	public ArrayList<OBDtoFaultCheckResultElement> getL23Results()
	{
		return l23Results;
	}
	public void setL23Results(ArrayList<OBDtoFaultCheckResultElement> l23Results)
	{
		this.l23Results = l23Results;
	}
	public ArrayList<OBDtoFaultCheckResultElement> getL47Results()
	{
		return l47Results;
	}
	public void setL47Results(ArrayList<OBDtoFaultCheckResultElement> l47Results)
	{
		this.l47Results = l47Results;
	}
	public ArrayList<OBDtoFaultCheckResultElement> getSvcResults()
	{
		return svcResults;
	}
	public void setSvcResults(ArrayList<OBDtoFaultCheckResultElement> svcResults)
	{
		this.svcResults = svcResults;
	}
}
