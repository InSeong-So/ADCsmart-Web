package kr.openbase.adcsmart.web.facade.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBAccount;
import kr.openbase.adcsmart.service.OBAdcManagement;
import kr.openbase.adcsmart.service.OBAlert;
import kr.openbase.adcsmart.service.OBIntegratedDashboard;
import kr.openbase.adcsmart.service.dashboard.OBIntegratedDashboardImpl;
import kr.openbase.adcsmart.service.dto.OBDtoADCGroupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAccount;
import kr.openbase.adcsmart.service.dto.OBDtoAccountRole;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoVSFilterInfo;
import kr.openbase.adcsmart.service.dto.OBDtoVSGroupInfo;
import kr.openbase.adcsmart.service.impl.OBAccountImpl;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBAlertImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.LoginFacade;
import kr.openbase.adcsmart.web.facade.dto.AccountDto;
import kr.openbase.adcsmart.web.facade.dto.AccountRoleDto;
import kr.openbase.adcsmart.web.facade.dto.SessionDto;

@Component
public class SysFacade {
	private transient Logger log = LoggerFactory.getLogger(SysFacade.class);

	@Autowired
	private LoginFacade loginFacade;

	private OBAccount accountSvc;
	private OBAlert alertSvc;
	private OBAdcManagement adcMgmt;
	private OBIntegratedDashboard dashboardServiceSvc;

	public SysFacade() {
		accountSvc = new OBAccountImpl();
		alertSvc = new OBAlertImpl();
		adcMgmt = new OBAdcManagementImpl();
		dashboardServiceSvc = new OBIntegratedDashboardImpl();
	}

	public AccountDto getAccountByIndex(Integer index) throws OBException, Exception {
		AccountDto account = null;
		List<OBDtoAccount> accountsFromSvc = accountSvc.getAccountList(index);
		log.debug("{}", accountsFromSvc);
		account = AccountDto.toAccountDto(accountsFromSvc.get(0));
		return account;
	}

	public AccountDto getAccount(String userId) throws OBException, Exception {
		return loginFacade.getAccount(userId);
	}

	public List<AccountDto> getAccountList(String accountId, String role, String searchKey, Integer orderType,
			Integer orderDir) throws OBException, Exception {
		List<AccountDto> users = new ArrayList<AccountDto>();
		if (role.equals("system")) {
			users.addAll(loginFacade.getAccounts(searchKey, orderType, orderDir));
		} else {
			users.add(loginFacade.getAccount(accountId));
		}

		return users;
	}

	public int getVSFilterInfoTotal(String searchKey) throws OBException, Exception {
		return dashboardServiceSvc.getDashboardVSFilterCount(searchKey);
	}

	public List<OBDtoVSFilterInfo> getVSFilterInfoList(String searchKey, Integer fromRow, Integer toRow,
			Integer orderType, Integer orderDir) throws OBException, Exception {

		return dashboardServiceSvc.getDashboardVSFilterList(searchKey, fromRow, toRow, orderType, orderDir);
//		List<OBDtoVSFilterInfo> vsFilterList = new ArrayList<OBDtoVSFilterInfo>();
//		OBDtoVSFilterInfo info1 = new OBDtoVSFilterInfo();
//		info1.setIndex("1111111");
//		info1.setAdcIndex(1);
//		info1.setAdcName("aaaaaa");
//		info1.setAdcType(1);
//		info1.setVsIndex("10.1.2.3_123123");
//		info1.setVsvcIndex("vssvcIndex");
//		info1.setVsIPAddress("1.2.3.4");
//		info1.setVsPort(80);
//		info1.setVsStatus(1);
//		info1.setShowHideState(1);
//
//		OBDtoVSFilterInfo info2 = new OBDtoVSFilterInfo();
//		info2.setIndex("22222222222222");
//		info2.setAdcIndex(2);
//		info2.setAdcName("222222");
//		info2.setAdcType(2);
//		info2.setVsStatus(1);
//		info2.setShowHideState(2);
//		info2.setVsIndex("10.1.2.3_123123");
//		info2.setVsIPAddress("2.2.2.2");
//		info2.setVsPort(8080);
//		info2.setVsvcIndex("vssvcIndex");
//
//		OBDtoVSFilterInfo info3 = new OBDtoVSFilterInfo();
//		info3.setAdcIndex(2);
//		info3.setAdcName("222222");
//		info3.setAdcType(2);
//		info3.setShowHideState(1);
//		info3.setVsStatus(2);
//		info3.setIndex("33333333333");
//		info3.setVsIndex("10.1.2.3_123123");
//		info3.setVsIPAddress("3.3.3.3");
//		info3.setVsPort(8080);
//		info3.setVsvcIndex("vssvcIndex");
//
//		vsFilterList.add(info1);
//		vsFilterList.add(info2);
//		vsFilterList.add(info3);
////				adcMgmt.getVSServiceGroup(searchKey, fromRow, toRow, orderType,
////				orderDir);
////	    ArrayList<OBDtoVSGroupInfo> vsGrpInfos = adcMgmt.getVSServiceGroup(null, 0, 10, 0, 0);
//		log.debug("vsGrpInfos : {}", vsFilterList);
//		return vsFilterList;
	}

