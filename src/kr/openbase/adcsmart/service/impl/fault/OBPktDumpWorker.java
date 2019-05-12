package kr.openbase.adcsmart.service.impl.fault;

import java.util.ArrayList;
import java.util.HashMap;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoPktDumpInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvNetwork;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.impl.OBSystemAuditImpl;
import kr.openbase.adcsmart.service.impl.alteon.handler.OBAdcAlteonHandler;
import kr.openbase.adcsmart.service.impl.dto.OBDtoFaultFileSizeInfo;
import kr.openbase.adcsmart.service.impl.f5.handler.OBAdcF5Handler;
import kr.openbase.adcsmart.service.impl.f5.handler.OBCLIParserF5;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoFaultCheckThreadInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktdumpInfo;
import kr.openbase.adcsmart.service.impl.fault.dto.OBDtoPktdumpOption;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBPktDumpWorker implements Runnable {
	private OBDtoFaultCheckThreadInfo threadInfo;
	private Integer packetDumpCount = 500000;
	private Integer alteonPacketDumpCount = 10000;
	private Long packetDumpSize = 10000000L;// 10000000
//	private Integer packetDumpMaxStart = 10000;
	private Integer packetDumpStart = 1;
	private Integer uptimeMaxDiffDate = 600;
	private Integer packetDumpLength = 0;

//	public static void main(String[] args)
//	{
//		try
//		{
//			OBDtoFaultCheckThreadInfo threadInfo = new OBDtoFaultCheckThreadInfo();
//			threadInfo.setAccntIndex(0);
//			threadInfo.setClientIP("172.172.2.206");
//			threadInfo.setCheckKey(46472053154136L);
//			OBPktDumpWorker worker = new OBPktDumpWorker(threadInfo);
//			worker.run();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	public OBPktDumpWorker(OBDtoFaultCheckThreadInfo threadInfo) {
		setThreadInfo(threadInfo);
		// 환경 변수를 읽어 들인다.
		try {
			String propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_PKT_COUNT);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.packetDumpCount = Integer.parseInt(propertyValue);

			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME, OBDefine.PROPERTY_KEY_PKT_MAX_SIZE);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.packetDumpSize = Long.parseLong(propertyValue);

			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME, OBDefine.PROPERTY_KEY_PKT_LENGTH);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.packetDumpLength = Integer.parseInt(propertyValue);

