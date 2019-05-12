package kr.openbase.adcsmart.service.impl.f5;
//import java.io.*;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberF5;
import kr.openbase.adcsmart.service.utility.OBDefine;

class PoolMember {
	private iControl.Interfaces _interfaces = null;
	private iControl.LocalLBPoolMemberPortType _LocalLBPoolMember = null;
	private String _pool_name = null;

	// constructor
	PoolMember(iControl.Interfaces interfaces, String pool_name) throws Exception {
		_interfaces = interfaces;
		_LocalLBPoolMember = _interfaces.getLocalLBPoolMember();
		_pool_name = pool_name;
	}

	private void validateBaseValues() throws Exception {
		if (_interfaces == null) {
			throw new Exception("PoolMember: F5 Interfaces error(null)");
		} else if (_pool_name == null) {
			throw new Exception("PoolMember: pool name error(null)");
		} else if (_LocalLBPoolMember == null) {
			throw new Exception("PoolMember: F5 API Stub error(null)");
		}
	}

	private static void validateBaseValues(iControl.Interfaces interfaces) throws Exception {
		if (interfaces == null) {
			throw new Exception("PoolMember: F5 interface error(null)");
		}
	}

	public iControl.LocalLBPoolMemberMemberRatio[] getRatio() throws Exception {
		validateBaseValues();

		String[] pool_names = { _pool_name };
		iControl.LocalLBPoolMemberMemberRatio[][] ratio = _LocalLBPoolMember.get_ratio(pool_names);
		return ratio[0];
	}

	public iControl.LocalLBPoolMemberMemberPriority[] getPriorityGroup() throws Exception {
		validateBaseValues();

		String[] pool_names = { _pool_name };
		iControl.LocalLBPoolMemberMemberPriority[][] prioritygroup = _LocalLBPoolMember.get_priority(pool_names);
		return prioritygroup[0];
	}

	public iControl.LocalLBPoolMemberMemberConnectionLimit[] getConnectionLimit() throws Exception {
		validateBaseValues();

		String[] pool_names = { _pool_name };
		iControl.LocalLBPoolMemberMemberConnectionLimit[][] conn_limit = _LocalLBPoolMember
				.get_connection_limit(pool_names);
		return conn_limit[0];
	}

	public iControl.LocalLBPoolMemberMemberObjectStatus[] getObjectStatus() throws Exception {
		validateBaseValues();

		String[] pool_names = { _pool_name };
		iControl.LocalLBPoolMemberMemberObjectStatus[][] obj_status = _LocalLBPoolMember.get_object_status(pool_names);
		return obj_status[0];
	}

	private static iControl.LocalLBPoolMemberMemberRatio[][] getRatio(iControl.Interfaces interfaces,
			String[] pool_names) throws Exception {
		validateBaseValues(interfaces);

		iControl.LocalLBPoolMemberMemberRatio[][] memberRatio = interfaces.getLocalLBPoolMember().get_ratio(pool_names);
		return memberRatio;
	}

	public iControl.LocalLBPoolMemberMemberMonitorStatus[] getMonitorStatus() throws Exception {
		validateBaseValues();

		String[] pool_names = { _pool_name };
		iControl.LocalLBPoolMemberMemberMonitorStatus[][] status = _LocalLBPoolMember.get_monitor_status(pool_names);
		return status[0];
	}

	private static iControl.LocalLBPoolMemberMemberMonitorStatus[][] getMonitorStatus(iControl.Interfaces interfaces,
			String[] pool_names) throws Exception {
		validateBaseValues(interfaces);

		iControl.LocalLBPoolMemberMemberMonitorStatus[][] status = interfaces.getLocalLBPoolMember()
				.get_monitor_status(pool_names);
		return status;
	}

	public void setMonitorState(iControl.LocalLBPoolMemberMemberMonitorState[] state) throws Exception {
		validateBaseValues();

		String[] pool_names = { _pool_name };
		iControl.LocalLBPoolMemberMemberMonitorState[][] _state = { state };

		_LocalLBPoolMember.set_monitor_state(pool_names, _state);
	}

