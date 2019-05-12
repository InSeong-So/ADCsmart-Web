package kr.openbase.adcsmart.service.utility;

import java.io.Serializable;

public class OBExceptionDuplicate extends Exception implements Serializable 
{
    private static final long serialVersionUID = 1L;
    private String  errorMessage;
    
	public OBExceptionDuplicate(String msgName, String extraMsg)
    {
        this.errorMessage = OBException.getExceptionMessage(msgName);
        this.errorMessage += "(" + extraMsg + ")";
        
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String className=stackTraceElements[2].getClassName();
        String methodName=stackTraceElements[2].getMethodName();
        String moduleName = className + "::" + methodName;
        
        OBSystemLog.error3(OBDefine.LOGFILE_SYSTEM, String.format("msg:%s, module:%s", this.errorMessage, moduleName));
    }

	@Override
	public String getMessage() 
	{
		return this.errorMessage;
	}	
	
	public String getErrorMessage()
    {
        return this.errorMessage;
    }
}
