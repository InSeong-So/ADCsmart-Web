package kr.openbase.adcsmart.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBAdcCheckResult;
import kr.openbase.adcsmart.service.dto.OBAdcConfigInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoLastAdcCheckTime;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonHandler;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBCLIParserAlteon;
import kr.openbase.adcsmart.service.impl.f5.CommonF5;
import kr.openbase.adcsmart.service.impl.f5.SystemF5;
import kr.openbase.adcsmart.service.impl.f5.handler.OBAdcF5Handler;
import kr.openbase.adcsmart.service.impl.f5.handler.OBCLIParserF5;
import kr.openbase.adcsmart.service.impl.pas.handler.OBAdcPASHandler;
import kr.openbase.adcsmart.service.impl.pas.handler.OBCLIParserPAS;
import kr.openbase.adcsmart.service.impl.pask.handler.OBAdcPASKHandler;
import kr.openbase.adcsmart.service.impl.pask.handler.OBCLIParserPASK;
import kr.openbase.adcsmart.service.snmp.alteon.OBSnmpAlteon;
import kr.openbase.adcsmart.service.snmp.f5.OBSnmpF5;
import kr.openbase.adcsmart.service.snmp.pas.OBSnmpPAS;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBNetwork;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBCheckAdcStatus {
	public static final int MAX_WAIT_TIME_MSEC = 1000;
	public static final int SSH_PORT_NUM = 22;
	public static final int HTTPS_PORT_NUM = 443;

	/**
	 * 현재 시간 기준으로 10분 사이의 검색 조건을 완성한다.
	 * 
	 * @param searchOption
	 * @param columnName
	 * @return
	 * @throws OBException
	 */
	private String makeTimeSqlText() throws OBException {
		String retVal = "";

		Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
		retVal = String.format(" %s <= %s ", "OCCUR_TIME", OBParser.sqlString(OBDateTime.now()));

		// start time
		Date startTime = new Date(now.getTime() - MAX_DIFF_TIME_SYSLOG);// 10분전 시간.

		retVal += String.format(" AND %s >= %s ", "OCCUR_TIME",
				OBParser.sqlString(OBDateTime.toString(new Timestamp(startTime.getTime()))));

		return retVal;
	}

	public ArrayList<OBDtoLastAdcCheckTime> getLastAdcCheckTime(Integer adcIndex) throws OBException {
		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoLastAdcCheckTime> retVal = new ArrayList<OBDtoLastAdcCheckTime>();
		String sqlText = "";
		try {
			db.openDB();
			sqlText = String.format(" SELECT PORT_OPEN_TIME, LOGIN_TIME, VERSION_TIME,    \n"
					+ " REVERSE_PORT_TIME, SNMP_TIME, SYSLOG_TIME           \n"
					+ " FROM TMP_LAST_ADC_CHECK_TIME                        \n"
					+ " WHERE ADC_INDEX=%d                                  \n", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				Timestamp checkTime = null;
				OBDtoLastAdcCheckTime obj = null;
				checkTime = db.getTimestamp(rs, "PORT_OPEN_TIME");
				obj = new OBDtoLastAdcCheckTime();
				obj.setCheckID(OBDtoLastAdcCheckTime.CHECK_ID_PORTOPEN);
				obj.setCheckTime(checkTime);
				retVal.add(obj);

				checkTime = db.getTimestamp(rs, "LOGIN_TIME");
				obj = new OBDtoLastAdcCheckTime();
				obj.setCheckID(OBDtoLastAdcCheckTime.CHECK_ID_LOGIN);
				obj.setCheckTime(checkTime);
				retVal.add(obj);

				checkTime = db.getTimestamp(rs, "VERSION_TIME");
				obj = new OBDtoLastAdcCheckTime();
				obj.setCheckID(OBDtoLastAdcCheckTime.CHECK_ID_VERSION);
				obj.setCheckTime(checkTime);
				retVal.add(obj);

				checkTime = db.getTimestamp(rs, "REVERSE_PORT_TIME");
				obj = new OBDtoLastAdcCheckTime();
				obj.setCheckID(OBDtoLastAdcCheckTime.CHECK_ID_PORTREVERSE);
				obj.setCheckTime(checkTime);
				retVal.add(obj);

				checkTime = db.getTimestamp(rs, "SNMP_TIME");
				obj = new OBDtoLastAdcCheckTime();
				obj.setCheckID(OBDtoLastAdcCheckTime.CHECK_ID_SNMP);
				obj.setCheckTime(checkTime);
				retVal.add(obj);

				checkTime = db.getTimestamp(rs, "SYSLOG_TIME");
				obj = new OBDtoLastAdcCheckTime();
				obj.setCheckID(OBDtoLastAdcCheckTime.CHECK_ID_SYSLOG);
				obj.setCheckTime(checkTime);
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		return retVal;
	}

	public ArrayList<OBDtoLastAdcCheckTime> getLastAdcCheckTime(Integer adcIndex, OBDatabase db) throws OBException {
		ArrayList<OBDtoLastAdcCheckTime> retVal = new ArrayList<OBDtoLastAdcCheckTime>();
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT PORT_OPEN_TIME, LOGIN_TIME, VERSION_TIME, 	\n"
					+ " REVERSE_PORT_TIME, SNMP_TIME, SYSLOG_TIME 			\n"
					+ " FROM TMP_LAST_ADC_CHECK_TIME 						\n"
					+ " WHERE ADC_INDEX=%d 									\n", adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				Timestamp checkTime = null;
				OBDtoLastAdcCheckTime obj = null;
				checkTime = db.getTimestamp(rs, "PORT_OPEN_TIME");
				obj = new OBDtoLastAdcCheckTime();
				obj.setCheckID(OBDtoLastAdcCheckTime.CHECK_ID_PORTOPEN);
				obj.setCheckTime(checkTime);
				retVal.add(obj);

				checkTime = db.getTimestamp(rs, "LOGIN_TIME");
				obj = new OBDtoLastAdcCheckTime();
				obj.setCheckID(OBDtoLastAdcCheckTime.CHECK_ID_LOGIN);
				obj.setCheckTime(checkTime);
				retVal.add(obj);

				checkTime = db.getTimestamp(rs, "VERSION_TIME");
				obj = new OBDtoLastAdcCheckTime();
				obj.setCheckID(OBDtoLastAdcCheckTime.CHECK_ID_VERSION);
				obj.setCheckTime(checkTime);
				retVal.add(obj);

				checkTime = db.getTimestamp(rs, "REVERSE_PORT_TIME");
				obj = new OBDtoLastAdcCheckTime();
				obj.setCheckID(OBDtoLastAdcCheckTime.CHECK_ID_PORTREVERSE);
				obj.setCheckTime(checkTime);
				retVal.add(obj);

				checkTime = db.getTimestamp(rs, "SNMP_TIME");
				obj = new OBDtoLastAdcCheckTime();
				obj.setCheckID(OBDtoLastAdcCheckTime.CHECK_ID_SNMP);
				obj.setCheckTime(checkTime);
				retVal.add(obj);

				checkTime = db.getTimestamp(rs, "SYSLOG_TIME");
				obj = new OBDtoLastAdcCheckTime();
				obj.setCheckID(OBDtoLastAdcCheckTime.CHECK_ID_SYSLOG);
				obj.setCheckTime(checkTime);
				retVal.add(obj);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
////			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(10);
//			ArrayList<OBAdcCheckResult> list = new OBCheckAdcStatus().checkADCStatusAll(1, db);
//
//			for(OBAdcCheckResult info:list)
//				System.out.println(info);
//
//			db.closeDB();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	public ArrayList<OBAdcCheckResult> checkADCStatusAll(Integer adcIndex, OBDatabase db) throws OBException
//	{
//		OBDtoAdcInfo adcInfo = null;
//		ArrayList<OBAdcCheckResult> retVal = new ArrayList<OBAdcCheckResult>();
//		try
//		{
//			//adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex, db);
//		    adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
//		}
//		catch(OBException e)
//		{// 검사에 실패한 경우임.
//			throw e;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//
//		// port open 테스트.
//		OBAdcCheckResult result = null;
//		try
//		{
//			Integer checkItem = OBAdcCheckResult.CHECK_ID_PORTOPEN;
//			result = checkADCStatus(adcInfo, checkItem, false, db);
//			retVal.add(result);
//			
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_PORTOPEN, db);
//		}
//		catch(OBException e)
//		{// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
//			throw e;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		
//		if(result.getCheckID()==OBAdcCheckResult.CHECK_ID_PORTOPEN 
//				&& result.getStatus().intValue()!=OBAdcCheckResult.CHECK_STATUS_OK)
//		{// 포트 접속에 실패한 경우임. 더이상의 하위 테스트는 의미가 없음. 나머지는 초기상태로 설정함.
//			OBAdcCheckResult obj = null;
//			obj = new OBAdcCheckResult();
//			obj.setCheckID(OBAdcCheckResult.CHECK_ID_LOGIN);
//			obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
//			retVal.add(obj);
//			obj = new OBAdcCheckResult();
//			obj.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
//			obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
//			retVal.add(obj);		
//			if(adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
//			{//
//				obj = new OBAdcCheckResult();
//				obj.setCheckID(OBAdcCheckResult.CHECK_ID_PORTREVERSE);
//				obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
//				retVal.add(obj);
//			}
//			obj = new OBAdcCheckResult();
//			obj.setCheckID(OBAdcCheckResult.CHECK_ID_SNMP);
//			obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
//			retVal.add(obj);
//			obj = new OBAdcCheckResult();
//			obj.setCheckID(OBAdcCheckResult.CHECK_ID_SYSLOG);
//			obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
//			retVal.add(obj);
//			return retVal;
//		}
//		
//		// 로그인 테스트
//		try
//		{
//			Integer checkItem = OBAdcCheckResult.CHECK_ID_LOGIN;
//			result = checkADCStatus(adcInfo, checkItem, false, db);
//			retVal.add(result);
//			
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_LOGIN, db);
//		}
//		catch(OBException e)
//		{// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
//			throw e;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		
//		//sleep은 필수 과정이 아니나, Alteon에서 이전 로그인 테스트 connection 종료가 늦게 되어, 허용 connection 수를 초과하는 현상이 있어서 넣음. 
//		try
//		{
//			Thread.sleep(500);
//		}
//		catch(InterruptedException e1)
//		{
//		}
//		
//		// 버전 검사
//		try
//		{
//			Integer checkItem = OBAdcCheckResult.CHECK_ID_VERSION;
//			result = checkADCStatus(adcInfo, checkItem, false, db);
//			retVal.add(result);
//		}
//		catch(OBException e)
//		{// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
//			throw e;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		
//		if(result.getCheckID()==OBAdcCheckResult.CHECK_ID_VERSION 
//				&& result.getStatus().intValue()!=OBAdcCheckResult.CHECK_STATUS_OK)
//		{// 포트 접속에 실패한 경우임. 더이상의 하위 테스트는 의미가 없음.
//			OBAdcCheckResult obj = null;
//			if(adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
//			{//
//				obj = new OBAdcCheckResult();
//				obj.setCheckID(OBAdcCheckResult.CHECK_ID_PORTREVERSE);
//				obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
//				retVal.add(obj);
//			}
//			obj = new OBAdcCheckResult();
//			obj.setCheckID(OBAdcCheckResult.CHECK_ID_SNMP);
//			obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
//			retVal.add(obj);
//			obj = new OBAdcCheckResult();
//			obj.setCheckID(OBAdcCheckResult.CHECK_ID_SYSLOG);
//			obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
//			retVal.add(obj);
//			return retVal;
//		}
//		
//		// 체크 시간을 기록함.
//		updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_VERSION, db);
//		
//		// reverse 포트 오픈 테스트
//		if(adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
//		{// alteon만 진행한다.
//			try
//			{
//				Integer checkItem = OBAdcCheckResult.CHECK_ID_PORTREVERSE;
//				result = checkADCStatus(adcInfo, checkItem, false, db);
//				retVal.add(result);
//
//				// 체크 시간을 기록함.
//				updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_PORTREVERSE, db);
//			}
//			catch(OBException e)
//			{// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
//				throw e;
//			}
//			catch(Exception e)
//			{// 일반적인 프로그램 오류.
//				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//			}
//		}		
//		// snmp 쿼리 테스트
//		try
//		{
//			Integer checkItem = OBAdcCheckResult.CHECK_ID_SNMP;
//			result = checkADCStatus(adcInfo, checkItem, false, db);
//			retVal.add(result);
//
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_SNMP, db);
//		}
//		catch(OBException e)
//		{// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
//			throw e;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		
//		// syslog 수신 테스트.
//		try
//		{
//			Integer checkItem = OBAdcCheckResult.CHECK_ID_SYSLOG;
//			result = checkADCStatus(adcInfo, checkItem, false, db);
//			retVal.add(result);
//		}
//		catch(OBException e)
//		{// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
//			throw e;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public ArrayList<OBAdcCheckResult> checkADCStatusAll(Integer adcIndex) throws OBException {
		OBDtoAdcInfo adcInfo = null;
		ArrayList<OBAdcCheckResult> retVal = new ArrayList<OBAdcCheckResult>();
		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		} catch (OBException e) {// 검사에 실패한 경우임.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// port open 테스트.
		OBAdcCheckResult result = null;
		try {
			Integer checkItem = OBAdcCheckResult.CHECK_ID_PORTOPEN;
			result = checkADCStatus(adcInfo, checkItem, false);
			retVal.add(result);

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_PORTOPEN);
		} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		if (result.getCheckID() == OBAdcCheckResult.CHECK_ID_PORTOPEN
				&& result.getStatus().intValue() != OBAdcCheckResult.CHECK_STATUS_OK) {// 포트 접속에 실패한 경우임. 더이상의 하위 테스트는
																						// 의미가 없음. 나머지는 초기상태로 설정함.
			OBAdcCheckResult obj = null;
			obj = new OBAdcCheckResult();
			obj.setCheckID(OBAdcCheckResult.CHECK_ID_LOGIN);
			obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
			retVal.add(obj);
			obj = new OBAdcCheckResult();
			obj.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
			obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
			retVal.add(obj);
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {//
				obj = new OBAdcCheckResult();
				obj.setCheckID(OBAdcCheckResult.CHECK_ID_PORTREVERSE);
				obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
				retVal.add(obj);
			}
			obj = new OBAdcCheckResult();
			obj.setCheckID(OBAdcCheckResult.CHECK_ID_SNMP);
			obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
			retVal.add(obj);
			obj = new OBAdcCheckResult();
			obj.setCheckID(OBAdcCheckResult.CHECK_ID_SYSLOG);
			obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
			retVal.add(obj);
			return retVal;
		}

		if (adcInfo.getAdcType() != OBDefine.ADC_TYPE_ALTEON) // 알테온이 아닐때 로그인 테스트 동작.
		{
			// a 로그인 테스트
			try {
				Integer checkItem = OBAdcCheckResult.CHECK_ID_LOGIN;
				result = checkADCStatus(adcInfo, checkItem, false);
				retVal.add(result);

				// a체크 시간을 기록함.
				updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_LOGIN);
			} catch (OBException e) {// a검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
				throw e;
			} catch (Exception e) {// a일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}

			// sleep은 필수 과정이 아니나, Alteon에서 이전 로그인 테스트 connection 종료가 늦게 되어, 허용 connection 수를
			// 초과하는 현상이 있어서 넣음.
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
			}

			// 버전 검사
			try {
				Integer checkItem = OBAdcCheckResult.CHECK_ID_VERSION;
				result = checkADCStatus(adcInfo, checkItem, false);
				retVal.add(result);
			} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
				throw e;
			} catch (Exception e) {// 일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}

			if (result.getCheckID() == OBAdcCheckResult.CHECK_ID_VERSION
					&& result.getStatus().intValue() != OBAdcCheckResult.CHECK_STATUS_OK) {// 버전 검사에 실패한 경우임. 더이상의 하위
																							// 테스트는 의미가 없음.
				OBAdcCheckResult obj = null;
				if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {//
					obj = new OBAdcCheckResult();
					obj.setCheckID(OBAdcCheckResult.CHECK_ID_PORTREVERSE);
					obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
					retVal.add(obj);
				}
				obj = new OBAdcCheckResult();
				obj.setCheckID(OBAdcCheckResult.CHECK_ID_SNMP);
				obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
				retVal.add(obj);
				obj = new OBAdcCheckResult();
				obj.setCheckID(OBAdcCheckResult.CHECK_ID_SYSLOG);
				obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
				retVal.add(obj);
				return retVal;
			}

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_VERSION);
		} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON
				&& adcInfo.getOpMode() != OBDefine.OP_MODE_MONITORING) { // 알테온일때는 운영모드가 모니터링 모드가 아닐때 로그인 테스트 동작
																			// a 로그인 테스트
			try {
				Integer checkItem = OBAdcCheckResult.CHECK_ID_LOGIN;
				result = checkADCStatus(adcInfo, checkItem, false);
				retVal.add(result);

				// a체크 시간을 기록함.
				updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_LOGIN);
			} catch (OBException e) {// a검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
				throw e;
			} catch (Exception e) {// a일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}

			// sleep은 필수 과정이 아니나, Alteon에서 이전 로그인 테스트 connection 종료가 늦게 되어, 허용 connection 수를
			// 초과하는 현상이 있어서 넣음.
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
			}

			// 버전 검사
			try {
				Integer checkItem = OBAdcCheckResult.CHECK_ID_VERSION;
				result = checkADCStatus(adcInfo, checkItem, false);
				retVal.add(result);
			} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
				throw e;
			} catch (Exception e) {// 일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}

			if (result.getCheckID() == OBAdcCheckResult.CHECK_ID_VERSION
					&& result.getStatus().intValue() != OBAdcCheckResult.CHECK_STATUS_OK) {// 버전 검사에 실패한 경우임. 더이상의 하위
																							// 테스트는 의미가 없음.
				OBAdcCheckResult obj = null;
				if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {//
					obj = new OBAdcCheckResult();
					obj.setCheckID(OBAdcCheckResult.CHECK_ID_PORTREVERSE);
					obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
					retVal.add(obj);
				}
				obj = new OBAdcCheckResult();
				obj.setCheckID(OBAdcCheckResult.CHECK_ID_SNMP);
				obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
				retVal.add(obj);
				obj = new OBAdcCheckResult();
				obj.setCheckID(OBAdcCheckResult.CHECK_ID_SYSLOG);
				obj.setStatus(OBAdcCheckResult.CHECK_STATUS_INIT);
				retVal.add(obj);
				return retVal;
			}

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_VERSION);
		}

		if (adcInfo.getOpMode() == OBDefine.OP_MODE_MONITORING_FAULT) // 운영모드가 모니터링+ 설정 + 진단 모드일때
		{
			// reverse 포트 오픈 테스트
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {// alteon만 진행한다.
				try {
					Integer checkItem = OBAdcCheckResult.CHECK_ID_PORTREVERSE;
					result = checkADCStatus(adcInfo, checkItem, false);
					retVal.add(result);

					// a체크 시간을 기록함.
					updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_PORTREVERSE);
				} catch (OBException e) {// a검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
					throw e;
				} catch (Exception e) {// a 일반적인 프로그램 오류.
					throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
				}
			}
		}

		// snmp 쿼리 테스트
		try {
			Integer checkItem = OBAdcCheckResult.CHECK_ID_SNMP;
			result = checkADCStatus(adcInfo, checkItem, false);
			retVal.add(result);

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_SNMP);
		} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		if (adcInfo.getOpMode() != OBDefine.OP_MODE_MONITORING) // 운영모드가 모니터링 모드가 아닐때
		{
			// syslog 수신 테스트.
			try {
				Integer checkItem = OBAdcCheckResult.CHECK_ID_SYSLOG;
				result = checkADCStatus(adcInfo, checkItem, false);
				retVal.add(result);
			} catch (OBException e) {// a. 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
				throw e;
			} catch (Exception e) {// a. 일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}
		}

		return retVal;
	}

	// 운영모드에서 모니터링 모드일때 연결 가능한지 검사
	public boolean checkADCStatusMonitoring(Integer adcIndex, boolean isNewADC) throws OBException {
		boolean retVal = false;
		int checkResult = 0;
		OBDtoAdcInfo adcInfo = null;
		// ArrayList<OBAdcCheckResult> retVal = new ArrayList<OBAdcCheckResult>();
		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		} catch (OBException e) {// 검사에 실패한 경우임.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// port open 테스트.
		OBAdcCheckResult result = null;
		try {
			Integer checkItem = OBAdcCheckResult.CHECK_ID_PORTOPEN;
			result = checkADCStatus(adcInfo, checkItem, isNewADC);

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_PORTOPEN);

			if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_OK) {
				checkResult = checkResult + 1;
			}

			if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_FAIL)// 포트 검사 실패시 더 이상 연결테스트는 진행하지 않는다.
			{
				return false;
			}
		} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		if (adcInfo.getAdcType() != OBDefine.ADC_TYPE_ALTEON) {
			// 로그인 테스트
			try {
				Integer checkItem = OBAdcCheckResult.CHECK_ID_LOGIN;
				result = checkADCStatus(adcInfo, checkItem, isNewADC);
				if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_OK) {
					checkResult = checkResult + 1;
				}
				// 체크 시간을 기록함.
				updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_LOGIN);
			} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
				throw e;
			} catch (Exception e) {// 일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}

			// sleep은 필수 과정이 아니나, Alteon에서 이전 로그인 테스트 connection 종료가 늦게 되어, 허용 connection 수를
			// 초과하는 현상이 있어서 넣음.
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
			}
		}

		// snmp 쿼리 테스트
		try {
			Integer checkItem = OBAdcCheckResult.CHECK_ID_SNMP;
			result = checkADCStatus(adcInfo, checkItem, isNewADC);
			if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_OK) {
				checkResult = checkResult + 1;
			}
			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_SNMP);
		} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		if (adcInfo.getAdcType() != OBDefine.ADC_TYPE_ALTEON) {
			if (checkResult == 3)
				retVal = true;
		} else {
			if (checkResult == 2)
				retVal = true;
		}

		return retVal;
	}

	// 운영모드에서 모니터링 + 설정 모드일때 연결 가능한지 검사
	public boolean checkADCStatusMonitoringSet(Integer adcIndex, boolean isNewADC) throws OBException {
		boolean retVal = false;
		int checkResult = 0;
		OBDtoAdcInfo adcInfo = null;
		// ArrayList<OBAdcCheckResult> retVal = new ArrayList<OBAdcCheckResult>();
		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		} catch (OBException e) {// 검사에 실패한 경우임.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// port open 테스트.
		OBAdcCheckResult result = null;
		try {
			Integer checkItem = OBAdcCheckResult.CHECK_ID_PORTOPEN;
			result = checkADCStatus(adcInfo, checkItem, isNewADC);

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_PORTOPEN);

			if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_OK) {
				checkResult = checkResult + 1;
			}

			if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_FAIL)// 포트 검사 실패시 더 이상 연결테스트는 진행하지 않는다.
			{
				return false;
			}
		} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// 로그인 테스트
		try {
			Integer checkItem = OBAdcCheckResult.CHECK_ID_LOGIN;
			result = checkADCStatus(adcInfo, checkItem, isNewADC);
			if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_OK) {
				checkResult = checkResult + 1;
			}

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_LOGIN);
		} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// sleep은 필수 과정이 아니나, Alteon에서 이전 로그인 테스트 connection 종료가 늦게 되어, 허용 connection 수를
		// 초과하는 현상이 있어서 넣음.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
		}

		// snmp 쿼리 테스트
		try {
			Integer checkItem = OBAdcCheckResult.CHECK_ID_SNMP;
			result = checkADCStatus(adcInfo, checkItem, isNewADC);
			if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_OK) {
				checkResult = checkResult + 1;
			}

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_SNMP);
		} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// syslog 수신 테스트.
		try {
			Integer checkItem = OBAdcCheckResult.CHECK_ID_SYSLOG;
			result = checkADCStatus(adcInfo, checkItem, isNewADC);
			if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_OK) {
				checkResult = checkResult + 1;
			}
		} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		if (checkResult == 4)
			retVal = true;

		return retVal;
	}

	// 운영모드에서 모니터링 + 설정 + 진단 모드일때 연결 가능한지 검사
	public boolean checkADCStatusMonitoringFault(Integer adcIndex, boolean isNewADC) throws OBException {
		boolean retVal = false;
		int checkResult = 0;
		OBDtoAdcInfo adcInfo = null;
		// ArrayList<OBAdcCheckResult> retVal = new ArrayList<OBAdcCheckResult>();
		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
		} catch (OBException e) {// 검사에 실패한 경우임.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// port open 테스트.
		OBAdcCheckResult result = null;
		try {
			Integer checkItem = OBAdcCheckResult.CHECK_ID_PORTOPEN;
			result = checkADCStatus(adcInfo, checkItem, isNewADC);

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_PORTOPEN);

			if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_OK) {
				checkResult = checkResult + 1;
			}

			if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_FAIL)// 포트 검사 실패시 더 이상 연결테스트는 진행하지 않는다.
			{
				return false;
			}
		} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// 로그인 테스트
		try {
			Integer checkItem = OBAdcCheckResult.CHECK_ID_LOGIN;
			result = checkADCStatus(adcInfo, checkItem, isNewADC);
			if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_OK) {
				checkResult = checkResult + 1;
			}

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_LOGIN);
		} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// sleep은 필수 과정이 아니나, Alteon에서 이전 로그인 테스트 connection 종료가 늦게 되어, 허용 connection 수를
		// 초과하는 현상이 있어서 넣음.
		try {
			Thread.sleep(500);
		} catch (InterruptedException e1) {
		}

		// reverse 포트 오픈 테스트
		if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {// alteon만 진행한다.
			try {
				Integer checkItem = OBAdcCheckResult.CHECK_ID_PORTREVERSE;
				result = checkADCStatus(adcInfo, checkItem, isNewADC);
				if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_OK) {
					checkResult = checkResult + 1;
				}

				// 체크 시간을 기록함.
				updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_PORTREVERSE);
			} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
				throw e;
			} catch (Exception e) {// 일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}
		}

		// snmp 쿼리 테스트
		try {
			Integer checkItem = OBAdcCheckResult.CHECK_ID_SNMP;
			result = checkADCStatus(adcInfo, checkItem, isNewADC);
			if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_OK) {
				checkResult = checkResult + 1;
			}

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcIndex, OBAdcCheckResult.CHECK_ID_SNMP);
		} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// syslog 수신 테스트.
		try {
			Integer checkItem = OBAdcCheckResult.CHECK_ID_SYSLOG;
			result = checkADCStatus(adcInfo, checkItem, isNewADC);
			if (result.getStatus() == OBAdcCheckResult.CHECK_STATUS_OK) {
				checkResult = checkResult + 1;
			}
		} catch (OBException e) {// 검사에 실패한 경우임. 포트 검사에 실패한 경우임. 더이상은 무의미함.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
			if (checkResult == 5)
				retVal = true;
		} else {
			if (checkResult == 4)
				retVal = true;
		}
		return retVal;
	}

	public ArrayList<OBAdcConfigInfo> checkAdcConfigInfo(Integer adcIndex, OBDatabase db) throws OBException {
		ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>();

		OBDtoAdcInfo adcInfo = null;
		try {
			// adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex, db);
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			if (adcInfo == null)
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid adcIndex:" + adcIndex);
		} catch (OBException e) {// 검사에 실패한 경우임.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		try {
			switch (adcInfo.getAdcType()) {
			case OBDefine.ADC_TYPE_ALTEON:
				retVal = checkAdcConfigInfoAlteon(adcInfo, db);
				break;
			case OBDefine.ADC_TYPE_F5:
				retVal = checkAdcConfigInfoF5(adcInfo, db);
				break;
			case OBDefine.ADC_TYPE_PIOLINK_PAS:
				retVal = checkAdcConfigInfoPAS(adcInfo, db);
				break;
			case OBDefine.ADC_TYPE_PIOLINK_PASK:
				retVal = checkAdcConfigInfoPASK(adcInfo, db);
				break;
			case OBDefine.ADC_TYPE_PIOLINK_UNKNOWN:
				retVal = checkAdcConfigInfoPASUnknown(adcInfo, db);// 로그인의 경우에 PAS와 동일한 절차로 진행한다.
				break;
			default:
				throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return retVal;
	}

	public ArrayList<OBAdcConfigInfo> checkAdcConfigInfo(Integer adcIndex) throws OBException // dbcp 추가
	{
		ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>();

		OBDtoAdcInfo adcInfo = null;
		try {
			adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			if (adcInfo == null)
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "invalid adcIndex:" + adcIndex);
		} catch (OBException e) {// 검사에 실패한 경우임.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		try {
			switch (adcInfo.getAdcType()) {
			case OBDefine.ADC_TYPE_ALTEON:
				retVal = checkAdcConfigInfoAlteon(adcInfo);
				break;
			case OBDefine.ADC_TYPE_F5:
				retVal = checkAdcConfigInfoF5(adcInfo);
				break;
			case OBDefine.ADC_TYPE_PIOLINK_PAS:
				retVal = checkAdcConfigInfoPAS(adcInfo);
				break;
			case OBDefine.ADC_TYPE_PIOLINK_PASK:
				retVal = checkAdcConfigInfoPASK(adcInfo);
				break;
			case OBDefine.ADC_TYPE_PIOLINK_UNKNOWN:
				retVal = checkAdcConfigInfoPASUnknown(adcInfo);// 로그인의 경우에 PAS와 동일한 절차로 진행한다.
				break;
			default:
				throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return retVal;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<OBAdcConfigInfo> checkAdcConfigInfoAlteon(OBDtoAdcInfo adcInfo) throws OBException // dbcp 적용
	{
		if (adcInfo.getOpMode() == OBDefine.OP_MODE_MONITORING) {
			ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
					makeDefaultAdcConfigInfoList(adcInfo).values());
			Collections.sort(retVal, new CheckIDCompare());
			return retVal;
		}

		HashMap<Integer, OBAdcConfigInfo> configMap = makeDefaultAdcConfigInfoList(adcInfo);
		OBAdcAlteonHandler handler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());

		try {
			handler.login();
		} catch (OBExceptionUnreachable e) {// 로그인 실패한 경우임.
			ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
					makeDefaultAdcConfigInfoList(adcInfo).values());
			Collections.sort(retVal, new CheckIDCompare());
			return retVal;
		} catch (OBExceptionLogin e) {// 로그인 실패한 경우임.
			ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
					makeDefaultAdcConfigInfoList(adcInfo).values());
			Collections.sort(retVal, new CheckIDCompare());
			handler.disconnect();
			return retVal;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// vstat 상태 검사.
		try {
			String vstat = handler.cmndCfgSlbVstatCur();
			OBCLIParserAlteon parser = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
			Integer state = parser.parseVstatState(vstat);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_VSTAT);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_VSTAT). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (state.intValue() == OBDefine.STATE_DISABLE) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(CONFIG_ENALBED);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// snmp enabled 검사. -v2, v3 상관없이 검사
		String snmpCfgText = "";
		try {
			snmpCfgText = handler.cmndCfgSnmp();
			OBCLIParserAlteon parser = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
			String state = parser.parseSnmpState(snmpCfgText); // Current v1/v2 access disabled
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (state.isEmpty()) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				if (adcInfo.getSnmpInfo().getVersion() == 3) {
					cfgInfo.setAdcInfo(state + "\n" + CONFIG_SNMP_V3_ENALBED);
				} else {
					cfgInfo.setAdcInfo(state);
				}
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		if (adcInfo.getSnmpInfo().getVersion() == 3) {
			// v3 SNMP 검사 (snmp user)
			// snmp CONFIG_ID_SNMP_USER
			try {
				OBCLIParserAlteon parser = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
//              String state = parser.parseSnmpUser(snmpCfgText);
				String state = parser.parseSnmpUsm(snmpCfgText);
				OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_USER);
				if (cfgInfo == null) {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_USER). callstack:%s",
									new OBUtility().getStackTrace()));
				}
				String snmpUser = "name " + adcInfo.getSnmpInfo().getSecurityName() + ", auth "
						+ adcInfo.getSnmpInfo().getAuthProto() + ", privacy " + adcInfo.getSnmpInfo().getPrivProto();
				// System.out.println(snmpUser);
				if (state.indexOf(snmpUser) == -1) {
					cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
					cfgInfo.setAdcInfo(state);
				} else {
					cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
					cfgInfo.setAdcInfo(state);
				}
			} catch (OBException e) {// 검사에 실패한 경우임.
				handler.disconnect();
				throw e;
			} catch (Exception e) {// 일반적인 프로그램 오류.
				handler.disconnect();
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}
		}
