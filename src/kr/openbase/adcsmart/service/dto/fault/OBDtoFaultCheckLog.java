package kr.openbase.adcsmart.service.dto.fault;

import java.util.Date;

public class OBDtoFaultCheckLog
{
	public static final  int	CHECK_ITEM_HW  = 1;
	public static final  int	CHECK_ITEM_SVC = 2;
	
	private Long	index;
	private Long 	logKey;
	private	Date	occurTime;
	private String 	status;//대기, 실패, 완료, 진행중
	private Integer	adcIndex;
	private String  adcName;
	private String  accntName;
	private Integer accntIndex;
	private Integer elapseTime;
	private Integer checkItem;
	private Long    templateIndex;
	private String  summary;
	private String  pcapFileName;
	private String  clientIP;
	private String  vsvcIndex;
	private String  vsvcName;
	private String  vsvcIPAddress;
	private Integer vsvcPort;
	
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultCheckLog [index=%s, logKey=%s, occurTime=%s, status=%s, adcIndex=%s, adcName=%s, accntName=%s, accntIndex=%s, elapseTime=%s, checkItem=%s, templateIndex=%s, summary=%s, pcapFileName=%s, clientIP=%s, vsvcIndex=%s, vsvcName=%s, vsvcIPAddress=%s, vsvcPort=%s]", index, logKey, occurTime, status, adcIndex, adcName, accntName, accntIndex, elapseTime, checkItem, templateIndex, summary, pcapFileName, clientIP, vsvcIndex, vsvcName, vsvcIPAddress, vsvcPort);
	}
	public String getVsvcName()
	{
		return vsvcName;
	}
	public void setVsvcName(String vsvcName)
	{
		this.vsvcName = vsvcName;
	}
	public String getVsvcIPAddress()
	{
		return vsvcIPAddress;
	}
	public void setVsvcIPAddress(String vsvcIPAddress)
	{
		this.vsvcIPAddress = vsvcIPAddress;
	}
	public Integer getVsvcPort()
	{
		return vsvcPort;
	}
	public void setVsvcPort(Integer vsvcPort)
	{
		this.vsvcPort = vsvcPort;
	}
	public String getClientIP()
	{
		return clientIP;
	}
	public void setClientIP(String clientIP)
	{
		this.clientIP = clientIP;
	}
	public String getVsvcIndex()
	{
		return vsvcIndex;
	}
	public void setVsvcIndex(String vsvcIndex)
	{
		this.vsvcIndex = vsvcIndex;
	}
	public String getPcapFileName()
	{
		return pcapFileName;
	}
	public void setPcapFileName(String pcapFileName)
	{
		this.pcapFileName = pcapFileName;
	}
	public Long getTemplateIndex()
	{
		return templateIndex;
	}
	public void setTemplateIndex(Long templateIndex)
	{
		this.templateIndex = templateIndex;
	}
	public Integer getElapseTime()
	{
		return elapseTime;
	}
	public void setElapseTime(Integer elapseTime)
	{
		this.elapseTime = elapseTime;
	}
	public Long getIndex()
	{
		return index;
	}
	public void setIndex(Long index)
	{
		this.index = index;
	}
	public Long getLogKey()
	{
		return logKey;
	}
	public void setLogKey(Long logKey)
	{
		this.logKey = logKey;
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
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
	public Integer getCheckItem()
	{
		return checkItem;
	}
	public void setCheckItem(Integer checkItem)
	{
		this.checkItem = checkItem;
	}
	public Integer getAccntIndex()
	{
		return accntIndex;
	}
	public void setAccntIndex(Integer accntIndex)
	{
		this.accntIndex = accntIndex;
	}
	public String getAccntName()
	{		
		return accntName;
	}
	public void setAccntName(String accntName)
	{
		/*String accntNameChange = "";
		if (accntIndex == 0)
		{
			accntNameChange = "System";
		}
		else
		{
			accntNameChange = accntName;
		}
		this.accntName = accntNameChange;*/
		if(accntName != null)
		{
			this.accntName = accntName;			
		}
		else
		{
			this.accntName = "System";
		}
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
