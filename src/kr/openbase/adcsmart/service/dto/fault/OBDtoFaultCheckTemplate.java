package kr.openbase.adcsmart.service.dto.fault;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoElement;

public class OBDtoFaultCheckTemplate
{
	private		Long		index=0L;//0:사용자 지정.
	private		String		name;
	private		Integer		adcType;
	private		ArrayList<OBDtoElement> hwCheckItems;
	private 	Integer     hwCheckItemCount;     
	private		ArrayList<OBDtoElement> l23CheckItems;
	private 	Integer     l23CheckItemCount;     
	private		ArrayList<OBDtoElement> l47CheckItems;
	private 	Integer     l47CheckItemCount;     
	private		ArrayList<OBDtoElement> svcCheckItems;
	private		Integer     svcCheckFlg;//1:check.
	private 	Integer     svcCheckItemCount;     
	private		Integer		thresholdHWCpuUsage		=80;
	private		Integer		thresholdHWMemoryUsage	=85;
	private		Integer		thresholdHWFanMin		=3500;// min rpm. 
	private    	Integer		thresholdHWFanMax		=15000;// max rpm.
	private		Integer		thresholdHWAdcLogCount	=10;
	private		Integer		thresholdL47SleepVSDay	=7;// Virtual Server 유휴설정.
	private		String		svcClientIPAddress="";
	private		String		svcVSIndex="";
	private		String		svcVSName="";
	private		String		svcVSIPAddress="";
	
	@Override
	public String toString()
	{
		return String.format("OBDtoFaultCheckTemplate [index=%s, name=%s, adcType=%s, hwCheckItems=%s, hwCheckItemCount=%s, l23CheckItems=%s, l23CheckItemCount=%s, l47CheckItems=%s, l47CheckItemCount=%s, svcCheckItems=%s, svcCheckFlg=%s, svcCheckItemCount=%s, thresholdHWCpuUsage=%s, thresholdHWMemoryUsage=%s, thresholdHWFanMin=%s, thresholdHWFanMax=%s, thresholdHWAdcLogCount=%s, thresholdL47SleepVSDay=%s, thresholdL47FlbSleepVSDay=%s, svcClientIPAddress=%s, svcVSIndex=%s, svcVSName=%s, svcVSIPAddress=%s]", index, name, adcType, hwCheckItems, hwCheckItemCount, l23CheckItems, l23CheckItemCount, l47CheckItems, l47CheckItemCount, svcCheckItems, svcCheckFlg, svcCheckItemCount, thresholdHWCpuUsage, thresholdHWMemoryUsage, thresholdHWFanMin, thresholdHWFanMax, thresholdHWAdcLogCount, thresholdL47SleepVSDay, svcClientIPAddress, svcVSIndex, svcVSName, svcVSIPAddress);
	}
	
	public Integer getSvcCheckFlg()
	{
		return svcCheckFlg;
	}

	public void setSvcCheckFlg(Integer svcCheckFlg)
	{
		this.svcCheckFlg = svcCheckFlg;
	}

	public Integer getHwCheckItemCount()
	{
		return hwCheckItemCount;
	}

	public void setHwCheckItemCount(Integer hwCheckItemCount)
	{
		this.hwCheckItemCount = hwCheckItemCount;
	}

	public Integer getL23CheckItemCount()
	{
		return l23CheckItemCount;
	}

	public void setL23CheckItemCount(Integer l23CheckItemCount)
	{
		this.l23CheckItemCount = l23CheckItemCount;
	}

	public Integer getL47CheckItemCount()
	{
		return l47CheckItemCount;
	}

	public void setL47CheckItemCount(Integer l47CheckItemCount)
	{
		this.l47CheckItemCount = l47CheckItemCount;
	}

	public Integer getSvcCheckItemCount()
	{
		return svcCheckItemCount;
	}

