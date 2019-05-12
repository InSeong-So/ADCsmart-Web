package kr.openbase.adcsmart.service.impl.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.utility.OBMessages;

public class OBDtoMonTotalAdcCondition
{
	private OBDtoMonTotalConditionUnit status;
	private OBDtoMonTotalConditionUnit name;
	private OBDtoMonTotalConditionUnit activeBackupState;
	private OBDtoMonTotalConditionUnit type;
	private OBDtoMonTotalConditionUnit model;
	private OBDtoMonTotalConditionUnit swVersion;
	private OBDtoMonTotalConditionUnit uptimeAge;
	private OBDtoMonTotalConditionUnit throughput;
	private OBDtoMonTotalConditionUnit concurrentSession;
	private OBDtoMonTotalConditionUnit adcIp;
	private OBDtoMonTotalConditionUnit configTime;
	private OBDtoMonTotalConditionUnit cpu;
	private OBDtoMonTotalConditionUnit memory;
	private OBDtoMonTotalConditionUnit errorPackets;
	private OBDtoMonTotalConditionUnit dropPackets;
	private OBDtoMonTotalConditionUnit sslTransaction;
	private OBDtoMonTotalConditionUnit httpRequest;
	private OBDtoMonTotalConditionUnit sslCertValidDays;
	private OBDtoMonTotalConditionUnit interfaceCount;
	private OBDtoMonTotalConditionUnit filter;
	private OBDtoMonTotalConditionUnit service;
	private OBDtoMonTotalConditionUnit adcLog24Hour;
	private OBDtoMonTotalConditionUnit slbConfig24Hour;
	private String searchKeyword;
	
