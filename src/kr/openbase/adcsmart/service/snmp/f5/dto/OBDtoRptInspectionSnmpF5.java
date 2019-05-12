package kr.openbase.adcsmart.service.snmp.f5.dto;

import java.util.ArrayList;

import org.snmp4j.smi.TimeTicks;

import kr.openbase.adcsmart.service.dto.OBNameValue;

public class OBDtoRptInspectionSnmpF5
{
    private String model;
    private String osVersion;
    private String hostName;
    private String serialNo;
    private String hotFix;
    private ArrayList<OBNameValue> powerStatusList;
    private ArrayList<OBNameValue> fanStatusList;
    private ArrayList<OBNameValue> cpuLoadStatusList;
    private ArrayList<OBNameValue> chassisTempStatusList;
    private TimeTicks upTime;
    private ArrayList<OBNameValue> linkUpInfoList;
    private ArrayList<OBNameValue> linkSpeedInfoList;
    private ArrayList<OBNameValue> linkDuplexInfoList;
    private ArrayList<OBNameValue> linkErrorInList;
    private ArrayList<OBNameValue> linkErrorOutInfoList;
    private ArrayList<OBNameValue> linkCollsInfoList;
    private ArrayList<OBNameValue> vlanInfoList;
    private ArrayList<OBNameValue> poolMemberStatusList;
    private ArrayList<OBNameValue> virtualServerStatusList;
    
    private ArrayList<OBNameValue> gatewayList;
    private int                     haStatus;
    private long                    memTotal;
    private long                    memUsed;
    private long                    connMax;
    private long                    connCurr;
    
