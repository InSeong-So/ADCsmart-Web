package kr.openbase.adcsmart.service.utility;

import java.util.LinkedHashMap;

public class OBDefine {
	public static final int SHOW_FLAG = 1;
	public static final int HIDE_FLAG = 2;

	public static final String PROPERTIES_PATH = "/opt/apache-tomcat/webapps/adcms/WEB-INF/classes/conf/";
	public static final String WIN_PROPERTIES_PATH = "/opt/apache-tomcat/webapps/adcms/WEB-INF/classes/conf/";
//	public static final String WIN_PROPERTIES_PATH = "c:/opt/apache-tomcat/webapps/adcms/WEB-INF/classes/conf/";
	public static final String PROPERTIES_BASE = "adcsmart.properties";
	public static final String PROPERTIES_MESSAGES = "Messages_%s_%s.properties";
	public static final String PROPERTIES_Errors = "Errors_%s_%s.properties";
	public static final String PROPERTIES_Freemarker = "Freemarker_%s_%s.properties";

	public static final String DIR_BASE_HOME = "/opt/adcsmart/";
	public static final String DIR_LOCKFILE = DIR_BASE_HOME + "lock/";
	public static final String DIR_LOCKFILE_SYSLOGD = DIR_LOCKFILE + "syslogd/";
	public static final String LOCAL_CONFIG = DIR_BASE_HOME + "cfg/local.conf";

	public static final String ADC_LOG_FILE_PATH = DIR_BASE_HOME + "logs/";
	public static final String DIR_EXCLUDE_VS = "/opt/adcsmart/cfg/excludevs.conf";

	public static final int TH_FIN = 0x01;
	public static final int TH_SYN = 0x02;
	public static final int TH_RST = 0x04;
	public static final int TH_PUSH = 0x08;
	public static final int TH_ACK = 0x10;
	public static final int TH_URG = 0x20;

	public final static String ACCOUNT_NAME_TCPDUMP01 = "tcpdump01";
	public final static String ACCOUNT_PASSWD_TCPDUMP01 = "tcpdump12#$";

//	public final static String     ALL_ADC    = "전체 그룹";

	public static final int FAULT_CHECK_ELEMENT_HW_POWER = 1;
	public static final int FAULT_CHECK_ELEMENT_HW_UPTIME = 2;
	public static final int FAULT_CHECK_ELEMENT_HW_LICENSE = 3;
	public static final int FAULT_CHECK_ELEMENT_HW_INTERFACE = 4;
	public static final int FAULT_CHECK_ELEMENT_HW_CPU = 5;
	public static final int FAULT_CHECK_ELEMENT_HW_MEMORY = 6;
	public static final int FAULT_CHECK_ELEMENT_HW_TEMPERATURE = 7;
	public static final int FAULT_CHECK_ELEMENT_HW_FAN = 8;
	public static final int FAULT_CHECK_ELEMENT_HW_ADCLOG = 9;
	public static final int FAULT_CHECK_ELEMENT_HW_OSINFO = 10;

	public static final int FAULT_CHECK_ELEMENT_L23_VLAN = 1;
	public static final int FAULT_CHECK_ELEMENT_L23_STP = 2;
	public static final int FAULT_CHECK_ELEMENT_L23_TRUNK = 3;
	public static final int FAULT_CHECK_ELEMENT_L23_VRRP = 4;
	public static final int FAULT_CHECK_ELEMENT_L23_ROUTING = 5;
	public static final int FAULT_CHECK_ELEMENT_L23_INTERFACE = 6;
	public static final int FAULT_CHECK_ELEMENT_L47_NOT_USED_CF = 1;
	public static final int FAULT_CHECK_ELEMENT_L47_SLEEP_VS = 2;
	public static final int FAULT_CHECK_ELEMENT_L47_SESSION_TABLE = 3;
//	public static final  int	FAULT_CHECK_ELEMENT_L47_SLEEP_POOL	=3;
//	public static final  int	FAULT_CHECK_ELEMENT_L47_SLEEP_NODE	=4;
	public static final int FAULT_CHECK_ELEMENT_SVC_PKTDUMP = 1;
	public static final int FAULT_CHECK_ELEMENT_SVC_DOWNLOAD = 2;
	public static final int FAULT_CHECK_ELEMENT_SVC_RESPONSE = 3;
	public static final int FAULT_CHECK_ELEMENT_SVC_PKTLOSS = 4;
//	public static final  int	FAULT_CHECK_ELEMENT_SVC_LOADBALANCE	=5;

//	public static final  String FAULT_DB_COLUMN_HW_CHECK            = "ITEM_HW_CHCK";
//	public static final  String FAULT_DB_COLUMN_HW_FAIL             = "ITEM_HW_FAIL";
//	public static final  String FAULT_DB_COLUMN_SVC_RATE            = "ITEM_SVC_RATE";
	public static final String PKT_DUMP_FILE_PATH = "/var/lib/adcsmart/pcap/";

	public static final int FAULT_CHECK_STATUS_NORMAL = 100;// 진단 결과 이상 무.
	public static final int FAULT_CHECK_STATUS_FAILURE = 101;// 진단 실패한 경우.
	public static final int FAULT_CHECK_STATUS_CANCEL = 102;// 진단 취소한 경우.
	public static final int FAULT_CHECK_STATUS_ABNORMAL = 100;// 진단 결과 이상 항목 발견. 진단 결과 이상 무와 값을 동일하게 처리.

