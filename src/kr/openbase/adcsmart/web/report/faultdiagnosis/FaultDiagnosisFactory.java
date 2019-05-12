package kr.openbase.adcsmart.web.report.faultdiagnosis;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.OBReportOperation;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResult;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultContent;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultElement;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptTitle;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.dto.OBDtoCpuMemStatus;
import kr.openbase.adcsmart.service.impl.fault.OBFaultMngImpl;
import kr.openbase.adcsmart.service.impl.report.OBReportOperationImpl;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.impl.OBDtoReportTextHdr;

public class FaultDiagnosisFactory {
//	public static void main(String args[]) throws Exception
//	{
//		ArrayList<OBDtoReportDiagnosisHW> list = getDiagnosisHW("1393466490672");
//		System.out.println(list);
////		List<L4SlbConfigChangeSummaryDto> slt = getL4SlbConfChange("1363684025010" , 1);
////		System.out.println("##########################TESTESTSETSETSETSTSET"+slt);
//	}

	public static List<OBDtoReportDiagnosisHeaderInfo> getSystemReport(String index) throws Exception {
		Class.forName("org.postgresql.Driver");

		OBReportOperation reportSvc = new OBReportOperationImpl();
		OBDtoRptTitle rptTitle = reportSvc.getTitle(index);
		OBDtoReportDiagnosisHeaderInfo report = new OBDtoReportDiagnosisHeaderInfo();// SystemReportDto.toSystemReportDto(rptTitle);

		Long checkKey = Long.parseLong(rptTitle.getExtraInfo());// Long checkKey = 3592403631291L;
		OBDtoFaultCheckResult diagResult = new OBFaultMngImpl().getFaultCheckLogDetail(checkKey);

		report.setAccountId(rptTitle.getUserID());
		report.setAdcs(rptTitle.getAdcList());

		report.setFromPeriod(diagResult.getStartTime());
		report.setToPeriod(diagResult.getEndTime());
		report.setCreationTime(diagResult.getStartTime());

		Integer itemCount = diagResult.getHwResults().size() + diagResult.getL23Results().size()
				+ diagResult.getL47Results().size();
		report.setExistADC(1);
		if (itemCount.intValue() == 0)
			report.setExistADC(0);

//		report.setExistRespTime(1);

		itemCount = diagResult.getSvcResults().size();
		report.setExistSrv(1);
		if (itemCount.intValue() == 0)
			report.setExistSrv(0);

		report.setTextHdr(OBDtoReportTextHdr.toTextHdr(
				OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_HDR_OCCUREDTIME)/* "진단 시간" */,
				OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_HDR_ELAPSEDTIME)/* "소요 시간" */,
				OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_HDR_TARGETADC)/* "대상 ADC" */));

		List<OBDtoReportDiagnosisHeaderInfo> reports = new ArrayList<OBDtoReportDiagnosisHeaderInfo>();
		reports.add(report);
		return reports;
	}

	private static String convertDiagnosisStatusToString(Integer status) {
		switch (status) {
		case OBDtoFaultCheckResultElement.STATUS_FAIL:
			return "ERROR";
		case OBDtoFaultCheckResultElement.STATUS_INFO:
			return "INFO";
		case OBDtoFaultCheckResultElement.STATUS_NA:
			return "N/A";
		case OBDtoFaultCheckResultElement.STATUS_SUCC:
			return "OK";
		default:
			return "N/A";
		}
	}

	public static ArrayList<OBDtoReportDiagnosisHW> getDiagnosisHW(String rptIndex) throws Exception {
		OBReportOperation reportSvc = new OBReportOperationImpl();
		OBDtoRptTitle rptTitle = reportSvc.getTitle(rptIndex);

		ArrayList<OBDtoReportDiagnosisHW> retVal = new ArrayList<OBDtoReportDiagnosisHW>();

		OBDtoReportDiagnosisHW item = new OBDtoReportDiagnosisHW();
		item.setTextHdr(OBDtoReportTextHdr.toTextHdr(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_ADC_DIAGNOSIS)));// "ADC
																														// 진단"));

		Long checkKey = Long.parseLong(rptTitle.getExtraInfo());// Long checkKey = 3592403631291L;
		OBDtoFaultCheckResult diagResult = new OBFaultMngImpl().getFaultCheckLogDetail(checkKey);

		ArrayList<OBDtoReportDiagnosisElement> hwList = new ArrayList<OBDtoReportDiagnosisElement>();

		ArrayList<OBDtoFaultCheckResultElement> resultList;

		OBDtoReportTextHdr textHdr = OBDtoReportTextHdr.toTextHdr(
				OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_TBL_CATEGORY)/* "구분" */,
				OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_TBL_TYPE)/* "종류" */,
				OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_TBL_STATUS)/* "상태" */,
				OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_TBL_SUMMARY)/* "요약" */,
				OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_TBL_DETAIL)/* "상세내용" */);

		// H/W 항목 적용.
		resultList = diagResult.getHwResults();
		for (int i = 0; i < resultList.size(); i++)// OBDtoFaultCheckResultElement obj:resultList)
		{
			OBDtoFaultCheckResultElement obj = resultList.get(i);
			ArrayList<OBDtoFaultCheckResultContent> subList = obj.getResultList();

			for (int ii = 0; ii < subList.size(); ii++) {
				OBDtoFaultCheckResultContent subObj = subList.get(ii);
				OBDtoReportDiagnosisElement info = new OBDtoReportDiagnosisElement();

				info.setTextHdr(textHdr);

				if (i == 0 && ii == 0)
					info.setCategory(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_CATEGORY_HW));// "H/W");
//				if(ii==0)
				info.setTitle(obj.getObj().getName());

				info.setStatus(convertDiagnosisStatusToString(obj.getStatus()));
				info.setStatusCode(obj.getStatus());
				info.setSummary(subObj.getSummary());
				info.setDetails(subObj.getDetail());
				hwList.add(info);
			}
		}

		// L2-3 항목 적용.
		resultList = diagResult.getL23Results();
		for (int i = 0; i < resultList.size(); i++)// OBDtoFaultCheckResultElement obj:resultList)
		{
			OBDtoFaultCheckResultElement obj = resultList.get(i);
			ArrayList<OBDtoFaultCheckResultContent> subList = obj.getResultList();

			for (int ii = 0; ii < subList.size(); ii++) {
				OBDtoFaultCheckResultContent subObj = subList.get(ii);
				OBDtoReportDiagnosisElement info = new OBDtoReportDiagnosisElement();

				info.setTextHdr(textHdr);

				if (i == 0 && ii == 0)
					info.setCategory(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_CATEGORY_L23));// "L2-3");
//				if(ii==0)
				info.setTitle(obj.getObj().getName());

				info.setStatus(convertDiagnosisStatusToString(obj.getStatus()));
				info.setStatusCode(obj.getStatus());

				info.setSummary(subObj.getSummary());
				info.setDetails(subObj.getDetail());
				hwList.add(info);
			}
		}

		// L4-7 항목 적용.
		resultList = diagResult.getL47Results();
		for (int i = 0; i < resultList.size(); i++)// OBDtoFaultCheckResultElement obj:resultList)
		{
			OBDtoFaultCheckResultElement obj = resultList.get(i);
			ArrayList<OBDtoFaultCheckResultContent> subList = obj.getResultList();

			for (int ii = 0; ii < subList.size(); ii++) {
				OBDtoFaultCheckResultContent subObj = subList.get(ii);
				OBDtoReportDiagnosisElement info = new OBDtoReportDiagnosisElement();

				info.setTextHdr(textHdr);

				if (i == 0 && ii == 0)
					info.setCategory(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_CATEGORY_L47));// "L4-7");
//				if(ii==0)
				info.setTitle(obj.getObj().getName());

				info.setStatus(convertDiagnosisStatusToString(obj.getStatus()));
				info.setStatusCode(obj.getStatus());

				info.setSummary(subObj.getSummary());
				info.setDetails(subObj.getDetail());
				hwList.add(info);
			}
		}

		item.setHwList(hwList);

		retVal.add(item);
		return retVal;
	}

	public static ArrayList<OBDtoReportDiagnosisHW> getDiagnosisService(String rptIndex) throws Exception {
		ArrayList<OBDtoReportDiagnosisHW> retVal = new ArrayList<OBDtoReportDiagnosisHW>();

		OBDtoReportDiagnosisHW item = new OBDtoReportDiagnosisHW();
		item.setTextHdr(OBDtoReportTextHdr.toTextHdr(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_SRV_DIAGNOSIS)));// "서비스
																														// 진단"));

		OBReportOperation reportSvc = new OBReportOperationImpl();
		OBDtoRptTitle rptTitle = reportSvc.getTitle(rptIndex);

		Long checkKey = Long.parseLong(rptTitle.getExtraInfo());// Long checkKey = 3592403631291L;
		OBDtoFaultCheckResult diagResult = new OBFaultMngImpl().getFaultCheckLogDetail(checkKey);

		ArrayList<OBDtoReportDiagnosisElement> hwList = new ArrayList<OBDtoReportDiagnosisElement>();

		ArrayList<OBDtoFaultCheckResultElement> resultList;

		OBDtoReportTextHdr textHdr = OBDtoReportTextHdr.toTextHdr(
				OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_TBL_CATEGORY)/* "구분" */,
				OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_TBL_TYPE)/* "종류" */,
				OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_TBL_STATUS)/* "상태" */,
				OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_TBL_SUMMARY)/* "요약" */,
				OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_TBL_DETAIL)/* "상세내용" */);

		// service 항목.
		resultList = diagResult.getSvcResults();
		for (int i = 0; i < resultList.size(); i++)// OBDtoFaultCheckResultElement obj:resultList)
		{
			OBDtoFaultCheckResultElement obj = resultList.get(i);
			ArrayList<OBDtoFaultCheckResultContent> subList = obj.getResultList();

			for (int ii = 0; ii < subList.size(); ii++) {
				OBDtoFaultCheckResultContent subObj = subList.get(ii);
				OBDtoReportDiagnosisElement info = new OBDtoReportDiagnosisElement();

				info.setTextHdr(textHdr);

				if (i == 0 && ii == 0)
					info.setCategory(OBMessages.getMessage(OBMessages.MSG_RPT_ADC_DIAG_CATEGORY_SERVICE));// "Service");
//				if(ii==0)
				info.setTitle(obj.getObj().getName());

				info.setStatus(convertDiagnosisStatusToString(obj.getStatus()));
				info.setStatusCode(obj.getStatus());

				info.setSummary(subObj.getSummary());
				info.setDetails(subObj.getDetail());
				hwList.add(info);
			}
		}

		item.setHwList(hwList);

		retVal.add(item);
		return retVal;
	}

	public static ArrayList<OBDtoRptDiagnosisCpuMemory> getDiagnosisCpuMemoryTrend(String rptIndex) throws Exception {
		ArrayList<OBDtoRptDiagnosisCpuMemory> retVal = new ArrayList<OBDtoRptDiagnosisCpuMemory>();

		OBReportOperation reportSvc = new OBReportOperationImpl();
		OBDtoRptTitle rptTitle = reportSvc.getTitle(rptIndex);
		if (rptTitle.getAdcList().isEmpty())
			return retVal;
		OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(rptTitle.getAdcList().get(0).getIndex());// adc 목록은
																												// 하나만
																												// 저장된다.
																												// 리스트의
																												// 첫번째
																												// 데이터만
																												// 유효함.

		Long checkKey = Long.parseLong(rptTitle.getExtraInfo());// Long checkKey = 3592403631291L;
		ArrayList<OBDtoCpuMemStatus> history = new OBFaultMngImpl().getFaultAdcCpuMemoryHistory(checkKey);
		retVal.add(OBDtoRptDiagnosisCpuMemory.toArrayList(history, adcInfo.getAdcType()));
		return retVal;
	}

