package beanAction;


/**----------------------------------------------------------
Author					:	V.RamachandranG
Date					:	25-March-2003
FileName				:	JavWebBookingServicesAction.java
SessionVariables		:
Other Used Classes		:
Function Names			:
Modified by amaladoss  On 9-Jan-2007: To solve Insurance service display and amount difference in guia_head and service tables 
Modified by Balaji  On 03-Jun-2009: To Check the "RAD" service for client in the SYS_CLNT_SRVC. 
Modified by Balaji  On 07-Jul-2009: To Skip record insertion when "RAD" service amount is "Zero" (i.e. '0'). 
Modified by Balaji  On 16-Nov-2009: To evaluate "RAD" service from PACK_WEB.FUN_FTCH_RAD_SRVC FUNCTION.  
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP05
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 04/07/2013
 * Descripci?n: Se agregaron m?todos y condiciones para validaciones de flete por cobrar en los servicios.
 * ------------------------------------------------------------------
 * Clave: AAP09
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 03/02/2015
 * Descripci?n: 
 * Se comento c?digo para que siempre se ejecute procedimiento almacenado "PRO_ASSIGN_DEST_BRNC" que determina la sucursal a asignar como destino.
 * Se agreg? validaci?n de sucursal virtual para entrega OCURRE.
 * ------------------------------------------------------------------ 
 */

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import logger.AccessLog;
import bean.AdditionalService;
import bean.CalculateServices;
import bean.ConsultaParametros;
import bean.GenerateBooking;
import bean.Global;
import bean.Insurance;
import bean.JavTariff;
import bean.Resources;
import bean.ServicesTotal;
import bean.ShipmentServiceDetail;
import bean.SucursalesConfiguradas;
import beanForm.JavWebBookingGeneralMainForm;

