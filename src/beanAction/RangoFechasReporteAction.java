package beanAction;


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

import bean.RangoFechasReporteRecords;
import beanForm.RangoFechasReporteForm;
import beanUtil.ConnectDB;

public class RangoFechasReporteAction extends Action{
	StringBuffer concat = new StringBuffer();
	
	public ActionForward perform(ActionMapping map,ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response){
		String page= null;
		long currenttime=0;
		Connection con = null;
		try{
			HttpSession session = request.getSession(false);
			con = ConnectDB.getConnection();
			
			String clientId = (String)session.getAttribute("sClientId");
			RangoFechasReporteForm cr3f = (RangoFechasReporteForm) form;
			String fromDate = cr3f.getStartDate();
			String toDate = cr3f.getEndDate();
			
			RangoFechasReporteRecords rrr = new RangoFechasReporteRecords();
			ArrayList values = rrr.fetchRecords(con,clientId,fromDate,toDate);
			String cadenaArchivo = "";
			if (values!=null) {
				if (!values.isEmpty()) {
					for (int i = 0; i < values.size(); i++) {
						cadenaArchivo = concat.delete(0, concat.length())
								.append(cadenaArchivo)
								.append( ((ArrayList) values.get(i) ).get(0).toString() ).toString();
					}
				}
			}
			new bean.FileExport(request.getRealPath("/")).writeFile(request, cadenaArchivo, getTimeStamp(con).getTime());

			page = "showRecords";												
		} catch(Exception e) {
			AccessLog.Log(e);
		} finally {
			try {
				if (con != null) 
					con.close();
			} catch (Exception e) {
				AccessLog.Log(e);
			}
		}
		request.setAttribute("currenttime",String.valueOf(currenttime));
		return map.findForward(page);
	}
	
	private java.sql.Timestamp getTimeStamp(Connection con)throws Exception{
		PreparedStatement pst = con.prepareStatement("select sysdate from dual");//Checked
		ResultSet rs = pst.executeQuery();
		java.sql.Timestamp sysTimeStamp =null;
		if(rs.next())
			sysTimeStamp=rs.getTimestamp(1);

		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		return sysTimeStamp;
	}
}