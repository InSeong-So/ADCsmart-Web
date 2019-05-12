package kr.openbase.adcsmart.service.utility;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import kr.openbase.adcsmart.service.database.OBDatabaseMssql;

public class OBConfigAdcVersion {
	Integer adcType;
	ArrayList<String> versions;

	@Override
	public String toString() {
		return "OBConfigAdcVersion [adcType=" + adcType + ", versions=" + versions + "]";
	}

	public Integer getAdcType() {
		return adcType;
	}

	public void setAdcType(Integer adcType) {
		this.adcType = adcType;
	}

	public ArrayList<String> getVersions() {
		return versions;
	}

	public void setVersions(ArrayList<String> versions) {
		this.versions = versions;
	}

	public static final String VERSION_FILE = OBDefine.CFG_DIR + "version.cfg";

	// constructor
	public OBConfigAdcVersion() {
		String line;
		String prevLine;
//    	Integer currentAdcType = OBDefine.ADC_TYPE_ALTEON;

		// config file 처리용 objects
		FileInputStream fstream = null;
		DataInputStream in = null;
		BufferedReader reader = null;

		try {
			fstream = new FileInputStream(VERSION_FILE);
		} catch (Exception e) // file not found exception
		{
			OBSystemLog.info(OBDefine.LOGFILE_DEBUG,
					String.format("ADC version config file \"%s\" not found.", VERSION_FILE));
		}

		try {
			in = new DataInputStream(fstream);
			reader = new BufferedReader(new InputStreamReader(in));

			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#")) // 주석 line 생략
				{
					continue;
				}
				if (line.toLowerCase().equals("[alteon]") == true) {
					prevLine = line;
					if ((line = reader.readLine()) == null) {
						OBSystemLog.warn(OBDefine.LOGFILE_DEBUG, // 일부러 설정하지 않았을 수 있기 때문에 오류로 처리하지 않는다. 단, 주소가 없으면 기능이
																	// 동작하지 않는다.
								String.format("NMS config value is missed in file %s, position : \"%s\"", VERSION_FILE,
										prevLine));
						// this.setDbUrl(null);
					} else {
						// this.setDbUrl(line);
					}
				} else if (line.equals("[DBUSERNAME]") == true) {
					prevLine = line;
					if ((line = reader.readLine()) == null) {
						OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format(
								"NMS config value is missed in file %s, position : \"%s\"", VERSION_FILE, prevLine));
						// this.setDbUserName(NMS_DBUSERNAME); //
					} else {
						// his.setDbUserName(line);
					}
				} else if (line.equals("[DBPASSWORD]") == true) {
					prevLine = line;
					if ((line = reader.readLine()) == null) {
						OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format(
								"NMS config value is missed in file %s, position : \"%s\"", VERSION_FILE, prevLine));
						// this.setDbPassword(NMS_DBPASSWORD);
					} else {
						// this.setDbPassword(line);
					}
				} else if (line.equals("[UPDATE_TIME]") == true) {
					prevLine = line;
					if ((line = reader.readLine()) == null) {
						OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format(
								"NMS config value is missed in file %s, position : \"%s\"", VERSION_FILE, prevLine));
						// this.setUpdateTime(NMS_UPDATE_TIME);
					} else {
						// this.setUpdateTime(line);
					}
				} else if (line.equals("[DAILY_DATA_START_TIME]") == true) {
					prevLine = line;
					if ((line = reader.readLine()) == null) {
						OBSystemLog.info(OBDefine.LOGFILE_DEBUG, String.format(
								"NMS config value is missed in file %s, position : \"%s\"", VERSION_FILE, prevLine));
						// this.setDailyDataStartTime(NMS_DAILY_DATA_START_TIME);
					} else {
						// this.setDailyDataStartTime(line);
					}
				}
			}
		} catch (Exception e) {
			OBSystemLog.warn(OBDefine.LOGFILE_DEBUG,
					String.format("NMS config syntax error. file:\"%s\"", VERSION_FILE));
		} finally {
			try {
				reader.close();
				in.close();
				fstream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

//	public static void main(String args[])
//	{
//		//OBConfigAdcVersion manager = new OBConfigAdcVersion(); // 진짜 코드
//		//OBSdsNms manager = new OBSdsNms("ADC_SMART_DB", "adcsmart", "adcsmart", "211.189.47.54", "1433", "05:00:00", "00:00:00"); //SDS에 대고 테스트
//		//OBSdsNms manager = new OBSdsNms("ADC_SMART_DB", "adcsmart", "adcsmart", "172.172.2.151", "1433", "05:00:00", "00:00:00"); //내부에서 테스트
//
//		//manager.getTableList();
//	}

	public void test1() {
//		try
//		{
//			ttdb.openDBLocalWithSystemAuth();
//		}
//		catch(OBException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		ttdb.closeDB();
	}

	public void getTableList() // throws OBException
	{
		OBDatabaseMssql db = new OBDatabaseMssql("ADC_SMART_DB", "adcsmart", // user name
				"adcsmart", // password,
				"211.189.47.54", "1433");

		try {
			db.openDB();
		} catch (OBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(" table name     column name    data type    is_nullable?");
		System.out.println("---------------------------------------------------------");
		try {
			ResultSet rs = db.executeQuery(
					" select TABLE_NAME, COLUMN_NAME, DATA_TYPE, IS_NULLABLE from information_schema.columns "
							+ " where table_name in (select TABLE_NAME from information_schema.tables) "
							+ " order by TABLE_NAME, COLUMN_NAME");
			while (rs.next()) {
				System.out.println(db.getString(rs, "TABLE_NAME") + "   " + db.getString(rs, "COLUMN_NAME") + "   "
						+ db.getString(rs, "DATA_TYPE") + "   " + db.getString(rs, "IS_NULLABLE"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (db != null)
				db.closeDB();
		}
	}
}