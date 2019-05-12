/*
 * 시스템 감사로그를 추가한다.
 */
package kr.openbase.adcsmart.service;

import kr.openbase.adcsmart.service.utility.OBException;

public interface OBSystemAudit {
	// 용어
	public static final String ADC_LIST = "ADC 장비 목록";
	public static final String SYSLOG_FILGER = "syslog 필터";

	// SLB 설정(1~999)
	// 감사로그 구성 및 각 필드 별 의미
	// code, type, level, conent
	// type은 다음과 같다. - 1=SLB 설정, 2=시스템 설정, 3=로그인/로그아웃, 4=운영로그, 999: 기타

	/**
	 * ADC를 새롭게 추가한다.
	 */
	public static final String AUDIT_ADC_ADD_SUCCESS[] = { "1", "1", "INFO", "AUDIT_ADC_ADD_SUCCESS" };// ADC 추가에
																										// 성공했습니다. (ADC
																										// 이름:%s)"};
	public static final String AUDIT_ADC_ADD_FAIL[] = { "2", "4", "INFO", "AUDIT_ADC_ADD_FAIL" };
	/**
	 * 추가된 ADC를 삭제한다.
	 */
	public static final String AUDIT_ADC_DEL_SUCCESS[] = { "3", "1", "INFO", "AUDIT_ADC_DEL_SUCCESS" };
	public static final String AUDIT_ADC_DEL_FAIL[] = { "4", "4", "INFO", "AUDIT_ADC_DEL_FAIL" };
	/**
	 * 생성된 ADC 속성을 변경한다.
	 */
	public static final String AUDIT_ADC_SET_SUCCESS[] = { "5", "1", "INFO", "AUDIT_ADC_SET_SUCCESS" };
	public static final String AUDIT_ADC_SET_FAIL[] = { "6", "4", "INFO", "AUDIT_ADC_SET_FAIL" };

	/**
	 * ADC의 공지 그룹을 설정한다.
	 */
	public static final String AUDIT_ADC_SET_NOTICE_GROUP_SUCCESS[] = { "7", "4", "INFO",
			"AUDIT_ADC_SET_NOTICE_GROUP_SUCCESS" };
	public static final String AUDIT_ADC_SET_NOTICE_GROUP_FAIL[] = { "8", "4", "INFO",
			"AUDIT_ADC_SET_NOTICE_GROUP_FAIL" };

	/**
	 * ADC 그룹을 새롭게 생성한다.
	 */
	public static final String AUDIT_ADCGROUP_ADD_SUCCESS[] = { "10", "1", "INFO", "AUDIT_ADCGROUP_ADD_SUCCESS" };
	/**
	 * 생성된 ADC 그룹을 삭제한다.
	 */
	public static final String AUDIT_ADCGROUP_DEL_SUCCESS[] = { "11", "1", "INFO", "AUDIT_ADCGROUP_DEL_SUCCESS" };

	/**
	 * Virtual server을 추가한다.
	 */
	public static final String AUDIT_VSERVER_ADD_SUCCESS[] = { "20", "1", "INFO", "AUDIT_VSERVER_ADD_SUCCESS" };
	/**
	 * virtual server를 삭제한다.
	 */
	public static final String AUDIT_VSERVER_DEL_SUCCESS[] = { "21", "1", "INFO", "AUDIT_VSERVER_DEL_SUCCESS" };
	/**
	 * virtual server의 속성을 변경한다.
	 */
	public static final String AUDIT_VSERVER_SET_SUCCESS[] = { "22", "1", "INFO", "AUDIT_VSERVER_SET_SUCCESS" };

	/**
	 * virtual server의 Pool Member를 활성화(Enabled)한다.
	 */
	public static final String AUDIT_POOLMEMBER_ENABLE_SUCCESS[] = { "23", "1", "INFO",
			"AUDIT_POOLMEMBER_ENABLE_SUCCESS" };
	/**
	 * virtual server의 Pool Member를 비활성화(Disabled)한다.
	 */
	public static final String AUDIT_POOLMEMBER_DISABLE_SUCCESS[] = { "24", "1", "INFO",
			"AUDIT_POOLMEMBER_DISABLE_SUCCESS" };

	public static final String AUDIT_VSERVER_ENABLE_SUCCESS[] = { "25", "1", "INFO", "AUDIT_VSERVER_ENABLE_SUCCESS" };
	public static final String AUDIT_VSERVER_DISABLE_SUCCESS[] = { "26", "1", "INFO", "AUDIT_VSERVER_DISABLE_SUCCESS" };

	/**
	 * F5용 profile을 추가한다.
	 */
	public static final String AUDIT_PROFILE_ADD_SUCCESS[] = { "30", "1", "INFO", "AUDIT_PROFILE_ADD_SUCCESS" };
	/**
	 * F5용 profile을 삭제한다.
	 */
	public static final String AUDIT_PROFILE_DEL_SUCCESS[] = { "31", "1", "INFO", "AUDIT_PROFILE_DEL_SUCCESS" };
	/**
	 * F5용 profile의 속성을 변경한다.
	 */
	public static final String AUDIT_PROFILE_SET_SUCCESS[] = { "32", "1", "INFO", "AUDIT_PROFILE_SET_SUCCESS" };