	public void setSvcCheckItemCount(Integer svcCheckItemCount)
	{
		this.svcCheckItemCount = svcCheckItemCount;
	}

	public ArrayList<OBDtoElement> getHwCheckItems()
	{
		return hwCheckItems;
	}

	public void setHwCheckItems(ArrayList<OBDtoElement> hwCheckItems)
	{
		this.hwCheckItems = hwCheckItems;
	}

	public ArrayList<OBDtoElement> getL23CheckItems()
	{
		return l23CheckItems;
	}

	public void setL23CheckItems(ArrayList<OBDtoElement> l23CheckItems)
	{
		this.l23CheckItems = l23CheckItems;
	}

	public ArrayList<OBDtoElement> getL47CheckItems()
	{
		return l47CheckItems;
	}

	public void setL47CheckItems(ArrayList<OBDtoElement> l47CheckItems)
	{
		this.l47CheckItems = l47CheckItems;
	}

	public ArrayList<OBDtoElement> getSvcCheckItems()
	{
		return svcCheckItems;
	}

	public void setSvcCheckItems(ArrayList<OBDtoElement> svcCheckItems)
	{
		this.svcCheckItems = svcCheckItems;
	}

	public String getSvcClientIPAddress()
	{
		return svcClientIPAddress;
	}
	public void setSvcClientIPAddress(String svcClientIPAddress)
	{
		this.svcClientIPAddress = svcClientIPAddress;
	}
	public String getSvcVSIndex()
	{
		return svcVSIndex;
	}
	public void setSvcVSIndex(String svcVSIndex)
	{
		this.svcVSIndex = svcVSIndex;
	}
	public String getSvcVSName()
	{
		return svcVSName;
	}
	public void setSvcVSName(String svcVSName)
	{
		this.svcVSName = svcVSName;
	}
	public String getSvcVSIPAddress()
	{
		return svcVSIPAddress;
	}
	public void setSvcVSIPAddress(String svcVSIPAddress)
	{
		this.svcVSIPAddress = svcVSIPAddress;
	}
	public Long getIndex()
	{
		return index;
	}
	public void setIndex(Long index)
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
	public Integer getThresholdHWCpuUsage()
	{
		return thresholdHWCpuUsage;
	}
	public void setThresholdHWCpuUsage(Integer thresholdHWCpuUsage)
	{
		this.thresholdHWCpuUsage = thresholdHWCpuUsage;
	}
	public Integer getThresholdHWMemoryUsage()
	{
		return thresholdHWMemoryUsage;
	}
	public void setThresholdHWMemoryUsage(Integer thresholdHWMemoryUsage)
	{
		this.thresholdHWMemoryUsage = thresholdHWMemoryUsage;
	}
	public Integer getThresholdHWFanMin()
	{
		return thresholdHWFanMin;
	}
	public void setThresholdHWFanMin(Integer thresholdHWFanMin)
	{
		this.thresholdHWFanMin = thresholdHWFanMin;
	}
	public Integer getThresholdHWFanMax()
	{
		return thresholdHWFanMax;
	}
	public void setThresholdHWFanMax(Integer thresholdHWFanMax)
	{
		this.thresholdHWFanMax = thresholdHWFanMax;
	}
	public Integer getThresholdHWAdcLogCount()
	{
		return thresholdHWAdcLogCount;
	}
	public void setThresholdHWAdcLogCount(Integer thresholdHWAdcLogCount)
	{
		this.thresholdHWAdcLogCount = thresholdHWAdcLogCount;
	}
	public Integer getThresholdL47SleepVSDay()
	{
		return thresholdL47SleepVSDay;
	}
	public void setThresholdL47SleepVSDay(Integer thresholdL47SleepVSDay)
	{
		this.thresholdL47SleepVSDay = thresholdL47SleepVSDay;
	}
	public Integer getAdcType()
	{
		return adcType;
	}

	public void setAdcType(Integer adcType)
	{
		this.adcType = adcType;
	}
}