//			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME, OBDefine.PROPERTY_KEY_PKT_MAX_START);
//			if (propertyValue != null && !propertyValue.isEmpty())
//				this.packetDumpMaxStart = Integer.parseInt(propertyValue);

			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME, OBDefine.PROPERTY_KEY_PKT_START);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.packetDumpStart = Integer.parseInt(propertyValue);

			propertyValue = OBParser.propertyToString(OBDefine.PROPERTY_FILE_NAME,
					OBDefine.PROPERTY_KEY_UPTIME_MAX_DIFF_DATE);
			if (propertyValue != null && !propertyValue.isEmpty())
				this.uptimeMaxDiffDate = Integer.parseInt(propertyValue);
		} catch (Exception e) {
		}
	}

	public void setThreadInfo(OBDtoFaultCheckThreadInfo threadInfo) {
		this.threadInfo = threadInfo;
	}

	public OBDtoFaultCheckThreadInfo getThreadInfo() {
		return threadInfo;
	}

	private void runCpuMemWorker(OBDtoPktdumpInfo dumpInfo) {
		try {
			Runnable r = new OBPktDumpCpuMemWorker(dumpInfo);
			Thread t = new Thread(r);
			t.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] pkt dump start.",
				this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
		OBDatabase db = new OBDatabase();
		try {
			db.openDB();

			long logKey = this.threadInfo.getCheckKey();
			String clientIP = this.threadInfo.getClientIP();
			int accntIndex = this.threadInfo.getAccntIndex();

			Object CLIObj = null;
			OBDtoAdcInfo adcInfo = null;
			OBDtoPktdumpInfo pktLogInfo = null;
			try {
				// template 정보 추출.
				pktLogInfo = new OBFaultMngImpl().getPktdumpInfo(logKey);
				if (pktLogInfo == null) {
					new OBFaultMngImpl().updatePktdumpStatus(logKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
							OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
					new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_PKT_DUMP_FAIL, "");
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("[%d/%d] failed to get basic dump info. msg:data not found",
									this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					return;
				}
			} catch (OBException e) {
				new OBFaultMngImpl().updatePktdumpStatus(logKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
						OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_PKT_DUMP_FAIL, "");
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("[%d/%d] failed to get basic dump info. msg:%s", this.threadInfo.getAdcIndex(),
								this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
				return;
			}

			// cpu/mem 추출용 thread 실행.
			runCpuMemWorker(pktLogInfo);

			try {
				adcInfo = new OBAdcManagementImpl().getAdcInfo(pktLogInfo.getAdcIndex());
				if (adcInfo == null) {
					new OBFaultMngImpl().updatePktdumpStatus(logKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
							OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
					new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_PKT_DUMP_FAIL, "");
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("[%d/%d] failed to get adcInfo. msg:data not found",
									this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					return;
				}
			} catch (OBException e) {
				new OBFaultMngImpl().updatePktdumpStatus(logKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
						OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_PKT_DUMP_FAIL, "");
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to get adcInfo. msg:%s",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
				return;
			}

			// cli용 채널 오픈.
			try {
				CLIObj = new OBFaultMngImpl().openCLIChannel(adcInfo);
				if (CLIObj == null) {
					new OBFaultMngImpl().updatePktdumpStatus(logKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
							OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
					new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_PKT_DUMP_FAIL,
							adcInfo.getName());
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
							String.format("[%d/%d] failed to open cli channel. msg: invalid cli channel info",
									this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					return;
				}
			} catch (OBException e) {
				new OBFaultMngImpl().updatePktdumpStatus(logKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
						OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_PKT_DUMP_FAIL,
						adcInfo.getName());
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to open cli channel. msg:%s",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
				return;
			}
			try {
				// 패킷 수집 부분 추가 필요.
				processPktdump(logKey, pktLogInfo, adcInfo, CLIObj, db);
			} catch (OBException e) {
				new OBFaultMngImpl().updatePktdumpStatus(logKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
						OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
				new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_PKT_DUMP_FAIL,
						adcInfo.getName());
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] failed to capture pkt. msg:%s",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), e.getErrorMessage()));
				return;
			}

			// 오픈된 채널을 종료한다.
			new OBFaultMngImpl().closeCLIChannel(adcInfo, CLIObj);

			try {
				if (new OBFaultMngImpl().isPktdumpCancelStopped(logKey, db) == false) {
					new OBSystemAuditImpl().writeLog(accntIndex, clientIP, OBSystemAuditImpl.AUDIT_PKT_DUMP_SUCCESS,
							adcInfo.getName());
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d] end.", logKey));
		} catch (OBException e) {
			return;
		} finally {
			if (db != null)
				db.closeDB();
		}
	}

	private void processPktdump(long logKey, OBDtoPktdumpInfo pktLogInfo, OBDtoAdcInfo adcInfo, Object CLIObj,
			OBDatabase db) throws OBException {
		switch (adcInfo.getAdcType()) {
		case OBDefine.ADC_TYPE_F5:
			processPktdumpF5(logKey, pktLogInfo, adcInfo, CLIObj, db);
			break;
		case OBDefine.ADC_TYPE_ALTEON:
			processPktdumpAlteon(logKey, pktLogInfo, adcInfo, CLIObj, db);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_PAS:
			processPktdumpPAS(logKey, pktLogInfo, adcInfo, CLIObj, db);
			break;
		case OBDefine.ADC_TYPE_PIOLINK_PASK:
			processPktdumpPASK(logKey, pktLogInfo, adcInfo, CLIObj, db);
			break;
		}
	}

	private String makePktdumpOptionString(OBDtoPktdumpInfo pktLogInfo) throws OBException {
		String retVal = "";

		try {
			String strFilter = pktLogInfo.getStrFilter();
			ArrayList<OBDtoPktdumpOption> filterList = pktLogInfo.toFilterArray(strFilter);
			for (OBDtoPktdumpOption option : filterList) {
				switch (option.getType()) {
				case OBDtoPktdumpOption.OPTION_TYPE_DST_IP:
					if (!retVal.isEmpty())
						retVal += " and ";
					retVal += String.format(" dst host %s ", option.getContent());
					break;
				case OBDtoPktdumpOption.OPTION_TYPE_DST_PORT:
					if (!retVal.isEmpty())
						retVal += " and ";
					retVal += String.format(" dst port %s ", option.getContent());
					break;
				case OBDtoPktdumpOption.OPTION_TYPE_PROTOCOL:
					if (!retVal.isEmpty())
						retVal += " and ";
					retVal += String.format(" %s ", option.getContent().toLowerCase());
					break;
				case OBDtoPktdumpOption.OPTION_TYPE_SRC_IP:
					if (!retVal.isEmpty())
						retVal += " and ";
					retVal += String.format(" src host %s ", option.getContent());
					break;
				case OBDtoPktdumpOption.OPTION_TYPE_SRC_PORT:
					if (!retVal.isEmpty())
						retVal += " and ";
					retVal += String.format(" src port %s ", option.getContent());
					break;
				case OBDtoPktdumpOption.OPTION_TYPE_HOST:
					if (!retVal.isEmpty())
						retVal += " and ";
					retVal += String.format(" host %s ", option.getContent());
					break;
				case OBDtoPktdumpOption.OPTION_TYPE_PORT:
					if (!retVal.isEmpty())
						retVal += " and ";
					retVal += String.format(" port %s ", option.getContent());
					break;
				}
			}
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);//
		}
		return retVal;
	}

	private void processPktdumpAlteon(long indexKey, OBDtoPktdumpInfo pktLogInfo, OBDtoAdcInfo adcInfo, Object CLIObj,
			OBDatabase db) throws OBException {
		try {
			if (pktLogInfo.getOptionMaxPkt().longValue() == 0) {
				pktLogInfo.setOptionMaxPkt(this.alteonPacketDumpCount);
			}
			if (pktLogInfo.getOptionMaxSize().longValue() == 0) {
				pktLogInfo.setOptionMaxSize((Long) this.packetDumpSize.longValue());
			}
			if (pktLogInfo.getOptionMaxTime().intValue() == 0) {
				pktLogInfo.setOptionMaxTime(this.uptimeMaxDiffDate);
			}

			((OBAdcAlteonHandler) CLIObj).cmndPktDumpStop();

			if (new OBFaultMngImpl().isPktdumpCancelStopped(indexKey, db) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return;
			}

			Integer statusFlag = new OBFaultMngImpl().getPktdumpStatusFlag(indexKey, db);

			if (statusFlag == OBDtoPktdumpInfo.STATUS_FAILURE) {
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
						String.format("pkt dump stopped by fault(logKey:%d)", indexKey));
				new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
						OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
				return;
			}

			// send download signal to ADC
			String localFileName = indexKey + ".pcap";
			OBDtoSystemEnvNetwork envInfo = new OBEnvManagementImpl().getNetworkConfig();

			String subOption = makePktdumpOptionString(pktLogInfo);

			String option = "";
			if (subOption.isEmpty())
				option = String.format("-s %d -c %d ", this.packetDumpLength, pktLogInfo.getOptionMaxPkt());
			else
				option = String.format("-s %d -c %d %s ", this.packetDumpLength, pktLogInfo.getOptionMaxPkt(),
						subOption);

			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] pkt dump option : %s",
					this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), option));

			// 패킷 덤프 시작.
			boolean isOk = ((OBAdcAlteonHandler) CLIObj).cmndPktDumpStartOption(pktLogInfo.getInterfaceName(), option);
			if (isOk == false) {// 패킷 덤프에 실패한 경우.
				new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
						OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
				return;
			}

			// 패킷 덤프 상태 조회.
			Long startTick = OBDateTime.toTimestamp(OBDateTime.now()).getTime();
			while (true) {
				OBDateTime.Sleep(1000);

				if (new OBFaultMngImpl().isPktdumpCancelStopped(indexKey, db) == true) {// 작업이 취소된 경우 처리.
					((OBAdcAlteonHandler) CLIObj).cmndPktDumpStop();
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					return;
				}

				statusFlag = new OBFaultMngImpl().getPktdumpStatusFlag(indexKey, db);
				if (statusFlag == OBDtoPktdumpInfo.STATUS_SUCCESS)
					break;

				if (statusFlag == OBDtoPktdumpInfo.STATUS_FAILURE) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] pkt dump stopped by error",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
							OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
					return;
				}

				Long currentTick = OBDateTime.toTimestamp(OBDateTime.now()).getTime();
				long elaspeTime = Math.abs(currentTick - startTick);
				if (elaspeTime > pktLogInfo.getOptionMaxTime() * 1000) {// 최대 대기 시간까지 기다린 경우.
					((OBAdcAlteonHandler) CLIObj).cmndPktDumpStop();
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] pkt dump stopped by max wait time",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					break;
				}

				// 패킷 덤프 상태를 검사한다.
				isOk = ((OBAdcAlteonHandler) CLIObj).cmndPktDumpStartStatus(this.packetDumpStart, this.packetDumpCount);
				if (isOk == false) {// 패킷 덤프 상태 조회에 실패. 진행중으로 간주한다.
					((OBAdcAlteonHandler) CLIObj).cmndPktDumpStop();
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
							String.format("[%d/%d] pkt dump stopped. fail to parse pkt process status",
									this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
							OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
					return;
				}

				String msgCLI = ((OBAdcAlteonHandler) CLIObj).getCmndRetString();
				OBDtoPktDumpInfo pktDumpInfo = OBCommon.getValidAlteonCLIParser(adcInfo.getSwVersion())
						.parsePktDumpInfo(msgCLI);
				if (pktDumpInfo == null) {// 진행중인 상태.
					int progressRate = (int) (elaspeTime * 100L / (pktLogInfo.getOptionMaxTime() * 1000));
					if (progressRate >= 100)
						progressRate = 99;
					new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, null, progressRate, null, null, db);
				} else {// 덤프 완료된 상태.
					break;
				}
			}

			((OBAdcAlteonHandler) CLIObj).cmndPktDumpStop();
			OBDateTime.Sleep(500);

			if (new OBFaultMngImpl().isPktdumpCancelStopped(indexKey, db) == true) {// 작업이 취소된 경우 처리.
				((OBAdcAlteonHandler) CLIObj).cmndPktDumpStop();
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return;
			}

			// 패킷 덤프 파일 수신.
			int retCode = ((OBAdcAlteonHandler) CLIObj).cmndPktDumpSend(localFileName, envInfo.getIpAddress());
			if (retCode != 0) {// 패킷 덤프에 실패한 경우.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
						String.format("[%d/%d] pkt dump stopped. failed to receive file. fileName:%s",
								this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), localFileName));
				new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
						OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
				return;
			}

			if (new OBFaultMngImpl().isPktdumpCancelStopped(indexKey, db) == true)
				return;

			// 28버전 이상이면 압축을 해제해야 한다.
			new OBFaultMngImpl().extractPktdumpAlteon(adcInfo, localFileName);

