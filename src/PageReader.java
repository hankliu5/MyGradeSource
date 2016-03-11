
import java.io.IOException;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PageReader {

	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
    String link = "http://www.gradesource.com/reports/5108/27681/coursestand.html";
    try {
  		Scanner scnr = new Scanner(System.in);
  		System.out.print("Please enter your secret number: ");
  		int userInput = scnr.nextInt();
      Document doc = Jsoup.connect(link).get();
      Elements tableElements = doc.select("table");
      Elements tableRowElements = tableElements.select(":not(thead) tr");
      
      for (int i = 0; i < tableRowElements.size(); i++) {
        Element row = tableRowElements.get(i);
        Elements rowItems = row.select("td");
        if (rowItems.size() > 2) { // 2 is for filtering the upper title, link or images
        	if (rowItems.get(0).text().length() == 4) {
        		if (userInput == Integer.parseInt(rowItems.get(0).text())) {
              for (int j = 0; j < rowItems.size(); j++) {
              	System.out.print(rowItems.get(j).text() + " ");
              }
              System.out.println();
        		}
        	}
        }
      }

   } catch (IOException e) {
      e.printStackTrace();
   }
	}
}
