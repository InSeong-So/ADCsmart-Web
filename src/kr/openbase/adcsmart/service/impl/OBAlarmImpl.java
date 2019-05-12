 package kr.openbase.adcsmart.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import kr.openbase.adcsmart.service.OBAlarm;
import kr.openbase.adcsmart.service.OBSystemAudit;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAlarmAction;
import kr.openbase.adcsmart.service.dto.OBDtoAlarmConfig;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoReadConfig;
import kr.openbase.adcsmart.service.snmp.OBSnmpTrap;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBNmsConfig;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSyslog;
import kr.openbase.adcsmart.service.utility.OBSyslogDefine;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAlarmImpl implements OBAlarm
{
    public static final int TYPE_FAULT      = 0;
    public static final int TYPE_WARN       = 1;
    public static final int STATUS_ABNORMAL = 0;
    public static final int STATUS_NORMAL   = 1;
    public static final int STATUS_WARN     = 1;

    public static final int ACTION_ENABLED  = 0x00000001;
    public static final int ACTION_SYSLOG   = 0x00000010;
    public static final int ACTION_EMAIL    = 0x00000020;
    public static final int ACTION_SMS      = 0x00000040;
    public static final int ACTION_SNMPTRAP = 0x00000080;

    public enum MASK
    {
        ENABLED(0x00000001), ACTION_SYSLOG(0x00000010), ACTION_EMAIL(0x00000020), ACTION_SMS(0x00000040), ACTION_SNMPTRAP(0x00000080), 
                INTERVAL_MIN(0x0000FF00), INTERVAL_HOUR(0x00FF0000);
        int mask;

        private MASK(int mask)
        {
            this.mask = mask;
        }

        public int getMask()
        {
            return mask;
        }

        public void setMask(int mask)
        {
            this.mask = mask;
        }
    }

    // ACTION필드 값 : web에서 받는 Action 설정값(DTO)을 내부 처리용으로 변환
    public int actions2Int(OBDtoAlarmAction actions)
    {
        if(actions == null)
            return 0;

        int ret = 0x00000000;

        if(actions.getEnable() == 1)
        {
            ret = ret | ACTION_ENABLED;
        }
        if(actions.getSyslog() == 1)
        {
            ret = ret | ACTION_SYSLOG;
        }
        if(actions.getEmail() == 1)
        {
            ret = ret | ACTION_EMAIL;
        }
        if(actions.getSms() == 1)
        {
            ret = ret | ACTION_SMS;
        }
        if(actions.getSnmptrap() == 1)
        {
            ret = ret | ACTION_SNMPTRAP;
        }
        if(actions.getIntervalValue() != null && actions.getIntervalValue() > 0)
        {
            if(actions.getIntervalUnit().equals("M"))
            {
                ret = ret | actions.getIntervalValue() << 8;
            }
            else if(actions.getIntervalUnit().equals("H"))
            {
                ret = ret | actions.getIntervalValue() << 16;
            }
        }

        return ret;
    }

    public void testActions2Int_Int2Actions()
    {
        OBDtoAlarmAction actions = new OBDtoAlarmAction(1, 1, 0, 0, 0, 100L, 60, "M");
        int res = actions2Int(actions);
        System.out.println(String.format("%x", res));
        actions = int2Actions(res);
        System.out.println(actions);
    }

    // ACTION필드 값 : DB의 Integer actions(mask 필드)을 외부 데이터로 바꿈
    public OBDtoAlarmAction int2Actions(int intActions)
    {
        OBDtoAlarmAction actions = new OBDtoAlarmAction();
        int interval = 0;
        if((intActions & MASK.ENABLED.mask) == ACTION_ENABLED)
        {
            actions.setEnable(1);
        }
        if((intActions & MASK.ACTION_SYSLOG.mask) == ACTION_SYSLOG)
        {
            actions.setSyslog(1);
        }
        if((intActions & MASK.ACTION_EMAIL.mask) == ACTION_EMAIL)
        {
            actions.setEmail(1);
        }
        if((intActions & MASK.ACTION_SMS.mask) == ACTION_SMS)
        {
            actions.setSms(1);
        }
        if((intActions & MASK.ACTION_SNMPTRAP.mask) == ACTION_SNMPTRAP)
        {
            actions.setSnmptrap(1);
        }
        if((interval = intActions & MASK.INTERVAL_HOUR.mask) > 0) // 시간 경고 주기가 있으면
        {
            actions.setIntervalValue(interval >> 16);
            actions.setIntervalUnit(OBDefine.TIME_UNIT_HOUR);
        }
        else if((interval = intActions & MASK.INTERVAL_MIN.mask) > 0) // 시간 경고 주기가 있으면
        {
            actions.setIntervalValue(interval >> 8);
            actions.setIntervalUnit(OBDefine.TIME_UNIT_MINUTE);
        }
        else
        // 기본값
        {
            actions.setIntervalValue(0);
            actions.setIntervalUnit(OBDefine.TIME_UNIT_MINUTE);
        }
        return actions;
    }

    public enum JOB
    {
        ADD, UPDATE
    };

    public static String[] ALARM_COLUMNS = new String[] {
                                         /* 0 */"OBJECT_INDEX",
                                         /* 1 */"CATEGORY",
                                         /* 2 */"ADC_DISCONNECT_ACTION",
                                         /* 3 */"ADC_CONNECT_ACTION",
                                         /* 4 */"VIRTUALSERVER_DOWN_ACTION",
                                         /* 5 */"VIRTUALSERVER_UP_ACTION",
                                         /* 6 */"POOLMEMBER_DOWN_ACTION",
                                         /* 7 */"POOLMEMBER_UP_ACTION",
                                         /* 8 */"LINK_DOWN_ACTION",
                                         /* 9 */"LINK_UP_ACTION",
                                         /* 10 */"ADC_BOOT_ACTION",
                                         /* 11 */"ADC_STANDBY_ACTION",
                                         /* 12 */"ADC_ACTIVE_ACTION",
                                         /* 13 */"ADC_CPU_ACTION",
                                         /* 14 */"ADC_CPU_VALUE",
                                         /* 15 */"ADC_MEM_ACTION",
                                         /* 16 */"ADC_MEM_VALUE",
                                         /* 17 */"ADC_SSL_TRANS_ACTION",
                                         /* 18 */"ADC_SSL_TRANS_VALUE",
                                         /* 19 */"ADC_CONN_ACTION",
                                         /* 20 */"ADC_CONN_VALUE",
                                         /* 19 */"ADC_CONN_LOW_ACTION",
                                         /* 20 */"ADC_CONN_LOW_VALUE",
                                         /* 21 */"ADC_UPTIME_ACTION",
                                         /* 22 */"ADC_UPTIME_VALUE",
                                         /* 23 */"ADC_PURCHASE_ACTION",
                                         /* 24 */"ADC_PURCHASE_VALUE",
                                         /* 25 */"ADC_SSLCERT_ACTION",
                                         /* 26 */"ADC_SSLCERT_VALUE",
                                         /* 27 */"ADC_MP_LIMIT_ACTION",
                                         /* 28 */"ADC_MP_LIMIT_VALUE",
                                         /* 29 */"ADC_SP_LIMIT_ACTION",
                                         /* 30 */"ADC_SP_LIMIT_VALUE",
                                         /* 31 */"INF_ERROR_ACTION",
                                         /* 32 */"INF_ERROR_VALUE",
                                         /* 33 */"INF_USAGE_LIMIT_ACTION",
                                         /* 34 */"INF_USAGE_LIMIT_VALUE",
                                         /* 35 */"INF_DUPLEX_ACTION",
                                         /* 36 */"INF_DUPLEX_VALUE",
                                         /* 37 */"INF_SPEED_ACTIN",
                                         /* 38 */"INF_SPEED_VALUE",
                                         /* 39 */"ADC_CONF_SYNC_FAIL_ACTION",
                                         /* 40 */"ADC_CONF_SYNC_FAIL_VALUE",
                                         /* 41 */"ADC_CONF_BACKUP_FAIL_ACTION",
                                         /* 42 */"ADC_CONF_BACKUP_FAIL_VALUE",
                                         /* 43 */"FAN_NOT_OPERATIONAL_ACTION",
                                         /* 44 */"FAN_NOT_OPERATIONAL_VALUE",
                                         /* 45 */"TEMP_TOO_HIGH_ACTION",
                                         /* 46 */"TEMP_TOO_HIGH_VALUE",
                                         /* 47 */"ONE_SUPPLY_USED_ACTION",
                                         /* 48 */"ONE_SUPPLY_USED_VALUE",
                                         /* 49 */"CPU_TEMP_TOO_HIGH_ACTION",
                                         /* 50 */"CPU_TEMP_TOO_HIGH_VALUE",
                                         /* 51 */"CPU_FAN_TOO_SLOW_ACTION",
                                         /* 52 */"CPU_FAN_TOO_SLOW_VALUE",
                                         /* 53 */"CPU_FAN_BAD_ACTION",
                                         /* 54 */"CPU_FAN_BAD_VALUE",
                                         /* 55 */"CHSS_TMP_TOO_HIGH_ACTION",
                                         /* 56 */"CHSS_TMP_TOO_HIGH_VALUE",
                                         /* 57 */"CHSS_FAN_BAD_ACTION",
                                         /* 58 */"CHSS_FAN_BAD_VALUE",
                                         /* 59 */"CHSS_SUPPLY_BAD_ACTION",
                                         /* 60 */"CHSS_SUPPLY_BAD_VALUE",
                                         /* 61 */"UNIT_GOING_ACTIVE_ACTION",
                                         /* 62 */"UNIT_GOING_ACTIVE_VALUE",
                                         /* 63 */"UNIT_GOING_STANDBY_ACTION",
                                         /* 64 */"UNIT_GOING_STANDBY_VALUE",
                                         /* 65 */"BLOCK_DDOS_ACTION",
                                         /* 66 */"BLOCK_DDOS_VALUE",
                                         /* 67 */"VOLTAGE_TOO_HIGH_ACTION",
                                         /* 68 */"VALTAGE_TOO_HIGH_VALUE",
                                         /* 69 */"FAN_TOO_SLOW_ACTION",
                                         /* 70 */"FAN_TOO_SLOW_VALUE",
                                         /* 71 */"RESP_TIME_TOO_LONG_ACTION",
                                         /* 72 */"RESP_TIME_TOO_LONG_VALUE",
                                         /* 73 */"REDUNDANCY_FALUT_ACTION",
                                         /* 74 */"REDUNDANCY_FALUT_VALUE",
                                         /* 75 */"VRRP_COLLISION_ACTION",
                                         /* 76 */"GATEWAY_FAIL_DOWN_ACTION",
                                         /* 77 */"GATEWAY_FAIL_UP_ACTION",
                                         /* 78 */"ADC_FILTERSESSION_ACTION",
                                         /* 79 */"ADC_FILTERSESSION_VALUE",
                                         /* 80 */"ADC_TYPE" };

    @Override
    public OBDtoAlarmConfig getAlarmConfiguration(OBDtoADCObject object, Integer accountIndex) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. object:", object));
        // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "requested object = " + object);

        ArrayList<OBDtoAlarmConfig> configList = new ArrayList<OBDtoAlarmConfig>();
        try
        {
            configList = getAlarmConfiguration(object);
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
        // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("category %d, index %d, configlevel %d, type %d ",
        // configList.get(0).getObject().getCategory(),
        // configList.get(0).getObject().getIndex(),
        // configList.get(0).getConfigLevel(),
        // configList.get(0).getAdcType())
        // );

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
        return configList.get(0);
    }

    @Override
    // #3926-3 #6: 14.07.23 sw.jung 경보 감사로그 추가를 위해 setAlarmConfiguration 메소드 이원화(기존 메소드는 private으로)
    public void setAlarmConfiguration(OBDtoAlarmConfig alarmConfig, OBDtoExtraInfo extraInfo) throws OBException
    {
        try
        {
            setAlarmConfiguration(alarmConfig);
            new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
                    OBSystemAudit.AUDIT_ALARM_UPDATE_SUCCESS, extraInfo.getClientIPAddress());
        }
        catch(Exception e)
        {
            new OBSystemAuditImpl().writeLog(extraInfo.getAccountIndex(), extraInfo.getClientIPAddress(),
                    OBSystemAudit.AUDIT_ALARM_UPDATE_FAIL, extraInfo.getClientIPAddress());
            throw e;
        }
    }

    private void setAlarmConfiguration(OBDtoAlarmConfig alarmConfig) throws OBException
    {
        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();

            // 알람 설정이 있는지 확인한다.
            if(isAlarmConfigExisting(alarmConfig.getObject().getCategory(), alarmConfig.getObject().getIndex(),
                    alarmConfig.getAdcType(), db)) // 있으면 업데이트
            {
                doAlarmConfiguration(alarmConfig, JOB.UPDATE, db);
            }
            else
            // 없으면 추가
            {
                doAlarmConfiguration(alarmConfig, JOB.ADD, db);
            }
            setAlarmLastUpdateTime(db);
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

    @Override
    public void initAlarmConfiguration(OBDtoADCObject object) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. object:%s", object));

        // 경보가 이미 있으면(초기화하려고 버튼을 누름)업데이트하고, 없으면 새로 만든다. 없을 때 새로 만드는 것은 패치를 염두에 둔 것이다.
        try
        {
            // 기본 설정을 읽는다.
            OBDtoADCObject initObject = new OBDtoADCObject(-1, -1, -1);
            ArrayList<OBDtoAlarmConfig> config = getAlarmConfiguration(initObject);

            // config를 object에 맞게 바꾼다.
            config.get(0).getObject().setCategory(object.getCategory());
            config.get(0).setConfigLevel(object.getCategory()); // 자기 것을 초기화하는 것이므로 category와 똑같다.
            config.get(0).getObject().setIndex(object.getIndex());
            config.get(0).setAdcType(object.getVendor());
            setAlarmConfiguration(config.get(0));
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }

//    public void initAlarmConfiguration(Integer adcIndex) throws OBException // 내부에서는 object로 돌릴 필요가 없기 때문에 만든 함수
//    {
//        OBDtoADCObject object = new OBDtoADCObject(OBDtoADCObject.CATEGORY_ADC, adcIndex, 0);
//        initAlarmConfiguration(object);
//    }

    public void addAdcAlarmConfiguration(Integer adcIndex, Integer adcType) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));

        OBAdcManagementImpl adcManager = new OBAdcManagementImpl();
        try
        {
            // ADC 설정이 있는지 index로 확인한다. ADC가 없으면 그만둔다.
            if(adcManager.isExistAdcIndex(adcIndex) == false)
            {
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);
            }
            // 전체 설정을 읽는다.
            if(adcType.equals(OBDefine.ADC_TYPE_PIOLINK_UNKNOWN)) // PIOLINK는 연결이 안 됐을 때 TYPE을 알 수없는데, PAS와 PASK 항목이 같으므로 PAS로 초기설정한다
            {
                adcType = OBDefine.ADC_TYPE_PIOLINK_PAS;
            }
            OBDtoADCObject object = new OBDtoADCObject(OBDtoADCObject.CATEGORY_ALL, 0, adcType);
            ArrayList<OBDtoAlarmConfig> config = getAlarmConfiguration(object);
            // config를 ADC것으로 바꾼다.
            config.get(0).getObject().setCategory(OBDtoADCObject.CATEGORY_ADC);
            config.get(0).setConfigLevel(OBDtoADCObject.CATEGORY_ALL);
            config.get(0).getObject().setIndex(adcIndex);
            config.get(0).setAdcType(adcType);
            setAlarmConfiguration(config.get(0)); // global을 내 설정으로 바꿔서 설정한다.
        }
        catch(OBException e)
        {
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
                    String.format("OBException. adcIndex:%d, message:%s", adcIndex, e.getErrorMessage()));
            throw e;
        }
        catch(Exception e)
        {
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG,
                    String.format("Exception. adcIndex:%d, message:%s", adcIndex, e.getMessage()));
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }

    public void addGroupAlarmConfiguration(Integer groupIndex) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. groupIndex:%d", groupIndex));

        OBAdcManagementImpl adcManager = new OBAdcManagementImpl();
        try
        {
            // group 설정이 있는지 index로 확인한다. ADC가 없으면 그만둔다.
            if(adcManager.isExistGroupIndex(groupIndex) == false)
            {
                throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA);// 경보를 설정할 대상 ADC 없음
            }
            // 전체 설정 중 F5를 읽는다.
            OBDtoADCObject object = new OBDtoADCObject(OBDtoADCObject.CATEGORY_ALL, 0, OBDefine.ADC_TYPE_F5);
            ArrayList<OBDtoAlarmConfig> config = getAlarmConfiguration(object); // F5 전체 설정 읽어오기
            // config를 group것으로 바꾼다.
            config.get(0).getObject().setCategory(OBDtoADCObject.CATEGORY_GROUP);
            config.get(0).setConfigLevel(OBDtoADCObject.CATEGORY_GROUP);
            config.get(0).getObject().setIndex(groupIndex);
            config.get(0).setAdcType(OBDefine.ADC_TYPE_F5);
            setAlarmConfiguration(config.get(0)); // global을 내 설정으로 바꿔서 설정한다.

            // 전체 설정 중 ALTEON을 읽는다.
            object = new OBDtoADCObject(OBDtoADCObject.CATEGORY_ALL, 0, OBDefine.ADC_TYPE_ALTEON);
            config = getAlarmConfiguration(object); // 전체 설정 읽어오기
            // config를 group것으로 바꾼다.
            config.get(0).getObject().setCategory(OBDtoADCObject.CATEGORY_GROUP);
            config.get(0).setConfigLevel(OBDtoADCObject.CATEGORY_GROUP);
            config.get(0).getObject().setIndex(groupIndex);
            config.get(0).setAdcType(OBDefine.ADC_TYPE_ALTEON);
            setAlarmConfiguration(config.get(0)); // global을 내 설정으로 바꿔서 설정한다.

            // 전체 설정 중 PIOLINK_PAS를 읽는다.
            object = new OBDtoADCObject(OBDtoADCObject.CATEGORY_ALL, 0, OBDefine.ADC_TYPE_PIOLINK_PAS);
            config = getAlarmConfiguration(object); // 전체 설정 읽어오기
            // config를 group것으로 바꾼다.
            config.get(0).getObject().setCategory(OBDtoADCObject.CATEGORY_GROUP);
            config.get(0).setConfigLevel(OBDtoADCObject.CATEGORY_GROUP);
            config.get(0).getObject().setIndex(groupIndex);
            config.get(0).setAdcType(OBDefine.ADC_TYPE_PIOLINK_PAS);
            setAlarmConfiguration(config.get(0)); // global을 내 설정으로 바꿔서 설정한다.

            // 전체 설정 중 PIOLINK_PASK를 읽는다.
            object = new OBDtoADCObject(OBDtoADCObject.CATEGORY_ALL, 0, OBDefine.ADC_TYPE_PIOLINK_PASK);
            config = getAlarmConfiguration(object); // 전체 설정 읽어오기
            // config를 group것으로 바꾼다.
            config.get(0).getObject().setCategory(OBDtoADCObject.CATEGORY_GROUP);
            config.get(0).setConfigLevel(OBDtoADCObject.CATEGORY_GROUP);
            config.get(0).getObject().setIndex(groupIndex);
            config.get(0).setAdcType(OBDefine.ADC_TYPE_PIOLINK_PASK);
            setAlarmConfiguration(config.get(0)); // global을 내 설정으로 바꿔서 설정한다.
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }

    /**
     * ADC의 소속 그룹이 바뀌었을 때 group 설정을 ADC가 쓰고 있다면 바꿔놓는다.
     * 
     * @param groupIndex
     * @param db
     * @throws OBException
     */
    public void changeAdcGroupAlarm(Integer adcIndex, Integer adcType, Integer newGroupIndex) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));

        try
        {
            // adc의 설정을 읽는다.
            OBDtoADCObject object = new OBDtoADCObject(OBDtoADCObject.CATEGORY_ADC, adcIndex, 0); // ADC일 때는 TYPE은 상관이 없으므로 0을 준다.(세번째 인자)
            ArrayList<OBDtoAlarmConfig> config = getAlarmConfiguration(object);

            if(config.get(0).getConfigLevel().equals(OBDtoADCObject.CATEGORY_GROUP)) // 그룹의 설정을 쓰고 있다면
            { // 새 그룹 설정을 읽는다.
                OBDtoADCObject groupObject = new OBDtoADCObject(OBDtoADCObject.CATEGORY_GROUP, newGroupIndex, adcType);
                ArrayList<OBDtoAlarmConfig> groupConfig = getAlarmConfiguration(groupObject);

                // GROUP CONFIG를 ADC에 입힌다. CONFIG LEVEL은 GROUP이다.
                groupConfig.get(0).getObject().setCategory(OBDtoADCObject.CATEGORY_ADC);
                groupConfig.get(0).setConfigLevel(OBDtoADCObject.CATEGORY_GROUP);
                groupConfig.get(0).getObject().setIndex(adcIndex);
                groupConfig.get(0).setAdcType(adcType);
                setAlarmConfiguration(config.get(0)); // 설정한다.
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
    }

    public void delAdcAlarmConfiguration(Integer adcIndex) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. adcIndex:%d", adcIndex));

        OBDatabase db = new OBDatabase();
        String sqlText = "";
        try
        {
            db.openDB();
            sqlText =
                    String.format(" DELETE FROM MNG_ALARM " + " WHERE CATEGORY = %d AND OBJECT_INDEX = %d ",
                            OBDtoADCObject.CATEGORY_ADC, adcIndex);
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

    public void delGroupAlarmConfiguration(Integer groupIndex) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start. groupIndex:%d", groupIndex));

        String sqlText = "";
        final OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();

            sqlText =
                    String.format(" DELETE FROM MNG_ALARM " + " WHERE CATEGORY = %d AND OBJECT_INDEX = %d ",
                            OBDtoADCObject.CATEGORY_GROUP, groupIndex);
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

    // 유효 ADC의 경보 설정을 모두 읽는다.
    public ArrayList<OBDtoAlarmConfig> getAlarmConfigurationAll() throws OBException
    {
        String sqlText = "";
        int iActions = 0x00000000;
        ArrayList<OBDtoAlarmConfig> retVal = new ArrayList<OBDtoAlarmConfig>(); // collection에 대해서 null을 리턴하지 않기로 했으므로 null로 초기화 하지 않는다.

        sqlText =
                String.format(" SELECT * FROM MNG_ALARM "
                        + " WHERE CATEGORY = %d AND OBJECT_INDEX IN (SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d )",
                        OBDtoADCObject.CATEGORY_ADC, OBDefine.STATUS_AVAILABLE);

        final OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
            ResultSet rs = db.executeQuery(sqlText);
            while(rs.next())
            {
                OBDtoAlarmConfig config = new OBDtoAlarmConfig();
                config.setObject(new OBDtoADCObject());
                config.getObject().setIndex(db.getInteger(rs, "ADC_INDEX"));

                iActions = db.getInteger(rs, "ADC_DISCONNECT_ACTION");
                config.setAdcDisconnect(int2Actions(iActions));

                // iActions = db.getInteger(rs, "ADC_CONNECT_ACTION");
                // config.setAdcConnectAction(int2Actions(iActions));

                iActions = db.getInteger(rs, "VIRTUALSERVER_DOWN_ACTION");
                config.setVirtualServerDown(int2Actions(iActions));

                // iActions = db.getInteger(rs, "VIRTUALSERVER_UP_ACTION");
                // config.setVirtualServerUpAction(int2Actions(iActions));

                iActions = db.getInteger(rs, "POOLMEMBER_DOWN_ACTION");
                config.setPoolMemberDown(int2Actions(iActions));

                // iActions = db.getInteger(rs, "POOLMEMBER_UP_ACTION");
                // config.setPoolMemberUpAction(int2Actions(iActions));

                iActions = db.getInteger(rs, "LINK_DOWN_ACTION");
                config.setInterfaceDown(int2Actions(iActions));

                // iActions = db.getInteger(rs, "LINK_UP_ACTION");
                // config.setLinkUpAction(int2Actions(iActions));

                iActions = db.getInteger(rs, "ADC_BOOT_ACTION");
                config.setAdcBooting(int2Actions(iActions));

                iActions = db.getInteger(rs, "ADC_STANDBY_ACTION");
                config.setAdcActiveToStandby(int2Actions(iActions));

                iActions = db.getInteger(rs, "ADC_ACTIVE_ACTION");
                config.setAdcStandbyToActive(int2Actions(iActions));

                iActions = db.getInteger(rs, "ADC_CPU_ACTION");
                config.setAdcCpuLimit(int2Actions(iActions));
                config.getAdcCpuLimit().setThreshold(db.getLong(rs, "ADC_CPU_VALUE"));

                iActions = db.getInteger(rs, "ADC_MEM_ACTION");
                config.setAdcMemLimit(int2Actions(iActions));
                config.getAdcMemLimit().setThreshold(db.getLong(rs, "ADC_MEM_VALUE"));

                iActions = db.getInteger(rs, "ADC_SSL_TRANS_ACTION");
                config.setAdcSslTransaction(int2Actions(iActions));
                config.getAdcSslTransaction().setThreshold(db.getLong(rs, "ADC_SSL_TRANS_VALUE"));

                iActions = db.getInteger(rs, "ADC_CONN_ACTION");
                config.setConnectionLimitHigh(int2Actions(iActions));
                config.getConnectionLimitHigh().setThreshold(db.getLong(rs, "ADC_CONN_VALUE"));

                iActions = db.getInteger(rs, "ADC_CONN_LOW_ACTION_");
                config.setConnectionLimitLow(int2Actions(iActions));
                config.getConnectionLimitLow().setThreshold(db.getLong(rs, "ADC_CONN_LOW_VALUE"));

                iActions = db.getInteger(rs, "ADC_UPTIME_ACTION");
                config.setAdcUptime(int2Actions(iActions));
                config.getAdcUptime().setThreshold(db.getLong(rs, "ADC_UPTIME_VALUE"));

                iActions = db.getInteger(rs, "ADC_PURCHASE_ACTION");
                config.setAdcPurchase(int2Actions(iActions));
                config.getAdcPurchase().setThreshold(db.getLong(rs, "ADC_PURCHASE_VALUE"));

                iActions = db.getInteger(rs, "ADC_SSLCERT_ACTION");
                config.setAdcSslcert(int2Actions(iActions));
                config.getAdcSslcert().setThreshold(db.getLong(rs, "ADC_SSLCERT_VALUE"));

                // new item.
                iActions = db.getInteger(rs, "ADC_MP_LIMIT_ACTION");
                config.setAdcMPLimit(int2Actions(iActions));
                config.getAdcMPLimit().setThreshold(db.getLong(rs, "ADC_MP_LIMIT_VALUE"));

                iActions = db.getInteger(rs, "ADC_SP_LIMIT_ACTION");
                config.setAdcSPLimit(int2Actions(iActions));
                config.getAdcSPLimit().setThreshold(db.getLong(rs, "ADC_SP_LIMIT_VALUE"));

                iActions = db.getInteger(rs, "INF_ERROR_ACTION");
                config.setInterfaceError(int2Actions(iActions));
                config.getInterfaceError().setThreshold(db.getLong(rs, "INF_ERROR_VALUE"));

                iActions = db.getInteger(rs, "INF_USAGE_LIMIT_ACTION");
                config.setInterfaceUsageLimit(int2Actions(iActions));
                config.getInterfaceUsageLimit().setThreshold(db.getLong(rs, "INF_USAGE_LIMIT_VALUE"));

                iActions = db.getInteger(rs, "INF_DUPLEX_ACTION");
                config.setInterfaceDuplexChange(int2Actions(iActions));
                config.getInterfaceDuplexChange().setThreshold(db.getLong(rs, "INF_DUPLEX_VALUE"));

                iActions = db.getInteger(rs, "INF_SPEED_ACTIN");
                config.setInterfaceSpeedChange(int2Actions(iActions));
                config.getInterfaceSpeedChange().setThreshold(db.getLong(rs, "INF_SPEED_VALUE"));

                iActions = db.getInteger(rs, "ADC_CONF_SYNC_FAIL_ACTION");
                config.setAdcConfSyncFailure(int2Actions(iActions));
                config.getAdcConfSyncFailure().setThreshold(db.getLong(rs, "ADC_CONF_SYNC_FAIL_VALUE"));

                iActions = db.getInteger(rs, "ADC_CONF_BACKUP_FAIL_ACTION");
                config.setAdcConfBackupFailure(int2Actions(iActions));
                config.getAdcConfBackupFailure().setThreshold(db.getLong(rs, "ADC_CONF_BACKUP_FAIL_VALUE"));

                iActions = db.getInteger(rs, "FAN_NOT_OPERATIONAL_ACTION");
                config.setFanNotOperational(int2Actions(iActions));
                config.getFanNotOperational().setThreshold(db.getLong(rs, "FAN_NOT_OPERATIONAL_VALUE"));

                iActions = db.getInteger(rs, "TEMP_TOO_HIGH_ACTION"); // TODO: low
                config.setTemperatureTooHigh(int2Actions(iActions));
                config.getTemperatureTooHigh().setThreshold(db.getLong(rs, "TEMP_TOO_HIGH_VALUE"));

                iActions = db.getInteger(rs, "ONE_SUPPLY_USED_ACTION");
                config.setOnlyOnePowerSupply(int2Actions(iActions));
                config.getOnlyOnePowerSupply().setThreshold(db.getLong(rs, "ONE_SUPPLY_USED_VALUE"));

                iActions = db.getInteger(rs, "CPU_TEMP_TOO_HIGH_ACTION");
                config.setCpuTempTooHigh(int2Actions(iActions));
                config.getCpuTempTooHigh().setThreshold(db.getLong(rs, "CPU_TEMP_TOO_HIGH_VALUE"));

                iActions = db.getInteger(rs, "CPU_FAN_TOO_SLOW_ACTION");
                config.setCpuFanTooSlow(int2Actions(iActions));
                config.getCpuFanTooSlow().setThreshold(db.getLong(rs, "CPU_FAN_TOO_SLOW_VALUE"));

                iActions = db.getInteger(rs, "CPU_FAN_BAD_ACTION");
                config.setCpuFanBad(int2Actions(iActions));
                config.getCpuFanBad().setThreshold(db.getLong(rs, "CPU_FAN_BAD_VALUE"));

                iActions = db.getInteger(rs, "CHSS_TMP_TOO_HIGH_ACTION");
                config.setChassisTempTooHigh(int2Actions(iActions));
                config.getChassisTempTooHigh().setThreshold(db.getLong(rs, "CHSS_TMP_TOO_HIGH_VALUE"));

                iActions = db.getInteger(rs, "CHSS_FAN_BAD_ACTION");
                config.setChassisFanBad(int2Actions(iActions));
                config.getChassisFanBad().setThreshold(db.getLong(rs, "CHSS_FAN_BAD_VALUE"));

                iActions = db.getInteger(rs, "CHSS_SUPPLY_BAD_ACTION");
                config.setChassisPowerSupplyBad(int2Actions(iActions));
                config.getChassisPowerSupplyBad().setThreshold(db.getLong(rs, "CHSS_SUPPLY_BAD_VALUE"));

                iActions = db.getInteger(rs, "UNIT_GOING_ACTIVE_ACTION");
                config.setUnitGoingActive(int2Actions(iActions));
                config.getUnitGoingActive().setThreshold(db.getLong(rs, "UNIT_GOING_ACTIVE_VALUE"));

                iActions = db.getInteger(rs, "UNIT_GOING_STANDBY_ACTION");
                config.setUnitGoingStandby(int2Actions(iActions));
                config.getUnitGoingStandby().setThreshold(db.getLong(rs, "UNIT_GOING_STANDBY_VALUE"));

                iActions = db.getInteger(rs, "BLOCK_DDOS_ACTION");
                config.setBlockDDoS(int2Actions(iActions));
                config.getBlockDDoS().setThreshold(db.getLong(rs, "BLOCK_DDOS_VALUE"));

                iActions = db.getInteger(rs, "VOLTAGE_TOO_HIGH_ACTION");
                config.setVoltageTooHigh(int2Actions(iActions));
                config.getVoltageTooHigh().setThreshold(db.getLong(rs, "VALTAGE_TOO_HIGH_VALUE"));

                iActions = db.getInteger(rs, "FAN_TOO_SLOW_ACTION");
                config.setChassisFanTooSlow(int2Actions(iActions));
                config.getChassisFanTooSlow().setThreshold(db.getLong(rs, "FAN_TOO_SLOW_VALUE"));

                iActions = db.getInteger(rs, "RESP_TIME_TOO_LONG_ACTION");
                config.setResponseTime(int2Actions(iActions));
                config.getResponseTime().setThreshold(db.getLong(rs, "RESP_TIME_TOO_LONG_VALUE"));

                iActions = db.getInteger(rs, "REDUNDANCY_FALUT_ACTION");
                config.setRedundancyCheck(int2Actions(iActions));
                config.getRedundancyCheck().setThreshold(db.getLong(rs, "REDUNDANCY_FALUT_VALUE"));

                iActions = db.getInteger(rs, "VRRP_COLLISION_ACTION");// vrrp/vrid 충돌 감지용. bwpark. 2014.04.11
                config.setVrrpCollision(int2Actions(iActions));

                iActions = db.getInteger(rs, "GATEWAY_FAIL_DOWN_ACTION");
                config.setGatewayFailDown(int2Actions(iActions));

                retVal.add(config);
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
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
        return retVal;
    }

    /**
     * alarmconfig를 가져온다.
     * 
     * @param adcIndex
     *            : 특정 ADC의 alarm 설정을 뽑는다. adcIndex null이면 유효 ADC모두의 설정을 리턴한다.
     * @param db
     * @throws OBException
     */
    public ArrayList<OBDtoAlarmConfig> getAlarmConfiguration(OBDtoADCObject object) throws OBException
    {
        String sqlText = "";

        if(object == null)
        {
        }

        Integer category = object.getCategory();

        if(category != null && (category.equals(OBDtoADCObject.CATEGORY_ALL) || category.equals(OBDtoADCObject.CATEGORY_GROUP)))
        {
            sqlText =
                    String.format(" SELECT * FROM MNG_ALARM WHERE CATEGORY = %d AND OBJECT_INDEX = %d AND ADC_TYPE = %d ",
                            category, object.getIndex(), object.getVendor());
        }
        else if(category != null && category.equals(OBDtoADCObject.CATEGORY_ADC) == true)
        {
            if(object.getIndex() != null)
            {
                sqlText =
                        String.format(" SELECT * FROM MNG_ALARM WHERE CATEGORY = %d AND OBJECT_INDEX = %d", category,
                                object.getIndex());
            }
        }
        try
        {
            ArrayList<OBDtoAlarmConfig> configs = getAlarmConfigurationFromDB(sqlText);

            if(configs.size() == 0) // 처음에 설정이 없으면 추가
            {
                if(category.equals(OBDtoADCObject.CATEGORY_ADC) == true)
                {
                    // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "config not found / add ADC config / object = " + object);
                    addAdcAlarmConfiguration(object.getIndex(), new OBAdcManagementImpl()
                            .getAdcInfo(object.getIndex())
                            .getAdcType());
                    configs = getAlarmConfigurationFromDB(sqlText);
                }
                else if(category.equals(OBDtoADCObject.CATEGORY_GROUP) == true)
                {
                    // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "config not found / add group config / object = " + object);
                    addGroupAlarmConfiguration(object.getIndex());
                    configs = getAlarmConfigurationFromDB(sqlText);
                }
            }
            return configs;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }

    private ArrayList<OBDtoAlarmConfig> getAlarmConfigurationFromDB(String sqlText) throws OBException
    {
        int iActions = 0x00000000;
        ArrayList<OBDtoAlarmConfig> ret = new ArrayList<OBDtoAlarmConfig>();
        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();

            ResultSet rs = db.executeQuery(sqlText);
            while(rs.next())
            {
                OBDtoAlarmConfig config = new OBDtoAlarmConfig();
                config.setObject(new OBDtoADCObject());
                config.getObject().setCategory(db.getInteger(rs, "CATEGORY"));
                config.getObject().setIndex(db.getInteger(rs, "OBJECT_INDEX"));
                config.getObject().setVendor(db.getInteger(rs, "ADC_TYPE"));
                config.setConfigLevel(db.getInteger(rs, "CONFIG_LEVEL"));
                config.setAdcType(db.getInteger(rs, "ADC_TYPE"));

                iActions = db.getInteger(rs, "ADC_DISCONNECT_ACTION");
                config.setAdcDisconnect(int2Actions(iActions));

                iActions = db.getInteger(rs, "VIRTUALSERVER_UP_ACTION");
                config.setVirtualServerUp(int2Actions(iActions));

                iActions = db.getInteger(rs, "VIRTUALSERVER_DOWN_ACTION");
                config.setVirtualServerDown(int2Actions(iActions));

                iActions = db.getInteger(rs, "POOLMEMBER_UP_ACTION");
                config.setPoolMemberUp(int2Actions(iActions));

                iActions = db.getInteger(rs, "POOLMEMBER_DOWN_ACTION");
                config.setPoolMemberDown(int2Actions(iActions));

                iActions = db.getInteger(rs, "LINK_Up_ACTION");
                config.setInterfaceUp(int2Actions(iActions));

                iActions = db.getInteger(rs, "LINK_DOWN_ACTION");
                config.setInterfaceDown(int2Actions(iActions));

                iActions = db.getInteger(rs, "ADC_BOOT_ACTION");
                config.setAdcBooting(int2Actions(iActions));

                iActions = db.getInteger(rs, "ADC_STANDBY_ACTION");
                config.setAdcActiveToStandby(int2Actions(iActions));

                iActions = db.getInteger(rs, "ADC_ACTIVE_ACTION");
                config.setAdcStandbyToActive(int2Actions(iActions));

                iActions = db.getInteger(rs, "ADC_CPU_ACTION");
                config.setAdcCpuLimit(int2Actions(iActions));
                config.getAdcCpuLimit().setThreshold(db.getLong(rs, "ADC_CPU_VALUE"));

                iActions = db.getInteger(rs, "ADC_MEM_ACTION");
                config.setAdcMemLimit(int2Actions(iActions));
                config.getAdcMemLimit().setThreshold(db.getLong(rs, "ADC_MEM_VALUE"));

                iActions = db.getInteger(rs, "ADC_SSL_TRANS_ACTION");
                config.setAdcSslTransaction(int2Actions(iActions));
                config.getAdcSslTransaction().setThreshold(db.getLong(rs, "ADC_SSL_TRANS_VALUE"));

                iActions = db.getInteger(rs, "ADC_CONN_ACTION");
                config.setConnectionLimitHigh(int2Actions(iActions));
                config.getConnectionLimitHigh().setThreshold(db.getLong(rs, "ADC_CONN_VALUE"));

                iActions = db.getInteger(rs, "ADC_CONN_LOW_ACTION");
                config.setConnectionLimitLow(int2Actions(iActions));
                config.getConnectionLimitLow().setThreshold(db.getLong(rs, "ADC_CONN_LOW_VALUE"));

                iActions = db.getInteger(rs, "ADC_UPTIME_ACTION");
                config.setAdcUptime(int2Actions(iActions));
                config.getAdcUptime().setThreshold(db.getLong(rs, "ADC_UPTIME_VALUE"));

                iActions = db.getInteger(rs, "ADC_PURCHASE_ACTION");
                config.setAdcPurchase(int2Actions(iActions));
                config.getAdcPurchase().setThreshold(db.getLong(rs, "ADC_PURCHASE_VALUE"));

                iActions = db.getInteger(rs, "ADC_SSLCERT_ACTION");
                config.setAdcSslcert(int2Actions(iActions));
                config.getAdcSslcert().setThreshold(db.getLong(rs, "ADC_SSLCERT_VALUE"));

                // new item.
                iActions = db.getInteger(rs, "ADC_MP_LIMIT_ACTION");
                config.setAdcMPLimit(int2Actions(iActions));
                config.getAdcMPLimit().setThreshold(db.getLong(rs, "ADC_MP_LIMIT_VALUE"));

                iActions = db.getInteger(rs, "ADC_SP_LIMIT_ACTION");
                config.setAdcSPLimit(int2Actions(iActions));
                config.getAdcSPLimit().setThreshold(db.getLong(rs, "ADC_SP_LIMIT_VALUE"));

                iActions = db.getInteger(rs, "INF_ERROR_ACTION");
                config.setInterfaceError(int2Actions(iActions));
                config.getInterfaceError().setThreshold(db.getLong(rs, "INF_ERROR_VALUE"));

                iActions = db.getInteger(rs, "INF_USAGE_LIMIT_ACTION");
                config.setInterfaceUsageLimit(int2Actions(iActions));
                config.getInterfaceUsageLimit().setThreshold(db.getLong(rs, "INF_USAGE_LIMIT_VALUE"));

                iActions = db.getInteger(rs, "INF_DUPLEX_ACTION");
                config.setInterfaceDuplexChange(int2Actions(iActions));
                config.getInterfaceDuplexChange().setThreshold(db.getLong(rs, "INF_DUPLEX_VALUE"));

                iActions = db.getInteger(rs, "INF_SPEED_ACTIN");
                config.setInterfaceSpeedChange(int2Actions(iActions));
                config.getInterfaceSpeedChange().setThreshold(db.getLong(rs, "INF_SPEED_VALUE"));

                iActions = db.getInteger(rs, "ADC_CONF_SYNC_FAIL_ACTION");
                config.setAdcConfSyncFailure(int2Actions(iActions));
                config.getAdcConfSyncFailure().setThreshold(db.getLong(rs, "ADC_CONF_SYNC_FAIL_VALUE"));

                iActions = db.getInteger(rs, "ADC_CONF_BACKUP_FAIL_ACTION");
                config.setAdcConfBackupFailure(int2Actions(iActions));
                config.getAdcConfBackupFailure().setThreshold(db.getLong(rs, "ADC_CONF_BACKUP_FAIL_VALUE"));

                iActions = db.getInteger(rs, "FAN_NOT_OPERATIONAL_ACTION");
                config.setFanNotOperational(int2Actions(iActions));
                config.getFanNotOperational().setThreshold(db.getLong(rs, "FAN_NOT_OPERATIONAL_VALUE"));

                iActions = db.getInteger(rs, "TEMP_TOO_HIGH_ACTION"); // TODO: low
                config.setTemperatureTooHigh(int2Actions(iActions));
                config.getTemperatureTooHigh().setThreshold(db.getLong(rs, "TEMP_TOO_HIGH_VALUE"));

                iActions = db.getInteger(rs, "ONE_SUPPLY_USED_ACTION");
                config.setOnlyOnePowerSupply(int2Actions(iActions));
                config.getOnlyOnePowerSupply().setThreshold(db.getLong(rs, "ONE_SUPPLY_USED_VALUE"));

                iActions = db.getInteger(rs, "CPU_TEMP_TOO_HIGH_ACTION");
                config.setCpuTempTooHigh(int2Actions(iActions));
                config.getCpuTempTooHigh().setThreshold(db.getLong(rs, "CPU_TEMP_TOO_HIGH_VALUE"));

                iActions = db.getInteger(rs, "CPU_FAN_TOO_SLOW_ACTION");
                config.setCpuFanTooSlow(int2Actions(iActions));
                config.getCpuFanTooSlow().setThreshold(db.getLong(rs, "CPU_FAN_TOO_SLOW_VALUE"));

                iActions = db.getInteger(rs, "CPU_FAN_BAD_ACTION");
                config.setCpuFanBad(int2Actions(iActions));
                config.getCpuFanBad().setThreshold(db.getLong(rs, "CPU_FAN_BAD_VALUE"));

                iActions = db.getInteger(rs, "CHSS_TMP_TOO_HIGH_ACTION");
                config.setChassisTempTooHigh(int2Actions(iActions));
                config.getChassisTempTooHigh().setThreshold(db.getLong(rs, "CHSS_TMP_TOO_HIGH_VALUE"));

                iActions = db.getInteger(rs, "CHSS_FAN_BAD_ACTION");
                config.setChassisFanBad(int2Actions(iActions));
                config.getChassisFanBad().setThreshold(db.getLong(rs, "CHSS_FAN_BAD_VALUE"));

                iActions = db.getInteger(rs, "CHSS_SUPPLY_BAD_ACTION");
                config.setChassisPowerSupplyBad(int2Actions(iActions));
                config.getChassisPowerSupplyBad().setThreshold(db.getLong(rs, "CHSS_SUPPLY_BAD_VALUE"));

                iActions = db.getInteger(rs, "UNIT_GOING_ACTIVE_ACTION");
                config.setUnitGoingActive(int2Actions(iActions));
                config.getUnitGoingActive().setThreshold(db.getLong(rs, "UNIT_GOING_ACTIVE_VALUE"));

                iActions = db.getInteger(rs, "UNIT_GOING_STANDBY_ACTION");
                config.setUnitGoingStandby(int2Actions(iActions));
                config.getUnitGoingStandby().setThreshold(db.getLong(rs, "UNIT_GOING_STANDBY_VALUE"));

                iActions = db.getInteger(rs, "BLOCK_DDOS_ACTION");
                config.setBlockDDoS(int2Actions(iActions));
                config.getBlockDDoS().setThreshold(db.getLong(rs, "BLOCK_DDOS_VALUE"));

                iActions = db.getInteger(rs, "VOLTAGE_TOO_HIGH_ACTION");
                config.setVoltageTooHigh(int2Actions(iActions));
                config.getVoltageTooHigh().setThreshold(db.getLong(rs, "VALTAGE_TOO_HIGH_VALUE"));

                iActions = db.getInteger(rs, "FAN_TOO_SLOW_ACTION");
                config.setChassisFanTooSlow(int2Actions(iActions));
                config.getChassisFanTooSlow().setThreshold(db.getLong(rs, "FAN_TOO_SLOW_VALUE"));

                iActions = db.getInteger(rs, "RESP_TIME_TOO_LONG_ACTION");
                config.setResponseTime(int2Actions(iActions));
                config.getResponseTime().setThreshold(db.getLong(rs, "RESP_TIME_TOO_LONG_VALUE"));

                iActions = db.getInteger(rs, "REDUNDANCY_FALUT_ACTION");
                config.setRedundancyCheck(int2Actions(iActions));
                config.getRedundancyCheck().setThreshold(db.getLong(rs, "REDUNDANCY_FALUT_VALUE"));

                iActions = db.getInteger(rs, "VRRP_COLLISION_ACTION");// vrrp/vrid 충돌 감지용. bwpark. 2014.04.11
                config.setVrrpCollision(int2Actions(iActions));

                iActions = db.getInteger(rs, "GATEWAY_FAIL_UP_ACTION");
                config.setGatewayFailUp(int2Actions(iActions));

                iActions = db.getInteger(rs, "GATEWAY_FAIL_DOWN_ACTION");
                config.setGatewayFailDown(int2Actions(iActions));

                iActions = db.getInteger(rs, "ADC_FILTERSESSION_ACTION");
                config.setFilterSessionLimitHigh(int2Actions(iActions));
                config.getFilterSessionLimitHigh().setThreshold(db.getLong(rs, "ADC_FILTERSESSION_VALUE"));
                
                ret.add(config);
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
        return ret;
    }

    public OBDtoAlarmConfig getAlarmConfigurationWithAdcIndex(Integer adcIndex) throws OBException
    {
        OBDtoADCObject object = new OBDtoADCObject(OBDtoADCObject.CATEGORY_ADC, adcIndex, 0);
        ArrayList<OBDtoAlarmConfig> config = getAlarmConfiguration(object);
        return config.get(0);
    }

    public HashMap<Integer, OBDtoAlarmConfig> getAlarmCfgAll() throws OBException
    {
        String sqlText = "";
        int iActions = 0x00000000;
        HashMap<Integer, OBDtoAlarmConfig> retVal = new HashMap<Integer, OBDtoAlarmConfig>(); // collection에 대해서 null을 리턴하지 않기로 했으므로 null로 초기화 하지 않는다.

        sqlText =
                String.format(" SELECT * FROM MNG_ALARM "
                        + " WHERE CATEGORY = %d AND OBJECT_INDEX IN (SELECT INDEX FROM MNG_ADC WHERE AVAILABLE = %d) ",
                        OBDtoADCObject.CATEGORY_ADC, OBDefine.ADC_STATE.AVAILABLE);
        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();

            ResultSet rs = db.executeQuery(sqlText);
            while(rs.next())
            {
                OBDtoAlarmConfig config = new OBDtoAlarmConfig();
                config.setObject(new OBDtoADCObject());
                config.getObject().setCategory(db.getInteger(rs, "CATEGORY"));
                config.getObject().setIndex(db.getInteger(rs, "OBJECT_INDEX"));
                config.getObject().setVendor(db.getInteger(rs, "ADC_TYPE"));
                config.setConfigLevel(db.getInteger(rs, "CONFIG_LEVEL"));
                config.setAdcType(db.getInteger(rs, "ADC_TYPE"));

                iActions = db.getInteger(rs, "ADC_DISCONNECT_ACTION");
                config.setAdcDisconnect(int2Actions(iActions));

                // iActions = db.getInteger(rs, "ADC_CONNECT_ACTION");
                // config.setAdcConnectAction(int2Actions(iActions));

                iActions = db.getInteger(rs, "VIRTUALSERVER_DOWN_ACTION");
                config.setVirtualServerDown(int2Actions(iActions));

                // iActions = db.getInteger(rs, "VIRTUALSERVER_UP_ACTION");
                // config.setVirtualServerUpAction(int2Actions(iActions));

                iActions = db.getInteger(rs, "POOLMEMBER_DOWN_ACTION");
                config.setPoolMemberDown(int2Actions(iActions));

                // iActions = db.getInteger(rs, "POOLMEMBER_UP_ACTION");
                // config.setPoolMemberUpAction(int2Actions(iActions));

                iActions = db.getInteger(rs, "LINK_DOWN_ACTION");
                config.setInterfaceDown(int2Actions(iActions));

                // iActions = db.getInteger(rs, "LINK_UP_ACTION");
                // config.setLinkUpAction(int2Actions(iActions));

                iActions = db.getInteger(rs, "ADC_BOOT_ACTION");
                config.setAdcBooting(int2Actions(iActions));

                iActions = db.getInteger(rs, "ADC_STANDBY_ACTION");
                config.setAdcActiveToStandby(int2Actions(iActions));

                iActions = db.getInteger(rs, "ADC_ACTIVE_ACTION");
                config.setAdcStandbyToActive(int2Actions(iActions));

                iActions = db.getInteger(rs, "ADC_CPU_ACTION");
                config.setAdcCpuLimit(int2Actions(iActions));
                config.getAdcCpuLimit().setThreshold(db.getLong(rs, "ADC_CPU_VALUE"));

                iActions = db.getInteger(rs, "ADC_MEM_ACTION");
                config.setAdcMemLimit(int2Actions(iActions));
                config.getAdcMemLimit().setThreshold(db.getLong(rs, "ADC_MEM_VALUE"));

                iActions = db.getInteger(rs, "ADC_SSL_TRANS_ACTION");
                config.setAdcSslTransaction(int2Actions(iActions));
                config.getAdcSslTransaction().setThreshold(db.getLong(rs, "ADC_SSL_TRANS_VALUE"));

                iActions = db.getInteger(rs, "ADC_CONN_ACTION");
                config.setConnectionLimitHigh(int2Actions(iActions));
                config.getConnectionLimitHigh().setThreshold(db.getLong(rs, "ADC_CONN_VALUE"));
                iActions = db.getInteger(rs, "ADC_CONN_LOW_ACTION");
                config.setConnectionLimitLow(int2Actions(iActions));
                config.getConnectionLimitLow().setThreshold(db.getLong(rs, "ADC_CONN_LOW_VALUE"));

                iActions = db.getInteger(rs, "ADC_UPTIME_ACTION");
                config.setAdcUptime(int2Actions(iActions));
                config.getAdcUptime().setThreshold(db.getLong(rs, "ADC_UPTIME_VALUE"));

                iActions = db.getInteger(rs, "ADC_PURCHASE_ACTION");
                config.setAdcPurchase(int2Actions(iActions));
                config.getAdcPurchase().setThreshold(db.getLong(rs, "ADC_PURCHASE_VALUE"));

                iActions = db.getInteger(rs, "ADC_SSLCERT_ACTION");
                config.setAdcSslcert(int2Actions(iActions));
                config.getAdcSslcert().setThreshold(db.getLong(rs, "ADC_SSLCERT_VALUE"));

                // new item.
                iActions = db.getInteger(rs, "ADC_MP_LIMIT_ACTION");
                config.setAdcMPLimit(int2Actions(iActions));
                config.getAdcMPLimit().setThreshold(db.getLong(rs, "ADC_MP_LIMIT_VALUE"));

                iActions = db.getInteger(rs, "ADC_SP_LIMIT_ACTION");
                config.setAdcSPLimit(int2Actions(iActions));
                config.getAdcSPLimit().setThreshold(db.getLong(rs, "ADC_SP_LIMIT_VALUE"));

                iActions = db.getInteger(rs, "INF_ERROR_ACTION");
                config.setInterfaceError(int2Actions(iActions));
                config.getInterfaceError().setThreshold(db.getLong(rs, "INF_ERROR_VALUE"));

                iActions = db.getInteger(rs, "INF_USAGE_LIMIT_ACTION");
                config.setInterfaceUsageLimit(int2Actions(iActions));
                config.getInterfaceUsageLimit().setThreshold(db.getLong(rs, "INF_USAGE_LIMIT_VALUE"));

                iActions = db.getInteger(rs, "INF_DUPLEX_ACTION");
                config.setInterfaceDuplexChange(int2Actions(iActions));
                config.getInterfaceDuplexChange().setThreshold(db.getLong(rs, "INF_DUPLEX_VALUE"));

                iActions = db.getInteger(rs, "INF_SPEED_ACTIN");
                config.setInterfaceSpeedChange(int2Actions(iActions));
                config.getInterfaceSpeedChange().setThreshold(db.getLong(rs, "INF_SPEED_VALUE"));

                iActions = db.getInteger(rs, "ADC_CONF_SYNC_FAIL_ACTION");
                config.setAdcConfSyncFailure(int2Actions(iActions));
                config.getAdcConfSyncFailure().setThreshold(db.getLong(rs, "ADC_CONF_SYNC_FAIL_VALUE"));

                iActions = db.getInteger(rs, "ADC_CONF_BACKUP_FAIL_ACTION");
                config.setAdcConfBackupFailure(int2Actions(iActions));
                config.getAdcConfBackupFailure().setThreshold(db.getLong(rs, "ADC_CONF_BACKUP_FAIL_VALUE"));

                iActions = db.getInteger(rs, "FAN_NOT_OPERATIONAL_ACTION");
                config.setFanNotOperational(int2Actions(iActions));
                config.getFanNotOperational().setThreshold(db.getLong(rs, "FAN_NOT_OPERATIONAL_VALUE"));

                iActions = db.getInteger(rs, "TEMP_TOO_HIGH_ACTION");
                config.setTemperatureTooHigh(int2Actions(iActions));
                config.getTemperatureTooHigh().setThreshold(db.getLong(rs, "TEMP_TOO_HIGH_VALUE"));

                iActions = db.getInteger(rs, "ONE_SUPPLY_USED_ACTION");
                config.setOnlyOnePowerSupply(int2Actions(iActions));
                config.getOnlyOnePowerSupply().setThreshold(db.getLong(rs, "ONE_SUPPLY_USED_VALUE"));

                iActions = db.getInteger(rs, "CPU_TEMP_TOO_HIGH_ACTION");
                config.setCpuTempTooHigh(int2Actions(iActions));
                config.getCpuTempTooHigh().setThreshold(db.getLong(rs, "CPU_TEMP_TOO_HIGH_VALUE"));

                iActions = db.getInteger(rs, "CPU_FAN_TOO_SLOW_ACTION");
                config.setCpuFanTooSlow(int2Actions(iActions));
                config.getCpuFanTooSlow().setThreshold(db.getLong(rs, "CPU_FAN_TOO_SLOW_VALUE"));

                iActions = db.getInteger(rs, "CPU_FAN_BAD_ACTION");
                config.setCpuFanBad(int2Actions(iActions));
                config.getCpuFanBad().setThreshold(db.getLong(rs, "CPU_FAN_BAD_VALUE"));

                iActions = db.getInteger(rs, "CHSS_TMP_TOO_HIGH_ACTION");
                config.setChassisTempTooHigh(int2Actions(iActions));
                config.getChassisTempTooHigh().setThreshold(db.getLong(rs, "CHSS_TMP_TOO_HIGH_VALUE"));

                iActions = db.getInteger(rs, "CHSS_FAN_BAD_ACTION");
                config.setChassisFanBad(int2Actions(iActions));
                config.getChassisFanBad().setThreshold(db.getLong(rs, "CHSS_FAN_BAD_VALUE"));

                iActions = db.getInteger(rs, "CHSS_SUPPLY_BAD_ACTION");
                config.setChassisPowerSupplyBad(int2Actions(iActions));
                config.getChassisPowerSupplyBad().setThreshold(db.getLong(rs, "CHSS_SUPPLY_BAD_VALUE"));

                iActions = db.getInteger(rs, "UNIT_GOING_ACTIVE_ACTION");
                config.setUnitGoingActive(int2Actions(iActions));
                config.getUnitGoingActive().setThreshold(db.getLong(rs, "UNIT_GOING_ACTIVE_VALUE"));

                iActions = db.getInteger(rs, "UNIT_GOING_STANDBY_ACTION");
                config.setUnitGoingStandby(int2Actions(iActions));
                config.getUnitGoingStandby().setThreshold(db.getLong(rs, "UNIT_GOING_STANDBY_VALUE"));

                iActions = db.getInteger(rs, "BLOCK_DDOS_ACTION");
                config.setBlockDDoS(int2Actions(iActions));
                config.getBlockDDoS().setThreshold(db.getLong(rs, "BLOCK_DDOS_VALUE"));

                iActions = db.getInteger(rs, "VOLTAGE_TOO_HIGH_ACTION");
                config.setVoltageTooHigh(int2Actions(iActions));
                config.getVoltageTooHigh().setThreshold(db.getLong(rs, "VALTAGE_TOO_HIGH_VALUE"));

                iActions = db.getInteger(rs, "FAN_TOO_SLOW_ACTION");
                config.setChassisFanTooSlow(int2Actions(iActions));
                config.getChassisFanTooSlow().setThreshold(db.getLong(rs, "FAN_TOO_SLOW_VALUE"));

                iActions = db.getInteger(rs, "RESP_TIME_TOO_LONG_ACTION");
                config.setResponseTime(int2Actions(iActions));
                config.getResponseTime().setThreshold(db.getLong(rs, "RESP_TIME_TOO_LONG_VALUE"));

                iActions = db.getInteger(rs, "REDUNDANCY_FALUT_ACTION");
                config.setRedundancyCheck(int2Actions(iActions));
                config.getRedundancyCheck().setThreshold(db.getLong(rs, "REDUNDANCY_FALUT_VALUE"));

                iActions = db.getInteger(rs, "VRRP_COLLISION_ACTION");// vrrp/vrid 충돌 감지용. bwpark. 2014.04.11
                config.setVrrpCollision(int2Actions(iActions));

                iActions = db.getInteger(rs, "GATEWAY_FAIL_DOWN_ACTION");
                config.setGatewayFailDown(int2Actions(iActions));

                retVal.put(config.getObject().getIndex(), config);
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
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
        return retVal;
    }

    private OBDtoAlarmConfig initNullClass(OBDtoAlarmConfig config) throws OBException
    {
        OBDtoAlarmAction initClass = new OBDtoAlarmAction();
        if(config.getAdcDisconnect() == null)
            config.setAdcDisconnect(initClass);
        if(config.getVirtualServerDown() == null)
            config.setVirtualServerDown(initClass);
        if(config.getPoolMemberDown() == null)
            config.setPoolMemberDown(initClass);
        if(config.getInterfaceDown() == null)
            config.setInterfaceDown(initClass);
        if(config.getAdcBooting() == null)
            config.setAdcBooting(initClass);
        if(config.getAdcActiveToStandby() == null)
            config.setAdcActiveToStandby(initClass);
        if(config.getAdcStandbyToActive() == null)
            config.setAdcStandbyToActive(initClass);
        if(config.getAdcCpuLimit() == null)
            config.setAdcCpuLimit(initClass);
        if(config.getAdcMemLimit() == null)
            config.setAdcMemLimit(initClass);
        if(config.getAdcSslTransaction() == null)
            config.setAdcSslTransaction(initClass);
        if(config.getConnectionLimitHigh() == null)
            config.setConnectionLimitHigh(initClass);
        if(config.getConnectionLimitLow() == null)
            config.setConnectionLimitLow(initClass);
        if(config.getAdcUptime() == null)
            config.setAdcUptime(initClass);
        if(config.getAdcPurchase() == null)
            config.setAdcPurchase(initClass);
        if(config.getAdcSslcert() == null)
            config.setAdcSslcert(initClass);

        if(config.getAdcMPLimit() == null)
            config.setAdcMPLimit(initClass);
        if(config.getAdcSPLimit() == null)
            config.setAdcSPLimit(initClass);

        if(config.getInterfaceError() == null)
            config.setInterfaceError(initClass);
        if(config.getInterfaceUsageLimit() == null)
            config.setInterfaceUsageLimit(initClass);
        if(config.getInterfaceDuplexChange() == null)
            config.setInterfaceDuplexChange(initClass);
        if(config.getInterfaceSpeedChange() == null)
            config.setInterfaceSpeedChange(initClass);
        if(config.getAdcConfSyncFailure() == null)
            config.setAdcConfSyncFailure(initClass);
        if(config.getAdcConfBackupFailure() == null)
            config.setAdcConfBackupFailure(initClass);
        if(config.getFanNotOperational() == null)
            config.setFanNotOperational(initClass);
        if(config.getTemperatureTooHigh() == null)
            config.setTemperatureTooHigh(initClass);
        if(config.getOnlyOnePowerSupply() == null)
            config.setOnlyOnePowerSupply(initClass);
        if(config.getCpuTempTooHigh() == null)
            config.setCpuTempTooHigh(initClass);
        if(config.getCpuFanTooSlow() == null)
            config.setCpuFanTooSlow(initClass);
        if(config.getCpuFanBad() == null)
            config.setCpuFanBad(initClass);
        if(config.getChassisTempTooHigh() == null)
            config.setChassisTempTooHigh(initClass);
        if(config.getChassisFanBad() == null)
            config.setChassisFanBad(initClass);
        if(config.getChassisPowerSupplyBad() == null)
            config.setChassisPowerSupplyBad(initClass);
        if(config.getUnitGoingActive() == null)
            config.setUnitGoingActive(initClass);
        if(config.getUnitGoingStandby() == null)
            config.setUnitGoingStandby(initClass);
        if(config.getBlockDDoS() == null)
            config.setBlockDDoS(initClass);
        if(config.getVoltageTooHigh() == null)
            config.setVoltageTooHigh(initClass);
        if(config.getChassisFanTooSlow() == null)
            config.setChassisFanTooSlow(initClass);
        if(config.getResponseTime() == null)
            config.setResponseTime(initClass);
        if(config.getRedundancyCheck() == null)
            config.setRedundancyCheck(initClass);
        if(config.getGatewayFailDown() == null)
            config.setGatewayFailDown(initClass);
        if(config.getFilterSessionLimitHigh() == null)
            config.setFilterSessionLimitHigh(initClass);
        return config;
    }

    // 실질적으로 alarm config를 하는 본 함수
    private void doAlarmConfiguration(OBDtoAlarmConfig config, JOB job, OBDatabase db) throws OBException
    {
        config = initNullClass(config);
        String sqlText = "";
        String sqlWhere = " FALSE ";
        Long respTimeValue = 1000L; // ADC 등록시 경보장애정보 응답시간 default 1000 msec
        // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "job = " + job);
        // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("category %d, index %d, configlevel %d, type %d ",
        // config.getObject().getCategory(),
        // config.getObject().getIndex(),
        // config.getConfigLevel(),
        // config.getAdcType())
        // );
        try
        {
            if(job.equals(JOB.ADD))
            {
                sqlText =
                        String.format(" INSERT INTO MNG_ALARM                                        \n"
                                + " (    OBJECT_INDEX, CATEGORY, ADC_TYPE, CONFIG_LEVEL,         \n"
                                + // 1-4
                                "      ADC_DISCONNECT_ACTION, ADC_CONNECT_ACTION,              \n"
                                + // 5-6
                                "      VIRTUALSERVER_DOWN_ACTION, VIRTUALSERVER_UP_ACTION,     \n"
                                + // 7-8
                                "      POOLMEMBER_DOWN_ACTION, POOLMEMBER_UP_ACTION,           \n"
                                + // 9-10
                                "      LINK_DOWN_ACTION, LINK_UP_ACTION,                       \n"
                                + // 11-12
                                "      ADC_BOOT_ACTION, ADC_STANDBY_ACTION, ADC_ACTIVE_ACTION, \n"
                                + // 13-15
                                "      ADC_CPU_ACTION, ADC_CPU_VALUE,                          \n"
                                + // 16-17
                                "      ADC_MEM_ACTION, ADC_MEM_VALUE,                          \n"
                                + // 18
                                "      ADC_SSL_TRANS_ACTION, ADC_SSL_TRANS_VALUE,              \n"
                                + // 20
                                "      ADC_CONN_ACTION, ADC_CONN_VALUE,                        \n"
                                + // 22
                                "      ADC_CONN_LOW_ACTION, ADC_CONN_LOW_VALUE,                \n"
                                + // 24
                                "      ADC_UPTIME_ACTION, ADC_UPTIME_VALUE,                    \n"
                                + // 26
                                "      ADC_PURCHASE_ACTION, ADC_PURCHASE_VALUE,                \n"
                                + // 28
                                "      ADC_SSLCERT_ACTION, ADC_SSLCERT_VALUE,                  \n"
                                + // 30
                                "      ADC_MP_LIMIT_ACTION, ADC_MP_LIMIT_VALUE,                \n"
                                + // 32
                                "      ADC_SP_LIMIT_ACTION, ADC_SP_LIMIT_VALUE,                \n"
                                + // 34
                                "      INF_ERROR_ACTION, INF_ERROR_VALUE,                      \n"
                                + // 36
                                "      INF_USAGE_LIMIT_ACTION, INF_USAGE_LIMIT_VALUE,          \n"
                                + // 38
                                "      INF_DUPLEX_ACTION, INF_DUPLEX_VALUE,                    \n"
                                + // 40
                                "      INF_SPEED_ACTIN, INF_SPEED_VALUE,                       \n"
                                + // 42
                                "      ADC_CONF_SYNC_FAIL_ACTION, ADC_CONF_SYNC_FAIL_VALUE,    \n"
                                + // 44
                                "      ADC_CONF_BACKUP_FAIL_ACTION, ADC_CONF_BACKUP_FAIL_VALUE,\n"
                                + // 46
                                "      FAN_NOT_OPERATIONAL_ACTION, FAN_NOT_OPERATIONAL_VALUE,  \n"
                                + // 48
                                "      TEMP_TOO_HIGH_ACTION, TEMP_TOO_HIGH_VALUE,              \n"
                                + // 50
                                "      ONE_SUPPLY_USED_ACTION, ONE_SUPPLY_USED_VALUE,          \n"
                                + // 52
                                "      CPU_TEMP_TOO_HIGH_ACTION, CPU_TEMP_TOO_HIGH_VALUE,      \n"
                                + // 54
                                "      CPU_FAN_TOO_SLOW_ACTION, CPU_FAN_TOO_SLOW_VALUE,        \n"
                                + // 56
                                "      CPU_FAN_BAD_ACTION, CPU_FAN_BAD_VALUE,                  \n"
                                + // 58
                                "      CHSS_TMP_TOO_HIGH_ACTION, CHSS_TMP_TOO_HIGH_VALUE,      \n"
                                + // 60
                                "      CHSS_FAN_BAD_ACTION, CHSS_FAN_BAD_VALUE,                \n"
                                + // 62
                                "      CHSS_SUPPLY_BAD_ACTION, CHSS_SUPPLY_BAD_VALUE,          \n"
                                + // 64
                                "      UNIT_GOING_ACTIVE_ACTION, UNIT_GOING_ACTIVE_VALUE,      \n"
                                + // 66
                                "      UNIT_GOING_STANDBY_ACTION, UNIT_GOING_STANDBY_VALUE ,   \n"
                                + // 68
                                "      BLOCK_DDOS_ACTION, BLOCK_DDOS_VALUE,                    \n"
                                + // 70
                                "      VOLTAGE_TOO_HIGH_ACTION, VALTAGE_TOO_HIGH_VALUE,        \n"
                                + // 72
                                "      FAN_TOO_SLOW_ACTION, FAN_TOO_SLOW_VALUE,                \n"
                                + // 74
                                "      RESP_TIME_TOO_LONG_ACTION, RESP_TIME_TOO_LONG_VALUE,    \n"
                                + // 76
                                "      REDUNDANCY_FALUT_ACTION, REDUNDANCY_FALUT_VALUE,        \n"
                                + // 78
                                "      VRRP_COLLISION_ACTION,                                  \n"
                                + // 80
                                "      GATEWAY_FAIL_DOWN_ACTION, GATEWAY_FAIL_UP_ACTION,       \n"
                                + // 81
                                
                                "      ADC_FILTERSESSION_ACTION, ADC_FILTERSESSION_VALUE       \n"
                                + // 82
                                " ) VALUES                                                     \n"
                                + " (    %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                 \n"
                                + // 1-10
                                "      %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                 \n"
                                + // 11-20
                                "      %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                 \n"
                                + // 21-30
                                "      %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                 \n"
                                + // 31-40
                                "      %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                 \n"
                                + // 41-50
                                "      %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                 \n"
                                + // 51-60
                                "      %d, %d, %d, %d, %d, %d, %d, %d, %d, %d,                 \n"
                                + // 61-70
                                "      %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d, %d  \n"
                                + // 71-82
                                " ) ", config.getObject().getIndex(), config.getObject().getCategory(), config
                                .getObject()
                                .getVendor(),
                                config.getConfigLevel(), // 1-4
                                actions2Int(config.getAdcDisconnect()),
                                0, // 5
                                actions2Int(config.getVirtualServerDown()),
                                0, // 7
                                actions2Int(config.getPoolMemberDown()),
                                0, // 9
                                actions2Int(config.getInterfaceDown()),
                                0, // 11
                                actions2Int(config.getAdcBooting()), actions2Int(config.getAdcActiveToStandby()),
                                actions2Int(config.getAdcStandbyToActive()), // 13
                                actions2Int(config.getAdcCpuLimit()), config.getAdcCpuLimit().getThreshold(), // 16
                                actions2Int(config.getAdcMemLimit()), config.getAdcMemLimit().getThreshold(), // 18
                                actions2Int(config.getAdcSslTransaction()), config.getAdcSslTransaction().getThreshold(), // 20
                                actions2Int(config.getConnectionLimitHigh()), config.getConnectionLimitHigh().getThreshold(), // 22
                                actions2Int(config.getConnectionLimitLow()), config.getConnectionLimitLow().getThreshold(), // 24
                                actions2Int(config.getAdcUptime()), config.getAdcUptime().getThreshold(), // 26
                                actions2Int(config.getAdcPurchase()), config.getAdcPurchase().getThreshold(), // 28
                                actions2Int(config.getAdcSslcert()), config.getAdcSslcert().getThreshold(), // 30
                                actions2Int(config.getAdcMPLimit()), config.getAdcMPLimit().getThreshold(), // 32
                                actions2Int(config.getAdcSPLimit()), config.getAdcSPLimit().getThreshold(), // 34
                                actions2Int(config.getInterfaceError()), config.getInterfaceError().getThreshold(), // 36
                                actions2Int(config.getInterfaceUsageLimit()), config.getInterfaceUsageLimit().getThreshold(), // 38
                                actions2Int(config.getInterfaceDuplexChange()), config
                                        .getInterfaceDuplexChange()
                                        .getThreshold(),// 40
                                actions2Int(config.getInterfaceSpeedChange()), config.getInterfaceSpeedChange().getThreshold(), // 42
                                actions2Int(config.getAdcConfSyncFailure()), config.getAdcConfSyncFailure().getThreshold(), // 44
                                actions2Int(config.getAdcConfBackupFailure()), config.getAdcConfBackupFailure().getThreshold(), // 46
                                actions2Int(config.getFanNotOperational()), config.getFanNotOperational().getThreshold(), // 48
                                actions2Int(config.getTemperatureTooHigh()), config.getTemperatureTooHigh().getThreshold(), // 50
                                actions2Int(config.getOnlyOnePowerSupply()), config.getOnlyOnePowerSupply().getThreshold(), // 52
                                actions2Int(config.getCpuTempTooHigh()), config.getCpuTempTooHigh().getThreshold(), // 54
                                actions2Int(config.getCpuFanTooSlow()), config.getCpuFanTooSlow().getThreshold(), // 56
                                actions2Int(config.getCpuFanBad()), config.getCpuFanBad().getThreshold(), // 58
                                actions2Int(config.getChassisTempTooHigh()), config.getChassisTempTooHigh().getThreshold(), // 60
                                actions2Int(config.getChassisFanBad()), config.getChassisFanBad().getThreshold(), // 62
                                actions2Int(config.getChassisPowerSupplyBad()), config
                                        .getChassisPowerSupplyBad()
                                        .getThreshold(),// 64
                                actions2Int(config.getUnitGoingActive()), config.getUnitGoingActive().getThreshold(), // 66
                                actions2Int(config.getUnitGoingStandby()), config.getUnitGoingStandby().getThreshold(), // 68
                                actions2Int(config.getBlockDDoS()), config.getBlockDDoS().getThreshold(), // 70
                                actions2Int(config.getVoltageTooHigh()), config.getVoltageTooHigh().getThreshold(), // 72
                                actions2Int(config.getChassisFanTooSlow()), config.getChassisFanTooSlow().getThreshold(), // 74
                                // actions2Int(config.getResponseTime()), config.getResponseTime().getThreshold(),
                                actions2Int(config.getResponseTime()), respTimeValue, // 76
                                actions2Int(config.getRedundancyCheck()), config.getRedundancyCheck().getThreshold(), // 78
                                actions2Int(config.getVrrpCollision()), // 80
                                actions2Int(config.getGatewayFailDown()), 0, //81 
                                actions2Int(config.getFilterSessionLimitHigh()), config.getFilterSessionLimitHigh().getThreshold()  // 82
                                );
            }
            else if(job.equals(JOB.UPDATE))
            {
                // ADC에서 전체나 그룹 설정으로 가는 경우는 config를 바꿔준다.
                if(config.getObject().getCategory().equals(OBDtoADCObject.CATEGORY_ADC)
                        && config.getConfigLevel().equals(OBDtoADCObject.CATEGORY_ALL) == true)
                {
                    config = changeAlarmConfigurationToGlobal(config.getObject().getIndex());
                }
                else if(config.getObject().getCategory().equals(OBDtoADCObject.CATEGORY_ADC)
                        && config.getConfigLevel().equals(OBDtoADCObject.CATEGORY_GROUP) == true)
                {
                    config = changeAlarmConfigurationToGroup(config.getObject().getIndex()); // 소속 그룹으로 설정한다.
                }
                else if(config.getObject().getCategory().equals(OBDtoADCObject.CATEGORY_ALL))
                {
                    config.setConfigLevel(OBDtoADCObject.CATEGORY_ALL);
                }
                else if(config.getObject().getCategory().equals(OBDtoADCObject.CATEGORY_GROUP))
                {
                    config.setConfigLevel(OBDtoADCObject.CATEGORY_GROUP);
                }

                if(config.getObject().getCategory().equals(OBDtoADCObject.CATEGORY_ADC)) // ADC 레벨의 설정 변경이면 ADC만 바꾼다.
                {
                    sqlWhere =
                            String.format(" CATEGORY = %d AND OBJECT_INDEX = %d AND ADC_TYPE = %d ", config
                                    .getObject()
                                    .getCategory(), config.getObject().getIndex(), config.getObject().getVendor());
                }
                else
                // 전체나 그룹. 설정이 바뀌면, 전체나 그룹 설정을 쓰는 ADC들도 같이 바꾼다.
                {
                    String memberAdcs =
                            new OBAdcManagementImpl().getUsersAdcIndexString(config.getObject().getCategory(), config
                                    .getObject()
                                    .getIndex(), null);
                    if(memberAdcs == null || memberAdcs.length() == 0)
                    {
                        memberAdcs = "-1";
                    }
                    sqlWhere =
                            String.format(" (CATEGORY = %d AND OBJECT_INDEX = %d AND ADC_TYPE = %d) OR "
                                    + " (CATEGORY = %d AND OBJECT_INDEX IN (%s) AND ADC_TYPE = %d AND CONFIG_LEVEL = %d) ",
                                    config.getObject().getCategory(), config.getObject().getIndex(), config
                                            .getObject()
                                            .getVendor() // 자기꺼
                                    , OBDtoADCObject.CATEGORY_ADC, memberAdcs, config.getAdcType(), config.getConfigLevel());
                }
                // 그룹 설정이 바뀌면, 그룹 설정을 쓰는 ADC들도 같이 바꾼다.
                sqlText =
                        String.format(" UPDATE MNG_ALARM                     \n" + " SET ADC_TYPE = %d,  CONFIG_LEVEL=%d, \n" + // 3,4
                                "     ADC_DISCONNECT_ACTION=%d,       ADC_CONNECT_ACTION=%d,         \n"
                                + // 5,6
                                "     VIRTUALSERVER_DOWN_ACTION=%d,   VIRTUALSERVER_UP_ACTION=%d,    \n"
                                + // 7,8
                                "     POOLMEMBER_DOWN_ACTION=%d,      POOLMEMBER_UP_ACTION=%d,       \n"
                                + // 9,10
                                "     LINK_DOWN_ACTION=%d,            LINK_UP_ACTION=%d,             \n"
                                + // 11,12
                                "     ADC_BOOT_ACTION=%d,                                            \n"
                                + // 13
                                "     ADC_STANDBY_ACTION=%d,          ADC_ACTIVE_ACTION=%d,          \n"
                                + // 14,15
                                "     ADC_CPU_ACTION=%d,              ADC_CPU_VALUE=%d,              \n"
                                + // 16,17
                                "     ADC_MEM_ACTION=%d,              ADC_MEM_VALUE=%d,              \n"
                                + // 18,19
                                "     ADC_SSL_TRANS_ACTION=%d,        ADC_SSL_TRANS_VALUE=%d,        \n"
                                + // 20,21
                                "     ADC_CONN_ACTION=%d,             ADC_CONN_VALUE=%d,             \n"
                                + // 22,23
                                "     ADC_CONN_LOW_ACTION=%d,         ADC_CONN_LOW_VALUE=%d,         \n"
                                + // 24,25
                                "     ADC_UPTIME_ACTION=%d,           ADC_UPTIME_VALUE=%d,           \n"
                                + // 26,27
                                "     ADC_PURCHASE_ACTION=%d,         ADC_PURCHASE_VALUE=%d,         \n"
                                + // 28,29
                                "     ADC_SSLCERT_ACTION=%d,          ADC_SSLCERT_VALUE=%d,          \n"
                                + // 30,31
                                "     ADC_MP_LIMIT_ACTION=%d,         ADC_MP_LIMIT_VALUE=%d,         \n"
                                + // 32,33
                                "     ADC_SP_LIMIT_ACTION=%d,         ADC_SP_LIMIT_VALUE=%d,         \n"
                                + // 34,35
                                "     INF_ERROR_ACTION=%d,            INF_ERROR_VALUE=%d,            \n"
                                + // 36,37
                                "     INF_USAGE_LIMIT_ACTION=%d,      INF_USAGE_LIMIT_VALUE=%d,      \n"
                                + // 38,39
                                "     INF_DUPLEX_ACTION=%d,           INF_DUPLEX_VALUE=%d,           \n"
                                + // 40,41
                                "     INF_SPEED_ACTIN=%d,             INF_SPEED_VALUE=%d,            \n"
                                + // 42,43
                                "     ADC_CONF_SYNC_FAIL_ACTION=%d,   ADC_CONF_SYNC_FAIL_VALUE=%d,   \n"
                                + // 44,45
                                "     ADC_CONF_BACKUP_FAIL_ACTION=%d, ADC_CONF_BACKUP_FAIL_VALUE=%d, \n"
                                + // 46,47
                                "     FAN_NOT_OPERATIONAL_ACTION=%d,  FAN_NOT_OPERATIONAL_VALUE=%d,  \n"
                                + // 48,49
                                "     TEMP_TOO_HIGH_ACTION=%d,        TEMP_TOO_HIGH_VALUE=%d,        \n"
                                + // 50,51
                                "     ONE_SUPPLY_USED_ACTION=%d,      ONE_SUPPLY_USED_VALUE=%d,      \n"
                                + // 52,53
                                "     CPU_TEMP_TOO_HIGH_ACTION=%d,    CPU_TEMP_TOO_HIGH_VALUE=%d,    \n"
                                + // 54,55
                                "     CPU_FAN_TOO_SLOW_ACTION=%d,     CPU_FAN_TOO_SLOW_VALUE=%d ,    \n"
                                + // 56,57
                                "     CPU_FAN_BAD_ACTION=%d,          CPU_FAN_BAD_VALUE=%d,          \n"
                                + // 58,59
                                "     CHSS_TMP_TOO_HIGH_ACTION=%d,    CHSS_TMP_TOO_HIGH_VALUE=%d,    \n"
                                + // 60,61
                                "     CHSS_FAN_BAD_ACTION=%d,         CHSS_FAN_BAD_VALUE=%d,         \n"
                                + // 62,63
                                "     CHSS_SUPPLY_BAD_ACTION=%d,      CHSS_SUPPLY_BAD_VALUE=%d ,     \n"
                                + // 64,65
                                "     UNIT_GOING_ACTIVE_ACTION=%d,    UNIT_GOING_ACTIVE_VALUE=%d,    \n"
                                + // 66,67
                                "     UNIT_GOING_STANDBY_ACTION=%d,   UNIT_GOING_STANDBY_VALUE=%d,   \n"
                                + // 68,69
                                "     BLOCK_DDOS_ACTION=%d,           BLOCK_DDOS_VALUE=%d,           \n"
                                + // 70,71
                                "     VOLTAGE_TOO_HIGH_ACTION=%d,     VALTAGE_TOO_HIGH_VALUE=%d,     \n"
                                + // 72,73
                                "     FAN_TOO_SLOW_ACTION=%d,         FAN_TOO_SLOW_VALUE=%d,         \n"
                                + // 74,75
                                "     RESP_TIME_TOO_LONG_ACTION=%d,   RESP_TIME_TOO_LONG_VALUE=%d,   \n"
                                + // 76,77
                                "     REDUNDANCY_FALUT_ACTION=%d,     REDUNDANCY_FALUT_VALUE=%d,     \n"
                                + // 78,79
                                "     VRRP_COLLISION_ACTION=%d,                                      \n"
                                + // 80
                                "     GATEWAY_FAIL_DOWN_ACTION=%d,    GATEWAY_FAIL_UP_ACTION=%d,     \n"
                                + "   ADC_FILTERSESSION_ACTION=%d,    ADC_FILTERSESSION_VALUE=%d     \n"
                                + // 81,82
                                " WHERE %s ", config.getAdcType(), config.getConfigLevel(), // 3,4
                                actions2Int(config.getAdcDisconnect()), 0, // 5,6
                                actions2Int(config.getVirtualServerDown()), 0, // 7,8
                                actions2Int(config.getPoolMemberDown()), 0, // 9.10
                                actions2Int(config.getInterfaceDown()), 0, // 11,12
                                actions2Int(config.getAdcBooting()), // 13
                                actions2Int(config.getAdcActiveToStandby()), actions2Int(config.getAdcStandbyToActive()), // 14,15
                                actions2Int(config.getAdcCpuLimit()), config.getAdcCpuLimit().getThreshold(), // 16,17
                                actions2Int(config.getAdcMemLimit()), config.getAdcMemLimit().getThreshold(), // 18,19
                                actions2Int(config.getAdcSslTransaction()), config.getAdcSslTransaction().getThreshold(), // 20,21
                                actions2Int(config.getConnectionLimitHigh()), config.getConnectionLimitHigh().getThreshold(), // 22,23
                                actions2Int(config.getConnectionLimitLow()), config.getConnectionLimitLow().getThreshold(), // 24,25
                                actions2Int(config.getAdcUptime()), config.getAdcUptime().getThreshold(), // 26,27
                                actions2Int(config.getAdcPurchase()), config.getAdcPurchase().getThreshold(), // 28,29
                                actions2Int(config.getAdcSslcert()), config.getAdcSslcert().getThreshold(), // 30,31
                                actions2Int(config.getAdcMPLimit()), config.getAdcMPLimit().getThreshold(), // 32,33
                                actions2Int(config.getAdcSPLimit()), config.getAdcSPLimit().getThreshold(), // 34,35
                                actions2Int(config.getInterfaceError()), config.getInterfaceError().getThreshold(), // 36,37
                                actions2Int(config.getInterfaceUsageLimit()), config.getInterfaceUsageLimit().getThreshold(), // 38,39
                                actions2Int(config.getInterfaceDuplexChange()), config
                                        .getInterfaceDuplexChange()
                                        .getThreshold(), // 40,41
                                actions2Int(config.getInterfaceSpeedChange()), config.getInterfaceSpeedChange().getThreshold(), // 42,43
                                actions2Int(config.getAdcConfSyncFailure()), config.getAdcConfSyncFailure().getThreshold(), // 44,45
                                actions2Int(config.getAdcConfBackupFailure()), config.getAdcConfBackupFailure().getThreshold(), // 46,47
                                actions2Int(config.getFanNotOperational()), config.getFanNotOperational().getThreshold(), // 48,49
                                actions2Int(config.getTemperatureTooHigh()), config.getTemperatureTooHigh().getThreshold(), // 50,51
                                actions2Int(config.getOnlyOnePowerSupply()), config.getOnlyOnePowerSupply().getThreshold(), // 52,53
                                actions2Int(config.getCpuTempTooHigh()), config.getCpuTempTooHigh().getThreshold(), // 54,55
                                actions2Int(config.getCpuFanTooSlow()), config.getCpuFanTooSlow().getThreshold(), // 56,57
                                actions2Int(config.getCpuFanBad()), config.getCpuFanBad().getThreshold(), // 58,59
                                actions2Int(config.getChassisTempTooHigh()), config.getChassisTempTooHigh().getThreshold(), // 60,61
                                actions2Int(config.getChassisFanBad()), config.getChassisFanBad().getThreshold(), // 62,63
                                actions2Int(config.getChassisPowerSupplyBad()), config
                                        .getChassisPowerSupplyBad()
                                        .getThreshold(), // 64,65
                                actions2Int(config.getUnitGoingActive()), config.getUnitGoingActive().getThreshold(), // 66,67
                                actions2Int(config.getUnitGoingStandby()), config.getUnitGoingStandby().getThreshold(), // 68,69
                                actions2Int(config.getBlockDDoS()), config.getBlockDDoS().getThreshold(), // 70,71
                                actions2Int(config.getVoltageTooHigh()), config.getVoltageTooHigh().getThreshold(), // 72,73
                                actions2Int(config.getChassisFanTooSlow()), config.getChassisFanTooSlow().getThreshold(), // 74,75
                                actions2Int(config.getResponseTime()), config.getResponseTime().getThreshold(), // 76,77
                                actions2Int(config.getRedundancyCheck()), config.getRedundancyCheck().getThreshold(), // 78,79
                                actions2Int(config.getVrrpCollision()), // 80
                                actions2Int(config.getGatewayFailDown()), 0, // 81,82
                                actions2Int(config.getFilterSessionLimitHigh()), config.getFilterSessionLimitHigh().getThreshold(),  // 82
                                sqlWhere);
            }
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }

        try
        {
            // OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText = " + sqlText);
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
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end."));
    }

    // 알람 설정이 있는지 확인한다.
    private boolean isAlarmConfigExisting(Integer category, Integer index, Integer adcType, OBDatabase db) throws OBException
    {
        String sqlText = "";
        if(category == null || index == null)
        {
            return false;
        }
        try
        {
            sqlText =
                    String.format(
                            " SELECT OBJECT_INDEX FROM MNG_ALARM WHERE CATEGORY=%d AND OBJECT_INDEX=%d AND ADC_TYPE=%d; ",
                            category, index, adcType);
            ResultSet rs;
            rs = db.executeQuery(sqlText);
            if(rs.next() == false)
            {
                return false;
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
        return true;
    }
    
    public OBDtoReadConfig getReadConfig() throws OBException
    {
        String sqlText = " SELECT SYSLOG_SERVER, SNMPTRAP_SERVER, SNMPTRAP_PORT, SNMPTRAP_COMMUNITY, SNMPTRAP_VERSION " +
        		         " FROM MNG_ENV_ADDITIONAL WHERE INDEX = 1 ;";
        OBDtoReadConfig readConfig = new OBDtoReadConfig();

        OBDatabase db = new OBDatabase();

        try
        {
            db.openDB();

            ResultSet rs = db.executeQuery(sqlText);
            if(rs.next() == true)
            {
                String syslogServer = db.getString(rs, "SYSLOG_SERVER");
                readConfig.setSyslogServer(syslogServer);
                if(syslogServer != null && syslogServer.equals("0.0.0.0"))
                {
                    readConfig.setSyslogServer(null);
                }
                
                readConfig.setSnmpTrapServerAddress(db.getString(rs, "SNMPTRAP_SERVER"));
                readConfig.setSnmpTrapPort(db.getInteger(rs, "SNMPTRAP_PORT"));
                readConfig.setSnmpTrapCommunity(db.getString(rs, "SNMPTRAP_COMMUNITY"));
                readConfig.setSnmpTrapVersion(db.getInteger(rs, "SNMPTRAP_VERSION"));                
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

        return readConfig;
    }
   
    public void sysloggingAlarmLog(Integer syslogLevel, String msg) throws OBException
    {
        OBNmsConfig logConfig = new OBNmsConfig();
//        logConfig.readSyslogConfig();// nms 경보 설정 DB에서 읽는다. 
		// 기존 syslog ip만을 처리하는 부분에서 공통 부분으로 변경처리함
        logConfig.readConfig();// nms 경보 설정 DB에서 읽는다.

        try
        {
//            OBSyslog.open(logConfig.getSyslogServerIp(), logConfig.getLogSenderName(), 0);
            // 기존의 syslog ip 하나만 설정되어 있는 부분을 snmp trap 추가로 인해 dto 형식으로 변경처리함.
            OBSyslog.open(logConfig.getReadConfigData().getSyslogServer(), logConfig.getLogSenderName(), 0);
            if(OBSyslog.isOpend() == true) // syslog 가능
            {
                OBSyslog.log(OBSyslogDefine.FACILITY.LOCAL0.getCode(), syslogLevel, msg);
            }
            OBSyslog.close();
        }
        // catch(OBException e)
        // {
        // new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_SYSLOG_INVALID_PROCESS, msg);
        // OBSystemLog.error(OBDefine.LOGFILE_SYSLOG, String.format("Syslog processing fail.(%s)", msg));
        // }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }

    //adcName은 없으면 null이어도 된다.
    public void sendAlarmSnmpTrap(String alarmName, String msg, String adcName, String level) throws OBException
    {
        OBNmsConfig logConfig = new OBNmsConfig();
        logConfig.readConfig();// nms 경보 설정 DB에서 읽는다. //todo - snmp config로 수정한다.
        
        try
        {
            OBSnmpTrap trap = new OBSnmpTrap(logConfig.getReadConfigData().getSnmpTrapVersion(), logConfig.getReadConfigData().getSnmpTrapServerAddress(), logConfig.getReadConfigData().getSnmpTrapPort(), logConfig.getReadConfigData().getSnmpTrapCommunity());
            trap.sendAlarmTrap(alarmName, logConfig.getLogSenderName() + ":" + msg, adcName, level);//adcName은 없으면 null이어도 된다.
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }

     public Timestamp getAlarmLastUpdateTime() throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "start.");
        String sqlText = "";
        OBDatabase db = new OBDatabase();
        try
        {
            db.openDB();
            sqlText =
                    String.format("SELECT OCCUR_TIME FROM MNG_TIME WHERE TYPE = %s;",
                            OBParser.sqlString(OBDefine.MNG_TIME_TYPE_ALARM));

            ResultSet rs;

            rs = db.executeQuery(sqlText);
            if(rs.next() == false)
            {
                return new Timestamp(0L);
            }
            Timestamp result = db.getTimestamp(rs, "OCCUR_TIME");
            OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", result));
            return result;
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

    private void setAlarmLastUpdateTime(OBDatabase db) throws OBException
    {
        String sqlText = "";
        try
        {
            sqlText =
                    String.format(" SELECT OCCUR_TIME FROM MNG_TIME WHERE TYPE = %s;",
                            OBParser.sqlString(OBDefine.MNG_TIME_TYPE_ALARM));

            ResultSet rs = db.executeQuery(sqlText);
            if(rs.next() == false)
            {// insert
                sqlText =
                        String.format(" INSERT INTO MNG_TIME (TYPE, OCCUR_TIME) " + " VALUES (%s, %s); ",
                                OBParser.sqlString(OBDefine.MNG_TIME_TYPE_ALARM), OBParser.sqlString(OBDateTime.now()));
                db.executeUpdate(sqlText);
            }
            else
            {// update
                sqlText =
                        String.format(" UPDATE MNG_TIME SET OCCUR_TIME=%s WHERE TYPE=%s;",
                                OBParser.sqlString(OBDateTime.now()), OBParser.sqlString(OBDefine.MNG_TIME_TYPE_ALARM));
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
    }

    @Override
    public OBDtoAlarmConfig changeAlarmConfigurationToGlobal(Integer adcIndex) throws OBException
    {
        ArrayList<OBDtoAlarmConfig> configList = new ArrayList<OBDtoAlarmConfig>();
        OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);

        try
        {
            OBDtoADCObject object = new OBDtoADCObject(OBDtoADCObject.CATEGORY_ALL, 0, adcInfo.getAdcType()); // type에 맞는 global 설정을 가져오려면 type 필요
            configList = getAlarmConfiguration(object);
            if(configList.size() <= 0)
            {
                throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
            }
            OBDtoAlarmConfig config = configList.get(0);
            config.getObject().setCategory(OBDtoADCObject.CATEGORY_ADC);
            config.getObject().setIndex(adcIndex);
            config.getObject().setVendor(adcInfo.getAdcType());
            config.setConfigLevel(OBDtoADCObject.CATEGORY_ALL);
            config.setAdcType(adcInfo.getAdcType());
            return config;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }

    @Override
    public OBDtoAlarmConfig changeAlarmConfigurationToGroup(Integer adcIndex) throws OBException
    {
        ArrayList<OBDtoAlarmConfig> configList = new ArrayList<OBDtoAlarmConfig>();
        try
        {
            OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
            if(adcInfo == null)
            {
                throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
            }
            OBDtoADCObject object =
                    new OBDtoADCObject(OBDtoADCObject.CATEGORY_GROUP, adcInfo.getGroupIndex(), adcInfo.getAdcType());
            configList = getAlarmConfiguration(object);
            if(configList.size() <= 0)
            {
                throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL);
            }
            OBDtoAlarmConfig config = configList.get(0);
            config.getObject().setCategory(OBDtoADCObject.CATEGORY_ADC);
            config.getObject().setIndex(adcIndex);
            config.getObject().setVendor(adcInfo.getAdcType());
            config.setConfigLevel(OBDtoADCObject.CATEGORY_GROUP);
            config.setAdcType(adcInfo.getAdcType());
            return config;
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
    }

  
}
