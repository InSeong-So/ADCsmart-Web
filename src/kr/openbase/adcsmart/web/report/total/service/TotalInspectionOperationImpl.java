package kr.openbase.adcsmart.web.report.total.service;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.report.inspection.impl.InspectionReportImpl;
import kr.openbase.adcsmart.web.report.total.TotalCollectionMethod;
import kr.openbase.adcsmart.web.report.total.dto.TotalReportNormalDto;

public class TotalInspectionOperationImpl
{

	private TotalCollectionMethod reportManager;

	public TotalCollectionMethod getInstance(OBDtoAdcInfo adcInfo)
	{
		return getValidInstance(adcInfo);
	}

//	public TotalInspectionOperationImpl(OBDtoAdcInfo adcInfo) throws OBException	{
//		reportManager = getValidInstance(adcInfo);
//		if (reportManager == null){
//			throw new OBException(OBDefine.LOGFILE_SYSTEM, "not supported vendor");
//		}
//	}

	public TotalReportNormalDto getSystemInfo(String rptIndex) throws OBException
	{

		if (reportManager != null)
		{
			OBReportInfo rptInfo = new InspectionReportImpl().getReportInfo(rptIndex);
			return reportManager.getSystemInfo(rptInfo);
		}

		return null;
	}

	public TotalCollectionMethod getValidInstance(OBDtoAdcInfo adcInfo)
	{
		switch (adcInfo.getAdcType())
		{
		case OBDefine.ADC_TYPE_ALTEON:
			return new TotalAlteonCollectionMethod(adcInfo);
		case OBDefine.ADC_TYPE_F5:
			return new TotalF5CollectionMethod(adcInfo);
		}
		return null;
	}
}
