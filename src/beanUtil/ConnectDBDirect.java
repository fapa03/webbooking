package beanUtil;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

public class ConnectDBDirect {
	private static StringBuffer conct = new StringBuffer();
	//private final String msgAvi = new StringBuffer("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private static String msgErr = new StringBuffer("MSERR_B_").append("ConnectDBDirect.").toString();
	
	private static String MM_sipweb2_DRIVER = "oracle.jdbc.driver.OracleDriver";
	
	/*usuario general*/
	/*private static String MM_sipweb2_USERNAME = "SIPWEB2";
	private static String MM_sipweb2_PASSWORD = "WMOY0412P";
	private static String MM_sipweb2_STRING = "jdbc:oracle:thin:@192.168.10.20:1521:SIPWEB";*/
	
	/*Usuario prepago*/
	private static String MM_sipweb2_USERNAME = "ODSPRE";
	private static String MM_sipweb2_PASSWORD = "maput6ejafacuqaswE";
	private static String MM_sipweb2_STRING = "jdbc:oracle:thin:@192.168.10.20:1521:SIPWEB";											   
	
	/*private static String MM_sipweb2_USERNAME = "SIPPRB";
	private static String MM_sipweb2_PASSWORD = "SIP2";
	private static String MM_sipweb2_STRING = "jdbc:oracle:thin:@192.168.10.224:1521:SIPPRB";*/
	
	public static Connection getConection() {
		Connection ConnRecordset1 = null;
		try {
			Driver DriverRecordset1 = (Driver)Class.forName(MM_sipweb2_DRIVER).newInstance();
			ConnRecordset1 = DriverManager.getConnection(MM_sipweb2_STRING,MM_sipweb2_USERNAME,MM_sipweb2_PASSWORD);
		} catch (Exception e) {
			System.out.println(conct.delete(0, conct.length()).append(msgErr).append("getConection() Error ").append(e).toString());
			e.printStackTrace();
		}
		return ConnRecordset1;
	}
}
