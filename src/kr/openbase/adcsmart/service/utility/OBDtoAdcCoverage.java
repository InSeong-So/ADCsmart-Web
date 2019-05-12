package kr.openbase.adcsmart.service.utility;

import java.util.ArrayList;

public class OBDtoAdcCoverage
{
	private int adcType;// adc type
	private String conditionType;// sw version, hw version
	private String conditionVersion;// version Info
	private Integer status;// 0: unavailable, 1: available

	private ArrayList<OBDtoAdcCoverage> adcList;

    public int getAdcType()
    {
        return adcType;
    }

    public void setAdcType(int adcType)
    {
        this.adcType = adcType;
    }

    public String getConditionType()
    {
        return conditionType;
    }

    public void setConditionType(String conditionType)
    {
        this.conditionType = conditionType;
    }

    public String getConditionVersion()
    {
        return conditionVersion;
    }

    public void setConditionVersion(String conditionVersion)
    {
        this.conditionVersion = conditionVersion;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public ArrayList<OBDtoAdcCoverage> getAdcList()
    {
        return adcList;
    }

    public void setAdcList(ArrayList<OBDtoAdcCoverage> adcList)
    {
        this.adcList = adcList;
    }

    @Override
    public String toString()
    {
        return "OBDtoAdcCoverage [adcType=" + adcType + ", conditionType=" + conditionType + ", conditionVersion=" + conditionVersion + ", status=" + status + ", adcList=" + adcList + ", getAdcType()=" + getAdcType() + ", getConditionType()=" + getConditionType() + ", getConditionVersion()=" + getConditionVersion() + ", getStatus()=" + getStatus() + ", getAdcList()=" + getAdcList() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }
}
