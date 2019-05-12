package kr.openbase.adcsmart.service.impl.f5;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberF5;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTraffic;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

//import iControl.*;

class Member {
	private String _address;
	private long _port;
	private long _ratio;
	private long _priority;
	private long _connectionLimit;
	private String _monitor_color; // bigip 화면 : Health Monitors의 색, API: availability status
	private String _state; // bigip 화면 : State, API: enabled status
	private String _availability; // bigip 화면 : Availability, API: object status
	private String _monitorStatus;

	public String getAddress() {
		return _address;
	}

	public void setAddress(String address) {
		_address = address;
	}

	public long getPort() {
		return _port;
	}

	public void setPort(long port) {
		_port = port;
	}

	public long getRatio() {
		return _ratio;
	}

	public void setRatio(long ratio) {
		_ratio = ratio;
	}

	public long getPriority() {
		return _priority;
	}

	public void setPriority(long priority) {
		_priority = priority;
	}

	public long getConnectionLimit() {
		return _connectionLimit;
	}

	public void setConnectionLimit(long connectionLimit) {
		_connectionLimit = connectionLimit;
	}

	public String getMonitorColor() {
		return _monitor_color;
	}

	public void setMonitorColor(String monitor_color) {
		_monitor_color = monitor_color;
	}

	public String getState() {
		return _state;
	}

	public void setState(String state) {
		_state = state;
	}

	public String getAvailability() {
		return _availability;
	}

	public void setAvailability(String availability) {
		_availability = availability;
	}

	public String getMonitorStatus() {
		return _monitorStatus;
	}

	public void setMonitorStatus(String monitorStatus) {
		_monitorStatus = monitorStatus;
	}
}

class PoolRecord {
	private String _name;
	private iControl.LocalLBMonitorRule _health_monitor_rule = null;
	private String _lb_method = null;
	private long _aggregate_dynamic_ratio = 0;
	private long _member_count = 0;
	private long _active_member_count = 0;
	private ArrayList<Member> _members = null;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public iControl.LocalLBMonitorRule getHealthMonitorRule() {
		return _health_monitor_rule;
	}

	public void setHealthMonitorRule(iControl.LocalLBMonitorRule health_monitor_rule) {
		_health_monitor_rule = health_monitor_rule;
	}

	public String getLbMethod() {
		return _lb_method;
	}

	public void setLbMethod(String lb_method) {
		_lb_method = lb_method;
	}

	public long getAggregateDynamicRatio() {
		return _aggregate_dynamic_ratio;
	}

	public void setAggregateDynamicRatio(long aggregate_dynamic_ratio) {
		_aggregate_dynamic_ratio = aggregate_dynamic_ratio;
	}

	public long getMemberCount() {
		return _member_count;
	}

	public void setMemberCount(long member_count) {
		_member_count = member_count;
	}

	public long getActiveMemberCount() {
		return _active_member_count;
	}

	public void setActiveMemberCount(long active_member_count) {
		_active_member_count = active_member_count;
	}

	public ArrayList<Member> getMembers() {
		return _members;
	}

	public void setMembers(ArrayList<Member> members) {
		_members = members;
	}
}

public class Pool {
	private iControl.Interfaces _interfaces = null;
	private iControl.LocalLBPoolPortType _LocalLBPool = null;
	private String _name = null;

	// constructor
	Pool(iControl.Interfaces interfaces, String name) throws Exception {
		_interfaces = interfaces;
		_name = name;
		_LocalLBPool = _interfaces.getLocalLBPool();
	}

	// member - interfaces
	public iControl.Interfaces getInterfaces() {
		return _interfaces;
	}

	public void setInterfaces(iControl.Interfaces interfaces) {
		_interfaces = interfaces;
	}

	// member - name
	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	// member - pool stub
	public iControl.LocalLBPoolPortType getPoolStub() {
		return _LocalLBPool;
	}

	public void setPoolStub(iControl.Interfaces interfaces) throws Exception {
		_LocalLBPool = _interfaces.getLocalLBPool();
	}

	private void validateBaseValues() throws Exception {
		if (_interfaces == null) {
			throw new Exception("Pool: F5 Interfaces error(null)");
		} else if (_name == null) {
			throw new Exception("Pool: pool name error(null)");
		} else if (_LocalLBPool == null) {
			throw new Exception("Pool: F5 API Stub error(null)");
		}
	}

	private static void validateBaseValues(iControl.Interfaces interfaces) throws Exception {
		if (interfaces == null) {
			throw new Exception("Pool: F5 interface error(null)");
		}
	}

	private static String[] getList(iControl.Interfaces interfaces) throws Exception {
		String[] pool_list = null;

		validateBaseValues(interfaces);

		pool_list = interfaces.getLocalLBPool().get_list();
		return pool_list;
	}

	// HealthMonitor 조회: monitor는 복수 할당
	public iControl.LocalLBPoolMonitorAssociation getMonitorAssociation() throws Exception {
		validateBaseValues();

		String[] pool_names = { _name };
		iControl.LocalLBPoolMonitorAssociation[] monitors = _LocalLBPool.get_monitor_association(pool_names);
		return monitors[0];
	}

