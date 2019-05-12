package kr.openbase.adcsmart.web.facade;

import org.springframework.stereotype.Component;

import kr.openbase.adcsmart.service.OBSystemAudit;
import kr.openbase.adcsmart.service.impl.OBSystemAuditImpl;
import kr.openbase.adcsmart.service.utility.OBException;

@Component
public class LogFacade {
	private OBSystemAudit logMgmt;

	public LogFacade() {
		logMgmt = new OBSystemAuditImpl();
	}

//	public void logAdcAddSuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_ADC_ADD_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void logAdcModifySuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_ADC_SET_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void logAdcDelSuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_ADC_DEL_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void logAdcGroupAddSuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_ADCGROUP_ADD_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void logAdcGroupDelSuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_ADCGROUP_DEL_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void logVServerAddSuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_VSERVER_ADD_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void logVServerModifySuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_VSERVER_SET_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void logVServerDelSuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_VSERVER_DEL_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void logProfileAddSuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_PROFILE_ADD_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void logProfileModifySuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_PROFILE_SET_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void logProfileDelSuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_PROFILE_DEL_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void logAccountAddSuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_ACCOUNT_ADD_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void logAccountModifySuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_ACCOUNT_SET_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public void logAccountDelSuccess(Integer accountIndex, String clientIp, String extraMsg) {
//		try {
//			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_ACCOUNT_DEL_SUCCESS, extraMsg);
//		} catch (OBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public void logLoginSuccess(Integer accountIndex, String clientIp, String extraMsg) {
		try {
			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_LOGIN_SUCCESS_CODE, extraMsg);
		} catch (OBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void logLoginFail(Integer accountIndex, String clientIp, String extraMsg) {
		try {
			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_LOGIN_FAIL_CODE, extraMsg);
		} catch (OBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void logLogoutSuccess(Integer accountIndex, String clientIp, String extraMsg) {
		try {
			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_LOGOUT_SUCCESS_CODE, extraMsg);
		} catch (OBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void logLogoutFail(Integer accountIndex, String clientIp, String extraMsg) {
		try {
			logMgmt.writeLog(accountIndex, clientIp, OBSystemAudit.AUDIT_LOGOUT_FAIL_CODE, extraMsg);
		} catch (OBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
