package kr.openbase.adcsmart.service.impl.pask.handler.dto;

public class OBDtoPortStatPASK
{
	private String  portName;
	private	long	rxBytes		=0L;
	private	long	rxPkts		=0L;
	private	long	rxErrros	=0L;
	private	long	rxDiscards	=0L;
	private	long	txBytes		=0L;
	private	long	txPkts		=0L;
	private	long	txErrros	=0L;
	private	long	txDiscards	=0L;
	@Override
	public String toString()
	{
		return "OBDtoPortStatPASK [portName=" + portName + ", rxBytes=" + rxBytes + ", rxPkts=" + rxPkts + ", rxErrros=" + rxErrros + ", rxDiscards=" + rxDiscards + ", txBytes=" + txBytes + ", txPkts=" + txPkts + ", txErrros=" + txErrros + ", txDiscards=" + txDiscards + "]";
	}
	public String getPortName()
	{
		return portName;
	}
	public void setPortName(String portName)
	{
		this.portName = portName;
	}
	public long getRxBytes()
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
	public long getRxErrros()
	{
		return rxErrros;
	}
	public void setRxErrros(long rxErrros)
	{
		this.rxErrros = rxErrros;
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
	public long getTxErrros()
	{
		return txErrros;
	}
	public void setTxErrros(long txErrros)
	{
		this.txErrros = txErrros;
	}
	public long getTxDiscards()
	{
		return txDiscards;
	}
	public void setTxDiscards(long txDiscards)
	{
		this.txDiscards = txDiscards;
	}
}
