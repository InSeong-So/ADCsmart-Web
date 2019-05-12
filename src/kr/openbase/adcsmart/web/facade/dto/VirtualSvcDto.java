package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;

public class VirtualSvcDto {
	private Integer svcPort;
	private Integer realPort;
	private AdcPoolDto pool;
	
	public static VirtualSvcDto toVirtualSvcDto(OBDtoAdcVService virtualSvcFromSvc) {
		VirtualSvcDto virtualSvc = new VirtualSvcDto();
		virtualSvc.setSvcPort(virtualSvcFromSvc.getServicePort());
		virtualSvc.setRealPort(virtualSvcFromSvc.getRealPort());
		virtualSvc.setPool(AdcPoolDto.toAdcPoolDto(virtualSvcFromSvc.getPool()));
		return virtualSvc;
	}

	public static List<VirtualSvcDto> toVirtualSvcDto(List<OBDtoAdcVService> virtualSvcsFromSvc) {
		List<VirtualSvcDto> virtualSvcs = new ArrayList<VirtualSvcDto>();
		if (virtualSvcsFromSvc == null)
			return virtualSvcs;
		
		for (OBDtoAdcVService e : virtualSvcsFromSvc)
			virtualSvcs.add(toVirtualSvcDto(e));

		return virtualSvcs;
	}
	
	public static OBDtoAdcVService toOBDtoAdcVService(VirtualSvcDto virtualSvc) {
		OBDtoAdcVService virtualSvcFromSvc = new OBDtoAdcVService();
		virtualSvcFromSvc.setServicePort(virtualSvc.getSvcPort());
		virtualSvcFromSvc.setRealPort(virtualSvc.getRealPort());
		virtualSvcFromSvc.setPool(AdcPoolDto.toOBDtoAdcPoolAlteon(virtualSvc.getPool()));
		return virtualSvcFromSvc;
	}

	public static List<OBDtoAdcVService> toOBDtoAdcVService(List<VirtualSvcDto> virtualSvcs) {
		List<OBDtoAdcVService> virtualSvcsFromSvc = new ArrayList<OBDtoAdcVService>();
		for (VirtualSvcDto virtualSvc : virtualSvcs)
			virtualSvcsFromSvc.add(toOBDtoAdcVService(virtualSvc));

		return virtualSvcsFromSvc;
	}
	
	public String toJSON() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public Integer getSvcPort() {
		return svcPort;
	}

	public void setSvcPort(Integer svcPort) {
		this.svcPort = svcPort;
	}

	public Integer getRealPort() {
		return realPort;
	}

	public void setRealPort(Integer realPort) {
		this.realPort = realPort;
	}

	public AdcPoolDto getPool() {
		return pool;
	}

	public void setPool(AdcPoolDto pool) {
		this.pool = pool;
	}

	@Override
	public String toString() {
		return "VirtualSvcDto [svcPort=" + svcPort + ", realPort=" + realPort + ", pool=" + pool + "]";
	}

}
