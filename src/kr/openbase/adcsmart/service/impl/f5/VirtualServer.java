package kr.openbase.adcsmart.service.impl.f5;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.util.ArrayList;

import iControl.CommonVLANFilterList;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

class VirtualServerRecord {
	private String _name = null;
	private String _address = null;
	private long _port = 0;
	private String _default_pool = null;
	private ArrayList<iControl.LocalLBVirtualServerVirtualServerPersistence> _persists = null;
	private ArrayList<iControl.LocalLBVirtualServerVirtualServerProfileAttribute> _profiles = null;
	private String _status_descr = null;
	private iControl.LocalLBEnabledStatus _status_enabled = null;
	private iControl.LocalLBAvailabilityStatus _status_avail = null;
	private String _wildmask = null;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

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

	public String getDefaultPool() {
		return _default_pool;
	}

	public void setDefaultPool(String default_pool) {
		_default_pool = default_pool;
	}

	public ArrayList<iControl.LocalLBVirtualServerVirtualServerPersistence> getPersistenceProfile() {
		return _persists;
	}

	public void setPersistenceProfile(ArrayList<iControl.LocalLBVirtualServerVirtualServerPersistence> persists) {
		_persists = persists;
	}

	public ArrayList<iControl.LocalLBVirtualServerVirtualServerProfileAttribute> getProfile() {
		return _profiles;
	}

	public void setProfile(ArrayList<iControl.LocalLBVirtualServerVirtualServerProfileAttribute> profiles) {
		_profiles = profiles;
	}

	public String getStatusDescription() {
		return _status_descr;
	}

	public void setStatusDescription(String status_descr) {
		_status_descr = status_descr;
	}

	public iControl.LocalLBEnabledStatus getStatusEnabled() {
		return _status_enabled;
	}

	public void setStatusEnabled(iControl.LocalLBEnabledStatus status_enabled) {
		_status_enabled = status_enabled;
	}

	public iControl.LocalLBAvailabilityStatus getStatusAvailibility() {
		return _status_avail;
	}

	public void setStatusAvailability(iControl.LocalLBAvailabilityStatus status_avail) {
		_status_avail = status_avail;
	}

	public String getWildmask() {
		return _wildmask;
	}

	public void setWildmask(String wildmask) {
		_wildmask = wildmask;
	}
}

class VServerStatus {
	private String vsName;

	private iControl.LocalLBAvailabilityStatus status;
	private Integer iStatus;

	private iControl.LocalLBEnabledStatus state;
	private Integer iState;

	public String getVsName() {
		return vsName;
	}

	public void setVsName(String vsName) {
		this.vsName = vsName;
	}

	public iControl.LocalLBAvailabilityStatus getStatus() {
		return status;
	}

	public void setStatus(iControl.LocalLBAvailabilityStatus status) {
		this.status = status;
	}

	public Integer getiStatus() {
		return iStatus;
	}

	public void setiStatus(Integer iStatus) {
		this.iStatus = iStatus;
	}

	public iControl.LocalLBEnabledStatus getState() {
		return state;
	}

	public void setState(iControl.LocalLBEnabledStatus state) {
		this.state = state;
	}

	public Integer getiState() {
		return iState;
	}

	public void setiState(Integer iState) {
		this.iState = iState;
	}
}

public class VirtualServer {
	private iControl.Interfaces _interfaces = null;
	private iControl.LocalLBVirtualServerPortType _LocalLBVirtualServer = null;
	private String _name = null;

	// constructor
	public VirtualServer(iControl.Interfaces interfaces, String name) throws Exception {
		_interfaces = interfaces;
		_name = name;
		_LocalLBVirtualServer = _interfaces.getLocalLBVirtualServer();
	}

	// member - interfaces
	public iControl.Interfaces getInterfaces() {
		return _interfaces;
	}

