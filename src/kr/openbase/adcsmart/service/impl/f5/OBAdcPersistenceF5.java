package kr.openbase.adcsmart.service.impl.f5;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.OBAdcProfile;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcProfile;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBSystemAuditImpl;
import kr.openbase.adcsmart.service.impl.OBVServerDB;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;

public class OBAdcPersistenceF5 implements OBAdcProfile {
	@Override
	public ArrayList<OBDtoAdcProfile> getProfileList(Integer adcIndex, String profileIndex)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// DB 작업. profileName이 null이면 전체 목록을 뽑는다.
		return new OBVServerDB().getF5ProfileList(adcIndex, profileIndex, OBDefine.ORDER_TYPE_FIRST,
				OBDefine.ORDER_DIR_DESCEND);
	}

	@Override
	public void addProfile(Integer adcIndex, OBDtoAdcProfile profile, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// 장비 기본 확인
		OBDtoAdcInfo adcInfo;
		adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

		if (adcInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					"Persistence profile add: adcInfo error(null).");
		}
		// F5 icontrol interface
		iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);

		// persistence profile 추가
		ProfilePersistence pp;
		try {
			pp = new ProfilePersistence(interfaces, profile.getProfileName());
			pp.create(CommonF5.valueInteger2PersistenceMode(profile.getPersistenceType()));

			// 설정보완
			pp.setDefaultProfile(profile.getParentProfile());
			if (profile.getMatchAcrossServiceYN() == 0) {
				pp.setMatchAcrossService(false, false); // 2nd false: parent profile의 값을 받아 쓰지 않고 자기 설정값으로 함
			} else {
				pp.setMatchAcrossService(true, false); // 2nd false: parent profile의 값을 받아 쓰지 않고 자기 설정값으로 함
			}
			pp.setTimeout((long) profile.getTimeout(), false); // false: parent profile의 값을 받아 쓰지 않고 자기 설정값으로 함

			// 디스크에 저장
			SystemF5.saveHighConfig(interfaces);

			// profile DB 업데이트
			updatePersistenceProfileDB(adcIndex, interfaces);
			new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex, extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_PROFILE_ADD_SUCCESS, adcInfo.getName(), profile.getProfileName(), null);
		} catch (Exception e) {
			CommonF5.Exception("Add persistence profile error. ", e.getMessage());
		}
	}

	public void updatePersistenceProfileDB(int adcIndex, iControl.Interfaces interfaces) throws Exception {
		OBVServerDB vDB = new OBVServerDB();

		vDB.delF5ProfileAll(adcIndex); // DB의 persistence profiles 모두 삭제

		// persistence profile을 device에서 가져오기
		ArrayList<OBDtoAdcProfile> profileList = ProfilePersistence.getAll(interfaces);

		for (OBDtoAdcProfile obj : profileList) {
			vDB.addF5ProfileInfo(adcIndex, obj);
		}
	}

	@Override
	public void setProfile(Integer adcIndex, OBDtoAdcProfile profile, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// 장비 기본 확인
		OBDtoAdcInfo adcInfo;
		adcInfo = new OBAdcManagementImpl().getAdcInfo(profile.getAdcIndex());

		if (adcInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					"Persistence profile edit: adcInfo error(null).");
		}
		// F5 icontrol interface
		iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);

		ProfilePersistence pp;
		try {
			pp = new ProfilePersistence(interfaces, profile.getProfileName());
			pp.setPersistenceMode(CommonF5.valueInteger2PersistenceMode(profile.getPersistenceType()), false);

			if (profile.getParentProfile() != null && !profile.getParentProfile().isEmpty()) {
				pp.setDefaultProfile(profile.getParentProfile());
			}

			if (profile.getMatchAcrossServiceYN() == 0) {
				pp.setMatchAcrossService(false, false); // 2nd false: parent profile의 값을 받아 쓰지 않고 자기 설정값으로 함
			} else {
				pp.setMatchAcrossService(true, false); // 2nd false: parent profile의 값을 받아 쓰지 않고 자기 설정값으로 함
			}

			pp.setTimeout((long) profile.getTimeout(), false); // false: parent profile의 값을 받아 쓰지 않고 자기 설정값으로 함

			// 디스크에 저장
			SystemF5.saveHighConfig(interfaces);

			// profile DB 업데이트
			updatePersistenceProfileDB(profile.getAdcIndex(), interfaces);
			new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex, extraInfo.getClientIPAddress(),
					OBSystemAuditImpl.AUDIT_PROFILE_SET_SUCCESS, adcInfo.getName(), profile.getProfileName(), null);
		} catch (Exception e) {
			CommonF5.Exception("Edit persistence profile error. ", e.getMessage());
		}
	}

	@Override
	public void delProfile(Integer adcIndex, ArrayList<String> profileIndexList, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// 장비 기본 확인
		OBDtoAdcInfo adcInfo;
		adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

		ArrayList<String> tempNameList = new OBVServerDB().getF5ProfileNameList(profileIndexList);

		String[] profile_names = tempNameList.toArray(new String[tempNameList.size()]);

		if (adcInfo == null) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
					"Persistence profile deletion: adcInfo error(null).");
		}

		// F5 icontrol interface
		iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo);
		try {
			ProfilePersistence.delete(interfaces, profile_names);

			// 디스크에 저장
			SystemF5.saveHighConfig(interfaces);

			// profile DB 업데이트
			updatePersistenceProfileDB(adcIndex, interfaces);
			for (String name : profile_names)
				new OBSystemAuditImpl().writeLogAdc(extraInfo.getAccountIndex(), adcIndex,
						extraInfo.getClientIPAddress(), OBSystemAuditImpl.AUDIT_PROFILE_DEL_SUCCESS, adcInfo.getName(),
						name, null);
		} catch (Exception e) {
			CommonF5.Exception("Delete persistence profile error. ", e.getMessage());
		}
	}

	@Override
	public ArrayList<OBDtoAdcProfile> searchProfileList(Integer adcIndex, String searchKey)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		// TODO Auto-generated method stub
		return new OBVServerDB().searchF5ProfileList(adcIndex, searchKey, OBDefine.ORDER_TYPE_FIRST,
				OBDefine.ORDER_DIR_ASCEND);
	}

	// test functions
	// --------------------------------------------------------------------------------------
//	public static void main(String[] args)
//	{
//		iControl.Interfaces inter = new iControl.Interfaces();
//		
//		inter.initialize("192.168.200.11", "admin", "admin");
//		try
//		{
//			OBAdcPersistenceF5 profile = new OBAdcPersistenceF5();
//			//ArrayList<OBDtoAdcProfile> profileDtoList = profile.getProfileList(1, null);
//			ArrayList<OBDtoAdcProfile> profileDtoList = profile.searchProfileList(1, "bbdd");
//			for(int i=0; i<profileDtoList.size(); i++)
//			{
//				//System.out.println(profileDtoList.get(i).getProfileName() + " / " + profileDtoList.get(i).getPersistenceType());
//				System.out.println(profileDtoList.get(i).toString());
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public ArrayList<OBDtoAdcProfile> searchProfileList(Integer adcIndex, String searchKey, Integer orderType,
			Integer orderDir) throws OBExceptionUnreachable, OBExceptionLogin, OBException {
		return new OBVServerDB().searchF5ProfileList(adcIndex, searchKey, orderType, orderDir);
	}
}