package kr.openbase.adcsmart.service.impl.dto;

import java.sql.Timestamp;

public class OBDtoCpuMemStatus
{
	private Integer adcIndex;
	private Timestamp occurTime;
	private Integer cpu1Usage=-1;//mp
	private Integer cpu2Usage=-1;//sp1의미.
	private Integer cpu3Usage=-1;
	private Integer cpu4Usage=-1;
	private Integer cpu5Usage=-1;
	private Integer cpu6Usage=-1;
	private Integer cpu7Usage=-1;
	private Integer cpu8Usage=-1;
	private Integer cpu9Usage=-1;
	private Integer cpu10Usage=-1;
	private Integer cpu11Usage=-1;
	private Integer cpu12Usage=-1;
	private Integer cpu13Usage=-1;
	private Integer cpu14Usage=-1;
	private Integer cpu15Usage=-1;
	private Integer cpu16Usage=-1;
	private Integer memUsage=-1;
	private Integer cpu17Usage=-1;
	private Integer cpu18Usage=-1;
	private Integer cpu19Usage=-1;
	private Integer cpu20Usage=-1;
	private Integer cpu21Usage=-1;
	private Integer cpu22Usage=-1;
	private Integer cpu23Usage=-1;
	private Integer cpu24Usage=-1;
	private Integer cpu25Usage=-1;
	private Integer cpu26Usage=-1;
	private Integer cpu27Usage=-1;
	private Integer cpu28Usage=-1;
	private Integer cpu29Usage=-1;
	private Integer cpu30Usage=-1;
	private Integer cpu31Usage=-1;
	private Integer cpu32Usage=-1;
	private Integer cpu2Conns=-1;//sp1 의 Connection 의미.
	private Integer cpu3Conns=-1;
	private Integer cpu4Conns=-1;
	private Integer cpu5Conns=-1;
	private Integer cpu6Conns=-1;
	private Integer cpu7Conns=-1;
	private Integer cpu8Conns=-1;
	private Integer cpu9Conns=-1;
	private Integer cpu10Conns=-1;
	private Integer cpu11Conns=-1;
	private Integer cpu12Conns=-1;
	private Integer cpu13Conns=-1;
	private Integer cpu14Conns=-1;
	private Integer cpu15Conns=-1;
	private Integer cpu16Conns=-1;
	private Integer cpu17Conns=-1;
	private Integer cpu18Conns=-1;
	private Integer cpu19Conns=-1;
	private Integer cpu20Conns=-1;
	private Integer cpu21Conns=-1;
	private Integer cpu22Conns=-1;
	private Integer cpu23Conns=-1;
	private Integer cpu24Conns=-1;
	private Integer cpu25Conns=-1;
	private Integer cpu26Conns=-1;
	private Integer cpu27Conns=-1;
	private Integer cpu28Conns=-1;
	private Integer cpu29Conns=-1;
	private Integer cpu30Conns=-1;
	private Integer cpu31Conns=-1;
	private Integer cpu32Conns=-1;
	private Integer spSessionMax=-1;
	@Override
	public String toString()
	{
		return "OBDtoCpuMemStatus [adcIndex=" + adcIndex + ", occurTime="
				+ occurTime + ", cpu1Usage=" + cpu1Usage + ", cpu2Usage="
				+ cpu2Usage + ", cpu3Usage=" + cpu3Usage + ", cpu4Usage="
				+ cpu4Usage + ", cpu5Usage=" + cpu5Usage + ", cpu6Usage="
				+ cpu6Usage + ", cpu7Usage=" + cpu7Usage + ", cpu8Usage="
				+ cpu8Usage + ", cpu9Usage=" + cpu9Usage + ", cpu10Usage="
				+ cpu10Usage + ", cpu11Usage=" + cpu11Usage + ", cpu12Usage="
				+ cpu12Usage + ", cpu13Usage=" + cpu13Usage + ", cpu14Usage="
				+ cpu14Usage + ", cpu15Usage=" + cpu15Usage + ", cpu16Usage="
				+ cpu16Usage + ", memUsage=" + memUsage + ", cpu17Usage="
				+ cpu17Usage + ", cpu18Usage=" + cpu18Usage + ", cpu19Usage="
				+ cpu19Usage + ", cpu20Usage=" + cpu20Usage + ", cpu21Usage="
				+ cpu21Usage + ", cpu22Usage=" + cpu22Usage + ", cpu23Usage="
				+ cpu23Usage + ", cpu24Usage=" + cpu24Usage + ", cpu25Usage="
				+ cpu25Usage + ", cpu26Usage=" + cpu26Usage + ", cpu27Usage="
				+ cpu27Usage + ", cpu28Usage=" + cpu28Usage + ", cpu29Usage="
				+ cpu29Usage + ", cpu30Usage=" + cpu30Usage + ", cpu31Usage="
				+ cpu31Usage + ", cpu32Usage=" + cpu32Usage + ", cpu2Conns="
				+ cpu2Conns + ", cpu3Conns=" + cpu3Conns + ", cpu4Conns="
				+ cpu4Conns + ", cpu5Conns=" + cpu5Conns + ", cpu6Conns="
				+ cpu6Conns + ", cpu7Conns=" + cpu7Conns + ", cpu8Conns="
				+ cpu8Conns + ", cpu9Conns=" + cpu9Conns + ", cpu10Conns="
				+ cpu10Conns + ", cpu11Conns=" + cpu11Conns + ", cpu12Conns="
				+ cpu12Conns + ", cpu13Conns=" + cpu13Conns + ", cpu14Conns="
				+ cpu14Conns + ", cpu15Conns=" + cpu15Conns + ", cpu16Conns="
				+ cpu16Conns + ", cpu17Conns=" + cpu17Conns + ", cpu18Conns="
				+ cpu18Conns + ", cpu19Conns=" + cpu19Conns + ", cpu20Conns="
				+ cpu20Conns + ", cpu21Conns=" + cpu21Conns + ", cpu22Conns="
				+ cpu22Conns + ", cpu23Conns=" + cpu23Conns + ", cpu24Conns="
				+ cpu24Conns + ", cpu25Conns=" + cpu25Conns + ", cpu26Conns="
				+ cpu26Conns + ", cpu27Conns=" + cpu27Conns + ", cpu28Conns="
				+ cpu28Conns + ", cpu29Conns=" + cpu29Conns + ", cpu30Conns="
				+ cpu30Conns + ", cpu31Conns=" + cpu31Conns + ", cpu32Conns="
				+ cpu32Conns + ", spSessionMax=" + spSessionMax + "]";
	}
	public Integer getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public Timestamp getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Timestamp occurTime)
	{
		this.occurTime = occurTime;
	}
	public Integer getCpu1Usage()
	{
		return cpu1Usage;
	}
	public void setCpu1Usage(Integer cpu1Usage)
	{
		this.cpu1Usage = cpu1Usage;
	}
	public Integer getCpu2Usage()
	{
		return cpu2Usage;
	}
	public void setCpu2Usage(Integer cpu2Usage)
	{
		this.cpu2Usage = cpu2Usage;
	}
	public Integer getCpu3Usage()
	{
		return cpu3Usage;
	}
	public void setCpu3Usage(Integer cpu3Usage)
	{
		this.cpu3Usage = cpu3Usage;
	}
	public Integer getCpu4Usage()
	{
		return cpu4Usage;
	}
	public void setCpu4Usage(Integer cpu4Usage)
	{
		this.cpu4Usage = cpu4Usage;
	}
	public Integer getCpu5Usage()
	{
		return cpu5Usage;
	}
	public void setCpu5Usage(Integer cpu5Usage)
	{
		this.cpu5Usage = cpu5Usage;
	}
	public Integer getCpu6Usage()
	{
		return cpu6Usage;
	}
	public void setCpu6Usage(Integer cpu6Usage)
	{
		this.cpu6Usage = cpu6Usage;
	}
	public Integer getCpu7Usage()
	{
		return cpu7Usage;
	}
	public void setCpu7Usage(Integer cpu7Usage)
	{
		this.cpu7Usage = cpu7Usage;
	}
	public Integer getCpu8Usage()
	{
		return cpu8Usage;
	}
	public void setCpu8Usage(Integer cpu8Usage)
	{
		this.cpu8Usage = cpu8Usage;
	}
	public Integer getCpu9Usage()
	{
		return cpu9Usage;
	}
	public void setCpu9Usage(Integer cpu9Usage)
	{
		this.cpu9Usage = cpu9Usage;
	}
	public Integer getCpu10Usage()
	{
		return cpu10Usage;
	}
	public void setCpu10Usage(Integer cpu10Usage)
	{
		this.cpu10Usage = cpu10Usage;
	}
	public Integer getCpu11Usage()
	{
		return cpu11Usage;
	}
	public void setCpu11Usage(Integer cpu11Usage)
	{
		this.cpu11Usage = cpu11Usage;
	}
	public Integer getCpu12Usage()
	{
		return cpu12Usage;
	}
	public void setCpu12Usage(Integer cpu12Usage)
	{
		this.cpu12Usage = cpu12Usage;
	}
	public Integer getCpu13Usage()
	{
		return cpu13Usage;
	}
	public void setCpu13Usage(Integer cpu13Usage)
	{
		this.cpu13Usage = cpu13Usage;
	}
	public Integer getCpu14Usage()
	{
		return cpu14Usage;
	}
	public void setCpu14Usage(Integer cpu14Usage)
	{
		this.cpu14Usage = cpu14Usage;
	}
	public Integer getCpu15Usage()
	{
		return cpu15Usage;
	}
	public void setCpu15Usage(Integer cpu15Usage)
	{
		this.cpu15Usage = cpu15Usage;
	}
	public Integer getCpu16Usage()
	{
		return cpu16Usage;
	}
	public void setCpu16Usage(Integer cpu16Usage)
	{
		this.cpu16Usage = cpu16Usage;
	}
	public Integer getMemUsage()
	{
		return memUsage;
	}
	public void setMemUsage(Integer memUsage)
	{
		this.memUsage = memUsage;
	}
	public Integer getCpu17Usage()
	{
		return cpu17Usage;
	}
	public void setCpu17Usage(Integer cpu17Usage)
	{
		this.cpu17Usage = cpu17Usage;
	}
	public Integer getCpu18Usage()
	{
		return cpu18Usage;
	}
	public void setCpu18Usage(Integer cpu18Usage)
	{
		this.cpu18Usage = cpu18Usage;
	}
	public Integer getCpu19Usage()
	{
		return cpu19Usage;
	}
	public void setCpu19Usage(Integer cpu19Usage)
	{
		this.cpu19Usage = cpu19Usage;
	}
	public Integer getCpu20Usage()
	{
		return cpu20Usage;
	}
	public void setCpu20Usage(Integer cpu20Usage)
	{
		this.cpu20Usage = cpu20Usage;
	}
	public Integer getCpu21Usage()
	{
		return cpu21Usage;
	}
	public void setCpu21Usage(Integer cpu21Usage)
	{
		this.cpu21Usage = cpu21Usage;
	}
	public Integer getCpu22Usage()
	{
		return cpu22Usage;
	}
	public void setCpu22Usage(Integer cpu22Usage)
	{
		this.cpu22Usage = cpu22Usage;
	}
	public Integer getCpu23Usage()
	{
		return cpu23Usage;
	}
	public void setCpu23Usage(Integer cpu23Usage)
	{
		this.cpu23Usage = cpu23Usage;
	}
	public Integer getCpu24Usage()
	{
		return cpu24Usage;
	}
	public void setCpu24Usage(Integer cpu24Usage)
	{
		this.cpu24Usage = cpu24Usage;
	}
	public Integer getCpu25Usage()
	{
		return cpu25Usage;
	}
	public void setCpu25Usage(Integer cpu25Usage)
	{
		this.cpu25Usage = cpu25Usage;
	}
	public Integer getCpu26Usage()
	{
		return cpu26Usage;
	}
	public void setCpu26Usage(Integer cpu26Usage)
	{
		this.cpu26Usage = cpu26Usage;
	}
	public Integer getCpu27Usage()
	{
		return cpu27Usage;
	}
	public void setCpu27Usage(Integer cpu27Usage)
	{
		this.cpu27Usage = cpu27Usage;
	}
	public Integer getCpu28Usage()
	{
		return cpu28Usage;
	}
	public void setCpu28Usage(Integer cpu28Usage)
	{
		this.cpu28Usage = cpu28Usage;
	}
	public Integer getCpu29Usage()
	{
		return cpu29Usage;
	}
	public void setCpu29Usage(Integer cpu29Usage)
	{
		this.cpu29Usage = cpu29Usage;
	}
	public Integer getCpu30Usage()
	{
		return cpu30Usage;
	}
	public void setCpu30Usage(Integer cpu30Usage)
	{
		this.cpu30Usage = cpu30Usage;
	}
	public Integer getCpu31Usage()
	{
		return cpu31Usage;
	}
	public void setCpu31Usage(Integer cpu31Usage)
	{
		this.cpu31Usage = cpu31Usage;
	}
	public Integer getCpu32Usage()
	{
		return cpu32Usage;
	}
	public void setCpu32Usage(Integer cpu32Usage)
	{
		this.cpu32Usage = cpu32Usage;
	}
	public Integer getCpu2Conns()
	{
		return cpu2Conns;
	}
	public void setCpu2Conns(Integer cpu2Conns)
	{
		this.cpu2Conns = cpu2Conns;
	}
	public Integer getCpu3Conns()
	{
		return cpu3Conns;
	}
	public void setCpu3Conns(Integer cpu3Conns)
	{
		this.cpu3Conns = cpu3Conns;
	}
	public Integer getCpu4Conns()
	{
		return cpu4Conns;
	}
	public void setCpu4Conns(Integer cpu4Conns)
	{
		this.cpu4Conns = cpu4Conns;
	}
	public Integer getCpu5Conns()
	{
		return cpu5Conns;
	}
	public void setCpu5Conns(Integer cpu5Conns)
	{
		this.cpu5Conns = cpu5Conns;
	}
	public Integer getCpu6Conns()
	{
		return cpu6Conns;
	}
	public void setCpu6Conns(Integer cpu6Conns)
	{
		this.cpu6Conns = cpu6Conns;
	}
	public Integer getCpu7Conns()
	{
		return cpu7Conns;
	}
	public void setCpu7Conns(Integer cpu7Conns)
	{
		this.cpu7Conns = cpu7Conns;
	}
	public Integer getCpu8Conns()
	{
		return cpu8Conns;
	}
	public void setCpu8Conns(Integer cpu8Conns)
	{
		this.cpu8Conns = cpu8Conns;
	}
	public Integer getCpu9Conns()
	{
		return cpu9Conns;
	}
	public void setCpu9Conns(Integer cpu9Conns)
	{
		this.cpu9Conns = cpu9Conns;
	}
	public Integer getCpu10Conns()
	{
		return cpu10Conns;
	}
	public void setCpu10Conns(Integer cpu10Conns)
	{
		this.cpu10Conns = cpu10Conns;
	}
	public Integer getCpu11Conns()
	{
		return cpu11Conns;
	}
	public void setCpu11Conns(Integer cpu11Conns)
	{
		this.cpu11Conns = cpu11Conns;
	}
	public Integer getCpu12Conns()
	{
		return cpu12Conns;
	}
	public void setCpu12Conns(Integer cpu12Conns)
	{
		this.cpu12Conns = cpu12Conns;
	}
	public Integer getCpu13Conns()
	{
		return cpu13Conns;
	}
	public void setCpu13Conns(Integer cpu13Conns)
	{
		this.cpu13Conns = cpu13Conns;
	}
	public Integer getCpu14Conns()
	{
		return cpu14Conns;
	}
	public void setCpu14Conns(Integer cpu14Conns)
	{
		this.cpu14Conns = cpu14Conns;
	}
	public Integer getCpu15Conns()
	{
		return cpu15Conns;
	}
	public void setCpu15Conns(Integer cpu15Conns)
	{
		this.cpu15Conns = cpu15Conns;
	}
	public Integer getCpu16Conns()
	{
		return cpu16Conns;
	}
	public void setCpu16Conns(Integer cpu16Conns)
	{
		this.cpu16Conns = cpu16Conns;
	}
	public Integer getCpu17Conns()
	{
		return cpu17Conns;
	}
	public void setCpu17Conns(Integer cpu17Conns)
	{
		this.cpu17Conns = cpu17Conns;
	}
	public Integer getCpu18Conns()
	{
		return cpu18Conns;
	}
	public void setCpu18Conns(Integer cpu18Conns)
	{
		this.cpu18Conns = cpu18Conns;
	}
	public Integer getCpu19Conns()
	{
		return cpu19Conns;
	}
	public void setCpu19Conns(Integer cpu19Conns)
	{
		this.cpu19Conns = cpu19Conns;
	}
	public Integer getCpu20Conns()
	{
		return cpu20Conns;
	}
	public void setCpu20Conns(Integer cpu20Conns)
	{
		this.cpu20Conns = cpu20Conns;
	}
	public Integer getCpu21Conns()
	{
		return cpu21Conns;
	}
	public void setCpu21Conns(Integer cpu21Conns)
	{
		this.cpu21Conns = cpu21Conns;
	}
	public Integer getCpu22Conns()
	{
		return cpu22Conns;
	}
	public void setCpu22Conns(Integer cpu22Conns)
	{
		this.cpu22Conns = cpu22Conns;
	}
	public Integer getCpu23Conns()
	{
		return cpu23Conns;
	}
	public void setCpu23Conns(Integer cpu23Conns)
	{
		this.cpu23Conns = cpu23Conns;
	}
	public Integer getCpu24Conns()
	{
		return cpu24Conns;
	}
	public void setCpu24Conns(Integer cpu24Conns)
	{
		this.cpu24Conns = cpu24Conns;
	}
	public Integer getCpu25Conns()
	{
		return cpu25Conns;
	}
	public void setCpu25Conns(Integer cpu25Conns)
	{
		this.cpu25Conns = cpu25Conns;
	}
	public Integer getCpu26Conns()
	{
		return cpu26Conns;
	}
	public void setCpu26Conns(Integer cpu26Conns)
	{
		this.cpu26Conns = cpu26Conns;
	}
	public Integer getCpu27Conns()
	{
		return cpu27Conns;
	}
	public void setCpu27Conns(Integer cpu27Conns)
	{
		this.cpu27Conns = cpu27Conns;
	}
	public Integer getCpu28Conns()
	{
		return cpu28Conns;
	}
	public void setCpu28Conns(Integer cpu28Conns)
	{
		this.cpu28Conns = cpu28Conns;
	}
	public Integer getCpu29Conns()
	{
		return cpu29Conns;
	}
	public void setCpu29Conns(Integer cpu29Conns)
	{
		this.cpu29Conns = cpu29Conns;
	}
	public Integer getCpu30Conns()
	{
		return cpu30Conns;
	}
	public void setCpu30Conns(Integer cpu30Conns)
	{
		this.cpu30Conns = cpu30Conns;
	}
	public Integer getCpu31Conns()
	{
		return cpu31Conns;
	}
	public void setCpu31Conns(Integer cpu31Conns)
	{
		this.cpu31Conns = cpu31Conns;
	}
	public Integer getCpu32Conns()
	{
		return cpu32Conns;
	}
	public void setCpu32Conns(Integer cpu32Conns)
	{
		this.cpu32Conns = cpu32Conns;
	}
	public Integer getSpSessionMax()
	{
		return spSessionMax;
	}
	public void setSpSessionMax(Integer spSessionMax)
	{
		this.spSessionMax = spSessionMax;
	}	
}
