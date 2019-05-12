package kr.openbase.adcsmart.service.alarm;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;

import kr.openbase.adcsmart.service.OBSystemAudit;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAlarmAction;
import kr.openbase.adcsmart.service.impl.OBAlarmImpl;
import kr.openbase.adcsmart.service.impl.OBSystemAuditImpl;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBDefineFault;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

class AlarmDto
{
	private int adcIndex;
	private String adcName;
	private OBDefineFault.TYPE fault;
	private Timestamp occurTime;
	private Timestamp alarmTime;
	private Integer type;
	private Integer status;
	private Integer objectType; 
	private String object;
	private String event;
	private String eventEng;
	private int action;

	@Override
	public String toString()
	{
		return "AlarmDto [adcIndex=" + adcIndex + ", adcName=" + adcName
				+ ", fault=" + fault + ", occurTime=" + occurTime
				+ ", alarmTime=" + alarmTime + ", type=" + type + ", status="
				+ status + ", objectType=" + objectType + ", object=" + object
				+ ", event=" + event + ", eventEng=" + eventEng + ", action="
				+ action + "]";
	}
	public int getAdcIndex()
	{
		return adcIndex;
	}
	public void setAdcIndex(int adcIndex)
	{
		this.adcIndex = adcIndex;
	}
	public String getAdcName()
	{
		return adcName;
	}
	public void setAdcName(String adcName)
	{
		this.adcName = adcName;
	}
	public OBDefineFault.TYPE getFault()
	{
		return fault;
	}
	public void setFault(OBDefineFault.TYPE fault)
	{
		this.fault = fault;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public Integer getObjectType()
	{
		return objectType;
	}
	public void setObjectType(Integer objectType)
	{
		this.objectType = objectType;
	}
	public String getObject()
	{
		return object;
	}
	public void setObject(String object)
	{
		this.object = object;
	}
	public String getEvent()
	{
		return event;
	}
	public void setEvent(String event)
	{
		this.event = event;
	}
	public String getEventEng()
	{
		return eventEng;
	}
	public void setEventEng(String eventEng)
	{
		this.eventEng = eventEng;
	}
	public int getAction()
	{
		return action;
	}
	public void setAction(int action)
	{
		this.action = action;
	}
	public Timestamp getAlarmTime()
	{
		return alarmTime;
	}
	public void setAlarmTime(Timestamp alarmTime)
	{
		this.alarmTime = alarmTime;
	}
	public Timestamp getOccurTime()
	{
		return occurTime;
	}
	public void setOccurTime(Timestamp occurTime)
	{
		this.occurTime = occurTime;
	}
}

public class OBAlarmDB
{
    private static final int    MAX_AUDIT_LOG_CONTENT_LENGTH = 950;
	
	
	private Timestamp calculateAlarmTime(String time, String unit, int interval)
	{
		Timestamp simpleTime;
		int fraction = 0;
		int diff = 0;
		
		if(interval == 0)
		{
			return OBDateTime.toTimestamp(time);
		}
		else
		{
			if(unit.equals(OBDefine.TIME_UNIT_MINUTE)) //분단위 입력되는 시간이 60을 넘지 않는다. 시간 안에서 주기가 돈다. 30분이면 한시간에 두번, 45분이면 한시간에 한번 
			{
				simpleTime = OBDateTime.toTimestamp("yyyy-MM-dd HH:mm", time);
				fraction = OBDateTime.getMinuteOfHour(simpleTime);
				diff = (fraction/interval+1)*interval - fraction;
				return OBDateTime.getTimestampWithMinuteOffset(simpleTime, diff); //미래에 alarm을 꺼낼 시간을 계산했다.				
			}
			else if(unit.equals(OBDefine.TIME_UNIT_HOUR)) //시간 단위, 24시간을 넘지 않는다.
			{
				simpleTime = OBDateTime.toTimestamp("yyyy-MM-dd HH", time);
				fraction = OBDateTime.getHourOfDay(simpleTime);
				diff = (fraction/interval+1)*interval+(9%interval) - fraction; //기준 시간을 09시로 한다.
				return OBDateTime.getTimestampWithHourOffset(simpleTime, diff); //미래에 alarm을 꺼낼 시간을 계산했다.
			}
			else
			{
				return null;
			}
		}
	}
	