//      else if (adcInfo.getSnmpInfo().getVersion()==2)
		else {
			// snmp CONFIG_ID_SNMP_COMMUNITY
			try {
				OBCLIParserAlteon parser = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
				String state = parser.parseSnmpRCommString(snmpCfgText);
				OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY);
				if (cfgInfo == null) {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format(
									"failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY). callstack:%s",
									new OBUtility().getStackTrace()));
				}
				if (adcInfo.getSnmpInfo().getRcomm().equals(state) == false) {
					cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
					cfgInfo.setAdcInfo(state);
				} else {
					cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
					cfgInfo.setAdcInfo(state);
				}
			} catch (OBException e) {// 검사에 실패한 경우임.
				handler.disconnect();
				throw e;
			} catch (Exception e) {// 일반적인 프로그램 오류.
				handler.disconnect();
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}
		}

		// CONFIG_ID_SYSLOG_ENABLED
		String syslogCfgText = "";
		try {
			syslogCfgText = handler.cmndCfgSyslog();
			OBCLIParserAlteon parser = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
			Integer state = parser.parseSyslogState(syslogCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (state.intValue() == OBDefine.STATE_DISABLE) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(CONFIG_ENALBED);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// CONFIG_ID_SYSLOG_IPADDR
		try {
			OBCLIParserAlteon parser = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
			ArrayList<String> hostList = parser.parseSyslogHost(syslogCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_IPADDR);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format(
								"failed to get cfgInfo(CONFIG_ID_SYSLOG_IPADDR.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
			boolean isMatched = false;
			for (String ipAddr : ipAddrList) {
				for (String host : hostList) {
					if (host.equals(ipAddr) == true) {
						isMatched = true;
						break;
					}
				}
				if (isMatched == true)
					break;
			}
			if (isMatched == false) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += "\n ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += "\n ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		handler.disconnect();
		ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(configMap.values());
		Collections.sort(retVal, new CheckIDCompare());
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<OBAdcConfigInfo> checkAdcConfigInfoAlteon(OBDtoAdcInfo adcInfo, OBDatabase db)
			throws OBException {
		HashMap<Integer, OBAdcConfigInfo> configMap = makeDefaultAdcConfigInfoList(adcInfo);
		OBAdcAlteonHandler handler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());

		try {
			handler.login();
		} catch (OBExceptionUnreachable e) {// 로그인 실패한 경우임.
			ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
					makeDefaultAdcConfigInfoList(adcInfo).values());
			Collections.sort(retVal, new CheckIDCompare());
			return retVal;
		} catch (OBExceptionLogin e) {// 로그인 실패한 경우임.
			ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
					makeDefaultAdcConfigInfoList(adcInfo).values());
			Collections.sort(retVal, new CheckIDCompare());
			handler.disconnect();
			return retVal;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// vstat 상태 검사.
		try {
			String vstat = handler.cmndCfgSlbVstatCur();
			OBCLIParserAlteon parser = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
			Integer state = parser.parseVstatState(vstat);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_VSTAT);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_VSTAT). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (state.intValue() == OBDefine.STATE_DISABLE) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(CONFIG_ENALBED);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// snmp enabled 검사. -v2, v3 상관없이 검사
		String snmpCfgText = "";
		try {
			snmpCfgText = handler.cmndCfgSnmp();
			OBCLIParserAlteon parser = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
			String state = parser.parseSnmpState(snmpCfgText); // Current v1/v2 access disabled
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (state.isEmpty()) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				if (adcInfo.getSnmpInfo().getVersion() == 3) {
					cfgInfo.setAdcInfo(state + "\n" + CONFIG_SNMP_V3_ENALBED);
				} else {
					cfgInfo.setAdcInfo(state);
				}
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		if (adcInfo.getSnmpInfo().getVersion() == 3) {
			// v3 SNMP 검사 (snmp user)
			// snmp CONFIG_ID_SNMP_USER
			try {
				OBCLIParserAlteon parser = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
//				String state = parser.parseSnmpUser(snmpCfgText);
				String state = parser.parseSnmpUsm(snmpCfgText);
				OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_USER);
				if (cfgInfo == null) {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_USER). callstack:%s",
									new OBUtility().getStackTrace()));
				}
				String snmpUser = "name " + adcInfo.getSnmpInfo().getSecurityName() + ", auth "
						+ adcInfo.getSnmpInfo().getAuthProto() + ", privacy " + adcInfo.getSnmpInfo().getPrivProto();
				// System.out.println(snmpUser);
				if (state.indexOf(snmpUser) == -1) {
					cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
					cfgInfo.setAdcInfo(state);
				} else {
					cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
					cfgInfo.setAdcInfo(state);
				}
			} catch (OBException e) {// 검사에 실패한 경우임.
				handler.disconnect();
				throw e;
			} catch (Exception e) {// 일반적인 프로그램 오류.
				handler.disconnect();
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}
		}
