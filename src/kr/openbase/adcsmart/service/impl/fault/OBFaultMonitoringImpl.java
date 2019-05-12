package kr.openbase.adcsmart.service.impl.fault;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.snmp4j.smi.VariableBinding;

import kr.openbase.adcsmart.service.OBFaultMonitoring;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.dto.OBDtoConnectionData;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoHddInfo;
import kr.openbase.adcsmart.service.dto.OBDtoOrdering;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoTargetObject;
import kr.openbase.adcsmart.service.dto.OBDtoTrafficMapMember;
import kr.openbase.adcsmart.service.dto.OBDtoTrafficMapVServiceMembers;
import kr.openbase.adcsmart.service.dto.fault.OBDtoAdcCurrentSession;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCpuDataObj;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCpuHistory;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultDataObj;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultGroupMemberPerfInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultGroupPerfInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultHWStatus;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultPreBpsConnChart;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultRealInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultSessionInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultSvcPerfInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoL2SearchInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoL2SearchOption;
import kr.openbase.adcsmart.service.dto.fault.OBDtoSessionSearchOption;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.impl.OBVServerDB;
import kr.openbase.adcsmart.service.impl.alteon.OBAdcMonitorAlteon;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonHandler;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcMonCpuDataObj;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcNodeSimple;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcPoolSimple;
import kr.openbase.adcsmart.service.impl.dto.OBDtoCpuMemStatus;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonL2Ports;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficAdc;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficPoolGroup;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficPoolGroupMember;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficPoolMembers;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficServiceAlteon;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVServer;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVSvcInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTrafficVSvcMemberInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoPowerSupplyInfo;
import kr.openbase.adcsmart.service.impl.f5.OBAdcMonitorF5;
import kr.openbase.adcsmart.service.impl.f5.handler.OBAdcF5Handler;
import kr.openbase.adcsmart.service.impl.f5.handler.OBCLIParserF5;
import kr.openbase.adcsmart.service.impl.f5.handler.dto.OBDtoFdbInfo;
import kr.openbase.adcsmart.service.impl.pas.OBAdcMonitorPAS;
import kr.openbase.adcsmart.service.impl.pask.OBAdcMonitorPASK;
import kr.openbase.adcsmart.service.snmp.OBSnmp;
import kr.openbase.adcsmart.service.snmp.OBSnmpOidDB;
import kr.openbase.adcsmart.service.snmp.alteon.OBSnmpAlteon;
import kr.openbase.adcsmart.service.snmp.dto.DtoOidFaultCheckHW;
import kr.openbase.adcsmart.service.snmp.dto.OBDtoArpInfo;
import kr.openbase.adcsmart.service.snmp.f5.OBSnmpF5;
import kr.openbase.adcsmart.service.snmp.pas.OBSnmpPAS;
import kr.openbase.adcsmart.service.snmp.pask.OBSnmpPASK;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBFaultMonitoringImpl implements OBFaultMonitoring {
	// public static void main(String[] args) throws OBException
	// {
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// object.setIndex(4);
	// ArrayList<OBDtoSessionSearchOption> searchKeyList = new
	// ArrayList<OBDtoSessionSearchOption>();
	// OBDtoSessionSearchOption searchOption = new OBDtoSessionSearchOption();
	// searchOption.setType(OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP);
	// searchOption.setContent("172.172.2.209");
	// searchKeyList.add(searchOption);
	// // OBDtoSessionSearchOption searchOption2 = new OBDtoSessionSearchOption();
	// // searchOption2.setType(OBDtoSessionSearchOption.OPTION_TYPE_DST_IP);
	// // searchOption2.setContent("172.172.2.209");
	// // searchKeyList.add(searchOption2);
	//
	// OBDtoSearch pagingOption = new OBDtoSearch();
	// pagingOption.setBeginIndex(0);
	// pagingOption.setEndIndex(20);
	// ArrayList<OBDtoFaultSessionInfo> list = new
	// OBFaultMonitoringImpl().searchSessionInfoList(object, searchKeyList,
	// pagingOption);
	//
	// System.out.println(list);
	// }

	// private static final String STRING_TO_F5_VERSION = "11";
	// public ArrayList<OBDtoFaultSessionInfo> searchSessionInfoList(OBDtoADCObject
	// object, ArrayList<OBDtoSessionSearchOption> searchKeyList, OBDtoSearch
	// pagingOption, OBDtoOrdering orderOption) throws OBException
	// {
	// ArrayList<OBDtoFaultSessionInfo> retVal = new
	// ArrayList<OBDtoFaultSessionInfo>();
	// OBDatabase db = new OBDatabase();
	// Integer totRow = 0;
	// try
	// {
	// db.openDB();
	// }
	// catch(OBException e)
	// {
	// throw e;
	// }
	// try
	// {
	// ArrayList<Integer> adcIndexList = new ArrayList<Integer>();
	// if(object.getCategory()==OBDtoADCObject.CATEGORY_ADC)
	// {
	// adcIndexList.add(object.getIndex());
	// }
	// else
	// {
	// if(object.getCategory()==OBDtoADCObject.CATEGORY_ALL)
	// {
	// adcIndexList = new OBFaultMngImpl().getAdcListAll(0, db);
	// }
	// else
	// {
	// adcIndexList = new OBFaultMngImpl().getAdcList(object.getIndex(), db);
	// }
	// }
	//
	// ArrayList<OBDtoFaultSessionInfo> searchVal =
	// searchSessionInfoListGroup(adcIndexList, searchKeyList, db);
	//
	// // 수집된 로그를 db에 저장한다.
	// new OBFaultMonitoringDB().updateSessionLog(adcIndexList, searchVal, db);
	//
	// retVal = searchSessionInfoListBySort(object, pagingOption, orderOption);
	// // totRow = getSessionListCount(object, pagingOption);
	// // // DB로 부터 데이터를 조회하여 전달한다.
	// // OBDtoOrdering orderOption = new OBDtoOrdering();
	// // orderOption.setOrderDirection(OBDtoOrdering.DIR_DESCEND);
	// // orderOption.setOrderType(OBDtoOrdering.TYPE_1FIRST);
	// // retVal = getSessionInfoList(object, pagingOption, orderOption, db);
	// System.out.println(totRow);
	//
	// }
	// catch(OBException e)
	// {
	// db.closeDB();
	// throw e;
	// }
	// catch(Exception e)
	// {
	// db.closeDB();
	// throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//);
	// }
	// db.closeDB();
	// return retVal;
	// }

	static final int SESSION_SEARCH_LIMIT = 100; // default 세션 검색 결과 개수, adcsmart.properties 파일의

	// public static void main(String[] args) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// db.openDB();
	//
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// object.setIndex(10);
	//
	// OBDtoCpuMemStatus retVal = new OBDtoCpuMemStatus();
	// retVal = new OBFaultMonitoringImpl().getFaultAdcCpuMemoryUsage1Min(object,
	// db);
	// System.out.println(retVal);
	//
	// // OBDtoSearch searchOption = new OBDtoSearch();
	//
	// // OBDtoOrdering orderOption = new OBDtoOrdering();
	// // orderOption.setOrderDirection(OBDtoOrdering.DIR_ASCEND);
	// // orderOption.setOrderType(OBDtoOrdering.TYPE_2SECOND);
	// //
	// // ArrayList<OBDtoSessionSearchOption> searchKeyList = new
	// ArrayList<OBDtoSessionSearchOption>();
	// // OBDtoSessionSearchOption keyword = new OBDtoSessionSearchOption();
	// // keyword.setContent("192.168.0.5");
	// // keyword.setType(OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP);
	// // searchKeyList.add(keyword);
	// // OBDtoSessionSearchOption keyword2 = new OBDtoSessionSearchOption();
	// // keyword2.setContent("172.172.2.1");
	// // keyword2.setType(OBDtoSessionSearchOption.OPTION_TYPE_DST_IP);
	// // searchKeyList.add(keyword2);
	//
	// // Integer total = new
	// OBFaultMonitoringImpl().getSvcPerfInfoTotalCount(object, searchOption);
	// // System.out.println(total);
	// // Integer list= new OBFaultMonitoringImpl().searchSessionInfoList(object,
	// searchKeyList, null, null);
	// // System.out.println(list);
	// db.closeDB();
	// }

	@Override
	public Integer searchSessionInfoList(OBDtoADCObject object, ArrayList<OBDtoSessionSearchOption> searchKeyList,
			OBDtoSearch pagingOption, OBDtoOrdering orderOption) throws OBException {
		Integer totRow = 0;
		try {
			ArrayList<Integer> adcIndexList = new ArrayList<Integer>();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ADC) {
				adcIndexList.add(object.getIndex());
			} else {
				if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
					adcIndexList = new OBFaultMngImpl().getAdcListAll(0);
				} else {
					adcIndexList = new OBFaultMngImpl().getAdcList(object.getIndex());
				}
			}

			ArrayList<OBDtoFaultSessionInfo> searchVal = searchSessionInfoListGroup(adcIndexList, searchKeyList);

			// 수집된 로그를 db에 저장한다.
			new OBFaultMonitoringDB().updateSessionLog(adcIndexList, searchVal);
			totRow = getSessionListCount(object, pagingOption);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return totRow;
	}

	private ArrayList<OBDtoFaultSessionInfo> searchSessionInfoListGroup(ArrayList<Integer> adcIndexList,
			ArrayList<OBDtoSessionSearchOption> searchKeyList) throws OBException {
		ArrayList<OBDtoFaultSessionInfo> retVal = new ArrayList<OBDtoFaultSessionInfo>();

		try {
			for (Integer adcIndex : adcIndexList) {
				OBDtoADCObject obj = new OBDtoADCObject();
				obj.setCategory(OBDtoADCObject.CATEGORY_ADC);
				obj.setIndex(adcIndex);
				ArrayList<OBDtoFaultSessionInfo> sessionList = searchSessionInfoListADC(obj, searchKeyList);
				retVal.addAll(sessionList);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return retVal;
	}

	private ArrayList<OBDtoFaultSessionInfo> searchSessionInfoListADC(OBDtoADCObject object,
			ArrayList<OBDtoSessionSearchOption> searchKeyList) throws OBException {
		ArrayList<OBDtoFaultSessionInfo> retVal = new ArrayList<OBDtoFaultSessionInfo>();
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());
			if (adcInfo == null) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get adcInfo:%d", object.getIndex()));
				return retVal;
			}

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
				return searchSessionInfoListAlteon(adcInfo, searchKeyList);
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5)
				return searchSessionInfoListF5(adcInfo, searchKeyList);
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS)
				return searchSessionInfoListPAS(adcInfo, searchKeyList);
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK)
				return searchSessionInfoListPASK(adcInfo, searchKeyList);

		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return retVal;
	}

	// public static void main(String[] args) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// db.openDB();
	//
	// OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(1);
	// ArrayList<OBDtoSessionSearchOption> searchKeyList = new
	// ArrayList<OBDtoSessionSearchOption>();
	// OBDtoSessionSearchOption keyword = new OBDtoSessionSearchOption();
	// keyword.setContent("172.172.2.209");
	// keyword.setType(OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP);
	// searchKeyList.add(keyword);
	// // Integer total = new
	// OBFaultMonitoringImpl().getSvcPerfInfoTotalCount(object, searchOption);
	// // System.out.println(total);
	// ArrayList<OBDtoFaultSessionInfo> list= new
	// OBFaultMonitoringImpl().searchSessionInfoListAlteon(adcInfo, searchKeyList);
	// for(OBDtoFaultSessionInfo obj:list)
	// System.out.println(obj);
	// db.closeDB();
	// }

	private ArrayList<OBDtoFaultSessionInfo> searchSessionInfoListAlteon(OBDtoAdcInfo adcInfo,
			ArrayList<OBDtoSessionSearchOption> searchKeyList) throws OBException {
		ArrayList<OBDtoFaultSessionInfo> retVal = new ArrayList<OBDtoFaultSessionInfo>();
		try {
			OBAdcAlteonHandler channel = OBCommon.getValidAlteonHandler(adcInfo.getSwVersion());
			channel.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
					adcInfo.getAdcPasswordDecrypt(), adcInfo.getConnService(), adcInfo.getConnPort());

			// alteon의 경우에는 하나의 키워드로 검색할 수 있다. 키워드 조합으로 검색할 수 없다.
			// adcsmart에서 키워드 조합 기능을 제공하기 위해서는 검색된 결과를 2차 가공한다.
			// 검색시에는 src ip 또는 dst ip 중 하나로 검색한다.
			String searchMsg = "";

			String srcIP = "";
			String dstIP = "";
			String realIP = "";
			String slbType = "";
			Integer resultLimit = -1; // 기본값, 개수제한이 없음
			Integer resultLimitForAdc = -1; // ADC에서 실제로 구해올 결과수, 결과가 아닌 빈줄이나 헤더가 있을 수 있어서 약간 더 뽑아야 한다.
			for (OBDtoSessionSearchOption option : searchKeyList) {
				if (option != null && option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_DST_IP) {
					dstIP = option.getContent();
					slbType = option.getLbType();
				} else if (option != null && option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP) {
					srcIP = option.getContent();
					slbType = option.getLbType();
				} else if (option != null && option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_REAL_IP) {
					realIP = option.getContent();
					slbType = option.getLbType();
				}
			}
			try {
				String propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
						OBDefine.PROPERTY_KEY_FAULT_SESSION_MAX);
				if (propertyValue != null && !propertyValue.isEmpty()) {
					resultLimit = Integer.parseInt(propertyValue);
					if (resultLimit != null) // 값을 읽어오긴 했으나 숫자로 바꿔보니 문제가 있을 경우 기본값으로 처리한다.
					{
						resultLimitForAdc = resultLimit + 20;
					}
				}
			} catch (Exception e) {
			}

			channel.login();
			if (!srcIP.isEmpty()) {
				searchMsg = channel.cmndInfoSessCip(srcIP, resultLimitForAdc); // 헤더 라인등이 들어 있을 수 있으므로 20줄을 더 가져온다
			} else if (!dstIP.isEmpty()) {
				searchMsg = channel.cmndInfoSessDip(dstIP, resultLimitForAdc);
			} else if (!realIP.isEmpty()) {
				searchMsg = channel.cmndInfoSessRip(realIP, resultLimitForAdc);
			} else {
				channel.disconnect();
				return retVal;
			}
			channel.disconnect();

			if (searchMsg.isEmpty())
				return retVal;

			// 검색 조건에 맞는 데이터만 추출한다.(필터링).
			ArrayList<OBDtoFaultSessionInfo> sessionList = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion())
					.parseSeesionDumpList(adcInfo.getIndex(), searchMsg, resultLimit, slbType);
			retVal = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion())
					.remanufactoringParsedSessionList(adcInfo.getIndex(), sessionList, searchKeyList);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return retVal;
	}

	private ArrayList<OBDtoFaultSessionInfo> searchSessionInfoListF5(OBDtoAdcInfo adcInfo,
			ArrayList<OBDtoSessionSearchOption> searchKeyList) throws OBException {
		ArrayList<OBDtoFaultSessionInfo> retVal = new ArrayList<OBDtoFaultSessionInfo>();
		try {
			String searchMsg = "";

			String in_srcIP = "";
			String in_dstIP = "";
			String in_realIP = "";

			for (OBDtoSessionSearchOption option : searchKeyList) {
				if (option != null && option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_DST_IP) {
					in_dstIP = option.getContent();
				} else if (option != null && option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP) {
					in_srcIP = option.getContent();
				} else if (option != null && option.getType() == OBDtoSessionSearchOption.OPTION_TYPE_REAL_IP) {
					in_realIP = option.getContent();
				}
			}
			// String versionInfo = adcInfo.getSwVersion();

			ArrayList<OBDtoFaultSessionInfo> sessionList = new ArrayList<OBDtoFaultSessionInfo>();

			OBAdcF5Handler handler = OBCommon.getValidF5Handler(adcInfo.getSwVersion());
			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(),
					adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnPort());
			handler.sshLogin();
			if (in_srcIP != null && !in_srcIP.isEmpty()) {
				searchMsg = handler.sessionSearchInfo(searchKeyList);
			} else if (in_dstIP != null && !in_dstIP.isEmpty()) {
				searchMsg = handler.sessionSearchInfo(searchKeyList);
			} else if (in_realIP != null && !in_realIP.isEmpty()) {
				searchMsg = handler.sessionSearchInfo(searchKeyList);
			} else {
				handler.disconnect();
				return retVal;
			}
			handler.disconnect();

			if (searchMsg.isEmpty())
				return retVal;

			OBCLIParserF5 cliHandler = OBCommon.getValidF5CLIParser(adcInfo.getSwVersion());
			sessionList = cliHandler.parseSessionInfoList(adcInfo.getIndex(), searchMsg);

			// if(versionInfo.contains(STRING_TO_F5_VERSION))
			// {
			// OBAdcF5HandlerV11 handler = new OBAdcF5HandlerV11();
			// // handler.setConnectionInfo(adcInfo.getAdcIpAddress(),
			// adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt());
			// handler.setConnectionInfo(adcInfo.getAdcIpAddress(),
			// adcInfo.getAdcCliAccount(), adcInfo.getAdcCliPasswordDecrypt(),
			// adcInfo.getConnPort());
			// handler.sshLogin();
			// if(in_srcIP != null && !in_srcIP.isEmpty())
			// {
			// searchMsg = handler.sessionSearchF5V11CLI(searchKeyList);
			// }
			// else if(in_dstIP != null && !in_dstIP.isEmpty())
			// {
			// searchMsg = handler.sessionSearchF5V11CLI(searchKeyList);
			// }
			// else
			// {
			// handler.disconnect();
			// return retVal;
			// }
			// handler.disconnect();
			//
			// if(searchMsg.isEmpty())
			// return retVal;
			//
			// sessionList = new
			// OBCLIParserF5().parseSessionInfoListF5Version11(adcInfo.getIndex(),
			// searchMsg);
			// }
			// else
			// {
			// OBAdcF5Handler handler = new OBAdcF5Handler();
			// handler.setConnectionInfo(adcInfo.getAdcIpAddress(),
			// adcInfo.getAdcCliAccount(), adcInfo.getAdcCliPasswordDecrypt(),
			// adcInfo.getConnPort());
			// handler.sshLogin();
			// if(in_srcIP != null && !in_srcIP.isEmpty())
			// {
			// searchMsg = handler.sessionSearchF5CLI(searchKeyList);
			// }
			// else if(in_dstIP != null && !in_dstIP.isEmpty())
			// {
			// searchMsg = handler.sessionSearchF5CLI(searchKeyList);
			// }
			// else
			// {
			// handler.disconnect();
			// return retVal;
			// }
			// handler.disconnect();
			//
			// if(searchMsg.isEmpty())
			// return retVal;
			//
			// sessionList = new
			// OBCLIParserF5().parseSessionInfoListDefault(adcInfo.getIndex(), searchMsg);
			// }
			//
			retVal = new OBCLIParserF5().remanufactoringParsedSessionList(adcInfo.getIndex(), sessionList,
					searchKeyList);

		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return retVal;
	}

	private ArrayList<OBDtoFaultSessionInfo> searchSessionInfoListPAS(OBDtoAdcInfo adcInfo,
			ArrayList<OBDtoSessionSearchOption> searchKeyList) throws OBException {
		ArrayList<OBDtoFaultSessionInfo> retVal = new ArrayList<OBDtoFaultSessionInfo>();
		// TODO
		return retVal;
	}

	private ArrayList<OBDtoFaultSessionInfo> searchSessionInfoListPASK(OBDtoAdcInfo adcInfo,
			ArrayList<OBDtoSessionSearchOption> searchKeyList) throws OBException {
		ArrayList<OBDtoFaultSessionInfo> retVal = new ArrayList<OBDtoFaultSessionInfo>();
		// TODO
		return retVal;
	}

	// public static void main(String[] args) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// db.openDB();
	//
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// object.setIndex(3);
	//
	// // OBDtoSearch searchOption = new OBDtoSearch();
	//
	// OBDtoOrdering orderOption = new OBDtoOrdering();
	// orderOption.setOrderDirection(OBDtoOrdering.DIR_ASCEND);
	// orderOption.setOrderType(OBDtoOrdering.TYPE_2SECOND);
	//
	// ArrayList<OBDtoSessionSearchOption> searchKeyList = new
	// ArrayList<OBDtoSessionSearchOption>();
	// OBDtoSessionSearchOption keyword = new OBDtoSessionSearchOption();
	// keyword.setContent("192.168.201.14");
	// keyword.setType(OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP);
	// searchKeyList.add(keyword);
	//
	// // Integer total = new
	// OBFaultMonitoringImpl().getSvcPerfInfoTotalCount(object, searchOption);
	// // System.out.println(total);
	// ArrayList<OBDtoFaultSessionInfo> list= new
	// OBFaultMonitoringImpl().searchSessionInfoListBySort(object, null,
	// orderOption);
	// for(OBDtoFaultSessionInfo info:list)
	// System.out.println(info);
	// db.closeDB();
	// }

	@Override
	public ArrayList<OBDtoFaultSessionInfo> searchSessionInfoListBySort(OBDtoADCObject object, OBDtoSearch pagingOption,
			OBDtoOrdering orderOption) throws OBException {
		// 1 DB연다.
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoFaultSessionInfo> retVal = new ArrayList<OBDtoFaultSessionInfo>();
		try {
			db.openDB();
			retVal = getSessionInfoList(object, pagingOption, orderOption);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private String getSessionInfoListOrderType(OBDtoOrdering orderObj) throws OBException {
		if (orderObj == null)
			return "";
		String retVal = " ORDER BY INET(CLIENT_IP) ASC NULLS LAST, CLIENT_PORT ASC NULLS LAST ";
		int orderDir = orderObj.getOrderDirection();
		switch (orderObj.getOrderType()) {
		case OBDtoOrdering.TYPE_1FIRST:// source ip. SRC_IP
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(CLIENT_IP) ASC NULLS LAST, CLIENT_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY INET(CLIENT_IP) DESC NULLS LAST, CLIENT_PORT ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_2SECOND:// source port. SRC_PORT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CLIENT_PORT ASC NULLS LAST, INET(CLIENT_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY CLIENT_PORT DESC NULLS LAST, INET(CLIENT_IP) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_3THIRD:// destination ip. DST_IP
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(VIRTUAL_IP) ASC NULLS LAST, INET(CLIENT_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY INET(VIRTUAL_IP) DESC NULLS LAST, INET(CLIENT_IP) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_4FOURTH: // destination port. DST_PORT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VIRTUAL_PORT ASC NULLS LAST, INET(CLIENT_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY VIRTUAL_PORT DESC NULLS LAST, INET(CLIENT_IP) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_5FIFTH: // REAL_IP
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(REAL_IP) ASC NULLS LAST, INET(CLIENT_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY INET(REAL_IP) DESC NULLS LAST, INET(CLIENT_IP) ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	// public static void main(String[] args) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// db.openDB();
	//
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// object.setIndex(1);
	//
	// OBDtoSearch searchObj = new OBDtoSearch();
	// OBDtoOrdering orderObj = new OBDtoOrdering();
	//
	// ArrayList<OBDtoFaultSessionInfo>list= new
	// OBFaultMonitoringImpl().getSessionInfoList(object, searchObj, orderObj, db);
	// System.out.println(list);
	// db.closeDB();
	// }

	public ArrayList<OBDtoFaultSessionInfo> getSessionInfoList(OBDtoADCObject object, OBDtoSearch searchObj,
			OBDtoOrdering orderObj) throws OBException {
		ArrayList<OBDtoFaultSessionInfo> retVal = new ArrayList<OBDtoFaultSessionInfo>();
		String sqlText = "";

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// adcIndex 추출.
			ArrayList<Integer> adcIndexList = new ArrayList<Integer>();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ADC) {
				adcIndexList.add(object.getIndex());
			} else {
				if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
					adcIndexList = new OBFaultMngImpl().getAdcListAll(0);
				} else {
					adcIndexList = new OBFaultMngImpl().getAdcList(object.getIndex());
				}
			}
			String adcIndexListText = "-1";
			for (Integer adcIndex : adcIndexList) {
				adcIndexListText += ", " + adcIndex;
			}
			int offset = 0;
			if (searchObj != null && searchObj.getBeginIndex() != null)
				offset = searchObj.getBeginIndex().intValue();
			int limit = 0;
			if (searchObj != null && searchObj.getEndIndex() != null)
				limit = Math.abs(searchObj.getEndIndex().intValue() - offset) + 1;
			String sqlLimit = "";
			if (limit != 0)
				sqlLimit = String.format(" LIMIT %d ", limit);
			String sqlOffset = String.format(" OFFSET %d ", offset);

			sqlText = String.format(
					" SELECT ADC_INDEX, CLIENT_IP, VIRTUAL_IP, REAL_IP,                        \n"
							+ " CLIENT_PORT, VIRTUAL_PORT, REAL_PORT, DAM_PORT, PROTOCOL, AGE_TIME, SP_NO \n"
							+ " FROM TMP_SESSION_INFO                                                    \n"
							+ " WHERE ADC_INDEX IN ( %s )                                                \n",
					adcIndexListText);

			sqlText += getSessionInfoListOrderType(orderObj);

			if (sqlLimit != null)
				sqlText += sqlLimit;
			if (sqlOffset != null)
				sqlText += sqlOffset;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {

				OBDtoFaultSessionInfo obj = new OBDtoFaultSessionInfo();
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setSrcIP(db.getString(rs, "CLIENT_IP"));
				obj.setDstIP(db.getString(rs, "VIRTUAL_IP"));
				obj.setSrcPort(db.getString(rs, "CLIENT_PORT"));
				obj.setDstPort(db.getString(rs, "VIRTUAL_PORT"));
				String damPort = db.getString(rs, "DAM_PORT");
				if (damPort != null && !damPort.isEmpty()) {
					String srcPort = obj.getSrcPort() + "->" + damPort;
					obj.setSrcPort(srcPort);
				}
				obj.setProtocol(db.getInteger(rs, "PROTOCOL"));
				obj.setAgingTime(db.getInteger(rs, "AGE_TIME"));
				obj.setSpNumber(db.getString(rs, "SP_NO"));
				obj.setRealIP(db.getString(rs, "REAL_IP"));
				obj.setRealPort(db.getString(rs, "REAL_PORT"));
				retVal.add(obj);
			}

			return retVal;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public Integer getSessionListCount(OBDtoADCObject object, OBDtoSearch pagingOption) throws OBException {
		int result = 0;
		String sqlText = "";

		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			// adcIndex 추출.
			ArrayList<Integer> adcIndexList = new ArrayList<Integer>();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ADC) {
				adcIndexList.add(object.getIndex());
			} else {
				if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
					adcIndexList = new OBFaultMngImpl().getAdcListAll(0);
				} else {
					adcIndexList = new OBFaultMngImpl().getAdcList(object.getIndex());
				}
			}
			String adcIndexListText = "-1";
			for (Integer adcIndex : adcIndexList) {
				adcIndexListText += ", " + adcIndex;
			}

			sqlText = String.format(" SELECT "
					+ "COUNT(ADC_INDEX) AS CNT                                                                        \n"
					+ " FROM TMP_SESSION_INFO                                                                          \n"
					+ " WHERE ADC_INDEX IN ( %s )                                                                      \n",
					adcIndexListText);

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				result = db.getInteger(rs, "CNT");
			}

		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

	// public static void main(String[] args) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// db.openDB();
	//
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// object.setIndex(3);
	//
	// OBDtoFaultBpsConnInfo prev = null;
	// OBDtoFaultBpsConnInfo list= new
	// OBFaultMonitoringImpl().getRealTimeBpsConnInfo(object, "3_MONITOR1", 80,
	// prev);
	// System.out.println(list);
	// db.closeDB();
	// }

	@Override
	public OBDtoFaultBpsConnInfo getRealTimeBpsConnInfo(OBDtoADCObject object, String vsIndex, Integer svcPort,
			OBDtoFaultBpsConnInfo prevInfo) throws OBException {
		OBDtoFaultBpsConnInfo retVal = null;
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());// .getAdcType(adcIndex, db);
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				String vsID = new OBVServerDB().getVServerID(vsIndex);
				if (vsID == null)
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , String.format("invalid
																					// vsIndex(%s). failed to found the
																					// vs id.", vsIndex));
				retVal = getRealTimeBpsConnInfoAlteon(adcInfo, vsID, svcPort, prevInfo);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				String vsName = new OBVServerDB().getVServerName(vsIndex);
				if (vsName == null)
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , String.format("invalid
																					// vsIndex(%s). failed to found the
																					// vs name.", vsIndex));
				retVal = getRealTimeBpsConnInfoF5(adcInfo, vsName, prevInfo);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				String vsName = new OBVServerDB().getVServerName(vsIndex);
				if (vsName == null)
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , String.format("invalid
																					// vsIndex(%s). failed to found the
																					// vs name.", vsIndex));
				retVal = getRealTimeBpsConnInfoPAS(adcInfo, vsName, prevInfo);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				String vsName = new OBVServerDB().getVServerName(vsIndex);
				if (vsName == null)
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// , String.format("invalid
																					// vsIndex(%s). failed to found the
																					// vs name.", vsIndex));
				retVal = getRealTimeBpsConnInfoPASK(adcInfo, vsName, prevInfo);
			} else {
				throw new OBException(OBException.ERRCODE_NOT_SUPPORT_VENDOR);// , String.format("invalid vendor
																				// type:%d", adcInfo.getAdcType()));
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return retVal;
	}

	private OBDtoFaultBpsConnInfo getRealTimeBpsConnInfoF5(OBDtoAdcInfo adcInfo, String vsName,
			OBDtoFaultBpsConnInfo prevInfo) throws OBException {
		OBDtoFaultBpsConnInfo retVal = new OBDtoFaultBpsConnInfo();

		try {
			Date now = new Date();
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());
			OBDtoConnectionData connInfo = snmp.getVServerConns(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsName);
			OBDtoMonTrafficVServer vsTrafficInfo = snmp.getTrafficVServerInfo(adcInfo.getAdcType(),
					adcInfo.getSwVersion(), vsName);

			OBDtoDataObj inBpsValue = new OBDtoDataObj();
			inBpsValue.setValue(vsTrafficInfo.getBytesIn());
			inBpsValue.setOccurTime(now);
			OBDtoDataObj outBpsValue = new OBDtoDataObj();
			outBpsValue.setValue(vsTrafficInfo.getBytesOut());
			outBpsValue.setOccurTime(now);
			OBDtoDataObj totalBpsValue = new OBDtoDataObj();
			totalBpsValue.setValue(vsTrafficInfo.getBytesIn() + vsTrafficInfo.getBytesOut());
			totalBpsValue.setOccurTime(now);

			OBDtoDataObj totalPpsValue = new OBDtoDataObj();
			totalPpsValue.setValue(vsTrafficInfo.getPktsIn() + vsTrafficInfo.getPktsOut());
			totalPpsValue.setOccurTime(now);
			OBDtoDataObj totalConnCurrValue = new OBDtoDataObj();
			totalConnCurrValue.setOccurTime(now);
			totalConnCurrValue.setValue(connInfo.getCurConns());
			OBDtoDataObj totalConnMaxValue = new OBDtoDataObj();
			totalConnMaxValue.setOccurTime(now);
			totalConnMaxValue.setValue(connInfo.getMaxConns());
			OBDtoDataObj totalConnTotValue = new OBDtoDataObj();
			totalConnTotValue.setOccurTime(now);
			totalConnTotValue.setValue(connInfo.getTotConns());

			OBDtoDataObj bpsInValue = new OBDtoDataObj();
			bpsInValue.setValue(0L);
			bpsInValue.setOccurTime(now);
			OBDtoDataObj bpsOutValue = new OBDtoDataObj();
			bpsOutValue.setValue(0L);
			bpsOutValue.setOccurTime(now);
			OBDtoDataObj bpsTotValue = new OBDtoDataObj();
			bpsTotValue.setValue(0L);
			bpsTotValue.setOccurTime(now);

			OBDtoDataObj ppsValue = new OBDtoDataObj();
			ppsValue.setOccurTime(now);
			ppsValue.setValue(0L);
			OBDtoDataObj connCurrValue = new OBDtoDataObj();
			connCurrValue.setOccurTime(now);
			connCurrValue.setValue(0L);
			OBDtoDataObj connMaxValue = new OBDtoDataObj();
			connMaxValue.setOccurTime(now);
			connMaxValue.setValue(0L);
			OBDtoDataObj connTotValue = new OBDtoDataObj();
			connTotValue.setOccurTime(now);
			connTotValue.setValue(0L);
			if (prevInfo != null && prevInfo.getTotalBpsRawData() != null
					&& prevInfo.getTotalBpsRawData().getOccurTime() != null) {
				long timeDiff = Math.abs(now.getTime() - prevInfo.getTotalBpsRawData().getOccurTime().getTime());
				long diffValue;
				long value = 0L;
				// BpsTotalValue 계산
				diffValue = totalBpsValue.getValue() - prevInfo.getTotalBpsRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 8 * 1000 / timeDiff);
				}
				bpsTotValue.setValue(value);

				// BpsInValue 계산
				diffValue = inBpsValue.getValue() - prevInfo.getInBpsRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 * 8 / timeDiff);
				}
				bpsInValue.setValue(value);

				// BpsOutValue 계산
				diffValue = outBpsValue.getValue() - prevInfo.getOutBpsRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 * 8 / timeDiff);
				}
				bpsOutValue.setValue(value);

				// ppsTotalValue 계산
				diffValue = totalPpsValue.getValue() - prevInfo.getTotalPpsRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 / timeDiff);
				}
				ppsValue.setValue(value);

				// value = Math.abs(totalConnCurrValue.getValue() -
				// prevInfo.getTotalConnCurrValue().getValue())*1000/timeDiff;
				connCurrValue.setValue(totalConnCurrValue.getValue());
				// value = Math.abs(totalConnMaxValue.getValue() -
				// prevInfo.getTotalConnMaxValue().getValue())*1000/timeDiff;
				connMaxValue.setValue(totalConnMaxValue.getValue());
				// value = Math.abs(totalConnTotValue.getValue() -
				// prevInfo.getTotalConnTotValue().getValue())*1000/timeDiff;
				connTotValue.setValue(totalConnTotValue.getValue());
			}

			retVal.setBpsInValue(bpsInValue);
			retVal.setBpsOutValue(bpsOutValue);
			retVal.setBpsTotValue(bpsTotValue);
			retVal.setConnCurrValue(connCurrValue);
			retVal.setConnMaxValue(connMaxValue);
			retVal.setConnTotValue(connTotValue);
			retVal.setPpsValue(ppsValue);

			retVal.setInBpsRawData(inBpsValue);
			retVal.setOutBpsRawData(outBpsValue);
			retVal.setTotalBpsRawData(totalBpsValue);
			retVal.setTotalConnCurrRawData(totalConnCurrValue);
			retVal.setTotalConnMaxRawData(totalConnMaxValue);
			retVal.setTotalConnTotRawData(totalConnTotValue);
			retVal.setTotalPpsRawData(totalPpsValue);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}

		return retVal;
	}

	private OBDtoFaultBpsConnInfo getRealTimeBpsConnInfoPAS(OBDtoAdcInfo adcInfo, String vsName,
			OBDtoFaultBpsConnInfo prevInfo) throws OBException {// alteon인 경우에는 vsIndex는 virtual service의 인덱스..
		OBDtoFaultBpsConnInfo retVal = new OBDtoFaultBpsConnInfo();

		try {
			Date now = new Date();
			OBSnmpPAS snmp = new OBSnmpPAS(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
			OBDtoConnectionData connInfo = snmp.getVServerConns(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsName);
			OBDtoMonTrafficVServer vsTrafficInfo = snmp.getTrafficVServerInfo(adcInfo.getAdcType(),
					adcInfo.getSwVersion(), vsName);

			OBDtoDataObj totalBpsValue = new OBDtoDataObj();
			totalBpsValue.setValue(vsTrafficInfo.getBytesIn() + vsTrafficInfo.getBytesOut());
			totalBpsValue.setOccurTime(now);
			OBDtoDataObj totalPpsValue = new OBDtoDataObj();
			totalPpsValue.setValue(vsTrafficInfo.getPktsIn() + vsTrafficInfo.getPktsOut());
			totalPpsValue.setOccurTime(now);
			OBDtoDataObj totalConnCurrValue = new OBDtoDataObj();
			totalConnCurrValue.setOccurTime(now);
			totalConnCurrValue.setValue(connInfo.getCurConns());
			OBDtoDataObj totalConnMaxValue = new OBDtoDataObj();
			totalConnMaxValue.setOccurTime(now);
			totalConnMaxValue.setValue(connInfo.getMaxConns());
			OBDtoDataObj totalConnTotValue = new OBDtoDataObj();
			totalConnTotValue.setOccurTime(now);
			totalConnTotValue.setValue(connInfo.getTotConns());

			OBDtoDataObj bpsValue = new OBDtoDataObj();
			bpsValue.setValue(0L);
			bpsValue.setOccurTime(now);
			OBDtoDataObj ppsValue = new OBDtoDataObj();
			ppsValue.setOccurTime(now);
			ppsValue.setValue(0L);
			OBDtoDataObj connCurrValue = new OBDtoDataObj();
			connCurrValue.setOccurTime(now);
			connCurrValue.setValue(0L);
			OBDtoDataObj connMaxValue = new OBDtoDataObj();
			connMaxValue.setOccurTime(now);
			connMaxValue.setValue(0L);
			OBDtoDataObj connTotValue = new OBDtoDataObj();
			connTotValue.setOccurTime(now);
			connTotValue.setValue(0L);
			if (prevInfo != null && prevInfo.getTotalBpsRawData() != null
					&& prevInfo.getTotalBpsRawData().getOccurTime() != null) {
				long timeDiff = Math.abs(now.getTime() - prevInfo.getTotalBpsRawData().getOccurTime().getTime());
				long diffValue;
				long value = 0L;

				diffValue = totalBpsValue.getValue() - prevInfo.getTotalBpsRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 * 8 / timeDiff);
				}
				bpsValue.setValue(value);

				diffValue = totalPpsValue.getValue() - prevInfo.getTotalPpsRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 / timeDiff);
				}
				ppsValue.setValue(value);

				diffValue = totalConnCurrValue.getValue() - prevInfo.getTotalConnCurrRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 / timeDiff);
				}
				connCurrValue.setValue(value);

				diffValue = totalConnMaxValue.getValue() - prevInfo.getTotalConnMaxRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 / timeDiff);
				}
				connMaxValue.setValue(value);

				diffValue = totalConnTotValue.getValue() - prevInfo.getTotalConnTotRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 / timeDiff);
				}
				connTotValue.setValue(value);
			}

			retVal.setBpsValue(bpsValue);
			retVal.setConnCurrValue(connCurrValue);
			retVal.setConnMaxValue(connMaxValue);
			retVal.setConnTotValue(connTotValue);
			retVal.setPpsValue(ppsValue);
			retVal.setTotalBpsRawData(totalBpsValue);
			retVal.setTotalConnCurrRawData(totalConnCurrValue);
			retVal.setTotalConnMaxRawData(totalConnMaxValue);
			retVal.setTotalConnTotRawData(totalConnTotValue);
			retVal.setTotalPpsRawData(totalPpsValue);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}

		return retVal;
	}

	private OBDtoFaultBpsConnInfo getRealTimeBpsConnInfoPASK(OBDtoAdcInfo adcInfo, String vsName,
			OBDtoFaultBpsConnInfo prevInfo) throws OBException {
		OBDtoFaultBpsConnInfo retVal = new OBDtoFaultBpsConnInfo();

		try {
			Date now = new Date();
			OBSnmpPASK snmp = new OBSnmpPASK(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());// OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(),
																								// adcInfo.getAdcIpAddress());
			OBDtoConnectionData connInfo = snmp.getVServerConns(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsName);
			OBDtoMonTrafficVServer vsTrafficInfo = snmp.getTrafficVServerInfo(adcInfo.getAdcType(),
					adcInfo.getSwVersion(), vsName);

			OBDtoDataObj totalBpsValue = new OBDtoDataObj();
			totalBpsValue.setValue(vsTrafficInfo.getBytesIn() + vsTrafficInfo.getBytesOut());
			totalBpsValue.setOccurTime(now);
			OBDtoDataObj totalPpsValue = new OBDtoDataObj();
			totalPpsValue.setValue(vsTrafficInfo.getPktsIn() + vsTrafficInfo.getPktsOut());
			totalPpsValue.setOccurTime(now);
			OBDtoDataObj totalConnCurrValue = new OBDtoDataObj();
			totalConnCurrValue.setOccurTime(now);
			totalConnCurrValue.setValue(connInfo.getCurConns());
			OBDtoDataObj totalConnMaxValue = new OBDtoDataObj();
			totalConnMaxValue.setOccurTime(now);
			totalConnMaxValue.setValue(connInfo.getMaxConns());
			OBDtoDataObj totalConnTotValue = new OBDtoDataObj();
			totalConnTotValue.setOccurTime(now);
			totalConnTotValue.setValue(connInfo.getTotConns());

			OBDtoDataObj bpsValue = new OBDtoDataObj();
			bpsValue.setValue(0L);
			bpsValue.setOccurTime(now);
			OBDtoDataObj ppsValue = new OBDtoDataObj();
			ppsValue.setOccurTime(now);
			ppsValue.setValue(0L);
			OBDtoDataObj connCurrValue = new OBDtoDataObj();
			connCurrValue.setOccurTime(now);
			connCurrValue.setValue(0L);
			OBDtoDataObj connMaxValue = new OBDtoDataObj();
			connMaxValue.setOccurTime(now);
			connMaxValue.setValue(0L);
			OBDtoDataObj connTotValue = new OBDtoDataObj();
			connTotValue.setOccurTime(now);
			connTotValue.setValue(0L);
			if (prevInfo != null && prevInfo.getTotalBpsRawData() != null
					&& prevInfo.getTotalBpsRawData().getOccurTime() != null) {
				long timeDiff = Math.abs(now.getTime() - prevInfo.getTotalBpsRawData().getOccurTime().getTime());
				long diffValue;
				long value = 0L;

				diffValue = totalBpsValue.getValue() - prevInfo.getTotalBpsRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 * 8 / timeDiff);
				}
				bpsValue.setValue(value);

				diffValue = totalPpsValue.getValue() - prevInfo.getTotalPpsRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 / timeDiff);
				}
				ppsValue.setValue(value);

				diffValue = totalConnCurrValue.getValue() - prevInfo.getTotalConnCurrRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 / timeDiff);
				}
				connCurrValue.setValue(value);

				diffValue = totalConnMaxValue.getValue() - prevInfo.getTotalConnMaxRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 / timeDiff);
				}
				connMaxValue.setValue(value);

				diffValue = totalConnTotValue.getValue() - prevInfo.getTotalConnTotRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 / timeDiff);
				}
				connTotValue.setValue(value);
			}

			retVal.setBpsValue(bpsValue);
			retVal.setConnCurrValue(connCurrValue);
			retVal.setConnMaxValue(connMaxValue);
			retVal.setConnTotValue(connTotValue);
			retVal.setPpsValue(ppsValue);
			retVal.setTotalBpsRawData(totalBpsValue);
			retVal.setTotalConnCurrRawData(totalConnCurrValue);
			retVal.setTotalConnMaxRawData(totalConnMaxValue);
			retVal.setTotalConnTotRawData(totalConnTotValue);
			retVal.setTotalPpsRawData(totalPpsValue);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}

		return retVal;
	}

	// public static void main(String[] args)
	// {
	// OBMonitoringImpl mon = new OBMonitoringImpl();
	//
	// try
	// {
	// for(int i=0;i<100;i++)
	// {
	// OBDtoConnectionInfo info = mon.getVSRealTimeCurrConnsAlteon(3, 122, 23);
	// System.out.println(info);
	// OBDateTime.Sleep(5000);
	// }
	// }
	// catch(OBException e)
	// {
	// e.printStackTrace();
	// }
	// }

	private OBDtoFaultBpsConnInfo getRealTimeBpsConnInfoAlteon(OBDtoAdcInfo adcInfo, String vsID, Integer virtPort,
			OBDtoFaultBpsConnInfo prevInfo) throws OBException {// alteon인 경우에는 vsIndex는 virtual service의 인덱스..
		OBDtoFaultBpsConnInfo retVal = new OBDtoFaultBpsConnInfo();

		try {
			// virtual server, poolmember로 구성한다.
			Date now = new Date();
			OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());
			Integer svcIndex = snmp.getVirtServiceIndex(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsID, virtPort);
			OBDtoConnectionData connInfo = snmp.getVServiceConns(adcInfo.getAdcType(), adcInfo.getSwVersion(), vsID,
					virtPort, svcIndex);
			OBDtoMonTrafficVServer vsTrafficInfo = snmp.getTrafficVServerInfo(adcInfo.getAdcType(),
					adcInfo.getSwVersion(), vsID, svcIndex);

			OBDtoDataObj inBpsValue = new OBDtoDataObj();
			inBpsValue.setValue(vsTrafficInfo.getBytesIn());
			inBpsValue.setOccurTime(now);
			OBDtoDataObj outBpsValue = new OBDtoDataObj();
			outBpsValue.setValue(vsTrafficInfo.getBytesOut());
			outBpsValue.setOccurTime(now);
			OBDtoDataObj totalBpsValue = new OBDtoDataObj();
			totalBpsValue.setValue(vsTrafficInfo.getBytesIn() + vsTrafficInfo.getBytesOut());
			totalBpsValue.setOccurTime(now);

			OBDtoDataObj totalPpsValue = new OBDtoDataObj();
			totalPpsValue.setValue(vsTrafficInfo.getPktsIn() + vsTrafficInfo.getPktsOut());
			totalPpsValue.setOccurTime(now);
			OBDtoDataObj totalConnCurrValue = new OBDtoDataObj();
			totalConnCurrValue.setOccurTime(now);
			totalConnCurrValue.setValue(connInfo.getCurConns());
			OBDtoDataObj totalConnMaxValue = new OBDtoDataObj();
			totalConnMaxValue.setOccurTime(now);
			totalConnMaxValue.setValue(connInfo.getMaxConns());
			OBDtoDataObj totalConnTotValue = new OBDtoDataObj();
			totalConnTotValue.setOccurTime(now);
			totalConnTotValue.setValue(connInfo.getTotConns());

			OBDtoDataObj bpsInValue = new OBDtoDataObj();
			bpsInValue.setValue(0L);
			bpsInValue.setOccurTime(now);
			OBDtoDataObj bpsOutValue = new OBDtoDataObj();
			bpsOutValue.setValue(0L);
			bpsOutValue.setOccurTime(now);
			OBDtoDataObj bpsTotValue = new OBDtoDataObj();
			bpsTotValue.setValue(0L);
			bpsTotValue.setOccurTime(now);

			OBDtoDataObj ppsValue = new OBDtoDataObj();
			ppsValue.setOccurTime(now);
			ppsValue.setValue(0L);
			OBDtoDataObj connCurrValue = new OBDtoDataObj();
			connCurrValue.setOccurTime(now);
			connCurrValue.setValue(connInfo.getCurConns());
			OBDtoDataObj connMaxValue = new OBDtoDataObj();
			connMaxValue.setOccurTime(now);
			connMaxValue.setValue(connInfo.getMaxConns());
			OBDtoDataObj connTotValue = new OBDtoDataObj();
			connTotValue.setOccurTime(now);
			connTotValue.setValue(connInfo.getTotConns());
			if (prevInfo != null && prevInfo.getTotalBpsRawData() != null
					&& prevInfo.getTotalBpsRawData().getOccurTime() != null) {
				long timeDiff = Math.abs(now.getTime() - prevInfo.getTotalBpsRawData().getOccurTime().getTime());
				long diffValue;
				long value = 0L;
				// BpsTotalValue 계산
				diffValue = totalBpsValue.getValue() - prevInfo.getTotalBpsRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 * 8 / timeDiff);
				}
				bpsTotValue.setValue(value);

				// BpsInValue 계산
				diffValue = inBpsValue.getValue() - prevInfo.getInBpsRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 * 8 / timeDiff);
				}
				bpsInValue.setValue(value);

				// BpsOutValue 계산
				diffValue = outBpsValue.getValue() - prevInfo.getOutBpsRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 * 8 / timeDiff);
				}
				bpsOutValue.setValue(value);

				// ppsTotalValue 계산
				diffValue = totalPpsValue.getValue() - prevInfo.getTotalPpsRawData().getValue();
				if (diffValue < 0) {
					value = -1;
				} else {
					value = (long) ((float) diffValue * 1000 / timeDiff);
				}
				ppsValue.setValue(value);

				// value = Math.abs(totalConnCurrValue.getValue() -
				// prevInfo.getTotalConnCurrValue().getValue())*1000/timeDiff;
				// connCurrValue.setValue(totalConnCurrValue.getValue());
				// value = Math.abs(totalConnMaxValue.getValue() -
				// prevInfo.getTotalConnMaxValue().getValue())*1000/timeDiff;
				// connMaxValue.setValue(totalConnMaxValue.getValue());
				// value = Math.abs(totalConnTotValue.getValue() -
				// prevInfo.getTotalConnTotValue().getValue())*1000/timeDiff;
				// connTotValue.setValue(totalConnTotValue.getValue());
			}

			retVal.setBpsInValue(bpsInValue);
			retVal.setBpsOutValue(bpsOutValue);
			retVal.setBpsTotValue(bpsTotValue);
			retVal.setConnCurrValue(connCurrValue);
			retVal.setConnMaxValue(connMaxValue);
			retVal.setConnTotValue(connTotValue);
			retVal.setPpsValue(ppsValue);

			retVal.setInBpsRawData(inBpsValue);
			retVal.setOutBpsRawData(outBpsValue);
			retVal.setTotalBpsRawData(totalBpsValue);
			retVal.setTotalConnCurrRawData(totalConnCurrValue);
			retVal.setTotalConnMaxRawData(totalConnMaxValue);
			retVal.setTotalConnTotRawData(totalConnTotValue);
			retVal.setTotalPpsRawData(totalPpsValue);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}

		return retVal;
	}

	// public static void main(String[] args) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// db.openDB();
	//
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// object.setIndex(1);
	//
	// OBDtoSearch searchOption = new OBDtoSearch();
	//
	// OBDtoOrdering orderOption = new OBDtoOrdering();
	// orderOption.setOrderDirection(OBDtoOrdering.DIR_ASCEND);
	// orderOption.setOrderType(OBDtoOrdering.TYPE_2SECOND);
	//
	// Integer total = new OBFaultMonitoringImpl().getSvcPerfInfoTotalCount(object,
	// searchOption);
	// System.out.println(total);
	// ArrayList<OBDtoFaultSvcPerfInfo> list= new
	// OBFaultMonitoringImpl().getSvcPerfInfoList(object, searchOption,
	// orderOption);
	// for(OBDtoFaultSvcPerfInfo obj:list)
	// System.out.println(obj);
	// db.closeDB();
	// }

	@Override
	public ArrayList<OBDtoFaultSvcPerfInfo> getSvcPerfInfoList(OBDtoADCObject object, OBDtoSearch searchOption,
			OBDtoOrdering orderOption, Integer accountIndex, String accountRole) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoFaultSvcPerfInfo> retVal = new ArrayList<OBDtoFaultSvcPerfInfo>();
		try {
			db.openDB();
			retVal = getSvcPerfInfoList(object, searchOption, orderOption, accountIndex, accountRole, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private ArrayList<OBDtoFaultSvcPerfInfo> getSvcPerfInfoList(OBDtoADCObject object, OBDtoSearch searchOption,
			OBDtoOrdering orderOption, Integer accountIndex, String accountRole, OBDatabase db) throws OBException {
		try {
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				return getSvcPerfInfoListAll(object, searchOption, orderOption, db);
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				return getSvcPerfInfoListGroup(object, searchOption, orderOption, db);
			} else {
				return getSvcPerfInfoListAdc(object, searchOption, orderOption, accountIndex, accountRole, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
	}

	private ArrayList<OBDtoFaultSvcPerfInfo> getSvcPerfInfoListAdc(OBDtoADCObject object, OBDtoSearch searchOption,
			OBDtoOrdering orderOption, Integer accountIndex, String accountRole, OBDatabase db) throws OBException {
		ArrayList<OBDtoFaultSvcPerfInfo> retVal = new ArrayList<OBDtoFaultSvcPerfInfo>();
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());
			if (adcInfo == null)
				return retVal;

			if (adcInfo.getAdcType() == OBDtoAdcInfo.ADC_TYPE_ALTEON) {
				return getSvcPerfInfoListAdcAlteon(object, searchOption, orderOption, accountIndex, accountRole, db);
			} else if (adcInfo.getAdcType() == OBDtoAdcInfo.ADC_TYPE_F5
					|| adcInfo.getAdcType() == OBDtoAdcInfo.ADC_TYPE_PIOLINK_PAS
					|| adcInfo.getAdcType() == OBDtoAdcInfo.ADC_TYPE_PIOLINK_PASK) {
				return getSvcPerfInfoListAdcOthers(object, searchOption, orderOption, accountIndex, accountRole, db);
			}

		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return retVal;
	}

	private ArrayList<OBDtoFaultSvcPerfInfo> getSvcPerfInfoListAll(OBDtoADCObject object, OBDtoSearch searchOption,
			OBDtoOrdering orderOption, OBDatabase db) throws OBException {
		// TODO
		return null;
	}

	private ArrayList<OBDtoFaultSvcPerfInfo> getSvcPerfInfoListGroup(OBDtoADCObject object, OBDtoSearch searchOption,
			OBDtoOrdering orderOption, OBDatabase db) throws OBException {
		// TODO
		return null;
	}

	private ArrayList<OBDtoFaultSvcPerfInfo> getSvcPerfInfoListAdcAlteon(OBDtoADCObject object,
			OBDtoSearch searchOption, OBDtoOrdering orderOption, Integer accountIndex, String accountRole,
			OBDatabase db) throws OBException {
		ArrayList<OBDtoFaultSvcPerfInfo> retVal = new ArrayList<OBDtoFaultSvcPerfInfo>();
		String sqlText = "";
		String sqlSearch = "";

		try {
			int offset = 0;
			if (searchOption != null && searchOption.getBeginIndex() != null)
				offset = searchOption.getBeginIndex().intValue();

			int limit = 0;
			String sqlLimit = "";

			if (searchOption != null && searchOption.getEndIndex() != null) {
				limit = Math.abs(searchOption.getEndIndex().intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			if (searchOption != null && searchOption.getSearchKey() != null && !searchOption.getSearchKey().isEmpty()) {
				String wildcardKey = "%" + searchOption.getSearchKey() + "%";
				sqlSearch = String.format(" (NAME ILIKE %s OR VIRTUAL_IP LIKE %s) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			} else {
				sqlSearch = " TRUE ";
			}

			int rescInterval = new OBEnvManagementImpl().getAdcSyncInterval(db);// 초 단위.

			if (accountRole.equals("vsAdmin")) {
				if (sqlSearch.length() > 0) {
					sqlSearch += " AND ";
				}
				sqlSearch += " A.VS_INDEX IN (SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ADC_INDEX = "
						+ object.getIndex() + " AND ACCNT_INDEX = " + accountIndex + ") ";
			}

			sqlText = String.format(
					" SELECT COALESCE(B.BPS_IN,-1) AS BPS_IN, COALESCE(B.BPS_OUT,-1) AS BPS_OUT, COALESCE(B.BPS_TOT,-1) AS BPS_TOT,   \n"
							+ " COALESCE(B.CONN_CURR,-1) AS CONN_CURR, COALESCE(B.CONN_MAX,-1) AS CONN_MAX, COALESCE(B.CONN_TOT,-1) AS CONN_TOT,\n"
							+ " COALESCE(D.RESPONSE_TIME,-1) AS RESPONSE_TIME, B.OCCUR_TIME,                                                                \n"
							+ " COALESCE(B.PPS_IN,-1) AS PPS_IN, COALESCE(B.PPS_OUT,-1) AS PPS_OUT, COALESCE(B.PPS_TOT,-1) AS PPS_TOT,          \n"
							+ " C.INDEX AS VS_INDEX, A.VIRTUAL_PORT AS SERVICE_PORT, C.VIRTUAL_IP AS VS_IPADDRESS, C.NAME AS VS_NAME,           \n"
							+ " A.STATUS AS STATUS, A.INDEX AS VSVC_INDEX                                                                       \n"
							+ " FROM TMP_SLB_VS_SERVICE            A                                                                            \n"
							+ " LEFT JOIN TMP_FAULT_SVC_PERF_STATS B                                                                            \n"
							+ " ON B.OBJ_INDEX = A.INDEX                                                                                        \n"
							+ " INNER JOIN TMP_SLB_VSERVER         C                                                                            \n"
							+ " ON C.INDEX = A.VS_INDEX                                                                                         \n"
							+ " LEFT JOIN (SELECT RESPONSE_TIME, OBJ_INDEX FROM TMP_FAULT_SVC_PERF_RESP_TIME 									  \n"
							+ " WHERE OCCUR_TIME > NOW() - INTERVAL '%d SECONDS') D  															  \n"
							+ " ON B.OBJ_INDEX = D.OBJ_INDEX                                                                                    \n"
							+ " WHERE A.ADC_INDEX = %d                                                                                          \n",
					rescInterval * 2, object.getIndex()); // 모니터링
			// 주기의
			// 2배보다
			// 시간이
			// 오래된
			// 것은
			// 보여주지
			// 않음.

			if (!sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += getSvcPerfInfoListOrderTypeAlteon(orderOption);

			sqlText += sqlLimit;

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoFaultSvcPerfInfo log = new OBDtoFaultSvcPerfInfo();

				log.setAdcObj(object);
				log.setVsIndex(db.getString(rs, "VS_INDEX"));
				log.setVsvcIndex(db.getString(rs, "VSVC_INDEX"));
				log.setVsStatus(db.getInteger(rs, "STATUS"));
				log.setVsIP(db.getString(rs, "VS_IPADDRESS"));
				log.setVsPort(db.getString(rs, "SERVICE_PORT"));
				log.setVsName(db.getString(rs, "VS_NAME"));

				String occur = db.getString(rs, "OCCUR_TIME");
				if (occur != null && OBDateTime.getTimeIntervalCheck(occur, rescInterval)) {
					log.setResponseTime(db.getInteger(rs, "RESPONSE_TIME"));
					log.setBpsIn(db.getLong(rs, "BPS_IN"));
					log.setBpsOut(db.getLong(rs, "BPS_OUT"));
					log.setBpsTotal(db.getLong(rs, "BPS_TOT"));
					log.setPpsIn(db.getLong(rs, "PPS_IN"));
					log.setPpsOut(db.getLong(rs, "PPS_OUT"));
					log.setPpsTotal(db.getLong(rs, "PPS_TOT"));
					log.setConnCurr(db.getLong(rs, "CONN_CURR"));
					log.setConnMax(db.getLong(rs, "CONN_MAX"));
					log.setConnTotal(db.getLong(rs, "CONN_TOT"));
				} else {
					log.setResponseTime(-1);
					log.setBpsIn(-1l);
					log.setBpsOut(-1l);
					log.setBpsTotal(-1l);
					log.setPpsIn(-1l);
					log.setPpsOut(-1l);
					log.setPpsTotal(-1l);
					log.setConnCurr(-1l);
					log.setConnMax(-1l);
					log.setConnTotal(-1l);
				}

				retVal.add(log);
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return retVal;
	}

	private ArrayList<OBDtoFaultSvcPerfInfo> getSvcPerfInfoListAdcOthers(OBDtoADCObject object,
			OBDtoSearch searchOption, OBDtoOrdering orderOption, Integer accountIndex, String accountRole,
			OBDatabase db) throws OBException {
		ArrayList<OBDtoFaultSvcPerfInfo> retVal = new ArrayList<OBDtoFaultSvcPerfInfo>();
		String sqlText = "";
		String sqlSearch = "";

		try {
			int offset = 0;
			if (searchOption != null && searchOption.getBeginIndex() != null)
				offset = searchOption.getBeginIndex().intValue();

			int limit = 0;
			String sqlLimit = "";

			if (searchOption != null && searchOption.getEndIndex() != null) {
				limit = Math.abs(searchOption.getEndIndex().intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}

			if (searchOption != null && searchOption.getSearchKey() != null && !searchOption.getSearchKey().isEmpty()) {
				String wildcardKey = "%" + searchOption.getSearchKey() + "%";
				sqlSearch = String.format(" (NAME ILIKE %s OR VIRTUAL_IP LIKE %s) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			} else {
				sqlSearch = " TRUE ";
			}

			int rescInterval = new OBEnvManagementImpl().getAdcSyncInterval(db);// 초 단위.

			if (accountRole.equals("vsAdmin")) {
				if (sqlSearch.length() > 0) {
					sqlSearch += " AND ";
				}
				sqlSearch += " A.INDEX IN (SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ADC_INDEX = " + object.getIndex()
						+ " AND ACCNT_INDEX = " + accountIndex + ") ";
			}

			sqlText = String.format(
					" SELECT COALESCE(B.BPS_IN,-1) AS BPS_IN, COALESCE(B.BPS_OUT,-1) AS BPS_OUT, COALESCE(B.BPS_TOT,-1) AS BPS_TOT,    \n"
							+ " COALESCE(B.CONN_CURR,-1) AS CONN_CURR, COALESCE(B.CONN_MAX,-1) AS CONN_MAX, COALESCE(B.CONN_TOT,-1) AS CONN_TOT, \n"
							+ " COALESCE(D.RESPONSE_TIME,-1) AS RESPONSE_TIME, B.OCCUR_TIME,                                                                   \n"
							+ " COALESCE(B.PPS_IN,-1) AS PPS_IN, COALESCE(B.PPS_OUT,-1) AS PPS_OUT, COALESCE(B.PPS_TOT,-1) AS PPS_TOT,           \n"
							+ " A.INDEX AS VS_INDEX, A.VIRTUAL_PORT AS SERVICE_PORT, A.POOL_INDEX,                                              \n"
							+ " A.STATUS AS STATUS, A.VIRTUAL_IP AS VS_IPADDRESS, A.NAME AS VS_NAME                                              \n"
							+ " FROM TMP_SLB_VSERVER                 A                                                                           \n"
							+ " LEFT JOIN TMP_FAULT_SVC_PERF_STATS   B                                                                           \n"
							+ " ON B.OBJ_INDEX = A.INDEX                                                                                             \n"
							+ " LEFT JOIN (SELECT RESPONSE_TIME, OBJ_INDEX FROM TMP_FAULT_SVC_PERF_RESP_TIME                                      \n"
							+ " WHERE OCCUR_TIME > NOW() - INTERVAL '%d SECONDS') D                                                               \n"
							+ " ON B.OBJ_INDEX = D.OBJ_INDEX                                                                                        \n"
							+ " WHERE A.ADC_INDEX = %d                                                                                           \n",
					rescInterval * 2, object.getIndex()); // 모니터링

			if (!sqlSearch.isEmpty())
				sqlText += " AND " + sqlSearch;

			sqlText += getSvcPerfInfoListOrderTypeOthers(orderOption);

			sqlText += sqlLimit;

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoFaultSvcPerfInfo log = new OBDtoFaultSvcPerfInfo();

				log.setAdcObj(object);
				log.setVsIndex(db.getString(rs, "VS_INDEX"));
				log.setVsStatus(db.getInteger(rs, "STATUS"));
				log.setVsIP(db.getString(rs, "VS_IPADDRESS"));
				log.setVsPort(db.getString(rs, "SERVICE_PORT"));
				log.setVsName(db.getString(rs, "VS_NAME"));

				String occur = db.getString(rs, "OCCUR_TIME");
				if (occur != null && OBDateTime.getTimeIntervalCheck(occur, rescInterval)) {
					log.setResponseTime(db.getInteger(rs, "RESPONSE_TIME"));
					log.setBpsIn(db.getLong(rs, "BPS_IN"));
					log.setBpsOut(db.getLong(rs, "BPS_OUT"));
					long value = db.getLong(rs, "BPS_TOT");
					if (value == -2)
						value = -1;
					log.setBpsTotal(value);
					log.setPpsIn(db.getLong(rs, "PPS_IN"));
					log.setPpsOut(db.getLong(rs, "PPS_OUT"));
					value = db.getInteger(rs, "PPS_TOT");
					if (value == -2)
						value = -1;
					log.setPpsTotal(value);
					log.setConnCurr(db.getLong(rs, "CONN_CURR"));
					log.setConnMax(db.getLong(rs, "CONN_MAX"));
					log.setConnTotal(db.getLong(rs, "CONN_TOT"));
				} else {
					log.setResponseTime(-1);
					log.setBpsIn(-1l);
					log.setBpsOut(-1l);
					log.setBpsTotal(-1l);
					log.setPpsIn(-1l);
					log.setPpsOut(-1l);
					log.setPpsTotal(-1l);
					log.setConnCurr(-1l);
					log.setConnMax(-1l);
					log.setConnTotal(-1l);
				}

				retVal.add(log);
			}
		} catch (SQLException e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			db.closeDB();
			throw e;
		} catch (Exception e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return retVal;
	}

	private String getSvcPerfInfoListOrderTypeOthers(OBDtoOrdering orderObj) throws OBException {
		if (orderObj == null)
			return "";
		String retVal = " ORDER BY CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST ";
		int orderDir = orderObj.getOrderDirection();
		switch (orderObj.getOrderType()) {
		case OBDtoOrdering.TYPE_2SECOND:// vip
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(A.VIRTUAL_IP) ASC NULLS LAST, CONN_CURR DESC NULLS LAST ";
			else
				retVal = " ORDER BY INET(A.VIRTUAL_IP) DESC NULLS LAST, CONN_CURR DESC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_3THIRD:// service port
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY SERVICE_PORT ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY SERVICE_PORT DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_4FOURTH:// vname
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VS_NAME ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST";
			else
				retVal = " ORDER BY VS_NAME DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST";
			break;
		case OBDtoOrdering.TYPE_5FIFTH:// response time
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY RESPONSE_TIME ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY RESPONSE_TIME DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST, NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_6SIXTH:// BPS_IN
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BPS_IN ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY BPS_IN DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_7SEVENTH:// BPS_OUT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BPS_OUT ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY BPS_OUT DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_8EIGHTH:// bps total
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BPS_TOT ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY BPS_TOT DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_9NINTH:// PPS_IN
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY PPS_IN ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY PPS_IN DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_10TENTH:// PPS_OUT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY PPS_OUT ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY PPS_OUT DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_11ELEVENTH:// pps total
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY PPS_TOT ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY PPS_TOT DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_14FOURTEENTH:// Concurrent Session
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CONN_CURR ASC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY CONN_CURR DESC NULLS LAST, INET(A.VIRTUAL_IP) ASC NULLS LAST ";
			break;
		}

		return retVal;
	}

	private String getSvcPerfInfoListOrderTypeAlteon(OBDtoOrdering orderObj) throws OBException {
		if (orderObj == null)
			return "";
		String retVal = " ORDER BY CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST ";
		int orderDir = orderObj.getOrderDirection();
		switch (orderObj.getOrderType()) {
		case OBDtoOrdering.TYPE_2SECOND:// vip
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(C.VIRTUAL_IP) ASC NULLS LAST, CONN_CURR DESC NULLS LAST ";
			else
				retVal = " ORDER BY INET(C.VIRTUAL_IP) DESC NULLS LAST, CONN_CURR DESC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_3THIRD:// service port
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY SERVICE_PORT ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY SERVICE_PORT DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_4FOURTH:// vname
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VS_NAME ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST";
			else
				retVal = " ORDER BY VS_NAME DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST";
			break;
		case OBDtoOrdering.TYPE_5FIFTH:// response time
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY RESPONSE_TIME ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY RESPONSE_TIME DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST, NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_6SIXTH:// bps in
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BPS_IN ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY BPS_IN DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_7SEVENTH:// bps out
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BPS_OUT ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY BPS_OUT DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_8EIGHTH:// bps total
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BPS_TOT ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY BPS_TOT DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST, VS_NAME ASC NULLS LAST, SERVICE_PORT ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_14FOURTEENTH:// connection total
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CONN_CURR ASC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST ";
			else
				retVal = " ORDER BY CONN_CURR DESC NULLS LAST, INET(C.VIRTUAL_IP) ASC NULLS LAST ";
			break;
		}

		return retVal;
	}

	// public static void main(String[] args) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// db.openDB();
	//
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// object.setIndex(1);
	//
	// OBDtoSearch searchOption = new OBDtoSearch();
	//
	// OBDtoOrdering orderOption = new OBDtoOrdering();
	// orderOption.setOrderDirection(OBDtoOrdering.DIR_ASCEND);
	// orderOption.setOrderType(OBDtoOrdering.TYPE_2SECOND);
	//
	// // Integer total = new
	// OBFaultMonitoringImpl().getSvcPerfInfoTotalCount(object, searchOption);
	// // System.out.println(total);
	// ArrayList<OBDtoFaultBpsConnInfo> list= new
	// OBFaultMonitoringImpl().getBpsConnHistory(object, "1_232", 80, searchOption);
	// for(OBDtoFaultBpsConnInfo obj:list)
	// System.out.println(obj);
	// db.closeDB();
	// }

	@Override
	public OBDtoFaultPreBpsConnChart getBpsConnHistory(OBDtoADCObject object, String vsIndex, Integer svcPort,
			OBDtoSearch searchOption) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultPreBpsConnChart retVal = new OBDtoFaultPreBpsConnChart();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getBpsConnChartHistory(object, vsIndex, svcPort, searchOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public OBDtoFaultPreBpsConnChart getBpsConnMaxAvgHistory(OBDtoADCObject object, String vsIndex, Integer svcPort,
			OBDtoSearch searchOption) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultPreBpsConnChart retVal = new OBDtoFaultPreBpsConnChart();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getBpsConnChartMaxAvgHistory(object, vsIndex, svcPort, searchOption,
						db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	// public static void main(String[] args) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// db.openDB();
	//
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// object.setIndex(1);
	//
	// OBDtoSearch searchOption = new OBDtoSearch();
	//
	// OBDtoOrdering orderOption = new OBDtoOrdering();
	// orderOption.setOrderDirection(OBDtoOrdering.DIR_ASCEND);
	// orderOption.setOrderType(OBDtoOrdering.TYPE_2SECOND);
	//
	// // Integer total = new
	// OBFaultMonitoringImpl().getSvcPerfInfoTotalCount(object, searchOption);
	// // System.out.println(total);
	// ArrayList<OBDtoDataObj> list= new
	// OBFaultMonitoringImpl().getResponseTimeHistory(object, "1_231", 80,
	// searchOption);
	// for(OBDtoDataObj obj:list)
	// System.out.println(obj);
	// db.closeDB();
	// }

	@Override
	public OBDtoFaultPreBpsConnChart getResponseTimeHistory(OBDtoADCObject object, String vsIndex, Integer svcPort,
			OBDtoSearch searchOption) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultPreBpsConnChart retVal = new OBDtoFaultPreBpsConnChart();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getResponseTimeChartHistory(object, vsIndex, svcPort, searchOption,
						db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public OBDtoFaultPreBpsConnChart getResponseTimeMaxAvgHistory(OBDtoADCObject object, String vsIndex,
			Integer svcPort, OBDtoSearch searchOption) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultPreBpsConnChart retVal = new OBDtoFaultPreBpsConnChart();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getResponseTimeChartMaxAvgHistory(object, vsIndex, svcPort,
						searchOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public OBDtoFaultHWStatus getADCMonHWStatus(OBDtoADCObject object) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultHWStatus retVal = new OBDtoFaultHWStatus();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getADCMonHWStatus(object);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public OBDtoFaultDataObj getADCMonPktErrHistory(OBDtoADCObject object, OBDtoSearch searchOption)
			throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultDataObj retVal = new OBDtoFaultDataObj();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getADCMonPktErrHistory(object, searchOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public Integer getSvcPerfInfoTotalCount(OBDtoADCObject object, OBDtoSearch searchOption, Integer accountIndex,
			String accountRole) throws OBException {
		OBDatabase db = new OBDatabase();
		int retVal;
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				retVal = -1; // 구현 보류
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				retVal = -1; // 구현 보류
			} else {
				retVal = getSvcPerfInfoTotalCountAdc(object, searchOption, accountIndex, accountRole, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public Integer getSvcPerfInfoTotalCountAdc(OBDtoADCObject object, OBDtoSearch searchOption, Integer accountIndex,
			String accountRole, OBDatabase db) throws OBException {
		int retVal = 0;
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());
			if (adcInfo == null)
				return retVal;
			if (adcInfo.getAdcType() == OBDtoAdcInfo.ADC_TYPE_ALTEON) {
				return getSvcPerfInfoTotalCountAlteon(object, searchOption, accountIndex, accountRole, db);
			} else if (adcInfo.getAdcType() == OBDtoAdcInfo.ADC_TYPE_F5
					|| adcInfo.getAdcType() == OBDtoAdcInfo.ADC_TYPE_PIOLINK_PAS
					|| adcInfo.getAdcType() == OBDtoAdcInfo.ADC_TYPE_PIOLINK_PASK) {
				return getSvcPerfInfoTotalCountExceptAlteon(object, searchOption, accountIndex, accountRole, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return retVal;
	}

	public Integer getSvcPerfInfoTotalCountAlteon(OBDtoADCObject object, OBDtoSearch searchOption, Integer accountIndex,
			String accountRole, OBDatabase db) throws OBException {
		int adcIndex = object.getIndex();
		String searchKeys = null;
		if (searchOption != null) {
			searchKeys = searchOption.getSearchKey();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		String sqlText = "";
		String sqlSearch = "";
		int result = 0;
		try {
			if (searchKeys != null && !searchKeys.isEmpty()) {
				String wildcardKey = "%" + searchKeys + "%";
				sqlSearch = String.format(" ( B.VIRTUAL_IP LIKE %s OR B.NAME ILIKE %s ) ",
						OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
				sqlText += sqlSearch;
			} else {
				sqlSearch = " TRUE ";
			}

			if (accountRole.equals("vsAdmin")) {
				if (sqlSearch.length() > 0) {
					sqlSearch += " AND ";
				}
				sqlSearch += " VS_INDEX IN (SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ADC_INDEX = " + adcIndex
						+ " AND ACCNT_INDEX = " + accountIndex + ") ";
			}
			sqlText = String.format(" SELECT COUNT(A.ADC_INDEX) AS CNT \n"
					+ " FROM TMP_SLB_VS_SERVICE    A                                                    \n"
					+ " INNER JOIN TMP_SLB_VSERVER B                                                    \n"
					+ " ON B.INDEX = A.VS_INDEX           		                                      \n"
					+ " WHERE A.ADC_INDEX = %d AND %s                    \n", adcIndex, sqlSearch);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true)
				result = db.getInteger(rs, "CNT");
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		db.closeDB();
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
		return result;
	}

	public Integer getSvcPerfInfoTotalCountExceptAlteon(OBDtoADCObject object, OBDtoSearch searchOption,
			Integer accountIndex, String accountRole, OBDatabase db) throws OBException {
		int adcIndex = object.getIndex();
		String searchKeys = null;
		if (searchOption != null) {
			searchKeys = searchOption.getSearchKey();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		String sqlText = "";
		String sqlSearch = "";
		int result = 0;
		try {
			if (searchKeys != null && !searchKeys.isEmpty()) {
				String wildcardKey = "%" + searchKeys + "%";
				sqlSearch = String.format(" ( NAME ILIKE %s OR VIRTUAL_IP LIKE %s ) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
				sqlText += sqlSearch;
			} else {
				sqlSearch = " TRUE ";
			}

			if (accountRole.equals("vsAdmin")) {
				if (sqlSearch.length() > 0) {
					sqlSearch += " AND ";
				}
				sqlSearch += " INDEX IN (SELECT VS_INDEX FROM MNG_ACCNT_VS_MAP WHERE ADC_INDEX = " + adcIndex
						+ " AND ACCNT_INDEX = " + accountIndex + ") ";
			}

			sqlText = String.format(" SELECT COUNT(ADC_INDEX) AS CNT	             \n"
					+ "     FROM TMP_SLB_VSERVER        				 \n"
					+ " WHERE ADC_INDEX = %d AND %s                    \n", adcIndex, sqlSearch);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true)
				result = db.getInteger(rs, "CNT");
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
		return result;
	}

	@Override
	public OBDtoFaultDataObj getADCMonPktDropHistory(OBDtoADCObject object, OBDtoSearch searchOption)
			throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultDataObj retVal = new OBDtoFaultDataObj();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getADCMonPktDropHistory(object, searchOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public OBDtoFaultDataObj getADCMonSessionHistory(OBDtoADCObject object, OBDtoSearch searchOption)
			throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultDataObj retVal = new OBDtoFaultDataObj();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getADCMonSessionHistory(object, searchOption, db);
			}
		} catch (OBException e) {
			db.closeDB();
			throw e;
		} catch (Exception e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public OBDtoAdcCurrentSession getADCMonSessionHistoryNew(OBDtoADCObject object, OBDtoSearch searchOption)
			throws OBException { // FLB/SLB 세션 그래프 데이터를 구한다. 기간조건
									// 완성되면 위 함수와 바꾼다.
		OBDtoAdcCurrentSession retVal = new OBDtoAdcCurrentSession();
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getADCMonSessionHistoryNew(object, searchOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public OBDtoAdcMonCpuDataObj getADCMonCpuData(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoAdcMonCpuDataObj retVal = new OBDtoAdcMonCpuDataObj();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getADCMonCpuData(object, searchOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public OBDtoAdcMonCpuDataObj getADCMonCpuHistory(OBDtoADCObject object, OBDtoSearch searchOption)
			throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoAdcMonCpuDataObj retVal = new OBDtoAdcMonCpuDataObj();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getADCMonCpuHistory(object, searchOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public OBDtoFaultCpuDataObj getADCMonCpuSPHistory(OBDtoADCObject object, OBDtoSearch searchOption, Integer cpuNum)
			throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
		} catch (OBException e) {
			throw e;
		}

		OBDtoFaultCpuDataObj retVal = new OBDtoFaultCpuDataObj();
		try {
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
			} else {
				retVal = new OBFaultMonitoringDB().getADCMonCpuSPHistory(object, searchOption, cpuNum, db);
			}
		} catch (OBException e) {
			db.closeDB();
			throw e;
		} catch (Exception e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		db.closeDB();
		return retVal;
	}

	// TODO
	@Override
	public OBDtoFaultCpuHistory getADCMonCpuSpConnectionInfo(OBDtoADCObject object, OBDtoSearch searchOption)
			throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
		} catch (OBException e) {
			throw e;
		}

		OBDtoFaultCpuHistory retVal = new OBDtoFaultCpuHistory();
		try {
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getADCMonCpuSpConnectionInfo(object, searchOption, db);
			}
		} catch (OBException e) {
			db.closeDB();
			throw e;
		} catch (Exception e) {
			db.closeDB();
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		db.closeDB();
		return retVal;
	}

	@Override
	public OBDtoFaultDataObj getADCMonMemoryHistory(OBDtoADCObject object, OBDtoSearch searchOption)
			throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultDataObj retVal = new OBDtoFaultDataObj();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getADCMonMemoryHistory(object, searchOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

//    public static void main(String[] args) throws OBException
//    {
//        OBDatabase db = new OBDatabase();
//
//        db.openDB();
//
//        OBDtoADCObject object = new OBDtoADCObject();
//        object.setCategory(OBDtoADCObject.CATEGORY_ADC);
//        object.setDesciption("전체 대상 스케줄.");
//        object.setName("total schedule");
//        object.setIndex(23);
//
//        // OBDtoCpuMemStatus retVal = new OBDtoCpuMemStatus();
//
//        Integer hour = 1;
//        Date endTime = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(endTime);
//        calendar.add(Calendar.HOUR_OF_DAY, -hour);
//        Date startTime = calendar.getTime();
//
//        OBDtoSearch searchOption = new OBDtoSearch();
//        searchOption.setFromTime(startTime);
//        searchOption.setToTime(endTime);
//        OBDtoFaultDataObj retVal = new OBFaultMonitoringImpl().getADCMonBpsHistory(object, searchOption);
//        System.out.println(retVal);
//
//        // OBDtoOrdering orderOption = new OBDtoOrdering();
//        // orderOption.setOrderDirection(OBDtoOrdering.DIR_ASCEND);
//        // orderOption.setOrderType(OBDtoOrdering.TYPE_2SECOND);
//        //
//        // ArrayList<OBDtoSessionSearchOption> searchKeyList = new ArrayList<OBDtoSessionSearchOption>();
//        // OBDtoSessionSearchOption keyword = new OBDtoSessionSearchOption();
//        // keyword.setContent("192.168.0.5");
//        // keyword.setType(OBDtoSessionSearchOption.OPTION_TYPE_SRC_IP);
//        // searchKeyList.add(keyword);
//        // OBDtoSessionSearchOption keyword2 = new OBDtoSessionSearchOption();
//        // keyword2.setContent("172.172.2.1");
//        // keyword2.setType(OBDtoSessionSearchOption.OPTION_TYPE_DST_IP);
//        // searchKeyList.add(keyword2);
//
//        // Integer total = new OBFaultMonitoringImpl().getSvcPerfInfoTotalCount(object, searchOption);
//        // System.out.println(total);
//        // Integer list= new OBFaultMonitoringImpl().searchSessionInfoList(object, searchKeyList, null, null);
//        // System.out.println(list);
//        db.closeDB();
//    }

	@Override
	public OBDtoFaultDataObj getADCMonBpsHistory(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultDataObj retVal = new OBDtoFaultDataObj();
		try {
			if (object == null)
				return retVal;

			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getADCMonBpsHistory(object, searchOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	/**
	 * 원래는 MP의 64초 평균 값을 수집했으나 요구사항에 의하여 ( 작업 #5059 ) 4초 평균 값으로 변경했다. 그 과정에서 메소드 명도
	 * 1Min 에서 4Sec 으로 변경됐다.
	 */
	private OBDtoCpuMemStatus getFaultAdcCpuMemoryUsageAlteon64Sec(DtoOidFaultCheckHW snmpOidInfoHW, String ipAddress,
			OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		OBDtoCpuMemStatus retVal = new OBDtoCpuMemStatus();
		retVal.setOccurTime(OBDateTime.toTimestamp(OBDateTime.now()));

		OBSnmp ClassSnmp = new OBSnmpAlteon(ipAddress, snmpInfo);
		List<VariableBinding> tmpList = null;
		try {
			Long totalValue = 0L;
			// memory usage;
			String oid = snmpOidInfoHW.getMpMemStatsTotal();
			if (oid != null) {
				tmpList = ClassSnmp.walk(oid);
				if (tmpList != null && tmpList.size() > 0) {
					VariableBinding var = tmpList.get(0);
					totalValue = var.getVariable().toLong();
				}
			}

			oid = snmpOidInfoHW.getMpMemStatsFree();
			if (oid != null) {
				tmpList = ClassSnmp.walk(oid);
				Long freeValue = 0L;
				if (tmpList != null && tmpList.size() > 0) {
					VariableBinding var = tmpList.get(0);
					freeValue = var.getVariable().toLong();
				}

				Long memUsage = 0L;
				if (totalValue.longValue() != 0)
					memUsage = 100L - (freeValue.longValue() * 100L / totalValue.longValue());
				retVal.setMemUsage(memUsage.intValue());
			}

			// mp usage
			oid = snmpOidInfoHW.getMpCpuStats64Sec();
			if (oid != null) {
				tmpList = ClassSnmp.walk(oid);
				if (tmpList != null && tmpList.size() > 0) {
					VariableBinding var = tmpList.get(0);
					retVal.setCpu1Usage(var.getVariable().toInt());
				}
			}

			// sp usage
			oid = snmpOidInfoHW.getSpCpuStats64Sec();
			if (oid != null) {
				tmpList = ClassSnmp.walk(oid);
				for (VariableBinding var : tmpList) {
					ArrayList<Integer> oidList = ClassSnmp.parseOid(var.getOid().toString(), oid);
					Integer spIndex = oidList.get(0);
					Integer spValue = var.getVariable().toInt();
					if (spIndex.intValue() == 1)
						retVal.setCpu2Usage(spValue);
					else if (spIndex.intValue() == 2)
						retVal.setCpu3Usage(spValue);
					else if (spIndex.intValue() == 3)
						retVal.setCpu4Usage(spValue);
					else if (spIndex.intValue() == 4)
						retVal.setCpu5Usage(spValue);
					else if (spIndex.intValue() == 5)
						retVal.setCpu6Usage(spValue);
					else if (spIndex.intValue() == 6)
						retVal.setCpu7Usage(spValue);
					else if (spIndex.intValue() == 7)
						retVal.setCpu8Usage(spValue);
					else if (spIndex.intValue() == 8)
						retVal.setCpu9Usage(spValue);
					else if (spIndex.intValue() == 9)
						retVal.setCpu10Usage(spValue);
					else if (spIndex.intValue() == 10)
						retVal.setCpu11Usage(spValue);
					else if (spIndex.intValue() == 11)
						retVal.setCpu12Usage(spValue);
					else if (spIndex.intValue() == 12)
						retVal.setCpu13Usage(spValue);
					else if (spIndex.intValue() == 13)
						retVal.setCpu14Usage(spValue);
					else if (spIndex.intValue() == 14)
						retVal.setCpu15Usage(spValue);
					else if (spIndex.intValue() == 15)
						retVal.setCpu16Usage(spValue);
					else if (spIndex.intValue() == 16)
						retVal.setCpu17Usage(spValue);
					else if (spIndex.intValue() == 17)
						retVal.setCpu18Usage(spValue);
					else if (spIndex.intValue() == 18)
						retVal.setCpu19Usage(spValue);
					else if (spIndex.intValue() == 19)
						retVal.setCpu20Usage(spValue);
					else if (spIndex.intValue() == 20)
						retVal.setCpu21Usage(spValue);
					else if (spIndex.intValue() == 21)
						retVal.setCpu22Usage(spValue);
					else if (spIndex.intValue() == 22)
						retVal.setCpu23Usage(spValue);
					else if (spIndex.intValue() == 23)
						retVal.setCpu24Usage(spValue);
					else if (spIndex.intValue() == 24)
						retVal.setCpu25Usage(spValue);
					else if (spIndex.intValue() == 25)
						retVal.setCpu26Usage(spValue);
					else if (spIndex.intValue() == 26)
						retVal.setCpu27Usage(spValue);
					else if (spIndex.intValue() == 27)
						retVal.setCpu28Usage(spValue);
					else if (spIndex.intValue() == 28)
						retVal.setCpu29Usage(spValue);
					else if (spIndex.intValue() == 29)
						retVal.setCpu30Usage(spValue);
					else if (spIndex.intValue() == 30)
						retVal.setCpu31Usage(spValue);
					else if (spIndex.intValue() == 31)
						retVal.setCpu32Usage(spValue);
				}
			}

			// sp Connection
			oid = snmpOidInfoHW.getSpCpuStats64CurBindings();
			if (oid != null) {
				tmpList = ClassSnmp.walk(oid);
				for (VariableBinding var : tmpList) {
					ArrayList<Integer> oidList = ClassSnmp.parseOid(var.getOid().toString(), oid);
					Integer spIndex = oidList.get(0);
					Integer spValue = var.getVariable().toInt();
					if (spIndex.intValue() == 1)
						retVal.setCpu2Conns(spValue);
					else if (spIndex.intValue() == 2)
						retVal.setCpu3Conns(spValue);
					else if (spIndex.intValue() == 3)
						retVal.setCpu4Conns(spValue);
					else if (spIndex.intValue() == 4)
						retVal.setCpu5Conns(spValue);
					else if (spIndex.intValue() == 5)
						retVal.setCpu6Conns(spValue);
					else if (spIndex.intValue() == 6)
						retVal.setCpu7Conns(spValue);
					else if (spIndex.intValue() == 7)
						retVal.setCpu8Conns(spValue);
					else if (spIndex.intValue() == 8)
						retVal.setCpu9Conns(spValue);
					else if (spIndex.intValue() == 9)
						retVal.setCpu10Conns(spValue);
					else if (spIndex.intValue() == 10)
						retVal.setCpu11Conns(spValue);
					else if (spIndex.intValue() == 11)
						retVal.setCpu12Conns(spValue);
					else if (spIndex.intValue() == 12)
						retVal.setCpu13Conns(spValue);
					else if (spIndex.intValue() == 13)
						retVal.setCpu14Conns(spValue);
					else if (spIndex.intValue() == 14)
						retVal.setCpu15Conns(spValue);
					else if (spIndex.intValue() == 15)
						retVal.setCpu16Conns(spValue);
					else if (spIndex.intValue() == 16)
						retVal.setCpu17Conns(spValue);
					else if (spIndex.intValue() == 17)
						retVal.setCpu18Conns(spValue);
					else if (spIndex.intValue() == 18)
						retVal.setCpu19Conns(spValue);
					else if (spIndex.intValue() == 19)
						retVal.setCpu20Conns(spValue);
					else if (spIndex.intValue() == 20)
						retVal.setCpu21Conns(spValue);
					else if (spIndex.intValue() == 21)
						retVal.setCpu22Conns(spValue);
					else if (spIndex.intValue() == 22)
						retVal.setCpu23Conns(spValue);
					else if (spIndex.intValue() == 23)
						retVal.setCpu24Conns(spValue);
					else if (spIndex.intValue() == 24)
						retVal.setCpu25Conns(spValue);
					else if (spIndex.intValue() == 25)
						retVal.setCpu26Conns(spValue);
					else if (spIndex.intValue() == 26)
						retVal.setCpu27Conns(spValue);
					else if (spIndex.intValue() == 27)
						retVal.setCpu28Conns(spValue);
					else if (spIndex.intValue() == 28)
						retVal.setCpu29Conns(spValue);
					else if (spIndex.intValue() == 29)
						retVal.setCpu30Conns(spValue);
					else if (spIndex.intValue() == 30)
						retVal.setCpu31Conns(spValue);
					else if (spIndex.intValue() == 31)
						retVal.setCpu32Conns(spValue);
				}
			}

			// sp session maximum
			oid = snmpOidInfoHW.getSpCpuSessionMax();
			if (oid != null) {
				tmpList = ClassSnmp.walk(oid);
				if (tmpList != null && tmpList.size() > 0) {
					tmpList = ClassSnmp.walk(oid);
					for (VariableBinding var : tmpList) {
						ArrayList<Integer> oidList = ClassSnmp.parseOid(var.getOid().toString(), oid);
						Integer spIndex = oidList.get(0);
						Integer spMaxValue = var.getVariable().toInt();
						if (spIndex.intValue() == 1)
							retVal.setSpSessionMax(spMaxValue);
					}
				}
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to query snmp");
		}

		return retVal;
	}

	private OBDtoCpuMemStatus getFaultAdcCpuMemoryUsageF51Min(DtoOidFaultCheckHW snmpOidInfoHW, String ipAddress,
			OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		OBDtoCpuMemStatus retVal = new OBDtoCpuMemStatus();
		retVal.setOccurTime(OBDateTime.toTimestamp(OBDateTime.now()));

		OBSnmp ClassSnmp = new OBSnmpF5(ipAddress, snmpInfo);
		List<VariableBinding> tmpList = null;
		try {
			Long totalValue = 0L;
			// memory usage;
			String oid = snmpOidInfoHW.getMpMemStatsTotal();
			if (oid != null) {
				tmpList = ClassSnmp.walk(oid);
				if (tmpList != null && tmpList.size() > 0) {
					VariableBinding var = tmpList.get(0);
					totalValue = var.getVariable().toLong();//
				}
			}

			oid = snmpOidInfoHW.getMpMemStatsFree();
			if (oid != null) {
				tmpList = ClassSnmp.walk(oid);
				Long usedValue = 0L;
				if (tmpList != null && tmpList.size() > 0) {
					VariableBinding var = tmpList.get(0);
					usedValue = var.getVariable().toLong();//
				}

				Long memUsage = 0L;
				if (totalValue.longValue() != 0)
					memUsage = usedValue.longValue() * 100L / totalValue.longValue();
				retVal.setMemUsage(memUsage.intValue());
			}

			// usage
			oid = snmpOidInfoHW.getMpCpuStats64Sec();
			if (oid != null) {
				tmpList = ClassSnmp.walk(oid);
				ArrayList<Integer> cpuValue = new ArrayList<Integer>();
				for (VariableBinding var : tmpList) {
					Integer spValue = var.getVariable().toInt();
					cpuValue.add(spValue);
				}

				if (cpuValue != null || !cpuValue.isEmpty()) {
					int cpuSize = cpuValue.size();
					if (cpuSize >= 1)
						retVal.setCpu1Usage(cpuValue.get(0));
					if (cpuSize >= 2)
						retVal.setCpu2Usage(cpuValue.get(1));
					if (cpuSize >= 3)
						retVal.setCpu3Usage(cpuValue.get(2));
					if (cpuSize >= 4)
						retVal.setCpu4Usage(cpuValue.get(3));
					if (cpuSize >= 5)
						retVal.setCpu5Usage(cpuValue.get(4));
					if (cpuSize >= 6)
						retVal.setCpu6Usage(cpuValue.get(5));
					if (cpuSize >= 7)
						retVal.setCpu7Usage(cpuValue.get(6));
					if (cpuSize >= 8)
						retVal.setCpu8Usage(cpuValue.get(7));
					if (cpuSize >= 9)
						retVal.setCpu9Usage(cpuValue.get(8));
					if (cpuSize >= 10)
						retVal.setCpu10Usage(cpuValue.get(9));
					if (cpuSize >= 11)
						retVal.setCpu11Usage(cpuValue.get(10));
					if (cpuSize >= 12)
						retVal.setCpu12Usage(cpuValue.get(11));
					if (cpuSize >= 13)
						retVal.setCpu13Usage(cpuValue.get(12));
					if (cpuSize >= 14)
						retVal.setCpu14Usage(cpuValue.get(13));
					if (cpuSize >= 15)
						retVal.setCpu15Usage(cpuValue.get(14));
					if (cpuSize >= 16)
						retVal.setCpu16Usage(cpuValue.get(15));
				}
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to query snmp");
		}

		return retVal;
	}

	/**
	 * PAS 장비의 CPU와 Memory 사용량을 측정한다. 작업 #4888에 의거해 새롭게 제작됐다.
	 * 
	 * @param snmpOidInfoHW
	 * @param ipAddress
	 * @param snmpInfo
	 * @return
	 * @throws OBException
	 */
	private OBDtoCpuMemStatus getFaultAdcCpuMemoryUsagePAS(DtoOidFaultCheckHW snmpOidInfoHW, String ipAddress,
			OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		OBSnmp snmp = new OBSnmpPAS(ipAddress, snmpInfo);
		return getFaultAdcCpuMemoryUsagePASSeries(snmpOidInfoHW, snmp);
	}

	private OBDtoCpuMemStatus getFaultAdcCpuMemoryUsagePASK1Min(DtoOidFaultCheckHW snmpOidInfoHW, String ipAddress,
			OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		OBSnmp snmp = new OBSnmpPASK(ipAddress, snmpInfo);
		return getFaultAdcCpuMemoryUsagePASSeries(snmpOidInfoHW, snmp);
	}

	private OBDtoCpuMemStatus getFaultAdcCpuMemoryUsagePASSeries(DtoOidFaultCheckHW snmpOidInfoHW, OBSnmp snmp)
			throws OBException {
		OBDtoCpuMemStatus retVal = new OBDtoCpuMemStatus();
		retVal.setOccurTime(OBDateTime.toTimestamp(OBDateTime.now()));

		List<VariableBinding> tmpList = null;
		try {
			long totalValue = 0L;
			// memory usage;
			String oid = snmpOidInfoHW.getMpMemStatsTotal();
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "mem total oid: " + oid);
			if (oid != null) {
				tmpList = snmp.walk(oid);
				if (tmpList != null && tmpList.size() > 0) {
					VariableBinding var = tmpList.get(0);
					totalValue = var.getVariable().toLong();
					// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "mem total: " + totalValue);
				}
			}

			oid = snmpOidInfoHW.getMpMemStatsFree();
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "mem free oid: " + oid);
			if (oid != null) {
				tmpList = snmp.walk(oid);
				Long freeValue = 0L;
				if (tmpList != null && tmpList.size() > 0) {
					VariableBinding var = tmpList.get(0);
					freeValue = var.getVariable().toLong();
					// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "mem free: " + freeValue);
				}

				Long memUsage = 0L;
				if (totalValue != 0)
					memUsage = 100L - (freeValue.longValue() * 100L / totalValue);
				retVal.setMemUsage(memUsage.intValue());
			}

			// mp usage
			oid = snmpOidInfoHW.getMpCpuStats1Sec();
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "mp usage oid: " + oid);
			if (oid != null) {
				tmpList = snmp.walk(oid);

				for (VariableBinding var : tmpList) {
					retVal.setCpu1Usage(var.getVariable().toInt());
					// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "mp usage: " +
					// var.getVariable().toInt());
				}
			}

			// sp usage
			oid = snmpOidInfoHW.getSpCpuStats1Sec();
			// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sp usage oid: " + oid);
			if (oid != null) {
				tmpList = snmp.walk(oid);

				for (VariableBinding var : tmpList) {
					retVal.setCpu2Usage(var.getVariable().toInt());
					// OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sp usage: " +
					// var.getVariable().toInt());
				}
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to query snmp");
		}

		return retVal;
	}

	// public static void main(String[] args) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// db.openDB();
	//
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// object.setIndex(3);
	//
	// // OBDtoFaultBpsConnInfo prev = null;
	// OBDtoCpuMemStatus list= new
	// OBFaultMonitoringImpl().getFaultAdcCpuMemoryUsage1Min(object, db);
	// System.out.println(list);
	// db.closeDB();
	// }

	// public OBDtoCpuMemStatus getFaultAdcCpuMemoryUsage1Min(OBDtoADCObject object,
	// OBDatabase db) throws OBException
	// {
	// OBDtoCpuMemStatus retVal = new OBDtoCpuMemStatus();
	// if(object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
	// return retVal;
	// try
	// {
	// OBDtoAdcInfo adcInfo = new
	// OBAdcManagementImpl().getAdcInfo(object.getIndex());
	// if(adcInfo == null)
	// {
	// return retVal;
	// }
	// DtoOidFaultCheckHW snmpOidInfoHW = new
	// OBSnmpOidDB().getFaultCheckHWInfo(adcInfo.getAdcType(),
	// adcInfo.getSwVersion(), db);
	//
	// if(adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
	// return getFaultAdcCpuMemoryUsageAlteon1Min(snmpOidInfoHW,
	// adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
	// else if(adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5)
	// return getFaultAdcCpuMemoryUsageF51Min(snmpOidInfoHW,
	// adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
	// else if(adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS)
	// return getFaultAdcCpuMemoryUsagePAS1Min(snmpOidInfoHW,
	// adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
	// else if(adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK)
	// return getFaultAdcCpuMemoryUsagePASK1Min(snmpOidInfoHW,
	// adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
	// }
	// catch(Exception e)
	// {
	// throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get
	// cpu/memory info");
	// }
	// return retVal;
	// }

	public OBDtoCpuMemStatus getFaultAdcCpuMemoryUsage(OBDtoADCObject object) throws OBException {
		OBDtoCpuMemStatus retVal = new OBDtoCpuMemStatus();
		if (object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
			return retVal;
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());
			if (adcInfo == null) {
				return retVal;
			}
			DtoOidFaultCheckHW snmpOidInfoHW = new OBSnmpOidDB().getFaultCheckHWInfo(adcInfo.getAdcType(),
					adcInfo.getSwVersion());

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
				return getFaultAdcCpuMemoryUsageAlteon64Sec(snmpOidInfoHW, adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5)
				return getFaultAdcCpuMemoryUsageF51Min(snmpOidInfoHW, adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS)
				return getFaultAdcCpuMemoryUsagePAS(snmpOidInfoHW, adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK)
				return getFaultAdcCpuMemoryUsagePASK1Min(snmpOidInfoHW, adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "failed to get cpu/memory info");
		}
		return retVal;
	}

	public ArrayList<Integer> getFanSpeed(OBDtoADCObject object) throws OBException {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		if (object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
			return retVal;
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(),
						adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
				return snmp.getSystemFanSpeed(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				return snmp.getSystemFanSpeed(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				OBSnmpPAS snmp = OBCommon.getValidSnmpPASHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				return snmp.getSystemFanSpeed(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				OBSnmpPASK snmp = OBCommon.getValidSnmpPASKHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				return snmp.getSystemFanSpeed(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} else {
				return retVal;
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
	}

	public ArrayList<Integer> getFanStatus(OBDtoADCObject object) throws OBException {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		if (object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
			return retVal;
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(),
						adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
				return snmp.getSystemFanStatus(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				return snmp.getSystemFanStatus(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				OBSnmpPAS snmp = OBCommon.getValidSnmpPASHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				return snmp.getSystemFanStatus(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				OBSnmpPASK snmp = OBCommon.getValidSnmpPASKHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				return snmp.getSystemFanStatus(adcInfo.getAdcType(), adcInfo.getSwVersion());
			} else {
				return retVal;
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
	}

	// private final static Integer MAX_POWER_SUPPLY_COUNT = 4;
	public OBDtoPowerSupplyInfo getPowerSupplyStatus(OBDtoADCObject object) throws OBException {
		OBDtoPowerSupplyInfo retVal = new OBDtoPowerSupplyInfo();
		if (object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
			return retVal;
		retVal.setAdcIndex(object.getIndex());
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());

			ArrayList<Integer> psStatusList = null;
			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(),
						adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
				psStatusList = snmp.getSystemPowerSupplyStatus(adcInfo.getAdcType(), adcInfo.getSwVersion());
				if (psStatusList.size() == 0)
					return retVal;
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				psStatusList = snmp.getSystemPowerSupplyStatus(adcInfo.getAdcType(), adcInfo.getSwVersion());
				if (psStatusList.size() == 0)
					return retVal;
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				OBSnmpPAS snmp = OBCommon.getValidSnmpPASHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				psStatusList = snmp.getSystemPowerSupplyStatus(adcInfo.getAdcType(), adcInfo.getSwVersion());
				if (psStatusList.size() == 0)
					return retVal;
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				OBSnmpPASK snmp = OBCommon.getValidSnmpPASKHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				psStatusList = snmp.getSystemPowerSupplyStatus(adcInfo.getAdcType(), adcInfo.getSwVersion());
				if (psStatusList.size() == 0)
					return retVal;
			} else {
				return retVal;
			}

			if (psStatusList.get(0) != null)
				retVal.setPan1Status(psStatusList.get(0));
			if (psStatusList.size() >= 2 && psStatusList.get(1) != null)
				retVal.setPan2Status(psStatusList.get(1));
			if (psStatusList.size() >= 3 && psStatusList.get(2) != null)
				retVal.setPan3Status(psStatusList.get(2));
			if (psStatusList.size() >= 4 && psStatusList.get(3) != null)
				retVal.setPan4Status(psStatusList.get(3));
			return retVal;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
	}

	public OBDtoHddInfo getHddStatus(OBDtoADCObject object, OBDatabase db) throws OBException {
		OBDtoHddInfo retVal = new OBDtoHddInfo();
		if (object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
			return retVal;

		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(),
						adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
				retVal = snmp.getSystemHddtatus(adcInfo.getAdcType(), adcInfo.getSwVersion(), db);
				return retVal;
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				retVal = snmp.getSystemHddtatus(adcInfo.getAdcType(), adcInfo.getSwVersion());
				return retVal;
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				OBSnmpPAS snmp = OBCommon.getValidSnmpPASHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				retVal = snmp.getSystemHddtatus(adcInfo.getAdcType(), adcInfo.getSwVersion(), db);
				return retVal;
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				OBSnmpPASK snmp = OBCommon.getValidSnmpPASKHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				retVal = snmp.getSystemHddtatus(adcInfo.getAdcType(), adcInfo.getSwVersion(), db);
				return retVal;
			} else {
				return retVal;
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
	}

	// 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할
	// 예정이다.
	public OBDtoHddInfo getHddStatus(OBDtoADCObject object) throws OBException {
		OBDtoHddInfo retVal = new OBDtoHddInfo();
		if (object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
			return retVal;

		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(),
						adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
				retVal = snmp.getSystemHddtatus(adcInfo.getAdcType(), adcInfo.getSwVersion());
				return retVal;
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				retVal = snmp.getSystemHddtatus(adcInfo.getAdcType(), adcInfo.getSwVersion());
				return retVal;
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				OBSnmpPAS snmp = OBCommon.getValidSnmpPASHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				retVal = snmp.getSystemHddtatus(adcInfo.getAdcType(), adcInfo.getSwVersion());
				return retVal;
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				OBSnmpPASK snmp = OBCommon.getValidSnmpPASKHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				retVal = snmp.getSystemHddtatus(adcInfo.getAdcType(), adcInfo.getSwVersion());
				return retVal;
			} else {
				return retVal;
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
	}

	public ArrayList<OBDtoMonL2Ports> getL2PortsInfo(int category, int adcType, String adcIPAddress, String swVersion,
			OBDtoAdcSnmpInfo snmpInfo) throws OBException {
		ArrayList<OBDtoMonL2Ports> retVal = new ArrayList<OBDtoMonL2Ports>();
		if (category != OBDtoADCObject.CATEGORY_ADC)
			return retVal;
		try {
			if (adcType == OBDefine.ADC_TYPE_ALTEON) {
				OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(swVersion, adcIPAddress, snmpInfo);
				return snmp.getPortsInfo(adcType, swVersion);
			} else if (adcType == OBDefine.ADC_TYPE_F5) {
				OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(swVersion, adcIPAddress, snmpInfo);
				return snmp.getPortsInfo(adcType, swVersion);
			} else if (adcType == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				OBSnmpPAS snmp = OBCommon.getValidSnmpPASHandler(swVersion, adcIPAddress, snmpInfo);
				return snmp.getPortsInfo(adcType, swVersion);
			} else if (adcType == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				OBSnmpPASK snmp = OBCommon.getValidSnmpPASKHandler(swVersion, adcIPAddress, snmpInfo);
				return snmp.getPortsInfo(adcType, swVersion);
			} else {
				return retVal;
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
	}

	private ArrayList<OBDtoMonTrafficVSvcInfo> convertVSVCMemberToVSVCForAlteon(Integer adcIndex, Timestamp occurTime,
			ArrayList<OBDtoMonTrafficVSvcMemberInfo> list) throws OBException {
		HashMap<String, OBDtoMonTrafficVSvcInfo> retVal = new HashMap<String, OBDtoMonTrafficVSvcInfo>();
		if (list.size() == 0) {
			return new ArrayList<OBDtoMonTrafficVSvcInfo>(retVal.values());
		}

		for (OBDtoMonTrafficVSvcMemberInfo memLog : list) {
			String vsvcIndex = memLog.getVsIndex();// 실제로는 vsvcIndex임. OBCommon.makeVSvcIndex(adcIndex,
													// memLog.getVsIndex(), memLog.getSvcPort());
			OBDtoMonTrafficVSvcInfo obj = retVal.get(vsvcIndex);
			if (obj != null) {
				obj.setBytesIn(obj.getBytesIn() + memLog.getBytesIn());
				obj.setBytesOut(0L);
				obj.setCurConns(obj.getCurConns() + memLog.getCurConns());
				obj.setMaxConns(obj.getMaxConns() + memLog.getMaxConns());
				obj.setPktsIn(-1L);
				obj.setPktsOut(-1L);
				obj.setTotConns(obj.getTotConns() + memLog.getTotConns());
			} else {
				obj = new OBDtoMonTrafficVSvcInfo();
				obj.setOccurTime(occurTime);
				obj.setObjIndex(vsvcIndex);
				obj.setVsvcIndex(vsvcIndex);
				obj.setVsIndex(memLog.getVsIndex());
				obj.setAdcIndex(adcIndex);
				obj.setSvcPort(memLog.getSvcPort());
				obj.setVsNameID(memLog.getVsNameID());

				obj.setBytesIn(memLog.getBytesIn());
				obj.setBytesOut(0L);
				obj.setCurConns(memLog.getCurConns());
				obj.setMaxConns(memLog.getMaxConns());
				obj.setPktsIn(-1L);
				obj.setPktsOut(-1L);
				obj.setTotConns(memLog.getTotConns());

				retVal.put(vsvcIndex, obj);
			}
		}

		return new ArrayList<OBDtoMonTrafficVSvcInfo>(retVal.values());
	}

	// public static void main(String[] args) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// db.openDB();
	//
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// object.setIndex(1);
	//
	// Date now = new Date();
	// Timestamp occurTime = new Timestamp(now.getTime());
	// ArrayList<OBDtoMonTrafficVSvcMemberInfo> alteonMemberList = null;
	//
	// // connection, bytes, bps, packets, pps
	// alteonMemberList = new
	// OBFaultMonitoringImpl().getVSvcMemberPerfDataFromADC(object, occurTime, db);
	//
	// ArrayList<OBDtoMonTrafficVSvcInfo> list= new
	// OBFaultMonitoringImpl().getVSvcPerfDataFromADC(object, occurTime,
	// alteonMemberList, db);
	// for(OBDtoMonTrafficVSvcInfo obj:list)
	// System.out.println(obj);
	// db.closeDB();
	// }

	public ArrayList<OBDtoMonTrafficVSvcInfo> getVSvcPerfDataFromADC(OBDtoADCObject object, Timestamp occurTime,
			ArrayList<OBDtoMonTrafficVSvcMemberInfo> alteonMemberList) throws OBException {
		ArrayList<OBDtoMonTrafficVSvcInfo> retVal = new ArrayList<OBDtoMonTrafficVSvcInfo>();

		if (object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
			return retVal;

		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				retVal = convertVSVCMemberToVSVCForAlteon(object.getIndex(), occurTime, alteonMemberList);
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());// new OBSnmpF5(ipaddress);
				ArrayList<OBDtoMonTrafficVServer> list = snmp.getTrafficVServer(adcInfo.getAdcType(),
						adcInfo.getSwVersion());
				for (OBDtoMonTrafficVServer info : list) {
					OBDtoMonTrafficVSvcInfo obj = new OBDtoMonTrafficVSvcInfo();

					obj.setAdcIndex(object.getIndex());
					obj.setOccurTime(occurTime);
					String vsIndex = OBCommon.makeVSIndex(object.getIndex(), info.getName());
					obj.setObjType(OBDtoMonTrafficVSvcInfo.SLBLAYER_SERVER);
					obj.setObjIndex(vsIndex);

					// 트래픽 정보 저장.
					obj.setCurConns(info.getCurConns());
					obj.setMaxConns(info.getMaxConns());
					obj.setTotConns(info.getTotConns());
					obj.setPktsIn(info.getPktsIn());
					obj.setPktsOut(info.getPktsOut());
					obj.setBytesIn(info.getBytesIn());
					obj.setBytesOut(info.getBytesOut());

					retVal.add(obj);
				}
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				OBSnmpPAS snmp = OBCommon.getValidSnmpPASHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				ArrayList<OBDtoMonTrafficVServer> list = snmp.getTrafficVServer(adcInfo.getAdcType(),
						adcInfo.getSwVersion());
				for (OBDtoMonTrafficVServer info : list) {
					OBDtoMonTrafficVSvcInfo obj = new OBDtoMonTrafficVSvcInfo();

					obj.setAdcIndex(object.getIndex());
					obj.setOccurTime(occurTime);
					String vsIndex = OBCommon.makeVSIndex(object.getIndex(), info.getName());
					obj.setObjType(OBDtoMonTrafficVSvcInfo.SLBLAYER_SERVER);
					obj.setObjIndex(vsIndex);

					// 트래픽 정보 저장.
					obj.setCurConns(info.getCurConns());
					obj.setMaxConns(info.getMaxConns());
					obj.setTotConns(info.getTotConns());
					obj.setPktsIn(info.getPktsIn());
					obj.setPktsOut(info.getPktsOut());
					obj.setBytesIn(info.getBytesIn());
					obj.setBytesOut(info.getBytesOut());

					retVal.add(obj);
				}
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				OBSnmpPASK snmp = OBCommon.getValidSnmpPASKHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
						adcInfo.getSnmpInfo());
				ArrayList<OBDtoMonTrafficVServer> list = snmp.getTrafficVServer(adcInfo.getAdcType(),
						adcInfo.getSwVersion());
				for (OBDtoMonTrafficVServer info : list) {
					OBDtoMonTrafficVSvcInfo obj = new OBDtoMonTrafficVSvcInfo();

					obj.setAdcIndex(object.getIndex());
					obj.setOccurTime(occurTime);
					String vsIndex = OBCommon.makeVSIndex(object.getIndex(), info.getName());
					obj.setObjType(OBDtoMonTrafficVSvcInfo.SLBLAYER_SERVER);
					obj.setObjIndex(vsIndex);

					// 트래픽 정보 저장.
					obj.setCurConns(info.getCurConns());
					obj.setMaxConns(info.getMaxConns());
					obj.setTotConns(info.getTotConns());
					obj.setPktsIn(info.getPktsIn());
					obj.setPktsOut(info.getPktsOut());
					obj.setBytesIn(info.getBytesIn());
					obj.setBytesOut(info.getBytesOut());

					retVal.add(obj);
				}
			} else {
				return retVal;
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}

		return retVal;
	}

	public ArrayList<OBDtoMonTrafficVSvcMemberInfo> getVSvcMemberPerfDataFromADC(OBDtoADCObject object,
			Timestamp occurTime) throws OBException {
		// Alteon은 bytes-in만 유효값이다.
		ArrayList<OBDtoMonTrafficVSvcMemberInfo> retVal = new ArrayList<OBDtoMonTrafficVSvcMemberInfo>();

		if (object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
			return retVal;

		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {
				// OBSnmpAlteon snmp =
				// OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(),
				// adcInfo.getAdcIpAddress());
				ArrayList<OBDtoMonTrafficServiceAlteon> list = new OBAdcMonitorAlteon().getVServiceTrafficAlteon(
						adcInfo.getIndex(), adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(),
						adcInfo.getAdcPassword(), adcInfo.getSwVersion());
				for (OBDtoMonTrafficServiceAlteon info : list) {
					OBDtoMonTrafficVSvcMemberInfo obj = new OBDtoMonTrafficVSvcMemberInfo();

					obj.setAdcIndex(object.getIndex());
					obj.setOccurTime(occurTime);
					String nodeIndex = OBCommon.makeNodeIndexAlteon(adcInfo.getIndex(), info.getNodeID());
					String poolIndex = info.getPoolIndex();// OBCommon.makePoolIndex(adcInfo.getIndex(),
															// adcInfo.getAdcType(), info.getPoolName(),
															// info.getPoolIndex());//info.getPoolIndex();//
					String poolMemberIndex = OBCommon.makePoolMemberIndex(object.getIndex(), info.getPoolIndex(),
							nodeIndex, 0);
					String vsvcIndex = OBCommon.makeVSvcIndex(object.getIndex(), info.getVsIndex(), info.getSrvPort());
					String objIndex = OBCommon.makeVSPoolMemberIndex(vsvcIndex, poolMemberIndex);

					obj.setVsIndex(vsvcIndex);
					obj.setSvcPort(info.getSrvPort());
					obj.setPoolIndex(poolIndex);
					obj.setPoolMemberIndex(poolMemberIndex);
					obj.setObjIndex(objIndex);

					// 트래픽 정보 저장.
					obj.setCurConns(info.getCurConns());
					obj.setMaxConns(info.getMaxConns());
					obj.setTotConns(info.getTotConns());
					obj.setPktsIn(-1L);
					obj.setPktsOut(-1L);
					obj.setBytesIn(info.getBytesIn());
					obj.setBytesOut(-1L);

					retVal.add(obj);
				}
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5) {
				// OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(),
				// adcInfo.getAdcIpAddress());//new OBSnmpF5(ipaddress);
				ArrayList<OBDtoMonTrafficPoolMembers> list = new OBAdcMonitorF5()
						.getPoolMembersTrafficF5(adcInfo.getIndex(), adcInfo.getAdcIpAddress());
				for (OBDtoMonTrafficPoolMembers info : list) {
					OBDtoMonTrafficVSvcMemberInfo obj = new OBDtoMonTrafficVSvcMemberInfo();

					obj.setAdcIndex(object.getIndex());
					obj.setOccurTime(occurTime);
					String nodeIndex = OBCommon.makeNodeIndexF5(adcInfo.getIndex(), info.getNodeAddress());
					String poolIndex = info.getPoolIndex();// OBCommon.makePoolIndex(adcInfo.getIndex(),
															// adcInfo.getAdcType(), info.getPoolName(),
															// info.getPoolIndex());
					String poolMemberIndex = OBCommon.makePoolMemberIndex(object.getIndex(), poolIndex, nodeIndex,
							info.getNodePort());
					String vsvcIndex = info.getVsIndex();
					String objIndex = OBCommon.makeVSPoolMemberIndex(vsvcIndex, poolMemberIndex);

					obj.setVsIndex(vsvcIndex);
					obj.setSvcPort(info.getSrvPort());
					obj.setPoolIndex(poolIndex);
					obj.setPoolMemberIndex(poolMemberIndex);
					obj.setObjIndex(objIndex);

					// 트래픽 정보 저장.
					obj.setCurConns(info.getCurConns());
					obj.setMaxConns(info.getMaxConns());
					obj.setTotConns(info.getTotConns());
					obj.setPktsIn(info.getPktsIn());
					obj.setPktsOut(info.getPktsOut());
					obj.setBytesIn(info.getBytesIn());
					obj.setBytesOut(info.getBytesOut());

					retVal.add(obj);
				}
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS) {
				ArrayList<OBDtoMonTrafficPoolMembers> list = new OBAdcMonitorPAS()
						.getPoolMembersTrafficPAS(adcInfo.getIndex(), adcInfo.getAdcIpAddress());
				for (OBDtoMonTrafficPoolMembers info : list) {
					OBDtoMonTrafficVSvcMemberInfo obj = new OBDtoMonTrafficVSvcMemberInfo();

					obj.setAdcIndex(object.getIndex());
					obj.setOccurTime(occurTime);
					String nodeIndex = OBCommon.makeNodeIndexPAS(adcInfo.getIndex(), info.getPoolName(),
							info.getNodeID());
					String poolIndex = info.getPoolIndex();// OBCommon.makePoolIndex(adcInfo.getIndex(),
															// adcInfo.getAdcType(), info.getPoolName(),
															// info.getPoolIndex());
					String poolMemberIndex = OBCommon.makePoolMemberIndex(object.getIndex(), poolIndex, nodeIndex,
							info.getNodePort());
					String vsvcIndex = info.getVsIndex();
					String objIndex = OBCommon.makeVSPoolMemberIndex(vsvcIndex, poolMemberIndex);

					obj.setVsIndex(vsvcIndex);
					obj.setSvcPort(info.getSrvPort());
					obj.setPoolIndex(poolIndex);
					obj.setPoolMemberIndex(poolMemberIndex);
					obj.setObjIndex(objIndex);

					// 트래픽 정보 저장.
					obj.setCurConns(info.getCurConns());
					obj.setMaxConns(info.getMaxConns());
					obj.setTotConns(info.getTotConns());
					obj.setPktsIn(info.getPktsIn());
					obj.setPktsOut(info.getPktsOut());
					obj.setBytesIn(info.getBytesIn());
					obj.setBytesOut(info.getBytesOut());

					retVal.add(obj);
				}
			} else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK) {
				ArrayList<OBDtoMonTrafficPoolMembers> list = new OBAdcMonitorPASK()
						.getPoolMembersTrafficPASK(adcInfo.getIndex(), adcInfo.getAdcIpAddress());
				for (OBDtoMonTrafficPoolMembers info : list) {
					OBDtoMonTrafficVSvcMemberInfo obj = new OBDtoMonTrafficVSvcMemberInfo();

					obj.setAdcIndex(object.getIndex());
					obj.setOccurTime(occurTime);
					String nodeIndex = OBCommon.makeNodeIndexPASK(adcInfo.getIndex(), info.getNodeID());
					String poolIndex = info.getPoolIndex();// OBCommon.makePoolIndex(adcInfo.getIndex(),
															// adcInfo.getAdcType(), info.getPoolName(),
															// info.getPoolIndex());
					String poolMemberIndex = OBCommon.makePoolMemberIndex(object.getIndex(), poolIndex, nodeIndex,
							info.getSrvPort());
//                    OBSystemLog.info("/opt/adcsmart/logs/rock.log", String.format("poolmember Index : %s, objindex : %d", 
//                            poolMemberIndex, object.getIndex()));
					String vsvcIndex = info.getVsIndex();
					String objIndex = OBCommon.makeVSPoolMemberIndex(vsvcIndex, poolMemberIndex);

					obj.setVsIndex(vsvcIndex);
					obj.setSvcPort(info.getSrvPort());
					obj.setPoolIndex(poolIndex);
					obj.setPoolMemberIndex(poolMemberIndex);
					obj.setObjIndex(objIndex);

					// 트래픽 정보 저장.
					obj.setCurConns(info.getCurConns());
					obj.setMaxConns(info.getMaxConns());
					obj.setTotConns(info.getTotConns());
					obj.setPktsIn(info.getPktsIn());
					obj.setPktsOut(info.getPktsOut());
					obj.setBytesIn(info.getBytesIn());
					obj.setBytesOut(info.getBytesOut());

					retVal.add(obj);
				}
			} else {
				return retVal;
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}

		return retVal;
	}

	public ArrayList<OBDtoMonTrafficVSvcInfo> getVSvcGroupPerfDataFromADC(OBDtoADCObject object, Timestamp occurTime)
			throws OBException {
		ArrayList<OBDtoMonTrafficVSvcInfo> retVal = new ArrayList<OBDtoMonTrafficVSvcInfo>();

		if (object.getCategory() != OBDtoADCObject.CATEGORY_ADC)
			return retVal;

		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {// alteon만 추출한다.
				OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(),
						adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
				ArrayList<OBDtoMonTrafficVServer> list = snmp.getTrafficVServer(adcInfo.getAdcType(),
						adcInfo.getSwVersion());
				for (OBDtoMonTrafficVServer info : list) {
					OBDtoMonTrafficVSvcInfo obj = new OBDtoMonTrafficVSvcInfo();

					obj.setAdcIndex(object.getIndex());
					obj.setOccurTime(occurTime);
					String vsIndex = OBCommon.makeVSIndex(object.getIndex(), info.getAlteonID());
					obj.setObjType(OBDtoMonTrafficVSvcInfo.SLBLAYER_SERVER);
					obj.setObjIndex(vsIndex);

					// 트래픽 정보 저장.
					obj.setCurConns(info.getCurConns());
					obj.setMaxConns(info.getMaxConns());
					obj.setTotConns(info.getTotConns());
					obj.setPktsIn(info.getPktsIn());
					obj.setPktsOut(info.getPktsOut());
					obj.setBytesIn(info.getBytesIn());
					obj.setBytesOut(info.getBytesOut());

					retVal.add(obj);
				}
			} else {
				return retVal;
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}

		return retVal;
	}

	public ArrayList<OBDtoMonTrafficPoolGroup> getPoolGroupPerfDataFromADC(int adcIndex, Timestamp occurTime)
			throws OBException {
		ArrayList<OBDtoMonTrafficPoolGroup> retVal = new ArrayList<OBDtoMonTrafficPoolGroup>();
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) {// alteon만 작업한다.
				ArrayList<OBDtoAdcPoolSimple> groupList = new OBVServerDB().getFlbGroupSelected(adcInfo.getIndex());
				OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(),
						adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
				retVal = snmp.getTrafficPoolGroup(adcInfo.getAdcType(), adcInfo.getSwVersion(), groupList);
				for (OBDtoMonTrafficPoolGroup trafficInfo : retVal) {
					trafficInfo.setOccurTime(occurTime);
				}
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	// flb 모니터링 대상 그룹이라고 지정한 group의 real 트래픽만 구한다.
	public ArrayList<OBDtoMonTrafficPoolGroupMember> getRealPerfDataFromADC(Integer adcIndex, Timestamp occurTime)
			throws OBException {
		ArrayList<OBDtoMonTrafficPoolGroupMember> retVal = new ArrayList<OBDtoMonTrafficPoolGroupMember>();
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) // Alteon만 한다.
			{
				OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(),
						adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
				ArrayList<OBDtoAdcNodeSimple> realList = new OBVServerDB()
						.getRealInFlbSelectedGroups(adcInfo.getIndex()); // 선택된 모니터링 그룹의 real을 구한다.
				retVal = snmp.getTrafficReal(adcInfo.getAdcType(), adcInfo.getSwVersion(), realList);
				for (OBDtoMonTrafficPoolGroupMember info : retVal) {
					info.setOccurTime(occurTime);
				}
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	// Alteon만 쓰고 잇다. //AAAAA
	public OBDtoMonTrafficAdc getAdcConnectionFromADC(Integer adcIndex, Timestamp occurTime) throws OBException {
		OBDtoMonTrafficAdc retVal = new OBDtoMonTrafficAdc();
		try {
			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON) // Alteon만 한다.
			{
				OBSnmpAlteon snmp = OBCommon.getValidSnmpAlteonHandler(adcInfo.getSwVersion(),
						adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo());
				retVal = snmp.getAdcConnection(adcInfo.getAdcType(), adcInfo.getSwVersion());
				retVal.setAdcIndex(adcIndex);
				retVal.setOccurTime(occurTime);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
		return retVal;
	}

	// public static void main(String[] args) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// db.openDB();
	//
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// object.setIndex(29);
	//
	// OBDtoSearch searchObj = new OBDtoSearch();
	// OBDtoOrdering orderObj = new OBDtoOrdering();
	// ArrayList<OBDtoL2SearchOption> searchKeyList = new
	// ArrayList<OBDtoL2SearchOption>();
	//
	// ArrayList<OBDtoL2SearchInfo>list= new
	// OBFaultMonitoringImpl().searchL2InfoList(object, searchKeyList, searchObj);
	// for(OBDtoL2SearchInfo info: list)
	// System.out.println(info);
	// db.closeDB();
	// }

	@Override
	public ArrayList<OBDtoL2SearchInfo> searchL2InfoList(OBDtoADCObject object,
			ArrayList<OBDtoL2SearchOption> searchKeyList, OBDtoSearch pagingOption) throws OBException {
		ArrayList<OBDtoL2SearchInfo> retVal = new ArrayList<OBDtoL2SearchInfo>();
		try {
			ArrayList<Integer> adcIndexList = new ArrayList<Integer>();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ADC) {
				adcIndexList.add(object.getIndex());
			} else {
				if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
					adcIndexList = new OBFaultMngImpl().getAdcListAll(0);
				} else {
					adcIndexList = new OBFaultMngImpl().getAdcList(object.getIndex());
				}
			}

			ArrayList<OBDtoL2SearchInfo> searchVal = retrieveL2SearchInfoListGroup(adcIndexList, searchKeyList);

			Timestamp occurTime = OBDateTime.toTimestamp(OBDateTime.now());

			// 수집된 로그를 db에 저장한다.
			new OBFaultMonitoringDB().writeL2SearchInfoList(occurTime, adcIndexList, searchVal);

			retVal = searchL2InfoList(object, pagingOption, null);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return retVal;
	}

	private ArrayList<OBDtoL2SearchInfo> retrieveL2SearchInfoListGroup(ArrayList<Integer> adcIndexList,
			ArrayList<OBDtoL2SearchOption> searchKeyList) throws OBException {
		ArrayList<OBDtoL2SearchInfo> retVal = new ArrayList<OBDtoL2SearchInfo>();

		try {
			for (Integer adcIndex : adcIndexList) {
				OBDtoADCObject obj = new OBDtoADCObject();
				obj.setCategory(OBDtoADCObject.CATEGORY_ADC);
				obj.setIndex(adcIndex);
				ArrayList<OBDtoL2SearchInfo> sessionList = retrieveL2SearchInfoListADC(obj, searchKeyList);
				retVal.addAll(sessionList);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return retVal;
	}

	private ArrayList<OBDtoL2SearchInfo> retrieveL2SearchInfoListADC(OBDtoADCObject object,
			ArrayList<OBDtoL2SearchOption> searchKeyList) throws OBException {
		ArrayList<OBDtoL2SearchInfo> retVal = new ArrayList<OBDtoL2SearchInfo>();
		try {

			OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());
			if (adcInfo == null) {
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to get adcInfo:%d", object.getIndex()));
				return retVal;
			}

			if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
				return retrieveL2SearchInfoListAlteon(adcInfo, searchKeyList);
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_F5)
				return retrieveL2SearchInfoListF5(adcInfo, searchKeyList);
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PAS)
				return retrieveL2SearchInfoListPAS(adcInfo, searchKeyList);
			else if (adcInfo.getAdcType() == OBDefine.ADC_TYPE_PIOLINK_PASK)
				return retrieveL2SearchInfoListPASK(adcInfo, searchKeyList);

		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return retVal;
	}

	private ArrayList<OBDtoL2SearchInfo> retrieveL2SearchInfoListAlteon(OBDtoAdcInfo adcInfo,
			ArrayList<OBDtoL2SearchOption> searchKeyList) throws OBException {
		ArrayList<OBDtoL2SearchInfo> retVal = new ArrayList<OBDtoL2SearchInfo>();
		try {
			// search option 계산.
			String optIPAddress = "";
			String optMacAddress = "";
			String optVlanInfo = "";
			String optInterfaceInfo = "";
			for (OBDtoL2SearchOption option : searchKeyList) {
				switch (option.getType()) {
				case OBDtoL2SearchOption.OPTION_TYPE_INTERFACE:
					optInterfaceInfo = option.getContent();
					break;
				case OBDtoL2SearchOption.OPTION_TYPE_IP_ADDR:
					optIPAddress = option.getContent();
					break;
				case OBDtoL2SearchOption.OPTION_TYPE_MAC_ADDR:
					optMacAddress = option.getContent();
					break;
				case OBDtoL2SearchOption.OPTION_TYPE_VLAN:
					optVlanInfo = option.getContent();
					break;
				default:
					break;
				}
			}

			// 조회...
			ArrayList<OBDtoArpInfo> arpInfoList = OBCommon
					.getValidSnmpAlteonHandler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo())
					.getArpInfo(adcInfo.getAdcType(), adcInfo.getSwVersion());
			for (OBDtoArpInfo arpInfo : arpInfoList) {
				if (!optIPAddress.isEmpty()
						&& !arpInfo.getDstIPAddr().toLowerCase().contains(optIPAddress.toLowerCase()))
					continue;

				if (!optMacAddress.isEmpty()
						&& !arpInfo.getMacAddr().toLowerCase().contains(optMacAddress.toLowerCase()))
					continue;

				if (!optVlanInfo.isEmpty() && !arpInfo.getVlanInfo().toLowerCase().contains(optVlanInfo.toLowerCase()))
					continue;

				if (!optInterfaceInfo.isEmpty()
						&& !arpInfo.getPortNum().toLowerCase().contains(optInterfaceInfo.toLowerCase()))
					continue;

				OBDtoL2SearchInfo obj = new OBDtoL2SearchInfo();

				obj.setAdcIndex(adcInfo.getIndex());
				obj.setInterfaceInfo(arpInfo.getPortNum());
				obj.setIpAddress(arpInfo.getDstIPAddr());
				obj.setMacAddress(arpInfo.getMacAddr());
				obj.setVlanInfo(arpInfo.getVlanInfo());
				retVal.add(obj);
			}
		} catch (Exception e) {
			throw new OBException(e.getMessage());
		}

		return retVal;
	}

	private ArrayList<OBDtoL2SearchInfo> retrieveL2SearchInfoListF5(OBDtoAdcInfo adcInfo,
			ArrayList<OBDtoL2SearchOption> searchKeyList) throws OBException {//
		ArrayList<OBDtoL2SearchInfo> retVal = new ArrayList<OBDtoL2SearchInfo>();
		try {
			// search option 계산.
			String optIPAddress = "";
			String optMacAddress = "";
			String optVlanInfo = "";
			String optInterfaceInfo = "";
			for (OBDtoL2SearchOption option : searchKeyList) {
				switch (option.getType()) {
				case OBDtoL2SearchOption.OPTION_TYPE_INTERFACE:
					optInterfaceInfo = option.getContent();
					break;
				case OBDtoL2SearchOption.OPTION_TYPE_IP_ADDR:
					optIPAddress = option.getContent();
					break;
				case OBDtoL2SearchOption.OPTION_TYPE_MAC_ADDR:
					optMacAddress = option.getContent();
					break;
				case OBDtoL2SearchOption.OPTION_TYPE_VLAN:
					optVlanInfo = option.getContent();
					break;
				default:
					break;
				}
			}

			// 조회...
			OBAdcF5Handler handler = OBCommon.getValidF5Handler(adcInfo.getSwVersion());
			handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcCliAccount(),
					adcInfo.getAdcCliPasswordDecrypt(), adcInfo.getConnPort());
			handler.sshLogin();
			String cliMsg = handler.cmndInfoArp();
			String fdbCliMsg = handler.cmndInfoFdb();
			handler.disconnect();

			OBSnmpF5 snmpHandler = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());
			HashMap<String, String> vlanMap = snmpHandler.getVlanInterfaceName(adcInfo.getAdcType(),
					adcInfo.getSwVersion());

			// ssh로 입력 받은 데이터를 파싱한다.
			OBCLIParserF5 f5Parser = OBCommon.getValidF5CLIParser(adcInfo.getSwVersion());
			String swVersion[] = adcInfo.getSwVersion().split("\\.");
			ArrayList<OBDtoArpInfo> arpInfoList = f5Parser.parseInfoArp(cliMsg);
			ArrayList<OBDtoFdbInfo> fdbInfoList = null;

			// 10Version Check
			if (swVersion[0].equals("10")) {
				fdbInfoList = f5Parser.parseInfoFdbV10(fdbCliMsg);
			} else {
				fdbInfoList = f5Parser.parseInfoFdb(fdbCliMsg);
			}

			for (OBDtoArpInfo arpInfo : arpInfoList) {
				if (!optIPAddress.isEmpty()
						&& !arpInfo.getDstIPAddr().toLowerCase().contains(optIPAddress.toLowerCase()))
					continue;

				if (!optMacAddress.isEmpty()
						&& !arpInfo.getMacAddr().toLowerCase().contains(optMacAddress.toLowerCase()))
					continue;

				if (!optVlanInfo.isEmpty()) {
					if (!arpInfo.getVlanInfo().toLowerCase().contains(optVlanInfo.toLowerCase()))
						continue;
				}

				String portName = vlanMap.get(arpInfo.getVlanInfo());
				if (portName != null && !portName.isEmpty()) {
					if (!optInterfaceInfo.isEmpty()
							&& !portName.toLowerCase().contains(optInterfaceInfo.toLowerCase())) {
						continue;
					}
				}

				OBDtoL2SearchInfo obj = new OBDtoL2SearchInfo();

				obj.setAdcIndex(adcInfo.getIndex());
				obj.setIpAddress(arpInfo.getDstIPAddr());
				obj.setMacAddress(convertMacAddress(arpInfo.getMacAddr()));
				obj.setVlanInfo(arpInfo.getVlanInfo());
				if (portName != null && !portName.isEmpty())
					obj.setInterfaceInfo(portName);
				for (OBDtoFdbInfo fdbInfo : fdbInfoList) {
					if (arpInfo.getMacAddr().equals(fdbInfo.getMacAddress())) {
						obj.setInterfaceInfo(fdbInfo.getPort());
					}
				}
				retVal.add(obj);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
		return retVal;
	}

	// 4:f4:bc:c:22:d1 =-> 04:f4:bc:0c:22:d1 로 변경..
	private String convertMacAddress(String macAddr) {
		String retVal = "";

		String element[] = macAddr.split(":"); // " "(공백으로 구분한다. 위의 Parser에서 멀티 스패이스로 공백처리 이미 함)
		if (element.length != 6)
			return macAddr;
		for (int i = 0; i < 6; i++) {
			String item = element[i];
			switch (item.length()) {
			case 1:
				if (!retVal.isEmpty())
					retVal += ":";
				retVal += "0" + item.toUpperCase();
				break;
			case 2:
				if (!retVal.isEmpty())
					retVal += ":";
				retVal += item.toUpperCase();
				break;
			default:
				if (!retVal.isEmpty())
					retVal += ":";
				retVal += item.toUpperCase();
				break;
			}
		}
		return retVal;
	}

	private ArrayList<OBDtoL2SearchInfo> retrieveL2SearchInfoListPAS(OBDtoAdcInfo adcInfo,
			ArrayList<OBDtoL2SearchOption> searchKeyList) throws OBException {// TODO
		ArrayList<OBDtoL2SearchInfo> retVal = new ArrayList<OBDtoL2SearchInfo>();
		return retVal;
	}

	private ArrayList<OBDtoL2SearchInfo> retrieveL2SearchInfoListPASK(OBDtoAdcInfo adcInfo,
			ArrayList<OBDtoL2SearchOption> searchKeyList) throws OBException {// TODO
		ArrayList<OBDtoL2SearchInfo> retVal = new ArrayList<OBDtoL2SearchInfo>();
		return retVal;
	}

	@Override
	public ArrayList<OBDtoL2SearchInfo> searchL2InfoListBySort(OBDtoADCObject object, OBDtoSearch pagingOption,
			OBDtoOrdering orderOption) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoL2SearchInfo> retVal = new ArrayList<OBDtoL2SearchInfo>();
		try {
			db.openDB();
			retVal = searchL2InfoList(object, pagingOption, orderOption);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private String searchL2InfoListOrderType(OBDtoOrdering orderObj) throws OBException {
		if (orderObj == null)
			return "";
		String retVal = " ORDER BY INET(IP_ADDRESS) ASC NULLS LAST ";
		int orderDir = orderObj.getOrderDirection();
		switch (orderObj.getOrderType()) {
		case OBDtoOrdering.TYPE_1FIRST:// IP_ADDRESS
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(IP_ADDRESS) ASC NULLS LAST, MAC_ADDRESS ASC NULLS LAST ";
			else
				retVal = " ORDER BY INET(IP_ADDRESS) DESC NULLS LAST, MAC_ADDRESS ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_2SECOND:// MAC_ADDRESS
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY MAC_ADDRESS ASC NULLS LAST, INET(IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY MAC_ADDRESS DESC NULLS LAST, INET(IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_3THIRD:// VLAN_INFO
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY VLAN_INFO ASC NULLS LAST, INET(IP_ADDRESS) NULLS LAST ";
			else
				retVal = " ORDER BY VLAN_INFO DESC NULLS LAST, INET(IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_4FOURTH: // INTERFACE
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INTERFACE ASC NULLS LAST, INET(IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY INTERFACE DESC NULLS LAST, INET(IP_ADDRESS) ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	// public static void main(String[] args) throws OBException
	// {
	// OBDatabase db = new OBDatabase();
	//
	// db.openDB();
	//
	// OBDtoADCObject object = new OBDtoADCObject();
	// object.setCategory(OBDtoADCObject.CATEGORY_ADC);
	// object.setDesciption("전체 대상 스케줄.");
	// object.setName("total schedule");
	// object.setIndex(29);
	//
	// OBDtoSearch searchObj = new OBDtoSearch();
	// OBDtoOrdering orderObj = new OBDtoOrdering();
	//
	// ArrayList<OBDtoL2SearchInfo>list= new
	// OBFaultMonitoringImpl().searchL2InfoList(object, searchObj, orderObj, db);
	// System.out.println(list);
	// db.closeDB();
	// }

	public ArrayList<OBDtoL2SearchInfo> searchL2InfoList(OBDtoADCObject object, OBDtoSearch searchObj,
			OBDtoOrdering orderObj) throws OBException {
		ArrayList<OBDtoL2SearchInfo> retVal = new ArrayList<OBDtoL2SearchInfo>();
		String sqlText = "";
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			// adcIndex 추출.
			ArrayList<Integer> adcIndexList = new ArrayList<Integer>();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ADC) {
				adcIndexList.add(object.getIndex());
			} else {
				if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
					adcIndexList = new OBFaultMngImpl().getAdcListAll(0);
				} else {
					adcIndexList = new OBFaultMngImpl().getAdcList(object.getIndex());
				}
			}
			String adcIndexListText = "-1";
			for (Integer adcIndex : adcIndexList) {
				adcIndexListText += ", " + adcIndex;
			}
			int offset = 0;
			if (searchObj != null && searchObj.getBeginIndex() != null)
				offset = searchObj.getBeginIndex().intValue();
			if (offset < 0)
				offset = 0;

			int limit = 0;
			if (searchObj != null && searchObj.getEndIndex() != null)
				limit = Math.abs(searchObj.getEndIndex().intValue() - offset) + 1;
			String sqlLimit = "";
			if (limit != 0)
				sqlLimit = String.format(" LIMIT %d ", limit);
			String sqlOffset = String.format(" OFFSET %d ", offset);

			sqlText = String.format(
					" SELECT ADC_INDEX, IP_ADDRESS, MAC_ADDRESS, VLAN_INFO, INTERFACE   \n"
							+ " FROM TMP_L2_SEARCH_INFO                                           \n"
							+ " WHERE ADC_INDEX IN ( %s )                                         \n",
					adcIndexListText);

			sqlText += searchL2InfoListOrderType(orderObj);

			if (sqlLimit != null)
				sqlText += sqlLimit;
			if (sqlOffset != null)
				sqlText += sqlOffset;

			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {

				OBDtoL2SearchInfo obj = new OBDtoL2SearchInfo();
				obj.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				obj.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				obj.setMacAddress(db.getString(rs, "MAC_ADDRESS"));
				obj.setVlanInfo(db.getString(rs, "VLAN_INFO"));
				obj.setInterfaceInfo(db.getString(rs, "INTERFACE"));

				retVal.add(obj);
			}

			return retVal;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public Integer getL2InfoTotalCount(OBDtoADCObject object) throws OBException {
		Integer retVal = 0;
		String sqlText = "";

		final OBDatabase db = new OBDatabase();
		try {
			// adcIndex 추출.
			ArrayList<Integer> adcIndexList = new ArrayList<Integer>();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ADC) {
				adcIndexList.add(object.getIndex());
			} else {
				if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
					adcIndexList = new OBFaultMngImpl().getAdcListAll(0);
				} else {
					adcIndexList = new OBFaultMngImpl().getAdcList(object.getIndex());
				}
			}
			String adcIndexListText = "-1";
			for (Integer adcIndex : adcIndexList) {
				adcIndexListText += ", " + adcIndex;
			}

			sqlText = String.format(" SELECT COUNT(ADC_INDEX) AS CNT      \n"
					+ " FROM TMP_L2_SEARCH_INFO             \n" + " WHERE ADC_INDEX IN ( %s )           \n",
					adcIndexListText);

			db.openDB();
			ResultSet rs = db.executeQuery(sqlText);

			if (rs.next() == true) {
				retVal = db.getInteger(rs, "CNT");
			}

			return retVal;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public OBDtoFaultDataObj getADCMonHttpReqestHistory(OBDtoADCObject object, OBDtoSearch searchOption)
			throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultDataObj retVal = null;
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getADCMonHttpReqestHistory(object, searchOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public OBDtoFaultDataObj getADCMonSSLTransactionHistory(OBDtoADCObject object, OBDtoSearch searchOption)
			throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultDataObj retVal = null;
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// TODO
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// TODO
			} else {
				retVal = new OBFaultMonitoringDB().getADCMonSSLTransactionHistory(object, searchOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public void cleanLocalSessionList(OBDtoADCObject object) throws OBException {
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			ArrayList<Integer> adcIndexList = new ArrayList<Integer>();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ADC) {
				adcIndexList.add(object.getIndex());
			} else {
				if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
					adcIndexList = new OBFaultMngImpl().getAdcListAll(0);
				} else {
					adcIndexList = new OBFaultMngImpl().getAdcList(object.getIndex());
				}
			}
			new OBFaultMonitoringDB().deleteSessionLog(adcIndexList);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	@Override
	public void cleanLocalL2List(OBDtoADCObject object) throws OBException {
		try {
			ArrayList<Integer> adcIndexList = new ArrayList<Integer>();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ADC) {
				adcIndexList.add(object.getIndex());
			} else {
				if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
					adcIndexList = new OBFaultMngImpl().getAdcListAll(0);
				} else {
					adcIndexList = new OBFaultMngImpl().getAdcList(object.getIndex());
				}
			}
			new OBFaultMonitoringDB().deleteL2SearchInfoList(adcIndexList);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		}
	}

	/**
	 * 서비스에 할당된 멤버의 개수를 제공한다.
	 * 
	 * @param object       . 지정된 service의 index 정보가 전달된다.()
	 * @param searchOption
	 * @param db
	 * @return
	 * @throws OBException
	 */

	@Override
	public Integer getSvcPerfVSrvMemberTotalCount(OBDtoADCObject object, OBDtoSearch searchOption) throws OBException {
		String sqlText = "";
		int result = 0;
		final OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_VSVC) {// TMP_SLB_POOLMEMBER에서 추출 한다.
				sqlText = " SELECT COUNT(*) AS CNT FROM TMP_SLB_POOLMEMBER A INNER JOIN TMP_SLB_VS_SERVICE B ON B.POOL_INDEX = A.POOL_INDEX WHERE B.INDEX = "
						+ OBParser.sqlString(object.getStrIndex());
			} else {
				sqlText = " SELECT COUNT(*) AS CNT FROM TMP_SLB_POOLMEMBER A INNER JOIN TMP_SLB_VSERVER B ON B.POOL_INDEX = A.POOL_INDEX WHERE B.INDEX = "
						+ OBParser.sqlString(object.getStrIndex());
			}

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true)
				result = db.getInteger(rs, "CNT");
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
		return result;
	}

	@Override
	public OBDtoFaultPreBpsConnChart getSvcPerfVSrvMemberHistory(OBDtoADCObject object, OBDtoSearch searchOption)
			throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultPreBpsConnChart retVal = new OBDtoFaultPreBpsConnChart();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// retVal = getSvcPerfVSrvMemberInfoAll(object, searchOption, orderOption, db);
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// retVal = getSvcPerfVSrvMemberInfoGroup(object, searchOption, orderOption,
				// db);
			} else {
				// if(object.getCategory() == OBDtoADCObject.CATEGORY_VSVC) // Alteon
				retVal = new OBFaultMonitoringDB().getSvcPerfVSrvMemberChartHistory(object, searchOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	@Override
	public OBDtoFaultPreBpsConnChart getSvcPerfVSrvMemberMaxAvgHistory(OBDtoADCObject object, OBDtoSearch searchOption)
			throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoFaultPreBpsConnChart retVal = new OBDtoFaultPreBpsConnChart();
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// retVal = getSvcPerfVSrvMemberInfoAll(object, searchOption, orderOption, db);
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// retVal = getSvcPerfVSrvMemberInfoGroup(object, searchOption, orderOption,
				// db);
			} else {
				// if(object.getCategory() == OBDtoADCObject.CATEGORY_VSVC) // Alteon
				retVal = new OBFaultMonitoringDB().getSvcPerfVSrvMemberChartMaxAvgHistory(object, searchOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private String makeTimeSqlText(OBDtoSearch searchOption, String columnName) throws OBException {
		String retVal = "";

		if (searchOption == null)
			return retVal;

		if (searchOption.getToTime() == null)
			searchOption.setToTime(new Date());
		retVal = String.format(" %s <= %s ", columnName,
				OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getToTime().getTime()))));

		if (searchOption.getFromTime() == null) {
			searchOption.setFromTime(new Date(searchOption.getToTime().getTime() - 7 * 24 * 60 * 60 * 1000));// 7일전 시간.
		}

		retVal += String.format(" AND %s >= %s ", columnName,
				OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getFromTime().getTime()))));

		return retVal;
	}

	@Override
	public OBDtoTrafficMapVServiceMembers getSvcPerfVSrvMemberInfo(OBDtoADCObject object, OBDtoSearch searchOption,
			OBDtoOrdering orderOption) throws OBException {
		OBDatabase db = new OBDatabase();
		OBDtoTrafficMapVServiceMembers retVal = null;
		try {
			db.openDB();
			if (object.getCategory() == OBDtoADCObject.CATEGORY_ALL) {
				// retVal = getSvcPerfVSrvMemberInfoAll(object, searchOption, orderOption, db);
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_GROUP) {
				// retVal = getSvcPerfVSrvMemberInfoGroup(object, searchOption, orderOption,
				// db);
			} else {
				// if(object.getCategory() == OBDtoADCObject.CATEGORY_VSVC) // Alteon
				retVal = getSvcPerfVSrvMemberInfo(object, searchOption, orderOption, db);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);// );
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private String getSvcPerfVSrvMemberListOrderType(OBDtoOrdering orderObj) throws OBException {
		if (orderObj == null)
			return "";
		String retVal = " ORDER BY INET(A.IP_ADDRESS) ASC NULLS LAST, A.PORT ASC NULLS LAST ";
		int orderDir = orderObj.getOrderDirection();
		switch (orderObj.getOrderType()) {
		case OBDtoOrdering.TYPE_1FIRST:// IP_ADDRESS
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(A.IP_ADDRESS) ASC NULLS LAST, A.PORT ASC NULLS LAST ";
			else
				retVal = " ORDER BY INET(A.IP_ADDRESS) DESC NULLS LAST, A.PORT ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_2SECOND:// PORT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY A.PORT ASC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY A.PORT DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_3THIRD:// NAME
			break;
		case OBDtoOrdering.TYPE_4FOURTH: // BPS_IN
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY B.BPS_IN ASC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY B.BPS_IN DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_5FIFTH: // BPS_OUT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY B.BPS_OUT ASC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY B.BPS_OUT DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_6SIXTH: // BPS_TOT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY B.BPS_TOT ASC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY B.BPS_TOT DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_7SEVENTH: // PPS_IN
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY B.PPS_IN ASC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY B.PPS_IN DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_8EIGHTH: // PPS_OUT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY B.PPS_OUT ASC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY B.PPS_OUT DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_9NINTH: // PPS_TOT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY B.PPS_TOT ASC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY B.PPS_TOT DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_14FOURTEENTH: // CONN_CURR
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY B.CONN_CURR ASC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY B.CONN_CURR DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			break;
		}
		return retVal;
	}

	public OBDtoTrafficMapVServiceMembers getSvcPerfVSrvMemberInfo(OBDtoADCObject object, OBDtoSearch searchObj,
			OBDtoOrdering orderOption, OBDatabase db) throws OBException {
		OBDtoTrafficMapVServiceMembers retVal = new OBDtoTrafficMapVServiceMembers();
		ArrayList<OBDtoTrafficMapMember> memberList = new ArrayList<OBDtoTrafficMapMember>();
		retVal.setMemberList(memberList);

		String sqlTextMember = "";
		String sqlTextSvc = "";
		String svcTalbeName = "TMP_SLB_VS_SERVICE";
		try {
			if (object.getCategory() == OBDtoADCObject.CATEGORY_VS) {
				svcTalbeName = "TMP_SLB_VSERVER";
				sqlTextSvc = " SELECT INDEX AS VS_INDEX, VIRTUAL_PORT, VIRTUAL_IP, NAME FROM TMP_SLB_VSERVER WHERE INDEX = "
						+ OBParser.sqlString(object.getStrIndex());
			} else if (object.getCategory() == OBDtoADCObject.CATEGORY_VSVC) {// for alteon
				svcTalbeName = "TMP_SLB_VS_SERVICE";
				sqlTextSvc = " SELECT A.VS_INDEX, A.VIRTUAL_PORT, A.REAL_PORT, B.VIRTUAL_IP, B.NAME FROM TMP_SLB_VS_SERVICE A  INNER JOIN TMP_SLB_VSERVER B  ON B.INDEX = A.VS_INDEX WHERE A.INDEX = "
						+ OBParser.sqlString(object.getStrIndex());
			} else {
				return retVal;
			}

			// VSERVER 정보를 추출한다.
			ResultSet rs = db.executeQuery(sqlTextSvc);
			String vsIPAddress = "";
			Integer svcPort = 0;
			Integer realPort = 0;
			String vsName = "";
			if (rs.next() == false) {
				return retVal;
			}
			vsIPAddress = db.getString(rs, "VIRTUAL_IP");
			svcPort = db.getInteger(rs, "VIRTUAL_PORT");
			vsName = db.getString(rs, "NAME");
			if (object.getCategory() == OBDtoADCObject.CATEGORY_VSVC) {
				realPort = db.getInteger(rs, "REAL_PORT");
			}

			// 멤버별 트래픽 정보를 추출한다.
			int offset = 0;
			if (searchObj != null && searchObj.getBeginIndex() != null)
				offset = searchObj.getBeginIndex().intValue();
			if (offset < 0)
				offset = 0;
			int limit = 0;
			if (searchObj != null && searchObj.getEndIndex() != null)
				limit = Math.abs(searchObj.getEndIndex().intValue() - offset) + 1;
			String sqlLimit = "";
			if (limit != 0)
				sqlLimit = String.format(" LIMIT %d ", limit);
			String sqlOffset = String.format(" OFFSET %d ", offset);

			int rescInterval = new OBEnvManagementImpl().getAdcSyncInterval(db);
			sqlTextMember = String.format(
					" SELECT B.OBJ_INDEX, A.IP_ADDRESS, A.PORT, A.EXTRA, B.OCCUR_TIME, B.BPS_IN, B.BPS_OUT, B.BPS_TOT, B.PPS_IN, B.PPS_OUT, B.PPS_TOT, B.CONN_CURR, B.CONN_TOT, B.CONN_MAX FROM ( SELECT A1.INDEX, A1.PORT, A2.IP_ADDRESS, A2.EXTRA FROM ( SELECT * FROM TMP_SLB_POOLMEMBER WHERE POOL_INDEX = ( SELECT POOL_INDEX FROM %s WHERE INDEX = %s ) ) A1 LEFT JOIN TMP_SLB_NODE A2 ON A1.NODE_INDEX = A2.INDEX ) A LEFT JOIN ( SELECT * FROM TMP_FAULT_SVC_MEMBER_PERF_STATS WHERE VS_INDEX = %s ) B ON A.INDEX = B.MEMBER_INDEX ",
					svcTalbeName, OBParser.sqlString(object.getStrIndex()), OBParser.sqlString(object.getStrIndex()),
					rescInterval * 2);

			/*
			 * String.
			 * format(" SELECT B.OBJ_INDEX, A.IP_ADDRESS, A.PORT, B.OCCUR_TIME, B.BPS_IN, B.BPS_OUT, B.BPS_TOT, B.PPS_IN, B.PPS_OUT, B.PPS_TOT, \n"
			 * +
			 * "		B.CONN_CURR, B.CONN_TOT, B.CONN_MAX FROM ( SELECT A1.INDEX, A1.PORT, A2.IP_ADDRESS 									\n"
			 * +
			 * " FROM ( SELECT MEMBER.INDEX, SERVICE.REAL_PORT PORT, MEMBER.NODE_INDEX FROM TMP_SLB_POOLMEMBER MEMBER 					\n"
			 * +
			 * " LEFT JOIN TMP_SLB_VS_SERVICE SERVICE																					\n"
			 * +
			 * " ON MEMBER.POOL_INDEX = SERVICE.POOL_INDEX																				\n"
			 * +
			 * " WHERE SERVICE.INDEX = %s ) ) A1 																						\n"
			 * +
			 * " LEFT JOIN TMP_SLB_NODE A2 ON A1.NODE_INDEX = A2.INDEX ) A 																\n"
			 * +
			 * " LEFT JOIN ( SELECT * FROM TMP_FAULT_SVC_MEMBER_PERF_STATS WHERE VS_INDEX = %s ) B 										\n"
			 * + " 	ON A.INDEX = B.MEMBER_INDEX ", svcTalbeName,
			 * OBParser.sqlString(object.getStrIndex()),
			 * OBParser.sqlString(object.getStrIndex()));
			 */

			sqlTextMember += getSvcPerfVSrvMemberListOrderType(orderOption);
			sqlTextMember += sqlOffset;

			if (!sqlLimit.isEmpty())
				sqlTextMember += sqlLimit;

			rs = db.executeQuery(sqlTextMember);

			long bpsInSum = 0;
			long bpsOutSum = 0;
			long bpsTotSum = 0;
			long ppsInSum = 0;
			long ppsOutSum = 0;
			long ppsTotSum = 0;
			long curConnsSum = 0;
			long maxConnsSum = 0;
			long totConnsSum = 0;

			Date occurTime = null;

			while (rs.next()) {
				String addPort = OBParser.extraAddPort(db.getString(rs, "EXTRA"));
				OBDtoTrafficMapMember info = new OBDtoTrafficMapMember();
				info.setIndex(db.getString(rs, "OBJ_INDEX"));
				info.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				info.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				if (object.getCategory() == OBDtoADCObject.CATEGORY_VSVC) {
					info.setAddPort(addPort);
					info.setPort(realPort);
				} else
					info.setPort(db.getInteger(rs, "PORT"));

				String occur = db.getString(rs, "OCCUR_TIME");
				if (occur != null && OBDateTime.getTimeIntervalCheck(occur, rescInterval)) {
					info.setBpsIn(db.getLong(rs, "BPS_IN"));
					info.setBpsOut(db.getLong(rs, "BPS_OUT"));
					info.setBpsTot(db.getLong(rs, "BPS_TOT"));
					info.setPpsIn(db.getLong(rs, "PPS_IN"));
					info.setPpsOut(db.getLong(rs, "PPS_IN"));
					info.setPpsTot(db.getLong(rs, "PPS_OUT"));
					info.setMaxConns(db.getLong(rs, "CONN_MAX"));
					info.setCurConns(db.getLong(rs, "CONN_CURR"));
					info.setTotConns(db.getLong(rs, "CONN_TOT"));
				} else {
					info.setBpsIn(-1l);
					info.setBpsOut(-1l);
					info.setBpsTot(-1l);
					info.setPpsIn(-1l);
					info.setPpsOut(-1l);
					info.setPpsTot(-1l);
					info.setMaxConns(-1l);
					info.setCurConns(-1l);
					info.setTotConns(-1l);
				}

				if (info.getBpsIn() > 0)
					bpsInSum += info.getBpsIn();
				if (info.getBpsOut() > 0)
					bpsOutSum += info.getBpsOut();
				if (info.getBpsTot() > 0)
					bpsTotSum += info.getBpsTot();

				if (info.getPpsIn() > 0)
					ppsInSum += info.getPpsIn();
				if (info.getPpsOut() > 0)
					ppsOutSum += info.getPpsOut();
				if (info.getPpsTot() > 0)
					ppsTotSum += info.getPpsTot();

				if (info.getCurConns() > 0)
					curConnsSum += info.getCurConns();
				if (info.getMaxConns() > 0)
					maxConnsSum += info.getMaxConns();
				if (info.getTotConns() > 0)
					totConnsSum += info.getTotConns();

				memberList.add(info);
			}
			while (rs.next())
				;

			retVal.setOccurTime(occurTime);
			retVal.setSrvPort(svcPort);
			retVal.setMemberList(memberList);
			retVal.setVsName(vsName);
			retVal.setVsIPAddress(vsIPAddress);

			retVal.setBpsIn(bpsInSum);
			retVal.setBpsOut(bpsOutSum);
			retVal.setBpsTot(bpsTotSum);
			retVal.setPpsIn(ppsInSum);
			retVal.setPpsOut(ppsOutSum);
			retVal.setPpsTot(ppsTotSum);
			if (bpsInSum < 0)
				retVal.setBpsIn(-1L);
			if (bpsOutSum < 0)
				retVal.setBpsOut(-1L);
			if (bpsTotSum < 0)
				retVal.setBpsTot(-1L);
			if (ppsInSum < 0)
				retVal.setPpsIn(-1L);
			if (ppsOutSum < 0)
				retVal.setPpsOut(-1L);
			if (ppsTotSum < 0)
				retVal.setPpsTot(-1L);

			retVal.setCurConns(curConnsSum);
			retVal.setMaxConns(maxConnsSum);
			retVal.setTotConns(totConnsSum);
			if (curConnsSum < 0)
				retVal.setCurConns(-1L);
			if (maxConnsSum < 0)
				retVal.setMaxConns(-1L);
			if (totConnsSum < 0)
				retVal.setTotConns(-1L);

			return retVal;
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlTextMember));
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
	}

	@Override
	public Integer getGroupCount(OBDtoTargetObject object, OBDtoSearch searchOption) throws OBException {
		try {
			if (object.getAdcRange() == OBDefine.ADC_RANGE.ALL) {
				return -1; // 구현 보류
			} else if (object.getAdcRange() == OBDefine.ADC_RANGE.GROUP) {
				return -1; // 구현 보류
			} else
			// single adc
			{
				OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());
				if (adcInfo == null) {
					return -1;
				}

				if (adcInfo.getAdcType().equals(OBDtoAdcInfo.ADC_TYPE_ALTEON)) {
					return getGroupCountOfSingleAdc(object.getIndex(), searchOption);
				} else
				// 구현 보류
				{
					return -1;
				}
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

	public Integer getGroupCountOfSingleAdc(int adcIndex, OBDtoSearch searchOption) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));
		String sqlText = "";
		// String sqlSearch=" TRUE "; //group search할 게 업다. 일단 통과.
		int result = 0;

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = " SELECT COUNT(GROUP_INDEX) GROUP_NUM FROM MNG_FLB_GROUP WHERE ADC_INDEX = " + adcIndex;

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result = db.getInteger(rs, "GROUP_NUM");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
		return result;
	}

	@Override
	public Integer getGroupMemberCount(String groupIndex) throws OBException {
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. groupIndex:%s", groupIndex));
		String sqlText = "";
		int result = 0;

		OBDatabase db = new OBDatabase();
		try {
			db.openDB();
			sqlText = " SELECT COUNT(INDEX) MEMBER_NUM FROM TMP_SLB_POOLMEMBER WHERE POOL_INDEX = "
					+ OBParser.sqlString(groupIndex);

			ResultSet rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				result = db.getInteger(rs, "MEMBER_NUM");
			}
		} catch (SQLException e) {
			throw new OBException(OBException.ERRCODE_DB_QEURY,
					String.format("%s, sqlText:%s", e.getMessage(), sqlText));
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%d", result));
		return result;
	}

	@Override
	public ArrayList<OBDtoFaultGroupPerfInfo> getGroupPerformanceList(OBDtoTargetObject object,
			OBDtoSearch searchOption, OBDtoOrdering orderOption) throws OBException {
		ArrayList<OBDtoFaultGroupPerfInfo> result = new ArrayList<OBDtoFaultGroupPerfInfo>();
		try {
			if (object.getAdcRange() == OBDefine.ADC_RANGE.ALL) {
				return null; // 기능 요구 없음
			} else if (object.getAdcRange() == OBDefine.ADC_RANGE.GROUP) {
				return null; // 기능 요구 없음
			} else if (object.getAdcRange() == OBDefine.ADC_RANGE.SINGLE_ADC) {
				result = getGroupPerformanceListOfAdc(object, searchOption, orderOption);
			}
			return result;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

	public ArrayList<OBDtoFaultGroupPerfInfo> getGroupPerformanceListOfAdc(OBDtoTargetObject object,
			OBDtoSearch searchOption, OBDtoOrdering orderOption) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoFaultGroupPerfInfo> retVal = new ArrayList<OBDtoFaultGroupPerfInfo>();
		try {
			db.openDB();
			retVal = getGroupPerformanceListOfAdc(object, searchOption, orderOption, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public ArrayList<OBDtoFaultGroupPerfInfo> getGroupPerformanceListOfAdc(OBDtoTargetObject object,
			OBDtoSearch searchOption, OBDtoOrdering orderOption, OBDatabase db) throws OBException {
		ArrayList<OBDtoFaultGroupPerfInfo> ret = new ArrayList<OBDtoFaultGroupPerfInfo>();
		OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(object.getIndex());
		String sqlText = "";
		String sqlSearch = "";
		int offset = 0;
		int limit = 0;
		String sqlLimit = "";

		if (adcInfo.getAdcType() == OBDtoAdcInfo.ADC_TYPE_ALTEON) {
			try {
				if (searchOption != null && searchOption.getBeginIndex() != null) {
					offset = searchOption.getBeginIndex().intValue();
				}
				if (searchOption != null && searchOption.getEndIndex() != null) {
					limit = Math.abs(searchOption.getEndIndex().intValue() - offset) + 1;
					sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
				}
				if (searchOption != null && searchOption.getSearchKey() != null
						&& !searchOption.getSearchKey().isEmpty()) // GROUP ID나 GROUP NAME으로 검색한다.
				{
					String wildcardKey = "%" + searchOption.getSearchKey() + "%";
					sqlSearch = String.format(" (GROUP_ID LIKE %s OR GROUP_NAME LIKE %s) ",
							OBParser.sqlString(wildcardKey), OBParser.sqlString(wildcardKey));
				} else {
					sqlSearch = " TRUE ";
				}

				sqlText = String.format(
						" SELECT A.INDEX GROUP_INDEX, A.ADC_INDEX, A.ALTEON_ID, A.NAME GROUP_NAME, A.STATUS, B.OCCUR_TIME, \n"
								+ "    C.POOL_INDEX, C.COUNT, COALESCE(B.BPS_IN,-1) BPS_IN, COALESCE(B.BPS_OUT,-1) BPS_OUT,   \n"
								+ "    COALESCE(B.BPS_TOT,-1) BPS_TOT, COALESCE(B.CONN_CURR,-1) CONN_CURR                     \n"
								+ " FROM (SELECT INDEX, ADC_INDEX, ALTEON_ID, NAME, STATUS FROM TMP_SLB_POOL WHERE INDEX IN   \n"
								+ "     (SELECT GROUP_INDEX FROM MNG_FLB_GROUP WHERE ADC_INDEX = %d) ) A                      \n"
								+ " LEFT JOIN (SELECT * FROM TMP_POOLGROUP_PERF_STATS WHERE ADC_INDEX = %d) B                 \n"
								+ "    ON A.INDEX = B.GROUP_INDEX                                                             \n"
								+ " LEFT JOIN (SELECT POOL_INDEX, COUNT(INDEX) FROM TMP_SLB_POOLMEMBER WHERE ADC_INDEX = %d GROUP BY POOL_INDEX ) C "
								+ "    ON A.INDEX = C.POOL_INDEX ",
						object.getIndex(), object.getIndex(), object.getIndex());

				if (!sqlSearch.isEmpty())
					sqlText += " AND " + sqlSearch;
				sqlText += getGroupPerformanceListOrderType(orderOption);
				sqlText += sqlLimit;

				ResultSet rs = db.executeQuery(sqlText);
				while (rs.next()) {
					OBDtoFaultGroupPerfInfo log = new OBDtoFaultGroupPerfInfo();

					log.setObject(object);
					log.setGroupDbIndex(db.getString(rs, "GROUP_INDEX"));
					log.setGroupId(db.getString(rs, "ALTEON_ID"));
					log.setGroupName(db.getString(rs, "GROUP_NAME"));
					log.setGroupStatus(db.getInteger(rs, "STATUS"));
					log.setBpsIn(db.getLong(rs, "BPS_IN"));
					log.setBpsOut(db.getLong(rs, "BPS_OUT"));
					log.setBpsTotal(db.getLong(rs, "BPS_TOT"));
					log.setConnCurr(db.getLong(rs, "CONN_CURR"));
					log.setMemberCount(db.getInteger(rs, "COUNT"));
					ret.add(log);
				}
			} catch (SQLException e) {
				throw new OBException(OBException.ERRCODE_DB_QEURY,
						String.format("%s, sqlText:%s", e.getMessage(), sqlText));
			} catch (OBException e) {
				throw e;
			} catch (Exception e) {
				throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
			}
		}
		return ret;
	}

	private String getGroupPerformanceListOrderType(OBDtoOrdering orderObj) throws OBException {
		if (orderObj == null) {
			return "";
		}
		String retVal = " ORDER BY CONN_CURR DESC NULLS LAST, ALTEON_ID ASC NULLS LAST ";
		int orderDir = orderObj.getOrderDirection();
		switch (orderObj.getOrderType()) {
		case OBDtoOrdering.TYPE_2SECOND:// ALTEON_ID:group id
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY ALTEON_ID ASC NULLS LAST, CONN_CURR DESC NULLS LAST ";
			else
				retVal = " ORDER BY ALTEON_ID DESC NULLS LAST, CONN_CURR DESC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_3THIRD:// GROUP_NAME
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY GROUP_NAME ASC NULLS LAST, CONN_CURR DESC NULLS LAST, ALTEON_ID ASC NULLS LAST ";
			else
				retVal = " ORDER BY GROUP_NAME DESC NULLS LAST, CONN_CURR DESC NULLS LAST, ALTEON_ID ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_4FOURTH:// COUNT : member count
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY COUNT ASC NULLS LAST, CONN_CURR DESC NULLS LAST, ALTEON_ID ASC NULLS LAST ";
			else
				retVal = " ORDER BY COUNT DESC NULLS LAST, CONN_CURR DESC NULLS LAST, ALTEON_ID ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_5FIFTH:// BPS_TOT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BPS_TOT ASC NULLS LAST, CONN_CURR DESC NULLS LAST, ALTEON_ID ASC NULLS LAST ";
			else
				retVal = " ORDER BY BPS_TOT DESC NULLS LAST, CONN_CURR DESC NULLS LAST, ALTEON_ID ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_8EIGHTH:// CONNECTION CURRENT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CONN_CURR ASC NULLS LAST, ALTEON_ID ASC NULLS LAST ";
			else
				retVal = " ORDER BY CONN_CURR DESC NULLS LAST, ALTEON_ID ASC NULLS LAST ";
			break;
		}

		return retVal;
	}

	@Override
	public OBDtoFaultGroupMemberPerfInfo getGroupMemberPerformanceList(String groupIndex, OBDtoSearch searchOption,
			OBDtoOrdering orderOption) throws OBException {
		OBDatabase db = new OBDatabase();

		String sqlText = "";
		String sqlSearch = "";
		int offset = 0;
		int limit = 0;
		String sqlLimit = "";

		OBDtoFaultGroupPerfInfo group = new OBDtoFaultGroupPerfInfo();
		ArrayList<OBDtoFaultRealInfo> memberList = new ArrayList<OBDtoFaultRealInfo>();
		OBDtoFaultGroupMemberPerfInfo retVal = new OBDtoFaultGroupMemberPerfInfo();

		try {
			db.openDB();
			if (searchOption != null && searchOption.getBeginIndex() != null) {
				offset = searchOption.getBeginIndex().intValue();
			}
			if (searchOption != null && searchOption.getEndIndex() != null) {
				limit = Math.abs(searchOption.getEndIndex().intValue() - offset) + 1;
				sqlLimit = String.format(" LIMIT %d OFFSET %d ", limit, offset);
			}
			if (searchOption != null && searchOption.getSearchKey() != null && !searchOption.getSearchKey().isEmpty()) // real의
																														// ID나
																														// 이름으로
																														// 검색한다.
			{
				String wildcardKey = "%" + searchOption.getSearchKey() + "%";
				sqlSearch = String.format(" (ALTEON_ID LIKE %s OR REAL_NAME LIKE %s) ", OBParser.sqlString(wildcardKey),
						OBParser.sqlString(wildcardKey));
			} else {
				sqlSearch = " TRUE ";
			}
			sqlText = String.format(
					" SELECT A.INDEX REAL_INDEX, A.ADC_INDEX, A.ALTEON_ID, A.NAME REAL_NAME, A.IP_ADDRESS,     \n"
							+ "    A.STATE REAL_STATUS, B.OCCUR_TIME, COALESCE(B.BPS_IN,-1) BPS_IN, COALESCE(B.BPS_OUT,-1) BPS_OUT,  \n"
							+ "    COALESCE(B.BPS_TOT,-1) BPS_TOT, COALESCE(B.CONN_CURR,-1) CONN_CURR                    \n"
							+ " FROM (SELECT INDEX, ADC_INDEX, ALTEON_ID, NAME, IP_ADDRESS, STATE                        \n"
							+ "       FROM TMP_SLB_NODE                                                                  \n"
							+ "       WHERE INDEX IN (SELECT NODE_INDEX FROM TMP_SLB_POOLMEMBER WHERE POOL_INDEX = %s)   \n"
							+ " ) A                                                                                      \n"
							+ " LEFT JOIN (SELECT * FROM TMP_REAL_PERF_STATS                                             \n"
							+ "            WHERE ADC_INDEX = (SELECT ADC_INDEX FROM TMP_SLB_POOL WHERE INDEX = %s)       \n"
							+ " ) B                                                                                      \n"
							+ " ON A.INDEX = B.REAL_INDEX ",
					OBParser.sqlString(groupIndex), OBParser.sqlString(groupIndex));

			if (!sqlSearch.isEmpty()) {
				sqlText += " AND " + sqlSearch;
			}
			sqlText += getGroupMemberPerformanceListOrderType(orderOption);
			sqlText += sqlLimit;

			ResultSet rs = db.executeQuery(sqlText);
			while (rs.next()) {
				OBDtoFaultRealInfo member = new OBDtoFaultRealInfo();
				member.setDbIndex(db.getString(rs, "REAL_INDEX"));
				member.setId(db.getString(rs, "ALTEON_ID"));
				member.setName(db.getString(rs, "REAL_NAME"));
				member.setIpAddress(db.getString(rs, "IP_ADDRESS"));
				member.setStatus(db.getInteger(rs, "REAL_STATUS"));
				member.setBpsIn(db.getLong(rs, "BPS_IN"));
				member.setBpsOut(db.getLong(rs, "BPS_OUT"));
				member.setBpsTotal(db.getLong(rs, "BPS_TOT"));
				member.setConnCurr(db.getLong(rs, "CONN_CURR"));
				memberList.add(member);
			}

			// group정보
			sqlText = String.format(
					" SELECT A.INDEX GROUP_INDEX, A.ADC_INDEX, A.ALTEON_ID, A.NAME GROUP_NAME, A.STATUS,            \n"
							+ "    B.OCCUR_TIME, C.COUNT, COALESCE(B.BPS_IN,-1) BPS_IN, COALESCE(B.BPS_OUT,-1) BPS_OUT,       \n"
							+ "    COALESCE(B.BPS_TOT,-1) BPS_TOT, COALESCE(B.CONN_CURR,-1) CONN_CURR                         \n"
							+ " FROM (SELECT INDEX, ADC_INDEX, ALTEON_ID, NAME, STATUS FROM TMP_SLB_POOL WHERE INDEX = %s) A  \n"
							+ " LEFT JOIN (SELECT * FROM TMP_POOLGROUP_PERF_STATS WHERE GROUP_INDEX = %s) B                   \n"
							+ " ON A.INDEX = B.GROUP_INDEX                                                                    \n"
							+ " LEFT JOIN (SELECT POOL_INDEX, COUNT(INDEX) FROM TMP_SLB_POOLMEMBER WHERE POOL_INDEX = %s GROUP BY POOL_INDEX ) C \n"
							+ " ON A.INDEX = C.POOL_INDEX ",
					OBParser.sqlString(groupIndex), OBParser.sqlString(groupIndex), OBParser.sqlString(groupIndex));
			rs = db.executeQuery(sqlText);
			if (rs.next() == true) {
				group.setGroupDbIndex(db.getString(rs, "GROUP_INDEX"));
				group.setGroupId(db.getString(rs, "ALTEON_ID"));
				group.setGroupName(db.getString(rs, "GROUP_NAME"));
				group.setMemberCount(db.getInteger(rs, "COUNT"));
				group.setGroupStatus(db.getInteger(rs, "STATUS"));
				group.setBpsIn(db.getLong(rs, "BPS_IN"));
				group.setBpsOut(db.getLong(rs, "BPS_OUT"));
				group.setBpsTotal(db.getLong(rs, "BPS_TOT"));
				group.setConnCurr(db.getLong(rs, "CONN_CURR"));
			}
			retVal.setGroupInfo(group);
			retVal.setMemberList(memberList);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	private String getGroupMemberPerformanceListOrderType(OBDtoOrdering orderObj) throws OBException {
		if (orderObj == null) {
			return "";
		}
		String retVal = " ORDER BY CONN_CURR DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
		int orderDir = orderObj.getOrderDirection();
		switch (orderObj.getOrderType()) {
		case OBDtoOrdering.TYPE_2SECOND:// IP_ADDRESS
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY INET(A.IP_ADDRESS) ASC NULLS LAST, CONN_CURR DESC NULLS LAST ";
			else
				retVal = " ORDER BY INET(A.IP_ADDRESS) DESC NULLS LAST, CONN_CURR DESC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_3THIRD:// REAL_NAME
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY REAL_NAME ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY REAL_NAME DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_4FOURTH:// BPS_TOT
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY BPS_TOT ASC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY BPS_TOT DESC NULLS LAST, CONN_CURR DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			break;
		case OBDtoOrdering.TYPE_5FIFTH:// CONNECTION total
			if (orderDir == OBDefine.ORDER_DIR_ASCEND)
				retVal = " ORDER BY CONN_CURR ASC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			else
				retVal = " ORDER BY CONN_CURR DESC NULLS LAST, INET(A.IP_ADDRESS) ASC NULLS LAST ";
			break;
		}

		return retVal;
	}

	@Override
	public ArrayList<OBDtoFaultBpsConnInfo> getGroupBpsConnHistory(String groupIndex, OBDtoSearch searchOption)
			throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoFaultBpsConnInfo> retVal = new ArrayList<OBDtoFaultBpsConnInfo>();
		try {
			db.openDB();
			retVal = getGroupBpsConnHistory(groupIndex, searchOption, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public ArrayList<OBDtoFaultBpsConnInfo> getGroupBpsConnHistory(String groupIndex, OBDtoSearch searchOption,
			OBDatabase db) throws OBException {
		ArrayList<OBDtoFaultBpsConnInfo> retVal = new ArrayList<OBDtoFaultBpsConnInfo>();
		String sqlText = "";

		try {
			String sqlSearch = makeTimeSqlText(searchOption, "OCCUR_TIME");
			sqlText = String.format(
					" SELECT GROUP_INDEX, OCCUR_TIME, BPS_IN, CONN_CURR, CONN_MAX, CONN_TOT, PPS_IN \n"
							+ " FROM LOG_POOLGROUP_PERF_STATS                                                 \n"
							+ " WHERE GROUP_INDEX = %s                                                        ",
					OBParser.sqlString(groupIndex));

			if (sqlSearch != null) {
				sqlText += " AND " + sqlSearch;
			}

			sqlText += " ORDER BY OCCUR_TIME ";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoFaultBpsConnInfo obj = new OBDtoFaultBpsConnInfo();

				OBDtoDataObj bpsObj = new OBDtoDataObj();
				bpsObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				bpsObj.setValue(db.getLong(rs, "BPS_IN"));

				OBDtoDataObj ppsObj = new OBDtoDataObj();
				ppsObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				ppsObj.setValue(db.getLong(rs, "PPS_IN"));

				OBDtoDataObj currObj = new OBDtoDataObj();
				currObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				currObj.setValue(db.getLong(rs, "CONN_CURR"));

				OBDtoDataObj maxObj = new OBDtoDataObj();
				maxObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				maxObj.setValue(db.getLong(rs, "CONN_MAX"));

				OBDtoDataObj totObj = new OBDtoDataObj();
				totObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				totObj.setValue(db.getLong(rs, "CONN_TOT"));

				obj.setBpsValue(bpsObj);
				obj.setPpsValue(ppsObj);
				obj.setConnCurrValue(currObj);
				obj.setConnMaxValue(maxObj);
				obj.setConnTotValue(totObj);

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

	@Override
	public ArrayList<OBDtoFaultBpsConnInfo> getGroupMemberBpsConnHistory(String realDbIndex, OBDtoSearch searchOption)
			throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoFaultBpsConnInfo> retVal = new ArrayList<OBDtoFaultBpsConnInfo>();
		try {
			db.openDB();
			retVal = getGroupMemberBpsConnHistory(realDbIndex, searchOption, db);
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		} finally {
			if (db != null)
				db.closeDB();
		}
		return retVal;
	}

	public ArrayList<OBDtoFaultBpsConnInfo> getGroupMemberBpsConnHistory(String realDbIndex, OBDtoSearch searchOption,
			OBDatabase db) throws OBException {
		ArrayList<OBDtoFaultBpsConnInfo> retVal = new ArrayList<OBDtoFaultBpsConnInfo>();
		String sqlText = "";

		try {
			String sqlSearch = makeTimeSqlText(searchOption, "OCCUR_TIME");
			sqlText = String.format(" SELECT OCCUR_TIME, BPS_IN, CONN_CURR, CONN_MAX, CONN_TOT, PPS_IN "
					+ " FROM LOG_REAL_PERF_STATS " + " WHERE REAL_INDEX = %s ", OBParser.sqlString(realDbIndex));

			if (sqlSearch != null) {
				sqlText += " AND " + sqlSearch;
			}

			sqlText += " ORDER BY OCCUR_TIME ";

			ResultSet rs = db.executeQuery(sqlText);

			while (rs.next()) {
				OBDtoFaultBpsConnInfo obj = new OBDtoFaultBpsConnInfo();

				OBDtoDataObj bpsObj = new OBDtoDataObj();
				bpsObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				bpsObj.setValue(db.getLong(rs, "BPS_IN"));

				OBDtoDataObj ppsObj = new OBDtoDataObj();
				ppsObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				ppsObj.setValue(db.getLong(rs, "PPS_IN"));

				OBDtoDataObj currObj = new OBDtoDataObj();
				currObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				currObj.setValue(db.getLong(rs, "CONN_CURR"));

				OBDtoDataObj maxObj = new OBDtoDataObj();
				maxObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				maxObj.setValue(db.getLong(rs, "CONN_MAX"));

				OBDtoDataObj totObj = new OBDtoDataObj();
				totObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
				totObj.setValue(db.getLong(rs, "CONN_TOT"));

				obj.setBpsValue(bpsObj);
				obj.setPpsValue(ppsObj);
				obj.setConnCurrValue(currObj);
				obj.setConnMaxValue(maxObj);
				obj.setConnTotValue(totObj);

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

}
