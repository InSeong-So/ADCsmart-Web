package kr.openbase.adcsmart.service.database;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.service.utility.OBUtility;

public class OBDBConnectionPool {
	private final static String VALIDATION_QUERY = " SELECT 1; ";
	private final static String SUFFIX_DB_URL = "url";
	private final static String SUFFIX_DB_USER = "user";
	private final static String SUFFIX_DB_PASSWORD = "password";
	private final static String SUFFIX_DB_MAXTOTAL = "maxTotal";
	private final static String SUFFIX_DB_MAXIDLE = "maxIdle";
	private final static String SUFFIX_DB_MINIDLE = "minIdle";

	private final static String DEFAULT_DB_URL = "jdbc:postgresql://localhost:5432/adcms";
	private final static String DEFAULT_DB_USER = "adcsmartdba";
	private final static String DEFAULT_DB_PASSWORD = "adcsmart!@34";

	private static final OBDBConnectionPool INSTANCE;

	private static final String DBCP_PROPS_PATH = "/opt/adcsmart/cfg/dbcp.conf";
//    private static final String             WIN_DBCP_PROPS_PATH = "c:/opt/adcsmart/cfg/dbcp.conf";
	private static final String WIN_DBCP_PROPS_PATH = "/opt/adcsmart/cfg/dbcp.conf";
	public static final Properties DBCP_PROPS;

	private DataSource dataSource;

	static {
		try {
			Class.forName("org.postgresql.Driver");
			// Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, "ojdbc driver NOT FOUND: " + e.getMessage());
		}

		DBCP_PROPS = new Properties();
		try {
			DBCP_PROPS.load(new BufferedReader(new FileReader(getDBOsPath())));
		} catch (FileNotFoundException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, "DBCP properties file NOT FOUND: " + e.getMessage());
		} catch (IOException e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, "error in read DBCP properties file: " + e.getMessage());
		}
		try {
			String value = "";
			value = DBCP_PROPS.getProperty(SUFFIX_DB_URL);
			if (value == null || value.isEmpty())
				DBCP_PROPS.setProperty(SUFFIX_DB_URL, DEFAULT_DB_URL);

			value = DBCP_PROPS.getProperty(SUFFIX_DB_USER);
			if (value == null || value.isEmpty())
				DBCP_PROPS.setProperty(SUFFIX_DB_USER, DEFAULT_DB_USER);

			value = DBCP_PROPS.getProperty(SUFFIX_DB_PASSWORD);
			if (value == null || value.isEmpty())
				DBCP_PROPS.setProperty(SUFFIX_DB_PASSWORD, DEFAULT_DB_PASSWORD);

			value = DBCP_PROPS.getProperty(SUFFIX_DB_MAXTOTAL);
			if (value == null || value.isEmpty())
				DBCP_PROPS.setProperty(SUFFIX_DB_MAXTOTAL, "100");

			value = DBCP_PROPS.getProperty(SUFFIX_DB_MAXIDLE);
			if (value == null || value.isEmpty())
				DBCP_PROPS.setProperty(SUFFIX_DB_MAXIDLE, "20");

			value = DBCP_PROPS.getProperty(SUFFIX_DB_MINIDLE);
			if (value == null || value.isEmpty())
				DBCP_PROPS.setProperty(SUFFIX_DB_MINIDLE, "10");
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, "error in read DBCP properties file: " + e.getMessage());
		}

		INSTANCE = new OBDBConnectionPool();
	}

	public OBDBConnectionPool() {
		dataSource = setupDataSource();
	}

	static OBDBConnectionPool getInstance() {
		return INSTANCE;
	}

	public Connection getConnection() throws SQLException {
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

	private static DataSource setupDataSource() {
		//
		// First, we'll create a ConnectionFactory that the
		// pool will use to create Connections.
		// We'll use the DriverManagerConnectionFactory,
		// using the connect string passed in the command line
		// arguments.
		//
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(DBCP_PROPS.getProperty("url"),
				DBCP_PROPS);
		//
		// Next we'll create the PoolableConnectionFactory, which wraps
		// the "real" Connections created by the ConnectionFactory with
		// the classes that implement the pooling functionality.
		//
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, null);

		//
		// Now we'll need a ObjectPool that serves as the
		// actual pool of connections.
		//
		// We'll use a GenericObjectPool instance, although
		// any ObjectPool implementation will suffice.
		//
		//

		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setTestOnCreate(true);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTimeBetweenEvictionRunsMillis(1000L * 60L * 1L);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setMinIdle(Integer.parseInt(DBCP_PROPS.getProperty(SUFFIX_DB_MINIDLE)));
		poolConfig.setMaxIdle(Integer.parseInt(DBCP_PROPS.getProperty(SUFFIX_DB_MAXIDLE)));
		poolConfig.setMaxTotal(Integer.parseInt(DBCP_PROPS.getProperty(SUFFIX_DB_MAXTOTAL)));
		ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory, poolConfig);

		// Set the factory's pool property to the owning pool
		poolableConnectionFactory.setValidationQuery(VALIDATION_QUERY);
		poolableConnectionFactory.setPool(connectionPool);

		// Finally, we create the PoolingDriver itself,
		// passing in the object pool we created.
		PoolingDataSource<PoolableConnection> dataSource = new PoolingDataSource<>(connectionPool);

		return dataSource;
	}

	public static String getDBOsPath() {
		String result = "";
		if (OBUtility.isWindows()) {
			result = WIN_DBCP_PROPS_PATH;
		} else {
			result = DBCP_PROPS_PATH;
		}
		return result;
	}
}
