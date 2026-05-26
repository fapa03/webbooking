/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creaci?n: 30/11/2011
 * Compa??a: PAQUETEXPRESS.
 * Descripci?n del programa: Bean accion para pantalla nueva de documentacion.
 * FileName: JavWebBookingGeneralMainAction.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP03
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 04/07/2013
 * Descripci?n: SE AGREG? CONDICION PARA VERIFICAR SI SE ENVIA A LA PANTALLA 
 * DE HISTORICO DE ENVIO, PARA CONSULTAR SI TIENE GUIAS DE FLETE POR COBRAR 
 * PARA GENERAR MANIFIESTO. SE AGREG? METODO consultaGuiasSinMnft()
 * ------------------------------------------------------------------
 * Clave: AAP04
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 
 * Descripci?n: SE OBTUVO MAIL ORIGEN PARA NOTIFICACIONES POR CORREO. 
 * ------------------------------------------------------------------
 * Clave: AAP05
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 02/08/2013
 * Descripci?n: SE OBTIENEN OBJETOS DE USUARIO FIRMADO Y COMBO DE CENTROS DE COSTO. 
 * 
 * ------------------------------------------------------------------ 
 * Clave: AAP06
 * Autor: ABRAHAM ARJONA
 * Fecha: 27/01/2015
 * Descripci?n: Modificaciones para calcular descuentos en servicios a tarifa "A"
 * 
 * ------------------------------------------------------------------ 
 */
package beanAction;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.paquetexpress.www.webbooking.Documentacion.ShipTypeSEG;

import bean.CentrosCosto;
import bean.ConsultaParametros;
import bean.Global;
import bean.Insurance;
import bean.JavTariff;
import bean.JavCatProducts;
import bean.Resources;
import bean.ShipmentServiceDetail;
import bean.SucursalesConfiguradas;
import beanForm.JavWebBookingGeneralMainForm;
import beanUtil.CargaInicialProductsSAT;
import beanUtil.ConnectDB;
import beanUtil.GetBrnchOcurre;
import logger.AccessLog;
import mx.com.paquetexpress.general.model.dto.BranchDetailDTOResponse;
import mx.com.paquetexpress.general.model.dto.SysSspDesMstrDTO;
import paquetexpress.internal.common.JavBranchRecords;

