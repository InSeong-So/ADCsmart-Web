package kr.openbase.adcsmart.service.dto.history;

import java.util.Date;

public class OBDtoAdcConfigHistory
{
	private int         logSeq;
	private int 		userType; //CHANGE_BY_USER = 1 / CHANGE_BY_SYSTEM = 2 
	private int 		accessType; //CHANGE_TYPE_NONE = 0/CHANGE_TYPE_ADD = 1 /CHANGE_TYPE_EDIT = 2/CHANGE_TYPE_DELETE = 3
	private int		adcType;
	private Integer 	accountIndex;
	private String 	accountName;
	private Integer 	adcIndex;
	private String 	adcName; //adc 이름, 2013.4.12 추가 
	private String 	vsIndex;
	private String 	vsName;
	private String 	vsIp;
	private Integer 	vsStatus;
	private Integer	vsAlive; 	//OBJECT_DEAD = 0, OBJECT_ALIVE = 1
	private int		objectType; //CHANGE_OBJECT_VIRTUALSERVER = 0 / CHANGE_OBJECT_PERSISTENCE = 1
	private Date 		occurTime;
	private String 	summary;

	@Override
	public String toString()
	{
		return "OBDtoAdcConfigHistory [logSeq=" + logSeq + ", userType="
				+ userType + ", accessType=" + accessType + ", adcType="
				+ adcType + ", accountIndex=" + accountIndex + ", accountName="
				+ accountName + ", adcIndex=" + adcIndex + ", adcName="
				+ adcName + ", vsIndex=" + vsIndex + ", vsName=" + vsName
				+ ", vsIp=" + vsIp + ", vsStatus=" + vsStatus + ", vsAlive="
				+ vsAlive + ", objectType=" + objectType + ", occurTime="
				+ occurTime + ", summary=" + summary + "]";
	}
	public int getLogSeq()
	{
		return logSeq;
	}
	public void setLogSeq(int logSeq)
	{
		this.logSeq = logSeq;
	}
	public int getAdcType()
	{
		return adcType;
	}
	public void setAdcType(int adcType)
	{
		this.adcType = adcType;
	}
	public int getUserType()
	{
		return userType;
	}
	public void setUserType(int userType)
	{
		this.userType = userType;
	}
	public int getAccessType()
	{
		return accessType;
	}
	public void setAccessType(int accessType)
	{
		this.accessType = accessType;
	}
	public Integer getAccountIndex()
	{
		return accountIndex;
	}
	public void setAccountIndex(Integer accountIndex)
	{
		this.accountIndex = accountIndex;
	}
	public String getAccountName()
	{
		return accountName;
	}
	public void setAccountName(String accountName)
	{
		this.accountName = accountName;
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getVsIndex()
	{
		return vsIndex;
	}
	public void setVsIndex(String vsIndex)
	{
		this.vsIndex = vsIndex;
	}
	public String getVsName()
	{
		return vsName;
	}
	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
	public String getVsIp()
	{
		return vsIp;
	}
	public void setVsIp(String vsIp)
	{
		this.vsIp = vsIp;
	}
	public Integer getVsStatus()
	{
		return vsStatus;
	}
	public void setVsStatus(Integer vsStatus)
	{
		this.vsStatus = vsStatus;
	}
	public int getObjectType()
	{
		return objectType;
	}
	public void setObjectType(int objectType)
	{
		this.objectType = objectType;
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	public Integer getVsAlive()
	{
		return vsAlive;
	}
	public void setVsAlive(Integer vsAlive)
	{
		this.vsAlive = vsAlive;
	}
}
