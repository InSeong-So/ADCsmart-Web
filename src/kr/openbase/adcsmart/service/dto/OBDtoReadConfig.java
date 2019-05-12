package kr.openbase.adcsmart.service.dto;

public class OBDtoReadConfig
{
    private String snmpTrapServerAddress;
    private Integer snmpTrapPort;
    private String snmpTrapCommunity;
    private Integer snmpTrapVersion;
    private String syslogServer;
    
    public String getSnmpTrapServerAddress()
    {
        return snmpTrapServerAddress;
    }
    public void setSnmpTrapServerAddress(String snmpTrapServerAddress)
    {
        this.snmpTrapServerAddress = snmpTrapServerAddress;
    }
    public Integer getSnmpTrapPort()
    {
        return snmpTrapPort;
    }
    public void setSnmpTrapPort(Integer snmpTrapPort)
    {
        this.snmpTrapPort = snmpTrapPort;
    }
    public String getSnmpTrapCommunity()
    {
        return snmpTrapCommunity;
    }
    public void setSnmpTrapCommunity(String snmpTrapCommunity)
    {
        this.snmpTrapCommunity = snmpTrapCommunity;
    }
    public Integer getSnmpTrapVersion()
    {
        return snmpTrapVersion;
    }
    public void setSnmpTrapVersion(Integer snmpTrapVersion)
    {
        this.snmpTrapVersion = snmpTrapVersion;
    }
    public String getSyslogServer()
    {
        return syslogServer;
    }
    public void setSyslogServer(String syslogServer)
    {
        this.syslogServer = syslogServer;
    }
    @Override
    public String toString()
    {
        return "OBDtoReadConfig [snmpTrapServerAddress="
                + snmpTrapServerAddress + ", snmpTrapPort=" + snmpTrapPort
                + ", snmpTrapCommunity=" + snmpTrapCommunity
                + ", snmpTrapVersion=" + snmpTrapVersion + ", syslogServer="
                + syslogServer + "]";
    }
}