	/**
	 * real server(node) 상태 변경
	 */
	public static final String AUDIT_RSERVER_STATE_SET_SUCCESS[] = { "40", "1", "INFO",
			"AUDIT_RSERVER_STATE_SET_SUCCESS" };
	public static final String AUDIT_RSERVER_STATE_SET_FAIL[] = { "41", "1", "INFO", "AUDIT_RSERVER_STATE_SET_FAIL" };

	/**
	 * virtual server 공지그룹을 변경한다.
	 */
	public static final String AUDIT_VSERVER_NOTICE_SET_SUCCESS[] = { "50", "1", "INFO",
			"AUDIT_VSERVER_NOTICE_SET_SUCCESS" };
	public static final String AUDIT_VSERVER_NOTICE_SET_FAIL[] = { "51", "1", "INFO", "AUDIT_VSERVER_NOTICE_SET_FAIL" };
	public static final String AUDIT_VSERVER_NOTICE_REVERT_SUCCESS[] = { "52", "1", "INFO",
			"AUDIT_VSERVER_NOTICE_REVERT_SUCCESS" };
	public static final String AUDIT_VSERVER_NOTICE_REVERT_FAIL[] = { "53", "1", "INFO",
			"AUDIT_VSERVER_NOTICE_REVERT_FAIL" };

	public static final String AUDIT_INVALID_ADCINFO_PEERIP[] = { "101", "1", "WARN", "AUDIT_INVALID_ADCINFO_PEERIP" };

	// 시스템 설정(1000~1999)
	/**
	 * 사용자 계정을 추가한다.
	 */
	public static final String AUDIT_ACCOUNT_ADD_SUCCESS[] = { "1001", "2", "INFO", "AUDIT_ACCOUNT_ADD_SUCCESS" };
	public static final String AUDIT_ACCOUNT_ADD_FAIL[] = { "1002", "2", "ERROR", "AUDIT_ACCOUNT_ADD_FAIL" };
	/**
	 * 사용자 계정을 삭제한다.
	 */
	public static final String AUDIT_ACCOUNT_DEL_SUCCESS[] = { "1003", "2", "INFO", "AUDIT_ACCOUNT_DEL_SUCCESS" };
	public static final String AUDIT_ACCOUNT_DEL_FAIL[] = { "1004", "2", "ERROR", "AUDIT_ACCOUNT_DEL_FAIL" };
	/**
	 * 사용자 계정의 속성을 변경한다.
	 */
	public static final String AUDIT_ACCOUNT_SET_SUCCESS[] = { "1005", "2", "INFO", "AUDIT_ACCOUNT_SET_SUCCESS" };
	public static final String AUDIT_ACCOUNT_SET_FAIL[] = { "1006", "2", "ERROR", "AUDIT_ACCOUNT_SET_FAIL" };
	public static final String AUDIT_ACCOUNT_PASSWD_SET_SUCCESS[] = { "1007", "2", "INFO",
			"AUDIT_ACCOUNT_PASSWD_SET_SUCCESS" };
	public static final String AUDIT_ACCOUNT_PASSWD_SET_FAIL[] = { "1008", "2", "ERROR",
			"AUDIT_ACCOUNT_PASSWD_SET_FAIL" };
	public static final String AUDIT_ACCOUNT_PASSWD_RESET_SUCCESS[] = { "1009", "2", "INFO",
			"AUDIT_ACCOUNT_PASSWD_RESET_SUCCESS" };
	public static final String AUDIT_ACCOUNT_PASSWD_RESET_FAIL[] = { "1010", "2", "ERROR",
			"AUDIT_ACCOUNT_PASSWD_RESET_FAIL" };

	/**
	 * 사용자 그룹 속성 관련.
	 */
	public static final String AUDIT_ACCNTGROUP_ADD_SUCCESS[] = { "1011", "2", "INFO", "AUDIT_ACCNTGROUP_ADD_SUCCESS" };
	public static final String AUDIT_ACCNTGROUP_ADD_FAIL[] = { "1012", "2", "ERROR", "AUDIT_ACCNTGROUP_ADD_FAIL" };

	public static final String AUDIT_ACCNTGROUP_SET_SUCCESS[] = { "1013", "2", "INFO", "AUDIT_ACCNTGROUP_SET_SUCCESS" };
	public static final String AUDIT_ACCNTGROUP_SET_FAIL[] = { "1014", "2", "ERROR", "AUDIT_ACCNTGROUP_SET_FAIL" };

	public static final String AUDIT_ACCNTGROUP_DEL_SUCCESS[] = { "1015", "2", "INFO", "AUDIT_ACCNTGROUP_DEL_SUCCESS" };
	public static final String AUDIT_ACCNTGROUP_DEL_FAIL[] = { "1016", "2", "ERROR", "AUDIT_ACCNTGROUP_DEL_FAIL" };

