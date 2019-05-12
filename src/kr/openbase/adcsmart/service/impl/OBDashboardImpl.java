package kr.openbase.adcsmart.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kr.openbase.adcsmart.service.OBDashboard;
import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcStatusCount;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSummary;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSummaryStatus;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSystemLog;
import kr.openbase.adcsmart.service.dto.OBDtoConnection;
import kr.openbase.adcsmart.service.dto.OBDtoCpu;
import kr.openbase.adcsmart.service.dto.OBDtoMemory;
import kr.openbase.adcsmart.service.dto.OBDtoStatusSummary;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.dto.OBDtoThroughput;
import kr.openbase.adcsmart.service.dto.OBDtoUsageConnection;
import kr.openbase.adcsmart.service.dto.OBDtoUsageCpu;
import kr.openbase.adcsmart.service.dto.OBDtoUsageMem;
import kr.openbase.adcsmart.service.dto.OBDtoUsageThroughput;
import kr.openbase.adcsmart.service.dto.OBDtoVSMonitorLog;
import kr.openbase.adcsmart.service.dto.OBDtoVservStatusCount;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoGroupHistory;
import kr.openbase.adcsmart.service.dto.history.OBDtoAdcConfigHistory;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBDefineFault;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBDashboardImpl implements OBDashboard
{

//	public static void main(String[] args)
//	{
//		OBDashboardImpl board = new OBDashboardImpl();
//		
//		try
//		{
//			OBDtoStatusSummary list = board.getStatusSummary(1);
//			System.out.println(list);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//	
//		}
//	}
	
	@Override
	public OBDtoStatusSummary getStatusSummary(Integer accountIndex) throws OBException
	{
		OBDtoStatusSummary summary = new OBDtoStatusSummary();
		OBDtoAdcStatusCount adcCount = new OBDtoAdcStatusCount();
		OBDtoVservStatusCount vservCount = new OBDtoVservStatusCount();
		
		OBDatabase db = new OBDatabase();
		
		String sqlText="";
		try
		{
			db.openDB();
			
			adcCount = new OBMonitoringImpl().GetAdcStatusCount(accountIndex, db);
			summary.setAdcCount(adcCount.getAdc());
			summary.setAdcCountAvail(adcCount.getAdcAvail());
			summary.setAdcCountUnavail(adcCount.getAdcUnavail());
			
			vservCount = new OBMonitoringImpl().GetVservStatusCount(accountIndex, db);
			summary.setVsCount(vservCount.getVs());
			summary.setVsCountAvail(vservCount.getVsAvail());
			summary.setVsCountDisable(vservCount.getVsDisable());
			summary.setVsCountUnavali(vservCount.getVsUnavail());

			sqlText=String.format(
					" SELECT CPU_USAGE, MEM_USAGE, HDD_USAGE " +
					" FROM LOG_SYSTEM_RESOURCES " +
					" ORDER BY OCCUR_TIME DESC LIMIT 1"
			);
			
			summary.setSysCpuUsage(0);
			summary.setSysHddUsage(0);
			summary.setSysMemUsage(0);
			ResultSet rs = db.executeQuery(sqlText);
			if(rs.next()==true)
			{
				summary.setSysCpuUsage(db.getInteger(rs, "CPU_USAGE"));
				summary.setSysHddUsage(db.getInteger(rs, "HDD_USAGE"));
				summary.setSysMemUsage(db.getInteger(rs, "MEM_USAGE"));
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
		return summary;
	}

//	public static void main(String[] args)
//	{
//		OBDashboardImpl dashboard = new OBDashboardImpl();
//		try
//		{
//			testAlert(8);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public ArrayList<OBDtoVSMonitorLog> getVSConfigLog(Integer accountIndex, Integer logCount) throws OBException
	{
		ArrayList<OBDtoVSMonitorLog> result = new  ArrayList<OBDtoVSMonitorLog>();

		OBDatabase db = new OBDatabase();

		String sqlText="";
		try
		{
			db.openDB();
			
			HashMap<String, Integer> statusMap = getVSStatusAll(accountIndex, db);
			String sqlLogcount="";
			if(logCount==null)
			{
				int count = new OBEnvManagementImpl().getLogViewCount(db);
				if(count==0)
				{
					count = 20;
				}
				sqlLogcount=String.format(" LIMIT %d ", count);
			}
			else
			{
				if(logCount.equals(0)==false)
				{
					sqlLogcount=String.format(" LIMIT %d ", logCount.intValue());
				}
			}
			
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
			if(roleNo==null)
			{
                throw new OBException(OBException .ERRCODE_SYSTEM_INVALIDDATA);
			}

			if(roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN))
			{
				sqlText=String.format(
						" SELECT A.OCCUR_TIME, A.ADC_INDEX, A.VS_INDEX, A.VS_NAME, A.VS_IP, C.TYPE, C.NAME   \n" +
						" FROM LOG_CONFIG_HISTORY A                                                          \n" +
						" INNER JOIN MNG_ADC C                                                               \n" +
						" ON A.ADC_INDEX=C.INDEX                                                             \n" +
						" WHERE A.VS_INDEX IN (SELECT INDEX FROM TMP_SLB_VSERVER) AND                        \n" + //살아 있는 VS의 정보만 꺼내는 조건, 삭제된 것도 표시하려면 막아야 함 //where-in:empty string 불가, null 불가, OK
						" 		A.LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY GROUP BY VS_INDEX) \n" + //where-in:empty string 불가, null 불가, OK
						" ORDER BY A.OCCUR_TIME DESC ");
			}
			else
			{
				ArrayList<Integer> adcList = new OBAccountImpl().getInvolvedAdcList(accountIndex);
				String adcText="-1";
				for(Integer index:adcList)
				{
					if(!adcText.isEmpty())
						adcText += ", ";
					adcText += index;
				}
				sqlText=String.format(
						" SELECT A.OCCUR_TIME, A.ADC_INDEX, A.VS_INDEX, A.VS_NAME, A.VS_IP, C.TYPE, C.NAME       \n" +
						" FROM LOG_CONFIG_HISTORY A                                                              \n" +
						" INNER JOIN MNG_ADC C                                                                   \n" +
						" ON A.ADC_INDEX=C.INDEX                                                                 \n" +
						" WHERE	A.VS_INDEX IN (SELECT INDEX FROM TMP_SLB_VSERVER)                                \n" + //살아 있는 VS의 정보만 꺼내는 조건, 삭제된 것도 표시하려면 막아야 함 //where-in:empty string 불가, null 불가, OK
						" 	    AND A.LOG_SEQ IN (SELECT MAX(LOG_SEQ) FROM LOG_CONFIG_HISTORY GROUP BY VS_INDEX) \n" + //where-in:empty string 불가, null 불가, OK
						"       AND A.ADC_INDEX IN ( %s )                                                        \n" + //where-in:empty string 불가, null 불가, OK
						" ORDER BY A.OCCUR_TIME DESC ",
						 adcText);			
			}
			sqlText += sqlLogcount;
			sqlText += ";";

			ResultSet rs = db.executeQuery(sqlText);

			while(rs.next())
			{
				OBDtoVSMonitorLog log = new OBDtoVSMonitorLog();
				log.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				log.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				log.setAdcType(db.getInteger(rs, "TYPE"));
				log.setVsIndex(db.getString(rs, "VS_INDEX"));
				log.setVsIPAddress(db.getString(rs, "VS_IP"));
				log.setVsName(db.getString(rs, "VS_NAME"));
				log.setAdcName(db.getString(rs, "NAME"));
				log.setVsStatus(statusMap.get(log.getVsIndex()));
				result.add(log);
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
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "result = " + result.toString());
		return result;
	}

//	public static void main(String[] args)
//	{
//		OBDashboardImpl board = new OBDashboardImpl();
//		
//		try
//		{
//			ArrayList<OBDtoAdcSystemLog> list = board.getAdcSystemFaultLog(1, 10);
//			for(OBDtoAdcSystemLog log:list)
//				System.out.println(log);
//			System.out.println(list.size());
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//	
//		}
//	}
	
	@Override
	public ArrayList<OBDtoAdcSystemLog> getAdcSystemFaultLog(Integer accountIndex, Integer logCount) throws OBException
	{
		ArrayList<OBDtoAdcSystemLog> result = new ArrayList<OBDtoAdcSystemLog>();
		OBDatabase db = new OBDatabase();

		String sqlText="";
		try
		{
			db.openDB();
			
			ArrayList<Integer> adcList = new OBAccountImpl().getInvolvedAdcList(accountIndex);
			String adcText="-1";
			for(Integer index:adcList)
			{
				if(!adcText.isEmpty())
					adcText += ", ";
				adcText += index;
			}

			String sqlLogcount="";
			if(logCount==null)
			{
				int count = new OBEnvManagementImpl().getLogViewCount(db);
				if(count==0)
					count=20;
				sqlLogcount=String.format(" LIMIT %d ", count);
			}
			else
			{
				if(logCount.equals(0)==false)
					sqlLogcount=String.format(" LIMIT %d ", logCount.intValue());
			}
			
			sqlText=String.format(" SELECT " +
									 " OCCUR_TIME, UPDATE_TIME, LEVEL, TYPE, ADC_INDEX, ADC_NAME, VS_INDEX, EVENT, STATUS " +
									 " FROM LOG_ADC_FAULT " +
									 " WHERE ADC_INDEX IN ( %s ) " +  //where-in:empty string 불가, null 불가, OK
									 " ORDER BY OCCUR_TIME DESC ",
									 adcText);					

			sqlText += sqlLogcount;
			sqlText += ";";
			ResultSet rs = db.executeQuery(sqlText);
			while(rs.next())
			{
				OBDtoAdcSystemLog log = new OBDtoAdcSystemLog();
				log.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				log.setStatus(db.getInteger(rs, "STATUS"));
				if(log.getStatus().equals(OBDefineFault.STATUS_UNSOLVED)==false) //미해결 장애가 아니면(해결/경고) 해결 시각을 준다.
				{
					log.setFinishTime(OBDateTime.toDate(db.getTimestamp(rs, "UPDATE_TIME")));
				}
				else	//미해결 장애는 해결 시각이 null
				{
					log.setFinishTime(null);					
				}
				log.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
				log.setAdcName(db.getString(rs, "ADC_NAME"));
				log.setEvent(db.getString(rs, "EVENT"));
				log.setLogLevel(db.getInteger(rs, "LEVEL"));
				log.setLogType(db.getInteger(rs, "TYPE"));
//				log.setServicePort(db.getInteger(rs, "SERVICE_PORT"));
				log.setStatus(db.getInteger(rs, "STATUS"));
				log.setVsIndex(db.getString(rs, "VS_INDEX"));
				result.add(log);
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
		return result;
	}

//	public static void main(String[] args)
//	{
//		OBDashboardImpl board = new OBDashboardImpl();
//		
//		try
//		{
//			Date curr = new Date();
//			ArrayList<OBDtoUsageConnection> list = board.getUsageConnections(1, "1_1", 80, curr, curr, 10);
//			System.out.println(list.size());
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//	
//		}
//	}
	
	@Override
	/**
	 * virtual service의  connection 정보를 읽는다. service 정보라서 port까지 파라미터로 받는다. 
	 */
	public OBDtoConnection getUsageConnections(Integer adcIndex, String vsIndex, Integer virtPort,
			Date beginTime, Date endTime, Integer logCount) throws OBException
	{
		OBDtoConnection result = new OBDtoConnection();
		ArrayList<OBDtoUsageConnection> connectionList = new ArrayList<OBDtoUsageConnection>();
		ArrayList<OBDtoAdcConfigHistory> confEventList;
		try
		{
			confEventList = new OBAdcConfigHistoryImpl().getAdcConfigHistoryListNoPagingNoSearch(adcIndex, beginTime, endTime);
		}
		catch(OBException e)
		{
			throw e;
		}

		result.setConfEventList(confEventList);
		result.setConnectionList(connectionList);
		
		OBDatabase db = new OBDatabase();
		
		String sqlText="";
		try
		{
			db.openDB();
			
			String sqlTime="";
			if(endTime==null)
				endTime=new Date();
			
			if(beginTime==null)
			{
				long count = new OBEnvManagementImpl().getLogViewPeriod(db);
				if(count==0)
					count = 6*60*60*1000;// 6시간.
				beginTime = new Date(endTime.getTime()-count*60*60*1000);// 
			}
			
			sqlTime=String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s ", 
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))), 
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime())))
			);

			String vsvcIndex = OBCommon.makeVSvcIndex(adcIndex, vsIndex, virtPort);
			sqlText=String.format(" SELECT OCCUR_TIME, CONN_CURR                                  \n" +
					              " FROM LOG_SVC_PERF_STATS                                 \n" +
								  " WHERE ADC_INDEX=%d AND OBJ_INDEX=%s AND %s                    \n" +
								  " ORDER BY OCCUR_TIME ASC;                                      \n",
								  adcIndex, 
								  OBParser.sqlString(vsvcIndex), 
								  sqlTime);
			//OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "sqlText = " + sqlText);

			ResultSet rs = db.executeQuery(sqlText);
			while(rs.next())
			{
				OBDtoUsageConnection conns = new OBDtoUsageConnection();
				conns.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				conns.setConns(db.getLong(rs, "CONN_CURR"));
				connectionList.add(conns);
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
		return result;
	}

	@Override
	/**
	 * virtual server 별 connection
	 */
	public OBDtoConnection getUsageConnections(Integer adcIndex, String vsIndex,
			Date beginTime, Date endTime, Integer logCount) throws OBException
	{
		
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, "vs start");

		OBDtoConnection result = new OBDtoConnection();
		ArrayList<OBDtoUsageConnection> connectionList = new ArrayList<OBDtoUsageConnection>();
		ArrayList<OBDtoAdcConfigHistory> confEventList;
		try
		{
			confEventList = new OBAdcConfigHistoryImpl().getAdcConfigHistoryListNoPagingNoSearch(adcIndex, beginTime, endTime);
		}
		catch(OBException e)
		{
			throw e;
		}

		result.setConfEventList(confEventList);
		result.setConnectionList(connectionList);
		
		OBDatabase db = new OBDatabase();
		
		String sqlText="";
		try
		{
			db.openDB();
			
			String sqlTime="";
			if(endTime==null)
				endTime=new Date();

			if(beginTime==null)
			{
				long count = new OBEnvManagementImpl().getLogViewPeriod(db);
				if(count==0)
					count = 6*60*60*1000;// 6시간.
				beginTime = new Date(endTime.getTime()-count*60*60*1000);// 
			}
			sqlTime=String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));	
			
			sqlText=String.format(
					" SELECT OCCUR_TIME, CONN_CURR                \n" +
					" FROM LOG_SVC_PERF_STATS               \n" +
					" WHERE ADC_INDEX=%d AND OBJ_INDEX=%s AND %s  \n" +
					" ORDER BY OCCUR_TIME ASC;                    \n",
					adcIndex,
					OBParser.sqlString(vsIndex), sqlTime);
			
			ResultSet rs = db.executeQuery(sqlText);

			while(rs.next())
			{
				OBDtoUsageConnection conns = new OBDtoUsageConnection();
				conns.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				conns.setConns(db.getLong(rs, "CONN_CURR"));
				connectionList.add(conns);
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
		
		return result;
	}

