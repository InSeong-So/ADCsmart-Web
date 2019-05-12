package kr.openbase.adcsmart.service.impl.f5;

import java.io.BufferedWriter;
import java.io.FileWriter;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class CommonF5 {
	public static iControl.Interfaces initInterfaces(OBDtoAdcInfo adcInfo) {
		iControl.Interfaces interfaces = new iControl.Interfaces();
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "password -- " + adcInfo.getAdcPasswordDecrypt());
		interfaces.initialize(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt());
		return interfaces;
	}

	public static iControl.Interfaces initInterfaces(String ipaddress, String account, String password) {
		iControl.Interfaces interfaces = new iControl.Interfaces();
		interfaces.initialize(ipaddress, account, password);
		return interfaces;
	}

	static final iControl.LocalLBVirtualServerVirtualServerType DEFAULT_VIRTUALSERVER_TYPE = iControl.LocalLBVirtualServerVirtualServerType.RESOURCE_TYPE_FAST_L4;
	// iControl.LocalLBVirtualServerVirtualServerType.RESOURCE_TYPE_POOL;

	static long buildLong(iControl.CommonULong64 ul64) {
		return ((long) ul64.getHigh()) << 32 | ((long) ul64.getLow());
	}

	static Integer getObjectStatus(iControl.LocalLBEnabledStatus enabledStatus,
			iControl.LocalLBAvailabilityStatus availabilityStatus) {
		// vs enabled/disabled가 상위 판별 조건이다. enabled이면 availability를 따져서 상태를 주고,
		// disabled면 availability가 enabled여도 disabled로 내 놓는다.
		if (enabledStatus == iControl.LocalLBEnabledStatus.ENABLED_STATUS_ENABLED) {
			return CommonF5.valueStatusAvailability2Int(availabilityStatus);
		} else {
			return OBDefine.VS_STATUS.DISABLE;
		}
	}

	static int valueStatusAvailability2Int(iControl.LocalLBAvailabilityStatus status) // virtual server poolmember의
																						// status를 구하는데 씀
	{
		int value = 0;
		if (status == iControl.LocalLBAvailabilityStatus.AVAILABILITY_STATUS_GREEN) // green = available
		{
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "green");
			value = OBDefine.VS_STATUS.AVAILABLE;
		} else if (status == iControl.LocalLBAvailabilityStatus.AVAILABILITY_STATUS_YELLOW) // yellow = unavailable
		{
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "yellow");
			value = OBDefine.VS_STATUS.UNAVAILABLE;
		} else if (status == iControl.LocalLBAvailabilityStatus.AVAILABILITY_STATUS_RED) // red = offline ==>
																							// unavailable
		{
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "red");
			value = OBDefine.VS_STATUS.UNAVAILABLE;
		} else if (status == iControl.LocalLBAvailabilityStatus.AVAILABILITY_STATUS_BLUE) // blue = unknown ==>
																							// available
		{
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "blue");
			value = OBDefine.VS_STATUS.AVAILABLE;
		} else // AVAILABILITY_STATUS_GRAY or AVAILABILITY_STATUS_NONE //unavailable로 처리
		{
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "unvailable");
			value = OBDefine.VS_STATUS.UNAVAILABLE;
		}
		return value;
	}

	static Integer valueLocalLBEnabledStatus2int(iControl.LocalLBEnabledStatus en_status) {
		if (en_status == iControl.LocalLBEnabledStatus.ENABLED_STATUS_ENABLED) {
			return OBDefine.STATE_ENABLE;
		} else {
			return OBDefine.STATE_DISABLE;
		}
	}

	// Load Balancing Method 조회:
	static Integer valueLBMethod2Integer(iControl.LocalLBLBMethod lb_method) {
		if (lb_method == iControl.LocalLBLBMethod.LB_METHOD_ROUND_ROBIN) {
			return OBDefine.LB_METHOD_ROUND_ROBIN;
		} else if (lb_method == iControl.LocalLBLBMethod.LB_METHOD_LEAST_CONNECTION_MEMBER) {
			return OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER;
		} else {
			return OBDefine.COMMON_NOT_ALLOWED; // not defined lb_method
		}
	}

	// Load Balancing Method
	static iControl.LocalLBLBMethod valueInteger2LBMethod(Integer lb_method) throws OBException {
		if (lb_method == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("Error: Loadbalancing method is null."));
		} else if (lb_method == OBDefine.LB_METHOD_ROUND_ROBIN) {
			return iControl.LocalLBLBMethod.LB_METHOD_ROUND_ROBIN;
		} else if (lb_method == OBDefine.LB_METHOD_LEAST_CONNECTION_MEMBER) {
			return iControl.LocalLBLBMethod.LB_METHOD_LEAST_CONNECTION_MEMBER;
		} else {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					String.format("Error: Loadbalancing method is impossible value. (%d)", lb_method));
		}
	}
	// profile 정보 보기/바꾸기 ----------------------------------------------------

	static Integer valuePersistenceMode2FilteredInteger(iControl.LocalLBPersistenceMode mode) // Mode definition값을
																								// Integer로 바꿈
	{
		if (mode == iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_NONE) {
			return 0;
		} else if (mode == iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_SOURCE_ADDRESS_AFFINITY) {
			return 1;
		} else // NONE과 Source Address Affinity만 쓸 수 있음.
		{
			return OBDefine.COMMON_NOT_ALLOWED;
		}
//		else if(mode == iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_DESTINATION_ADDRESS_AFFINITY)
//		{
//			return 2;  
//		}
//		else if(mode == iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_COOKIE)
//		{
//			return 3;  
//		}
//		else if(mode == iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_MSRDP)
//		{
//			return 4;  
//		}
//		else if(mode == iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_SSL_SID)
//		{
//			return 5;  
//		}
//		else if(mode == iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_SIP)
//		{
//			return 6;  
//		}
//		else if(mode == iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_UIE)
//		{
//			return 7;  
//		}
//		else if(mode == iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_HASH)
//		{
//			return 8;  
//		}
//		else // LocalLBPersistenceMode.PERSISTENCE_MODE_NONE
//		{
//			return 0;
//		}
	}

	static iControl.LocalLBPersistenceMode valueInteger2PersistenceMode(Integer mode) {
		// 실제는 None, Source Address Affinity만 쓸 수 있음
		if (mode == 1) {
			return iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_SOURCE_ADDRESS_AFFINITY;
		} else if (mode == 2) {
			return iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_DESTINATION_ADDRESS_AFFINITY;
		} else if (mode == 3) {
			return iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_COOKIE;
		} else if (mode == 4) {
			return iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_MSRDP;
		} else if (mode == 5) {
			return iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_SSL_SID;
		} else if (mode == 6) {
			return iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_SIP;
		} else if (mode == 7) {
			return iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_UIE;
		} else if (mode == 8) {
			return iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_HASH;
		} else {
			return iControl.LocalLBPersistenceMode.PERSISTENCE_MODE_NONE;
		}
	}

	static int valueLocalLBMonitorTemplate2Int(String template) {
		if (template.endsWith(OBDefine.HEALTH_CHECK.TCP_STR)) // 원래 equals() 였는데, F5 v11부터 "tcp" 대신 "/Common/tcp"로 오기
																// 때문에 endsWith로 바꿨다.
		{
			return OBDefine.HEALTH_CHECK.TCP;
		} else if (template.endsWith(OBDefine.HEALTH_CHECK.HTTP_STR)) {
			return OBDefine.HEALTH_CHECK.HTTP;
		} else if (template.endsWith(OBDefine.HEALTH_CHECK.HTTPS_STR)) {
			return OBDefine.HEALTH_CHECK.HTTPS;
		} else if (template.endsWith(OBDefine.HEALTH_CHECK.UDP_STR)) {
			return OBDefine.HEALTH_CHECK.UDP;
		}
//		else if(template.endsWith(OBDefine.HEALTH_CHECK.ICMP_STR))
//		{
//			return OBDefine.HEALTH_CHECK.ICMP;
//		}
		else if (template.endsWith(OBDefine.HEALTH_CHECK.GATEWAY_ICMP_STR)) {
			return OBDefine.HEALTH_CHECK.GATEWAY_ICMP;
		}
//		else if(template.endsWith(OBDefine.HEALTH_CHECK.ARP_STR))
//		{
//			return OBDefine.HEALTH_CHECK.ARP;
//		}
//		else if(template.endsWith(OBDefine.HEALTH_CHECK.LINK_STR))
//		{
//			return OBDefine.HEALTH_CHECK.LINK;
//		}
		else {
			return OBDefine.COMMON_NOT_ALLOWED;
		}
	}

	static String valueInt2LocalLBMonitorTemplate(int template) {
		if (template == OBDefine.HEALTH_CHECK.TCP) {
			return OBDefine.HEALTH_CHECK.TCP_STR;
		} else if (template == OBDefine.HEALTH_CHECK.HTTP) {
			return OBDefine.HEALTH_CHECK.HTTP_STR;
		} else if (template == OBDefine.HEALTH_CHECK.HTTPS) {
			return OBDefine.HEALTH_CHECK.HTTPS_STR;
		} else if (template == OBDefine.HEALTH_CHECK.UDP) {
			return OBDefine.HEALTH_CHECK.UDP_STR;
		}
//		else if(template==OBDefine.HEALTH_CHECK.ICMP)
//		{
//			return OBDefine.HEALTH_CHECK.ICMP_STR;
//		}
		else if (template == OBDefine.HEALTH_CHECK.GATEWAY_ICMP) {
			return OBDefine.HEALTH_CHECK.GATEWAY_ICMP_STR;
		} else if (template == OBDefine.HEALTH_CHECK.ARP) {
			return OBDefine.HEALTH_CHECK.ARP_STR;
		} else if (template == OBDefine.HEALTH_CHECK.LINK) {
			return OBDefine.HEALTH_CHECK.LINK_STR;
		} else {
			return OBDefine.COMMON_NOT_ALLOWED_STR;
		}
	}

	static boolean isCommonEnabledState(iControl.CommonEnabledState state) {
		if (state == iControl.CommonEnabledState.STATE_ENABLED) {
			return true;
		} else {
			return false;
		}
	}

	public static OBDtoAdcVServerF5 cloneVServer(OBDtoAdcVServerF5 original) {
		OBDtoAdcVServerF5 clone = null;
		if (original != null) {
			clone = new OBDtoAdcVServerF5(original);
		}
		return clone;
	}

	public static OBDtoAdcPoolF5 clonePool(OBDtoAdcPoolF5 original) {
		OBDtoAdcPoolF5 clone = new OBDtoAdcPoolF5(original);

		return clone;
	}

	public static void Exception(String comment, String msg)
			throws OBException, OBExceptionLogin, OBExceptionUnreachable {
		if (msg == null) {
			msg = "null";
		}

		if (msg.contains("ConnectException")) {
			throw new OBExceptionUnreachable("Connection timed out.");
		} else if (msg.contains("Network is unreachable")) {
			throw new OBExceptionUnreachable("Network is unreachable.");
		} else if (msg.contains("No route to host")) {
			throw new OBExceptionUnreachable("No route to host.");
		} else if (msg.contains("F5 Authorization Required")) {
			throw new OBExceptionLogin("F5 Authorization Required.");
		} else if (msg.contains("F5 CLI Authorization Required")) {
			throw new OBExceptionLogin("F5 CLI Authorization Required.");
		} else {
			if (comment == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("msg: %s. call:%s", msg, new OBUtility().getStackTrace()));
			} else {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("%s (msg: %s). call:%s", comment, msg, new OBUtility().getStackTrace()));
			}
		}
	}

	// test functions
	// ------------------------------------------------------------------------------------------
