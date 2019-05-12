package kr.openbase.adcsmart.web.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVWriter;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnData;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResult;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultContent;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultElement;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCpuHistory;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFlbSlbSession;
import kr.openbase.adcsmart.service.dto.fault.OBDtoL2SearchInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcMonCpuHistory;
import kr.openbase.adcsmart.service.impl.dto.OBDtoConnectionDataObj;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalAdc;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalAdcCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalAdcOne;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalGroupCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalGroupOne;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalRealCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalRealOne;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalServiceCondition;
import kr.openbase.adcsmart.service.impl.dto.OBDtoMonTotalServiceOne;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBUtility;
import kr.openbase.adcsmart.web.facade.dto.AdcDto;
import kr.openbase.adcsmart.web.facade.dto.AdcLogDto;
import kr.openbase.adcsmart.web.facade.dto.AdcSystemLogDto;
import kr.openbase.adcsmart.web.facade.dto.AuditLogDto;
import kr.openbase.adcsmart.web.facade.dto.FaultSessionInfoDto;
import kr.openbase.adcsmart.web.facade.dto.FlbFilterInfoDto;
import kr.openbase.adcsmart.web.facade.dto.NetworkMapVsDto;
import kr.openbase.adcsmart.web.facade.dto.NetworkMapVsMemberDto;
import kr.openbase.adcsmart.web.facade.dto.PerfActiveConnectionDataDto;
import kr.openbase.adcsmart.web.facade.dto.PerfActiveConnectionInfoDto;
import kr.openbase.adcsmart.web.facade.dto.PerfHttpRequestDataDto;
import kr.openbase.adcsmart.web.facade.dto.PerfHttpRequestInfoDto;
import kr.openbase.adcsmart.web.facade.dto.PerfSslTransactionDataDto;
import kr.openbase.adcsmart.web.facade.dto.PerfSslTransactionInfoDto;
import kr.openbase.adcsmart.web.facade.dto.PerfThroughputDataDto;
import kr.openbase.adcsmart.web.facade.dto.PerfThroughputInfoDto;
import kr.openbase.adcsmart.web.facade.dto.PerformanceDto;
import kr.openbase.adcsmart.web.facade.dto.Statistic5GraphDataDto;
import kr.openbase.adcsmart.web.facade.dto.SystemStatusDto;
import kr.openbase.adcsmart.web.facade.dto.VsConnectionDataDto;
import kr.openbase.adcsmart.web.facade.dto.VsConnectionInfoDto;

public class CsvMaker {
	private transient static Logger log = LoggerFactory.getLogger(CsvMaker.class);
	private static final String CSV_SAVE_FOLDER = "/var/temp/adcms/csv/";

	private static final String MSG_CSV_ORI_DATE = "MSG_CSV_ORI_DATE";
	private static final String MSG_CSV_CREATE_TIME = "MSG_CSV_CREATE_TIME";
	private static final String MSG_CSV_RES_TIME = "MSG_CSV_RES_TIME";
	private static final String MSG_CSV_ADC_NAME = "MSG_CSV_ADC_NAME";
	private static final String MSG_CSV_LOG_KIND = "MSG_CSV_LOG_KIND";
	private static final String MSG_CSV_IMP = "MSG_CSV_IMP";
	private static final String MSG_CSV_DET_CONT = "MSG_CSV_DET_CONT";
	private static final String MSG_CSV_USER = "MSG_CSV_USER";
	private static final String MSG_CSV_ACC_IP = "MSG_CSV_ACC_IP";
	private static final String MSG_CSV_CLASS = "MSG_CSV_CLASS";
	private static final String MSG_CSV_LIST = "MSG_CSV_LIST";
	private static final String MSG_CSV_STATUS = "MSG_CSV_STATUS";
	private static final String MSG_CSV_ERR = "MSG_CSV_ERR";
	private static final String MSG_CSV_NORMAL = "MSG_CSV_NORMAL";
	private static final String MSG_CSV_INFO = "MSG_CSV_INFO";
	private static final String MSG_CSV_SPORT = "MSG_CSV_SPORT";
	private static final String MSG_CSV_DPORT = "MSG_CSV_DPORT";
	private static final String MSG_CSV_DESTPORT = "MSG_CSV_DESTPORT";
//	private static final String MSG_CSV_AGE_S				= "MSG_CSV_AGE_S";
	private static final String MSG_CSV_AGE_M = "MSG_CSV_AGE_M";
	private static final String MSG_CSV_NOW = "MSG_CSV_NOW";
	private static final String MSG_CSV_MAX = "MSG_CSV_MAX";
	private static final String MSG_CSV_FILE_WRI_START = "MSG_CSV_FILE_WRI_START";
	private static final String MSG_CSV_CSV_FILE_CRE_ERR = "MSG_CSV_CSV_FILE_CRE_ERR";
	private static final String MSG_CSV_FILE_WRI_END = "MSG_CSV_FILE_WRI_END";
	private static final String MSG_CSV_IN = "MSG_CSV_IN";
	private static final String MSG_CSV_OUT = "MSG_CSV_OUT";
	private static final String MSG_CSV_TOTAL = "MSG_CSV_TOTAL";
	private static final String MSG_CSV_BPS = "MSG_CSV_BPS";
	private static final String MSG_CSV_BPS_IN = "MSG_CSV_BPS_IN";
	private static final String MSG_CSV_BPS_OUT = "MSG_CSV_BPS_OUT";
	private static final String MSG_CSV_BPS_TOTAL = "MSG_CSV_BPS_TOTAL";
	private static final String MSG_CSV_CONN = "MSG_CSV_CONN";

	private static final String MSG_CSV_PREDAY = "MSG_CSV_PREDAY";
	private static final String MSG_CSV_PREWEEKS = "MSG_CSV_PREWEEKS";
	private static final String MSG_CSV_PREMONTH = "MSG_CSV_PREMONTH";
	private static final String MSG_CSV_PREDAYCHK = "MSG_CSV_PREDAYCHK";

	private static final String MSG_CSV_ADCIP = "MSG_CSV_ADCIP";
	private static final String MSG_CSV_IP = "MSG_CSV_IP";
	private static final String MSG_CSV_MAC = "MSG_CSV_MAC";
	private static final String MSG_CSV_VLAN = "MSG_CSV_VLAN";
	private static final String MSG_CSV_PORT = "MSG_CSV_PORT";
	private static final String MSG_CSV_HW = "MSG_CSV_HW";
	private static final String MSG_CSV_L23 = "MSG_CSV_L23";
	private static final String MSG_CSV_L47 = "MSG_CSV_L47";
	private static final String MSG_CSV_SVC = "MSG_CSV_SVC";
	private static final String MSG_CSV_CIP = "MSG_CSV_CIP";
	private static final String MSG_CSV_DESTIP = "MSG_CSV_DESTIP";
	private static final String MSG_CSV_REALIP = "MSG_CSV_REALIP";
	private static final String MSG_CSV_REALPORT = "MSG_CSV_REALPORT";
	private static final String MSG_CSV_REALSTATUS = "MSG_CSV_REALSTATUS";
	private static final String MSG_CSV_ACTION = "MSG_CSV_ACTION";
	private static final String MSG_CSV_VIP = "MSG_CSV_VIP";
	private static final String MSG_CSV_VIPNAME = "MSG_CSV_VIPNAME";
	private static final String MSG_CSV_VPORT = "MSG_CSV_VPORT";
	private static final String MSG_CSV_VSTATUS = "MSG_CSV_VSTATUS";
	private static final String MSG_CSV_PROTOCOL = "MSG_CSV_PROTOCOL";
	private static final String MSG_CSV_CPU = "MSG_CSV_CPU";
	private static final String MSG_CSV_SP = "MSG_CSV_SP";
	private static final String MSG_CSV_CPUUSAGE = "MSG_CSV_CPUUSAGE";
	private static final String MSG_CSV_MEMUSAGE = "MSG_CSV_MEMUSAGE";
	private static final String MSG_CSV_THRU = "MSG_CSV_THRU";
	private static final String MSG_CSV_SESSION = "MSG_CSV_SESSION";
	private static final String MSG_CSV_SSL = "MSG_CSV_SSL";
	private static final String MSG_CSV_HTTP = "MSG_CSV_HTTP";
	private static final String MSG_CSV_FALUT = "MSG_CSV_FALUT";
	private static final String MSG_CSV_FAULT_RESOLUTION = "MSG_CSV_FAULT_RESOLUTION";
	private static final String MSG_CSV_WARNING = "MSG_CSV_WARNING";
	private static final String MSG_CSV_SLB_SESSION = "MSG_CSV_SLB_SESSION";
	private static final String MSG_CSV_FLB_SESSION = "MSG_CSV_FLB_SESSION";
	private static final String MSG_CSV_ENV_COUNT = "MSG_ENV_COUNT";
	private static final String MSG_CSV_TPS = "MSG_CSV_TPS";
	private static final String MSG_CSV_RPS = "MSG_CSV_RPS";
	private static final String MSG_CSV_MODEL = "MSG_CSV_MODEL";
	private static final String MSG_CSV_HOSTNAME = "MSG_CSV_HOSTNAME";
	private static final String MSG_CSV_RPORT = "MSG_CSV_RPORT";
	private static final String MSG_CSV_METRIC = "MSG_CSV_METRIC";
	private static final String MSG_CSV_GROUP = "MSG_CSV_GROUP";

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String[] header;
	private List<String[]> data;
	private String[] dataText;
	private ArrayList<String> headerMon;
	private ArrayList<String> dataMon;
	private ArrayList<Integer> dataVal;

	private static Properties msgProps = null;

	public void setHeader(String[] header) {
		this.header = header;
	}

	public String[] getDataText() {
		return dataText;
	}

	public void setDataText(String[] dataText) {
		this.dataText = dataText;
	}

	public void setData(List<String[]> data) {
		this.data = data;
	}

	public void initWithfilterInfo(ArrayList<FlbFilterInfoDto> filterInfoList) {
		if (CollectionUtils.isEmpty(filterInfoList)) {
			return;
		}
		header = new String[] { "Index", "상태", "Name", "Sip", "Smask", "Dip", "Dmask", "Protocol", "Sport", "Dport",
				"Action", "Group", "Redirection" };

		data = new ArrayList<String[]>(filterInfoList.size());
		for (FlbFilterInfoDto filterInfo : filterInfoList) {
			String filterStatus = "";
			String SrcPort = "";
			String DstPort = "";
			if (filterInfo.getState() == 1) {
				filterStatus = "ENABLE";
			} else {
				filterStatus = "DISABLE";
			}
			if (filterInfo.getSrcPortFrom() == filterInfo.getSrcPortTo()) {
				SrcPort = filterInfo.getSrcPortFrom().toString();
			} else {
				SrcPort = filterInfo.getSrcPortFrom().toString() + "-" + filterInfo.getSrcPortTo().toString();
			}
			if (filterInfo.getDstPortFrom() == filterInfo.getDstPortTo()) {
				DstPort = filterInfo.getDstPortFrom().toString();
			} else {
				DstPort = filterInfo.getDstPortFrom().toString() + "-" + filterInfo.getDstPortTo().toString();
			}
			String[] row = { filterInfo.getFilterId().toString(), filterStatus, filterInfo.getName(),
					filterInfo.getSrcIP(), filterInfo.getSrcMask(), filterInfo.getDstIP(), filterInfo.getDstMask(),
					filterInfo.getProtocol(), SrcPort, DstPort, filterInfo.getAction(), filterInfo.getGroup(),
					filterInfo.getRedirection() };
			data.add(row);
		}

	}

