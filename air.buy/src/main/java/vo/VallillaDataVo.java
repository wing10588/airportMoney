package vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VallillaDataVo {
	String status;
	String Timestamp;
	class Result{
		String boardPoint;
		String OffPoint;
		String StartDate;
		String EndDate;
		String LowestFare;
		List<HashMap<String, String>> FareListOfDay = new ArrayList<HashMap<String, String>>();
	}
}
