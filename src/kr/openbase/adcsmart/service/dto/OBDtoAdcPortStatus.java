package kr.openbase.adcsmart.service.dto;

import java.util.Date;
import java.util.List;

public class OBDtoAdcPortStatus
{
	public static final int PHY_CONN_TYPE_COPPER 	= 1;// 
	public static final int PHY_CONN_TYPE_SFP 		= 2;// 
	
	public static final int STATUS_UP 				= 1;// 
	public static final int STATUS_DOWN 			= 2;// 
	public static final int STATUS_DISABLED 		= 3;// 
	public static final int STATUS_INOPERATIVE 		= 4;//
	public static final int STATUS_UNCONNECT 		= 5;// 

	private Integer portIndex=0;
	private String intfName="";
	private Integer linkStatus = -1;
	private Integer speed = -1;
	private Integer mode = -1;
	private Integer phyConnType = -1;
	private Long pktsIn = -1L;
	private Long pktsOut = -1L;
	private Long pktsTot = -1L;
	private Long bytesIn = -1L;
	private Long bytesOut = -1L;
	private Long bytesTot = -1L;
	private Long errorsIn = -1L;
	private Long errorsOut = -1L;
	private Long errorsTot = -1L;
	private Long dropsIn = -1L;
	private Long dropsOut = -1L;
	private Long dropsTot = -1L;
	private String dispName="";
	
	private Date occurTime;
	private List<String> nameList;
	
	@Override
	public String toString()
	{
		return String.format("OBDtoAdcPortStatus [portIndex=%s, intfName=%s, linkStatus=%s, speed=%s, mode=%s, phyConnType=%s, pktsIn=%s, pktsOut=%s, pktsTot=%s, bytesIn=%s, bytesOut=%s, bytesTot=%s, errorsIn=%s, errorsOut=%s, errorsTot=%s, dropsIn=%s, dropsOut=%s, dropsTot=%s, dispName=%s, occurTime=%s, nameList=%s]", portIndex, intfName, linkStatus, speed, mode, phyConnType, pktsIn, pktsOut, pktsTot, bytesIn, bytesOut, bytesTot, errorsIn, errorsOut, errorsTot, dropsIn, dropsOut, dropsTot, dispName, occurTime, nameList);
	}
	public Integer getPortIndex()
	{
		return portIndex;
	}
	public void setPortIndex(Integer portIndex)
	{
		this.portIndex = portIndex;
	}
	public Integer getPhyConnType()
	{
		return phyConnType;
	}
	public void setPhyConnType(Integer phyConnType)
	{
		this.phyConnType = phyConnType;
	}
	public Integer getSpeed()
	{
		return speed;
	}
	public void setSpeed(Integer speed)
	{
		this.speed = speed;
	}
	public Integer getMode()
	{
		return mode;
	}
	public void setMode(Integer mode)
	{
		this.mode = mode;
	}
	public List<String> getNameList()
	{
		return nameList;
	}
	public void setNameList(List<String> nameList)
	{
		this.nameList = nameList;
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getDispName()
	{
		return dispName;
	}
	public void setDispName(String dispName)
	{
		this.dispName = dispName;
	}
	public String getIntfName()
	{
		return intfName;
	}
	public void setIntfName(String intfName)
	{
		this.intfName = intfName;
	}
	public Integer getLinkStatus()
	{
		return linkStatus;
	}
	public void setLinkStatus(Integer linkStatus)
	{
		this.linkStatus = linkStatus;
	}
	public Long getPktsIn()
	{
		return pktsIn;
	}
	public void setPktsIn(Long pktsIn)
	{
		this.pktsIn = pktsIn;
	}
	public Long getPktsOut()
	{
		return pktsOut;
	}
	public void setPktsOut(Long pktsOut)
	{
		this.pktsOut = pktsOut;
	}
	public Long getPktsTot()
	{
		return pktsTot;
	}
	public void setPktsTot(Long pktsTot)
	{
		this.pktsTot = pktsTot;
	}
	public Long getBytesIn()
	{
		return bytesIn;
	}
	public void setBytesIn(Long bytesIn)
	{
		this.bytesIn = bytesIn;
	}
	public Long getBytesOut()
	{
		return bytesOut;
	}
	public void setBytesOut(Long bytesOut)
	{
		this.bytesOut = bytesOut;
	}
	public Long getBytesTot()
	{
		return bytesTot;
	}
	public void setBytesTot(Long bytesTot)
	{
		this.bytesTot = bytesTot;
	}
	public Long getErrorsIn()
	{
		return errorsIn;
	}
	public void setErrorsIn(Long errorsIn)
	{
		this.errorsIn = errorsIn;
	}
	public Long getErrorsOut()
	{
		return errorsOut;
	}
	public void setErrorsOut(Long errorsOut)
	{
		this.errorsOut = errorsOut;
	}
	public Long getErrorsTot()
	{
		return errorsTot;
	}
	public void setErrorsTot(Long errorsTot)
	{
		this.errorsTot = errorsTot;
	}
	public Long getDropsIn()
	{
		return dropsIn;
	}
	public void setDropsIn(Long dropsIn)
	{
		this.dropsIn = dropsIn;
	}
	public Long getDropsOut()
	{
		return dropsOut;
	}
	public void setDropsOut(Long dropsOut)
	{
		this.dropsOut = dropsOut;
	}
	public Long getDropsTot()
	{
		return dropsTot;
	}
	public void setDropsTot(Long dropsTot)
	{
		this.dropsTot = dropsTot;
	}
}
