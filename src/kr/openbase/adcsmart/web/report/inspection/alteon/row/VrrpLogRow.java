package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.sql.ResultSet;
import java.sql.SQLException;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class VrrpLogRow extends AbstractRow
{
//    private final AlteonDataHandler dataHandler;
//    private final String            swVersion;
    private Integer                 adcIndex;

    public VrrpLogRow(final String swVersion, final AlteonDataHandler dataHandler, final Integer adcIndex)
    {
//        this.swVersion = swVersion;
//        this.dataHandler = dataHandler;
        this.adcIndex = adcIndex;
    }

    public OBDtoInspectionReportRow getRow(final Column col) throws OBException
    {
        final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();
        
        try
        {
         // null 값을 주면 바로 윗 항목의 제목을 같이 공유한다.
            reportRowDto.setColumn(col.getForthColumn());
            reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_L3_VRRPLOG_STATE));
            reportRowDto.setCheckMethod("/info/sys/log");
            reportRowDto.setChecklist(checkVrrpLog());
            reportRowDto.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_NORMAL));
            
            if(!reportRowDto.getChecklist().isEmpty())
                reportRowDto.setResult(OBMessages.getMessage(OBMessages.MSG_RPT_ANORMAL));
        }
        catch(Exception e)
        {
            throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
        }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, reportRowDto.toString());
        return reportRowDto;
    }

    private String checkVrrpLog() throws OBException    
    {
        String sqlText = "";
        OBDatabase db = new OBDatabase();
        String retVal = "";
        
        try
        {
            db.openDB();
            
            sqlText = String.format(" SELECT * FROM LOG_ADC_SYSLOG WHERE ADC_INDEX = %d          \n" + 
                         " AND EVENT LIKE %s                                                     \n" +
                         " AND OCCUR_TIME > (CURRENT_DATE - INTERVAL '30 days') LIMIT 1;         \n",
                adcIndex, OBParser.sqlString(OBDefine.VRRP_INCORRECT.VRRP_INCORRECT));
            
            ResultSet rs = db.executeQuery(sqlText);
            if(rs.next() == true)
            {
                retVal = "Received incorrect addresses";
                return retVal;
            }
        }
        catch(SQLException e)
        {
            throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sqlText));
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
        finally
        {
            if(db != null)
                db.closeDB();
        }
        return retVal;
    }
}