	// member - name
	public void setInterfaces(iControl.Interfaces interfaces) {
		_interfaces = interfaces;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public iControl.LocalLBVirtualServerPortType getVirtualServerStub() {
		return _LocalLBVirtualServer;
	}

	// member - VirtualServer stub
	public void setVirtualServerStub(iControl.Interfaces interfaces) throws Exception {
		_LocalLBVirtualServer = _interfaces.getLocalLBVirtualServer();
	}

	// base value validate
	private void validateBaseValues() throws Exception {
		if (_interfaces == null) {
			throw new Exception("VirtualServer: F5 Interfaces error(null)");
		} else if (_name == null) {
			throw new Exception("VirtualServer: virtual server name error(null)");
		} else if (_LocalLBVirtualServer == null) {
			throw new Exception("VirtualServer: F5 API Stub error(null)");
		}
	}

	private static void validateBaseValues(iControl.Interfaces interfaces) throws Exception {
		if (interfaces == null) {
			throw new Exception("VirtualServer: F5 interface error(null)");
		}
	}

	private static String[] getList(iControl.Interfaces interfaces) throws Exception {
		String[] vs_list = null;

		validateBaseValues(interfaces);

		vs_list = interfaces.getLocalLBVirtualServer().get_list();
		return vs_list;
	}

	void create(String virtual_server_address, long port, String default_pool_name) throws Exception {
		validateBaseValues();

		iControl.CommonVirtualServerDefinition[] definitions = new iControl.CommonVirtualServerDefinition[1];
		definitions[0] = new iControl.CommonVirtualServerDefinition();
		definitions[0].setName(_name);
		definitions[0].setAddress(virtual_server_address);
		definitions[0].setPort(port);
		definitions[0].setProtocol(iControl.CommonProtocolType.PROTOCOL_TCP); // default fixed = TCP

		final String[] masks = { "255.255.255.255" };

		iControl.LocalLBVirtualServerVirtualServerResource[] resources = new iControl.LocalLBVirtualServerVirtualServerResource[1];
		resources[0] = new iControl.LocalLBVirtualServerVirtualServerResource();
		resources[0].setDefault_pool_name(default_pool_name);
		// resources[0].setType(LocalLBVirtualServerVirtualServerType.RESOURCE_TYPE_POOL);
		// // Standard, pool 기반 virtual server
		resources[0].setType(CommonF5.DEFAULT_VIRTUALSERVER_TYPE);

		iControl.LocalLBVirtualServerVirtualServerProfile[][] profiles = new iControl.LocalLBVirtualServerVirtualServerProfile[1][1];
		profiles[0][0] = new iControl.LocalLBVirtualServerVirtualServerProfile();
		profiles[0][0].setProfile_context(iControl.LocalLBProfileContextType.PROFILE_CONTEXT_TYPE_ALL);

		// profile 기본값을 http로 하면 virtual server type이 fastL4가 아니라 standard로 바뀐다.
		// profiles[0][0].setProfile_name("http");
		profiles[0][0].setProfile_name("fastL4");

		create(definitions, masks, resources, profiles);
	}

	private void create(iControl.CommonVirtualServerDefinition[] definitions, String[] masks,
			iControl.LocalLBVirtualServerVirtualServerResource[] resources,
			iControl.LocalLBVirtualServerVirtualServerProfile[][] profiles) throws Exception {
		validateBaseValues();

		_LocalLBVirtualServer.create(definitions, masks, resources, profiles);
	}

	void delete() throws Exception {
		validateBaseValues();

		String[] virtual_servers = { _name };

		_LocalLBVirtualServer.delete_virtual_server(virtual_servers);
	}

	static void delete(iControl.Interfaces interfaces, String[] virtual_servers) throws Exception {
		// String [] virtual_servers = { virtual_server };
		validateBaseValues(interfaces);

		if (virtual_servers == null) {
			throw new Exception("VirtualServer: virtual server name error(null)");
		}

		interfaces.getLocalLBVirtualServer().delete_virtual_server(virtual_servers);
	}

	// V.Server의 IP와 Port 정보 구함 - 현재 virtual server
	public iControl.CommonIPPortDefinition getVirtualPPort() throws Exception {
		validateBaseValues();

		String[] virtual_servers = { _name };
		iControl.CommonIPPortDefinition[] vs_ipport = _LocalLBVirtualServer.get_destination(virtual_servers);
		return vs_ipport[0];
	}

	private static iControl.CommonIPPortDefinition[] getVirtualIPPort(iControl.Interfaces interfaces,
			String[] virtual_servers) throws Exception {
		validateBaseValues(interfaces);

		iControl.CommonIPPortDefinition[] vs_ipport = interfaces.getLocalLBVirtualServer()
				.get_destination(virtual_servers);
		return vs_ipport;
	}

	public String getDefaultPoolName() throws Exception {
		String[] virtual_servers = { _name };
		String[] default_pool_name = _LocalLBVirtualServer.get_default_pool_name(virtual_servers);
		return default_pool_name[0];
	}

	private static String[] getDefaultPoolName(iControl.Interfaces interfaces, String[] virtual_servers)
			throws Exception {
		validateBaseValues(interfaces);

		String[] default_pool_name = interfaces.getLocalLBVirtualServer().get_default_pool_name(virtual_servers);
		return default_pool_name;
	}

	public void setDefaultPoolName(String pool_name) throws Exception {
		String[] virtual_servers = { _name };
		String[] pool_names = { pool_name };
		_LocalLBVirtualServer.set_default_pool_name(virtual_servers, pool_names);
	}

	public iControl.LocalLBObjectStatus getStatus() throws Exception {
		validateBaseValues();

		String[] virtual_servers = { _name };
		iControl.LocalLBObjectStatus[] obj_status = _LocalLBVirtualServer.get_object_status(virtual_servers);
		return obj_status[0];
	}

	public String getStatusDescription() throws Exception {
		validateBaseValues();

		String status_descr = getStatus().getStatus_description();
		return status_descr;
	}

	public iControl.LocalLBAvailabilityStatus getAvailabilityStatus() throws Exception {
		validateBaseValues();

		return getStatus().getAvailability_status();
	}

	/*
	 * //virtual server Index와 state 리스트 : 쓰고 있는 곳이 없지만, 필요할 가능성 높음. 지우지 말기. 아래 함수와
	 * 유사기능 public static ArrayList<OBDtoVServerStatus>
	 * getVirtualServerStatusList(Interfaces interfaces, ArrayList<String>
	 * vsIndexList, ArrayList<String> vsNameList) throws Exception {
	 * ArrayList<OBDtoVServerStatus> vstatus_list = new
	 * ArrayList<OBDtoVServerStatus>();
	 * 
	 * int i;
	 * 
	 * validateBaseValues(interfaces);
	 * 
	 * String [] vs_names = vsNameList.toArray(new String [vsNameList.size()]);
	 * LocalLBObjectStatus [] obj_status =
	 * interfaces.getLocalLBVirtualServer().get_object_status(vs_names);
	 * 
	 * for(i=0; i<vsIndexList.size(); i++) { OBDtoVServerStatus vs = new
	 * OBDtoVServerStatus(); vs.setVsIndex(vsIndexList.get(i));
	 * 
	 * vs.setStatus(CommonF5.getObjectStatus(obj_status[i].getEnabled_status(),
	 * obj_status[i].getAvailability_status()));
	 * 
	 * vstatus_list.add(vs); } return vstatus_list; }
	 */
	// virtual server status 리스트구함
	private static ArrayList<VServerStatus> getVirtualServerStatusTotalList(iControl.Interfaces interfaces,
			String[] vs_names) throws Exception {
		ArrayList<VServerStatus> vstatus_list = new ArrayList<VServerStatus>();

		int i;

		validateBaseValues(interfaces);

		iControl.LocalLBObjectStatus[] obj_status = interfaces.getLocalLBVirtualServer().get_object_status(vs_names);

		for (i = 0; i < vs_names.length; i++) {
			VServerStatus status = new VServerStatus();
			status.setVsName(vs_names[i]);

			status.setStatus(obj_status[i].getAvailability_status());
			status.setState(obj_status[i].getEnabled_status());
			status.setiStatus(CommonF5.getObjectStatus(obj_status[i].getEnabled_status(),
					obj_status[i].getAvailability_status()));
			status.setiState(CommonF5.valueLocalLBEnabledStatus2int(obj_status[i].getEnabled_status()));

			vstatus_list.add(status);
		}
		return vstatus_list;
	}

	public iControl.LocalLBEnabledStatus getEnabledStatus() throws Exception {
		validateBaseValues();

		return getStatus().getEnabled_status();
	}

	public void setStatusEnableDisable(int status) throws Exception {
		validateBaseValues();

		iControl.CommonEnabledState enstatus = iControl.CommonEnabledState.STATE_ENABLED;
		if (status == 1) {
			enstatus = iControl.CommonEnabledState.STATE_ENABLED;
		} else if (status == 0) {
			enstatus = iControl.CommonEnabledState.STATE_DISABLED;
		}

		String[] virtual_servers = { _name };
		iControl.CommonEnabledState[] states = { enstatus };
		_LocalLBVirtualServer.set_enabled_state(virtual_servers, states);
	}

	static void setStatusEnableDisable(iControl.Interfaces interfaces, String[] virtual_servers, int status)
			throws Exception {
		int i = 0;
		validateBaseValues(interfaces);

		iControl.CommonEnabledState enstatus = iControl.CommonEnabledState.STATE_ENABLED;
		if (status == 1) {
			enstatus = iControl.CommonEnabledState.STATE_ENABLED;
		} else if (status == 0) {
			enstatus = iControl.CommonEnabledState.STATE_DISABLED;
		}

		// String [] virtual_servers = { _name };
		iControl.CommonEnabledState[] states = new iControl.CommonEnabledState[virtual_servers.length];
		for (i = 0; i < virtual_servers.length; i++) {
			states[i] = enstatus;
		}

		interfaces.getLocalLBVirtualServer().set_enabled_state(virtual_servers, states);
	}

	void setVirtualPPort(String address, long port) throws Exception {
		validateBaseValues();

		String[] virtual_servers = { _name };

		iControl.CommonIPPortDefinition[] vs_ipports = new iControl.CommonIPPortDefinition[1];
		vs_ipports[0] = new iControl.CommonIPPortDefinition();
		vs_ipports[0].setAddress(address);
		vs_ipports[0].setPort(port);

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, address + " / " + port);

		_LocalLBVirtualServer.set_destination(virtual_servers, vs_ipports);
	}

