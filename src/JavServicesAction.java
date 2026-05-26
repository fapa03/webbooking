
/**
 * @author: kumaran
 * Fecha de Creación: 
 * Compañía: KUMARAN.
 * Descripción del programa: Bean accion para pantalla de servicios en la consulta de manifiestos.
 * FileName: JavServicesAction.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 04/07/2013
 * Descripción: Se agregó variable formaPago para mantener el valor seleccionado en la consulta de manifiestos.
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import beanUtil.ConnectDB;

public class JavServicesAction extends Action{
	public ActionForward perform(ActionMapping map,ActionForm form,HttpServletRequest request,HttpServletResponse response){
		Connection con = null;
		String formaPago = "";//AAP01
		String docuType = "";//AAP02
		try{
    //create a httpSession object for accession session
			HttpSession session = request.getSession(true);
		    if(session == null || session.isNew()){
		        return map.findForward("nosession");
		    }
			
			if (request.getParameter("formaPago") != null && request.getParameter("formaPago").length() > 0) {// AAP01
				formaPago = request.getParameter("formaPago");// AAP01
			}// AAP01
			
			if (request.getParameter("docuType") != null && request.getParameter("docuType").length() > 0) {// AAP02
				docuType = request.getParameter("docuType");// AAP02
			}// AAP02
			//get connection from the connectdb class
			con = ConnectDB.getConnection();

			JavServicesForm serForm = (JavServicesForm) form;
			serForm.setFormaPago(formaPago);//AAP01
			serForm.setDocuType(docuType);//AAP02
			//getting the guia number from the session
			String guiaNumber = request.getParameter("guiaNumber");
			if(guiaNumber ==null)
				guiaNumber="";
			serForm.setHidGuiaNumber(guiaNumber);

			//String guiaNumber = "111333";
			serForm.setHidGuiaNumber(guiaNumber);
			
			//getting the additional service display flag
			String additionalSeriviceFlag = (String) session.getAttribute("sAdditionalServiceFlag");
			//AccessLog.Log("INSIDE JAVSERVICES flag "+additionalSeriviceFlag);
			//for the time being hardcoded the value has to be fetched from the main screen
			//String additionalSeriviceFlag = "Y";
			// creating an instance for shipmentbean class
			ShipmentBean shipmentRecords = new ShipmentBean();
			//fetching the records from the shipmentbean class and storing the records in an arraylist
			ArrayList records = shipmentRecords.fecthShipmentRecords(guiaNumber,con);
			//store the records in a session
			request.setAttribute("SHIPMENT",records);
			//redirect according to the flag
			
			if(additionalSeriviceFlag.equalsIgnoreCase("Y")){
				ArrayList addServ = shipmentRecords.fetchAdtitionalServiceRecords(guiaNumber,con);
				ArrayList addReq = shipmentRecords.fetchRequirement(guiaNumber,con);
				//String success = shipmentRecords.fetchContactInfo(guiaNumber,serForm,con);
                shipmentRecords.fetchContactInfo(guiaNumber,serForm,con);
				request.setAttribute("additionService",addServ);
				request.setAttribute("additionRequirement",addReq);
				//AAP//AccessLog.Log("COMMENT  "+serForm.getComment());
				//AAP//AccessLog.Log("addinfo  "+serForm.getAddInfo());
				return map.findForward("service1");
			}
			else{
				ArrayList addReq = shipmentRecords.fetchAdtitionalServiceRecords(guiaNumber,con);        
				//String success = shipmentRecords.fetchContactInfo(guiaNumber,serForm,con);
                shipmentRecords.fetchContactInfo(guiaNumber,serForm,con);
				request.setAttribute("additionRequirement",addReq);
				return map.findForward("service2");
			}     	
		}
		catch(Exception e){
			AccessLog.Log(e);
		}
		finally{
			try{
				if(con != null)
				  con.close();
			}
			catch(SQLException se){
				AccessLog.Log(se);
			}
		}
		return map.findForward("service1");
	}
}
