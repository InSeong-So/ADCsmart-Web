package kr.openbase.adcsmart.service.impl.f5.handler.dto;

public class OBDtoFdbInfo
{
    String vlan;
    String macAddress;
    String port;
    
    @Override
    public String toString()
    {
        return "OBDtoFdbInfo [vlan=" + vlan + ", macAddress=" + macAddress
                + ", port=" + port + "]";
    }
    
    public String getVlan()
    {
        return vlan;
    }
    public void setVlan(String vlan)
    {
        this.vlan = vlan;
    }
    public String getMacAddress()
    {
        return macAddress;
    }
    public void setMacAddress(String macAddress)
    {
        this.macAddress = macAddress;
    }
    public String getPort()
    {
        return port;
    }
    public void setPort(String port)
    {
        this.port = port;
    }
}
