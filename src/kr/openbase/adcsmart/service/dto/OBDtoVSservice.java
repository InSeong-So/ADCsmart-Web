package kr.openbase.adcsmart.service.dto;

public class OBDtoVSservice
{
    private Integer groupIndex;
    private Integer adcIndex;
    private String vsIndex;
    private String vsName;
    private String vsIP;
    private Integer vsPort;

    @Override
    public String toString()
    {
        return "OBDtoVSservice [groupIndex=" + groupIndex + ", adcIndex="
                + adcIndex + ", vsIndex=" + vsIndex + ", vsName=" + vsName
                + ", vsIP=" + vsIP + ", vsPort=" + vsPort + "]";
    }
    public Integer 
    getVsPort()
    {
        return vsPort;
    }

    public void setVsPort(Integer vsPort)
    {
        this.vsPort = vsPort;
    }
    public Integer getGroupIndex()
    {
        return groupIndex;
    }

    public void setGroupIndex(Integer groupIndex)
    {
        this.groupIndex = groupIndex;
    }

    public String getVsIndex()
    {
        return vsIndex;
    }
    public void setVsIndex(String vsIndex)
    {
        this.vsIndex = vsIndex;
    }
    public String getVsName()
    {
        return vsName;
    }
    public void setVsName(String vsName)
    {
        this.vsName = vsName;
    }

    public Integer getAdcIndex()
    {
        return adcIndex;
    }

    public void setAdcIndex(Integer adcIndex)
    {
        this.adcIndex = adcIndex;
    }
    public String getVsIP()
    {
        return vsIP;
    }
    public void setVsIP(String vsIP)
    {
        this.vsIP = vsIP;
    }
}
