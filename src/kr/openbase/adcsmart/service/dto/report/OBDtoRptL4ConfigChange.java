package kr.openbase.adcsmart.service.dto.report;

import java.util.Date;

public class OBDtoRptL4ConfigChange
{
	private static final int NAME_MIN_LENGTH = 20;
	private static final int USER_NAME_MIN_LENGTH = 20;
	private static final int DOT_LENGTH = 4;
	
	private Date 	occurTime;
	private String 	adcName;
	private String 	adcIPAddress;
	private String 	vsName;
	private String 	vsIPAddress;
	private String 	contents;
	private String 	userID;
	
	public static String toUserID(String userID)
	{
		String ret = "";
		if(userID.length()>USER_NAME_MIN_LENGTH)
		{
			ret = userID.substring(0,(USER_NAME_MIN_LENGTH-DOT_LENGTH))+" ...";
		}
		else
		{
			ret = userID;
		}
		return ret;
	}
	
	public static String toVsName(String vsName)
	{
		String ret = "";
		
		if(vsName.length()>NAME_MIN_LENGTH)
		{
			ret = vsName.substring(0, (NAME_MIN_LENGTH-DOT_LENGTH))+" ...";
		}
		else
		{
			ret = vsName;
		}
		return ret;
	}
	
	public static String toAdcName(String adcName)
	{
		String ret = "";
		if(adcName.length()>NAME_MIN_LENGTH)
		{
			ret = adcName.substring(0,(NAME_MIN_LENGTH-DOT_LENGTH))+" ...";
		}
		else
		{
			ret = adcName;
		}
		return ret;
	}
	
	@Override
	public String toString()
	{
		return "OBDtoRptConfigChange [occurTime=" + occurTime + ", adcName="
				+ adcName + ", adcIPAddress=" + adcIPAddress + ", vsName="
				+ vsName + ", vsIPAddress=" + vsIPAddress + ", contents="
				+ contents + ", userID=" + userID + "]";
	}
	public Date getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Date occurTime)
	{
		this.occurTime = occurTime;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public String getAdcIPAddress()
	{
		return adcIPAddress;
	}
	public void setAdcIPAddress(String adcIPAddress)
	{
		this.adcIPAddress = adcIPAddress;
	}
	public String getVsName()
	{
		return vsName;
	}
	public void setVsName(String vsName)
	{
		this.vsName = vsName;
	}
	public String getVsIPAddress()
	{
		return vsIPAddress;
	}
	public void setVsIPAddress(String vsIPAddress)
	{
		this.vsIPAddress = vsIPAddress;
	}
	public String getContents()
	{
		return contents;
	}
	public void setContents(String contents)
	{
		this.contents = contents;
	}
	public String getUserID()
	{
		return userID;
	}
	public void setUserID(String userID)
	{
		this.userID = userID;
	}
		
}