	/**
	 * 장애 로그를 log_adc_fault 테이블에 기록하고 해당 로그가 경보로 알려야 하는지 판단한다.
	 * 
	 * 
	 * @param adcIndex
	 * @param adcName
	 * @param faultType
	 * @param action
	 * @param status
	 * @param objName
	 * @param vsIndex
	 * @param event
	 * @param eventEng
	 * @param db
	 * @throws OBException
	 */	
	public void writeFaultLog(Integer adcIndex, String adcName, OBDefineFault.TYPE faultType, OBDtoAlarmAction action, int status, String objName, String vsIndex, String event, String eventEng) throws OBException
	{
		String currentTimeStr = OBDateTime.now();
		Timestamp currentTime = OBDateTime.toTimestamp(currentTimeStr);
		String sqlText = "";
        final String delimiter = ",";
		
		OBDatabase db = new OBDatabase();
		try
		{
			db.openDB();
			
			//장애 기록은 필수!!
			final String insertIntoLogAdcFaultQuery = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE)
            .append(" INSERT INTO LOG_ADC_FAULT ")
            .append(" (OCCUR_TIME, UPDATE_TIME, LEVEL, TYPE, ADC_INDEX, ADC_NAME, VS_INDEX, TARGET_OBJECT, EVENT, STATUS ) ")
            .append(" VALUES ( ")
            .append(OBParser.sqlString(currentTimeStr))
            .append(delimiter)
            .append(OBParser.sqlString(currentTimeStr))
            .append(delimiter)
            .append(0)
            .append(delimiter)
            .append(faultType.getCode())
            .append(delimiter)
            .append(adcIndex)
            .append(delimiter)
            .append(OBParser.sqlString(adcName))
            .append(delimiter)
            .append(OBParser.sqlString(StringUtils.abbreviate(vsIndex, MAX_AUDIT_LOG_CONTENT_LENGTH))) // VS INDEX의 길이를 최대 1000으로 조정함
            .append(delimiter)
            .append(OBParser.sqlString(objName))
            .append(delimiter)
            .append(OBParser.sqlString(StringUtils.abbreviate(event, MAX_AUDIT_LOG_CONTENT_LENGTH)))             //EVENT
            .append(delimiter)
            .append(status)
            .append(" );").toString();
			
			db.executeUpdate(insertIntoLogAdcFaultQuery);
			
			if(action.getEnable().equals(1)) //경보가 유효할 때만 예비 경보 테이블로 데이터를 넣는다.
			{
				Timestamp alarmTime = calculateAlarmTime(currentTimeStr, action.getIntervalUnit(), action.getIntervalValue());
				if(action.getIntervalValue()==null || action.getIntervalValue().equals(0)) //기간 요약 설정이 없으면 바로 기록한다.
				{
					ArrayList<AlarmDto> alarmList = new ArrayList<AlarmDto>();
					AlarmDto alarm = new AlarmDto();

					alarm.setAdcIndex(adcIndex);
					alarm.setAdcName(adcName);
					alarm.setFault(faultType);
					alarm.setAction(new OBAlarmImpl().actions2Int(action));
					alarm.setStatus(status);
					alarm.setObject(objName);
					alarm.setEvent(event);
					alarm.setEventEng(eventEng);
					alarm.setOccurTime(currentTime);
					alarm.setAlarmTime(currentTime);
					alarm.setObjectType(faultType.getObjectType());
					alarm.setType(faultType.getCode());
					
					alarmList.add(alarm);
					writeAlarmAndAction(alarmList);
				}
				else
				{
		            final String insertIntoTmpAdcFaultQuery = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE)
		            .append(" INSERT INTO TMP_ADC_FAULT ")
		            .append(" (OCCUR_TIME, UPDATE_TIME, ALARM_TIME, LEVEL, TYPE, ADC_INDEX, ADC_NAME, VS_INDEX, TARGET_OBJECT, EVENT, EVENT_ENG, STATUS, ACTION, ALARM_STATUS ) ")
		            .append(" VALUES ( ")
		            .append(OBParser.sqlString(currentTimeStr))
		            .append(delimiter)
		            .append(OBParser.sqlString(currentTimeStr))
		            .append(delimiter)
		            .append(OBParser.sqlString(alarmTime))
		            .append(delimiter)
		            .append(0)
		            .append(delimiter)
                    .append(faultType.getCode())                   //TYPE
                    .append(delimiter)
                    .append(adcIndex)                              //ADC_INDEX
                    .append(delimiter)
                    .append(OBParser.sqlString(adcName))           //ADC_NAME
                    .append(delimiter)
                    .append(OBParser.sqlString(StringUtils.abbreviate(vsIndex, MAX_AUDIT_LOG_CONTENT_LENGTH))) // VS INDEX의 길이를 최대 1000으로 조정함
                    .append(delimiter)
                    .append(OBParser.sqlString(objName))           //TARGET_OBJECT
                    .append(delimiter)
                    .append(OBParser.sqlString(StringUtils.abbreviate(event, MAX_AUDIT_LOG_CONTENT_LENGTH)))             //EVENT
                    .append(delimiter)
                    .append(OBParser.sqlString(StringUtils.abbreviate(eventEng, MAX_AUDIT_LOG_CONTENT_LENGTH)))          //EVENT_ENG
                    .append(delimiter)
                    .append(status)                                //STATUS
                    .append(delimiter)
                    .append(new OBAlarmImpl().actions2Int(action)) //ACTION
                    .append(delimiter)
                    .append(OBDefine.STATE_DISABLE)                // alarm이 안 나갔음
		            .append(" ) ; ").toString();
		            
					db.executeUpdate(insertIntoTmpAdcFaultQuery);
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
			if(db!=null) db.closeDB();
		}
	}
	
