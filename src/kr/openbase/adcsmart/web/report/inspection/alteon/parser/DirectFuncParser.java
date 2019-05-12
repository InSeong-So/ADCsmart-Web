package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;

public class DirectFuncParser extends AbstractAlteonParser {
	private final static String SUFFIX_DIRECT_ENABLED = "direct enabled";
	private final static int MAX_BOUNDS = 1;

	public DirectFuncParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> directFunc = new ArrayList<String>();

		Matcher match = getMatcher("(" + SUFFIX_DIRECT_ENABLED + ")", input);
		if (match.find()) {
			if (match.groupCount() == MAX_BOUNDS) {
				directFunc.add(match.group(1));
			}
		}

		return directFunc;
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new DirectFuncParser("test").parse("Current Layer 4 advanced settings:\n  direct enabled, matrix enabled, vma sport disabled, grace disabled clrbkp disabled  vma dip disabled\n  submac disabled, vstat enabled, rtsvlan disabled\n  rtsiplkup disabled, pvlantag disabled\n  fastage 1 sec, slowage 2 mins, tpcp disabled\n  imask 255.255.255.255  nmask 0.0.0.0, pmask 255.255.255.255\n  mnet 0.0.0.0, mmask 255.255.255.255, portbind disabled, rstchk enabled, srvckdata disabled, clsrst enabled  vmacbkp enabled  fmrport disabled\n  valcksum disabled  sessrev disabled\n  riphash disabled\n\n  subdmac disabled\n\n  sessvpt disabled\n\nSLB session attack detection check interval: 0 seconds\nSLB attack detection sessions allowed: 1048550 new sessions\n\nSYN attack detection check interval: 2 seconds\nSYN attack detection alarm threshold: 10000 new half-open sessions/second\nSMT real ports: 554 80 8080 389 2000 2020 \n"));
//        System.out.println(new DirectFuncParser("test").parse("Current Layer 4 advanced settings:\r\n" + 
//        		"  direct enabled, matrix enabled, vma sport disabled, grace disabled clrbkp disabled  vma dip disabled\r\n" + 
//        		"  submac disabled, vstat enabled, rtsvlan disabled\r\n" + 
//        		"  rtsiplkup disabled, pvlantag disabled\r\n" + 
//        		"  fastage 1 sec, slowage 2 mins, tpcp disabled\r\n" + 
//        		"  imask 255.255.255.255  nmask 0.0.0.0, pmask 255.255.255.255\r\n" + 
//        		"  mnet 0.0.0.0, mmask 255.255.255.255, portbind disabled, rstchk enabled, srvckdata disabled, clsrst enabled  vmacbkp enabled  fmrport disabled\r\n" + 
//        		"  valcksum disabled  sessrev disabled\r\n" + 
//        		"  riphash disabled\r\n" + 
//        		"\r\n" + 
//        		"  subdmac disabled\r\n" + 
//        		"\r\n" + 
//        		"  sessvpt disabled\r\n" + 
//        		"\r\n" + 
//        		"SLB session attack detection check interval: 0 seconds\r\n" + 
//        		"SLB attack detection sessions allowed: 1048550 new sessions\r\n" + 
//        		"\r\n" + 
//        		"SYN attack detection check interval: 2 seconds\r\n" + 
//        		"SYN attack detection alarm threshold: 10000 new half-open sessions/second\r\n" + 
//        		"SMT real ports: 554 80 8080 389 2000 2020 \r\n" + 
//        		"\r\n" + 
//        		"\r\n" + 
//        		">> scard_alt_slb1 - Layer 4 Advanced# "));
//    }
}
