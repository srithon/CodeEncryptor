package srithon.encryptor.scene;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import srithon.encryptor.encryption.Handler;

public class KeyPrompt
{
	private static int currentChar;
	private static String key;
	private static Stage window;
	
	static
	{
		key = "";
		
		currentChar = 0;
		
		window = new Stage();
		
		window.setTitle("Key");
		window.setMinWidth(575);
		window.setMinHeight(200);
		window.setMaxHeight(575);
		window.setMaxWidth(575);
		window.initModality(Modality.APPLICATION_MODAL);
	}
	
	private KeyPrompt()
	{
		
	}
	
	public static void prompt()
	{
		Label[] characters = new Label[10];
		
		Font labelFont = new Font("Times New Roman", 48);
		
		HBox box = new HBox(10);
		box.setMaxSize(575, 575);
		box.setAlignment(Pos.CENTER);
		
		for (int i = 0; i < characters.length; i++)
		{
			characters[i] = new Label((i < key.length() ? String.valueOf(key.charAt(i)) : KeyCode.ESCAPE.getChar()));
			characters[i].setFont(labelFont);
			characters[i].setMaxSize(52.5, 52.5);
			
			box.getChildren().add(characters[i]);
		}
		
		Scene sc = new Scene(box);
		
		sc.setOnMouseClicked((MouseEvent click) ->
		{
			for (int i = currentChar; i < characters.length; i++)
			{
				characters[i].setText(KeyCode.ESCAPE.getChar());
			}

			if (currentChar > 0)
			{
				characters[--currentChar].setText(KeyCode.ESCAPE.getChar());

				for (int j = currentChar; j < characters.length; j++)
				{
					if (!characters[j].getText().equals(KeyCode.ESCAPE.getChar()))
					{
						System.out.println("OOPS");
						System.out.println(characters[j].getText());
						characters[j].setText(KeyCode.ESCAPE.getChar());
					}
				}
			}
			else
			{
				characters[0].setText(KeyCode.ESCAPE.getChar());

				for (int j = 1; j < characters.length; j++)
				{
					if (!characters[j].getText().equals(KeyCode.ESCAPE.getChar()))
					{
						System.out.println("OOPS");
						System.out.println(characters[j].getText());
						characters[j].setText(KeyCode.ESCAPE.getChar());
					}
				}
			}
		});
		
		sc.setOnKeyTyped((KeyEvent press) ->
		{
			if (press.getCode().equals(KeyCode.DELETE) && currentChar > 0)
			{
				characters[--currentChar].setText("");
				return;
			}

			if (currentChar < characters.length)
			{
				characters[currentChar++].setText(press.getCharacter());
			}
		});
		
		window.setOnCloseRequest((WindowEvent closeRequest) ->
		{
			key = "";

			StringBuilder builder = new StringBuilder();

			for (int i = 0; i < currentChar; i++)
			{
				if (!characters[i].getText().equals(KeyCode.ESCAPE.getChar()))
					builder.append(characters[i].getText());
			}

			key = builder.toString();
			Handler.setKey(key.toCharArray());
			System.out.println(key);
			window.close();
		});
		
		window.setScene(sc);
		
		window.show();
	}
	
	public static String getKey(Label[] characters)
	{
		String key = "";
		
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < currentChar; i++)
		{
			if (!characters[i].getText().equals(KeyCode.ESCAPE.getChar()))
				builder.append(characters[i].getText());
		}
		
		key = builder.toString();
		
		return key;
	}
}
