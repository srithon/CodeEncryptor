package srithon.encryptor.encryption;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import srithon.encryptor.backend.Delay;
import srithon.encryptor.backend.TaskContainer;
import srithon.encryptor.backend.TaskObject;
import srithon.encryptor.scene.MainScene;

public class InstructionClickable extends HBox
{
	private static final Background defaultBackground;
	private static final Background alternateBackground;

	private static final Background successBackground;
	private static final Background failureBackground;

	private static final Border defaultBorder;

	private static final Font timesNewRoman24;
	private static final Font timesNewRoman16;

	private TextArea text;
	private Instruction instruction;
	private boolean requestingLife;

	static
	{
		defaultBackground = new Background(new BackgroundFill(Color.ALICEBLUE, CornerRadii.EMPTY, Insets.EMPTY));
		alternateBackground = new Background(new BackgroundFill(Color.BISQUE, CornerRadii.EMPTY, Insets.EMPTY));

		successBackground = new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY));
		failureBackground = new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY));

		defaultBorder = new Border(new BorderStroke(Color.BLACK,
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT));

		timesNewRoman24 = new Font("Times New Roman", 24);
		timesNewRoman16 = new Font("Times New Roman", 16);
	}

	{
		text = new TextArea();

		text.setMinSize(MainScene.getSceneWidth() / 6.0, 50.0); //was 20
		text.setMaxSize(MainScene.getSceneWidth(), 70.0);

		text.setBorder(defaultBorder);
	}

	public InstructionClickable(Instruction instruction)
	{
		super();

		this.setMinSize(MainScene.getSceneWidth() / 6.0, 50.0);
		this.setMaxSize(MainScene.getSceneWidth(), 70.0);

		text.setBackground(defaultBackground);
		text.setEditable(false);
		text.setMouseTransparent(true);
		text.setFocusTraversable(false);
		text.setScrollTop(Double.MIN_VALUE);

		this.setOnMouseClicked((MouseEvent click) ->
		{
			final InstructionClickable target = this;

			if (click.getButton().equals(MouseButton.PRIMARY))
			{
				removeSelfFromQueue();
				MainScene.updateQueue();
				text = null;
			}
			else
			{
				if (Boolean.TRUE.equals(this.getInstruction().getType()))
				{ //TODO replace repeated code
					this.getInstruction().setType(null);

					Runner.addTask(new TaskContainer(
							new TaskObject(() -> {
								Platform.runLater(() ->
								{
									target.getChildren().clear();

									target.setBackground(alternateBackground);

									Label message = new Label();

									message.setFont(timesNewRoman16);

									message.setAlignment(Pos.CENTER);

									message.setBorder(defaultBorder);

									message.setText("Checksum Enabled");

									message.setMinSize(target.getWidth(), target.getHeight());
									message.setMaxSize(target.getWidth(), target.getHeight());

									target.getChildren().add(message);
								});
							}),
							new Delay(750),
							new TaskObject(() -> {
								Platform.runLater(() ->
								{
									target.getChildren().clear();
									target.getChildren().add(target.getText());
									target.setBackground(defaultBackground);
								});
							}),
							new TaskObject(() -> {
								Platform.runLater(() -> target.getText().setBackground(alternateBackground));
							})
						));
				}
				else if (target.getInstruction().getType() == null)
				{
					target.getInstruction().setType(true);

					Runner.addTask(new TaskContainer(
							new TaskObject(() ->
									Platform.runLater(() ->
									{
										target.getChildren().clear();

										target.setBackground(alternateBackground);

										Label message = new Label();

										message.setFont(timesNewRoman16);

										message.setAlignment(Pos.CENTER);

										message.setBorder(defaultBorder);

										message.setText("Checksum Disabled");

										message.setMinSize(target.getWidth(), target.getHeight());
										message.setMaxSize(target.getWidth(), target.getHeight());

										target.getChildren().add(message);
									}
							)),
							new Delay(750),
							new TaskObject(() ->
									Platform.runLater(() ->
									{
										target.getChildren().clear();
										target.getChildren().add(target.getText());
										target.setBackground(defaultBackground);

										target.getText().setBackground(defaultBackground);
									})
							)));					
				}

				target.setRequestingLife(true);
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

	public Boolean execute()
	{
		return instruction.execute();
	}

	public void removeSelfFromQueue()
	{
		MainScene.getQueue().remove(MainScene.getQueue().indexOf(this));
	}

	public static TaskContainer flashContainer(ArrayList<InstructionClickable> objects, ArrayList<Boolean> successes)
	{
		return (new TaskContainer(
				new TaskObject(1, 0, false, () -> {
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

							notifications[i].setFont(timesNewRoman24);

							if (successes.get(i) == null)
							{
								notifications[i].setText("Unknown");
							}
							else
							{
								notifications[i].setText((Boolean.TRUE.equals(successes.get(i)) ? "Success" : "Failure"));
							}

							notifications[i].setAlignment(Pos.CENTER);

							notifications[i].setBorder(defaultBorder);

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
					}
				),
				/*
				 * None of this is happening and idk why
				 */
				new TaskObject(1, 0, false, () -> {
						System.out.println("Flashing special color");
						Platform.runLater(() ->
						{
							for (int i = 0; i < objects.size(); i++)
							{
								if (successes.get(i) == null)
								{
									objects.get(i).setBackground(alternateBackground);
								}
								else
								{
									objects.get(i).setBackground((Boolean.TRUE.equals(successes.get(i)) ? successBackground : failureBackground));
								}
							}
						});
				}),
				new TaskObject(1, 500, true, () -> {
						System.out.println("Flashing regular color");
						Platform.runLater(() ->
						{
							for (int i = 0; i < objects.size(); i++)
								objects.get(i).setBackground(defaultBackground);
						});
				}),
				new TaskObject(1, 500, true, () ->
						Platform.runLater(() ->
						{
							System.out.println("Flashing special color");
							for (int i = 0; i < objects.size(); i++)
							{
								if (successes.get(i) == null)
								{
									objects.get(i).setBackground(alternateBackground);
								}
								else
								{
									objects.get(i).setBackground((Boolean.TRUE.equals(successes.get(i)) ? successBackground : failureBackground));
								}
							}
						})
				),
				new TaskObject(1, 500, true, () -> {
						System.out.println("Flashing default color");
						Platform.runLater(() ->
						{
							for (int i = 0; i < objects.size(); i++)
								objects.get(i).setBackground(defaultBackground);
						});
				}),
				new TaskObject(1, 0, false, () ->
						Platform.runLater(() ->
						{
							for (InstructionClickable j : objects)
							{
								j.getChildren().clear();
							}

							for (int i = 0; i < objects.size(); i++)
							{
								objects.get(i).getChildren().add(objects.get(i).getText());
							}
						})
				),
				new TaskObject(1, 1000, true, () -> {
						for (int i = 0; i < objects.size(); i++)
						{
							System.out.println("Checking index " + i);
							if (successes.get(i) == null || Boolean.TRUE.equals(successes.get(i)))
							{
								objects.get(i).removeSelfFromQueue();
								System.out.println("Removed " + i + " from the queue");
								//objects.remove(i);
								//i--;
							}
						}
						System.out.println("Next am going to updateQueue");
				}),
				new TaskObject(1, 0, false, () -> Platform.runLater(MainScene::updateQueue))));
	}
}