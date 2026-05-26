/**
 * @author: V.RamachandranG
 * Fecha de Creación: 25-March-2003
 * Compañía: KUMARAN.
 * Descripción del programa: Bean para calculo de servicios.
 * FileName: JavWebBookingServicesAction.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Modified by amaladoss  On 9-Jan-2007: To solve Insurance service display and amount difference in guia_head and service tables 
 * Modified by Balaji  On 03-Jun-2009: To Check the "RAD" service for client in the SYS_CLNT_SRVC. 
 * Modified by Balaji  On 07-Jul-2009: To Skip record insertion when "RAD" service amount is "Zero" (i.e. '0'). 
 * Modified by Balaji  On 16-Nov-2009: To evaluate "RAD" service from PACK_WEB.FUN_FTCH_RAD_SRVC FUNCTION.
 * -----------------------------------------------------------------   
 * Clave: AAP04
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 21/06/2012
 * Descripción:  Se agregó modificacion para calcular tarifa en EAD, ya no se envia el monto minimo en la funcion 
 * que calcula los descuentos, ahora todo el proceso se hace en el java, debido a que hay que comparar los montos
 * deel total a cobrar contra el monto minimo de EAD, para ver cual de los dos aplica.
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción: 	
 * ------------------------------------------------------------------
 */
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bean.AdditionalService;
import bean.Global;
import bean.JavTariff;
import bean.JavWebBookingGeneralForm;
import bean.JavWebBookingServicesForm;
import bean.PrintFileExport;
import bean.Services;
import bean.ServicesGlobal;
import bean.ServicesTotal;
import bean.ShipmentServiceDetail;
import bean.SucursalesConfiguradas;
import beanUtil.ConnectDB;


