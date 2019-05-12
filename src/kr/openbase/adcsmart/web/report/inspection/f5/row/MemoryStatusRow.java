package kr.openbase.adcsmart.web.report.inspection.f5.row;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;
import kr.openbase.adcsmart.web.report.inspection.utility.Common;

public class MemoryStatusRow extends AbstractRow
{
    private final F5DataHandler dataHandler;
     private final String swVersion;

    public MemoryStatusRow(final String swVersion, final F5DataHandler dataHandler)
    {
         this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoInspectionReportRow memStatus = new OBDtoInspectionReportRow();

        try
        {
            if(Common.f5SwVersion(swVersion))
            {
                memStatus.setCheckMethod("#tmsh show sys memory");
            }
            else
                memStatus.setCheckMethod("#b memory");
            memStatus.setColumn(col.getFirstColumn());
            memStatus.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_MEM_STATE));
            final int memUsePercent = convertMemUseRatio(dataHandler.getMemTotal(), dataHandler.getMemUsed());
            memStatus.setChecklist(memUsePercent + "%");
            memStatus.setResult(checkMemLoadStatus(memUsePercent));
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, memStatus.toString());
        return memStatus;
    }

    private int convertMemUseRatio(long memTotal, long memUsed) throws Exception
    {
        int memUseRatio = (int) (((float) memUsed * 100) / memTotal);
        return memUseRatio;
    }

    private String checkMemLoadStatus(int memUseRatio)
    {
        if(memUseRatio >= 50)
        {
            return OBMessages.getMessage(OBMessages.MSG_RPT_WARNING);
        }
        else
        {
            return OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL);
        }
    }
}
