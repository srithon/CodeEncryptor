package srithon.encryptor.backend;

import java.util.ArrayList;

public class TaskHandler
{
	private final ArrayList<TaskObject> tasks;
	private final Thread taskThread;
	private final Object lock;
	private boolean stop;
	
	{
		tasks = new ArrayList<TaskObject>();
		taskThread = new Thread(this::start);
		
		stop = false;
		lock = new Object();
	}
	
	public TaskHandler()
	{
		/*
		 * TODO Add thread count functionality
		 * Add universal timer
		 * Have array of delays
		 * Sort the array
		 * After each delay is finished, call method on object
		 */
		
		taskThread.start();
	}

	public void addTask(TaskObject t)
	{
		synchronized (lock)
		{
			tasks.add(t);
		}
	}
	
	public void stop()
	{
		stop = true;
	}
	
	public void start()
	{
		while (!stop)
		{
			synchronized (lock)
			{
				for (int i = 0; i < tasks.size(); i++)
				{
					TaskObject j = tasks.get(i);
					
					if (j.check())
					{
						j.run();
						
						if (j.getTimes() < 1)
						{
							tasks.remove(i);
							j = null;
							
							if (i == 0)
							{
								break;
							}
							else
							{
								i--;
							}
						}
					}
				}
			}
			
			try
			{
				Thread.sleep(15);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
	
	public static void main(String[] args)
	{
		TaskHandler handler = new TaskHandler();
		
		handler.addTask(new TaskObject(10, 1000, true)
		{
			public void execute()
			{
				System.out.println("EX1 - " + this.getTimes());
			}
		});
		
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		handler.addTask(new TaskObject(5, 250, false)
		{
			public void execute()
			{
				System.out.println("EX2 - " + this.getTimes());
			}
		});
		
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		handler.addTask(new TaskObject(10, 500, true)
		{
			public void execute()
			{
				System.out.println("EX3 - " + this.getTimes());
			}
		});
		
		try
		{
			handler.taskThread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