//		else if (adcInfo.getSnmpInfo().getVersion()==2)
		else {
			// snmp CONFIG_ID_SNMP_COMMUNITY
			try {
				OBCLIParserAlteon parser = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
				String state = parser.parseSnmpRCommString(snmpCfgText);
				OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY);
				if (cfgInfo == null) {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							String.format(
									"failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY). callstack:%s",
									new OBUtility().getStackTrace()));
				}
				if (adcInfo.getSnmpInfo().getRcomm().equals(state) == false) {
					cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
					cfgInfo.setAdcInfo(state);
				} else {
					cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
					cfgInfo.setAdcInfo(state);
				}
			} catch (OBException e) {// 검사에 실패한 경우임.
				handler.disconnect();
				throw e;
			} catch (Exception e) {// 일반적인 프로그램 오류.
				handler.disconnect();
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
			}
		}

		// CONFIG_ID_SYSLOG_ENABLED
		String syslogCfgText = "";
		try {
			syslogCfgText = handler.cmndCfgSyslog();
			OBCLIParserAlteon parser = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
			Integer state = parser.parseSyslogState(syslogCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (state.intValue() == OBDefine.STATE_DISABLE) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(CONFIG_ENALBED);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// CONFIG_ID_SYSLOG_IPADDR
		try {
			OBCLIParserAlteon parser = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion());
			ArrayList<String> hostList = parser.parseSyslogHost(syslogCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_IPADDR);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format(
								"failed to get cfgInfo(CONFIG_ID_SYSLOG_IPADDR.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
			boolean isMatched = false;
			for (String ipAddr : ipAddrList) {
				for (String host : hostList) {
					if (host.equals(ipAddr) == true) {
						isMatched = true;
						break;
					}
				}
				if (isMatched == true)
					break;
			}
			if (isMatched == false) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += "\n ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += "\n ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		handler.disconnect();
		ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(configMap.values());
		Collections.sort(retVal, new CheckIDCompare());
		return retVal;
	}

	/**
	 * 기본적인 ADC config 정보를 구성한다.
	 */
	private final String CONFIG_ENALBED = "enabled";
	private final String CONFIG_DISABLED = "disabled";
//	private final String NOT_SUPPORT_VERSION 		= "not support method";
	private final String CONFIG_SNMP_V1_ENALBED = "Current v1/v2 access enabled";
	private final String CONFIG_SNMP_V3_ENALBED = "v3 access enabled";

	private HashMap<Integer, OBAdcConfigInfo> makeDefaultAdcConfigInfoList(OBDtoAdcInfo adcInfo) throws OBException {
		HashMap<Integer, OBAdcConfigInfo> retVal = new HashMap<Integer, OBAdcConfigInfo>();

		OBAdcConfigInfo obj = null;
		if (adcInfo.getAdcType().intValue() == OBDefine.ADC_TYPE_ALTEON) {
			obj = new OBAdcConfigInfo();
			obj.setConfigID(OBAdcConfigInfo.CONFIG_ID_VSTAT);
			obj.setStatus(OBAdcConfigInfo.STATUS_INIT);
			obj.setLocalInfo(CONFIG_ENALBED);
			obj.setAdcInfo("");
			retVal.put(OBAdcConfigInfo.CONFIG_ID_VSTAT, obj);
		}

//		if(adcInfo.getAdcType().intValue() == OBDefine.ADC_TYPE_F5)
//		{// F5는 enable/disable 없음.
//			obj = new OBAdcConfigInfo();
//			obj.setConfigID(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED);
//			obj.setStatus(OBAdcConfigInfo.STATUS_FAIL);
//			obj.setLocalInfo("STATE_ENABLED");
//			obj.setAdcInfo("");
//			retVal.put(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED, obj);
//		}
		if (adcInfo.getAdcType().intValue() != OBDefine.ADC_TYPE_F5) {
			obj = new OBAdcConfigInfo();
			obj.setConfigID(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED);
			obj.setStatus(OBAdcConfigInfo.STATUS_INIT);

			if (adcInfo.getSnmpInfo().getVersion() == 3) {
				obj.setLocalInfo(CONFIG_SNMP_V3_ENALBED);
			}
//			else if(adcInfo.getSnmpInfo().getVersion()==2)
			else {
				obj.setLocalInfo(CONFIG_SNMP_V1_ENALBED);
			}
			obj.setAdcInfo("");
			retVal.put(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED, obj);
		}

		if (adcInfo.getSnmpInfo().getVersion() == 3) {
			obj = new OBAdcConfigInfo();
			obj.setConfigID(OBAdcConfigInfo.CONFIG_ID_SNMP_USER);
			obj.setStatus(OBAdcConfigInfo.STATUS_INIT);
			obj.setLocalInfo("name " + adcInfo.getSnmpInfo().getSecurityName() + ", auth "
					+ adcInfo.getSnmpInfo().getAuthProto() + ", privacy " + adcInfo.getSnmpInfo().getPrivProto());
			// 5: name test, auth md5, privacy des
			obj.setAdcInfo("");
			retVal.put(OBAdcConfigInfo.CONFIG_ID_SNMP_USER, obj);
		}
//		else if(adcInfo.getSnmpInfo().getVersion()==2)
		else {
			obj = new OBAdcConfigInfo();
			obj.setConfigID(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY);
			obj.setStatus(OBAdcConfigInfo.STATUS_INIT);
//			obj.setLocalInfo(adcInfo.getSnmpRComm());
			obj.setLocalInfo(adcInfo.getSnmpInfo().getRcomm());
			obj.setAdcInfo("");
			retVal.put(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY, obj);
		}

		ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
		String ipAddrText = "";
		for (String ipAddr : ipAddrList) {
			if (!ipAddrText.isEmpty())
				ipAddrText += "\n ";
			ipAddrText += ipAddr;
		}

		if (adcInfo.getAdcType().intValue() == OBDefine.ADC_TYPE_F5) {
			obj = new OBAdcConfigInfo();
			obj.setConfigID(OBAdcConfigInfo.CONFIG_ID_SNMP_ALLOWLIST);
			obj.setStatus(OBAdcConfigInfo.STATUS_INIT);
			obj.setLocalInfo(ipAddrText);
			obj.setAdcInfo("");
			retVal.put(OBAdcConfigInfo.CONFIG_ID_SNMP_ALLOWLIST, obj);
		}

		if (adcInfo.getAdcType().intValue() != OBDefine.ADC_TYPE_F5) {// f5는 list 존재 유무로 enable/disable 판별함.
			obj = new OBAdcConfigInfo();
			obj.setConfigID(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED);
			obj.setStatus(OBAdcConfigInfo.STATUS_INIT);
			obj.setLocalInfo(CONFIG_ENALBED);
			obj.setAdcInfo("");
			retVal.put(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED, obj);
		}

		obj = new OBAdcConfigInfo();
		obj.setConfigID(OBAdcConfigInfo.CONFIG_ID_SYSLOG_IPADDR);
		obj.setStatus(OBAdcConfigInfo.STATUS_INIT);
		obj.setLocalInfo(ipAddrText);
		obj.setAdcInfo("");
		retVal.put(OBAdcConfigInfo.CONFIG_ID_SYSLOG_IPADDR, obj);

		if (adcInfo.getAdcType().intValue() == OBDefine.ADC_TYPE_F5) {
			obj = new OBAdcConfigInfo();
			obj.setConfigID(OBAdcConfigInfo.CONFIG_ID_HTTPS_ALLOWLIST);
			obj.setStatus(OBAdcConfigInfo.STATUS_INIT);
			obj.setLocalInfo(ipAddrText);
			obj.setAdcInfo("");
			retVal.put(OBAdcConfigInfo.CONFIG_ID_HTTPS_ALLOWLIST, obj);

			obj = new OBAdcConfigInfo();
			obj.setConfigID(OBAdcConfigInfo.CONFIG_ID_SSH_ALLOWLIST);
			obj.setStatus(OBAdcConfigInfo.STATUS_INIT);
			obj.setLocalInfo(ipAddrText);
			obj.setAdcInfo("");
			retVal.put(OBAdcConfigInfo.CONFIG_ID_SSH_ALLOWLIST, obj);
		}
		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(5);
////			HashMap<String, OBDtoAdcInfo> list = new OBAdcManagementImpl().getAdcInfoBasicMap(db);
//			ArrayList<OBAdcConfigInfo> list = new OBCheckAdcStatus().checkAdcConfigInfoF5(adcInfo, db);
////			OBAdcCheckResult list = new OBAdcManagementImpl().checkADCStatusLoginAlteon(adcInfo, db);
//			for(OBAdcConfigInfo info:list)
//				System.out.println(info);
////			for(OBDtoAdcInfo info:list)
////				System.out.println(info);
//			db.closeDB();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@SuppressWarnings("rawtypes")
	class CheckIDCompare implements Comparator {
		@Override
		public int compare(Object arg0, Object arg1) {
			return (((OBAdcConfigInfo) arg0).getConfigID().intValue() > ((OBAdcConfigInfo) arg1).getConfigID()
					.intValue() ? 1 : 0);
		}
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(5);
//			ArrayList<OBAdcConfigInfo> list = new OBCheckAdcStatus().checkAdcConfigInfoF5(adcInfo, db);
//			for(OBAdcConfigInfo info:list)
//				System.out.println(info);
//			db.closeDB();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@SuppressWarnings({ "unchecked" })
	private ArrayList<OBAdcConfigInfo> checkAdcConfigInfoF5(OBDtoAdcInfo adcInfo) throws OBException // dbcp 적용
	{// f5는 icontroll을 이용해서 수집한다.

		HashMap<Integer, OBAdcConfigInfo> configMap = makeDefaultAdcConfigInfoList(adcInfo);

		iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
				adcInfo.getAdcPasswordDecrypt());

		try {
			SystemF5.checkInterface(interfaces);
		} catch (Exception e) {
			try {
				CommonF5.Exception("", e.getMessage());
			} catch (OBExceptionUnreachable e1) {// 로그인 실패한 경우임.
				ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
						makeDefaultAdcConfigInfoList(adcInfo).values());
				Collections.sort(retVal, new CheckIDCompare());
				return retVal;
			} catch (OBExceptionLogin e1) {// 로그인 실패한 경우임.
				ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
						makeDefaultAdcConfigInfoList(adcInfo).values());
				Collections.sort(retVal, new CheckIDCompare());
				return retVal;
			} catch (Exception e1) {// 일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
			}
			// error
		}

		// snmp enabled 검사.
		OBAdcConfigInfo cfgInfo = null;
//      try
//      {
//          cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED);
//          if(cfgInfo==null)
//          {
//              throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED). callstack:%s", new OBUtility().getStackTrace()));
//          }
//          String state = SystemF5.getSnmpState(interfaces);
//          if(state.isEmpty() || state.contains("STATE_DISABLED"))
//          {
//              cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
//              cfgInfo.setAdcInfo(CONFIG_DISABLED);
//          }
//          else
//          {
//              cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
//              cfgInfo.setAdcInfo(state);
//          }
//      }
//      catch(OBExceptionVersion e)
//      {// 지원되지 않은 버전인 경우. 
//          cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
//          cfgInfo.setAdcInfo(CONFIG_DISABLED);
//      }
//      catch(OBException e)
//      {
//          throw e;
//      }
//      catch(Exception e)
//      {// 일반적인 프로그램 오류. 
//          throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//      }

//      // snmp CONFIG_ID_SNMP_COMMUNITY
		try {
			ArrayList<String> communityList = SystemF5.getSnmpReadCommunity(interfaces);
			cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY). callstack:%s",
								new OBUtility().getStackTrace()));
			}

			cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
			String communityString = "";
			for (String community : communityList) {
				if (!communityString.isEmpty())
					communityString += "\n ";
				communityString += community;

				if (adcInfo.getSnmpRComm().equals(community) == true) {
					cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				}
			}
			cfgInfo.setAdcInfo(communityString);
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// snmp allow list
		try {
			ArrayList<String> allowList = SystemF5.getSnmpAllowList(interfaces);
			cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_ALLOWLIST);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY). callstack:%s",
								new OBUtility().getStackTrace()));
			}

			ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
			cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
			String hostText = "";
			for (String host : allowList) {
				if (!hostText.isEmpty())
					hostText += "\n ";
				hostText += host;
				for (String ipAddr : ipAddrList) {
					if (OBNetwork.isInvolvedIPAddress(host, ipAddr) == true) {
						cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
					}
				}
			}
			cfgInfo.setAdcInfo(hostText);
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// ssh 연결을 통해서 데이터를 수집한다.
		OBAdcF5Handler handler = OBCommon.getValidF5Handler(adcInfo.getSwVersion());
		try {
			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(),
					adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnPort());
			handler.sshLogin();
		} catch (OBExceptionUnreachable e) {
			return new ArrayList<OBAdcConfigInfo>(configMap.values());
		} catch (OBExceptionLogin e) {
			return new ArrayList<OBAdcConfigInfo>(configMap.values());
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}

