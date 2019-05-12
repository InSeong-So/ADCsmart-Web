package kr.openbase.adcsmart.service.dto.dashboard;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoDataObj;

public class OBDtoDashboardVSConnHistory
{
	private String adcName;
	private String adcIPAddress;
	private String vsIPAddress;
	private Integer port;
	private ArrayList<OBDtoDataObj> connection;

	@Override
	public String toString()
	{
		return "OBDtoDashboardVSConnHistory [adcName=" + adcName
				+ ", adcIPAddress=" + adcIPAddress + ", vsIPAddress="
				+ vsIPAddress + ", port=" + port + ", connection=" + connection
				+ "]";
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getAdcIPAddress()
	{
		return adcIPAddress;
	}
	public void setAdcIPAddress(String adcIPAddress)
	{
		this.adcIPAddress = adcIPAddress;
	}
	public Integer getPort()
	{
		return port;
	}
	public void setPort(Integer port)
	{
		this.port = port;
	}
	public String getVsIPAddress()
	{
		return vsIPAddress;
	}
	public void setVsIPAddress(String vsIPAddress)
	{
		this.vsIPAddress = vsIPAddress;
	}
	public ArrayList<OBDtoDataObj> getConnection()
	{
		return connection;
	}
	public void setConnection(ArrayList<OBDtoDataObj> connection)
	{
		this.connection = connection;
	}
}
