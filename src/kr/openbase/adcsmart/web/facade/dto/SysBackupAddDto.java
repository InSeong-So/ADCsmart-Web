package kr.openbase.adcsmart.web.facade.dto;

import java.util.Calendar;
import java.util.Date;

import kr.openbase.adcsmart.service.dto.OBDtoBackupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoBackupSchedule;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

public class SysBackupAddDto
{
	private String fileName;
	private String description;
	private String target;
	private Boolean deletesLogs;
	
	private int scheduleType;
	private int scheduleMinute;
	private int scheduleHour;
	private int scheduleDay;
	private int scheduleMonth;
	private int scheduleDayweek;
	private Date scheduleDate;
	
	public static OBDtoBackupInfo toOBDtoBackupInfo(SysBackupAddDto backupAdd, int accountIndex)
	{
		OBDtoBackupInfo backupFromSvc = new OBDtoBackupInfo();
		backupFromSvc.setOccurTime(new Date());
		backupFromSvc.setAccntIndex(accountIndex);
		backupFromSvc.setComments(backupAdd.getDescription());
		backupFromSvc.setFileName(backupAdd.getFileName());
		backupFromSvc.setLogDelete(backupAdd.getDeletesLogs() == null ? false : backupAdd.getDeletesLogs());

		if(backupAdd.getTarget().equals(OBDefineWeb.MSG_DEFINEWEB_BAK_TGT_ALL))
			backupFromSvc.setType(OBDefineWeb.BACKUP_TYPE_CFG_LOG);
		else if(backupAdd.getTarget().equals(OBDefineWeb.MSG_DEFINEWEB_BAK_TGT_ADCSMARTSETTINGS))
			backupFromSvc.setType(OBDefineWeb.BACKUP_TYPE_CFG);
		else if(backupAdd.getTarget().equals(OBDefineWeb.MSG_DEFINEWEB_BAK_TGT_ADCSMARTLOGS))
			backupFromSvc.setType(OBDefineWeb.BACKUP_TYPE_LOG);
		else if(backupAdd.getTarget().equals(OBDefineWeb.MSG_DEFINEWEB_BAK_TGT_ADCSETTINGS))
			backupFromSvc.setType(OBDefineWeb.BACKUP_TYPE_ADC_CFG);
		return backupFromSvc;
	}

	public static OBDtoBackupSchedule toOBDtoBackupSchedule(SysBackupAddDto backupAdd, int accountIndex)
	{
	    OBDtoBackupSchedule backupSchedule = new OBDtoBackupSchedule();
	    backupSchedule.setAccntIndex(accountIndex);
	    backupSchedule.setLogDetete(backupAdd.getDeletesLogs() == null ? false : backupAdd.getDeletesLogs());
	    
	    if(backupAdd.getTarget().equals(OBDefineWeb.MSG_DEFINEWEB_BAK_TGT_ALL))
	        backupSchedule.setType(OBDefineWeb.BACKUP_TYPE_CFG_LOG);
        else if(backupAdd.getTarget().equals(OBDefineWeb.MSG_DEFINEWEB_BAK_TGT_ADCSMARTSETTINGS))
            backupSchedule.setType(OBDefineWeb.BACKUP_TYPE_CFG);
        else if(backupAdd.getTarget().equals(OBDefineWeb.MSG_DEFINEWEB_BAK_TGT_ADCSMARTLOGS))
            backupSchedule.setType(OBDefineWeb.BACKUP_TYPE_LOG);
        else if(backupAdd.getTarget().equals(OBDefineWeb.MSG_DEFINEWEB_BAK_TGT_ADCSETTINGS))
            backupSchedule.setType(OBDefineWeb.BACKUP_TYPE_ADC_CFG);
	    
	    backupSchedule.setScheduleType(backupAdd.getScheduleType());
	    
	    switch(backupAdd.getScheduleType())
	    {
	        case OBDtoBackupSchedule.SCHEDULE_DAILY:
	            backupSchedule.setScheduleHour(backupAdd.getScheduleHour());
	            backupSchedule.setScheduleMinute(backupAdd.getScheduleMinute());
	            break;
	        case OBDtoBackupSchedule.SCHEDULE_WEEKLY:
	            backupSchedule.setScheduleDayweek(backupAdd.getScheduleDayweek());
	            backupSchedule.setScheduleHour(backupAdd.getScheduleHour());
	            backupSchedule.setScheduleMinute(backupAdd.getScheduleMinute());
	            break;
	        case OBDtoBackupSchedule.SCHEDULE_MONTHLY:
	            backupSchedule.setScheduleDay(backupAdd.getScheduleDay());
	            backupSchedule.setScheduleHour(backupAdd.getScheduleHour());
	            backupSchedule.setScheduleMinute(backupAdd.getScheduleMinute());
	            break;
	        case OBDtoBackupSchedule.SCHEDULE_ONCE:
	            Calendar scheduleCalendar = Calendar.getInstance();
	            scheduleCalendar.setTime(backupAdd.getScheduleDate());
	            backupSchedule.setScheduleMonth(scheduleCalendar.get(Calendar.MONTH));
	            backupSchedule.setScheduleDay(scheduleCalendar.get(Calendar.DAY_OF_MONTH));
	            backupSchedule.setScheduleHour(backupAdd.getScheduleHour());
	            backupSchedule.setScheduleMinute(backupAdd.getScheduleMinute());
	            break;
	        default:
	    }
	    
	    return backupSchedule;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getTarget()
	{
		return target;
	}

	public void setTarget(String target)
	{
		this.target = target;
	}

	public Boolean getDeletesLogs()
	{
		return deletesLogs;
	}

	public void setDeletesLogs(Boolean deletesLogs)
	{
		this.deletesLogs = deletesLogs;
	}

    public int getScheduleType()
    {
        return scheduleType;
    }

    public void setScheduleType(int scheduleType)
    {
        this.scheduleType = scheduleType;
    }

    public int getScheduleMinute()
    {
        return scheduleMinute;
    }

    public void setScheduleMinute(int scheduleMinute)
    {
        this.scheduleMinute = scheduleMinute;
    }

    public int getScheduleHour()
    {
        return scheduleHour;
    }

    public void setScheduleHour(int scheduleHour)
    {
        this.scheduleHour = scheduleHour;
    }

    public int getScheduleDay()
    {
        return scheduleDay;
    }

    public void setScheduleDay(int scheduleDay)
    {
        this.scheduleDay = scheduleDay;
    }

    public int getScheduleMonth()
    {
        return scheduleMonth;
    }

    public void setScheduleMonth(int scheduleMonth)
    {
        this.scheduleMonth = scheduleMonth;
    }

    public int getScheduleDayweek()
    {
        return scheduleDayweek;
    }

    public void setScheduleDayweek(int scheduleDayweek)
    {
        this.scheduleDayweek = scheduleDayweek;
    }
    
    public Date getScheduleDate()
    {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate)
    {
        this.scheduleDate = scheduleDate;
    }

    @Override
    public String toString()
    {
        return "SysBackupAddDto [fileName=" + fileName + ", description="
                + description + ", target=" + target + ", deletesLogs="
                + deletesLogs + ", scheduleType=" + scheduleType
                + ", scheduleMinute=" + scheduleMinute + ", scheduleHour="
                + scheduleHour + ", scheduleDay=" + scheduleDay
                + ", scheduleMonth=" + scheduleMonth + ", scheduleDayweek="
                + scheduleDayweek + ", scheduleDate=" + scheduleDate + "]";
    }
}
