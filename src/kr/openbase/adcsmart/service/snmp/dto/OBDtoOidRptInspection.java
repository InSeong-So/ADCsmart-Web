package kr.openbase.adcsmart.service.snmp.dto;

public class OBDtoOidRptInspection
{
    private String hostName="";
    private String hotFix;
    private String model="";
    private String osVersion="";
    private String serialNo="";
    private String powerStatus="";
    private String fanStatus="";
    private String cupLoad="";
    private String upTime="";
    private String linkupInfo="";
    private String portSpeedInfo="";
    private String portDuplex="";
    private String portErrorIn="";
    private String portErrorOut="";
    private String portDiscardsIn="";
    private String portDiscardsOut="";
    private String portColls="";
    private String vlanInfo="";
    private String poolMemberStatus="";
    private String virtualServerStatus="";
    private String filterEnabledPort="";
    private String filterPortMatching="";
    private String chassisTempStatus="";

    private String memUsed;
    private String memTotal;
    private String haStatus;
    private String gateway;
    private String connMax;
    private String connCurr;
    
    public String getPortDiscardsIn()
    {
        return portDiscardsIn;
    }
    public void setPortDiscardsIn(String portDiscardsIn)
    {
        this.portDiscardsIn = portDiscardsIn;
    }
    public String getPortDiscardsOut()
    {
        return portDiscardsOut;
    }
    public void setPortDiscardsOut(String portDiscardsOut)
    {
        this.portDiscardsOut = portDiscardsOut;
    }
    public String getFilterEnabledPort()
    {
        return filterEnabledPort;
    }
    public void setFilterEnabledPort(String filterEnabledPort)
    {
        this.filterEnabledPort = filterEnabledPort;
    }
    public String getFilterPortMatching()
    {
        return filterPortMatching;
    }
    public void setFilterPortMatching(String filterPortMatching)
    {
        this.filterPortMatching = filterPortMatching;
    }
    public String getMemUsed()
    {
        return memUsed;
    }
    public void setMemUsed(String memUsed)
    {
        this.memUsed = memUsed;
    }
    public String getMemTotal()
    {
        return memTotal;
    }
    public void setMemTotal(String memTotal)
    {
        this.memTotal = memTotal;
    }
    public String getHaStatus()
    {
        return haStatus;
    }
    public void setHaStatus(String haStatus)
    {
        this.haStatus = haStatus;
    }
    public String getGateway()
    {
        return gateway;
    }
    public void setGateway(String gateway)
    {
        this.gateway = gateway;
    }
    public String getConnMax()
    {
        return connMax;
    }
    public void setConnMax(String connMax)
    {
        this.connMax = connMax;
    }
    public String getConnCurr()
    {
        return connCurr;
    }
    public void setConnCurr(String connCurr)
    {
        this.connCurr = connCurr;
    }
    public String getHotFix()
    {
        return hotFix;
    }
    public void setHotFix(String hotFix)
    {
        this.hotFix = hotFix;
    }
    public String getHostName()
    {
        return hostName;
    }
    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }
    public String getModel()
    {
        return model;
    }
    public void setModel(String model)
    {
        this.model = model;
    }
    public String getOsVersion()
    {
        return osVersion;
    }
    public void setOsVersion(String osVersion)
    {
        this.osVersion = osVersion;
    }
    public String getSerialNo()
    {
        return serialNo;
    }
    public void setSerialNo(String serialNo)
    {
        this.serialNo = serialNo;
    }
    public String getPowerStatus()
    {
        return powerStatus;
    }
    public void setPowerStatus(String powerStatus)
    {
        this.powerStatus = powerStatus;
    }
    public String getFanStatus()
    {
        return fanStatus;
    }
    public void setFanStatus(String fanStatus)
    {
        this.fanStatus = fanStatus;
    }
    public String getCupLoad()
    {
        return cupLoad;
    }
    public void setCupLoad(String cupLoad)
    {
        this.cupLoad = cupLoad;
    }
    public String getUpTime()
    {
        return upTime;
    }
    public void setUpTime(String upTime)
    {
        this.upTime = upTime;
    }
    public String getLinkupInfo()
    {
        return linkupInfo;
    }
    public void setLinkupInfo(String linkupInfo)
    {
        this.linkupInfo = linkupInfo;
    }
    public String getPortSpeedInfo()
    {
        return portSpeedInfo;
    }
    public void setPortSpeedInfo(String portSpeedInfo)
    {
        this.portSpeedInfo = portSpeedInfo;
    }
    public String getPortDuplex()
    {
        return portDuplex;
    }
    public void setPortDuplex(String portDuplex)
    {
        this.portDuplex = portDuplex;
    }
    public String getPortErrorIn()
    {
        return portErrorIn;
    }
    public void setPortErrorIn(String portErrorIn)
    {
        this.portErrorIn = portErrorIn;
    }
    public String getPortErrorOut()
    {
        return portErrorOut;
    }
    public void setPortErrorOut(String portErrorOut)
    {
        this.portErrorOut = portErrorOut;
    }
    public String getPortColls()
    {
        return portColls;
    }
    public void setPortColls(String portColls)
    {
        this.portColls = portColls;
    }
    public String getVlanInfo()
    {
        return vlanInfo;
    }
    public void setVlanInfo(String vlanInfo)
    {
        this.vlanInfo = vlanInfo;
    }
    public String getPoolMemberStatus()
    {
        return poolMemberStatus;
    }
    public void setPoolMemberStatus(String poolMemberStatus)
    {
        this.poolMemberStatus = poolMemberStatus;
    }
    public String getVirtualServerStatus()
    {
        return virtualServerStatus;
    }
    public void setVirtualServerStatus(String virtualServerStatus)
    {
        this.virtualServerStatus = virtualServerStatus;
    }
    public String getChassisTempStatus()
    {
        return chassisTempStatus;
    }
    public void setChassisTempStatus(String chassisTempStatus)
    {
        this.chassisTempStatus = chassisTempStatus;
    }
    @Override
    public String toString()
    {
        return "OBDtoOidRptInspection [hostName=" + hostName + ", hotFix=" + hotFix + ", model=" + model + ", osVersion=" + osVersion + ", serialNo=" + serialNo + ", powerStatus=" + powerStatus + ", fanStatus=" + fanStatus + ", cupLoad=" + cupLoad + ", upTime=" + upTime + ", linkupInfo=" + linkupInfo + ", portSpeedInfo=" + portSpeedInfo + ", portDuplex=" + portDuplex + ", portErrorIn=" + portErrorIn + ", portErrorOut=" + portErrorOut + ", portDiscardsIn=" + portDiscardsIn + ", portDiscardsOut=" + portDiscardsOut + ", portColls=" + portColls + ", vlanInfo=" + vlanInfo + ", poolMemberStatus=" + poolMemberStatus + ", virtualServerStatus=" + virtualServerStatus + ", filterEnabledPort=" + filterEnabledPort + ", filterPortMatching=" + filterPortMatching + ", chassisTempStatus=" + chassisTempStatus + ", memUsed=" + memUsed + ", memTotal=" + memTotal + ", haStatus=" + haStatus + ", gateway=" + gateway + ", connMax=" + connMax + ", connCurr=" + connCurr + "]";
    }
}
