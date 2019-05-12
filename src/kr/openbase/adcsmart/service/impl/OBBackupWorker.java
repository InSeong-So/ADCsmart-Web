package kr.openbase.adcsmart.service.impl;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoBackupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBBackupWorker implements Runnable
{
	private OBDtoBackupInfo backupInfo;
	private OBDtoExtraInfo 	extraInfo;
	
	public OBDtoBackupInfo getBackupInfo()
	{
		return backupInfo;
	}

	public void setBackupInfo(OBDtoBackupInfo backupInfo)
	{
		this.backupInfo = backupInfo;
	}

	public OBDtoExtraInfo getExtraInfo()
	{
		return extraInfo;
	}

	public void setExtraInfo(OBDtoExtraInfo extraInfo)
	{
		this.extraInfo = extraInfo;
	}

	public OBBackupWorker(OBDtoBackupInfo backupInfo, OBDtoExtraInfo extraInfo)
	{
		this.backupInfo=backupInfo;
		this.extraInfo=extraInfo;
	}
	
	@Override
	public void run()
	{
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("start. backupInfo:%s, extraInfo:%s", getBackupInfo(), getExtraInfo()));

		OBDtoBackupInfo backupInfo = getBackupInfo();
		OBDtoExtraInfo 	extraInfo = getExtraInfo();

		OBDatabase db=new OBDatabase();

		try
		{
			db.openDB();
			new OBBackupRestoreImpl().doBackup(backupInfo, extraInfo);		
		}
		catch(Exception e)
		{
			String typeMsg= makeDBMsg(backupInfo.getType());
			
			new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_ENV_ADD_DB_BACKUP_FAIL, typeMsg);
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to backup db.msg:%s", e.getMessage()));
			return;
		}
		finally
		{
			if(db!=null) db.closeDB();
		}
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format("end. backupInfo:%s, extraInfo:%s", backupInfo, extraInfo));;
	}
	
	private String makeDBMsg(Integer type)
	{
		String retVal = "";
		switch(type)
		{
		case 0:
			retVal = OBMessages.getMessage(OBMessages.MSG_SYSTEM_DB_BACKUP_TYPE1);
			break;
		case 1:
			retVal = OBMessages.getMessage(OBMessages.MSG_SYSTEM_DB_BACKUP_TYPE2);
			break;
		case 2:
			retVal = OBMessages.getMessage(OBMessages.MSG_SYSTEM_DB_BACKUP_TYPE3);
			break;
		case 3:
			retVal = OBMessages.getMessage(OBMessages.MSG_SYSTEM_DB_BACKUP_TYPE4);
			break;
		}
		return retVal;
	}
}