	public static final int IS_REACHABLE_NOT = 0; // 연결테스트가 수행되지 않았을 때
	public static final int IS_REACHABLE_ALL = 1; // 모든 연결테스트가 수행되었을 때
	public static final int IS_REACHABLE_EXIST_FAIL = 2; // 연결테스트중 수행되지 않은 테스트가 존재할 때
	public static final int IS_REACHABLE_MONITORING = 3; // 모니터링모드 즉 네트워크접근, snmp 테스트만 할 때

	public static final int OP_MODE_MONITORING = 1; // 모니터링 모드
	public static final int OP_MODE_MONITORING_SET = 2; // 설정 모드
	public static final int OP_MODE_MONITORING_FAULT = 3; // 진단 모드

	public static final int SYSTEM_TIME_SYNC_TYPE_NONE = 0; // 설정안함
	public static final int SYSTEM_TIME_SYNC_TYPE_MAUALLY = 1; // 수동설정
	public static final int SYSTEM_TIME_SYNC_TYPE_NTP = 2; // NTP
	public static final int NTP_NONE = 0;
	public static final int NTP_USE = 1;

	public static final int LICENSE_VALID = 0; // 라이선스 유효
	public static final int LICENSE_INVALID_DATE = 1; // 라이선스 기한만료
	public static final int LICENSE_INVALID_MAC = 2; // 라이선스 MAC 오류
	public static final int LICENSE_INVALID_ADC_CNT = 3; // 라이선스 ADC 최대치 초과

	public static final int ELEMENT_TYPE_HW = 1;
	public static final int ELEMENT_TYPE_L23 = 2;
	public static final int ELEMENT_TYPE_L47 = 3;
	public static final int ELEMENT_TYPE_SVC = 4;

	public static final int ORDER_DIR_ASCEND = 1;
	public static final int ORDER_DIR_DESCEND = 2;

	public static final int ORDER_TYPE_DEFAULT = 0;
	public static final int ORDER_TYPE_VSNAME = 1;
	public static final int ORDER_TYPE_VSIPADDRESS = 2;
	public static final int ORDER_TYPE_PORT = 3;
	public static final int ORDER_TYPE_BPS = 4;
	public static final int ORDER_TYPE_PPS = 5;
	public static final int ORDER_TYPE_CPS = 6;
	public static final int ORDER_TYPE_VSSTATUS = 7;
	public static final int ORDER_TYPE_ADCNAME = 8;
	public static final int ORDER_TYPE_ADCIPADDRESS = 9;
	public static final int ORDER_TYPE_ADCSTATUS = 10;
	public static final int ORDER_TYPE_OCCURTIME = 11;
	public static final int ORDER_TYPE_SOLVEDTIME = 12;
	public static final int ORDER_TYPE_CONTENT = 13;
	public static final int ORDER_TYPE_NAME = 14;
	public static final int ORDER_TYPE_STATUS = 15;
	public static final int ORDER_TYPE_TYPE = 16;
	public static final int ORDER_TYPE_INDEX = 17;
	public static final int ORDER_TYPE_SERVICEPORT = 18;
	public static final int ORDER_TYPE_USERNAME = 19;
	public static final int ORDER_TYPE_IPADDRESS = 20;
	public static final int ORDER_TYPE_SEVERITY = 21;
	public static final int ORDER_TYPE_PRODUCTNAME = 22;
	public static final int ORDER_TYPE_VERSION = 23;
	public static final int ORDER_TYPE_CPUUSAGE = 24;
	public static final int ORDER_TYPE_MEMUSAGE = 25;
	public static final int ORDER_TYPE_MEMBERIPADDRESS = 26;
	public static final int ORDER_TYPE_INTERFACENAME = 27;
	public static final int ORDER_TYPE_PERIOD = 28;
	public static final int ORDER_TYPE_OPTION = 29;
	public static final int ORDER_TYPE_SIZE = 30;
	public static final int ORDER_TYPE_FIRST = 33;
	public static final int ORDER_TYPE_SECOND = 34;
	public static final int ORDER_TYPE_THIRD = 35;
	public static final int ORDER_TYPE_FOURTH = 36;
	public static final int ORDER_TYPE_FIFTH = 37;
	public static final int ORDER_TYPE_SIXTH = 38;
	public static final int ORDER_TYPE_SEVENTH = 39;
	public static final int ORDER_TYPE_RESPONSE = 40;

	public static final int ORDER_TYPE_SRC_IP = 40;
	public static final int ORDER_TYPE_SRC_PORT = 41;
	public static final int ORDER_TYPE_DST_IP = 42;
	public static final int ORDER_TYPE_DST_PORT = 43;
	public static final int ORDER_TYPE_PROTOCOL = 44;
	public static final int ORDER_TYPE_ADCACTIVEBACKUP = 45;
	public static final int ORDER_TYPE_STATE = 46;

	public static final int CHECK_ADC_NORMAL = 0;// 정상상태.
	public static final int CHECK_ADC_UNREACHABLE = 1;// unreachable
	public static final int CHECK_ADC_LOGINFAIL = 2;// login fail
	public static final int CHECK_ADC_SNMPCOMMERR = 3;// snmp community mismatching
	public static final int CHECK_ADC_VERSIONERR = 4;// not supported version. version mismatching
	public static final int CHECK_ADC_CLILOGINFAIL = 5;// CLI login fail

