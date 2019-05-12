package kr.openbase.adcsmart.web.facade.dto;

import java.text.SimpleDateFormat;

import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsAdcInfo;
import kr.openbase.adcsmart.web.util.NumberUtil;

public class SdsDashboardAdcInfoDto 
{
	
	private Integer index;
	private Integer type;
	private String name;
	private String ip;
	private String model;
	private String os;
	private String updateTime;
	private Integer status;
	private Integer vsAvailableCount;
	private Integer vsUnavailableCount;
	private Integer vsDisableCount;
	private Integer countNdaysFault;
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
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
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
	public String getUpdateTime() 
	{
		return updateTime;
	}
	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
	}
	public Integer getStatus() 
	{
		return status;
	}
	public void setStatus(Integer status) 
	{
		this.status = status;
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
	public Integer getVsDisableCount()
	{
		return vsDisableCount;
	}
	public void setVsDisableCount(Integer vsDisableCount)
	{
		this.vsDisableCount = vsDisableCount;
	}
	public Integer getCountNdaysFault() 
	{
		return countNdaysFault;
	}
	public void setCountNdaysFault(Integer countNdaysFault) 
	{
		this.countNdaysFault = countNdaysFault;
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
		return "SdsDashboardAdcInfoDto [index=" + index + ", type=" + type
				+ ", name=" + name + ", ip=" + ip + ", model=" + model
				+ ", os=" + os + ", updateTime=" + updateTime + ", status="
				+ status + ", vsAvailableCount=" + vsAvailableCount
				+ ", vsUnavailableCount=" + vsUnavailableCount
				+ ", vsDisableCount=" + vsDisableCount + ", countNdaysFault="
				+ countNdaysFault + ", connection=" + connection
				+ ", throughput=" + throughput + ", cpuUsage=" + cpuUsage
				+ ", memoryUsage=" + memoryUsage + "]";
	}
	
	public static SdsDashboardAdcInfoDto getSdsDashboardAdcInfo(OBDtoDashboardSdsAdcInfo obDtoDashboardSdsAdcInfo)
	{
		SdsDashboardAdcInfoDto adcInfo = new SdsDashboardAdcInfoDto();
		
		adcInfo.setIndex(obDtoDashboardSdsAdcInfo.getIndex());
		adcInfo.setType(obDtoDashboardSdsAdcInfo.getType());
		adcInfo.setName(obDtoDashboardSdsAdcInfo.getIp());
		adcInfo.setIp(obDtoDashboardSdsAdcInfo.getIp());
		adcInfo.setModel(obDtoDashboardSdsAdcInfo.getModel());
		adcInfo.setOs(obDtoDashboardSdsAdcInfo.getOs());
		
		if (null != obDtoDashboardSdsAdcInfo.getUptime())
		{
			adcInfo.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
					obDtoDashboardSdsAdcInfo.getUptime().getTime()));
		}
		adcInfo.setStatus(obDtoDashboardSdsAdcInfo.getStatus());
		adcInfo.setVsAvailableCount(obDtoDashboardSdsAdcInfo.getCountVsAvail());
		adcInfo.setVsUnavailableCount(obDtoDashboardSdsAdcInfo.getCountVsUnavail());
		adcInfo.setVsDisableCount(obDtoDashboardSdsAdcInfo.getCountVsDisable());
		adcInfo.setCountNdaysFault(obDtoDashboardSdsAdcInfo.getCountNdaysFault());
		adcInfo.setConnection(NumberUtil.toStringWithUnit(obDtoDashboardSdsAdcInfo.getConnection(), ""));
		adcInfo.setThroughput(NumberUtil.toStringWithUnit(obDtoDashboardSdsAdcInfo.getThroughput(), ""));
		adcInfo.setCpuUsage(obDtoDashboardSdsAdcInfo.getCpu());
		adcInfo.setMemoryUsage(obDtoDashboardSdsAdcInfo.getMemory());
		
		return adcInfo;
	}
	
}
