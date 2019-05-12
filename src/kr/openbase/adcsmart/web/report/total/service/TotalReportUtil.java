package kr.openbase.adcsmart.web.report.total.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import kr.openbase.adcsmart.web.report.total.dto.TotalReportAbNormalDto;
import kr.openbase.adcsmart.web.report.total.dto.TotalReportNormalDto;
import kr.openbase.adcsmart.web.report.total.type.TotalReportAbNormalEnum;
import kr.openbase.adcsmart.web.report.total.type.TotalReportNormalEnum;

public class TotalReportUtil {

	public static final String FONT_PATH = "/opt/apache-tomcat/webapps/adcms/WEB-INF/classes/NanumGothic.ttf";
	public static final String FONT_NAME = "NanumGothic";

	public static boolean htmlToPdf(String htmlStr, String distPath) throws Exception {
		Document document = null;
		OutputStream file = null;
		PdfWriter writer = null;

		try {
			file = new FileOutputStream(new File(distPath));
			document = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
			writer = PdfWriter.getInstance(document, file);
			writer.setInitialLeading(12.5f);

			document.open();

			CSSResolver cssResolver = new StyleAttrCSSResolver();
			XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
			fontProvider.register(FONT_PATH, FONT_NAME);

			CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
			HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
			htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

			PdfWriterPipeline pdf = new PdfWriterPipeline(document, writer);
			HtmlPipeline html = new HtmlPipeline(htmlContext, pdf);
			CssResolverPipeline css = new CssResolverPipeline(cssResolver, html);

			XMLWorker worker = new XMLWorker(css, true);
			XMLParser xmlParser = new XMLParser(worker, Charset.forName("UTF-8"));

			StringBuilder htmlString = new StringBuilder();
			htmlString.append(htmlStr);

			StringReader strReader = new StringReader(htmlStr);
			xmlParser.parse(strReader);

			return true;

		} catch (Exception e) {
			throw e;
		} finally {
			document.close();
			writer.close();
			file.close();
		}
	}

	public static String normalDtoToHtml(List<TotalReportNormalDto> normalDtoList) {
		if (normalDtoList == null || normalDtoList.size() == 0)
			return "";
		String style = " style='border:1px solid black;'";

		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<div style='font-family: NanumGothic;'>전체 운영 보고서</div>");
		sb.append("<table style='border-collapse:collapse;font-family: NanumGothic;font-size:8px;'>");
		sb.append("<thead>");
		sb.append("<tr>");

		Arrays.asList(TotalReportNormalEnum.values()).stream().forEach(
				e -> sb.append("<th").append(style).append(">&nbsp;").append(e.getTitle()).append("&nbsp;</th>"));

		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");

		int index = 0;
		for (TotalReportNormalDto normal : normalDtoList) {
			if (normal.getHostName() == null || "".equals(normal.getHostName()))
				continue;

			sb.append("<tr>");
			sb.append("<td").append(style).append(">&nbsp;").append(++index).append("&nbsp;</td>");
			sb.append("<td").append(style).append(">&nbsp;").append(nullFilter(normal.getHostName()))
					.append("&nbsp;</td>");
			sb.append("<td").append(style).append(">&nbsp;").append(nullFilter(normal.getSerial()))
					.append("&nbsp;</td>");
			sb.append("<td").append(style).append(">&nbsp;").append(nullFilter(normal.getModelName()))
					.append("&nbsp;</td>");
			sb.append("<td").append(style).append(">&nbsp;").append(nullFilter(normal.getOs())).append("&nbsp;</td>");
			sb.append("<td").append(style).append(">&nbsp;").append(nullFilter(normal.getPowerStatus()))
					.append("&nbsp;</td>");
			sb.append("<td").append(style).append(">&nbsp;").append(nullFilter(normal.getCpu())).append("&nbsp;</td>");
			sb.append("<td").append(style).append(">&nbsp;").append(nullFilter(normal.getPortStatus()))
					.append("&nbsp;</td>");
			sb.append("<td").append(style).append(">&nbsp;").append(nullFilter(normal.getFailover()))
					.append("&nbsp;</td>");
			sb.append("<td").append(style).append(">&nbsp;").append(nullFilter(normal.getDirect()))
					.append("&nbsp;</td>");
			sb.append("<td").append(style).append(">&nbsp;").append(nullFilter(normal.getRealServerStatus()))
					.append("&nbsp;</td>");
			sb.append("<td").append(style).append(">&nbsp;").append(nullFilter(normal.getVirtualServerStatus()))
					.append("&nbsp;</td>");
			sb.append("<td").append(style).append(">&nbsp;").append(nullFilter(normal.getSlbSession()))
					.append("&nbsp;</td>");
			sb.append("<td").append(style).append(">&nbsp;").append(nullFilter(normal.getResult()))
					.append("&nbsp;</td>");
			sb.append("</tr>");
		}

		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</html>");
		return sb.toString();
	}

