package kr.openbase.adcsmart.service.dto;

import kr.openbase.adcsmart.service.utility.OBUtility;


public class OBDtoNameCurAvgMax
{
    private String name;
    private Long current;
    private Long avg;
    private Long max;
    private String currentUnit;
    private String avgUnit;
    private String maxUnit;
    
    @Override
    public String toString()
    {
        return "OBDtoNameCurAvgMax [name=" + name + ", current=" + current
                + ", avg=" + avg + ", max=" + max + ", currentUnit="
                + currentUnit + ", avgUnit=" + avgUnit + ", maxUnit=" + maxUnit
                + "]";
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public Long getCurrent()
    {
        return current;
    }
    public void setCurrent(Long current)
    {
        this.current = current;
    }
    public Long getAvg()
    {
        return avg;
    }
    public void setAvg(Long avg)
    {
        this.avg = avg;
    }
    public Long getMax()
    {
        return max;
    }
    public void setMax(Long max)
    {
        this.max = max;
    }
    public String getCurrentUnit()
    {        
        currentUnit = OBUtility.toStringWithDataUnitSvc(current, "");
        return currentUnit;
    }
    public void setCurrentUnit(String currentUnit)
    {
        this.currentUnit = currentUnit;
    }
    public String getAvgUnit()
    {
        avgUnit = OBUtility.toStringWithDataUnitSvc(avg, "");
        return avgUnit;
    }
    public void setAvgUnit(String avgUnit)
    {
        this.avgUnit = avgUnit;
    }
    public String getMaxUnit()
    {
        maxUnit = OBUtility.toStringWithDataUnitSvc(max, "");
        return maxUnit;
    }
    public void setMaxUnit(String maxUnit)
    {
        this.maxUnit = maxUnit;
    }
    
}