	/**
	 * 시스템 일반 설정을 변경한다.
	 */
	public static final String AUDIT_SYSCONFIG_SET[] = { "1020", "2", "INFO", "AUDIT_SYSCONFIG_SET" };

	// 로그인/로그아웃(2000~2999)
	/**
	 * 로그인에 성공했습니다..
	 */
	public static final String AUDIT_LOGIN_SUCCESS[] = { "2000", "3", "INFO", "AUDIT_LOGIN_SUCCESS" };
	public static final int AUDIT_LOGIN_SUCCESS_CODE = 2000;

	/**
	 * 로그인에 실패했습니다.
	 */
	public static final String AUDIT_LOGIN_FAIL[] = { "2001", "3", "WARN", "AUDIT_LOGIN_FAIL" };
	public static final int AUDIT_LOGIN_FAIL_CODE = 2001;

	/**
	 * 로그아웃에 성공했습니다.
	 */
	public static final String AUDIT_LOGOUT_SUCCESS[] = { "2002", "3", "INFO", "AUDIT_LOGOUT_SUCCESS" };
	public static final int AUDIT_LOGOUT_SUCCESS_CODE = 2002;
	/**
	 * 로그아웃에 실패했습니다.
	 */
	public static final String AUDIT_LOGOUT_FAIL[] = { "2003", "3", "WARN", "AUDIT_LOGOUT_FAIL" };
	public static final int AUDIT_LOGOUT_FAIL_CODE = 2003;

	// 시스템 운영로그(3000~3999)
	/**
	 * db 연결 도중 오류이다.
	 */
	public static final String AUDIT_DB_CONNECTION_FAIL[] = { "3000", "4", "ERROR", "AUDIT_DB_CONNECTION_FAIL" };
	/**
	 * db 조회 오류이다.
	 */
	public static final String AUDIT_DB_QUERY_FAIL[] = { "3001", "4", "ERROR", "AUDIT_DB_QUERY_FAIL" };

	/**
	 * ADC 연결 오류이다. 연결 가능한지 확인이 필요하다.
	 */
	public static final String AUDIT_ADC_CONNECTION_FAIL[] = { "3010", "4", "ERROR", "AUDIT_ADC_CONNECTION_FAIL" };
	/**
	 * ADC로부터 데이터 조회 오류이다. 단위 테스트가 필요함.
	 */
	public static final String AUDIT_ADC_QUERY_FAIL[] = { "3011", "4", "ERROR", "AUDIT_ADC_QUERY_FAIL" };
	/**
	 * ADC로부터 입력된 데이터 분석 오류이다.
	 */
	public static final String AUDIT_ADC_ANALYSIS_FAIL[] = { "3012", "4", "ERROR", "AUDIT_ADC_ANALYSIS_FAIL" };

	/**
	 * 시간이 동기화 되었습니다.
	 */
	public static final String AUDIT_TIME_SYNC_SUCESS[] = { "3020", "4", "INFO", "AUDIT_TIME_SYNC_SUCESS" };

	/**
	 * 시간 동기화에 실패했습니다.
	 */
	public static final String AUDIT_TIME_SYNC_FAIL[] = { "3021", "4", "WARN", "AUDIT_TIME_SYNC_FAIL" };

	public static final String AUDIT_DAEMON_START_SUCCESS[] = { "3030", "4", "INFO", "AUDIT_DAEMON_START_SUCCESS" };
	public static final String AUDIT_DAEMON_START_FAIL[] = { "3031", "4", "ERROR", "AUDIT_DAEMON_START_FAIL" };
	public static final String AUDIT_DAEMON_STOP_SUCCESS[] = { "3032", "4", "WARN", "AUDIT_DAEMON_STOP_SUCCESS" };

	public static final String AUDIT_LOG_WRITE_FAIL[] = { "3040", "4", "ERROR", "AUDIT_LOG_WRITE_FAIL" };

	public static final String AUDIT_ADC_SAVE_SUCCESS[] = { "3050", "4", "INFO", "AUDIT_ADC_SAVE_SUCCESS" };
	public static final String AUDIT_ADC_SAVE_FAIL[] = { "3051", "4", "ERROR", "AUDIT_ADC_SAVE_FAIL" };
	public static final String AUDIT_ADC_CONFIGSYNC_SUCCESS[] = { "3052", "4", "INFO", "AUDIT_ADC_CONFIGSYNC_SUCCESS" };
	public static final String AUDIT_ADC_CONFIGSYNC_FAIL[] = { "3053", "4", "ERROR", "AUDIT_ADC_CONFIGSYNC_FAIL" };

