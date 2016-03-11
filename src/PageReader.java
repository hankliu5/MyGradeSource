
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
    try {
      Document doc = Jsoup.connect(link).get();
      Elements tableElements = doc.select("table");
      Elements tableRowElements = tableElements.select(":not(thead) tr");
      int[] secretNums = new int[tableRowElements.size()];
      ArrayList<String> rowData = new ArrayList<String>();
      int rank = -1;
      
  		System.out.print("Please enter your secret number: ");
  		int userInput = scnr.nextInt();
      
  		int rowWithData = 0;
      for (int i = 0; i < tableRowElements.size(); i++) {
        Element row = tableRowElements.get(i);
        Elements rowItems = row.select("td");
        if (rowItems.size() > 2) { // 2 is for filtering the upper title, link or images
        	if (rowItems.get(0).text().equals("Secret Number")) {
        		for (int j = 0; j < rowItems.size(); j++) {
      				if (!rowItems.get(j).text().equals("\u00a0")){ // jsoup maps &nbsp; to U+00A0.
      					System.out.print(rowItems.get(j).text() + " ");
      				}
            }
        		System.out.println();	
        	}
        	else if (rowItems.get(0).text().length() == 4) {
        		secretNums[rowWithData] = Integer.parseInt(rowItems.get(0).text()); 
        		if (secretNums[rowWithData] == userInput) {
        			rank = rowWithData + 1;
        			for (int j = 0; j < rowItems.size(); j++) {
        				if (!rowItems.get(j).text().equals("\u00a0")){
                  System.out.print(rowItems.get(j).text() + " "); 
                  rowData.add(rowItems.get(j).text());
        				}
        			}
              System.out.println();
        		}
        		rowWithData++;
        	}
        }
      }
  		System.out.println("Your current rank is " + rank);
  		for (int i = 0; i < secretNums.length; i++) {
  			System.out.print(secretNums[i] + " ");
  		}
  		System.out.println("");
  		System.out.println(rowData);
  			
   } catch (IOException e) {
      e.printStackTrace();
   } finally {
  	 scnr.close();
   }
	}
}