	private static iControl.LocalLBPoolMonitorAssociation[] getMonitorAssociations(iControl.Interfaces interfaces,
			String[] pool_names) throws Exception {
		validateBaseValues(interfaces);
		if (pool_names == null) {
			throw new Exception("Pool: pool name error(null)");
		}
		iControl.LocalLBPoolMonitorAssociation[] monitors = interfaces.getLocalLBPool()
				.get_monitor_association(pool_names);
		return monitors;
	}

	// HealthMonitor 설정: monitor는 복수 할당
	public void setMonitorAssociation(iControl.LocalLBPoolMonitorAssociation monitorAssociation) throws Exception {
		validateBaseValues();

		iControl.LocalLBPoolMonitorAssociation[] monitorAssociations = { monitorAssociation };

		_LocalLBPool.set_monitor_association(monitorAssociations);
	}

	void setHealthMonitor(iControl.LocalLBMonitorRuleType monitorRuleType, long quorum, String[] monitor_templates)
			throws Exception {
		iControl.LocalLBPoolMonitorAssociation monitorAssociation = new iControl.LocalLBPoolMonitorAssociation(); // 1개
																													// pool의
																													// monitor를
																													// 설정하기
																													// 때문에
																													// association은
																													// 하나다.

		iControl.LocalLBMonitorRule monitorRule = new iControl.LocalLBMonitorRule(); // Association이 하나이기 때문에 Rule도 1개면
																						// 됨
		monitorRule.setType(monitorRuleType);
		monitorRule.setQuorum(quorum);
		monitorRule.setMonitor_templates(monitor_templates);

		monitorAssociation.setPool_name(_name);
		monitorAssociation.setMonitor_rule(monitorRule);

		setMonitorAssociation(monitorAssociation);
	}

	void removeHealthMonitor() throws Exception {
		validateBaseValues();

		String[] pool_names = { _name };

		_LocalLBPool.remove_monitor_association(pool_names);
	}

	// 정확하게 무슨 기능이지.... 테스트 필요.
	public iControl.LocalLBMonitorInstanceState[] getMonitorInstance() throws Exception {
		validateBaseValues();

		String[] pool_names = { _name };

		iControl.LocalLBMonitorInstanceState[][] mis = _LocalLBPool.get_monitor_instance(pool_names);

		return mis[0];

	}

	// Load Balancing Method 조회:
	public iControl.LocalLBLBMethod getLBMethod() throws Exception {
		validateBaseValues();

		String[] pool_names = { _name };
		iControl.LocalLBLBMethod[] lb_methods = _LocalLBPool.get_lb_method(pool_names);
		return lb_methods[0];
	}

	private static iControl.LocalLBLBMethod[] getLBMethods(iControl.Interfaces interfaces, String[] pool_names)
			throws Exception {
		validateBaseValues(interfaces);

		if (pool_names == null) {
			throw new Exception("Pool: pool name error(null)");
		}
		iControl.LocalLBLBMethod[] lb_methods = interfaces.getLocalLBPool().get_lb_method(pool_names);
		return lb_methods;
	}

	public void setLBMethod(iControl.LocalLBLBMethod lb_method) throws Exception {
		validateBaseValues();

		String[] pool_names = { _name };
		iControl.LocalLBLBMethod[] lb_methods = { lb_method };
		_LocalLBPool.set_lb_method(pool_names, lb_methods);
	}

	public iControl.LocalLBServiceDownAction getActionOnServiceDown() throws Exception {
		validateBaseValues();

		String[] pool_names = { _name };
		iControl.LocalLBServiceDownAction[] down_action = _LocalLBPool.get_action_on_service_down(pool_names);
		return down_action[0];
	}

	public long getAggregateDynamicRatio() throws Exception {
		validateBaseValues();

		String[] pool_names = { _name };
		long[] dynamicRatio = _LocalLBPool.get_aggregate_dynamic_ratio(pool_names);
		return dynamicRatio[0];
	}

	public long getActiveMemberCount() throws Exception {
		validateBaseValues();

		String[] pool_names = { _name };
		long[] activeMemberCount = _LocalLBPool.get_active_member_count(pool_names);
		return activeMemberCount[0];
	}

	// member가 복수이므로 pool-member 2차원 배열로 나오는데, 1개 pool의 members만 뽑기 때문에 item[0]만
	// 리턴한다.
	public iControl.CommonIPPortDefinition[] getMembers() throws Exception {
		validateBaseValues();

		String[] pool_names = { _name };
		iControl.CommonIPPortDefinition[][] membersIPPort = _LocalLBPool.get_member(pool_names);
		return membersIPPort[0];
	}

	public long getMembersCount() throws Exception {
		validateBaseValues();

		String[] pool_names = { _name };
		iControl.CommonIPPortDefinition[][] membersIPPort = _LocalLBPool.get_member(pool_names);
		return membersIPPort[0].length;
	}

