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
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.DateUtil;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Scoot {
	private static String url = "http://makeabooking.flyscoot.com/Book";

	public static void main(String[] args) throws Exception {
		String outboundDate = "2017-01";
		String returnDate = "2017-03";
		String from = "TPE";
		String to = "NRT";

		//String structure = "Return";
		 String structure = "Oneway";
		Scoot t = new Scoot();
		List<HashMap<String, String>> moneyList = t.getMoney(outboundDate,
				returnDate, from, to, structure);
	}

	/* Error */
	public List<HashMap<String, String>> getMoney(String outboundDate,
			String returnDate, String from, String to, String structure)
			throws java.io.IOException, ParseException {
		System.out.println("scoot select start !");
		File saveFile = new File("D:\\Data.txt");
		FileWriter fwriter = new FileWriter(saveFile);
		HashMap<String, String> outboundMap = new HashMap<String, String>();
		HashMap<String, String> returnMap = new HashMap<String, String>();
		List<HashMap<String, String>> moneyList = new ArrayList<HashMap<String, String>>();
		try {
			for (int i = 16; i <= 17; i++) {
				Document doc = Jsoup
						.connect(url)
						.ignoreContentType(true)
						.followRedirects(true)
						.userAgent(
								"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
						.referrer(
								"view-source:http://makeabooking.flyscoot.com/Book/Flight")
						.data("revAvailabilitySearch.SearchInfo.Direction",structure)
						.data("revAvailabilitySearch.SearchInfo.SearchStations[0].DepartureStationCode",
								from)
						.data("revAvailabilitySearch.SearchInfo.SearchStations[0].ArrivalStationCode",
								to)
						.data("revAvailabilitySearch.SearchInfo.SearchStations[0].DepartureDate",
								outboundDate + "-" + i)
						.data("revAvailabilitySearch.SearchInfo.SearchStations[1].DepartureDate",
								returnDate + "-" + i)
						.data("revAvailabilitySearch.SearchInfo.AdultCount:",
								"1")
						.data("revAvailabilitySearch.SearchInfo.ChildrenCount:",
								"0")
						.data("revAvailabilitySearch.SearchInfo.InfantCount:",
								"0")
						.data("revAvailabilitySearch.SearchInfo.PromoCode:", "")
						.timeout(10000).post();

				fwriter.write(doc.toString());

				Elements link = doc.select("div[class=chart__wrapper] li");
				if (!link.isEmpty()) {
					String origin = "";
					for (Element e : link) {
						String dataOrigin = e.select("p").last().text()
								.split("from")[1];

						if (dataOrigin.equals(origin) || origin.isEmpty()) {
							if(!DateUtil.dateToString(DateUtil.StringToDate(e.attr("data-date"))).contains(outboundDate)){
								continue;
							}
							if(!"No flights".equals(e.select("p[class=price]").text())){
								outboundMap.put(DateUtil.dateToString(DateUtil.StringToDate(e.attr("data-date"))),	e.select("p[class=price]").text().replace(",", ""));
							}
								System.out.println(DateUtil.dateToString(DateUtil.StringToDate(e.attr("data-date")))+ ":"+ e.select("p[class=price]").text().replace(",", ""));
							origin = dataOrigin;
						} else {
							if(!DateUtil.dateToString(DateUtil.StringToDate(e.attr("data-date"))).contains(outboundDate)){
								continue;
							}
							if(!"No flights".equals(e.select("p[class=price]").text())){
								returnMap.put(DateUtil.dateToString(DateUtil.StringToDate(e.attr("data-date"))),	e.select("p[class=price]").text().replace(",", ""));
							}
							System.out.println("回程："+ DateUtil.dateToString(DateUtil.StringToDate(e.attr("data-date")))+ ":"+ e.select("p[class=price]").text().replace(",", ""));
						}
					}

				}
			}
		} catch (HttpException httpexc) {
			System.err.println("Fatal protocol violation: "
					+ httpexc.getMessage());
			httpexc.printStackTrace();
		} catch (IOException ioexc) {
			System.err.println("Fatal transport error: " + ioexc.getMessage());
			ioexc.printStackTrace();
		} finally {

		}
		moneyList.add(outboundMap);
		moneyList.add(returnMap);
		fwriter.close();
		System.out.println("scoot select end !");
		return moneyList;

	}




}
