package kr.openbase.adcsmart.web.report.inspection.manager;

import java.util.List;

import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReporHeader;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;

public interface ReportDataManager
{
    public OBDtoInspectionReporHeader getAdcInfo();

    public List<OBDtoInspectionReportRow> getSystemInfo(OBReportInfo rptInfo);
}
