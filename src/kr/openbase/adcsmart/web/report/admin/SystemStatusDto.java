package kr.openbase.adcsmart.web.report.admin;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.dto.report.OBDtoRptSystemInfo;
import kr.openbase.adcsmart.service.utility.OBMessages;

public class SystemStatusDto
{
	private SystemBasicStatusDto basic;
	private SystemL2StatusDto l2;
	private SystemL3StatusDto l3;
	private SystemL4StatusDto l4;
	private SystemL7StatusDto l7;
	private SystemMiscStatusDto misc;
	private List<SystemStatusRowDto> table  = new ArrayList<SystemStatusRowDto>(25);		// for JasperReports
	
	public static SystemStatusDto toSystemStatusDto(OBDtoRptSystemInfo statusFromSvc) 
	{
		if (statusFromSvc == null)
			return null;
		
		SystemStatusDto status = new SystemStatusDto();
		
		
		status.setBasic(SystemBasicStatusDto.toSystemBasicStatusDto(statusFromSvc.getBasicInfo()));
		status.setL2(SystemL2StatusDto.toSystemL2StatusDto(statusFromSvc.getL2Info()));
		status.setL3(SystemL3StatusDto.toSystemL3StatusDto(statusFromSvc.getL3Info()));
		status.setL4(SystemL4StatusDto.toSystemL4StatusDto(statusFromSvc.getL4Info()));
		status.setL7(SystemL7StatusDto.toSystemL7StatusDto(statusFromSvc.getL7Info()));
		status.setMisc(SystemMiscStatusDto.toSystemMiscStatusDto(statusFromSvc.getEtcInfo()));
		status.makeTable();
		return status;
	}
	
	public void makeTable() 
	{
		makeBasicRows();
		makeL2Rows();
		makeL3Rows();
		makeL4Rows();
		makeL7Rows();
		makeMiscRows();
	}