	static void setVlanFilter(iControl.Interfaces interfaces, String virtual_server, int status, String[] vlans)
			throws Exception {
		validateBaseValues(interfaces);
		String[] vs = { virtual_server };
		iControl.CommonEnabledState enstatus = iControl.CommonEnabledState.STATE_ENABLED;

		if (status == 1) {
			enstatus = iControl.CommonEnabledState.STATE_ENABLED;
		} else {
			enstatus = iControl.CommonEnabledState.STATE_DISABLED;
		}

		CommonVLANFilterList filter = new CommonVLANFilterList(enstatus, vlans);
		CommonVLANFilterList[] vlanFilters = { filter };

		interfaces.getLocalLBVirtualServer().set_vlan(vs, vlanFilters);
	}

	public iControl.LocalLBVirtualServerVirtualServerPersistence[] getPersistenceProfile() throws Exception {
		validateBaseValues();

		String[] virtual_servers = { _name };
		iControl.LocalLBVirtualServerVirtualServerPersistence[][] persists = _LocalLBVirtualServer
				.get_persistence_profile(virtual_servers);
		return persists[0];
	}

	private static iControl.LocalLBVirtualServerVirtualServerPersistence[][] getPersistenceProfile(
			iControl.Interfaces interfaces, String[] virtual_servers) throws Exception {
		validateBaseValues(interfaces);

		iControl.LocalLBVirtualServerVirtualServerPersistence[][] persistenceProfiles = interfaces
				.getLocalLBVirtualServer().get_persistence_profile(virtual_servers);
		return persistenceProfiles;
	}

