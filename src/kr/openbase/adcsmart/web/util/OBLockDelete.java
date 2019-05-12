package kr.openbase.adcsmart.web.util;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.io.FileUtils;

import kr.openbase.adcsmart.service.utility.OBDefine;

public class OBLockDelete extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8981019390227653501L;

	public void init() throws ServletException {
		try { // lock 파일을 제거한다.

			File desti = new File(OBDefine.DIR_LOCKFILE_SYSLOGD);
			if (!desti.exists()) {
				desti.mkdirs();
			}

			File file = new File(OBDefine.DIR_LOCKFILE_SYSLOGD);

			FileUtils.cleanDirectory(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}