	// 구 인터페이스 내보내기 (2015.02.05 부터 미사용 yh.yang)
	public void initWithStatisticContents(List<Statistic5GraphDataDto> portInfo) {
		if (CollectionUtils.isEmpty(portInfo)) {
			return;
		}

//		header = new String[]{"발생 날짜"," 인터페이스 이름", 
//									"Bytes In", "Bytes Out", "Bytes Total",
//									"Packets In", "Packets Out", "Packets Total", 
//									"Errors In", "Erros Out", "Erros Total",
//									"Drops In", "Drops Out", "Drops Total"
//									};

		{
			Statistic5GraphDataDto port = portInfo.get(0);

			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_ORI_DATE), port.getFirstName(), port.getSecondName(),
					port.getThirdName(), port.getFourthName(), port.getFifthName(), port.getSixName(),
					port.getSevenName(), port.getEightName(), port.getNineName(), port.getTenName(),
					port.getElevenName(), port.getTwelveName(), port.getThirteenName(), port.getFourteenName(),
					port.getFifteenName(), port.getSixteenName(), port.getSeventeenName(), port.getEighteenName(),
					port.getNineteenName(), port.getTwentyName(), port.gettOneName(), port.gettTwoName(),
					port.gettThreeName(), port.gettFourName(), port.gettFiveName(), port.gettSixName(),
					port.gettSevenName(), port.gettEightName(), port.gettNineName(), port.getThirtyName(),
					port.getThrOneName(), port.getThrTwoName() };
		}
		data = new ArrayList<String[]>(portInfo.size());

		for (Statistic5GraphDataDto port : portInfo) {
			String[] row = {

					sdf.format(port.getOccurTime()),
					String.valueOf(port.getFirstName() != "" ? port.getFirstValue() : ""),
					String.valueOf(port.getSecondName() != "" ? port.getSecondValue() : ""),
					String.valueOf(port.getThirdName() != "" ? port.getThirdValue() : ""),
					String.valueOf(port.getFourthName() != "" ? port.getFourthValue() : ""),
					String.valueOf(port.getFifthName() != "" ? port.getFifthValue() : ""),
					String.valueOf(port.getSixName() != "" ? port.getSixValue() : ""),
					String.valueOf(port.getSevenName() != "" ? port.getSevenValue() : ""),
					String.valueOf(port.getEightName() != "" ? port.getEightValue() : ""),
					String.valueOf(port.getNineName() != "" ? port.getNineValue() : ""),
					String.valueOf(port.getTenName() != "" ? port.getTenValue() : ""),

					String.valueOf(port.getElevenName() != "" ? port.getElevenValue() : ""),
					String.valueOf(port.getTwelveName() != "" ? port.getTwelveValue() : ""),
					String.valueOf(port.getThirteenName() != "" ? port.getThirteenValue() : ""),
					String.valueOf(port.getFourteenName() != "" ? port.getFourteenValue() : ""),
					String.valueOf(port.getFifteenName() != "" ? port.getFifteenValue() : ""),
					String.valueOf(port.getSixteenName() != "" ? port.getSixteenValue() : ""),
					String.valueOf(port.getSeventeenName() != "" ? port.getSeventeenValue() : ""),
					String.valueOf(port.getEighteenName() != "" ? port.getEighteenValue() : ""),
					String.valueOf(port.getNineteenName() != "" ? port.getNineteenValue() : ""),
					String.valueOf(port.getTwentyName() != "" ? port.getTwentyValue() : ""),

					String.valueOf(port.gettOneName() != "" ? port.gettOneValue() : ""),
					String.valueOf(port.gettTwoName() != "" ? port.gettTwoValue() : ""),
					String.valueOf(port.gettThreeName() != "" ? port.gettThreeValue() : ""),
					String.valueOf(port.gettFourName() != "" ? port.gettFourValue() : ""),
					String.valueOf(port.gettFiveName() != "" ? port.gettFiveValue() : ""),
					String.valueOf(port.gettSixName() != "" ? port.gettSixValue() : ""),
					String.valueOf(port.gettSevenName() != "" ? port.gettSevenValue() : ""),
					String.valueOf(port.gettEightName() != "" ? port.gettEightValue() : ""),
					String.valueOf(port.gettNineName() != "" ? port.gettNineValue() : ""),
					String.valueOf(port.getThirtyName() != "" ? port.getThirtyValue() : ""),

					String.valueOf(port.getThrOneName() != "" ? port.getThrOneValue() : ""),
					String.valueOf(port.getThrTwoName() != "" ? port.getThrTwoValue() : "") };
			data.add(row);
		}
	}

	// ADC Error/Drop Packet 모니터링 내보내기
	public void initWithPktErrorContents(List<OBDtoDataObj> pktErrorInfo) {
		if (CollectionUtils.isEmpty(pktErrorInfo)) {
			return;
		}
		{
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_ORI_DATE),
					CsvMaker.getMessageWeb(MSG_CSV_ENV_COUNT) };
		}
		data = new ArrayList<String[]>(pktErrorInfo.size());

		for (OBDtoDataObj pktError : pktErrorInfo) {
			String[] row = {

					sdf.format(pktError.getOccurTime()), String.valueOf(pktError.getValue()) };
			data.add(row);
		}
	}

	// ADC SSL Transaction 모니터링 내보내기
	public void initWithSslTransactionContents(List<OBDtoDataObj> sslTransactionInfo) {
		if (CollectionUtils.isEmpty(sslTransactionInfo)) {
			return;
		}
		{
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_ORI_DATE), CsvMaker.getMessageWeb(MSG_CSV_TPS) };
		}
		data = new ArrayList<String[]>(sslTransactionInfo.size());

		for (OBDtoDataObj sslTransaction : sslTransactionInfo) {
			String[] row = {

					sdf.format(sslTransaction.getOccurTime()), String.valueOf(sslTransaction.getValue()) };
			data.add(row);
		}
	}

	// ADC HTTP Request 모니터링 내보내기
	public void initWithHttpRequestContents(List<OBDtoDataObj> pktErrorInfo) {
		if (CollectionUtils.isEmpty(pktErrorInfo)) {
			return;
		}
		{
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_ORI_DATE), CsvMaker.getMessageWeb(MSG_CSV_RPS) };
		}
		data = new ArrayList<String[]>(pktErrorInfo.size());

		for (OBDtoDataObj pktError : pktErrorInfo) {
			String[] row = {

					sdf.format(pktError.getOccurTime()), String.valueOf(pktError.getValue()) };
			data.add(row);
		}
	}

	// ADC ConcurrentSessions 모니터링 내보내기
	public void initWithConcurrentSessionsContents(List<OBDtoFlbSlbSession> concurrSessionInfo) {
		if (CollectionUtils.isEmpty(concurrSessionInfo)) {
			return;
		}
		{
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_ORI_DATE),
					CsvMaker.getMessageWeb(MSG_CSV_SLB_SESSION), CsvMaker.getMessageWeb(MSG_CSV_FLB_SESSION) };
		}
		data = new ArrayList<String[]>(concurrSessionInfo.size());

		for (OBDtoFlbSlbSession conSession : concurrSessionInfo) {
			String[] row = {

					sdf.format(conSession.getOccurTime()), String.valueOf(conSession.getSlbSession().longValue()),
					String.valueOf(conSession.getFlbSession().longValue()) };
			data.add(row);
		}
	}

	// ADC Bps 모니터링 내보내기
	public void initWithBpsContents(List<OBDtoDataObj> bpsInfo) {
		if (CollectionUtils.isEmpty(bpsInfo)) {
			return;
		}
		{
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_ORI_DATE), CsvMaker.getMessageWeb(MSG_CSV_BPS) };
		}
		data = new ArrayList<String[]>(bpsInfo.size());

		for (OBDtoDataObj bps : bpsInfo) {
			String[] row = {

					sdf.format(bps.getOccurTime()), String.valueOf(NumberUtil.toStringWithUnit(bps.getValue(), "")) };
			data.add(row);
		}

	}

	// ADC Memory 모니터링 내보내기
	public void initWithMemoryContents(List<OBDtoDataObj> memHistory) {
		if (CollectionUtils.isEmpty(memHistory)) {
			return;
		}
		{
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_ORI_DATE),
					CsvMaker.getMessageWeb(MSG_CSV_MEMUSAGE) };

			data = new ArrayList<String[]>(memHistory.size());

			for (OBDtoDataObj mem : memHistory) {
				String[] row = {

						sdf.format(mem.getOccurTime()), String.valueOf(NumberUtil.toStringLong(mem.getValue())) };
				data.add(row);
			}
		}
	}

	public void initWithSPCpuHistoryContents(List<OBDtoFaultCpuHistory> spCpuHistoryInfo) {
		if (CollectionUtils.isEmpty(spCpuHistoryInfo)) {
			return;
		}
		{
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_ORI_DATE), CsvMaker.getMessageWeb(MSG_CSV_CPUUSAGE),
					"Session" };
		}

		data = new ArrayList<String[]>(spCpuHistoryInfo.size());

		for (OBDtoFaultCpuHistory cpu : spCpuHistoryInfo) {
			String[] row = {

					sdf.format(cpu.getOccurTime()), String.valueOf(NumberUtil.toStringInteger(cpu.getCpuValue())),
					String.valueOf(cpu.getCpuConns()) };
			data.add(row);
		}
	}

	// ADC SP CPU
	public void initWithSPCpuContents(OBDtoFaultCpuHistory spCpuInfo) {
		if (spCpuInfo == null) {
			return;
		}
		{
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_ORI_DATE), CsvMaker.getMessageWeb("SP Cpu"),
					CsvMaker.getMessageWeb("SP Conns") };
		}

		data = new ArrayList<String[]>();

		dataText = new String[] { sdf.format(spCpuInfo.getOccurTime()), String.valueOf(spCpuInfo.getCpu1Value()),
				String.valueOf(spCpuInfo.getCpu1Conns()), String.valueOf(spCpuInfo.getCpu2Value()),
				String.valueOf(spCpuInfo.getCpu2Conns()), String.valueOf(spCpuInfo.getCpu3Value()),
				String.valueOf(spCpuInfo.getCpu3Conns()), String.valueOf(spCpuInfo.getCpu4Value()),
				String.valueOf(spCpuInfo.getCpu4Conns()), String.valueOf(spCpuInfo.getCpu5Value()),
				String.valueOf(spCpuInfo.getCpu5Conns()), String.valueOf(spCpuInfo.getCpu6Value()),
				String.valueOf(spCpuInfo.getCpu6Conns()), String.valueOf(spCpuInfo.getCpu7Value()),
				String.valueOf(spCpuInfo.getCpu7Conns()), String.valueOf(spCpuInfo.getCpu8Value()),
				String.valueOf(spCpuInfo.getCpu8Conns()), String.valueOf(spCpuInfo.getCpu9Value()),
				String.valueOf(spCpuInfo.getCpu9Conns()), String.valueOf(spCpuInfo.getCpu10Value()),
				String.valueOf(spCpuInfo.getCpu10Conns()), String.valueOf(spCpuInfo.getCpu11Value()),
				String.valueOf(spCpuInfo.getCpu11Conns()), String.valueOf(spCpuInfo.getCpu12Value()),
				String.valueOf(spCpuInfo.getCpu12Conns()), String.valueOf(spCpuInfo.getCpu13Value()),
				String.valueOf(spCpuInfo.getCpu13Conns()), String.valueOf(spCpuInfo.getCpu14Value()),
				String.valueOf(spCpuInfo.getCpu14Conns()), String.valueOf(spCpuInfo.getCpu15Value()),
				String.valueOf(spCpuInfo.getCpu15Conns()), String.valueOf(spCpuInfo.getCpu16Value()),
				String.valueOf(spCpuInfo.getCpu16Conns()), String.valueOf(spCpuInfo.getCpu17Value()),
				String.valueOf(spCpuInfo.getCpu17Conns()), String.valueOf(spCpuInfo.getCpu18Value()),
				String.valueOf(spCpuInfo.getCpu18Conns()), String.valueOf(spCpuInfo.getCpu19Value()),
				String.valueOf(spCpuInfo.getCpu19Conns()), String.valueOf(spCpuInfo.getCpu20Value()),
				String.valueOf(spCpuInfo.getCpu20Conns()), String.valueOf(spCpuInfo.getCpu21Value()),
				String.valueOf(spCpuInfo.getCpu21Conns()), String.valueOf(spCpuInfo.getCpu22Value()),
				String.valueOf(spCpuInfo.getCpu22Conns()), String.valueOf(spCpuInfo.getCpu23Value()),
				String.valueOf(spCpuInfo.getCpu23Conns()), String.valueOf(spCpuInfo.getCpu24Value()),
				String.valueOf(spCpuInfo.getCpu24Conns()), String.valueOf(spCpuInfo.getCpu25Value()),
				String.valueOf(spCpuInfo.getCpu25Conns()), String.valueOf(spCpuInfo.getCpu26Value()),
				String.valueOf(spCpuInfo.getCpu26Conns()), String.valueOf(spCpuInfo.getCpu27Value()),
				String.valueOf(spCpuInfo.getCpu27Conns()), String.valueOf(spCpuInfo.getCpu28Value()),
				String.valueOf(spCpuInfo.getCpu28Conns()), String.valueOf(spCpuInfo.getCpu29Value()),
				String.valueOf(spCpuInfo.getCpu29Conns()), String.valueOf(spCpuInfo.getCpu30Value()),
				String.valueOf(spCpuInfo.getCpu30Conns()), String.valueOf(spCpuInfo.getCpu31Value()),
				String.valueOf(spCpuInfo.getCpu31Conns()), String.valueOf(spCpuInfo.getCpu32Value()),
				String.valueOf(spCpuInfo.getCpu32Conns()) };
		data.add(dataText);
	}

	// ADC CPU 모니터링 내보내기
	public void initWithCpuContents(List<OBDtoAdcMonCpuHistory> cpuInfo) {
		if (CollectionUtils.isEmpty(cpuInfo)) {
			return;
		}
		{
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_ORI_DATE), "CPU1_USAGE", "CPU2 USAGE", "CPU3_USAGE",
					"CPU4_USAGE", "CPU5_USAGE", "CPU6_USAGE", "CPU7_USAGE", "CPU8_USAGE", "CPU9_USAGE", "CPU10_USAGE",
					"CPU11_USAGE", "CPU12_USAGE", "CPU13_USAGE", "CPU14_USAGE", "CPU15_USAGE", "CPU15_USAGE",
					"CPU16_USAGE", "CPU17_USAGE", "CPU18_USAGE", "CPU19_USAGE", "CPU20_USAGE", "CPU21_USAGE",
					"CPU22_USAGE", "CPU23_USAGE", "CPU24_USAGE", "CPU25_USAGE", "CPU26_USAGE", "CPU27_USAGE",
					"CPU28_USAGE", "CPU29_USAGE", "CPU30_USAGE", "CPU31_USAGE", "CPU32_USAGE" };
		}
//        data = new ArrayList<String[]>(cpuInfo.size());

//        data = new ArrayList<String[]>(cpuInfo.size());
//        String[] row =  new String[cpuInfo.size()+1];
//        row[0] = cpuInfo.get(0).getOccurTime().toString();
//        for(int i=1; i < cpuInfo.size();i++)
//        {
//            row[i] = cpuInfo.get(i).getCpus().get(i).toString();
//            data.add(row);
//        }

		data = new ArrayList<String[]>(cpuInfo.size());
		for (int i = 0; i < cpuInfo.size(); i++) {
			ArrayList<String> row = new ArrayList<String>();
			row.add(sdf.format(cpuInfo.get(i).getOccurTime()));
			for (int j = 0; j < cpuInfo.get(i).getCpus().size(); j++) {
				row.add(cpuInfo.get(i).getCpus().get(j).toString());
			}
			String[] rowData = row.toArray(new String[row.size()]);
			data.add(rowData);
		}
	}

	// ADC 내부 인터페이스 모니터링 내보내기 (2015.02.05 부터 사용 yh.yang)
	public void initWithInterfaceContents(List<OBDtoConnectionDataObj> portInfo, AdcDto adc,
			ArrayList<String> interfaceNameList) {
		if (CollectionUtils.isEmpty(portInfo)) {
			return;
		}
		{
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_ORI_DATE), CsvMaker.getMessageWeb(MSG_CSV_IN),
					CsvMaker.getMessageWeb(MSG_CSV_OUT), CsvMaker.getMessageWeb(MSG_CSV_TOTAL) };
		}
		data = new ArrayList<String[]>(portInfo.size());

		for (OBDtoConnectionDataObj port : portInfo) {
			String[] row = {

					sdf.format(port.getOccurTime()), String.valueOf(NumberUtil.toStringWithUnit(port.getInValue(), "")),
					String.valueOf(NumberUtil.toStringWithUnit(port.getOutValue(), "")),
					String.valueOf(NumberUtil.toStringWithUnit(port.getTotalValue(), "")) };
			data.add(row);
		}
	}

	public void initWithResponseTimeHistory(List<OBDtoDataObj> logs) {
		if (CollectionUtils.isEmpty(logs)) {
			return;
		}
		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), CsvMaker.getMessageWeb(MSG_CSV_RES_TIME) };
		data = new ArrayList<String[]>(logs.size());

		for (OBDtoDataObj log : logs) {
			String[] row = { sdf.format(log.getOccurTime()), log.getValue().toString() };
			data.add(row);
		}
	}