	void removeAllPersistenceProfile() throws Exception {
		validateBaseValues();

		String[] virtual_servers = { _name };
		_LocalLBVirtualServer.remove_all_persistence_profiles(virtual_servers); // 할당된 persistence profile clear
	}

	public void setDefaultPersistenceProfile(String persist_name) throws Exception {
		validateBaseValues();

		String[] virtual_servers = { _name };

		iControl.LocalLBVirtualServerVirtualServerPersistence persist = new iControl.LocalLBVirtualServerVirtualServerPersistence();
		persist.setProfile_name(persist_name);
		persist.setDefault_profile(true);
		iControl.LocalLBVirtualServerVirtualServerPersistence[] persists = { persist };
		iControl.LocalLBVirtualServerVirtualServerPersistence[][] persistss = { persists };

		_LocalLBVirtualServer.add_persistence_profile(virtual_servers, persistss);
	}

	public void setVlanFilter(String virtual_server, int status, String[] vlans) throws Exception {
		validateBaseValues();

		String[] vs = { virtual_server };
		String[] allVlan = {};
		iControl.CommonEnabledState enstatus = iControl.CommonEnabledState.STATE_ENABLED;

		if (status == 1) {
			enstatus = iControl.CommonEnabledState.STATE_ENABLED;
		} else if (status == 0) {
			enstatus = iControl.CommonEnabledState.STATE_DISABLED;
			vlans = allVlan;
		} else {
			enstatus = iControl.CommonEnabledState.STATE_DISABLED;
		}

		CommonVLANFilterList filter = new CommonVLANFilterList(enstatus, vlans);
		CommonVLANFilterList[] vlanFilters = { filter };

		_LocalLBVirtualServer.set_vlan(vs, vlanFilters);
	}

