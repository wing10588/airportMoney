package controller;

import java.io.IOException;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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

import com.google.gson.Gson;

import service.Airasia;
import service.Jetstar;
import service.Peach;
import service.Scoot;
import service.Tigerair;
import service.Vanilla;
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

	@RequestMapping({ "/getMoneyWeb" })
	public ModelAndView handleRequest(
			@RequestParam(value = "outboundDate", required = true) String outboundDate,
			@RequestParam(value = "returnDate", required = true) String returnDate,
			@RequestParam(value = "from", required = true) String from,
			@RequestParam(value = "to", required = true) String to,
			@RequestParam(value = "structure", required = true) String structure)
			throws Exception {
		logger.info(outboundDate + "," + returnDate + "," + from + "," + to
				+ "," + structure);
		if (!outboundDate.isEmpty()) {
			outboundDate = outboundDate.replace("/", "-").substring(0, 7);
			System.out.println(outboundDate);
		}
		if (!returnDate.isEmpty()) {
			returnDate = returnDate.replace("/", "-").substring(0, 7);
			System.out.println(returnDate);
		}
		Tigerair t = new Tigerair();
		List<HashMap<String, String>> moneyList = t.getMoney(outboundDate,
				returnDate, from, to, structure);

		return new ModelAndView("index", "money", moneyList);
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
								returnD.substring(0, 7), from, to, structure);
						moneyVo = new MoneyVo();
						moneyVo.setAirportName("Tigerair");
						moneyVo.setMoneyList(moneyList);

					} catch (Exception e) {
						e.printStackTrace();

						moneyVo.setErrMsg("琩高ア毖叫穝琩高");

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
								returnD.substring(0, 7), from, to, structure);
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
								returnD.substring(0, 7), from, to, scootst);
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
			airasiaMoneyList = airasia.getMoney(outbound.substring(0, 7), returnD.substring(0, 7),
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
			errVo.setErrMsg("琩高ア毖叫穝琩高");
			
		}
		Gson gson = new Gson(); 
		String json = gson.toJson(moneyVoList);
		
		return moneyVoList;
	}
}
