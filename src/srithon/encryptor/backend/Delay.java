package srithon.encryptor.backend;

public class Delay extends TaskObject
{
	public Delay(int delay)
	{
		super(1, delay, true, () -> {});
	}
}
