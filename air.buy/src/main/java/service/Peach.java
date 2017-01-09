package service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import util.DateUtil;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Peach {
	private static String url = "http://www.flypeach.com/pc/tw";
	Logger logger=Logger.getLogger(Peach.class);
	public static void main(String[] args) throws Exception {
		String outboundDate = "2017/01/01";
		String returnDate = "2017/03/01";
		String from = "TPE";
		String to = "OKA";

		// String structure = "RoundTrip";
		String structure = "Oneway";
		Peach t = new Peach();
		List<HashMap<String, String>> moneyList = t.getMoney(outboundDate,
				returnDate, from, to, structure);

	}

	public List<HashMap<String, String>> getMoney(String outboundDate,
			String returnDate, String from, String to, String structure)
			throws java.io.IOException, ParseException, JSONException,
			InterruptedException {
		logger.info("peach select start !");
		File saveFile = new File("D:\\Data.txt");
		FileWriter fwriter = new FileWriter(saveFile);
		HashMap<String, String> outboundMap = new HashMap<String, String>();
		HashMap<String, String> returnMap = new HashMap<String, String>();
		List<HashMap<String, String>> moneyList = new ArrayList<HashMap<String, String>>();

		final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.waitForBackgroundJavaScript(30 * 1000); /*
														 * will wait JavaScript
														 * to execute up to 30s
														 */

		// Get the first page
		final HtmlPage page1 = webClient.getPage(url);

		final HtmlForm form = page1
				.getFirstByXPath("//form[@action='https://booking.flypeach.com/tw']");

		HtmlHiddenInput departure_date = (HtmlHiddenInput) page1
				.getElementById("departure_date");
		HtmlHiddenInput toCode = (HtmlHiddenInput) page1
				.getElementById("arrival_airport_code");
		HtmlHiddenInput fromCode = (HtmlHiddenInput) page1
				.getElementById("departure_airport_code");
		HtmlHiddenInput return_date = (HtmlHiddenInput) page1
				.getElementById("return_date");
		HtmlHiddenInput adult_count = (HtmlHiddenInput) page1
				.getElementById("adult_count");
		HtmlHiddenInput child_count = (HtmlHiddenInput) page1
				.getElementById("child_count");
		HtmlHiddenInput infant_count = (HtmlHiddenInput) page1
				.getElementById("infant_count");
		HtmlHiddenInput structureCode = (HtmlHiddenInput) page1
				.getElementById("arrival_airport_code");

		HtmlRadioButtonInput roundtripRadioButton = (HtmlRadioButtonInput) page1
				.getElementById("trip_type_roundtrip");
		HtmlRadioButtonInput onewayRadioButton = (HtmlRadioButtonInput) page1
				.getElementById("trip_type_oneway");
		HtmlTextInput departInput = page1.getElementByName("departingon");
		HtmlTextInput returnInput = page1.getElementByName("returningon");
		HtmlSelect select = (HtmlSelect) page1
				.getElementById("selectOptionAdult");
		HtmlOption option = select.getOptionByValue("1");
		select.setSelectedAttribute(option, true);
		HtmlSelect selectC = (HtmlSelect) page1
				.getElementById("selectOptionChild");
		HtmlOption optionC = selectC.getOptionByValue("0");
		selectC.setSelectedAttribute(optionC, true);
		HtmlSelect selectI = (HtmlSelect) page1
				.getElementById("selectOptionInfant");
		HtmlOption optionI = selectC.getOptionByValue("0");
		selectI.setSelectedAttribute(optionI, true);
		if ("RoundTrip".equals(structure)) {
			structureCode.setValueAttribute("true");
			roundtripRadioButton.click();

		} else {
			structureCode.setValueAttribute("false");
			onewayRadioButton.click();
		}

		fromCode.setValueAttribute(from);
		toCode.setValueAttribute(to);
		adult_count.setValueAttribute("1");
		child_count.setValueAttribute("0");
		infant_count.setValueAttribute("0");
		departInput.setValueAttribute(outboundDate);
		departure_date.setValueAttribute(outboundDate);

		return_date.setValueAttribute(returnDate);
		returnInput.setValueAttribute(returnDate);

		HtmlButton submitButton = (HtmlButton) page1.createElement("button");
		submitButton.setAttribute("type", "submit");
		form.appendChild(submitButton);

		HtmlPage page2 = submitButton.click();
	
		List<?> cal = page2.getByXPath("//a[@href='#select_date']");

		// HtmlAnchor anchor = (HtmlAnchor)
		// cal.getFirstByXPath("//a[@href='#select_date']");
	//	Runtime.getRuntime().exec("cmd /c start " + page2.getBaseURI());
		if (cal.size() > 0) {
			HtmlPage page3 = ((HtmlAnchor) cal.get(0)).click();

			Thread.sleep(3000);
			// System.out.println(page3.asXml());
			
			outboundMap = fromHtml(outboundDate.split("-")[0], page3.asXml());

			if ("RoundTrip".equals(structure)) {
				page3 = ((HtmlAnchor) cal.get(1)).click();

				Thread.sleep(3000);

				returnMap = fromHtml(returnDate.split("-")[0], page3.asXml());
			}

			fwriter.write(page3.asXml());
		}
		moneyList.add(outboundMap);
		moneyList.add(returnMap);
		fwriter.close();
		logger.info("peach select end !");
		return moneyList;

	}

	private HashMap<String, String> fromHtml(String year, String html) {

		HashMap<String, String> map = new HashMap<String, String>();
		Document doc = Jsoup.parse(html);

		Elements link = doc.select("div[class=time_table-body]");
		if (!link.isEmpty()) {
			Elements ticket = link.select("div[class=plane_ticket-c ]");
			if (!ticket.isEmpty()) {
				for (Element e : ticket) {
					if (e.text().contains("查無剩餘機位")) {
						continue;
					}
					String[] str = e.text().replace("~", "").replace("$", " ")
							.replace(",", "").split(" ");
					String day = year + "-" + addZero(str[0].split("/")[0])
							+ "-" + addZero(str[0].split("/")[1]);
					logger.info(day + " " + str[1] +" "+ str[2]);
					map.put(day, str[1]+" "+ str[2]  );

				}

			}

		}

		return map;

	}

	private static String addZero(String date) {
		String formatStr = "%02d";

		String formatAns = String.format(formatStr,
				new Object[] { Integer.valueOf(date) });
		return formatAns;
	}

}
