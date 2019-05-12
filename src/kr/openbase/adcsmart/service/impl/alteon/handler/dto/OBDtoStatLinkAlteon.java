package kr.openbase.adcsmart.service.impl.alteon.handler.dto;

public class OBDtoStatLinkAlteon
{
	private	String	linkName;
	private	long	bytes;
	private long	pkts;
	private	long	errors;
	private	long	discards;
	@Override
	public String toString()
	{
		return "OBDtoStatLinkAlteon [linkName=" + linkName + ", bytes=" + bytes + ", pkts=" + pkts + ", errors=" + errors + ", discards=" + discards + "]";
	}
	public String getLinkName()
	{
		return linkName;
	}
	public void setLinkName(String linkName)
	{
		this.linkName = linkName;
	}
	public long getBytes()
	{
		return bytes;
	}
	public void setBytes(long bytes)
	{
		this.bytes = bytes;
	}
	public long getPkts()
	{
		return pkts;
	}
	public void setPkts(long pkts)
	{
		this.pkts = pkts;
	}
	public long getErrors()
	{
		return errors;
	}
	public void setErrors(long errors)
	{
		this.errors = errors;
	}
	public long getDiscards()
	{
		return discards;
	}
	public void setDiscards(long discards)
	{
		this.discards = discards;
	}
}
