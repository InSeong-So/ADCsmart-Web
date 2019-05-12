package kr.openbase.adcsmart.service.utility;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAlarmConfig;
import kr.openbase.adcsmart.service.dto.OBDtoReadConfig;
import kr.openbase.adcsmart.service.impl.OBAlarmImpl;

public class OBNmsConfig
{
	final static String LOG_SENDER = "ADCSMART";

//	private String syslogServerIp;
	private String LogSenderName;
	private ArrayList<OBDtoAlarmConfig> alarmList;
	private OBDtoReadConfig readConfigData;

	public OBNmsConfig() //constructor
	{
		this.setAlarmList(null);
		this.setLogSenderName(OBNmsConfig.LOG_SENDER);
//		this.setSyslogServerIp(null);
		this.setReadConfigData(null);
	}
	public OBDtoReadConfig getReadConfigData()
    {
        return readConfigData;
    }
    public void setReadConfigData(OBDtoReadConfig readConfigData)
    {
        this.readConfigData = readConfigData;
    }
//    public String getSyslogServerIp()
//	{
//		return syslogServerIp;
//	}
//	public void setSyslogServerIp(String syslogServerIp)
//	{
//		this.syslogServerIp = syslogServerIp;
//	}
	public ArrayList<OBDtoAlarmConfig> getAlarmList()
	{
		return alarmList;
	}
	public void setAlarmList(ArrayList<OBDtoAlarmConfig> alarmList)
	{
		this.alarmList = alarmList;
	}
	public String getLogSenderName()
	{
		return LogSenderName;
	}
	public void setLogSenderName(String logSenderName)
	{
		LogSenderName = logSenderName;
	}
//	public void readSyslog() //config read에 실패해도 기록만 남기고 exception을 내지 않는다.
//	{
//		OBAlarmImpl tempAlarm = new OBAlarmImpl();
//		try
//		{
//			String tempStr = tempAlarm.getSyslogServerIp();
//			if(tempStr==null) //주소가 없으면 기능을 무효화 해야 하기 때문에 다시 server ip를 null로 mark 해 준다.
//			{
//				OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "syslog ip = null");
//				this.setSyslogServerIp(null);
//			}
//			else
//			{
//				this.setSyslogServerIp(tempStr);
//				this.setLogSenderName(OBNmsConfig.LOG_SENDER);
//				this.setAlarmList(tempAlarm.getAlarmConfigurationAll());
//			}
//		}
//		catch(OBException e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "Config read error. alarm(syslogging) will not work.");
//		}
//	}
	// snmpTrap , syslog config
	public void readConfig() //config read 실패해도 기록만 남기고 exception을 내지 않는다.
	{
		OBAlarmImpl tempAlarm = new OBAlarmImpl();
		try
		{
//		    String tempStr = tempAlarm.getSyslogServerIp();
			OBDtoReadConfig tempConfigStr = tempAlarm.getReadConfig();
			if(tempConfigStr==null) //주소가 없으면 기능을 무효화 해야 하기 때문에 다시 server ip를 null로 mark 해 준다.
			{
				OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "syslog ip = null");
//				this.setSyslogServerIp(null);
				this.setReadConfigData(tempConfigStr);
			}
			else
			{
//				this.setSyslogServerIp(tempStr);
				this.setLogSenderName(OBNmsConfig.LOG_SENDER);
				this.setReadConfigData(tempConfigStr);
			}	
		}
		catch(OBException e)
		{
			OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "Config read error. alarm(syslogging) will not work.");
		}
	}
	   
	public void readPopup() //config read에 실패해도 기록만 남기고 exception을 내지 않는다.
	{
		OBAlarmImpl tempAlarm = new OBAlarmImpl();
		try
		{
//			this.setSyslogServerIp(null);
			this.setLogSenderName(OBNmsConfig.LOG_SENDER);
			this.setAlarmList(tempAlarm.getAlarmConfigurationAll());
			this.getReadConfigData().setSyslogServer(null);
		}
		catch(OBException e)
		{
			OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "Config read error. alarm(popup) will not work.");
		}
	}
	public void clear()
	{
		this.setAlarmList(null);
		this.setLogSenderName(null);
//		this.setSyslogServerIp(null);
		this.getReadConfigData().setSyslogServer(null);
	}
	public boolean isValid4Syslog()
	{
//		if(this.getSyslogServerIp() == null)
	    if(this.getReadConfigData().getSyslogServer() == null)
		{
			return false;
		}
		else
		{
			if(this.getAlarmList().size()==0)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
	}
	public boolean isValid4Popup()
	{
		if(this.getAlarmList()==null || this.getAlarmList().size()==0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}