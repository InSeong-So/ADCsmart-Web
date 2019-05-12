package kr.openbase.adcsmart.web.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.util.OBFileHandler;

/*
 * 코딩 가이드. 
 * 	1. public 함수의 Exception은 무조건 OBException만 throw 한다.
 *  2. public 함수내에서는 try-catch를 추가하면 catch로 OBException, Exception을 받는다.
 *  3. try-catch로 Exception을 받았을 경우에는 OBException으로 throw 한다. 
 *    참고: public String retrieveAdcGroupToAdcsMap() throws OBException
 *  4. private 함수는 OBException, Exception 두종류를 throw한다. 
 *    참고: private void prepareAdcGroupMapAndGroupToAdcsMap(String searchKey)
 *  5. 내부 처리 도중 사용자에게 메세지를 알릴 필요가 있을 경우에는 다음과 같이 두단계로 진행한다. 
 *    5.1 isSuccessful = false;
 *    5.2 message = OBMessageWeb.LOGIN_ALIVE_TIMEOUT;
 *    5.3. 참고: String addAdc() ....
 *
 */

@Controller
@Scope(value = "prototype")
public class securityNoticeAction extends BaseAction {
	private String content = "";
	private Boolean isShown = false;

	public static final String SECURITY_NOTICE_FILENAME = "/opt/adcsmart/cfg/security.cfg";

	public String loadConfigContent() throws OBException {
		isShown = false;
		String notice = OBFileHandler.toString(SECURITY_NOTICE_FILENAME);
		if (notice != null && !notice.isEmpty()) {
			isShown = true;
			content = notice;
		}
//		content = "이 시스템은 인가된 사용자만 접근, 사용해야 하며, 접속자의 모든 활동이 모니터링되고 있습니다. ";
//		content += "<br>부당한 방법으로 접속하거나 정보를 삭제, 변경, 유출하는 사용자는 관게법령에 따라 처벌을 받게 됩니다. ";
//		content += "<br><br>This System is strictly restricted to authorized users only. Any illegal access or use shall be punished with a related-law. ";
//		content += "<br><br>SAMSUNG SDS Inc.";

		return SUCCESS;
	}

	@Override
	public String toString() {
		return "securityNotice [content=" + content + ", isShown=" + isShown + "]";
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getIsShown() {
		return isShown;
	}

	public void setIsShown(Boolean isShown) {
		this.isShown = isShown;
	}
}
