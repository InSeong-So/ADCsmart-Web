package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidCfgPorts
{
	private String portName;
	private String portDuplex;
	private String portSpeed;
	private String portStatus;
	private String portPktsIn;
	private String portPktsOut;
	private String portPktsMultiIn;
	private String portPktsMultiOut;
	private String portPktsBroadIn;
	private String portPktsBroadOut;
	private String portPktsUnknownProtos;

	private String portPktsDiscardsIn;
	private String portPktsDiscardsOut;
	private String portPktsErrorsIn;
	private String portPktsErrorsOut;
	private String portBytesIn;
	private String portBytesOut;
	private String portAliasName;
	private String portConnType;
	private String portMgmtState; // management port enable/disable
	private String portMgmtStatus; // management port up/down/disable
	private String portMgmtSpeed; // management port speed
	private String portMgmtBytesIn; // management port BytesIn
	private String portMgmtBytesOut; // management port BytesOut
	private String portMgmtPacketsIn; // management port BytesIn
	private String portMgmtPacketsOut; // management port BytesOut
	private String portMgmtErrorsIn; // management port ErrorsIn
	private String portMgmtErrorsOut; // management port ErrorsOut
	
	//v26에서만 쓰는 보정용 oid
	private String portBytesInPerSec;
	private String portBytesOutPerSec;
	
	@Override
	public String toString()
	{
		return "DtoOidCfgPorts [portName=" + portName + ", portDuplex="
				+ portDuplex + ", portSpeed=" + portSpeed + ", portStatus="
				+ portStatus + ", portPktsIn=" + portPktsIn + ", portPktsOut="
				+ portPktsOut + ", portPktsMultiIn=" + portPktsMultiIn
				+ ", portPktsMultiOut=" + portPktsMultiOut
				+ ", portPktsBroadIn=" + portPktsBroadIn
				+ ", portPktsBroadOut=" + portPktsBroadOut
				+ ", portPktsUnknownProtos=" + portPktsUnknownProtos
				+ ", portPktsDiscardsIn=" + portPktsDiscardsIn
				+ ", portPktsDiscardsOut=" + portPktsDiscardsOut
				+ ", portPktsErrorsIn=" + portPktsErrorsIn
				+ ", portPktsErrorsOut=" + portPktsErrorsOut + ", portBytesIn="
				+ portBytesIn + ", portBytesOut=" + portBytesOut
				+ ", portAliasName=" + portAliasName + ", portConnType="
				+ portConnType + ", portMgmtState=" + portMgmtState
				+ ", portMgmtStatus=" + portMgmtStatus + ", portMgmtSpeed="
				+ portMgmtSpeed + ", portMgmtBytesIn=" + portMgmtBytesIn
				+ ", portMgmtBytesOut=" + portMgmtBytesOut
				+ ", portMgmtPacketsIn=" + portMgmtPacketsIn
				+ ", portMgmtPacketsOut=" + portMgmtPacketsOut
				+ ", portMgmtErrorsIn=" + portMgmtErrorsIn
				+ ", portMgmtErrorsOut=" + portMgmtErrorsOut
				+ ", portBytesInPerSec=" + portBytesInPerSec
				+ ", portBytesOutPerSec=" + portBytesOutPerSec + "]";
	}
	public String getPortMgmtState()
	{
		return portMgmtState;
	}
	public void setPortMgmtState(String portMgmtState)
	{
		this.portMgmtState = portMgmtState;
	}
	public String getPortMgmtStatus()
	{
		return portMgmtStatus;
	}
	public void setPortMgmtStatus(String portMgmtStatus)
	{
		this.portMgmtStatus = portMgmtStatus;
	}
	public String getPortConnType()
	{
		return portConnType;
	}
	public void setPortConnType(String portConnType)
	{
		this.portConnType = portConnType;
	}
	public String getPortPktsMultiIn()
	{
		return portPktsMultiIn;
	}
	public void setPortPktsMultiIn(String portPktsMultiIn)
	{
		this.portPktsMultiIn = portPktsMultiIn;
	}
	public String getPortPktsMultiOut()
	{
		return portPktsMultiOut;
	}
	public void setPortPktsMultiOut(String portPktsMultiOut)
	{
		this.portPktsMultiOut = portPktsMultiOut;
	}
	public String getPortPktsBroadIn()
	{
		return portPktsBroadIn;
	}
	public void setPortPktsBroadIn(String portPktsBroadIn)
	{
		this.portPktsBroadIn = portPktsBroadIn;
	}
	public String getPortPktsBroadOut()
	{
		return portPktsBroadOut;
	}
	public void setPortPktsBroadOut(String portPktsBroadOut)
	{
		this.portPktsBroadOut = portPktsBroadOut;
	}
	public String getPortPktsUnknownProtos()
	{
		return portPktsUnknownProtos;
	}
	public void setPortPktsUnknownProtos(String portPktsUnknownProtos)
	{
		this.portPktsUnknownProtos = portPktsUnknownProtos;
	}
	public String getPortAliasName()
	{
		return portAliasName;
	}
	public void setPortAliasName(String portAliasName)
	{
		this.portAliasName = portAliasName;
	}
	public String getPortName()
	{
		return portName;
	}
	public String getPortBytesIn()
	{
		return portBytesIn;
	}
	public void setPortBytesIn(String portBytesIn)
	{
		this.portBytesIn = portBytesIn;
	}
	public String getPortBytesOut()
	{
		return portBytesOut;
	}
	public void setPortBytesOut(String portBytesOut)
	{
		this.portBytesOut = portBytesOut;
	}
	public void setPortName(String portName)
	{
		this.portName = portName;
	}
	public String getPortDuplex()
	{
		return portDuplex;
	}
	public void setPortDuplex(String portDuplex)
	{
		this.portDuplex = portDuplex;
	}
	public String getPortSpeed()
	{
		return portSpeed;
	}
	public void setPortSpeed(String portSpeed)
	{
		this.portSpeed = portSpeed;
	}
	public String getPortStatus()
	{
		return portStatus;
	}
	public void setPortStatus(String portStatus)
	{
		this.portStatus = portStatus;
	}
	public String getPortPktsIn()
	{
		return portPktsIn;
	}
	public void setPortPktsIn(String portPktsIn)
	{
		this.portPktsIn = portPktsIn;
	}
	public String getPortPktsOut()
	{
		return portPktsOut;
	}
	public void setPortPktsOut(String portPktsOut)
	{
		this.portPktsOut = portPktsOut;
	}
	public String getPortPktsDiscardsIn()
	{
		return portPktsDiscardsIn;
	}
	public void setPortPktsDiscardsIn(String portPktsDiscardsIn)
	{
		this.portPktsDiscardsIn = portPktsDiscardsIn;
	}
	public String getPortPktsDiscardsOut()
	{
		return portPktsDiscardsOut;
	}
	public void setPortPktsDiscardsOut(String portPktsDiscardsOut)
	{
		this.portPktsDiscardsOut = portPktsDiscardsOut;
	}
	public String getPortPktsErrorsIn()
	{
		return portPktsErrorsIn;
	}
	public void setPortPktsErrorsIn(String portPktsErrorsIn)
	{
		this.portPktsErrorsIn = portPktsErrorsIn;
	}
	public String getPortPktsErrorsOut()
	{
		return portPktsErrorsOut;
	}
	public void setPortPktsErrorsOut(String portPktsErrorsOut)
	{
		this.portPktsErrorsOut = portPktsErrorsOut;
	}
	public String getPortMgmtSpeed()
	{
		return portMgmtSpeed;
	}
	public void setPortMgmtSpeed(String portMgmtSpeed)
	{
		this.portMgmtSpeed = portMgmtSpeed;
	}
	public String getPortMgmtBytesIn()
	{
		return portMgmtBytesIn;
	}
	public void setPortMgmtBytesIn(String portMgmtBytesIn)
	{
		this.portMgmtBytesIn = portMgmtBytesIn;
	}
	public String getPortMgmtBytesOut()
	{
		return portMgmtBytesOut;
	}
	public void setPortMgmtBytesOut(String portMgmtBytesOut)
	{
		this.portMgmtBytesOut = portMgmtBytesOut;
	}
	public String getPortMgmtErrorsIn()
	{
		return portMgmtErrorsIn;
	}
	public void setPortMgmtErrorsIn(String portMgmtErrorsIn)
	{
		this.portMgmtErrorsIn = portMgmtErrorsIn;
	}
	public String getPortMgmtErrorsOut()
	{
		return portMgmtErrorsOut;
	}
	public void setPortMgmtErrorsOut(String portMgmtErrorsOut)
	{
		this.portMgmtErrorsOut = portMgmtErrorsOut;
	}
	public String getPortMgmtPacketsIn()
	{
		return portMgmtPacketsIn;
	}
	public void setPortMgmtPacketsIn(String portMgmtPacketsIn)
	{
		this.portMgmtPacketsIn = portMgmtPacketsIn;
	}
	public String getPortMgmtPacketsOut()
	{
		return portMgmtPacketsOut;
	}
	public void setPortMgmtPacketsOut(String portMgmtPacketsOut)
	{
		this.portMgmtPacketsOut = portMgmtPacketsOut;
	}
	public String getPortBytesInPerSec()
	{
		return portBytesInPerSec;
	}
	public void setPortBytesInPerSec(String portBytesInPerSec)
	{
		this.portBytesInPerSec = portBytesInPerSec;
	}
	public String getPortBytesOutPerSec()
	{
		return portBytesOutPerSec;
	}
	public void setPortBytesOutPerSec(String portBytesOutPerSec)
	{
		this.portBytesOutPerSec = portBytesOutPerSec;
	}
}
