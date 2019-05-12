package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;

public class OBDtoDashboardSdsVservSummary
{
	private Integer recordType;	//0:virtual server, 1:virtual service
	private String vservIndex;
	private Integer vservStatus;
	private String vservIp;
	private Integer vservport;
	private Timestamp updateTime;   //??? ADC 업데이트 시각인지?
	private Integer adcIndex;
	private String adcName;
	private Integer adcType;        //F5? Alteon?
	private Long vservConnection;
	private Long vservThroughput;
	private Integer adcCpu;
	private Integer adcMemory;
	private String adcModel;
	private String adcOs;	
	
	@Override
	public String toString()
	{
		return "OBDtoDashboardSdsVservSummary [recordType=" + recordType
				+ ", vservIndex=" + vservIndex + ", vservStatus=" + vservStatus
				+ ", vservIp=" + vservIp + ", vservport=" + vservport
				+ ", updateTime=" + updateTime + ", adcIndex=" + adcIndex
				+ ", adcName=" + adcName + ", adcType=" + adcType
				+ ", vservConnection=" + vservConnection + ", vservThroughput="
				+ vservThroughput + ", adcCpu=" + adcCpu + ", adcMemory="
				+ adcMemory + ", adcModel=" + adcModel + ", adcOs=" + adcOs
				+ "]";
	}
	public Integer getRecordType()
	{
		return recordType;
	}
	public void setRecordType(Integer recordType)
	{
		this.recordType = recordType;
	}
	public String getVservIndex()
	{
		return vservIndex;
	}
	public void setVservIndex(String vservIndex)
	{
		this.vservIndex = vservIndex;
	}
	public Integer getVservStatus()
	{
		return vservStatus;
	}
	public void setVservStatus(Integer vservStatus)
	{
		this.vservStatus = vservStatus;
	}
	public String getVservIp()
	{
		return vservIp;
	}
	public void setVservIp(String vservIp)
	{
		this.vservIp = vservIp;
	}
	public Integer getVservport()
	{
		return vservport;
	}
	public void setVservport(Integer vservport)
	{
		this.vservport = vservport;
	}
	public Timestamp getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
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
	public Long getVservConnection()
	{
		return vservConnection;
	}
	public void setVservConnection(Long vservConnection)
	{
		this.vservConnection = vservConnection;
	}
	public Long getVservThroughput()
	{
		return vservThroughput;
	}
	public void setVservThroughput(Long vservThroughput)
	{
		this.vservThroughput = vservThroughput;
	}
	public Integer getAdcCpu()
	{
		return adcCpu;
	}
	public void setAdcCpu(Integer adcCpu)
	{
		this.adcCpu = adcCpu;
	}
	public Integer getAdcMemory()
	{
		return adcMemory;
	}
	public void setAdcMemory(Integer adcMemory)
	{
		this.adcMemory = adcMemory;
	}
	public String getAdcModel()
	{
		return adcModel;
	}
	public void setAdcModel(String adcModel)
	{
		this.adcModel = adcModel;
	}
	public String getAdcOs()
	{
		return adcOs;
	}
	public void setAdcOs(String adcOs)
	{
		this.adcOs = adcOs;
	}	
}