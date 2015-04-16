package cn.edu.jnu.ie.util;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormater {
  /**
   * Get today's date in the yyyyMMdd format.
   **/
  public static String getDateofToday() {
    Date date = new Date(System.currentTimeMillis());
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String today = dateFormat.format(date);
    return today;
  }
  public static String second2date(String seconds){
	long now = Long.parseLong(seconds);
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	 Calendar calendar = Calendar.getInstance();
	 calendar.setTimeInMillis(now*1000);
	String d =  formatter.format(calendar.getTime());
	return d;
}
  /**
   * Transform the String with "yyyy-MM-dd-HH" formate into a Date instance. 
   * @param strDate a date String formatted like "yyyy-MM-dd-HH"
   * @return the Date instance
   */
  public static Date timeStr2Date(String strDate) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH");
    ParsePosition pos = new ParsePosition(0);
    Date strtodate = formatter.parse(strDate, pos);
    return strtodate;
  }
  
  public static String getFormatTime(Long time) {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH");
    ParsePosition pos = new ParsePosition(0);
    Date strtodate = new Date();
    strtodate.setTime(time);
    return formatter.format(strtodate);
  }
}
