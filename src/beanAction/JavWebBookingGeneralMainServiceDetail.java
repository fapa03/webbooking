/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creaci?n: 05/12/2011
 * Compa??a: PAQUETEXPRESS.
 * Descripci?n del programa: Bean para acciones de captura de detalle de servicios.
 * FileName: JavWebBookingGeneralMainAction.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 05/06/2012
 * Descripci?n: Se agregó lógica para obtener tope máximo de peso para la captura de bultos por linea 
 * ------------------------------------------------------------------
 * Clave: AAP02
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 05/06/2012
 * Descripci?n: Se obtiene de variable de sesion "global" el indicador de tarifa nueva o vieja 
 * y se asigna a una variable de la forma para la validacion de este tipo de tarifas. 
 * ------------------------------------------------------------------
 * Clave: AAP03
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 27/12/2014
 * Descripci?n: Aplicacion de reglas para incrementos de tarifario 2015.
 *
 * ------------------------------------------------------------------ 
 * Clave: AAP04
 * Autor: ABRAHAM ARJONA
 * Fecha: 27/01/2015
 * Descripci?n: Modificaciones para calcular descuentos en servicios a tarifa "A"
 * 
 * ------------------------------------------------------------------  
 */
package beanAction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.paquetexpress.www.webbooking.Documentacion.Embarque;
import com.paquetexpress.www.webbooking.Documentacion.ShipTypeSEG;

import bean.ConsultaParametros;
import bean.Global;
import bean.JavCatProducts;
import bean.JavTariff;
import bean.Resources;
import bean.ShipmentServiceDetail;
import beanForm.JavWebBookingGeneralMainForm;
//import paquetexpress.internal.common.ConnectDB;
import logger.AccessLog;
import mx.com.paquetexpress.general.model.dto.SysSspDesMstrDTO;

public class JavWebBookingGeneralMainServiceDetail {
	private StringBuffer concatena = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	
	@SuppressWarnings("rawtypes")
	public String perform(JavWebBookingGeneralMainForm aform, Connection con,
			HttpServletRequest req, HttpSession session, Global global) {
		String returnPage = "thispage";
		try {
			String accion = aform.getAccion();
			ArrayList servicesDetailArray = (ArrayList) session.getAttribute("servicesDetail");
			
			  if (accion.equals("init")) {
				aform.setFactorDividorPesoVol(global.getFactorDivisorPesoVol());
				/*obtiene informacion del detalle del ultimo envio*/
				getInfoUltimoEnvio(con, aform, global);
				aform = getMAXSEG(con, aform);
				aform = getMinMeasures(con, aform);
				if(aform.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
					aform.setPeso("");
				}
				servicesDetailArray = new ArrayList(0);
				session.setAttribute("servicesDetail", servicesDetailArray);
				
				String msgInfo = getMsgAdvertenciaDetalleEnvio(con);  
				req.getSession().setAttribute("msgDimInfo", msgInfo);
		        req.setAttribute("msgDimInfo", msgInfo); 

				
			if(aform.getForceCaptureDimensions().equalsIgnoreCase("N") && aform.getIsShippingTypeSEG().equalsIgnoreCase("N")) {
				String msg = getMsgDimensionesIngresar(con);
				if(!msg.isEmpty()) {
					req.setAttribute("zeroexist",msg);//JSA03
				}
			}						
			} else if (accion.equals("Agregar")) {
				returnPage = agregarRenglon(aform, con, req, session, global, servicesDetailArray);
				aform.setAccionServices("");//se limpia variable para que se vuelva a ejecutar procesos de validacion en captura de servicios.
			} else if (accion.equalsIgnoreCase("Editar")) {
				returnPage = editarRenglon(aform, con, req, session, global, servicesDetailArray);
				aform.setAccionServices("");//se limpia variable para que se vuelva a ejecutar procesos de validacion en captura de servicios.
			} else if(accion.equalsIgnoreCase("Borrar")){
				returnPage = borrarRenglon(aform, req, session, servicesDetailArray);
				aform.setAccionServices("");//se limpia variable para que se vuelva a ejecutar procesos de validacion en captura de servicios.
			}
			/*si ya tiene un envio capturado ... se manda llamar la clase que contiene los procesos de servicios*/
			if (servicesDetailArray!= null && servicesDetailArray.size() > 0) {
				boolean soloSobre = false;
				String envelopealone = (String) session.getAttribute("envelopealone");//AAP26
				if (envelopealone != null && envelopealone.equalsIgnoreCase("true")) {//AAP26
				    soloSobre = true;
                }//AAP26
				if (!soloSobre) {//AAP26
					/*recalcula flete para clientes tarifa costo base mas excedente que cotizan por kilos (caso mary kay)*/
                    servicesDetailArray = amountRecalcTariffCType(con, session, aform, global, servicesDetailArray, "SHP-G", "PACKETS");//AAP26
                    
				}//AAP26
				
				/*asigna variables para inicializar la seccion de los servicios*/
				if (aform.getAccionServices().equals("")) {
					aform.setAccionServices("initServices");
				}
				//AAP
				JavWebBookingGeneralMainServices services = new JavWebBookingGeneralMainServices();
				returnPage = services.perform(aform, con, req, session, global);
				//AAP
			} else {
				/*se asigna vacio porque no hay servicios capturados, una vez que se capture un servicio, 
				 * se cambiar? de valor en la condicion "si aform.getAccionServices().equals("")" */
				aform.setAccionServices("");
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("perform()_Error:").append(e).toString());
			e.printStackTrace();
		} 
		return returnPage;
	}
	
