import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SecondStage {
  public SecondStage() {
    Stage secondStage = new Stage();
    TableView dataTable = new TableView();
    dataTable.setEditable(false);

    TableColumn secretNumCol = new TableColumn("Secret Number");
    TableColumn bookCol = new TableColumn("Book Activities");
    TableColumn quizCol = new TableColumn("Quizzes");
    TableColumn hwCol = new TableColumn("Homework");
    TableColumn lectureCol = new TableColumn("Lectures");
    TableColumn midCol = new TableColumn("Midterm");
    TableColumn extraCol = new TableColumn("Extra Credit");
    TableColumn courseCol = new TableColumn("Course");
            
    dataTable.getColumns().addAll(secretNumCol, bookCol, quizCol, hwCol, 
    		lectureCol, midCol, extraCol, courseCol);

    TableColumn pointCol = new TableColumn("Points");
    TableColumn scoreCol = new TableColumn("Score");

    secretNumCol.getColumns().addAll(pointCol, scoreCol);
    bookCol.getColumns().addAll(pointCol, scoreCol);
    quizCol.getColumns().add(scoreCol);
    hwCol.getColumns().add(scoreCol);
    lectureCol.getColumns().addAll(pointCol, scoreCol);
    midCol.getColumns().addAll(pointCol, scoreCol);
    extraCol.getColumns().addAll(pointCol, scoreCol);
    courseCol.getColumns().add(scoreCol);
    
    
    StackPane root = new StackPane();
    root.getChildren().add(dataTable);
    secondStage.setScene(new Scene(root));
    secondStage.show();
  }
}