//      // CONFIG_ID_SYSLOG_ALLOWLIST
		try {
			String syslogCfgText = handler.cmndListSyslog();
			OBCLIParserF5 parser = OBCommon.getValidF5CLIParser(adcInfo.getSwVersion());
			ArrayList<String> hostList = parser.parseSyslogList(syslogCfgText);
			cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_IPADDR);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}

			ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
			boolean isMatched = false;
			for (String ipAddr : ipAddrList) {
				for (String host : hostList) {
					if (OBNetwork.isInvolvedIPAddress(host, ipAddr) == true) {
						isMatched = true;
						break;
					}
				}
				if (isMatched == true)
					break;
			}
			if (isMatched == false) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += "\n ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += "\n ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
//      // CONFIG_ID_HTTPS_ALLOWLIST
//      try
//      {
//          String syslogCfgText = handler.cmndListHttps();
//          OBCLIParserF5 parser = OBCommon.getValidF5CLIParser(adcInfo.getSwVersion());
//          ArrayList<String> hostList = parser.parseHttpsList(syslogCfgText);
//          cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_HTTPS_ALLOWLIST);
//          if(cfgInfo==null)
//          {
//              throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_HTTPS_ALLOWLIST). callstack:%s", new OBUtility().getStackTrace()));
//          }
//          
//          ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
//          cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
//          String hostText = "";
//          for(String host:hostList)
//          {
//              if(!hostText.isEmpty())
//                  hostText += "\n ";
//              hostText += host;
//              if(host.equals(OBCLIParserF5.SUFFIX_ALLOW_ALL))
//              {
//                  cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
//                  continue;
//              }
//              for(String ipAddr:ipAddrList)
//              {
//                  if(OBNetwork.isInvolvedIPAddress(host, ipAddr)==true)
//                  {
//                      cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
//                  }
//              }
//          }
//          cfgInfo.setAdcInfo(hostText);
//      }
//      catch(OBException e)
//      {// 검사에 실패한 경우임.
//          handler.disconnect();
//          throw e;
//      }
//      catch(Exception e)
//      {// 일반적인 프로그램 오류.
//          handler.disconnect();
//          throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//      }       

//      // CONFIG_ID_SSH_ALLOWLIST
		try {
			String syslogCfgText = handler.cmndListSshd();
			OBCLIParserF5 parser = OBCommon.getValidF5CLIParser(adcInfo.getSwVersion());
			ArrayList<String> hostList = parser.parseSshdList(syslogCfgText);
			cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SSH_ALLOWLIST);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SSH_ALLOWLIST). callstack:%s",
								new OBUtility().getStackTrace()));
			}

			ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
			cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
			String hostText = "";
			for (String host : hostList) {
				if (!hostText.isEmpty())
					hostText += "\n ";
				hostText += host;
				if (host.equals(OBCLIParserF5.SUFFIX_ALLOW_ALL)) {
					cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
					continue;
				}
				for (String ipAddr : ipAddrList) {
					if (OBNetwork.isInvolvedIPAddress(host, ipAddr) == true) {
						cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
					}
				}
			}
			cfgInfo.setAdcInfo(hostText);
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		handler.disconnect();

		ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(configMap.values());
		Collections.sort(retVal, new CheckIDCompare());
		return retVal;
	}

	@SuppressWarnings({ "unchecked" })
	private ArrayList<OBAdcConfigInfo> checkAdcConfigInfoF5(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException {// f5는
																														// icontroll을
																														// 이용해서
																														// 수집한다.
		HashMap<Integer, OBAdcConfigInfo> configMap = makeDefaultAdcConfigInfoList(adcInfo);

		iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
				adcInfo.getAdcPasswordDecrypt());

		try {
			SystemF5.checkInterface(interfaces);
		} catch (Exception e) {
			try {
				CommonF5.Exception("", e.getMessage());
			} catch (OBExceptionUnreachable e1) {// 로그인 실패한 경우임.
				ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
						makeDefaultAdcConfigInfoList(adcInfo).values());
				Collections.sort(retVal, new CheckIDCompare());
				return retVal;
			} catch (OBExceptionLogin e1) {// 로그인 실패한 경우임.
				ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
						makeDefaultAdcConfigInfoList(adcInfo).values());
				Collections.sort(retVal, new CheckIDCompare());
				return retVal;
			} catch (Exception e1) {// 일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
			}
			// error
		}

		// snmp enabled 검사.
		OBAdcConfigInfo cfgInfo = null;
//		try
//		{
//			cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED);
//			if(cfgInfo==null)
//			{
//				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED). callstack:%s", new OBUtility().getStackTrace()));
//			}
//			String state = SystemF5.getSnmpState(interfaces);
//			if(state.isEmpty() || state.contains("STATE_DISABLED"))
//			{
//				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
//				cfgInfo.setAdcInfo(CONFIG_DISABLED);
//			}
//			else
//			{
//				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
//				cfgInfo.setAdcInfo(state);
//			}
//		}
//		catch(OBExceptionVersion e)
//		{// 지원되지 않은 버전인 경우. 
//			cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
//			cfgInfo.setAdcInfo(CONFIG_DISABLED);
//		}
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류. 
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}

//		// snmp CONFIG_ID_SNMP_COMMUNITY
		try {
			ArrayList<String> communityList = SystemF5.getSnmpReadCommunity(interfaces);
			cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY). callstack:%s",
								new OBUtility().getStackTrace()));
			}

			cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
			String communityString = "";
			for (String community : communityList) {
				if (!communityString.isEmpty())
					communityString += "\n ";
				communityString += community;

				if (adcInfo.getSnmpRComm().equals(community) == true) {
					cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				}
			}
			cfgInfo.setAdcInfo(communityString);
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// snmp allow list
		try {
			ArrayList<String> allowList = SystemF5.getSnmpAllowList(interfaces);
			cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_ALLOWLIST);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY). callstack:%s",
								new OBUtility().getStackTrace()));
			}

			ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
			cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
			String hostText = "";
			for (String host : allowList) {
				if (!hostText.isEmpty())
					hostText += "\n ";
				hostText += host;
				for (String ipAddr : ipAddrList) {
					if (OBNetwork.isInvolvedIPAddress(host, ipAddr) == true) {
						cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
					}
				}
			}
			cfgInfo.setAdcInfo(hostText);
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// ssh 연결을 통해서 데이터를 수집한다.
		OBAdcF5Handler handler = OBCommon.getValidF5Handler(adcInfo.getSwVersion());
		try {
			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(),
					adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnPort());
			handler.sshLogin();
		} catch (OBExceptionUnreachable e) {
			return new ArrayList<OBAdcConfigInfo>(configMap.values());
		} catch (OBExceptionLogin e) {
			return new ArrayList<OBAdcConfigInfo>(configMap.values());
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}

//		// CONFIG_ID_SYSLOG_ALLOWLIST
		try {
			String syslogCfgText = handler.cmndListSyslog();
			OBCLIParserF5 parser = OBCommon.getValidF5CLIParser(adcInfo.getSwVersion());
			ArrayList<String> hostList = parser.parseSyslogList(syslogCfgText);
			cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_IPADDR);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}

			ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
			boolean isMatched = false;
			for (String ipAddr : ipAddrList) {
				for (String host : hostList) {
					if (OBNetwork.isInvolvedIPAddress(host, ipAddr) == true) {
						isMatched = true;
						break;
					}
				}
				if (isMatched == true)
					break;
			}
			if (isMatched == false) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += "\n ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += "\n ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
//		// CONFIG_ID_HTTPS_ALLOWLIST
//		try
//		{
//			String syslogCfgText = handler.cmndListHttps();
//			OBCLIParserF5 parser = OBCommon.getValidF5CLIParser(adcInfo.getSwVersion());
//			ArrayList<String> hostList = parser.parseHttpsList(syslogCfgText);
//			cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_HTTPS_ALLOWLIST);
//			if(cfgInfo==null)
//			{
//				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_HTTPS_ALLOWLIST). callstack:%s", new OBUtility().getStackTrace()));
//			}
//			
//			ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
//			cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
//			String hostText = "";
//			for(String host:hostList)
//			{
//				if(!hostText.isEmpty())
//					hostText += "\n ";
//				hostText += host;
//				if(host.equals(OBCLIParserF5.SUFFIX_ALLOW_ALL))
//				{
//					cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
//					continue;
//				}
//				for(String ipAddr:ipAddrList)
//				{
//					if(OBNetwork.isInvolvedIPAddress(host, ipAddr)==true)
//					{
//						cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
//					}
//				}
//			}
//			cfgInfo.setAdcInfo(hostText);
//		}
//		catch(OBException e)
//		{// 검사에 실패한 경우임.
//			handler.disconnect();
//			throw e;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			handler.disconnect();
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}		

