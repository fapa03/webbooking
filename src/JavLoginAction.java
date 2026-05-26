/**
 * @author: D.SivaKumar
 * Fecha de Creación: 25-Feb-2003
 * Compañía: KUMARAN.
 * Descripción del programa: This is the Action Class For JavLoginForm
 *				 to handle and Control the Inputs of that FormBean.
 * FileName: Global.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 04/06/2012
 * Descripción: Se agregó lógica para obtener indicador de tarifa nueva o vieja 
 * se formateó codigo y se metieron algunas mejoras en los metodos 
 * (eliminar throws exception, cerrar en bloque finally los objetos de base de datos, etc).
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción: 	
 * ------------------------------------------------------------------
 */

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bean.ConsultaParametros;
import bean.Global;
import bean.Resources;
import beanUtil.ConnectDB;

public class JavLoginAction extends Action {

	private final String msgError = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private StringBuffer concatena = new StringBuffer();
	private Resources resources = new Resources();
	
	@SuppressWarnings("rawtypes")
	public ActionForward perform(ActionMapping am, ActionForm af,
			HttpServletRequest request, HttpServletResponse response) {

		AccessLog.appRealPath = request.getRealPath("/");// added by B.Emerson on 30/04/2004

		Connection con = null;
		HttpSession session = null;
		Global global = new Global();

		String strClientId;
		String strTarifType;
		String strClasifTarif;// AAP01
		String strAssignedBranch;
		String strAssignedSite;
		String strAdditionalServiceFlag;
		String strDisplayAmountFlag;
		String strBrncIdWeb; 

		String returnPage = "";
		String lFlag = null;
		PreparedStatement timeDiffPst = null;
		ResultSet timeDiffRs = null;
		String useUrl = "";
		try {
			if (request.getRemoteAddr().trim().substring(0,3).equals("0:0") || 
					request.getRemoteAddr().trim().substring(0,8).equals("192.168.") || 
					request.getRemoteAddr().trim().substring(0,7).equals("172.16.")) {
				useUrl = "INT";//interno
			} else {
				useUrl = "EXT";//externo
			}
			session = request.getSession(true);
						
			if ((session == null || session.isNew()) && request.getParameter("loginNameTmp")==null)
				return am.findForward("nosession");

			con = ConnectDB.getConnection();
			if (af instanceof JavLoginForm) {

				JavLoginForm lf = (JavLoginForm) af;
				lf.setReadOnly("After Test");
				lf.setUseUrl(useUrl);
				String strLogout = request.getParameter("logout");
				if (strLogout != null && strLogout.equals("yes")) {
					lFlag = "logout";
					request.setAttribute("sLoginFlag", lFlag);
					session.removeAttribute("sClientId");
					session.removeAttribute("servicesDetailDefault");
					session.removeAttribute("servicesDetail");
					session.removeAttribute("count");
					session.removeAttribute("defaultservicescreen");
					session.removeAttribute("sHitCount");
					session.removeAttribute("addresscode");
					session.removeAttribute("servicesGlobal");
					session.removeAttribute("webBookinggeneral");
					session.removeAttribute("webBookingservices");
					session.removeAttribute("serFormHit");
					session.removeAttribute("editclicked");
					session.removeAttribute("envelopealone");
					// code added by sundar on 29/10/2003 for description and
					// content display
					session.removeAttribute("serviceid");
					session.removeAttribute("referserviceid");
					session.removeAttribute("servicecode");
					session.removeAttribute("status");
					session.removeAttribute("sdescription");
					session.removeAttribute("scontent");
					// Added by R.Mohan Babu(10/06/2004) in order to free the
					// value that is set in the valorvalue during logout.
					session.removeAttribute("valorvalue");
					// code added by palanivel to remove the session variable
					session.removeAttribute("aditionalServicesDetail");

					// Added by Sam[21-06-2006], romoving servicestotal from
					// session , Fixing the wrong data displaying when first
					// time adding additional servive
					session.removeAttribute("servicestotal");

					session.removeAttribute("webBookinggeneralMain");
					session.removeAttribute("calculatedservicelist");
					// End

					// set empty for login page .....
					lf.setLoginName("");
					lf.setPassword("");
					if (con != null) {
						con.close();
					}
					return am.findForward("failed");
				}

				// Create Object For Login Validate Class..Where Login
				// Validation Taken Place...
				// added by bala
				lf.setUserValidate("");
				// ended by bala
				
				if (request.getParameter("loginNameTmp")!=null) {
					
					/*Se agrega funcionalidad para poder obtener parametro de usuario con decode de UTF-8, para respetar caracteres especiales (�)*/
					String queryString = request.getQueryString();
					String decoded = URLDecoder.decode(queryString, "UTF-8");
					String[] pares = decoded.split("&");
					Map<String, String> parameters = new HashMap<String, String>();
					for(String pare : pares) {
					    String[] nameAndValue = pare.split("=");
					    parameters.put(nameAndValue[0], nameAndValue[1]);
					}
					String loginNameTmp = parameters.get("loginNameTmp");					
					
					lf.setLoginName(loginNameTmp);
					
					if (request.getParameter("passwordTmp")!=null) {//indicador que viene de customer Central
						lf.setPassword(parameters.get("passwordTmp"));
					} else {
						lf.setPassword("");
					}
				}
				
				if (request.getParameter("milliSeconds")!=null) {//indicador que viene de customer Central
					lf.setMilliSeconds(request.getParameter("milliSeconds").toString());
				} else {
					lf.setMilliSeconds("");
				}

				String token = request.getParameter("token")!= null ? 
					request.getParameter("token") : "";
				lf.setToken(token);

				lf.setLoginName(lf.getLoginName().toUpperCase());
				lf.setPassword(lf.getPassword());

				LoginValidate lv = new LoginValidate();
				String forward = lv.validate(con, lf);
				if (con.isClosed()) {
					con = ConnectDB.getConnection();
				}
				global = lv.getGlobal(); // For Putting ClientName in Session

				if (forward.equalsIgnoreCase("success")) {
					lFlag = "yes";
					// Put All the Global variable in Session.
					request.setAttribute("sLoginFlag", lFlag);
					request.setAttribute("showPopup", lf.getShowPopup());
					returnPage = "success";
				} else {					
					lFlag = "no";
					if (lf.getMsjeVal().trim().length()>0) {
						lFlag = lf.getMsjeVal();
					}
					request.setAttribute("sLoginFlag", lFlag);
					returnPage = "failed";
				}
				// Set the Session values...
				strClientId = lf.getClientId() != null ? lf.getClientId() : "";
				strTarifType = lf.getTarifType();
				strAssignedBranch = lf.getAssignedBranch();
				strAssignedSite = lf.getAssignedSite();
				strAdditionalServiceFlag = lf.getAdditionalServiceFlag();
				strDisplayAmountFlag = lf.getDisplayAmountFlag();
				strBrncIdWeb = lf.getBrncIdWeb(); 
						
				session.setAttribute("sClientId", strClientId);
				session.setAttribute("sAssignedBranch", strAssignedBranch);
				session.setAttribute("branchid", strAssignedBranch);//AAP VARIABLE PARA GUIAS DE PREPAGO
				session.setAttribute("branchName", getBranchName(con, strAssignedBranch));//AAP VARIABLE PARA GUIAS DE PREPAGO
				session.setAttribute("sSiteId", lf.getAssignedSite());
				session.setAttribute("siteid", lf.getAssignedSite());//AAP VARIABLE PARA GUIAS DE PREPAGO
				session.setAttribute("sTarifType", strTarifType);
				session.setAttribute("sAdditionalServiceFlag", strAdditionalServiceFlag);
				session.setAttribute("sDisplayAmountFlag", strDisplayAmountFlag);
				
				// Client type Added by rama
				String clientType = null;
				clientType = getClientType(con, strClientId);
				global.setClientType(clientType);

				global.setDisplayAmountFlag(strDisplayAmountFlag);
				global.setClientId(strClientId.toUpperCase());
				global.setAssignedBranch(strAssignedBranch);
				global.setBrncIdWeb(strBrncIdWeb);
				global.setAssignedSite(strAssignedSite);
				global.setAdditionalServiceFlag(strAdditionalServiceFlag);
				global.setTarifType(strTarifType);
				
				/*
				 * se obtiene de configuracion si tarifa es nueva (1) o vieja
				 * (0)
				 */// AAP01
				ConsultaParametros cons = new ConsultaParametros();// AAP01
				ArrayList temp = cons.QryMdulTypeParm1(con, "WEB", "TARIF_TYPE", strTarifType);// AAP01
				/*
				 * si no encuentra configuracion, deja por default como tarifa
				 * antigua (0)
				 */
				strClasifTarif = temp.isEmpty() ? "0" : ((ArrayList) temp .get(0)).get(3).toString();// obteniendo PM_VLUE2_ID//AAP01

				global.setClasifTarif(strClasifTarif);// AAP01
				// Get the time difference;
				String timeQuery = "select pack_web.fun_ftch_time_diff(?) from dual";
				timeDiffPst = con.prepareStatement(timeQuery);
				timeDiffPst.setString(1, strAssignedSite);
				timeDiffRs = timeDiffPst.executeQuery();

				if (timeDiffRs.next()) {
					global.setTimediff(timeDiffRs.getLong(1) * 1000); // TimeDifference in milliseconds
				}
				session.setAttribute("sGlobal", global);
				getGroupClientId(con, strClientId, session);
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyy H:m");
				global.setServerDate(sdf.format(new java.util.Date()));
				session.setAttribute("sGlobal", global);
				session.removeAttribute("webBookinggeneralMain");
			}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgError).append("perform()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(timeDiffRs, timeDiffPst);
			resources.cerrarConexion(con);
		}
		return am.findForward(returnPage);
	}

