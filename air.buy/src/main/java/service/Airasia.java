package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.DateUtil;

public class Airasia {
	Logger logger=Logger.getLogger(Airasia.class);
	private static String url = "https://booking.airasia.com/Flight/Select?c=true&s=false&r=true";
	public static void main(String[] args) throws IOException, ParseException {
	
		String outboundDate = "2017-01";
		String returnDate = "2017-02";
		String from = "TPE";
		String to = "SYD";

		String structure = "RoundTrip";
		Airasia a = new Airasia();
		List<HashMap<String, String>> moneyList = a.getMoney(outboundDate,
				returnDate, from, to, structure);

	}
	
	/* Error */
	public List<HashMap<String, String>> getMoney(String outboundDate,
			String returnDate, String from, String to, String structure)
			throws java.io.IOException, ParseException {
		logger.info("Airasia select start !");
		HashMap<String, String> outboundMap = new HashMap<String, String>();
		HashMap<String, String> returnMap = new HashMap<String, String>();
		List<HashMap<String, String>> moneyList = new ArrayList<HashMap<String, String>>();
		
		File saveFile = new File("D:\\Data.txt");
		FileWriter fwriter = new FileWriter(saveFile);
		HttpClient client = new HttpClient();
		
		for (int i = 10; i <= 22; i=i+12) {
			String retrunarg = "";
			if ("RoundTrip".equals(structure)) {
				retrunarg = "&dd2=" + returnDate+"-"+i;
			}
			GetMethod get = new GetMethod(url + "&o1=" + from + "&d1=" + to
					+ "&dd1=" + outboundDate+"-"+i + retrunarg);

			byte responseBody[] = null;
			get.getParams().setParameter("http.method.retry-handler",
					new DefaultHttpMethodRetryHandler(3, false));
			int getStatusCode = client.executeMethod(get);
			if (getStatusCode != 200) {
				System.err.println((new StringBuilder("Method failed: "))
						.append(get.getStatusLine()).toString());
			}
			System.out.println(get.getURI());
			responseBody = get.getResponseBody();
			fwriter.write(new String(responseBody));
			// System.out.println(new String(responseBody));

			Document doc = Jsoup.parse(new String(responseBody));
			Elements link = doc.select("div[class=low-fare-cal]");
			if (!link.isEmpty()) {
				Element fromElement = link.first();
				outboundMap = getHtml(fromElement, outboundDate,outboundMap);
				if ("RoundTrip".equals(structure)) {
					System.out.println("Airasia RoundTrip !");
					Element toElement = link.last();
					returnMap = getHtml(toElement, returnDate,returnMap);
				}

			}
			
		}
		moneyList.add(outboundMap);
		moneyList.add(returnMap);
		logger.info("Airasia select end !");
		return moneyList;
		
		
	}
	
	
	private HashMap<String, String> getHtml(Element element,String year,HashMap<String, String> map ) {
		
		Elements fromElements = element.select("div[class=low-fare-cal-day]");
		for (Element e : fromElements) {
			String date = e.select("input").attr("value");
			
			String price = e.select("div[class=low-fare-cal-day-amount]").text();
			if(!date.contains(year)){
				continue;
			}
			logger.info("Airasia "+date +":" + price.split(" ")[1] + " "+ price.split(" ")[0].replace(".00", "").replace(",", ""));
			map.put(date,price.split(" ")[1] + " "+ price.split(" ")[0].replace(".00", "").replace(",", ""));
		}
		return map;
		
	}

}
