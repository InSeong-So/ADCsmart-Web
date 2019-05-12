package kr.openbase.adcsmart.service.dto.fault;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPortStatus;

public class OBDtoFaultHWStatus
{
	public static final int STATUS_NORMAL 				= 1;// 
	public static final int STATUS_ABNORMAL 			= 2;// 
	
	private ArrayList<Integer> 				cpuStatusList;
	private ArrayList<String>               cpuNormalList;
	private ArrayList<String>               cpuAbNormalList;
    private ArrayList<String>               cpuNotList;
    
	private ArrayList<Integer> 				fanStatusList;
    
	private Integer			   				temperatureStatus;
	private Integer							hddUsage;
	private Integer							hddStatus;
	private ArrayList<Integer> 				powerSupplyStatusList;
	private ArrayList<OBDtoFaultVlanInfo> 	vlanInfoList;
	private ArrayList<OBDtoAdcPortStatus>   portStatusList;

    @Override
    public String toString()
    {
        return "OBDtoFaultHWStatus [cpuStatusList=" + cpuStatusList
                + ", cpuNormalList=" + cpuNormalList + ", cpuAbNormalList="
                + cpuAbNormalList + ", cpuNotList=" + cpuNotList
                + ", fanStatusList=" + fanStatusList + ", temperatureStatus="
                + temperatureStatus + ", hddUsage=" + hddUsage + ", hddStatus="
                + hddStatus + ", powerSupplyStatusList="
                + powerSupplyStatusList + ", vlanInfoList=" + vlanInfoList
                + ", portStatusList=" + portStatusList + "]";
    }
	
	public ArrayList<Integer> getPowerSupplyStatusList()
	{
		return powerSupplyStatusList;
	}

	public void setPowerSupplyStatusList(ArrayList<Integer> powerSupplyStatusList)
	{
		this.powerSupplyStatusList = powerSupplyStatusList;
	}
	
	public Integer getHddStatus()
	{
		return hddStatus;
	}

	public void setHddStatus(Integer hddStatus)
	{
		this.hddStatus = hddStatus;
	}

	public ArrayList<Integer> getCpuStatusList()
	{
		return cpuStatusList;
	}
	public void setCpuStatusList(ArrayList<Integer> cpuStatusList)
	{
		this.cpuStatusList = cpuStatusList;
	}
	public ArrayList<Integer> getFanStatusList()
	{
		return fanStatusList;
	}
	public void setFanStatusList(ArrayList<Integer> fanStatusList)
	{
		this.fanStatusList = fanStatusList;
	}
	public ArrayList<OBDtoAdcPortStatus> getPortStatusList()
	{
		return portStatusList;
	}
	public void setPortStatusList(ArrayList<OBDtoAdcPortStatus> portStatusList)
	{
		this.portStatusList = portStatusList;
	}
	public Integer getTemperatureStatus()
	{
		return temperatureStatus;
	}
	public void setTemperatureStatus(Integer temperatureStatus)
	{
		this.temperatureStatus = temperatureStatus;
	}
	public ArrayList<OBDtoFaultVlanInfo> getVlanInfoList()
	{
		return vlanInfoList;
	}
	public void setVlanInfoList(ArrayList<OBDtoFaultVlanInfo> vlanInfoList)
	{
		this.vlanInfoList = vlanInfoList;
	}

	public Integer getHddUsage()
	{
		return hddUsage;
	}

	public void setHddUsage(Integer hddUsage)
	{
		this.hddUsage = hddUsage;
	}

    public ArrayList<String> getCpuNormalList()
    {
        return cpuNormalList;
    }

    public void setCpuNormalList(ArrayList<String> cpuNormalList)
    {
        this.cpuNormalList = cpuNormalList;
    }

    public ArrayList<String> getCpuAbNormalList()
    {
        return cpuAbNormalList;
    }

    public void setCpuAbNormalList(ArrayList<String> cpuAbNormalList)
    {
        this.cpuAbNormalList = cpuAbNormalList;
    }
    public ArrayList<String> getCpuNotList()
    {
        return cpuNotList;
    }

    public void setCpuNotList(ArrayList<String> cpuNotList)
    {
        this.cpuNotList = cpuNotList;
    }
}