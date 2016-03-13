import java.io.IOException;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This class controls the main GUI that allows users to enter their secret
 * number and then search their current grade status of CSE11. 
 * 
 * This application is only for "overall course standing" page of CSE11 since
 * there are minor different between different tables. But we think the 
 * function is enough to reach our primitive goal: allowing users search their
 * status and current rank of this course.
 * 
 * @author Yu-Chia Liu, Ming-Wei Liu
 * @email yul560@eng.ucsd.edu, mil228@eng.ucsd.edu
 * @compile javac -cp jsoup-1.8.3.jar:. OperateGUI.java
 * @run java -cp jsoup-1.8.3.jar:. OperateGUI
 *
 */
public class OperateGUI extends Application{
	
	/**
	 * Default method to start GUI.
	 */
	public void start(Stage primaryStage) throws IOException { 
		// set up objects
		Label label1 = new Label("Secret Number:"); 
		TextField userTextField = new TextField("Enter Your Secret Number here...");
		Text t = new Text("(Enter \"demo\" to pick up a random secret number.)");
		Button showButton = new Button("Show");
		Button quitButton = new Button("Quit");
		showButton.setMaxWidth(Double.MAX_VALUE);
		quitButton.setMaxWidth(Double.MAX_VALUE);

		
		// reading the table first can make search faster.
		PageReader page = new PageReader();
		
		// set up pane
		GridPane pane1 = new GridPane(); 
		pane1.setHgap(5); 
		pane1.setVgap(10); 
		pane1.setPadding(new Insets(10));
		pane1.add(label1, 0, 0);
		pane1.add(userTextField, 1, 0);
		pane1.add(t, 1, 1);
		pane1.add(showButton, 2, 0);
		pane1.add(quitButton, 2, 1);
		
		// sets up the function to show user's grades.
		showButton.setOnAction(e -> {
			String userInput = userTextField.getText();
			try {
				page.searchUser(userInput);
			} catch (Exception e1) {
				// PageReader class can handle the error message itself.
				// So just leave it empty.
			}
		}); 
		
		// sets up the function to close the stage.
		quitButton.setOnAction(e -> {
			Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to quit?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				primaryStage.close();
			}
		});
		
		// sets a scene for primaryStage.
		Scene scene = new Scene(pane1);
		primaryStage.setScene(scene);
		primaryStage.setTitle("My GradeSource"); 
		primaryStage.show();

	}
	
	// main method to launch the GUI.
	public static void main(String[] args){
		launch(args);
	}
}