	public static String abNormalDtoToHtml(List<TotalReportAbNormalDto> abNormalList) {
		if (abNormalList == null || abNormalList.size() == 0)
			return "";
		String style = " style='border:1px solid black;'";

		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<table style='border-collapse:collapse;font-size:8px;'>");
		sb.append("<thead>");
		sb.append("<tr>비정상 상세내역</tr>");
		sb.append("<tr>");

		Arrays.asList(TotalReportAbNormalEnum.values()).stream()
				.forEach(e -> sb.append("<th").append(style).append(">").append(e.getTitle()).append("</th>"));

		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");

		int index = 0;
		for (TotalReportAbNormalDto abNormal : abNormalList) {
			sb.append("<tr>");
			sb.append("<td").append(style).append(">").append(++index).append("</td>");
			sb.append("<td").append(style).append(">").append(abNormal.getHostName()).append("</td>");
			sb.append("<td").append(style).append(">").append(abNormal.getPowerStatus()).append("</td>");
			sb.append("<td").append(style).append(">").append(abNormal.getCpu()).append("</td>");
			sb.append("<td").append(style).append(">").append(abNormal.getPortStatus()).append("</td>");
			sb.append("</tr>");
		}

		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("</html>");
		return sb.toString();

	}

	public static String nullFilter(String target) {
		if (target == null || "".equals(target)) {
			return "-";
		}
		return target;
	}

//	public static void main(String[] ar)
//	{
////		List<TotalReportNormalDto> nomalDtoList = new ArrayList<>();
////		
////		TotalReportNormalDto e = new TotalReportNormalDto();
////		e.setHostName("JUI_ITSO_R6420_N1");
////		e.setModelName("6420");
////		e.setOs("30.07");
////		e.setPortStatus("비정상");
////		e.setCpu("비정상(cpu 80%이상)");
////		e.setPortStatus("정상(전일 평균치와 비교)");
////		e.setVrrpStatus("Master");
////		e.setDirect("사용/미사용");
////		e.setRealServerStatus("UP : 12개 Failed : 10개");
////		e.setVirtualServerStatus("UP : 12개 Failed : 10개");
////		e.setSlbSession("Curr:31K Max: 46M");
////		e.setResult("비정상");
////		
////		nomalDtoList.add(e);
////		TotalReportNormalDto e2 = new TotalReportNormalDto();
////		e2.setHostName("test2");
////		nomalDtoList.add(e2);
////		TotalReportNormalDto e3 = new TotalReportNormalDto();
////		e3.setHostName("test3");
////		nomalDtoList.add(e3);
////		TotalReportNormalDto e4 = new TotalReportNormalDto();
////		e4.setHostName("test4");
////		nomalDtoList.add(e4);
////		
////		String result = normalDtoToHtml(nomalDtoList);
////		
////		System.out.println(result);
////		
////		
////		
////		List<TotalReportAbNormalDto> abNormalList = new ArrayList<>();
////		
////		TotalReportAbNormalDto a = new TotalReportAbNormalDto();
////		a.setHostName("JUI_ITSO_R6420_N1");
////		a.setCpu("sp3 cpu 100%");
////		a.setPortStatus("port 2");
////		a.setPowerStatus("power down");
////		
////		abNormalList.add(a);
////		String result2 = abNormalDtoToHtml(abNormalList);
////		
////		System.out.println(result2);
////		
//	}

}
