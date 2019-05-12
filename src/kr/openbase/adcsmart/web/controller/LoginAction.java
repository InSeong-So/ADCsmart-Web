package kr.openbase.adcsmart.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.Cookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.facade.LoginFacade;
import kr.openbase.adcsmart.web.facade.dto.AccountDto;
import kr.openbase.adcsmart.web.facade.system.ConfigSettingFacade;
import kr.openbase.adcsmart.web.util.OBDefineWeb;
import kr.openbase.adcsmart.web.util.OBMessageWeb;

@Controller
@Scope(value = "prototype")
public class LoginAction extends BaseAction {
	private transient Logger log = LoggerFactory.getLogger(LoginAction.class);

	@Autowired
	private LoginFacade accountFacade;

	@Autowired
	private ConfigSettingFacade envFacade;

	@Value("#{settings['site.isSDSSite']}")
	private boolean varIsSDSSite;// ftl에서 사용하는 변수임.

	@Value("#{settings['lang.code']}")
	private String langCode;//

	@Value("#{settings['account.isIpChk']}")
	private boolean ipChkFlag;

	private String id;
	private String password;
	private String idSaveFlag = "off"; // on/off
	private String passwordSaveFlag = "off"; // on/off
//	private String Logincheck = "off";			// on/off	
//	private String strLangCode="ko_KR";
	private Boolean isPasswordReset = false;
	private String isPasswordResetWarningMsg = "";
	private String clientIpCheck = "";
	private Date aliveTimeCheck;
	private String msg2Show = "";

	private void updateLoginCheckTime(Integer accountIndex) throws Exception {
		Calendar cal = Calendar.getInstance();
		try {
			new LoginFacade().setLastAliveTime(accountIndex, cal.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

//	private String base64Decoder(String data)
//	{
//		String passwd=null;
//		try
//		{
//			BASE64Decoder decoder = new BASE64Decoder();
//			String decPwd;
//		 
//			byte[] CookieString2;
//
//			decPwd = new StringBuffer(data).toString();
//
//			CookieString2 = decoder.decodeBuffer(decPwd);
//
//			passwd = new String(CookieString2, "UTF-8");
//			return passwd;
//		}
//		catch(Exception e)
//		{
//			System.err.println("[ssologin decript Error] Exception: "+e);
//		}
//		return passwd;
//	}

	public String login() throws Exception {
		log.debug("login: {}", toString());
		addCookies();
		isSuccessful = false;
		try {
			AccountDto account = accountFacade.getAccount(id);
			log.debug("{}", account);
			if (account == null) {
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_NAME_CHECK);
				return SUCCESS;
			}

//			password = base64Decoder(password);// 입력된 passwd는 base64로 encoding된 상태임. 이를 decoding하여야 함.
			String encPassword = accountFacade.encryptPassword(password);
			log.debug("{}", encPassword);
//			
			// 패스워드가 틀렸을 때.
			if (!encPassword.equals(account.getPassword())) {
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_PWD_CHECK);
				return SUCCESS;
			}

			// 로그인 3회 실패시 처리.
			if (account.getLoginFailCount() > 3) {
				message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_LOGIN_FAIL);
				return SUCCESS;
			}

			// 사용 기간 검사. 사용 기간 설정이 되어 있는 경우에만 검사함.
			if (account.getStartTime() != null && account.getEndTime() != null) // 계정 사용기한 check, startTime 과 EndTime 모두
																				// null 이 아닐때 동작
			{
				Calendar nowTime = Calendar.getInstance();
				long LnowTime = nowTime.getTime().getTime();
				long startTime = account.getLstartTime();
				long endTime = account.getLendTime();
				if (LnowTime > endTime || LnowTime < startTime) {// 사용기간이 만료된 경우.
					message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_EXPIRE_ACCOUNT); // 사용기한이 아니거나 만료된 계정입니다.;
					return SUCCESS;
				}
			}

			// 중복 로그인 설정이 되었을 경우.
			if (envFacade.getEnvLoginAccess() != 0) // 중복로그인 on / off 부분 비교값을 off로 할 시엔 동작
			{
				clientIpCheck = accountFacade.getLoginClientIP(account.getIndex());
				log.debug("{}", clientIpCheck);

				if (clientIpCheck != null && !clientIpCheck.isEmpty()
						&& !clientIpCheck.equals(request.getRemoteAddr())) {// ClientIP가 비어있지 않거나, 같은 IP가 접속중이지 않을 경우에
																			// 중복 로그인을 검사함.
																			// 현재 시간과 alive 시간을 계산하는 부분
					aliveTimeCheck = accountFacade.getLastAliveTime(account.getIndex());
					Calendar nowTime = Calendar.getInstance();
					Calendar aliveTime = Calendar.getInstance();

					aliveTime.setTime(aliveTimeCheck);

					log.debug("{}", aliveTimeCheck);
					long diffMillis = Math.abs(nowTime.getTimeInMillis() - aliveTime.getTimeInMillis());
					long second = diffMillis / 1000;

					if (second < OBDefineWeb.LOGIN_ALIVE_TIMEOUT) {
						// login fail message
//						message = "동일 계정의 동시 접근은 금지되어 있습니다. 아이디 " + account.getId() + 
//								" 은(는)  " + clientIpCheck + "에서 접근하여 사용되고 있습니다. 확인 후 재 시도하시기 바랍니다.";	

						message = String.format(OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SAME_ACCOUNT_CANNOT_ACCESS),
								account.getId(), clientIpCheck);
						return SUCCESS;
					}
				}
				// updateLoginCheckTime(account.getIndex());
			}

