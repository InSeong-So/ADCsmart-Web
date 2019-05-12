package kr.openbase.adcsmart.web.facade.dto;

import java.util.List;

/**
 * @author yh.yang
 *
 */
public class SystemMemoryInfoDto {

	private String maxValue;
	private String minValue;
	private String avgValue;
	private String curValue;
	private Integer maxUsage;
	private Integer minUsage;
	private Integer avgUsage;
	private Integer curUsage;
	
	private List<SystemMemoryDataDto> systemMemoryDataList;

    public String getMaxValue()
    {
        return maxValue;
    }

    public void setMaxValue(String maxValue)
    {
        this.maxValue = maxValue;
    }

    public String getMinValue()
    {
        return minValue;
    }

    public void setMinValue(String minValue)
    {
        this.minValue = minValue;
    }

    public String getAvgValue()
    {
        return avgValue;
    }

    public void setAvgValue(String avgValue)
    {
        this.avgValue = avgValue;
    }

    public String getCurValue()
    {
        return curValue;
    }

    public void setCurValue(String curValue)
    {
        this.curValue = curValue;
    }

    public Integer getMaxUsage()
    {
        return maxUsage;
    }

    public void setMaxUsage(Integer maxUsage)
    {
        this.maxUsage = maxUsage;
    }

    public Integer getMinUsage()
    {
        return minUsage;
    }

    public void setMinUsage(Integer minUsage)
    {
        this.minUsage = minUsage;
    }

    public Integer getAvgUsage()
    {
        return avgUsage;
    }

    public void setAvgUsage(Integer avgUsage)
    {
        this.avgUsage = avgUsage;
    }

    public Integer getCurUsage()
    {
        return curUsage;
    }

    public void setCurUsage(Integer curUsage)
    {
        this.curUsage = curUsage;
    }

    public List<SystemMemoryDataDto> getSystemMemoryDataList()
    {
        return systemMemoryDataList;
    }

    public void setSystemMemoryDataList(List<SystemMemoryDataDto> systemMemoryDataList)
    {
        this.systemMemoryDataList = systemMemoryDataList;
    }

    @Override
    public String toString()
    {
        return "SystemMemoryInfoDto [maxValue=" + maxValue + ", minValue=" + minValue + ", avgValue=" + avgValue + ", curValue=" + curValue + ", maxUsage=" + maxUsage + ", minUsage=" + minUsage + ", avgUsage=" + avgUsage + ", curUsage=" + curUsage + ", systemMemoryDataList=" + systemMemoryDataList + "]";
    }
}