package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;

public class OBDtoDashboardSdsIssueSummary
{
	private Long issueIndex;
	
	/**
	 * issueType : 장애 유형
	 * ADC_FAULT_SYSTEM : 1, adc reachable/unreachable
	 * ADC_FAULT_VIRTSRV : 2, virtual server up/down
	 * ADC_FAULT_POOLMEMS : 3, pool member up/down
	 * ADC_FAULT_LINKS : 4, link up/down
	 */
	private Integer issueType;
	private String vservIp;        //issue type에 따라 결정되는 target object
	private String memberIp;       //issue type에 따라 결정되는 target object
	private String interfaceNumber;//issue type에 따라 결정되는 target object

	private Integer adcIndex;
	private String adcName;
	private Integer adcType;
	private String vservInex;
	private Timestamp occurTime;
	private Integer issueLevel; //높음(LOG_LEVEL_HIGH):0 / 중간(LOG_LEVEL_MIDDLE):1 / 낮음(LOG_LEVEL_LOW):2
	private Integer status;
	
	private Integer cpu;
	private Integer memory;
	
	private String event;

	
	@Override
	public String toString()
	{
		return "OBDtoDashboardSdsIssueSummary [issueIndex=" + issueIndex
				+ ", issueType=" + issueType + ", vservIp=" + vservIp
				+ ", memberIp=" + memberIp + ", interfaceNumber="
				+ interfaceNumber + ", adcIndex=" + adcIndex + ", adcName="
				+ adcName + ", adcType=" + adcType + ", vservInex=" + vservInex
				+ ", occurTime=" + occurTime + ", issueLevel=" + issueLevel
				+ ", status=" + status + ", cpu=" + cpu + ", memory=" + memory + ", event=" + event
				+ "]";
	}
	public Integer getAdcType()
	{
		return adcType;
	}
	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}
	public String getInterfaceNumber()
	{
		return interfaceNumber;
	}
	public void setInterfaceNumber(String interfaceNumber)
	{
		this.interfaceNumber = interfaceNumber;
	}
	public Long getIssueIndex()
	{
		return issueIndex;
	}
	public void setIssueIndex(Long issueIndex)
	{
		this.issueIndex = issueIndex;
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
	public String getVservIp()
	{
		return vservIp;
	}
	public void setVservIp(String vservIp)
	{
		this.vservIp = vservIp;
	}
	public String getVservInex()
	{
		return vservInex;
	}
	public void setVservInex(String vservInex)
	{
		this.vservInex = vservInex;
	}
	public String getMemberIp()
	{
		return memberIp;
	}
	public void setMemberIp(String memberIp)
	{
		this.memberIp = memberIp;
	}
	public Timestamp getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Timestamp occurTime)
	{
		this.occurTime = occurTime;
	}
	public Integer getIssueLevel()
	{
		return issueLevel;
	}
	public void setIssueLevel(Integer issueLevel)
	{
		this.issueLevel = issueLevel;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public Integer getIssueType()
	{
		return issueType;
	}
	public void setIssueType(Integer issueType)
	{
		this.issueType = issueType;
	}
	public Integer getCpu()
	{
		return cpu;
	}
	public void setCpu(Integer cpu)
	{
		this.cpu = cpu;
	}
	public Integer getMemory()
	{
		return memory;
	}
	public void setMemory(Integer memory)
	{
		this.memory = memory;
	}
	public String getEvent()
	{
		return event;
	}
	public void setEvent(String event)
	{
		this.event = event;
	}		
}

