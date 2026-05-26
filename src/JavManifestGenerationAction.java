/**
 * File Name    : GuiaDetailAction.java
 * Description  :This is the Action Class For LoginForm
 *				 to handle and Control the Inputs of that FormBean. 
 * Date Written :  28-Feb-2003
 * @author 	    :  D.SivaKumar
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * ----------------------------------------------------------------- 
 * Clave: AAP02
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 04/07/2013
 * Descripción: se agregó variable formaPago. para guardar resultado 
 * obtenido de consulta de guias FxC pendientes de manifiesto
 * se agregó objeto mgf.getFormaPago() para obtener y almacenar el valor de "formaPago" 
 * para realizar consulta de guias pendientes de generar manifiesto segun lo seleccionado ('PAID' o 'TO_PAY')
 * ----------------------------------------------------------------- 
 * Clave: AAP03
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 08/07/2013
 * Descripción: Se eliminó insercion de tabla WEB_CTRL_EMAIL en la generacion de manifiesto, 
 * ahora se realizará en la generacion de la guía.
 * ----------------------------------------------------------------- 
 */ 
 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bean.Global;
import bean.JavManifestGenerationForm;
import bean.Resources;
import beanUtil.ConnectDB;
public class JavManifestGenerationAction extends Action {
	private StringBuffer cnct = new StringBuffer();	
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	/** Creates a new instance of LoginAction */
	public JavManifestGenerationAction() {		
		//AAP//AccessLog.Log("Inside JavManifestGenerationAction....");
	}
	