	/**
	 * log_adc_alarm 테이블에 값을 삽입한다.
	 * 
	 * @param alarmList
	 * @throws OBException
	 * @author 최영조
	 */
	void writeAlarmAndAction(ArrayList<AlarmDto> alarmList) throws OBException
	{
		if(alarmList==null || alarmList.size()==0)
		{
			return;
		}
		
		OBAlarmImpl alarmImpl = new OBAlarmImpl();
		int type = 0;
		
		String delimiter = "";		
		StringBuilder valueStringBuilder = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE);
		
		for(AlarmDto alarm:alarmList)
		{
		    OBDtoAlarmAction action = alarmImpl.int2Actions(alarm.getAction());
			if(action.getSyslog().equals(1)) //syslog 설정이 있으면
			{
				try
				{
					//alarmImpl.sysloggingAlarmLog(OBDefineFault.TYPE_ARRAY[alarm.getType()].getLevel().getSyslogLevel(), alarm.getEventEng());
				    alarmImpl.sysloggingAlarmLog(alarm.getType(), alarm.getEventEng());
					//경보 syslog가 전송되었으므로 action을 바꾸지 않는다.
				}
				catch (OBException e)  
				{
					action.setSyslog(0); //경보 syslog 전송이 실패함
					new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_ALARM_ACTION_FAIL, alarm.getAdcName(), "Syslog");
					OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("Sending alarm syslog fail.(%s)", alarm.getEventEng()));
				}
				new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_ALARM_ACTION_SUCCESS, alarm.getAdcName(), "Syslog");
			}
			if(action.getSnmptrap().equals(1)) //
			{
	            try
                {
                    alarmImpl.sendAlarmSnmpTrap(OBDefineFault.TYPE_ARRAY[alarm.getObjectType()].getName(), alarm.getEventEng(), alarm.getAdcName(), //adcName은 없으면 null 이어도 된다. 
                            OBDefineFault.TYPE_ARRAY[alarm.getObjectType()].getLevel().getCaption());
                    //경보 syslog가 전송되었으므로 action을 바꾸지 않는다.
                }
                catch (OBException e)  
                {
                    action.setSnmptrap(0); //경보 snmp trap 전송이 실패함
                    new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_ALARM_ACTION_FAIL, alarm.getAdcName(), "SNMP trap");
                    OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, String.format("Sending alarm SNMP trap fail.(%s)", alarm.getEventEng()));
                }
	            new OBSystemAuditImpl().writeLog(0, "localhost", OBSystemAudit.AUDIT_ALARM_ACTION_SUCCESS, alarm.getAdcName(), "SNMP trap");
			}
			
			if(alarm.getType().equals(OBDefineFault.TYPE.SYSTEM_OFF.getCode()) 
			|| alarm.getType().equals(OBDefineFault.TYPE.SYSTEM_ON.getCode())
			|| alarm.getType().equals(OBDefineFault.TYPE.VIRTUALSRV_DOWN.getCode())
			|| alarm.getType().equals(OBDefineFault.TYPE.VIRTUALSRV_UP.getCode())
			|| alarm.getType().equals(OBDefineFault.TYPE.POOLMEMS_DOWN.getCode())
			|| alarm.getType().equals(OBDefineFault.TYPE.POOLMEMS_UP.getCode())
			|| alarm.getType().equals(OBDefineFault.TYPE.LINKS_DOWN.getCode())
			|| alarm.getType().equals(OBDefineFault.TYPE.LINKS_UP.getCode())
//			|| alarm.getType().equals(OBDefineFault.TYPE.BOOT.getCode())
//			|| alarm.getType().equals(OBDefineFault.TYPE.STANDBY.getCode())
//			|| alarm.getType().equals(OBDefineFault.TYPE.ACTIVE.getCode())
			|| alarm.getType().equals(OBDefineFault.TYPE.REDUNDANCY_ACTIVE.getCode())
			|| alarm.getType().equals(OBDefineFault.TYPE.REDUNDANCY_STANDBY.getCode())
			|| alarm.getType().equals(OBDefineFault.TYPE.VRRP_COLLISION.getCode())
			|| alarm.getType().equals(OBDefineFault.TYPE.GATEWAY_FAIL_DOWN.getCode())
			|| alarm.getType().equals(OBDefineFault.TYPE.GATEWAY_FAIL_UP.getCode())
			)
			{
				type = 0; //장애
			}
			else
			{
				type = 1; //경고
			}
			
			
			// 각 알람들의 구분자
			valueStringBuilder.append(delimiter);
			
            delimiter = ", ";

            valueStringBuilder
            .append("(")
            .append(OBParser.sqlString(alarm.getOccurTime())) // occur time
            .append(delimiter)
            .append(OBParser.sqlString(OBDateTime.now()))     // alarm time
            .append(delimiter)
            .append(alarm.getAdcIndex())                      // adc index
            .append(delimiter)
            .append(OBParser.sqlString(alarm.getAdcName()))   // adc name
            .append(delimiter)
            .append(alarm.getType())                          //class
            .append(delimiter)
            .append(type)                                     //type
            .append(delimiter)
            .append(alarm.getStatus())                        //status
            .append(delimiter)
            .append(alarm.getObjectType())                    //object_type
            .append(delimiter)
            .append(OBParser.sqlString(alarm.getObject()))    // object
            .append(delimiter)
            .append(OBParser.sqlString(alarm.getEvent()))     // event
            .append(delimiter)
            .append(OBParser.sqlString(alarm.getEventEng()))  // event_eng
            .append(delimiter)
            .append(new OBAlarmImpl().actions2Int(action))    // action
            .append(")");
		}

        String sqlText = "";
		OBDatabase db = new OBDatabase();
		try
		{
			db.openDB();
			sqlText = new StringBuilder(OBDefine.MAX_STRING_BUILDER_SIZE)
    			.append(" INSERT INTO LOG_ADC_ALARM ")
    			.append(" ( OCCUR_TIME, ALARM_TIME, ADC_INDEX, ADC_NAME, CLASS, TYPE, STATUS, OBJECT_TYPE, OBJECT, EVENT, EVENT_ENG, ACTION ) ")
    			.append(" VALUES ")
    			.append(valueStringBuilder.toString())
    			.append(" ; ")
    			.toString();

			db.executeUpdate(sqlText);
		}
		catch(SQLException e)
		{
			throw new OBException(OBException.ERRCODE_DB_QEURY, e.getMessage() + ", sql: " + sqlText);
		}		
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
		}
		finally
		{
			if(db!=null) db.closeDB();
		}
	}

}
