/**
 * @author: Jose Manuel Armenta
 * Fecha de Creaci�n: 10/07/2017
 * Compa��a: PAQUETEXPRESS.
 * Descripci�n del programa: Bean accion para pantalla reimpresion de guias.
 * FileName: JavReprintGuiaAction.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 03/12/2018
 * Descripci�n: funcionalidad para marcar si quiere imprimir la cantidad de CP configuradas por cliente
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n: 
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n: 
 * 
 * ------------------------------------------------------------------ 
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n: 
 * 
 * ------------------------------------------------------------------ 
 */
package mx.com.paquetexpress.reprintGuia.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bean.Global;
import bean.PrintFileExport;
import bean.Resources;
import beanUtil.ConnectDB;
import logger.AccessLog;
import mx.com.paquetexpress.reprintGuia.dao.JavReprintGuiaDao;
import mx.com.paquetexpress.reprintGuia.form.JavReprintGuiaForm;


public class JavReprintGuiaAction extends Action {
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	JavReprintGuiaDao dao;
	Time sysTime = null;
	private Resources resources = new Resources();
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) {
		JavReprintGuiaForm guiaForm = (JavReprintGuiaForm) form;
		String returnPage = "thispage";
		HttpSession session = request.getSession(true);
		Global global = (Global) session.getAttribute("sGlobal");
		String linkGenCartaPorte = ConnectDB.getCartaPorteExt() + "GenCartaPorte?trackingNoGen=" ;

		// if session expired ask him to login once again
		if (session == null || session.isNew()) { 
			return mapping.findForward("nosession"); 
		}
		
		request.setAttribute("linkGenCartaPorte", linkGenCartaPorte); //Necesario para poder permitir la visualizacion de la guia con el linkGenCartaPorte
		if (guiaForm.getCurrentTask().equalsIgnoreCase("start")) {
			dao = new JavReprintGuiaDao();
			guiaForm = dao.getGuiasByClient((String) session.getAttribute("sClientId"));
			session.setAttribute("listGuias", guiaForm.getListGuias());
			request.setAttribute("javReprintGuiaForm", guiaForm);
			request.setAttribute("cadena", "xxx");
			

		} else if(guiaForm.getCurrentTask().equalsIgnoreCase("print")){
			int SSTotal = 1;//AAP01
			
			String[] guias = request.getParameterValues("Guias");
			String[] etiquetas = request.getParameterValues("etiquetas");
			String[] checkedGuias = request.getParameterValues("checkGuia");
			String[] checkedEtiquetas = request.getParameterValues("checkEtiquetaDtl");
						
			guias = guias != null ? guias : new String[0];
			etiquetas = etiquetas != null ? etiquetas : new String[0];
			checkedGuias = checkedGuias != null ? checkedGuias : new String[0];
			checkedEtiquetas = checkedEtiquetas != null ? checkedEtiquetas : new String[0];
			
			if(checkedGuias.length > 0 	|| checkedEtiquetas.length > 0) {
				request.setAttribute("errormsgprint", ""); 
				if (guiaForm.isCheckSStotal()) {//AAP01
					SSTotal = global.getSStotal() > 1 ? global.getSStotal() : SSTotal;//AAP01
				}//AAP01
				dao = new JavReprintGuiaDao();
				String cadenaImpresion = dao.print(guias,etiquetas, checkedGuias, checkedEtiquetas, global.getClientIdAgreement(), SSTotal/*AAP01*/);
				request.setAttribute("cadena", cadenaImpresion);
				
				if (!global.getAllowPrintQZ().equalsIgnoreCase("Y")) {
					try {
						new PrintFileExport(request.getSession().getServletContext().getRealPath("/"))
								.writeToGuiaFile(request, cadenaImpresion.toString(), getTimeStamp().getTime());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				guiaForm.setCurrentTask("download");
			}else {	
				//ingresa mensaje de error para poder seleccionar guias
				request.setAttribute("errormsgprint", "No se seleccionaron guias para imprimir"); 
				request.setAttribute("cadena", "xxx");
				return mapping.findForward(returnPage);
			}
		}
		
		
		return mapping.findForward(returnPage);
	}

	public java.sql.Timestamp getTimeStamp() {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		java.sql.Timestamp sysTimeStamp = null;
		try {
			con = ConnectDB.getConnection();
			pst = con.prepareStatement("select sysdate from dual");// Checked
			rs = pst.executeQuery();
			

			if (rs.next())
				// sysDate = rs.getString(1);
				sysTime = rs.getTime(1);
			sysTimeStamp = rs.getTimestamp(1);

			if (rs != null)
				rs.close();
			if (pst != null)
				pst.close();	
		} catch (Exception e) {
			AccessLog.Log(msgErr+"getTimeStamp() Error:"+e);
		} finally {
			resources.closeResources(rs, pst);
			resources.cerrarConexion(con);
			
		}
		
		return sysTimeStamp;
	}
}
