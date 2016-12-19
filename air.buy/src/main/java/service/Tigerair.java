package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
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
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.DateUtil;

public class Tigerair {
	private static String url = "https://booking.tigerair.com/Search.aspx";

	public static void main(String[] args) throws Exception {
		String outboundDate = "2017-01";
		String returnDate = "2017-02";
		String from = "TPE";
		String to = "NRT";

		String structure = "RoundTrip";
		// String structure = "OneWay";
		Tigerair t = new Tigerair();
		List<HashMap<String, String>> moneyList = t.getMoney(outboundDate,
				returnDate, from, to, structure);
	}

	/* Error */
	public List<HashMap<String, String>> getMoney(String outboundDate,
			String returnDate, String from, String to, String structure)
			throws java.io.IOException {
		System.out.println("tigerair select start !");
		File saveFile = new File("D:\\Data.txt");
		FileWriter fwriter = new FileWriter(saveFile);
		HashMap<String, String> outboundMap = new HashMap<String, String>();
		HashMap<String, String> returnMap = new HashMap<String, String>();
		List<HashMap<String, String>> moneyList = new ArrayList<HashMap<String, String>>();
		// 建立HttpClient實體.
		HttpClient client = new HttpClient();
		client.setTimeout(5000);

		// 建立PostMethod實體, 並指派網址
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("ache-control", "no-cache");
		post.setRequestHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.126 Safari/535.1");
		post.setRequestHeader("content-type",
				"application/x-www-form-urlencoded");
		// 建立NameValuePair陣列�儲欲傳送的資料, 對照為 (名稱, 內容)
		NameValuePair data[] = {
				new NameValuePair(
						"ControlGroupSearchView$AvailabilitySearchInputSearchView$RadioButtonMarketStructure",
						structure),
				new NameValuePair(
						"ControlGroupSearchView_AvailabilitySearchInputSearchVieworiginStation1",
						from),
				new NameValuePair(
						"ControlGroupSearchView_AvailabilitySearchInputSearchViewdestinationStation1",
						to),
				new NameValuePair(
						"ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketOrigin1",
						from),
				new NameValuePair(
						"ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketDestination1",
						to),
				new NameValuePair(
						"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_ADT",
						"1"),
				new NameValuePair(
						"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_CHD",
						"0"),
				new NameValuePair(
						"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_INFANT",
						"0"),
				new NameValuePair(
						"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDateRange1",
						"1|1"),
				new NameValuePair(
						"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDay1",
						"01"),
				new NameValuePair(
						"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketMonth1",
						outboundDate),
				new NameValuePair(
						"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDay2",
						"01"),
				new NameValuePair(
						"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketMonth2",
						returnDate),
				new NameValuePair(
						"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDateRange2",
						"1|1"),
				new NameValuePair("ControlGroupSearchView$ButtonSubmit",
						"\u7372\u53D6\u822A\u73ED"),
				new NameValuePair("hiddendAdultSelection", "1"),
				new NameValuePair("hiddendChildSelection", "0"),
				new NameValuePair(
						"__VIEWSTATE",
						"/wEPDwUBMGQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFU0NvbnRyb2xHcm91cEhlYWRlclNlbGVjdEJ1bmRsZVZpZXckTG9naW5IZWFkZXJWaWV3U2VsZWN0QnVuZGxlVmlldyRjaGtCb3hSZW1lbWJlck1leFenyyjSor9G1yG/r5oBpIOJo+o=") };
		// 將NameValuePair陣列設置到請求內容中

		try {
			byte responseBody[] = null;
			post.setRequestBody(data);

			// 返回狀態值.
			int statusCode = client.executeMethod(post);
			System.out.println(statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println((new StringBuilder("Method failed: "))
						.append(post.getStatusLine()).toString());
				if (statusCode == 301 || statusCode == 302) {
					URI uri = null;
					uri = new URI(
							"https://booking.tigerair.com/SelectFlights.aspx",
							false);
					post.setURI(uri);
					client.executeMethod(post);
				}
			}

			 responseBody = post.getResponseBody();
			outboundMap = fromHtml(outboundMap, responseBody,outboundDate);
			returnMap = toHtml(returnMap, responseBody,returnDate);
			for (int i = 7; i <= 30; i++) {
				String date = addZero(i);
				GetMethod method = new GetMethod(
						(new StringBuilder(
								"https://booking.tigerair.com/SelectFlights.aspx?segment=1&date="))
								.append(outboundDate).append("-").append(date)
								.toString());
				method.getParams().setParameter("http.method.retry-handler",
						new DefaultHttpMethodRetryHandler(3, false));
				int getStatusCode = client.executeMethod(method);
				if (getStatusCode != 200) {
					System.err.println((new StringBuilder("Method failed: "))
							.append(method.getStatusLine()).toString());
				}
				byte getResponseBody[] = method.getResponseBody();
				fwriter.write(new String(getResponseBody));
				outboundMap = fromHtml(outboundMap, getResponseBody,outboundDate);
				if (structure.equals("RoundTrip")) {
					System.out.println("回程");
					GetMethod methodtReturn = new GetMethod(
							(new StringBuilder(
									"https://booking.tigerair.com/SelectFlights.aspx?segment=2&date="))
									.append(returnDate).append("-")
									.append(date).toString());
					methodtReturn.getParams().setParameter(
							"http.method.retry-handler",
							new DefaultHttpMethodRetryHandler(3, false));
					int gettReturnStatusCode = client
							.executeMethod(methodtReturn);
					if (gettReturnStatusCode != 200)
						System.err
								.println((new StringBuilder("Method failed: "))
										.append(methodtReturn.getStatusLine())
										.toString());
					byte getReturnResponseBody[] = methodtReturn
							.getResponseBody();
					returnMap = toHtml(returnMap, getReturnResponseBody,returnDate);
				}
				i += 6;
			}

		} catch (HttpException httpexc) {
			System.err.println("Fatal protocol violation: "
					+ httpexc.getMessage());
			httpexc.printStackTrace();
		} catch (IOException ioexc) {
			System.err.println("Fatal transport error: " + ioexc.getMessage());
			ioexc.printStackTrace();
		} finally {

			// ** 無論如何都必須釋放連接.
			post.releaseConnection();
		}
		moneyList.add(outboundMap);
		moneyList.add(returnMap);
		fwriter.close();
		System.out.println("tigerair select end !");
		return moneyList;

	}

