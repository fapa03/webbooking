
/*----------------------------------------------------------
Author					:	V.Ramachandran
Date					:	25-March-2003
FileName				:	JavWebBookingServicesDetailAddAction1.java
SessionVariables		:
Other Used Classes		:
Function Names			:
------------------------------------------------------------*/

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

import bean.ConsultaParametros;
import bean.Global;
import bean.JavWebBookingServicesDetailAddForm1;
import bean.JavWebBookingServicesForm;
import bean.ShipmentServiceDetail;
import beanUtil.ConnectDB;


public class JavWebBookingServicesDetailAddAction1 extends Action {
	
	public JavWebBookingServicesDetailAddAction1() {
		//AAP//AccessLog.Log("***JavWebBookingServicesDetailAddAction1***");
	}
	public ActionForward perform(ActionMapping am, ActionForm af, 
								 HttpServletRequest req, HttpServletResponse res)
	{
		Connection con=null;
		String returnpage="thispage";
		
		//code added by sundar on 23/09/2003 for displaying description and content by default
		PreparedStatement Pst=null;
		ResultSet Rs=null;		
		String Description=null,Content=null;
		
		try{
			
			if(af instanceof JavWebBookingServicesDetailAddForm1){
				JavWebBookingServicesDetailAddForm1 aform = (JavWebBookingServicesDetailAddForm1)af;
				HttpSession session = req.getSession(false);
				
				//For No Session
				if(session==null || session.isNew()){
					return am.findForward("nosession");
				}
				con = ConnectDB.getConnection();
				
				String clientId = (String)session.getAttribute("sClientId");
				
				if(clientId==null )
					return am.findForward("nosession");
				
				String action = req.getParameter("action");
				String servicecount = (String)session.getAttribute("count");
				//AAP//AccessLog.Log("action ==>"+action);
				//AAP//AccessLog.Log("servicecount ==>"+servicecount);
				/*obtiene configuracion de peso maximo permitido*/
				ConsultaParametros consulta = new ConsultaParametros();
				ArrayList sysParmMstr = consulta.QryMdulTypeParm1(con, "BOK", "LIM_TOP_PESO_BG","G");
				//System.out.println("&%&%&%&%&%&%&%&%& peso maximo "+((ArrayList)sysParmMstr.get(0)).get(2).toString());
				String pesoMax = ((ArrayList)sysParmMstr.get(0)).get(2).toString();
				aform.setPesoMax(pesoMax);
		//code added by sundar on 23/09/2003 for displaying description and content by default
				if(servicecount.equals("new")){				
				String query =	"select wc_desc,wc_cont,wc_weight,wc_volume from web_clnt_mstr where wc_clnt_id = ? ";
				Pst=con.prepareStatement(query);
				Pst.setString(1,clientId);
				Rs=Pst.executeQuery();
				String desc=null,cont=null;
				String weight="";
				String volume="";
				while(Rs.next()){
				 desc=Rs.getString(1);
				 cont=Rs.getString(2);
				 weight=Rs.getString(3);
				 volume=Rs.getString(4);
				}
				if(Rs!=null)
					 Rs.close();
					if(Pst!=null)
				     Pst.close();
					String packets="";
					query="select SS_REFR_SRVC_ID FROM  SYS_SHP_DESC_MSTR where SS_DESC =?";
					Pst=con.prepareStatement(query);
					Pst.setString(1,desc);
					Rs=Pst.executeQuery();
					while(Rs.next())
					{
						packets=Rs.getString(1);
					}
					if(packets.equalsIgnoreCase("PACKETS"))
					{
						if((weight!=null)&&(weight.length()>0)){
							aform.setPeso(weight);
							aform.setPesoDB(weight);//AAP02
							aform.setVolumen(volume);
							aform.setVolumenDB(volume);//AAP02
							aform.setSpecialTariff("true");
						}
					}
				if(Rs!=null)
				 Rs.close();
				if(Pst!=null)
			     Pst.close();	
				
				//code added by sundar on 29/10/2003
				if(desc!=null){
				String chkquery="select 1 from sys_shp_desc_mstr where ss_desc=?";
				Pst=con.prepareStatement(chkquery);
				Pst.setString(1,desc);
				Rs=Pst.executeQuery();
				if(Rs.next()){				
				aform.setDescripcion(desc);
				aform.setContenido(cont);		
				}
				else{
					desc=null;
					cont=null;
				}
				}
				if(Rs!=null)
					Rs.close();
				if(Pst!=null)
					Pst.close();
						
				
				//code added by sundar on 26/09/2003
				if(desc!=null){	
				String query1="select ss_srvc_id,ss_refr_srvc_id,ss_code from sys_shp_desc_mstr where ss_desc=?";
				// code commented by sundar on 29/10/2003
				//Pst=con.prepareStatement(query1);
				//code added by sundar on 29/10/2003
				Pst=con.prepareStatement(query1,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				Pst.setString(1,desc);					
				Rs=Pst.executeQuery();
				String service_Id=null,refer_srvc_Id=null,service_code=null;				
				
				while(Rs.next()){					
					service_Id=Rs.getString(1);
					refer_srvc_Id=Rs.getString(2);
					service_code=Rs.getString(3);
				    Rs.last();//code added by sundar on 29/10/2003		
				}
				
				if(Rs!=null)
				 Rs.close();
				if(Pst!=null)
			     Pst.close();		
					
				session.setAttribute("serviceid",service_Id);
				session.setAttribute("referserviceid",refer_srvc_Id);
				session.setAttribute("servicecode",service_code);

				}				
				session.setAttribute("status","new");					
				}	
						
				ArrayList servicesDetailArray=(ArrayList)session.getAttribute("servicesDetailDefault");
				if(servicecount!=null && servicecount.equals("new")){					
					servicesDetailArray= new ArrayList();
					session.setAttribute("count","old");					 
					session.setAttribute("servicesDetailDefault",servicesDetailArray);
				}
				
				if(action!=null && action.equalsIgnoreCase("add")){					
					String referenceServiceId = req.getParameter("referenceserviceid");
					String serviceId = req.getParameter("serviceid");
					String descripcionCode=req.getParameter("descripcioncode");
					ShipmentServiceDetail ssd = new ShipmentServiceDetail();
					//AAP//AccessLog.Log("*****************referenceServiceId"+referenceServiceId);
					//AAP//AccessLog.Log("*****************serviceId"+serviceId);
					//AAP//AccessLog.Log("*****************descripcionCode"+descripcionCode);
					ssd.cantidad = aform.getCantidad();
					ssd.descripcion = aform.getDescripcion();
					ssd.contenido=aform.getContenido().toUpperCase();
					ssd.peso=aform.getPeso();
					ssd.volumen=aform.getVolumen();					
					int intCantidad = Integer.parseInt(aform.getCantidad());
					
					//Code added by palanivel to calculate the importe value based on the tariff values
					Global global = (Global)session.getAttribute("sGlobal");

					if(!global.tarifType.equalsIgnoreCase("c")) {
						//AAP//AccessLog.Log("*****************Not C type");
						ssd.importe = new java.text.DecimalFormat("0.00").format(intCantidad*getServiceAmount(con,session,referenceServiceId,serviceId));
					}
					else
					{
						
                        ssd.importe = new java.text.DecimalFormat("0.00").format(intCantidad*getServiceAmountCtype(con,session,referenceServiceId,serviceId,aform.getPeso(),aform.getVolumen()));
						
					}
					 
					
					double totalValue=Double.parseDouble(ssd.importe);
					if(totalValue>0)
					{
					ssd.serviceId=serviceId;
					ssd.refServiceId=referenceServiceId;
					ssd.descripcionCode=descripcionCode;
					
					if(servicesDetailArray.size()==0)
						{if( Double.parseDouble(ssd.importe) >0)
							ssd.importe=new java.text.DecimalFormat("0.00").format((Double.parseDouble(ssd.importe)));//changed to reduce 1$ from amount
						//ssd.importe=new java.text.DecimalFormat("0.00").format((Double.parseDouble(ssd.importe)+1));
						}
					
					servicesDetailArray.add(ssd);
					
					//code added by sundar on 23/09/2003 for displaying description and content by default
					String status=session.getAttribute("status").toString();
					Description=ssd.descripcion;
					Content=ssd.contenido;
					if(status.equals("new")){
						session.setAttribute("status","old");
						session.setAttribute("sdescription",Description);
				        session.setAttribute("scontent",Content);	

					}				
					
					int arraysize = servicesDetailArray.size();
					
					req.setAttribute("hitcount",String.valueOf(arraysize));
					session.setAttribute("servicesDetailDefault",servicesDetailArray);
					String envelopealone = (String)session.getAttribute("envelopealone");
					JavWebBookingServicesForm serForm = (JavWebBookingServicesForm)session.getAttribute("webBookingservices");
					if(envelopealone!=null && envelopealone.equalsIgnoreCase("true")){						
						serForm.i=0;
						session.removeAttribute("envelopealone");
					}
					}
					else {
						//AAP//AccessLog.Log("global.assignedBranch"+global.assignedBranch);
						//AAP//AccessLog.Log("global.destinationBranchId"+global.destinationBranchId);
						//pst.setString(3,global.destinationBranchId)
						String orginsite=global.assignedBranch.substring(1,3);
						String destsite=global.destinationBranchId.substring(1,3);
						//AAP//AccessLog.Log("global.assignedBranch"+orginsite);
						//AAP//AccessLog.Log("global.destinationBranchId"+destsite);	
						if(orginsite.equalsIgnoreCase(destsite)) {
							//AAP//AccessLog.Log("same site");
							servicesDetailArray.add(ssd);
							
							//code added by sundar on 23/09/2003 for displaying description and content by default	
							String status=session.getAttribute("status").toString();
							Description=ssd.descripcion;
							Content=ssd.contenido;
							if(status.equals("new")){
								session.setAttribute("status","old");
								session.setAttribute("sdescription",Description);
						        session.setAttribute("scontent",Content);
							}	
						
							int arraysize = servicesDetailArray.size();
							req.setAttribute("hitcount",String.valueOf(arraysize));
							session.setAttribute("servicesDetail",servicesDetailArray);
							String envelopealone = (String)session.getAttribute("envelopealone");
							JavWebBookingServicesForm serForm = (JavWebBookingServicesForm)session.getAttribute("webBookingservices");
							if(envelopealone!=null && envelopealone.equalsIgnoreCase("true")){
								serForm.i=0;
								session.removeAttribute("envelopealone");
							}
						}
						else
						req.setAttribute("zeroexist","This combination gives 0, So please select other options...........");
					}
					returnpage="webbookingservicesdetailedit1";
				}else if(action!=null && action.equalsIgnoreCase("edit")){

					Global global = (Global)session.getAttribute("sGlobal");
					
					ShipmentServiceDetail ssd  = new ShipmentServiceDetail();
					
					String strIndex = req.getParameter("arrayindex");
					int index = Integer.parseInt(strIndex);					
					ssd.cantidad = aform.getCantidad();					
					ssd.descripcion = aform.getDescripcion();
					ssd.contenido=aform.getContenido().toUpperCase();
					ssd.peso=aform.getPeso();
					ssd.volumen=aform.getVolumen();
					ssd.serviceId=aform.getSs_srvc_id();
					ssd.refServiceId=aform.getSs_refr_srvc_id();
					ssd.descripcionCode=aform.getDescripcioncode();
					
					int intCantidad = Integer.parseInt(aform.getCantidad());
					
					// Sam
					if(!global.tarifType.equalsIgnoreCase("c"))
					{
						
						//ssd.importe = new java.text.DecimalFormat("0.00").format(intCantidad*getServiceAmount(con,session,referenceServiceId,serviceId));
						ssd.importe = new java.text.DecimalFormat("0.00").format(intCantidad*getServiceAmount(con,session,aform.getSs_refr_srvc_id(),aform.getSs_srvc_id()));
					}
					else
					{
						
                       // ssd.importe = new java.text.DecimalFormat("0.00").format(intCantidad*getServiceAmountCtype(con,session,referenceServiceId,serviceId,aform.getPeso(),aform.getVolumen()));
					    ssd.importe = new java.text.DecimalFormat("0.00").format(intCantidad*getServiceAmountCtype(con,session,aform.getSs_refr_srvc_id(),aform.getSs_srvc_id(),aform.getPeso(),aform.getVolumen()));
						
					}
					double totalValue=Double.parseDouble(ssd.importe);
					if(totalValue>0)
					{
					//Commented by Sam
					//ssd.importe = new java.text.DecimalFormat("0.00").format(intCantidad*getServiceAmount(con,session,aform.getSs_refr_srvc_id(),aform.getSs_srvc_id()));
					
					servicesDetailArray.remove(index);
					servicesDetailArray.add(index,ssd);//Adding the changed values into the arraylist
					
					//code added by sundar on 23/09/2003 for displaying description and content by default
					if(index==0){
					
					Description=ssd.descripcion;
					Content=ssd.contenido;					
					session.setAttribute("sdescription",Description);
				    session.setAttribute("scontent",Content);		
					}
					
					String envelopealone = (String)session.getAttribute("envelopealone");
					JavWebBookingServicesForm serForm = (JavWebBookingServicesForm)session.getAttribute("webBookingservices");
					if(envelopealone!=null && envelopealone.equalsIgnoreCase("true")){						
						serForm.i=0;
						session.removeAttribute("envelopealone");
					}
					}
					else {
						global=(Global)session.getAttribute("sGlobal");
						//AAP//AccessLog.Log("global.assignedBranch"+global.assignedBranch);
						//AAP//AccessLog.Log("global.destinationBranchId"+global.destinationBranchId);
						//pst.setString(3,global.destinationBranchId)
						String orginsite=global.assignedBranch.substring(1,3);
						String destsite=global.destinationBranchId.substring(1,3);
						//AAP//AccessLog.Log("global.assignedBranch"+orginsite);
						//AAP//AccessLog.Log("global.destinationBranchId"+destsite);	
						if(orginsite.equalsIgnoreCase(destsite))
						{
						req.setAttribute("zeroexist","This combination gives 0, So please select other options...........");
						}
					}
					returnpage="webbookingservicesdetailedit1";
				}
			}			
		}catch(Exception e){
			AccessLog.Log(e);
		}finally{
			try {
				if (Rs != null && !Rs.isClosed())
					Rs.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (Pst != null && !Pst.isClosed())
					Pst.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			try {
				if (con != null)
					con.close();
			} catch (Exception ex) {
				AccessLog.Log(ex);
			}
		}
		return am.findForward(returnpage);
	}


	public double getServiceAmount(Connection con, HttpSession session,
								   String referenceServiceId,String serviceId)throws Exception{
		
		Global global = (Global)session.getAttribute("sGlobal");
		String query = "select pack_web.FUN_FTCH_TRIF_AMNT(?,?,?,?,?,?) from dual";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1,global.clientId);
		pst.setString(2,global.assignedBranch);
		pst.setString(3,global.destinationBranchId);
		pst.setString(4,global.tarifType);
		pst.setString(5,referenceServiceId);
		pst.setString(6,serviceId);
		ResultSet rs = pst.executeQuery();
		double amount=0.0;
		if(rs.next()){
			amount=rs.getDouble(1);
		}
		
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();		
		return amount;
	}
	/* 
     * code added by palanivel
	 * This method is added to calulate the tariff value based on the KG ,CUM & NON-Factor 
	 * whose product with quantity give the importe value
	 *
	 */

	public double getServiceAmountCtype(Connection con, HttpSession session,
								   String referenceServiceId,String serviceId ,String weight ,String volume )throws Exception{
		
		Global global = (Global)session.getAttribute("sGlobal");
		String query = "select pack_web.FUN_FTCH_WT_VOL_AMNT(?,?,?,?,?,?,?,?) from dual";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1,global.clientId);
		pst.setString(2,global.assignedBranch);
		pst.setString(3,global.destinationBranchId);
		pst.setString(4,global.tarifType);
		pst.setString(5,referenceServiceId);
		pst.setString(6,serviceId);
		pst.setString(5,referenceServiceId);
		pst.setString(6,serviceId);
		pst.setDouble (7,Double.parseDouble(weight));
		pst.setDouble (8,Double.parseDouble(volume));
		ResultSet rs = pst.executeQuery();
		double amount=0.0;
		if(rs.next()){
			amount=rs.getDouble(1);
		}
		
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();		
		return amount;
	}
}