package kr.openbase.adcsmart.service.dto;

/**
 * 경보 관리 항목 - 각 ADC마다 설정하는 경보 action을 항목
 * @author ykkim
 *
 */
public class OBDtoAlarmConfig
{
	OBDtoADCObject object;
	
	Integer adcType; //F5, Alteon, PAS/PASK
	Integer configLevel; //object가 ADC일 때만 유효한 값이다.Global, Group, ADC(mine)
	
	private OBDtoAlarmAction  adcDisconnect;// ADC 연결 실패.
	private OBDtoAlarmAction  adcBooting;// ADC (재) 부팅.  
	private OBDtoAlarmAction  adcActiveToStandby;//Active->Standby 전환.
	private OBDtoAlarmAction  adcStandbyToActive;//Standby -> Active 전환.
	private OBDtoAlarmAction  virtualServerUp;   // Virtual server up
	private OBDtoAlarmAction  virtualServerDown; // Virtual server down
	private OBDtoAlarmAction  poolMemberUp; // pool member up
	private OBDtoAlarmAction  poolMemberDown; // pool member down.
	private OBDtoAlarmAction  vrrpCollision;  // vrrp/vrid 충돌 장애
	private OBDtoAlarmAction  gatewayFailUp;// gatewayFail up
	private OBDtoAlarmAction  gatewayFailDown;// gatewayFail 장애

	private OBDtoAlarmAction  interfaceDown;// interface down
	private OBDtoAlarmAction  interfaceUp;// interface up

	private OBDtoAlarmAction  adcCpuLimit;// CPU 사용율 초과.
	private OBDtoAlarmAction  adcMPLimit;// MP CPU 사용율 초과. for alteon, pas series
	private OBDtoAlarmAction  adcSPLimit;// SP CPU 사용율 초과. for alteon, pas series
	private OBDtoAlarmAction  adcMemLimit;// Memory 사용율 초과.
	private OBDtoAlarmAction  connectionLimitHigh;// connection 기준값 초과
	private OBDtoAlarmAction  connectionLimitLow;// connection 기준값 이하
	private OBDtoAlarmAction  adcSslTransaction;// ssl transaction for f5
	private OBDtoAlarmAction  adcUptime;
	private OBDtoAlarmAction  adcPurchase;
	private OBDtoAlarmAction  adcSslcert;// for f5
	private OBDtoAlarmAction  filterSessionLimitHigh; // for Alteon filter session 기준값 초과
	
	private OBDtoAlarmAction  interfaceError;
	private OBDtoAlarmAction  interfaceUsageLimit;// 인터페이스 사용율 이상.
	private OBDtoAlarmAction  interfaceDuplexChange;
	private OBDtoAlarmAction  interfaceSpeedChange;
	private OBDtoAlarmAction  adcConfSyncFailure;
	private OBDtoAlarmAction  adcConfBackupFailure;
	
	private OBDtoAlarmAction  fanNotOperational;//for alteon
	private OBDtoAlarmAction  temperatureTooHigh;//for alteon
	private OBDtoAlarmAction  onlyOnePowerSupply;// 한개의 power supply사용. for alteon
	
	private OBDtoAlarmAction  cpuTempTooHigh;//for f5
	private OBDtoAlarmAction  cpuFanTooSlow;//for f5
	private OBDtoAlarmAction  cpuFanBad;//for f5
	private OBDtoAlarmAction  chassisTempTooHigh;//for f5
	private OBDtoAlarmAction  chassisFanBad;//for f5
	private OBDtoAlarmAction  chassisPowerSupplyBad;//for f5
	private OBDtoAlarmAction  unitGoingActive;//for f5
	private OBDtoAlarmAction  unitGoingStandby;//for f5
	private OBDtoAlarmAction  blockDDoS;//for f5
	private OBDtoAlarmAction  voltageTooHigh;//for f5
	private OBDtoAlarmAction  chassisFanTooSlow;//for f5
	
