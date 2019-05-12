package kr.openbase.adcsmart.web.facade.dto;

import java.util.List;

import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryF5;

public class AdcHistoryDetailF5Dto {
	private String revertKey;
	private AdcSettingF5Dto currentAdcSetting;
	private AdcSettingF5Dto pastAdcSetting;
	private List<AdcHistoryDto> historyList;
	
	public static AdcHistoryDetailF5Dto toAdcHistoryDetailF5Dto(OBDtoAdcConfigHistoryF5 historyDetailFromSvc) {
		if (historyDetailFromSvc == null)
			return null;
		
		AdcHistoryDetailF5Dto historyDetail = new AdcHistoryDetailF5Dto();
		if (historyDetailFromSvc.isRecoverable() == true)
		{
			historyDetail.setRevertKey("On");
		}
		else if (historyDetailFromSvc.isRecoverable() == false)
		{
			historyDetail.setRevertKey("Off");
		}		
		historyDetail.currentAdcSetting = AdcSettingF5Dto.toAdcSettingF5Dto(historyDetailFromSvc.getVsConfigInfoNew());
		historyDetail.pastAdcSetting = AdcSettingF5Dto.toAdcSettingF5Dto(historyDetailFromSvc.getVsConfigInfoOld());
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

	public AdcSettingF5Dto getCurrentAdcSetting() {
		return currentAdcSetting;
	}

	public void setCurrentAdcSetting(AdcSettingF5Dto currentAdcSetting) {
		this.currentAdcSetting = currentAdcSetting;
	}

	public AdcSettingF5Dto getPastAdcSetting() {
		return pastAdcSetting;
	}

	public void setPastAdcSetting(AdcSettingF5Dto pastAdcSetting) {
		this.pastAdcSetting = pastAdcSetting;
	}

	public List<AdcHistoryDto> getHistoryList() {
		return historyList;
	}

	public void setHistoryList(List<AdcHistoryDto> historyList) {
		this.historyList = historyList;
	}

	@Override
	public String toString()
	{
		return "AdcHistoryDetailF5Dto [revertKey=" + revertKey + ", currentAdcSetting=" + currentAdcSetting + ", pastAdcSetting=" + pastAdcSetting + ", historyList=" + historyList + "]";
	}	
}