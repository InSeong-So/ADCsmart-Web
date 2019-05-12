package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidInfoAdcPerformance
{
	private String clientSslTotNativeConns;
	private String clientSslTotCompactConns;
	private String httpStatGetReqs;
	private String clientCurConns;
	private String clientBytesIn;
	private String clientBytesOut;
	private String clientPktsIn;
	private String clientPktsOut;
	@Override
	public String toString()
	{
		return "DtoOidInfoAdcPerformance [clientSslTotNativeConns="
				+ clientSslTotNativeConns + ", clientSslTotCompactConns="
				+ clientSslTotCompactConns + ", httpStatGetReqs="
				+ httpStatGetReqs + ", clientCurConns=" + clientCurConns
				+ ", clientBytesIn=" + clientBytesIn + ", clientBytesOut="
				+ clientBytesOut + ", clientPktsIn=" + clientPktsIn
				+ ", clientPktsOut=" + clientPktsOut + "]";
	}
	public String getClientSslTotNativeConns()
	{
		return clientSslTotNativeConns;
	}
	public void setClientSslTotNativeConns(String clientSslTotNativeConns)
	{
		this.clientSslTotNativeConns = clientSslTotNativeConns;
	}
	public String getClientSslTotCompactConns()
	{
		return clientSslTotCompactConns;
	}
	public void setClientSslTotCompactConns(String clientSslTotCompactConns)
	{
		this.clientSslTotCompactConns = clientSslTotCompactConns;
	}
	public String getHttpStatGetReqs()
	{
		return httpStatGetReqs;
	}
	public void setHttpStatGetReqs(String httpStatGetReqs)
	{
		this.httpStatGetReqs = httpStatGetReqs;
	}
	public String getClientCurConns()
	{
		return clientCurConns;
	}
	public void setClientCurConns(String clientCurConns)
	{
		this.clientCurConns = clientCurConns;
	}
	public String getClientBytesIn()
	{
		return clientBytesIn;
	}
	public void setClientBytesIn(String clientBytesIn)
	{
		this.clientBytesIn = clientBytesIn;
	}
	public String getClientBytesOut()
	{
		return clientBytesOut;
	}
	public void setClientBytesOut(String clientBytesOut)
	{
		this.clientBytesOut = clientBytesOut;
	}
	public String getClientPktsIn()
	{
		return clientPktsIn;
	}
	public void setClientPktsIn(String clientPktsIn)
	{
		this.clientPktsIn = clientPktsIn;
	}
	public String getClientPktsOut()
	{
		return clientPktsOut;
	}
	public void setClientPktsOut(String clientPktsOut)
	{
		this.clientPktsOut = clientPktsOut;
	}
}
