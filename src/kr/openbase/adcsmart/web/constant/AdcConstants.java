package kr.openbase.adcsmart.web.constant;

public class AdcConstants {
	// Common
	public static final String SELECTION_ALL = "all";
	public static final String SELECTION_GROUP = "group";
	public static final String SELECTION_ADC = "adc";
	public static final String STATUS_INIT = "등록";
	public static final String STATUS_CREATING = "진행";
	public static final String STATUS_COMPLETE = "완료";
	public static final String STATUS_FAIL = "실패";
	
	public static final String STATUS_CANCEL = "취소";
	public static final String STATUS_STOP = "중지";
	public static final String STATUS_ING = "진행중";
	public static final String STATUS_DELETE = "삭제";
	
	public static final String STATUS_NORMAL = "정상";
	public static final String STATUS_ABNORMAL = "비정상";
	public static final String STATUS_ISOLATION = "단절";
	public static final String STATUS_OFF = "꺼짐";
	public static final String STATUS_DISCONN = "연결끊김";
	//ssss
	// Report
	public static final String RPT_TYPE_SYSADMIN = "sysAdminReport";
	public static final String RPT_TYPE_SYSADMIN_ko = "시스템 운영 보고서";
	
	public static final String RPT_TYPE_SYSADMIN_TOTAL = "sysAdminTotalReport";
	public static final String RPT_TYPE_SYSADMIN_TOTAL_ko = "전체 운영 보고서";
	
	public static final String RPT_TYPE_SYSFAULT = "sysFalultReport";
	public static final String RPT_TYPE_SYSFAULT_ko = "장애 분석 보고서";
	
	public static final String RPT_TYPE_UNKNOWN = "unknownReport";
	
	public static final String RPT_TYPE_L4DAILY = "l4OpDailyReport";
	public static final String RPT_TYPE_L4DAILY_ko = "L4운영보고서(일간)";
	
	public static final String RPT_TYPE_L4WEEKLY = "l4OpWeeklyReport";
	public static final String RPT_TYPE_L4WEEKLY_ko = "L4운영보고서(주간)";
	
	public static final String RPT_TYPE_L4MONTHLY = "l4OpMonthlyReport";
	public static final String RPT_TYPE_L4MONTHLY_ko = "L4운영보고서(월간)";
	
	public static final String RPT_TYPE_L4OPERATION = "l4OperationReport";
	public static final String RPT_TYPE_L4OPERATION_ko = "L4운영 보고서";
	
	public static final String RPT_PERIOD_PREVIOUSDATE = "previousDate";
	public static final String RPT_PERIOD_CUSTUM = "custom";
	public static final String RPT_OUT_PDF = "pdf";
	public static final String RPT_OUT_RTF = "rtf";
	public static final String RPT_OUT_PPTX = "pptx";
	public static final String RPT_FILENAME_SYSADMIN = "SystemAdmin.jasper";
	public static final String RPT_FILENAME_SYSFAULT = "SystemFault.jasper";
	public static final String RPT_FILENAME_L4OPERATION= "L4Operation.jasper";
	// Backup
	/*
	public static final String BAK_TARGET_ALL = "all";
	public static final String BAK_TARGET_ALL_ko = "전체";
	public static final String BAK_TARGET_ADCSETTINGS = "adcSettings";
	public static final String BAK_TARGET_ADCSETTINGS_ko = "ADC 설정 정보";
	public static final String BAK_TARGET_ADCLOGS = "adcLogs";
	public static final String BAK_TARGET_ADCLOGS_ko = "ADC 로그 정보";
	public static final String BAK_TARGET_UNKNOWN = "unknownTarget";
	*/
	/*
	   전체 =-> ADCSmart 설정&로그 정보			0
	 ADC 설정 정보 =-> ADCSmart 설정 정보		1
	 ADC 로그 정보 =-> ADCSmart 로그 정보		2
	   신규 =-> ADC 설정 정보					3
	*/
	public static final String BAK_TARGET_ALL = "all";
	public static final String BAK_TARGET_ALL_ko = "ADCsmart 설정&로그 정보";
	public static final String BAK_TARGET_ADCSMARTSETTINGS = "adcsmart Settings";
	public static final String BAK_TARGET_ADCSMARTSETTINGS_ko = "ADCsmart 설정 정보";
	public static final String BAK_TARGET_ADCSMARTLOGS = "adcsmart";
	public static final String BAK_TARGET_ADCSMARTLOGS_ko = "ADCsmart 로그 정보";
	public static final String BAK_TARGET_ADCSETTINGS = "ADC Settings";
	public static final String BAK_TARGET_ADCSETTINGS_ko = "ADC 설정 정보";
	public static final String BAK_TARGET_UNKNOWN = "unknownTarget";
	// Monitoring
	public static final String SEARCH_TIME_HOURS_MODE = "hoursMode";
	public static final String SEARCH_TIME_PERIOD_MODE = "periodMode";
	
