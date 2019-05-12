package kr.openbase.adcsmart.web.facade.dto;

import java.text.SimpleDateFormat;

import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsAdcSummary;
import kr.openbase.adcsmart.web.util.NumberUtil;

public class SdsDashboardAdcSummaryDto 
{	
	private Integer index;
	private String name;
	private Integer type;
	private String ip;
	private String model;
	private String os;
	private Integer status;
	private String updateTime;
	private Integer vsTotalCount;
	private Integer vsAvailableCount;
	private Integer vsUnavailableCount;
	private Integer countVsUnavailablePeriod;
	private Integer vsDisableCount;
	private String connection;
	private String throughput;
	private Integer cpuUsage;
	private Integer memoryUsage;
	
	public Integer getIndex() 
	{
		return index;
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
	public Integer getType() 
	{
		return type;
	}
	public void setType(Integer type) 
	{
		this.type = type;
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
	public Integer getStatus() 
	{
		return status;
	}
	public void setStatus(Integer status) 
	{
		this.status = status;
	}
	public String getUpdateTime() 
	{
		return updateTime;
	}
	public void setUpdateTime(String updateTime) 
	{
		this.updateTime = updateTime;
	}
	public Integer getVsTotalCount() 
	{
		return vsTotalCount;
	}
	public void setVsTotalCount(Integer vsTotalCount) 
	{
		this.vsTotalCount = vsTotalCount;
	}
	public Integer getVsAvailableCount() 
	{
		return vsAvailableCount;
	}
	public void setVsAvailableCount(Integer vsAvailableCount) 
	{
		this.vsAvailableCount = vsAvailableCount;
	}
	public Integer getVsUnavailableCount() 
	{
		return vsUnavailableCount;
	}
	public void setVsUnavailableCount(Integer vsUnavailableCount) 
	{
		this.vsUnavailableCount = vsUnavailableCount;
	}
	public Integer getCountVsUnavailablePeriod() 
	{
		return countVsUnavailablePeriod;
	}
	public void setCountVsUnavailablePeriod(Integer countVsUnavailablePeriod) 
	{
		this.countVsUnavailablePeriod = countVsUnavailablePeriod;
	}
	public Integer getVsDisableCount() 
	{
		return vsDisableCount;
	}
	public void setVsDisableCount(Integer vsDisableCount) 
	{
		this.vsDisableCount = vsDisableCount;
	}
	public String getConnection() 
	{
		return connection;
	}
	public void setConnection(String connection) 
	{
		this.connection = connection;
	}
	public String getThroughput() 
	{
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
		return "SdsDashboardAdcSummaryDto [index=" + index + ", name=" + name
				+ ", type=" + type + ", ip=" + ip + ", model=" + model
				+ ", os=" + os + ", status=" + status + ", updateTime="
				+ updateTime + ", vsTotalCount=" + vsTotalCount
				+ ", vsAvailableCount=" + vsAvailableCount
				+ ", vsUnavailableCount=" + vsUnavailableCount
				+ ", countVsUnavailablePeriod=" + countVsUnavailablePeriod
				+ ", vsDisableCount=" + vsDisableCount + ", connection="
				+ connection + ", throughput=" + throughput + ", cpuUsage="
				+ cpuUsage + ", memoryUsage=" + memoryUsage + "]";
	}
	
	public static SdsDashboardAdcSummaryDto getSdsDashboardAdcSummary(OBDtoDashboardSdsAdcSummary obDtoDashboardSdsAdcSummary) 
	{
		SdsDashboardAdcSummaryDto adcSummary = new SdsDashboardAdcSummaryDto();
			
		adcSummary.setIndex(obDtoDashboardSdsAdcSummary.getIndex());
		adcSummary.setName(obDtoDashboardSdsAdcSummary.getName());
		adcSummary.setType(obDtoDashboardSdsAdcSummary.getType());
		adcSummary.setStatus(obDtoDashboardSdsAdcSummary.getStatus());
		if (null != obDtoDashboardSdsAdcSummary.getUpdateTime()) 
		{
			adcSummary.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
					obDtoDashboardSdsAdcSummary.getUpdateTime().getTime()));
		}
		adcSummary.setVsTotalCount(obDtoDashboardSdsAdcSummary.getCountVs());
		adcSummary.setVsAvailableCount(obDtoDashboardSdsAdcSummary.getCountVsAvail());
		adcSummary.setVsUnavailableCount(obDtoDashboardSdsAdcSummary.getCountVsUnavail());
		adcSummary.setCountVsUnavailablePeriod(obDtoDashboardSdsAdcSummary.getCountVsUnavailLong());
		adcSummary.setVsDisableCount(obDtoDashboardSdsAdcSummary.getCountVsDisable());
		adcSummary.setConnection(NumberUtil.toStringWithUnit(obDtoDashboardSdsAdcSummary.getConnection(), ""));
		adcSummary.setThroughput(NumberUtil.toStringWithUnit(obDtoDashboardSdsAdcSummary.getThroughput(), ""));
		adcSummary.setCpuUsage(obDtoDashboardSdsAdcSummary.getCpu());
		adcSummary.setMemoryUsage(obDtoDashboardSdsAdcSummary.getMemory());
		
		adcSummary.setIp(obDtoDashboardSdsAdcSummary.getIp());
		adcSummary.setModel(obDtoDashboardSdsAdcSummary.getModel());
		adcSummary.setOs(obDtoDashboardSdsAdcSummary.getOs());
		return adcSummary;
	}
}
