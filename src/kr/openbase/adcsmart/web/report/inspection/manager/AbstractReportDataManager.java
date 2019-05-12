package kr.openbase.adcsmart.web.report.inspection.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptPortErrDiscard;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptPortInfo;
import kr.openbase.adcsmart.service.impl.OBAdcConfigHistoryImpl;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBParser;

public abstract class AbstractReportDataManager implements ReportDataManager
{
    protected Timestamp getLastApplyTime(int adcIndex) throws OBException
    {
        HashMap<String, Timestamp> hashMap = new OBAdcConfigHistoryImpl().getLastConfigTimeList(adcIndex);
        ArrayList<Timestamp> timeList = new ArrayList<Timestamp>(hashMap.values());
        if(timeList.size() == 0)
            return null;

        Timestamp lastTime = timeList.get(0);
        for(Timestamp time : timeList)
        {
            if(time.getTime() > lastTime.getTime())
                lastTime = time;
        }
        return lastTime;
    }

    protected String convertLinkStatus(int status) throws OBException
    {
        switch(status)
        {
        case OBDefine.L2_LINK_STATUS_UP:// link up
            return OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_UP);// OBTerminologyDB.TYPE_GENERAL_UP;
        case OBDefine.L2_LINK_STATUS_DOWN:// down
            return OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DOWN);// OBTerminologyDB.TYPE_GENERAL_DOWN;
        case OBDefine.L2_LINK_STATUS_DISABLED:// disabled
            return OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_DISALBED);// OBTerminologyDB.TYPE_GENERAL_DISALBED;
        case OBDefine.L2_LINK_STATUS_INOPERATIVE:// inoperative
            return OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_INOPERATIVE);// OBTerminologyDB.TYPE_GENERAL_INOPERATIVE;
        default:
            return OBMessages.getMessage(OBMessages.MSG_RPT_TYPE_GENERAL_INOPERATIVE);// OBTerminologyDB.TYPE_GENERAL_INOPERATIVE;
        }
    }

    protected HashMap<String, OBDtoRptPortInfo> getPortInfo(OBDtoAdcInfo adcInfo, ArrayList<String> portNameList, Date lastTime) throws OBException
    {
        String sqlText = "";
        HashMap<String, OBDtoRptPortInfo> result = new HashMap<String, OBDtoRptPortInfo>();
        final OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();

            for(String portName : portNameList)
            {
                sqlText = String.format(" SELECT OCCUR_TIME, ERRORS_IN, ERRORS_OUT, DROPS_IN, DROPS_OUT FROM LOG_ADC_PORTS WHERE ADC_INDEX=%d AND PORT_NAME=%s ", adcInfo.getIndex(), OBParser.sqlString(portName));

                String sqlTime = "";
                if(lastTime != null)
                    sqlTime += String.format(" OCCUR_TIME >= %s ", OBParser.sqlString(OBDateTime.toString(new Timestamp(lastTime.getTime()))));

                if(sqlTime != null && !sqlTime.isEmpty())
                    sqlText += " AND" + sqlTime;

                sqlText += " ORDER BY OCCUR_TIME DESC ";

                sqlText += ";";

                ResultSet rs = db.executeQuery(sqlText);
                ArrayList<OBDtoRptPortErrDiscard> errDiscardList = new ArrayList<OBDtoRptPortErrDiscard>();
                while(rs.next())
                {
                    OBDtoRptPortErrDiscard errDiscard = new OBDtoRptPortErrDiscard();
                    errDiscard.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
                    errDiscard.setDiscards(db.getLong(rs, "DROPS_IN") + db.getLong(rs, "DROPS_OUT"));
                    errDiscard.setErrors(db.getLong(rs, "ERRORS_IN") + db.getLong(rs, "ERRORS_OUT"));
                    errDiscardList.add(errDiscard);
                }
                
                rs.close();

                OBDtoRptPortInfo portInfo = new OBDtoRptPortInfo();
                portInfo.setPortName(portName);
                portInfo.setErrDiscardsList(errDiscardList);
                result.put(portName, portInfo);
            }
        }
        catch(SQLException e)
        {
            throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(), sqlText));

        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
        }
        finally
        {
            if(db != null)
            {
                db.closeDB();
            }
        }

        return result;
    }
}