	public static final String PROPERTY_FILE_NAME = "adcsmart.properties";
	public static final String PROPERTY_KEY_PKT_COUNT = "pktdump.count";
	public static final String PROPERTY_KEY_PKT_LENGTH = "pktdump.length";
	public static final String PROPERTY_KEY_PKT_MAX_WAIT_MSEC = "pktdump.max.wait.msec";
	public static final String PROPERTY_KEY_PKT_MAX_START = "pktdump.max.start";
	public static final String PROPERTY_KEY_PKT_START = "pktdump.start";
	public static final String PROPERTY_KEY_UPTIME_MAX_DIFF_DATE = "uptime.max.diff.date";
	public static final String PROPERTY_KEY_TEMPERATURE_MAX = "temperature.max";
	public static final String PROPERTY_KEY_DWONLOAD_MAX_WAIT_100MSEC = "download.max.wait.100msec";
	public static final String PROPERTY_KEY_SYSTEM_CPU_MAX_USAGE = "system.cpu.max.usage";
	public static final String PROPERTY_KEY_SYSTEM_MEM_MAX_USAGE = "system.memory.max.usage";
	public static final String PROPERTY_KEY_SYSTEM_HDD_MAX_USAGE = "system.hdd.max.usage";
	public static final String PROPERTY_KEY_SYSTEM_FAN_MIN_RPM = "system.fan.min.rpm";
	public static final String PROPERTY_KEY_SYSTEM_FAN_MAX_RPM = "system.fan.max.rpm";
	public static final String PROPERTY_KEY_PKT_MAX_SIZE = "pktdump.max.size";

	public static final String PROPERTY_KEY_ADCMON_THREAD_POOL_MAX = "adcmon.thread.pool.max";
	public static final String PROPERTY_KEY_ADCMON_THREAD_POOL_MIN = "adcmon.thread.pool.min";
	public static final String PROPERTY_KEY_ADCMON_DB_POOL_MAX = "adcmon.db.pool.max";
	public static final String PROPERTY_KEY_ADCMON_CHECK_REDUNDANCY = "adcmon.check.redundancy";

	// ssh, telnet 접속시 사용되는 timeout value. 기본값: 5000(5초)
	public static final String PROPERTY_KEY_TELNET_CONNECT_TIMEOUT = "telnet.connect.timeout";
	public static final String PROPERTY_KEY_LOCAL_EXCEPT_IPADDR = "local.except.ipaddr";

	public static final String PROPERTY_KEY_FAULT_SESSION_MAX = "fault.session.max"; // 진단 세션 검색 최대 값

	public static final int MAX_EXPORT_COUNT = 1000000;// 내보내기 최대 개수.
	public static final int SYSENV_MAX_ADCLOG_EXPORT = 10000;// adc log 내보내기 최대 개수.

	public static final String defaultPassword = "adcsmart1234";

	public static final String[] MNG_TABLE_LIST = { "MNG_ACCNT", "MNG_ACCNT_ADC_MAP", "MNG_ACCNT_ROLE", "MNG_ADC",
			"MNG_ADC_GROUP", "MNG_ADCLOG_FILTER_PATTERN", "MNG_DB_BACKUP", "MNG_ENV_ADDITIONAL", "MNG_ENV_NETWORK",
			"MNG_ENV_VIEW", "MNG_SCHEDULE", "MNG_SNMPOID", "MNG_SYSLOG_FILTER", "MNG_TIME", "SYSTEM_VERSION",
			"TMP_L3_INTERFACE", "TMP_SLB_NODE", "TMP_SLB_POOL", "TMP_SLB_POOLMEMBER", "TMP_SLB_PROFILE",
			"TMP_SLB_VS_SERVICE", "TMP_SLB_VSERVER", "TMP_FAULT_LINK_INFO" };
	public static final String[][] LOG_TABLE_LIST = { { "LOG_ADC_FAULT", "LOG_ADC_FAULT_LOG_SEQ_SEQ" },
			{ "LOG_ADC_PERFORMANCE", "LOG_ADC_PERFORMANCE_INDEX_SEQ" },
			{ "LOG_ADC_SYSLOG", "LOG_ADC_SYSLOG_LOG_SEQ_SEQ" },
			{ "LOG_CONFIG_HISTORY", "LOG_CONFIG_HISTORY_LOG_SEQ_SEQ" },
			{ "LOG_DISABLE_ALERT", "LOG_DISABLE_ALERT_INDEX_SEQ" },
			{ "LOG_PROFILE_CONFIG", "LOG_PROFILE_CONFIG_INDEX_SEQ" }, { "LOG_REPORT", "" },
			{ "LOG_SYSTEM_AUDIT", "LOG_SYSTEM_AUDIT_LOG_SEQ_SEQ" },
			{ "LOG_SYSTEM_RESOURCES", "LOG_SYSTEM_RESOURCES_INDEX_SEQ" } };

	public static final int DATA_NOT_EXIST = -1; // 트래픽,connection,pps 등 양수인 수량 값이 수집되지 않아 존재하지 않음을 DB에 표시하는 값. 수집했는데 0인
													// 것과는 다르다. 않음을 나타낸다.

	public static final int NTP_STATE_ENABLED = 1;//
	public static final int NTP_STATE_DISABLED = 2;//

