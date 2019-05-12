package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcVServerAlteon;
import kr.openbase.adcsmart.service.dto.OBDtoAdcVService;
import kr.openbase.adcsmart.web.json.VirtualSvcJsonAdapter;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

//@Conversion()
public class AlteonVsAddDto {
	private String index;
	private Integer adcIndex;
	private String name;
	private String ip;
	private String alteonId;
	private Integer vrrpState;
	private Integer routerId;
	private Integer vrId;
	private Integer interfaceNo;
	private String subInfo;//기타 정보칸, virtual server에 할당된 network class 가 있다.
	private List<VirtualSvcDto> virtualSvcs = new ArrayList<VirtualSvcDto>();
	private String virtualSvcsInString;
	
	public static OBDtoAdcVServerAlteon toOBDtoAdcVServer(AlteonVsAddDto vsAdd) {
		OBDtoAdcVServerAlteon vsFromSvc = new OBDtoAdcVServerAlteon();
		vsFromSvc.setIndex(vsAdd.getIndex());
		vsFromSvc.setAdcIndex(vsAdd.getAdcIndex());
		vsFromSvc.setName(vsAdd.getName());
		vsFromSvc.setvIP(vsAdd.getIp());
		vsFromSvc.setAlteonId(vsAdd.getAlteonId());
		vsFromSvc.setVrrpState(vsAdd.getVrrpState());
		vsFromSvc.setIfNum(vsAdd.getInterfaceNo());
		vsFromSvc.setRouterIndex(vsAdd.getRouterId());
		vsFromSvc.setVrIndex(vsAdd.getVrId());
		vsFromSvc.setVserviceList(new ArrayList<OBDtoAdcVService>(VirtualSvcDto.toOBDtoAdcVService(vsAdd.getVirtualSvcs())));
		return vsFromSvc;
	}
	
	private void convertVirtualSvcsToJSON() throws Exception {
		if (StringUtils.isEmpty(virtualSvcsInString))
			return;
		
//		virtualSvcsInString = "[{\"svcPort\":\"443\",\"realPort\":\"443\",\"pool\":{\"index\":\"vs_Pool_2\",\"name\":\"vs_Pool_2\",\"loadBalancingType\":\"Round Robin\",\"members\":[{\"ip\":\"2.2.2.2\",\"isEnabled\":\"true\"}]}}]";
		virtualSvcs = new ArrayList<VirtualSvcDto>();
		Gson gson = new GsonBuilder().registerTypeAdapter(VirtualSvcDto.class, new VirtualSvcJsonAdapter()).create();
		JsonParser parser = new JsonParser();
		try {
			JsonArray jarray = parser.parse(virtualSvcsInString).getAsJsonArray();
			for (JsonElement e : jarray)
				virtualSvcs.add(gson.fromJson(e, VirtualSvcDto.class));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	public String getIndex() 
	{
		return index;
	}
	public void setIndex(String index) 
	{
		this.index = index;
	}
	public Integer getAdcIndex() 
	{
		return adcIndex;
	}
	public void setAdcIndex(Integer adcIndex) 
	{
		this.adcIndex = adcIndex;
	}
	public String getName() 
	{
		return name;
	}
	public void setName(String name) 
	{
		this.name = name;
	}
	public String getIp() 
	{
		return ip;
	}
	public void setIp(String ip) 
	{
		this.ip = ip;
	}
	public String getAlteonId() 
	{
		return alteonId;
	}
	public void setAlteonId(String alteonId) 
	{
		this.alteonId = alteonId;
	}
	public Integer getVrrpState()
	{
		return vrrpState;
	}
	public void setVrrpState(Integer vrrpState)
	{
		this.vrrpState = vrrpState;
	}
	public Integer getRouterId() 
	{
		return routerId;
	}
	public void setRouterId(Integer routerId) 
	{
		this.routerId = routerId;
	}
	public Integer getVrId() 
	{
		return vrId;
	}
	public void setVrId(Integer vrId) 
	{
		this.vrId = vrId;
	}
	public Integer getInterfaceNo() 
	{
		return interfaceNo;
	}
	public void setInterfaceNo(Integer interfaceNo) 
	{
		this.interfaceNo = interfaceNo;
	}	
	public String getSubInfo()
	{
		return subInfo;
	}
	public void setSubInfo(String subInfo)
	{
		this.subInfo = subInfo;
	}
	public List<VirtualSvcDto> getVirtualSvcs() 
	{
		return virtualSvcs;
	}
	public void setVirtualSvcs(List<VirtualSvcDto> virtualSvcs) 
	{
		this.virtualSvcs = virtualSvcs;
	}	
	public String getVirtualSvcsInString() 
	{
		return virtualSvcsInString;
	}
	public void setVirtualSvcsInString(String virtualSvcsInString) throws Exception 
	{
		this.virtualSvcsInString = virtualSvcsInString;
		convertVirtualSvcsToJSON();
	}

	@Override
	public String toString()
	{
		return "AlteonVsAddDto [index=" + index + ", adcIndex=" + adcIndex
				+ ", name=" + name + ", ip=" + ip + ", alteonId=" + alteonId
				+ ", vrrpState=" + vrrpState + ", routerId=" + routerId
				+ ", vrId=" + vrId + ", interfaceNo=" + interfaceNo
				+ ", subInfo=" + subInfo + ", virtualSvcs=" + virtualSvcs
				+ ", virtualSvcsInString=" + virtualSvcsInString + "]";
	}
}