package kr.openbase.adcsmart.web.report.l4operation;

//import iControl.SystemVersionInformation;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.service.OBReportOperation;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4ConfigChange;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4PerformanceInfo;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4PerformanceSummary;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4PerformanceTrend;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptL4SlbConfigChangeSummary;
import kr.openbase.adcsmart.service.dto.report.OBDtoRptTitle;
import kr.openbase.adcsmart.service.impl.report.OBReportL4OperationImpl;
import kr.openbase.adcsmart.service.impl.report.OBReportOperationImpl;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.web.report.impl.SystemReportDto;

public class L4Factory {
//	public static void main(String args[]) throws Exception
//	{
////		List<L4SlbConfigChangeSummaryDto> slt = getL4SlbConfChange("1363684025010" , 1);
////		System.out.println("##########################TESTESTSETSETSETSTSET"+slt);
//	}
//	

	public static List<SystemReportDto> getSystemReport(String index) throws Exception {
		Class.forName("org.postgresql.Driver");
		OBReportOperation reportSvc = new OBReportOperationImpl();
		OBDtoRptTitle rptTitle = reportSvc.getTitle(index);
		SystemReportDto report = SystemReportDto.toSystemReportDto(rptTitle);
		List<SystemReportDto> reports = new ArrayList<SystemReportDto>();
		reports.add(report);
		return reports;
	}

//	public static List<L4PerformSummaryDto> getPerformSummry(String index) throws Exception
//	{
//		List<SystemReportDto> systemReports = getSystemReport(index);
//		List<L4PerformSummaryDto> l4Perform = getPerformSummry(index, systemReports.g)
//	}

	public static List<OBDtoRptL4PerformanceSummary> getPerformSummary(String rptIndex, Integer accntIndex)
			throws Exception { // 성능정보요약
		OBDtoRptL4PerformanceSummary report = new OBReportL4OperationImpl().getPerformanceInfo(rptIndex, accntIndex);
//      L4PerformSummaryDto su = L4PerformSummaryDto.toL4PerformanceSummary(l4change);

		ArrayList<OBDtoRptL4PerformanceInfo> oldConneList = report.getTop10ConnectionList();
		ArrayList<OBDtoRptL4PerformanceInfo> oldThrouList = report.getTop10ThroughputList();

		OBDtoRptL4PerformanceSummary obj = new OBDtoRptL4PerformanceSummary();

		ArrayList<OBDtoRptL4PerformanceInfo> newConneList = new ArrayList<OBDtoRptL4PerformanceInfo>();
		ArrayList<OBDtoRptL4PerformanceInfo> newThrouList = new ArrayList<OBDtoRptL4PerformanceInfo>();

		for (OBDtoRptL4PerformanceInfo item : oldConneList) {
			OBDtoRptL4PerformanceInfo connItem = new OBDtoRptL4PerformanceInfo();

			connItem.setAdcName(OBDtoRptL4PerformanceInfo.toAdcName(item.getAdcName()));
			connItem.setAdcType(item.getAdcType());
			connItem.setAvgBps(item.getAvgBps());
			connItem.setAvgConnections(item.getAvgConnections());
			connItem.setObjName(item.getObjName());
			connItem.setRank(item.getRank());
			connItem.setVsIPAddress(item.getVsIPAddress());
			connItem.setVsPort(item.getVsPort());
			connItem.setVsName(OBDtoRptL4PerformanceInfo.toVsName(item.getVsName()));

			newConneList.add(connItem);

		}
		for (OBDtoRptL4PerformanceInfo item : oldThrouList) {
			OBDtoRptL4PerformanceInfo throuItem = new OBDtoRptL4PerformanceInfo();

			throuItem.setAdcName(OBDtoRptL4PerformanceInfo.toAdcName(item.getAdcName()));
			throuItem.setAdcType(item.getAdcType());
			throuItem.setAvgBps(item.getAvgBps());
			throuItem.setAvgConnections(item.getAvgConnections());
			throuItem.setObjName(item.getObjName());
			throuItem.setRank(item.getRank());
			throuItem.setVsIPAddress(item.getVsIPAddress());
			throuItem.setVsPort(item.getVsPort());
			throuItem.setVsName(OBDtoRptL4PerformanceInfo.toVsName(item.getVsName()));

			newThrouList.add(throuItem);

		}
		obj.setTop10ConnectionList(newConneList);
		obj.setTop10ThroughputList(newThrouList);

		List<OBDtoRptL4PerformanceSummary> retVal = new ArrayList<OBDtoRptL4PerformanceSummary>();
		retVal.add(obj);
		return retVal;

	}

