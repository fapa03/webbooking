import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import bean.Resources;
import logger.AccessLog;

public class JavClientRecords {
	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	Resources resources = new Resources();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getLovRecords(Connection con, String clientID, String desSiteId) {
		HashMap values = new HashMap(2);
		ArrayList result = new ArrayList();

		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			String query = cnct
					.delete(0, cnct.length())
					.append("SELECT PACK_WEB.FUN_FTCH_CLNT_NAME(WO_DEST_CLNT_ID), WO_DEST_CLNT_ID FROM WEB_ORGN_DEST_CLNT ")
					.append("WHERE WO_ORGN_CLNT_ID = ? AND WO_DEST_SITE_ID = ? ORDER BY 1")
					.toString();// condition added by B.Emerson on 20/06/2003

			psmt = con.prepareStatement(query);

			psmt.setString(1, clientID);
			psmt.setString(2, desSiteId);// added by B.Emerson on 20/06/2003

			rs = psmt.executeQuery();

			while (rs.next()) {
				values.put("clientName", rs.getString(1));
				values.put("clientId", rs.getString(2));
				result.add(values.clone());
				values.clear();
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecords()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecords()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getClientrecordsOnly(Connection con, String siteId,
			String clientName, String clientId1,String groupClientId) throws Exception {
		HashMap values = new HashMap(2);
		ArrayList result = new ArrayList();
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		//AccessLog.Log("clientid " + clientId1);
		//AccessLog.Log("clientname " + clientName);
		// String query =
		// "select pack_web.fun_ftch_clnt_name(WO_DEST_CLNT_ID),"+
		// "WO_DEST_CLNT_ID from web_orgn_dest_clnt "+
		// "where WO_ORGN_CLNT_ID =?"+
		// "and WO_DEST_SITE_ID =? order by 1";//condition added by B.Emerson on
		// 20/06/2003

		// String
		// query1="SELECT CM_CLNT_ID, CM_CLNT_NAME FROM SYS_CLNT_MSTR WHERE " +
		// " CM_ASGN_TO_SITE =? AND CM_CLNT_NAME LIKE '"+clientName+"%' AND CM_CLNT_ID LIKE '"+clientId1+"%'";
		// query="SELECT CM_CLNT_ID, CM_CLNT_NAME FROM SYS_CLNT_MSTR WHERE " +
		// " CM_ASGN_TO_SITE =? AND CM_CLNT_ID LIKE '"+clientId1+"%'";

		try {
			String tipoCliente = "";
			String filtroGpo = "";
			String query = "SELECT CM_CLNT_TYPE FROM SYS_CLNT_MSTR WHERE CM_CLNT_ID = ?"; 
			
			psmt = con.prepareStatement(query);
			psmt.setString(1, groupClientId);
			rs = psmt.executeQuery();
			
			if (rs.next()) {
				tipoCliente =  rs.getString(1);
			}
			
			if (tipoCliente.equals("G") || tipoCliente.equals("N")) {
				filtroGpo = "AND CM_GRUP_CLNT_ID = ? ";
			} else {
				filtroGpo = "AND CM_CLNT_ID = ? ";
			}
			//query = "SELECT CM_CLNT_ID, CM_CLNT_NAME FROM SYS_CLNT_VIEW WHERE AM_PE_BRNC_ID = ?"+filtroGpo+" AND CM_CLNT_ID = ? AND CM_CLNT_NAME LIKE ?";
			
			if (rs!=null)
				rs.close();
			if (psmt != null)
				psmt.close();
			
			query = cnct.delete(0, cnct.length())
					.append("select a.cm_clnt_id, a.cm_clnt_name ")
					.append("from sys_clnt_mstr a, sys_addr_mstr b ")
					.append("where a.cm_clnt_id = b.am_enty_id(+) ")
					.append("and am_pe_site_id = ? ")
					.append(filtroGpo).append("and a.cm_clnt_id = ? and cm_clnt_name like ? and cm_actv_flag = ? ")
					.append("group by a.cm_clnt_id, a.cm_clnt_name").toString();

			//AccessLog.Log("clientid " + clientId1);
			//AccessLog.Log("clientname " + clientName);
			if (((clientName != null) && (clientName.length() > 0))
					&& ((clientId1 != null) && (clientId1.length() > 0))) {
				//query = query;
				psmt = con.prepareStatement(query);
				//if (tipoCliente.equals("G")) {
					psmt.setString(1, siteId);
					psmt.setString(2, groupClientId);					
					psmt.setString(3, clientId1);
					psmt.setString(4, clientName+"%");
					psmt.setString(5, "A");
//				} else {
//					psmt.setString(1, siteId);
//					psmt.setString(2, clientId1);
//					psmt.setString(3, clientName+"%");	
//				}
				
			} else if ((clientId1 != null) && ((clientId1.length() > 0))) {
				//query = "SELECT CM_CLNT_ID, CM_CLNT_NAME FROM SYS_CLNT_VIEW WHERE AM_PE_BRNC_ID = ?"+filtroGpo+" AND CM_CLNT_ID = ?";
				query = cnct.delete(0, cnct.length())
						.append("select a.cm_clnt_id, a.cm_clnt_name ")
						.append("from sys_clnt_mstr a, sys_addr_mstr b ")
						.append("where a.cm_clnt_id = b.am_enty_id(+) ")
						.append("and am_pe_site_id = ? ")
						.append(filtroGpo).append("and a.cm_clnt_id = ? and cm_actv_flag = ?  ")
						.append("group by a.cm_clnt_id, a.cm_clnt_name").toString();
				
				
				psmt = con.prepareStatement(query);
				
				//if (tipoCliente.equals("G")) {
					psmt.setString(1, siteId);
					psmt.setString(2, groupClientId);
					psmt.setString(3, clientId1);
					psmt.setString(4, "A");
//				} else {
//					psmt.setString(1, siteId);
//					psmt.setString(2, clientId1);	
//				}
			} else if ((clientName != null) && (clientName.length() > 0)) {
				//query = "SELECT CM_CLNT_ID, CM_CLNT_NAME FROM SYS_CLNT_VIEW WHERE AM_PE_BRNC_ID = ?"+filtroGpo+" AND CM_CLNT_NAME LIKE ?";
				query = cnct.delete(0, cnct.length())
						.append("select a.cm_clnt_id, a.cm_clnt_name ")
						.append("from sys_clnt_mstr a, sys_addr_mstr b ")
						.append("where a.cm_clnt_id = b.am_enty_id(+) ")
						.append("and am_pe_site_id = ? ")
						.append(filtroGpo).append("and a.cm_clnt_name like ? and cm_actv_flag = ?  ")
						.append("group by a.cm_clnt_id, a.cm_clnt_name").toString();
				
				psmt = con.prepareStatement(query);
				
				//if (tipoCliente.equals("G")) {
					psmt.setString(1, siteId);
					psmt.setString(2, groupClientId);
					psmt.setString(3, clientName+"%");
					psmt.setString(4, "A");
//				} else {
//					psmt.setString(1, siteId);
//					psmt.setString(2, clientName+"%");	
//				}								
			}

			//psmt = con.prepareStatement(query);
			//AccessLog.Log(query);
			//AccessLog.Log("siteId "+siteId);
			//AccessLog.Log("clientName "+clientName);
			//AccessLog.Log("clientId1 "+clientId1);
			//AccessLog.Log("tipoCliente "+tipoCliente);
			//AccessLog.Log("groupClientId "+groupClientId);

			rs = psmt.executeQuery();
			String clientName1 = "";
			String clientId = "";

			while (rs.next()) {
				clientId = rs.getString(1);
				clientName1 = rs.getString(2);				
				values.put("clientName", clientName1);
				values.put("clientId", clientId);
				result.add(values.clone());
				values.clear();
			}
			//AccessLog.Log("after 7thwhl in JavClientR");
			/*
			 * values = new HashMap(); values.put("clientName","ab");
			 * values.put("clientId","rama"); result.add(values);
			 * 
			 * values = new HashMap(); values.put("clientName","cd");
			 * values.put("clientId","rama1"); result.add(values);
			 */
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getClientrecordsOnly()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getClientrecordsOnly()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getClientDestRecordsOnly(Connection con, String siteId,
			String clientName, String clientId1, String origClntId, String userClave) {
		HashMap values = new HashMap(2);
		ArrayList result = new ArrayList();
		String parms = "";
		String query = "";
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		double time = System.currentTimeMillis();
		try {			
			int timeOutVal = getTimeOut(con, "PPG_CLNT_DEST");
			
		    String qryVer = getQueryVersion(con, "PPG_CLNT_DEST");
			query = cnct.delete(0, cnct.length())
					.append("SELECT /*"+qryVer+"*/ CM_CLNT_ID, CM_CLNT_NAME, SM_SITE_NAME ")
					.append("FROM WEB_ORGN_DEST_CLNT wodc, SYS_CLNT_MSTR clnt, SYS_ADDR_MSTR sam, SYS_SITE_MSTR site ")
					.append("WHERE WODC.WO_ORGN_CLNT_ID = ? AND wodc.WO_DEST_CLNT_ID = clnt.CM_CLNT_ID AND clnt.CM_CLNT_ID = ? AND clnt.CM_CLNT_ID = sam.AM_ENTY_ID AND am_pe_site_id = ? AND clnt.cm_clnt_name LIKE ? AND AM_ADDR_TYPE = 'CLNT' AND clnt.CM_ACTV_FLAG = ? AND site.SM_SITE_ID = sam.am_pe_site_id ")
					.append("GROUP BY CM_CLNT_ID, CM_CLNT_NAME, SM_SITE_NAME").toString();

			if (((clientName != null) && (clientName.length() > 0))
					&& ((clientId1 != null) && (clientId1.length() > 0))) {
				psmt = con.prepareStatement(query);
				
				psmt.setString(1, origClntId);
				psmt.setString(2, siteId);
				psmt.setString(3, clientId1);
				psmt.setString(4, clientName+"%");
				psmt.setString(5, "A");
			} else if ((clientId1 != null) && ((clientId1.length() > 0))) {
				query = cnct.delete(0, cnct.length())
						.append("SELECT /*"+qryVer+"*/ CM_CLNT_ID, CM_CLNT_NAME, SM_SITE_NAME ")
						.append("FROM WEB_ORGN_DEST_CLNT wodc, SYS_CLNT_MSTR clnt, SYS_ADDR_MSTR sam, SYS_SITE_MSTR site ")
						.append("WHERE WODC.WO_ORGN_CLNT_ID = ? AND wodc.WO_DEST_CLNT_ID = clnt.CM_CLNT_ID AND am_pe_site_id = ? AND clnt.CM_CLNT_ID = ? AND clnt.CM_CLNT_ID = sam.AM_ENTY_ID AND clnt.CM_ACTV_FLAG = ? AND site.SM_SITE_ID = sam.am_pe_site_id ")
						.append("GROUP BY CM_CLNT_ID, CM_CLNT_NAME, SM_SITE_NAME").toString();
				
				psmt = con.prepareStatement(query);
				
				psmt.setString(1, origClntId);
				psmt.setString(2, siteId);
				psmt.setString(3, clientId1);
				psmt.setString(4, "A");

			} else if ((clientName != null) && (clientName.length() > 0)) {
				query = cnct.delete(0, cnct.length())
						.append("SELECT /*"+qryVer+"*/CM_CLNT_ID, CM_CLNT_NAME, SM_SITE_NAME ")
						.append("FROM WEB_ORGN_DEST_CLNT wodc, SYS_CLNT_MSTR clnt, SYS_ADDR_MSTR sam, SYS_SITE_MSTR site ")
						.append("WHERE WODC.WO_ORGN_CLNT_ID = ? AND wodc.WO_DEST_CLNT_ID = clnt.CM_CLNT_ID AND clnt.CM_CLNT_ID = sam.AM_ENTY_ID AND am_pe_site_id = ? AND clnt.cm_clnt_name LIKE ? AND clnt.CM_ACTV_FLAG = ? AND site.SM_SITE_ID = sam.am_pe_site_id ")
						.append("GROUP BY CM_CLNT_ID, CM_CLNT_NAME, SM_SITE_NAME").toString();
				
				psmt = con.prepareStatement(query);
				
				psmt.setString(1, origClntId);
				psmt.setString(2, siteId);
				psmt.setString(3, clientName+"%");			
				psmt.setString(4, "A");
			}			
			
			psmt.setQueryTimeout(timeOutVal);
			rs = psmt.executeQuery();
			
			String clientName1 = "";
			String clientId = "";
			String siteName = "";

			while (rs.next()) {
				clientId = rs.getString(1);
				clientName1 = rs.getString(2);
				siteName = rs.getString(3);	
				
				values.put("clientName", clientName1);
				values.put("clientId", clientId);
				values.put("siteName", siteName);
				
				result.add(values.clone());
				values.clear();
			}
			
		} catch(SQLException e){
			time = (System.currentTimeMillis() - time);
			String resultRound = String.format("%.4f", (time/60));
			String msge = ". seconds "+resultRound+" CLIENTE ORIGEN: "+ origClntId + " USUARIO: "+ userClave + 
					" PARAMETROS:["+origClntId+","+siteId+","+clientId1+","+clientName+"%,"+"A] CONSULTA : "+query;
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getClientDestRecordsOnly() Error1:").append(e).append(msge).toString());
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getClientDestRecordsOnly() Error2:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getClientrecordsOnly()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	public int getTimeOut(Connection con, String type) {
		String query = "SELECT PM_VLUE1_ID FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = ? AND PM_PARM_TYPE = ? AND PM_PARM_CODE1 = ? AND PM_PARM_CODE2 = ?";
		PreparedStatement psmt = null;
		ResultSet rs= null;
		int timeOutVal = 60;
		
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, "WEB");
			psmt.setString(2, "SQL_TIMEOUT");
			psmt.setString(3, "WEB_PP");
			psmt.setString(4, type);
			
			rs = psmt.executeQuery();
			
			if (rs.next()) {
				timeOutVal = rs.getInt(1);				
				if (timeOutVal<=0) {
					timeOutVal = 60;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			resources.closeResources(rs, psmt);
		}
		return timeOutVal;
	}
	
	public String getQueryVersion(Connection con, String type) {
		String query = "SELECT PM_VLUE1_ID FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = ? AND PM_PARM_TYPE = ? AND PM_PARM_CODE1 = ? AND PM_PARM_CODE2 = ?";
		PreparedStatement psmt = null;
		ResultSet rs= null;
		String version = "";
		
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, "WEB");
			psmt.setString(2, "QUERY_VERSION");
			psmt.setString(3, "WEB_PP");
			psmt.setString(4, type);
			
			rs = psmt.executeQuery();
			
			if (rs.next()) {
				version = rs.getString(1);	
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			resources.closeResources(rs, psmt);
		}
		return version;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getClientGpoAsig(Connection con, String siteId,
			String clientName, String clientId1,String groupClientId) throws Exception {
		HashMap values = new HashMap(2);
		ArrayList result = new ArrayList();
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		//AccessLog.Log("clientid " + clientId1);
		//AccessLog.Log("clientname " + clientName);
	
		try {
			String tipoCliente = "";
			String filtroGpo = "";
			String query = "SELECT CM_CLNT_TYPE FROM SYS_CLNT_MSTR WHERE CM_CLNT_ID = ?"; 
			
			psmt = con.prepareStatement(query);
			psmt.setString(1, groupClientId);
			rs = psmt.executeQuery();
			
			if (rs.next()) {
				tipoCliente =  rs.getString(1);
			}
			
			if (tipoCliente.equals("G") || tipoCliente.equals("N")) {
				filtroGpo = "AND CM_GRUP_CLNT_ID = ? ";
			} else {
				filtroGpo = "AND CM_CLNT_ID = ? ";
			}
			//query = "SELECT CM_CLNT_ID, CM_CLNT_NAME FROM SYS_CLNT_VIEW WHERE AM_PE_BRNC_ID = ?"+filtroGpo+" AND CM_CLNT_ID = ? AND CM_CLNT_NAME LIKE ?";
			if (rs!=null)
				rs.close();
			if (psmt != null)
				psmt.close();
			
			query = cnct.delete(0, cnct.length())
					.append("select a.cm_clnt_id, a.cm_clnt_name, PACK_WEB.Fun_ftch_site_name(B.AM_PE_SITE_ID) ")
					.append("from sys_clnt_mstr a, sys_addr_mstr b ")
					.append("where a.cm_clnt_id = b.am_enty_id(+) ")
					//.append("and am_pe_site_id = ? ")
					.append(filtroGpo)
					.append("and cm_clnt_name like ? and cm_actv_flag = ? ")
					//.append("and a.cm_clnt_id = ? and cm_clnt_name like ? ")
					.append("group by a.cm_clnt_id, a.cm_clnt_name, B.AM_PE_SITE_ID").toString();

			//AccessLog.Log("clientid " + clientId1);
			//AccessLog.Log("clientname " + clientName);
			if (((clientName != null) && (clientName.length() > 0))
					&& ((clientId1 != null) && (clientId1.length() > 0))) {
				//query = query;
				psmt = con.prepareStatement(query);

				//psmt.setString(1, siteId);
				psmt.setString(1, groupClientId);					
				//psmt.setString(3, clientId1);
				psmt.setString(2, clientName+"%");
				psmt.setString(3, "A");
				
			} else if ((clientId1 != null) && ((clientId1.length() > 0))) {
				//query = "SELECT CM_CLNT_ID, CM_CLNT_NAME FROM SYS_CLNT_VIEW WHERE AM_PE_BRNC_ID = ?"+filtroGpo+" AND CM_CLNT_ID = ?";
				query = cnct.delete(0, cnct.length())
						.append("select a.cm_clnt_id, a.cm_clnt_name, PACK_WEB.Fun_ftch_site_name(B.AM_PE_SITE_ID) ")
						.append("from sys_clnt_mstr a, sys_addr_mstr b ")
						.append("where a.cm_clnt_id = b.am_enty_id(+) ")
						//.append("and am_pe_site_id = ? ")
						.append(filtroGpo)
						.append("and cm_actv_flag = ? ")
						.append("group by a.cm_clnt_id, a.cm_clnt_name, B.AM_PE_SITE_ID").toString();				
				
				psmt = con.prepareStatement(query);				
				
				//psmt.setString(1, siteId);
				psmt.setString(1, groupClientId);
				psmt.setString(2, "A");					

			} else if ((clientName != null) && (clientName.length() > 0)) {
				//query = "SELECT CM_CLNT_ID, CM_CLNT_NAME FROM SYS_CLNT_VIEW WHERE AM_PE_BRNC_ID = ?"+filtroGpo+" AND CM_CLNT_NAME LIKE ?";
				query = cnct.delete(0, cnct.length())
						.append("select a.cm_clnt_id, a.cm_clnt_name, PACK_WEB.Fun_ftch_site_name(B.AM_PE_SITE_ID) ")
						.append("from sys_clnt_mstr a, sys_addr_mstr b ")
						.append("where a.cm_clnt_id = b.am_enty_id(+) ")
						//.append("and am_pe_site_id = ? ")
						.append(filtroGpo)
						.append("and a.cm_clnt_name like ? and cm_actv_flag = ? ")
						.append("group by a.cm_clnt_id, a.cm_clnt_name, B.AM_PE_SITE_ID").toString();
				
				psmt = con.prepareStatement(query);
				
				//psmt.setString(1, siteId);
				psmt.setString(1, groupClientId);
				psmt.setString(2, clientName+"%");
				psmt.setString(3, "A");
			}

			//psmt = con.prepareStatement(query);
			/*AccessLog.Log(query);
			AccessLog.Log("siteId "+siteId);
			AccessLog.Log("clientName "+clientName);
			AccessLog.Log("clientId1 "+clientId1);
			AccessLog.Log("tipoCliente "+tipoCliente);
			AccessLog.Log("groupClientId "+groupClientId);*/

			rs = psmt.executeQuery();
			String clientName1 = "";
			String clientId = "";
			String siteName = "";

			while (rs.next()) {
				clientId = rs.getString(1);
				clientName1 = rs.getString(2);
				siteName = rs.getString(3);
				
				values.put("clientName", clientName1);
				values.put("clientId", clientId);
				values.put("siteName", siteName);
				
				result.add(values.clone());
				values.clear();
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getClientGpoAsig()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getClientGpoAsig()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getUserClientAsig(Connection con, String clientID, String nomUser) {
		HashMap values = new HashMap(2);
		ArrayList result = new ArrayList();

		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			String query = cnct
					.delete(0, cnct.length())
					.append("SELECT a.CU_USER_ID, b.CU_USER_NAME FROM SYS_CLNT_USER_WEB a, SYS_CLNT_USER b WHERE a.CU_CLNT_ID = ? and a.CU_USER_ID = b.CU_USER_ID AND b.CU_USER_NAME LIKE ?")
					.toString();

			psmt = con.prepareStatement(query);

			psmt.setString(1, clientID);
			psmt.setString(2, nomUser+"%");

			rs = psmt.executeQuery();

			while (rs.next()) {
				values.put("userId", rs.getString(1));
				values.put("userName", rs.getString(2));
				result.add(values.clone());
				values.clear();
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getUserClientAsig()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}

				if (psmt != null) {
					psmt.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getUserClientAsig()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}	
}