	private HashMap<String, String> fromHtml(
			HashMap<String, String> outboundMap, byte[] responseBody, String outboundDate) {
		Document doc = Jsoup.parse(new String(responseBody));

		Elements link = doc.select("ul[id=lfMarket1] li");

		Elements name = doc.select("h3");
		if (!link.isEmpty()) {
	
			System.out.println(name.first().text());
			for (Element e : link) {
				if(!e.select("a").attr("data-date").contains(outboundDate)){
					continue;
				}
				if (!e.select("a").attr("data-date").isEmpty()) {
					System.out.println(e.select("a").attr("data-date") + ":"
							+ e.select("span").text().replace(",", "").replace(".00", ""));
					outboundMap.put(e.select("a").attr("data-date"),
							e.select("span").text().replace(",", "").replace(".00", ""));
				}
			}
		}
		return outboundMap;
	}

	private HashMap<String, String> toHtml(HashMap<String, String> returnMap,
			byte[] responseBody,String returnDate) {
		Document doc = Jsoup.parse(new String(responseBody));
		Elements link2 = doc.select("ul[id=lfMarket2] li");
	
		if (!link2.isEmpty()) {
			for (Element e : link2) {
				
				if (!e.select("a").attr("data-date").isEmpty()) {
					if(!e.select("a").attr("data-date").contains(returnDate)){
						continue;
					}
					System.out.println(e.select("a").attr("data-date") + ":"
							+ e.select("span").text().replace(".00", ""));
					returnMap.put(e.select("a").attr("data-date"),
							e.select("span").text().replace(",", "").replace(".00", ""));
				}
			}
		}
		return returnMap;
	}

	private static String addZero(int date) {
		String formatStr = "%02d";

		String formatAns = String.format(formatStr,
				new Object[] { Integer.valueOf(date) });
		return formatAns;
	}
}
