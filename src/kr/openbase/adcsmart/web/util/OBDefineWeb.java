package kr.openbase.adcsmart.web.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBDefineWeb {
	private transient static Logger log = LoggerFactory.getLogger(OBDefineWeb.class);
	private static Properties msgProps = null;

	public static final String HOUR = "MSG_DEFINEWEB_HOUR"; // 시간
	public static final String MIN = "MSG_DEFINEWEB_MINUTE"; // 분
	public static final String SECOND = "MSG_DEFINEWEB_SECOND"; // 초
	public static final int ONE_HOUR = 3600;// 단위:초.
	public static final int ONE_DAY = 86400;// 단위:초.

	public static final int LOGIN_ALIVE_TIMEOUT = 30;// 단위:초.

	public static final int CFG_ON = 1;
	public static final int CFG_OFF = 0;
//	public static final int CFG_DOUBLE_LOGIN_ACCESS_ON = 1;
//	public static final int CFG_DOUBLE_LOGIN_ACCESS_OFF = 0;

	// 서비스 응답시간 on/off
//	public static final int CFG_SERVICE_RESPONSE_TIME_ON = 1;
//	public static final int CFG_SERVICE_RESPONSE_TIME_OFF = 0;

	public static final int CFG_SCH_BACKUP_ON = 1;
	public static final int CFG_SCH_BACKUP_OFF = 2;
	public static final int CFG_BACKUP_LOGDEL_ON = 1;
	public static final int CFG_BACKUP_LOGDEL_OFF = 2;

	public static final int ALERT_NONE = 0;
	public static final int ALERT_POPUP = 1;
	public static final int ALERT_TICKER = 2;
	public static final int ALERT_SOUND_ON = 1;
	public static final int ALERT_SOUND_OFF = 0;

//	public static final int DASHBOARD_ALERT_ON = 1;
//	public static final int DASHBOARD_ALERT_OFF = 0;

//	public static final int PROFILE_MATCHACROSS_SERVICE_ON = 1;
//	public static final int PROFILE_MATCHACROSS_SERVICE_OFF = 0;

	// 구간응답시간 on/off
//	public static final int CFG_RESPONSE_TIME_SECTION_ON = 1;
//	public static final int CFG_RESPONSE_TIME_SECTION_OFF = 0;

	/**
	 * 백업 Type ADCSmart 설정&로그 정보 : BACKUP_TYPE_CFG_LOG ADCSmart 설정 정보 :
	 * BACKUP_TYPE_CFG ADCSmart 로그 정보 : BACKUP_TYPE_LOG ADC 설정 정보 :
	 * BACKUP_TYPE_ADC_CFG *
	 */

	public static final int BACKUP_TYPE_CFG_LOG = 0;
	public static final int BACKUP_TYPE_CFG = 1;
	public static final int BACKUP_TYPE_LOG = 2;
	public static final int BACKUP_TYPE_ADC_CFG = 3;

	/* ADC 설정 점검 */
	public static final int ALL_CONFIG_CHECK = 0;
	public static final int SNMP_CONFIG_CHECK = 1;
	public static final int SNMP_READCOMM_CONFIG_CHECK = 2;
	public static final int SYSLOG_CONFIG_CHECK = 3;
	public static final int SYSLOG_SERVERLIST_CONFIG_CHECK = 4;
	public static final int SYSLOG_LEVEL_CONFIG_CHECK = 5;
	public static final int VSTAT_STATE_CONFIG_CHECK = 6;
	/* ADC 기능 점검 */
	public static final int LOGIN_FUNCTION_CHECK = 7;
	public static final int SNMP_FUNCTION_CHECK = 8;
	public static final int SYSLOG_FUNCTION_CHECK = 9;

	public static final String SUCCESS = "MSG_DEFINEWEB_NORMAL"; // 정상
	public static final String FAIL = "MSG_DEFINEWEB_FAIL"; // 실패
	public static final String SNMP = "MSG_DEFINEWEB_SNMP"; // v1/v2 access enabled
	public static final String SYSLOG = "MSG_DEFINEWEB_SYSLOG"; // syslogging all features
	public static final String VSTAT = "MSG_DEFINEWEB_VSTAT"; // enabled

	// license
	public static final String MSG_DEFINEWEB_LICSUCCESS = "MSG_DEFINEWEB_LICSUCCESS";
	public static final String MSG_DEFINEWEB_LICFORMAT_ERROR = "MSG_DEFINEWEB_LICFORMAT_ERROR";
	public static final String MSG_DEFINEWEB_LICFILE_ERROR = "MSG_DEFINEWEB_LICFILE_ERROR";
	public static final String MSG_DEFINEWEB_LICINTEGRITY_ERROR = "MSG_DEFINEWEB_LICINTEGRITY_ERROR";
	public static final String MSG_DEFINEWEB_LICTYPE_ERROR = "MSG_DEFINEWEB_LICTYPE_ERROR";
	public static final String MSG_DEFINEWEB_LICVERSION_ERROR = "MSG_DEFINEWEB_LICVERSION_ERROR";
	public static final String MSG_DEFINEWEB_LICIP_ERROR = "MSG_DEFINEWEB_LICIP_ERROR";
	public static final String MSG_DEFINEWEB_LICMAC_ERROR = "MSG_DEFINEWEB_LICMAC_ERROR";
	public static final String MSG_DEFINEWEB_LICTIME_ERROR = "MSG_DEFINEWEB_LICTIME_ERROR";
	public static final String MSG_DEFINEWEB_LICARGUMENT_ERROR = "MSG_DEFINEWEB_LICARGUMENT_ERROR";
	public static final String MSG_DEFINEWEB_LICUNKOWN = "MSG_DEFINEWEB_LICUNKOWN";

	public static final String MSG_NOT_SUPPORT_REPORT_OUT = "MSG_NOT_SUPPORT_REPORT_OUT"; // 초
	public static final String MSG_NOT_SUPPORT_REPORT = "MSG_NOT_SUPPORT_REPORT"; // 초

	/* 세션검색 타입 define 추가 */
	public static final String SESSION_ALTEON = "Alteon";
	public static final String SESSION_F5 = "F5";

	// 백업설정
	public static final String STATUS_INIT = "MSG_DEFINEWEB_INIT";
	public static final String STATUS_CREATING = "MSG_DEFINEWEB_CREATING";
	public static final String STATUS_COMPLETE = "MSG_DEFINEWEB_COMPLETE";
	public static final String STATUS_FAIL = "MSG_DEFINEWEB_FAIL";

	public static final String MSG_DEFINEWEB_BAK_TGT_ALL = "all";
	public static final String MSG_DEFINEWEB_BAK_TGT_ADCSMARTSETTINGS = "adcsmart Settings";
	public static final String MSG_DEFINEWEB_BAK_TGT_ADCSMARTLOGS = "adcsmart";
	public static final String MSG_DEFINEWEB_BAK_TGT_ADCSETTINGS = "ADC Settings";
	public static final String MSG_DEFINEWEB_BAK_TGT_UNKNOWN = "unknownTarget";

	// Monitoring
	public static final String SEARCH_TIME_HOURS_MODE = "hoursMode";
	public static final String SEARCH_TIME_PERIOD_MODE = "periodMode";

	// Dashboard
	public static final String SELECTION_ALL = "all";
	public static final String SELECTION_GROUP = "group";
	public static final String SELECTION_ADC = "adc";

	// Report
	public static final String MSG_DEFINEWEB_RPT_TYPE_SYSADMIN = "sysAdminReport";
	public static final String MSG_DEFINEWEB_RPT_TYPE_SYSADMIN_KO = "MSG_DEFINEWEB_RPT_TYPE_SYSADMIN_KO";
	public static final String MSG_DEFINEWEB_RPT_TYPE_SYSADMIN_TOTAL = "sysAdminTotalReport";
	public static final String MSG_DEFINEWEB_RPT_TYPE_SYSADMIN_TOTAL_KO = "MSG_DEFINEWEB_RPT_TYPE_SYSADMIN_TOTAL_KO";
	public static final String MSG_DEFINEWEB_RPT_TYPE_SYSFAULT = "sysFalultReport";
	public static final String MSG_DEFINEWEB_RPT_TYPE_SYSFAULT_KO = "MSG_DEFINEWEB_RPT_TYPE_SYSFAULT_KO";
	public static final String MSG_DEFINEWEB_RPT_TYPE_UNKNOWN = "unknownReport";
	public static final String MSG_DEFINEWEB_RPT_TYPE_ADC_DIAGNOSIS = "adcDiagnosisReport";
	public static final String MSG_DEFINEWEB_RPT_TYPE_ADC_DIAGNOSIS_KO = "MSG_DEFINEWEB_RPT_TYPE_ADC_DIAGNOSIS_KO";
	public static final String MSG_DEFINEWEB_RPT_TYPE_L4DAILY = "l4OpDailyReport";
	public static final String MSG_DEFINEWEB_RPT_TYPE_L4DAILY_KO = "MSG_DEFINEWEB_RPT_TYPE_L4DAILY_KO";
	public static final String MSG_DEFINEWEB_RPT_TYPE_L4WEEKLY = "l4OpWeeklyReport";
	public static final String MSG_DEFINEWEB_RPT_TYPE_L4WEEKLY_KO = "MSG_DEFINEWEB_RPT_TYPE_L4WEEKLY_KO";
	public static final String MSG_DEFINEWEB_RPT_TYPE_L4MONTHLY = "l4OpMonthlyReport";
	public static final String MSG_DEFINEWEB_RPT_TYPE_L4MONTHLY_KO = "MSG_DEFINEWEB_RPT_TYPE_L4MONTHLY_KO";
	public static final String MSG_DEFINEWEB_RPT_TYPE_L4OPERATION = "l4OperationReport";
	public static final String MSG_DEFINEWEB_RPT_TYPE_L4OPERATION_KO = "MSG_DEFINEWEB_RPT_TYPE_L4OPERATION_KO";
	public static final String RPT_PERIOD_PREVIOUSDATE = "previousDate";
	public static final String RPT_PERIOD_CUSTUM = "custom";
	public static final String RPT_OUT_PDF = "pdf";
	public static final String RPT_OUT_RTF = "rtf";
	public static final String RPT_OUT_PPTX = "pptx";
	public static final String RPT_FILENAME_SYSADMIN = "SystemAdmin.jasper";
	public static final String RPT_FILENAME_SYSFAULT = "SystemFault.jasper";
	public static final String RPT_FILENAME_L4OPERATION = "L4Operation.jasper";

	public static final String STATUS_AVAILABLE = "available";
	public static final String CONFIG_SUCCESS = "success";

	private String getLanguageCode(String langCode) {
		String elements[] = langCode.split("_");
		if (elements.length != 2)
			return "ko"; // default language is korean
		return elements[0];
	}

	private String getCountryCode(String langCode) {
		String elements[] = langCode.split("_");
		if (elements.length != 2)
			return "KR"; // default language is korean
		return elements[1];
	}

	public synchronized static String getDefineWeb(String code) {
		try {
			if (msgProps == null) {
				String langCode = OBCommon.getLocalInfo();
				String language = new OBDefineWeb().getLanguageCode(langCode);
				String country = new OBDefineWeb().getCountryCode(langCode);
				String fileName = String.format(OBDefine.PROPERTIES_MESSAGES, language, country);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(new FileInputStream(OBUtility.getPropOsPath() + fileName), "UTF-8"));
				msgProps = new Properties();
				log.debug("{}", msgProps);
				msgProps.load(in);
				log.debug("{}", msgProps.size());
			}
			String retVal = msgProps.getProperty(code).trim();
			if (retVal == null) {
				log.debug("{}", code);
				return "not defined";
			}
			return retVal;
		} catch (Exception e) {
			log.debug("{}", code);
			return "not defined";
		}
	}

	public OBDefineWeb() {
	}
}
