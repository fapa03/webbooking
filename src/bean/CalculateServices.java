/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creaci?n: 15/12/2011
 * Compa??a: PAQUETEXPRESS.
 * Descripci?n del programa: Bean para calculo de servicios.
 * FileName: CalculateServices.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP04
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 21/06/2012
 * Descripci?n:  Se agreg? modificacion para calcular tarifa en EAD, ya no se envia el monto minimo en la funcion 
 * que calcula los descuentos, ahora todo el proceso se hace en el java, debido a que hay que comparar los montos
 * deel total a cobrar contra el monto minimo de EAD, para ver cual de los dos aplica.
 * ------------------------------------------------------------------
 * Clave: AAP05
 * Autor: ABRAHAM ARJONA
 * Fecha: 
 * Descripci?n: Modificacion para no calcular iva retenido cuando se esta documentando solo sobres.
 * ------------------------------------------------------------------
 * Clave: AAP06
 * Autor: ABRAHAM ARJONA
 * Fecha: 
 * Descripci?n: Modificaciones para nuevo proceso para zona extendida
 * ------------------------------------------------------------------
 * Clave: AAP07
 * Autor: ABRAHAM ARJONA
 * Fecha: 01/07/2013
 * Descripci?n: Modificaciones para verificar si es flete por cobrar, validar que el cliente sea individuo
 * para no retener iva.
 * ------------------------------------------------------------------ 
 * Clave: AAP10
 * Autor: ABRAHAM ARJONA
 * Fecha: 26/12/2014
 * Descripci?n: Modificaciones para nuevas reglas de SOBRE (cobro de 140 pesos fijo antes de descuentos).
 * 
 * ------------------------------------------------------------------ 
 * Clave: AAP11
 * Autor: ABRAHAM ARJONA
 * Fecha: 27/01/2015
 * Descripci?n: Modificaciones para calcular descuentos en servicios a tarifa "A"
 * ------------------------------------------------------------------ 
 * Clave: AAP12
 * Autor: ABRAHAM ARJONA
 * Fecha: 23/06/2015
 * Descripci?n: Se elimin? l?gica para omitir cobro de seguro. Ahora se bloquea captura de monto en valor declarado
 * ------------------------------------------------------------------
 * Clave: AAP16
 * Autor: ABRAHAM ARJONA
 * Fecha: 29/01/2016
 * Descripci?n: Se Modifica c?lculo de servicios especiales (adicionales) para tomar los datos de importes desde la clase "AdditionalService"
 * Ahora existen servicios especiales con posibilidad de modificar el importe y se almacena en el atributo "importe" de la clase.
 * ------------------------------------------------------------------
 * Clave: AAP17
 * Autor: ABRAHAM ARJONA
 * Fecha: 15/06/2017
 * Descripci?n: calculo para servicio ELC-1 (ENTREGA LOCAL). Para proyecto COSTO BASE MAS EXCEDENTES.
 * ------------------------------------------------------------------  
 * Clave: JASA01
 * Autor: ARMANDO SANCHEZ
 * Fecha: 13/05/2020
 * Descripci?n: Se implementa cambios para asignar la retencion ya sea con 0 o con valor
 * ------------------------------------------------------------------
 */
package bean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.paquetexpress.core.web.util.Rfc;
import com.paquetexpress.www.webbooking.Documentacion.ShipTypeSEG;

import logger.AccessLog;
import beanForm.JavWebBookingGeneralMainForm;

