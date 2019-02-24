package srithon.encryptor.encryption;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
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
		{
			Handler.main(args);
		}
	}
	
	public void start(Stage primaryStage)
	{
		Runner.stage = primaryStage;
		
		VBox mainSceneBox = new VBox();
		
		Scene mainScene = new MainScene(mainSceneBox);
		
		primaryStage.setScene(mainScene);
		
		primaryStage.setTitle("Handler GUI");
		
		primaryStage.show();
		 //primaryStage.show();
		 
		 //Handler.handleRaw(sourceFile, outputFile, false);
		 
		 /*BufferedReader reader = null;
		 
		 try {
			reader = new BufferedReader(new FileReader(selectedFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		String currentLine = "";
		while (currentLine != null)
		{
			try {
				currentLine = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(currentLine);
		}*/
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
