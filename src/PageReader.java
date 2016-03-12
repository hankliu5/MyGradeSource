
import java.io.IOException;
import java.util.HashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageReader {

	public PageReader(String userInput) throws IOException{
		// TODO Auto-generated method stub
    String link = "http://www.gradesource.com/reports/5108/27681/coursestand.html";
		Document doc = Jsoup.connect(link).get();
		Elements tableElements = doc.select("table");
		Elements tableRowElements = tableElements.select(":not(thead) tr");
		// The 1D size will be over than actual size we put data in.
		// But not over too much. I think no need to worry to much.
		String[][] tableData = new String[tableRowElements.size()][];
		HashMap<String, Integer> numMapData = new HashMap<String, Integer>();
		int colWithData = 0; 
		int rowWithData = 0; // only need data start from the first secret number.
		
		for (int i = 0; i < tableRowElements.size(); i++) {
			Element row = tableRowElements.get(i);
			Elements rowItems = row.select("td");
			colWithData = 0; // reset the count of column with data inside.
			// 2 is for filtering the upper title, link or images
			if (rowItems.size() > 2) { 
				// print out table title
				if (rowItems.get(0).text().equals("Secret Number")) { 
					for (int j = 0; j < rowItems.size(); j++) {
						//jsoup maps &nbsp; to U+00A0.
						if (!rowItems.get(j).text().equals("\u00a0")) {
							System.out.print(rowItems.get(j).text() + " ");
						}
					}
					System.out.println();
				} else if (rowItems.get(0).text().length() == 4) {
					numMapData.put(rowItems.get(0).text(), rowWithData);
					tableData[rowWithData] = new String[rowItems.size()];
						for (int j = 0; j < rowItems.size(); j++) {
							if (!rowItems.get(j).text().equals("\u00a0")) {
								tableData[rowWithData][colWithData] = rowItems.get(j).text();
								colWithData++;
							}	
						}
						rowWithData++;
					}	
				}
			}
		
		if (numMapData.containsKey(userInput)) {
			int userRow = numMapData.get(userInput);
  		for (int j = 0; j < colWithData; j++) {
  			System.out.print(tableData[userRow][j] + " ");
  		}
			System.out.println();
			// index + 1 is actual rank.
			System.out.println("Your Current Rank: " + (userRow + 1));
		} else {
			System.out.println("Cannot find the number.");
		}
	}
}
