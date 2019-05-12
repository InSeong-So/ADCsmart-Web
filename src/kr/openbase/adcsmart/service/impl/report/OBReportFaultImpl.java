package kr.openbase.adcsmart.service.impl.report;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.OBReportFault;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcName;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemLog;
import kr.openbase.adcsmart.service.dto.OBDtoAuditLogAdcSystem;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptAdcFaultInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptAdcLogInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptTitle;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBMonitoringImpl;
import kr.openbase.adcsmart.service.utility.OBException;

public class OBReportFaultImpl implements OBReportFault {
//	public static void main(String[] args)
//	{
//		try
//		{
//			System.out.println(new OBReportFaultImpl().getTitle("12342567"));
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public OBDtoRptTitle getTitle(String rptIndex) throws OBException {
		try {
			OBReportInfo rptInfo = new OBReportImpl().getReportInfo(rptIndex);
			OBDtoRptTitle result = new OBDtoRptTitle();
			result.setIndex(rptInfo.getIndex());
			result.setAdcList(rptInfo.getAdcList());
			result.setBeginTime(rptInfo.getBeginTime());
			result.setEndTime(rptInfo.getEndTime());
			result.setOccurTime(rptInfo.getOccurTime());
			result.setUserIndex(rptInfo.getAccountIndex());
			result.setUserID(rptInfo.getAccountID());
			return result;
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
		}
	}

//	public static void main(String[] args)
//	{
//		try
//		{
//			System.out.println(new OBReportFaultImpl().getAdcFaultList("1344487568696"));
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	@Override
	public ArrayList<OBDtoRptAdcFaultInfo> getAdcFaultList(String rptIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoRptAdcFaultInfo> result = new ArrayList<OBDtoRptAdcFaultInfo>();
		try {
			db.openDB();

			OBDtoRptTitle rptTitle = getTitle(rptIndex);

			ArrayList<OBDtoAdcName> adcList = rptTitle.getAdcList();
			for (OBDtoAdcName adcInfo : adcList) {
				OBDtoRptAdcFaultInfo faultInfo = new OBDtoRptAdcFaultInfo();

				faultInfo.setAdcIndex(adcInfo.getIndex());
				faultInfo.setAdcName(adcInfo.getName());
				faultInfo.setAdcIPAddress(new OBAdcManagementImpl().getAdcIPAddress(adcInfo.getIndex(), db));

				OBDtoADCObject adcObject = new OBDtoADCObject();
				adcObject.setCategory(OBDtoADCObject.CATEGORY_ADC);
				adcObject.setIndex(adcInfo.getIndex());
				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(rptTitle.getBeginTime());
				searchOption.setToTime(rptTitle.getEndTime());

				ArrayList<OBDtoAdcSystemLog> logList = new OBMonitoringImpl().getAdcSystemFaultLog(adcObject,
						searchOption);
				faultInfo.setLogList(logList);
				result.add(faultInfo);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}

	@Override
	public ArrayList<OBDtoRptAdcLogInfo> getAdcLogList(String rptIndex) throws OBException {
		OBDatabase db = new OBDatabase();
		ArrayList<OBDtoRptAdcLogInfo> result = new ArrayList<OBDtoRptAdcLogInfo>();
		try {
			db.openDB();

			OBDtoRptTitle rptTitle = getTitle(rptIndex);

			ArrayList<OBDtoAdcName> adcList = rptTitle.getAdcList();
			for (OBDtoAdcName adcInfo : adcList) {
				OBDtoRptAdcLogInfo adcLog = new OBDtoRptAdcLogInfo();

				adcLog.setAdcIndex(adcInfo.getIndex());
				adcLog.setAdcName(adcInfo.getName());
				adcLog.setAdcIPAddress(new OBAdcManagementImpl().getAdcIPAddress(adcInfo.getIndex(), db));

				OBDtoSearch searchOption = new OBDtoSearch();
				searchOption.setFromTime(rptTitle.getBeginTime());
				searchOption.setToTime(rptTitle.getEndTime());
				OBDtoADCObject adcObject = new OBDtoADCObject();
				adcObject.setCategory(OBDtoADCObject.CATEGORY_ADC);
				adcObject.setIndex(adcInfo.getIndex());
				ArrayList<OBDtoAuditLogAdcSystem> logList = new OBAdcManagementImpl()
						.getAdcAuditLogExOrdering(adcObject, searchOption, null, null);
				adcLog.setLogList(logList);
				result.add(adcLog);
			}
		} catch (OBException e) {
			throw e;
		} catch (Exception e) {
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		} finally {
			if (db != null)
				db.closeDB();
		}
		return result;
	}
}
