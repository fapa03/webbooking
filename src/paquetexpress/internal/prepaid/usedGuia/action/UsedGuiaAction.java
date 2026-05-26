package paquetexpress.internal.prepaid.usedGuia.action;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import paquetexpress.internal.prepaid.usedGuia.dao.UsedGuiaDao;
import paquetexpress.internal.prepaid.usedGuia.form.UsedGuiaForm;

public class UsedGuiaAction extends Action {
	private StringBuffer concatena = new StringBuffer();
	//private final String msgAvi  = concatena.delete(0, concatena.length()).append("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgErr  = concatena.delete(0, concatena.length()).append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		HttpSession session = request.getSession(false);
		UsedGuiaDao dao = new UsedGuiaDao();
		String forward = "start";
		try {
			//System.out.println("entró a accion" );
			if(session==null){
				return mapping.findForward("nosession");
				// return mapping.findForward("nosession");
			}
			String clientId = (String)session.getAttribute("sClientId");
			//For Nosession
			if (clientId==null) {
				return mapping.findForward("nosession");
			}
			if (form instanceof UsedGuiaForm) {
				ArrayList result = null;
				
				UsedGuiaForm monitorForm = (UsedGuiaForm) form;

				forward = monitorForm.getCurrentTask();
				if (monitorForm.getCurrentTask().equals("start")) {
					result = dao.obtieneSetRegistros(session);
					request.setAttribute("conjuntoGuias", result);
				} else if (monitorForm.getCurrentTask().equals("loadSetDetail")) {
					result = dao.obtieneSetRegistros(session);
					request.setAttribute("conjuntoGuias", result.clone());				
								
					result = dao.obtieneDetalleRegistro(monitorForm.getIdSetSel());					
					request.setAttribute("detalleGuias", result);
					
				} /*else if (monitorForm.getCurrentTask().equals("mainpage")) {
					
				}*/
			}			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("perform()_Error:").append(e).append("\nCausa:").append(e.getCause()).toString());
			e.printStackTrace();
		}
		return mapping.findForward(forward);	
	}

}