	/*
	 * //STATE 판별 방법 Enabled (All traffic allowed): object_enabled_status :
	 * ENABLED_STATUS_ENABLED (= session_enabled_state : SESSION_STATUS_ENABLED)
	 * monitor_state : 상관없음
	 * 
	 * Disabled (Only persistent or active connections allowed):
	 * object_enabled_status : ENABLED_STATUS_DISABLED(= session_enabled_state :
	 * SESSION_STATUS_FORCED_DISABLED) monitor_state : offline 외
	 * 
	 * Forced Offline (Only active connections allowed): object_enabled_status :
	 * ENABLED_STATUS_DISABLED(= session_enabled_state :
	 * SESSION_STATUS_FORCED_DISABLED) monitor_state : MONITOR_STATUS_FORCED_DOWN
	 */
	static ArrayList<ArrayList<DtoPoolMember>> get(iControl.Interfaces interfaces, String[] pool_list)
			throws Exception {
		validateBaseValues(interfaces);
		if (pool_list == null) {
			return new ArrayList<ArrayList<DtoPoolMember>>();
		}
		int poolNum = pool_list.length;
		if (poolNum == 0) {
			return new ArrayList<ArrayList<DtoPoolMember>>();
		}

		ArrayList<ArrayList<DtoPoolMember>> memberListSet = new ArrayList<ArrayList<DtoPoolMember>>();
		iControl.LocalLBPoolMemberMemberMonitorStatus[][] monitorStatusList = interfaces.getLocalLBPoolMember()
				.get_monitor_status(pool_list);
		iControl.LocalLBPoolMemberMemberObjectStatus[][] objectStatusList = interfaces.getLocalLBPoolMember()
				.get_object_status(pool_list);
		iControl.LocalLBPoolMemberMemberSessionState[][] sessionStatusList = interfaces.getLocalLBPoolMember()
				.get_session_enabled_state(pool_list);
		iControl.LocalLBPoolMemberMemberRatio[][] ratioList = PoolMember.getRatio(interfaces, pool_list);

		int i, j;

		for (i = 0; i < poolNum; i++) {
			iControl.LocalLBPoolMemberMemberMonitorStatus[] monitorStatus = monitorStatusList[i];
			iControl.LocalLBPoolMemberMemberObjectStatus[] objectStatus = objectStatusList[i];
			iControl.LocalLBPoolMemberMemberSessionState[] sessionStatus = sessionStatusList[i];
			iControl.LocalLBPoolMemberMemberRatio[] ratios = ratioList[i];

			ArrayList<DtoPoolMember> memberList = new ArrayList<DtoPoolMember>();

			for (j = 0; j < monitorStatus.length; j++) {
				DtoPoolMember member = new DtoPoolMember();
				member.setIpAddress(monitorStatus[j].getMember().getAddress());
				member.setPort((int) monitorStatus[j].getMember().getPort());
				// 원래 member state 구하는 루틴
//				if(monitorStatus[j].getMonitor_status().equals(iControl.LocalLBMonitorStatus.MONITOR_STATUS_FORCED_DOWN) 
//						&& sessionStatus[j].getSession_state().equals(iControl.CommonEnabledState.STATE_DISABLED))
//                {
//                    member.setState(OBDefine.MEMBER_STATE.FORCED_OFFLINE);
//                }
//                else if(monitorStatus[j].getMonitor_status().equals(iControl.LocalLBMonitorStatus.MONITOR_STATUS_UP) 
//						&& sessionStatus[j].getSession_state().equals(iControl.CommonEnabledState.STATE_ENABLED))
//                {
//                    member.setState(OBDefine.MEMBER_STATE.ENABLE);
//                }
//                else //확인 필요
//                {
//                    member.setState(OBDefine.MEMBER_STATE.DISABLE);
//                }
				// 위 루틴을 node와 같은 방식으로 맞춤. 이 쪽이 더 조사한 후의 결과이며, 둘 사이 결과 차이는 없음
				if (objectStatus[j].getObject_status().getEnabled_status()
						.equals(iControl.LocalLBEnabledStatus.ENABLED_STATUS_ENABLED)
						|| sessionStatus[j].getSession_state().equals(iControl.CommonEnabledState.STATE_ENABLED)) {
					member.setState(OBDefine.MEMBER_STATE.ENABLE);
				} else // ==
						// if(objectStatus[j].getObject_status().getEnabled_status().equals(iControl.LocalLBEnabledStatus.ENABLED_STATUS_DISABLED))
				{
					if (monitorStatus[j].getMonitor_status()
							.equals(iControl.LocalLBMonitorStatus.MONITOR_STATUS_FORCED_DOWN)) {
						member.setState(OBDefine.MEMBER_STATE.FORCED_OFFLINE);
					} else {
						member.setState(OBDefine.MEMBER_STATE.DISABLE);
					}
				}
				// ratio
				member.setRatio((int) ratios[j].getRatio());
				// status !state
				member.setStatus(CommonF5
						.valueStatusAvailability2Int(objectStatus[j].getObject_status().getAvailability_status()));
				memberList.add(member);
			}
			// 특정 pool 소속의 member들을 pmDtos에 모았으므로 한꺼번에 종합 집결지에 넣는다.
			memberListSet.add(memberList);
		}
		return memberListSet;
	}
	// 이 함수는 만들고 확인 안함.

