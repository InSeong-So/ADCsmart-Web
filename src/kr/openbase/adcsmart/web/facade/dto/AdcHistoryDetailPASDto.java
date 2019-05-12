package kr.openbase.adcsmart.web.facade.dto;

import java.util.List;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryPAS;

public class AdcHistoryDetailPASDto
{
	private String revertKey;
	private AdcSettingPASDto currentAdcSetting;
	private AdcSettingPASDto pastAdcSetting;
	private List<AdcHistoryDto> historyList;
	
	public static AdcHistoryDetailPASDto toAdcHistoryDetailPASDto(OBDtoAdcConfigHistoryPAS historyDetailFromSvc) 
	{
		if (historyDetailFromSvc == null)
			return null;
		
		AdcHistoryDetailPASDto historyDetail = new AdcHistoryDetailPASDto();
		if (historyDetailFromSvc.isRecoverable() == true)
		{
			historyDetail.setRevertKey("On");
		}
		else if (historyDetailFromSvc.isRecoverable() == false)
		{
			historyDetail.setRevertKey("Off");
		}
		historyDetail.currentAdcSetting = AdcSettingPASDto.toAdcSettingPASDto(historyDetailFromSvc.getVsConfigInfoNew());
		historyDetail.pastAdcSetting = AdcSettingPASDto.toAdcSettingPASDto(historyDetailFromSvc.getVsConfigInfoOld());
		historyDetail.historyList = AdcHistoryDto.toAdcHistoryDtoFromOBDtoAdcConfigHistory2(historyDetailFromSvc.getVsHistoryList());
		return historyDetail;		
	}
	
	public String getRevertKey()
	{
		return revertKey;
	}

	public void setRevertKey(String revertKey)
	{
		this.revertKey = revertKey;
	}

	public AdcSettingPASDto getCurrentAdcSetting()
	{
		return currentAdcSetting;
	}

	public void setCurrentAdcSetting(AdcSettingPASDto currentAdcSetting)
	{
		this.currentAdcSetting = currentAdcSetting;
	}

	public AdcSettingPASDto getPastAdcSetting()
	{
		return pastAdcSetting;
	}

	public void setPastAdcSetting(AdcSettingPASDto pastAdcSetting)
	{
		this.pastAdcSetting = pastAdcSetting;
	}

	public List<AdcHistoryDto> getHistoryList()
	{
		return historyList;
	}

	public void setHistoryList(List<AdcHistoryDto> historyList)
	{
		this.historyList = historyList;
	}
	@Override
	public String toString()
	{
		return "AdcHistoryDetailPASDto [revertKey=" + revertKey + ", currentAdcSetting=" + currentAdcSetting + ", pastAdcSetting=" + pastAdcSetting + ", historyList=" + historyList + "]";
	}
	
	
}
