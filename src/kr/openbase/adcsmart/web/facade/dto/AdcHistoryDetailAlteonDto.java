package kr.openbase.adcsmart.web.facade.dto;

import java.util.List;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryAlteon;

public class AdcHistoryDetailAlteonDto {
	private String revertKey;
	private AdcSettingAlteonDto currentAdcSetting;
	private AdcSettingAlteonDto pastAdcSetting;
	private List<AdcHistoryDto> historyList;
	
	public static AdcHistoryDetailAlteonDto toAdcHistoryDetailAlteonDto(OBDtoAdcConfigHistoryAlteon historyDetailFromSvc) {
		AdcHistoryDetailAlteonDto historyDetail = new AdcHistoryDetailAlteonDto();
		if (historyDetailFromSvc.isRecoverable() == true)
		{
			historyDetail.setRevertKey("On");
		}
		else if (historyDetailFromSvc.isRecoverable() == false)
		{
			historyDetail.setRevertKey("Off");
		}
		historyDetail.currentAdcSetting = AdcSettingAlteonDto.toAdcSettingAlteonDto(historyDetailFromSvc.getVsConfigInfoNew());
		historyDetail.pastAdcSetting = AdcSettingAlteonDto.toAdcSettingAlteonDto(historyDetailFromSvc.getVsConfigInfoOld());
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
	public AdcSettingAlteonDto getCurrentAdcSetting() {
		return currentAdcSetting;
	}

	public void setCurrentAdcSetting(AdcSettingAlteonDto currentAdcSetting) {
		this.currentAdcSetting = currentAdcSetting;
	}

	public AdcSettingAlteonDto getPastAdcSetting() {
		return pastAdcSetting;
	}

	public void setPastAdcSetting(AdcSettingAlteonDto pastAdcSetting) {
		this.pastAdcSetting = pastAdcSetting;
	}

	public List<AdcHistoryDto> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<AdcHistoryDto> historyList) {
		this.historyList = historyList;
	}

	@Override
	public String toString() {
		return "AdcHistoryDetailAlteonDto [currentAdcSetting=" + currentAdcSetting + ", pastAdcSetting="
				+ pastAdcSetting + ", historyList=" + historyList + "]";
	}
}