//	public static void main(String[] args)
//	{
//		OBDashboardImpl board = new OBDashboardImpl();
//		
//		try
//		{
//			ArrayList<OBDtoUsageConnection> list = board.getUsageConnections(1, null, null, 10);
//			System.out.println(list.size());
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//	
//		}
//	}
	
	@Override
	public OBDtoConnection getUsageConnections(Integer adcIndex, Date beginTime, Date endTime, Integer logCount) throws OBException
	{ 
		OBDtoConnection result = new OBDtoConnection();
		ArrayList<OBDtoUsageConnection> connectionList = new ArrayList<OBDtoUsageConnection>();
		ArrayList<OBDtoAdcConfigHistory> confEventList;
		try
		{
			confEventList = new OBAdcConfigHistoryImpl().getAdcConfigHistoryListNoPagingNoSearch(adcIndex, beginTime, endTime);
		}
		catch(OBException e)
		{
			throw e;
		}

		result.setConfEventList(confEventList);
		result.setConnectionList(connectionList);
		
		OBDatabase db = new OBDatabase();
		
		String sqlText="";
		try
		{
			db.openDB();
			
			String sqlTime="";
			if(endTime==null)
			{
				endTime=new Date();
			}
			if(beginTime==null)
			{
				long count = new OBEnvManagementImpl().getLogViewPeriod(db);
				if(count==0)
					count = 6*60*60*1000;// 6시간.
				beginTime = new Date(endTime.getTime()-count*60*60*1000);// 
			}
			sqlTime=String.format(" OCCUR_TIME <= %s  AND OCCUR_TIME >= %s ", 
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));	

			sqlText=String.format(" SELECT OCCUR_TIME, CONN_CURR \n" +
									" FROM LOG_ADC_PERF_STATS          \n" +
									" WHERE ADC_INDEX=%d AND %s       \n" +
									" ORDER BY OCCUR_TIME ASC         ;",
									adcIndex, 
									sqlTime);
//			System.out.println(sqlText);
			long startA = System.currentTimeMillis();
			ResultSet rs = db.executeQuery(sqlText);
			long endA = System.currentTimeMillis();
			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, "<time>sql run: " + (endA - startA)/1000.0 );
			while(rs.next())
			{
				OBDtoUsageConnection conns = new OBDtoUsageConnection();
				conns.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				conns.setConns(db.getLong(rs, "CONN_CURR"));
				connectionList.add(conns);
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
		return result;
	}