//		// CONFIG_ID_SSH_ALLOWLIST
		try {
			String syslogCfgText = handler.cmndListSshd();
			OBCLIParserF5 parser = OBCommon.getValidF5CLIParser(adcInfo.getSwVersion());
			ArrayList<String> hostList = parser.parseSshdList(syslogCfgText);
			cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SSH_ALLOWLIST);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SSH_ALLOWLIST). callstack:%s",
								new OBUtility().getStackTrace()));
			}

			ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
			cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
			String hostText = "";
			for (String host : hostList) {
				if (!hostText.isEmpty())
					hostText += "\n ";
				hostText += host;
				if (host.equals(OBCLIParserF5.SUFFIX_ALLOW_ALL)) {
					cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
					continue;
				}
				for (String ipAddr : ipAddrList) {
					if (OBNetwork.isInvolvedIPAddress(host, ipAddr) == true) {
						cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
					}
				}
			}
			cfgInfo.setAdcInfo(hostText);
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		handler.disconnect();

		ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(configMap.values());
		Collections.sort(retVal, new CheckIDCompare());
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<OBAdcConfigInfo> checkAdcConfigInfoPAS(OBDtoAdcInfo adcInfo) throws OBException // dbcp 적용
	{
		HashMap<Integer, OBAdcConfigInfo> configMap = makeDefaultAdcConfigInfoList(adcInfo);

		OBAdcPASHandler handler = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());

		try {
			handler.login();
		} catch (OBExceptionUnreachable e) {// 로그인 실패한 경우임.
			ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
					makeDefaultAdcConfigInfoList(adcInfo).values());
			Collections.sort(retVal, new CheckIDCompare());
			return retVal;
		} catch (OBExceptionLogin e) {// 로그인 실패한 경우임.
			ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
					makeDefaultAdcConfigInfoList(adcInfo).values());
			Collections.sort(retVal, new CheckIDCompare());
			return retVal;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// snmp enabled 검사.
		String snmpCfgText = "";
		try {
			snmpCfgText = handler.cmndSnmpInfo();
			OBCLIParserPAS parser = OBCommon.getValidPASCLIParser(adcInfo.getSwVersion());
			String state = parser.parseSnmpState(snmpCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}

			if (state.isEmpty() || state.contains(CONFIG_DISABLED)) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(state);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// snmp CONFIG_ID_SNMP_COMMUNITY
		try {
			OBCLIParserPAS parser = OBCommon.getValidPASCLIParser(adcInfo.getSwVersion());
			String state = parser.parseSnmpRCommString(snmpCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (adcInfo.getSnmpRComm().equals(state) == false) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(state);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(state);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// CONFIG_ID_SYSLOG_ENABLED
		String syslogCfgText = "";
		try {
			syslogCfgText = handler.cmndSyslogInfo();
			OBCLIParserPAS parser = OBCommon.getValidPASCLIParser(adcInfo.getSwVersion());
			Integer state = parser.parseSyslogState(syslogCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (state.intValue() == OBDefine.STATE_DISABLE) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(CONFIG_ENALBED);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// CONFIG_ID_SYSLOG_IPADDR
		try {
			OBCLIParserPAS parser = OBCommon.getValidPASCLIParser(adcInfo.getSwVersion());
			ArrayList<String> hostList = parser.parseSyslogHost(syslogCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_IPADDR);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format(
								"failed to get cfgInfo(CONFIG_ID_SYSLOG_IPADDR.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
			boolean isMatched = false;
			for (String ipAddr : ipAddrList) {
				for (String host : hostList) {
					if (host.equals(ipAddr) == true) {
						isMatched = true;
						break;
					}
				}
				if (isMatched == true)
					break;
			}
			if (isMatched == false) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += ", ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += ", ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		handler.disconnect();
		ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(configMap.values());
		Collections.sort(retVal, new CheckIDCompare());
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<OBAdcConfigInfo> checkAdcConfigInfoPAS(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException {
		HashMap<Integer, OBAdcConfigInfo> configMap = makeDefaultAdcConfigInfoList(adcInfo);

		OBAdcPASHandler handler = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());

		try {
			handler.login();
		} catch (OBExceptionUnreachable e) {// 로그인 실패한 경우임.
			ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
					makeDefaultAdcConfigInfoList(adcInfo).values());
			Collections.sort(retVal, new CheckIDCompare());
			return retVal;
		} catch (OBExceptionLogin e) {// 로그인 실패한 경우임.
			ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
					makeDefaultAdcConfigInfoList(adcInfo).values());
			Collections.sort(retVal, new CheckIDCompare());
			return retVal;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// snmp enabled 검사.
		String snmpCfgText = "";
		try {
			snmpCfgText = handler.cmndSnmpInfo();
			OBCLIParserPAS parser = OBCommon.getValidPASCLIParser(adcInfo.getSwVersion());
			String state = parser.parseSnmpState(snmpCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}

			if (state.isEmpty() || state.contains(CONFIG_DISABLED)) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(state);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// snmp CONFIG_ID_SNMP_COMMUNITY
		try {
			OBCLIParserPAS parser = OBCommon.getValidPASCLIParser(adcInfo.getSwVersion());
			String state = parser.parseSnmpRCommString(snmpCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (adcInfo.getSnmpRComm().equals(state) == false) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(state);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(state);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// CONFIG_ID_SYSLOG_ENABLED
		String syslogCfgText = "";
		try {
			syslogCfgText = handler.cmndSyslogInfo();
			OBCLIParserPAS parser = OBCommon.getValidPASCLIParser(adcInfo.getSwVersion());
			Integer state = parser.parseSyslogState(syslogCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (state.intValue() == OBDefine.STATE_DISABLE) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(CONFIG_ENALBED);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// CONFIG_ID_SYSLOG_IPADDR
		try {
			OBCLIParserPAS parser = OBCommon.getValidPASCLIParser(adcInfo.getSwVersion());
			ArrayList<String> hostList = parser.parseSyslogHost(syslogCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_IPADDR);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format(
								"failed to get cfgInfo(CONFIG_ID_SYSLOG_IPADDR.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
			boolean isMatched = false;
			for (String ipAddr : ipAddrList) {
				for (String host : hostList) {
					if (host.equals(ipAddr) == true) {
						isMatched = true;
						break;
					}
				}
				if (isMatched == true)
					break;
			}
			if (isMatched == false) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += ", ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += ", ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		handler.disconnect();
		ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(configMap.values());
		Collections.sort(retVal, new CheckIDCompare());
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<OBAdcConfigInfo> checkAdcConfigInfoPASK(OBDtoAdcInfo adcInfo) throws OBException // dbcp 적용
	{
		HashMap<Integer, OBAdcConfigInfo> configMap = makeDefaultAdcConfigInfoList(adcInfo);

		OBAdcPASKHandler handler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());

		try {
			handler.login();

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_PORTOPEN);
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN);
		} catch (OBExceptionUnreachable e) {// 로그인 실패한 경우임.
			return new ArrayList<OBAdcConfigInfo>(makeDefaultAdcConfigInfoList(adcInfo).values());
		} catch (OBExceptionLogin e) {// 로그인 실패한 경우임.
			return new ArrayList<OBAdcConfigInfo>(makeDefaultAdcConfigInfoList(adcInfo).values());
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// snmp enabled 검사.
		String snmpCfgText = "";
		try {
			snmpCfgText = handler.cmndSnmpInfo();
			OBCLIParserPASK parser = OBCommon.getValidPASKCLIParser(adcInfo.getSwVersion());
			String state = parser.parseSnmpState(snmpCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}

			if (state.isEmpty() || state.contains(CONFIG_DISABLED)) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(state);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// snmp CONFIG_ID_SNMP_COMMUNITY
		try {
			OBCLIParserPASK parser = OBCommon.getValidPASKCLIParser(adcInfo.getSwVersion());
			String state = parser.parseSnmpRCommString(snmpCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (adcInfo.getSnmpRComm().equals(state) == false) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(state);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(state);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// CONFIG_ID_SYSLOG_ENABLED
		String syslogCfgText = "";
		try {
			syslogCfgText = handler.cmndSyslogInfo();
			OBCLIParserPASK parser = OBCommon.getValidPASKCLIParser(adcInfo.getSwVersion());
			Integer state = parser.parseSyslogState(syslogCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (state.intValue() == OBDefine.STATE_DISABLE) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(CONFIG_ENALBED);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// CONFIG_ID_SYSLOG_IPADDR
		try {
			OBCLIParserPASK parser = OBCommon.getValidPASKCLIParser(adcInfo.getSwVersion());
			ArrayList<String> hostList = parser.parseSyslogHost(syslogCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_IPADDR);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format(
								"failed to get cfgInfo(CONFIG_ID_SYSLOG_IPADDR.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
			boolean isMatched = false;
			for (String ipAddr : ipAddrList) {
				for (String host : hostList) {
					if (host.equals(ipAddr) == true) {
						isMatched = true;
						break;
					}
				}
				if (isMatched == true)
					break;
			}
			if (isMatched == false) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += ", ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += ", ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		handler.disconnect();
		ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(configMap.values());
		Collections.sort(retVal, new CheckIDCompare());
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<OBAdcConfigInfo> checkAdcConfigInfoPASK(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException {
		HashMap<Integer, OBAdcConfigInfo> configMap = makeDefaultAdcConfigInfoList(adcInfo);

		OBAdcPASKHandler handler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());

		try {
			handler.login();

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_PORTOPEN, db);
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN, db);
		} catch (OBExceptionUnreachable e) {// 로그인 실패한 경우임.
			return new ArrayList<OBAdcConfigInfo>(makeDefaultAdcConfigInfoList(adcInfo).values());
		} catch (OBExceptionLogin e) {// 로그인 실패한 경우임.
			return new ArrayList<OBAdcConfigInfo>(makeDefaultAdcConfigInfoList(adcInfo).values());
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// snmp enabled 검사.
		String snmpCfgText = "";
		try {
			snmpCfgText = handler.cmndSnmpInfo();
			OBCLIParserPASK parser = OBCommon.getValidPASKCLIParser(adcInfo.getSwVersion());
			String state = parser.parseSnmpState(snmpCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}

			if (state.isEmpty() || state.contains(CONFIG_DISABLED)) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(state);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// snmp CONFIG_ID_SNMP_COMMUNITY
		try {
			OBCLIParserPASK parser = OBCommon.getValidPASKCLIParser(adcInfo.getSwVersion());
			String state = parser.parseSnmpRCommString(snmpCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SNMP_COMMUNITY). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (adcInfo.getSnmpRComm().equals(state) == false) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(state);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(state);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// CONFIG_ID_SYSLOG_ENABLED
		String syslogCfgText = "";
		try {
			syslogCfgText = handler.cmndSyslogInfo();
			OBCLIParserPASK parser = OBCommon.getValidPASKCLIParser(adcInfo.getSwVersion());
			Integer state = parser.parseSyslogState(syslogCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("failed to get cfgInfo(OBAdcConfigInfo.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			if (state.intValue() == OBDefine.STATE_DISABLE) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				cfgInfo.setAdcInfo(CONFIG_DISABLED);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				cfgInfo.setAdcInfo(CONFIG_ENALBED);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		// CONFIG_ID_SYSLOG_IPADDR
		try {
			OBCLIParserPASK parser = OBCommon.getValidPASKCLIParser(adcInfo.getSwVersion());
			ArrayList<String> hostList = parser.parseSyslogHost(syslogCfgText);
			OBAdcConfigInfo cfgInfo = configMap.get(OBAdcConfigInfo.CONFIG_ID_SYSLOG_IPADDR);
			if (cfgInfo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format(
								"failed to get cfgInfo(CONFIG_ID_SYSLOG_IPADDR.CONFIG_ID_SYSLOG_ENABLED). callstack:%s",
								new OBUtility().getStackTrace()));
			}
			ArrayList<String> ipAddrList = OBNetwork.getAllIPAddress();
			boolean isMatched = false;
			for (String ipAddr : ipAddrList) {
				for (String host : hostList) {
					if (host.equals(ipAddr) == true) {
						isMatched = true;
						break;
					}
				}
				if (isMatched == true)
					break;
			}
			if (isMatched == false) {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_ABNORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += ", ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			} else {
				cfgInfo.setStatus(OBAdcConfigInfo.STATUS_NORMAL);
				String hostText = "";
				for (String host : hostList) {
					if (!hostText.isEmpty())
						hostText += ", ";
					hostText += host;
				}
				cfgInfo.setAdcInfo(hostText);
			}
		} catch (OBException e) {// 검사에 실패한 경우임.
			handler.disconnect();
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		handler.disconnect();
		ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(configMap.values());
		Collections.sort(retVal, new CheckIDCompare());
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<OBAdcConfigInfo> checkAdcConfigInfoPASUnknown(OBDtoAdcInfo adcInfo) throws OBException // dbcp 적용
	{// 버전 확인이 안된 상태임으로 아무런 작업을 하지 않는다.
		ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
				makeDefaultAdcConfigInfoList(adcInfo).values());
		Collections.sort(retVal, new CheckIDCompare());
		return retVal;
	}

	@SuppressWarnings("unchecked")
	private ArrayList<OBAdcConfigInfo> checkAdcConfigInfoPASUnknown(OBDtoAdcInfo adcInfo, OBDatabase db)
			throws OBException {// 버전 확인이 안된 상태임으로 아무런 작업을 하지 않는다.
		ArrayList<OBAdcConfigInfo> retVal = new ArrayList<OBAdcConfigInfo>(
				makeDefaultAdcConfigInfoList(adcInfo).values());
		Collections.sort(retVal, new CheckIDCompare());
		return retVal;
	}

	/**
	 * db에 저장된 syslog를 찾는다. 최근 10분동안 수집된 syslog가 있는지 검사한다.
	 * 
	 * @param adcInfo
	 * @param db
	 * @return
	 * @throws OBException
	 */
	private Timestamp getLastSyslogReceviedTime(OBDtoAdcInfo adcInfo, boolean isNewADC) throws OBException {
		OBDatabase db = new OBDatabase();
		String sqlText = "";
		ResultSet rs = null;
		try {
			db.openDB();

			Integer adcIndex = adcInfo.getIndex();
//			if(adcIndex==null || adcIndex.intValue()==0)
			if (isNewADC == true) {// adcIndex가 입력되지 않은 상태. ipaddress를 이용해서 index을 찾는다. 없을 경우에는 syslog 테이블 검사는
									// skip한다.
				OBDtoAdcInfo adcBasicInfo = new OBAdcManagementImpl()
						.getAdcInfoBasicByIPAddress(adcInfo.getAdcIpAddress());
				if (adcBasicInfo != null) {
					adcIndex = adcBasicInfo.getIndex();
				}
			}

			// log_adc_syslog 테이블을 찾는다.
//			String timeSqlText = makeTimeSqlText();

			if (adcIndex != 0) {
				sqlText = String.format(" SELECT SYSLOG_TIME		    			\n"
						+ " FROM TMP_LAST_ADC_CHECK_TIME            \n" + " WHERE ADC_INDEX = %d LIMIT 1            \n",
						adcIndex);

				rs = db.executeQuery(sqlText);
				if (rs.next() == true) {// 데이터가 없는 경우임.
					Timestamp rcvTime = db.getTimestamp(rs, "SYSLOG_TIME");
					if (rcvTime != null) {
						return rcvTime;
					}
				}
			}

			if (isNewADC == true) {// 신규 ADC 추가 작업일 때에만 감사로그 테이블을 검색하여 입력되었는지 확인한다.
									// log_adc_syslog 테이블에 없을 경우에 log_system_audit에서 code==5001인 로그에서 찾는다.
				String wildcardKeyIPAddr = "%" + adcInfo.getAdcIpAddress() + ")%";// IP 주소는 ()에 표시되어 있기 때문.
																					// 192.168.10.111과 192.168.10.11을
																					// 구분하기 위해.
				String wildcardKeySyslogIPAddr = "%" + adcInfo.getSyslogIpAddress() + ")%";
				String contentText = String.format("CONTENT LIKE %s ", OBParser.sqlString(wildcardKeyIPAddr));
				if (adcInfo.getSyslogIpAddress() != null && !adcInfo.getSyslogIpAddress().isEmpty()) {
					contentText += String.format(" OR CONTENT LIKE %s", OBParser.sqlString(wildcardKeySyslogIPAddr));
				}

				sqlText = String.format(" SELECT OCCUR_TIME 			    		\n"
						+ " FROM LOG_SYSTEM_AUDIT                   \n" + " WHERE CODE = %d                  		\n"
						+ " AND %s									\n" + " ORDER BY OCCUR_TIME DESC LIMIT 1		\n",
						5001, // audit code
						contentText);
				rs = db.executeQuery(sqlText);
				if (rs.next() == true) {// 데이터 추출에 성공한 경우
					Timestamp rcvTime = db.getTimestamp(rs, "OCCUR_TIME");
					if (rcvTime != null)
						return rcvTime;
				}
			}
			return null;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			String content = "등록되지 않은 ADC에서 SYSLOG을 수신했습니다. (IP 주소: 192.168.200.111)";
//			String list = new OBCheckAdcStatus().extractIPAddressFromContent(content);
//			System.out.println(list);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	private String extractIPAddressFromContent(String content) {
		String retVal = "";
		String ipRegex = "\\b(?:[0-9]{1,3}\\.){3}[0-9]{1,3}\\b";
		Pattern pSyslog = Pattern.compile(ipRegex);
		Matcher mc = pSyslog.matcher(content);
		// Syslog를 가져온 데이터를 :로 시작하고 ,로끝나는 구문을 데이터 가공 처리함 text1에 삽입
		if (mc.find()) {
			retVal = mc.group();
		}
		return retVal;
	}

	/**
	 * 가장 최근에 수신한 로그를 제공한다.
	 * 
	 * @param adcInfo
	 * @param db
	 * @return
	 * @throws OBException
	 */
	private ArrayList<String> getLastUnregisteredSyslog(OBDtoAdcInfo adcInfo) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();

		try {
			db.openDB();

			// log_system_audit에서 code==5001인 로그에서 찾는다.
			String timeSqlText = makeTimeSqlText();
			sqlText = String.format(" SELECT OCCUR_TIME, CONTENT				\n"
					+ " FROM LOG_SYSTEM_AUDIT                   \n" + " WHERE CODE = %d                    		\n"
					+ " AND %s 									\n" + " ORDER BY OCCUR_TIME DESC 		        \n",
					5001, timeSqlText);// audit code
			ResultSet rs = db.executeQuery(sqlText);

			HashMap<String, String> map = new HashMap<String, String>();
			while (rs.next()) {
				String content = db.getString(rs, "CONTENT");
				String ipAddress = extractIPAddressFromContent(content);
				if (map.get(ipAddress) == null)
					map.put(ipAddress, ipAddress);
			}
			return new ArrayList<String>(map.values());
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

//	public OBAdcCheckResult checkADCStatus(OBDtoAdcInfo adcInfo, Integer checkItem, boolean isNewADC, OBDatabase db) throws OBException
//	{
//		// 입력된 패스워드는 plain text 형태로 입력된다. 이를 암호화 하여 처리한다.
//		OBAdcCheckResult retVal = null;
//		try
//		{
//			switch(checkItem)
//			{
//			case OBAdcCheckResult.CHECK_ID_PORTOPEN:
//				retVal = checkADCStatusPortOpen(adcInfo, db);
//				break;
//			case OBAdcCheckResult.CHECK_ID_LOGIN:
//				retVal = checkADCStatusLogin(adcInfo, db);
//				break;
//			case OBAdcCheckResult.CHECK_ID_VERSION:
//				retVal = checkADCStatusVersion(adcInfo, db);
//				break;
//			case OBAdcCheckResult.CHECK_ID_PORTREVERSE:
//				retVal = checkADCStatusReversePortOpen(adcInfo, db);
//				break;
//			case OBAdcCheckResult.CHECK_ID_SNMP:
//				retVal = checkADCStatusSnmp(adcInfo, db);
//				break;
//			case OBAdcCheckResult.CHECK_ID_SYSLOG:
//				retVal = checkADCStatusSyslog(adcInfo, isNewADC, db);
//				break;
//			default:
//				throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER, "invalid paramer. unsuported check id:"+checkItem);
//			}
//		}
//		catch(OBException e)
//		{
//			throw e;
//		}
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public OBAdcCheckResult checkADCStatus(OBDtoAdcInfo adcInfo, Integer checkItem, boolean isNewADC)
			throws OBException {
		// 입력된 패스워드는 plain text 형태로 입력된다. 이를 암호화 하여 처리한다.
		OBAdcCheckResult retVal = null;
		try {
			switch (checkItem) {
			case OBAdcCheckResult.CHECK_ID_PORTOPEN:
				retVal = checkADCStatusPortOpen(adcInfo);
				break;
			case OBAdcCheckResult.CHECK_ID_LOGIN:
				retVal = checkADCStatusLogin(adcInfo);
				break;
			case OBAdcCheckResult.CHECK_ID_VERSION:
				retVal = checkADCStatusVersion(adcInfo);
				break;
			case OBAdcCheckResult.CHECK_ID_PORTREVERSE:
				retVal = checkADCStatusReversePortOpen(adcInfo);
				break;
			case OBAdcCheckResult.CHECK_ID_SNMP:
				retVal = checkADCStatusSnmp(adcInfo);
				break;
			case OBAdcCheckResult.CHECK_ID_SYSLOG:
				retVal = checkADCStatusSyslog(adcInfo, isNewADC);
				break;
			default:
				throw new OBException(OBException.ERRCODE_SYSTEM_PARAMETER,
						"invalid paramer. unsuported check id:" + checkItem);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

//	private OBAdcCheckResult checkADCStatusPortOpen(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		switch (adcInfo.getAdcType())
//		{
//		case OBDefine.ADC_TYPE_ALTEON:
//			retVal = checkADCStatusPortOpenAlteon(adcInfo, db);
//			break;
//		case OBDefine.ADC_TYPE_F5:
//			retVal = checkADCStatusPortOpenF5(adcInfo, db);
//			break;
//		case OBDefine.ADC_TYPE_PIOLINK_PAS:
//			retVal = checkADCStatusPortOpenPAS(adcInfo, db);
//			break;
//		case OBDefine.ADC_TYPE_PIOLINK_PASK:
//			retVal = checkADCStatusPortOpenPASK(adcInfo, db);
//			break;
//		case OBDefine.ADC_TYPE_PIOLINK_UNKNOWN:
//			retVal = checkADCStatusPortOpenPASUnknown(adcInfo, db);
//			break;
//		default:
//			throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);
//		}
//	
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusPortOpen(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		switch (adcInfo.getAdcType()) {
		case OBDefine.ADC_TYPE_ALTEON:
			retVal = checkADCStatusPortOpenAlteon(adcInfo);
			break;
		case OBDefine.ADC_TYPE_F5:
			retVal = checkADCStatusPortOpenF5(adcInfo);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_PAS:
			retVal = checkADCStatusPortOpenPAS(adcInfo);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_PASK:
			retVal = checkADCStatusPortOpenPASK(adcInfo);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_UNKNOWN:
			retVal = checkADCStatusPortOpenPASUnknown(adcInfo);
			break;
		default:
			throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);
		}

		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(8);
////			HashMap<String, OBDtoAdcInfo> list = new OBAdcManagementImpl().getAdcInfoBasicMap(db);
//			OBAdcCheckResult list = new OBAdcManagementImpl().checkADCStatusPortOpenAlteon(adcInfo, db);
//			System.out.println(list);
////			for(OBDtoAdcInfo info:list)
////				System.out.println(info);
//			db.closeDB();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	private OBAdcCheckResult checkADCStatusPortOpenAlteon(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTOPEN);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//		
//		// port open 테스트. adcInfo의 connPort 에 입력된 정보의 포트 오픈 여부를 확인한다.
//		try
//		{
//			if(OBNetwork.portTcpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(), MAX_WAIT_TIME_MSEC)==false)
//			{
//				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("tcp port(%s:%d) unreachable", adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), adcInfo.getConnPort().toString()));
//				return retVal;
//			}
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_PORTOPEN, db);
//		}
//		catch(Exception e)
//		{
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check open port(%s:%d)-illegal null pointer error", adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusPortOpenAlteon(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTOPEN);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		// port open 테스트. adcInfo의 connPort 에 입력된 정보의 포트 오픈 여부를 확인한다.
		try {
			if (adcInfo.getConnProtocol() == OBDefine.PROTOCOL_ICMP) {
				if (OBNetwork.icmpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(),
						MAX_WAIT_TIME_MSEC) == false) {
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
							String.format("icmp (%s) unreachable", adcInfo.getAdcIpAddress()));
					retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
					retVal.setSummary(
							String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
									adcInfo.getConnPort().toString()));
					return retVal;
				}
			} else {
				if (OBNetwork.portTcpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(),
						MAX_WAIT_TIME_MSEC) == false) {
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("tcp port(%s:%d) unreachable",
							adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
					retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
					retVal.setSummary(
							String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
									adcInfo.getConnPort().toString()));
					return retVal;
				}
			}

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_PORTOPEN);
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check open port(%s:%d)-illegal null pointer error",
							adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

//	private OBAdcCheckResult checkADCStatusPortOpenF5(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTOPEN);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//		
//		// port open 테스트. adcInfo의 connPort 에 입력된 정보의 포트 오픈 여부를 확인한다. cli, https 포트를 검사한다.
//		try
//		{
//			if(OBNetwork.portTcpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(), MAX_WAIT_TIME_MSEC)==false)
//			{
//				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("tcp port(%s:%d) unreachable", adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), adcInfo.getConnPort().toString()));
//				return retVal;
//			}
//			if(OBNetwork.portTcpIsOpen(adcInfo.getAdcIpAddress(), HTTPS_PORT_NUM, MAX_WAIT_TIME_MSEC)==false)
//			{
//				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("tcp port(%s:%d) unreachable", adcInfo.getAdcIpAddress(), HTTPS_PORT_NUM));
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), adcInfo.getConnPort().toString()));
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				return retVal;
//			}
//			// 체크 시간을 기록함. 
//			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_PORTOPEN, db);
//		}
//		catch(Exception e)
//		{
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check open port(%s:%d)-illegal null pointer error", adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusPortOpenF5(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTOPEN);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		// port open 테스트. adcInfo의 connPort 에 입력된 정보의 포트 오픈 여부를 확인한다. cli, https 포트를
		// 검사한다.
		try {
			if (adcInfo.getOpMode() != OBDefine.OP_MODE_MONITORING) {
				if (OBNetwork.portTcpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(),
						MAX_WAIT_TIME_MSEC) == false) {
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("tcp port(%s:%d) unreachable",
							adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
					retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
					retVal.setSummary(
							String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
									adcInfo.getConnPort().toString()));
					return retVal;
				}
			}
			if (adcInfo.getConnProtocol() == OBDefine.PROTOCOL_ICMP) {
				if (OBNetwork.icmpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(),
						MAX_WAIT_TIME_MSEC) == false) {
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
							String.format("icmp (%s) unreachable", adcInfo.getAdcIpAddress()));
					retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
					retVal.setSummary(
							String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
									adcInfo.getConnPort().toString()));
					return retVal;
				}
			} else {
				if (OBNetwork.portTcpIsOpen(adcInfo.getAdcIpAddress(), HTTPS_PORT_NUM, MAX_WAIT_TIME_MSEC) == false) {
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
							String.format("tcp port(%s:%d) unreachable", adcInfo.getAdcIpAddress(), HTTPS_PORT_NUM));
					retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
					retVal.setSummary(
							String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
									adcInfo.getConnPort().toString()));
					return retVal;
				}
			}

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_PORTOPEN);
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check open port(%s:%d)-illegal null pointer error",
							adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

//	private OBAdcCheckResult checkADCStatusPortOpenPAS(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{		
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTOPEN);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//		
//		// port open 테스트. adcInfo의 connPort 에 입력된 정보의 포트 오픈 여부를 확인한다.
//		try
//		{
//			if(OBNetwork.portTcpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(), MAX_WAIT_TIME_MSEC)==false)
//			{
//				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("tcp port(%s:%d) unreachable", adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), adcInfo.getConnPort().toString()));
//				return retVal;
//			}
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_PORTOPEN, db);
//		}
//		catch(Exception e)
//		{
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check open port(%s:%d)-illegal null pointer error", adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusPortOpenPAS(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTOPEN);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		// port open 테스트. adcInfo의 connPort 에 입력된 정보의 포트 오픈 여부를 확인한다.
		try {
			if (adcInfo.getConnProtocol() == OBDefine.PROTOCOL_ICMP) {
				if (OBNetwork.icmpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(),
						MAX_WAIT_TIME_MSEC) == false) {
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
							String.format("icmp (%s) unreachable", adcInfo.getAdcIpAddress()));
					retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
					retVal.setSummary(
							String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
									adcInfo.getConnPort().toString()));
					return retVal;
				}
			} else {
				if (OBNetwork.portTcpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(),
						MAX_WAIT_TIME_MSEC) == false) {
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("tcp port(%s:%d) unreachable",
							adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
					retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
					retVal.setSummary(
							String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
									adcInfo.getConnPort().toString()));
					return retVal;
				}
			}
			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_PORTOPEN);
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check open port(%s:%d)-illegal null pointer error",
							adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

//	private OBAdcCheckResult checkADCStatusPortOpenPASK(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTOPEN);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//		
//		// port open 테스트. adcInfo의 connPort 에 입력된 정보의 포트 오픈 여부를 확인한다.
//		try
//		{
//			if(OBNetwork.portTcpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(), MAX_WAIT_TIME_MSEC)==false)
//			{
//				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("tcp port(%s:%d) unreachable", adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), adcInfo.getConnPort().toString()));
//				return retVal;
//			}
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_PORTOPEN, db);
//		}
//		catch(Exception e)
//		{
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check open port(%s:%d)-illegal null pointer error", adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusPortOpenPASK(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTOPEN);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		// port open 테스트. adcInfo의 connPort 에 입력된 정보의 포트 오픈 여부를 확인한다.
		try {
			if (adcInfo.getConnProtocol() == OBDefine.PROTOCOL_ICMP) {
				if (OBNetwork.icmpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(),
						MAX_WAIT_TIME_MSEC) == false) {
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
							String.format("icmp (%s) unreachable", adcInfo.getAdcIpAddress()));
					retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
					retVal.setSummary(
							String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
									adcInfo.getConnPort().toString()));
					return retVal;
				}
			} else {
				if (OBNetwork.portTcpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(),
						MAX_WAIT_TIME_MSEC) == false) {
					OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("tcp port(%s:%d) unreachable",
							adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
					retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
					retVal.setSummary(
							String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
									adcInfo.getConnPort().toString()));
					return retVal;
				}
			}

			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_PORTOPEN);
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check open port(%s:%d)-illegal null pointer error",
							adcInfo.getAdcIpAddress(), adcInfo.getConnPort()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

//	private OBAdcCheckResult checkADCStatusPortOpenPASUnknown(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTOPEN);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//		
//		// port open 테스트. adcInfo의 connPort 에 입력된 정보의 포트 오픈 여부를 확인한다.
//		try
//		{
//			if(OBNetwork.portTcpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(), MAX_WAIT_TIME_MSEC)==false)
//			{
//				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("tcp port(%d) unreachable", adcInfo.getConnPort()));
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), adcInfo.getConnPort().toString()));
//				return retVal;
//			}
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_PORTOPEN, db);
//		}
//		catch(Exception e)
//		{
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check open port(%d)-illegal null pointer error", adcInfo.getConnPort()));
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusPortOpenPASUnknown(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTOPEN);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		// port open 테스트. adcInfo의 connPort 에 입력된 정보의 포트 오픈 여부를 확인한다.
		try {
			if (OBNetwork.portTcpIsOpen(adcInfo.getAdcIpAddress(), adcInfo.getConnPort(),
					MAX_WAIT_TIME_MSEC) == false) {
				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
						String.format("tcp port(%d) unreachable", adcInfo.getConnPort()));
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
						adcInfo.getConnPort().toString()));
				return retVal;
			}
			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_PORTOPEN);
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check open port(%d)-illegal null pointer error", adcInfo.getConnPort()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

