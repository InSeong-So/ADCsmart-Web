package kr.openbase.adcsmart.service.impl.f5;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcProfile;
import kr.openbase.adcsmart.service.utility.OBUtility;

class PersistenceProfileRecord {
	private String _name;
	private boolean _is_basic = false;
	private String _parent_profile = null;
	private String _type = null;
	private boolean _type_inherited = false;
	private String _match_across = null;
	private boolean _match_across_inherited = false;
	private long _timeout = 0;
	private boolean _timeout_inherited = false;

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public boolean getIsBasicProfile() {
		return _is_basic;
	}

	public void setIsBasicProfile(boolean is_basic) {
		_is_basic = is_basic;
	}

	public String getParentProfile() {
		return _parent_profile;
	}

	public void setParentProfile(String parent_profile) {
		_parent_profile = parent_profile;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		_type = type;
	}

	public boolean getTypeInherited() {
		return _type_inherited;
	}

	public void setTypeInherited(boolean type_inherited) {
		_type_inherited = type_inherited;
	}

	public String getMatchAcross() {
		return _match_across;
	}

	public void setMatchAcross(String match_across) {
		_match_across = match_across;
	}

	public boolean getMatchAcrossInherited() {
		return _match_across_inherited;
	}

	public void setMatchAcrossInherited(boolean match_across_inherited) {
		_match_across_inherited = match_across_inherited;
	}

	public long getTimeout() {
		return _timeout;
	}

	public void setTimeout(long timeout) {
		_timeout = timeout;
	}

	public boolean getTimeoutInherited() {
		return _timeout_inherited;
	}

	public void setTimeoutInherited(boolean timeout_inherited) {
		_timeout_inherited = timeout_inherited;
	}
}

class ProfilePersistence {

	private iControl.Interfaces _interfaces = null;
	private iControl.LocalLBProfilePersistencePortType _LocalLBProfilePersistence = null;
	private String _name = null;

