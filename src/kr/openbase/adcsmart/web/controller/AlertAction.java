package kr.openbase.adcsmart.web.controller;

import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.dto.OBDtoAdcAlertLog;
import kr.openbase.adcsmart.service.dto.OBDtoAlert;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.web.facade.AlertFacade;
import kr.openbase.adcsmart.web.facade.dto.AlertSettingsDto;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class AlertAction extends BaseAction {
	private transient Logger log = LoggerFactory.getLogger(AlertAction.class);

	@Autowired
	private AlertFacade alertFacade;

	private AlertSettingsDto settings;
	private OBDtoAlert alertData;
	private OBDtoAlert tickerData;
	private OBDtoOrdering orderOption;
	private Integer alertType;
	private Date nowTime;

	// 경보설정확인 AlertSettingData Get
	public String loadAlertSettingData() throws Exception {
		isSuccessful = true;
		try {
			settings = alertFacade.getSettings(session.getAccountIndex());
			log.debug("{}", settings);
		} catch (Exception e) {
			isSuccessful = false;
			e.printStackTrace();
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_ALERT_SETTING);
		}

		return SUCCESS;
	}

	// Ticker 정보 Get
	public String loadTickerAlertContent() throws Exception {
		try {
			tickerData = alertFacade.getAlertTicker(session.getAccountIndex());
			log.debug("alertMessage :{}", tickerData);
		} catch (Exception e) {
			e.printStackTrace();
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_ALERT_RETRIEVE);
		}

		return SUCCESS;
	}

	// Popup 정보 Get
	public String loadPopupAlertContent() throws Exception {
		try {
			if (new OBEnvManagementImpl().getEnvAlarmPopup() == 0) {// 옵션 체크함.
				alertData = new OBDtoAlert();
				ArrayList<OBDtoAdcAlertLog> alertList = new ArrayList<OBDtoAdcAlertLog>();
				alertData.setAlertCount(0);
				alertData.setAlertList(alertList);
				return SUCCESS;
			}
			Date dateRev = new Date();
			nowTime = dateRev;
			alertData = alertFacade.getAlertPopup(session.getAccountIndex(), alertType, 100, orderOption);
			log.debug("{}", alertData);
		} catch (Exception e) {
			e.printStackTrace();
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_ALERT_RETRIEVE);
		}

		return SUCCESS;
	}

	// UserAlertTime 갱신
	public String modifyUserAlertTime() throws Exception {
		try {
			alertFacade.updateUserAlertTime(session.getAccountIndex());
		} catch (Exception e) {
			e.printStackTrace();
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_ALERT_CHECK);
		}

		return SUCCESS;
	}

	public AlertSettingsDto getSettings() {
		return settings;
	}

	public void setSettings(AlertSettingsDto settings) {
		this.settings = settings;
	}

	public OBDtoAlert getAlertData() {
		return alertData;
	}

	public void setAlertData(OBDtoAlert alertData) {
		this.alertData = alertData;
	}

	public OBDtoAlert getTickerData() {
		return tickerData;
	}

	public void setTickerData(OBDtoAlert tickerData) {
		this.tickerData = tickerData;
	}

	public OBDtoOrdering getOrderOption() {
		return orderOption;
	}

	public void setOrderOption(OBDtoOrdering orderOption) {
		this.orderOption = orderOption;
	}

	public Integer getAlertType() {
		return alertType;
	}

	public void setAlertType(Integer alertType) {
		this.alertType = alertType;
	}

	public Date getNowTime() {
		return nowTime;
	}

	public void setNowTime(Date nowTime) {
		this.nowTime = nowTime;
	}

	@Override
	public String toString() {
		return "AlertAction [settings=" + settings + ", alertData=" + alertData + ", tickerData=" + tickerData
				+ ", orderOption=" + orderOption + ", alertType=" + alertType + ", nowTime=" + nowTime + "]";
	}
}