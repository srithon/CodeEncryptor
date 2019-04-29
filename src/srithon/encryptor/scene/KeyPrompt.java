package srithon.encryptor.scene;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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
		
		window.setMinWidth(1000);
		window.setMinHeight(100);
		window.setMaxHeight(300);
		window.setMaxWidth(1250);
		
		window.initModality(Modality.APPLICATION_MODAL);
	}
	
	private KeyPrompt()
	{
		
	}
	
	public static void prompt()
	{
		Label[] characters = new Label[16];
		
		Font labelFont = new Font("Times New Roman", 48);
		
		HBox box = new HBox(10);
		box.setMaxSize(1250, 400);
		box.setMinSize(800, 150);
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
			String character = press.getCharacter();
			char characterChar = character.charAt(0);
			int keyCode = (int) characterChar;
			System.out.println("KeyChar: " + character);
			System.out.println("Numeric Value: " + (int) characterChar);
			
			//delete button
			if ((keyCode == 127) && currentChar > 0)
			{
				characters[--currentChar].setText("");
				return;
			}
			else if (keyCode == 27) //escape key
			{
				if (characters[0].getText().contentEquals(KeyCode.ESCAPE.getChar()))
				{
					Handler.setKey(getKey(characters));
					window.close();
				}
				
				for (Label lab : characters)
				{
					lab.setText(KeyCode.ESCAPE.getChar());
				}
				
				return;
			}

			if (currentChar < characters.length)
			{
				characters[currentChar++].setText(character);
			}
		});
		
		window.setOnCloseRequest((WindowEvent closeRequest) ->
		{
			Handler.setKey(getKey(characters));
			window.close();
		});
		
		window.setScene(sc);
		
		window.show();
	}
	
	public static SecretKeySpec getKey(Label[] characters)
	{
		String key = "";
		
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < currentChar; i++)
		{
			if (!characters[i].getText().equals(KeyCode.ESCAPE.getChar()))
				builder.append(characters[i].getText());
		}
		for (int i = currentChar; i < 16; i++)
		{
			builder.append((char) (Math.pow(i, 2) % 128));
		}
		
		key = builder.toString();
		
		System.out.println(key);
		
		SecretKeySpec retKey = new SecretKeySpec(String.valueOf(key).getBytes(StandardCharsets.UTF_8), "AES");
		key = null;
		return retKey;
	}
	
	public static void main(String[] args)
	{
		prompt();
	}
}
