package kr.openbase.adcsmart.web.report.inspection.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcName;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

/**
 * OBReport 인터페이스를 구현했으나 많은 모듈들을 정기점검 보고서에는 필요하지 않아 구현하지 않게 만들었다.
 * 
 * @author 최영조
 */
public class InspectionReportImpl
{
    private ArrayList<OBDtoAdcName> getAdcList(String index) throws OBException
    {
        final OBDatabase db = new OBDatabase();
        ArrayList<OBDtoAdcName> retVal = new ArrayList<OBDtoAdcName>();
        String sqlText = "";
        try
        {
            db.openDB();
            sqlText = String.format(" SELECT ADC_INDEX_LIST, ADC_NAME_LIST FROM LOG_REPORT WHERE INDEX IN (%s) ", OBParser.sqlString(index));

            sqlText += ";";

            ResultSet rs = db.executeQuery(sqlText);
            if(rs.next() == true)
            {
                ArrayList<Integer> adcIndexList = convertAdcIndexList(db.getString(rs, "ADC_INDEX_LIST"));
                ArrayList<OBDtoAdcInfo> adcInfoList = new OBAdcManagementImpl().getBasicAdcInfoListForReport(adcIndexList);
                for(OBDtoAdcInfo adcInfo : adcInfoList)
                {
                    OBDtoAdcName obj = new OBDtoAdcName();
                    obj.setIndex(adcInfo.getIndex());
                    obj.setIpaddress(adcInfo.getAdcIpAddress());
                    obj.setName(adcInfo.getName());
                    retVal.add(obj);
                }
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
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
        finally
        {
            if(db != null)
            {
                db.closeDB();
            }
        }

        return retVal;
    }

    private ArrayList<Integer> convertAdcIndexList(String indexList)
    {
        ArrayList<Integer> result = new ArrayList<Integer>();

        if(indexList == null)
        {
            return result;
        }

        String[] indexs = indexList.split(",");
        for(String index : indexs)
        {
            index = index.replace("\"", "").trim();
            result.add(Integer.parseInt(index));
        }
        return result;
    }

    public OBReportInfo getReportInfo(String rptIndex)
    {
        final OBDatabase db = new OBDatabase();
        OBReportInfo result = new OBReportInfo();

        StringBuilder builder = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
        // String sqlText = "";
        try
        {
            db.openDB();

            builder.append(" SELECT INDEX, OCCUR_TIME, STATUS, NAME, BEGIN_TIME, END_TIME, TYPE, ADC_INDEX_LIST ");
            builder.append(" ADC_NAME_LIST, ACCNT_ID, FILE_NAME, ACCNT_INDEX, FILE_TYPE, EXTRA_INFO ");
            builder.append(" FROM LOG_REPORT ");
            builder.append(" WHERE INDEX = ");
            builder.append(OBParser.sqlString(rptIndex));
            builder.append(" AND AVAILABLE = ");
            builder.append(OBDefine.STATE_ENABLE);
            builder.append(";");

            ResultSet rs = db.executeQuery(builder.toString());
            if(rs.next())
            {
                result.setIndex(db.getString(rs, "INDEX"));
                result.setAccountID(db.getString(rs, "ACCNT_ID"));
                result.setAccountIndex(db.getInteger(rs, "ACCNT_INDEX"));
                result.setAdcList(getAdcList(result.getIndex()));
                result.setBeginTime(OBDateTime.toDate(db.getTimestamp(rs, "BEGIN_TIME")));
                result.setEndTime(OBDateTime.toDate(db.getTimestamp(rs, "END_TIME")));
                result.setName(db.getString(rs, "NAME"));
                result.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
                result.setStatus(db.getInteger(rs, "STATUS"));
                result.setRptType(db.getInteger(rs, "TYPE"));
                result.setFileName(db.getString(rs, "FILE_NAME"));
                result.setFileType(db.getInteger(rs, "FILE_TYPE"));
                result.setExtraInfo(db.getString(rs, "EXTRA_INFO"));
            }

            OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, result.toString());
            rs.close();
        }
        catch(Exception e)
        {
            OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
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
