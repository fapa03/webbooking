
/**
 * File Name    : GuiaDetailAction.java
 * Description  :This is the Action Class For LoginForm
 *				 to handle and Control the Inputs of that FormBean. 
 * Date Written :  28-Feb-2003
 * @author 	    :  D.SivaKumar
 */


import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;

import bean.Global;
import bean.JavManifestGenerationForm;
import beanUtil.ConnectDB;

import java.io.IOException;
import java.sql.*;

import logger.AccessLog;
public class JavManifestGenerationAction1 extends Action {
	
	/** Creates a new instance of LoginAction */
	public JavManifestGenerationAction1() {
		
		//AAP//AccessLog.Log("Inside JavManifestGenerationAction....");
	}
	
	public ActionForward perform(ActionMapping am,ActionForm af,HttpServletRequest request,HttpServletResponse response)
		throws ServletException,IOException{

	
		int all=2;//added by amal on 21-aug-2006 
		JavManifestGenerationForm mgf=null;
		JavManifestGenerationBean1 mgb=null;
		Connection  con=null;		
		String returnPage="success";		
		HttpSession session=null;
		ArrayList manifestDetails=null;
		ArrayList bookedGuiasDetails=null;
		String strGuiaNumbers;			
		//StringTokenizer st=null ;
		//PrintWriter out=null;
		String strOperation="Normal";
		String stroriginClientId="";
		String strorginBranchId="";		
		//double ClientCreditLimit=0.0;
		//String sumAmount;
		try{
			// Get  the Connection Object...
			
			con = ConnectDB.getConnection();
			con.setAutoCommit(false);//Added By Siva on 15-May-2003
			session=request.getSession(true);

			//This Will redirect to Error Page If AnyBody Directly Visiting a  Page With Enterring the System thru Login..
			
			if(session==null || session.isNew())
				return am.findForward("nosession");
			
			String selection="";
			String showmodel="";
			//Added By Amal on 21-Aug-2006 (Purpose: --> To Get The Manifest Type Option say Border or Internal
			if(request.getParameter("txt_selection")!=null)
			{
			selection=request.getParameter("txt_selection");
			session.setAttribute("selection",selection);
			}
			if(request.getParameter("show")!=null)
			showmodel=request.getParameter("show");
			
			
			
			// Get the WebClientId From the Session...
			
			if(session!=null){
				stroriginClientId=(String)session.getAttribute("sClientId");
				strorginBranchId=(String)session.getAttribute("sAssignedBranch");
			}
			
			
			if (af instanceof JavManifestGenerationForm){
				
				mgf = (JavManifestGenerationForm)af;			
				mgb = new JavManifestGenerationBean1();
				//Added by Sam.D.Jabeen on [31-May-2005] for Setting the Source Branch Location Type
				//ClientCreditLimit=mgb.getCreditLimit(con,mgf,stroriginClientId);
				
                mgb.getCreditLimit(con,mgf,stroriginClientId);
				
				//sumAmount=mgf.getBookedGuiaAmt();
				// if(sumAmount !=null)
				// request.setAttribute("sBookedGuiaAmt",sumAmount);
				
				// Get the Manifest Details...
				String strBranchLocType =  mgb.getBranchLocationType(strorginBranchId,con);
				mgf.setClientBranchLocType(strBranchLocType);
				if(strOperation==null)
										strOperation="Normal";
									else 
										strOperation=mgf.getOperation();

				//Added By Amal on 21-Aug-2006 (Purpose: --> To pass the Manifest Type Selection made
									//by User to all the Record Pages  
				if((strOperation!=null)&&(strOperation.length()>0))
			    {
				  if(session.getAttribute("selection") != null)
					  selection = (String)session.getAttribute("selection");
			    }       
				//Added By Amal on 21-Aug-2006 (Purpose: -->To check whether popup needs to be opened or not for Getting manifest type 
				if((selection.length()<=0)&& (showmodel.equalsIgnoreCase("true")))
				{
				if(strBranchLocType.equalsIgnoreCase("BR"))
				{
				int value=mgb.getType(con,strorginBranchId,stroriginClientId);
				if(value==all)
				return am.findForward("showmodel");
				}
				}
				//Added By Amal on 21-Aug-2006 (Purpose: --> Database column has only one char for manifesttype
				if(selection.length()>0)
				{
					if(selection.equals("IN"))
					mgf.setManifiestoType("I");
					else
					mgf.setManifiestoType("B");
				}			
				
				manifestDetails=mgb.getManifestDetails(con,mgf,stroriginClientId,strorginBranchId,request,selection);				
				
				request.setAttribute("sManifestDetails",manifestDetails);
				session.setAttribute("sManifestGenerationForm",mgf);							
				
				//AccessLog.Log("..... Operation "+strOperation);				
				//AccessLog.Log("..... Operation after "+strOperation);
				
				if(strOperation !=null && strOperation.equalsIgnoreCase("Print")){
					
					bookedGuiasDetails=mgb.getBookedGuiasDetails(con,mgf,stroriginClientId,strorginBranchId,request);
					request.setAttribute("sBookedGuiasDetails",bookedGuiasDetails);					
					return am.findForward("printbookedguias");
				}
				
				// Generated the Manifest
				
				if(strOperation!=null && strOperation.equalsIgnoreCase("Generate")){
					

					// Insert  the Records...
					strGuiaNumbers=mgf.getClickedItems();
					if(strGuiaNumbers!=null){
						//AccessLog.Log("hidden Field ClickedItems Values..."+strGuiaNumbers);
						int insCount=mgb.insertToManifestDetail(session,con,mgf,stroriginClientId,strGuiaNumbers,strorginBranchId); // Branch Id hot Coded here.. Get the Value For it From Session and Pass it..
						//AccessLog.Log("insCount in  Action..."+insCount);
						
						if(insCount>0){
							
							//disable the Genrate Button
							//And Modify the Guia Status in Bok_guia_Stus And Update Gh_guia_Refr_no in Bok_guia_head
							
							
							int updateinsCount=mgb.updateBokGuiaHead( con,mgf,stroriginClientId,strGuiaNumbers,strorginBranchId);
							//AAPif(updateinsCount>0)
								//AAP//AccessLog.Log("Guia Status Modified Successfully");
							int inscttCount=mgb.insertToComCTT(con,mgf,stroriginClientId,strorginBranchId);
							
							if(inscttCount>0){
								mgf.setDisableGenerate("true");
								//AccessLog.Log("Records Inserted to cmoctt..");
								mgb.insertToBokGuiaStatus(con,mgf,strGuiaNumbers,stroriginClientId);
								//AccessLog.Log("Guia Status Updated Successfully...");
								request.setAttribute("sDisabledFlag","true");
							}
							
							
						}else{
							request.setAttribute("sDisabledFlag","false");
							mgf.setDisableGenerate("false");
							
						}
						
					}
				}	
				
				//Added by rama
				String systemDate = getSystemDate(con,session);

                long systemLongDate = new java.util.Date(systemDate).getTime();//Commented by rama

                //long systemLongDate;
                //systemLongDate = new java.text.SimpleDateFormat().parse(systemDate).getTime();
                //AAP//AccessLog.Log("---------- LONG VALUE FOR THE SYSTEM DATE "+systemLongDate);
				//AAP//AccessLog.Log("---------- SYSTEM DATE "+systemDate);
				
				request.setAttribute("systemdate",systemDate);
				request.setAttribute("systemlongdate",String.valueOf(systemLongDate));
			}
			
		}catch(Exception e){
			request.setAttribute("sDisabledFlag","notgenerated");
			mgf.setDisableGenerate("false");
			AccessLog.Log(e.getMessage());
			AccessLog.Log(e.toString());
			AccessLog.Log(e);
			mgf.setManifestNumber("");
			try{
				con.rollback();
			}catch(Exception ex){
				AccessLog.Log(ex);
			}
		}finally{	
			//Close the Database Resources...
			try{
				if (con!=null)
					con.close();
			}catch(Exception e){
				AccessLog.Log(e);
			}
		}
		
		return am.findForward(returnPage);
	}
	
	public String getSystemDate(Connection con,HttpSession session)throws Exception {
		Global global = (Global)session.getAttribute("sGlobal");
		String query = "select to_char(sysdate+pack_web.fun_ftch_time_diff(?)/86400,'dd/mm/yyyy hh24:mi')from dual";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1,global.assignedBranch);
		ResultSet rs = pst.executeQuery();
		String systemDate="";
		if(rs.next()){
			systemDate = rs.getString(1);
		}
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		return systemDate;
	}
	
}