	private void makeBasicRows() 
	{
		SystemStatusRowDtoTextHdr textHdr = new SystemStatusRowDtoTextHdr();
		textHdr.setColumn1(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN1));//"대항목");
		textHdr.setColumn2(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN2));//"항목명");
		textHdr.setColumn3(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN3));//"결과");
		textHdr.setColumn4(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN4));//"상세정보");
		textHdr.setColumn5(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN5));//"비고");
		
		String category = OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_CATEGORY_BASIC);//"기본항목";
		SystemStatusRowDto status = null;
		if(basic!=null && basic.getUpTime()!=null)
		{
			status = new SystemStatusRowDto(category, basic.getUpTime().getTitle(), basic.getUpTime().getResult(), basic.getUpTime().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(basic!=null && basic.getLastApply()!=null )
		{
			status = new SystemStatusRowDto(category, basic.getLastApply().getTitle(), basic.getLastApply() == null ? "" : basic.getLastApply().getResult(), basic.getLastApply() == null ? "" : basic.getLastApply().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(basic.getCpu() != null )
		{
			status = new SystemStatusRowDto(category, basic.getCpu().getTitle(), basic.getCpu() == null ? "" : basic.getCpu().getResult(), basic.getCpu() == null ? "" : basic.getCpu().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(basic!=null && basic.getMemory()!=null)
		{
			status = new SystemStatusRowDto(category, basic.getMemory().getTitle(), basic.getMemory() == null ? "" : basic.getMemory().getResult(), basic.getMemory() == null ? "" : basic.getMemory().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(basic!=null && basic.getPower()!=null)
		{
			status = new SystemStatusRowDto(category, basic.getPower().getTitle(), basic.getPower() == null ? "" : basic.getPower().getResult(), basic.getPower() == null ? "" : basic.getPower().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(basic!=null && basic.getFan()!=null)
		{	
			status = new SystemStatusRowDto(category, basic.getFan().getTitle(), basic.getFan() == null ? "" : basic.getFan().getResult(), basic.getFan() == null ? "" : basic.getFan().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
	};
	
	private void makeL2Rows()
	{
		SystemStatusRowDtoTextHdr textHdr = new SystemStatusRowDtoTextHdr();
		textHdr.setColumn1(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN1));//"대항목");
		textHdr.setColumn2(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN2));//"항목명");
		textHdr.setColumn3(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN3));//"결과");
		textHdr.setColumn4(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN4));//"상세정보");
		textHdr.setColumn5(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN5));//"비고");
		
		String category = OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_CATEGORY_L2);//"L2 항목";
		SystemStatusRowDto status = null;
		if(l2!=null && l2.getLinkUp()!=null)
		{
			status = new SystemStatusRowDto(category, l2.getLinkUp().getTitle(), l2.getLinkUp() == null ? "" : l2.getLinkUp().getResult(), l2.getLinkUp() == null ? "" :l2.getLinkUp().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(l2!=null && l2.getPort()!=null)
		{
			status = new SystemStatusRowDto(category, l2.getPort().getTitle(), l2.getPort() == null ? "" : l2.getPort().getResult(), l2.getPort() == null ? "" : l2.getPort().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(l2!=null && l2.getVlan()!=null)
		{
			status = new SystemStatusRowDto(category, l2.getVlan().getTitle(), l2.getVlan() == null ? "" : l2.getVlan().getResult(), l2.getVlan() == null ? "" : l2.getVlan().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(l2!=null && l2.getStp()!=null)
		{
			status = new SystemStatusRowDto(category, l2.getStp().getTitle(), l2.getStp() == null ? "" : l2.getStp().getResult(), l2.getStp() == null ? "" : l2.getStp().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(l2!=null && l2.getTrunk()!=null)
		{
			status = new SystemStatusRowDto(category, l2.getTrunk().getTitle(), l2.getTrunk() == null ? "" : l2.getTrunk().getResult(), l2.getTrunk() == null ? "" : l2.getTrunk().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
	};
	
	private void makeL3Rows() 
	{
		SystemStatusRowDtoTextHdr textHdr = new SystemStatusRowDtoTextHdr();
		textHdr.setColumn1(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN1));//"대항목");
		textHdr.setColumn2(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN2));//"항목명");
		textHdr.setColumn3(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN3));//"결과");
		textHdr.setColumn4(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN4));//"상세정보");
		textHdr.setColumn5(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN5));//"비고");
		
		String category = OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_CATEGORY_L3);//"L3 항목";
		SystemStatusRowDto status = null;
		
		if(l3!=null && l3.getInterfaceInfo()!=null)
		{
			status = new SystemStatusRowDto(category, l3.getInterfaceInfo().getTitle(), l3.getInterfaceInfo() == null ? "" : l3.getInterfaceInfo().getResult(), l3.getInterfaceInfo() == null ? "" : l3.getInterfaceInfo().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}

		if(l3!=null && l3.getGateway()!=null)
		{
			status = new SystemStatusRowDto(category, l3.getGateway().getTitle(), l3.getGateway() == null ? "" : l3.getGateway().getResult(), l3.getGateway() == null ? "" : l3.getGateway().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
	};
	
	private void makeL4Rows()
	{
		SystemStatusRowDtoTextHdr textHdr = new SystemStatusRowDtoTextHdr();
		textHdr.setColumn1(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN1));//"대항목");
		textHdr.setColumn2(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN2));//"항목명");
		textHdr.setColumn3(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN3));//"결과");
		textHdr.setColumn4(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN4));//"상세정보");
		textHdr.setColumn5(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN5));//"비고");
		
		String category = OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_CATEGORY_L4);//"L4 항목";
		SystemStatusRowDto status = null;
		
		if(l4!=null && l4.getPoolMember()!=null)
		{
			status = new SystemStatusRowDto(category, l4.getPoolMember().getTitle(), l4.getPoolMember() == null ? "" : l4.getPoolMember().getResult(), l4.getPoolMember() == null ? "" : l4.getPoolMember().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}

		if(l4!=null && l4.getVirtualSvr()!=null)
		{
			status = new SystemStatusRowDto(category, l4.getVirtualSvr().getTitle(), l4.getVirtualSvr() == null ? "" : l4.getVirtualSvr().getResult(), l4.getVirtualSvr() == null ? "" : l4.getVirtualSvr().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(l4!=null && l4!=null && l4.getConnection()!=null)
		{
			status = new SystemStatusRowDto(category, l4.getConnection().getTitle(), l4.getConnection() == null ? "" : l4.getConnection().getResult(), l4.getConnection() == null ? "" : l4.getConnection().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(l4!=null && l4.getDirectFunc()!=null)
		{
			status = new SystemStatusRowDto(category, l4.getDirectFunc().getTitle(), l4.getDirectFunc() == null ? "" : l4.getDirectFunc().getResult(), l4.getDirectFunc() == null ? "" : l4.getDirectFunc().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
	};
	
	private void makeL7Rows() 
	{
		SystemStatusRowDtoTextHdr textHdr = new SystemStatusRowDtoTextHdr();
		textHdr.setColumn1(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN1));//"대항목");
		textHdr.setColumn2(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN2));//"항목명");
		textHdr.setColumn3(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN3));//"결과");
		textHdr.setColumn4(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN4));//"상세정보");
		textHdr.setColumn5(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN5));//"비고");
		
		String category = OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_CATEGORY_L7);//"L7 항목";
		SystemStatusRowDto status = null;
		if(l7!=null && l7.getiRule()!=null)
		{
			status = new SystemStatusRowDto(category, l7.getiRule().getTitle(), (l7 == null || l7.getiRule() == null) ? "" : l7.getiRule().getResult(), (l7 == null || l7.getiRule() == null) ? "" : l7.getiRule().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(l7!=null && l7.getOneConnect()!=null)
		{
			status = new SystemStatusRowDto(category, l7.getOneConnect().getTitle(), (l7 == null || l7.getOneConnect() == null) ? "" : l7.getOneConnect().getResult(), (l7 == null || l7.getOneConnect() == null) ? "" : l7.getOneConnect().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(l7!=null && l7.getRamCache()!=null)
		{
			status = new SystemStatusRowDto(category, l7.getRamCache().getTitle(), (l7 == null || l7.getRamCache() == null) ? "" : l7.getRamCache().getResult(), (l7 == null || l7.getRamCache() == null) ? "" : l7.getRamCache().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(l7!=null && l7.getCompression()!=null)
		{
			status = new SystemStatusRowDto(category, l7.getCompression().getTitle(), (l7 == null || l7.getCompression() == null) ? "" : l7.getCompression().getResult(), (l7 == null || l7.getCompression() == null) ? "" : l7.getCompression().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(l7!=null && l7.getSslAccel()!=null)
		{
			status = new SystemStatusRowDto(category, l7.getSslAccel().getTitle(), (l7 == null || l7.getSslAccel() == null) ? "" : l7.getSslAccel().getResult(), (l7 == null || l7.getSslAccel() == null) ? "" : l7.getSslAccel().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
	}
	
	private void makeMiscRows()
	{
		SystemStatusRowDtoTextHdr textHdr = new SystemStatusRowDtoTextHdr();
		textHdr.setColumn1(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN1));//"대항목");
		textHdr.setColumn2(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN2));//"항목명");
		textHdr.setColumn3(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN3));//"결과");
		textHdr.setColumn4(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN4));//"상세정보");
		textHdr.setColumn5(OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_COLUMN5));//"비고");
		
		String category = OBMessages.getMessage(OBMessages.MSG_RPT_SYSTEM_ADMIN_CATEGORY_MISC);//"기타";
		SystemStatusRowDto status = null;

		if(misc!=null && misc.getSysLog()!=null)
		{
			status = new SystemStatusRowDto(category, misc.getSysLog().getTitle(), misc.getSysLog() == null ? "" : misc.getSysLog().getResult(), misc.getSysLog() == null ? "" : misc.getSysLog().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(misc!=null && misc.getNtp()!=null)
		{
			status = new SystemStatusRowDto(category, misc.getNtp().getTitle(), misc.getNtp() == null ? "" : misc.getNtp().getResult(), misc.getNtp() == null ? "" : misc.getNtp().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
		
		if(misc!=null && misc.getLog()!=null)
		{
			status = new SystemStatusRowDto(category, misc.getLog().getTitle(), misc.getLog() == null ? "" : misc.getLog().getResult(), misc.getLog() == null ? "" : misc.getLog().getDetail());
			category="";
			status.setTextHdr(textHdr);
			table.add(status);
		}
	}
	
	public SystemBasicStatusDto getBasic()
	{
		return basic;
	}
	public void setBasic(SystemBasicStatusDto basic) 
	{
		this.basic = basic;
	}
	public SystemL2StatusDto getL2() 
	{
		return l2;
	}
	public void setL2(SystemL2StatusDto l2) 
	{
		this.l2 = l2;
	}
	public SystemL3StatusDto getL3()
	{
		return l3;
	}
	public void setL3(SystemL3StatusDto l3) 
	{
		this.l3 = l3;
	}
	public SystemL4StatusDto getL4()
	{
		return l4;
	}
	public void setL4(SystemL4StatusDto l4)
	{
		this.l4 = l4;
	}
	public SystemL7StatusDto getL7()
	{
		return l7;
	}
	public void setL7(SystemL7StatusDto l7)
	{
		this.l7 = l7;
	}
	public SystemMiscStatusDto getMisc()
	{
		return misc;
	}
	public void setMisc(SystemMiscStatusDto misc)
	{
		this.misc = misc;
	}

	public List<SystemStatusRowDto> getTable() 
	{
		return table;
	}

	public void setTable(List<SystemStatusRowDto> table)
	{
		this.table = table;
	}

	@Override
	public String toString()
	{
		return "SystemStatusDto [basic=" + basic + ", l2=" + l2 + ", l3=" + l3 + ", l4=" + l4 + ", l7=" + l7
				+ ", misc=" + misc + ", table=" + table + "]";
	}
	
}