//	public static void main(String[] args)
//	{
//		OBDashboardImpl board = new OBDashboardImpl();
//		
//		try
//		{
//			ArrayList<OBDtoUsageCpu> list = board.getUsageCpu(3, null, null, 10);
//			for(OBDtoUsageCpu cpu:list)
//				System.out.println(cpu);
//			System.out.println(list.size());
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//	
//		}
//	}
	
	@Override
	public OBDtoCpu getUsageCpu(Integer adcIndex, Date beginTime, Date endTime, Integer logCount) throws OBException
	{
		OBDtoCpu result = new OBDtoCpu();
		ArrayList<OBDtoUsageCpu> cpuList = new ArrayList<OBDtoUsageCpu>();
		ArrayList<OBDtoAdcConfigHistory> confEventList;
		try
		{
			confEventList = new OBAdcConfigHistoryImpl().getAdcConfigHistoryListNoPagingNoSearch(adcIndex, beginTime, endTime);
		}
		catch(OBException e)
		{
			throw e;
		}

		result.setConfEventList(confEventList);
		result.setCpuList(cpuList);
		
		OBDatabase db = new OBDatabase();
		String sqlText="";
		try
		{
			db.openDB();
			
			String sqlTime="";
			if(endTime==null)
				endTime=new Date();

			if(beginTime==null)
			{
				long count = new OBEnvManagementImpl().getLogViewPeriod(db);
				if(count==0)
					count = 6*60*60*1000;// 6시간.
				beginTime = new Date(endTime.getTime()-count*60*60*1000);// 
			}
			sqlTime=String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s ", 
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));	

			sqlText=String.format(
					" SELECT OCCUR_TIME, CPU1_USAGE  \n" +
					" FROM LOG_RESC_CPUMEM        \n" +
					" WHERE ADC_INDEX=%d AND %s     \n" +
					" ORDER BY OCCUR_TIME ASC ;    \n",
					adcIndex,
					sqlTime);

			ResultSet rs = db.executeQuery(sqlText);

			while(rs.next())
			{
				OBDtoUsageCpu cpu = new OBDtoUsageCpu();
				cpu.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				cpu.setUsage(db.getInteger(rs, "CPU1_USAGE"));
				cpuList.add(cpu);
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

		return result;
	}

    @Override
    public ArrayList<OBDtoGroupHistory> getUsageCpuGroup(Integer adcGroupIndex, Date beginTime, Date endTime, Integer logCount) throws OBException
    {
       ArrayList<OBDtoGroupHistory> retVal = new ArrayList<OBDtoGroupHistory>();       
       String sqlText="";
       ArrayList<Integer> adcList = new OBAdcManagementImpl().getAdcIndexListInGroup(adcGroupIndex);
       ArrayList<String> adcName = new ArrayList<String>();
       for(Integer adcIndex : adcList)
       {
            OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
            adcName.add(adcInfo.getName());
       }

       OBDatabase db = new OBDatabase();

        try
        {
            db.openDB();
            
            String sqlTime="";
            if(endTime==null)
                endTime=new Date();

            if(beginTime==null)
            {
                long count = new OBEnvManagementImpl().getLogViewPeriod(db);
                if(count==0)
                    count = 6*60*60*1000;// 6시간.
                beginTime = new Date(endTime.getTime()-count*60*60*1000);// 
            }
            sqlTime=String.format(" AND OCCUR_TIME <= %s AND OCCUR_TIME >= %s ", 
                    OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
                    OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));   
            
            OBDtoSystemEnvAdditional env = new OBEnvManagementImpl().getAdditionalConfig();
            int interval = env.getIntervalAdcConfSync();
            
            Integer size = adcList.size();
            String tableName = "LOG_RESC_CPUMEM";
            String tableColumn = "CPU1_USAGE";
            String column = makeColumnSql(adcList, "CPU1_USAGE");
            String joinSql = makeTableSql(adcList, interval, sqlTime, tableName, tableColumn);
            sqlText = " SELECT SELECT A.INTERVAL_TIME ";
            sqlText += column;
            
            sqlText =String.format(" SELECT A.INTERVAL_TIME %s FROM                             \n" +
                    " %s                                                                        \n" +
                    " ORDER BY INTERVAL_TIME ASC                                                \n",
                    
                    column, joinSql);

            ResultSet rs = db.executeQuery(sqlText);

            while(rs.next())
            {
                OBDtoGroupHistory obj = new OBDtoGroupHistory();
                obj.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "INTERVAL_TIME")));
                if(size >= 1)
                {
                   obj.setValue1(db.getLong(rs, "A_CPU1_USAGE"));
                   if(adcName.get(0)!=null)
                       obj.setName1(adcName.get(0));
                }
               if(size >= 2)
                {
                   obj.setValue2(db.getLong(rs, "B_CPU1_USAGE"));
                   if(adcName.get(1)!=null)
                       obj.setName2(adcName.get(1));
                }
               if(size >= 3)
               {
                   obj.setValue3(db.getLong(rs, "C_CPU1_USAGE"));
                   if(adcName.get(2)!=null)
                       obj.setName3(adcName.get(2));
               }
               if(size >= 4)
               {
                   obj.setValue4(db.getLong(rs, "D_CPU1_USAGE"));
                   if(adcName.get(3)!=null)
                       obj.setName4(adcName.get(3));
               }
               if(size >= 5)
               {
                   obj.setValue5(db.getLong(rs, "E_CPU1_USAGE"));
                   if(adcName.get(4)!=null)
                       obj.setName5(adcName.get(4));
               }
               if(size >= 6)
               {
                   obj.setValue6(db.getLong(rs, "F_CPU1_USAGE"));
                   if(adcName.get(5)!=null)
                       obj.setName6(adcName.get(5));
               }
               if(size >= 7)
               {
                   obj.setValue7(db.getLong(rs, "G_CPU1_USAGE"));
                   if(adcName.get(6)!=null)
                       obj.setName7(adcName.get(6));
               }
               if(size >= 8)
               {
                   obj.setValue8(db.getLong(rs, "H_CPU1_USAGE"));
                   if(adcName.get(7)!=null)
                       obj.setName8(adcName.get(7));
               }
               if(size >= 9)
               {
                   obj.setValue9(db.getLong(rs, "I_CPU1_USAGE"));
                   if(adcName.get(8)!=null)
                       obj.setName9(adcName.get(8));
               }
               if(size >= 10)
               {
                   obj.setValue10(db.getLong(rs, "J_CPU1_USAGE"));
                   if(adcName.get(9)!=null)
                       obj.setName10(adcName.get(9));
               }

                retVal.add(obj);
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

        return retVal;
    }

