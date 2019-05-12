package kr.openbase.adcsmart.web.facade.dto;

import kr.openbase.adcsmart.service.dto.OBDtoAlarmAction;
import kr.openbase.adcsmart.service.utility.OBDefine;

public class AlarmActionDto 
{	
	private Integer enable =0;// 사용유무. 0: 사용. 1: 사용하지 않음. 
	private Long threshold	=0L;// 임계치 정보.
	private Integer intervalValue = 0;
	
	private String intervalUnit = OBDefine.TIME_UNIT_MINUTE;
	// public static final String TIME_UNIT_MINUTE = "M";
	// public static final String TIME_UNIT_HOUR = "H";
	// public static final String TIME_UNIT_DAY = "D";
	
	//check : 1, uncheck : 0
	private Integer syslog;	
	private Integer email = 0;
	private Integer sms = 0;	
	private Integer snmptrap = 0;

	
	public AlarmActionDto(Integer syslog, Integer popup, Integer email, Integer sms, Integer snmptrap, Integer enable)
	{
		this.enable = enable;
		this.intervalValue = 0;
		this.intervalUnit = OBDefine.TIME_UNIT_MINUTE;
		this.syslog = syslog;		
		this.email = email;
		this.snmptrap = snmptrap;
		this.sms = sms;		
	}
	
	public AlarmActionDto()
	{		
	}	
	
	public void setCustomAlarmAction(OBDtoAlarmAction alarmActionSvc)
	{		
		this.enable = alarmActionSvc.getEnable();
		this.intervalValue = alarmActionSvc.getIntervalValue();
		this.intervalUnit = alarmActionSvc.getIntervalUnit();
		this.syslog = alarmActionSvc.getSyslog();		
		this.email = alarmActionSvc.getEmail();
		this.sms = alarmActionSvc.getSms();			
		this.snmptrap = alarmActionSvc.getSnmptrap();
	}

	public Integer getEnable()
	{
		return enable;
	}

	public void setEnable(Integer enable)
	{
		this.enable = enable;
	}

	public Long getThreshold()
	{
		return threshold;
	}

	public void setThreshold(Long threshold)
	{
		this.threshold = threshold;
	}

	public Integer getIntervalValue()
	{
		return intervalValue;
	}

	public void setIntervalValue(Integer intervalValue)
	{
		this.intervalValue = intervalValue;
	}

	public String getIntervalUnit()
	{
		return intervalUnit;
	}

	public void setIntervalUnit(String intervalUnit)
	{
		this.intervalUnit = intervalUnit;
	}

	public int getSyslog()
	{
		return syslog;
	}

	public void setSyslog(int syslog)
	{
		this.syslog = syslog;
	}

	public int getEmail()
	{
		return email;
	}

	public void setEmail(int email)
	{
		this.email = email;
	}

	public int getSms()
	{
		return sms;
	}

	public void setSms(int sms)
	{
		this.sms = sms;
	}
	public Integer getSnmptrap()
    {
        return snmptrap;
    }

    public void setSnmptrap(int snmptrap)
    {
        this.snmptrap = snmptrap;
    }

    @Override
    public String toString()
    {
        return "AlarmActionDto [enable=" + enable + ", threshold=" + threshold + ", intervalValue=" + intervalValue + ", intervalUnit=" + intervalUnit + ", syslog=" + syslog + ", email=" + email + ", sms=" + sms + ", snmptrap=" + snmptrap + "]";
    }
}