	// profile 처리
	// ---------------------------------------------------------------------
	// profile 상세 조회
	public iControl.LocalLBVirtualServerVirtualServerProfileAttribute[] getProfile() throws Exception {
		validateBaseValues();

		String[] virtual_servers = { _name };
		iControl.LocalLBVirtualServerVirtualServerProfileAttribute[][] profile_attr = _LocalLBVirtualServer
				.get_profile(virtual_servers);
		return profile_attr[0];
	}

	public String getWildMask() throws Exception {
		validateBaseValues();

		String[] virtual_servers = { _name };
		String[] mask = _LocalLBVirtualServer.get_wildmask(virtual_servers);
		return mask[0];
	}

	public void setWildMask(String mask) throws Exception {
		validateBaseValues();

		String[] virtual_servers = { _name };
		String[] masks = { mask };
		_LocalLBVirtualServer.set_wildmask(virtual_servers, masks);
	}

//	public iControl.CommonVLANFilterList [] getVlanAndTunnel() throws Exception
//	{
//		validateBaseValues();
//		
//		String [] virtual_servers = { _name };
//		CommonVLANFilterList[] vlan = _LocalLBVirtualServer.get_vlan(virtual_servers);
//		return vlan;
//	}

	public static iControl.CommonVLANFilterList[] getVlanAndTunnel(iControl.Interfaces interfaces,
			String[] virtual_servers) throws Exception {
		validateBaseValues(interfaces);

		iControl.CommonVLANFilterList[] vlan = interfaces.getLocalLBVirtualServer().get_vlan(virtual_servers);
		return vlan;
	}

	public static ArrayList<DtoVirtualServer> getAll(iControl.Interfaces interfaces) throws Exception {
		validateBaseValues(interfaces);
		String[] virtual_servers = VirtualServer.getList(interfaces); // 모든 vs 목록을 가져옴
		ArrayList<DtoVirtualServer> virtualServerSet = get(interfaces, virtual_servers);
		return virtualServerSet;
	}