//	public void initWithBpsConnHistory(List<OBDtoFaultBpsConnInfo> logs) 
//	{
//		if (CollectionUtils.isEmpty(logs))
//		{
//			return;
//		}		
//		header = new String[]{CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), CsvMaker.getMessageWeb(MSG_CSV_BPS_IN), CsvMaker.getMessageWeb(MSG_CSV_BPS_OUT), CsvMaker.getMessageWeb(MSG_CSV_BPS_TOTAL), CsvMaker.getMessageWeb(MSG_CSV_CONN)};
//		data = new ArrayList<String[]>(logs.size());
//		for (OBDtoFaultBpsConnInfo log : logs)
//		{
//			String[] row = {
//				sdf.format(log.getConnCurrValue().getOccurTime()),
//				log.getBpsInValue().getValue().toString().replace("-1", "0"),
//				log.getBpsOutValue().getValue().toString().replace("-1", "0"),
//				log.getBpsTotValue().getValue().toString().replace("-1", "0"),
//				log.getConnCurrValue().getValue().toString().replace("-1", "0")
//			};
//			data.add(row);
//		}
//	}

	public void initWithBpsConnHistory(List<OBDtoFaultBpsConnData> logs, Integer bpsVal, Integer preVal,
			Integer connVal) {
		if (CollectionUtils.isEmpty(logs)) {
			return;
		}

		String headerBpsVal = "";
		String headerConnVal = "";
		String preHeaderVal = "";

		if (bpsVal == 0) {
			headerBpsVal = CsvMaker.getMessageWeb(MSG_CSV_BPS_IN);
		} else if (bpsVal == 1) {
			headerBpsVal = CsvMaker.getMessageWeb(MSG_CSV_BPS_OUT);
		} else if (bpsVal == 2) {
			headerBpsVal = CsvMaker.getMessageWeb(MSG_CSV_BPS_TOTAL); // CsvMaker.getMessageWeb(MSG_CSV_CONN)
		}

		headerConnVal = CsvMaker.getMessageWeb(MSG_CSV_CONN);

		if (preVal == 0) {
			preHeaderVal = CsvMaker.getMessageWeb(MSG_CSV_PREDAY);
		} else if (preVal == 1) {
			preHeaderVal = CsvMaker.getMessageWeb(MSG_CSV_PREWEEKS);
		} else if (preVal == 2) {
			preHeaderVal = CsvMaker.getMessageWeb(MSG_CSV_PREMONTH);
		} else if (preVal == 3) {
			preHeaderVal = CsvMaker.getMessageWeb(MSG_CSV_PREDAYCHK);
		}

		if (preVal == -1) {
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), headerBpsVal, headerConnVal };
		} else {
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), headerBpsVal, preHeaderVal,
					headerConnVal, preHeaderVal };
		}

//        header = new String[]{CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), headerBpsVal, preHeaderVal};
		data = new ArrayList<String[]>(logs.size());

		if (preVal == -1) {
			for (OBDtoFaultBpsConnData log : logs) {
				if (bpsVal == 0) {
					String[] row = { sdf.format(log.getOccurTime()), log.getBpsInValue().toString().replace("-1", "0"),
							log.getConnCurrValue().toString().replace("-1", "0") };
					data.add(row);
				} else if (bpsVal == 1) {
					String[] row = { sdf.format(log.getOccurTime()), log.getBpsOutValue().toString().replace("-1", "0"),
							log.getConnCurrValue().toString().replace("-1", "0") };
					data.add(row);
				} else if (bpsVal == 2) {
					String[] row = { sdf.format(log.getOccurTime()), log.getBpsTotValue().toString().replace("-1", "0"),
							log.getConnCurrValue().toString().replace("-1", "0") };
					data.add(row);
				}
			}
		} else {
			for (OBDtoFaultBpsConnData log : logs) {
				if (bpsVal == 0) {
					String[] row = { sdf.format(log.getOccurTime()), log.getBpsInValue().toString().replace("-1", "0"),
							log.getPreBpsInValue().toString().replace("-1", "0"),
							log.getConnCurrValue().toString().replace("-1", "0"),
							log.getPreConnCurrValue().toString().replace("-1", "0") };
					data.add(row);
				} else if (bpsVal == 1) {
					String[] row = { sdf.format(log.getOccurTime()), log.getBpsOutValue().toString().replace("-1", "0"),
							log.getPreBpsOutValue().toString().replace("-1", "0"),
							log.getConnCurrValue().toString().replace("-1", "0"),
							log.getPreConnCurrValue().toString().replace("-1", "0") };
					data.add(row);
				} else if (bpsVal == 2) {
					String[] row = { sdf.format(log.getOccurTime()), log.getBpsTotValue().toString().replace("-1", "0"),
							log.getPreBpsOutValue().toString().replace("-1", "0"),
							log.getConnCurrValue().toString().replace("-1", "0"),
							log.getPreConnCurrValue().toString().replace("-1", "0") };
					data.add(row);
				}
			}
		}
	}

	public void initWithBpsConnResponseHistory(List<OBDtoFaultBpsConnData> logs,
			List<OBDtoFaultBpsConnData> respTimeLogs, Integer bpsVal, Integer preVal, Integer connVal) {
		if (CollectionUtils.isEmpty(logs)) {
			return;
		}

		String headerBpsVal = "";
		String headerConnVal = "";
		String headerRespTimeVal = "";
		String preHeaderVal = "";

		if (bpsVal == 0) {
			headerBpsVal = CsvMaker.getMessageWeb(MSG_CSV_BPS_IN);
		} else if (bpsVal == 1) {
			headerBpsVal = CsvMaker.getMessageWeb(MSG_CSV_BPS_OUT);
		} else if (bpsVal == 2) {
			headerBpsVal = CsvMaker.getMessageWeb(MSG_CSV_BPS_TOTAL); // CsvMaker.getMessageWeb(MSG_CSV_CONN)
		}

		headerConnVal = CsvMaker.getMessageWeb(MSG_CSV_CONN);

		if (preVal == 0) {
			preHeaderVal = CsvMaker.getMessageWeb(MSG_CSV_PREDAY);
		} else if (preVal == 1) {
			preHeaderVal = CsvMaker.getMessageWeb(MSG_CSV_PREWEEKS);
		} else if (preVal == 2) {
			preHeaderVal = CsvMaker.getMessageWeb(MSG_CSV_PREMONTH);
		} else if (preVal == 3) {
			preHeaderVal = CsvMaker.getMessageWeb(MSG_CSV_PREDAYCHK);
		}

		headerRespTimeVal = CsvMaker.getMessageWeb(MSG_CSV_RES_TIME);
		if (preVal == -1) {
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), headerBpsVal, headerConnVal,
					CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), headerRespTimeVal };
		} else {
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), headerBpsVal, preHeaderVal,
					headerConnVal, preHeaderVal, CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), headerRespTimeVal,
					preHeaderVal };
		}

//        header = new String[]{CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), headerBpsVal, preHeaderVal};
		int bpsSize = logs.size();
		int respSize = respTimeLogs.size();
		int tmpSize = 0;
		data = new ArrayList<String[]>(bpsSize + respSize);

		if (bpsSize > respSize) {
			tmpSize = bpsSize;
		} else {
			tmpSize = respSize;
		}
		if (preVal == -1) {
			for (int i = 0; i < tmpSize; i++) {
				if (bpsVal == 0) {
					List<String> columnList = new ArrayList<String>();
					if (bpsSize > i && logs.get(i) != null) {
						columnList.add(sdf.format(logs.get(i).getOccurTime()));
						columnList.add(logs.get(i).getBpsInValue().toString().replace("-1", "0"));
						columnList.add(logs.get(i).getConnCurrValue().toString().replace("-1", "0"));
					}
					if (respSize > i && respTimeLogs.get(i) != null) {
						columnList.add(sdf.format(respTimeLogs.get(i).getOccurTime()));
						columnList.add(respTimeLogs.get(i).getRespTimeValue().toString().replace("-1", "0"));
					}

					// List -> String[]
					String[] sArrays = columnList.toArray(new String[columnList.size()]);
					data.add(sArrays);
				} else if (bpsVal == 1) {
					List<String> columnList = new ArrayList<String>();
					if (bpsSize > i && logs.get(i) != null) {
						columnList.add(sdf.format(logs.get(i).getOccurTime()));
						columnList.add(logs.get(i).getBpsOutValue().toString().replace("-1", "0"));
						columnList.add(logs.get(i).getConnCurrValue().toString().replace("-1", "0"));
					}
					if (respSize > i && respTimeLogs.get(i) != null) {
						columnList.add(sdf.format(respTimeLogs.get(i).getOccurTime()));
						columnList.add(respTimeLogs.get(i).getRespTimeValue().toString().replace("-1", "0"));
					}

					// List -> String[]
					String[] sArrays = columnList.toArray(new String[columnList.size()]);
					data.add(sArrays);
				} else if (bpsVal == 2) {
					List<String> columnList = new ArrayList<String>();
					if (bpsSize > i && logs.get(i) != null) {
						columnList.add(sdf.format(logs.get(i).getOccurTime()));
						columnList.add(logs.get(i).getBpsTotValue().toString().replace("-1", "0"));
						columnList.add(logs.get(i).getConnCurrValue().toString().replace("-1", "0"));
					}
					if (respSize > i && respTimeLogs.get(i) != null) {
						columnList.add(sdf.format(respTimeLogs.get(i).getOccurTime()));
						columnList.add(respTimeLogs.get(i).getRespTimeValue().toString().replace("-1", "0"));
					}

					// List -> String[]
					String[] sArrays = columnList.toArray(new String[columnList.size()]);
					data.add(sArrays);
				}
			}
		} else {
			for (int i = 0; i < tmpSize; i++) {
				if (bpsVal == 0) {
					List<String> columnList = new ArrayList<String>();
					if (bpsSize > i && logs.get(i) != null) {
						columnList.add(sdf.format(logs.get(i).getOccurTime()));
						columnList.add(logs.get(i).getBpsInValue().toString().replace("-1", "0"));
						columnList.add(logs.get(i).getPreBpsInValue().toString().replace("-1", "0"));
						columnList.add(logs.get(i).getConnCurrValue().toString().replace("-1", "0"));
						columnList.add((logs.get(i).getPreConnCurrValue().toString().replace("-1", "0")));
					}
					if (respSize > i && respTimeLogs.get(i) != null) {
						columnList.add(sdf.format(respTimeLogs.get(i).getOccurTime()));
						columnList.add(respTimeLogs.get(i).getRespTimeValue().toString().replace("-1", "0"));
						columnList.add(respTimeLogs.get(i).getPreRespTimeValue().toString().replace("-1", "0"));
					}

					// List -> String[]
					String[] sArrays = columnList.toArray(new String[columnList.size()]);
					data.add(sArrays);
				} else if (bpsVal == 1) {
					List<String> columnList = new ArrayList<String>();
					if (bpsSize > i && logs.get(i) != null) {
						columnList.add(sdf.format(logs.get(i).getOccurTime()));
						columnList.add(logs.get(i).getBpsOutValue().toString().replace("-1", "0"));
						columnList.add(logs.get(i).getPreBpsOutValue().toString().replace("-1", "0"));
						columnList.add(logs.get(i).getConnCurrValue().toString().replace("-1", "0"));
						columnList.add((logs.get(i).getPreConnCurrValue().toString().replace("-1", "0")));
					}
					if (respSize > i && respTimeLogs.get(i) != null) {
						columnList.add(sdf.format(respTimeLogs.get(i).getOccurTime()));
						columnList.add(respTimeLogs.get(i).getRespTimeValue().toString().replace("-1", "0"));
						columnList.add(respTimeLogs.get(i).getPreRespTimeValue().toString().replace("-1", "0"));
					}

					// List -> String[]
					String[] sArrays = columnList.toArray(new String[columnList.size()]);
					data.add(sArrays);
				} else if (bpsVal == 2) {
					List<String> columnList = new ArrayList<String>();
					if (bpsSize > i && logs.get(i) != null) {
						columnList.add(sdf.format(logs.get(i).getOccurTime()));
						columnList.add(logs.get(i).getBpsTotValue().toString().replace("-1", "0"));
						columnList.add(logs.get(i).getPreBpsOutValue().toString().replace("-1", "0"));
						columnList.add(logs.get(i).getConnCurrValue().toString().replace("-1", "0"));
						columnList.add((logs.get(i).getPreConnCurrValue().toString().replace("-1", "0")));
					}
					if (respSize > i && respTimeLogs.get(i) != null) {
						columnList.add(sdf.format(respTimeLogs.get(i).getOccurTime()));
						columnList.add(respTimeLogs.get(i).getRespTimeValue().toString().replace("-1", "0"));
						columnList.add(respTimeLogs.get(i).getPreRespTimeValue().toString().replace("-1", "0"));
					}

					// List -> String[]
					String[] sArrays = columnList.toArray(new String[columnList.size()]);
					data.add(sArrays);
				}
			}
		}
	}

	public void initWithBpsConnResponseHistory11(List<OBDtoFaultBpsConnData> logs,
			List<OBDtoFaultBpsConnData> respTimeLogs, Integer bpsVal, Integer preVal, Integer connVal) {
		if (CollectionUtils.isEmpty(logs)) {
			return;
		}

		String headerBpsVal = "";
		String headerConnVal = "";
		String headerRespTimeVal = "";
		String preHeaderVal = "";

		if (bpsVal == 0) {
			headerBpsVal = CsvMaker.getMessageWeb(MSG_CSV_BPS_IN);
		} else if (bpsVal == 1) {
			headerBpsVal = CsvMaker.getMessageWeb(MSG_CSV_BPS_OUT);
		} else if (bpsVal == 2) {
			headerBpsVal = CsvMaker.getMessageWeb(MSG_CSV_BPS_TOTAL); // CsvMaker.getMessageWeb(MSG_CSV_CONN)
		}

		headerConnVal = CsvMaker.getMessageWeb(MSG_CSV_CONN);

		if (preVal == 0) {
			preHeaderVal = CsvMaker.getMessageWeb(MSG_CSV_PREDAY);
		} else if (preVal == 1) {
			preHeaderVal = CsvMaker.getMessageWeb(MSG_CSV_PREWEEKS);
		} else if (preVal == 2) {
			preHeaderVal = CsvMaker.getMessageWeb(MSG_CSV_PREMONTH);
		} else if (preVal == 3) {
			preHeaderVal = CsvMaker.getMessageWeb(MSG_CSV_PREDAYCHK);
		}

//        if(connVal == 1)
//        {
//            headerRespTimeVal = CsvMaker.getMessageWeb(MSG_CSV_RES_TIME);  
//            header = new String[]{CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), headerBpsVal, preHeaderVal, headerConnVal, preHeaderVal, headerRespTimeVal, preHeaderVal};
//        }
//        else 
//        {
//            headerRespTimeVal = "";        
//            header = new String[]{CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), headerBpsVal, preHeaderVal, headerConnVal, preHeaderVal};
//        }

		headerRespTimeVal = CsvMaker.getMessageWeb(MSG_CSV_RES_TIME);

		if (preVal == -1) {
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), headerBpsVal, headerConnVal,
					headerRespTimeVal };
		} else {
			header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), headerBpsVal, preHeaderVal,
					headerConnVal, preHeaderVal, headerRespTimeVal, preHeaderVal };
		}