	private void addMember(String address, long port) throws Exception {
		validateBaseValues();

		iControl.CommonIPPortDefinition[] members = new iControl.CommonIPPortDefinition[1];
		members[0] = new iControl.CommonIPPortDefinition();
		members[0].setAddress(address);
		members[0].setPort(port);
		addMembers(members);
	}

	private void addMembers(iControl.CommonIPPortDefinition[] memberDefinitions) throws Exception {
		validateBaseValues();

		String[] pool_names = { _name };

		iControl.CommonIPPortDefinition[][] _memberDefinitions = { memberDefinitions };

		_LocalLBPool.add_member(pool_names, _memberDefinitions);
	}

	void addMembers(ArrayList<OBDtoAdcPoolMemberF5> members) throws Exception {
		validateBaseValues();

		int i = 0;
		String[] pool_names = { _name };

		iControl.CommonIPPortDefinition[] memberDefs = new iControl.CommonIPPortDefinition[members.size()];

		for (i = 0; i < members.size(); i++) {
			memberDefs[i] = new iControl.CommonIPPortDefinition();
			memberDefs[i].setAddress(members.get(i).getIpAddress());
			memberDefs[i].setPort(members.get(i).getPort());
		}

		iControl.CommonIPPortDefinition[][] memberDefsTemp = { memberDefs };

		_LocalLBPool.add_member(pool_names, memberDefsTemp);
	}

	private void removeMember(String address, long port) throws Exception {
		validateBaseValues();

		iControl.CommonIPPortDefinition[] members = new iControl.CommonIPPortDefinition[1];
		members[0] = new iControl.CommonIPPortDefinition();
		members[0].setAddress(address);
		members[0].setPort(port);
		removeMembers(members);
	}

	private void removeMembers(iControl.CommonIPPortDefinition[] members) throws Exception {
		validateBaseValues();

		String[] pool_names = { _name };
		iControl.CommonIPPortDefinition[][] members_list = { members };
		_LocalLBPool.remove_member(pool_names, members_list);
	}

	void removeMembers(ArrayList<OBDtoAdcPoolMemberF5> members) throws Exception {
		validateBaseValues();

		int i = 0;
		String[] pool_names = { _name };

		iControl.CommonIPPortDefinition[] memberDefs = new iControl.CommonIPPortDefinition[members.size()];

		for (i = 0; i < members.size(); i++) {
			memberDefs[i] = new iControl.CommonIPPortDefinition();
			memberDefs[i].setAddress(members.get(i).getIpAddress());
			memberDefs[i].setPort(members.get(i).getPort());
		}

		iControl.CommonIPPortDefinition[][] memberDefsTemp = { memberDefs };

		_LocalLBPool.remove_member(pool_names, memberDefsTemp);
	}

	void removeMemberAll() throws Exception {
		validateBaseValues();

		iControl.CommonIPPortDefinition[] members = getMembers(); // 모든 멤버를 구한다.
		removeMembers(members);
	}

	// pool 만들기 - 멤버 목록을 받아서 통으로 한번에 넣음
	void create(iControl.LocalLBLBMethod lbmethod, iControl.CommonIPPortDefinition[] member_list) throws Exception {
		validateBaseValues();

		String[] pool_names = { _name };

		if (member_list == null) {
			iControl.CommonIPPortDefinition[][] members = new iControl.CommonIPPortDefinition[1][1];
			members[0][0] = new iControl.CommonIPPortDefinition();
			members[0][0].setAddress(null);
			members[0][0].setPort(0);
		}
		iControl.LocalLBLBMethod[] lbmethods = { lbmethod };
		iControl.CommonIPPortDefinition[][] members = { member_list };
		_LocalLBPool.create(pool_names, lbmethods, members);
	}

	void delete() throws Exception {
		validateBaseValues();
		String[] pool_names = { _name };

		_LocalLBPool.delete_pool(pool_names);
	}

	private static void delete(iControl.Interfaces interfaces, String pool_name) throws Exception {
		String[] pool_names = { pool_name };

		validateBaseValues(interfaces);

		if (pool_name == null) {
			throw new Exception("Pool: pool name error(null)");
		}
		interfaces.getLocalLBPool().delete_pool(pool_names);
	}

	public static ArrayList<OBDtoAdcPoolF5> getAll(iControl.Interfaces interfaces) throws Exception {
		long tt0, tt1;
		tt0 = System.currentTimeMillis();
		validateBaseValues(interfaces);
		String[] pools = Pool.getList(interfaces);// 모든 vs 목록을 가져옴
		tt1 = System.currentTimeMillis();
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time> Pool.getAll() --- getList(): " + (tt1 - tt0) / 1000.0);

		ArrayList<OBDtoAdcPoolF5> result = get(interfaces, pools);
		return result;
	}

