package srithon.encryptor.backend;

public interface Task
{
	public void execute();
	public int getTimes();
	public void run();
	public void decrementTimes();
}
