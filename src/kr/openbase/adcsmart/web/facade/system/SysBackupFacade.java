package kr.openbase.adcsmart.web.facade.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.OBBackupRestore;
import kr.openbase.adcsmart.service.dto.OBDtoBackupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoBackupSchedule;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.impl.OBBackupRestoreImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;
import kr.openbase.adcsmart.web.facade.dto.SysBackupAddDto;
import kr.openbase.adcsmart.web.facade.dto.SysBackupDto;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SysBackupFacade 
{
private transient Logger log = LoggerFactory.getLogger(SysBackupFacade.class);
	private OBBackupRestore sysBackupSvc;
	
	public SysBackupFacade() 
	{
		sysBackupSvc =  new OBBackupRestoreImpl();
	}
	
	public SysBackupDto getSysBackup(String index) throws OBException, Exception 
	{
		log.debug("backup index:{}", index);
		try 
		{
			OBDtoBackupInfo backupFromSvc = sysBackupSvc.getBackupInfo(index);
			log.debug("{}", backupFromSvc);
			return SysBackupDto.toSysBackupDto(backupFromSvc);
		} 
        catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
	}
	
	public List<SysBackupDto> getSysBackups(int accountIndex, String searchKey, Date fromPeriod, Date toPeriod, Integer fromRow, Integer toRow, Integer orderType, Integer orderDir) throws OBException, Exception 
	{
		// TODO request fromRow, endRow
		List<SysBackupDto> backups = new ArrayList<SysBackupDto>();
		log.debug("accountIndex:{}, searchKey:{} , fromPeriod:{} , toPeriod:{} , fromRow:{}, toRow:{}", new Object[]{accountIndex, searchKey, fromPeriod, toPeriod, fromRow, toRow});
		List<OBDtoBackupInfo> backupsFromSvc = sysBackupSvc.getBackupInfoList(accountIndex, StringUtils.isEmpty(searchKey) ? null : searchKey, fromPeriod, toPeriod, fromRow, toRow, orderType, orderDir);
		log.debug("{}", backupsFromSvc);
		backups.addAll(SysBackupDto.toSysBackupDto(backupsFromSvc));
		log.debug("{}", backups);
		
		return backups;
	}
	
	public int getSysBackupsTotal(int accountIndex, String searchKey , Date fromPeriod , Date toPeriod) throws OBException, Exception 
	{
		log.debug("accountIndex:{}, searchKey:{} , fromPeriod:{} , toPeriod:{}", new Object[]{accountIndex, searchKey ,fromPeriod , toPeriod});
		return sysBackupSvc.getBackupInfoListCount(accountIndex, StringUtils.isEmpty(searchKey) ? null : searchKey, fromPeriod, toPeriod);
	}
	
	public List<OBDtoBackupSchedule> getSysBackupSchedule(int accountIndex) throws OBException, Exception
	{
	    log.debug("accountIndex:{}", accountIndex);
	    return sysBackupSvc.getBackupScheduleList(accountIndex);
	}
	
	public void addSysBackup(SysBackupAddDto backupAdd, SessionDto session) throws OBException, Exception 
	{
	    log.debug("{}", backupAdd);
	    if (backupAdd.getScheduleType() == 0) // 일반 백업 추가
	    {
	        OBDtoBackupInfo backupFromSvc = SysBackupAddDto.toOBDtoBackupInfo(backupAdd, session.getAccountIndex());
	        log.debug("{}", backupFromSvc);
	        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
	        extraInfo.setExtraMsg1(backupAdd.getFileName());
	        sysBackupSvc.addBackup(backupFromSvc, extraInfo);
	    }
	    else // 백업 예약 추가
	    {
	        OBDtoBackupSchedule backupSchedule = SysBackupAddDto.toOBDtoBackupSchedule(backupAdd, session.getAccountIndex());
	        log.debug("{}", backupSchedule);
	        OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
	        extraInfo.setExtraMsg1(backupSchedule.getIndex() + "");
	        sysBackupSvc.addBackupSchedule(backupSchedule, extraInfo);
	    }
	}
	
	public void delSysBackups(List<String> backupIndices, SessionDto session) throws OBException, Exception 
	{
		log.debug("backupIndices:{}", backupIndices);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(backupIndices.toString());
		sysBackupSvc.delBackup(new ArrayList<String>(backupIndices), extraInfo); 
	}
	
	public void delBackupSchedule(int index, SessionDto session) throws OBException, Exception
	{
	    OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
	    extraInfo.setExtraMsg1(index + "");
	    sysBackupSvc.delBackupSchedule(index, extraInfo);
	}
}