package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBDtoMonTotalAdcOne
{
	private Integer   adcIndex;
	private Integer   status;
	private String    adcName;
	private Integer   activeBackupState; //Active / Standby
	private String    model;
	private String    swVersion; 
	private String    uptimeAge;
	private Long      throughput;
	private Long      concurrentSession;
	private String    adcIp;
	private Timestamp configTime;
	private Integer   cpu;
	private Integer   memory;
	private Long      errorPackets;
	private Long      dropPackets;
	private Long      sslTransaction;
	private Long      httpRequest;
	private Integer   sslCertValidDays;
	private Integer   interfaceAvailable;
	private Integer   interfaceUnavailable;
	private Integer   filterUse;
	private Integer   filterUnuse;
	private Integer   serviceAvailable;
	private Integer   serviceUnavailable;
	private Integer   serviceDisable;
	private Integer   adcLog24Hour;
	private Integer   slbConfig24Hour;
	private Integer   adcType; //화면에 표시하지 않지만 필요
	
	// 웹에서 단위변환된 값으로 사용하기 위해서 추가함
	private String    throughputUnit;
	private String    concurrentSessionUnit;
	
	@Override
	public String toString() {
		return "OBDtoMonTotalAdcOne [adcIndex=" + adcIndex + ", status="
				+ status + ", adcName=" + adcName + ", activeBackupState="
				+ activeBackupState + ", model=" + model + ", swVersion="
				+ swVersion + ", uptimeAge=" + uptimeAge + ", throughput="
				+ throughput + ", concurrentSession=" + concurrentSession
				+ ", adcIp=" + adcIp + ", configTime=" + configTime + ", cpu="
				+ cpu + ", memory=" + memory + ", errorPackets=" + errorPackets
				+ ", dropPackets=" + dropPackets + ", sslTransaction="
				+ sslTransaction + ", httpRequest=" + httpRequest
				+ ", sslCertValidDays=" + sslCertValidDays
				+ ", interfaceAvailable=" + interfaceAvailable
				+ ", interfaceUnavailable=" + interfaceUnavailable
				+ ", filterUse=" + filterUse + ", filterUnuse=" + filterUnuse
				+ ", serviceAvailable=" + serviceAvailable
				+ ", serviceUnavailable=" + serviceUnavailable
				+ ", serviceDisable=" + serviceDisable + ", adcLog24Hour="
				+ adcLog24Hour + ", slbConfig24Hour=" + slbConfig24Hour
				+ ", adcType=" + adcType + ", throughputUnit=" + throughputUnit
				+ ", concurrentSessionUnit=" + concurrentSessionUnit + "]";
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public Integer getActiveBackupState()
	{
		return activeBackupState;
	}
	public void setActiveBackupState(Integer activeBackupState)
	{
		this.activeBackupState = activeBackupState;
	}
	public String getModel()
	{
		return model;
	}
	public void setModel(String model)
	{
		this.model = model;
	}
	public String getSwVersion()
	{
		return swVersion;
	}
	public void setSwVersion(String swVersion)
	{
		this.swVersion = swVersion;
	}
	public String getUptimeAge()
	{
		return uptimeAge;
	}
	public void setUptimeAge(String uptimeAge)
	{
		this.uptimeAge = uptimeAge;
	}
	public Long getThroughput()
	{
		return throughput;
	}	
	public void setThroughput(Long throughput)
	{
		this.throughput = throughput;
	}
	public Long getConcurrentSession()
	{
		return concurrentSession;
	}
	public void setConcurrentSession(Long concurrentSession)
	{
		this.concurrentSession = concurrentSession;
	}
	public String getAdcIp()
	{
		return adcIp;
	}
	public void setAdcIp(String adcIp)
	{
		this.adcIp = adcIp;
	}
	public Timestamp getConfigTime()
	{
		return configTime;
	}
	public void setConfigTime(Timestamp configTime)
	{
		this.configTime = configTime;
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
	public Long getErrorPackets()
	{
		return errorPackets;
	}
	public void setErrorPackets(Long errorPackets)
	{
		this.errorPackets = errorPackets;
	}
	public Long getDropPackets()
	{
		return dropPackets;
	}
	public void setDropPackets(Long dropPackets)
	{
		this.dropPackets = dropPackets;
	}
	public Long getSslTransaction()
	{
		return sslTransaction;
	}
	public void setSslTransaction(Long sslTransaction)
	{
		this.sslTransaction = sslTransaction;
	}
	public Long getHttpRequest()
	{
		return httpRequest;
	}
	public void setHttpRequest(Long httpRequest)
	{
		this.httpRequest = httpRequest;
	}
	public Integer getSslCertValidDays()
	{
		return sslCertValidDays;
	}
	public void setSslCertValidDays(Integer sslCertValidDays)
	{
		this.sslCertValidDays = sslCertValidDays;
	}
	public Integer getInterfaceAvailable()
	{
		return interfaceAvailable;
	}
	public void setInterfaceAvailable(Integer interfaceAvailable)
	{
		this.interfaceAvailable = interfaceAvailable;
	}
	public Integer getInterfaceUnavailable()
	{
		return interfaceUnavailable;
	}
	public void setInterfaceUnavailable(Integer interfaceUnavailable)
	{
		this.interfaceUnavailable = interfaceUnavailable;
	}
	public Integer getFilterUse()
	{
		return filterUse;
	}
	public void setFilterUse(Integer filterUse)
	{
		this.filterUse = filterUse;
	}
	public Integer getFilterUnuse()
	{
		return filterUnuse;
	}
	public void setFilterUnuse(Integer filterUnuse)
	{
		this.filterUnuse = filterUnuse;
	}
	public Integer getServiceAvailable()
	{
		return serviceAvailable;
	}
	public void setServiceAvailable(Integer serviceAvailable)
	{
		this.serviceAvailable = serviceAvailable;
	}
	public Integer getServiceUnavailable()
	{
		return serviceUnavailable;
	}
	public void setServiceUnavailable(Integer serviceUnavailable)
	{
		this.serviceUnavailable = serviceUnavailable;
	}
	public Integer getServiceDisable()
	{
		return serviceDisable;
	}
	public void setServiceDisable(Integer serviceDisable)
	{
		this.serviceDisable = serviceDisable;
	}
	public Integer getAdcLog24Hour()
	{
		return adcLog24Hour;
	}
	public void setAdcLog24Hour(Integer adcLog24Hour)
	{
		this.adcLog24Hour = adcLog24Hour;
	}
	public Integer getSlbConfig24Hour()
	{
		return slbConfig24Hour;
	}
	public void setSlbConfig24Hour(Integer slbConfig24Hour)
	{
		this.slbConfig24Hour = slbConfig24Hour;
	}
	public Integer getAdcType()
	{
		return adcType;
	}
	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}
	public String getThroughputUnit()
	{
		return OBUtility.convertKmg(throughput.longValue());
	}
	public void setThroughputUnit(String throughputUnit) 
	{
		this.throughputUnit = throughputUnit;
	}
	public String getConcurrentSessionUnit()
	{
		return OBUtility.convertKmg(concurrentSession.longValue());
	}
	public void setConcurrentSessionUnit(String concurrentSessionUnit)
	{
		this.concurrentSessionUnit = concurrentSessionUnit;
	}
}
