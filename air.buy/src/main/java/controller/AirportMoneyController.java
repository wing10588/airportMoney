package controller;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHiddenInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.google.gson.Gson;

import service.Airasia;
import service.Jetstar;
import service.Peach;
import service.Scoot;
import service.Tigerair;
import service.Vanilla;
import util.DateUtil;
import vo.MoneyVo;

@Controller
public class AirportMoneyController extends Thread{
	private static Logger logger = Logger
			.getLogger(AirportMoneyController.class);

	@RequestMapping({ "/" })
	public ModelAndView index(
			@RequestParam(value = "name", required = false, defaultValue = "World") String name,
			Model model) {
		model.addAttribute("name", name);

		return new ModelAndView("index");
	}

	@RequestMapping({ "/getUrl" })
	@ResponseBody
	public List<String> getUrl(
			@RequestParam(value = "outboundDate", required = true) String outboundDate,
			@RequestParam(value = "returnDate") String returnDate,
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to,
			@RequestParam(value = "structure", required = true) String structure,
			@RequestParam(value = "airName[]", required = true) String[] airName)
			throws Exception {
		List<String> urlList = new ArrayList<String>();
		logger.info(outboundDate + "," + returnDate + "," + from + "," + to
				+ "," + structure);
		if (!outboundDate.isEmpty()) {
			outboundDate = outboundDate.replace("/", "-");
		}
		if (!returnDate.isEmpty()) {
			returnDate = returnDate.replace("/", "-");
		}
	
		if ("RoundTrip".equals(structure) && airName.length <= 1) {
			urlList.add(swatchRT(airName[0],outboundDate, from, to, returnDate));
		} else if("RoundTrip".equals(structure) && airName.length > 1) {
			urlList.add(swatch(airName[0],outboundDate, from, to, structure));
			urlList.add(swatch(airName[1],returnDate, to, from, structure));
		}else if(airName.length <= 1) {
			String url = swatch(airName[0],outboundDate, from, to, structure);
			urlList.add(url);
		} 

		return urlList;
	}
	
