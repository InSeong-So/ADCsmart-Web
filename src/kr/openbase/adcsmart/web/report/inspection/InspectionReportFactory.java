package kr.openbase.adcsmart.web.report.inspection;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptTitle;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.fault.AdcDto;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReporHeader;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReport;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportInfo;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.impl.InspectionOperationImpl;
import kr.openbase.adcsmart.web.report.inspection.impl.InspectionReportImpl;

/**
 * 시스템 운영 보고서를 생성해주는 클래스다. 한 페이지로 축약해서 보여준다. 기존 코드와 분리를 위해서 새롭게 만들었다.
 * 
 * @author ienter9
 */
public class InspectionReportFactory
{
    /**
     * 정기점검 보고서를 만들어주는 외부 인터페이스다. 실제로 외부에서 호출된다.
     * 
     * @param reportIdx
     * @return
     * @throws Exception
     */
    public static List<OBDtoInspectionReport> getInspectionSummary(String reportIdx) throws Exception
    {
        final InspectionReportFactory reportFactory = new InspectionReportFactory();
        List<OBDtoInspectionReport> reportList = null;

        final List<OBDtoInspectionReportInfo> systemReports = reportFactory.getReportInfo(reportIdx);
        try
        {
            final OBDtoInspectionReportInfo inspectionReportDto = systemReports.get(0);
            final List<AdcDto> adcDtoList = inspectionReportDto.getAdcs();
            reportList = reportFactory.getInspectionReport(reportIdx, adcDtoList);
        }
        catch(Exception e)
        {
            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
            throw e;
        }

        return reportList;
    }

    public List<OBDtoInspectionReportInfo> getReportInfo(String index)
    {
        OBDtoRptTitle rptTitle = getTitle(index);

        OBDtoInspectionReportInfo report = OBDtoInspectionReportInfo.toSystemReportDto(rptTitle);
        List<OBDtoInspectionReportInfo> reports = new ArrayList<OBDtoInspectionReportInfo>();
        reports.add(report);

        return reports;
    }

    public List<OBDtoInspectionReport> getInspectionReport(String reportIndex, List<AdcDto> adcs)
    {
        List<OBDtoInspectionReport> reportList = new ArrayList<OBDtoInspectionReport>();
        for(AdcDto adc : adcs)
        {
            try
            {
                final OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adc.getIndex());
                final InspectionOperationImpl reportSvc = new InspectionOperationImpl(adcInfo);
                final OBDtoInspectionReport report = new OBDtoInspectionReport();

                report.setAdcInfo(getAdc(reportSvc, reportIndex, adcInfo));
                report.setContentsList(getSystemStatus(reportSvc, reportIndex, adcInfo));
                reportList.add(report);
            }
            catch(Exception e)
            {
                OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
            }
        }
        return reportList;
    }

    public OBDtoInspectionReporHeader getAdc(InspectionOperationImpl reportSvc, String reportIndex, OBDtoAdcInfo adcInfo) throws Exception
    {
        try
        {
            OBDtoInspectionReporHeader adcFromSvc = reportSvc.getAdcInfo(reportIndex);

            return adcFromSvc;
        }
        catch(Exception e)
        {
            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("failed to get adcInfo. reportIndex:%s, adcIndex:%d. error:%s", reportIndex, adcInfo.getIndex(), e.getMessage()));
            throw e;
        }
    }

    /**
     * 
     * @param reportIndex
     * @param adcInfo
     * @return
     */
    private List<OBDtoInspectionReportRow> getSystemStatus(InspectionOperationImpl reportSvc, String reportIndex, OBDtoAdcInfo adcInfo)
    {
        List<OBDtoInspectionReportRow> statusFromSvc = null;
        try
        {
            statusFromSvc = reportSvc.getSystemInfo(reportIndex);
        }
        catch(Exception e)
        {
            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        return statusFromSvc;
    }

    private OBDtoRptTitle getTitle(String rptIndex)
    {
        OBReportInfo rptInfo = new InspectionReportImpl().getReportInfo(rptIndex);
        OBDtoRptTitle result = new OBDtoRptTitle();
        result.setIndex(rptInfo.getIndex());
        result.setAdcList(rptInfo.getAdcList());
        result.setBeginTime(rptInfo.getBeginTime());
        result.setEndTime(rptInfo.getEndTime());
        result.setOccurTime(rptInfo.getOccurTime());
        result.setUserIndex(rptInfo.getAccountIndex());
        result.setUserID(rptInfo.getAccountID());
        result.setExtraInfo(rptInfo.getExtraInfo());
        return result;
    }
}
