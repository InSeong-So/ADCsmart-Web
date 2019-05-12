package kr.openbase.adcsmart.service.dto;

public class OBDtoAdcVServerNotice
{
    private String index;
    private String alteonID;
    private Integer vsStatus;
    private String vsName;
    private String virtualIp;
    private String servicePort;
    private String servicePoolIndex;
    private String servicePoolAlteonID;
    private String noticePoolIndex;
    private String servicePoolName;
    private String noticePoolName;
    private Boolean isNotice;
    private Integer accntIndex;
    
    @Override
	public String toString() 
    {
		return "OBDtoAdcVServerNotice [index=" + index + ", alteonID=" + alteonID + ", vsStatus=" + vsStatus
				+ ", vsName=" + vsName + ", virtualIp=" + virtualIp + ", servicePort=" + servicePort
				+ ", servicePoolIndex=" + servicePoolIndex + ", servicePoolAlteonID=" + servicePoolAlteonID
				+ ", noticePoolIndex=" + noticePoolIndex + ", servicePoolName=" + servicePoolName + ", noticePoolName="
				+ noticePoolName + ", isNotice=" + isNotice + ", accntIndex=" + accntIndex + "]";
	}
    public String getServicePoolName()
    {
        return servicePoolName;
    }
    public void setServicePoolName(String servicePoolName)
    {
        this.servicePoolName = servicePoolName;
    }
    public String getNoticePoolName()
    {
        return noticePoolName;
    }
    public void setNoticePoolName(String noticePoolName)
    {
        this.noticePoolName = noticePoolName;
    }
    public Boolean getIsNotice()
    {
        return isNotice;
    }
    public void setIsNotice(Boolean isNotice)
    {
        this.isNotice = isNotice;
    }
    public String getIndex()
    {
        return index;
    }
    public void setIndex(String index)
    {
        this.index = index;
    }
    public Integer getVsStatus()
    {
        return vsStatus;
    }
    public void setVsStatus(Integer vsStatus)
    {
        this.vsStatus = vsStatus;
    }
    public String getVsName()
    {
        return vsName;
    }
    public void setVsName(String vsName)
    {
        this.vsName = vsName;
    }
    public String getVirtualIp()
    {
        return virtualIp;
    }
    public void setVirtualIp(String virtualIp)
    {
        this.virtualIp = virtualIp;
    }
    public String getServicePort()
    {
        return servicePort;
    }
    public void setServicePort(String servicePort)
    {
        this.servicePort = servicePort;
    }
    public String getServicePoolIndex()
    {
        return servicePoolIndex;
    }
    public void setServicePoolIndex(String servicePoolIndex)
    {
        this.servicePoolIndex = servicePoolIndex;
    }
    public String getNoticePoolIndex()
    {
        return noticePoolIndex;
    }
    public void setNoticePoolIndex(String noticePoolIndex)
    {
        this.noticePoolIndex = noticePoolIndex;
    }
    public String getAlteonID()
    {
        return alteonID;
    }
    public void setAlteonID(String alteonID)
    {
        this.alteonID = alteonID;
    }
    public String getServicePoolAlteonID()
    {
        return servicePoolAlteonID;
    }
    public void setServicePoolAlteonID(String servicePoolAlteonID)
    {
        this.servicePoolAlteonID = servicePoolAlteonID;
    }
	public Integer getAccntIndex() 
	{
		return accntIndex;
	}
	public void setAccntIndex(Integer accntIndex) 
	{
		this.accntIndex = accntIndex;
	}       
}