public class CalculateServices {
	private StringBuffer conct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void calculateServices(Connection con, JavWebBookingGeneralMainForm aform, HttpServletRequest req, Global global) {		
		try {
			ArrayList calServicesList = null;
			HttpSession session = req.getSession(false);
			ServicesGlobal servicesGlobal = (ServicesGlobal) session.getAttribute("servicesGlobal");
			String groupClientId = global.groupClientId;
			ArrayList servicesDetailArray = null;
			ServicesTotal servicesTotal = null;// For display
			String mostrarDescuentos = "";//AAP11

			servicesDetailArray = (ArrayList) session.getAttribute("servicesDetail");			
			servicesDetailArray = companyDefine(con, servicesDetailArray, aform, global, null); //recalcula compa?ia en detalle de envio, en base a forma de pago//AAP23
			session.setAttribute("servicesDetail", servicesDetailArray);//AAP23

			double shipEimporte = 0.0;
			double shipEcostoBase = 0.0;//AAP11
			double shipGimporte = 0.0;
			double shipGcostoBase = 0.0;//AAP11
			boolean isShipEExists = false;
			boolean isShipGExists = false;
			boolean envelopeOnly = false;//AAP10
			int shipEQty = 0;
			int shipGQty = 0;
			double totalImporte = 0.0;
			double totalCostobase = 0.0;//AAP11
			double descuentoBase = 0.00;//AAP11			
			int totalQty = 0;
			double totalWeight = 0.0;
			double totalVolume = 0.0;
			
			boolean noRetiene = false;//AAP07
			boolean noRetieneShipE = false;//JSA02
			String shipECompanyID = "";//JSA02
			String validClntRnt = groupClientId;//ADAP27
			if (aform.getFormaPago().equals("TO_PAY")) {//AAP07
				validClntRnt = aform.getDestinationclave();
			}

			ArrayList infoCliente = getClientInfo(con, validClntRnt);// AAP07
			if (!infoCliente.isEmpty()) {// AAP07
				if (infoCliente.get(1).toString().equals("I")
						|| infoCliente.get(1).toString().equals("N")) {// AAP07
					noRetiene = true;// AAP07
				} else if ((infoCliente.get(1).toString().equals("C") || infoCliente
						.get(1).toString().equals("G"))
						&& infoCliente.get(2).equals("N")) {
					noRetiene = true;// AAP07
				}// AAP07
			}// AAP07
		
			if (aform.getFormaPago().equals("TO_PAY")) {//JSA02
				Rfc r = new Rfc();
				boolean rfcGenerico = r.isRfcGenerico(aform.getDestinationrfc());
				if(rfcGenerico) {
					noRetiene = true;
				}
			}
			boolean continuar=false;
			double descuento = 0.00;			
			double doubleIva = 0.00;
			double doubleIvaRet = 0.00;
			double doubleServicios = 0.0;
			DecimalFormat df = new DecimalFormat("0.00");
			boolean tieneSetAsignado = false;
			String companyTemp = null, companyChange = null;
			int repeticiones=0;
			do {
				servicesTotal = new ServicesTotal();
				calServicesList = new ArrayList();
				shipEimporte = 0.0;
				shipEcostoBase = 0.0;//AAP11
				shipGimporte = 0.0;
				shipGcostoBase = 0.0;//AAP11
				isShipEExists = false;
				isShipGExists = false;
				envelopeOnly = false;//AAP10
				shipEQty = 0;
				shipGQty = 0;
				totalImporte = 0.0;
				totalCostobase = 0.0;//AAP11
				descuentoBase = 0.00;//AAP11			
				totalQty = 0;
				totalWeight = 0.0;
				totalVolume = 0.0;

				shipECompanyID = "";
			Map<String, Double> mapShipGimporteCompany = new HashMap<String, Double>();
			Map<String, Double> mapShipGcostoBaseCompany = new HashMap<String, Double>();
			Map<String, Integer> mapShipGQtyCompany = new HashMap<String, Integer>();
			List<String> listCompany = new ArrayList<String>();
			Map<String, Double> mapImportTotal = new HashMap<String, Double>();
			Double shipGCompany = 0D;
			ShipmentServiceDetail shipEDetaild = null;
			for (int i = 0; i < servicesDetailArray.size(); i++) {				
				ShipmentServiceDetail ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
				mostrarDescuentos = ssd.getMostrarDesCuento();
				if (ssd.getServiceId().equalsIgnoreCase("SHP-E")) {
					noRetieneShipE = !getRetIvaByCompany(con, ssd.getCompanyID());
					shipECompanyID = ssd.getCompanyID();
					shipEimporte = shipEimporte + Double.parseDouble(ssd.getImporte());
					
					/*si el convenio no es mas alto que el costo base, se suma importe neto como costo base
					 * si no, se suma costo base normalmente*/
					if (ssd.getConvenioAlto().equals("Y")){
						shipEcostoBase = shipEcostoBase + Double.parseDouble(ssd.getImporte());//AAP11
					} else {
						shipEcostoBase = shipEcostoBase + Double.parseDouble(ssd.getCostoTarifaBase());//AAP11
					}
					
					isShipEExists = true;
					shipEDetaild = ssd;
					shipEQty = shipEQty + Integer.parseInt(ssd.getCantidad());
				} else if (ssd.getServiceId().equalsIgnoreCase("SHP-G")) {
					shipGimporte = shipGimporte + Double.parseDouble(ssd.getImporte());
					if(!listCompany.contains(ssd.getCompanyID())){
						listCompany.add(ssd.getCompanyID());
					}
					String key = "shipGimporteCompany" + ssd.getCompanyID();
					if (mapShipGimporteCompany.containsKey(key)) {
						shipGCompany = mapShipGimporteCompany.get(key) + Double.parseDouble(ssd.getImporte());
					} else {
						shipGCompany = Double.parseDouble(ssd.getImporte());
					}
					mapShipGimporteCompany.put(key, shipGCompany);
					
					key = "shipGcostoBaseCompany" + ssd.getCompanyID();
					/*si el convenio no es mas alto que el costo base, se suma importe neto como costo base
					 * si no, se suma costo base normalmente*/
					if (ssd.getConvenioAlto().equals("Y")){						
						shipGcostoBase = shipGcostoBase + Double.parseDouble(ssd.getImporte());//AAP11
						if (mapShipGcostoBaseCompany.containsKey(key)) {
							shipGCompany = mapShipGcostoBaseCompany.get(key) + Double.parseDouble(ssd.getImporte());
						} else {
							shipGCompany = Double.parseDouble(ssd.getImporte());
						}
					} else {
						shipGcostoBase = shipGcostoBase + Double.parseDouble(ssd.getCostoTarifaBase());//AAP11
						if (mapShipGcostoBaseCompany.containsKey(key)) {
							shipGCompany = mapShipGcostoBaseCompany.get(key) + Double.parseDouble(ssd.getCostoTarifaBase());
						} else {
							shipGCompany = Double.parseDouble(ssd.getCostoTarifaBase());
						}
					}
					mapShipGcostoBaseCompany.put(key, shipGCompany);
					
					isShipGExists = true;
					shipGQty = shipGQty + Integer.parseInt(ssd.getCantidad());		
					key = "shipGQtyCompany" + ssd.getCompanyID();
					int shipGQtyCompany = 0;
					if (mapShipGQtyCompany.containsKey(key)) {
						shipGQtyCompany = mapShipGQtyCompany.get(key) + Integer.parseInt(ssd.getCantidad());
					} else {
						shipGQtyCompany = Integer.parseInt(ssd.getCantidad());
					}
					mapShipGQtyCompany.put(key, shipGQtyCompany);
				}				
				
				totalImporte = totalImporte + Double.parseDouble(ssd.getImporte());
				totalQty = totalQty + Integer.parseInt(ssd.getCantidad());
				servicesTotal.totalPack = String.valueOf(totalQty);

				totalWeight = totalWeight + (Integer.parseInt(ssd.getCantidad()) * Double.parseDouble(ssd.getPeso()));
				totalVolume = totalVolume + (Integer.parseInt(ssd.getCantidad()) * Double.parseDouble(ssd.getVolumen()));

				servicesTotal.totalWeight = df.format(totalWeight);
				servicesTotal.totalVolume = df.format(totalVolume);

			}		
			totalCostobase = shipGcostoBase + shipEcostoBase;//AAP11
			if (totalCostobase>totalImporte) {//AAP11
				servicesTotal.flete = df.format(totalCostobase);//AAP11
			} else {//AAP11
				servicesTotal.flete = df.format(totalImporte);//AAP11
			}//AAP11
			
			descuento = 0.00;			
			doubleIva = 0.00;
			doubleIvaRet = 0.00;
			doubleServicios = 0.0;// Sum of ALL SubTotal//AAP17

			// ShipE Service
			// 0---slab 1---- discountAmount
			String discountShipE[] = new String[2];
			if (isShipEExists) {
				
				if (!isShipGExists) {//AAP10
					envelopeOnly = true;
				}
				double minamount = 0.0;
				if(aform.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
					discountShipE[0] = null;
					discountShipE[1] = "0";
					if (shipEDetaild != null && shipEDetaild.getDatosCalculosTarifaSEG().getTypeCalculoTariff().equalsIgnoreCase("PISO")) {
						ShipTypeSEG ship = findServiceSEG(aform);
						discountShipE = calculateQuantity(con, groupClientId, ship.getShipTypeSEGSrvcRefr(), df.format(shipEQty), df.format(shipEimporte), df.format(minamount));						
					} else {
						if (shipEcostoBase>0 && shipEcostoBase>shipEimporte) {
							descuentoBase = shipEcostoBase - shipEimporte;
							discountShipE[1] =  String.valueOf( df.format(descuentoBase) );
							shipEimporte = shipEcostoBase;
						}
					}
				} else if (aform.getDefaultservicescreen().equalsIgnoreCase("yes")) {
					discountShipE[0] = null;
					discountShipE[1] = "0";
					if (shipEcostoBase>0 && shipEcostoBase>shipEimporte) {//AAP11		
						descuentoBase = shipEcostoBase - shipEimporte;//AAP11
						discountShipE[1] =  String.valueOf( df.format(descuentoBase) );//AAP11
						shipEimporte = shipEcostoBase;//AAP11
					}//AAP11
				} else {
					discountShipE = calculateQuantity(con, groupClientId, "ENVELOPES", df.format(shipEQty), df.format(shipEimporte), df.format(minamount));
				}
				descuento = descuento + Double.parseDouble(discountShipE[1]);
				String tax[] = getTax( con, global, "ENVELOPES", df.format(shipEimporte - Double.parseDouble(discountShipE[1])), noRetieneShipE, shipECompanyID, aform.getFormaPago());
				doubleIva = doubleIva + Double.parseDouble(tax[0]);
				Services shipE = new Services();
				if (tax[2] != null) {
					tieneSetAsignado = true;
					shipE.setGS_CMPY_ID(tax[2]);
					companyTemp = shipECompanyID;
					if (!companyTemp.equalsIgnoreCase(tax[2])) {
						companyChange = tax[2];
					}
				} else {
					shipE.setGS_CMPY_ID(shipECompanyID);
				}
				String descriptionShipE = getServicesAditionalDescription(con, "SHP-E");
				shipE.descriptionAditional = descriptionShipE;
				shipE.GS_SRVC_ID = "SHP-E";
				shipE.GS_DISC = discountShipE[1];
				shipE.GS_TAX = tax[0];
				shipE.GS_TAX_RET = tax[1];//AAP05
				doubleIvaRet = doubleIvaRet + Double.parseDouble(tax[1]);//AAP05
				shipE.GS_SUB_TOTL = df.format(shipEimporte);
				shipE.GS_ADD_PYMT_FLAG = "N";
				shipE.GS_SRVC_TYPE = "S";
				shipE.GS_DOCU_TYPE = "Q";
				shipE.GS_GUIA_TYPE = "H";
				shipE.GS_STUS_FLAG = "A";
				shipE.GS_DISC_SLAB_NO = discountShipE[0];
				shipE.TOTAL = df.format((shipEimporte) - Double.parseDouble(discountShipE[1]));
				if (mapImportTotal.containsKey(shipECompanyID)) {
					mapImportTotal.put(shipECompanyID, mapImportTotal.get(shipECompanyID) + Double.parseDouble(shipE.TOTAL));
				} else {
					mapImportTotal.put(shipECompanyID,  Double.parseDouble(shipE.TOTAL));
				}
				Hashtable calServicestable = new Hashtable();
				calServicestable.put("shipE", shipE);
				calServicesList.add(calServicestable);

			}
			// End ShipE Service

			// ShipG services
			if (isShipGExists) {
				List<Object> list;
				Services shipGComp;
				for (int i = 0; i < listCompany.size(); i++) {
					list = calculoShipG(con, aform, global, mapShipGcostoBaseCompany.get("shipGcostoBaseCompany" + listCompany.get(i)), mapShipGimporteCompany.get("shipGimporteCompany" + listCompany.get(i)), df, groupClientId, mapShipGQtyCompany.get("shipGQtyCompany" + listCompany.get(i)), servicesDetailArray, noRetiene, listCompany.get(i));
					shipGComp = (Services) list.get(0);
					descuento += (double) list.get(1);
					doubleIva += (double) list.get(2);
					doubleIvaRet += (double) list.get(3);
					
					if (list.get(4) != null) {
						shipGComp.setGS_CMPY_ID(list.get(4).toString());
						tieneSetAsignado = true;
						companyTemp = listCompany.get(i);
						if (!companyTemp.equalsIgnoreCase(list.get(4).toString())) {
							companyChange = list.get(4).toString();
						}
					} else {
						shipGComp.setGS_CMPY_ID(listCompany.get(i));
					}
					if (mapImportTotal.containsKey(listCompany.get(i))) {
						mapImportTotal.put(listCompany.get(i), mapImportTotal.get(listCompany.get(i)) + Double.parseDouble(shipGComp.TOTAL));
					} else {
						mapImportTotal.put(listCompany.get(i), Double.parseDouble(shipGComp.TOTAL));
					}
					Hashtable calServicestable = new Hashtable();
					calServicestable.put("shipGComp", shipGComp);
					calServicesList.add(calServicestable);
				}

				Hashtable calServicestable = new Hashtable();
				
			}
			// End ShipG Service
			Double imports=0.0;
			for (Map.Entry<String, Double> entry : mapImportTotal.entrySet()) {
				if(entry.getValue()>imports){
					aform.setCompanyIdForServices(entry.getKey());
					imports = entry.getValue();
				}
			}

			if(tieneSetAsignado && (mapImportTotal.size()>1 || (!companyTemp.equalsIgnoreCase(companyChange)) && companyChange != null)) {
				servicesDetailArray = (ArrayList) session.getAttribute("servicesDetail");			
				servicesDetailArray = companyDefine(con, servicesDetailArray, aform, global, aform.getCompanyIdForServices()); //recalcula compa?ia en detalle de envio, en base a forma de pago//AAP23
				session.setAttribute("servicesDetail", servicesDetailArray);
				continuar = true;
			} else {
				continuar = false;
			}
			repeticiones++;
			if (repeticiones>4){
				req.setAttribute("zeroexist","Se tiene problemas con el calculo al momento de usar las plantillas");//JSA03
				return;
			}
			}
			while(continuar);

			servicesTotal.descuento = df.format(descuento);
			//double doubleServicios = 0.0;// Sum of ALL SubTotal//AAP17

			// RAD Service
			Services rad = new Services();
			String referenciaItem = "RAD";// AAP26
            //String idItem = "RAD-1";// AAP26
			String idItem = "";
			if (global.assignedBranch.contains("70")) {
				idItem = "RAD-ZP-1";
				referenciaItem = "RAD-ZP";
			}else {
				idItem = "RAD-1";// AAP26
			}
			String descriptionRad = getServicesAditionalDescription(con, idItem);
			rad.descriptionAditional = descriptionRad;
			//rad.GS_SRVC_ID = "RAD";
			rad.GS_SRVC_ID = referenciaItem;
			String lcZoneRad = getRadZone(con, aform);
			String subTotalRad = null;

			String[] amount_totalRad = null;// 0--- subTotal, 1---- Minamount
			String discountRad[] = new String[2];
			double taxableAmountRad = 0.0;
			double descuentoBaseRAD = 0.0;//AAP11
			double costoBaseRAD = 0.0;//AAP11
			if(aform.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
				discountRad[0] = null;
				discountRad[1] = "0";
				String typeCalculoTariff = ""; 
				Double subTotalRadSEG = 0.0, subTotalRadPISO = 0.0;
				for (int i = 0; i < servicesDetailArray.size(); i++) {				
					ShipmentServiceDetail ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
					if (ssd.getDatosCalculosTarifaSEG() != null) {
						typeCalculoTariff  = ssd.getDatosCalculosTarifaSEG().getTypeCalculoTariff();
						subTotalRadSEG += ssd.getDatosCalculosTarifaSEG().getvRAD();
					}
					if (ssd.getDatosCalculosTarifaSEGPISO() != null) {
						subTotalRadPISO += ssd.getDatosCalculosTarifaSEGPISO().getvRAD();
					}
				}
				subTotalRad = subTotalRadSEG.toString();
				if (isShipGExists || isShipEExists) {
					if (Double.parseDouble(subTotalRad) > 0) {
						if (typeCalculoTariff.equalsIgnoreCase("PISO")) {
							//calcula descuento de piso
							ShipTypeSEG ship = findServiceSEG(aform);
							discountRad = calculateQuantity(con, groupClientId, ship.getShipTypeSEGSrvcRefr(), String.valueOf(totalQty), df.format(Double.parseDouble(subTotalRad)), "0.0");
							descuentoBaseRAD = Double.parseDouble(discountRad[1]);
						} else {
							amount_totalRad = new String[2];
							amount_totalRad[0] = subTotalRadPISO.toString();
							costoBaseRAD = Double.parseDouble(amount_totalRad[0]);
							if (costoBaseRAD > 0) {
								descuentoBaseRAD = costoBaseRAD - Double.parseDouble(subTotalRad);
								/*
								 * Valida que el descuento sea mayor a cero para
								 * reflejar subtotal de costo base y descuento
								 * obtenido si no, continua con el importe neto de
								 * convenio
								 */
								if (descuentoBaseRAD > 0) {
									subTotalRad = df.format(costoBaseRAD);
									// Se multiplica por el total de bultos para
									// sacar el descuento real.
									discountRad[1] = df.format(descuentoBaseRAD);
								} else {
									// asignacion de valor 0 para evitar montos
									// negativos.
									descuentoBaseRAD = 0;
								}
							}
						}
					}
				}
				
				rad.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalRad));
				//doubleServicios = doubleServicios + (Double.parseDouble(subTotalRad) - descuentoBaseRAD);
				doubleServicios = doubleServicios + (Double.parseDouble(rad.GS_SUB_TOTL) - descuentoBaseRAD);
				taxableAmountRad = Double.parseDouble(rad.GS_SUB_TOTL)-descuentoBaseRAD;
				//System.out.println("subTotalRad "+subTotalRad);
				//System.out.println("descuentoBaseRAD "+descuentoBaseRAD);
				//System.out.println("taxableAmountRad "+taxableAmountRad);
			} else if (aform.getDefaultservicescreen().equalsIgnoreCase("yes")) {
				discountRad[0] = null;
				discountRad[1] = "0";
				/*calculo de rad para tarifas antiguas*/
				if (aform.getClasifTarif().equals("0")) {
//					if (aform.getDefaultservicescreenKm().equals("Y")) {
//						subTotalRad = getAditionalServicesTotalKm(con, global, "RAD-1", "RAD");
//					} else {
//						subTotalRad = getAditionalServicesTotal(con, global, "RAD-1", "RAD");	
//					}
//					
//					subTotalRad = df.format( Double.parseDouble(subTotalRad) * totalQty);//AAP21
					
					//AccessLog.Log(msgAvi+"calculateServices() aform.getTarifType() "+aform.getTarifType());
					if (aform.getTarifType().equals("C")) {
						if (aform.getDefaultservicescreenKm().equals("Y")) {//ADAP26
                            String additionalvalue = getAdditionalValueFactorKm(con, global, idItem, referenciaItem, "KG");
                            if (additionalvalue.equals("K")) {
                                double amount = getAditionalServicesTotalCtypeKm(con, global, idItem, referenciaItem, totalWeight, totalVolume);
                                subTotalRad = df.format(amount);
                            } else {
                                subTotalRad = getAditionalServicesTotalCtypeByLine(con, aform, global, servicesDetailArray, idItem, referenciaItem);//AAP26
                            }
                        } else {
                            String additionalvalue = getAdditionalValueFactor(con, global, idItem, referenciaItem, "KG");
                            if (additionalvalue.equals("K")) {
                                double amount = getAditionalServicesTotalCtype(con, global, idItem, referenciaItem, totalWeight, totalVolume);
                                subTotalRad = df.format(amount);
                            } else {
                                subTotalRad = getAditionalServicesTotalCtypeByLine(con, aform, global, servicesDetailArray, idItem, referenciaItem);//AAP26
                            }
                        }
						
						/*obtiene costo base solo cuando venga flete SHP-G*/
						//AccessLog.Log(msgAvi+"calculateServices() isShipGExists "+isShipGExists);
						if(isShipGExists) {//AAP11
							if (Double.parseDouble(subTotalRad) > 0) {//AAP11
								//AccessLog.Log(msgAvi+"calculateServices() subTotalRad "+subTotalRad);
								//amount_totalRad = getAditionalServicesTotal(con, global, lcZoneRad, df.format(totalCostobase), "RAD-1", "RAD");//AAP11
								//amount_totalRad = getAditionalServicesTotal(con, global, lcZoneRad, df.format(totalCostobase), idItem, "RAD");
								amount_totalRad = getAditionalServicesTotal(con, global, lcZoneRad, df.format(totalCostobase), idItem, referenciaItem);
								costoBaseRAD = Double.parseDouble(amount_totalRad[0]);//AAP11
								//AccessLog.Log(msgAvi+"calculateServices() costoBaseRAD "+costoBaseRAD);
								if (costoBaseRAD>0){//AAP11									
									descuentoBaseRAD = costoBaseRAD - Double.parseDouble(subTotalRad);//AAP11
									//AccessLog.Log(msgAvi+"calculateServices() descuentoBaseRAD "+descuentoBaseRAD);
									/*Valida que el descuento sea mayor a cero para reflejar subtotal de costo base y descuento obtenido
									 * si no, continua con el importe neto de convenio*/
									if (descuentoBaseRAD>0) {//AAP11
										subTotalRad = df.format(costoBaseRAD);//AAP11										
										discountRad[1] = df.format(descuentoBaseRAD);//AAP11
									} else {//AAP11
										//asignacion de valor 0 para evitar montos negativos.
										descuentoBaseRAD =0;//AAP11
									}
								}//AAP11
							} else {
								if (referenciaItem.equalsIgnoreCase("RAD-ZP")) {
									//Aqu? calcular el descuento del cliente
									amount_totalRad = getAditionalServicesTotal(con, global, lcZoneRad, df.format(totalCostobase), idItem, referenciaItem);
									costoBaseRAD = Double.parseDouble(amount_totalRad[1]);//AAP11
									costoBaseRAD = costoBaseRAD * totalQty;
									subTotalRad = String.valueOf(costoBaseRAD);
									descuentoBaseRAD = Double.parseDouble(calculateQuantity(con, groupClientId, referenciaItem, df.format(totalQty), df.format(costoBaseRAD), "0")[1]);
									discountRad[1] = df.format(descuentoBaseRAD);
									amount_totalRad[0] = df.format(costoBaseRAD);//AAP11
									amount_totalRad[1] = df.format(costoBaseRAD);//AAP11
								}
							}
						}//AAP11
						
						rad.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalRad));
						doubleServicios = doubleServicios + (Double.parseDouble(subTotalRad) - descuentoBaseRAD/*AAP11*/);
						taxableAmountRad = Double.parseDouble(rad.GS_SUB_TOTL)-descuentoBaseRAD/*AAP11*/;
					} else {//para clientes que queden con tarifa G //ADAP26
						if (aform.getDefaultservicescreenKm().equals("Y")) {
							//subTotalRad = getAditionalServicesTotalKm(con, global, "RAD-1", "RAD");
							//subTotalRad = getAditionalServicesTotalKm(con, global, idItem, "RAD");
							subTotalRad = getAditionalServicesTotalKm(con, global, idItem, referenciaItem);
						} else {
							//subTotalRad = getAditionalServicesTotal(con, global, "RAD-1", "RAD");
							//subTotalRad = getAditionalServicesTotal(con, global, idItem, "RAD");
							subTotalRad = getAditionalServicesTotal(con, global, idItem, referenciaItem);
						}
						
						subTotalRad = df.format( Double.parseDouble(subTotalRad) * totalQty);//AAP21
						
						rad.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalRad));
						doubleServicios = doubleServicios + (Double.parseDouble(subTotalRad) - descuentoBaseRAD/*AAP11*/);
						taxableAmountRad = Double.parseDouble(rad.GS_SUB_TOTL)-descuentoBaseRAD/*AAP11*/;	
					}
					
					
				} else {
					/*calculo de RAD para tarifa nueva*/
					if (aform.getDefaultservicescreenKm().equals("Y")) {
						//subTotalRad = getAditionalServicesTotalNewKm(con, aform, global, servicesDetailArray,"RAD-1");
						subTotalRad = getAditionalServicesTotalNewKm(con, aform, global, servicesDetailArray,idItem);
					} else {
						//subTotalRad = getAditionalServicesTotalNew(con, aform,servicesDetailArray,"RAD-1", global);
						subTotalRad = getAditionalServicesTotalNew(con, aform,servicesDetailArray,idItem, global);
					}					
					
					/*obtiene costo base solo cuando venga flete SHP-G*/
					if(isShipGExists) {//AAP11
						if (Double.parseDouble(subTotalRad) > 0) {//AAP11
							//amount_totalRad = getAditionalServicesTotal(con, global, lcZoneRad, df.format(totalCostobase), "RAD-1", "RAD");//AAP11
							//amount_totalRad = getAditionalServicesTotal(con, global, lcZoneRad, df.format(totalCostobase), idItem, "RAD");
							amount_totalRad = getAditionalServicesTotal(con, global, lcZoneRad, df.format(totalCostobase), idItem, referenciaItem);
							costoBaseRAD = Double.parseDouble(amount_totalRad[0]);//AAP11
							
							if (costoBaseRAD>0){//AAP11
								//costoBaseRAD = costoBaseRAD * totalQty;//AAP11
								descuentoBaseRAD = costoBaseRAD - Double.parseDouble(subTotalRad);//AAP11
								
								/*Valida que el descuento sea mayor a cero para reflejar subtotal de costo base y descuento obtenido
								 * si no, continua con el importe neto de convenio*/
								if (descuentoBaseRAD>0) {//AAP11
									subTotalRad = df.format(costoBaseRAD);//AAP11
									discountRad[1] = df.format(descuentoBaseRAD);//AAP11
								} else {//AAP11
									//asignacion de valor 0 para evitar montos negativos.
									descuentoBaseRAD =0;//AAP11
								}
							}//AAP11
						} else {
							if (referenciaItem.equalsIgnoreCase("RAD-ZP")) {
								//Aqu? calcular el descuento del cliente
								amount_totalRad = getAditionalServicesTotal(con, global, lcZoneRad, df.format(totalCostobase), idItem, referenciaItem);
								costoBaseRAD = Double.parseDouble(amount_totalRad[1]);//AAP11
								costoBaseRAD = costoBaseRAD * totalQty;
								subTotalRad = String.valueOf(costoBaseRAD);
								descuentoBaseRAD = Double.parseDouble(calculateQuantity(con, groupClientId, referenciaItem, df.format(totalQty), df.format(costoBaseRAD), "0")[1]);
								discountRad[1] = df.format(descuentoBaseRAD);
								amount_totalRad[0] = df.format(costoBaseRAD);//AAP11
								amount_totalRad[1] = df.format(costoBaseRAD);//AAP11
							}
						}
					}//AAP11
					
					rad.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalRad));//AAP11
					doubleServicios = doubleServicios + (Double.parseDouble(subTotalRad) - descuentoBaseRAD);//AAP11
					taxableAmountRad = Double.parseDouble(rad.GS_SUB_TOTL)-descuentoBaseRAD;//AAP11
				}
			} else {
				//amount_totalRad = getAditionalServicesTotal(con, global, lcZoneRad, df.format(totalImporte), "RAD-1", "RAD");
				//amount_totalRad = getAditionalServicesTotal(con, global, lcZoneRad, df.format(totalImporte), idItem, "RAD");
				amount_totalRad = getAditionalServicesTotal(con, global, lcZoneRad, df.format(totalImporte), idItem, referenciaItem);
				if (envelopeOnly) {//AAP10
					rad.setEnvelopeOnly(envelopeOnly);
					amount_totalRad[0] = "0";
					amount_totalRad[1] = "0";
				}
				
				rad.GS_SUB_TOTL = referenciaItem.equalsIgnoreCase("RAD-ZP") ? String.valueOf(Double.parseDouble(amount_totalRad[0]) * totalQty) : amount_totalRad[0];
				
				if (Double.parseDouble(rad.GS_SUB_TOTL) > 0) {
					//discountRad = calculateQuantity(con, groupClientId, "RAD", df.format(totalImporte), amount_totalRad[0], "0");
					discountRad = calculateQuantity(con, groupClientId, referenciaItem, df.format(totalImporte), amount_totalRad[0], "0");
					discountRad[1] = referenciaItem.equalsIgnoreCase("RAD-ZP") ? String.valueOf(Double.parseDouble(discountRad[1]) * totalQty) : discountRad[1];
				} else {
					discountRad[0] = null;
					discountRad[1] = "0.00";
				}
				
				doubleServicios = doubleServicios + (Double.parseDouble(amount_totalRad[0]) - Double.parseDouble(discountRad[1]));
				taxableAmountRad = Double.parseDouble(rad.GS_SUB_TOTL) - Double.parseDouble(discountRad[1]);
			}
			//String taxForRad[] = getTax( con, global, "RAD", df.format(taxableAmountRad), true, aform.getCompanyIdForServices(), aform.getFormaPago());
			String taxForRad[] = getTax( con, global, referenciaItem, df.format(taxableAmountRad), true, aform.getCompanyIdForServices(), aform.getFormaPago());
			if (taxForRad[2] != null) {
				rad.GS_CMPY_ID = taxForRad[2];
			}
			doubleIva = doubleIva + Double.parseDouble(taxForRad[0]);
			doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForRad[1]);//JASA01
			rad.GS_DISC = discountRad[1];
			rad.GS_TAX = taxForRad[0];
			rad.GS_TAX_RET = taxForRad[1];//JASA01
			rad.GS_ADD_PYMT_FLAG = "N";
			rad.GS_SRVC_TYPE = "N";
			rad.GS_DOCU_TYPE = "Q";
			rad.GS_GUIA_TYPE = "H";
			rad.GS_STUS_FLAG = "A";
			rad.GS_DISC_SLAB_NO = null;
			rad.TOTAL = df.format(Double.parseDouble(rad.GS_SUB_TOTL) - Double.parseDouble(rad.GS_DISC));
			rad.setMostrarDescuentos(mostrarDescuentos);			
			Hashtable calServicestableRad = new Hashtable();
			calServicestableRad.put("rad", rad);
			calServicesList.add(calServicestableRad);
			// End Rad Service

			// new service
			//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append("****defaultScreen ==> ").append(aform.getDefaultservicescreen()).toString());
			if (aform.getDefaultservicescreen().equalsIgnoreCase("yes")) {
				ArrayList newListService = new ArrayList();
				String Query = "";
				
				if (aform.getDefaultservicescreenKm().equalsIgnoreCase("Y")) {
					Query = conct.delete(0,conct.length()).append("SELECT WT_SRVC_ID SERVICE_ID, PACK_WEB.FUN_SRVC_NAME(WT_SRVC_ID) SERVICE_NAME, WT_TRIF_AMNT AMOUNT,")
							.append(" WT_REFR_SRVC_ID REF_SERVICE_ID FROM WEB_CLNT_SRVC_TRIF_KM WHERE WT_ORGN_CLNT_ID = ? AND ? between WT_DSTN_FROM_KM and WT_DSTN_TO_KM")
							.append(" AND WT_TRIF_SLAB = ? AND ")
							.append(" WT_REFR_SRVC_ID NOT IN('EAD','RAD','ACK','INV','ENVELOPES','PACKETS','COD','EXT','RAD-ZP') AND WT_TRIF_AMNT <> 0 AND wt_srvc_type IS NULL").toString();	
				} else {
					Query = conct.delete(0,conct.length()).append("SELECT WT_SRVC_ID SERVICE_ID, PACK_WEB.FUN_SRVC_NAME(WT_SRVC_ID) SERVICE_NAME, WT_TRIF_AMNT AMOUNT,")
							.append(" WT_REFR_SRVC_ID REF_SERVICE_ID FROM WEB_CLNT_SRVC_TRIF WHERE WT_ORGN_CLNT_ID = ? AND substr(WT_ORGN_BRNC_ID,1,3) = substr(?,1,3)")
							.append(" AND substr(WT_DEST_BRNC_ID,1,3) = substr(?,1,3) AND WT_TRIF_SLAB = ? AND ")
							.append(" WT_REFR_SRVC_ID NOT IN('EAD','RAD','ACK','INV','ENVELOPES','PACKETS','COD','EXT','RAD-ZP') AND WT_TRIF_AMNT <> 0 AND wt_srvc_type IS NULL").toString();	
				}

				
				String newservice_desc = "";
				String newservice_id = "";
				String newservice_ref_id = "";
				String subTotalnewService = null;
				//String[] amount_totalservice = null;
				String discountservice[] = new String[2];
				double taxableAmountservice = 0.0;

				PreparedStatement pst = con.prepareStatement(Query);
				
				if (aform.getDefaultservicescreenKm().equalsIgnoreCase("Y")) {
					//pst.setString(1, global.getClientId());
					pst.setString(1, global.getClientIdAgreement());
					pst.setString(2, global.getKmTarifType());
					pst.setString(3, global.getTarifType());
				} else {
					//pst.setString(1, global.getClientId());
					pst.setString(1, global.getClientIdAgreement());
					pst.setString(2, global.getAssignedBranch());
					pst.setString(3, global.getDestinationBranchId());
					pst.setString(4, global.getTarifType());
				}
				
				ResultSet rs = pst.executeQuery();
				while (rs.next()) {
					newservice_desc = "";
					newservice_id = "";
					newservice_ref_id = "";
					Services newservice = new Services();
					newservice_id = rs.getString("SERVICE_ID");
					newservice_ref_id = rs.getString("REF_SERVICE_ID");
					newservice_desc = rs.getString("SERVICE_NAME");
					newservice.descriptionAditional = newservice_desc;
					newservice.GS_SRVC_ID = newservice_ref_id;
					//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append(newservice_ref_id).append(" EXISTS").toString());
					subTotalnewService = null;
					//amount_totalservice = null;
					taxableAmountservice = 0.0;
					subTotalnewService = rs.getString("AMOUNT");
					discountservice[0] = null;
					discountservice[1] = "0";
					newservice.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalnewService) * totalQty);
					doubleServicios = doubleServicios + (Double.parseDouble(subTotalnewService) * totalQty - 0);
					taxableAmountservice = Double.parseDouble(newservice.GS_SUB_TOTL);
					//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append("****Taxable ").append(newservice_ref_id).append(" amount ").append(taxableAmountRad).toString());

					//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append("------- CALLING GET TAX IN  ").append(newservice_ref_id).toString());
					String taxForservice[] = getTax( con, global, newservice_ref_id, df.format(taxableAmountservice), true, aform.getCompanyIdForServices(), aform.getFormaPago());
					if (taxForservice[2] != null) {
						newservice.GS_CMPY_ID = taxForservice[2];
					}
					//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append("------- AFTER CALLING GET TAX IN  ").append(newservice_ref_id).toString());

					doubleIva = doubleIva + Double.parseDouble(taxForservice[0]);
					//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append("****Tax for taxForservice ").append(newservice_ref_id).append(doubleIva).toString());
					doubleIvaRet = doubleIvaRet +Double.parseDouble(taxForservice[1]);//JASA01
					//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append("****Tax Return for taxForservice ").append(newservice_ref_id).append(" doubleIvaRet ").append(doubleIvaRet).toString());
					newservice.GS_DISC = discountservice[1];
					newservice.GS_TAX = taxForservice[0];
					newservice.GS_TAX_RET = taxForservice[1];
					newservice.GS_ADD_PYMT_FLAG = "N";
					newservice.GS_SRVC_TYPE = "N";
					newservice.GS_DOCU_TYPE = "Q";
					newservice.GS_GUIA_TYPE = "H";
					newservice.GS_STUS_FLAG = "A";
					newservice.GS_DISC_SLAB_NO = null;
					newservice.TOTAL = df.format(Double.parseDouble(newservice.GS_SUB_TOTL) - Double.parseDouble(newservice.GS_DISC));
					Hashtable calServicestableService = new Hashtable();
					calServicestableService.put(newservice_id, newservice);
					newListService.add(calServicestableService);
				}
				if (rs!=null)
					rs.close();
				if (pst!=null)
					pst.close();
				
				if (newListService.size() > 0) {
					Hashtable calServicestableService = new Hashtable();
					calServicestableService.put("newservice", newListService);
					calServicesList.add(calServicestableService);
				}
			}

			// end of new service
			// EAD Service
			if (aform.getEntrega().equalsIgnoreCase("2") || aform.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
				referenciaItem = "";// AAP01
				idItem = "";// AAP01
				ArrayList AlcZoneEad = null;//AAP09
				String lcZoneEad = getEADZone(con, aform);
				String lcOpeLogEad = "";//AAP09
				aform.setOperadorLogistico(lcOpeLogEad);// asigna operador logistico a bean form de servicios
				
				if (aform.getZonaExtendida().substring(0, 1).equals("Y") && aform.getDestinationcode().contains("70")) {// AAP01
					AlcZoneEad = getLcZone(con, aform);// AAP09
					lcZoneEad = AlcZoneEad.get(0).toString();// zona//AAP09
					lcOpeLogEad = AlcZoneEad.get(1).toString();// operador logistico//AAP09
					aform.setOperadorLogistico(lcOpeLogEad);// asigna operador logistico a bean form de servicios
					referenciaItem = "EXT";
					idItem = "EXT-1";//AAP06
				} else {// AAP01
					referenciaItem = "EAD";
					idItem = "EAD-1";
				}// AAP01

				Services ead = new Services();
				String descriptionEad = getServicesAditionalDescription(con, idItem);// AAP01
				ead.descriptionAditional = descriptionEad;
				ead.GS_SRVC_ID = referenciaItem;// AAP01

				String subTotalEad = null;
				String[] amount_totalEad = null;// 0--- subTotal, 1---- Minamount
				String[] discountEad = new String[2];
				double taxableAmountEad = 0.0;
				double descuentoBaseEAD = 0.0;//AAP11
				double costoBaseEAD = 0.0;//AAP11
				if(aform.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
					discountEad[0] = null;
					discountEad[1] = "0";
					String typeCalculoTariff = ""; 
					Double subTotalEADSEG = 0.0, subTotalEADPISO = 0.0;
					for (int i = 0; i < servicesDetailArray.size(); i++) {				
						ShipmentServiceDetail ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
						if (ssd.getDatosCalculosTarifaSEG() != null) {
							typeCalculoTariff  = ssd.getDatosCalculosTarifaSEG().getTypeCalculoTariff();
							subTotalEADSEG += ssd.getDatosCalculosTarifaSEG().getvEAD();
						}
						if (ssd.getDatosCalculosTarifaSEGPISO() != null) {
							subTotalEADPISO += ssd.getDatosCalculosTarifaSEGPISO().getvEAD();
						}
					}
					subTotalEad = subTotalEADSEG.toString();
					if (isShipGExists || isShipEExists) {
						if (Double.parseDouble(subTotalEad) > 0) {
							if (typeCalculoTariff.equalsIgnoreCase("PISO")) {
								//calcula descuento de piso
								ShipTypeSEG ship = findServiceSEG(aform);
								discountEad = calculateQuantity(con, groupClientId, ship.getShipTypeSEGSrvcRefr(), String.valueOf(totalQty), df.format(Double.parseDouble(subTotalEad)), "0.0");
								descuentoBaseEAD = Double.parseDouble(discountEad[1]);
							} else {
								amount_totalEad = new String[2];
								amount_totalEad[0] = subTotalEADPISO.toString();
								costoBaseEAD = Double.parseDouble(amount_totalEad[0]);
								if (costoBaseEAD > 0) {
									descuentoBaseEAD = costoBaseEAD - Double.parseDouble(subTotalEad);
									/*
									 * Valida que el descuento sea mayor a cero para
									 * reflejar subtotal de costo base y descuento
									 * obtenido si no, continua con el importe neto de
									 * convenio
									 */
									if (descuentoBaseEAD > 0) {
										subTotalEad = df.format(costoBaseEAD);
										// Se multiplica por el total de bultos para
										// sacar el descuento real.
										//descuentoBaseEAD = descuentoBaseEAD * totalQty;
										discountEad[1] = df.format(descuentoBaseEAD);
									} else {
										// asignacion de valor 0 para evitar montos
										// negativos.
										descuentoBaseEAD = 0;
									}
								}
							}
						}
					}
					
					ead.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalEad));
					//doubleServicios = doubleServicios + (Double.parseDouble(subTotalEad) - descuentoBaseEAD);
					doubleServicios = doubleServicios + (Double.parseDouble(ead.GS_SUB_TOTL) - descuentoBaseEAD);
					taxableAmountEad = Double.parseDouble(ead.GS_SUB_TOTL)-descuentoBaseEAD;

				} else if ((aform.getDefaultservicescreen().equalsIgnoreCase("yes")) && servicesGlobal != null && servicesGlobal.ead != null && (aform.getZonaExtendida() != null &&
						(!aform.getZonaExtendida().substring(0, 1).equals("Y") || !aform.getDestinationcode().contains("70")))) {// AAP01
					//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append(" INSIDE EAD IF").toString());
					discountEad[0] = null;
					discountEad[1] = "0";
					/*calculo de ead para tarifas antiguas*/
					if (aform.getClasifTarif().equals("0")) {
						if (aform.getTarifType().equals("C")) {
							if (aform.getDefaultservicescreenKm().equals("Y")) {//ADAP26
                                String additionalvalue = getAdditionalValueFactorKm(con, global, idItem, referenciaItem, "KG");
                                if (additionalvalue.equals("K")) {
                                    double amount = getAditionalServicesTotalCtypeKm(con, global, idItem, referenciaItem, totalWeight, totalVolume);
                                    subTotalEad = df.format(amount);
                                } else {
                                    subTotalEad = getAditionalServicesTotalCtypeByLine(con, aform, global, servicesDetailArray, idItem, referenciaItem);//AAP26
                                }
                            } else {
                                String additionalvalue = getAdditionalValueFactor(con, global, idItem, referenciaItem, "KG");
                                if (additionalvalue.equals("K")) {
                                    double amount = getAditionalServicesTotalCtype(con, global, idItem, referenciaItem, totalWeight, totalVolume);
                                    subTotalEad = df.format(amount);
                                } else {
                                    subTotalEad = getAditionalServicesTotalCtypeByLine(con, aform, global, servicesDetailArray, idItem, referenciaItem);//AAP26
                                }
                            }
							/*obtiene costo base solo cuando venga flete SHP-G*/
							if(isShipGExists) {//AAP11
								if (Double.parseDouble(subTotalEad) > 0) {//AAP11
									amount_totalEad = getAditionalServicesTotal(con, global, lcZoneEad, df.format(totalCostobase), "EAD-1", "EAD");//AAP11							
									costoBaseEAD = Double.parseDouble(amount_totalEad[0]);//AAP11
									if (costoBaseEAD>0){//AAP11										
										descuentoBaseEAD = costoBaseEAD - Double.parseDouble(subTotalEad);//AAP11
										
										/*Valida que el descuento sea mayor a cero para reflejar subtotal de costo base y descuento obtenido
										 * si no, continua con el importe neto de convenio*/
										if (descuentoBaseEAD>0) {//AAP11
											subTotalEad = df.format(costoBaseEAD);//AAP11
											discountEad[1] = df.format(descuentoBaseEAD);//AAP11
										} else {//AAP11
											//asignacion de valor 0 para evitar montos negativos.
											descuentoBaseEAD = 0;//AAP11
										}
									}//AAP11
								}//AAP11
							}//AAP11
						}
						ead.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalEad));//AAP21
						doubleServicios = doubleServicios + (Double.parseDouble(subTotalEad) - descuentoBaseEAD /*AAP11*/);//AAP21
						taxableAmountEad = Double.parseDouble(ead.GS_SUB_TOTL)-descuentoBaseEAD/*AAP11*/;
					} else {
						/*calculo de ead para tarifa nueva*/
						if (aform.getDefaultservicescreenKm().equals("Y")) {
							subTotalEad = getAditionalServicesTotalNewKm(con, aform, global, servicesDetailArray,"EAD-1");
						} else {
							subTotalEad = getAditionalServicesTotalNew(con, aform,servicesDetailArray,"EAD-1", global);	
						}
								
						//ead.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalEad));
						//doubleServicios = doubleServicios + (Double.parseDouble(subTotalEad));
						//taxableAmountEad = Double.parseDouble(ead.GS_SUB_TOTL);
						
						/*obtiene costo base solo cuando venga flete SHP-G*/
						if(isShipGExists) {//AAP11
							if (Double.parseDouble(subTotalEad) > 0) {//AAP11
								amount_totalEad = getAditionalServicesTotal(con, global, lcZoneEad, df.format(totalCostobase), "EAD-1", "EAD");//AAP11							
								costoBaseEAD = Double.parseDouble(amount_totalEad[0]);//AAP11
								if (costoBaseEAD>0){//AAP11
									//costoBaseEAD = costoBaseEAD * totalQty;//AAP11
									descuentoBaseEAD = costoBaseEAD - Double.parseDouble(subTotalEad);//AAP11
									
									/*Valida que el descuento sea mayor a cero para reflejar subtotal de costo base y descuento obtenido
									 * si no, continua con el importe neto de convenio*/
									if (descuentoBaseEAD>0) {//AAP11
										subTotalEad = df.format(costoBaseEAD);//AAP11
										discountEad[1] = df.format(descuentoBaseEAD);//AAP11
									} else {//AAP11
										//asignacion de valor 0 para evitar montos negativos.
										descuentoBaseEAD = 0;//AAP11
									}								
								}//AAP11
							}//AAP11
						}//AAP11
						
						ead.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalEad));//AAP11
						doubleServicios = doubleServicios + (Double.parseDouble(subTotalEad) - descuentoBaseEAD);//AAP11
						taxableAmountEad = Double.parseDouble(ead.GS_SUB_TOTL)-descuentoBaseEAD;//AAP11			
					}
				} else {
					amount_totalEad = getAditionalServicesTotal(con, global, lcZoneEad, df.format(totalImporte), "EAD-1", "EAD");
					
					/*
					 * si es zona extendida, se multiplica por el numero de paquetes
					 * a entregar No se aplican descuentos.
					 */// AAP01
					if (aform.getZonaExtendida().substring(0, 1).equals("Y") && aform.getDestinationcode().contains("70")) {// AAP01
					/*
					 * verifica si el cliente origen tiene tarifa especial para zona
					 * extendida. si no tiene tarifa especial, se realiza el cobro
					 * normal
					 */
						String tarifaEspecialExt = "";
						if (aform.getDefaultservicescreenKm().equals("Y")) {
							tarifaEspecialExt = getTarifaEspecialExtKm(con, aform, global, referenciaItem, idItem);// AAP03
						} else {
							tarifaEspecialExt = getTarifaEspecialExt(con, aform, referenciaItem, idItem, global);// AAP03
						}
						
						double dTarifaEspecialExt = 0;
						try { dTarifaEspecialExt = Double.parseDouble(tarifaEspecialExt); } catch (Exception e) { dTarifaEspecialExt = 0; }
						
						/*nuevo calculo para zona extenida en tarifa A*/
						if (aform.getClasifTarif().equals("1")) {
							tarifaEspecialExt = getTarifaEspecialExtAll(con, aform, servicesDetailArray, referenciaItem, idItem, global, tarifaEspecialExt, amount_totalEad[1]);
						} else {//AAP28 se agrega logica para calcular EXT mediante tarifario costo base mas excedente, para tarifa c		
							if (dTarifaEspecialExt <= 0) {// si no se encontr? importe de costo fijo de convenio. se busca en configuracion costo base mas excedente para cliente C
								if (global.getTarifType().equals("C")) {//AAP28
		                            if (aform.getDefaultservicescreenKm().equals("Y")) {//AAP28
		                                String additionalvalue = getAdditionalValueFactorKm(con, global, idItem, referenciaItem, "KG");//AAP28
		                                if (additionalvalue.equals("K")) {//AAP28
		                                    double amount = getAditionalServicesTotalCtypeKm(con, global, idItem, referenciaItem, totalWeight, totalVolume);
		                                    tarifaEspecialExt = df.format(amount);
		                                } else {//AAP28
		                                	tarifaEspecialExt = getAditionalServicesTotalCtypeByLine(con, aform, global, servicesDetailArray, idItem, referenciaItem);//AAP28		                                	
		                                }//AAP28
		                            } else {//AAP28
		                                String additionalvalue = getAdditionalValueFactor(con, global, idItem, referenciaItem, "KG");//AAP28
		                                if (additionalvalue.equals("K")) {//AAP28
		                                    double amount = getAditionalServicesTotalCtype(con, global, idItem, referenciaItem, totalWeight, totalVolume);//AAP28
		                                    tarifaEspecialExt = df.format(amount);//AAP28
		                                } else {//AAP28
		                                	tarifaEspecialExt = getAditionalServicesTotalCtypeByLine(con, aform, global, servicesDetailArray, idItem, referenciaItem);//AAP28
		                                }//AAP28
		                            }//AAP28
								}//AAP28
							} else {//AAP28
								/*cuando estarifario C (clasifTarif=0), se multiplica por al cantidad de bultos, para ahora poder calcular descuentos (misma logica que tarifario A)*/
								try { tarifaEspecialExt = df.format((Double.parseDouble(tarifaEspecialExt) * totalQty)); } catch (Exception e) { tarifaEspecialExt = "0"; }
							}
						}
						
						//AccessLog.Log(msgAvi+conct.delete(0,conct.length()).append("calculateServices()_").append(global.clientId).append(" tarifaEspecialExt==>").append(tarifaEspecialExt).toString());
						discountEad[0] = null;// AAP01
						discountEad[1] = "0.00"; // AAP01
						
						try { dTarifaEspecialExt = Double.parseDouble(tarifaEspecialExt); } catch (Exception e) { dTarifaEspecialExt = 0; }						
						
						/*se calcula descuento tanto para clientes A como para clientes C(AAP28)*/
						if (dTarifaEspecialExt > 0) {
							costoBaseEAD = Double.parseDouble(amount_totalEad[1]);//AAP11
							costoBaseEAD = costoBaseEAD * totalQty;
							//AccessLog.Log(msgAvi+"calculateServices() EXT costoBaseEAD1 "+costoBaseEAD);
							if (costoBaseEAD>0){//AAP11
								descuentoBaseEAD = costoBaseEAD - Double.parseDouble(tarifaEspecialExt);//AAP11

								/*Valida que el descuento sea mayor a cero para reflejar subtotal de costo base y descuento obtenido
								 * si no, continua con el importe neto de convenio*/
								if (descuentoBaseEAD>=0) {//AAP11
									subTotalEad = df.format(costoBaseEAD);//AAP11
									discountEad[1] = df.format(descuentoBaseEAD);//AAP11 // Calculo completo contando descuento a aplicar. Solo para EXT tarifario especial
								} else {//AAP11
									//asignacion de valor 0 para evitar montos negativos.
									descuentoBaseEAD = 0;//AAP11
									subTotalEad = tarifaEspecialExt;
								}
							}//AAP11
							amount_totalEad[0] = subTotalEad;//AAP11
							amount_totalEad[1] = subTotalEad;//AAP11
						} else {
							//Aqu? calcular el descuento del cliente
							costoBaseEAD = Double.parseDouble(amount_totalEad[1]);//AAP11
							costoBaseEAD = costoBaseEAD * totalQty;
							descuentoBaseEAD = Double.parseDouble(calculateQuantity(con, groupClientId, "EXT", df.format(totalQty), df.format(costoBaseEAD), "0")[1]);
							discountEad[1] = df.format(descuentoBaseEAD);
							amount_totalEad[0] = df.format(costoBaseEAD);//AAP11
							amount_totalEad[1] = df.format(costoBaseEAD);//AAP11
						}

						ead.setGS_SUB_TOTL(df.format(Double.parseDouble(amount_totalEad[1])));
						doubleServicios = doubleServicios + ((Double.parseDouble(amount_totalEad[1])-descuentoBaseEAD));				
						taxableAmountEad = (Double.parseDouble(amount_totalEad[1])-descuentoBaseEAD);
						
					} else {// AAP01
						if (envelopeOnly) {//AAP10
							amount_totalEad[0] = "0";
							amount_totalEad[1] = "0";
						}
						ead.GS_SUB_TOTL = amount_totalEad[0];// AAP01
						if (Double.parseDouble(ead.GS_SUB_TOTL) > 0) {// AAP01
							/*en monto minimo ahora se envia 0, la comparacion de monto minimo contra total de cobro se hace en java.*///AAP04
							discountEad = calculateQuantity(con, groupClientId, "EAD", df.format(totalImporte), ead.GS_SUB_TOTL, "0"/*amount_totalEad[1]*/);// AAP01
						} else {// AAP01
							discountEad[0] = null;// AAP01
							discountEad[1] = "0.00"; // AAP01
						}// AAP01
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
					}// AAP01
				}
				String taxForEad[] = getTax( con, global, "EAD", df.format(taxableAmountEad), true, aform.getCompanyIdForServices(), aform.getFormaPago());
				if (taxForEad[2] != null) {
					ead.GS_CMPY_ID = taxForEad[2];
				}
				
				doubleIva = doubleIva + Double.parseDouble(taxForEad[0]);
				doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForEad[1]);//JASA01
				
				ead.GS_DISC = discountEad[1];
				ead.GS_TAX = taxForEad[0];
				ead.GS_TAX_RET = taxForEad[1];
				ead.GS_ADD_PYMT_FLAG = "N";
				ead.GS_SRVC_TYPE = "N";
				ead.GS_DOCU_TYPE = "Q";
				ead.GS_GUIA_TYPE = "H";
				ead.GS_STUS_FLAG = "A";
				ead.GS_DISC_SLAB_NO = null;
				ead.TOTAL = df.format(Double.parseDouble(ead.GS_SUB_TOTL) - Double.parseDouble(ead.GS_DISC));
				ead.setMostrarDescuentos(mostrarDescuentos);
				Hashtable calServicestableEad = new Hashtable();
				calServicestableEad.put("ead", ead);
				calServicesList.add(calServicestableEad);
			}
			// End EAD Service

			// Acknowledgement Service
			Services ack = new Services();
			String descriptionAck = getServicesAditionalDescription(con, aform.getAcusederecibo());
			ack.descriptionAditional = descriptionAck;
			ack.GS_SRVC_ID = "ACK";
			String subTotalAck = null;
			String[] amount_totalAck = null;// 0--- subTotal, 1---- Minamount
			String discountAck[] = new String[2];
			double taxableAmountAck = 0.0;
			double costoBaseACK = 0.0;
			double descuentoBaseACK = 0.0;
			
			ArrayList resultConfiguration = null;
			
			if (aform.getDefaultservicescreenKm().equals("Y")) {
				resultConfiguration = getServicesConfigurationKm(con, global, aform.getAcusederecibo(), "ACK");
			} else{
				resultConfiguration = getServicesConfiguration(con, global, aform.getAcusederecibo(), "ACK");
			}
				
			String calculo = "";
			
			if (!resultConfiguration.isEmpty()) {
				calculo = ((ArrayList)resultConfiguration.get(0)).get(0).toString();
			}
			if ((aform.getDefaultservicescreen().equalsIgnoreCase("yes")) && servicesGlobal != null && servicesGlobal.ack != null) {
				if (aform.getDefaultservicescreenKm().equals("Y")) {
					subTotalAck = getAditionalServicesTotalKm(con, global, aform.getAcusederecibo(), "ACK");	
				} else {
					subTotalAck = getAditionalServicesTotal(con, global, aform.getAcusederecibo(), "ACK");
				}
				
				discountAck[0] = null;
				discountAck[1] = "0";
				
				/*obtiene costo base para tarifa A o tarifa C*/
				if (subTotalAck!= null && Double.parseDouble(subTotalAck) > 0 && (aform.getClasifTarif().equals("1") || aform.getTarifType().equals("C"))) {//AAP11
					amount_totalAck = getAditionalServicesTotal(con, global, "", "0", aform.getAcusederecibo(), "ACK");//AAP11						
					costoBaseACK = Double.parseDouble(amount_totalAck[0]);//AAP11
					if (costoBaseACK>0){//AAP11
						
						descuentoBaseACK = costoBaseACK - Double.parseDouble(subTotalAck);//AAP11
						
						/*Valida que el descuento sea mayor a cero para reflejar subtotal de costo base y descuento obtenido
						 * si no, continua con el importe neto de convenio*/
						if (descuentoBaseACK>0) {//AAP11
							subTotalAck = df.format(costoBaseACK);//AAP11
							discountAck[1] = df.format(descuentoBaseACK);//AAP11
						} else {//AAP11
							//asignacion de valor 0 para evitar montos negativos.							
							descuentoBaseACK = 0;//AAP11
							
							costoBaseACK = Double.parseDouble(subTotalAck);//AAP11
						}						
						
					}//AAP11
				} else {//AAP11
					costoBaseACK = Double.parseDouble(subTotalAck);//AAP11
				}//AAP11
				
				if (calculo.equals("1")) {//AAP08
					ack.GS_SUB_TOTL = df.format(costoBaseACK * totalQty);//AAP011
					descuentoBaseACK = descuentoBaseACK * totalQty;//AAP011
					discountAck[1] = df.format(descuentoBaseACK);//AAP11
					costoBaseACK = costoBaseACK * totalQty;//AAP011
					subTotalAck = df.format(costoBaseACK);//AAP011
					doubleServicios = doubleServicios + (costoBaseACK - descuentoBaseACK);//AAP011					
				} else {
					ack.GS_SUB_TOTL = df.format(costoBaseACK);//AAP11
					doubleServicios = doubleServicios + ((costoBaseACK - descuentoBaseACK));//AAP11					
				}
				taxableAmountAck = costoBaseACK - descuentoBaseACK;//AAP11
			} else {
				amount_totalAck = getAditionalServicesTotal(con, global, "", "0", aform.getAcusederecibo(), "ACK");

				ack.GS_SUB_TOTL = amount_totalAck[0];
				if (Double.parseDouble(ack.GS_SUB_TOTL) > 0) {
					discountAck = calculateQuantity(con, groupClientId, "ACK", "0", amount_totalAck[0], "0");
				} else {
					discountAck[0] = null;
					discountAck[1] = "0.00";
				}
				doubleServicios = doubleServicios + (Double.parseDouble(amount_totalAck[0]) - Double.parseDouble(discountAck[1]));
				taxableAmountAck = Double.parseDouble(ack.GS_SUB_TOTL) - Double.parseDouble(discountAck[1]);
			}
			String taxForAck[] = getTax( con, global, "ACK", df.format(taxableAmountAck), true, aform.getCompanyIdForServices(), aform.getFormaPago());
			if (taxForAck[2] != null) {
				ack.GS_CMPY_ID = taxForAck[2];
			}
			doubleIva = doubleIva + Double.parseDouble(taxForAck[0]);
			doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForAck[1]);//JASA01
			ack.GS_DISC = discountAck[1];
			ack.GS_TAX = taxForAck[0];
			ack.GS_TAX_RET = taxForAck[1];
			ack.GS_ADD_PYMT_FLAG = "N";
			ack.GS_SRVC_TYPE = "N";
			ack.GS_DOCU_TYPE = "Q";
			ack.GS_GUIA_TYPE = "H";
			ack.GS_STUS_FLAG = "A";
			ack.GS_DISC_SLAB_NO = null;
			ack.TOTAL = df.format(Double.parseDouble(ack.GS_SUB_TOTL) - Double.parseDouble(ack.GS_DISC));
			ack.setMostrarDescuentos(mostrarDescuentos);
			Hashtable calServicestableAck = new Hashtable();
			calServicestableAck.put("ack", ack);
			calServicesList.add(calServicestableAck);
			
			// End Acknowledgement Service

			/*
			 * Added by palanivel for additional service
			 */
			
			/*
			 * AAP11
			 * Al momento de la modificacion para mostrar y calcular descuentos, ningun servicio adicional de webbooking esta registrado en ac_trif, 
			 * por lo que no existe un costo base, todos son calculados de manera manual. No se agrega a servicios adicionales el c?lculo de descuento
			 * */
			// Additional Service
			//if (global.tarifType.equalsIgnoreCase("C") && session.getAttribute("aditionalServicesDetail") != null) {
			if (aform.getShowAdditional().equals("Y") && session.getAttribute("aditionalServicesDetail") != null) {
				ArrayList additionalServicesArray = null;
				
				additionalServicesArray = (ArrayList) session.getAttribute("aditionalServicesDetail");
				int arraySize = additionalServicesArray.size();

				for (int i = 0; i < arraySize; i++) {

					AdditionalService serviceRecordsRecs = (AdditionalService) additionalServicesArray.get(i);
					Services add1 = new Services();
					String add1Ack = getServicesAditionalDescription(con, serviceRecordsRecs.service);
					add1.descriptionAditional = add1Ack;
					add1.GS_SRVC_ID = serviceRecordsRecs.serviceId;
					String subTotalAdd1 = null;
					//String[] amount_totalAdd1 = null;// 0--- subTotal, 1---- // Minamount
					String discountAdd1[] = new String[2];
					double taxableAmountAdd1 = 0.0;
					
					//subTotalAdd1 = getAditionalServicesTotal(con, global, serviceRecordsRecs.service, serviceRecordsRecs.serviceId);
					/*Obtiene importe de objeto*///AAP16
					subTotalAdd1 = serviceRecordsRecs.getImporte();//AAP16
					try {//AAP16
						/*intenta convertirlo a double para validar que exista un numero*/
						Double.parseDouble(subTotalAdd1);
					} catch (Exception e) {//AAP16
						/*por respaldo, obtiene el costo del servicio desde la configuracion en caso de algun fallo*/
						if (aform.getDefaultservicescreenKm().equals("Y")) {
							subTotalAdd1 = getAditionalServicesTotalKm(con, global, serviceRecordsRecs.service, serviceRecordsRecs.serviceId);
						} else {
							subTotalAdd1 = getAditionalServicesTotal(con, global, serviceRecordsRecs.service, serviceRecordsRecs.serviceId);	
						}
					}					
					
					discountAdd1[0] = null;
					discountAdd1[1] = "0";

					String additionalvalue = "";
					if (aform.getDefaultservicescreenKm().equals("Y")) {
						additionalvalue = getAdditionalValueKm(con, global, serviceRecordsRecs.service, serviceRecordsRecs.serviceId);
					} else {
						additionalvalue = getAdditionalValue(con, global, serviceRecordsRecs.service, serviceRecordsRecs.serviceId);
					}
					
					if (additionalvalue != null && additionalvalue.equalsIgnoreCase("G")) {
						add1.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalAdd1));
					} else if (additionalvalue != null && additionalvalue.equalsIgnoreCase("K")) {
						add1.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalAdd1) * totalWeight);
					} else {
						add1.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalAdd1) * totalQty);
					}
					
					// Modified By Sam[15-06-2006] , for rectifying wrong additional  service amt in services
					doubleServicios = doubleServicios + Double.parseDouble(add1.GS_SUB_TOTL);
					// doubleServicios=doubleServicios+(Double.parseDouble(subTotalAdd1)*totalQty-0);

					taxableAmountAdd1 = Double.parseDouble(add1.GS_SUB_TOTL);
					String taxForAdd1[] = getTax( con, global, serviceRecordsRecs.serviceId, df.format(taxableAmountAdd1), true, aform.getCompanyIdForServices(), aform.getFormaPago());
					if (taxForAdd1[2] != null) {
						add1.GS_CMPY_ID = taxForAdd1[2];
					}
					doubleIva = doubleIva + Double.parseDouble(taxForAdd1[0]);
					doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForAdd1[1]);//JASA01
					add1.GS_DISC = discountAdd1[1];
					add1.GS_TAX = taxForAdd1[0];
					add1.GS_TAX_RET = taxForAdd1[1];
					add1.GS_ADD_PYMT_FLAG = "N";
					add1.GS_SRVC_TYPE = "N";
					add1.GS_DOCU_TYPE = "Q";
					add1.GS_GUIA_TYPE = "H";
					add1.GS_STUS_FLAG = "A";
					add1.GS_DISC_SLAB_NO = null;
					add1.TOTAL = df.format(Double.parseDouble(add1.GS_SUB_TOTL) - Double.parseDouble(add1.GS_DISC));
					Hashtable calServicestableAdd1 = new Hashtable();
					calServicestableAdd1.put(serviceRecordsRecs.service, add1);
					calServicesList.add(calServicestableAdd1);
				}
			}
			// End Additional Service

			// COD Service
			if (aform.getValorcod() != null && aform.getValorcod().trim().length() > 0) {
				//System.out.println("entro a calcular COD");
				Services cod = new Services();
				String descriptionCod = getServicesAditionalDescription(con, "COD-1");
				cod.descriptionAditional = descriptionCod;
				cod.GS_SRVC_ID = "COD";
				String subTotalCod = null;
				String[] amount_totalCod = null;// 0--- subTotal, 1---- Minamount
				String[] discountCod = new String[2];
				double taxableAmountCod = 0.0;
				double costoBaseCOD = 0.0;
				double descuentoBaseCOD = 0.0;
				
				if ((aform.getDefaultservicescreen().equalsIgnoreCase("yes")) && servicesGlobal.cod != null) {
					
					if (aform.getDefaultservicescreenKm().equals("Y")) {
						subTotalCod = getAditionalServicesTotalKm(con, global, "COD-1", "COD");
					} else {
						subTotalCod = getAditionalServicesTotal(con, global, "COD-1", "COD");
					}
					discountCod[0] = null;
					discountCod[1] = "0";
					
					/*obtiene costo base para tarifa A o tarifa C*/			
					if (subTotalCod!= null && Double.parseDouble(subTotalCod) > 0 && (aform.getClasifTarif().equals("1") || aform.getTarifType().equals("C"))) {//AAP11
						amount_totalCod = getAditionalServicesTotal(con, global, "", "", "CODR-1", "CODR");//AAP11
						costoBaseCOD = Double.parseDouble(amount_totalCod[0]);//AAP11
						
						if (costoBaseCOD>0){//AAP11
							descuentoBaseCOD = costoBaseCOD - Double.parseDouble(subTotalCod);//AAP11
							
							/*Valida que el descuento sea mayor a cero para reflejar subtotal de costo base y descuento obtenido
							 * si no, continua con el importe neto de convenio*/
							if (descuentoBaseCOD>0) {//AAP11
								subTotalCod = df.format(costoBaseCOD);//AAP11
								discountCod[1] = df.format(descuentoBaseCOD);//AAP11
							} else {//AAP11
								//asignacion de valor 0 para evitar montos negativos.
								descuentoBaseCOD = 0;//AAP11
							}							
							
						}//AAP11
					} else {//AAP11						
						costoBaseCOD = Double.parseDouble(subTotalCod);//AAP11
					}//AAP11
					
					cod.GS_SUB_TOTL = df.format(costoBaseCOD * totalQty);//AAP11
					doubleServicios = doubleServicios + ((costoBaseCOD - descuentoBaseCOD) * totalQty);//AAP11
					taxableAmountCod = costoBaseCOD - descuentoBaseCOD;//AAP11
				} else {
					amount_totalCod = getAditionalServicesTotal(con, global, "", "", "CODR-1", "CODR");

					cod.GS_SUB_TOTL = amount_totalCod[0];
					if ((Double.parseDouble(cod.GS_SUB_TOTL)) > 0)
						discountCod = calculateQuantity(con, groupClientId, "CODR", "", cod.GS_SUB_TOTL, amount_totalCod[1]);
					else {
						discountCod[0] = null;
						discountCod[1] = "0.00";
					}

					doubleServicios = doubleServicios + (Double.parseDouble(amount_totalCod[0]) - Double.parseDouble(discountCod[1]));
					taxableAmountCod = Double.parseDouble(amount_totalCod[0]) - Double.parseDouble(discountCod[1]);					
				}
				String taxForCod[] = getTax( con, global, "COD", df.format(taxableAmountCod), true, aform.getCompanyIdForServices(), aform.getFormaPago());
				if (taxForCod[2] != null) {
					cod.GS_CMPY_ID = taxForCod[2];
				}
				doubleIva = doubleIva + Double.parseDouble(taxForCod[0]);
				doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForCod[1]);//JASA01
				cod.GS_DISC = discountCod[1];
				cod.GS_TAX = taxForCod[0];
				cod.GS_TAX_RET = taxForCod[1];
				cod.GS_ADD_PYMT_FLAG = "N";
				cod.GS_SRVC_TYPE = "N";
				cod.GS_DOCU_TYPE = "Q";
				cod.GS_GUIA_TYPE = "H";
				cod.GS_STUS_FLAG = "A";
				cod.GS_DISC_SLAB_NO = null;
				cod.TOTAL = df.format(Double.parseDouble(cod.GS_SUB_TOTL) - Double.parseDouble(cod.GS_DISC));
				cod.setMostrarDescuentos(mostrarDescuentos);
				Hashtable calServicestableCod = new Hashtable();
				calServicestableCod.put("cod", cod);
				calServicesList.add(calServicestableCod);

				// CODR Service
				Services codr = new Services();
				String descriptionCodr = getServicesAditionalDescription(con, "CODR-1");
				codr.descriptionAditional = descriptionCodr;
				//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append(" CODR EXISTS").toString());
				codr.GS_SRVC_ID = "CODR";
				codr.GS_SUB_TOTL = "0";
				codr.GS_DISC = "0";
				codr.GS_TAX = "0";
				codr.GS_TAX_RET = "0";
				codr.GS_ADD_PYMT_FLAG = "Y";
				codr.GS_SRVC_TYPE = "N";
				codr.GS_DOCU_TYPE = "Q";
				codr.GS_GUIA_TYPE = "H";
				codr.GS_STUS_FLAG = "A";
				codr.GS_DISC_SLAB_NO = null;
				codr.TOTAL = df.format(Double.parseDouble(codr.GS_SUB_TOTL) - Double.parseDouble(codr.GS_DISC));
				codr.setMostrarDescuentos(mostrarDescuentos);
				if (taxForCod[2] != null) {
					codr.GS_CMPY_ID = taxForCod[2];
				}
				Hashtable calServicestableCodr = new Hashtable();
				calServicestableCodr.put("codr", codr);
				calServicesList.add(calServicestableCodr);				
				global.commentText = getComment(con, aform.getValorcod().trim());
				session.setAttribute("sGlobal", global);
				// End CODR Service

			} else {
				
				/*AAP11
				 * Revisar porqu? todas las tarifas excepto la C calculan COD 
				 * si tiene importe de servicio configurado aunque no capture importe de monto a cobrar a cliente tercero
				 * DETECTADO EN EL CAMBIO PARA MOSTRAR LOS DESCUENTOS.*/
				if ((aform.getDefaultservicescreen().equalsIgnoreCase("yes")) && servicesGlobal != null && servicesGlobal.cod != null) {
					// Added Sam [12-06-2006] , if Valorcod is null calculation of cod is not needed in case of tariff type C
					if (!global.tarifType.equalsIgnoreCase("C")) {
						Services cod = new Services();
						String descriptionCod = getServicesAditionalDescription(con, "COD-1");
						cod.descriptionAditional = descriptionCod;
						cod.GS_SRVC_ID = "COD";
						String subTotalCod = null;
						// String[] amount_totalCod=null;//0--- subTotal, 1---- Minamount
						String[] discountCod = new String[2];
						double taxableAmountCod = 0.0;
						if (aform.getDefaultservicescreenKm().equals("Y")) {
							subTotalCod = getAditionalServicesTotalKm(con, global, "COD-1", "COD");
						} else {
							subTotalCod = getAditionalServicesTotal(con, global, "COD-1", "COD");	
						}
						
						discountCod[0] = null;
						discountCod[1] = "0";
						cod.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalCod) * totalQty);
						doubleServicios = doubleServicios + (Double.parseDouble(subTotalCod) * totalQty - 0);
						taxableAmountCod = Double.parseDouble(cod.GS_SUB_TOTL);
						//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append("------- CALLING GET TAX IN COD ELSE ").toString());
						String taxForCod[] = getTax( con, global, "COD", df.format(taxableAmountCod), true, aform.getCompanyIdForServices(), aform.getFormaPago());
						if (taxForCod[2] != null) {
							cod.GS_CMPY_ID = taxForCod[2];
						}
						//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append("------- AFTER CALLING GET TAX IN COD ELSE ").toString());

						doubleIva = doubleIva + Double.parseDouble(taxForCod[0]);
						doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForCod[1]);//JASA01
						//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append("COD EXISTS").toString());
						cod.GS_DISC = discountCod[1];
						cod.GS_TAX = taxForCod[0];
						cod.GS_TAX_RET = taxForCod[1];
						cod.GS_ADD_PYMT_FLAG = "N";
						cod.GS_SRVC_TYPE = "N";
						cod.GS_DOCU_TYPE = "Q";
						cod.GS_GUIA_TYPE = "H";
						cod.GS_STUS_FLAG = "A";
						cod.GS_DISC_SLAB_NO = null;
						cod.TOTAL = df.format(Double.parseDouble(cod.GS_SUB_TOTL) - Double.parseDouble(cod.GS_DISC));
						cod.setMostrarDescuentos(mostrarDescuentos);
						Hashtable calServicestableCod = new Hashtable();
						calServicestableCod.put("cod", cod);
						calServicesList.add(calServicestableCod);
					}
				}
			}

			// Insurance Service
			Services inv = new Services();
			String descriptionInv = getServicesAditionalDescription(con, aform.getCobertura());
			inv.descriptionAditional = descriptionInv;
			inv.GS_SRVC_ID = "INV";
			String subTotalInv = null;
			String[] amount_totalInv = null;// 0--- subTotal, 1---- Minamount
			String discountInv[] = new String[2];
			String taxForInv[] = new String[2];
			double taxableAmountInv = 0.0;
			//String insuranceView = "";//AAP12
			
			if(!aform.getValordeclarado().isEmpty() && Double.parseDouble(aform.getValordeclarado()) > 0) {
				if ((aform.getDefaultservicescreen().equalsIgnoreCase("yes")) && servicesGlobal.inv != null) {
					
					if (aform.getDefaultservicescreenKm().equals("Y")) {
						subTotalInv = getAditionalServicesTotalKm(con, global, aform.getCobertura(), "INV");
					} else {
						subTotalInv = getAditionalServicesTotal(con, global, aform.getCobertura(), "INV");
					}
					discountInv[0] = null;
					discountInv[1] = "0";
					inv.GS_SUB_TOTL = df.format(Double.parseDouble(subTotalInv) * totalQty);
					//insuranceView = getInsuranceViewFlag(con, global, req);//AAP12
					//if (insuranceView.equalsIgnoreCase("N")) {//AAP12
						doubleServicios = doubleServicios + (Double.parseDouble(subTotalInv) * totalQty - 0);
					//}//AAP12
					taxableAmountInv = Double.parseDouble(inv.GS_SUB_TOTL);
				} else {
					amount_totalInv = getAditionalServicesTotal(con, global, "", aform.getValordeclarado().trim(), aform.getCobertura(), "INV");
					inv.GS_SUB_TOTL = amount_totalInv[0];
					//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append(" BEFORE CALLING CALCULATEQUANTITY OF INV").toString());
					if (Double.parseDouble(inv.GS_SUB_TOTL) > 0) {
						//discountInv = calculateQuantity(con, groupClientId, "INV", amount_totalInv[0], aform.getValordeclarado().trim(), "0");//AAP_ERROR DESCUENTO SEGURO SEGURO
						discountInv = calculateQuantity(con, groupClientId, "INV", aform.getValordeclarado().trim(), amount_totalInv[0], "0");//AAP_ERROR DESCUENTO SEGURO SEGURO
					} else {
						discountInv[0] = null;
						discountInv[1] = "0.00";
					}
					// discountInv = calculateQuantity(con,groupClientId,aform.getCobertura(),amount_totalInv[0],aform.getValordeclarado().trim(),"0"); // commented and added prev line by amal
					//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append(" AFTER CALLING CALCULATEQUANTITY OF INV").toString());
					//insuranceView = getInsuranceViewFlag(con, global, req);//AAP12
					//if (insuranceView.equalsIgnoreCase("N")) {//AAP12
						doubleServicios = doubleServicios + (Double.parseDouble(amount_totalInv[0]) - Double.parseDouble(discountInv[1]));
					//}//AAP12
					taxableAmountInv = Double.parseDouble(inv.GS_SUB_TOTL) - Double.parseDouble(discountInv[1]);
				}
				//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append("------- CALLING GET TAX IN INV ").toString());
				taxForInv = getTax( con, global, "INV", df.format(taxableAmountInv), true, aform.getCompanyIdForServices(), aform.getFormaPago());
				if (taxForInv[2] != null) {
					inv.GS_CMPY_ID = taxForInv[2];
				}
				//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append("------- AFTER CALLING GET TAX IN INV ").toString());
				//insuranceView = getInsuranceViewFlag(con, global, req);//AAP12
				//if (insuranceView.equalsIgnoreCase("N")) {//AAP12
					doubleIva = doubleIva + Double.parseDouble(taxForInv[0]);
				//}//AAP12
				//insuranceView = getInsuranceViewFlag(con, global, req);//AAP12
				// if(insuranceView.equalsIgnoreCase("N"))
				doubleIvaRet = doubleIvaRet + Double.parseDouble(taxForInv[1]);//JASA
			}else {
				discountInv[0] = null;
				discountInv[1] = "0";
				inv.GS_SUB_TOTL = "0";
				taxableAmountInv = Double.parseDouble(inv.GS_SUB_TOTL);
				taxForInv = getTax( con, global, "INV", df.format(taxableAmountInv), true, aform.getCompanyIdForServices(), aform.getFormaPago());
			}
			
			//insuranceView = "";//AAP12
			//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append(" INV EXISTS").toString());
			inv.GS_DISC = discountInv[1];
			inv.GS_TAX = taxForInv[0];
			inv.GS_TAX_RET = taxForInv[1];
			inv.GS_ADD_PYMT_FLAG = "N";
			inv.GS_SRVC_TYPE = "N";
			inv.GS_DOCU_TYPE = "Q";
			inv.GS_GUIA_TYPE = "H";
			inv.GS_STUS_FLAG = "A";
			inv.GS_DISC_SLAB_NO = null;
			inv.TOTAL = df.format(Double.parseDouble(inv.GS_SUB_TOTL) - Double.parseDouble(inv.GS_DISC));
			Hashtable calServicestableInv = new Hashtable();
			calServicestableInv.put("inv", inv);
			calServicesList.add(calServicestableInv);
			// End Insurance Service
			for (int i = 0; i < calServicesList.size(); i++) {
				Hashtable servicesTable = (Hashtable) calServicesList.get(i);
				if(servicesTable.containsKey("shipE") || servicesTable.containsKey("shipGComp")) {
					Services services = (Services) (servicesTable.containsKey("shipE") ? servicesTable.get("shipE") : servicesTable.get("shipGComp"));
					String servicio=services.GS_SRVC_ID;
				}
			}

			session.setAttribute("calculatedservicelist", calServicesList);
			servicesTotal.servicios = df.format(doubleServicios);
			servicesTotal.subTotal = df.format((Double.parseDouble(servicesTotal.flete) + Double.parseDouble(servicesTotal.servicios)) - Double.parseDouble(servicesTotal.descuento));
			
			servicesTotal.iva = df.format(doubleIva);
			servicesTotal.ivaRet = df.format(doubleIvaRet);
			servicesTotal.total = df.format((Double.parseDouble(servicesTotal.subTotal) + doubleIva) - doubleIvaRet);
			
			if (mostrarDescuentos.equals("N")) {//AAP11
				//si se calculo servicio ENTREGA LOCAL se resta el subtotal del servicio de totalImporte para no mostrarlo en pantalla como flete)  
				servicesTotal.setFlete( df.format(totalImporte) );//AAP11
				servicesTotal.setDescuento("0.00");//AAP11
			}//AAP11

			session.setAttribute("servicestotal", servicesTotal);					
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("calculateServices()_Error:").append(e).toString());
			e.printStackTrace();
		}
	}

	// Returns discountSlab,discountAmount
	private String[] calculateQuantity(Connection con, String grpclientId,
			String serviceId, String qty, String amount, String minAmount) {
		CallableStatement cst = null;
		String a[] = new String[2];
		try {
			DecimalFormat df = new DecimalFormat("0.00");
			String query = "Begin ? := pack_web.fun_calc_disc(?,?,?,?,?,?); End; ";
			cst = con.prepareCall(query);
			cst.registerOutParameter(1, Types.NUMERIC);
			cst.setString(2, serviceId);
			cst.setString(3, grpclientId);

			if (amount != null && amount.length() == 0) {
				cst.setNull(4, Types.NUMERIC);
			} else {
				cst.setDouble(4, Double.parseDouble(amount));
			}
			if (qty != null && qty.length() == 0) {				
				cst.setDouble(5, Types.NUMERIC);
			} else {
				cst.setDouble(5, Double.parseDouble(qty));
			}
			if (minAmount != null && minAmount.length() == 0) {
				cst.setNull(6, Types.NUMERIC);
			} else {
				cst.setDouble(6, Double.parseDouble(minAmount));
			}
			cst.registerOutParameter(7, Types.VARCHAR);

			cst.executeQuery();
			
			a[0] = cst.getString(7);
			a[1] = df.format(cst.getDouble(1));

		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("calculateQuantity()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return a;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList getLcZone(Connection con, JavWebBookingGeneralMainForm aform) {		
		CallableStatement cst = null;
		ArrayList datos = new ArrayList(2);
		try {			
			String query = "{ call pack_web.pro_brnc_coly_ze(?,?,?,?,?,?) }";
			cst = con.prepareCall(query);
			cst.setString(1, aform.getCitycode());
			cst.setString(2, aform.getDestinationcoloniacode());
			cst.setString(3, aform.getDestinationcode());
			cst.registerOutParameter(4, Types.VARCHAR);
			cst.registerOutParameter(5, Types.VARCHAR);
			cst.registerOutParameter(6, Types.VARCHAR);
			cst.executeQuery();

			if (cst.getString(4) == null) {
				datos.add("");
			} else {
				datos.add(cst.getString(4));// zona
			}

			if (cst.getString(6) == null) {
				datos.add("");
			} else {
				datos.add(cst.getString(6));// operador logistico
			}
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getLcZone()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return datos;
	}

	// code added on 29/03/2004
	private String getRadZone(Connection con, JavWebBookingGeneralMainForm aform) {
		String lcZone = "";
		CallableStatement cst = null;
		try {
			//String query = "{ call pack_web.pro_brnc_coly(?,?,?,?,?) }";//AAP09
			String query = "{ call pack_web.pro_site_coly(?,?,?,?,?) }";//AAP09
			//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getRadZone()_orginCityCode==>").append(aform.getOrginCityCode()).append(" generalForm.originColinaCode==>").append(aform.getOriginColinaCode()).append(" orgioncode==>").append(aform.getOrgioncode()).toString());
			cst = con.prepareCall(query);
			cst.setString(1, aform.getOrginCityCode());
			cst.setString(2, aform.getOriginColinaCode());
			//cst.setString(3, aform.getOrgioncode());//AAP09
			cst.setString(3, aform.getOrgioncode().substring(0,3));//AAP09
			cst.registerOutParameter(4, Types.VARCHAR);
			cst.registerOutParameter(5, Types.VARCHAR);
			cst.executeQuery();
			lcZone = cst.getString(4);

		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getRadZone()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return lcZone;
	}
	// code added on 12/09/2014 //AAP09
	private String getEADZone(Connection con, JavWebBookingGeneralMainForm aform) {
		String lcZone = "";
		CallableStatement cst = null;
		try {
			//String query = "{ call pack_web.pro_brnc_coly(?,?,?,?,?) }";
			String query = "{ call pack_web.pro_site_coly(?,?,?,?,?) }";
			//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getRadZone()_orginCityCode==>").append(aform.getOrginCityCode()).append(" generalForm.originColinaCode==>").append(aform.getOriginColinaCode()).append(" orgioncode==>").append(aform.getOrgioncode()).toString());
			cst = con.prepareCall(query);
			
			cst.setString(1, aform.getCitycode());
			cst.setString(2, aform.getDestinationcoloniacode());
			cst.setString(3, aform.getDestinationcode().substring(0,3));
		
			cst.registerOutParameter(4, Types.VARCHAR);
			cst.registerOutParameter(5, Types.VARCHAR);
			cst.executeQuery();
			lcZone = cst.getString(4);

		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getEADZone()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return lcZone;
	}
	// Total For AditionalServices With Non-defalut screen
	private String[] getAditionalServicesTotal(Connection con, Global global, String lcZone, String totalAmount, String serviceId, String referenceServiceId) {
		
		CallableStatement cst = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String[] amount_total = new String[2];
		try {
			DecimalFormat df = new DecimalFormat("0.00");
			boolean defaServcItemExist = true;
			// Added on 03/06/2009 Purpose: To Check the "RAD" service for client in the SYS_CLNT_SRVC.
			if (referenceServiceId.equalsIgnoreCase("RAD")) {// || referenceServiceId.equalsIgnoreCase("RAD-ZP")) {
				defaServcItemExist = false;
				pst = con.prepareStatement("select CS_DEFA_SRVC_ITEM from SYS_CLNT_SRVC where CS_CLNT_ID =? and CS_SRVC_ID =?");
				pst.setString(1, global.clientId);
				pst.setString(2, referenceServiceId);
				rs = pst.executeQuery();
				if (rs.next()) {
					defaServcItemExist = true;
				}				
			}
			
			// ADDED on 16/011/2009 Purpose: To calcualate the "RAD" service of the client from PACK_WEB.FUN_FTCH_RAD_SRVC FUNCTION.
			if (referenceServiceId.equalsIgnoreCase("RAD")){// || referenceServiceId.equalsIgnoreCase("RAD-ZP")) {
				/*if (serviceId.equalsIgnoreCase("RAD-ZP-1")) {
					serviceId = "RAD-1";
					referenceServiceId = "RAD";
				}*/
				if (defaServcItemExist) {
					//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getAditionalServicesTotal()16-11-2009==INSIDE RAD SERVICE").toString());
					String query = "Begin ? := pack_web.FUN_FTCH_ADD_RAD_SRVC(?,?,?,?,?,?,?); End;";
					cst = con.prepareCall(query);
					cst.registerOutParameter(1, Types.NUMERIC);
					cst.setString(2, serviceId);
					cst.setString(3, referenceServiceId);
					cst.setString(4, global.assignedBranch);
					cst.setString(5, global.destinationBranchId);

					if (totalAmount != null && totalAmount.length() == 0) {
						cst.setNull(6, Types.NUMERIC);
					} else {						
						cst.setDouble(6, Double.parseDouble(totalAmount));
					}
					
					cst.setString(7, "NON");
					cst.registerOutParameter(8, Types.NUMERIC);

					cst.executeQuery();

					amount_total[0] = df.format(cst.getDouble(1));
					amount_total[1] = df.format(cst.getDouble(8));
				} else {
					amount_total[0] = df.format(0);
					amount_total[1] = df.format(0);
				}
			} else {
				if ("RAD-ZP-1".equalsIgnoreCase(serviceId)) {
					serviceId = "RAD-1";
					referenceServiceId = "RAD";
				}
				String query = "Begin ? := pack_web.FUN_FTCH_ADD_SRVC(?,?,?,?,?,?,?,?); End;";
				cst = con.prepareCall(query);
				cst.registerOutParameter(1, Types.NUMERIC);
				cst.setString(2, serviceId);
				cst.setString(3, referenceServiceId);
				cst.setString(4, global.assignedBranch);
				cst.setString(5, global.destinationBranchId);
				cst.setString(6, lcZone);

				if (totalAmount == null || (totalAmount != null && totalAmount.isEmpty())) {
					cst.setNull(7, Types.NUMERIC);
				} else {
					cst.setDouble(7, Double.parseDouble(totalAmount));
				}
				cst.setString(8, "NON");
				cst.registerOutParameter(9, Types.NUMERIC);

				cst.executeQuery();

				amount_total[0] = df.format(cst.getDouble(1));
				amount_total[1] = df.format(cst.getDouble(9));				
			}		
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getAditionalServicesTotal()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst, cst);
		}
		return amount_total;
	}

	// Total For AditionalServices With Defalut screen
	private String getAditionalServicesTotal(Connection con, Global global, String serviceId, String referenceServiceId) {
		double amount = 0.0;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = "select pack_web.FUN_FTCH_TRIF_AMNT(?,?,?,?,?,?) from dual";
			pst = con.prepareStatement(query);
			//pst.setString(1, global.clientId);
			pst.setString(1, global.getClientIdAgreement());
			pst.setString(2, global.getAssignedBranch());
			pst.setString(3, global.getDestinationBranchId());
			pst.setString(4, global.getTarifType());
			pst.setString(5, referenceServiceId);
			pst.setString(6, serviceId);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				amount = rs.getDouble(1);
			}					
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getAditionalServicesTotal()2_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return new java.text.DecimalFormat("0.00").format(amount);
	}

	// Total For AditionalServices With Defalut screen
		private String getAditionalServicesTotalKm(Connection con, Global global, String serviceId, String referenceServiceId) {
			double amount = 0.0;
			PreparedStatement pst = null;
			ResultSet rs = null;
			try {
				String query = "select pack_web.FUN_FTCH_TRIF_AMNT_KM(?,?,?,?,?,?) from dual";
				pst = con.prepareStatement(query);
				//pst.setString(1, global.getClientId());
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, global.getAssignedBranch());
				pst.setString(3, global.getKmTarifType());
				pst.setString(4, global.getTarifType());
				pst.setString(5, referenceServiceId);
				pst.setString(6, serviceId);
				rs = pst.executeQuery();
				
				if (rs.next()) {
					amount = rs.getDouble(1);
				}					
			} catch (Exception e) {
				AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getAditionalServicesTotalKm()2_Error:").append(e).toString());
				e.printStackTrace();
			} finally {
				resources.closeResources(rs, pst);
			}
			return new java.text.DecimalFormat("0.00").format(amount);
		}
		
	    @SuppressWarnings("rawtypes")
		public String getAditionalServicesTotalCtypeByLine(Connection con, JavWebBookingGeneralMainForm aform, Global global, ArrayList servicesDetailArray, String serviceId, String referenceServiceId) {//AAP26
			double total_amount = 0;//monto total de los renglones capturados
			double amount = 0;//monto de cobro tarifa
			int cantidad = 0;//cantidad de bultos
			double peso = 0;
			double volumen = 0;
			try {
		                ShipmentServiceDetail ssd = null;
		                for (int i = 0; i < servicesDetailArray.size(); i++) {
		                    ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);	                    
		                    
		                    /* se convierten los datos de la forma a valores enteros y dobles*/
		                    try {cantidad = Integer.parseInt(ssd.getCantidad().trim());	} catch (Exception e) { cantidad = 0;}
		                    try {peso = Double.parseDouble(ssd.getPeso()); 				} catch (Exception e) { peso = 0;}
		                    try {volumen = Double.parseDouble(ssd.getVolumen());		} catch (Exception e) { volumen = 0;}
		                    if (aform.getDefaultservicescreenKm().equals("Y")) {//ADAP26
		                        amount = getAditionalServicesTotalCtypeKm(con, global, serviceId, referenceServiceId, peso, volumen);
		                    } else {
		                        amount = getAditionalServicesTotalCtype(con, global, serviceId, referenceServiceId, peso, volumen);
		                    }                     
		                    amount = amount * cantidad;
		
		                    /*suma al importe total, el cobro obtenido por la cantidad de bultos del renglon actual*/ 
		                    total_amount = total_amount + amount;
		                    amount = 0;
		                }
			} catch (Exception e) {
		                AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getAditionalServicesTotalCtypeByLine()_Error:").append(e).toString());
		                e.printStackTrace();
			}
			return new java.text.DecimalFormat("0.00").format(total_amount);
		}
	    

	    private double getAditionalServicesTotalCtypeKm(Connection con, Global global, String serviceId, String referenceServiceId, double totalWeight, double totalVolume) {//ADAP26
	        double amount = 0.0;
	        PreparedStatement pst = null;
	        ResultSet rs = null;
	        try {
	            String query = "select pack_web.fun_ftch_trif_amnt_C_KM(?,?,?,?,?,?,?,?) from dual";
	            pst = con.prepareStatement(query);
	            pst.setString(1, global.getClientIdAgreement());
	            pst.setString(2, global.getAssignedBranch());
	            pst.setString(3, global.getKmTarifType());
	            pst.setString(4, global.getTarifType());
	            pst.setString(5, referenceServiceId);
	            pst.setString(6, serviceId);
	            pst.setDouble(7, totalWeight);
	            pst.setDouble(8, totalVolume);
	            rs = pst.executeQuery();

	            if (rs.next()) {
	                amount = rs.getDouble(1);
	            }					
	        } catch (Exception e) {
	            AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getAditionalServicesTotalCtypeKm()2_Error:").append(e).toString());
	            e.printStackTrace();
	        } finally {
	            resources.closeResources(rs, pst);
	        }
	        return amount;
	    }
	    
	    private double getAditionalServicesTotalCtype(Connection con, Global global, String serviceId, String referenceServiceId, double totalWeight, double totalVolume) {//ADAP26
	        double amount = 0.0;
	        PreparedStatement pst = null;
	        ResultSet rs = null;
	        try {
	            String query = "select pack_web.fun_ftch_trif_amnt_C(?,?,?,?,?,?,?,?) from dual";
	            
	            pst = con.prepareStatement(query);
	            pst.setString(1, global.getClientIdAgreement());
	            pst.setString(2, global.getAssignedBranch());
	            pst.setString(3, global.getDestinationBranchId());
	            pst.setString(4, global.getTarifType());
	            pst.setString(5, referenceServiceId);
	            pst.setString(6, serviceId);
	            pst.setDouble(7, totalWeight);
	            pst.setDouble(8, totalVolume);
	            
	            rs = pst.executeQuery();

	            if (rs.next()) {
	                amount = rs.getDouble(1);
	            }					
	        } catch (Exception e) {
	            AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getAditionalServicesTotalCtype()2_Error:").append(e).toString());
	            e.printStackTrace();
	        } finally {
	            resources.closeResources(rs, pst);
	        }
	        return amount;
	}
	    
	/****************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa especial de tipo de tarifa nueva		*
	 * **************************************************************************************/
	@SuppressWarnings("rawtypes")
	public String getAditionalServicesTotalNew(Connection con, JavWebBookingGeneralMainForm aform, ArrayList servicesDetailArray, String serviceId, Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		double total_amount = 0;//monto total de los renglones capturados
		double amount = 0;//monto de cobro tarifa normal o T7V (segun condicion)
		double amountp = 0;//monto de cobro tarifa T7P
		double totalv = 0;//total en volumen
		double totalp = 0;//total en peso
		int cantidad = 0;//cantidad de bultos
		double peso = 0;
		double volumen = 0;
		try {
			ShipmentServiceDetail ssd = null;
			for (int i = 0; i < servicesDetailArray.size(); i++) {
				ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
				
				//String query = "select pack_web.FUN_FTCH_TRIF_AMNT_NEW(?,?,?,?) from dual";
				String query = "select pack_web.FUN_FTCH_TRIF_AMNT_NEW_BRNC(?,?,?,?,?) from dual";
				
				pst = con.prepareStatement(query);

				//pst.setString(1, aform.getOrgienclave());
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, aform.getDestinationcode());
				pst.setString(3, serviceId);
				
				pst.setString(5, aform.getOrginbranchcode());
				
				try {cantidad = Integer.parseInt(ssd.cantidad);	} catch (Exception e) { cantidad = 0;}
				
				if (ssd.tarifa.equals("T7")) {
					/*obtiene tarifa por peso*/
					pst.setString(4, "T7P");
					
					rs = pst.executeQuery();
					
					if (rs.next()) {
						amountp = rs.getDouble(1);
					}
					
					if (rs!=null)
						rs.close();
					
					/*obtiene tarifa por volumen*/
					pst.setString(4, "T7V");
					
					rs = pst.executeQuery();
					
					if (rs.next()) {
						amount = rs.getDouble(1);
					}
					
					/* se convierten los datos de la forma a valores enteros y dobles*/					
					try {peso = Double.parseDouble(ssd.peso); 		} catch (Exception e) { peso = 0;}
					try {volumen = Double.parseDouble(ssd.volumen);	} catch (Exception e) { volumen = 0;}
					
					/*se realiza calculo de precio en base a formulas*/
					totalp = cantidad * (amountp * peso);
					totalv = cantidad * (amount * volumen);

					if (totalp > totalv) {						
						amount = totalp;						
					} else {
						amount = totalv;
					}
				} else {
					pst.setString(4, ssd.tarifa);
					rs = pst.executeQuery();
					
					if (rs.next()) {
						amount = rs.getDouble(1);						
					}
					amount = amount * cantidad;
				}
				
				/*suma al importe total, el cobro obtenido por la cantidad de bultos del renglon actual*/ 
				total_amount = total_amount + amount;
				amount = 0;
			}
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getAditionalServicesTotalNew()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return new java.text.DecimalFormat("0.00").format(total_amount);
	}

	/****************************************************************************************
	 * metodo que ejecuta funcion que retorna tarifa especial de tipo de tarifa nueva		*
	 * **************************************************************************************/
	@SuppressWarnings("rawtypes")
	public String getAditionalServicesTotalNewKm(Connection con, JavWebBookingGeneralMainForm aform, Global global, ArrayList servicesDetailArray, String serviceId) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		double total_amount = 0;//monto total de los renglones capturados
		double amount = 0;//monto de cobro tarifa normal o T7V (segun condicion)
		double amountp = 0;//monto de cobro tarifa T7P
		double totalv = 0;//total en volumen
		double totalp = 0;//total en peso
		int cantidad = 0;//cantidad de bultos
		double peso = 0;
		double volumen = 0;
		try {
			ShipmentServiceDetail ssd = null;
			for (int i = 0; i < servicesDetailArray.size(); i++) {
				ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
				
				String query = "select pack_web.FUN_FTCH_TRIF_AMNT_NEW_KM(?,?,?,?) from dual";
				
				pst = con.prepareStatement(query);

				//pst.setString(1, aform.getOrgienclave());
				pst.setString(1, global.getClientIdAgreement());
				pst.setString(2, global.getKmTarifType());	
				pst.setString(3, serviceId);
				
				try {cantidad = Integer.parseInt(ssd.cantidad);	} catch (Exception e) { cantidad = 0;}
				
				if (ssd.tarifa.equals("T7")) {
					/*obtiene tarifa por peso*/
					pst.setString(4, "T7P");
					
					rs = pst.executeQuery();
					
					if (rs.next()) {
						amountp = rs.getDouble(1);
					}
					
					if (rs!=null)
						rs.close();
					
					/*obtiene tarifa por volumen*/
					pst.setString(4, "T7V");
					
					rs = pst.executeQuery();
					
					if (rs.next()) {
						amount = rs.getDouble(1);
					}
					
					/* se convierten los datos de la forma a valores enteros y dobles*/					
					try {peso = Double.parseDouble(ssd.peso); 		} catch (Exception e) { peso = 0;}
					try {volumen = Double.parseDouble(ssd.volumen);	} catch (Exception e) { volumen = 0;}
					
					/*se realiza calculo de precio en base a formulas*/
					totalp = cantidad * (amountp * peso);
					totalv = cantidad * (amount * volumen);

					if (totalp > totalv) {						
						amount = totalp;						
					} else {
						amount = totalv;
					}
				} else {
					pst.setString(4, ssd.tarifa);
					rs = pst.executeQuery();
					
					if (rs.next()) {
						amount = rs.getDouble(1);						
					}
					amount = amount * cantidad;
				}
				
				/*suma al importe total, el cobro obtenido por la cantidad de bultos del renglon actual*/ 
				total_amount = total_amount + amount;
				amount = 0;
			}
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getAditionalServicesTotalNewKm()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return new java.text.DecimalFormat("0.00").format(total_amount);
	}
	
	private String[] getTax(Connection con, Global global, String referenceServiceId, String taxableAmount, boolean noRetiene, String companyID, String formaPago) {
		CallableStatement cst = null;
		String tax[] = new String[3];
		try {
			String taxQuery = "{call pack_sipweb.PRO_CALC_TAX_AMT_SET_CUS(?,?,?,?,?,?,?,?,?)}";
			
			String orgionBorderBranch = global.getOrigionBorderBranch();//AAP20
			String destinationBorderBranch = global.getDestinationBorderBranch();//AAP20
			cst = con.prepareCall(taxQuery);
			
			if (orgionBorderBranch.equals("BR") && !destinationBorderBranch.equals("BR")) {//AAP20
				cst.setString(1, global.destinationBranchId);
			} else {
				cst.setString(1, global.assignedBranch);
			}

			cst.setString(2, referenceServiceId);
			cst.setDouble(3, Double.parseDouble(taxableAmount));
			if (formaPago.equals("TO_PAY") ) {
				cst.setString(4, global.getGroupClientIdDestino());
			} else {
				cst.setString(4, global.getGroupClientId());
			}
			cst.setString(5, "BOOKING");
			cst.setString(6, companyID);
			cst.registerOutParameter(7, Types.NUMERIC);
			cst.registerOutParameter(8, Types.NUMERIC);
			cst.registerOutParameter(9, Types.VARCHAR);
			cst.executeQuery();

			DecimalFormat df = new DecimalFormat("0.00");
			
			tax[0] = df.format(cst.getDouble(7));
			tax[1] = df.format(cst.getDouble(8));
			tax[2] = cst.getString(9);
			//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getTax()_").append(global.clientId).append("tax of 0 ").append(tax[0]).toString());
			resources.cerrarCallableStatement(cst);
			cst = null;
			if (tax[2] == null || (tax[2] != null && tax[2].isEmpty())) {
				if (referenceServiceId.equalsIgnoreCase("PACKETS")) {
					if (noRetiene) {
						tax[1] = "0";
					} else if (!getRetIvaByCompany(con, companyID)) {
						tax[1] = "0";
					}
				} else if (noRetiene) {
					tax[1] = "0";
				}
			}
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getTax()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return tax;
	}

	private String getServicesAditionalDescription(Connection con, String serviceId) {
		String description = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = con.prepareStatement("select pack_web.fun_srvc_name(?) from dual");
			pst.setString(1, serviceId);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				description = rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getServicesAditionalDescription()_Error:").append(e).toString());
			e.printStackTrace();
		}finally {
			resources.closeResources(rs, pst);
		}		
		//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getServicesAditionalDescription()_").append("$$$$$$$$$$$$$$$$$$$$$$$$ ADITIONAL SERVICE DESCRIPTION").append(description).toString());
		return description;
	}

	private String getAdditionalValue(Connection con, Global global, String serviceId, String referenceServiceId) {
		String serviceValue = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = conct.delete(0,conct.length()).append("select wt_srvc_cant from  web_clnt_srvc_trif where wt_orgn_clnt_id = ? and ")
					.append(" substr(WT_ORGN_BRNC_ID,1,3) = substr(?,1,3) and substr(WT_DEST_BRNC_ID,1,3) = substr(?,1,3) and wt_trif_slab = ? and ")
					.append("wt_refr_srvc_id= ? and wt_srvc_id = ?").toString();
			pst = con.prepareStatement(query);
			//pst.setString(1, global.clientId);
			pst.setString(1, global.getClientIdAgreement());
			pst.setString(2, global.getAssignedBranch());
			pst.setString(3, global.getDestinationBranchId());
			pst.setString(4, global.getTarifType());
			pst.setString(5, referenceServiceId);
			pst.setString(6, serviceId);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				serviceValue = rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getAdditionalValue()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);			
		}
		return serviceValue;
	}

	private String getAdditionalValueKm(Connection con, Global global, String serviceId, String referenceServiceId) {
		String serviceValue = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = conct.delete(0,conct.length()).append("select wt_srvc_cant from  web_clnt_srvc_trif_km where wt_orgn_clnt_id = ? and ")
					.append("? between WT_DSTN_FROM_KM and WT_DSTN_TO_KM and wt_trif_slab = ? and ")
					.append("wt_refr_srvc_id= ? and wt_srvc_id = ?").toString();
			pst = con.prepareStatement(query);
			
			//pst.setString(1, global.getClientId());
			pst.setString(1, global.getClientIdAgreement());
			pst.setString(2, global.getKmTarifType());
			pst.setString(3, global.getTarifType());
			pst.setString(4, referenceServiceId);
			pst.setString(5, serviceId);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				serviceValue = rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getAdditionalValueKm()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);			
		}
		return serviceValue;
	}
	
    private String getAdditionalValueFactor(Connection con, Global global, String serviceId, String referenceServiceId, String factor) {//AAP26
        String serviceValue = "N";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            String query = conct.delete(0,conct.length())
                    .append("select nvl(wcp_srvc_cant,'E') from WEB_CLNT_SRVC_TRIF_FACTOR where wcp_orgn_clnt_id = ? and ")
                    .append(" substr(WCP_ORGN_BRNC_ID,1,3) = substr(?,1,3) and substr(WCP_DEST_BRNC_ID,1,3) = substr(?,1,3) and wcp_trif_slab = ? and ")
                    .append("wcp_refr_srvc_id= ? and wcp_srvc_id = ? AND WCP_FACTOR = ? order by WCP_FACTOR_VALUE desc").toString();
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
            AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getAdditionalValueFactor()_Error:").append(e).toString());
            e.printStackTrace();
        } finally {
            resources.closeResources(rs, pst);			
        }
        return serviceValue;
    }

    private String getAdditionalValueFactorKm(Connection con, Global global, String serviceId, String referenceServiceId, String factor) {//AAP26
        String serviceValue = "N";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            String query = conct.delete(0,conct.length())
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
            AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getAdditionalValueFactorKm()_Error:").append(e).toString());
            e.printStackTrace();
        } finally {
            resources.closeResources(rs, pst);			
        }
        return serviceValue;
    }
	
	private String getComment(Connection con, String valorCod) {
		CallableStatement cst = null;
		String commentText = "";
		try {			
			cst = con.prepareCall("{call pack_web.PRO_COD_TEXT(?,?)}");
			cst.setDouble(1, Double.parseDouble(valorCod));
			cst.registerOutParameter(2, Types.VARCHAR);
			cst.executeQuery();
			commentText = cst.getString(2);
			//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getComment()_").append(global.clientId).append("**************************** comment text").append(commentText).toString());			
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getComment()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return commentText;
	}

	/****************************************************************************************************************
    * Metodo para obtener la tarifa especial para cliente, en caso de que tenga zona extendida y tarifa configurada*
    ****************************************************************************************************************/
   private String getTarifaEspecialExt(Connection con, JavWebBookingGeneralMainForm aform, String referenciaItem, String idItem, Global global){
		PreparedStatement pst = null;
		ResultSet rs = null;
		String tarifaEspecial = "0";
		//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getTarifaEspecialExt()aform.getOrgienclave()==>").append(aform.getOrgienclave()).toString());		
		//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getTarifaEspecialExt()_idItem==>").append(idItem).toString());
		//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getTarifaEspecialExt()_referenciaItem==>").append(referenciaItem).toString());
		//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getTarifaEspecialExt()aform.getDestinationsitecode()==>").append(aform.getDestinationsitecode()).toString());
		try {					
			pst = con.prepareStatement("SELECT WT_TRIF_AMNT FROM WEB_CLNT_SRVC_TRIF WHERE WT_ORGN_CLNT_ID = ? AND WT_SRVC_ID = ? AND WT_REFR_SRVC_ID = ? AND WT_ORGN_BRNC_ID = ? AND WT_DEST_BRNC_ID = ?");
			//pst.setString(1,aform.getOrgienclave());
			pst.setString(1,global.getClientIdAgreement());
			pst.setString(2,idItem);
			pst.setString(3,referenciaItem);
			pst.setString(4,aform.getOrgioncode());
			pst.setString(5,aform.getDestinationsitecode());
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				//tarifa Especial
				if (rs.getString(1)!=null){
					tarifaEspecial = rs.getString(1);
				}
			}
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getTarifaEspecialExt()_Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.closeResources(rs, pst);
		}
		return tarifaEspecial;
	}
   
   /****************************************************************************************************************
    * Metodo para obtener la tarifa especial para cliente, en caso de que tenga zona extendida y tarifa configurada*
    ****************************************************************************************************************/
   private String getTarifaEspecialExtKm(Connection con, JavWebBookingGeneralMainForm aform, Global global, String referenciaItem, String idItem){
		PreparedStatement pst = null;
		ResultSet rs = null;
		String tarifaEspecial = "0";
		try {					
			pst = con.prepareStatement("SELECT WT_TRIF_AMNT FROM WEB_CLNT_SRVC_TRIF_KM WHERE WT_ORGN_CLNT_ID = ? AND WT_SRVC_ID = ? AND WT_REFR_SRVC_ID = ? AND ? between WT_DSTN_FROM_KM and WT_DSTN_TO_KM");
			//pst.setString(1,aform.getOrgienclave());
			pst.setString(1,global.getClientIdAgreement());
			pst.setString(2,idItem);
			pst.setString(3,referenciaItem);
			pst.setString(4,global.getKmTarifType());
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				//tarifa Especial
				if (rs.getString(1)!=null){
					tarifaEspecial = rs.getString(1);
				}
			}
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getTarifaEspecialExt()_Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.closeResources(rs, pst);
		}
		return tarifaEspecial;
	}
   
   /****************************************************************************************************************
    * Metodo para obtener la tarifa especial para cliente, en caso de que tenga zona extendida y tarifa configurada*
    ****************************************************************************************************************/
   @SuppressWarnings("rawtypes")
   private String getTarifaEspecialExtAll(Connection con, JavWebBookingGeneralMainForm aform, ArrayList servicesDetailArray, String referenciaItem, String idItem, Global global, String tarifEspGen, String tarifBase){
		PreparedStatement pst = null;
		ResultSet rs = null;
		String tarifaEspecial = "0";
		JavTariff javTariff = new JavTariff();
		double dTarifaEspecial = 0;
		double dTarifEspGen = 0;
		double dTarifBase = 0;
		double tarifTotal = 0;
		int cantidad = 0;
		
		try {
			try { dTarifEspGen = Double.parseDouble(tarifEspGen); } catch (Exception e) { dTarifEspGen = 0;}
			try { dTarifBase = Double.parseDouble(tarifBase); } catch (Exception e) { dTarifBase = 0;}
			ShipmentServiceDetail ssd = null;
			for (int i = 0; i < servicesDetailArray.size(); i++) {
				ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
				try { cantidad = Integer.parseInt(ssd.getCantidad()); } catch (Exception e) { cantidad = 0;}
				
				if (ssd.getTarifa().equals("T7")){
					if (aform.getDefaultservicescreenKm().equals("Y")) {
						tarifaEspecial = javTariff.getImporteEXTT7convenioKm(con, global.getClientIdAgreement(), aform.getDestinationsitecode(), ssd.getTarifa(), idItem, ssd.getCantidad(), ssd.getPeso(), ssd.getVolumen(), global.getKmTarifType());
					} else {
						tarifaEspecial = javTariff.getImporteEXTT7convenio(con, global.getClientIdAgreement(), aform.getDestinationsitecode(), ssd.getTarifa(), idItem, ssd.getCantidad(), ssd.getPeso(), ssd.getVolumen(), global.getAssignedSite());
					}
					
					try { dTarifaEspecial = Double.parseDouble(tarifaEspecial); } catch (Exception e) { dTarifaEspecial = 0;}					
				}
				
				if (dTarifaEspecial<=0) {
					if (dTarifEspGen<=0) {
						//tarifTotal = tarifTotal + (dTarifBase * cantidad);//costo base (sipweb)
						// Se realiza el c?lculo fuera
						tarifTotal = 0;//costo base (sipweb)
					} else {
						tarifTotal = tarifTotal + (dTarifEspGen * cantidad);//costo tarifa especial web_clnt_srvc_tarif o web_clnt_srvc_tarif_km 
					}
				} else {
					tarifTotal = tarifTotal + (dTarifaEspecial * cantidad);//costo tarifa especial web_clnt_srvc_tarif_dtl o web_clnt_srvc_tarif_dtl_km
				}
				/* se reinicia a 0 para nueva iteraccion (esta variable solo tendr? valor cuando la tarifa sea T7)*/
				dTarifaEspecial = 0;
				tarifaEspecial = "0";
			}
			
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getTarifaEspecialExtAll()_Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.closeResources(rs, pst);
		}
		return new DecimalFormat("0.00").format(tarifTotal);
	}
   
   /****************************************************************************************************************
    * Metodo para obtener informacion de cliente destino para validaciones de flete por cobrar                     *
    ****************************************************************************************************************///AAP07
   @SuppressWarnings({ "rawtypes", "unchecked" })
