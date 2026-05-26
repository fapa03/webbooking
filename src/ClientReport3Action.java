

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.http.*;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import bean.Global;
import beanUtil.ConnectDB;

public class ClientReport3Action extends Action{
	public ClientReport3Action(){
		//AAP//AccessLog.Log("Inside ClientReport3Action ...");
	}
	public ActionForward perform(ActionMapping map,ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response){
		String page= null;
		long currenttime=0;
		Connection con = null;
		try{
			//AAP//AccessLog.Log("INSIDE PERFORM METHOD ");
			response.setContentType("application/pdf");			
			HttpSession session = request.getSession(false);
			con = ConnectDB.getConnection();
			
			String clientId = (String)session.getAttribute("sClientId");
			ClientReport3Form cr3f = (ClientReport3Form) form;
			String fromDate = cr3f.getStartDate();
			String toDate = cr3f.getEndDate();
			
			//AAP//AccessLog.Log("Rama ...... startdate for client report "+fromDate);
			//AAP//AccessLog.Log("Rama ...... enddate for client report "+toDate);
			
			
			ClientReport3Records crr = new ClientReport3Records();
			HashMap values = crr.fetchRecords(con,clientId,fromDate,toDate);
			

			ClientReport3 cr3 = new ClientReport3();
			//String fileName = context.getRealPath("/")+clientId+"Report3.pdf";
			
			//Added by rama
			File f=new File(request.getRealPath("/")+File.separator+"ClientReport");
            //File f=new File(request.getContextPath()+File.separator+"ClientReport");
			if(!f.exists())
				f.mkdirs();
			String exportdirectory=f.toString();
			currenttime = getTimeStamp(con).getTime();
			String fileName = exportdirectory+File.separator+"ClientReport_"+currenttime+".pdf";
			//AAP//AccessLog.Log("CURRENT FOR CLIENT REPORT IN ACTION "+currenttime);
			Global global = (Global)session.getAttribute("sGlobal");//Added by rama
			//cr3.getReport(con,values,fileName,fromDate,toDate,global.clientName);//Changed by rama
			cr3.getReport(con,values,fileName,fromDate,toDate,global.clientName,global.displayAmountFlag);//Changed on 01/12/2009
			page = "showRecords";
												
		}
		catch(Exception e){
			AccessLog.Log(e);
		}
		finally{
			//Added By rama
			try{
				if(con != null) 
					con.close();
			}
			catch(Exception e){
				AccessLog.Log(e);
			}
		}
		//AAP//AccessLog.Log("BEFORE RETURNING PERFORM METHOD ");
		request.setAttribute("currenttime",String.valueOf(currenttime));
		return map.findForward(page);
	}
	public java.sql.Timestamp getTimeStamp(Connection con)throws Exception{
		PreparedStatement pst = con.prepareStatement("select sysdate from dual");//Checked
		ResultSet rs = pst.executeQuery();
		java.sql.Timestamp sysTimeStamp =null;
		if(rs.next())
			//sysDate = rs.getString(1);
			sysTimeStamp=rs.getTimestamp(1);

		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		//AAP//AccessLog.Log("SYSDATE WITH TIME STAMP IN SERVICES ACTION "+sysTimeStamp);
		return sysTimeStamp;
	}
}