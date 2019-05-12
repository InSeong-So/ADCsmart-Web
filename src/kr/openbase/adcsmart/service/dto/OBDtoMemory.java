package kr.openbase.adcsmart.service.dto;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;

public class OBDtoMemory
{
	private Long maxValue;
	private Long minValue;
	private Long avgValue;
	private Long maxUsage;
	private Long minUsage;
	private Long avgUsage;
	private ArrayList<OBDtoUsageMem> memList;
	private ArrayList<OBDtoAdcConfigHistory> confEventList;
	
    public Long getMaxValue()
    {
        return maxValue;
    }
    public void setMaxValue(Long maxValue)
    {
        this.maxValue = maxValue;
    }
    public Long getMinValue()
    {
        return minValue;
    }
    public void setMinValue(Long minValue)
    {
        this.minValue = minValue;
    }
    public Long getAvgValue()
    {
        return avgValue;
    }
    public void setAvgValue(Long avgValue)
    {
        this.avgValue = avgValue;
    }
    public Long getMaxUsage()
    {
        return maxUsage;
    }
    public void setMaxUsage(Long maxUsage)
    {
        this.maxUsage = maxUsage;
    }
    public Long getMinUsage()
    {
        return minUsage;
    }
    public void setMinUsage(Long minUsage)
    {
        this.minUsage = minUsage;
    }
    public Long getAvgUsage()
    {
        return avgUsage;
    }
    public void setAvgUsage(Long avgUsage)
    {
        this.avgUsage = avgUsage;
    }
    public ArrayList<OBDtoUsageMem> getMemList()
    {
        return memList;
    }
    public void setMemList(ArrayList<OBDtoUsageMem> memList)
    {
        this.memList = memList;
    }
    public ArrayList<OBDtoAdcConfigHistory> getConfEventList()
    {
        return confEventList;
    }
    public void setConfEventList(ArrayList<OBDtoAdcConfigHistory> confEventList)
    {
        this.confEventList = confEventList;
    }
    @Override
    public String toString()
    {
        return "OBDtoMemory [maxValue=" + maxValue + ", minValue=" + minValue + ", avgValue=" + avgValue + ", maxUsage=" + maxUsage + ", minUsage=" + minUsage + ", avgUsage=" + avgUsage + ", memList=" + memList + ", confEventList=" + confEventList + "]";
    }    
}