//        header = new String[]{CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), headerBpsVal, preHeaderVal};
//        data = new ArrayList<String[]>(logs.size()+respTimeLogs.size());
		data = new ArrayList<String[]>(logs.size());
		List<String[]> data1 = new ArrayList<String[]>(respTimeLogs.size());
//        List<String[]> dataMon = new ArrayList<String[]>();

		if (preVal == -1) {
			for (OBDtoFaultBpsConnData log : logs) {
				if (bpsVal == 0) {
					String[] row = { sdf.format(log.getOccurTime()), log.getBpsInValue().toString().replace("-1", "0"),
							log.getConnCurrValue().toString().replace("-1", "0") };
					data.add(row);
				} else if (bpsVal == 1) {
					String[] row = { sdf.format(log.getOccurTime()), log.getBpsOutValue().toString().replace("-1", "0"),
							log.getConnCurrValue().toString().replace("-1", "0") };
					data.add(row);
				} else if (bpsVal == 2) {
					String[] row = { sdf.format(log.getOccurTime()), log.getBpsTotValue().toString().replace("-1", "0"),
							log.getConnCurrValue().toString().replace("-1", "0") };
					data.add(row);
				}
			}

			for (OBDtoFaultBpsConnData log : respTimeLogs) {
				String[] row = { sdf.format(log.getOccurTime()),

						log.getRespTimeValue().toString().replace("-1", "0") };
				data1.add(row);
				// dataMon.add(row);
			}

			data.addAll(data1);
			// String[] dataStr = (String[]) dataMon.toArray(new String[dataMon.size()]);
			// data.add(dataStr);
		} else {
			for (OBDtoFaultBpsConnData log : logs) {
				if (bpsVal == 0) {
					String[] row = { sdf.format(log.getOccurTime()), log.getBpsInValue().toString().replace("-1", "0"),
							log.getPreBpsInValue().toString().replace("-1", "0"),
							log.getConnCurrValue().toString().replace("-1", "0"),
							log.getPreConnCurrValue().toString().replace("-1", "0") };
					data.add(row);
				} else if (bpsVal == 1) {
					String[] row = { sdf.format(log.getOccurTime()), log.getBpsOutValue().toString().replace("-1", "0"),
							log.getPreBpsOutValue().toString().replace("-1", "0"),
							log.getConnCurrValue().toString().replace("-1", "0"),
							log.getPreConnCurrValue().toString().replace("-1", "0") };
					data.add(row);
				} else if (bpsVal == 2) {
					String[] row = { sdf.format(log.getOccurTime()), log.getBpsTotValue().toString().replace("-1", "0"),
							log.getPreBpsOutValue().toString().replace("-1", "0"),
							log.getConnCurrValue().toString().replace("-1", "0"),
							log.getPreConnCurrValue().toString().replace("-1", "0") };
					data.add(row);
				}
			}

			for (OBDtoFaultBpsConnData log : respTimeLogs) {
				String[] row = { sdf.format(log.getOccurTime()),

						log.getRespTimeValue().toString().replace("-1", "0"),
						log.getPreRespTimeValue().toString().replace("-1", "0") };
				data1.add(row);
				// dataMon.add(row);
			}

			data.addAll(data1);
		}
	}

	public void initWithFlbBpsConnHistory(List<OBDtoFaultBpsConnInfo> logs) {
		if (CollectionUtils.isEmpty(logs)) {
			return;
		}
		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), CsvMaker.getMessageWeb(MSG_CSV_BPS),
				CsvMaker.getMessageWeb(MSG_CSV_CONN) };
		data = new ArrayList<String[]>(logs.size());
		for (OBDtoFaultBpsConnInfo log : logs) {
			String[] row = { sdf.format(log.getConnCurrValue().getOccurTime()), log.getBpsValue().getValue().toString(),
					log.getConnCurrValue().getValue().toString() };
			data.add(row);
		}
	}

	public void initWithAdcMonTotalList(List<OBDtoMonTotalAdcOne> logs, OBDtoMonTotalAdcCondition condition) {
		headerMon = new ArrayList<String>();
		String headerText = "";
		dataVal = new ArrayList<Integer>();

		if (condition.getStatus().isSelect() == true) {
			headerText = "Status";
			headerMon.add(headerText);
			dataVal.add(0);
		}
		if (condition.getType().isSelect() == true) {
			headerText = "종류";
			headerMon.add(headerText);
			dataVal.add(1);
		}
		if (condition.getName().isSelect() == true) {
			headerText = "ADC 이름";
			headerMon.add(headerText);
			dataVal.add(2);
		}
		if (condition.getActiveBackupState().isSelect() == true) {
			headerText = "이중화상태";
			headerMon.add(headerText);
			dataVal.add(3);
		}
		if (condition.getModel().isSelect() == true) {
			headerText = "장비명";
			headerMon.add(headerText);
			dataVal.add(4);
		}
		if (condition.getSwVersion().isSelect() == true) {
			headerText = "OS버전";
			headerMon.add(headerText);
			dataVal.add(5);
		}
		if (condition.getThroughput().isSelect() == true) {
			headerText = "Concurrent Seesions(개)";
			headerMon.add(headerText);
			dataVal.add(6);
		}
		if (condition.getConcurrentSession().isSelect() == true) {
			headerText = "Throughput(bps)";
			headerMon.add(headerText);
			dataVal.add(7);
		}
		if (condition.getUptimeAge().isSelect() == true) {
			headerText = "Uptime";
			headerMon.add(headerText);
			dataVal.add(8);
		}
		if (condition.getAdcIp().isSelect() == true) {
			headerText = "ADC IP";
			headerMon.add(headerText);
			dataVal.add(9);
		}

		if (condition.getConfigTime().isSelect() == true) {
			headerText = "최근 업데이트";
			headerMon.add(headerText);
			dataVal.add(10);
		}
		if (condition.getCpu().isSelect() == true) {
			headerText = "CPU(%)";
			headerMon.add(headerText);
			dataVal.add(11);
		}
		if (condition.getMemory().isSelect() == true) {
			headerText = "Memory(%)";
			headerMon.add(headerText);
			dataVal.add(12);
		}
		if (condition.getErrorPackets().isSelect() == true) {
			headerText = "Error Packets(개)";
			headerMon.add(headerText);
			dataVal.add(13);
		}
		if (condition.getDropPackets().isSelect() == true) {
			headerText = "Dropped Packets(개)";
			headerMon.add(headerText);
			dataVal.add(14);
		}
		if (condition.getSslCertValidDays().isSelect() == true) {
			headerText = "인증서(개)";
			headerMon.add(headerText);
			dataVal.add(15);
		}
		if (condition.getInterfaceCount().isSelect() == true) {
			headerText = "인터페이스 정보(개)";
			headerMon.add(headerText);
			dataVal.add(16);
		}
		if (condition.getFilter().isSelect() == true) {
			headerText = "FLB 정보(개)";
			headerMon.add(headerText);
			dataVal.add(17);
		}
		if (condition.getService().isSelect() == true) {
			headerText = "서비스 정보(개)";
			headerMon.add(headerText);
			dataVal.add(18);
		}
		if (condition.getAdcLog24Hour().isSelect() == true) {
			headerText = "ADC로그(개)";
			headerMon.add(headerText);
			dataVal.add(19);
		}
		if (condition.getSlbConfig24Hour().isSelect() == true) {
			headerText = "설정 이력 정보(개)";
			headerMon.add(headerText);
			dataVal.add(20);
		}

//		if(condition.getDropPackets().isSelect() == true)
//	    {
//			headerText = "Dropped Packets(개)"; 
//	        headerMon.add(headerText);
//	        row.add(adcStatus)
//	    }
//	    if(condition.getSslCertValidDays().isSelect() == true)
//	    {
//	    	headerText = "인증서 만료일(개)"; 
//	        headerMon.add(headerText);
//	        if(log.getActiveBackupState() == null)
//	        	row.add("-");
//	        else
//	        	row.add(log.getActiveBackupState().toString())
//	    }
		header = (String[]) headerMon.toArray(new String[headerMon.size()]);
		data = new ArrayList<String[]>();
		dataMon = new ArrayList<String>();

		if (CollectionUtils.isEmpty(logs)) {
			return;
		}

		for (OBDtoMonTotalAdcOne log : logs) {
			String adcStatus = "";
			if (log.getStatus() == 1)
				adcStatus = "정상";
			else
				adcStatus = "단절";

			String activeBackupState = "";
			if (log.getActiveBackupState() == 1)
				activeBackupState = "Active";
			else if (log.getActiveBackupState() == 2)
				activeBackupState = "Standby";
			else
				activeBackupState = "Unknown";

			String adcType = "";
			if (log.getAdcType() == 1)
				adcType = "F5";
			else if (log.getAdcType() == 2)
				adcType = "Alteon";
			else if (log.getAdcType() == 3)
				adcType = "PAS";
			else if (log.getAdcType() == 4)
				adcType = "PASK";
			else
				adcType = "Unknown";

			dataMon = new ArrayList<String>();
			for (int dataV : dataVal) {
				if (dataV == 0)
					dataMon.add(adcStatus);
				else if (dataV == 1)
					dataMon.add(adcType);
				else if (dataV == 2)
					dataMon.add(log.getAdcName());
				else if (dataV == 3)
					dataMon.add(activeBackupState);
				else if (dataV == 4)
					dataMon.add(log.getModel());
				else if (dataV == 5)
					dataMon.add(log.getSwVersion());
				else if (dataV == 6)
					dataMon.add((log.getConcurrentSession() == -1) ? "-" : log.getConcurrentSession().toString());
				else if (dataV == 7)
					dataMon.add((log.getThroughput() == -1) ? "-" : log.getThroughput().toString());
				else if (dataV == 8)
					dataMon.add((log.getUptimeAge() == null) ? "-" : log.getUptimeAge());
				else if (dataV == 9)
					dataMon.add(log.getAdcIp());
				else if (dataV == 10)
					dataMon.add((log.getConfigTime() == null) ? "-" : sdf.format(log.getConfigTime()));
				else if (dataV == 11)
					dataMon.add((log.getCpu() == -1) ? "-" : log.getCpu().toString());
				else if (dataV == 12)
					dataMon.add((log.getMemory() == -1) ? "-" : log.getMemory().toString());
				else if (dataV == 13)
					dataMon.add((log.getErrorPackets() == -1) ? "-" : log.getErrorPackets().toString());
				else if (dataV == 14)
					dataMon.add((log.getDropPackets() == -1) ? "-" : log.getDropPackets().toString());
				else if (dataV == 15)
					dataMon.add((log.getSslCertValidDays() == -1) ? "-" : log.getSslCertValidDays().toString());
				else if (dataV == 16)
					dataMon.add((log.getInterfaceAvailable() == -1) ? "-" : log.getInterfaceAvailable().toString());
				else if (dataV == 17)
					dataMon.add((log.getFilterUse() == -1) ? "-" : log.getFilterUse().toString());
				else if (dataV == 18)
					dataMon.add((log.getServiceAvailable() == -1) ? "-" : log.getServiceAvailable().toString());
				else if (dataV == 19)
					dataMon.add((log.getAdcLog24Hour() == null) ? "-" : log.getAdcLog24Hour().toString());
				else if (dataV == 20)
					dataMon.add((log.getSlbConfig24Hour() == null) ? "-" : log.getSlbConfig24Hour().toString());
			}
			String[] dataStr = (String[]) dataMon.toArray(new String[dataMon.size()]);
			data.add(dataStr);
//			String[] row = {
//				adcStatus,
//				log.getAdcName(),
//				activeBackupState,
//				log.getModel(),
//				log.getSwVersion(),				
//				(log.getThroughput() == null) ? "-" : log.getThroughput().toString(),
//				(log.getConcurrentSession() == null) ? "-" : log.getConcurrentSession().toString(),
//				log.getUptimeAge(),
//				log.getAdcIp(),
//				sdf.format(log.getConfigTime()),
//				(log.getCpu() == null) ? "-" : log.getCpu().toString(),
//				(log.getMemory() == null) ? "-" : log.getMemory().toString(),
//				(log.getErrorPackets() == null) ? "-" : log.getErrorPackets().toString(),
//				(log.getDropPackets() == null) ? "-" : log.getDropPackets().toString(),
//				(log.getSslCertValidDays() == null) ? "-" : log.getSslCertValidDays().toString(),
//				(log.getInterfaceAvailable() == null) ? "-" : log.getInterfaceAvailable().toString(),				
//				(log.getFilterUse() == null) ? "-" : log.getFilterUse().toString(),				
//				(log.getServiceAvailable() == null) ? "-" : log.getServiceAvailable().toString(),
//				(log.getAdcLog24Hour() == null) ? "-" : log.getAdcLog24Hour().toString(),
//				(log.getSlbConfig24Hour() == null) ? "-" : log.getSlbConfig24Hour().toString()
//			};
//			data.add(row);		
//			[정상, mulsan_alt_slb1, 1, 2208 E, 25.3.6.0, 22960730, 1247, 160 days, 112.106.132.77, 2014-11-10 17:37:08, -, 0, 0, 0, 0, 0, 0, 13, 0, 0]
		}
	}

	public void initWithAdcMonTotalList1(List<OBDtoMonTotalAdcOne> logs, OBDtoMonTotalAdcCondition condition) {
//		private String[] header;
//		String[] array= new String[]{};
//		header = new String[]{};	

		String headText = "";
		if (condition.getStatus().isSelect() == true) {
//			String[] array = new String[]{"Staus"};
//			headerMon.add(array);
			String value = "Status";
			headText = headText.concat(value);
//			header = new String[]{value};			
		}
		if (condition.getName().isSelect() == true) {
//			String[] array = new String[]{"ADC 이름"};
//			headerMon.add(array);
			String value = "ADC 이름";
			headText = headText.concat(value);
//			header = new String[]{value};

		}
		if (condition.getActiveBackupState().isSelect() == true) {
//			String value = "State";
			headText = headText.concat("State");
		}
		if (condition.getActiveBackupState().isSelect() == true) {
			String value = "장비명";
			headText = headText.concat(value);
		}
		if (condition.getActiveBackupState().isSelect() == true) {
			String value = "버전";
			headText = headText.concat(value);
		}
		if (condition.getActiveBackupState().isSelect() == true) {
			String value = "Throughput(bps)";
			headText = headText.concat(value);
		}
		if (condition.getActiveBackupState().isSelect() == true) {
			String value = "Concurrent Seesions(개)";
			headText = headText.concat(value);
		}
		if (condition.getActiveBackupState().isSelect() == true) {
			String value = "Uptime";
			headText = headText.concat(value);
		}
		if (condition.getActiveBackupState().isSelect() == true) {
			String value = "ADC IP";
			headText = headText.concat(value);
		}
		if (condition.getActiveBackupState().isSelect() == true) {
			String value = "최근 업데이트";
			headText = headText.concat(value);
		}
		if (condition.getActiveBackupState().isSelect() == true) {
			String value = "CPU(%)";
			headText = headText.concat(value);
		}
		if (condition.getActiveBackupState().isSelect() == true) {
			String value = "Memory(%)";
			headText = headText.concat(value);
		}
//		header = new String[]{headText};
//		header = new String[]{"Staus", "Adc이름", "State", "장비명", "버전", "Throughput(bps)", "Concurrent Seesions(개)", "Uptime", "ADC IP", 
//				"최근 업데이트", "CPU(%)", "Memory(%)", "Error Packets(개)", "Dropped Packets(개)", "인증서 만료일(개)", "인터페이스 정보(개)", "FLB 정보(개)", 
//				"서비스 정보(개)", "ADC로그(개)", "설정 이력 정보(개)"};
//		data = new ArrayList<String[]>(logs.size());
		data = new ArrayList<String[]>();

		if (CollectionUtils.isEmpty(logs)) {
			return;
		}

		for (OBDtoMonTotalAdcOne log : logs) {
			String adcStatus = "";
			if (log.getStatus() == 1)
				adcStatus = "정상";
			else
				adcStatus = "단절";

			String activeBackupState = "";
			if (log.getActiveBackupState() == 1)
				activeBackupState = "Active";
			else if (log.getActiveBackupState() == 2)
				activeBackupState = "Standby";
			else
				activeBackupState = "Unknown";

			String[] row = {
//				log.getStatus().toString(),
					adcStatus, log.getAdcName(),
//				(log.getActiveBackupState() == null) ? "-" : log.getActiveBackupState().toString(),
					activeBackupState, log.getModel(), log.getSwVersion(),
					(log.getThroughput() == null) ? "-" : log.getThroughput().toString(),
					(log.getConcurrentSession() == null) ? "-" : log.getConcurrentSession().toString(),
					log.getUptimeAge(), log.getAdcIp(), sdf.format(log.getConfigTime()),
					(log.getCpu() == null) ? "-" : log.getCpu().toString(),
					(log.getMemory() == null) ? "-" : log.getMemory().toString(),
					(log.getErrorPackets() == null) ? "-" : log.getErrorPackets().toString(),
					(log.getDropPackets() == null) ? "-" : log.getDropPackets().toString(),
					(log.getSslCertValidDays() == null) ? "-" : log.getSslCertValidDays().toString(),
					(log.getInterfaceAvailable() == null) ? "-" : log.getInterfaceAvailable().toString(),
					(log.getFilterUse() == null) ? "-" : log.getFilterUse().toString(),
					(log.getServiceAvailable() == null) ? "-" : log.getServiceAvailable().toString(),
					(log.getAdcLog24Hour() == null) ? "-" : log.getAdcLog24Hour().toString(),
					(log.getSlbConfig24Hour() == null) ? "-" : log.getSlbConfig24Hour().toString() };
			data.add(row);
		}
	}

	public void initWithAdcMonTotalListDefault(List<OBDtoMonTotalAdcOne> logs) {
		header = new String[] { "Staus", "Adc이름", "State", "장비명", "버전", "Throughput(bps)", "Concurrent Seesions(개)",
				"Uptime", "ADC IP", "최근 업데이트", "CPU(%)", "Memory(%)", "Error Packets(개)", "Dropped Packets(개)",
				"인증서 만료일(개)", "인터페이스 정보(개)", "FLB 정보(개)", "서비스 정보(개)", "ADC로그(개)", "설정 이력 정보(개)" };
		data = new ArrayList<String[]>(logs.size());

		if (CollectionUtils.isEmpty(logs)) {
			return;
		}

		for (OBDtoMonTotalAdcOne log : logs) {
			String adcStatus = "";
			if (log.getStatus() == 1) {
				adcStatus = "정상";
			} else {
				adcStatus = "단절";
			}

			String[] row = {
//				log.getStatus().toString(),
					adcStatus, log.getAdcName(),
					(log.getActiveBackupState() == null) ? "-" : log.getActiveBackupState().toString(), log.getModel(),
					log.getSwVersion(), (log.getThroughput() == null) ? "-" : log.getThroughput().toString(),
					(log.getConcurrentSession() == null) ? "-" : log.getConcurrentSession().toString(),
					log.getUptimeAge(), log.getAdcIp(), sdf.format(log.getConfigTime()),
					(log.getCpu() == null) ? "-" : log.getCpu().toString(),
					(log.getMemory() == null) ? "-" : log.getMemory().toString(),
					(log.getErrorPackets() == null) ? "-" : log.getErrorPackets().toString(),
					(log.getDropPackets() == null) ? "-" : log.getDropPackets().toString(),
					(log.getSslCertValidDays() == null) ? "-" : log.getSslCertValidDays().toString(),
					(log.getInterfaceAvailable() == null) ? "-" : log.getInterfaceAvailable().toString(),
					(log.getFilterUse() == null) ? "-" : log.getFilterUse().toString(),
					(log.getServiceAvailable() == null) ? "-" : log.getServiceAvailable().toString(),
					(log.getAdcLog24Hour() == null) ? "-" : log.getAdcLog24Hour().toString(),
					(log.getSlbConfig24Hour() == null) ? "-" : log.getSlbConfig24Hour().toString() };
			data.add(row);
		}
	}

	public void initWithAdcMonTotal(OBDtoMonTotalAdc montotalAdc) {

		ArrayList<OBDtoMonTotalAdcOne> logs = montotalAdc.getAdcList();

		header = new String[] { "Staus", "Adc이름", "State", "장비명", "버전", "Throughput(bps)", "Concurrent Seesions(개)",
				"Uptime", "ADC IP", "최근 업데이트", "CPU(%)", "Memory(%)", "Error Packets(개)", "Dropped Packets(개)",
				"인증서 만료일(개)", "인터페이스 정보(개)", "FLB 정보(개)", "서비스 정보(개)", "ADC로그(개)", "설정 이력 정보(개)" };
//		data = new ArrayList<String[]>(logs.size());
		data = new ArrayList<String[]>();

		if (CollectionUtils.isEmpty(logs)) {
			return;
		}

		for (OBDtoMonTotalAdcOne log : logs) {
			String adcStatus = "";
			if (log.getStatus().toString().equals("1")) {
				adcStatus = "정상";
			} else {
				adcStatus = "단절";
			}

			String[] row = {
//				log.getStatus().toString(),
					adcStatus, log.getAdcName(),
					(log.getActiveBackupState() == null) ? "-" : log.getActiveBackupState().toString(), log.getModel(),
					log.getSwVersion(), (log.getThroughput() == null) ? "-" : log.getThroughput().toString(),
					(log.getConcurrentSession() == null) ? "-" : log.getConcurrentSession().toString(),
					log.getUptimeAge(), log.getAdcIp(), sdf.format(log.getConfigTime()),
					(log.getCpu() == null) ? "-" : log.getCpu().toString(),
					(log.getMemory() == null) ? "-" : log.getMemory().toString(),
					(log.getErrorPackets() == null) ? "-" : log.getErrorPackets().toString(),
					(log.getDropPackets() == null) ? "-" : log.getDropPackets().toString(),
					(log.getSslCertValidDays() == null) ? "-" : log.getSslCertValidDays().toString(),
					(log.getInterfaceAvailable() == null) ? "-" : log.getInterfaceAvailable().toString(),
					(log.getFilterUse() == null) ? "-" : log.getFilterUse().toString(),
					(log.getServiceAvailable() == null) ? "-" : log.getServiceAvailable().toString(),
					(log.getAdcLog24Hour() == null) ? "-" : log.getAdcLog24Hour().toString(),
					(log.getSlbConfig24Hour() == null) ? "-" : log.getSlbConfig24Hour().toString() };
			data.add(row);

			logs.add(log);
		}
	}

	public void initWithGrpMonTotalList(List<OBDtoMonTotalGroupOne> logs, OBDtoMonTotalGroupCondition condition) {
		headerMon = new ArrayList<String>();
		String headerText = "";
		dataVal = new ArrayList<Integer>();

		if (condition.getName().isSelect() == true) {
			headerText = "그룹 이름";
			headerMon.add(headerText);
			dataVal.add(0);
		}
		if (condition.getId().isSelect() == true) {
			headerText = "그룹 Index";
			headerMon.add(headerText);
			dataVal.add(1);
		}
		if (condition.getBackup().isSelect() == true) {
			headerText = "Backup";
			headerMon.add(headerText);
			dataVal.add(2);
		}
		if (condition.getMember().isSelect() == true) {
			headerText = "Member";
			headerMon.add(headerText);
			dataVal.add(3);
		}
		if (condition.getFilter().isSelect() == true) {
			headerText = "Filter 정보";
			headerMon.add(headerText);
			dataVal.add(4);
		}
		if (condition.getVsAssigned().isSelect() == true) {
			headerText = "Virtual Server";
			headerMon.add(headerText);
			dataVal.add(5);
		}
		if (condition.getAdcName().isSelect() == true) {
			headerText = "ADC 이름";
			headerMon.add(headerText);
			dataVal.add(6);
		}
		if (condition.getAdcName().isSelect() == true) {
			headerText = "종류";
			headerMon.add(headerText);
			dataVal.add(7);
		}
		if (condition.getAdcIp().isSelect() == true) {
			headerText = "ADC IP";
			headerMon.add(headerText);
			dataVal.add(8);
		}

		header = (String[]) headerMon.toArray(new String[headerMon.size()]);
		data = new ArrayList<String[]>();

		if (CollectionUtils.isEmpty(logs))
			return;

		for (OBDtoMonTotalGroupOne log : logs) {
			String adcType = "";
			if (log.getAdcType() == 1)
				adcType = "F5";
			else if (log.getAdcType() == 2)
				adcType = "Alteon";
			else if (log.getAdcType() == 3)
				adcType = "PAS";
			else if (log.getAdcType() == 4)
				adcType = "PASK";
			else
				adcType = "Unknown";

			dataMon = new ArrayList<String>();
			for (int dataV : dataVal) {
				if (dataV == 0)
					dataMon.add(log.getName());
				else if (dataV == 1)
					dataMon.add(log.getId());
				else if (dataV == 2)
					dataMon.add((log.getBackup() == null) ? "-" : log.getBackup());
				else if (dataV == 3)
					dataMon.add((log.getMember() == -1) ? "-" : log.getMember().toString());
				else if (dataV == 4)
					dataMon.add((log.getFilter() == -1) ? "-" : log.getFilter().toString());
				else if (dataV == 5)
					dataMon.add((log.getVsAssigned() == -1) ? "-" : log.getVsAssigned().toString());
				else if (dataV == 6)
					dataMon.add(log.getAdcName());
				else if (dataV == 7)
					dataMon.add(adcType);
				else if (dataV == 8)
					dataMon.add(log.getAdcIp());
			}

			String[] dataStr = (String[]) dataMon.toArray(new String[dataMon.size()]);
			data.add(dataStr);
		}
	}

	public void initWithRsMonTotalList(List<OBDtoMonTotalRealOne> logs, OBDtoMonTotalRealCondition condition) {
		headerMon = new ArrayList<String>();
		String headerText = "";
		dataVal = new ArrayList<Integer>();

		if (condition.getStatus().isSelect() == true) {
			headerText = "Status";
			headerMon.add(headerText);
			dataVal.add(0);
		}
		if (condition.getState().isSelect() == true) {
			headerText = "State";
			headerMon.add(headerText);
			dataVal.add(1);
		}
		if (condition.getIp().isSelect() == true) {
			headerText = "Real Sever IP";
			headerMon.add(headerText);
			dataVal.add(2);
		}
		if (condition.getName().isSelect() == true) {
			headerText = "Real Server 이름";
			headerMon.add(headerText);
			dataVal.add(3);
		}
		if (condition.getUsed().isSelect() == true) {
			headerText = "사용여부";
			headerMon.add(headerText);
			dataVal.add(4);
		}
		if (condition.getGroup().isSelect() == true) {
			headerText = "그룹개수(개)";
			headerMon.add(headerText);
			dataVal.add(5);
		}
		if (condition.getAdcName().isSelect() == true) {
			headerText = "ADC 이름";
			headerMon.add(headerText);
			dataVal.add(6);
		}

		if (condition.getAdcType().isSelect() == true) {
			headerText = "종류";
			headerMon.add(headerText);
			dataVal.add(7);
		}
		if (condition.getAdcIp().isSelect() == true) {
			headerText = "ADC IP";
			headerMon.add(headerText);
			dataVal.add(8);
		}
		if (condition.getRatio().isSelect() == true) {
			headerText = "Ratio";
			headerMon.add(headerText);
			dataVal.add(9);
		}

		header = (String[]) headerMon.toArray(new String[headerMon.size()]);
		data = new ArrayList<String[]>();

		for (OBDtoMonTotalRealOne log : logs) {
			String rsStaus = "";
			if (log.getStatus() == 1)
				rsStaus = "정상";
			else if (log.getStatus() == 0)
				rsStaus = "단절";
			else
				rsStaus = "꺼짐";

			String rsState = "";
			if (log.getState() == 1)
				rsState = "Enabled";
			else if (log.getState() == 0)
				rsState = "Disabled";
			else
				rsState = "Forced Offline";

			String adcType = "";
			if (log.getAdcType() == 1)
				adcType = "F5";
			else if (log.getAdcType() == 2)
				adcType = "Alteon";
			else if (log.getAdcType() == 3)
				adcType = "PAS";
			else if (log.getAdcType() == 4)
				adcType = "PASK";
			else
				adcType = "Unknown";

			dataMon = new ArrayList<String>();
			for (int dataV : dataVal) {
				if (dataV == 0)
					dataMon.add(rsStaus);
				else if (dataV == 1)
					dataMon.add(rsState);
				else if (dataV == 2)
					dataMon.add(log.getIp());
				else if (dataV == 3)
					dataMon.add((log.getName() == null) ? "-" : log.getName());
				else if (dataV == 4)
					dataMon.add(log.getUsed() == 1 ? "사용" : "사용안함");
				else if (dataV == 5)
					dataMon.add((log.getGroup() == -1) ? "-" : log.getGroup().toString());
				else if (dataV == 6)
					dataMon.add(log.getAdcName());
				else if (dataV == 7)
					dataMon.add(adcType);
				else if (dataV == 8)
					dataMon.add(log.getAdcIp());
				else if (dataV == 9)
					dataMon.add((log.getRatio() == -1) ? "-" : log.getRatio().toString());
			}
			String[] dataStr = (String[]) dataMon.toArray(new String[dataMon.size()]);
			data.add(dataStr);
		}

	}

	public void initWithSvcMonTotalList(List<OBDtoMonTotalServiceOne> logs, OBDtoMonTotalServiceCondition condition) {
		headerMon = new ArrayList<String>();
		String headerText = "";
		dataVal = new ArrayList<Integer>();

		if (condition.getStatus().isSelect() == true) {
			headerText = "Status";
			headerMon.add(headerText);
			dataVal.add(0);
		}
		if (condition.getIp().isSelect() == true) {
			headerText = "VS IP";
			headerMon.add(headerText);
			dataVal.add(1);
		}
		if (condition.getPort().isSelect() == true) {
			headerText = "포트";
			headerMon.add(headerText);
			dataVal.add(2);
		}
		if (condition.getName().isSelect() == true) {
			headerText = "VS 이름";
			headerMon.add(headerText);
			dataVal.add(3);
		}
//		if(condition.getBackup().isSelect() == true)
//		{
//			headerText = "백업여부";
//			headerMon.add(headerText);
//			dataVal.add(4);
//		}
//		if(condition.getThroughput().isSelect() == true)
//		{
//			headerText = "Throughput(bps)";
//			headerMon.add(headerText);
//			dataVal.add(5);
//		}
		if (condition.getBpsIn().isSelect() == true) {
			headerText = "Bps In";
			headerMon.add(headerText);
			dataVal.add(4);
		}
		if (condition.getBpsOut().isSelect() == true) {
			headerText = "Bps Out";
			headerMon.add(headerText);
			dataVal.add(5);
		}
		if (condition.getBpsTotal().isSelect() == true) {
			headerText = "Bps Total";
			headerMon.add(headerText);
			dataVal.add(6);
		}
		if (condition.getConcurrentSession().isSelect() == true) {
			headerText = "Concurrent Seesions(개)";
			headerMon.add(headerText);
			dataVal.add(7);
		}
		if (condition.getAdcName().isSelect() == true) {
			headerText = "ADC 이름";
			headerMon.add(headerText);
			dataVal.add(8);
		}
		if (condition.getAdcName().isSelect() == true) {
			headerText = "종류";
			headerMon.add(headerText);
			dataVal.add(9);
		}
		if (condition.getAdcIp().isSelect() == true) {
			headerText = "ADC IP";
			headerMon.add(headerText);
			dataVal.add(10);
		}
		if (condition.getMember().isSelect() == true) {
			headerText = "Member 정보(개)";
			headerMon.add(headerText);
			dataVal.add(11);
		}
		if (condition.getGroup().isSelect() == true) {
			headerText = "그룹 이름";
			headerMon.add(headerText);
			dataVal.add(12);
		}
		if (condition.getLoadbalancing().isSelect() == true) {
			headerText = "Load Balancing";
			headerMon.add(headerText);
			dataVal.add(13);
		}
		if (condition.getHealthCheck().isSelect() == true) {
			headerText = "Health Check";
			headerMon.add(headerText);
			dataVal.add(14);
		}
		if (condition.getPersistence().isSelect() == true) {
			headerText = "Persistence";
			headerMon.add(headerText);
			dataVal.add(15);
		}
		if (condition.getNoticeGroup().isSelect() == true) {
			headerText = "Notice Group 여부";
			headerMon.add(headerText);
			dataVal.add(16);
		}
		if (condition.getUpdateTime().isSelect() == true) {
			headerText = "최근 업데이트";
			headerMon.add(headerText);
			dataVal.add(17);
		}
		if (condition.getConfigHistory().isSelect() == true) {
			headerText = "설정 이력 정보(개)";
			headerMon.add(headerText);
			dataVal.add(18);
		}

		header = (String[]) headerMon.toArray(new String[headerMon.size()]);
		data = new ArrayList<String[]>();

		if (CollectionUtils.isEmpty(logs)) {
			return;
		}

		for (OBDtoMonTotalServiceOne log : logs) {
			String svcStatus = "";
			if (log.getStatus() == 1)
				svcStatus = "정상";
			else if (log.getStatus() == 0)
				svcStatus = "단절";
			else
				svcStatus = "꺼짐";

//			String backStatus = "";
//			if(log.getBackup() == 1)
//				backStatus = "있음";
//			else
//				backStatus = "없음";		

			String adcType = "";
			if (log.getAdcType() == 1)
				adcType = "F5";
			else if (log.getAdcType() == 2)
				adcType = "Alteon";
			else if (log.getAdcType() == 3)
				adcType = "PAS";
			else if (log.getAdcType() == 4)
				adcType = "PASK";
			else
				adcType = "Unknown";

			dataMon = new ArrayList<String>();
			for (int dataV : dataVal) {
				if (dataV == 0)
					dataMon.add(svcStatus);
				else if (dataV == 1)
					dataMon.add(log.getIp());
				else if (dataV == 2)
					dataMon.add(log.getPort().toString());
				else if (dataV == 3)
					dataMon.add(log.getName());
				else if (dataV == 4)
					dataMon.add((log.getBpsIn() == -1) ? "-" : log.getBpsIn().toString());
				else if (dataV == 5)
					dataMon.add((log.getBpsOut() == -1) ? "-" : log.getBpsOut().toString());
				else if (dataV == 6)
					dataMon.add((log.getBpsTotal() == -1) ? "-" : log.getBpsTotal().toString());
				else if (dataV == 7)
					dataMon.add((log.getConcurrentSession() == -1) ? "-" : log.getConcurrentSession().toString());
				else if (dataV == 8)
					dataMon.add(log.getAdcName());
				else if (dataV == 9)
					dataMon.add(adcType);
				else if (dataV == 10)
					dataMon.add(log.getAdcIp());
				else if (dataV == 11)
					dataMon.add((log.getMember() == -1) ? "-" : log.getMember().toString());
				else if (dataV == 12)
					dataMon.add((log.getGroupName() == null) ? "-" : log.getGroupName());
				else if (dataV == 13)
					dataMon.add(log.getLoadbalancing());
				else if (dataV == 14)
					dataMon.add(log.getHealthCheck());
				else if (dataV == 15)
					dataMon.add((log.getPersistence() == null) ? "-" : log.getPersistence());
				else if (dataV == 16)
					dataMon.add((log.getNoticeGroup() == null) ? "-" : log.getNoticeGroup().toString());
				else if (dataV == 17)
					dataMon.add((log.getUpdateTime() == null) ? "-" : sdf.format(log.getUpdateTime()));
				else if (dataV == 18)
					dataMon.add(log.getSlbConfig24Hour().toString());
//				else if(dataV == 4)
//					dataMon.add(backStatus);
//				else if(dataV == 5)
//					dataMon.add((log.getThroughput() == null) ? "-" : log.getThroughput().toString());
//				else if(dataV == 6)
//					dataMon.add((log.getConcurrentSession() == null) ? "-" : log.getConcurrentSession().toString());
//				else if(dataV == 7)
//					dataMon.add(log.getAdcName());
//				else if(dataV == 8)
//					dataMon.add(log.getAdcIp());
//				else if(dataV == 9)
//					dataMon.add(log.getMember().toString());
//				else if(dataV == 10)
//					dataMon.add((log.getGroupName() == null) ? "-" : log.getGroupName());
//				else if(dataV == 11)
//					dataMon.add(log.getLoadbalancing());
//				else if(dataV == 12)
//					dataMon.add(log.getHealthCheck());
//				else if(dataV == 13)
//					dataMon.add((log.getPersistence() == null) ? "-" : log.getPersistence());
//				else if(dataV == 14)
//					dataMon.add((log.getNoticeGroup() == null) ? "-" : log.getNoticeGroup().toString());
//				else if(dataV == 15)
//					dataMon.add(sdf.format(log.getUpdateTime()));
//				else if(dataV == 16)
//					dataMon.add(log.getSlbConfig24Hour().toString());		

			}
			String[] dataStr = (String[]) dataMon.toArray(new String[dataMon.size()]);
			data.add(dataStr);
		}
	}

	public void initWithAdcLog(List<AdcLogDto> logs) {
		if (CollectionUtils.isEmpty(logs)) {
			return;
		}
		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), CsvMaker.getMessageWeb(MSG_CSV_ADC_NAME),
				CsvMaker.getMessageWeb(MSG_CSV_ADCIP), CsvMaker.getMessageWeb(MSG_CSV_IMP),
				CsvMaker.getMessageWeb(MSG_CSV_DET_CONT) };
		data = new ArrayList<String[]>(logs.size());
		for (AdcLogDto log : logs) {
			String[] row = { sdf.format(log.getOccur_time()), log.getAdc_name(), log.getAdc_ipaddress(),
					log.getLog_level(), log.getContent() };
			data.add(row);
		}
	}

	public void initWithFaultLog(List<AdcSystemLogDto> logs) {
		if (CollectionUtils.isEmpty(logs)) {
			return;
		}
		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), CsvMaker.getMessageWeb(MSG_CSV_ADC_NAME),
				CsvMaker.getMessageWeb(MSG_CSV_CLASS), CsvMaker.getMessageWeb(MSG_CSV_DET_CONT) };
		data = new ArrayList<String[]>(logs.size());
		for (AdcSystemLogDto log : logs) {
			String faultStatus = "";
			if (log.getStatus() == 0) {
				faultStatus = CsvMaker.getMessageWeb(MSG_CSV_FALUT);
			} else if (log.getStatus() == 1) {
				faultStatus = CsvMaker.getMessageWeb(MSG_CSV_FAULT_RESOLUTION);
			} else {
				faultStatus = CsvMaker.getMessageWeb(MSG_CSV_WARNING);
			}
			String[] row = { log.getOccurredTime(), log.getAdcName(), faultStatus, log.getEvent() };
			data.add(row);
		}
	}

	public void initWithAuditLog(List<AuditLogDto> logs) {
		if (CollectionUtils.isEmpty(logs)) {
			return;
		}
		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), CsvMaker.getMessageWeb(MSG_CSV_USER),
				CsvMaker.getMessageWeb(MSG_CSV_ACC_IP), CsvMaker.getMessageWeb(MSG_CSV_LOG_KIND),
				CsvMaker.getMessageWeb(MSG_CSV_IMP), CsvMaker.getMessageWeb(MSG_CSV_DET_CONT) };
		data = new ArrayList<String[]>(logs.size());

		for (AuditLogDto log : logs) {
			String[] row = { sdf.format(log.getOccur_time()), log.getGenerator(), log.getClient_ip(), log.getType(),
					log.getLevel(), log.getContent() };
			data.add(row);
		}
	}

	public void initWithL2Info(List<OBDtoL2SearchInfo> logs) {
		if (CollectionUtils.isEmpty(logs)) {
			return;
		}
		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_IP), CsvMaker.getMessageWeb(MSG_CSV_MAC),
				CsvMaker.getMessageWeb(MSG_CSV_VLAN), CsvMaker.getMessageWeb(MSG_CSV_PORT) };
		data = new ArrayList<String[]>(logs.size());

		for (OBDtoL2SearchInfo log : logs) {
			String[] row = {
					// sdf.format(log.getOccur_time()),
					log.getIpAddress(), log.getMacAddress(), log.getVlanInfo(), log.getInterfaceInfo() };
			data.add(row);
		}
	}

	public void initWithResultContents(OBDtoFaultCheckResult faultCheckResult) {
		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CLASS), CsvMaker.getMessageWeb(MSG_CSV_LIST),
				CsvMaker.getMessageWeb(MSG_CSV_STATUS), CsvMaker.getMessageWeb(MSG_CSV_DET_CONT) };
		data = new ArrayList<String[]>();

		// ArrayList<OBDtoFaultCheckResultElement> faultChkResult = new
		// ArrayList<OBDtoFaultCheckResultElement>();
		// data = new ArrayList<String[]>(faultChkResult.size());
