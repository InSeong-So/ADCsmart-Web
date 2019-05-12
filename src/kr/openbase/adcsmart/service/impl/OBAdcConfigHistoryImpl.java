package kr.openbase.adcsmart.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kr.openbase.adcsmart.service.OBAdcConfigHistory;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAccount;
import kr.openbase.adcsmart.service.dto.OBDtoAdcHealthCheckAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcNodeF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;
import kr.openbase.adcsmart.service.dto.OBDtoConnectionData;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkF5;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkPAS;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigChunkPASK;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory2;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryDB;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryF5;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryPAS;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistoryPASK;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigInfoAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigInfoF5;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigInfoPAS;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigInfoPASK;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigMixAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigMixF5;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigMixPAS;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigMixPASK;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigNodeAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigNodeF5;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigNodePASK;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigPoolAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigPoolF5;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigPoolMemberAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigPoolMemberF5;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigPoolMemberPAS;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigPoolMemberPASK;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigPoolPAS;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigVServerAlteon;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigVServerF5;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigVServerPAS;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigVServerPASK;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigVServiceAlteon;
import kr.openbase.adcsmart.service.impl.alteon.OBAdcVServerAlteon;
import kr.openbase.adcsmart.service.impl.f5.CommonF5;
import kr.openbase.adcsmart.service.impl.f5.OBAdcVServerF5;
import kr.openbase.adcsmart.service.impl.pas.OBAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolMemberPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pask.OBAdcVServerPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcNodePASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolMemberPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBDefineHealthcheckAlteon;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBLicenseExpiredException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcConfigHistoryImpl implements OBAdcConfigHistory {
	public static final String CONFIG_VERSION = "13.1118";

	// Virtual Server 이력을 페이지 인덱스 분할 없이, 검색조건 없이, 시간 조건으로만 뽑는다. VS변경 이력을 그래프등 다른 자료와
	// 병행 표시할 때 쓴다.
	public ArrayList<OBDtoAdcConfigHistory> getVsConfigHistoryListNoPagingNoSearch(Integer adcIndex, String vsIndex,
			Date beginTime, Date endTime) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoAdcConfigHistory> list;
		try {
			db.openDB();

			// VS의config history를 구해야하기 때문에 2nd 파라미터에 vsIndex를 준다. 2nd 파라미터는 vsIndex인데 null로
			// 주면 이 조건은 무시된다.
			// search keyword(3rd), beginindex(6th), endindex(7th)도 없다.
			list = getConfigHistoryList(adcIndex, vsIndex, null, beginTime, endTime, null, null,
					OBDefine.ORDER_TYPE_OCCURTIME, OBDefine.ORDER_DIR_DESCEND, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return list;
	}

	// ADC의 이력을 페이지 인덱스 분할 없이, 검색조건 없이, 시간 조건으로만 뽑는다. ADC scope의 변경 이력을 그래프등 다른 자료와
	// 병행 표시할 때 쓴다.
	public ArrayList<OBDtoAdcConfigHistory> getAdcConfigHistoryListNoPagingNoSearch(Integer adcIndex, Date beginTime,
			Date endTime) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoAdcConfigHistory> list;
		try {
			db.openDB();

			// ADC의config history 전체를 구해야하기 때문에 2nd 파라미터가 null이다. 2nd 파라미터는 vsIndex인데 null로
			// 주면 이 조건은 무시된다.
			list = getConfigHistoryList(adcIndex, null, null, beginTime, endTime, null, null,
					OBDefine.ORDER_TYPE_OCCURTIME, OBDefine.ORDER_DIR_DESCEND, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return list;
	}

	@Override
	public Long revertConfigF5(Integer adcIndex, String vsIndex, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "begin / vsindex = " + vsIndex);
		OBDtoAdcConfigHistoryDB lastConfig;
		Long retVal = 0L;
		try {
			lastConfig = this.getLastConfig(vsIndex);
			new OBAdcVServerF5().revertSlbConfig(adcIndex, lastConfig.getChunkF5(), extraInfo);
			retVal = lastConfig.getDbIndex();
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBLicenseExpiredException e) {
			throw new OBException(OBException.ERRCODE_LICENSE_INVALID, e.getMessage());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "end");
		return retVal;
	}

	@Override
	public Long revertConfigAlteon(Integer adcIndex, String vsIndex, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "begin / vsindex = " + vsIndex);
		OBDtoAdcConfigHistoryDB lastConfig;
		Long retVal = 0L;
		try {
			lastConfig = this.getLastConfig(vsIndex);
			new OBAdcVServerAlteon().Revert(adcIndex, lastConfig.getChunkAlteon(), extraInfo);
			retVal = lastConfig.getDbIndex();
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBLicenseExpiredException e) {
			throw new OBException(OBException.ERRCODE_LICENSE_INVALID, e.getMessage());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "done");
		return retVal;
	}

	@Override
	public Long revertConfigPAS(Integer adcIndex, String vsIndex, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException {
		Long retVal = 0L;
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "begin / vsindex = " + vsIndex);
		OBDtoAdcConfigHistoryDB lastConfig;
		try {
			lastConfig = this.getLastConfig(vsIndex);
			new OBAdcVServerPAS().revertSlbConfig(adcIndex, lastConfig.getChunkPAS(), extraInfo);
			retVal = lastConfig.getDbIndex();
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBLicenseExpiredException e) {
			throw new OBException(OBException.ERRCODE_LICENSE_INVALID, e.getMessage());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "end");
		return retVal;
	}

	@Override
	public Long revertConfigPASK(Integer adcIndex, String vsIndex, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "begin / vsindex = " + vsIndex);
		OBDtoAdcConfigHistoryDB lastConfig;
		Long retVal = 0L;
		try {
			lastConfig = this.getLastConfig(vsIndex);
			new OBAdcVServerPASK().revertSlbConfig(adcIndex, lastConfig.getChunkPASK(), extraInfo);
			retVal = lastConfig.getDbIndex();
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBLicenseExpiredException e) {
			throw new OBException(OBException.ERRCODE_LICENSE_INVALID, e.getMessage());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "end");
		return retVal;
	}

	@Override
	public OBDtoAdcConfigHistoryF5 getVSConfigHistoryF5(Integer adcIndex, String vsIndex, Integer logSeq)
			throws OBException {
		OBDtoAdcConfigHistoryF5 ret;
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex = %d, vsIndex = %s, logSeq = %d", adcIndex, vsIndex, logSeq));
		try {
			ret = getVSHistoryInfoF5(vsIndex, logSeq);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result = %s", ret));
		return ret;
	}

	@Override
	public OBDtoAdcConfigHistoryAlteon getVSConfigHistoryAlteon(Integer adcIndex, String vsIndex, Integer logSeq)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex = %d, vsIndex = %s, logSeq = %d", adcIndex, vsIndex, logSeq));
		OBDtoAdcConfigHistoryAlteon ret;
		try {
			ret = getVSHistoryInfoAlteon(vsIndex, logSeq);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result = %s", ret));

		return ret;
	}

	@Override
	public OBDtoAdcConfigHistoryPAS getVSConfigHistoryPAS(Integer adcIndex, String vsIndex, Integer logSeq)
			throws OBException {
		OBDtoAdcConfigHistoryPAS ret;
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex = %d, vsIndex = %s, logSeq = %d", adcIndex, vsIndex, logSeq));
		try {
			ret = getVSHistoryInfoPAS(vsIndex, logSeq);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result = %s", ret));
		return ret;
	}

	@Override
	public OBDtoAdcConfigHistoryPASK getVSConfigHistoryPASK(Integer adcIndex, String vsIndex, Integer logSeq)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
				String.format("start. adcIndex = %d, vsIndex = %s, logSeq = %d", adcIndex, vsIndex, logSeq));
		OBDatabase db = new OBDatabase();
		OBDtoAdcConfigHistoryPASK ret;
		try {
			db.openDB();
			ret = getVSConfigHistoryPASK(vsIndex, logSeq, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result = %s", ret));
		return ret;
	}

	@Override
	public OBDtoConnectionData getVSRealTimeCurrConns(Integer adcIndex, String vsIndex) throws OBException {
		try {
			OBDtoConnectionData connData = null;
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
			OBVServerDB vdb = new OBVServerDB();

			if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_F5)) {
				String vsName = vdb.getVirtualServerName(vsIndex);
				connData = OBCommon
						.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
						.getVServerConns(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsName);
			} else if (adcInfo.getAdcType().equals(OBDefine.ADC_TYPE_ALTEON)) {
				String vsID = vdb.getVServerID(vsIndex);
				connData = OBCommon
						.getValidSnmpAlteonHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
								adcInfo.getSnmpInfo())
						.getVServerConns(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsID);
			}
			return connData;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	@Override
	public Integer getAdcConfigHistoryTotalRecordCount(Integer adcIndex, String searchKey, Date beginTime, Date endTime)
			throws OBException {
		OBDatabase db = new OBDatabase();
		Integer recordCount = 0;
		try {
			db.openDB();
			recordCount = getConfigHistoryListTotalRecordCount(adcIndex, searchKey, beginTime, endTime, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		return recordCount;
	}

	@Override
	public String checkRecoverable(Integer accountIndex, Integer adcIndex, String vsIndex, Integer logSeq)
			throws OBException {
		if (accountIndex == null || vsIndex == null) {
			return OBMessages.getMessage(OBMessages.MSG_SYSTEM_CONFIG_HISTORY_INVALID_ID_INDEX);// OBDefine.CONFIG_HISTORY_INVALID_ID_INDEX;//"사용자
																								// ID 또는 복구할 Virtual
																								// Server 인덱스에 오류가
																								// 있습니다.";
		}

		try {
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
			if (roleNo == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
						String.format("accountIndex \"%d\" role error.", accountIndex));
			} else if (roleNo.equals(OBDefine.ACCNT_ROLE_READONLY)) {
				return OBMessages.getMessage(OBMessages.MSG_SYSTEM_CONFIG_HISTORY_INVALID_ROLE);// OBDefine.CONFIG_HISTORY_INVALID_ROLE;//"역할이
																								// 'ReadOnlyUser'인 사용자는
																								// 설정을 복구할 수 없습니다.";
			}

			OBDtoAdcConfigHistoryDB thisConfig = getConfigHistorySingle(vsIndex, logSeq); // logSeq에 해당하는 리스트만 가져온다
			return checkRecoverable(adcIndex, vsIndex, thisConfig); // vs의 변경 이력이 복구가능한지 이력 내용을 확인한다.
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private String checkRecoverable(Integer adcIndex, String vsIndex, OBDtoAdcConfigHistoryDB config)
			throws OBException {
		try {
			Integer adcType = new OBAdcManagementImpl().getAdcInfo(adcIndex).getAdcType();
			OBDtoAdcConfigHistoryDB lastConfig = getLastConfig(vsIndex);

			if (config.getUserType() == OBDefine.CHANGE_BY_SYSTEM) // 시스템에 의한 변경은 복구할 수 없다.
			{
				return OBMessages.getMessage(OBMessages.MSG_SYSTEM_CONFIG_HISTORY_NOT_SUPPORT);// OBDefine.CONFIG_HISTORY_NOT_SUPPORT;//"ADCSmart
																								// 외부에서 변경된 설정은 복구할 수
																								// 없습니다.";
			}

			if (lastConfig.getDbIndex().equals(config.getDbIndex()) == false) // 선택된 이력이 vs의 마지막 이력이 아니면 복구할 수 없다.
			{
				return OBMessages.getMessage(OBMessages.MSG_SYSTEM_CONFIG_HISTORY_NOT_LAST_VSCHANGE);// OBDefine.CONFIG_HISTORY_NOT_LAST_VSCHANGE;
			}

			if (adcType.equals(OBDefine.ADC_TYPE_F5)) // F5 조건 검사
			{
				if (config.getChunkF5() == null) {
					return OBMessages.getMessage(OBMessages.MSG_SYSTEM_CONFIG_HISTORY_INVALID_ROLE);// //"변경 이력에 오류가 있어
																									// 복구할 수 없습니다.";
				}
				if (config.getChunkF5().getChangeObject() != OBDefine.CHANGE_OBJECT_VIRTUALSERVER) {
					return OBMessages.getMessage(OBMessages.MSG_SYSTEM_CONFIG_HISTORY_INVALID_CONF);// //"virtual server
																									// 변경이 아니므로 설정을 복구할
																									// 수 없습니다.";
				}
			} else if (adcType.equals(OBDefine.ADC_TYPE_ALTEON)) // alteon 조건 검사
			{
				if (config.getChunkAlteon() == null) {
					return OBMessages.getMessage(OBMessages.MSG_SYSTEM_CONFIG_HISTORY_INVALID_ROLE);// //"변경 이력에 오류가 있어
																									// 복구할 수 없습니다.";
				}

				if (config.getChunkAlteon().getChangeObject() != OBDefine.CHANGE_OBJECT_VIRTUALSERVER) {
					return OBMessages.getMessage(OBMessages.MSG_SYSTEM_CONFIG_HISTORY_INVALID_CONF);// //"virtual server
																									// 변경이 아니므로 설정을 복구할
																									// 수 없습니다.";
				}
			} else if (adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) // PAS 조건 검사
			{
				if (config.getChunkPAS() == null) {
					return OBMessages.getMessage(OBMessages.MSG_SYSTEM_CONFIG_HISTORY_INVALID_ROLE);// //"변경 이력에 오류가 있어
																									// 복구할 수 없습니다.";
				}
				if (config.getChunkPAS().getChangeObject() != OBDefine.CHANGE_OBJECT_VIRTUALSERVER) {
					return OBMessages.getMessage(OBMessages.MSG_SYSTEM_CONFIG_HISTORY_INVALID_CONF);// //"virtual server
																									// 변경이 아니므로 설정을 복구할
																									// 수 없습니다.";
				}
			} else if (adcType.equals(OBDefine.ADC_TYPE_PIOLINK_PASK)) // PASK 조건 검사
			{
				if (config.getChunkPASK() == null) {
					return OBMessages.getMessage(OBMessages.MSG_SYSTEM_CONFIG_HISTORY_INVALID_ROLE);// //"변경 이력에 오류가 있어
																									// 복구할 수 없습니다.";
				}
				if (config.getChunkPASK().getChangeObject() != OBDefine.CHANGE_OBJECT_VIRTUALSERVER) {
					return OBMessages.getMessage(OBMessages.MSG_SYSTEM_CONFIG_HISTORY_INVALID_CONF);// //"virtual server
																									// 변경이 아니므로 설정을 복구할
																									// 수 없습니다.";
				}
			} else
			// 여기 오지 않을 테지만 혹시나 ...
			{
				return OBMessages.getMessage(OBMessages.MSG_SYSTEM_CONFIG_HISTORY_INVALID_CONF);// OBDefine.CONFIG_HISTORY_NOT_SUPPORT_VERSION;//F5,Alteon,PAS,PASK
																								// ADC가 아니므로 설정을 복구할 수
																								// 없다.;
			}

			return "";
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	private String getConfigHistoryListOrderType(Integer orderType, Integer orderDir) throws OBException {
		String retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST ";

		switch (orderType) {
		case OBDefine.ORDER_TYPE_OCCURTIME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY OCCUR_TIME ASC NULLS LAST ";
			else
				retVal = " ORDER BY OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_VSNAME:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VS_NAME ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY VS_NAME DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		case OBDefine.ORDER_TYPE_VSIPADDRESS:
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(A.VS_IP) ASC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			else
				retVal = " ORDER BY INET(A.VS_IP) DESC NULLS LAST, OCCUR_TIME DESC NULLS LAST ";
			break;
		}
		return retVal;
	}

	/**
	 * 
	 * @param adcIndex : 0=전체
	 * @param vsIndex  : null=전체, 즉 조건 없음
	 */
	public ArrayList<OBDtoAdcConfigHistory> getConfigHistoryList(int adcIndex, String vsIndex, String searchKey,
			Date beginTime, Date endTime, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir,
			OBDatabase db) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex = %d", adcIndex));
		String sqlText = "";
		String wildcardKey = "";
		String whereClauseKey = "";
		String whereClauseTime = "";
		String limitClause = "";
		try {
			sqlText = " SELECT A.LOG_SEQ, A.LOG_TYPE, A.ACCESS_TYPE, A.ACCNT_INDEX, A.ACCNT_NAME, A.ADC_INDEX, A.VS_INDEX,	\n"
					+ " A.VS_NAME AS VS_NAME, A.VS_IP AS VS_IPADDRESS, A.OBJECT_TYPE, A.OCCUR_TIME AS OCCUR_TIME,            \n"
					+ " A.SUMMARY AS CONTENT, B.INDEX AS VS_EXIST	                                \n"
					+ " FROM LOG_CONFIG_HISTORY A   \n" + " LEFT JOIN TMP_SLB_VSERVER B \n"
					+ " ON A.VS_INDEX = B.INDEX     \n";

			if (adcIndex == 0) {
				sqlText += " WHERE TRUE \n";
			} else {
				sqlText += String.format(" WHERE A.ADC_INDEX = %d \n", adcIndex);
				if (vsIndex != null && vsIndex.isEmpty() == false) {
					sqlText += String.format(" AND A.VS_INDEX = %s \n", OBParser.sqlString(vsIndex));
				}
			}

			if (searchKey != null) {
				// #3984-2 #15: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				wildcardKey = OBParser.sqlString("%" + OBParser.removeWildcard(searchKey) + "%");
				whereClauseKey = String.format(" AND (A.VS_NAME LIKE %s OR A.VS_IP LIKE %s OR A.SUMMARY LIKE %s) ",
						wildcardKey, wildcardKey, wildcardKey);
			}

			if (beginTime != null) {
				whereClauseTime = String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));
				if (endTime != null) {
					whereClauseTime += String.format(" AND OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			} else {
				if (endTime != null) {
					whereClauseTime += String.format(" AND OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			}

			if (beginIndex != null && endIndex != null) {
				int limit = (endIndex - beginIndex) >= 0 ? (endIndex - beginIndex) + 1 : 0;
				int offset = beginIndex >= 0 ? beginIndex : 0;
				limitClause = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			ArrayList<OBDtoAdcConfigHistory> list = new ArrayList<OBDtoAdcConfigHistory>();

			sqlText += String.format(" %s \n %s \n", whereClauseKey, whereClauseTime);

			sqlText += getConfigHistoryListOrderType(orderType, orderDir);
			sqlText += limitClause;

			ResultSet rs;

			rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoAdcConfigHistory obj = new OBDtoAdcConfigHistory();

				obj.setLogSeq(db.getInteger(rs, "LOG_SEQ"));
				obj.setUserType(db.getInteger(rs, "LOG_TYPE"));
				// obj.setAdcType(db.getInteger(rs, "TYPE"));
				obj.setAccessType(db.getInteger(rs, "ACCESS_TYPE"));
				obj.setAccountIndex(db.getInteger(rs, "ACCNT_INDEX"));
				obj.setAccountName(db.getString(rs, "ACCNT_NAME"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setVsIndex(db.getString(rs, "VS_INDEX"));
				obj.setVsName(db.getString(rs, "VS_NAME"));
				obj.setVsIp(db.getString(rs, "VS_IPADDRESS"));
				// obj.setVsStatus(db.getInteger(rs, "STATUS")); //TODO 삭제후 같은 ID로 재입력 된 virtual
				// server의 경우 상태가 복원되는 문제가 있을 수 있다.
				obj.setObjectType(db.getInteger(rs, "OBJECT_TYPE"));
				obj.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				obj.setSummary(db.getString(rs, "CONTENT"));
				String vsExist = db.getString(rs, "VS_EXIST");
				if (vsExist == null || vsExist.isEmpty())
				// if(obj.getAccessType()==OBDefine.CHANGE_TYPE_DELETE)
				{
					obj.setVsAlive(OBDefine.OBJECT_DEAD); // 삭제된 virtual server 표시. 이 값을 보고 삭제된 virtual server는 화면에서
															// "설정"버튼을 지운다.
				} else {
					obj.setVsAlive(OBDefine.OBJECT_ALIVE);
				}

				list.add(obj);
			}

			OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. list count = %d", list.size()));
			return list;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public OBDtoAdcConfigHistoryF5 getVSHistoryInfoF5(String vServerIndex, Integer logSeq) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoAdcConfigHistoryF5 info;
		try {
			db.openDB();

			info = getVSHistoryInfoF5(vServerIndex, logSeq, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		return info;
	}

	public OBDtoAdcConfigHistoryF5 getVSHistoryInfoF5(String vServerIndex, Integer logSeq, OBDatabase db)
			throws OBException {
		ArrayList<OBDtoAdcConfigHistoryDB> listAll = getConfigHistoryCore(vServerIndex, 0, false, db); // 가져올 데이터 카운트=0,
																										// 모두 가져온다.
		OBDtoAdcConfigHistoryDB configDetail = getConfigHistorySingle(vServerIndex, logSeq, db); // logSeq에 해당하는 리스트만
																									// 가져온다
		if (listAll == null || configDetail == null) {
			return null;
		}
		// list.size()=0은 불가능하기 때문에 확인하지 않음

		int i = 0;
		OBDtoAdcConfigHistoryF5 infoMain = new OBDtoAdcConfigHistoryF5();
		OBDtoAdcConfigInfoF5 infoOld = new OBDtoAdcConfigInfoF5();
		OBDtoAdcConfigInfoF5 infoNew = new OBDtoAdcConfigInfoF5();

		if (configDetail.getAccessType() == OBDefine.CHANGE_TYPE_ADD) // virtual server 추가
		{
			infoOld = null;
		} else
		// virtual server 수정이나 삭제
		{
			if (listAll.size() < 2) // 첫 수정사례면 없을 수 있다.
			{
				infoOld.setLastTime(null);
				infoOld.setSummary("");
			} else {
				infoOld.setLastTime(configDetail.getChunkF5().getVsConfig().getVsOld().getApplyTime());
			}
			infoOld.setPool(configDetail.getChunkF5().getVsConfig().getVsOld().getPool());
			infoOld.setVsIPAddress(configDetail.getChunkF5().getVsConfig().getVsOld().getvIP());
			infoOld.setVsName(configDetail.getChunkF5().getVsConfig().getVsOld().getName());
			infoOld.setVsPersistenceName(configDetail.getChunkF5().getVsConfig().getVsOld().getPersistence());
			infoOld.setVsServicePort(configDetail.getChunkF5().getVsConfig().getVsOld().getServicePort());
			infoOld.setVsUseYN(configDetail.getChunkF5().getVsConfig().getVsOld().getUseYN());
		}

		if (configDetail.getAccessType() == OBDefine.CHANGE_TYPE_DELETE) // virtual server 삭제
		{
			infoNew = null;
		} else
		// virtual server 수정이나 추가
		{
			infoNew.setLastTime(configDetail.getOccurTime());
			infoNew.setPool(configDetail.getChunkF5().getVsConfig().getVsNew().getPool());
			infoNew.setSummary(configDetail.getSummary());
			if (configDetail.getSummary().equals("change : virtual server disable")) {
				infoNew.setState("disable");
				infoOld.setState("enable");
			} else if (configDetail.getSummary().equals("change : virtual server enable")) {
				infoNew.setState("enable");
				infoOld.setState("disable");
			}
			infoNew.setVsIPAddress(configDetail.getChunkF5().getVsConfig().getVsNew().getvIP());
			infoNew.setVsName(configDetail.getChunkF5().getVsConfig().getVsNew().getName());
			infoNew.setVsPersistenceName(configDetail.getChunkF5().getVsConfig().getVsNew().getPersistence());
			infoNew.setVsServicePort(configDetail.getChunkF5().getVsConfig().getVsNew().getServicePort());
			infoNew.setVsUseYN(configDetail.getChunkF5().getVsConfig().getVsNew().getUseYN());
		}

		ArrayList<OBDtoAdcConfigHistory2> vsHistoryList = new ArrayList<OBDtoAdcConfigHistory2>();

		for (i = 0; i < listAll.size(); i++) {
			OBDtoAdcConfigHistory2 history = new OBDtoAdcConfigHistory2();
			history.setLogSeq(listAll.get(i).getDbIndex());
			history.setVirtualSvrIndex(listAll.get(i).getVsIndex());
			history.setAccountName(listAll.get(i).getAccountName());
			history.setSummary(listAll.get(i).getSummary());
			history.setOccurTime(listAll.get(i).getOccurTime());

			vsHistoryList.add(history);
		}

		infoMain.setAdcIndex(configDetail.getAdcIndex());
		infoMain.setVsIndex(configDetail.getVsIndex());
		infoMain.setVsConfigInfoOld(infoOld);
		infoMain.setVsConfigInfoNew(infoNew);
		infoMain.setVsHistoryList(vsHistoryList);

		String recoverString = checkRecoverable(infoMain.getAdcIndex(), vServerIndex, configDetail);

		if (recoverString != null && recoverString.isEmpty()) {
			infoMain.setRecoverable(true);
		} else {
			infoMain.setRecoverable(false);
		}
		return infoMain;
	}

	public OBDtoAdcConfigHistoryAlteon getVSHistoryInfoAlteon(String vServerIndex, Integer logSeq) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoAdcConfigHistoryAlteon info;
		try {
			db.openDB();

			info = getVSHistoryInfoAlteon(vServerIndex, logSeq, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		return info;
	}

	private boolean remakeChunkAlteon(OBDtoAdcConfigHistoryDB config, OBDatabase db) throws OBException {
		if (config.getVersion() == null) {
			String healthcheck;
			OBDtoAdcVServerAlteon vsOld = config.getChunkAlteon().getVsConfig().getVsOld();
			OBDtoAdcVServerAlteon vsNew = config.getChunkAlteon().getVsConfig().getVsNew();
			if (vsOld != null) {
				if (vsOld.getVserviceList() != null) {
					for (OBDtoAdcVService service : vsOld.getVserviceList()) {
						if (service != null) {
							OBDtoAdcHealthCheckAlteon hc = new OBDtoAdcHealthCheckAlteon();
							healthcheck = OBDefineHealthcheckAlteon.OLD.TYPE.get(service.getPool().getHealthCheck());
							hc.setId(healthcheck);
							hc.setExtra(healthcheck);
							service.getPool().setHealthCheckV2(hc);
							service.getPool().setHealthCheck(null); // 안 쓰는 healthcheck는 null로
						}
					}
				}
			}
			if (vsNew != null) {
				if (vsNew.getVserviceList() != null) {
					for (OBDtoAdcVService service : vsNew.getVserviceList()) {
						if (service != null) {
							OBDtoAdcHealthCheckAlteon hc = new OBDtoAdcHealthCheckAlteon();
							healthcheck = OBDefineHealthcheckAlteon.OLD.TYPE.get(service.getPool().getHealthCheck());
							hc.setId(healthcheck);
							hc.setExtra(healthcheck);
							service.getPool().setHealthCheckV2(hc);
							service.getPool().setHealthCheck(null); // 안 쓰는 healthcheck는 null로
						}
					}
				}
			}
			config.setVersion(CONFIG_VERSION); // 현재 history 버전을 준다.

			updateConfigHistory(config, db);
			return true;
		}
		return false;
	}

	public void updateConfigHistory(OBDtoAdcConfigHistoryDB history, OBDatabase db) throws OBException {
		String sqlText = "";
		try {
			sqlText = String.format(" UPDATE LOG_CONFIG_HISTORY "
					+ " SET LOG_TYPE=?, ACCNT_INDEX=?, ACCNT_NAME=?, ADC_INDEX=?, VS_INDEX=?, VS_NAME=?, VS_IP=?, "
					+ "   OBJECT_TYPE=?, OCCUR_TIME=?, SUMMARY=?, DESCRIPTION=?, ACCESS_TYPE=?, CONFIG_CHUNK=?, VERSION=? "
					+ " WHERE LOG_SEQ = %s;", history.getDbIndex());

			db.initPreparedStatement(sqlText);
			db.setPreparedStatementInt(1, history.getUserType());
			db.setPreparedStatementInt(2, history.getAccountIndex());
			db.setPreparedStatementString(3, history.getAccountName());
			db.setPreparedStatementInt(4, history.getAdcIndex());
			db.setPreparedStatementString(5, history.getVsIndex());
			db.setPreparedStatementString(6, history.getVsName());
			db.setPreparedStatementString(7, history.getVsIp());
			db.setPreparedStatementInt(8, history.getObjectType());
			db.setPreparedStatementTimestamp(9, new Timestamp(history.getOccurTime().getTime()));
			db.setPreparedStatementString(10, history.getSummary());
			db.setPreparedStatementString(11, null); // description
			db.setPreparedStatementInt(12, history.getAccessType());
			if (history.getChunkF5() != null) {
				db.setPreparedStatementObject(13, history.getChunkF5());
			} else if (history.getChunkAlteon() != null) {
				db.setPreparedStatementObject(13, history.getChunkAlteon());
			} else if (history.getChunkPAS() != null) {
				db.setPreparedStatementObject(13, history.getChunkPAS());
			} else if (history.getChunkPASK() != null) {
				db.setPreparedStatementObject(13, history.getChunkPASK());
			}
			db.setPreparedStatementString(14, history.getVersion());

			db.executeUpdatePreparedStreamt();
			db.deInitPreparedStatement();
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "end.");
	}

	public OBDtoAdcConfigHistoryAlteon getVSHistoryInfoAlteon(String vServerIndex, Integer logSeq, OBDatabase db)
			throws OBException {
		ArrayList<OBDtoAdcConfigHistoryDB> listAll = getConfigHistoryCore(vServerIndex, 0, false, db);// 가져올 데이터 카운트=0,
																										// 모두 가져온다.
		OBDtoAdcConfigHistoryDB configDetail = getConfigHistorySingle(vServerIndex, logSeq, db); // logSeq에 해당하는 리스트만
																									// 가져온다

		if (listAll == null || configDetail == null) {
			return null;
		} // listAll.size()=0은 불가능하기 때문에 확인하지 않음

		int i = 0;
		boolean result = remakeChunkAlteon(configDetail, db); // 저장된 이력 변형을 보정한다.
		if (result == true) // remake해서 chunk 버전이 바뀌었으면 DB에 한번 써준다.
		{
			configDetail = getConfigHistorySingle(vServerIndex, logSeq, db); // 바꾼 것을 다시 읽는다.
		}
		OBDtoAdcConfigHistoryAlteon info = new OBDtoAdcConfigHistoryAlteon();
		OBDtoAdcConfigInfoAlteon infoOld = new OBDtoAdcConfigInfoAlteon();
		OBDtoAdcConfigInfoAlteon infoNew = new OBDtoAdcConfigInfoAlteon();

		if (configDetail.getAccessType() == OBDefine.CHANGE_TYPE_ADD) // virtual server 추가
		{
			infoOld = null;
		} else
		// virtual server 수정이나 삭제
		{
			infoOld.setAlteonId(configDetail.getChunkAlteon().getVsConfig().getVsOld().getAlteonId());
			infoOld.setIfNum(configDetail.getChunkAlteon().getVsConfig().getVsOld().getIfNum());

			if (listAll.size() < 2) // 첫 수정사례면 없을 수 있다.
			{
				infoOld.setLastTime(null);
				// summary는 쓰지 않으므로 넣지 않는다.
			} else {
				infoOld.setLastTime(configDetail.getChunkAlteon().getVsConfig().getVsOld().getApplyTime());
				// summary는 쓰지 않으므로 넣지 않는다.
			}
			// infoOld.setVrrpYN(configDetail.getChunkAlteon().getVsConfig().getVsOld().isVrrpYN());
			// //VrrpYN 제거됐음
			infoOld.setVserviceList(configDetail.getChunkAlteon().getVsConfig().getVsOld().getVserviceList());
			infoOld.setVsIPAddress(configDetail.getChunkAlteon().getVsConfig().getVsOld().getvIP());
			infoOld.setVsName(configDetail.getChunkAlteon().getVsConfig().getVsOld().getName());
			infoOld.setVsUseYN(configDetail.getChunkAlteon().getVsConfig().getVsOld().getUseYN());
		}

		if (configDetail.getAccessType() == OBDefine.CHANGE_TYPE_DELETE) // virtual server 삭제
		{
			infoNew = null;
		} else
		// virtual server 수정이나 추가
		{
			infoNew.setAlteonId(configDetail.getChunkAlteon().getVsConfig().getVsNew().getAlteonId());
			infoNew.setIfNum(configDetail.getChunkAlteon().getVsConfig().getVsNew().getIfNum());
			infoNew.setLastTime(configDetail.getOccurTime());
			infoNew.setSummary(configDetail.getSummary());
			if (configDetail.getSummary().equals("change : virtual server disable")) {
				infoNew.setState("disable");
				infoOld.setState("enable");
			} else if (configDetail.getSummary().equals("change : virtual server enable")) {
				infoNew.setState("enable");
				infoOld.setState("disable");
			}
			// infoNew.setVrrpYN(configDetail.getChunkAlteon().getVsConfig().getVsNew().isVrrpYN());
			// //VrrpYN 제거됐음
			infoNew.setVserviceList(configDetail.getChunkAlteon().getVsConfig().getVsNew().getVserviceList());
			infoNew.setVsIPAddress(configDetail.getChunkAlteon().getVsConfig().getVsNew().getvIP());
			infoNew.setVsName(configDetail.getChunkAlteon().getVsConfig().getVsNew().getName());
			infoNew.setVsUseYN(configDetail.getChunkAlteon().getVsConfig().getVsNew().getUseYN());
		}
		ArrayList<OBDtoAdcConfigHistory2> vsHistoryList = new ArrayList<OBDtoAdcConfigHistory2>();

		for (i = 0; i < listAll.size(); i++) {
			OBDtoAdcConfigHistory2 history = new OBDtoAdcConfigHistory2();
			history.setLogSeq(listAll.get(i).getDbIndex());
			history.setVirtualSvrIndex(listAll.get(i).getVsIndex());
			history.setAccountName(listAll.get(i).getAccountName());
			history.setSummary(listAll.get(i).getSummary());
			history.setOccurTime(listAll.get(i).getOccurTime());

			vsHistoryList.add(history);
		}
		info.setAdcIndex(configDetail.getAdcIndex());
		info.setVsIndex(configDetail.getVsIndex());
		info.setVsConfigInfoOld(infoOld);
		info.setVsConfigInfoNew(infoNew);
		info.setVsHistoryList(vsHistoryList);

		String recoverString = checkRecoverable(info.getAdcIndex(), vServerIndex, configDetail);
		if (recoverString != null && recoverString.isEmpty()) {
			info.setRecoverable(true);
		} else {
			info.setRecoverable(false);
		}
		return info;
	}

	public OBDtoAdcConfigHistoryPAS getVSHistoryInfoPAS(String vServerIndex, Integer logSeq) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoAdcConfigHistoryPAS info;
		try {
			db.openDB();

			info = getVSHistoryInfoPAS(vServerIndex, logSeq, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		return info;
	}

	public OBDtoAdcConfigHistoryPAS getVSHistoryInfoPAS(String vServerIndex, Integer logSeq, OBDatabase db)
			throws OBException {
		ArrayList<OBDtoAdcConfigHistoryDB> listAll = getConfigHistoryCore(vServerIndex, 0, false, db); // 가져올 데이터 카운트=0,
																										// 모두 가져온다.
		OBDtoAdcConfigHistoryDB configDetail = getConfigHistorySingle(vServerIndex, logSeq, db); // logSeq에 해당하는 리스트만
																									// 가져온다

		if (listAll == null || configDetail == null) {
			return null;
		} // listAll.size()=0은 불가능하기 때문에 확인하지 않음
		int i = 0;
		OBDtoAdcConfigHistoryPAS infoMain = new OBDtoAdcConfigHistoryPAS();
		OBDtoAdcConfigInfoPAS infoOld = new OBDtoAdcConfigInfoPAS();
		OBDtoAdcConfigInfoPAS infoNew = new OBDtoAdcConfigInfoPAS();
		if (configDetail.getAccessType() == OBDefine.CHANGE_TYPE_ADD) // virtual server 추가
		{
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "virtual server add, old info = null");
			infoOld = null;
		} else
		// virtual server 수정이나 삭제
		{
			if (listAll.size() < 2) // 첫 수정사례면 없을 수 있다.
			{
				infoOld.setLastTime(null);
			} else {
				infoOld.setLastTime(configDetail.getChunkPAS().getVsConfig().getVsOld().getApplyTime());
			}

			infoOld.setPool(configDetail.getChunkPAS().getVsConfig().getVsOld().getPool());
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					"[vs add] IP=" + configDetail.getChunkPAS().getVsConfig().getVsOld().getvIP());
			infoOld.setVsIPAddress(configDetail.getChunkPAS().getVsConfig().getVsOld().getvIP());
			infoOld.setVsName(configDetail.getChunkPAS().getVsConfig().getVsOld().getName());
			infoOld.setVsSrvPort(configDetail.getChunkPAS().getVsConfig().getVsOld().getSrvPort());
			infoOld.setState(configDetail.getChunkPAS().getVsConfig().getVsOld().getState());
		}
		if (configDetail.getAccessType() == OBDefine.CHANGE_TYPE_DELETE) // virtual server 삭제
		{
			infoNew = null;
		} else
		// virtual server 수정이나 추가
		{
			infoNew.setLastTime(configDetail.getOccurTime());
			infoNew.setPool(configDetail.getChunkPAS().getVsConfig().getVsNew().getPool());
			infoNew.setSummary(configDetail.getSummary());
			infoNew.setVsIPAddress(configDetail.getChunkPAS().getVsConfig().getVsNew().getvIP());
			infoNew.setVsName(configDetail.getChunkPAS().getVsConfig().getVsNew().getName());
			infoNew.setVsSrvPort(configDetail.getChunkPAS().getVsConfig().getVsNew().getSrvPort());
			infoNew.setState(configDetail.getChunkPAS().getVsConfig().getVsNew().getState());
		}
		ArrayList<OBDtoAdcConfigHistory2> vsHistoryList = new ArrayList<OBDtoAdcConfigHistory2>();

		for (i = 0; i < listAll.size(); i++) {
			OBDtoAdcConfigHistory2 history = new OBDtoAdcConfigHistory2();
			history.setLogSeq(listAll.get(i).getDbIndex());
			history.setVirtualSvrIndex(listAll.get(i).getVsIndex());
			history.setAccountName(listAll.get(i).getAccountName());
			history.setSummary(listAll.get(i).getSummary());
			history.setOccurTime(listAll.get(i).getOccurTime());

			vsHistoryList.add(history);
		}
		infoMain.setAdcIndex(configDetail.getAdcIndex());
		infoMain.setVsIndex(configDetail.getVsIndex());
		infoMain.setVsConfigInfoOld(infoOld);
		infoMain.setVsConfigInfoNew(infoNew);
		infoMain.setVsHistoryList(vsHistoryList);

		String recoverString = checkRecoverable(infoMain.getAdcIndex(), vServerIndex, configDetail);
		if (recoverString != null && recoverString.isEmpty()) {
			infoMain.setRecoverable(true);
		} else {
			infoMain.setRecoverable(false);
		}
		return infoMain;
	}

	public OBDtoAdcConfigHistoryPASK getVSConfigHistoryPASK(String vServerIndex, Integer logSeq, OBDatabase db)
			throws OBException {
		ArrayList<OBDtoAdcConfigHistoryDB> listAll = getConfigHistoryCore(vServerIndex, 0, false, db); // 가져올 데이터 카운트=0,
																										// 모두 가져온다.
		OBDtoAdcConfigHistoryDB configDetail = getConfigHistorySingle(vServerIndex, logSeq, db); // logSeq에 해당하는 리스트만
																									// 가져온다

		if (listAll == null || configDetail == null) {
			return null;
		}
		// list.size()=0은 불가능하기 때문에 확인하지 않음
		int i = 0;
		OBDtoAdcConfigHistoryPASK infoMain = new OBDtoAdcConfigHistoryPASK();
		OBDtoAdcConfigInfoPASK infoOld = new OBDtoAdcConfigInfoPASK();
		OBDtoAdcConfigInfoPASK infoNew = new OBDtoAdcConfigInfoPASK();
		if (configDetail.getAccessType() == OBDefine.CHANGE_TYPE_ADD) // virtual server 추가
		{
			infoOld = null; // virtual server add, old info = null
		} else
		// virtual server 수정이나 삭제
		{
			if (listAll.size() < 2) // 첫 수정사례면 없을 수 있다.
			{
				infoOld.setLastTime(null);
			} else {
				infoOld.setLastTime(configDetail.getChunkPASK().getVsConfig().getVsOld().getApplyTime());
			}

			infoOld.setPool(configDetail.getChunkPASK().getVsConfig().getVsOld().getPool());
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
					"[vs add] IP=" + configDetail.getChunkPASK().getVsConfig().getVsOld().getvIP());
			infoOld.setVsIPAddress(configDetail.getChunkPASK().getVsConfig().getVsOld().getvIP());
			infoOld.setVsName(configDetail.getChunkPASK().getVsConfig().getVsOld().getName());
			infoOld.setVsSrvPort(configDetail.getChunkPASK().getVsConfig().getVsOld().getSrvPort());
			infoOld.setState(configDetail.getChunkPASK().getVsConfig().getVsOld().getState());
			infoOld.setSubInfo(configDetail.getChunkPASK().getVsConfig().getVsOld().getSubInfo());
		}
		if (configDetail.getAccessType() == OBDefine.CHANGE_TYPE_DELETE) // virtual server 삭제
		{
			infoNew = null;
		} else
		// virtual server 수정이나 추가
		{
			infoNew.setLastTime(configDetail.getOccurTime());
			infoNew.setPool(configDetail.getChunkPASK().getVsConfig().getVsNew().getPool());
			infoNew.setSummary(configDetail.getSummary());
			infoNew.setVsIPAddress(configDetail.getChunkPASK().getVsConfig().getVsNew().getvIP());
			infoNew.setVsName(configDetail.getChunkPASK().getVsConfig().getVsNew().getName());
			infoNew.setVsSrvPort(configDetail.getChunkPASK().getVsConfig().getVsNew().getSrvPort());
			infoNew.setState(configDetail.getChunkPASK().getVsConfig().getVsNew().getState());
			infoNew.setSubInfo(configDetail.getChunkPASK().getVsConfig().getVsNew().getSubInfo());
		}
		ArrayList<OBDtoAdcConfigHistory2> vsHistoryList = new ArrayList<OBDtoAdcConfigHistory2>();

		for (i = 0; i < listAll.size(); i++) {
			OBDtoAdcConfigHistory2 history = new OBDtoAdcConfigHistory2();
			history.setLogSeq(listAll.get(i).getDbIndex());
			history.setVirtualSvrIndex(listAll.get(i).getVsIndex());
			history.setAccountName(listAll.get(i).getAccountName());
			history.setSummary(listAll.get(i).getSummary());
			history.setOccurTime(listAll.get(i).getOccurTime());

			vsHistoryList.add(history);
		}
		infoMain.setAdcIndex(configDetail.getAdcIndex());
		infoMain.setVsIndex(configDetail.getVsIndex());
		infoMain.setVsConfigInfoOld(infoOld);
		infoMain.setVsConfigInfoNew(infoNew);
		infoMain.setVsHistoryList(vsHistoryList);

		String recoverString = checkRecoverable(infoMain.getAdcIndex(), vServerIndex, configDetail);
		if (recoverString != null && recoverString.isEmpty()) {
			infoMain.setRecoverable(true);
		} else {
			infoMain.setRecoverable(false);
		}

		return infoMain;
	}

	public OBDtoAdcConfigHistoryDB getLastConfig(String vServerIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoAdcConfigHistoryDB info;
		try {
			db.openDB();

			info = getLastConfig(vServerIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		return info;
	}

	public OBDtoAdcConfigHistoryDB getLastConfig(String vServerIndex, OBDatabase db) throws OBException {
		ArrayList<OBDtoAdcConfigHistoryDB> list = getConfigHistoryCore(vServerIndex, 1, true, db); // 최근 1개만, 변경이력 상세정보
																									// chunk까지(true부분)
		// return : null 또는 list 엔트리, list가 null이 아니면 반드시 1개가 있어서 개수가 0이 될 수는 없으므로 개수 0
		// 확인하지 않음
		return list.get(0);
	}

	public OBDtoAdcConfigHistoryDB getLastConfigByLogIndex(Long logIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoAdcConfigHistoryDB info;
		try {
			db.openDB();

			info = getLastConfigByLogIndex(logIndex, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		return info;
	}

	private OBDtoAdcConfigHistoryDB getLastConfigByLogIndex(Long logIndex, OBDatabase db) throws OBException {
		String sqlText = "";
		ResultSet rs;
		Integer adcType = 0;
		OBDtoAdcConfigHistoryDB retVal = new OBDtoAdcConfigHistoryDB();

		if (logIndex == null || logIndex.equals(0)) {
			return null;
		}

		try {
			sqlText = String.format(
					" SELECT HI.LOG_SEQ, HI.LOG_TYPE, HI.ACCNT_INDEX, HI.ACCNT_NAME, HI.ADC_INDEX, ADC.TYPE AS ADC_TYPE, "
							+ "     HI.VS_INDEX, HI.VS_NAME, HI.VS_IP, HI.OBJECT_TYPE, HI.OCCUR_TIME, "
							+ "     HI.SUMMARY, HI.DESCRIPTION, HI.ACCESS_TYPE, HI.CONFIG_CHUNK, HI.VERSION "
							+ " FROM LOG_CONFIG_HISTORY HI " + " INNER JOIN MNG_ADC ADC ON HI.ADC_INDEX = ADC.INDEX "
							+ " WHERE HI.LOG_SEQ = %d ;",
					logIndex);

			rs = db.executeQuery(sqlText);

			if (rs.next()) {
				retVal.setDbIndex(db.getLong(rs, "LOG_SEQ"));
				retVal.setUserType(db.getInteger(rs, "LOG_TYPE"));
				retVal.setAccountIndex(db.getInteger(rs, "ACCNT_INDEX"));
				retVal.setAccountName(db.getString(rs, "ACCNT_NAME"));
				retVal.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				retVal.setVsIndex(db.getString(rs, "VS_INDEX"));
				retVal.setVsName(db.getString(rs, "VS_NAME"));
				retVal.setVsIp(db.getString(rs, "VS_IP"));
				retVal.setObjectType(db.getInteger(rs, "OBJECT_TYPE"));
				retVal.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				retVal.setSummary(db.getString(rs, "SUMMARY"));
				retVal.setDescription(db.getString(rs, "DESCRIPTION"));
				retVal.setAccessType(db.getInteger(rs, "ACCESS_TYPE"));
				ByteArrayInputStream bisConfigChunk = new ByteArrayInputStream(rs.getBytes("CONFIG_CHUNK"));
				ObjectInputStream oisConfigChunk = new ObjectInputStream(bisConfigChunk);

				retVal.setChunkAlteon(null);
				retVal.setChunkF5(null);
				retVal.setChunkPAS(null);
				retVal.setChunkPASK(null);

				retVal.setVersion(db.getString(rs, "VERSION"));

				adcType = db.getInteger(rs, "ADC_TYPE");

				if (adcType == OBDefine.ADC_TYPE_F5) {
					retVal.setChunkF5((OBDtoAdcConfigChunkF5) oisConfigChunk.readObject());
				} else if (adcType == OBDefine.ADC_TYPE_ALTEON) {
					retVal.setChunkAlteon((OBDtoAdcConfigChunkAlteon) oisConfigChunk.readObject());
				} else if (adcType == OBDefine.ADC_TYPE_PIOLINK_PAS) {
					retVal.setChunkPAS((OBDtoAdcConfigChunkPAS) oisConfigChunk.readObject());
				} else if (adcType == OBDefine.ADC_TYPE_PIOLINK_PASK) {
					retVal.setChunkPASK((OBDtoAdcConfigChunkPASK) oisConfigChunk.readObject());
				}
				return retVal;
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	public ArrayList<OBDtoAdcConfigHistoryDB> getConfigHistoryCore(String vServerIndex, int limit,
			boolean withConfigChunk, OBDatabase db) throws OBException {
		String sqlText = "";
		ResultSet rs;
		Integer adcType = 0;
		ArrayList<OBDtoAdcConfigHistoryDB> list = new ArrayList<OBDtoAdcConfigHistoryDB>();

		if (vServerIndex == null || vServerIndex.isEmpty() == true) {
			return null;
		}

		String limitClause = ""; // initially empty

		if (limit > 0) {
			limitClause = " LIMIT " + limit;
		}

		try {
			sqlText = String.format(
					" SELECT HI.LOG_SEQ, HI.LOG_TYPE, HI.ACCNT_INDEX, HI.ACCNT_NAME, HI.ADC_INDEX, ADC.TYPE AS ADC_TYPE, "
							+ "     HI.VS_INDEX, HI.VS_NAME, HI.VS_IP, HI.OBJECT_TYPE, HI.OCCUR_TIME, "
							+ "     HI.SUMMARY, HI.DESCRIPTION, HI.ACCESS_TYPE, HI.CONFIG_CHUNK, HI.VERSION "
							+ " FROM LOG_CONFIG_HISTORY HI " + " INNER JOIN MNG_ADC ADC ON HI.ADC_INDEX = ADC.INDEX "
							+ " WHERE HI.VS_INDEX = %s " + " ORDER BY HI.OCCUR_TIME DESC " + " %s ;",
					OBParser.sqlString(vServerIndex), limitClause);

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoAdcConfigHistoryDB obj = new OBDtoAdcConfigHistoryDB();
				obj.setDbIndex(db.getLong(rs, "LOG_SEQ"));
				obj.setUserType(db.getInteger(rs, "LOG_TYPE"));
				obj.setAccountIndex(db.getInteger(rs, "ACCNT_INDEX"));
				obj.setAccountName(db.getString(rs, "ACCNT_NAME"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setVsIndex(db.getString(rs, "VS_INDEX"));
				obj.setVsName(db.getString(rs, "VS_NAME"));
				obj.setVsIp(db.getString(rs, "VS_IP"));
				obj.setObjectType(db.getInteger(rs, "OBJECT_TYPE"));
				obj.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				obj.setSummary(db.getString(rs, "SUMMARY"));
				obj.setDescription(db.getString(rs, "DESCRIPTION"));
				obj.setAccessType(db.getInteger(rs, "ACCESS_TYPE"));

				obj.setChunkAlteon(null);
				obj.setChunkF5(null);
				obj.setChunkPAS(null);
				obj.setChunkPASK(null);

				obj.setVersion(db.getString(rs, "VERSION"));

				// 변경 이력 상세 chunk는 가져오기로 선택한 경우만 뽑는다. 요약정보만 필요해서 이 함수를 쓴다면 false로 call 한다.
				if (withConfigChunk == true) {
					ByteArrayInputStream bisConfigChunk = new ByteArrayInputStream(rs.getBytes("CONFIG_CHUNK"));
					ObjectInputStream oisConfigChunk = new ObjectInputStream(bisConfigChunk);
					adcType = db.getInteger(rs, "ADC_TYPE");

					if (adcType == OBDefine.ADC_TYPE_F5) {
						obj.setChunkF5((OBDtoAdcConfigChunkF5) oisConfigChunk.readObject());
					} else if (adcType == OBDefine.ADC_TYPE_ALTEON) {
						obj.setChunkAlteon((OBDtoAdcConfigChunkAlteon) oisConfigChunk.readObject());
					} else if (adcType == OBDefine.ADC_TYPE_PIOLINK_PAS) {
						obj.setChunkPAS((OBDtoAdcConfigChunkPAS) oisConfigChunk.readObject());
					} else if (adcType == OBDefine.ADC_TYPE_PIOLINK_PASK) {
						obj.setChunkPASK((OBDtoAdcConfigChunkPASK) oisConfigChunk.readObject());
					}
				}
				list.add(obj);
			}

			if (list.size() == 0) {
				list = null;
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return list;
	}

	// logSeq number로 특정 이력 하나만 뽑는다.
	// getConfigHistorySingle(String vServerIndex, int logSeq, OBDatabase db)를 db때문에
	// wrapping 하는 함수이다.
	public OBDtoAdcConfigHistoryDB getConfigHistorySingle(String vServerIndex, int logSeq) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoAdcConfigHistoryDB obj = null;

		try {
			db.openDB();

			if (vServerIndex == null || vServerIndex.isEmpty() == true) {
				return null;
			}
			obj = getConfigHistorySingle(vServerIndex, logSeq, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return obj;
	}

	// logㄴeq number로 특정 이력 하나만 뽑는다.
	public OBDtoAdcConfigHistoryDB getConfigHistorySingle(String vServerIndex, int logSeq, OBDatabase db)
			throws OBException {
		String sqlText = "";
		ResultSet rs;
		Integer adcType = 0;
		OBDtoAdcConfigHistoryDB obj = null;

		if (vServerIndex == null || vServerIndex.isEmpty() == true) {
			return null;
		}

		try {
			sqlText = String.format(
					" SELECT HI.LOG_SEQ, HI.LOG_TYPE, HI.ACCNT_INDEX, HI.ACCNT_NAME, HI.ADC_INDEX, ADC.TYPE AS ADC_TYPE, "
							+ "     HI.VS_INDEX, HI.VS_NAME, HI.VS_IP, HI.OBJECT_TYPE, HI.OCCUR_TIME, "
							+ "     HI.SUMMARY, HI.DESCRIPTION, HI.ACCESS_TYPE, HI.CONFIG_CHUNK, HI.VERSION "
							+ " FROM LOG_CONFIG_HISTORY HI " + " INNER JOIN MNG_ADC ADC ON HI.ADC_INDEX = ADC.INDEX "
							+ " WHERE HI.VS_INDEX = %s AND HI.LOG_SEQ = %d ",
					OBParser.sqlString(vServerIndex), logSeq);

			rs = db.executeQuery(sqlText);

			while (rs.next()) {
				obj = new OBDtoAdcConfigHistoryDB();
				obj.setDbIndex(db.getLong(rs, "LOG_SEQ"));
				obj.setUserType(db.getInteger(rs, "LOG_TYPE"));
				obj.setAccountIndex(db.getInteger(rs, "ACCNT_INDEX"));
				obj.setAccountName(db.getString(rs, "ACCNT_NAME"));
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setVsIndex(db.getString(rs, "VS_INDEX"));
				obj.setVsName(db.getString(rs, "VS_NAME"));
				obj.setVsIp(db.getString(rs, "VS_IP"));
				obj.setObjectType(db.getInteger(rs, "OBJECT_TYPE"));
				obj.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				obj.setSummary(db.getString(rs, "SUMMARY"));
				obj.setDescription(db.getString(rs, "DESCRIPTION"));
				obj.setAccessType(db.getInteger(rs, "ACCESS_TYPE"));
				ByteArrayInputStream bisConfigChunk = new ByteArrayInputStream(rs.getBytes("CONFIG_CHUNK"));
				ObjectInputStream oisConfigChunk = new ObjectInputStream(bisConfigChunk);

				obj.setChunkAlteon(null);
				obj.setChunkF5(null);
				obj.setChunkPAS(null);
				obj.setChunkPASK(null);

				obj.setVersion(db.getString(rs, "VERSION"));

				adcType = db.getInteger(rs, "ADC_TYPE");

				if (adcType == OBDefine.ADC_TYPE_F5) {
					obj.setChunkF5((OBDtoAdcConfigChunkF5) oisConfigChunk.readObject());
				} else if (adcType == OBDefine.ADC_TYPE_ALTEON) {
					obj.setChunkAlteon((OBDtoAdcConfigChunkAlteon) oisConfigChunk.readObject());
				} else if (adcType == OBDefine.ADC_TYPE_PIOLINK_PAS) {
					obj.setChunkPAS((OBDtoAdcConfigChunkPAS) oisConfigChunk.readObject());
				} else if (adcType == OBDefine.ADC_TYPE_PIOLINK_PASK) {
					obj.setChunkPASK((OBDtoAdcConfigChunkPASK) oisConfigChunk.readObject());
				}
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return obj;
	}

	public void writeConfigHistoryPAS(OBDtoAdcConfigChunkPAS configChunk) throws OBException {
		OBDtoAdcConfigHistoryDB history = new OBDtoAdcConfigHistoryDB();
		/* // getAccountList 가 아닌 getAccountInfo 로 변경하여 accountName 값을 가져온다. */
		// String accountName = new
		// OBAccountImpl().getAccountList(configChunk.getAccountIndex()).get(0).getAccountName();
		// String accountName = new
		// OBAccountImpl().getAccountInfo(configChunk.getAccountIndex()).getAccountName();
		Integer accountIndex = configChunk.getAccountIndex();
		OBDtoAccount accountInfo = new OBAccountImpl().getAccountInfo(accountIndex);
		String accountName = accountInfo.getAccountName();

		if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_DELETE) {
			history.setAdcIndex(configChunk.getVsConfig().getVsOld().getAdcIndex());
			history.setVsIndex(configChunk.getVsConfig().getVsOld().getDbIndex());
			history.setVsName(configChunk.getVsConfig().getVsOld().getName());
			history.setVsIp(configChunk.getVsConfig().getVsOld().getvIP());
			history.setVsStatus(configChunk.getVsConfig().getVsOld().getStatus());
		} else if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_ADD
				|| configChunk.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
			history.setAdcIndex(configChunk.getVsConfig().getVsNew().getAdcIndex());
			history.setVsIndex(configChunk.getVsConfig().getVsNew().getDbIndex());
			history.setVsName(configChunk.getVsConfig().getVsNew().getName());
			history.setVsIp(configChunk.getVsConfig().getVsNew().getvIP());
			history.setVsStatus(configChunk.getVsConfig().getVsNew().getStatus());
		} else
		// nothing changed
		{
			return;
		}

		history.setUserType(configChunk.getUserType());
		// 작업이 system에 의한 자동 감지 이력이면 "System" user로 설정, 사용자가 수정한 이력이면 사용자의 계정으로 등록

		if (configChunk.getUserType() == OBDefine.CHANGE_BY_USER) {
			history.setAccountIndex(configChunk.getAccountIndex());
			history.setAccountName(accountName);
		} else {
			history.setAccountIndex(OBDefine.SYSTEM_USER_INDEX);
			history.setAccountName(OBDefine.SYSTEM_USER_NAME);
		}

		history.setObjectType(configChunk.getChangeObject());
		history.setOccurTime(OBDateTime.toTimestamp(OBDateTime.now()));
		history.setDescription(null);
		history.setAccessType(configChunk.getChangeType());
		history.setChunkF5(null);
		history.setChunkAlteon(null);
		history.setChunkPAS(configChunk);
		history.setChunkPASK(null);
		history.setSummary(SummaryConfig(history));

		writeConfigHistory(history);
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "end.");
	}

	public void writeConfigHistoryPASK(OBDtoAdcConfigChunkPASK configChunk) throws OBException {
		OBDtoAdcConfigHistoryDB history = new OBDtoAdcConfigHistoryDB();
		/* // getAccountList 가 아닌 getAccountInfo 로 변경하여 accountName 값을 가져온다. */
		// String accountName = new
		// OBAccountImpl().getAccountList(configChunk.getAccountIndex()).get(0).getAccountName();
		// String accountName = new
		// OBAccountImpl().getAccountInfo(configChunk.getAccountIndex()).getAccountName();
		Integer accountIndex = configChunk.getAccountIndex();
		OBDtoAccount accountInfo = new OBAccountImpl().getAccountInfo(accountIndex);
		String accountName = accountInfo.getAccountName();

		if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_DELETE) {
			history.setAdcIndex(configChunk.getVsConfig().getVsOld().getAdcIndex());
			history.setVsIndex(configChunk.getVsConfig().getVsOld().getDbIndex());
			history.setVsName(configChunk.getVsConfig().getVsOld().getName());
			history.setVsIp(configChunk.getVsConfig().getVsOld().getvIP());
			history.setVsStatus(configChunk.getVsConfig().getVsOld().getStatus());
		} else if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_ADD
				|| configChunk.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
			history.setAdcIndex(configChunk.getVsConfig().getVsNew().getAdcIndex());
			history.setVsIndex(configChunk.getVsConfig().getVsNew().getDbIndex());
			history.setVsName(configChunk.getVsConfig().getVsNew().getName());
			history.setVsIp(configChunk.getVsConfig().getVsNew().getvIP());
			history.setVsStatus(configChunk.getVsConfig().getVsNew().getStatus());
		} else
		// nothing changed
		{
			return;
		}

		history.setUserType(configChunk.getUserType());
		// 작업이 system에 의한 자동 감지 이력이면 "System" user로 설정, 사용자가 수정한 이력이면 사용자의 계정으로 등록

		if (configChunk.getUserType() == OBDefine.CHANGE_BY_USER) {
			history.setAccountIndex(configChunk.getAccountIndex());
			history.setAccountName(accountName);
		} else {
			history.setAccountIndex(OBDefine.SYSTEM_USER_INDEX);
			history.setAccountName(OBDefine.SYSTEM_USER_NAME);
		}

		history.setObjectType(configChunk.getChangeObject());
		history.setOccurTime(OBDateTime.toTimestamp(OBDateTime.now()));
		history.setDescription(null);
		history.setAccessType(configChunk.getChangeType());
		history.setChunkF5(null);
		history.setChunkAlteon(null);
		history.setChunkPAS(null);
		history.setChunkPASK(configChunk);
		history.setSummary(SummaryConfig(history));

		writeConfigHistory(history);
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "end.");
	}

	public void writeConfigHistoryF5(OBDtoAdcConfigChunkF5 configChunk) throws OBException {
		OBDtoAdcConfigHistoryDB history = new OBDtoAdcConfigHistoryDB();
		/* // getAccountList 가 아닌 getAccountInfo 로 변경하여 accountName 값을 가져온다. */
		// String accountName = new
		// OBAccountImpl().getAccountList(configChunk.getAccountIndex()).get(0).getAccountName();
		// String accountName = new
		// OBAccountImpl().getAccountInfo(configChunk.getAccountIndex()).getAccountName();
		Integer accountIndex = configChunk.getAccountIndex();
		String accountName = "";
		if (accountIndex.equals(OBDefine.SYSTEM_USER_INDEX)) {
			accountName = OBDefine.SYSTEM_USER_NAME;
		} else {
			accountName = new OBAccountImpl().getAccountInfo(accountIndex).getAccountName();
		}

		if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_DELETE) {
			history.setAdcIndex(configChunk.getVsConfig().getVsOld().getAdcIndex());
			history.setVsIndex(configChunk.getVsConfig().getVsOld().getIndex());
			history.setVsName(configChunk.getVsConfig().getVsOld().getName());
			history.setVsIp(configChunk.getVsConfig().getVsOld().getvIP());
			history.setVsStatus(configChunk.getVsConfig().getVsOld().getStatus());
		} else if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_ADD
				|| configChunk.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
			history.setAdcIndex(configChunk.getVsConfig().getVsNew().getAdcIndex());
			history.setVsIndex(configChunk.getVsConfig().getVsNew().getIndex());
			history.setVsName(configChunk.getVsConfig().getVsNew().getName());
			history.setVsIp(configChunk.getVsConfig().getVsNew().getvIP());
			history.setVsStatus(configChunk.getVsConfig().getVsNew().getStatus());
		} else
		// changed nothing
		{
			return;
		}
		history.setUserType(configChunk.getUserType());
		// 작업이 system에 의한 자동 감지 이력이면 "System" user로 설정, 사용자가 수정한 이력이면 사용자의 계정으로 등록
		if (configChunk.getUserType() == OBDefine.CHANGE_BY_USER) {
			history.setAccountIndex(configChunk.getAccountIndex());
			history.setAccountName(accountName);
		} else {
			history.setAccountIndex(OBDefine.SYSTEM_USER_INDEX);
			history.setAccountName(OBDefine.SYSTEM_USER_NAME);
		}

		history.setObjectType(configChunk.getChangeObject());
		history.setOccurTime(OBDateTime.toTimestamp(OBDateTime.now()));
		history.setDescription(null);
		history.setAccessType(configChunk.getChangeType());
		history.setChunkF5(configChunk);
		history.setChunkAlteon(null);
		history.setChunkPAS(null);
		history.setChunkPASK(null);
		history.setSummary(SummaryConfig(history));

		writeConfigHistory(history);
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "end.");
	}

	public void writeConfigHistoryAlteon(OBDtoAdcConfigChunkAlteon configChunk) throws OBException {
		OBDtoAdcConfigHistoryDB history = new OBDtoAdcConfigHistoryDB();
		// String accountName = new
		// OBAccountImpl().getAccountList(configChunk.getAccountIndex()).get(0).getAccountName();
		Integer accountIndex = configChunk.getAccountIndex();
		String accountName = "";
		if (accountIndex.equals(OBDefine.SYSTEM_USER_INDEX)) {
			accountName = OBDefine.SYSTEM_USER_NAME;
		} else {
			accountName = new OBAccountImpl().getAccountInfo(accountIndex).getAccountName();
		}

		if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_DELETE) {
			history.setAdcIndex(configChunk.getVsConfig().getVsOld().getAdcIndex());
			history.setVsIndex(configChunk.getVsConfig().getVsOld().getIndex());
			history.setVsName(configChunk.getVsConfig().getVsOld().getName());
			history.setVsIp(configChunk.getVsConfig().getVsOld().getvIP());
			history.setVsStatus(configChunk.getVsConfig().getVsOld().getStatus());
		} else if (configChunk.getChangeType() == OBDefine.CHANGE_TYPE_ADD
				|| configChunk.getChangeType() == OBDefine.CHANGE_TYPE_EDIT) {
			history.setAdcIndex(configChunk.getVsConfig().getVsNew().getAdcIndex());
			history.setVsIndex(configChunk.getVsConfig().getVsNew().getIndex());
			history.setVsName(configChunk.getVsConfig().getVsNew().getName());
			history.setVsIp(configChunk.getVsConfig().getVsNew().getvIP());
			history.setVsStatus(configChunk.getVsConfig().getVsNew().getStatus());
		} else
		// changed nothing
		{
			return;
		}

		history.setUserType(configChunk.getUserType());
		// 작업이 system에 의한 자동 감지 이력이면 "System" user로 설정, 사용자가 수정한 이력이면 사용자의 계정으로 등록
		if (configChunk.getUserType() == OBDefine.CHANGE_BY_USER) {
			history.setAccountIndex(configChunk.getAccountIndex());
			history.setAccountName(accountName);
		} else {
			history.setAccountIndex(OBDefine.SYSTEM_USER_INDEX);
			history.setAccountName(OBDefine.SYSTEM_USER_NAME);
		}
		history.setObjectType(configChunk.getChangeObject());
		history.setOccurTime(OBDateTime.toDate(OBDateTime.toTimestamp(OBDateTime.now())));
		history.setDescription(null);
		history.setAccessType(configChunk.getChangeType());

		history.setChunkF5(null);
		history.setChunkAlteon(configChunk);
		history.setChunkPAS(null);
		history.setChunkPASK(null);
		history.setSummary(SummaryConfig(history));

		writeConfigHistory(history);
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "end.");
	}

	public void writeConfigHistory(OBDtoAdcConfigHistoryDB history) throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			writeConfigHistory(history, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void writeConfigHistory(OBDtoAdcConfigHistoryDB history, OBDatabase db) throws OBException {
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "start. history = " + history);
		String sqlText = "";
		try {
			sqlText = "INSERT INTO LOG_CONFIG_HISTORY "
					+ "	(LOG_TYPE, ACCNT_INDEX, ACCNT_NAME, ADC_INDEX, VS_INDEX, VS_NAME, VS_IP, "
					+ "    OBJECT_TYPE, OCCUR_TIME, SUMMARY, DESCRIPTION, ACCESS_TYPE, CONFIG_CHUNK, VERSION) "
					+ "	VALUES " + "	(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

			db.initPreparedStatement(sqlText);
			db.setPreparedStatementInt(1, history.getUserType());
			db.setPreparedStatementInt(2, history.getAccountIndex());
			db.setPreparedStatementString(3, history.getAccountName());
			db.setPreparedStatementInt(4, history.getAdcIndex());
			db.setPreparedStatementString(5, history.getVsIndex());
			db.setPreparedStatementString(6, history.getVsName());
			db.setPreparedStatementString(7, history.getVsIp());
			db.setPreparedStatementInt(8, history.getObjectType());
			db.setPreparedStatementTimestamp(9, new Timestamp(history.getOccurTime().getTime()));
			db.setPreparedStatementString(10, history.getSummary());
			db.setPreparedStatementString(11, null); // description
			db.setPreparedStatementInt(12, history.getAccessType());
			if (history.getChunkF5() != null) {
				db.setPreparedStatementObject(13, history.getChunkF5());
			} else if (history.getChunkAlteon() != null) {
				db.setPreparedStatementObject(13, history.getChunkAlteon());
			} else if (history.getChunkPAS() != null) {
				db.setPreparedStatementObject(13, history.getChunkPAS());
			} else if (history.getChunkPASK() != null) {
				db.setPreparedStatementObject(13, history.getChunkPASK());
			}
			db.setPreparedStatementString(14, CONFIG_VERSION); // description
			db.executeUpdatePreparedStreamt();
			db.deInitPreparedStatement();
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "end.");
	}

	public String SummaryConfig(OBDtoAdcConfigHistoryDB config) {
		String summary = "";

		if (config.getChunkF5() != null) {
			if (config.getChunkF5().getVsConfig().getChange() == OBDefine.CHANGE_TYPE_EDIT) {
				if (config.getChunkF5().getVsConfig().getUseynChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					if (config.getChunkF5().getVsConfig().getVsNew().getUseYN() != null) {
						if (config.getChunkF5().getVsConfig().getVsNew().getUseYN() == OBDefine.STATE_ENABLE) {
							summary += "virtual server enable";
						} else {
							summary += "virtual server disable";
						}
					}
				}

				if (config.getChunkF5().getVsConfig().getIpChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += String.format("virtual IP");
				}

				if (config.getChunkF5().getVsConfig().getPortChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "virtual port";
				}
				if (config.getChunkF5().getVsConfig().getPersistenceChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "persistence profile";
				}

				if (config.getChunkF5().getVsConfig().getPoolChange() != OBDefine.CHANGE_TYPE_NONE) {
					String poolSummary = "";
					if (config.getChunkF5().getVsConfig().getPoolChange() == OBDefine.CHANGE_TYPE_EDIT_NOTICEON) {
						poolSummary = "set notice pool";
					} else if (config.getChunkF5().getVsConfig()
							.getPoolChange() == OBDefine.CHANGE_TYPE_EDIT_NOTICEOFF) {
						poolSummary = "unset notice pool";
					} else if (config.getChunkF5().getVsConfig().getPoolConfig()
							.getChange() == OBDefine.CHANGE_TYPE_EDIT) {
						if (config.getChunkF5().getVsConfig().getPoolConfig()
								.getHealthCheckChange() != OBDefine.CHANGE_TYPE_NONE) {
							if (poolSummary.isEmpty() == false) {
								poolSummary += ", ";
							}
							poolSummary += "health monitor";
						}
						if (config.getChunkF5().getVsConfig().getPoolConfig()
								.getLbMethodChange() != OBDefine.CHANGE_TYPE_NONE) {
							if (poolSummary.isEmpty() == false) {
								poolSummary += ", ";
							}
							poolSummary += "load balancing";
						}
						if (config.getChunkF5().getVsConfig().getPoolConfig()
								.getMemberChange() != OBDefine.CHANGE_TYPE_NONE) {
							if (poolSummary.isEmpty() == false) {
								poolSummary += ", ";
							}
							poolSummary += "member";
						}
					}

					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "pool";

					if (poolSummary.isEmpty() == false) {
						summary += ("(" + poolSummary + ")");
					}
				}
			}
		} else if (config.getChunkAlteon() != null) {
			if (config.getChunkAlteon().getVsConfig().getChange() == OBDefine.CHANGE_TYPE_EDIT) {
				if (config.getChunkAlteon().getVsConfig().getUseYNChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					if (config.getChunkAlteon().getVsConfig().getVsNew().getUseYN() != null) {
						if (config.getChunkAlteon().getVsConfig().getVsNew().getUseYN() == OBDefine.STATE_ENABLE) {
							summary += "virtual server enable";
						} else {
							summary += "virtual server disable";
						}
					}
				}

				if (config.getChunkAlteon().getVsConfig().getAlteonIdChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "id";

				}
				if (config.getChunkAlteon().getVsConfig().getIfNumChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "interface";
				}

				if (config.getChunkAlteon().getVsConfig().getNameChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "virtual server name";
				}

				if (config.getChunkAlteon().getVsConfig().getRouterIndexChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "router index";
				}

				if (config.getChunkAlteon().getVsConfig().getvIPChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "virtual server IP";
				}

				if (config.getChunkAlteon().getVsConfig().getVrIndexChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "VR index";
				}

				if (config.getChunkAlteon().getVsConfig().getVrrpYNChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "vrrp";
				}

				if (config.getChunkAlteon().getVsConfig().getServiceChange() != OBDefine.CHANGE_TYPE_NONE) {
					boolean bServiceChange = false;
					boolean bPoolChange = false;
					boolean bHealthChange = false;
					boolean bLBChange = false;
					boolean bMemberChange = false;

					String poolSummary = "";

					bServiceChange = true;
					if (config.getChunkAlteon().getVsConfig().getServiceConfigList() != null) {
						for (int i = 0; i < config.getChunkAlteon().getVsConfig().getServiceConfigList().size(); i++) {
							if (bPoolChange && bHealthChange && bLBChange && bMemberChange) // 바뀐 항목을 파악하기 위한 검사이기 때문에
																							// 모두 바뀐 것이 감지됐으면 더 하지 않는다.
							{
								break;
							}
							if (config.getChunkAlteon().getVsConfig().getServiceConfigList().get(i)
									.getChange() != OBDefine.CHANGE_TYPE_NONE) {
								if (config.getChunkAlteon().getVsConfig().getServiceConfigList().get(i)
										.getPoolChange() != OBDefine.CHANGE_TYPE_NONE) {
									bPoolChange = true;
									if (config.getChunkAlteon().getVsConfig().getServiceConfigList().get(i)
											.getPoolConfig() != null) {
										if (config.getChunkAlteon().getVsConfig().getServiceConfigList().get(i)
												.getPoolConfig().getHealthCheckChange() != OBDefine.CHANGE_TYPE_NONE) {
											bHealthChange = true;
										}
										if (config.getChunkAlteon().getVsConfig().getServiceConfigList().get(i)
												.getPoolConfig().getLbMethodChange() != OBDefine.CHANGE_TYPE_NONE) {
											bLBChange = true;
										}

										if (config.getChunkAlteon().getVsConfig().getServiceConfigList().get(i)
												.getPoolConfig().getMemberChange() != OBDefine.CHANGE_TYPE_NONE) {
											bMemberChange = true;
										}
									}
								}
								if (config.getChunkAlteon().getVsConfig().getServiceConfigList().get(i)
										.getRealPortChange() != OBDefine.CHANGE_TYPE_NONE) {

								}
								if (config.getChunkAlteon().getVsConfig().getServiceConfigList().get(i)
										.getServicePortChange() != OBDefine.CHANGE_TYPE_NONE) {

								}
							}
						}
					}

					if (bHealthChange == true) {
						if (poolSummary.isEmpty() == false) {
							poolSummary += ", ";
						}
						poolSummary += "health monitor";
					}

					if (bLBChange == true) {
						if (poolSummary.isEmpty() == false) {
							poolSummary += ", ";
						}
						poolSummary += "load balancing";
					}
					if (bMemberChange == true) {
						if (poolSummary.isEmpty() == false) {
							poolSummary += ", ";
						}
						poolSummary += "member";
					}

					if (bServiceChange == true) {
						if (summary.isEmpty() == false) {
							summary += ", ";
						}
						summary += "virtual service";
					}
					if (bPoolChange == true) {
						if (summary.isEmpty() == false) {
							summary += ", ";
						}
						summary += "pool";

						if (poolSummary.isEmpty() == false) {
							summary += ("(" + poolSummary + ")");
						}
					}
				}
			}
		} else if (config.getChunkPAS() != null) {
			if (config.getChunkPAS().getVsConfig().getChange() == OBDefine.CHANGE_TYPE_EDIT) {
				if (config.getChunkPAS().getVsConfig().getStateChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					if (config.getChunkPAS().getVsConfig().getVsNew().getState() != null) {
						if (config.getChunkPAS().getVsConfig().getVsNew().getState() == OBDefine.STATE_ENABLE) {
							summary += "virtual server enable";
						} else {
							summary += "virtual server disable";
						}
					}
				}
				if (config.getChunkPAS().getVsConfig().getIpChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += String.format("virtual IP");
				}
				if (config.getChunkPAS().getVsConfig().getProtocolChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "protocol";
				}
				if (config.getChunkPAS().getVsConfig().getPortChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "virtual port";
				}

				if (config.getChunkPAS().getVsConfig().getPoolConfig().getChange() != OBDefine.CHANGE_TYPE_NONE) {
					String poolSummary = "";
					if (config.getChunkPAS().getVsConfig().getPoolConfig().getChange() == OBDefine.CHANGE_TYPE_EDIT) {
						if (config.getChunkPAS().getVsConfig().getPoolConfig()
								.getHealthCheckChange() != OBDefine.CHANGE_TYPE_NONE) {
							if (poolSummary.isEmpty() == false) {
								poolSummary += ", ";
							}
							poolSummary += "health monitor";
						}
						if (config.getChunkPAS().getVsConfig().getPoolConfig()
								.getLbMethodChange() != OBDefine.CHANGE_TYPE_NONE) {
							if (poolSummary.isEmpty() == false) {
								poolSummary += ", ";
							}
							poolSummary += "load balancing";
						}
						if (config.getChunkPAS().getVsConfig().getPoolConfig()
								.getMemberChange() != OBDefine.CHANGE_TYPE_NONE) {
							if (poolSummary.isEmpty() == false) {
								poolSummary += ", ";
							}
							poolSummary += "member";
						}
					}

					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "pool";

					if (poolSummary.isEmpty() == false) {
						summary += ("(" + poolSummary + ")");
					}
				}
			}
		} else if (config.getChunkPASK() != null) {
			if (config.getChunkPASK().getVsConfig().getChange() == OBDefine.CHANGE_TYPE_EDIT) {
				if (config.getChunkPASK().getVsConfig().getStateChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					if (config.getChunkPASK().getVsConfig().getVsNew().getState() != null) {
						if (config.getChunkPASK().getVsConfig().getVsNew().getState() == OBDefine.STATE_ENABLE) {
							summary += "virtual server enable";
						} else {
							summary += "virtual server disable";
						}
					}
				}
				if (config.getChunkPASK().getVsConfig().getIpChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += String.format("virtual IP");
				}
				if (config.getChunkPASK().getVsConfig().getProtocolChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "protocol";
				}
				if (config.getChunkPASK().getVsConfig().getPortChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "virtual port";
				}
				if (config.getChunkPASK().getVsConfig().getMemberChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "member";
				}
				if (config.getChunkPASK().getVsConfig().getLbMethodChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "load balancing";
				}
				if (config.getChunkPASK().getVsConfig().getHealthCheckChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "health-check";
				}
				if (config.getChunkPASK().getVsConfig().getSubinfoChange() != OBDefine.CHANGE_TYPE_NONE) {
					if (summary.isEmpty() == false) {
						summary += ", ";
					}
					summary += "extra virtual ip/port";
				}
			}
		}
		if (config.getAccessType() == OBDefine.CHANGE_TYPE_ADD) {
			summary = "Virtual server created.";
		} else if (config.getAccessType() == OBDefine.CHANGE_TYPE_EDIT) {
			summary = ("change : " + summary);
		} else if (config.getAccessType() == OBDefine.CHANGE_TYPE_DELETE) {
			summary = "Virtual server deleted.";
		}
		return summary;
	}

	public Integer getConfigHistoryListTotalRecordCount(int adcIndex, String searchKey, Date beginTime, Date endTime,
			OBDatabase db) throws OBException {
		Integer recCount = 0;
		String sqlText = "";
		String wildcardKey = "";
		String whereClauseKey = "";
		String whereClauseTime = "";
		try {
			sqlText = " SELECT COUNT(*) AS CNT FROM LOG_CONFIG_HISTORY ";

			if (adcIndex == 0) {
				sqlText += String.format(" WHERE TRUE "); // INDEX 조건이 없다. 덧붙는 조건의 AND에 맞추기 위해 TREU를 넣는다. SQL 실행에 부하가 없고
															// 결과에도 영향이 없다.
			} else {
				sqlText += String.format(" WHERE ADC_INDEX = %d \n", adcIndex);
			}
			if (searchKey != null) {
				// #3984-2 #15: 14.07.25 sw.jung 검색어 와일드카드 이스케이프
				wildcardKey = OBParser.sqlString("%" + OBParser.removeWildcard(searchKey) + "%");
				whereClauseKey = String.format(" AND (VS_NAME LIKE %s OR VS_IP LIKE %s OR SUMMARY LIKE %s) ",
						wildcardKey, wildcardKey, wildcardKey);
			}

			if (beginTime != null) {
				whereClauseTime = String.format(" AND OCCUR_TIME >= %s ",
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));
				if (endTime != null) {
					whereClauseTime += String.format(" AND OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			} else {
				if (endTime != null) {
					whereClauseTime += String.format(" AND OCCUR_TIME <= %s ",
							OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))));
				}
			}

			sqlText += String.format("%s %s ;", whereClauseKey, whereClauseTime);

			ResultSet rs;
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				recCount = db.getInteger(rs, "CNT");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		return recCount;
	}

	public Timestamp getLastConfigTime(int adcIndex, String vsIndex) throws OBException {
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = String.format(
					" SELECT OCCUR_TIME " + " FROM LOG_CONFIG_HISTORY " + " WHERE ADC_INDEX = %d "
							+ " AND VS_INDEX = %s " + " ORDER BY OCCUR_TIME DESC " + " LIMIT 1 ;",
					adcIndex, OBParser.sqlString(vsIndex));
			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == false)
				return null;
			return db.getTimestamp(rs, "OCCUR_TIME");
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public HashMap<String, Timestamp> getLastConfigTimeList(int adcIndex, OBDatabase db) throws OBException {
		HashMap<String, Timestamp> result = new HashMap<String, Timestamp>();
		String sqlText = "";
		try {
			sqlText = String.format(" SELECT OCCUR_TIME, VS_INDEX " + " FROM LOG_CONFIG_HISTORY " + " WHERE LOG_SEQ IN "
					+ "                 ( SELECT MAX(LOG_SEQ) " + "                   FROM LOG_CONFIG_HISTORY "
					+ "                   WHERE ADC_INDEX=%d " + "					GROUP BY VS_INDEX"
					+ "                  ) " + " ORDER BY LOG_SEQ ;", adcIndex);
			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				result.put(db.getString(rs, "VS_INDEX"), db.getTimestamp(rs, "OCCUR_TIME"));
			}
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
	}

	// DBCP 적용을 위해서 새롭게 만들었다.
	public HashMap<String, Timestamp> getLastConfigTimeList(int adcIndex) throws OBException {
		HashMap<String, Timestamp> result = new HashMap<String, Timestamp>();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			sqlText = String.format(" SELECT OCCUR_TIME, VS_INDEX " + " FROM LOG_CONFIG_HISTORY " + " WHERE LOG_SEQ IN "
					+ "                 ( SELECT MAX(LOG_SEQ) " + "                   FROM LOG_CONFIG_HISTORY "
					+ "                   WHERE ADC_INDEX=%d " + "                   GROUP BY VS_INDEX"
					+ "                  ) " + " ORDER BY LOG_SEQ ;", adcIndex);
			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				result.put(db.getString(rs, "VS_INDEX"), db.getTimestamp(rs, "OCCUR_TIME"));
			}
			return result;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null) {
				db.closeDB();
			}
		}
	}

	// old 설정을 old vserver list로 빼 놓는다.
	public OBDtoAdcConfigMixAlteon PrepareHistoryAlteon(int adcIndex) throws OBException {
		OBVServerDB vsDB = new OBVServerDB();

		// 전체 old virtual server 목록을 만든다. 새 정보로 업데이트 하면 old data set은 없어지니까 지금 만들어야 한다.
		ArrayList<OBDtoAdcVServerAlteon> vsList = vsDB.getVServerListAllAlteon(adcIndex); // 새 virtual server를 쭉 구한다,
																							// old와 비교할 때 씀

		// 전체 old pool 목록
		ArrayList<OBDtoAdcPoolAlteon> poolList = vsDB.getPoolListAllAlteon(adcIndex);

		OBDtoAdcConfigMixAlteon tempConfig = new OBDtoAdcConfigMixAlteon();
		tempConfig.setVsList(vsList);
		tempConfig.setPoolList(poolList);
		return tempConfig;
	}

	// old 설정 보관
	// adcIndex에 해당하는 장비의 전체 vs 변경정보(chunk list)를 준비한다. -- 시스템 변경 이력을 남길 때 쓴다.
	public OBDtoAdcConfigMixF5 PrepareHistoryF5System(int adcIndex) throws OBException {
		OBVServerDB vsDB = new OBVServerDB();
		OBDtoAdcConfigMixF5 tempConfig = new OBDtoAdcConfigMixF5(); // 리턴

		// 전체 old virtual server 목록을 만든다. 새 정보로 업데이트 하면 old data set은 없어지니까 지금 만들어야 한다.
		ArrayList<OBDtoAdcVServerF5> vsList = vsDB.getVServerListAllF5(adcIndex); // 새 virtual server를 쭉 구한다, old와 비교할 때
																					// 씀

		// 전체 old pool 목록
		ArrayList<OBDtoAdcPoolF5> poolList = vsDB.getPoolListAllF5(adcIndex);

		tempConfig.setVsList(vsList);
		tempConfig.setPoolList(poolList);

		return tempConfig;
	}

	// old 설정 보관
	// adcIndex에 해당하는 장비의 전체 vs 변경정보(chunk list)를 준비한다. -- 시스템 변경 이력을 남길 때 쓴다.
	// OBDtoAdcConfigMixPAS.poollist는 필요없을텐데. 제거 필요 //TODO
	public OBDtoAdcConfigMixPAS PrepareHistoryPAS(int adcIndex) throws OBException {
		OBVServerDB vsDB = new OBVServerDB();
		OBDtoAdcConfigMixPAS tempConfig = new OBDtoAdcConfigMixPAS(); // 리턴

		// 전체 old virtual server 목록을 만든다. 새 정보로 업데이트 하면 old data set은 없어지니까 지금 만들어야 한다.
		ArrayList<OBDtoAdcVServerPAS> vsList = vsDB.getVServerListAllPAS(adcIndex); // 새 virtual server를 쭉 구한다, old와 비교할
																					// 때 씀

		// 전체 old pool 목록
		ArrayList<OBDtoAdcPoolPAS> poolList = vsDB.getPoolListAllPAS(adcIndex);

		tempConfig.setVsList(vsList);
		tempConfig.setPoolList(poolList);

		return tempConfig;
	}

	public OBDtoAdcConfigMixPASK PrepareHistoryPASK(int adcIndex) throws OBException {
		OBVServerDB vsDB = new OBVServerDB();
		OBDtoAdcConfigMixPASK tempConfig = new OBDtoAdcConfigMixPASK(); // 리턴

		// 전체 old virtual server 목록을 만든다. 새 정보로 업데이트 하면 old data set은 없어지니까 지금 만들어야 한다.
		ArrayList<OBDtoAdcVServerPASK> vsList = vsDB.getVServerListAllPASK(adcIndex); // 새 virtual server를 쭉 구한다, old와
																						// 비교할 때 씀

		// 전체 old pool 목록
		ArrayList<OBDtoAdcPoolPASK> poolList = vsDB.getPoolListAllPASK(adcIndex);

		tempConfig.setVsList(vsList);
		tempConfig.setPoolList(poolList);

		return tempConfig;
	}

	// old 설정 보관
	// virtual server의 변경 정보(chunk)를 준비한다. -- vs 개별 설정이력을 남길 때 쓴다.
	public OBDtoAdcConfigChunkF5 MakeConfigChunkF5(OBDtoAdcVServerF5 virtualServerNew, int userType, int changeType,
			int changeObject, int accountIndex) throws OBException {
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, String.format("start, VS = %s", virtualServerNew));
		OBDtoAdcConfigChunkF5 configChunk = new OBDtoAdcConfigChunkF5(); // 리턴할 값

		OBDtoAdcConfigVServerF5 vsConfig = null;

		OBDtoAdcVServerF5 vsOld = new OBDtoAdcVServerF5();
		OBDtoAdcVServerF5 vsNew = new OBDtoAdcVServerF5();
		OBDtoAdcPoolF5 poolOrg = new OBDtoAdcPoolF5();

		OBVServerDB vsDB = new OBVServerDB();
		if (changeType == OBDefine.CHANGE_TYPE_ADD) {
			vsOld = null;
			vsNew = CommonF5.cloneVServer(virtualServerNew);
			vsNew.setIndex(OBCommon.makeVSIndexF5(vsNew.getAdcIndex(), vsNew.getName())); // 신규추가일 때는 vsIndex가 확정되지 않았기
																							// 때문에 미리구해준다.
			if (virtualServerNew.getPool() != null) {
				poolOrg = vsDB.getPoolInfoF5(virtualServerNew.getPool().getIndex()); // 새 VS에서도 기존 pool을 쓸 수 있기 때문에 원래
																						// pool이 있는지 봐야 한다.
			} else {
				poolOrg = null;
			}
		} else if (changeType == OBDefine.CHANGE_TYPE_DELETE) {
			vsOld = CommonF5.cloneVServer(virtualServerNew);
			vsNew = null;
		} else
		// virtual server edit
		{
			// old Virtual 서버 확인
			vsOld = vsDB.getVServerInfoF5(virtualServerNew.getIndex());
			if (vsOld == null) {
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "VirtualServer doesn't exist.");
			}

			vsNew = CommonF5.cloneVServer(virtualServerNew);
			if (virtualServerNew.getPool() != null) {
				poolOrg = vsDB.getPoolInfoF5(virtualServerNew.getPool().getIndex());
			} else {
				poolOrg = null;
			}
		}
		vsConfig = DiffVServerF5(vsOld, vsNew, poolOrg); // 두 virtual server의 차이 비교
		// 기본데이터 보강
		configChunk.setUserType(userType);
		configChunk.setChangeType(vsConfig.getChange());
		configChunk.setChangeObject(changeObject);
		configChunk.setAccountIndex(accountIndex);
		configChunk.setVsConfig(vsConfig);
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "end");
		return configChunk;
	}

	public OBDtoAdcConfigChunkF5 MakeConfigChunkBySystemF5(OBDtoAdcVServerF5 vsOld, OBDtoAdcVServerF5 vsNew,
			OBDtoAdcPoolF5 poolOrg) throws Exception {
		OBDtoAdcConfigChunkF5 chunk = new OBDtoAdcConfigChunkF5(); // 리턴할 값

		OBDtoAdcConfigVServerF5 vsConfig = null;

		vsConfig = DiffVServerF5(vsOld, vsNew, poolOrg); // 두 virtual server의 차이 비교

		// 기본데이터
		vsConfig.setVsNew(vsNew);
		vsConfig.setVsOld(vsOld);

		chunk.setAccountIndex(0);
		chunk.setChangeObject(OBDefine.CHANGE_OBJECT_VIRTUALSERVER);
		chunk.setChangeType(vsConfig.getChange());
		chunk.setUserType(OBDefine.CHANGE_BY_SYSTEM);
		chunk.setVsConfig(vsConfig);

		return chunk;
	}

	public OBDtoAdcConfigChunkPAS MakeConfigChunkBySystemPAS(OBDtoAdcVServerPAS vsOld, OBDtoAdcVServerPAS vsNew)
			throws OBException {
		OBDtoAdcConfigChunkPAS chunk = new OBDtoAdcConfigChunkPAS(); // 리턴할 값
		OBDtoAdcConfigVServerPAS vsConfig = null;
		try {
			vsConfig = DiffVServerPAS(vsOld, vsNew); // 두 virtual server의 차이 비교
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// , String.format("%s", e.getMessage()));
		}
		// 기본데이터
		vsConfig.setVsNew(vsNew);
		vsConfig.setVsOld(vsOld);

		chunk.setAccountIndex(0);
		chunk.setChangeObject(OBDefine.CHANGE_OBJECT_VIRTUALSERVER);
		chunk.setChangeType(vsConfig.getChange());
		chunk.setUserType(OBDefine.CHANGE_BY_SYSTEM);
		chunk.setVsConfig(vsConfig);

		return chunk;
	}

	public OBDtoAdcConfigChunkPASK MakeConfigChunkBySystemPASK(OBDtoAdcVServerPASK vsOld, OBDtoAdcVServerPASK vsNew)
			throws OBException {
		OBDtoAdcConfigChunkPASK chunk = new OBDtoAdcConfigChunkPASK(); // 리턴할 값
		OBDtoAdcConfigVServerPASK vsConfig = null;
		try {
			vsConfig = diffVServerPASK(vsOld, vsNew); // 두 virtual server의 차이 비교
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// , String.format("%s", e.getMessage()));
		}
		// 기본데이터
		vsConfig.setVsNew(vsNew);
		vsConfig.setVsOld(vsOld);

		chunk.setAccountIndex(0);
		chunk.setChangeObject(OBDefine.CHANGE_OBJECT_VIRTUALSERVER);
		chunk.setChangeType(vsConfig.getChange());
		chunk.setUserType(OBDefine.CHANGE_BY_SYSTEM);
		chunk.setVsConfig(vsConfig);

		return chunk;
	}

	public OBDtoAdcConfigVServerF5 DiffVServerF5(OBDtoAdcVServerF5 vsOld, OBDtoAdcVServerF5 vsNew,
			OBDtoAdcPoolF5 poolOrg) throws OBException {
		OBDtoAdcConfigVServerF5 vsConfig = new OBDtoAdcConfigVServerF5(); // 리턴할 값

		vsConfig.setChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setIpChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setPortChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setPersistenceChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setPoolChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setUseynChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setVlanFilterChange(OBDefine.CHANGE_TYPE_NONE);

		vsConfig.setPoolConfig(null);
		vsConfig.setVsNew(null);
		vsConfig.setVsOld(null);
		vsConfig.setNodeConfigList(null);

		if (vsOld == null) {
			vsConfig.setChange(OBDefine.CHANGE_TYPE_ADD);
		} else if (vsNew == null) {
			vsConfig.setChange(OBDefine.CHANGE_TYPE_DELETE);
		} else
		// virtual server edit
		{
			if (vsOld.getvIP().equals(vsNew.getvIP()) == false) {
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setIpChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			if (vsOld.getServicePort().equals(vsNew.getServicePort()) == false) {
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setPortChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			if (vsOld.getPersistence() == null) {
				if (vsNew.getPersistence() != null) {
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					vsConfig.setPersistenceChange(OBDefine.CHANGE_TYPE_ADD);
				}
			} else {
				if (vsNew.getPersistence() == null) {
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					vsConfig.setPersistenceChange(OBDefine.CHANGE_TYPE_DELETE);
				} else {
					if (vsNew.getPersistence().equals(vsOld.getPersistence()) == false) {
						vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
						vsConfig.setPersistenceChange(OBDefine.CHANGE_TYPE_EDIT);
					}
				}
			}
			if (vsOld.getVlanFilter() == null) {
				if (vsNew.getVlanFilter() != null) {
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					vsConfig.setVlanFilterChange(OBDefine.CHANGE_TYPE_ADD);
				}
			} else {
				if (vsNew.getVlanFilter().equals(vsOld.getVlanFilter()) == false) {
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					vsConfig.setVlanFilterChange(OBDefine.CHANGE_TYPE_EDIT);
				}
			}
			if (vsOld.getPool() == null && vsNew.getPool() == null) {
				vsConfig.setPoolChange(OBDefine.CHANGE_TYPE_NONE);

			} else if (vsOld.getPool() == null && vsNew.getPool() != null) {
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setPoolChange(OBDefine.CHANGE_TYPE_ADD);
			} else if (vsOld.getPool() != null && vsNew.getPool() == null) {
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setPoolChange(OBDefine.CHANGE_TYPE_DELETE);
			} else
			// pool 교체
			{
				if (vsOld.getPool().getIndex().equals(vsNew.getPool().getIndex()) == false) {
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					OBAdcManagementImpl adcManager = new OBAdcManagementImpl();
					if (adcManager.isGroupNotice(vsOld.getPool().getIndex()) == true
							&& adcManager.isGroupNotice(vsNew.getPool().getIndex()) == false) {
						vsConfig.setPoolChange(OBDefine.CHANGE_TYPE_EDIT_NOTICEOFF);
					} else if (adcManager.isGroupNotice(vsOld.getPool().getIndex()) == false
							&& adcManager.isGroupNotice(vsNew.getPool().getIndex()) == true) {
						vsConfig.setPoolChange(OBDefine.CHANGE_TYPE_EDIT_NOTICEON);
					} else
					// 단순 변경
					{
						vsConfig.setPoolChange(OBDefine.CHANGE_TYPE_EDIT);
					}
				} else {
					vsConfig.setPoolChange(OBDefine.CHANGE_TYPE_NONE);
				}
			}
			if (vsOld.getUseYN() == null && vsNew.getUseYN() == null) {
				vsConfig.setUseynChange(OBDefine.CHANGE_TYPE_NONE);
			} else if (vsOld.getUseYN() == null && vsNew.getUseYN() != null) {
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setUseynChange(OBDefine.CHANGE_TYPE_EDIT);
			} else if (vsOld.getUseYN() != null && vsNew.getUseYN() == null) {
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setUseynChange(OBDefine.CHANGE_TYPE_EDIT);
			} else {
				if (vsOld.getUseYN().equals(vsNew.getUseYN()) == false) {
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					vsConfig.setUseynChange(OBDefine.CHANGE_TYPE_EDIT);
				} else {
					vsConfig.setUseynChange(OBDefine.CHANGE_TYPE_NONE);
				}
			}
		}

		if (vsNew != null) {
			// POOL
			OBDtoAdcPoolF5 poolNew = new OBDtoAdcPoolF5();

			OBDtoAdcConfigPoolF5 poolConfig = new OBDtoAdcConfigPoolF5();
			poolConfig.setChange(OBDefine.CHANGE_TYPE_NONE);
			poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_NONE);
			poolConfig.setLbMethodChange(OBDefine.CHANGE_TYPE_NONE);
			poolConfig.setMemberChange(OBDefine.CHANGE_TYPE_NONE);
			if (vsNew.getPool() != null && vsNew.getPool().getName() != null) // pool은 pool 독자적인 변화의 기록이기 때문에 결과쪽 pool이
																				// 없으면 안 한다.//처음에 getIndex로 실제 pool이 있나
																				// 확인하게 코드를 넣었는데, 신규 pool이라 index가 없다.
																				// --; 사실은 pool이 없는 vs 이면 getPool()에서
																				// null이 나오게 하는 것이 맞다.
			{
				poolNew = CommonF5.clonePool(vsNew.getPool());
				// poolOld = vsDB.getPoolInfoF5(vsNew.getPool().getIndex()); //System에서 쓸 수 없다.
				if (poolOrg == null) {
					poolConfig.setChange(OBDefine.CHANGE_TYPE_ADD);

					OBDtoAdcConfigVServerF5 temp = new OBDtoAdcConfigVServerF5();
					temp = DiffPoolMemberF5(vsNew.getAdcIndex(), null, poolNew.getMemberList());

					poolConfig.setMemberConfigList(temp.getPoolConfig().getMemberConfigList());
					// node, node del은 없다.
					vsConfig.setNodeConfigList(temp.getNodeConfigList());
				} else {
					if (poolOrg.getHealthCheck() == null && poolNew.getHealthCheck() != null) {
						poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_ADD);
						poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					} else if (poolOrg.getHealthCheck() != null && poolNew.getHealthCheck() == null) {
						poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_DELETE);
						poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					} else if (poolOrg.getHealthCheck() != null && poolNew.getHealthCheck() != null) {
						if (poolOrg.getHealthCheck().equals(poolNew.getHealthCheck()) == false) {
							poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_EDIT);
							poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
						}
					}
					if (poolOrg.getLbMethod().equals(poolNew.getLbMethod()) == false) {
						poolConfig.setLbMethodChange(OBDefine.CHANGE_TYPE_EDIT);
						poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					}

					OBDtoAdcConfigVServerF5 temp = new OBDtoAdcConfigVServerF5();
					temp = DiffPoolMemberF5(vsNew.getAdcIndex(), poolOrg.getMemberList(), poolNew.getMemberList());
					if (temp.getPoolConfig().getMemberConfigList() != null
							&& temp.getPoolConfig().getMemberConfigList().size() != 0) {
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "changed(add/del/edit) member count = "
								+ temp.getPoolConfig().getMemberConfigList().size());

						poolConfig.setMemberChange(OBDefine.CHANGE_TYPE_EDIT);
						poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					}
					poolConfig.setMemberConfigList(temp.getPoolConfig().getMemberConfigList());

					vsConfig.setNodeConfigList(temp.getNodeConfigList()); // node, node del은 없다.

				} // pool delete는 없다.
				poolConfig.setPoolOld(poolOrg);
				poolConfig.setPoolNew(poolNew);
			}

			if (vsConfig.getChange() != OBDefine.CHANGE_TYPE_ADD && vsConfig.getChange() != OBDefine.CHANGE_TYPE_DELETE) // EDIT이나
																															// NONE에
																															// 대해서만
																															// 수정
			{
				if (poolConfig.getChange() != OBDefine.CHANGE_TYPE_NONE) {
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					vsConfig.setPoolChange(OBDefine.CHANGE_TYPE_EDIT);
				}
			}

			vsConfig.setPoolConfig(poolConfig);
		}
		vsConfig.setVsOld(vsOld);
		vsConfig.setVsNew(vsNew);

		return vsConfig;
	}

	private OBDtoAdcConfigVServerF5 DiffPoolMemberF5(int adcIndex, ArrayList<OBDtoAdcPoolMemberF5> oldMemberList,
			ArrayList<OBDtoAdcPoolMemberF5> newMemberList) throws OBException {
		int i, j;
		boolean bMemberMatched = false;
		String nodeIndex = null;

		OBDtoAdcConfigVServerF5 objVs = new OBDtoAdcConfigVServerF5();
		OBDtoAdcConfigPoolF5 objPool = new OBDtoAdcConfigPoolF5();
		ArrayList<OBDtoAdcConfigNodeF5> nodeConfigList = new ArrayList<OBDtoAdcConfigNodeF5>();

		if (oldMemberList == null && newMemberList == null) {
			objVs.setNodeConfigList(null);
			objPool.setMemberConfigList(null);
			objVs.setPoolConfig(objPool);

			return objVs;
		}

		ArrayList<OBDtoAdcPoolMemberF5> oldList;
		ArrayList<OBDtoAdcPoolMemberF5> newList;
		if (oldMemberList == null) {
			oldList = new ArrayList<OBDtoAdcPoolMemberF5>();
		} else {
			oldList = oldMemberList;
		}
		if (newMemberList == null) {
			newList = new ArrayList<OBDtoAdcPoolMemberF5>();
		} else {
			newList = newMemberList;
		}

		ArrayList<OBDtoAdcPoolMemberF5> poolMemberCommonList = new ArrayList<OBDtoAdcPoolMemberF5>();
		ArrayList<OBDtoAdcConfigPoolMemberF5> poolMemberConfigList = new ArrayList<OBDtoAdcConfigPoolMemberF5>();

		OBVServerDB dbManager = new OBVServerDB();

		OBDtoAdcPoolMemberF5 oldMember = null;
		OBDtoAdcPoolMemberF5 newMember = null;
		OBDtoAdcPoolMemberF5 commonMember = null;
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			for (i = 0; i < newList.size(); i++) {
				bMemberMatched = false;
				newMember = newList.get(i);
				for (j = 0; j < oldList.size(); j++) {
					oldMember = oldList.get(j);
					if (newMember.getIpAddress().equals(oldMember.getIpAddress())
							&& newMember.getPort().equals(oldMember.getPort())) {
						bMemberMatched = true;
						break;
					}
				}

				if (bMemberMatched == true) {
					poolMemberCommonList.add(new OBDtoAdcPoolMemberF5(newMember));
					// 같은 멤버면 state가 바뀌었는지?
					if (newMember.getState().equals(oldMember.getState()) == false) {
						OBDtoAdcConfigPoolMemberF5 temp = new OBDtoAdcConfigPoolMemberF5();
						temp.setChange(OBDefine.CHANGE_TYPE_EDIT);
						temp.setStateChange(OBDefine.CHANGE_TYPE_EDIT);
						temp.setMemberNew(new OBDtoAdcPoolMemberF5(newMember));
						temp.setMemberOld(new OBDtoAdcPoolMemberF5(oldMember));
						poolMemberConfigList.add(temp);
						// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "same member, state diff TRUE");
					}
				} else {// 추가된 멤버
						// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "new member");
					OBDtoAdcConfigPoolMemberF5 temp = new OBDtoAdcConfigPoolMemberF5();
					temp.setChange(OBDefine.CHANGE_TYPE_ADD);
					temp.setStateChange(OBDefine.CHANGE_TYPE_NONE);
					temp.setMemberNew(new OBDtoAdcPoolMemberF5(newMember));
					temp.setMemberOld(null);

					poolMemberConfigList.add(temp);
					nodeIndex = dbManager.getNodeIndex(adcIndex, newMember.getIpAddress());

					if (nodeIndex == null) // new node, 삭제node는 사실상 없기 때문에 확인 안 한다.
					{
						OBDtoAdcNodeF5 node = new OBDtoAdcNodeF5();
						OBDtoAdcConfigNodeF5 nodeConfig = new OBDtoAdcConfigNodeF5();

						node.setIpAddress(newMember.getIpAddress());

						nodeConfig.setChange(OBDefine.CHANGE_TYPE_ADD);
						nodeConfig.setIpAddressChange(OBDefine.CHANGE_TYPE_NONE);
						nodeConfig.setNameChange(OBDefine.CHANGE_TYPE_NONE);
						nodeConfig.setNodeNew(node);
						nodeConfig.setNodeOld(null);

						nodeConfigList.add(nodeConfig);
					}
				}
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		try {
			for (i = 0; i < oldList.size(); i++) {
				bMemberMatched = false;
				oldMember = oldList.get(i);
				for (j = 0; j < poolMemberCommonList.size(); j++) {
					commonMember = poolMemberCommonList.get(j);
					if (oldMember.getIpAddress().equals(commonMember.getIpAddress())
							&& oldMember.getPort().equals(commonMember.getPort())) {
						// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "matched !!!! = " +
						// oldMember.getIpAddress());
						bMemberMatched = true;
						break;
					}
				}
				if (bMemberMatched == false) // deleted member
				{
					OBDtoAdcConfigPoolMemberF5 temp = new OBDtoAdcConfigPoolMemberF5();
					temp.setChange(OBDefine.CHANGE_TYPE_DELETE);
					temp.setStateChange(OBDefine.CHANGE_TYPE_NONE);
					temp.setMemberNew(null);
					temp.setMemberOld(new OBDtoAdcPoolMemberF5(oldMember));

					poolMemberConfigList.add(temp);
				}
			}

			poolMemberCommonList = null;// 쓴 것 비우기
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		objPool.setMemberConfigList(poolMemberConfigList);
		objVs.setPoolConfig(objPool);
		objVs.setNodeConfigList(nodeConfigList);
		return objVs;
	}

	public OBDtoAdcConfigChunkAlteon MakeConfigChunkAlteon(OBDtoAdcVServerAlteon virtualServerNew, int userType,
			int changeType, int changeObject, int accountIndex) throws OBException {
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, String.format("start, VS = %s", virtualServerNew));
		OBDtoAdcConfigChunkAlteon configChunk = new OBDtoAdcConfigChunkAlteon(); // 리턴할 값

		OBDtoAdcConfigVServerAlteon vsConfig = null;

		OBDtoAdcVServerAlteon vsOld = new OBDtoAdcVServerAlteon();
		OBDtoAdcVServerAlteon vsNew = new OBDtoAdcVServerAlteon();
		OBVServerDB vsDB = new OBVServerDB();

		int adcIndex = virtualServerNew.getAdcIndex();
		String vsIndex = virtualServerNew.getIndex();

		try {
			if (changeType == OBDefine.CHANGE_TYPE_ADD) {
				vsOld = null;
				vsNew = OBAdcVServerAlteon.CloneVServer(virtualServerNew);
				vsNew.setIndex(OBCommon.makeVSIndexAlteon(adcIndex, vsNew.getAlteonId())); // 신규추가일 때는 vsIndex가 확정되지 않았기
																							// 때문에 미리 구한다.
			} else if (changeType == OBDefine.CHANGE_TYPE_DELETE) {
				vsOld = OBAdcVServerAlteon.CloneVServer(virtualServerNew);
				vsNew = null;
			} else
			// changeType==OBDefine.CHANGE_TYPE_EDIT
			{
				vsOld = vsDB.getVServerInfoAlteon(vsIndex); // 기존 상태확인
				if (vsOld == null) {
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA,
							"Error. VirtualServer doesn't exist.");
				}
				vsNew = OBAdcVServerAlteon.CloneVServer(virtualServerNew);
			}
			vsConfig = DiffVServerAlteon(vsOld, vsNew);

			// 기본데이터 보강
			configChunk.setUserType(userType);
			configChunk.setChangeType(vsConfig.getChange());
			configChunk.setChangeObject(changeObject);
			configChunk.setAccountIndex(accountIndex);
			configChunk.setVsConfig(vsConfig);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "end.");
		return configChunk;
	}

	public OBDtoAdcConfigChunkAlteon MakeConfigChunkBySystemAlteon(OBDtoAdcVServerAlteon vsOld,
			OBDtoAdcVServerAlteon vsNew) throws OBException {
		OBDtoAdcConfigChunkAlteon chunk = new OBDtoAdcConfigChunkAlteon();
		OBDtoAdcConfigVServerAlteon vsConfig = new OBDtoAdcConfigVServerAlteon();
		OBDtoAdcConfigVServerAlteon vsConfigTemp = new OBDtoAdcConfigVServerAlteon();

		// ArrayList<OBDtoAdcConfigVServiceAlteon> serviceConfigList = null;
		// ArrayList<OBDtoAdcConfigPoolAlteon> poolConfigList = null;
		// ArrayList<OBDtoAdcConfigNodeAlteon> nodeConfigList = null;

		int adcIndex = 0;
		// OBVServerDB vDB = new OBVServerDB();

		// old/new virtual server를 복제 해 놓는다.
		OBDtoAdcVServerAlteon vsOldSave = null;
		if (vsOld != null) {
			vsOldSave = OBAdcVServerAlteon.CloneVServer(vsOld);
			adcIndex = vsOld.getAdcIndex();

			if (vsOldSave.getVserviceList() != null && vsOldSave.getVserviceList().size() == 0) {
				vsOldSave.setVserviceList(null);
			}
		}

		OBDtoAdcVServerAlteon vsNewSave = null;
		if (vsNew != null) {
			vsNewSave = OBAdcVServerAlteon.CloneVServer(vsNew);
			adcIndex = vsNew.getAdcIndex();

			if (vsNewSave.getVserviceList() != null && vsNewSave.getVserviceList().size() == 0) {
				vsNewSave.setVserviceList(null);
			}
		}

		// defaults...
		vsConfig.setChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setAlteonIdChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setIfNumChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setNameChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setRouterIndexChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setUseYNChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setvIPChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setVrIndexChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setVrrpYNChange(OBDefine.CHANGE_TYPE_NONE);

		vsConfig.setGolbalNodeChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setGolbalPoolChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setServiceChange(OBDefine.CHANGE_TYPE_NONE);

		vsConfig.setServiceConfigList(null);
		vsConfig.setNodeConfigList(null);
		vsConfig.setPoolConfigList(null);

		if (vsOldSave == null && vsNewSave != null) // virtual server 추가
		{
			vsConfig.setChange(OBDefine.CHANGE_TYPE_ADD);
			vsNewSave.setIndex(OBCommon.makeVSIndexAlteon(adcIndex, vsNewSave.getAlteonId())); // 신규추가일 때는 vsIndex가 확정되지
																								// 않았기 때문에 미리구해준다.
		} else if (vsOldSave != null && vsNewSave == null) // virtual server 삭제
		{
			vsConfig.setChange(OBDefine.CHANGE_TYPE_DELETE);
		} else if (vsOldSave != null && vsNewSave != null) // 무변경 또는 변경
		{
			// alteon ID
			if (vsOldSave.getAlteonId().equals(vsNewSave.getAlteonId()) == false) {
				vsConfig.setAlteonIdChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}
			// IF number
			if (vsOldSave.getIfNum().equals(vsNewSave.getIfNum()) == false) {
				vsConfig.setIfNumChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}
			// name
			if (vsOldSave.getName().equals(vsNewSave.getName()) == false) {
				vsConfig.setNameChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}
			// Router Index
			if (vsOldSave.getRouterIndex().equals(vsNewSave.getRouterIndex()) == false) {
				vsConfig.setRouterIndexChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			// Use YN(enable/disable)
			if (vsOldSave.getUseYN() != null && vsNewSave.getUseYN() != null) // 설정 화면에서는 얘가 null로 들어온다
			{
				if (vsOldSave.getUseYN().equals(vsNewSave.getUseYN()) == false) {
					vsConfig.setUseYNChange(OBDefine.CHANGE_TYPE_EDIT);
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				}
			}

			// Virtual IP
			if (vsOldSave.getvIP().equals(vsNewSave.getvIP()) == false) {
				vsConfig.setvIPChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			// vr Index
			if (vsOldSave.getVrIndex().equals(vsNewSave.getVrIndex()) == false) {
				vsConfig.setVrIndexChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			// vrrp YN
			if (vsOldSave.isVrrpYN() != vsNewSave.isVrrpYN()) {
				vsConfig.setVrrpYNChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			// virtual service 파트 비교
			// pool 파트 비교 작업도 같이 하기 때문에 vsConfigTemp에 바뀐 서비스와 pool 정보를 담아옴 ---
			vsConfigTemp = DiffVServiceAlteon(adcIndex, vsOldSave.getVserviceList(), vsNewSave.getVserviceList());

			// if(vsConfigTemp.getChange()==OBDefine.CHANGE_TYPE_NONE) //바뀐 서비스가 없다.
			if (vsConfigTemp.getServiceConfigList().size() > 0) // 바뀐 서비스가 있다.
			{
				vsConfig.setServiceChange(OBDefine.CHANGE_TYPE_EDIT); // 서비스가 바뀜
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}
		}

		vsConfig.setServiceConfigList(vsConfigTemp.getServiceConfigList());
		vsConfig.setPoolConfigList(vsConfigTemp.getPoolConfigList());
		// vsConfig.setNodeConfigList(nodeConfigList);
		vsConfig.setVsNew(vsNewSave);
		vsConfig.setVsOld(vsOldSave);

		chunk.setAccountIndex(0);
		chunk.setChangeObject(OBDefine.CHANGE_OBJECT_VIRTUALSERVER);
		chunk.setChangeType(vsConfig.getChange());
		chunk.setUserType(OBDefine.CHANGE_BY_SYSTEM);
		chunk.setVsConfig(vsConfig);

		return chunk;
	}

	public OBDtoAdcConfigVServerAlteon DiffVServerAlteon(OBDtoAdcVServerAlteon vsOld, OBDtoAdcVServerAlteon vsNew)
			throws OBException {
		OBDtoAdcConfigVServerAlteon vsConfig = new OBDtoAdcConfigVServerAlteon(); // 리턴할 값
		OBDtoAdcConfigVServerAlteon vsConfigTemp = new OBDtoAdcConfigVServerAlteon(); // vservice와 pool 비교 임시저장

		// defaults......
		vsConfig.setChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setAlteonIdChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setIfNumChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setNameChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setRouterIndexChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setServiceChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setUseYNChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setvIPChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setVrIndexChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setVrrpYNChange(OBDefine.CHANGE_TYPE_NONE);

		vsConfig.setGolbalNodeChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setGolbalPoolChange(OBDefine.CHANGE_TYPE_NONE);

		vsConfig.setVsNew(null);
		vsConfig.setVsOld(null);
		vsConfig.setServiceConfigList(null);
		vsConfig.setPoolConfigList(null);
		vsConfig.setNodeConfigList(null);
		// defaults 끝

		int adcIndex = 0;

		// old/new virtual server를 복제 해 놓는다.
		if (vsOld != null) {
			adcIndex = vsOld.getAdcIndex();
		}
		if (vsNew != null) {
			adcIndex = vsNew.getAdcIndex();
		}

		if (vsOld == null) {
			vsConfig.setChange(OBDefine.CHANGE_TYPE_ADD);
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "- alteon vs add");
			vsNew.setIndex(OBCommon.makeVSIndexAlteon(adcIndex, vsNew.getAlteonId())); // 신규추가일 때는 vsIndex가 확정되지 않았기 때문에
																						// 미리구해준다.
		} else if (vsNew == null) {
			vsConfig.setChange(OBDefine.CHANGE_TYPE_DELETE);
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "- alteon vs delete");
		} else
		// virtual server edit
		{
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "- alteon vs change???");

			if (vsOld.getAlteonId().equals(vsNew.getAlteonId()) == false) {
				vsConfig.setAlteonIdChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			if (vsOld.getIfNum().equals(vsNew.getIfNum()) == false) {
				vsConfig.setIfNumChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			if (vsOld.getName().equals(vsNew.getName()) == false) {
				vsConfig.setNameChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			if (vsOld.getRouterIndex().equals(vsNew.getRouterIndex()) == false) {
				vsConfig.setRouterIndexChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			// if(vsOld.getState().equals(vsNew.getState())==false) //TODO: 얘는 곧 없어질 필드이다.
			// 없어지면 지운다.
			// {
			// // vsConfig.setUseYNChange(OBDefine.CHANGE_TYPE_EDIT);
			// vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			// }

			if (vsOld.getUseYN() == null && vsNew.getUseYN() == null) {
				vsConfig.setUseYNChange(OBDefine.CHANGE_TYPE_NONE);
			} else if (vsOld.getUseYN() == null && vsNew.getUseYN() != null) {
				// vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT); //TODO
				// vsConfig.setUseYNChange(OBDefine.CHANGE_TYPE_EDIT);
			} else if (vsOld.getUseYN() != null && vsNew.getUseYN() == null) {
				// vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				// vsConfig.setUseYNChange(OBDefine.CHANGE_TYPE_EDIT);
			} else {
				if (vsOld.getUseYN().equals(vsNew.getUseYN()) == false) {
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					vsConfig.setUseYNChange(OBDefine.CHANGE_TYPE_EDIT);
				} else {
					vsConfig.setUseYNChange(OBDefine.CHANGE_TYPE_NONE);
				}
			}

			if (vsOld.getvIP().equals(vsNew.getvIP()) == false) {
				vsConfig.setvIPChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			if (vsOld.getVrIndex().equals(vsNew.getVrIndex()) == false) {
				vsConfig.setVrIndexChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			// virtual service 파트 비교
			// pool 파트 비교 작업도 같이 하기 때문에 vsConfigTemp에 바뀐 서비스와 pool 정보를 담아옴 ---
			vsConfigTemp = DiffVServiceAlteon(adcIndex, vsOld.getVserviceList(), vsNew.getVserviceList());

			if (vsConfigTemp.getServiceConfigList().size() > 0) // 바뀐 서비스가 있다.
			{
				vsConfig.setServiceChange(OBDefine.CHANGE_TYPE_EDIT); // 서비스가 바뀜
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
			}
		}

		vsConfig.setServiceConfigList(vsConfigTemp.getServiceConfigList());
		vsConfig.setPoolConfigList(vsConfigTemp.getPoolConfigList());
		// vsConfig.setNodeConfigList(nodeConfigList);
		vsConfig.setVsNew(vsNew);
		vsConfig.setVsOld(vsOld);

		return vsConfig;
	}

	// pool 변경내역도 같이 return하기 때문에 VService가 아니라 Vserver단위 class로 리턴한다.
	private OBDtoAdcConfigVServerAlteon DiffVServiceAlteon(int adcIndex, ArrayList<OBDtoAdcVService> oldVssList,
			ArrayList<OBDtoAdcVService> newVssList) throws OBException {
		int i, j;
		boolean isCommon = false;
		OBDtoAdcVService vssNew = null;
		OBDtoAdcVService vssOld = null;
		ArrayList<Integer> commonPortList = new ArrayList<Integer>();

		ArrayList<OBDtoAdcConfigVServiceAlteon> serviceChangeList = new ArrayList<OBDtoAdcConfigVServiceAlteon>(); // 리턴
		ArrayList<OBDtoAdcConfigPoolAlteon> poolChangeList = new ArrayList<OBDtoAdcConfigPoolAlteon>(); // 리턴

		OBDtoAdcConfigVServerAlteon vsConfig = new OBDtoAdcConfigVServerAlteon();

		if (newVssList == null) {
			if (oldVssList != null) // vservice 전부 삭제됨
			{
				for (i = 0; i < oldVssList.size(); i++) {
					OBDtoAdcConfigVServiceAlteon tempVss = new OBDtoAdcConfigVServiceAlteon();
					tempVss.setChange(OBDefine.CHANGE_TYPE_DELETE);
					tempVss.setPoolChange(OBDefine.CHANGE_TYPE_NONE);
					tempVss.setRealPortChange(OBDefine.CHANGE_TYPE_NONE);
					tempVss.setServicePortChange(OBDefine.CHANGE_TYPE_NONE);

					tempVss.setServiceNew(null);
					tempVss.setServiceOld(oldVssList.get(i));
					tempVss.setPoolConfig(null);
					serviceChangeList.add(tempVss);
				}
			}
		} else {
			if (oldVssList == null) // vservice 전부 추가
			{
				for (i = 0; i < newVssList.size(); i++) {
					OBDtoAdcConfigVServiceAlteon tempVss = new OBDtoAdcConfigVServiceAlteon();
					tempVss.setChange(OBDefine.CHANGE_TYPE_ADD);
					tempVss.setPoolChange(OBDefine.CHANGE_TYPE_NONE);
					tempVss.setRealPortChange(OBDefine.CHANGE_TYPE_NONE);
					tempVss.setServicePortChange(OBDefine.CHANGE_TYPE_NONE);

					tempVss.setServiceNew(newVssList.get(i));
					tempVss.setServiceOld(null);

					if (newVssList.get(i).getPool() != null) // 추가된 virtual service의 pool 처리
					{
						OBDtoAdcConfigPoolAlteon tempPool = DiffPoolAlteon(adcIndex, null, newVssList.get(i).getPool());
						if (tempPool.getChange() != OBDefine.CHANGE_TYPE_NONE) // 바뀌었으면 리턴할 pool로 추가한다.
						{
							poolChangeList.add(tempPool);
						}
						tempVss.setPoolConfig(tempPool);
					} else {
						tempVss.setPoolConfig(null);
					}
					serviceChangeList.add(tempVss);
				}
			} else
			// 변경확인
			{
				for (i = 0; i < newVssList.size(); i++) {
					vssNew = newVssList.get(i);
					isCommon = false;
					for (j = 0; j < oldVssList.size(); j++) {
						vssOld = oldVssList.get(j);
						if (vssNew.getServicePort().equals(vssOld.getServicePort()) == true) {
							isCommon = true;
							break;
						}
					}

					if (isCommon == true) // old에 같은 virtual service가 있다. 바뀐건지 확인
					{
						OBDtoAdcConfigVServiceAlteon tempVss = new OBDtoAdcConfigVServiceAlteon();
						tempVss.setChange(OBDefine.CHANGE_TYPE_NONE);
						tempVss.setPoolChange(OBDefine.CHANGE_TYPE_NONE);
						tempVss.setRealPortChange(OBDefine.CHANGE_TYPE_NONE);
						tempVss.setServicePortChange(OBDefine.CHANGE_TYPE_NONE);
						tempVss.setServiceNew(vssNew);
						tempVss.setServiceOld(vssOld);
						tempVss.setPoolConfig(null);
						commonPortList.add(vssNew.getServicePort()); // 겹친 virtual port 목록을 만든다. 삭제 체크할 때 쓴다.

						if (vssNew.getRealPort() == null) {
							if (vssOld.getRealPort() != null) {
								tempVss.setRealPortChange(OBDefine.CHANGE_TYPE_EDIT);
								tempVss.setChange(OBDefine.CHANGE_TYPE_EDIT);
							}
						} else {
							if (vssOld.getRealPort() == null) {
								tempVss.setRealPortChange(OBDefine.CHANGE_TYPE_EDIT);
								tempVss.setChange(OBDefine.CHANGE_TYPE_EDIT);
							} else {
								if (vssNew.getRealPort().equals(vssOld.getRealPort()) == false) {
									tempVss.setRealPortChange(OBDefine.CHANGE_TYPE_EDIT);
									tempVss.setChange(OBDefine.CHANGE_TYPE_EDIT);
								}
							}
						}

						if (vssNew.getServicePort() == null) {
							if (vssOld.getServicePort() != null) {
								tempVss.setServicePortChange(OBDefine.CHANGE_TYPE_EDIT);
								tempVss.setChange(OBDefine.CHANGE_TYPE_EDIT);
							}
						} else {
							if (vssOld.getServicePort() == null) {
								tempVss.setServicePortChange(OBDefine.CHANGE_TYPE_EDIT);
								tempVss.setChange(OBDefine.CHANGE_TYPE_EDIT);
							} else {
								if (vssNew.getServicePort().equals(vssOld.getServicePort()) == false) {
									tempVss.setServicePortChange(OBDefine.CHANGE_TYPE_EDIT);
									tempVss.setChange(OBDefine.CHANGE_TYPE_EDIT);
								}
							}
						}

						// 변경된 virtual service의 pool 처리
						if (vssNew.getPool() == null) {
							if (vssOld.getPool() != null) {
								tempVss.setPoolChange(OBDefine.CHANGE_TYPE_DELETE);
								tempVss.setChange(OBDefine.CHANGE_TYPE_EDIT);
								tempVss.setPoolConfig(null);
							}
						} else {
							// pool이 있는 상황이면 그 pool 자체가 바뀌었는지도 확인하고 간다.
							OBDtoAdcConfigPoolAlteon tempPool = DiffPoolAlteon(adcIndex, vssOld.getPool(),
									vssNew.getPool());
							tempVss.setPoolConfig(tempPool);
							if (tempPool.getChange() != OBDefine.CHANGE_TYPE_NONE) // 바뀌었으면 리턴할 pool로 추가한다.
							{
								poolChangeList.add(tempPool);
							}
							if (vssOld.getPool() == null) {
								tempVss.setPoolChange(OBDefine.CHANGE_TYPE_ADD);
								tempVss.setChange(OBDefine.CHANGE_TYPE_EDIT);
							} else
							// pool diff
							{
								if (vssNew.getPool().getIndex().equals(vssOld.getPool().getIndex()) == false) {
									tempVss.setPoolChange(OBDefine.CHANGE_TYPE_EDIT);
									tempVss.setChange(OBDefine.CHANGE_TYPE_EDIT);
								} else
								// assigned pool은 바뀌지 않았는데, 속성이 달라졌나 본다
								{
									if (tempPool.getChange() != OBDefine.CHANGE_TYPE_NONE) {
										// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "same pool but differences");
										tempVss.setPoolChange(OBDefine.CHANGE_TYPE_EDIT);
										tempVss.setChange(OBDefine.CHANGE_TYPE_EDIT);
									}
								}
							}
						}
						if (tempVss.getChange() != OBDefine.CHANGE_TYPE_NONE) // 바뀐 서비스만 추가한다.
						{
							serviceChangeList.add(tempVss);
						}
					} else
					// old에 없다. 새로 생긴 virtual server다
					{
						// pool이 있는 상황이면 그 pool 자체가 바뀌었는지도 확인하고 간다.
						OBDtoAdcConfigPoolAlteon tempPool = DiffPoolAlteon(adcIndex, null, vssNew.getPool());
						if (tempPool.getChange() != OBDefine.CHANGE_TYPE_NONE) // 바뀌었으면 리턴할 pool로 추가한다.
						{
							poolChangeList.add(tempPool);
						}

						OBDtoAdcConfigVServiceAlteon tempVss = new OBDtoAdcConfigVServiceAlteon();
						tempVss.setChange(OBDefine.CHANGE_TYPE_ADD);
						tempVss.setPoolChange(OBDefine.CHANGE_TYPE_NONE);
						tempVss.setRealPortChange(OBDefine.CHANGE_TYPE_NONE);
						tempVss.setServicePortChange(OBDefine.CHANGE_TYPE_NONE);

						tempVss.setServiceNew(vssNew);
						tempVss.setServiceOld(null);
						tempVss.setPoolConfig(null);
						serviceChangeList.add(tempVss);
					}
				}

				// 삭제된 virtual service 처리
				for (i = 0; i < oldVssList.size(); i++) {
					isCommon = false;
					for (Integer port : commonPortList) {
						if (oldVssList.get(i).getServicePort().equals(port) == true) {
							isCommon = true;
							break;
						}
					}
					if (isCommon == false) // 삭제된 virtual service
					{
						OBDtoAdcConfigVServiceAlteon tempVss = new OBDtoAdcConfigVServiceAlteon();
						tempVss.setChange(OBDefine.CHANGE_TYPE_DELETE);
						tempVss.setPoolChange(OBDefine.CHANGE_TYPE_NONE);
						tempVss.setRealPortChange(OBDefine.CHANGE_TYPE_NONE);
						tempVss.setServicePortChange(OBDefine.CHANGE_TYPE_NONE);

						tempVss.setPoolConfig(null);
						tempVss.setServiceNew(null);
						tempVss.setServiceOld(oldVssList.get(i));

						serviceChangeList.add(tempVss);
					}
				}
			}
		}

		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "changed service count = " +
		// serviceChangeList.size());
		if (serviceChangeList.size() == 0) // 바뀐 서비스가 없다.
		{
			// serviceChangeList = null;
		}
		if (poolChangeList.size() == 0) // 바뀐 pool이 없다.
		{
			// poolChangeList = null;
		}
		vsConfig.setServiceConfigList(serviceChangeList);
		vsConfig.setPoolConfigList(poolChangeList);

		return vsConfig;
	}

	private OBDtoAdcConfigPoolAlteon DiffPoolAlteon(int adcIndex, OBDtoAdcPoolAlteon oldPool,
			OBDtoAdcPoolAlteon newPool) throws OBException {
		OBDtoAdcConfigPoolAlteon poolConfig = new OBDtoAdcConfigPoolAlteon(); // 리턴

		poolConfig.setChange(OBDefine.CHANGE_TYPE_NONE);
		poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_NONE);
		poolConfig.setLbMethodChange(OBDefine.CHANGE_TYPE_NONE);
		poolConfig.setMemberChange(OBDefine.CHANGE_TYPE_NONE);
		poolConfig.setNameChange(OBDefine.CHANGE_TYPE_NONE);
		poolConfig.setPoolMemberAddList(null);
		poolConfig.setPoolMemberDelList(null);
		poolConfig.setPoolNew(null);
		poolConfig.setPoolOld(null);

		if (oldPool == null) {
			if (newPool == null) {
				poolConfig.setChange(OBDefine.CHANGE_TYPE_NONE);
			} else {
				poolConfig.setChange(OBDefine.CHANGE_TYPE_ADD);
			}
		} else {
			if (newPool == null) {
				poolConfig.setChange(OBDefine.CHANGE_TYPE_DELETE); // pool이 vservice설정에서 빠진 게 아니라 pool 자체가 없어진 것을 의미한다.
			} else
			// old,new 둘다 null이 아니므로 달라진 것을 비교한다.
			{
				if (oldPool.getName() == null && newPool.getName() != null) {
					poolConfig.setNameChange(OBDefine.CHANGE_TYPE_EDIT);
					poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				} else if (oldPool.getName() != null && newPool.getName() == null) {
					poolConfig.setNameChange(OBDefine.CHANGE_TYPE_EDIT);
					poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				} else if (oldPool.getName() != null && newPool.getName() != null) {
					if (oldPool.getName().equals(newPool.getName()) == false) {
						poolConfig.setNameChange(OBDefine.CHANGE_TYPE_EDIT);
						poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					}
				}
				// 옛날 healthcheck
				if (oldPool.getHealthCheck() == null && newPool.getHealthCheck() != null) {
					poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_EDIT);
					poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				} else if (oldPool.getHealthCheck() != null && newPool.getHealthCheck() == null) {
					poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_EDIT);
					poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				} else if (oldPool.getHealthCheck() != null && newPool.getHealthCheck() != null) {
					if (oldPool.getHealthCheck().equals(newPool.getHealthCheck()) == false) {
						poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_EDIT);
						poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					}
				}
				// new healthcheck
				if (isHealthcheckAlteonChanged(oldPool.getHealthCheckV2(), newPool.getHealthCheckV2()) == true) {
					poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_EDIT);
					poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				}

				if (oldPool.getLbMethod().equals(newPool.getLbMethod()) == false) {
					poolConfig.setLbMethodChange(OBDefine.CHANGE_TYPE_EDIT);
					poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				}

				// if(IsPoolMemberSameAlteon(oldPool.getMemberList(),
				// newPool.getMemberList())==false)
				// pool 멤버 diff
				OBDtoAdcConfigVServerAlteon tempVsConfig = DiffPoolMemberAlteon(adcIndex, oldPool.getMemberList(),
						newPool.getMemberList());
				if (tempVsConfig.getPoolConfigList().get(0).getMemberConfigList().size() > 0) // 바뀐 멤버가 있다.
				{
					poolConfig.setMemberChange(OBDefine.CHANGE_TYPE_EDIT);
					poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				}
			}
		}

		poolConfig.setPoolNew(newPool);
		poolConfig.setPoolOld(oldPool);

		return poolConfig;
	}

	private OBDtoAdcConfigVServerAlteon DiffPoolMemberAlteon(int adcIndex,
			ArrayList<OBDtoAdcPoolMemberAlteon> oldMemberList, ArrayList<OBDtoAdcPoolMemberAlteon> newMemberList)
			throws OBException {
		int i, j;
		boolean bMemberMatched = false;
		String nodeIndex = null;

		OBDtoAdcConfigVServerAlteon confVs = new OBDtoAdcConfigVServerAlteon();
		OBDtoAdcConfigPoolAlteon confPool = new OBDtoAdcConfigPoolAlteon();
		ArrayList<OBDtoAdcConfigPoolAlteon> poolConfigList = new ArrayList<OBDtoAdcConfigPoolAlteon>();
		ArrayList<OBDtoAdcConfigNodeAlteon> nodeConfigList = new ArrayList<OBDtoAdcConfigNodeAlteon>();

		if (oldMemberList == null && newMemberList == null) {
			confPool.setPoolMemberAddList(null); // TODO : 나중에 setMemberConfigList(null);로 바꿔야 한다. ALTEON 멤버리스트
													// structure조정 필요
			poolConfigList.add(confPool);
			confVs.setPoolConfigList(poolConfigList);
			confVs.setNodeConfigList(null);

			return confVs;
		}

		ArrayList<OBDtoAdcPoolMemberAlteon> oldList;
		ArrayList<OBDtoAdcPoolMemberAlteon> newList;
		if (oldMemberList == null) {
			oldList = new ArrayList<OBDtoAdcPoolMemberAlteon>();
		} else {
			oldList = oldMemberList;
		}
		if (newMemberList == null) {
			newList = new ArrayList<OBDtoAdcPoolMemberAlteon>();
		} else {
			newList = newMemberList;
		}

		ArrayList<OBDtoAdcPoolMemberAlteon> poolMemberCommonList = new ArrayList<OBDtoAdcPoolMemberAlteon>();
		ArrayList<OBDtoAdcConfigPoolMemberAlteon> poolMemberConfigList = new ArrayList<OBDtoAdcConfigPoolMemberAlteon>();

		OBDatabase db = new OBDatabase();
		OBVServerDB dbManager = new OBVServerDB();
		try {
			db.openDB();

			OBDtoAdcPoolMemberAlteon oldMember = null;
			OBDtoAdcPoolMemberAlteon newMember = null;
			OBDtoAdcPoolMemberAlteon commonMember = null;
			for (i = 0; i < newList.size(); i++) {
				bMemberMatched = false;
				newMember = newList.get(i);
				for (j = 0; j < oldList.size(); j++) {
					oldMember = oldList.get(j);
					if (newMember.getAlteonNodeID().equals(oldMember.getAlteonNodeID())) {
						bMemberMatched = true;
						break;
					}
				}

				if (bMemberMatched == true) // old-new 공통으로 있는 멤버다.
				{
					poolMemberCommonList.add(new OBDtoAdcPoolMemberAlteon(newMember));
					// 같은 멤버면 state가 바뀌었는지?
					if (newMember.getState().equals(oldMember.getState()) == false) {
						OBDtoAdcConfigPoolMemberAlteon temp = new OBDtoAdcConfigPoolMemberAlteon();
						temp.setChange(OBDefine.CHANGE_TYPE_EDIT);
						temp.setStateChange(OBDefine.CHANGE_TYPE_EDIT);
						temp.setMemberNew(new OBDtoAdcPoolMemberAlteon(newMember));
						temp.setMemberOld(new OBDtoAdcPoolMemberAlteon(oldMember));
						poolMemberConfigList.add(temp);
						// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "same member, state diff TRUE");
					}
				} else {// 추가된 멤버
						// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "new member");
					OBDtoAdcConfigPoolMemberAlteon temp = new OBDtoAdcConfigPoolMemberAlteon();
					temp.setChange(OBDefine.CHANGE_TYPE_ADD);
					temp.setStateChange(OBDefine.CHANGE_TYPE_NONE);
					temp.setMemberNew(new OBDtoAdcPoolMemberAlteon(newMember));
					temp.setMemberOld(null);

					poolMemberConfigList.add(temp);

					try {
						nodeIndex = dbManager.getNodeIndex(adcIndex, newMember.getIpAddress());
					} catch (OBException e) {
						db.closeDB();
						throw new OBException(e.getMessage());
					}

					if (nodeIndex == null) // new node, 삭제node는 사실상 없기 때문에 확인 안 한다.
					{
						OBDtoAdcNodeAlteon node = new OBDtoAdcNodeAlteon();
						OBDtoAdcConfigNodeAlteon nodeConfig = new OBDtoAdcConfigNodeAlteon();

						node.setIpAddress(newMember.getIpAddress());

						nodeConfig.setChange(OBDefine.CHANGE_TYPE_ADD);
						nodeConfig.setIpAddressChange(OBDefine.CHANGE_TYPE_NONE);
						nodeConfig.setNameChange(OBDefine.CHANGE_TYPE_NONE);
						nodeConfig.setNodeNew(node);
						nodeConfig.setNodeOld(null);

						nodeConfigList.add(nodeConfig);
					}
				}
			}

			for (i = 0; i < oldList.size(); i++) {
				bMemberMatched = false;
				oldMember = oldList.get(i);
				for (j = 0; j < poolMemberCommonList.size(); j++) {
					commonMember = poolMemberCommonList.get(j);
					if (oldMember.getAlteonNodeID().equals(commonMember.getAlteonNodeID())) {
						// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "matched !!!! = " +
						// oldMember.getIpAddress());
						bMemberMatched = true;
						break;
					}
				}
				if (bMemberMatched == false) // deleted member
				{
					OBDtoAdcConfigPoolMemberAlteon temp = new OBDtoAdcConfigPoolMemberAlteon();
					temp.setChange(OBDefine.CHANGE_TYPE_DELETE);
					temp.setStateChange(OBDefine.CHANGE_TYPE_NONE);
					temp.setMemberNew(null);
					temp.setMemberOld(new OBDtoAdcPoolMemberAlteon(oldMember));

					poolMemberConfigList.add(temp);
				}
			}

			poolMemberCommonList = null;// 쓴 것 비우기
			if (poolMemberConfigList.size() == 0) {
				// poolMemberConfigList=null;
			}
			confPool.setMemberConfigList(poolMemberConfigList);
			poolConfigList.add(confPool);
			confVs.setPoolConfigList(poolConfigList);
			confVs.setNodeConfigList(nodeConfigList);
			return confVs;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	// 삭제하기 전 만든 old vserver list를 받아서 작업한다.
	public void SaveHistoryAlteon(OBDtoAdcConfigMixAlteon configOld, int adcIndex) throws OBException {
		OBVServerDB vsDB = new OBVServerDB();
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		String oldIndexStr = "''"; // where-in에 empty 방지
		ArrayList<OBDtoAdcVServerAlteon> vsOldList;
		ArrayList<OBDtoAdcConfigChunkAlteon> chunkList = new ArrayList<OBDtoAdcConfigChunkAlteon>();
		OBDtoAdcConfigChunkAlteon chunkTemp = null;
		ArrayList<String> vsIndexCommonList = new ArrayList<String>();
		try {
			db.openDB();

			vsOldList = configOld.getVsList();
			// ArrayList<OBDtoAdcPoolAlteon> poolOldList = configOld.getPoolList();

			int i;
			for (i = 0; i < vsOldList.size(); i++) {
				if (vsOldList.get(i) != null) {
					oldIndexStr += ", " + OBParser.sqlString(vsOldList.get(i).getIndex());
				}
			}

			sqlText = String.format("SELECT INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d AND INDEX IN (%s)",
					adcIndex, oldIndexStr);// where-in:empty string 불가, null 불가, OK

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				vsIndexCommonList.add(db.getString(rs, "INDEX"));
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}

		boolean isCommon = false;
		// old virtual server 중 non-common을 골라내 deleted로 처리한다.
		for (OBDtoAdcVServerAlteon vsOld : vsOldList) {
			isCommon = false;
			for (String vsIndex : vsIndexCommonList) {
				if (vsOld.getIndex().equals(vsIndex) == true) {
					isCommon = true;
					break;
				}
			}
			if (isCommon == false) // deleted virtual servers
			{
				chunkTemp = MakeConfigChunkBySystemAlteon(vsOld, null);
				if (chunkTemp.getChangeType() != OBDefine.CHANGE_TYPE_NONE) {
					chunkList.add(chunkTemp);
				}
			}
		}

		// new와 changed virtual server를 기록한다.
		ArrayList<OBDtoAdcVServerAlteon> vsNewList = vsDB.getVServerListAllAlteon(adcIndex); // 새 virtual server를 쭉 구한다,
																								// old와 비교할 것

		for (OBDtoAdcVServerAlteon vsNew : vsNewList) {
			isCommon = false;
			for (String vsIndex : vsIndexCommonList) {
				if (vsNew.getIndex().equals(vsIndex) == true) {
					isCommon = true;
					break;
				}
			}
			if (isCommon == true) // 공통된 virtual server면 바뀌었는지 체크한다.
			{
				for (OBDtoAdcVServerAlteon vsOld : vsOldList) {
					if (vsNew.getIndex().equals(vsOld.getIndex()) == true) {
						chunkTemp = MakeConfigChunkBySystemAlteon(vsOld, vsNew);
						if (chunkTemp.getChangeType() != OBDefine.CHANGE_TYPE_NONE) {
							chunkList.add(chunkTemp);
						}
						break;
					}
				}
			} else
			// 새로 추가된 virtual server로 처리한다.
			{
				chunkTemp = MakeConfigChunkBySystemAlteon(null, vsNew);
				if (chunkTemp.getChangeType() != OBDefine.CHANGE_TYPE_NONE) {
					chunkList.add(chunkTemp);
				}
			}
		}

		for (OBDtoAdcConfigChunkAlteon chunk : chunkList) {
			writeConfigHistoryAlteon(chunk);
		}
	}

	// 삭제하기 전 만든 old vserver list를 받아서 작업한다.
	public void SaveHistoryF5(OBDtoAdcConfigMixF5 configOld, int adcIndex) throws OBException {
		// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("configOld = %s /
		// adcIndex = %s", configOld, adcIndex));
		OBVServerDB vsDB = new OBVServerDB();
		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoAdcVServerF5> vsOldList;
		ArrayList<OBDtoAdcPoolF5> poolOldList;

		// new와 changed virtual server를 기록한다.
		ArrayList<OBDtoAdcVServerF5> vsNewList;
		String sqlText = "";
		try {
			db.openDB();

			vsOldList = configOld.getVsList();
			poolOldList = configOld.getPoolList();
			vsNewList = vsDB.getVServerListAllF5(adcIndex); // 새 virtual server를 쭉 구한다, old와 비교할 것

			ArrayList<OBDtoAdcConfigChunkF5> chunkList = new ArrayList<OBDtoAdcConfigChunkF5>();
			OBDtoAdcConfigChunkF5 chunkTemp = null;

			ArrayList<String> vsIndexCommonList = new ArrayList<String>();
			String oldIndexStr = "''";
			int i, j;

			for (i = 0; i < vsOldList.size(); i++) {
				if (vsOldList.get(i) != null) {
					oldIndexStr += ", " + OBParser.sqlString(vsOldList.get(i).getIndex());
				}
			}

			sqlText = String.format("SELECT INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d AND INDEX IN (%s)",
					adcIndex, oldIndexStr);// where-in:empty string 불가, null 불가, OK
			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				vsIndexCommonList.add(db.getString(rs, "INDEX"));
			}

			OBDtoAdcVServerF5 vsOld = null;
			OBDtoAdcVServerF5 vsNew = null;

			boolean isCommon = false;
			// old virtual server 중 non-common을 골라내 deleted로 처리한다.
			for (i = 0; i < vsOldList.size(); i++) {
				vsOld = vsOldList.get(i);
				isCommon = false;
				for (String vsIndex : vsIndexCommonList) {
					if (vsOld.getIndex().equals(vsIndex) == true) {
						isCommon = true;
						break;
					}
				}
				if (isCommon == false) // deleted virtual servers
				{
					chunkTemp = MakeConfigChunkBySystemF5(vsOld, null, null);
					if (chunkTemp.getChangeType() != OBDefine.CHANGE_TYPE_NONE) {
						chunkList.add(chunkTemp);
						// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "deleted vs = " + vsOld.getName());
					}
				}
			}
			for (i = 0; i < vsNewList.size(); i++) {
				vsNew = vsNewList.get(i);

				isCommon = false;
				for (String vsIndex : vsIndexCommonList) {
					if (vsNew.getIndex().equals(vsIndex) == true) {
						isCommon = true;
						break;
					}
				}
				if (isCommon == true) // 공통된 virtual server면 바뀌었는지 체크한다.
				{
					for (j = 0; j < vsOldList.size(); j++) {
						vsOld = vsOldList.get(j);
						if (vsNew.getIndex().equals(vsOld.getIndex()) == true) {
							OBDtoAdcPoolF5 poolOrg = null;// 새 vs pool의 원래 상태를 찾아온다. 일치하는 pool이 없으면 null이다.
							if (vsNew.getPool() != null) {
								for (OBDtoAdcPoolF5 pool : poolOldList) {
									if (vsNew.getPool().getIndex().equals(pool.getIndex())) {
										poolOrg = CommonF5.clonePool(pool);
										break;
									}
								}
							}
							chunkTemp = MakeConfigChunkBySystemF5(vsOld, vsNew, poolOrg);
							if (chunkTemp.getChangeType() != OBDefine.CHANGE_TYPE_NONE) {
								chunkList.add(chunkTemp);
								// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "changed vs = " + vsOld.getName());
							}
							break;
						}
					}
				} else
				// 새로 추가된 virtual server로 처리한다.
				{
					OBDtoAdcPoolF5 poolOrg = null;// 새 vs pool의 원래 상태를 찾아온다. 일치하는 pool이 없으면 null이다.
					if (vsNew.getPool() != null) {
						for (OBDtoAdcPoolF5 pool : poolOldList) {
							if (vsNew.getPool().getIndex().equals(pool.getIndex())) {
								poolOrg = CommonF5.clonePool(pool);
								break;
							}
						}
					}

					chunkTemp = MakeConfigChunkBySystemF5(null, vsNew, poolOrg);
					if (chunkTemp.getChangeType() != OBDefine.CHANGE_TYPE_NONE) {
						chunkList.add(chunkTemp);
						// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "added vs = " + vsNew.getName());
					}
				}
			}

			for (OBDtoAdcConfigChunkF5 chunk : chunkList) {
				writeConfigHistoryF5(chunk);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	// 삭제하기 전 만든 old vserver list를 받아서 작업한다.
	public void SaveHistoryPAS(OBDtoAdcConfigMixPAS configOld, int adcIndex) throws OBException {
		OBVServerDB vsDB = new OBVServerDB();
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoAdcVServerPAS> vsOldList;

		// new와 changed virtual server를 기록한다.
		ArrayList<OBDtoAdcVServerPAS> vsNewList;
		String sqlText = "";
		try {
			db.openDB();

			vsOldList = configOld.getVsList();
			vsNewList = vsDB.getVServerListAllPAS(adcIndex); // 새 virtual server를 쭉 구한다, old와 비교할 것

			ArrayList<OBDtoAdcConfigChunkPAS> chunkList = new ArrayList<OBDtoAdcConfigChunkPAS>();
			OBDtoAdcConfigChunkPAS chunkTemp = null;

			ArrayList<String> vsIndexCommonList = new ArrayList<String>();
			String oldIndexStr = "''";
			int i, j;

			for (i = 0; i < vsOldList.size(); i++) {
				if (vsOldList.get(i) != null) {
					oldIndexStr += ", " + OBParser.sqlString(vsOldList.get(i).getDbIndex());
				}
			}

			sqlText = String.format("SELECT INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d AND INDEX IN (%s)",
					adcIndex, oldIndexStr);// where-in:empty string 불가, null 불가, OK

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				vsIndexCommonList.add(db.getString(rs, "INDEX"));
			}

			OBDtoAdcVServerPAS vsOld = null;
			OBDtoAdcVServerPAS vsNew = null;

			boolean isCommon = false;
			// old virtual server 중 non-common을 골라내 deleted로 처리한다.
			for (i = 0; i < vsOldList.size(); i++) {
				vsOld = vsOldList.get(i);
				isCommon = false;
				for (String vsIndex : vsIndexCommonList) {
					if (vsOld.getDbIndex().equals(vsIndex) == true) {
						isCommon = true;
						break;
					}
				}
				if (isCommon == false) // deleted virtual servers
				{
					chunkTemp = MakeConfigChunkBySystemPAS(vsOld, null);
					if (chunkTemp.getChangeType() != OBDefine.CHANGE_TYPE_NONE) {
						chunkList.add(chunkTemp);
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "deleted vs = " + vsOld.getName());
					}
				}
			}

			for (i = 0; i < vsNewList.size(); i++) {
				vsNew = vsNewList.get(i);

				isCommon = false;
				for (String vsIndex : vsIndexCommonList) {
					if (vsNew.getDbIndex().equals(vsIndex) == true) {
						isCommon = true;
						break;
					}
				}
				if (isCommon == true) // 공통된 virtual server면 바뀌었는지 체크한다.
				{
					for (j = 0; j < vsOldList.size(); j++) {
						vsOld = vsOldList.get(j);
						if (vsNew.getDbIndex().equals(vsOld.getDbIndex()) == true) {
							chunkTemp = MakeConfigChunkBySystemPAS(vsOld, vsNew);
							if (chunkTemp.getChangeType() != OBDefine.CHANGE_TYPE_NONE) {
								chunkList.add(chunkTemp);
								OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "changed vs = " + vsOld.getName());
							}
							break;
						}
					}
				} else
				// 새로 추가된 virtual server로 처리한다.
				{
					chunkTemp = MakeConfigChunkBySystemPAS(null, vsNew);
					if (chunkTemp.getChangeType() != OBDefine.CHANGE_TYPE_NONE) {
						chunkList.add(chunkTemp);
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "added vs = " + vsNew.getName());
					}
				}
			}

			for (OBDtoAdcConfigChunkPAS chunk : chunkList) {
				writeConfigHistoryPAS(chunk);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	public void SaveHistoryPASK(OBDtoAdcConfigMixPASK configOld, int adcIndex) throws OBException {
		OBVServerDB vsDB = new OBVServerDB();
		OBDatabase db = new OBDatabase();

		ArrayList<OBDtoAdcVServerPASK> vsOldList;

		// new와 changed virtual server를 기록한다.
		ArrayList<OBDtoAdcVServerPASK> vsNewList;
		String sqlText = "";
		try {
			db.openDB();

			vsOldList = configOld.getVsList();
			vsNewList = vsDB.getVServerListAllPASK(adcIndex); // 새 virtual server를 쭉 구한다, old와 비교할 것

			ArrayList<OBDtoAdcConfigChunkPASK> chunkList = new ArrayList<OBDtoAdcConfigChunkPASK>();
			OBDtoAdcConfigChunkPASK chunkTemp = null;

			ArrayList<String> vsIndexCommonList = new ArrayList<String>();
			String oldIndexStr = "''";
			int i, j;

			for (i = 0; i < vsOldList.size(); i++) {
				if (vsOldList.get(i) != null) {
					oldIndexStr += ", " + OBParser.sqlString(vsOldList.get(i).getDbIndex());
				}
			}

			sqlText = String.format("SELECT INDEX FROM TMP_SLB_VSERVER WHERE ADC_INDEX = %d AND INDEX IN (%s)",
					adcIndex, oldIndexStr);// where-in:empty string 불가, null 불가, OK

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				vsIndexCommonList.add(db.getString(rs, "INDEX"));
			}

			OBDtoAdcVServerPASK vsOld = null;
			OBDtoAdcVServerPASK vsNew = null;

			boolean isCommon = false;
			// old virtual server 중 non-common을 골라내 deleted로 처리한다.
			for (i = 0; i < vsOldList.size(); i++) {
				vsOld = vsOldList.get(i);
				isCommon = false;
				for (String vsIndex : vsIndexCommonList) {
					if (vsOld.getDbIndex().equals(vsIndex) == true) {
						isCommon = true;
						break;
					}
				}
				if (isCommon == false) // deleted virtual servers
				{
					chunkTemp = MakeConfigChunkBySystemPASK(vsOld, null);
					if (chunkTemp.getChangeType() != OBDefine.CHANGE_TYPE_NONE) {
						chunkList.add(chunkTemp);
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "deleted vs = " + vsOld.getName());
					}
				}
			}

			for (i = 0; i < vsNewList.size(); i++) {
				vsNew = vsNewList.get(i);

				isCommon = false;
				for (String vsIndex : vsIndexCommonList) {
					if (vsNew.getDbIndex().equals(vsIndex) == true) {
						isCommon = true;
						break;
					}
				}
				if (isCommon == true) // 공통된 virtual server면 바뀌었는지 체크한다.
				{
					for (j = 0; j < vsOldList.size(); j++) {
						vsOld = vsOldList.get(j);
						if (vsNew.getDbIndex().equals(vsOld.getDbIndex()) == true) {
							chunkTemp = MakeConfigChunkBySystemPASK(vsOld, vsNew);
							if (chunkTemp.getChangeType() != OBDefine.CHANGE_TYPE_NONE) {
								chunkList.add(chunkTemp);
								OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "changed vs = " + vsOld.getName());
							}
							break;
						}
					}
				} else
				// 새로 추가된 virtual server로 처리한다.
				{
					chunkTemp = MakeConfigChunkBySystemPASK(null, vsNew);
					if (chunkTemp.getChangeType() != OBDefine.CHANGE_TYPE_NONE) {
						chunkList.add(chunkTemp);
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "added vs = " + vsNew.getName());
					}
				}
			}

			for (OBDtoAdcConfigChunkPASK chunk : chunkList) {
				writeConfigHistoryPASK(chunk);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	// ykk___aaaa
	public OBDtoAdcConfigChunkPAS MakeConfigChunkPAS(OBDtoAdcVServerPAS virtualServerOld,
			OBDtoAdcVServerPAS virtualServerNew, int userType, int changeType, int changeObject, int accountIndex)
			throws OBException {
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, String.format("start, VS = %s", virtualServerNew));
		OBDtoAdcConfigChunkPAS configChunk = new OBDtoAdcConfigChunkPAS(); // 리턴할 값

		OBDtoAdcConfigVServerPAS vsConfig = null;

		OBDtoAdcVServerPAS vsOld = new OBDtoAdcVServerPAS();
		OBDtoAdcVServerPAS vsNew = new OBDtoAdcVServerPAS();
		// OBDtoAdcPoolPAS poolOld = new OBDtoAdcPoolPAS();

		if (changeType == OBDefine.CHANGE_TYPE_ADD) {
			vsOld = null;
			vsNew = OBAdcVServerPAS.cloneVServer(virtualServerNew);
			// 신규추가일 때는 vsIndex가 확정되지 않았기 때문에 vsIndex를 인위로 구성한다.
			vsNew.setDbIndex(OBCommon.makeVSIndexPAS(vsNew.getAdcIndex(), vsNew.getName()));
			// poolOld = null;
		} else if (changeType == OBDefine.CHANGE_TYPE_DELETE) {
			vsOld = OBAdcVServerPAS.cloneVServer(virtualServerOld);
			vsNew = null;
		} else
		// virtual server edit
		{
			vsOld = OBAdcVServerPAS.cloneVServer(virtualServerOld);
			vsNew = OBAdcVServerPAS.cloneVServer(virtualServerNew);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "virtualServerNew = " + virtualServerNew);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "vsNew = " + vsNew);
			// F5같이 pool이 독립적으로 운영되는 경우는 pool도 old/new 비교를 해야 하는데, PAS는 pool이 VS 종속이므로 따로 하지
			// 않는다.
		}

		try {
			vsConfig = DiffVServerPAS(vsOld, vsNew); // 두 virtual server의 차이 비교
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// , String.format("%s", e.getMessage()));
		}
		// 기본데이터 보강
		configChunk.setUserType(userType);
		configChunk.setChangeType(vsConfig.getChange());
		configChunk.setChangeObject(changeObject);
		configChunk.setAccountIndex(accountIndex);
		configChunk.setVsConfig(vsConfig);
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "end.");
		return configChunk;
	}

	// F5같이 pool이 독립적으로 운영되는 경우는 pool도 old/new 비료를 해야 하는데, PAS는 pool이 VS 종속이므로 따로 하지
	// 않는다.
	public OBDtoAdcConfigVServerPAS DiffVServerPAS(OBDtoAdcVServerPAS vsOld, OBDtoAdcVServerPAS vsNew)
			throws Exception {
		OBDtoAdcConfigVServerPAS vsConfig = new OBDtoAdcConfigVServerPAS(); // 리턴할 값

		vsConfig.setChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setIpChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setProtocolChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setPortChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setStateChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setPoolChange(OBDefine.CHANGE_TYPE_NONE);

		vsConfig.setVsNew(null);
		vsConfig.setVsOld(null);
		vsConfig.setPoolConfig(null);

		if (vsOld == null && vsNew != null) // vs add
		{
			vsConfig.setChange(OBDefine.CHANGE_TYPE_ADD);
			vsConfig.setIpChange(OBDefine.CHANGE_TYPE_EDIT);
			vsConfig.setProtocolChange(OBDefine.CHANGE_TYPE_EDIT);
			vsConfig.setPortChange(OBDefine.CHANGE_TYPE_EDIT);
			// vs state는 추가시 기본 enable 되므로 건드리지 않는다.

			// POOL
			OBDtoAdcPoolPAS poolNew = OBAdcVServerPAS.clonePool(vsNew.getPool());
			OBDtoAdcConfigPoolPAS poolConfig = new OBDtoAdcConfigPoolPAS();
			poolConfig.setChange(OBDefine.CHANGE_TYPE_NONE);
			poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_NONE);
			poolConfig.setLbMethodChange(OBDefine.CHANGE_TYPE_NONE);
			poolConfig.setMemberChange(OBDefine.CHANGE_TYPE_NONE);

			// PAS에서 pool은 필수이므로 null 체크는 안 해도 된다만...
			if (poolNew != null) {
				poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				// LB-Method
				poolConfig.setLbMethodChange(OBDefine.CHANGE_TYPE_EDIT);
				// health check
				if (poolNew.getHealthCheckInfo() != null) {
					poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_ADD);
				}
				OBDtoAdcConfigVServerPAS temp = new OBDtoAdcConfigVServerPAS();
				temp = DiffPoolMemberPAS(vsNew.getAdcIndex(), null, poolNew.getMemberList()); // member 비교
				if (temp.getPoolConfig().getMemberConfigList() != null
						&& temp.getPoolConfig().getMemberConfigList().size() != 0) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "changed(add/del/edit) member count = "
							+ temp.getPoolConfig().getMemberConfigList().size());
					poolConfig.setMemberChange(OBDefine.CHANGE_TYPE_EDIT);
				}
				poolConfig.setMemberConfigList(temp.getPoolConfig().getMemberConfigList());
			}
			vsConfig.setPoolConfig(poolConfig);
		} else if (vsOld != null && vsNew == null) // vs delete
		{
			vsConfig.setChange(OBDefine.CHANGE_TYPE_DELETE);
		}
		// else //virtual server edit
		else if (vsNew != null && vsOld != null) // vs edit
		{
			if (vsOld.getvIP().equals(vsNew.getvIP()) == false) {
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setIpChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			if (vsOld.getSrvProtocol().equals(vsNew.getSrvProtocol()) == false) {
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setProtocolChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			if (vsOld.getSrvPort().equals(vsNew.getSrvPort()) == false) {
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setPortChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			// F5는 이 부분에서 pool 교체(같은 pool의 설정 내역 변경이 아니라)를 파악한다.
			// 그러나 PAS는 pool이 필수이고 VS에 종속-고정되므로 교체 개념이 없어 구현할 것이 없다.
			vsConfig.setPoolChange(OBDefine.CHANGE_TYPE_NONE);

			if (vsOld.getState() == null && vsNew.getState() == null) {
				vsConfig.setStateChange(OBDefine.CHANGE_TYPE_NONE);
			} else if (vsOld.getState() == null && vsNew.getState() != null) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "state old null / new " + vsNew.getState());
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setStateChange(OBDefine.CHANGE_TYPE_EDIT);
			} else if (vsOld.getState() != null && vsNew.getState() == null) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "state new null / old " + vsOld.getState());
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setStateChange(OBDefine.CHANGE_TYPE_EDIT);
			} else {
				if (vsOld.getState().equals(vsNew.getState()) == false) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							"state old-new = " + vsOld.getState() + " / " + vsNew.getState());
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					vsConfig.setStateChange(OBDefine.CHANGE_TYPE_EDIT);
				} else {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							"state old-new = " + vsOld.getState() + " / " + vsNew.getState());
					vsConfig.setStateChange(OBDefine.CHANGE_TYPE_NONE);
				}
			}
			// POOL
			OBDtoAdcPoolPAS poolNew = OBAdcVServerPAS.clonePool(vsNew.getPool());
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "new MEMBERS ==== " + vsNew.getPool().getMemberList());
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "new MEMBERS2==== " + poolNew.getMemberList());
			OBDtoAdcPoolPAS poolOld = OBAdcVServerPAS.clonePool(vsOld.getPool());
			OBDtoAdcConfigPoolPAS poolConfig = new OBDtoAdcConfigPoolPAS();
			poolConfig.setChange(OBDefine.CHANGE_TYPE_NONE);
			poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_NONE);
			poolConfig.setLbMethodChange(OBDefine.CHANGE_TYPE_NONE);
			poolConfig.setMemberChange(OBDefine.CHANGE_TYPE_NONE);

			// PAS에서 pool은 필수이므로 null 체크는 안 해도 된다만...
			if (poolNew != null && poolOld != null) {
				// LB-Method
				if (poolOld.getLbMethod().equals(poolNew.getLbMethod()) == false) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "-------------lbmethod changed");
					poolConfig.setLbMethodChange(OBDefine.CHANGE_TYPE_EDIT);
					poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				}
				// health check
				if (poolOld.getHealthCheckInfo() == null && poolNew.getHealthCheckInfo() != null) {
					poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_ADD);
					poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							"health add / new health = " + poolNew.getHealthCheckInfo());
				} else if (poolOld.getHealthCheckInfo() != null && poolNew.getHealthCheckInfo() == null) {
					poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_DELETE);
					poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							"health del / old health = " + poolOld.getHealthCheckInfo());
				} else if (poolOld.getHealthCheckInfo() != null && poolNew.getHealthCheckInfo() != null) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "old health = " + poolOld.getHealthCheckInfo());
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "new health = " + poolNew.getHealthCheckInfo());
					if (poolOld.getHealthCheckInfo().getId().equals(poolNew.getHealthCheckInfo().getId()) == false // 복수일
																													// 경우?
							|| poolOld.getHealthCheckInfo().getType()
									.equals(poolNew.getHealthCheckInfo().getType()) == false
					// ||
					// poolOld.getHealthCheckInfo().getState().equals(poolNew.getHealthCheckInfo().getState())==false
					// //설정 안함
					// ||
					// poolOld.getHealthCheckInfo().getPort().equals(poolNew.getHealthCheckInfo().getPort())==false
					// //설정 안함
					// ||
					// poolOld.getHealthCheckInfo().getInterval().equals(poolNew.getHealthCheckInfo().getInterval())==false
					// //설정 안함
					// ||
					// poolOld.getHealthCheckInfo().getTimeout().equals(poolNew.getHealthCheckInfo().getTimeout())==false
					// //설정 안함
					) {
						poolConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_EDIT);
						poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					}
				}

				OBDtoAdcConfigVServerPAS temp = new OBDtoAdcConfigVServerPAS();
				temp = DiffPoolMemberPAS(vsNew.getAdcIndex(), poolOld.getMemberList(), poolNew.getMemberList());
				if (temp.getPoolConfig().getMemberConfigList() != null
						&& temp.getPoolConfig().getMemberConfigList().size() != 0) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "changed(add/del/edit) member count = "
							+ temp.getPoolConfig().getMemberConfigList().size());

					poolConfig.setMemberChange(OBDefine.CHANGE_TYPE_EDIT);
					poolConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				}
				poolConfig.setMemberConfigList(temp.getPoolConfig().getMemberConfigList());

				// pool delete는 없다

				// 이 부분을 불필요하다. PAS는 pool이 virtual server에 내포된 구조여서 이미 갖고 있다.
				// poolConfig.setPoolOld(poolOld);
				// poolConfig.setPoolNew(poolNew);
			}

			if (vsConfig.getChange() != OBDefine.CHANGE_TYPE_ADD && vsConfig.getChange() != OBDefine.CHANGE_TYPE_DELETE) // EDIT이나
																															// NONE에
																															// 대해서만
																															// 수정
			{
				if (poolConfig.getChange() != OBDefine.CHANGE_TYPE_NONE) {
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				}
			}
			vsConfig.setPoolConfig(poolConfig);
		}
		vsConfig.setVsOld(vsOld);
		vsConfig.setVsNew(vsNew);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "history pas  vsConfig = " + vsConfig);
		return vsConfig;
	}

	private OBDtoAdcConfigVServerPAS DiffPoolMemberPAS(int adcIndex, ArrayList<OBDtoAdcPoolMemberPAS> oldMemberList,
			ArrayList<OBDtoAdcPoolMemberPAS> newMemberList) throws Exception {
		int i, j;
		boolean bMemberMatched = false;

		OBDtoAdcConfigVServerPAS objVs = new OBDtoAdcConfigVServerPAS();
		OBDtoAdcConfigPoolPAS objPool = new OBDtoAdcConfigPoolPAS();
		// ArrayList<OBDtoAdcConfigNodePAS> nodeConfigList = new
		// ArrayList<OBDtoAdcConfigNodePAS>();

		if (oldMemberList == null && newMemberList == null) {
			objPool.setMemberConfigList(null);
			objVs.setPoolConfig(objPool);

			return objVs;
		}

		ArrayList<OBDtoAdcPoolMemberPAS> oldList;
		ArrayList<OBDtoAdcPoolMemberPAS> newList;
		if (oldMemberList == null) {
			oldList = new ArrayList<OBDtoAdcPoolMemberPAS>();
		} else {
			oldList = oldMemberList;
		}
		if (newMemberList == null) {
			newList = new ArrayList<OBDtoAdcPoolMemberPAS>();
		} else {
			newList = newMemberList;
		}

		ArrayList<OBDtoAdcPoolMemberPAS> poolMemberCommonList = new ArrayList<OBDtoAdcPoolMemberPAS>();
		ArrayList<OBDtoAdcConfigPoolMemberPAS> poolMemberConfigList = new ArrayList<OBDtoAdcConfigPoolMemberPAS>();

		OBDtoAdcPoolMemberPAS oldMember = null;
		OBDtoAdcPoolMemberPAS newMember = null;
		OBDtoAdcPoolMemberPAS commonMember = null;
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "old list = " + oldList);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "new list = " + newList);

		for (i = 0; i < newList.size(); i++) {
			newMember = newList.get(i);
			if (newMember.getId() != null) // 기존 멤버다. change or not-change
			{
				for (j = 0; j < oldList.size(); j++) {
					oldMember = oldList.get(j);
					if (newMember.getId().equals(oldMember.getId())) {
						poolMemberCommonList.add(new OBDtoAdcPoolMemberPAS(newMember)); // 공통 멤버 리스트에 넣는다.
						// id가 같으면 port나 status가 변했는지 본다.
						if (newMember.getPort().equals(oldMember.getPort()) == false
								|| newMember.getState().equals(oldMember.getState()) == false) {
							OBDtoAdcConfigPoolMemberPAS temp = new OBDtoAdcConfigPoolMemberPAS();
							temp.setChange(OBDefine.CHANGE_TYPE_EDIT);
							temp.setStateChange(OBDefine.CHANGE_TYPE_EDIT);
							temp.setMemberNew(new OBDtoAdcPoolMemberPAS(newMember));
							temp.setMemberOld(new OBDtoAdcPoolMemberPAS(oldMember));
							poolMemberConfigList.add(temp);
							OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "same member, diff TRUE");
						}
						break;
					}
				}
			} else {// 추가된 멤버
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "new member");
				OBDtoAdcConfigPoolMemberPAS temp = new OBDtoAdcConfigPoolMemberPAS();
				temp.setChange(OBDefine.CHANGE_TYPE_ADD);
				temp.setStateChange(OBDefine.CHANGE_TYPE_NONE);
				temp.setMemberNew(new OBDtoAdcPoolMemberPAS(newMember));
				temp.setMemberOld(null);

				poolMemberConfigList.add(temp);
			}
		}
		// 삭제된 멤버 구하기
		for (i = 0; i < oldList.size(); i++) {
			bMemberMatched = false;
			oldMember = oldList.get(i);
			for (j = 0; j < poolMemberCommonList.size(); j++) {
				commonMember = poolMemberCommonList.get(j);
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						"old = " + oldMember.getIpAddress() + " / common = " + commonMember.getId());
				if (oldMember.getId().equals(commonMember.getId())) {
					// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "matched !!!! = " +
					// oldMember.getIpAddress());
					bMemberMatched = true;
					break;
				}
			}
			if (bMemberMatched == false) // deleted member
			{
				OBDtoAdcConfigPoolMemberPAS temp = new OBDtoAdcConfigPoolMemberPAS();
				temp.setChange(OBDefine.CHANGE_TYPE_DELETE);
				temp.setStateChange(OBDefine.CHANGE_TYPE_NONE);
				temp.setMemberNew(null);
				temp.setMemberOld(new OBDtoAdcPoolMemberPAS(oldMember));
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "to delete !!!! = " + oldMember.getIpAddress());
				poolMemberConfigList.add(temp);
			}
		}

		poolMemberCommonList = null;// 쓴 것 비우기
		if (poolMemberConfigList.size() == 0) {
			// poolMemberConfigList=null;
		}
		objPool.setMemberConfigList(poolMemberConfigList);
		objVs.setPoolConfig(objPool);
		return objVs;
	}

	public OBDtoAdcConfigChunkPASK MakeConfigChunkPASK(OBDtoAdcVServerPASK virtualServerOld,
			OBDtoAdcVServerPASK virtualServerNew, int userType, int changeType, int changeObject, int accountIndex)
			throws OBException {
		OBVServerDB dbManager = new OBVServerDB();
		OBDatabase db = new OBDatabase();
		OBDtoAdcNodePASK tempNode = new OBDtoAdcNodePASK();

		// member 정보 보정
		// 1. 기존 있는 member의 웹에서 관리 안 하는 정보를 보충한다.
		// 2. real 목록에서 가져오지 않고 직접 입력해서 넣은 member가 기 등록된 real일 수 있으므로 확인해서 id를 구해서 보충한다.
		try {
			db.openDB();

			if (virtualServerNew != null) {
				for (OBDtoAdcPoolMemberPASK member : virtualServerNew.getPool().getMemberList()) {
					if (member.getId() != null) {
						tempNode = dbManager.getNodeInfoPASK(virtualServerNew.getAdcIndex(), member.getId(), db);
						if (tempNode != null) {
							member.setName(tempNode.getName());// 웹에서 이름을 갖고 오지 않으므로 보정한다.
						}
					} else
					// id==null, 신규 real을 갖고 새로 들어온 member들인데 IP/port/state가 맞으면 기존 real을 쓴다.
					{
						tempNode = dbManager.getNodeInfoPASK(virtualServerNew.getAdcIndex(), member.getIpAddress(),
								member.getPort(), member.getState(), db);
						if (tempNode != null) // 똑같은 조건의 node를 기존 node에서 찾은 경우
						{
							member.setId(tempNode.getId()); // id
							member.setName(tempNode.getName()); // name
							OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("registered real = %s", member));
						} else
						// 못 찾은 경우, 완전 신규 node
						{
						}
					}
				}
			}
		} catch (OBException e) {
			throw e;
		} finally {
			if (db != null)
				db.closeDB();
		}

		OBDtoAdcConfigChunkPASK configChunk = new OBDtoAdcConfigChunkPASK(); // 리턴할 값

		OBDtoAdcConfigVServerPASK vsConfig = null;

		OBDtoAdcVServerPASK vsOld = new OBDtoAdcVServerPASK();
		OBDtoAdcVServerPASK vsNew = new OBDtoAdcVServerPASK();
		// OBDtoAdcPoolPASK poolOld = new OBDtoAdcPoolPASK();

		if (changeType == OBDefine.CHANGE_TYPE_ADD) {
			vsOld = null;
			vsNew = OBAdcVServerPASK.cloneVServer(virtualServerNew);
			// 신규추가일 때는 vsIndex가 확정되지 않았기 때문에 vsIndex를 인위로 구성한다.
			vsNew.setDbIndex(OBCommon.makeVSIndexPAS(vsNew.getAdcIndex(), vsNew.getName()));
			// poolOld = null;
		} else if (changeType == OBDefine.CHANGE_TYPE_DELETE) {
			vsOld = OBAdcVServerPASK.cloneVServer(virtualServerOld);
			vsNew = null;
		} else
		// virtual server edit
		{
			vsOld = OBAdcVServerPASK.cloneVServer(virtualServerOld);
			vsNew = OBAdcVServerPASK.cloneVServer(virtualServerNew);
			// F5같이 pool이 독립적으로 운영되는 경우는 pool도 old/new 비교를 해야 하는데, PAS는 pool이 VS 종속이므로 따로 하지
			// 않는다.
		}

		try {
			vsConfig = diffVServerPASK(vsOld, vsNew); // 두 virtual server의 차이 비교
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// , String.format("%s", e.getMessage()));
		}
		// 기본데이터 보강
		configChunk.setUserType(userType);
		configChunk.setChangeType(vsConfig.getChange());
		configChunk.setChangeObject(changeObject);
		configChunk.setAccountIndex(accountIndex);
		configChunk.setVsConfig(vsConfig);
		OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, "end.");
		return configChunk;
	}

	private OBDtoAdcConfigVServerPASK diffVServerPASK(OBDtoAdcVServerPASK vsOld, OBDtoAdcVServerPASK vsNew)
			throws Exception {
		OBDtoAdcConfigVServerPASK vsConfig = new OBDtoAdcConfigVServerPASK(); // 리턴할 값

		vsConfig.setChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setIpChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setProtocolChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setPortChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setLbMethodChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setMemberChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setStateChange(OBDefine.CHANGE_TYPE_NONE);
		vsConfig.setSubinfoChange(OBDefine.CHANGE_TYPE_NONE);

		vsConfig.setVsNew(null);
		vsConfig.setVsOld(null);
		if (vsOld == null && vsNew != null) // vs add
		{
			// pool. PASK에서 pool은 필수이므로 null 체크는 안 해도 된다.
			OBDtoAdcPoolPASK poolNew = OBAdcVServerPASK.clonePool(vsNew.getPool());

			vsConfig.setChange(OBDefine.CHANGE_TYPE_ADD);
			vsConfig.setIpChange(OBDefine.CHANGE_TYPE_EDIT);
			vsConfig.setProtocolChange(OBDefine.CHANGE_TYPE_EDIT);
			vsConfig.setPortChange(OBDefine.CHANGE_TYPE_EDIT);

			vsConfig.setLbMethodChange(OBDefine.CHANGE_TYPE_EDIT);
			if (poolNew.getHealthCheckInfo() != null) {
				vsConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_ADD);
			}
			// vs state는 추가시 기본 enable 되므로 건드리지 않는다.
			vsConfig.setSubinfoChange(OBDefine.CHANGE_TYPE_EDIT);

			OBDtoAdcConfigVServerPASK temp = new OBDtoAdcConfigVServerPASK();
			temp = diffMemberPASK(vsNew.getAdcIndex(), null, poolNew.getMemberList()); // member 변화 파악: 기존 멤버 변화, 새로 추가
																						// 파악
			if (temp.getMemberConfigList() != null && temp.getMemberConfigList().size() != 0) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
						"changed(add/del/edit) member count = " + temp.getMemberConfigList().size());
				vsConfig.setMemberChange(OBDefine.CHANGE_TYPE_EDIT);
			} else {
				vsConfig.setMemberChange(OBDefine.CHANGE_TYPE_NONE);
			}
			vsConfig.setMemberConfigList(temp.getMemberConfigList());
			vsConfig.setNodeConfigList(temp.getNodeConfigList());
		} else if (vsOld != null && vsNew == null) // virtual server delete
		{
			vsConfig.setChange(OBDefine.CHANGE_TYPE_DELETE);
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "DELETE");
		}
		// else = virtual server edit
		else if (vsNew != null && vsOld != null) // vs edit
		{
			if (vsOld.getvIP().equals(vsNew.getvIP()) == false) {
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setIpChange(OBDefine.CHANGE_TYPE_EDIT);
			}
			if (vsOld.getSrvProtocol().equals(vsNew.getSrvProtocol()) == false) {
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setProtocolChange(OBDefine.CHANGE_TYPE_EDIT);
			}
			if (vsOld.getSrvPort().equals(vsNew.getSrvPort()) == false) {
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setPortChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			// F5는 이 부분에서 pool 교체(같은 pool 설정 변경이 아니라)를 파악한다. 그러나 PAS/PASK는 pool이 필수이고 VS에
			// 종속-고정되므로 교체 개념이 없어 할 것이 없다.

			if (vsOld.getState() == null && vsNew.getState() == null) {
				vsConfig.setStateChange(OBDefine.CHANGE_TYPE_NONE);
			} else if (vsOld.getState() == null && vsNew.getState() != null) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "state old null / new " + vsNew.getState());
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setStateChange(OBDefine.CHANGE_TYPE_EDIT);
			} else if (vsOld.getState() != null && vsNew.getState() == null) {
				OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "state new null / old " + vsOld.getState());
				vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
				vsConfig.setStateChange(OBDefine.CHANGE_TYPE_EDIT);
			} else {
				if (vsOld.getState().equals(vsNew.getState()) == false) {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							"state old-new = " + vsOld.getState() + " / " + vsNew.getState());
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					vsConfig.setStateChange(OBDefine.CHANGE_TYPE_EDIT);
				} else {
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							"state old-new = " + vsOld.getState() + " / " + vsNew.getState());
					vsConfig.setStateChange(OBDefine.CHANGE_TYPE_NONE);
				}
			}

			if (vsOld.getSubInfo().equals(vsNew.getSubInfo()) == false) {
				vsConfig.setSubinfoChange(OBDefine.CHANGE_TYPE_EDIT);
			}

			OBDtoAdcPoolPASK poolNew = OBAdcVServerPASK.clonePool(vsNew.getPool());
			OBDtoAdcPoolPASK poolOld = OBAdcVServerPASK.clonePool(vsOld.getPool());
			// PAS/PASK에서 pool은 필수이므로 null 체크는 안 해도 된다
			if (poolNew != null && poolOld != null) {
				// LB-Method
				if (poolOld.getLbMethod().equals(poolNew.getLbMethod()) == false) {
					vsConfig.setLbMethodChange(OBDefine.CHANGE_TYPE_EDIT);
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							"lbmethod changed: " + poolOld.getLbMethod() + " --> " + poolNew.getLbMethod());
				}
				// health check
				if (poolOld.getHealthCheckInfo() == null && poolNew.getHealthCheckInfo() != null) {
					vsConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_ADD);
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "health add: null --> " + poolNew.getHealthCheckInfo());
				} else if (poolOld.getHealthCheckInfo() != null && poolNew.getHealthCheckInfo() == null) {
					vsConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_DELETE);
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							"health del: " + poolOld.getHealthCheckInfo().getId() + " --> null");
				} else if (poolOld.getHealthCheckInfo() != null && poolNew.getHealthCheckInfo() != null) {
					if (poolOld.getHealthCheckInfo().getId().equals(poolNew.getHealthCheckInfo().getId()) == false) // 복수일
																													// 경우?
					{
						vsConfig.setHealthCheckChange(OBDefine.CHANGE_TYPE_EDIT);
						vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
						OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "health edit: " + poolOld.getHealthCheckInfo().getId()
								+ " --> " + poolNew.getHealthCheckInfo().getId());
					}
				}
				OBDtoAdcConfigVServerPASK temp = new OBDtoAdcConfigVServerPASK();
				temp = diffMemberPASK(vsNew.getAdcIndex(), poolOld.getMemberList(), poolNew.getMemberList()); // old-new
																												// 변경된
																												// 멤버를
																												// 구한다.
				if (temp.getMemberConfigList() != null && temp.getMemberConfigList().size() != 0) {
					vsConfig.setMemberChange(OBDefine.CHANGE_TYPE_EDIT);
					vsConfig.setChange(OBDefine.CHANGE_TYPE_EDIT);
					OBSystemLog.debug(OBDefine.LOGFILE_DEBUG,
							"changed(add/del/edit) member count = " + temp.getMemberConfigList().size());
				}
				vsConfig.setMemberConfigList(temp.getMemberConfigList());
				vsConfig.setNodeConfigList(temp.getNodeConfigList());
			}
		}
		vsConfig.setVsOld(vsOld);
		vsConfig.setVsNew(vsNew);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "end. vsConfig = " + vsConfig);
		return vsConfig;
	}

	private OBDtoAdcConfigVServerPASK diffMemberPASK(int adcIndex, ArrayList<OBDtoAdcPoolMemberPASK> oldMemberList,
			ArrayList<OBDtoAdcPoolMemberPASK> newMemberList) throws Exception {
		int i, j;
		boolean bMemberMatched = false;

		OBDtoAdcConfigVServerPASK vs = new OBDtoAdcConfigVServerPASK();
		ArrayList<OBDtoAdcConfigNodePASK> nodeConfigList = new ArrayList<OBDtoAdcConfigNodePASK>();

		if (oldMemberList == null && newMemberList == null) {
			vs.setMemberConfigList(null);
			return vs;
		}

		ArrayList<OBDtoAdcPoolMemberPASK> oldList;
		ArrayList<OBDtoAdcPoolMemberPASK> newList;
		if (oldMemberList == null) {
			oldList = new ArrayList<OBDtoAdcPoolMemberPASK>();
		} else {
			oldList = oldMemberList;
		}
		if (newMemberList == null) {
			newList = new ArrayList<OBDtoAdcPoolMemberPASK>();
		} else {
			newList = newMemberList;
		}

		ArrayList<OBDtoAdcPoolMemberPASK> tempCommonMembers = new ArrayList<OBDtoAdcPoolMemberPASK>(); // 임시 작업 변수
		ArrayList<OBDtoAdcConfigPoolMemberPASK> memberConfigList = new ArrayList<OBDtoAdcConfigPoolMemberPASK>(); // 멤버
																													// config
																													// 목록

		OBDtoAdcPoolMemberPASK oldMember = null;
		OBDtoAdcPoolMemberPASK newMember = null;
		OBDtoAdcPoolMemberPASK commonMember = null;
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "old list = " + oldList);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "new list = " + newList);

		String tempNewMemberName = null, tempOldMemberName = null; // real server 이름 비교할 때 쓴다. 이름 null이 가능해서 직접 비교하기
																	// 복잡하다.

		for (i = 0; i < newList.size(); i++) {
			newMember = newList.get(i);
			bMemberMatched = false;

			if (newMember.getId() != null) // id가 null이 아니면, 기존 real을 가져와서 만든 멤버거나, 이미 있던멤버다.
			{
				for (j = 0; j < oldList.size(); j++) {
					oldMember = oldList.get(j);
					if (newMember.getId().equals(oldMember.getId())) // new와 같은 id가 old에서 걸리면 둘이 비교
					{
						bMemberMatched = true;
						break;
					}
				}
			}

			if (bMemberMatched == true) // 공통으로 있는 멤버
			{
				tempCommonMembers.add(new OBDtoAdcPoolMemberPASK(newMember)); // 공통 멤버 리스트에 넣는다.
				tempNewMemberName = newMember.getName() == null ? "null" : newMember.getName();
				tempOldMemberName = oldMember.getName() == null ? "null" : oldMember.getName();

				// id는 같다. rip, name, port, status가 변했는지 본다. 넷 다 바뀔 수 있다.
				if (newMember.getIpAddress().equals(oldMember.getIpAddress()) == false
						|| tempNewMemberName.equals(tempOldMemberName) == false
						|| newMember.getPort().equals(oldMember.getPort()) == false // 없을 때 -1
						|| newMember.getState().equals(oldMember.getState()) == false // 1(enable) 또는 0(disable)만 있으므로
																						// 문제 없음
				) {
					OBDtoAdcConfigPoolMemberPASK temp = new OBDtoAdcConfigPoolMemberPASK();
					temp.setChange(OBDefine.CHANGE_TYPE_EDIT);
					if (newMember.getState().equals(oldMember.getState()) == false) {
						temp.setStateChange(OBDefine.CHANGE_TYPE_EDIT);
					}
					temp.setMemberNew(new OBDtoAdcPoolMemberPASK(newMember));
					temp.setMemberOld(new OBDtoAdcPoolMemberPASK(oldMember));
					memberConfigList.add(temp);
					// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "member id same - changed");
				}
			} else
			// 추가된 멤버, node(real)도 없을 수 있으므로 확인한다.
			{
				// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "new member");
				OBDtoAdcConfigPoolMemberPASK temp = new OBDtoAdcConfigPoolMemberPASK();
				temp.setChange(OBDefine.CHANGE_TYPE_ADD);
				temp.setStateChange(OBDefine.CHANGE_TYPE_NONE);
				temp.setMemberNew(new OBDtoAdcPoolMemberPASK(newMember));
				temp.setMemberOld(null);
				memberConfigList.add(temp); // 추가 등록해야 하는 멤버

				// id가 null이면 real에도 없는 상태이다.
				if (newMember.getId() == null) {
					OBDtoAdcNodePASK node = new OBDtoAdcNodePASK();
					node.setId(null);
					node.setIpAddress(newMember.getIpAddress());
					node.setName(null);
					node.setPort(newMember.getPort());
					node.setState(newMember.getState());

					OBDtoAdcConfigNodePASK nodeConfig = new OBDtoAdcConfigNodePASK();
					nodeConfig.setChange(OBDefine.CHANGE_TYPE_ADD);
					nodeConfig.setIpAddressChange(OBDefine.CHANGE_TYPE_NONE);
					nodeConfig.setNameChange(OBDefine.CHANGE_TYPE_NONE);
					nodeConfig.setPortChange(OBDefine.CHANGE_TYPE_NONE);
					nodeConfig.setStateChange(OBDefine.CHANGE_TYPE_NONE);
					nodeConfig.setNodeOld(null);
					nodeConfig.setNodeNew(node);
					nodeConfigList.add(nodeConfig);
				}
			}
		}

		// vs에서 빠진 멤버 구하기
		for (i = 0; i < oldList.size(); i++) {
			bMemberMatched = false;
			oldMember = oldList.get(i);
			for (j = 0; j < tempCommonMembers.size(); j++) {
				commonMember = tempCommonMembers.get(j);
				if (oldMember.getId().equals(commonMember.getId())) {
					bMemberMatched = true;
					break;
				}
			}
			if (bMemberMatched == false) // unset 해야되는 member
			{
				OBDtoAdcConfigPoolMemberPASK temp = new OBDtoAdcConfigPoolMemberPASK();
				temp.setChange(OBDefine.CHANGE_TYPE_DELETE);
				temp.setStateChange(OBDefine.CHANGE_TYPE_NONE);
				temp.setMemberNew(null);
				temp.setMemberOld(new OBDtoAdcPoolMemberPASK(oldMember));
				memberConfigList.add(temp);
			}
		}

		tempCommonMembers = null;// 쓴 것 비우기
		if (memberConfigList.size() == 0) {
			// poolMemberConfigList=null;
		}

		vs.setMemberConfigList(memberConfigList);
		vs.setNodeConfigList(nodeConfigList);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "memberConfigList = " + memberConfigList);
		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "nodeConfigList = " + nodeConfigList);
		return vs;
	}

	public boolean isHealthcheckAlteonChanged(OBDtoAdcHealthCheckAlteon hcOld, OBDtoAdcHealthCheckAlteon hcNew) {
		if (hcOld == null && hcNew == null) {
			return false;
		} else if (hcOld == null && hcNew != null) {
			if (hcNew.getId() != null && hcNew.getId().isEmpty() == false) {
				return true;
			} else {
				return false;
			}
		} else if (hcOld != null && hcNew == null) {
			if (hcOld.getId() != null && hcOld.getId().isEmpty() == false) {
				return true;
			} else {
				return false;
			}
		} else
		// 둘다 null 아님
		{
			if (hcOld.getId().equals(hcNew.getId()) == false) // healthcheck가 있으면 id는 필수로 있다고 보기때문에 null 검사 안함
			{
				return true; // 변함
			} else {
				return false; // 같음
			}
		}
	}

