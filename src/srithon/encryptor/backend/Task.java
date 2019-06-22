package srithon.encryptor.backend;

public interface Task
{
	public int getTimes();
	public void run();
	public void decrementTimes();
}