	public static ArrayList<OBDtoAdcPoolF5> get(iControl.Interfaces interfaces, String[] pools) throws Exception {
		validateBaseValues(interfaces);
		int i;
		long tt0, tt1, tt2, tt3;
		tt0 = System.currentTimeMillis();

		// pool DTO 총합계 수집장소
		ArrayList<OBDtoAdcPoolF5> poolDtoSet = new ArrayList<OBDtoAdcPoolF5>();

		// load balancing method를 array로 한번에 가져옴, 성능에 좋음
		iControl.LocalLBLBMethod[] lbmethods = Pool.getLBMethods(interfaces, pools);
		tt1 = System.currentTimeMillis();
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time> Pool.get() --- getLBMethods(): " + (tt1 - tt0) / 1000.0);

		// health monitor 읽어옴
		iControl.LocalLBPoolMonitorAssociation[] monitors = Pool.getMonitorAssociations(interfaces, pools);
		tt2 = System.currentTimeMillis();
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
				"<time> Pool.get() --- getMonitorAssociations(): " + (tt2 - tt1) / 1000.0);

		for (i = 0; i < pools.length; i++) {
			// pool DTO storage
			OBDtoAdcPoolF5 poolDto = new OBDtoAdcPoolF5();

			// name
			poolDto.setName(pools[i]);
			// load balancing method
			poolDto.setLbMethod(CommonF5.valueLBMethod2Integer(lbmethods[i]));

			poolDto.setHealthCheck(OBDefine.HEALTH_CHECK.NONE); // 기본값
			if (monitors[i] != null) // 사실 monitors[i]가 null인 경우는 없지만 확인사살
			{
				if (monitors[i].getMonitor_rule() == null) // 실제로 여기 걸리는 경우는 못 봄
				{
					poolDto.setHealthCheck(OBDefine.HEALTH_CHECK.NONE);
					// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "monitor record rule null");
				} else {
					if (monitors[i].getMonitor_rule().getMonitor_templates() == null) // 실제로 여기 걸리는 경우는 못 봄
					{
						poolDto.setHealthCheck(OBDefine.HEALTH_CHECK.NONE);
						// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "monitor record template null");
					} else {
						if (monitors[i].getMonitor_rule().getMonitor_templates().length == 0) // health monitor가 설정되지
																								// 않으면 여기에 걸림
						{
							poolDto.setHealthCheck(OBDefine.HEALTH_CHECK.NONE);
							// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "monitor record template length
							// zero ");
						} else if (monitors[i].getMonitor_rule().getMonitor_templates().length == 1) // health monitor가
																										// 1개. 기다리던 case
						{
							poolDto.setHealthCheck(CommonF5.valueLocalLBMonitorTemplate2Int(
									monitors[i].getMonitor_rule().getMonitor_templates()[0]));
						} else // health monitor가 복수이면 not allowed로 만들어 건드리지 않는다.
						{
							// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "not allowed(multi)");
							poolDto.setHealthCheck(OBDefine.COMMON_NOT_ALLOWED);
						}
					}
				}
			}
			poolDtoSet.add(poolDto);
		}

