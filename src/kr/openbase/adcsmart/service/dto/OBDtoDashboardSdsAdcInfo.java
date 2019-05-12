package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;

public class OBDtoDashboardSdsAdcInfo
{
	private Integer index;
	private String name;
	private Integer status;
	private String ip;
	private Integer type;           //F5? Alteon?
	private String model;           //product model : BIG-IP 8400...
	private String os;
	private Timestamp uptime;		//ADC latest boot time
	private Integer countVs;
	private Integer countVsAvail;
	private Integer countVsUnavail;		//단절
	private Integer countVsDisable; 	//꺼짐

	private Integer countNdaysFault;	//최근 N일의 장애
	
	private Long connection;
	private Long throughput; //bits
	private Integer cpu;
	private Integer memory;
	
	private Long issueIndex; //장애 인덱스
	private Integer issueStatus;//장애 상태
	
	public OBDtoDashboardSdsAdcInfo()
	{
		issueIndex = 0L; //처리하지 않을 수 있으므로 null 방지를 위해
		issueStatus = 0; //처리하지 않을 수 있으므로 null 방지를 위해
	}

	@Override
	public String toString()
	{
		return "OBDtoDashboardSdsAdcInfo [index=" + index + ", name=" + name
				+ ", status=" + status + ", ip=" + ip + ", type=" + type
				+ ", model=" + model + ", os=" + os + ", uptime=" + uptime
				+ ", countVs=" + countVs + ", countVsAvail=" + countVsAvail
				+ ", countVsUnavail=" + countVsUnavail + ", countVsDisable="
				+ countVsDisable + ", countNdaysFault=" + countNdaysFault
				+ ", connection=" + connection + ", throughput=" + throughput
				+ ", cpu=" + cpu + ", memory=" + memory + ", issueIndex="
				+ issueIndex + ", issueStatus=" + issueStatus + "]";
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
	public Integer getCountVs()
	{
		return countVs;
	}
	public void setCountVs(Integer countVs)
	{
		this.countVs = countVs;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public Integer getIndex()
	{
		return index;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public void setIndex(Integer index)
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
	public String getModel()
	{
		return model;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	public String getOs()
	{
		return os;
	}
	public void setOs(String os)
	{
		this.os = os;
	}
	public Timestamp getUptime()
	{
		return uptime;
	}
	public void setUptime(Timestamp uptime)
	{
		this.uptime = uptime;
	}
	public Integer getCountVsAvail()
	{
		return countVsAvail;
	}
	public void setCountVsAvail(Integer countVsAvail)
	{
		this.countVsAvail = countVsAvail;
	}
	public Integer getCountVsUnavail()
	{
		return countVsUnavail;
	}
	public void setCountVsUnavail(Integer countVsUnavail)
	{
		this.countVsUnavail = countVsUnavail;
	}
	public Integer getCountVsDisable()
	{
		return countVsDisable;
	}
	public void setCountVsDisable(Integer countVsDisable)
	{
		this.countVsDisable = countVsDisable;
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