	public static List<OBDtoRptL4SlbConfigChangeSummary> getL4SlbConfChange(String rptIndex, Integer accntIndex)
			throws Exception { // 설정 변경 정보 요약

//         OBReportL4Operation l4Op = new OBReportL4OperationImpl();
//         OBDtoRptL4SlbConfigChangeSummary report = l4Op.getSlbConfigChangeInfo(rptIndex, accntIndex);
//         L4SlbConfigChangeSummaryDto su = L4SlbConfigChangeSummaryDto.toL4SlbConfigChangeSummaryDto(l4change);

		OBDtoRptL4SlbConfigChangeSummary report = new OBReportL4OperationImpl().getSlbConfigChangeInfo(rptIndex,
				accntIndex);
		ArrayList<OBDtoRptL4ConfigChange> oldChangeList = report.getConfigChangeList();

		OBDtoRptL4SlbConfigChangeSummary obj = new OBDtoRptL4SlbConfigChangeSummary();
		ArrayList<OBDtoRptL4ConfigChange> newChangeList = new ArrayList<OBDtoRptL4ConfigChange>();

		for (OBDtoRptL4ConfigChange item : oldChangeList) {
			OBDtoRptL4ConfigChange newItem = new OBDtoRptL4ConfigChange();

			newItem.setAdcIPAddress(item.getAdcIPAddress());
			newItem.setAdcName(OBDtoRptL4ConfigChange.toAdcName(item.getAdcName()));
			newItem.setContents(item.getContents());
			newItem.setOccurTime(item.getOccurTime());
			newItem.setUserID(OBDtoRptL4ConfigChange.toUserID(item.getUserID()));
			newItem.setVsIPAddress(item.getVsIPAddress());
			newItem.setVsName(OBDtoRptL4ConfigChange.toVsName(item.getVsName()));

			newChangeList.add(newItem);
		}
		obj.setTotalLogCount(report.getTotalLogCount());
		obj.setConfigChangeList(newChangeList);
		List<OBDtoRptL4SlbConfigChangeSummary> retVal = new ArrayList<OBDtoRptL4SlbConfigChangeSummary>();
		retVal.add(obj);
		return retVal;
	}

//	public static List<L4PerformanceTrend> get10ConnectionTrend(String rptIndex, Integer accntIndex, ArrayList<OBDtoRptL4PerformanceInfo> itemList) throws OBException
//	{	// 10connection 추이
//		OBReportL4Operation l4Op = new OBReportL4OperationImpl();
//		ArrayList<OBDtoRptL4PerformanceTrend> svcl4 = l4Op.getTop10ConnectionList(rptIndex, accntIndex, itemList);
//		ArrayList<L4PerformanceTrend> l4trend = L4PerformanceTrend.toL4PerformTrend(svcl4);
//	
//		return l4trend;
//	}

//	public static ArrayList<OBDtoTrendObject> toTrendObjectConnection(OBDtoRptL4PerformanceTrend iTem)
//	{
//		ArrayList<OBDtoTrendObject> retVal = new ArrayList<OBDtoTrendObject>();
//		
//		String name = iTem.getVsIPAddress()+":"+iTem.getVsPort();
//		
//		for(OBDtoUsageThroughput usage:iTem.getUsage())
//		{
//			OBDtoTrendObject obj = new OBDtoTrendObject();
//			obj.setName(name);
//			obj.setxAxis(usage.getOccurTime());
//			obj.setyAxis(usage.getPps());
//			retVal.add(obj);
//		}
//		return retVal;
//	}
//	
	public static List<OBDtoTopNTrend> get10ConnectionTrend(String rptIndex, Integer accntIndex,
			ArrayList<OBDtoRptL4PerformanceInfo> itemList) throws OBException { // 10connection 추이
		ArrayList<OBDtoRptL4PerformanceTrend> svcl4 = new OBReportL4OperationImpl().getTop10ConnectionList(rptIndex,
				accntIndex, itemList);
//		ArrayList<L4PerformanceTrend> l4trend = L4PerformanceTrend.toL4PerformTrend(svcl4);

//		OBDtoTop10ConnectionTrend obj = new OBDtoTop10ConnectionTrend();
		ArrayList<OBDtoTopNTrend> retVal = new ArrayList<OBDtoTopNTrend>();
		for (OBDtoRptL4PerformanceTrend svc : svcl4) {
			retVal.add(OBDtoTopNTrend.toTrendObjectConnection(svc));
		}
		return retVal;
//		
//		retVal.add(obj);
//		
//		ArrayList<Integer> xAxis = new ArrayList<Integer>();
//		xAxis.add(1000);
//		xAxis.add(2000);
//		xAxis.add(3000);
//		xAxis.add(4000);
//		ArrayList<Integer> yAxis = new ArrayList<Integer>();
//		
//		yAxis.add(10000);
//		yAxis.add(20000);
//		yAxis.add(10000);
//		yAxis.add(40000);
//		
//		obj.setxArray(xAxis);
//		obj.setyArray(yAxis);
//		obj.setFirstName("first");
//		
//		// first
//		if(svcl4.size()<1)
//			return retVal;
//		obj.setFirst(OBDtoTop10ConnectionTrend.toTrendObjectConnection(svcl4.get(0)));
//		
//		// second
//		if(svcl4.size()<2)
//			return retVal;
//		obj.setSecond(OBDtoTop10ConnectionTrend.toTrendObjectConnection(svcl4.get(1)));
//
//		// third
//		if(svcl4.size()<3)
//			return retVal;
//		obj.setThird(OBDtoTop10ConnectionTrend.toTrendObjectConnection(svcl4.get(2)));
//
//		// fourth
//		if(svcl4.size()<4)
//			return retVal;
//		obj.setFourth(OBDtoTop10ConnectionTrend.toTrendObjectConnection(svcl4.get(3)));
//		
//		// fifth
//		if(svcl4.size()<5)
//			return retVal;
//		obj.setFifth(OBDtoTop10ConnectionTrend.toTrendObjectConnection(svcl4.get(4)));
//		
//		// sixth
//		if(svcl4.size()<6)
//			return retVal;
//		obj.setSixth(OBDtoTop10ConnectionTrend.toTrendObjectConnection(svcl4.get(5)));
//		
//		// seventh
//		if(svcl4.size()<7)
//			return retVal;
//		obj.setSeventh(OBDtoTop10ConnectionTrend.toTrendObjectConnection(svcl4.get(6)));
//		
//		// eightn
//		if(svcl4.size()<8)
//			return retVal;
//		obj.setEighth(OBDtoTop10ConnectionTrend.toTrendObjectConnection(svcl4.get(7)));
//		
//		// nineth
//		if(svcl4.size()<9)
//			return retVal;
//		obj.setNineth(OBDtoTop10ConnectionTrend.toTrendObjectConnection(svcl4.get(8)));
//		
//		// tenth
//		if(svcl4.size()<10)
//			return retVal;
//		obj.setTenth(OBDtoTop10ConnectionTrend.toTrendObjectConnection(svcl4.get(9)));
//
//		return retVal;
	}

