package kr.openbase.adcsmart.web.controller.system;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.dto.OBDtoADCGroupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcRSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoRSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoVSFilterInfo;
import kr.openbase.adcsmart.service.dto.OBDtoVSGroupInfo;
import kr.openbase.adcsmart.service.dto.OBDtoVSInfo;
import kr.openbase.adcsmart.service.dto.OBDtoVSservice;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.controller.BaseAction;
import kr.openbase.adcsmart.web.facade.adcman.AdcFacade;
import kr.openbase.adcsmart.web.facade.dto.AccountDto;
import kr.openbase.adcsmart.web.facade.dto.AccountRoleDto;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.FaultSvcPerfInfoDto;
import kr.openbase.adcsmart.web.facade.system.SysFacade;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class SysSettingAction extends BaseAction {
	private transient Logger log = LoggerFactory.getLogger(SysSettingAction.class);

	@Autowired
	private SysFacade sysFacade;
	@Autowired
	private AdcFacade adcFacade;

	private AccountDto account;
	private Integer accntCloneRole;
	private Map<String, AdcDto> registeredAdcMap;
	private List<AdcDto> availableAdcs;
	private List<AccountRoleDto> roles;
	private Map<String, AccountRoleDto> roleMap;
	private List<AccountDto> accounts;
	private List<Integer> accountIndices;
	private String searchKey;
	private String userId;
	private String password;
	private String confirmPassword;
	private String history;
	private Integer index;
	private Integer orderDir; // �삤瑜몄감�닚 = 1, �궡由쇱감�닚 = 2
	private Integer orderType;
	private Map<String, OBDtoAdcVSInfo> registeredAdcVSMap;
	private List<OBDtoAdcVSInfo> availableAdcVSs;
	private String vsIndexList; // �꽑�깮�맂 vsIndexList
	private boolean isInitailized = false;
	private String contents;
	private String fileName;
	private Map<String, OBDtoAdcRSInfo> registeredAdcRSMap;
	private List<OBDtoAdcRSInfo> availableAdcRSs;
	private String rsIndexList; // �꽑�깮�맂 rsIndexList
	private ArrayList<FaultSvcPerfInfoDto> svcPerfInfoList;
	private OBDtoADCObject adcObject;
	private OBDtoSearch searchOption;
	private OBDtoOrdering orderOption;
	private Integer rowTotal;
	private Integer fromRow;
	private Integer toRow;

	// �꽌鍮꾩뒪 洹몃９ �꽕�젙
	private OBDtoVSGroupInfo vsGrpInfo;
	private List<OBDtoVSGroupInfo> vsGrpInfoList;
	private List<OBDtoADCGroupInfo> adcSelectedGroupList;
	private List<OBDtoADCGroupInfo> adcAllGroupList;
	private List<OBDtoVSFilterInfo> vsFilterInfoList;
	private List<String> vsFilterInfoIndexList;
	private String vsGroupName;
	private List<Integer> groupIndexList;
	private Integer roleIdVal;

	public List<Integer> getGroupIndexList() {
		return groupIndexList;
	}

	public void setGroupIndexList(List<Integer> groupIndexList) {
		this.groupIndexList = groupIndexList;
	}

	public List<OBDtoADCGroupInfo> getAdcSelectedGroupList() {
		return adcSelectedGroupList;
	}

	public void setAdcSelectedGroupList(List<OBDtoADCGroupInfo> adcSelectedGroupList) {
		this.adcSelectedGroupList = adcSelectedGroupList;
	}

	public List<OBDtoADCGroupInfo> getAdcAllGroupList() {
		return adcAllGroupList;
	}

	public void setAdcAllGroupList(List<OBDtoADCGroupInfo> adcAllGroupList) {
		this.adcAllGroupList = adcAllGroupList;
	}

	public Integer getFromRow() {
		return fromRow;
	}

	public void setFromRow(Integer fromRow) {
		this.fromRow = fromRow;
	}

	public Integer getToRow() {
		return toRow;
	}

	public void setToRow(Integer toRow) {
		this.toRow = toRow;
	}

	public Integer getRowTotal() {
		return rowTotal;
	}

	public void setRowTotal(Integer rowTotal) {
		this.rowTotal = rowTotal;
	}

	public OBDtoVSGroupInfo getVsGrpInfo() {
		return vsGrpInfo;
	}

	public void setVsGrpInfo(OBDtoVSGroupInfo vsGrpInfo) {
		this.vsGrpInfo = vsGrpInfo;
	}

	public List<OBDtoVSGroupInfo> getVsGrpInfoList() {
		return vsGrpInfoList;
	}

	public void setVsGrpInfoList(List<OBDtoVSGroupInfo> vsGrpInfoList) {
		this.vsGrpInfoList = vsGrpInfoList;
	}

	public String getVsGroupName() {
		return vsGroupName;
	}

	public void setVsGroupName(String vsGroupName) {
		this.vsGroupName = vsGroupName;
	}

	public String loadLeftPane() throws OBException {
		return SUCCESS;
	}

	public String loadUserListContent() throws OBException {
		try {
			log.debug("{}", searchKey);
			accounts = sysFacade.getAccountList(session.getAccountId(), session.getAccountRole(), searchKey, orderType,
					orderDir);
			log.debug("{}", accounts);
			roleMap = sysFacade.getAccountRoleMap();
			log.debug("{}", roleMap);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String loadUserAddContent() throws OBException {
		try {
			roles = sysFacade.getAccountRoleList();
			log.debug("{}", roles);
			availableAdcs = adcFacade.getAllAdcs(session.getAccountIndex(), null);
			availableAdcVSs = adcFacade.getAllAdcVSs(session.getAccountIndex(), null);
			availableAdcRSs = adcFacade.getAllAdcRSs(session.getAccountIndex(), null);
			accounts = sysFacade.getAccountList(session.getAccountId(), session.getAccountRole(), null, 33, 2);
			log.debug("{}", availableAdcs);
			log.debug("{}", availableAdcVSs);
			log.debug("{}", availableAdcRSs);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String loadAccntCloneRole() throws OBException {
		isSuccessful = true;
		try {
			Integer accountIndex = (account == null ? session.getAccountIndex() : account.getIndex());
			account = sysFacade.getAccountByIndex(accountIndex);
			accntCloneRole = account.getRoleId();
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String retrieveVsFilterTotal() throws Exception {
		isSuccessful = true;
		try {
			log.debug("searchKey: {}", searchKey);
			rowTotal = sysFacade.getVSFilterInfoTotal(searchKey);
//			vsFilterInfoList = sysFacade.getVSFilterInfoList(searchKey, fromRow, toRow, orderType, orderDir);
//			rowTotal = vsFilterInfoList.size();
			log.debug("getVSFilterInfoTotal: {}", rowTotal);
		}
//	    catch(OBException e)
//	    {
//	        throw e;
//	    }
		catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String loadVsFilterListContent() throws Exception {
		try {
			log.debug("searchKey:{}", searchKey);

			vsFilterInfoList = sysFacade.getVSFilterInfoList(searchKey, fromRow, toRow, orderType, orderDir);
//			rowTotal = vsFilterInfoList.size();
			isSuccessful = true;
			log.debug("{}", vsFilterInfoList);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return SUCCESS;
	}

	public String loadUserModifyContent() throws OBException {
		try {
			Integer accountIndex = (account == null ? session.getAccountIndex() : account.getIndex());
			account = sysFacade.getAccountByIndex(accountIndex);
//			accntCloneRole = account.getRoleId();
			roles = sysFacade.getAccountRoleList();
			registeredAdcMap = adcFacade.getRegisteredAdcMap(accountIndex);
			registeredAdcVSMap = adcFacade.getRegisteredAdcVSMap(accountIndex);
			registeredAdcRSMap = adcFacade.getRegisteredAdcRSMap(accountIndex);

			availableAdcs = adcFacade.getAvailableAdcs(accountIndex, null);
			availableAdcVSs = adcFacade.getAvailableAdcVSs(accountIndex, null);
			availableAdcRSs = adcFacade.getAvailableAdcRSs(accountIndex, null);

			log.debug("{}", registeredAdcMap);
			log.debug("{}", registeredAdcVSMap);
			log.debug("{}", registeredAdcRSMap);
			log.debug("{}", availableAdcs);
			log.debug("{}", availableAdcVSs);
			log.debug("{}", availableAdcRSs);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String showVSFilterList() throws OBException {
		isSuccessful = true;
		try {
			sysFacade.showHideVSFilterList(vsFilterInfoIndexList, vsFilterInfoList, OBDefine.SHOW_FLAG);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String hideVSFilterList() throws OBException {
		isSuccessful = true;
		try {
			vsFilterInfoList = sysFacade.getVSFilterInfoList(searchKey, fromRow, toRow, orderType, orderDir);
			sysFacade.showHideVSFilterList(vsFilterInfoIndexList, vsFilterInfoList, OBDefine.HIDE_FLAG);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String UserIdCheck() throws OBException // 以묐났�맂 �븘�씠�뵒 泥댄겕 異붽� junhyun.ok_GS
	{
		try {
			AccountDto accountCheck;
			accountCheck = sysFacade.getAccount(account.getId());

			if (accountCheck != null) {
				isSuccessful = false; // �븘�씠�뵒 以묐났
			} else {
				isSuccessful = true; // �븘�씠�뵒 以묐났 �븞�맖
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String addUser() throws OBException {
		log.debug("{}", account);
		if (!validateUserAdd())
			return SUCCESS;

		isSuccessful = true;
		try {

//			account.setHistory(account.getPassword());
			account.setChangedTime(new Timestamp(System.currentTimeMillis()));

			account.setRoleId(roleIdVal);
			if ((account.getRoleId() == 4) || (account.getRoleId() == 5)) {
				if (vsIndexList != null && !vsIndexList.equals("")) {
					account.setAdcVsList(getAdcVSInfo(vsIndexList)); // vsIndex list �쟾�떖
				}

				if (rsIndexList != null && !rsIndexList.equals("")) {
					account.setAdcRsList(getAdcRSInfo(rsIndexList)); // rsIndex list �쟾�떖
				}
			}
			sysFacade.addAccount(account, session.getSessionDto());
			sysFacade.updateUserAlertTimebyName(account.getId());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	private boolean validateUserAdd() throws OBException {
		isSuccessful = false;
		if (!account.getPassword().equals(account.getConfirmPassword())) {
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PWD_VALIDATE);
			return false;
		}

		isSuccessful = true;
		return true;
	}

	public String delAccounts() throws OBException {
		isSuccessful = false;
		try {
			sysFacade.delAccountsByIndices(accountIndices, session.getSessionDto());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		isSuccessful = true;
		return SUCCESS;
	}

	public String modifyUser() throws OBException {
		log.debug("{}", account);

		/*
		 * if (!validateUserAdd()) return SUCCESS;
		 */

		isSuccessful = true;
		try {
			if ((account.getRoleId() == 4) || (account.getRoleId() == 5)) {
				if (vsIndexList != null && !vsIndexList.equals("")) {
					account.setAdcVsList(getAdcVSInfo(vsIndexList)); // vsIndex list �쟾�떖
				}

				if (rsIndexList != null && !rsIndexList.equals("")) {
					account.setAdcRsList(getAdcRSInfo(rsIndexList)); // rsIndex list �쟾�떖
				}
			}
			sysFacade.modifyAccount(account, session.getSessionDto());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	private ArrayList<OBDtoAdcVSInfo> getAdcVSInfo(String vsIndexList) {
		String[] arrVsIndex = vsIndexList.split("\\|");

		ArrayList<OBDtoAdcVSInfo> adcVsList = new ArrayList<OBDtoAdcVSInfo>();

		for (int i = 0; i < arrVsIndex.length; i++) {
			String vsIndex = arrVsIndex[i];
			String adcIndex = vsIndex.split("\\_")[0];

			OBDtoAdcVSInfo adcVSInfos = null;
			for (int j = 0; j < adcVsList.size(); j++) {
				OBDtoAdcVSInfo adcVSInfosTemp = adcVsList.get(j);
				if (adcVSInfosTemp.getAdcIndex() == Integer.parseInt(adcIndex)) {
					adcVSInfos = adcVSInfosTemp;
					break;
				}
			}

			if (null == adcVSInfos) {
				adcVSInfos = new OBDtoAdcVSInfo();
				adcVSInfos.setAdcIndex(Integer.parseInt(adcIndex));
				adcVSInfos.setVsInfoList(new ArrayList<OBDtoVSInfo>());
				adcVsList.add(adcVSInfos);
			}

			//
			ArrayList<OBDtoVSInfo> vsInfoList = adcVSInfos.getVsInfoList();
			OBDtoVSInfo vsInfo = new OBDtoVSInfo();
			vsInfo.setVsIndex(vsIndex);
			vsInfoList.add(vsInfo);
		}

		return adcVsList;
	}

	private ArrayList<OBDtoADCGroupInfo> getAdcVSGrpInfo(String vsIndexList) {
		String[] arrVsIndex = vsIndexList.split("\\|");

		ArrayList<OBDtoADCGroupInfo> adcVsList = new ArrayList<OBDtoADCGroupInfo>();

		for (int i = 0; i < arrVsIndex.length; i++) {
			String vsIndex = arrVsIndex[i];
			String adcIndex = vsIndex.split("\\_")[0];

			OBDtoADCGroupInfo adcVSInfos = null;
			for (int j = 0; j < adcVsList.size(); j++) {
				OBDtoADCGroupInfo adcVSInfosTemp = adcVsList.get(j);
				if (adcVSInfosTemp.getIndex() == Integer.parseInt(adcIndex)) {
					adcVSInfos = adcVSInfosTemp;
					break;
				}
			}

			if (null == adcVSInfos) {
				adcVSInfos = new OBDtoADCGroupInfo();
				adcVSInfos.setIndex(Integer.parseInt(adcIndex));
				adcVSInfos.setVsList(new ArrayList<OBDtoVSservice>());
				adcVsList.add(adcVSInfos);
			}

			//
			ArrayList<OBDtoVSservice> vsInfoList = adcVSInfos.getVsList();
			OBDtoVSservice vsInfo = new OBDtoVSservice();
			vsInfo.setVsIndex(vsIndex);
			vsInfoList.add(vsInfo);
		}

		return adcVsList;
	}

//	private ArrayList<OBDtoVSservice> getAdcVSGrpInfo(String vsIndexList) 
//    {
//        String[] arrVsIndex = vsIndexList.split("\\|"); 
//        ArrayList<OBDtoVSservice> vsList = new ArrayList<OBDtoVSservice>(); 
//        for(int i=0; i < arrVsIndex.length; i++)
//        {
//            String vsIndex = arrVsIndex[i]; 
//            String adcIndex = vsIndex.split("\\_")[0];
//            OBDtoVSservice vs = new OBDtoVSservice();
//            vs.setAdcIndex(Integer.parseInt(adcIndex));
//            vs.setVsIndex(vsIndex);
//            vsList.add(vs);
//        }        
//        
//        return vsList;
//    }

	private ArrayList<OBDtoAdcRSInfo> getAdcRSInfo(String rsIndexList) {
		String[] arrRsIndex = rsIndexList.split("\\|");

		ArrayList<OBDtoAdcRSInfo> adcRsList = new ArrayList<OBDtoAdcRSInfo>();

		for (int i = 0; i < arrRsIndex.length; i++) {
			String rsIndex = arrRsIndex[i];
			String adcIndex = rsIndex.split("\\_")[0];

			OBDtoAdcRSInfo adcRSInfos = null;
			for (int j = 0; j < adcRsList.size(); j++) {
				OBDtoAdcRSInfo adcRSInfosTemp = adcRsList.get(j);
				if (adcRSInfosTemp.getAdcIndex() == Integer.parseInt(adcIndex)) {
					adcRSInfos = adcRSInfosTemp;
					break;
				}
			}

			if (null == adcRSInfos) {
				adcRSInfos = new OBDtoAdcRSInfo();
				adcRSInfos.setAdcIndex(Integer.parseInt(adcIndex));
				adcRSInfos.setRsInfoList(new ArrayList<OBDtoRSInfo>());
				adcRsList.add(adcRSInfos);
			}

			ArrayList<OBDtoRSInfo> rsInfoList = adcRSInfos.getRsInfoList();
			OBDtoRSInfo rsInfo = new OBDtoRSInfo();
			rsInfo.setRsIndex(rsIndex);
			rsInfoList.add(rsInfo);
		}

		return adcRsList;
	}

	public String changePassword() throws OBException {
		// 鍮꾨�踰덊샇 蹂�寃�

		isSuccessful = true;

		try {
			// 현재 userId의 정보를 가져옴.
			account = sysFacade.getAccount(userId);
			// Password History를 확인하는 플래그
			if (password == null || account.getHistory().contains(password)) {
				isSuccessful = false;
				return SUCCESS;
			} else {
				sysFacade.changePassword(index, password, session.getSessionDto());
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	// adc log �젙蹂대�� �솕硫댁뿉 �몴�떆�븿.
	public String loadLogContent() throws OBException {
		// �솕硫댁뿉 �몴�떆�븯怨좎옄�븯�뒗 �뜲�씠�꽣瑜� ���옣�븳�떎.
//		if(false == isInitailized)
//		{ 
//			contents = "";
//			return SUCCESS;
//		}

		if (fileName == null) {
			contents = "";
			return SUCCESS;
		}

		try {
			contents = sysFacade.getLogContent(fileName);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		return SUCCESS;
	}

	// �꽌鍮꾩뒪 洹몃９ Count
	public String retrieveServiceGroupTotal() throws Exception {
		isSuccessful = true;
		try {
			log.debug("searchKey: {}", searchKey);
			rowTotal = sysFacade.getVSServiceGroupTotal(searchKey);
			log.debug("serviceGrpTotal: {}", rowTotal);
		}
//	    catch(OBException e)
//	    {
//	        throw e;
//	    }
		catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	// �꽌鍮꾩뒪 洹몃９ 異붽�
	public String loadServiceGroupContent() throws Exception {
		try {
			log.debug("searchKey:{}", searchKey);

			vsGrpInfoList = sysFacade.getVSServiceGroup(searchKey, fromRow, toRow, orderType, orderDir);
//            vsGrpInfoList = sysFacade.getVSServiceGroup(null, 0, 10, 0, 0);
			log.debug("{}", vsGrpInfoList);

//            accounts = sysFacade.getAccountList(session.getAccountId(), session.getAccountRole(), searchKey, orderType, orderDir);
//            log.debug("{}", accounts);
//            roleMap = sysFacade.getAccountRoleMap();
//            log.debug("{}", roleMap);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String loadServiceGrpAddContent() throws Exception {
		try {
//            roles = sysFacade.getAccountRoleList();
//            log.debug("{}", roles);
//            availableAdcVSs = adcFacade.getAllAdcVSs(session.getAccountIndex(), null);
//            log.debug("{}", availableAdcVSs);
			Integer accountIndex = (account == null ? session.getAccountIndex() : account.getIndex());
			adcAllGroupList = sysFacade.getVSServiceGroupAll(0, accountIndex);

//            String vsGroupName = (vsGrpInfo == null ? null : vsGrpInfo.getName());
//            vsGrpInfo = sysFacade.getVSServiceGroup(vsGroupName);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String loadServiceGrpModifyContent() throws Exception {
		try {
			Integer accountIndex = (account == null ? session.getAccountIndex() : account.getIndex());
//            account = sysFacade.getAccountByIndex(accountIndex);
//            roles = sysFacade.getAccountRoleList();

			Integer vsGroupIndex = (vsGrpInfo == null ? null : vsGrpInfo.getIndex());
			vsGrpInfo = sysFacade.getVSServiceGroup(vsGroupIndex, accountIndex);
			adcAllGroupList = sysFacade.getVSServiceGroupAll(vsGroupIndex, accountIndex);

//            registeredAdcVSMap = adcFacade.getRegisteredAdcVSMap(accountIndex);            
//            availableAdcVSs = adcFacade.getAvailableAdcVSs(accountIndex, null);

			log.debug("{}", registeredAdcVSMap);
			log.debug("{}", availableAdcVSs);

		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String addServiceGrp() throws OBException {
//	    log.debug("{}", account);
//        if (!validateUserAdd())
//            return SUCCESS;

		isSuccessful = true;
		try {
			log.debug("vsGrpInfo: {}", vsGrpInfo);
//            if((account.getRoleId() == 4))
//            {
			if (vsIndexList != null && !vsIndexList.equals("")) {
//                    account.setAdcVsList(getAdcVSInfo(vsIndexList));

//                    ArrayList<OBDtoAdcVSInfo> adcVsList = new ArrayList<OBDtoAdcVSInfo>();
//                    adcVsList = getAdcVSInfo(vsIndexList);
//                    vsGrpInfo.setIndex();
//                    vsGrpInfo.setName(vsGroupName);
				vsGrpInfo.setAdcList(getAdcVSGrpInfo(vsIndexList));
//                    vsGrpInfo.setAdcVsList(adcVsList);         //vsIndex list �쟾�떖          

				// [OBDtoAdcVSInfo [adcIndex=1, adcName=null, vsInfoList=[OBDtoVSInfo
				// [vsIndex=1_30, vsName=null, vsIPAddress=null, vsPort=null], OBDtoVSInfo
				// [vsIndex=1_62, vsName=null, vsIPAddress=null, vsPort=null]]]]
			}
//            }

			sysFacade.addVServiceGroup(vsGrpInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String modifyServiceGrp() throws OBException {
//      log.debug("{}", account);
//        if (!validateUserAdd())
//            return SUCCESS;

		isSuccessful = true;
		try {
			log.debug("vsGrpInfo: {}", vsGrpInfo);
//            if((account.getRoleId() == 4))
//            {
			if (vsIndexList != null && !vsIndexList.equals("")) {
				vsGrpInfo.setAdcList(getAdcVSGrpInfo(vsIndexList)); // vsIndex list �쟾�떖
			}
//            }

			sysFacade.setVServiceGroup(vsGrpInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public String delServiceGrp() throws OBException {
		isSuccessful = false;
		try {
			sysFacade.delServiceGrp(groupIndexList, session.getSessionDto());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		isSuccessful = true;
		return SUCCESS;
	}

	public String serviceGrpNameCheck() throws OBException // 以묐났�맂 �븘�씠�뵒 泥댄겕 異붽� junhyun.ok_GS
	{
		try {
			Boolean nameCheck;
			nameCheck = sysFacade.isExistVSServiceGroup(vsGroupName);
			if (nameCheck) {
				isSuccessful = false; // �븘�씠�뵒 以묐났
			} else {
				isSuccessful = true; // �븘�씠�뵒 以묐났 �븞�맖
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return SUCCESS;
	}

	public ArrayList<FaultSvcPerfInfoDto> getSvcPerfInfoList() {
		return svcPerfInfoList;
	}

	public void setSvcPerfInfoList(ArrayList<FaultSvcPerfInfoDto> svcPerfInfoList) {
		this.svcPerfInfoList = svcPerfInfoList;
	}

	public OBDtoADCObject getAdcObject() {
		return adcObject;
	}

	public void setAdcObject(OBDtoADCObject adcObject) {
		this.adcObject = adcObject;
	}

	public OBDtoSearch getSearchOption() {
		return searchOption;
	}

	public void setSearchOption(OBDtoSearch searchOption) {
		this.searchOption = searchOption;
	}

	public OBDtoOrdering getOrderOption() {
		return orderOption;
	}

	public void setOrderOption(OBDtoOrdering orderOption) {
		this.orderOption = orderOption;
	}

	public String loadBackupContent() throws OBException {
		return SUCCESS;
	}

	public String loadBackupAddContent() throws OBException {
		return SUCCESS;
	}

	public String loadSystemInfoContent() throws OBException {
		return SUCCESS;
	}

	public AccountDto getAccount() {
		return account;
	}

	public void setAccount(AccountDto account) {
		this.account = account;
	}

	public Map<String, AdcDto> getRegisteredAdcMap() {
		return registeredAdcMap;
	}

	public void setRegisteredAdcMap(Map<String, AdcDto> registeredAdcMap) {
		this.registeredAdcMap = registeredAdcMap;
	}

	public List<AdcDto> getAvailableAdcs() {
		return availableAdcs;
	}

	public void setAvailableAdcs(List<AdcDto> availableAdcs) {
		this.availableAdcs = availableAdcs;
	}

	public List<AccountRoleDto> getRoles() {
		return roles;
	}

	public void setRoles(List<AccountRoleDto> roles) {
		this.roles = roles;
	}

	public Map<String, AccountRoleDto> getRoleMap() {
		return roleMap;
	}

	public void setRoleMap(Map<String, AccountRoleDto> roleMap) {
		this.roleMap = roleMap;
	}

	public List<AccountDto> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<AccountDto> accounts) {
		this.accounts = accounts;
	}

	public List<Integer> getAccountIndices() {
		return accountIndices;
	}

	public void setAccountIndices(List<Integer> accountIndices) {
		this.accountIndices = accountIndices;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getOrderDir() {
		return orderDir;
	}

	public void setOrderDir(Integer orderDir) {
		this.orderDir = orderDir;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Map<String, OBDtoAdcVSInfo> getRegisteredAdcVSMap() {
		return registeredAdcVSMap;
	}

	public void setRegisteredAdcVSMap(Map<String, OBDtoAdcVSInfo> registeredAdcVSMap) {
		this.registeredAdcVSMap = registeredAdcVSMap;
	}

	public List<OBDtoAdcVSInfo> getAvailableAdcVSs() {
		return availableAdcVSs;
	}

	public void setAvailableAdcVSs(List<OBDtoAdcVSInfo> availableAdcVSs) {
		this.availableAdcVSs = availableAdcVSs;
	}

	public String getVsIndexList() {
		return vsIndexList;
	}

	public void setVsIndexList(String vsIndexList) {
		this.vsIndexList = vsIndexList;
	}

	public boolean isInitailized() {
		return isInitailized;
	}

	public void setInitailized(boolean isInitailized) {
		this.isInitailized = isInitailized;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Map<String, OBDtoAdcRSInfo> getRegisteredAdcRSMap() {
		return registeredAdcRSMap;
	}

	public void setRegisteredAdcRSMap(Map<String, OBDtoAdcRSInfo> registeredAdcRSMap) {
		this.registeredAdcRSMap = registeredAdcRSMap;
	}

	public List<OBDtoAdcRSInfo> getAvailableAdcRSs() {
		return availableAdcRSs;
	}

	public void setAvailableAdcRSs(List<OBDtoAdcRSInfo> availableAdcRSs) {
		this.availableAdcRSs = availableAdcRSs;
	}

	public String getRsIndexList() {
		return rsIndexList;
	}

	public void setRsIndexList(String rsIndexList) {
		this.rsIndexList = rsIndexList;
	}

	public Integer getAccntCloneRole() {
		return accntCloneRole;
	}

	public void setAccntCloneRole(Integer accntCloneRole) {
		this.accntCloneRole = accntCloneRole;
	}

	public Integer getRoleIdVal() {
		return roleIdVal;
	}

	public void setRoleIdVal(Integer roleIdVal) {
		this.roleIdVal = roleIdVal;
	}

	public List<OBDtoVSFilterInfo> getVsFilterInfoList() {
		return vsFilterInfoList;
	}

	public void setVsFilterInfoList(List<OBDtoVSFilterInfo> vsFilterInfoList) {
		this.vsFilterInfoList = vsFilterInfoList;
	}

	public List<String> getVsFilterInfoIndexList() {
		return vsFilterInfoIndexList;
	}

	public void setVsFilterInfoIndexList(List<String> vsFilterInfoIndexList) {
		this.vsFilterInfoIndexList = vsFilterInfoIndexList;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