//	public static void main(String[] args)
//	{
//		OBDashboardImpl board = new OBDashboardImpl();
//		
//		try
//		{
//			ArrayList<OBDtoUsageMem> list = board.getUsageMem(1, null, null, 10);
//			for(OBDtoUsageMem mem:list)
//				System.out.println(mem);
//			System.out.println(list.size());
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public OBDtoMemory getUsageMem(Integer adcIndex, Date beginTime, Date endTime, Integer logCount) throws OBException
	{
		OBDtoMemory result = new OBDtoMemory();
		ArrayList<OBDtoUsageMem> memList = new ArrayList<OBDtoUsageMem>();
		ArrayList<OBDtoAdcConfigHistory> confEventList;
		try
		{
			confEventList = new OBAdcConfigHistoryImpl().getAdcConfigHistoryListNoPagingNoSearch(adcIndex, beginTime, endTime);
		}
		catch(OBException e)
		{
			throw e;
		}

		result.setConfEventList(confEventList);
		result.setMemList(memList);		
		
		OBDatabase db = new OBDatabase();
		String sqlText="";
		try
		{
			db.openDB();
			
			String sqlTime="";
			if(endTime==null)
			{
				endTime=new Date();
			}

			if(beginTime==null)
			{
				long count = new OBEnvManagementImpl().getLogViewPeriod(db);
				if(count==0)
					count = 6*60*60*1000;// 6시간.
				beginTime = new Date(endTime.getTime()-count*60*60*1000);// 
			}
			sqlTime=String.format(" OCCUR_TIME <= %s  AND OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText=String.format(" SELECT OCCUR_TIME, MEM_USAGE  " +
//					" SELECT OCCUR_TIME, /*MEM_TOTAL, MEM_USED,*/ MEM_USAGE  " +
									" FROM LOG_RESC_CPUMEM                             " +
									" WHERE ADC_INDEX=%d AND %s                          " +
									" ORDER BY OCCUR_TIME ASC ;                         ",
									adcIndex, sqlTime);

			ResultSet rs = db.executeQuery(sqlText);
			
			while(rs.next())
			{
				OBDtoUsageMem mem = new OBDtoUsageMem();
				mem.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
//				mem.setTotal(db.getLong(rs, "MEM_TOTAL"));
				mem.setUsage(db.getInteger(rs, "MEM_USAGE"));
//				mem.setUsed(db.getLong(rs, "MEM_USED"));
				memList.add(mem);
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

		return result;
	}
	
    @Override
    public ArrayList<OBDtoGroupHistory> getUsageMemGroup(Integer adcGroupIndex, Date beginTime, Date endTime, Integer logCount) throws OBException
    {
       ArrayList<OBDtoGroupHistory> retVal = new ArrayList<OBDtoGroupHistory>();       
       String sqlText="";
       ArrayList<Integer> adcList = new OBAdcManagementImpl().getAdcIndexListInGroup(adcGroupIndex);
       ArrayList<String> adcName = new ArrayList<String>();
       for(Integer adcIndex : adcList)
       {
            OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);
            adcName.add(adcInfo.getName());
       }
       
       OBDatabase db = new OBDatabase();

        try
        {
            db.openDB();
            
            String sqlTime="";
            if(endTime==null)
                endTime=new Date();

            if(beginTime==null)
            {
                long count = new OBEnvManagementImpl().getLogViewPeriod(db);
                if(count==0)
                    count = 6*60*60*1000;// 6시간.
                beginTime = new Date(endTime.getTime()-count*60*60*1000);// 
            }
            sqlTime=String.format(" AND OCCUR_TIME <= %s AND OCCUR_TIME >= %s ", 
                    OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
                    OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));   
            
            OBDtoSystemEnvAdditional env = new OBEnvManagementImpl().getAdditionalConfig();
            int interval = env.getIntervalAdcConfSync();

            Integer size = adcList.size();
            String tableName = "LOG_RESC_CPUMEM";
            String tableColumn = "MEM_USAGE";
            String column = makeColumnSql(adcList, "MEM_USAGE");
            String joinSql = makeTableSql(adcList, interval, sqlTime, tableName, tableColumn);
            
            
            sqlText =String.format(" SELECT A.INTERVAL_TIME %s FROM                             \n" +
                    " %s                                                                        \n" +
                    " ORDER BY INTERVAL_TIME ASC                                                \n",
                    
                    column, joinSql);
            
            ResultSet rs = db.executeQuery(sqlText);

            while(rs.next())
            {
                OBDtoGroupHistory obj = new OBDtoGroupHistory();
                obj.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "INTERVAL_TIME")));
                if(size >= 1)
                {
                   obj.setValue1(db.getLong(rs, "A_MEM_USAGE"));
                   if(adcName.get(0)!=null)
                       obj.setName1(adcName.get(0));
                }
               if(size >= 2)
                {
                   obj.setValue2(db.getLong(rs, "B_MEM_USAGE"));
                   if(adcName.get(1)!=null)
                       obj.setName2(adcName.get(1));
                }
               if(size >= 3)
               {
                   obj.setValue3(db.getLong(rs, "C_MEM_USAGE"));
                   if(adcName.get(2)!=null)
                       obj.setName3(adcName.get(2));
               }
               if(size >= 4)
               {
                   obj.setValue4(db.getLong(rs, "D_MEM_USAGE"));
                   if(adcName.get(3)!=null)
                       obj.setName4(adcName.get(3));
               }
               if(size >= 5)
               {
                   obj.setValue5(db.getLong(rs, "E_MEM_USAGE"));
                   if(adcName.get(4)!=null)
                       obj.setName5(adcName.get(4));
               }
               if(size >= 6)
               {
                   obj.setValue6(db.getLong(rs, "F_MEM_USAGE"));
                   if(adcName.get(5)!=null)
                       obj.setName6(adcName.get(5));
               }
               if(size >= 7)
               {
                   obj.setValue7(db.getLong(rs, "G_MEM_USAGE"));
                   if(adcName.get(6)!=null)
                       obj.setName7(adcName.get(6));
               }
               if(size >= 8)
               {
                   obj.setValue8(db.getLong(rs, "H_MEM_USAGE"));
                   if(adcName.get(7)!=null)
                       obj.setName8(adcName.get(7));
               }
               if(size >= 9)
               {
                   obj.setValue9(db.getLong(rs, "I_MEM_USAGE"));
                   if(adcName.get(8)!=null)
                       obj.setName9(adcName.get(8));
               }
               if(size >= 10)
               {
                   obj.setValue10(db.getLong(rs, "J_MEM_USAGE"));
                   if(adcName.get(9)!=null)
                       obj.setName10(adcName.get(9));
               }

                retVal.add(obj);
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

        return retVal;
    }

