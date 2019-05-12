/**
 * 
 */
package kr.openbase.adcsmart.web.facade.dto;

import java.text.SimpleDateFormat;

import kr.openbase.adcsmart.service.dto.OBDtoDashboardSdsVservSummary;
import kr.openbase.adcsmart.web.util.NumberUtil;

/**
 * @author paul
 *
 */
public class SdsDashboardVsSummaryDto 
{	
	private Integer vsType;
	private Integer adcIndex;
	private String adcName;
	private Integer adcType;
	private String index;
	private String ip;
	private Integer port;
	private Integer status;
	private String updateTime;
	private String connection;
	private String throughput;
	private Integer cpuUsage;
	private Integer memoryUsage;
	private String model;
	private String os;
	
	public Integer getVsType()
	{
		return vsType;
	}
	public void setVsType(Integer vsType)
	{
		this.vsType = vsType;
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
	public Integer getAdcType()
	{
		return adcType;
	}
	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}
	public String getIndex()
	{
		return index;
	}
	public void setIndex(String index)
	{
		this.index = index;
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
	public String getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(String updateTime)
	{
		this.updateTime = updateTime;
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

	@Override
	public String toString()
	{
		return "SdsDashboardVsSummaryDto [vsType=" + vsType + ", adcIndex="
				+ adcIndex + ", adcName=" + adcName + ", adcType=" + adcType
				+ ", index=" + index + ", ip=" + ip + ", port=" + port
				+ ", status=" + status + ", updateTime=" + updateTime
				+ ", connection=" + connection + ", throughput=" + throughput
				+ ", cpuUsage=" + cpuUsage + ", memoryUsage=" + memoryUsage
				+ ", model=" + model + ", os=" + os + "]";
	}
	
	public static SdsDashboardVsSummaryDto getSdsDashboardVsSummary(OBDtoDashboardSdsVservSummary obDtoDashboardSdsVservSummary)
	{
		SdsDashboardVsSummaryDto vsSummary = new SdsDashboardVsSummaryDto();
		vsSummary.setVsType(obDtoDashboardSdsVservSummary.getRecordType());
		vsSummary.setAdcIndex(obDtoDashboardSdsVservSummary.getAdcIndex());
		vsSummary.setAdcName(obDtoDashboardSdsVservSummary.getAdcName());
		vsSummary.setAdcType(obDtoDashboardSdsVservSummary.getAdcType());
		vsSummary.setIndex(obDtoDashboardSdsVservSummary.getVservIndex());
		vsSummary.setIp(obDtoDashboardSdsVservSummary.getVservIp());
		vsSummary.setPort(obDtoDashboardSdsVservSummary.getVservport());
		vsSummary.setStatus(obDtoDashboardSdsVservSummary.getVservStatus());
		if (null != obDtoDashboardSdsVservSummary.getUpdateTime()) 
		{
			vsSummary.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
					obDtoDashboardSdsVservSummary.getUpdateTime().getTime()));
		}
		vsSummary.setConnection(NumberUtil.toStringWithUnit(obDtoDashboardSdsVservSummary.getVservConnection(), ""));
		vsSummary.setThroughput(NumberUtil.toStringWithUnit(obDtoDashboardSdsVservSummary.getVservThroughput(), ""));
		vsSummary.setCpuUsage(obDtoDashboardSdsVservSummary.getAdcCpu());
		vsSummary.setMemoryUsage(obDtoDashboardSdsVservSummary.getAdcMemory());
		vsSummary.setModel(obDtoDashboardSdsVservSummary.getAdcModel());
		vsSummary.setOs(obDtoDashboardSdsVservSummary.getAdcOs());
		return vsSummary;
	}

}