	public static final String AUDIT_GET_VSERVER_STATUS_FAIL[] = { "3101", "4", "ERROR",
			"AUDIT_GET_VSERVER_STATUS_FAIL" };
	public static final String AUDIT_GET_ADC_STATUS_FAIL[] = { "3102", "4", "ERROR", "AUDIT_GET_ADC_STATUS_FAIL" };
	public static final String AUDIT_GET_PORTINTERFACE_STATUS_FAIL[] = { "3103", "4", "ERROR",
			"AUDIT_GET_PORTINTERFACE_STATUS_FAIL" };
	public static final String AUDIT_GET_SYSTEMINFO_STATUS_FAIL[] = { "3104", "4", "ERROR",
			"AUDIT_GET_SYSTEMINFO_STATUS_FAIL" };
	public static final String AUDIT_GET_SLBCONFIG_FAIL[] = { "3105", "4", "ERROR", "AUDIT_GET_SLBCONFIG_FAIL" };
	public static final String AUDIT_GET_ADC_SYSTEM_RESC_FAIL[] = { "3106", "4", "ERROR",
			"AUDIT_GET_ADC_SYSTEM_RESC_FAIL" };
	public static final String AUDIT_GET_VSERVER_TRAFFIC_FAIL[] = { "3107", "4", "ERROR",
			"AUDIT_GET_VSERVER_TRAFFIC_FAIL" };

	public static final String AUDIT_SYSTEM_CONFIG_READ_FAIL[] = { "4000", "4", "ERROR",
			"AUDIT_SYSTEM_CONFIG_READ_FAIL" };

	// 라이센스 관련
	public static final String AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL[] = { "4101", "4", "WARN",
			"AUDIT_SYSTEM_LICENSE_FILENOTFOUNDL" };
	public static final String AUDIT_SYSTEM_LICENSE_EXPIRED[] = { "4102", "4", "WARN", "AUDIT_SYSTEM_LICENSE_EXPIRED" };
	public static final String AUDIT_SYSTEM_LICENSE_EXPIRED_ACCOUNT[] = { "4103", "4", "WARN",
			"AUDIT_SYSTEM_LICENSE_EXPIRED_ACCOUNT" };
	public static final String AUDIT_SYSTEM_LICENSE_EXPIRED_VIRTUALSERVER[] = { "4104", "4", "WARN",
			"AUDIT_SYSTEM_LICENSE_EXPIRED_VIRTUALSERVER" };
	public static final String AUDIT_SYSTEM_LICENSE_EXPIRED_ADC[] = { "4105", "4", "WARN",
			"AUDIT_SYSTEM_LICENSE_EXPIRED_ADC" };
	public static final String AUDIT_SYSTEM_LICENSE_UPDATE_SUCCESS[] = { "4106", "4", "INFO",
			"AUDIT_SYSTEM_LICENSE_UPDATE_SUCCESS" };
	public static final String AUDIT_SYSTEM_LICENSE_UPDATE_FAIL[] = { "4107", "4", "WARN",
			"AUDIT_SYSTEM_LICENSE_UPDATE_FAIL" };

	public static final String AUDIT_REPORT_ADD_SUCCESS[] = { "4201", "4", "INFO", "AUDIT_REPORT_ADD_SUCCESS" };
	public static final String AUDIT_REPORT_ADD_FAIL[] = { "4202", "4", "ERROR", "AUDIT_REPORT_ADD_FAIL" };
	public static final String AUDIT_REPORT_CREATE_SUCCESS[] = { "4203", "4", "INFO", "AUDIT_REPORT_CREATE_SUCCESS" };
	public static final String AUDIT_REPORT_CREATE_FAIL[] = { "4204", "4", "ERROR", "AUDIT_REPORT_CREATE_FAIL" };
	public static final String AUDIT_REPORT_DELETE_SUCCESS[] = { "4205", "4", "INFO", "AUDIT_REPORT_DELETE_SUCCESS" };
	public static final String AUDIT_REPORT_DELETE_FAIL[] = { "4206", "4", "ERROR", "AUDIT_REPORT_DELETE_FAIL" };
	public static final String AUDIT_REPORT_EXPORT_SUCCESS[] = { "4207", "4", "INFO", "AUDIT_REPORT_EXPORT_SUCCESS" };
	public static final String AUDIT_REPORT_EXPORT_FAIL[] = { "4208", "4", "ERROR", "AUDIT_REPORT_EXPORT_FAIL" };