//    	data = new ArrayList<String[]>(faultCheckResult.getHwResults().size());		
//    	data1 = new ArrayList<String[]>(faultCheckResult.getL23Results().size());
//    	data2 = new ArrayList<String[]>(faultCheckResult.getL47Results().size());
//    	data3 = new ArrayList<String[]>(faultCheckResult.getSvcResults().size());

		ArrayList<OBDtoFaultCheckResultElement> topList = faultCheckResult.getHwResults();

		for (OBDtoFaultCheckResultElement result : topList) {
			ArrayList<OBDtoFaultCheckResultContent> list = result.getResultList();

			for (OBDtoFaultCheckResultContent result1 : list) {
				String resultStatus = "";
				if (result.getStatus() == 1) {
					resultStatus = CsvMaker.getMessageWeb(MSG_CSV_ERR);
				} else if (result.getStatus() == 2) {
					resultStatus = CsvMaker.getMessageWeb(MSG_CSV_NORMAL);
				} else if (result.getStatus() == 3) {
					resultStatus = CsvMaker.getMessageWeb(MSG_CSV_INFO);
				}

				if (result1.getDetail() != null) {
					String[] row = { CsvMaker.getMessageWeb(MSG_CSV_HW), result.getObj().getName(), resultStatus + "",
							result1.getSummary() + "\n" + result1.getDetail() };
					data.add(row);
				} else {
					String[] row = { CsvMaker.getMessageWeb(MSG_CSV_HW), result.getObj().getName(), resultStatus + "",
							result1.getSummary() };
					data.add(row);
				}
			}

			if (result.getSolution() != null) {
				if (false == result.getSolution().equals("")) {
					String[] row = { CsvMaker.getMessageWeb(MSG_CSV_HW), result.getObj().getName(), "",
							result.getSolution() };
					data.add(row);
				}
			}
		}

		topList = faultCheckResult.getL23Results();

		for (OBDtoFaultCheckResultElement result : topList) {
			ArrayList<OBDtoFaultCheckResultContent> list = result.getResultList();

			for (OBDtoFaultCheckResultContent result1 : list) {
				String resultStatus = "";
				if (result.getStatus() == 1) {
					resultStatus = CsvMaker.getMessageWeb(MSG_CSV_ERR);
				} else if (result.getStatus() == 2) {
					resultStatus = CsvMaker.getMessageWeb(MSG_CSV_NORMAL);
				} else if (result.getStatus() == 3) {
					resultStatus = CsvMaker.getMessageWeb(MSG_CSV_INFO);
				}

				if (result1.getDetail() != null) {
					String[] row = { CsvMaker.getMessageWeb(MSG_CSV_L23), result.getObj().getName(), resultStatus + "",
							result1.getSummary() + "\n" + result1.getDetail() };
					data.add(row);
				} else {
					String[] row = { CsvMaker.getMessageWeb(MSG_CSV_L23), result.getObj().getName(), resultStatus + "",
							result1.getSummary() };
					data.add(row);
				}
			}

			if (result.getSolution() != null) {
				if (false == result.getSolution().equals("")) {
					String[] row = { CsvMaker.getMessageWeb(MSG_CSV_L23), result.getObj().getName(), "",
							result.getSolution() };
					data.add(row);
				}
			}
		}

		topList = faultCheckResult.getL47Results();

		for (OBDtoFaultCheckResultElement result : topList) {
			ArrayList<OBDtoFaultCheckResultContent> list = result.getResultList();

			for (OBDtoFaultCheckResultContent result1 : list) {
				String resultStatus = "";
				if (result.getStatus() == 1) {
					resultStatus = CsvMaker.getMessageWeb(MSG_CSV_ERR);
				} else if (result.getStatus() == 2) {
					resultStatus = CsvMaker.getMessageWeb(MSG_CSV_NORMAL);
				} else if (result.getStatus() == 3) {
					resultStatus = CsvMaker.getMessageWeb(MSG_CSV_INFO);
				}

				if (result1.getDetail() != null) {
					String[] row = { CsvMaker.getMessageWeb(MSG_CSV_L47), result.getObj().getName(), resultStatus + "",
							result1.getSummary() + "\n" + result1.getDetail() };
					data.add(row);
				} else {
					String[] row = { CsvMaker.getMessageWeb(MSG_CSV_L47), result.getObj().getName(), resultStatus + "",
							result1.getSummary() };
					data.add(row);
				}
			}

			if (result.getSolution() != null) {
				if (false == result.getSolution().equals("")) {
					String[] row = { CsvMaker.getMessageWeb(MSG_CSV_L47), result.getObj().getName(), "",
							result.getSolution() };
					data.add(row);
				}
			}
		}

		topList = faultCheckResult.getSvcResults();

		for (OBDtoFaultCheckResultElement result : topList) {
			ArrayList<OBDtoFaultCheckResultContent> list = result.getResultList();

			for (OBDtoFaultCheckResultContent result1 : list) {
				String resultStatus = "";
				if (result.getStatus() == 1) {
					resultStatus = CsvMaker.getMessageWeb(MSG_CSV_ERR);
				} else if (result.getStatus() == 2) {
					resultStatus = CsvMaker.getMessageWeb(MSG_CSV_NORMAL);
				} else if (result.getStatus() == 3) {
					resultStatus = CsvMaker.getMessageWeb(MSG_CSV_INFO);
				}

				if (result1.getDetail() != null) {
					String[] row = { CsvMaker.getMessageWeb(MSG_CSV_SVC), result.getObj().getName(), resultStatus + "",
							result1.getSummary() + "\n" + result1.getDetail() };
					data.add(row);
				} else {
					String[] row = { CsvMaker.getMessageWeb(MSG_CSV_SVC), result.getObj().getName(), resultStatus + "",
							result1.getSummary() };
					data.add(row);
				}
			}

			if (result.getSolution() != null) {
				if (false == result.getSolution().equals("")) {
					String[] row = { CsvMaker.getMessageWeb(MSG_CSV_SVC), result.getObj().getName(), "",
							result.getSolution() };
					data.add(row);
				}
			}
		}
	}

	public void initWithResultContents(ArrayList<OBDtoFaultCheckResultElement> faultChkResult) {
		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CLASS), CsvMaker.getMessageWeb(MSG_CSV_LIST),
				CsvMaker.getMessageWeb(MSG_CSV_DET_CONT) };
		data = new ArrayList<String[]>();

