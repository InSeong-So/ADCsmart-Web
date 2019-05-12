package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoNetworkInterface;


public class InterfaceDto {
	private Integer id;
	private String ip;
	
	public static InterfaceDto toInterfaceDto(OBDtoNetworkInterface ifaceFromSvc) {
		InterfaceDto iface = new InterfaceDto();
		iface.setId(ifaceFromSvc.getIfNum());
		iface.setIp(ifaceFromSvc.getIpAddress());
		return iface;
	}

	public static List<InterfaceDto> toInterfaceDto(List<OBDtoNetworkInterface> ifacesFromSvc) {
		ArrayList<InterfaceDto> ifaces = new ArrayList<InterfaceDto>();
		for (OBDtoNetworkInterface e : ifacesFromSvc)
			ifaces.add(toInterfaceDto(e));

		return ifaces;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Override
	public String toString() {
		return "InterfaceDto [id=" + id + ", ip=" + ip + "]";
	}
	
}