	public static final int SYS_CPU_TYPE_MP = 1;//
	public static final int SYS_CPU_TYPE_SP = 2;//
	public static final int SYS_MEM_TYPE_MP = 1;//
	public static final int SYS_MEM_TYPE_SP = 2;//
	public static final int SYS_MEM_TYPE_TMM = 3;//

	public static final int SYS_FAN_STATUS_OK = 1;//
	public static final int SYS_FAN_STATUS_FAIL = 2;//
	public static final int SYS_FAN_STATUS_NOTPRESENT = 3;//

	public static final int SYS_POWERSUPPLY_STATUS_OK = 1;//
	public static final int SYS_POWERSUPPLY_STATUS_FAIL = 2;//
	public static final int SYS_POWERSUPPLY_STATUS_NOTPRESENT = 3;//

	public static final int L4_DIRECT_ENABLED = 1;//
	public static final int L4_DIRECT_DISABLED = 2;//

	public static final int L4_PM_STATUS_BLOCKED = 1;//
	public static final int L4_PM_STATUS_RUNNING = 2;//
	public static final int L4_PM_STATUS_FAILED = 3;//
	public static final int L4_PM_STATUS_DISABLE = 4;//
	public static final int L4_PM_STATUS_SLOWSTART = 5;//

	public static final int L3_GW_STATUS_UP = 1;//
	public static final int L3_GW_STATUS_FAILED = 2;//

	public static final int L2_LINK_STATUS_UP = 1;//
	public static final int L2_LINK_STATUS_DOWN = 2;//
	public static final int L2_LINK_STATUS_DISABLED = 3;//
	public static final int L2_LINK_STATUS_INOPERATIVE = 4;//

	public static final int L2_VLAN_STATE_ENABLED = 1;//
	public static final int L2_VLAN_STATE_DISABLED = 2;//

	public static final int L2_STP_STATE_ENABLED = 1;//
	public static final int L2_STP_STATE_DISABLED = 2;//

	public static final int L2_TRUNK_STATE_ENABLED = 1;//
	public static final int L2_TRUNK_STATE_DISABLED = 2;//
	public static final int L2_TRUNK_STATUS_UP = 0;//
	public static final int L2_TRUNK_STATUS_DOWN = 1;//
	public static final int L2_TRUNK_STATUS_DISABLED = 2;//
	public static final int L2_TRUNK_STATUS_UNINIT = 3;//
	public static final int L2_TRUNK_STATUS_LOOPBACK = 4;//
	public static final int L2_TRUNK_STATUS_UNPOPULATED = 5;//

	public static final int L2_STG_STATUS_DISABLED = 1;// disabled:1, blocking:2, listening:3, learning:4, forwarding:5,
														// broken:6, discarding:7
	public static final int L2_STG_STATUS_BLOCKING = 2;
	public static final int L2_STG_STATUS_LISTENING = 3;
	public static final int L2_STG_STATUS_LEARNING = 4;
	public static final int L2_STG_STATUS_FORWARDING = 5;
	public static final int L2_STG_STATUS_BROKEN = 6;
	public static final int L2_STG_STATUS_DISCARDING = 7;
	public static final int L2_STG_STATUS_DETACH = 8;

	public class HEALTH_CHECK {
		public static final int NONE = 0;
		public static final int NA = 0;
		public static final String NA_STR = "-";
		public static final String NA_VIEW = "-";

		public static final int TCP = 1;
		public static final String TCP_STR = "tcp";
		public static final String TCP_VIEW = "TCP";

		public static final int HTTP = 2;
		public static final String HTTP_STR = "http";
		public static final String HTTP_VIEW = "HTTP";

		public static final int HTTPS = 3;
		public static final String HTTPS_STR = "https";
		public static final String HTTPS_VIEW = "HTTPS";

		public static final int UDP = 4;
		public static final String UDP_STR = "udp";
		public static final String UDP_VIEW = "UDP";

		public static final int ICMP = 5;
		public static final String ICMP_STR = "icmp";
		public static final String ICMP_VIEW = "ICMP";

		public static final int GATEWAY_ICMP = 6;
		public static final String GATEWAY_ICMP_STR = "gateway_icmp";
		public static final String GATEWAY_ICMP_VIEW = "GATEWAY_ICMP";

		public static final int ARP = 7;
		public static final String ARP_STR = "arp";
		public static final String ARP_VIEW = "ARP";

		public static final int LINK = 8;
		public static final String LINK_STR = "link";
		public static final String LINK_VIEW = "LINK";

	}

	public static final int PROTOCOL_NA = 0; // 할당안됨 의미, 원래 -1인데 그러면 화면에서 "-" 대체 처리를 모두 해 줘야 해서 당분간 0으로 쓴다.
	public static final int PROTOCOL_TCP = 6;
	public static final int PROTOCOL_UDP = 17;
	public static final int PROTOCOL_ICMP = 1;
	public static final int PROTOCOL_ALL = 0;
	public static final int PROTOCOL_ETC = 256; // 255까지 reserve 영역

	public static final String PROTOCOL_NA_STRING = "-";
	public static final String PROTOCOL_TCP_STRING = "tcp";
	public static final String PROTOCOL_UDP_STRING = "udp";
	public static final String PROTOCOL_ICMP_STRING = "icmp";
	public static final String PROTOCOL_ALL_STRING = "all";
	public static final String PROTOCOL_ETC_STRING = "etc";

