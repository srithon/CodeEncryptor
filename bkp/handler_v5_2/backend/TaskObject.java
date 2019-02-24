package srithon.encryptor.backend;

public abstract class TaskObject implements Task, Marker
{
	private int times;
	private Timer timer;
	
	public TaskObject(int times, int delay, boolean startWithDelay)
	{
		this.times = times;
		this.timer = new Timer(delay, startWithDelay);
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
	
	public boolean check()
	{
		return this.timer.check();
	}
	
	public static void main(String[] args)
	{
		TaskObject task = new TaskObject(10, 1000, false)
		{
			public void execute()
			{
				System.out.println("Bonjour");
			}
		};
		
		while (task.getTimes() > 0)
		{
			if (task.getTimer().check())
				task.run();
			
			try
			{
				Thread.sleep(15);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
}
