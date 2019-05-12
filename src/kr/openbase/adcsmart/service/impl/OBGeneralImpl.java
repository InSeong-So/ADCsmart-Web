package kr.openbase.adcsmart.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import kr.openbase.adcsmart.service.OBGeneral;
import kr.openbase.adcsmart.service.utility.OBException;

public class OBGeneralImpl implements OBGeneral
{
	@Override
	public OBException getOBExceptionInfo() throws OBException
	{
		try(FileInputStream fileStream = new FileInputStream(".exception.ser"); ObjectInputStream os = new ObjectInputStream(fileStream);)
		{
			Object obj = os.readObject();
			File  f = new File(".exception.ser");
			if(f!=null)
				f.delete();// 파일을 삭제한다.
			return (OBException)obj;
			
		}
		catch(FileNotFoundException e)
		{
		}
		catch(IOException e)
		{
		}
		catch(ClassNotFoundException e)
		{
		}
		catch(Exception e)
		{
		}
		return null;
	}

	public boolean setOBExceptionInfo(OBException eobj)
	{
		try
		{
			FileOutputStream fileStream = new FileOutputStream(".exception.ser");
			ObjectOutputStream os = new ObjectOutputStream(fileStream);
			os.writeObject(eobj);
			os.close();
		}
		catch(FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		catch(IOException e2)
		{
			e2.printStackTrace();
		}
		return true;
	}
}
