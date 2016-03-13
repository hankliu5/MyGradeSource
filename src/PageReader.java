import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

/**
 * PageReader class allows application to parse table data from GradeSource's
 * website. This application is only for "overall course standing" page of 
 * CSE11 since there are minor different between different tables.
 * 
 * @author Yu-Chia Liu, Ming-Wei Liu
 * @email yul560@eng.ucsd.edu, mil228@eng.ucsd.edu
 *
 */
public class PageReader {
	String link = "http://www.gradesource.com/reports/5108/27681/coursestand.html";
	Document doc = Jsoup.connect(link).get();
	Elements tableElements = doc.select("table");
	Elements tableRowElements = tableElements.select(":not(thead) tr");
	// The 1D size will be over than actual size we put data in.
	// But not over too much. I think no need to worry to much.
	String[][] tableData = new String[tableRowElements.size()][];
	String[] title;
	Map<String, Integer> numMapData = new HashMap<String, Integer>();
	int colWithData = 0; 
	int rowWithData = 0; // only need data start from the first secret number.
	
	/**
	 * Reads and stores table data from the website.
	 * @throws IOException
	 */
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
					// start from the next column to secret number.
					for (int j = 1; j < rowItems.size(); j++) {
						// jsoup maps &nbsp; to U+00A0. we don't need empty cell.
						if (!rowItems.get(j).text().equals("\u00a0")) {
							// to simplify the type of data. only grab score column.
							if ((rowItems.get(j).text().indexOf("%") != -1)) {
								tableData[rowWithData][colWithData] = rowItems.get(j).text();
								colWithData++;
							}
						}
					}
					rowWithData++;
				} else if (rowItems.get(0).text().equals("Secret Number")) {
					title = new String[rowItems.size()];
					// start from the next column to secret number.
					for (int j = 1; j < rowItems.size(); j++) {
						// jsoup maps &nbsp; to U+00A0.
						if (!rowItems.get(j).text().equals("\u00a0")) {
							title[colWithData] = rowItems.get(j).text();
							colWithData++;
						}
					}
				}
			}
		}
	}
	
	/**
	 * allows main GUI to run the search function.
	 * if user enter "demo" and run, this method will grab a secret number
	 * from the map, and then run this method again with grabbed secret number.
	 * (concept of recursion).
	 *  
	 * @param userInput
	 */
	public void searchUser(String userInput){
		if (numMapData.containsKey(userInput)) {
			int rankRow = numMapData.get(userInput);
			// the last element is overall score.
			// Since I increment one on colWithData before finish every loop
			// I minus 1 on colWithData here.
			double overallScore = Double.parseDouble
					(tableData[rankRow][colWithData - 1].replace("%", ""));
			String userGrade = giveUserGrade(overallScore);
			
			// setting a dialog for showing the result.
			Dialog<Pair<String, String>> dialog = new Dialog<>();
			dialog.setTitle("Result");
			dialog.setHeaderText("Your Current Rank: " + (rankRow + 1) + " / " + rowWithData);
			ButtonType showMoreButton = new ButtonType("Show All..", ButtonData.OK_DONE);
			dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE, showMoreButton);
			GridPane grid = new GridPane();
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(20, 150, 10, 10));
			grid.add(new Label("Secret number: " + userInput), 0, 0);
			for (int i = 0; i < colWithData; i++) {
				// secret number already occupied the first row.
				grid.add(new Label(title[i] + ": " + tableData[rankRow][i]), 0, i + 1);
			}
			grid.add(new Label("Current Grade: " + userGrade), 0, colWithData + 1);
			dialog.getDialogPane().setContent(grid);
			// allows showMoreButton to open a website on browser. 
			dialog.setResultConverter(dialogButton -> {
		    if (dialogButton == showMoreButton) {
					try {
						URL sourceURL = new URL(link);
			    	openWebpage(sourceURL);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    }
		    return null;
		});
			dialog.showAndWait();	
		}
		// if user enters demo, it will grab a random secret number here.
		else if (userInput.equals("demo")) {
			Random rand = new Random();
			int randonRank = rand.nextInt(rowWithData);
			String randonNum = (String) getKeyFromValue(numMapData, randonRank);
			searchUser(randonNum);		
		} 
		// if cannot find the number from userInput, show a window alert.
		else {
			Alert alert = new Alert(AlertType.ERROR, "Cannot find your number.");
			alert.showAndWait();
		}
	}
	
	/**
	 * Allows application to open source website through user's default browser.
	 * 
	 * @param uri
	 */
	public static void openWebpage(URI uri) {
    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
        try {
            desktop.browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	}
	
	/**
	 * Allows application to open source website through user's default browser.
	 * 
	 * @param url
	 */
  public static void openWebpage(URL url) {
    try {
        openWebpage(url.toURI());
    } catch (URISyntaxException e) {
        e.printStackTrace();
    }
  }
	
  /**
   * Uses user's overall score to check user's letter grade.
   *  
   * @param score
   * @return - user's current letter grade.
   */
	public String giveUserGrade(double score) {
		if (score >= 100) {
			return "A+";
		} else if (score >= 93) {
			return "A";
		} else if (score >= 90) {
			return "A-";
		} else if (score >= 87) {
			return "B+";
		} else if (score >= 83) {
			return "B";
		} else if (score >= 80) {
			return "B-";
		} else if (score >= 77) {
			return "C+";
		} else if (score >= 73) {
			return "C";
		} else if (score >= 70) {
			return "C-";
		} else if (score >= 60) {
			return "D";
		} else {
			return "F";
		}
	}
	
	/**
	 * gets a random rank number and then switch it into its key.
	 * 
	 * @param hm
	 * @param value
	 * @return - value's key
	 */
	public static Object getKeyFromValue(Map<String, Integer> hm, int value) {
    for (Object o : hm.keySet()) {
      if (hm.get(o).equals(value)) {
        return o;
      }
    }
    return null;
  }
}
