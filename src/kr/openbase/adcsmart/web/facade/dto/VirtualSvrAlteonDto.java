package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.utility.OBDefine;

public class VirtualSvrAlteonDto extends VirtualSvrDto {
	//private Boolean vrrpFlag; //안씀. 아래 vrrpState로 대체
	private Integer vrrpState;
	private String alteonId;
	private Integer routerId;
	private Integer vrId;
	private Integer intefaceNo;
	private Integer nwclassId;//네트워크 class 설정 여부 0:설정 없음, 1:설정
	private String subInfo;//기타 정보칸, virtual server에 할당된 network class 가 있다.
	private List<VirtualSvcDto> virtualSvcs;
	
	public static VirtualSvrAlteonDto toVirtualSvrDto(OBDtoAdcVServerAlteon vsFromSvc) {
		VirtualSvrAlteonDto virtualSvr = new VirtualSvrAlteonDto();
		if(vsFromSvc.getIndex()!=null)
			virtualSvr.setIndex(vsFromSvc.getIndex());
		if(vsFromSvc.getAdcIndex()!=null)
			virtualSvr.setAdcIndex(vsFromSvc.getAdcIndex());
		if(vsFromSvc.getName()!=null)
			virtualSvr.setName(vsFromSvc.getName());
		if(vsFromSvc.getvIP()!=null)
			virtualSvr.setVirtualIp(vsFromSvc.getvIP());
		if(vsFromSvc.getAlteonId()!=null)
			virtualSvr.setAlteonId(vsFromSvc.getAlteonId());
		if(vsFromSvc.getApplyTime()!=null)
			virtualSvr.setLastUpdateTime(vsFromSvc.getApplyTime());
		if(vsFromSvc.getStatus()!=null)
		{
			switch (vsFromSvc.getStatus()) {
				case OBDefine.STATUS_BLOCK:
					virtualSvr.setStatus("block");	break;
				case OBDefine.STATUS_DISABLE:
					virtualSvr.setStatus("disable");	break;
				case OBDefine.STATUS_AVAILABLE:
					virtualSvr.setStatus("available");	break;
				case OBDefine.STATUS_UNAVAILABLE:
					virtualSvr.setStatus("unavailable");	break;
				default:
					virtualSvr.setStatus("unknown");
			}
		}
		
		if(vsFromSvc.getVrrpState()!=null)
			virtualSvr.setVrrpState(vsFromSvc.getVrrpState());
		if(vsFromSvc.getRouterIndex()!=null)
			virtualSvr.setRouterId(vsFromSvc.getRouterIndex());
		if(vsFromSvc.getVrIndex()!=null)
			virtualSvr.setVrId(vsFromSvc.getVrIndex());
		if(vsFromSvc.getIfNum()!=null)
			virtualSvr.setIntefaceNo(vsFromSvc.getIfNum());
		if(vsFromSvc.getNwclassId()!=null)
			virtualSvr.setNwclassId(vsFromSvc.getNwclassId());
		if(vsFromSvc.getSubInfo()!=null)
			virtualSvr.setSubInfo(vsFromSvc.getSubInfo());
		if(vsFromSvc.getVserviceList()!=null)
			virtualSvr.setVirtualSvcs(VirtualSvcDto.toVirtualSvcDto(vsFromSvc.getVserviceList()));
		
		return virtualSvr;
	}
	
	public static List<VirtualSvrAlteonDto> toVirtualSvrAlteonDto(List<OBDtoAdcVServerAlteon> vssFromSvc) {
		List<VirtualSvrAlteonDto> vs = new ArrayList<VirtualSvrAlteonDto>();
		if(vssFromSvc==null)
			return vs;
		for (OBDtoAdcVServerAlteon vsFromSvc : vssFromSvc)
			vs.add(toVirtualSvrDto(vsFromSvc));
		
		return vs;
	}
	
	public String getAlteonId() {
		return alteonId;
	}
	public void setAlteonId(String alteonId) {
		this.alteonId = alteonId;
	}
	public Integer getRouterId() {
		return routerId;
	}
	public void setRouterId(Integer routerId) {
		this.routerId = routerId;
	}
	public Integer getVrId() {
		return vrId;
	}
	public void setVrId(Integer vrId) {
		this.vrId = vrId;
	}
	public Integer getIntefaceNo() {
		return intefaceNo;
	}
	public void setIntefaceNo(Integer intefaceNo) {
		this.intefaceNo = intefaceNo;
	}
	public List<VirtualSvcDto> getVirtualSvcs() {
		return virtualSvcs;
	}
	public void setVirtualSvcs(List<VirtualSvcDto> virtualSvcs) {
		this.virtualSvcs = virtualSvcs;
	}
	public Integer getVrrpState()
	{
		return vrrpState;
	}
	public void setVrrpState(Integer vrrpState)
	{
		this.vrrpState = vrrpState;
	}

	public Integer getNwclassId()
	{
		return nwclassId;
	}

	public void setNwclassId(Integer nwclassId)
	{
		this.nwclassId = nwclassId;
	}

	public String getSubInfo()
	{
		return subInfo;
	}
	public void setSubInfo(String subInfo)
	{
		this.subInfo = subInfo;
	}

	@Override
	public String toString()
	{
		return "VirtualSvrAlteonDto [vrrpState=" + vrrpState + ", alteonId="
				+ alteonId + ", routerId=" + routerId + ", vrId=" + vrId
				+ ", intefaceNo=" + intefaceNo + ", nwclassId=" + nwclassId
				+ ", subInfo=" + subInfo + ", virtualSvcs=" + virtualSvcs + "]";
	}
}