//	private OBAdcCheckResult checkADCStatusLogin(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		switch (adcInfo.getAdcType())
//		{
//		case OBDefine.ADC_TYPE_ALTEON:
//			retVal = checkADCStatusLoginAlteon(adcInfo);
//			break;
//		case OBDefine.ADC_TYPE_F5:
//			retVal = checkADCStatusLoginF5(adcInfo, db);
//			break;
//		case OBDefine.ADC_TYPE_PIOLINK_PAS:
//			retVal = checkADCStatusLoginPAS(adcInfo, db);
//			break;
//		case OBDefine.ADC_TYPE_PIOLINK_PASK:
//			retVal = checkADCStatusLoginPASK(adcInfo, db);
//			break;
//		case OBDefine.ADC_TYPE_PIOLINK_UNKNOWN:
//			retVal = checkADCStatusLoginPAS(adcInfo, db);// 로그인의 경우에 PAS와 동일한 절차로 진행한다.
//			break;
//		default:
//			throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);
//		}
//	
//		return retVal;
//	}
//	
	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusLogin(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		switch (adcInfo.getAdcType()) {
		case OBDefine.ADC_TYPE_ALTEON:
			retVal = checkADCStatusLoginAlteon(adcInfo);
			break;
		case OBDefine.ADC_TYPE_F5:
			retVal = checkADCStatusLoginF5(adcInfo);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_PAS:
			retVal = checkADCStatusLoginPAS(adcInfo);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_PASK:
			retVal = checkADCStatusLoginPASK(adcInfo);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_UNKNOWN:
			retVal = checkADCStatusLoginPAS(adcInfo);// 로그인의 경우에 PAS와 동일한 절차로 진행한다.
			break;
		default:
			throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);
		}

		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(8);
////			HashMap<String, OBDtoAdcInfo> list = new OBAdcManagementImpl().getAdcInfoBasicMap(db);
//			OBAdcCheckResult list = new OBAdcManagementImpl().checkADCStatusLoginPASK(adcInfo, db);
////			OBAdcCheckResult list = new OBAdcManagementImpl().checkADCStatusLoginAlteon(adcInfo, db);
//			System.out.println(list);
////			for(OBDtoAdcInfo info:list)
////				System.out.println(info);
//			db.closeDB();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	private OBAdcCheckResult checkADCStatusLoginAlteon(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_LOGIN);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//		
//		// 로그인 테스트
//		OBAdcAlteonHandler handler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
//		try
//		{
//			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
//			handler.login();
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN, db);
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-unreachable. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), adcInfo.getConnPort().toString()));
//		}
//		catch(OBExceptionLogin e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-invalid id/passwd. ip:%s, id:%s, passwd:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, adcInfo.toString());
//		}
//		finally
//		{
//			handler.disconnect();			
//		}
//
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusLoginAlteon(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_LOGIN);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		// 로그인 테스트
		OBAdcAlteonHandler handler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
		try {
			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
			handler.login();
			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN);
		} catch (OBExceptionUnreachable e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-unreachable. ip:%s, id:%s",
					adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
					adcInfo.getConnPort().toString()));
		} catch (OBExceptionLogin e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check login-invalid id/passwd. ip:%s, id:%s, passwd:%s",
							adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, adcInfo.toString());
		} finally {
			handler.disconnect();
		}

		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(19);
////			HashMap<String, OBDtoAdcInfo> list = new OBAdcManagementImpl().getAdcInfoBasicMap(db);
//			OBAdcCheckResult list = new OBAdcManagementImpl().checkADCStatusLoginF5(adcInfo, db);
////			OBAdcCheckResult list = new OBAdcManagementImpl().checkADCStatusLoginAlteon(adcInfo, db);
//			System.out.println(list);
////			for(OBDtoAdcInfo info:list)
////				System.out.println(info);
//			db.closeDB();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	private OBAdcCheckResult checkADCStatusLoginF5(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_LOGIN);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//		
//		// CLI 로그인 테스트
//		OBAdcF5Handler handler = OBCommon.getValidF5Handler(adcInfo.getSwVersion());
//		try
//		{
//			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(), adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnPort());
//            handler.sshLogin();
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-unreachable. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), adcInfo.getConnPort().toString()));
//			return retVal;
//		}
//		catch(OBExceptionLogin e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
//			return retVal;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//		}	
//		handler.disconnect();
//		
//		// icontroll 로그인 확인.
//		try
//		{
//			iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt()); 
//			SystemF5.checkInterface(interfaces);
//		}
//		catch(Exception e)
//		{
//			try
//			{
//				CommonF5.Exception("", e.getMessage());
//				// 체크 시간을 기록함.
//				updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN, db);
//			}
//			catch(OBExceptionUnreachable e1)
//			{
//				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login(icontroll)-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), HTTPS_PORT_NUM));
//			}
//			catch(OBExceptionLogin e1)
//			{
//				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login(icontroll)-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
//			}
//			catch(OBException e1)
//			{// 일반적인 프로그램 오류.
//				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//			}
//			catch(Exception e1)
//			{// 일반적인 프로그램 오류.
//				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//			}
//			// error 
//		}
//
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusLoginF5(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_LOGIN);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		if (adcInfo.getOpMode() != OBDefine.OP_MODE_MONITORING) {
			// CLI 로그인 테스트
			OBAdcF5Handler handler = OBCommon.getValidF5Handler(adcInfo.getSwVersion());
			try {
				handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(),
						adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnPort());
				handler.sshLogin();
			} catch (OBExceptionUnreachable e) {
				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to check login-unreachable. ip:%s, id:%s", adcInfo.getAdcIpAddress(),
								adcInfo.getAdcCliAccount()));
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
						adcInfo.getConnPort().toString()));
				return retVal;
			} catch (OBExceptionLogin e) {
				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to check login-invalid id/passwd. ip:%s, id:%s",
								adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount()));
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
				return retVal;
			} catch (Exception e) {// 일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
			}
			handler.disconnect();
		}

		// icontroll 로그인 확인.
		try {
			iControl.Interfaces interfaces = CommonF5.initInterfaces(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt());
			SystemF5.checkInterface(interfaces);
		} catch (Exception e) {
			try {
				CommonF5.Exception("", e.getMessage());
				// 체크 시간을 기록함.
				updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN);
			} catch (OBExceptionUnreachable e1) {
				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to check login(icontroll)-invalid id/passwd. ip:%s, id:%s",
								adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
						HTTPS_PORT_NUM));
			} catch (OBExceptionLogin e1) {
				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to check login(icontroll)-invalid id/passwd. ip:%s, id:%s",
								adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
			} catch (OBException e1) {// 일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
			} catch (Exception e1) {// 일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
			}
			// error
		}

		return retVal;
	}

//	private OBAdcCheckResult checkADCStatusLoginPAS(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_LOGIN);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//		
//		// 로그인 테스트
//		OBAdcPASHandler handler = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
//		try
//		{
//			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
//			handler.login();
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN, db);
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-unreachable. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), adcInfo.getConnPort().toString()));
//		}
//		catch(OBExceptionLogin e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//		}	
//		handler.disconnect();
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusLoginPAS(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_LOGIN);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		// 로그인 테스트
		OBAdcPASHandler handler = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
		try {
			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
			handler.login();
			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN);
		} catch (OBExceptionUnreachable e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-unreachable. ip:%s, id:%s",
					adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
					adcInfo.getConnPort().toString()));
		} catch (OBExceptionLogin e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check login-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(),
							adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		handler.disconnect();
		return retVal;
	}

//	private OBAdcCheckResult checkADCStatusLoginPASK(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_LOGIN);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//		
//		// 로그인 테스트
//		OBAdcPASKHandler handler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
//		try
//		{
//			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
//			handler.login();
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN, db);
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-unreachable. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), adcInfo.getConnPort().toString()));
//		}
//		catch(OBExceptionLogin e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//		}	
//		handler.disconnect();
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusLoginPASK(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_LOGIN);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		// 로그인 테스트
		OBAdcPASKHandler handler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
		try {
			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
			handler.login();
			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN);
		} catch (OBExceptionUnreachable e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-unreachable. ip:%s, id:%s",
					adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
					adcInfo.getConnPort().toString()));
		} catch (OBExceptionLogin e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check login-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(),
							adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		handler.disconnect();
		return retVal;
	}

//	private OBAdcCheckResult checkADCStatusVersion(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		switch (adcInfo.getAdcType())
//		{
//		case OBDefine.ADC_TYPE_ALTEON:
//			retVal = checkADCStatusVersionAlteon(adcInfo, db);
//			break;
//		case OBDefine.ADC_TYPE_F5:
//			retVal = checkADCStatusVersionF5(adcInfo, db);
//			break;
//		case OBDefine.ADC_TYPE_PIOLINK_PAS:
//			retVal = checkADCStatusVersionPAS(adcInfo, db);
//			break;
//		case OBDefine.ADC_TYPE_PIOLINK_PASK:
//			retVal = checkADCStatusVersionPASK(adcInfo, db);
//			break;
//		case OBDefine.ADC_TYPE_PIOLINK_UNKNOWN:
//			retVal = checkADCStatusVersionPASUnknown(adcInfo, db);
//			break;
//		default:
//			throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);
//		}
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusVersion(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		switch (adcInfo.getAdcType()) {
		case OBDefine.ADC_TYPE_ALTEON:
			retVal = checkADCStatusVersionAlteon(adcInfo);
			break;
		case OBDefine.ADC_TYPE_F5:
			retVal = checkADCStatusVersionF5(adcInfo);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_PAS:
			retVal = checkADCStatusVersionPAS(adcInfo);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_PASK:
			retVal = checkADCStatusVersionPASK(adcInfo);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_UNKNOWN:
			retVal = checkADCStatusVersionPASUnknown(adcInfo);
			break;
		default:
			throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);
		}
		return retVal;
	}

