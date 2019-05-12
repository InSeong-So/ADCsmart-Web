package kr.openbase.adcsmart.web.util;

public class NumberUtil
{
	// 문자열 안에서 숫자만 추출하여 return 한다.
	public static int extractNumbertoString(String unit)
	{
		int retVal = -1;
		if(null != unit)
		{
			String tmpStr = "";
			char[] chArr = unit.toCharArray();
			int intNumber = chArr.length;
			for (int i = 0; i < intNumber; i++)
			{
				if (Character.isDigit((chArr[i])))
				{
					tmpStr += chArr[i];
				}
			}
			if (null != tmpStr && "" != tmpStr)
			{
				retVal = (Integer.valueOf(tmpStr));
			}
		}
		else
		{
			retVal = -1;
		}
		return retVal;		
	}
	
	// traffic
	public static String toStringWithUnit(Long number, String unit)
	{
		if (null == number)
			number = 0l;
		
		String numberWithUnit;
		if (number < 1000)
			numberWithUnit = String.valueOf(number) + unit;
		else if (number < 1000000)
			numberWithUnit = String.format("%,.1fk%s", number/1000., unit);
		else if (number < 1000000000)
			numberWithUnit = String.format("%,.1fM%s", number/1000000., unit);
		else if (number < 1000000000000L)
			numberWithUnit = String.format("%,.1fG%s", number/1000000000., unit);
		else if (number < 1000000000000000L)
			numberWithUnit = String.format("%,.1fT%s", number/1000000000000., unit);
		else if (number < 1000000000000000000L) //페타
			numberWithUnit = String.format("%,.1fP%s", number/1000000000000000., unit);	
		else
			numberWithUnit = String.format("%,dbyte%s", number, unit);
		return numberWithUnit;
	}
	
	// traffic Filter Filter Negative number
	public static String toStringWithUnitFilterNegativeNumber(Long number, String unit)
	{
		if (null == number)
			number = 0l;
		
		String numberWithUnit;
		if (number < 0)
		{
			numberWithUnit = "-";
		}
		else if (number < 1000)
			numberWithUnit = String.valueOf(number) + unit;
		else if (number < 1000000)
			numberWithUnit = String.format("%,.1fk%s", number/1000., unit);
		else if (number < 1000000000)
			numberWithUnit = String.format("%,.1fM%s", number/1000000., unit);
		else if (number < 1000000000000L)
			numberWithUnit = String.format("%,.1fG%s", number/1000000000., unit);
		else if (number < 1000000000000000L)
			numberWithUnit = String.format("%,.1fT%s", number/1000000000000., unit);
		else if (number < 1000000000000000000L) //페타
			numberWithUnit = String.format("%,.1fP%s", number/1000000000000000., unit);	
		else
			numberWithUnit = String.format("%,dbyte%s", number, unit);
		return numberWithUnit;
	}
	
	// data
    public static String toStringWithDataUnit(Long number, String unit)
    {
        if (null == number)
            number = 0l;
        
        String numberWithUnit;
        if (number < 1000)
            numberWithUnit = String.valueOf(number) + unit;
        else if (number < 1000000)
            numberWithUnit = String.format("%,.1fkB%s", number/1000., unit);
        else if (number < 1000000000)
            numberWithUnit = String.format("%,.1fMB%s", number/1000000., unit);
        else if (number < 1000000000000L)
            numberWithUnit = String.format("%,.1fGB%s", number/1000000000., unit);
        else if (number < 1000000000000000L)
            numberWithUnit = String.format("%,.1fTB%s", number/1000000000000., unit);
        else if (number < 1000000000000000000L) //페타
            numberWithUnit = String.format("%,.1fPB%s", number/1000000000000000., unit);    
        else
            numberWithUnit = String.format("%,dbyte%s", number, unit);
        return numberWithUnit;
    }
    
