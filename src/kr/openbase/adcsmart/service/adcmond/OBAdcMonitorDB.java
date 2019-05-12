package kr.openbase.adcsmart.service.adcmond;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import kr.openbase.adcsmart.service.alarm.OBAlarmDB;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoActiveStandbyState;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcPoolMemberF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerF5;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;
import kr.openbase.adcsmart.service.dto.OBDtoAlarmConfig;
import kr.openbase.adcsmart.service.dto.OBDtoVServerStatus;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBAlarmImpl;
import kr.openbase.adcsmart.service.impl.OBVServerDB;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcSystemInfo;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolMemberPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcPoolPAS;
import kr.openbase.adcsmart.service.impl.pas.dto.OBDtoAdcVServerPAS;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolMemberPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcPoolPASK;
import kr.openbase.adcsmart.service.impl.pask.dto.OBDtoAdcVServerPASK;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBDefineFault;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;

public class OBAdcMonitorDB
{
    public OBDtoActiveStandbyState writeAdcActiveStandbyState(int adcIndex, int state) throws OBException
    {
        OBDtoActiveStandbyState retVal = new OBDtoActiveStandbyState();
        String sqlText = "";

        retVal.setPrevState(OBDtoActiveStandbyState.ACTIVE_STANDBY_STATE_UNKNOWN);
        retVal.setCurrState(state);
        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();

            sqlText =
                    String.format(" SELECT ADC_INDEX, ACTIVE_BACKUP_STATE \n"
                            + " FROM MNG_ADC_ADDITIONAL               \n"
                            + " WHERE ADC_INDEX=%d                    \n"
                            + " LIMIT 1                               \n", adcIndex);

            ResultSet rs = db.executeQuery(sqlText);
            if(rs.next() == true)
            {// 데이터가 있는 경우.
                retVal.setPrevState(db.getInteger(rs, "ACTIVE_BACKUP_STATE"));
                sqlText =
                        String.format(" UPDATE MNG_ADC_ADDITIONAL        \n"
                                + " SET ACTIVE_BACKUP_STATE = %d     \n"
                                + " WHERE ADC_INDEX=%d               \n", state, adcIndex);
                db.executeUpdate(sqlText);

            }
            else
            {// 데이터가 없는 경우.
                sqlText =
                        String.format(" INSERT INTO MNG_ADC_ADDITIONAL        \n"
                                + " (ADC_INDEX, ACTIVE_BACKUP_STATE )     \n"
                                + " VALUES                                \n"
                                + " ( %d, %d )                            \n", adcIndex, state);
                db.executeUpdate(sqlText);
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

    private void writeVServerStatusAlteon(Integer adcIndex, ArrayList<OBDtoAdcVServerAlteon> vsList) throws OBException
    {
        ArrayList<String> disabledList = new ArrayList<String>();
        ArrayList<String> availableList = new ArrayList<String>();
        ArrayList<String> unavailableList = new ArrayList<String>();
        // ArrayList<String> blockedList=new ArrayList<String>(); //검색 : task:blocked_cancel

        // vs status 업데이트.
        for(OBDtoAdcVServerAlteon vs : vsList)
        {
            if(vs.getStatus() == OBDefine.VS_STATUS.DISABLE)
                disabledList.add(vs.getIndex());
            else if(vs.getStatus() == OBDefine.VS_STATUS.AVAILABLE)
                availableList.add(vs.getIndex());
            else
                // (vs.getStatus()==OBDefine.VS_STATUS.UNAVAILABLE)
                unavailableList.add(vs.getIndex());
        }

        String sqlText = "";
        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
            String indexList = "''"; // where-in empty방지
            // available status
            for(String index : availableList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VSERVER  \n"
                                + " SET STATUS=%d           \n"
                                + " WHERE INDEX IN ( %s );  \n", // where-in:empty string 불가, null 불가, OK
                                OBDefine.VS_STATUS.AVAILABLE, indexList);
                db.executeUpdate(sqlText);
            }

            // unavailable status
            indexList = "''"; // where-in empty방지
            for(String index : unavailableList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VSERVER   \n"
                                + " SET STATUS=%d            \n"
                                + " WHERE INDEX IN ( %s );   \n", // where-in:empty string 불가, null 불가, OK
                                OBDefine.VS_STATUS.UNAVAILABLE, indexList);
                db.executeUpdate(sqlText);
            }

            // disabled status
            indexList = "''"; // where-in empty방지
            for(String index : disabledList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VSERVER \n"
                                + " SET STATUS=%d          \n"
                                + " WHERE INDEX IN ( %s ); \n", // where-in:empty string 불가, null 불가, OK
                                OBDefine.VS_STATUS.DISABLE, indexList);
                db.executeUpdate(sqlText);
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
    }

