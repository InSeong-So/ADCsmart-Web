package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoTrafficMapVServer;


public class VirtualSvrTrafficDto {
	private Integer vs_status;
	private String vs_name;
	private Long in_bps;
	private Long out_bps;
	private Long in_pps;
	private Long out_pps;
	private Long active_connections;
	private Long max_connections;
	private Long total_connections;
	
	public static VirtualSvrTrafficDto fromOBDtoTrafficMapVServer(OBDtoTrafficMapVServer trafficMapFromSvc) {
		VirtualSvrTrafficDto traffic = new VirtualSvrTrafficDto();
		traffic.setVs_status(trafficMapFromSvc.getStatus());
		traffic.setVs_name(trafficMapFromSvc.getVsName());
		traffic.setIn_bps(trafficMapFromSvc.getBpsIn());
		traffic.setOut_bps(trafficMapFromSvc.getBpsOut());
		traffic.setIn_pps(trafficMapFromSvc.getPpsIn());
		traffic.setOut_pps(trafficMapFromSvc.getPpsOut());
		traffic.setActive_connections(trafficMapFromSvc.getCurConns());
		traffic.setMax_connections(trafficMapFromSvc.getMaxConns());
		traffic.setTotal_connections(trafficMapFromSvc.getTotConns());
		return traffic;
	}
	
	public static List<VirtualSvrTrafficDto> fromOBDtoTrafficMapVServer(List<OBDtoTrafficMapVServer> trafficMapsFromSvc) {
		List<VirtualSvrTrafficDto> traffics = new ArrayList<VirtualSvrTrafficDto>();
		for (OBDtoTrafficMapVServer e : trafficMapsFromSvc)
			traffics.add(fromOBDtoTrafficMapVServer(e));
		
		return traffics;
	}
	
	public Integer getVs_status() {
		return vs_status;
	}
	public void setVs_status(Integer vs_status) {
		this.vs_status = vs_status;
	}
	public String getVs_name() {
		return vs_name;
	}
	public void setVs_name(String vs_name) {
		this.vs_name = vs_name;
	}
	public Long getIn_bps() {
		return in_bps;
	}
	public void setIn_bps(Long in_bps) {
		this.in_bps = in_bps;
	}
	public Long getOut_bps() {
		return out_bps;
	}
	public void setOut_bps(Long out_bps) {
		this.out_bps = out_bps;
	}
	public Long getIn_pps() {
		return in_pps;
	}
	public void setIn_pps(Long in_pps) {
		this.in_pps = in_pps;
	}
	public Long getOut_pps() {
		return out_pps;
	}
	public void setOut_pps(Long out_pps) {
		this.out_pps = out_pps;
	}
	public Long getActive_connections() {
		return active_connections;
	}
	public void setActive_connections(Long active_connections) {
		this.active_connections = active_connections;
	}
	public Long getMax_connections() {
		return max_connections;
	}
	public void setMax_connections(Long max_connections) {
		this.max_connections = max_connections;
	}
	public Long getTotal_connections() {
		return total_connections;
	}
	public void setTotal_connections(Long total_connections) {
		this.total_connections = total_connections;
	}
	@Override
	public String toString() {
		return "VServerTrafficDto [vs_status=" + vs_status + ", vs_name=" + vs_name + ", in_bps=" + in_bps
				+ ", out_bps=" + out_bps + ", in_pps=" + in_pps + ", out_pps=" + out_pps + ", active_connections="
				+ active_connections + ", max_connections=" + max_connections + ", total_connections="
				+ total_connections + "]";
	}
	
}
