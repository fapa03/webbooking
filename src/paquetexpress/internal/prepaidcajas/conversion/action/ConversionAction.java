
/**
 * 
 * Kits-Id			Date				Purpose
 * 
 * 69949	  		26-Mar-2010: 		To include Package type like sobre, paquete, caja.
 * 
 */
package paquetexpress.internal.prepaidcajas.conversion.action;

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

import bean.AcusesXT;
import bean.ConsultaParametros;
import bean.Global;
import bean.JavAddressLovRecords;
import bean.Resources;
import bean.SucursalesConfiguradas;
import logger.AccessLog;
import mx.com.paquetexpress.general.model.dto.BranchDetailDTOResponse;
import beanUtil.CargaInicialProductsSAT;
import beanUtil.ConnectDB;
import beanUtil.GetBrnchOcurre;
import paquetexpress.internal.prepaidcajas.action.valueobject.PrepaidValueObject;
import paquetexpress.internal.prepaidcajas.conversion.dao.ConversionDao;
import paquetexpress.internal.prepaidcajas.conversion.form.ConversionForm;
import paquetexpress.internal.prepaidcajas.conversion.action.guiamstr.PrepaidGuiaMstr;

public class ConversionAction extends Action {
	private StringBuffer concatena = new StringBuffer();
	//private final String msgAvi  = concatena.delete(0, concatena.length()).append("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	//private final String msgErr  = concatena.delete(0, concatena.length()).append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgErr  = concatena.delete(0, concatena.length()).append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	
@SuppressWarnings({ "rawtypes", "unchecked" })
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
			// return mapping.findForward("nosession");
		}
		con = ConnectDB.getConnection();
		/* Se obtiene el catalogo de productos */
		CargaInicialProductsSAT.getInstance();
		global = (Global)session.getAttribute("sGlobal");
		if(global==null) {
			return mapping.findForward("start");
			// return mapping.findForward("nosession");
		}
		conversionform=(ConversionForm)form;
		
		conversionform.setBanCerrar("true");//bandera para validar cuando se cierre la ventana desde "x" de navegador
		conversionform.setGuiaHasErrors("N");
		conversionform.setForceCaptureDimensions("N"); //global.getForceCaptureDimensions() parametro OBINDI del GLP_CUSTOMER_CTRL
		String pMaxT7="";
		pMaxT7=dao.getPesoMaximoTarifa7();
		conversionform.setPesoMaximoT7(pMaxT7);
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
		// AGREGADO CENITSOFT
		
		ArrayList list;
		list=dao.getZones();
		request.setAttribute("zones", (ArrayList) list.get(0));
		//list = null;
		//list = dao.getZonesTO();
		//ArrayList zonesTolist = new ArrayList();
		//zonesTolist = (ArrayList) list.get(0);
		request.setAttribute("zonesTO", (ArrayList) list.get(1));
		ArrayList listTarifas=new ArrayList();
		listTarifas=dao.getTarifas();
		request.setAttribute("tarifas",listTarifas);
		list=null;
		list=dao.getTarifas();
		request.setAttribute("tarifasids", list);
		// Agregar los maximos de las tarifas
		// Kilos
		list=null;
		list=dao.getMaximos("KG");
		request.setAttribute("maximoskg", list);
		// Volumen
		list=null;
		list=dao.getMaximos("CUM");
		request.setAttribute("maximosvol", list);
		list=null;
		list=dao.getZonesIds();
		request.setAttribute("zoneids",list);
		ArrayList acklabel=new ArrayList();
		ArrayList ackvalue=new ArrayList();
		ArrayList totalacklist=new ArrayList();
		totalacklist=dao.fetchAcknowledgementTypeDesc();
		acklabel=(ArrayList)totalacklist.get(0);
		ackvalue=(ArrayList)totalacklist.get(1);	
		request.setAttribute("ackvalue",ackvalue);
		request.setAttribute("acklabel",acklabel);
		//AccessLog.Log("ackvalue: "+ackvalue);
		//AccessLog.Log("acklabel: "+acklabel);
		// Agregar para Lista de Cajas
		ArrayList totalListCajas=new ArrayList();
		ArrayList listCajasType=new ArrayList();
		ArrayList listCajasValue=new ArrayList();
		String sucursal=(String)session.getAttribute("branchid");
		totalListCajas=dao.fetchFormNo(sucursal); // Obtener las cajas de ESTA SUCURSAL!!!
		listCajasType=(ArrayList)totalListCajas.get(0);
		listCajasValue=(ArrayList)totalListCajas.get(1);
		request.setAttribute("listaCajasType",listCajasType);
		request.setAttribute("listaCajasValue",listCajasValue);
		
	// FIN AGREGADO
		
		ConsultaParametros topeValorDeclarado = new ConsultaParametros();
		Double tope = Double.parseDouble(((ArrayList<?>) topeValorDeclarado.QryMdulTypeParm1(con,"BOK","MAX_DECL_AMNT", "AMOUNT").get(0)).get(2).toString());
		conversionform.setMaxDeclAmnt(tope);
	//equalsIgnoreCase
		/*AccessLog.Log(
				"*******************************************************************"+
				"conversionform.getCurrentTask() "+conversionform.getCurrentTask()+
				"********************************************************************");*/
		/*genera lista de guias en un ArrayList.*/
		StringTokenizer st = null;
		ArrayList guias = null;
		if (conversionform.getClickedItems() != null || !conversionform.getClickedItems().equals("")) {
			conversionform.setClickedItems(conversionform.getClickedItems().trim());
			st = new StringTokenizer(conversionform.getClickedItems().trim());			
			guias = new ArrayList(st.countTokens());
			while (st.hasMoreElements()) {
				guias.add(st.nextToken());
			}
		}
		
		request.setAttribute("detalleGuias", guias);
		
		if(conversionform.getCurrentTask().equals("start")) {
			session.removeAttribute("shippingtypes");
			
			conversionform.seteMailOrigCheck(false);//AAP03
			conversionform.seteMailOrigText("");//AAP03
			conversionform.seteMailDestCheck(false);//AAP03
			conversionform.seteMailDestText("");//AAP03
			dao.getCentrosCosto(conversionform, global);
			conversionform.setGenMultiCaja(dao.getBanderaMulticaja(global));
			conversionform.setFactorDividorPesoVol(dao.getConfigPesoVolumetrico());
			conversionform.setOpcOcurre(false);
			conversionform.setBrnchOcurre("");
			conversionform.setProductDescSat("");
			conversionform.setProductIdSat("");
			conversionform.setModFormnoFlag("N");

			if (global.getAssignedBranch().contains("70")){
				request.setAttribute("error", "Error: DE MOMENTO NO SE PUEDE DOCUMENTAR PREPAGO CON ORIGEN ZONA PLUS");
				return mapping.findForward("guiaConversionMonitor.do?currentTask=start");
			}
			return mapping.findForward("start");			
		} else if(conversionform.getCurrentTask().equals("load")) {
			conversionform.setBorderBranch(dao.branchLocationType((String) session.getAttribute("branchid")));//AAP20
			conversionform.seteMailOrigCheck(false);//AAP03
			conversionform.seteMailOrigText("");//AAP03
			conversionform.seteMailDestCheck(false);//AAP03
			conversionform.seteMailDestText("");//AAP03
			dao.getCentrosCosto(conversionform, global);
			conversionform.setGenMultiCaja(dao.getBanderaMulticaja(global));
			conversionform.setFlagValidRefrClnt(dao.getFlagValidRefrClnt(global));//AAP13
			conversionform.setModFormnoFlag("N");
			conversionform.setPedinumber("");
			conversionform.setCustagent("");
			/*String clickedItems = request.getParameter("clickedItems");
			System.out.println("clickedItems "+clickedItems);		
			conversionform.setTrackingNo(clickedItems.trim());*/			
			
			//System.out.println("arreglo guias "+guias);
			//if (conversionform.getTrackingNoEnd().length() == 0) {
			if (guias != null && guias.size()==1) {
				conversionform.setTrackingNo(guias.get(0).toString());
				String msjeVal = dao.verificaRastreo(conversionform.getTrackingNo(), "SHPG", "WBK");
				
				if (msjeVal.trim().equals("OK")) {
					//AccessLog.Log("Obteniendo Datos de la Guia");
					pObject = dao.getDetail(conversionform.getTrackingNo(), global.getClientId());
					//AccessLog.Log("Datos de la guia Obtenidos");								
					if (pObject != null) {
						try {
							/*si la sucursal no es frontera*///AAP20
							if (!conversionform.getBorderBranch().trim().equals("BR")) {//AAP20
								/*Si el rastreo se compro para usarse solo en frontera*///AAP20
								if (pObject.getLocationType().trim().equals("BR")) {//AAP20
									msjeVal = "Rastreo para uso solo en frontera.";//AAP20
									request.setAttribute("error", "Error: " + msjeVal);//AAP20
									return mapping.findForward("start");//AAP20
								}//AAP20
							}//AAP20
							
							//AccessLog.Log("Llenando la clase con la guia");
							conversionform.setAllowNewInvoice("N");
							fillform(conversionform,pObject, dao.validateIsTypeSEG(pObject.getShipType()));
							conversionform.setVolH("");
							conversionform.setVolL("");
							conversionform.setVolW("");
							//AccessLog.Log("Clase llenada");
						} catch (Exception e) {
							//System.out.println("error " +e);
							request.setAttribute("error", "Error:" + e.getMessage());
							return mapping.findForward("start");
						}
						if (conversionform.getOrgioncode() != null) {
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
						
						if (conversionform.getReLoad().equals("Y")) {
							request.setAttribute("error", "Se ha modificado rastreo con exito");//leyenda temporal por modificacion de rastreo
							conversionform.setReLoad("");
							conversionform.setAceptarNuevosCargos("");
							
							conversionform.setIdSetSel(pObject.getRefNo());
							conversionform.setZonaKmSel(pObject.getZone());
							conversionform.setTarifaSel(pObject.getTarifa());
							conversionform.setPesoKgSel(pObject.getWeight());
							conversionform.setVolumenSel(pObject.getVolume());							
							conversionform.setNumRastreoSel("1/1");
							conversionform.setAcuseSel(pObject.getAcusederecibo());
							conversionform.setValorDeclaradoSel(pObject.getValordeclarado());
							conversionform.setEADSel(pObject.getEad());
							conversionform.setRADSel(pObject.getRad());
							conversionform.setEXTSel(pObject.getExt());
							conversionform.setMainSetSel(pObject.getOldRefNo());
							conversionform.setInvcSel("");
							
						}
						//con=ConnectDB.getConnection(); 
					} else {
						request.setAttribute("error",concatena.delete(0, concatena.length()).append("La gu?a ").append(conversionform.getTrackingNo()).append(" no est? disponible o no est? pagada").toString());
					}
				} else {
					request.setAttribute("error", "Error: " + msjeVal);
					return mapping.findForward("start");
				}
			   	
			} else if (guias != null && guias.size()>1) {
				conversionform.setTrackingNo(guias.get(0).toString());
				conversionform.setTrackingNoEnd(guias.get( guias.size()-1 ).toString());
				//String strGuiaInicial=conversionform.getTrackingNo();
			    //String strGuiaFinal =conversionform.getTrackingNoEnd();
			    String clienteID="";
			    String clienteSiguienteID="";
	
				//int guiaInicial=Integer.parseInt(idGuiaInicial);
				//int guiaFinal=Integer.parseInt(idGuiaFinal);
				//String strGuiaN="";
				//String totalGuias="";
				//String zona="";
				String tarifa="";
				//String vol="";
				String peso="";
				String clientType="";
				boolean error=false;
			    //if (lngGuiaInicial< lngGuiaFinal) {
				ArrayList listaGuias = new ArrayList();
				String guiaNo="";
				PrepaidValueObject guiaTmp;
				//for (l=lngGuiaInicial;l<=lngGuiaFinal;l++) {
				String msjeVal = "";
				for (int i=0;i<guias.size();i++) {
					//se limpia atributo shippingtypes para que se inicie en cada iteraccion de validacion de guia
					session.removeAttribute("shippingtypes");//AAP20
					request.removeAttribute("shippingtypes");//AAP20
					guiaNo = guias.get(i).toString();
					msjeVal = dao.verificaRastreo(guiaNo, "SHPG", "WBK");
					if (msjeVal.trim().equals("OK")) {
						pObject=dao.getDetail(guiaNo, global.getClientId());
						if(pObject!=null) {
							//System.out.println("listaGuias "+listaGuias);
							// Detectar si alguna de las guias pertenecen a otro cliente
							if (listaGuias.size()>0) {
								guiaTmp=(PrepaidValueObject)listaGuias.get(0);
								clienteID=guiaTmp.getClientId();
								clienteSiguienteID=pObject.getClientId();
								
								if (!clienteID.equals(clienteSiguienteID)) {
									request.setAttribute("error", concatena.delete(0, concatena.length()).append("La guia ").append(guiaNo).append(" no corresponde al cliente:").append(clienteID).toString());
									error=true;
									return mapping.findForward("start");
								}
								// Detectar si alguna de las guias No corresponde a la Zona en kilometros del primer elemento de la lista
								//zona=pObject.getZone();
								if (!pObject.getZone().equals(guiaTmp.getZone())) {
									error=true;
									request.setAttribute("error", concatena.delete(0, concatena.length()).append("La guia ").append(guiaNo).append(" no corresponde al rango de kilometros de la primera guia").toString());
									return mapping.findForward("start");	
								}
								// Detectar si alguna de las guias no corresponde a la tarifa del primer elemento de la lista
								tarifa=pObject.getTarifa();
								if (!tarifa.equals(guiaTmp.getTarifa())) {
									error=true;
									request.setAttribute("error", concatena.delete(0, concatena.length()).append("La guia ").append(guiaNo).append(" no corresponde a la tarifa de la primera guia").toString());
									return mapping.findForward("start");
								}
								peso=pObject.getWeight();
								if (!peso.equals(guiaTmp.getWeight())) {
									error=true;
									request.setAttribute("error", concatena.delete(0, concatena.length()).append("La guia ").append(guiaNo).append(" no corresponde al peso de la primera guia").toString());
									return mapping.findForward("start");
								}
								//vol=pObject.getVolume();
								if (!pObject.getVolume().equals(guiaTmp.getVolume())) {
									error=true;
									request.setAttribute("error", concatena.delete(0, concatena.length()).append("La guia ").append(guiaNo).append(" no corresponde al volumen de la primera guia").toString());
									return mapping.findForward("start");
								}
								// Detectar si alguna de las guias no
								// corresponde a la venta del primer
								// elemento de la lista (FRONTERA / NO FRONTERA)
								if (!pObject.getLocationType().equals(guiaTmp.getLocationType())) {//AAP20
									error = true;//AAP20
									request.setAttribute("error", "La guia " + guiaNo + " no corresponde a la venta de la primer guia (FRONTERA / NO FRONTERA)");//AAP20											
									return mapping.findForward("start");//AAP20
								}//AAP20
							}
							// Detectar si alguna de las guias no corresponde a la tarifa del primer elemento de la lista
							if (!error) {
								// Get Client Type
								clientType=dao.clientType(pObject.getClientId());
								fillform(conversionform,pObject, dao.validateIsTypeSEG(pObject.getShipType()));
								conversionform.setClientType(clientType);
								if(conversionform.getOrgioncode()!=null) {
									if(conversionform.getOrgioncode().length()>0) {
										request.setAttribute("origin","originpresent");
									}
								}
								// 23/01/2012

								session.setAttribute("shippingtypes",pObject.getShippingType());
								request.setAttribute("shippingtypes",pObject.getShippingType());
							}
							listaGuias.add(pObject);
							//con=ConnectDB.getConnection(); 
						} else {
							request.setAttribute("error",concatena.delete(0, concatena.length()).append("La gu?a ").append(guiaNo).append(" no est? disponible o no est? pagada o ya fue utilizada").toString());
							return mapping.findForward("success");
						}
						// Fin Ciclo For
						if (conversionform.getSerieCaja().length()>0) {
							session.setAttribute("serieCaja", conversionform.getSerieCaja());
							request.setAttribute("serieCaja", conversionform.getSerieCaja());
						}					
					}					
				}
				// Fin Verificar Rango de Guias
				//} else {
				//	 request.setAttribute("error","La guia Final es MENOR a la guia Inicial en el rango");
				//	 return mapping.findForward("success");
				//}
			}
			
			/*VALIDACION DE REQUERIMIENTO DE ACUSES*///AAP02
			if (dao.obtieneInfRequerimientosACK(conversionform.getClientId())) {
				//AccessLog.Log("tiene requerimiento de acuses");
				conversionform.setReqAcuse("Y");
			} else {			
				conversionform.setReqAcuse("N");
			}
			/*fin validacion requerimiento de acuses.*/			
			
			conversionform.setClientId(global.getClientId());//AAP06 REGISTRO MULTI CAJA MANEJO DE CLIENTE AL QUE ESTA ASIGNADO EL USUARIO DEL LOGIN.
			conversionform.setClientName(global.getClientName());//AAP06 REGISTRO MULTI CAJA
			dao.getDirCentrosCosto(conversionform, session);
			conversionform.setAssignedBranch(global.getAssignedBranch());
			if (conversionform.getOrgien1().equalsIgnoreCase("")) {
				request.setAttribute("error","Centro de costo sin direcci?n asignada.");
			}
		} else if(conversionform.getCurrentTask().equalsIgnoreCase("cambioCCosto")) {
			//System.out.println("ENTRO A CURRENTTASK load");
			dao.getDirCentrosCosto(conversionform, session);
			conversionform.setAssignedBranch(global.getAssignedBranch());
		} else if(conversionform.getCurrentTask().equalsIgnoreCase("insurance")) {			
			fillValueObject(pObject,conversionform);			
			con=ConnectDB.getConnection(); 
			dao.calculateInsuranceAmount(con,pObject);
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
			String horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(), tipoEnvio, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
			String horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(), tipoEnvio, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
	
			conversionform.setHorasEntregaOcu(horaOcu);
			conversionform.setHorasEntregaEad(horaEad);
					
			String guideHasExtended = conversionform.getExt();
			boolean zoneextendedcheck = dao.isextendedzone(conversionform);//AAP02
			String msjeReturn = "";
			if (zoneextendedcheck) {
			   conversionform.setIsExtendedZone("S");
			   
			   /*VALIDACION PARA LIMITE DE VALOR DECLARADO CON OPERADOR LOGISTICO*///AAP02
			   //AccessLog.Log("conversionform.getInsCheck() "+conversionform.getInsCheck());
			   //AccessLog.Log("conversionform.getOperadorLogistico() "+conversionform.getOperadorLogistico());
			   //AccessLog.Log("conversionform.conversionform.getValordeclarado() "+conversionform.getValordeclarado());
			   if (conversionform.getOperadorLogistico().trim().length()==0) {					   	
					request.setAttribute("error","Direcci?n destino sin operador logistico configurado. Favor de revisar con el ?rea correspondiente para su correcci?n");
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
			   }
				if (conversionform.getInsCheck().trim().length()>0 && conversionform.getInsCheck().trim().equals("on")) {
					//aform.setOperadorLogistico(ol.get(1).toString());
					double montoMaxOl = 0;
					conversionform.setMontoMaxOl("");
					if (conversionform.getOperadorLogistico().trim().length()>0) {
						
						//AccessLog.Log("conversionform.getOperadorLogistico() "+conversionform.getOperadorLogistico());
						ArrayList ol = dao.obtieneInfOperadorLogistico(conversionform);
						if (!ol.isEmpty()) {
							try {
								montoMaxOl = Double.parseDouble(ol.get(3).toString());
								//AccessLog.Log("montoMaxOl "+montoMaxOl);
							} catch (Exception e) {
								montoMaxOl = 0;
							}
							if (Double.parseDouble(conversionform.getValordeclarado())>montoMaxOl) {
								conversionform.setMontoMaxOl(ol.get(3).toString());
								//AccessLog.Log("********************************MONTO ASEGURADO NO PERMITIDO");									
							}
						}
					}			
				}/*fin VALIDACION PARA LIMITE DE VALOR DECLARADO CON OPERADOR LOGISTICO*///AAP02
			  
			   if (guideHasExtended.indexOf("N")>-1) {
					request.setAttribute("error","El cliente pertenece a una zona extendida y no se ha contratado el servicio");
					//AccessLog.Log("Error in Extended Zone selection");
					conversionform.setGuiaHasErrors("S");
					conversionform.setGuiaErrorType("ERROR EN ZONA EXTENDIDA");
					//session.setAttribute("zoneFocus",String.valueOf(0));
					msjeReturn = "start";
					//return mapping.findForward("start");					
			   } else {
					conversionform.setGuiaHasErrors("N");
					msjeReturn = "success";
					//return mapping.findForward("success");
			   }			   
			} else {
				conversionform.setIsExtendedZone("N");
				conversionform.setGuiaHasErrors("N");
				conversionform.setGuiaErrorType("");
			}
			if(conversionform.getForceCaptureDimensions().equalsIgnoreCase("N") && !(dao.validateIsTypeSEG(conversionform.getShipType()))) {
				String msg = dao.getMsgDimensionesIngresar();
				if(!msg.isEmpty()) {
					request.setAttribute("msjeDim",msg);
					conversionform.setGuiaHasErrors("N");
					conversionform.setGuiaErrorType(msg);
				}
			}
			
			conversionform.setNeededConfirmationService2D(false);
			conversionform.setConfirmationService2D(false);

			if(dao.validateIsTypeSEG(conversionform.getShipType())) {
				calculaTarifaSEG(conversionform, dao);
			    String isInsideTimeCourt=dao.isInsideTimeCourtSEG((String) session.getAttribute("branchid"),conversionform.getDestBranch(), shipTypeSEG, conversionform.getClientId(), conversionform.getOrgioncode());
			    //System.out.println("isInsideTimeCourt "+isInsideTimeCourt);

			    if (isInsideTimeCourt.equalsIgnoreCase("WITHOUTSERVICE")) {
					seg =  getNextServiceSEG(dao, shipType, true, session, conversionform);
					shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
					shipTypeSEGDescChange= seg.getShipTypeSEGSrvcDesc().toLowerCase();
					shipTypeSipWebChange= seg.getShipTypeSEGSrvc();
					horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
					horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
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
					conversionform.setMsjConfirmationService("No tenemos disponible el servicio  \"" +shipTypeSEGDesc.toLowerCase()+" \" entre "+ (String) session.getAttribute("branchid")+ " y "+ conversionform.getDestBranch()+ " pero podemos ofrecer el servicio "+shipTypeSEGDescChange+", "+" ?Desea confirmar el servicio?");
					conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE NO PODEMOS GARANTIZAR EL SERVICIO "+ shipTypeSEGDesc.toUpperCase() + " ENTRE ESTAS SUCURSALES");
					msjeReturn = "start";
			    } else if (isInsideTimeCourt.equalsIgnoreCase("OUT")) {
					horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
					horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
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
					conversionform.setMsjConfirmationService("El tiempo para garantizar el servicio "+shipTypeSEGDesc.toLowerCase() + " ya paso pero podemos ofrecer el servicio " + shipTypeSEGDescChange + " ?Desea confirmar el servicio?");
					conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE EL TIEMPO PARA GARANTIZAR EL SERVICIO "+ shipTypeSEGDesc.toUpperCase() + " YA PASO");
					msjeReturn = "success";
			    }
			    if(!conversionform.getGuiaHasErrors().equalsIgnoreCase("S") && !conversionform.getDeliveryType().isEmpty() && !conversionform.getDeliveryType().equalsIgnoreCase("O")){
				    boolean cobertura=dao.getCoverageSEG(conversionform.getDest_am_gety_code(), conversionform.getDestclave(), conversionform.getDestcode(), shipTypeSEG);
				    if(!cobertura) {
						seg =  getNextServiceSEG(dao, shipType, true, session, conversionform);
						shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
						shipTypeSEGDescChange= seg.getShipTypeSEGSrvcDesc().toLowerCase();
						shipTypeSipWebChange= seg.getShipTypeSEGSrvc();
						horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
						horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
						conversionform.setHorasEntregaOcu(horaOcu);
						conversionform.setHorasEntregaEad(horaEad);
	        			request.setAttribute("error","No tenemos disponible este servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" para este domicilio pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\".");
	        			//AccessLog.Log("Error in Extended Zone selection");
	        			conversionform.setGuiaHasErrors("N");
	        			conversionform.setGuiaErrorType("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
	        			conversionform.setNeededConfirmationService2D(true);
	        			conversionform.setConfirmationService2D(false);
	        			conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
						conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
						conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
						conversionform.setMsjConfirmationService("No tenemos disponible este servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" para este domicilio pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\", "+" ?Desea confirmar el servicio?");
						conversionform.setAlertNOConfirmationService("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
	        			return mapping.findForward("start");
	        		}
				}
			}
			if (msjeReturn.length()>0) {
				return mapping.findForward(msjeReturn);	
			}			
		} else if(conversionform.getCurrentTask().equalsIgnoreCase("zoneverify")) {
			
			boolean zonecheck=dao.zoneverify(conversionform.getTrackingNo(),(String)session.getAttribute("branchid"),conversionform.getDestbranch(),conversionform.getDestSite());
			//AccessLog.Log("zone"+zonecheck);
			if(!zonecheck) {
				request.setAttribute("error","Error en la selecci?n de zona y destino, excede el rango de kilometros contratados");
				//AccessLog.Log("Error in Zone selection");
				conversionform.setGuiaHasErrors("S");
				conversionform.setGuiaErrorType("ERROR EN RANGO DE KILOMETROS CONTRATADOS");
				session.setAttribute("zoneFocus",String.valueOf(0)); // ADDED FOR ZONE VERIFIED FOCUS
			} else {
				//AccessLog.Log("No error in Zone selection");
				conversionform.setCurrentTask("zoneverified");
				conversionform.setGuiaHasErrors("N");
				session.setAttribute("zoneFocus",String.valueOf(1)); // ADDED FOR ZONE VERIFIED FOCUS
			}
			
			/*PENDIENTE REVISAR VARIABLE session.getAttribute("shippingtypes")*/
			//String ship=(String)session.getAttribute("shippingtypes");//PENDIENTE
			//request.setAttribute("shippingtypes",ship);//PENDIENTE
			//request.setAttribute("shippingtypes","notorigin");
			
		    String caja=(String)session.getAttribute("serieCaja");
		    request.setAttribute("serieCaja", caja);
		    
			/*ArrayList listins=(ArrayList)session.getAttribute("insurancetypes");
			ArrayList listinslabel=(ArrayList)session.getAttribute("insurancetypeslabels");
			request.setAttribute("insurancetypes",listins);
			request.setAttribute("insurancetypeslabels",listinslabel);*/
		
		} else if(conversionform.getCurrentTask().equalsIgnoreCase("checkclientcredit")) {
			if(session.getAttribute("status")!=null)
				session.removeAttribute("status");
			String credit = dao.creditStatus(conversionform.getClientId(),session);
			conversionform.setActType(conversionform.getActTypeCurrent());
			conversionform.setAcusederecibo(conversionform.getActTypeCurrent());
			conversionform.setClientHasLocalCredit(credit);
			//System.out.println("conversionform.getClientHasLocalCredit() "+conversionform.getClientHasLocalCredit());
			return mapping.findForward("start");			
			} else if (conversionform.getCurrentTask().equalsIgnoreCase("guidepreviewcost")) {
				PrepaidGuiaMstr guiaOld = new PrepaidGuiaMstr();
				//PrepaidValueObject guiaOld=new PrepaidValueObject();
				guiaOld = dao.getPrepaidGuiaMaster(conversionform.getTrackingNo());
				String nuevaZona = conversionform.getZone();
				//AccessLog.Log("nuevaZona " + nuevaZona);
				//AccessLog.Log("guiaOld.getBgm_zone() " + guiaOld.getBgm_zone());
				String nuevaTarifa = conversionform.getTarifa();
				//AccessLog.Log("nuevaTarifa " + nuevaTarifa);
				//AccessLog.Log("guiaOld.getBgm_tarifa() "+ guiaOld.getBgm_tarifa());
				String volumen = conversionform.getVolume().replace(",", ".");
				//AccessLog.Log("volumen " + volumen);
				String peso = conversionform.getWeight();
				//AccessLog.Log("peso " + peso);
				String tarifaT7 = "";
				String valorRegreso[] = null;//AAP10
				//valorRegreso = dao.getTarifaEnvio(nuevaZona, nuevaTarifa, peso, volumen, tarifaT7);//AAP10				
//				String tarifaEnvio = valorRegreso[0];//AAP10
//				//AccessLog.Log("tarifaEnvio " + tarifaEnvio);
//				tarifaT7 = valorRegreso[1];//AAP10
				//AccessLog.Log("tarifaT7 " + tarifaT7);
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
				
				//String impuestos[] = dao.getTaxes(guiaOld.getBgm_prep_brnc_id().substring(0, 3));//AAP20
				String impuestos[] = dao.getTaxes(taxBrnc.substring(0, 3));//AAP20
				String clientType = "";
				double tarifaAnterior = guiaOld.getBgm_gh_amt() / guiaOld.getBgm_no_of_guis();
				boolean weightChangeSEG=false;
				boolean changeZone=false;
				double pesoDouble = 0.0;
				double pesoNuevoSEG = 0.0;
				try {
					pesoDouble = Double.parseDouble(peso.trim());
				} catch (Exception e) {
					pesoDouble = 0.0;
				}
				boolean modifTarif = true;
				if(dao.validateIsTypeSEG(conversionform.getShipType())) {
					if(!nuevaZona.equals(guiaOld.getBgm_zone())){
						changeZone=true;
					}
					modifTarif = false;
					try {
						pesoNuevoSEG = Double.parseDouble(conversionform.getWeightOriginal());
						pesoNuevoSEG += conversionform.getWeightUpdate();
					} catch (Exception e) {
						pesoNuevoSEG = 0.0;
					}
					if(pesoNuevoSEG>Double.valueOf(conversionform.getWeightOriginal())){
						weightChangeSEG = true;
						calculaTarifaSEG(conversionform, dao);
						nuevaTarifa = conversionform.getTarifa();
					}
				}
				
				// habilitar para produccion
				// volumen = volumen.replace(',', '.');

				//if ((nuevaZona.equals(guiaOld.getBgm_zone()) && nuevaTarifa.equals(guiaOld.getBgm_tarifa()))
				//		&& (volumen.replace(',', '.').equals(guiaOld.getBgm_vlum().replace(',', '.')) && pesoDouble == guiaOld.getBgm_wght())) {
				if ( Integer.parseInt(nuevaZona) == Integer.parseInt(guiaOld.getBgm_zone()) && nuevaTarifa.equals(guiaOld.getBgm_tarifa())){
						//&& (Double.parseDouble(volumen.replace(',', '.')) <= Double.parseDouble(guiaOld.getBgm_vlum()))) {
					//System.out.println("ENTRO A ASIGNAR TARIFA ANTERIOR COMO NUEVA");
					//tarifaEnvio = String.valueOf(tarifaAnterior);//AAP10
					modifTarif = false;
				}
				if (nuevaTarifa.substring(0,2).equals("T7") && 
						(guiaOld.getBgm_wght() != pesoDouble || !guiaOld.getBgm_vlum().equalsIgnoreCase(volumen))) {
					modifTarif = true;
				}
				
				/*COMPARACION PARA VALIDAR TARIFA POR SALTO DE TARIFA/ZONA O POR DIFERENCIA DE FLETE (solo para T7)*/
				if (modifTarif) {//AAP10
					if (nuevaTarifa.substring(0,2).equals("T7")) {//AAP10
						valorRegreso = dao.getTarifaEnvio(nuevaZona, nuevaTarifa, peso, volumen, tarifaT7);//AAP10
					} else {//AAP10
						valorRegreso = dao.getTarifaEnvioSaltoTarif(guiaOld, conversionform);//AAP10
					}//AAP10
				} else {//AAP10
					valorRegreso = new String[2];//AAP10
					valorRegreso[0] = String.valueOf(tarifaAnterior);//AAP10
					valorRegreso[1] = "";//AAP10
				}//AAP10
				
				
				
				String tarifaEnvio = valorRegreso[0];
				//AccessLog.Log("tarifaEnvio " + tarifaEnvio);
				tarifaT7 = valorRegreso[1];
				
				double eadAnterior = guiaOld.getBgm_ead_amt();
				double radAnterior = guiaOld.getBgm_rad_amt();
				double extAnterior = guiaOld.getBgm_ext_amt();
				// double actAnterior=guiaOld.getBgm_ack_amt();
				//double dblNuevaTarifa = Double.parseDouble(tarifaEnvio) - tarifaAnterior;//AAP10
				Double dblNuevaTarifa = 0D;//AAP10,JSA01

//				if (modifTarif && dblNuevaTarifa <= 0) {//AAP10
//					tarifaEnvio = String.valueOf(tarifaAnterior + 1);//AAP10
//					dblNuevaTarifa = Double.parseDouble(tarifaEnvio) - tarifaAnterior;//AAP10
//				}//AAP10
				//AccessLog.Log("dblNuevaTarifa " + dblNuevaTarifa);//AAP10
				//double dblNuevoEAD = Double.parseDouble(dao.getTarifaEAD(tarifaEnvio)) - eadAnterior;//AAP10
				//double dblNuevoRAD = Double.parseDouble(dao.getTarifaRAD(tarifaEnvio)) - radAnterior;//AAP10
				double dblNuevoEAD = 0;//AAP10
				double dblNuevoRAD = 0;//AAP10
				
				String rateConfig = dao.getRateConfig();//AAP12				
				if(weightChangeSEG || changeZone){

					double valorExceso = 0, valor = 0;
					Double exce=pesoNuevoSEG -Double.valueOf(conversionform.getWeightOriginal());
					Double[] tarifa =dao.getTariffSEG(conversionform.getShippingTypeSEG().get(0).getShipTypeSEGSrvc(), nuevaZona);
					if(changeZone){
						exce=pesoNuevoSEG - 1;
						valor = 1 * tarifa[0];
						if(exce > 0){
							valorExceso =exce * tarifa[1];
						}
						dblNuevaTarifa = valor + valorExceso;
						dblNuevaTarifa = dblNuevaTarifa - (Double.valueOf(tarifaEnvio) + eadAnterior + radAnterior);
					}else{
						valorExceso = exce * tarifa[1];
						dblNuevaTarifa = valor + valorExceso;
					}
					dblNuevoEAD = Double.parseDouble(dao.getTariffServiceSEG(dblNuevaTarifa.toString(), "BOK","EAD", "SERV_EXPRESS",false));
					dblNuevoEAD = (double)Math.round(dblNuevoEAD * 100) / 100;//REDONDEA A 2 DIGITOS//AAP10
					dblNuevoRAD = Double.parseDouble(dao.getTariffServiceSEG(dblNuevaTarifa.toString(), "BOK","RAD", "SERV_EXPRESS",false));
					dblNuevoRAD = (double)Math.round(dblNuevoEAD * 100) / 100;//REDONDEA A 2 DIGITOS//AAP10
					dblNuevaTarifa = dblNuevaTarifa-(dblNuevoEAD+dblNuevoRAD);
				}else if (nuevaTarifa.substring(0,2).equals("T7")) {//AAP10
					dblNuevaTarifa = Double.parseDouble(tarifaEnvio) - tarifaAnterior;
					
					if (modifTarif && dblNuevaTarifa <= 0) {//AAP10
						tarifaEnvio = String.valueOf(tarifaAnterior + 1);//AAP10
						dblNuevaTarifa = Double.parseDouble(tarifaEnvio) - tarifaAnterior;//AAP10
					}//AAP10
					
					dblNuevaTarifa = (double)Math.round(dblNuevaTarifa * 100) / 100;//REDONDEA A 2 DIGITOS//AAP10
					
					if (conversionform.getEad().equalsIgnoreCase("S") || 
							eadAnterior > 0 || modifTarif) {
						dblNuevoEAD = Double.parseDouble(dao.getTariffService(tarifaEnvio, "PPG", "EAD", rateConfig/*AAP12*/, true)) - eadAnterior;//AAP10
						dblNuevoEAD = (double)Math.round(dblNuevoEAD * 100) / 100;//REDONDEA A 2 DIGITOS//AAP10
					}
					
					if (conversionform.getRad().equalsIgnoreCase("S") || 
							radAnterior > 0 || modifTarif) {
						dblNuevoRAD = Double.parseDouble(dao.getTariffService(tarifaEnvio, "PPG", "RAD", rateConfig/*AAP12*/, true)) - radAnterior;//AAP10
						dblNuevoRAD = (double)Math.round(dblNuevoRAD * 100) / 100;//REDONDEA A 2 DIGITOS//AAP10
					}
				} else {//AAP10					
					if (modifTarif) {
						dblNuevaTarifa = Double.parseDouble(tarifaEnvio);//AAP10
					}
					
					if (eadAnterior == 0 || modifTarif) {
						dblNuevoEAD = Double.parseDouble(dao.getTariffService(tarifaEnvio, "PPG", "EAD", rateConfig/*AAP12*/, false));//AAP10
						dblNuevoEAD = (double)Math.round(dblNuevoEAD * 100) / 100;//REDONDEA A 2 DIGITOS//AAP10
					}
					
					if (radAnterior == 0 || modifTarif) {
						dblNuevoRAD = Double.parseDouble(dao.getTariffService(tarifaEnvio, "PPG", "RAD", rateConfig/*AAP12*/, false));//AAP10
						dblNuevoRAD = (double)Math.round(dblNuevoRAD * 100) / 100;//REDONDEA A 2 DIGITOS//AAP10
					}
					
					
				}//AAP10
				
				double dblNuevoEXT = 0.0d;
				if (conversionform.getExt().equalsIgnoreCase("S") && extAnterior == 0) {
					dblNuevoEXT = Double.parseDouble(dao.getTarifaEXT()) - extAnterior;
					dblNuevoEXT = (double)Math.round(dblNuevoEXT * 100) / 100;//REDONDEA A 2 DIGITOS//AAP10
				}
				 
				if (conversionform.getInsAmt() != null && !conversionform.getInsAmt().isEmpty() &&
						Double.parseDouble(conversionform.getInsAmt()) > tope) {	
					request.setAttribute("error","Monto Máximo a Capturar de Valor Declarado para este Envio: "+tope);
				}
				
				double dblNuevoValorAsegurado = 0;
				String strNuevoAcuseRecibo = dao.getAckTotal(conversionform.getAcusederecibo());
				double dblNuevoAcuseRecibo = 0;
				if (strNuevoAcuseRecibo != null && strNuevoAcuseRecibo.length() > 0)
					dblNuevoAcuseRecibo = Double.parseDouble(strNuevoAcuseRecibo);
				if (conversionform.getValordeclarado().charAt(0) == '0') {
					if (conversionform.getInsAmt().charAt(0) != '0') {
						//dblNuevoValorAsegurado = (Double.parseDouble(conversionform.getInsAmt()) / 1000) * 9;// 9 por Millar
						dblNuevoValorAsegurado = (Double.parseDouble(conversionform.getInsAmt()) / 1000) * dao.getInvBase();// 9.8 por Millar
					}
				}
				
				
				dblNuevoValorAsegurado = (double)Math.round(dblNuevoValorAsegurado * 100) / 100;//REDONDEA A 2 DIGITOS//AAP10
				dblNuevoAcuseRecibo = (double)Math.round(dblNuevoAcuseRecibo * 100) / 100;//REDONDEA A 2 DIGITOS//AAP10
				
				clientType = dao.clientType(guiaOld.getBgm_clint_id());
				conversionform.setClientType(clientType);
				// dblNuevoAcuseRecibo=0;
				//conversionform.setNuevoValorEnvio(Double.toString(dblNuevaTarifa));//AAP10
				conversionform.setNuevoValorAcuse(Double.toString(dblNuevoAcuseRecibo));
				conversionform.setNuevoValorSeguro(Double.toString(dblNuevoValorAsegurado));
				
				if (conversionform.getEad().indexOf("S") > -1) {//AAP10
					conversionform.setNuevoValorEAD(Double.toString(dblNuevoEAD));
					if (modifTarif && !nuevaTarifa.substring(0,2).equals("T7")) {//AAP10
						dblNuevaTarifa =  dblNuevaTarifa - dblNuevoEAD;//AAP10
					}//AAP10
				}//AAP10
					
				if (conversionform.getRad().indexOf("S") > -1) {//AAP10
					conversionform.setNuevoValorRAD(Double.toString(dblNuevoRAD));
					if (modifTarif && !nuevaTarifa.substring(0,2).equals("T7")) {//AAP10
						dblNuevaTarifa =  dblNuevaTarifa - dblNuevoRAD;//AAP10
					}//AAP10
				}//AAP10
				
				if (conversionform.getExt().indexOf("S") > -1)
					conversionform.setNuevoValorEXT(Double.toString(dblNuevoEXT));
				
				conversionform.setNuevoValorEnvio(Double.toString(dblNuevaTarifa));//AAP10
				
				conversionform.setAceptarNuevosCargos("S");
				conversionform.setTarifaT7(tarifaT7);
				if (tarifaT7.length() > 0)
					conversionform.setTarifa(tarifaT7);
				//conversionform.setPayMode("ToPay");
				conversionform.setValorIVA(impuestos[0]);
				conversionform.setValorRET(impuestos[1]);
				
				conversionform.setCmpyId(dao.getCompanyId(conversionform.getWeight(), conversionform.getClientId(), "SHP-G"));
				conversionform.setRetieneCmpy(dao.getRetIvaByCompany(conversionform.getCmpyId()));
				
				
				boolean creditEnabled = dao.clntCreditStatusEnabled(conversionform.getClientId(), taxBrnc);
				if (creditEnabled) {
					if (conversionform.getPayMode().isEmpty()) {
						conversionform.setPayMode("PAID");
					}
				} else {
					conversionform.setPayMode("ToPay");
				}
				
				//Se crea objeto PrepaidGuiaMstr con importes de la nueva guia
				PrepaidGuiaMstr nuevaGuia = new PrepaidGuiaMstr();
				nuevaGuia.setBgm_gh_amt(dblNuevaTarifa);
				nuevaGuia.setBgm_ack_amt(dblNuevoAcuseRecibo);
				nuevaGuia.setBgm_insur_amt(dblNuevoValorAsegurado);
				nuevaGuia.setBgm_ead_amt(conversionform.getEad().indexOf("S") > -1 ? dblNuevoEAD : 0);
				nuevaGuia.setBgm_rad_amt(conversionform.getRad().indexOf("S") > -1 ? dblNuevoRAD : 0);
				nuevaGuia.setBgm_ext_amt(conversionform.getExt().indexOf("S") > -1 ? dblNuevoEXT : 0);
				nuevaGuia.setCompanyId(conversionform.getCmpyId());
				
				//Se calcula el total de retenci?n tal cual se va a guardar en bd
				String[] results = dao.calcRetAmount(nuevaGuia, conversionform.getPayMode(), conversionform.getClientId(), taxBrnc);
				conversionform.setValorRetAmount(results[0]);
				conversionform.setIvaRetTemplate(results[1]);
				
				if (session.getAttribute("status") != null)
					session.removeAttribute("status");
				request.removeAttribute("origin");
				conversionform.setAllowNewInvoice("N");
				String siteId=(String)session.getAttribute("sSiteId");
				ArrayList records = dao.getClientrecords(siteId,conversionform.getClientId());
				boolean validAddress = false;
				for(int i=0;i < records.size(); i++){
					HashMap values = (HashMap) records.get(i);
					if(values.get("AM_ADDR_CODE").toString().equalsIgnoreCase(conversionform.getFiscaladdresscode())){
						validAddress=true;
						conversionform.setFiscalcolonia1(values.get("u14").toString());
						conversionform.setFiscalcolonia2(values.get("u16").toString());
						conversionform.setFiscal1(values.get("AM_STRT_NAME").toString());
						conversionform.setFiscal2(values.get("AM_DRNR").toString());
						conversionform.setFiscaltelefono(values.get("AM_PHNO1").toString());
						conversionform.setFiscalrfc(values.get("rfc").toString());
						break;
					}
				}
				if(!validAddress){
					conversionform.setFiscaladdresscode("");
				}
				conversionform.setOrgioncode("");
				
				if (conversionform.getEad().equalsIgnoreCase("S")) conversionform.setEadCheck("on");
				if (conversionform.getRad().equalsIgnoreCase("S")) conversionform.setRadCheck("on");
				if (conversionform.getExt().equalsIgnoreCase("S")) conversionform.setExtCheck("on");
				return mapping.findForward("start");
			} else if(conversionform.getCurrentTask().equalsIgnoreCase("guideconfirm")) {	
			String result = "";
			boolean msjeErr = false;
		    if (conversionform.getAllowNewInvoice().equals("N")) {
				request.setAttribute("error", "Si va a facturar nuevos servicios, seleccione un codigo de cliente que pertenezca a la sucursal" );
				//AccessLog.Log(result);
				msjeErr = true;
				//return mapping.findForward("start");
		    } else {
		    	if (conversionform.getFiscaladdresscode().equals("")) {
					request.setAttribute("error", "Seleccione el domicilio fiscal del cliente que pertenece a esta sucursal" );
					//AccessLog.Log(result);
					msjeErr = true;
					//return mapping.findForward("start");
		    	}
		    }
		    String facturaMod = dao.valGuiaModificada(conversionform.getTrackingNo());
			if (facturaMod.length()>0) {
				request.setAttribute("error", "El rastreo ya esta modificado, factura: "+facturaMod);
				msjeErr = true;
			}
		    if (msjeErr) {
		    	return mapping.findForward("start");
		    }
			try {
			// Inserta Nuevo Registro en BOK_GUIDE_MSTR con los datos actuales
			PrepaidGuiaMstr guiaOld = new PrepaidGuiaMstr();
			
			// PrepaidValueObject guiaOld=new PrepaidValueObject();
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
			
			guiaOld.setBgm_vlum(conversionform.getVolume());
			guiaOld.setBgm_wght(Double.parseDouble(conversionform.getWeight()));
			if(dao.validateIsTypeSEG(conversionform.getShipType())) {
				Double nuevoPeso = Double.parseDouble(conversionform.getWeightOriginal())+conversionform.getWeightUpdate();
				guiaOld.setBgm_wght(nuevoPeso);
			}
			guiaOld.setBgm_zone(conversionform.getZone());
			guiaOld.setBgm_ead_service(conversionform.getEad());
			guiaOld.setBgm_rad_service(conversionform.getRad());
			guiaOld.setBgm_ext_service(conversionform.getExt());
			guiaOld.setBgm_gh_amt(Double.parseDouble(conversionform.getNuevoValorEnvio()));
			guiaOld.setBgm_ead_amt(Double.parseDouble(conversionform.getNuevoValorEAD()));
			guiaOld.setBgm_rad_amt(Double.parseDouble(conversionform.getNuevoValorRAD()));
			guiaOld.setBgm_ext_amt(Double.parseDouble(conversionform.getNuevoValorEXT()));
			guiaOld.setBgm_tarifa(conversionform.getTarifa());
			String strActTax="0";
			if (conversionform.getAcusederecibo().equalsIgnoreCase("ACK-N") && conversionform.getAcusederecibo().length()==0) {
				guiaOld.setBgm_ack_service("");
				guiaOld.setBgm_ack_amt(0);
				guiaOld.setBgm_ack_tax(0);
			} else {
			  guiaOld.setBgm_ack_service(conversionform.getAcusederecibo());
			  // guiaOld.setBgm_ack_amt(Double.parseDouble(dao.getAckTotal(guiaOld.getBgm_ack_service())));
			  guiaOld.setBgm_ack_amt(Double.parseDouble(conversionform.getNuevoValorAcuse()));
			  //strActTax=dao.getImpuesto("ACK",Double.toString(guiaOld.getBgm_ack_amt()), guiaOld.getBgm_orgn_brnc_id()/*, guiaOld.getBgm_prep_brnc_id()*/);//AAP20
			  strActTax=dao.getImpuesto("ACK",Double.toString(guiaOld.getBgm_ack_amt()), taxBrnc);//AAP20
			  strActTax=strActTax.replace(',','.');
			  guiaOld.setBgm_ack_tax(Double.parseDouble(strActTax));
			}
			if (conversionform.getNuevoValorSeguro().charAt(0)!='0') {
				guiaOld.setBgm_insur_service(conversionform.getInsAmt());
				guiaOld.setBgm_insur_amt(Double.parseDouble(conversionform.getNuevoValorSeguro()));
				//guiaOld.setBgm_insur_tax(Double.parseDouble(dao.getImpuesto("INV",Double.toString(guiaOld.getBgm_insur_amt()), guiaOld.getBgm_orgn_brnc_id()/*, guiaOld.getBgm_prep_brnc_id()*/).replace(',','.')));//AAP20
				guiaOld.setBgm_insur_tax(Double.parseDouble(dao.getImpuesto("INV",Double.toString(guiaOld.getBgm_insur_amt()), taxBrnc).replace(',','.')));
			} else {
				guiaOld.setBgm_insur_service("");
				guiaOld.setBgm_insur_amt(0);
				guiaOld.setBgm_insur_tax(0);
			}
			//guiaOld.setBgm_ext_amt(Double.parseDouble(conversionform.getExtAmt()));
	        guiaOld.setBgm_valid_flg("I");
	        guiaOld.setBgm_clint_id(conversionform.getClientId());
	        guiaOld.setBgm_prep_brnc_id((String)session.getAttribute("branchid"));
	        guiaOld.setBgm_orgn_brnc_id((String)session.getAttribute("branchid"));
	        // Modifica aqui los nuevos valores de lo que viene de la forma contra lo que tienes de valor anterior        
	        
	        
	        // Aqui termina lo que debes de cambiar
	        
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
	        // Finalizar revisar credito disponible
	        
//	        String tipoTarifa="";
//	        if (conversionform.getTarifaT7().length()>0)
//	        	tipoTarifa=conversionform.getTarifaT7();
//	        else
//	        	tipoTarifa=conversionform.getTarifa();
	        //result=dao.reInsertRecord(guiaOld, session, conversionform.getTrackingNo(),tipoTarifa, conversionform.getFiscaladdresscode(), conversionform.getOrgioncode());
	        result=dao.reInsertRecord(guiaOld, conversionform.getTrackingNo(),conversionform.getFiscaladdresscode(), conversionform.getOrgioncode());
			if((result!=null)&&(result.length()>0)) {
				request.setAttribute("error", result + "   Error de ReInsercion" );
				//AccessLog.Log(result);
				return mapping.findForward("start");
			}
			String formNoStr = "";
			if (!tipoPago.equals("PAID")) {//AAPXX
				int formNo = dao.fetchFormNumber(session);
				formNoStr = Integer.toString(formNo);
			}
			PrepaidGuiaMstr guiaNew=new PrepaidGuiaMstr();
			guiaNew=dao.getPrepaidGuiaMaster(conversionform.getTrackingNo());
			
			 result = dao.reFactura(guiaNew, session, request, conversionform.getTrackingNo(), conversionform.getPayMode(),formNoStr, conversionform.getClientId(), taxBrnc/*AAP20*/);
			} catch (Exception e) {
				request.setAttribute("error", "Error de Procedimiento:" + e.getMessage());
				return mapping.findForward("start");
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
			  if (conversionform.getPayMode().equals("PAID")) {//AAPXX
				  request.setAttribute("error", "Se ha modificado rastreo con exito");
			  } else {					
				request.setAttribute("error", "Se ha generado la factura : " + result);
				//System.out.println("result "+result);
				//System.out.println("result.substring(0, 2) "+result.substring(0, 2));
				if (result.substring(0, 2).equals("AD")|| result.substring(0, 2).equals("BD")||result.substring(0, 2).equals("CD")) {
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
				} else if (conversionform.getCurrentTask().equalsIgnoreCase("convert")) {
					
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
					// Revisa si no hay ningun error activo
	
					// if (conversionform.getGuiaHasErrors().indexOf("S")>-1)
					// /{
					// request.setAttribute("error",
					// "La guia contiene errores que no se corrigieron");
					// return mapping.findForward("success");
					// }
					//AccessLog.Log("paso 1");
					if (conversionform.getDeliveryType().equalsIgnoreCase("H")) {
						boolean delvryCheck = getEadService(dao, pObject, conversionform);
						boolean chkEADAvl = getChkEadAvl(dao, pObject, conversionform);
						if (!delvryCheck) {
							request.setAttribute("error", "El servicio de EAD no est? disponible para la direcci?n destino seleccionada");
							//AccessLog.Log("EAD Service is not available for the destination");
							conversionform.setGuiaHasErrors("S");
							return mapping.findForward("start");
						} else if (!chkEADAvl) {
							request.setAttribute("error", "LA FACILIDAD (SERVICIO) DE ENTREGA A DOMICILIO NO ESTA DISPONIBLE PARA ESTE CLIENTE");
							//AccessLog.Log("EAD Service is not available for the client");
							conversionform.setGuiaHasErrors("S");
							return mapping.findForward("start");
						} else {
							//AccessLog.Log("No error in EAD Service");
							conversionform.setCurrentTask("eadverified");
						}
					}
					//Validaciones para servicio express garantizado
					if(dao.validateIsTypeSEG(conversionform.getShipType())) {
						calculaTarifaSEG(conversionform, dao);
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
								horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
								horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
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
								conversionform.setMsjConfirmationService("No tenemos disponible el servicio  \"" +shipTypeSEGDesc.toLowerCase()+" \" entre "+ (String) session.getAttribute("branchid")+ " y "+ conversionform.getDestBranch()+ " pero podemos ofrecer el servicio "+shipTypeSEGDescChange+", "+" ?Desea confirmar el servicio?");
								conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE NO PODEMOS GARANTIZAR EL SERVICIO "+ shipTypeSEGDesc.toUpperCase() + " ENTRE ESTAS SUCURSALES");
								return mapping.findForward("start");
						    } else if (isInsideTimeCourt.equalsIgnoreCase("OUT")) {
								horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
								horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
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
								conversionform.setMsjConfirmationService("El tiempo para garantizar el servicio "+shipTypeSEGDesc.toLowerCase() + " ya paso pero podemos ofrecer el servicio " + shipTypeSEGDescChange + " ?Desea confirmar el servicio?");
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
									horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
									horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
									conversionform.setHorasEntregaOcu(horaOcu);
									conversionform.setHorasEntregaEad(horaEad);
				        			request.setAttribute("error","No tenemos disponible este servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" para este domicilio pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\".");
				        			//AccessLog.Log("Error in Extended Zone selection");
				        			conversionform.setGuiaHasErrors("N");
				        			conversionform.setGuiaErrorType("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
				        			conversionform.setNeededConfirmationService2D(true);
				        			conversionform.setConfirmationService2D(false);
				        			conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
									conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
									conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
									conversionform.setMsjConfirmationService("No tenemos disponible este servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" para este domicilio pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\", "+" ?Desea confirmar el servicio?");
									conversionform.setAlertNOConfirmationService("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
				        			return mapping.findForward("start");
				        		}
							}
						}
					}
					if (guias != null && (guias.size()==1 || (guias.size()>1 && conversionform.isGenMultiCaja()) ) ) {
						
						pObject = dao.getDetail(conversionform.getTrackingNo(), global.getClientId());
						if (pObject == null) {
							request.setAttribute("error", "La gu?a no est? disponible o no est? pagada");
							conversionform.setGuiaHasErrors("S");
							// request.setAttribute("error","Either the guia is not available or is not paid");
							return mapping.findForward("start");
						}
						if(!conversionform.getShipType().equalsIgnoreCase("ST") && (Double.valueOf(conversionform.getWeight())>Double.valueOf(conversionform.getWeightOriginal())
								|| Double.valueOf(conversionform.getWeightVolumetric())>Double.valueOf(conversionform.getWeightOriginal()))){
		        			if(conversionform.getOldRefNo() != null && !conversionform.getOldRefNo().isEmpty()) {
		        				request.setAttribute("error","El peso ingresado es mayor al contratado.");
		        				conversionform.setGuiaErrorType("El peso ingresado es mayor al contratado.");
		        			} else {
		        				request.setAttribute("error","El peso ingresado es mayor al contratado, favor de generar primero el pago del cambio antes de continuar");
		        				conversionform.setGuiaErrorType("El peso ingresado es mayor al contratado, favor de generar primero el pago del cambio antes de continuar");
		        			}
		        			//AccessLog.Log("Error in Extended Zone selection");
		        			conversionform.setGuiaHasErrors("S");//session.setAttribute("zoneFocus",String.valueOf(0));
		        			return mapping.findForward("start");
						}
						//AccessLog.Log("Before conversion*********************" + pObject.getDestclave());
						//AccessLog.Log("Before conversion*********************" + pObject.getDestcode());
						fillValueObject(pObject, conversionform);
						// Revisar si es una guia con datos cambiados
						String result;
						result = dao.convert(pObject,
								conversionform.getTrackingNo(), session, 
								//request,
								//(String) session.getAttribute("serieCaja"),
								"PP",
								conversionform,
								guias.size()
								);
	
						if ((result != null) && (result.length() > 0)) {
							if (result.indexOf("Conversion Not Allowed") > -1)
								request.setAttribute("error", "Documentaci?n no permitida");
							// request.setAttribute("error","Conversion Not Allowed");
							else if (result.indexOf("Error in Zone") > -1)
								// request.setAttribute("error",result);
								request.setAttribute("error", "Error en la selecci?n de zona y destino");
							// request.setAttribute("error","Error in Zone Selection And Destination Selection");
							else
								request.setAttribute("error", result + "Falla de inserci?n");
							// request.setAttribute("error","Error in Conversion");
							conversionform.setGuiaHasErrors("S");
							//AccessLog.Log(result);
						} else {
							boolean insrtEmail = false;
							if (conversionform.geteMailOrigText().length() > 0
									|| conversionform.geteMailDestText().length() > 0) {// AAP03
								insrtEmail = true;
							} 
	
							if (insrtEmail) {// AAP03
								dao.insertWebCntrlMail(conversionform,
										conversionform.getTrackingNo(), 
										global.getOrigenUserClave());// AAP03
							}// AAP03
							
							/*inserta guia y centro de costo seleccionado*/
							dao.insertBookGuiaExtra(conversionform, conversionform.getTrackingNo(), global);//AAP05
							
							if (conversionform.getPedinumber().isEmpty() && !conversionform.getLastWrongPediNum().isEmpty()) {
								dao.insertBokHistErrorPediNumb(con, conversionform.getLastWrongPediNum(), conversionform.getTrackingNo());
							}
							
							dao.insertBookGuiaRel(conversionform, conversionform.getTrackingNo(), global, guias);//AAP06 REGISTRO MULTI CAJA
							
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
							
							request.setAttribute("error", "Gu?a documentada con ?xito....");
							// request.setAttribute("error","The Guia Converted SuccessFully....");
							// pObject=null;
							/*generacion de archivo, BRPETIQ.ETI*/
							//Valido si la guia tiene acuse XT
							String rastreo_XT ="";
							AcusesXT xt = new AcusesXT();
							//Validamos si el cliente selecciono acuses XT
							if(conversionform.getActTypeCurrent().equals("ACK-X")) {
								global.setAcuseXT(true);
								if(!conversionform.getTrackingNo().contains("|")) {
									rastreo_XT = xt.genAcuseXT(conversionform.getTrackingNo());
									//<input type="hidden" name="trackingNoGenRet" value='<bean:write name="trackingNoGenRet"/>'/>
								}
							}else {global.setAcuseXT(false);}
					
							/*insercion de etiquetas en tabla para generacion de PDF*/
							dao.insertCtrlLabelPrn(conversionform.getTrackingNo());
							
							//Generar cadena de impresi?n
							String cadena = dao.getCadenaImpresion(request, conversionform.getTrackingNo(), conversionform.isGenMultiCaja(),global.isAcuseXT(),rastreo_XT);//AAP04
							//System.out.println("GUIA STRING: " + conversionform.getTrackingNo());
							if(conversionform.getActTypeCurrent().equals("ACK-X")) {
								if(!conversionform.getTrackingNo().contains("|")) {
									//System.out.println("Se desactiva la guia de retorno");
									xt.desactivarGuiaDeRetorno(conversionform.getTrackingNo());//Desactivamos la guia de retorno
									//System.out.println("Genero el PDF para: " + rastreo_XT);
									xt.insertForPDFXT(rastreo_XT);//Generamos el PDF para la guia de retorno
								}
							}
							String getCartaPorte = ConnectDB.getCartaPorteExt();
							if (request.getRemoteAddr().trim().substring(0,3).equals("0:0") || request.getRemoteAddr().trim().substring(0,8).equals("192.168.")) {
								getCartaPorte =ConnectDB.getCartaPorteInt();
							}
							//request.setAttribute("clickedItems", conversionform.getClickedItems());
							request.setAttribute("clickedItems", conversionform.getTrackingNo());
							request.setAttribute("getCartaPorte", getCartaPorte);
							request.setAttribute("clickedItemsRet", rastreo_XT);
							request.setAttribute("cadena", cadena);
							return mapping.findForward("downloadpage");
						}
					} else if ((guias != null && guias.size()>1) && !conversionform.isGenMultiCaja())  {
						// Realizar la conversion de el rango de guias de Inicio
						// hasta el final
						// Ya no es necesario hacer la verificacion de guias puesto
						// que ya se hizo al principio
						String guiaNo = "";
						String result = "";
						String guiasImpresion = "";
						String guiasImpresionXT = "";
						String rastreo_XT ="";
						//for (l = lngGuiaInicial; l <= lngGuiaFinal; l++) {
						for (int i = 0; i < guias.size(); i++) {	
							guiaNo = guias.get(i).toString();
							pObject = dao.getDetail(guiaNo, global.getClientId());
							// pObject=dao.getDetail(conversionform.getTrackingNo());
							if (pObject != null) {
								//AccessLog.Log("Before conversion*********************" + pObject.getDestclave());
								//AccessLog.Log("Before conversion*********************" + pObject.getDestcode());
								//AccessLog.Log("guiaNo*********************" + guiaNo);
								fillValueObject(pObject, conversionform);
								result = dao.convert(pObject, guiaNo, session,
										//request,									
										//(String) session.getAttribute("serieCaja"),
										"PP",
										conversionform, 1);// conversionform.getSerieCaja()
								// result=dao.convert(pObject,conversionform.getTrackingNo(),session,request);
								if ((result != null) && (result.length() > 0)) {
									if (result.indexOf("Conversion Not Allowed") > -1)
										request.setAttribute("error", "Documentaci?n no permitida");
									else if (result.indexOf("Error in Zone") > -1)
										request.setAttribute("error", "Error en la selecci?n de zona y destino");
									else
										request.setAttribute("error", result);
									//AccessLog.Log(result);
									conversionform.setGuiaHasErrors("S");
								} else {
									boolean insrtEmail = false;
									if (conversionform.geteMailOrigText().length() > 0
											|| conversionform.geteMailDestText().length() > 0) {// AAP03
										insrtEmail = true;
									} 
			
									if (insrtEmail) {// AAP03
										dao.insertWebCntrlMail(conversionform,
												guiaNo, 
												global.getOrigenUserClave());// AAP03
									}// AAP03
									
									/*inserta guia y centro de costo seleccionado*/
									dao.insertBookGuiaExtra(conversionform, guiaNo, global);//AAP05
									
									if (conversionform.getPedinumber().isEmpty() && 
											conversionform.getLastWrongPediNum() != null &&
											!conversionform.getLastWrongPediNum().isEmpty()) {
										dao.insertBokHistErrorPediNumb(con, conversionform.getLastWrongPediNum(), guiaNo);
									}
									
									dao.insertBookGuiaRel(conversionform, guiaNo, global, guias);//AAP06 REGISTRO MULTI CAJA
									
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
											dao.insertwebGuiaRefr(stRefers.nextToken(), guiaNo, global, mainRefr);
											count++;
										}
									} else {
										
										//Insercion de referencia en WEB_GUIA_REFR
										if (conversionform.getReference().length()>0) {
											dao.insertwebGuiaRefr(conversionform.getReference(), guiaNo, global,"Y");//AAP07
										}
									}
									
									//Generar guias de retorno por cada guia original
									AcusesXT xt = new AcusesXT();
									//Validamos si el cliente selecciono acuses XT
									if(conversionform.getActTypeCurrent().equals("ACK-X")) {
										global.setAcuseXT(true);
										if(!conversionform.getTrackingNo().contains("|")) {
											rastreo_XT = xt.genAcuseXT(guiaNo);
										}
									}else {global.setAcuseXT(false);}
									
									/*insercion de etiquetas en tabla para generacion de PDF*/
									dao.insertCtrlLabelPrn(guiaNo);
									
									request.setAttribute("error", "Guias Documentadas con Exito");
									//AccessLog.Log("inside download page");
								}
							} else {
								request.setAttribute("error","No se encontro la guia numero: " + guiaNo);
								return mapping.findForward("success");
							}
							guiasImpresion =  concatena.delete(0, concatena.length()).append(guiasImpresion).append(guiaNo).append("|").toString();
							if(conversionform.getActTypeCurrent().equals("ACK-X")) {
								guiasImpresionXT = concatena.delete(0, concatena.length()).append(guiasImpresionXT).append(rastreo_XT).append("|").toString();
							}
						}
						guiasImpresion = guiasImpresion.substring(0,guiasImpresion.length()-1);
						//System.out.println("guiasImpresion "+guiasImpresion);
						if(conversionform.getAcuseSel().equals("ACK-X")) {global.setAcuseXT(true);}else {global.setAcuseXT(false);}
						String cadena = dao.getCadenaImpresion(request, guiasImpresion, conversionform.isGenMultiCaja(), global.isAcuseXT(),guiasImpresionXT);//AAP04
						String rastreosArray[] = null;
						String rastreosRet[] = null;
						if(conversionform.getAcuseSel().equals("ACK-X")) {
							AcusesXT xt = new AcusesXT();
							rastreosArray = guiasImpresion.split("\\|");
							rastreosRet = guiasImpresionXT.split("\\|");
							for(String strRastreo:rastreosArray) {
								xt.desactivarGuiaDeRetorno(strRastreo);
								//System.out.println("La guia de retorno ligada al rastreo: " + strRastreo +  " ha sido desactivada");
								//xt.insertForPDFXT(strRastreo);
							}
							for(String x:rastreosRet) {
								xt.insertForPDFXT(x);
								//System.out.println("PDF para: " + x + " ha sido generado");
							}
						}
						String getCartaPorte = ConnectDB.getCartaPorteExt();
						if (request.getRemoteAddr().trim().substring(0,3).equals("0:0") || request.getRemoteAddr().trim().substring(0,8).equals("192.168.")) {
							getCartaPorte =ConnectDB.getCartaPorteInt();
						}
						request.setAttribute("clickedItems", conversionform.getClickedItems());
						request.setAttribute("clickedItemsRet", guiasImpresionXT);
						request.setAttribute("getCartaPorte", getCartaPorte);
						request.setAttribute("cadena", cadena);
						return mapping.findForward("downloadpage");
					}
	
			} else if(conversionform.getCurrentTask().equalsIgnoreCase("delvryverify")) {// Added on 14/07/2010 - For kitsId : 70454 Starts
				conversionform.setBrncVrtl("");
				//System.out.println("conversionform.getDeliveryType() "+conversionform.getDeliveryType());
				if (conversionform.getDeliveryType().equalsIgnoreCase("H")) {
					boolean delvryCheck = getEadService(dao, pObject, conversionform);
					boolean chkEADAvl = getChkEadAvl(dao, pObject, conversionform);
					if (!delvryCheck) {
						request.setAttribute("error", "El servicio de EAD no est? disponible para la direcci?n destino seleccionada");
						// AccessLog.Log("EAD Service is not available");
					} else if (!chkEADAvl) {
						request.setAttribute("error", "LA FACILIDAD (SERVICIO???) DE ENTREGA A DOMICILIO NO ESTA DISPONIBLE PARA ESTE CLIENTE");
						// AccessLog.Log("EAD Service is not available for the client");
						conversionform.setGuiaHasErrors("S");
						return mapping.findForward("start");
					} else if(conversionform.getShippingTypeSEG() != null) {
						String horaOcu, horaEad;
					    String shipTypeSEGChange, shipTypeSEGDescChange, shipTypeSipWebChange;
					    Object[] x = findServiceSEG(conversionform);
					    int i= (Integer) x[0];
					    String shipType=x[1].toString();
					    String shipTypeSEG = conversionform.getShippingTypeSEG().get(i).getShipTypeSEGSrvc();
					    String shipTypeSEGDesc = conversionform.getShippingTypeSEG().get(i).getShipTypeSEGSrvcDesc();
					    
					    horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSEG, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
						horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSEG, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
						conversionform.setHorasEntregaOcu(horaOcu);
						conversionform.setHorasEntregaEad(horaEad);
						
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
							horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
							horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
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
							conversionform.setMsjConfirmationService("No tenemos disponible el servicio  \"" +shipTypeSEGDesc.toLowerCase()+" \" entre "+ (String) session.getAttribute("branchid")+ " y "+ conversionform.getDestBranch()+ " pero podemos ofrecer el servicio "+shipTypeSEGDescChange+", "+" ?Desea confirmar el servicio?");
							conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE NO PODEMOS GARANTIZAR EL SERVICIO "+ shipTypeSEGDesc.toUpperCase() + " ENTRE ESTAS SUCURSALES");
							return mapping.findForward("start");
					    } else if (isInsideTimeCourt.equalsIgnoreCase("OUT")) {
							horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
							horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
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
							conversionform.setMsjConfirmationService("El tiempo para garantizar el servicio "+shipTypeSEGDesc.toLowerCase() + " ya paso pero podemos ofrecer el servicio " + shipTypeSEGDescChange + " ?Desea confirmar el servicio?");
							conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE EL TIEMPO PARA GARANTIZAR EL SERVICIO "+ shipTypeSEGDesc.toUpperCase() + " YA PASO");
					    }
					    if(!conversionform.getGuiaHasErrors().equalsIgnoreCase("S") && !conversionform.getDeliveryType().isEmpty()){
						    boolean cobertura=dao.getCoverageSEG(conversionform.getDest_am_gety_code(), conversionform.getDestclave(), conversionform.getDestcode(), shipTypeSEG);
						    if(!cobertura) {
								seg =  getNextServiceSEG(dao, shipType, true, session, conversionform);
								shipTypeSEGChange = seg.getShipTypeSEGSrvcPP();
								shipTypeSEGDescChange= seg.getShipTypeSEGSrvcDesc().toLowerCase();
								shipTypeSipWebChange= seg.getShipTypeSEGSrvc();
								horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
								horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
								conversionform.setHorasEntregaOcu(horaOcu);
								conversionform.setHorasEntregaEad(horaEad);
			        			request.setAttribute("error","No tenemos disponible este servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" para este domicilio pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\".");
			        			//AccessLog.Log("Error in Extended Zone selection");
			        			conversionform.setGuiaHasErrors("N");
			        			conversionform.setGuiaErrorType("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
			        			conversionform.setNeededConfirmationService2D(true);
			        			conversionform.setConfirmationService2D(false);
			        			conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
								conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
								conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
								conversionform.setMsjConfirmationService("No tenemos disponible este servicio \"" +shipTypeSEGDesc.toLowerCase()+" \" para este domicilio pero podemos ofrecer el servicio \""+shipTypeSEGDescChange+"\", "+" ?Desea confirmar el servicio?");
								conversionform.setAlertNOConfirmationService("ERROR SERVICIO \"" +shipTypeSEGDesc.toUpperCase()+" \" NO DISPONIBLE PARA ESTE DOMICILIO");
			        			return mapping.findForward("start");
			        		}
						}
					}else {
						// AccessLog.Log("No error in EAD Service");
						conversionform.setCurrentTask("eadverified");
					}
				} else {
					conversionform.setBrncVrtl(dao.valBranchVrtl(conversionform.getDestBranch()));
					String horaOcu, horaEad;
					if (conversionform.getBrncVrtl().equals("1")){
						request.setAttribute("error", "EL DESTINO SELECCIONADO ES UN DESTINO VIRTUAL, CUYO SERVICIO E.A.D. ES OBLIGATORIO");
						return mapping.findForward("start");
					}
					conversionform.setNeededConfirmationService2D(false);
					conversionform.setConfirmationService2D(false);
					if(dao.validateIsTypeSEG(conversionform.getShipType())) {
						calculaTarifaSEG(conversionform, dao);
						String shipTypeSEGChange, shipTypeSEGDescChange, shipTypeSipWebChange;
						Object[] x = findServiceSEG(conversionform);
						int i = (Integer) x[0];
						String shipType = x[1].toString();
						String shipTypeSEG = conversionform.getShippingTypeSEG().get(i).getShipTypeSEGSrvc();
						String shipTypeSEGDesc = conversionform.getShippingTypeSEG().get(i).getShipTypeSEGSrvcDesc();
						
						horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSEG, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
						horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSEG, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
						conversionform.setHorasEntregaOcu(horaOcu);
						conversionform.setHorasEntregaEad(horaEad);
						
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
							horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
							horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
							conversionform.setHorasEntregaOcu(horaOcu);
							conversionform.setHorasEntregaEad(horaEad);
							request.setAttribute("error", "No tenemos disponible el servicio \"" + shipTypeSEGDesc.toLowerCase() + " \" entre "+ (String) session.getAttribute("branchid") + " y " + conversionform.getDestBranch() + " pero podemos ofrecer el servicio \"" + shipTypeSEGDescChange + "\".");
							// AccessLog.Log("Error in Extended Zone selection");
							conversionform.setGuiaHasErrors("N");
							conversionform.setGuiaErrorType("ERROR SERVICIO \"" + shipTypeSEGDesc.toUpperCase() + " \" NO DISPONIBLE PARA ESTE DOMICILIO");
							conversionform.setNeededConfirmationService2D(true);
							conversionform.setConfirmationService2D(false);
							conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
							conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
							conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
							conversionform.setMsjConfirmationService("No tenemos disponible el servicio  \"" + shipTypeSEGDesc.toLowerCase() + " \" entre " + (String) session.getAttribute("branchid") + " y " + conversionform.getDestBranch() + " pero podemos ofrecer el servicio " + shipTypeSEGDescChange + ", " + " ?Desea confirmar el servicio?");
							conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE NO PODEMOS GARANTIZAR EL SERVICIO " + shipTypeSEGDesc.toUpperCase() + " ENTRE ESTAS SUCURSALES");
							return mapping.findForward("start");
						} else if (isInsideTimeCourt.equalsIgnoreCase("OUT")) {
							horaOcu = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "OCU", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
							horaEad = dao.getDeliveryHours((String)session.getAttribute("branchid"), conversionform.getDestBranch(), "EAD", conversionform.getDest_am_gety_code(),shipTypeSipWebChange, dao.getSysDateFormat(con,"'dd/mm/rrrr'"), global.getClientId());
							conversionform.setHorasEntregaOcu(horaOcu);
							conversionform.setHorasEntregaEad(horaEad);
							request.setAttribute("error", "No se puede garantizar el servicio express del servicio \"" + shipTypeSEGDesc.toLowerCase() + " \" ya que se encuentra fuera del horario de corte pero podemos ofrecer el servicio \"" + shipTypeSEGDescChange + "\".");
							// AccessLog.Log("Error in Extended Zone selection");
							conversionform.setGuiaHasErrors("N");
							conversionform.setGuiaErrorType("El corte de este servicio ya fue realizado");
							conversionform.setNeededConfirmationService2D(true);
							conversionform.setConfirmationService2D(false);
							conversionform.setShipTypeSEGChange(shipTypeSipWebChange);
							conversionform.setShipTypeSEGDescChange(shipTypeSEGDescChange);
							conversionform.setShipTypeSEGPPChange(shipTypeSEGChange);
							conversionform.setMsjConfirmationService("El tiempo para garantizar el servicio " + shipTypeSEGDesc.toLowerCase() + " ya paso pero podemos ofrecer el servicio " + shipTypeSEGDescChange + " ?Desea confirmar el servicio?");
							conversionform.setAlertNOConfirmationService("NO SE PUEDE CONTINUAR YA QUE EL TIEMPO PARA GARANTIZAR EL SERVICIO "+ shipTypeSEGDesc.toUpperCase() + " YA PASO");
						}
					}
				}
						//PENDIENTE REVISAR VARIABLE
						//String ship=(String)session.getAttribute("shippingtypes");
						//request.setAttribute("shippingtypes",ship);
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
			
			/* Si el cliente busca seleccionar sucursal */
			Boolean brnchCheck = false;
			if (conversionform.getCurrentTask() != null
					&& (conversionform.getCurrentTask().equalsIgnoreCase("checkbranchtrue"))
					&& conversionform.getDeliveryType().equalsIgnoreCase("O")) {
				conversionform.setOpcOcurre(true);
				GetBrnchOcurre gbo = new GetBrnchOcurre();
				String tokenApi = gbo.getToken(con);
				con.close();
				ArrayList<BranchDetailDTOResponse> sucursales = gbo.getBrnchsOcurre(tokenApi, conversionform.getDestSite(),
						conversionform.getWeight(), conversionform.getVolL(), conversionform.getVolH(),
						conversionform.getVolW(), conversionform.getVolume(), conversionform.getTarifa());
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
				ArrayList<BranchDetailDTOResponse> sucursales = gbo.getBrnchsOcurre(tokenApi, conversionform.getDestSite(),
						conversionform.getWeight(), conversionform.getVolL(), conversionform.getVolH(),
						conversionform.getVolW(), conversionform.getVolume(), conversionform.getTarifa());
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
			}else if ((conversionform.getCurrentTask() != null
					&& conversionform.getCurrentTask().equalsIgnoreCase("checkbranchfalse")
					|| conversionform.getCurrentTask().equalsIgnoreCase("delvryverify"))
					|| !conversionform.getDeliveryType().equalsIgnoreCase("O")) {
				conversionform.setOpcOcurre(false);
			}
			
			if (conversionform.getCurrentTask() != null && conversionform.getCurrentTask().equalsIgnoreCase("validaPedimento") &&
					!conversionform.getPedinumber().isEmpty()) {
				String valResult = validaPedimento(con, conversionform.getPedinumber()); 
				if (!valResult.equalsIgnoreCase("OK")){
					conversionform.setLastWrongPediNum(conversionform.getPedinumber());
					conversionform.setMsgInfo(valResult);
				}else {
					conversionform.setLastWrongPediNum("");
				}
			}
			con.close();
		} catch (Exception e) {
			AccessLog.Log(e.getMessage());
			e.printStackTrace();
		} finally {
		try {
			if(con != null) {
				con.close();
			}
		} catch(Exception e) {
			AccessLog.Log( "IGNORE THE FOLLOWING EXCEPTION:");
			AccessLog.Log(e);
		}
	}
	return mapping.findForward("success");
}

	/********************************************************************************************************
	 * m?todo para obtener la direcci?n de una sucursal		*
	 ********************************************************************************************************/
	private String getBrncAddr(Connection con, String brnchId) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		String query = "";
		String calle="", num="", col="", colCP="", ciudad="", estado="", lat="", lon="";
		
		try {
			if (brnchId != null && !brnchId.equals("")) {
				query = concatena.delete(0,concatena.length())
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
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getBrncAddr()_Error:").append(e).toString());
			e.printStackTrace();
			resources.closeResources(rs,pst);
		} finally {
			resources.closeResources(rs, pst);
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
	private boolean getChkEadAvl(ConversionDao dao, PrepaidValueObject pObject, ConversionForm conversionform) {
		boolean result;
		fillValueObject(pObject,conversionform);
		result = dao.eadVerify(pObject);
		return result;
	}
	//Added on 28/07/2010 - For kitsId : 70454 Ends
	
	public void fillform(ConversionForm form,PrepaidValueObject object, boolean isSrvcExpress) {	
		
		form.setClientId(object.getClientId());	
		form.setBranchId(object.getBranchId());	
		form.setFiscaladdresscode(object.getFiscaladdresscode());	
		form.setOrgioncode(object.getOrgioncode());	
		form.setZone(object.getZone());	
		form.setVolume(object.getVolume());
		form.setWeightVolumetric(object.getVolume());
		form.setVolumeOriginal(object.getVolume());
		form.setWeight(object.getWeight());
		form.setWeightOriginal(object.getWeight());
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
		form.setShippingTypeSEG(object.getShippingTypeSEG());//JSA01
		form.setShippingTypeSEGALL(object.getShippingTypeSEGALL());//JSA01
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
		form.setEad(object.getEad());	
		form.setRad(object.getRad());	
		form.setExt(object.getExt());	
		if (form.getEad().charAt(0)=='S')
			form.setEadCheck("on");
		else
			form.setEadCheck("off");
	
		if (form.getRad().charAt(0)=='S')
			form.setRadCheck("on");
		else
			form.setRadCheck("off");
	
		if (form.getExt().charAt(0)=='S')
			form.setExtCheck("on");
		else
			form.setExtCheck("off");
				
		form.setRefNo(object.getRefNo());	
		form.setGuiaHasErrors("N");	
		form.setIsExtendedZone("N");	
		form.setOldRefNo(object.getOldRefNo());	
		// form.setOriginalClientID(object.getClientId());
		form.setTarifa(object.getTarifa());
		form.setTarifaOriginal(object.getTarifa());
		form.setNuevoValorEnvio("0");	
		form.setNuevoValorEAD("0");	
		form.setNuevoValorRAD("0");	
		form.setNuevoValorEXT("0");	
		form.setNuevoValorSeguro("0");	
		form.setNuevoValorAcuse("0");	
		form.setAceptarNuevosCargos("N");	
		form.setTarifaT7("");	
		form.setAct(object.getAckAmt());	
		form.setActType(object.getAcklabels());	
		if(object.getAcklabels()==null) {
			form.setActTypeCurrent(object.getAcusederecibo());
			form.setAcusederecibo(object.getAcusederecibo());
		}else {
			form.setActTypeCurrent(object.getAcklabels());
			form.setAcusederecibo(object.getAcklabels());
		}
		
		if (form.getAct().charAt(0)!='0')
			form.setActCheck("on");
		else
			form.setActCheck("off");
			
		form.setInsAmt(object.getValordeclarado());	
	    if (form.getValordeclarado().charAt(0)!='0')
	    	form.setInsCheck("on");
	    else
	    	form.setInsCheck("off");
		form.setVolHMAX(object.getVolHMAX());//JSA01
		form.setVolLMAX(object.getVolLMAX());//JSA01
		form.setVolWMAX(object.getVolWMAX());//JSA01
		form.setPesoMAX(object.getPesoMAX());//JSA01
		form.setVolMAX(object.getVolMAX());//JSA02
		form.setVolHMin(object.getVolHMin());
		form.setVolLMin(object.getVolLMin());
		form.setVolWMin(object.getVolWMin());
		form.setWghtMin(object.getWghtMin());
		form.setVolMin(object.getVolMin());
		form.setShipType(object.getShipType());//JSA01
		form.setWeightCheck("off");//JSA01
		form.setWeightUpdate(0D);//JSA01
		if (isSrvcExpress) {
			form.setWeight("");
		}
		form.setCmpyId(object.getCmpyId());//JSA03
		form.setLocationType(object.getLocationType());//AAP20
		
		/* Validaci?n Modificaci?n sin cambios */
		form.setEadOrigVal(object.getEad());
		form.setRadOrigVal(object.getRad());
		form.setExtOrigVal(object.getExt());
		form.setZoneOrigVal(object.getZone());
		form.setTarifOrigVal(object.getTarifa());
		form.setInsOrigVal(object.getValordeclarado());
		form.setInsOrigVal(object.getAcklabels());
	}

	public void fillValueObject(PrepaidValueObject object,ConversionForm conversionform) {
		if (conversionform.getOriginalClientID().length()>0)
			object.setClientId(conversionform.getOriginalClientID());
		else
		object.setClientId(conversionform.getClientId());
		object.setClientName(conversionform.getClientName());
		object.setFiscaladdresscode(conversionform.getFiscaladdresscode());
		object.setOrgioncode(conversionform.getOrgioncode());
		object.setNumberOfGuias(conversionform.getNumberOfGuias());
		object.setVolume(conversionform.getVolume());
		object.setWeight(conversionform.getWeight());
		object.setZone(conversionform.getZone());
		object.setTarifa(conversionform.getTarifa());
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
		
		
		// Agregado 17-Oct-2011 C.Solano CenitSoft/EnlaceDise?o
		object.setTarifa(conversionform.getTarifa());
		object.setEad(conversionform.getEad());
		object.setRad(conversionform.getRad());
		object.setExt(conversionform.getExt());
		object.setAcklabels(conversionform.getActType());
		object.setAckAmt(conversionform.getAct());
		object.setValordeclarado(conversionform.getIns());
		object.setDest_am_gety_code(conversionform.getDest_am_gety_code());
		
	}

	//Added on 17/01/2017 - For kitsId : ----- Ends JSA01, JSA02
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
	private void calculaTarifaSEG(ConversionForm conversionform, ConversionDao dao ) {
		Double pesoDouble, volumen;
		try {
			pesoDouble = Double.parseDouble(conversionform.getWeight().trim());
			volumen = Double.parseDouble(conversionform.getVolume().replace(',', '.'));
		} catch (Exception e) {
			pesoDouble = 0.0;
			volumen = 0.0;
		}
		if (pesoDouble > 0.0 ||  volumen > 0.0) {
			String tarifa = dao.getTipoTarifa(pesoDouble, volumen);
			if (tarifa.substring(0,2).equals("T7")) {
				String valorRegreso[] = null;
				valorRegreso = dao.getTarifaEnvio(conversionform.getZone(), tarifa, pesoDouble.toString(), volumen.toString(), "");
				if(!valorRegreso[1].isEmpty()) {
					tarifa = valorRegreso[1];
				}
			} 
		//System.out.println("TARIFA OLD "+conversionform.getTarifa()+" TARIFA NEW "+tarifa);	
			conversionform.setTarifa(tarifa);
		}
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
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("validaPedimento()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return validPedimento;
	}
}