    private void writeVServiceStatusAlteon(Integer adcIndex, ArrayList<OBDtoAdcVServerAlteon> vsList) throws OBException
    {
        ArrayList<String> disabledList = new ArrayList<String>();
        ArrayList<String> availableList = new ArrayList<String>();
        ArrayList<String> unavailableList = new ArrayList<String>();

        // vs status 업데이트.
        for(OBDtoAdcVServerAlteon vs : vsList)
        {
            ArrayList<OBDtoAdcVService> vserviceList = vs.getVserviceList();
            if(vserviceList == null || vserviceList.size() == 0)
                continue;
            for(OBDtoAdcVService vsrv : vserviceList)
            {
                if(vsrv.getStatus() == OBDefine.VS_STATUS.DISABLE)
                    disabledList.add(vsrv.getServiceIndex());
                else if(vsrv.getStatus() == OBDefine.VS_STATUS.AVAILABLE)
                    availableList.add(vsrv.getServiceIndex());
                else
                    // if(vsrv.getStatus()==OBDefine.VS_STATUS.UNAVAILABLE)
                    unavailableList.add(vsrv.getServiceIndex());
            }
        }

        String sqlText = "";
        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
            String indexList = "''"; // where-in empty 방지
            // available status
            for(String index : availableList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VS_SERVICE \n"
                                + " SET STATUS=%d             \n"
                                + " WHERE INDEX IN ( %s );    \n" // where-in:empty string 불가, null 불가, OK
                        , OBDefine.VS_STATUS.AVAILABLE, indexList);
                db.executeUpdate(sqlText);
            }

            // unavailable status
            indexList = "''"; // where-in empty 방지
            for(String index : unavailableList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VS_SERVICE \n"
                                + " SET STATUS=%d             \n"
                                + " WHERE INDEX IN ( %s );    \n" // where-in:empty string 불가, null 불가, OK
                        , OBDefine.VS_STATUS.UNAVAILABLE, indexList);