			// 모든 로그인 검사 조건을 만족한 경우.
			accountFacade.setLoginClientIP(account.getIndex(), request.getRemoteAddr());
//			ipChkFlag = accountFacade.getIpCheck(account.getIndex(), request.getRemoteAddr());
			isSuccessful = true;
			fillSessionInfo(account);

			isPasswordReset = account.getIsPasswordReset();
			if (isPasswordReset == false && OBCommon.getProperties("password.change.popup").equals("enable")) {
				int maxExpiredDays = getPasswdExpiredDays();// 패스워드 사용 기간 만료 여부 확인함.
				isPasswordReset = isPasswdExpired(account.getChangedTime(), maxExpiredDays);
			}

			accountFacade.login(account.getIndex());
			logFacade.logLoginSuccess(session.getAccountIndex(), session.getClientIp(), session.getAccountId());
			updateLoginCheckTime(account.getIndex());
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			message = OBMessageWeb.getMessageWeb(OBMessageWeb.MSG_SYSTEM_ERR);
		}

		return SUCCESS;
	}

	private int getPasswdExpiredDays() {
		int maxPasswdExpired = 90;// 기본 90일
		String proertiesValue = OBCommon.getProperties("password.use.period.day");
		if (proertiesValue != null && proertiesValue.length() > 0) {
			try {
				int max = Integer.parseInt(proertiesValue);
				if (max <= 30)
					maxPasswdExpired = 30;
				return maxPasswdExpired;
			} catch (Exception e) {
				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
						"Failed to get max passwd expired days. error: " + e.getMessage());
			}
		}
		return maxPasswdExpired;
	}

	private boolean isPasswdExpired(Timestamp lastModifiedTime, int expiredDays) {
		long currentTime = System.currentTimeMillis();
		if (lastModifiedTime == null) {
			return false;
		}
		if ((lastModifiedTime.getTime() + expiredDays * 24 * 3600 * 1000L) <= currentTime) {
			return true;
		}
		return false;
	}

	private String isPasswdRemainTime(Timestamp changedTime, int expiredDays, String warningMax) throws Exception {
		Date lastModifiedTime = new Date(changedTime.getTime());
		Date currentTime = new Date(System.currentTimeMillis());
		int remainTimeMsg = Integer.parseInt(warningMax);
		if (((lastModifiedTime.getTime() - currentTime.getTime()) / (24 * 3600 * 1000L)) == remainTimeMsg)
			return "현재 비밀번호 만료 " + remainTimeMsg + "일 전입니다. 비밀번호를 변경해주세요.";
		return "";
	}

	public String logout() throws Exception {
		log.debug("starting logLogoutSuccess.");
		try {
			logFacade.logLogoutSuccess(session.getAccountIndex(), session.getClientIp(), session.getAccountId());
			/* accountFacade.resetLoginClientIP(session.getAccountIndex()); */
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		log.debug("finished logLogoutSuccess.");
		session.invalidate();

		return SUCCESS;
	}

	public String loadMain() throws Exception {
		log.debug("isPasswordReset:{}", isPasswordReset);
		return SUCCESS;
	}

	private void addCookies() throws UnsupportedEncodingException {
		addCookie("id", id);
		addCookie("password", password);
		addCookie("idSaveFlag", idSaveFlag);
		addCookie("passwordSaveFlag", passwordSaveFlag);
	}

	private void fillSessionInfo(AccountDto account) {
		session.setAccountId(id);
		session.setAccountPassword(password);
		log.debug("RemoteAddr: {}", request.getRemoteAddr());
		session.setClientIp(request.getRemoteAddr());
		session.setAccountIndex(account.getIndex());
		Date occurTime = new Date();
		Timestamp time = new Timestamp(occurTime.getTime());
		session.setLoginTime(time);
		switch (account.getRoleId()) {
		case 1:
			session.setAccountRole("system");
			break;
		case 2:
			session.setAccountRole("config");
			break;
		case 4:
			session.setAccountRole("vsAdmin");
			break;
		case 5:
			session.setAccountRole("rsAdmin");
			break;
		default:
			session.setAccountRole("readOnly");
			break;
		}

//		System.out.println("session ClientIP : " + session.getClientIp());
//		System.out.println("account userIP : " + account.getUserIp());
//		System.out.println(session.getAccountRole());
//		if (session.getClientIp().equals(account.getUserIp()))
//		{
//		    session.setAccountRole("config");
//		}
//		System.out.println(session.getAccountRole());
	}

	private void addCookie(String name, String value) throws UnsupportedEncodingException {
		Cookie cookie = new Cookie(name, URLEncoder.encode(value, "UTF-8"));
		cookie.setMaxAge(60 * 60 * 24 * 365);
		response.addCookie(cookie);
	}

	public String retrieveCookies() throws Exception {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return SUCCESS;

		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("id"))
				id = URLDecoder.decode(cookie.getValue(), "UTF-8");
			else if (cookie.getName().equals("password"))
				password = URLDecoder.decode(cookie.getValue(), "UTF-8");
			else if (cookie.getName().equals("idSaveFlag"))
				idSaveFlag = URLDecoder.decode(cookie.getValue(), "UTF-8");
			else if (cookie.getName().equals("passwordSaveFlag"))
				passwordSaveFlag = URLDecoder.decode(cookie.getValue(), "UTF-8");
		}

		log.debug("retrieveCookies: {}", toString());
		return SUCCESS;
	}

	public String retrievePasswdResetWarningMsg() throws Exception {
		if (OBCommon.getProperties("password.change.popup").contains("enable")) {
			String accountId = session.getAccountId();
			AccountDto account = accountFacade.getAccount(accountId);
			// TODO. 만료일 N일 전 검사 기능 추가 필요.
			isPasswordResetWarningMsg = isPasswdRemainTime(account.getChangedTime(), getPasswdExpiredDays(),
					OBCommon.getProperties("password.warning.max"));
		} else {
			isPasswordResetWarningMsg = "";
		}

		return SUCCESS;
	}

	public String retrieveLangCode() throws Exception {
		return SUCCESS;
//		try
//		{
////		Locale locale = new Locale("en", "US");
////		Locale locale = new Locale("pt", "PT");
////
////		ResourceBundle labels = ResourceBundle.getBundle("conf.Messages", locale);
////		System.out.println(labels.getString("MSG_LOGIN_ID"));
////		 InputStream is = getClass().getResourceAsStream("/bundle/Messages_pt_PT.properties");
////		    Properties props = new Properties();
////		    //파일 InputStream을 Properties 객체로 읽어온다
////		    props.load(is);
////		    //파일에 key 에 값을 가져온다 = "/com/test/key" 
////		    props.get("key");   
//		return SUCCESS;
//		}
//		catch(Exception e)
//		{
//			throw e;
//		}
	}

	public String test() throws Exception {
		log.debug("test: " + toString());
		return SUCCESS;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIdSaveFlag() {
		return idSaveFlag;
	}

	public void setIdSaveFlag(String idSaveFlag) {
		this.idSaveFlag = idSaveFlag;
	}

	public String getPasswordSaveFlag() {
		return passwordSaveFlag;
	}

	public void setPasswordSaveFlag(String passwordSaveFlag) {
		this.passwordSaveFlag = passwordSaveFlag;
	}

	public Boolean getIsPasswordReset() {
		return isPasswordReset;
	}

	public void setIsPasswordReset(Boolean isPasswordReset) {
		this.isPasswordReset = isPasswordReset;
	}

	public boolean isVarIsSDSSite() {
		return varIsSDSSite;
	}

	public void setVarIsSDSSite(boolean varIsSDSSite) {
		this.varIsSDSSite = varIsSDSSite;
	}

	@Override
	public String getLangCode() {
		return langCode;
	}

	public void setlangCode(String langCode) {
		this.langCode = langCode;
	}

	@Override
	public boolean isIpChkFlag() {
		return ipChkFlag;
	}

	@Override
	public void setIpChkFlag(boolean ipChkFlag) {
		this.ipChkFlag = ipChkFlag;
	}

	public String getIsPasswordResetWarningMsg() {
		return isPasswordResetWarningMsg;
	}

	public void setIsPasswordResetWarningMsg(String isPasswordResetWarningMsg) {
		this.isPasswordResetWarningMsg = isPasswordResetWarningMsg;
	}

	public String getMsg2Show() {
		return msg2Show;
	}

	public void setMsg2Show(String msg2Show) {
		this.msg2Show = msg2Show;
	}

}