	public void getGroupClientId(Connection con, String clientId,
			HttpSession session) {
		Global global = (Global) session.getAttribute("sGlobal");
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String groupIdQuery = "select pack_web.FUN_CHCK_GRUP_CLNT(?)as groupid,to_char(sysdate,'DD/MM/YYYY HH24:MI') from dual";
			pst = con.prepareStatement(groupIdQuery);
			pst.setString(1, clientId);
			rs = pst.executeQuery();
			String groupClientId = "";
			String sysDate = "";
			while (rs.next()) {
				groupClientId = rs.getString(1);
				sysDate = rs.getString(2);
			}
			global.setGroupClientId(groupClientId);
			global.setSysDate(sysDate);

			session.setAttribute("sGlobal", global);

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgError).append("getGroupClientId()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (Exception e) {
				AccessLog.Log(concatena.delete(0, concatena.length()).append(msgError).append("getGroupClientId()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
	}

	public String getClientType(Connection con, String clientId) {
		String clientType = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = "SELECT CM_CLNT_TYPE FROM SYS_CLNT_MSTR WHERE CM_CLNT_ID = ?";
			pst = con.prepareStatement(query);
			pst.setString(1, clientId);

			rs = pst.executeQuery();

			if (rs.next()) {
				clientType = rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length())
					.append(msgError).append("getGroupClientId()_Error:")
					.append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (Exception e) {
				AccessLog.Log(concatena.delete(0, concatena.length())
						.append(msgError).append("getGroupClientId()_Error:")
						.append(e).toString());
				e.printStackTrace();
			}
		}
		return clientType;
	}
	
	public String getBranchName(Connection con, String branchId) {
		String branchName = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = "select pack_web.fun_ftch_brnc_name(bm_brnc_id) from sys_brnc_mstr where bm_brnc_id = ?";
			pst = con.prepareStatement(query);
			pst.setString(1, branchId);

			rs = pst.executeQuery();

			if (rs.next()) {
				branchName = rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length())
					.append(msgError).append("getBranchName()_Error:")
					.append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (Exception e) {
				AccessLog.Log(concatena.delete(0, concatena.length())
						.append(msgError).append("getBranchName()_Error:")
						.append(e).toString());
				e.printStackTrace();
			}
		}
		return branchName;
	}
}