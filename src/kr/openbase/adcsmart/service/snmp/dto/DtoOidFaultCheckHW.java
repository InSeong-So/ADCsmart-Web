package kr.openbase.adcsmart.service.snmp.dto;

public class DtoOidFaultCheckHW
{
    private String powerSupply;                   // alteon: powersupply 상태 검사. singlePowerSupplyOK(1), firstFailed(2), secondFailed(3), doubleOK(4), unknownPowerSupplyFailed(5)
    private String uptime;
    private String license;
    private String portInfoSpeed;                 // alteon: 인터페이스 speed. mbs10(2), mbs100(3), mbs1000(4), any(5), mbs10000(6)
    private String portInfoMode;                  // alteon: full-duplex(2), half-duplex(3), any(4)
    private String portInfoLink;                  // alteon: up(1), down(2), disabled(3), inoperative(4).
    private String portInfoPhyIfLastChange;       // alteon: 현재 동작 상태가 유지된 시간. TimeTicks. 10msec 단위.
    private String portStatsPhyIfInOctets;        // alteon: Counter32.
    private String portStatsPhyIfInUcastPkts;     // alteon: Counter32.
    private String portStatsPhyIfInNUcastPkts;    // alteon: Counter32.
    private String portStatsPhyIfInDiscards;      // alteon: Counter32.
    private String portStatsPhyIfInErrors;        // alteon: Counter32.
    private String portStatsPhyIfInUnknownProtos; // alteon: Counter32.
    private String mpCpuStats1Sec;
    private String mpCpuStats4Sec;
    private String mpCpuStats64Sec;
    private String spCpuStats1Sec;
    private String spCpuStats4Sec;
    private String spCpuStats64Sec;
    private String spCpuStats64CurBindings;
    private String spCpuSessionMax;
    private String mpMemStatsTotal;
    private String mpMemStatsFree;
    private String hwTemperatureStatus;           // alteon: Ok(1), exceed(2)
    private String hwFanStatus;                   // alteon: ok(1), fail(2)
    private String hwFanSpeed;                    //
    private String adcLog;

    public String getHwFanSpeed()
    {
        return hwFanSpeed;
    }

    public void setHwFanSpeed(String hwFanSpeed)
    {
        this.hwFanSpeed = hwFanSpeed;
    }

    public String getPowerSupply()
    {
        return powerSupply;
    }

    public void setPowerSupply(String powerSupply)
    {
        this.powerSupply = powerSupply;
    }

    public String getUptime()
    {
        return uptime;
    }

    public void setUptime(String uptime)
    {
        this.uptime = uptime;
    }

    public String getLicense()
    {
        return license;
    }

    public void setLicense(String license)
    {
        this.license = license;
    }

    public String getPortInfoSpeed()
    {
        return portInfoSpeed;
    }

    public void setPortInfoSpeed(String portInfoSpeed)
    {
        this.portInfoSpeed = portInfoSpeed;
    }

    public String getPortInfoMode()
    {
        return portInfoMode;
    }

    public void setPortInfoMode(String portInfoMode)
    {
        this.portInfoMode = portInfoMode;
    }

    public String getPortInfoLink()
    {
        return portInfoLink;
    }

    public void setPortInfoLink(String portInfoLink)
    {
        this.portInfoLink = portInfoLink;
    }

    public String getPortInfoPhyIfLastChange()
    {
        return portInfoPhyIfLastChange;
    }

    public void setPortInfoPhyIfLastChange(String portInfoPhyIfLastChange)
    {
        this.portInfoPhyIfLastChange = portInfoPhyIfLastChange;
    }

    public String getPortStatsPhyIfInOctets()
    {
        return portStatsPhyIfInOctets;
    }

    public void setPortStatsPhyIfInOctets(String portStatsPhyIfInOctets)
    {
        this.portStatsPhyIfInOctets = portStatsPhyIfInOctets;
    }

    public String getPortStatsPhyIfInUcastPkts()
    {
        return portStatsPhyIfInUcastPkts;
    }

    public void setPortStatsPhyIfInUcastPkts(String portStatsPhyIfInUcastPkts)
    {
        this.portStatsPhyIfInUcastPkts = portStatsPhyIfInUcastPkts;
    }

    public String getPortStatsPhyIfInNUcastPkts()
    {
        return portStatsPhyIfInNUcastPkts;
    }

