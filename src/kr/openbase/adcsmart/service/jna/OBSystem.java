package kr.openbase.adcsmart.service.jna;

import java.nio.ByteBuffer;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface OBSystem extends Library {
	OBSystem INSTANCE = (OBSystem) Native.loadLibrary(("libOBCommon.so"), OBSystem.class);

//	public void Sleep(Integer msec);
	void OBGetVersion(ByteBuffer szOSVer, ByteBuffer szWEBOver, ByteBuffer szSRVVer);

	void OBGetSystemNetworkInfo(ByteBuffer szIPAddr, ByteBuffer szMask, ByteBuffer szGateway);
//	
//	public void printTest();
//	
//	public int canplus_Write(CANMsg msg );
//	
//	public class CANMsg extends Structure
//	{
//	    public int id = 0;
//	    public int timestamp = 0;
//	    public byte flags = 0;
//	    public byte len = 8;
//	    public char[] data = new char[16];
//	}
}
