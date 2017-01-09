package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DateUtil {
	static String dateFormat = "yyyy-MM-dd";
	public static String dateToString(Date date){
	
		//設定日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//進行轉換
		String dateString = sdf.format(date);
	
		return dateString;
	}
	
	public static Date StringToDate(String dateString) throws ParseException{
		
		//設定日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		//進行轉換
		Date date = sdf.parse(dateString);

		return date;
	}
	private static Date str2Date(String str) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		if (str == null)
			return null;
 
		try {
			return format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取两个日期之间所有的日期
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static ArrayList days(String date1, String date2) {
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		ArrayList L = new ArrayList();
		if (date1.equals(date2)) {
			System.out.println("两个日期相等!");
			return L;
		}
 
		String tmp;
		if (date1.compareTo(date2) > 0) { // 确保 date1的日期不晚于date2
			tmp = date1;
			date1 = date2;
			date2 = tmp;
		}
 
		tmp = format.format(str2Date(date1).getTime() + 3600 * 24 * 1000);
 
		int num = 0;
		while (tmp.compareTo(date2) < 0) {
			L.add(tmp);
			num++;
			tmp = format.format(str2Date(tmp).getTime() + 3600 * 24 * 1000);
		}
 
		if (num == 0)
			System.out.println("两个日期相邻!");
		return L;
	}

}