//	private OBAdcCheckResult checkADCStatusVersionAlteon(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//		
//		// 로그인 테스트
//		OBAdcAlteonHandler handler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
//		try
//		{
//			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
//			handler.login();
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN, db);
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-unreachable. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), adcInfo.getConnPort().toString()));
//			handler.disconnect();
//			return retVal;
//		}
//		catch(OBExceptionLogin e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
//			handler.disconnect();
//			return retVal;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류
//			handler.disconnect();
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//		}	
//		
//		// version 확인.
//		try
//		{
//			String cmdResult = handler.cmndSysGeneral();
//			String swVersion = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion()).parseSwVersion(cmdResult);
//			if(OBCommon.checkVersionAlteon(swVersion)==false)
//			{// 지원하지 않은 버전인 경우..
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_UNSUPPORT_VERSION), swVersion));
//			}
//			else
//			{
//				// 체크 시간을 기록함.
//				updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_VERSION, db);
//			}
//			retVal.setExtraInfo(swVersion);
//		}
//		catch(OBException e)
//		{// 일반적인 프로그램 오류.
//			handler.disconnect();
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check sw version. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			throw e;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			handler.disconnect();
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check sw version-illegal null pointer error. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//
//		handler.disconnect();
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusVersionAlteon(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		// 로그인 테스트
		OBAdcAlteonHandler handler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
		try {
			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
			handler.login();
			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN);
		} catch (OBExceptionUnreachable e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-unreachable. ip:%s, id:%s",
					adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
					adcInfo.getConnPort().toString()));
			handler.disconnect();
			return retVal;
		} catch (OBExceptionLogin e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check login-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(),
							adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
			handler.disconnect();
			return retVal;
		} catch (Exception e) {// 일반적인 프로그램 오류
			handler.disconnect();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}

		// version 확인.
		try {
			String cmdResult = handler.cmndSysGeneral();
			String swVersion = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion()).parseSwVersion(cmdResult);
			if (OBCommon.checkVersionAlteon(swVersion) == false) {// 지원하지 않은 버전인 경우..
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(String
						.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_UNSUPPORT_VERSION), swVersion));
			} else {
				// 체크 시간을 기록함.
				updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_VERSION);
			}
			retVal.setExtraInfo(swVersion);
		} catch (OBException e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check sw version. ip:%s, id:%s",
					adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check sw version-illegal null pointer error. ip:%s, id:%s",
							adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		handler.disconnect();
		return retVal;
	}
//	
//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(19);
////			HashMap<String, OBDtoAdcInfo> list = new OBAdcManagementImpl().getAdcInfoBasicMap(db);
//			OBAdcCheckResult list = new OBAdcManagementImpl().checkADCStatusVersionF5(adcInfo, db);
////			OBAdcCheckResult list = new OBAdcManagementImpl().checkADCStatusLoginAlteon(adcInfo, db);
//			System.out.println(list);
////			for(OBDtoAdcInfo info:list)
////				System.out.println(info);
//			db.closeDB();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	private OBAdcCheckResult checkADCStatusVersionF5(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//
//		// icontroll 로그인하여 버전을 확인한다.
//		iControl.Interfaces interfaces = null;
//		try
//		{
//			interfaces = CommonF5.initInterfaces(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt()); 
//			SystemF5.checkInterface(interfaces);
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN, db);			
//		}
//		catch(Exception e)
//		{
//			try
//			{
//				CommonF5.Exception("", e.getMessage());
//			}
//			catch(OBExceptionUnreachable e1)
//			{
//				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login(icontroll)-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), HTTPS_PORT_NUM));
//				return retVal;
//			}
//			catch(OBExceptionLogin e1)
//			{
//				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login(icontroll)-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
//				return retVal;
//			}
//			catch(OBException e1)
//			{// 일반적인 프로그램 오류.
//				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//			}
//			catch(Exception e1)
//			{// 일반적인 프로그램 오류.
//				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//			}
//			// error 
//		}
//	
//		// version 확인.
//		try
//		{
//			String swVersion = SystemF5.getProductVersion(interfaces);
//			if(OBCommon.checkVersionF5(swVersion)==false)
//			{// 지원하지 않은 버전인 경우..
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_UNSUPPORT_VERSION), swVersion));
//			}
//			else
//			{
//				// 체크 시간을 기록함.
//				updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_VERSION, db);				
//			}
//			retVal.setExtraInfo(swVersion);
//		}
//		catch(OBException e)
//		{// 일반적인 프로그램 오류.
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check sw version. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			throw e;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check sw version-illegal null pointer error. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//
//		return retVal;
//	}
//	
	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusVersionF5(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		// icontroll 로그인하여 버전을 확인한다.
		iControl.Interfaces interfaces = null;
		try {
			interfaces = CommonF5.initInterfaces(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt());
			SystemF5.checkInterface(interfaces);
			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN);
		} catch (Exception e) {
			try {
				CommonF5.Exception("", e.getMessage());
			} catch (OBExceptionUnreachable e1) {
				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to check login(icontroll)-invalid id/passwd. ip:%s, id:%s",
								adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
						HTTPS_PORT_NUM));
				return retVal;
			} catch (OBExceptionLogin e1) {
				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to check login(icontroll)-invalid id/passwd. ip:%s, id:%s",
								adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
				return retVal;
			} catch (OBException e1) {// 일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
			} catch (Exception e1) {// 일반적인 프로그램 오류.
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
			}
			// error
		}

		// version 확인.
		try {
			String swVersion = SystemF5.getProductVersion(interfaces);
			if (OBCommon.checkVersionF5(swVersion) == false) {// 지원하지 않은 버전인 경우..
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(String
						.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_UNSUPPORT_VERSION), swVersion));
			} else {
				// 체크 시간을 기록함.
				updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_VERSION);
			}
			retVal.setExtraInfo(swVersion);
		} catch (OBException e) {// 일반적인 프로그램 오류.
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check sw version. ip:%s, id:%s",
					adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check sw version-illegal null pointer error. ip:%s, id:%s",
							adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return retVal;
	}

//	private OBAdcCheckResult checkADCStatusVersionPAS(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//		
//		// 로그인 테스트
//		OBAdcPASHandler handler = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
//		try
//		{
//			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
//			handler.login();
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN, db);					
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-unreachable. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), adcInfo.getConnPort().toString()));
//			return retVal;
//		}
//		catch(OBExceptionLogin e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
//			return retVal;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//		}	
//		
//		// version 확인.
//		try
//		{
//			String cmdResult = handler.cmndSystem();
//			String swVersion = OBCommon.getValidPASCLIParser(adcInfo.getSwVersion()).parseSwVersion(cmdResult);
//			if(OBCommon.checkVersionPAS(swVersion)==false)
//			{// 지원하지 않은 버전인 경우..
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_UNSUPPORT_VERSION), swVersion));
//			}
//			else
//			{
//				// 체크 시간을 기록함.
//				updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_VERSION, db);				
//			}
//			retVal.setExtraInfo(swVersion);
//		}
//		catch(OBException e)
//		{// 일반적인 프로그램 오류.
//			handler.disconnect();
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check sw version. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			throw e;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			handler.disconnect();
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check sw version-illegal null pointer error. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//
//		handler.disconnect();
//		return retVal;
//	}
//	
	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusVersionPAS(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		// 로그인 테스트
		OBAdcPASHandler handler = OBCommon.getValidPASHandler(adcInfo.getSwVersion());
		try {
			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
			handler.login();
			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN);
		} catch (OBExceptionUnreachable e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-unreachable. ip:%s, id:%s",
					adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
					adcInfo.getConnPort().toString()));
			return retVal;
		} catch (OBExceptionLogin e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check login-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(),
							adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
			return retVal;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}

		// version 확인.
		try {
			String cmdResult = handler.cmndSystem();
			String swVersion = OBCommon.getValidPASCLIParser(adcInfo.getSwVersion()).parseSwVersion(cmdResult);
			if (OBCommon.checkVersionPAS(swVersion) == false) {// 지원하지 않은 버전인 경우..
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(String
						.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_UNSUPPORT_VERSION), swVersion));
			} else {
				// 체크 시간을 기록함.
				updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_VERSION);
			}
			retVal.setExtraInfo(swVersion);
		} catch (OBException e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check sw version. ip:%s, id:%s",
					adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check sw version-illegal null pointer error. ip:%s, id:%s",
							adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		handler.disconnect();
		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(8);
////			HashMap<String, OBDtoAdcInfo> list = new OBAdcManagementImpl().getAdcInfoBasicMap(db);
//			OBAdcCheckResult list = new OBAdcManagementImpl().checkADCStatusVersionPAS(adcInfo, db);
////			OBAdcCheckResult list = new OBAdcManagementImpl().checkADCStatusLoginAlteon(adcInfo, db);
//			System.out.println(list);
////			for(OBDtoAdcInfo info:list)
////				System.out.println(info);
//			db.closeDB();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//	private OBAdcCheckResult checkADCStatusVersionPASUnknown(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//		
//		// PAS, PASK가 지정되어 있지 않은 상태에서 버전을 검사한다. 각각 접속 후 성공한 버전을 버전으로 간주한다.
//		OBAdcCheckResult pasRetval = null;
//		// PAS 버전 검사. 
//		try
//		{
//			pasRetval = checkADCStatusVersionPAS(adcInfo, db);
//		}
//		catch(OBException e)
//		{
//			throw e;
//		}	
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}	
//		
//		OBAdcCheckResult paskRetval = null;
//		try
//		{
//			paskRetval = checkADCStatusVersionPASK(adcInfo, db);
//		}
//		catch(OBException e)
//		{
//			throw e;
//		}	
//		catch(Exception e)
//		{
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}	
//		
//		if(pasRetval!=null && !pasRetval.getExtraInfo().isEmpty())
//		{// getExtraInfo()에 정보가 있으면 버전 추출에 성공한 경우로 간주한다.
//			return pasRetval;
//		}
//
//		if(paskRetval!=null && !paskRetval.getExtraInfo().isEmpty())
//		{// getExtraInfo()에 정보가 있으면 버전 추출에 성공한 경우로 간주한다.
//			return paskRetval;
//		}
//
//		// pas/pask 둘다 실패한 경우임. 0.0.0.0으로 설정하여 제공하도록 함.
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//		retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_UNSUPPORT_VERSION), "0.0.0.0"));
//		retVal.setExtraInfo("0.0.0.0");
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusVersionPASUnknown(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		// PAS, PASK가 지정되어 있지 않은 상태에서 버전을 검사한다. 각각 접속 후 성공한 버전을 버전으로 간주한다.
		OBAdcCheckResult pasRetval = null;
		// PAS 버전 검사.
		try {
			pasRetval = checkADCStatusVersionPAS(adcInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBAdcCheckResult paskRetval = null;
		try {
			paskRetval = checkADCStatusVersionPASK(adcInfo);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		if (pasRetval != null && !pasRetval.getExtraInfo().isEmpty()) {// getExtraInfo()에 정보가 있으면 버전 추출에 성공한 경우로 간주한다.
			return pasRetval;
		}

		if (paskRetval != null && !paskRetval.getExtraInfo().isEmpty()) {// getExtraInfo()에 정보가 있으면 버전 추출에 성공한 경우로 간주한다.
			return paskRetval;
		}

		// pas/pask 둘다 실패한 경우임. 0.0.0.0으로 설정하여 제공하도록 함.
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
		retVal.setSummary(
				String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_UNSUPPORT_VERSION), "0.0.0.0"));
		retVal.setExtraInfo("0.0.0.0");
		return retVal;
	}

//	private OBAdcCheckResult checkADCStatusVersionPASK(OBDtoAdcInfo adcInfo, OBDatabase db) throws OBException
//	{
//		OBAdcCheckResult retVal = new OBAdcCheckResult();
//		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
//		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
//		retVal.setSummary("");
//		
//		// 로그인 테스트
//		OBAdcPASKHandler handler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
//		try
//		{
//			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
//			handler.login();
//			// 체크 시간을 기록함.
//			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN, db);				
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-unreachable. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE), adcInfo.getConnPort().toString()));
//			return retVal;
//		}
//		catch(OBExceptionLogin e)
//		{
//			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
//			return retVal;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
//		}	
//		
//		// version 확인.
//		try
//		{
//			String cmdResult = handler.cmndSystem();
//			String swVersion = OBCommon.getValidPASKCLIParser(adcInfo.getSwVersion()).parseSwVersion(cmdResult);
//			if(OBCommon.checkVersionPASK(swVersion)==false)
//			{// 지원하지 않은 버전인 경우..
//				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
//				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_UNSUPPORT_VERSION), swVersion));
//			}
//			else
//			{
//				// 체크 시간을 기록함.
//				updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_VERSION, db);				
//			}
//			retVal.setExtraInfo(swVersion);
//		}
//		catch(OBException e)
//		{// 일반적인 프로그램 오류.
//			handler.disconnect();
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check sw version. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			throw e;
//		}
//		catch(Exception e)
//		{// 일반적인 프로그램 오류.
//			handler.disconnect();
//			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check sw version-illegal null pointer error. ip:%s, id:%s", adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
//		}
//
//		handler.disconnect();
//		return retVal;
//	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	private OBAdcCheckResult checkADCStatusVersionPASK(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_VERSION);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		// 로그인 테스트
		OBAdcPASKHandler handler = OBCommon.getValidPASKHandler(adcInfo.getSwVersion());
		try {
			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());
			handler.login();
			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN);
		} catch (OBExceptionUnreachable e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("failed to check login-unreachable. ip:%s, id:%s",
					adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_PORTOPEN_FAILURE),
					adcInfo.getConnPort().toString()));
			return retVal;
		} catch (OBExceptionLogin e) {
			OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check login-invalid id/passwd. ip:%s, id:%s", adcInfo.getAdcIpAddress(),
							adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_LOGIN_FAILURE)));
			return retVal;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}

		// version 확인.
		try {
			String cmdResult = handler.cmndSystem();
			String swVersion = OBCommon.getValidPASKCLIParser(adcInfo.getSwVersion()).parseSwVersion(cmdResult);
			if (OBCommon.checkVersionPASK(swVersion) == false) {// 지원하지 않은 버전인 경우..
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(String
						.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_UNSUPPORT_VERSION), swVersion));
			} else {
				// 체크 시간을 기록함.
				updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_VERSION);
			}
			retVal.setExtraInfo(swVersion);
		} catch (OBException e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to check sw version. ip:%s, id:%s",
					adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			handler.disconnect();
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check sw version-illegal null pointer error. ip:%s, id:%s",
							adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		handler.disconnect();
		return retVal;
	}

	private OBAdcCheckResult checkADCStatusReversePortOpen(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		switch (adcInfo.getAdcType()) {
		case OBDefine.ADC_TYPE_ALTEON:
			retVal = checkADCStatusReversePortOpenAlteon(adcInfo);
			break;
		case OBDefine.ADC_TYPE_F5:
			retVal = checkADCStatusReversePortOpenF5(adcInfo);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_PAS:
			retVal = checkADCStatusReversePortOpenPAS(adcInfo);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_PASK:
			retVal = checkADCStatusReversePortOpenPASK(adcInfo);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_UNKNOWN:
			retVal = checkADCStatusReversePortOpenPASUnknown(adcInfo);
			break;
		default:
			throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);
		}
		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(10);
//			OBAdcCheckResult list = new OBCheckAdcStatus().checkADCStatusReversePortOpenAlteon(adcInfo, db);
//
//			System.out.println(list);
//
//			db.closeDB();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

