package kr.openbase.adcsmart.web.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import kr.openbase.adcsmart.service.utility.OBException;

public class OBFileHandler
{
	public static boolean isFileExist(String fileName) throws OBException
	{
		try
		{
			File f = new File(fileName);

		    	// 파일 존재 여부 판단
			if (f.isFile()) 
			{
				return true;
			}
			else 
			{
				return false;
			}
		}
		catch(Exception e)
		{
			throw new OBException(e.getMessage());
		}
	}
	
	public static String toString(String filename) throws OBException
	{
		try
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
		catch(FileNotFoundException	e)
		{
			return "";
		}
		catch(IOException e)
		{
			return "";
		}
		catch(Exception e)
		{
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, e.getMessage());
		}
	}
	
	public static boolean isFilewrite(String filename, String fileString) throws OBException
	{
		File f = new File(filename);
		try
		{
			FileWriter fileWriter = new FileWriter(f, false);
			BufferedWriter out = new BufferedWriter(fileWriter);
			
			out.write(fileString);
			out.close();
			return true;
		}
		catch(Exception e)
		{
//			System.out.println(e.getMessage());
			return false;
		}
	}
}