	public static List<OBDtoTopNTrend> get10ThroughputTrend(String rptIndex, Integer accntIndex,
			ArrayList<OBDtoRptL4PerformanceInfo> itemList) throws OBException { // 10 throughput 추이
//		OBReportL4Operation l4Op = new OBReportL4OperationImpl();
//		ArrayList<OBDtoRptL4PerformanceTrend> svcl4 = l4Op.getTop10ThroughputList(rptIndex, accntIndex, itemList);
//		ArrayList<L4PerformanceTrend> l4trend = L4PerformanceTrend.toL4PerformTrend(svcl4);
//		return l4trend;
		ArrayList<OBDtoRptL4PerformanceTrend> svcl4 = new OBReportL4OperationImpl().getTop10ThroughputList(rptIndex,
				accntIndex, itemList);
//		ArrayList<L4PerformanceTrend> l4trend = L4PerformanceTrend.toL4PerformTrend(svcl4);

//		OBDtoTop10ConnectionTrend obj = new OBDtoTop10ConnectionTrend();
		ArrayList<OBDtoTopNTrend> retVal = new ArrayList<OBDtoTopNTrend>();
		for (OBDtoRptL4PerformanceTrend svc : svcl4) {
			retVal.add(OBDtoTopNTrend.toTrendObjectThroughput(svc));
		}
		return retVal;
	}

//    public static List<L4SlbConfigChangeSummaryDto> getConfig()
//    {
//        L4SlbConfigChangeSummaryDto test = new L4SlbConfigChangeSummaryDto();
//        test.setTotalLogCount(10);
//        
//        ArrayList<L4ConfigChangeDto> conList= new ArrayList<L4ConfigChangeDto>();
//        L4ConfigChangeDto con = new L4ConfigChangeDto();
//        con.setAdcIPAddress("172.172.2.41");
//        con.setAdcName("YounjuKim");
//        con.setContents("helpme");
//        con.setVsName("Iwish");
//        con.setVsIPAddress("172.172.2.42");
//        con.setUserID("oyarng");
//        con.setOccurTime("2013-03-15");
//        conList.add(con);
//        System.out.println("1111111111111111111" +conList);
//        
//        
//        con.setAdcIPAddress("172.172.2.51");
//        con.setAdcName("yuna");
//        con.setContents("fighting");
//        con.setVsName("Iwish");
//        con.setVsIPAddress("172.172.2.52");
//        con.setUserID("YunaKim");
//        con.setOccurTime("2013-03-15");
//        conList.add(con);
//        System.out.println("222222222222222222222" +conList);
//        
//        test.setL4ConfigChangeList(conList);
//        List<L4SlbConfigChangeSummaryDto> ret = new ArrayList<L4SlbConfigChangeSummaryDto>();
//        ret.add(test);
//        
//        return ret;
//    }

//    public static List<L4SlbConfigChangeSummaryDto> getPerfom()
//    {
//         L4SlbConfigChangeSummaryDto test = new L4SlbConfigChangeSummaryDto();
//         test.setTotalLogCount(10);
//         
//         ArrayList<L4ConfigChangeDto> conList= new ArrayList<L4ConfigChangeDto>();
//         L4ConfigChangeDto con = new L4ConfigChangeDto();
//         con.setAdcIPAddress("172.172.2.41");
//         con.setAdcName("YounjuKim");
//         con.setContents("helpme");
//         con.setVsName("Iwish");
//         con.setVsIPAddress("172.172.2.42");
//         con.setUserID("oyarng");
//         con.setOccurTime("2013-03-15");
//         conList.add(con);
//         System.out.println("1111111111111111111" +conList);
//         
//         con.setAdcIPAddress("172.172.2.51");
//         con.setAdcName("yuna");
//         con.setContents("fighting");
//         con.setVsName("Iwish");
//         con.setVsIPAddress("172.172.2.52");
//         con.setUserID("YunaKim");
//         con.setOccurTime("2013-03-15");
//         conList.add(con);
//         System.out.println("222222222222222222222" +conList);
//         
//         test.setL4ConfigChangeList(conList);
//         List<L4SlbConfigChangeSummaryDto> ret = new ArrayList<L4SlbConfigChangeSummaryDto>();
//         ret.add(test);
//         
//         
//         
//         return ret;
//         
//    }
}
