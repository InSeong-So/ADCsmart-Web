package kr.openbase.adcsmart.service.dto;

public class OBDtoExcludeVip
{
    private String vsIp;
    private String vsPort;
    private String adcIp;
    
    @Override
    public String toString()
    {
        return "OBDtoExcludeVip [vsIp=" + vsIp + ", vsPort=" + vsPort + ", adcIp=" + adcIp + "]";
    }
    
    public String toSQLString()
    {
        return "'" + adcIp + "', '" + vsIp + "', '" + vsPort +"'";
    }

    public String getVsIp()
    {
        return vsIp;
    }

    public void setVsIp(String vsIp)
    {
        this.vsIp = vsIp;
    }

    public String getVsPort()
    {
        return vsPort;
    }

    public void setVsPort(String vsPort)
    {
        this.vsPort = vsPort;
    }

    public String getAdcIp()
    {
        return adcIp;
    }

    public void setAdcIp(String adcIp)
    {
        this.adcIp = adcIp;
    }    
}