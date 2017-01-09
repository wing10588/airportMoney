package service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import util.DateUtil;

public class Vanilla {
	private static String url = "https://www.vanilla-air.com/en/booking/#/flight-select/?";
	static Logger logger=Logger.getLogger(Vanilla.class);
	public static void main(String[] args) throws Exception {
		String outboundDate = "2017-05-01";
		String returnDate = "2017-06-01";
		String from = "TPE";
		String to = "NRT";

		String structure = "RT";
		//String structure = "OW";
		Vanilla t = new Vanilla();
		List<HashMap<String, String>> moneyList = t.getMoney(outboundDate,
				returnDate, from, to, structure);
	
      
		
/*		DesiredCapabilities options = new DesiredCapabilities();
		System.setProperty("phantomjs.binary.path", "D:/phantomjs.exe");  
	
		options.setJavascriptEnabled(true);         
	    // the website i am scraping uses ssl, but I dont know what version
	    options.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, new String[] {
	          "--ssl-protocol=any"
	      });

	    PhantomJSDriver driver = null;
		for (int j = 1; j <= 32; j++) {
			String date = addZero(j);
			System.out.println(date);
			  driver = new PhantomJSDriver(options);
			driver.get("https://www.vanilla-air.com/tw/booking/#/flight-select/?tripType=RT&origin=TPE&destination=KIX&outboundDate=2017-01-"+date+"&returnDate=2017-02-"+date+"&adults=1&children=0&infants=0&promoCode=&mode=searchResultInter");
 
			// System.out.println("test:"+driver.getPageSource());
				print(driver);
			
			
			j=+6;
			
		}
		driver.close();
		driver.quit();*/
	   
	}

	public static void print(PhantomJSDriver driver){
		for (int i = 0; i <= 7; i++) {
			List<WebElement> divInfos = driver.findElements(By.cssSelector("li.libox"+i));
			for (WebElement divInfo : divInfos) {
				WebElement day = divInfo.findElement(By.className("ng-binding"));
				WebElement price = divInfo.findElement(By.className("price"));
				System.out.println("test:" + day.getText() + ":" + price.getText());
				
			}
			logger.info("--------");
		}
    }
	public List<HashMap<String, String>> getMoney(String outboundDate,
			String returnDate, String from, String to, String structure)
			throws java.io.IOException, ParseException, JSONException {
		HashMap<String, String> outboundMap = new HashMap<String, String>();
		HashMap<String, String> returnMap = new HashMap<String, String>();
		List<HashMap<String, String>> moneyList = new ArrayList<HashMap<String, String>>();
	
		
	
			outboundMap = getDate(from, to,outboundDate,from);

			if("RT".equals(structure)) {
				
				returnMap = getDate(to, from,returnDate,from);
			}
					
			

		

	
		moneyList.add(outboundMap);
		moneyList.add(returnMap);
		return moneyList;

	}

	private HashMap<String, String> getDate( String from, String to,
			String outboundDate,String fromRt) throws HttpException, IOException, JSONException {
		HttpClient client1 = new HttpClient();
		GetMethod get1 = null ;
		byte responseJsonBody[] = null;
		HashMap<String, String> map = new HashMap<String, String>();
	
		String cur = currency(fromRt);
			 get1 = new GetMethod(
					(new StringBuilder(
							"https://www.vanilla-air.com/api/booking/flight-fare/list.json?"))
							.append("__ts=")
							.append(new Date().getTime())
							.append("&adultCount=1&childCount=0&couponCode=&currency="+cur)
							.append("&destination=")
							.append(to)
							.append("&infantCount=0&isMultiFlight=true")
							.append("&origin=")
							.append(from)

							.append("&targetMonth=")
							.append(outboundDate.replace("-", "").substring(0,
									6))

							.append("&version=1.0&channel=1").toString()); 

			
			get1.getParams().setParameter("http.method.retry-handler",
					new DefaultHttpMethodRetryHandler(3, false));
			int getStatusJsonCode = client1.executeMethod(get1);

			if (getStatusJsonCode != 200) {
				logger.error((new StringBuilder("Method failed: "))
						.append(get1.getStatusLine()).toString());
				
			}
		
		
			responseJsonBody = get1.getResponseBody();

		//System.out.println(response);
		JSONObject json = null;

		json = new JSONObject(new String(get1.getResponseBody()));
	
		JSONArray array = json.getJSONArray("Result");

		if( array.getJSONObject(0).getString("Value") != "null"){
		JSONObject fareListOfDay = array.getJSONObject(0).getJSONObject("FareListOfDay");
		Iterator<?> keys = fareListOfDay.keys();
		while(keys.hasNext() ) {
		    String key = (String)keys.next();
		    if(Integer.valueOf(fareListOfDay.getJSONObject(key.toString()).getString("LowestFare"))== 0 || fareListOfDay.getJSONObject(key.toString()).getString("LowestFare")=="Full"){
		    	continue;
		    }
		    map.put(fareListOfDay.getJSONObject(key.toString()).getString("FlightDate"),cur+" "+ fareListOfDay.getJSONObject(key.toString()).getString("LowestFare"));
		    logger.info("香草 :"+fareListOfDay.getJSONObject(key.toString()).getString("FlightDate")+":"+cur+" "+ fareListOfDay.getJSONObject(key.toString()).getString("LowestFare"));
		}
		}
		return map;
	}

	private static String currency (String from) {
		
		if("TPE".equals(from) || "KHH".equals(from) ){
			return "TWD";
		}else{
			return "JPY";
		}
		
		
	}
}