	// data
	public static String toStringWithDataUnitSvc(Long number, String unit)
	{
	    int min = 0;
		if (null == number)
			number = 0l;
		
		String numberWithUnit;
		if (number < 0)
		{
		    number = Math.abs(number);
		    min = 1;
		}
		
		if (number < 1000)
			numberWithUnit = String.valueOf(number) + unit;
		else if (number < 1000000)
			numberWithUnit = String.format("%,.1fk%s", number/1000., unit);
		else if (number < 1000000000)
			numberWithUnit = String.format("%,.1fM%s", number/1000000., unit);
		else if (number < 1000000000000L)
			numberWithUnit = String.format("%,.1fG%s", number/1000000000., unit);
		else if (number < 1000000000000000L)
			numberWithUnit = String.format("%,.1fT%s", number/1000000000000., unit);
		else if (number < 1000000000000000000L) //페타
			numberWithUnit = String.format("%,.1fP%s", number/1000000000000000., unit);	
		else
			numberWithUnit = String.format("%,dbyte%s", number, unit);
		
		if(min == 1)
		    numberWithUnit = "-"+numberWithUnit;
		return numberWithUnit;
	}
	
	public static String toStringWithIntUnit(int number, String unit)
	{		
		String numberWithUnit;
		if (number < 1000)
			numberWithUnit = String.valueOf(number) + unit;
		else if (number < 1000000)
			numberWithUnit = String.format("%,.1fk%s", number/1000., unit);
		else if (number < 1000000000)
			numberWithUnit = String.format("%,.1fM%s", number/1000000., unit);
		else if (number < 1000000000000L)
			numberWithUnit = String.format("%,.1fG%s", number/1000000000., unit);
		else if (number < 1000000000000000L)
			numberWithUnit = String.format("%,.1fT%s", number/1000000000000., unit);
		else if (number < 1000000000000000000L) //페타
			numberWithUnit = String.format("%,.1fP%s", number/1000000000000000., unit);	
		else
			numberWithUnit = String.format("%,dbyte%s", number, unit);
		return numberWithUnit;
	}
	
	
	public static String toStringWithUnitchart(Long number)
	{
		if (null == number)
		{
			number = 0l;
		}	
		String numberWithUnit;
		if (number < 1000000000000000000L)
		{
			numberWithUnit = String.format("%,.2f", number/1000000000.);
		}
		else
		{
			numberWithUnit = String.format("%,d%s", number);
		}			
		return numberWithUnit;
	}
	public static String toStringWithPercentageChart(Long number, String unit)//단위에 %표시
	{
		String numUtil;
		if (null == number)
		{
			number = 0l;
		}
		
		if(number>0)
		{
			numUtil = String.format("%, d%s", number, unit)+"%";
		}
		else
		{
			numUtil = String.format("%, d%s", number, unit);
		}
				
		return numUtil;
	}
	public static String toStringPercentageValue(Long denominator, Long numerator)
    {
	    double doublePercentage = ((double)denominator / (double)numerator) * 100;
	    
	    String strVal = String.format("%.2f", doublePercentage);           
        return strVal;
    }
	public static String toStringInteger(Integer number)//Integer형 String으로 표시
	{
		String numUtil;
		if(number>0)
		{
			numUtil = Integer.toString(number)+"%";
		}
		else
		{
			numUtil = Integer.toString(number);
		}
		return numUtil;
	}
	
	public static String toStringLong(Long number)//Integer형 String으로 표시
    {
        String numUtil;
        if(number>0)
        {
            numUtil = Long.toString(number)+"%";
        }
        else
        {
            numUtil = Long.toString(number);
        }
        return numUtil;
    }
	
/*	public static Long toLongWithUnit(Long number) {
		if (null == number)
			number = 0l;
		
		String numberWithUnit;
		Long LnumberWithUnit;
		if (number < 1000000000000000000L)	// HDD USED, DATABASE USED 차트 사용 기가로만 표현 G
		{
			numberWithUnit = String.format("%,.1f", number/1000000000.);
			LnumberWithUnit = Long.parseLong(numberWithUnit);
		}
		else
			numberWithUnit = String.format("%,d%s", number);
			LnumberWithUnit = Long.parseLong(numberWithUnit);
		
		return LnumberWithUnit;
	}*/
	
}
