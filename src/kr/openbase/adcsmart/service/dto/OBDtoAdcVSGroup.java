package kr.openbase.adcsmart.service.dto;

import java.io.Serializable;

public class OBDtoAdcVSGroup implements Serializable
{
    private static final long serialVersionUID = 10L;
    private Integer adcIndex;
    private Integer adcType;
    private String vsIndex;
    private String vsIp;
    
    public OBDtoAdcVSGroup(OBDtoAdcVSGroup obj)
    {
        super();
        this.vsIndex = obj.getVsIndex();
        this.adcIndex = obj.getAdcIndex();
        this.adcType = obj.getAdcType();
        this.vsIp = obj.getVsIp();
    }
    public OBDtoAdcVSGroup()
    {
    }

    
    public Integer getAdcIndex()
    {
        return adcIndex;
    }
    public void setAdcIndex(Integer adcIndex)
    {
        this.adcIndex = adcIndex;
    }
    public String getVsIndex()
    {
        return vsIndex;
    }
    public void setVsIndex(String vsIndex)
    {
        this.vsIndex = vsIndex;
    }
    public Integer getAdcType()
    {
        return adcType;
    }
    public void setAdcType(Integer adcType)
    {
        this.adcType = adcType;
    }
    public String getVsIp()
    {
        return vsIp;
    }
    public void setVsIp(String vsIp)
    {
        this.vsIp = vsIp;
    }
    @Override
    public String toString()
    {
        return "OBDtoAdcVSGroup [adcIndex=" + adcIndex + ", adcType=" + adcType
                + ", vsIndex=" + vsIndex + ", vsIp=" + vsIp + "]";
    }
}
