package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoBackupInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.web.util.NumberUtil;
import kr.openbase.adcsmart.web.util.OBDefineWeb;

public class SysBackupDto
{
	private String index;
	private Date backupTime;
	private String status;
	private String fileName;
	private Integer accountIndex;
	private String accountId;
	private String description;
	private String target;
	private Long fileSize;
	private String fileSizeWithUnit;
	
	public static SysBackupDto toSysBackupDto(OBDtoBackupInfo backupFromSvc) 
	{
		SysBackupDto backup = new SysBackupDto();
		backup.setIndex(backupFromSvc.getIndex());
		backup.setAccountId(backupFromSvc.getAccntID());
		backup.setAccountIndex(backupFromSvc.getAccntIndex());
		backup.setDescription(backupFromSvc.getComments());
		backup.setFileName(backupFromSvc.getFileName());
		backup.setFileSize(backupFromSvc.getFileSize());
		backup.setFileSizeWithUnit(NumberUtil.toStringWithUnit(backupFromSvc.getFileSize(), "B"));
		backup.setBackupTime(backupFromSvc.getOccurTime());
		
		switch (backupFromSvc.getStatus()) 
		{
		case OBReportInfo.STATUS_INIT:
			backup.setStatus(OBDefineWeb.getDefineWeb(OBDefineWeb.STATUS_INIT)); break;
		case OBReportInfo.STATUS_RUNNING:
			backup.setStatus(OBDefineWeb.getDefineWeb(OBDefineWeb.STATUS_CREATING)); break;
		case OBReportInfo.STATUS_COMPLETE:
			backup.setStatus(OBDefineWeb.getDefineWeb(OBDefineWeb.STATUS_COMPLETE)); break;
		case OBReportInfo.STATUS_ERROR:
			backup.setStatus(OBDefineWeb.getDefineWeb(OBDefineWeb.STATUS_FAIL)); break;
		}
		
//		private Integer type;//0:전체. 1: ADC 설정정보. 2: ADC 로그 정보.
//		0: ADCSmart 설정&로그 정보 , 1: ADCSmart 설정 정보, 2: ADCSmart 로그 정보, 3: ADC 설정 정보 (기존1)
		
		switch (backupFromSvc.getType()) 
		{
		case 0:
			backup.setTarget(OBDefineWeb.MSG_DEFINEWEB_BAK_TGT_ALL); break;
		case 1:
			backup.setTarget(OBDefineWeb.MSG_DEFINEWEB_BAK_TGT_ADCSMARTSETTINGS); break;
		case 2:
			backup.setTarget(OBDefineWeb.MSG_DEFINEWEB_BAK_TGT_ADCSMARTLOGS); break;
		case 3:
			backup.setTarget(OBDefineWeb.MSG_DEFINEWEB_BAK_TGT_ADCSETTINGS); break;
		default:
			backup.setTarget(OBDefineWeb.MSG_DEFINEWEB_BAK_TGT_UNKNOWN);	
		}
		
		return backup;
	}
	
	public static List<SysBackupDto> toSysBackupDto(List<OBDtoBackupInfo> backupsFromSvc)
	{
		List<SysBackupDto> backups = new ArrayList<SysBackupDto>();
		
		if (backupsFromSvc != null) 
		{
			for (OBDtoBackupInfo e : backupsFromSvc)
			{
				backups.add(toSysBackupDto(e));
			}
				
		}
		
		return backups;
	}
	
	public String getIndex() 
	{
		return index;
	}

	public void setIndex(String index)
	{
		this.index = index;
	}

	public Date getBackupTime() 
	{
		return backupTime;
	}
	public void setBackupTime(Date backupTime) 
	{
		this.backupTime = backupTime;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}
	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}
	
	public Integer getAccountIndex()
	{
		return accountIndex;
	}

	public void setAccountIndex(Integer accountIndex)
	{
		this.accountIndex = accountIndex;
	}

	public String getAccountId()
	{
		return accountId;
	}

	public void setAccountId(String accountId)
	{
		this.accountId = accountId;
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
	
	public Long getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(Long fileSize)
	{
		this.fileSize = fileSize;
	}

	public String getFileSizeWithUnit()
	{
		return fileSizeWithUnit;
	}

	public void setFileSizeWithUnit(String fileSizeWithUnit)
	{
		this.fileSizeWithUnit = fileSizeWithUnit;
	}

	@Override
	public String toString()
	{
		return "SysBackupDto [index=" + index + ", backupTime=" + backupTime
				+ ", status=" + status + ", fileName=" + fileName
				+ ", accountIndex=" + accountIndex + ", accountId=" + accountId
				+ ", description=" + description + ", target=" + target
				+ ", fileSize=" + fileSize + ", fileSizeWithUnit="
				+ fileSizeWithUnit + "]";
	}
	
}
