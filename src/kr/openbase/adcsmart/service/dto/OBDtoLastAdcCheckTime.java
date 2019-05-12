package kr.openbase.adcsmart.service.dto;

import java.sql.Timestamp;

public class OBDtoLastAdcCheckTime
{
	public static final int CHECK_ID_PORTOPEN 		= 1;// 포트 오픈 테스트   
	public static final int CHECK_ID_LOGIN 			= 2;// 로그인 테스트  
	public static final int CHECK_ID_VERSION		= 3;// version  
	public static final int CHECK_ID_PORTREVERSE 	= 4;// 포트 오픈 테스트(역방향). alteon만 해당됨. 
	public static final int CHECK_ID_SNMP 			= 5;// snmp 수신 테스트 
	public static final int CHECK_ID_SYSLOG 		= 6;// syslog 수신 테스트. 
	
	private Integer checkID;
	private Timestamp checkTime;
	@Override
	public String toString()
	{
		return "OBLastAdcCheckTime [checkID=" + checkID + ", checkTime=" + checkTime + "]";
	}
	public Integer getCheckID()
	{
		return checkID;
	}
	public void setCheckID(Integer checkID)
	{
		this.checkID = checkID;
	}
	public Timestamp getCheckTime()
	{
		return checkTime;
	}
	public void setCheckTime(Timestamp checkTime)
	{
		this.checkTime = checkTime;
	}
}