//			Integer majorVersion = getMajorVersion(adcInfo.getSwVersion());
//			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("5555:"+majorVersion));
//
//			if(majorVersion.intValue()>=28)
//			{// tar.gz 형태를 압축 해제 한다.
//				String cmndExtract = String.format("tar -zxvf /var/lib/adcsmart/pcap/%s.tar.gz -C /var/lib/adcsmart/pcap", localFileName);
//				String cmndMove = String.format("mv /var/lib/adcsmart/pcap/pkt.pcap /var/lib/adcsmart/pcap/%s", localFileName);
//				String srcName = OBDefine.PKT_DUMP_FILE_PATH+"pkt.pcap";
//				String dstName = OBDefine.PKT_DUMP_FILE_PATH+localFileName;
////				String tarFile = dstName+".tar.gz";
////				if(OBUtility.untarFile(tarFile, OBDefine.PKT_DUMP_FILE_PATH)==false)
////				{
////					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to extract file:%s", tarFile));
////					new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()), OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
////					return;
////				}
//				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] extract file:%s", this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), cmndExtract));
//				Runtime.getRuntime().exec(cmndExtract).waitFor();
////				OBDateTime.Sleep(500);
//
//				OBUtility.fileMove(srcName, dstName);
//				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] move file:src:%s, dst:%s", this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey(), srcName, dstName));
////					Runtime.getRuntime().exec(cmndMove);
////					OBDateTime.Sleep(500);
//			}
			// 수신한 패킷의 사이즈 추출.
			String fullPathName = OBDefine.PKT_DUMP_FILE_PATH + localFileName;
			Long fileSize = OBUtility.fileLength(fullPathName);
			new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
					OBDtoPktdumpInfo.STATUS_SUCCESS, null, fileSize, db);
		} catch (OBException e) {
			new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
					OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
			throw e;
		} catch (Exception e) {
			new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
					OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

	private void processPktdumpF5(long indexKey, OBDtoPktdumpInfo pktLogInfo, OBDtoAdcInfo adcInfo, Object CLIObj,
			OBDatabase db) throws OBException {
		try {
			if (pktLogInfo.getOptionMaxPkt().longValue() == 0) {
				pktLogInfo.setOptionMaxPkt(this.packetDumpCount);
			}
			if (pktLogInfo.getOptionMaxSize().longValue() == 0) {
				pktLogInfo.setOptionMaxSize(this.packetDumpSize);
			}
			if (pktLogInfo.getOptionMaxTime().intValue() == 0) {
				pktLogInfo.setOptionMaxTime(this.uptimeMaxDiffDate);
			}

			((OBAdcF5Handler) CLIObj).cmndStopTcpdump();

			if (new OBFaultMngImpl().isPktdumpCancelStopped(indexKey, db) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return;
			}

			Integer statusFlag = new OBFaultMngImpl().getPktdumpStatusFlag(indexKey, db);
//			if(statusFlag==OBDtoPktdumpInfo.STATUS_STOP)//stop 명령 상태임.
//			{	
//				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("pkt dump stopped by command(logKey:%d)", indexKey));
//				new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()), OBDtoPktdumpInfo.STATUS_SUCCESS, null, null, db);
//				return;// 하위 작업을 종료한다.
//			}
//			
//			if(statusFlag==OBDtoPktdumpInfo.STATUS_CANCEL)//cancel 명령 상태임.
//			{	
//				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("pkt dump cancelled by command(logKey:%d)", indexKey));
//				new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()), OBDtoPktdumpInfo.STATUS_SUCCESS, null, null, db);
//				return;// 하위 작업을 종료한다.
//			}

			if (statusFlag == OBDtoPktdumpInfo.STATUS_FAILURE) {
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
						String.format("pkt dump stopped by fault(logKey:%d)", indexKey));
				new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
						OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
				return;
			}

			// send download signal to ADC
			String localFileName = indexKey + ".pcap";

			String subOption = makePktdumpOptionString(pktLogInfo);

			String option = "";
			if (subOption.isEmpty())
				option = String.format("-s %d -c %d ", this.packetDumpLength, pktLogInfo.getOptionMaxPkt());
			else
				option = String.format("-s %d -c %d %s ", this.packetDumpLength, pktLogInfo.getOptionMaxPkt(),
						subOption);

			// 패킷 덤프 시작.
			((OBAdcF5Handler) CLIObj).cmndTcpdumpStart(pktLogInfo.getInterfaceName(), localFileName, option);

			// 패킷 덤프 상태 조회.
			Long startTick = OBDateTime.toTimestamp(OBDateTime.now()).getTime();
			Long fileSize = 0L;
			while (true) {
				OBDateTime.Sleep(1000);
				if (new OBFaultMngImpl().isPktdumpCancelStopped(indexKey, db) == true) {// 작업이 취소된 경우 처리.
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
							this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
					return;
				}

				statusFlag = new OBFaultMngImpl().getPktdumpStatusFlag(indexKey, db);
				if (statusFlag == OBDtoPktdumpInfo.STATUS_SUCCESS)
					break;
//				
//				if(statusFlag==OBDtoPktdumpInfo.STATUS_STOP)//stop 명령 상태임.
//				{
//					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("pkt dump stopped by command(logKey:%d)", indexKey));
//					new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()), OBDtoPktdumpInfo.STATUS_SUCCESS, null, null, db);
//					return;// 하위 작업을 종료한다.
//				}
//				
//				if(statusFlag==OBDtoPktdumpInfo.STATUS_CANCEL)//cancel 명령 상태임.
//				{	
//					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("pkt dump cancelled by command(logKey:%d)", indexKey));
//					new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()), OBDtoPktdumpInfo.STATUS_SUCCESS, null, null, db);
//					return;// 하위 작업을 종료한다.
//				}

				if (statusFlag == OBDtoPktdumpInfo.STATUS_FAILURE) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
							String.format("pkt dump stopped by fault(logKey:%d)", indexKey));
					new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
							OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
					return;
				}

				Long currentTick = OBDateTime.toTimestamp(OBDateTime.now()).getTime();
				long elaspeTime = Math.abs(currentTick - startTick);
				if (elaspeTime > pktLogInfo.getOptionMaxTime() * 1000) {// 최대 대기 시간까지 기다린 경우.
					break;
				}

				// 패킷 덤프 상태를 검사한다.
				ArrayList<String> dumpFileList = new ArrayList<String>();
				dumpFileList.add(localFileName);
				String retMsg = ((OBAdcF5Handler) CLIObj).cmndCheckFileSize(dumpFileList);
				retMsg = ((OBAdcF5Handler) CLIObj).getCmndRetString();

				// parsing.
				HashMap<String, OBDtoFaultFileSizeInfo> fileMap = null;
				try {
					fileMap = new OBCLIParserF5().parseFileSizeInfo(retMsg);
				} catch (OBException e) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
							String.format("pkt dump stopped. failure in parsing size(logKey:%d)", indexKey));
					new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
							OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
					return;
				}

				OBDtoFaultFileSizeInfo tmpFileInfo = fileMap.get(localFileName);
				if (tmpFileInfo == null) {
					OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
							String.format("pkt dump stopped. failure in parsing size(logKey:%d)", indexKey));
					new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
							OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
					return;
				}

				if (tmpFileInfo.getFileSize().longValue() >= (pktLogInfo.getOptionMaxSize().longValue() * 1000)) {// 종료해야
																													// 함.
					break;
				}

				// 프로세스가 종료되었는지 확인한다.
				if (tmpFileInfo.getFileSize().longValue() > 0) {
					retMsg = ((OBAdcF5Handler) CLIObj).cmndCheckProcess("tcpdump");
					retMsg = ((OBAdcF5Handler) CLIObj).getCmndRetString();
					if (retMsg.isEmpty())
						break;
				}

				// 진행중인 것으로 간주.
				int progressRate = (int) (elaspeTime * 100L / (pktLogInfo.getOptionMaxTime() * 1000));
				if (progressRate >= 100)
					progressRate = 99;
				new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, null, progressRate, null, null, db);
				fileSize = tmpFileInfo.getFileSize();
			}

			((OBAdcF5Handler) CLIObj).cmndStopTcpdump();

			if (new OBFaultMngImpl().isPktdumpCancelStopped(indexKey, db) == true) {// 작업이 취소된 경우 처리.
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("[%d/%d] fault check was canceled.",
						this.threadInfo.getAdcIndex(), this.getThreadInfo().getCheckKey()));
				return;
			}

			// 패킷 덤프 파일 수신.
			String remoteFileName = OBDefine.PKT_DUMP_FILE_PATH + localFileName;
			boolean isOk = ((OBAdcF5Handler) CLIObj).cmndScpDumpfile(localFileName, remoteFileName);
			if (isOk == false) {// 패킷 덤프에 실패한 경우.
				new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
						OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
				return;
			}

			if (new OBFaultMngImpl().isPktdumpCancelStopped(indexKey, db) == true)
				return;

			// 수신한 패킷의 사이즈 추출.
			String fullPathName = OBDefine.PKT_DUMP_FILE_PATH + localFileName;
			fileSize = OBUtility.fileLength(fullPathName);
			new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
					OBDtoPktdumpInfo.STATUS_SUCCESS, null, fileSize, db);

			// 수집된 파일을 삭제한다.
			((OBAdcF5Handler) CLIObj).cmndRemoveTcpdumpFile(localFileName);
		} catch (OBException e) {
			new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
					OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
			throw e;
		} catch (Exception e) {
			new OBFaultMngImpl().updatePktdumpStatus(indexKey, null, OBDateTime.toTimestamp(OBDateTime.now()),
					OBDtoPktdumpInfo.STATUS_FAILURE, null, null, db);
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

	private void processPktdumpPAS(long logKey, OBDtoPktdumpInfo pktLogInfo, OBDtoAdcInfo adcInfo, Object CLIObj,
			OBDatabase db) throws OBException {

	}

	private void processPktdumpPASK(long logKey, OBDtoPktdumpInfo pktLogInfo, OBDtoAdcInfo adcInfo, Object CLIObj,
			OBDatabase db) throws OBException {

	}
}
