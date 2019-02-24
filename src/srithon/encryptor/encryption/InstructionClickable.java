package srithon.encryptor.encryption;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import srithon.encryptor.scene.MainScene;
import javafx.scene.Node;

public class InstructionClickable extends HBox
{
	private static final Background defaultBackground;
	private static final Background alternateBackground;
	
	private static final Background successBackground;
	private static final Background failureBackground;
	
	private TextArea text;
	private Instruction instruction;
	private boolean requestingLife;
	
	static
	{
		defaultBackground = new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY));
		alternateBackground = new Background(new BackgroundFill(Color.FORESTGREEN, CornerRadii.EMPTY, Insets.EMPTY));
		
		successBackground = new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY));
		failureBackground = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));
	
	}
	
	{
		text = new TextArea();
		
		text.setMinSize(MainScene.getSceneWidth() / 6.0, 50.0); //was 20
		text.setMaxSize(MainScene.getSceneWidth(), 70.0);
	}
	
	public InstructionClickable(Instruction instruction)
	{
		super();
		
		this.setMinSize(MainScene.getSceneWidth() / 6.0, 50.0);
		this.setMaxSize(MainScene.getSceneWidth(), 70.0);
		//this.setBackground(defaultBackground);
		
		//text.setStyle("-fx-background-color: #D3D3D3;");
		text.setBackground(defaultBackground);
		text.setEditable(false);
		text.setMouseTransparent(true);
		text.setFocusTraversable(false);
		text.setScrollTop(Double.MIN_VALUE);

		this.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent click)
			{
				if (click.getButton().equals(MouseButton.PRIMARY))
				{
					removeSelfFromQueue();
					MainScene.updateQueue();
					text = null;
				}
				else
				{
					InstructionClickable target = null;
					boolean good = false;
					
					try
					{
						target = (InstructionClickable) click.getTarget();
						good = true;
					}
					catch (ClassCastException e)
					{
						
					}
					
					if (good && Boolean.TRUE.equals(target.getInstruction().getType()))
					{
						target.getInstruction().setType(null);
						//target.text.setStyle("-fx-background-color: #DB7093;");
						target.getText().setBackground(alternateBackground);
					}
					else if (good && target.getInstruction().getType() == null)
					{
						target.getInstruction().setType(true);
						//target.text.setStyle("-fx-background-color: #D3D3D3;");
						target.getText().setBackground(defaultBackground);
					}
					
					if (good)
						target.setRequestingLife(true);
					else
					{
						/*Node x = (Node) (click.getTarget());
						InstructionClickable y = (InstructionClickable) x.getParent();
						
						y.setRequestingLife(true);*/
					}
				}
			}
		});

		this.instruction = instruction;

		this.getChildren().add(text);
	}

	public Instruction getInstruction()
	{
		return instruction;
	}
	
	public TextArea getText()
	{
		return text;
	}
	
	public void setRequestingLife(boolean requestingLife)
	{
		this.requestingLife = requestingLife;
	}
	
	public boolean requestsLife()
	{
		return requestingLife;
	}

	public boolean execute()
	{
		return instruction.execute();
		//removeSelfFromQueue();
	}

	public void removeSelfFromQueue()
	{
		MainScene.getQueue().remove(MainScene.getQueue().indexOf(this));
	}
	
	public static void flash(ArrayList<InstructionClickable> clickables, ArrayList<Boolean> successes)
	{
		Runner.setThreadRunnable(new Flash(clickables, successes));
		Runner.getThread().start();
	}
	
	private static class Flash implements Runnable
	{
        private ArrayList<InstructionClickable> objects;
        private ArrayList<Boolean> successes;
        
        public Flash(ArrayList<InstructionClickable> objects, ArrayList<Boolean> successes)
        {
        	this.objects = objects;
        	this.successes = successes;
        }
        
        public void run()
        {
        	Label[] notifications = new Label[objects.size()];
        	
        	Platform.runLater(() ->
    		{
    			for (InstructionClickable j : objects)
        		{
        			j.getChildren().clear();
        		}
    		});
        	
        	for (int i = 0; i < objects.size(); i++)
        	{
        		notifications[i] = new Label();
        		
				notifications[i].setFont(new Font("Times New Roman", 24));
				
				notifications[i].setText((Boolean.TRUE.equals(successes.get(i)) ? "Success" : "Failure"));
				
				notifications[i].setMinSize(objects.get(i).getWidth(), objects.get(i).getHeight());
				notifications[i].setMaxSize(objects.get(i).getWidth(), objects.get(i).getHeight());
        	}
        	
        	Platform.runLater(() ->
        	{
        		for (int i = 0; i < objects.size(); i++)
        		{
        			objects.get(i).getChildren().add(notifications[i]);
        		}
        	});
			
			for (int j = 0; j < 2; j++)
			{
				Platform.runLater(() ->
				{
					for (int i = 0; i < objects.size(); i++)
						objects.get(i).setBackground((Boolean.TRUE.equals(successes.get(i)) ? successBackground : failureBackground));
				});
				
				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e)
				{
					
				}
				
				Platform.runLater(() ->
				{
					for (int i = 0; i < objects.size(); i++)
						objects.get(i).setBackground(defaultBackground);
				});
				
				try
				{
					Thread.sleep(500);
				}
				catch (InterruptedException e)
				{
					
				}
			}
			
			Platform.runLater(() ->
    		{
    			for (InstructionClickable j : objects)
        		{
        			j.getChildren().clear();
        		}
    		});
			
			Platform.runLater(() ->
			{
				for (int i = 0; i < objects.size(); i++)
				{
					objects.get(i).getChildren().add(objects.get(i).getText());
				}
			});
			
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				
			}
			
			Platform.runLater(() ->
			{
				for (int i = 0; i < objects.size(); i++)
				{
					if (Boolean.TRUE.equals(successes.get(i)))
					{
						objects.get(i).removeSelfFromQueue();
					}
				}
			});
			
			Platform.runLater(() -> MainScene.updateQueue());
        }
    }
}