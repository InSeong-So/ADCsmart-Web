package kr.openbase.adcsmart.web.report.inspection.manager.impl;

import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReporHeader;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.F5CollectionMethod;
import kr.openbase.adcsmart.web.report.inspection.manager.AbstractReportDataManager;
import kr.openbase.adcsmart.web.report.inspection.manager.collection.CollectionMethod;

public class F5ReportManager extends AbstractReportDataManager
{
    private CollectionMethod useMethodsBehavior;

    public F5ReportManager(OBDtoAdcInfo adcInfo)
    {
        useMethodsBehavior = new F5CollectionMethod(adcInfo);
    }

    @Override
    public OBDtoInspectionReporHeader getAdcInfo()
    {
        return useMethodsBehavior.getAdcInfo();        
    }

    @Override
    public List<OBDtoInspectionReportRow> getSystemInfo(OBReportInfo rptInfo)
    {
        return useMethodsBehavior.getSystemInfo(rptInfo);
    }

}
