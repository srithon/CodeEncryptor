package srithon.encryptor.scene;

import javafx.event.ActionEvent;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox
{
	private static final double MIN_HEIGHT = 185;
	private static final double MIN_WIDTH = 700;
	
	private static final Font font;
	
	static
	{
		font = new Font("Times New Roman", 18);
	}
	
	private AlertBox()
	{
		
	}
	
	public static void alert(String title, String message)
	{
		Stage window = new Stage();
		
		window.setMinHeight(MIN_HEIGHT);
		window.setMinWidth(MIN_WIDTH);
		
		window.setTitle(title);
		
		VBox box = new VBox(20);
		box.setAlignment(Pos.CENTER);
		box.setMinSize(MIN_WIDTH, MIN_HEIGHT);
		
		Label m = new Label(message);
		m.setAlignment(Pos.TOP_CENTER);
		m.setFont(font);
		m.setFocusTraversable(false);
		m.setText(message);
		
		Button exit = new Button("Close");
		exit.setAlignment(Pos.CENTER);
		
		exit.setOnAction((ActionEvent e) -> {
			window.close();
		});
		
		box.getChildren().addAll(m, exit);
		
		Scene sc = new Scene(box);
		
		window.initModality(Modality.WINDOW_MODAL);
		window.setScene(sc);
		window.show();
	}
}