	//oneway
	private String swatch(String name,String date,String from,String to,String structure) throws ParseException{
		if("Airasia".equals(name)) {
			date = date.replace("/", "-");
			return  "https://booking.airasia.com/Flight/Select?c=true&s=false&r=true&o1="+from+"&d1="+to+"&dd1="+date;
		}else if("Scoot".equals(name)) {
		
			return  "http://makeabooking.flyscoot.com/book/Flight/Select?utm_campaign=update&adt=1&chd=0&dd="+date+"&dst1="+from+"&culture=zh-tw&utm_source=intent_connect&ast1="+to+"&inf=0&type=Oneway";
		}else if("Vanilla".equals(name)) {
			
			date = date.replace("/", "-");
			return "https://www.vanilla-air.com/tw/booking/#/flight-select/?tripType=OW&origin="+from+"&destination="+to+"&outboundDate="+date+"&adults=1&children=0&infants=0&promoCode=&mode=searchResultInter";
		}else if("Peach".equals(name)) {
			return "";
		}else if("Jetstar".equals(name)) {
			return "http://pricewatch.jetstar.com/deeplink?deeplink=http://booknow.jetstar.com/Search.aspx?utm_campaign=pricedrop%26ADT=1%26Origin1="+from+"%26RadioButtonMarketStructure=OneWay%26utm_content="+from+"%253ACTS%253A"+date+"%26INFANT=0%26utm_source=pricewatch%26Day1="+date.substring(8, 10)+"%26utm_medium=email%26MonthYear1="+date.substring(0, 7)+"%26CHD=0%26AutoSubmit=Y%26Destination1="+to+"&partner_id=jetstar&culture_code=en-AU";
		}else if("Tigerair".equals(name)) {
			return "";
		}
		else{
			return null;
		}
			
	}
	//來回
	private String swatchRT(String name,String date,String from,String to,String date2) throws ParseException, FailingHttpStatusCodeException, MalformedURLException, IOException{
		if("Airasia".equals(name)) {
			date = date.replace("/", "-");
			return  "https://booking.airasia.com/Flight/Select?c=true&s=false&r=true&o1="+from+"&d1="+to+"&dd1="+date+"&dd2="+date2;
		}else if("Scoot".equals(name)) {
			return  "http://makeabooking.flyscoot.com/book/Flight/Select?utm_campaign=update&adt=1&chd=0&dd="+date+  "&dst1="+from+"&rd="+date2+"&culture=zh-tw&utm_source=intent_connect&ast1="+to+"&inf=0&type=Return";
		}else if("Vanilla".equals(name)) {
			date = date.replace("/", "-");
			return "https://www.vanilla-air.com/tw/booking/#/flight-select/?tripType=RT&origin="+from+"&destination="+to+"&outboundDate="+date+"&returnDate="+date2+"&adults=1&children=0&infants=0&promoCode=&mode=searchResultInter";
		}else if("Peach".equals(name)) {
			final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());
			webClient.waitForBackgroundJavaScript(30 * 1000); /*
															 * will wait JavaScript
															 * to execute up to 30s
															 */

			// Get the first page
			final HtmlPage page1 = webClient.getPage("http://www.flypeach.com/pc/tw");

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
		
				structureCode.setValueAttribute("true");
				roundtripRadioButton.click();

			
			fromCode.setValueAttribute(from);
			toCode.setValueAttribute(to);
			adult_count.setValueAttribute("1");
			child_count.setValueAttribute("0");
			infant_count.setValueAttribute("0");
			departInput.setValueAttribute(date);
			departure_date.setValueAttribute(date);

			return_date.setValueAttribute(date2);
			returnInput.setValueAttribute(date2);

			HtmlButton submitButton = (HtmlButton) page1.createElement("button");
			submitButton.setAttribute("type", "submit");
			form.appendChild(submitButton);
			HtmlPage page2 = submitButton.click();
			
				
			return  page2.getUrl()+"air.buy"+page2.asXml();
		}else if("Jetstar".equals(name)) {
			return "http://pricewatch.jetstar.com/deeplink?deeplink=http://booknow.jetstar.com/Search.aspx?utm_campaign=pricedrop%26ADT=1%26Origin1="+from+"%26RadioButtonMarketStructure=RoundTrip%26utm_content=TPE%253ACTS%253A"+date+"%26INFANT=0%26utm_source=pricewatch%26Day1="+date.substring(8, 10)+"%26utm_medium=email%26MonthYear1="+date.substring(0, 7)+"%26Origin2="+to+"%26Destination2="+from+"%26Day2="+date2.substring(8, 10)+"%26MonthYear2="+date2.substring(0, 7)+"%26CHD=0%26AutoSubmit=Y%26Destination1="+to+"&partner_id=jetstar&culture_code=en-AU";
		}else if("Tigerair".equals(name)) {
			return "";
		}
		else{
			return null;
		}
			
	}

	@RequestMapping(value = { "/getMoney" }, headers = { "Accept=*/*" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public List<MoneyVo> getMoney(
			@RequestParam(value = "outboundDate", required = true) String outboundDate,
			@RequestParam(value = "returnDate", required = true) String returnDate,
			@RequestParam(value = "from", required = true) final String from,
			@RequestParam(value = "to", required = true) final String to,
			@RequestParam(value = "structure", required = true) final String structure,HttpServletResponse res)  
			throws Exception {
		final List<MoneyVo> moneyVoList = new ArrayList();
		final String outbound = outboundDate.replace("/", "-");
		final String returnD = returnDate.replace("/", "-");
		try {
			
			Thread thread1 = new Thread(new Runnable() {

				public void run() {
					MoneyVo moneyVo = new MoneyVo();
					try {

						Tigerair t = new Tigerair();
						List<HashMap<String, String>> moneyList = t.getMoney(
								outbound.substring(0, 7),
								returnD.isEmpty()? "": returnD.substring(0, 7), from, to, structure);
						moneyVo = new MoneyVo();
						moneyVo.setAirportName("Tigerair");
						moneyVo.setMoneyList(moneyList);

					} catch (Exception e) {
						e.printStackTrace();

						moneyVo.setErrMsg("查詢失敗，請重新查詢");

					}
					moneyVoList.add(moneyVo);
				}
			});
			 
		//	thread1.start();
			 
			Thread thread2 = new Thread(new Runnable() {
				public void run() {
					Jetstar jetstar = new Jetstar();
					List<HashMap<String, String>> jetstarMoneyList = null;
					try {
						jetstarMoneyList = jetstar.getMoney(
								outbound.substring(0, 7),
								returnD.isEmpty()? "": returnD.substring(0, 7), from, to, structure);
					} catch (IOException | ParseException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					MoneyVo moneyjetstarVo = new MoneyVo();
					moneyjetstarVo.setAirportName("Jetstar");
					moneyjetstarVo.setMoneyList(jetstarMoneyList);
					moneyVoList.add(moneyjetstarVo);

					
					Vanilla vanilla = new Vanilla();
					String st = structure;
					if ("RoundTrip".equals(st)) {
						st = "RT";
					} else {
						st = "OW";
					}
					List<HashMap<String, String>> vanillaMoneyList = null;
					try {
						vanillaMoneyList = vanilla
								.getMoney(outbound, returnD, from, to, st);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					MoneyVo moneyvanillarVo = new MoneyVo();
					moneyvanillarVo.setAirportName("Vanilla");
					moneyvanillarVo.setMoneyList(vanillaMoneyList);
					moneyVoList.add(moneyvanillarVo);

					Scoot scoot = new Scoot();
					String scootst = structure;
					if ("RoundTrip".equals(scootst)) {
						scootst = "Return";
					} else {
						scootst = "Oneway";
					}
					List<HashMap<String, String>> scootMoneyList = null;
					try {
						scootMoneyList = scoot.getMoney(
								outbound.substring(0, 7),
								returnD.isEmpty()? "": returnD.substring(0, 7), from, to, scootst);
					} catch (IOException | ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					MoneyVo moneyscootVo = new MoneyVo();
					moneyscootVo.setAirportName("Scoot");
					moneyscootVo.setMoneyList(scootMoneyList);
					moneyVoList.add(moneyscootVo);
				}
			});
			
	
		Thread thread3 = new Thread(new Runnable() {
						public void run() {
					Peach peach = new Peach();
					List<HashMap<String, String>> peachMoneyList = null;
					try {
						peachMoneyList = peach.getMoney(outbound, returnD,
								from, to, structure);
					} catch (IOException | ParseException | JSONException
							| InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					MoneyVo moneypeachVo = new MoneyVo();
					moneypeachVo.setAirportName("Peach ");
					moneypeachVo.setMoneyList(peachMoneyList);
					moneyVoList.add(moneypeachVo);
				}
			});
		Thread thread4 = new Thread(new Runnable() {
			public void run() {
		Airasia airasia = new Airasia();
		List<HashMap<String, String>> airasiaMoneyList = null;
	
		try {
			airasiaMoneyList = airasia.getMoney(outbound.substring(0, 7), returnD.isEmpty()? "": returnD.substring(0, 7),
						from, to, structure);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MoneyVo moneyairasiaVo = new MoneyVo();
		moneyairasiaVo.setAirportName("Airasia");
		moneyairasiaVo.setMoneyList(airasiaMoneyList);
		moneyVoList.add(moneyairasiaVo);
	}
});	
			thread2.start();
			thread3.start();
			thread4.start();
		//	thread1.join();   
			thread2.join();  
			thread3.join(); 
			thread4.join(); 
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			MoneyVo errVo = new MoneyVo();
			errVo.setErrMsg("查詢失敗，請重新查詢");
			
		}
		Gson gson = new Gson(); 
		String json = gson.toJson(moneyVoList);
		
		return moneyVoList;
	}
}
