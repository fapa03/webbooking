

/*----------------------------------------------------------
Author					:	V.Ramachandran
Date					:	25-March-2003
FileName				:	JavWebBookingServicesDetailAddAction.java
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

import bean.ConsultaParametros;
import bean.Global;
import bean.JavWebBookingServicesForm;
import bean.ShipmentServiceDetail;
import beanUtil.ConnectDB;


public class JavWebBookingServicesDetailAddAction extends Action {
	
	public JavWebBookingServicesDetailAddAction() {
		//APP//AccessLog.Log("JavWebBookingServicesDetailAddAction");
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
			
			if(af instanceof JavWebBookingServicesDetailAddForm){
				JavWebBookingServicesDetailAddForm aform = (JavWebBookingServicesDetailAddForm)af;
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
				
				//code added by sundar on 23/09/2003 for displaying description and content by default
				if(servicecount.equals("new")){
					/*obtiene configuracion de peso maximo permitido*/
					ConsultaParametros consulta = new ConsultaParametros();
					ArrayList sysParmMstr = consulta.QryMdulTypeParm1(con, "BOK", "LIM_TOP_PESO_BG","G");
					//System.out.println("&%&%&%&%&%&%&%&%& peso maximo "+((ArrayList)sysParmMstr.get(0)).get(2).toString());
					String pesoMax = ((ArrayList)sysParmMstr.get(0)).get(2).toString();
					aform.setPesoMax(pesoMax);
					
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
				/*if(packets.equalsIgnoreCase("PACKETS"))
				{
					if((weight!=null)&&(weight.length()>0))
						{
						aform.setPeso(weight);
						aform.setVolumen(volume);
						aform.setSpecialTariff("true");
						}
				}*/
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
				
								
				ArrayList servicesDetailArray=(ArrayList)session.getAttribute("servicesDetail");
				if(servicecount!=null && servicecount.equals("new")){
					servicesDetailArray= new ArrayList();
					session.setAttribute("count","old");					 
					session.setAttribute("servicesDetail",servicesDetailArray);
				}
				if(action!=null && action.equalsIgnoreCase("add")){		
					   
					String referenceServiceId = req.getParameter("referenceserviceid");
					String serviceId = req.getParameter("serviceid");
					String descripcionCode=req.getParameter("descripcioncode");					
					ShipmentServiceDetail ssd  = new ShipmentServiceDetail();
					ssd.cantidad = aform.getCantidad();
					ssd.descripcion = aform.getDescripcion();
					ssd.contenido=aform.getContenido().toUpperCase();
					ssd.tarifa=aform.getTarifa();
					ssd.peso=aform.getPeso();
					ssd.volumen=aform.getVolumen();
					ssd.serviceId=serviceId;
					ssd.refServiceId=referenceServiceId;
					ssd.descripcionCode=descripcionCode;
					ssd.importe=new java.text.DecimalFormat("0.00").format(getServiceAmount(con,req,referenceServiceId,serviceId,aform));
					//ssd.importe=String.valueOf(getServiceAmount(con,req,referenceServiceId,serviceId,aform));
				
					if(servicesDetailArray.size()==0)
						//ssd.importe=ssd.importe+1;
						ssd.importe=ssd.importe; //changed to reduce 1$ minus
					
					double totalValue=Double.parseDouble(ssd.importe);
					
					if(totalValue>0)
					{
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
					else {
						
						Global global=(Global)session.getAttribute("sGlobal");
						//APP//AccessLog.Log("global.assignedBranch"+global.assignedBranch);
						//APP//AccessLog.Log("global.destinationBranchId"+global.destinationBranchId);
						//pst.setString(3,global.destinationBranchId)
						String orginsite=global.assignedBranch.substring(1,3);
						String destsite=global.destinationBranchId.substring(1,3);
						//APP//AccessLog.Log("global.assignedBranch"+orginsite);
						//APP//AccessLog.Log("global.destinationBranchId"+destsite);	
						if(orginsite.equalsIgnoreCase(destsite)) {
							//APP//AccessLog.Log("same site");
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
					returnpage="webbookingservicesdetailedit";
				}else if(action!=null && action.equalsIgnoreCase("edit")){
					
					ShipmentServiceDetail ssd  = new ShipmentServiceDetail();
					
					String strIndex = req.getParameter("arrayindex");
					int index = Integer.parseInt(strIndex);
					ssd.cantidad = aform.getCantidad();
					ssd.descripcion = aform.getDescripcion();
					ssd.contenido=aform.getContenido().toUpperCase();
					ssd.tarifa=aform.getTarifa();
					ssd.peso=aform.getPeso();
					ssd.volumen=aform.getVolumen();
					ssd.serviceId=aform.getSs_srvc_id();
					ssd.refServiceId=aform.getSs_refr_srvc_id();
					ssd.descripcionCode=aform.getDescripcioncode();
					ssd.importe=new java.text.DecimalFormat("0.00").format(getServiceAmount(con,req,aform.getSs_refr_srvc_id(),aform.getSs_srvc_id(),aform));
					//ssd.importe=String.valueOf(getServiceAmount(con,req,aform.getSs_refr_srvc_id(),aform.getSs_srvc_id(),aform));
					double totalValue=Double.parseDouble(ssd.importe);
					if(totalValue>0)
					{
					servicesDetailArray.remove(index);
					servicesDetailArray.add(index,ssd);//Adding the changed values into the arraylist
					//code added by sundar on 23/09/2003 for displaying description and content by default
					if(index==0){						
					Description=ssd.descripcion;
					Content=ssd.contenido;	
					session.setAttribute("sdescription",Description);
				    session.setAttribute("scontent",Content);					
					}
					session.setAttribute("servicesDetail",servicesDetailArray);
					String envelopealone = (String)session.getAttribute("envelopealone");
					JavWebBookingServicesForm serForm = (JavWebBookingServicesForm)session.getAttribute("webBookingservices");
					if(envelopealone!=null && envelopealone.equalsIgnoreCase("true")){
						serForm.i=0;
						session.removeAttribute("envelopealone");
					}
					}
					else {
						Global global=(Global)session.getAttribute("sGlobal");
						//APP//AccessLog.Log("global.assignedBranch"+global.assignedBranch);
						//APP//AccessLog.Log("global.destinationBranchId"+global.destinationBranchId);
						//pst.setString(3,global.destinationBranchId)
						String orginsite=global.assignedBranch.substring(1,3);
						String destsite=global.destinationBranchId.substring(1,3);
						//APP//AccessLog.Log("global.assignedBranch"+orginsite);
						//APP//AccessLog.Log("global.destinationBranchId"+destsite);	
						if (orginsite.equalsIgnoreCase(destsite)) {
						req.setAttribute("zeroexist","This combination gives 0, So please select other options...........");
						}
					}
					returnpage="webbookingservicesdetailedit";
				}
			}
			
		}catch(Exception e){
			AccessLog.Log(e);
		}finally{
			try{
				if(Rs!=null)
					Rs.close();
			}catch(Exception ex){
				AccessLog.Log(ex);
			}
			try{
				if(Pst!=null)
					Pst.close();
			}catch(Exception ex){
				AccessLog.Log(ex);
			}
			try{
				if(con!=null)
					con.close();
			}catch(Exception ex){
				AccessLog.Log(ex);
			}
		}
		return am.findForward(returnpage);
	}
	
	public double getServiceAmount(Connection con, 
								   HttpServletRequest request,
								   String referenceServiceId,
								   String serviceId,
								   JavWebBookingServicesDetailAddForm aform)throws Exception {
		
		
		String actionfirst = request.getParameter("actionfirst");
		
		double returnvalue=0.0f;
		HttpSession session =request.getSession(false);
		Global global=(Global)session.getAttribute("sGlobal");
		String query = "Begin ? := pack_sipweb.ftch_servc_tarif(?,?,?,?,?,?,?,?,?,?); End; ";
		CallableStatement cst =con.prepareCall(query);
		cst.registerOutParameter(1,Types.NUMERIC);
		cst.setString(2,serviceId);
		cst.setString(3,referenceServiceId);
		cst.setInt(6,Integer.parseInt(aform.getCantidad()));
		cst.setString(7,global.assignedBranch);
		cst.setString(8,global.destinationBranchId);
		cst.registerOutParameter(10,Types.NUMERIC);
		//APP//AccessLog.Log("THE VALUE OF KM TARIF TYPE "+global.kmTarifType);
		if(global.kmTarifType==null){
			//APP//AccessLog.Log("INSIDE IF");
			cst.setNull(11,Types.NUMERIC);
		} else {
			cst.setDouble(11,Double.parseDouble(global.kmTarifType));
		}
		
		if(serviceId.equalsIgnoreCase("SHP-E")){
			cst.setString(4,"NON");
			//cst.setDouble(5,0.0d);//Empty
			cst.setNull(5,Types.NUMERIC);
			cst.setString(9,aform.getTarifa());			
		}else{
			cst.setString(4,"KG");
			cst.setDouble(5,Double.parseDouble(aform.getPeso()));
			if(aform.getTarifa().equalsIgnoreCase("T7")){
				cst.setString(9,"T7P");
			}
			else{
				cst.setString(9,aform.getTarifa());
			}
		}
		
		cst.executeQuery();
		
		double weight = cst.getDouble(1);
		returnvalue = weight;
		
		double volume=0.0f;
		if(serviceId.equalsIgnoreCase("SHP-G") && aform.getTarifa().equalsIgnoreCase("T7")){
			cst.setString(4,"CUM");
			cst.setDouble(5,Double.parseDouble(aform.getVolumen()));
			cst.setString(9,"T7V");
			cst.executeQuery();
			volume= cst.getDouble(1);
			if(weight>volume){
				returnvalue=weight;
				if(!(actionfirst!=null && actionfirst.equals("add"))){
					request.setAttribute("calculationdone","weight");
				}
			}
			else{
				returnvalue=volume;
				if(!(actionfirst!=null && actionfirst.equals("add"))){
					request.setAttribute("calculationdone","volume");
				}
			}
		}
		//APP//AccessLog.Log("THE SERVICE AMOUNT RETURNED "+cst.getDouble(10));
		
		if(cst!=null)
			cst.close();
		
		//APP//AccessLog.Log(" THE VALUE RETURNED FOR SERVICE AMOUNT "+returnvalue);
		return returnvalue;
	}  
   
}