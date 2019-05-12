package kr.openbase.adcsmart.web.report.inspection.f5.row;

import kr.openbase.adcsmart.service.impl.f5.handler.dto.OBDtoNtpInfoF5;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

/**
 * F5 장비의 NTP 정보 줄
 * 
 * @author 최영조
 */
public class NTPStatusRow extends AbstractRow
{
    private final F5DataHandler dataHandler;
    // private final String swVersion;

    public NTPStatusRow(final String swVersion, final F5DataHandler dataHandler)
    {
        // this.swVersion = swVersion;
        this.dataHandler = dataHandler;
    }

    @Override
    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        OBDtoNtpInfoF5 ntpInfo = dataHandler.getNTPInfo();

        OBDtoInspectionReportRow ntpStatus = new OBDtoInspectionReportRow();
        try
        {
            ntpStatus.setColumn(col.getFifthColumn());
            ntpStatus.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_ETC_NTP_INFO));
            ntpStatus.setCheckMethod("#ntpq -np");
            ntpStatus.setChecklist(ntpInfo.getRemoteIPAddress());
            ntpStatus.setResult(checkNtpStatus(ntpInfo));
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, ntpStatus.toString());
        return ntpStatus;
    }

    private String checkNtpStatus(OBDtoNtpInfoF5 ntpInfo) throws Exception
    {
        if(ntpInfo == null)
            return OBMessages.getMessage(OBMessages.MSG_RPT_NOT_INTERLOCK);
        if(ntpInfo.getReferenceID().contains("LOCL"))
            return OBMessages.getMessage(OBMessages.MSG_RPT_NOT_INTERLOCK);
        if(ntpInfo.getReachTime() == 0)
            return OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL);
        return OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL);
    }

}
