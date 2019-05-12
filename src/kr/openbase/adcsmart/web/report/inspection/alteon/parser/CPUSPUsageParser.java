package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

/**
 * CPU SP (swtich processor) 사용량의 문자열을 파싱한다.
 * 
 * 
 * @author 최영조
 */
public class CPUSPUsageParser extends AbstractAlteonParser {
	private final static String SUFFIX_STATS_SP_CPU = "cpuUtil64Seconds:";
	private final static int MAX_BOUNDS = 1;

	public CPUSPUsageParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> spUsage = new ArrayList<String>();

		Matcher match = getMatcher(SUFFIX_STATS_SP_CPU + "\\s*(\\d+)%", input);
		while (match.find()) {
			if (match.groupCount() == MAX_BOUNDS) {
				spUsage.add(match.group(1));
			}
		}

		return spUsage;
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new CPUSPUsageParser("test").parse("CPU utilization for SP 1:\ncpuUtil1Second:             2%\ncpuUtil4Seconds:            2%\ncpuUtil64Seconds:           2%\n\nCPU utilization for SP 2:\ncpuUtil1Second:             4%\ncpuUtil4Seconds:            4%\ncpuUtil64Seconds:           4%\n\nCPU utilization for SP 1:\ncpuUtil1Second:             8%\ncpuUtil4Seconds:            8%\ncpuUtil64Seconds:           8%\n"));
//        System.out.println(new CPUSPUsageParser("test").parse("/stats/sp 1/cpu" +
//        "        ------------------------------------------------------------------" +
//        "        CPU utilization for SP 1:" +
//        "        cpuUtil1Second:             5%" +
//        "        cpuUtil4Seconds:            5%" +
//        "        cpuUtil64Seconds:           4%" +
//        "" +
//        "        >> ADC_2424_22.0.4 - SP-specific Statistics: SP 1# /stats/sp 2/cpu" +
//        "        ------------------------------------------------------------------" +
//        "        CPU utilization for SP 2:" +
//        "        cpuUtil1Second:             5%" +
//        "        cpuUtil4Seconds:            5%" +
//        "        cpuUtil64Seconds:           5%" +
//        "" +
//        "        >> ADC_2424_22.0.4 - SP-specific Statistics: SP 2# /stats/sp 3/cpu" +
//        "        ------------------------------------------------------------------" +
//        "        CPU utilization for SP 3:" +
//        "        cpuUtil1Second:             2%" +
//        "        cpuUtil4Seconds:            2%" +
//        "        cpuUtil64Seconds:           2%" +
//        "" +
//        "        >> ADC_2424_22.0.4 - SP-specific Statistics: SP 3# /stats/sp 4/cpu" +
//        "        ------------------------------------------------------------------" +
//        "        CPU utilization for SP 4:" +
//        "        cpuUtil1Second:             2%" +
//        "        cpuUtil4Seconds:            2%" +
//        "        cpuUtil64Seconds:           2%" +
//        "" +
//        "        >> ADC_2424_22.0.4 - SP-specific Statistics: SP 4# /stats/sp 5/cpu" +
//        "        Error: bad sp number 5; must be between 1 and 4" +
//        "" +
//        "        ------------------------------------------------------------" +
//        "        [Statistics Menu]" +
//        "             port     - Port Stats Menu" +
//        "             l2       - Layer 2 Stats Menu" +
//        "             l3       - Layer 3 Stats Menu"));
//    }
}
