package kr.openbase.adcsmart.service.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class OBSocket {
	private Socket socket;
	private BufferedInputStream bis;
	private BufferedOutputStream bos;

	public OBSocket() {
		socket = null;
		bis = null;
		bos = null;
	}

//	 public static void main(String[] args) 
//	 {
//        try 
//        {
//        	OBSocket w = new OBSocket();
//            w.connect("192.168.200.233", 80);
//
//            StringBuffer message = new StringBuffer();
//            message.append("xGET /TEST/ HTTP/1.1\r\n");
////            message.append("Host: adcsmart.test.com\r\n");
//            message.append("\r\n");
//
//        	Date startDate = new Date();
//            w.sendMessage(new String(message));
//            System.out.println(w.receiveMessage());
//        	Date endDate = new Date();
//System.out.println(endDate.getTime()-startDate.getTime());
//            w.disconnect();
//        }
//        catch(Exception e) 
//        {
//            e.printStackTrace();
//        }
//    }

	public void connect(String host, int port) throws UnknownHostException, IOException {
		InetAddress addr = InetAddress.getByName(host);
		SocketAddress sockaddr = new InetSocketAddress(addr, port);

		// Creates an unconnected socket
		this.socket = new Socket();

		int timeout = 2000; // 5000 millis = 5 seconds

		// Connects this socket to the server with a specified timeout value
		// If timeout occurs, SocketTimeoutException is thrown
		this.socket.connect(sockaddr, timeout);

//		 socket = new Socket(InetAddress.getByName(host), port, 100);

		this.bis = new BufferedInputStream(socket.getInputStream());
		this.bos = new BufferedOutputStream(socket.getOutputStream());
	}

	public void connect(String host, int port, int localPort) throws UnknownHostException, IOException {
		this.socket = new Socket(InetAddress.getByName(host), port, InetAddress.getLocalHost(), localPort);
	}

	public void sendMessage(String message) throws IOException {
		this.bos.write(message.getBytes());
		this.bos.flush();
	}

	public String receiveMessage() throws IOException {
		byte[] buf = new byte[4096];
		StringBuffer strbuf = new StringBuffer(4096);

		int read = 0;
		while ((read = this.bis.read(buf)) > 0) {
			strbuf.append(new String(buf, 0, read));
		}

		return new String(strbuf);
	}

	public void disconnect() throws IOException {
		this.bis.close();
		this.bos.close();
		this.socket.close();
	}
}
