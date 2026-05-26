
/*----------------------------------------------------------
Author					:	V.Ramachandran
Date					:	25-March-2003
FileName				:	JavWebBookingServicesDetailEditAction1.java
SessionVariables		:
Other Used Classes		:
Function Names			:
------------------------------------------------------------*/

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bean.ConsultaParametros;
import bean.JavWebBookingServicesDetailAddForm1;
import bean.ShipmentServiceDetail;
import beanUtil.ConnectDB;


public class JavWebBookingServicesDetailEditAction1 extends Action {
	
	public JavWebBookingServicesDetailEditAction1() {
		//AAP//AccessLog.Log("JavWebBookingServicesDetailEditAction1");
	}
	public ActionForward perform(ActionMapping am, ActionForm af, 
								 HttpServletRequest req, HttpServletResponse res)
	{
		Connection con=null;
		String returnpage="thispage";
		try{
			
			if(af instanceof JavWebBookingServicesDetailEditForm1){
				
				HttpSession session = req.getSession(false);
				//For No Session
				if(session==null || session.isNew())
				{
					return am.findForward("nosession");
				}
				con = ConnectDB.getConnection();
				String action = req.getParameter("action");
				
				
				String clientId = (String)session.getAttribute("sClientId");
				if(clientId==null )
					return am.findForward("nosession");
				
				if(action !=null && action.equalsIgnoreCase("edit")){
					/*obtiene configuracion de peso maximo permitido*/
					ConsultaParametros consulta = new ConsultaParametros();
					ArrayList sysParmMstr = consulta.QryMdulTypeParm1(con, "BOK", "LIM_TOP_PESO_BG","G");
					//System.out.println("&%&%&%&%&%&%&%&%& peso maximo "+((ArrayList)sysParmMstr.get(0)).get(2).toString());
					String pesoMax = ((ArrayList)sysParmMstr.get(0)).get(2).toString();
					
					
					String selectedIndex = req.getParameter("radioselect");
					String hitCount = req.getParameter("hitcount");
					
					if(hitCount!=null)
						session.setAttribute("sHitCount",hitCount);
					int index=-1;
					if(selectedIndex!=null)
						index = Integer.parseInt(selectedIndex);
					
					ArrayList servicesDetail = (ArrayList)session.getAttribute("servicesDetailDefault");
					ShipmentServiceDetail ssd = (ShipmentServiceDetail)servicesDetail.get(index);
					JavWebBookingServicesDetailAddForm1 addForm = new JavWebBookingServicesDetailAddForm1();
					addForm.setCantidad(ssd.cantidad);
					addForm.setContenido(ssd.contenido);
					addForm.setDescripcion(ssd.descripcion);
					addForm.setPeso(ssd.peso);
					addForm.setVolumen(ssd.volumen);
					addForm.setImporte(ssd.importe);
					addForm.setDescripcioncode(ssd.descripcionCode);
					addForm.setSs_srvc_id(ssd.serviceId);
					addForm.setSs_refr_srvc_id(ssd.refServiceId);
					addForm.setDescripcioncode(ssd.descripcionCode);
					addForm.setPesoMax(pesoMax);
					
					req.setAttribute("action","edit");
					req.setAttribute("arrayindex",String.valueOf(index));
					req.setAttribute("webBookingservicesdetailadd1",addForm);
					session.setAttribute("editclicked","yes");//For Button validation while clicking delete in EDITDELETE screen
					returnpage="webbookingservicesdetailadd1";
				}else if(action !=null && action.equalsIgnoreCase("delete")){
					String selectedIndex = req.getParameter("radioselect");					
					String hitCount = req.getParameter("hitcount");
					//AccessLog.Log("SELECTED INDEX IN EDIT FORM "+selectedIndex);
					//AccessLog.Log("HITCOUNT IN EDIT FORM "+hitCount);
					
					if(hitCount!=null)
						session.setAttribute("sHitCount",hitCount);
					
					int index=-1;
					if(selectedIndex!=null)
						index = Integer.parseInt(selectedIndex);					
					
					ArrayList servicesDetail = (ArrayList)session.getAttribute("servicesDetailDefault");
					
					//code added by sundar on 26/09/2003 for displaying description and content by default
					int size=servicesDetail.size();				    
				    		
					if(index==0 && size>=2){						
					ShipmentServiceDetail ssd = (ShipmentServiceDetail)servicesDetail.get((index+1));
					String desc=ssd.descripcion;
					String cont=ssd.contenido;
					session.setAttribute("sdescription",desc);
					session.setAttribute("scontent",cont);					
					}
					else if(index==0 && size==1){
					session.setAttribute("status","new");					
					
					}
					
					
					servicesDetail.remove(index);
					session.setAttribute("servicesDetailDefault",servicesDetail);
					
					int newHitCount = Integer.parseInt(hitCount)-1;
					session.setAttribute("sHitCount",String.valueOf(newHitCount));
					
					returnpage="thispage";
				}
			}
			
		}catch(Exception e){
			AccessLog.Log(e);
		}finally{
			try{
				if(con!=null)
					con.close();
			}catch(Exception ex){
				AccessLog.Log(ex);
			}
		}
		return am.findForward(returnpage);
	}
	
}