	public static ArrayList<DtoVirtualServer> get(iControl.Interfaces interfaces, String[] virtual_servers)
			throws Exception {
		// 반환값 저장
		ArrayList<DtoVirtualServer> vsDtoSet = new ArrayList<DtoVirtualServer>();

		int i = 0;
		int num = 0;
		long tt1, tt2, tt3, tt4, tt5, tt6, tt7, tt8;
		String tempString;

		validateBaseValues(interfaces);

		tt1 = System.currentTimeMillis();

		num = virtual_servers.length;
		if (num == 0) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, "Virtual server list to read is empty.");
			return vsDtoSet;
		}
		tt2 = System.currentTimeMillis();

		Timestamp tm = ManagementDBVariable.getLocalConfigTime(interfaces);
		tt3 = System.currentTimeMillis();

		// ip & port
		iControl.CommonIPPortDefinition[] ipportTemp = VirtualServer.getVirtualIPPort(interfaces, virtual_servers);
		tt4 = System.currentTimeMillis();

		// persistence profile
		iControl.LocalLBVirtualServerVirtualServerPersistence[][] persistenceProfiles = VirtualServer
				.getPersistenceProfile(interfaces, virtual_servers);
		tt5 = System.currentTimeMillis();

		// default pool
		String[] default_pools = getDefaultPoolName(interfaces, virtual_servers);
		tt6 = System.currentTimeMillis();

		// Vlan Filter
		iControl.CommonVLANFilterList[] vlanFilter = VirtualServer.getVlanAndTunnel(interfaces, virtual_servers);
		tt4 = System.currentTimeMillis();

		// status - enable(use_yn), availability
		// LocalLBObjectStatus [] object_status = VirtualServer.getStatus(interfaces,
		// virtual_servers);
		ArrayList<VServerStatus> statusList = new ArrayList<VServerStatus>();
		statusList = getVirtualServerStatusTotalList(interfaces, virtual_servers);
		tt7 = System.currentTimeMillis();

		for (i = 0; i < num; i++) {
			// virtualserver별 DTO
			DtoVirtualServer vsDto = new DtoVirtualServer();

			vsDto.setvIP(ipportTemp[i].getAddress());
			vsDto.setServicePort((int) ipportTemp[i].getPort());

			// vsDto.setLbMethod(lbMethod);
			vsDto.setName(virtual_servers[i]);

			// persistenceProfiles는 API 구조상 배열로 리턴되지만 인자로 준 Virtualserver가 1개이므로 0 아니면 1이다.
			// 그러므로 persistenceProfiles가 있으면 하나만 뺀다.
			vsDto.setPersistenceName(null);
			if (persistenceProfiles[i] != null && persistenceProfiles[i].length != 0) {
				tempString = persistenceProfiles[i][0].getProfile_name();
				if (tempString != null && tempString.isEmpty() == false) {
					vsDto.setPersistenceName(tempString);
				}
			}

			// default pool
			vsDto.setPoolName(default_pools[i]);

			// status
			vsDto.setStatus(statusList.get(i).getiStatus());
			vsDto.setUseYN(statusList.get(i).getiState());

			// vlan filter
			DtoVlanTunnelFilter vlanTunnel = new DtoVlanTunnelFilter();

			if (vlanFilter[i].getState().toString() == OBDefine.VLAN_ENABLED)
				vlanTunnel.setStatus(OBDefine.VLAN_ENABLE);
			else
				vlanTunnel.setStatus(OBDefine.VLAN_DISABLE);

			String[] vlanList = vlanFilter[i].getVlans();

			if (vlanFilter[i].getState().toString() == OBDefine.VLAN_DISABLED && vlanList.length == 0) {
				vlanTunnel.setStatus(OBDefine.VLAN_ALL);
				ArrayList<String> vlanName = new ArrayList<String>(); // 없어도 빈 vlan으로 리턴한다.
				vlanTunnel.setVlanName(vlanName);
			} else {
				ArrayList<String> vlanName = new ArrayList<String>();
				for (int j = 0; j < vlanList.length; j++) {
					vlanName.add(vlanList[j]);
				}
				vlanTunnel.setVlanName(vlanName);
			}

			vsDto.setVlanTunnel(vlanTunnel);
			// 공통값
			vsDto.setApplyTime(tm);
			vsDtoSet.add(vsDto);
		}

		tt8 = System.currentTimeMillis();

		OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
				"<time> VirtualServer.getAll() --- getList(): " + (tt2 - tt1) / 1000.0);
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
				"<time> VirtualServer.getAll() --- getLocalConfigTime(): " + (tt3 - tt2) / 1000.0);
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
				"<time> VirtualServer.getAll() --- getVirtualIPPort(): " + (tt4 - tt3) / 1000.0);
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
				"<time> VirtualServer.getAll() --- getPersistenceProfile(): " + (tt5 - tt4) / 1000.0);
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
				"<time> VirtualServer.getAll() --- getDefaultPoolName(): " + (tt6 - tt5) / 1000.0);
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
				"<time> VirtualServer.getAll() --- getStatus(): " + (tt7 - tt6) / 1000.0);
		OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
				"<time> VirtualServer.getAll() --- make data: " + (tt8 - tt7) / 1000.0);

		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "virtual server count: " + vsDtoSet.size());
		return vsDtoSet;
	}

	// test functions
	// --------------------------------------------------------------------------------------
	/***********
	 * W A R N I N G !!!!! *********** adcsmart가 F5 BIGIP를 제어하는데 필요한 기능을 손쉽게 테스트하고,
	 * 주요 설정 값을 즉시 확인하도록 갖춰 놓은 테스트 함수 및 main()입니다. 가급적 바꾸지 말아 주세요. 특히 제거하면 안 됩니다.
	 * 필요한 경우 comment로 코드 실행을 건너뛰거나 테스트 함수 추가 권장
	 *********************************************/
