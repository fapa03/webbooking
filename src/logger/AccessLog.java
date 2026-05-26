 package logger;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: prasannas
 * Date: Jun 6, 2003
 * Time: 1:30:50 PM
 * To change this template use Options | File Templates.
 */

public class AccessLog {
    private FileOutputStream fp=null;
    private static AccessLog singleton = null;
    public static String appRealPath;
	private static String VERSION = " :ver1: ";
	private static StringBuilder conct = new StringBuilder();
	
    public static void Log(Exception e)  {
    	
    	try {
    		conct.setLength(0);
    		System.out.println( conct.append("_webbooking_").append(e).toString());
    		e.printStackTrace();
		} catch (Exception e2) {
			System.err.println( "Log()_Exception error1: " + e2);
			e2.printStackTrace();
		}
//        if(singleton == null) {
//            singleton = new AccessLog();
//        }
//		if( singleton == null || singleton.fp == null) {
//			System.err.println( "Could not write to Log: ");
//			e.printStackTrace();
//			return;
//		}
//		try {
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			e.printStackTrace(new PrintStream(bos));
//			Date dt = new Date();
//			DataOutputStream dos = new DataOutputStream(singleton.fp);
//			String tempStr = conct.delete(0,conct.length()).append(dt).append(VERSION).append(bos.toString()).append("\r\n").toString();
//			dos.writeBytes(tempStr);
//		} catch( IOException ioe) {
//			System.err.println( "Could not write to Log: ");
//			e.printStackTrace();
//		}
    }//method ends

	public static void Log(String str) {
		//Date dt = new Date();
		//System.out.println( conct.delete(0,conct.length()).append(dt).append("_webbooking_").append(str).toString());
		//conct.setLength(0);
		System.out.println("_webbooking_" + str);
//        if(singleton == null)  {
//			singleton = new AccessLog();
//        }
//		if( singleton == null || singleton.fp == null) {
//			System.err.println( conct.delete(0,conct.length()).append("Could not write to Log: ").append(str).toString());
//			return;
//		}
//		try {
//			//Date dt = new Date();
//			DataOutputStream dos = new DataOutputStream(singleton.fp);
//			String tempStr = conct.delete(0,conct.length()).append(dt).append(VERSION).append(str).append("\r\n").toString();
//			dos.writeBytes(tempStr);
//			dos.flush();
//			
//		} catch( IOException ioe) {
//			System.err.println( conct.delete(0,conct.length()).append("Could not write to Log: ").append(str).toString());
//		}		
    }//method ends

    private AccessLog() {
		//String filename = appRealPath+"log/error_log.log";
    	conct.setLength(0);
		String filename = conct.append(appRealPath).append("log/error_log.txt").toString();
		try {
			fp = new FileOutputStream( filename, true);
		} catch( IOException ioe) {
			conct.setLength(0);
			System.err.println(conct.append( "Could not create log file: " ).append(filename).toString());
			ioe.printStackTrace();
		}
    }
}
 