package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

/**
 * CPU MP 사용량의 문자열을 파싱한다.
 * 
 * @author 최영조
 */
public class CPUMPUsageParser extends AbstractAlteonParser {
	private final static String SUFFIX_STATS_MP_CPU = "cpuUtil64Seconds:";

	public CPUMPUsageParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> mpUsage = new ArrayList<String>();

		try {
			Matcher match = getMatcher(SUFFIX_STATS_MP_CPU + "\\s*(\\d+)%\\s*", input);
			if (match.find()) {
				mpUsage.add(match.group(1));
			}
		} catch (Exception e) {
			OBSystemLog.error(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		}

		return mpUsage;
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new CPUMPUsageParser("test").parse("------------------------------------------------------------------\nCPU utilization:\ncpuUtil1Second:            41%\ncpuUtil4Seconds:           16%\ncpuUtil64Seconds:          15%\n"));
//    }
}
