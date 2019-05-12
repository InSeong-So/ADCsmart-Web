package kr.openbase.adcsmart.service.impl.dto;

import java.util.Date;

public class OBDtoConnectionDataObj
{
    private Date    occurTime=null;
    private Long    inValue=0L;
    private Long    outValue=0L;
    private Long    totalValue=0L;
    private Long    diff=0L;
    
    public Date getOccurTime()
    {
        return occurTime;
    }
    public void setOccurTime(Date occurTime)
    {
        this.occurTime = occurTime;
    }
    public Long getInValue()
    {
        return inValue;
    }
    public void setInValue(Long inValue)
    {
        this.inValue = inValue;
    }
    public Long getOutValue()
    {
        return outValue;
    }
    public void setOutValue(Long outValue)
    {
        this.outValue = outValue;
    }
    public Long getTotalValue()
    {
        return totalValue;
    }
    public void setTotalValue(Long totalValue)
    {
        this.totalValue = totalValue;
    }
    public Long getDiff()
    {
        return diff;
    }
    public void setDiff(Long diff)
    {
        this.diff = diff;
    }
    @Override
    public String toString()
    {
        return "OBDtoConnectionDataObj [occurTime=" + occurTime + ", inValue=" + inValue + ", outValue=" + outValue + ", totalValue=" + totalValue + ", diff=" + diff + "]";
    }    
}