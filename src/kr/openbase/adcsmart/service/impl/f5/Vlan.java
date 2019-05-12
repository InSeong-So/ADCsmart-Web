package kr.openbase.adcsmart.service.impl.f5;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcVlan;

class Vlan {
//	private iControl.Interfaces _interfaces = null;
//	private iControl.NetworkingVLANBindingStub _NetworkingVlan = null;
//	private String _vlan_name = null;

	// constructor
	Vlan(iControl.Interfaces interfaces, String vlan_name) throws Exception {
//		_interfaces = interfaces;
//		_NetworkingVlan = _interfaces.getNetworkingVLAN();
//		_vlan_name = vlan_name;
	}

//	private void validateBaseValues() throws Exception {
//		if (_interfaces == null) {
//			throw new Exception("Vlan: F5 Interfaces error(null)");
//		} else if (_vlan_name == null) {
//			throw new Exception("VlanName: vlan name error(null)");
//		} else if (_NetworkingVlan == null) {
//			throw new Exception("Vlan: F5 API Stub error(null)");
//		}
//	}

	private static void validateBaseValues(iControl.Interfaces interfaces) throws Exception {
		if (interfaces == null) {
			throw new Exception("Vlan: F5 API Stub error(null)");
		}
	}

	// VlanList 정보 가져오기
	private static String[] getVlanInfo(iControl.Interfaces interfaces) throws Exception {
		validateBaseValues(interfaces);

		String[] vlan = interfaces.getNetworkingVLAN().get_list();
		return vlan;
	}

	static ArrayList<OBDtoAdcVlan> getAll(iControl.Interfaces interfaces) throws Exception {
		validateBaseValues(interfaces);
		// vlan
		// Vlan and tunnel
		String[] vlanName = Vlan.getVlanInfo(interfaces);

		ArrayList<OBDtoAdcVlan> vlanInfo = new ArrayList<OBDtoAdcVlan>();
		for (int i = 0; i < vlanName.length; i++) {
			OBDtoAdcVlan vlan = new OBDtoAdcVlan();
			vlan.setVlanName(vlanName[i]);
			vlanInfo.add(vlan);
		}
		return vlanInfo;
	}

//	public static void main(String[] args)
//	{
//		testVlan();
////		testShowPoolMemberRatio();
//	}

	public static void testVlan() {
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