	public static final String AUDIT_ENV_SET_IP_SUCCESS[] = { "4301", "4", "INFO", "AUDIT_ENV_SET_IP_SUCCESS" };
	public static final String AUDIT_ENV_SET_IP_FAIL[] = { "4302", "4", "ERROR", "AUDIT_ENV_SET_IP_FAIL" };
	public static final String AUDIT_ENV_SET_GATEWAY_SUCCESS[] = { "4303", "4", "INFO",
			"AUDIT_ENV_SET_GATEWAY_SUCCESS" };
	public static final String AUDIT_ENV_SET_GATEWAY_FAIL[] = { "4304", "4", "ERROR", "AUDIT_ENV_SET_GATEWAY_FAIL" };
	public static final String AUDIT_ENV_SET_HOSTNAME_SUCCESS[] = { "4305", "4", "INFO",
			"AUDIT_ENV_SET_HOSTNAME_SUCCESS" };
	public static final String AUDIT_ENV_SET_HOSTNAME_FAIL[] = { "4306", "4", "ERROR", "AUDIT_ENV_SET_HOSTNAME_FAIL" };
	public static final String AUDIT_ENV_SET_ADC_SYNC_TIME_SUCCESS[] = { "4307", "4", "INFO",
			"AUDIT_ENV_SET_ADC_SYNC_TIME_SUCCESS" };
	public static final String AUDIT_ENV_SET_ADC_SYNC_TIME_FAIL[] = { "4308", "4", "ERROR",
			"AUDIT_ENV_SET_ADC_SYNC_TIME_FAIL" };
	public static final String AUDIT_ENV_SET_TIME_SYNC_SUCCESS[] = { "4309", "4", "INFO",
			"AUDIT_ENV_SET_TIME_SYNC_SUCCESS" };
	public static final String AUDIT_ENV_SET_TIME_SYNC_FAIL[] = { "4310", "4", "ERROR",
			"AUDIT_ENV_SET_TIME_SYNC_FAIL" };
	public static final String AUDIT_ENV_SET_SYSLOG_PORT_SUCCESS[] = { "4311", "4", "INFO",
			"AUDIT_ENV_SET_SYSLOG_PORT_SUCCESS" };
	public static final String AUDIT_ENV_SET_SYSLOG_PORT_FAIL[] = { "4312", "4", "ERROR",
			"AUDIT_ENV_SET_SYSLOG_PORT_FAIL" };
	public static final String AUDIT_ENV_SET_SYSTEM_TIME_SUCCESS[] = { "4313", "4", "INFO",
			"AUDIT_ENV_SET_SYSTEM_TIME_SUCCESS" };
	public static final String AUDIT_ENV_SET_SYSTEM_TIME_FAIL[] = { "4314", "4", "ERROR",
			"AUDIT_ENV_SET_SYSTEM_TIME_FAIL" };
	public static final String AUDIT_ENV_SET_MAX_LOG_SUCCESS[] = { "4315", "4", "INFO",
			"AUDIT_ENV_SET_MAX_LOG_SUCCESS" };
	public static final String AUDIT_ENV_SET_MAX_LOG_FAILE[] = { "4316", "4", "ERROR", "AUDIT_ENV_SET_MAX_LOG_FAILE" };
	public static final String AUDIT_ENV_SET_MONITOR_TIME_SUCCESS[] = { "4317", "4", "INFO",
			"AUDIT_ENV_SET_MONITOR_TIME_SUCCESS" };
	public static final String AUDIT_ENV_SET_MONITOR_TIME_FAILE[] = { "4318", "4", "ERROR",
			"AUDIT_ENV_SET_MONITOR_TIME_FAILE" };
	public static final String AUDIT_ENV_SET_REFRESH_TIME_SUCCESS[] = { "4319", "4", "INFO",
			"AUDIT_ENV_SET_REFRESH_TIME_SUCCESS" };
	public static final String AUDIT_ENV_SET_REFRESH_TIME_FAIL[] = { "4320", "4", "ERROR",
			"AUDIT_ENV_SET_REFRESH_TIME_FAIL" };
	public static final String AUDIT_ENV_SET_LOGOUT_TIME_SUCCESS[] = { "4321", "4", "INFO",
			"AUDIT_ENV_SET_LOGOUT_TIME_SUCCESS" };
	public static final String AUDIT_ENV_SET_LOGOUT_TIME_FAIL[] = { "4322", "4", "ERROR",
			"AUDIT_ENV_SET_LOGOUT_TIME_FAIL" };
	public static final String AUDIT_ENV_ADD_ADCLOG_FILTER_SUCCESS[] = { "4323", "4", "INFO",
			"AUDIT_ENV_ADD_ADCLOG_FILTER_SUCCESS" };
	public static final String AUDIT_ENV_ADD_ADCLOG_FILTER_FAIL[] = { "4324", "4", "ERROR",
			"AUDIT_ENV_ADD_ADCLOG_FILTER_FAIL" };
	public static final String AUDIT_ENV_DEL_ADCLOG_FILTER_SUCCESS[] = { "4325", "4", "INFO",
			"AUDIT_ENV_DEL_ADCLOG_FILTER_SUCCESS" };
	public static final String AUDIT_ENV_DEL_ADCLOG_FILTER_FAIL[] = { "4326", "4", "ERROR",
			"AUDIT_ENV_DEL_ADCLOG_FILTER_FAIL" };
	public static final String AUDIT_ENV_ADD_DB_BACKUP_SUCCESS[] = { "4327", "4", "INFO",
			"AUDIT_ENV_ADD_DB_BACKUP_SUCCESS" };
	public static final String AUDIT_ENV_ADD_DB_BACKUP_FAIL[] = { "4328", "4", "ERROR",
			"AUDIT_ENV_ADD_DB_BACKUP_FAIL" };
	public static final String AUDIT_ENV_ADD_DB_RESTORE_SUCCESS[] = { "4329", "4", "INFO",
			"AUDIT_ENV_ADD_DB_RESTORE_SUCCESS" };
	public static final String AUDIT_ENV_ADD_DB_RESTORE_FAIL[] = { "4330", "4", "ERROR",
			"AUDIT_ENV_ADD_DB_RESTORE_FAIL" };
	public static final String AUDIT_ENV_SET_SCHBACKUP_SUCCESS[] = { "4331", "4", "INFO",
			"AUDIT_ENV_SET_SCHBACKUP_SUCCESS" };
	public static final String AUDIT_ENV_SET_SCHBACKUP_FAIL[] = { "4332", "4", "ERROR",
			"AUDIT_ENV_SET_SCHBACKUP_FAIL" };
	public static final String AUDIT_ENV_SND_SCHBACKUP_SUCCESS[] = { "4333", "4", "INFO",
			"AUDIT_ENV_SND_SCHBACKUP_SUCCESS" };
	public static final String AUDIT_ENV_SND_SCHBACKUP_FAIL[] = { "4334", "4", "ERROR",
			"AUDIT_ENV_SND_SCHBACKUP_FAIL" };
	public static final String AUDIT_ENV_SET_ALERT_ONOFF_SUCCESS[] = { "4335", "4", "INFO",
			"AUDIT_ENV_SET_ALERT_ONOFF_SUCCESS" };
	public static final String AUDIT_ENV_SET_ALERT_ONOFF_FAIL[] = { "4336", "4", "ERROR",
			"AUDIT_ENV_SET_ALERT_ONOFF_FAIL" };
	public static final String AUDIT_ENV_SET_ALTEON_AUTO_SAVE_SUCCESS[] = { "4337", "4", "INFO",
			"AUDIT_ENV_SET_ALTEON_AUTO_SAVE_SUCCESS" };
	public static final String AUDIT_ENV_SET_ALTEON_AUTO_SAVE_FAIL[] = { "4338", "4", "ERROR",
			"AUDIT_ENV_SET_ALTEON_AUTO_SAVE_FAIL" };
//	public static final String AUDIT_ENV_ADD_ADC_CONF_BACKUP_DONE[] = {"4339", "4", "INFO", "AUDIT_ENV_ADD_ADC_CONF_BACKUP_DONE"};
	public static final String AUDIT_ENV_ADD_ADC_CONF_BACKUP_SUCCESS[] = { "4340", "4", "INFO",
			"AUDIT_ENV_ADD_ADC_CONF_BACKUP_SUCCESS" };
	public static final String AUDIT_ENV_ADD_ADC_CONF_BACKUP_FAIL[] = { "4341", "4", "ERROR",
			"AUDIT_ENV_ADD_ADC_CONF_BACKUP_FAIL" };
	public static final String AUDIT_ENV_SET_LOGINACCESS_ONOFF_SUCCESS[] = { "4342", "4", "INFO",
			"AUDIT_ENV_SET_LOGINACCESS_ONOFF_SUCCESS" };
	public static final String AUDIT_ENV_SET_LOGINACCESS_ONOFF_FAIL[] = { "4343", "4", "ERROR",
			"AUDIT_ENV_SET_LOGINACCESS_ONOFF_FAIL" };

