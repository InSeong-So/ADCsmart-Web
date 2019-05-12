package kr.openbase.adcsmart.web.controller.adcman;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.adcman.AdcFacade;
import kr.openbase.adcsmart.web.facade.adcman.AdcHistoryFacade;
import kr.openbase.adcsmart.web.facade.adcman.VirtualSvrFacade;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcHistoryDetailAlteonDto;
import kr.openbase.adcsmart.web.facade.dto.AdcHistoryDetailF5Dto;
import kr.openbase.adcsmart.web.facade.dto.AdcHistoryDetailPASDto;
import kr.openbase.adcsmart.web.facade.dto.AdcHistoryDetailPASKDto;
import kr.openbase.adcsmart.web.facade.dto.AdcHistoryDto;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class AdcHistoryAction extends BaseAction
{
	private transient Logger log = LoggerFactory.getLogger(AdcHistoryAction.class);
	
	@Autowired
	private AdcHistoryFacade adcHistoryFacade;
	
	private AdcDto adc;
	private List<AdcHistoryDto> adcHistoryList;
	private Integer rowTotal;
	private String searchKey;
	private Integer fromRow;
	private Integer toRow;
	private String virtualSvrIndex;
	private Integer logSeq; //이력 상세 조회 시 사용
	private Integer orderDir; // 오른차순 = 1, 내림차순 = 2
	private Integer orderType; // occurTime = 11 , vsName = 1 , vsIpaddress =2, content=3
	private AdcHistoryDetailF5Dto historyDetailF5;
	private AdcHistoryDetailAlteonDto historyDetailAlteon;
	private AdcHistoryDetailPASDto historyDetailPAS;
	private AdcHistoryDetailPASKDto historyDetailPASK;
	private Date fromPeriod;
	private Date toPeriod;
	private Long startTimeL;
    private Long endTimeL;
	private Integer pairIndex;
	private Long	lastLogHistoryIndex;// 복구한 이력의 index
	
	@SuppressWarnings("unchecked")
	public String loadListContent() throws OBException 
	{
		try 
		{
			log.debug("adc:{}, searchKey:{}, fromRow:{}, toRow:{}, fromPeriod:{}, toPeriod:{}", new Object[]{adc, searchKey, fromRow, toRow, fromPeriod, toPeriod});	
			if (fromRow != null && fromRow < 0)
				adcHistoryList = ListUtils.EMPTY_LIST;
			else
			{
			    setSearchTime();
				log.debug("adc:{}, searchKey:{}, fromRow:{}, toRow:{}, fromPeriod:{}, toPeriod:{}", new Object[]{adc, searchKey, fromRow, toRow, fromPeriod, toPeriod});
				adcHistoryList = adcHistoryFacade.getAdcHistoryList(adc.getIndex(), searchKey, fromPeriod, toPeriod, fromRow, toRow, orderType, orderDir);
			}
			log.debug("{}", adcHistoryList);
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
	
	public String retrieveHistoryListTotal() throws OBException 
	{
		isSuccessful = true;
		try 
		{
			log.debug("adc: {}, searchKey: {}, fromPeriod: {}, toPeriod: {}", new Object[]{adc, searchKey, fromPeriod, toPeriod});
			setSearchTime();
			log.debug("adc: {}, searchKey: {}, fromPeriod: {}, toPeriod: {}", new Object[]{adc, searchKey, fromPeriod, toPeriod});
			rowTotal = adcHistoryFacade.getHistoryListTotal(adc.getIndex(), searchKey,  fromPeriod, toPeriod);
		
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
	
	public String loadHistoryDetail() throws OBException 
	{
		try 
		{
			log.debug("adc:{}, virtualSvrIndex:{}", adc, virtualSvrIndex);
			if (adc.getType().equals("F5")) 
			{
				historyDetailF5 = adcHistoryFacade.getHistoryDetailF5(adc.getIndex(), virtualSvrIndex, logSeq);
				log.debug("{}", historyDetailF5);
				return "F5";
			} 
			else if (adc.getType().equals("Alteon"))
			{
				historyDetailAlteon = adcHistoryFacade.getHistoryDetailAlteon(adc.getIndex(), virtualSvrIndex, logSeq);
				log.debug("{}", historyDetailAlteon);
				return "ALTEON";
			}
			else if (adc.getType().equals("PAS"))
			{
				historyDetailPAS = adcHistoryFacade.getHistoryDetailPAS(adc.getIndex(), virtualSvrIndex, logSeq);
				log.debug("{}", historyDetailPAS);
				return "PAS";
			}
			else 
			{
				historyDetailPASK = adcHistoryFacade.getHistoryDetailPASK(adc.getIndex(), virtualSvrIndex, logSeq);
				log.debug("{}", historyDetailPASK);
				return "PASK";
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
	}
	
	public String isRevertable() throws OBException 
	{
		isSuccessful = true;
		try 
		{
			log.debug("adc:{}, virtualSvrIndex:{}", adc, virtualSvrIndex);
			adcHistoryFacade.throwExceptionWhenNotRevertable(adc.getIndex(), virtualSvrIndex, session.getAccountIndex(), logSeq);
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REVERT_CONFIRM);			
		} 
		catch (OBException e) 
        {
			isSuccessful = false;
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REVERT_CHECK_ERROR);
            throw e;
        }
        catch (Exception e) 
        {
        	isSuccessful = false;
        	message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REVERT_CHECK_ERROR);
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
		return SUCCESS;
	}
	
	public String checkPairIndex() throws Exception
	{
		try
		{
			pairIndex = new VirtualSvrFacade().getPeerIndex(adc.getIndex());
			
			String peerAdcIPAddress = "";
			String peerAdcName = "";

    		if(pairIndex!=null && pairIndex>0)
    		{
    			AdcDto adcInfo = new AdcFacade().getAdc(pairIndex);
    			peerAdcIPAddress = adcInfo.getIp();
    			peerAdcName = adcInfo.getName();
    			message = String.format(OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REVERT_CONFIRM_ACTIVE_STANDBY), peerAdcName, peerAdcIPAddress);
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
	
	public String revert() throws OBException 
	{
		isSuccessful = true;
		try 
		{
			log.debug("adc:{}, virtualSvrIndex:{}", adc, virtualSvrIndex);
			lastLogHistoryIndex = adcHistoryFacade.revert(adc, virtualSvrIndex, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REVERT_SLB_SUCCESS);
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
	
	private String revertPeerF5() throws OBException 
	{// peer 장비에 대한 이력 복구 처리. F5의 경우에는 config sync를 이용한다.
		isSuccessful = true;
		try
		{
			new VirtualSvrFacade().syncConifgF5(adc, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_CONFIG_PEER_SUCCESS);
		}
		catch (OBException e) 
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}
	
	private String revertPeerAlteon() throws OBException 
	{
		isSuccessful = true;
		try 
		{
			log.debug("adc:{}, virtualSvrIndex:{}", adc, virtualSvrIndex);
			adcHistoryFacade.revertPeerAlteon(adc.getIndex(), virtualSvrIndex, pairIndex, lastLogHistoryIndex, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REVERT_SLB_SUCCESS);
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

	private String revertPeerPAS() throws OBException 
	{
		isSuccessful = true;
		try 
		{
			log.debug("adc:{}, virtualSvrIndex:{}", adc, virtualSvrIndex);
			adcHistoryFacade.revertPeerPAS(adc.getIndex(), virtualSvrIndex, pairIndex, lastLogHistoryIndex, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REVERT_SLB_SUCCESS);
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

	private String revertPeerPASK() throws OBException 
	{
		isSuccessful = true;
		try 
		{
			log.debug("adc:{}, virtualSvrIndex:{}", adc, virtualSvrIndex);
			adcHistoryFacade.revertPeerPASK(adc.getIndex(), virtualSvrIndex, pairIndex, lastLogHistoryIndex, session.getSessionDto());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_REVERT_SLB_SUCCESS);
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

	public String revertPeer() throws OBException 
	{
		if (adc.getType().equals("F5"))
		{
			return revertPeerF5();
		}
		else if (adc.getType().equals("Alteon"))
		{
			return revertPeerAlteon();
		}
		else if (adc.getType().equals("PAS"))
		{
			return revertPeerPAS();
		}
		else if (adc.getType().equals("PASK"))
		{
			return revertPeerPASK();
		}	
		//return adc.getType().equals("F5") ? "F5" : (adc.getType().equals("Alteon") ? "ALTEON" : "PAS");
		return "";
		
//		try 
//		{
//			log.debug("adc:{}, virtualSvrIndex:{}", adc, virtualSvrIndex);
//			adcHistoryFacade.revert(adc, virtualSvrIndex, session.getSessionDto());
//			message = OBMessageWeb.MSG_REVERT_SLB_SUCCESS;
//		}        
//		catch (OBException e) 
//        {
//            throw e;
//        }
//        catch (Exception e) 
//        {
//            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//        }
//		return SUCCESS;
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
            calendar.add(Calendar.HOUR_OF_DAY, -12);            
            fromPeriod = calendar.getTime();         
        }       
        log.debug("startTime: " + fromPeriod.toString() + ", endTime: " + toPeriod.toString());
    }

	public AdcDto getAdc()
	{
		return adc;
	}

	public void setAdc(AdcDto adc)
	{
		this.adc = adc;
	}

	public List<AdcHistoryDto> getAdcHistoryList()
	{
		return adcHistoryList;
	}

	public void setAdcHistoryList(List<AdcHistoryDto> adcHistoryList)
	{
		this.adcHistoryList = adcHistoryList;
	}

	public Integer getRowTotal()
	{
		return rowTotal;
	}

	public void setRowTotal(Integer rowTotal)
	{
		this.rowTotal = rowTotal;
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

	public String getVirtualSvrIndex()
	{
		return virtualSvrIndex;
	}

	public void setVirtualSvrIndex(String virtualSvrIndex)
	{
		this.virtualSvrIndex = virtualSvrIndex;
	}

	public AdcHistoryDetailF5Dto getHistoryDetailF5()
	{
		return historyDetailF5;
	}

	public void setHistoryDetailF5(AdcHistoryDetailF5Dto historyDetailF5)
	{
		this.historyDetailF5 = historyDetailF5;
	}

	public AdcHistoryDetailAlteonDto getHistoryDetailAlteon()
	{
		return historyDetailAlteon;
	}

	public void setHistoryDetailAlteon(AdcHistoryDetailAlteonDto historyDetailAlteon)
	{
		this.historyDetailAlteon = historyDetailAlteon;
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

	public AdcHistoryDetailPASDto getHistoryDetailPAS()
	{
		return historyDetailPAS;
	}

	public void setHistoryDetailPAS(AdcHistoryDetailPASDto historyDetailPAS)
	{
		this.historyDetailPAS = historyDetailPAS;
	}
	
	public AdcHistoryDetailPASKDto getHistoryDetailPASK()
	{
		return historyDetailPASK;
	}

	public void setHistoryDetailPASK(AdcHistoryDetailPASKDto historyDetailPASK)
	{
		this.historyDetailPASK = historyDetailPASK;
	}

    public Logger getLog()
    {
        return log;
    }

    public void setLog(Logger log)
    {
        this.log = log;
    }

    public Integer getLogSeq()
    {
        return logSeq;
    }

    public void setLogSeq(Integer logSeq)
    {
        this.logSeq = logSeq;
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

    public Integer getPairIndex()
    {
        return pairIndex;
    }

    public void setPairIndex(Integer pairIndex)
    {
        this.pairIndex = pairIndex;
    }

    public Long getLastLogHistoryIndex()
    {
        return lastLogHistoryIndex;
    }

    public void setLastLogHistoryIndex(Long lastLogHistoryIndex)
    {
        this.lastLogHistoryIndex = lastLogHistoryIndex;
    }	
}