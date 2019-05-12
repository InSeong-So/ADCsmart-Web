package kr.openbase.adcsmart.service.utility;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
//import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/* 
 * syslog 전송
 */

public class OBSyslog extends Object {
	static private OBSyslog logger = null;

	private String logName;
//	private String			hostName;
	private int portNum;
//	private int				flags;
	private boolean includeDate;

	private InetAddress hostAddress;
	private DatagramSocket socket;

	private SimpleDateFormat date1Format;
	private SimpleDateFormat date2Format;

	// Binds the Syslog class to a specified host for logging.
	static public void open(String hostname, String name, int flags) throws Exception {
		if (hostname == null || hostname.isEmpty())
			return;

		try {
			if (OBSyslog.logger == null) {
				OBSyslog.logger = new OBSyslog(hostname, OBSyslogDefine.DEFAULT_PORT, name, flags);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	// 지정된 syslog 서버로 로그를 날린다.
	static public void log(int fac, int level, String msg) throws Exception {
		try {
			logger.syslog(fac, level, msg);
		} catch (Exception e) {
			throw e;
		}
	}

	// syslog 서버 release
	static public void close() {
		logger = null;
	}

	static public boolean isOpend() {
		if (logger == null) {
			return false;
		} else {
			return true;
		}
	}

	// Syslog 전송 객체 생성
	public OBSyslog(String name, int flags) throws Exception {
		super();
		this.logName = name;
//		this.hostName = null;
		this.portNum = OBSyslogDefine.DEFAULT_PORT;
//		this.flags = flags;

		this.initialize();
	}

	/**
	 * Syslog 전송 객체 생성
	 * 
	 * @param hostname : syslog를 보낼 hostname or IP string
	 * @param port     : syslog port
	 * @param name     : log name, 로그의 요약등
	 * @param flags    : 안씀
	 * @throws Exception
	 */
	public OBSyslog(String hostname, int port, String name, int flags) throws Exception {
		super();

		this.logName = name;
//		this.hostName = hostname;
		this.portNum = port;
//		this.flags = flags;

		try {
			this.hostAddress = InetAddress.getByName(hostname);
		} catch (UnknownHostException e) {
			String message = "Error binding address from host named '" + hostname + "': " + e.getMessage();
			throw new Exception(message);
		}

		this.initialize();
	}

	private void initialize() throws Exception {
		try {
			this.socket = new DatagramSocket();
		} catch (SocketException e) {
			String message = "Error creating syslog udp socket: " + e.getMessage();
			throw new Exception(message);
		}

		this.includeDate = true; // TRUE : timestamp를 client에서 지정해 보낸다. standard이다. / FALSE : old style, 서버에서 시간을
									// 결정한다.

		if (this.includeDate) {
			// 날짜 자리수에 따라 포맷이 다르기 때문에 두 포맷을 정의한다.
			// - 한자리 날짜(1~9)면 앞을 space로 채운다.
			// - 두자리 날짜는 그대로 쓴다.
			this.date1Format = new SimpleDateFormat("MMM  d HH:mm:ss ", Locale.US);
			this.date2Format = new SimpleDateFormat("MMM dd HH:mm:ss ", Locale.US);
			this.date1Format.setTimeZone(TimeZone.getDefault());
			this.date2Format.setTimeZone(TimeZone.getDefault());
		}
	}

	// log syslog messages
	// msg : log 내용
	public void syslog(int fac, int pri, String msg) throws Exception {
		this.syslog(this.hostAddress, this.portNum, fac, pri, msg);
	}

	// log syslog messages
	// msg : log 내용
	public void syslog(InetAddress addr, int fac, int pri, String msg) throws Exception {
		this.syslog(addr, this.portNum, fac, pri, msg);
	}

	// log syslog messages
	// msg : log 내용
	public void syslog(String hostname, int fac, int pri, String msg) throws Exception {
		try {
			InetAddress address = InetAddress.getByName(hostname);
			this.syslog(address, this.portNum, fac, pri, msg);
		} catch (UnknownHostException e) {
			String message = "error locating host named '" + hostname + "': " + e.getMessage();
			throw new Exception(message);
		}
	}

	// log syslog messages
	// msg : log 내용
	public void syslog(String hostname, int port, int fac, int pri, String msg) throws Exception {
		try {
			InetAddress address = InetAddress.getByName(hostname);
			this.syslog(address, port, fac, pri, msg);
		} catch (UnknownHostException e) {
			String message = "error locating host named '" + hostname + "': " + e.getMessage();
			throw new Exception(message);
		}
	}

	// 실제 syslog 전송
	public void syslog(InetAddress addr, int port, int fac, int pri, String msg) throws Exception {
		int pricode;
		int length;
		int idx;
		// StringBuffer buffer;
		byte[] data;
		byte[] sBytes;
		String nmObj;
		String strObj;

		pricode = computeCode(fac, pri);
		Integer priObj = new Integer(pricode);

		if (this.logName != null) {
			nmObj = new String(this.logName);
		} else {
			nmObj = new String(Thread.currentThread().getName());
		}

		length = 4 + nmObj.length() + msg.length() + 1;
		length += (pricode > 99) ? 3 : ((pricode > 9) ? 2 : 1);

		String dStr = null;
		if (this.includeDate) {
			// 날짜의 자리수에 따라 맞는 포맷을 지정해 준다. 10보다 작으면 한자리는 space가 들어가야 한다.
			Calendar now = Calendar.getInstance();
			if (now.get(Calendar.DAY_OF_MONTH) < 10)
				dStr = this.date1Format.format(now.getTime());
			else
				dStr = this.date2Format.format(now.getTime());

			length += dStr.length();
		}

		data = new byte[length];

		idx = 0;
		data[idx++] = '<';

		// strObj = priObj.toString(priObj.intValue()); //?
		strObj = priObj.toString();
		sBytes = strObj.getBytes();
		System.arraycopy(sBytes, 0, data, idx, sBytes.length);
		idx += sBytes.length;

		data[idx++] = '>';

		if (this.includeDate) {
			sBytes = dStr.getBytes();
			System.arraycopy(sBytes, 0, data, idx, sBytes.length);
			idx += sBytes.length;
		}

		sBytes = nmObj.getBytes();
		System.arraycopy(sBytes, 0, data, idx, sBytes.length);
		idx += sBytes.length;

		data[idx++] = ':';
		data[idx++] = ' ';

		sBytes = msg.getBytes();
		System.arraycopy(sBytes, 0, data, idx, sBytes.length);
		idx += sBytes.length;

		data[idx] = 0x0a;

		DatagramPacket packet = new DatagramPacket(data, length, addr, port);

		try {
			socket.send(packet);
		} catch (IOException e) {
			String message = "error sending message: '" + e.getMessage() + "'";
			System.err.println(message);
			throw new Exception(message);
		}

//		if((this.flags & SyslogDefs.LOG_PERROR) != 0)
//		{
//			if(this.logName != null)
//			{
//				System.err.print(this.logName + ": ");
//			}
//			else
//			{
//				System.err.print(Thread.currentThread().getName() + ": ");
//			}
//
//			System.err.println(msg);
//		}
	}

	private int computeCode(int facility, int priority) {
		return ((facility << 3) | priority);
	}

//	public static void main(String [] argv)
//	{
//		try
//		{
//			OBSyslog.open("172.172.2.222", "logName", 0);
//			while(true)
//			{
//				long start = System.currentTimeMillis();
//				for(int i = 0; i < 3300; i++)
//				{
//					OBSyslog.log(FACILITY.USER.getCode(), LEVEL.ERR.getCode(), "content.........................end.");
//				}
//				long end = System.currentTimeMillis();
//				System.out.println(end - start);
//				OBDateTime.Sleep((int)(1000L - (end - start)));
//			}
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
}