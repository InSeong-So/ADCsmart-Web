package kr.openbase.adcsmart.web.report.inspection.manager.collection;

import java.util.List;

import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReporHeader;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;

public interface CollectionMethod
{
    OBDtoInspectionReporHeader getAdcInfo();

    List<OBDtoInspectionReportRow> getSystemInfo(OBReportInfo rptInfo);
}