	public OBDtoMonTotalAdcCondition()
	{
		ArrayList<OBDtoMonTotalFilterUnit> temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_ONLINE), "1", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_OFFLINE), "0", false));
		this.status            = new OBDtoMonTotalConditionUnit(true, temp);
		
		this.name              = new OBDtoMonTotalConditionUnit(true, null);
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, "Active", "1", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, "Standby", "2", false));
		temp.add(new OBDtoMonTotalFilterUnit(3, "Unknown", "0", false));
		this.activeBackupState = new OBDtoMonTotalConditionUnit(true, temp);
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>(); 
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		this.type = new OBDtoMonTotalConditionUnit(true, temp);
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		this.model             = new OBDtoMonTotalConditionUnit(true, temp);
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		this.swVersion         = new OBDtoMonTotalConditionUnit(true, temp);
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_UPTIME10), "12:", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_UPTIME30), "30:", false));
		temp.add(new OBDtoMonTotalFilterUnit(3, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_UPTIME90), "90:", false));
		temp.add(new OBDtoMonTotalFilterUnit(4, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_UPTIME180), "180:", false));
		temp.add(new OBDtoMonTotalFilterUnit(5, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_UPTIME365), "365:", false));
		this.uptimeAge         = new OBDtoMonTotalConditionUnit(true, temp);
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, "~100M", "0:100000000", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, "100M~500M", "100000000:500000000", false));
		temp.add(new OBDtoMonTotalFilterUnit(3, "500M~1G", "500000000:1000000000", false));
		temp.add(new OBDtoMonTotalFilterUnit(4, "1G~", "1000000000:", false));
		this.throughput        = new OBDtoMonTotalConditionUnit(true, temp);
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, "~1K", "0:1000", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, "1K~10K", "1000:10000", false));
		temp.add(new OBDtoMonTotalFilterUnit(3, "10K~100K", "10000:100000", false));
		temp.add(new OBDtoMonTotalFilterUnit(4, "100K~", "100000:", false));
		this.concurrentSession = new OBDtoMonTotalConditionUnit(true, temp);
		
		this.adcIp             = new OBDtoMonTotalConditionUnit(false, null);
		
		this.configTime        = new OBDtoMonTotalConditionUnit(false, null);
		this.cpu               = new OBDtoMonTotalConditionUnit(false, null);
		this.memory            = new OBDtoMonTotalConditionUnit(false, null);
		this.errorPackets      = new OBDtoMonTotalConditionUnit(false, null);
		this.dropPackets       = new OBDtoMonTotalConditionUnit(false, null);
		this.sslTransaction    = new OBDtoMonTotalConditionUnit(false, null);
		this.httpRequest       = new OBDtoMonTotalConditionUnit(false, null);
		
		temp = new ArrayList<OBDtoMonTotalFilterUnit>();
		temp.add(new OBDtoMonTotalFilterUnit(0, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_STATUS_TOTAL), "-1", true));
		temp.add(new OBDtoMonTotalFilterUnit(1, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_SSH1), "0:1", false));
		temp.add(new OBDtoMonTotalFilterUnit(2, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_SSH10), "0:10", false));
		temp.add(new OBDtoMonTotalFilterUnit(3, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_SSH30), "0:30", false));
		temp.add(new OBDtoMonTotalFilterUnit(4, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_SSH90), "0:90", false));
		temp.add(new OBDtoMonTotalFilterUnit(5, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_SSH180), "0:180", false));
		temp.add(new OBDtoMonTotalFilterUnit(6, OBMessages.getMessage(OBMessages.MSG_DEFINEWEB_SSH365), "0:365", false));
		this.sslCertValidDays  = new OBDtoMonTotalConditionUnit(false, temp);
		
		this.interfaceCount    = new OBDtoMonTotalConditionUnit(false, null);
		this.filter            = new OBDtoMonTotalConditionUnit(false, null);
		this.service           = new OBDtoMonTotalConditionUnit(false, null);
		this.adcLog24Hour      = new OBDtoMonTotalConditionUnit(false, null);
		this.slbConfig24Hour   = new OBDtoMonTotalConditionUnit(false, null);
	}

	@Override
	public String toString()
	{
		return "OBDtoMonTotalAdcCondition [status=" + status + ", name=" + name
				+ ", activeBackupState=" + activeBackupState + ", type=" + type
				+ ", model=" + model + ", swVersion=" + swVersion
				+ ", uptimeAge=" + uptimeAge + ", throughput=" + throughput
				+ ", concurrentSession=" + concurrentSession + ", adcIp="
				+ adcIp + ", configTime=" + configTime + ", cpu=" + cpu
				+ ", memory=" + memory + ", errorPackets=" + errorPackets
				+ ", dropPackets=" + dropPackets + ", sslTransaction="
				+ sslTransaction + ", httpRequest=" + httpRequest
				+ ", sslCertValidDays=" + sslCertValidDays
				+ ", interfaceCount=" + interfaceCount + ", filter=" + filter
				+ ", service=" + service + ", adcLog24Hour=" + adcLog24Hour
				+ ", slbConfig24Hour=" + slbConfig24Hour + ", searchKeyword="
				+ searchKeyword + "]";
	}

	public OBDtoMonTotalConditionUnit getStatus()
	{
		return status;
	}
	public void setStatus(OBDtoMonTotalConditionUnit status)
	{
		this.status = status;
	}
	public OBDtoMonTotalConditionUnit getName()
	{
		return name;
	}
	public void setName(OBDtoMonTotalConditionUnit name)
	{
		this.name = name;
	}
	public OBDtoMonTotalConditionUnit getActiveBackupState()
	{
		return activeBackupState;
	}
	public void setActiveBackupState(OBDtoMonTotalConditionUnit activeBackupState)
	{
		this.activeBackupState = activeBackupState;
	}
	public OBDtoMonTotalConditionUnit getType()
	{
		return type;
	}
	public void setType(OBDtoMonTotalConditionUnit type)
	{
		this.type = type;
	}
	public OBDtoMonTotalConditionUnit getModel()
	{
		return model;
	}
	public void setModel(OBDtoMonTotalConditionUnit model)
	{
		this.model = model;
	}
	public OBDtoMonTotalConditionUnit getSwVersion()
	{
		return swVersion;
	}
	public void setSwVersion(OBDtoMonTotalConditionUnit swVersion)
	{
		this.swVersion = swVersion;
	}
	public OBDtoMonTotalConditionUnit getUptimeAge()
	{
		return uptimeAge;
	}
	public void setUptimeAge(OBDtoMonTotalConditionUnit uptimeAge)
	{
		this.uptimeAge = uptimeAge;
	}
	public OBDtoMonTotalConditionUnit getThroughput()
	{
		return throughput;
	}
	public void setThroughput(OBDtoMonTotalConditionUnit throughput)
	{
		this.throughput = throughput;
	}
	public OBDtoMonTotalConditionUnit getConcurrentSession()
	{
		return concurrentSession;
	}
	public void setConcurrentSession(OBDtoMonTotalConditionUnit concurrentSession)
	{
		this.concurrentSession = concurrentSession;
	}
	public OBDtoMonTotalConditionUnit getAdcIp()
	{
		return adcIp;
	}
	public void setAdcIp(OBDtoMonTotalConditionUnit adcIp)
	{
		this.adcIp = adcIp;
	}
	public OBDtoMonTotalConditionUnit getConfigTime()
	{
		return configTime;
	}
	public void setConfigTime(OBDtoMonTotalConditionUnit configTime)
	{
		this.configTime = configTime;
	}
	public OBDtoMonTotalConditionUnit getCpu()
	{
		return cpu;
	}
	public void setCpu(OBDtoMonTotalConditionUnit cpu)
	{
		this.cpu = cpu;
	}
	public OBDtoMonTotalConditionUnit getMemory()
	{
		return memory;
	}
	public void setMemory(OBDtoMonTotalConditionUnit memory)
	{
		this.memory = memory;
	}
	public OBDtoMonTotalConditionUnit getErrorPackets()
	{
		return errorPackets;
	}
	public void setErrorPackets(OBDtoMonTotalConditionUnit errorPackets)
	{
		this.errorPackets = errorPackets;
	}
	public OBDtoMonTotalConditionUnit getDropPackets()
	{
		return dropPackets;
	}
	public void setDropPackets(OBDtoMonTotalConditionUnit dropPackets)
	{
		this.dropPackets = dropPackets;
	}
	public OBDtoMonTotalConditionUnit getSslTransaction()
	{
		return sslTransaction;
	}
	public void setSslTransaction(OBDtoMonTotalConditionUnit sslTransaction)
	{
		this.sslTransaction = sslTransaction;
	}
	public OBDtoMonTotalConditionUnit getHttpRequest()
	{
		return httpRequest;
	}
	public void setHttpRequest(OBDtoMonTotalConditionUnit httpRequest)
	{
		this.httpRequest = httpRequest;
	}
	public OBDtoMonTotalConditionUnit getSslCertValidDays()
	{
		return sslCertValidDays;
	}
	public void setSslCertValidDays(OBDtoMonTotalConditionUnit sslCertValidDays)
	{
		this.sslCertValidDays = sslCertValidDays;
	}
	public OBDtoMonTotalConditionUnit getInterfaceCount()
	{
		return interfaceCount;
	}
	public void setInterfaceCount(OBDtoMonTotalConditionUnit interfaceCount)
	{
		this.interfaceCount = interfaceCount;
	}
	public OBDtoMonTotalConditionUnit getFilter()
	{
		return filter;
	}
	public void setFilter(OBDtoMonTotalConditionUnit filter)
	{
		this.filter = filter;
	}
	public OBDtoMonTotalConditionUnit getService()
	{
		return service;
	}
	public void setService(OBDtoMonTotalConditionUnit service)
	{
		this.service = service;
	}
	public OBDtoMonTotalConditionUnit getAdcLog24Hour()
	{
		return adcLog24Hour;
	}
	public void setAdcLog24Hour(OBDtoMonTotalConditionUnit adcLog24Hour)
	{
		this.adcLog24Hour = adcLog24Hour;
	}
	public OBDtoMonTotalConditionUnit getSlbConfig24Hour()
	{
		return slbConfig24Hour;
	}
	public void setSlbConfig24Hour(OBDtoMonTotalConditionUnit slbConfig24Hour)
	{
		this.slbConfig24Hour = slbConfig24Hour;
	}
	public String getSearchKeyword()
	{
		return searchKeyword;
	}
	public void setSearchKeyword(String searchKeyword)
	{
		this.searchKeyword = searchKeyword;
	}
}
