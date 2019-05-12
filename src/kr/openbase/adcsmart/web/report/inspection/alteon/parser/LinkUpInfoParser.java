package kr.openbase.adcsmart.web.report.inspection.alteon.parser;

import java.util.ArrayList;
import java.util.List;

import kr.openbase.adcsmart.web.report.inspection.parser.AbstractAlteonParser;
import kr.openbase.adcsmart.web.report.inspection.utility.Common;

/**
 * 
 * @author 최영조
 */
public class LinkUpInfoParser extends AbstractAlteonParser {
	private final static String LINKUP_KEYWORD = "up";

	public LinkUpInfoParser(String swVersion) {
		super(swVersion);
	}

	@Override
	public List<String> parse(String input) {
		final List<String> linkUpInfo = new ArrayList<String>();

		String[] linkText = input.split("\\n");
		for (String line : linkText) {
			line = Common.removeMoreText(line);
			String[] link = line.trim().split("\\s+");
			if (link[link.length - 1].toLowerCase().contains(LINKUP_KEYWORD)) {
				linkUpInfo.add(link[0]);
			}
		}

		return linkUpInfo;
	}

//    public static void main(String[] args)
//    {
//        System.out.println(new LinkUpInfoParser("test").parse("------------------------------------------------------------------\nAlias    Port   Speed    Duplex     Flow Ctrl      Link\n------   ----   -----   --------  --TX-----RX--   ------\n1          1    10/100     any     yes    yes      down\n2          2      100     full     yes    yes       up\n3          3    10/100     any     yes    yes    disabled\n4          4      100     full     yes    yes       up\n5          5      100     full     yes    yes       up\n6          6    10/100     any     yes    yes      down\n7          7    10/100     any     yes    yes      down\n8          8    10/100     any     yes    yes      down\n9          9     1000*    full*     no*    no*     up\n10        10     1000     full     yes    yes      down\n* = value set by configuration; not autonegotiated.\n"));
//        System.out.println(new LinkUpInfoParser("test").parse("------------------------------------------------------------------\nPort    Speed    Duplex     Flow Ctrl      Link\n-----   -----   --------  --TX-----RX--   ------\n  1     10/100     any     yes    yes      down\n  2       100     full     yes    yes       up\n\n  3     10/100     any     yes    yes      down\n  4       100     full     yes    yes       up\n\n  5     10/100     any     yes    yes      down\n  6       100     full     yes    yes       up\n\n  7     10/100     any     yes    yes      down\n  8     10/100     any     yes    yes      down\n  9        10*    half*     no*    no*     down\n 10     10/100     any     yes    yes      down\n 11     10/100     any     yes    yes      down\n 12     10/100     any     yes    yes      down\n 13     10/100     any     yes    yes      down\n 14     10/100     any     yes    yes      down\n 15     10/100     any     yes    yes      down\n 16     10/100     any     yes    yes      down\n 17     10/100     any     yes    yes      down\n 18     10/100     any     yes    yes      down\n 19     10/100     any     yes    yes      down\n 20     10/100     any     yes    yes      down\n 21     10/100     any     yes    yes      down\n 22     10/100     any     yes    yes      down\n 23     10/100     any     yes    yes      down\n 24     10/100     any     yes    yes      down\n 25      1000     full     yes    yes      down\n 26      1000     full     yes    yes      down\n 27      1000     full     yes    yes      down\n 28      1000GB     full     yes    yes      up\n* = value set by configuration; not autonegotiated.\n"));
//    }
}
