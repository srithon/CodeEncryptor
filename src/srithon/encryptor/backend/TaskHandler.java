package srithon.encryptor.backend;

import java.util.ArrayList;

public class TaskHandler
{
	private final ArrayList<Marker> tasks;
	private final Thread taskThread;
	private final Object lock;
	private boolean stop;
	
	{
		tasks = new ArrayList<Marker>();
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

	public void addTask(Marker t)
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
					Marker j = tasks.get(i);
					TaskObject m;
					
					if (j instanceof TaskContainer)
					{
						m = ((TaskContainer) j).getNext();
					}
					else
					{
						m = (TaskObject) j;
					}

					if (m == null)
					{
						tasks.remove(i);
						continue;
					}
					
					if (m.check())
					{
						System.out.println(m.getTimes());
						
						m.run();

						if (m.getTimes() < 1)
						{
							if (j instanceof TaskObject)
							{
								tasks.remove(i);
							}
							
							m = null;

							i--;
						}
					}
				}
			}
						
			try
			{
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
	}
	
	/*
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
		
		handler.addTask(
				new TaskContainer(
					new TaskObject(5, 100, true)
					{
						public void execute()
						{
							System.out.println("CONT_TASK1");
						}
					},
					new TaskObject(10, 100, false)
					{
						public void execute()
						{
							System.out.println("CONT_TASK2");
						}
					},
					new TaskObject(15, 100, true)
					{
						public void execute()
						{
							System.out.println("CONT_TASK3");
						}
					}));
		
		try
		{
			handler.taskThread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	*/
}
