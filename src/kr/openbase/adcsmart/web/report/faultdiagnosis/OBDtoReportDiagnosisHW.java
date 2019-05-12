package kr.openbase.adcsmart.web.report.faultdiagnosis;

import java.util.ArrayList;

import kr.openbase.adcsmart.web.report.impl.OBDtoReportTextHdr;

public class OBDtoReportDiagnosisHW
{
	private ArrayList<OBDtoReportDiagnosisElement> hwList;
	private OBDtoReportTextHdr textHdr;
	
	@Override
	public String toString()
	{
		return "OBDtoReportDiagnosisHW [hwList=" + hwList + ", textHdr=" + textHdr + "]";
	}

	public OBDtoReportTextHdr getTextHdr()
	{
		return textHdr;
	}

	public void setTextHdr(OBDtoReportTextHdr textHdr)
	{
		this.textHdr = textHdr;
	}

	public ArrayList<OBDtoReportDiagnosisElement> getHwList()
	{
		return hwList;
	}

	public void setHwList(ArrayList<OBDtoReportDiagnosisElement> hwList)
	{
		this.hwList = hwList;
	}
}