private ArrayList getClientInfo(Connection con, String clientId) {//AAP07
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList result = null;
		String query = "SELECT CM_TAX_ID, CM_CLNT_TYPE, CM_RET_FLAG FROM SYS_CLNT_MSTR WHERE CM_CLNT_ID = ?";

		try {
			pst = con.prepareStatement(query);
			pst.setString(1, clientId);

			rs = pst.executeQuery();

			if (rs.next()) {
				result = new ArrayList(rs.getFetchSize());
				result.add(rs.getString(1) == null ? "" : rs.getString(1));
				result.add(rs.getString(2) == null ? "I" : rs.getString(2));
				result.add(rs.getString(3) == null ? "N" : rs.getString(3));
			} else {
				result = new ArrayList(0);
			}
		} catch (Exception exe) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getClientInfo()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return result;
	}
   
   @SuppressWarnings({ "rawtypes", "unchecked" })
   private ArrayList getServicesConfiguration(Connection con, Global global, String serviceId, String referenceServiceId) {		
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList result = new ArrayList(1);
		ArrayList row = new ArrayList(1);
		try {
			pst = con.prepareStatement("SELECT WT_FACTOR_VALUE FROM WEB_CLNT_SRVC_TRIF WHERE WT_ORGN_CLNT_ID = ? AND WT_ORGN_BRNC_ID = ? AND WT_DEST_BRNC_ID = ? AND WT_TRIF_SLAB = ? AND WT_REFR_SRVC_ID = ? AND WT_SRVC_ID = ?");			
			
			//pst.setString(1, global.getClientId());
			pst.setString(1, global.getClientIdAgreement());			
			pst.setString(2, global.getAssignedBranch().substring(0,3));
			pst.setString(3, global.getDestinationBranchId().substring(0,3));
			pst.setString(4, global.getTarifType());
			pst.setString(5, referenceServiceId);
			pst.setString(6, serviceId);
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				row.add(rs.getString(1) == null ? "" : rs.getString(1));
				result.add(row.clone());
				row.clear();
			}
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getServicesConfiguration()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}		
		return result;
	}
   
   @SuppressWarnings({ "rawtypes", "unchecked" })
   private ArrayList getServicesConfigurationKm(Connection con, Global global, String serviceId, String referenceServiceId) {		
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList result = new ArrayList(1);
		ArrayList row = new ArrayList(1);
		try {
			pst = con.prepareStatement("SELECT WT_FACTOR_VALUE FROM WEB_CLNT_SRVC_TRIF_KM WHERE WT_ORGN_CLNT_ID = ? AND ? between WT_DSTN_FROM_KM and WT_DSTN_TO_KM AND WT_TRIF_SLAB = ? AND WT_REFR_SRVC_ID = ? AND WT_SRVC_ID = ?");			
			
			pst.setString(1, global.getClientIdAgreement());
			pst.setString(2, global.getKmTarifType());
			pst.setString(3, global.getTarifType());
			pst.setString(4, referenceServiceId);
			pst.setString(5, serviceId);
			
			rs = pst.executeQuery();
			
			while (rs.next()) {
				row.add(rs.getString(1) == null ? "" : rs.getString(1));
				result.add(row.clone());
				row.clear();
			}
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0,conct.length()).append(msgErr).append("getServicesConfigurationKm()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}		
		return result;
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
   @SuppressWarnings("rawtypes")
private List<Object> calculoShipG(Connection con, JavWebBookingGeneralMainForm aform, Global global, Double shipGcostoBase, Double shipGimporte, DecimalFormat df, String groupClientId, Integer shipGQty, ArrayList servicesDetailArray, boolean noRetiene, String idCompany) throws Exception {
	   String discountShipG[] = new String[2];
	   Double descuentoBase=0.00;
	   double minamount = 0.0;
		if(aform.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
			String typeCalculoTariff = ""; 
			for (int i = 0; i < servicesDetailArray.size(); i++) {				
				ShipmentServiceDetail ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
				if (!ssd.getServiceId().equalsIgnoreCase("SHP-E")) {
					typeCalculoTariff  = ssd.getDatosCalculosTarifaSEG().getTypeCalculoTariff();
					break;
				}
			}
			discountShipG[0] = null;
			discountShipG[1] = "0";
			if (typeCalculoTariff.equalsIgnoreCase("PISO")) {												
				ShipTypeSEG ship = findServiceSEG(aform);
				discountShipG = calculateQuantity(con, groupClientId, ship.getShipTypeSEGSrvcRefr(), df.format(shipGQty), df.format(shipGimporte), df.format(minamount));
			} else {
				if (shipGcostoBase>0 && shipGcostoBase>shipGimporte) {		
					descuentoBase = shipGcostoBase - shipGimporte;
					discountShipG[1] =  String.valueOf( df.format(descuentoBase) );
					shipGimporte = shipGcostoBase;
				}
			}
		} else if (aform.getDefaultservicescreen().equalsIgnoreCase("yes")) {
			discountShipG[0] = null;
			discountShipG[1] = "0";
			
			if (shipGcostoBase>0 && shipGcostoBase>shipGimporte) {//AAP11		
				descuentoBase = shipGcostoBase - shipGimporte;//AAP11
				discountShipG[1] =  String.valueOf( df.format(descuentoBase) );//AAP11
				shipGimporte = shipGcostoBase;//AAP11
			}//AAP11
			
		} else {
			//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append(" BEFORE CALLING CALCULATEQUANTITY OF SHIPG").toString());
			discountShipG = calculateQuantity(con, groupClientId, "PACKETS", String.valueOf(shipGQty), df.format(shipGimporte), df.format(minamount));
			//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("calculateServices()_").append(global.clientId).append(" AFTER CALLING CALCULATEQUANTITY OF SHIPG").toString());
		}

		String tax[] = getTax( con, global,	"PACKETS", df.format(shipGimporte - Double.parseDouble(discountShipG[1])), noRetiene, idCompany, aform.getFormaPago());
		Services shipG = new Services();
		String descriptionShipG = getServicesAditionalDescription(con, "SHP-G");
		shipG.descriptionAditional = descriptionShipG;
		shipG.GS_SRVC_ID = "SHP-G";
		shipG.GS_DISC = discountShipG[1];
		shipG.GS_TAX = tax[0];
		shipG.GS_TAX_RET = tax[1];
		
		shipG.GS_SUB_TOTL = df.format(shipGimporte);
		shipG.GS_ADD_PYMT_FLAG = "N";
		shipG.GS_SRVC_TYPE = "S";
		shipG.GS_DOCU_TYPE = "Q";
		shipG.GS_GUIA_TYPE = "H";
		shipG.GS_STUS_FLAG = "A";
		shipG.GS_DISC_SLAB_NO = discountShipG[0];
		shipG.TOTAL = df.format((shipGimporte) - Double.parseDouble(discountShipG[1]));
		List<Object> list = new ArrayList<Object>();
		list.add(shipG);
		list.add(Double.parseDouble(discountShipG[1]));//Descuento
		list.add(Double.parseDouble(tax[0]));//iva
		list.add(Double.parseDouble(tax[1]));//iva retenido
		list.add((tax[2]));//compa?ia srvc si tiene un set asignado
		return list;
   }

	private boolean getRetIvaByCompany(Connection con, String companyID) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean retiene = false;
		try {
			pst = con.prepareStatement("SELECT FUN_GET_RET_FLAG_BY_CPNY(?) FROM DUAL");
			pst.setString(1, companyID);

			rs = pst.executeQuery();

			if (rs.next()) {
				String flag = rs.getString(1) == null ? "" : rs.getString(1);
				retiene = flag.equalsIgnoreCase("Y");
			}
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0, conct.length()).append(msgErr).append("getRetIvaByCompany()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return retiene;
	}
	
	//recalcula compa?ia en detalle de envio, en base a forma de pago//AAP23
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ArrayList companyDefine(Connection con, ArrayList servicesDetailArray, JavWebBookingGeneralMainForm aform, Global global, String companySrvcCalculada) {
		JavTariff company = new JavTariff();
		try {
			for (int i = 0; i < servicesDetailArray.size(); i++) {
				ShipmentServiceDetail ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
				if (aform.getFormaPago().equals("TO_PAY")) {
					if (companySrvcCalculada != null) {
						List<Object> list = company.getTaxRenEspecial(con, global.getGroupClientIdDestino(), ssd.getRefServiceId(), companySrvcCalculada, ssd.getServiceId());
						if (Integer.valueOf(list.get(0).toString()) > -1) {
							ssd.setCompanyID(list.get(2).toString());
						}
					} else {
						ssd.setCompanyID(company.getCompanyId(con, ssd.getPeso(), aform.getDestinationclave(),ssd.getServiceId()));
					}
				} else {// PAID

					if (companySrvcCalculada != null) {
						List<Object> list = company.getTaxRenEspecial(con, global.getGroupClientId(), ssd.getRefServiceId(), companySrvcCalculada, ssd.getServiceId());
						if (Integer.valueOf(list.get(0).toString()) > -1) {
							ssd.setCompanyID(list.get(2).toString());
						}
					} else {
						ssd.setCompanyID(company.getCompanyId(con, ssd.getPeso(), global.getClientId(), ssd.getServiceId()));
					}
				}
				servicesDetailArray.set(i, ssd);
			}
			
		} catch (Exception e) {
			AccessLog.Log(conct.delete(0, conct.length()).append(msgErr).append("companyDefine()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return servicesDetailArray;
	}

}
