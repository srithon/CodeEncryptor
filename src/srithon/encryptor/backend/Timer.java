package srithon.encryptor.backend;

public class Timer
{
	private long startTime;
	private int delay; //milliseconds
	private boolean startWithDelay;

	public Timer(int delay, boolean startWithDelay)
	{
		this.delay = delay;
		this.startWithDelay = startWithDelay;
	}

	public void start()
	{
		if (startWithDelay)
			this.startTime = System.currentTimeMillis();
		else
			this.startTime = -(delay + 1);
	}

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