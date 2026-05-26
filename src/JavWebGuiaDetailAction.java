
/**
 * File Name    : GuiaDetailAction.java
 * Description  :This is the Action Class For LoginForm
 *				 to handle and Control the Inputs of that FormBean. 
 * Date Written :  28-Feb-2003
 * @author 	    :  D.SivaKumar
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 04/07/2013
 * Descripción: Se agregó variable formaPago para mantener el valor seleccionado en la consulta de manifiestos.
 * ------------------------------------------------------------------ 
 */

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import beanUtil.ConnectDB;
public class JavWebGuiaDetailAction extends Action {
	
	/** Creates a new instance of LoginAction */
	public JavWebGuiaDetailAction() {
	}
	
	public ActionForward perform(ActionMapping am,ActionForm af,HttpServletRequest request,HttpServletResponse response)
		throws ServletException,IOException{
		Connection  con=null;		
		String returnPage="success";
		//String  strGuiaNo=null;
		String  pstrGuiaNo=null;	
		//String pstrOrginBranchId=null;
		String fromFlag=null;
		HttpSession session=null;
		String formaPago = "";//AAP01
		String docuType = "";//AAP02
		try{
			// Get  the Connection Object...
			con = ConnectDB.getConnection();
			session=request.getSession(true);	
			
			if(session==null || session.isNew())
			 return am.findForward("nosession");			
			
			if ( request.getParameter("formaPago") != null && request.getParameter("formaPago").length()>0) {//AAP01
				formaPago = request.getParameter("formaPago");//AAP01
			}//AAP01
			
			if ( request.getParameter("docuType") != null && request.getParameter("docuType").length()>0) {//AAP02
				docuType = request.getParameter("docuType");//AAP02
			}//AAP02
			if (af instanceof JavWebGuiaDetailForm){
				
				JavWebGuiaDetailForm gf = (JavWebGuiaDetailForm)af;				
				WebGuiaDetailDataBean gdb = new WebGuiaDetailDataBean();
				//strGuiaNo = gf.getGuiaNumber();
				
				gf.setFormaPago(formaPago);//AAP01
				gf.setDocuType(docuType);//AAP02
				
				fromFlag=request.getParameter("fromFlag");
			    //AAP//AccessLog.Log("QUERY String FRomFlag"+fromFlag);
				if(fromFlag!=null){
					gf.setHidFromFlag(fromFlag);
				}				
				pstrGuiaNo = request.getParameter("hidGuiaNumber");
				
				if(pstrGuiaNo==null)
					pstrGuiaNo = "";
				
				//AccessLog.Log("pstrGuiaNo "+pstrGuiaNo);
				
				request.setAttribute("guiaNumber",pstrGuiaNo);
				
				if(session !=null ){				
					session.setAttribute("sFromFlag",fromFlag);						
				}		
				
				// pDisplay the WEBGUIADETAILS...					
				gdb.showGuiaDetails(con,gf,pstrGuiaNo);		
				
			}
		}catch(Exception e){			
			AccessLog.Log("Exception From  JavWebGuiaDetailsAction...");			
			AccessLog.Log(e.getMessage());
			AccessLog.Log(e.toString());
			e.printStackTrace();
		} finally {	
			 //Close the Database Resources...
			 try {
				 if (con!=null)
					 con.close();
			 } catch(Exception e) {
				 AccessLog.Log(e);
			 }
		 }		
		return am.findForward(returnPage);
	}
	
}

