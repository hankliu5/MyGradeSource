import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class OperateGUI extends Application{
	public void start(Stage primaryStage) { 
		// set up objects
		Label label1 = new Label("Secret Number:"); 
		TextField userTextField = new TextField("Enter Your Secret Number here...");
		Button button1 = new Button("Show Overall");
		Button quitButton = new Button("Quit");
		
		// set up pane
		GridPane pane = new GridPane(); 
		pane.setHgap(5); 
		pane.setVgap(10); 
		pane.setPadding(new Insets(2));
		pane.add(label1, 0, 0);
		pane.add(userTextField, 1, 0);
		pane.add(button1, 0, 1);
		pane.add(quitButton, 1, 1);
		// shows overall
		button1.setOnAction(e -> {
			String userInput = userTextField.getText();
			try {
				new PageReader(userInput);
			} catch (Exception e1) {
				// IOXlsxFile class can handle the error message itself.
				// So just leave it empty.
			}
		}); 
		
		quitButton.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you should you want to quit?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				primaryStage.close();
			}
		});
		
	// sets a scene for primaryStage.
			Scene scene = new Scene(pane);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Updating Stock WorkSheet"); 
			primaryStage.show();

	}


	public static void main(String[] args) throws Exception{
		launch(args);
	}

}