	@SuppressWarnings("rawtypes")
	public ActionForward perform(ActionMapping am, ActionForm af,
			HttpServletRequest request, HttpServletResponse response) {
		//System.out.println("entro a accion generar manifiesto");
		//int all = 2;
		JavManifestGenerationForm mgf = null;
		JavManifestGenerationBean mgb = null;
		Connection con = null;
		String returnPage = "success";
		HttpSession session = null;
		ArrayList manifestDetails = null;
		ArrayList bookedGuiasDetails = null;
		String strGuiaNumbers;
		String strOperation = "Normal";
		String stroriginClientId = "";
		String strorginBranchId = "";
		//String formaPago = "PAID";//AAP02
		//int packageValue = 0;
		try {
			
			
			// Get the Connection Object...			
			con = ConnectDB.getConnection();
			con.setAutoCommit(false);// Added By Siva on 15-May-2003
			session = request.getSession(true);

			// This Will redirect to Error Page If AnyBody Directly Visiting a
			// Page With Enterring the System thru Login..
			if (session == null || session.isNew()) {
				return am.findForward("nosession");
			}

			
			stroriginClientId = (String) session.getAttribute("sClientId");
			strorginBranchId = (String) session.getAttribute("sAssignedBranch");
			//strorginSite = (String) session.getAttribute("sSiteId");
			
			if (af instanceof JavManifestGenerationForm) {

				mgf = (JavManifestGenerationForm) af;
				mgb = new JavManifestGenerationBean();
				//System.out.println("mgf.getManifiestoType() "+mgf.getManifiestoType());
				//System.out.println("mgf.getFormaPago() "+mgf.getFormaPago());
				//System.out.println("mgf.getDocuType() "+mgf.getDocuType());
				//mgb.getCreditLimit(con, mgf, stroriginClientId);//AAP03				
				
				strOperation = mgf.getOperation();

				

				request.setAttribute("tituloPantalla", "GENERACION DE MANIFIESTOS");
				//manifestDetails = mgb.getManifestDetails(con, mgf, stroriginClientId, strorginBranchId, request, selection, mgf.getFormaPago());//AAP02
				manifestDetails = mgb.getManifestDetails(con, mgf, stroriginClientId, strorginBranchId, request, (Global)session.getAttribute("sGlobal"));//AAP04

				request.setAttribute("sManifestDetails", manifestDetails);
				session.setAttribute("sManifestGenerationForm", mgf);

				if (strOperation != null && strOperation.equalsIgnoreCase("Print")) {
					bookedGuiasDetails = mgb.getBookedGuiasDetails(con, stroriginClientId, strorginBranchId, mgf.getFormaPago());//AAP02
					request.setAttribute("sBookedGuiasDetails", bookedGuiasDetails);
					return am.findForward("printbookedguias");
				}

				// Generated the Manifest
				if (strOperation != null && strOperation.equalsIgnoreCase("Generate")) {
					// Insert the Records...
					strGuiaNumbers = mgf.getClickedItems();
					if (strGuiaNumbers != null) {
//						if (strBranchLocType.equalsIgnoreCase("IN")) {
//							mgf.setManifiestoType("I");
//						} else if (selection.length() > 0) {
//							if (selection.equals("IN")) {
//								mgf.setManifiestoType("I");
//							} else {
//								mgf.setManifiestoType("B");
//							}
//						} else if (packageValue == 0) {
//							mgf.setManifiestoType("B");
//						} else if (packageValue == 1) {
//							mgf.setManifiestoType("I");
//						}
						
						/*valida si hay guias con manifiesto generado*/
						ArrayList guias = mgb.validGuiasBokGuiaHead(con, mgf, stroriginClientId, strGuiaNumbers, strorginBranchId);//AAP05
						
						if (guias.isEmpty()) {
							
							int insCount = mgb.insertToManifestDetail(session, con, mgf, stroriginClientId, strGuiaNumbers, strorginBranchId); 
							// Branch Id hot Coded here..  Get the Value For it From Session and Pass it..
							
							if (insCount > 0) {
								// disable the Generate Button And Modify the Guia Status in Bok_guia_Stus And Update Gh_guia_Refr_no in Bok_guia_head
								//mgb.updateBokGuiaHead(con, mgf, stroriginClientId, strGuiaNumbers, strorginBranchId);//AAP04
								mgb.updateBokGuiaHead(con, mgf, stroriginClientId, strGuiaNumbers, strorginBranchId, (Global)session.getAttribute("sGlobal"));//AAP04

								//int inscttCount = mgb.insertToComCTT(con, mgf, stroriginClientId, strorginBranchId);//AAP03
								int inscttCount = mgb.insertToComCTT(con, mgf, stroriginClientId, strorginBranchId, (Global)session.getAttribute("sGlobal"));//AAP04
								//JavLoginForm loginForm = (JavLoginForm) session.getAttribute("loginForm");

								/*
								 * valida que tenga activada la bandera de envios de
								 * correo para insertar las lineas a la tabla de
								 * control de envios
								 */
								/*//AAP03
								 * if (loginForm.getSendEmail().trim().equals("Y")) {//AAP01
									mgb.insertControlMails(con, mgf.getManifestNumber());//AAP01
								}//AAP01
								*/
								if (inscttCount > 0) {
									mgf.setDisableGenerate("true");
									mgb.insertToBokGuiaStatus(con, mgf, strGuiaNumbers, stroriginClientId);
									request.setAttribute("sDisabledFlag", "true");
								}
							} else {
								request.setAttribute("sDisabledFlag", "false");
								mgf.setDisableGenerate("false");
							}
						} else {
							cnct.delete(0, cnct.length())
							.append("Guias invalidas para generar Manifiesto. Ya se encuentran incluidas en manifiesto las siguientes guias: ");
							for (int i=0; i<guias.size(); i++) {								
								cnct.append( ((ArrayList)guias.get(i)).get(0).toString() )
								.append(", ");
							}
							mgf.setMsjErr(cnct.substring(0, cnct.length()-2).toString());
						}
					}
				}

				// Added by rama
				String systemDate = getSystemDate(con, session);
				long systemLongDate = new java.util.Date(systemDate).getTime();	

				request.setAttribute("systemdate", systemDate);
				request.setAttribute("systemlongdate", String.valueOf(systemLongDate));
			}

		} catch (Exception e) {
			request.setAttribute("sDisabledFlag", "notgenerated");
			if (mgf != null) {
				mgf.setDisableGenerate("false");
				mgf.setManifestNumber("");
			}
			try {
				if (con != null) {
					con.rollback();
				}
			} catch (Exception ex) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error1:").append(ex).toString());
			}
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error2:").append(e).toString());
			e.printStackTrace();
			
		} finally {
			// Close the Database Resources...
			request.setAttribute("emailfax", "emailfax");
			resources.cerrarConexion(con);
		}
		return am.findForward(returnPage);
	}

	public String getSystemDate(Connection con, HttpSession session) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String systemDate = "";
		try {
			Global global = (Global) session.getAttribute("sGlobal");
			String query = "select to_char(sysdate+pack_web.fun_ftch_time_diff(?)/86400,'dd/mm/yyyy hh24:mi')from dual";
			pst = con.prepareStatement(query);
			pst.setString(1, global.assignedBranch);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				systemDate = rs.getString(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getSystemDate()_Error2:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}		
		return systemDate;
	}
}