	public boolean showHideVSFilterList(List<String> indexList, List<OBDtoVSFilterInfo> vsFilterInfoList, int opFlag)
			throws Exception {
		dashboardServiceSvc.addRemoveDashboardVSFilterList(indexList, vsFilterInfoList, opFlag);
		return true;
	}

	public int getVSServiceGroupTotal(String searchKey) throws OBException, Exception {
		return adcMgmt.getVSServiceGroupTotalCount(searchKey);
	}

	public List<OBDtoVSGroupInfo> getVSServiceGroup(String searchKey, Integer fromRow, Integer toRow, Integer orderType,
			Integer orderDir) throws OBException, Exception {
		ArrayList<OBDtoVSGroupInfo> vsGrpInfos = adcMgmt.getVSServiceGroup(searchKey, fromRow, toRow, orderType,
				orderDir);
//	    ArrayList<OBDtoVSGroupInfo> vsGrpInfos = adcMgmt.getVSServiceGroup(null, 0, 10, 0, 0);
		log.debug("vsGrpInfos : {}", vsGrpInfos);
		return vsGrpInfos;
	}

	public OBDtoVSGroupInfo getVSServiceGroup(Integer vsGroupIndex, Integer accountIndex)
			throws OBException, Exception {
		return adcMgmt.getVSServiceGroup(vsGroupIndex, accountIndex);
	}

	public ArrayList<OBDtoADCGroupInfo> getVSServiceGroupAll(Integer vsGroupIndex, Integer accountIndex)
			throws OBException, Exception {
		return adcMgmt.getVSServiceGroupAll(vsGroupIndex, accountIndex);
	}

	public boolean addVServiceGroup(OBDtoVSGroupInfo vsGroupInfo) throws OBException, Exception {
		log.debug("vsGroupInfo: {}", vsGroupInfo);
		return adcMgmt.addVSServiceGroup(vsGroupInfo);
	}

	public boolean setVServiceGroup(OBDtoVSGroupInfo vsGroupInfo) throws OBException, Exception {
		log.debug("vsGroupInfo: {}", vsGroupInfo);
		return adcMgmt.setVSServiceGroup(vsGroupInfo);
	}

	// serviceName 중복체크
	public boolean isExistVSServiceGroup(String vsGroupName) throws OBException, Exception {
		log.debug("vsGroupName: {}", vsGroupName);
		return adcMgmt.isExistVSServiceGroup(vsGroupName);
	}

	public void delServiceGrp(List<Integer> groupIndexList, SessionDto session) throws OBException, Exception {
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(groupIndexList.toString());
		adcMgmt.delVSServiceGroup(new ArrayList<Integer>(groupIndexList));
	}

	public List<AccountRoleDto> getAccountRoleList() throws OBException, Exception {
		List<AccountRoleDto> roles = new ArrayList<AccountRoleDto>();
		for (OBDtoAccountRole roleFromSvc : accountSvc.getAccountRoleList(null))
			roles.add(toAccountRoleDto(roleFromSvc));

		return roles;
	}

	private AccountRoleDto toAccountRoleDto(OBDtoAccountRole roleFromSvc) throws OBException, Exception {
		AccountRoleDto role = new AccountRoleDto();
		role.setId(roleFromSvc.getIndex());
		role.setName(roleFromSvc.getName());
		role.setDescription(roleFromSvc.getComment());

		return role;
	}

	public Map<String, AccountRoleDto> getAccountRoleMap() throws OBException, Exception {
		Map<String, AccountRoleDto> accountRoleMap = new HashMap<String, AccountRoleDto>();
		for (AccountRoleDto role : getAccountRoleList())
			accountRoleMap.put(String.valueOf(role.getId()), role);

		return accountRoleMap;
	}

	public void addAccount(AccountDto account, SessionDto session) throws OBException, Exception {
		OBDtoAccount accountFromSvc = AccountDto.toOBDtoAccount(account);
		log.debug("{}", accountFromSvc);
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(account.getId());
		accountSvc.addAccount(accountFromSvc, extraInfo);
	}

	public void delAccountsByIndices(List<Integer> accountIndices, SessionDto session) throws OBException, Exception {
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(accountIndices.toString());
		accountSvc.delAccount(new ArrayList<Integer>(accountIndices), extraInfo);
	}

	public void modifyAccount(AccountDto account, SessionDto session) throws OBException, Exception {
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		extraInfo.setExtraMsg1(account.getId());
		accountSvc.setAccount(AccountDto.toOBDtoAccount(account), extraInfo);
	}

	/*
	 * public void resetAccountPassword(int accountIndex, SessionDto session) throws
	 * OBException, Exception { OBDtoExtraInfo extraInfo =
	 * session.toOBDtoExtraInfo(); accountSvc.resetPassword(accountIndex,
	 * extraInfo); }
	 */
	public void changePassword(Integer accountIndex, String password, SessionDto session)
			throws OBException, Exception {
		OBDtoExtraInfo extraInfo = session.toOBDtoExtraInfo();
		accountSvc.changePassword(accountIndex, password, extraInfo);
	}

	// UserAlertTime 갱신
	public void updateUserAlertTimebyName(String accountName) throws Exception {
		alertSvc.updateUserAlertTimebyName(accountName);
	}

	public String getLogContent(String fileName) throws Exception {
		return accountSvc.getLogContent(fileName);
	}
}