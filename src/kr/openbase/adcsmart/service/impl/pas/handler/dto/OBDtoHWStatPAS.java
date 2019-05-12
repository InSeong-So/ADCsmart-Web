package kr.openbase.adcsmart.service.impl.pas.handler.dto;

import java.util.Arrays;

public class OBDtoHWStatPAS
{
	public static final  int	POWER_COUNT		=	18;
	public static final  int	FAN_COUNT		=	5;
	private	int		pwStatusList[] ;// 최대 18개까지 제공됨.		
	private	int		fanStatus[] ;// 최대 5개까지 제공됨.		
	
	public OBDtoHWStatPAS()
	{
		pwStatusList = new int[POWER_COUNT];
		//-1로 초기화한다.
		for(int i=0;i<POWER_COUNT;i++)
		{
			pwStatusList[i]=-1;
		}
		fanStatus = new int[FAN_COUNT];
		//-1로 초기화한다.
		for(int i=0;i<FAN_COUNT;i++)
		{
			fanStatus[i]=-1;
		}
	}
	@Override
	public String toString()
	{
		return "OBDtoHWStatPAS [pwStatusList=" + Arrays.toString(pwStatusList) + ", fanStatus=" + Arrays.toString(fanStatus) + "]";
	}
	public int[] getFanStatus()
	{
		return fanStatus;
	}
	public void setFanStatus(int[] fanStatus)
	{
		this.fanStatus = fanStatus;
	}
	public int[] getPwStatusList()
	{
		return pwStatusList;
	}
	public void setPwStatusList(int[] pwStatusList)
	{
		this.pwStatusList = pwStatusList;
	}

	public int getFanStatus(int index)
	{
		if(index<0 || index>FAN_COUNT)
			return -1;
		return fanStatus[index];
	}
	public void setFanStatus(int index, int value)
	{
		if(index>=0 && index<FAN_COUNT)
			this.fanStatus[index] = value;
	}
	public int getPwStatusList(int index)
	{
		if(index<0 || index>POWER_COUNT)
			return -1;
		return pwStatusList[index];
	}
	public void setPwStatusList(int index, int value)
	{
		if(index>=0 && index<POWER_COUNT)
			this.pwStatusList[index] = value;
	}
}
