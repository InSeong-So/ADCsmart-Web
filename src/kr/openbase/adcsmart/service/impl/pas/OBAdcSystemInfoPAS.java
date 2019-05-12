package kr.openbase.adcsmart.service.impl.pas;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoAdcSnmpInfo;
import kr.openbase.adcsmart.service.impl.OBAdcSystemInfo;
import kr.openbase.adcsmart.service.impl.dto.OBDtoAdcSystemInfo;
import kr.openbase.adcsmart.service.impl.pas.handler.OBAdcPASHandler;
import kr.openbase.adcsmart.service.snmp.dto.DtoSnmpAdcResc;
import kr.openbase.adcsmart.service.snmp.pas.OBSnmpPAS;
import kr.openbase.adcsmart.service.utility.OBCipherAES;
import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBExceptionLogin;
import kr.openbase.adcsmart.service.utility.OBExceptionUnreachable;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcSystemInfoPAS implements OBAdcSystemInfo
{
    // public static void main(String[] args)
    // {
    // try
    // {
    // String name = new OBAdcSystemInfoPAS().getAdcHostName("192.168.200.120", "", "default");
    // System.out.println(name);
    // }
    // catch(Exception e)
    // {
    // e.printStackTrace();
    // }
    // }

    @Override
    public String getAdcHostName(String ipAddress, String swVersion, OBDtoAdcSnmpInfo snmpInfo) throws OBException
    {
        return new OBSnmpPAS(ipAddress, snmpInfo).getAdcHostname(OBDefine.ADC_TYPE_PIOLINK_PAS, "");
    }

    private OBDtoAdcSystemInfo getAdcSystemInfoSnmp(OBDtoAdcInfo adcInfo, int mode) throws OBException
    {
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(index:%s)", adcInfo));
        OBDtoAdcSystemInfo info = new OBDtoAdcSystemInfo();

        try
        {
            info.setAdcIndex(adcInfo.getIndex());
            info.setStatus(OBDefine.ADC_STATUS.REACHABLE);

            DtoSnmpAdcResc snmp;
            snmp = new OBSnmpPAS(adcInfo.getAdcIpAddress(), adcInfo.getSnmpInfo()).getAdcResc(OBDefine.ADC_TYPE_PIOLINK_PAS, adcInfo.getSwVersion());

            if(snmp != null)
            {
                info.setHostName(snmp.getName());
                info.setModel(snmp.getModel());
                info.setSwVersion(snmp.getSwVersion());
                info.setLastBootTime(snmp.getUpTime());
            }
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
        OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:%s", info));
        return info;
    }

    // public static void main(String[] args)
    // {
    // try
    // {
    // OBDtoAdcSystemInfo name = new OBAdcSystemInfoPAS().getAdcSystemInfo(5, "192.168.200.110", "root", "admin", "", "default");
    // System.out.println(name);
    // }
    // catch(OBExceptionUnreachable e)
    // {
    // e.printStackTrace();
    // }
    // catch(OBExceptionLogin e)
    // {
    // e.printStackTrace();
    // }
    // catch(OBException e)
    // {
    // e.printStackTrace();
    // }
    // }

    @Override
    public OBDtoAdcSystemInfo getAdcSystemInfo(Integer adcIndex, String ipaddress, String account, String password, int connService, int connPort, String swVersion, OBDtoAdcSnmpInfo snmpInfo, int opMode) throws OBExceptionUnreachable, OBExceptionLogin, OBException
    {
        OBDtoAdcSystemInfo info;

        try
        {
            OBDtoAdcInfo adcInfo = new OBDtoAdcInfo();
            adcInfo.setIndex(adcIndex);
            adcInfo.setAdcIpAddress(ipaddress);
            adcInfo.setAdcAccount(account);
            adcInfo.setAdcPassword(new OBCipherAES().Encrypt(password));
            adcInfo.setSwVersion(swVersion);
            adcInfo.setSnmpInfo(snmpInfo);
            adcInfo.setOpMode(opMode);

            info = getAdcSystemInfoSnmp(adcInfo, 0);
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }

        return info;
    }

    @Override
    public boolean isAvailableSystem(String ipaddress, String account, String password, int connService, int connPort, String cliAccount, String cliPassword) throws OBExceptionUnreachable, OBExceptionLogin, OBException
    {
        try
        {
            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("start"));
            OBAdcPASHandler pas;
            pas = new OBAdcPASHandler();
            pas.setConnectionInfo(ipaddress, account, password, connService, connPort);
            pas.login();
            pas.disconnect();
            OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("end. result:true"));

            return true;
        }
        catch(OBExceptionUnreachable e)
        {
            throw e;
        }
        catch(OBExceptionLogin e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(e.getMessage());
        }
    }

    public String getCfgDump(int adcIndex, String ipaddress, String account, String password, String swVersion, int connService, int connPort) throws OBExceptionUnreachable, OBExceptionLogin, OBException
    {
        OBAdcPASHandler pasHandler;
        // if(swVersion == null || swVersion.isEmpty())
        // {
        pasHandler = new OBAdcPASHandler(ipaddress, account, password, connService, connPort);
        // }

        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start"));
        try
        {
            // pasHandler.setSocketTimeout(10000);//10초로 변경.
            pasHandler.login();
        }
        catch(OBExceptionUnreachable e)
        {
            throw new OBExceptionUnreachable(e.getMessage());
        }
        catch(OBExceptionLogin e)
        {
            throw new OBExceptionLogin(e.getMessage());
        }
        String result = "";
        try
        {
            result = pasHandler.cmndDumpcfg();
        }
        catch(Exception e)
        {
            pasHandler.disconnect();
            throw new OBException(e.getMessage());
        }
        pasHandler.disconnect();
        OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));
        return result;
    }

    @Override
    public String getAdcSWVersionCli(String adcIPAddress, String adcCLIAccount, String adcCLIPW, String swVersion, int connService, int connPort) throws OBException
    {
        throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, "not supported function");
    }

    @Override
    public String getAdcSWVersionSnmp(String adcIPAddress, OBDtoAdcSnmpInfo snmpInfo) throws OBException
    { // snmp를 이용해서 추출한다.
        String retVal = "";
        try
        {
            retVal = new OBSnmpPAS(adcIPAddress, snmpInfo).getAdcSWVersion(OBDefine.ADC_TYPE_PIOLINK_PAS, "");
        }
        catch(OBException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new OBException(OBException.ERRCODE_SYSTEM_ILLEGALNULL, e.getMessage());
        }
        return retVal;
    }
}