//    	data = new ArrayList<String[]>(faultCheckResult.getHwResults().size());
		data = new ArrayList<String[]>(faultChkResult.size());
//    	data1 = new ArrayList<String[]>(faultCheckResult.getL23Results().size());
//    	data2 = new ArrayList<String[]>(faultCheckResult.getL47Results().size());
//    	data3 = new ArrayList<String[]>(faultCheckResult.getSvcResults().size());

		for (OBDtoFaultCheckResultElement faultResult : faultChkResult) {
			String[] row = { faultResult.getSolution() };
			data.add(row);
		}
	}

	public void initWithSessionListContents(List<FaultSessionInfoDto> sessionInfo) {
		if (CollectionUtils.isEmpty(sessionInfo)) {
			return;
		}
		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CIP), CsvMaker.getMessageWeb(MSG_CSV_SPORT),
				CsvMaker.getMessageWeb(MSG_CSV_VIP), CsvMaker.getMessageWeb(MSG_CSV_DPORT),
				CsvMaker.getMessageWeb(MSG_CSV_REALIP), CsvMaker.getMessageWeb(MSG_CSV_REALPORT),
				CsvMaker.getMessageWeb(MSG_CSV_PROTOCOL), CsvMaker.getMessageWeb(MSG_CSV_CPU) };
		data = new ArrayList<String[]>(sessionInfo.size());

		for (FaultSessionInfoDto session : sessionInfo) {
			String[] row = { session.getSrcIP(), session.getSrcPort(), session.getDstIP(), session.getDstPort(),
					session.getRealIP(), session.getRealPort(), session.getProtocol(), session.getSpNumber() };
			data.add(row);
		}
	}

	public void initWithAlteonSessionListContents(List<FaultSessionInfoDto> sessionInfo, String selectedOption) {
		if (CollectionUtils.isEmpty(sessionInfo)) {
			return;
		}

//    	String ageHeder;
		String ip;
		String port;
		String realPort;
		if (selectedOption.equals("SLB")) {
			ip = MSG_CSV_VIP;
			port = MSG_CSV_DPORT;
			realPort = MSG_CSV_REALPORT;
//        	ageHeder = MSG_CSV_AGE_S;
		} else {
			ip = MSG_CSV_DESTIP;
			port = MSG_CSV_DESTPORT;
			realPort = MSG_CSV_ACTION;
//        	ageHeder = MSG_CSV_AGE_M;
		}

		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CIP), CsvMaker.getMessageWeb(MSG_CSV_SPORT),
				CsvMaker.getMessageWeb(ip), CsvMaker.getMessageWeb(port),

				// 14.07.14 sw.jung 알테온 세션검색 내보내기 컬럼 이상 수정
				CsvMaker.getMessageWeb(MSG_CSV_REALIP), CsvMaker.getMessageWeb(realPort),
