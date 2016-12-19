package vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoneyVo {
	private List<HashMap<String, String>> moneyList = new ArrayList();
	private String airportName;
	private String errMsg;

	public List<HashMap<String, String>> getMoneyList() {
		return this.moneyList;
	}

	public void setMoneyList(List<HashMap<String, String>> moneyList) {
		this.moneyList = moneyList;
	}

	public String getAirportName() {
		return this.airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	

	
}
