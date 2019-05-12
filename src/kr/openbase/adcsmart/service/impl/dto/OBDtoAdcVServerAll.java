package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBDtoAdcVServerAll
{
	private String    index;
	private Integer   state;
	private Integer   status;
	private String    name;
	private String    ip;
	private String    port;
	private Long      bpsIn;
	private Long      bpsOut;
	private Long      bpsTotal;
	private Long      concurrentSession;
	
	private String    adcName;
	private String    adcIp;
	private Integer   adcIndex; //화면에 표시하지 않지만 필요
	private Integer   adcType; //화면에 표시하지 않지만 필요
	private Integer   adcStatus; //화면에 표시하지 않지만 필요
	private Integer   member; //서비스의 전체 멤버
	private String    groupIndex;
	private String    groupName;
	private String    loadbalancing; 
	private String    healthCheck;
	private String    persistence;
	private Integer   noticeGroup;
	private Timestamp updateTime;
	private Integer   slbConfig24Hour;
	
	// 웹에서 단위변환된 값으로 사용하기 위해서 추가함
	private String    bpsInUnit;
	private String    bpsOutUnit;
	private String    bpsTotalUnit;
	private String    concurrentSessionUnit;

	@Override
    public String toString()
    {
        return "OBDtoAdcVServerAll [index=" + index + ", state=" + state
                + ", status=" + status + ", name=" + name + ", ip=" + ip
                + ", port=" + port + ", bpsIn=" + bpsIn + ", bpsOut=" + bpsOut
                + ", bpsTotal=" + bpsTotal + ", concurrentSession="
                + concurrentSession + ", adcName=" + adcName + ", adcIp="
                + adcIp + ", adcIndex=" + adcIndex + ", adcType=" + adcType
                + ", adcStatus=" + adcStatus + ", member=" + member
                + ", groupIndex=" + groupIndex + ", groupName=" + groupName
                + ", loadbalancing=" + loadbalancing + ", healthCheck="
                + healthCheck + ", persistence=" + persistence
                + ", noticeGroup=" + noticeGroup + ", updateTime=" + updateTime
                + ", slbConfig24Hour=" + slbConfig24Hour + ", bpsInUnit="
                + bpsInUnit + ", bpsOutUnit=" + bpsOutUnit + ", bpsTotalUnit="
                + bpsTotalUnit + ", concurrentSessionUnit="
                + concurrentSessionUnit + "]";
    }
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public void setConcurrentSessionUnit(String concurrentSessionUnit)
	{
		this.concurrentSessionUnit = concurrentSessionUnit;
	}
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	public String getPort()
	{
		return port;
	}
	public void setPort(String port)
	{
		this.port = port;
	}
	public Long getBpsIn()
	{
		return bpsIn;
	}
	public void setBpsIn(Long bpsIn)
	{
		this.bpsIn = bpsIn;
	}
	public Long getBpsOut()
	{
		return bpsOut;
	}
	public void setBpsOut(Long bpsOut)
	{
		this.bpsOut = bpsOut;
	}
	public Long getBpsTotal()
	{
		return bpsTotal;
	}
	public void setBpsTotal(Long bpsTotal)
	{
		this.bpsTotal = bpsTotal;
	}
	public String getBpsInUnit()
	{
		return OBUtility.convertKmg(bpsIn.longValue());
	}
	public void setBpsInUnit(String bpsInUnit)
	{
		this.bpsInUnit = bpsInUnit;
	}
	public String getBpsOutUnit()
	{
		return OBUtility.convertKmg(bpsOut.longValue());
	}
	public void setBpsOutUnit(String bpsOutUnit)
	{
		this.bpsOutUnit = bpsOutUnit;
	}
	public String getBpsTotalUnit()
	{
		return OBUtility.convertKmg(bpsTotal.longValue());
	}
	public void setBpsTotalUnit(String bpsTotalUnit)
	{
		this.bpsTotalUnit = bpsTotalUnit;
	}
	public Long getConcurrentSession()
	{
		return concurrentSession;
	}
	public void setConcurrentSession(Long concurrentSession)
	{
		this.concurrentSession = concurrentSession;
	}
	public String getConcurrentSessionUnit()
	{
		return OBUtility.convertKmg(concurrentSession.longValue());
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getAdcIp()
	{
		return adcIp;
	}
	public void setAdcIp(String adcIp)
	{
		this.adcIp = adcIp;
	}
	public Integer getAdcType()
	{
		return adcType;
	}
	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}
	public Integer getMember()
	{
		return member;
	}
	public void setMember(Integer member)
	{
		this.member = member;
	}
	public String getGroupIndex()
	{
		return groupIndex;
	}
	public void setGroupIndex(String groupIndex)
	{
		this.groupIndex = groupIndex;
	}
	public String getGroupName()
	{
		return groupName;
	}
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
	public String getLoadbalancing()
	{
		return loadbalancing;
	}
	public void setLoadbalancing(String loadbalancing)
	{
		this.loadbalancing = loadbalancing;
	}
	public String getHealthCheck()
	{
		return healthCheck;
	}
	public void setHealthCheck(String healthCheck)
	{
		this.healthCheck = healthCheck;
	}
	public String getPersistence()
	{
		return persistence;
	}
	public void setPersistence(String persistence)
	{
		this.persistence = persistence;
	}
	public Integer getNoticeGroup()
	{
		return noticeGroup;
	}
	public void setNoticeGroup(Integer noticeGroup)
	{
		this.noticeGroup = noticeGroup;
	}
	public Timestamp getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}
	public Integer getSlbConfig24Hour()
	{
		return slbConfig24Hour;
	}
	public void setSlbConfig24Hour(Integer slbConfig24Hour)
	{
		this.slbConfig24Hour = slbConfig24Hour;
	}
    public Integer getState()
    {
        return state;
    }
    public void setState(Integer state)
    {
        this.state = state;
    }
    public Integer getAdcStatus()
    {
        return adcStatus;
    }
    public void setAdcStatus(Integer adcStatus)
    {
        this.adcStatus = adcStatus;
    }
}
