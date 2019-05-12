package kr.openbase.adcsmart.service.impl.f5;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;

class ManagementDBVariable {
	private iControl.Interfaces _interfaces = null;
	private iControl.ManagementDBVariablePortType _ManagementDBVariablePortType = null;

	// constructors
	private ManagementDBVariable(iControl.Interfaces interfaces) throws Exception {
		_interfaces = interfaces;
		_ManagementDBVariablePortType = _interfaces.getManagementDBVariable();
	}

	private void validateBaseValues() throws Exception {
		if (_interfaces == null) {
			throw new Exception("ManagementDBVariable: F5 interface error(null)");
		} else if (_ManagementDBVariablePortType == null) {
			throw new Exception("ManagementDBVariable: F5 interface API stub setup error(null)");
		}
	}

	private static void validateBaseValues(iControl.Interfaces interfaces) throws Exception {
		if (interfaces == null) {
			throw new Exception("ManagementDBVariable: F5 interface error(null)");
		}
	}

	public iControl.ManagementDBVariableVariableNameValue[] getAllDBVariables() throws Exception {
		validateBaseValues();

		iControl.ManagementDBVariableVariableNameValue[] db = _ManagementDBVariablePortType.get_list();
		return db;
	}

	public Timestamp getLocalConfigTime() throws Exception {
		validateBaseValues();

		String[] name = { "ConfigSync.LocalConfigTime" };
		iControl.ManagementDBVariableVariableNameValue[] NameValue = _ManagementDBVariablePortType.query(name);
		Timestamp ts = new Timestamp(Long.parseLong(NameValue[0].getValue()) * 1000);
		return ts;
	}

	static Timestamp getLocalConfigTime(iControl.Interfaces inter) throws Exception {
		validateBaseValues(inter);

		String[] name = { "ConfigSync.LocalConfigTime" };
		iControl.ManagementDBVariableVariableNameValue[] NameValue = inter.getManagementDBVariable().query(name);
		Timestamp ts = new Timestamp(Long.parseLong(NameValue[0].getValue()) * 1000);
		return ts;
	}

	static String getLocalConfigTimeString(iControl.Interfaces inter) throws Exception {
		validateBaseValues(inter);

		ManagementDBVariable db = new ManagementDBVariable(inter);
		// time value only
		SimpleDateFormat sdfTemp = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		String strTime = sdfTemp.format(db.getLocalConfigTime());
		return strTime;
	}

// test functions ------------------------------------------------------------------------------------------
//	public static void main(String[] args)
//	{
//		iControl.Interfaces inter = new iControl.Interfaces();
//		
//		inter.initialize("61.82.88.186", "admin", "admin");
//		
//		try
//		{
//			testPrintDBValues(inter, "db_vars.csv");
//			//testConfigTime(inter, null);
//		}
//		catch(OBExceptionUnreachable e)
//		{
//			System.out.println("OBExceptionUnreachable - " + e.getMessage());
//		}
//		catch(OBExceptionLogin e)
//		{
//			System.out.println("OBExceptionLogin - " + e.getMessage());
//		}
//		catch(OBException e)
//		{
//			System.out.println("OBException - " + e.getMessage());
//		}
//		catch(Exception e)
//		{
//			System.out.println("Exception - " + e.getMessage());
//		}
//	}

	public static void testConfigTime(iControl.Interfaces inter, String report_file)
			throws OBException, OBExceptionUnreachable, OBExceptionLogin {
		BufferedWriter report;
		try {
			if (report_file == null) {
				System.out.println("Last Config Time," + getLocalConfigTimeString(inter));
			} else {
				report = new BufferedWriter(new FileWriter(report_file));
				report.write("Last Config Time," + getLocalConfigTimeString(inter) + "\n");
				report.close();
			}
		} catch (Exception e) {
			CommonF5.Exception("Get time fail.", e.getMessage());
		}
	}

	/**
	 * DB의 모든 (필드,값)을 csv 파일에 쓴다.
	 * 
	 * @param interTemp
	 * @param report_file
	 */
	public static void testPrintDBValues(iControl.Interfaces inter, String report_file) throws Exception {
		BufferedWriter report = new BufferedWriter(new FileWriter(report_file));

		ManagementDBVariable dbTemp = new ManagementDBVariable(inter);
		// all db values
		iControl.ManagementDBVariableVariableNameValue[] db = dbTemp.getAllDBVariables();
		report.write(",\nDB Variables,\n,\n");
		report.write("Name,Value\n");
		for (iControl.ManagementDBVariableVariableNameValue iterDB : db) {
			report.write(iterDB.getName() + "," + iterDB.getValue().toString() + "\n");
		}
		report.close();
	}

}