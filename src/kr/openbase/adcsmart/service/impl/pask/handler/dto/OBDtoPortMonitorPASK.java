package kr.openbase.adcsmart.service.impl.pask.handler.dto;

public class OBDtoPortMonitorPASK
{
	private String		portName;
	private long		rxBps;
	private	long		rxPps;
	private	long		txBps;
	private	long		txPps;
	@Override
	public String toString()
	{
		return "OBDtoPortMonitorPASK [portName=" + portName + ", rxBps=" + rxBps + ", rxPps=" + rxPps + ", txBps=" + txBps + ", txPps=" + txPps + "]";
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
