package kr.openbase.adcsmart.service.impl.fault.dto;

import java.sql.Timestamp;

public class OBDtoFaultCheckPacketLossInfo
{
	public static final int DIR_CLNT_VIP 	= (int)1;
	public static final int DIR_CLNT_REAL 	= (int)2;
	public static final int DIR_VIP_CLNT 	= (int)3;
	public static final int DIR_REAL_CLNT 	= (int)6;
	public static final int DIR_VIP_REAL 	= (int)4;
	public static final int DIR_REAL_VIP 	= (int)5;
	public static final int DIR_NA 			= (int)0;
	
	public static final int TYPE_NORMAL			= (int)1;
	public static final int TPYE_ABNORMAL 		= (int)2;
	
	private long 		seqNo;
	private long 		ackNo;
	private int    		dataLength;
	private Timestamp   rcvTime;
	private boolean     isValid=true;
	private Integer 	tcpFlag;//1:normal, 2:abnormal
	private Integer 	direction;//--1: client->vip, 2:client->real, 3:vip->client, 4:vip->real, 5: real->vip, 6: real->client
	private String  	srcIPAddress;
	private String  	dstIPAddress;
	private Integer		srcPort;
	private Integer 	dstPort;
	private Integer 	timeDiff=0;// msec단위.
	private String  	summary;
	
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultCheckPacketLossInfo [seqNo=%s, ackNo=%s, dataLength=%s, rcvTime=%s, isValid=%s, tcpFlag=%s, direction=%s, srcIPAddress=%s, dstIPAddress=%s, srcPort=%s, dstPort=%s, timeDiff=%s, summary=%s]", seqNo, ackNo, dataLength, rcvTime, isValid, tcpFlag, direction, srcIPAddress, dstIPAddress, srcPort, dstPort, timeDiff, summary);
	}
	public Integer getSrcPort()
	{
		return srcPort;
	}
	public void setSrcPort(Integer srcPort)
	{
		this.srcPort = srcPort;
	}
	public Integer getDstPort()
	{
		return dstPort;
	}
	public void setDstPort(Integer dstPort)
	{
		this.dstPort = dstPort;
	}
	public boolean isValid()
	{
		return isValid;
	}
	public void setValid(boolean isValid)
	{
		this.isValid = isValid;
	}
	public long getSeqNo()
	{
		return seqNo;
	}
	public void setSeqNo(long seqNo)
	{
		this.seqNo = seqNo;
	}
	public long getAckNo()
	{
		return ackNo;
	}
	public void setAckNo(long ackNo)
	{
		this.ackNo = ackNo;
	}
	public int getDataLength()
	{
		return dataLength;
	}
	public void setDataLength(int dataLength)
	{
		this.dataLength = dataLength;
	}
	public Timestamp getRcvTime()
	{
		return rcvTime;
	}
	public void setRcvTime(Timestamp rcvTime)
	{
		this.rcvTime = rcvTime;
	}
	public Integer getTcpFlag()
	{
		return tcpFlag;
	}
	public void setTcpFlag(Integer tcpFlag)
	{
		this.tcpFlag = tcpFlag;
	}
	public Integer getDirection()
	{
		return direction;
	}
	public void setDirection(Integer direction)
	{
		this.direction = direction;
	}
	public String getSrcIPAddress()
	{
		return srcIPAddress;
	}
	public void setSrcIPAddress(String srcIPAddress)
	{
		this.srcIPAddress = srcIPAddress;
	}
	public String getDstIPAddress()
	{
		return dstIPAddress;
	}
	public void setDstIPAddress(String dstIPAddress)
	{
		this.dstIPAddress = dstIPAddress;
	}
	public Integer getTimeDiff()
	{
		return timeDiff;
	}
	public void setTimeDiff(Integer timeDiff)
	{
		this.timeDiff = timeDiff;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
}
