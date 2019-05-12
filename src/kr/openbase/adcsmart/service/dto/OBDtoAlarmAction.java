package kr.openbase.adcsmart.service.dto;

import kr.openbase.adcsmart.service.utility.OBDefine;

public class OBDtoAlarmAction
{
	private Integer enable =0;// 사용유무. 0: 사용. 1: 사용하지 않음. 
	private Integer syslog	=0;
	private Integer email	=0;
	private Integer sms		=0;
	private Integer snmptrap =0;
	private Long threshold	=0L;// 임계치 정보
	private Integer intervalValue = 0;
	private String  intervalUnit = OBDefine.TIME_UNIT_MINUTE;

	public OBDtoAlarmAction(Integer enable, Integer syslog, Integer email, Integer sms, Integer snmptrap, Long threshold, Integer intervalValue, String intervalUnit)
	{
		this.enable = enable;
		this.syslog = syslog;
		this.email = email;
		this.sms = sms;
		this.snmptrap = snmptrap;
		this.threshold = threshold;
		this.intervalValue = intervalValue;
		this.intervalUnit = intervalUnit;
	}

	public OBDtoAlarmAction()
	{
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
	public Integer getSyslog()
	{
		return syslog;
	}
	public void setSyslog(Integer syslog)
	{
		this.syslog = syslog;
	}
	public Integer getEmail()
	{
		return email;
	}
	public void setEmail(Integer email)
	{
		this.email = email;
	}
	public Integer getSms()
	{
		return sms;
	}
	public void setSms(Integer sms)
	{
		this.sms = sms;
	}
	public Integer getSnmptrap()
    {
        return snmptrap;
    }
    public void setSnmptrap(Integer snmptrap)
    {
        this.snmptrap = snmptrap;
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
	@Override
    public String toString()
    {
        return "OBDtoAlarmAction [enable=" + enable + ", syslog=" + syslog + ", email=" + email + ", sms=" + sms + ", snmptrap=" + snmptrap + ", threshold=" + threshold + ", intervalValue=" + intervalValue + ", intervalUnit=" + intervalUnit + "]";
    }	
}