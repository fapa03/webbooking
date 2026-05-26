/**
 * 
 * Kits-Id			Date				Purpose
 * 
 * 69949	  		26-Mar-2010: 		To include Package type like sobre, paquete, caja.
 * 
 */
package paquetexpress.internal.prepaid.conversion.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.paquetexpress.www.webbooking.Documentacion.ShipTypeSEG;
import logger.AccessLog;
import mx.com.paquetexpress.general.model.dto.BranchDetailDTOResponse;
import bean.AcusesXT;
import bean.Global;
import bean.JavAddressLovRecords;
import bean.Resources;
import bean.SucursalesConfiguradas;
import beanUtil.ConnectDB;
import beanUtil.GetBrnchOcurre;
import paquetexpress.internal.prepaid.action.valueobject.PrepaidValueObject;
import paquetexpress.internal.prepaid.conversion.dao.ConversionDao;
import paquetexpress.internal.prepaid.conversion.form.ConversionForm;
import paquetexpress.internal.prepaidcajas.conversion.action.guiamstr.PrepaidGuiaMstr;

public class ConversionAction extends Action {
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();	
	private Resources resources = new Resources();
	
	@SuppressWarnings("rawtypes")
	public ActionForward perform(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {		
		Connection con = null;
		ConversionForm conversionform=null;
		ConversionDao dao=new ConversionDao();
		PrepaidValueObject pObject=new PrepaidValueObject();
		HttpSession session = request.getSession(false);
		Global global = null;
		try{  
			if(session==null){
				return mapping.findForward("start");
				//return mapping.findForward("nosession");
			}
			con = ConnectDB.getConnection();
			global = (Global)session.getAttribute("sGlobal");
			if(global == null) {
				return mapping.findForward("start");
				//return mapping.findForward("nosession");
			}
			
			conversionform=(ConversionForm)form;
			
			conversionform.setBanCerrar("true");//bandera para validar cuando se cierre la ventana desde "x" de navegador
			session.setAttribute("zoneFocus",String.valueOf(0)); // ADDED FOR ZONE VERIFIED FOCUS
			// Added on 26/03/2010
			ArrayList packTypes=new ArrayList();
			packTypes=dao.fetchPackageType();
			request.setAttribute("packTypeName", packTypes.get(0));
			request.setAttribute("packTypeLabel", packTypes.get(1));
			
			// Added on 14/07/2010
			ArrayList delvryTypes=new ArrayList();
			delvryTypes=dao.fetchDeliveryType();
			request.setAttribute("delvryTypeDesc", delvryTypes.get(0));
			request.setAttribute("delvryTypeCode", delvryTypes.get(1));
			// Added on 14/07/2010	
			
			// Agregar para Lista de Cajas
			// 23/01/2012
			ArrayList totalListCajas=new ArrayList();
			ArrayList listCajasType=new ArrayList();
			ArrayList listCajasValue=new ArrayList();
			String sucursal=(String)session.getAttribute("branchid");
			totalListCajas=dao.fetchFormNo(sucursal); // Obtener las cajas de ESTA SUCURSAL!!!
			listCajasType=(ArrayList)totalListCajas.get(0);
			listCajasValue=(ArrayList)totalListCajas.get(1);
			request.setAttribute("listaCajasType",listCajasType);
			request.setAttribute("listaCajasValue",listCajasValue);
			// 23/01/2012	
			//AccessLog.Log("**************************************conversionform.getCurrentTask() "+conversionform.getCurrentTask());
			if(conversionform.getCurrentTask().equalsIgnoreCase("start")) {
				conversionform.seteMailOrigCheck(false);//AAP03
				conversionform.seteMailOrigText("");//AAP03
				conversionform.seteMailDestCheck(false);//AAP03
				conversionform.seteMailDestText("");//AAP03
				session.removeAttribute("shippingtypes");
				request.removeAttribute("shippingtypes");
				conversionform.setOpcOcurre(false);
				conversionform.setBrnchOcurre("");
				conversionform.setProductDescSat("");
				conversionform.setProductIdSat("");
				conversionform.setModFormnoFlag("N");
				conversionform.setPedinumber(""); 
				conversionform.setCustagent("");
				dao.getCentrosCosto(conversionform, global);
				if (global.getAssignedBranch().contains("70")){
					request.setAttribute("error", "Error: DE MOMENTO NO SE PUEDE DOCUMENTAR PREPAGO CON ORIGEN ZONA PLUS");
					return mapping.findForward("guiaConversionMonitor.do?currentTask=start");
				}
				return mapping.findForward("start");
			} else if(conversionform.getCurrentTask().equalsIgnoreCase("load")) {
				conversionform.setBorderBranch(dao.branchLocationType((String) session.getAttribute("branchid")));//AAP20
				
				conversionform.seteMailOrigCheck(false);//AAP03
				conversionform.seteMailOrigText("");//AAP03
				conversionform.seteMailDestCheck(false);//AAP03
				conversionform.seteMailDestText("");//AAP03
				conversionform.setProductDescSat("");
				conversionform.setProductIdSat("");
				dao.getCentrosCosto(conversionform, global);
				conversionform.setFlagValidRefrClnt(dao.getFlagValidRefrClnt(global));//AAP13
				conversionform.setModFormnoFlag("N");
				conversionform.setPedinumber("");
				conversionform.setCustagent("");
				String clickedItems = request.getParameter("clickedItems");
				//System.out.println("clickedItems "+clickedItems);			
				conversionform.setTrackingNo(clickedItems.trim());
				//System.out.println("session.getLastAccessedTime() "+session.getLastAccessedTime());
				//System.out.println("session.getMaxInactiveInterval() "+session.getMaxInactiveInterval());
				
				
				String msjeVal = dao.verificaRastreo(conversionform.getTrackingNo(), "SHPE", "WBK");
				//AccessLog.Log("msjeVal "+msjeVal);
			    //AccessLog.Log("Obteniendo datos de la guia");
			    if (msjeVal.trim().equals("OK")) {
			    	pObject=dao.getDetail(conversionform.getTrackingNo());
					//AccessLog.Log("Datos obtenidos...");
					if(pObject!=null) { 
						
						/*si la sucursal no es frontera*///AAP20
						if (!conversionform.getBorderBranch().trim().equals("BR")) {//AAP20
							/*Si el rastreo se compro para usarse solo en frontera*///AAP20
							if (pObject.getLocationType().trim().equals("BR")) {//AAP20
								msjeVal = "Rastreo para uso solo en frontera.";//AAP20
								request.setAttribute("error", "Error: " + msjeVal);//AAP20
								return mapping.findForward("start");//AAP20
							}//AAP20
						}//AAP20
						
						//AccessLog.Log("Llenando datos de la guia");
						fillform(conversionform,pObject);
						//AccessLog.Log("Datos llenados");
						if(conversionform.getOrgioncode()!=null) {	
							if(conversionform.getOrgioncode().length()>0) {
								request.setAttribute("origin","originpresent");
							}
						}
						// 23/01/2012
						if (conversionform.getSerieCaja().length()>0) {
							session.setAttribute("serieCaja", conversionform.getSerieCaja());
							request.setAttribute("serieCaja", conversionform.getSerieCaja());
						}
						// 23/01/2012
						session.setAttribute("shippingtypes",pObject.getShippingType());
						request.setAttribute("shippingtypes",pObject.getShippingType());
						
						//con=ConnectDB.getConnection(); 
						if (conversionform.getReLoad().equals("Y")) {
							request.setAttribute("error", "Se ha modificado rastreo con exito");//leyenda temporal por modificacion de rastreo
							conversionform.setReLoad("");
							conversionform.setAceptarNuevosCargos("");
						}	
					/*	pObject=dao.getInsuranceAmount(conversionform.getClientId(),pObject,con,"ACTION");
						if((pObject.getValordeclarado()==null)||(pObject.getValordeclarado().length()<=0))
							request.setAttribute("valornull","null");
						session.setAttribute("insurancetypes",pObject.getInsurancetype());
						session.setAttribute("insurancetypeslabels",pObject.getInsurancetypelabel());
						request.setAttribute("insurancetypes",pObject.getInsurancetype());
						request.setAttribute("insurancetypeslabels",pObject.getInsurancetypelabel());*/
					} else {
						request.setAttribute("error","La guía no está disponible o no está pagada");
						//request.setAttribute("error","Either the guia is not available or is not paid");
					}
				
				
					/*VALIDACION DE REQUERIMIENTO DE ACUSES*///AAP02
					if (dao.obtieneInfRequerimientosACK(conversionform.getClientId(), "ACK-C")) {
						conversionform.setReqAcuse("Y");
					} else {			
						conversionform.setReqAcuse("N");
					}
					
					//Validamos si se tiene requerimientos de acuses XT
					if(dao.obtieneInfRequerimientosACK(conversionform.getClientId(), "ACK-X")) {
						conversionform.setReqAcuseXT("Y");
					}
					else {
						conversionform.setReqAcuseXT("N");
					}
			    } else {
			    	request.setAttribute("error", "Error: " + msjeVal);
					return mapping.findForward("start");
			    }
				
				conversionform.setClientId(global.getClientId());//AAP06 REGISTRO MULTI CAJA MANEJO DE CLIENTE AL QUE ESTA ASIGNADO EL USUARIO DEL LOGIN.
				conversionform.setClientName(global.getClientName());//AAP06 REGISTRO MULTI CAJA
				dao.getDirCentrosCosto(conversionform, session);
				conversionform.setAssignedBranch(global.getAssignedBranch());
			} else if(conversionform.getCurrentTask().equalsIgnoreCase("cambioCCosto")) {
				dao.getDirCentrosCosto(conversionform, session);		
				conversionform.setAssignedBranch(global.getAssignedBranch());				
			} else if(conversionform.getCurrentTask().equalsIgnoreCase("zoneextendedverify")) {
				/*validacion para uso de rastreo entre fronteras*///AAP20
				if (conversionform.getLocationType().trim().equals("BR")) { //AAP20
					String isDestBorderbranch = dao.branchLocationType(conversionform.getDestBranch());//AAP20
					boolean success = false;//AAP20
					if (conversionform.getBorderBranch().trim().equals("BR") && isDestBorderbranch.trim().equals("BR")) {//AAP20
						success = true;//AAP20
					}//AAP20
					
					/*sucursales no fronterizas*///AAP20
					if (!success) {//AAP20
						request.setAttribute("error","Rastreo para uso solo en frontera.");//AAP20				
						conversionform.setGuiaHasErrors("S");//AAP20
						conversionform.setGuiaErrorType("Rastreo para uso solo en frontera.");//AAP20				
						return mapping.findForward("start");//AAP20
					}//AAP20
				}//AAP20
				
				//String horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestSite()+"01", "OCU", conversionform.getDest_am_gety_code());
				//String horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestSite()+"01", "EAD", conversionform.getDest_am_gety_code());
				
				/*LINEAS COMENTADAS PARA FUNCION DE NUEVA PROMESA COMERCIAL*/
				String tipoEnvio = "AEREO";
				if (conversionform.getShipType().equals("ST") || conversionform.getShipType().equals("TR")) {
					tipoEnvio = "TERRESTRE";
				}
			    String shipTypeSEGChange="", shipTypeSEGDescChange="", shipTypeSipWebChange="", shipType="", shipTypeSEG="", shipTypeSEGDesc="";
			    ShipTypeSEG seg = new ShipTypeSEG();
			    if(dao.validateIsTypeSEG(conversionform.getShipType())) {
				    int i = 0;
				    Object[] x = findServiceSEG(conversionform);
				    i= (Integer) x[0];
				    shipType=x[1].toString();
				    shipTypeSEG = conversionform.getShippingTypeSEG().get(i).getShipTypeSEGSrvc();
				    shipTypeSEGDesc = conversionform.getShippingTypeSEG().get(i).getShipTypeSEGSrvcDesc();
				    seg =  getNextServiceSEG(dao, shipType, false, session, conversionform);
				    shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
				    shipTypeSEGDescChange= seg.getShipTypeSEGSrvcDesc().toLowerCase();
				    shipTypeSipWebChange= seg.getShipTypeSEGSrvc();
				    tipoEnvio=shipTypeSEG;
				}

				String horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(), tipoEnvio, global.getClientId());
				String horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(), tipoEnvio, global.getClientId());		

				conversionform.setHorasEntregaOcu(horaOcu);
				conversionform.setHorasEntregaEad(horaEad);
				String guideHasExtended=conversionform.getExt();
				boolean zoneextendedcheck = dao.isextendedzone(conversionform);//AAP02
				if (zoneextendedcheck) {
				   conversionform.setIsExtendedZone("S");
				   if (guideHasExtended.indexOf("S")>-1) {
					   if (conversionform.getOperadorLogistico().trim().length()==0) {					   	
							request.setAttribute("error","Dirección destino sin operador logistico configurado. Favor de revisar con el área correspondiente para su corrección");
							conversionform.setDestcolonia1("");
							conversionform.setDestcolonia2("");
							conversionform.setDest1("");
							conversionform.setDest2("");
							conversionform.setDesttelefono("");
							conversionform.setDestcode("");
							conversionform.setDestrfc("");
							//conversionform.setDestCelular("");
							//conversionform.setDestCorreo("");
							return mapping.findForward("start");
					   } else {
						   conversionform.setGuiaHasErrors("N");
						   return mapping.findForward("start");   
					   }			   
				   } else {
						request.setAttribute("error","El cliente pertenece a una zona extendida y no se ha contratado el servicio");
						//AccessLog.Log("Error in Extended Zone selection");
						conversionform.setGuiaHasErrors("S");
						conversionform.setGuiaErrorType("ERROR EN ZONA EXTENDIDA");
						//session.setAttribute("zoneFocus",String.valueOf(0));
						return mapping.findForward("start");
				   }
				} else {
					conversionform.setIsExtendedZone("N");
					conversionform.setGuiaHasErrors("N");
					conversionform.setGuiaErrorType("");			
				}
				conversionform.setNeededConfirmationService2D(false);
				conversionform.setConfirmationService2D(false);
				if(dao.validateIsTypeSEG(conversionform.getShipType())) {
				    String isInsideTimeCourt=dao.isInsideTimeCourtSEG((String) session.getAttribute("branchid"),conversionform.getDestBranch(), shipTypeSEG, conversionform.getClientId(), conversionform.getOrgioncode());
				    //System.out.println("isInsideTimeCourt "+isInsideTimeCourt);
				    if (isInsideTimeCourt.equalsIgnoreCase("WITHOUTSERVICE")) {
						seg =  getNextServiceSEG(dao, shipType, true, session, conversionform);
						shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
						shipTypeSEGDescChange= seg.getShipTypeSEGSrvcDesc().toLowerCase();
						shipTypeSipWebChange= seg.getShipTypeSEGSrvc();
						horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, global.getClientId());
						horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, global.getClientId());
						conversionform.setHorasEntregaOcu(horaOcu);
						conversionform.setHorasEntregaEad(horaEad);
						request.setAttribute("error", "No tenemos disponible el servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" entre "+ (String) session.getAttribute("branchid")+ " y "+ conversionform.getDestBranch()+ " pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\".");
						// AccessLog.Log("Error in Extended Zone selection");
						conversionform.setGuiaHasErrors("N");
						conversionform.setGuiaErrorType("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
			 			conversionform.setNeededConfirmationService2D(true);
						conversionform.setConfirmationService2D(false);
						conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
						conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
						conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
						conversionform.setMsjConfirmationService("No tenemos disponible el servicio  \"" +shipTypeSEGDesc.toLowerCase()+" \" entre "+ (String) session.getAttribute("branchid")+ " y "+ conversionform.getDestBranch()+ " pero podemos ofrecer el servicio "+shipTypeSEGDescChange+", "+" ¿Desea confirmar el servicio?");
						conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE NO PODEMOS GARANTIZAR EL SERVICIO "+ shipTypeSEGDesc.toUpperCase() + " ENTRE ESTAS SUCURSALES");
						return mapping.findForward("start");
				    } else if (isInsideTimeCourt.equalsIgnoreCase("OUT")) {
						horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, global.getClientId());
						horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, global.getClientId());
						conversionform.setHorasEntregaOcu(horaOcu);
						conversionform.setHorasEntregaEad(horaEad);
						request.setAttribute("error", "No se puede garantizar el servicio express del servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" ya que se encuentra fuera del horario de corte pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\".");
						// AccessLog.Log("Error in Extended Zone selection");
						conversionform.setGuiaHasErrors("N");
						conversionform.setGuiaErrorType("El corte de este servicio ya fue realizado");
						conversionform.setNeededConfirmationService2D(true);
						conversionform.setConfirmationService2D(false);
						conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
						conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
						conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
						conversionform.setMsjConfirmationService("El tiempo para garantizar el servicio "+shipTypeSEGDesc.toLowerCase() + " ya paso pero podemos ofrecer el servicio " + shipTypeSEGDescChange + " ¿Desea confirmar el servicio?");
						conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE EL TIEMPO PARA GARANTIZAR EL SERVICIO "+ shipTypeSEGDesc.toUpperCase() + " YA PASO");
				    }
				    if(!conversionform.getGuiaHasErrors().equalsIgnoreCase("S") && !conversionform.getDeliveryType().isEmpty() && !conversionform.getDeliveryType().equalsIgnoreCase("O")){
					    boolean cobertura=dao.getCoverageSEG(conversionform.getDest_am_gety_code(), conversionform.getDestclave(), conversionform.getDestcode(), shipTypeSEG);
					    if(!cobertura) {
							seg =  getNextServiceSEG(dao, shipType, true, session, conversionform);
							shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
							shipTypeSEGDescChange= seg.getShipTypeSEGSrvcDesc().toLowerCase();
							shipTypeSipWebChange= seg.getShipTypeSEGSrvc();
		        			request.setAttribute("error","No tenemos disponible este servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" para este domicilio pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\".");
		        			//AccessLog.Log("Error in Extended Zone selection");
		        			conversionform.setGuiaHasErrors("N");
		        			conversionform.setGuiaErrorType("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
		        			conversionform.setNeededConfirmationService2D(true);
		        			conversionform.setConfirmationService2D(false);
		        			conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
							conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
							conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
							conversionform.setMsjConfirmationService("No tenemos disponible este servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" para este domicilio pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\", "+" ¿Desea confirmar el servicio?");
							conversionform.setAlertNOConfirmationService("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
		        			return mapping.findForward("start");
		        		}
					}
				}
			} else if(conversionform.getCurrentTask().equalsIgnoreCase("guidepreview")) {
				 if(session.getAttribute("status")!=null)
						session.removeAttribute("status");
				    PrepaidGuiaMstr guiaOld=new PrepaidGuiaMstr();					
					String credit = dao.creditStatus(conversionform.getClientId(),session);
					conversionform.setClientHasLocalCredit(credit);
					guiaOld=dao.getPrepaidGuiaMaster(conversionform.getTrackingNo());
					
					//String taxBrnc = guiaOld.getBgm_prep_brnc_id();//AAP20
					String taxBrnc = (String)session.getAttribute("branchid");//AAP20
					//si la sucursal de modificacion es frontera
					if (conversionform.getBorderBranch().trim().equals("BR")) {//AAP20
						//si la venta no es fronteriza, el calculo de iva tiene que ser al 16%
						if (!conversionform.getBorderBranch().trim().equals(//AAP20
								guiaOld.getLocationType().trim()//AAP20
								) ) {//AAP20
							//Se obtiene sucursal referencia con iva al 16
							taxBrnc = dao.getTaxesBranch("IN");//AAP20
						}//AAP20
					}//AAP20
					
					
					//String impuestos[] = dao.getTaxes(session.getAttribute("branchid").toString().substring(0,3));//AAP20
					String impuestos[] = dao.getTaxes(taxBrnc.substring(0, 3));//AAP20
					if(session.getAttribute("status")!=null) {
						request.setAttribute("status","ena");
						
						if (conversionform.getPayMode().isEmpty()) {
							conversionform.setPayMode("PAID");
						}
					} else {
						conversionform.setPayMode("ToPay");	
					}
					
					String clientType = dao.clientType(guiaOld.getBgm_clint_id());
					conversionform.setClientType(clientType);
					
					if (conversionform.getExt().charAt(0)=='S') {
						conversionform.setExtCheck("on");
						//conversionform.setPayMode("ToPay");
						conversionform.setNuevoValorExt(dao.getTarifaEXT());
						conversionform.setValorIVA(impuestos[0]);
						
						//Se crea objeto PrepaidGuiaMstr con importes de la nueva guia
						PrepaidGuiaMstr nuevaGuia = new PrepaidGuiaMstr();
						nuevaGuia.setBgm_gh_amt(0);
						nuevaGuia.setBgm_ack_amt(0);
						nuevaGuia.setBgm_insur_amt(0);
						nuevaGuia.setBgm_ead_amt(0);
						nuevaGuia.setBgm_rad_amt(0);
						nuevaGuia.setBgm_ext_amt(dao.getTarifaEXT().isEmpty() ? 0 : Double.valueOf(dao.getTarifaEXT()));
						nuevaGuia.setCompanyId(guiaOld.getCompanyId());
						
						//Se calcula el total de retenci�n tal cual se va a guardar en bd
						conversionform.setValorRetAmount(dao.calcRetAmount(nuevaGuia, conversionform.getPayMode(), conversionform.getClientId(), taxBrnc));
						
					} else
						conversionform.setExtCheck("off");
					return mapping.findForward("start");
			} else if(conversionform.getCurrentTask().equalsIgnoreCase("guideconfirm")) {
				String facturaMod = dao.valGuiaModificada(conversionform.getTrackingNo());
				if (facturaMod.length()>0) {
					request.setAttribute("error", "El rastreo ya esta modificado, factura: "+facturaMod);
					//AccessLog.Log(result);
					return mapping.findForward("start");
				}
				 // Genera el Nuevo PP_BOK_MSTR
				// Asigna la guia actual al nuevo PPG_BOK_MSTR
				// Agregalos al IPH y FIN_INVC
				// Regresa el Valor De La Factura
				// Desactiva los botones de la forma
				String result="";	
				try {
				// Inserta Nuevo Registro en BOK_GUIDE_MSTR con los datos actuales
				PrepaidGuiaMstr guiaOld = new PrepaidGuiaMstr();
				guiaOld=dao.getPrepaidGuiaMaster(conversionform.getTrackingNo());
				
				//String taxBrnc = guiaOld.getBgm_prep_brnc_id();//AAP20
				String taxBrnc = (String)session.getAttribute("branchid");//AAP20
				//si la sucursal de modificacion es frontera
				if (conversionform.getBorderBranch().trim().equals("BR")) {//AAP20
					//si la venta no es fronteriza, el calculo de iva tiene que ser al 16%
					if (!conversionform.getBorderBranch().trim().equals(//AAP20
							guiaOld.getLocationType().trim()//AAP20
							) ) {//AAP20
						//Se obtiene sucursal referencia con iva al 16
						taxBrnc = dao.getTaxesBranch(guiaOld.getLocationType());//AAP20
					}//AAP20
				}//AAP20
				
				guiaOld.setBgm_gh_amt(0);
				guiaOld.setBgm_ead_amt(0);
				guiaOld.setBgm_rad_amt(0);
				guiaOld.setBgm_ack_service("");
				guiaOld.setBgm_ack_amt(0);
				guiaOld.setBgm_ack_tax(0);
				guiaOld.setBgm_ext_service(conversionform.getExt());
				guiaOld.setBgm_ext_amt(Double.parseDouble(dao.getTarifaEXT()));
				guiaOld.setBgm_prep_brnc_id((String)session.getAttribute("branchid"));
				
				
				// Verifica si la factura es de credito y si el cliente tiene credito disponible
		        String tipoPago=conversionform.getPayMode();
		        double creditoDisponible=0;
		        if (tipoPago.equals("PAID")) {
		 	        //creditoDisponible=dao.clientCredit(conversionform.getClientId(), session);
		 	       creditoDisponible=dao.clientCredit(conversionform.getClientId());
			        //AccessLog.Log("Tipo de Pago indicado:" + tipoPago);
			        if (creditoDisponible<=0) {
						request.setAttribute("error", "El cliente no tiene credito disponible" );
						//AccessLog.Log("Credito disponible indicado:" + creditoDisponible);
						return mapping.findForward("start");
			        }
		        }
				
				if (conversionform.getPayMode().indexOf("PAID")>-1)
					guiaOld.setBgm_valid_flg("A");
				else
					guiaOld.setBgm_valid_flg("I");
		
		        result=dao.reInsertRecord(guiaOld, session, conversionform.getTrackingNo(),"", conversionform.getFiscaladdresscode(), conversionform.getOrgioncode());
				if((result!=null)&&(result.length()>0)) {
					request.setAttribute("error", result + "   Error de ReInsercion" );
					//AccessLog.Log(result);
					return mapping.findForward("start");
				}
				
				String formNoStr = "";
				//solo cuando sea de contado 'ToPay' es cuando se obtendr� consecutivo de factura.
				if (!conversionform.getPayMode().equals("PAID")) {//AAPXX
					int formNo = dao.fetchFormNumber(session);
					formNoStr = Integer.toString(formNo);
				}
				PrepaidGuiaMstr guiaNew=new PrepaidGuiaMstr();
				guiaNew=dao.getPrepaidGuiaMaster(conversionform.getTrackingNo());
				// Modificaste esto el dia 18/Nov/2011 11:19 para ver si esto era lo que estaba haciendo el acto de miccion fuera del lugar indicado
				 result = dao.reFactura(guiaNew, session, request, conversionform.getTrackingNo(), conversionform.getPayMode(),formNoStr,guiaNew.getBgm_ref_no(), taxBrnc/*AAP20*/);
				} catch (Exception e) {
					request.setAttribute("error", "Error de Procedimiento:" + e.getMessage());
					return mapping.findForward("success");
				}
				// insertModifiedRecord genera un book_guide_mstr nuevo, y cambia la 
				// referencia de la guia modificada a este nuevo registro
				
				// Tambien se genera la factura y se congela el registro hasta que se pago la guia creada.
				// NOTA - El valor de la guia nueva es basandose en las tarifas comerciales actuales
				// Es decir, NO SE OTORGA DESCUENTO.
				// P.Ej. Si por la guia ya se pago X cantidad, esta se le resta al valor facturable nuevo.
				if(result.indexOf("unique constraint")>-1) {
					  request.setAttribute("error","Indice Duplicado, El Numero de Forma ya existe");
					  return mapping.findForward("start");
				}
				//request.setAttribute("error", "Se ha generado la factura:" + result  );
				if (conversionform.getPayMode().equals("PAID")) {//AAPXX
					request.setAttribute("error", "Se ha modificado rastreo con exito");
				} else {					
					request.setAttribute("error", "Se ha generado la factura : " + result);
					//System.out.println("result "+result);
					//System.out.println("result.substring(0, 2) "+result.substring(0, 2));
					if (result.substring(0, 2).equals("AD")) {
						Date date = new Date();				
						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
						//System.out.println("Fecha: "+dateFormat.format(date));
										
						String sDay = dateFormat.format(date).toString().substring(0, 2);
						String sMonth = dateFormat.format(date).toString().substring(3, 5);
						String sYear = dateFormat.format(date).toString().substring(6);
						if (sDay.length() == 1) {
							sDay = "0"+sDay;
						}
						if (sMonth.length() == 1) {
							sMonth = "0"+sMonth;
						}
						
						String params = "seleccionaDia="+sDay+"&seleccionaMes="+sMonth+"&seleccionaAnyo="+sYear+"&fact="+sucursal.substring(0, 3)+result;
						//System.out.println("params "+params);
						//request.setAttribute("factura", params);
						conversionform.setFacturaMod(params);
						
					}
				}			
				
				conversionform.setReLoad("Y");
				request.removeAttribute("shippingtypes");
				return mapping.findForward("start");
		
			} else if(conversionform.getCurrentTask().equalsIgnoreCase("insurance")) {			
				fillValueObject(pObject,conversionform);
				con = ConnectDB.getConnection();
				dao.calculateInsuranceAmount(con,pObject);
			} else if(conversionform.getCurrentTask().equalsIgnoreCase("zoneverify")) {
				
				boolean zonecheck=dao.zoneverify(conversionform.getTrackingNo(),(String)session.getAttribute("branchid"),conversionform.getDestSite()+"01",conversionform.getDestSite());
				
				//AccessLog.Log("zone"+zonecheck);
				if(!zonecheck) {
					request.setAttribute("error","Error en la selección de zona y destino. Si no se indica ZONA la guia pertenece a una CAJA");
					//AccessLog.Log("Error in Zone selection");
					session.setAttribute("zoneFocus",String.valueOf(0)); // ADDED FOR ZONE VERIFIED FOCUS
				} else {
					//AccessLog.Log("No error in Zone selection");
					conversionform.setCurrentTask("zoneverified");
					session.setAttribute("zoneFocus",String.valueOf(1)); // ADDED FOR ZONE VERIFIED FOCUS
				}
				//PENDIENTE REVISAR VARIABLE
				//String ship=(String)session.getAttribute("shippingtypes");
				//request.setAttribute("shippingtypes",ship);
				
				
				/*ArrayList listins=(ArrayList)session.getAttribute("insurancetypes");
				ArrayList listinslabel=(ArrayList)session.getAttribute("insurancetypeslabels");
				request.setAttribute("insurancetypes",listins);
				request.setAttribute("insurancetypeslabels",listinslabel);*/
			
			} else if(conversionform.getCurrentTask().equalsIgnoreCase("convert")) {
				
				if (!conversionform.getPedinumber().isEmpty()) {
					String valResult = validaPedimento(con, conversionform.getPedinumber()); 
					if (!valResult.equalsIgnoreCase("OK")){
						conversionform.setMsgInfo(valResult);
						conversionform.setLastWrongPediNum(conversionform.getPedinumber());
						conversionform.setPedinumber("error");
						conversionform.setCustagent("");
						return mapping.findForward("start");//AAP20
					}else {
						conversionform.setLastWrongPediNum("");
					}
				}
				
				/*validacion para uso de rastreo entre fronteras*///AAP20
				if (conversionform.getLocationType().trim().equals("BR")) { //AAP20
					String isDestBorderbranch = dao.branchLocationType(conversionform.getDestBranch());//AAP20
					boolean success = false;//AAP20
					if (conversionform.getBorderBranch().trim().equals("BR") && isDestBorderbranch.trim().equals("BR")) {//AAP20
						success = true;//AAP20
					}//AAP20
					
					/*sucursales no fronterizas*///AAP20
					if (!success) {//AAP20
						request.setAttribute("error","Rastreo para uso solo en frontera.");//AAP20
						return mapping.findForward("start");//AAP20
					}//AAP20
				}//AAP20
				
				if(conversionform.getDeliveryType().equalsIgnoreCase("H")) {
					boolean delvryCheck = getEadService(dao, pObject, conversionform);
					boolean chkEADAvl = getChkEadAvl(dao, pObject, conversionform);
					if(!delvryCheck) {
						request.setAttribute("error","El servicio de EAD no está disponible para la dirección destino seleccionada");
						//AccessLog.Log("EAD Service is not available for the destination");
						return mapping.findForward("start");
					} else if(!chkEADAvl) {
						request.setAttribute("error","LA FACILIDAD (SERVICIO???) DE ENTREGA A DOMICILIO NO ESTA DISPONIBLE PARA ESTE CLIENTE");
						//AccessLog.Log("EAD Service is not available for the client");
						return mapping.findForward("start");
					} else {
						//AccessLog.Log("No error in EAD Service");
						conversionform.setCurrentTask("eadverified");
					}
				}

				//Validaciones para servicio express garantizado
				if(dao.validateIsTypeSEG(conversionform.getShipType())) {
					if(!conversionform.isNeededConfirmationService2D()) {
					    String shipTypeSEGChange="", shipTypeSEGDescChange="", shipTypeSipWebChange="", shipType="", shipTypeSEG="", shipTypeSEGDesc="";
					    ShipTypeSEG seg = new ShipTypeSEG();
						int i = 0;
						Object[] x = findServiceSEG(conversionform);
						i = (Integer) x[0];
						shipType = x[1].toString();
						shipTypeSEG = conversionform.getShippingTypeSEG().get(i).getShipTypeSEGSrvc();
						shipTypeSEGDesc = conversionform.getShippingTypeSEG().get(i).getShipTypeSEGSrvcDesc();
						seg = getNextServiceSEG(dao,shipType, false, session, conversionform);
						shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
						shipTypeSEGDescChange = seg.getShipTypeSEGSrvcDesc().toLowerCase();
						shipTypeSipWebChange = seg.getShipTypeSEGSrvc();
						String isInsideTimeCourt=dao.isInsideTimeCourtSEG((String) session.getAttribute("branchid"),conversionform.getDestBranch(), shipTypeSEG, conversionform.getClientId(), conversionform.getOrgioncode());
					    //System.out.println("isInsideTimeCourt "+isInsideTimeCourt);
					    String horaOcu, horaEad;
					    if (isInsideTimeCourt.equalsIgnoreCase("WITHOUTSERVICE")) {
							seg =  getNextServiceSEG(dao, shipType, true, session, conversionform);
							shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
							shipTypeSEGDescChange= seg.getShipTypeSEGSrvcDesc().toLowerCase();
							shipTypeSipWebChange= seg.getShipTypeSEGSrvc();
							horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, global.getClientId());
							horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, global.getClientId());
							conversionform.setHorasEntregaOcu(horaOcu);
							conversionform.setHorasEntregaEad(horaEad);
							request.setAttribute("error", "No tenemos disponible el servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" entre "+ (String) session.getAttribute("branchid")+ " y "+ conversionform.getDestBranch()+ " pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\".");
							//AccessLog.Log("Error in Extended Zone selection");
							conversionform.setGuiaHasErrors("N");
							conversionform.setGuiaErrorType("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
							conversionform.setNeededConfirmationService2D(true);
							conversionform.setConfirmationService2D(false);
							conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
							conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
							conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
							conversionform.setMsjConfirmationService("No tenemos disponible el servicio  \"" +shipTypeSEGDesc.toLowerCase()+" \" entre "+ (String) session.getAttribute("branchid")+ " y "+ conversionform.getDestBranch()+ " pero podemos ofrecer el servicio "+shipTypeSEGDescChange+", "+" ¿Desea confirmar el servicio?");
							conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE NO PODEMOS GARANTIZAR EL SERVICIO "+ shipTypeSEGDesc.toUpperCase() + " ENTRE ESTAS SUCURSALES");
							return mapping.findForward("start");
					    } else if (isInsideTimeCourt.equalsIgnoreCase("OUT")) {
							horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, global.getClientId());
							horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, global.getClientId());
							conversionform.setHorasEntregaOcu(horaOcu);
							conversionform.setHorasEntregaEad(horaEad);
							request.setAttribute("error", "No se puede garantizar el servicio express del servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" ya que se encuentra fuera del horario de corte pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\".");
							// AccessLog.Log("Error in Extended Zone selection");
							conversionform.setGuiaHasErrors("N");
							conversionform.setGuiaErrorType("El corte de este servicio ya fue realizado");
							conversionform.setNeededConfirmationService2D(true);
							conversionform.setConfirmationService2D(false);
							conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
							conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
							conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
							conversionform.setMsjConfirmationService("El tiempo para garantizar el servicio "+shipTypeSEGDesc.toLowerCase() + " ya paso pero podemos ofrecer el servicio " + shipTypeSEGDescChange + " ¿Desea confirmar el servicio?");
							conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE EL TIEMPO PARA GARANTIZAR EL SERVICIO "+ shipTypeSEGDesc.toUpperCase() + " YA PASO");
							return mapping.findForward("start");
					    }
					    if(!conversionform.getGuiaHasErrors().equalsIgnoreCase("S") && !conversionform.getDeliveryType().isEmpty() && !conversionform.getDeliveryType().equalsIgnoreCase("O")){
						    boolean cobertura=dao.getCoverageSEG(conversionform.getDest_am_gety_code(), conversionform.getDestclave(), conversionform.getDestcode(), shipTypeSEG);
						    if(!cobertura) {
								seg =  getNextServiceSEG(dao, shipType, true, session, conversionform);
								shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
								shipTypeSEGDescChange= seg.getShipTypeSEGSrvcDesc().toLowerCase();
								shipTypeSipWebChange= seg.getShipTypeSEGSrvc();
			        			request.setAttribute("error","No tenemos disponible este servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" para este domicilio pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\".");
			        			//AccessLog.Log("Error in Extended Zone selection");
			        			conversionform.setGuiaHasErrors("N");
			        			conversionform.setGuiaErrorType("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
			        			conversionform.setNeededConfirmationService2D(true);
			        			conversionform.setConfirmationService2D(false);
			        			conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
								conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
								conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
								conversionform.setMsjConfirmationService("No tenemos disponible este servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" para este domicilio pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\", "+" ¿Desea confirmar el servicio?");
								conversionform.setAlertNOConfirmationService("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
			        			return mapping.findForward("start");
			        		}
						}
					}
				}
				