	public static final String AUDIT_ENV_SET_SVCRESPTIME_ONOFF_SUCCESS[] = { "4344", "4", "INFO",
			"AUDIT_ENV_SET_SVCRESPTIME_ONOFF_SUCCESS" };
	public static final String AUDIT_ENV_SET_SVCRESPTIME_ONOFF_FAIL[] = { "4345", "4", "ERROR",
			"AUDIT_ENV_SET_SVCRESPTIME_ONOFF_FAIL" };

	public static final String AUDIT_ENV_SET_PRIMARY_NTP_SUCCESS[] = { "4346", "4", "INFO",
			"AUDIT_ENV_SET_SVCRESPTIME_ONOFF_FAIL" };
	public static final String AUDIT_ENV_SET_SECONDARY_NTP_SUCCESS[] = { "4347", "4", "INFO",
			"AUDIT_ENV_SET_SECONDARY_NTP_SUCCESS" };
	public static final String AUDIT_ENV_SET_NTP_INTERVAL[] = { "4348", "4", "INFO", "AUDIT_ENV_SET_NTP_INTERVAL" };
	public static final String AUDIT_ENV_SET_SYSTEM_TIME_MANUALLY[] = { "4349", "4", "INFO",
			"AUDIT_ENV_SET_SYSTEM_TIME_MANUALLY" };
	public static final String AUDIT_ENV_SET_SYSTEM_TIME_NTP_SUCCESS[] = { "4350", "4", "INFO",
			"AUDIT_ENV_SET_SYSTEM_TIME_NTP_SUCCESS" };
	public static final String AUDIT_ENV_SET_SYSTEM_TIME_NTP_FAIL[] = { "4351", "4", "INFO",
			"AUDIT_ENV_SET_SYSTEM_TIME_NTP_FAIL" };

