package kr.openbase.adcsmart.service.impl.alteon.handler;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBAdcAlteonV22 extends OBAdcAlteonHandler
{
	private final String SUFFIX_PROMPT_OK 	= "# ";
	
	private final static String        CMND_STRG_ADD_VSERVER_ID    = "/cfg/slb/virt %s/";
	private final static String        CMND_STRG_ADD_VSERVER_VIP   = "/cfg/slb/virt %s/vip %s";
	private final static String        CMND_STRG_ADD_VSERVER_ENABLE= "/cfg/slb/virt %s/ena";
	private final static String        CMND_STRG_ADD_VSERVER_DISABLE= "/cfg/slb/virt %s/dis";
	    
	public void addVirtualServer(String vsID, String vsName, String vIP, Integer useYN) throws OBException
	{
//		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%d. vsName:%s, vIP:%s, useYN:%d", this.serverName, vsID, vsName, vIP, useYN));		
		String output;
		String command;
	
		// ID 추가.
		command = String.format(CMND_STRG_ADD_VSERVER_ID, vsID);
//		OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));		
		output = sendCommand(command);
		if(output.indexOf(SUFFIX_ERROR) >= 0)
		{// 비 정상 상태.
			throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
		}
		
		// Name 추가. 22.0.6.4 버전에서는 지원되지 않음.
//		if((vsName!=null) &&(!vsName.isEmpty()))
//		{
//			command = String.format("/cfg/slb/virt %d/vname %s", vsID, vsName);
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));		
//			output = sendCommand(command, SUFFIX_PROMPT_OK);
//			resultPattern = String.format("Error: ");
//			if(output.indexOf(resultPattern) >= 0)
//			{// 비 정상 상태.
//				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, resultPattern));
//			}
//		}
//		else
//		{
//			command = String.format("/cfg/slb/virt %d/vname %s", vsID, "none");
//			OBSystemLog.debug(OBDefine.LOGFILE_DEBUG, String.format("host:%s, command:%s", this.serverName, command));		
//			output = sendCommand(command, SUFFIX_PROMPT_OK);
//			resultPattern = String.format("Error: ");
//			if(output.indexOf(resultPattern) >= 0)
//			{// 비 정상 상태.
//				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, resultPattern));
//			}
//		}
		// virtual IP 주소 추가.
		if((vIP!=null) &&(!vIP.isEmpty()))
		{
			command = String.format(CMND_STRG_ADD_VSERVER_VIP, vsID, vIP);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}

		// State 설정.
		if(useYN==null)
		{
			command = String.format(CMND_STRG_ADD_VSERVER_ENABLE, vsID);
			output = sendCommand(command, SUFFIX_PROMPT_OK);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}
		else
		{	
			if(useYN == OBDefine.STATE_DISABLE)
			{// disabled
				command = String.format(CMND_STRG_ADD_VSERVER_DISABLE, vsID);
				output = sendCommand(command, SUFFIX_PROMPT_OK);
				if(output.indexOf(SUFFIX_ERROR) >= 0)
				{// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
				}
			}
			else if(useYN == OBDefine.STATE_ENABLE)
			{// disabled
				command = String.format(CMND_STRG_ADD_VSERVER_ENABLE, vsID);
				output = sendCommand(command, SUFFIX_PROMPT_OK);
				if(output.indexOf(SUFFIX_ERROR) >= 0)
				{// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
				}
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));		
	}
	
	//2014.9.16 ykkim. Alteon v22에는 vname명령이 없다. 
	// - 그래서 setVirtualServer함수도 부모것을 쓰지 말고 addVirtualServer처럼 vname을 빼야하므로 옮겨왔다.
	// - 옮기기 전에는 virtual server 수정을 하면 오류로 실패했다.
	public void setVirtualServer(String vsID, String vsName, String vIP, Integer useYN, String ipaddress) throws OBException
	{
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("start(%s). vsID:%d. vsName:%s, vIP:%s, useYN:%d", this.serverName, vsID, vsName, vIP, useYN));		
		String output;
		String command;
	
		// Name - 이 부분은, 명령이 없으므로 제거한다.
//		if((vsName!=null) &&(!vsName.isEmpty()))
//		{
//			command = String.format("/cfg/slb/virt %d/vname %s", vsID, vsName);
//			output = sendCommand(command);
//			if(output.indexOf(SUFFIX_ERROR) >= 0)
//			{// 비 정상 상태.
//				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
//			}
//		}
//		else
//		{
//			command = String.format("/cfg/slb/virt %d/vname %s", vsID, "none");
//			output = sendCommand(command);
//			if(output.indexOf(SUFFIX_ERROR) >= 0)
//			{// 비 정상 상태.
//				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
//			}
//		}
		// virtual IP 주소 추가.
		if((vIP!=null) &&(!vIP.isEmpty()))
		{
			command = String.format(CMND_STRG_ADD_VSERVER_VIP, vsID, vIP);
			output = sendCommand(command);
			if(output.indexOf(SUFFIX_ERROR) >= 0)
			{// 비 정상 상태.
				throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
			}
		}

		// State 설정.
		if(useYN!=null)
		{
			if(useYN == OBDefine.STATE_DISABLE)
			{// disabled
				command = String.format(CMND_STRG_ADD_VSERVER_DISABLE, vsID);
				output = sendCommand(command);
				if(output.indexOf(SUFFIX_ERROR) >= 0)
				{// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
				}
			}
			else if(useYN == OBDefine.STATE_ENABLE)
			{// disabled
				command = String.format(CMND_STRG_ADD_VSERVER_ENABLE, vsID);
				output = sendCommand(command);
				if(output.indexOf(SUFFIX_ERROR) >= 0)
				{// 비 정상 상태.
					throw new OBException(OBException.ERRCODE_SYSTEM_INVALIDDATA, String.format("parsing error(command:%s, result:%s)", command, output));
				}
			}
		}
		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, String.format("end"));		
	}
}