	public static final int PORT_NA = 0; // 할당안됨 의미, 원래 -1인데 그러면 화면에서 "-" 대체 처리를 모두 해 줘야 해서 당분간 0으로 쓴다.
	public static final String PORT_NA_STR = "-";

	public static final int LOG_LEVEL_INFO = 1;
	public static final int LOG_LEVEL_WARNING = 2;
	public static final int LOG_LEVEL_RISK = 3;
	public static final int LOG_LEVEL_ERROR = 4;

	public static final int LOG_TYPE_SLB = 1; // 외부에는 LOG_TYPE_SYSTEM과 똑같이 표시중
	public static final int LOG_TYPE_CONFIG = 2;
	public static final int LOG_TYPE_LOGINOUT = 3;
	public static final int LOG_TYPE_SYSTEM = 4;
	public static final int LOG_TYPE_OTHERS = 999;

	public static final int PORT_SPEED_10 = 10;
	public static final int PORT_SPEED_100 = 100;
	public static final int PORT_SPEED_1000 = 1000;
	public static final int PORT_SPEED_0 = 0;// any

	public static final int DUPLEX_NONE = 0;
	public static final int DUPLEX_HALF = 1;
	public static final int DUPLEX_FULL = 2;
	public static final String DUPLEX_NONE_STR = "any";
	public static final String DUPLEX_HALF_STR = "half";
	public static final String DUPLEX_FULL_STR = "full";

	public static final int ACCNT_ROLE_ADMIN = 1;
	public static final int ACCNT_ROLE_CONFIG = 2;
	public static final int ACCNT_ROLE_READONLY = 3;
	public static final int ACCNT_ROLE_VSADMIN = 4;
	public static final int ACCNT_ROLE_RSADMIN = 5;
	public static final String ACCNT_STRING_RSADMIN = "rsAdmin";

	public static final String MNG_TIME_TYPE_ADC = "ADC";
	public static final String MNG_TIME_TYPE_ENV = "ENV";
	public static final String MNG_TIME_TYPE_ALARM = "ARM";

	public static final int WEEK_ALL = 0;
	public static final int WEEK_SUN = 1;
	public static final int WEEK_MON = 2;
	public static final int WEEK_TUE = 3;
	public static final int WEEK_WED = 4;
	public static final int WEEK_THU = 5;
	public static final int WEEK_FRI = 6;
	public static final int WEEK_SAT = 7;

	public static final int CHANGE_BY_USER = 1;
	public static final int CHANGE_BY_SYSTEM = 2;
	public static final int CHANGE_TYPE_NONE = 0;
	public static final int CHANGE_TYPE_ADD = 1;
	public static final int CHANGE_TYPE_EDIT = 2;
	public static final int CHANGE_TYPE_DELETE = 3;
	public static final int CHANGE_TYPE_EDIT_NOTICEON = 4;
	public static final int CHANGE_TYPE_EDIT_NOTICEOFF = 5;
	public static final int CHANGE_OBJECT_VIRTUALSERVER = 0;
	public static final int CHANGE_OBJECT_PERSISTENCE = 1;

	/**
	 * 등록하여 사용할 수 있는 최대 ADC 장비 대수.
	 */
	public static final int MAX_DEVICE_NUM = 200;

	// ADC TYPE DEFINE , MNG_ADC.TYPE
	public static final int ADC_TYPE_F5 = 1;
	public static final int ADC_TYPE_ALTEON = 2;
	public static final int ADC_TYPE_PIOLINK_PAS = 3;
	public static final int ADC_TYPE_PIOLINK_PASK = 4;
	public static final int ADC_TYPE_PIOLINK_UNKNOWN = -2;
	public static final int ADC_TYPE_UNKNOWN = -1;
	public static final int ADC_TYPE_ADCSMART = 11; // "ADC_TYPE"을 붙이기 부적절하지만, 기존 포맷을 따른다. ADCsmart의 장비 type은 11번이다.

	public static final int COMMON_NOT_DEFINED = -1;

	public static final String ADC_TYPESTR_ALTEON = "Alteon";
	public static final String ADC_TYPESTR_F5 = "F5";

	/**
	 * 기본 DashboardindexKey 기본으로 제공되는 Sample1,2 대시보드 index 는 1,2 를 사용한다. index 가 3
	 * 이상이면 사용자 custom 대시보드이다.
	 */
	public static final int DEFAULT_DASHBOARD_KEY = 3;

	public class VRRP_STATE {
		public static final int NONE = -1;
		public static final int DISABLE = 0;
		public static final int ENABLE = 1;
	}

	public class VRRP_STATE_ALTEON {
		public static final int ENABLE = 1; // SNMP에서 vrrp state enable 일 때 주는 값, ADCSMART에는 ENABLE로 바꿔 저장
		public static final int DISABLE = 2; // SNMP에서 vrrp state disable일 때 주는 값, ADCSMART에는 DISABLE로 바꿔 저장
	}

	// 공통: 지정된 옵션목록들에 해당하지 않는 경우의 옵션값
	public static final int COMMON_NOT_ALLOWED = -1;
	public static final String COMMON_NOT_ALLOWED_STR = "Not Allowed";

	public static final int STATE_DISABLE = 0;
	public static final int STATE_ENABLE = 1;
	public static final int STATE_TEMPORARY = 2;
	public static final int STATE_FORCEDOFFLINE = 4;

	public static final int STATUS_UNREACHABLE = 0;
	public static final int STATUS_REACHABLE = 1;