	public static final String AUDIT_ENV_SET_SNMPTRAP_ONOFF_SUCCESS[] = { "4352", "4", "INFO",
			"AUDIT_ENV_SET_SNMPTRAP_ONOFF_SUCCESS" };
	public static final String AUDIT_ENV_SET_SNMPTRAP_ONOFF_FAIL[] = { "4353", "4", "ERROR",
			"AUDIT_ENV_SET_SNMPTRAP_ONOFF_FAIL" };

	public static final String AUDIT_ENV_SET_SNMPCOMMUNITY_ONOFF_SUCCESS[] = { "4354", "4", "INFO",
			"AUDIT_ENV_SET_SNMPCOMMUNITY_ONOFF_SUCCESS" };
	public static final String AUDIT_ENV_SETSNMPCOMMUNITY_ONOFF_FAIL[] = { "4355", "4", "ERROR",
			"AUDIT_ENV_SETSNMPCOMMUNITY_ONOFF_FAIL" };

	public static final String AUDIT_ENV_SET_ALARMPOPUP_ONOFF_SUCCESS[] = { "4356", "4", "INFO",
			"AUDIT_ENV_SET_ALARMPOPUP_ONOFF_SUCCESS" };
	public static final String AUDIT_ENV_SET_ALARMPOPUP_ONOFF_FAIL[] = { "4356", "4", "ERROR",
			"AUDIT_ENV_SET_ALARMPOPUP_ONOFF_FAIL" };

	public static final String AUDIT_ENV_SET_SMSACTION_ONOFF_SUCCESS[] = { "4357", "4", "INFO",
			"AUDIT_ENV_SET_SMSACTION_ONOFF_SUCCESS" };
	public static final String AUDIT_ENV_SET_SMSACTION_ONOFF_FAIL[] = { "4357", "4", "ERROR",
			"AUDIT_ENV_SET_SMSACTION_ONOFF_FAIL" };

	public static final String AUDIT_SYSLOG_NOTDEF_ADC[] = { "5001", "4", "WARN", "AUDIT_SYSLOG_NOTDEF_ADC" };
	public static final String AUDIT_SYSLOG_INVALID_FORMAT[] = { "5002", "4", "WARN", "AUDIT_SYSLOG_INVALID_FORMAT" };
	public static final String AUDIT_SYSLOG_INVALID_PROCESS[] = { "5003", "4", "ERROR",
			"AUDIT_SYSLOG_INVALID_PROCESS" };

	public static final String AUDIT_FAULT_SAVE_TEMPLATE_SUCCESS[] = { "6001", "4", "INFO",
			"AUDIT_FAULT_SAVE_TEMPLATE_SUCCESS" };
	public static final String AUDIT_FAULT_SAVE_TEMPLATE_FAIL[] = { "6002", "4", "ERROR",
			"AUDIT_FAULT_SAVE_TEMPLATE_FAIL" };
	public static final String AUDIT_FAULT_SAVE_SCHEDULE_SUCCESS[] = { "6003", "4", "INFO",
			"AUDIT_FAULT_SAVE_SCHEDULE_SUCCESS" };
	public static final String AUDIT_FAULT_SAVE_SCHEDULE_FAIL[] = { "6004", "4", "ERROR",
			"AUDIT_FAULT_SAVE_SCHEDULE_FAIL" };
	public static final String AUDIT_FAULT_CHECK_START_SUCCESS[] = { "6005", "4", "INFO",
			"AUDIT_FAULT_CHECK_START_SUCCESS" };
	public static final String AUDIT_FAULT_CHECK_START_FAIL[] = { "6006", "4", "ERROR",
			"AUDIT_FAULT_CHECK_START_FAIL" };
	public static final String AUDIT_FAULT_CHECK_SUCCESS[] = { "6007", "4", "INFO", "AUDIT_FAULT_CHECK_SUCCESS" };
	public static final String AUDIT_FAULT_CHECK_FAIL[] = { "6008", "4", "ERROR", "AUDIT_FAULT_CHECK_FAIL" };
	public static final String AUDIT_FAULT_CHECK_FAIL2[] = { "6008", "4", "ERROR", "AUDIT_FAULT_CHECK_FAIL2" };
	public static final String AUDIT_FAULT_DELETE_TEMPLATE_SUCCESS[] = { "6009", "4", "INFO",
			"AUDIT_FAULT_DELETE_TEMPLATE_SUCCESS" };
	public static final String AUDIT_FAULT_DELETE_TEMPLATE_FAIL[] = { "6010", "4", "ERROR",
			"AUDIT_FAULT_DELETE_TEMPLATE_FAIL" };
	public static final String AUDIT_DYNAMIC_DASHBOARD_SET_SUCCESS[] = { "6011", "4", "INFO",
			"AUDIT_DYNAMIC_DASHBOARD_SET_SUCCESS" };
	public static final String AUDIT_DYNAMIC_DASHBOARD_SET_FAIL[] = { "6012", "4", "ERROR",
			"AUDIT_DYNAMIC_DASHBOARD_SET_FAIL" };
	public static final String AUDIT_DYNAMIC_DASHBOARD_DEL_SUCCESS[] = { "6013", "4", "INFO",
			"AUDIT_DYNAMIC_DASHBOARD_DEL_SUCCESS" };
	public static final String AUDIT_DYNAMIC_DASHBOARD_DEL_FAIL[] = { "6014", "4", "ERROR",
			"AUDIT_DYNAMIC_DASHBOARD_DEL_FAIL" };
	public static final String AUDIT_DYNAMIC_DASHBOARD_ADD_SUCCESS[] = { "6015", "4", "INFO",
			"AUDIT_DYNAMIC_DASHBOARD_ADD_SUCCESS" };
	public static final String AUDIT_DYNAMIC_DASHBOARD_ADD_FAIL[] = { "6016", "4", "ERROR",
			"AUDIT_DYNAMIC_DASHBOARD_ADD_FAIL" };
	public static final String AUDIT_PKT_DUMP_START_SUCCESS[] = { "6017", "4", "INFO", "AUDIT_PKT_DUMP_START_SUCCESS" };
	public static final String AUDIT_PKT_DUMP_START_FAIL[] = { "6018", "4", "ERROR", "AUDIT_PKT_DUMP_START_FAIL" };
	public static final String AUDIT_PKT_DUMP_SUCCESS[] = { "6019", "4", "INFO", "AUDIT_PKT_DUMP_SUCCESS" };
	public static final String AUDIT_PKT_DUMP_FAIL[] = { "6020", "4", "ERROR", "AUDIT_PKT_DUMP_FAIL" };

