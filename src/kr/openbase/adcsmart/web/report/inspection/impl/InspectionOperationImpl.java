package kr.openbase.adcsmart.web.report.inspection.impl;

import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReporHeader;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.manager.ReportDataManager;
import kr.openbase.adcsmart.web.report.inspection.manager.impl.AlteonReportManager;
import kr.openbase.adcsmart.web.report.inspection.manager.impl.F5ReportManager;

/**
 * 
 * 한 페이지로 구성 된 정기점검 보고서를 만들기 위해 필요한 정보들을 제공한다.
 * 
 * @author 최영조
 * 
 */
public class InspectionOperationImpl
{
    private ReportDataManager reportManager;

    public InspectionOperationImpl(OBDtoAdcInfo adcInfo) throws OBException
    {
        reportManager = getValidInstance(adcInfo);
        if(reportManager == null)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, "not supported vendor");
        }
    }

    public OBDtoInspectionReporHeader getAdcInfo(String rptIndex) throws OBException
    {
        if(reportManager != null)
        {
            return reportManager.getAdcInfo();
        }

        return null;
    }

    public List<OBDtoInspectionReportRow> getSystemInfo(String rptIndex) throws OBException
    {

        if(reportManager != null)
        {
            OBReportInfo rptInfo = new InspectionReportImpl().getReportInfo(rptIndex);
            return reportManager.getSystemInfo(rptInfo);
        }

        return null;
    }

    private ReportDataManager getValidInstance(OBDtoAdcInfo adcInfo)
    {
        switch(adcInfo.getAdcType())
        {
        case OBDefine.ADC_TYPE_ALTEON:
            return new AlteonReportManager(adcInfo);
        case OBDefine.ADC_TYPE_F5:
            return new F5ReportManager(adcInfo);
        }
        
        return null;
    }
}
