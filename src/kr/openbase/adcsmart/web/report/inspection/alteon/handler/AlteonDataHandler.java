package kr.openbase.adcsmart.web.report.inspection.alteon.handler;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBNameValue;
import kr.openbase.adcsmart.service.snmp.alteon.dto.OBDtoRptFilterInfo;
import kr.openbase.adcsmart.service.snmp.alteon.dto.OBDtoRptInspectionSnmpAlteon;
import kr.openbase.adcsmart.service.utility.OBDateTime;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

/**
 * SNMP, CLI 환경에서 가져온 값들을 보관해준다.
 */
public class AlteonDataHandler
{
	private static final int INSPECTION_INTERVAL_LINK_INFO = 10000;

	private AlteonExecuteHandler handler;
	private OBDtoAdcInfo adcInfo;

	private String sysGeneral;
	private String sysPs;
	private String sysFan;
	private String sysTemp;
	private String cfgSyslog;
	private String statCPUSP;
	private String statCPUMP;
	private String infoLink;
	private String infoVlan;
	private String infoStg;
	private String infoTrunk;
	private String statPort;
	private String infoL3Ip;
	private String infoL3Vrrp;
	private String cfgSlbVstatCur;
	private String statMaint;
	private String cfgNtpCur;
	private String cfgSnmp;

	private OBDtoRptInspectionSnmpAlteon inspectionDto1st;
	private OBDtoRptInspectionSnmpAlteon inspectionDto2nd;
	private OBDtoRptInspectionSnmpAlteon inspectionDto3rd;

	public AlteonDataHandler(OBDtoAdcInfo adcInfo)
	{
		this.handler = new AlteonExecuteHandler();

		this.adcInfo = adcInfo;
	}

	public void load()
	{
		handler.setConnectionInfo(adcInfo.getAdcIpAddress(), adcInfo.getAdcAccount(), adcInfo.getAdcPasswordDecrypt(),
				adcInfo.getConnService(), adcInfo.getConnPort());

		try
		{
			handler.login();
		} catch (OBExceptionUnreachable | OBExceptionLogin | OBException e)
		{
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, "failed to telnet/ssh login");
		}

		try
		{
			// 명령어 실행 순서를 바꾸면 제대로 데이터를 출력하지 못하는 경우가 있다.
			// 어떤 원인인지 아직 파악하지 못했다.
			sysGeneral = handler.cmndSysGeneral();
			sysPs = handler.cmndSysPs();
			sysFan = handler.cmndSysFan();
			sysTemp = handler.cmndSysTemp();
			statCPUSP = handler.cmndStatCPUSPUsage();
			statCPUMP = handler.cmndCPUMPUsage();
			cfgSyslog = handler.cmndCfgSyslog();
			infoLink = handler.cmndInfoLink();
			infoVlan = handler.cmndInfoVlan();
			infoStg = handler.cmndInfoStg();
			infoTrunk = handler.cmndInfoTrunk();
			statPort = handler.cmndStatPort();
			infoL3Ip = handler.cmndInfoL3IP();
			infoL3Vrrp = handler.cmndInfoL3Vrrp();
			cfgSlbVstatCur = handler.cmndCfgSlbVstatCur();
			statMaint = handler.cmndStatMaint();
			cfgNtpCur = handler.cmndCfgNtpCur();
			cfgSnmp = handler.cmndCfgSnmp();

			inspectionDto1st = handler.getRtpInspection(adcInfo);
			OBDateTime.Sleep(INSPECTION_INTERVAL_LINK_INFO);
			inspectionDto2nd = handler.getRtpInspection(adcInfo);
			OBDateTime.Sleep(INSPECTION_INTERVAL_LINK_INFO);
			inspectionDto3rd = handler.getRtpInspection(adcInfo);
		} catch (Exception e)
		{
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		}

		handler.disconnect();
	}

	public AlteonExecuteHandler getHandler()
	{
		return handler;
	}

	public String getSysGeneral()
	{
		return sysGeneral;
	}

	public String getSysPs()
	{
		return sysPs;
	}

	public String getSysFan()
	{
		return sysFan;
	}

	public String getSysTemp()
	{
		return sysTemp;
	}

	public String getCfgSyslog()
	{
		return cfgSyslog;
	}

	public String getStatCPUSP()
	{
		return statCPUSP;
	}

	public String getStatCPUMP()
	{
		return statCPUMP;
	}

	public String getInfoLink()
	{
		return infoLink;
	}

	public String getInfoVlan()
	{
		return infoVlan;
	}

	public String getInfoStg()
	{
		return infoStg;
	}

	public String getInfoTrunk()
	{
		return infoTrunk;
	}

	public String getStatPort()
	{
		return statPort;
	}

	public String getInfoL3Ip()
	{
		return infoL3Ip;
	}

	public String getInfoL3Vrrp()
	{
		return infoL3Vrrp;
	}

	public String getCfgSlbVstatCur()
	{
		return cfgSlbVstatCur;
	}

	public String getStatMaint()
	{
		return statMaint;
	}

	public String getCfgNtpCur()
	{
		return cfgNtpCur;
	}

	public OBDtoRptInspectionSnmpAlteon getRptInspection1st()
	{
		return inspectionDto1st;
	}

	public OBDtoRptInspectionSnmpAlteon getRptInspection2nd()
	{
		return inspectionDto2nd;
	}

	public OBDtoRptInspectionSnmpAlteon getRptInspection3rd()
	{
		return inspectionDto3rd;
	}

	public ArrayList<OBDtoRptFilterInfo> getFilterInfo()
	{
		return inspectionDto1st.getFilterInfo();
	}

	public ArrayList<OBNameValue> getRealServer()
	{
		return inspectionDto1st.getRealServerStatusList();
	}

	public ArrayList<OBNameValue> getVirtualServerStatusList()
	{
		return inspectionDto1st.getVirtualServerStatusList();
	}

	public String getCfgSnmp()
	{
		return cfgSnmp;
	}

}
