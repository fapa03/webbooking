/**
 * @author: kumaran
 * Fecha de Creación: 
 * Compañía: KUMARAN.
 * Descripción del programa: Bean de accion para pantalla de historial de manifiestos.
 * FileName: JavServicesForm.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 04/07/2013
 * Descripción: Se agregó modificacion para ejecutar la consulta que verifica si hay
 * guias fxc pendientes de generar manifiesto.
 * ------------------------------------------------------------------
 * Clave: 
 * Autor:
 * Fecha:
 * Descripción:
 * ------------------------------------------------------------------
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bean.Global;
import beanUtil.ConnectDB;

public class JavShipmentHistoryAction extends Action{
	private StringBuffer cnct = new StringBuffer();	
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	
	@SuppressWarnings("rawtypes")
	public ActionForward perform(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){		
		Connection con = null;
		try{
			HttpSession session = request.getSession(true);
      		if(session == null || session.isNew()){
        		return mapping.findForward("nosession");
      		}
			
			JavShipmentHistoryForm manifestForms =(JavShipmentHistoryForm) form;		
			
			
			String strOperation = manifestForms.getOperation();			
			con = ConnectDB.getConnection();
			
			con.setAutoCommit(false); // Added By Siva on 15-May-2003
			String clientId = (String)session.getAttribute("sClientId");//AAP01
			String branchId = (String)session.getAttribute("sAssignedBranch");//AAP01
			Global global = (Global)session.getAttribute("sGlobal");//AAP02
			JavFetchManifestRecords manifestRecords = new JavFetchManifestRecords();
			
			manifestForms.setFlagManifestWE(global.getManifestTypeWE());
			
			if (strOperation.equals("getDetails") || strOperation.equals("Print")) {//AAP01
				
				String datetime = manifestRecords.getSystemDate(con);
	            session.setAttribute("sysdate",datetime);
				String deleteGuiaDetails = manifestForms.getDeleteGuiaNumber();
	        	String genManifest = manifestForms.getGenManifestNumber();
	            
				
				//Added by rama
				String returnVal = request.getParameter("returnval");
				String manfestdate=request.getParameter("manifestdate");
				
				if(returnVal!=null && returnVal.equalsIgnoreCase("true")){
					manifestForms.setManifestDate(manfestdate);
					request.setAttribute("returnval","true");
				}
				
				if(genManifest != null && genManifest.length() != 0) {
					session.setAttribute("MANIFEST_NUMBER",genManifest);
					return mapping.findForward("manifestdetails");
				}
				
				if(deleteGuiaDetails != null && deleteGuiaDetails.length() != 0){	
					manifestRecords.deleteManifestRecord(con,deleteGuiaDetails,clientId, global.getOrigenUserClave());
					manifestForms.setDeleteGuiaNumber(null);
				}
				
				if(strOperation.equalsIgnoreCase("Print")){
					ArrayList printnotSentResult = manifestRecords.printManifestNotSent(con,manifestForms,clientId,branchId);
					ArrayList printSentResult = manifestRecords.printManifestSent(con,manifestForms,clientId,branchId);
					request.setAttribute("manifestdate",manifestForms.getManifestDate());
					request.setAttribute("printSentValues",printSentResult);
					request.setAttribute("printNotsentValues",printnotSentResult);
					return mapping.findForward("printshipmenthistorylog");
				}
	 		    ArrayList sentResult = manifestRecords.fetchManifestSent(con,manifestForms,clientId,branchId);
	      		ArrayList notSentResult = manifestRecords.fetchManifestNotSent(con,manifestForms,clientId,branchId);
				request.setAttribute("sentValues",sentResult);
				request.setAttribute("notSentValues",notSentResult);
			} 
				
			if (manifestForms.getFiltroPor().equals("")){
				manifestForms.setFiltroPor("2");
			}

				ArrayList dirOrigen = null;
				
				if (manifestForms.getFiltroPor().equals("1")){// filtra todas las direccion de centros de costo a donde tiene acceso el usuario
					dirOrigen = manifestRecords.consultaGuiasSinMnftDirRecoleccionCCostos(con, global, clientId);
				} else if  (manifestForms.getFiltroPor().equals("3")){//se filtra todas las direcciones que contiene guias WE
					dirOrigen = manifestRecords.consultaGuiasSinMnftDirRecoleccionWE(con, global, clientId);
				}else {
					// filtra las direccion de centros de costo donde solo ha generado gu�as el usuario
					if (global.getOrigenUserClave() == null) {
						return mapping.findForward("nosession");
					}
					dirOrigen = manifestRecords.consultaGuiasSinMnftDirRecoleccion(con, global, clientId);
				}
				
				if (dirOrigen.isEmpty()) {
					manifestForms.setGenManifestValue("0");
					manifestForms.setGenManifestLabel("NO HAY GUIAS PENDIENTES");
				} else {
					String dirText = "";
					String dirCode = "";
					manifestForms.getGenManifestLabel().clear();
					manifestForms.getGenManifestValue().clear();
					for (int i =0; i<dirOrigen.size();i++) {
						dirCode = ((HashMap)dirOrigen.get(i)).get("GA_ADDR_CODE").toString();
						dirText = cnct.delete(0, cnct.length())
								.append(((HashMap)dirOrigen.get(i)).get("GA_STRT_NAME").toString())
								.append(", ")
								.append(((HashMap)dirOrigen.get(i)).get("GA_DRNR").toString())
								.append(", ")
								.append(((HashMap)dirOrigen.get(i)).get("GA_ADDR_LIN6").toString())
								.append(", C.P. ")
								.append(((HashMap)dirOrigen.get(i)).get("GA_ZIP_CODE").toString())
								.append(", ")
								.append(((HashMap)dirOrigen.get(i)).get("GA_ADDR_LIN4").toString())
								.append(", ")
								.append(((HashMap)dirOrigen.get(i)).get("GA_ADDR_LIN3").toString())
								.append(", ")
								.append(((HashMap)dirOrigen.get(i)).get("GA_ADDR_LIN1").toString())
								.toString();
						manifestForms.setGenManifestValue(dirCode);
						manifestForms.setGenManifestLabel(dirText);						
					}
				}
			return mapping.findForward("shipmenthistorylog");
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch(SQLException e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return mapping.findForward("shipmenthistorylog");
	}
}