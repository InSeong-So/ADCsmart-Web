package kr.openbase.adcsmart.service.utility;

import java.io.*;
import java.security.*;

public class OBSha512
{
	private static final String ALGORITHM = "SHA-512";
	
	/**
	 * byte[] 형태의 데이터를  대상으로 해슁하여 byte[] 형태로 해쉬값을 리턴한다.
	 * 
	 * @param input
	 *			-- byte[] 형태의 데이터.
	 *
	 * @return  byte[] 형태의 해쉬값.
	 * 
	 */ 	
	public static byte[] getHash(byte[] input) 
	{
        try 
        {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			return md.digest(input);
		} 
        catch (NoSuchAlgorithmException e) 
		{
			e.printStackTrace();
			return null;
		}
    }
	
	/**
	 * InputStream 형태의 데이터를  대상으로 해슁하여 byte[] 형태로 해쉬값을 리턴한다.
	 * 
	 * @param input
	 *			-- InputStream 오브젝트.
	 *
	 * @return  byte[] 형태의 해쉬값.
	 * 
	 * @throws IOException
	 * 			-- 파일 핸들링과 관련된 오류 발생시.
	 */ 
    public static byte[] getHash(InputStream input) throws IOException 
    {
        try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM);
			int read = -1;
			byte[] buffer = new byte[1024];
			while ((read = input.read(buffer)) != -1)
			{
				md.update(buffer, 0, read);
			}
			return md.digest();
		} 
        catch (NoSuchAlgorithmException e) 
        {
			e.printStackTrace();
			return null;
		}
    }

	/**
	 * 파일을 대상으로 해슁하여 byte[] 형태로 해쉬값을 리턴한다.
	 * 
	 * @param file
	 *			-- File 오브젝트.
	 *
	 * @return  byte[] 형태의 해쉬값.
	 * 
	 * @throws IOException
	 * 			-- 파일 핸들링과 관련된 오류 발생시.
	 */ 
    public static byte[] getHash(File file) throws IOException 
    {
 		byte[] hash = null;
     	BufferedInputStream bis = null;
 		try 
 		{
 			bis = new BufferedInputStream(new FileInputStream(file));
 			hash = getHash(bis);
 		} 
 		finally 
 		{
 			if (bis != null) try { bis.close(); } catch(IOException ie) {}
 		}
 		return hash;
     }
    
	/**
	 * 입력받은 byte[]의 해쉬값을 문자열 형태로 제공한다.
	 * 
	 * @param input
	 *			-- byte[] 형태의 데이터.
	 *
	 * @return Hex형태의 문자열(예: bd6ae522ada462ec9c3199e...)
	 * 
	 */ 
    public static String getHashHexString(byte[] input) 
    {
		byte[] hash = getHash(input);
		StringBuffer sb = new StringBuffer(); 
		for (int i = 0; i < hash.length; i++) 
		{ 
			 sb.append(Integer.toString((hash[i] & 0xf0) >> 4, 16)); 
			 sb.append(Integer.toString(hash[i] & 0x0f, 16));
		} 
		return sb.toString();
    }
    
	/**
	 * 입력받은 문자열을 해슁하여 해슁한 값을 문자열 형태로 반환한다.
	 * 
	 * @param input
	 *			-- 해슁을 원하는 문자열.
	 *
	 * @return Hex형태의 문자열(예: bd6ae522ada462ec9c3199e...)
	 * 
	 */ 
    public static String getHashHexString(String input) 
    {
		return getHashHexString(input.getBytes());
    }
 
/*
    public static String getHashHexString(String input, String charsetName) throws UnsupportedEncodingException 
    {
		return getHashHexString(input.getBytes(charsetName));
    }    

    
    public static String getHashHexString(InputStream input) throws IOException 
    {
        byte[] hash = getHash(input);
        StringBuffer sb = new StringBuffer(hash.length * 2); 
        for (int i = 0; i < hash.length; i++) { 
			 sb.append(Integer.toString((hash[i] & 0xf0) >> 4, 16)); 
			 sb.append(Integer.toString(hash[i] & 0x0f, 16));
		} 
    	return sb.toString();
    }
*/    
	/**
	 * 파일의 내용을 해슁하여 해쉬값을 문자열로 반환한다.
	 * 
	 * @param file
	 *			-- 파일 오브젝트.
	 *
	 * @return Hex형태의 문자열(예: bd6ae522ada462ec9c3199e...)
	 * 
	 * @throws IOException
	 * 			-- 파일 핸들링과 관련된 오류 발생시.
	 */ 
    public static String getHashHexString(File file) throws IOException 
    {
		byte[] hash = getHash(file);
		StringBuffer sb = new StringBuffer(hash.length * 2);
		for (int i = 0; i < hash.length; i++) { 
			 sb.append(Integer.toString((hash[i] & 0xf0) >> 4, 16)); 
			 sb.append(Integer.toString(hash[i] & 0x0f, 16));
		}
    	return sb.toString();
    }
}

