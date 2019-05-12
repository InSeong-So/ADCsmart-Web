package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;

public class OBDtoDashboardSdsVservInfo
{
	private String index;
	private String name;
	private String ip;
	private Integer port;
	private Integer status;
	
	private Integer countMember;
	private Integer countMemberAvail;	//
	private Integer countMemberUnavail;	//단절
	private Integer countMemberDisable; //꺼짐
	
	private Timestamp lastConfigTime;	//최근  virtual server 변경 시각
	private Integer countNdaysConfig;	//최근 7일의 설정 N=7
	private Integer countNdaysFault;	//최근 7일의 장애 N=7
	
	private Long connection;
	private Long throughput; //bits
	private Integer cpu;
	private Integer memory;
	
	private Long issueIndex; //장애 인덱스
	private Integer issueStatus;//장애 상태
	
	public OBDtoDashboardSdsVservInfo()
	{
		issueIndex = 0L; //처리하지 않을 수 있으므로 null 방지를 위해
		issueStatus = 0; //처리하지 않을 수 있으므로 null 방지를 위해
	}

	@Override
	public String toString()
	{
		return "OBDtoDashboardSdsVservInfo [index=" + index + ", name=" + name
				+ ", ip=" + ip + ", port=" + port + ", status=" + status
				+ ", countMember=" + countMember + ", countMemberAvail="
				+ countMemberAvail + ", countMemberUnavail="
				+ countMemberUnavail + ", countMemberDisable="
				+ countMemberDisable + ", lastConfigTime=" + lastConfigTime
				+ ", countNdaysConfig=" + countNdaysConfig
				+ ", countNdaysFault=" + countNdaysFault + ", connection="
				+ connection + ", throughput=" + throughput + ", cpu=" + cpu
				+ ", memory=" + memory + "]";
	}
	
	public Long getIssueIndex()
	{
		return issueIndex;
	}
	public void setIssueIndex(Long issueIndex)
	{
		this.issueIndex = issueIndex;
	}
	public Integer getIssueStatus()
	{
		return issueStatus;
	}
	public void setIssueStatus(Integer issueStatus)
	{
		this.issueStatus = issueStatus;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
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
	public Integer getPort()
	{
		return port;
	}
	public void setPort(Integer port)
	{
		this.port = port;
	}
	public Integer getCountMember()
	{
		return countMember;
	}
	public void setCountMember(Integer countMember)
	{
		this.countMember = countMember;
	}
	public Integer getCountMemberAvail()
	{
		return countMemberAvail;
	}
	public void setCountMemberAvail(Integer countMemberAvail)
	{
		this.countMemberAvail = countMemberAvail;
	}
	public Integer getCountMemberUnavail()
	{
		return countMemberUnavail;
	}
	public void setCountMemberUnavail(Integer countMemberUnavail)
	{
		this.countMemberUnavail = countMemberUnavail;
	}
	public Integer getCountMemberDisable()
	{
		return countMemberDisable;
	}
	public void setCountMemberDisable(Integer countMemberDisable)
	{
		this.countMemberDisable = countMemberDisable;
	}
	public Timestamp getLastConfigTime()
	{
		return lastConfigTime;
	}
	public void setLastConfigTime(Timestamp lastConfigTime)
	{
		this.lastConfigTime = lastConfigTime;
	}
	public Integer getCountNdaysConfig()
	{
		return countNdaysConfig;
	}
	public void setCountNdaysConfig(Integer countNdaysConfig)
	{
		this.countNdaysConfig = countNdaysConfig;
	}
	public Integer getCountNdaysFault()
	{
		return countNdaysFault;
	}
	public void setCountNdaysFault(Integer countNdaysFault)
	{
		this.countNdaysFault = countNdaysFault;
	}
	public Long getConnection()
	{
		return connection;
	}
	public void setConnection(Long connection)
	{
		this.connection = connection;
	}
	public Long getThroughput()
	{
		return throughput;
	}
	public void setThroughput(Long throughput)
	{
		this.throughput = throughput;
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
}