    @Override
    public String toString()
    {
        return "OBDtoRptInspectionSnmpF5 [model=" + model + ", osVersion=" + osVersion + ", hostName=" + hostName + ", serialNo=" + serialNo + ", hotFix=" + hotFix + ", powerStatusList=" + powerStatusList + ", fanStatusList=" + fanStatusList + ", cpuLoadStatusList=" + cpuLoadStatusList + ", chassisTempStatusList=" + chassisTempStatusList + ", upTime=" + upTime + ", linkUpInfoList=" + linkUpInfoList + ", linkSpeedInfoList=" + linkSpeedInfoList + ", linkDuplexInfoList=" + linkDuplexInfoList + ", linkErrorInList=" + linkErrorInList + ", linkErrorOutInfoList=" + linkErrorOutInfoList + ", linkCollsInfoList=" + linkCollsInfoList + ", vlanInfoList=" + vlanInfoList + ", poolMemberStatusList=" + poolMemberStatusList + ", virtualServerStatusList=" + virtualServerStatusList + ", gatewayList=" + gatewayList + ", haStatus=" + haStatus + ", memTotal=" + memTotal + ", memUsed=" + memUsed + ", connMax=" + connMax + ", connCurr=" + connCurr + "]";
    }
    public ArrayList<OBNameValue> getGatewayList()
    {
        return gatewayList;
    }
    public void setGatewayList(ArrayList<OBNameValue> gatewayList)
    {
        this.gatewayList = gatewayList;
    }
    public int getHaStatus()
    {
        return haStatus;
    }
    public void setHaStatus(int haStatus)
    {
        this.haStatus = haStatus;
    }
    public long getMemTotal()
    {
        return memTotal;
    }
    public void setMemTotal(long memTotal)
    {
        this.memTotal = memTotal;
    }
    public long getMemUsed()
    {
        return memUsed;
    }
    public void setMemUsed(long memUsed)
    {
        this.memUsed = memUsed;
    }
    public long getConnMax()
    {
        return connMax;
    }
    public void setConnMax(long connMax)
    {
        this.connMax = connMax;
    }
    public long getConnCurr()
    {
        return connCurr;
    }
    public void setConnCurr(long connCurr)
    {
        this.connCurr = connCurr;
    }
    public String getModel()
    {
        return model;
    }
    public String getHotFix()
    {
        return hotFix;
    }
    public void setHotFix(String hotFix)
    {
        this.hotFix = hotFix;
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
    public String getHostName()
    {
        return hostName;
    }
    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }
    public String getSerialNo()
    {
        return serialNo;
    }
    public void setSerialNo(String serialNo)
    {
        this.serialNo = serialNo;
    }
    public ArrayList<OBNameValue> getPowerStatusList()
    {
        return powerStatusList;
    }
    public void setPowerStatusList(ArrayList<OBNameValue> powerStatusList)
    {
        this.powerStatusList = powerStatusList;
    }
    public ArrayList<OBNameValue> getFanStatusList()
    {
        return fanStatusList;
    }
    public void setFanStatusList(ArrayList<OBNameValue> fanStatusList)
    {
        this.fanStatusList = fanStatusList;
    }
    public ArrayList<OBNameValue> getCpuLoadStatusList()
    {
        return cpuLoadStatusList;
    }
    public void setCpuLoadStatusList(ArrayList<OBNameValue> cpuLoadStatusList)
    {
        this.cpuLoadStatusList = cpuLoadStatusList;
    }
    public TimeTicks getUpTime()
    {
        return upTime;
    }
    public void setUpTime(TimeTicks upTime)
    {
        this.upTime = upTime;
    }
    public ArrayList<OBNameValue> getLinkUpInfoList()
    {
        return linkUpInfoList;
    }
    public void setLinkUpInfoList(ArrayList<OBNameValue> linkUpInfoList)
    {
        this.linkUpInfoList = linkUpInfoList;
    }
    public ArrayList<OBNameValue> getLinkSpeedInfoList()
    {
        return linkSpeedInfoList;
    }
    public void setLinkSpeedInfoList(ArrayList<OBNameValue> linkSpeedInfoList)
    {
        this.linkSpeedInfoList = linkSpeedInfoList;
    }
    public ArrayList<OBNameValue> getLinkDuplexInfoList()
    {
        return linkDuplexInfoList;
    }
    public void setLinkDuplexInfoList(ArrayList<OBNameValue> linkDuplexInfoList)
    {
        this.linkDuplexInfoList = linkDuplexInfoList;
    }
    public ArrayList<OBNameValue> getLinkErrorInList()
    {
        return linkErrorInList;
    }
    public void setLinkErrorInList(ArrayList<OBNameValue> linkErrorInList)
    {
        this.linkErrorInList = linkErrorInList;
    }
    public ArrayList<OBNameValue> getLinkErrorOutInfoList()
    {
        return linkErrorOutInfoList;
    }
    public void setLinkErrorOutInfoList(ArrayList<OBNameValue> linkErrorOutInfoList)
    {
        this.linkErrorOutInfoList = linkErrorOutInfoList;
    }
    public ArrayList<OBNameValue> getLinkCollsInfoList()
    {
        return linkCollsInfoList;
    }
    public void setLinkCollsInfoList(ArrayList<OBNameValue> linkCollsInfoList)
    {
        this.linkCollsInfoList = linkCollsInfoList;
    }
    public ArrayList<OBNameValue> getVlanInfoList()
    {
        return vlanInfoList;
    }
    public void setVlanInfoList(ArrayList<OBNameValue> vlanInfoList)
    {
        this.vlanInfoList = vlanInfoList;
    }
    public ArrayList<OBNameValue> getPoolMemberStatusList()
    {
        return poolMemberStatusList;
    }
    public void setPoolMemberStatusList(ArrayList<OBNameValue> poolMemberStatusList)
    {
        this.poolMemberStatusList = poolMemberStatusList;
    }
    public ArrayList<OBNameValue> getVirtualServerStatusList()
    {
        return virtualServerStatusList;
    }
    public void setVirtualServerStatusList(ArrayList<OBNameValue> virtualServerStatusList)
    {
        this.virtualServerStatusList = virtualServerStatusList;
    }
    public ArrayList<OBNameValue> getChassisTempStatusList()
    {
        return chassisTempStatusList;
    }
    public void setChassisTempStatusList(ArrayList<OBNameValue> chassisTempStatusList)
    {
        this.chassisTempStatusList = chassisTempStatusList;
    }
}