	/****license 정보                    2013.06.12  김연주 ****/
	
	// OBDtoLicense.java
	public static final String LIC_SUCCESS = "정상"; //0
	public static final String LIC_FORMAT_ERROR = "포맷 오류"; //1
	public static final String LIC_FILE_ERROR = "파일 오류"; //2
	public static final String LIC_INTEGRITY_ERROR = "무결성 오류"; //3
	public static final String LIC_TYPE_ERROR = "타입 오류"; //4
	public static final String LIC_VERSION_ERROR = "버전 오류"; //5
	public static final String LIC_IP_ERROR = "IP 오류"; //6
	public static final String LIC_MAC_ERROR = "MAC 오류"; //7
	public static final String LIC_TIME_ERROR = "시간 만료"; //8
	public static final String LIC_ARGUMENT_ERROR = "인자 오류"; //9
	public static final String LIC_UNKOWN = "비정상"; //10
	
	/*** 세션 검색 정보 ***/
	public static final String OPTION_TYPE_SRC_IP = "Source IP";
	public static final String OPTION_TYPE_SRC_PORT = "Source Port";
	public static final String OPTION_TYPE_CLIENT_IP = "Client IP";
	public static final String OPTION_TYPE_CLIENT_PORT = "Client Port";
	public static final String OPTION_TYPE_DST_IP = "Destination IP";
	public static final String OPTION_TYPE_VIRTUAL_IP = "Virtual IP";
	public static final String OPTION_TYPE_DST_PORT = "Destination Port";
	public static final String OPTION_TYPE_VIRTUAL_PORT = "Virtual Port";
	public static final String OPTION_TYPE_PROTOCOL = "Protocol";
	public static final String OPTION_TYPE_HOST = "Host";
	
	//진단설정 예약정보	
	public static final String DIAGNOSIS_SCHEDULE = "(예약)";
	
	public static final String SCHEDULE_MONTH = "월";
	public static final String SCHEDULE_DAY = "일";
	public static final String SCHEDULE_HOUR = "시";
	public static final String SCHEDULE_MINUTE = "분";
	
	public static final String OPTION_PERIOD_NONE = "사용안함";
	public static final String OPTION_PERIOD_EVERYDAY = "매일";
	public static final String OPTION_PERIOD_EVERYWEEK = "매주";
	public static final String OPTION_PERIOD_EVERYMONTH = "매월";
	public static final String OPTION_PERIOD_ONCE = "한번만";
	
	public static final String OPTION_WEEK_SUN = "일요일";
	public static final String OPTION_WEEK_MON = "월요일";
	public static final String OPTION_WEEK_TUE = "화요일";
	public static final String OPTION_WEEK_WED = "수요일";
	public static final String OPTION_WEEK_THU = "목요일";
	public static final String OPTION_WEEK_FRI = "금요일";
	public static final String OPTION_WEEK_SAT = "토요일";
}