public class JavWebBookingServicesAction extends Action {
	//private StringBuffer cnct = new StringBuffer();
	//private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	public JavWebBookingServicesAction() {
		//AAP//AccessLog.Log("JavWebBookingServicesAction");
	}
	
	long outtime;
	//long totaltime;
	long intime;
	public ActionForward perform(ActionMapping am, ActionForm af, HttpServletRequest req, HttpServletResponse res) {
		Connection con=null;
		 String insuranceView="";
		try
		{
			
			//totaltime=System.currentTimeMillis();
			if(af instanceof JavWebBookingServicesForm){

				//AAP//AccessLog.Log("-------------- VERSION 26thmay 12PM");
				JavWebBookingServicesForm aform = (JavWebBookingServicesForm)af;
				HttpSession session = req.getSession(false);
				con = ConnectDB.getConnection();
				con.setAutoCommit(false);
				//For No Session
				if(session==null || session.isNew()){
					return am.findForward("nosession");
				}
				String clientId = (String)session.getAttribute("sClientId");
				if(clientId==null )
					return am.findForward("nosession");
				Global global = (Global)session.getAttribute("sGlobal");
				
				String groupClientId = getGroupClientId(con,session);
				String operation="";
				if(req.getParameter("operation")!=null)
				{
					//AAP//AccessLog.Log(clientId + "inside operation...................");
					operation=req.getParameter("operation");
				}
				if(operation.equalsIgnoreCase("checkbranch"))
				{
					checkBranch(con,session,aform.getEntrega());
				}
				

				session.removeAttribute("editclicked");//For Button Validation In EditDelete JSP for Hitcount is 6
				String onchange = req.getParameter("onchange");
				//AAP//AccessLog.Log(clientId + "onchange==>"+onchange);
				if((onchange!=null && onchange.equalsIgnoreCase("cobertura"))
				   || (onchange!=null && onchange.equalsIgnoreCase("cobertura"))){
					if(aform.getValordeclarado().trim().length()>0) {
						Double.parseDouble(aform.getValordeclarado().trim());//For throwing exception
					}						
				}

				String action=req.getParameter("action");
				String confirm = req.getParameter("confirm");
				String show = req.getParameter("show");

				//Hit Count Settings
				String hitCount=req.getParameter("hitcount");
				String delCount=req.getParameter("delcount");
				//AAP//AccessLog.Log(clientId + "action==>"+action);
				//AAP//AccessLog.Log(clientId + "confirm==>"+confirm);
				//AAP//AccessLog.Log(clientId + "show==>"+show);
				//AAP//AccessLog.Log(clientId + "hitCount==>"+hitCount);
				//AAP//AccessLog.Log(clientId + "delCount==>"+delCount);
				int intDelCount=-1;
				int intHitCount=-1;
				if(delCount!=null && delCount.length()>0){
					intDelCount = Integer.parseInt(delCount);
					intHitCount = Integer.parseInt(hitCount);
					intHitCount = intHitCount-intDelCount;
					hitCount = String.valueOf(intHitCount);
				}
				if(hitCount!=null && hitCount.length()>0){
					session.setAttribute("sHitCount",hitCount);
				}

				//Address Code Maintenance in session
				String addressCode=req.getParameter("addresscode");
				if(addressCode!=null && addressCode.length()>0)
					session.setAttribute("addresscode",addressCode);
				
				/*verifica zona extendida*///aap01				
				String zonaExt = "";				
				//if (aform.getZonaExtendida() == null){
				//AAP//AccessLog.Log(clientId + "entró a obtener zona extendida");
				zonaExt = validaZonaExtendida(con,session);
				aform.setZonaExtendida(zonaExt);
					//request zonaExtendida
				//}
				//AAP//AccessLog.Log(clientId + "zona Extendida "+aform.getZonaExtendida());
				/* *****************************************************************
				 * realiza validaciones de servicios cuando tenga tarifa extendida *
				 * *****************************************************************///AAP02
				if (aform.getZonaExtendida().substring(0,1).equals("Y")){//AAP02
					JavTariff tarifa = new JavTariff();
					if (tarifa.isTarifaInvalida(con, session)){
						aform.setTarifaInvalida("Y");
					}else{
						aform.setTarifaInvalida("N");
					}
				}//AAP02
				
				//Redirection of screens
				String to = req.getParameter("to");
				String screen = req.getParameter("screen");
				//AAP//AccessLog.Log(clientId + "to==>"+to);
				//AAP//AccessLog.Log(clientId + "screen==>"+screen);
				if(to!=null && to.equalsIgnoreCase("bookinggeneral")){
					return am.findForward("webbookinggeneral");
				}else if(to!=null && to.equalsIgnoreCase("bookingdetail")){
					if(screen!=null && screen.equalsIgnoreCase("default"))
						return am.findForward("webbookingdetaildefault");
					else
						return am.findForward("webbookingdetail");
				}

				//Form Validation and Populating fields
				//AAP//AccessLog.Log(clientId + "aform.i==>"+aform.i);
				if(aform.i==0){
					//For Showing Delivery Type
				//	intime=System.currentTimeMillis();
					//AAP//AccessLog.Log(""+ clientId + "....................................................");
					//AAP//AccessLog.Log(""+ clientId + "Getting inside Form Validation and Populating fields");
					getServiceCovered(con,req,aform);					
					//For Acknoledgement Type
					fetchAcknowledgementTypeDesc(con,session,aform);

					//Default Insurance Amount and Populating Insurance Type
					aform.setValordeclarado("1000");
					getInsuranceAmount(con,global,req,aform);
                    session.setAttribute("serFormHit","yes");//For General Form Session Check
				//	outtime=System.currentTimeMillis();
			    //		AccessLog.Log(""+ clientId + "Getting outside Form Validation and Populating fields-->time"+(outtime-intime));
					
				}

				if((action!=null && action.equalsIgnoreCase("calculate")) ||
				   (action!=null && action.equalsIgnoreCase("generate"))){
					
					//outtime=System.currentTimeMillis();
					//AAP//AccessLog.Log(""+ clientId + "....................................................");
					//AAP//AccessLog.Log(""+ clientId + "Getting inside  calculate or generate");
					if(aform.getValordeclarado().trim().length()>0) {
						Double.parseDouble(aform.getValordeclarado().trim());//For throwing exception
					}						

					String defaultScreen = (String)session.getAttribute("defaultservicescreen");
					//boolean isDefault;
                    //isDefault = false;
					//code added here by Kumaran 14th oct
					//code added ends here
                    ArrayList servicesDetailArray=null;
					if(defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes"))
					{
						//isDefault = true;
						servicesDetailArray = (ArrayList)session.getAttribute("servicesDetailDefault");
						
					}
					else
					{
						servicesDetailArray = (ArrayList)session.getAttribute("servicesDetail");
					}
					//AAP//AccessLog.Log(""+ clientId + "servicesDetailArray.size "+servicesDetailArray.size());
					
					boolean isEnvelopealone=true;
					for(int i=0;i<servicesDetailArray.size();i++)
					{
						ShipmentServiceDetail ssd = (ShipmentServiceDetail)servicesDetailArray.get(i);
						if(!ssd.serviceId.equalsIgnoreCase("SHP-E"))
						{
							isEnvelopealone=false;
							break;
						}
					}
					if(isEnvelopealone){
						//intime=System.currentTimeMillis();
						if(aform.getValordeclarado()!=null &&
						   aform.getValordeclarado().trim().length()>0){
							//AAP//AccessLog.Log(""+ clientId + "............................................................");
							//AAP//AccessLog.Log(""+ clientId + "Inside isEnvelopealone ");
							getMessageForEnvelopeAlone(con,aform);
							String query =	"SELECT pm_vlue1_desc,PM_PARM_CODE1 FROM "+
											"sys_parm_mstr Where pm_mdul_id = 'BOK' "+
											"and pm_parm_type = 'COVERAGE' and pm_parm_code2 ='NULL'";
							PreparedStatement pst =con.prepareStatement(query);
							ResultSet rs =pst.executeQuery();
							String label="";
							String value="";
							aform.getInsurancetype().clear();
							aform.getInsurancetypelabel().clear();
							while(rs.next()){
								label = rs.getString(1);
								value = rs.getString(2);
								//code added by kumaran 14 th octo testing
								
																
								//code added by kumaran 14 th octo testing
								aform.setInsurancetypelabel(label);
								
								aform.setInsurancetype(value);
							}
							//Default Selection
							aform.setCobertura("INV-1");
							if(pst!=null)
								pst.close();
							if(rs!=null)
								rs.close();
							session.setAttribute("envelopealone","true");
						//outtime=System.currentTimeMillis();
							return am.findForward("thispage");
						}
					//	outtime=System.currentTimeMillis();
					//AccessLog.Log(""+ clientId + "Time..............Inside isEnvelopealone"+(outtime-intime));
					}
				}



				//Insurance Amount and Populating Insurance Type While Insurance Amount field value changes
				if(onchange!=null && onchange.equalsIgnoreCase("insuranceamount")){
					//intime=System.currentTimeMillis();
					String insuranceAmount=getInsuranceAmount(con,global,req,aform);
					aform.setValordeclarado(insuranceAmount);
					//outtime=System.currentTimeMillis();
					//AAP//AccessLog.Log(""+ clientId + ".....................................................");
					//AccessLog.Log(""+ clientId + "Time..............insuranceamount"+(outtime-intime));
					// return am.findForward("thispage"); // Commented by R.Mohan Babu on 10/04/2004
				}else if(onchange!=null && onchange.equalsIgnoreCase("cobertura")){
				//	intime=System.currentTimeMillis();
					String insuranceInformation = getInsuranceInformation(con,aform.getValordeclarado().trim(),groupClientId,aform,true);
					//AAP//AccessLog.Log(""+ clientId + "******************** INSURANCE INFORMATION "+insuranceInformation);
					//outtime=System.currentTimeMillis();
					//AAP//AccessLog.Log(""+ clientId + ".....................................................");
					//AccessLog.Log(""+ clientId + "Time..............insuranceInformation"+(outtime-intime));
					aform.setSeguro(insuranceInformation);
					//return am.findForward("thispage"); // Commented by R.Mohan Babu on 10/04/2004
				}

                //Below code addded by palanivel
				
			//	intime=System.currentTimeMillis();
                String additionalService = getAdditionalService(con,global,req,aform);
                //AAP//AccessLog.Log(clientId + "additionalService==>"+additionalService);
            //    outtime=System.currentTimeMillis();
                //AAP//AccessLog.Log(""+ clientId + ".....................................................");
               // AccessLog.Log(""+ clientId + "Time ..........After getAdditionalService "+(outtime-intime));
         		aform.setShowAdditional(additionalService) ;
                //Add the additional service in UI
				if(action!=null && action.equalsIgnoreCase("add")){
                    int size = 0;
					AdditionalService serv = new AdditionalService();
					//AAP//AccessLog.Log(""+ clientId + "............................................................");
					//AAP//AccessLog.Log(""+ clientId + "Inside action add if(action!=null && action.equalsIgnoreCase('add')){");
					serv.service=aform.getServiceAdditional().toUpperCase();
					serv.importe = aform.getImporteValue();
					serv.serviceId = aform.getReferenceId();
					ArrayList additionalServicesArray = null ;
					if (session.getAttribute("aditionalServicesDetail") == null)
					{
						additionalServicesArray = new ArrayList();
						size =  additionalServicesArray.size();
						additionalServicesArray.add(serv);
						aform.setServiceAdditional("");
						aform.setImporteValue("");
						aform.setReferenceId("");
                    }
					else
					{
						boolean isAvail =true;
						additionalServicesArray =  (ArrayList)session.getAttribute("aditionalServicesDetail");
						size =  additionalServicesArray.size();
						for(int i=0;i<size;i++)
						{
							AdditionalService as = (AdditionalService)additionalServicesArray.get(i);
							AdditionalService asTemp = new AdditionalService();
							
							asTemp.service = aform.getServiceAdditional().toUpperCase();
							asTemp.importe = aform.getImporteValue();
							asTemp.serviceId = aform.getReferenceId();

							if(as.service.equals(asTemp.service))
							{
								//AAP//AccessLog.Log(""+ clientId + "..............................................................");
								//AAP//AccessLog.Log(""+ clientId + "Inside Writing Error Messages in if(as.service.equals(asTemp.service))");	
								isAvail = false;
								 String messageText="";
								 String query = "{call pack_web.PRO_SHOW_MESG('WEB',PACK_WEB.LANGUAGE_ID,9000041,1,?,NULL,NULL,?,?)}";
								 CallableStatement cst = null;
								 try {
									 cst = con.prepareCall(query);
									 cst.setString(1,aform.getServiceAdditional());
									 cst.registerOutParameter(2,Types.VARCHAR);
									 cst.registerOutParameter(3,Types.VARCHAR);
									 cst.executeQuery();
									 messageText = cst.getString(3);
								 } catch(Exception ex) {
									 ex.printStackTrace();
								 } finally {
									 try {			 
										 if(cst!=null)
											 cst.close();				 
									 }
									 catch(Exception e){
										 e.printStackTrace();
									 }
								 }
								 req.setAttribute("errMsgAdditional",messageText);
								 break;
							}
						}

                        if(isAvail)
						{
							additionalServicesArray.add(serv);
							aform.setServiceAdditional("");
							aform.setImporteValue("");
							aform.setReferenceId("");
						}
					}
						
					session.setAttribute("aditionalServicesDetail",additionalServicesArray);
					return am.findForward("thispage");
				}
				//delete the additional service in UI
				 if(action!=null && action.equalsIgnoreCase("delete"))
				{
					    
						ArrayList additionalServicesArray = null ;
						int index = Integer.parseInt(req.getParameter("index"));
						additionalServicesArray =  (ArrayList)session.getAttribute("aditionalServicesDetail");
						additionalServicesArray.remove(index);
                        session.setAttribute("aditionalServicesDetail",additionalServicesArray);
					    return am.findForward("thispage");
				}
                //End of code


				if(show!=null && show.equalsIgnoreCase("aditional")){
					calculateServices(con,aform,req);
					session.setAttribute("valorCod",aform.getValorcod());
					insuranceView=getInsuranceViewFlag(con,session,req);
					//AAP//AccessLog.Log(clientId + "insuranceView ==>"+insuranceView);
					//AAP//AccessLog.Log(clientId + "global.displayAmountFlag ==>"+global.displayAmountFlag);
					if((global.displayAmountFlag != null) && (global.displayAmountFlag.equalsIgnoreCase("Y"))) 
					{
						return am.findForward("webbookingservicesaditional");
					}
					return am.findForward("webbookingnullservicesaditional");
				}
					
				if(action!=null && action.equalsIgnoreCase("calculate")){
					
					
					if(!beforeCredit(con,session,req))
						return am.findForward("thispage");
				//	intime=System.currentTimeMillis();
					if(!validateFields(con,req,aform))
						return am.findForward("thispage");
				//	outtime=System.currentTimeMillis();
				//	AccessLog.Log(""+ clientId + "Time ..........After validateFields in calculate "+(outtime-intime));

					//calculateServices(con,aform,req);
					return am.findForward("thispage");
				}

				
				if(action!=null && action.equalsIgnoreCase("generate")){
				
				//code added by sundar on 29/10/2003 for displaying description,content 
					//AAP//AccessLog.Log(""+ clientId + "..................................................");
					//AccessLog.Log(""+ clientId + "inside generate  in time" + System.currentTimeMillis());
					
				String desc=(String)session.getAttribute("sdescription");
				String cont=(String)session.getAttribute("scontent");
				//code added by kumaran 14 th octo testing
				
				
					if(updateDescCont(con,global.clientId,desc,cont))
				    {
						//AAP//AccessLog.Log(""+ clientId + "*****value updated successfully******");
						//code added by kumaran 14 th octo testing
					}

					//intime=System.currentTimeMillis();
				//	AccessLog.Log(""+ clientId + "Time .............before validate fields time taken"+(intime-totaltime));
					if(!beforeCredit(con,session,req))
						return am.findForward("thispage");
					if(!validateFields(con,req,aform))
						return am.findForward("thispage");
					//calculateServices(con,aform,req);
					//outtime=System.currentTimeMillis();
					//AccessLog.Log(""+ clientId + "Time ..........After validateFields in calculate "+(outtime-intime));
					//intime=System.currentTimeMillis();
					String formNumber = getFormNumber(con,global);
				
					//outtime=System.currentTimeMillis();
				//	AccessLog.Log(""+ clientId + "Time ..........After getting Form number "+(outtime-intime));
					if(formNumber !=null && formNumber.length()>0)
					{
						
						aform.setGuiano(formNumber);
						//code added by kumaran 14 th octo testing
					}
                    AccessLog.Log(""+ clientId + " %%%%%%%%%%% FORM NUMBER "+formNumber);
					if(confirm==null){
						aform.setConfirmgenerate("yes");
						
					}else if(confirm!=null && confirm.equalsIgnoreCase("yes")){
						//intime=System.currentTimeMillis();
						if(generateGuia(con,aform,req,formNumber)){
							
							con.commit();
							//outtime=System.currentTimeMillis();
						//	AccessLog.Log(""+ clientId + "Time ..........After generating guia "+(outtime-intime));
							//aform.setSuccessmessage("SU GUIA HA SIDO DOCUMENTADA CON ÉXITO!");
							//req.setAttribute("successmessage","SU GUIA HA SIDO DOCUMENTADA CON ÉXITO!");
							return am.findForward("downloadpage");
						}else{
						//	outtime=System.currentTimeMillis();
						//	AccessLog.Log(""+ clientId + "Time ..........After generating guia "+(outtime-intime));
							return am.findForward("thispage");
						}
					}
					return am.findForward("thispage");
				}

				/*else if(action!=null && action.equalsIgnoreCase("calculate")){
				if(!validateFields(con,req,aform))
				return am.findForward("thispage");
				calculateServices(con,aform,req);
				}*/

				//For No Records Entered in Services Detail
				String norecords = req.getParameter("norecords");
				if(norecords!=null && norecords.equalsIgnoreCase("yes")){
					if(screen!=null && screen.equalsIgnoreCase("default"))
						return am.findForward("webbookingdetaildefault");
					else
						return am.findForward("webbookingdetail");
				}
			}

		}catch(NumberFormatException nfe){
			AccessLog.Log(nfe);
			AccessLog.Log("INSIDE CATCH ");
			req.setAttribute("valordeclarado","nan");
			return am.findForward("thispage");
		}catch(Exception e){
			 try{
				 con.rollback();
			 }catch(Exception ex){
				 AccessLog.Log(ex);
			 }
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

	public void getMessageForEnvelopeAlone(Connection con,
										   JavWebBookingServicesForm serForm)throws Exception{

		String query = "{call pack_web.PRO_SHOW_MESG('BOK',PACK_WEB.LANGUAGE_ID,900149,1,NULL,NULL,NULL,?,?)}";//Checked
		CallableStatement cst=con.prepareCall(query);
		cst.registerOutParameter(1,Types.VARCHAR);
		cst.registerOutParameter(2,Types.VARCHAR);
		cst.executeQuery();
		String messageText = cst.getString(2);
		serForm.setErrmsgenvelope(messageText);
		if(cst!=null)
			cst.close();
	}


	public void getServiceCovered(Connection con,
								  HttpServletRequest req,
								  JavWebBookingServicesForm serForm) throws Exception {

		HttpSession session=req.getSession(false);
		Global global =(Global)session.getAttribute("sGlobal");
		String groupClientId = getGroupClientId(con,session);
		String query = "{call pack_web.pro_ftch_srvs_covd(?,?,?,?,?) }";//Checked
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
			String query1 = "select pack_web.fun_dflt_dlvy(?,?) from dual";//Checked
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
		String groupIdQuery = "select pack_web.FUN_CHCK_GRUP_CLNT(?)as groupid from dual";//Checked
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

		return groupClientId.toUpperCase();
	}
	public String getBranchColony(Connection con,HttpSession session)throws Exception {

		JavWebBookingGeneralForm generalForm = (JavWebBookingGeneralForm)session.getAttribute("webBookinggeneral");
		String query = "{ call pack_web.pro_brnc_coly(?,?,?,?,?) }";//Checked
		CallableStatement cst = con.prepareCall(query);
		cst.setString(1,generalForm.citycode);
		cst.setString(2,generalForm.destinationcoloniacode);
		cst.setString(3,generalForm.destinationcode);
		cst.registerOutParameter(4,Types.VARCHAR);
		cst.registerOutParameter(5,Types.VARCHAR);
		cst.executeQuery();

		String lcZone = cst.getString(4);
		String lcBoolean = cst.getString(5);
		String errorMessage="";
		if(lcBoolean!=null && lcBoolean.equalsIgnoreCase("false")){
			errorMessage=getErrorMessage(con,"900169");
			return errorMessage;
		}
		else if(lcZone==null){
			errorMessage=getErrorMessage(con,"900194");
			return errorMessage;
		}
		if(cst!=null)
			cst.close();

		return errorMessage ;
	}

	public String getErrorMessage(Connection con,String number)throws Exception {
		CallableStatement cst=null;
		cst=con.prepareCall("{ call pack_web.pro_show_mesg('BOK',pack_web.language_id,?,1,NULL,NULL,NULL,?,?) }");//Checked
		cst.setInt(1,Integer.parseInt(number));
		cst.registerOutParameter(2,Types.VARCHAR);
		cst.registerOutParameter(3,Types.VARCHAR);
		cst.executeQuery();
		String errMsg = cst.getString(3);

		if(cst!=null)
			cst.close();
		return errMsg;
	}

	public String getAcknowledgementType(Connection con,HttpSession session)throws Exception {
		Global global = (Global)session.getAttribute("sGlobal");
		PreparedStatement pst = con.prepareStatement("select  wc_ack_type from WEB_CLNT_MSTR where wc_clnt_id =?");//Checked
		pst.setString(1,global.clientId);
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

	public void fetchAcknowledgementTypeDesc(Connection con,
											 HttpSession session,
											 JavWebBookingServicesForm serForm)throws Exception {
		Global global =(Global)session.getAttribute("sGlobal");
		String query=null;
		boolean checkTariffType=true;
		String ackType = null;
		if(global.tarifType.equalsIgnoreCase("G")){
			query=	"	SELECT pm_vlue1_desc,pm_parm_code1 FROM sys_parm_mstr "+
					"	WHERE pm_parm_type = 'ACK_TYPE'"+
					"	AND pm_parm_code1 = ?";//Chceked

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
					"	WHERE pm_parm_type = 'ACK_TYPE'";//Checked
			checkTariffType=false;
		}

		PreparedStatement pst = con.prepareStatement(query);
		if(checkTariffType)
			pst.setString(1,ackType);

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
		String defaultQuery =	"SELECT CS_DEFA_SRVC_item FROM SYS_CLNT_SRVC "+
								"WHERE CS_CLNT_ID = ? AND CS_SRVC_ID =? ";//Checked
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
		//AAP//AccessLog.Log(""+ global.clientId + "DEFAULT SELECTED FOR ACUSE DE RECIBO "+serForm.getAcusederecibo());
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
	}

	public String getInsuranceAmount(Connection con,
									 Global global,
									 HttpServletRequest req,
									 JavWebBookingServicesForm serForm)throws Exception {

		HttpSession session = req.getSession(false);
		String groupClientId = getGroupClientId(con,session);
		String query ="{ call pack_web.PRO_CLNT_INSU_FLAG(?,?,?,?,?) }";//Checked
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

		//AAP//AccessLog.Log(""+ global.clientId + "-------------------------------------------------------------------------------------");
		//AAP//AccessLog.Log(""+ global.clientId + "**************************** -------------EXPIERY DATE "+expiryDate);
		//AAP//AccessLog.Log(""+ global.clientId + "**************************** -------------SYSDATE  "+sysDate);

		if(valorDeclarado!=null && valorDeclarado.length()>0)
			intValorDeclarado=Double.parseDouble(valorDeclarado);

		if(policyNumber!=null && ((expiryDate!=null && expiryDate.after(sysDate)) || (expiryDate!=null && expiryDate.equals(sysDate)))){
			valorDeclarado = null;
			lc_inv_vlue="CLN";
			serForm.setValordeclarado("");
			session.setAttribute("valorvalue","null");
			//AAP//AccessLog.Log(" 1 ");
		}else if(policyNumber!=null && (expiryDate!=null && expiryDate.before(sysDate)) && insuranceFlag.equalsIgnoreCase("N")){
			valorDeclarado =null;
			lc_inv_vlue="NO";
			serForm.setValordeclarado("");
			session.setAttribute("valorvalue","null");
			//AAP//AccessLog.Log(" 2 ");
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
			session.setAttribute("valorvalue","null");
			//AAP//AccessLog.Log(" 4 ");
		}else if(insuranceFlag.equalsIgnoreCase("Y")){
			if(valorDeclarado==null  || valorDeclarado.length()==0){
				lc_inv_vlue="NO";
				//AAP//AccessLog.Log(" 5 , IF");
			}else if(intValorDeclarado<= 1000){
				lc_inv_vlue="LTH";
				//AAP//AccessLog.Log(" 5 , 2");
			}else if(intValorDeclarado>1000 && intValorDeclarado<=5000){
				lc_inv_vlue="GTH";
				//AAP//AccessLog.Log(" 5 , 3");
			}else if(intValorDeclarado > 5000 && intValorDeclarado <=10000000){
				lc_inv_vlue = "GTF";
				//AAP//AccessLog.Log(" 5 , 4");
			}
			//AAP//AccessLog.Log(" 5 ");
		}
		//AAP//AccessLog.Log(""+ global.clientId + "VALOR DECLARADO "+valorDeclarado);

		//For Insurance Type
		getInsuranceType(con,lc_inv_vlue,serForm);
		//req.setAttribute("insurancetype",insuranceType);

		//For Insurance Infomation Display
		String insuranceInformationDisp = getInsuranceInformation(con,
																  valorDeclarado,
																  groupClientId,
																  serForm,
																  false);
		serForm.setSeguro(insuranceInformationDisp);
		//AAP//AccessLog.Log(""+ global.clientId + "$$$$$$$$$$$$$$$$$$ INSURANCE INFORMATIOM DISPLAY "+insuranceInformationDisp);

		return valorDeclarado;
	}

	public void getInsuranceType(Connection con,
								 String lc_inv_vlue,
								 JavWebBookingServicesForm serForm)throws Exception {

		//Hashtable col =new Hashtable();
	
		String query =	"SELECT pm_vlue1_desc,pm_parm_code1 "+
						"FROM sys_parm_mstr "+
						"Where pm_mdul_id=? "+
						"and pm_parm_type = ? "+
						"and pm_parm_code2 = ?";//Checked
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
			//AAP//AccessLog.Log("VALUE INSIDE GET INSURANCE TYPE"+value);
			//AAP//AccessLog.Log("LABEL INSIDE GET INSURANCE TYPE"+label);
			serForm.setInsurancetypelabel(label);
			serForm.setInsurancetype(value);
		}
		//AAP//AccessLog.Log("INSIDE INSURANCE TYPE ");
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
										  JavWebBookingServicesForm serForm,
										  boolean onchange)throws Exception {

		//Default Selected
		ArrayList insuranceTypeValue  = serForm.getInsurancetype();
		ArrayList insuranceTypeLabel = serForm.getInsurancetypelabel();
		String value=null;
		String label=null;
		/*AccessLog.Log("COBERTURA IN SIDE INFORMATION "+serForm.getCobertura());
		for(int i=0;i<insuranceTypeLabel.size();i++){
		AccessLog.Log("LABEL INSIDE FOR  "+(String)insuranceTypeLabel.get(i));
		AccessLog.Log("VALUE INSIDE FOR  "+(String)insuranceTypeValue.get(i));
		}*/
		if(!onchange){
			value= (String)insuranceTypeValue.get(0);
			label= (String)insuranceTypeLabel.get(0);
		}else{
			value= serForm.getCobertura();
		}

		//AAP//AccessLog.Log("VALUE IN GET INSURANCE INFORMATION "+value);
		//AAP//AccessLog.Log("LABEL  IN GET INSURANCE INFORMATION "+label);
		String query = "{ call pack_web.pro_srvc_info(?,?,?,?) }";//Checked
		CallableStatement cst = con.prepareCall(query);
		cst.setString(1,value);

		if(insuranceAmount==null  || (insuranceAmount!=null && insuranceAmount.length()==0))
			//cst.setDouble(2,0.0d);//Empty
			cst.setNull(2,Types.NUMERIC);
		else
			cst.setDouble(2,Double.parseDouble(insuranceAmount));

		cst.setString(3,groupClientId);
		cst.registerOutParameter(4,Types.VARCHAR);

		cst.executeQuery();
		String insInfo = cst.getString(4);
		if(cst!=null)
			cst.close();
		//AAP//AccessLog.Log("INSURANCE INFORMATION "+insInfo);
		return insInfo;
	}

	public boolean generateGuia(Connection con,
								JavWebBookingServicesForm serForm,
								HttpServletRequest req,
								String formNumber)throws Exception {

		HttpSession session =req.getSession(false);
		Global global = (Global)session.getAttribute("sGlobal");
		PreparedStatement pst=null;
		ResultSet rs=null;

		String checkFormQuery =	"select 1 from bok_guia_head where GH_FORM_NO=? and "+
								"GH_PREP_BRNC_ID = ? and GH_DOCU_TYPE= ?";//Checked
		pst = con.prepareStatement(checkFormQuery);
		pst.setString(1,serForm.getGuiano().trim().toUpperCase());
		pst.setString(2,global.assignedBranch.trim());
		pst.setString(3,"Q");
		rs = pst.executeQuery();

		if(rs.next())
		{
			serForm.setDuplicateguianumber("EL NUMERO DE FORMA YA EXISTE");
			return false;
		}
		if(pst!=null)
			pst.close();
		if(rs!=null)
			rs.close();

		String guiaQuery = "select pack_web.FUN_GEN_GUIA_NO(?) from dual";//Checked
		pst = con.prepareStatement(guiaQuery);
		pst.setString(1,global.assignedBranch);
		rs = pst.executeQuery();
		String genGuiaNumber=null;
		//java.sql.Date sysDate = null;
		if(rs.next()){
			genGuiaNumber= rs.getString(1);
		}

		if(pst!=null)
			pst.close();
		if(rs!=null)
			rs.close();

		//BOK_GUIA_HEAD insertion
		ServicesTotal servicesTotal = (ServicesTotal)session.getAttribute("servicestotal");
		JavWebBookingGeneralForm generalForm = (JavWebBookingGeneralForm)session.getAttribute("webBookinggeneral");
		//java.sql.Date sysDate=getSysdate(con);--commented and below line coded by B.Emerson on 19/07/2003
		java.sql.Timestamp	sysDate=getTimeStamp(con);
		String branchId = generalForm.getDestinationsitecode();
		String entriga=serForm.getEntrega();
		String entrega = "";//AAP02
		System.out.println("********************* JavWebBookingServiceAction.generateGuia()_generalForm.getDestinationcode().substring(3) "+generalForm.getDestinationcode().substring(3));
		/*se asigna el tipo de búsqueda de configuracion en base al tipo de entrega*/
		if (entriga.equalsIgnoreCase("1")) {//AAP02					
			entrega = "DEST_OCURRE";
		} else {
			if (!generalForm.getDestinationcode().substring(3).equals("70")) {
				entrega = "DEST_EAD";
			}
		}
		if (entrega.length() > 0) {
			/*se busca configuracion*/	
			SucursalesConfiguradas suc = new SucursalesConfiguradas();//AAP02			
			String nuevaSucursal = suc.obtieneConfigSucursal(con, "BOK", entrega, branchId);//AAP02
			
			/*en caso de no encontrar configuracion, no realiza ninguna modificacion a la sucursal.
			 * se deja la sucursal obtenida desde un principio*/
			if (nuevaSucursal.length()>0) {//AA02
				generalForm.setDestinationcode(nuevaSucursal);
				generalForm.setCur_dest(nuevaSucursal);
				session.setAttribute("webBookinggeneral",generalForm);
				global.destinationBranchId=generalForm.getDestinationcode();
				//AAP//AccessLog.Log("The destination code is "+global.destinationBranchId);
				session.setAttribute("sGlobal",global);
			}
		}
		
		//se comentaron lineas porque ahora se realiza este proceso con configuracion
//		if(branchId.equalsIgnoreCase("GDL"))
//		{
//			if(entriga.equalsIgnoreCase("1"))
//			{
//				generalForm.setDestinationcode("GDL01");
//				generalForm.setCur_dest("GDL01");
//			}
//			else
//			{
//				generalForm.setDestinationcode("GDL02");
//				generalForm.setCur_dest("GDL02");
//			}
//			session.setAttribute("webBookinggeneral",generalForm);
//			global.destinationBranchId=generalForm.getDestinationcode();
//			//AAP//AccessLog.Log("The destination code is "+global.destinationBranchId);
//			session.setAttribute("sGlobal",global);
//			
//		}
		//ConID
		String conIdQuery = "{ call pack_web.pro_defa_cont(?,?,?,?,?,?) }";//Checked
		CallableStatement cst = con.prepareCall(conIdQuery);
		cst.setString(1,generalForm.getOrgionaddresscode());
		cst.setString(2,global.clientId);
		cst.registerOutParameter(3,Types.VARCHAR);
		cst.registerOutParameter(4,Types.VARCHAR);
		cst.registerOutParameter(5,Types.VARCHAR);
		cst.registerOutParameter(6,Types.VARCHAR);
		cst.executeQuery();

		String cont_id = "";
		String iden_type="";
		String iden_no="";
		String cont_name="";
		cont_id = cst.getString(3);
		iden_type = cst.getString(4);
		iden_no = cst.getString(5);
		cont_name = cst.getString(6);
 
        //AAP//AccessLog.Log(""+global.clientId+"&&&&&&&&&&&&&&&&&&&&&&& CONTACT INFORMATION "+cont_id);
		//AAP//AccessLog.Log(""+global.clientId+"&&&&&&&&&&&&&&&&&&&&&&& CONTACT INFORMATION IDEN_TYPE "+iden_type);
		//AAP//AccessLog.Log(""+global.clientId+"&&&&&&&&&&&&&&&&&&&&&&& CONTACT INFORMATION IDEN_NO "+iden_no);
		//AAP//AccessLog.Log(""+global.clientId+"&&&&&&&&&&&&&&&&&&&&&&& CONTACT INFORMATION CONT_NAME "+cont_name);

		if(rs!=null)
			rs.close();
		if(cst!=null)
			cst.close();
		//End ConID

		//SLMN Id
		String slmnQuery = "select pack_web.fun_slmn_id(?) from dual";//Checked
		pst = con.prepareStatement(slmnQuery);
		String orginSlmnId = null;
		String destSlmnId = null;
		for(int i=1;i<=2;i++){
			if(i==1)
				pst.setString(1,global.clientId);
			if(i==2)
				pst.setString(1,generalForm.getDestinationclave());
			rs = pst.executeQuery();
			if(rs.next()){
				if(i==1)
					orginSlmnId=rs.getString(1);
				else
					destSlmnId=rs.getString(1);
			}
		}
		if(rs!=null)
			rs.close();
		if(pst!=null)
			cst.close();
		//End  SLMN Id

		String eadFlag = (serForm.getEntrega().equals("2")?"1":"0");
		String defaultEadRout=null;
		if(serForm.getEntrega().equals("2")){
			defaultEadRout = getDefaultEadRout(con,session);
		}

		String invoiceFlag="N";
		if(getInvoiceFlag(con,global))
			invoiceFlag = ((generalForm.getFiscaladdresscode()!=null && global.rfc!=null)?"Y":"N");

		/*verifica si es zona extendida para obtener los datos del operador logistico*/
		//String rutaEntrega = generalForm.getLc_rout();//AAP01//AAP05
		String bandOperLog = "";//AAP01
		ArrayList datos = null;//AAP01
		if (serForm.getZonaExtendida().substring(0, 1).equals("Y")){//AAP01
			datos = obtieneInfOperadorLogistico(con,serForm);//AAP01
			bandOperLog = datos.get(0).toString();//AAP01
			//rutaEntrega = datos.get(1).toString();//AAP01//AAP05
			defaultEadRout = datos.get(1).toString();//AAP05
		}//AAP01
		
		String insertQuery=	"insert into bok_guia_head "+
							"(GH_ORGN_BRNC_ID,"+
							"GH_DEST_BRNC_ID,"+
							"GH_BILL_BRNC_ID ,"+
							"GH_GUIA_NO ,"+
							"GH_GUIA_TYPE ,"+
							"GH_GUIA_AMNT ,"+
							"GH_PYMT_MODE ,"+
							"GH_PYMT_TYPE ,"+
							"GH_GUIA_STUS,"+
							"GH_PAID_DATE,"+
							"GH_ISSE_DATE,"+
							"GH_ORGN_CLNT_ID,"+
							"GH_ORGN_CLNT_NAME ,"+
							"GH_DEST_CLNT_ID,"+
							"GH_DEST_CLNT_NAME,"+
							"GH_BILL_CLNT_NAME,"+
							"GH_BILL_CLNT_ID,"+
							"GH_BOOK_USER_ID,"+
							"GH_BOOK_CONT_ID,"+
							"CRTD_ON,"+
							"CRTD_BY,"+
							"MDFD_ON,"+
							"MDFD_BY,"+
							"GH_GUIA_SUB_FLAG,"+
							"GH_CONV_FLAG,"+
							"GH_ORGN_SLMN_ID,"+
							"GH_DEST_SLMN_ID,"+
							"GH_NO_TRIF,"+
							"GH_PREP_BRNC_ID,"+
							"GH_ACTV_FLAG,"+
							"GH_DOCU_TYPE,"+
							"GH_NUMB_PACK,"+
							"GH_EAD_FLAG ,"+
							"GH_RAD_FLAG,"+
							"GH_ANMO_EXST,"+
							"GH_LOAD_STUS,"+
							"GH_TOTL_WGHT,"+
							"GH_TOTL_VLUM,"+
							"GH_TOTL_DECL_VLUE,"+
							"GH_FORM_NO,"+
							"GH_GUIA_SUMD,"+
							"GH_CRED_PERD,"+
							"GH_INVC_FLAG,"+
							"GH_PEDI_NUMB,"+
							"GH_CUST_AGNT,"+
							"GH_SHIP_SEQN,"+
							"GH_NO_SHIP,"+
							"GH_CURR_LOCA,"+
							"GH_CURR_DEST,"+
							"GH_DEFA_ROUT,"+
							"GH_COMT,"+
							"GH_EAD_DEFA_ROUT,"+
							"GH_CONC_IDEN_TYPE,"+
							"GH_CONC_IDEN_NO"+
							",GH_ORG_SITE_ID,"+
							"GH_DEST_SITE_ID," +
							"GH_FLAG_1)"+//AAP01
							"values ("+
							"?,?,?,?,?,?,?,?,?,?, "+
							"?,?,?,?,?,?,?,?,?,?, "+
							"?,?,?,?,?,?,?,?,?,?, "+
							"?,?,?,?,?,?,?,?,?,?, "+
							"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";//Checked//AAP01
		
		if((global.destinationBranchId==null)||(global.destinationBranchId.length()<=0))
		{
		req.setAttribute("errormsgstatus","There is a problem in SESSION , please close the page and open once again");
		AccessLog.Log("There is a problem in SESSION");
		return false;
		}
		pst = con.prepareStatement(insertQuery);
		pst.setString(1,global.assignedBranch);
		
		
		pst.setString(2,global.destinationBranchId);
		pst.setString(3,global.assignedBranch);
		pst.setString(4,genGuiaNumber);
		pst.setString(5,"H");
		pst.setDouble(6,Double.parseDouble(servicesTotal.total));
		pst.setString(7,"PAID");
		pst.setString(8,"C");
		pst.setString(9,"WBK");
		pst.setString(10,"");
		//pst.setDate(11,sysDate);--commented and below line coded by B.Emerson on 19/07/2003
		pst.setTimestamp(11,sysDate);
		pst.setString(12,global.clientId);
		pst.setString(13,global.clientName);
		pst.setString(14,generalForm.getDestinationclave());
		pst.setString(15,generalForm.getDestinationnombre());
		pst.setString(16,global.clientName);
		pst.setString(17,global.clientId);
		pst.setString(18,global.clientId);
		pst.setString(19,cont_id);
		pst.setTimestamp(20,sysDate);//pst.setTimestamp(20,getTimeStamp(con));--commented by B.Emerson on 19/07/2003
		pst.setString(21,global.clientId);
		pst.setTimestamp(22,sysDate);//pst.setTimestamp(22,getTimeStamp(con));--commented by B.Emerson on 19/07/2003
		pst.setString(23,global.clientId);
		pst.setString(24,"N");
		pst.setString(25,"N");
		pst.setString(26,orginSlmnId);
		pst.setString(27,destSlmnId);
		pst.setString(28,"N");
		pst.setString(29,global.assignedBranch);
		pst.setString(30,"A");
		pst.setString(31,"Q");
		pst.setDouble(32,Double.parseDouble(servicesTotal.totalPack));
		pst.setString(33,eadFlag);
		pst.setString(34,"1");
		pst.setString(35,"N");
		pst.setString(36,"N");
		pst.setDouble(37,Double.parseDouble(servicesTotal.totalWeight));
		pst.setDouble(38,Double.parseDouble(servicesTotal.totalVolume));
		if(serForm.getValordeclarado().trim().length()>0)
			pst.setDouble(39,Double.parseDouble(serForm.getValordeclarado().trim()));
		else
			//pst.setDouble(39,0.0d);//Empty
			pst.setNull(39,Types.NUMERIC);
		pst.setString(40,serForm.getGuiano().trim().toUpperCase());
		pst.setString(41,"N");
		pst.setInt(42,0);
		pst.setString(43,invoiceFlag);
		pst.setString(44,generalForm.getPedinumber().trim().toUpperCase());
		pst.setString(45,generalForm.getCustagent().trim().toUpperCase());
		pst.setString(46,generalForm.getShip_seqn());
		
		//Modified By Sam [14-06-2006] 
		if(generalForm.getNo_ship() != null && generalForm.getNo_ship().trim().length() >0){
			pst.setInt(47,Integer.parseInt(generalForm.getNo_ship()));
		}else{
			pst.setNull(47,Types.INTEGER);
		}

		pst.setString(48,global.assignedBranch);
		pst.setString(49,global.destinationBranchId);
		//pst.setString(50,rutaEntrega);//AAP01//AAP05
		pst.setString(50,generalForm.getLc_rout());//AAP05


		String comText="";
		if((generalForm.getFiscaladdresscode()==null || generalForm.getFiscaladdresscode().length()==0)
		   || global.rfc==null){
			 comText= global.commentText+"No Proporciono RFC, No sustitucion de factura;"
							 +serForm.getComments().trim().toUpperCase();
			 if (serForm.getReference()!=null && serForm.getReference().length()>0)
				 comText=comText+":"+serForm.getReference().trim().toUpperCase();
			pst.setString(51,comText);
		}else{
			comText = global.commentText+serForm.getComments().trim().toUpperCase();
			if (serForm.getReference()!=null && serForm.getReference().length()>0)
				 comText=comText+":"+serForm.getReference().trim().toUpperCase();

			pst.setString(51,comText);
		}
		pst.setString(52,defaultEadRout);
		pst.setString(53,iden_type);
		pst.setString(54,iden_no);
		pst.setString(55,global.assignedSite);
		pst.setString(56,generalForm.getDestinationsitecode());
		pst.setString(57,bandOperLog);//AAP01
		pst.executeUpdate();
        //Addres Insertion
		insertBookGuiaAddress(con,session,genGuiaNumber);
		//Service Item Insertion
		insertBookGuiaServiceItem(con,session,genGuiaNumber,sysDate);

		//Services Insertion with Service Item
		insertBookGuiaServices(con,session,genGuiaNumber,sysDate,serForm,req);

		insertBookGuiaStatus(con,session,generalForm,genGuiaNumber);

		//refernce insertion coded by B.Emerson on 24/07/2003. The insertion will take place only when there is value for the reference field
		if (serForm.getReference()!=null && serForm.getReference().length()>0)
			insertReferenceRecord(con,session,genGuiaNumber,serForm.getReference().trim().toUpperCase(),sysDate);



		//Form number Update
		if(formNumber!=null && formNumber.length()>0){
			if(serForm.getGuiano().equals(formNumber)){
				formNumber= formNumber.substring(2,formNumber.length()); 
				//formNumber= formNumber.substring(1,formNumber.length());
				int intFormNumber=Integer.parseInt(formNumber);
                int totQty=Integer.parseInt(servicesTotal.totalPack);//added by sundar on 07/10/2003
				//AAP//AccessLog.Log(""+global.clientId+"+++++++!!!!!!-----total quantity !!!!!!+++++++"+totQty);
				//AAP//AccessLog.Log(""+global.clientId+"&*&*&&*&*&&*& INTEGER FORM NUMBER "+intFormNumber);

                //code added by sundar on 26/09/2003 for autoincrementing labelused
				/* below code commented by sundar on 07/10/2003
				String updateQuery =	"	UPDATE web_clnt_mstr SET WC_FORM_LAST=?,WC_LABL_USED=Nvl(wc_labl_used,0)+1"+
										"	WHERE WC_CLNT_ID = ? ";*/
                //code updated by sundar on 07/10/2003

				// code below commented and update below by Kavitha on 25/10/2010 - Starts
				/*
				String updateQuery =	"	UPDATE web_clnt_mstr SET WC_FORM_LAST=?,WC_LABL_USED=Nvl(wc_labl_used,0)+?"+
										"	WHERE WC_CLNT_ID = ? ";
										PreparedStatement updatePst = con.prepareStatement(query);
				updatePst.setInt(1,intFormNumber);
				updatePst.setInt(2,totQty);	//added by sundar on 07/10/2003
				updatePst.setString(3,global.clientId);
				updatePst.executeUpdate();
				*/
				String query = "update sys_parm_mstr set pm_vlue1_id = ?, mdfd_on = SYSDATE, mdfd_by = ? " +
				" where pm_mdul_id = 'WEB'  AND pm_parm_type = 'FORM_NO'  AND pm_parm_code1 = ? "
				+" AND pm_parm_code2 = 'WW'";

				PreparedStatement updatePst = con.prepareStatement(query);
				updatePst.setInt(1,intFormNumber);
				updatePst.setString(2,session.getAttribute("sClientId").toString());
				updatePst.setString(3,global.assignedBranch);
				updatePst.executeUpdate();
				if(updatePst!=null)
					updatePst.close();
//				code added by Kavitha on 25/10/2010 - Ends
			}
		}

		printFileForGuia(con,req,serForm,genGuiaNumber,cont_id,defaultEadRout,invoiceFlag,cont_name,comText,sysDate);//sysdate added by B.Emerson on 19/07/2003
		//printFileForBcl(con,req,serForm,genGuiaNumber,cont_id,defaultEadRout,invoiceFlag,cont_name);
		return true;
	}
	public boolean getInvoiceFlag(Connection con,Global global)throws Exception{
		CallableStatement cst = con.prepareCall("Begin ? := pack_web.fun_chk_inv_req(?); End;");//Checked
		cst.registerOutParameter(1,Types.VARCHAR);
		cst.setString(2,global.clientId);
		cst.executeQuery();
		boolean isTrue=false;

		String temp = cst.getString(1);
		if(temp!=null && temp.equalsIgnoreCase("Y"))
			isTrue=true;
		else
			isTrue=false;

		if(cst!=null)
			cst.close();

		return isTrue;
	}

	public String getDefaultEadRout(Connection con,HttpSession session)throws Exception {
		JavWebBookingGeneralForm generalForm = (JavWebBookingGeneralForm)session.getAttribute("webBookinggeneral");
		PreparedStatement pst = con.prepareStatement("select pack_web.fun_ftch_ead_rout(?,?) from dual");//Checked
		pst.setString(1,generalForm.destinationcode);
		pst.setString(2,generalForm.destinationcoloniacode);
		ResultSet rs = pst.executeQuery();
		String rout=null;
		if(rs.next()){
			rout = rs.getString(1);
		}
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();

		return rout;
	}

	public void insertBookGuiaStatus(Connection con,
									 HttpSession session,
									 JavWebBookingGeneralForm generalForm,
									 String genGuiaNumber)throws Exception {
		Global global =(Global)session.getAttribute("sGlobal");
		String query = "{call pack_web.PRO_INSRT_WEB_BOK_STUS(?,?,?,?,?,?)}";//Checked
		CallableStatement cst  = con.prepareCall(query);
		cst.setString(1,genGuiaNumber);

		// Modified By Sam[14-06-06]
		if(generalForm.getShip_seqn() != null && generalForm.getShip_seqn().trim().length() >0){
			cst.setInt(2,Integer.parseInt(generalForm.getShip_seqn()));
		}else{
			cst.setNull(2,Types.INTEGER);
		}


		//cst.setInt(2,Integer.parseInt(generalForm.getShip_seqn()));

		// Modified By Sam[14-06-06]
		if(generalForm.getNo_ship() != null && generalForm.getNo_ship().trim().length() >0){
			cst.setInt(3,Integer.parseInt(generalForm.getNo_ship()));
		}else{
			cst.setNull(3,Types.INTEGER);
		}
		
		cst.setString(4,global.assignedBranch);
		cst.setString(5,global.destinationBranchId);
		cst.setString(6,global.clientId);
		cst.executeQuery();
		if(cst!=null)
			cst.close();

	}

	public void	insertBookGuiaAddress(Connection con,
									  HttpSession session,
									  String genGuiaNumber)throws Exception {


		Global global = (Global)session.getAttribute("sGlobal");
		JavWebBookingGeneralForm generalForm = (JavWebBookingGeneralForm)session.getAttribute("webBookinggeneral");
		String addressQuery = "{call pack_web.PRO_INSRT_WEB_BOK_ADDR(?,?,?,?,?)}";//Checked
		CallableStatement cst  = con.prepareCall(addressQuery);
		cst.setString(1,genGuiaNumber);
		cst.setString(2,global.clientId);
		cst.setString(3,generalForm.getOrgionaddresscode());
		cst.setString(4,generalForm.getDestinationaddresscode());
		cst.setString(5,generalForm.getFiscaladdresscode());
		cst.executeQuery();
		if(cst!=null)
			cst.close();
	}

	public void insertBookGuiaServiceRequirement(Connection con,
												 HttpSession session,
												 String genGuiaNumber,
												 String aditionalService)throws Exception {
		Global global =(Global)session.getAttribute("sGlobal");
		CallableStatement cst =  con.prepareCall("{call pack_web.pro_insrt_web_bok_item_reqm(?,?,?)}");//Checked
		cst.setString(1,global.groupClientId);
		cst.setString(2,aditionalService);
		cst.setString(3,genGuiaNumber);
		cst.executeQuery();
		if(cst!=null)
			cst.close();
	}

	public void insertBookGuiaServiceItem(Connection con,
										  HttpSession session,
										  String genGuiaNumber,
										  java.sql.Timestamp sysDate)throws Exception {


		ArrayList servicesDetailArray=null;
           
        Global global =(Global)session.getAttribute("sGlobal");
		String defaultScreen = (String)session.getAttribute("defaultservicescreen");
		boolean isDefault = false;
		if(defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes")){
			isDefault = true;
			servicesDetailArray = (ArrayList)session.getAttribute("servicesDetailDefault");
		}else{
			servicesDetailArray = (ArrayList)session.getAttribute("servicesDetail");
		}
		String insertQuery=		"INSERT INTO BOK_GUIA_SRVC_ITEM("+
								"GL_GUIA_NO,"+
								"GL_SRVC_ID,"+
								"GL_REFR_SRVC_ID,"+
								"GL_DESC,"+
								"GL_CONT,"+
								"GL_QUNT,"+
								"GL_SRVC_CHRG,"+
								"GL_STUS_FLAG,"+
								"CRTD_ON,"+
								"CRTD_BY,"+
								"MDFD_ON,"+
								"MDFD_BY,"+
								"GL_SLAB_NO,"+
								"GL_VLUE_1,"+
								"GL_VLUE_2,"+
								"GL_DOCU_TYPE,"+
								"GL_GUIA_TYPE )"+
								"VALUES(?,?,?,?,?,?,?,?,?,?,"+
								"?,?,?,?,?,?,?)";

		//java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		for(int i=0;i<servicesDetailArray.size();i++){
			
			ShipmentServiceDetail ssd = (ShipmentServiceDetail)servicesDetailArray.get(i);
			//AAP//AccessLog.Log(global.clientId+"ssd"+ssd.contenido);
			//AAP//AccessLog.Log(global.clientId+"ssd"+ssd.refServiceId);
			//AAP//AccessLog.Log(global.clientId+"ssd"+ssd.descripcionCode);
			PreparedStatement pst = con.prepareStatement(insertQuery);
			pst.setString(1,genGuiaNumber);
			pst.setString(2,ssd.refServiceId);
			pst.setString(3,ssd.serviceId);
			pst.setString(4,ssd.descripcionCode);
			pst.setString(5,ssd.contenido);
			pst.setInt(6,Integer.parseInt(ssd.cantidad));
			pst.setDouble(7,Double.parseDouble(ssd.importe));
			pst.setString(8,"Y");
			//pst.setDate(9,sysDate);--commented and below line coded by B.Emerson on 19/07/2003
			pst.setTimestamp(9,sysDate);
			pst.setString(10,global.clientId);
			//pst.setDate(11,sysDate);--commented and below line coded by B.Emerson on 19/07/2003
			pst.setTimestamp(11,sysDate);
			pst.setString(12,global.clientId);
			if(isDefault)
				pst.setString(13,global.tarifType);
			else
				pst.setString(13,ssd.tarifa);
			pst.setDouble(14,Double.parseDouble(ssd.peso));
			pst.setDouble(15,Double.parseDouble(ssd.volumen));
			pst.setString(16,"Q");
			pst.setString(17,"H");
			int insertCount = pst.executeUpdate();

			/*//AAPif(insertCount>0)
				AccessLog.Log(""+global.clientId+"BOK GUIA HEAD INSERT SCUCESS");
			else
				AccessLog.Log(""+global.clientId+"BOK GUIA HEAD INSERT FALILURE");
			*/
			if(pst!=null)
				pst.close();
		}
   }
	public void insertBookGuiaServiceItem(Connection con,
										  HttpSession session,
										  String genGuiaNumber,
										  java.sql.Timestamp sysDate,
										  Services services,
										  String serviceId,
										  String refServiceId,
										  JavWebBookingServicesForm serForm,String insView)throws Exception {

		Global global = (Global)session.getAttribute("sGlobal");
		ArrayList servicesDetailArray = null;
		double totalImporte=0.0;
		//java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		String defaultScreen = (String)session.getAttribute("defaultservicescreen");
		if(defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes"))
			servicesDetailArray= (ArrayList)session.getAttribute("servicesDetailDefault");
		else
			servicesDetailArray= (ArrayList)session.getAttribute("servicesDetail");

		for(int i=0;i<servicesDetailArray.size();i++){
			ShipmentServiceDetail ssd = (ShipmentServiceDetail)servicesDetailArray.get(i);
			totalImporte = totalImporte+Double.parseDouble(ssd.importe);
		}
		//String formatTotalImporte=df.format(totalImporte);
		//AAP//AccessLog.Log(global.clientId+"insertBookGuiaServiceItem"+serviceId+"refer"+refServiceId);
		ResultSet rs = null;
		String descQuery =	"SELECT SM_SRVC_NAME FROM SYS_SRVC_MSTR "+
							"WHERE SM_SRVC_ID = ? AND SM_REFR_SRVC_ID = ?";//Checked
		PreparedStatement pst =con.prepareStatement(descQuery);
		String serItemQuery="";
		
		if(insView.equalsIgnoreCase("Y"))
				{
			serItemQuery ="{call pack_web.PRO_INSRT_WEB_BOK_SRVC_ITEM_NE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
				}
		else
		{
		serItemQuery= "{call pack_web.PRO_INSRT_WEB_BOK_SRVC_ITEM(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//Checked
		}
		CallableStatement cst = con.prepareCall(serItemQuery);

		pst.setString(1,serviceId);
		pst.setString(2,refServiceId);
		rs = pst.executeQuery();
		String description = null;
		if(rs.next()){
			description = rs.getString(1);
			
		}
		//AAP//AccessLog.Log("description"+description);
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();

		cst.setString(1,genGuiaNumber);
		cst.setString(2,serviceId);
        cst.setString(3,refServiceId);
		cst.setString(4,(description== null)?"":description);
        cst.setString(5,"");
		cst.setString(6,"");
		cst.setDouble(7,Double.parseDouble(services.GS_SUB_TOTL));
		cst.setString(8,"Y");
		cst.setTimestamp(9,sysDate);
		cst.setString(10,global.clientId);
		cst.setTimestamp(11,sysDate);
		cst.setString(12,global.clientId);
		cst.setString(13,serviceId);


		if(refServiceId.equalsIgnoreCase("EAD") || refServiceId.equalsIgnoreCase("RAD"))
			cst.setDouble(14,totalImporte);
		else if(refServiceId.equalsIgnoreCase("INV")){
			if(serForm.getValordeclarado().trim().length()>0)
				cst.setDouble(14,Double.parseDouble(serForm.getValordeclarado().trim()));
			else
				//cst.setDouble(14,0.0d);//Empty
				cst.setNull(14,Types.NUMERIC);
		}
		else if(refServiceId.equalsIgnoreCase("ACK"))
			//cst.setDouble(14,0.0d);//Empty
			cst.setNull(14,Types.NUMERIC);
		else if(refServiceId.equalsIgnoreCase("COD"))
			//cst.setDouble(14,0.0d);//Empty
			cst.setNull(14,Types.NUMERIC);
		else if(refServiceId.equalsIgnoreCase("CODR")){
			if(serForm.getValorcod().trim().length()>0)
				cst.setDouble(14,Double.parseDouble(serForm.getValorcod().trim()));
			else
				cst.setDouble(14,0.0d);
		}
		//code  added by palanivel
		else if(totalImporte==0)
		{
			
			cst.setNull(14,Types.NUMERIC);
		}
		else
			cst.setDouble(14,totalImporte);


		cst.setInt(15,0);
		cst.setString(16,"Q");
		cst.setString(17,"H");
		cst.executeQuery();

		if(cst!=null)
			cst.close();
	}

	public void insertBookGuiaServices(Connection con,
									   HttpSession session,
									   String genGuiaNumber,
									   java.sql.Timestamp sysDate,
									   JavWebBookingServicesForm serForm,HttpServletRequest req)throws Exception {

		Global global = (Global)session.getAttribute("sGlobal");
		ArrayList servicesList = (ArrayList)session.getAttribute("calculatedservicelist");
		/*Available Services Object..
		1.shipE	2.shipG	3.rad	4.ead	5.ack	6.cod	7.codr	8.inv 9.Add1
		*/
		String insuranceView="";
		for(int i=0;i<servicesList.size();i++){
			Hashtable servicesTable = (Hashtable)servicesList.get(i);
			if(servicesTable.get("newservice")!=null){
				ArrayList listOfservices=new ArrayList();
				listOfservices=(ArrayList)servicesTable.get("newservice");
				for(int index=0;index<listOfservices.size();index++)
				{
					Hashtable serv_new=new Hashtable();
					//AAP//AccessLog.Log(""+global.clientId+"INSIDE RAD");
					serv_new=(Hashtable)listOfservices.get(index);
				    Set set_new=serv_new.keySet();
				    Iterator new_iterator=set_new.iterator();
				    for(;new_iterator.hasNext();)
				    {
				    String key=(String)new_iterator.next();
				    Services servicesTemp=(Services)serv_new.get(key);
				    insertBookGuiaServiceItem(con,session,genGuiaNumber,sysDate,servicesTemp,key,servicesTemp.GS_SRVC_ID,serForm,"N");
					insertBookGuiaServiceRequirement(con,session,genGuiaNumber,key);
					insertServicesRecord(con,session,sysDate,servicesTemp,genGuiaNumber,"N");
				    }
				}
			}
			else if((Services)servicesTable.get("shipE")!=null){
				//AAP//AccessLog.Log(""+global.clientId+"INSIDE SHIPE");
				Services servicesTemp = (Services)servicesTable.get("shipE");
				insertServicesRecord(con,session,sysDate,servicesTemp,genGuiaNumber,"N");
			}else if((Services)servicesTable.get("shipG")!=null){
				//AAP//AccessLog.Log(""+global.clientId+"INSIDE SHIPG");
				Services servicesTemp = (Services)servicesTable.get("shipG");
				insertServicesRecord(con,session,sysDate,servicesTemp,genGuiaNumber,"N");
			}else if((Services)servicesTable.get("rad")!=null){
				//AAP//AccessLog.Log(""+global.clientId+"INSIDE RAD");
				Services servicesTemp = (Services)servicesTable.get("rad");
				if(Double.parseDouble(servicesTemp.GS_SUB_TOTL)>0) // modified on 07/07/2009
				{
				insertBookGuiaServiceItem(con,session,genGuiaNumber,sysDate,servicesTemp,"RAD-1","RAD",serForm,"N");
				insertBookGuiaServiceRequirement(con,session,genGuiaNumber,"RAD-1");
				insertServicesRecord(con,session,sysDate,servicesTemp,genGuiaNumber,"N");
				}
			}else if((Services)servicesTable.get("ead")!=null){
				String referenciaItem = "";//AAP01
				String idItem = "";//AAP01
				if (serForm.getZonaExtendida().substring(0, 1).equals("Y")){//AAP01				
					referenciaItem = "EXT";//AAP01
					//AAP//AccessLog.Log("#$#$#$#$$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$");
					//AAP//AccessLog.Log("operador logistico serForm.getOperadorLogistico() "+serForm.getOperadorLogistico());
					//AAP//AccessLog.Log("#$#$#$#$$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$");
					idItem = "EXT-"+serForm.getOperadorLogistico();//AAP01
				}else{//AAP01
					referenciaItem="EAD";
					idItem = "EAD-1";				
				}//AAP01
				
				//AAP//AccessLog.Log(""+global.clientId+"INSIDE EAD");
				Services servicesTemp = (Services)servicesTable.get("ead");
				insertBookGuiaServiceRequirement(con,session,genGuiaNumber,idItem);//AAP01
				insertBookGuiaServiceItem(con,session,genGuiaNumber,sysDate,servicesTemp,idItem,referenciaItem,serForm,"N");//AAP01
				insertServicesRecord(con,session,sysDate,servicesTemp,genGuiaNumber,"N");
			}else if((Services)servicesTable.get("ack")!=null){
				//AAP//AccessLog.Log(""+global.clientId+"INSIDE ACK");
				Services servicesTemp = (Services)servicesTable.get("ack");
				insertBookGuiaServiceRequirement(con,session,genGuiaNumber,serForm.getAcusederecibo());
				insertBookGuiaServiceItem(con,session,genGuiaNumber,sysDate,servicesTemp,serForm.getAcusederecibo(),"ACK",serForm,"N");
				insertServicesRecord(con,session,sysDate,servicesTemp,genGuiaNumber,"N");
			}else if((Services)servicesTable.get("cod")!=null){
				//AAP//AccessLog.Log(""+global.clientId+"INSIDE COD");
				Services servicesTemp = (Services)servicesTable.get("cod");
				insertBookGuiaServiceRequirement(con,session,genGuiaNumber,"COD-1");
				insertBookGuiaServiceItem(con,session,genGuiaNumber,sysDate,servicesTemp,"COD-1","COD",serForm,"N");
				insertServicesRecord(con,session,sysDate,servicesTemp,genGuiaNumber,"N");
			}else if((Services)servicesTable.get("codr")!=null){
				//AAP//AccessLog.Log(""+global.clientId+"INSIDE CODR");
				Services servicesTemp = (Services)servicesTable.get("codr");
				insertBookGuiaServiceRequirement(con,session,genGuiaNumber,"CODR-1");
				insertBookGuiaServiceItem(con,session,genGuiaNumber,sysDate,servicesTemp,"CODR-1","CODR",serForm,"N");
				insertServicesRecord(con,session,sysDate,servicesTemp,genGuiaNumber,"N");
			}else if((Services)servicesTable.get("inv")!=null){
				//AAP//AccessLog.Log(""+global.clientId+"INSIDE INV");
				Services servicesTemp = (Services)servicesTable.get("inv");
				
				insuranceView=getInsuranceViewFlag(con,session,req);
				insertBookGuiaServiceRequirement(con,session,genGuiaNumber,serForm.getCobertura());
				insertBookGuiaServiceItem(con,session,genGuiaNumber,sysDate,servicesTemp,serForm.getCobertura(),"INV",serForm,insuranceView);
				insertServicesRecord(con,session,sysDate,servicesTemp,genGuiaNumber,insuranceView);
				insuranceView="";
				
			}
			
           /*
		    * code added by palanivel to insert Additional Service Related Information
		    */
		  
		   if (global.tarifType.equalsIgnoreCase("C") && session.getAttribute("aditionalServicesDetail") != null)
           {
                ArrayList additionalServicesArray = null ;
				additionalServicesArray =  (ArrayList) session.getAttribute("aditionalServicesDetail");
				int arraySize =  additionalServicesArray. size();
				
				for(int j=0;j<arraySize;j++)
				{
			       AdditionalService serviceRecordsRecs1 = (AdditionalService)additionalServicesArray.get(j);
			       
                   if((Services)servicesTable.get(serviceRecordsRecs1.service)!=null){
                        //AAP//AccessLog.Log(""+global.clientId+"INSIDE INV");
                        Services servicesTemp = (Services)servicesTable.get(serviceRecordsRecs1.service);
                        insertBookGuiaServiceRequirement(con,session,genGuiaNumber,serForm.getCobertura());
                        
                        insertBookGuiaServiceItem(con,session,genGuiaNumber,sysDate,servicesTemp,serviceRecordsRecs1.service,serviceRecordsRecs1.serviceId,serForm,"N");
                        
                        insertServicesRecord(con,session,sysDate,servicesTemp,genGuiaNumber,"N");
                        
                    }
				}
			}
			   
  		}
	}
	public void insertServicesRecord(Connection con,
									 HttpSession session,
									 java.sql.Timestamp sysDate,
									 Services services,
									 String genGuiaNumber,String insView){

		Global global = (Global)session.getAttribute("sGlobal");
		String query="";
		if(insView.equalsIgnoreCase("Y"))
		{
			query="{ call pack_web.PRO_INSRT_WEB_BOK_SRVC_NEW(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//Checked
		}
		else
		{
			query= "{ call pack_web.PRO_INSRT_WEB_BOK_SRVC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";//Checked
		}
		try{
		 
		CallableStatement cst = con.prepareCall(query);
		//AAP//AccessLog.Log(query);
		cst.setString(1,genGuiaNumber);
		//AAP//AccessLog.Log(genGuiaNumber);
		cst.setString(2,services.GS_SRVC_ID);
		//AAP//AccessLog.Log(services.GS_SRVC_ID);
        cst.setDouble(3,Double.parseDouble(services.GS_DISC));
        //AAP//AccessLog.Log(services.GS_DISC);
		cst.setDouble(4,Double.parseDouble(services.GS_TAX));
		//AAP//AccessLog.Log(services.GS_TAX);
		
		//services.GS_SUB_TOTL=services.GS_SUB_TOTL+services.GS_TAX_RET;
		
		cst.setDouble(5,Double.parseDouble(services.GS_TAX_RET));
		//cst.setDouble(5,Double.parseDouble(services.GS_TAX_RET));
		
		//AAP//AccessLog.Log(services.GS_TAX_RET);
		//AAP//AccessLog.Log("before insertion the amount is"+services.GS_SUB_TOTL);
        cst.setDouble(6,Double.parseDouble(services.GS_SUB_TOTL));
        cst.setString(7,services.GS_ADD_PYMT_FLAG);
        //AAP//AccessLog.Log(services.GS_ADD_PYMT_FLAG);
        cst.setString(8,services.GS_SRVC_TYPE);
        //AAP//AccessLog.Log(services.GS_SRVC_TYPE);
        cst.setString(9,services.GS_DOCU_TYPE);
        //AAP//AccessLog.Log(services.GS_DOCU_TYPE);
        cst.setString(10,services.GS_GUIA_TYPE);
        //AAP//AccessLog.Log(services.GS_GUIA_TYPE);
        //cst.setDate(11,sysDate);--commented and below line coded by B.Emerson on 19/07/2003
		cst.setTimestamp(11,sysDate);
		//AAP//AccessLog.Log(sysDate+"");
        cst.setString(12,global.clientId);
        //AAP//AccessLog.Log(global.clientId);
        //cst.setDate(13,sysDate);--commented and below line coded by B.Emerson on 19/07/2003
		cst.setTimestamp(13,sysDate);
		//AAP//AccessLog.Log(sysDate+"");
		cst.setString(14,global.clientId);
		//AAP//AccessLog.Log(global.clientId);
        cst.setString(15,services.GS_STUS_FLAG);
        //AAP//AccessLog.Log(services.GS_STUS_FLAG);
        cst.setString(16,services.GS_DISC_SLAB_NO);
        //AAP//AccessLog.Log(services.GS_DISC_SLAB_NO);
        
        cst.executeQuery();
        //AAP//AccessLog.Log("before insertion the amount is"+services.GS_SUB_TOTL);
		} catch(Exception e) {
		    AccessLog.Log(""+global.clientId+" BEFORE SERVICE SPECIAL"+e.getStackTrace());
		}
        //AAP//AccessLog.Log(""+global.clientId+"****************************** BOOK_GUIA_SERVICES INSERTION SUCCESS *************************");
	}

	public void calculateServices(Connection con,
								  JavWebBookingServicesForm aform,
								  HttpServletRequest req)throws Exception {

		ArrayList calServicesList = new ArrayList();
		HttpSession session = req.getSession(false);
		String defaultScreen = (String)session.getAttribute("defaultservicescreen");
		Global global = (Global)session.getAttribute("sGlobal");
		ServicesGlobal servicesGlobal = (ServicesGlobal)session.getAttribute("servicesGlobal");
		String groupClientId = global.groupClientId;
		ArrayList servicesDetailArray=null;
		//AAP//AccessLog.Log("defaultScreen ==>"+defaultScreen);
		ServicesTotal servicesTotal = new ServicesTotal();//For display

		if(defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes"))			
			servicesDetailArray= (ArrayList)session.getAttribute("servicesDetailDefault");
		else
			servicesDetailArray= (ArrayList)session.getAttribute("servicesDetail");

		double shipEimporte=0.0;
		double shipGimporte=0.0;
		boolean isShipEExists=false;
		boolean isShipGExists=false;
		int shipEQty=0;
		int shipGQty=0;
		double totalImporte=0.0;
		int totalQty=0;
		//int toincreaseE=0;
		//int toincreaseG=0;
		double totalWeight = 0.0;
		double totalVolume = 0.0;
		
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		for(int i=0;i<servicesDetailArray.size();i++)
		{
			ShipmentServiceDetail ssd = (ShipmentServiceDetail)servicesDetailArray.get(i);
			
				
			if(ssd.serviceId.equalsIgnoreCase("SHP-E")){
				shipEimporte= shipEimporte+Double.parseDouble(ssd.importe);
				
				/*  removed for 1 $ minus
				 * 	if((i==0)&& (shipEimporte>0))
				{
					shipEimporte=shipEimporte-1;
					toincreaseE=1;
				}
				 */
				isShipEExists=true;
				shipEQty = shipEQty+Integer.parseInt(ssd.cantidad);
			}else if(ssd.serviceId.equalsIgnoreCase("SHP-G")){
				shipGimporte= shipGimporte+Double.parseDouble(ssd.importe);
				/* removed for 1 $ minus 
				 * if((i==0)&& (shipGimporte>0))
				{
					shipGimporte=shipGimporte-1;
					toincreaseG=1;
				}*/
				isShipGExists=true;
				shipGQty = shipGQty+Integer.parseInt(ssd.cantidad);
			}
			totalImporte = totalImporte+Double.parseDouble(ssd.importe);
			totalQty=totalQty+Integer.parseInt(ssd.cantidad);
			servicesTotal.totalPack=String.valueOf(totalQty);

			totalWeight = totalWeight+(Integer.parseInt(ssd.cantidad)*Double.parseDouble(ssd.peso));
			totalVolume = totalVolume+(Integer.parseInt(ssd.cantidad)*Double.parseDouble(ssd.volumen));

			servicesTotal.totalWeight=df.format(totalWeight);
			servicesTotal.totalVolume=df.format(totalVolume);

		}
		servicesTotal.flete=df.format(totalImporte);
		double descuento=0.00;
		double doubleIva = 0.00;
		double doubleIvaRet = 0.00;

		//ShipE Service
		//0---slab 1---- discountAmount
		String discountShipE[] = new String[2];
		String discountShipG[] = new String[2];
		if(isShipEExists){
			//AAP//AccessLog.Log(""+global.clientId+"SHIPE EXISTS");
			double minamount =0.0;
			if(defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes")){
				discountShipE[0] = null;
				discountShipE[1] = "0";
			}else{
				//AAP//AccessLog.Log(""+global.clientId+"BEFORE CALLING CALCULATEQUANTITY OF SHIPE");
				discountShipE= calculateQuantity(con,groupClientId,"ENVELOPES",df.format(shipEQty),df.format(shipEimporte),df.format(minamount));
				//AAP//AccessLog.Log(""+global.clientId+"AFTER CALLING CALCULATEQUANTITY OF SHIPE");
			}
			descuento = descuento+Double.parseDouble(discountShipE[1]);
			//AAP//AccessLog.Log(""+global.clientId+"------- CALLING GET TAX IN SHIPE ");
			String tax[] = getTax(con,session,"ENVELOPES",df.format(shipEimporte-Double.parseDouble(discountShipE[1])));

			//AAP//AccessLog.Log(""+global.clientId+"------- AFTER CALLING GET TAX IN SHIPE ");

			doubleIva = doubleIva + Double.parseDouble(tax[0]);
			//doubleIvaRet = doubleIvaRet + Double.parseDouble(tax[1]);//AAP05

			Services shipE= new Services();
			String descriptionShipE = getServicesAditionalDescription(con,"SHP-E");
			shipE.descriptionAditional=descriptionShipE;
			shipE.GS_SRVC_ID="SHP-E";
			shipE.GS_DISC=discountShipE[1];
			shipE.GS_TAX=tax[0];
			//shipE.GS_TAX_RET=tax[1];//AAP05
			shipE.GS_TAX_RET="0";//AAP05
			if (isShipGExists) {//AAP05
				doubleIvaRet = doubleIvaRet + Double.parseDouble(tax[1]);//AAP05
				shipE.GS_TAX_RET=tax[1];//AAP05
			}
			/*removed for 1$ minus
			 * 			 * if((shipEimporte>0)&&(toincreaseE==1))
			shipE.GS_SUB_TOTL=df.format(shipEimporte+1);
			else*/
			//shipE.GS_SUB_TOTL=df.format(shipEimporte);  //26/07
			shipE.GS_SUB_TOTL=df.format(shipEimporte);	
			shipE.GS_ADD_PYMT_FLAG="N";
			shipE.GS_SRVC_TYPE="S";
			shipE.GS_DOCU_TYPE="Q";
			shipE.GS_GUIA_TYPE="H";
			shipE.GS_STUS_FLAG="A";
			shipE.GS_DISC_SLAB_NO=discountShipE[0];
			/*
			 * removed for 1$ minus
			 * if((shipEimporte>0)&&(toincreaseE==1))
			shipE.TOTAL=df.String.valueOf((shipEimporte+1)-Double.parseDouble(discountShipE[1]));
			else*/
				shipE.TOTAL=df.format((shipEimporte)-Double.parseDouble(discountShipE[1]));
			Hashtable calServicestable= new Hashtable();
			calServicestable.put("shipE",shipE);
			calServicesList.add(calServicestable);

		}
		//End ShipE Service

		//ShipG services
		if(isShipGExists) {
			//AAP//AccessLog.Log(""+global.clientId+"SHIPG EXISTS");
			double minamount =0.0;
			if(defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes")){
				discountShipG[0] = null;
				discountShipG[1] = "0";
			}else{
				//AAP//AccessLog.Log(""+global.clientId+"BEFORE CALLING CALCULATEQUANTITY OF SHIPG");
				discountShipG= calculateQuantity(con,groupClientId,"PACKETS",String.valueOf(shipGQty),df.format(shipGimporte),df.format(minamount));
				//AAP//AccessLog.Log(""+global.clientId+"AFTER CALLING CALCULATEQUANTITY OF SHIPG");
			}

			descuento =descuento+Double.parseDouble(discountShipG[1]);
			//AAP//AccessLog.Log(""+global.clientId+"------- CALLING GET TAX IN SHIPG ");
			String tax[] = getTax(con,session,"PACKETS",df.format(shipGimporte-Double.parseDouble(discountShipG[1])));
			//AAP//AccessLog.Log(""+global.clientId+"------- AFTER CALLING GET TAX IN SHIPG ");
			//AccessLog.Log("PEPEPEPE -----------------    FOR PACKETS AFTER CALLING GETTAX");
			doubleIva = doubleIva + Double.parseDouble(tax[0]);
			doubleIvaRet = doubleIvaRet + Double.parseDouble(tax[1]);

			Services shipG= new Services();
			String descriptionShipG = getServicesAditionalDescription(con,"SHP-G");
			shipG.descriptionAditional=descriptionShipG;
			shipG.GS_SRVC_ID="SHP-G";
			shipG.GS_DISC=discountShipG[1];
			shipG.GS_TAX=tax[0];
			shipG.GS_TAX_RET=tax[1];
			/*removed for 1$ minus
			 * 
			 if((shipGimporte>0)&&(toincreaseG==1))
				shipG.GS_SUB_TOTL=df.format(shipGimporte+1);
			else*/
				shipG.GS_SUB_TOTL=df.format(shipGimporte);
			shipG.GS_ADD_PYMT_FLAG="N";
			shipG.GS_SRVC_TYPE="S";
			shipG.GS_DOCU_TYPE="Q";
			shipG.GS_GUIA_TYPE="H";
			shipG.GS_STUS_FLAG="A";
			shipG.GS_DISC_SLAB_NO=discountShipG[0];
			/* removed for 1$ minus
			 * if((shipGimporte>0)&&(toincreaseG==1))
			shipG.TOTAL=df.format((shipGimporte+1)-Double.parseDouble(discountShipG[1]));
			else
			*/
				shipG.TOTAL=df.format((shipGimporte)-Double.parseDouble(discountShipG[1]));
			Hashtable calServicestable= new Hashtable();
			calServicestable.put("shipG",shipG);
			calServicesList.add(calServicestable);

		}
		//End ShipG Service

		/*	Servicios = allSubtotal-all Discount(Except shipe and shipg)
		SubTotal = (Flete + Servicios)-discount
		IVA = sum of all tax
		IVARet = sum of all taxRet
		Total = (SubTotal+IVA)-IVARet
		*/

		servicesTotal.descuento=df.format(descuento);
		double doubleServicios=0.0;//Sum of ALL SubTotal

		//RAD Service
		Services rad= new Services();
		//AAP//AccessLog.Log(""+global.clientId+"RAD EXISTS");
		String descriptionRad = getServicesAditionalDescription(con,"RAD-1");
		rad.descriptionAditional=descriptionRad;
		rad.GS_SRVC_ID="RAD";
		String lcZoneRad = getRadZone(con,session);
		String subTotalRad = null;

		String[] amount_totalRad=null;//0--- subTotal, 1---- Minamount
		String discountRad[] = new String[2];
		double taxableAmountRad=0.0;
		if(defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes")){
			subTotalRad= getAditionalServicesTotal(con,session,"RAD-1","RAD");
			discountRad[0] = null;
			discountRad[1] = "0";
			rad.GS_SUB_TOTL=df.format(Double.parseDouble(subTotalRad)*totalQty);
			doubleServicios=doubleServicios+(Double.parseDouble(subTotalRad)*totalQty-0);
			taxableAmountRad= Double.parseDouble(rad.GS_SUB_TOTL);
			//AAP//AccessLog.Log("****Taxable rad amount"+taxableAmountRad);
		}else{
			amount_totalRad= getAditionalServicesTotal(con,session,lcZoneRad,df.format(totalImporte),"RAD-1","RAD"); 
			rad.GS_SUB_TOTL=amount_totalRad[0];
			//AAP//AccessLog.Log(""+global.clientId+"Thank Lord - RAD amount is "+rad.GS_SUB_TOTL);
			//AAP//AccessLog.Log(""+global.clientId+"BEFORE CALLING CALCULATEQUANTITY OF RAD");
			if(Double.parseDouble(rad.GS_SUB_TOTL)>0)
			discountRad = calculateQuantity(con,groupClientId,"RAD",df.format(totalImporte),amount_totalRad[0],"0");
			else
			{
				discountRad[0] = null;
				discountRad[1] = "0.00";	
			}
			//discountRad = calculateQuantity(con,groupClientId,"RAD-1",amount_totalRad[0],df.format(totalImporte),"0"); commented and added prev by amal
			//AAP//AccessLog.Log(""+global.clientId+"AFTER CALLING CALCULATEQUANTITY OF RAD");
			doubleServicios=doubleServicios+(Double.parseDouble(amount_totalRad[0])-Double.parseDouble(discountRad[1]));
			taxableAmountRad= Double.parseDouble(rad.GS_SUB_TOTL)-Double.parseDouble(discountRad[1]);
			//AAP//AccessLog.Log("****Taxable rad amount"+taxableAmountRad);
		}
		//AAP//AccessLog.Log(""+global.clientId+"------- CALLING GET TAX IN RAD ");
		String taxForRad[]=getTax(con,session,"RAD",df.format(taxableAmountRad));
		//AAP//AccessLog.Log(""+global.clientId+"------- AFTER CALLING GET TAX IN RAD ");
		//AccessLog.Log("PEPEPEPE -----------------    FOR RAD AFTER CALLING GETTAX");
		doubleIva = doubleIva + Double.parseDouble(taxForRad[0]);
		//AAP//AccessLog.Log("****Tax for rad "+doubleIva);
		//doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForRad[1]); //commented on 29 -0ct-2008
		//AAP//AccessLog.Log("****Tax Return for rad "+doubleIvaRet);
		rad.GS_DISC=discountRad[1];
		rad.GS_TAX=taxForRad[0];
		rad.GS_TAX_RET="0";
		rad.GS_ADD_PYMT_FLAG="N";
		rad.GS_SRVC_TYPE="N";
		rad.GS_DOCU_TYPE="Q";
		rad.GS_GUIA_TYPE="H";
		rad.GS_STUS_FLAG="A";
		rad.GS_DISC_SLAB_NO=null;
		rad.TOTAL=df.format(Double.parseDouble(rad.GS_SUB_TOTL)-Double.parseDouble(rad.GS_DISC));
		Hashtable calServicestableRad= new Hashtable();
		calServicestableRad.put("rad",rad);
		calServicesList.add(calServicestableRad);
		//End Rad Service
		
		//new service
		//AAP//AccessLog.Log("****defaultScreen ==> "+defaultScreen);
		if(defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes")){
			ArrayList newListService=new ArrayList();
			
			String Query="SELECT WT_SRVC_ID SERVICE_ID,PACK_WEB.FUN_SRVC_NAME(WT_SRVC_ID) SERVICE_NAME ,WT_TRIF_AMNT AMOUNT"+
			",WT_REFR_SRVC_ID REF_SERVICE_ID FROM WEB_CLNT_SRVC_TRIF WHERE WT_ORGN_CLNT_ID= ? AND substr(WT_ORGN_BRNC_ID,1,3)=substr(?,1,3)"+
			 " AND	substr(WT_DEST_BRNC_ID,1,3)=substr(?,1,3) AND WT_TRIF_SLAB   = ?  AND "+
			 " WT_REFR_SRVC_ID NOT IN('EAD','RAD','ACK','INV','ENVELOPES','PACKETS','COD','EXT') AND WT_TRIF_AMNT <> 0 AND wt_srvc_type IS NULL";
			//AAP//AccessLog.Log("****Query ==> "+Query);
			
			String newservice_desc="";
			String newservice_id="";
			String newservice_ref_id="";
			String   subTotalnewService = null;
			String[] amount_totalservice=null;
			String   discountservice[] = new String[2];
			double   taxableAmountservice=0.0;
			
			PreparedStatement pst = con.prepareStatement(Query);
			pst.setString(1,global.clientId);
			pst.setString(2,global.assignedBranch);
			pst.setString(3,global.destinationBranchId);
			pst.setString(4,global.tarifType);
			//AAP//AccessLog.Log("****global.clientId ==> "+global.clientId);
			//AAP//AccessLog.Log("****global.assignedBranch ==> "+global.assignedBranch);
			//AAP//AccessLog.Log("****global.destinationBranchId ==> "+global.destinationBranchId);
			//AAP//AccessLog.Log("****global.tarifType ==> "+global.tarifType);
			ResultSet rs = pst.executeQuery();
		       while(rs.next()){
		    	   //AAP//AccessLog.Log("****OBTUVO DATOS ");
			newservice_desc="";
			newservice_id="";
			newservice_ref_id="";
			Services  newservice=new Services();
			newservice_id=rs.getString("SERVICE_ID");
			newservice_ref_id=rs.getString("REF_SERVICE_ID");
			newservice_desc=rs.getString("SERVICE_NAME");
			newservice.descriptionAditional=newservice_desc;
			newservice.GS_SRVC_ID=newservice_ref_id;
			//AAP//AccessLog.Log(""+global.clientId+newservice_ref_id+" EXISTS");
			subTotalnewService = null;
			amount_totalservice=null;
			taxableAmountservice=0.0;
				subTotalnewService= rs.getString("AMOUNT");
				discountservice[0] = null;
				discountservice[1] = "0";
				newservice.GS_SUB_TOTL=df.format(Double.parseDouble(subTotalnewService)*totalQty);
				doubleServicios=doubleServicios+(Double.parseDouble(subTotalnewService)*totalQty-0);
				taxableAmountservice= Double.parseDouble(newservice.GS_SUB_TOTL);
				//AAP//AccessLog.Log("****Taxable "+newservice_ref_id+" amount"+taxableAmountRad);
			
			//AAP//AccessLog.Log(""+global.clientId+"------- CALLING GET TAX IN  "+newservice_ref_id);
			String taxForservice[]=getTax(con,session,newservice_ref_id,df.format(taxableAmountservice));
			//AAP//AccessLog.Log(""+global.clientId+"------- AFTER CALLING GET TAX IN  "+newservice_ref_id);
			
			doubleIva = doubleIva + Double.parseDouble(taxForservice[0]);
			//AAP//AccessLog.Log("****Tax for taxForservice "+newservice_ref_id+doubleIva);
			//doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForservice[1]); 
			//AAP//AccessLog.Log("****Tax Return for taxForservice "+newservice_ref_id+doubleIvaRet);
			newservice.GS_DISC=discountservice[1];
			newservice.GS_TAX=taxForservice[0];
			newservice.GS_TAX_RET="0";
			newservice.GS_ADD_PYMT_FLAG="N";
			newservice.GS_SRVC_TYPE="N";
			newservice.GS_DOCU_TYPE="Q";
			newservice.GS_GUIA_TYPE="H";
			newservice.GS_STUS_FLAG="A";
			newservice.GS_DISC_SLAB_NO=null;
			newservice.TOTAL=df.format(Double.parseDouble(newservice.GS_SUB_TOTL)-Double.parseDouble(newservice.GS_DISC));
			Hashtable calServicestableService= new Hashtable();
			calServicestableService.put(newservice_id,newservice);
			newListService.add(calServicestableService);
			}
			if(newListService.size()>0)
			{
			Hashtable calServicestableService= new Hashtable();
			calServicestableService.put("newservice",newListService);
			calServicesList.add(calServicestableService);
			}
			}
		
		//end of new service
		//EAD Service
		//AAP//AccessLog.Log("****aform.getEntrega() ==> "+aform.getEntrega());
		if(aform.getEntrega().equalsIgnoreCase("2")) {
			
			String referenciaItem = "";//AAP01
			String idItem = "";//AAP01
			ArrayList AlcZoneEad = getLcZone(con,session);//AAP01
			String lcZoneEad = AlcZoneEad.get(0).toString();//zona//AAP01
			String lcOpeLogEad = AlcZoneEad.get(1).toString();//operador logistico//AAP01
			aform.setOperadorLogistico(lcOpeLogEad);//asigna operador logistico a bean form de servicios
			if (aform.getZonaExtendida().substring(0, 1).equals("Y")){//AAP01		
				referenciaItem = "EXT";
				idItem = "EXT-"+lcOpeLogEad;
			}else{//AAP01
				referenciaItem="EAD";
				idItem = "EAD-1";				
			}//AAP01
			
			Services ead= new Services();
			String descriptionEad = getServicesAditionalDescription(con,idItem);//AAP01
			ead.descriptionAditional=descriptionEad;
			ead.GS_SRVC_ID=referenciaItem;//AAP01
			
			String subTotalEad = null;
			String[] amount_totalEad=null;//0--- subTotal, 1---- Minamount
			String[] discountEad = new String[2];
			double taxableAmountEad = 0.0;
			//AAP//AccessLog.Log("descriptionEad ==>"+descriptionEad);
			//AAP//AccessLog.Log("lcZoneEad ==>"+lcZoneEad);
			
			if((defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes"))&& servicesGlobal.ead!=null && (aform.getZonaExtendida() !=null && !aform.getZonaExtendida().substring(0, 1).equals("Y"))){//AAP01
				//AAP//AccessLog.Log("INSIDE EAD IF");
				subTotalEad= getAditionalServicesTotal(con,session,"EAD-1","EAD");
				discountEad[0] = null;
				discountEad[1] = "0";
				ead.GS_SUB_TOTL=df.format(Double.parseDouble(subTotalEad)*totalQty);
				doubleServicios=doubleServicios+(Double.parseDouble(subTotalEad)*totalQty-0);
				taxableAmountEad = Double.parseDouble(ead.GS_SUB_TOTL);
				
			}else{
				//AAP//AccessLog.Log("INSIDE EAD ELSE");
				amount_totalEad= getAditionalServicesTotal(con,session,lcZoneEad,df.format(totalImporte),"EAD-1","EAD");
				//AAP//AccessLog.Log("amount_totalEad[0] EAD==>"+amount_totalEad[0]);
				//AAP//AccessLog.Log("amount_totalEad[1] EAD==>"+amount_totalEad[1]);
				//AAP//AccessLog.Log(global.clientId+"BEFORE CALLING CALCULATEQUANTITY OF EAD");

				/*si es zona extendida, se multiplica por el numero de paquetes a entregar
				 * No se aplican descuentos.*///AAP01
				if (aform.getZonaExtendida().substring(0, 1).equals("Y")){//AAP01
					//se asigna la tarifa extendida del elemento 1 al 0 (no usa la tarifa calculada sobre importe de flete, solo la tarifa pactada a zona extendida)//AAP04
					amount_totalEad[0] = amount_totalEad[1];//AAP04
					/*verifica si el cliente origen tiene tarifa especial para zona extendida.
					 * si no tiene tarifa especial, se realiza el cobro normal*/
					String tarifaEspecialExt = getTarifaEspecialExt(con, session, referenciaItem, idItem);//AAP03
					if (tarifaEspecialExt.length()>0 && !tarifaEspecialExt.equals("0")) {
						//AAP//AccessLog.Log("calculateServices()_"+global.clientId+"_ Asignó tarifa especial ==>"+tarifaEspecialExt);
						amount_totalEad[0] = tarifaEspecialExt;
						amount_totalEad[1] = tarifaEspecialExt;
					}
					//AAP//AccessLog.Log("amount_totalEad[0] EAD ZONA EXTENDIDA==>"+amount_totalEad[0]);
					//AAP//AccessLog.Log("amount_totalEad[1] EAD ZONA EXTENDIDA==>"+amount_totalEad[1]);
					ead.GS_SUB_TOTL=df.format(Double.parseDouble(amount_totalEad[0])*totalQty);//AAP01
					discountEad[0] = null;//AAP01
					discountEad[1] = "0.00";	//AAP01
					//ead.GS_SUB_TOTL=amount_totalEad[0];	
					//AAP//AccessLog.Log("df.format(Double.parseDouble(amount_totalEad[0])*totalQty) "+df.format(Double.parseDouble(amount_totalEad[0])*totalQty));
					doubleServicios=doubleServicios+(Double.parseDouble(amount_totalEad[0])*totalQty);//AAP01	
					taxableAmountEad = Double.parseDouble(amount_totalEad[0])*totalQty;
				}else{//AAP01
					ead.GS_SUB_TOTL=amount_totalEad[0];//AAP01	
					if(Double.parseDouble(ead.GS_SUB_TOTL)>0)//AAP01
						/*en monto minimo ahora se envia 0, la comparacion de monto minimo contra total de cobro se hace en java.*///AAP04
						discountEad = calculateQuantity(con, groupClientId, "EAD", df.format(totalImporte), ead.GS_SUB_TOTL, "0"/*amount_totalEad[1]*/);// AAP01
					else{//AAP01
						discountEad[0] = null;//AAP01
						discountEad[1] = "0.00";	//AAP01
					}//AAP01
					double totaldescuento = Double.parseDouble(amount_totalEad[0]) - Double.parseDouble(discountEad[1]);//AAP04

					/*Comparacion para verificar si el monto minimo es mayor que el descuento a aplicar*/
					if (Double.parseDouble(amount_totalEad[1])>totaldescuento) {//AAP04
						/*Si el cobro minimo es mayor que el monto con descuento, se deja el cobro minimo*/
						doubleServicios = doubleServicios + Double.parseDouble(amount_totalEad[1]);
						taxableAmountEad = Double.parseDouble(amount_totalEad[1]);
						discountEad[1] = "0.00";//AAP04 SE ASIGNA VALOR 0.00 PARA NO ASIGNAR DESCUENTO (LLEVA EL CALCULO DE MONTO MINIMO)
						ead.GS_SUB_TOTL = amount_totalEad[1];
					} else {//AAP04
						/*Si el cobro minimo es menor o igual que el monto con descuento, se deja el monto con descuento*/
						doubleServicios = doubleServicios + (Double.parseDouble(amount_totalEad[0]) - Double.parseDouble(discountEad[1]));// AAP01
						taxableAmountEad = Double.parseDouble(amount_totalEad[0]) - Double.parseDouble(discountEad[1]);	
					}
				}//AAP01
				
				
				//discountEad = calculateQuantity(con,groupClientId,"EAD-1",ead.GS_SUB_TOTL,df.format(totalImporte),amount_totalEad[1]); //commented and added prev line by amal
				//AAP//AccessLog.Log(""+global.clientId+"AFTER CALLING CALCULATEQUANTITY OF EAD");
				
				//AAP//AccessLog.Log("doubleServicios ==>"+doubleServicios);
				//AAP//AccessLog.Log("taxableAmountEad ==>"+taxableAmountEad);
				
				//AccessLog.Log("INSIDE EAD ELSE");
			}
			//AAP//AccessLog.Log("Taxable amount for ead"+taxableAmountEad);
			//AAP//AccessLog.Log(""+global.clientId+"------- CALLING GET TAX IN EAD IF ");
			String taxForEad[]=getTax(con,session,"EAD",df.format(taxableAmountEad));
			//AAP//AccessLog.Log(""+global.clientId+"------- AFTER CALLING GET TAX IN EAD IF ");
			//AccessLog.Log("PEPEPEPE -----------------    FOR EAD AFTER CALLING GETTAX");
			
			doubleIva = doubleIva + Double.parseDouble(taxForEad[0]);
			//AAP//AccessLog.Log("Tax amount for ead"+doubleIva);
			//doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForEad[1]);
			//AAP//AccessLog.Log("Tax amount for ead"+doubleIvaRet);
			//AAP//AccessLog.Log(""+global.clientId+"EAD EXISTS");
			ead.GS_DISC=discountEad[1];
			ead.GS_TAX=taxForEad[0];
			ead.GS_TAX_RET="0";
			ead.GS_ADD_PYMT_FLAG="N";
			ead.GS_SRVC_TYPE="N";
			ead.GS_DOCU_TYPE="Q";
			ead.GS_GUIA_TYPE="H";
			ead.GS_STUS_FLAG="A";
			ead.GS_DISC_SLAB_NO=null;
			ead.TOTAL=df.format(Double.parseDouble(ead.GS_SUB_TOTL)-Double.parseDouble(ead.GS_DISC));
			Hashtable calServicestableEad= new Hashtable();
			calServicestableEad.put("ead",ead);
			calServicesList.add(calServicestableEad);
		}/*else {

			//AccessLog.Log("INSIDE EAD else ........................");
			//AccessLog.Log("DEFAULT SCREEN "+defaultScreen);
			//AccessLog.Log("servicesGlobal.ead"+servicesGlobal.ead);

			if((defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes"))
			   && servicesGlobal.ead!=null){
				Services ead= new Services();
				String descriptionEad = getServicesAditionalDescription(con,"EAD-1");
				ead.descriptionAditional=descriptionEad;
				ead.GS_SRVC_ID="EAD";
				//String lcZoneEad = getLcZone(con,session);
				String subTotalEad = null;
				//String[] amount_totalEad=null;//0--- subTotal, 1---- Minamount
				String[] discountEad = new String[2];
				double taxableAmountEad = 0.0;
				subTotalEad= getAditionalServicesTotal(con,session,"EAD-1","EAD");
				discountEad[0] = null;
				discountEad[1] = "0";
				ead.GS_SUB_TOTL=df.format(Double.parseDouble(subTotalEad)*totalQty);
				doubleServicios=doubleServicios+(Double.parseDouble(subTotalEad)*totalQty-0);
				taxableAmountEad = Double.parseDouble(ead.GS_SUB_TOTL);
				AccessLog.Log(""+global.clientId+"------- CALLING GET TAX IN EAD ELSE ");
				String taxForEad[]=getTax(con,session,"EAD",df.format(taxableAmountEad));
				AccessLog.Log(""+global.clientId+"------- AFTER CALLING GET TAX IN EAD ELSE ");

				doubleIva = doubleIva + Double.parseDouble(taxForEad[0]);
				doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForEad[1]);
				AccessLog.Log(""+global.clientId+"EAD EXISTS");
				ead.GS_DISC=discountEad[1];
				ead.GS_TAX=taxForEad[0];
				ead.GS_TAX_RET=taxForEad[1];
				ead.GS_ADD_PYMT_FLAG="N";
				ead.GS_SRVC_TYPE="N";
				ead.GS_DOCU_TYPE="Q";
				ead.GS_GUIA_TYPE="H";
				ead.GS_STUS_FLAG="A";
				ead.GS_DISC_SLAB_NO=null;
				ead.TOTAL=df.format(Double.parseDouble(ead.GS_SUB_TOTL)-Double.parseDouble(ead.GS_DISC));
				Hashtable calServicestableEad= new Hashtable();
				calServicestableEad.put("ead",ead);
				calServicesList.add(calServicestableEad);
				//AccessLog.Log("INSIDE EAD ELSE IF");
			}

		}*/
		//End EAD Service
       
	    
		//Acknowledgement Service
		Services ack= new Services();
		String descriptionAck = getServicesAditionalDescription(con,aform.getAcusederecibo());
		ack.descriptionAditional=descriptionAck;
		ack.GS_SRVC_ID="ACK";
		String subTotalAck = null;
		String[] amount_totalAck=null;//0--- subTotal, 1---- Minamount
		String discountAck[] = new String[2];
		double taxableAmountAck=0.0;
		
		
		if((defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes"))&& servicesGlobal.ack!=null){
			subTotalAck= getAditionalServicesTotal(con,session,aform.getAcusederecibo(),"ACK");
			
			discountAck[0] = null;
			discountAck[1] = "0";
		//	ack.GS_SUB_TOTL=df.format(Double.parseDouble(subTotalAck)*totalQty); //commented on sep26
			ack.GS_SUB_TOTL=df.format(Double.parseDouble(subTotalAck));
			//doubleServicios=doubleServicios+(Double.parseDouble(subTotalAck)*totalQty-0);//commented on sep26
			doubleServicios=doubleServicios+(Double.parseDouble(subTotalAck)-0);
			taxableAmountAck= Double.parseDouble(ack.GS_SUB_TOTL);
		}else{
			amount_totalAck= getAditionalServicesTotal(con,session,"","0",aform.getAcusederecibo(),"ACK");
			
			ack.GS_SUB_TOTL=amount_totalAck[0];
			//AAP//AccessLog.Log(""+global.clientId+"BEFORE CALLING CALCULATEQUANTITY OF ACK");
			if(Double.parseDouble(ack.GS_SUB_TOTL)>0)
			discountAck = calculateQuantity(con,groupClientId,"ACK","0",amount_totalAck[0],"0");
			else
			{
				discountAck[0] = null;
				discountAck[1] = "0.00";	
			}
			//discountAck = calculateQuantity(con,groupClientId,aform.getAcusederecibo(),amount_totalAck[0],"0","0");//commented and added prec line by amal
			//AAP//AccessLog.Log(""+global.clientId+"AFTER CALLING CALCULATEQUANTITY OF ACK");
			doubleServicios=doubleServicios+(Double.parseDouble(amount_totalAck[0])-Double.parseDouble(discountAck[1]));
			taxableAmountAck= Double.parseDouble(ack.GS_SUB_TOTL)-Double.parseDouble(discountAck[1]);
		}
		//AAP//AccessLog.Log(""+global.clientId+"------- CALLING GET TAX IN ACK ");
		String taxForAck[]=getTax(con,session,"ACK",df.format(taxableAmountAck));
		//AAP//AccessLog.Log(""+global.clientId+"------- AFTER CALLING GET TAX IN ACK ");
		//AccessLog.Log("PEPEPEPE -----------------    FOR ACK AFTER CALLING GETTAX");
		doubleIva = doubleIva + Double.parseDouble(taxForAck[0]);
		//doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForAck[1]);
		//AccessLog.Log("ACK EXISTS");
		ack.GS_DISC=discountAck[1];
		ack.GS_TAX=taxForAck[0];
		ack.GS_TAX_RET="0";
		ack.GS_ADD_PYMT_FLAG="N";
		ack.GS_SRVC_TYPE="N";
		ack.GS_DOCU_TYPE="Q";
		ack.GS_GUIA_TYPE="H";
		ack.GS_STUS_FLAG="A";
		ack.GS_DISC_SLAB_NO=null;
		ack.TOTAL=df.format(Double.parseDouble(ack.GS_SUB_TOTL)-Double.parseDouble(ack.GS_DISC));
		Hashtable calServicestableAck= new Hashtable();
		calServicestableAck.put("ack",ack);
		calServicesList.add(calServicestableAck);
		//End Acknowledgement Service


		/*
		 *Added by palanivel for additional service 
		 *
		 **/

		//Additional  Service

        if(global.tarifType.equalsIgnoreCase("C") && session.getAttribute("aditionalServicesDetail") != null)
		{
         ArrayList additionalServicesArray = null ;
		additionalServicesArray =  (ArrayList) session.getAttribute("aditionalServicesDetail");
		int arraySize =  additionalServicesArray. size();
		

			for(int i=0;i<arraySize;i++)
			{
			
				AdditionalService serviceRecordsRecs = (AdditionalService)additionalServicesArray.get(i);
                Services add1= new Services();
				String add1Ack = getServicesAditionalDescription(con,serviceRecordsRecs.service);
				add1.descriptionAditional=add1Ack;
				add1.GS_SRVC_ID=serviceRecordsRecs.serviceId;
				String subTotalAdd1 = null;
				String[] amount_totalAdd1=null;//0--- subTotal, 1---- Minamount
				String discountAdd1[] = new String[2];
				double taxableAmountAdd1=0.0;
				
				subTotalAdd1= getAditionalServicesTotal(con,session,serviceRecordsRecs.service,serviceRecordsRecs.serviceId);
				discountAdd1[0] = null;
				discountAdd1[1] = "0";
				
				String additionalvalue =  getAdditionalValue(con,global,serviceRecordsRecs.service,serviceRecordsRecs.serviceId);
				
				if (additionalvalue != null  && additionalvalue.equalsIgnoreCase("G") )
				{
                    add1.GS_SUB_TOTL=df.format(Double.parseDouble(subTotalAdd1));
				}
				else if (additionalvalue != null  && additionalvalue.equalsIgnoreCase("K") )
				{
                    add1.GS_SUB_TOTL=df.format(Double.parseDouble(subTotalAdd1)*totalWeight);
				}
				else
				{
					add1.GS_SUB_TOTL=df.format(Double.parseDouble(subTotalAdd1)*totalQty);
				}

				//Modified By Sam[15-06-2006] , for rectifying wrong additional service amt in services
				doubleServicios=doubleServicios+Double.parseDouble(add1.GS_SUB_TOTL);
				//doubleServicios=doubleServicios+(Double.parseDouble(subTotalAdd1)*totalQty-0);

				taxableAmountAdd1= Double.parseDouble(add1.GS_SUB_TOTL);
				//AAP//AccessLog.Log("serviceRecordsRecs.service"+serviceRecordsRecs.service);
				//AAP//AccessLog.Log("serviceRecordsRecs"+serviceRecordsRecs.serviceId);
				String taxForAdd1[]=getTax(con,session,serviceRecordsRecs.serviceId,df.format(taxableAmountAdd1));
				doubleIva = doubleIva + Double.parseDouble(taxForAdd1[0]);
				//doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForAdd1[1]);
				add1.GS_DISC=discountAdd1[1];
				add1.GS_TAX=taxForAdd1[0];
				add1.GS_TAX_RET="0";
				add1.GS_ADD_PYMT_FLAG="N";
				add1.GS_SRVC_TYPE="N";
				add1.GS_DOCU_TYPE="Q";
				add1.GS_GUIA_TYPE="H";
				add1.GS_STUS_FLAG="A";
				add1.GS_DISC_SLAB_NO=null;
				add1.TOTAL=df.format(Double.parseDouble(add1.GS_SUB_TOTL)-Double.parseDouble(add1.GS_DISC));
                Hashtable calServicestableAdd1= new Hashtable();
				calServicestableAdd1.put(serviceRecordsRecs.service,add1);
                calServicesList.add(calServicestableAdd1);
			}
		}
        //End Additional Service

		//COD Service
		if(aform.getValorcod()!=null && aform.getValorcod().trim().length()>0) {
			Services cod= new Services();
			String descriptionCod = getServicesAditionalDescription(con,"COD-1");
			cod.descriptionAditional=descriptionCod;
			cod.GS_SRVC_ID="COD";
			String subTotalCod = null;
			String[] amount_totalCod=null;//0--- subTotal, 1---- Minamount
			String[] discountCod = new String[2];
			double taxableAmountCod = 0.0;
			
			
			
			if((defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes"))&& servicesGlobal.cod!=null){
				subTotalCod= getAditionalServicesTotal(con,session,"COD-1","COD");
				discountCod[0] = null;
				discountCod[1] = "0";
				cod.GS_SUB_TOTL=df.format(Double.parseDouble(subTotalCod)*totalQty);
				doubleServicios=doubleServicios+(Double.parseDouble(subTotalCod)*totalQty-0);
				taxableAmountCod = Double.parseDouble(cod.GS_SUB_TOTL);
				//AccessLog.Log("INSIDE COD IF");
			}else{
				amount_totalCod= getAditionalServicesTotal(con,session,"","","CODR-1","CODR");
				
				cod.GS_SUB_TOTL=amount_totalCod[0];
				//AAP//AccessLog.Log(""+global.clientId+"BEFORE CALLING CALCULATEQUANTITY OF COD"+cod.GS_SUB_TOTL);
				if((Double.parseDouble(cod.GS_SUB_TOTL))>0)				
				discountCod = calculateQuantity(con,groupClientId,"CODR","",cod.GS_SUB_TOTL,amount_totalCod[1]);
				else
				{
					discountCod[0] = null;
					discountCod[1] = "0.00";
				}
				
				//discountCod = calculateQuantity(con,groupClientId,"COD-1",cod.GS_SUB_TOTL,"",amount_totalCod[1]); //commented and added prev line by amal
				//AAP//AccessLog.Log(""+global.clientId+"AFTER CALLING CALCULATEQUANTITY OF COD");
				doubleServicios=doubleServicios+(Double.parseDouble(amount_totalCod[0])-Double.parseDouble(discountCod[1]));
				taxableAmountCod = Double.parseDouble(amount_totalCod[0])-Double.parseDouble(discountCod[1]);
				//AccessLog.Log("INSIDE COD ELSE");
			}
			//AAP//AccessLog.Log(""+global.clientId+"------- CALLING GET TAX IN COD IF ");
			String taxForCod[]=getTax(con,session,"COD",df.format(taxableAmountCod));
			//AAP//AccessLog.Log(""+global.clientId+"------- AFTER CALLING GET TAX IN COD IF "+taxForCod);
			//AccessLog.Log("PEPEPEPE -----------------    FOR COD AFTER CALLING GETTAX");
			doubleIva = doubleIva + Double.parseDouble(taxForCod[0]);
			//doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForCod[1]);
			//AAP//AccessLog.Log(""+global.clientId+"COD EXISTS");
			cod.GS_DISC=discountCod[1];
			cod.GS_TAX=taxForCod[0];
			cod.GS_TAX_RET="0";
			cod.GS_ADD_PYMT_FLAG="N";
			cod.GS_SRVC_TYPE="N";
			cod.GS_DOCU_TYPE="Q";
			cod.GS_GUIA_TYPE="H";
			cod.GS_STUS_FLAG="A";
			cod.GS_DISC_SLAB_NO=null;
			cod.TOTAL=df.format(Double.parseDouble(cod.GS_SUB_TOTL)-Double.parseDouble(cod.GS_DISC));
			Hashtable calServicestableCod= new Hashtable();
			calServicestableCod.put("cod",cod);
			calServicesList.add(calServicestableCod);

			//CODR Service
			Services codr = new Services();
			String descriptionCodr = getServicesAditionalDescription(con,"CODR-1");
			codr.descriptionAditional=descriptionCodr;
			//AAP//AccessLog.Log(""+global.clientId+"CODR EXISTS");
			codr.GS_SRVC_ID="CODR";
			codr.GS_SUB_TOTL="0";
			codr.GS_DISC="0";
			codr.GS_TAX="0";
			codr.GS_TAX_RET="0";
			codr.GS_ADD_PYMT_FLAG="Y";
			codr.GS_SRVC_TYPE="N";
			codr.GS_DOCU_TYPE="Q";
			codr.GS_GUIA_TYPE="H";
			codr.GS_STUS_FLAG="A";
			codr.GS_DISC_SLAB_NO=null;
			codr.TOTAL=df.format(Double.parseDouble(codr.GS_SUB_TOTL)-Double.parseDouble(codr.GS_DISC));
			Hashtable calServicestableCodr= new Hashtable();
			calServicestableCodr.put("codr",codr);
			calServicesList.add(calServicestableCodr);
			getComment(con,session,aform.getValorcod().trim());
			//End CODR Service

		}
		else {
			if((defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes"))&& servicesGlobal.cod!=null){

				// Added Sam [12-06-2006] , if Valorcod is null calculation of cod is not needed in case of tariff type  C
				if(!global.tarifType.equalsIgnoreCase("C"))
				{

					Services cod= new Services();
					String descriptionCod = getServicesAditionalDescription(con,"COD-1");
					cod.descriptionAditional=descriptionCod;
					cod.GS_SRVC_ID="COD";
					String subTotalCod = null;
					//String[] amount_totalCod=null;//0--- subTotal, 1---- Minamount
					String[] discountCod = new String[2];
					double taxableAmountCod = 0.0;
					subTotalCod= getAditionalServicesTotal(con,session,"COD-1","COD");
					discountCod[0] = null;
					discountCod[1] = "0";
					cod.GS_SUB_TOTL=df.format(Double.parseDouble(subTotalCod)*totalQty);
					doubleServicios=doubleServicios+(Double.parseDouble(subTotalCod)*totalQty-0);
					taxableAmountCod = Double.parseDouble(cod.GS_SUB_TOTL);
					//AAP//AccessLog.Log(""+global.clientId+"------- CALLING GET TAX IN COD ELSE ");
					String taxForCod[]=getTax(con,session,"COD",df.format(taxableAmountCod));
					//AAP//AccessLog.Log(""+global.clientId+"------- AFTER CALLING GET TAX IN COD ELSE ");

					doubleIva = doubleIva + Double.parseDouble(taxForCod[0]);
				//	doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForCod[1]);
					//AAP//AccessLog.Log(""+global.clientId+"COD EXISTS");
					cod.GS_DISC=discountCod[1];
					cod.GS_TAX=taxForCod[0];
					cod.GS_TAX_RET="0";
					cod.GS_ADD_PYMT_FLAG="N";
					cod.GS_SRVC_TYPE="N";
					cod.GS_DOCU_TYPE="Q";
					cod.GS_GUIA_TYPE="H";
					cod.GS_STUS_FLAG="A";
					cod.GS_DISC_SLAB_NO=null;
					cod.TOTAL=df.format(Double.parseDouble(cod.GS_SUB_TOTL)-Double.parseDouble(cod.GS_DISC));
					Hashtable calServicestableCod= new Hashtable();
					calServicestableCod.put("cod",cod);
					calServicesList.add(calServicestableCod);
				}
			}
		}

		//Insurance Service
		Services inv = new Services();
		String descriptionInv = getServicesAditionalDescription(con,aform.getCobertura());
		inv.descriptionAditional=descriptionInv;
		inv.GS_SRVC_ID="INV";
		String subTotalInv = null;
		String[] amount_totalInv=null;//0--- subTotal, 1---- Minamount
		String discountInv[] = new String[2];
		double taxableAmountInv=0.0;
		String insuranceView="";
		
		if((defaultScreen!=null && defaultScreen.equalsIgnoreCase("yes"))&& servicesGlobal.inv!=null){
			subTotalInv= getAditionalServicesTotal(con,session,aform.getCobertura(),"INV");
			discountInv[0] = null;
			discountInv[1] = "0";
			inv.GS_SUB_TOTL=df.format(Double.parseDouble(subTotalInv)*totalQty);
			insuranceView=getInsuranceViewFlag(con,session,req);
		if(insuranceView.equalsIgnoreCase("N"))
			doubleServicios=doubleServicios+(Double.parseDouble(subTotalInv)*totalQty-0);
			taxableAmountInv= Double.parseDouble(inv.GS_SUB_TOTL);
		}else{
			amount_totalInv= getAditionalServicesTotal(con,session,"",aform.getValordeclarado().trim(),aform.getCobertura(),"INV");
			inv.GS_SUB_TOTL=amount_totalInv[0];
			//AAP//AccessLog.Log(""+global.clientId+"BEFORE CALLING CALCULATEQUANTITY OF INV");
			if(Double.parseDouble(inv.GS_SUB_TOTL)>0)
			discountInv = calculateQuantity(con,groupClientId,"INV",amount_totalInv[0],aform.getValordeclarado().trim(),"0");
			else
			{
			discountInv[0] = null;
			discountInv[1] = "0.00";
			}
			//discountInv = calculateQuantity(con,groupClientId,aform.getCobertura(),amount_totalInv[0],aform.getValordeclarado().trim(),"0"); commented and added prev line by amal
			//AAP//AccessLog.Log(""+global.clientId+"AFTER CALLING CALCULATEQUANTITY OF INV");
			insuranceView=getInsuranceViewFlag(con,session,req);
			if(insuranceView.equalsIgnoreCase("N"))
			doubleServicios=doubleServicios+(Double.parseDouble(amount_totalInv[0])-Double.parseDouble(discountInv[1]));
			taxableAmountInv= Double.parseDouble(inv.GS_SUB_TOTL)-Double.parseDouble(discountInv[1]);
		}
		//AAP//AccessLog.Log(""+global.clientId+"------- CALLING GET TAX IN INV ");
		String taxForInv[]=getTax(con,session,"INV",df.format(taxableAmountInv));
		//AAP//AccessLog.Log(""+global.clientId+"------- AFTER CALLING GET TAX IN INV ");
		//AccessLog.Log("PEPEPEPE -----------------    FOR INV AFTER CALLING GETTAX");
		insuranceView=getInsuranceViewFlag(con,session,req);
		if(insuranceView.equalsIgnoreCase("N"))
		doubleIva = doubleIva + Double.parseDouble(taxForInv[0]);
		insuranceView=getInsuranceViewFlag(con,session,req);
		//if(insuranceView.equalsIgnoreCase("N"))
		//doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForInv[1]);
		insuranceView="";
		//AAP//AccessLog.Log(""+global.clientId+"INV EXISTS");
		inv.GS_DISC=discountInv[1];
		inv.GS_TAX=taxForInv[0];
		inv.GS_TAX_RET="0";
		inv.GS_ADD_PYMT_FLAG="N";
		inv.GS_SRVC_TYPE="N";
		inv.GS_DOCU_TYPE="Q";
		inv.GS_GUIA_TYPE="H";
		inv.GS_STUS_FLAG="A";
		inv.GS_DISC_SLAB_NO=null;
		inv.TOTAL=df.format(Double.parseDouble(inv.GS_SUB_TOTL)-Double.parseDouble(inv.GS_DISC));
		Hashtable calServicestableInv= new Hashtable();
		calServicestableInv.put("inv",inv);
		calServicesList.add(calServicestableInv);
		//End Insurance Service
		session.setAttribute("calculatedservicelist",calServicesList);

		servicesTotal.servicios=df.format(doubleServicios);
		servicesTotal.subTotal=df.format((Double.parseDouble(servicesTotal.flete)+
										  Double.parseDouble(servicesTotal.servicios))
										 -Double.parseDouble(servicesTotal.descuento));

		servicesTotal.iva=df.format(doubleIva);
		servicesTotal.ivaRet=df.format(doubleIvaRet);
		servicesTotal.total	=df.format((Double.parseDouble(servicesTotal.subTotal)+doubleIva)- doubleIvaRet);

		session.setAttribute("servicestotal",servicesTotal);
	}

	public String getServicesAditionalDescription(Connection con,
												  String serviceId)throws Exception{

		PreparedStatement pst = con.prepareStatement("select pack_web.fun_srvc_name(?) from dual");//Checked
		pst.setString(1,serviceId);
		ResultSet rs =pst.executeQuery();
		String description="";
		if(rs.next()){
			description=rs.getString(1);
		}

		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		//AAP//AccessLog.Log("$$$$$$$$$$$$$$$$$$$$$$$$ ADITIONAL SERVICE DESCRIPTION"+description);
		return description;
	}


	//Returns discountSlab,discountAmount
	public String[] calculateQuantity(Connection con,
									  String grpclientId,
									  String serviceId,
									  String qty,
									  String amount,
									  String minAmount)throws Exception {

		//AAP//AccessLog.Log("----------------INSIDE CALCULATE QUANTITY ------------------");
		//AAP//AccessLog.Log("----------------inputs are grpclientId-"+grpclientId+"--serviceId"+serviceId+"--qty"+qty+"amount"+amount+"minAmount"+minAmount);
		
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		String query = "Begin ? := pack_web.fun_calc_disc(?,?,?,?,?,?); End; ";
		//AAP//AccessLog.Log("BEFORE PREPARING CALCULATE QUANTITY'S CALLABLE STATEMENT ");
		CallableStatement cst=con.prepareCall(query);
		//AAP//AccessLog.Log("AFTER PREPARING CALCULATE QUANTITY'S CALLABLE STATEMENT ");
		cst.registerOutParameter(1,Types.NUMERIC);
		cst.setString(2,serviceId);
		cst.setString(3,grpclientId);

		if(amount!=null && amount.length()==0)
			//cst.setDouble(4,0.0d);//Empty
			cst.setNull(4,Types.NUMERIC);
		else
			cst.setDouble(4,Double.parseDouble(amount));
		if(qty!=null && qty.length()==0)
			//cst.setDouble(5,0.0d);//Empty
			cst.setDouble(5,Types.NUMERIC);
		else
			cst.setDouble(5,Double.parseDouble(qty));
		if(minAmount!=null && minAmount.length()==0)
			//cst.setDouble(6,0.0d);//Empty
			cst.setNull(6,Types.NUMERIC);
		else
			cst.setDouble(6,Double.parseDouble(minAmount));
		cst.registerOutParameter(7,Types.VARCHAR);

		cst.executeQuery();
		String a[] = new String[2];
		a[0] = cst.getString(7);
		a[1] = df.format(cst.getDouble(1));

		//AAP//AccessLog.Log(" DISCOUNT SLAB IN SERVICE IN CALCULATE QUANTIY "+a[0]);
		//AAP//AccessLog.Log(" DISCOUNT AMOUNT IN SERVICE IN CALCULATE QUANTIY "+a[1]);

		if(cst!=null)
			cst.close();
		return a;
	}

	public java.sql.Date getSysdate(Connection con)throws Exception{
		PreparedStatement pst = con.prepareStatement("select sysdate from dual");//Checked
		ResultSet rs = pst.executeQuery();
		java.sql.Date sysDate =null;
		if(rs.next())
			//sysDate = rs.getString(1);
			sysDate=rs.getDate(1);

		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		//AAP//AccessLog.Log("SYSDATE IN SERVICES ACTION "+sysDate);
		return sysDate;
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

	public ArrayList getLcZone(Connection con,HttpSession session)throws Exception {
		ArrayList datos = new ArrayList(2);
		JavWebBookingGeneralForm generalForm = (JavWebBookingGeneralForm)session.getAttribute("webBookinggeneral");
		String query = "{ call pack_web.pro_brnc_coly_ze(?,?,?,?,?,?) }";//Checked//AAP01
		CallableStatement cst = con.prepareCall(query);
		cst.setString(1,generalForm.citycode);
		cst.setString(2,generalForm.destinationcoloniacode);
		cst.setString(3,generalForm.destinationcode);
		cst.registerOutParameter(4,Types.VARCHAR);
		cst.registerOutParameter(5,Types.VARCHAR);
		cst.registerOutParameter(6,Types.VARCHAR);//AAP01
		cst.executeQuery();
				
		if (cst.getString(4)== null){//AAP01
			datos.add("");//AAP01
		}else{
			datos.add(cst.getString(4));//zona				//AAP01
		}
		
		if (cst.getString(6)== null){
			datos.add("");//AAP01
		}else{
			datos.add(cst.getString(6));//operador logistico//AAP01
		}

		if(cst!=null)
			cst.close();

		return datos;
	}
	//code added on 29/03/2004
	public String getRadZone(Connection con,HttpSession session)throws Exception {

		JavWebBookingGeneralForm generalForm = (JavWebBookingGeneralForm)session.getAttribute("webBookinggeneral");
		String query = "{ call pack_web.pro_brnc_coly(?,?,?,?,?) }";//Checked
		//AAP//AccessLog.Log("orginCityCode"+generalForm.orginCityCode+"generalForm.originColinaCode"+generalForm.originColinaCode+"orgioncode"+generalForm.orgioncode);
		CallableStatement cst = con.prepareCall(query);
		cst.setString(1,generalForm.orginCityCode);
		cst.setString(2,generalForm.originColinaCode);
		cst.setString(3,generalForm.orgioncode);
		cst.registerOutParameter(4,Types.VARCHAR);
		cst.registerOutParameter(5,Types.VARCHAR);
		cst.executeQuery();
		String lcZone = cst.getString(4);

		if(cst!=null)
			cst.close();
		return lcZone ;
	}

	//Total For AditionalServices With Non-defalut screen
	public String[] getAditionalServicesTotal(Connection con,
											  HttpSession session,
											  String lcZone,
											  String totalAmount,
											  String serviceId,
											  String referenceServiceId)throws Exception {
		Global global = (Global)session.getAttribute("sGlobal");
		//AAP//AccessLog.Log("serviceId"+serviceId+"referenceServiceId"+referenceServiceId+"global.assignedBranch"+global.assignedBranch+"global.destinationBranchId"+global.destinationBranchId+"lcZone"+lcZone+"totalAmount"+totalAmount);
		String[] amount_total = new String[2];
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		boolean defaServcItemExist = true;
		// Added on 03/06/2009 Purpose: To Check the "RAD" service for client in the SYS_CLNT_SRVC. 
		if (referenceServiceId.equalsIgnoreCase("RAD"))
		{
			
			defaServcItemExist = false;
			PreparedStatement pst = con.prepareStatement("select CS_DEFA_SRVC_ITEM from SYS_CLNT_SRVC where CS_CLNT_ID =? and CS_SRVC_ID =?");//Checked
			pst.setString(1,global.clientId);
			pst.setString(2,referenceServiceId);
			ResultSet rs = pst.executeQuery();
			if(rs.next())
			{
				defaServcItemExist = true;
			}
			if(rs!=null)
				rs.close();
			if(pst!=null)
				pst.close();
		}
//	  ADDED on 16/011/2009 Purpose: To calcualate the "RAD" service of the client from PACK_WEB.FUN_FTCH_RAD_SRVC FUNCTION.  
		if (referenceServiceId.equalsIgnoreCase("RAD"))
		{
			if (defaServcItemExist)
			{
				//AAP//AccessLog.Log("16-11-2009=="+"INSIDE RAD SERVICE");
				String query = "Begin ? := pack_web.FUN_FTCH_ADD_RAD_SRVC(?,?,?,?,?,?,?); End;";//Checked
				CallableStatement cst = con.prepareCall(query);
				cst.registerOutParameter(1,Types.NUMERIC);
				cst.setString(2,serviceId);
				cst.setString(3,referenceServiceId);
				cst.setString(4,global.assignedBranch);
				cst.setString(5,global.destinationBranchId);
								
				if(totalAmount!=null && totalAmount.length()==0)
					cst.setNull(6,Types.NUMERIC);
				else
					cst.setDouble(6,Double.parseDouble(totalAmount));
				
				cst.setString(7,"NON");
				
				cst.registerOutParameter(8,Types.NUMERIC);

				cst.executeQuery();
				
				amount_total[0] = df.format(cst.getDouble(1));
				amount_total[1] = df.format(cst.getDouble(8));
				if(cst!=null)
					cst.close();
			}
			else
			{
				amount_total[0] = df.format(0);
				amount_total[1] = df.format(0);
			
			}

		}
		else
		{
				String query = "Begin ? := pack_web.FUN_FTCH_ADD_SRVC(?,?,?,?,?,?,?,?); End;";//Checked
				CallableStatement cst = con.prepareCall(query);
				cst.registerOutParameter(1,Types.NUMERIC);
				cst.setString(2,serviceId);
				cst.setString(3,referenceServiceId);
				cst.setString(4,global.assignedBranch);
				cst.setString(5,global.destinationBranchId);
				cst.setString(6,lcZone);
				
				if (totalAmount == null || (totalAmount != null && totalAmount.isEmpty()))
					cst.setNull(7,Types.NUMERIC);
				else
					cst.setDouble(7,Double.parseDouble(totalAmount));
				
				cst.setString(8,"NON");
				
				cst.registerOutParameter(9,Types.NUMERIC);

				cst.executeQuery();
				
				amount_total[0] = df.format(cst.getDouble(1));
				amount_total[1] = df.format(cst.getDouble(9));
				if(cst!=null)
					cst.close();
			}
				
		
		//  Commented on 16/011/2009
		/*
		if (defaServcItemExist)
		{
				
			String query = "Begin ? := pack_web.FUN_FTCH_ADD_SRVC(?,?,?,?,?,?,?,?); End;";//Checked
			CallableStatement cst = con.prepareCall(query);
			cst.registerOutParameter(1,Types.NUMERIC);
			cst.setString(2,serviceId);
			cst.setString(3,referenceServiceId);
			cst.setString(4,global.assignedBranch);
			cst.setString(5,global.destinationBranchId);
			cst.setString(6,lcZone);
			
			if(totalAmount!=null && totalAmount.length()==0)
				cst.setNull(7,Types.NUMERIC);
			else
				cst.setDouble(7,Double.parseDouble(totalAmount));
			
			cst.setString(8,"NON");
			
			cst.registerOutParameter(9,Types.NUMERIC);

			cst.executeQuery();
			
			amount_total[0] = df.format(cst.getDouble(1));
			amount_total[1] = df.format(cst.getDouble(9));
			if(cst!=null)
				cst.close();
		}
		else
		{
			amount_total[0] = df.format(0);
			amount_total[1] = df.format(0);
		}

		*/
	
//
/*		Comment on 03/06/09
		String query = "Begin ? := pack_web.FUN_FTCH_ADD_SRVC(?,?,?,?,?,?,?,?); End;";//Checked
		CallableStatement cst = con.prepareCall(query);
		cst.registerOutParameter(1,Types.NUMERIC);
		cst.setString(2,serviceId);
		cst.setString(3,referenceServiceId);
		cst.setString(4,global.assignedBranch);
		
		cst.setString(5,global.destinationBranchId);
		
		cst.setString(6,lcZone);
		
		if(totalAmount!=null && totalAmount.length()==0)
			//cst.setDouble(7,0.0d);//Empty
			cst.setNull(7,Types.NUMERIC);
		else
			cst.setDouble(7,Double.parseDouble(totalAmount));
		cst.setString(8,"NON");
		
		cst.registerOutParameter(9,Types.NUMERIC);

		cst.executeQuery();
		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		String[] amount_total = new String[2];
		amount_total[0] = df.format(cst.getDouble(1));
		amount_total[1] = df.format(cst.getDouble(9));
		if(cst!=null)
				cst.close();
*/			
	
		return amount_total;
	}


	//Total For AditionalServices With Defalut screen
	public String getAditionalServicesTotal(Connection con,
											HttpSession session,
											String serviceId,
											String referenceServiceId)throws Exception {

		Global global = (Global)session.getAttribute("sGlobal");
		String query = "select pack_web.FUN_FTCH_TRIF_AMNT(?,?,?,?,?,?) from dual";//Checked
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
		return new java.text.DecimalFormat("0.00").format(amount);
	}

	public String[] getTax(Connection con,
						   HttpSession session,
						   String referenceServiceId,
						   String taxableAmount)throws Exception {

		String taxQuery = "{call pack_sipweb.PRO_CALC_TAX_AMT(?,?,?,?,?)}";//Checked
		Global global =  (Global)session.getAttribute("sGlobal");

		boolean isOrgionBorderBranch = global.isOrigionBorderBranch;
		boolean isDestinationBorderBranch = global.isDestinationBorderBranch;
		//AAP//AccessLog.Log(""+global.clientId+"-------- INSIDE GET TAX METHOD BEFORE PREPARING THE CALLABEL STATEMENT ");
		CallableStatement cst  = con.prepareCall(taxQuery);
		//AAP//AccessLog.Log(""+global.clientId+"PE ------- AFTER PREPARING THE CALLABLE STATEMENT ");

		if(isOrgionBorderBranch && !isDestinationBorderBranch){
			cst.setString(1,global.destinationBranchId);
			//AAP//AccessLog.Log(""+global.clientId+"get tax global.destinationBranchId "+global.destinationBranchId);
		}
		else{
			cst.setString(1,global.assignedBranch);
			//AAP//AccessLog.Log(""+global.clientId+"get tax global.assignedBranch "+global.assignedBranch);
		}

		//AAP//AccessLog.Log(""+global.clientId+"get tax referenceServiceId "+referenceServiceId);
		//AAP//AccessLog.Log(""+global.clientId+"get tax taxableAmount "+taxableAmount);

		cst.setString(2,referenceServiceId);
		cst.setDouble(3,Double.parseDouble(taxableAmount));
		cst.registerOutParameter(4,Types.NUMERIC);
		cst.registerOutParameter(5,Types.NUMERIC);
		cst.executeQuery();

		java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
		String tax[] = new String[2];
		tax[0] = df.format(cst.getDouble(4));
		
		//AAP//AccessLog.Log(""+global.clientId+"tax of 0 "+tax[0]);
		//AAP//AccessLog.Log(""+global.clientId+"*&*&*&*&*&*&*&* INSIDE GET TAX METHOD CLIENT TYPE "+global.clientType);
		//AAP//AccessLog.Log(""+global.clientId+"cst.getDouble(5) "+cst.getDouble(5));
		if(global.clientType!=null && global.clientType.equalsIgnoreCase("C") ||
		   global.clientType!=null && global.clientType.equalsIgnoreCase("G")){
			//AAP//AccessLog.Log(""+global.clientId+" IF INSIDE ");
			if (global.rfc==null || global.rfc.length()!=14)//added by Emerson on 05/02/2004
				tax[1]="0";
			else
				tax[1] = df.format(cst.getDouble(5));
			}
		else{
			//AAP//AccessLog.Log(" "+global.clientId+"ELSE INSIDE ");
			tax[1]="0";
		}


		if(cst!=null)
			cst.close();
		//AAP//AccessLog.Log(""+global.clientId+"tax of 0 "+tax[1]);
		//AAP//AccessLog.Log(""+global.clientId+"------------------- INSIDE GET TAX 23RD MAY PRINT AFTER CALCULATING TAX");
		return tax;
	}

	//code added by sundar on 29/10/2003 for displaying description,content by default
    public boolean updateDescCont(Connection con,String clientid,String desc,String cont) throws Exception{
		PreparedStatement pst=null;
		
		//AAP//AccessLog.Log(""+clientid+"------------------- INSIDE updateDescCont");
		String query="update web_clnt_mstr set wc_desc=?,wc_cont=?"+
					 "where wc_clnt_id=?";
		pst=con.prepareStatement(query);
		pst.setString(1,desc);
		pst.setString(2,cont);
		pst.setString(3,clientid);
		pst.executeUpdate();
		if(pst!=null)
			pst.close();
		//AAP//AccessLog.Log(""+clientid+"------------------- INSIDE 2nd updateDescCont");
		return true;
	}

	public boolean validateFields(Connection con,
								  HttpServletRequest req,
								  JavWebBookingServicesForm serForm)throws Exception {

		HttpSession session = req.getSession(false);

		Global global = (Global)session.getAttribute("sGlobal");

        // palanivel Validation for the client status type
		
		//outtime=System.currentTimeMillis();
		String prequery = "SELECT CM_CLNT_ID FROM SYS_CLNT_MSTR  "+
					                  " WHERE  CM_CLNT_ID = ? AND CM_CRED_STUS_ID =  'ENA' ";
		
        PreparedStatement pst = con.prepareStatement(prequery);
		pst.setString(1,global.clientId);
		//AAP//AccessLog.Log(""+global.clientId+" inside validate fields.....................................................");
		ResultSet rs = pst.executeQuery();
		//intime=System.currentTimeMillis();
	//	AccessLog.Log(global.clientId+".....................AFter prequery"+(intime-outtime));
		if(!rs.next())
		{
			String messageText="";
			String query1 = "{call pack_web.PRO_SHOW_MESG('BOK',PACK_WEB.LANGUAGE_ID,900120,1,NULL,?,NULL,?,?)}";
			CallableStatement cst2=con.prepareCall(query1);
			cst2.setString(1,global.clientName);
			cst2.registerOutParameter(2,Types.VARCHAR);
			cst2.registerOutParameter(3,Types.VARCHAR);
			cst2.executeQuery();
			messageText = cst2.getString(3);
            req.setAttribute("errormsgstatus",messageText.concat(global.clientName));
          //code added by kumaran 14th oct
			return false;
		}
	//	intime=System.currentTimeMillis();
		//AccessLog.Log(global.clientId+".....................AFter prequery loop"+(intime-outtime));
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		//from here added newly
		
	/*	intime=System.currentTimeMillis();
		
			double cdrlmt=0.0;
			String groupClientId="";
			String clientType="";
			prequery = "SELECT CM_CLNT_ID,NVL(cm_cred_limt,0) cm_cred_limt, cm_grup_clnt_id, cm_clnt_type FROM SYS_CLNT_MSTR  "+
				            " WHERE  CM_CLNT_ID = ? ";
				
				 pst = con.prepareStatement(prequery);
				pst.setString(1,global.clientId);
				
				 rs = pst.executeQuery();
				
				
				while(rs.next())
				{
					cdrlmt=rs.getDouble("cm_cred_limt");
					groupClientId=rs.getString("cm_grup_clnt_id");
					clientType=rs.getString("cm_clnt_type");
				}
				
				
				if(rs!=null)
				rs.close();
				if(pst!=null)
				pst.close();
				String query2 ="";
				
				if((clientType.equalsIgnoreCase("I"))||(clientType.equalsIgnoreCase("C")))
				{
					query2 = " SELECT NVL(gh_guia_amnt,0) FROM BOK_GUIA_HEAD WHERE gh_bill_clnt_id = ? AND" +
								" gh_pymt_type = 'C' AND gh_paid_date IS NULL AND "+ 
								" gh_guia_type IN ('N','S','G','T','H') AND gh_docu_type IN ('G','D','Q') AND "+
								" gh_used_date IS NULL AND gh_actv_flag = 'A'";
				}
				else if((clientType.equalsIgnoreCase("N"))||(clientType.equalsIgnoreCase("G")))
				{
								query2 = "SELECT NVL(gh_guia_amnt,0) FROM BOK_GUIA_HEAD WHERE gh_bill_clnt_id IN "+
							" (SELECT cm_clnt_id FROM SYS_CLNT_MSTR	WHERE cm_grup_clnt_id = ?) "+
					 	" AND	gh_pymt_type = 'C'	AND gh_paid_date IS NULL "+
					 	" AND gh_guia_type IN ('N','S','G','T','H') AND gh_docu_type IN ('G','D','Q') "+
					 	" AND gh_used_date IS NULL 	AND gh_actv_flag = 'A' ";
				
								query2="SELECT NVL(gh_guia_amnt,0) FROM BOK_GUIA_HEAD a,  sys_clnt_mstr b "+
								" where a.gh_bill_clnt_id = b.CM_CLNT_ID and b.cm_grup_clnt_id = ? "+
								" and gh_bill_brnc_id = gh_bill_brnc_id AND gh_paid_date IS NULL "+
								" AND gh_guia_type IN ('N','S','G','T','H') AND gh_docu_type IN  ('G','D','Q') "+
								" AND gh_actv_flag =  'A' AND	gh_pymt_type = 'C'	AND gh_used_date IS NULL";		
				}
				
				
			PreparedStatement pst1 = con.prepareStatement(query2);
			if((clientType.equalsIgnoreCase("N"))||(clientType.equalsIgnoreCase("G")))
			pst1.setString(1,groupClientId);
			else
				pst1.setString(1,global.clientId);	
			
			ResultSet rs1 = pst1.executeQuery();
			double sumGuia=0.0;
			while(rs1.next())
			{
				sumGuia=sumGuia+ rs1.getDouble(1);
			}
			
			if(rs1!=null)
				rs1.close();
			if(pst1!=null)
				pst1.close();
		*/
		
		
		String msgText="";
		double doubleavailCred	= 0.0;
		double doubleguiaAmnt	= 0.0;
		
		//doubleavailCred= cdrlmt-sumGuia;
		
	//	outtime=System.currentTimeMillis();
//		AccessLog.Log(global.clientId+"After credt check"+(outtime-intime));
	// up to this new code
		
	//	Global global = (Global)session.getAttribute("sGlobal");
		outtime=System.currentTimeMillis();
		String query2 = "select pack_web.FUN_CLNT_CRDT_AMT(?) from dual";
		PreparedStatement pst1 = con.prepareStatement(query2);
		pst1.setString(1,global.clientId);
		//AAP//AccessLog.Log(""+global.clientId+" inside second statement.....................................................");
		ResultSet rs1 = pst1.executeQuery();
		if(rs1.next())
			doubleavailCred = rs1.getDouble(1);
		if(rs1!=null)
			rs1.close();
		if(pst1!=null)
			pst1.close();
	//	intime=System.currentTimeMillis();
		//AAP//AccessLog.Log((global.clientId+".....................AFter pack_web.FUN_CLNT_CRDT_AMT(?)"+(intime-outtime)));
		//AAP//AccessLog.Log(""+global.clientId+".....................................................");
		//AAP//AccessLog.Log(""+global.clientId+"inside Validate Fields the avail cred limit"+doubleavailCred);
		
		if (doubleavailCred>0){
			//AAP//AccessLog.Log(""+global.clientId+"........................... befoer calling cal services");
		//	outtime=System.currentTimeMillis();
			calculateServices(con,serForm,req);
			//intime=System.currentTimeMillis();
		//	AccessLog.Log(global.clientId+".....................after calculateServices"+(intime-outtime));
			ServicesTotal servicesTotal = (ServicesTotal)session.getAttribute("servicestotal");
			doubleguiaAmnt=Double.parseDouble(servicesTotal.total);
			//AAP//AccessLog.Log(""+global.clientId+".....................................................");
			//AAP//AccessLog.Log(""+global.clientId+"inside Validate Fields thepurchased amount"+doubleguiaAmnt);
		}
		if ((doubleavailCred<1) || (doubleavailCred<doubleguiaAmnt)){
			String query3 = "{call pack_web.PRO_SHOW_MESG('BOK',PACK_WEB.LANGUAGE_ID,900019,1,NULL,?,NULL,?,?)}";
			CallableStatement cst1=con.prepareCall(query3);
			cst1.setString(1,""+doubleavailCred);
			cst1.registerOutParameter(2,Types.VARCHAR);
			cst1.registerOutParameter(3,Types.VARCHAR);
			cst1.executeQuery();
			msgText = cst1.getString(3);
			req.setAttribute("erroravailcred",msgText);
		}
		if(msgText.length()>0)
			return false;

        
		//ServicesGlobal servicesGlobal=(ServicesGlobal)session.getAttribute("servicesGlobal");
		String entrega = serForm.getEntrega();
		String errMsg="";
		if(entrega!=null && entrega.equalsIgnoreCase("2")){
			
			//AAP//AccessLog.Log("global.assignedBranch"+global.assignedBranch);
			//AAP//AccessLog.Log("global.destinationBranchId"+global.destinationBranchId);
			//pst.setString(3,global.destinationBranchId)
			String orginsite=global.assignedBranch.substring(1,3);
			String destsite=global.destinationBranchId.substring(1,3);
			//AAP//AccessLog.Log("global.assignedBranch"+orginsite);
			//AAP//AccessLog.Log("global.destinationBranchId"+destsite);	
			if(orginsite.equalsIgnoreCase(destsite)) {
				//AAP//AccessLog.Log("same site");			
			} else {
				errMsg = getBranchColony(con,session);
				req.setAttribute("errormsgentrega",errMsg);
			}
		}

		/*if(errMsg.length()>0 || messageText.length()>0)//Commented and below code added by Kumaran on 04/02/2005 to make the credit check
			return false;
		else
			return true;*/
		if(errMsg.length()>0 )
			return false;

		//Above code added by Kumaran on 04/02/2005 to make the credit check
		//Valor COD Validation
		String valorCOD = serForm.getValorcod().trim();
		double doublevalorCOD = 0.0;

		if(valorCOD!=null && valorCOD.length()>0)
			doublevalorCOD = Double.parseDouble(valorCOD);
		//String messageType="";
		String messageText="";
		if(doublevalorCOD < 0){
			String query = "{call pack_web.PRO_SHOW_MESG('BOK',PACK_WEB.LANGUAGE_ID,900222,1,NULL,NULL,NULL,?,?)}";
			CallableStatement cst=con.prepareCall(query);
			cst.registerOutParameter(1,Types.VARCHAR);
			cst.registerOutParameter(2,Types.VARCHAR);
			cst.executeQuery();
			messageText = cst.getString(2);
			req.setAttribute("errormsgcod",messageText);
		}
		if( messageText.length()>0)
			return false;
		else
			return true;

	}

	public void getComment(Connection con,HttpSession session,String valorCod)throws Exception{
		Global global = (Global)session.getAttribute("sGlobal");
		CallableStatement cst = con.prepareCall("{call pack_web.PRO_COD_TEXT(?,?)}");
		cst.setDouble(1,Double.parseDouble(valorCod));//To be Checked
		cst.registerOutParameter(2,Types.VARCHAR);
		cst.executeQuery();
		String commentText = cst.getString(2);
		global.commentText = commentText;
		//AAP//AccessLog.Log(""+global.clientId+"**************************** comment text"+commentText);
		session.setAttribute("sGlobal",global);
		if(cst!=null)
			cst.close();
	}

	public void printFileForGuia(Connection con,
								 HttpServletRequest request,
								 JavWebBookingServicesForm serForm,
								 String genGuiaNumber,
								 String contId,
								 String defaultEadRout,
								 String invoiceFlag,
								 String contactName,
								 String comText,
								 java.sql.Timestamp sysDate)throws Exception {

		HttpSession session = request.getSession(false);
		ServicesTotal serTotal =  (ServicesTotal)session.getAttribute("servicestotal");
		Global global = (Global)session.getAttribute("sGlobal");
		JavWebBookingGeneralForm generalForm = (JavWebBookingGeneralForm)session.getAttribute("webBookinggeneral");

		String eadFlag = (serForm.getEntrega().equals("2")?"1":"0");

		java.sql.CallableStatement cst=null;
		
		//cst = con.prepareCall("{call PACK_GUIA_PRINT.PRO_GUIA_PRINT_PRINT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");//Checked
		
		/* 
		 * Changed PACK_GUIA_PRINT.PRO_GUIA_PRINT_PRINT as PACK_GUIA_PRINT.PRO_GUIA_PRINT_PRINT_FE
		 * for KITS bug id 0070933 posted on 11-29-10 10:56
		 * 
		 */
		cst = con.prepareCall("{call PACK_GUIA_PRINT.PRO_GUIA_PRINT_PRINT_FE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");//Checked
		cst.setString(1,genGuiaNumber);
		cst.setString(2,global.assignedBranch);
		cst.setString(3,global.destinationBranchId);
		cst.setString(4,global.clientId);
		cst.setString(5,global.clientName);
		cst.setString(6,generalForm.getDestinationclave());
		cst.setString(7,generalForm.getDestinationnombre());
		cst.setString(8,eadFlag);
		cst.setString(9,generalForm.getPedinumber());
		cst.setString(10,generalForm.getCustagent());
		//cst.setDate(11,getSysdate(con));--commented and below line coded by B.Emerson on 19/07/2003
		cst.setTimestamp(11,sysDate);
		cst.setString(12,serForm.getGuiano().trim().toUpperCase());
		if(serForm.getValordeclarado().trim().length()>0)
			cst.setDouble(13,Double.parseDouble(serForm.getValordeclarado().trim()));//Empty
		else
		//cst.setDouble(13,0.0d);
		cst.setNull(13,Types.NUMERIC);
		cst.setDouble(14,Double.parseDouble(serTotal.totalWeight));
		cst.setString(15,comText);
		cst.setString(16,contId);
		cst.setString(17,contactName);
		cst.setString(18,"");
		cst.setString(19,generalForm.getOrgionbranch());
		cst.setString(20,generalForm.getDestinationbranch());
		cst.setString(21,defaultEadRout);
		cst.setString(22,invoiceFlag);
		cst.setString(23,serForm.getCobertura());
		cst.setString(24,global.groupClientId);
		cst.setString(25,serForm.getSeguro());
		cst.registerOutParameter(26,Types.LONGVARCHAR);
		cst.setString(27,generalForm.getOrgien1());
		cst.setString(28,generalForm.getDestino1());
		cst.executeQuery();		
        new PrintFileExport(request.getRealPath("/")).writeToGuiaFile(request,cst.getString(26),getTimeStamp(con).getTime());
		if(cst!=null)
			cst.close();
	}

	/*public void printFileForBcl(Connection con,
								HttpServletRequest request,
								JavWebBookingServicesForm serForm,
								String genGuiaNumber,
								String contId,
								String defaultEadRout,
								String invoiceFlag,
								String contactName)throws Exception {

		HttpSession session = request.getSession(false);
		ServicesTotal serTotal =  (ServicesTotal)session.getAttribute("servicestotal");
		Global global = (Global)session.getAttribute("sGlobal");
		JavWebBookingGeneralForm generalForm = (JavWebBookingGeneralForm)session.getAttribute("webBookinggeneral");

		String eadFlag = (serForm.getEntrega().equals("2")?"1":"0");

		java.sql.CallableStatement cst=null;
		cst = con.prepareCall("{call PACK_GUIA_PRINT.PRO_BCL_PRNT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

		cst.registerOutParameter(15,Types.LONGVARCHAR);
		cst.setString(1,global.assignedBranch);
		cst.setString(2,genGuiaNumber);
		cst.setDate(3,getSysdate(con));
		cst.setString(4,generalForm.getOrgionbranch());
		cst.setString(5,generalForm.getDestinationbranch());
		cst.setString(6,global.destinationBranchId);
		cst.setString(7,global.assignedBranch);
		cst.setString(8,serForm.getGuiano().trim().toUpperCase());
		cst.setString(9,eadFlag);
		cst.setString(10,generalForm.getOrgiennombre());
		cst.setString(11,generalForm.getOrgien1());
		cst.setString(12,generalForm.getDestinationnombre());
		cst.setString(13,generalForm.getDestino1());
		cst.setString(14,global.commentText);

		cst.executeQuery();

		AccessLog.Log("BRANCH ID "+global.assignedBranch);
		AccessLog.Log("GUIA NUMBER "+genGuiaNumber);
		AccessLog.Log("ISSUE DATE "+getSysdate(con));
		AccessLog.Log("ORIGION BRANCH "+generalForm.getOrgionbranch());
		AccessLog.Log("DESTINATION BRANCH "+generalForm.getDestinationbranch());
		AccessLog.Log("DEST BRANCH ID "+global.destinationBranchId);
		AccessLog.Log("ORGION BRANCH ID "+global.assignedBranch);
		AccessLog.Log("FORM NUMBER "+serForm.getGuiano().trim().toUpperCase());
		AccessLog.Log("EAD FLAG "+eadFlag);
		AccessLog.Log("ORGION CLIENT NAME "+generalForm.getOrgiennombre());
		AccessLog.Log("ORGION STREET NAME "+generalForm.getOrgien1());
		AccessLog.Log("DESTINATION CLIENT NAME "+generalForm.getDestinationnombre());
		AccessLog.Log("DESTINATION STREET NAME "+generalForm.getDestino1());
		AccessLog.Log("COMMENT TEXT "+global.commentText);

		AccessLog.Log("BCL PRINT STRING "+cst.getString(15));

		new PrintFileExport(request.getRealPath("/")).writeToBarcodeFile(request,cst.getString(15));

		if(cst!=null)
			cst.close();

	}*/

	//code added by palanivel  to check whether AdditionalService Available for the client or not
    
	public String getAdditionalService(Connection con,
									               Global global,
                    	         				   HttpServletRequest req,
									                JavWebBookingServicesForm serForm)throws Exception 
	{

		String query = "select pack_web.fun_disp_addl_srvc(?,?,?,?) from dual";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1,global.clientId);
		pst.setString(2,global.tarifType);
		pst.setString(3,global.assignedBranch);
		pst.setString(4,global.destinationBranchId);
		ResultSet rs = pst.executeQuery();
		String serviceAvailable=null;
		if(rs.next()){
			serviceAvailable = rs.getString(1);
		}
		if(pst!=null)
			pst.close();
		if(rs!=null)
			rs.close();
		return serviceAvailable;
	}
	
	public String getAdditionalValue(Connection con,
									               Global global,
                    	         				   String serviceId,
											       String referenceServiceId )throws Exception 
	{

		String query = "select  wt_srvc_cant from  web_clnt_srvc_trif  where wt_orgn_clnt_id = ?  and "+
                			"  substr(WT_ORGN_BRNC_ID,1,3)=substr(?,1,3)  and 	substr(WT_DEST_BRNC_ID,1,3)=substr(?,1,3)  and  wt_trif_slab    =   ?   and "+
			                " wt_refr_srvc_id= ? and 	wt_srvc_id     = ?";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1,global.clientId);
		pst.setString(2,global.assignedBranch);
		pst.setString(3,global.destinationBranchId);
		pst.setString(4,global.tarifType);
		pst.setString(5,referenceServiceId);
		pst.setString(6,serviceId);
		ResultSet rs = pst.executeQuery();
		String serviceValue=null;
		if(rs.next()){
			serviceValue = rs.getString(1);
		}
		if(pst!=null)
			pst.close();
		if(rs!=null)
			rs.close();
		return serviceValue;
		
		
	
	}

   //End
	
	
	public String getFormNumber(Connection con,Global global)throws Exception 
	{
		int formNumber=0;
		//AAP//AccessLog.Log("assignedBranch--->"+global.assignedBranch);
		/*
		String query = "select pack_web.fun_ftch_form_no(?) from dual";
		PreparedStatement pst = con.prepareStatement(query);
		pst.setString(1,global.assignedBranch);
		ResultSet rs = pst.executeQuery();
		
		if(rs.next()){
			formNumber = rs.getString(1);
		}
		if(pst!=null)
			pst.close();
		if(rs!=null)
			rs.close();
		*/
		CallableStatement cst=null;
		
		// cst = con.prepareCall("Begin ? := pack_web.fun_ftch_form_no(?); End;");

		/* 
		 * Changed pack_web.fun_ftch_form_no as PACK_WEB.FUN_FTCH_FORM_NO_FE.
		 * for KITS bug id 0070933 posted on 11-29-10 10:56
		 * 
		 */
		//AAP//AccessLog.Log("Calling PACK_WEB.FUN_FTCH_FORM_NO_FE procedure ");
		cst = con.prepareCall("Begin ? := PACK_WEB.FUN_FTCH_FORM_NO_FE(?); End;");
		cst.registerOutParameter(1,Types.INTEGER);
		cst.setString(2,global.assignedBranch);
		cst.executeQuery();
		formNumber = cst.getInt(1);
		//AAP//AccessLog.Log("Form number is======="+formNumber);
		
		if(cst!=null)
			cst.close();
		return "WW"+formNumber;
		
		
	}
	public void insertReferenceRecord(Connection con,
									  HttpSession session,
									  String genGuiaNumber,
									  String reference,
									  java.sql.Timestamp sysDate)throws Exception {
		
		//HttpSession session=req.getSession(false); get the clarification
		Global global =(Global)session.getAttribute("sGlobal");

		String insertQuery=		"INSERT INTO WEB_GUIA_REFR("+
								"GR_GUIA_NO,"+
								"GR_GUIA_TYPE,"+
								"GR_DOCU_TYPE,"+
								"GR_GUIA_REFR,"+
								"CRTD_ON,"+
								"CRTD_BY,"+
								"MDFD_ON,"+
								"MDFD_BY)"+
								"VALUES(?,?,?,?,?,?,?,?)";

			PreparedStatement pst = con.prepareStatement(insertQuery);
			pst.setString(1,genGuiaNumber);
			pst.setString(2,"H");
			pst.setString(3,"Q");
			pst.setString(4,reference);
			pst.setTimestamp(5,sysDate);
			pst.setString(6,global.clientId);
			pst.setTimestamp(7,sysDate);
			pst.setString(8,global.clientId);
			int insertCount = pst.executeUpdate();

			/*//AAP if(insertCount>0)
				AccessLog.Log(""+global.clientId+"WEB GUIA REFR INSERT SCUCESS");
			else
				AccessLog.Log(""+global.clientId+"WEB GUIA REFR INSERT FALILURE");
			*/
			if(pst!=null)
				pst.close();
	}

	public boolean beforeCredit(Connection con,HttpSession session,HttpServletRequest req)
	{
		Global global =(Global)session.getAttribute("sGlobal");
		String query="select ACP_PRCS_STAT from adm_clnt_pend_amt where acp_clnt_id =?";
		PreparedStatement pst;
		ResultSet rs=null;
		try {
			pst = con.prepareStatement(query);
			pst.setString(1,global.clientId);
			rs=pst.executeQuery();
			while(rs.next())
			{
				if(rs.getString(1).equalsIgnoreCase("P"))
				{
					req.setAttribute("erroravailcred","Please Wait ....Credit Limit Calculation is in process");
					//code added by kumaran 14th oct
				return false;
				}	
				
			}
			if(rs!=null)
				rs.close();
			if(pst!=null)
			{
				pst.close();
			}
		} catch (Exception e) {		
			AccessLog.Log(e.getMessage());		
		}	
		return true;
	}
	
	public String getInsuranceViewFlag(Connection con,HttpSession session,HttpServletRequest req) {
		
		String query="select nvl(WC_OMITIR_COBRO_SEGURO,'N') WC_OMITIR_COBRO_SEGURO from web_clnt_mstr where wc_clnt_id =?";
		PreparedStatement pst;
		ResultSet rs=null;
		Global global =(Global)session.getAttribute("sGlobal");
		try {
			pst = con.prepareStatement(query);
			pst.setString(1,global.clientId);
			rs=pst.executeQuery();
			while(rs.next())
			{
				if(rs.getString(1).equalsIgnoreCase("Y"))
				{
					req.setAttribute("NOINSURANCE","SHOULD NOT DISPLAY INSURANCE");
					return "Y";
				}	
				
			}
			if(rs!=null)
				rs.close();
			if(pst!=null)
			{
				pst.close();
			}
		} catch (Exception e) {		
			AccessLog.Log(e.getMessage());		
		}
		return "N";
	}
	
	public String  getDeliveryType(Connection con,HttpSession session) {
		String deliveryType ="";
		Global global =(Global)session.getAttribute("sGlobal");
		try{
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
		} else
			deliveryType="H";
		
		} catch(Exception e) {
			AccessLog.Log("exception "+e);
		}
		return deliveryType;
	}	

	public void checkBranch(Connection con, HttpSession session, String entriga) {
		String deliveryType = "";
		// AAP//AccessLog.Log("AfeterservicesGlobal ");
		try {
			JavWebBookingGeneralForm generalForm = (JavWebBookingGeneralForm) session.getAttribute("webBookinggeneral");
			deliveryType = getDeliveryType(con, session);
			// AAP//AccessLog.Log("After deliveryType "+deliveryType);
			// AAP//AccessLog.Log("AfeterservicesGlobal ");
			String callFunction = "";
			String branchId = "";
			branchId = generalForm.getDestinationsitecode();
			// AAP//AccessLog.Log("Inside site checking"+branchId);
			//AAP02 SE ELIMINÓ CONDICION PORQUE AHORA SE REALIZA EN BASE A LA CONFIGURACION
			/*
			 * if (branchId.equalsIgnoreCase("GDL")) { 
			 * 	if (entriga.equalsIgnoreCase("1")) {
			 * 		generalForm.setDestinationcode("GDL01"); 
			 * 	} else {
			 * 		generalForm.setDestinationcode("GDL02"); }
			 * 
			 * } else {
			 */
			int fun_chk_phy_dc_exist = 0;
			int fun_chk_no_of_br = 0;
			PreparedStatement pst = null;
			CallableStatement cst = null;
			ResultSet rset = null;
			pst = con.prepareStatement("select pack_web.fun_chk_phy_dc_exist(?) from dual");
			pst.setString(1, branchId);
			rset = pst.executeQuery();
			if (rset.next()) {
				fun_chk_phy_dc_exist = rset.getInt(1);
			}
			if (rset != null)
				rset.close();
			if (pst != null)
				pst.close();

			pst = con.prepareStatement("select pack_web.fun_chk_no_of_br(?) from dual");
			pst.setString(1, branchId);
			rset = pst.executeQuery();
			if (rset.next()) {
				fun_chk_no_of_br = rset.getInt(1);
			}
			if (rset != null)
				rset.close();
			if (pst != null)
				pst.close();
			// AAP//AccessLog.Log("deliveryType"+deliveryType);
			if (deliveryType.equalsIgnoreCase("H")) {
				callFunction = "PRO_ASSIGN_DEST_BRNC";
			} else if (deliveryType.equalsIgnoreCase("O")
					&& (fun_chk_phy_dc_exist == 1) && (fun_chk_no_of_br > 1)) {
				callFunction = "PRO_ASSIGN_DEST_BRNC";
			} else {
				callFunction = "DIRECTBRANCH";
			}

			String AM_GETY_CODE = "";
			String AM_GETY_TYPE = "";
			String AM_GETY_LEVL = "";
			AM_GETY_CODE = generalForm.getycode;
			AM_GETY_TYPE = generalForm.getytype;
			AM_GETY_LEVL = generalForm.getylevl;
			// AAP//AccessLog.Log("AM_GETY_CODEAM_GETY_CODE"+AM_GETY_CODE);
			if (callFunction.equalsIgnoreCase("PRO_ASSIGN_DEST_BRNC")) {
				cst = con.prepareCall("{call pack_web.PRO_ASSIGN_DEST_BRNC(?,?,?,?,?,?)}");
				cst.setString(1, AM_GETY_CODE);
				cst.setString(2, AM_GETY_TYPE);
				cst.setString(3, AM_GETY_LEVL);
				cst.registerOutParameter(4, Types.VARCHAR);
				cst.registerOutParameter(5, Types.VARCHAR);
				cst.setString(6, branchId);
				cst.executeQuery();
				String br = cst.getString(4);
				String brname = cst.getString(5);
				// AAP//AccessLog.Log("the branch is"+br);
				// AAP//AccessLog.Log("the branch is"+br);
				generalForm.setDestinationcode(br);
				generalForm.setDestinationbranch(brname);

				if (cst != null) {
					cst.close();
				}
				// result.add(values);
			} else {
				// AAP//AccessLog.Log("Single Branch"+deliveryType);
				String br = "";
				String brname = "";
				pst = con.prepareStatement("select BM_BRNC_ID, BM_BRNC_NAME from sys_brnc_mstr where BM_BRNC_SITE_ID = ? AND BM_FLAG1 = 'G'");
				pst.setString(1, branchId);
				rset = pst.executeQuery();
				if (rset.next()) {
					br = rset.getString(1);
					brname = rset.getString(2);
					generalForm.setDestinationcode(br);
					generalForm.setDestinationbranch(brname);
				}
				// AAP//AccessLog.Log("Single Branch"+br);
				// AAP//AccessLog.Log("Single Branch nmame"+brname);
				if (rset != null) {
					rset.close();
				}
				if (pst != null) {
					pst.close();
				}
			}
			/*
			 * se asigna el tipo de búsqueda de configuracion en base al tipo de
			 * entrega (Solo se valida entrega ocurre)
			 */
			String entrega = "";
			if (entriga.equalsIgnoreCase("1")) {// AAP02
				entrega = "DEST_OCURRE";
			} else {
				if (!branchId.substring(2).equals("70")) {
					entrega = "DEST_EAD";
				}
			}
			
			if (entrega.length() > 0){
				/* se busca configuracion */
				SucursalesConfiguradas suc = new SucursalesConfiguradas();
				String nuevaSucursal = suc.obtieneConfigSucursal(con, "BOK", entrega, branchId);// AAP02

				/*
				 * Si encontró configuración, asigna nueva sucursal. En caso de
				 * no encontrar configuracion, no realiza ninguna modificacion a
				 * la sucursal. se deja la sucursal obtenida desde un principio
				 * y realiza las validaciones correspondientes.
				 */
				if (nuevaSucursal.length() > 0) {
					generalForm.setDestinationcode(nuevaSucursal);
				}
			}			
			// }//AAP02

			session.setAttribute("webBookinggeneral", generalForm);
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
		}
	}

   /****************************************************************************
    * Metodo para obtener bandera de zona extendida y costo. 				   *
    ****************************************************************************///AAP01
   public String validaZonaExtendida(Connection con, HttpSession session){
	   JavWebBookingGeneralForm webBookingGeneralFormData = (JavWebBookingGeneralForm)session.getAttribute("webBookinggeneral");
	   Global global =(Global)session.getAttribute("sGlobal");
		PreparedStatement pst = null;
		ResultSet rs = null;
		String zonaExt ="N";
		
		try {//select fun_get_ze('81130', 'CENTRO') FROM DUAL			
			//AAP//AccessLog.Log("codigo postal destino "+webBookingGeneralFormData.getDestinationzipcode());
			//AAP//AccessLog.Log("colonia destino "+webBookingGeneralFormData.getDestinationcolonia1());
			//AAP//AccessLog.Log("codigo plaza destino "+webBookingGeneralFormData.getDestinationsitecode());
			//AAP//AccessLog.Log("global.clientId "+global.clientId);
			
			pst = con.prepareStatement("select fun_get_ze(?, ?, ?, ?) FROM DUAL");
			
			pst.setString(1,webBookingGeneralFormData.getDestinationzipcode());
			pst.setString(2,webBookingGeneralFormData.getDestinationcolonia1());
			pst.setString(3,webBookingGeneralFormData.getDestinationsitecode());
			pst.setString(4,global.clientId);
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				zonaExt=rs.getString(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
		} finally{
			try {
				if(rs!=null)
					rs.close();
				if(pst!=null)
					pst.close();				
			} catch (Exception e2) {
				AccessLog.Log(e2.getMessage());
			}
		}
		return zonaExt.trim();
	}

   /*************************************************************************************************************
    * Metodo para obtener la informacion del operador logistico para ser almacenada en la generacion de la guia *
    *************************************************************************************************************///AAP01
   public ArrayList obtieneInfOperadorLogistico(Connection con, JavWebBookingServicesForm serForm){

		PreparedStatement pst = null;
		ResultSet rs = null;
		
		ArrayList datos = new ArrayList(2);
		
		try {//select fun_get_ze('81130', 'CENTRO') FROM DUAL
			//AAP//AccessLog.Log("SELECT OM_OL_FLAG, OM_OLREFRUTA FROM SYS_OL_MSTR WHERE OM_OL_ID = ? ");
			//AAP//AccessLog.Log("clave operador logistico "+serForm.getOperadorLogistico());
			pst = con.prepareStatement("SELECT OM_OL_FLAG, OM_OLREFRUTA FROM SYS_OL_MSTR WHERE OM_OL_ID = ?");
			pst.setString(1,serForm.getOperadorLogistico());
			rs = pst.executeQuery();
			
			if(rs.next()){
				//bandera operador logistico
				if (rs.getString(1)==null){
					datos.add("");
				}else{
					datos.add(rs.getString(1));
				}
				
				//ruta de operador logistico
				if (rs.getString(2)==null){
					datos.add("");
				}else{
					datos.add(rs.getString(2));
				}
			}			
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
		} finally {
			try {
				if(rs!=null)
					rs.close();
				if(pst!=null)
					pst.close();				
			} catch (Exception e2) {
				AccessLog.Log(e2.getMessage());
			}
		}
		return datos;
	}
   /****************************************************************************************************************
    * Metodo para obtener la tarifa especial para cliente, en caso de que tenga zona extendida y tarifa configurada*
    ****************************************************************************************************************///AAP03
   public String getTarifaEspecialExt(Connection con, HttpSession session, String referenciaItem, String idItem){
		JavWebBookingGeneralForm webBookingGeneralFormData = (JavWebBookingGeneralForm) session.getAttribute("webBookinggeneral");
		Global global = (Global) session.getAttribute("sGlobal");
		PreparedStatement pst = null;
		ResultSet rs = null;
		String tarifaEspecial = "0";
		//AAP//AccessLog.Log("getTarifaEspecialExt()_query==>SELECT WT_TRIF_AMNT FROM WEB_CLNT_SRVC_TRIF WHERE WT_ORGN_CLNT_ID  = ? AND WT_SRVC_ID  = ? AND WT_REFR_SRVC_ID = ? AND WT_DEST_BRNC_ID = ?");
		//AAP//AccessLog.Log("getTarifaEspecialExt()_global.clientId==>"+global.clientId);		
		//AAP//AccessLog.Log("getTarifaEspecialExt()_idItem==>"+idItem);
		//AAP//AccessLog.Log("getTarifaEspecialExt()_referenciaItem==>"+referenciaItem);
		//AAP//AccessLog.Log("getTarifaEspecialExt()_webBookingGeneralFormData.getDestinationsitecode()==>"+webBookingGeneralFormData.getDestinationsitecode());
		try {					
			pst = con.prepareStatement("SELECT WT_TRIF_AMNT FROM WEB_CLNT_SRVC_TRIF WHERE WT_ORGN_CLNT_ID  = ? AND WT_SRVC_ID  = ? AND WT_REFR_SRVC_ID = ? AND WT_DEST_BRNC_ID = ?");
			pst.setString(1,global.clientId);			
			pst.setString(2,idItem);
			pst.setString(3,referenciaItem);
			pst.setString(4,webBookingGeneralFormData.getDestinationsitecode());
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				//tarifa Especial
				if (rs.getString(1)!=null){
					tarifaEspecial = rs.getString(1);
				}
			}
		} catch (Exception e) {
			AccessLog.Log("getTarifaEspecialExt()_Error:"+e.getMessage());
		}finally{
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pst!=null) {
					pst.close();				
				}
			} catch (Exception e2) {
				AccessLog.Log(e2.getMessage());
			}
		}
		return tarifaEspecial;
	}   
}
	