//	public static void main(String[] args)
//	{
//		iControl.Interfaces inter = new iControl.Interfaces();
//		
//		inter.initialize("192.168.200.12", "admin", "openbase");
//		try
//		{
//			ArrayList<DtoVirtualServer> aa = getAll(inter);
//			for(int i = 0; i < aa.size(); i++)
//			{
//				System.out.println(aa.size());
//				System.out.println(aa.get(i));
//			}
//			//virtual server와 pool 만들기, 확인, 삭제 테스트 : pool은 member 없이 만든다.
//			// (1) 만들기
////			Pool.testCreatePoolWithNoMember(inter, "aaapool");
////			VirtualServer.testCreateVirtualServer(inter, "aaavs", "10.10.10.55", 80L, "aaapool");
//			
//			// (2) 확인
////			iControl.CommonIPPortDefinition [] ipportTemp = VirtualServer.getVirtualIPPort(inter, new String[]{"aaavs"});
////			String [] vsList = VirtualServer.getList(inter);
////			for(int i=0; i<vsList.length; i++)
////			{
////				System.out.println(String.format(" vs[%d]: %s", i, vsList[i]));
////			}
////			System.out.println("ipport = " + ipportTemp[0].getAddress() + "/" + ipportTemp[0].getPort());
////			System.out.println("pool = " + VirtualServer.getDefaultPoolName(inter, new String[]{"aaavs"})[0]);
////			
//			// (3) 삭제, 그래야 또 만들지..
////			VirtualServer.testDeleteVirtualServer(inter, "aaavs");
////			Pool.delete(inter, "aaapool");
//			//테스트 끝
//
//			//ADC의 SLB 정보를 요약 하기: slb 설정을 요약해서  aaa.txt 파일에 기록한다.
//			//VirtualServer.testSeeAllVServers(inter);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	public static void testCreateVirtualServer(iControl.Interfaces interfaces, String vs_name, String vs_address,
			long vs_port, String def_pool) {
		try {
			validateBaseValues(interfaces);

			if (vs_name == null) {
				throw new Exception("VirtualServer: virtual server name error(null)");
			}
			VirtualServer vs_new = new VirtualServer(interfaces, vs_name);
			long startA = System.currentTimeMillis();
			vs_new.create(vs_address, vs_port, def_pool);
			vs_new.setDefaultPoolName(def_pool);

			long endA = System.currentTimeMillis();
			System.out.println("<time>VirtualServer Create : " + (endA - startA) / 1000.0);

			long startB = System.currentTimeMillis();
			SystemF5.saveHighConfig(interfaces);
			long endB = System.currentTimeMillis();
			System.out.println("<time>Save Config Normal: " + (endB - startB) / 1000.0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void testDeleteVirtualServer(iControl.Interfaces interfaces, String vs_name) {
		try {
			validateBaseValues(interfaces);

			if (vs_name == null) {
				throw new Exception("VirtualServer: virtual server name error(null)");
			}

			String[] vs_names = { vs_name };
			long startA = System.currentTimeMillis();
			VirtualServer.delete(interfaces, vs_names);
			long endA = System.currentTimeMillis();
			System.out.println("<time>VirtualServer Delete : " + (endA - startA) / 1000.0);

			long startB = System.currentTimeMillis();
			SystemF5.saveHighConfig(interfaces);
			long endB = System.currentTimeMillis();
			System.out.println("<time>Save Config Normal: " + (endB - startB) / 1000.0);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void testSeeAllVServers(iControl.Interfaces interfaces) {
		try {
			validateBaseValues(interfaces);

			String[] virtual_servers = VirtualServer.getList(interfaces);
			ArrayList<VirtualServerRecord> vsr = testSeeVirtualServer(interfaces, virtual_servers);
			testPrintVirtualServer(vsr, "aaa.txt", false);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static ArrayList<VirtualServerRecord> testSeeVirtualServer(iControl.Interfaces interfaces,
			String[] virtual_servers) {
		int i;
		ArrayList<VirtualServerRecord> res = new ArrayList<VirtualServerRecord>();
		try {
			validateBaseValues(interfaces);

			if (virtual_servers == null) {
				throw new Exception("VirtualServer: virtual server name error(null)");
			}

			for (String onevserver : virtual_servers) {
				VirtualServerRecord resTemp = new VirtualServerRecord();
				VirtualServer vsTemp = new VirtualServer(interfaces, onevserver);
				resTemp.setName(vsTemp.getName());
				iControl.CommonIPPortDefinition ipportTemp = vsTemp.getVirtualPPort();
				resTemp.setAddress(ipportTemp.getAddress());
				resTemp.setPort(ipportTemp.getPort());
				resTemp.setDefaultPool(vsTemp.getDefaultPoolName());

				iControl.LocalLBVirtualServerVirtualServerPersistence[] persistsTemp = vsTemp.getPersistenceProfile();
				ArrayList<iControl.LocalLBVirtualServerVirtualServerPersistence> persistsTemp2 = new ArrayList<iControl.LocalLBVirtualServerVirtualServerPersistence>();
				for (i = 0; i < persistsTemp.length; i++) {
					persistsTemp2.add(persistsTemp[i]);
				}
				resTemp.setPersistenceProfile(persistsTemp2);

				iControl.LocalLBVirtualServerVirtualServerProfileAttribute[] profileTemp = vsTemp.getProfile();
				ArrayList<iControl.LocalLBVirtualServerVirtualServerProfileAttribute> profileTemp2 = new ArrayList<iControl.LocalLBVirtualServerVirtualServerProfileAttribute>();
				for (i = 0; i < profileTemp.length; i++) {
					profileTemp2.add(profileTemp[i]);
				}
				resTemp.setProfile(profileTemp2);

				resTemp.setStatusAvailability(vsTemp.getStatus().getAvailability_status());
				resTemp.setStatusEnabled(vsTemp.getStatus().getEnabled_status());
				resTemp.setStatusDescription(vsTemp.getStatusDescription());
				resTemp.setWildmask(vsTemp.getWildMask());

				res.add(resTemp);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return res;
	}

	public static void testPrintVirtualServer(ArrayList<VirtualServerRecord> vsr, String report_file, boolean bExtend) {
		BufferedWriter report;
		try {
			report = new BufferedWriter(new FileWriter(report_file, bExtend));

			report.write("\n====================================\n");
			report.write(" V I R T U A L S E R V E R S\n");
			for (VirtualServerRecord iterV : vsr) {

				report.write("------------------------------------\n");
				report.write("virtual server: " + iterV.getName() + "\n");

				report.write("\t address: " + iterV.getAddress() + "(mask " + iterV.getWildmask() + ")\n");
				report.write("\t port: " + iterV.getPort() + "\n");
				report.write("\t default pool: " + iterV.getDefaultPool() + "\n");

				int i = 0;
				if (iterV.getPersistenceProfile().size() > 0) {
					report.write("\t persistence profile: " + "\n");
					for (iControl.LocalLBVirtualServerVirtualServerPersistence iterP : iterV.getPersistenceProfile()) {
						report.write("\t\t " + i++ + ") " + iterP.getProfile_name() + " / default: "
								+ iterP.isDefault_profile() + "\n");
					}
				} else {
					report.write("\t persistence profile: NONE" + "\n");
				}

				i = 1;
				if (iterV.getProfile().size() > 0) {
					report.write("\t profile: " + "\n");
					for (iControl.LocalLBVirtualServerVirtualServerProfileAttribute iterP : iterV.getProfile()) {
						report.write("\t\t " + i++ + ")" + iterP.getProfile_name() + "\n");
						report.write("\t\t\t type: " + iterP.getProfile_type().toString() + "\n");
						report.write("\t\t\t context type: " + iterP.getProfile_context().toString() + "\n");
					}
				} else {
					report.write("\t profile: NONE" + "\n");
				}

				report.write("\t status:\n");
				report.write("\t\t " + iterV.getStatusAvailibility().getValue() + "\n");
				report.write("\t\t " + iterV.getStatusEnabled() + "\n");
				report.write("\t\t " + iterV.getStatusDescription() + "\n");
			}
			report.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
