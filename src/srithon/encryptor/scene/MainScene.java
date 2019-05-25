package srithon.encryptor.scene;

import java.io.File;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import srithon.encryptor.encryption.Instruction;
import srithon.encryptor.encryption.InstructionClickable;
import srithon.encryptor.encryption.Runner;
import srithon.encryptor.encryption.Handler;

public class MainScene extends Scene
{
	private static ArrayList<InstructionClickable> queue;
	
	private static Pane listPane;
	private static Pane buttonPane;
	
	private static VBox layout;
	
	private static HBox listPaneBoxBox;
	private static VBox[] listPaneBoxes;
	
	private static final double width = 400.0;
	private static final double height = 400.0;
	
	private static final Font font;
	
	static
	{
		queue = new ArrayList<InstructionClickable>();
		
		layout = new VBox();
		
		listPaneBoxBox = new HBox();
		listPaneBoxes = new VBox[3];
		
		for (int i = 0; i < listPaneBoxes.length; i++)
			listPaneBoxes[i] = new VBox();
		
		buttonPane = new Pane();
		listPane = new Pane();
		
		listPane.setPrefSize(width, height / 1.5);
		buttonPane.setPrefSize(width, height / 3.0);
		
		font = new Font("Times New Roman", 24);
		
		layout.getChildren().addAll(listPane, buttonPane);
	}
	
	public MainScene()
	{
		super(layout);
		
		HBox encryptDecryptButtons = new HBox();
		HBox runBox = new HBox();
		
		Button encrypt = new Button("Encrypt");
		Button decrypt = new Button("Decrypt");
		
		Button run = new Button("Run");
		
		encrypt.setFont(font);
		decrypt.setFont(font);
		
		run.setFont(font);
		
		encrypt.setMinSize(width / 2.0, height / 6.0);
		decrypt.setMinSize(width / 2.0, height / 6.0);
		
		run.setMinSize(width, height / 6.0);
		
		encrypt.setOnAction((ActionEvent event) ->
		{
			EnDeButtonHandler.handle(true);
		});
		
		decrypt.setOnAction((ActionEvent event) ->
		{
			EnDeButtonHandler.handle(false);
		});
		
		run.setOnAction((ActionEvent event) ->
		{
			if (!Handler.keyIsValid())
			{
				AlertBox.alert("Empty Password", "Close this window and press 'J' to open the password window");
				return;
			}
			
			ArrayList<Boolean> successes = new ArrayList<Boolean>();
			
			for (InstructionClickable x : queue)
			{
				successes.add(x.execute());
			}
			
			InstructionClickable.flash(queue, successes);
		});
		
		this.setOnKeyPressed((KeyEvent keyPress) ->
		{
			if (keyPress.getCode().equals(KeyCode.J))
			{
				KeyPrompt.prompt();
			}
		});
		
		for (int i = 0; i < listPaneBoxes.length; i++)
		{
			listPaneBoxes[i].setMaxSize(width / listPaneBoxes.length, listPane.getMaxHeight());
		}
		
		for (VBox x : listPaneBoxes)
		{
			listPaneBoxBox.getChildren().add(x);
		}
		
		VBox buttonPaneBox = new VBox();
		
		runBox.getChildren().add(run);
		encryptDecryptButtons.getChildren().addAll(encrypt, decrypt);
		
		buttonPaneBox.getChildren().addAll(runBox, encryptDecryptButtons);
		
		listPane.getChildren().add(listPaneBoxBox);
		buttonPane.getChildren().add(buttonPaneBox);
	}
	
	public static ArrayList<InstructionClickable> getQueue()
	{
		return queue;
	}
	
	public static double getSceneWidth()
	{
		return width;
	}
	
	public static void addToQueue(InstructionClickable instruction)
	{
		queue.add(instruction);
	}
	
	private static class EnDeButtonHandler
	{
		public static void handle(boolean encrypting)
		{
			String titlePrefix = null;
			String inputFileName = null;
			String outputFileName = null;
			
			if (encrypting)
			{
				titlePrefix = "Encrypting";
				inputFileName = "Source";
				outputFileName = "Encrypted Output";
			}
			else
			{
				titlePrefix = "Decrypting";
				inputFileName = "Encrypted";
				outputFileName = "Decrypted Output";
			}
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
			fileChooser.setTitle(titlePrefix + " - Open " + inputFileName + " File");
			//fileChooser.getExtensionFilters().addAll(
			//		new ExtensionFilter("Text Files", "*.txt", "*.cpp", "*.h", "*.java", "*.c"));

			File inputFile;
			File outputFile;
			
			inputFile = fileChooser.showOpenDialog(Runner.getStage());
			
			if (inputFile == null)
			{
				return;
			}

			fileChooser.setTitle(titlePrefix + " - Open " + outputFileName + " File");
			fileChooser.setInitialDirectory(inputFile.getParentFile());
			
			outputFile = fileChooser.showOpenDialog(Runner.getStage());
			
			if (outputFile == null)
			{
				return;
			}
			
			addToQueue(new InstructionClickable(new Instruction(inputFile, outputFile, encrypting)));
			
			updateQueue();
		}
	}
	
	public static void updateQueue()
	{
		for (VBox x : listPaneBoxes)
			x.getChildren().clear();
		
		
		for (int i = 0; i < queue.size(); i++)
		{
			queue.get(i).getText().clear();
			queue.get(i).getText().setText("#" + (i + 1) + " - " + queue.get(i).getInstruction().toString());
			listPaneBoxes[i / 3].getChildren().add(queue.get(i));
		}
	}
}
