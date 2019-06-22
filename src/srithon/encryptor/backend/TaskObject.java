package srithon.encryptor.backend;

public class TaskObject implements Task, Marker
{
	private int times;
	private Timer timer;
	private Action action;
	
	public TaskObject(Action action)
	{
		this.action = action;
		this.times = 1;
	}
	public TaskObject(int times, int delay, boolean startWithDelay, Action action)
	{
		this.action = action;
		this.times = times;
		this.timer = new Timer(delay, startWithDelay);
		this.timer.start();
	}
	
	public void run()
	{
		if (times > 0)
		{
			action.act();
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
		if (timer == null)
			return true;
		return this.timer.check();
	}
}