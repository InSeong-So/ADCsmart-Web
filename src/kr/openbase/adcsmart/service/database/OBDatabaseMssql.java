package kr.openbase.adcsmart.service.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBDatabaseMssql extends OBDatabaseBasic {

//	private final static String VALIDATION_QUERY = " SELECT 1; ";
	private final static String SUFFIX_DB_TABLE = "table";
	private final static String SUFFIX_DB_IPADDRESS = "ipaddres";
	private final static String SUFFIX_DB_USER = "user";
	private final static String SUFFIX_DB_PASSWORD = "password";
	private final static String SUFFIX_DB_PORT = "port";

	private final static String DEFAULT_DB_TABLE = "ADC_SMART";
	private final static String DEFAULT_DB_IPADDRESS = "172.172.2.2";
	private final static String DEFAULT_DB_USER = "adcsmart";
	private final static String DEFAULT_DB_PASSWORD = "adcsmart!1";
	private final static String DEFAULT_DB_PORT = "1433";

	private static final String DBCP_PROPS_PATH = "/opt/adcsmart/cfg/sms_dbcp.conf";
	private static final Properties DBCP_PROPS;
	private final String selectMethod = "cursor";

	static {
		DBCP_PROPS = new Properties();
		try {
			DBCP_PROPS.load(new BufferedReader(new FileReader(DBCP_PROPS_PATH)));
		} catch (FileNotFoundException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, "DBCP properties file NOT FOUND: " + e.getMessage());
		} catch (IOException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, "error in read DBCP properties file: " + e.getMessage());
		}
		try {
			String value = "";
			value = DBCP_PROPS.getProperty(SUFFIX_DB_TABLE);
			if (value == null || value.isEmpty())
				DBCP_PROPS.setProperty(SUFFIX_DB_TABLE, DEFAULT_DB_TABLE);

			value = DBCP_PROPS.getProperty(SUFFIX_DB_IPADDRESS);
			if (value == null || value.isEmpty())
				DBCP_PROPS.setProperty(SUFFIX_DB_IPADDRESS, DEFAULT_DB_IPADDRESS);

			value = DBCP_PROPS.getProperty(SUFFIX_DB_USER);
			if (value == null || value.isEmpty())
				DBCP_PROPS.setProperty(SUFFIX_DB_USER, DEFAULT_DB_USER);

			value = DBCP_PROPS.getProperty(SUFFIX_DB_PASSWORD);
			if (value == null || value.isEmpty())
				DBCP_PROPS.setProperty(SUFFIX_DB_PASSWORD, DEFAULT_DB_PASSWORD);

			value = DBCP_PROPS.getProperty(SUFFIX_DB_PORT);
			if (value == null || value.isEmpty())
				DBCP_PROPS.setProperty(SUFFIX_DB_PORT, DEFAULT_DB_PORT);

		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, "error in read DBCP properties file: " + e.getMessage());
		}

	}

	/**
	 * 데이터베이스에 작업을 위한 인스턴스 생성자.
	 * 
	 * @param dbname
	 * @param username
	 * @param password
	 */
	public OBDatabaseMssql(String dbname, String username, String password, String server, String port) {
		this.dbName = dbname;
		this.userName = username;
		this.password = password;
		this.server = server;
		this.port = port;
	}

	public OBDatabaseMssql() {
		this.dbName = DBCP_PROPS.getProperty(SUFFIX_DB_TABLE);
		this.userName = DBCP_PROPS.getProperty(SUFFIX_DB_USER);
		this.password = DBCP_PROPS.getProperty(SUFFIX_DB_PASSWORD);
		this.server = DBCP_PROPS.getProperty(SUFFIX_DB_IPADDRESS);
		this.port = DBCP_PROPS.getProperty(SUFFIX_DB_PORT);
	}

	/**
	 * 데이터베이스에 작업을 위한 인스턴스 생성자. DB이름, 사용자이름, 비밀번호는 기본값을 사용한다.
	 */

//  public OBDatabaseMssql()
//  {
//  }

	/**
	 * DB을 오픈한다.
	 * 
	 * @throws SQLException -- DB 관련 오류 발생시.
	 */

	private String getConnectionUrl() {
		return "jdbc:sqlserver://" + this.server + ":" + this.port + ";databaseName=" + this.dbName + ";selectMethod="
				+ this.selectMethod + ";";
	}

	public void openDB() throws OBException {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 데이터 베이스 class load
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			this.connection = DriverManager.getConnection(getConnectionUrl(), this.userName, this.password);
//            this.connection = DriverManager.getConnection("jdbc:sqlserver://172.172.2.49\\SQLEXPRESS:1433;DatabaseName=ADC_SMART", "adcsmart", "adcsmart!1");

			if (this.connection == null) {
				// TODO throw error
			} else {
				this.statement = this.connection.createStatement();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void openDBLocalWithSystemAuth() throws OBException {
		try {
			// 데이터 베이스 class load
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			this.connection = DriverManager.getConnection("jdbc:sqlserver://localhost;integratedSecurity=true;");

			if (this.connection == null) {
				// TODO throw error
			} else {
				System.out.println("table open success");
				this.statement = this.connection.createStatement();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//    public static void main(String[] args)
//    {
//        System.out.println("hi");
//        
//        OBDatabaseMssql db = new OBDatabaseMssql();
//        try
//        {
//            db.openDB();
//            
//            String select = "select SMS_INDEX, SMS_MSG from SMS_SEND;";
//            
//            try
//            {
//                ResultSet rs;
//                rs = db.executeQuery(select);
//                while(rs.next())
//                {
//                    Integer index = db.getInteger(rs, "SMS_INDEX");
//                    String msg = db.getString(rs, "SMS_MSG");
//                    System.out.println("index : " + index + "msg : " + msg);
//                }
//            }
//            catch(SQLException e)
//            {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            
//        }
//        catch(OBException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        finally
//        {
//            if(db != null)
//                db.closeDB();
//        }
//    }
}