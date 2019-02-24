package srithon.encryptor.backend;

public abstract class TaskObject implements Task, Marker
{
	private int times;
	private Timer timer;
	
	public TaskObject()
	{
		this.times = 1;
		this.timer = new Timer(0, false);
		this.timer.start();
	}
	public TaskObject(int times, int delay, boolean startWithDelay)
	{
		this.times = times;
		this.timer = new Timer(delay, startWithDelay);
		this.timer.start();
	}
	
	public void run()
	{
		if (times > 0)
		{
			execute();
			decrementTimes();
		}
		else
		{
			System.out.println("RUN METHOD WAS CALLED BUT !TIMES > 0");
		}
	}

	public int getTimes()
	{
		return times;
	}
	
	public void decrementTimes()
	{
		times--;
	}
	
	public Timer getTimer()
	{
		return timer;
	}
	
	public void startTimer()
	{
		timer.start();
	}
	
	public boolean check()
	{
		return this.timer.check();
	}
}