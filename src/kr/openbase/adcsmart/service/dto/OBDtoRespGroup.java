package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;
import java.util.ArrayList;

public class OBDtoRespGroup
{
    private Timestamp   lastTime;
    private Integer index;
    private String name;
    private ArrayList<OBDtoRespInfo> respInfo;
    
    @Override
    public String toString()
    {
        return "OBDtoRespGroup [lastTime=" + lastTime + ", index=" + index
                + ", name=" + name + ", respInfo=" + respInfo + "]";
    }
    
    public Timestamp getLastTime()
    {
        return lastTime;
    }
    public void setLastTime(Timestamp lastTime)
    {
        this.lastTime = lastTime;
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
    public ArrayList<OBDtoRespInfo> getRespInfo()
    {
        return respInfo;
    }
    public void setRespInfo(ArrayList<OBDtoRespInfo> respInfo)
    {
        this.respInfo = respInfo;
    }
}
