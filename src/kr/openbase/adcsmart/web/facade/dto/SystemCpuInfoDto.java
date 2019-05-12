package kr.openbase.adcsmart.web.facade.dto;

import java.util.List;

/**
 *  @author yh.yang
 */
public class SystemCpuInfoDto
{	
	private Integer intMinUsage;
	private Integer intMaxUsage;
	private Integer intAvgUsage;	
	private Integer intCurUsage;
	private String minUsage;
	private String maxUsage;
	private String avgUsage;
	private String curUsage;
	private List<SystemCpuDataDto> systemCpuDataList;
    public Integer getIntMinUsage()
    {
        return intMinUsage;
    }
    public void setIntMinUsage(Integer intMinUsage)
    {
        this.intMinUsage = intMinUsage;
    }
    public Integer getIntMaxUsage()
    {
        return intMaxUsage;
    }
    public void setIntMaxUsage(Integer intMaxUsage)
    {
        this.intMaxUsage = intMaxUsage;
    }
    public Integer getIntAvgUsage()
    {
        return intAvgUsage;
    }
    public void setIntAvgUsage(Integer intAvgUsage)
    {
        this.intAvgUsage = intAvgUsage;
    }
    public Integer getIntCurUsage()
    {
        return intCurUsage;
    }
    public void setIntCurUsage(Integer intCurUsage)
    {
        this.intCurUsage = intCurUsage;
    }
    public String getMinUsage()
    {
        return minUsage;
    }
    public void setMinUsage(String minUsage)
    {
        this.minUsage = minUsage;
    }
    public String getMaxUsage()
    {
        return maxUsage;
    }
    public void setMaxUsage(String maxUsage)
    {
        this.maxUsage = maxUsage;
    }
    public String getAvgUsage()
    {
        return avgUsage;
    }
    public void setAvgUsage(String avgUsage)
    {
        this.avgUsage = avgUsage;
    }
    public String getCurUsage()
    {
        return curUsage;
    }
    public void setCurUsage(String curUsage)
    {
        this.curUsage = curUsage;
    }
    public List<SystemCpuDataDto> getSystemCpuDataList()
    {
        return systemCpuDataList;
    }
    public void setSystemCpuDataList(List<SystemCpuDataDto> systemCpuDataList)
    {
        this.systemCpuDataList = systemCpuDataList;
    }
    @Override
    public String toString()
    {
        return "SystemCpuInfoDto [intMinUsage=" + intMinUsage + ", intMaxUsage=" + intMaxUsage + ", intAvgUsage=" + intAvgUsage + ", intCurUsage=" + intCurUsage + ", minUsage=" + minUsage + ", maxUsage=" + maxUsage + ", avgUsage=" + avgUsage + ", curUsage=" + curUsage + ", systemCpuDataList=" + systemCpuDataList + "]";
    }

}