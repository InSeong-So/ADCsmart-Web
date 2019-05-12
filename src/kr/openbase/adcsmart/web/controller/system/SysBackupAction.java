package kr.openbase.adcsmart.web.controller.system;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoBackupSchedule;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.dto.SysBackupAddDto;
import kr.openbase.adcsmart.web.facade.dto.SysBackupDto;
import kr.openbase.adcsmart.web.facade.system.SysBackupFacade;
import kr.openbase.adcsmart.web.util.OBFileHandler;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "prototype")
public class SysBackupAction extends BaseAction 
{
	private transient Logger log = LoggerFactory.getLogger(SysBackupAction.class);
	
	@Autowired
	private SysBackupFacade sysBackupFacade;
	
	private List<OBDtoBackupSchedule> backupSchedules;
	private List<SysBackupDto> sysBackups;
	private Integer rowTotal;
	private List<String> sysBackupIndices;
	private SysBackupAddDto sysBackupAdd;
	private String searchKey;
	private Integer fromRow;
	private Integer toRow;
	private String  backupIndex;
	private Date fromPeriod;
	private Date toPeriod;
	private Long startTimeL;
    private Long endTimeL;
	private Integer orderDir; 						// 오른차순 = 1, 내림차순 = 2
	private Integer orderType;	
	
	@SuppressWarnings("unchecked")
	public String loadListContent() throws OBException 
	{
		try 
		{
			log.debug("searchKey:{},fromPeriod:{}, toPeriod:{}, fromRow:{}, toRow:{}", new Object[]{searchKey, fromPeriod, toPeriod, fromRow, toRow});
			if (fromRow != null && fromRow < 0)
			{
				sysBackups = ListUtils.EMPTY_LIST;
			}
			else
			{
			    setSearchTime();
				sysBackups = sysBackupFacade.getSysBackups(session.getAccountIndex(), searchKey , fromPeriod, toPeriod, fromRow, toRow, orderType, orderDir);
			}
			backupSchedules = sysBackupFacade.getSysBackupSchedule(session.getAccountIndex());
			
			log.debug("{}", sysBackups);
		}  
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
		
		return SUCCESS;
	}
	
	public String retrieveSysBackupsTotal() throws OBException
	{
		isSuccessful = true;
		try 
		{
			log.debug("searchKey:{} , fromPeriod:{} , toPeriod:{}", new Object[] {searchKey , fromPeriod , toPeriod});
			setSearchTime();
			rowTotal = sysBackupFacade.getSysBackupsTotal(session.getAccountIndex(), searchKey , fromPeriod, toPeriod);
			log.debug("sysBackupsTotal: {}", rowTotal);
		} 
		catch (OBException e) 
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}
	
