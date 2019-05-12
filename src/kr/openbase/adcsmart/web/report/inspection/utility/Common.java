package kr.openbase.adcsmart.web.report.inspection.utility;

import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonExecuteHandler;

// 미완성
public class Common
{

    private final static String SUFFIX_MORE         = "any other key to continue[0m";
    
    public static AlteonExecuteHandler getValidAlteonHandler(String swVersion)
    {
        if(swVersion == null || swVersion.isEmpty())
        {
            return new AlteonExecuteHandler();
        }

        return new AlteonExecuteHandler();
    }
    
    public static String removeMoreText(String line)
    {
        String retVal = line;
        int strIndex = 0;
        strIndex = line.indexOf(SUFFIX_MORE);
        if(strIndex>=0)
        {
            retVal = line.substring(strIndex+SUFFIX_MORE.length());
        }
        // remove white space
        byte [] byteArray = retVal.getBytes();
        int iPos = 0;
        for(int i = 0 ; i < byteArray.length ; i++)
        {
            if(byteArray[i] != ' ')
            {
                iPos = i;
                break;
            }
        }
        
        retVal = retVal.substring(iPos);
        return retVal;
    }
    
    public static boolean f5SwVersion(String swVersion)
    {
        boolean retVal = false;
        if(swVersion.contains("11"))
        {
            retVal = true;
        }
        return retVal;
    }
}