package kr.openbase.adcsmart.service.utility;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class OBUtility {
	private static String OS = System.getProperty("os.name").toLowerCase();

	public static long unsigned32(int n) {
		return n & 0xFFFFFFFFL;
	}

//	public static void main(String[] args)
//	{
////		return logKey+".png";aa
////		long logKey = 6884488616672L;
////		String pngFullPathName = OBDefine.PKT_DUMP_FILE_PATH+logKey+".png";
////		
//////		String imgName = loginTime.getTime()%1000L+".png";
////		String imgName = "pktlossinfo.png";
////		String targetFile = "/opt/apache-tomcat/webapps/adcms/imgs/"+imgName;
//////		String cmnd = String.format("/bin/cp -rf %s %s", pngFullPathName, targetFile);
////		try
////		{
////			// 이전 파일은 삭제한다. 
////			OBUtility.untarFile("/var/lib/adcsmart/pcap/3585948692683.pcap.tar.gz", "/var/lib/adcsmart/pcap/");
////			
////			// 파일을 복사한다.
//////			OBUtility.fileCopy(pngFullPathName, targetFile);
//////			OBSystemLog.info(OBDefine.LOGFILE_SYSTEM, String.format("cmnd:%s", cmnd));
//////			Runtime.getRuntime().exec(cmnd);
//////			OBDateTime.Sleep(100);
////			return;
////		}
////		catch(Exception e)
////		{
////			e.printStackTrace();
////			return ;
////		}
//		
//	}

//	public static List<File> untarFile(String tarFile, String destFolder) throws OBException
//	{
//	    final List<File> untaredFiles = new LinkedList<File>();
//	    
//		try
//		{
//			
//			File archive = new File(tarFile);
//			File destination = new File(destFolder);
//
//			Archiver archiver = ArchiverFactory.createArchiver("tar", "gz");
//			archiver.extract(archive, destination);
////			
////			InputStream is = new FileInputStream(tarFile);
////		
////		    TarArchiveInputStream debInputStream;
////			debInputStream = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream("tar", is);
////			
////		    TarArchiveEntry entry = null; 
////
////			while ((entry = (TarArchiveEntry)debInputStream.getNextEntry()) != null) {
////			    final File outputFile = new File(destFolder, entry.getName());
////			    if (entry.isDirectory()) {
////			        if (!outputFile.exists()) {
////			            if (!outputFile.mkdirs()) {
////			                throw new IllegalStateException(String.format("Couldn't create directory %s.", outputFile.getAbsolutePath()));
////			            }
////			        }
////			    } else {
////			        final OutputStream outputFileStream = new FileOutputStream(outputFile); 
////			        IOUtils.copy(debInputStream, outputFileStream);
////			        outputFileStream.close();
////			    }
////			    untaredFiles.add(outputFile);
////			}
////			
////		    debInputStream.close(); 
//		}
////		catch(ArchiveException e)
////		{
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}		
//		catch(FileNotFoundException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		catch(IOException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    return untaredFiles;
//	}

	public static String convertKMG(Long value) {
		String retVal = "";

		if (value > 1000000000)// G
			retVal = String.format("%.2fG", value / 1000000000f);
		else if (value > 1000000)// M
			retVal = String.format("%.2fM", value / 1000000f);
		else if (value > 1000)// K
			retVal = String.format("%.2fK", value / 1000f);
		else
			retVal = String.format("%d", value);
		return retVal;
	}

	public static String convertKmg(Long value) {
		String retVal = "";

		if (value > 1000000000)// G
			retVal = String.format("%.1fG", value / 1000000000f);
		else if (value > 1000000)// M
			retVal = String.format("%.1fM", value / 1000000f);
		else if (value > 1000)// k
			retVal = String.format("%.1fk", value / 1000f);
		else
			retVal = String.format("%d", value);
		return retVal;
	}

	public static String toStringWithIntUnit(int number, String unit) {
		String numberWithUnit;
		if (number < 1000)
			numberWithUnit = String.valueOf(number) + unit;
		else if (number < 1000000)
			numberWithUnit = String.format("%,.1fk%s", number / 1000., unit);
		else if (number < 1000000000)
			numberWithUnit = String.format("%,.1fM%s", number / 1000000., unit);
		else if (number < 1000000000000L)
			numberWithUnit = String.format("%,.1fG%s", number / 1000000000., unit);
		else if (number < 1000000000000000L)
			numberWithUnit = String.format("%,.1fT%s", number / 1000000000000., unit);
		else if (number < 1000000000000000000L) // 페타
			numberWithUnit = String.format("%,.1fP%s", number / 1000000000000000., unit);
		else
			numberWithUnit = String.format("%,dbyte%s", number, unit);
		return numberWithUnit;
	}

	public static String toStringWithDataUnitSvc(Long number, String unit) {
		int min = 0;
		if (null == number)
			number = 0l;

		String numberWithUnit;
		if (number < 0) {
			number = Math.abs(number);
			min = 1;
		}

		if (number < 1000)
			numberWithUnit = String.valueOf(number) + unit;
		else if (number < 1000000)
			numberWithUnit = String.format("%,.1fk%s", number / 1000., unit);
		else if (number < 1000000000)
			numberWithUnit = String.format("%,.1fM%s", number / 1000000., unit);
		else if (number < 1000000000000L)
			numberWithUnit = String.format("%,.1fG%s", number / 1000000000., unit);
		else if (number < 1000000000000000L)
			numberWithUnit = String.format("%,.1fT%s", number / 1000000000000., unit);
		else if (number < 1000000000000000000L) // 페타
			numberWithUnit = String.format("%,.1fP%s", number / 1000000000000000., unit);
		else
			numberWithUnit = String.format("%,dbyte%s", number, unit);

		if (min == 1)
			numberWithUnit = "-" + numberWithUnit;
		return numberWithUnit;
	}

	// 파일을 이동하는 메소드
	public static Long fileLength(String inFileName) {
		try {
			File oFile = new File(inFileName);

			if (oFile.exists()) {
				return oFile.length();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return 0L;
	}

	// 파일을 이동하는 메소드
	public static void fileMove(String inFileName, String outFileName) {
		try {
			FileInputStream fis = new FileInputStream(inFileName);
			FileOutputStream fos = new FileOutputStream(outFileName);

			int data = 0;
			while ((data = fis.read()) != -1) {
				fos.write(data);
			}
			fis.close();
			fos.close();

			// 복사한뒤 원본파일을 삭제함
			fileDelete(inFileName);

		} catch (IOException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
		}
	}

	public static boolean fileCopy(String inFileName, String outFileName) {
		try {
			boolean retVal = true;
			FileInputStream fis = new FileInputStream(inFileName);
			FileOutputStream fos = new FileOutputStream(outFileName);

			int data = 0;
			while ((data = fis.read()) != -1) {
				fos.write(data);
			}
			fis.close();
			fos.close();
			return retVal;
		} catch (IOException e) {
			return false;
		}
	}

	// 파일을 삭제하는 메소드
	public static void fileDelete(String deleteFileName) {
		try {
			File I = new File(deleteFileName);
			I.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException nfe) {

		}
		return false;
	}

	public static String getStackTrace(Exception e) {
		String stackTraceString = "";

		ByteArrayOutputStream out = null;
		PrintStream pinrtStream = null;
		try {
			out = new ByteArrayOutputStream();
			pinrtStream = new PrintStream(out);
			e.printStackTrace(pinrtStream);
			stackTraceString = out.toString();
		} catch (Exception except) {

		} finally {
			try {
				pinrtStream.close();
			} catch (Exception except) {
			}
			try {
				out.close();
			} catch (Exception except) {
			}
		}

		return stackTraceString;
	}

	public String getStackTrace() {
		StringBuffer stacktrace = new StringBuffer();
		StackTraceElement[] stackTrace = new Exception().getStackTrace();
		for (int x = 1; x < stackTrace.length; x++) {
			stacktrace.append(stackTrace[x].toString() + "\n");
			if (x > 6)
				break;
		}
		return stacktrace.toString();
	}

	public static String getPropOsPath() {
		String result = "";
		if (isWindows()) {
			result = OBDefine.WIN_PROPERTIES_PATH;
		} else {
			result = OBDefine.PROPERTIES_PATH;
		}
		return result;
	}

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);
	}

	public static String inputStreamToString(InputStream is) {

		String line = "";
		StringBuilder total = new StringBuilder();

		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try {
			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
		} catch (IOException e) {

		}
		return total.toString();
	}
}
