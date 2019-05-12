package kr.openbase.adcsmart.web.facade.dto;

import java.text.SimpleDateFormat;

import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservInfo;
import kr.openbase.adcsmart.web.util.NumberUtil;

public class SdsDashboardVsInfoDto
{
	
	private String index;
	private String name;
	private String ip;
	private Integer port;
	private Integer status;
	private Integer memberCount;
	private Integer memberAvailableCount;
	private Integer memberUnavailableCount;
	private Integer memberDisableCount;
	private String lastConfigTime;
	private Integer countNdaysConfig;
	private Integer countNdaysFault;
	private String connection;
	private String throughput;
	private Integer cpuUsage;
	private Integer memoryUsage;
	
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
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public Integer getMemberCount()
	{
		return memberCount;
	}
	public void setMemberCount(Integer memberCount)
	{
		this.memberCount = memberCount;
	}
	public Integer getMemberAvailableCount() 
	{
		return memberAvailableCount;
	}
	public void setMemberAvailableCount(Integer memberAvailableCount)
	{
		this.memberAvailableCount = memberAvailableCount;
	}
	public Integer getMemberUnavailableCount()
	{
		return memberUnavailableCount;
	}
	public void setMemberUnavailableCount(Integer memberUnavailableCount)
	{
		this.memberUnavailableCount = memberUnavailableCount;
	}
	public Integer getMemberDisableCount() 
	{
		return memberDisableCount;
	}
	public void setMemberDisableCount(Integer memberDisableCount) 
	{
		this.memberDisableCount = memberDisableCount;
	}
	public String getLastConfigTime()
	{
		return lastConfigTime;
	}
	public void setLastConfigTime(String lastConfigTime) 
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
	public String getConnection() {
		return connection;
	}
	public void setConnection(String connection) {
		this.connection = connection;
	}
	public String getThroughput() {
		return throughput;
	}
	public void setThroughput(String throughput)
	{
		this.throughput = throughput;
	}
	public Integer getCpuUsage()
	{
		return cpuUsage;
	}
	public void setCpuUsage(Integer cpuUsage) 
	{
		this.cpuUsage = cpuUsage;
	}
	public Integer getMemoryUsage() 
	{
		return memoryUsage;
	}
	public void setMemoryUsage(Integer memoryUsage)
	{
		this.memoryUsage = memoryUsage;
	}
	
	@Override
	public String toString()
	{
		return "SdsDashboardVsInfoDto [index=" + index + ", name=" + name
				+ ", ip=" + ip + ", port=" + port + ", status=" + status
				+ ", memberCount=" + memberCount + ", memberAvailableCount="
				+ memberAvailableCount + ", memberUnavailableCount="
				+ memberUnavailableCount + ", memberDisableCount="
				+ memberDisableCount + ", lastConfigTime=" + lastConfigTime
				+ ", countNdaysConfig=" + countNdaysConfig
				+ ", countNdaysFault=" + countNdaysFault + ", connection="
				+ connection + ", throughput=" + throughput + ", cpuUsage="
				+ cpuUsage + ", memoryUsage=" + memoryUsage + "]";
	}
	
	public static SdsDashboardVsInfoDto getSdsDashboardVsInfo(OBDtoDashboardSdsVservInfo obDtoDashboardSdsVservInfo) 
	{
		SdsDashboardVsInfoDto vsInfo = new SdsDashboardVsInfoDto();
		
		vsInfo.setIndex(obDtoDashboardSdsVservInfo.getIndex());
		vsInfo.setName(obDtoDashboardSdsVservInfo.getName());
		vsInfo.setIp(obDtoDashboardSdsVservInfo.getIp());
		vsInfo.setPort(obDtoDashboardSdsVservInfo.getPort());
		vsInfo.setStatus(obDtoDashboardSdsVservInfo.getStatus());
		vsInfo.setMemberCount(obDtoDashboardSdsVservInfo.getCountMember());
		vsInfo.setMemberAvailableCount(obDtoDashboardSdsVservInfo.getCountMemberAvail());
		vsInfo.setMemberUnavailableCount(obDtoDashboardSdsVservInfo.getCountMemberUnavail());
		vsInfo.setMemberDisableCount(obDtoDashboardSdsVservInfo.getCountMemberDisable());
		
		if (null != obDtoDashboardSdsVservInfo.getLastConfigTime())
		{
			vsInfo.setLastConfigTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
					obDtoDashboardSdsVservInfo.getLastConfigTime().getTime()));	
		}
		vsInfo.setCountNdaysConfig(obDtoDashboardSdsVservInfo.getCountNdaysConfig());
		vsInfo.setCountNdaysFault(obDtoDashboardSdsVservInfo.getCountNdaysFault());
		vsInfo.setConnection(NumberUtil.toStringWithUnit(obDtoDashboardSdsVservInfo.getConnection(), ""));
		vsInfo.setThroughput(NumberUtil.toStringWithUnit(obDtoDashboardSdsVservInfo.getThroughput(), ""));
		vsInfo.setCpuUsage(obDtoDashboardSdsVservInfo.getCpu());
		vsInfo.setMemoryUsage(obDtoDashboardSdsVservInfo.getMemory());
		
		return vsInfo;
	}
	
}
