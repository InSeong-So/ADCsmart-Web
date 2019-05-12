package kr.openbase.adcsmart.service.impl.pas.handler.dto;

public class OBDtoPortMonitorPAS
{
	private String		portName	="";
	private long		rxBps		=0L;
	private	long		rxPps		=0L;
	private	long		txBps		=0L;
	private	long		txPps		=0L;
	@Override
	public String toString()
	{
		return "OBDtoPortMonitorPAS [portName=" + portName + ", rxBps=" + rxBps
				+ ", rxPps=" + rxPps + ", txBps=" + txBps + ", txPps=" + txPps
				+ "]";
	}
	public String getPortName()
	{
		return portName;
	}
	public void setPortName(String portName)
	{
		this.portName = portName;
	}
	public long getRxBps()
	{
		return rxBps;
	}
	public void setRxBps(long rxBps)
	{
		this.rxBps = rxBps;
	}
	public long getRxPps()
	{
		return rxPps;
	}
	public void setRxPps(long rxPps)
	{
		this.rxPps = rxPps;
	}
	public long getTxBps()
	{
		return txBps;
	}
	public void setTxBps(long txBps)
	{
		this.txBps = txBps;
	}
	public long getTxPps()
	{
		return txPps;
	}
	public void setTxPps(long txPps)
	{
		this.txPps = txPps;
	}
}
