package cf.amrebada.DQTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.acl.LastOwnerException;

import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MainAction {

	private static String url1 = "http://alfanous.org/jos2?query=";
	private static Handle obj;
	private static String allAyas = "";
	private static int wordIndex=0;
	private static int dataNum=0;
	public static void main(String[] args) throws ParseException, IOException {
		String url2 = JOptionPane.showInputDialog(null,
				"«œŒ· «·ﬂ·„Â «·· Ï  —Ìœ «·»ÕÀ ⁄‰Â« ");
		// @SuppressWarnings("deprecation")
		String[] words = url2.split(" ");
		JOptionPane.showMessageDialog(null, words.length);
		for (String word : words) {
			JOptionPane.showMessageDialog(null, word);
		}
		for (String word : words) {
			allAyas="";
			String encodedUrl = url1 + URLEncoder.encode(word, "utf-8");
			System.out.println(encodedUrl);
			obj = new Handle(encodedUrl);
			obj.fetchJSON();

			while (obj.parsingComplete)
				;
			String content = obj.getData();

			JSONParser parser = new JSONParser();

			Object object = parser.parse(content);
			JSONObject reader = (JSONObject) object;
			JSONObject search = (JSONObject) reader.get("search");
			JSONObject interval = (JSONObject) search.get("interval");
			JSONObject ayas = (JSONObject) search.get("ayas");
			long nbpage = (Long) interval.get("nb_pages");
			long start = (Long) interval.get("start");
			long end = (Long) interval.get("end");
			long total = (Long) interval.get("total");
			JOptionPane.showMessageDialog(null, "start : " + start
					+ " , end : " + end + " , total : " + total
					+ " , nb_pages : " + nbpage);

			if (start != -1) {
				for (int i = (int) start; i <= (int) end; i++) {
					JSONObject numOfAya = (JSONObject) ayas.get("" + i);
					JSONObject aya = (JSONObject) numOfAya.get("aya");
					allAyas += "<br/>( " + i + " ) : " + aya.get("text")
							+ "<br/>";

				}

				for (int y = 1; y < nbpage; y++) {
					encodedUrl = url1 + URLEncoder.encode(word, "utf-8")
							+ "&page=" + (y + 1);
					obj = new Handle(encodedUrl);
					obj.fetchJSON();

					while (obj.parsingComplete)
						;
					content = obj.getData();

					object = parser.parse(content);
					reader = (JSONObject) object;
					search = (JSONObject) reader.get("search");
					interval = (JSONObject) search.get("interval");
					ayas = (JSONObject) search.get("ayas");
					start = (Long) interval.get("start");
					end = (Long) interval.get("end");
					total = (Long) interval.get("total");
					
					System.out.println(dataNum++);
					for (int i = (int) start; i <= (int) end; i++) {
						JSONObject numOfAya = (JSONObject) ayas.get("" + i);
						JSONObject aya = (JSONObject) numOfAya.get("aya");
						allAyas += "<br/>( " + i + " ) : " + aya.get("text")
								+ "<br/>";

					}
				}
				
				write();
			}
			wordIndex++;
		}
	}

	public static void write() throws IOException {
		// JOptionPane.showMessageDialog(null, allAyas);
		File file = new File("alfanousResult["+wordIndex+"].html");
		// creates the file
		file.createNewFile();
		// creates a FileWriter Object

		FileWriter writer = new FileWriter(file);
		// Writes the content to the file
		String firstBody = "<!DOCTYPE html>" + "<html>" + "<head>"
				+ "<title>ROIC | Dalel Quraany</title>" + "<style>"
				+ "	.match{" + "	color:red;" + "	}" + "</style>" + "</head>"
				+ "<body>";
		String lastBody = " </body></html>";
		writer.write(firstBody + allAyas + lastBody);
		writer.flush();
		writer.close();
	}
}
