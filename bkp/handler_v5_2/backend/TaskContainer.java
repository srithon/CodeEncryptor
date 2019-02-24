package srithon.encryptor.backend;

public class TaskContainer implements Marker
{
	private TaskObject[] tasks;
	private Iterator iter;
	
	public TaskContainer(TaskObject... tasks)
	{
		this.tasks = tasks;
		iter = new Iterator();
	}
	
	private class Iterator
	{
		private int index;
		
		{
			index = 0;
		}
		
		public TaskObject next()
		{
			if (index < tasks.length)
				return tasks[index++];
			else
			{
				tasks = null;
				return null;
			}
		}
	}
	
	public TaskObject getNext()
	{
		return iter.next();
	}
}
