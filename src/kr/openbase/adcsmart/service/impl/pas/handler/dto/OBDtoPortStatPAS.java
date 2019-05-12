package kr.openbase.adcsmart.service.impl.pas.handler.dto;

public class OBDtoPortStatPAS
{
	private	long	rxBytes		=0L;
	private	long	rxPkts		=0L;
	private	long	rxDiscards	=0L;
	private	long	txBytes		=0L;
	private	long	txPkts		=0L;
	private	long	txDiscards	=0L;
	private long	collisions	=0L;
	private long	undersize	=0L;
	private	long	oversize	=0L;
	private	long	fragments	=0L;
	private	long	crcErrors	=0L;

	@Override
	public String toString()
	{
		return "OBDtoPortStatPAS [rxBytes=" + rxBytes + ", rxPkts=" + rxPkts
				+ ", rxDiscards=" + rxDiscards + ", txBytes=" + txBytes
				+ ", txPkts=" + txPkts + ", txDiscards=" + txDiscards
				+ ", collisions=" + collisions + ", undersize=" + undersize
				+ ", oversize=" + oversize + ", fragments=" + fragments
				+ ", crcErrors=" + crcErrors + "]";
	}
	public long getCrcErrors()
	{
		return crcErrors;
	}
	public void setCrcErrors(long crcErrors)
	{
		this.crcErrors = crcErrors;
	}	public long getRxBytes()
	{
		return rxBytes;
	}
	public void setRxBytes(long rxBytes)
	{
		this.rxBytes = rxBytes;
	}
	public long getRxPkts()
	{
		return rxPkts;
	}
	public void setRxPkts(long rxPkts)
	{
		this.rxPkts = rxPkts;
	}
	public long getRxDiscards()
	{
		return rxDiscards;
	}
	public void setRxDiscards(long rxDiscards)
	{
		this.rxDiscards = rxDiscards;
	}
	public long getTxBytes()
	{
		return txBytes;
	}
	public void setTxBytes(long txBytes)
	{
		this.txBytes = txBytes;
	}
	public long getTxPkts()
	{
		return txPkts;
	}
	public void setTxPkts(long txPkts)
	{
		this.txPkts = txPkts;
	}
	public long getTxDiscards()
	{
		return txDiscards;
	}
	public void setTxDiscards(long txDiscards)
	{
		this.txDiscards = txDiscards;
	}
	public long getCollisions()
	{
		return collisions;
	}
	public void setCollisions(long collisions)
	{
		this.collisions = collisions;
	}
	public long getUndersize()
	{
		return undersize;
	}
	public void setUndersize(long undersize)
	{
		this.undersize = undersize;
	}
	public long getOversize()
	{
		return oversize;
	}
	public void setOversize(long oversize)
	{
		this.oversize = oversize;
	}
	public long getFragments()
	{
		return fragments;
	}
	public void setFragments(long fragments)
	{
		this.fragments = fragments;
	}
}