	public String isDownloadFileExist() throws OBException
	{
		try 
		{
			SysBackupDto backupInfo = sysBackupFacade.getSysBackup(backupIndex);
			String fileName = "/backup/" + backupInfo.getIndex()+".tgz";
			if(OBFileHandler.isFileExist(fileName)==true)
			{
				isSuccessful = true;
			}
			else
			{
				isSuccessful = false;
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SYSTEM_FILE_NOT_FOUND);
			}
		}
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }		
		
		return SUCCESS;
	}
	
	public String downloadBackupfile() throws OBException 
	{
		try 
		{
			SysBackupDto backupInfo = sysBackupFacade.getSysBackup(backupIndex);
			String fileName = "/backup/" + backupInfo.getIndex()+".tgz";
			File file = new File(fileName);
			if (file != null)
			{
				setStrutsStream(file);
			}			
		}
		catch (OBException e) 
        {
            throw e;
        }
        catch (Exception e) 
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }		
		
		return SUCCESS;
	}
	
	public String loadAddContent() throws OBException 
	{
		log.debug("loadAddContent");
		return SUCCESS;
	}
	
	public String addSysBackup() throws OBException 
	{
		isSuccessful = true;
		try 
		{
			log.debug("sysBackupAdd: {}", sysBackupAdd);
			sysBackupFacade.addSysBackup(sysBackupAdd, session.getSessionDto());
		} 
		catch (OBException e) 
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}
	
	public String delSysBackups() throws Exception
	{
		isSuccessful = true;
		try 
		{
			log.debug("sysBackupIndices:{}", sysBackupIndices);
			sysBackupFacade.delSysBackups(sysBackupIndices, session.getSessionDto());
		} 
		catch (OBException e) 
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		
		return SUCCESS;
	}
	
	public String delBackupSchedule() throws Exception
	{
	    isSuccessful = true;
	    sysBackupFacade.delBackupSchedule(Integer.parseInt(sysBackupIndices.get(0)), session.getSessionDto());
	    return SUCCESS;
	}
	
	private void setSearchTime()
    {
        if (null != startTimeL && null != endTimeL)
        {
            fromPeriod = new Date(startTimeL);
            toPeriod = new Date(endTimeL);   
        } 
        else
        {
            toPeriod = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(toPeriod);
            calendar.set(Calendar.SECOND, 59);
            toPeriod = calendar.getTime();
            
            calendar.set(Calendar.SECOND, 00);
            calendar.add(Calendar.HOUR_OF_DAY, -12); 
            fromPeriod = calendar.getTime();         
        }       
        log.debug("startTime: " + fromPeriod.toString() + ", endTime: " + toPeriod.toString());
    }

	public List<OBDtoBackupSchedule> getBackupSchedules()
    {
        return backupSchedules;
    }

    public void setBackupSchedules(List<OBDtoBackupSchedule> backupSchedules)
    {
        this.backupSchedules = backupSchedules;
    }

    public List<SysBackupDto> getSysBackups() 
	{
		return sysBackups;
	}

	public void setSysBackups(List<SysBackupDto> sysBackups) 
	{
		this.sysBackups = sysBackups;
	}

	public Integer getRowTotal() 
	{
		return rowTotal;
	}

	public void setRowTotal(Integer rowTotal) 
	{
		this.rowTotal = rowTotal;
	}

	public List<String> getSysBackupIndices() 
	{
		return sysBackupIndices;
	}

	public void setSysBackupIndices(List<String> sysBackupIndices) 
	{
		this.sysBackupIndices = sysBackupIndices;
	}

	public SysBackupAddDto getSysBackupAdd()
	{
		return sysBackupAdd;
	}

	public void setSysBackupAdd(SysBackupAddDto sysBackupAdd) 
	{
		this.sysBackupAdd = sysBackupAdd;
	}

	public String getSearchKey()
	{
		return searchKey;
	}

	public void setSearchKey(String searchKey) 
	{
		this.searchKey = searchKey;
	}

	public Integer getFromRow() 
	{
		return fromRow;
	}

	public void setFromRow(Integer fromRow) 
	{
		this.fromRow = fromRow;
	}

	public Integer getToRow() 
	{
		return toRow;
	}

	public void setToRow(Integer toRow) 
	{
		this.toRow = toRow;
	}

	public Date getFromPeriod()
	{
		return fromPeriod;
	}

	public void setFromPeriod(Date fromPeriod) 
	{
		this.fromPeriod = fromPeriod;
	}

	public Date getToPeriod() 
	{
		return toPeriod;
	}

	public void setToPeriod(Date toPeriod) 
	{
		this.toPeriod = toPeriod;
	}

	public Integer getOrderDir()
	{
		return orderDir;
	}

	public void setOrderDir(Integer orderDir)
	{
		this.orderDir = orderDir;
	}

	public Integer getOrderType()
	{
		return orderType;
	}

	public void setOrderType(Integer orderType)
	{
		this.orderType = orderType;
	}

	public String getBackupIndex()
	{
		return backupIndex;
	}

	public void setBackupIndex(String backupIndex)
	{
		this.backupIndex = backupIndex;
	}

    public Long getStartTimeL()
    {
        return startTimeL;
    }

    public void setStartTimeL(Long startTimeL)
    {
        this.startTimeL = startTimeL;
    }

    public Long getEndTimeL()
    {
        return endTimeL;
    }

    public void setEndTimeL(Long endTimeL)
    {
        this.endTimeL = endTimeL;
    }	
}
