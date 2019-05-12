package kr.openbase.adcsmart.service.threadpool;

import java.util.LinkedList;

import kr.openbase.adcsmart.service.utility.AleadyClosedException;

public class OBWorkQueue
{
	 // 쓰레드가 수행할 작업을 저장한다.
	private LinkedList<Runnable> workList = new LinkedList<Runnable>();
	   
	 //WorkQueue의 사용이 끝났는지의 여부를 나타낸다.
	private boolean closed = false;
	   
	// 큐에 새로운 작업을 삽입한다.
	public synchronized void enqueue(Runnable work) throws AleadyClosedException 
	{
		if (closed) 
	    {
			throw new AleadyClosedException();
	    }
	    workList.addLast(work);
	    notify();
	}
	   
	// 큐에 저장된 작업을 읽어온다.
	public synchronized Runnable dequeue() throws AleadyClosedException, InterruptedException 
	{
		while( workList.size() <= 0 ) 
	    {
			wait();
	        if ( closed ) 
	        {
	        	throw new AleadyClosedException();
	        }
	    }
	    return workList.removeFirst();
	 }
	   
	 public synchronized int size() 
	 {
		 return workList.size();
	 }
	   
	 public synchronized boolean isEmpty() 
	 {
	      return workList.size() == 0;
	 }
	   
	 public synchronized void close() 
	 {
		 closed = true;
	     notifyAll();
	 }
}
