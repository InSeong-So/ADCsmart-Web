package kr.openbase.adcsmart.service.impl.alteon;

import kr.openbase.adcsmart.service.vdirect.dto.CommandDto;
import kr.openbase.adcsmart.service.vdirect.dto.ConfigAlteonDto;
import kr.openbase.adcsmart.service.vdirect.dto.ConfigSlbDto;
import kr.openbase.adcsmart.service.vdirect.dto.ServerEnableDisableDto;

public interface OBVdirectService
{
	public String createAlteon(ConfigAlteonDto alteon);
	public String configSnmp(ConfigAlteonDto alteon);
	public String configCli(ConfigAlteonDto alteon);
	public String deleteAlteon(ConfigAlteonDto alteon);
	public String deleteSnmp(ConfigAlteonDto alteon);
	public String deleteCli(ConfigAlteonDto alteon);
    public ConfigSlbDto createSlb();
    
    public String createVirtualServer(ConfigSlbDto virtualServer);
    public String deleteVirtualServer(ConfigSlbDto virtualServer);
    public String changeVirtualServerState(ServerEnableDisableDto virtualServer);
    
    public String createVirtualService(ConfigSlbDto service);
    public String deleteVirtualService(ConfigSlbDto service);
    
	public String createGroupServer(ConfigSlbDto group);
	public String deleteGroupServer(ConfigSlbDto group);
	public String changeGroupState(ServerEnableDisableDto group);
	
	public String createRealServer(ConfigSlbDto real);
	public String deleteRealServer(ConfigSlbDto real);
	public String changeRealSeverStatus(ServerEnableDisableDto real);
	
	public String createVrrp(ConfigSlbDto vrrp);
	public String deleteVrrp(ConfigSlbDto vrrp);
	
	public String sendCommand(CommandDto command);
}