//                CsvMaker.getMessageWeb(MSG_CSV_PROTOCOL),
				CsvMaker.getMessageWeb(MSG_CSV_AGE_M), CsvMaker.getMessageWeb(MSG_CSV_SP) };
		data = new ArrayList<String[]>(sessionInfo.size());

		for (FaultSessionInfoDto session : sessionInfo) {
			String[] row = { session.getSrcIP(), session.getSrcPort(), session.getDstIP(), session.getDstPort(),

					// 14.07.14 sw.jung 알테온 세션검색 내보내기 컬럼 이상 수정
					session.getRealIP(), session.getRealPort(),
//    			session.getProtocol(),

					Integer.toString(session.getAgingTime()), session.getSpNumber() };
			data.add(row);
		}
	}

	// 모니터링 VS 분석에서 내보내기 (PAS-K 같이 최대값이 없는 경우엔 "-" 을 INSERT 한다.)
	public void initWithVsMonitorList(VsConnectionInfoDto vsConnectionInfo) {
		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), CsvMaker.getMessageWeb(MSG_CSV_NOW),
				CsvMaker.getMessageWeb(MSG_CSV_MAX) };
		data = new ArrayList<String[]>();

		for (VsConnectionDataDto vsconndatas : vsConnectionInfo.getVsConnectionDataList()) {
			if (vsconndatas.getMaxConnections() == -1 || vsconndatas.getMaxConnections() == null) {
				String[] row = { sdf.format(vsconndatas.getOccurredTime()),
						String.valueOf(vsconndatas.getActiveConnections()), String.valueOf("-") };
				data.add(row);
			} else {
				String[] row = { sdf.format(vsconndatas.getOccurredTime()),
						String.valueOf(vsconndatas.getActiveConnections()),
						String.valueOf(vsconndatas.getMaxConnections()) };
				data.add(row);
			}
		}
	}

	public void initWithNetworkMap(List<NetworkMapVsDto> logs) {
		if (CollectionUtils.isEmpty(logs)) {
			return;
		}
		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_MODEL), CsvMaker.getMessageWeb(MSG_CSV_HOSTNAME),
				CsvMaker.getMessageWeb(MSG_CSV_VIPNAME), CsvMaker.getMessageWeb(MSG_CSV_VIP),
				CsvMaker.getMessageWeb(MSG_CSV_VPORT), CsvMaker.getMessageWeb(MSG_CSV_RPORT),
				CsvMaker.getMessageWeb(MSG_CSV_GROUP), CsvMaker.getMessageWeb(MSG_CSV_METRIC),
				CsvMaker.getMessageWeb(MSG_CSV_VSTATUS), CsvMaker.getMessageWeb(MSG_CSV_REALIP),
				CsvMaker.getMessageWeb(MSG_CSV_REALPORT), CsvMaker.getMessageWeb(MSG_CSV_REALSTATUS) };
		data = new ArrayList<String[]>(logs.size());
		for (NetworkMapVsDto log : logs) {
			String vsStatus = "";
			if (log.getStatus() == 1)
				vsStatus = "UP";
			else if (log.getStatus() == 2)
				vsStatus = "DOWN";
			else
				vsStatus = "DISABLE";

			if (log.getNetworkMapVsMemberList() == null || log.getNetworkMapVsMemberList().isEmpty()) {
				ArrayList<String> row = new ArrayList<String>();
				row.add(log.getModel());
				row.add(log.getHostName());
				row.add(log.getName());
				row.add(log.getIpAddress());
				row.add(log.getPort().toString());
				row.add(log.getRport());
				row.add(log.getGroupId());
				row.add(log.getLoadBalancingType());
				row.add(vsStatus);
				row.add("");
				row.add("");
				row.add("");
				String[] sArrays = row.toArray(new String[row.size()]);
				data.add(sArrays);
			} else {
				for (NetworkMapVsMemberDto memberList : log.getNetworkMapVsMemberList()) {
					String memberStatus = "";
					if (memberList.getStatus() == 1)
						memberStatus = "up";
					else if (memberList.getStatus() == 2)
						memberStatus = "down";
					else
						memberStatus = "disable";

					ArrayList<String> row = new ArrayList<String>();
					row.add(log.getModel());
					row.add(log.getHostName());
					row.add(log.getName());
					row.add(log.getIpAddress());
					row.add(log.getPort().toString());
					row.add(log.getRport());
					row.add(log.getGroupId());
					row.add(log.getLoadBalancingType());
					row.add(vsStatus);
					row.add(memberList.getIpAddress());

					row.add(memberList.getPort().toString());
					row.add(memberStatus);
					String[] sArrays = row.toArray(new String[row.size()]);
					data.add(sArrays);
				}
			}

		}
	}
	/*
	 * VsConnectionInfoDto private Date maxDate; private Date minDate; private
	 * String maxConnections; private String minConnections; private String
	 * avgConnections; private List<VsConnectionDataDto> vsConnectionDataList;
	 * private Date occurredTime; private Long maxConnections; private Long
	 * activeConnections; private Long totalConnections; private
	 * List<VsConfigEventDto> vsConfigEventList;
	 */

	// 모니터링 System Status 에서 내보내기 (생성시간 / CPU사용량 / 메모리사용량)
	public void initWithSystemStatusList(List<SystemStatusDto> sstatus) {
		if (CollectionUtils.isEmpty(sstatus)) {
			return;
		}

		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), CsvMaker.getMessageWeb(MSG_CSV_CPUUSAGE),
				CsvMaker.getMessageWeb(MSG_CSV_MEMUSAGE) };
		data = new ArrayList<String[]>(sstatus.size());
		for (SystemStatusDto status : sstatus) {
			String[] row = { sdf.format(status.getOccurredTime()), Integer.toString(status.getCpuUsage()),
					Integer.toString(status.getMemoryUsage()) };
			data.add(row);
		}
	}

	public void initWithExportTo(List<SystemStatusDto> sstatus, PerformanceDto performance) {
		PerfActiveConnectionInfoDto perfActiveConnectionInfo = performance.getPerfActiveConnectionInfo();
		PerfThroughputInfoDto perfThroughputInfo = performance.getPerfThroughputInfo();

		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), CsvMaker.getMessageWeb(MSG_CSV_CONN),
				CsvMaker.getMessageWeb(MSG_CSV_THRU), CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME),
				CsvMaker.getMessageWeb(MSG_CSV_CPUUSAGE), CsvMaker.getMessageWeb(MSG_CSV_MEMUSAGE) };
		data = new ArrayList<String[]>();

		int nCnt = 0;

		for (PerfActiveConnectionDataDto perfactive : perfActiveConnectionInfo.getPerfActiveConnectionDataList()) {
			String[] row = new String[6];
//			row[0] = String.valueOf(perfssl.getOccurredTime());
			row[0] = sdf.format(perfactive.getOccurredTime());
			row[1] = String.valueOf(perfactive.getConnectionPerSec());
			data.add(row);
		}

		if (CollectionUtils.isEmpty(sstatus)) {
			return;
		}

		for (SystemStatusDto status : sstatus) {
			String[] row = data.get(nCnt);
			row[3] = sdf.format(status.getOccurredTime());
			row[4] = Integer.toString(status.getCpuUsage());
			row[5] = Integer.toString(status.getMemoryUsage());
			nCnt++;
		}
		nCnt = 0;

		for (PerfThroughputDataDto perfthrough : perfThroughputInfo.getPerfThroughputDataList()) {
			String[] row = data.get(nCnt);
			row[2] = String.valueOf(perfthrough.getBps());

			nCnt++;
		}
		nCnt = 0;

	}

	// 도구 및 진단 Session Table 에서 내보내기(session 정보)
	public void initWithSessionList(String content) {
		if (content == null) {
			return;
		}
		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_SESSION) };
		data = new ArrayList<String[]>();
		String[] row = new String[2];
		{
			row[0] = content;
		}
		;
		data.add(row);
	}

	public void initWithSystemPerformance(List<SystemStatusDto> sstatus, PerformanceDto performance) {
		PerfSslTransactionInfoDto perfSslTransactionInfo = performance.getPerfSslTransactionInfo();
		PerfHttpRequestInfoDto perfHttpRequestInfo = performance.getPerfHttpRequestInfo();
		PerfActiveConnectionInfoDto perfActiveConnectionInfo = performance.getPerfActiveConnectionInfo();
		PerfThroughputInfoDto perfThroughputInfo = performance.getPerfThroughputInfo();

		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), CsvMaker.getMessageWeb(MSG_CSV_SSL),
				CsvMaker.getMessageWeb(MSG_CSV_HTTP), CsvMaker.getMessageWeb(MSG_CSV_CONN),
				CsvMaker.getMessageWeb(MSG_CSV_THRU), CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME),
				CsvMaker.getMessageWeb(MSG_CSV_CPUUSAGE), CsvMaker.getMessageWeb(MSG_CSV_MEMUSAGE) };
		data = new ArrayList<String[]>();

		int nCnt = 0;

		for (PerfSslTransactionDataDto perfssl : perfSslTransactionInfo.getPerfSslTransactionDataList()) {
			String[] row = new String[8];
//			row[0] = String.valueOf(perfssl.getOccurredTime());
			row[0] = sdf.format(perfssl.getOccurredTime());
			row[1] = String.valueOf(perfssl.getSslTransactionPerSec());
			data.add(row);
		}

		if (CollectionUtils.isEmpty(sstatus)) {
			return;
		}

		for (SystemStatusDto status : sstatus) {
			String[] row = data.get(nCnt);
			row[5] = sdf.format(status.getOccurredTime());
			row[6] = Integer.toString(status.getCpuUsage());
			row[7] = Integer.toString(status.getMemoryUsage());
			nCnt++;
		}

		nCnt = 0;

		for (PerfHttpRequestDataDto perfhttp : perfHttpRequestInfo.getPerfHttpRequestDataList()) {
			String[] row = data.get(nCnt);
			row[2] = String.valueOf(perfhttp.getHttpRequestPerSec());

			nCnt++;
		}

		nCnt = 0;

		for (PerfActiveConnectionDataDto perfactive : perfActiveConnectionInfo.getPerfActiveConnectionDataList()) {
			String[] row = data.get(nCnt);
			row[3] = String.valueOf(perfactive.getConnectionPerSec());

			nCnt++;
		}

		nCnt = 0;

		for (PerfThroughputDataDto perfthrough : perfThroughputInfo.getPerfThroughputDataList()) {
			String[] row = data.get(nCnt);
			row[4] = String.valueOf(perfthrough.getBps());

			nCnt++;
		}

		nCnt = 0;

	}

	// 모티터링 Performance 에서 내보내기 (SSL Transaction / HTTP Request / Connection /
	// Throughput)
