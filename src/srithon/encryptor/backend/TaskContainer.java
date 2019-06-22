package srithon.encryptor.backend;

public class TaskContainer implements Marker
{
	private TaskObject[] tasks;
	private Iterator iter;
	
	public TaskContainer(TaskObject... tasks)
	{
		setTasks(tasks);
		iter = new Iterator();
	}
	
	public void setTasks(TaskObject[] tasks)
	{
		this.tasks = tasks;
	}
	
	private class Iterator
	{
		private int currentTask;
		
		public Iterator()
		{
			currentTask = 0;
		}
		
		//	WATCH OUT FOR NULL POINTER EXCEPTION
		public TaskObject next()
		{
			try
			{				
				if (tasks[currentTask].getTimes() >= 1)
				{
					return tasks[currentTask];
				}
				else
				{
					tasks[++currentTask].startTimer();
					return tasks[currentTask];
				}
			}
			catch (ArrayIndexOutOfBoundsException e)
			{
				//System.out.println("Index Out Of Bounds Exception");
			}
			catch (NullPointerException e)
			{
				System.out.println("Null Pointer");
			}
			
			return null;
		}
	}
	
	public TaskObject getNext()
	{
		return iter.next();
	}
}