		tt3 = System.currentTimeMillis();
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time> Pool.get() construct data: " + (tt3 - tt0) / 1000.0);
		return poolDtoSet;
	}

	private static ArrayList<DtoPoolMemberTraffic> getStatistics(iControl.Interfaces inter) throws Exception {
		validateBaseValues(inter);

		// 모든 pool이름을 구한다.
		long t1 = System.currentTimeMillis();
		String[] pool_list = Pool.getList(inter);
		long t2 = System.currentTimeMillis();
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time> Pool.getStatistics() --- getList(): " + (t2 - t1) / 1000.0);

		return getStatistics(inter, pool_list);
	}

	private static ArrayList<DtoPoolMemberTraffic> getStatistics(iControl.Interfaces inter, String[] pool_list)
			throws Exception {
		validateBaseValues(inter);

		int i = 0, j = 0;
		long bytes_in = 0L;
		long bytes_out = 0L;
		long packets_in = 0L;
		long packets_out = 0L;
		long maxConn = 0L;
		long totalConn = 0L;

		// pool 통계 전체를 저장할 DTO
		ArrayList<DtoPoolMemberTraffic> poolStatDtoList = new ArrayList<DtoPoolMemberTraffic>();

		// 모든 pool의 member 통계를 구한다. pool별로 하나의 status object를 가지고, 그 안에 member entries가
		// 있다.
		long t1 = System.currentTimeMillis();
		iControl.LocalLBPoolMemberMemberStatistics[] poolAll = inter.getLocalLBPoolMember()
				.get_all_statistics(pool_list);
		long t2 = System.currentTimeMillis();

		// 모든 pool의 member status를 구한다. 구성은 member 통계와 같다. pool안에 member entries가 들어 있는
		// 2차원 배열이다.
		iControl.LocalLBPoolMemberMemberObjectStatus[][] poolStatusAll = inter.getLocalLBPoolMember()
				.get_object_status(pool_list);
		long t3 = System.currentTimeMillis();

		OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
				"<time> Pool.getStatistics() --- get_all_statistics(pool_list): " + (t2 - t1) / 1000.0);
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
				"<time> Pool.getStatistics() --- get_object_status(pool_list) : " + (t3 - t2) / 1000.0);

		for (i = 0; i < poolAll.length; i++) // pool들
		{
			// pool 개별 통계를 저장할 DTO
			DtoPoolMemberTraffic poolStatDto = new DtoPoolMemberTraffic();

			// i번 pool의 멤버통계전체를 가져온다.
			iControl.LocalLBPoolMemberMemberStatisticEntry[] memberAll = poolAll[i].getStatistics();

			// i번 pool의 멤버통계전체에 관한 시간을 가져온다. 사실 쓰는데가 없다.
			// iControl.CommonTimeStamp ts = poolAll[i].getTime_stamp();

			// i번 pool의 멤버status전체 목록을 가져온다.
			iControl.LocalLBPoolMemberMemberObjectStatus[] memberStatusAll = poolStatusAll[i];

			// 멤버 통계 전체를 저장할 Dto 변수
			ArrayList<DtoNodeTraffic> memberDtoAll = new ArrayList<DtoNodeTraffic>();

			// 각 멤버의 통계를 하나씩 보면서 개별 멤버통계를 구성한다
			for (iControl.LocalLBPoolMemberMemberStatisticEntry ms : memberAll) {
				// 멤버 개별 통계 DTO
				DtoNodeTraffic nodeStatDto = new DtoNodeTraffic();

				// 트래픽 DTO
				OBDtoMonTraffic traffic = new OBDtoMonTraffic();

				// 멤버식별
				nodeStatDto.setIpaddress(ms.getMember().getAddress());
				nodeStatDto.setPort((int) ms.getMember().getPort());
				nodeStatDto.setAlteonID(0);

				// 멤버status
				iControl.LocalLBPoolMemberMemberObjectStatus memberStatus;
				nodeStatDto.setStatus(OBDefine.MEMBER_STATUS.AVAILABLE); // 원래는 unknown으로 할 건데 unknown을 available로 간주하기로
																			// 결정

				for (j = 0; j < memberStatusAll.length; j++) // i번멤버들 status들에서 지금 멤버의 것을 찾는다.
				{
					memberStatus = memberStatusAll[j];

					if (nodeStatDto.getIpaddress().equals(memberStatus.getMember().getAddress())
							&& nodeStatDto.getPort() == (int) memberStatus.getMember().getPort()) {
						nodeStatDto.setStatus(CommonF5
								.valueStatusAvailability2Int(memberStatus.getObject_status().getAvailability_status()));
						break;
					}
				}

				// 트래픽 계산 변수 초기화
				bytes_in = 0L;
				bytes_out = 0L;
				packets_in = 0L;
				packets_out = 0L;
				maxConn = 0L;
				totalConn = 0L;

				// 트래픽 항목별 값(배열) 가져옴
				iControl.CommonStatistic[] stat = ms.getStatistics();

				for (iControl.CommonStatistic st : stat) {
					if (st.getType() == iControl.CommonStatisticType.STATISTIC_CLIENT_SIDE_BYTES_IN
							|| st.getType() == iControl.CommonStatisticType.STATISTIC_SERVER_SIDE_BYTES_IN) {
						bytes_in += CommonF5.buildLong(st.getValue());
					} else if (st.getType() == iControl.CommonStatisticType.STATISTIC_CLIENT_SIDE_BYTES_OUT
							|| st.getType() == iControl.CommonStatisticType.STATISTIC_SERVER_SIDE_BYTES_OUT) {
						bytes_out += CommonF5.buildLong(st.getValue());
					} else if (st.getType() == iControl.CommonStatisticType.STATISTIC_CLIENT_SIDE_PACKETS_IN
							|| st.getType() == iControl.CommonStatisticType.STATISTIC_SERVER_SIDE_PACKETS_IN) {
						packets_in += CommonF5.buildLong(st.getValue());
					} else if (st.getType() == iControl.CommonStatisticType.STATISTIC_CLIENT_SIDE_PACKETS_OUT
							|| st.getType() == iControl.CommonStatisticType.STATISTIC_SERVER_SIDE_PACKETS_OUT) {
						packets_out += CommonF5.buildLong(st.getValue());
					} else if (st.getType() == iControl.CommonStatisticType.STATISTIC_CLIENT_SIDE_MAXIMUM_CONNECTIONS
							|| st.getType() == iControl.CommonStatisticType.STATISTIC_SERVER_SIDE_MAXIMUM_CONNECTIONS) {
						maxConn += CommonF5.buildLong(st.getValue());
					} else if (st.getType() == iControl.CommonStatisticType.STATISTIC_CLIENT_SIDE_TOTAL_CONNECTIONS
							|| st.getType() == iControl.CommonStatisticType.STATISTIC_SERVER_SIDE_TOTAL_CONNECTIONS) {
						totalConn += CommonF5.buildLong(st.getValue());
					}
				}
				traffic.setInBps(bytes_in);
				traffic.setOutBps(bytes_out);
				traffic.setInPps(packets_in);
				traffic.setOutPps(packets_out);
				traffic.setMaxConnections(maxConn);
				traffic.setTotalConnections(totalConn);
				nodeStatDto.setTraffic(traffic); // node DTO 중 트래픽값

				memberDtoAll.add(nodeStatDto); // 멤버들의 DTO에 추가
			}

			// 이번 pool의 DTO 구성
			poolStatDto.setName(pool_list[i]);
			poolStatDto.setNodeList(memberDtoAll);
			poolStatDtoList.add(poolStatDto);
		}
		return poolStatDtoList;
	}

