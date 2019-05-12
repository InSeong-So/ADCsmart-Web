package kr.openbase.adcsmart.web.report.impl;

import java.util.ArrayList;

public class OBDtoReportTextHdr
{
	private ArrayList<String> textList;

	public synchronized static OBDtoReportTextHdr toTextHdr(String ... args)
	{
		ArrayList<String> textList = new ArrayList<String>();
		OBDtoReportTextHdr retVal = new OBDtoReportTextHdr();
		for(String arg:args)
			textList.add(arg);
		retVal.setTextList(textList);
		return retVal; 
	}

	public ArrayList<String> getTextList()
	{
		return textList;
	}

	public void setTextList(ArrayList<String> textList)
	{
		this.textList = textList;
	}
}
