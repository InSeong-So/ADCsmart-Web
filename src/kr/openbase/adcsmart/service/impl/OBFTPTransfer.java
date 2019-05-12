package kr.openbase.adcsmart.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import kr.openbase.adcsmart.service.utility.OBDefine;
import kr.openbase.adcsmart.service.utility.OBSystemLog;

public class OBFTPTransfer {
//	public static void main(String[] args)
//	{
//		try
//		{
//			ArrayList<String> files = new ArrayList<String>();
//			files.add("/root/test.pg");
//			new OBFTPTransfer().FtpPut("172.172.2.209", 21, "bwpark", "no1openbase", "", "", files);
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	// �뿬�윭媛쒖쓽 �뙆�씪�쓣 �쟾�넚�븳�떎.
	public boolean FtpPut(String ip, int port, String id, String password, String uploaddir, String makedir,
			ArrayList<String> files) {
		boolean result = false;
		FTPClient ftp = null;
		int reply = 0;

		try {
			ftp = new FTPClient();
			ftp.connect(ip, port);

			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to rcv a positive completion response(ip:%s)", ip));
				return result;
			}

			if (!ftp.login(id, password)) {
				ftp.logout();
				OBSystemLog.error(OBDefine.LOGFILE_SYSTEM,
						String.format("failed to send file. invalid id/passwd(%s/%s)", id, password));
				return result;
			}

			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();

			ftp.changeWorkingDirectory(uploaddir);
			ftp.makeDirectory(makedir);
			ftp.changeWorkingDirectory(makedir);

			for (int i = 0; i < files.size(); i++) {
				String sourceFile = (String) files.get(i); // �뵒�젆�넗由�+�뙆�씪紐�

				File uploadFile = new File(sourceFile);
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(uploadFile);
					boolean isSuccess = ftp.storeFile(uploadFile.getName(), fis);
					if (isSuccess) {
						System.out.println(sourceFile + " �뙆�씪 FTP �뾽濡쒕뱶 �꽦怨�");
					}
				} catch (IOException ioe) {
//              System.out.println(StringUtil.stackTraceToString(ioe));
					// ioe.printStackTrace();
				} finally {
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException ioe) {
//                   System.out.println(StringUtil.stackTraceToString(ioe));
							// ioe.printStackTrace();
						}
					}
				}
			}

			ftp.logout();
			result = true;
		} catch (SocketException se) {
//         System.out.println(StringUtil.stackTraceToString(se));
			// se.printStackTrace();
		} catch (IOException ioe) {
//         System.out.println(StringUtil.stackTraceToString(ioe));
			// ioe.printStackTrace();
		} catch (Exception e) {
//         System.out.println(StringUtil.stackTraceToString(e));
			// e.printStackTrace();
		} finally {
			if (ftp != null && ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException e) {
				}
			}
		}

		return result;
	}

	// �뙆�씪�쓣 諛쏅뒗�떎.
	public boolean FtpGet(String ip, int port, String id, String password, String localdir, String serverdir,
			String fileName) {
		boolean result = false;
		FTPClient ftp = null;
		int reply = 0;

		try {
			ftp = new FTPClient();

			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}

			if (!ftp.login(id, password)) {
				ftp.logout();
				return result;
			}

			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.enterLocalPassiveMode();

			ftp.changeWorkingDirectory(serverdir);

			File f = new File(localdir, fileName);
			FileOutputStream fos = null;
			try {
				fos = new FileOutputStream(f);
				boolean isSuccess = ftp.retrieveFile(fileName, fos);
				if (isSuccess) {
					System.out.println("�떎�슫濡쒕뱶 �꽦怨�");
				} else {
					System.out.println("�떎�슫濡쒕뱶 �떎�뙣");
				}
			} catch (IOException ioe) {
//             System.out.println(StringUtil.stackTraceToString(ioe));
				// ioe.printStackTrace();
			} finally {
				if (fos != null)
					try {
						fos.close();
					} catch (IOException ex) {
					}
			}
			ftp.logout();
		} catch (SocketException se) {
//         System.out.println(StringUtil.stackTraceToString(se));
			// se.printStackTrace();
		} catch (IOException ioe) {
//         System.out.println(StringUtil.stackTraceToString(ioe));
			// ioe.printStackTrace();
		} catch (Exception e) {
//         System.out.println(StringUtil.stackTraceToString(e));
			// e.printStackTrace();
		} finally {
			if (ftp != null && ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException e) {
//                 System.out.println(StringUtil.stackTraceToString(e));
				}
			}
		}

		return result;
	}
}