	// 14.07.14 sw.jung: 패킷수집 취소, 삭제에 대한 감사로그 메세지 추가(중지의 경우 성공/실패 메세지 활용)
	public static final String AUDIT_FAULT_DELETE_SCHEDULE_SUCCESS[] = { "6021", "4", "INFO",
			"AUDIT_FAULT_DELETE_SCHEDULE_SUCCESS" };
	public static final String AUDIT_FAULT_DELETE_SCHEDULE_FAIL[] = { "6022", "4", "ERROR",
			"AUDIT_FAULT_DELETE_SCHEDULE_FAIL" };
	public static final String AUDIT_FAULT_DELETE_LIST_SUCCESS[] = { "6023", "4", "INFO",
			"AUDIT_FAULT_DELETE_LIST_SUCCESS" };
	public static final String AUDIT_FAULT_DELETE_LIST_FAIL[] = { "6024", "4", "ERROR",
			"AUDIT_FAULT_DELETE_LIST_FAIL" };
	public static final String AUDIT_PKT_DUMP_CANCEL_SUCCESS[] = { "6025", "4", "INFO",
			"AUDIT_PKT_DUMP_CANCEL_SUCCESS" };
	public static final String AUDIT_PKT_DUMP_CANCEL_FAIL[] = { "6026", "4", "ERROR", "AUDIT_PKT_DUMP_CANCEL_FAIL" };
	public static final String AUDIT_PKT_DUMP_DELETE_SUCCESS[] = { "6027", "4", "INFO",
			"AUDIT_PKT_DUMP_DELETE_SUCCESS" };
	public static final String AUDIT_PKT_DUMP_DELETE_FAIL[] = { "6028", "4", "ERROR", "AUDIT_PKT_DUMP_DELETE_FAIL" };
	public static final String AUDIT_PKT_DUMP_STOP[] = { "6029", "4", "INFO", "AUDIT_PKT_DUMP_STOP" };

	// 기타(9000~9999)
	// #3926-3 #6: 13.07.23 sw.jung 경보설정 감사로그 추가
	public static final String AUDIT_ALARM_UPDATE_SUCCESS[] = { "9000", "4", "INFO", "AUDIT_ALARM_UPDATE_SUCCESS" };
	public static final String AUDIT_ALARM_UPDATE_FAIL[] = { "9001", "4", "ERROR", "AUDIT_ALARM_UPDATE_FAIL" };
	public static final String AUDIT_ALARM_ACTION_SUCCESS[] = { "9002", "4", "INFO", "AUDIT_ALARM_ACTION_SUCCESS" };
	public static final String AUDIT_ALARM_ACTION_FAIL[] = { "9003", "4", "ERROR", "AUDIT_ALARM_ACTION_FAIL" };

	/**
	 * 시스템 감사로그를 추가한다.
	 * 
	 * @param accountIndex 현재 로그인한 사용자 계정의 Index
	 * @param clientIP     -- client ip주소.
	 * @param auditCode    -- 감사로그 코드.
	 * @throws OBException
	 */
//	public void writeLog(Integer accountIndex, String clientIP, int auditCode) throws OBException;

	/**
	 * 시스템 감사로그를 추가한다.
	 * 
	 * @param accountIndex -- 현재 로그인한 사용자 계정의 Index
	 * @param clientIP     -- client ip주소.
	 * @param auditCode    -- 감사로그 코드.
	 * @param extraMessage -- 추가적인 감사로그.
	 * @throws OBException
	 */
	public void writeLog(Integer accountIndex, String clientIP, int auditCode, String extraMessage) throws OBException;
}