	public class ADC_STATUS {
		public static final int UNREACHABLE = 0;
		public static final int REACHABLE = 1;// 네트워크, 로그인, snmp, syslog등의 상태가 하나라도 비정상일 경우임.
		public static final int ABNORMAL = 2;// reverse 포트만 fail인 상태임.
		public static final int UNKNOWN = 3;// 알수 없는 상태임. 검사에 실패한 경우임. 이 상태가 나오면 프로그램적으로 오류임.
	}

	public class ADC_STATE {
		public static final int UNAVAILABLE = 0;
		public static final int AVAILABLE = 1;
	}

	public class ADC_RANGE {
		public static final int ALL = 0;
		public static final int GROUP = 1;
		public static final int SINGLE_ADC = 2;
	}

	public class ADC_LB_CLASS {
		public static final int ALL = 0;
		public static final int SLB = 1;
		public static final int FLB = 2;
	}

	public class ADC_ROLE_MASK {
		public static final int SLB = 0x80000000;
		public static final int FLB = 0x40000000;
		public static final int FLB_CLEAR = 0xBFFFFFFF;
	}

	public class RANGE {
		public static final int ALL = 0;
		public static final int PARTIAL = 1;
		public static final int PARTIAL_VS = 2;
	}

	public static final int DATA_UNAVAILABLE = 0;
	public static final int DATA_AVAILABLE = 1;

	// 개체의 존재 여부를 표시할 때 쓴다. 존재하지 않으면 DEAD, 있으면 ALIVE
	public static final int OBJECT_DEAD = 0;
	public static final int OBJECT_ALIVE = 1;

	// for alteon(VS, VService인경우). Disable: state 값이용. block: 모든 멤버의 상태가 block일 경우.
	// available: 한개 이상이 available일 경우. unavailable: 그외.
	public static final int STATUS_DISABLE = 0; // 회색.
	public static final int STATUS_AVAILABLE = 1; // 초록색. endable 장비만 해당. F5의 unknown 도 포함됨. ALTEON에서는 UP으로 표시됨.
	public static final int STATUS_UNAVAILABLE = 2; // 빨간색. 'NO SERVICES UP' OR 'UNREACHABLE' OR OFFLINE, FAILED.
													// endable 장비만 해당.
	// 아래 STATUS_BLOCK 상태는 ALTEON에만 있는 상태인데, 없애기로 하여, STATUS_BLOCK이면
	// STATUS_UNAVAILABLE로 처리하고 제거했다. 수정한 부분에 "task:blocked_cancel"라고 표시함 2012.11.9
	// ykkim
	// 2015년 2월 10일, 아래의 상태 값도 인식하게 만들었다. 최영조.
	public static final int STATUS_BLOCK = 3; // 노란색. ALTEON만 해당함. 동반 서비스가 죽었을 때 인위적으로 죽은 것으로 간주한 상태, 서비스 레벨의 상태이다.
												// endable 장비만 해당.

	public class VS_STATE {
		public static final int ENABLED = 1;
		public static final int DISABLED = 2;
	}

	public class VS_STATUS {
		// for alteon(VS, VService인경우). Disable: state 값이용. block: 모든 멤버의 상태가 block일 경우.
		// available: 한개 이상이 available일 경우. unavailable: 그외.
		public static final int DISABLE = 0; // 회색.
		public static final int AVAILABLE = 1; // 초록색. endable. F5 unknown도 포함. ALTEON에서는 UP으로 표시됨.
		public static final int UNAVAILABLE = 2; // 빨간색. enable. 'NO SERVICES UP' OR 'UNREACHABLE' OR OFFLINE, FAILED.
		// 아래 STATUS_BLOCK 상태는 ALTEON에만 있는 상태인데, 없애기로 하여, STATUS_BLOCK이면
		// STATUS_UNAVAILABLE로 처리하고 제거했다. 수정한 부분에 "task:blocked_cancel"라고 표시함 2012.11.9
		// ykkim
		public static final int BLOCK = 3; // 노란색. ALTEON만 해당함. 동반 서비스가 죽었을 때 인위적으로 죽은 것으로 간주한 상태, 서비스 레벨의 상태이다. endable
											// 장비만 해당.
		public static final int ERRORAVAILABLE = 4; // 주황색 VS에 Member가 1개가 다운
	}

	public class MEMBER_STATUS // Member와 node 상태 옵션
	{
		public static final int DISABLE = 0; // 회색.
		public static final int AVAILABLE = 1; // 초록색. enable, F5 unknown도 포함
		public static final int UNAVAILABLE = 2; // 빨간색. enable
		public static final int UNKNOWN = 3;
	}

	public class MEMBER_STATE {
		public static final int DISABLE = 0;
		public static final int ENABLE = 1;
		public static final int FORCED_OFFLINE = 2;
		public static final int UNKNOWN = 3;
	}

	public class MEMBER_STATE_PASK {
		public static final int ENABLED = 1;
		public static final int DISABLED = 2;
	}

	public class BACKUP_STATE {
		public static final int EMPTY = 0;
		public static final int REALBAK = 1;
		public static final int GROUPBAK = 2;
	}

