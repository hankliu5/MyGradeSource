
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageReader {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
    String link = "http://www.gradesource.com/reports/5108/27681/coursestand.html";
    Scanner scnr = new Scanner(System.in);
		Document doc = Jsoup.connect(link).get();
		Elements tableElements = doc.select("table");
		Elements tableRowElements = tableElements.select(":not(thead) tr");
		// The 1D size will be over than actual size we put data in.
		// But not over too much. I think no need to worry to much.
		String[][] tableData = new String[tableRowElements.size()][];
		
		int colWithData = 0; 
/*
		System.out.print("Please enter your secret number: ");
		int userInput = scnr.nextInt();
		*/

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
						//jsoup mas &nbsp; to U+00A0.
						if (!rowItems.get(j).text().equals("\u00a0")) {
							System.out.print(rowItems.get(j).text() + " ");
						}
					}
					System.out.println();
				} else if (rowItems.get(0).text().length() == 4) {
					tableData[rowWithData] = new String[rowItems.size()];
						for (int j = 0; j < rowItems.size(); j++) {
							if (!rowItems.get(j).text().equals("\u00a0")) {
								// System.out.print(rowItems.get(j).text() + " ");
								tableData[rowWithData][colWithData] = rowItems.get(j).text();
								colWithData++;
							}	
						}
						rowWithData++;
					}	
				}
			}
		for (int i = 0; i < rowWithData; i++) {
			for (int j = 0; j < colWithData; j++) {
				System.out.print(tableData[i][j] + " ");
			}
			System.out.println("");
		}
		System.out.println(rowWithData);
		scnr.close();
	}
}
