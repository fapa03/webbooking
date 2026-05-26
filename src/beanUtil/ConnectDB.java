package beanUtil;

import java.sql.Connection;

import javax.naming.Context;
import javax.sql.DataSource;

import logger.AccessLog;

public class ConnectDB {
	private static String dbUrl=null;
	private static String driver=null;
	private static String propfilewithpath="dbconnect.properties";
	private static String cartaPorteInt = null;
	private static String cartaPorteExt = null;
	private static String endPointDocInt = null;
	private static String endPointDocExt = null;
	private static String endPointGuiaRetorno = null;
	private static String apiLoginLDAPInt = null;
	private static String apiLoginLDAPExt = null;
	private static String apiLoginLDAPIntWL = null;
	private static String apiLoginLDAPExtWL = null;
	private static String apibrncOCUInt = null;
	private static String apibrncOCUExt = null;
	private static String dataSource = null;
	private static String pathFileXls = null;
	
	private static boolean loaded = false;
	

	public static void init(){
		java.io.InputStream is = null;
		try {
			if(dbUrl==null || driver==null){
				
				is = ConnectDB.class.getClassLoader().
											getResourceAsStream(propfilewithpath);
				java.util.Properties p=new java.util.Properties();

				if(is!=null) {
					p.load(is);
					is.close();
					is = null;
				}
									

				driver = p.getProperty("driver");
				dbUrl = p.getProperty("dbUrl");
				cartaPorteInt = p.getProperty("cartaPorteInt");	
				cartaPorteExt = p.getProperty("cartaPorteExt");
				endPointDocInt = p.getProperty("endPointDocInt");
				endPointDocExt = p.getProperty("endPointDocExt");
				endPointGuiaRetorno = p.getProperty("endPointGuiaRetorno");
				apiLoginLDAPInt = p.getProperty("apiLoginLDAPInt");
				apiLoginLDAPExt = p.getProperty("apiLoginLDAPExt");
				setApiLoginLDAPIntWL(p.getProperty("apiLoginLDAPIntWL"));
				setApiLoginLDAPExtWL(p.getProperty("apiLoginLDAPExtWL"));
				apibrncOCUInt = p.getProperty("apibrncOCUInt");
				apibrncOCUExt = p.getProperty("apibrncOCUExt");
				dataSource = p.getProperty("dataSource");
				pathFileXls = p.getProperty("pathFileXls");
			}			
		} catch( Exception ex) {
			AccessLog.Log(ex);
			ex.printStackTrace();
		} finally {
			try {
				if (is!=null)
					is.close();
			} catch (Exception e) {
				AccessLog.Log(e);
				e.printStackTrace();
			}
		}		
	}	

	public static Connection getConnection() {
		if (!loaded) {
			init();
			loaded = true;
		}
		Connection dbCon = null;
		try {
			Context initContext = new javax.naming.InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup(dataSource);
			dbCon = ds.getConnection();
		} catch (Exception e) {
			AccessLog.Log(e);
			e.printStackTrace();
		}
		return dbCon;
	}

	public static String getCartaPorteInt() {
		return cartaPorteInt;
	}

	public static void setCartaPorteInt(String cartaPorteInt) {
		ConnectDB.cartaPorteInt = cartaPorteInt;
	}

	public static String getCartaPorteExt() {
		return cartaPorteExt;
	}

	public static void setCartaPorteExt(String cartaPorteExt) {
		ConnectDB.cartaPorteExt = cartaPorteExt;
	}


	public static String getEndPointDocInt() {
		return endPointDocInt;
	}

	public static void setEndPointDocInt(String endPointDocInt) {
		ConnectDB.endPointDocInt = endPointDocInt;
	}

	public static String getEndPointDocExt() {
		return endPointDocExt;
	}

	public static void setEndPointDocExt(String endPointDocExt) {
		ConnectDB.endPointDocExt = endPointDocExt;
	}

	public static String getEndPointGuiaRetorno() {
		return endPointGuiaRetorno;
	}

	public static void setEndPointGuiaRetorno(String endPointGuiaRetorno) {
		ConnectDB.endPointGuiaRetorno = endPointGuiaRetorno;
	}

	public static String getApiLoginLDAPInt() {
		return apiLoginLDAPInt;
	}

	public static void setApiLoginLDAPInt(String apiLoginLDAPInt) {
		ConnectDB.apiLoginLDAPInt = apiLoginLDAPInt;
	}

	public static String getApiLoginLDAPExt() {
		return apiLoginLDAPExt;
	}

	public static void setApiLoginLDAPExt(String apiLoginLDAPExt) {
		ConnectDB.apiLoginLDAPExt = apiLoginLDAPExt;
	}

	public static String getDataSource() {
		return dataSource;
	}

	public static void setDataSource(String dataSource) {
		ConnectDB.dataSource = dataSource;
	}
	/**
	 * @return the apibrncOCUInt
	 */
	public static String getApibrncOCUInt() {
		return apibrncOCUInt;
	}

	/**
	 * @param apibrncOCUInt the apibrncOCUInt to set
	 */
	public static void setApibrncOCUInt(String apibrncOCUInt) {
		ConnectDB.apibrncOCUInt = apibrncOCUInt;
	}

	/**
	 * @return the apibrncOCUExt
	 */
	public static String getApibrncOCUExt() {
		return apibrncOCUExt;
	}

	/**
	 * @param apibrncOCUExt the apibrncOCUExt to set
	 */
	public static void setApibrncOCUExt(String apibrncOCUExt) {
		ConnectDB.apibrncOCUExt = apibrncOCUExt;
	}

	public static String getPathFileXls() {
		return pathFileXls;
	}

	public static void setPathFileXls(String pathFileXls) {
		ConnectDB.pathFileXls = pathFileXls;
	}

	/**
	 * @return the apiLoginLDAPIntWL
	 */
	public static String getApiLoginLDAPIntWL() {
		return apiLoginLDAPIntWL;
	}

	/**
	 * @param apiLoginLDAPIntWL the apiLoginLDAPIntWL to set
	 */
	public static void setApiLoginLDAPIntWL(String apiLoginLDAPIntWL) {
		ConnectDB.apiLoginLDAPIntWL = apiLoginLDAPIntWL;
	}

	/**
	 * @return the apiLoginLDAPExtWL
	 */
	public static String getApiLoginLDAPExtWL() {
		return apiLoginLDAPExtWL;
	}

	/**
	 * @param apiLoginLDAPExtWL the apiLoginLDAPExtWL to set
	 */
	public static void setApiLoginLDAPExtWL(String apiLoginLDAPExtWL) {
		ConnectDB.apiLoginLDAPExtWL = apiLoginLDAPExtWL;
	}
}