public class JavWebBookingGeneralMainServices {
	private StringBuffer cnct = new StringBuffer();
	private final String msgAvi = new StringBuffer("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	
	//long outtime;
	//long intime;
	
	@SuppressWarnings("rawtypes")
	public String perform(JavWebBookingGeneralMainForm aform, Connection con,
			HttpServletRequest req, HttpSession session, Global global) {
		//String insuranceView = "";
		String returnPage = "thispage";
		try {
			con.setAutoCommit(false);			
			String groupClientId = getGroupClientId(con,aform);

			String action = aform.getAccionServices();
			aform.setIsSoloSobre(isSoloSobre(session));//AAP05
			String sucDestSobre = verificaSoloSobres(aform, con);//AAP08
			
			aform.setHasEnvelope(hasEnvelope(session));//AAPXX			
			
			CalculateServices calServ = new CalculateServices(); 
			/* *****************************************************************
			 * realiza validaciones de servicios cuando tenga tarifa extendida *
			 * *****************************************************************/  
			if (!aform.getZonaExtendida().isEmpty() && aform.getZonaExtendida().substring(0, 1).equals("Y") &&
					!aform.getDestinationcode().isEmpty() && aform.getDestinationcode().contains("70")) {
				JavTariff tarifa = new JavTariff();
				if (tarifa.isTarifaInvalidaNew(con, session, aform.getDefaultservicescreen(), aform.getOrgienclave(), aform.getDestinationcode(), aform.getDefaultservicescreenKm())) {
					aform.setTarifaInvalida("Y");
				} else {
					aform.setTarifaInvalida("N");
				}
			}
			
			/* *****************************************************************
			 * realiza validaciones de servicios cuando tenga tarifa extendida *
			 * *****************************************************************/  
			if (global.getAssignedBranch().contains("70")) {
				aform.setZonaExtendida("Y");
				JavTariff tarifa = new JavTariff();
				if (tarifa.isTarifaInvalidaZP(con, session, aform.getDefaultservicescreen(), aform.getOrgienclave(), aform.getDestinationcode(), aform.getDefaultservicescreenKm())) {
					aform.setTarifaInvalidaZp("Y");
				} else {
					aform.setTarifaInvalidaZp("N");
				}
			}else {
				aform.setTarifaInvalidaZp("N");
			}
			
			if (action.equalsIgnoreCase("initServices")) {
				aform.setAllowedFXC(global.getAllowedFXC());//AAP05
				//aform.setFormaPago("PAID");//AAP05
				aform.setFormaPago(aform.getFormaPago().equalsIgnoreCase("TO_PAY") ? aform.getFormaPago() : "PAID") ;//AAP05
				
				//ArrayList envioEmail = getInputEmail(con, aform, global);//AAP06
				
				/*if (!envioEmail.isEmpty()) {//AAP06
					aform.setSendeMailOrigBD(envioEmail.get(0).toString());//AAP06
					aform.setSendeMailDestBD(envioEmail.get(1).toString());//AAP06
				}//AAP06
				
				if (aform.getSendeMailOrigBD().equalsIgnoreCase("Y")) {//AAP06
					aform.seteMailOrigText(aform.geteMailOrigBD());//AAP06					
				}//AAP06
				
				if (aform.getSendeMailDestBD().equalsIgnoreCase("Y")) {//AAP06
					aform.seteMailDestText(aform.geteMailDestBD());//AAP06
				}
				*/
				aform.seteMailOrigText(aform.geteMailOrigBD());
				aform.seteMailDestText(aform.geteMailDestBD());
				/*verifica zona extendida*/
				String zonaExt = validaZonaExtendida(con, aform, global);
				aform.setZonaExtendida(zonaExt);
				//AccessLog.Log(cnct.delete(0,cnct.length()).append("perform()_zona Extendida ").append(aform.getZonaExtendida()).toString());

				/* *****************************************************************
				 * realiza validaciones de servicios cuando tenga tarifa extendida *
				 * *****************************************************************/  
				if (aform.getZonaExtendida().substring(0, 1).equals("Y") && aform.getDestinationcode().contains("70")) {
					JavTariff tarifa = new JavTariff();
					if (tarifa.isTarifaInvalidaNew(con, session, aform.getDefaultservicescreen(), aform.getOrgienclave(), aform.getDestinationcode(), aform.getDefaultservicescreenKm())) {
						aform.setTarifaInvalida("Y");
					} else {
						aform.setTarifaInvalida("N");
					}
				}
				
				/* *****************************************************************
				 * realiza validaciones de servicios cuando tenga tarifa extendida *
				 * *****************************************************************/  
				if (global.getAssignedBranch().contains("70")) {
					aform.setZonaExtendida("Y");
					JavTariff tarifa = new JavTariff();
					if (tarifa.isTarifaInvalidaZP(con, session, aform.getDefaultservicescreen(), aform.getOrgienclave(), aform.getDestinationcode(), aform.getDefaultservicescreenKm())) {
						aform.setTarifaInvalidaZp("Y");
					} else {
						aform.setTarifaInvalidaZp("N");
					}
				}else {
					aform.setTarifaInvalidaZp("N");
				}
				
				/*obtiene bandera para capturar servicios adicionales*/
	            //String additionalService = getAdditionalService(con, global);//AAPXX
				String additionalService = "";//AAPXX
	            if (aform.getDefaultservicescreenKm().equals("Y")) {//AAPXX
	            	additionalService = getAdditionalServiceKm(con, global);
	            } else {
	            	additionalService = getAdditionalService(con, global);
	            }
	           aform.setShowAdditional(additionalService) ;
	     		
	     		/*verifica si la clasificacion de la tarifa es nueva para presentar los datos de los servicios a seleccionar*/
	     		if (aform.getClasifTarif().equals("1")) {
	     			Insurance insurance = new Insurance();
	     			//obtiene acuses de recibo a presentar
					insurance.fetchAcknowledgementTypeDesc(con, aform, global);//AAPXX Se agrego variable global.
	     		}
				
				aform.setAccionServices("initServicesOK");//se asigna este valor para ya no ejecutar el proceso de inicio de seccion "servicios"
				if(!obtieneInfRequerimientosACK(con, groupClientId, "ACK-X")) {
					aform.setReqAcuseXT("N");
					if ("ACK-X".equals(aform.getAcusederecibo())) {
						aform.setAcusederecibo("ACK-N");
					}
				} else {
					aform.setReqAcuseXT("Y");
				}
				if (obtieneInfRequerimientosACK(con, global.getGroupClientId(), "ACK-C")) {
					aform.setReqAcuse("Y");
				} else {
					aform.setReqAcuse("N");
					
					if (aform.getAcusederecibo().equals("ACK-C")) {
						aform.setAcusederecibo("ACK-N");
					}
				}				
				
				/*valida sucursal virtual si entrega es OCURRE, para cambiar COMBOBOX a EAD*/
				if (aform.getEntrega().equals("1")) {//AAP09
					aform.setBrncVrtl(valBranchVrtl(con, aform.getDestinationcode()));//AAP09
					
					if (aform.getBrncVrtl().equals("1")) {//AAP09
						aform.setEntrega("2");//AAP09			
					}//AAP09
				}//AAP09
				
				aform.setFlagValidRefrClnt(getFlagValidRefrClnt(con, global));
			} else if (action.equalsIgnoreCase("checkbranch")) {
				if (aform.getEntrega().equals("1")) {//AAP09
					aform.setBrncVrtl(valBranchVrtl(con, aform.getDestinationcode()));//AAP09
					if (aform.getBrncVrtl().equals("1")) {//AAP09
						aform.setEntrega("2");//AAP09
						cnct.delete(0, cnct.length()).append("La sucursal ").append(aform.getDestinationbranch()).append(" solamente cuenta con servicio de entrega EAD (Sucursal Virtual).");//AAP09
						req.setAttribute("errMsgBrncVrtl",cnct.toString());//AAP09
					}//AAP09
				}//AAP09		
				checkBranch(con, aform, global, sucDestSobre);
			} else if (action.equalsIgnoreCase("cobertura")) {
				
				Insurance insurance = new Insurance();
				String insuranceInformation = insurance.getInsuranceInformation(con,aform.getValordeclarado().trim(),groupClientId,aform,true);
				//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("perform()_").append(clientId).append("******************** INSURANCE INFORMATION ").append(insuranceInformation).toString());
				//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("perform()_").append(clientId).append(".....................................................").toString());
				aform.setSeguro(insuranceInformation);
			} else if (action.equalsIgnoreCase("insuranceamount")) {
				//System.out.println("*******************************insuranceamount aform.getZonaExtendida() "+aform.getZonaExtendida());
				//System.out.println("*******************************insuranceamount aform.getOperadorLogistico() "+aform.getOperadorLogistico());
				Insurance insurance = new Insurance();
				
				ConsultaParametros topeValorDeclarado = new ConsultaParametros();
				
				Double tope = Double.parseDouble(((ArrayList) topeValorDeclarado.QryMdulTypeParm1(con,"BOK","MAX_DECL_AMNT", "AMOUNT").get(0)).get(2).toString());
				
			    Double valorDeclarado = Double.valueOf(aform.getValordeclarado().isEmpty() ? "0":
			    		aform.getValordeclarado().replace(",", ""));
				if (aform.getValordeclarado() != null && !aform.getValordeclarado().isEmpty() &&
						valorDeclarado>tope) {
					req.setAttribute("errorMsg", "Monto Máximo a Capturar de Valor Declarado para este Envio: "+tope);
				}
				
				String insuranceAmount = insurance.getInsuranceAmount(con, session, aform, false);
				aform.setValordeclarado(insuranceAmount);
				
				ArrayList ol = getLcZone(con, aform);//AAP04
				if (!ol.isEmpty()) {//AAP04
					aform.setOperadorLogistico(ol.get(1).toString());//AAP04
				}//AAP04
				/*VALIDACION PARA LIMITE DE VALOR DECLARADO CON OPERADOR LOGISTICO*/
				if (aform.getValordeclarado() != null && aform.getValordeclarado().trim().length()>0) {
					double montoMaxOl = 0;
					aform.setMontoMaxOl("");
					if ((aform.getZonaExtendida().substring(0, 1).equals("Y") && aform.getDestinationcode().contains("70"))|| aform.getOperadorLogistico().trim().length()>0) {						
						ol = obtieneInfOperadorLogistico(con, aform);
						if (!ol.isEmpty()) {
							try {
								montoMaxOl = Double.parseDouble(ol.get(3).toString());
							} catch (Exception e) {
								montoMaxOl = 0;
							}
							if (Double.parseDouble(aform.getValordeclarado())>montoMaxOl) {
								aform.setMontoMaxOl(ol.get(3).toString());								
							}
						}
					}
					//}//AAP04
				}
			} else if (action.equalsIgnoreCase("addService")) {
				//Add the additional service in UI
				returnPage = agregarServAdicional(con, aform, session, req);
			} else if (action.equalsIgnoreCase("deleteService")) {
				//delete the additional service in UI
				int index = Integer.parseInt(req.getParameter("index"));
				ArrayList additionalServicesArray =  (ArrayList)session.getAttribute("aditionalServicesDetail");
				additionalServicesArray.remove(index);
                session.setAttribute("aditionalServicesDetail",additionalServicesArray);
                returnPage = "thispage";
			} else if (action.equalsIgnoreCase("calculate")) {
				returnPage = getCoberturaSobre(aform, con, session);
				if (returnPage.length() == 0 ) {
					if(beforeCredit(con, global, req)) {
						validateFields(con, req, aform, global);
					}
				}
				returnPage = "thispage";
			} else if (action.equalsIgnoreCase("generate")) {
				if (!aform.getPedinumber().isEmpty()) {
					String valResult = validaPedimento(con, aform.getPedinumber()); 
					if (!valResult.equalsIgnoreCase("OK")){
						req.setAttribute("errorMsg", "Corregir número de pedimento o no capturarlo.");
						aform.setLastWrongPediNum(aform.getPedinumber());
						aform.setErrMsgPediNum(valResult);
						aform.setPedinumber("error");
						aform.setCustagent("");
					}else {
						aform.setLastWrongPediNum("");
					}
				}
				if (!aform.getFlagValidProductId().equalsIgnoreCase("Y")) {
					req.setAttribute("errorMsg", "Una o m?s l?neas de env?o necesitan cambio de producto.");
				}
				
				if (req.getAttribute("errorMsg")==null ||
						req.getAttribute("errorMsg").toString().isEmpty()){
					returnPage = getCoberturaSobre(aform, con, session);				
					if (returnPage.length() == 0 ) {
						//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("perform()_").append(clientId).append("aform.getConfirmgenerate()_1==>").append(aform.getConfirmgenerate()).toString());
						boolean valida = false;
						if (aform.getConfirmgenerate().trim().length() == 0) {
							if (beforeCredit(con, global, req)) {
								if (validateFields(con, req, aform, global)) {
									valida = true;
								}
							}
							
							//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("perform()_").append(clientId).append("---bandera valida ==>").append(valida).toString());
							if (valida) {
								aform.setConfirmgenerate("yes");
							}
							returnPage = "thispage";
						} else if (aform.getConfirmgenerate().trim().equalsIgnoreCase("yes")) {
							aform.setConfirmgenerate("");
							GenerateBooking genBooking = new GenerateBooking();
							returnPage = genBooking.generate(aform, con, req, session, global);
						}
						//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("perform()_").append(clientId).append("aform.getConfirmgenerate()_2==>").append(aform.getConfirmgenerate()).toString());
						//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("perform()_").append(clientId).append("---LO QUE VALE RETURNPAGE==>").append(returnPage).toString());
					}
				}
			} else if (action.equalsIgnoreCase("showAditional")) {
				
				calServ.calculateServices(con, aform, req, global);
				//insuranceView = getInsuranceViewFlag(con,global,req);
				//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("perform()_").append(clientId).append("insuranceView ==>").append(insuranceView).toString());
				//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("perform()_").append(clientId).append("global.displayAmountFlag ==>").append(global.displayAmountFlag).toString());
				returnPage = "thispage";
			} else if (action.equalsIgnoreCase("validRefrClnt")) {
				HashMap<String, String> valuesRefr = validRefrClnt(con, aform, global);
				
				String valResponse = valuesRefr.get("valResponse").toString();
				String msgeRefr = valuesRefr.get("msgeRefr").toString();
				
				if (valResponse.equals("1")) {
					String listRefer = aform.getListReferences();
					listRefer = listRefer + aform.getReference()+"|";
					aform.setListReferences(listRefer);
					aform.setReference("");					
				} else {
					req.setAttribute("errorMsg", "Referencia no valida. "+msgeRefr);
				}				
			}

			//Form Validation and Populating fields
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append("perform()_").append("aform.i==>").append(aform.getI()).toString());
			if(aform.getI() == 0) {
				//For Showing Delivery Type
			//	intime=System.currentTimeMillis();
				//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("perform()_").append(clientId).append("....................................................").toString());
				//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("perform()_").append(clientId).append("Getting inside Form Validation and Populating fields").toString());
				
				//Default Insurance Amount and Populating Insurance Type
				aform.setValordeclarado("1000");
				Insurance insurance = new Insurance();
				
				/*For Showing Delivery Type*/
				insurance.getServiceCovered(con,session,aform);
				//For Acknoledgement Type
				insurance.fetchAcknowledgementTypeDesc(con, aform, global);//AAPXX Se agrego variable global.
				insurance.getInsuranceAmount(con, session, aform, true);
				aform.setI(1);
			}

		} catch(NumberFormatException nfe){
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error_nfe:").append(nfe).toString());
			nfe.printStackTrace();
			returnPage = "thispage";
		} catch(Exception e){
			 try{
				 con.rollback();
			 }catch(Exception ex){
				 AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error_ex:").append(ex).toString());
			 }
			 AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error_e:").append(e).toString());
			 e.printStackTrace();
		} finally {
			try {
				con.setAutoCommit(true);	
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("perform()_Error_e2:").append(e2).toString());
				e2.printStackTrace();
			}			
		}	
		return returnPage;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String agregarServAdicional(Connection con, JavWebBookingGeneralMainForm aform, 
			HttpSession session, HttpServletRequest req) {
		CallableStatement cst = null;
		String returnPage = "";
		//String clientId = aform.getOrgienclave();
		try {
			int size = 0;
			AdditionalService serv = new AdditionalService();
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("agregarServAdicional()_").append(clientId).append("............................................................").toString());
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("agregarServAdicional()_").append(clientId).append("Inside action add if(action!=null && action.equalsIgnoreCase('add')){").toString());
			serv.service=aform.getServiceAdditional().toUpperCase();
			serv.importe = aform.getImporteValue();
			serv.serviceId = aform.getReferenceId();
			ArrayList additionalServicesArray = null ;
			if (session.getAttribute("aditionalServicesDetail") == null) {
				additionalServicesArray = new ArrayList(3);
				//size =  additionalServicesArray.size();
				additionalServicesArray.add(serv);
				aform.setServiceAdditional("");
				aform.setImporteValue("");
				aform.setReferenceId("");
            } else {
				boolean isAvail = true;
				additionalServicesArray = (ArrayList)session.getAttribute("aditionalServicesDetail");
				size =  additionalServicesArray.size();
				for (int i=0; i<size; i++) {
					AdditionalService as = (AdditionalService)additionalServicesArray.get(i);
					AdditionalService asTemp = new AdditionalService();
					
					asTemp.service = aform.getServiceAdditional().toUpperCase();
					asTemp.importe = aform.getImporteValue();
					asTemp.serviceId = aform.getReferenceId();

					if (as.service.equals(asTemp.service)) {
						//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("perform()_").append(clientId).append("..............................................................").toString());
						//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("perform()_").append(clientId).append("Inside Writing Error Messages in if(as.service.equals(asTemp.service))").toString());	
						isAvail = false;
						 String messageText="";
						 String query = "{call pack_web.PRO_SHOW_MESG('WEB',PACK_WEB.LANGUAGE_ID,9000041,1,?,NULL,NULL,?,?)}";
						 cst = con.prepareCall(query);
						 cst.setString(1,aform.getServiceAdditional());
						 cst.registerOutParameter(2,Types.VARCHAR);
						 cst.registerOutParameter(3,Types.VARCHAR);
						 cst.executeQuery();
						 messageText = cst.getString(3);
						 
						 aform.setServiceAdditional("");
						 aform.setImporteValue("");
						 aform.setReferenceId("");
						 
						 req.setAttribute("errMsgAdditional",messageText);
						 break;
					}
				}

                if (isAvail) {
					additionalServicesArray.add(serv);
					aform.setServiceAdditional("");
					aform.setImporteValue("");
					aform.setReferenceId("");
				}
			}
				
