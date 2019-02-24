package srithon.encryptor.scene;

import java.io.File;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import srithon.encryptor.encryption.Handler;
import srithon.encryptor.encryption.Runner;

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
	
	//private static String key;
	
	static
	{
		//key = new String("");
		
		queue = new ArrayList<InstructionClickable>();
		
		layout = new VBox();
		
		listPaneBoxBox = new HBox();
		listPaneBoxes = new VBox[3];
		
		buttonPane = new Pane();
		listPane = new Pane();
		
		listPane.setPrefSize(width, height / 1.5);
		buttonPane.setPrefSize(width, height / 3.0);
		
		layout.getChildren().addAll(listPane, buttonPane);
	}
	
	public MainScene(VBox layoutb)//, double xDim, double yDim)
	{
		super(layout);//, xDim, yDim);
		
		HBox encryptDecryptButtons = new HBox();
		HBox runBox = new HBox();
		
		Button encrypt = new Button("Encrypt");
		Button decrypt = new Button("Decrypt");
		
		Button run = new Button("Run");
		
		encrypt.setFont(new Font("Times New Roman", 24));
		decrypt.setFont(new Font("Times New Roman", 24));
		
		run.setFont(new Font("Times New Roman", 24));
		
		encrypt.setMinSize(width / 2.0, height / 6.0);
		decrypt.setMinSize(width / 2.0, height / 6.0);
		
		run.setMinSize(width, height / 6.0);
		
		encrypt.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				EnDeButtonHandler.handle(true);
			}
		});
		
		decrypt.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent arg0)
			{
				EnDeButtonHandler.handle(false);
			}
		});
		
		run.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent ev)
			{
				while (queue.size() > 0)
				{
					queue.get(0).execute();
				}
				
				updateQueue();
			}
		});
		
		this.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent keyPress)
			{
				if (keyPress.getCode().equals(KeyCode.J))
				{
					KeyPrompt.prompt();
				}
			}
		});
		
		/*this.setOnKeyPressed(new EventHandler<KeyEvent>()
		{

			@Override
			public void handle(KeyEvent arg0) {
				updateQueue();
				System.out.println("KEY PRESSED");
			}
			
		});*/
		
		for (int i = 0; i < listPaneBoxes.length; i++)
		{
			listPaneBoxes[i] = new VBox();
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
		
		/*new Thread(() ->
		{
			while (true)
			{
				try
				{
					Thread.sleep(1000);
				}
				catch(InterruptedException e) {}
				
				Platform.runLater(() -> updateQueue());
			}
		}).start();*/
		
		//for (int i = 0; i < 6; i++)
			//addToQueue(new InstructionClickable(new Instruction(new File("C:\\Hello\\GoodBye\\hey" + i + ".txt"), new File("C:\\Hello\\GoodBye\\hello" + i + ".txt"), true)));
	}
	
	public static void addToQueue(InstructionClickable instruction)
	{
		queue.add(instruction);
		//updateQueue();
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
			fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter("Text Files", "*.txt", "*.cpp", "*.h", "*.java", "*.c"));

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
			
			//Handler.handleRaw(inputFile, outputFile, encrypting);
		}
	}
	
	private static void updateQueue()
	{
		//System.out.println(queue.size());
		
		for (VBox x : listPaneBoxes)
			x.getChildren().clear();
		
		
		for (int i = 0; i < queue.size(); i++)
		{
			queue.get(i).text.clear();
			queue.get(i).text.setText("#" + (i + 1) + " - " + queue.get(i).getInstruction().toString());
			listPaneBoxes[i / 3].getChildren().add(queue.get(i));
		}
		
		/*
		 * Why do you have to call this method twice in
		 * order to reflect the number on the last instruction?
		 */
	}
	
	private static class Instruction
	{
		//private static int CNT;
		//private int id;
		private File in;
		private File out;
		private boolean encrypting;
		
		public Instruction(File in, File out, boolean encrypting)
		{
			this.in = in;
			this.out = out;
			this.encrypting = encrypting;
			//this.id = CNT++;
		}
		
		public void execute()
		{
			Handler.handleRaw(in, out, encrypting);
		}
		
		public String toString()
		{
			String[] split = in.getAbsolutePath().split("\\\\");
			String inStr = split[split.length - 2] + File.separatorChar + split[split.length - 1];
			
			split = out.getAbsolutePath().split("\\\\");
			String outStr = split[split.length - 2] + File.separatorChar + split[split.length - 1];
			
			return (encrypting ? "ENCRYPTING" : "DECRYPTING") + "\n"
					+ inStr + "\n"
					+ outStr + "\n";
		}
	}
	
	private static class InstructionClickable extends HBox
	{
		private TextArea text;
		//private InstructionClickable self;
		private Instruction instruction;
		//private boolean waitingForMouseDragRelease;
		
		{
			text = new TextArea();
			
			text.setMinSize(width / 6.0, 50.0); //was 20
			text.setMaxSize(width, 70.0);
			
			//waitingForMouseDragRelease = false;
			//self = this;
		}
		
		public InstructionClickable(Instruction instruction)
		{
			super();
			
			this.setMinSize(width / 6.0, 50.0);
			this.setMaxSize(width, 70.0);
			
			text.setEditable(false);
			text.setMouseTransparent(true);
			text.setFocusTraversable(false);
			text.setScrollTop(Double.MIN_VALUE);
			
			//text.setScrollLeft(0);
			//text.setScrollTop(0);
			
			this.setOnMouseClicked(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent click)
				{
					removeSelfFromQueue();
					updateQueue();
					//self = null;
					text = null;
				}
			});
			/*this.setOnMouseDragEntered(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent drag)
				{
					//int thisInd = queue.indexOf((InstructionClickable) drag.getSource());
					waitingForMouseDragRelease = true;
					
					System.out.println("Entered");
				}
			});
			this.setOnMouseDragReleased(new EventHandler<MouseEvent>()
			{
				@Override
				public void handle(MouseEvent drag)
				{
					for (int i = 0; i < queue.size(); i++)
					{
						if (queue.get(i).isWaitingForMouseDragRelease())
						{
							queue.get(i).handleMouseDragRelease(queue.indexOf(this));
							break;
						}
					}
					/*if (targetInd > thisInd)
					{
						queue.remove(thisInd);
						queue.add(targetInd, self);
					}
					else
					{
						queue.remove(thisInd);
						queue.add(targetInd, self);
					}*/
					
					/*queue.remove(thisInd);
					queue.add(targetInd, self); * /
					
					updateQueue();
				}
			});*/
			
			this.instruction = instruction;
			
			this.getChildren().add(text);
			
			//self = this;
		}
		
		/*public boolean isWaitingForMouseDragRelease()
		{
			return waitingForMouseDragRelease;
		}
		
		public void handleMouseDragRelease(int index)
		{
			int thisInd = queue.indexOf(this);
			
			queue.remove(thisInd);
			queue.add(index, self);
			
			System.out.println("Handling");
			
			waitingForMouseDragRelease = false;
		}*/
		
		public Instruction getInstruction()
		{
			return instruction;
		}
		
		public void execute()
		{
			instruction.execute();
			removeSelfFromQueue();
		}
		
		public void removeSelfFromQueue()
		{
			queue.remove(queue.indexOf(this));
		}
	}
	
	/*public static void setKey(String key)
	{
		MainScene.key = key;
	}*/
}