//    public static void main(String[] args)
//    {
//        try
//        {
//            OBAdcConfigHistoryImpl tester = new OBAdcConfigHistoryImpl();
//            // tester.testMakeHistory("3_obLabTest4", OBDefine.ADC_TYPE_F5);
//            // tester.testGetHistory("3_obLabTest4", OBDefine.ADC_TYPE_F5);
//            // tester.testMakeHistory("2_101", OBDefine.ADC_TYPE_ALTEON);
//            // tester.testGetHistory("2_101", OBDefine.ADC_TYPE_ALTEON);
//            // tester.testMakeHistory("1_aayk", OBDefine.ADC_TYPE_PIOLINK_PAS);
//            tester.testGetHistory("1_aayk", OBDefine.ADC_TYPE_PIOLINK_PAS);
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

	public void testGetHistory(String vsIndex, Integer vendor) throws OBException {
		// 목록 가져오기 테스트
		// args
		Integer adcIndex = 0;
		String searchKey = "";
		Date date_from = null;
		Date date_to = null;

		OBAdcConfigHistoryImpl tester = new OBAdcConfigHistoryImpl();
		int count = tester.getAdcConfigHistoryTotalRecordCount(adcIndex, searchKey, null, null);

		ArrayList<OBDtoAdcConfigHistory> historyList = tester.getVsConfigHistoryListNoPagingNoSearch(adcIndex, null,
				date_from, date_to);

		for (OBDtoAdcConfigHistory history : historyList) {
			if (history.getVsIndex().equals(vsIndex)) {
				System.out.println(
						"<occur time> " + OBDateTime.toString(new Timestamp(history.getOccurTime().getTime())));
				System.out.println("	virtual server ==> " + history.getVsName() + " / " + history.getVsIp());
				System.out.println("	status ==> " + history.getVsStatus());
				System.out.println("	summary ==> " + history.getSummary());
				System.out.println("	user ==> " + history.getAccountName());
			}
		}

		// virtual server 상세정보 보기 테스트
		if (vendor.equals(OBDefine.ADC_TYPE_F5)) {
			OBDtoAdcConfigHistoryF5 log = tester.getVSHistoryInfoF5(vsIndex, count);
			if (log.getVsConfigInfoNew() != null) {
				System.out.println("<virtual server> " + log.getVsConfigInfoNew().getVsName());
			}
			if (log.getVsConfigInfoOld() != null) {
				System.out.println("	time ==> " + log.getVsConfigInfoOld().getLastTime());
				System.out.println("	vip ==> " + log.getVsConfigInfoOld().getVsIPAddress());
				System.out.println("	lbmethod ==> " + log.getVsConfigInfoOld().getPool().getLbMethod());
				System.out
						.println("	member old ==> " + log.getVsConfigInfoOld().getPool().getMemberList().toString());
			}
			if (log.getVsConfigInfoNew() != null) {
				System.out.println("	time ==> " + log.getVsConfigInfoNew().getLastTime());
				System.out.println("	vip ==> " + log.getVsConfigInfoNew().getVsIPAddress());
				System.out.println("	lbmethod ==> " + log.getVsConfigInfoNew().getPool().getLbMethod());
				System.out
						.println("	member new ==> " + log.getVsConfigInfoNew().getPool().getMemberList().toString());
			}
		} else if (vendor.equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
			OBDtoAdcConfigHistoryPAS log = tester.getVSHistoryInfoPAS(vsIndex, count);
			if (log.getVsConfigInfoOld() != null) {
				System.out.println("	time ==> " + log.getVsConfigInfoOld().getLastTime());
				System.out.println("	vip ==> " + log.getVsConfigInfoOld().getVsIPAddress());
				System.out.println("	lbmethod ==> " + log.getVsConfigInfoOld().getPool().getLbMethod());
				System.out
						.println("	member old ==> " + log.getVsConfigInfoOld().getPool().getMemberList().toString());
			}
			if (log.getVsConfigInfoNew() != null) {
				System.out.println("	time ==> " + log.getVsConfigInfoNew().getLastTime());
				System.out.println("	vip ==> " + log.getVsConfigInfoNew().getVsIPAddress());
				System.out.println("	lbmethod ==> " + log.getVsConfigInfoNew().getPool().getLbMethod());
				System.out
						.println("	member new ==> " + log.getVsConfigInfoNew().getPool().getMemberList().toString());
			}
		} else if (vendor.equals(OBDefine.ADC_TYPE_ALTEON)) {
			OBDtoAdcConfigHistoryAlteon log = tester.getVSHistoryInfoAlteon(vsIndex, count);
			if (log.getVsConfigInfoOld() != null) {
				System.out.println("	time ==> " + log.getVsConfigInfoOld().getLastTime());
				System.out.println("	vip ==> " + log.getVsConfigInfoOld().getVsIPAddress());
			}
			if (log.getVsConfigInfoNew() != null) {
				System.out.println("	time ==> " + log.getVsConfigInfoNew().getLastTime());
				System.out.println("	vip ==> " + log.getVsConfigInfoNew().getVsIPAddress());
			}
		}
	}

	public void testMakeHistory(String vsIndex, Integer vendor) throws OBException, Exception {
		OBAdcConfigHistoryImpl historyManager = new OBAdcConfigHistoryImpl();
		OBDtoExtraInfo info = new OBDtoExtraInfo();
		info.setAccountIndex(1);
		info.setClientIPAddress("172.172.2.151");
		info.setExtraMsg1("ykkim's add virtual server test");

		OBDtoAdcConfigChunkPAS configChunkPAS = null;
		OBDtoAdcConfigChunkF5 configChunkF5 = null;
		OBDtoAdcConfigChunkAlteon configChunkAlteon = null;
		OBDtoAdcVServerF5 virtualServerF5;
		OBDtoAdcVServerPAS virtualServerPAS;
		OBDtoAdcVServerAlteon virtualServerAlteon;

		if (vendor.equals(OBDefine.ADC_TYPE_F5)) {
			virtualServerF5 = new OBVServerDB().getVServerInfoF5(vsIndex);
			virtualServerF5.setServicePort(9999);
			configChunkF5 = historyManager.MakeConfigChunkF5(virtualServerF5, OBDefine.CHANGE_BY_USER,
					OBDefine.CHANGE_TYPE_ADD, OBDefine.CHANGE_OBJECT_VIRTUALSERVER, info.getAccountIndex());
			historyManager.writeConfigHistoryF5(configChunkF5);
		} else if (vendor.equals(OBDefine.ADC_TYPE_PIOLINK_PAS)) {
			virtualServerPAS = new OBVServerDB().getVServerInfoPAS(vsIndex);
			virtualServerPAS.setSrvPort(9999);
			configChunkPAS = historyManager.MakeConfigChunkPAS(null, virtualServerPAS, OBDefine.CHANGE_BY_USER,
					OBDefine.CHANGE_TYPE_ADD, OBDefine.CHANGE_OBJECT_VIRTUALSERVER, info.getAccountIndex());
			historyManager.writeConfigHistoryPAS(configChunkPAS);
		} else if (vendor.equals(OBDefine.ADC_TYPE_ALTEON)) {
			virtualServerAlteon = new OBVServerDB().getVServerInfoAlteon(vsIndex);
			configChunkAlteon = historyManager.MakeConfigChunkAlteon(virtualServerAlteon, OBDefine.CHANGE_BY_USER,
					OBDefine.CHANGE_TYPE_ADD, OBDefine.CHANGE_OBJECT_VIRTUALSERVER, info.getAccountIndex());
			historyManager.writeConfigHistoryAlteon(configChunkAlteon);
		}
	}

	@Override
	public Long revertConfigF5Peer(Integer adcIndex, Long activeHistoryLogIndex, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException {// F5는 지원하는 않는다.
																										// F5는 iControl
																										// config sync
																										// API로 한다.
		throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not support fuction");
	}

	// adcIndex만 변경한다. vsIndex, poolIndex, memberIndex 정보도 같이 변경한다.
	// 입력받은 chunk데이터는 active 장비의 데이터이다. 그러나 우리가 필요한 데이터는 peer 장비의 데이터이다.
	// active 장비의 index 데이터를 peer 장비로 바꾼 chunk 데이터를 사용한다. bwpark.
	private OBDtoAdcConfigChunkAlteon convertChunkDataAlteon(Integer peerIndex,
			OBDtoAdcConfigChunkAlteon activeChunkData) throws OBException {
		OBDtoAdcConfigChunkAlteon retVal = activeChunkData;
		String vsIndex = "";
		OBDtoAdcConfigVServerAlteon vsConfig = activeChunkData.getVsConfig();
		if (vsConfig == null)
			return retVal;
		OBDtoAdcVServerAlteon vsObj = vsConfig.getVsNew();
		if (vsObj != null) {
			vsObj.setAdcIndex(peerIndex);
			vsObj.setIndex(OBCommon.makeVSIndexAlteon(peerIndex, vsObj.getAlteonId()));
			vsIndex = vsObj.getIndex();
			ArrayList<OBDtoAdcVService> vserviceList = vsObj.getVserviceList();
			if (vserviceList != null) {
				for (OBDtoAdcVService srvObj : vserviceList) {
					if (srvObj.getServiceIndex() != null && !srvObj.getServiceIndex().isEmpty())
						srvObj.setServiceIndex(
								OBCommon.makeVSrcIndexAlteon(peerIndex, vsObj.getIndex(), srvObj.getServicePort()));
					OBDtoAdcPoolAlteon pool = srvObj.getPool();
					if (pool != null) {
						if (pool.getIndex() != null && !pool.getIndex().isEmpty())
							pool.setIndex(OBCommon.makePoolIndexAlteon(peerIndex, pool.getAlteonId()));
						ArrayList<OBDtoAdcPoolMemberAlteon> memberList = pool.getMemberList();
						if (memberList != null) {
							for (OBDtoAdcPoolMemberAlteon member : memberList) {
								if (member.getIndex() != null && !member.getIndex().isEmpty())
									member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
											member.getAlteonNodeID(), member.getPort()));
							}
						}
					}
				}
			}
		}
		vsObj = vsConfig.getVsOld();
		if (vsObj != null) {
			vsObj.setAdcIndex(peerIndex);
			vsObj.setIndex(OBCommon.makeVSIndexAlteon(peerIndex, vsObj.getAlteonId()));
			vsIndex = vsObj.getIndex();
			ArrayList<OBDtoAdcVService> vserviceList = vsObj.getVserviceList();
			if (vserviceList != null) {
				for (OBDtoAdcVService srvObj : vserviceList) {
					if (srvObj.getServiceIndex() != null && !srvObj.getServiceIndex().isEmpty())
						srvObj.setServiceIndex(
								OBCommon.makeVSrcIndexAlteon(peerIndex, vsObj.getIndex(), srvObj.getServicePort()));
					OBDtoAdcPoolAlteon pool = srvObj.getPool();
					if (pool != null) {
						if (pool.getIndex() != null && !pool.getIndex().isEmpty())
							pool.setIndex(OBCommon.makePoolIndexAlteon(peerIndex, pool.getAlteonId()));
						ArrayList<OBDtoAdcPoolMemberAlteon> memberList = pool.getMemberList();
						if (memberList != null) {
							for (OBDtoAdcPoolMemberAlteon member : memberList) {
								if (member.getIndex() != null && !member.getIndex().isEmpty())
									member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
											member.getAlteonNodeID(), member.getPort()));
							}
						}
					}
				}
			}
		}
		// 바뀐 service 정보 목록
		ArrayList<OBDtoAdcConfigVServiceAlteon> serviceConfigList = vsConfig.getServiceConfigList();
		if (serviceConfigList != null) {
			for (OBDtoAdcConfigVServiceAlteon srvObj : serviceConfigList) {
				OBDtoAdcVService service = srvObj.getServiceNew();
				if (service != null) {
					if (service.getServiceIndex() != null && !service.getServiceIndex().isEmpty())
						service.setServiceIndex(
								OBCommon.makeVSrcIndexAlteon(peerIndex, vsIndex, service.getServicePort()));
					OBDtoAdcPoolAlteon pool = service.getPool();
					if (pool != null) {
						if (pool.getIndex() != null && !pool.getIndex().isEmpty())
							pool.setIndex(OBCommon.makePoolIndexAlteon(peerIndex, pool.getAlteonId()));
						ArrayList<OBDtoAdcPoolMemberAlteon> memberList = pool.getMemberList();
						if (memberList != null) {
							for (OBDtoAdcPoolMemberAlteon member : memberList) {
								if (member.getIndex() != null && !member.getIndex().isEmpty())
									member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
											member.getAlteonNodeID(), member.getPort()));
							}
						}
					}
				}
				service = srvObj.getServiceOld();
				if (service != null) {
					if (service.getServiceIndex() != null && !service.getServiceIndex().isEmpty())
						service.setServiceIndex(
								OBCommon.makeVSrcIndexAlteon(peerIndex, vsIndex, service.getServicePort()));
					OBDtoAdcPoolAlteon pool = service.getPool();
					if (pool != null) {
						if (pool.getIndex() != null && !pool.getIndex().isEmpty())
							pool.setIndex(OBCommon.makePoolIndexAlteon(peerIndex, pool.getAlteonId()));
						ArrayList<OBDtoAdcPoolMemberAlteon> memberList = pool.getMemberList();
						if (memberList != null) {
							for (OBDtoAdcPoolMemberAlteon member : memberList) {
								if (member.getIndex() != null && !member.getIndex().isEmpty())
									member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
											member.getAlteonNodeID(), member.getPort()));
							}
						}
					}
				}

				OBDtoAdcConfigPoolAlteon poolConfig = srvObj.getPoolConfig();
				if (poolConfig != null) {
					OBDtoAdcPoolAlteon pool = poolConfig.getPoolOld();
					if (pool != null) {
						if (pool.getIndex() != null && !pool.getIndex().isEmpty())
							pool.setIndex(OBCommon.makePoolIndexAlteon(peerIndex, pool.getAlteonId()));
						ArrayList<OBDtoAdcPoolMemberAlteon> memberList = pool.getMemberList();
						if (memberList != null) {
							for (OBDtoAdcPoolMemberAlteon member : memberList) {
								if (member.getIndex() != null && !member.getIndex().isEmpty())
									member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
											member.getAlteonNodeID(), member.getPort()));
							}
						}
					}
					pool = poolConfig.getPoolNew();
					if (pool != null) {
						if (pool.getIndex() != null && !pool.getIndex().isEmpty())
							pool.setIndex(OBCommon.makePoolIndexAlteon(peerIndex, pool.getAlteonId()));
						ArrayList<OBDtoAdcPoolMemberAlteon> memberList = pool.getMemberList();
						if (memberList != null) {
							for (OBDtoAdcPoolMemberAlteon member : memberList) {
								if (member.getIndex() != null && !member.getIndex().isEmpty())
									member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
											member.getAlteonNodeID(), member.getPort()));
							}
						}
					}

					ArrayList<OBDtoAdcPoolMemberAlteon> poolMemberList = poolConfig.getPoolMemberAddList(); // TODO :
																											// 데모후 지우기
					if (poolMemberList != null) {
						for (OBDtoAdcPoolMemberAlteon member : poolMemberList) {
							if (member.getIndex() != null && !member.getIndex().isEmpty())
								member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
										member.getAlteonNodeID(), member.getPort()));
						}
					}

					poolMemberList = poolConfig.getPoolMemberDelList(); // TODO : 데모후 지우기
					if (poolMemberList != null) {
						for (OBDtoAdcPoolMemberAlteon member : poolMemberList) {
							if (member.getIndex() != null && !member.getIndex().isEmpty())
								member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
										member.getAlteonNodeID(), member.getPort()));
						}
					}
					ArrayList<OBDtoAdcConfigPoolMemberAlteon> memberConfigList = poolConfig.getMemberConfigList();
					if (memberConfigList != null) {
						for (OBDtoAdcConfigPoolMemberAlteon poolMember : memberConfigList) {
							OBDtoAdcPoolMemberAlteon member = poolMember.getMemberOld();
							if (member != null) {
								if (member.getIndex() != null && !member.getIndex().isEmpty())
									member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
											member.getAlteonNodeID(), member.getPort()));
							}
							member = poolMember.getMemberNew();
							if (member != null) {
								if (member.getIndex() != null && !member.getIndex().isEmpty())
									member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
											member.getAlteonNodeID(), member.getPort()));
							}
						}
					}
				}
			}
		}

		// 바뀐 pool 정보 목록
		ArrayList<OBDtoAdcConfigPoolAlteon> poolConfigList = vsConfig.getPoolConfigList();
		if (poolConfigList != null) {
			for (OBDtoAdcConfigPoolAlteon poolConfig : poolConfigList) {
				OBDtoAdcPoolAlteon pool = poolConfig.getPoolOld();
				if (pool != null) {
					if (pool.getIndex() != null && !pool.getIndex().isEmpty())
						pool.setIndex(OBCommon.makePoolIndexAlteon(peerIndex, pool.getAlteonId()));
					ArrayList<OBDtoAdcPoolMemberAlteon> memberList = pool.getMemberList();
					if (memberList != null) {
						for (OBDtoAdcPoolMemberAlteon member : memberList) {
							if (member.getIndex() != null && !member.getIndex().isEmpty())
								member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
										member.getAlteonNodeID(), member.getPort()));
						}
					}
				}
				pool = poolConfig.getPoolNew();
				if (pool != null) {
					if (pool.getIndex() != null && !pool.getIndex().isEmpty())
						pool.setIndex(OBCommon.makePoolIndexAlteon(peerIndex, pool.getAlteonId()));
					ArrayList<OBDtoAdcPoolMemberAlteon> memberList = pool.getMemberList();
					if (memberList != null) {
						for (OBDtoAdcPoolMemberAlteon member : memberList) {
							if (member.getIndex() != null && !member.getIndex().isEmpty())
								member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
										member.getAlteonNodeID(), member.getPort()));
						}
					}
				}

				ArrayList<OBDtoAdcPoolMemberAlteon> poolMemberList = poolConfig.getPoolMemberAddList(); // TODO : 데모후
																										// 지우기
				if (poolMemberList != null) {
					for (OBDtoAdcPoolMemberAlteon member : poolMemberList) {
						if (member.getIndex() != null && !member.getIndex().isEmpty())
							member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
									member.getAlteonNodeID(), member.getPort()));
					}
				}

				poolMemberList = poolConfig.getPoolMemberDelList(); // TODO : 데모후 지우기
				if (poolMemberList != null) {
					for (OBDtoAdcPoolMemberAlteon member : poolMemberList) {
						if (member.getIndex() != null && !member.getIndex().isEmpty())
							member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
									member.getAlteonNodeID(), member.getPort()));
					}
				}
				ArrayList<OBDtoAdcConfigPoolMemberAlteon> memberConfigList = poolConfig.getMemberConfigList();
				if (memberConfigList != null) {
					for (OBDtoAdcConfigPoolMemberAlteon poolMember : memberConfigList) {
						OBDtoAdcPoolMemberAlteon member = poolMember.getMemberOld();
						if (member != null) {
							if (member.getIndex() != null && !member.getIndex().isEmpty())
								member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
										member.getAlteonNodeID(), member.getPort()));
						}
						member = poolMember.getMemberNew();
						if (member != null) {
							if (member.getIndex() != null && !member.getIndex().isEmpty())
								member.setIndex(OBCommon.makePoolMemberIndexAlteon(peerIndex, pool.getAlteonId(),
										member.getAlteonNodeID(), member.getPort()));
						}
					}
				}
			}
		}
		// 바뀐 node 정보 목록
		ArrayList<OBDtoAdcConfigNodeAlteon> nodeConfigList = vsConfig.getNodeConfigList();
		if (nodeConfigList != null) {
			for (OBDtoAdcConfigNodeAlteon nodeConfig : nodeConfigList) {
				OBDtoAdcNodeAlteon nodeInfo = nodeConfig.getNodeOld();
				if (nodeInfo != null) {
					if (nodeInfo.getIndex() != null && !nodeInfo.getIndex().isEmpty())
						nodeInfo.setIndex(OBCommon.makeNodeIndexAlteon(peerIndex, nodeInfo.getAlteonId()));
				}
				nodeInfo = nodeConfig.getNodeNew();
				if (nodeInfo != null) {
					if (nodeInfo.getIndex() != null && !nodeInfo.getIndex().isEmpty())
						nodeInfo.setIndex(OBCommon.makeNodeIndexAlteon(peerIndex, nodeInfo.getAlteonId()));
				}
			}
		}
		return retVal;
	}

	@Override
	public Long revertConfigAlteonPeer(Integer adcIndex, Long activeHistoryLogIndex, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException {
		Long retVal = 0L;
		try {
			OBDtoAdcConfigHistoryDB adcHistoryInfo = getLastConfigByLogIndex(activeHistoryLogIndex);

			if (adcHistoryInfo == null)
				return retVal;

			// convert chunk data.
			OBDtoAdcConfigChunkAlteon peerChunkData = convertChunkDataAlteon(adcIndex, adcHistoryInfo.getChunkAlteon());
			new OBAdcVServerAlteon().Revert(adcIndex, peerChunkData, extraInfo);
		} catch (OBExceptionUnreachable e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_UNREACHABLE, e.getMessage());
		} catch (OBExceptionLogin e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_LOGIN, e.getMessage());
		} catch (OBLicenseExpiredException e) {
			throw new OBException(OBException.ERRCODE_LICENSE_INVALID, e.getMessage());
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}

		return retVal;
	}

	@Override
	public Long revertConfigPASPeer(Integer adcIndex, Long activeHistoryLogIndex, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Long revertConfigPASKPeer(Integer adcIndex, Long activeHistoryLogIndex, OBDtoExtraInfo extraInfo)
			throws OBExceptionUnreachable, OBExceptionLogin, OBException, OBLicenseExpiredException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<OBDtoAdcConfigHistory> getAdcConfigHistoryList(Integer adcIndex, String searchKey, Date beginTime,
			Date endTime, Integer beginIndex, Integer endIndex, Integer orderType, Integer orderDir)
			throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("beginIndex = %d, endIndex = %d", beginIndex, endIndex));
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoAdcConfigHistory> list;
		try {
			db.openDB();

			// ADC의config history 전체를 구해야하기 때문에 2nd 파라미터가 null이다. 2nd 파라미터는 vsIndex인데 null로
			// 주면 이 조건은 무시된다.
			list = getConfigHistoryList(adcIndex, null, searchKey, beginTime, endTime, beginIndex, endIndex, orderType,
					orderDir, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return list;
	}
}