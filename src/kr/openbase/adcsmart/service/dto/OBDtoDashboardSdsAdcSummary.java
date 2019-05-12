package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;

public class OBDtoDashboardSdsAdcSummary
{
	private Integer index;	//adc_index
	private String name;	//adc_name
	private Integer type;	//adc_type
	private Integer status;
	private Timestamp updateTime;		//ADC 상태 마지막 확인 시각
	private Integer countVs;
	private Integer countVsAvail;
	private Integer countVsUnavail;		//단절
	private Integer countVsUnavailLong; //단절-지정기간 이상
	private Integer countVsDisable; 	//꺼짐
	
	private Long connection;
	private Long throughput;
	private Integer cpu;
	private Integer memory;
	
	private String ip;
	private String model;
	private String os;
	
	@Override
	public String toString()
	{
		return "OBDtoDashboardSdsAdcSummary [index=" + index + ", name=" + name
				+ ", type=" + type + ", status=" + status + ", updateTime="
				+ updateTime + ", countVs=" + countVs + ", countVsAvail="
				+ countVsAvail + ", countVsUnavail=" + countVsUnavail
				+ ", countVsUnavailLong=" + countVsUnavailLong
				+ ", countVsDisable=" + countVsDisable + ", connection="
				+ connection + ", throughput=" + throughput + ", cpu=" + cpu
				+ ", memory=" + memory + ", ip=" + ip + ", model=" + model
				+ ", os=" + os + "]";
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
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
	public Timestamp getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}
	public Integer getCountVs()
	{
		return countVs;
	}
	public void setCountVs(Integer countVs)
	{
		this.countVs = countVs;
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
	public Integer getCountVsUnavailLong()
	{
		return countVsUnavailLong;
	}
	public void setCountVsUnavailLong(Integer countVsUnavailLong)
	{
		this.countVsUnavailLong = countVsUnavailLong;
	}
	public Integer getCountVsDisable()
	{
		return countVsDisable;
	}
	public void setCountVsDisable(Integer countVsDisable)
	{
		this.countVsDisable = countVsDisable;
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
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
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
}
