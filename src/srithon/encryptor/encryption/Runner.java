package srithon.encryptor.encryption;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import srithon.encryptor.backend.Marker;
import srithon.encryptor.backend.TaskHandler;
import srithon.encryptor.scene.MainScene;

public class Runner extends Application
{
	private static Stage stage;
	
	private static TaskHandler handler;
	
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			handler = new TaskHandler();
			
			launch(args);
		}
		else
		{
			Handler.main(args);
		}
	}
	
	public void start(Stage primaryStage)
	{
		Runner.setStage(primaryStage);
		
		Scene mainScene = new MainScene();
		
		primaryStage.setScene(mainScene);
		
		primaryStage.setTitle("Handler GUI");
		
		primaryStage.setOnCloseRequest((WindowEvent e) ->
		{
			handler.stop();
			primaryStage.close();
		});
		
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
	
	public static void addTask(Marker j)
	{
		handler.addTask(j);
	}
}
