package kr.openbase.adcsmart.service.utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class OBLocalConfig {
//    public static void main(String args[])
//    {
//        try
//        {
//            System.out.println(OBLocalConfig.getData(OBLocalConfig.CONFIG_KEY_GET_TIME_METHOD_DB));
//        }
//        catch( Exception e ) 
//        { 
//        }
//    }

	private static Properties msgProps = null;// = LoggerFactory.getLogger(AlertFacade.class);

	public final static String CONFIG_KEY_GET_TIME_METHOD_DB = "applytime.dbmode";

	public synchronized static String getData(String key) {
		try {
			if (msgProps == null) {
				String fileName = OBDefine.LOCAL_CONFIG;
				;
				BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
				msgProps = new Properties();
				msgProps.load(in);
			}
			String retVal = msgProps.getProperty(key).trim();
			if (retVal == null) {
				OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("date not found. key:%s", key));
				return null;
			}
			return retVal;
		} catch (Exception e) {
			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("date not found. key:%s", key));
			return null;
		}
	}
}