
import java.io.IOException;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class PageReader {
	String link = "http://www.gradesource.com/reports/5108/27681/coursestand.html";
	Document doc = Jsoup.connect(link).get();
	Elements tableElements = doc.select("table");
	Elements tableRowElements = tableElements.select(":not(thead) tr");
	// The 1D size will be over than actual size we put data in.
	// But not over too much. I think no need to worry to much.
	String[][] tableData = new String[tableRowElements.size()][];
	String[] title;
	HashMap<String, Integer> numMapData = new HashMap<String, Integer>();
	int colWithData = 0; 
	int rowWithData = 0; // only need data start from the first secret number.
	
	public PageReader() throws IOException{
		
		for (int i = 0; i < tableRowElements.size(); i++) {
			Element row = tableRowElements.get(i);
			Elements rowItems = row.select("td");
			colWithData = 0; // reset the count of column with data inside.
			// 2 is for filtering the upper title, link or images
			if (rowItems.size() > 2) { 
				// print out table title
				if (rowItems.get(0).text().length() == 4) {
					numMapData.put(rowItems.get(0).text(), rowWithData);
					tableData[rowWithData] = new String[rowItems.size()];
						for (int j = 0; j < rowItems.size(); j++) {
							if (!rowItems.get(j).text().equals("\u00a0")) {
								if ((rowItems.get(j).text().indexOf("%") != -1)) {
									tableData[rowWithData][colWithData] = rowItems.get(j).text();
									colWithData++;
								}
							}	
						}
						rowWithData++;
					} else if (rowItems.get(0).text().equals("Secret Number")) { 
  					title = new String[rowItems.size()];
  					// start from the next column of secret number.
  					for (int j = 1; j < rowItems.size(); j++) {
  						//jsoup maps &nbsp; to U+00A0.
  						if (!rowItems.get(j).text().equals("\u00a0")) {
  							title[colWithData] = rowItems.get(j).text();
  							colWithData++;
  						}
  					}
  				} 	
				}
			}
	}
	public void searchUser(String userInput){
		if (numMapData.containsKey(userInput)) {
			int rankRow = numMapData.get(userInput);
			
			// setting a dialog for showing the result.
			Dialog<Pair<String, String>> dialog = new Dialog<>();
			dialog.setTitle("Result");
			dialog.setHeaderText("Your Current Rank: " + (rankRow + 1));
			dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));
			for (int i = 0; i < colWithData; i++) {
				grid.add(new Label(title[i] + ": " + tableData[rankRow][i]), 0, i);
			}
			dialog.getDialogPane().setContent(grid);
			dialog.showAndWait();
		} else {
			Alert alert = new Alert(AlertType.ERROR, "Cannot find your number.");
			alert.showAndWait();
		}
	}
}
