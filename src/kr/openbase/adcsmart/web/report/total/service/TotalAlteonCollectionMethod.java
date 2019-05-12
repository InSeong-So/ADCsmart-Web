package kr.openbase.adcsmart.web.report.total.service;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.SerialNumberParser;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.SoftwareVersionParser;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.CPUMPUsageRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.CPUSPUsageRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.DirectFuncRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.LinkUpLogRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.PowerStateRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.RealServerRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.SLBSessionStatsRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.VirtualServerStateRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.VrrpStateRow;
import kr.openbase.adcsmart.web.report.inspection.alteon.row.column.AlteonColumn;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.total.TotalCollectionMethod;
import kr.openbase.adcsmart.web.report.total.dto.TotalReportNormalDto;

public class TotalAlteonCollectionMethod implements TotalCollectionMethod
{
	private String swVersion;
	private AlteonDataHandler handler;
	private OBDtoAdcInfo adcInfo;

	public TotalAlteonCollectionMethod(OBDtoAdcInfo adcInfo)
	{
		swVersion = adcInfo.getSwVersion();

		handler = new AlteonDataHandler(adcInfo);
		handler.load(); // 데이터를 한 번에 텔넷 로그인을 한 후에 모두 가져온다.

		this.adcInfo = adcInfo;
	}

	/**
	 * 순서가 중요하다.
	 */
	@Override
	public TotalReportNormalDto getSystemInfo(OBReportInfo rptInfo)
	{
		final AlteonColumn alteonColumn = new AlteonColumn();
		TotalReportNormalDto inspection = new TotalReportNormalDto();

		try
		{
			inspection.setAdcType(adcInfo.getAdcType());
			inspection.setResult("정상");
			final String adcName = (adcInfo.getName() == null) ? "" : adcInfo.getName();
			inspection.setHostName(adcName);

			final String model = (adcInfo.getModel() == null) ? "" : adcInfo.getModel();
			inspection.setModelName(model);

			String osVersion = "";
			String serialNumber = "";
			try
			{
				// general info 추출.
				final String cmdSysGeneralResult = handler.getSysGeneral();

				osVersion = new SoftwareVersionParser().parse(cmdSysGeneralResult).get(0);
				serialNumber = new SerialNumberParser().parse(cmdSysGeneralResult).get(0);

			} catch (Exception e)
			{
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
			}

			inspection.setOs(osVersion);
			inspection.setSerial(serialNumber);

			OBDtoInspectionReportRow value = new PowerStateRow(swVersion, handler, adcInfo.getModel())
					.getRow(alteonColumn); // 모델명이 필요하다.
			inspection.setPowerStatus(value.getResult());
			value = new CPUMPUsageRow(swVersion, handler).getRow(alteonColumn);
			if (value.getResult().equals("비정상"))
			{
				inspection.setResult("비정상");
			}
			inspection.setCpu("MP:" + value.getChecklist() + ",");
			value = new CPUSPUsageRow(swVersion, handler).getRow(alteonColumn);
			if (value.getResult().equals("비정상"))
			{
				inspection.setResult("비정상");
			}
			inspection.setCpu(inspection.getCpu() + "SP:" + value.getChecklist());
			if (value.getResult().equals("비정상"))
			{
				inspection.setResult("비정상");
			}
			value = new LinkUpLogRow(swVersion, handler, adcInfo.getIndex()).getRow(alteonColumn);
			inspection.setPortStatus(value.getResult());
			if (value.getResult().equals("비정상"))
			{
				inspection.setResult("비정상");
			}
			value = new VrrpStateRow(swVersion, handler).getRow(alteonColumn);
			inspection.setFailover(value.getChecklist());
			value = new RealServerRow(swVersion, handler).getRow(alteonColumn);
			inspection.setRealServerStatus(value.getChecklist());
			value = new DirectFuncRow(swVersion, handler).getRow(alteonColumn);
			inspection.setDirect(value.getChecklist());
			value = new VirtualServerStateRow(swVersion, handler).getRow(alteonColumn);
			inspection.setVirtualServerStatus(value.getChecklist());
			value = new SLBSessionStatsRow(swVersion, handler).getRow(alteonColumn);
			inspection.setSlbSession(value.getChecklist());

		} catch (Exception e)
		{
			System.out.println("fail");
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		}
		System.out.println("success");
		return inspection;
	}
}
