package kr.openbase.adcsmart.web.report.inspection.manager.impl;

import java.io.File;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.web.report.inspection.alteon.AlteonCollectionMethod;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReporHeader;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.manager.AbstractReportDataManager;
import kr.openbase.adcsmart.web.report.inspection.manager.collection.CollectionMethod;

public class AlteonReportManager extends AbstractReportDataManager
{
    private static final String CFG_SNMP_FILE_NAME      = "rpt.alteon.snmp";
    private static final String CFG_SNMP_FILE_FULL_NAME = OBDefine.CFG_DIR + CFG_SNMP_FILE_NAME;
    
    private CollectionMethod    useMethodsBehavior;

    public AlteonReportManager(OBDtoAdcInfo adcInfo)
    {
        useMethodsBehavior = new AlteonCollectionMethod(adcInfo);
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

    /*
     * SNMP와 CLI 환경을 혼용해서 쓰기 때문에 아래 기능을 사용하지 않는다.
     */
    @SuppressWarnings("unused")
    private boolean isAvailableSnmpAlteon()
    {
        try
        {
            return new File(CFG_SNMP_FILE_FULL_NAME).exists();
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