			session.setAttribute("aditionalServicesDetail",additionalServicesArray);
			returnPage = "thispage";	
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("agregarServAdicional()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return returnPage;
	}
	@SuppressWarnings("rawtypes")
	private String getCoberturaSobre(JavWebBookingGeneralMainForm aform, Connection con, HttpSession session) {
		String returnPage = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			//AAP//AccessLog.Log(""+ aform.getOrgienclave() + "....................................................");
			//AAP//AccessLog.Log(""+ aform.getOrgienclave() + "Getting inside  calculate or generate");
		
            ArrayList servicesDetailArray = (ArrayList)session.getAttribute("servicesDetail");

            boolean isEnvelopealone = true;
			for (int i=0;i<servicesDetailArray.size();i++) {
				ShipmentServiceDetail ssd = (ShipmentServiceDetail)servicesDetailArray.get(i);
				if (!ssd.serviceId.equalsIgnoreCase("SHP-E")) {
					isEnvelopealone=false;
					break;
				}
			}
			
			if (isEnvelopealone) {
				if (aform.getValordeclarado() != null && aform.getValordeclarado().trim().length()>0) {
					//AAP//AccessLog.Log(""+ aform.getOrgienclave() + "............................................................");
					//AAP//AccessLog.Log(""+ aform.getOrgienclave() + "Inside isEnvelopealone ");
					
					getMessageForEnvelopeAlone(con,aform);
					
					String query =	"SELECT pm_vlue1_desc,PM_PARM_CODE1 FROM sys_parm_mstr Where pm_mdul_id = ? and pm_parm_type = ? and pm_parm_code2 = ?";
					pst = con.prepareStatement(query);
					pst.setString(1, "BOK");
					pst.setString(2, "COVERAGE");
					pst.setString(3, "NULL");
					rs = pst.executeQuery();
					aform.getInsurancetype().clear();
					aform.getInsurancetypelabel().clear();
					
					while(rs.next()){
						aform.setInsurancetypelabel(rs.getString(1));						
						aform.setInsurancetype(rs.getString(2));
					}
					//Default Selection
					aform.setCobertura("INV-1");
 
					session.setAttribute("envelopealone","true");
					returnPage = "thispage";
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCoberturaSobre()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return returnPage;
	}
	
	private void getMessageForEnvelopeAlone(Connection con,
			JavWebBookingGeneralMainForm aform) {
		CallableStatement cst = null;
		String messageText = "";
		try {
			String query = "{call pack_web.PRO_SHOW_MESG('BOK',PACK_WEB.LANGUAGE_ID,900149,1,NULL,NULL,NULL,?,?)}";//Checked
			cst = con.prepareCall(query);
			cst.registerOutParameter(1,Types.VARCHAR);
			cst.registerOutParameter(2,Types.VARCHAR);
			
			cst.executeQuery();
			
			messageText = cst.getString(2);
			
			aform.setErrmsgenvelope(messageText);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getMessageForEnvelopeAlone()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}		
	}

	private String getGroupClientId(Connection con, JavWebBookingGeneralMainForm aform)  {
		String groupIdQuery = "";
		String groupClientId = "";
		PreparedStatement pst = null;
		ResultSet rs = null;		
		try {
			groupIdQuery = "select pack_web.FUN_CHCK_GRUP_CLNT(?)as groupid from dual";
			pst = con.prepareStatement(groupIdQuery);
			pst.setString(1,aform.getOrgienclave());
			rs = pst.executeQuery();
			
			while(rs.next()){
				groupClientId = rs.getString("groupid");
			}
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("getGroupClientId()GROUPCLIENTID:").append(groupClientId).toString());			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getGroupClientId()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return groupClientId.toUpperCase();
	}
	
	private String getBranchColony(Connection con, JavWebBookingGeneralMainForm aform) {
		CallableStatement cst = null;
		String errorMessage="";
		try {			
			String query = "{ call pack_web.pro_brnc_coly(?,?,?,?,?) }";
			cst = con.prepareCall(query);
			
			cst.setString(1,aform.getCitycode());
			cst.setString(2,aform.getDestinationcoloniacode());
			//cst.setString(3,aform.getDestinationcode());//AAP08
			cst.setString(3,aform.getDestinationcodeIni());//AAP08 SE MANEJA LA SUCURSAL SELECCIONADA DE INICIO PARA VALIDACION DE ENTREGA A DOMICILIO
			cst.registerOutParameter(4,Types.VARCHAR);
			cst.registerOutParameter(5,Types.VARCHAR);
			
			cst.executeQuery();

			String lcZone = cst.getString(4);
			String lcBoolean = cst.getString(5);
			
			if(lcBoolean!=null && lcBoolean.equalsIgnoreCase("false")){
				errorMessage = getErrorMessage(con,"900169");
			} else if (lcZone==null){
				errorMessage = getErrorMessage(con,"900194");
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getBranchColony()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return errorMessage ;
	}
	public boolean eadVerify(Connection con, String clntId) {
		boolean result = false;
		String eadValue = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {

			pst = con.prepareStatement("SELECT CM_EAD_FLAG FROM SYS_CLNT_MSTR WHERE CM_CLNT_ID = ?");
			pst.setString(1, clntId);
			rs = pst.executeQuery();
			while (rs.next()) {
				eadValue = rs.getString(1);
			}
			
			if (eadValue != null && eadValue.equalsIgnoreCase("y")) {
				result = true;
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("eadVerify()_Error:").append(e).toString());
		} finally {			
			resources.closeResources(rs, pst);
		}
		return result;
	}

	private String getErrorMessage(Connection con,String number) {
		CallableStatement cst = null;
		String errMsg = "";
		try {
			cst=con.prepareCall("{ call pack_web.pro_show_mesg('BOK',pack_web.language_id,?,1,NULL,NULL,NULL,?,?) }");//Checked
			cst.setInt(1, Integer.parseInt(number));
			cst.registerOutParameter(2,Types.VARCHAR);
			cst.registerOutParameter(3,Types.VARCHAR);
			cst.executeQuery();
			errMsg = cst.getString(3);
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getErrorMessage()_Error:").append(e).toString());
			e.printStackTrace();
		}finally {
			resources.cerrarCallableStatement(cst);
		}		
		return errMsg;
	}

	private boolean validateFields(Connection con, HttpServletRequest req, JavWebBookingGeneralMainForm aform, Global global) {

		HttpSession session = req.getSession(false);
		boolean continuar = true;
		CallableStatement cst = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = ""; 
		String messageText="";
		try {
			if (aform.getFormaPago().equals("PAID")) {
				query = "SELECT CM_CLNT_ID FROM SYS_CLNT_MSTR WHERE CM_CLNT_ID = ? AND CM_CRED_STUS_ID = ? ";
				
		        pst = con.prepareStatement(query);
				pst.setString(1,global.clientId);
				pst.setString(2,"ENA");
				//AAP//AccessLog.Log(""+global.clientId+" inside validate fields.....................................................");
				rs = pst.executeQuery();
				if(!rs.next()) {
					
					String query1 = "{call pack_web.PRO_SHOW_MESG('BOK',PACK_WEB.LANGUAGE_ID,900120,1,NULL,?,NULL,?,?)}";
					cst = con.prepareCall(query1);
					cst.setString(1,global.clientName);
					cst.registerOutParameter(2,Types.VARCHAR);
					cst.registerOutParameter(3,Types.VARCHAR);
					cst.executeQuery();
					messageText = cst.getString(3);
		            req.setAttribute("errormsgstatus",messageText.concat(global.clientName));
		            continuar = false;
				}
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (cst != null)
					cst.close();
				//resources.closeResources(rs, pst, cst);
			}
			//from here added newly
			double doubleguiaAmnt	= 0.0;
			if (continuar && aform.getFormaPago().equals("PAID")) {
				messageText = "";
				double doubleavailCred	= 0.0;
				//double doubleguiaAmnt	= 0.0;
			
				//outtime = System.currentTimeMillis();
				//query = "select pack_web.FUN_CLNT_CRDT_AMT(?) from dual";
				query = "select FUN_CLNT_CRDT_AMT_WW(?) from dual";
				pst = con.prepareStatement(query);
				pst.setString(1,global.getClientId());
				//AAP//AccessLog.Log(""+global.clientId+" inside second statement.....................................................");
				rs = pst.executeQuery();
				if(rs.next()) {
					doubleavailCred = rs.getDouble(1);
				}
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				//resources.closeResources(rs, pst);
				//AAP//AccessLog.Log((global.clientId+".....................AFter pack_web.FUN_CLNT_CRDT_AMT(?)"+(intime-outtime)));
				//AAP//AccessLog.Log(""+global.clientId+".....................................................");
				//AAP//AccessLog.Log(""+global.clientId+"inside Validate Fields the avail cred limit"+doubleavailCred);
				
				if (doubleavailCred>0){
					//AAP//AccessLog.Log(""+global.clientId+"........................... befoer calling cal services");
					CalculateServices calcServ = new CalculateServices();
					calcServ.calculateServices(con, aform, req, global);
					ServicesTotal servicesTotal = (ServicesTotal)session.getAttribute("servicestotal");
					if (servicesTotal == null || servicesTotal.total == null) {
						continuar = false;
					}else {
						doubleguiaAmnt = Double.parseDouble(servicesTotal.total);
					}
					//AAP//AccessLog.Log(""+global.clientId+".....................................................");
					//AAP//AccessLog.Log(""+global.clientId+"inside Validate Fields thepurchased amount"+doubleguiaAmnt);
				}
				if ((doubleavailCred<1) || (doubleavailCred<doubleguiaAmnt)){
					query = "{call pack_web.PRO_SHOW_MESG('BOK', PACK_WEB.LANGUAGE_ID, 900019, 1, NULL, ?, NULL, ?, ?)}";
					cst = con.prepareCall(query);
					cst.setString(1,""+doubleavailCred);
					cst.registerOutParameter(2,Types.VARCHAR);
					cst.registerOutParameter(3,Types.VARCHAR);
					cst.executeQuery();
					
					messageText = cst.getString(3);
					req.setAttribute("erroravailcred",messageText);
					resources.cerrarCallableStatement(cst);
				}
				if (messageText.length()>0) {
					continuar = false;
					//return false;
				}
			} else if (continuar && aform.getFormaPago().equals("TO_PAY")) { 
				CalculateServices calcServ = new CalculateServices();
				calcServ.calculateServices(con, aform, req, global);
				ServicesTotal servicesTotal = (ServicesTotal)session.getAttribute("servicestotal");
				doubleguiaAmnt = Double.parseDouble(servicesTotal.total);
			}
			
			if (continuar){
				String entrega = aform.getEntrega();
				messageText = "";
				if(entrega!=null && entrega.equalsIgnoreCase("2")) {					
					//AAP//AccessLog.Log("global.assignedBranch"+global.assignedBranch);
					//AAP//AccessLog.Log("global.destinationBranchId"+global.destinationBranchId);
					//pst.setString(3,global.destinationBranchId)
					String orginsite=global.assignedBranch.substring(0,3);
					String destsite=global.destinationBranchId.substring(0,3);
					
					if(orginsite.equalsIgnoreCase(destsite)) {
						//AAP//AccessLog.Log("same site");
					} else {
						messageText = getBranchColony(con, aform);
						req.setAttribute("errormsgentrega", messageText);
					}
					
					// valida EAD cliente destino
					if (!eadVerify(con, aform.getDestinationclave())) {
						continuar = false;
						req.setAttribute("errormsgentrega", "La facilidad de Entrega a Domicilio no esta disponible para este cliente Destino");
					}
					
					if(!aform.getShippingType().equalsIgnoreCase("STD-T")) {						
					    boolean cobertura = getCoverageSEG(con, aform.getDestinationcoloniacode(),  aform.getDestinationclave(),  aform.getDestinationaddresscode(), aform.getTypeSEGOperativa(aform.getShippingType()));
					    if(!cobertura) {
				    		req.setAttribute("errormsgentrega","No tenemos disponible servicio \"" + aform.getTypeSEGOperativa(aform.getShippingType()) + " \" para este domicilio, solo se puede enviar a ocurre.");					    	
					    	continuar = false;
		        		}
					}
				}
				
				if (messageText.length()>0 ) {
					continuar = false;
					//return false;
				}
			}
			
			if (continuar) {
				//Above code added by Kumaran on 04/02/2005 to make the credit check
				//Valor COD Validation
				String valorCOD = aform.getValorcod().trim();
				double doublevalorCOD = 0.0;

				if(valorCOD!=null && valorCOD.length()>0) {
					doublevalorCOD = Double.parseDouble(valorCOD);
				}
				messageText = "";
				if(doublevalorCOD < 0){
					query = "{call pack_web.PRO_SHOW_MESG('BOK', PACK_WEB.LANGUAGE_ID, 900222, 1, NULL, NULL, NULL, ?, ?)}";
					cst = con.prepareCall(query);
					cst.registerOutParameter(1,Types.VARCHAR);
					cst.registerOutParameter(2,Types.VARCHAR);
					
					cst.executeQuery();
					
					messageText = cst.getString(2);
					req.setAttribute("errormsgcod",messageText);
					resources.cerrarCallableStatement(cst);
				}
				if (messageText.length()>0) {
					continuar = false;
				}
			}				
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validateFields()_Error_e:").append(e).toString());
			e.printStackTrace();
		} finally {
			//resources.closeResources(rs, pst, cst);
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (cst != null)
					cst.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validateFields()_Error_e2:").append(e).toString());
				e.printStackTrace();
			}
			
		}
		return continuar;
	}

	//code added by palanivel  to check whether AdditionalService Available for the client or not    
	private String getAdditionalService(Connection con, Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String serviceAvailable = "";
		try {
			String query = "select pack_web.fun_disp_addl_srvc(?,?,?,?) from dual";
			pst = con.prepareStatement(query);
			//pst.setString(1,global.clientId);
			pst.setString(1,global.getClientIdAgreement());
			pst.setString(2,global.getTarifType());
			pst.setString(3,global.getAssignedBranch());
			pst.setString(4,global.getDestinationBranchId());
			rs = pst.executeQuery();
			
			if(rs.next()){
				serviceAvailable = rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getAdditionalService()_Error:").append(e).toString());
			e.printStackTrace();
		}finally {
			resources.closeResources(rs, pst);
		}		
		return serviceAvailable;
	}	

	private String getAdditionalServiceKm(Connection con, Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String serviceAvailable = "";
		try {
			String query = "select pack_web.fun_disp_addl_srvc_km(?,?,?,?) from dual";
			pst = con.prepareStatement(query);
			//pst.setString(1,global.getClientId());
			pst.setString(1,global.getClientIdAgreement());
			pst.setString(2,global.getTarifType());
			pst.setString(3,global.getAssignedBranch());
			pst.setString(4,global.getKmTarifType());
			rs = pst.executeQuery();
			
			if(rs.next()){
				serviceAvailable = rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getAdditionalServiceKm()_Error:").append(e).toString());
			e.printStackTrace();
		}finally {
			resources.closeResources(rs, pst);
		}		
		return serviceAvailable;
	}

	private boolean beforeCredit(Connection con, Global global, HttpServletRequest req) {
		
		String query="select ACP_PRCS_STAT from adm_clnt_pend_amt where acp_clnt_id =?";
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean band = true;
		try {
			pst = con.prepareStatement(query);
			pst.setString(1,global.clientId);
			rs = pst.executeQuery();
			while(rs.next()) {
				if(rs.getString(1).equalsIgnoreCase("P")) {
					req.setAttribute("erroravailcred","Please Wait ....Credit Limit Calculation is in process");
					band = false;
					break;
					//code added by kumaran 14th oct
					//return false;
				}				
			}
		} catch (Exception e) {		
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("beforeCredit()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		
		//AAP//AccessLog.Log("beforeCredit()_band "+band);
		return band;
	}
	
//	private String getDeliveryType(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
//		String deliveryType = "";
//		CallableStatement cst = null;
//		ResultSet rs = null;
//		PreparedStatement pst = null;
//		try {
//			String groupClientId = getGroupClientId(con, aform);
//			String query = "{call pack_web.pro_ftch_srvs_covd(?,?,?,?,?) }";
//			cst = con.prepareCall(query);
//			cst.setString(1, global.tarifType);
//			cst.registerOutParameter(2, Types.VARCHAR);
//			cst.registerOutParameter(3, Types.VARCHAR);
//			cst.registerOutParameter(4, Types.VARCHAR);
//			cst.registerOutParameter(5, Types.VARCHAR);
//
//			rs = cst.executeQuery();
//			String ead = null;
//
//			// if(rs.next()){
//			ead = cst.getString(2);
//			//ack = cst.getString(3);
//			//cod = cst.getString(4);
//			//inv = cst.getString(5);
//			// }
//
//			resources.cerrarCallableStatement(cst);
//
//			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("getDeliveryType()EAD VALUE ").append(ead).toString());
//
//			if (ead == null) {
//				String query1 = "select pack_web.fun_dflt_dlvy(?,?) from dual";
//				pst = con.prepareStatement(query1);
//				pst.setString(1, groupClientId);
//				pst.setString(2, ead);
//				rs = pst.executeQuery();
//				if (rs.next()) {
//					if (rs.getString(1) != null && rs.getString(1).equalsIgnoreCase("TRUE")) {
//						deliveryType = "H";
//					} else {
//						deliveryType = "O";
//					}
//				}
//			} else {
//				deliveryType = "H";
//			}
//		} catch (Exception e) {
//			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDeliveryType()_Error:").append(e).toString());
//			e.printStackTrace();
//		} finally {
//			resources.closeResources(rs, pst, cst);
//		}
//		return deliveryType;
//	}
	

	private void checkBranch(Connection con, JavWebBookingGeneralMainForm aform, Global global, String sucDestSobre) {
		//String deliveryType = "";
		PreparedStatement pst = null;
		CallableStatement cst = null;
		ResultSet rset = null;
		SucursalesConfiguradas suc = new SucursalesConfiguradas();
		try {
			//deliveryType = getDeliveryType(con, aform, global);
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("checkBranch()_After deliveryType ").append(deliveryType).toString());
			String callFunction = "PRO_ASSIGN_DEST_BRNC";//AAP09
			String branchId = "";
			branchId = aform.getDestinationsitecode();
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("checkBranch()_Inside site checking").append(branchId).toString());
			
			//AAP02 SE ELIMIN? CONDICION PORQUE AHORA SE REALIZA EN BASE A LA CONFIGURACION
			/*if (branchId.equalsIgnoreCase("GDL")) {
				if (aform.getEntrega().equalsIgnoreCase("1")) {
					aform.setDestinationcode("GDL01");
				} else {
					aform.setDestinationcode("GDL02");
				}
			}*/ 
			//else {//AAP02
			/*Se coment? codigo para siempre ejecutar procedimeinto de validacion PRO_ASSIGN_DEST_BRNC *///AAP09
//			int fun_chk_phy_dc_exist = 0;
//			int fun_chk_no_of_br = 0;
//
//			pst = con.prepareStatement("select pack_web.fun_chk_phy_dc_exist(?) from dual");
//			pst.setString(1, branchId);
//			rset = pst.executeQuery();
//			if (rset.next()) {
//				fun_chk_phy_dc_exist = rset.getInt(1);
//			}
//
//			resources.closeResources(rset, pst);
//
//			pst = con.prepareStatement("select pack_web.fun_chk_no_of_br(?) from dual");
//			pst.setString(1, branchId);
//			rset = pst.executeQuery();
//			if (rset.next()) {
//				fun_chk_no_of_br = rset.getInt(1);
//			}
//
//			resources.closeResources(rset, pst);
//
//			// AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("checkBranch()_deliveryType ").append(deliveryType).toString());
//			if (deliveryType.equalsIgnoreCase("H")) {
//				callFunction = "PRO_ASSIGN_DEST_BRNC";
//			} else if (deliveryType.equalsIgnoreCase("O")
//					&& (fun_chk_phy_dc_exist == 1) && (fun_chk_no_of_br > 1)) {
//				callFunction = "PRO_ASSIGN_DEST_BRNC";
//			} else {
//				callFunction = "DIRECTBRANCH";
//			}

			String AM_GETY_CODE = "";
			String AM_GETY_TYPE = "";
			String AM_GETY_LEVL = "";
			AM_GETY_CODE = aform.getGetycode();
			AM_GETY_TYPE = aform.getGetytype();
			AM_GETY_LEVL = aform.getGetylevl();
			// AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("checkBranch() AM_GETY_CODE ").append(AM_GETY_CODE).toString());
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
				//AccessLog.Log(cnct.delete(0,cnct.length()).append("checkBranch() the branch is ").append(br).toString());
				//AccessLog.Log(cnct.delete(0,cnct.length()).append("checkBranch() the branchname is ").append(brname).toString());
				aform.setDestinationcode(br);
				aform.setDestinationbranch(brname);
				
				resources.cerrarCallableStatement(cst);
			} else {
				//AccessLog.Log(cnct.delete(0,cnct.length()).append("checkBranch() deliveryType Single Branch ").append(deliveryType).toString());
				String br = "";
				String brname = "";
				pst = con.prepareStatement("select BM_BRNC_ID,BM_BRNC_NAME from sys_brnc_mstr where BM_BRNC_SITE_ID = ? AND BM_FLAG1 = ?");
				pst.setString(1, branchId);
				pst.setString(2, "G");
				rset = pst.executeQuery();
				if (rset.next()) {
					br = rset.getString(1);
					brname = rset.getString(2);
					aform.setDestinationcode(br);
					aform.setDestinationbranch(brname);
				}
				// AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("checkBranch() Single Branch ").append(br).toString());
				// AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("checkBranch() Single Branch name ").append(brname).toString());
			}
			// }//AAP02
			//System.out.println("JavWebBookingGeneralMainServices.checkBranch()_branchId.substring(2)"+branchId.substring(2));
			//System.out.println("JavWebBookingGeneralMainServices.checkBranch()_aform.getDestinationcode().substring(3)"+aform.getDestinationcode().substring(3));
			/*
			 * se asigna el tipo de b?squeda de configuracion en base al tipo de
			 * entrega (Solo se valida entrega ocurre)
			 */
			String entrega = "";
			if (aform.getEntrega().equalsIgnoreCase("1")) {// AAP02
				entrega = "DEST_OCURRE";
			} else {
				if (!aform.getDestinationcode().substring(3).equals("70")) {				
					
					if (sucDestSobre.length()<=0) {
						aform.setDestinationcode(aform.getDestinationcodeIni());//AAP08
						aform.setDestinationbranch(aform.getDestinationbranchIni());//AAP08
					}					
					//entrega = "DEST_EAD_YA_NO";//AAP08
					entrega = "";//AAP08
				}
			}
			
			if (entrega.length()>0) {
				/* se busca configuracion */
				String nuevaSucursal = suc.obtieneConfigSucursal(con, "BOK", entrega, branchId);// AAP02

				/*
				 * Si encontr? configuraci?n, asigna nueva sucursal. En caso de no
				 * encontrar configuracion, no realiza ninguna modificacion a la
				 * sucursal. se deja la sucursal obtenida desde un principio y
				 * realiza las validaciones correspondientes.
				 */
				if (nuevaSucursal.length() > 0) {
					if (entrega.equals("DEST_OCURRE")) {//AAP07
						/*valida sucursal de entrega ocurre de cliente por excepcion*///AAP07
						String nuevaSucursalOcu = suc.obtieneConfigSucursalOcurre(con, aform.getDestinationclave(), aform.getDestinationaddresscode());//AAP07							
						
						if (!nuevaSucursalOcu.equals("")){//AAP07
							/*si coinciden los sites de la sucursa de excepcion y la sucursal destino original, 
							 * se asigna la nueva sucursal excepcion de ocurre*///AAP07
							if (nuevaSucursalOcu.substring(0,2).equals(nuevaSucursal.substring(0,2))) {//AAP07
								nuevaSucursal = nuevaSucursalOcu;//AAP07
							}//AAP07
						}//AAP07
					}//AAP07
					aform.setDestinationcode(nuevaSucursal);
					aform.setDestinationbranch(getDescrBranch(con,nuevaSucursal));//AAP08
				}	
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("checkBranch()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rset, pst, cst);
		}
	}
   
   /****************************************************************************
    * Metodo para obtener bandera de zona extendida y costo. 				   *
    ****************************************************************************/
   private String validaZonaExtendida(Connection con, JavWebBookingGeneralMainForm aform, Global global){
		PreparedStatement pst = null;
		ResultSet rs = null;
		String zonaExt ="N";
		
		try {//select fun_get_ze('81130', 'CENTRO') FROM DUAL			
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("validaZonaExtendida() codigo postal destino ").append(aform.getDestinationzipcode()).toString());
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("validaZonaExtendida() colonia destino ").append(aform.getDestinationcolonia1()).toString());
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("validaZonaExtendida() codigo plaza destino ").append(aform.getDestinationsitecode()).toString());
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("validaZonaExtendida() aform.getOrgienclave() ").append(aform.getOrgienclave()).toString());
			
			pst = con.prepareStatement("select fun_get_ze_gety_code(?, ?, ?, ?) FROM DUAL");
			
			pst.setString(1,aform.getDestinationzipcode());
			//pst.setString(2,aform.getDestinationcolonia1());
			pst.setString(2,aform.getDestinationcoloniacode());
			pst.setString(3,aform.getDestinationsitecode());
			//pst.setString(4,aform.getOrgienclave());
			pst.setString(4,global.getClientIdAgreement());
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				zonaExt=rs.getString(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validaZonaExtendida()_Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.closeResources(rs, pst);
		}
		return zonaExt.trim();
	}
   /*************************************************************************************************************
    * Metodo para obtener la informacion del operador logistico para ser almacenada en la generacion de la guia *
    *************************************************************************************************************///AAP03
   @SuppressWarnings({ "rawtypes", "unchecked" })
private ArrayList obtieneInfOperadorLogistico(Connection con, JavWebBookingGeneralMainForm serForm) {

		PreparedStatement pst = null;
		ResultSet rs = null;		
		ArrayList datos = new ArrayList(2);
		
		try {
			//pst = con.prepareStatement("SELECT OM_OL_FLAG, OM_OLREFRUTA, OM_OL_ID, OM_MAX_VAL_DECL FROM SYS_OL_MSTR WHERE OM_OL_ID = ?");
			pst = con.prepareStatement("SELECT OM_OL_FLAG, OM_OLREFRUTA, OM_OL_ID, case when nvl(PM_VLUE1_ID,0) < OM_MAX_VAL_DECL then OM_MAX_VAL_DECL else PM_VLUE1_ID end OM_MAX_VAL_DECL FROM SYS_OL_MSTR LEFT JOIN SYS_PARM_MSTR ON PM_MDUL_ID = ? AND PM_PARM_TYPE = ? AND PM_PARM_CODE1 = ? WHERE OM_OL_ID = ?");
			
			pst.setString(1, "WEB");
			pst.setString(2, "OM_MAX_VAL_DECL");
			pst.setString(3, serForm.getOrgienclave());
			pst.setString(4, serForm.getOperadorLogistico());
			rs = pst.executeQuery();
			
			if (rs.next()) {
				// bandera operador logistico
				if (rs.getString(1) == null) {
					datos.add("");
				} else {
					datos.add(rs.getString(1));
				}
				// ruta de operador logistico
				if (rs.getString(2) == null) {
					datos.add("");
				} else {
					datos.add(rs.getString(2));
				}

				// ID operador logistico
				if (rs.getString(3) == null) {
					datos.add("");
				} else {
					datos.add(rs.getString(3));
				}

				// valor maximo a declarar por operador logistico
				if (rs.getString(4) == null) {
					datos.add("");
				} else {
					datos.add(rs.getString(4));
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("obtieneInfOperadorLogistico()_Error:").append(e).toString());
			e.printStackTrace();
		} finally{
			resources.closeResources(rs, pst);
		}
		return datos;
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
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getLcZone()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return datos;
	}   
   /*************************************************************************************************************
    * Metodo para obtener la informacion del operador logistico para ser almacenada en la generacion de la guia *
    *************************************************************************************************************///AAP03
   private boolean obtieneInfRequerimientosACK(Connection con, String groupClientId, String tipo_acuse) {

		PreparedStatement pst = null;
		ResultSet rs = null;		
		boolean exist = false;
		
		try {
			pst = con.prepareStatement("SELECT CR_CLNT_ID FROM SYS_CLNT_SRVC_REQM_MSTR WHERE CR_CLNT_ID = ? AND CR_SRVC_ID = ? AND CR_REFR_SRVC_ID = ?");
			pst.setString(1, groupClientId);
			pst.setString(2, tipo_acuse);
			pst.setString(3, "ACK");
			rs = pst.executeQuery();
			
			if (rs.next()) {
				exist = true;
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("obtieneInfRequerimientosACK()_Error:").append(e).toString());
			e.printStackTrace();
		} finally{
			resources.closeResources(rs, pst);
		}
		return exist;
	}
   
   @SuppressWarnings("rawtypes")
   private String isSoloSobre(HttpSession session) {//AAP03
		String returnString = "N";
		
		try {
           ArrayList servicesDetailArray = (ArrayList)session.getAttribute("servicesDetail");

           boolean isEnvelopealone = true;
			for (int i=0;i<servicesDetailArray.size();i++) {
				ShipmentServiceDetail ssd = (ShipmentServiceDetail)servicesDetailArray.get(i);
				if (!ssd.serviceId.equalsIgnoreCase("SHP-E")) {
					isEnvelopealone = false;
					break;
				}
			}
			
			if (isEnvelopealone) {
				returnString = "Y";				
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("isSoloSobre()Error:").append(e).toString());
			e.printStackTrace();
		}
		return returnString;
	}   
   
   @SuppressWarnings("rawtypes")
   private String hasEnvelope(HttpSession session) {//AAP03
		String returnString = "N";
		
		try {
           ArrayList servicesDetailArray = (ArrayList)session.getAttribute("servicesDetail");

           boolean hasEnvelope = false;
			for (int i=0;i<servicesDetailArray.size();i++) {
				ShipmentServiceDetail ssd = (ShipmentServiceDetail)servicesDetailArray.get(i);
				if (ssd.serviceId.equalsIgnoreCase("SHP-E")) {
					hasEnvelope = true;
					break;
				}
			}
			
			if (hasEnvelope) {
				returnString = "Y";				
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("hasEnvelope()Error:").append(e).toString());
			e.printStackTrace();
		}
		return returnString;
	}
   
  /* @SuppressWarnings({ "rawtypes", "unchecked" })
   private ArrayList getInputEmail(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		CallableStatement cst = null;
		ArrayList datos = new ArrayList(2);
		try {
			//pro_val_input_email(CLNT_ORGN_ID VARCHAR2, CLNT_DEST_ID VARCHAR2, EMAIL_ORGN OUT VARCHAR2, EMAIL_DEST OUT VARCHAR2)
			String query = "{ call pack_web.pro_val_input_email(?, ?, ?, ?) }";
			cst = con.prepareCall(query);
			//cst.setString(1, aform.getOrgienclave());
			cst.setString(1, global.getClientIdAgreement());
			cst.setString(2, aform.getDestinationclave());
			cst.registerOutParameter(3, Types.VARCHAR);
			cst.registerOutParameter(4, Types.VARCHAR);
			cst.executeQuery();
			
			datos.add(cst.getString(3) == null ? "" : cst.getString(3));// envio email cliente origen
			datos.add(cst.getString(4) == null ? "" : cst.getString(4));// envio email cliente destino
			
			//System.out.println("datos "+datos);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getInputEmail()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return datos;
	}*/
   
   private String verificaSoloSobres(JavWebBookingGeneralMainForm aform, Connection con) {//AAP08		
		PreparedStatement pst = null;
		ResultSet rs = null;		
		CallableStatement cst = null;
		String sucDestSobre = "";
		try {
			if (aform.getDestinationcode() != null && !aform.getDestinationcode().isEmpty() &&
					!aform.getDestinationcode().substring(3).equals("70")) {				
				if (aform.getEntrega().equals("2")) {
					// INICIO ==>//AAP08
					
					cst = con.prepareCall("{call pack_web.PRO_ASSIGN_DEST_BRNC(?,?,?,?,?,?)}");
					cst.setString(1, aform.getGetycode());
					cst.setString(2, aform.getGetytype());
					cst.setString(3, aform.getGetylevl());
					cst.registerOutParameter(4, Types.VARCHAR);
					cst.registerOutParameter(5, Types.VARCHAR);
					cst.setString(6, aform.getDestinationsitecode());
					cst.executeQuery();
					String br = cst.getString(4);
					String brname = cst.getString(5);					
					
					aform.setDestinationcode(br);
					aform.setDestinationbranch(brname);	
					aform.setDestinationcodeIni(br);
					aform.setDestinationbranchIni(brname);
					// FIN ==>//AAP08			
					if (aform.getIsSoloSobre().equals("Y")) {
						SucursalesConfiguradas suc = new SucursalesConfiguradas();//AAP08
						sucDestSobre = suc.obtieneConfigSucursal(con, "BOK", "DEST_SOBRES", aform.getDestinationsitecode());//AAP08
						
						if (sucDestSobre.length()>0) {
							aform.setDestinationcode(sucDestSobre);
							aform.setDestinationbranch(getDescrBranch(con,sucDestSobre));
						}
					} 
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("verificaSoloSobres()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
			resources.cerrarCallableStatement(cst);
		}
		return sucDestSobre;
	}
   /*obtiene clave y descripcion de sucursales de guadalajara*/
	private String getDescrBranch(Connection con, String branch){
		PreparedStatement pst = null;
		ResultSet rset = null;
		String brname = "";
		try {
			pst = con.prepareStatement("select NVL(BM_BRNC_NAME,'') from sys_brnc_mstr where BM_BRNC_ID=?");
			pst.setString(1, branch);			
			rset = pst.executeQuery();
			if (rset.next()) {
				brname = rset.getString(1);							
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDescrBranch()Error_1:").append(e).toString());
			e.printStackTrace();
		}finally{
			try {
				if (rset != null)
					rset.close();
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDescrBranch()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return brname;
	}
	/*valida sucursal virtual*///AAP09
	public String valBranchVrtl(Connection con, String destBrnch) {//AAP09
		String result = "0";
		PreparedStatement pstmt =  null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement("SELECT BM_FLAG2 FROM SYS_BRNC_MSTR WHERE BM_BRNC_ID = ?");
			pstmt.setString(1, destBrnch);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				result = rs.getString(1) == null ? "0" : rs.getString(1);
			}
			//System.out.println("sucursal virtual "+result);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("valBranchVrtl()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				resources.closeResources(rs, pstmt);				
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("valBranchVrtl()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/****************************************************************************
	* Metodo para obtener bandera de validacion de referencia de cliente        *
	****************************************************************************/
	private String getFlagValidRefrClnt(Connection con, Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String flagRefer ="0";
		
		try {			
			
			pst = con.prepareStatement("select NVL(FUN_ISREQ_REFER(?),'0') FROM DUAL");
			
			pst.setString(1, global.getClientIdAgreement());
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				flagRefer=rs.getString(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getFlagValidRefrClnt()_Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.closeResources(rs, pst);
		}
		return flagRefer.trim();
	}
	
	/****************************************************************************
	* Metodo para validacion de referencia de cliente        					*
	****************************************************************************/
	private HashMap<String, String> validRefrClnt(Connection con, JavWebBookingGeneralMainForm aform, Global global) {
		CallableStatement cst = null;
		String msgeRefr = "OK";
		String valResponse = "1";
		int posicion = 0;
		HashMap<String, String> valuesReturn = new HashMap<>(2);
		try {
			posicion = getTotalReferences(aform)+1;
			
			String query = "Begin ? := FUN_VALID_REFER(?, ?, ?, ?); End;";			
			
			cst = con.prepareCall(query);

			cst.registerOutParameter(1, Types.VARCHAR);
			cst.setString(2, global.getClientIdAgreement());
			cst.setString(3, aform.getReference());
			cst.setInt(4, posicion);
			cst.registerOutParameter(5, Types.VARCHAR);
			
			cst.executeQuery();
			
			valResponse = cst.getString(1) == null ? "" : cst.getString(1);
			msgeRefr = cst.getString(5) == null ? "" : cst.getString(5);
			
			valuesReturn.put("valResponse", valResponse);
			valuesReturn.put("msgeRefr", msgeRefr);
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validRefrClnt()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return valuesReturn;
	}
	
	private int getTotalReferences(JavWebBookingGeneralMainForm aform) {
		int length = 0;
		String[] listRefers = null;
		try {
			if (aform.getListReferences().trim().length()>0) {
				listRefers = aform.getListReferences().split("\\|");
				length = listRefers.length;				
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTotalReferences()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return length;
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
			//System.out.println("clave colonia: "+codColo);
			rst = pstmt.executeQuery();
			if(rst.next()) {
				String valor = rst.getString(1);
				iscobertura=valor.equalsIgnoreCase("1");
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getCoverageSEG()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rst != null) {
					rst.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getCoverageSEG()_Error2:").append(e).toString());
				e.printStackTrace();
			}
			
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getCoverageSEG()_Error4:").append(e).toString());
				e.printStackTrace();
			}
			
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getCoverageSEG()_Error5:").append(e).toString());
				e.printStackTrace();
			}
		}
		return iscobertura;
	}
	
	/* Valida el pedimento capturado */
	private String validaPedimento(Connection con, String pedimento) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String validPedimento = "";
		try {
			pst = con.prepareStatement("SELECT FUN_VALID_PEDI_NUMB(?) FROM DUAL");
			
			pst.setString(1, pedimento);
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
	