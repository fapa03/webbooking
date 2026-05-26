

/*----------------------------------------------------------
Author					:	V.Ramachandran
Date					:	24-March-2003
FileName				:	JavWebBookingGeneralAction.java
SessionVariables		:
Other Used Classes		:
Function Names			:
------------------------------------------------------------*/

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bean.Global;
import bean.JavWebBookingGeneralForm;
import bean.JavWebBookingServicesForm;
import bean.ServicesGlobal;
import beanUtil.ConnectDB;

public class JavWebBookingGeneralAction extends Action {
	
	public JavWebBookingGeneralAction() {
		//AAP//AccessLog.Log("JavWebBookingGeneral Action");
	}
	
	public ActionForward perform(ActionMapping am, ActionForm af, 
								 HttpServletRequest req, HttpServletResponse res) {
		Connection con=null;
		
		try{
			
			if(af instanceof JavWebBookingGeneralForm){
				
				JavWebBookingGeneralForm aform = (JavWebBookingGeneralForm)af;
				con = ConnectDB.getConnection();				
				HttpSession session = req.getSession(true);
				//	For No Session
				if(session==null || session.isNew()){
					return am.findForward("nosession");
				}
				Global global = new Global();
				global = (Global)session.getAttribute("sGlobal");				

				//AAP//AccessLog.Log(aform.getDestinationsitecode());
				//AAP//AccessLog.Log(aform.getOrgioncode());
				//AAP//AccessLog.Log("*******************");
				
				if(global==null)
					return am.findForward("nosession");
				
				String newDestinationBranch = aform.getDestinationcode();
				String to = req.getParameter("to");
				
				//Handling of request coming from Include Page link. 
				JavWebBookingGeneralForm aformnew =null;
				String includeattribute = req.getParameter("includeattribute");
				
				if(global.destinationBranchId !=null && 
				   !global.destinationBranchId.equals(newDestinationBranch)){
					aform.setBorderbranchcheck("");//For New Destination Border Branch
					session.removeAttribute("count");
					session.removeAttribute("sHitCount");
					session.removeAttribute("servicesDetail");
					session.removeAttribute("servicesDetailDefault");
					session.removeAttribute("defaultservicescreen");
					session.removeAttribute("addresscode");
					session.removeAttribute("servicesGlobal");
					session.removeAttribute("webBookingservices");//Services Form
					session.removeAttribute("serFormHit");
					session.removeAttribute("editclicked");
					session.removeAttribute("envelopealone");
					//code added by sundar on 29/10/2003 for description and content display
					session.removeAttribute("serviceid");
					session.removeAttribute("referserviceid");
					session.removeAttribute("servicecode");
					session.removeAttribute("status");
					session.removeAttribute("sdescription");
					session.removeAttribute("scontent");
            		//code added by palanivel to remove the session variable 
					session.removeAttribute("aditionalServicesDetail");
					global.commentText="";
					session.setAttribute("sGlobal",global);
					
					//For KMTarif Type(Tarif Calculation for non- default screen);
					//calculateKMTarifType(con,session,aform);--commented and coded the same next to "IsTariffSlabExists" by B.Emerson on 06/04/2004 
				}
				String page = req.getParameter("page");
				if(includeattribute !=null && includeattribute.equalsIgnoreCase("yes")){
					session.removeAttribute("webBookinggeneral");
					aformnew=new JavWebBookingGeneralForm();
					
					session.removeAttribute("servicesDetailDefault");
					session.removeAttribute("servicesDetail");
					session.removeAttribute("count");
					session.removeAttribute("defaultservicescreen");
					session.removeAttribute("sHitCount");
					session.removeAttribute("addresscode");
					session.removeAttribute("servicesGlobal");
					session.removeAttribute("webBookingservices");//Services Form
					session.removeAttribute("serFormHit");
					session.removeAttribute("editclicked");	
					session.removeAttribute("envelopealone");
					//code added by sundar on 29/10/2003 for description and content display
					session.removeAttribute("serviceid");
					session.removeAttribute("referserviceid");
					session.removeAttribute("servicecode");
					session.removeAttribute("status");
					session.removeAttribute("sdescription");
					session.removeAttribute("scontent");
					//code added by palanivel to remove the session variable 
					session.removeAttribute("aditionalServicesDetail");
					global.commentText="";
					session.setAttribute("sGlobal",global);
					
					//Redirection to Main Menu Page
					if(page!=null && page.length()>0){
						if(!page.equalsIgnoreCase("thispage"))
							return am.findForward(page);
					}
					
					//Getting Orgien Client Address
					getOrgionClientAddress(aformnew,con,session);
					aform = aformnew;
					session.setAttribute("webBookinggeneral",aform);
					
				}else{
					//Getting Orgien Client Address
					getOrgionClientAddress(aform,con,session);
				}
				//For Instansiating the Arraylist
				String servicecount = (String)session.getAttribute("count");
				if(servicecount ==null)
					session.setAttribute("count","new");
				
				global.destinationBranchId = aform.getDestinationcode();
				session.setAttribute("sGlobal",global);
				
				boolean tariffSlabExists = isTariffSlabExists(con,global);				
				
				//For KMTarif Type(Tarif Calculation for non- default screen);
				calculateKMTarifType(con,session,aform);

				if(tariffSlabExists)
					session.setAttribute("defaultservicescreen","yes");
				else
					session.setAttribute("defaultservicescreen","no");
				
				//Traversing to Services Screen
				String from = req.getParameter("from");
				
				String addressCode=req.getParameter("addresscode");
				if(addressCode!=null && addressCode.length()>0)
					session.setAttribute("addresscode",addressCode);
				
				//Border Branch Checking
				if(from!=null && from.length()>0){
					//AccessLog.Log("border bracnh"+aform.getDestinationcode());
					if(isBorderBranch(con,session,aform.getDestinationsitecode())){
						global.isDestinationBorderBranch=true;
					}
					else{
						global.isDestinationBorderBranch=false;
					}
					
					//AccessLog.Log("border bracnh"+aform.getOrginbranchcode());
					if(isBorderBranch(con,session,aform.getOrgioncode())){
						global.isOrigionBorderBranch=true;
					}
					else{
						global.isOrigionBorderBranch=false;
					}
					session.setAttribute("sGlobal",global);
				/*	if(!isShipmentDestinationExists(con,session,aform)){
						//AccessLog.Log("INSIDE isShipmentDestinationExists ");
						String errMsg = getShipErrorMessage(con);
						req.setAttribute("shipmentdestination",errMsg);
						return am.findForward("thispage");
					}*/
					
				
					//Border Branch Validation
					if(global.isOrigionBorderBranch && !global.isDestinationBorderBranch){
						if(aform.getBorderbranchcheck()!=null && aform.getBorderbranchcheck().length()==0){
							
							//AAP//AccessLog.Log("---------INSIDE BORDER BRANCH CHECKING-------------");
							aform.setBorderbranchcheck("true");
							return am.findForward("thispage");
						}
						
						if(aform.getBorderbranchcheck()!=null && aform.getBorderbranchcheck().equalsIgnoreCase("false")){
							//AAP//AccessLog.Log("INSIDE FALSE OF BORDER BRANCH ");
							if(from.equalsIgnoreCase("bookingdetail")){
								if(tariffSlabExists)
								{
									return am.findForward("webbookingdetaildefault"); 
								}
								else{									
									return am.findForward("webbookingdetail");
								}
							}else if(from !=null && from.equalsIgnoreCase("bookingservices")){
								String serFormHit = (String)session.getAttribute("serFormHit");							
								//AAP//AccessLog.Log("%%%%%%%%%%% SER FORM HIT IN GENERAL "+serFormHit);
								//Form Validation and Populating fields
								if(serFormHit==null){
									JavWebBookingServicesForm serForm = new JavWebBookingServicesForm();
									//String groupClientId = getGroupClientId(con,session);
									/*For Showing Delivery Type*/
									getServiceCovered(con,req,serForm);
									//For Acknoledgement Type
									fetchAcknowledgementTypeDesc(con,session,serForm);
									//Default Insurance Amount and Populating Insurance Type
									//AAP//AccessLog.Log("--------------------------------------");
									//AAP//AccessLog.Log("INSIDE SERVICES FORM CALCULATION ");
									serForm.setValordeclarado("1000");
									getInsuranceAmount(con,global,req,serForm);
									//getInsuranceType(con,"LTH",serForm);
									//serForm.setSeguro(getInsuranceInformation(con,"1000",groupClientId,serForm));
									session.setAttribute("serFormHit","yes");
									session.setAttribute("webBookingservices",serForm);
								}
								return am.findForward("webbookingservices");
							}
						}
					}
					
				}
				
				if(from !=null && from.equalsIgnoreCase("bookingdetail")){
					if(tariffSlabExists){
						return am.findForward("webbookingdetaildefault");
					}
					else{
						
						return am.findForward("webbookingdetail");
					}
				}else if(from !=null && from.equalsIgnoreCase("bookingservices")){
					String serFormHit = (String)session.getAttribute("serFormHit");
					//AAP//AccessLog.Log("-------------SER FORM HIT 2 "+serFormHit);
					//Form Validation and Populating fields
					if(serFormHit==null){
						JavWebBookingServicesForm serForm = new JavWebBookingServicesForm();
						//String groupClientId = getGroupClientId(con,session);
						/*For Showing Delivery Type*/
						getServiceCovered(con,req,serForm);
						//For Acknoledgement Type
						fetchAcknowledgementTypeDesc(con,session,serForm);
						//Default Insurance Amount and Populating Insurance Type
						serForm.setValordeclarado("1000");
						//getInsuranceType(con,"LTH",serForm);
						//serForm.setSeguro(getInsuranceInformation(con,"1000",groupClientId,serForm));
						getInsuranceAmount(con,global,req,serForm);
						session.setAttribute("serFormHit","yes");
						session.setAttribute("webBookingservices",serForm);
					}
					return am.findForward("webbookingservices");
				}
				String hitCount=req.getParameter("hitcount");
				String delCount=req.getParameter("delcount");
				
				if(to!=null && to.equalsIgnoreCase("bookinggeneral")){
					
					int intDelCount=-1;
					int intHitCount=-1;
					if(delCount!=null && delCount.length()>0){
						intDelCount = Integer.parseInt(delCount);
						intHitCount = Integer.parseInt(hitCount);
						intHitCount = intHitCount-intDelCount;
						hitCount = String.valueOf(intHitCount);
					}	
					
					if(hitCount!=null)
						session.setAttribute("sHitCount",hitCount);
					//AccessLog.Log("DEL COUNT "+intDelCount);
					//AccessLog.Log("HIT COUNT "+intHitCount);
					//AccessLog.Log(" ^^^^^^^^^^^^^^^^^ NEW HIT COUNT AFTER DELETION IN GENERAL ^^^^^^^^^^^^^^^^^^^^^^^^^ "+(String)session.getAttribute("sHitCount"));					
					aform = (JavWebBookingGeneralForm)session.getAttribute("webBookinggeneral");
					req.setAttribute("action","add");//For Disabling the Add Button If true.
					session.removeAttribute("editclicked");
					return am.findForward("thispage");
				}
			}
		}catch(Exception e){
			AccessLog.Log(e);
			/*java.io.StringWriter sw = new java.io.StringWriter();
			e.printStackTrace(new java.io.PrintWriter(sw));
			StringBuffer sbf = sw.getBuffer();
			AccessLog.Log(sw);*/
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
	
	public boolean isShipmentDestinationExists(Connection con,
											   HttpSession session,
											   JavWebBookingGeneralForm generalForm)throws Exception {
		
		Global global = (Global)session.getAttribute("sGlobal");
		String shipDestQuery = "{ call pack_web.pro_ship_dest_mstr(?,?,?,?,?,?,?) }";
		CallableStatement cst = con.prepareCall(shipDestQuery);
		cst.setString(1,global.assignedBranch);
		cst.setString(2,generalForm.getDestinationcode());
		cst.registerOutParameter(3,Types.NUMERIC);
		cst.registerOutParameter(4,Types.NUMERIC);
		cst.registerOutParameter(5,Types.VARCHAR);
		cst.registerOutParameter(6,Types.VARCHAR);
		cst.registerOutParameter(7,Types.VARCHAR);
		ResultSet rs = cst.executeQuery();
		
		String ship_seqn = null;
		String no_ship=null;
		String cur_loc=null;
		String cur_dest=null;
		String lc_rout=null;
		
		if(rs.next()){
			ship_seqn = String.valueOf(cst.getInt(3));
			no_ship = String.valueOf(cst.getInt(4));
			cur_loc = cst.getString(5);
			cur_dest= cst.getString(6);
			lc_rout= cst.getString(7);

		}
		if(rs!=null)
			rs.close();
		if(cst!=null)
			cst.close();
		if(cur_loc!=null){
			generalForm.setShip_seqn(ship_seqn);
			generalForm.setNo_ship(no_ship);
			generalForm.setCur_loc(cur_loc);
			generalForm.setCur_dest(cur_dest);
			generalForm.setLc_rout(lc_rout);
			return true;
		}
		else
			return false;
	}
	
	public String getShipErrorMessage(Connection con)throws Exception {		
		String query = "{call pack_web.pro_show_mesg(?,PACK_WEB.LANGUAGE_ID,?,?,NULL,NULL,NULL,?,?) }";
		CallableStatement cst =  con.prepareCall(query);
		cst.setString(1,"BOK");
		cst.setInt(2,9000191);
		cst.setInt(3,1);
		//cst.setString(4,"NULL");
		//cst.setString(5,"NULL");
		//cst.setString(6,"NULL");
		cst.registerOutParameter(4,Types.VARCHAR);
		cst.registerOutParameter(5,Types.VARCHAR);
		cst.executeQuery();
		//String msgType = cst.getString(4);
		String msgText = cst.getString(5);
		
		if(cst!=null)
			cst.close();
		return msgText;
	}
	
	
	public String getAcknowledgementType(Connection con,HttpSession session)throws Exception{
		PreparedStatement pst = con.prepareStatement("select  wc_ack_type from WEB_CLNT_MSTR where wc_trif_type =?");
		Global global = (Global)session.getAttribute("sGlobal");
		pst.setString(1,global.tarifType);
		ResultSet rs = pst.executeQuery();
		String ackType =null;
		if(rs.next())
			ackType=rs.getString(1);
		
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		
		return ackType;
	}
	
	public String getGouupClientId(Connection con,Global global)throws Exception{
		String groupIdQuery = "select pack_web.FUN_CHCK_GRUP_CLNT(?)as groupid from dual";
		PreparedStatement pst = con.prepareStatement(groupIdQuery);
		pst.setString(1,global.clientId);
		ResultSet rs = pst.executeQuery();
		String groupClientId="";
		while(rs.next()){
			groupClientId = rs.getString("groupid");
		}
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		//AAP//AccessLog.Log("GROUPCLIENTID IN WEBBOOKING GENERAL "+groupClientId);
		return groupClientId;
	}
	
	public ArrayList fetchAcknowledgementTypeDesc(Connection con,String ackTypeIndex,HttpSession session)throws Exception{
		
		Global global = (Global)session.getAttribute("sGlobal");
		String query=null;
		boolean checkTariffType=true;
		if(global.tarifType.equalsIgnoreCase("G")){			
			query=	"	SELECT pm_vlue1_desc FROM sys_parm_mstr "+
					"	WHERE pm_parm_type = ? "+
					"	AND pm_parm_code1 = ?";
		}
		else{
			query=	"	SELECT pm_vlue1_desc FROM sys_parm_mstr "+
					"	WHERE pm_parm_type = ?";
			checkTariffType=false;
		}
		
		PreparedStatement pst = con.prepareStatement(query);
		if(checkTariffType){
			pst.setString(1,"ACK_TYPE");
			pst.setString(2,ackTypeIndex);
		}else{
			pst.setString(1,"ACK_TYPE");
		}
		
		ResultSet rs = pst.executeQuery();
		ArrayList ackTypeDesc=new ArrayList();
		
		while(rs.next()){
			ackTypeDesc.add(rs.getString(1));
		}
		
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		
		return ackTypeDesc;
	}
	
	
	
	public void getOrgionClientAddress(JavWebBookingGeneralForm aform,Connection con,
									   HttpSession session)throws Exception{
		
		
		CallableStatement cst = null;
		ResultSet rs = null;
		PreparedStatement pst = null;

		Global global = new Global();
		global = (Global)session.getAttribute("sGlobal");
		String strClientId = global.clientId;		
		String strAssignedSite=global.assignedSite;
		String strAssignedBranch=global.assignedBranch;
		//AAP//AccessLog.Log("global.assignedBranch"+global.assignedBranch);
		aform.setOrgiennombre(global.clientName);
		aform.setOrgienrfc(global.rfc);
		
		String query =	"SELECT AM_ADDR_CODE,AM_DRNR,AM_STRT_NAME, "+
						"AM_PHNO1,AM_SUIT_NO,AM_FLOR_NO,AM_ADDR_STYP,AM_ADDR_DEFN_TYPE,"+
						"AM_ADDR_REF_NO,AM_GETY_LEVL,AM_GETY_TYPE,AM_GETY_CODE "+
						"FROM SYS_ADDR_MSTR WHERE AM_PE_SITE_ID=? "+
						"AND AM_ENTY_ID=? AND AM_ADDR_TYPE=? AND AM_DEFA_FLAG = ? ";
		//changed query for dc AM_PE_SITE_ID=? instead of AM_PE_brnc_ID=? amaladoss
		
		pst = con.prepareStatement(query);
		//AAP//AccessLog.Log("global.assignedsite"+strAssignedSite);
		//AAP//AccessLog.Log("global.strClientId"+strClientId);
		pst.setString(1,strAssignedSite);
		pst.setString(2,strClientId);
		pst.setString(3,"CLNT");
		pst.setString(4,"Y");
		
		rs=pst.executeQuery();
		String	AM_ADDR_CODE=null,AM_DRNR=null,AM_STRT_NAME=null,AM_PHNO1=null,AM_SUIT_NO=null,AM_FLOR_NO=null;
		String	AM_ADDR_STYP=null,AM_ADDR_DEFN_TYPE=null,AM_ADDR_REF_NO=null,AM_GETY_LEVL=null,AM_GETY_TYPE=null,AM_GETY_CODE=null;
		//code added on 29/03/2004
		String orignCityCode=null;
		
		while(rs.next()){
			AM_ADDR_CODE=rs.getString(1);			
			AM_DRNR=rs.getString(2);
			AM_STRT_NAME=rs.getString(3);
			AM_PHNO1=rs.getString(4);
			AM_SUIT_NO=rs.getString(5);
			AM_FLOR_NO=rs.getString(6);
			AM_ADDR_STYP=rs.getString(7);
			AM_ADDR_DEFN_TYPE=rs.getString(8);
			AM_ADDR_REF_NO=rs.getString(9);
			AM_GETY_LEVL=String.valueOf(rs.getInt(10));
			AM_GETY_TYPE=String.valueOf(rs.getInt(11));
			AM_GETY_CODE=rs.getString(12);
		}
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		//AAP//AccessLog.Log("Before finding ro_ftch_addr");
		cst = con.prepareCall(	"{ call pack_web.pro_ftch_addr(?,?,?,"+
								"?,?,"+
								"?,?,?,"+
								"?,?,?,?,?,"+
								"?,?,?,?,?,?) }");
		//AAP//AccessLog.Log("AM_ADDR_DEFN_TYPE"+AM_ADDR_DEFN_TYPE);
		//AAP//AccessLog.Log("AM_ADDR_REF_NO"+AM_ADDR_REF_NO);
		//AAP//AccessLog.Log("AM_GETY_CODE"+AM_GETY_CODE);
		//AAP//AccessLog.Log("AM_GETY_LEVL"+AM_GETY_LEVL);
		//AAP//AccessLog.Log("AM_GETY_TYPE"+AM_GETY_TYPE);
		cst.setString(1,AM_ADDR_DEFN_TYPE);
		cst.setString(2,AM_ADDR_REF_NO);
		cst.setString(3,AM_GETY_CODE);
		cst.setInt(4,Integer.parseInt(AM_GETY_LEVL));
		cst.setInt(5,Integer.parseInt(AM_GETY_TYPE));
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
		
		rs=cst.executeQuery();
		
		/*AccessLog.Log("CST 6 "+cst.getString(6));
		AccessLog.Log("CST 7 "+cst.getString(7));
		AccessLog.Log("CST 8 "+cst.getString(8));
		AccessLog.Log("CST 9 "+cst.getString(9));
		AccessLog.Log("CST 10 "+cst.getString(10));
		AccessLog.Log("CST 11 "+cst.getString(11));
		AccessLog.Log("CST 12 "+cst.getString(12));
		AccessLog.Log("CST 13 "+cst.getString(13));
		AccessLog.Log("CST 14 "+cst.getString(14));
		AccessLog.Log("CST 15 "+cst.getString(15));
		AccessLog.Log("CST 16 "+cst.getString(16));
		AccessLog.Log("CST 17 "+cst.getString(17));
		AccessLog.Log("CST 18 "+cst.getString(18));
		AccessLog.Log("CST 19 "+cst.getString(19));
		AccessLog.Log("+++++testing +++++++++");
						   
		AccessLog.Log("CST 17 "+cst.getString(17));*/
		orignCityCode = cst.getString(17);
		aform.setOrginCityCode (orignCityCode);
		aform.setOriginColinaCode(cst.getString(19));
								  
			
			
		
		//From Session
		aform.setOrgioncode(strAssignedSite);
		aform.setOrgienclave(strClientId);
	
		//From the Resultset of the first query
		aform.setOrgien1(AM_STRT_NAME);
		aform.setOrgien2(AM_DRNR);
		aform.setOrgientelefono(AM_PHNO1);
		aform.setOrgionaddresscode(AM_ADDR_CODE);
		//AAP//AccessLog.Log("Before Fiscal name");
		if ((aform.getFiscal1()!=null) && (aform.getFiscal1().length()>0)) {//Changes done by B.Emerson on 29/03/2004 in order to have the latest fiscal address
			//aaAccessLog.Log("Fiscal address available");
		} else{
			//From the Resultset of the first query
			aform.setFiscal1(AM_STRT_NAME);
			aform.setFiscal2(AM_DRNR);
			aform.setFiscaltelefono(AM_PHNO1);
			aform.setFiscaladdresscode(AM_ADDR_CODE);
			
			//From the Stored Procedure output.
			aform.setFiscalcolonia1(cst.getString(11));
			aform.setFiscalcolonia2(cst.getString(9));
		}
		
		//From the Stored Procedure output.
		aform.setOrgiencolonia1(cst.getString(11));
		aform.setOrgiencolonia2(cst.getString(9));		
		//AAP//AccessLog.Log("Before finding branch name");
		String branchQuery = " select pack_web.fun_ftch_brnc_name(?) from dual ";
		pst = con.prepareStatement(branchQuery);
		pst.setString(1,global.assignedBranch);
		rs = pst.executeQuery();
		String branchName = "";		
		
		if(rs.next())
			branchName = rs.getString(1);
		
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		//AAP//AccessLog.Log("Before finding site name");
		String siteQuery = " select pack_web.fun_ftch_site_name(?) from dual ";
		pst = con.prepareStatement(siteQuery);
		pst.setString(1,global.assignedSite);
		rs = pst.executeQuery();
		String siteName = "";		
		
		if(rs.next())
			siteName = rs.getString(1);
		
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		aform.setOrginbranchcode(strAssignedBranch);
		aform.setOrgionbranch(branchName);
		aform.setOrginsite(siteName);
		global.destinationBranchName=branchName;
		global.siteName=siteName;
		session.setAttribute("sGlobal",global);
		if(cst!=null)
			cst.close();
		if(rs!=null)
			rs.close();
	}
	public boolean isTariffSlabExists(Connection con,Global global)	throws Exception
	{		
		String query = "select pack_web.fun_ftch_trif_exist(?,?,?,?) from dual";		
		PreparedStatement pst = con.prepareStatement(query);		
		pst.setString(1,global.clientId);
		pst.setString(2,global.assignedBranch);
		pst.setString(3,global.destinationBranchId);
		pst.setString(4,global.tarifType);
		ResultSet rs = pst.executeQuery();
		String tariffSlab = "";
		if(rs.next()){
			tariffSlab = rs.getString(1);
			global.tariffSlab = tariffSlab;
		}
		closeResources(rs,pst);	
		if(tariffSlab.equalsIgnoreCase("Y"))
			return true;
		else
			return false;
		
	}
	public void closeResources(ResultSet rs, PreparedStatement pst)throws Exception{		
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
	}
	
	public boolean isBorderBranch(Connection con, HttpSession session,String branchId)throws Exception{
		//Global global = (Global)session.getAttribute("sGlobal");
		String query = "select pack_web.fun_brdr_brnc(?) from dual ";
		//AAP//AccessLog.Log(query);
		//AAP//AccessLog.Log(branchId);
		//AAPif(con!=null)
			//AAP//AccessLog.Log("connection is ok");
		PreparedStatement pst  = con.prepareStatement(query);
		pst.setString(1,branchId);
		
		ResultSet rs =pst.executeQuery();
		
		if(rs.next()){
			//AAP//AccessLog.Log("inside result set");
			if(rs.getString(1).equalsIgnoreCase("BR")){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	public void getServiceCovered(Connection con,
								  HttpServletRequest req,
								  JavWebBookingServicesForm serForm) throws Exception {
		
		HttpSession session=req.getSession(false);
		Global global =(Global)session.getAttribute("sGlobal");
		String groupClientId = getGroupClientId(con,session);
		String query = "{call pack_web.pro_ftch_srvs_covd(?,?,?,?,?) }";
		CallableStatement cst = con.prepareCall(query);
		cst.setString(1,global.tarifType);
		cst.registerOutParameter(2,Types.VARCHAR);
		cst.registerOutParameter(3,Types.VARCHAR);
		cst.registerOutParameter(4,Types.VARCHAR);
		cst.registerOutParameter(5,Types.VARCHAR);
		
		ResultSet rs=cst.executeQuery();
		String ead=null,cod=null,ack=null,inv=null;
		
		//if(rs.next()){
		ead=cst.getString(2);
		ack=cst.getString(3);
		cod=cst.getString(4);
		inv=cst.getString(5);
		//}
		
		if(cst!=null)
			cst.close();
		
		String deliveryType ="";
		
		//AAP//AccessLog.Log("EAD VALUE "+ead);
		
		if(ead==null){
			String query1 = "select pack_web.fun_dflt_dlvy(?,?) from dual";
			PreparedStatement pst = con.prepareStatement(query1);
			pst.setString(1,groupClientId);
			pst.setString(2,ead);
			rs = pst.executeQuery();
			if(rs.next()){
				if(rs.getString(1)!=null && rs.getString(1).equalsIgnoreCase("TRUE"))
					deliveryType="H";
				else
					deliveryType="O";
			}
			if(rs!=null)
				rs.close();
			if(pst!=null)
				pst.close();
			serForm.setEntrega("1");//For default selection
			
		}else{
			deliveryType="H";
			serForm.setEntrega("2");//For default selection
		}
		
		ServicesGlobal servicesGlobal = (ServicesGlobal)session.getAttribute("servicesGlobal");
		if(servicesGlobal!=null){
			servicesGlobal.deliveryType=deliveryType;
			servicesGlobal.ead=ead;
			servicesGlobal.cod=cod;
			servicesGlobal.ack=ack;
			servicesGlobal.inv=inv;
		}
		else{
			servicesGlobal=new ServicesGlobal();
			servicesGlobal.deliveryType=deliveryType;
			servicesGlobal.ead=ead;
			servicesGlobal.cod=cod;
			servicesGlobal.ack=ack;
			servicesGlobal.inv=inv;
		}
		
		session.setAttribute("servicesGlobal",servicesGlobal);
		servicesGlobal = (ServicesGlobal)session.getAttribute("servicesGlobal");
	}
	
	public String getGroupClientId(Connection con,HttpSession session)throws Exception {
		Global global = (Global)session.getAttribute("sGlobal");
		String groupIdQuery = "select pack_web.FUN_CHCK_GRUP_CLNT(?)as groupid from dual";
		PreparedStatement pst = con.prepareStatement(groupIdQuery);
		pst.setString(1,global.clientId);
		ResultSet rs = pst.executeQuery();
		String groupClientId="";
		while(rs.next()){
			groupClientId = rs.getString("groupid");
		}
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		//AAP//AccessLog.Log("GROUPCLIENTID"+groupClientId);
		
		return groupClientId;
	}
	
	public void fetchAcknowledgementTypeDesc(Connection con,
											 HttpSession session,
											 JavWebBookingServicesForm serForm)throws Exception {
		Global global =(Global)session.getAttribute("sGlobal");
		String query=null;
		boolean checkTariffType=true;
		String ackType = null;
		if(global.tarifType.equalsIgnoreCase("G")){
			query=	"	SELECT pm_vlue1_desc,pm_parm_code1 FROM sys_parm_mstr "+
					"	WHERE pm_parm_type = ? "+
					"	AND pm_parm_code1 = ?";
			
			ackType =getAcknowledgementType(con,session);
			if(ackType.equalsIgnoreCase("ACK-P"))
				ackType="I";
			else if(ackType.equalsIgnoreCase("ACK-C"))
				ackType="C";
			else if(ackType.equalsIgnoreCase("ACK-N"))
				ackType="N";
		}
		else{
			query=	"	SELECT pm_vlue1_desc,pm_parm_code1 FROM sys_parm_mstr "+
					"	WHERE pm_parm_type = ? ";
			checkTariffType=false;
		}
		
		
		
		PreparedStatement pst = con.prepareStatement(query);
		if(checkTariffType){
			pst.setString(1,"ACK_TYPE");
			pst.setString(2,ackType);
		}else{
			pst.setString(1,"ACK_TYPE");
		}
		
		ResultSet rs = pst.executeQuery();
		//ArrayList ackTypeDesc=new ArrayList();
		serForm.getAcklabel().clear();
		serForm.getAckvalue().clear();
		while(rs.next()){
			serForm.setAcklabel(rs.getString(1));
			if(rs.getString(2).equalsIgnoreCase("I"))
				serForm.setAckvalue("ACK-P");
			else if(rs.getString(2).equalsIgnoreCase("N"))
				serForm.setAckvalue("ACK-N");
			else if(rs.getString(2).equalsIgnoreCase("C"))
				serForm.setAckvalue("ACK-C");
			
		}
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		
		String ack = "ACK";
		String groupClientId = getGroupClientId(con,session);
		//AAP//AccessLog.Log("GROUP CLIENT ID IN SIDE "+groupClientId);
		String defaultQuery =	"SELECT CS_DEFA_SRVC_item FROM SYS_CLNT_SRVC "+
								"WHERE CS_CLNT_ID = ? AND CS_SRVC_ID =? ";
		pst=con.prepareStatement(defaultQuery);
		pst.setString(1,groupClientId);
		pst.setString(2,ack);
		rs = pst.executeQuery();
		
		if(rs.next()){
			String typeIndex = rs.getString(1);
			serForm.setAcusederecibo(typeIndex);
		}else{
			serForm.setAcusederecibo("ACK-N");
		}
		//AccessLog.Log("DEFAULT SELECTED FOR ACUSE DE RECIBO "+serForm.getAcusederecibo());
		
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
	}
	public void getInsuranceType(Connection con,String lc_inv_vlue,
								 JavWebBookingServicesForm serForm)throws Exception {
		
		//Hashtable col =new Hashtable();
		
		//AAP//AccessLog.Log("LC_INV_VALUE "+lc_inv_vlue);
		String query =	"SELECT pm_vlue1_desc,pm_parm_code1 "+
						"FROM sys_parm_mstr "+
						"Where pm_mdul_id=? "+
						"and pm_parm_type = ? "+
						"and pm_parm_code2 = ?";
		PreparedStatement pst = con.prepareStatement(query);
		
		pst.setString(1,"BOK");
		pst.setString(2,"COVERAGE");
		pst.setString(3,lc_inv_vlue);
		ResultSet rs = pst.executeQuery();
		
		String label="";
		String value="";
		
		serForm.getInsurancetype().clear();
		serForm.getInsurancetypelabel().clear();
		while(rs.next()){
			label = rs.getString(1);
			value = rs.getString(2);
			//AAP//AccessLog.Log("VALUE OF OPTION  "+value);
			serForm.setInsurancetypelabel(label);
			serForm.setInsurancetype(value);
		}
		
		//Default Selection
		ArrayList temp =serForm.getInsurancetype();
		if(lc_inv_vlue!=null && lc_inv_vlue.length()==0){
			for(int i=0;i<temp.size();i++){
				String tempValue = (String)temp.get(i);
				if(tempValue.equalsIgnoreCase("INV-1"))
					serForm.setCobertura("INV-1");
			}
		}else if(lc_inv_vlue.equalsIgnoreCase("LTH")){
			for(int i=0;i<temp.size();i++){
				String tempValue = (String)temp.get(i);
				if(tempValue.equalsIgnoreCase("INV-PF"))
					serForm.setCobertura("INV-PF");
			}
			
		}else if(lc_inv_vlue.equalsIgnoreCase("GTH")){
			for(int i=0;i<temp.size();i++){
				String tempValue = (String)temp.get(i);
				if(tempValue.equalsIgnoreCase("IV-PF"))
					serForm.setCobertura("IV-PF");
			}
			
		}else if(lc_inv_vlue.equalsIgnoreCase("GTF")){
			for(int i=0;i<temp.size();i++){
				String tempValue = (String)temp.get(i);
				if(tempValue.equalsIgnoreCase("IV-XF"))
					serForm.setCobertura("IV-XF");
			}
			
		}else if(lc_inv_vlue.equalsIgnoreCase("NO")){
			for(int i=0;i<temp.size();i++){
				String tempValue = (String)temp.get(i);
				if(tempValue.equalsIgnoreCase("INV-1"))
					serForm.setCobertura("INV-1");
			}
			
		}else if(lc_inv_vlue.equalsIgnoreCase("CLN")){
			for(int i=0;i<temp.size();i++){
				String tempValue = (String)temp.get(i);
				if(tempValue.equalsIgnoreCase("INV-CX"))
					serForm.setCobertura("INV-CX");
			}
		}
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
	}
	public String getInsuranceInformation(Connection con,
										  String insuranceAmount,
										  String groupClientId,
										  JavWebBookingServicesForm serForm)throws Exception {
		
		//Default Selected
		ArrayList insuranceTypeValue  = serForm.getInsurancetype();
		ArrayList insuranceTypeLabel = serForm.getInsurancetypelabel();
		
		String value = (String)insuranceTypeValue.get(0);
		String label = (String)insuranceTypeLabel.get(0);
		
		//AAP//AccessLog.Log("VALUE "+value);
		//AAP//AccessLog.Log("LABEL  "+label);
		
		String query = "{ call pack_web.pro_srvc_info(?,?,?,?) }";
		CallableStatement cst = con.prepareCall(query);
		cst.setString(1,value);
		
		if(insuranceAmount==null  || (insuranceAmount!=null && insuranceAmount.length()==0))
			//cst.setDouble(2,0.0d);//Empty
			cst.setNull(2,Types.NUMERIC);
		
		else
			cst.setDouble(2,Double.parseDouble(insuranceAmount));
		
		cst.setString(3,groupClientId);
		//cst.setString(4,"");
		cst.registerOutParameter(4,Types.VARCHAR);
		
		cst.executeQuery();
		String insInfo = cst.getString(4);
		if(cst!=null)
			cst.close();
		
		return insInfo;
	}
	
	public void calculateKMTarifType(Connection con,HttpSession session,JavWebBookingGeneralForm aform)throws Exception{
		Global global =(Global)session.getAttribute("sGlobal");
		String query = "select pack_web.FUN_CHK_ACTV_TRIF(?,?) from dual";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1,global.assignedBranch);
		//pst.setString(2,aform.getDestinationcode());--commented and below line added by B.Emerson on 06/04/2004
		pst.setString(2,global.destinationBranchId);
		ResultSet rs = pst.executeQuery();
		String kmTarifType=null;
		
		if(rs.next()){
			//kmTarifType = Double.toString(rs.getDouble(1));
			kmTarifType = rs.getString(1);
			//AccessLog.Log("Inside km tarif type rs "+rs.getString(1));
		}		
		global.kmTarifType = kmTarifType;
		session.setAttribute("sGlobal",global);		
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		
	}
	
	public String getInsuranceAmount(Connection con,
									 Global global,
									 HttpServletRequest req,
									 JavWebBookingServicesForm serForm)throws Exception {
		
		String groupClientId = getGroupClientId(con,req.getSession(false));
		String query ="{ call pack_web.PRO_CLNT_INSU_FLAG(?,?,?,?,?) }";
		CallableStatement cst = con.prepareCall(query);
		cst.setString(1,groupClientId);
		cst.registerOutParameter(2,Types.VARCHAR);
		cst.registerOutParameter(3,Types.VARCHAR);
		cst.registerOutParameter(4,Types.DATE);
		cst.registerOutParameter(5,Types.DATE);
		cst.executeQuery();
		
		String insuranceFlag=cst.getString(2);
		String policyNumber=cst.getString(3);
		java.sql.Date expiryDate=cst.getDate(4);
		java.sql.Date sysDate=cst.getDate(5);
		
		String lc_inv_vlue =null;
		String valorDeclarado = serForm.getValordeclarado().trim();
		double intValorDeclarado=0.0;
		
		//AAP//AccessLog.Log("-------------------------------------------------------------------------------------");
		//AAP//AccessLog.Log("**************************** -------------EXPIERY DATE IN GENERAL "+expiryDate);
		//AAP//AccessLog.Log("**************************** -------------SYSDATE  IN GENERAL "+sysDate);
		
		if(valorDeclarado!=null && valorDeclarado.length()>0)
			intValorDeclarado=Double.parseDouble(valorDeclarado);
		
		//if(policyNumber!=null && (expiryDate.after(sysDate) || expiryDate.equals(sysDate))){
		if(policyNumber!=null && ((expiryDate!=null && expiryDate.after(sysDate)) || (expiryDate!=null && expiryDate.equals(sysDate)))){
			valorDeclarado = null;
			lc_inv_vlue="CLN";
			serForm.setValordeclarado("");
			req.setAttribute("valorvalue","null");
			//AAP//AccessLog.Log(" 1 ");
			//}else if(policyNumber!=null && expiryDate.before(sysDate) && insuranceFlag.equalsIgnoreCase("N")){
		}else if(policyNumber!=null && (expiryDate!=null && expiryDate.before(sysDate)) && insuranceFlag.equalsIgnoreCase("N")){
			valorDeclarado =null;
			lc_inv_vlue="NO";
			serForm.setValordeclarado("");
			req.setAttribute("valorvalue","null");
			//AAP//AccessLog.Log(" 2 ");
			//}else if(policyNumber!=null && expiryDate.before(sysDate) && insuranceFlag.equalsIgnoreCase("Y")){
		}else if(policyNumber!=null && (expiryDate!=null && expiryDate.before(sysDate)) && insuranceFlag.equalsIgnoreCase("Y")){
			if(valorDeclarado==null || valorDeclarado.length()==0){
				lc_inv_vlue="NO";
			}else if(intValorDeclarado <= 1000){
				lc_inv_vlue="LTH";
			}else if(intValorDeclarado>1000 && intValorDeclarado<=5000){
				lc_inv_vlue="GTH";
			}else if(intValorDeclarado > 5000 && intValorDeclarado <=10000000){
				lc_inv_vlue = "GTF";
			}
			//AAP//AccessLog.Log(" 3 ");
		}else if(insuranceFlag.equalsIgnoreCase("N")){
			valorDeclarado=null;
			lc_inv_vlue= "NO";
			serForm.setValordeclarado("");
			req.setAttribute("valorvalue","null");
			//AAP//AccessLog.Log(" 4 ");
		}else if(insuranceFlag.equalsIgnoreCase("Y")){
			if(valorDeclarado==null || valorDeclarado.length()==0){
				lc_inv_vlue="NO";
			}else if(intValorDeclarado<= 1000){
				lc_inv_vlue="LTH";
			}else if(intValorDeclarado>1000 && intValorDeclarado<=5000){
				lc_inv_vlue="GTH";
			}else if(intValorDeclarado > 5000 && intValorDeclarado <=10000000){
				lc_inv_vlue = "GTF";
			}
			//AAP//AccessLog.Log(" 5 ");
		}
		//AAP//AccessLog.Log("VALOR DECLARADO IN GENERAL "+valorDeclarado);
		
		//For Insurance Type
		getInsuranceType(con,lc_inv_vlue,serForm);
		//req.setAttribute("insurancetype",insuranceType);
		
		//For Insurance Infomation Display
		String insuranceInformationDisp = getInsuranceInformation(con,
																  valorDeclarado,
																  groupClientId,
																  serForm);
		serForm.setSeguro(insuranceInformationDisp);
		//AAP//AccessLog.Log("INSURANCE INFORMATIOM DISPLAY IN GENERAL "+insuranceInformationDisp);
		
		return valorDeclarado;
	}
}