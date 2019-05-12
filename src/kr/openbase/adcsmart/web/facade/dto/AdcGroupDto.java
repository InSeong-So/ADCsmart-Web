package kr.openbase.adcsmart.web.facade.dto;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.OBDtoAdcGroup;

public class AdcGroupDto implements Comparable<AdcGroupDto> {
	private Integer index;
	private String name;
	private String description;
	private Integer unavailableAdcCount;
	private Integer adcsize;
	private List<AdcDto> adcs;
	
	public static AdcGroupDto toAdcGroupDto(OBDtoAdcGroup adcGroupFromSvc) {
		AdcGroupDto adcGroup = new AdcGroupDto();
		adcGroup.setIndex(adcGroupFromSvc.getIndex());;
		adcGroup.setName(adcGroupFromSvc.getName());
		adcGroup.setDescription(adcGroupFromSvc.getDescription());
		adcGroup.setUnavailableAdcCount(adcGroupFromSvc.getAdcUnavailCount());
		adcGroup.setAdcs(AdcDto.toAdcDto(adcGroupFromSvc.getAdcList()));
		adcGroup.setAdcsize(adcGroupFromSvc.getAdcList().size());
		return adcGroup;
	}
	
	public static List<AdcGroupDto> toAdcGroupDto(List<OBDtoAdcGroup> adcGroupsFromSvc) {
		List<AdcGroupDto> adcGroups = new ArrayList<AdcGroupDto>();
		for (OBDtoAdcGroup adcGroupFromSvc : adcGroupsFromSvc)
			adcGroups.add(toAdcGroupDto(adcGroupFromSvc));

		return adcGroups;
	}

	@Override
	
	
	public int compareTo(AdcGroupDto o)
	{
		return name.compareTo(o.name);
	}

	public Integer getIndex()
	{
		return index;
	}

	public void setIndex(Integer index)
	{
		this.index = index;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public Integer getAdcsize()
	{
		return adcsize;
	}

	public void setAdcsize(Integer adcsize)
	{
		this.adcsize = adcsize;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Integer getUnavailableAdcCount()
	{
		return unavailableAdcCount;
	}

	public void setUnavailableAdcCount(Integer unavailableAdcCount)
	{
		this.unavailableAdcCount = unavailableAdcCount;
	}

	public List<AdcDto> getAdcs()
	{
		return adcs;
	}

	public void setAdcs(List<AdcDto> adcs)
	{
		this.adcs = adcs;
	}

	@Override
	public String toString() {
		return "AdcGroupDto [index=" + index + ", name=" + name + ", description=" + description
				+ ", unavailableAdcCount=" + unavailableAdcCount + ", adcs=" + adcs + "]";
	}	
}