package service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.DateUtil;

public class Vanilla {
	private static String url = "https://www.vanilla-air.com/en/booking/#/flight-select/?";

	public static void main(String[] args) throws Exception {
		String outboundDate = "2017-01-01";
		String returnDate = "2017-02-01";
		String from = "TPE";
		String to = "NRT";

		String structure = "RT";
		//String structure = "OW";
		Vanilla t = new Vanilla();
		List<HashMap<String, String>> moneyList = t.getMoney(outboundDate,
				returnDate, from, to, structure);
	}


	public List<HashMap<String, String>> getMoney(String outboundDate,
			String returnDate, String from, String to, String structure)
			throws java.io.IOException, ParseException, JSONException {
		HashMap<String, String> outboundMap = new HashMap<String, String>();
		HashMap<String, String> returnMap = new HashMap<String, String>();
		List<HashMap<String, String>> moneyList = new ArrayList<HashMap<String, String>>();
		File saveFile = new File("D:\\Data.txt");
		FileWriter fwriter = new FileWriter(saveFile);
		HttpClient client = new HttpClient();

		
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(Long.valueOf("1481079697187"));
		// here's how to get the minutes
		final int minutes = cal.get(Calendar.MINUTE);
		// and here's how to get the String representation
		final String timeString =
		    new SimpleDateFormat("HH:mm:ss:SSS").format(cal.getTime());
		System.out.println(timeString);
		
		GetMethod get = new GetMethod(
				(new StringBuilder(url))
						.append("tripType=")
						.append(structure)
						.append("&origin=")
						.append(from)
						.append("&destination=")
						.append(to)
						.append("&outboundDate=")
						.append(outboundDate)
						.append("&returnDate=")
						.append(returnDate)

						.append("&adults=1&children=0&infants=0&promoCode=&mode=searchResultInter")
						.toString());
		try {
			byte responseBody[] = null;
			get.setRequestHeader("Cookie","flightSearch=");
			get.getParams().setParameter("http.method.retry-handler",
					new DefaultHttpMethodRetryHandler(3, false));
			Date date = new Date();
			Long dateLong = date.getTime();
			int getStatusCode = client.executeMethod(get);
			if (getStatusCode != 200) {
				System.err.println((new StringBuilder("Method failed: "))
						.append(get.getStatusLine()).toString());
			}
			responseBody = get.getResponseBody();

			fwriter.write(new String(responseBody));

			
			outboundMap = getDate(dateLong,from, to,outboundDate);
			System.out.println(structure);
			if("RT".equals(structure)) {
				 date = new Date();
				 dateLong = date.getTime();
				returnMap = getDate(dateLong, to, from,returnDate);
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

		fwriter.close();
		moneyList.add(outboundMap);
		moneyList.add(returnMap);
		return moneyList;

	}

	private HashMap<String, String> getDate(Long date, String from, String to,
			String outboundDate) throws JSONException, HttpException,
			IOException {
		HttpClient client1 = new HttpClient();
		GetMethod get1 = null ;
		byte responseJsonBody[] = null;
		HashMap<String, String> map = new HashMap<String, String>();
		int i =0;		
		while (i <=50) {
			 get1 = new GetMethod(
					(new StringBuilder(
							"https://www.vanilla-air.com/api/booking/flight-fare/list.json?"))
							.append("__ts=")
							.append(date)
							.append("&adultCount=1&childCount=0&couponCode=&currency=TWD")
							.append("&destination=")
							.append(to)
							.append("&infantCount=0&isMultiFlight=true")
							.append("&origin=")
							.append(from)

							.append("&targetMonth=")
							.append(outboundDate.replace("-", "").substring(0,
									6))

							.append("&version=1.0").toString());

			
			get1.getParams().setParameter("http.method.retry-handler",
					new DefaultHttpMethodRetryHandler(3, false));
			int getStatusJsonCode = client1.executeMethod(get1);

			if (getStatusJsonCode != 200) {
				System.err.println((new StringBuilder("Method failed: "))
						.append(get1.getStatusLine()).toString());
				System.err.println(new String(get1.getResponseBody()));
			}else{
				break;
			}
		}
		System.out.println(get1.getURI());
		responseJsonBody = get1.getResponseBody();

		//System.out.println(response);
		JSONObject json = new JSONObject(new String(get1.getResponseBody()));
		JSONArray array = json.getJSONArray("Result");
		
		JSONObject fareListOfDay = array.getJSONObject(0).getJSONObject("FareListOfDay");
		Iterator<?> keys = fareListOfDay.keys();
		while(keys.hasNext() ) {
		    String key = (String)keys.next();
		    if(fareListOfDay.getJSONObject(key.toString()).getString("LowestFare")=="0" || fareListOfDay.getJSONObject(key.toString()).getString("LowestFare")=="Full"){
		    	continue;
		    }
		    map.put(fareListOfDay.getJSONObject(key.toString()).getString("FlightDate"), fareListOfDay.getJSONObject(key.toString()).getString("LowestFare"));
			System.out.println(fareListOfDay.getJSONObject(key.toString()).getString("FlightDate")+":"+fareListOfDay.getJSONObject(key.toString()).getString("LowestFare"));
		}
		return map;
	}

	private static String addZero(int date) {
		String formatStr = "%02d";

		String formatAns = String.format(formatStr,
				new Object[] { Integer.valueOf(date) });
		return formatAns;
	}
}