public class JavWebBookingGeneralMainAction extends Action {
	private StringBuffer concatena = new StringBuffer();
	private final String msgAvi  = concatena.delete(0, concatena.length()).append("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgErr  = concatena.delete(0, concatena.length()).append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	
	public JavWebBookingGeneralMainAction() {
		
	}
	
	@SuppressWarnings("rawtypes")
	public ActionForward perform(ActionMapping am, ActionForm af, 
								 HttpServletRequest req, HttpServletResponse res) {
		Connection con = null;
		String returnPage = "";
		
		Global global = null;
		String newDestinationSite = "";//aap01
		boolean nvaSucDest = false;/* bandera para indicar cuando la aplicacion tenga una sucursal destino nueva o sea la primera vez que se carga la pantalla de documentaci?n*/
		try{
			
			if (af instanceof JavWebBookingGeneralMainForm){
				
				JavWebBookingGeneralMainForm aform = (JavWebBookingGeneralMainForm) af;
				con = ConnectDB.getConnection();
				/* Se obtiene el catalogo de productos */
				CargaInicialProductsSAT.getInstance();
				HttpSession session = req.getSession(true);
				
				//	For No Session
				if(session==null || session.isNew()){
					returnPage = "nosession";
				}
				
				if (returnPage.length() == 0){
					global = (Global) session.getAttribute("sGlobal");
					if(global==null) {
						returnPage = "nosession";
					}
				}
				
				if (returnPage.equalsIgnoreCase("nosession")) {
					con.close();
					return am.findForward(returnPage);
				}
				/*metodo de validacion para salir de la aplicacion*/
				if (returnPage.length() == 0) {
					returnPage = salirApp(con,req, session, aform, global);//AAP03
					/*esta seccion se ejecuta cuando se hayan reiniciado los valores de la forma*/
					if (reloadForm) {
						aform = (JavWebBookingGeneralMainForm) session.getAttribute("webBookinggeneralMain");
					}
				}

				/*si la variable returnPage no trae valor, continua ejecutando los procesos de esta clase*/
				if (returnPage.length() == 0) {//returnPage 1
					//elimina el valor nulo de las variables en caso de que venga nulo
					aform.setDestinationcode( aform.getDestinationcode()==null?"":aform.getDestinationcode() );
					global.destinationBranchId = global.destinationBranchId == null?"":global.destinationBranchId;
					global.destinationSiteId = global.destinationSiteId == null?"":global.destinationSiteId;
					newDestinationSite = aform.getDestinationsitecode();
					//global.
					/*cuando entra la primera vez, las dos variables de sucursal destino estan vacias
					 * se obtiene la informacion del cliente origen para presentar en pantalla*/
					
					/*obtiene usuario firmado en aplicacion para mostrarlo en pantalla*/
					aform.setOrigenUserClave(global.getOrigenUserClave());
					aform.setOrigenUserNombre(global.getOrigenUserNombre());
					aform.setOrgienclave(global.getClientId());
					aform.setMaxQtyPack(getMaxQtyPackages(con));
					if (global.destinationSiteId.trim().length() == 0 && newDestinationSite.trim().length() == 0) {
						/*alimenta combo de centros de costo.*///AAP05
						CentrosCosto centrosCosto =  new CentrosCosto();//AAP05
						centrosCosto.fetchCentrosCosto(con, aform);//AAP05				
						
						aform.setClasifTarif(global.clasifTarif);//AAP02;bandera para validacion de clasificacion de tarifa nueva
						
						getOrgionClientAddressCC(aform, con, session);
						getOrgionFiscalClientAddress(aform, con, session);
						
						if (aform.getOrgionaddresscode() == null) {
							AccessLog.Log(msgAvi+"INFO SIN DIRECCION. cliente: "+global.getClientId()+" Usuario:" +aform.getOrigenUserClave()+" Centro de costo: "+aform.getCentrosCosto());
							getOrgionClientAddress(aform,con,session);							
							if (aform.getOrgionaddresscode() == null) {
								req.setAttribute("errormsgentrega", "Centro de costo sin dirección asignada.");
							}
						}

						nvaSucDest = true;
						/*bandera para mostrar las tarifas*/
						aform.setDisplayAmountFlag(global.displayAmountFlag);
						/*bandera de tipo de tarifa para validaciones en captura de detalle*/
						aform.setTarifType(global.tarifType.toUpperCase());
						aform.setForceCaptureDimensions(global.getForceCaptureDimensions());
						
						Insurance insurance = new Insurance();
						/*For Showing Delivery Type*/
						insurance.getServiceCovered(con,session,aform);
						//For Acknoledgement Type
						insurance.fetchAcknowledgementTypeDesc(con, aform, global);
						ConsultaParametros topeValorDeclarado = new ConsultaParametros();
						
						Double tope = Double.parseDouble(((ArrayList) topeValorDeclarado.QryMdulTypeParm1(con,"BOK","MAX_DECL_AMNT", "AMOUNT").get(0)).get(2).toString());
						aform.setMaxDeclAmnt(tope);
						if (!aform.getValordeclarado().isEmpty() &&
								Double.parseDouble(aform.getValordeclarado())>tope) {
							req.setAttribute("errorMsg", "Monto Máximo a Capturar de Valor Declarado para este Envio: "+tope);
						}
						insurance.getInsuranceAmount(con,session,aform,true);
						
					} else if (global.destinationSiteId.equals(newDestinationSite)) {
						//las sucursales destino actual y nueva son iguales (no cambi? de surcursal)
						//AAP//AccessLog.Log("las dos variables de sucursal destino son iguales (no cambi? de surcursal)...continuacion con captura de detalle");
						nvaSucDest = false;
						/*valida cambio de sucursal 70 (condiciones para poder realizar el cobro de ext cuando al mismo cliente le cambian de direccion en boton "destinos")*/
						if (!global.destinationBranchId.equals(aform.getDestinationcode())) {
							if ( (global.destinationBranchId.substring(3).equals("70") && !aform.getDestinationcode().substring(3).equals("70")) ||
									(!global.destinationBranchId.substring(3).equals("70") && aform.getDestinationcode().substring(3).equals("70"))) {
								nvaSucDest = true;
							}
						}
					} else if (global.destinationSiteId.length() == 0 && newDestinationSite.length() > 0) {
						//la sucursal destino nueva se acaba de asignar
						nvaSucDest = true;
						
						/*se habilita la bandera Action para presentar la informacion del detalle de la captura*/
						if (!aform.getAccion().equalsIgnoreCase("validaPedimento")) {
							aform.setAccion("init");
						}
					} else {
						//las sucursales destino actual y nueva son diferentes hay que recarga los datos de inicio para la captura de la nueva informacion
						aform.setBorderbranchcheck("");//For New Destination Border Branch
						removerAtributos(session, global);
						nvaSucDest = true;
						
						/*****************************************************************************************************************
						 * esta seccion se agreg? para obtener la informacion como si fuera la primera vez que entra a la aplicacion...  *
						 * para verificar el error de p?rdida de informacion															 *
						 *****************************************************************************************************************/
						getOrgionClientAddressCC(aform, con, session);
						/*bandera para mostrar las tarifas*/
						aform.setDisplayAmountFlag(global.displayAmountFlag);
						/*bandera de tipo de tarifa para validaciones en captura de detalle*/
						aform.setTarifType(global.tarifType.toUpperCase());
						
						Insurance insurance = new Insurance();
						/*For Showing Delivery Type*/
						insurance.getServiceCovered(con,session,aform);
						//For Acknoledgement Type
						insurance.fetchAcknowledgementTypeDesc(con, aform, global);
						
						insurance.getInsuranceAmount(con,session,aform,true);
						/*******************************************************************************************************************
						 * Fin de seccion para validar el error de perdida de informacion												   *
						 *******************************************************************************************************************/
						
					}
				}//fin de la condicion returnPage 1
				
				String changeCCosto = req.getParameter("changeCCosto") == null ? "": req.getParameter("changeCCosto");
				
				if (changeCCosto.equals("Y")) {
					nvaSucDest = true;
					getOrgionClientAddressCC(aform, con, session);	
					returnPage="";
					if (aform.getOrginbranchcode().contains("70") && aform.getAllowRadZe().equalsIgnoreCase("Y")) {
						aform.setShiperrmsg("La dirección origen es zona plus. Existe costo adicional por unidad.");
						
					}
					if (session != null){
						ArrayList servicesDetailArray = (ArrayList) session.getAttribute("servicesDetail");
						if (aform.getOrginbranchcode().contains("70") && existTariffServiceDtl(servicesDetailArray, "T7").equalsIgnoreCase("Y") &&
								aform.getAllowRadZeT7().equalsIgnoreCase("N")) {
							String dir = concatena.delete(0, concatena.length())
							.append("Calle ")
							.append(aform.getOrgien1()).append(" Num. ").append(aform.getOrgien2()).append(" ")
							.append(aform.getOrgiencolonia1()).append(", ").append(aform.getOrgiencolonia2()).append(".").toString();
							aform.setShiperrmsg("LA DIRECCION "+ dir +" DE RECOLECCION DEL CENTRO DE COSTO SELECCIONADO PERTENECE A ZONA PLUS.\nSIN AUTORIZACIÓN PARA ESTE TIPO DE DOCUMENTACIÓN.");
							String ccRespaldo = aform.getCentrosCosto();
							if (aform.getCentrosCostoValue().size()>1) {
								for (Object cc : aform.getCentrosCostoValue()) {
									if (!cc.toString().equalsIgnoreCase(aform.getCentrosCosto())) {
										ccRespaldo = cc.toString();
										break;
									}
								}
							}
							aform.setCentrosCosto(ccRespaldo);
							getOrgionClientAddressCC(aform, con, session);
						}
					}
					if (aform.getOrgionaddresscode() == null) {
						AccessLog.Log(msgAvi+"CAMBIO CENTRO DE COSTO INFO SIN DIRECCION. cliente: "+global.getClientId()+" Usuario:" +aform.getOrigenUserClave()+" Centro de costo: "+aform.getCentrosCosto());
						getOrgionClientAddress(aform,con,session);
						if (aform.getOrgionaddresscode() == null) {
							req.setAttribute("errormsgentrega", "Centro de costo sin dirección asignada.");
						}
					}
				}
				
				req.setAttribute("dirOrigenTitle", concatena.delete(0, concatena.length())
						.append("Direccion origen:\nCalle ")
						.append(aform.getOrgien1()).append(" Num. ").append(aform.getOrgien2()).append(" ")
						.append(aform.getOrgiencolonia1()).append(", ").append(aform.getOrgiencolonia2()).append(".").toString()
					);
				/*si la variable returnPage no trae valor continua, ejecutando los procesos de esta clase*/
				if (returnPage.length() == 0) {//returnPage 2
					
					/*se verifica si es tarifa de piso o tarifa pactada con el cliente*/
					verificaTarifas(con, aform, global);
					
					/*si es nueva sucursal la que se esta documentando. se ejecuta el siguiente bloque de codigo*/
					if (nvaSucDest) {//fin condicion nueva sucursal 1
						/*asigna la nueva sucursal destino a la variable global*/
						global.destinationBranchId = aform.getDestinationcode();	
						global.destinationSiteId = aform.getDestinationsitecode();
						
						/*se verifica si es tarifa de piso o tarifa pactada con el cliente*/
						verificaTarifas(con, aform, global);						
						/*se verifica si las sucursales origen y destino son fronterizas*/

							returnPage = verificaZonaFronteriza(con, aform, global);
						
						
						/*sube a sesion los atributos asignados a la variable "global" */
						session.setAttribute("sGlobal",global);
						//Aqui se llenan los tipos de envios.
						if(aform.getDestinationcode() != null && !aform.getDestinationcode().isEmpty()) {
							List<Object>  list = getTypeShipSEGALLActive(con, aform.getOrginbranchcode(), aform.getDestinationcode(), aform.getOrgienclave(), aform.getOrgionaddresscode());
							List<String> listStr= (ArrayList<String>) list.get(0);
							aform.setShippingTypeTO(listStr);
							aform.setShippingTypesName((ArrayList<String>)  list.get(1));
							if (aform.getShippingType() == null || aform.getShippingType().isEmpty()) {
								aform.setShippingType(listStr.get(listStr.size()-1));
							}
							aform.setShippingTypeSEGActive(getExclusiveTypeShipSEG(con));
							aform.setIsShippingTypeSEG(validateIsTypeSEG(listStr.get(listStr.size()-1), aform.getShippingTypeSEGActive()));
							aform.setShippingTypeSEGALL((List<ShipTypeSEG>) list.get(2));
						} else {
							aform.setShippingTypeTO(new ArrayList<String>());
							aform.setShippingTypesName(new ArrayList<String>());
							aform.setShippingType("");
							aform.setIsShippingTypeSEG("N");
						}
					}//fin condicion nueva sucursal 1
					
					/*Se agrego esta validacion para saber si se tiene cobertura del SEG*/
					if(!aform.getShippingType().isEmpty() && ((aform.getAccionServices() != null && !aform.getAccionServices().isEmpty() 
							&& (aform.getAccionServices().equalsIgnoreCase("checkbranch")) || aform.getAccionServices().equalsIgnoreCase("changeShippingType")) || nvaSucDest)) {
						aform.setIsShippingTypeSEG(validateIsTypeSEG(aform.getShippingType(), aform.getShippingTypeSEGActive()));
						if(!aform.getShippingType().equalsIgnoreCase("STD-T")) {
						    boolean cobertura = getCoverageSEG(con, aform.getDestinationcoloniacode(),  aform.getDestinationclave(),  aform.getDestinationaddresscode(), aform.getTypeSEGOperativa(aform.getShippingType()));
						    if(!cobertura) {
						    	if (!aform.getHitCount().isEmpty() && !aform.getHitCount().equalsIgnoreCase("0")) {
						    		req.setAttribute("errormsgentrega","No tenemos disponible este servicio \"" + aform.getTypeSEGOperativa(aform.getShippingType()) + " \" para este domicilio, solo se puede enviar a ocurre");
						    	}
			        			aform.setEntrega("1");
			        			aform.setMsjShippingCbtr("No se encuentra disponible el servicio para este domicilio, solo se puede realizar envios a ocurre"); 
			        		}else {
			        			aform.setMsjShippingCbtr(""); 
			        		}
						}else { 
							aform.setMsjShippingCbtr(""); 
						}
					}
					//seccion donde se setter el cliente agrupado del destino
					getGroupClientId(con, aform.getDestinationclave(), session);
					
					if (aform.getDestinationsitecode() != null&& !aform.getDestinationsitecode().isEmpty()) {
						String blockDocMsg = getBlockDocWW(con, aform.getOrgienclave(), global.assignedSite, aform.getDestinationzipcode());
						if (!"OK".equalsIgnoreCase(blockDocMsg)) {
							aform.setShiperrmsg(blockDocMsg);
							req.setAttribute("zeroexist",blockDocMsg);
							aform.setAccion("blockDoc");
						}
					}
					
					/*seccion para la captura de detalle*/
					if (aform.getAccion().length() != 0) {	
						
						if (!(global.getAssignedBranch() == null || global.getAssignedBranch().equals("")))  {//validacion de sucursal origen
							aform.setFlagCCAssignedBrnc("Y");
						} 
						
						if(aform.getFlagCCAssignedBrnc().equals("Y")) {//validacion de sucursal origen
							JavWebBookingGeneralMainServiceDetail detalle = new JavWebBookingGeneralMainServiceDetail();						
							returnPage = detalle.perform(aform, con, req, session, global);
						} else {
							/*reinicia detalle de servicio*/
							session.setAttribute("servicesDetail", new ArrayList());
							aform.setAccionServices("");//se limpia variable para que se vuelva a ejecutar procesos de validacion en captura de servicios.
							returnPage = "thispage";
						}		
					}
				}//fin de la condicion returnPage 2        
				/*	Si el cliente busca seleccionar sucursal */
				Boolean brnchCheck = false;
				if (aform.getAccionServices() != null && (aform.getAccionServices().equalsIgnoreCase("checkbranchtrue") || aform.getAccionServices().equalsIgnoreCase("initServicesOK"))
						&& !aform.getEntrega().equalsIgnoreCase("2")) {
					aform.setOpcOcurre(true);
					GetBrnchOcurre gbo = new GetBrnchOcurre();
					ArrayList servicesDetailArray = (ArrayList) session.getAttribute("servicesDetail");
					if (servicesDetailArray != null && !servicesDetailArray.isEmpty()) {
						String tokenApi = gbo.getToken(con);
						con.close();
						ArrayList<BranchDetailDTOResponse> sucursales = gbo.getBrnchsOcurre(tokenApi, aform.getDestinationsitecode(), gbo.getMaxDetail(servicesDetailArray));
						con = ConnectDB.getConnection();
						aform.getFilteredBrnch().clear();
						if (!sucursales.isEmpty()) {
							for (Object s : sucursales) {
								BranchDetailDTOResponse sucursal = gbo.mapToDTO((LinkedHashMap) s);
								if ((sucursal.getClave().equalsIgnoreCase(aform.getBrnchOcurre().split("\\|")[0]))) {
									brnchCheck = true;
								}
								aform.setFilteredBrnch(sucursal);
							}
							if (Boolean.FALSE.equals(brnchCheck)) {
								aform.setBrnchOcurre("");
							}
						}
					}
				}else if ((aform.getAccionServices() != null && (aform.getAccionServices().equalsIgnoreCase("checkbranchfalse") || aform.getAccionServices().equalsIgnoreCase("checkbranch") || aform.getAccionServices().equalsIgnoreCase("initServicesOK")))
						&& aform.getEntrega().equalsIgnoreCase("1")) { //Condici?n para llenar el combo y comparar contra default
					aform.setOpcOcurre(false);
					GetBrnchOcurre gbo = new GetBrnchOcurre();
					ArrayList servicesDetailArray = (ArrayList) session.getAttribute("servicesDetail");
					if (servicesDetailArray != null && !servicesDetailArray.isEmpty()) {
						String tokenApi = gbo.getToken(con);
						con.close();
						ArrayList<BranchDetailDTOResponse> sucursales = gbo.getBrnchsOcurre(tokenApi, aform.getDestinationsitecode(), gbo.getMaxDetail(servicesDetailArray));
						con = ConnectDB.getConnection();
						aform.getFilteredBrnch().clear();
						if (!sucursales.isEmpty()) {
							for (Object s : sucursales) {
								BranchDetailDTOResponse sucursal = gbo.mapToDTO((LinkedHashMap) s);
								if (!(sucursal.getClave().equalsIgnoreCase(aform.getBrnchOcurre().split("\\|")[0]))) {
									aform.setFilteredBrnch(sucursal);
									aform.setBrnchOcurre("");
								}
							}
						}
					}
					SucursalesConfiguradas suc = new SucursalesConfiguradas();
					String brncDefaultId = suc.obtieneConfigSucursal(con, "BOK", "DEST_OCURRE", aform.getDestinationsitecode());
					String addr = getBrncAddr(con, brncDefaultId);
					aform.setDefaultBrnchAddr(addr);
				} else if((aform.getAccionServices() != null && aform.getAccionServices().equalsIgnoreCase("checkbranchfalse"))
						|| aform.getEntrega().equalsIgnoreCase("2")) {
					aform.setOpcOcurre(false);
				}
				
				/* Validaci?n T7 = 0 */
				if (aform.getDefaultservicescreen() != null && aform.getDefaultservicescreen().equals("yes") 
						&& global != null && global.getTarifType() != null && global.getTarifType().equals("C")){
					JavTariff tarifaCheck = new JavTariff();
					double dPeso = 0;
					double dvolu = 0;
					try { dPeso = Double.parseDouble(aform.getPeso()); } catch (Exception e) { dPeso = 0; }
					try { dvolu = Double.parseDouble(aform.getVolumen()); } catch (Exception e) { dvolu = 0; }
					if (dPeso == 0 && dvolu == 0) {
						String tarifa = tarifaCheck.getTarrifDefaultByTarrifType_C(con, global.getClientIdAgreement());
						if (tarifa != null && !tarifa.equals("")) {
							aform.setTarifDefaultChck(true);
						}
					}
				}
				if (aform.getAccionServices() != null && aform.getAccionServices().equalsIgnoreCase("validateFxcCcp")
						|| aform.getAccionServices().equalsIgnoreCase("initServicesOK") || aform.getAccionServices().equalsIgnoreCase("generate")) {
					aform.setErrMsgAdditional("");
					ShipmentServiceDetail ssd = (ShipmentServiceDetail) session.getAttribute("ServicesDetail");
					ArrayList servicesDetailArray = (ArrayList) session.getAttribute("servicesDetail");
					int size = 0;
					if (servicesDetailArray != null && servicesDetailArray.size() > 0) {
							if (servicesDetailArray != null) {
								size = servicesDetailArray.size();
							}
							ssd = null;
							if (size > 0) {
								JavCatProducts catProducts = new JavCatProducts();
								String clntId = aform.getFormaPago().equalsIgnoreCase("TO_PAY") ? aform.getDestinationclave() : aform.getOrgienclave();
								String content = "FxcValidation";
								/* Obtiene productos condigurados del cliente destino */
								@SuppressWarnings("unchecked")
								ArrayList<SysSspDesMstrDTO> records = (ArrayList<SysSspDesMstrDTO>) catProducts.getLovRecords(con, clntId, content);
								String errorProd = "Las siguientes líneas necesitan cambiar de código de producto: \n";
								String errorProdDtl = "";
								int validProductCount = 0;
								StringBuilder sb = new StringBuilder();
								if (!records.isEmpty()) {
									for (int i = 0; i < size; i++) {
										ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
										for (int j = 0; j < records.size(); j++) {
											SysSspDesMstrDTO product = records.get(j);
											if (product.getCode().equalsIgnoreCase(ssd.getProductIdSat())
													&& content.equalsIgnoreCase("FxcValidation")){
												sb.append(ssd.getProductDescSat());
												validProductCount++;
											}
										}
										if (!errorProdDtl.contains(ssd.getProductDescSat()) &&
												!sb.toString().contains(ssd.getProductDescSat())) {
											errorProdDtl += ssd.getProductDescSat() + " \n";
										}
									}
								}
								if (validProductCount == size) {
									errorProdDtl = "";
								}
								if (aform.getFlagValidProductId().equalsIgnoreCase("generate") && 
										!aform.getFlagValidProductId().equalsIgnoreCase("N")) {
									aform.setAccionServices("generate");
								}
								if (!errorProdDtl.equals("")) {
									aform.setErrMsgAdditional(errorProd + errorProdDtl);
									aform.setFlagValidProductId("N");
								}else{
									aform.setFlagValidProductId("Y");
								}
								if (!aform.getDestinationrfc().equalsIgnoreCase("XAXXX-010101-000")) {
									String datoFiscalValido = getValidDatosFiscal(con, aform.getDestinationrfc());
									aform.setValidFiscal(datoFiscalValido);
									if (aform.getFormaPago().equalsIgnoreCase("TO_PAY") && !aform.getValidFiscal().equalsIgnoreCase("Y") && datoFiscalValido != null){
										aform.setErrMsgAdditional("Datos Fiscales de cliente destino no son validos, si desea corregirlos diríjase a la pantalla de  Registro de Cliente Destino, o presione Aceptar para continuar.");
										aform.setAccionServices("invalidFiscal");
									}
								}else {
									aform.setValidFiscal("Y");
								}
							}
					}
				}

				if (global.getAssignedBranch() == null || global.getAssignedBranch().equals(""))  {
					req.setAttribute("zeroexist", msgCCSinCobertura(con, aform,  global.getBrncNameValue())); 
					aform.setFlagCCAssignedBrnc("N");
				} 
				
				if (aform.getAccion() != null && (aform.getAccion().equalsIgnoreCase("validaPedimento") || aform.getAccionServices().equalsIgnoreCase("generate") 
						|| aform.getAccion().equalsIgnoreCase("serviceDetailOK") || aform.getAccionServices().equalsIgnoreCase("validateFxcCcp"))
						&&!aform.getPedinumber().isEmpty() && !aform.getErrMsgPediNum().isEmpty()) {
					String valResult = validaPedimento(con, aform.getPedinumber()); 
					if (!valResult.equalsIgnoreCase("OK")){
						aform.setErrMsgPediNum(valResult);
						if (!aform.getPedinumber().equalsIgnoreCase("error")) {
							aform.setLastWrongPediNum(aform.getPedinumber());
						}
						aform.setPedinumber("error");
						aform.setCustagent("");
					}else {
						aform.setLastWrongPediNum("");
					}
				}
			}
			
		} catch(Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("perform()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarConexion(con);
		}
		return am.findForward(returnPage);
	}
	
	private String getBlockDocWW(Connection con, String clntId, String destSite, String zipCode) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement("SELECT FUN_RSTCN_CLNT_CVGE(?,?,?) FROM DUAL");
			
			pst.setString(1, clntId);
			pst.setString(2, destSite);
			pst.setString(3, zipCode);
			rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getBlockDocWW()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return "";
	}
	
	/********************************************************************************************************
	 * m?todo para obtener la direcci?n de una sucursal		*
	 ********************************************************************************************************/
	private String getBrncAddr(Connection con, String brnchId) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		String query = "";
		String calle="";
		String num="";
		String col="";
		String colCP="";
		String ciudad="";
		String estado="";
		String lat="";
		String lon="";
		
		try {
			if (!brnchId.equals("")) {
				query = concatena.delete(0,concatena.length())
						.append("SELECT AM_STRT_NAME, AM_DRNR, COL_DES, COLO_ZIPCODE, CIUDAD, ESTADO, BM_LOCA_LAT, BM_LOCA_LON FROM SYS_ADDR_MSTR A ")
						.append("INNER JOIN PCOBERTURA_VIEW B ON A.AM_GETY_CODE = B.COD_COLO ")
						.append("INNER JOIN SYS_BRNC_MSTR C ON A.AM_ENTY_ID = C.BM_BRNC_ID ")
						.append("WHERE A.AM_ENTY_ID = ? AND AM_DEFA_FLAG = 'Y'").toString();		
				pst = con.prepareStatement(query);
				pst.setString(1, brnchId);
				
				rs = pst.executeQuery();
				
				while(rs.next()) {
                    calle = rs.getString(1) != null ? rs.getString(1) : "";
                    num = rs.getString(2) != null ? rs.getString(2) : "";
                    col = rs.getString(3) != null ? rs.getString(3) : "";
                    colCP = rs.getString(4) != null ? rs.getString(4) : "";
                    ciudad = rs.getString(5) != null ? rs.getString(5) : "";
                    estado = rs.getString(6) != null ? rs.getString(6) : "";
                    lat = rs.getString(7) != null ? rs.getString(7) : "";
                    lon = rs.getString(8) != null ? rs.getString(8) : "";
				}
			}
		}catch (Exception e){
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("perform()_Error:").append(e).toString());
			e.printStackTrace();
			resources.closeResources(rs,pst);
		} finally {
			resources.closeResources(rs,pst);
		}
		return calle +" "+num+" "+col+" "+colCP+" "+ciudad+" "+estado+"^"+lat+","+lon;
	}
	
	/********************************************************************************************************
	 * metodo para verificar si el cobro del envio es tarifa de piso o tarifa pactada con el cliente		*
	 ********************************************************************************************************/
	private void verificaTarifas(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		boolean tariffSlabExists = false;
		aform.setDefaultservicescreen("no");
		aform.setDefaultservicescreenKm("N");
		aform.setSrvcConfigHasAmntC(false);
		//AAP06
		
		try {
			/*se verifica si es tarifa de piso o tarifa pactada con el cliente*/
			tariffSlabExists = isTariffSlabExists(con,global);
			
			if (tariffSlabExists) {
				aform.setDefaultservicescreen("yes");	
			} else {
				aform.setDefaultservicescreen("no");
			}
			/*siempre se calcula kilometraje*/
			calculateKMTarifType(con,global);//AAP
			
			if (aform.getDefaultservicescreen().equals("no")) {
				tariffSlabExists = isTariffSlabExistsKm(con,global);
				if (tariffSlabExists) {
					aform.setDefaultservicescreen("yes");
					aform.setDefaultservicescreenKm("Y");
				}
			}	
			
			if (aform.getDefaultservicescreen().equals("yes")) {	aform.setSrvcConfigHasAmntC(verificaTipoTarifaC(con, global));	}

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("verificaTarifas()_Error:").append(e).toString());
			e.printStackTrace();
		}
	}
	
	/********************************************************************************************************
	 * metodo para verificar si que tipo de tarifa contiene el Cliente del Tipo C 							*
	 ********************************************************************************************************/
	private boolean verificaTipoTarifaC(Connection con, Global global) {

		boolean packets = false;
		String query = "";
		String tipoTarifaC = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			if (global.tarifType.equalsIgnoreCase("c")) {
				// El query verifica si el tipo de cliente C corresponde a una tarifa anterior o nueva
				query = "SELECT * FROM (SELECT WC_CLNT_ID,'PTP' TIPO FROM WEB_CLNT_MSTR WCM INNER JOIN WEB_CLNT_SRVC_TRIF TRIF ON WCM.WC_CLNT_ID = TRIF.WT_ORGN_CLNT_ID AND NOT EXISTS (SELECT * FROM WEB_CLNT_SRVC_TRIF_FACTOR FCTR WHERE FCTR.WCP_ORGN_CLNT_ID = TRIF.WT_ORGN_CLNT_ID) WHERE WCM.WC_CLNT_ID = ? AND WC_TRIF_TYPE = 'C' UNION SELECT WC_CLNT_ID, 'KM' TIPO FROM WEB_CLNT_MSTR WCM INNER JOIN WEB_CLNT_SRVC_TRIF_KM TRIF_KM ON WCM.WC_CLNT_ID = TRIF_KM.WT_ORGN_CLNT_ID AND NOT EXISTS (SELECT * FROM WEB_CLNT_SRVC_TRIF_FACTOR_KM FCTR_KM WHERE FCTR_KM.WCK_ORGN_CLNT_ID = TRIF_KM.WT_ORGN_CLNT_ID) WHERE WCM.WC_CLNT_ID = ? AND WC_TRIF_TYPE = 'C')";
				pst = con.prepareStatement(query);
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, global.getClientIdAgreement());
				rs = pst.executeQuery();
				if (rs.next()) {
					tipoTarifaC = rs.getString(2);
				}
				rs.close();
				pst.close();

				if (tipoTarifaC.equals("")) {
					packets = true;
				}
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr)
					.append("importeTarifaEspecial()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return packets;
	}

	/********************************************************************************************************
	 * metodo para verificar si las sucursales origen y destino son fronterizas								*
	 ********************************************************************************************************/
	private String verificaZonaFronteriza(Connection con, JavWebBookingGeneralMainForm aform, Global global){
		String returnPage = "thispage";
		try {
			if (aform.getDestinationsitecode().trim().length() >0) {
				global.setOrigionBorderBranch(branchLocationType(con, global.getAssignedBranch()));//AAP20
				global.setDestinationBorderBranch(branchLocationType(con, global.getDestinationBranchId()));//AAP20
				/*se verifica si las sucursales son fronterizas*/
				if (global.getDestinationBorderBranch().substring(0, 2).equals("BR")) {//AAP20
					global.isDestinationBorderBranch=true;
				} else {
					global.isDestinationBorderBranch=false;
				}
				
				if (global.getOrigionBorderBranch().substring(0, 2).equals("BR") ) {
					global.isOrigionBorderBranch = true;
				} else {
					global.isOrigionBorderBranch = false;
				}
				
				/*verifica si la sucursal origen es fronteriza y la sucursal destino no es fronteriza 
				 * para levantar la pantalla de solicitud de pedimento y agente aduanal*/
				if (global.isOrigionBorderBranch && !global.isDestinationBorderBranch) {
					
					/*activa bandera para mostrar pantalla*/
					aform.setBorderbranchcheck("true");
					
					/*realiza el retorno de la pagina para capturar los datos de aduana (trunca los siguientes procesos a ejecutar de la clase) */
					returnPage = "thispage";
				}				
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("verificaZonaFronteriza()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return returnPage;
	}
	
	private String salirApp(Connection con, HttpServletRequest req, HttpSession session, JavWebBookingGeneralMainForm aform, Global global) {
		
		String returnPage = "";
		String includeattribute = "";
		
		try {
			//Handling of request coming from Include Page link.
			includeattribute = req.getParameter("includeattribute");
			/*
			 * Posibles valores para parametro "page"
			 * clientreport = para enviar a reporte de clientes
			 * shipmenthistory = para enviar al historial de envios
			 * cliententry = para enviar al catalogo de clientes
			 * mainpage = envia a la pagina principal
			 * thispage = envia a esta misma pagina.
			 * guiacancel = envia a la cancelacion de guias 
			 * guiabooking (desde clientDestinationEntry) envia desde otras opciones a la opcion de documentacion
			 */
			String page = req.getParameter("page") == null ? "" : req.getParameter("page");
			if(includeattribute !=null && includeattribute.equalsIgnoreCase("yes")) {
				if (page.equals("shipmenthistory")) {//AAP03
					String clientId = (String)session.getAttribute("sClientId");//AAP03
					String branchId = (String)session.getAttribute("sAssignedBranch");//AAP03
					String to_pay = consultaGuiasSinMnft(con, clientId, branchId, "TO_PAY");//AAP03					
					req.setAttribute("formaPago", to_pay);//AAP03
				}
				
				removerAtributos(session, global);
				/*no envia parametro "page" desde el boton de documentacion de una nueva guia, se reinician los valores*/
				if (page.length() != 0 && !page.equalsIgnoreCase("thispage")) {
					/*si el parametro page trae valor diferente de "thispage", esta enviando a otras opciones del men? 
					 * ya no se ejecutan los demas procesos de esta clase. 
					 */
					returnPage = page;	
				}
				session.removeAttribute("webBookinggeneralMain");
				aform = reiniciaFormaPrincipal(aform);
				session.setAttribute("webBookinggeneralMain",aform);
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("salirApp()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return returnPage;
	}
	
	
	private void getOrgionClientAddress(JavWebBookingGeneralMainForm aform,Connection con,
									   HttpSession session) {
		
		CallableStatement cst = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Global global = new Global();
		String query = "";
		try {
			global = (Global)session.getAttribute("sGlobal");
			String strClientId = global.clientId;		
			String strAssignedSite=global.assignedSite;
			String strAssignedBranch=global.assignedBranch;
			
			aform.setOrgiennombre(global.clientName);
			aform.setOrgienrfc(global.rfc);
			
			query =	concatena.delete(0,concatena.length())
					.append("SELECT AM_ADDR_CODE,AM_DRNR,AM_STRT_NAME, ")
					.append("AM_PHNO1,AM_SUIT_NO,AM_FLOR_NO,AM_ADDR_STYP,AM_ADDR_DEFN_TYPE,")
					.append("AM_ADDR_REF_NO,AM_GETY_LEVL,AM_GETY_TYPE,AM_GETY_CODE, AM_MAIL_ID ")//AAP04
					.append("FROM SYS_ADDR_MSTR WHERE AM_PE_SITE_ID=? ")
					.append("AND AM_ENTY_ID=? AND AM_ADDR_TYPE=? AND AM_DEFA_FLAG = ? ").toString();	
			pst = con.prepareStatement(query);
			
			pst.setString(1,strAssignedSite);
			pst.setString(2,strClientId);
			pst.setString(3,"CLNT");
			pst.setString(4,"Y");
			
			rs=pst.executeQuery();
			
			String	AM_ADDR_CODE=null,AM_DRNR=null,AM_STRT_NAME=null,AM_PHNO1=null;//
			String	AM_ADDR_DEFN_TYPE=null,AM_ADDR_REF_NO=null,AM_GETY_LEVL=null,AM_GETY_TYPE=null,AM_GETY_CODE=null;
			//code added on 29/03/2004
			String orignCityCode=null, AM_MAIL_ID = "";//AAP04
			
			while (rs.next()) {
				AM_ADDR_CODE=rs.getString(1);			
				AM_DRNR=rs.getString(2);
				AM_STRT_NAME=rs.getString(3);
				AM_PHNO1=rs.getString(4);
				AM_ADDR_DEFN_TYPE=rs.getString(8);
				AM_ADDR_REF_NO=rs.getString(9);
				AM_GETY_LEVL=String.valueOf(rs.getInt(10));
				AM_GETY_TYPE=String.valueOf(rs.getInt(11));
				AM_GETY_CODE= rs.getString(12);
				AM_MAIL_ID = rs.getString(13) == null ? "" : rs.getString(13);//AAP04
			}
			
			if ((AM_GETY_LEVL == null || "0".equals(AM_GETY_LEVL))||
					(AM_GETY_TYPE == null || "0".equals(AM_GETY_TYPE))) {
				aform.setValidCCAddrCvge("N");
				return;
			}
			rs.close();
			pst.close();
	
			cst = con.prepareCall("{ call pack_web.pro_ftch_addr(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
			
			cst.setString(1,AM_ADDR_DEFN_TYPE);
			cst.setString(2,AM_ADDR_REF_NO);
			cst.setString(3,AM_GETY_CODE);
			cst.setInt(4,Integer.parseInt(AM_GETY_LEVL));
			cst.setInt(5,Integer.parseInt(AM_GETY_TYPE));
			cst.registerOutParameter(6, Types.VARCHAR);
			cst.registerOutParameter(7, Types.VARCHAR);
			cst.registerOutParameter(8, Types.VARCHAR);
			cst.registerOutParameter(9, Types.VARCHAR);//ciudad
			cst.registerOutParameter(10, Types.VARCHAR);//delegacion
			cst.registerOutParameter(11, Types.VARCHAR);//descripcin colonia
			cst.registerOutParameter(12, Types.VARCHAR);
			cst.registerOutParameter(13, Types.VARCHAR);//codigo postal
			cst.registerOutParameter(14, Types.VARCHAR);
			cst.registerOutParameter(15, Types.VARCHAR);
			cst.registerOutParameter(16, Types.VARCHAR);
			cst.registerOutParameter(17, Types.VARCHAR);//id ciudad
			cst.registerOutParameter(18, Types.VARCHAR);//id delegacion
			cst.registerOutParameter(19, Types.VARCHAR);//id colonia
			
			cst.executeQuery();
			
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
			aform.seteMailOrigBD(AM_MAIL_ID);//AAP04
			
			
			if ((aform.getFiscal1()!=null) && (aform.getFiscal1().length()>0)) {//Changes done by B.Emerson on 29/03/2004 in order to have the latest fiscal address
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
			
			cst.close();
			rs.close();
			pst.close();
			
			query = "select pack_web.fun_ftch_brnc_name(?) from dual";
			pst = con.prepareStatement(query);
			pst.setString(1,global.assignedBranch);
			rs = pst.executeQuery();
			
			String branchName = "";			
			if(rs.next()) {
				branchName = rs.getString(1);
			}
			
			rs.close();
			pst.close();
			
			query = "select pack_web.fun_ftch_site_name(?) from dual";
			pst = con.prepareStatement(query);
			pst.setString(1,global.assignedSite);
			
			rs = pst.executeQuery();
			
			String siteName = "";			
			if(rs.next()) {
				siteName = rs.getString(1);
			}
			
			aform.setOrginbranchcode(strAssignedBranch);
			aform.setOrgionbranch(branchName);
			aform.setOrginsite(siteName);	
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getOrgionClientAddress()_Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.closeResources(rs,pst,cst);
		}
	}
	

	private void getOrgionClientAddressCC(JavWebBookingGeneralMainForm aform,Connection con, HttpSession session) {

		ResultSet rs = null;
		PreparedStatement pst = null;
		Global global = new Global();
		String query = "";
		try {
			global = (Global) session.getAttribute("sGlobal");
			String strClientId = global.getClientId() != null ? global.getClientId() : "";
			String strAssignedSite = "";
			String strAssignedBranch = "";
			String desColonia = "";
			String desCiudad = "";
			String strAssignedBranchExcept = ""; 

			aform.setOrgiennombre(global.getClientName());
			aform.setOrgienrfc(global.getRfc());

			query = concatena
					.delete(0, concatena.length())
					.append("SELECT AM_ADDR_CODE,AM_DRNR,AM_STRT_NAME, ")
					.append("AM_PHNO1,AM_SUIT_NO,AM_FLOR_NO,AM_ADDR_STYP,AM_ADDR_DEFN_TYPE, ")
					.append("AM_ADDR_REF_NO,AM_GETY_LEVL,AM_GETY_TYPE,AM_GETY_CODE, AM_MAIL_ID, CC_BRNC_ORGN, CC_CCOSTO_NAME ")
					.append("FROM SYS_ADDR_MSTR, SYS_CLNT_CCOSTO WHERE AM_ENTY_ID = ? and CC_CLNT_ID = ? ")
					.append("AND CC_CCOSTO_ID = ? and AM_ADDR_CODE = CC_ADDR_CODE ")
					.toString();
			
			pst = con.prepareStatement(query);
			
			pst.setString(1, strClientId);
			pst.setString(2, strClientId);
			pst.setString(3, aform.getCentrosCosto());
			
			rs = pst.executeQuery();

			String AM_ADDR_CODE = null, AM_DRNR = null, AM_STRT_NAME = null, AM_PHNO1 = null;
			String AM_GETY_LEVL = null, AM_GETY_TYPE = null, AM_GETY_CODE = null;
			// code added on 29/03/2004
			String orignCityCode = null, AM_MAIL_ID = "";// AAP04

			while (rs.next()) {
				AM_ADDR_CODE = rs.getString(1);
				AM_DRNR = rs.getString(2);
				AM_STRT_NAME = rs.getString(3);
				AM_PHNO1 = rs.getString(4);				
				AM_GETY_LEVL = String.valueOf(rs.getInt(10));
				AM_GETY_TYPE = String.valueOf(rs.getInt(11));
				AM_GETY_CODE = rs.getString(12);
				AM_MAIL_ID = rs.getString(13) == null ? "" : rs.getString(13);
				strAssignedBranchExcept= rs.getString(14) == null ? "" : rs.getString(14);
				global.setBrncNameValue(rs.getString(15) == null ? "" : rs.getString(15));
			}
			
			if ((AM_GETY_LEVL == null || "0".equals(AM_GETY_LEVL))||
					(AM_GETY_TYPE == null || "0".equals(AM_GETY_TYPE))) {
				aform.setValidCCAddrCvge("N");
				return;
			}
			
			global.setAssignedBranch(strAssignedBranchExcept);
			global.setAssignedSite(strAssignedBranchExcept.trim().length() == 0 ? "" : strAssignedBranchExcept.substring(0,3));
			strAssignedSite= global.getAssignedSite();
			strAssignedBranch = global.getAssignedBranch();
				
			rs.close();
			pst.close();
			
			query = "SELECT PLAZA, SUCURSAL, COL_DES, COD_CIUD, CIUDAD, COD_DELE, DELMUN FROM PCOBERTURA_VIEW WHERE COL_LEVL = ? AND COL_TYPE = ? AND COD_COLO = ? AND SUCURSAL IS NOT NULL";			
			pst = con.prepareStatement(query);
			
			pst.setString(1, AM_GETY_LEVL);
			pst.setString(2, AM_GETY_TYPE);
			pst.setString(3, AM_GETY_CODE);
			
			rs = pst.executeQuery();
			
			
			if (rs.next()) {				
				global.setAssignedSite(rs.getString(1));
				if (strAssignedBranchExcept.trim().length()>0) {
					global.setAssignedBranch(strAssignedBranchExcept);
					global.setAssignedSite(global.getAssignedBranch().substring(0,3));
				} else {
					global.setAssignedBranch(rs.getString(2));
				}
				
				desColonia = rs.getString(3);
				orignCityCode = rs.getString(4);
				desCiudad = rs.getString(5);				
				strAssignedSite = global.getAssignedSite();
				strAssignedBranch = global.getAssignedBranch();
			} 
			
			
			
			if (global.getAssignedBranch().trim().length()>0) {
				aform.setFlagCCAssignedBrnc("Y");
			} else {
				aform.setFlagCCAssignedBrnc("N");
			}
			
			//valida si la sucursal viene vacia, para activar oo no el flag correspondiente 

			aform.setOrginCityCode(orignCityCode);
			aform.setOriginColinaCode(AM_GETY_CODE);
			// From Session
			aform.setOrgioncode(strAssignedSite);
			aform.setOrgienclave(strClientId);
			// From the Resultset of the first query
			aform.setOrgien1(AM_STRT_NAME);
			aform.setOrgien2(AM_DRNR);
			aform.setOrgientelefono(AM_PHNO1);
			aform.setOrgionaddresscode(AM_ADDR_CODE);
			aform.seteMailOrigBD(AM_MAIL_ID);		
			
			if ((aform.getFiscal1() != null)
					&& (aform.getFiscal1().length() > 0)) {
			} else {
				// From the Resultset of the first query
				aform.setFiscal1(AM_STRT_NAME);
				aform.setFiscal2(AM_DRNR);
				aform.setFiscaltelefono(AM_PHNO1);
				aform.setFiscaladdresscode(AM_ADDR_CODE);

				// From the Stored Procedure output.
				aform.setFiscalcolonia1(desColonia);
				aform.setFiscalcolonia2(desCiudad);
			}

			// From the Stored Procedure output.
			aform.setOrgiencolonia1(desColonia);
			aform.setOrgiencolonia2(desCiudad);

			rs.close();
			pst.close();
			
			query = "select pack_web.fun_ftch_brnc_name(?) from dual";
			pst = con.prepareStatement(query);
			pst.setString(1, global.getAssignedBranch());
			rs = pst.executeQuery();

			String branchName = "";
			if (rs.next()) {
				branchName = rs.getString(1);
			}

			rs.close();
			pst.close();

			query = "select pack_web.fun_ftch_site_name(?) from dual";
			pst = con.prepareStatement(query);
			pst.setString(1, global.getAssignedSite());

			rs = pst.executeQuery();

			String siteName = "";
			if (rs.next()) {
				siteName = rs.getString(1);
			}

			aform.setOrginbranchcode(strAssignedBranch);
			aform.setOrgionbranch(branchName);
			aform.setOrginsite(siteName);
			aform.setAllowRadZe(global.getAcceptRadZp());
			aform.setAllowRadZeT7(global.getAcceptRadZpT7());
			session.setAttribute("sGlobal", global);//asignacion de sGlobal por nuevos site y sucursal asignados.
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length())
					.append(msgErr).append("getOrgionClientAddressCC()_Error:")
					.append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
	}
	
	private void getOrgionFiscalClientAddress(JavWebBookingGeneralMainForm aform,
			Connection con, HttpSession session) {

		CallableStatement cst = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Global global = new Global();
		String query = "";
		try {
			global = (Global) session.getAttribute("sGlobal");
			String strClientId = global.clientId;
			
			aform.setOrgiennombre(global.clientName);
			aform.setOrgienrfc(global.rfc);

			query = concatena
					.delete(0, concatena.length())
					.append("SELECT AM_ADDR_CODE,AM_DRNR,AM_STRT_NAME, ")
					.append("AM_PHNO1,AM_SUIT_NO,AM_FLOR_NO,AM_ADDR_STYP,AM_ADDR_DEFN_TYPE,")
					.append("AM_ADDR_REF_NO,AM_GETY_LEVL,AM_GETY_TYPE,AM_GETY_CODE, AM_MAIL_ID ")
					// AAP04
					.append("FROM SYS_ADDR_MSTR WHERE AM_ADDR_STYP=? ")
					.append("AND AM_ENTY_ID=? AND AM_ADDR_TYPE=? AND AM_DEFA_FLAG = ? ")
					.toString();
			// changed query for dc AM_PE_SITE_ID=? instead of AM_PE_brnc_ID=?
			// amaladoss
			pst = con.prepareStatement(query);
			
			pst.setString(1, "FISCAL");
			pst.setString(2, strClientId);
			pst.setString(3, "CLNT");
			pst.setString(4, "Y");

			rs = pst.executeQuery();

			String AM_ADDR_CODE = null, AM_DRNR = null, AM_STRT_NAME = null, AM_PHNO1 = null;
			String AM_ADDR_DEFN_TYPE = null, AM_ADDR_REF_NO = null, AM_GETY_LEVL = null, AM_GETY_TYPE = null, AM_GETY_CODE = null;		

			while (rs.next()) {
				AM_ADDR_CODE = rs.getString(1);
				AM_DRNR = rs.getString(2);
				AM_STRT_NAME = rs.getString(3);
				AM_PHNO1 = rs.getString(4);
				AM_ADDR_DEFN_TYPE = rs.getString(8);
				AM_ADDR_REF_NO = rs.getString(9);
				AM_GETY_LEVL = String.valueOf(rs.getInt(10));
				AM_GETY_TYPE = String.valueOf(rs.getInt(11));
				AM_GETY_CODE = rs.getString(12);
			}

			resources.closeResources(rs, pst);
			
			boolean continuar = false;
			try {
				Integer.parseInt(AM_GETY_LEVL);
				continuar = true;
			} catch (Exception e) {
				continuar = false;
			}
			
			if (continuar) {
				cst = con.prepareCall("{ call pack_web.pro_ftch_addr(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

				cst.setString(1, AM_ADDR_DEFN_TYPE);
				cst.setString(2, AM_ADDR_REF_NO);
				cst.setString(3, AM_GETY_CODE);
				cst.setInt(4, Integer.parseInt(AM_GETY_LEVL));
				cst.setInt(5, Integer.parseInt(AM_GETY_TYPE));
				cst.registerOutParameter(6, Types.VARCHAR);
				cst.registerOutParameter(7, Types.VARCHAR);
				cst.registerOutParameter(8, Types.VARCHAR);
				cst.registerOutParameter(9, Types.VARCHAR);// ciudad
				cst.registerOutParameter(10, Types.VARCHAR);// delegacion
				cst.registerOutParameter(11, Types.VARCHAR);// descripcin colonia
				cst.registerOutParameter(12, Types.VARCHAR);
				cst.registerOutParameter(13, Types.VARCHAR);// codigo postal
				cst.registerOutParameter(14, Types.VARCHAR);
				cst.registerOutParameter(15, Types.VARCHAR);
				cst.registerOutParameter(16, Types.VARCHAR);
				cst.registerOutParameter(17, Types.VARCHAR);// id ciudad
				cst.registerOutParameter(18, Types.VARCHAR);// id delegacion
				cst.registerOutParameter(19, Types.VARCHAR);// id colonia

				cst.executeQuery();

				// From the Resultset of the first query
				aform.setFiscal1(AM_STRT_NAME);
				aform.setFiscal2(AM_DRNR);
				aform.setFiscaltelefono(AM_PHNO1);
				aform.setFiscaladdresscode(AM_ADDR_CODE);

				// From the Stored Procedure output.
				aform.setFiscalcolonia1(cst.getString(11));
				aform.setFiscalcolonia2(cst.getString(9));

				cst.close();
				rs.close();
				pst.close();
			}
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length())
					.append(msgErr).append("getOrgionFiscalClientAddress()_Error:")
					.append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst, cst);
		}
	}
	
	private boolean isTariffSlabExists(Connection con, Global global) {
		boolean exist = false;
		String tariffSlab = "";
		String query = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			query = "select pack_web.fun_ftch_trif_exist(?,?,?,?) from dual";
			
			pst = con.prepareStatement(query);
			
			pst.setString(1, global.getClientIdAgreement());
			pst.setString(2, global.getAssignedBranch());
			pst.setString(3, global.getDestinationBranchId());
			pst.setString(4, global.getTarifType());
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				tariffSlab = rs.getString(1);
				global.tariffSlab = tariffSlab;
			}
			
			if (tariffSlab.equalsIgnoreCase("Y")) {
				exist = true;
			}			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("isTariffSlabExists()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return exist;
	}
	
	private boolean isTariffSlabExistsKm(Connection con, Global global) {
		boolean exist = false;
		String tariffSlab = "";
		String query = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			query = "select pack_web.fun_ftch_trif_exist_km(?,?,?) from dual";
			
			pst = con.prepareStatement(query);
			
			pst.setString(1, global.getClientIdAgreement());
			pst.setString(2, global.getKmTarifType());
			pst.setString(3, global.getTarifType());
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				tariffSlab = rs.getString(1);
				global.setTariffSlab(tariffSlab);
			}
			
			if (tariffSlab.equalsIgnoreCase("Y")){
				exist = true;
			}			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("isTariffSlabExistsKm()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return exist;
	}
	
	private void calculateKMTarifType(Connection con, Global global){
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "";
		String kmTarifType="";
		try {
			query = "select pack_web.FUN_CHK_ACTV_TRIF(?,?) from dual";
			pst = con.prepareStatement(query);
			pst.setString(1,global.assignedBranch);
			pst.setString(2,global.destinationBranchId);
			rs = pst.executeQuery();
						
			if(rs.next()){	
				kmTarifType = rs.getString(1);
			}		
			global.kmTarifType = kmTarifType;
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("calculateKMTarifType()_Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.closeResources(rs, pst);
		}
	}
	
	private void removerAtributos(HttpSession session, Global global) {
		try {			
			/*mantiene el detalle de los envios*/
			session.removeAttribute("servicesDetail");
			/**/
			session.removeAttribute("envelopealone");
			session.removeAttribute("aditionalServicesDetail");
			global.commentText = "";
			global.tariffSlab = "";
			global.kmTarifType = null;
			global.destinationBranchId = "";
			global.destinationSiteId = "";//aap01
			session.setAttribute("sGlobal",global);
			
			/* variables que mantendr?n los servicios calculados del envio*/
			session.removeAttribute("calculatedservicelist");
			session.removeAttribute("servicestotal");
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("removerAtributos()_Error:").append(e).toString());
			e.printStackTrace();
		}
	}
	
	boolean reloadForm = false;
	private JavWebBookingGeneralMainForm reiniciaFormaPrincipal(JavWebBookingGeneralMainForm aform){
		JavWebBookingGeneralMainForm aformnew = new JavWebBookingGeneralMainForm();
		try {
			aformnew.setOrgiennombre(aform.getOrgiennombre());
			aformnew.setOrgienrfc(aform.getOrgienrfc());
			aformnew.setOrginCityCode(aform.getOrginCityCode());
			aformnew.setOriginColinaCode(aform.getOriginColinaCode());	
			
			aformnew.setOrgioncode(aform.getOrgioncode());
			aformnew.setOrgienclave(aform.getOrgienclave());		
			
			aformnew.setOrgien1(aform.getOrgien1());
			aformnew.setOrgien2(aform.getOrgien2());
			aformnew.setOrgientelefono(aform.getOrgientelefono());
			aformnew.setOrgionaddresscode(aform.getOrgionaddresscode());
			aformnew.setFiscal1(aform.getFiscal1());
			aformnew.setFiscal2(aform.getFiscal2());
			aformnew.setFiscaltelefono(aform.getFiscaltelefono());
			aformnew.setFiscaladdresscode(aform.getFiscaladdresscode());
			
			//From the Stored Procedure output.
			aformnew.setFiscalcolonia1(aform.getFiscalcolonia1());
			aformnew.setFiscalcolonia2(aform.getFiscalcolonia2());
			aformnew.setOrgiencolonia1(aform.getOrgiencolonia1());
			aformnew.setOrgiencolonia2(aform.getOrgiencolonia2());
			aformnew.setOrginbranchcode(aform.getOrginbranchcode());
			aformnew.setOrgionbranch(aform.getOrgionbranch());
			aformnew.setOrginsite(aform.getOrginsite());
			
			reloadForm = true;
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("reiniciaFormaPrincipal()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return aformnew;
	}
	
		
	public String consultaGuiasSinMnft(Connection con, String clientId, String branchId, String pymtMode) {//AAP03
		
		String result = "";
		String query= "SELECT GH_PYMT_MODE FROM bok_guia_head WHERE gh_orgn_brnc_id = ? AND gh_orgn_clnt_id = ? AND gh_guia_refr_no IS NULL AND GH_GUIA_TYPE = ? and GH_ACTV_FLAG = ? AND GH_RAD_FLAG IN (?, ?) AND GH_PYMT_MODE = ? AND ROWNUM = ?";
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = con.prepareStatement(query);
			
			psmt.setString(1, branchId);
			psmt.setString(2, clientId);
			psmt.setString(3, "H");
			psmt.setString(4, "A");
			psmt.setString(5, "1");			
			psmt.setString(6, "6");
			psmt.setString(7, pymtMode);
			psmt.setString(8, "1");
			
			rs = psmt.executeQuery();
			
			if (rs.next()){						
				result = rs.getString(1) == null ? "" :rs.getString(1);				
			}	
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("consultaGuiasSinMnft()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, psmt);
		}
		return result;
	}	

	public List<Object> getTypeShipSEGALLActive(Connection con, String orgnBrncId, String destBrncID, String clienteID, String addrCode) {
		PreparedStatement pst = null;
		ResultSet rst = null;
		List<ShipTypeSEG> listSEG = null;
		List<Object> list = null;
		List<String> listSrvcId= null;
		List<String> listSrvcName = null;
		List<String> listSrvcRefr = null;
		try {
			String str = "SELECT PM_VLUE1_DESC,PM_PARM_CODE1 ,PM_PARM_CODE2 ,sactivos, sm_refr_srvc_id from (SELECT PM_VLUE1_DESC,PM_PARM_CODE1, PM_PARM_CODE2, nvl(CASE WHEN PM_PARM_CODE1 = 'STD-T' THEN 1 WHEN PM_PARM_CODE1 NOT IN ('STD-T') THEN FUN_IS_CORTE_SEG_CUSTOMER(PM_PARM_CODE1, :GH_ORGN_BRNC_ID, :GH_DEST_BRNC_ID, :CLIENTEID, :ADDRCODE) END, 0) sactivos FROM SYS_PARM_MSTR WHERE PM_PARM_TYPE =  'SRVC_LOG' ) INNER JOIN SYS_SRVC_MSTR SSM ON PM_PARM_CODE1 = ssm.sm_srvc_id where sactivos = 1 AND PM_PARM_CODE1 NOT IN (SELECT PM_PARM_CODE1 FROM (SELECT CASE WHEN COUNT(*)>0 THEN '1' ELSE '2' END ACTIVO FROM GLP_CUSTOMER_CTRL GCC WHERE (GCC.C_CLNT_ID = :CLIENTEID ) AND C_PARM_TYPE='ISSEG' AND gcc.c_actv_flag = 'A' AND C_DELETED_FLAG ='N'), SYS_PARM_MSTR WHERE PM_PARM_TYPE = 'SRVC_LOG' AND PM_PARM_CODE1 != 'STD-T' AND ACTIVO ='2')";
			str = str.replace(":GH_ORGN_BRNC_ID", "'" + orgnBrncId + "'");
			str = str.replace(":GH_DEST_BRNC_ID", "'" + destBrncID + "'");
			str = str.replace(":CLIENTEID", "'" + clienteID + "'");
			str = str.replace(":ADDRCODE", "'" + addrCode + "'");
			pst = con.prepareStatement(str);
			rst = pst.executeQuery();
			listSEG = new ArrayList<ShipTypeSEG>();
			listSrvcId = new ArrayList<String>();
			listSrvcName = new ArrayList<String>();
			listSrvcRefr = new ArrayList<String>();
			ShipTypeSEG shipTypeSEG = null;
			while (rst.next()) {
				listSrvcId.add(rst.getString(2));
				listSrvcName.add(rst.getString(1));
				listSrvcRefr.add(rst.getString(5));
				shipTypeSEG = new ShipTypeSEG();
				shipTypeSEG.setShipTypeSEGSrvcDesc(rst.getString(1));
				shipTypeSEG.setShipTypeSEGSrvc(rst.getString(2));
				shipTypeSEG.setShipTypeSEGSrvcPP(rst.getString(3));
				shipTypeSEG.setShipTypeSEGSrvcRefr(rst.getString(5));
				listSEG.add(shipTypeSEG);
			}
			if (rst != null) {
				rst.close();
				rst = null;
			}
			if (pst != null) {
				pst.close();
				pst = null;
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("getFetchShipSEGALLActive()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rst, pst);
		}
		if (listSEG != null && !listSEG.isEmpty()) {
			list = new ArrayList<Object>();
			list.add(listSrvcId);
			list.add(listSrvcName);
			list.add(listSEG);
		}
		return list;
	}
	public boolean getCoverageSEG(Connection con, String codColo, String destClient, String destAddrCode, String srvcType){
		ResultSet rst = null;
		boolean iscobertura =false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
		    if(codColo == null ) {
			codColo ="";
		    }
		    if(codColo.isEmpty()){
		    	
				pstmt = con.prepareStatement("SELECT am_gety_code FROM sys_addr_mstr WHERE am_enty_id = ? AND AM_ADDR_CODE = ?");
				pstmt.setString(1, destClient);
				pstmt.setString(2, destAddrCode);
	
				rs = pstmt.executeQuery();
				while (rs.next()) {
				    codColo = rs.getString(1);
				}
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
		    }
		    pstmt = con.prepareStatement("SELECT FUN_GET_COVER_SEGxSER(?,?) FROM DUAL");
		    pstmt.setString(1, srvcType);
		    pstmt.setString(2, codColo);
		    
			rst = pstmt.executeQuery();
			if(rst.next()) {
				String valor = rst.getString(1);
				iscobertura=valor.equalsIgnoreCase("1");
			}			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("getCoverageSEG()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarResultSet(rs);
			resources.cerrarResultSet(rst);
			resources.cerrarPreparedStatement(pstmt);
		}
		return iscobertura;
	}
	public String getExclusiveTypeShipSEG(Connection con){
    	PreparedStatement pst = null;
		ResultSet rst = null;
		String typeShipSEG = "";
		try {
			String getExclusiveTypeShipSEG = "SELECT PM_PARM_CODE1 FROM SYS_PARM_MSTR WHERE PM_PARM_TYPE ='SRVC_LOG' AND pm_vlue1_id = '1' AND NVL(pm_delete_flag, 'N') = 'N'";
			pst = con.prepareStatement(getExclusiveTypeShipSEG);
			rst = pst.executeQuery();
			while(rst.next()) {
				typeShipSEG += rst.getString(1);
				typeShipSEG += ",";
			}		
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getExclusiveTypeShipSEG()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rst, pst);
		}
		return typeShipSEG;
	}
	
	public String validateIsTypeSEG(String typeService, String str ){
		String[] list = str.split(",");
		// Se comenta condicion por que el SEG-2D ya es un servicio con cobro y reglas de coberturas
		for (int i = 0; i < list.length; i++) {
			if (typeService.equalsIgnoreCase(list[i])) {
				return "Y";
			}
		}
		return "N";
	}
	
	public String branchLocationType(Connection con, String brncId){//AAP20
		String result = "";
		JavBranchRecords brncRec = new JavBranchRecords();
		result = brncRec.branchLocationType(con, brncId);
		return result;
	}

	public void getGroupClientId(Connection con, String clientId, HttpSession session) {
		Global global = (Global) session.getAttribute("sGlobal");
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String groupIdQuery = "select pack_web.FUN_CHCK_GRUP_CLNT(?)as groupid,to_char(sysdate,'DD/MM/YYYY HH24:MI') from dual";
			pst = con.prepareStatement(groupIdQuery);
			pst.setString(1, clientId);
			rs = pst.executeQuery();
			String groupClientId = clientId;
			while (rs.next()) {
				groupClientId = rs.getString(1);
			}
			global.setGroupClientIdDestino(groupClientId);
			session.setAttribute("sGlobal", global);

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("getGroupClientId()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
	}
	
	/* Verifica si el cliente tiene registrados los datos fiscales */
	private String getValidDatosFiscal(Connection con, String rfc) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String existeDatoFiscal = "N";
		try {
			pst = con.prepareStatement("SELECT VALID_FLAG FROM SYS_CAT_CLNT_FISC WHERE TAX_ID = ?");
			
			pst.setString(1, rfc);
			rs = pst.executeQuery();
			if (rs.next()) {
				existeDatoFiscal = rs.getString(1).equalsIgnoreCase("Y") ? "Y" : "N";
			}
		}catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getExisteDatosFiscales()Error:").append(e).toString());
			e.printStackTrace();
		}finally {
			resources.closeResources(rs, pst);
		}
		return existeDatoFiscal;
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
	
	//generacion de mensaje una vez encuentre un centro de costo sin cobertura
	public StringBuilder msgCCSinCobertura(Connection con, JavWebBookingGeneralMainForm aform, String desCCcosto ) {
		StringBuilder mensaje = new StringBuilder("La direccion "); 
		String  colonia = "",  CP = "", ciudad = "", estado= ""; 
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {	
			String query = "select COL_DES, COLO_ZIPCODE, CIUDAD, ESTADO from pcobertura_view where cod_colo = ?"; 
		
			pst = con.prepareStatement(query);
			pst.setString(1, aform.getOriginColinaCode());
			rs = pst.executeQuery();
			
			if (rs.next()) {
				colonia = rs.getString(1);
				CP = rs.getString(2);
				ciudad = rs.getString(3);
				estado = rs.getString(4);	
			}
			
			mensaje.append("'Calle ").append( aform.getOrgien1())
			.append(" Num. " ).append(aform.getOrgien2() )
			.append(" CP: ").append(CP)
			.append(", " ).append(colonia)
			.append(", ").append(ciudad)
			.append(", " ).append(estado)
			.append("' , del centro de costo ") .append(aform.getCentrosCosto()).append(" - ").append(desCCcosto)
			.append(" sin cobertura asociada. Favor de Contactar a su asesor logístico para verificar cobertura.req.setAttribute ");

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("msgCCSinCobertura()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return mensaje; 
	}
	
	public String existTariffServiceDtl(ArrayList<?> servicesDetailArray, String tariff) {
		String existTariff = "N";
		ShipmentServiceDetail ssd = null;
		int size = 0;

		if (servicesDetailArray != null) {
			size = servicesDetailArray.size();
		}
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
				if (ssd.getTarifa().contains(tariff)) {
					existTariff = "Y";
				}
			}

		}
		return existTariff;
	}
	
	public int getMaxQtyPackages(Connection con) {
		ConsultaParametros consulta = new ConsultaParametros();
		int tope = Integer.parseInt(((ArrayList) consulta.QryMdulTypeParm1(con,"BOK","MAX_QUANTITY", "PACKAGES").get(0)).get(2).toString());
		tope = tope >= 1 ? tope : Integer.MAX_VALUE;
		return tope;
	}
}
