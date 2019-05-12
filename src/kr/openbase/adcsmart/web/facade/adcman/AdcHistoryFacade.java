package kr.openbase.adcsmart.web.facade.adcman;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.openbase.adcsmart.service.OBAdcConfigHistory;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryF5;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryPAS;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryPASK;
import kr.openbase.adcsmart.service.impl.OBAdcConfigHistoryImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcHistoryDetailAlteonDto;
import kr.openbase.adcsmart.web.facade.dto.AdcHistoryDetailF5Dto;
import kr.openbase.adcsmart.web.facade.dto.AdcHistoryDetailPASDto;
import kr.openbase.adcsmart.web.facade.dto.AdcHistoryDetailPASKDto;
import kr.openbase.adcsmart.web.facade.dto.AdcHistoryDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AdcHistoryFacade 
{
	private static transient Logger log = LoggerFactory.getLogger(AdcHistoryFacade.class);
	
	private OBAdcConfigHistory adcConfigHistorySvc;

	public AdcHistoryFacade() 
	{
		adcConfigHistorySvc = new OBAdcConfigHistoryImpl();
	}
	
	public List<AdcHistoryDto> getAdcHistoryList(int adcIndex, String searchKey, Date fromPeriod, Date toPeriod, Integer fromRow, Integer toRow, Integer orderType, Integer orderDir) throws OBException, Exception 
	{
		 //TODO request fromRow, endRow
			List<AdcHistoryDto> historyList = new ArrayList<AdcHistoryDto>();
			List<OBDtoAdcConfigHistory> adcConfigHistoryListFromSvc = adcConfigHistorySvc.getAdcConfigHistoryList(adcIndex, searchKey, fromPeriod, toPeriod, fromRow, toRow, orderType, orderDir);
			log.debug("{}", adcConfigHistoryListFromSvc);
			historyList.addAll(AdcHistoryDto.toAdcHistoryDto(adcConfigHistoryListFromSvc));
			log.debug("{}", historyList);
			return historyList;	
	}
	
	public int getHistoryListTotal(int adcIndex, String searchKey, Date fromPeriod, Date toPeriod) throws OBException, Exception 
	{
			return adcConfigHistorySvc.getAdcConfigHistoryTotalRecordCount(adcIndex, StringUtils.isEmpty(searchKey) ? null : searchKey, fromPeriod, toPeriod);
	}
	
	public AdcHistoryDetailF5Dto getHistoryDetailF5(int adcIndex, String virtualSvrIndex, int logSeq) throws OBException, Exception 
	{
			AdcHistoryDetailF5Dto historyDetail = null;
			OBDtoAdcConfigHistoryF5 historyDetailFromSvc = adcConfigHistorySvc.getVSConfigHistoryF5(adcIndex, virtualSvrIndex, logSeq);
			log.debug("{}", historyDetailFromSvc);
			historyDetail = AdcHistoryDetailF5Dto.toAdcHistoryDetailF5Dto(historyDetailFromSvc);
			log.debug("{}", historyDetail);
			return historyDetail;
	
	}
	
	public AdcHistoryDetailAlteonDto getHistoryDetailAlteon(int adcIndex, String virtualSvrIndex, int logSeq) throws OBException, Exception 
	{
			AdcHistoryDetailAlteonDto historyDetail = null;
			OBDtoAdcConfigHistoryAlteon historyDetailFromSvc = adcConfigHistorySvc.getVSConfigHistoryAlteon(adcIndex, virtualSvrIndex, logSeq);
			log.debug("{}", historyDetailFromSvc);
			historyDetail = AdcHistoryDetailAlteonDto.toAdcHistoryDetailAlteonDto(historyDetailFromSvc);
			log.debug("{}", historyDetail);
			return historyDetail;
	}
	
	public AdcHistoryDetailPASDto getHistoryDetailPAS(int adcIndex, String virtualSvrIndex, int logSeq) throws OBException, Exception 
	{
		AdcHistoryDetailPASDto historyDetail = null;
			OBDtoAdcConfigHistoryPAS historyDetailFromSvc = adcConfigHistorySvc.getVSConfigHistoryPAS(adcIndex, virtualSvrIndex, logSeq);
			log.debug("{}", historyDetailFromSvc);
			historyDetail = AdcHistoryDetailPASDto.toAdcHistoryDetailPASDto(historyDetailFromSvc);
			log.debug("{}", historyDetail);
			return historyDetail;
	}
	
	public AdcHistoryDetailPASKDto getHistoryDetailPASK(int adcIndex, String virtualSvrIndex, int logSeq) throws OBException, Exception 
	{
		AdcHistoryDetailPASKDto historyDetail = null;
		OBDtoAdcConfigHistoryPASK historyDetailFromSvc = adcConfigHistorySvc.getVSConfigHistoryPASK(adcIndex, virtualSvrIndex, logSeq);
		log.debug("{}", historyDetailFromSvc);
		historyDetail = AdcHistoryDetailPASKDto.toAdcHistoryDetailPASKDto(historyDetailFromSvc);
		log.debug("{}", historyDetail);
		return historyDetail;
	}
	
	public Long revert(AdcDto adc, String virtualSvrIndex, SessionDto session) throws OBException, Exception 
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(virtualSvrIndex);
		if (adc.getType().equals("F5"))
		{
			return adcConfigHistorySvc.revertConfigF5(adc.getIndex(), virtualSvrIndex, extraInfo);
		}
		else if (adc.getType().equals("Alteon"))
		{
			return adcConfigHistorySvc.revertConfigAlteon(adc.getIndex(), virtualSvrIndex, extraInfo);
		}
		else if  (adc.getType().equals("PAS"))
		{
			return adcConfigHistorySvc.revertConfigPAS(adc.getIndex(), virtualSvrIndex, extraInfo);
		}
		else if  (adc.getType().equals("PASK"))
		{
			return adcConfigHistorySvc.revertConfigPASK(adc.getIndex(), virtualSvrIndex, extraInfo);
		}
		return 0L;
	}
	
	private String convertVSIndex(Integer adcIndex, String vsIndex)
	{
		if(vsIndex==null)
			return "";
		String [] items = vsIndex.split("_", 2);
		if(items.length!=2)
			return vsIndex;
		String newVal = adcIndex + "_" + items[1];
		return newVal;
	}
	
	public Long revertPeerAlteon(Integer activeIndex, String activeVSvrIndex, Integer peerIndex, Long activeHistoryLogIndex, SessionDto session) throws OBException, Exception 
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(convertVSIndex(peerIndex, activeVSvrIndex));
		return adcConfigHistorySvc.revertConfigAlteonPeer(peerIndex, activeHistoryLogIndex, extraInfo);
	}

	public Long revertPeerPAS(Integer activeIndex, String activeVSvrIndex, Integer peerIndex, Long activeHistoryLogIndex, SessionDto session) throws OBException, Exception 
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(convertVSIndex(peerIndex, activeVSvrIndex));
		return adcConfigHistorySvc.revertConfigPASPeer(peerIndex, activeHistoryLogIndex, extraInfo);
	}

	public Long revertPeerPASK(Integer activeIndex, String activeVSvrIndex, Integer peerIndex, Long activeHistoryLogIndex, SessionDto session) throws OBException, Exception 
	{
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(convertVSIndex(peerIndex, activeVSvrIndex));
		return adcConfigHistorySvc.revertConfigPASKPeer(peerIndex, activeHistoryLogIndex, extraInfo);
	}

	
	public void throwExceptionWhenNotRevertable(Integer adcIndex, String virtualSvrIndex, Integer accountIndex, Integer logSeq) throws OBException, Exception 
	{
		String resultMsg;
		{
			resultMsg = adcConfigHistorySvc.checkRecoverable(accountIndex, adcIndex, virtualSvrIndex, logSeq);
			if (!StringUtils.isEmpty(resultMsg))
			{
				throw new UnsupportedOperationException(resultMsg);
			}
		} 		
	}
}
