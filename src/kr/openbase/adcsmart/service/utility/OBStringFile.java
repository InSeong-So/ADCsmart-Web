package kr.openbase.adcsmart.service.utility;

import java.io.*;

public class OBStringFile
{
	/**
	 * 파일의 내용을 문자열 형태로 반환한다. 
	 * 
	 * @param filename
	 *			-- 파일 이름. 절대 경로 형태로 입력되어야 한다.
	 *
	 * @return String 형태의 파일 내용.
	 * 
	 * @throws IOException
	 * 			-- 파일 핸들링과 관련된 오류 발생시. 예: 파일 오픈 실패. 
	 */ 	
	public static String toString(String filename) throws IOException//adcsmart.properties 파일의 내용을 문자열 형태로 반환한다. 
	{
		StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1)
        {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();		
	}
	/**
	 * 파일의 내용을 문자열 형태로 반환한다. 
	 * 
	 * @param filename
	 *			-- 파일 이름. 절대 경로 형태로 입력되어야 한다.
	 *
	 * @return String 형태의 파일 내용.
	 * 
	 * @throws IOException
	 * 			-- 파일 핸들링과 관련된 오류 발생시. 예: 파일 오픈 실패. 
	 */ 	
	public static String filePathtoString(String fileName) throws IOException
	{
		String propertiFile = OBDefine.PROPERTY_DIR +fileName;  //경로에 있는 파일의 내용을 문자열로 반환하려 만듬
		File file = new File(propertiFile);
		FileReader fileReader=new FileReader(file); 
		StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(fileReader);
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1)
        {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();		
	}
	
	/**
	 * 데이터를 파일로 저장한다.
	 * 
	 * @param filename
	 *			-- 파일 이름. 절대 경로 형태로 입력되어야 한다.
	 *
	 * @param data
	 *			-- String 형태의 저장하고자 하는 데이
	 *
	 * @throws IOException
	 * 			-- 파일 핸들링과 관련된 오류 발생시. 예: 파일 오픈 실패. 
	 */ 
	public static void toFile(String filename, String data) throws IOException
	{
		BufferedWriter out = new BufferedWriter(new FileWriter(filename));
		out.write(data);
		out.close();
	}
}
