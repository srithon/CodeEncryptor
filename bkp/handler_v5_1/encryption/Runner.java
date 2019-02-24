package srithon.encryptor.encryption;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import srithon.encryptor.scene.MainScene;

public class Runner extends Application
{
	private static Stage stage;
	
	private static Thread secondaryThread;
	
	public static void main(String[] args)
	{
		if (args.length == 0)
			launch(args);
		else
			Handler.main(args);
	}
	
	public void start(Stage primaryStage)
	{
		Runner.setStage(primaryStage);
		
		Scene mainScene = new MainScene();
		
		primaryStage.setScene(mainScene);
		
		primaryStage.setTitle("Handler GUI");
		
		primaryStage.show();
	}
	
	public static void setStage(Stage stage)
	{
		Runner.stage = stage;
	}
	
	public static Stage getStage()
	{
		return stage;
	}
	
	public static synchronized void setThreadRunnable(Runnable j)
	{
		secondaryThread = new Thread(j);
	}
	
	public static Thread getThread()
	{
		return secondaryThread;
	}
}