//	public static ArrayList<OBDtoReportDiagnosisHW> getDiagnosisSrvResponseTime(String rptIndex) throws Exception 
//	{
//		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("aaaaaaaaaaaaaaaaaaaaaaa. %s", rptIndex));
//		ArrayList<OBDtoReportDiagnosisHW> retVal = new ArrayList<OBDtoReportDiagnosisHW>();
//		
//		OBDtoReportDiagnosisHW item = new OBDtoReportDiagnosisHW();
//		
//		ArrayList<OBDtoReportDiagnosisElement> hwList = new ArrayList<OBDtoReportDiagnosisElement>();
//		
//		OBDtoReportDiagnosisElement powerSupply = new OBDtoReportDiagnosisElement();
//		powerSupply.setCategory("Packet capture");
//		powerSupply.setTitle("UnusedSLB");
//		powerSupply.setStatus("OK");
//		powerSupply.setSummary("VLAN information");
//		powerSupply.setDetails("");
//		hwList.add(powerSupply);
//		
//		OBDtoReportDiagnosisElement osVersion = new OBDtoReportDiagnosisElement();
//		osVersion.setCategory("L4-7");
//		osVersion.setTitle("Offlined SLB");
//		osVersion.setStatus("info");
//		osVersion.setSummary("STP information");
//		osVersion.setDetails("MODEL : Alteon Application Switch 5412, OS : 28.1.11.0MODEL : Alteon Application Switch 5412, OS : 28.1.11.0MODEL : Alteon Application Switch 5412, OS : 28.1.11.0\nMODEL : Alteon Application Switch 5412, OS : 28.1.11.0MODEL : Alteon Application Switch 5412, OS : 28.1.11.0\n1111111111111111111111111111111111111111111111122222222222222233333333333333333333333박병욱.....");
//		hwList.add(osVersion);
//		
//		item.setHwList(hwList);
//		
//		retVal.add(item);
//		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("bbbbbbbbbbbbbbbbbbbbbbb. %s. ret:%s", rptIndex, retVal));
//		return retVal;
//	}

	public static ArrayList<OBDtoReportDiagnosisPktLoss> getDiagnosisSrvPktLoss(String rptIndex) throws Exception {
		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("aaaaaaaaaaaaaaaaaaaaaaa. %s", rptIndex));
		ArrayList<OBDtoReportDiagnosisPktLoss> retVal = new ArrayList<OBDtoReportDiagnosisPktLoss>();

		OBSystemLog.info(OBDefine.LOGFILE_SYSTEM,
				String.format("bbbbbbbbbbbbbbbbbbbbbbb. %s. ret:%s", rptIndex, retVal));
		return retVal;
	}
}
