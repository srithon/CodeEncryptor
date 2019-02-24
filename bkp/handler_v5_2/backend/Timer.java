package srithon.encryptor.backend;

public class Timer
{
	private long startTime;
	private int delay; //milliseconds

	public Timer(int delay, boolean startWithDelay)
	{
		this.delay = delay;
		
		if (startWithDelay)
			this.startTime = System.currentTimeMillis();
		else
			this.startTime = -(delay + 100);
	}

	/*public void start()
	{
		startTime = System.currentTimeMillis();
	}*/

	public boolean check()
	{
		if (System.currentTimeMillis() - startTime < delay)
		{
			return false;
		}
		else
		{
			startTime = System.currentTimeMillis();
			return true;
		}
	}
}