package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

public class OBDtoVSGroupInfo
{
    private Integer index;
    private String name;
    private Integer count;
    private ArrayList<OBDtoADCGroupInfo> adcList;
    @Override
    public String toString()
    {
        return "OBDtoVSGroupInfo [index=" + index + ", name=" + name
                + ", count=" + count + ", adcList=" + adcList + "]";
    }
    public Integer getIndex()
    {
        return index;
    }
    public void setIndex(Integer index)
    {
        this.index = index;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public Integer getCount()
    {
        return count;
    }
    public void setCount(Integer count)
    {
        this.count = count;
    }
    public ArrayList<OBDtoADCGroupInfo> getAdcList()
    {
        return adcList;
    }
    public void setAdcList(ArrayList<OBDtoADCGroupInfo> adcList)
    {
        this.adcList = adcList;
    }
}