//	public static void main(String[] args)
//	{
//		OBDashboardImpl board = new OBDashboardImpl();
//		
//		try
//		{
//			ArrayList<OBDtoUsageThroughput> list = board.getUsageThroughput(3, "3_server_terminal", null, null, 10);
//			for(OBDtoUsageThroughput throughput:list)
//				System.out.println(throughput);
//			System.out.println(list.size());
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	

//	public static void main(String[] args)
//	{
//		OBDashboardImpl board = new OBDashboardImpl();
//		
//		try
//		{
//			ArrayList<OBDtoUsageThroughput> list = board.getUsageThroughput(1, null, null, 10);
//			for(OBDtoUsageThroughput throughput:list)
//				System.out.println(throughput);
//			System.out.println(list.size());
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 지정된 adc장비에서 사용된 throughput 추이 그래프를 위한 데이터를 제공한다. 
	 * @param adcIndex : adc 장비의 인덱스. null 불가.
	 * @param beginTime : 조회하고자하는 시작 날짜. 
	 * @param endTime : 조회하고자하는 종료 날짜. null일 경우에는 현재 시점으로 간주.
	 * @param logCount : 조회하고자하는 데이터 개수. null일 경우에는 최대 100개만 제공. 0일 경우에는 모든 데이터 제공.
	 * @return ArrayList<OBDtoUsageThroughput>
	 * @throws OBException
	 */	
	@Override
	public OBDtoThroughput getUsageThroughput(Integer adcIndex, Date beginTime, Date endTime, Integer logCount) throws OBException
	{ 
		OBDtoThroughput result = new OBDtoThroughput();
		ArrayList<OBDtoUsageThroughput> throughputList = new ArrayList<OBDtoUsageThroughput>();
		ArrayList<OBDtoAdcConfigHistory> confEventList;
		try
		{
			confEventList = new OBAdcConfigHistoryImpl().getAdcConfigHistoryListNoPagingNoSearch(adcIndex, beginTime, endTime);
		}
		catch(OBException e)
		{
			throw e;
		}

		result.setConfEventList(confEventList);
		result.setThroughputList(throughputList);	
		
		OBDatabase db = new OBDatabase();
		String sqlText="";
		try
		{
			db.openDB();
			
			String sqlTime="";
			if(endTime==null)
				endTime=new Date();

			if(beginTime==null)
			{
				long count = new OBEnvManagementImpl().getLogViewPeriod(db);
				if(count==0)
					count = 6*60*60*1000;// 6시간.
				beginTime = new Date(endTime.getTime()-count*60*60*1000);// 
			}

			sqlTime=String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s ",
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));

			sqlText=String.format(" SELECT OCCUR_TIME, BPS_IN, PKT_IN  \n" +
								" FROM LOG_ADC_PERF_STATS             \n" +
								" WHERE ADC_INDEX=%d AND %s          \n" +
								" ORDER BY OCCUR_TIME ASC ;         \n",
								adcIndex,
								sqlTime);
			
			ResultSet rs = db.executeQuery(sqlText);

			while(rs.next())
			{
				OBDtoUsageThroughput throughput = new OBDtoUsageThroughput();
				throughput.setBps(db.getLong(rs, "BPS_IN"));
				throughput.setPps(db.getLong(rs, "PKT_IN"));
				throughput.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				throughputList.add(throughput);
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
		return result;
	}

