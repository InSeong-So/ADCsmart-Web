package kr.openbase.adcsmart.web.report.total.service;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBReportInfo;
import kr.openbase.adcsmart.service.impl.OBTelnetCmndExecV2;
import kr.openbase.adcsmart.service.snmp.f5.OBSnmpF5;
import kr.openbase.adcsmart.service.snmp.f5.dto.OBDtoRptInspectionSnmpF5;
import kr.openbase.adcsmart.service.utility.OBCommon;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.f5.handler.F5DataHandler;
import kr.openbase.adcsmart.web.report.inspection.f5.row.CPULoadRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.ConnectionInfoRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.FailoverLogRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.LinkLogRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.PoolmemberStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.PowerStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.VirtualServerStatusRow;
import kr.openbase.adcsmart.web.report.inspection.f5.row.column.F5Column;
import kr.openbase.adcsmart.web.report.total.TotalCollectionMethod;
import kr.openbase.adcsmart.web.report.total.dto.TotalReportNormalDto;

/**
 * 미완성이다. 차후 구조를 변경하여 적용해야 한다.
 * 
 * @author 최영조
 */
public class TotalF5CollectionMethod implements TotalCollectionMethod
{
	private OBDtoAdcInfo adcInfo;

	public TotalF5CollectionMethod(OBDtoAdcInfo adcInfo)
	{
		this.adcInfo = adcInfo;
	}

	@Override
	public TotalReportNormalDto getSystemInfo(OBReportInfo rptInfo)
	{
		final TotalReportNormalDto inspection = new TotalReportNormalDto();
		final F5Column f5Column = new F5Column();

		// 장비가 살아있는지 확인한다.
		if (!isStandby())
		{
			return inspection;
		}

		final F5DataHandler dataHandler = new F5DataHandler(adcInfo);
		dataHandler.load();
		inspection.setResult("정상");
		String swVersion = adcInfo.getSwVersion();
		inspection.setOs(swVersion);
		inspection.setModelName(adcInfo.getModel());
		inspection.setHostName(adcInfo.getName());

		try
		{

			// snmp를 이용해서 데이터를 가져온다.
			OBSnmpF5 snmp = OBCommon.getValidSnmpF5Handler(adcInfo.getSwVersion(), adcInfo.getAdcIpAddress(),
					adcInfo.getSnmpInfo());
			OBDtoRptInspectionSnmpF5 snmpInfo = snmp.getRptInspection(adcInfo.getAdcType(), adcInfo.getSwVersion());
			String serialNumber = (snmpInfo.getSerialNo() == null) ? "" : snmpInfo.getSerialNo();
			inspection.setSerial(serialNumber);

			OBDtoInspectionReportRow value = new PowerStatusRow(swVersion, dataHandler).getRow(f5Column);
			if (value.getResult().equals("비정상"))
			{
				inspection.setResult("비정상");
			}
			inspection.setPowerStatus(value.getResult());
			value = new CPULoadRow(swVersion, dataHandler).getRow(f5Column);
			if (value.getResult().equals("비정상"))
			{
				inspection.setResult("비정상");
			}
			inspection.setCpu("CPU:" + value.getChecklist());
			value = new LinkLogRow(swVersion, dataHandler, adcInfo.getIndex()).getRow(f5Column);
			inspection.setPortStatus(value.getResult());
			if (value.getResult().equals("비정상"))
			{
				inspection.setResult("비정상");
			}
			value = new FailoverLogRow(swVersion, dataHandler, adcInfo.getIndex()).getRow(f5Column);
			if (value.getResult().equals("비정상"))
			{
				inspection.setResult("비정상");
			}
			inspection.setFailover(value.getResult());
			value = new PoolmemberStatusRow(swVersion, dataHandler).getRow(f5Column);
			inspection.setRealServerStatus(value.getChecklist());
			value = new VirtualServerStatusRow(swVersion, dataHandler).getRow(f5Column);
			inspection.setVirtualServerStatus(value.getChecklist());
			value = new ConnectionInfoRow(swVersion, dataHandler).getRow(f5Column);
			inspection.setSlbSession(value.getChecklist());

		} catch (Exception e)
		{
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		}

		return inspection;
	}

	private boolean isStandby()
	{
		boolean isStandby = false;
		try
		{
			isStandby = new OBTelnetCmndExecV2().isReachable(adcInfo.getAdcIpAddress(), adcInfo.getConnPort());
		} catch (OBException e)
		{
			// do nothing
		}

		return isStandby;
	}
}