	/***************************************************************************************************************
	 * Consulta para obtener informacion de ultimo envio para mostrar por default en seccion de captura de detalle *
	 ***************************************************************************************************************/
	@SuppressWarnings("rawtypes")
	private void getInfoUltimoEnvio(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "";
		try {
			query = "select wc_desc, wc_cont, wc_weight, wc_volume from web_clnt_mstr where wc_clnt_id = ? ";
			

			pst = con.prepareStatement(query);
			pst.setString(1, global.getClientIdAgreement());
			rs = pst.executeQuery();
			String desc = null, cont = null, weight = "", volume = "", productIdSat="";
			while (rs.next()) {
				desc = rs.getString(1);
				cont = rs.getString(2);
				weight = rs.getString(3);
				volume = rs.getString(4);
			}
			
			rs.close();
			pst.close();
			
			/*nueva obtencion de ULTIMA descripcion, contenido y producto del sat*/
			query = "select cu_desc, cu_cont, cu_product_id from sys_clnt_user_web where cu_clnt_id = ? and cu_user_id = ?";
			
			pst = con.prepareStatement(query);
			pst.setString(1, global.getClientId());
			pst.setString(2, global.getOrigenUserClave());
			rs = pst.executeQuery();
			while (rs.next()) {
				desc = rs.getString(1);
				cont = rs.getString(2);
				productIdSat = rs.getString(3);
			}
			resources.closeResources(rs, pst);
			if (productIdSat != null) {
				query = "SELECT DESCRIPTION FROM BOK_CAT_PRODUCTS WHERE PRODUCT_ID = ? AND ACTV_FLAG = 'Y'";
				pst = con.prepareStatement(query);
				pst.setString(1, productIdSat);
				rs = pst.executeQuery();
				while(rs.next()) {
					aform.setProductIdSat(productIdSat);
					aform.setProductDescSat(productIdSat+ " - " + rs.getString(1));
				}
			}
			resources.closeResources(rs, pst);
			if (desc != null) {
				query = "select ss_srvc_id, ss_refr_srvc_id, ss_code from sys_shp_desc_mstr where ss_desc = ?";
				pst = con.prepareStatement(query,
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				pst.setString(1, desc);
				rs = pst.executeQuery();
				String service_Id = null, refer_srvc_Id = null, service_code = null;
				while (rs.next()) {
					
					aform.setDescripcion(desc);
					aform.setContenido(cont);					
					
					service_Id = rs.getString(1);
					refer_srvc_Id = rs.getString(2);
					service_code = rs.getString(3);
					rs.last();
				}
				aform.setSs_srvc_id(service_Id);
				aform.setSs_refr_srvc_id(refer_srvc_Id);
				aform.setDescripcioncode(service_code);
			
				/*obtiene informacion para tarifa especial*/
				if (aform.getDefaultservicescreen().equals("yes")) {
					ArrayList resultado = getInfoUltimoEnvioTarifEsp(con, desc, weight, volume);
					if (resultado.size()>0) {
						aform.setPeso(resultado.get(0).toString());
						aform.setPesoDB(resultado.get(0).toString());//variable para validacion de peso original
						aform.setVolumen(resultado.get(1).toString());
						aform.setVolumenDB(resultado.get(1).toString());//variable para validacion de volumen original
						aform.setSpecialTariff(resultado.get(2).toString());	
					}					
				}
			}			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getInfoUltimoEnvio()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs!=null)
						rs.close();
				
				if (pst != null)
					pst.close();
			} catch (Exception e) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getInfoUltimoEnvio()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList getInfoUltimoEnvioTarifEsp(Connection con, String desc, String weight, String volume){
		PreparedStatement pst = null;
		ResultSet rs = null;
		String packets = "";
		String query = "";
		ArrayList result = new ArrayList(5);
		try {
			
			query = "select SS_REFR_SRVC_ID FROM SYS_SHP_DESC_MSTR where SS_DESC =?";
			pst = con.prepareStatement(query);
			pst.setString(1,desc);
			rs = pst.executeQuery();
			while(rs.next()) {
				packets = rs.getString(1);
			}
			if(packets.equalsIgnoreCase("PACKETS")) {
				if((weight != null) && (weight.length() > 0)) {
					result.add(weight);
					result.add(volume);
					result.add("true");
				}
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getInfoUltimoEnvioTarifEsp()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return result;
	}
	
	/*************************************************************************************************
	 * Metodo para agregar renglon a la lista del detalle de servicios								 *
	 *************************************************************************************************/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String agregarRenglon(JavWebBookingGeneralMainForm aform, Connection con, HttpServletRequest req,
			HttpSession session, Global global, ArrayList servicesDetailArray) {
		ShipmentServiceDetail ssd = new ShipmentServiceDetail();
		String returnPage = "";
		int arraysize = 0;
		aform.setShiperrmsg("");
		String errorMsgNoConfig = "This combination gives 0, So please select other options...........";
 
		try {			
			ssd = generaObjDetalle(con, aform, global);
			double totalValue = Double.parseDouble(ssd.importe.isEmpty() ? "0" : ssd.importe);
			// AAP
			if (totalValue > 0) {
				/* Validaci?n para no pemritir agregar la misma tarifa dos veces */
				if (servicesDetailArray.size() > 0) {
					for (int i = 0; i < servicesDetailArray.size(); i++) { 
						ShipmentServiceDetail ssdTmp = (ShipmentServiceDetail) servicesDetailArray.get(i);
						if (((ShipmentServiceDetail) servicesDetailArray.get(i)).tarifa.equalsIgnoreCase(ssd.tarifa)
								&& ((ShipmentServiceDetail) servicesDetailArray.get(i)).descripcion
										.equalsIgnoreCase(ssd.descripcion)) {
							if (ssd.getPeso().equalsIgnoreCase(ssdTmp.getPeso())
									&& ssd.getVolumen().equalsIgnoreCase(ssdTmp.getVolumen())) {
								aform.setShiperrmsg("No se pudo agregar el registro porque ya se capturó.");
								aform.setAccion("tarifChange");
								break;
							}
						}
					}
				}
				String validSatProd = validSATProduct(con, ssd.getProductIdSat(), ssd.getProductDescSat());
				if (!"Y".equalsIgnoreCase(validSatProd)) {
					aform.setShiperrmsg(validSatProd);
					aform.setAccion("satProduct");
				}
				if (aform.getShiperrmsg().isEmpty()) {
					servicesDetailArray.add(ssd);
				}
				arraysize = servicesDetailArray.size();
				aform.setHitCount(String.valueOf(arraysize));
				session.setAttribute("servicesDetail", servicesDetailArray);
				/*
				 * seccion para reiniciar la parte de las coberturas en caso de que en la
				 * iteraccion anterior fueran puros envios de sobres por si en este agregado de
				 * renglon no es sobre... se recargen los combos en la seccion de servicios
				 */
				validaSobre(aform, session);
			} else {
				String orginsite = global.assignedBranch.substring(0, 3);
				String destsite = global.destinationBranchId.substring(0, 3);
				if (orginsite.equalsIgnoreCase(destsite)) {
					// Validacion para poder verificar que el tipo de tarifa sea punto a punto
					if (aform.getDefaultservicescreen().equals("yes")
							&& aform.getDefaultservicescreenKm().equals("N")) {
						// se extrae de la funcion si esta linea de envio tiene alguna configuracion
						if (srvcConfigHasAmnt(con, aform, global)) {
							if (!ssd.getServiceId().equalsIgnoreCase("SHP-E") && ssd.getTarifa().equalsIgnoreCase("S")
									|| ssd.getServiceId().equalsIgnoreCase("SHP-E")
											&& !ssd.getTarifa().equalsIgnoreCase("S")) {
								aform.setShiperrmsg("No se pudo agregar el registro por falta de configuraciones.");
							} else {
								// AAP
								/* Validación para no permitir agregar la misma tarifa dos veces */
								if (servicesDetailArray.size() > 0) {
									for (int i = 0; i < servicesDetailArray.size(); i++) {
										ShipmentServiceDetail ssdTmp = (ShipmentServiceDetail) servicesDetailArray
												.get(i);
										if (((ShipmentServiceDetail) servicesDetailArray.get(i)).tarifa
												.equalsIgnoreCase(ssd.tarifa)
												&& ((ShipmentServiceDetail) servicesDetailArray.get(i)).descripcion
														.equalsIgnoreCase(ssd.descripcion)) {
											if (ssd.getPeso().equalsIgnoreCase(ssdTmp.getPeso())
													&& ssd.getVolumen().equalsIgnoreCase(ssdTmp.getVolumen())) {
												aform.setShiperrmsg(
														"No se pudo agregar el registro porque ya se capturó.");
												aform.setAccion("tarifChange");
												break;
											}
										}
									}
								}
								String validSatProd = validSATProduct(con, ssd.getProductIdSat(), ssd.getProductDescSat());
								if (!"Y".equalsIgnoreCase(validSatProd)) {
									aform.setShiperrmsg(validSatProd);
									aform.setAccion("satProduct");
								}
								if (aform.getShiperrmsg().isEmpty()) {
									servicesDetailArray.add(ssd);
								}
								arraysize = servicesDetailArray.size();
								aform.setHitCount(String.valueOf(arraysize));
								session.setAttribute("servicesDetail", servicesDetailArray);
								/*
								 * seccion para reiniciar la parte de las coberturas en caso de que en la
								 * iteraccion anterior fueran puros envios de sobres por si en este agregado de
								 * renglon no es sobre... se recargen los combos en la seccion de servicios
								 */
								validaSobre(aform, session);
							}
						} else {req.setAttribute("zeroexist", errorMsgNoConfig);}
					} else {req.setAttribute("zeroexist", errorMsgNoConfig);}
				} else {req.setAttribute("zeroexist", errorMsgNoConfig);}
			}
			returnPage = "thispage";

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("agregarRenglon()_Error:")
					.append(e).toString());
			e.printStackTrace();
		}
		return returnPage;
	}

	/*************************************************************************************************
	 * Metodo para editar renglon de la lista del detalle de servicios *
	 *************************************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String editarRenglon(JavWebBookingGeneralMainForm aform, Connection con, HttpServletRequest req,
			HttpSession session, Global global, ArrayList servicesDetailArray) {
		ShipmentServiceDetail ssd = new ShipmentServiceDetail();
		String returnPage = "";
		String strIndex = "";
		Boolean found = false;
		ArrayList servicesDetailArrayTmp = servicesDetailArray == null || servicesDetailArray.isEmpty() ?
				new ArrayList<>() : new ArrayList<>(servicesDetailArray);
		String errorMsgNoConfig = "This combination gives 0, So please select other options...........";
		try {
			strIndex = req.getParameter("radioselect");
			int index = Integer.parseInt(strIndex);
			ssd = generaObjDetalle(con, aform, global);
			double totalValue = Double.parseDouble(ssd.importe);
			if (totalValue > 0) {
				servicesDetailArray.remove(index);
				if (!servicesDetailArray.isEmpty()) {
					for (int i = 0; i < servicesDetailArray.size(); i++) {
						ShipmentServiceDetail ssdTmp = (ShipmentServiceDetail) servicesDetailArray.get(i);
						if (ssd.getTarifa().equalsIgnoreCase(ssdTmp.getTarifa())
								&& ssd.getDescripcion().equalsIgnoreCase(ssdTmp.getDescripcion())) {
							if (ssd.getPeso().equalsIgnoreCase(ssdTmp.getPeso())
									&& ssd.getVolumen().equalsIgnoreCase(ssdTmp.getVolumen())) {
								found = true;
								break;
							}
						}
					}
				}
				if (!found) {
					String validSatProd = validSATProduct(con, ssd.getProductIdSat(), ssd.getProductDescSat());
					if (!"Y".equalsIgnoreCase(validSatProd)) {
						aform.setShiperrmsg(validSatProd);
						aform.setAccion("satProduct");
					} else {
						servicesDetailArray.add(index, ssd);// Adding the changed values into the arraylist
						session.setAttribute("servicesDetail", servicesDetailArray);
						/*
						 * seccion para reiniciar la parte de las coberturas en caso de que en la
						 * iteraccion anterior fueran puros envios de sobres por si en este agregado de
						 * renglon no es sobre... se recargen los combos en la seccion de servicios
						 */
						validaSobre(aform, session);
					}
				} else {
					servicesDetailArray = servicesDetailArrayTmp;
					session.setAttribute("servicesDetail", servicesDetailArray);
					aform.setShiperrmsg("No se pudo agregar el registro porque ya se capturó");
				}
			} else {
				String orginsite = global.assignedBranch.substring(0, 3);
				String destsite = global.destinationBranchId.substring(0, 3);

				if (orginsite.equalsIgnoreCase(destsite)) {
					if (aform.getAccion() != null && aform.getAccion().equalsIgnoreCase("Editar")) {
						// Validacion para poder verificar que el tipo de tarifa sea punto a punto
						if (aform.getDefaultservicescreen().equals("yes")
								&& aform.getDefaultservicescreenKm().equals("N")) {
							if (srvcConfigHasAmnt(con, aform, global)) {
								if (!ssd.getServiceId().equalsIgnoreCase("SHP-E")
										&& ssd.getTarifa().equalsIgnoreCase("S")
										|| ssd.getServiceId().equalsIgnoreCase("SHP-E")
												&& !ssd.getTarifa().equalsIgnoreCase("S")) {
									aform.setShiperrmsg(
											"No se pudo actualizar el registro por falta de configuraciones.");
									aform.setRadioselectCurrent(index);
								} else {
									servicesDetailArray.remove(index);
									if (!servicesDetailArray.isEmpty()) {
										for (int i = 0; i < servicesDetailArray.size(); i++) {
											ShipmentServiceDetail ssdTmp = (ShipmentServiceDetail) servicesDetailArray
													.get(i);
											if (ssd.getTarifa().equalsIgnoreCase(ssdTmp.getTarifa())
													&& ssd.getDescripcion().equalsIgnoreCase(ssdTmp.getDescripcion())) {
												if (ssd.getPeso().equalsIgnoreCase(ssdTmp.getPeso())
														&& ssd.getVolumen().equalsIgnoreCase(ssdTmp.getVolumen())) {
													found = true;
													break;
												}
											}
										}
									}
									if (!found) {
										String validSatProd = validSATProduct(con, ssd.getProductIdSat(), ssd.getProductDescSat());
										if (!"Y".equalsIgnoreCase(validSatProd)) {
											aform.setShiperrmsg(validSatProd);
											aform.setAccion("satProduct");
										} else {
											servicesDetailArray.add(index, ssd);// Adding the changed values into the
																				// arraylist
											session.setAttribute("servicesDetail", servicesDetailArray);
											/*
											 * seccion para reiniciar la parte de las coberturas en caso de que en la
											 * iteraccion anterior fueran puros envios de sobres por si en este agregado de
											 * renglon no es sobre... se recargen los combos en la seccion de servicios
											 */
											validaSobre(aform, session);
										}
									} else {
										servicesDetailArray = servicesDetailArrayTmp;
										session.setAttribute("servicesDetail", servicesDetailArray);
										aform.setShiperrmsg("No se pudo agregar el registro porque ya se capturó");
									}
								}
							} else {req.setAttribute("zeroexist", errorMsgNoConfig);}
						} else {req.setAttribute("zeroexist", errorMsgNoConfig);}
					}
				} else {
					if (aform.getAccion() != null && aform.getAccion().equalsIgnoreCase("Editar")) {
						if (!ssd.getServiceId().equalsIgnoreCase("SHP-E") && !ssd.getTarifa().equalsIgnoreCase("S")
								|| ssd.getServiceId().equalsIgnoreCase("SHP-E")
										&& !ssd.getTarifa().equalsIgnoreCase("S")) {
							aform.setShiperrmsg("No se pudo actualizar el registro por falta de configuraciones.");
							aform.setRadioselectCurrent(index);
						}
					} else {req.setAttribute("zeroexist", errorMsgNoConfig);}
				}
			}
			returnPage = "thispage";
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("editarRenglon()_Error:")
					.append(e).toString());
			e.printStackTrace();
		}
		return returnPage;
	}
	
	/*************************************************************************************************
	 * Metodo para borrar renglon de la lista del detalle de servicios								 *
	 *************************************************************************************************/
	@SuppressWarnings("rawtypes")
	private String borrarRenglon(JavWebBookingGeneralMainForm aform, 
			HttpServletRequest req, HttpSession session, ArrayList servicesDetailArray){
		String returnPage = "";
		int arraysize = 0;
		try {

			String selectedIndex = req.getParameter("radioselect");			
			
			int index=-1;
			if(selectedIndex!=null)
				index = Integer.parseInt(selectedIndex);					
			
			ArrayList servicesDetail = (ArrayList)session.getAttribute("servicesDetail");
			
			if (servicesDetail != null && !servicesDetail.isEmpty()) {
				servicesDetail.remove(index);
			}
			session.setAttribute("servicesDetail",servicesDetail);
			
			arraysize = servicesDetailArray.size();
			aform.setHitCount(String.valueOf(arraysize));
			
			returnPage = "thispage";
					
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("borrarRenglon()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return returnPage;
	}
	
	/****************************************************************************************************
	 * Metodo para reiniciar variable que provoca la ejecucion de la parte de las coberturas en caso 	*
	 * de que en la iteraccion anterior fueran puros envios de sobres por si en este 					*
	 * agregado de renglon no es sobre... se recargen los combos en la seccion de servicios				*
	 * **************************************************************************************************/
	private void validaSobre(JavWebBookingGeneralMainForm aform, HttpSession session) {
		try {
			String envelopealone = (String) session.getAttribute("envelopealone");
			if (envelopealone != null && envelopealone.equalsIgnoreCase("true")) {
				aform.setI(0);
				session.removeAttribute("envelopealone");
			} else {
				aform.setI(1);
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("validaSobre()_Error:").append(e).toString());
			e.printStackTrace();
		}
	}
	
	/****************************************************************************************
	 * metodo para generar renglon de detalle de envio										*
	 * **************************************************************************************/
	private ShipmentServiceDetail generaObjDetalle(Connection con, JavWebBookingGeneralMainForm aform, Global global){
		ShipmentServiceDetail ssd = new ShipmentServiceDetail();
		try {
			ssd.cantidad = aform.getCantidad();
			ssd.descripcion = aform.getDescripcion();
			ssd.contenido = aform.getContenido().toUpperCase();
			ssd.peso = aform.getPeso();
			ssd.volumen = aform.getVolumen();
			ssd.serviceId = aform.getSs_srvc_id();
			ssd.refServiceId = aform.getSs_refr_srvc_id();
			ssd.descripcionCode = aform.getDescripcioncode();
			if (aform.getTarifa() == null ||  aform.getTarifa().isEmpty())  {
				if(ssd.getServiceId().equalsIgnoreCase("SHP-E") || ssd.getDescripcion().equalsIgnoreCase("SOBRE")) {
					aform.setTarifa("S");	
					if (global.tarifType.equalsIgnoreCase("c") && aform.getSrvcConfigHasAmntC()) {
						aform.setPeso("1");
						aform.setVolumen("0.001278");
					}
				}
			}
			
			ssd.importe = obtenerImporte(con, aform, global);
			ssd.tarifa = aform.getTarifa();
			ssd.setVolL(aform.getVolL() == null ? "0.0" : (aform.getVolL().isEmpty()? "0.0" : aform.getVolL()));
			ssd.setVolH(aform.getVolH() == null ? "0.0" : (aform.getVolH().isEmpty()? "0.0" : aform.getVolH()));
			ssd.setVolW(aform.getVolW() == null ? "0.0" : (aform.getVolW().isEmpty()? "0.0" : aform.getVolW()));
			ssd.setWeightVolumetric(aform.getWeightVolumetric() == null ? "0.0" : (aform.getWeightVolumetric().isEmpty()? "0.0" : aform.getWeightVolumetric()));
			ssd.setProductIdSat(aform.getProductIdSat() != null ? aform.getProductIdSat() : "");
			ssd.setProductDescSat(aform.getProductDescSat() != null ? aform.getProductDescSat() : "");
			double dImporte = 0;
			double dCostoTarifaBase = 0;
			double descuento = 0;
			
			/*calculos tarifa base*/
			if(aform.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
				ssd.setDatosCalculosTarifaSEG(aform.getDatosCalculosTarifaSEG());
				if (aform.getDatosCalculosTarifaSEG() != null && !aform.getDatosCalculosTarifaSEG().getTypeCalculoTariff().equalsIgnoreCase("PISO")) {
					try {
						DatosCalculosTarifaSEG calculosTarifaSEG = getServiceAmountSEG(con, aform, global, true);
						if (calculosTarifaSEG != null) {
							Double valueTarif = calculosTarifaSEG.getvFlete();
							ssd.setCostoTarifaBase(valueTarif.toString());
							ssd.setMostrarDesCuento("N");
							try { dImporte = Double.parseDouble(ssd.getImporte()); } catch (Exception e) { dImporte = 0; }
							try { dCostoTarifaBase = Double.parseDouble(ssd.getCostoTarifaBase()); } catch (Exception e) { dCostoTarifaBase = 0; }
							descuento = dCostoTarifaBase - dImporte;
							ssd.setDatosCalculosTarifaSEGPISO(calculosTarifaSEG);
							if (descuento < 0){
								ssd.setConvenioAlto("Y");
							} else {
								ssd.setConvenioAlto("N");
							}
						} else 						{
							ssd.setCostoTarifaBase("0.00");
							ssd.setMostrarDesCuento("N");
						}
					} catch (Exception e) {
						AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("obtenerImporte3()_Error:").append(e).toString());
						e.printStackTrace();
					}
				} else{
					ssd.setCostoTarifaBase("0.00");
					ssd.setMostrarDesCuento("Y");
				}
			}
			else if (aform.getDefaultservicescreen().equals("yes") && global.getClasifTarif().equals("1")) {//AAP04				
				ssd.setCostoTarifaBase(importeTarifaPiso(con, aform, global, false));//AAP04
				ssd.setMostrarDesCuento("N");//AAP04
				
				try { dImporte = Double.parseDouble(ssd.getImporte()); } catch (Exception e) { dImporte = 0; }
				try { dCostoTarifaBase = Double.parseDouble(ssd.getCostoTarifaBase()); } catch (Exception e) { dCostoTarifaBase = 0; }
				descuento = dCostoTarifaBase - dImporte;
				
				if (descuento<0){
					ssd.setConvenioAlto("Y");
				} else {
					ssd.setConvenioAlto("N");
				}
				
				
			} else if (aform.getDefaultservicescreen().equals("yes") && global.getTarifType().equals("C")) {//AAP04				
				double dPeso = 0;
				double dvolu = 0;
				boolean verificaTarifa = false;
				String tarifa = "";
				
				try { dPeso = Double.parseDouble(ssd.getPeso()); } catch (Exception e) { dPeso = 0; }
				try { dvolu = Double.parseDouble(ssd.getVolumen()); } catch (Exception e) { dvolu = 0; }
				
				if ((dPeso>0 || dvolu >0) || ssd.getServiceId().equals("SHP-E")) {
					verificaTarifa = true;	
				}
				
				if(verificaTarifa) {
					if (ssd.getServiceId().equals("SHP-E")){
						ssd.setTarifa("S");
						aform.setTarifa("S");
					} else {
						JavTariff tariff = new JavTariff();
						tarifa = tariff.getTipoTarifa(con, dPeso, dvolu);					
						ssd.setTarifa(tarifa);
						aform.setTarifa(tarifa);	
					}
					
					calculateKMTarifType(con, global);
					
					ssd.setCostoTarifaBase(importeTarifaPiso(con, aform, global, false));//AAP04
					ssd.setMostrarDesCuento("N");//AAP04
					
					
					try { dImporte = Double.parseDouble(ssd.getImporte()); } catch (Exception e) { dImporte = 0; }
					try { dCostoTarifaBase = Double.parseDouble(ssd.getCostoTarifaBase()); } catch (Exception e) { dCostoTarifaBase = 0; }
					descuento = dCostoTarifaBase - dImporte;
					
					if (descuento<0){
						ssd.setConvenioAlto("Y");
					} else {
						ssd.setConvenioAlto("N");
					}
					
				} else {
					ssd.setCostoTarifaBase("0.00");//AAP05
					ssd.setMostrarDesCuento("N");//AAP05
					JavTariff tariff = new JavTariff();
					tarifa = tariff.getTarrifDefaultByTarrifType_C(con, global.getClientIdAgreement());
					ArrayList records = tariff.getLovRecordsByTarrif(con, tarifa);

					ssd.setPeso(((HashMap) records.get(0)).get("WT").toString());
					ssd.setVolumen(((HashMap) records.get(0)).get("VOL").toString());
					ssd.setTarifa(tarifa);
					aform.setTarifa(tarifa);	
				}
			} else {//AAP04
				ssd.setCostoTarifaBase("0.00");//AAP04
				ssd.setMostrarDesCuento("Y");//AAP04
			}		
			
			if (ssd.getServiceId().equals("SHP-E") && ((ssd.getPeso() != null && Double.parseDouble(ssd.getPeso()) == 0) || (ssd.getVolumen() != null && Double.parseDouble(ssd.getVolumen()) == 0))){
				if((ssd.getPeso() != null && Double.parseDouble(ssd.getPeso()) == 0)){
					ssd.setPeso("1");
				}
				if((ssd.getVolumen() != null && Double.parseDouble(ssd.getVolumen()) == 0)){
					//ssd.setVolumen("0.01");
                    ssd.setVolumen("0.001278");
				}
			}
			JavTariff company = new JavTariff();//AAP23
			if (aform.getFormaPago().equals("TO_PAY")) {
				ssd.setCompanyID(company.getCompanyId(con, ssd.getPeso(), aform.getDestinationclave(), ssd.getServiceId()));
			} else {//PAID
				ssd.setCompanyID(company.getCompanyId(con, ssd.getPeso(), global.getClientId(), ssd.getServiceId()));	
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("generaObjDetalle()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return ssd;
	}
	
	/****************************************************************************************
	 * metodo para obtener importe de tarifa de piso o especial								*
	 * **************************************************************************************/
	private String obtenerImporte(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		String totalValue = "";	
		try {
			if(aform.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
				double valueTarif = 0;
				DatosCalculosTarifaSEG calculosTarifaSEG = null;
				try {
					calculosTarifaSEG = getServiceAmountTarifaEspecialSEG(con, aform, global);
					if (calculosTarifaSEG != null) {
						valueTarif = calculosTarifaSEG.getvFlete();
						aform.setTarifa(calculosTarifaSEG.getTarifa());
					}
				} catch (Exception e) {
					AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("obtenerImporte1()_Error:").append(e).toString());
					e.printStackTrace();
				}
				if(valueTarif == 0) {
					try {
						calculosTarifaSEG = getServiceAmountTarifaKmSEG(con, aform, global);
						if (calculosTarifaSEG != null) {
							valueTarif = calculosTarifaSEG.getvFlete();
							aform.setTarifa(calculosTarifaSEG.getTarifa());
						}
					} catch (Exception e) {
						AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("obtenerImporte2()_Error:").append(e).toString());
						e.printStackTrace();
					}
					if(valueTarif == 0) {
						try {
							calculosTarifaSEG = getServiceAmountSEG(con, aform, global, true);
							if (calculosTarifaSEG != null) {
								valueTarif = calculosTarifaSEG.getvFlete();
								aform.setTarifa(calculosTarifaSEG.getTarifa());
								aform.setDatosCalculosTarifaSEG(calculosTarifaSEG);
							}
						} catch (Exception e) {
							AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("obtenerImporte3()_Error:").append(e).toString());
							e.printStackTrace();
						}
					} else {
						aform.setDatosCalculosTarifaSEG(calculosTarifaSEG);
					}
				} else {
					aform.setDatosCalculosTarifaSEG(calculosTarifaSEG);
				}
				totalValue = new java.text.DecimalFormat("0.00").format(valueTarif);
			}
			else {
				if (aform.getTarifa() == null || (aform.getTarifa() != null && aform.getTarifa().isEmpty())) {
					if(aform.getWeightVolumetric() != null && !aform.getWeightVolumetric().isEmpty() && Double.parseDouble(aform.getWeightVolumetric()) >0 &&
							(aform.getPeso() != null && !aform.getPeso().isEmpty()) && (aform.getVolumen() != null && !aform.getVolumen().isEmpty())) {
						if(aform.getDescripcion().equalsIgnoreCase("SOBRE")) {
							aform.setTarifa("S");
						} else {
							JavTariff tariff = new JavTariff();
							String tarifa = tariff.getTipoTarifa(con, Double.parseDouble(aform.getPeso()), Double.parseDouble(aform.getVolumen()));
							aform.setTarifa(tarifa);
						}
					}
				}
				if (aform.getDefaultservicescreen().equals("yes")) {
					if (aform.getDefaultservicescreenKm().equals("Y")) {
						totalValue = importeTarifaEspecialKm(con, aform, global);
					} else {
						totalValue = importeTarifaEspecial(con, aform, global);
					}
				} else {
					totalValue = importeTarifaPiso(con, aform, global, true);
				}
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("obtenerImporte()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return totalValue;
	}
	
	/****************************************************************************************
	 * metodo para obtener importe de tarifa de piso										*
	 * **************************************************************************************/
	private String importeTarifaPiso(Connection con, JavWebBookingGeneralMainForm aform, Global global, boolean valPesoVol){
		String totalValue = "";		
		try {
			totalValue = new java.text.DecimalFormat("0.00").format(getServiceAmount(con, aform, global, valPesoVol));
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("importeTarifaPiso()_Error:").append(e).toString());
			e.printStackTrace();
		}		
		return totalValue;
	}
	
	/****************************************************************************************
	 * metodo para obtener importe de tarifa especial										*
	 * **************************************************************************************/
	private String importeTarifaEspecial(Connection con,  JavWebBookingGeneralMainForm aform, Global global){
		String totalValue = "";
		try {
			int intCantidad = Integer.parseInt(aform.getCantidad());
			/*TARIFA FIJA C*/
			if(global.tarifType.equalsIgnoreCase("c")) {
				totalValue = new java.text.DecimalFormat("0.00").format(intCantidad*getServiceAmountCtype(con, aform, global));
				/*TARIFA FIJA ANTERIOR*/
			} else if (global.clasifTarif.equals("0")){//AAP02
				totalValue = new java.text.DecimalFormat("0.00").format(intCantidad*getServiceAmountTarifaEspecial(con,aform, global));
				/*TARIFA FIJA NUEVA*/
			} else {//AAP02
				totalValue = new java.text.DecimalFormat("0.00").format(intCantidad*getServiceAmountTarifaEspecialNueva(con,aform, global));
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("importeTarifaEspecial()_Error:").append(e).toString());
			e.printStackTrace();
		}		
		return totalValue;
	}
	/****************************************************************************************
	 * metodo para obtener importe de tarifa especial por kilometro							*
	 * **************************************************************************************/
	private String importeTarifaEspecialKm(Connection con,  JavWebBookingGeneralMainForm aform, Global global){
		String totalValue = "";
		try {
			int intCantidad = Integer.parseInt(aform.getCantidad());
			/*TARIFA FIJA C*/
			if(global.tarifType.equalsIgnoreCase("c")) {
				totalValue = new java.text.DecimalFormat("0.00").format(intCantidad*getServiceAmountCtypeKm(con, aform, global));
				/*TARIFA FIJA ANTERIOR*/
			} else if (global.clasifTarif.equals("0")){//AAP02
				totalValue = new java.text.DecimalFormat("0.00").format(intCantidad*getServiceAmountTarifaEspecialKm(con,aform, global));
				/*TARIFA FIJA NUEVA*/
			} else {//AAP02
				totalValue = new java.text.DecimalFormat("0.00").format(intCantidad*getServiceAmountTarifaEspecialNuevaKm(con, aform, global));
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("importeTarifaEspecialKm()_Error:").append(e).toString());
			e.printStackTrace();
		}		
		return totalValue;
	}
	
	/****************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa de piso								*
	 * **************************************************************************************/
	private double getServiceAmount(Connection con, JavWebBookingGeneralMainForm aform, Global global, boolean valPesoVol) {
		double returnvalue = 0.0f;
		CallableStatement cst = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = "Begin ? := PACK_TARIFAS.FTCH_SERVC_TARIF_PISO(?,?,?,?,?,?,?,?,?,?); End; ";//AAP03
			
			cst = con.prepareCall(query);
			
	          
			  String srvcId = aform.getSs_srvc_id().equalsIgnoreCase("RAD-ZP-1") ? 
			                    "RAD-1" : aform.getSs_srvc_id();
			            
			  String refrSrvcId = aform.getSs_refr_srvc_id().equalsIgnoreCase("RAD-ZP") ?
			                    "RAD" : aform.getSs_refr_srvc_id();
			
			cst.registerOutParameter(1, Types.NUMERIC);
			cst.setString(2, srvcId);
			cst.setString(3, refrSrvcId);
			cst.setInt(6, Integer.parseInt(aform.getCantidad()));
			cst.setString(7, aform.getOrginbranchcode());
			cst.setString(8, aform.getDestinationcode());
			cst.registerOutParameter(10, Types.NUMERIC);
			
			if (global.kmTarifType == null) {
				cst.setNull(11, Types.NUMERIC);
			} else {
				cst.setDouble(11, Double.parseDouble(global.kmTarifType));
			}
			String peso = aform.getPeso();
			if(aform.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
				double pesoReal = 0, pesoVol = 0;
				pesoReal = Double.parseDouble(aform.getPeso());
				if(!aform.getWeightVolumetric().isEmpty()) {
					pesoVol = Double.parseDouble(aform.getWeightVolumetric());
				}
				if(pesoVol > pesoReal) {
					peso = aform.getWeightVolumetric();
				}
				JavTariff tariff = new JavTariff();
				String tarifa = tariff.getTipoTarifa(con, Double.parseDouble(peso), Double.parseDouble(aform.getVolumen()));
				aform.setTarifa(tarifa);
			}
			if (aform.getSs_srvc_id().equalsIgnoreCase("SHP-E")) {
				cst.setString(4, "NON");
				cst.setNull(5, Types.NUMERIC);
				cst.setString(9, aform.getTarifa());
			} else {
				cst.setString(4, "KG");
				cst.setDouble(5, Double.parseDouble(peso));
				if (aform.getTarifa().equalsIgnoreCase("T7")) {
					cst.setString(9, "T7P");
				} else {
					cst.setString(9, aform.getTarifa());
				}
			}
			
			cst.executeQuery();

			double weight = cst.getDouble(1);
			returnvalue = weight;
			double volume = 0.0f;
			if (aform.getSs_srvc_id().equalsIgnoreCase("SHP-G") && aform.getTarifa().equalsIgnoreCase("T7")) {
				cst.setString(4, "CUM");
				cst.setDouble(5, Double.parseDouble(aform.getVolumen()));
				cst.setString(9, "T7V");
				cst.executeQuery();
				volume = cst.getDouble(1);
				
				if (weight > volume) {
					returnvalue = weight;
					if (valPesoVol) {
						aform.setCalculationdone("weight");
					}					
				} else {
					returnvalue = volume;
					if (valPesoVol) {
						aform.setCalculationdone("volume");
					}					
				}
								
			} else if (aform.getSs_srvc_id().equalsIgnoreCase("SHP-G")) {
				/*aplica regla para descontar el 3% al flete SHP-G cuando el cliente haya sido creado antes del 2015 (NO APLICA PARA T7)*///AAP03
				query = "SELECT CRTD_ON FROM SYS_CLNT_MSTR WHERE CM_CLNT_ID = ?";
				
				pst = con.prepareStatement(query);
				pst.setString(1, aform.getOrgienclave());
				
				rs = pst.executeQuery();
				
				Calendar fechaCreacion = Calendar.getInstance();
				if (rs.next()) {
					fechaCreacion.setTime(rs.getDate(1));
				}
				
				rs.close();
				pst.close();
				
				/*verifica que tenga fecha de creacion antes del 2015*///AAP03
				if (fechaCreacion!= null && fechaCreacion.get(fechaCreacion.YEAR)<2015) {//AAP03
					
					query = "SELECT * FROM SYS_CLNT_SRVC WHERE CS_CLNT_ID = ? AND CS_SRVC_ID = ?";
					
					pst = con.prepareStatement(query);
					pst.setString(1, aform.getOrgienclave());
					pst.setString(2, "PACKETS");
					
					rs = pst.executeQuery();
					
					/*verifica que tenga descuentos registrados para aplicar un descuento del 3% adicional (incremento de 5% a tarifario 2014)*///AAP03
					if (rs.next()) {//AAP03
						Double descuento = returnvalue * .03;
						returnvalue = returnvalue - descuento;						
					}
				}				
			}			

		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getServiceAmount()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {			
			resources.closeResources(rs, pst, cst);
		}		
		return returnvalue;
	}	
	
	/****************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa especial de tipo de tarifa no C		*
	 * **************************************************************************************/
	public double getServiceAmountTarifaEspecial(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		double amount = 0.0;
		try { 
			
			String query = "select pack_web.FUN_FTCH_TRIF_AMNT(?,?,?,?,?,?) from dual";
			pst = con.prepareStatement(query);
			
			pst.setString(1, global.getClientIdAgreement());
			pst.setString(2, aform.getOrginbranchcode());//global.assignedBranch
			pst.setString(3, aform.getDestinationcode());//global.destinationBranchId	
			pst.setString(4, global.tarifType);
			pst.setString(5, aform.getSs_refr_srvc_id());
			pst.setString(6, aform.getSs_srvc_id());
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				amount = rs.getDouble(1);
			}
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getServiceAmountTarifaEspecial()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return amount;
	}

	/************************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa especial de tipo de tarifa no C por kilometro	*
	 * **********************************************************************************************/
	public double getServiceAmountTarifaEspecialKm(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		double amount = 0.0;
		try {

			String query = "select pack_web.FUN_FTCH_TRIF_AMNT_KM(?,?,?,?,?,?) from dual";
			pst = con.prepareStatement(query);

			pst.setString(1, global.getClientIdAgreement());			
			pst.setString(2, aform.getOrginbranchcode());//global.assignedBranch
			pst.setString(3, global.getKmTarifType());// distancia kilometros	
			pst.setString(4, global.getTarifType());
			pst.setString(5, aform.getSs_refr_srvc_id());
			pst.setString(6, aform.getSs_srvc_id());
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				amount = rs.getDouble(1);
			}
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getServiceAmountTarifaEspecialKm()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return amount;
	}
	
	/****************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa especial de tipo de tarifa C			*
	 * **************************************************************************************/
	public double getServiceAmountCtype(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		double amount = 0.0;
		try {
			
			String query = "select pack_web.FUN_FTCH_WT_VOL_AMNT(?,?,?,?,?,?,?,?) from dual";
			
			pst = con.prepareStatement(query);

			pst.setString(1, global.getClientIdAgreement());
			pst.setString(2, aform.getOrginbranchcode());
			pst.setString(3, aform.getDestinationcode());	
			pst.setString(4, aform.getTarifType());
			pst.setString(5, aform.getSs_refr_srvc_id());
			pst.setString(6, aform.getSs_srvc_id());
			pst.setDouble(7, Double.parseDouble(aform.getPeso()));
			pst.setDouble(8, Double.parseDouble(aform.getVolumen()));
			
			rs = pst.executeQuery();			
			
			if (rs.next()) {
				amount = rs.getDouble(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getServiceAmountCtype()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return amount;
	}
	
	/********************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa especial de tipo de tarifa C por kilometro	*
	 * ******************************************************************************************/
	public double getServiceAmountCtypeKm(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		double amount = 0.0;
		String query = ""; 
		try {
			
				query = "select pack_web.FUN_FTCH_WT_VOL_AMNT_KM(?,?,?,?,?,?,?,?) from dual";		
				pst = con.prepareStatement(query);
				
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, aform.getOrginbranchcode());
				pst.setString(3, global.getKmTarifType());// distancia kilometros	
				pst.setString(4, aform.getTarifType());
				pst.setString(5, aform.getSs_refr_srvc_id());
				pst.setString(6, aform.getSs_srvc_id());
				pst.setDouble(7, Double.parseDouble(aform.getPeso()));
				pst.setDouble(8, Double.parseDouble(aform.getVolumen()));
				
				rs = pst.executeQuery();			
				
				if (rs.next()) {
					amount = rs.getDouble(1);
				}	
			
		} catch (Exception e) {	
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getServiceAmountCtypeKm()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return amount;
	}
	
	/****************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa especial de tipo de tarifa C			*
	 * **************************************************************************************/
	public double getServiceAmountCtype(Connection con, JavWebBookingGeneralMainForm aform, Embarque embarque, Global global) {//AAP26
		PreparedStatement pst = null;
		ResultSet rs = null;
		double amount = 0.0;
		try {
			
			String query = "select pack_web.FUN_FTCH_WT_VOL_AMNT(?,?,?,?,?,?,?,?) from dual";
			
			pst = con.prepareStatement(query);
			
			pst.setString(1, global.getClientIdAgreement());
			pst.setString(2, aform.getOrginbranchcode());
			pst.setString(3, aform.getDestinationcode());	
			pst.setString(4, aform.getTarifType());
			pst.setString(5, aform.getSs_refr_srvc_id());
			pst.setString(6, aform.getSs_srvc_id());
			pst.setDouble(7, embarque.getWeight());
			pst.setDouble(8, embarque.getVolume());			
			
			rs = pst.executeQuery();			
			
			if (rs.next()) {
				amount = rs.getDouble(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getServiceAmountCtype()_Error2:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return amount;
	}
	
	/************************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa especial de tipo de tarifa C para kilometraje	*
	 * **********************************************************************************************/
	public double getServiceAmountCtypeKm(Connection con, JavWebBookingGeneralMainForm aform, Embarque embarque, Global global) {//AAP26
		PreparedStatement pst = null;
		ResultSet rs = null;
		double amount = 0.0;
		try {
			
			String query = "select pack_web.FUN_FTCH_WT_VOL_AMNT_KM(?,?,?,?,?,?,?,?) from dual";
			
			pst = con.prepareStatement(query);

			pst.setString(1, global.getClientIdAgreement());
			pst.setString(2, aform.getOrginbranchcode());
			pst.setString(3, global.getKmTarifType());// distancia kilometros	
			pst.setString(4, aform.getTarifType());
			pst.setString(5, aform.getSs_refr_srvc_id());
			pst.setString(6, aform.getSs_srvc_id());
			pst.setDouble(7, embarque.getWeight());
			pst.setDouble(8, embarque.getVolume());
			
			rs = pst.executeQuery();			
			
			if (rs.next()) {
				amount = rs.getDouble(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getServiceAmountCtypeKm()_Error2:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return amount;
	}
	
	/****************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa especial de tipo de tarifa no C nueva	*
	 * **************************************************************************************/
	public double getServiceAmountTarifaEspecialNueva(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		double amount = 0;//monto de cobro tarifa normal o T7V (segun condicion)
		double amountp = 0;//monto de cobro tarifa T7P
		double totalv = 0;//total en volumen
		double totalp = 0;//total en peso
		//int cantidad = 0;//cantidad de bultos
		double peso = 0;
		double volumen = 0;
		try { 

			String query = "select pack_web.FUN_FTCH_TRIF_AMNT_NEW_BRNC(?,?,?,?,?) from dual";
			if (aform.getTarifa().equals("T7")) {
				/*obtiene tarifa por peso*/
				pst = con.prepareStatement(query);
				
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, aform.getDestinationcode());	
				pst.setString(3, aform.getSs_srvc_id());
				pst.setString(4, "T7P");
				pst.setString(5, aform.getOrginbranchcode());
				
				rs = pst.executeQuery();
				
				if (rs.next()) {
					amountp = rs.getDouble(1);
				}
				rs.close();
				pst.close();
				
				/*obtiene tarifa por volumen*/
				pst = con.prepareStatement(query);
				
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, aform.getDestinationcode());	
				pst.setString(3, aform.getSs_srvc_id());
				pst.setString(4, "T7V");
				pst.setString(5, aform.getOrginbranchcode());
				
				rs = pst.executeQuery();
				
				if (rs.next()) {
					amount = rs.getDouble(1);
				}
				
				/* se convierten los datos de la forma a valores enteros y dobles*/
				try {peso = Double.parseDouble(aform.getPeso()); 		} catch (Exception e) { peso = 0;}
				try {volumen = Double.parseDouble(aform.getVolumen()); 	} catch (Exception e) { volumen = 0;}
				
				/*se realiza calculo de precio en base a formulas*/
				totalp = amountp * peso;
				totalv = amount * volumen;
				
				if (totalp > totalv) {
					amount = totalp;
					aform.setCalculationdone("weight");
				} else {
					amount = totalv;
					aform.setCalculationdone("volume");
				}
			} else {
				
				pst = con.prepareStatement(query);
				
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, aform.getDestinationcode());	
				pst.setString(3, aform.getSs_srvc_id());
				pst.setString(4, aform.getTarifa());
				pst.setString(5, aform.getOrginbranchcode());
				
				rs = pst.executeQuery();
				
				if (rs.next()) {
					amount = rs.getDouble(1);
				}
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getServiceAmountTarifaEspecialNueva()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return amount;
	}
	
	/********************************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa especial de tipo de tarifa no C nueva por kilometro	*
	 * ******************************************************************************************************/
	public double getServiceAmountTarifaEspecialNuevaKm(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		double amount = 0;//monto de cobro tarifa normal o T7V (segun condicion)
		double amountp = 0;//monto de cobro tarifa T7P
		double totalv = 0;//total en volumen
		double totalp = 0;//total en peso
		//int cantidad = 0;//cantidad de bultos
		double peso = 0;
		double volumen = 0;
		try {

			String query = "select pack_web.FUN_FTCH_TRIF_AMNT_NEW_KM(?,?,?,?) from dual";
						
			if (aform.getTarifa().equals("T7")) {
				/*obtiene tarifa por peso*/
				pst = con.prepareStatement(query);
				
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, global.getKmTarifType());// distancia kilometros
				pst.setString(3, aform.getSs_srvc_id());
				pst.setString(4, "T7P");
				
				rs = pst.executeQuery();
				
				if (rs.next()) {
					amountp = rs.getDouble(1);
				}
				rs.close();
				pst.close();
				
				/*obtiene tarifa por volumen*/
				pst = con.prepareStatement(query);
				
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, global.getKmTarifType());// distancia kilometros	
				pst.setString(3, aform.getSs_srvc_id());
				pst.setString(4, "T7V");
				
				rs = pst.executeQuery();
				
				if (rs.next()) {
					amount = rs.getDouble(1);
				}
				
				/* se convierten los datos de la forma a valores enteros y dobles*/
				try {peso = Double.parseDouble(aform.getPeso()); 		} catch (Exception e) { peso = 0;}
				try {volumen = Double.parseDouble(aform.getVolumen()); 	} catch (Exception e) { volumen = 0;}
				
				/*se realiza calculo de precio en base a formulas*/
				totalp = amountp * peso;
				totalv = amount * volumen;
				
				if (totalp > totalv) {
					amount = totalp;
					aform.setCalculationdone("weight");
				} else {
					amount = totalv;
					aform.setCalculationdone("volume");
				}
			} else {
				
				pst = con.prepareStatement(query);
				
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, global.getKmTarifType());// distancia kilometros	
				pst.setString(3, aform.getSs_srvc_id());
				pst.setString(4, aform.getTarifa());
				rs = pst.executeQuery();
				
				if (rs.next()) {
					amount = rs.getDouble(1);
				}
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getServiceAmountTarifaEspecialNuevaKm()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return amount;
	}
	
	private void calculateKMTarifType(Connection con, Global global){
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "";
		String kmTarifType="";
		try {
			query = "select pack_web.FUN_CHK_ACTV_TRIF(?,?) from dual";
			pst = con.prepareStatement(query);
			pst.setString(1,global.getAssignedBranch());
			pst.setString(2,global.getDestinationBranchId());
			rs = pst.executeQuery();
						
			if(rs.next()){	
				kmTarifType = rs.getString(1);
			}		
			global.setKmTarifType(kmTarifType);			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("calculateKMTarifType()_Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.closeResources(rs, pst);
		}
	}

	//Este metodo obtiene los valores maximos para largo, ancho, alto, peso tanto para sobre como para caja
	private JavWebBookingGeneralMainForm getMAXSEG(Connection con, JavWebBookingGeneralMainForm aform) {
		CallableStatement cst = null;
		try {
			String str = "{ call PRO_GET_MAX_SEG(?,?,?,?,?)}";
			if (aform.getIsShippingTypeSEG().equalsIgnoreCase("N")) {
				str = "{ call PRO_GET_MAX_STD_V2(?,?,?,?,?,?,?)}";
			}
			cst = con.prepareCall(str);
			cst.setString(1, "PAQ");
			cst.registerOutParameter(2, Types.NUMERIC);
			cst.registerOutParameter(3, Types.NUMERIC);
			cst.registerOutParameter(4, Types.NUMERIC);
			cst.registerOutParameter(5, Types.NUMERIC);
			if (aform.getIsShippingTypeSEG().equalsIgnoreCase("N")) {
				cst.registerOutParameter(6, Types.NUMERIC);
				cst.setString(7, aform.getOrgienclave());
			}
			cst.execute();
			aform.setVolLMAXSEG(cst.getDouble(2));
			aform.setVolWMAXSEG(cst.getDouble(3));
			aform.setVolHMAXSEG(cst.getDouble(4));
			aform.setPesoMAXSEG(cst.getDouble(5));
			if (aform.getIsShippingTypeSEG().equalsIgnoreCase("N")) {
				aform.setVolMAX(cst.getDouble(6));
			}
			cst.close();
			cst = null;
			
			cst = con.prepareCall(str);
			cst.setString(1, "ENV");
			cst.registerOutParameter(2, Types.NUMERIC);
			cst.registerOutParameter(3, Types.NUMERIC);
			cst.registerOutParameter(4, Types.NUMERIC);
			cst.registerOutParameter(5, Types.NUMERIC);
			if (aform.getIsShippingTypeSEG().equalsIgnoreCase("N")) {
				cst.registerOutParameter(6, Types.NUMERIC);
				cst.setString(7, aform.getOrgienclave());
			}
			cst.execute();
			aform.setVolLMAXSEGSobre(cst.getDouble(2));
			aform.setVolWMAXSEGSobre(cst.getDouble(3));
			aform.setVolHMAXSEGSobre(cst.getDouble(4));
			aform.setPesoMAXSEGSobre(cst.getDouble(5));
			if (aform.getIsShippingTypeSEG().equalsIgnoreCase("N")) {
				aform.setVolMAXSobre(cst.getDouble(6));
			}
			cst.close();
			cst = null;
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("getMAXSEG()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (cst != null) {
					cst.close();
				}
			} catch (Exception e) {
				AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("getMAXSEG()_Error3:").append(e).toString());
				e.printStackTrace();
			}
		}
		return aform;
	}
	
	// Este metodo obtiene los valores mínimos para largo, ancho, alto, peso tanto
	// para sobre como para caja
	private JavWebBookingGeneralMainForm getMinMeasures(Connection con, JavWebBookingGeneralMainForm aform) {
		CallableStatement cst = null;
		try {
			String str = "{ call PRO_GET_MIN(?,?,?,?,?,?)}";
			
			cst = con.prepareCall(str);
			cst.setString(1, "PAQ");
			cst.registerOutParameter(2, Types.NUMERIC);
			cst.registerOutParameter(3, Types.NUMERIC);
			cst.registerOutParameter(4, Types.NUMERIC);
			cst.registerOutParameter(5, Types.NUMERIC);
			cst.registerOutParameter(6, Types.NUMERIC);
			
			cst.execute();
			aform.setVolLMinPaq(cst.getDouble(2));
			aform.setVolWMinPaq(cst.getDouble(3));
			aform.setVolHMinPaq(cst.getDouble(4));
			aform.setWghtMinPaq(cst.getDouble(5));
			aform.setVolMinPaq(cst.getDouble(6));
			cst.close();
			cst = null;
			
			cst = con.prepareCall(str);
			cst.setString(1, "ENV");
			cst.registerOutParameter(2, Types.NUMERIC);
			cst.registerOutParameter(3, Types.NUMERIC);
			cst.registerOutParameter(4, Types.NUMERIC);
			cst.registerOutParameter(5, Types.NUMERIC);
			cst.registerOutParameter(6, Types.NUMERIC);
			
			cst.execute();
			aform.setVolLMinEnv(cst.getDouble(2));
			aform.setVolWMinEnv(cst.getDouble(3));
			aform.setVolHMinEnv(cst.getDouble(4));
			aform.setWghtMinEnv(cst.getDouble(5));
			aform.setVolMinEnv(cst.getDouble(6));
			resources.cerrarCallableStatement(cst);
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("getMAXSEG()_Error1:").append(e)
					.toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return aform;
	}

	/********************************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa especial de tipo de tarifa por kilometro DE SERVICIO EXPRESS*
	 * ******************************************************************************************************/
	public DatosCalculosTarifaSEG getServiceAmountTarifaKmSEG(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		CallableStatement cst = null;
		DatosCalculosTarifaSEG calculosTarifaSEG = null;
		try {
			String query = "Begin ? := pack_web.fun_ftch_trif_amnt_km_SEG(?,?,?,?,?,?,?,?,?,?,?,?,?); End;";
			cst = con.prepareCall(query);
			cst.registerOutParameter(1, Types.NUMERIC);
			cst.setString(2, global.getClientIdAgreement());
			cst.setString(3, global.getKmTarifType());// distancia kilometros
			cst.setString(4, aform.getTypeSEGOperativa(aform.getShippingType()));
			cst.setLong(5, Integer.parseInt(aform.getCantidad()));
			cst.setString(6, aform.getSs_refr_srvc_id());
			cst.setDouble(7,Double.valueOf(aform.getPeso()));
			cst.setDouble(8,Double.valueOf(aform.getVolumen()));
			cst.setDouble(9,Double.valueOf(aform.getPeso()));
			cst.setDouble(10,Double.valueOf(aform.getWeightVolumetric()));
			cst.registerOutParameter(11, Types.VARCHAR);
			cst.registerOutParameter(12, Types.NUMERIC);
			cst.registerOutParameter(13, Types.NUMERIC);
			cst.registerOutParameter(14, Types.NUMERIC);
			cst.executeQuery();

			double amount = cst.getDouble(1);
			String tariff_id = cst.getString(11);
			double clnt_dis = cst.getDouble(12);
			double VEAD = cst.getDouble(13);
			double VRAD = cst.getDouble(14);
			calculosTarifaSEG = new DatosCalculosTarifaSEG();
			calculosTarifaSEG.setTarifa(tariff_id);
			calculosTarifaSEG.setvEAD(VEAD);
			calculosTarifaSEG.setvRAD(VRAD);
			calculosTarifaSEG.setvFlete(amount);
			calculosTarifaSEG.setClnt_dis(clnt_dis);
			calculosTarifaSEG.setTypeCalculoTariff("KM");
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getServiceAmountTarifaKmSEG()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return calculosTarifaSEG;
	}
	/****************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa especial de tipo de tarifa DE PUNTO A PUNTO DEL SERVICIO EXPRESS	*
	 * **************************************************************************************/
	public DatosCalculosTarifaSEG getServiceAmountTarifaEspecialSEG(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		CallableStatement cst = null;
		DatosCalculosTarifaSEG calculosTarifaSEG = null;
		try {
			String query = "Begin ? := pack_web.fun_ftch_trif_amnt_brnc_SEG(?,?,?,?,?,?,?,?,?,?,?,?,?,?); End;";
			cst = con.prepareCall(query);
			cst.registerOutParameter(1, Types.NUMERIC);
			cst.setString(2, global.getClientIdAgreement());
			cst.setString(3, aform.getDestinationcode());
			cst.setString(4, aform.getTypeSEGOperativa(aform.getShippingType()));
			cst.setString(5, aform.getOrginbranchcode());
			cst.setInt(6, Integer.parseInt(aform.getCantidad()));
			cst.setString(7, aform.getSs_refr_srvc_id());
			cst.setDouble(8,Double.valueOf(aform.getPeso()));
			cst.setDouble(9,Double.valueOf(aform.getVolumen()));
			cst.setDouble(10,Double.valueOf(aform.getPeso()));
			cst.setDouble(11,Double.valueOf(aform.getWeightVolumetric()));
			cst.registerOutParameter(12, Types.VARCHAR);
			cst.registerOutParameter(13, Types.NUMERIC);
			cst.registerOutParameter(14, Types.NUMERIC);
			cst.registerOutParameter(15, Types.NUMERIC);
			cst.executeQuery();

			double amount = cst.getDouble(1);
			String tariff_id = cst.getString(12);
			double clnt_dis = cst.getDouble(13);
			double VEAD = cst.getDouble(14);
			double VRAD = cst.getDouble(15);
			calculosTarifaSEG = new DatosCalculosTarifaSEG();
			calculosTarifaSEG.setTarifa(tariff_id);
			calculosTarifaSEG.setvEAD(VEAD);
			calculosTarifaSEG.setvRAD(VRAD);
			calculosTarifaSEG.setvFlete(amount);
			calculosTarifaSEG.setClnt_dis(clnt_dis);
			calculosTarifaSEG.setTypeCalculoTariff("BRNC");
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getServiceAmountTarifaEspecialSEG()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return calculosTarifaSEG;
	}
	/****************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa de piso								*
	 * **************************************************************************************/
	private DatosCalculosTarifaSEG getServiceAmountSEG(Connection con, JavWebBookingGeneralMainForm aform, Global global, boolean valPesoVol) {
		CallableStatement cst = null;
		DatosCalculosTarifaSEG calculosTarifaSEG = null;
		try {
			String query = "Begin ? := FUN_FTCH_SEG_TARIF(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); End;";//JSA01
			cst = con.prepareCall(query);
			
			cst.registerOutParameter(1, Types.NUMERIC);
			cst.setString(2, aform.getTypeSEGOperativa(aform.getShippingType()));
			ShipTypeSEG ship = findServiceSEG(aform);
			cst.setString(3, ship.getShipTypeSEGSrvcRefr());
			cst.setString(4, aform.getSs_refr_srvc_id());
			
			cst.setInt(5, Integer.parseInt(aform.getCantidad()));
			cst.setString(6, aform.getOrginbranchcode());
			cst.setString(7, aform.getDestinationcode());
			cst.setDouble(8,Double.valueOf(aform.getPeso()));//WEIGHT number
			cst.setDouble(9,Double.valueOf(aform.getVolumen()));
			cst.setDouble(10,Double.valueOf(aform.getPeso()));
			cst.setDouble(11,Double.valueOf(aform.getWeightVolumetric()));
			cst.setString(12, global.getClientIdAgreement());
			cst.registerOutParameter(13, Types.VARCHAR);
			cst.registerOutParameter(14, Types.NUMERIC);
			cst.registerOutParameter(15, Types.NUMERIC);
			cst.registerOutParameter(16, Types.NUMERIC);			
			cst.executeQuery();

			double returnvalue = new BigDecimal(cst.getDouble(1)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
			String tariff_id = cst.getString(13);
			double clnt_dis = new BigDecimal(cst.getDouble(14)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();;
			double VEAD = new BigDecimal(cst.getDouble(15)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();;
			double VRAD = new BigDecimal(cst.getDouble(16)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();;
			calculosTarifaSEG = new DatosCalculosTarifaSEG();
			calculosTarifaSEG.setTarifa(tariff_id);
			calculosTarifaSEG.setvEAD(VEAD);
			calculosTarifaSEG.setvRAD(VRAD);
			calculosTarifaSEG.setvFlete(returnvalue);
			calculosTarifaSEG.setClnt_dis(clnt_dis);
			calculosTarifaSEG.setTypeCalculoTariff("PISO");
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getServiceAmount()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {			
			resources.cerrarCallableStatement(cst);
		}		
		return calculosTarifaSEG;
	}	
	private ShipTypeSEG findServiceSEG(JavWebBookingGeneralMainForm conversionform){
		String shipType = conversionform.getShippingType();
	    int i = 0;
	    for (i = 0; i < conversionform.getShippingTypeSEGALL().size(); i++) {
	    	if (conversionform.getShippingTypeSEGALL().get(i).getShipTypeSEGSrvc().equals(shipType)) {
	    		return conversionform.getShippingTypeSEGALL().get(i);
	    	}
	    }
	    return null;
    }

	public class DatosCalculosTarifaSEG {
		String typeCalculoTariff, tarifa;
		Double clnt_dis, vEAD, vRAD, vFlete;
		public Double getvFlete() {
			return vFlete;
		}
		public void setvFlete(Double vFlete) {
			this.vFlete = vFlete;
		}
		public Double getClnt_dis() {
			return clnt_dis;
		}
		public void setClnt_dis(Double clnt_dis) {
			this.clnt_dis = clnt_dis;
		}
		public Double getvEAD() {
			return vEAD;
		}
		public void setvEAD(Double vEAD) {
			this.vEAD = vEAD;
		}
		public Double getvRAD() {
			return vRAD;
		}
		public void setvRAD(Double vRAD) {
			this.vRAD = vRAD;
		}
		public String getTarifa() {
			return tarifa;
		}
		public void setTarifa(String tarifa) {
			this.tarifa = tarifa;
		}
		public String getTypeCalculoTariff() {
			return typeCalculoTariff;
		}
		public void setTypeCalculoTariff(String typeCalculoTariff) {
			this.typeCalculoTariff = typeCalculoTariff;
		}
		@Override
		public String toString() {
			return "DatosCalculosTarifaSEG [typeCalculoTariff="
					+ typeCalculoTariff + ", tarifa=" + tarifa + ", clnt_dis="
					+ clnt_dis + ", vEAD=" + vEAD + ", vRAD=" + vRAD
					+ ", vFlete=" + vFlete + "]";
		}
		
	}
	
	private String getMsgDimensionesIngresar(Connection con) {
		String result = "";
		try {
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList temp = cons.QryMdulTypeParm1(con, "WEB", "TMP_MSGE", "MSGDIMINGR");
			if (!temp.isEmpty()) {
				result = temp.isEmpty() ? "" : ((ArrayList) temp .get(0)).get(4).toString();// obteniendo PM_VLUE1_DESC	
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getMsgDimensionesIngresar()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	
	private String getMsgAdvertenciaDetalleEnvio(Connection con) {
		String result = "";
		try {
			ConsultaParametros cons = new ConsultaParametros();// Llama al método para obtener los parámetros: WEB, TMP_MSGE, MSGDIMINF	
			ArrayList temp = cons.QryMdulTypeParm1(con, "WEB", "TMP_MSGE", "MSGDIMINF");
			if (!temp.isEmpty()) {
				ArrayList primeraFila = (ArrayList) temp.get(0); // Obtiene la primera fila de resultados
				String mensaje = primeraFila.get(4) != null ? primeraFila.get(4).toString() : ""; // Obtiene el contenido de PM_VLUE1_DESC
				String valorId = primeraFila.get(2) != null ? primeraFila.get(2).toString() : "NULO_O_VACIO";// Obtiene el contenido de PM_VLUE1_ID
				result = (valorId.equals("1")) ? mensaje : ""; 
				}
		} catch (Exception e) {
			// Log de error (asume que concatena y msgErr están definidos)
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getMsgDimensionesInfo()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	
	/*Clientes C, costo base mas excendente, clientes por kilo.
    Recalcula todas las lineas de flete capturas*///AAP26
	@SuppressWarnings("rawtypes")
	private ArrayList amountRecalcTariffCType(Connection con, HttpSession session, JavWebBookingGeneralMainForm aform, Global global, ArrayList servicesDetailArray, String serviceId, String referenceServiceId){
	    
	    Embarque embarque = new Embarque();
	    String serviceValue = "";
	    double amount = 0.0;
	    double prorationAmount = 0.0;
	    double pesoTotal = 0;
	    double volumenTotal = 0;        
	    boolean doCalc = false;
	    
	    try {           
	        if (global.getTarifType().equals("C")) {//solo si es tarifa C
	        	
	            if (aform.getDefaultservicescreen().equalsIgnoreCase("yes")) {//si tiene convenio
	                
	                HashMap values = getTotalWeightVolume(servicesDetailArray);//calcula peso volumen y total de bultos
	                pesoTotal = (Double) values.get("pesoTotal");
	                volumenTotal = (Double) values.get("volumenTotal");
	                
	                embarque.setWeight(pesoTotal);
	                embarque.setVolume(volumenTotal);
	                
	                if (aform.getDefaultservicescreenKm().equals("Y")) {//si tiene covenio por kilometros
	                    serviceValue = getAdditionalValueFactorKm(con, global, serviceId, referenceServiceId, "KG");//valida si tiene calculo por KG en configuracion por rango de kilometros
	                    if (serviceValue.equals("K")) {                                                    
	                        amount = getServiceAmountCtypeKm(con, aform, embarque, global);//obtiene costo por rango de km
	                        doCalc = true;
	                    }
	                } else {
	                    serviceValue = getAdditionalValueFactor(con, global, serviceId, referenceServiceId, "KG");//valida si tiene calculo por KG en configuracion punto a punto
	                    if (serviceValue.equals("K")) {
	                        amount = getServiceAmountCtype(con, aform, embarque, global);//obtiene costo por punto a punto
	                        doCalc = true;
	                    }
	                }
	                
	                if (doCalc) {
	                    if (amount>0) {                            
	                        prorationAmount = amount / pesoTotal;
	                        servicesDetailArray = recalcLines(servicesDetailArray, prorationAmount);
	                        session.setAttribute("servicesDetail", servicesDetailArray);//se almacena arreglo modificado en session.
	                    }
	                }
	            }
	        }
	        
	    } catch (Exception e) {
	        AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("amountRecalc()_Error:").append(e).toString());
	        e.printStackTrace();
	    }
	    return servicesDetailArray;
	}
	/*Calcula el total de peso volumen y bultos de todas las lineas de captura*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap getTotalWeightVolume(ArrayList servicesDetailArray) {//AAP26
	    int cantidad = 0;//cantidad de bultos
	    double peso = 0;
	    double volumen = 0;
	    double pesoTotal = 0;
	    double volumenTotal = 0;
	    int totalBultos = 0;
	    HashMap result = new HashMap(3);
	    try {
	        ShipmentServiceDetail ssd = null;
	        for (int i = 0; i < servicesDetailArray.size(); i++) {
	            ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
	
	            /* se convierten los datos de la forma a valores enteros y dobles*/
	            try {cantidad = Integer.parseInt(ssd.getCantidad().trim());	} catch (Exception e) { cantidad = 0;}
	            try {peso = Double.parseDouble(ssd.getPeso()); 				} catch (Exception e) { peso = 0;}
	            try {volumen = Double.parseDouble(ssd.getVolumen());		} catch (Exception e) { volumen = 0;}
	            
	            peso =  peso * cantidad;
	            volumen = volumen * cantidad;
	            
	            pesoTotal = pesoTotal + peso;
	            volumenTotal = volumenTotal + volumen;
	            totalBultos = totalBultos + cantidad;
	            result.put("pesoTotal", pesoTotal);
	            result.put("volumenTotal", volumenTotal);
	            result.put("totalBultos", totalBultos);
	        }
	    } catch (Exception e) {
	        AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getTotalWeightVolume()_Error:").append(e).toString());
	        e.printStackTrace();
	    }
	    return result;
	}
	
	/*obtiene si el cobro de flete será por Kilos (Configuracion punto a punto)*/
	private String getAdditionalValueFactor(Connection con, Global global, String serviceId, String referenceServiceId, String factor) {//AAP26
	    String serviceValue = "N";
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    try {
	        String query = concatena.delete(0,concatena.length())
	                .append("select nvl(wcp_srvc_cant,'E') from WEB_CLNT_SRVC_TRIF_FACTOR where wcp_orgn_clnt_id = ? and ")
	                .append(" substr(WCP_ORGN_BRNC_ID,1,3) = substr(?,1,3) and substr(WCP_DEST_BRNC_ID,1,3) = substr(?,1,3) and wcp_trif_slab = ? and ")
	                .append("wcp_refr_srvc_id= ? and wcp_srvc_id = ? and WCP_FACTOR = ? order by WCP_FACTOR_VALUE desc").toString();
	        pst = con.prepareStatement(query);
	        pst.setString(1, global.getClientIdAgreement());
	        pst.setString(2, global.getAssignedBranch());
	        pst.setString(3, global.getDestinationBranchId());
	        pst.setString(4, global.getTarifType());
	        pst.setString(5, referenceServiceId);
	        pst.setString(6, serviceId);
	        pst.setString(7, factor);            
	        rs = pst.executeQuery();
	
	        if (rs.next()) {
	            serviceValue = rs.getString(1);
	        }
	    } catch (Exception e) {
	        AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getAdditionalValueFactor()_Error:").append(e).toString());
	        e.printStackTrace();
	    } finally {
	        resources.closeResources(rs, pst);			
	    }
	    return serviceValue;
	}
	
	/*obtiene si el cobro de flete será por Kilos (Configuracion rango de km)*/
	private String getAdditionalValueFactorKm(Connection con, Global global, String serviceId, String referenceServiceId, String factor) {
	    String serviceValue = "N";
	    PreparedStatement pst = null;
	    ResultSet rs = null;
	    try {
	        String query = concatena.delete(0,concatena.length())
	                .append("select NVL(WCK_SRVC_CANT, 'E') from web_clnt_srvc_trif_factor_km where wck_orgn_clnt_id = ? and ")
	                .append("? between WCK_DSTN_FROM_KM and WCK_DSTN_TO_KM and WCK_trif_slab = ? and ")
	                .append("WCK_refr_srvc_id= ? and WCK_srvc_id = ? and WCK_FACTOR = ? order by WCK_FACTOR_VALUE desc").toString();
	        pst = con.prepareStatement(query);
	        pst.setString(1, global.getClientIdAgreement());
	        pst.setString(2, global.getKmTarifType());
	        pst.setString(3, global.getTarifType());
	        pst.setString(4, referenceServiceId);
	        pst.setString(5, serviceId);
	        pst.setString(6, factor);
	        rs = pst.executeQuery();
	
	        if (rs.next()) {
	            serviceValue = rs.getString(1);
	        }
	    } catch (Exception e) {
	        AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getAdditionalValueFactorKm()_Error:").append(e).toString());
	        e.printStackTrace();
	    } finally {
	        resources.closeResources(rs, pst);			
	    }
	    return serviceValue;
	}
	/*realiza prorrateo por linea, del costo total del flete*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList recalcLines(ArrayList servicesDetailArray, double proration) {//AAP26
	    int cantidad = 0;//cantidad de bultos
	    double peso = 0;
	    double dCostoTarifaBase = 0;
	    double descuento = 0;
	    double amount = 0;
	    DecimalFormat df = new DecimalFormat("0.00");        
	    try {
	        ShipmentServiceDetail ssd = null;
	        for (int i = 0; i < servicesDetailArray.size(); i++) {
	            ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
	
	            
	            /* se convierten los datos de la forma a valores enteros y dobles*/					
	            try {cantidad = Integer.parseInt(ssd.getCantidad().trim());} catch (Exception e) { cantidad = 0;}
	            try {peso = Double.parseDouble(ssd.getPeso());		} catch (Exception e) { peso = 0;}
	            
	            peso =  peso * cantidad;
	            amount = proration * peso;
	            
	            ssd.setImporte(df.format(amount));               
	            
	            try { dCostoTarifaBase = Double.parseDouble(ssd.getCostoTarifaBase()); } catch (Exception e) { dCostoTarifaBase = 0; }
	            descuento = dCostoTarifaBase - amount;
	
	            if (descuento<0){
	                ssd.setConvenioAlto("Y");                
	            } else {
	                ssd.setConvenioAlto("N");                
	            }
	            servicesDetailArray.set(i, ssd);
	        }
	    } catch (Exception e) {
	        AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("getTotalWeightVolume()_Error:").append(e).toString());
	        e.printStackTrace();
	    }
	    return servicesDetailArray;
	}
	
	/*
	 * ****************************************************************************** ******************************** 
	 * metodo para determinar si la linea de envio contiene alguna configuracion dependiendo del tipo de usuario *
	 * *************************************************************************************************************
	 */
	private boolean srvcConfigHasAmnt(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		double packets = 0;
		String query = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			if (global.tarifType.equalsIgnoreCase("c")) {
				// verifica que que el cliente tipo C tenga alguna configuracion dependiendo de
				// su tipo de tarifa
				packets = typeTarifClientC(con, aform, global);

			} /* TARIFA FIJA ANTERIOR */
			else if (global.clasifTarif.equals("0")) {
				// Se genera una consulta tipo contador para determinar si existen valores
				// dentro de la tabla que coincidan con el caso
				query = "SELECT COUNT(*) FROM WEB_CLNT_SRVC_TRIF WHERE WT_ORGN_CLNT_ID =? AND substr(WT_ORGN_BRNC_ID,1,3) = substr(?,1,3) AND substr(WT_DEST_BRNC_ID,1,3) = substr(?,1,3) AND WT_TRIF_SLAB = ? AND WT_REFR_SRVC_ID = ? AND WT_SRVC_ID = ?";
				pst = con.prepareStatement(query);

				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, aform.getOrginbranchcode());// global.assignedBranch
				pst.setString(3, aform.getDestinationcode());// global.destinationBranchId
				pst.setString(4, global.tarifType);
				pst.setString(5, aform.getSs_refr_srvc_id());
				pst.setString(6, aform.getSs_srvc_id());
				rs = pst.executeQuery();
				if (rs.next()) {
					packets = rs.getDouble(1);
				}
				rs.close();
				pst.close();

			} /* TARIFA FIJA NUEVA */
			else {
				// Se genera una consulta tipo contador para determinar si existen valores
				// dentro de la tabla que coincidan con el caso
				query = "SELECT COUNT(*) FROM WEB_CLNT_SRVC_TRIF_DTL WHERE TD_ORGN_CLNT_ID = ? AND substr(TD_ORGN_BRNC_ID,1,3) = substr(?,1,3) AND substr(TD_DEST_BRNC_ID,1,3) = substr(?,1,3) AND TD_SRVC_ID = ? AND substr(TD_TRIF_ID,1,2) = ?";
				pst = con.prepareStatement(query);
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, aform.getOrginbranchcode());
				pst.setString(3, aform.getDestinationcode());
				pst.setString(4, aform.getSs_srvc_id());
				pst.setString(5, aform.getTarifa());
				rs = pst.executeQuery();
				if (rs.next()) {
					packets = rs.getDouble(1);
				}
				rs.close();
				pst.close();
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr)
					.append("importeTarifaEspecial()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return (packets > 0);
	}

	private double typeTarifClientC(Connection con, JavWebBookingGeneralMainForm aform, Global global) {

		double packets = 0;
		String query = "";
		String tipoTarifaC = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			// El query verifica si el tipo de cliente C corresponde a una tarifa anterior o
			// nueva
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
			// si se encuentra un valor dentro del query anterior, se determina que es un
			// usuario de tipo C con tarifa vieja y se busca alguna configuracion
			if (tipoTarifaC.length() > 0) {

				query = "SELECT COUNT(*) FROM WEB_CLNT_SRVC_TRIF WHERE WT_ORGN_CLNT_ID = ? AND substr(WT_ORGN_BRNC_ID,1,3) = substr(?,1,3) AND substr(WT_DEST_BRNC_ID,1,3) = substr(?,1,3) AND WT_TRIF_SLAB = ? AND WT_REFR_SRVC_ID = ? AND  WT_SRVC_ID = ? AND (WT_FACTOR = 'NON' OR WT_FACTOR = 'KG' OR WT_FACTOR = 'CUM')";
				pst = con.prepareStatement(query);
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, aform.getOrginbranchcode());
				pst.setString(3, aform.getDestinationcode());
				pst.setString(4, aform.getTarifType());
				pst.setString(5, aform.getSs_refr_srvc_id());
				pst.setString(6, aform.getSs_srvc_id());
				rs = pst.executeQuery();
				if (rs.next()) {
					packets = rs.getDouble(1);
				}
				rs.close();
				pst.close();

				// En caso contrario, se busca configuracion en tarifa nueva
			} else if (tipoTarifaC.equals("")) {
				query = "SELECT COUNT(*) FROM WEB_CLNT_SRVC_TRIF_FACTOR WHERE WCP_ORGN_CLNT_ID = ? AND substr(WCP_ORGN_BRNC_ID,1,3) = substr(?,1,3) AND substr(WCP_DEST_BRNC_ID,1,3) = substr(?,1,3) AND WCP_TRIF_SLAB = ? AND WCP_REFR_SRVC_ID = ? AND WCP_SRVC_ID = ? AND (WCP_FACTOR = 'KG' OR WCP_FACTOR = 'CUM')";
				pst = con.prepareStatement(query);
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, aform.getOrginbranchcode());
				pst.setString(3, aform.getDestinationcode());
				pst.setString(4, aform.getTarifType());
				pst.setString(5, aform.getSs_refr_srvc_id());
				pst.setString(6, aform.getSs_srvc_id());
				rs = pst.executeQuery();
				if (rs.next()) {
					packets = rs.getDouble(1);
				}
				rs.close();
				pst.close();
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
	
	//generacion de mensaje una vez encuentre un centro de costo sin cobertura
		public StringBuilder msgCCSinCobertura(Connection con, JavWebBookingGeneralMainForm aform, String desCCcosto ) {
			StringBuilder mensaje = new StringBuilder("La direccion "); 
			String colonia = "",  CP = "", ciudad = "", estado= ""; 
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
	
	public String validSATProduct(Connection con, String prodId, String prodDes) {
		JavCatProducts catProducts = new JavCatProducts();
		SysSspDesMstrDTO product = catProducts.getProductById(con, prodId);
		String description = prodDes.contains("-") ? 
				prodDes.trim().substring(prodDes.indexOf("-")+1).trim() :
					prodDes;
		if (product.getCode() == null || !description.equalsIgnoreCase(product.getDescr())) {
			return "Producto \""+description+"\" No encontrado. Favor de seleccionar un producto del catalogo.";
		}
		return "Y";
	}
}