package kr.openbase.adcsmart.web.report.inspection.f5.row;

import java.util.List;

import kr.openbase.adcsmart.service.dto.OBNameValue;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class CPULoadRow extends AbstractRow
{
    private final F5DataHandler dataHandler;
    // private final String swVersion;

    public CPULoadRow(final String swVersion, final F5DataHandler dataHandler)
    {
        // this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoInspectionReportRow cpuStatus = new OBDtoInspectionReportRow();
        try
        {
            cpuStatus.setColumn(col.getFirstColumn());
            cpuStatus.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_CPU_USAGE));
            cpuStatus.setCheckMethod("#tmstat");
            final int cpuUseRatio = convertCPUUseRatio(dataHandler.getCpuLoadStatusList());
            cpuStatus.setChecklist(cpuUseRatio + "%");
            cpuStatus.setResult(checkCPULoadStatus(cpuUseRatio));
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, cpuStatus.toString());
        return cpuStatus;
    }

    private int convertCPUUseRatio(List<OBNameValue> cpuStatusList) throws Exception
    {
        int cpuLoad = 0;
        for(OBNameValue obj : cpuStatusList)
        {
            cpuLoad += Integer.parseInt(obj.getValue());
        }

        return (int) (((float) cpuLoad) / cpuStatusList.size());
    }

    private String checkCPULoadStatus(int cpuUseRatio)
    {
        if(cpuUseRatio >= 50)
        {
            return OBMessages.getMessage(OBMessages.MSG_RPT_WARNING);
        }
        else
        {
            return OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL);
        }
    }
}
