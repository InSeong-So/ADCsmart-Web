package kr.openbase.adcsmart.service.dto;

import java.util.Date;

public class OBDtoBackupSchedule
{
    public static final int SCHEDULE_DAILY = 1;
    public static final int SCHEDULE_WEEKLY = 2;
    public static final int SCHEDULE_MONTHLY = 3;
    public static final int SCHEDULE_ONCE = 4;
    
    public static final int LOG_DELETE_FALSE = 0;
    public static final int LOG_DELETE_TRUE = 1;

    private long index;
    private int state;
    private Date occurTime;
    private String accntId;
    private int accntIndex;
    private int type;
    private boolean logDetete;
    private int scheduleType;
    private Integer scheduleMinute;
    private Integer scheduleHour;
    private Integer scheduleDay;
    private Integer scheduleMonth;
    private Integer scheduleDayweek;

    public long getIndex()
    {
        return index;
    }

    public void setIndex(long index)
    {
        this.index = index;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public Date getOccurTime()
    {
        return occurTime;
    }

    public void setOccurTime(Date occurTime)
    {
        this.occurTime = occurTime;
    }

    public String getAccntId()
    {
        return accntId;
    }

    public void setAccntId(String accntId)
    {
        this.accntId = accntId;
    }

    public int getAccntIndex()
    {
        return accntIndex;
    }

    public void setAccntIndex(int accntIndex)
    {
        this.accntIndex = accntIndex;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public boolean isLogDetete()
    {
        return logDetete;
    }

    public void setLogDetete(boolean logDetete)
    {
        this.logDetete = logDetete;
    }

    public int getScheduleType()
    {
        return scheduleType;
    }

    public void setScheduleType(int scheduleType)
    {
        this.scheduleType = scheduleType;
    }

    public Integer getScheduleMinute()
    {
        return scheduleMinute;
    }

    public void setScheduleMinute(Integer scheduleMinute)
    {
        this.scheduleMinute = scheduleMinute;
    }

    public Integer getScheduleHour()
    {
        return scheduleHour;
    }

    public void setScheduleHour(Integer scheduleHour)
    {
        this.scheduleHour = scheduleHour;
    }

    public Integer getScheduleDay()
    {
        return scheduleDay;
    }

    public void setScheduleDay(Integer scheduleDay)
    {
        this.scheduleDay = scheduleDay;
    }

    public Integer getScheduleMonth()
    {
        return scheduleMonth;
    }

    public void setScheduleMonth(Integer scheduleMonth)
    {
        this.scheduleMonth = scheduleMonth;
    }

    public Integer getScheduleDayweek()
    {
        return scheduleDayweek;
    }

    public void setScheduleDayweek(Integer scheduleDayweek)
    {
        this.scheduleDayweek = scheduleDayweek;
    }

    @Override
    public String toString()
    {
        return "OBDtoBackupSchedule [index=" + index + ", state=" + state
                + ", occurTime=" + occurTime + ", accntId=" + accntId
                + ", accntIndex=" + accntIndex + ", type=" + type
                + ", logDetete=" + logDetete + ", scheduleType=" + scheduleType
                + ", scheduleMinute=" + scheduleMinute + ", scheduleHour="
                + scheduleHour + ", scheduleDay=" + scheduleDay
                + ", scheduleMonth=" + scheduleMonth + ", scheduleDayweek="
                + scheduleDayweek + "]";
    }
}