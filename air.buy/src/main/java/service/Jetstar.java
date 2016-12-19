package service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.DateUtil;

public class Jetstar {
	private static String url = "https://booknow.jetstar.com/LowFareFinder.aspx?culture=zh-HK";

	public static void main(String[] args) throws Exception {
		String outboundDate = "2017-01";
		String returnDate = "2017-02";
		String from = "TPE";
		String to = "NRT";

		String structure = "RoundTrip";
		Jetstar t = new Jetstar();
		List<HashMap<String, String>> moneyList = t.getMoney(outboundDate,
				returnDate, from, to, structure);
	}

	/* Error */
	public List<HashMap<String, String>> getMoney(String outboundDate,
			String returnDate, String from, String to, String structure)
			throws java.io.IOException, ParseException {
		System.out.println("jetstar select start !");
		HashMap<String, String> outboundMap = new HashMap<String, String>();
		HashMap<String, String> returnMap = new HashMap<String, String>();
		List<HashMap<String, String>> moneyList = new ArrayList<HashMap<String, String>>();
		for (int i = 1; i <= 30; i++) {
			HttpClient client = new HttpClient();
			String date = addZero(i);

			GetMethod get = new GetMethod(
					(new StringBuilder(url))
							.append("&RadioButtonMarketStructure=")
							.append(structure)
							.append("&Origin1=")
							.append(from)
							.append("&Destination1=")
							.append(to)
							.append("&Day1=")
							.append(date)
							.append("&MonthYear1=")
							.append(outboundDate)
							.append("&Origin2=")
							.append(to)
							.append("&Destination2=")
							.append(from)
							.append("&Day2=")
							.append(date)
							.append("&MonthYear2=")
							.append(returnDate)
							.append("&ADT=1&CHD=0&INF=0&AutoSubmit=Y&ControlGroupCalendarSearchView%24AvailabilitySearchInputCalendarSearchView%24DropDownListCurrency=")
							.toString());
			try {
				byte responseBody[] = null;
				get.getParams().setParameter("http.method.retry-handler",
						new DefaultHttpMethodRetryHandler(3, false));
				int getStatusCode = client.executeMethod(get);
				if (getStatusCode != 200) {
					System.err.println((new StringBuilder("Method failed: "))
							.append(get.getStatusLine()).toString());
				}
				responseBody = get.getResponseBody();

				Document doc = Jsoup.parse(new String(responseBody));
				Elements link = doc.select("div[class=low-fare-selector] li");
				if (!link.isEmpty()) {
					String origin = "";
					String cueenrcy = doc.select("div[id=DataExposed] CURRENCYCODE").first().text();
					for (Iterator iterator = link.iterator(); iterator
							.hasNext();) {
						Element e = (Element) iterator.next();
						
						if (e.attr("data-origin").equals(origin)|| origin.isEmpty()) {
							if(e.attr("data-price").isEmpty()){
								continue;
							}
							if(!DateUtil.dateToString(DateUtil.StringToDate(e.attr("data-date"))).contains(outboundDate)){
									continue;
								}
							outboundMap.put(DateUtil.dateToString(DateUtil.StringToDate(e.attr("data-date"))),	cueenrcy +" "+ e.attr("data-price").replace(".00", ""));
							System.out.println((new StringBuilder(
									"去程"))
									.append(e.attr("data-origin")).append(":")
									.append(DateUtil.dateToString(DateUtil.StringToDate(e.attr("data-date")))).append(":")
									.append(cueenrcy +e.select("li").attr("data-price").replace(".00", ""))
									.toString());
							origin = e.attr("data-origin");
						} else {
							if(e.attr("data-price").isEmpty()){
								continue;
							}
							if(!DateUtil.dateToString(DateUtil.StringToDate(e.attr("data-date"))).contains(returnDate)){
								continue;
							}
							returnMap.put(DateUtil.dateToString(DateUtil.StringToDate(e.attr("data-date"))),cueenrcy +" "+e.attr("data-price").replace(".00", ""));
							System.out.println((new StringBuilder(
									"回程"))
									.append(e.attr("data-origin")).append(":")
									.append(DateUtil.dateToString(DateUtil.StringToDate(e.attr("data-date")))).append(":")
									.append(cueenrcy +e.select("li").attr("data-price").replace(".00", ""))
									.toString());
						}
					}

				}

			} catch (HttpException httpexc) {
				System.err.println("Fatal protocol violation: "
						+ httpexc.getMessage());
				httpexc.printStackTrace();
			} catch (IOException ioexc) {
				System.err.println("Fatal transport error: "
						+ ioexc.getMessage());
				ioexc.printStackTrace();
			} finally {

			}
		
			i += 10;

		}
		moneyList.add(outboundMap);
		moneyList.add(returnMap);
		System.out.println("jetstar select end !");
		return moneyList;

	}

	private static String addZero(int date) {
		String formatStr = "%02d";

		String formatAns = String.format(formatStr,
				new Object[] { Integer.valueOf(date) });
		return formatAns;
	}
}