//	public static void main(String[] args)
//	{
//		iControl.Interfaces inter = new iControl.Interfaces();
//		
//		inter.initialize("192.168.200.155", "admin", "admin");
//		
//		try
//		{
//			test(inter, "performance_tables.txt");
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	/**
	 * F5의 공통 자료형인 CommonStatistic 데이터 배열을 print한다. 데이터 값을 확인하는 테스트 함수이다.
	 * 
	 * @param statistics
	 */
	public static void testPrintCommonStatisticEntry(iControl.CommonStatistic[] statistics) {
		long temp = 0L;
		for (iControl.CommonStatistic stat : statistics) {
			temp = buildLong(stat.getValue());
			if (temp != 0) {
				System.out.println(stat.getType().toString() + ": " + temp);
			}
		}
	}

	// 테스트 함수 껍데기, 아무거나 만들어 쓴다.
	public static void test(iControl.Interfaces inter, String report_file) throws Exception {
		iControl.SystemStatisticsPerformanceTable[] pTable = inter.getSystemStatistics().get_performance_table_list();

		BufferedWriter report = new BufferedWriter(new FileWriter(report_file));
		for (iControl.SystemStatisticsPerformanceTable pt : pTable) {
			report.write(pt.getTable_name() + ": " + pt.getTable_description() + "\n");
		}
		report.close();
	}
}