	public static final String LOGFILE_SYSTEM = "/opt/adcsmart/logs/system.log";
	public static final String LOGFILE_ADCMON = "/opt/adcsmart/logs/adcmon.log";
	public static final String LOGFILE_RESPTIME = "/opt/adcsmart/logs/resptime.log";
	public static final String LOGFILE_RESP = "/opt/adcsmart/logs/resp.log";
	public static final String LOGFILE_SYSMON = "/opt/adcsmart/logs/sysmon.log";
	public static final String LOGFILE_SYSMON_STAT = "/opt/adcsmart/logs/sysmon.stat";
	public static final String LOGFILE_SYSLOG = "/opt/adcsmart/logs/syslog.log";
	public static final String LOGFILE_SYSLOG_DRP = "/opt/adcsmart/logs/syslog_drp.log";
	public static final String LOGFILE_SYSLOG_FILTER = "/opt/adcsmart/logs/syslog_filter.log";
	public static final String LOGFILE_SYSLOG_STAT = "/opt/adcsmart/logs/syslog.stat";
	public static final String LOGFILE_DEBUG = "/opt/adcsmart/logs/debug.log";
	public static final String LOGFILE_ALTEON_CMND = "/opt/adcsmart/logs/alteon.log";
//	public static final String LOGFILE_FAULT_CHECK = "/opt/adcsmart/logs/faultcheck.log";
	public static final String LOGFILE_F5_CMND = "/opt/adcsmart/logs/f5.log";
	public static final String LOGFILE_PAS_CMND = "/opt/adcsmart/logs/pas.log";
	public static final String LOGFILE_PASK_CMND = "/opt/adcsmart/logs/pask.log";

	public static final String LICENSEFILE = "/opt/adcsmart/cfg/license.lic";
	public static final String CFG_DIR = "/opt/adcsmart/cfg/";
	public static final String SQL_DIR = "/opt/adcsmart/sql/";
	public static final String LOG_DIR = "/opt/adcsmart/logs/";
	public static final String REPORT_DIR = "/var/lib/adcsmart/log";

	public static final String PROPERTY_DIR = "/opt/apache-tomcat/webapps/adcms/WEB-INF/classes/conf/"; // 프로퍼티 파일 경로
	public static final String WEB_BASE_DIR = "/opt/apache-tomcat/webapps/adcms/"; // 프로퍼티 파일 경로

//	public static final String PROPERTY_DIR ="J:\\ADCSMART_V2.0\\RW\\resources\\conf\\"; //로컬에서 테스트 할 경우 프로퍼티 파일 경로

	public static final String DEFAULT_SNMP_RCOMM = "public";

	/**
	 * load balancing method LB_METHOD_HASH는 alteon만 유효 이외 옵션들에 대해서는?
	 */
	public static final int LB_METHOD_ROUND_ROBIN = 0;
	public static final int LB_METHOD_LEAST_CONNECTION_MEMBER = 1;
	public static final int LB_METHOD_HASH = 2; // ALTEON만 씀

	public static final String LB_METHOD_ROUND_ROBIN_STR = "rr";
	public static final String LB_METHOD_LEAST_CONNECTION_MEMBER_STR = "lc";
	public static final String LB_METHOD_ALTEON_HASH_STR = "hash"; // ALTEON만 씀
	public static final String LB_METHOD_PASK_HASH_STR = "sh"; // pask hash
	public static final String LB_METHOD_NA_STR = "N/A"; // ALTEON만 씀

	/**
	 * F5 Persistence profile type 사실상 쓸 수 있는 것은 NONE과 SOURCE_ADDRESS_AFFINITY
	 */
	public static final int LB_PROFILE_PERSISTENCE_TYPE_NONE = 0;
	public static final int LB_PROFILE_PERSISTENCE_TYPE_SOURCE_ADDRESS_AFFINITY = 1;
//	public static final int LB_PROFILE_PERSISTENCE_TYPE_DESTINATION_ADDRESS_AFFINITY = 2;
//	public static final int LB_PROFILE_PERSISTENCE_TYPE_COOKIE = 3;
//	public static final int LB_PROFILE_PERSISTENCE_TYPE_MSRDP = 4;
//	public static final int LB_PROFILE_PERSISTENCE_TYPE_SSL_SID = 5;
//	public static final int LB_PROFILE_PERSISTENCE_TYPE_SIP = 6;
//	public static final int LB_PROFILE_PERSISTENCE_TYPE_UIE = 7;
//	public static final int LB_PROFILE_PERSISTENCE_TYPE_HASH = 8;

	// adc 장비에서 발생되는 syslog 종류.
	public static final int ADC_SYSLOG_SYSTEM = 1;
	public static final int ADC_SYSLOG_AUDIT = 2;
	public static final int ADC_SYSLOG_PACKETFILTER = 3;
	public static final int ADC_SYSLOG_LOCALTRAFFIC = 4;
	public static final int ADC_SYSLOG_ACCESSPOLICY = 5;

	// reserved 'System' user account define
	public static final int SYSTEM_USER_INDEX = 0;
	public static final String SYSTEM_USER_NAME = "System";

	// ALERT definitions..
	// alert을 볼까/말까, 사용자별 설정
	public static final int ALERT_USER_POPUP_ON = 1; // 사용자가 alert popup을 본다.
	public static final int ALERT_USER_POPUP_OFF = 0; // 사용자가 alert popup을 안 본다.
	// alert에 소리를 낼까/말까, 사용자별 설정
	public static final int ALERT_USER_SOUND_ON = 1; // 사용자가 alert 소리를 듣는다.
	public static final int ALERT_USER_SOUND_OFF = 0; // 사용자가 alert 소리를 안 듣는다.

