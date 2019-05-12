/*
 * a ADC 장비의 Pool에 설정된 Member 정보
 */
package kr.openbase.adcsmart.service.dto;

import java.io.Serializable;

public class OBDtoAdcPoolMemberAlteon implements Serializable
{
	private static final long serialVersionUID = 10L;
	private String 	index;
	private String alteonNodeID;
	private String ipAddress;// ip 주소가 입력된다.
	private Integer port;
	private Integer state;
	private Integer status;
	private Integer backupType;
	private String backupId;
	private String  extra;
	
	@Override
	public String toString()
	{
		return "OBDtoAdcPoolMemberAlteon [index=" + index + ", alteonNodeID=" + alteonNodeID + ", ipAddress=" 
				+ ipAddress + ", port=" + port + ", state=" + state + ", status=" + status + ", backupType=" 
				+ backupType + ", backupId=" + backupId + ", extra=" + extra + "]";
	}
	public OBDtoAdcPoolMemberAlteon()
	{
	}
	public OBDtoAdcPoolMemberAlteon(OBDtoAdcPoolMemberAlteon obj)
	{
		if(obj!=null)
		{
			this.index = obj.getIndex();
			this.alteonNodeID = obj.getAlteonNodeID();
			this.ipAddress = obj.getIpAddress();
			this.port = obj.getPort();
			this.state = obj.getState();
			this.status = obj.getStatus();
			this.backupType = obj.getBackupType();
			this.backupId = obj.getBackupId();
		}
	}
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getAlteonNodeID()
	{
		return alteonNodeID;
	}
	public void setAlteonNodeID(String alteonNodeID)
	{
		this.alteonNodeID = alteonNodeID;
	}
	public String getIpAddress()
	{
		return ipAddress;
	}
	public void setIpAddress(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}
	public Integer getPort()
	{
		return port;
	}
	public void setPort(Integer port)
	{
		this.port = port;
	}
	public Integer getState()
	{
		return state;
	}
	public void setState(Integer state)
	{
		this.state = state;
	}
	public String getExtra()
	{
		return extra;
	}
	public Integer getBackupType()
	{
		return backupType;
	}
	public void setBackupType(Integer backupType)
	{
		this.backupType = backupType;
	}
	public String getBackupId()
	{
		return backupId;
	}
	public void setBackupId(String backupId)
	{
		this.backupId = backupId;
	}
	public void setExtra(String extra)
	{
		this.extra = extra;
	}
}
