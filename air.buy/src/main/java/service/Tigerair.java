package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

import util.DateUtil;
import util.ProtocolOverridingSSLSocketFactory;

public class Tigerair {
	private static String url = "https://booking.tigerair.com/Search.aspx?MarketStructure=RoundTrip&culture=en-GB&selOrigin=TPE&selDest=OKA&ControlGroupSearchView%24AvailabilitySearchInputSearchView%24DropDownListMarketDateRange1=1%7C1&ControlGroupSearchView%24AvailabilitySearchInputSearchView%24DropDownListMarketDateRange2=1%7C1&ControlGroupSearchView%24AvailabilitySearchInputSearchView%24DropDownListMarketDay1=08&ControlGroupSearchView%24AvailabilitySearchInputSearchView%24DropDownListMarketDay2=15&ControlGroupSearchView%24AvailabilitySearchInputSearchView%24DropDownListMarketMonth1=2017-01&ControlGroupSearchView%24AvailabilitySearchInputSearchView%24DropDownListMarketMonth2=2017-01&ControlGroupSearchView%24AvailabilitySearchInputSearchView%24DropDownListPassengerType_ADT=1&ControlGroupSearchView%24AvailabilitySearchInputSearchView%24DropDownListPassengerType_CHD=0&ControlGroupSearchView%24AvailabilitySearchInputSearchView%24DropDownListPassengerType_INFANT=0&ControlGroupSearchView%24AvailabilitySearchInputSearchView%24RadioButtonMarketStructure=RoundTrip&ControlGroupSearchView%24AvailabilitySearchInputSearchView%24TextBoxMarketDestination1=OKA&ControlGroupSearchView%24AvailabilitySearchInputSearchView%24TextBoxMarketOrigin1=TPE&ControlGroupSearchView%24ButtonSubmit=Get+Flights&ControlGroupSearchView_AvailabilitySearchInputSearchViewdestinationStation1=OKA&ControlGroupSearchView_AvailabilitySearchInputSearchVieworiginStation1=TPE&__VIEWSTATE=%2fwEPDwUBMGRk7p3dDtvn3PMYYJ9u4RznKUiVx98%3d&date_picker=2017-01-08&date_picker=2017-01-15&hiddendAdultSelection=1&hiddendChildSelection=0&utm_source=price_watch&utm_medium=email&utm_content=&utm_campaign=update&b_cer=";