				pObject=dao.getDetail(conversionform.getTrackingNo());
				if(pObject==null) {
					request.setAttribute("error","La guía no está disponible o no está pagada");
					//request.setAttribute("error","Either the guia is not available or is not paid");
					return mapping.findForward("start");
				}
				//AccessLog.Log("Before conversion*********************"+pObject.getDestclave());
				//AccessLog.Log("Before conversion*********************"+pObject.getDestcode());
				fillValueObject(pObject,conversionform);
				
				String result = dao.convert(pObject,
						conversionform.getTrackingNo(), session, request,
						"PP", 
						conversionform);// AAP02
				if ((result != null) && (result.length() > 0)) {
					if (result.indexOf("Conversion Not Allowed") > -1)
						request.setAttribute("error", "Documentación no permitida");
					// request.setAttribute("error","Conversion Not Allowed");
					else if (result.indexOf("Error in Zone") > -1)
						request.setAttribute("error", result);
					// request.setAttribute("error","Error en la selecci�n de zona y destino");
					// request.setAttribute("error","Error in Zone Selection And Destination Selection");
					else
						request.setAttribute("error", result);
					// request.setAttribute("error","Falla de inserci�n");
					// request.setAttribute("error","Error in Conversion");
					//AccessLog.Log(result);
				} else {
					boolean insrtEmail = false;
					if (conversionform.geteMailOrigText().length() > 0
							|| conversionform.geteMailDestText().length() > 0) {// AAP03
						insrtEmail = true;
					} /*
					 * else if (aform.getSendeMailDestBD().equals("Y") &&
					 * !aform.geteMailDestBD().equals("")) {//AAP03
					 * aform.seteMailDestText(aform.geteMailDestBD());//AAP03
					 * insrtEmail = true;//AAP03 }//AAP03
					 */

					if (insrtEmail) {// AAP03
						//dao.insertWebCntrlMail(conversionform, conversionform.getTrackingNo(), session.getAttribute("user").toString());// AAP03
						dao.insertWebCntrlMail(conversionform, conversionform.getTrackingNo(), global.getOrigenUserClave());// AAP03
					}// AAP03
					
					/*inserta guia y centro de costo seleccionado*/
					dao.insertBookGuiaExtra(conversionform, conversionform.getTrackingNo(), global);//AAP05
					
					if (conversionform.getPedinumber().isEmpty() && !conversionform.getLastWrongPediNum().isEmpty()) {
						dao.insertBokHistErrorPediNumb(con, conversionform.getLastWrongPediNum(), conversionform.getTrackingNo());
					}
					
					dao.insertBookGuiaRel(conversionform, conversionform.getTrackingNo(), global);//AAP06 REGISTRO MULTI CAJA
					
					/* Actualiza bok guia head, desde el srvc */
					dao.updateFinalAmnt(con, conversionform.getTrackingNo());
					
					String mainRefr = "";
					int count = 1;
					if (conversionform.getListReferences() != null && conversionform.getListReferences().length() > 0) {
						StringTokenizer stRefers = null;
						stRefers = new StringTokenizer(conversionform .getListReferences().trim().toUpperCase(),"|");
						
						while (stRefers.hasMoreElements()) {
							if (count==1) {
								mainRefr = "Y";
							} else {
								mainRefr = "N";
							}
							dao.insertwebGuiaRefr(stRefers.nextToken(), conversionform.getTrackingNo(), global, mainRefr);
							count++;
						}
					} else {
						
						//Insercion de referencia en WEB_GUIA_REFR
						if (conversionform.getReference().length()>0) {
							dao.insertwebGuiaRefr(conversionform.getReference(), conversionform.getTrackingNo(), global,"Y");//AAP07
						}
					}
					
					request.setAttribute("error", "Gu�a documentada con �xito....");
					// request.setAttribute("error","The Guia Converted SuccessFully....");
					// pObject=null;
					//AccessLog.Log("inside download page");					
					
					/*generacion de archivo, BRPETIQ.ETI*/
					//Valido si la guia tiene acuse XT
					String rastreo_XT ="";
					AcusesXT xt = new AcusesXT();
					//System.out.println("Clase: ConversionAction\nLinea: 441\nAcuse: "+conversionform.getAcuseSel());
					if(conversionform.getAcuseSel().equals("ACK-X")) {
						global.setAcuseXT(true);
						rastreo_XT = xt.genAcuseXT(conversionform.getTrackingNo());
					}else {global.setAcuseXT(false);}
					/*insercion de etiquetas en tabla para generacion de PDF*/
					dao.insertCtrlLabelPrn(conversionform.getTrackingNo());		
					//Generar cadena de impresi�n
					String cadena = dao.getCadenaImpresion(request, conversionform.getTrackingNo(), global.isAcuseXT(),rastreo_XT);//AAP04 - mandamos por parametro si es acuse XT
					if(conversionform.getAcuseSel().equals("ACK-X")) {
						xt.desactivarGuiaDeRetorno(conversionform.getTrackingNo());//Desactivamos la guia de retorno
						xt.insertForPDFXT(rastreo_XT);//Generamos el PDF para la guia de retorno
					}
					
					String getCartaPorte = ConnectDB.getCartaPorteExt();
					if (request.getRemoteAddr().trim().substring(0,3).equals("0:0") || request.getRemoteAddr().trim().substring(0,8).equals("192.168.")) {
						getCartaPorte =ConnectDB.getCartaPorteInt();
					}
					request.setAttribute("trackingNoGenRet",conversionform.getAcuseSel().equals("ACK-X") == true ? rastreo_XT : "");
					request.setAttribute("trackingNoGen", conversionform.getTrackingNo());					
					request.setAttribute("getCartaPorte", getCartaPorte);
					request.setAttribute("cadena", cadena);
					//System.out.println("ConnectDB.getCartaPorte() "+ConnectDB.getCartaPorte());
					return mapping.findForward("downloadpage");
				}
			} else if (conversionform.getCurrentTask().equalsIgnoreCase("delvryverify")) {// Added on 14/07/2010 - For kitsId :70454 Starts
				if (conversionform.getDeliveryType().equalsIgnoreCase("H")) {
					boolean delvryCheck = getEadService(dao, pObject, conversionform);
					boolean chkEADAvl = getChkEadAvl(dao, pObject, conversionform);
					if (!delvryCheck) {
						request.setAttribute("error", "El servicio de EAD no est� disponible para la direcci�n destino seleccionada");
						//AccessLog.Log("EAD Service is not available");
					} else if (!chkEADAvl) {
						request.setAttribute("error", "LA FACILIDAD (SERVICIO???) DE ENTREGA A DOMICILIO NO ESTA DISPONIBLE PARA ESTE CLIENTE");
						//AccessLog.Log("EAD Service is not available for the client");
						return mapping.findForward("start");
					} else if(conversionform.getShippingTypeSEG() != null) {
					    String shipTypeSEGChange, shipTypeSEGDescChange, shipTypeSipWebChange;
					    Object[] x = findServiceSEG(conversionform);
					    int i= (Integer) x[0];
					    String shipType=x[1].toString();
					    String shipTypeSEG = conversionform.getShippingTypeSEG().get(i).getShipTypeSEGSrvc();
					    String shipTypeSEGDesc = conversionform.getShippingTypeSEG().get(i).getShipTypeSEGSrvcDesc();
					    String isInsideTimeCourt=dao.isInsideTimeCourtSEG((String) session.getAttribute("branchid"),conversionform.getDestBranch(), shipTypeSEG, conversionform.getClientId(), conversionform.getOrgioncode());
					    ShipTypeSEG seg = new ShipTypeSEG();
						seg = getNextServiceSEG(dao,shipType, false, session, conversionform);
						shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
						shipTypeSEGDescChange = seg.getShipTypeSEGSrvcDesc().toLowerCase();
						shipTypeSipWebChange = seg.getShipTypeSEGSrvc();
					    //System.out.println("isInsideTimeCourt "+isInsideTimeCourt);
					    if (isInsideTimeCourt.equalsIgnoreCase("WITHOUTSERVICE")) {
							seg =  getNextServiceSEG(dao, shipType, true, session, conversionform);
							shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
							shipTypeSEGDescChange= seg.getShipTypeSEGSrvcDesc().toLowerCase();
							shipTypeSipWebChange= seg.getShipTypeSEGSrvc();
							request.setAttribute("error", "No tenemos disponible el servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" entre "+ (String) session.getAttribute("branchid")+ " y "+ conversionform.getDestBranch()+ " pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\".");
							//AccessLog.Log("Error in Extended Zone selection");
							conversionform.setGuiaHasErrors("N");
							conversionform.setGuiaErrorType("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
							conversionform.setNeededConfirmationService2D(true);
							conversionform.setConfirmationService2D(false);
							conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
							conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
							conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
							conversionform.setMsjConfirmationService("No tenemos disponible el servicio  \"" +shipTypeSEGDesc.toLowerCase()+" \" entre "+ (String) session.getAttribute("branchid")+ " y "+ conversionform.getDestBranch()+ " pero podemos ofrecer el servicio "+shipTypeSEGDescChange+", "+" �Desea confirmar el servicio?");
							conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE NO PODEMOS GARANTIZAR EL SERVICIO "+ shipTypeSEGDesc.toUpperCase() + " ENTRE ESTAS SUCURSALES");
							return mapping.findForward("start");
					    } else if (isInsideTimeCourt.equalsIgnoreCase("OUT")) {
							request.setAttribute("error", "No se puede garantizar el servicio express del servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" ya que se encuentra fuera del horario de corte pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\".");
							// AccessLog.Log("Error in Extended Zone selection");
							conversionform.setGuiaHasErrors("N");
							conversionform.setGuiaErrorType("El corte de este servicio ya fue realizado");
							conversionform.setNeededConfirmationService2D(true);
							conversionform.setConfirmationService2D(false);
							conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
							conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
							conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
							conversionform.setMsjConfirmationService("El tiempo para garantizar el servicio "+shipTypeSEGDesc.toLowerCase() + " ya paso pero podemos ofrecer el servicio " + shipTypeSEGDescChange + " �Desea confirmar el servicio?");
							conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE EL TIEMPO PARA GARANTIZAR EL SERVICIO "+ shipTypeSEGDesc.toUpperCase() + " YA PASO");
					    }
					    if(!conversionform.getGuiaHasErrors().equalsIgnoreCase("S") && !conversionform.getDeliveryType().isEmpty()){
						    boolean cobertura=dao.getCoverageSEG(conversionform.getDest_am_gety_code(), conversionform.getDestclave(), conversionform.getDestcode(), shipTypeSEG);
						    if(!cobertura) {
								seg =  getNextServiceSEG(dao, shipType, true, session, conversionform);
								shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
								shipTypeSEGDescChange= seg.getShipTypeSEGSrvcDesc().toLowerCase();
								shipTypeSipWebChange= seg.getShipTypeSEGSrvc();
			        			request.setAttribute("error","No tenemos disponible este servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" para este domicilio pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\".");
			        			//AccessLog.Log("Error in Extended Zone selection");
			        			conversionform.setGuiaHasErrors("N");
			        			conversionform.setGuiaErrorType("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
			        			conversionform.setNeededConfirmationService2D(true);
			        			conversionform.setConfirmationService2D(false);
			        			conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
								conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
								conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
								conversionform.setMsjConfirmationService("No tenemos disponible este servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" para este domicilio pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\", "+" �Desea confirmar el servicio?");
								conversionform.setAlertNOConfirmationService("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
			        			return mapping.findForward("start");
			        		}
						}
					} else {
						//AccessLog.Log("No error in EAD Service");
						conversionform.setCurrentTask("eadverified");
					}
				} else {
					conversionform.setBrncVrtl(dao.valBranchVrtl(conversionform.getDestSite()+"01"));
					
					if (conversionform.getBrncVrtl().equals("1")){
						request.setAttribute("error", "EL DESTINO SELECCIONADO ES UN DESTINO VIRTUAL, CUYO SERVICIO E.A.D. ES OBLIGATORIO");
						return mapping.findForward("start");
					}
					conversionform.setNeededConfirmationService2D(false);
					conversionform.setConfirmationService2D(false);
					if(dao.validateIsTypeSEG(conversionform.getShipType())) {
						String shipTypeSEGChange, shipTypeSEGDescChange, shipTypeSipWebChange;
						Object[] x = findServiceSEG(conversionform);
						int i = (Integer) x[0];
						String shipType = x[1].toString();
						String shipTypeSEG = conversionform.getShippingTypeSEG().get(i).getShipTypeSEGSrvc();
						String shipTypeSEGDesc = conversionform.getShippingTypeSEG().get(i).getShipTypeSEGSrvcDesc();
						ShipTypeSEG seg = new ShipTypeSEG();
						seg = getNextServiceSEG(dao, shipType, false, session, conversionform);
						shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
						shipTypeSEGDescChange = seg.getShipTypeSEGSrvcDesc().toLowerCase();
						shipTypeSipWebChange = seg.getShipTypeSEGSrvc();
						String isInsideTimeCourt = dao.isInsideTimeCourtSEG((String) session.getAttribute("branchid"), conversionform.getDestBranch(), shipTypeSEG, conversionform.getClientId(), conversionform.getOrgioncode());
						//System.out.println("isInsideTimeCourt " + isInsideTimeCourt);
						if (isInsideTimeCourt.equalsIgnoreCase("WITHOUTSERVICE")) {
							seg =  getNextServiceSEG(dao, shipType, true, session, conversionform);
							shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
							shipTypeSEGDescChange= seg.getShipTypeSEGSrvcDesc().toLowerCase();
							shipTypeSipWebChange= seg.getShipTypeSEGSrvc();
							request.setAttribute("error", "No tenemos disponible el servicio \"" + shipTypeSEGDesc.toLowerCase() + " \" entre "+ (String) session.getAttribute("branchid") + " y " + conversionform.getDestBranch() + " pero podemos ofrecer el servicio \"" + shipTypeSEGDescChange + "\".");
							// AccessLog.Log("Error in Extended Zone selection");
							conversionform.setGuiaHasErrors("N");
							conversionform.setGuiaErrorType("ERROR SERVICIO \"" + shipTypeSEGDesc.toUpperCase() + " \" NO DISPONIBLE PARA ESTE DOMICILIO");
							conversionform.setNeededConfirmationService2D(true);
							conversionform.setConfirmationService2D(false);
							conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
							conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
							conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
							conversionform.setMsjConfirmationService("No tenemos disponible el servicio  \"" + shipTypeSEGDesc.toLowerCase() + " \" entre " + (String) session.getAttribute("branchid") + " y " + conversionform.getDestBranch() + " pero podemos ofrecer el servicio " + shipTypeSEGDescChange + ", " + " �Desea confirmar el servicio?");
							conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE NO PODEMOS GARANTIZAR EL SERVICIO " + shipTypeSEGDesc.toUpperCase() + " ENTRE ESTAS SUCURSALES");
							return mapping.findForward("start");
						} else if (isInsideTimeCourt.equalsIgnoreCase("OUT")) {
							request.setAttribute("error", "No se puede garantizar el servicio express del servicio \"" + shipTypeSEGDesc.toLowerCase() + " \" ya que se encuentra fuera del horario de corte pero podemos ofrecer el servicio \"" + shipTypeSEGDescChange + "\".");
							// AccessLog.Log("Error in Extended Zone selection");
							conversionform.setGuiaHasErrors("N");
							conversionform.setGuiaErrorType("El corte de este servicio ya fue realizado");
							conversionform.setNeededConfirmationService2D(true);
							conversionform.setConfirmationService2D(false);
							conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
							conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
							conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
							conversionform.setMsjConfirmationService("El tiempo para garantizar el servicio " + shipTypeSEGDesc.toLowerCase() + " ya paso pero podemos ofrecer el servicio " + shipTypeSEGDescChange + " �Desea confirmar el servicio?");
							conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE EL TIEMPO PARA GARANTIZAR EL SERVICIO "+ shipTypeSEGDesc.toUpperCase() + " YA PASO");
						}
					}
				}
				//PENDIENTE REVISAR ESTE CODIGO
				//String ship = (String) session.getAttribute("shippingtypes");
				//request.setAttribute("shippingtypes", ship);
				
			} else if(conversionform.getCurrentTask().equals("validRefrClnt")) {
				HashMap<String, String> valuesRefr = dao.validRefrClnt(conversionform, global);
				
				String valResponse = valuesRefr.get("valResponse").toString();
				String msgeRefr = valuesRefr.get("msgeRefr").toString();
				
				if (valResponse.equals("1")) {
					String listRefer = conversionform.getListReferences();
					listRefer = listRefer + conversionform.getReference()+"|";
					conversionform.setListReferences(listRefer);
					conversionform.setReference("");					
				} else {
					request.setAttribute("error", "Referencia no valida. "+msgeRefr);
				}
			} else if (conversionform.getCurrentTask().equalsIgnoreCase("returnToMonitor")){
				JavAddressLovRecords addressLov = new JavAddressLovRecords();
				dao.getCentrosCosto(conversionform, global);
				addressLov.getOrgionClientAddressCC(con,session,conversionform.getCentrosCosto());
				conversionform.setAssignedBranch(global.getAssignedBranch());
				return mapping.findForward("start");
			}
			
			/*	Si el cliente busca seleccionar sucursal ocurre */
			Boolean brnchCheck = false;
			if (conversionform.getCurrentTask() != null && conversionform.getCurrentTask().equalsIgnoreCase("checkbranchtrue")
					&& conversionform.getDeliveryType().equalsIgnoreCase("O")) {
				conversionform.setOpcOcurre(true);
				GetBrnchOcurre gbo = new GetBrnchOcurre();
				String tokenApi = gbo.getToken(con);
				con.close();
				ArrayList<BranchDetailDTOResponse> sucursales = gbo.getBrnchsOcurre(tokenApi,
						conversionform.getDestSite(), conversionform.getWeight(), conversionform.getVolL(), conversionform.getVolH(), conversionform.getVolW(),
						conversionform.getVolume(), conversionform.getTarifaSel());
				con = ConnectDB.getConnection();
				conversionform.getFilteredBrnch().clear();
				if (!sucursales.isEmpty()) {
					for (Object s : sucursales) {
						BranchDetailDTOResponse brnch = gbo.mapToDTO((LinkedHashMap) s);
						if ((brnch.getClave().equalsIgnoreCase(conversionform.getBrnchOcurre().split("\\|")[0]))) {
							brnchCheck = true;
						}
						conversionform.setFilteredBrnch(brnch);
					}
					if (Boolean.FALSE.equals(brnchCheck)) {
						conversionform.setBrnchOcurre("");
					}
				}
			}else if (conversionform.getCurrentTask() != null && conversionform.getCurrentTask().equalsIgnoreCase("checkbranchtrue")
					|| conversionform.getDeliveryType().equalsIgnoreCase("O")) {
				conversionform.setOpcOcurre(false);
				GetBrnchOcurre gbo = new GetBrnchOcurre();
				String tokenApi = gbo.getToken(con);
				con.close();
				ArrayList<BranchDetailDTOResponse> sucursales = gbo.getBrnchsOcurre(tokenApi,
						conversionform.getDestSite(), conversionform.getWeight(), conversionform.getVolL(), conversionform.getVolH(), conversionform.getVolW(),
						conversionform.getVolume(), conversionform.getTarifaSel());
				con = ConnectDB.getConnection();
				conversionform.getFilteredBrnch().clear();
				if (sucursales != null && !sucursales.isEmpty()) {
					for (Object s : sucursales) {
						BranchDetailDTOResponse brnch = gbo.mapToDTO((LinkedHashMap) s);
						conversionform.setFilteredBrnch(brnch);
					}
				}
				SucursalesConfiguradas suc = new SucursalesConfiguradas();
				String brncDefaultId = suc.obtieneConfigSucursal(con, "BOK", "DEST_OCURRE", conversionform.getDestSite());
				String addr = getBrncAddr(con, brncDefaultId);
				conversionform.setDefaultBrnchAddr(addr);
			}else if((conversionform.getCurrentTask() != null && (conversionform.getCurrentTask().equalsIgnoreCase("checkbranchfalse") ||  conversionform.getCurrentTask().equalsIgnoreCase("delvryverify")))
					|| !conversionform.getDeliveryType().equalsIgnoreCase("O")) {
				conversionform.setOpcOcurre(false);
			}
			
			if (conversionform.getCurrentTask() != null && conversionform.getCurrentTask().equalsIgnoreCase("validaPedimento") &&
					!conversionform.getPedinumber().isEmpty()) {
				String valResult = validaPedimento(con, conversionform.getPedinumber()); 
				if (!valResult.equalsIgnoreCase("OK")){
					conversionform.setMsgInfo(valResult);
				}
			}
			con.close();
		} catch(Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch(Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return mapping.findForward("success");
	}
	
	/********************************************************************************************************
	 * m�todo para obtener la direcci�n de una sucursal		*
	 ********************************************************************************************************/
	private String getBrncAddr(Connection con, String brnchId) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		String query = "";
		String calle="", num="", col="", colCP="", ciudad="", estado="", lat="", lon="";
		
		try {
			if (!brnchId.equals("")) {
				query = cnct.delete(0,cnct.length())
						.append("SELECT AM_STRT_NAME, AM_DRNR, COL_DES, COLO_ZIPCODE, CIUDAD, ESTADO, BM_LOCA_LAT, BM_LOCA_LON FROM SYS_ADDR_MSTR A ")
						.append("INNER JOIN PCOBERTURA_VIEW B ON A.AM_GETY_CODE = B.COD_COLO ")
						.append("INNER JOIN SYS_BRNC_MSTR C ON A.AM_ENTY_ID = C.BM_BRNC_ID ")
						.append("WHERE A.AM_ENTY_ID = ? AND AM_DEFA_FLAG = 'Y'").toString();
				pst = con.prepareStatement(query);
				pst.setString(1, brnchId);
				
				rs = pst.executeQuery();
				
				while(rs.next()) {
					calle = rs.getString(1);
					num = rs.getString(2);
					col = rs.getString(3);
					colCP = rs.getString(4);
					ciudad = rs.getString(5);
					estado = rs.getString(6);
					lat = rs.getString(7);
					lon = rs.getString(8);
				}
				
			}
		}catch (Exception e){
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getBrncAddr()_Error:").append(e).toString());
			e.printStackTrace();
			resources.closeResources(rs,pst);
		}
		return calle +" "+num+" "+col+" "+colCP+" "+ciudad+" "+estado+"^"+lat+","+lon;
	}
	
//Added on 14/07/2010 - For kitsId : 70454 Starts
private boolean getEadService(ConversionDao dao, PrepaidValueObject pObject, ConversionForm conversionform) {
	boolean result;
	fillValueObject(pObject,conversionform);
	result = dao.delvryVerify(pObject);
	
	return result;
}
//Added on 14/07/2010 - For kitsId : 70454 Ends
//Added on 28/07/2010 - For kitsId : 70454 Starts
private boolean getChkEadAvl(ConversionDao dao, PrepaidValueObject pObject, ConversionForm conversionform)
{
	boolean result;
	fillValueObject(pObject,conversionform);
	result = dao.eadVerify(pObject);
	return result;
}
//Added on 28/07/2010 - For kitsId : 70454 Ends

	private void fillform(ConversionForm form,PrepaidValueObject object) {
		form.setClientId(object.getClientId());
		form.setBranchId(object.getBranchId());
		form.setFiscaladdresscode(object.getFiscaladdresscode());
		form.setOrgioncode(object.getOrgioncode());
		form.setZone(object.getZone());
		//form.setVolume(object.getVolume());
		form.setWeight(object.getWeight());
		form.setTotalGuiaAmount(object.getTotalGuiaAmount());
		form.setFiscal1(object.getFiscal1());
		form.setFiscal2(object.getFiscal2());
		form.setFiscalcolonia1(object.getFiscalcolonia1());
		form.setFiscalcolonia2(object.getFiscalcolonia2());
		form.setFiscaltelefono(object.getFiscaltelefono());
		form.setOrgien1(object.getOrgien1());
		form.setOrgien2(object.getOrgien2());
		form.setOrgiencolonia1(object.getOrgiencolonia1());
		form.setOrgiencolonia2(object.getOrgiencolonia2());
		form.setShippingType(object.getShippingType());
		form.setShippingTypeSEG(object.getShippingTypeSEG());
		form.setShippingTypeSEGALL(object.getShippingTypeSEGALL());
		form.setCobertura(object.getCobertura());
		form.setValordeclarado(object.getValordeclarado());
		form.setDest1(object.getDest1());
		form.setDest2(object.getDest2());
		form.setDestcolonia1(object.getDestcolonia1());
		form.setDestcolonia2(object.getDestcolonia2());
		form.setDesttelefono(object.getDesttelefono());
		form.setDestSite(object.getDestSite());
		form.setDestclave(object.getDestclave());
		form.setDestnombre(object.getDestnombre());
		form.setDestSiteName(object.getDestSiteName());
		form.setClientName(object.getClientName());
		form.setContent(object.getContent());
		if (object.getExt()!=null) {
			form.setExt(object.getExt());  // 02/Ene/2012
			form.setExtOrgnValue(object.getExt());
			if (form.getExt().charAt(0)=='S')
				form.setExtCheck("on");
			else
				form.setExtCheck("off");
		} else {
		   object.setExt("N");
		   form.setExtCheck("off");
		}
		form.setGuiaOld(object.getGuiaOld());
		form.setAceptarNuevosCargos("N");
		form.setGuiaHasErrors("N");
		form.setIsExtendedZone("N");	
		
		form.setAct(object.getAckAmt());//AAP02
		form.setActType(object.getAcklabels());//AAP02
		if (form.getAct().charAt(0)!='0')//AAP02
			form.setActCheck("on");//AAP02
		else//AAP02
			form.setActCheck("off");//AAP02
		form.setVolHMAX(object.getVolHMAX());
		form.setVolLMAX(object.getVolLMAX());
		form.setVolWMAX(object.getVolWMAX());
		form.setPesoMAX(object.getPesoMAX());
		form.setVolH(object.getVolHMAX());
		form.setVolL(object.getVolLMAX());
		form.setVolW(object.getVolWMAX());
		form.setVolHMin(object.getVolHMin());
		form.setVolLMin(object.getVolLMin());
		form.setVolWMin(object.getVolWMin());
		form.setWghtMin(object.getWghtMin());
		form.setVolMin(object.getVolMin());
		form.setShipType(object.getShipType());//JSA01
		form.setVolumeOriginal(object.getVolume());
		form.setLocationType(object.getLocationType());//AAP20
	}
	public void fillValueObject(PrepaidValueObject object,ConversionForm conversionform) {
		object.setClientId(conversionform.getClientId());
		object.setClientName(conversionform.getClientName());
		object.setFiscaladdresscode(conversionform.getFiscaladdresscode());
		object.setOrgioncode(conversionform.getOrgioncode());
		object.setNumberOfGuias(conversionform.getNumberOfGuias());
		object.setVolume(conversionform.getVolume());
		object.setWeight(conversionform.getWeight());
		object.setZone(conversionform.getZone());
		object.setShippingType(conversionform.getShippingType());
		object.setDeliveryType(conversionform.getDeliveryType()); // Added on 14/07/2010 - For kitsId : 70454
		object.setSessionId(conversionform.getSessionId());
		object.setDest1(conversionform.getDest1());
		object.setDest2(conversionform.getDest2());
		object.setDestbranch(conversionform.getDestbranch());
		//AccessLog.Log("inside setting object");
		object.setDestclave(conversionform.getDestclave());
		object.setDestcode(conversionform.getDestcode());
		//AccessLog.Log("inside setting object"+conversionform.getDestclave());
		//AccessLog.Log("inside setting object"+conversionform.getDestcode());
		object.setFiscal1(conversionform.getFiscal1());
		object.setFiscal2(conversionform.getFiscal2());
		object.setFiscalcolonia1(conversionform.getFiscalcolonia1());
		object.setFiscalcolonia2(conversionform.getFiscalcolonia2());
		object.setOrgien1(conversionform.getOrgien1());
		object.setContent(conversionform.getContent());
		
		object.setBranchId(conversionform.getBranchId());
		object.setOrgien2(conversionform.getOrgien2());
		object.setValordeclarado(conversionform.getValordeclarado());
		object.setCobertura(conversionform.getCobertura());
		object.setDestSite(conversionform.getDestSite());
		
		object.setPackType(conversionform.getPackType()); // Added on 26/03/2010
		
		object.setExt(conversionform.getExt()); // 02/Ene/2012
		object.setDest_am_gety_code(conversionform.getDest_am_gety_code());	
	}

	private ShipTypeSEG getNextServiceSEG(ConversionDao dao, String ShipTypeSEGCurrent, boolean servicioStandar, HttpSession session, ConversionForm conversionform) {
		List<ShipTypeSEG> list = dao.getFetchShipSEGALLActive((String)session.getAttribute("branchid"), conversionform.getDestBranch(), conversionform.getClientId(), conversionform.getOrgioncode());
		for (int i = 0; i < list.size(); i++) {
			if(servicioStandar && (list.get(i).getShipTypeSEGSrvcPP().contains("STD-T") || list.get(i).getShipTypeSEGSrvcPP().contains("ST") )){
				return list.get(i);
			} else if(!servicioStandar && i <= 0){
        		return list.get(i);
        	}
        }
		return null;
    }
	
	private Object[] findServiceSEG(ConversionForm conversionform){
		String shipType = conversionform.getShipType();
		if(conversionform.getShipType().equals("1D")){
			shipType = "DS";
	    }
	    int i = 0;
	    for (i = 0; i < conversionform.getShippingTypeSEG().size(); i++) {
	    	if (conversionform.getShippingTypeSEG().get(i).getShipTypeSEGSrvcPP().equals(shipType)) {
	    		break;
	    	}
	    }
	    return new Object[] {i, shipType};
    }
	
	/* Valida el pedimento capturado */
	private String validaPedimento(Connection con, String pedimento) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String validPedimento = "";
		try {
			pst = con.prepareStatement("SELECT FUN_VALID_PEDI_NUMB(?) FROM DUAL");
			
			pst.setString(1, pedimento.trim());
			rs = pst.executeQuery();
			if (rs.next()) {
				validPedimento = rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validaPedimento()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return validPedimento;
	}
}