    public void setPortStatsPhyIfInNUcastPkts(String portStatsPhyIfInNUcastPkts)
    {
        this.portStatsPhyIfInNUcastPkts = portStatsPhyIfInNUcastPkts;
    }

    public String getPortStatsPhyIfInDiscards()
    {
        return portStatsPhyIfInDiscards;
    }

    public void setPortStatsPhyIfInDiscards(String portStatsPhyIfInDiscards)
    {
        this.portStatsPhyIfInDiscards = portStatsPhyIfInDiscards;
    }

    public String getPortStatsPhyIfInErrors()
    {
        return portStatsPhyIfInErrors;
    }

    public void setPortStatsPhyIfInErrors(String portStatsPhyIfInErrors)
    {
        this.portStatsPhyIfInErrors = portStatsPhyIfInErrors;
    }

    public String getPortStatsPhyIfInUnknownProtos()
    {
        return portStatsPhyIfInUnknownProtos;
    }

    public void setPortStatsPhyIfInUnknownProtos(String portStatsPhyIfInUnknownProtos)
    {
        this.portStatsPhyIfInUnknownProtos = portStatsPhyIfInUnknownProtos;
    }

    // FIXME
    public String getMpCpuStats1Sec()
    {
        return mpCpuStats1Sec;
    }

    public void setMpCpuStats1Sec(String mpCpuStats1Sec)
    {
        this.mpCpuStats1Sec = mpCpuStats1Sec;
    }

    // FIXME
    public String getMpCpuStats64Sec()
    {
        return mpCpuStats64Sec;
    }

    public void setMpCpuStats64Sec(String mpCpuStats64Sec)
    {
        this.mpCpuStats64Sec = mpCpuStats64Sec;
    }

    public String getSpCpuStats1Sec()
    {
        return spCpuStats1Sec;
    }

    public void setSpCpuStats1Sec(String spCpuStats1Sec)
    {
        this.spCpuStats1Sec = spCpuStats1Sec;
    }

    public String getSpCpuStats64Sec()
    {
        return spCpuStats64Sec;
    }

    public void setSpCpuStats64Sec(String spCpuStats64Sec)
    {
        this.spCpuStats64Sec = spCpuStats64Sec;
    }

    public String getMpMemStatsTotal()
    {
        return mpMemStatsTotal;
    }

    public void setMpMemStatsTotal(String mpMemStatsTotal)
    {
        this.mpMemStatsTotal = mpMemStatsTotal;
    }

    public String getMpMemStatsFree()
    {
        return mpMemStatsFree;
    }

    public void setMpMemStatsFree(String mpMemStatsFree)
    {
        this.mpMemStatsFree = mpMemStatsFree;
    }

    public String getHwTemperatureStatus()
    {
        return hwTemperatureStatus;
    }

    public void setHwTemperatureStatus(String hwTemperatureStatus)
    {
        this.hwTemperatureStatus = hwTemperatureStatus;
    }

    public String getHwFanStatus()
    {
        return hwFanStatus;
    }

    public void setHwFanStatus(String hwFanStatus)
    {
        this.hwFanStatus = hwFanStatus;
    }

    public String getAdcLog()
    {
        return adcLog;
    }

    public void setAdcLog(String adcLog)
    {
        this.adcLog = adcLog;
    }

    public String getSpCpuStats64CurBindings()
    {
        return spCpuStats64CurBindings;
    }

    public void setSpCpuStats64CurBindings(String spCpuStats64CurBindings)
    {
        this.spCpuStats64CurBindings = spCpuStats64CurBindings;
    }

    public String getSpCpuSessionMax()
    {
        return spCpuSessionMax;
    }

    public void setSpCpuSessionMax(String spCpuSessionMax)
    {
        this.spCpuSessionMax = spCpuSessionMax;
    }

    public String getMpCpuStats4Sec()
    {
        return mpCpuStats4Sec;
    }

    public void setMpCpuStats4Sec(String mpCpuStats4Sec)
    {
        this.mpCpuStats4Sec = mpCpuStats4Sec;
    }

    public String getSpCpuStats4Sec()
    {
        return spCpuStats4Sec;
    }

    public void setSpCpuStats4Sec(String spCpuStats4Sec)
    {
        this.spCpuStats4Sec = spCpuStats4Sec;
    }
}
