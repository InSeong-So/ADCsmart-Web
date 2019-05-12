package kr.openbase.adcsmart.web.report.inspection.alteon.row;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBException;
import kr.openbase.adcsmart.service.utility.OBMessages;
import kr.openbase.adcsmart.service.utility.OBSystemLog;
import kr.openbase.adcsmart.web.report.inspection.alteon.handler.AlteonDataHandler;
import kr.openbase.adcsmart.web.report.inspection.alteon.parser.SyslogUseStateParser;
import kr.openbase.adcsmart.web.report.inspection.dto.OBDtoInspectionReportRow;
import kr.openbase.adcsmart.web.report.inspection.row.AbstractRow;
import kr.openbase.adcsmart.web.report.inspection.row.column.Column;

public class SyslogUseStateRow extends AbstractRow {
	private final AlteonDataHandler dataHandler;
	private final String swVersion;

	public SyslogUseStateRow(final String swVersion, final AlteonDataHandler dataHandler) {
		this.swVersion = swVersion;
		this.dataHandler = dataHandler;
	}

	public OBDtoInspectionReportRow getRow(final Column col) throws OBException {
		final OBDtoInspectionReportRow reportRowDto = new OBDtoInspectionReportRow();

		// null 값을 주면 바로 윗 항목의 제목을 같이 공유한다.
		reportRowDto.setColumn(col.getFirstColumn());
		reportRowDto.setInspectionlist(OBMessages.getMessage(OBMessages.MSG_RPT_BASIC_SYSLOG_USE_STATE));
		reportRowDto.setCheckMethod("/cfg/sys/syslog/cur");

		try {
			final String commandResult = dataHandler.getCfgSyslog();
			final List<String> parsedResult = new SyslogUseStateParser(swVersion).parse(commandResult);

			reportRowDto.setChecklist(formatter(parsedResult));
			reportRowDto.setResult(validator(parsedResult));
		} catch (Exception e) {
			throw new OBException(OBDefine.LOGFILE_SYSTEM, e.getMessage());
		}

		OBSystemLog.call(OBDefine.LOGFILE_DEBUG, reportRowDto.toString());
		return reportRowDto;
	}

	@Override
	protected String formatter(final List<String> parsedString) throws Exception {
		for (String str : parsedString) {
			return str;
		}

		return NOT_PARSED;
	}

	private String validator(final List<String> parsedString) throws Exception {
		for (String str : parsedString) {
			final String regex = ":0\\.0\\.0\\.0";
			final Pattern pattern = Pattern.compile(regex);
			final Matcher match = pattern.matcher(str);

			if (!match.find()) {
				return OBMessages.getMessage(OBMessages.MSG_RPT_USED);
			}
		}

		return OBMessages.getMessage(OBMessages.MSG_RPT_NOT_USED);
	}

//    public static void main(String[] args)
//    {
//        String str = "1:0.0.0.0";
//
//        final String regex = ":0\\.0\\.0\\.0";
//        final Pattern pattern = Pattern.compile(regex);
//        final Matcher match = pattern.matcher(str);
//
//        if(match.find())
//        {
//            System.out.println(match.group());
//            // return Messages.getMessage(Messages.MSG_RPT_NOT_USED);
//        }
//    }

}