//	public void initWithPerformanceList(List<PerfSslTransactionInfoDto> perfsslt) {
	public void initWithPerformanceList(PerformanceDto performance) {

		// 공통으로 occurredTime 을 조회조건으로 사용
		// SSL Transaction에서는 sslTransactionPerSec 사용
		// HTTP Request에서는 httpRequestPerSec 사용
		// Connection에서는 connectionPerSec 사용
		// Throughput에서는 bps 사용

		/*
		 * PerfSslTransactionInfoDto private Long maxTransaction; private Long
		 * minTransaction; private Long avgTransaction; private Long curTransaction;
		 * private List<PerfSslTransactionDataDto> perfSslTransactionDataList; private
		 * Date occurredTime; private Long sslTransactionPerSec;
		 * 
		 * PerfHttpRequestInfoDto private Long maxHttpRequest; private Long
		 * minHttpRequest; private Long avgHttpRequest; private Long curHttpRequest;
		 * private List<PerfHttpRequestDataDto> perfHttpRequestDataList; private Date
		 * occurredTime; private Long httpRequestPerSec;
		 * 
		 * PerfActiveConnectionInfoDto private Long maxActiveConnection; private Long
		 * minActiveConnection; private Long avgActiveConnection; private Long
		 * curActiveConnection; private List<PerfActiveConnectionDataDto>
		 * perfActiveConnectionDataList; private Date occurredTime; private Long
		 * connectionPerSec;
		 * 
		 * PerfThroughputInfoDto private Long maxBps; private Long minBps; private Long
		 * avgBps; private Long curBps;
		 * 
		 * private List<PerfThroughputDataDto> perfThroughputDataList; private Date
		 * occurredTime; private Long pps; private Long bps;
		 */

//		if (CollectionUtils.isEmpty(perfsslt))
//			return;
//		if (CollectionUtils.isEmpty(perf))
//			return;

		// 하나의 arraylist에 4개를 붙임. 또는 4개의 arraylist를 하나로 묶어서
		PerfSslTransactionInfoDto perfSslTransactionInfo = performance.getPerfSslTransactionInfo();
		PerfHttpRequestInfoDto perfHttpRequestInfo = performance.getPerfHttpRequestInfo();
		PerfActiveConnectionInfoDto perfActiveConnectionInfo = performance.getPerfActiveConnectionInfo();
		PerfThroughputInfoDto perfThroughputInfo = performance.getPerfThroughputInfo();

//		header = new String[]{"SSL Transaction", CsvMaker.getMessageWeb(CsvMaker.MSG_CSV_CREATE_TIME), "최대", "평균", "현재", " ", "HTTP Request", CsvMaker.getMessageWeb(CsvMaker.MSG_CSV_CREATE_TIME), "최대", "평균", "현재", " ", 
//						"Connection", CsvMaker.getMessageWeb(CsvMaker.MSG_CSV_CREATE_TIME), "최대", "평균", "현재", " ", "Throughput", CsvMaker.getMessageWeb(CsvMaker.MSG_CSV_CREATE_TIME), "최대", "평균", "현재"};

		header = new String[] { CsvMaker.getMessageWeb(MSG_CSV_CREATE_TIME), CsvMaker.getMessageWeb(MSG_CSV_SSL),
				CsvMaker.getMessageWeb(MSG_CSV_HTTP), CsvMaker.getMessageWeb(MSG_CSV_CONN),
				CsvMaker.getMessageWeb(MSG_CSV_THRU) };
		data = new ArrayList<String[]>();

		int nCnt = 0;

		for (PerfSslTransactionDataDto perfssl : perfSslTransactionInfo.getPerfSslTransactionDataList()) {
			String[] row = new String[5];
//			row[0] = String.valueOf(perfssl.getOccurredTime());
			row[0] = sdf.format(perfssl.getOccurredTime());
			row[1] = String.valueOf(perfssl.getSslTransactionPerSec());
			data.add(row);
		}

		nCnt = 0;

		for (PerfHttpRequestDataDto perfhttp : perfHttpRequestInfo.getPerfHttpRequestDataList()) {
			String[] row = data.get(nCnt);
			row[2] = String.valueOf(perfhttp.getHttpRequestPerSec());

			nCnt++;
		}

		nCnt = 0;

		for (PerfActiveConnectionDataDto perfactive : perfActiveConnectionInfo.getPerfActiveConnectionDataList()) {
			String[] row = data.get(nCnt);
			row[3] = String.valueOf(perfactive.getConnectionPerSec());

			nCnt++;
		}

		nCnt = 0;

		for (PerfThroughputDataDto perfthrough : perfThroughputInfo.getPerfThroughputDataList()) {
			String[] row = data.get(nCnt);
			row[4] = String.valueOf(perfthrough.getBps());

			nCnt++;
		}
		/*
		 * header = new String[]{"SSL Transaction",
		 * CsvMaker.getMessageWeb(CsvMaker.MSG_CSV_CREATE_TIME), "최대값", "평균값", "현재값"};
		 * header = new String[]{"SSL Transaction",
		 * CsvMaker.getMessageWeb(CsvMaker.MSG_CSV_CREATE_TIME),
		 * "sslTransactionPerSec"};
		 * 
		 * header = new String[]{"HTTP Request",
		 * CsvMaker.getMessageWeb(CsvMaker.MSG_CSV_CREATE_TIME), "최대값", "평균값", "현재값"};
		 * header = new String[]{"HTTP Request",
		 * CsvMaker.getMessageWeb(CsvMaker.MSG_CSV_CREATE_TIME), "httpRequestPerSec"};
		 * 
		 * header = new String[]{"Connection",
		 * CsvMaker.getMessageWeb(CsvMaker.MSG_CSV_CREATE_TIME), "최대값", "평균값", "현재값"};
		 * header = new String[]{"Connection",
		 * CsvMaker.getMessageWeb(CsvMaker.MSG_CSV_CREATE_TIME), "connectionPerSec"};
		 * 
		 * header = new String[]{"Throughput",
		 * CsvMaker.getMessageWeb(CsvMaker.MSG_CSV_CREATE_TIME), "최대값", "평균값", "현재값"};
		 * header = new String[]{"Throughput",
		 * CsvMaker.getMessageWeb(CsvMaker.MSG_CSV_CREATE_TIME), "bps"};
		 */

	}

	public File write(String fileName) throws Exception {
		if (ArrayUtils.isEmpty(header) || CollectionUtils.isEmpty(data)) {
			return null;
		}

		log.info("{}" + CsvMaker.getMessageWeb(MSG_CSV_FILE_WRI_START));
		File csv = new File(decideCsvPathAndMakeFolder(fileName));
		CSVWriter writer = null;

		try {
			writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(csv), "EUC-KR"));

			writeHeader(writer);
			writeContent(writer);
			log.info("{}" + CsvMaker.getMessageWeb(MSG_CSV_FILE_WRI_END));
		} catch (IOException e) {
			log.error(CsvMaker.getMessageWeb(MSG_CSV_CSV_FILE_CRE_ERR));
			throw e;
		} finally {
			if (writer != null) {
				writer.close();
			}

			header = null;
			data = null;
		}

		return csv;
	}

	public File write() throws Exception {
		if (ArrayUtils.isEmpty(header) || CollectionUtils.isEmpty(data)) {
			return null;
		}

		log.info("{}" + CsvMaker.getMessageWeb(MSG_CSV_FILE_WRI_START));
		File csv = new File(decideCsvPathAndMakeFolder());
		CSVWriter writer = null;

		try {
			writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(csv), "EUC-KR"));

			writeHeader(writer);
			writeContent(writer);
			log.info("{}" + CsvMaker.getMessageWeb(MSG_CSV_FILE_WRI_END));
		} catch (IOException e) {
			log.error(CsvMaker.getMessageWeb(MSG_CSV_CSV_FILE_CRE_ERR));
			throw e;
		} finally {
			if (writer != null) {
				writer.close();
			}

			header = null;
			data = null;
		}

		return csv;
	}

	public File writeMon() throws Exception {
		if (headerMon.isEmpty() || CollectionUtils.isEmpty(data)) {
			return null;
		}

		log.info("{}" + CsvMaker.getMessageWeb(MSG_CSV_FILE_WRI_START));
		File csv = new File(decideCsvPathAndMakeFolder());
		CSVWriter writer = null;

		try {
			writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(csv), "EUC-KR"));

//			writeHeaderMon(writer);
			writeContent(writer);
			log.info("{}" + CsvMaker.getMessageWeb(MSG_CSV_FILE_WRI_END));
		} catch (IOException e) {
			log.error(CsvMaker.getMessageWeb(MSG_CSV_CSV_FILE_CRE_ERR));
			throw e;
		} finally {
			if (writer != null) {
				writer.close();
			}

			headerMon = null;
			data = null;
		}

		return csv;
	}

	private String decideCsvPathAndMakeFolder() throws IOException {
		String folderName = FilenameUtils.getFullPath(CSV_SAVE_FOLDER);

		if (!new File(folderName).exists()) {
			FileUtils.forceMkdir(new File(folderName));
		}

		return makeDatedPath(folderName);
	}

	private String decideCsvPathAndMakeFolder(String fileName) throws IOException {
		String folderName = FilenameUtils.getFullPath(CSV_SAVE_FOLDER);

		if (!new File(folderName).exists()) {
			FileUtils.forceMkdir(new File(folderName));
		}

		return makeDatedPath(folderName, fileName);
	}

	private String makeDatedPath(String folderName) {
		String baseName = "log";
		String postfix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String extension = "csv";

		return folderName + baseName + "_" + postfix + "." + extension;
	}

	private String makeDatedPath(String folderName, String fileName) {
//		String baseName = "log";
//		String postfix = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String extension = "csv";

		return folderName + fileName + "." + extension;
	}

	private void writeHeader(CSVWriter writer) {
		writer.writeNext(header);

	}

//	private void writeHeaderMon(CSVWriter writer) 
//	{
//		writer.writeAll(headerMon);		
//	}

	private void writeContent(CSVWriter writer) {
		for (String[] row : data) {
			writer.writeNext(row);
		}
	}

	private String getLanguageCode(String langCode) {
		String elements[] = langCode.split("_");
		if (elements.length != 2)
			return "ko";// default language is korean
		return elements[0];
	}

	private String getCountryCode(String langCode) {
		String elements[] = langCode.split("_");
		if (elements.length != 2)
			return "KR";// default language is korean
		return elements[1];
	}

	public synchronized static String getMessageWeb(String code) {
		try {
			if (msgProps == null) {
				String langCode = OBCommon.getLocalInfo();
				String language = new CsvMaker().getLanguageCode(langCode);
				String country = new CsvMaker().getCountryCode(langCode);
				String fileName = String.format(OBDefine.PROPERTIES_MESSAGES, language, country);
				BufferedReader in = new BufferedReader(
						new InputStreamReader(new FileInputStream(OBUtility.getPropOsPath() + fileName), "UTF8"));
				msgProps = new Properties();
				log.debug("{}", msgProps);
				msgProps.load(in);
				log.debug("{}", msgProps.size());
			}
			String retVal = msgProps.getProperty(code);
			if (retVal == null) {
				log.debug("{}", code);
				return "not defined";
			}
			return retVal;
		} catch (Exception e) {
			log.debug("{}", code);
			return "not defined";
		}
	}

	public CsvMaker() {
	}

}
