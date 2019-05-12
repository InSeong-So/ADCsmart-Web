package kr.openbase.adcsmart.service.threadpool;

public class TestPool 
{
//	static int count = 0;
//	static long sleepTime = 0;
//	static OBThreadPool pool = null;
//	   
//	
//	public static void main(String[] args) 
//	{
//		pool = new OBThreadPool(10,  // 초기 생성
//	                    20,  // max
//	                    0,  // min
//	                    			10 ); // 허용되는 idle 개수
//	    sleepTime = 100;
//	      
//	    try 
//	    {
//	    	int i=0;
//	    	Timestamp occurTime = OBDateTime.toTimestamp(OBDateTime.now());
//	    	while(true) 
//	    	{
//	    		i++;
//				OBAdcRespTimeWorker workerObj =new OBAdcRespTimeWorker(occurTime, 1, 2, "", "192.168.100.232", 80);
//
//	    		pool.execute(workerObj);
////	            System.out.println(new Date());
//	            OBDateTime.Sleep(100);
//	            if(i > 1000)
//	            	break;
//	            if(i%10==1)
//		        	 System.out.println(pool.getCurrentStatus());
//	            
//	         }
//	         
//	         pool.close();
//	    } 
//	    catch(AleadyClosedException ex) 
//	    {
//	    	ex.printStackTrace();
//	    }
//	}
}
