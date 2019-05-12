package kr.openbase.adcsmart.service.snmp.dto;

import java.sql.Timestamp;

public class OBDtoAdcPerformanceData
{
	private Timestamp occurTime;
	private long clientSslTotNativeConns;
	private long clientSslTotCompactConns;
	private long httpStatGetReqs;
	private long clientCurConns;
	private long clientBytesIn;
	private long clientBytesOut;
	private long clientPktsIn;
	private long clientPktsOut;
	@Override
	public String toString()
	{
		return "OBDtoAdcPerformanceData [occurTime=" + occurTime
				+ ", clientSslTotNativeConns=" + clientSslTotNativeConns
				+ ", clientSslTotCompactConns=" + clientSslTotCompactConns
				+ ", httpStatGetReqs=" + httpStatGetReqs + ", clientCurConns="
				+ clientCurConns + ", clientBytesIn=" + clientBytesIn
				+ ", clientBytesOut=" + clientBytesOut + ", clientPktsIn="
				+ clientPktsIn + ", clientPktsOut=" + clientPktsOut + "]";
	}
	public long getClientSslTotNativeConns()
	{
		return clientSslTotNativeConns;
	}
	public Timestamp getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Timestamp occurTime)
	{
		this.occurTime = occurTime;
	}
	public void setClientSslTotNativeConns(long clientSslTotNativeConns)
	{
		this.clientSslTotNativeConns = clientSslTotNativeConns;
	}
	public long getClientSslTotCompactConns()
	{
		return clientSslTotCompactConns;
	}
	public void setClientSslTotCompactConns(long clientSslTotCompactConns)
	{
		this.clientSslTotCompactConns = clientSslTotCompactConns;
	}
	public long getHttpStatGetReqs()
	{
		return httpStatGetReqs;
	}
	public void setHttpStatGetReqs(long httpStatGetReqs)
	{
		this.httpStatGetReqs = httpStatGetReqs;
	}
	public long getClientCurConns()
	{
		return clientCurConns;
	}
	public void setClientCurConns(long clientCurConns)
	{
		this.clientCurConns = clientCurConns;
	}
	public long getClientBytesIn()
	{
		return clientBytesIn;
	}
	public void setClientBytesIn(long clientBytesIn)
	{
		this.clientBytesIn = clientBytesIn;
	}
	public long getClientBytesOut()
	{
		return clientBytesOut;
	}
	public void setClientBytesOut(long clientBytesOut)
	{
		this.clientBytesOut = clientBytesOut;
	}
	public long getClientPktsIn()
	{
		return clientPktsIn;
	}
	public void setClientPktsIn(long clientPktsIn)
	{
		this.clientPktsIn = clientPktsIn;
	}
	public long getClientPktsOut()
	{
		return clientPktsOut;
	}
	public void setClientPktsOut(long clientPktsOut)
	{
		this.clientPktsOut = clientPktsOut;
	}
}