	private OBDtoAlarmAction  responseTime;
	private OBDtoAlarmAction  redundancyCheck;

	@Override
    public String toString()
    {
        return "OBDtoAlarmConfig [object=" + object + ", adcType=" + adcType
                + ", configLevel=" + configLevel + ", adcDisconnect="
                + adcDisconnect + ", adcBooting=" + adcBooting
                + ", adcActiveToStandby=" + adcActiveToStandby
                + ", adcStandbyToActive=" + adcStandbyToActive
                + ", virtualServerUp=" + virtualServerUp
                + ", virtualServerDown=" + virtualServerDown
                + ", poolMemberUp=" + poolMemberUp + ", poolMemberDown="
                + poolMemberDown + ", vrrpCollision=" + vrrpCollision
                + ", gatewayFailUp=" + gatewayFailUp + ", gatewayFailDown="
                + gatewayFailDown + ", interfaceDown=" + interfaceDown
                + ", interfaceUp=" + interfaceUp + ", adcCpuLimit="
                + adcCpuLimit + ", adcMPLimit=" + adcMPLimit + ", adcSPLimit="
                + adcSPLimit + ", adcMemLimit=" + adcMemLimit
                + ", connectionLimitHigh=" + connectionLimitHigh
                + ", connectionLimitLow=" + connectionLimitLow
                + ", adcSslTransaction=" + adcSslTransaction + ", adcUptime="
                + adcUptime + ", adcPurchase=" + adcPurchase + ", adcSslcert="
                + adcSslcert + ", filterSessionLimitHigh="
                + filterSessionLimitHigh + ", interfaceError=" + interfaceError
                + ", interfaceUsageLimit=" + interfaceUsageLimit
                + ", interfaceDuplexChange=" + interfaceDuplexChange
                + ", interfaceSpeedChange=" + interfaceSpeedChange
                + ", adcConfSyncFailure=" + adcConfSyncFailure
                + ", adcConfBackupFailure=" + adcConfBackupFailure
                + ", fanNotOperational=" + fanNotOperational
                + ", temperatureTooHigh=" + temperatureTooHigh
                + ", onlyOnePowerSupply=" + onlyOnePowerSupply
                + ", cpuTempTooHigh=" + cpuTempTooHigh + ", cpuFanTooSlow="
                + cpuFanTooSlow + ", cpuFanBad=" + cpuFanBad
                + ", chassisTempTooHigh=" + chassisTempTooHigh
                + ", chassisFanBad=" + chassisFanBad
                + ", chassisPowerSupplyBad=" + chassisPowerSupplyBad
                + ", unitGoingActive=" + unitGoingActive
                + ", unitGoingStandby=" + unitGoingStandby + ", blockDDoS="
                + blockDDoS + ", voltageTooHigh=" + voltageTooHigh
                + ", chassisFanTooSlow=" + chassisFanTooSlow
                + ", responseTime=" + responseTime + ", redundancyCheck="
                + redundancyCheck + "]";
    }
	public OBDtoAlarmAction getInterfaceUp()
	{
		return interfaceUp;
	}
	public void setInterfaceUp(OBDtoAlarmAction interfaceUp)
	{
		this.interfaceUp = interfaceUp;
	}
	public OBDtoAlarmAction getVirtualServerUp()
	{
		return virtualServerUp;
	}
	public void setVirtualServerUp(OBDtoAlarmAction virtualServerUp)
	{
		this.virtualServerUp = virtualServerUp;
	}
	public OBDtoAlarmAction getPoolMemberUp()
	{
		return poolMemberUp;
	}
	public void setPoolMemberUp(OBDtoAlarmAction poolMemberUp)
	{
		this.poolMemberUp = poolMemberUp;
	}
	public OBDtoAlarmAction getVrrpCollision()
	{
		return vrrpCollision;
	}
	public void setVrrpCollision(OBDtoAlarmAction vrrpCollision)
	{
		this.vrrpCollision = vrrpCollision;
	}	
	public OBDtoAlarmAction getRedundancyCheck()
	{
		return redundancyCheck;
	}
	public void setRedundancyCheck(OBDtoAlarmAction redundancyCheck)
	{
		this.redundancyCheck = redundancyCheck;
	}
	public OBDtoAlarmAction getResponseTime()
	{
		return responseTime;
	}
	public void setResponseTime(OBDtoAlarmAction responseTime)
	{
		this.responseTime = responseTime;
	}
	public OBDtoAlarmAction getAdcConfSyncFailure()
	{
		return adcConfSyncFailure;
	}
	public void setAdcConfSyncFailure(OBDtoAlarmAction adcConfSyncFailure)
	{
		this.adcConfSyncFailure = adcConfSyncFailure;
	}
	public OBDtoADCObject getObject()
	{
		return object;
	}
	public void setObject(OBDtoADCObject object)
	{
		this.object = object;
	}
	public Integer getAdcType()
	{
		return adcType;
	}
	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}
	public OBDtoAlarmAction getAdcDisconnect()
	{
		return adcDisconnect;
	}
	public void setAdcDisconnect(OBDtoAlarmAction adcDisconnect)
	{
		this.adcDisconnect = adcDisconnect;
	}
	public OBDtoAlarmAction getAdcBooting()
	{
		return adcBooting;
	}
	public void setAdcBooting(OBDtoAlarmAction adcBooting)
	{
		this.adcBooting = adcBooting;
	}
	public OBDtoAlarmAction getAdcActiveToStandby()
	{
		return adcActiveToStandby;
	}
	public void setAdcActiveToStandby(OBDtoAlarmAction adcActiveToStandby)
	{
		this.adcActiveToStandby = adcActiveToStandby;
	}
	public OBDtoAlarmAction getAdcStandbyToActive()
	{
		return adcStandbyToActive;
	}
	public void setAdcStandbyToActive(OBDtoAlarmAction adcStandbyToActive)
	{
		this.adcStandbyToActive = adcStandbyToActive;
	}
	public OBDtoAlarmAction getVirtualServerDown()
	{
		return virtualServerDown;
	}
	public void setVirtualServerDown(OBDtoAlarmAction virtualServerDown)
	{
		this.virtualServerDown = virtualServerDown;
	}
	public OBDtoAlarmAction getPoolMemberDown()
	{
		return poolMemberDown;
	}
	public void setPoolMemberDown(OBDtoAlarmAction poolMemberDown)
	{
		this.poolMemberDown = poolMemberDown;
	}
	public OBDtoAlarmAction getInterfaceDown()
	{
		return interfaceDown;
	}
	public void setInterfaceDown(OBDtoAlarmAction interfaceDown)
	{
		this.interfaceDown = interfaceDown;
	}
	public OBDtoAlarmAction getAdcCpuLimit()
	{
		return adcCpuLimit;
	}
	public void setAdcCpuLimit(OBDtoAlarmAction adcCpuLimit)
	{
		this.adcCpuLimit = adcCpuLimit;
	}
	public OBDtoAlarmAction getAdcMPLimit()
	{
		return adcMPLimit;
	}
	public void setAdcMPLimit(OBDtoAlarmAction adcMPLimit)
	{
		this.adcMPLimit = adcMPLimit;
	}
	public OBDtoAlarmAction getAdcSPLimit()
	{
		return adcSPLimit;
	}
	public void setAdcSPLimit(OBDtoAlarmAction adcSPLimit)
	{
		this.adcSPLimit = adcSPLimit;
	}
	public OBDtoAlarmAction getAdcMemLimit()
	{
		return adcMemLimit;
	}
	public void setAdcMemLimit(OBDtoAlarmAction adcMemLimit)
	{
		this.adcMemLimit = adcMemLimit;
	}
	public Integer getConfigLevel()
	{
		return configLevel;
	}
	public void setConfigLevel(Integer configLevel)
	{
		this.configLevel = configLevel;
	}
	public OBDtoAlarmAction getConnectionLimitHigh()
	{
		return connectionLimitHigh;
	}
	public void setConnectionLimitHigh(OBDtoAlarmAction connectionLimitHigh)
	{
		this.connectionLimitHigh = connectionLimitHigh;
	}
	public OBDtoAlarmAction getConnectionLimitLow()
	{
		return connectionLimitLow;
	}
	public void setConnectionLimitLow(OBDtoAlarmAction connectionLimitLow)
	{
		this.connectionLimitLow = connectionLimitLow;
	}
	public OBDtoAlarmAction getAdcSslTransaction()
	{
		return adcSslTransaction;
	}
	public void setAdcSslTransaction(OBDtoAlarmAction adcSslTransaction)
	{
		this.adcSslTransaction = adcSslTransaction;
	}
	public OBDtoAlarmAction getAdcUptime()
	{
		return adcUptime;
	}
	public void setAdcUptime(OBDtoAlarmAction adcUptime)
	{
		this.adcUptime = adcUptime;
	}
	public OBDtoAlarmAction getAdcPurchase()
	{
		return adcPurchase;
	}
	public void setAdcPurchase(OBDtoAlarmAction adcPurchase)
	{
		this.adcPurchase = adcPurchase;
	}
	public OBDtoAlarmAction getAdcSslcert()
	{
		return adcSslcert;
	}
	public void setAdcSslcert(OBDtoAlarmAction adcSslcert)
	{
		this.adcSslcert = adcSslcert;
	}
	public OBDtoAlarmAction getInterfaceError()
	{
		return interfaceError;
	}
	public void setInterfaceError(OBDtoAlarmAction interfaceError)
	{
		this.interfaceError = interfaceError;
	}
	public OBDtoAlarmAction getInterfaceUsageLimit()
	{
		return interfaceUsageLimit;
	}
	public void setInterfaceUsageLimit(OBDtoAlarmAction interfaceUsageLimit)
	{
		this.interfaceUsageLimit = interfaceUsageLimit;
	}
	public OBDtoAlarmAction getInterfaceDuplexChange()
	{
		return interfaceDuplexChange;
	}
	public void setInterfaceDuplexChange(OBDtoAlarmAction interfaceDuplexChange)
	{
		this.interfaceDuplexChange = interfaceDuplexChange;
	}
	public OBDtoAlarmAction getInterfaceSpeedChange()
	{
		return interfaceSpeedChange;
	}
	public void setInterfaceSpeedChange(OBDtoAlarmAction interfaceSpeedChange)
	{
		this.interfaceSpeedChange = interfaceSpeedChange;
	}
	public OBDtoAlarmAction getAdcConfBackupFailure()
	{
		return adcConfBackupFailure;
	}
	public void setAdcConfBackupFailure(OBDtoAlarmAction adcConfBackupFailure)
	{
		this.adcConfBackupFailure = adcConfBackupFailure;
	}
	public OBDtoAlarmAction getFanNotOperational()
	{
		return fanNotOperational;
	}
	public void setFanNotOperational(OBDtoAlarmAction fanNotOperational)
	{
		this.fanNotOperational = fanNotOperational;
	}
	public OBDtoAlarmAction getTemperatureTooHigh()
	{
		return temperatureTooHigh;
	}
	public void setTemperatureTooHigh(OBDtoAlarmAction temperatureTooHigh)
	{
		this.temperatureTooHigh = temperatureTooHigh;
	}
	public OBDtoAlarmAction getOnlyOnePowerSupply()
	{
		return onlyOnePowerSupply;
	}
	public void setOnlyOnePowerSupply(OBDtoAlarmAction onlyOnePowerSupply)
	{
		this.onlyOnePowerSupply = onlyOnePowerSupply;
	}
	public OBDtoAlarmAction getCpuTempTooHigh()
	{
		return cpuTempTooHigh;
	}
	public void setCpuTempTooHigh(OBDtoAlarmAction cpuTempTooHigh)
	{
		this.cpuTempTooHigh = cpuTempTooHigh;
	}
	public OBDtoAlarmAction getCpuFanTooSlow()
	{
		return cpuFanTooSlow;
	}
	public void setCpuFanTooSlow(OBDtoAlarmAction cpuFanTooSlow)
	{
		this.cpuFanTooSlow = cpuFanTooSlow;
	}
	public OBDtoAlarmAction getCpuFanBad()
	{
		return cpuFanBad;
	}
	public void setCpuFanBad(OBDtoAlarmAction cpuFanBad)
	{
		this.cpuFanBad = cpuFanBad;
	}
	public OBDtoAlarmAction getChassisTempTooHigh()
	{
		return chassisTempTooHigh;
	}
	public void setChassisTempTooHigh(OBDtoAlarmAction chassisTempTooHigh)
	{
		this.chassisTempTooHigh = chassisTempTooHigh;
	}
	public OBDtoAlarmAction getChassisFanBad()
	{
		return chassisFanBad;
	}
	public void setChassisFanBad(OBDtoAlarmAction chassisFanBad)
	{
		this.chassisFanBad = chassisFanBad;
	}
	public OBDtoAlarmAction getChassisPowerSupplyBad()
	{
		return chassisPowerSupplyBad;
	}
	public void setChassisPowerSupplyBad(OBDtoAlarmAction chassisPowerSupplyBad)
	{
		this.chassisPowerSupplyBad = chassisPowerSupplyBad;
	}
	public OBDtoAlarmAction getUnitGoingActive()
	{
		return unitGoingActive;
	}
	public void setUnitGoingActive(OBDtoAlarmAction unitGoingActive)
	{
		this.unitGoingActive = unitGoingActive;
	}
	public OBDtoAlarmAction getUnitGoingStandby()
	{
		return unitGoingStandby;
	}
	public void setUnitGoingStandby(OBDtoAlarmAction unitGoingStandby)
	{
		this.unitGoingStandby = unitGoingStandby;
	}
	public OBDtoAlarmAction getBlockDDoS()
	{
		return blockDDoS;
	}
	public void setBlockDDoS(OBDtoAlarmAction blockDDoS)
	{
		this.blockDDoS = blockDDoS;
	}
	public OBDtoAlarmAction getVoltageTooHigh()
	{
		return voltageTooHigh;
	}
	public void setVoltageTooHigh(OBDtoAlarmAction voltageTooHigh)
	{
		this.voltageTooHigh = voltageTooHigh;
	}
	public OBDtoAlarmAction getChassisFanTooSlow()
	{
		return chassisFanTooSlow;
	}
	public void setChassisFanTooSlow(OBDtoAlarmAction chassisFanTooSlow)
	{
		this.chassisFanTooSlow = chassisFanTooSlow;
	}
	public OBDtoAlarmAction getGatewayFailDown()
	{
		return gatewayFailDown;
	}
	public void setGatewayFailDown(OBDtoAlarmAction gatewayFailDown)
	{
		this.gatewayFailDown = gatewayFailDown;
	}
	public OBDtoAlarmAction getGatewayFailUp()
	{
		return gatewayFailUp;
	}
	public void setGatewayFailUp(OBDtoAlarmAction gatewayFailUp)
	{
		this.gatewayFailUp = gatewayFailUp;
	}
    public OBDtoAlarmAction getFilterSessionLimitHigh()
    {
        return filterSessionLimitHigh;
    }
    public void setFilterSessionLimitHigh(OBDtoAlarmAction filterSessionLimitHigh)
    {
        this.filterSessionLimitHigh = filterSessionLimitHigh;
    }
}