// test functions ---------------------------------------------------------------------------

//	public static void main(String[] args)
//	{
//		iControl.Interfaces inter = new iControl.Interfaces();
//		
//		inter.initialize("192.168.200.11", "admin", "admin");
//		try
//		{
//			//testSeeAllPool(inter);
//			
//			String [] pool_names = { "Pool_L3", "FWD_192.168.200.0_pool" };
//			testSeeSomePool(inter, pool_names);
//					
//			//Pool.testGetStatistics(inter);
//			//pool = new Pool()
//
//		}
//		catch(Exception e)
//		{
//			System.out.println(e.getMessage());
//			//e.printStackTrace();
//		}
//	}
	public static void testGetStatistics(iControl.Interfaces inter) throws Exception {
		ArrayList<DtoPoolMemberTraffic> poolTraffic = Pool.getStatistics(inter);
		testPrintStatistics(poolTraffic);
	}

	public static void testPrintStatistics(ArrayList<DtoPoolMemberTraffic> poolTraffic) {
		if (poolTraffic == null) {
			return;
		}
		for (DtoPoolMemberTraffic pt : poolTraffic) {
			System.out.println("pool: " + pt.getName());
			ArrayList<DtoNodeTraffic> nodeTraffic = pt.getNodeList();

			for (DtoNodeTraffic nt : nodeTraffic) {
				if ((nt.getTraffic().getInBps() + nt.getTraffic().getOutBps()) > 0) // 트래픽이 있는 것만...
				{
					System.out.println("\t member: " + nt.getIpaddress() + ":" + nt.getPort());
					System.out.println("\t\t status : " + nt.getStatus());
					System.out.println("\t\t bytes in: " + nt.getTraffic().getInBps());
					System.out.println("\t\t bytes out: " + nt.getTraffic().getOutBps());
					System.out.println("\t\t packets in: " + nt.getTraffic().getInPps());
					System.out.println("\t\t packets out: " + nt.getTraffic().getOutPps());
					System.out.println("\t\t current connection: " + nt.getTraffic().getCurrentConnections());
					System.out.println("\t\t max connection: " + nt.getTraffic().getMaxConnections());
					System.out.println("\t\t total connection: " + nt.getTraffic().getTotalConnections());
				}
			}
		}
	}

	public static void testCreatePool(iControl.Interfaces interfaces, String pool_name) {
		int i;
		try {
			validateBaseValues(interfaces);
			if (pool_name == null) {
				throw new Exception("Pool: pool name error(null)");
			}

			Pool pool_new = new Pool(interfaces, pool_name);

			iControl.CommonIPPortDefinition[] members = new iControl.CommonIPPortDefinition[3];
			for (i = 0; i < members.length; i++) {
				members[i] = new iControl.CommonIPPortDefinition();
			}
			members[0].setAddress("10.10.10.100");
			members[0].setPort(7770);
			members[1].setAddress("10.10.10.101");
			members[1].setPort(7771);
			members[2].setAddress("10.10.10.102");
			members[2].setPort(7772);

			long startA = System.currentTimeMillis();
			pool_new.create(iControl.LocalLBLBMethod.LB_METHOD_RATIO_MEMBER, members);
			long endA = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time>Pool Create : " + (endA - startA) / 1000.0);

			long startB = System.currentTimeMillis();
			SystemF5.saveHighConfig(interfaces);
			long endB = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time>SystemF5.SaveHighConfig(): " + (endB - startB) / 1000.0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void testCreatePoolWithNoMember(iControl.Interfaces interfaces, String pool_name) {
		try {
			validateBaseValues(interfaces);
			if (pool_name == null) {
				throw new Exception("Pool: pool name error(null)");
			}
			Pool pool_new = new Pool(interfaces, pool_name);

			long startA = System.currentTimeMillis();
			// pool_new.create(LocalLBLBMethod.LB_METHOD_RATIO_MEMBER, null);
			pool_new.create(iControl.LocalLBLBMethod.LB_METHOD_ROUND_ROBIN, null);

			long endA = System.currentTimeMillis();
			System.out.println("<time>Pool Create : " + (endA - startA) / 1000.0);

			long startB = System.currentTimeMillis();
			SystemF5.saveHighConfig(interfaces);
			long endB = System.currentTimeMillis();
			System.out.println("<time>SystemF5.SaveHighConfig(): " + (endB - startB) / 1000.0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void testDeletePool(iControl.Interfaces interfaces, String pool_name) {
		try {
			validateBaseValues(interfaces);
			if (pool_name == null) {
				throw new Exception("Pool: pool name error(null)");
			}

			long startA = System.currentTimeMillis();
			Pool.delete(interfaces, pool_name);
			long endA = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time>Pool Delete : " + (endA - startA) / 1000.0);

			long startB = System.currentTimeMillis();
			SystemF5.saveHighConfig(interfaces);
			long endB = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time>SystemF5.SaveHighConfig(): " + (endB - startB) / 1000.0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void testAddPoolMember(iControl.Interfaces interfaces, String pool_name) {
		try {
			validateBaseValues(interfaces);
			if (pool_name == null) {
				throw new Exception("Pool: pool name error(null)");
			}

			Pool pTemp = new Pool(interfaces, pool_name);

			long startA = System.currentTimeMillis();
			pTemp.addMember("10.10.10.118", 5555);
			long endA = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time>Member Add : " + (endA - startA) / 1000.0);

			long startB = System.currentTimeMillis();
			SystemF5.saveHighConfig(interfaces);
			long endB = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time>Save Config(High Level): " + (endB - startB) / 1000.0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void testRemovePoolMember(iControl.Interfaces interfaces, String pool_name) {
		try {
			validateBaseValues(interfaces);
			if (pool_name == null) {
				throw new Exception("Pool: pool name error(null)");
			}

			Pool pTemp = new Pool(interfaces, pool_name);

			long startC = System.currentTimeMillis();
			pTemp.removeMember("10.10.10.118", 5555);
			long endC = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time>Member Remove : " + (endC - startC) / 1000.0);

			long startB = System.currentTimeMillis();
			SystemF5.saveHighConfig(interfaces);
			long endB = System.currentTimeMillis();
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG, "<time>SystemF5.SaveHighConfig(): " + (endB - startB) / 1000.0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void testSeeAllPool(iControl.Interfaces interfaces) {
		try {
			validateBaseValues(interfaces);

			String[] pool_list = Pool.getList(interfaces);
			ArrayList<PoolRecord> temp = testSeePool(interfaces, pool_list);
			System.out.println("#pool = " + temp.size());
			// testPrintPool(testSeePool(interfaces, pool_list), "aaa.txt", false);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void testSeeSomePool(iControl.Interfaces interfaces, String[] pool_list) {
		try {
			validateBaseValues(interfaces);

			testPrintPool(testSeePool(interfaces, pool_list), "aaa.txt", false);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static ArrayList<PoolRecord> testSeePool(iControl.Interfaces interfaces, String[] pool_list) {
		int i, j;
		ArrayList<PoolRecord> res = new ArrayList<PoolRecord>();

		try {
			validateBaseValues(interfaces);
			if (pool_list == null) {
				throw new Exception("Pool: pool name error(null)");
			}
			System.out.println("#pool = " + pool_list.length);
			for (String onepool : pool_list) {
				// System.out.println(onepool);
				PoolRecord poolRecordTemp = new PoolRecord();

				Pool pTemp = new Pool(interfaces, onepool);
				poolRecordTemp.setName(pTemp.getName());

				iControl.LocalLBPoolMonitorAssociation monitor = pTemp.getMonitorAssociation();
				poolRecordTemp.setHealthMonitorRule(monitor.getMonitor_rule());
				poolRecordTemp.setLbMethod(pTemp.getLBMethod().getValue());
				poolRecordTemp.setAggregateDynamicRatio(pTemp.getAggregateDynamicRatio());

				iControl.CommonIPPortDefinition[] memberTemp = pTemp.getMembers();
				poolRecordTemp.setMemberCount(memberTemp.length);
				poolRecordTemp.setActiveMemberCount(pTemp.getActiveMemberCount());

				PoolMember pmTemp = new PoolMember(interfaces, poolRecordTemp.getName());
				iControl.LocalLBPoolMemberMemberRatio[] ratioTemp = pmTemp.getRatio();
				iControl.LocalLBPoolMemberMemberPriority[] priorityTemp = pmTemp.getPriorityGroup();
				iControl.LocalLBPoolMemberMemberConnectionLimit[] conLimitTemp = pmTemp.getConnectionLimit();
				iControl.LocalLBPoolMemberMemberObjectStatus[] objStatusTemp = pmTemp.getObjectStatus();
				iControl.LocalLBPoolMemberMemberMonitorStatus[] monStatusTemp = pmTemp.getMonitorStatus();

				Member[] mTemp = new Member[(int) poolRecordTemp.getMemberCount()];

				for (i = 0; i < mTemp.length; i++) {
					mTemp[i] = new Member();
					mTemp[i].setAddress(memberTemp[i].getAddress());
					mTemp[i].setPort(memberTemp[i].getPort());
					mTemp[i].setRatio(-1);
				}
				for (i = 0; i < mTemp.length; i++) {
					for (j = 0; j < ratioTemp.length; j++) {
						if (mTemp[i].getAddress().equals(ratioTemp[j].getMember().getAddress())
								&& mTemp[i].getPort() == ratioTemp[j].getMember().getPort()) {
							mTemp[i].setRatio(ratioTemp[j].getRatio());
							mTemp[i].setPriority(priorityTemp[j].getPriority());
							mTemp[i].setConnectionLimit(conLimitTemp[j].getConnection_limit());
							mTemp[i].setMonitorColor(
									objStatusTemp[j].getObject_status().getAvailability_status().getValue());
							mTemp[i].setState(objStatusTemp[j].getObject_status().getEnabled_status().getValue());
							mTemp[i].setAvailability(objStatusTemp[j].getObject_status().getStatus_description());
							mTemp[i].setMonitorStatus(monStatusTemp[j].getMonitor_status().getValue());
							break;
						}
					}
				}

				ArrayList<Member> arrayTemp = new ArrayList<Member>();
				for (i = 0; i < mTemp.length; i++) {
					arrayTemp.add(mTemp[i]);
				}

				poolRecordTemp.setMembers(arrayTemp);
				res.add(poolRecordTemp);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return res;
	}

	public static void testPrintPool(ArrayList<PoolRecord> res, String report_file, boolean bExtend) {
		BufferedWriter report;
		try {
			report = new BufferedWriter(new FileWriter(report_file, bExtend));
			report.write("\n====================================\n");
			report.write(" P O O L\n");
			int i;
			for (PoolRecord iterR : res) {
				report.write("------------------------------------\n");
				report.write("pool: " + iterR.getName() + "\n");
				report.write("\t health monitor\n");
				report.write("\t\t type: " + iterR.getHealthMonitorRule().getType() + "\n");
				report.write("\t\t template: ");
				for (i = 0; i < iterR.getHealthMonitorRule().getMonitor_templates().length; i++) {
					if (iterR.getHealthMonitorRule().getMonitor_templates()[i] != null) {
						if (i != 0) {
							report.write(", ");
						}
						report.write(iterR.getHealthMonitorRule().getMonitor_templates()[i]);
					}
				}
				report.write("\n");

				if (iterR.getHealthMonitorRule().getQuorum() > 0) {
					report.write("\t\t qurom: " + iterR.getHealthMonitorRule().getQuorum() + "\n");
				}
				report.write("\t load balancing method: " + iterR.getLbMethod() + "\n");
				report.write("\t aggregate dynamic ratio: " + iterR.getAggregateDynamicRatio() + "\n");
				report.write(
						"\t member active/all: " + iterR.getActiveMemberCount() + "/" + iterR.getMemberCount() + "\n");
				report.write("\t member: \n");

				for (i = 0; i < iterR.getMemberCount(); i++) {
					report.write("\t   " + i + ")" + iterR.getMembers().get(i).getAddress() + ":"
							+ iterR.getMembers().get(i).getPort() + "\n");
					report.write("\t\t ratio: " + iterR.getMembers().get(i).getRatio() + "\n");
					report.write("\t\t priority: " + iterR.getMembers().get(i).getPriority() + "\n");
					report.write("\t\t connection limit: " + iterR.getMembers().get(i).getConnectionLimit() + "\n");
					report.write("\t\t monitor color: " + iterR.getMembers().get(i).getMonitorColor() + "\n");
					report.write("\t\t state: " + iterR.getMembers().get(i).getState() + "\n");
					report.write("\t\t availability: " + iterR.getMembers().get(i).getAvailability() + "\n");
					report.write("\t\t monitor status: " + iterR.getMembers().get(i).getMonitorStatus() + "\n");
				}
			}
			report.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void testPoolMonitor(iControl.Interfaces interTemp, String[] pool_names, String report_file) // pool의
																												// monitor를
																												// 설정/조회
	{
		int i;
		ArrayList<PoolRecord> res = new ArrayList<PoolRecord>();

		try {
			for (i = 0; i < pool_names.length; i++) {
				PoolRecord tempPoolRecord = new PoolRecord();
				Pool pool = new Pool(interTemp, pool_names[i]);
				tempPoolRecord.setName(pool_names[i]);
				tempPoolRecord.setHealthMonitorRule(pool.getMonitorAssociation().getMonitor_rule());
			}

			BufferedWriter report = new BufferedWriter(new FileWriter(report_file));
			report.write("config time: " + ManagementDBVariable.getLocalConfigTimeString(interTemp) + "\n\n");

			for (PoolRecord tt : res) {
				report.write("====================================\n");
				report.write("pool: " + tt.getName() + "\n");
				report.write("\thealth monitor\n");
				report.write("\t\ttype: " + tt.getHealthMonitorRule().getType().toString() + "\n");
				report.write("\t\ttemplate: ");
				for (i = 0; i < tt.getHealthMonitorRule().getMonitor_templates().length; i++) {
					if (tt.getHealthMonitorRule().getMonitor_templates()[i] != null) {
						if (i != 0) {
							report.write(", ");
						}
						report.write(tt.getHealthMonitorRule().getMonitor_templates()[i]);
					}
				}
				report.write("\n");

				if (tt.getHealthMonitorRule().getType() == iControl.LocalLBMonitorRuleType.MONITOR_RULE_TYPE_M_OF_N) {
					report.write("\t\tqurom: " + tt.getHealthMonitorRule().getQuorum() + "\n");
				}
			}
			report.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}