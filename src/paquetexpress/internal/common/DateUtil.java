package paquetexpress.internal.common;


	
	import java.sql.Date;
	import java.sql.Timestamp;
	import java.text.ParseException;
	import java.util.Calendar;
	import java.text.SimpleDateFormat;
	import logger.AccessLog;
	
	public class DateUtil {
		
		private static final String DATE_FORMAT = "dd/MM/yy  hh:mm:ss  a";
		private static final String DATE_ONLY_FORMAT = "dd/MM/yy";
		private static final String DATE_FORMAT_NEW = "dd/MM/yyyy";
		
		public static Date getDate(Date date) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.AM_PM, 0);		
			return (new Date(cal.getTime().getTime()));
		}
		
		public static String getDateString(Timestamp timeStamp) {
			String value = null;
			if(timeStamp != null) {
				Date date = new Date(timeStamp.getTime());
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
				value = (date != null) ? dateFormat.format(date) : "";		
			}		
			return (value == null ? "" : value);
		}	
		
		public static String getStringDate(Date date) {
			String value = null;
			if(date != null) {
				Date date1 = new Date(date.getTime());
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_NEW);
				value = (date1 != null) ? dateFormat.format(date1) : "";		
			}		
			return (value == null ? "" : value);
		}	
		
		public static Date getDateValue(String dt) {
		
			Date date = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_ONLY_FORMAT);
			if (dt.isEmpty()) {
				return new Date(new java.util.Date().getTime());
			}
			
			try {			
				date = new Date(dateFormat.parse(dt).getTime());			
			} catch(ParseException e) {
				AccessLog.Log("Inside parse() " + dt + " " + e);
			}
			return (date == null ? (new Date(new java.util.Date().getTime())) : date);
		}
		
		public static Date getTimeValue(String dt) {
			
			Date date = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			
			if (dt.isEmpty()) {
				return new Date(new java.util.Date().getTime());
			}
			
			try {			
				date = new Date(dateFormat.parse(dt).getTime());
			} catch(ParseException e) {
				AccessLog.Log("Inside parse() " + dt + " " + e);
			}
			return (date == null ? (new Date(new java.util.Date().getTime())) : date);
		}
		
		//To display today
		public String getToday() {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			return format.format(new java.util.Date());				
		}
	}