//    public static void main(String[] args)
//    {
//        try
//        {
//            OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(6);
//            new OBCheckAdcStatus().checkADCStatusReversePortOpenAlteon(adcInfo);
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//    
	private OBAdcCheckResult checkADCStatusReversePortOpenAlteon(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTREVERSE);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		OBAdcAlteonHandler handler = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());

		// port open 테스트(역방향) alteon만 적용됨.
		try {
			// adcsmart 호스트에 ip가 여러개 존재할 수 있으므로 순환하여 검사한다.
			ArrayList<String> ipAddressList = OBNetwork.getAllIPAddress();
			ArrayList<String> adcsmartIpAddressList = new ArrayList<String>();
			String checkIP = "";
			int isReachable = OBDefine.STATUS_UNREACHABLE;
			handler.login();

			String input = handler.cmndInfoCurrntUser();
			adcsmartIpAddressList = OBParser.getAdcsmartIP(input);
			if (adcsmartIpAddressList == null || adcsmartIpAddressList.isEmpty()) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("ReversePortOpenAlteon adcsmartList ip null. ip:%s, id:%s",
								adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(
						String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_REVERSEPORTOPEN_FAILURE)));
				return retVal;
			}
			for (String ipAddress : ipAddressList) {
				for (String adcsmartIP : adcsmartIpAddressList) {
					if (ipAddress.equals(adcsmartIP)) {
						checkIP = adcsmartIP;
					}
				}
			}
			if (checkIP == "") {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("ReversePortOpenAlteon adcsmart ip null. ip:%s, id:%s", adcInfo.getAdcIpAddress(),
								adcInfo.getAdcAccount()));
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(
						String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_REVERSEPORTOPEN_FAILURE)));
				return retVal;
			}

			isReachable = handler.isAdcsmartReachable(checkIP, SSH_PORT_NUM, adcInfo.getMgmtMode());
			if (isReachable == OBDefine.STATUS_REACHABLE)
				isReachable = OBDefine.STATUS_REACHABLE;
			else
				OBSystemLog.warn(OBDefine.LOGFILE_SYSTEM, String.format("host %s:%d unreachable. adcip:%s", checkIP,
						SSH_PORT_NUM, adcInfo.getAdcIpAddress()));
			handler.disconnect();

			if (isReachable == OBDefine.STATUS_UNREACHABLE) {
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(
						String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_REVERSEPORTOPEN_FAILURE)));
				return retVal;
			}
			// 체크 시간을 기록함.
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_LOGIN);
			updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_PORTREVERSE);
		} catch (OBExceptionUnreachable e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check adcsmart port open from ADC(unreachable). ip:%s, id:%s",
							adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(
					String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_REVERSEPORTOPEN_FAILURE),
							adcInfo.getConnPort().toString()));
			return retVal;
		} catch (OBExceptionLogin e) {// 로그인 실패한 경우임.
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check adcsmart port open from ADC(login fail). ip:%s, id:%s",
							adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(
					String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_REVERSEPORTOPEN_FAILURE)));
			return retVal;
		} catch (OBException e) {// 일반적인 프로그램 오류.
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check adcsmart port open from ADC. ip:%s, id:%s",
							adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(
					String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_REVERSEPORTOPEN_FAILURE)));
			return retVal;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check adcsmart port open from ADC(illegal null pointer). ip:%s, id:%s",
							adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			handler.disconnect();
		}
		return retVal;
	}

	private OBAdcCheckResult checkADCStatusReversePortOpenF5(OBDtoAdcInfo adcInfo) throws OBException {// 테스트 하지 않음.
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTREVERSE);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");
		return retVal;
	}

	private OBAdcCheckResult checkADCStatusReversePortOpenPAS(OBDtoAdcInfo adcInfo) throws OBException {// 테스트 하지 않음.
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTREVERSE);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");
		return retVal;
	}

	private OBAdcCheckResult checkADCStatusReversePortOpenPASK(OBDtoAdcInfo adcInfo) throws OBException {// 테스트 하지 않음.
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTREVERSE);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");
		return retVal;
	}

	private OBAdcCheckResult checkADCStatusReversePortOpenPASUnknown(OBDtoAdcInfo adcInfo) throws OBException {// 테스트 하지
																												// 않음.
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_PORTREVERSE);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");
		return retVal;
	}

	private OBAdcCheckResult checkADCStatusSnmp(OBDtoAdcInfo adcInfo) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_SNMP);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");

		try {
			String version = null;
			switch (adcInfo.getAdcType()) {
			case OBDefine.ADC_TYPE_ALTEON:
				version = new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
						.getAdcSWVersion(OBDefine.ADC_TYPE_ALTEON, null);
				break;
			case OBDefine.ADC_TYPE_F5:
				version = new OBSnmpF5(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
						.getAdcSWVersion(OBDefine.ADC_TYPE_F5, null, null);
				break;
			case OBDefine.ADC_TYPE_PIOLINK_PAS:
				version = new OBSnmpPAS(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
						.getAdcSWVersion(OBDefine.ADC_TYPE_PIOLINK_PAS, null);
				break;
			case OBDefine.ADC_TYPE_PIOLINK_PASK:
				// FIXME: PASK는 버전체크가 없음. 따라서 기존과 같은 방식으로 처리한다.(익셉션만 안나면 OK)
				new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
						.getAdcSWVersion(OBDefine.ADC_TYPE_PIOLINK_PAS, null);
				version = "dummy";
				break;
			case OBDefine.ADC_TYPE_PIOLINK_UNKNOWN:
				version = new OBSnmpPAS(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
						.getAdcSWVersion(OBDefine.ADC_TYPE_PIOLINK_PAS, null);
				break;
			case OBDefine.ADC_TYPE_UNKNOWN:
				// FIXME: UNKNOWN은 버전체크를 할 수 없음. 따라서 기존과 같은 방식으로 처리한다.(익셉션만 안나면 OK)
				new OBSnmpAlteon(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
						.getAdcSWVersion(OBDefine.ADC_TYPE_PIOLINK_PAS, null);
				version = "dummy";
				break;
			default:
			}
			if (version == null || version.isEmpty()) // 버전을 가져오지 못한다면 실패
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
		} catch (OBException e) {// snmp 쿼리에 실패하여 응답이 없는 경우에 발생됨. 따라서 exception을 전달하지 않음.
			retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
			retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_QUERY_SNMP)));
			return retVal;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
					String.format("failed to check snmp access-illegal null pointer error. ip:%s, id:%s",
							adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount()));
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		// 체크 시간을 기록함.
		updateLastAdcCheckTime(adcInfo.getIndex(), OBAdcCheckResult.CHECK_ID_SNMP);
		return retVal;
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(8);
////			HashMap<String, OBDtoAdcInfo> list = new OBAdcManagementImpl().getAdcInfoBasicMap(db);
//			OBAdcCheckResult list = new OBAdcManagementImpl().checkADCStatusSyslog(adcInfo, db);
////			OBAdcCheckResult list = new OBAdcManagementImpl().checkADCStatusLoginAlteon(adcInfo, db);
//			System.out.println(list);
////			for(OBDtoAdcInfo info:list)
////				System.out.println(info);
//			db.closeDB();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	private Integer MAX_DIFF_TIME_SYSLOG = 600000;

	public OBAdcCheckResult checkADCStatusSyslog(OBDtoAdcInfo adcInfo, boolean isNewADC) throws OBException {
		OBAdcCheckResult retVal = new OBAdcCheckResult();
		retVal.setCheckID(OBAdcCheckResult.CHECK_ID_SYSLOG);
		retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_OK);
		retVal.setSummary("");
		// syslog 수신 확인. 최근 5분간 입력된 syslog 또는 감사로그를 조사한다.
		try {
			Timestamp lastTime = getLastSyslogReceviedTime(adcInfo, isNewADC);
			if (lastTime == null || adcInfo.getAdcIpAddress() == null || adcInfo.getAdcIpAddress().isEmpty()) {// 수신안된
																												// 경우.
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				if (isNewADC == false) {
					retVal.setSummary(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_RCV_SYSLOG_NO));
					return retVal;
				}

				// 등록되지 않은 상태에서 검사하는 경우. 감사로그에 미등록 로그가 있는지 확인한다.
				ArrayList<String> hostList = getLastUnregisteredSyslog(adcInfo);
				String hostContent = "";
				for (String host : hostList) {
					if (!hostContent.isEmpty())
						hostContent += ", ";
					hostContent += host;
				}
				if (hostContent.isEmpty())
					retVal.setSummary(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_RCV_SYSLOG_NO));
				else
					retVal.setSummary(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_RCV_SYSLOG_NO)
							+ String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_RCV_SYSLOG_INFO),
									hostContent));
				return retVal;
			}

			// 시간차이를 검사한다. 10분 이상 차이가 나면 false 간주한다.
			Timestamp nowTime = OBDateTime.toTimestamp(OBDateTime.now());
			if (nowTime.getTime() - lastTime.getTime() > MAX_DIFF_TIME_SYSLOG) {
				retVal.setStatus(OBAdcCheckResult.CHECK_STATUS_FAIL);
				retVal.setSummary(String.format(OBMessages.getMessage(OBMessages.MSG_ADC_CHECK_STATUS_RCV_SYSLOG_TIME),
						MAX_DIFF_TIME_SYSLOG / (1000 * 60), lastTime.toString()));
				return retVal;
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {// 일반적인 프로그램 오류.
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return retVal;
	}

	public void updateLastAdcCheckTime(Integer adcIndex, ArrayList<OBDtoLastAdcCheckTime> infoList, OBDatabase db)
			throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT COUNT(*) AS CNT  			\n" + " FROM TMP_LAST_ADC_CHECK_TIME 		\n"
					+ " WHERE ADC_INDEX=%d					\n", adcIndex);
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false) {
				return;
			}
			if (db.getInteger(rs, "CNT") > 0) {// 데이터가 있는 경우. update한다.
				String updateText = "";
				for (OBDtoLastAdcCheckTime info : infoList) {
					String columnName = getAdcCheckColumnName(info.getCheckID());
					if (columnName.isEmpty())
						continue;
					if (!updateText.isEmpty())
						updateText += ", ";
					updateText += String.format("%s=%s", columnName, OBParser.sqlString(info.getCheckTime()));
				}
				sqlText = String.format(" UPDATE TMP_LAST_ADC_CHECK_TIME 	\n" + " SET %s 						\n"
						+ " WHERE ADC_INDEX=%d; 				\n", updateText, adcIndex);

				db.executeUpdate(sqlText);
			} else {// 데이터가 없는 경우. insert한다.
				String colunmText = "";
				String valueText = "";
				for (OBDtoLastAdcCheckTime info : infoList) {
					String columnName = getAdcCheckColumnName(info.getCheckID());
					if (columnName.isEmpty())
						continue;

					if (!colunmText.isEmpty()) {
						colunmText += ", ";
						valueText += ", ";
					}
					colunmText += columnName;
					valueText += OBParser.sqlString(info.getCheckTime());
				}
				sqlText = String.format(
						" INSERT INTO TMP_LAST_ADC_CHECK_TIME 							\n"
								+ " (ADC_INDEX, %s) 												\n"
								+ " VALUES														\n"
								+ " (%s, %s)														\n",
						colunmText, adcIndex, valueText);
				db.executeUpdate(sqlText);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// tmp_last_adc_check_time 테이블에 지정된 adcIndex에 레코드를 추가한다.
	public void makeLastADCCheckTimeTable(int adcIndex, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT ADC_INDEX 						\n"
					+ " FROM TMP_LAST_ADC_CHECK_TIME	       	\n" + " WHERE ADC_INDEX=%d  LIMIT 1          	\n",
					adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {// 데이터가 있는 경우.
				return;
			} else {// 데이터가 없는 경우.
				sqlText = String.format(" INSERT INTO TMP_LAST_ADC_CHECK_TIME       \n"
						+ " (ADC_INDEX )     							\n"
						+ " VALUES                                	\n" + " ( %d )                            		\n",
						adcIndex);
				db.executeUpdate(sqlText);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return;
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void makeLastADCCheckTimeTable(int adcIndex) throws OBException {
		String sqlText = "";
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT ADC_INDEX 						\n"
					+ " FROM TMP_LAST_ADC_CHECK_TIME	       	\n" + " WHERE ADC_INDEX=%d  LIMIT 1          	\n",
					adcIndex);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {// 데이터가 있는 경우.
				return;
			} else {// 데이터가 없는 경우.
				sqlText = String.format(" INSERT INTO TMP_LAST_ADC_CHECK_TIME       \n"
						+ " (ADC_INDEX )     							\n"
						+ " VALUES                                	\n" + " ( %d )                            		\n",
						adcIndex);
				db.executeUpdate(sqlText);
			}
		} catch (SQLException e) {
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("errorerror"));
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return;
	}

	public void updateLastAdcCheckTime(Integer adcIndex, Integer checkID, OBDatabase db) throws OBException {
		OBDtoLastAdcCheckTime info = new OBDtoLastAdcCheckTime();

		info.setCheckID(checkID);
		info.setCheckTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String sqlText = "";
		try {
//			sqlText=String.format(" SELECT COUNT(*) AS CNT  			\n" +
//					              " FROM TMP_LAST_ADC_CHECK_TIME 		\n" +
//					              " WHERE ADC_INDEX=%d					\n",
//					              adcIndex);
//			ResultSet rs = db.executeQuery(sqlText);
//			if(rs.next()==false)
//			{
//				return;
//			}
//			if(db.getInteger(rs, "CNT")>0)
//			{// 데이터가 있는 경우. update한다.
			String updateText = "";
//				for(OBDtoLastAdcCheckTime info: infoList)
			String columnName = getAdcCheckColumnName(info.getCheckID());
			if (columnName.isEmpty())
				return;

			if (!updateText.isEmpty())
				updateText += ", ";
			updateText += String.format("%s=%s", columnName, OBParser.sqlString(info.getCheckTime()));

			sqlText = String.format(" UPDATE TMP_LAST_ADC_CHECK_TIME 	\n" + " SET %s 				  		  	\n"
					+ " WHERE ADC_INDEX=%d; 				\n", updateText, adcIndex);

			db.executeUpdate(sqlText);
//			}
//			else
//			{// 데이터가 없는 경우. insert한다.
//				String colunmText = "";
//				String valueText = "";
////				for(OBDtoLastAdcCheckTime info: infoList)
//				{
//					String columnName = getAdcCheckColumnName(info.getCheckID());
//					if(columnName.isEmpty())
//						return;
//					
//					if(!colunmText.isEmpty())
//					{
//						colunmText += ", ";
//						valueText += ", ";
//					}
//					colunmText += columnName;
//					valueText += OBParser.sqlString(info.getCheckTime());
//				}
//				sqlText = String.format(" INSERT INTO TMP_LAST_ADC_CHECK_TIME 							\n" +
//										" (ADC_INDEX, %s) 												\n" +
//										" VALUES														\n" +
//										" (%s, %s)														\n",
//										colunmText,
//										adcIndex,
//										valueText);
//				db.executeUpdate(sqlText);
//			}
		} catch (SQLException e) {
//			throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
//			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public void updateLastAdcCheckTime(Integer adcIndex, Integer checkID) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoLastAdcCheckTime info = new OBDtoLastAdcCheckTime();

		info.setCheckID(checkID);
		info.setCheckTime(OBDateTime.toTimestamp(OBDateTime.now()));

		String sqlText = "";
		try {
			db.openDB();
//			sqlText=String.format(" SELECT COUNT(*) AS CNT  			\n" +
//					              " FROM TMP_LAST_ADC_CHECK_TIME 		\n" +
//					              " WHERE ADC_INDEX=%d					\n",
//					              adcIndex);
//			ResultSet rs = db.executeQuery(sqlText);
//			if(rs.next()==false)
//			{
//				return;
//			}
//			if(db.getInteger(rs, "CNT")>0)
//			{// 데이터가 있는 경우. update한다.
			String updateText = "";
//				for(OBDtoLastAdcCheckTime info: infoList)
			String columnName = getAdcCheckColumnName(info.getCheckID());
			if (columnName.isEmpty())
				return;

			if (!updateText.isEmpty())
				updateText += ", ";
			updateText += String.format("%s=%s", columnName, OBParser.sqlString(info.getCheckTime()));

			sqlText = String.format(" UPDATE TMP_LAST_ADC_CHECK_TIME 	\n" + " SET %s 				  		  	\n"
					+ " WHERE ADC_INDEX=%d; 				\n", updateText, adcIndex);

			db.executeUpdate(sqlText);
//			}
//			else
//			{// 데이터가 없는 경우. insert한다.
//				String colunmText = "";
//				String valueText = "";
////				for(OBDtoLastAdcCheckTime info: infoList)
//				{
//					String columnName = getAdcCheckColumnName(info.getCheckID());
//					if(columnName.isEmpty())
//						return;
//					
//					if(!colunmText.isEmpty())
//					{
//						colunmText += ", ";
//						valueText += ", ";
//					}
//					colunmText += columnName;
//					valueText += OBParser.sqlString(info.getCheckTime());
//				}
//				sqlText = String.format(" INSERT INTO TMP_LAST_ADC_CHECK_TIME 							\n" +
//										" (ADC_INDEX, %s) 												\n" +
//										" VALUES														\n" +
//										" (%s, %s)														\n",
//										colunmText,
//										adcIndex,
//										valueText);
//				db.executeUpdate(sqlText);
//			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private String getAdcCheckColumnName(Integer id) {
		String columnText = "";
		switch (id) {
		case OBDtoLastAdcCheckTime.CHECK_ID_PORTOPEN:
			columnText += "PORT_OPEN_TIME";
			break;
		case OBDtoLastAdcCheckTime.CHECK_ID_LOGIN:
			columnText += "LOGIN_TIME";
			break;
		case OBDtoLastAdcCheckTime.CHECK_ID_VERSION:
			columnText += "VERSION_TIME";
			break;
		case OBDtoLastAdcCheckTime.CHECK_ID_PORTREVERSE:
			columnText += "REVERSE_PORT_TIME";
			break;
		case OBDtoLastAdcCheckTime.CHECK_ID_SNMP:
			columnText += "SNMP_TIME";
			break;
		case OBDtoLastAdcCheckTime.CHECK_ID_SYSLOG:
			columnText += "SYSLOG_TIME";
			break;
		default:
			return "";
		}
		return columnText;
	}
}
