package kr.openbase.adcsmart.service.utility;

public class OBDtoNetwork
{
	private String ipAddress;
	private String netmask;
	private String gateway;
	private String dns;
	
    @Override
    public String toString()
    {
        return "OBDtoNetwork [ipAddress=" + ipAddress + ", netmask=" + netmask + ", gateway=" + gateway + ", dns=" + dns + "]";
    }
    public String getIpAddress()
    {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }
    public String getNetmask()
    {
        return netmask;
    }
    public void setNetmask(String netmask)
    {
        this.netmask = netmask;
    }
    public String getGateway()
    {
        return gateway;
    }
    public void setGateway(String gateway)
    {
        this.gateway = gateway;
    }
    public String getDns()
    {
        return dns;
    }
    public void setDns(String dns)
    {
        this.dns = dns;
    }
}