	// constructor
	ProfilePersistence(iControl.Interfaces interfaces, String name) throws Exception {
		_interfaces = interfaces;
		_name = name;
		_LocalLBProfilePersistence = _interfaces.getLocalLBProfilePersistence();
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

	// member - Persistence profile stub
	public iControl.LocalLBProfilePersistencePortType getProfilePersistenceStub() {
		return _LocalLBProfilePersistence;
	}

	public void setProfilePersistenceStub(iControl.Interfaces interfaces) throws Exception {
		_LocalLBProfilePersistence = _interfaces.getLocalLBProfilePersistence();
	}

	private void validateBaseValues() throws Exception {
		if (_interfaces == null) {
			throw new Exception("Persistence profile: F5 Interfaces error(null)");

		} else if (_name == null) {
			throw new Exception("Persistence profile: profile name error(null)");
		} else if (_LocalLBProfilePersistence == null) {
			throw new Exception("Persistence profile: F5 API Stub error(null)");
		}
	}

	private static void validateBaseValues(iControl.Interfaces interfaces) throws Exception {
		if (interfaces == null) {
			throw new Exception("Persistence profile: F5 interface error(null)");
		}
	}

	private static String[] getList(iControl.Interfaces interfaces) throws Exception {
		validateBaseValues(interfaces);

		String[] profile_list = null;
		profile_list = interfaces.getLocalLBProfilePersistence().get_list();
		return profile_list;
	}

	public boolean isBasic() throws Exception {
		String[] profile_names = { _name };
		boolean[] ret = null;

		validateBaseValues();
		ret = _LocalLBProfilePersistence.is_base_profile(profile_names);

		return ret[0];

	}

	// persistence profile(mode) 1개를 등록
	void create(iControl.LocalLBPersistenceMode persis_mode) throws Exception {
		validateBaseValues();

		String[] profile_names = { _name };
		iControl.LocalLBPersistenceMode[] persis_modes = { persis_mode };

		_LocalLBProfilePersistence.create(profile_names, persis_modes);
	}

	static void delete(iControl.Interfaces interfaces, String[] profile_names) throws Exception {
		validateBaseValues(interfaces);

		if (profile_names == null) {
			throw new Exception("Persistence profile: profile name error(null)");
		}
		interfaces.getLocalLBProfilePersistence().delete_profile(profile_names);
	}

	public iControl.LocalLBProfilePersistenceMode getPersistenceMode() throws Exception // Persistence Type
	{
		validateBaseValues();

		String[] profile_names = { _name };
		iControl.LocalLBProfilePersistenceMode[] profile_type = _LocalLBProfilePersistence
				.get_persistence_mode(profile_names);
		return profile_type[0];
	}

	private static iControl.LocalLBProfilePersistenceMode[] getPersistenceMode(iControl.Interfaces interfaces,
			String[] profile_names) throws Exception // Persistence Type
	{
		validateBaseValues(interfaces);

		if (profile_names == null) {
			throw new Exception("Persistence profile: profile name error(null)");
		}

		iControl.LocalLBProfilePersistenceMode[] profile_type = interfaces.getLocalLBProfilePersistence()
				.get_persistence_mode(profile_names);
		return profile_type;
	}

	public void setPersistenceMode(iControl.LocalLBProfilePersistenceMode profile_type) throws Exception // Persistence
																											// Type
	{
		validateBaseValues();

		String[] profile_names = { _name };
		iControl.LocalLBProfilePersistenceMode[] profile_types = { profile_type };
		_LocalLBProfilePersistence.set_persistence_mode(profile_names, profile_types);
	}

	void setPersistenceMode(iControl.LocalLBPersistenceMode type, boolean isParentValueForced) throws Exception // Persistence
																												// Type
	{
		validateBaseValues();

		iControl.LocalLBProfilePersistenceMode profile_type = new iControl.LocalLBProfilePersistenceMode();

		profile_type.setValue(type);
		profile_type.setDefault_flag(isParentValueForced); // true면 parent의 값을 받아서 쓰고, false면 사용자가 지정한 값을 쓴다.
		setPersistenceMode(profile_type);
	}

	// TODO: parent profile을 이것으로 뽑는 게 맞나 확인
	public String getDefaultProfile() throws Exception // Parent profile
	{
		validateBaseValues();

		String[] profile_names = { _name };
		String[] default_profiles = _LocalLBProfilePersistence.get_default_profile(profile_names);
		return default_profiles[0];
	}

	private static String[] getDefaultProfile(iControl.Interfaces interfaces, String[] profile_names) throws Exception // Parent
																														// profile
	{
		validateBaseValues(interfaces);

		if (profile_names == null) {
			throw new Exception("Persistence profile: profile name error(null)");
		}

		String[] default_profiles = interfaces.getLocalLBProfilePersistence().get_default_profile(profile_names);
		return default_profiles;
	}

	public void setDefaultProfile(String default_profile) throws Exception // Parent profile
	{
		validateBaseValues();

		String[] profile_names = { _name };
		String[] default_profiles = { default_profile };
		_LocalLBProfilePersistence.set_default_profile(profile_names, default_profiles);
	}

	public iControl.LocalLBProfileEnabledState getMatchAcrossPool() throws Exception // Match Across - Pool, 내부 조회
	{
		validateBaseValues();

		String[] profile_names = { _name };
		iControl.LocalLBProfileEnabledState[] ma_states = _LocalLBProfilePersistence
				.get_across_pool_state(profile_names);
		return ma_states[0];
	}

	public void setMatchAcrossPool(iControl.LocalLBProfileEnabledState ma_state) throws Exception // Match Across -
																									// Pool, 내부 설정
	{
		validateBaseValues();

		String[] profile_names = { _name };
		iControl.LocalLBProfileEnabledState[] ma_states = { ma_state };
		_LocalLBProfilePersistence.set_across_pool_state(profile_names, ma_states);
	}

	private static iControl.LocalLBProfileEnabledState[] getMatchAcrossService(iControl.Interfaces interfaces,
			String[] profile_names) throws Exception {
		validateBaseValues(interfaces);

		if (profile_names == null) {
			throw new Exception("Persistence profile: profile name error(null)");
		}

		iControl.LocalLBProfileEnabledState[] ma_states = interfaces.getLocalLBProfilePersistence()
				.get_across_service_state(profile_names);
		return ma_states;
	}

	public iControl.LocalLBProfileEnabledState getMatchAcrossService() throws Exception // Match Across - Service, 내부 조회
	{
		validateBaseValues();

		String[] profile_names = { _name };
		iControl.LocalLBProfileEnabledState[] ma_states = _LocalLBProfilePersistence
				.get_across_service_state(profile_names);
		return ma_states[0];
	}

	void setMatchAcrossService(boolean isEnabled, boolean isParentValueForced) throws Exception // Match Across -
																								// Service, 외부 설정
	{
		validateBaseValues();

		iControl.LocalLBProfileEnabledState ma_state = new iControl.LocalLBProfileEnabledState();
		ma_state.setDefault_flag(isParentValueForced);
		if (isEnabled == true) {
			ma_state.setValue(iControl.CommonEnabledState.STATE_ENABLED);
		} else {
			ma_state.setValue(iControl.CommonEnabledState.STATE_DISABLED);
		}
		setMatchAcrossService(ma_state);
	}

	public void setMatchAcrossService(iControl.LocalLBProfileEnabledState ma_state) throws Exception // Match Across -
																										// Service, 내부
																										// 설정
	{
		validateBaseValues();

		String[] profile_names = { _name };
		iControl.LocalLBProfileEnabledState[] ma_states = { ma_state };
		_LocalLBProfilePersistence.set_across_service_state(profile_names, ma_states);
	}

	public iControl.LocalLBProfileEnabledState getMatchAcrossVirtual() throws Exception // Match Across - Virtual
																						// Server, 내부 조회
	{
		validateBaseValues();

		String[] profile_names = { _name };
		iControl.LocalLBProfileEnabledState[] ma_states = _LocalLBProfilePersistence
				.get_across_virtual_state(profile_names);
		return ma_states[0];
	}

	public void setMatchAcrossVirtual(iControl.LocalLBProfileEnabledState ma_state) throws Exception // Match Across -
																										// Virtual
																										// Server, 내부 설정
	{
		validateBaseValues();

		String[] profile_names = { _name };
		iControl.LocalLBProfileEnabledState[] ma_states = { ma_state };
		_LocalLBProfilePersistence.set_across_virtual_state(profile_names, ma_states);
	}

	public iControl.LocalLBProfileULong getTimeout() throws Exception // timeout
	{
		validateBaseValues();

		String[] profile_names = { _name };
		iControl.LocalLBProfileULong[] timeout = _LocalLBProfilePersistence.get_timeout(profile_names);

		return timeout[0];
	}

	private static iControl.LocalLBProfileULong[] getTimeout(iControl.Interfaces interfaces, String[] profile_names)
			throws Exception // timeout
	{
		validateBaseValues(interfaces);

		iControl.LocalLBProfileULong[] timeout = interfaces.getLocalLBProfilePersistence().get_timeout(profile_names);

		return timeout;
	}

	public void setTimeout(iControl.LocalLBProfileULong timeout) throws Exception // timeout
	{
		validateBaseValues();

		String[] profile_names = { _name };
		iControl.LocalLBProfileULong[] timeouts = { timeout };
		_LocalLBProfilePersistence.set_timeout(profile_names, timeouts);
	}

	void setTimeout(long timeout_long, boolean isParentValueForced) throws Exception // timeout
	{
		validateBaseValues();

		iControl.LocalLBProfileULong timeout = new iControl.LocalLBProfileULong();
		timeout.setValue(timeout_long);
		timeout.setDefault_flag(isParentValueForced); // true면 parent의 값을 받아서 쓰고, false면 사용자가 지정한 값을 쓴다.
		setTimeout(timeout);
	}

	static ArrayList<OBDtoAdcProfile> getAll(iControl.Interfaces interfaces) throws Exception {
		ArrayList<OBDtoAdcProfile> profileDtoSet = new ArrayList<OBDtoAdcProfile>();

		validateBaseValues(interfaces);

		// persistence 이름 리스트 구함
		String[] profiles = ProfilePersistence.getList(interfaces);

		int num = profiles.length;
		int i = 0;

		// persistence type 리스트 구함
		iControl.LocalLBProfilePersistenceMode[] modes = ProfilePersistence.getPersistenceMode(interfaces, profiles);

		// parent profile 리스트 구함
		String[] parents = ProfilePersistence.getDefaultProfile(interfaces, profiles);

		// persistence timeout 리스트 구함
		iControl.LocalLBProfileULong[] timeouts = ProfilePersistence.getTimeout(interfaces, profiles);

		// match across service 목록 구함
		iControl.LocalLBProfileEnabledState[] maStates = ProfilePersistence.getMatchAcrossService(interfaces, profiles);

		for (i = 0; i < num; i++) {
			OBDtoAdcProfile profileDto = new OBDtoAdcProfile();
			profileDto.setProfileName(profiles[i]);
			profileDto.setPersistenceType(CommonF5.valuePersistenceMode2FilteredInteger(modes[i].getValue()));
			profileDto.setParentProfile(parents[i]);
			profileDto.setTimeout(OBUtility.unsigned32((int) timeouts[i].getValue()));

			if (CommonF5.isCommonEnabledState(maStates[i].getValue()) == true) {
				profileDto.setMatchAcrossServiceYN(1);
			} else {
				profileDto.setMatchAcrossServiceYN(0);
			}

			profileDtoSet.add(profileDto);
		}
		return profileDtoSet;
	}

	public static ArrayList<PersistenceProfileRecord> testSeeAll(iControl.Interfaces interfaces) throws Exception {
		ArrayList<PersistenceProfileRecord> recSet = new ArrayList<PersistenceProfileRecord>();

		validateBaseValues(interfaces);

		String[] profile_list = ProfilePersistence.getList(interfaces);

		for (String profile : profile_list) {
			PersistenceProfileRecord rec = testSeeDetail(interfaces, profile);
			recSet.add(rec);
		}
		return recSet;
	}

	public static PersistenceProfileRecord testSeeDetail(iControl.Interfaces interfaces, String profile_name)
			throws Exception {
		validateBaseValues(interfaces);

		if (profile_name == null) {
			throw new Exception("Persistence profile: profile name error(null)");
		}

		PersistenceProfileRecord rec = new PersistenceProfileRecord();

		ProfilePersistence pp = new ProfilePersistence(interfaces, profile_name);
		iControl.LocalLBProfilePersistenceMode ppmode = pp.getPersistenceMode();
		iControl.LocalLBProfileEnabledState ppma = pp.getMatchAcrossService();
		iControl.LocalLBProfileULong pptimeout = pp.getTimeout();

		rec.setName(pp.getName());
		rec.setIsBasicProfile(pp.isBasic());
		rec.setParentProfile(pp.getDefaultProfile()); // parent profile
		rec.setType(ppmode.getValue().toString());
		rec.setTypeInherited(ppmode.isDefault_flag());
		rec.setMatchAcross(ppma.getValue().getValue());
		rec.setMatchAcrossInherited(ppma.isDefault_flag());
		rec.setTimeout(pptimeout.getValue());
		rec.setTimeoutInherited(pptimeout.isDefault_flag());

		return rec;
	}

	public static void testPrintPersistenceProfile(ArrayList<PersistenceProfileRecord> recSet, String report_file,
			boolean bExtend) {
		BufferedWriter report;
		try {
			report = new BufferedWriter(new FileWriter(report_file, bExtend));
			report.write("\n==========================================\n");
			report.write(" P E R S I S T E N C E  P R O F I L E\n");
			report.write("------------------------------------------\n");
			for (PersistenceProfileRecord rec : recSet) {
				report.write(" " + rec.getName() + " ");
				if (rec.getIsBasicProfile() == true) {
					report.write("/ system base profile\n");
				} else {
					report.write("/ user-defined profile\n");
				}
				report.write("\t parent profile:\n");
				report.write("\t\t " + rec.getParentProfile() + "\n");

				report.write("\t type:\n");
				report.write("\t\t " + rec.getType() + "\n");
				if (rec.getTypeInherited() == true) {
					report.write("\t\t " + "*custom off\n");
				} else {
					report.write("\t\t " + "*custom on\n");
				}

				report.write("\t match across service:\n");
				report.write("\t\t " + rec.getMatchAcross() + "\n");
				if (rec.getMatchAcrossInherited() == true) {
					report.write("\t\t " + "*custom off\n");
				} else {
					report.write("\t\t " + "*custom on\n");
				}

				report.write("\t timeout:\n");
				report.write("\t\t " + rec.getTimeout() + "\n");
				if (rec.getTimeoutInherited() == true) {
					report.write("\t\t " + "*custom off\n");
				} else {
					report.write("\t\t " + "*custom on\n");
				}
				report.write("------------------------------------------\n");
			}
			report.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

//	public static void main(String[] args)
//	{
//		iControl.Interfaces interface_ob = new iControl.Interfaces();
//		
//		interface_ob.initialize("175.196.72.100", "admin", "openbase");
//		
//		try
//		{
//			ArrayList<PersistenceProfileRecord> ppset = testSeeAll(interface_ob);
//			testPrintPersistenceProfile(ppset, "ProfilePersistence_List.txt", false);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
}