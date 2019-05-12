package kr.openbase.adcsmart.service.snmp.alteon.dto;

public class DtoSnmpNetworkClassNetAlteon
{
	private String netId;
	private String netType;
	private String ipSingle;
	private String ipMask;
	private String ipFrom;
	private String ipTo;
	private String matchType;
	private String ncId; //소속 network class id

	@Override
	public String toString()
	{
		return "DtoSnmpNetworkClassNetAlteon [netId=" + netId + ", netType="
				+ netType + ", ipSingle=" + ipSingle + ", ipMask=" + ipMask
				+ ", ipFrom=" + ipFrom + ", ipTo=" + ipTo + ", matchType="
				+ matchType + ", ncId=" + ncId + "]";
	}
	public String getNetId()
	{
		return netId;
	}
	public void setNetId(String netId)
	{
		this.netId = netId;
	}
	public String getNetType()
	{
		return netType;
	}
	public void setNetType(String netType)
	{
		this.netType = netType;
	}
	public String getIpSingle()
	{
		return ipSingle;
	}
	public void setIpSingle(String ipSingle)
	{
		this.ipSingle = ipSingle;
	}
	public String getIpMask()
	{
		return ipMask;
	}
	public void setIpMask(String ipMask)
	{
		this.ipMask = ipMask;
	}
	public String getIpFrom()
	{
		return ipFrom;
	}
	public void setIpFrom(String ipFrom)
	{
		this.ipFrom = ipFrom;
	}
	public String getIpTo()
	{
		return ipTo;
	}
	public void setIpTo(String ipTo)
	{
		this.ipTo = ipTo;
	}
	public String getMatchType()
	{
		return matchType;
	}
	public void setMatchType(String matchType)
	{
		this.matchType = matchType;
	}
	public String getNcId()
	{
		return ncId;
	}
	public void setNcId(String ncId)
	{
		this.ncId = ncId;
	}
}
