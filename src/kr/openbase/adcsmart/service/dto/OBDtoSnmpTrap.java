package kr.openbase.adcsmart.service.dto;

public class OBDtoSnmpTrap
{
    private String snmpTrapServerAddress;
    private Integer snmpTrapPort = 161;
    private String snmpTrapCommunity;
    private Integer snmpTrapVersion;
    
    @Override
    public String toString()
    {
        return "OBDtoSnmpTrap [snmpTrapServerAddress=" + snmpTrapServerAddress
                + ", snmpTrapPort=" + snmpTrapPort + ", snmpTrapCommunity="
                + snmpTrapCommunity + ", snmpTrapVersion=" + snmpTrapVersion
                + "]";
    }
    
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
}