	public iControl.LocalLBPoolMemberMemberSessionState[] getSessionEnabledState() throws Exception {
		validateBaseValues();

		String[] pool_names = { _pool_name };
		iControl.LocalLBPoolMemberMemberSessionState[][] state = _LocalLBPoolMember
				.get_session_enabled_state(pool_names);
		return state[0];
	}

	private static iControl.LocalLBPoolMemberMemberSessionState[][] getSessionEnabledState(
			iControl.Interfaces interfaces, String[] pool_names) throws Exception {
		validateBaseValues(interfaces);

		iControl.LocalLBPoolMemberMemberSessionState[][] status = interfaces.getLocalLBPoolMember()
				.get_session_enabled_state(pool_names);
		return status;
	}

	public void setSessionEnabledState(iControl.LocalLBPoolMemberMemberSessionState[] state) throws Exception {
		validateBaseValues();

		String[] pool_names = { _pool_name };
		iControl.LocalLBPoolMemberMemberSessionState[][] _state = { state };

		_LocalLBPoolMember.set_session_enabled_state(pool_names, _state);
	}

	/*
	 * Enabled (All traffic allowed): set_monitor_state : STATE_ENABLED
	 * set_session_enabled_state : STATE_ENABLED
	 * 
	 * Disabled (Only persistent or active connections allowed): set_monitor_state :
	 * STATE_ENABLED set_session_enabled_state : STATE_DISABLED
	 * 
	 * Forced Offline (Only active connections allowed): set_monitor_state :
	 * STATE_DISABLED set_session_enabled_state : STATE_DISABLED
	 * 
	 * 현재 ADCsmart에서는 enabled 상태는 맞으나 disabled 상태는 forced offline을 보여주고 있었음.
	 */
	public void setMemberState(ArrayList<OBDtoAdcPoolMemberF5> poolMembers) throws Exception {
		validateBaseValues();

		int memberNum = poolMembers.size();
		int i;
		iControl.LocalLBPoolMemberMemberSessionState[] stateSession = new iControl.LocalLBPoolMemberMemberSessionState[memberNum];
		iControl.LocalLBPoolMemberMemberMonitorState[] stateMonitor = new iControl.LocalLBPoolMemberMemberMonitorState[memberNum];

		for (i = 0; i < memberNum; i++) {
			iControl.CommonIPPortDefinition memberDef = new iControl.CommonIPPortDefinition();

			stateSession[i] = new iControl.LocalLBPoolMemberMemberSessionState();
			stateMonitor[i] = new iControl.LocalLBPoolMemberMemberMonitorState();

			OBDtoAdcPoolMemberF5 pm = poolMembers.get(i);

			memberDef.setAddress(pm.getIpAddress());
			memberDef.setPort(pm.getPort());

			stateMonitor[i].setMember(memberDef);
			stateSession[i].setMember(memberDef);

			if (pm.getState().equals(OBDefine.MEMBER_STATE.ENABLE)) {
				stateMonitor[i].setMonitor_state(iControl.CommonEnabledState.STATE_ENABLED);
				stateSession[i].setSession_state(iControl.CommonEnabledState.STATE_ENABLED);
			} else if (pm.getState().equals(OBDefine.MEMBER_STATE.FORCED_OFFLINE)) {
				stateMonitor[i].setMonitor_state(iControl.CommonEnabledState.STATE_DISABLED);
				stateSession[i].setSession_state(iControl.CommonEnabledState.STATE_DISABLED);
			} else // == if(pm.getState().equals(OBDefine.MEMBER_STATE.DISABLE))
			{
				stateMonitor[i].setMonitor_state(iControl.CommonEnabledState.STATE_ENABLED);
				stateSession[i].setSession_state(iControl.CommonEnabledState.STATE_DISABLED);
			}
		}

		setSessionEnabledState(stateSession);
		setMonitorState(stateMonitor);
	}

// ----------------------- test functions ------------------------------

//	public static void main(String[] args)
//	{
//		testShowPoolMember();
////		testShowPoolMemberRatio();
//	}

//	mon status            = MONITOR_STATUS_UP
//	session status        = STATE_ENABLED
//	mon status            = MONITOR_STATUS_FORCED_DOWN
//	session status        = STATE_DISABLED
	public static void testShowPoolMember() {
		iControl.Interfaces interfaces = new iControl.Interfaces();
		interfaces.initialize("192.168.200.11", "admin", "admin");
		int i = 0, j = 0;
		try {
			String[] poolNames = { "MONITOR_POOL1", "MONITOR_POOL4" };
			iControl.LocalLBPoolMemberMemberMonitorStatus[][] monStatus = PoolMember.getMonitorStatus(interfaces,
					poolNames);
			iControl.LocalLBPoolMemberMemberSessionState[][] sesStatus = PoolMember.getSessionEnabledState(interfaces,
					poolNames);
			for (i = 0; i < monStatus.length; i++) {
				System.out.println(String.format("member        monitorStatus    sessionStatus   ObjectStatus"));
				System.out.println(String.format("--------------------------------------------------------------"));
				for (j = 0; j < monStatus[i].length; j++) {
					System.out.println(String.format("[%d:%s:%d] %s \t %s", i, monStatus[i][j].getMember().getAddress(),
							monStatus[i][j].getMember().getPort(), monStatus[i][j].getMonitor_status().getValue(),
							sesStatus[i][j].getSession_state().getValue()));
				}
				System.out.println(String.format("--------------------------------------------------------------"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}
	}

	public static void testShowPoolMemberRatio() {
		iControl.Interfaces interfaces = new iControl.Interfaces();
		interfaces.initialize("192.168.200.11", "admin", "admin");
		String poolName = "vs192.168.200.252_Pool_1";
		int i = 0, j = 0;
		try {
			PoolMember pm = new PoolMember(interfaces, poolName);
			iControl.LocalLBPoolMemberMemberRatio[] ratio = pm.getRatio();
			String[] poolNames = { "vs192.168.200.252_Pool_1", "MONITOR_POOL1", "MONITOR_POOL4" };
			iControl.LocalLBPoolMemberMemberRatio[][] pmRatioAll = PoolMember.getRatio(interfaces, poolNames);
			for (j = 0; j < pmRatioAll.length; j++) {
				for (i = 0; i < ratio.length; i++) {
					System.out.println(
							String.format("[%d:%s:%d] pmRatioAll = %s", i, pmRatioAll[j][i].getMember().getAddress(),
									pmRatioAll[j][i].getMember().getPort(), pmRatioAll[j][i].getRatio()));
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}
	}

	public void testMember() {
		iControl.Interfaces inter = new iControl.Interfaces();

		inter.initialize("192.168.200.11", "admin", "admin");
		try {
			PoolMember pm = new PoolMember(inter, "vs192.168.200.252_Pool_1");
			iControl.LocalLBPoolMemberMemberObjectStatus[] objstatus = pm.getObjectStatus();
			iControl.LocalLBPoolMemberMemberMonitorStatus[] monstatus = pm.getMonitorStatus();
			iControl.LocalLBPoolMemberMemberSessionState[] sesstate = pm.getSessionEnabledState();

			for (int i = 0; i < objstatus.length; i++) {
				System.out.println("obj status member     = " + objstatus[i].getMember().getAddress() + ":"
						+ objstatus[i].getMember().getPort());
				System.out.println("obj status avail      = "
						+ objstatus[i].getObject_status().getAvailability_status().getValue());
				System.out.println(
						"obj status enabled    = " + objstatus[i].getObject_status().getEnabled_status().getValue());
				System.out
						.println("obj status descr      = " + objstatus[i].getObject_status().getStatus_description());

				// System.out.println("mon status member = " +
				// monstatus[i].getMember().getAddress() + ":" +
				// monstatus[i].getMember().getPort());
				System.out.println("monitor status            = " + monstatus[i].getMonitor_status().getValue());

				// System.out.println("session status member = " +
				// sesstate[i].getMember().getAddress() + ":" +
				// sesstate[i].getMember().getPort());
				System.out.println("session status        = " + sesstate[i].getSession_state().getValue());
			}

			iControl.LocalLBPoolMemberMemberMonitorState monstate = new iControl.LocalLBPoolMemberMemberMonitorState();
			iControl.LocalLBPoolMemberMemberSessionState sesstate2 = new iControl.LocalLBPoolMemberMemberSessionState();
			iControl.LocalLBPoolMemberMemberMonitorState[] _monstate = { monstate };
			iControl.LocalLBPoolMemberMemberSessionState[] _sesstate = { sesstate2 };

			monstate.setMember(objstatus[0].getMember());
			monstate.setMonitor_state(iControl.CommonEnabledState.STATE_ENABLED);
//			monstate.setMonitor_state(iControl.CommonEnabledState.STATE_DISABLED);
			pm.setMonitorState(_monstate);

			sesstate2.setMember(objstatus[0].getMember());
//			sesstate2.setSession_state(iControl.CommonEnabledState.STATE_ENABLED);
			sesstate2.setSession_state(iControl.CommonEnabledState.STATE_DISABLED);
			pm.setSessionEnabledState(_sesstate);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// e.printStackTrace();
		}
	}
}