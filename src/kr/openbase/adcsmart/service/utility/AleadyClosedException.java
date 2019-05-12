package kr.openbase.adcsmart.service.utility;

/**
 * WorkQueue 클래스의 enqueue(Runnable work) 메소드와 dequeue() 메소드를 호출할 때,
 * 이미 WorkQueue가 닫힌 상태일 경우 발생한다.
 *
 * 또한, ThreadPool 클래스의 
 *
 */
public class AleadyClosedException extends Exception 
{
	private static final long serialVersionUID = 1L;

	public AleadyClosedException(String msg) 
   {
      super(msg);
   }
   
   public AleadyClosedException() 
   {
      super();
   }
}
