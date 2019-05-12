package kr.openbase.adcsmart.service.impl.f5;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

import iControl.CommonEnabledState;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class NodeAddress {
	private iControl.Interfaces _interfaces = null;
	private String _address = null;

	// member - interfaces
	public iControl.Interfaces getInterfaces() {
		return _interfaces;
	}

	public void setInterfaces(iControl.Interfaces interfaces) {
		_interfaces = interfaces;
	}

	// member - address
	public String getAddress() {
		return _address;
	}

	public void setAddress(String address) {
		_address = address;
	}

	private static void validateBaseValues(iControl.Interfaces interfaces) throws Exception {
		if (interfaces == null) {
			throw new Exception("NodeAddress: F5 interface error(null)");
		}
	}

	private static String[] getList(iControl.Interfaces interfaces) throws Exception {
		validateBaseValues(interfaces);

		String[] node_list = null;
		node_list = interfaces.getLocalLBNodeAddress().get_list();
		return node_list;
	}

	private static String[] getScreenName(iControl.Interfaces interfaces, String[] nodeAddresses) throws Exception {
		validateBaseValues(interfaces);

		String[] node_names = null;
		node_names = interfaces.getLocalLBNodeAddress().get_screen_name(nodeAddresses);
		return node_names;
	}

	private static long[] getRatio(iControl.Interfaces interfaces, String[] nodeAddresses) throws Exception {
		validateBaseValues(interfaces);
		long[] node_ratio = null;

		node_ratio = interfaces.getLocalLBNodeAddress().get_ratio(nodeAddresses);
		return node_ratio;
	}

	// node 일괄 추가
	static void create(iControl.Interfaces interfaces, String[] nodeAddresses) throws Exception {
		long[] connectionLimits = new long[nodeAddresses.length];
		for (int i = 0; i < connectionLimits.length; i++) {
			connectionLimits[i] = 0L;
		}

		create(interfaces, nodeAddresses, connectionLimits);

	}

	private static void create(iControl.Interfaces interfaces, String[] nodeAddresses, long[] connectionLimits)
			throws Exception {
		validateBaseValues(interfaces);

		interfaces.getLocalLBNodeAddress().create(nodeAddresses, connectionLimits);
	}

	// node 일괄 삭제
	static void delete(iControl.Interfaces interfaces, String[] nodeAddresses) throws Exception {
		validateBaseValues(interfaces);

		interfaces.getLocalLBNodeAddress().delete_node_address(nodeAddresses);
	}

	/**
	 * 모든 노드의 정보 가져오기
	 * 
	 * @param interfaces
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<OBDtoAdcNodeF5> getAll(iControl.Interfaces interfaces) throws Exception {
		validateBaseValues(interfaces);
		String[] node_list = NodeAddress.getList(interfaces);

		ArrayList<OBDtoAdcNodeF5> nodeRecSet = get(interfaces, node_list);
		return nodeRecSet;
	}

	/**
	 * 요청한 노드 목록의 정보 가져오기, getAll()도 모든 목록을 조회한다는 것 말고는 같은 기능이므로 전체 node목록을 파라미터로 이
	 * 함수를 호출한다. node 정보 가져오는 함수는 원래 getAll()만 있었다가 부분업데이트를 도입하면서 함수를 분리했다.
	 * 
	 * @param interfaces
	 * @param node_list
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<OBDtoAdcNodeF5> get(iControl.Interfaces interfaces, String[] node_list) throws Exception {
		validateBaseValues(interfaces);
		ArrayList<OBDtoAdcNodeF5> nodeRecSet = new ArrayList<OBDtoAdcNodeF5>();

		String[] node_name = NodeAddress.getScreenName(interfaces, node_list);
		long[] node_ratio = NodeAddress.getRatio(interfaces, node_list);
		int[] node_state = NodeAddress.getState(interfaces, node_list);
		int[] node_status = NodeAddress.getStatus(interfaces, node_list);
		int i;
		for (i = 0; i < node_list.length; i++) {
			OBDtoAdcNodeF5 nodeRec = new OBDtoAdcNodeF5();
			nodeRec.setIpAddress(node_list[i]);
			nodeRec.setState(node_state[i]); // node state는 pask, F5만 유효
			nodeRec.setStatus(node_status[i]);
			nodeRec.setRatio((int) node_ratio[i]);
			nodeRec.setName(node_name[i]);
			nodeRecSet.add(nodeRec);
		}
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "node count: " + nodeRecSet.size());
		return nodeRecSet;
	}

	/*
	 * //STATE 설정방법 Enabled (All traffic allowed): set_session_enabled_state :
	 * STATE_ENABLED set_monitor_state : STATE_ENABLED
	 * 
	 * Disabled (Only persistent or active connections allowed):
	 * set_session_enabled_state : STATE_DISABLED set_monitor_state : STATE_ENABLED
	 * 
	 * Forced Offline (Only active connections allowed): set_session_enabled_state :
	 * STATE_DISABLED set_monitor_state : STATE_DISABLED
	 */
	static void setState(iControl.Interfaces interfaces, ArrayList<OBDtoAdcNodeF5> nodeList, Integer action)
			throws Exception {
		validateBaseValues(interfaces);

		if (nodeList == null || nodeList.size() == 0) {
			return;
		}

		int nodeNum = nodeList.size();
		int i;

		String[] node_addresses = new String[nodeNum];
		iControl.CommonEnabledState[] stateSession = new iControl.CommonEnabledState[nodeNum];
		iControl.CommonEnabledState[] stateMonitor = new iControl.CommonEnabledState[nodeNum];

		OBDtoAdcNodeF5 currentNode = null;
		CommonEnabledState targetSessionState = null;
		CommonEnabledState targetMonitorState = null;
		if (action == OBDefine.MEMBER_STATE.ENABLE) {
			targetSessionState = iControl.CommonEnabledState.STATE_ENABLED;
			targetMonitorState = iControl.CommonEnabledState.STATE_ENABLED;
		} else if (action.equals(OBDefine.MEMBER_STATE.FORCED_OFFLINE)) {
			targetSessionState = iControl.CommonEnabledState.STATE_DISABLED;
			targetMonitorState = iControl.CommonEnabledState.STATE_DISABLED;
		} else {
			targetSessionState = iControl.CommonEnabledState.STATE_DISABLED;
			targetMonitorState = iControl.CommonEnabledState.STATE_ENABLED;
		}

		for (i = 0; i < nodeNum; i++) {
			currentNode = nodeList.get(i);
			node_addresses[i] = currentNode.getIpAddress();
			stateSession[i] = targetSessionState;
			stateMonitor[i] = targetMonitorState;
			/*
			 * // if(currentNode.getState().equals(OBDefine.MEMBER_STATE.ENABLE))
			 * if(action.equals(OBDefine.MEMBER_STATE.ENABLE)) { stateSession[i] =
			 * iControl.CommonEnabledState.STATE_ENABLED; stateMonitor[i] =
			 * iControl.CommonEnabledState.STATE_ENABLED; } // else
			 * if(currentNode.getState().equals(OBDefine.MEMBER_STATE.FORCED_OFFLINE)) else
			 * if(action.equals(OBDefine.MEMBER_STATE.FORCED_OFFLINE)) { stateSession[i] =
			 * iControl.CommonEnabledState.STATE_DISABLED; stateMonitor[i] =
			 * iControl.CommonEnabledState.STATE_DISABLED; } else // ==
			 * if(currentNode.getState().equals(OBDefine.MEMBER_STATE.DISABLE)) {
			 * stateSession[i] = iControl.CommonEnabledState.STATE_DISABLED; stateMonitor[i]
			 * = iControl.CommonEnabledState.STATE_ENABLED; }
			 */
		}
//		interfaces.getLocalLBNodeAddress().set_session_enabled_state(node_addresses, stateSession);	
//		interfaces.getLocalLBNodeAddress().set_monitor_state(node_addresses, stateMonitor);
		interfaces.getLocalLBNodeAddress().set_session_enabled_state(node_addresses, stateSession);
		interfaces.getLocalLBNodeAddress().set_monitor_state(node_addresses, stateMonitor);
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
	private static int[] getState(iControl.Interfaces interfaces, String[] node_list) throws Exception {
		validateBaseValues(interfaces);
		if (node_list == null) {
			return new int[0];
		}

		int nodeNum = node_list.length;
		if (nodeNum == 0) {
			return new int[0];
		}
		iControl.LocalLBMonitorStatus[] monitorStatus = interfaces.getLocalLBNodeAddress()
				.get_monitor_status(node_list);
		iControl.LocalLBObjectStatus[] objectStatus = interfaces.getLocalLBNodeAddress().get_object_status(node_list);

		// 예전에는 get_session_enabled_state()를 호출했으나, deprecated되고, 이 함수로 대체
		// object status로 확인하기 때문에 호출할 필요가 없어 막았다.
		// iControl.LocalLBSessionStatus[] sessionStatus =
		// interfaces.getLocalLBNodeAddress().get_session_status(node_list);

		int i;
		int[] state_list = new int[nodeNum];

		for (i = 0; i < nodeNum; i++) {
			if (objectStatus[i].getEnabled_status().equals(iControl.LocalLBEnabledStatus.ENABLED_STATUS_ENABLED)) {
				state_list[i] = OBDefine.MEMBER_STATE.ENABLE;
			} else // ==
					// if(objectStatus[i].getEnabled_status().equals(iControl.LocalLBEnabledStatus.ENABLED_STATUS_DISABLED))
			{
				if (monitorStatus[i].equals(iControl.LocalLBMonitorStatus.MONITOR_STATUS_FORCED_DOWN)) {
					state_list[i] = OBDefine.MEMBER_STATE.FORCED_OFFLINE;
				} else {
					state_list[i] = OBDefine.MEMBER_STATE.DISABLE;
				}
			}
		}
		return state_list;
	}

	private static int[] getStatus(iControl.Interfaces interfaces, String[] node_list) throws Exception {
		validateBaseValues(interfaces);
		if (node_list == null) {
			return new int[0];
		}

		int nodeNum = node_list.length;
		if (nodeNum == 0) {
			return new int[0];
		}

		iControl.LocalLBObjectStatus[] objectStatus = interfaces.getLocalLBNodeAddress().get_object_status(node_list);

		int i;
		int[] state_list = new int[nodeNum];

		for (i = 0; i < nodeNum; i++) {
			if (objectStatus[i].getAvailability_status()
					.equals(iControl.LocalLBAvailabilityStatus.AVAILABILITY_STATUS_GREEN)) {
				state_list[i] = OBDefine.MEMBER_STATUS.AVAILABLE;
			} else if (objectStatus[i].getAvailability_status()
					.equals(iControl.LocalLBAvailabilityStatus.AVAILABILITY_STATUS_GRAY)
					|| objectStatus[i].getAvailability_status()
							.equals(iControl.LocalLBAvailabilityStatus.AVAILABILITY_STATUS_YELLOW)) {
				state_list[i] = OBDefine.MEMBER_STATUS.UNAVAILABLE;
			} else if (objectStatus[i].getAvailability_status()
					.equals(iControl.LocalLBAvailabilityStatus.AVAILABILITY_STATUS_RED)) {
				state_list[i] = OBDefine.MEMBER_STATUS.DISABLE;
			} else {
				state_list[i] = OBDefine.MEMBER_STATUS.AVAILABLE;
			}

//            System.out.println("objectStatus getAvailability_status");
//            for(i=0; i<nodeNum; i++)
//            {                
//                System.out.println(objectStatus[i].getAvailability_status() + "\n" + iControl.LocalLBAvailabilityStatus.AVAILABILITY_STATUS_BLUE + "\n" + iControl.CommonAvailabilityStatus.AVAILABILITY_STATUS_GREEN + "\n" + state_list[i]);               
//            }
		}
		return state_list;
	}

	/**
	 * 설정하려고 하는 node들의 상태가 device와 같은지(설정 동기화 상태인지) 확인한다.
	 * 
	 * @param interfaces
	 * @param nodeList
	 * @return false: device와 다름, true: device와 일치함
	 * @throws Exception Sync가 맞지 않으면 업데이트할 리스트를 반환하고, Sync가 맞으면 null을 리턴한다.
	 */
	public ArrayList<OBDtoAdcNodeF5> checkConfigSync(iControl.Interfaces interfaces, ArrayList<OBDtoAdcNodeF5> nodeList,
			ArrayList<OBDtoAdcNodeF5> dbNodes) throws Exception {
		boolean check = false;
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "config compare nodes = " + nodeList);
		String[] nodeIpArray = getNodeIpArrayFromNodeList(nodeList);

		// F5에서 node를 구한다.
		ArrayList<OBDtoAdcNodeF5> deviceNodes = get(interfaces, nodeIpArray);

		if (dbNodes.size() != nodeList.size()) // 개수로만 두 리스트가 같은지 확인한다. 일대일 비교 생략
		{ // client node 목록과 DB의 node 목록이 다름
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("Requested nodes aren't matched with DB data."));
			return deviceNodes; // 일치하지 않음. 업데이트 해야함
		}

		OBDtoAdcNodeF5 deviceNode;
		for (OBDtoAdcNodeF5 localNode : dbNodes) {
			deviceNode = null;
			OBSystemLog.error(OBDefine.LOGFILE_DEBUG, "db node = " + localNode.getIpAddress());
			for (OBDtoAdcNodeF5 dNode : deviceNodes) {
				if (localNode.getIpAddress().equals(dNode.getIpAddress())) {
					deviceNode = dNode;
					break;
				}
			}
			if (deviceNode == null) // 맞는 node가 device에 없었음. device와 설정이 일치하지 않는다.
			{
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						String.format("node %s doesn't exist on device.", localNode.getIpAddress()));
				check = false;
			} else {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "node in DB = " + localNode);
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "node in F5 = " + deviceNode);
				check = localNode.configEquals(deviceNode); // 두개 노드가 같은지 비교한다. 장비에서 받아오는 설정만 같은지 비교한다. 일부 필드들은 device에서
															// 읽을 때는 없다.
			}
			if (check == false) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						String.format("node '%s' config comparison: DB node != device node", localNode.getIpAddress()));
				return deviceNodes;
			} else {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						String.format("node '%s' config comparison: DB node == device node", localNode.getIpAddress()));
			}
		}
		return null;
	}

	/**
	 * Node 목록에서 IP를 배열로 뽑는다.
	 * 
	 * @param nodeList
	 * @return
	 */
	public String[] getNodeIpArrayFromNodeList(ArrayList<OBDtoAdcNodeF5> nodeList) {
		String[] nodeIpArray = new String[nodeList.size()];
		int i = 0;
		for (OBDtoAdcNodeF5 node : nodeList) {
			nodeIpArray[i++] = node.getIpAddress();
		}
		return nodeIpArray;
	}

	/**
	 * Node 목록에서 IP를 ArrayList로 뽑는다.
	 * 
	 * @param nodeList
	 * @return
	 */
	public ArrayList<String> getNodeIndexListFromNodeList(ArrayList<OBDtoAdcNodeF5> nodeList) {
		ArrayList<String> nodeIndexList = new ArrayList<String>();
		for (OBDtoAdcNodeF5 node : nodeList) {
			nodeIndexList.add(node.getIndex());
		}
		return nodeIndexList;
	}