	public static void main(String[] args) throws Exception {
		String outboundDate = "2017-01-04";
		String returnDate = "2017-02-01";
		String from = "TPE";
		String to = "NRT";

		String structure = "return";
		// String structure = "OneWay";
		Tigerair t = new Tigerair();
		System.setProperty("https.protocols", "TLSv1.2");
	/*	List<HashMap<String, String>> moneyList = t.getMoney(outboundDate,
				returnDate, from, to, structure);*/
	/*	 StringBuilder result = new StringBuilder();
		 URL url = new URL("http://pricewatch.tigerair.com/deeplink?result_uuid=63cbfe740ec747d0a4ad47d6eda6ae25&deeplink=https%3A%2F%2Fbooking.tigerair.com%2FTigerDeepLink.aspx%3Forigin%3DTPE%26utm_campaign%3Dupdate%26retDate%3D2017-01-15%26dest%3DOKA%26utm_source%3Dprice_watch%26culture%3Den-GB%26utm_medium%3Demail%26ms%3DRoundTrip%26depDate%3D2017-01-08%26psgr%3D1_0_0%26dlType%3Dfltsrch&partner_id=tigerair&uid=tmcmU7f1L_ZaoaF47XQp67&culture_code=en-GB");
	      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	      conn.setRequestMethod("GET");
	      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	      String line;
	      while ((line = rd.readLine()) != null) {
	         result.append(line);
	      }
	      System.out.println(result.toString());
	      rd.close();*/
		
		URI uri = URI.create("https://booking.tigerair.com/TigerDeepLink.aspx?dlType=fltsrch&culture=zh-tw&ms=RoundTrip&psgr=1_0_0&depDate=2017-01-04&retDate=2017-01-07&origin=TPE&dest=MFM&icn=find-flights-btn&ici=real-deals-tpemfm-twzh");

	
		Document doc = Jsoup
				.connect("https://booking.tigerair.com/TigerDeepLink.aspx?dlType=fltsrch&culture=zh-tw&ms=RoundTrip&psgr=1_0_0&depDate=2017-01-04&retDate=2017-01-07&origin=TPE&dest=MFM&icn=find-flights-btn&ici=real-deals-tpemfm-twzh")
				//.ignoreContentType(true)
				.followRedirects(true)
				.userAgent(
						"Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
				.referrer(
						"view-source:https://booking.tigerair.com/TigerDeepLink.aspx?dlType=fltsrch&culture=zh-tw&ms=RoundTrip&psgr=1_0_0&depDate=2017-01-04&retDate=2017-01-07&origin=TPE&dest=MFM&icn=find-flights-btn&ici=real-deals-tpemfm-twzh")
						.data(
								"ControlGroupSearchView$AvailabilitySearchInputSearchView$RadioButtonMarketStructure",
								structure)
								.data(
								"ControlGroupSearchView_AvailabilitySearchInputSearchVieworiginStation1",
								from)
								.data(
								"ControlGroupSearchView_AvailabilitySearchInputSearchViewdestinationStation1",
								to)
								.data(
								"ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketOrigin1",
								from)
								.data(
								"ControlGroupSearchView$AvailabilitySearchInputSearchView$TextBoxMarketDestination1",
								to)
								.data(
								"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_ADT",
								"1")
								.data(
								"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_CHD",
								"0")
								.data(
								"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListPassengerType_INFANT",
								"0")
								.data(
								"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDateRange1",
								"1|1")
								.data(
								"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDay1",
								"01")
								.data(
								"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketMonth1",
								outboundDate)
								.data(
								"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDay2",
								"01")
								.data(
								"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketMonth2",
								returnDate)
								.data(
								"ControlGroupSearchView$AvailabilitySearchInputSearchView$DropDownListMarketDateRange2",
								"1|1")
								.data("ControlGroupSearchView$ButtonSubmit","\u7372\u53D6\u822A\u73ED")
								.data("hiddendAdultSelection", "1")
										.data("hiddendChildSelection", "0")
												.data(
								"__VIEWSTATE",
								"/wEPDwUBMGQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgEFU0NvbnRyb2xHcm91cEhlYWRlclNlbGVjdEJ1bmRsZVZpZXckTG9naW5IZWFkZXJWaWV3U2VsZWN0QnVuZGxlVmlldyRjaGtCb3hSZW1lbWJlck1leFenyyjSor9G1yG/r5oBpIOJo+o=") 
				.timeout(10000).get();
		for (Element refresh : doc.select("html head meta[http-equiv=refresh]")) {
			System.out.println("2");
	        Matcher m = Pattern.compile("(?si)\\d+;\\s+url=(.+)|\\d+")
	                           .matcher(refresh.attr("content"));

	        // find the first one that is valid
	        if (m.matches()) {
	            if (m.group(1) != null)
	            	try{
	                doc = Jsoup.connect(uri.resolve(m.group(1)).toString()).post();
	            	}catch (Exception httpexc) {
	        			System.err.println("Fatal protocol violation: "
	        					+ httpexc.getMessage());
	        			httpexc.printStackTrace();
	        		}
	            break;
	        }
	    }
	
		System.out.println(doc.toString());
		/*File saveFile = new File("D:\\Data.txt");
		FileWriter fwriter = new FileWriter(saveFile);
		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getCache().setMaxSize(0);
		webClient.getOptions().setSSLClientProtocols(new String[] { "TLSv1.2", "TLSv1.1", "TLSv1" });
		final HtmlPage page1 = webClient
				.getPage("https://booking.tigerair.com/TigerDeepLink.aspx?dlType=fltsrch&culture=zh-tw&ms=RoundTrip&psgr=1_0_0&depDate=2017-01-04&retDate=2017-01-07&origin=TPE&dest=MFM&icn=find-flights-btn&ici=real-deals-tpemfm-twzh");
		fwriter.write(page1.asXml());
		System.out.println(page1.asXml());
		/*final HtmlForm form = page1
				.getFirstByXPath("//form[@action='https://booking.tigerair.com/Search.aspx?culture=zh-TW&gaculture=TWZH']");*/
	/*	HtmlSelect departInput = (HtmlSelect) page1
				.getElementById("ControlGroupSearchView_AvailabilitySearchInputSearchVieworiginStation1");
		HtmlSelect returnInput = (HtmlSelect) page1
				.getElementById("ControlGroupSearchView_AvailabilitySearchInputSearchViewdestinationStation1");
		HtmlSelect marketMonth1 = (HtmlSelect) page1
				.getElementById("ControlGroupSearchView_AvailabilitySearchInputSearchView_DropDownListMarketMonth1");
		HtmlSelect marketMonth2 = (HtmlSelect) page1
				.getElementById("ControlGroupSearchView_AvailabilitySearchInputSearchView_DropDownListMarketMonth2");
		
		HtmlSelect marketDay1 = (HtmlSelect) page1
				.getElementById("ControlGroupSearchView_AvailabilitySearchInputSearchView_DropDownListMarketDay1");
		
		HtmlSelect marketDay2 = (HtmlSelect) page1
				.getElementById("ControlGroupSearchView_AvailabilitySearchInputSearchView_DropDownListMarketDay2");
		*/
	/*	HtmlHiddenInput selOrigin = (HtmlHiddenInput) page1
				.getElementById("selOrigin");
		HtmlHiddenInput MarketOrigin1 = (HtmlHiddenInput) page1
				.getElementById("MarketOrigin1");
		HtmlHiddenInput VieworiginStation1 = (HtmlHiddenInput) page1
				.getElementById("VieworiginStation1");
		HtmlHiddenInput selDest = (HtmlHiddenInput) page1
				.getElementById("selDest");
		HtmlHiddenInput MarketDestination1 = (HtmlHiddenInput) page1
				.getElementById("MarketDestination1");
		HtmlHiddenInput ViewdestinationStation1 = (HtmlHiddenInput) page1
				.getElementById("ViewdestinationStation1");
		HtmlHiddenInput TT_Origin = (HtmlHiddenInput) page1
				.getElementById("TT_Origin");
		HtmlHiddenInput TT_Destination = (HtmlHiddenInput) page1
				.getElementById("TT_Destination");
		HtmlHiddenInput TT_TripKind = (HtmlHiddenInput) page1
				.getElementById("TT_TripKind");
		HtmlHiddenInput TT_DepartureDate = (HtmlHiddenInput) page1
				.getElementById("TT_DepartureDate");
		HtmlHiddenInput TT_ReturnDate = (HtmlHiddenInput) page1
				.getElementById("TT_ReturnDate");
		HtmlHiddenInput MarketMonth1 = (HtmlHiddenInput) page1
				.getElementById("MarketMonth1");
		HtmlHiddenInput MarketMonth2 = (HtmlHiddenInput) page1
				.getElementById("MarketMonth2");
		HtmlHiddenInput MarketDay1 = (HtmlHiddenInput) page1
				.getElementById("MarketDay1");
		HtmlHiddenInput MarketDay2 = (HtmlHiddenInput) page1
				.getElementById("MarketDay2");
		
		selOrigin.setValueAttribute(from);
		MarketOrigin1.setValueAttribute(from);
		VieworiginStation1.setValueAttribute(from);
		selDest.setValueAttribute(to);
		MarketDestination1.setValueAttribute(to);
		ViewdestinationStation1.setValueAttribute(to);
		
		TT_Origin.setValueAttribute(from);
		TT_Destination.setValueAttribute(to);
		TT_TripKind.setValueAttribute(structure);
		TT_DepartureDate.setValueAttribute(outboundDate);
		TT_ReturnDate.setValueAttribute(returnDate);
		
		MarketDay2.setValueAttribute(returnDate.substring(8, 10));
		MarketMonth2.setValueAttribute(returnDate.substring(0, 7));
		
		MarketDay1.setValueAttribute(outboundDate.substring(8, 10));
		MarketMonth1.setValueAttribute(outboundDate.substring(0, 7));
		

	
		HtmlTextInput departInput = (HtmlTextInput) page1.getElementById("selOriginPicker");
		departInput.setValueAttribute(from);
		
		HtmlTextInput selDestPicker = (HtmlTextInput) page1.getElementById("selDestPicker");
		selDestPicker.setValueAttribute(to);
		HtmlTextInput dateDepart = (HtmlTextInput) page1.getElementById("dateDepart");
		dateDepart.setValueAttribute(outboundDate);
		HtmlTextInput dateReturn = (HtmlTextInput) page1.getElementById("dateReturn");
		dateReturn.setValueAttribute(returnDate);
		HtmlSelect selAdult = (HtmlSelect) page1.getElementById("selAdult");
		HtmlOption optionD2 =selAdult.getOptionByValue("1");
		selAdult.setSelectedAttribute(optionD2, true);
		
		
	/*	HtmlOption optionM1 =marketMonth1.getOptionByValue(outboundDate.substring(0, 7));
		marketMonth1.setSelectedAttribute(optionM1, true);
		
		HtmlOption optionM2 =marketMonth2.getOptionByValue(returnDate.substring(0, 7));
		marketMonth2.setSelectedAttribute(optionM2, true);
		
		HtmlOption optionD1 =marketDay1.getOptionByValue(outboundDate.substring(0, 7));
		marketDay1.setSelectedAttribute(optionD1, true);
		
		HtmlOption optionD2 =marketDay2.getOptionByValue(returnDate.substring(0, 7));
		marketDay2.setSelectedAttribute(optionD2, true);
		
		HtmlOption optionR =returnInput.getOptionByValue(to);
		returnInput.setSelectedAttribute(optionR, true);
		
		HtmlOption optionC = departInput.getOptionByValue(from);
		departInput.setSelectedAttribute(optionC, true);
		*/
		
	/*	HtmlButton submitButton = (HtmlButton) page1
				.getElementById("submitSearch");
		
	//	submitButton.setAttribute("class", "btnbooking");
  //	submitButton.removeAttribute("disabled");
		System.out.println(submitButton.getAttribute("class"));
		HtmlPage page2 = submitButton.click();
		Thread.sleep(3000);
		fwriter.write(page2.asXml());
		//System.out.println(page2.asXml());*/
	}
	
	//Set the https use TLSv1.2
	private static Registry<ConnectionSocketFactory> getRegistry() throws KeyManagementException, NoSuchAlgorithmException {
	    SSLContext sslContext = SSLContexts.custom().build();
	    SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
	            new String[]{"TLSv1.2"}, null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
	    return RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	            .register("https", sslConnectionSocketFactory)
	            .build();
	}

	/* Error */
	public List<HashMap<String, String>> getMoney(String outboundDate,
			String returnDate, String from, String to, String structure)
			throws java.io.IOException, KeyManagementException, NoSuchAlgorithmException {
		System.out.println("tigerair select start !");
		File saveFile = new File("D:\\Data.txt");
		FileWriter fwriter = new FileWriter(saveFile);
		HashMap<String, String> outboundMap = new HashMap<String, String>();
		HashMap<String, String> returnMap = new HashMap<String, String>();
		List<HashMap<String, String>> moneyList = new ArrayList<HashMap<String, String>>();
	
	
		System.setProperty("https.protocols", "TLSv1.2");
		// 建立HttpClient實體.
		HttpClient client = new HttpClient();
		client.setTimeout(5000);

		//String date = addZero(i);

		GetMethod get = new GetMethod(
				(new StringBuilder(url))
					/*	.append("&ms=")
						.append(structure)
						.append("&origin=")
						.append(from)
						.append("&dest=")
						.append(to)
						.append("&depDate=")
						.append("&retDate=")
						.append(returnDate)*/
					
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
			fwriter.write(new String(responseBody));
			System.out.println(new String(responseBody));
	/*	// 建立PostMethod實體, 並指派網址
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
			}*/

		} catch (HttpException httpexc) {
			System.err.println("Fatal protocol violation: "
					+ httpexc.getMessage());
			httpexc.printStackTrace();
		} catch (IOException ioexc) {
			System.err.println("Fatal transport error: " + ioexc.getMessage());
			ioexc.printStackTrace();
		} finally {

			// ** 無論如何都必須釋放連接.
		//	post.releaseConnection();
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
