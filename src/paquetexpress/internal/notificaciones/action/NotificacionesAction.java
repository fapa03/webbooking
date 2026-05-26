package paquetexpress.internal.notificaciones.action;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import paquetexpress.internal.notificaciones.dao.NotificacionesDao;
import paquetexpress.internal.notificaciones.form.NotificacionesForm;

public class NotificacionesAction extends Action {
	private StringBuffer concatena = new StringBuffer();
	//private final String msgAvi  = concatena.delete(0, concatena.length()).append("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgErr  = concatena.delete(0, concatena.length()).append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		NotificacionesDao dao = new NotificacionesDao();
		String forward = "error";
		try {
			
			if (form instanceof NotificacionesForm) {
				NotificacionesForm notifForm = (NotificacionesForm) form;
				
				//System.out.println("notifForm.getNombre() "+notifForm.getNombre());
				//System.out.println("notifForm.geteMail() "+notifForm.getEmail());
				//System.out.println("notifForm.getRastreos() "+notifForm.getGuias());
				String[] guias = notifForm.getGuias().split("\n");
				ArrayList datosGuia = null;				
				int registro = 0;
		        for (int i = 0; i < guias.length; i++) {
		            //System.out.println("TRI SALTO LINEA "+guias[i].trim()+"|");
		            if (guias[i].trim().length()>0) {
		            	datosGuia = dao.validaGuia(guias[i].trim());
		            	if (!datosGuia.isEmpty()) {
		            		registro = registro + dao.insertGuia((ArrayList) datosGuia.get(0), notifForm.getNombre(), notifForm.getEmail());
		            	}
		            }
		        }
				
				if (registro >0) {					
					forward = "success";
				} else {
					forward = "error";
				}
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("perform()_Error:").append(e).append("\nCausa:").append(e.getCause()).toString());
			e.printStackTrace();
		}
		return mapping.findForward(forward);	
	}	
}
