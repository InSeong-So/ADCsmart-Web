package kr.openbase.adcsmart.service.dto.history;

import java.util.Date;

public class OBDtoAdcConfigHistoryDB //implements Serializable
{
	//private static final long serialVersionUID = 10L;
	private Long    dbIndex;
	private int 	userType;
	private Integer accountIndex;
	private String 	accountName;
	private Integer adcIndex;
	private String 	vsIndex;
	private String 	vsName;
	private String 	vsIp;
	private Integer vsStatus;
	private int		objectType;
	private Date 	occurTime;
	private String 	summary;
	private String 	description;
	private int	 	accessType;
	private String	version; //이력 버전 yy.mmdd

	private OBDtoAdcConfigChunkAlteon chunkAlteon;
	private OBDtoAdcConfigChunkF5 chunkF5;
	private OBDtoAdcConfigChunkPAS chunkPAS;
	private OBDtoAdcConfigChunkPASK chunkPASK;

	public Long getDbIndex()
	{
		return dbIndex;
	}
	public void setDbIndex(Long dbIndex)
	{
		this.dbIndex = dbIndex;
	}
	public int getUserType()
	{
		return userType;
	}
	public void setUserType(int userType)
	{
		this.userType = userType;
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
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public int getAccessType()
	{
		return accessType;
	}
	public void setAccessType(int accessType)
	{
		this.accessType = accessType;
	}
	public OBDtoAdcConfigChunkAlteon getChunkAlteon()
	{
		return chunkAlteon;
	}
	public void setChunkAlteon(OBDtoAdcConfigChunkAlteon chunkAlteon)
	{
		this.chunkAlteon = chunkAlteon;
	}
	public OBDtoAdcConfigChunkF5 getChunkF5()
	{
		return chunkF5;
	}
	public void setChunkF5(OBDtoAdcConfigChunkF5 chunkF5)
	{
		this.chunkF5 = chunkF5;
	}
	public OBDtoAdcConfigChunkPAS getChunkPAS()
	{
		return chunkPAS;
	}
	public void setChunkPAS(OBDtoAdcConfigChunkPAS chunkPAS)
	{
		this.chunkPAS = chunkPAS;
	}
	public OBDtoAdcConfigChunkPASK getChunkPASK()
	{
		return chunkPASK;
	}
	public void setChunkPASK(OBDtoAdcConfigChunkPASK chunkPASK)
	{
		this.chunkPASK = chunkPASK;
	}
	public String getVersion()
	{
		return version;
	}
	public void setVersion(String version)
	{
		this.version = version;
	}
}
