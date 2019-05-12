package kr.openbase.adcsmart.web.facade.dto;

import java.util.List;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryPASK;

public class AdcHistoryDetailPASKDto
{
	private String revertKey;
	private AdcSettingPASKDto currentAdcSetting;
	private AdcSettingPASKDto pastAdcSetting;
	private List<AdcHistoryDto> historyList;
	
	public static AdcHistoryDetailPASKDto toAdcHistoryDetailPASKDto(OBDtoAdcConfigHistoryPASK historyDetailFromSvc) 
	{
		if (historyDetailFromSvc == null)
			return null;
		
		AdcHistoryDetailPASKDto historyDetail = new AdcHistoryDetailPASKDto();
		if (historyDetailFromSvc.isRecoverable() == true)
		{
			historyDetail.setRevertKey("On");
		}
		else if (historyDetailFromSvc.isRecoverable() == false)
		{
			historyDetail.setRevertKey("Off");
		}
		historyDetail.currentAdcSetting = AdcSettingPASKDto.toAdcSettingPASKDto(historyDetailFromSvc.getVsConfigInfoNew());
		historyDetail.pastAdcSetting = AdcSettingPASKDto.toAdcSettingPASKDto(historyDetailFromSvc.getVsConfigInfoOld());
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

	public AdcSettingPASKDto getCurrentAdcSetting()
	{
		return currentAdcSetting;
	}

	public void setCurrentAdcSetting(AdcSettingPASKDto currentAdcSetting)
	{
		this.currentAdcSetting = currentAdcSetting;
	}

	public AdcSettingPASKDto getPastAdcSetting()
	{
		return pastAdcSetting;
	}

	public void setPastAdcSetting(AdcSettingPASKDto pastAdcSetting)
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
		return "AdcHistoryDetailPASKDto [revertKey=" + revertKey + ", currentAdcSetting=" + currentAdcSetting + ", pastAdcSetting=" + pastAdcSetting + ", historyList=" + historyList + "]";
	}
	
	
}
