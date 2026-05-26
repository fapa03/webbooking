
/**
 * Source code
 * Author Angshuman Debnath
 * Created 2/3/2003
 */


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
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

import beanUtil.ConnectDB;


public class JavManifestDetailsAction extends Action{
	private StringBuffer cnct = new StringBuffer();	
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	public JavManifestDetailsAction()
	{
		//AAP//AccessLog.Log("JavManifestDetailAction .................");
	}


	/**
	* This is the method called on by ActionServlet
	* when a request is made.
	*/
	@SuppressWarnings("rawtypes")
	public ActionForward perform(ActionMapping mapping,
			    	     	   ActionForm form,
			    	     	   HttpServletRequest request,
			    	     	   HttpServletResponse response
			    	     	  )

	{
		Connection con = null;
		String strOperation=null;
		
		if( request.getSession(false) == null)
			mapping.findForward("nosession");
		try{
			JavManifestDetailsForm mdform = (JavManifestDetailsForm) form;
			HttpSession session = request.getSession(false);
			String strClientId = (String) session.getAttribute("sClientId");
			con = ConnectDB.getConnection();
            String manifestNumber = request.getParameter("genManifestNumber");
            if(request.getAttribute("MANIFEST_NUMBER") != null){
            	manifestNumber = request.getAttribute("MANIFEST_NUMBER").toString();
            }
            if(manifestNumber != null){
				request.getSession(false).setAttribute("MANIFEST_NUMBER", manifestNumber);
            }
            if (request.getSession(false).getAttribute("MANIFEST_NUMBER") == null) {
            	mapping.findForward("nosession");
            }
			JavManifestDetail mftFetchDetails = new JavManifestDetail();
			mftFetchDetails.getManifestDetail(con, mdform, request);
//			String tipoMnft = mftFetchDetails.getManifestDetail(con, mdform, request);
//			String forward = "printmanifestdetails";
//			
//			if (tipoMnft.length()>0 && tipoMnft.equals("OR")){
//				forward = "printOrderDetails";
//			}
			
			strOperation = mdform.getOperation();
			//AAP//AccessLog.Log("Operation in ManifestDetails.."+strOperation);
			if(strOperation !=null && strOperation.equalsIgnoreCase("Print")){
				
				JavManifestDetail printManifestDetails = new JavManifestDetail();
			    printManifestDetails.printManifestDetail(con, mdform, request);
				
				java.util.ArrayList defaultAddress = getDefaultAddress(con,strClientId);
				request.setAttribute("defaultaddress",defaultAddress);
				
				//return mapping.findForward(forward);
				return mapping.findForward("printOrderDetails");
			}
			
		}catch(Exception e){
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error1:").append(e).toString());
		}
		 finally{
			 try{
				 if (con!=null)
					 con.close();
			 }catch(Exception ee){
				 AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error2:").append(ee).toString());
			 }
		 }
		
		return mapping.findForward("thispage");
	}
	
	
	//Added by Siva
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getDefaultAddress(Connection con,
									   String destinationClientId)throws Exception{
		CallableStatement cst = null;
		ResultSet rs = null;
		//ResultSet crs = null;
		PreparedStatement pst = null;
		HashMap values = null;
		ArrayList result = new ArrayList();
		
		String addressType="CLNT";
		String defaultAddressFlag = "Y";
		String query = "SELECT AM_ADDR_CODE,AM_DRNR,AM_STRT_NAME, "+
					   "       AM_PHNO1,AM_SUIT_NO,AM_FLOR_NO,"+
					   "       AM_ADDR_STYP,AM_ADDR_DEFN_TYPE,"+
					   "       AM_ADDR_REF_NO,AM_GETY_LEVL,"+
					   "       AM_GETY_TYPE,AM_GETY_CODE "+
					   "  FROM SYS_ADDR_MSTR "+
					   " WHERE AM_ENTY_ID=?"+
					   "   AND AM_ADDR_TYPE=?"+
					   "   AND AM_DEFA_FLAG=?";
		
		pst = con.prepareStatement(query);
		pst.setString(1,destinationClientId);
		pst.setString(2,addressType);
		pst.setString(3,defaultAddressFlag);
		
		rs=pst.executeQuery();
		String AM_ADDR_CODE=null,AM_DRNR=null,AM_STRT_NAME=null,AM_PHNO1=null,AM_SUIT_NO=null,AM_FLOR_NO=null,
			AM_ADDR_STYP=null,AM_ADDR_DEFN_TYPE=null,AM_ADDR_REF_NO=null,AM_GETY_LEVL=null,AM_GETY_TYPE=null,AM_GETY_CODE=null;
		values = new HashMap();
		while(rs.next()){
			AM_ADDR_CODE  =(rs.getString(1)== null?"":rs.getString(1));
			AM_DRNR    =(rs.getString(2)== null?"":rs.getString(2));
			AM_STRT_NAME  =(rs.getString(3)== null?"":rs.getString(3));
			AM_PHNO1   =(rs.getString(4)== null?"":rs.getString(4));
			AM_SUIT_NO   =(rs.getString(5)== null?"":rs.getString(5));
			AM_FLOR_NO   =(rs.getString(6)== null?"":rs.getString(6));
			AM_ADDR_STYP  =(rs.getString(7)== null?"":rs.getString(7));
			AM_ADDR_DEFN_TYPE =rs.getString(8);
			AM_ADDR_REF_NO  =rs.getString(9);
			AM_GETY_LEVL  =rs.getString(10);
			AM_GETY_TYPE  =rs.getString(11);
			AM_GETY_CODE  =rs.getString(12);
			
			cst = con.prepareCall( "{call pack_web.pro_ftch_addr(?,?,?,"+
								   "?,?,"+
								   "?,?,?,"+
								   "?,?,?,?,?,"+
								   "?,?,?,?,?,?) }");      
			cst.setString(1,AM_ADDR_DEFN_TYPE);
			cst.setString(2,AM_ADDR_REF_NO);
			cst.setString(3,AM_GETY_CODE);
			cst.setString(4,AM_GETY_LEVL);
			cst.setString(5,AM_GETY_TYPE);
			
			cst.registerOutParameter(6,Types.VARCHAR);
			cst.registerOutParameter(7,Types.VARCHAR);
			cst.registerOutParameter(8,Types.VARCHAR);
			cst.registerOutParameter(9,Types.VARCHAR);
			cst.registerOutParameter(10,Types.VARCHAR);
			cst.registerOutParameter(11,Types.VARCHAR);
			cst.registerOutParameter(12,Types.VARCHAR);
			cst.registerOutParameter(13,Types.VARCHAR);
			cst.registerOutParameter(14,Types.VARCHAR);
			cst.registerOutParameter(15,Types.VARCHAR);
			cst.registerOutParameter(16,Types.VARCHAR);
			cst.registerOutParameter(17,Types.VARCHAR);
			cst.registerOutParameter(18,Types.VARCHAR);
			cst.registerOutParameter(19,Types.VARCHAR);
			
			//crs=cst.executeQuery();
			cst.executeQuery();
			//if(crs.next()){				
				values.put("AM_ADDR_CODE",AM_ADDR_CODE);
				values.put("AM_DRNR",AM_DRNR);
				values.put("AM_STRT_NAME",AM_STRT_NAME);
				values.put("AM_PHNO1",AM_PHNO1); 
				values.put("AM_SUIT_NO",AM_SUIT_NO);
				values.put("AM_FLOR_NO",AM_FLOR_NO);
				values.put("AM_ADDR_STYP",AM_ADDR_STYP);
				values.put("AM_ADDR_DEFN_TYPE",AM_ADDR_DEFN_TYPE);
				values.put("AM_GETY_LEVL",AM_GETY_LEVL);
				values.put("AM_GETY_TYPE",AM_GETY_TYPE);
				values.put("AM_GETY_CODE",AM_GETY_CODE);
				values.put("u11",(cst.getString(6)== null?"":cst.getString(6)));
				values.put("u12",(cst.getString(7)== null?"":cst.getString(7)));
				values.put("u13",(cst.getString(8)== null?"":cst.getString(8)));
				values.put("u14",(cst.getString(9)== null?"":cst.getString(9)));
				values.put("u15",(cst.getString(10)== null?"":cst.getString(10)));
				values.put("u16",(cst.getString(11)== null?"":cst.getString(11)));
				values.put("u17",(cst.getString(12)== null?"":cst.getString(12)));
				values.put("Zipcode",(cst.getString(13)== null?"":cst.getString(13)));
				values.put("c11",(cst.getString(14)== null?"":cst.getString(14)));
				values.put("c12",(cst.getString(15)== null?"":cst.getString(15)));
				values.put("c13",(cst.getString(16)== null?"":cst.getString(16)));
				values.put("c14",(cst.getString(17)== null?"":cst.getString(17)));
				values.put("c15",(cst.getString(18)== null?"":cst.getString(18)));
				values.put("c16",(cst.getString(19)== null?"":cst.getString(19)));
				result.add(values.clone());
				values.clear();
			//}
			//if (crs != null)
				//crs.close();
			
			cst.close();
		}
		if (rs != null)
			rs.close();
		//if (crs != null)
			//crs.close();
		if(pst != null){
			pst.close();
		}
		if(cst != null){
			cst.close();
		}
		return result;
	}
	
}