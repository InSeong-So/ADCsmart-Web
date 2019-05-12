package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoADCGroupInfo
{
    private Integer index;
    private String ipAddress;
    private String adcName;
    private ArrayList<OBDtoVSservice> vsList;
    @Override
    public String toString()
    {
        return "OBDtoADCGroupInfo [index=" + index + ", ipAddress=" + ipAddress
                + ", adcName=" + adcName + ", vsList=" + vsList + "]";
    }
    public Integer getIndex()
    {
        return index;
    }
    public void setIndex(Integer index)
    {
        this.index = index;
    }
    public String getIpAddress()
    {
        return ipAddress;
    }
    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }
    public String getAdcName()
    {
        return adcName;
    }
    public void setAdcName(String adcName)
    {
        this.adcName = adcName;
    }
    public ArrayList<OBDtoVSservice> getVsList()
    {
        return vsList;
    }
    public void setVsList(ArrayList<OBDtoVSservice> vsList)
    {
        this.vsList = vsList;
    }
}
