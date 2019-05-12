package kr.openbase.adcsmart.web.report.total;

import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.web.report.total.dto.TotalReportNormalDto;

public interface TotalCollectionMethod
{
	TotalReportNormalDto getSystemInfo(OBReportInfo rptInfo);
}
