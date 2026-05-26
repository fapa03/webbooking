
/*----------------------------------------------------------
Author					:	V.Ramachandran
Date					:	25-March-2003
FileName				:	JavWebBookingServicesAditionalAction.java
SessionVariables		:
Other Used Classes		:
Function Names			:
------------------------------------------------------------*/

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import beanUtil.ConnectDB;

public class JavWebBookingServicesAditionalAction extends Action {
	public JavWebBookingServicesAditionalAction() {
		//AAP//AccessLog.Log("JavWebBookingServicesAditionalAction");
	}
	public ActionForward perform(ActionMapping am, ActionForm af, 
								 HttpServletRequest req, HttpServletResponse res)
	{
		Connection con=null;
		try{
			
			if(af instanceof JavWebBookingServicesAditionalForm){
				con = ConnectDB.getConnection();
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
		return am.findForward("thispage");		
	}	
}