//	public static void main(String[] args)
//	{
//		OBDashboardImpl board = new OBDashboardImpl();
//		
//		try
//		{
//			OBDatabase db = new OBDatabase();
//			db.openDB();
//			HashMap<String, Integer> list = board.getVSStatusAll(1, db);
//			System.out.println(list);
//			System.out.println(list.size());
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	//	public static void main(String[] args)
	//	{
	//		OBDashboardImpl board = new OBDashboardImpl();
	//		
	//		try
	//		{
	//			ArrayList<OBDtoUsageThroughput> list = board.getUsageThroughput(3, "3_server_terminal", null, null, 10);
	//			for(OBDtoUsageThroughput throughput:list)
	//				System.out.println(throughput);
	//			System.out.println(list.size());
	//		}
	//		catch(Exception e)
	//		{
	//			e.printStackTrace();
	//		}
	//	}
		
		@Override
		public OBDtoThroughput getUsageThroughput(Integer adcIndex, String vsIndex,	Date beginTime, Date endTime, Integer logCount) throws OBException
		{
			OBDtoThroughput result = new OBDtoThroughput();
			ArrayList<OBDtoUsageThroughput> throughputList = new ArrayList<OBDtoUsageThroughput>();
			ArrayList<OBDtoAdcConfigHistory> confEventList;
			try
			{
				confEventList = new OBAdcConfigHistoryImpl().getAdcConfigHistoryListNoPagingNoSearch(adcIndex, beginTime, endTime);
			}
			catch(OBException e)
			{
				throw e;
			}
	
			result.setConfEventList(confEventList);
			result.setThroughputList(throughputList);	
			
			OBDatabase db = new OBDatabase();
			String sqlText="";
			try
			{
				db.openDB();
				
				String sqlTime="";
				if(endTime==null)
					endTime=new Date();

				if(beginTime==null)
				{
					long count = new OBEnvManagementImpl().getLogViewPeriod(db);
					if(count==0)
						count = 6*60*60*1000;// 6시간.
					beginTime = new Date(endTime.getTime()-count*60*60*1000);// 
				}
				sqlTime=String.format(" OCCUR_TIME <= %s  AND OCCUR_TIME >= %s ", 
						OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
						OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));	
				
				sqlText=String.format(
						" SELECT OCCUR_TIME, BPS_IN, PPS_IN, BPS_OUT, PPS_OUT \n" +
						" FROM LOG_SVC_PERF_STATS                       \n" +
						" WHERE ADC_INDEX=%d AND OBJ_INDEX=%s AND %s          \n" +
						" ORDER BY OCCUR_TIME ASC ;",
						adcIndex,
						OBParser.sqlString(vsIndex),
						sqlTime);

				ResultSet rs = db.executeQuery(sqlText);
	
				while(rs.next())
				{
					OBDtoUsageThroughput throughput = new OBDtoUsageThroughput();
					throughput.setBps(db.getLong(rs, "BPS_IN")+db.getLong(rs, "BPS_OUT"));
					throughput.setPps(db.getLong(rs, "PPS_IN")+db.getLong(rs, "PPS_OUT"));
					throughput.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
					throughputList.add(throughput);
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
			return result;
		}

	//이 dashboard에서는 virtual service별 throuput을 쓰지 않지만, getUsageThroughput이 모두 여기 있으므로, 이 곳에 코드를 쓴다.
	//getUsageConnections도 adc, virtual server, virtual service각각 만들어져 있으므로 throughput도 이 함수까지 있으면 짝이 맞는다.
	public OBDtoThroughput getUsageThroughput(Integer adcIndex, String vsIndex, Integer virtPort, Date beginTime, Date endTime) throws OBException
	{ 
		OBDtoThroughput result = new OBDtoThroughput();
		ArrayList<OBDtoUsageThroughput> throughputList = new ArrayList<OBDtoUsageThroughput>();
		ArrayList<OBDtoAdcConfigHistory> confEventList = new OBAdcConfigHistoryImpl().getAdcConfigHistoryListNoPagingNoSearch(adcIndex, beginTime, endTime);
		
		result.setConfEventList(confEventList);
		result.setThroughputList(throughputList);
		
		OBDatabase db = new OBDatabase();
		
		String sqlText="";
		try
		{
			db.openDB();
			
			String sqlTime="";
			if(endTime==null)
				endTime=new Date();
			
			if(beginTime==null)
			{
				long count = new OBEnvManagementImpl().getLogViewPeriod(db);
				if(count==0)
					count = 6*60*60*1000;// 6시간.
				beginTime = new Date(endTime.getTime()-count*60*60*1000);// 
			}

			sqlTime=String.format(" OCCUR_TIME <= %s AND OCCUR_TIME >= %s ", 
					OBParser.sqlString(OBDateTime.toString(new Timestamp(endTime.getTime()))),
					OBParser.sqlString(OBDateTime.toString(new Timestamp(beginTime.getTime()))));	
			
			String vsvcIndex = OBCommon.makeVSvcIndex(adcIndex, vsIndex, virtPort);
			
			sqlText=String.format(  //SERVICE테이블은 ALTEON만 해당하는데, ALTEON은 IN/OUT을 나누지 않아서 IN필드만 있다.
					" SELECT OCCUR_TIME, BPS_IN                                        \n" + 
					" FROM LOG_SVC_PERF_STATS                                    \n" +
					" WHERE ADC_INDEX=%d AND OBJ_INDEX=%s AND %s                       \n" +
					" ORDER BY OCCUR_TIME ASC;                                         \n",
					adcIndex,
					OBParser.sqlString(vsvcIndex),
					sqlTime);

			ResultSet rs = db.executeQuery(sqlText);

			while(rs.next())
			{
				OBDtoUsageThroughput throughput = new OBDtoUsageThroughput();
				throughput.setBps(db.getLong(rs, "BPS_IN"));
				throughput.setPps(0);
				throughput.setOccurTime(OBDateTime.toDate(db.getTimestamp(rs, "OCCUR_TIME")));
				throughputList.add(throughput);
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
		return result;
	}

	/**
	 * 주어진 계정에 등록된 ADC 장비의 VS상태를 추출하여 HashMap 형태로 제공한다. Hash의 키는 virtual server의 Index, value는 status.
	 * ADC virtual server의 서버별 status 정보를 추출할때 사용된다.
	 * 
	 * @param accountIndex : 로그인 사용자 계정. null 불가.
	 * @param db
	 * @return HashMap<String, Integer>
	 */
	private HashMap<String, Integer> getVSStatusAll(Integer accountIndex, OBDatabase db) throws OBException
	{
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		
		String sqlText="";
		try
		{
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
			if(roleNo==null)
			{
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("accountIndex \"%d\" role error.", accountIndex));
			}
			
			if(roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN))
			{
				sqlText=String.format(" SELECT INDEX, STATUS FROM TMP_SLB_VSERVER ");				
			}
			else
			{
				ArrayList<Integer> adcList = new OBAccountImpl().getInvolvedAdcList(accountIndex);
				String adcText="-1";
				for(Integer index:adcList)
				{
					if(!adcText.isEmpty())
						adcText += ", ";
					adcText += index;
				}				
				sqlText=String.format(" SELECT INDEX, STATUS " +
									  " FROM TMP_SLB_VSERVER " +
									  " WHERE ADC_INDEX IN ( %s ) ",  //where-in:empty string 불가, null 불가, OK
									 adcText);
				
			}
			ResultSet rs = db.executeQuery(sqlText);
			while(rs.next())
			{
				result.put(db.getString(rs, "INDEX"), db.getInteger(rs, "STATUS"));
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
		return result;
	}

//	public static void main(String[] args)
//	{
//		OBDashboardImpl board = new OBDashboardImpl();
//		
//		try
//		{
//			ArrayList<OBDtoAdcSummary> list = board.getAdcSummary(1);
//			System.out.println(list);
//		}
//		catch(OBException e)
//		{
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public ArrayList<OBDtoAdcSummary> getAdcSummary(Integer accountIndex) throws OBException
	{
		ArrayList<OBDtoAdcSummary> result = new ArrayList<OBDtoAdcSummary>();
		OBDatabase db = new OBDatabase();
		OBDatabase db2 = new OBDatabase();
		
		String sqlText="";
		try
		{
			db.openDB();
			db2.openDB();
			
			Integer roleNo = new OBAccountImpl().getAccountRoleNo(accountIndex);
			if(roleNo==null)
			{
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "roleNo");
			}

			String sqlSub;
			
			if(roleNo.equals(OBDefine.ACCNT_ROLE_ADMIN)) //admin //if(adcList.size()==0)// admin 계정.
			{
				sqlSub = String.format(" ( " +
										" SELECT " +
										" GROUP_INDEX, COUNT(GROUP_INDEX) AS COUNT " +
										" FROM MNG_ADC " +
										" WHERE AVAILABLE = 1 " +
										" GROUP BY GROUP_INDEX " +
										" ) ");
			}
			else
			{
				ArrayList<Integer> adcList = new OBAccountImpl().getInvolvedAdcList(accountIndex);
				
				String adcText="-1";
				for(Integer index:adcList)
				{
					if(!adcText.isEmpty())
						adcText += ", ";
					adcText += index;
				}
				sqlSub = String.format(" ( " +
										" SELECT " +
										" GROUP_INDEX, COUNT(GROUP_INDEX) AS COUNT " +
										" FROM MNG_ADC " +
										" WHERE AVAILABLE = 1 " +
										" AND INDEX IN ( %s ) " +  //where-in:empty string 불가, null 불가, OK
										" GROUP BY GROUP_INDEX " +
										" ) ", 
										adcText);

			}
			
			sqlText = String.format(" SELECT A.GROUP_INDEX, A.COUNT, B.NAME \n" +
									" FROM %s A                             \n" +
									" INNER JOIN MNG_ADC_GROUP B            \n" +
									" ON A.GROUP_INDEX=B.INDEX              \n" +
									" ORDER BY B.NAME ;                     \n"
									, sqlSub);
			
			ResultSet rs = db.executeQuery(sqlText);
			while(rs.next())
			{
				OBDtoAdcSummary summary = new OBDtoAdcSummary();
				
				summary.setGroupName(db.getString(rs, "NAME"));
				summary.setAdcList(getAdcSummaryStatus(db.getInteger(rs, "GROUP_INDEX"), db2));
				
				result.add(summary);
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
			if(db2!=null) db2.closeDB();
		}
		return result;
	}
	
	private ArrayList<OBDtoAdcSummaryStatus> getAdcSummaryStatus(Integer groupIndex, OBDatabase db) throws OBException
	{
		ArrayList<OBDtoAdcSummaryStatus> result = new ArrayList<OBDtoAdcSummaryStatus>();
		
		String sqlText="";
		try
		{
			sqlText = String.format(" SELECT " +
											" INDEX, NAME, STATUS " +
											" FROM MNG_ADC " +
											" WHERE GROUP_INDEX=%d AND AVAILABLE=1; ",
											groupIndex);
			ResultSet rs = db.executeQuery(sqlText);
			while(rs.next())
			{
				OBDtoAdcSummaryStatus status = new OBDtoAdcSummaryStatus();
				status.setAdcIndex(db.getInteger(rs, "INDEX"));
				status.setAdcName(db.getString(rs, "NAME"));
				status.setStatus(db.getInteger(rs, "STATUS"));
				
				result.add(status);
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

		return result;
	}
	
    private String makeColumnSql(ArrayList<Integer> adcIndex, String columnName) throws OBException
    {
        String retVal = "";
        int ascii = 65;
        if(adcIndex==null)
            return retVal;
        int size = adcIndex.size();
        for(int i =0; i < size; i++)
        {
            String next = Character.toString ((char) ascii);
            retVal+=String.format(" , %s.%s AS %s_%s\n",
                    next, columnName, next, columnName);
            ascii++;
        }
        
        return retVal;
    }
    
    private String makeTableSql(ArrayList<Integer> adcList, int interval, String searchOption, String tableName, String column) throws OBException
    {
        String retVal = "";
        if(adcList==null)
            return retVal;
        int ascii = 65;
        int size = adcList.size();
        for(int i = 0; i < size; i++)
        {

            String next = Character.toString ((char) ascii);
            
            String join = 
                    String.format(" \n( SELECT                                                          \n" +
                        " (to_timestamp(floor((extract('epoch' from OCCUR_TIME) / %d )) * %d) )         \n" +
                        " AS INTERVAL_TIME, %s                                                          \n" +                                        
                        " FROM %s                                                                       \n" +
                        " WHERE ADC_INDEX = %d %s                                                       \n" +
                        " GROUP BY INTERVAL_TIME, %s) %s                                                \n",
                        interval, interval, column, tableName, adcList.get(i), searchOption, column, next);

            if(i > 0)
            {
                join += String.format("  ON A.INTERVAL_TIME = %s.INTERVAL_TIME \n", next);
            }
            
            if(i+1 != size)
            {
                join += " \n LEFT JOIN \n";
            }
            
            retVal += join;
            ascii++;
        }
        
        return retVal;
    }
}