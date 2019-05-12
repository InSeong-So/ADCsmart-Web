package kr.openbase.adcsmart.service.impl.fault;

import java.util.ArrayList;

import kr.openbase.adcsmart.service.dto.OBDtoAdcInfo;
import kr.openbase.adcsmart.service.dto.OBDtoElement;
import kr.openbase.adcsmart.service.dto.OBDtoReturnInfo;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckLog;
import kr.openbase.adcsmart.service.dto.fault.OBDtoFaultCheckResultElement;
import kr.openbase.adcsmart.service.utility.OBException;

public interface OBFaultCheckHandler
{
	public void loadSnmpOidHWInfo() throws OBException;
	
	public void setParameter(long logKey, OBDtoFaultCheckLog logInfo, OBDtoAdcInfo adcInfo, Object CLIObj) throws OBException;

	public OBDtoFaultCheckResultElement checkHWPowerSupply(OBDtoElement obj) throws OBException;
	public OBDtoFaultCheckResultElement checkHWUptime(OBDtoElement obj) throws OBException;
	public OBDtoFaultCheckResultElement checkHWLicense(OBDtoElement obj) throws OBException;
	public OBDtoFaultCheckResultElement checkHWPortInterface(OBDtoElement obj, Integer adcIndex) throws OBException;
	public OBDtoFaultCheckResultElement checkHWCpuStatus(OBDtoElement obj, int max) throws OBException;
	public OBDtoFaultCheckResultElement checkHWMemoryStatus(OBDtoElement obj, int max) throws OBException;
	public OBDtoFaultCheckResultElement checkHWTemperature(OBDtoElement obj) throws OBException;
	public OBDtoFaultCheckResultElement checkHWFanStatus(OBDtoElement obj, int min, int max) throws OBException;
	public OBDtoFaultCheckResultElement checkHWAdclog(OBDtoElement obj, Integer logCount) throws OBException;
	public OBDtoFaultCheckResultElement checkHWOSInfo(OBDtoElement obj, OBDtoAdcInfo adcInfo) throws OBException;

	public OBDtoFaultCheckResultElement checkL23VlanInfo(OBDtoElement obj) throws OBException;
	public OBDtoFaultCheckResultElement checkL23StpInfo(OBDtoElement obj) throws OBException;
	public OBDtoFaultCheckResultElement checkL23TrunkInfo(OBDtoElement obj) throws OBException;
	public OBDtoFaultCheckResultElement checkL23VrrpInfo(OBDtoElement obj) throws OBException;
	public OBDtoFaultCheckResultElement checkL23RoutingInfo(OBDtoElement obj) throws OBException;
	public OBDtoFaultCheckResultElement checkL23InterfaceInfo(OBDtoElement obj) throws OBException;
	
	//유휴 SLB component
	public OBDtoFaultCheckResultElement checkL47NotUsedSLB(OBDtoElement obj) throws OBException;
	//설정했지만 데이터없는 SLB component
	public OBDtoFaultCheckResultElement checkL47SleepSLB(OBDtoElement obj, Integer sleepDay) throws OBException;
	//유휴 FLB component
	public OBDtoFaultCheckResultElement checkL47UnsedFLB(OBDtoElement obj) throws OBException;
	
	public OBDtoFaultCheckResultElement checkL47SessionTable(OBDtoElement obj) throws OBException;
	
	public OBDtoFaultCheckResultElement startSvcPacketDump(OBDtoElement obj, Long logKey, String clientIPAddress, String serverIPAddress, Integer svcPort) throws OBException;
	public boolean stopSvcPacketDump() throws OBException;
	public OBDtoReturnInfo isSvcPacketDumpProgress(String dumpFileName) throws OBException;
	public boolean         isSvcPacketDumpSizeExceed(String dumpFileName, Long baseSize) throws OBException;
	public OBDtoFaultCheckResultElement isSvcPacketAvaliable(OBDtoElement obj, Long logKey) throws OBException;
	
	public OBDtoReturnInfo downloadDumpFile(String remoteFileName, String localFileName, String srvIPAddress) throws OBException;
	public OBDtoFaultCheckResultElement checkSvcResponseTime(OBDtoElement obj, String pszFileName, String pszClientIPAddress, String pszVIPAddress, ArrayList<String> realIPAddress) throws OBException;
	public OBDtoFaultCheckResultElement checkSvcLoadBalancing(OBDtoElement obj) throws OBException;
	public OBDtoFaultCheckResultElement checkSvcPacketLoss(OBDtoElement obj, Long logKey, String fileName, String clientIPAddress, String vsIPAddress, ArrayList<String> realIPAddress) throws OBException;
	
}
