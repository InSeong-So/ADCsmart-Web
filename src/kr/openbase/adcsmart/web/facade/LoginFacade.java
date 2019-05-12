package kr.openbase.adcsmart.web.facade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBAccount;
import kr.openbase.adcsmart.service.dto.OBDtoAccount;
import kr.openbase.adcsmart.service.impl.OBAccountImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.facade.dto.AccountDto;

@Component
public class LoginFacade {
	private transient Logger log = LoggerFactory.getLogger(LoginFacade.class);

	private OBAccount accountSvc;

	public LoginFacade() {
		accountSvc = new OBAccountImpl();
	}

	public AccountDto getAccount(String accountId) throws OBException, Exception {
		log.debug("accountId:{}", accountId);
		OBDtoAccount accountFromSvc = accountSvc.getAccountInfo(accountId);
		log.debug("{}", accountFromSvc);
		AccountDto account = AccountDto.toAccountDto(accountFromSvc);
		log.debug("{}", account);
		return account;
	}

	public List<AccountDto> getAccounts(String searchKey, Integer orderType, Integer orderDir)
			throws OBException, Exception {
		log.debug("searchKey:{}", searchKey);
		ArrayList<OBDtoAccount> accountsFromSvc = accountSvc
				.searchAccountList(StringUtils.isEmpty(searchKey) ? null : searchKey, orderType, orderDir);
		log.debug("{}", accountsFromSvc);
		List<AccountDto> accounts = AccountDto.toAccountDto(accountsFromSvc);
		log.debug("{}", accounts);
		return accounts;
	}

	public void login(Integer accountIndex) throws Exception {
		try {
			accountSvc.setlastLoginTime(accountIndex);
		} catch (OBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public String encryptPassword(String password) throws Exception {
		if (StringUtils.isEmpty(password))
			return "";

		try {
			return accountSvc.encryptPassword(password);
		} catch (OBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public static List<Integer> retrieveAccountIndicesFromIds(List<String> accountIds) throws OBException {
		List<Integer> accountIndices = new ArrayList<Integer>();
		if (CollectionUtils.isEmpty(accountIds))
			return accountIndices;

		OBAccount accountSvc = new OBAccountImpl();
		for (String id : accountIds) {
			OBDtoAccount accountFromSvc = accountSvc.getAccountInfo(id);
			accountIndices.add(accountFromSvc.getAccountIndex());
		}

		return accountIndices;
	}

	public void setLoginClientIP(Integer accountIndex, String ipAddress) throws Exception {
		try {
			accountSvc.setLoginClientIP(accountIndex, ipAddress);
		} catch (OBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	public String getLoginClientIP(Integer accountIndex) throws Exception {
		String clientIp = "";
		try {
			clientIp = accountSvc.getLoginClientIP(accountIndex);
		} catch (OBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return clientIp;
	}

	public void setLastAliveTime(Integer accountIndex, Date currentTime) throws Exception {
		try {
			accountSvc.setLastAliveTime(accountIndex, currentTime);
		} catch (OBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

	}

	public Date getLastAliveTime(Integer accountIndex) throws Exception {

		Date currentTime;
		try {
			currentTime = accountSvc.getLastAliveTime(accountIndex);
		} catch (OBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return currentTime;

	}

	public boolean getIpCheck(Integer accountIndex, String ipAddress) throws Exception {
		log.debug("ipFilter: {}", ipAddress);
		boolean isFlag = false;
		isFlag = accountSvc.isIpFilter(accountIndex, ipAddress);
		return isFlag;
	}
}
