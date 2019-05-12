package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

public class OBDtoMonL2Ports
{
	public static final int PHY_CONN_TYPE_COPPER 	= 1;// 
	public static final int PHY_CONN_TYPE_SFP 		= 2;// 
	
	public static final int STATUS_UP 				= 1;// 
	public static final int STATUS_DOWN 			= 2;// 
	public static final int STATUS_DISABLED 		= 3;// 
	public static final int STATUS_INOPERATIVE 		= 4;// 
	
	private Timestamp	time;
	private int			portType;						//일반포트와 MGMT 구분
	private int 		portIndex;
	private String 		portName;
	private int 		duplex;
	private int 		speed;
	private int 		status;
	private long		pktsIn;
	private long 		pktsOut;
	private long		pktsMultiIn;
	private long 		pktsMultiOut;
	private long		pktsBroadIn;
	private long 		pktsBroadOut;
	private long 		pktsUnknownProts;
	private long		bytesIn;
	private long 		bytesOut;
	private long 		errorsIn;
	private long 		errorsOut;
	private long 		dropsIn;
	private long 		dropsOut;
	private String		aliasName;
	//아래 2건, Alteon v26에서만 보정용으로 구하는 데이터
	private long		bytesInPerSec;
	private long 		bytesOutPerSec;
	
	private int     connType    =PHY_CONN_TYPE_COPPER;// connector type. 
	
	@Override
	public String toString()
	{
		return "OBDtoMonL2Ports [time=" + time + ", portType=" + portType
				+ ", portIndex=" + portIndex + ", portName=" + portName
				+ ", duplex=" + duplex + ", speed=" + speed + ", status="
				+ status + ", pktsIn=" + pktsIn + ", pktsOut=" + pktsOut
				+ ", pktsMultiIn=" + pktsMultiIn + ", pktsMultiOut="
				+ pktsMultiOut + ", pktsBroadIn=" + pktsBroadIn
				+ ", pktsBroadOut=" + pktsBroadOut + ", pktsUnknownProts="
				+ pktsUnknownProts + ", bytesIn=" + bytesIn + ", bytesOut="
				+ bytesOut + ", errorsIn=" + errorsIn + ", errorsOut="
				+ errorsOut + ", dropsIn=" + dropsIn + ", dropsOut=" + dropsOut
				+ ", aliasName=" + aliasName + ", bytesInPerSec="
				+ bytesInPerSec + ", bytesOutPerSec=" + bytesOutPerSec
				+ ", connType=" + connType + "]";
	}
	public int getPortIndex()
	{
		return portIndex;
	}
	public void setPortIndex(int portIndex)
	{
		this.portIndex = portIndex;
	}
	public int getConnType()
	{
		return connType;
	}
	public void setConnType(int connType)
	{
		this.connType = connType;
	}
	public long getPktsMultiIn()
	{
		return pktsMultiIn;
	}
	public void setPktsMultiIn(long pktsMultiIn)
	{
		this.pktsMultiIn = pktsMultiIn;
	}
	public long getPktsMultiOut()
	{
		return pktsMultiOut;
	}
	public void setPktsMultiOut(long pktsMultiOut)
	{
		this.pktsMultiOut = pktsMultiOut;
	}
	public long getPktsBroadIn()
	{
		return pktsBroadIn;
	}
	public void setPktsBroadIn(long pktsBroadIn)
	{
		this.pktsBroadIn = pktsBroadIn;
	}
	public long getPktsBroadOut()
	{
		return pktsBroadOut;
	}
	public void setPktsBroadOut(long pktsBroadOut)
	{
		this.pktsBroadOut = pktsBroadOut;
	}
	public long getPktsUnknownProts()
	{
		return pktsUnknownProts;
	}
	public void setPktsUnknownProts(long pktsUnknownProts)
	{
		this.pktsUnknownProts = pktsUnknownProts;
	}
	public String getAliasName()
	{
		return aliasName;
	}
	public void setAliasName(String aliasName)
	{
		this.aliasName = aliasName;
	}
	public Timestamp getTime()
	{
		return time;
	}
	public void setTime(Timestamp time)
	{
		this.time = time;
	}
	public String getPortName()
	{
		return portName;
	}
	public void setPortName(String portName)
	{
		this.portName = portName;
	}
	public int getDuplex()
	{
		return duplex;
	}
	public void setDuplex(int duplex)
	{
		this.duplex = duplex;
	}
	public int getSpeed()
	{
		return speed;
	}
	public void setSpeed(int speed)
	{
		this.speed = speed;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public long getPktsIn()
	{
		return pktsIn;
	}
	public void setPktsIn(long pktsIn)
	{
		this.pktsIn = pktsIn;
	}
	public long getPktsOut()
	{
		return pktsOut;
	}
	public void setPktsOut(long pktsOut)
	{
		this.pktsOut = pktsOut;
	}
	public long getBytesIn()
	{
		return bytesIn;
	}
	public void setBytesIn(long bytesIn)
	{
		this.bytesIn = bytesIn;
	}
	public long getBytesOut()
	{
		return bytesOut;
	}
	public void setBytesOut(long bytesOut)
	{
		this.bytesOut = bytesOut;
	}
	public long getErrorsIn()
	{
		return errorsIn;
	}
	public void setErrorsIn(long errorsIn)
	{
		this.errorsIn = errorsIn;
	}
	public long getErrorsOut()
	{
		return errorsOut;
	}
	public void setErrorsOut(long errorsOut)
	{
		this.errorsOut = errorsOut;
	}
	public long getDropsIn()
	{
		return dropsIn;
	}
	public void setDropsIn(long dropsIn)
	{
		this.dropsIn = dropsIn;
	}
	public long getDropsOut()
	{
		return dropsOut;
	}
	public void setDropsOut(long dropsOut)
	{
		this.dropsOut = dropsOut;
	}
	public int getPortType()
	{
		return portType;
	}
	public void setPortType(int portType)
	{
		this.portType = portType;
	}
	public long getBytesInPerSec()
	{
		return bytesInPerSec;
	}
	public void setBytesInPerSec(long bytesInPerSec)
	{
		this.bytesInPerSec = bytesInPerSec;
	}
	public long getBytesOutPerSec()
	{
		return bytesOutPerSec;
	}
	public void setBytesOutPerSec(long bytesOutPerSec)
	{
		this.bytesOutPerSec = bytesOutPerSec;
	}
}