	// 이력의 대상 객체
	public static final int HISTORY_OBJECT_VIRTUALSERVER = 0; // virtual server 변경
	public static final int HISTORY_OBJECT_PERSISTENCE = 0; // persistence profile 변경 이력

	public static final int LOGIN_SUCCES = 0;

	// 세션 검색 포트 관련
	public static final String STRING_TO_PROTOCOL_TCP = "tcp";
	public static final String STRING_TO_PROTOCOL_UDP = "udp";
	public static final String STRING_TO_PROTOCOL_ICMP = "icmp";

	public static final String LBTYPE_SLB = "SLB";
	public static final String LBTYPE_FLB = "FLB";

	public static final int INT_TO_PROTOCOL_TCP = 6;
	public static final int INT_TO_PROTOCOL_UDP = 17;
	public static final int INT_TO_PROTOCOL_ICMP = 1;

	public static final int REALSERVER_DELETE = 0;
	public static final int REALSERVER_NOTGROUP = 0;
	public static final int REALSERVER_GROUP = 1;

	// Vlan Filter
	public static final int VLAN_ALL = 0;
	public static final int VLAN_ENABLE = 1;
	public static final int VLAN_DISABLE = 2;

	public static final String VLAN_ENABLED = "STATE_ENABLED";
	public static final String VLAN_DISABLED = "STATE_DISABLED";

	public static class SERVICE {
		public static final int NOT_DEFINED = -1;
		public static final int TELNET = 23;
		public static final int SSH = 22;

		public static final LinkedHashMap<Integer, String> NAME;
		static {
			NAME = new LinkedHashMap<Integer, String>();
			NAME.put(SERVICE.NOT_DEFINED, "Not Defined");
			NAME.put(SERVICE.TELNET, "Telnet");
			NAME.put(SERVICE.SSH, "SSH");
		}
	}

	public static final String TIME_UNIT_MINUTE = "M";
	public static final String TIME_UNIT_HOUR = "H";
	public static final String TIME_UNIT_DAY = "D";

	public static final String NODESTR = "inherit";

	// SLB 설정 extraKey 추가
	public static final int RECENT_SLB = 1;

	public static final int MAX_STRING_BUILDER_SIZE = 50000;

	public static final int ALTEON_INDEX_STRING = 2;
	public static final int RESP_SECTION_TYPE = 1;

	// ADC, 서비스, member 상태 알리미 (정상 , 주의, 경고)
	public static class STATUS_CHECK {
		public static final int WARNING = 0;
		public static final int AVAILABLE = 1;
		public static final int CAUTION = 2;

	}

	public static final int UNKNOWN_MODE = 0;
	public static final int DATA_MODE = 1;
	public static final int MGMT_MODE = 2;

	// 구간 응답시간 체크
	public static class INTERVAL_CHECK {
		public static final int NONE = 0;
		public static final int TCP = 1;
		public static final int HTTP = 2;
	}

	// 구간 응답시간 체크
	public static class SLBUSER_TYPE {
		public static final int ALL = 0;
		public static final int APPLICANT = 1;
		public static final int RECEIVE = 2;
		public static final int RECENT = 3;
	}

	// SSHO MODE
	public static class SSO_MODE {
		public static final int DEFAULT_MODE = 0;
		public static final int TACACS_MODE = 1;
	}

	// Configuration state
	public static class CONFIG_STATE {
		public static final int INCOMPLETE = 0;
		public static final int COMPLETE = 1;
		public static final int FAILED = 2;
	}

	public static class CHASSIS_TEMPERATURE {
		public static final int HIGH = 70;
	}

	public static class FAILOVER_LOG {
		public static final String STANDBY1 = "%010c0044:5: Command:  go standby%";
		public static final String STANDBY2 = "%010c0018:5: Standby%";
		public static final String ACTIVE1 = "%010c0019:5: Active%";
	}

	public static class LINK_DOWN_LOG {
		public static final String LINK_DOWN1 = "%link is down%";
		public static final String LINK_DOWN_ALTEON = "%link down on port%";
	}

	public static class TEMP_HIGH {
		public static final String TEMP_HIGH = "%Temperature exceeds threshold%";
	}

	public static class VRRP_INCORRECT {
		public static final String VRRP_INCORRECT = "%received incorrect addresses%";
	}

	public static enum CPU {
		CPU1_USAGE, CPU2_USAGE, CPU3_USAGE, CPU4_USAGE, CPU5_USAGE, CPU6_USAGE, CPU7_USAGE, CPU8_USAGE, CPU9_USAGE,
		CPU10_USAGE, CPU11_USAGE, CPU12_USAGE, CPU13_USAGE, CPU14_USAGE, CPU15_USAGE, CPU16_USAGE, CPU17_USAGE,
		CPU18_USAGE, CPU19_USAGE, CPU20_USAGE, CPU21_USAGE, CPU22_USAGE, CPU23_USAGE, CPU24_USAGE, CPU25_USAGE,
		CPU26_USAGE, CPU27_USAGE, CPU28_USAGE, CPU29_USAGE, CPU30_USAGE, CPU31_USAGE, CPU32_USAGE
	}

	// PROTOCOL
	public static class PROTOCOL {
		public static final int TELNET = 23;
		public static final int SSH = 22;
	}

	public static class SNMPVERSION {
		public static final int SNMPV2 = 2;
		public static final int SNMPV3 = 3;
	}
}