package kr.openbase.adcsmart.service.dashboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import kr.openbase.adcsmart.service.database.OBDatabase;
import kr.openbase.adcsmart.service.dto.OBDtoADCObject;
import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoDashboardStatusNotification;
import kr.openbase.adcsmart.service.dto.OBDtoDataObj;
import kr.openbase.adcsmart.service.dto.OBDtoExtraInfo;
import kr.openbase.adcsmart.service.dto.OBDtoSearch;
import kr.openbase.adcsmart.service.dto.OBDtoSystemEnvAdditional;
import kr.openbase.adcsmart.service.dto.OBDtoVSservice;
import kr.openbase.adcsmart.service.dto.dashboard.OBDtoGroupHistory;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultBpsConnInfo;
import kr.openbase.adcsmart.service.impl.OBAdcManagementImpl;
import kr.openbase.adcsmart.service.impl.OBEnvManagementImpl;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBParser;

class OBDynamicDashboardDB
{
    public ArrayList<OBDtoDataObj> getResponseTimeHistoryByIndex(OBDtoADCObject target, OBDtoSearch searchOption, OBDatabase db) throws OBException
	{
		ArrayList<OBDtoDataObj> retVal = new ArrayList<OBDtoDataObj>();		
        String sqlText="";
        
        try
        {   
        	String sqlSearch = makeTimeSqlText(searchOption, "OCCUR_TIME");

        	sqlText =String.format(" SELECT OBJ_INDEX, OCCUR_TIME, RESPONSE_TIME     \n" +
								   " FROM LOG_SVC_PERF_RESP_TIME               \n"+
	    						   " WHERE OBJ_INDEX = %s                            \n",
		   						   OBParser.sqlString(target.getStrIndex()));
        	
        	if(sqlSearch!=null)
        		sqlText += " AND " + sqlSearch;

 			sqlText += " ORDER BY OCCUR_TIME ASC ";

 			ResultSet rs = db.executeQuery(sqlText);
        	
        	while(rs.next())
        	{
        		
        		OBDtoDataObj obj = new OBDtoDataObj();
        		obj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
        		Long respValue = db.getLong(rs, "RESPONSE_TIME");
        		if(respValue == -1)
        			continue;
        		obj.setValue(db.getLong(rs, "RESPONSE_TIME"));
        		
        		retVal.add(obj);
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
        return retVal;
	}
    
	public ArrayList<OBDtoADCObject> getFlbGroupList(OBDtoADCObject target, OBDtoExtraInfo extraInfo, OBDatabase db) throws OBException
	{
		ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();
		String sqlText="";

		try
		{			
			sqlText = String.format(" SELECT INDEX, NAME FROM TMP_SLB_POOL 		 									\n" +
					                " WHERE INDEX IN (SELECT GROUP_INDEX FROM MNG_FLB_GROUP WHERE ADC_INDEX = %d) 	\n" ,
					                target.getIndex());			
			ResultSet rs = db.executeQuery(sqlText);
			while(rs.next())
			{
				OBDtoADCObject log = new OBDtoADCObject();				
				log.setCategory(OBDtoADCObject.CATEGORY_POOLGROUP);				
				log.setStrIndex(db.getString(rs, "INDEX"));
				log.setName(db.getString(rs, "NAME"));				
				retVal.add(log);
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
		return retVal;
	}

	public ArrayList<OBDtoGroupHistory> getVsBpsConcurrentSessionHistoryByIndexGroup(OBDtoADCObject target, OBDtoSearch searchOption, OBDatabase db) throws OBException
    {

	    ArrayList<OBDtoGroupHistory> retVal = new ArrayList<OBDtoGroupHistory>();       
        
        ArrayList<OBDtoADCObject> adcVsInfo = getServiceGroupVSList(target, db);
        
        retVal = getVsBpsConcurrentSessionHistoryByChart(target, searchOption, adcVsInfo, db);
        
        return retVal;
    }
	
    public ArrayList<OBDtoGroupHistory> getVsThroughputHistoryByIndex(OBDtoADCObject target, OBDtoSearch searchOption, OBDatabase db) throws OBException
    {

        ArrayList<OBDtoGroupHistory> retVal = new ArrayList<OBDtoGroupHistory>();
        
        ArrayList<OBDtoADCObject> adcVsInfo = getServiceGroupVSList(target, db);
        
        retVal = getVsBpsHistoryByChart(target, searchOption, adcVsInfo, db);
        
        return retVal;
    }
   
   public ArrayList<OBDtoGroupHistory> getResponseTimeHistoryByIndexGroup(OBDtoADCObject target, OBDtoSearch searchOption, OBDatabase db) throws OBException
   {
     ArrayList<OBDtoGroupHistory> retVal = new ArrayList<OBDtoGroupHistory>();       
     
     ArrayList<OBDtoADCObject> adcVsInfo = getServiceGroupVSList(target, db);
     
     retVal = getResponseTimeHistoryByChart(target, searchOption, adcVsInfo, db);
     
     return retVal;
   }
	
	public ArrayList<OBDtoFaultBpsConnInfo> getVsBpsConcurrentSessionHistoryByIndex(OBDtoADCObject target, OBDtoSearch searchOption, OBDatabase db) throws OBException
	{
		ArrayList<OBDtoFaultBpsConnInfo> retVal = new ArrayList<OBDtoFaultBpsConnInfo>();		
        String sqlText="";
        
        try
        {
        	String index = target.getStrIndex();
        	String sqlSearch = makeTimeSqlText(searchOption, "OCCUR_TIME");
         	sqlText =String.format(" SELECT OBJ_INDEX, OCCUR_TIME, BPS_IN, CONN_CURR, CONN_MAX, CONN_TOT, PPS_IN     \n" +
    							   " FROM LOG_SVC_PERF_STATS                                                 		 \n"+
        						   " WHERE OBJ_INDEX = %s                                                            \n",
        						   OBParser.sqlString(index));
        	
        	if(sqlSearch!=null)
        		sqlText += " AND " + sqlSearch;

 			sqlText += " ORDER BY OCCUR_TIME ASC ";

 			ResultSet rs = db.executeQuery(sqlText);
        	
        	while(rs.next())
        	{
        		OBDtoFaultBpsConnInfo obj = new OBDtoFaultBpsConnInfo();
        		
        		OBDtoDataObj bpsObj = new OBDtoDataObj();
        		bpsObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
        		bpsObj.setValue(db.getLong(rs, "BPS_IN"));

        		OBDtoDataObj ppsObj = new OBDtoDataObj();
        		ppsObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
        		ppsObj.setValue(db.getLong(rs, "PPS_IN"));
        		
        		OBDtoDataObj currObj = new OBDtoDataObj();
        		currObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
        		currObj.setValue(db.getLong(rs, "CONN_CURR"));

        		OBDtoDataObj maxObj = new OBDtoDataObj();
        		maxObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
        		maxObj.setValue(db.getLong(rs, "CONN_MAX"));

        		OBDtoDataObj totObj = new OBDtoDataObj();
        		totObj.setOccurTime(db.getTimestamp(rs, "OCCUR_TIME"));
        		totObj.setValue(db.getLong(rs, "CONN_TOT"));

        		obj.setBpsValue(bpsObj);
        		obj.setPpsValue(ppsObj);
        		obj.setConnCurrValue(currObj);
        		obj.setConnMaxValue(maxObj);
        		obj.setConnTotValue(totObj);
        		
        		retVal.add(obj);
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
        return retVal;
	}
	
	ArrayList<OBDtoGroupHistory> getVsBpsConcurrentSessionHistoryByChart(OBDtoADCObject target, OBDtoSearch searchOption, ArrayList<OBDtoADCObject> adcVsInfo, OBDatabase db) throws OBException
	    {
	       ArrayList<OBDtoGroupHistory> retVal = new ArrayList<OBDtoGroupHistory>();       
	        String sqlText="";
	        
	        try
	        {
	            
                OBDtoSystemEnvAdditional env = new OBEnvManagementImpl().getAdditionalConfig();
                int interval = env.getIntervalAdcConfSync();
	            Integer size = adcVsInfo.size();
                String tableName = "LOG_SVC_PERF_STATS";
                String tableColumn = "CONN_CURR";
                ArrayList<Integer> adcList = makeAdcSql(adcVsInfo);
                String column = makeColumnSql(adcList, "CONN_CURR");
                String sqlSearch = "AND" + makeTimeSqlText(searchOption, "OCCUR_TIME");
                String joinSql = makeTableSql(adcList, adcVsInfo, interval, sqlSearch, tableName, tableColumn);
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
                       obj.setValue1(db.getLong(rs, "A_CONN_CURR"));
                       if(adcVsInfo.get(0).getName()!=null)
                           obj.setName1(adcVsInfo.get(0).getDesciption());
	                }
                   if(size >= 2)
                    {
                       obj.setValue2(db.getLong(rs, "B_CONN_CURR"));
                       if(adcVsInfo.get(1).getName()!=null)
                           obj.setName2(adcVsInfo.get(1).getDesciption());
                    }
                   if(size >= 3)
                   {
                       obj.setValue3(db.getLong(rs, "C_CONN_CURR"));
                       if(adcVsInfo.get(2).getName()!=null)
                           obj.setName3(adcVsInfo.get(2).getDesciption());
                   }
                   if(size >= 4)
                   {
                       obj.setValue4(db.getLong(rs, "D_CONN_CURR"));
                       if(adcVsInfo.get(3).getName()!=null)
                           obj.setName4(adcVsInfo.get(3).getDesciption());
                   }
                   if(size >= 5)
                   {
                       obj.setValue5(db.getLong(rs, "E_CONN_CURR"));
                       if(adcVsInfo.get(4).getName()!=null)
                           obj.setName5(adcVsInfo.get(4).getDesciption());
                   }
                   if(size >= 6)
                   {
                       obj.setValue6(db.getLong(rs, "F_CONN_CURR"));
                       if(adcVsInfo.get(5).getName()!=null)
                           obj.setName6(adcVsInfo.get(5).getDesciption());
                   }
                   if(size >= 7)
                   {
                       obj.setValue7(db.getLong(rs, "G_CONN_CURR"));
                       if(adcVsInfo.get(6).getName()!=null)
                           obj.setName7(adcVsInfo.get(6).getDesciption());
                   }
                   if(size >= 8)
                   {
                       obj.setValue8(db.getLong(rs, "H_CONN_CURR"));
                       if(adcVsInfo.get(7).getName()!=null)
                           obj.setName8(adcVsInfo.get(7).getDesciption());
                   }
                   if(size >= 9)
                   {
                       obj.setValue9(db.getLong(rs, "I_CONN_CURR"));
                       if(adcVsInfo.get(8).getName()!=null)
                           obj.setName9(adcVsInfo.get(8).getDesciption());
                   }
                   if(size >= 10)
                   {
                       obj.setValue10(db.getLong(rs, "J_CONN_CURR"));
                       if(adcVsInfo.get(9).getName()!=null)
                           obj.setName10(adcVsInfo.get(9).getDesciption());
                   }

	                retVal.add(obj);
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
	        return retVal;
	    }
	
	   ArrayList<OBDtoGroupHistory> getVsBpsHistoryByChart(OBDtoADCObject target, OBDtoSearch searchOption, ArrayList<OBDtoADCObject> adcVsInfo, OBDatabase db) throws OBException
       {
	       ArrayList<OBDtoGroupHistory> retVal = new ArrayList<OBDtoGroupHistory>();       
           String sqlText="";
           
           try
           {
               OBDtoSystemEnvAdditional env = new OBEnvManagementImpl().getAdditionalConfig();
               int interval = env.getIntervalAdcConfSync();
               Integer size = adcVsInfo.size();
               String tableName = "LOG_SVC_PERF_STATS";
               String tableColumn = "BPS_IN";
               ArrayList<Integer> adcList = makeAdcSql(adcVsInfo);
               String column = makeColumnSql(adcList, "BPS_IN");
               String sqlSearch = "AND" + makeTimeSqlText(searchOption, "OCCUR_TIME");
               sqlText = " SELECT SELECT A.INTERVAL_TIME ";
               sqlText += column;
               
               String joinSql = makeTableSql(adcList, adcVsInfo, interval, sqlSearch, tableName, tableColumn);
               
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
                      obj.setValue1(db.getLong(rs, "A_BPS_IN"));
                      if(adcVsInfo.get(0).getName()!=null)
                          obj.setName1(adcVsInfo.get(0).getDesciption());
                   }
                  if(size >= 2)
                   {
                      obj.setValue2(db.getLong(rs, "B_BPS_IN"));
                      if(adcVsInfo.get(1).getName()!=null)
                          obj.setName2(adcVsInfo.get(1).getDesciption());
                   }
                  if(size >= 3)
                  {
                      obj.setValue3(db.getLong(rs, "C_BPS_IN"));
                      if(adcVsInfo.get(2).getName()!=null)
                          obj.setName3(adcVsInfo.get(2).getDesciption());
                  }
                  if(size >= 4)
                  {
                      obj.setValue4(db.getLong(rs, "D_BPS_IN"));
                      if(adcVsInfo.get(3).getName()!=null)
                          obj.setName4(adcVsInfo.get(3).getDesciption());
                  }
                  if(size >= 5)
                  {
                      obj.setValue5(db.getLong(rs, "E_BPS_IN"));
                      if(adcVsInfo.get(4).getName()!=null)
                          obj.setName5(adcVsInfo.get(4).getDesciption());
                  }
                  if(size >= 6)
                  {
                      obj.setValue6(db.getLong(rs, "F_BPS_IN"));
                      if(adcVsInfo.get(5).getName()!=null)
                          obj.setName6(adcVsInfo.get(5).getDesciption());
                  }
                  if(size >= 7)
                  {
                      obj.setValue7(db.getLong(rs, "G_BPS_IN"));
                      if(adcVsInfo.get(6).getName()!=null)
                          obj.setName7(adcVsInfo.get(6).getDesciption());
                  }
                  if(size >= 8)
                  {
                      obj.setValue8(db.getLong(rs, "H_BPS_IN"));
                      if(adcVsInfo.get(7).getName()!=null)
                          obj.setName8(adcVsInfo.get(7).getDesciption());
                  }
                  if(size >= 9)
                  {
                      obj.setValue9(db.getLong(rs, "I_BPS_IN"));
                      if(adcVsInfo.get(8).getName()!=null)
                          obj.setName9(adcVsInfo.get(8).getDesciption());
                  }
                  if(size >= 10)
                  {
                      obj.setValue10(db.getLong(rs, "J_BPS_IN"));
                      if(adcVsInfo.get(9).getName()!=null)
                          obj.setName10(adcVsInfo.get(9).getDesciption());
                  }

                   retVal.add(obj);
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
           return retVal;
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
	   
       ArrayList<OBDtoGroupHistory> getResponseTimeHistoryByChart(OBDtoADCObject target, OBDtoSearch searchOption, ArrayList<OBDtoADCObject> adcVsInfo, OBDatabase db) throws OBException
       {
           ArrayList<OBDtoGroupHistory> retVal = new ArrayList<OBDtoGroupHistory>();       
           String sqlText="";
//           System.out.println(adcVsInfo);
           try
           {
               if(adcVsInfo == null)
               {
                   return retVal;
               }
               Integer size = adcVsInfo.size();
               OBDtoSystemEnvAdditional env = new OBEnvManagementImpl().getAdditionalConfig();
               int interval = env.getIntervalAdcConfSync();
               String tableName = "LOG_SVC_PERF_RESP_TIME";
               String tableColumn = "RESPONSE_TIME";
               ArrayList<Integer> adcList = makeAdcSql(adcVsInfo);
               String column = makeColumnSql(adcList, "RESPONSE_TIME");
               String sqlSearch = "AND" + makeTimeSqlText(searchOption, "OCCUR_TIME");
               String joinSql = makeTableSql(adcList, adcVsInfo, interval, sqlSearch, tableName, tableColumn);
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
                       Long respValue = db.getLong(rs, "A_RESPONSE_TIME");
                      obj.setValue1(respValue);
                      if(adcVsInfo.get(0).getName()!=null)
                          obj.setName1(adcVsInfo.get(0).getDesciption());
                   }
                  if(size >= 2)
                   {
                      Long respValue = db.getLong(rs, "B_RESPONSE_TIME");
                      obj.setValue2(respValue);
                      if(adcVsInfo.get(1).getName()!=null)
                          obj.setName2(adcVsInfo.get(1).getDesciption());
                   }
                  if(size >= 3)
                  {
                      Long respValue = db.getLong(rs, "C_RESPONSE_TIME");
                      obj.setValue3(respValue);
                      if(adcVsInfo.get(2).getName()!=null)
                          obj.setName3(adcVsInfo.get(2).getDesciption());
                  }
                  if(size >= 4)
                  {
                      Long respValue = db.getLong(rs, "D_RESPONSE_TIME");
                      obj.setValue4(respValue);
                      if(adcVsInfo.get(3).getName()!=null)
                          obj.setName4(adcVsInfo.get(3).getDesciption());
                  }
                  if(size >= 5)
                  {
                      Long respValue = db.getLong(rs, "E_RESPONSE_TIME");
                      obj.setValue5(respValue);
                      if(adcVsInfo.get(4).getName()!=null)
                          obj.setName5(adcVsInfo.get(4).getDesciption());
                  }
                  if(size >= 6)
                  {
                      Long respValue = db.getLong(rs, "F_RESPONSE_TIME");
                      obj.setValue6(respValue);
                      if(adcVsInfo.get(5).getName()!=null)
                          obj.setName6(adcVsInfo.get(5).getDesciption());
                  }
                  if(size >= 7)
                  {
                      Long respValue = db.getLong(rs, "G_RESPONSE_TIME");
                      obj.setValue7(respValue);
                      if(adcVsInfo.get(6).getName()!=null)
                          obj.setName7(adcVsInfo.get(6).getDesciption());
                  }
                  if(size >= 8)
                  {
                      Long respValue = db.getLong(rs, "H_RESPONSE_TIME");
                      obj.setValue8(respValue);
                      if(adcVsInfo.get(7).getName()!=null)
                          obj.setName8(adcVsInfo.get(7).getDesciption());
                  }
                  if(size >= 9)
                  {
                      Long respValue = db.getLong(rs, "I_RESPONSE_TIME");
                      obj.setValue9(respValue);
                      if(adcVsInfo.get(8).getName()!=null)
                          obj.setName9(adcVsInfo.get(8).getDesciption());
                  }
                  if(size >= 10)
                  {
                      Long respValue = db.getLong(rs, "J_RESPONSE_TIME");
                      obj.setValue10(respValue);
                      if(adcVsInfo.get(9).getName()!=null)
                          obj.setName10(adcVsInfo.get(9).getDesciption());
                  }
                   retVal.add(obj);
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
           return retVal;
       }
	
	private String makeTimeSqlText(OBDtoSearch searchOption, String columnName) throws OBException
	{
		String retVal = "";
		
		if(searchOption==null)
			return retVal;
		
		if(searchOption.getToTime()==null)
			searchOption.setToTime(new Date());
		retVal=String.format(" %s <= %s ", columnName, OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getToTime().getTime()))));

		if(searchOption.getFromTime()==null)
		{
			searchOption.setFromTime(new Date(searchOption.getToTime().getTime()-7*24*60*60*1000));// 7일전 시간.
		}

		retVal+=String.format(" AND %s >= %s ", columnName, OBParser.sqlString(OBDateTime.toString(new Timestamp(searchOption.getFromTime().getTime()))));

				
		return retVal;
	}
	
    private ArrayList<Integer> makeAdcSql(ArrayList<OBDtoADCObject> objIndex) throws OBException
    {
        ArrayList<Integer> retVal = new ArrayList<Integer>();
        
        if(objIndex==null)
            return retVal;
        for(OBDtoADCObject adcObject : objIndex)
        {
            String trim[] = adcObject.getStrIndex().split("_");
            retVal.add(Integer.parseInt(trim[0]));
        }
        return retVal;
    }
    
    private String makeTableSql(ArrayList<Integer> adcList, ArrayList<OBDtoADCObject> adcVsInfo, int interval, String searchOption, String tableName, String column) throws OBException
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
                        " WHERE ADC_INDEX = %d AND OBJ_INDEX = %s %s                                    \n" +
                        " GROUP BY INTERVAL_TIME, %s) %s                                                \n",
                        interval, interval, column, tableName, adcList.get(i), OBParser.sqlString(adcVsInfo.get(i).getStrIndex()), searchOption, column, next);

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
    
    public ArrayList<OBDtoDashboardStatusNotification> getAdcNotificationList(OBDtoADCObject adcObject, OBDatabase db) throws OBException
    {
        ArrayList<OBDtoDashboardStatusNotification> retVal = new ArrayList<OBDtoDashboardStatusNotification>();
        String sqlText="";

            try
            {
                if(adcObject.getCategory() == OBDtoADCObject.CATEGORY_ADC)
                {
                    sqlText = String.format(" SELECT INDEX, NAME, IPADDRESS, DESCRIPTION, TYPE, STATUS          \n" +
                            " FROM MNG_ADC                                                                      \n" +
                            " WHERE INDEX = %d AND AVAILABLE = %d                                               \n" +
                            " ORDER BY NAME ASC                                                                 \n",
                            adcObject.getIndex(), 
                            OBDefine.DATA_AVAILABLE);
                }
                else
                {
                    sqlText = String.format(" SELECT INDEX, NAME, IPADDRESS, DESCRIPTION, TYPE, STATUS          \n" +
                            " FROM MNG_ADC                                                                      \n" +
                            " WHERE GROUP_INDEX = %d AND AVAILABLE = %d                                         \n" +
                            " ORDER BY NAME ASC                                                                 \n",
                            adcObject.getIndex(), 
                            OBDefine.DATA_AVAILABLE);
                }

                ResultSet rs = db.executeQuery(sqlText);
                while(rs.next())
                {
                    OBDtoDashboardStatusNotification log = new OBDtoDashboardStatusNotification();
                    
                    log.setTargetIndex(db.getString(rs, "INDEX"));
                    log.setTargetName(db.getString(rs, "NAME"));
                    log.setTargetIp(db.getString(rs, "IPADDRESS"));
                    log.setStatus(db.getInteger(rs, "STATUS"));
                    
                    retVal.add(log);
                }
                
                if(retVal != null && !retVal.isEmpty())
                {
                    for(OBDtoDashboardStatusNotification adcNotification : retVal)
                    {
                        if(adcNotification.getStatus() == OBDefine.ADC_STATE.AVAILABLE)
                        {
                           sqlText = String.format(" SELECT CLASS                                   \n" +
                                        " FROM LOG_ADC_ALARM                                        \n" +
                                        " WHERE ADC_INDEX = %s AND CLASS IN (27, 32, 33,34, 35)     \n",
                                        adcNotification.getTargetIndex());
                            rs = db.executeQuery(sqlText);
                            if(rs.next())
                            {
                                adcNotification.setStatus(OBDefine.STATUS_CHECK.CAUTION);
                            }
                        }
                    }
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
            return retVal;
    }
    
    public ArrayList<OBDtoDashboardStatusNotification> getVsNotificationList(OBDtoADCObject adcObject, OBDatabase db)  throws OBException
    {
        ArrayList<OBDtoDashboardStatusNotification> retVal = new ArrayList<OBDtoDashboardStatusNotification>();
        String sqlText="";

        try
        {
            if(adcObject.getCategory() == OBDtoADCObject.CATEGORY_VS || adcObject.getCategory() == OBDtoADCObject.CATEGORY_VSVC)
            {
                if(adcObject.getVendor() == OBDefine.ADC_TYPE_ALTEON)
                {
                    
                    sqlText = String.format(" SELECT A.INDEX AS VS_INDEX, COALESCE(NULLIF(B.NAME, ''),                          \n" +
                            " B.VIRTUAL_IP) AS VS_NAME, B.VIRTUAL_IP AS VS_IP, A.VIRTUAL_PORT AS PORT, B.STATUS, A.POOL_INDEX   \n" +
                            " FROM TMP_SLB_VS_SERVICE  A                                                                        \n" +
                            " INNER JOIN TMP_SLB_VSERVER  B                                                                     \n" +
                            " ON A.VS_INDEX = B.INDEX                                                                           \n" +
                            " WHERE A.INDEX = %s                                                                               \n" +
                            " ORDER BY VS_INDEX ASC                                                                             \n",
                            OBParser.sqlString(adcObject.getStrIndex()));
                }
                else
                {
                    sqlText = String.format(" SELECT STATUS AS STATUS, INDEX AS VS_INDEX, COALESCE(NULLIF(NAME, ''),    \n"
                            + " VIRTUAL_IP) AS VS_NAME, VIRTUAL_IP AS VS_IP, VIRTUAL_PORT AS PORT, STATUS, POOL_INDEX   \n"
                            + " FROM TMP_SLB_VSERVER WHERE INDEX = %s                                                   \n"
                            + " ORDER BY VS_INDEX ASC                                                                   \n",
                            OBParser.sqlString(adcObject.getStrIndex()));
                }
                
                ResultSet rs = db.executeQuery(sqlText);
                
                while(rs.next())
                {
                    
                    OBDtoDashboardStatusNotification log = new OBDtoDashboardStatusNotification();
                    log.setTargetName(db.getString(rs, "VS_NAME"));
                    log.setTargetIp(db.getString(rs, "VS_IP") + ":" + db.getInteger(rs, "PORT"));
                    log.setPoolIndex(db.getString(rs, "POOL_INDEX"));
                    log.setTargetIndex(db.getString(rs, "VS_INDEX"));
                    log.setStatus(OBDefine.STATUS_CHECK.AVAILABLE);
                    
                    retVal.add(log);
                }
            }
            else
            {
                sqlText = String.format(" SELECT A.INDEX AS INDEX, A.NAME AS NAME, B.VS_INDEX AS VS_INDEX, B.ADC_INDEX AS ADC_INDEX \n" +
                        " FROM (SELECT INDEX, NAME FROM MNG_VSSERVICE_GROUP WHERE INDEX = %d ) A                                    \n" +
                        " LEFT JOIN (SELECT * FROM MNG_VSSERVICE_GROUP_MAP) B                                                       \n" +
                        " ON A.INDEX = B.GR_INDEX;                                                                                   ",
                        adcObject.getIndex());
                
                ResultSet rs = db.executeQuery(sqlText);
                while(rs.next())
                {
                    
                    OBDtoDashboardStatusNotification log = new OBDtoDashboardStatusNotification();
                    
                    log.setTargetIndex(db.getString(rs, "VS_INDEX"));
                    log.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
                    log.setStatus(OBDefine.STATUS_CHECK.AVAILABLE);
                    
                    retVal.add(log);
                }
                
                for(OBDtoDashboardStatusNotification service : retVal)
                {
                    OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(service.getAdcIndex());//.getAdcType(adcIndex, db);
                    if(adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
                    {
                        sqlText = String.format(" SELECT B.STATUS AS STATUS, A.INDEX AS VSS_INDEX, A.VIRTUAL_IP VS_IP,                  \n" +
                                " COALESCE(NULLIF(A.VIRTUAL_PORT, 0), B.VIRTUAL_PORT) AS PORT, B.POOL_INDEX,                            \n" +
                                " COALESCE(NULLIF(A.NAME, ''), A.VIRTUAL_IP) AS VS_NAME                                                 \n" +
                                " FROM (SELECT * FROM TMP_SLB_VSERVER) A                                                                \n" +
                                " RIGHT JOIN (SELECT * FROM TMP_SLB_VS_SERVICE WHERE INDEX = %s) B                                      \n" +
                                " ON A.INDEX = B.VS_INDEX                                                                               ",
                                OBParser.sqlString(service.getTargetIndex()));
                    }
                    else
                    {
                        sqlText = String.format(" SELECT NAME AS VS_NAME, STATUS, VIRTUAL_IP AS VS_IP, POOL_INDEX,      \n" +
                                " VIRTUAL_PORT AS PORT                                                                  \n" +
                                " FROM TMP_SLB_VSERVER  WHERE INDEX = %s                                                \n",
                                OBParser.sqlString(service.getTargetIndex()));
                    }
                    
                    rs = db.executeQuery(sqlText);
                    
                    while(rs.next())
                    {
                        service.setTargetName(db.getString(rs, "VS_NAME"));
                        service.setTargetIp(db.getString(rs, "VS_IP") + ":" + db.getInteger(rs, "PORT"));
                        service.setPoolIndex(db.getString(rs, "POOL_INDEX"));
                    }
                }
            }
            

            
            for(OBDtoDashboardStatusNotification service : retVal)
            {
                ArrayList<Integer> memberStatus = new ArrayList<Integer>();
                
                sqlText = String.format(" SELECT MEMBER_STATUS FROM                                                         \n" +
                        " (SELECT INDEX FROM TMP_SLB_POOLMEMBER WHERE POOL_INDEX = %s) A                        \n" +
                        " LEFT JOIN (SELECT POOLMEMBER_INDEX, MEMBER_STATUS FROM TMP_SLB_POOLMEMBER_STATUS) B   \n" +
                        " ON A.INDEX = B.POOLMEMBER_INDEX                                                       ",
                        OBParser.sqlString(service.getPoolIndex()));
                
                ResultSet rs2 = db.executeQuery(sqlText);
                while(rs2.next())
                {
                    memberStatus.add(db.getInteger(rs2, "MEMBER_STATUS"));
                }
                if(memberStatus != null || !memberStatus.isEmpty())
                {
                    int norMal = 0;
                    int memberSize = memberStatus.size();
                    for(int i = 0; i < memberSize; i++)
                    {
                
                        if(memberStatus.get(i) == OBDefine.STATUS_CHECK.AVAILABLE)
                        {
                            norMal++;
                        }
                    }
                    
                    if(memberSize == norMal)
                    {
                        service.setStatus(OBDefine.STATUS_CHECK.AVAILABLE);
                    }
                    else if(norMal == 0)
                    {
                        service.setStatus(OBDefine.STATUS_CHECK.WARNING);
                    }
                    else
                    {
                        service.setStatus(OBDefine.STATUS_CHECK.CAUTION);
                    }
                }
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
        return retVal;
    }
    
    public ArrayList<OBDtoADCObject> getServiceGroupVSList(OBDtoADCObject adcObject, OBDatabase db)  throws OBException
    {
        ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();

        String sqlText="";

        try
        {
            
            sqlText = String.format(" SELECT A.INDEX AS INDEX, A.NAME AS NAME, B.VS_INDEX AS VS_INDEX                           \n" +
                    " FROM (SELECT INDEX, NAME FROM MNG_VSSERVICE_GROUP WHERE INDEX = %d ) A                                    \n" +
                    " LEFT JOIN (SELECT * FROM MNG_VSSERVICE_GROUP_MAP) B                                                       \n" +
                    " ON A.INDEX = B.GR_INDEX                                                                                   \n",
                    adcObject.getIndex());
            
            ResultSet rs = db.executeQuery(sqlText);
            while(rs.next())
            {
                OBDtoADCObject log = new OBDtoADCObject();
                
                if(db.getString(rs, "VS_INDEX")==null)
                {
                    continue;
                }
                
                log.setStrIndex(db.getString(rs, "VS_INDEX"));
                log.setName(db.getString(rs, "NAME"));
                log.setAlteonId(db.getString(rs, "VS_INDEX"));
                retVal.add(log);
            }
            
            for(OBDtoADCObject vs : retVal)
            {
                String adc[] = vs.getStrIndex().split("_");
                int adcIndex = Integer.parseInt(adc[0]);
                int adcType = new OBAdcManagementImpl().getAdcType(adcIndex);
                
                if(OBDefine.ADC_TYPE_ALTEON == adcType)
                {
                    sqlText = String.format(" SELECT B.STATUS AS STATUS, A.INDEX AS VSS_INDEX, A.VIRTUAL_IP VS_IP,                      \n" +
                            " COALESCE(NULLIF(A.VIRTUAL_PORT, 0), B.VIRTUAL_PORT) AS PORT                                               \n" +
                            " FROM (SELECT * FROM TMP_SLB_VSERVER) A                                                                    \n" +
                            " RIGHT JOIN (SELECT * FROM TMP_SLB_VS_SERVICE WHERE INDEX = %s) B                                           \n" +
                            " ON A.INDEX = B.VS_INDEX                                                                                   ",
                            OBParser.sqlString(vs.getStrIndex()));
                    
                    rs = db.executeQuery(sqlText);
                    while(rs.next())
                    {
                        if(db.getString(rs, "VS_IP") == null)
                        {
                            continue;
                        }
                        
                        vs.setDesciption(db.getString(rs, "VS_IP") + ":" + db.getInteger(rs, "PORT"));
                        vs.setStatus(db.getInteger(rs, "STATUS"));
                        vs.setPort(db.getInteger(rs, "PORT"));
                    }
                }
                else
                {
                    sqlText = String.format(" SELECT STATUS, VIRTUAL_IP AS VS_IP,                \n" +
                            " VIRTUAL_PORT AS PORT                                               \n" +
                            " FROM TMP_SLB_VSERVER  WHERE INDEX = %s                             \n",
                            OBParser.sqlString(vs.getStrIndex()));
                    
                    rs = db.executeQuery(sqlText);
                    while(rs.next())
                    {
                        if(db.getString(rs, "VS_IP") == null)
                        {
                            continue;
                        }
                        
                        vs.setDesciption(db.getString(rs, "VS_IP") + ":" + db.getInteger(rs, "PORT"));
                        vs.setStatus(db.getInteger(rs, "STATUS"));
                        vs.setPort(db.getInteger(rs, "PORT"));
                    }
                }
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
        return retVal;
    }
    
    
    
    public ArrayList<OBDtoDashboardStatusNotification> getMemberNotificationList(OBDtoADCObject adcObject, OBDatabase db)  throws OBException
    {
        ArrayList<OBDtoDashboardStatusNotification> retVal = new ArrayList<OBDtoDashboardStatusNotification>();
        String sqlText="";
        
        try
        {
            
                String memberList = adcObject.getDesciption().toString();
                memberList = OBParser.convertCommaSqlInList(memberList);
                sqlText = String.format(" SELECT C.INDEX AS INDEX, COALESCE(NULLIF(C.PORT, 0), A.REAL_PORT) AS PORT,                   \n" +
                         " D.MEMBER_STATUS AS STATE, E.IP_ADDRESS AS IP_ADDRESS                                                        \n" +
                         " FROM (SELECT * FROM TMP_SLB_VS_SERVICE) A                                                                   \n" +
                         " LEFT JOIN (SELECT * FROM TMP_SLB_POOL) B                                                                    \n" +
                         " ON A.POOL_INDEX = B.INDEX                                                                                   \n" +
                         " RIGHT JOIN (SELECT * FROM TMP_SLB_POOLMEMBER WHERE INDEX IN (%s)) C                                         \n" +
                         " ON B.INDEX = C.POOL_INDEX                                                                                   \n" +
                         " LEFT JOIN (SELECT * FROM TMP_SLB_POOLMEMBER_STATUS) D                                                       \n" +
                         " ON C.INDEX = D.POOLMEMBER_INDEX                                                                             \n" +
                         " LEFT JOIN (SELECT * FROM TMP_SLB_NODE) E                                                                    \n" +
                         " ON C.NODE_INDEX = E.INDEX                                                                                   \n" +
                         " GROUP BY C.INDEX, C.PORT, A.REAL_PORT, D.MEMBER_STATUS, E.IP_ADDRESS                                        \n" +
                         " ORDER BY IP_ADDRESS ASC                                                                                     \n",
                        memberList);
            
            ResultSet rs = db.executeQuery(sqlText);
            while(rs.next())
            {
                if(db.getString(rs, "INDEX") == null)
                {
                    continue;
                }
                OBDtoDashboardStatusNotification log = new OBDtoDashboardStatusNotification();
                
                log.setTargetIndex(db.getString(rs, "INDEX"));
                log.setTargetIp(db.getString(rs, "IP_ADDRESS") + ":" + db.getInteger(rs, "PORT"));              
                log.setStatus(db.getInteger(rs, "STATE"));
                retVal.add(log);
            }
            
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
        return retVal;
    }
    

    

    
    public ArrayList<OBDtoADCObject> getADCVSMemberList(OBDtoADCObject adcObject, OBDatabase db)  throws OBException
    {
        ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();
        String sqlText="";
        String arg[] = adcObject.getStrIndex().split("_");
        Integer adcIndex = Integer.parseInt(arg[0]);
        OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(adcIndex);//.getAdcType(adcIndex, db);
        adcObject.setVendor(adcInfo.getAdcType());
        
        try
        {

            if(adcObject.getVendor() == OBDefine.ADC_TYPE_ALTEON)
            {
                sqlText = String.format(" SELECT D.INDEX AS INDEX, C.ADC_INDEX AS ADC_INDEX, F.IP_ADDRESS AS IP_ADDRESS,        \n" +
                        " COALESCE(NULLIF(D.PORT, 0), B.REAL_PORT) AS PORT, E.MEMBER_STATUS AS STATE                            \n" +
                        " FROM (SELECT INDEX, NAME, ADC_INDEX FROM TMP_SLB_VSERVER) A                                           \n" +
                        " RIGHT JOIN (SELECT * FROM TMP_SLB_VS_SERVICE  WHERE INDEX = %s) B                                     \n" +
                        " ON A.INDEX = B.VS_INDEX                                                                               \n" +
                        " LEFT JOIN (SELECT * FROM TMP_SLB_POOL) C                                                              \n" +
                        " ON B.POOL_INDEX = C.INDEX                                                                             \n" +
                        " LEFT JOIN (SELECT * FROM TMP_SLB_POOLMEMBER) D                                                        \n" +
                        " ON C.INDEX = D.POOL_INDEX                                                                             \n" +
                        " LEFT JOIN (SELECT * FROM TMP_SLB_POOLMEMBER_STATUS) E                                                 \n" +
                        " ON D.INDEX = E.POOLMEMBER_INDEX                                                                       \n" +
                        " LEFT JOIN (SELECT * FROM TMP_SLB_NODE) F                                                              \n" +
                        " ON D.NODE_INDEX = F.INDEX                                                                             \n" +
                        " GROUP BY  D.INDEX, C.ADC_INDEX, F.IP_ADDRESS, D.PORT, B.REAL_PORT, E.MEMBER_STATUS                    ",
                        OBParser.sqlString(adcObject.getStrIndex()));
            }
            else
            {
                sqlText = String.format(" SELECT C.INDEX AS INDEX, E.IP_ADDRESS AS IP_ADDRESS,                                 \n" +
                        " C.PORT AS PORT, D.MEMBER_STATUS AS STATE                                                             \n" +
                        " FROM (SELECT * FROM TMP_SLB_VSERVER WHERE INDEX = %s) A                                              \n" +
                        " LEFT JOIN (SELECT * FROM TMP_SLB_POOL) B                                                             \n" +
                        " ON A.POOL_INDEX = B.INDEX                                                                            \n" +
                        " LEFT JOIN (SELECT * FROM TMP_SLB_POOLMEMBER) C                                                       \n" +
                        " ON B.INDEX = C.POOL_INDEX                                                                            \n" +
                        " LEFT JOIN (SELECT * FROM TMP_SLB_POOLMEMBER_STATUS) D                                                \n" +
                        " ON C.INDEX = D.POOLMEMBER_INDEX                                                                      \n" +
                        " LEFT JOIN (SELECT * FROM TMP_SLB_NODE) E                                                             \n" +
                        " ON C.NODE_INDEX = E.INDEX                                                                            \n" +
                        " GROUP BY C.INDEX, D.POOLMEMBER_INDEX,  E.IP_ADDRESS, C.PORT, D.MEMBER_STATUS                          ",
                        OBParser.sqlString(adcObject.getStrIndex()));
            }
            
            ResultSet rs = db.executeQuery(sqlText);
            while(rs.next())
            {
                OBDtoADCObject log = new OBDtoADCObject();
                
                log.setStrIndex(db.getString(rs, "INDEX"));
                log.setDesciption(db.getString(rs, "IP_ADDRESS") + ":" + db.getInteger(rs, "PORT"));              
                log.setStatus(db.getInteger(rs, "STATE"));
              
              retVal.add(log);
            }
            
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
        return retVal;
    }
    
    public ArrayList<OBDtoADCObject> getServiceGroupMemberList(OBDtoADCObject adcObject, OBDatabase db)  throws OBException
    {
        ArrayList<OBDtoADCObject> retVal = new ArrayList<OBDtoADCObject>();
        ArrayList<OBDtoVSservice> adcList = new ArrayList<OBDtoVSservice>();
        String sqlText="";
        
        try
        {
            sqlText = String.format(" SELECT A.INDEX AS INDEX, A.NAME AS NAME, B.VS_INDEX AS VS_INDEX, B.ADC_INDEX AS ADC_INDEX \n" +
                    " FROM (SELECT INDEX, NAME FROM MNG_VSSERVICE_GROUP WHERE INDEX = %d ) A                                    \n" +
                    " LEFT JOIN (SELECT * FROM MNG_VSSERVICE_GROUP_MAP) B                                                       \n" +
                    " ON A.INDEX = B.GR_INDEX;                                                                                   ",
                    adcObject.getIndex());
            
            ResultSet rs = db.executeQuery(sqlText);
            while(rs.next())
            {
                OBDtoVSservice adc = new OBDtoVSservice();
                
                adc.setAdcIndex(db.getInteger(rs, "ADC_INDEX"));
                adc.setVsIndex(db.getString(rs, "VS_INDEX"));
                adc.setGroupIndex(db.getInteger(rs, "INDEX"));
                
                adcList.add(adc);
            }
            
            for(OBDtoVSservice vSservice : adcList)
            {
                OBDtoAdcInfo adcInfo = new OBAdcManagementImpl().getAdcInfo(vSservice.getAdcIndex());
                if(adcInfo.getAdcType() == OBDefine.ADC_TYPE_ALTEON)
                {
                    sqlText = String.format(" SELECT D.INDEX AS INDEX, C.ADC_INDEX AS ADC_INDEX, F.IP_ADDRESS AS IP_ADDRESS,        \n" +
                            " COALESCE(NULLIF(D.PORT, 0), B.REAL_PORT) AS PORT, E.MEMBER_STATUS AS STATE                            \n" +
                            " FROM (SELECT INDEX, NAME, ADC_INDEX FROM TMP_SLB_VSERVER) A                                           \n" +
                            " RIGHT JOIN (SELECT * FROM TMP_SLB_VS_SERVICE  WHERE INDEX = %s) B                                      \n" +
                            " ON A.INDEX = B.VS_INDEX                                                                               \n" +
                            " LEFT JOIN (SELECT * FROM TMP_SLB_POOL) C                                                              \n" +
                            " ON B.POOL_INDEX = C.INDEX                                                                             \n" +
                            " LEFT JOIN (SELECT * FROM TMP_SLB_POOLMEMBER) D                                                        \n" +
                            " ON C.INDEX = D.POOL_INDEX                                                                             \n" +
                            " LEFT JOIN (SELECT * FROM TMP_SLB_POOLMEMBER_STATUS) E                                                 \n" +
                            " ON D.INDEX = E.POOLMEMBER_INDEX                                                                       \n" +
                            " LEFT JOIN (SELECT * FROM TMP_SLB_NODE) F                                                              \n" +
                            " ON D.NODE_INDEX = F.INDEX                                                                             \n" +
                            "  GROUP BY  D.INDEX , C.ADC_INDEX, F.IP_ADDRESS, D.PORT, B.REAL_PORT, E.MEMBER_STATUS                  ",
                            OBParser.sqlString(vSservice.getVsIndex()));
                }
                else
                {
                    sqlText = String.format(" SELECT C.INDEX AS INDEX, E.IP_ADDRESS AS IP_ADDRESS,                                  \n" +
                             " C.PORT AS PORT, D.MEMBER_STATUS AS STATE                                                             \n" +
                             " FROM (SELECT * FROM TMP_SLB_VSERVER WHERE INDEX = %s) A                                              \n" +
                             " LEFT JOIN (SELECT * FROM TMP_SLB_POOL) B                                                             \n" +
                             " ON A.POOL_INDEX = B.INDEX                                                                            \n" +
                             " LEFT JOIN (SELECT * FROM TMP_SLB_POOLMEMBER) C                                                       \n" +
                             " ON B.INDEX = C.POOL_INDEX                                                                            \n" +
                             " LEFT JOIN (SELECT * FROM TMP_SLB_POOLMEMBER_STATUS) D                                                \n" +
                             " ON C.INDEX = D.POOLMEMBER_INDEX                                                                      \n" +
                             " LEFT JOIN (SELECT * FROM TMP_SLB_NODE) E                                                             \n" +
                             " ON C.NODE_INDEX = E.INDEX                                                                            \n" +
                             " GROUP BY  C.INDEX, E.IP_ADDRESS, C.PORT, D.MEMBER_STATUS                                             ",
                            OBParser.sqlString(vSservice.getVsIndex()));
                }


                rs = db.executeQuery(sqlText);
                while(rs.next())
                {
                    if(db.getString(rs, "INDEX") == null)
                    {
                        continue;
                    }
                    OBDtoADCObject log = new OBDtoADCObject();
                    
                    log.setStrIndex(db.getString(rs, "INDEX"));
                    log.setDesciption(db.getString(rs, "IP_ADDRESS") + ":" + db.getString(rs, "PORT"));
                    log.setStatus(db.getInteger(rs, "STATE"));
                  
                    retVal.add(log);
                }
            }

            
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
        return retVal;
    }
}