                db.executeUpdate(sqlText);
            }

            // disabled status
            indexList = "''"; // where-in empty 방지
            for(String index : disabledList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VS_SERVICE  \n"
                                + " SET STATUS=%d              \n"
                                + " WHERE INDEX IN ( %s );     \n" // where-in:empty string 불가, null 불가, OK
                        , OBDefine.VS_STATUS.DISABLE, indexList);
                db.executeUpdate(sqlText);
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
    }

    private void writegGroupStatusAlteon(Integer adcIndex) throws OBException
    {
        OBDatabase db = new OBDatabase();

        ArrayList<String> disabledList = new ArrayList<String>();
        ArrayList<String> availableList = new ArrayList<String>();
        ArrayList<String> unavailableList = new ArrayList<String>();

        int availableCount = 0;
        int disableCount = 0;
        int unavailableCount = 0;
        ArrayList<OBDtoAdcPoolAlteon> poolList = new OBVServerDB().getFlbGroupInfoAlteon(adcIndex);
        // group status 업데이트.
        if(poolList == null || poolList.size() == 0)
        {
            return;
        }
        for(OBDtoAdcPoolAlteon pool : poolList)
        {
            ArrayList<OBDtoAdcPoolMemberAlteon> realList = pool.getMemberList();
            if(realList == null || realList.size() == 0)
            {
                unavailableList.add(pool.getIndex());
                continue;
            }
            availableCount = 0;
            disableCount = 0;
            unavailableCount = 0;
            for(OBDtoAdcPoolMemberAlteon real : realList)
            {
                if(real.getStatus() == OBDefine.STATUS_AVAILABLE) // status가 state에 있다.
                {
                    availableCount++;
                    break;
                }
                else if(real.getStatus() == OBDefine.STATUS_DISABLE)
                {
                    disableCount++;
                }
                else
                // STATUS_UNAVAILABLE
                {
                    unavailableCount++;
                }
            }
            if(availableCount > 0)
            {
                availableList.add(pool.getIndex());
            }
            else if(disableCount == realList.size()) // 모두 disable이면
            {
                disabledList.add(pool.getIndex());
            }
            else if(unavailableCount > 0)
            {
                unavailableList.add(pool.getIndex());
            }
            else
            // 그룹의 real이 없으면
            {
                unavailableList.add(pool.getIndex());
            }
        }

        String sqlText = "";
        try
        {
            db.openDB();

            String indexList = "''"; // where-in empty 방지
            // available status
            for(String index : availableList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_POOL SET STATUS=%d WHERE INDEX IN ( %s ); ", OBDefine.STATUS_AVAILABLE,
                                indexList);
                db.executeUpdate(sqlText);
            }

            // unavailable status
            indexList = "''"; // where-in empty 방지
            for(String index : unavailableList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_POOL SET STATUS=%d WHERE INDEX IN ( %s ); ",
                                OBDefine.STATUS_UNAVAILABLE, indexList);
                db.executeUpdate(sqlText);
            }
            // disabled status
            indexList = "''"; // where-in empty 방지
            for(String index : disabledList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_POOL SET STATUS=%d WHERE INDEX IN ( %s ); ", OBDefine.VS_STATUS.DISABLE,
                                indexList);
                db.executeUpdate(sqlText);
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
    }

    private void writePoolMemberStatusAlteon(Integer adcIndex, ArrayList<OBDtoAdcVServerAlteon> vsList) throws OBException
    {
        if(vsList == null || vsList.isEmpty())
        {
            return;
        }
        String sqlText = "";
        String sqlBaseText = "";
        String sqlDataText = "";

        String szIndex = "";
        String szVsIndex = "";
        String szVSrcIndex = "";
        String szPoolmemberIndex = "";

        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
            // 테이블에 있는 이전 데이터 삭제.
            sqlText =
                    String.format(" DELETE FROM TMP_SLB_POOLMEMBER_STATUS  \n" + " WHERE ADC_INDEX = %d;           		  \n",
                            adcIndex);
            db.executeUpdate(sqlText);

            // 데이터 추가를 위한 기본 쿼리 작성.
            sqlBaseText =
                    String.format(" INSERT INTO TMP_SLB_POOLMEMBER_STATUS "
                            + " (INDEX, ADC_INDEX, VS_INDEX, VSRC_INDEX, POOLMEMBER_INDEX, MEMBER_STATUS) "
                            + " VALUES ");

            sqlDataText = "";
            // vs status 업데이트.
            for(OBDtoAdcVServerAlteon vs : vsList)
            {
                ArrayList<OBDtoAdcVService> vserviceList = vs.getVserviceList();
                if(vserviceList == null || vserviceList.size() == 0)
                    continue;
                for(OBDtoAdcVService vsrv : vserviceList)
                {
                    OBDtoAdcPoolAlteon pool = vsrv.getPool();
                    if(pool == null)
                        continue;

                    ArrayList<OBDtoAdcPoolMemberAlteon> memberList = pool.getMemberList();
                    if(memberList == null || memberList.size() == 0)
                        continue;
                    for(OBDtoAdcPoolMemberAlteon member : memberList)
                    {
                        if(false == sqlDataText.isEmpty())
                            sqlDataText += ", ";

                        szVsIndex = OBCommon.makeVSIndexAlteon(adcIndex, vs.getAlteonId());
                        szIndex =
                                OBCommon.makePoolMemberStatusIndexAlteon(adcIndex, vs.getAlteonId().toString(), 0, vsrv
                                        .getServicePort(), pool.getAlteonId().toString(), member.getAlteonNodeID().toString(),
                                        0);
                        szVSrcIndex = OBCommon.makeVSrcIndexAlteon(adcIndex, szVsIndex, vsrv.getServicePort());
                        szPoolmemberIndex =
                                OBCommon.makePoolMemberIndexAlteon(adcIndex, pool.getAlteonId(), member.getAlteonNodeID(), 0);

                        sqlDataText +=
                                String.format(" (%s, %d, %s, %s, %s, %d) ", OBParser.sqlString(szIndex), adcIndex,
                                        OBParser.sqlString(szVsIndex), OBParser.sqlString(szVSrcIndex),
                                        OBParser.sqlString(szPoolmemberIndex), member.getStatus());
                    }
                }
            }

            if(false == sqlDataText.isEmpty())
            {
                sqlText = sqlBaseText + sqlDataText;
                db.executeUpdate(sqlText);
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
    }

   // 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할 예정이다.
    private void writeVServerStatusPAS(Integer adcIndex, ArrayList<OBDtoAdcVServerPAS> vsList) throws OBException
    {
        ArrayList<String> disabledList = new ArrayList<String>();
        ArrayList<String> availableList = new ArrayList<String>();
        ArrayList<String> unavailableList = new ArrayList<String>();

        // vs status 업데이트.
        for(OBDtoAdcVServerPAS vs : vsList)
        {
            if(vs.getStatus() == OBDefine.VS_STATUS.DISABLE)
                disabledList.add(vs.getDbIndex());
            else if(vs.getStatus() == OBDefine.VS_STATUS.AVAILABLE)
                availableList.add(vs.getDbIndex());
            else
                // (vs.getStatus()==OBDefine.VS_STATUS.UNAVAILABLE)
                unavailableList.add(vs.getDbIndex());
        }

        String sqlText = "";
        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
            String indexList = "''"; // where-in empty방지
            // available status
            for(String index : availableList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VSERVER  \n"
                                + " SET STATUS=%d           \n"
                                + " WHERE INDEX IN ( %s );  \n", // where-in:empty string 불가, null 불가, OK
                                OBDefine.VS_STATUS.AVAILABLE, indexList);
                db.executeUpdate(sqlText);
            }

            // unavailable status
            indexList = "''"; // where-in empty방지
            for(String index : unavailableList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VSERVER   \n"
                                + " SET STATUS=%d            \n"
                                + " WHERE INDEX IN ( %s );   \n", // where-in:empty string 불가, null 불가, OK
                                OBDefine.VS_STATUS.UNAVAILABLE, indexList);
                db.executeUpdate(sqlText);
            }

            // disabled status
            indexList = "''"; // where-in empty방지
            for(String index : disabledList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VSERVER \n"
                                + " SET STATUS=%d          \n"
                                + " WHERE INDEX IN ( %s ); \n", // where-in:empty string 불가, null 불가, OK
                                OBDefine.VS_STATUS.DISABLE, indexList);
                db.executeUpdate(sqlText);
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
    }

   private void writeVServerStatusPASK(Integer adcIndex, ArrayList<OBDtoAdcVServerPASK> vsList) throws OBException
    {
        ArrayList<String> disabledList = new ArrayList<String>();
        ArrayList<String> availableList = new ArrayList<String>();
        ArrayList<String> unavailableList = new ArrayList<String>();

        // vs status 업데이트.
        for(OBDtoAdcVServerPASK vs : vsList)
        {
            if(vs.getStatus() == OBDefine.VS_STATUS.DISABLE)
                disabledList.add(vs.getDbIndex());
            else if(vs.getStatus() == OBDefine.VS_STATUS.AVAILABLE)
                availableList.add(vs.getDbIndex());
            else
                // (vs.getStatus()==OBDefine.VS_STATUS.UNAVAILABLE)
                unavailableList.add(vs.getDbIndex());
        }

        String sqlText = "";
        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
            String indexList = "''"; // where-in empty방지
            // available status
            for(String index : availableList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VSERVER  \n"
                                + " SET STATUS=%d           \n"
                                + " WHERE INDEX IN ( %s );  \n", // where-in:empty string 불가, null 불가, OK
                                OBDefine.VS_STATUS.AVAILABLE, indexList);
                db.executeUpdate(sqlText);
            }

            // unavailable status
            indexList = "''"; // where-in empty방지
            for(String index : unavailableList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VSERVER   \n"
                                + " SET STATUS=%d            \n"
                                + " WHERE INDEX IN ( %s );   \n", // where-in:empty string 불가, null 불가, OK
                                OBDefine.VS_STATUS.UNAVAILABLE, indexList);
                db.executeUpdate(sqlText);
            }

            // disabled status
            indexList = "''"; // where-in empty방지
            for(String index : disabledList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VSERVER \n"
                                + " SET STATUS=%d          \n"
                                + " WHERE INDEX IN ( %s ); \n", // where-in:empty string 불가, null 불가, OK
                                OBDefine.VS_STATUS.DISABLE, indexList);
                db.executeUpdate(sqlText);
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
    }



    private void writePoolMemberStatusPAS(Integer adcIndex, ArrayList<OBDtoAdcVServerPAS> vsList) throws OBException
    {
        if(vsList == null || vsList.isEmpty())
        {
            return;
        }
        String sqlText = "";
        String sqlBaseText = "";
        String sqlDataText = "";

        String szIndex = "";
        String szVsIndex = "";
        String szVSrcIndex = "";
        String szNodeIndex = "";
        String szPoolmemberIndex = "";

        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
            // 테이블에 있는 이전 데이터 삭제.
            sqlText =
                    String.format(" DELETE FROM TMP_SLB_POOLMEMBER_STATUS  \n" + " WHERE ADC_INDEX = %d;           		  \n",
                            adcIndex);
            db.executeUpdate(sqlText);

            // 데이터 추가를 위한 기본 쿼리 작성.
            sqlBaseText =
                    String.format(" INSERT INTO TMP_SLB_POOLMEMBER_STATUS "
                            + " (INDEX, ADC_INDEX, VS_INDEX, VSRC_INDEX, POOLMEMBER_INDEX, MEMBER_STATUS) "
                            + " VALUES ");

            sqlDataText = "";

            for(OBDtoAdcVServerPAS vs : vsList)
            {
                OBDtoAdcPoolPAS pool = vs.getPool();
                ;
                if(pool == null)
                    continue;

                ArrayList<OBDtoAdcPoolMemberPAS> memberList = pool.getMemberList();
                if(memberList == null || memberList.size() == 0)
                    continue;
                for(OBDtoAdcPoolMemberPAS member : memberList)
                {
                    if(false == sqlDataText.isEmpty())
                        sqlDataText += ", ";

                    szVsIndex = OBCommon.makeVSIndexPAS(adcIndex, vs.getName());
                    szVSrcIndex = OBCommon.makePoolIndex(adcIndex, pool.getName());
                    szNodeIndex = OBCommon.makeNodeIndexPAS(adcIndex, member.getDbIndex(), member.getId());
                    szPoolmemberIndex =
                            OBCommon.makePoolMemberIndexPAS(adcIndex, vs.getDbIndex(), szNodeIndex, member.getPort());// member.getPort());
                    szIndex = OBCommon.makePoolMemberStatusIndexPAS(adcIndex, vs.getName(), pool.getName(), member.getId());

                    sqlDataText +=
                            String.format(" (%s, %d, %s, %s, %s, %d) ", OBParser.sqlString(szIndex), adcIndex,
                                    OBParser.sqlString(szVsIndex), OBParser.sqlString(szVSrcIndex),
                                    OBParser.sqlString(szPoolmemberIndex), member.getStatus());
                }
            }

            if(false == sqlDataText.isEmpty())
            {
                sqlText = sqlBaseText + sqlDataText;
                db.executeUpdate(sqlText);
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
    }

    private void writePoolMemberStatusPASK(Integer adcIndex, ArrayList<OBDtoAdcVServerPASK> vsList) throws OBException
    {
        if(vsList == null || vsList.isEmpty())
        {
            return;
        }
        String sqlText = "";
        String sqlBaseText = "";
        String sqlDataText = "";

        String szIndex = "";
        String szVsIndex = "";
        String szVSrcIndex = "";
        String szNodeIndex = "";
        String szPoolmemberIndex = "";

        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
            // 테이블에 있는 이전 데이터 삭제.
            sqlText = " DELETE FROM TMP_SLB_POOLMEMBER_STATUS WHERE ADC_INDEX = " + adcIndex + ";";
            db.executeUpdate(sqlText);

            // 데이터 추가를 위한 기본 쿼리 작성.
            sqlBaseText =
                    " INSERT INTO TMP_SLB_POOLMEMBER_STATUS "
                            + " (INDEX, ADC_INDEX, VS_INDEX, VSRC_INDEX, POOLMEMBER_INDEX, MEMBER_STATUS) "
                            + " VALUES ";

            sqlDataText = "";

            for(OBDtoAdcVServerPASK vs : vsList)
            {
                OBDtoAdcPoolPASK pool = vs.getPool();
                ;
                if(pool == null)
                    continue;

                ArrayList<OBDtoAdcPoolMemberPASK> memberList = pool.getMemberList();
                if(memberList == null || memberList.size() == 0)
                    continue;
                for(OBDtoAdcPoolMemberPASK member : memberList)
                {
                    if(false == sqlDataText.isEmpty())
                        sqlDataText += ", ";

                    szVsIndex = OBCommon.makeVSIndexPASK(adcIndex, vs.getName());
                    szVSrcIndex = OBCommon.makePoolIndex(adcIndex, pool.getName());
                    szNodeIndex = OBCommon.makeNodeIndexPASK(adcIndex, member.getId());
                    szPoolmemberIndex =
                            OBCommon.makePoolMemberIndexPASK(adcIndex, vs.getDbIndex(), szNodeIndex, member.getPort());// member.getPort());
                    szIndex = OBCommon.makePoolMemberStatusIndexPASK(adcIndex, vs.getName(), pool.getName(), member.getId());

                    sqlDataText +=
                            String.format(" (%s, %d, %s, %s, %s, %d) ", OBParser.sqlString(szIndex), adcIndex,
                                    OBParser.sqlString(szVsIndex), OBParser.sqlString(szVSrcIndex),
                                    OBParser.sqlString(szPoolmemberIndex), member.getStatus());
                }
            }

            if(false == sqlDataText.isEmpty())
            {
                sqlText = sqlBaseText + sqlDataText;
                db.executeUpdate(sqlText);
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
                db.closeDB();
        }
    }

    public void writeSlbStatusPAS(Integer adcIndex, ArrayList<OBDtoAdcVServerPAS> vsList) throws OBException
    {// vs, poolmember의 status를 저장한다.
        writeVServerStatusPAS(adcIndex, vsList);
        writePoolMemberStatusPAS(adcIndex, vsList);
    }

    public void writeSlbStatusPASK(Integer adcIndex, ArrayList<OBDtoAdcVServerPASK> vsList) throws OBException
    {// vs, poolmember의 status를 저장한다.
        writeVServerStatusPASK(adcIndex, vsList);
        writePoolMemberStatusPASK(adcIndex, vsList);
    }

    public void writeSlbStatusAlteon(Integer adcIndex, ArrayList<OBDtoAdcVServerAlteon> vsList) throws OBException
    {// vs, vsrv, poolmember의 status를 저장한다.
        writeVServerStatusAlteon(adcIndex, vsList);
        writeVServiceStatusAlteon(adcIndex, vsList);
        writePoolMemberStatusAlteon(adcIndex, vsList);
        writegGroupStatusAlteon(adcIndex);
    }

    private void writeVServerStatusF5(Integer adcIndex, ArrayList<OBDtoAdcVServerF5> vsList) throws OBException
    {
        ArrayList<String> disabledList = new ArrayList<String>();
        ArrayList<String> availableList = new ArrayList<String>();
        ArrayList<String> unavailableList = new ArrayList<String>();
        // ArrayList<String> blockedList=new ArrayList<String>();

        // vs status 업데이트.
        for(OBDtoAdcVServerF5 vs : vsList)
        {
            if(vs.getStatus() == OBDefine.VS_STATUS.DISABLE)
                disabledList.add(vs.getIndex());
            else if(vs.getStatus() == OBDefine.VS_STATUS.AVAILABLE)
                availableList.add(vs.getIndex());
            else
                // if(vs.getStatus()==OBDefine.VS_STATUS.UNAVAILABLE)
                unavailableList.add(vs.getIndex());
        }

        String sqlText = "";
        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
            String indexList = "''"; // where-in empty 방지
            // available status
            for(String index : availableList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VSERVER \n"
                                + " SET STATUS=%d          \n"
                                + " WHERE INDEX IN ( %s ); \n" // where-in:empty string 불가, null 불가, OK
                        , OBDefine.VS_STATUS.AVAILABLE, indexList);
                db.executeUpdate(sqlText);
            }

            // unavailable status
            indexList = "''"; // where-in empty방지
            for(String index : unavailableList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VSERVER \n"
                                + " SET STATUS=%d          \n"
                                + " WHERE INDEX IN ( %s ); \n", // where-in:empty string 불가, null 불가, OK
                                OBDefine.VS_STATUS.UNAVAILABLE, indexList);
                db.executeUpdate(sqlText);
            }

            // disabled status
            indexList = "''"; // where-in empty 방지
            for(String index : disabledList)
            {
                if(!indexList.isEmpty())
                    indexList += ",";
                indexList += OBParser.sqlString(index);
            }
            if(!indexList.isEmpty())
            {
                sqlText =
                        String.format(" UPDATE TMP_SLB_VSERVER \n"
                                + " SET STATUS=%d          \n"
                                + " WHERE INDEX IN ( %s ); \n", // where-in:empty string 불가, null 불가, OK
                                OBDefine.VS_STATUS.DISABLE, indexList);
                db.executeUpdate(sqlText);
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
                db.closeDB();
        }
    }

    public void writeSlbStatusAllF5(Integer adcIndex, ArrayList<OBDtoAdcVServerF5> vsList) throws OBException
    {// vs, vsrv, poolmember의 status를 저장한다.
        writeVServerStatusF5(adcIndex, vsList);
        writePoolMemberStatusAllF5(adcIndex, vsList); //전체 member update true
    }
    public void writeSlbStatusPartialF5(Integer adcIndex, ArrayList<OBDtoAdcVServerF5> vsList) throws OBException
    {// vs, vsrv, poolmember의 status를 저장한다.
        writeVServerStatusF5(adcIndex, vsList);
        writePoolMemberStatusPartialF5(adcIndex, vsList); //전체 member update false
    }
    private void writePoolMemberStatusAllF5(Integer adcIndex, ArrayList<OBDtoAdcVServerF5> vsList) throws OBException
    {
        writePoolMemberStatusF5(adcIndex, vsList, true); //전체 vs 멤버 업데이트
    }
    private void writePoolMemberStatusPartialF5(Integer adcIndex, ArrayList<OBDtoAdcVServerF5> vsList) throws OBException
    {
        writePoolMemberStatusF5(adcIndex, vsList, false); //일부 vs 관련 멤버만 업데이트
    }
    private void writePoolMemberStatusF5(Integer adcIndex, ArrayList<OBDtoAdcVServerF5> vsList, boolean bUpdateAllVirtualServer) throws OBException
    {
        if(vsList == null || vsList.isEmpty())
        {
            return;
        }
        OBDatabase db = new OBDatabase();
        OBVServerDB vserverDB = new OBVServerDB();

        StringBuilder dataQuery = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
        // ADC 데이터 모두 삭제
        if(bUpdateAllVirtualServer==true)
        {
            vserverDB.delPoolmemberStatusAll(adcIndex, dataQuery);
        }
        else //특정 vs만 업데이트, 파라미터로 전달된 vs 목록이 전체가 아님
        {
            ArrayList<String> vsIndexList = new ArrayList<String>(); //삭제할 vs 인덱스 목록을 만듦
            for(OBDtoAdcVServerF5 vs: vsList)
            {
                vsIndexList.add(vs.getIndex());
            }
            vserverDB.delPoolmemberStatusPartial(adcIndex, vsIndexList, dataQuery);
        }

        String prefix = "";
        final String delimiter = ", ";
        try
        {
            db.openDB();

            // 데이터 추가를 위한 기본 쿼리 작성.
            dataQuery
                    .append(" INSERT INTO TMP_SLB_POOLMEMBER_STATUS ")
                    .append(" (INDEX, ADC_INDEX, VS_INDEX, VSRC_INDEX, POOLMEMBER_INDEX, MEMBER_STATUS) ")
                    .append(" VALUES ");
            int dataLength = dataQuery.length();
            for(OBDtoAdcVServerF5 vs : vsList)
            {
                OBDtoAdcPoolF5 pool = vs.getPool();
                if(pool == null)
                    continue;

                ArrayList<OBDtoAdcPoolMemberF5> memberList = pool.getMemberList();
                if(memberList == null || memberList.size() == 0)
                    continue;

                for(OBDtoAdcPoolMemberF5 member : memberList)
                {
                    String szVsIndex = OBCommon.makeVSIndexF5(adcIndex, vs.getName());
                    String szVSrcIndex = OBCommon.makePoolIndex(adcIndex, pool.getName());
                    String szPoolmemberIndex =
                            OBCommon.makePoolMemberIndexF5(adcIndex, pool.getName(), member.getIpAddress(), member.getPort());
                    String szIndex =
                            OBCommon.makePoolMemberStatusIndexF5(adcIndex, vs.getName(), vs.getServicePort(), 0,
                                    pool.getName(), member.getIpAddress(), member.getPort());

                    dataQuery
                            .append(prefix)
                            .append(" ( ")
                            .append(OBParser.sqlString(szIndex))
                            .append(delimiter)
                            .append(adcIndex)
                            .append(delimiter)
                            .append(OBParser.sqlString(szVsIndex))
                            .append(delimiter)
                            .append(OBParser.sqlString(szVSrcIndex))
                            .append(delimiter)
                            .append(OBParser.sqlString(szPoolmemberIndex))
                            .append(delimiter)
                            .append(member.getStatus())
                            .append(" ) ");

                    prefix = ", ";
                }
            }
            if(dataLength != dataQuery.length())
            {
                db.executeUpdate(dataQuery.toString());
            }
        }
        catch(SQLException e)
        {
            throw new OBException(OBException.ERRCODE_DB_QEURY, String.format("%s, sqlText:%s", e.getMessage(),
                    dataQuery.toString()));
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
    }

    // 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할 예정이다.
    public boolean writeAdcStatus(Integer adcIndex, Integer status) throws OBException
    {
        String sqlText = "";
        boolean bStatusChanged = false;
        OBDatabase db = new OBDatabase();

        try
        {
            db.openDB();
            // 현재 상태를 검출
            Timestamp lastDisconnTime = null;
            Integer lastStatus = OBDefine.ADC_STATUS.UNREACHABLE;

            sqlText =
                    String.format(" SELECT A.STATUS STATUS, B.DISCONN_TIME DISCONN_TIME FROM MNG_ADC      A    \n"
                            + " LEFT JOIN MNG_ADC_ADDITIONAL                      B    \n"
                            + " ON B.ADC_INDEX = A.INDEX                               \n"
                            + "WHERE A.INDEX = %d;                                     \n", adcIndex);
            ResultSet rs = db.executeQuery(sqlText);
            if(rs.next())
            {
                lastStatus = db.getInteger(rs, "STATUS");
                lastDisconnTime = db.getTimestamp(rs, "DISCONN_TIME");
            }

            sqlText =
                    String.format(
                            " UPDATE MNG_ADC SET      \n" + " STATUS=%d               \n" + " WHERE INDEX = %d;       \n",
                            status, adcIndex);
            db.executeUpdate(sqlText);
            if(status == OBDefine.ADC_STATUS.UNREACHABLE)
            {// 끊겨진 상태임.
                if(lastDisconnTime == null)
                {//
                    updateAdcDisconnTime(adcIndex);
                }
                else
                {
                    if(lastStatus == OBDefine.ADC_STATUS.REACHABLE)
                    {// 연결에서 끊김 상태로 변환되었을 경우에만 저장한다.
                        updateAdcDisconnTime(adcIndex);
                    }
                    else
                    {// 연속적으로 끊겨진 상태는 pass.
                    }
                }
            }
            else
            {// 현재가 연결 상태임. pass.
            }

            if(status.equals(lastStatus) == false) // ADC 상태가 바뀌었으면 바뀌었음을 리턴한다.
            {
                bStatusChanged = true;
            }
            else
            {
                bStatusChanged = false;
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
                db.closeDB();
        }
        return bStatusChanged;
    }
    
    // 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할 예정이다.
    public boolean writeAdcSlbScheduleStatus(Integer adcIndex, Integer status) throws OBException
    {
        String sqlText = "";
        boolean bStatusChanged = false;
        OBDatabase db = new OBDatabase();

        try
        {
            db.openDB();
            // 현재 상태를 검출
            sqlText =
                    String.format(
                            " UPDATE MNG_SLB_SCHEDULE SET               \n"
                          + " STATE=%d WHERE INDEX = %d;                \n",
                            status, adcIndex);
            db.executeUpdate(sqlText);
            
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
                db.closeDB();
        }
        return bStatusChanged;
    }

    // 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할 예정이다.
    private void updateAdcDisconnTime(Integer adcIndex) throws OBException
    {
        String sqlText = "";
        OBDatabase db = new OBDatabase();

        try
        {
            db.openDB();

            Timestamp now = OBDateTime.toTimestamp(OBDateTime.now());
            sqlText =
                    String.format(" UPDATE MNG_ADC_ADDITIONAL      \n"
                            + " SET                            \n"
                            + " DISCONN_TIME=%s                \n"
                            + " WHERE ADC_INDEX = %d;          \n", OBParser.sqlString(now), adcIndex);
            db.executeUpdate(sqlText);
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
    }

    // public static void main(String[] args)
    // {
    // OBDatabase db = new OBDatabase();
    // try
    // {
    // db.openDB();
    //
    // new OBAdcMonitorDB().writeAdcFaultLogPoolMemberUpDownAlteon(12, 1, "192.168.100.20:80", db);
    // }
    // catch(Exception e)
    // {
    // }
    // }

    // public static void main(String[] args)
    // {
    // OBDatabase db = new OBDatabase();
    // try
    // {
    // db.openDB();
    //
    // new OBAdcMonitorDB().writeAdcFaultLogVSUpDownAlteon(12, 1, "1:192.168.100.20", db);
    // }
    // catch(Exception e)
    // {
    // }
    // }

    // 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할 예정이다.
    public void writeAdcFaultLogVSUpDownF5(int adcIndex, HashMap<String, OBDtoVServerStatus> vsOldStatus,
            ArrayList<OBDtoAdcVServerF5> vsList) throws OBException
    {
        if(vsList.size() <= 0)
        {
            return;
        }
        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
            String adcName = new OBAdcManagementImpl().getAdcNameNew(adcIndex);
            OBDtoAlarmConfig alarmConfig = new OBAlarmImpl().getAlarmConfigurationWithAdcIndex(adcIndex);
            OBAlarmDB alarmDB = new OBAlarmDB();
            OBDtoVServerStatus oldStatus;
            OBDefineFault.TYPE faultType = null;
            String event;
            String eventEng;
            int status = OBDefineFault.STATUS_UNSOLVED;
            for(OBDtoAdcVServerF5 vs : vsList)
            {
                oldStatus = vsOldStatus.get(vs.getIndex());
                faultType = null;
                if(oldStatus != null) // 이전 상태가 없으면 장애로 치지 않는다.
                {
                    if(oldStatus.getStatus().equals(vs.getStatus()) == false) // 상태가 달라졌다.
                    {
                        if(vs.getStatus().equals(OBDefine.VS_STATUS.UNAVAILABLE)) // vs down됨
                        {
                            faultType = OBDefineFault.TYPE.VIRTUALSRV_DOWN;
                            status = OBDefineFault.STATUS_UNSOLVED;
                        }
                        else if(vs.getStatus().equals(OBDefine.VS_STATUS.AVAILABLE)) // vs up
                        {
                            faultType = OBDefineFault.TYPE.VIRTUALSRV_UP;
                            status = OBDefineFault.STATUS_SOLVED;
                        }

                        if(faultType != null)
                        {
                            event = String.format(faultType.getContent(), vs.getName(), vs.getvIP());
                            eventEng =
                                    String.format(faultType.getSyslogContent(), adcName, faultType.getLevel().getCaption(),
                                            vs.getvIP());
                            alarmDB.writeFaultLog(adcIndex, adcName, faultType, alarmConfig.getVirtualServerDown(), status,
                                    vs.getName(), vs.getIndex(), event, eventEng); // 장애 로그를 두개 쓴다. 로그, 통계용 데이터
                        }
                    }
                }
            }
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
                db.closeDB();
        }
    }

    // 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할 예정이다.
    public void updateAdcApplyTime(int adcIndex, Timestamp applyTime) throws OBException
    {
        String sqlText = "";

        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
            sqlText =
                    String.format("UPDATE MNG_ADC SET " + "APPLY_TIME=%s " + "WHERE INDEX = %d; ",
                            OBParser.sqlString(applyTime), adcIndex);
            db.executeUpdate(sqlText);
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
    }

      
    // 데몬 변경으로 인해 DBConnection Pool로 변경 할 코드이다. DBConnection Pool로 변경되면 위에 함수는 삭제 할 예정이다.
    public void writeAdcSystemInfo(int adcIndex, OBDtoAdcSystemInfo info) throws OBException
    {
        ResultSet rs;
        String sqlText = "";
        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();

            sqlText = String.format(" SELECT INDEX FROM MNG_ADC WHERE INDEX = %d; ", adcIndex);
            rs = db.executeQuery(sqlText);

            if(rs.next() == true) // 있는 장비에 대해서 시스템정보를 저장한다. 없는 장비 정보는 오지 않겠지만, 혹시라도 오면 조용히 무시한다.
            {
                sqlText =
                        String.format(" UPDATE MNG_ADC SET "
                                + " MODEL=%s, SW_VERSION=%s, HOST_NAME=%s, APPLY_TIME=%s, SAVE_TIME=%s, LAST_BOOT_TIME=%s "
                                + " WHERE INDEX = %d; ", OBParser.sqlString(info.getModel()),
                                OBParser.sqlString(info.getSwVersion()), OBParser.sqlString(info.getHostName()),
                                OBParser.sqlString(info.getLastApplyTime()), OBParser.sqlString(info.getLastSaveTime()),
                                OBParser.sqlString(info.getLastBootTime()),
                                // info.getStatus(),
                                adcIndex);
            }

            db.executeUpdate(sqlText);
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
                db.closeDB();
        }
    }
}