//  test functions -------------------------------------------------------------------------------------------
	public static void getAllTest() throws Exception {
		iControl.Interfaces interfaces = new iControl.Interfaces();
		interfaces.initialize("192.168.200.12", "admin", "admin");
		int i;

		validateBaseValues(interfaces);

		ArrayList<OBDtoAdcNodeF5> nodes = NodeAddress.getAll(interfaces);
		OBDtoAdcNodeF5 node = null;
		System.out.println("   N O D E      state     Status    name");
		System.out.println("------------------------------------");
		for (i = 0; i < nodes.size(); i++) {
			node = nodes.get(i);
			System.out.println(" " + node.getIpAddress() + " \t " + node.getState() + " \t " + node.getStatus() + "\t"
					+ node.getName());
		}
	}

	public static void testPrintNodeAddress(ArrayList<NodeAddress> nodeList, String report_file, boolean bExtend)
			throws Exception {
		BufferedWriter report;

		report = new BufferedWriter(new FileWriter(report_file, bExtend));
		report.write("\n====================================\n");
		report.write(" N O D E  A D D R E S S\n");
		report.write("------------------------------------\n");
		for (NodeAddress node : nodeList) {
			report.write("\t " + node.getAddress() + "\n");
			System.out.print("\t " + node.getAddress() + "\n");
		}
		report.close();
	}

	public static void setStateTest() {
		iControl.Interfaces interfaces = new iControl.Interfaces();
		interfaces.initialize("192.168.200.11", "admin", "admin");
		ArrayList<OBDtoAdcNodeF5> nodeList = new ArrayList<OBDtoAdcNodeF5>();
		Integer action = 1;
		OBDtoAdcNodeF5 node1 = new OBDtoAdcNodeF5();
		node1.setIpAddress("2.2.2.2");
		node1.setState(OBDefine.MEMBER_STATE.ENABLE);

		OBDtoAdcNodeF5 node2 = new OBDtoAdcNodeF5();
		node2.setIpAddress("172.16.50.11");
		node2.setState(OBDefine.MEMBER_STATE.DISABLE);

		OBDtoAdcNodeF5 node3 = new OBDtoAdcNodeF5();
		node3.setIpAddress("121.254.130.222");
		node3.setState(OBDefine.MEMBER_STATE.FORCED_OFFLINE);

		nodeList.add(node1);
		nodeList.add(node2);
		nodeList.add(node3);

		try {
			NodeAddress.setState(interfaces, nodeList, action);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 아래 함수는 node의 모든 종류의 상태를 찍는 함수이다.
	// 테스트함수지만 유용하므로 지우거나 바꾸지 말기.
	public static int[] testPrintAllState(iControl.Interfaces interfaces, String[] node_list) throws Exception {
		validateBaseValues(interfaces);
		if (node_list == null) {
			return null;
		}
		int nodeNum = node_list.length;
		if (nodeNum == 0) {
			return null;
		}

		iControl.LocalLBMonitorStatus[] monitorStatus = interfaces.getLocalLBNodeAddress()
				.get_monitor_status(node_list);
		iControl.LocalLBSessionStatus[] sessionStatus = interfaces.getLocalLBNodeAddress()
				.get_session_status(node_list); // 예전에는 get_session_enabled_state()를 호출했으나, deprecated되고, 이 함수로 대체
		iControl.LocalLBObjectStatus[] objectStatus = interfaces.getLocalLBNodeAddress().get_object_status(node_list);
		int i;
		int[] state_list = new int[nodeNum];

		System.out.println("node,monitor,session,object-avail,object-enabled");
		for (i = 0; i < nodeNum; i++) {
			System.out.println(String.format("%s,%s,%s,%s,%s", node_list[i], monitorStatus[i], sessionStatus[i],
					objectStatus[i].getAvailability_status().getValue(),
					objectStatus[i].getEnabled_status().getValue()));
		}
		return state_list;
	}
//	public static void main(String[] args)
//	{
//		try
//		{
//			//node add test
//			//iControl.Interfaces inter = new iControl.Interfaces();
//			//inter.initialize("192.168.200.11", "admin", "admin");
//			
//			//add
//			//NodeAddress.create(inter, new String []{"192.168.199.200"});
//			
//			//delete
//			//NodeAddress.delete(inter, new String [] {"192.168.199.200"});
//			
//			//view			
//			//testSeeAllNode();
//			
//			getAllTest();
//			
//			//state set test
//			//setNodeStateTest();
//			
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
}