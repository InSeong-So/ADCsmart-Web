package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;
import java.util.Date;

public class OBDtoBpsConnData
{
    private Date    occurTime=null;
    private ArrayList<Long>    bpsInValue; // BPS IN
    private ArrayList<Long>    connCurrValue; // Conn Curr
    private ArrayList<String>   name;
    
    @Override
    public String toString()
    {
        return "OBDtoBpsConnData [occurTime=" + occurTime + ", bpsInValue="
                + bpsInValue + ", connCurrValue=" + connCurrValue + ", name="
                + name + "]";
    }
    
    public Date getOccurTime()
    {
        return occurTime;   
    }
    public void setOccurTime(Date occurTime)
    {
        this.occurTime = occurTime;
    }
    public ArrayList<Long> getBpsInValue()
    {
        return bpsInValue;
    }
    public void setBpsInValue(ArrayList<Long> bpsInValue)
    {
        this.bpsInValue = bpsInValue;
    }
    public ArrayList<Long> getConnCurrValue()
    {
        return connCurrValue;
    }
    public void setConnCurrValue(ArrayList<Long> connCurrValue)
    {
        this.connCurrValue = connCurrValue;
    }

    public ArrayList<String> getName()
    {
        return name;
    }

    public void setName(ArrayList<String> name)
    {
        this.name = name;
    }
    
}
