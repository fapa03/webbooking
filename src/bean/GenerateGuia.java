/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creación: 
 * Compañía: PAQUETEXPRESS.
 * Descripción del programa: Bean para generacion de guia.
 * FileName: GenerateGuia.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP04
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 01/07/2013
 * Descripción: Modificaciones para servicios de flete por cobrar.
 * Cambiar el cliente y sucursal de facturacion al destino.
 * Cambiar tipo de pago segun lo seleccionado 
 * fxc. tipo pago = N. 
 * pagada. tipo pago = C.
 * ------------------------------------------------------------------
 * Clave: AAP09
 * Autor: ABRAHAM ARJONA
 * Fecha: 26/12/2014
 * Descripción: Modificaciones para nuevas reglas de SOBRE (cobro de 140 pesos fijo antes de descuentos).
 * 
 * ------------------------------------------------------------------ 
 * Clave: AAP10
 * Autor: ABRAHAM ARJONA
 * Fecha: 17/02/2015
 * Descripción: Se agregó lógica para el cálculo de los descuentos para tarifa A y C
 * Para tarifa A y C obtiene el costo base para registrarlo en bok_Guia_srvc_item
 * Para tarifa C obtiene el tipo de tarifa (ya calculada desde el agregado de los servicios), para registrarla (T1, T2, T3 .. T7)
 * aplica solo para tarifa C con cálculo por kilo y/o volumen.
 * ------------------------------------------------------------------
 * Clave: AAP11
 * Autor: ABRAHAM ARJONA
 * Fecha: 20/02/2015
 * Descripción: Se agregó validación para cuando marque error de inserción en bok_guia_srvc_item por duplicidad de registros.
 * No permite continuar con el proceso haciendo un rollback de la transacción y marca en pantalla el error:
 * "Existe un problema con los servicios de la guía en sesion. Favor de Terminar SESION, limpiar Historial de exploración en el navegador, cerrar la página y cargarla nuevamente."
 * ------------------------------------------------------------------ 
 * Clave: AAP12
 * Autor: ABRAHAM ARJONA
 * Fecha: 23/06/2015
 * Descripción: Se eliminó lógica para omitir cobro de seguro. Ahora se bloquea captura de monto en valor declarado
 * ------------------------------------------------------------------ 
 * Clave: AAP13
 * Autor: ABRAHAM ARJONA
 * Fecha: 08/12/2018
 * Descripción: Busca diferencia de centavo entre sumatoria servicios y total de bok_Guia_head para ajustar importe.  
 * ------------------------------------------------------------------ 
 */
package bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.paquetexpress.www.webbooking.Documentacion.ShipTypeSEG;

import beanForm.JavWebBookingGeneralMainForm;
import beanUtil.ConnectDB;
import logger.AccessLog;
import mx.com.paquetexpress.dto.ParamDTO;
import mx.com.paquetexpress.general.model.dto.BranchDetailDTOResponse;
import paquetexpress.internal.common.JavDeliveryHours;

public class GenerateGuia {
	private StringBuffer cnct = new StringBuffer();
	private final String msgAvi = new StringBuffer("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	
	@SuppressWarnings("rawtypes")
	public boolean generateGuia(Connection con,
			JavWebBookingGeneralMainForm aform, HttpServletRequest req) {

		HttpSession session = req.getSession(false);
		Global global = (Global) session.getAttribute("sGlobal");
		//String clientId = aform.getOrgienclave();
		String formNumber = "";
		String modulo = "WEB";
		String serieCaja = "WW";
		boolean continuar = true;
		SucursalesConfiguradas suc = new SucursalesConfiguradas();//AAP02
		try {
			formNumber = getFormNumber(con, global, modulo, serieCaja);
			
			if(formNumber !=null && formNumber.length()>0) {
				aform.setGuiano(formNumber);
			}
	        //AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("generateGuia()_").append(clientId).append(" %%%%%%%%%%% FORM NUMBER ").append(global.getAssignedBranch()).append(formNumber).toString());
			
			if (validaGH_FORM_NO(con, aform)) {
				aform.setDuplicateguianumber("EL NUMERO DE FORMA YA EXISTE");
				continuar = false;
			} else {
				String genGuiaNumber = getNumRastreo(con, aform);			

				ServicesTotal servicesTotal = (ServicesTotal) session.getAttribute("servicestotal");
				
				// coded by B.Emerson on 19/07/2003
				Timestamp sysDate = getTimeStamp(con);
				String branchId = aform.getDestinationsitecode();
				//System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&branchId "+branchId);
				//System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&aform.getDestinationcode() "+aform.getDestinationcode());
				String entriga = aform.getEntrega();
				
				String entrega = "";//AAP02
				/*se asigna el tipo de búsqueda de configuracion en base al tipo de entrega*/
				if (entriga.equalsIgnoreCase("1")) {//AAP02
					entrega = "DEST_OCURRE";
				} else {
					if (!aform.getDestinationcode().substring(3).equals("70")) {//AAP06
						//System.out.println("entro a asignar busqueda de surcursal ead "+aform.getDestinationcode().substring(3));
						//entrega = "DEST_EAD_YA_NO";//AAP08
						entrega = "";//AAP08
						aform.setCur_dest(aform.getDestinationcode());
						global.setDestinationBranchId(aform.getDestinationcode());
						session.setAttribute("sGlobal", global);
						
						if (aform.getIsSoloSobre().equals("Y")) {//AAP08
							entrega = "DEST_SOBRES";
						}
					}
				}
				
				if (entrega.length()>0) {
					/*se busca configuracion*/				
					String nuevaSucursal = suc.obtieneConfigSucursal(con, "BOK", entrega, branchId);//AAP02
					
					/*en caso de no encontrar configuracion, no realiza ninguna modificacion a la sucursal.
					 * se deja la sucursal obtenida desde un principio*/
					if (nuevaSucursal.length()>0) {
						if (entrega.equals("DEST_OCURRE")) {//AAP07
							/*valida sucursal de entrega ocurre de cliente por excepcion*///AAP07
							String nuevaSucursalOcu = suc.obtieneConfigSucursalOcurre(con, aform.getDestinationclave(), aform.getDestinationaddresscode());//AAP07
							String nuevaSucursalEspec = suc.obtieneConfigSucursalEspec(con, "BOK", "DEST_OCURRE_ESP", branchId);
							//System.out.println("Ocurre: " +nuevaSucursal);
							/* El cliente eligió sucursal ocurre */
							String[] brnchClave = aform.getBrnchOcurre().split("\\|");
							if (aform.getOpcOcurre() && ! brnchClave[0].equals("")) {
								nuevaSucursal = brnchClave[0];
							}else if (!nuevaSucursalOcu.equals("")){//AAP07
								/*si coinciden los sites de la sucursa de excepcion y la sucursal destino original, 
								 * se asigna la nueva sucursal excepcion de ocurre*///AAP07
								/* Valida si la sucursal por excepción cumple con los topes */
								for (BranchDetailDTOResponse s : aform.getFilteredBrnch()) {
									if (s.getClave().equalsIgnoreCase(nuevaSucursalOcu)) {
										nuevaSucursal = nuevaSucursalOcu;
									}
								}
								if (!nuevaSucursal.equalsIgnoreCase(nuevaSucursalOcu)) {
									if (nuevaSucursalEspec.equalsIgnoreCase(nuevaSucursalOcu)) {
										/* Asigna sucursal especial */
										nuevaSucursal = nuevaSucursalEspec;
										}
								}
							}
								/*//Aqui va el código a comprar con la lsita que traigo
								else if (nuevaSucursalOcu.substring(0,2).equals(nuevaSucursal.substring(0,2))) {//AAP07
									nuevaSucursal = nuevaSucursalOcu;//AAP07 //AAP07*/
							//AAP07
						}//AAP07
						
						aform.setDestinationcode(nuevaSucursal);
						aform.setCur_dest(nuevaSucursal);
						global.setDestinationBranchId(aform.getDestinationcode());
						session.setAttribute("sGlobal", global);
					}
				}
				//AAP02
				//se comentaron lineas porque ahora se realiza este proceso con configuracion
//				if (branchId.equalsIgnoreCase("GDL")) {
//					if (entriga.equalsIgnoreCase("1")) {
//						aform.setDestinationcode("GDL01");
//						aform.setCur_dest("GDL01");
//					} else {
//						aform.setDestinationcode("GDL02");
//						aform.setCur_dest("GDL02");
//					}
//					global.destinationBranchId = aform.getDestinationcode();
//					//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("generateGuia()_").append(clientId).append(" The destination code is ").append(global.destinationBranchId).toString());
//					session.setAttribute("sGlobal", global);
//				}
				//AAP02
				
				// ConID
				ArrayList infContact = getInfContacto(con, aform);
				
				String defaultEadRout = null;
				if (aform.getEntrega().equals("2")) {
					defaultEadRout = getDefaultEadRout(con, aform);
				}

				String invoiceFlag = "N";
				if (getInvoiceFlag(con, global)) {
					invoiceFlag = ((aform.getFiscaladdresscode() != null && global.rfc != null) ? "Y" : "N");
				}

				String comText = "";
				if ((aform.getFiscaladdresscode() == null || aform.getFiscaladdresscode().length() == 0) || global.rfc == null) {
					comText = cnct.delete(0,cnct.length()).append(global.commentText).append("No Proporciono RFC, No sustitucion de factura;").append(aform.getComments().trim().toUpperCase()).toString();
					if (aform.getReference() != null && aform.getReference().length() > 0) {
						comText = cnct.delete(0,cnct.length()).append(comText).append(":").append(aform.getReference().trim().toUpperCase()).toString();
					}					
				} else {
					comText = cnct.delete(0,cnct.length()).append(global.commentText).append(aform.getComments().trim().toUpperCase()).toString();
					if (aform.getReference() != null && aform.getReference().length() > 0) {
						comText = cnct.delete(0,cnct.length()).append(comText).append(":").append(aform.getReference().trim().toUpperCase()).toString();
					}
				}
				
				if (aform.getCheckRefDir().equals("V")) {
					comText = cnct.delete(0,cnct.length()).append(comText).append(":").append(aform.getDestinationRefDom().trim().toUpperCase())
							.append(". Telefono: ").append(aform.getDestinationtelefono()).toString();	
				}
				
				/*se limita el contenido de comTex para que no sobre pase la capacidad del campo*/
				if (comText.length() > 255) {
					comText = comText.substring(0,255);
				}
				//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("generateGuia()_comText: ").append(comText).toString());
				if ((global.destinationBranchId == null) || (global.destinationBranchId.length() <= 0) || session.getAttribute("sClientId") == null) {
					req.setAttribute("errormsgstatus", "Existe un problema de sesion. Favor de cerrar la pagina y cargarla nuevamente");
					//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("generateGuia()_").append(clientId).append("There is a problem in SESSION").toString());
					continuar = false;
				} else {
					ArrayList servicesDetailArray = (ArrayList) session.getAttribute("servicesDetail");
					String bandOperLog = "";
					String idOperLog = "";
					ArrayList datos = null;
					
					/*
					 * verifica si es zona extendida para obtener los datos del operador
					 * logistico
					 */
					if ((aform.getZonaExtendida().substring(0, 1).equals("Y") && aform.getDestinationcode().contains("70")) || aform.getOperadorLogistico().trim().length()>0) {
						datos = obtieneInfOperadorLogistico(con, aform);
						bandOperLog = datos.get(0).toString();
						defaultEadRout = datos.get(1).toString();
						idOperLog = datos.get(2).toString();
					}
					
					Map<String, String> mapsClntGeneric = null;
//					Rfc r = new Rfc();
//					boolean rfcGenerico = r.isRfcGenerico(aform.getDestinationrfc());
//					if (aform.getFormaPago().equals("TO_PAY")  && rfcGenerico) {// JSA02
//						mapsClntGeneric = getAddrCodeCustomerGenericFXC(con);
//					}
					String companyHead = null;
					if (aform.getFormaPago().equals("TO_PAY")) {
						companyHead = getCompanyHead(con,  global.getGroupClientIdDestino());
					} else {
						companyHead = getCompanyHead(con,  global.getGroupClientId());
					}
					/*inserta bok_guia_head*/
					insertBookGuiaHead(con, aform, global, genGuiaNumber, servicesTotal, sysDate, infContact, invoiceFlag, defaultEadRout, bandOperLog, comText, mapsClntGeneric, companyHead);					
					
					if (aform.getPedinumber().isEmpty() && !aform.getLastWrongPediNum().isEmpty()) {
						insertBokHistErrorPediNumb(con, aform.getLastWrongPediNum(), genGuiaNumber);
					}
					
					// Addres Insertion
					insertBookGuiaAddress(con, aform, genGuiaNumber, mapsClntGeneric);

					// Service Item Insertion
					//insertBookGuiaServiceItem(con, aform, genGuiaNumber, sysDate, servicesDetailArray);//AAP05
					continuar = insertBookGuiaServiceItem(con, aform, genGuiaNumber, sysDate, servicesDetailArray, global);
					
					/*validacion si la insercion de bok_Guia_srvc_item se completó*///AAP11
					if (continuar) {//AAP11
						HashMap <String, ParamDTO> list = getConfigItemFacturaBycompany(con, companyHead);
						// Services Insertion with Service Item
						insertBookGuiaServices(con, session, genGuiaNumber, sysDate, aform, req, idOperLog, servicesTotal, list, companyHead);

						ParamDTO pa =getBillCustomerInvoice(con, companyHead);
						insertBokInvcHeadRinv(con, session, sysDate, genGuiaNumber, pa);
						insertBookGuiaStatus(con, session, aform, genGuiaNumber);

						// refernce insertion coded by B.Emerson on 24/07/2003. The insertion
						// will take place only when there is value for the reference field
//						if (aform.getReference() != null && aform.getReference().length() > 0) {
//							insertReferenceRecord(con, session, genGuiaNumber, aform .getReference().trim().toUpperCase(), sysDate);
//						}
						String mainRefr = "";
						int count = 1;
						if (aform.getListReferences() != null && aform.getListReferences().length() > 0) {
							StringTokenizer st = null;
							st = new StringTokenizer(aform .getListReferences().trim().toUpperCase(),"|");
							
							while (st.hasMoreElements()) {
								if (count==1) {
									mainRefr = "Y";
								} else {
									mainRefr = "N";
								}
								insertReferenceRecord(con, session, genGuiaNumber, st.nextToken(), sysDate, mainRefr);
								count++;
							}
						} else {
							
							if (aform.getReference() != null && aform.getReference().length() > 0) {
								insertReferenceRecord(con, session, genGuiaNumber, aform .getReference().trim().toUpperCase(), sysDate, "Y");
							}
						}
						
						boolean insrtEmail = false;
						if (aform.geteMailOrigText().length() > 0 || aform.geteMailDestText().length()>0) {//AAP05
							insrtEmail = true;						
						} else if (aform.getSendeMailDestBD().equals("Y") && !aform.geteMailDestBD().equals("")) {//AAP05
							aform.seteMailDestText(aform.geteMailDestBD());//AAP05
							insrtEmail = true;//AAP05
						}//AAP05
						
						if (insrtEmail) {//AAP05
							insertWebCntrlMail(con, global, aform, genGuiaNumber);//AAP05
						}//AAP05
												
						/*inserta guia y centro de costo seleccionado*/
						insertBookGuiaExtra(con, aform, genGuiaNumber, global);//AAP05
						
						/*Busca diferencia de centavo entre sumatoria servicios y total de bok_Guia_head para ajustar importe.*///AAP13
						updateBokGuiaHeadRound(con, genGuiaNumber);//AAP13
						
						//Valido si la guia tiene acuse XT
						String rastreo_XT ="";
						//System.out.println("Clase: GenerateGuia\nLinea: 341\nAcuse: "+aform.getAcusederecibo());
						AcusesXT xt = new AcusesXT();
						//System.out.println("ENTRO POR WEBBOOKING... " + aform.getAcusederecibo());
						if(aform.getAcusederecibo().equals("ACK-X")) {
							global.setAcuseXT(true);
							rastreo_XT = xt.genAcuseXT(con, genGuiaNumber);
						}else{
							global.setAcuseXT(false);
						}
						/*insercion de etiquetas en tabla para generacion de PDF*/
						insertCtrlLabelPrn(con, genGuiaNumber);
						/*obtiene cadena de impresion con funcion genérica*/
						String cadena = getCadenaImpresion(con, req, global, genGuiaNumber,rastreo_XT);
						//Si la guia tenia acuses XT hay que desactivar el rastreo
						if(aform.getAcusederecibo().equals("ACK-X")) {
							//Desactivo el rastreo de retorno
							xt.desactivarGuiaDeRetorno(con, genGuiaNumber);
							//Generamos los registros para la impresión del PDF
							//System.out.println("Se genero PDF de retorno: " +rastreo_XT);
							xt.insertForPDFXT(con, rastreo_XT);
						}
						
						/*actualiza centro de costo default para usuario de sesion en login*/
						updateCentroCostoDefault(con, aform);
						
						String getCartaPorte = ConnectDB.getCartaPorteExt();
						if (req.getRemoteAddr().trim().substring(0,3).equals("0:0") || req.getRemoteAddr().trim().substring(0,8).equals("192.168.")||req.getRemoteAddr().trim().substring(0,7).equals("172.16.")) {
							getCartaPorte =ConnectDB.getCartaPorteInt();
						}
						
						if(aform.getAcusederecibo().equals("ACK-X")) {
							req.setAttribute("trackingNoGenRet", global.getTrackingNoGenRet());
						}
						else {
							req.setAttribute("trackingNoGenRet", "");
						}
						//req.setAttribute("trackingNoGenRet", global.getTrackingNoGenRet());
						req.setAttribute("trackingNoGen", genGuiaNumber);
						req.setAttribute("getCartaPorte", getCartaPorte);
						req.setAttribute("paquetes", servicesTotal.totalPack);
						req.setAttribute("cadena", cadena);
					} else {//AAP11
						req.setAttribute("errormsgstatus", "Existe un problema con los servicios de la guía en sesion. Favor de Terminar SESION, limpiar Historial de exploración en el navegador, cerrar la página y cargarla nuevamente.");//AAP11
						//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("generateGuia()_").append(clientId).append("There is a problem in SESSION").toString());						
					}//AAP11				
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("generateGuia()_Error:").append(e).toString());
			e.printStackTrace();
		}
		return continuar;
	}
	
	/****************************************************************
	 * Metodo para validar si el numero de forma (guia) ya existe	*
	 ****************************************************************/
	private boolean validaGH_FORM_NO(Connection con, JavWebBookingGeneralMainForm serForm) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean existe = false;
		try {
			String checkFormQuery = "select 1 from bok_guia_head where GH_FORM_NO = ? and GH_PREP_BRNC_ID = ? and GH_DOCU_TYPE = ?";
			pst = con.prepareStatement(checkFormQuery);
			pst.setString(1, serForm.getGuiano());
			pst.setString(2, serForm.getOrginbranchcode());
			pst.setString(3, "Q");
			rs = pst.executeQuery();
			if (rs.next()) {
				existe = true;
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validaGH_FORM_NO()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return existe;
	}
	/****************************************************************
	 * Metodo para generar numero de rastreo						*
	 ****************************************************************/
	private String getNumRastreo(Connection con, JavWebBookingGeneralMainForm serForm) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String genGuiaNumber = null;
		try {			
			String guiaQuery = "select pack_web.FUN_GEN_GUIA_NO(?) from dual";
			pst = con.prepareStatement(guiaQuery);
			pst.setString(1, serForm.getOrginbranchcode());
			rs = pst.executeQuery();
			
			if (rs.next()) {
				genGuiaNumber = rs.getString(1);
			}

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getNumRastreo()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return genGuiaNumber;
	}
	
	/****************************************************************
	 * Metodo para obtener TimeStamp								*
	 ****************************************************************/
	private Timestamp getTimeStamp(Connection con) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Timestamp sysTimeStamp =null;
		try {
			pst = con.prepareStatement("select sysdate from dual");
			rs = pst.executeQuery();
			if(rs.next()) {
				sysTimeStamp=rs.getTimestamp(1);
			}
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("getTimeStamp()_").append("SYSDATE WITH TIME STAMP IN SERVICES ACTION ").append(sysTimeStamp).toString());			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTimeStamp()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return sysTimeStamp;
	}
	
	/****************************************************************
	 * Metodo para obtener informacion de contacto					*
	 ****************************************************************/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ArrayList getInfContacto(Connection con, JavWebBookingGeneralMainForm aform) {
		CallableStatement cst = null;
		ArrayList infContact = new ArrayList(4);
		try {
			// ConID
			String conIdQuery = "{ call pack_web.pro_defa_cont(?,?,?,?,?,?) }";
			cst = con.prepareCall(conIdQuery);
			cst.setString(1, aform.getOrgionaddresscode());
			cst.setString(2, aform.getOrgienclave());
			cst.registerOutParameter(3, Types.VARCHAR);
			cst.registerOutParameter(4, Types.VARCHAR);
			cst.registerOutParameter(5, Types.VARCHAR);
			cst.registerOutParameter(6, Types.VARCHAR);
			
			cst.executeQuery();
			
			infContact.add(cst.getString(3) == null ? "" : cst.getString(3));//cont_id
			infContact.add(cst.getString(4) == null ? "" : cst.getString(4));//iden_type
			infContact.add(cst.getString(5) == null ? "" : cst.getString(5));//iden_no
			infContact.add(cst.getString(6) == null ? "" : cst.getString(6));//cont_name
			
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("getInfContacto()_").append(aform.getOrgienclave()).append("&&&&&&&&&&&&&&&&&&&&&&& CONTACT INFORMATION ").append(infContact.get(0)).toString());
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("getInfContacto()_").append(aform.getOrgienclave()).append("&&&&&&&&&&&&&&&&&&&&&&& CONTACT INFORMATION IDEN_TYPE ").append(infContact.get(1)).toString());
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("getInfContacto()_").append(aform.getOrgienclave()).append("&&&&&&&&&&&&&&&&&&&&&&& CONTACT INFORMATION IDEN_NO ").append(infContact.get(2)).toString());
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("getInfContacto()_").append(aform.getOrgienclave()).append("&&&&&&&&&&&&&&&&&&&&&&& CONTACT INFORMATION CONT_NAME ").append(infContact.get(3)).toString());
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getInfContacto()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return infContact;
	}
	
	/****************************************************************** 
	 * Metodo para obtener valor de campo CM_SLMN_ID de SYS_CLNT_MSTR *
	 ******************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList getSLMN_ID(Connection con, JavWebBookingGeneralMainForm aform) {
		PreparedStatement pst = null;
		ResultSet rs = null;		
		ArrayList slmnIdOrigDest = new ArrayList(2);
		try {			
			String slmnQuery = "select pack_web.fun_slmn_id(?) from dual";
			pst = con.prepareStatement(slmnQuery);
			
			for (int i = 1; i <= 2; i++) {
				if (i == 1) {
					pst.setString(1, aform.getOrgienclave());
				} else if (i == 2) {
					pst.setString(1, aform.getDestinationclave());
				}
				rs = pst.executeQuery();
				if (rs.next()) {
					if (i == 1) {
						//orginSlmnId = rs.getString(1);
						slmnIdOrigDest.add(rs.getString(1) == null ? "" : rs.getString(1));
					} else if (i == 2) {
						//destSlmnId = rs.getString(1);
						slmnIdOrigDest.add(rs.getString(1) == null ? "" : rs.getString(1));
					}
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getSLMN_ID()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return slmnIdOrigDest;
	}
	
	/****************************************************************
	 * Metodo para obtener Ruta default de entrega a domicilio		*
	 ****************************************************************/
	private String getDefaultEadRout(Connection con, JavWebBookingGeneralMainForm aform) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String rout = "";
		
		try {			
			pst = con.prepareStatement("select pack_web.fun_ftch_ead_rout(?,?) from dual");
			pst.setString(1,aform.getDestinationcode());
			pst.setString(2,aform.getDestinationcoloniacode());
			
			rs = pst.executeQuery();
			
			if(rs.next()){
				rout = rs.getString(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDefaultEadRout()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return rout;
	}
	
	private boolean getInvoiceFlag(Connection con,Global global) {
		CallableStatement cst = null;
		boolean isTrue=false;
		try {
			cst = con.prepareCall("Begin ? := pack_web.fun_chk_inv_req(?); End;");
			cst.registerOutParameter(1,Types.VARCHAR);
			cst.setString(2,global.clientId);
			cst.executeQuery();
			

			String temp = cst.getString(1);
			if(temp!=null && temp.equalsIgnoreCase("Y")) {
				isTrue=true;
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getInvoiceFlag()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return isTrue;
	}
	
   /*************************************************************************************************************
    * Metodo para obtener la informacion del operador logistico para ser almacenada en la generacion de la guia *
    *************************************************************************************************************///AAP01
   @SuppressWarnings({ "rawtypes", "unchecked" })
private ArrayList obtieneInfOperadorLogistico(Connection con, JavWebBookingGeneralMainForm serForm) {

		PreparedStatement pst = null;
		ResultSet rs = null;		
		ArrayList datos = new ArrayList(3);
		
		try {
			pst = con.prepareStatement("SELECT OM_OL_FLAG, OM_OLREFRUTA, OM_OL_ID FROM SYS_OL_MSTR WHERE OM_OL_ID = ?");
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
				
				//ID operador logistico
				if (rs.getString(3)==null){
					datos.add("");
				}else{
					datos.add(rs.getString(3));
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

	@SuppressWarnings("rawtypes")
	private void insertBookGuiaHead(Connection con, JavWebBookingGeneralMainForm aform, Global global, String 
			genGuiaNumber, ServicesTotal servicesTotal, Timestamp sysDate, ArrayList infContact, 
			String invoiceFlag, String defaultEadRout, String bandOperLog, String comText, Map<String, String> mapsClntGeneric, String companyHead) {
		PreparedStatement pst = null;
		try {
			// SLMN Id
			ArrayList slmnIdOrigDest = getSLMN_ID(con, aform);
			String eadFlag = aform.getEntrega().equals("2") ? "1" : "0";	
			String billBranchId = "";//AAP04
			String billClientId = "";//AAP04
			String billClientName = "";//AAP04
			String tipoPago = "C";//AAP04
			
			if (aform.getFormaPago().equals("TO_PAY")) {//AAP04
				billBranchId = global.getDestinationBranchId();//AAP04
				billClientId = aform.getDestinationclave();//AAP04
				billClientName = aform.getDestinationnombre();//AAP04
				if(mapsClntGeneric != null) {
					billClientId = mapsClntGeneric.get("clntID");//JSA02
					billClientName = mapsClntGeneric.get("clntName");//JSA02
				}
				tipoPago = "C";//AAP04
			} else {//AAP04
				billBranchId = global.getAssignedBranch();//AAP04
				billClientId =  global.getClientId();//AAP04
				billClientName = global.getClientName();//AAP04
				tipoPago = "C";//AAP04
				
				String evalChild = "Y";
				
				/*si el cliente origen es distinto al cliente SEDE DE CREDITO, Se valida si SEDE //AAP18
				 * se encuentra configurado para asignarlo directamente en la cobranza de la guia.*///AAP18
				if (!global.getClientId().equals(global.getGroupClientId())) {//AAP18
					HashMap result = getInfoGroupClientId(con, global);//AAP18
					if (!result.isEmpty() && result.get("continuar").toString().equals("Y")){//AAP18
						billBranchId = result.get("billBrncId").toString();//AAP18
						billClientId =  global.getGroupClientId();//AAP18
						billClientName = result.get("groupClientName").toString();//AAP18
						evalChild = "N";
					}//AAP18
				}//AAP18
				
				if (evalChild.equals("Y")) {
					HashMap resultidClient = getInfoClientId(con, global);
					if (!resultidClient.isEmpty() && resultidClient.get("continuar").toString().equals("Y")){
						billBranchId = resultidClient.get("billBrncId").toString();
						billClientId =  global.getClientId();
						billClientName = resultidClient.get("clientName").toString();
					}else {
						billBranchId = getBillBrncId(con, global.getAssignedBranch().substring(0, 3));
						billClientId =  global.getClientId();
						billClientName = global.getClientName();
					}
				}
			}//AAP04

			if (billBranchId.contains("70")) {
				billBranchId = getBillBrncId(con, billBranchId.substring(0, 3));
			}
			
			String curr_local = global.getAssignedBranch();//AAP01
			String curr_dest = global.getDestinationBranchId();//AAP01
			String destBranchId = global.getDestinationBranchId();//AAP01
			
			if (aform.getEntrega().equals("2")) {//AAP01
				/*validacion para zona extendida*/
				if (aform.getZonaExtendida().substring(0, 1).equals("Y") && aform.getDestinationcode().contains("70")) {//AAP01
					/*validacion de envio al mismo site*/
					if (global.getAssignedSite().equals(aform.getDestinationsitecode())) {//AAP01
						curr_local = global.getDestinationBranchId();//AAP01
					}
				} else {//AAP01
					curr_local = global.getAssignedBranch();//AAP01
					/*condicion para entrega de EAD normal pero con operador logistico*/					
				if (aform.getOperadorLogistico().trim().length()>0) {
						curr_dest = cnct.delete(0, cnct.length()).append(global.getDestinationSiteId()).append("70").toString();
						destBranchId = cnct.delete(0, cnct.length()).append(global.getDestinationSiteId()).append("70").toString();
						
						/*validacion de envio al mismo site*/
						if (global.assignedSite.equals(aform.getDestinationsitecode())) {//AAP01
							curr_local = curr_dest;//AAP01
						}
						/*cambiar registro de sucursal para variables de forma*/
						aform.setDestinationcode(destBranchId);
						
					}
				}//AAP01	
			}			
			
			String insertQuery = cnct.delete(0,cnct.length())
					.append("insert into bok_guia_head (GH_ORGN_BRNC_ID,")
					.append("GH_DEST_BRNC_ID,GH_BILL_BRNC_ID ,GH_GUIA_NO ,")
					.append( "GH_GUIA_TYPE ,GH_GUIA_AMNT ,GH_PYMT_MODE ,")
					.append("GH_PYMT_TYPE ,GH_GUIA_STUS,GH_PAID_DATE,")
					.append("GH_ISSE_DATE,GH_ORGN_CLNT_ID,GH_ORGN_CLNT_NAME ,")
					.append("GH_DEST_CLNT_ID,GH_DEST_CLNT_NAME,")
					.append("GH_BILL_CLNT_NAME,GH_BILL_CLNT_ID,")
					.append("GH_BOOK_USER_ID,GH_BOOK_CONT_ID,CRTD_ON,")
					.append("CRTD_BY,MDFD_ON,MDFD_BY,GH_GUIA_SUB_FLAG,")
					.append("GH_CONV_FLAG,GH_ORGN_SLMN_ID,GH_DEST_SLMN_ID,")
					.append("GH_NO_TRIF,GH_PREP_BRNC_ID,GH_ACTV_FLAG,")
					.append("GH_DOCU_TYPE,GH_NUMB_PACK,GH_EAD_FLAG ,")
					.append("GH_RAD_FLAG,GH_ANMO_EXST,GH_LOAD_STUS,")
					.append("GH_TOTL_WGHT,GH_TOTL_VLUM,GH_TOTL_DECL_VLUE,")
					.append("GH_FORM_NO,GH_GUIA_SUMD,GH_CRED_PERD,")
					.append("GH_INVC_FLAG,GH_PEDI_NUMB,GH_CUST_AGNT,")
					.append("GH_SHIP_SEQN,GH_NO_SHIP,GH_CURR_LOCA,")
					.append("GH_CURR_DEST,GH_DEFA_ROUT,GH_COMT,")
					.append("GH_EAD_DEFA_ROUT,GH_CONC_IDEN_TYPE,")
					.append("GH_CONC_IDEN_NO,GH_ORG_SITE_ID,")
					.append("GH_DEST_SITE_ID, GH_FLAG_1")
					.append(",GH_CMPY_ID")
					.append(")")
					.append(" VALUES (?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ")
					.append("?,?,?,?,?,?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?, ")
					.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)").toString();
			
			pst = con.prepareStatement(insertQuery);
			pst.setString(1, global.getAssignedBranch());

			pst.setString(2, destBranchId);//AAP01
			//pst.setString(3, global.getAssignedBranch());//AAP04
			pst.setString(3, billBranchId);//AAP04
			pst.setString(4, genGuiaNumber);
			pst.setString(5, "H");
			pst.setDouble(6, Double.parseDouble(servicesTotal.total));
			//pst.setString(7, "PAID");//AAP04
			pst.setString(7, aform.getFormaPago());//AAP04
			//pst.setString(8, "C");
			pst.setString(8, tipoPago);
			pst.setString(9, "WBK");
			pst.setString(10, "");
			pst.setTimestamp(11, sysDate);
			pst.setString(12, global.getClientId());
			pst.setString(13, global.getClientName());
			pst.setString(14, aform.getDestinationclave());
			pst.setString(15, aform.getDestinationnombre());
			pst.setString(16, billClientName); //AAP04
			pst.setString(17, billClientId); //AAP04
			//pst.setString(18, global.getClientId());//AAP05
			pst.setString(18, global.getOrigenUserClave());//AAP05
			pst.setString(19, infContact.get(0).toString());//cont_id
			pst.setTimestamp(20, sysDate);
			//pst.setString(21, global.getClientId());//AAP05
			pst.setString(21, global.getOrigenUserClave());//AAP05
			pst.setTimestamp(22, sysDate);
			//pst.setString(23, global.getClientId());//AAP05
			pst.setString(23, global.getOrigenUserClave());//AAP05
			pst.setString(24, "N");
			pst.setString(25, "N");
			pst.setString(26, slmnIdOrigDest.get(0).toString());//orginSlmnId
			pst.setString(27, slmnIdOrigDest.get(1).toString());//destSlmnId
			pst.setString(28, "N");
			pst.setString(29, global.getAssignedBranch());
			pst.setString(30, "A");
			pst.setString(31, "Q");
			pst.setDouble(32, Double.parseDouble(servicesTotal.totalPack));
			pst.setString(33, eadFlag);
			pst.setString(34, "1");
			pst.setString(35, "N");
			pst.setString(36, "N");
			pst.setDouble(37, Double.parseDouble(servicesTotal.totalWeight));
			pst.setDouble(38, Double.parseDouble(servicesTotal.totalVolume));
			if (aform.getValordeclarado().trim().length() > 0) {
				pst.setDouble(39, Double.parseDouble(aform.getValordeclarado().trim()));
			} else {
				pst.setNull(39, Types.NUMERIC);
			}					
			pst.setString(40, aform.getGuiano().trim().toUpperCase());
			pst.setString(41, "N");
			pst.setInt(42, 0);
			pst.setString(43, invoiceFlag);
			pst.setString(44, aform.getPedinumber().trim().toUpperCase());
			pst.setString(45, aform.getCustagent().trim().toUpperCase());
			pst.setString(46, aform.getShip_seqn());

			if (aform.getNo_ship() != null && aform.getNo_ship().trim().length() > 0) {
				pst.setInt(47, Integer.parseInt(aform.getNo_ship()));
			} else {
				pst.setNull(47, Types.INTEGER);
			}			
			
			pst.setString(48, curr_local);//AAP01
			pst.setString(49, curr_dest);//AAP01
			pst.setString(50, aform.getLc_rout());
			pst.setString(51, comText);
			pst.setString(52, defaultEadRout);
			pst.setString(53, infContact.get(1).toString());//iden_type
			pst.setString(54, infContact.get(2).toString());//iden_no
			pst.setString(55, global.getAssignedSite());
			pst.setString(56, aform.getDestinationsitecode());
			pst.setString(57, bandOperLog);
			pst.setString(58, companyHead);
			pst.executeUpdate();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBookGuiaHead()_Error:").append(e).toString());
			e.printStackTrace();
		} finally{
			resources.cerrarPreparedStatement(pst);
		}
	}

	private void insertBookGuiaAddress(Connection con, JavWebBookingGeneralMainForm aform, String genGuiaNumber, Map<String, String> mapsClntGeneric) {
		CallableStatement cst = null;
		try {
		
			String addressQuery = "{call pack_web.PRO_INSRT_WEB_BOK_ADDR(?,?,?,?,?)}";
			cst = con.prepareCall(addressQuery);
			cst.setString(1, genGuiaNumber);
			cst.setString(2, aform.getOrgienclave());
			cst.setString(3, aform.getOrgionaddresscode());
			cst.setString(4, aform.getDestinationaddresscode());
			if (aform.getFormaPago().equals("PAID")) {
				cst.setString(5, aform.getFiscaladdresscode());				
			} else {
				if(mapsClntGeneric == null) {
					cst.setString(5, aform.getDestinationaddresscode());
				} else {
					cst.setString(5, mapsClntGeneric.get("addrCode"));
				}
			}
			
			cst.executeQuery();
			
			if (aform.getCheckTelDir().equals("V")) {
				updateBookGuiaAddress(con, aform, genGuiaNumber);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBookGuiaAddress()_Error:").append(e).toString());
			e.printStackTrace();
		} finally{
			resources.cerrarCallableStatement(cst);
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean insertBookGuiaServiceItem(Connection con, JavWebBookingGeneralMainForm aform,
			String genGuiaNumber, java.sql.Timestamp sysDate, ArrayList servicesDetailArray, Global global)  {
		PreparedStatement pst = null;
		boolean registro = false;//AAP11
		ShipmentServiceDetail ssd = null;
		boolean isDefault = false;
		try {
			//boolean isDefault = false;
			if (aform.getDefaultservicescreen().equalsIgnoreCase("yes") && aform.getClasifTarif().equalsIgnoreCase("0")) {//AAP03
				isDefault = true;
			}
			
			String insertQuery = cnct.delete(0,cnct.length())
					.append("INSERT INTO BOK_GUIA_SRVC_ITEM(GL_GUIA_NO,")
					.append("GL_SRVC_ID,GL_REFR_SRVC_ID,GL_DESC,GL_CONT,")
					.append("GL_QUNT,GL_SRVC_CHRG,GL_STUS_FLAG,CRTD_ON,")
					.append("CRTD_BY,MDFD_ON,MDFD_BY,GL_SLAB_NO,")
					.append("GL_VLUE_1,GL_VLUE_2,GL_DOCU_TYPE, GL_GUIA_TYPE, GL_FLAG_2, GL_VOL_L, GL_VOL_W, GL_VOL_H, GL_CMPY_ID, GL_PRODUCT_ID)")
					.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)").toString();

			//ShipmentServiceDetail ssd = null; 
			
			for (int i = 0; i < servicesDetailArray.size(); i++) {

				ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
				//Validamos si el Array de servicios contiene el de ACUSE XT
				//Validamos si el Array de servicios contiene el de ACUSE XT
				
				//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServiceItem()1_").append(aform.getOrgienclave()).append("ssd ").append(ssd.contenido).toString());
				//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServiceItem()1_").append(aform.getOrgienclave()).append("ssd ").append(ssd.refServiceId).toString());
				//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServiceItem()1_").append(aform.getOrgienclave()).append("ssd ").append(ssd.descripcionCode).toString());
				pst = con.prepareStatement(insertQuery);
				pst.setString(1, genGuiaNumber);
				pst.setString(2, ssd.getRefServiceId());
				pst.setString(3, ssd.getServiceId());
				pst.setString(4, ssd.getDescripcionCode());
				pst.setString(5, ssd.getContenido());				
				pst.setInt(6, Integer.parseInt(ssd.getCantidad()));
				
				if (!ssd.getCostoTarifaBase().equals("0.00") && ssd.getConvenioAlto().equals("N")) {//AAP10				
					pst.setDouble(7, Double.parseDouble(ssd.getCostoTarifaBase()));//AAP10
				} else {//AAP10
					pst.setDouble(7, Double.parseDouble(ssd.getImporte()));
				}//AAP10
				
				/*si el servicio es SHP-G ó SHP-e y esta habilitada la bandera para generar el servicio local, se deja en 0.00 el costo en GL_SRVC_CHRG para SHP-G y SHP-E*/
				if ((ssd.getServiceId().equals("SHP-G") || ssd.getServiceId().equals("SHP-E")) && global.isGenLocalService()) {//AAP17
					pst.setDouble(7, 0.0);//AAP17
				}//AAP17
				
				pst.setString(8, "Y");
				pst.setTimestamp(9, sysDate);
				//pst.setString(10, aform.getOrgienclave());//AAP05
				pst.setString(10, global.getOrigenUserClave());//AAP05
				pst.setTimestamp(11, sysDate);
				//pst.setString(12, aform.getOrgienclave());//AAP05
				pst.setString(12, global.getOrigenUserClave());//AAP05
				if(aform.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
					pst.setString(13, ssd.getTarifa());
				}else if (isDefault) {
					/*asignar Tipo de tarifa a Tarifa clasificacion C. Aplica cuando a la tarifa C, se le pudo calcular tipo de tarifa (T1, T2 .. T7)*/
					if (aform.getTarifType().equals("C") && ssd.getTarifa()!=null && ssd.getTarifa().length()>0 ) {//AAP10
						pst.setString(13, ssd.getTarifa());//AAP10
					} else {//AAP10
						pst.setString(13, aform.getTarifType());
					}//AAP10
					
				} else {
					pst.setString(13, ssd.getTarifa());
				}
				//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServiceItem()1_").append(aform.getOrgienclave()).append("%&%&%&%&%&%&%&%&%&%%&&aform.getTarifType()=====>").append(aform.getTarifType()).toString());
				//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServiceItem()1_").append(aform.getOrgienclave()).append("%&%&%&%&%&%&%&%ssd.tarifa=====>").toString());
				pst.setDouble(14, Double.parseDouble(ssd.getPeso()));
				pst.setDouble(15, Double.parseDouble(ssd.getVolumen()));
				pst.setString(16, "Q");
				pst.setString(17, "H");
				pst.setString(18, ssd.getConvenioAlto());
				pst.setString(19, ssd.getVolL());
				pst.setString(20, ssd.getVolW());
				pst.setString(21, ssd.getVolH());
				pst.setString(22, ssd.getCompanyID());
				pst.setString(23, ssd.getProductIdSat());
				int insertCount = pst.executeUpdate();
				
				if (insertCount > 0) {
					registro = true;//AAP11
					//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServiceItem()1_").append(aform.getOrgienclave()).append("BOK GUIA HEAD INSERT SCUCESS").toString());
				} else {
					AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServiceItem()1_").append(aform.getOrgienclave()).append("BOK GUIA HEAD INSERT FALILURE").toString());
				}
				
				if (pst!=null)
					pst.close();
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBookGuiaServiceItem()1_Error:").append(e).toString());					
			registro = false;//AAP11
			e.printStackTrace();
			
			cnct.delete(0,cnct.length())
			.append("Cliente Origen: ").append(global.getClientId()).append("\n")
			.append("Usuario Origen: ").append(global.getOrigenUserClave()).append("\n")
			.append("Rastreo: ").append(genGuiaNumber).append("\n")
			.append("Servicios En sesión:\n");
			for (int i = 0; i < servicesDetailArray.size(); i++) {
				ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
				cnct
				.append("******************************************************************************************\n")
				.append("Servicio[").append(i).append("]:\n******************************************************************************************\n")
				.append("ssd.getRefServiceId(): ").append(ssd.getRefServiceId()).append("\n")
				.append("ssd.getServiceId(): ").append(ssd.getServiceId()).append("\n")
				.append("ssd.getDescripcionCode(): ").append(ssd.getDescripcionCode()).append("\n")
				.append("ssd.getContenido(): ").append(ssd.getContenido()).append("\n")
				.append("ssd.getCantidad(): ").append(ssd.getCantidad()).append("\n")
				.append("ssd.getCostoTarifaBase(): ").append(ssd.getCostoTarifaBase()).append("\n")
				.append("ssd.getImporte(): ").append(ssd.getImporte()).append("\n")
				.append("ssd.getConvenioAlto(): ").append(ssd.getConvenioAlto()).append("\n");
				if (!ssd.getCostoTarifaBase().equals("0.00") && ssd.getConvenioAlto().equals("N")) {//AAP10
					cnct.append("Registraría costo base.\n");					
				} else {//AAP10					
					cnct.append("Registraría Importe neto.\n");
				}//AAP10
				cnct
				.append("Fecha: ").append(sysDate).append("\n")
				.append("ssd.getTarifa(): ").append(ssd.getTarifa()).append("\n")
				.append("aform.getTarifType(): ").append(aform.getTarifType()).append("\n")
				.append("isDefault: ").append(aform.getTarifType()).append("\n");

				if (isDefault) {
					/*asignar Tipo de tarifa a Tarifa clasificacion C. Aplica cuando a la tarifa C, se le pudo calcular tipo de tarifa (T1, T2 .. T7)*/
					if (aform.getTarifType().equals("C") && ssd.getTarifa()!=null && ssd.getTarifa().length()>0 ) {//AAP10						
						cnct.append("Obtendría tipo de tarifa (msg 1).\n");	
					} else {//AAP10						
						cnct.append("Obtendría clasificacion de tarifa de cliente.\n");
					}//AAP10
					
				} else {
					cnct.append("Obtendría tipo de tarifa (msg 2).\n");
				}
				cnct.append("ssd.getPeso(): ").append(ssd.getPeso()).append("\n")
				.append("ssd.getVolumen(): ").append(ssd.getVolumen()).append("\n");
				cnct.append("ssd.getVolL(): ").append(ssd.getVolL()).append("\n");
				cnct.append("ssd.getVolW(): ").append(ssd.getVolW()).append("\n");
				cnct.append("ssd.getVolH(): ").append(ssd.getVolH()).append("\n");
				cnct.append("ssd.getCompanyID(): ").append(ssd.getCompanyID()).append("\n");
				cnct.append("ssd.getProductIdSat(): ").append(ssd.getProductIdSat()).append("\n");
			}
			cnct.append("******************************************************************************************");
			String datos = cnct.toString();
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBookGuiaServiceItem()1_Información de datos:\n").append(datos).toString());	
		} finally{
			resources.cerrarPreparedStatement(pst);
		}
		return registro;
	}
		
	@SuppressWarnings("rawtypes")
	private void insertBookGuiaServices(Connection con, HttpSession session, String genGuiaNumber, Timestamp sysDate,
			JavWebBookingGeneralMainForm serForm, HttpServletRequest req, String OperadorLogistico, ServicesTotal servicesTotal,HashMap <String, ParamDTO> servicesCompanyNueva, String companyHead) {
		
		try {			
			//Global global = (Global) session.getAttribute("sGlobal");
			ArrayList servicesList = (ArrayList) session.getAttribute("calculatedservicelist");
			boolean insbokExtDtl= false;
			/*
			 * Available Services Object.. 1.shipE 2.shipG 3.rad 4.ead 5.ack 6.cod
			 * 7.codr 8.inv 9.Add1
			 */
			String insuranceView = "";
			Hashtable servicesTable = null;
			ArrayList listOfservices = null;
			Hashtable serv_new = null;
			Set set_new = null;
			Iterator new_iterator = null;
			String key = "";
			Services servicesTemp = null;
			ArrayList additionalServicesArray = null;
			String referenciaItem = "";
			String idItem = "";
			AdditionalService serviceRecordsRecs1 = null;
			for (int i = 0; i < servicesList.size(); i++) {
				servicesTable = (Hashtable) servicesList.get(i);
				if (servicesTable.get("newservice") != null) {
					listOfservices = (ArrayList) servicesTable.get("newservice");
					for (int index = 0; index < listOfservices.size(); index++) {
						//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServices()_").append(global.clientId).append("INSIDE RAD").toString());
						serv_new = (Hashtable) listOfservices.get(index);
						set_new = serv_new.keySet();
						new_iterator = set_new.iterator();
						for (; new_iterator.hasNext();) {
							key = (String) new_iterator.next();
							servicesTemp = (Services) serv_new.get(key);
							insertBookGuiaServiceItem(con, session, genGuiaNumber, sysDate, servicesTemp, key, servicesTemp.GS_SRVC_ID, serForm, "N");
							insertBookGuiaServiceRequirement(con, session, genGuiaNumber, key);
							insertServicesRecord(con, session, sysDate, servicesTemp, genGuiaNumber, "N", servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID());
							insertServicesRecordRinv(con, session, sysDate, servicesTemp, genGuiaNumber, servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID(), servicesCompanyNueva);
						}
					}
				} else if ((Services) servicesTable.get("shipE") != null) {
					//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServices()_").append(global.clientId).append("INSIDE SHIPE").toString());
					servicesTemp = (Services) servicesTable.get("shipE");
					insertServicesRecord(con, session, sysDate, servicesTemp, genGuiaNumber, "N", servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID());
					insertServicesRecordRinv(con, session, sysDate, servicesTemp, genGuiaNumber, servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID(), servicesCompanyNueva);
				} else if ((Services) servicesTable.get("shipGComp") != null) {
					//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServices()_").append(global.clientId).append("INSIDE SHIPG").toString());
					servicesTemp = (Services) servicesTable.get("shipGComp");
					insertServicesRecord(con, session, sysDate, servicesTemp, genGuiaNumber, "N", servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID());
					insertServicesRecordRinv(con, session, sysDate, servicesTemp, genGuiaNumber, servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID(), servicesCompanyNueva);
				}/* else if ((Services) servicesTable.get("shipLS") != null) {
					//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServices()_").append(global.clientId).append("INSIDE SHIPG").toString());
					servicesTemp = (Services) servicesTable.get("shipLS");
					insertServicesRecord(con, session, sysDate, servicesTemp, genGuiaNumber, "N");
					insertBookGuiaServiceItem(con, session, genGuiaNumber, sysDate, servicesTemp, "ELC-1", "ELC", serForm, "N");
				}*/ else if ((Services) servicesTable.get("rad") != null) {
					//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServices()_").append(global.clientId).append("INSIDE RAD").toString());
					servicesTemp = (Services) servicesTable.get("rad");
					referenciaItem = "RAD";
					if (serForm.getOrginbranchcode().contains("70")) {
						idItem = "RAD-ZP-1";
						referenciaItem = "RAD-ZP";
					}else {
						idItem = "RAD-1";
					}
					
					if (Double.parseDouble(servicesTemp.GS_SUB_TOTL) > 0 || servicesTemp.isEnvelopeOnly()) {
						insertBookGuiaServiceItem(con, session, genGuiaNumber, sysDate, servicesTemp, idItem, referenciaItem, serForm, "N");
						insertBookGuiaServiceRequirement(con, session, genGuiaNumber, idItem);
						insertServicesRecord(con, session, sysDate, servicesTemp, genGuiaNumber, "N", servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID());
						insertServicesRecordRinv(con, session, sysDate, servicesTemp, genGuiaNumber, servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID(), servicesCompanyNueva);
					}
				} else if ((Services) servicesTable.get("ead") != null) {
					servicesTemp = (Services) servicesTable.get("ead");
					
					if (serForm.getZonaExtendida().substring(0, 1).equals("Y") && serForm.getDestinationcode().contains("70")) {
						referenciaItem = "EXT";
						//AAP//AccessLog.Log("#$#$#$#$$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$");
						//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServices()_").append(global.clientId).append("operador logistico serForm.getOperadorLogistico() ").append(serForm.getOperadorLogistico()).toString());
						//AAP//AccessLog.Log("#$#$#$#$$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$");
						//idItem = "EXT-" + serForm.getOperadorLogistico();//AAP06
						idItem = "EXT-1";//AAP06
						insbokExtDtl = true;//bandera para insertar en la tabla bok_ext_dtl
					} else {					
						referenciaItem = "EAD";
						idItem = "EAD-1";
						
						/*condicion para insertar en bok_ext_dtl cuando la entrega se EAD a operador logistico (zonas de operador logistico con concepto de EAD)*/
						if (serForm.getOperadorLogistico().trim().length()>0) {
							insbokExtDtl = true;
						}
					}
					
					if (insbokExtDtl) {
						insertRelacionGuiaOL(con, serForm, servicesTemp, genGuiaNumber, OperadorLogistico, servicesTotal);//AAP06
					}
					
					//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServices()_").append(global.clientId).append( "INSIDE EAD").toString());
					
					insertBookGuiaServiceRequirement(con, session, genGuiaNumber, idItem);
					insertBookGuiaServiceItem(con, session, genGuiaNumber, sysDate, servicesTemp, idItem, referenciaItem, serForm, "N");
					insertServicesRecord(con, session, sysDate, servicesTemp, genGuiaNumber, "N", servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID());
					insertServicesRecordRinv(con, session, sysDate, servicesTemp, genGuiaNumber, servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID(), servicesCompanyNueva);
				} else if ((Services) servicesTable.get("ack") != null) {
					//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServices()_").append(global.clientId).append("INSIDE ACK").toString());
					servicesTemp = (Services) servicesTable.get("ack");
					insertBookGuiaServiceRequirement(con, session, genGuiaNumber, serForm.getAcusederecibo());
					insertBookGuiaServiceItem(con, session, genGuiaNumber, sysDate, servicesTemp, serForm.getAcusederecibo(), "ACK", serForm, "N");
					insertServicesRecord(con, session, sysDate, servicesTemp, genGuiaNumber, "N", servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID());
					insertServicesRecordRinv(con, session, sysDate, servicesTemp, genGuiaNumber, servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID(), servicesCompanyNueva);
				} else if ((Services) servicesTable.get("cod") != null) {
					//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServices()_").append(global.clientId).append("INSIDE COD").toString());
					servicesTemp = (Services) servicesTable.get("cod");
					insertBookGuiaServiceRequirement(con, session, genGuiaNumber, "COD-1");
					insertBookGuiaServiceItem(con, session, genGuiaNumber, sysDate, servicesTemp, "COD-1", "COD", serForm, "N");
					insertServicesRecord(con, session, sysDate, servicesTemp, genGuiaNumber, "N", servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID());
					insertServicesRecordRinv(con, session, sysDate, servicesTemp, genGuiaNumber, servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID(), servicesCompanyNueva);
				} else if ((Services) servicesTable.get("codr") != null) {
					//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServices()_").append(global.clientId).append("INSIDE CODR").toString());
					servicesTemp = (Services) servicesTable.get("codr");
					insertBookGuiaServiceRequirement(con, session, genGuiaNumber, "CODR-1");
					insertBookGuiaServiceItem(con, session, genGuiaNumber, sysDate, servicesTemp, "CODR-1", "CODR", serForm, "N");
					insertServicesRecord(con, session, sysDate, servicesTemp, genGuiaNumber, "N", servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID());
					insertServicesRecordRinv(con, session, sysDate, servicesTemp, genGuiaNumber, servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID(), servicesCompanyNueva);
				} else if ((Services) servicesTable.get("inv") != null) {
					//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServices()_").append(global.clientId).append("INSIDE INV").toString());
					servicesTemp = (Services) servicesTable.get("inv");

					insuranceView = "N";//getInsuranceViewFlag(con, global, req);//AAP12
					insertBookGuiaServiceRequirement(con, session, genGuiaNumber, serForm.getCobertura());
					insertBookGuiaServiceItem(con, session, genGuiaNumber, sysDate, servicesTemp, serForm.getCobertura(), "INV", serForm, insuranceView);
					insertServicesRecord(con, session, sysDate, servicesTemp, genGuiaNumber, insuranceView, servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID());
					insertServicesRecordRinv(con, session, sysDate, servicesTemp, genGuiaNumber, servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID(), servicesCompanyNueva);
					insuranceView = "";
				}

				/*
				 * code added by palanivel to insert Additional Service Related
				 * Information
				 */

				//if (global.tarifType.equalsIgnoreCase("C") && session.getAttribute("aditionalServicesDetail") != null) {
				if (serForm.getShowAdditional().equalsIgnoreCase("Y") && session.getAttribute("aditionalServicesDetail") != null) {
					additionalServicesArray = (ArrayList) session.getAttribute("aditionalServicesDetail");
					for (int j = 0; j < additionalServicesArray.size(); j++) {
						serviceRecordsRecs1 = (AdditionalService) additionalServicesArray.get(j);

						if ((Services) servicesTable.get(serviceRecordsRecs1.service) != null) {
							//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServices()_").append(global.clientId).append("INSIDE INV").toString());
							servicesTemp = (Services) servicesTable.get(serviceRecordsRecs1.service);
							insertBookGuiaServiceRequirement(con, session, genGuiaNumber, serForm.getCobertura());
							insertBookGuiaServiceItem(con, session, genGuiaNumber, sysDate, servicesTemp, serviceRecordsRecs1.service, serviceRecordsRecs1.serviceId, serForm, "N");
							insertServicesRecord(con, session, sysDate, servicesTemp, genGuiaNumber, "N", servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID());
							insertServicesRecordRinv(con, session, sysDate, servicesTemp, genGuiaNumber, servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID(), servicesCompanyNueva);
						}
					}
				}
			}	
			if (serForm.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
				if ((Services) servicesTable.get("shipE") != null) {
					servicesTemp = (Services) servicesTable.get("shipE");
				}  if ((Services) servicesTable.get("shipGComp") != null) {
					servicesTemp = (Services) servicesTable.get("shipGComp");
				}  /*if ((Services) servicesTable.get("shipLS") != null) {
					servicesTemp = (Services) servicesTable.get("shipLS");
				}*/
				if ((Services) servicesTable.get("rad") != null) {
					servicesTemp = (Services) servicesTable.get("rad");
				}
				if ((Services) servicesTable.get("ead") != null) {
					servicesTemp = (Services) servicesTable.get("ead");
				}
				ShipTypeSEG seg = findServiceSEG(serForm);
				servicesTemp.setGS_TAX("0.0");
				servicesTemp.setGS_DISC("0.0");
				servicesTemp.setGS_TAX_RET("0.0");
				servicesTemp.setGS_SUB_TOTL("0.0");
				servicesTemp.setTOTAL("0.0");
				servicesTemp.setGS_SRVC_TYPE("N");
				servicesTemp.setGS_SRVC_ID(seg.getShipTypeSEGSrvcRefr());
				servicesTemp.setIsSrvcSEG("Y");
				insertBookGuiaServiceItem(con, session, genGuiaNumber, sysDate, servicesTemp, seg.getShipTypeSEGSrvc(), seg.getShipTypeSEGSrvcRefr(), serForm, "N");
				insertBookGuiaServiceRequirement(con, session, genGuiaNumber, seg.getShipTypeSEGSrvc());
				insertServicesRecord(con, session, sysDate, servicesTemp, genGuiaNumber, "N", servicesTemp.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : servicesTemp.getGS_CMPY_ID());
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBookGuiaServices()_Error:").append(e).toString());
			e.printStackTrace();
		}
	}
		
	private void insertBookGuiaStatus(Connection con, HttpSession session,
			JavWebBookingGeneralMainForm generalForm, String genGuiaNumber) {
		CallableStatement cst = null;
		try {
			Global global = (Global) session.getAttribute("sGlobal");
			String query = "{call pack_web.PRO_INSRT_WEB_BOK_STUS(?,?,?,?,?,?)}";
			cst = con.prepareCall(query);
			cst.setString(1, genGuiaNumber);

			if (generalForm.getShip_seqn() != null && generalForm.getShip_seqn().trim().length() > 0) {
				cst.setInt(2, Integer.parseInt(generalForm.getShip_seqn()));
			} else {
				cst.setNull(2, Types.INTEGER);
			}

			if (generalForm.getNo_ship() != null && generalForm.getNo_ship().trim().length() > 0) {
				cst.setInt(3, Integer.parseInt(generalForm.getNo_ship()));
			} else {
				cst.setNull(3, Types.INTEGER);
			}

			cst.setString(4, global.assignedBranch);
			cst.setString(5, global.destinationBranchId);
			cst.setString(6, global.clientId);
			cst.executeQuery();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBookGuiaStatus()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
	}

	private void insertReferenceRecord(Connection con, HttpSession session,
			String genGuiaNumber, String reference, Timestamp sysDate, String mainRefer) {
		PreparedStatement pst = null;
		try {
			Global global = (Global) session.getAttribute("sGlobal");

			String insertQuery = "INSERT INTO WEB_GUIA_REFR(GR_GUIA_NO, GR_GUIA_TYPE, GR_DOCU_TYPE, GR_GUIA_REFR, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY, GR_MAIN_REFR) VALUES(?,?,?,?,?,?,?,?,?)";

			pst = con.prepareStatement(insertQuery);
			pst.setString(1, genGuiaNumber);
			pst.setString(2, "H");
			pst.setString(3, "Q");
			pst.setString(4, reference);
			pst.setTimestamp(5, sysDate);
			//pst.setString(6, global.clientId);//AAP05
			pst.setString(6, global.getOrigenUserClave());//AAP05
			pst.setTimestamp(7, sysDate);
			//pst.setString(8, global.clientId);//AAP05
			pst.setString(8, global.getOrigenUserClave());//AAP05
			pst.setString(9, mainRefer);
			
			int insertCount = pst.executeUpdate();

			if (insertCount > 0) {
				//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertReferenceRecord()_").append(global.clientId).append("WEB GUIA REFR INSERT SCUCESS").toString());
			} else {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertReferenceRecord()_").append(global.clientId).append("WEB GUIA REFR INSERT FALILURE").toString());
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertReferenceRecord()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		
	}

//	private void printFileForGuia(Connection con, HttpServletRequest request,
//			JavWebBookingGeneralMainForm serForm, String genGuiaNumber,
//			String contId, String defaultEadRout, String invoiceFlag,
//			String contactName, String comText, Timestamp sysDate) {
//		CallableStatement cst = null;
//		try {
//			HttpSession session = request.getSession(false);
//			ServicesTotal serTotal = (ServicesTotal) session.getAttribute("servicestotal");
//			Global global = (Global) session.getAttribute("sGlobal");
//			
//			String eadFlag = (serForm.getEntrega().equals("2") ? "1" : "0");
//			
//			cst = con.prepareCall("{call PACK_GUIA_PRINT.PRO_GUIA_PRINT_PRINT_FE(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
//			cst.setString(1, genGuiaNumber);
//			cst.setString(2, global.assignedBranch);
//			//cst.setString(3, global.destinationBranchId);
//			cst.setString(3, serForm.getDestinationcode());			
//			cst.setString(4, global.clientId);
//			cst.setString(5, global.clientName);
//			cst.setString(6, serForm.getDestinationclave());
//			cst.setString(7, serForm.getDestinationnombre());
//			cst.setString(8, eadFlag);
//			cst.setString(9, serForm.getPedinumber());
//			cst.setString(10, serForm.getCustagent());
//			cst.setTimestamp(11, sysDate);
//			cst.setString(12, serForm.getGuiano().trim().toUpperCase());
//			if (serForm.getValordeclarado().trim().length() > 0) {
//				cst.setDouble(13, Double.parseDouble(serForm.getValordeclarado().trim()));
//			} else {
//				cst.setNull(13, Types.NUMERIC);
//			}
//			cst.setDouble(14, Double.parseDouble(serTotal.totalWeight));
//			cst.setString(15, comText);
//			cst.setString(16, contId);
//			cst.setString(17, contactName);
//			cst.setString(18, "");
//			cst.setString(19, serForm.getOrgionbranch());
//			cst.setString(20, serForm.getDestinationbranch());
//			cst.setString(21, defaultEadRout);
//			cst.setString(22, invoiceFlag);
//			cst.setString(23, serForm.getCobertura());
//			cst.setString(24, global.groupClientId);
//			cst.setString(25, serForm.getSeguro());
//			cst.registerOutParameter(26, Types.LONGVARCHAR);
//			cst.setString(27, serForm.getOrgien1());
//			cst.setString(28, serForm.getDestino1());
//			
//			cst.executeQuery();
//			
//			//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("printFileForGuia()_").append(global.clientId).append(" INFORMACION PARA IMPRESION cst.getString(26)").append(cst.getString(26)).toString());
//			
//			new PrintFileExport(request.getSession().getServletContext().getRealPath("/")).writeToGuiaFile(request, cst.getString(26), getTimeStamp(con).getTime());
//		} catch (Exception e) {
//			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("printFileForGuia()_Error:").append(e).toString());
//			e.printStackTrace();
//		} finally {
//			resources.cerrarCallableStatement(cst);
//		}		
//	}
//
//	private void getCadenaImpresion(Connection con, HttpServletRequest request, String rastreo) {
//		CallableStatement cst = null;
//		//String cadenaEtiqueta = "";
//		try {
//			cst = con.prepareCall("{call PACK_GUIA_PRINT_WS.PRO_GUIA_PRINT_WS_ZPL(?, ?) }");
//			cst.setString(1, rastreo);
//			cst.registerOutParameter(2, Types.LONGVARCHAR);
//			
//			cst.executeQuery();
//			
//			//cadenaEtiqueta = cst.getString(2);
//			
//			//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("printFileForGuia()_").append(global.clientId).append(" INFORMACION PARA IMPRESION cst.getString(26)").append(cst.getString(26)).toString());
//			
//			new PrintFileExport(request.getSession().getServletContext().getRealPath("/")).writeToGuiaFile(request, cst.getString(2), getTimeStamp(con).getTime());
//		} catch (Exception e) {
//			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCadenaImpresion()_Error:").append(e).toString());
//			e.printStackTrace();
//		} finally {
//			resources.cerrarCallableStatement(cst);
//		}
//	}
	
	@SuppressWarnings("rawtypes")
	private void insertBookGuiaServiceItem(Connection con, HttpSession session,
			String genGuiaNumber, Timestamp sysDate,
			Services services, String serviceId, String refServiceId,
			JavWebBookingGeneralMainForm serForm, String insView) {
		CallableStatement cst = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			Global global = (Global) session.getAttribute("sGlobal");
			ArrayList servicesDetailArray = null;
			double totalImporte = 0.0;
			
			servicesDetailArray = (ArrayList) session.getAttribute("servicesDetail");
				
			ShipmentServiceDetail ssd = null;			
			for (int i = 0; i < servicesDetailArray.size(); i++) {
				ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);
				totalImporte = totalImporte + Double.parseDouble(ssd.importe);
			}
			
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServiceItem()2_").append(global.clientId).append("insertBookGuiaServiceItem").append(serviceId).append("refer").append(refServiceId).toString());
			
			String descQuery = "SELECT SM_SRVC_NAME FROM SYS_SRVC_MSTR WHERE SM_SRVC_ID = ? AND SM_REFR_SRVC_ID = ?";

			pst = con.prepareStatement(descQuery);

			pst.setString(1, serviceId);
			pst.setString(2, refServiceId);
			rs = pst.executeQuery();
			String description = "";
			if (rs.next()) {
				description = rs.getString(1) == null ? "" : rs.getString(1);
			}
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaServiceItem()2_").append("description").append(description).toString());

			resources.closeResources(rs, pst);

			String serItemQuery = "";

			if (insView.equalsIgnoreCase("Y")) {
				serItemQuery = "{call pack_web.PRO_INSRT_WEB_BOK_SRVC_ITEM_NE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			} else {
				serItemQuery = "{call pack_web.PRO_INSRT_WEBBOKSRVCITEM_CIA(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
			}
			cst = con.prepareCall(serItemQuery);
			
			cst.setString(1, genGuiaNumber);
			cst.setString(2, serviceId);
			cst.setString(3, refServiceId);
			cst.setString(4, description );
			cst.setString(5, "");
			cst.setString(6, "");
			cst.setDouble(7, Double.parseDouble(services.GS_SUB_TOTL));
			cst.setString(8, "Y");
			cst.setTimestamp(9, sysDate);
			cst.setString(10, global.clientId);
			cst.setTimestamp(11, sysDate);
			cst.setString(12, global.clientId);
			cst.setString(13, serviceId);

			if (services.getIsSrvcSEG().equalsIgnoreCase("Y")) {
				cst.setDouble(14, 0.0);
			} else if (refServiceId.equalsIgnoreCase("EAD") || refServiceId.equalsIgnoreCase("RAD") || refServiceId.equalsIgnoreCase("RAD-ZP")) {
				cst.setDouble(14, totalImporte);
			} else if (refServiceId.equalsIgnoreCase("INV")) {
				if (serForm.getValordeclarado().trim().length() > 0) {
					cst.setDouble(14, Double.parseDouble(serForm.getValordeclarado().trim()));
				} else {
					cst.setNull(14, Types.NUMERIC);
				}
			} else if (refServiceId.equalsIgnoreCase("ACK")) {
				cst.setNull(14, Types.NUMERIC);
			} else if (refServiceId.equalsIgnoreCase("COD")) { 
				cst.setNull(14, Types.NUMERIC);
			} else if (refServiceId.equalsIgnoreCase("CODR")) {
				if (serForm.getValorcod().trim().length() > 0) {
					cst.setDouble(14, Double.parseDouble(serForm.getValorcod().trim()));
				} else {
					cst.setDouble(14, 0.0d);
				}
			} else if (totalImporte == 0) {
				cst.setNull(14, Types.NUMERIC);
			} else {
				cst.setDouble(14, totalImporte);
			}

			cst.setInt(15, 0);
			cst.setString(16, "Q");
			cst.setString(17, "H");
			cst.setString(18, services.getGS_CMPY_ID() == null? serForm.getCompanyIdForServices() : services.getGS_CMPY_ID());
			cst.executeQuery();
			//boolean xt_bool = false;
			//System.out.println("ID DE SERVICIO:" + serviceId);
			//if(serviceId.equals("ACK-X")) {
				//xt_bool = true;
			//}
//			if(xt_bool == true) {
//				System.out.println("**************************************************************");
//				System.out.println("LA GUIA CONTIENE ACUSE XT, GENERAR GUIA DE RETORNO AQUI");
//				System.out.println("**************************************************************");
//			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBookGuiaServiceItem()2_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst, cst);
		}
	}

	private void insertBookGuiaServiceRequirement(Connection con,
			HttpSession session, String genGuiaNumber, String aditionalService) {
		CallableStatement cst = null;
		try {
			Global global = (Global) session.getAttribute("sGlobal");
			cst = con.prepareCall("{call pack_web.pro_insrt_web_bok_item_reqm(?,?,?)}");
			cst.setString(1, global.groupClientId);
			cst.setString(2, aditionalService);
			cst.setString(3, genGuiaNumber);
			
			cst.executeQuery();
						
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBookGuiaServiceRequirement()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);	
		}
	}

	private void insertServicesRecord(Connection con, HttpSession session, Timestamp sysDate, 
			Services services, String genGuiaNumber, String insView, String companyId) {
		CallableStatement cst = null;
		Global global = (Global) session.getAttribute("sGlobal");
		String query = "";
		if (insView.equalsIgnoreCase("Y")) {
			query = "{ call pack_web.PRO_INSRT_WEB_BOK_SRVC_NEW(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		} else {
			query = "{ call pack_web.PRO_INSRT_WEB_BOK_SRVC_CIA(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		}
		try {

			cst = con.prepareCall(query);
			//AAP//AccessLog.Log(query);
			cst.setString(1, genGuiaNumber);
			//AAP//AccessLog.Log(genGuiaNumber);
			cst.setString(2, services.GS_SRVC_ID);
			//AAP//AccessLog.Log(services.GS_SRVC_ID);
			cst.setDouble(3, Double.parseDouble(services.GS_DISC));
			//AAP//AccessLog.Log(services.GS_DISC);
			cst.setDouble(4, Double.parseDouble(services.GS_TAX));
			//AAP//AccessLog.Log(services.GS_TAX);
			cst.setDouble(5, Double.parseDouble(services.GS_TAX_RET));
			//AAP//AccessLog.Log(services.GS_TAX_RET);
			//AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertServicesRecord()_").append("before insertion the amount is").append(services.GS_SUB_TOTL).toString());
			cst.setDouble(6, Double.parseDouble(services.GS_SUB_TOTL));
			cst.setString(7, services.GS_ADD_PYMT_FLAG);
			//AAP//AccessLog.Log(services.GS_ADD_PYMT_FLAG);
			cst.setString(8, services.GS_SRVC_TYPE);
			//AAP//AccessLog.Log(services.GS_SRVC_TYPE);
			cst.setString(9, services.GS_DOCU_TYPE);
			//AAP//AccessLog.Log(services.GS_DOCU_TYPE);
			cst.setString(10, services.GS_GUIA_TYPE);
			//AAP//AccessLog.Log(services.GS_GUIA_TYPE);
			cst.setTimestamp(11, sysDate);
			//AAP//AccessLog.Log(sysDate + "");
			cst.setString(12, global.clientId);
			//AAP//AccessLog.Log(global.clientId);
			cst.setTimestamp(13, sysDate);
			//AAP//AccessLog.Log(sysDate + "");
			cst.setString(14, global.clientId);
			//AAP//AccessLog.Log(global.clientId);
			cst.setString(15, services.GS_STUS_FLAG);
			//AAP//AccessLog.Log(services.GS_STUS_FLAG);
			cst.setString(16, services.GS_DISC_SLAB_NO);
			//AAP//AccessLog.Log(services.GS_DISC_SLAB_NO);
			cst.setString(17, companyId);
			//JSA//AccessLog.Log(companyId);
			cst.executeQuery();			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertServicesRecord()_").append( " BEFORE SERVICE SPECIAL").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		//AAP//AccessLog .Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertServicesRecord()_").append("****************************** BOOK_GUIA_SERVICES INSERTION SUCCESS *************************").toString());
	}

//	private String getInsuranceViewFlag(Connection con, Global global, HttpServletRequest req) {//AAP12
//
//		String query = "";
//		String band = "";
//		PreparedStatement pst = null;
//		ResultSet rs = null;
//		try {
//			query = "select nvl(WC_OMITIR_COBRO_SEGURO,'N') WC_OMITIR_COBRO_SEGURO from web_clnt_mstr where wc_clnt_id =?";
//			pst = con.prepareStatement(query);
//			pst.setString(1, global.clientId);
//			rs = pst.executeQuery();
//			while (rs.next()) {
//				band = rs.getString(1);
//				if (rs.getString(1).equalsIgnoreCase("Y")) {
//					req.setAttribute("NOINSURANCE", "SHOULD NOT DISPLAY INSURANCE");
//					//return "Y";
//				}
//			}
//		} catch (Exception e) {
//			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTarifaEspecialExt()_Error:").append(e).toString());
//			e.printStackTrace();
//		}finally{
//			resources.closeResources(rs, pst);
//		}
//		//return "N";
//		return band;
//	}

	private String getFormNumber(Connection con,Global global, String modulo, String serieCaja) {
		CallableStatement cst=null;
		
		String formNumber="A0";
		try {			
			String sql = "declare\n"
                    + "pragma Autonomous_Transaction;\n"
                    + "begin\n"
                    + "? := fun_ftch_form_no_v2(?, ?, ?, ?); \n"
                    + "commit;\n"
                    + "end; ";
			cst = con.prepareCall(sql);
			cst.registerOutParameter(1,Types.VARCHAR);
			cst.setString(2, global.getAssignedBranch());
			cst.setString(3, global.getAssignedBranch());
			cst.setString(4, serieCaja);
			cst.setString(5, modulo);
			cst.executeQuery();
			formNumber = cst.getString(1);			
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getFormNumber()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}		
		return cnct.delete(0,cnct.length()).append("WW").append(formNumber).toString();		
	}
	
	private void insertRelacionGuiaOL(Connection con, JavWebBookingGeneralMainForm serForm, 
			Services services, String genGuiaNumber, String OperadorLogistico, ServicesTotal servicesTotal) {
		CallableStatement cst = null;
		String query = "{ call PACK_ZE_SIP.pro_ext_insert_BOK_EXT(?,?,?,?,?,?,?,?,?)}";
		try {

			cst = con.prepareCall(query);
			//AccessLog.Log(query);
			cst.setString(1, OperadorLogistico);
			//AccessLog.Log("insertRelacionGuiaOL()_OperadorLogistico "+OperadorLogistico);
			cst.setString(2, genGuiaNumber);
			//AccessLog.Log("insertRelacionGuiaOL()_genGuiaNumber "+genGuiaNumber);
			//cst.setDouble(3, Double.parseDouble(servicesTotal.totalPack));
			cst.setInt(3, Integer.parseInt(servicesTotal.totalPack));
			//AccessLog.Log("insertRelacionGuiaOL()_servicesTotal.totalPack "+servicesTotal.totalPack);
			cst.setDouble(4, Double.parseDouble(services.GS_SUB_TOTL) - Double.parseDouble(services.getGS_DISC()) );
			//AccessLog.Log("insertRelacionGuiaOL()_services.GS_SUB_TOTL "+services.GS_SUB_TOTL);
			cst.setString(5, serForm.getOrginbranchcode());
			//AccessLog.Log("insertRelacionGuiaOL()_serForm.getOrginbranchcode() "+serForm.getOrginbranchcode());
			cst.setString(6, serForm.getOrgienclave());
			//AccessLog.Log("insertRelacionGuiaOL()_global.clientId "+serForm.getOrgienclave());
			cst.setString(7, "3");//ORIGEN DOCUMENTACION REMOTA
			cst.setString(8, "WBK");//ESTATUS GUIA DOCUMENTACION REMOTA
			cst.setString(9, "");//CLAVE DE AUTORIZACION DE ENVIOS, (NO APLICA PARA DOCUMENTACION REMOTA)
			
			cst.executeQuery();			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertRelacionGuiaOL()_").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		//AAP//AccessLog .Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertServicesRecord()_").append("****************************** BOOK_GUIA_SERVICES INSERTION SUCCESS *************************").toString());
	}
	
	private String updateBookGuiaAddress(Connection con, JavWebBookingGeneralMainForm aform, String genGuiaNumber) {
		String query = "";
		String band = "";
		PreparedStatement pst = null;

		try {
			query = "UPDATE BOK_GUIA_ADDR SET GA_PHNO_1 = ? WHERE GA_GUIA_NO = ? AND GA_ADDR_TYPE = ?";
			pst = con.prepareStatement(query);
			pst.setString(1, aform.getDestinationtelefono());
			pst.setString(2, genGuiaNumber);
			pst.setString(3, "DESTINATION");
			
			pst.executeUpdate();
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateBookGuiaAddress()_Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.cerrarPreparedStatement(pst);
		}		
		return band;
	}
	
	private void insertWebCntrlMail(Connection con, Global global,
			JavWebBookingGeneralMainForm generalForm, String genGuiaNumber) {
		CallableStatement cst = null;
		try {			
			//String query = "{call pack_web.pro_insrt_web_ctrl_email(?,?,?,?,?,?,?,?)}";
			/*se cambio llamado a procedimiento pro_insrt_web_ctrl_email_right debido a que el anterior 
			 * es usado en la documentacion normal con los campos de correo de origen y destino al reves que en documentacion remota*/			
			String query = "{call pack_web.pro_insrt_web_ctrl_email_right(?,?,?,?,?,?,?,?)}";
			cst = con.prepareCall(query);
			
			cst.setString(1, genGuiaNumber);
			cst.setString(2, global.getClientId());
			cst.setString(3, generalForm.getDestinationclave());
			cst.setString(4, generalForm.geteMailOrigText());
			cst.setString(5, generalForm.geteMailDestText());
			cst.setString(6, "P");
			cst.setString(7, global.getClientId());
			cst.registerOutParameter(8, Types.VARCHAR);
			
			cst.executeQuery();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertWebCntrlMail()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
	}
	
	private int insertBookGuiaExtra(Connection con, 
			JavWebBookingGeneralMainForm generalForm, String genGuiaNumber, Global global) {
		PreparedStatement pst = null;
		int insertCount = 0;
		String ssSrvcId = "SHP-G";
		String ocuEadFlag = "";
		try {	

			String insertQuery = "INSERT INTO BOK_GUIA_HEAD_EXTRA(GE_GUIA_NO, GE_CLNT_ID, GE_CLNT_CCOSTO_ID, GE_CLNT_USER_ID, SRVC_ID, GE_PROMESA_HRS, GE_PROMESA_HRS_RADZP, GE_MAND_RET) VALUES(?, ?, ?, ?, ?, ?, ?, FUN_GET_MAND_IVARET(?))";
			String tipoEnvio = "AEREO";
			if (generalForm.getIsShippingTypeSEG().equalsIgnoreCase("N")) {
				tipoEnvio = "TERRESTRE";
			}
			if (generalForm.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
				tipoEnvio = generalForm.getShippingType();
			}
			
			if (generalForm.getIsSoloSobre().equals("Y")) {//evalua si viene solo sobres
				ssSrvcId = "SHP-E";
			}
			
			if(generalForm.getEntrega().equalsIgnoreCase("1")){//OCURRE
				ocuEadFlag = "OCU";
			} else {
				ocuEadFlag = "EAD";
			}
			
			JavDeliveryHours horas = new JavDeliveryHours();
			String tiempoEntrega = horas.getDeliveryHoursRecordNew(con, generalForm.getOrginbranchcode(), generalForm.getDestinationcode(), ocuEadFlag, generalForm.getDestinationcoloniacode(), tipoEnvio, global.getClientId(), ssSrvcId);
										
			String tiempoEntregaRadZp = !global.getAssignedBranch().contains("70") ? "0" : 
								   horas.getDeliveryHoursZPRecord(con, generalForm.getOrginbranchcode(), generalForm.getDestinationcode(), ocuEadFlag, generalForm.getOriginColinaCode(), tipoEnvio, global.getClientId(), ssSrvcId);
			
			Long tiempoEntregaDLVY = Long.parseLong(tiempoEntrega);
			Long tiempoEntregaZpDLVY = Long.parseLong(tiempoEntregaRadZp);
			pst = con.prepareStatement(insertQuery);
			pst.setString(1, genGuiaNumber);
			pst.setString(2, generalForm.getOrgienclave());
			pst.setString(3, generalForm.getCentrosCosto());
			pst.setString(4, generalForm.getOrigenUserClave());
			
			pst.setString(5, generalForm.getShippingType());
			
//			if (generalForm.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
//				pst.setString(5, generalForm.getShippingType());
//			} else {
//				pst.setNull(5, java.sql.Types.VARCHAR);
//			}
			
//			if (generalForm.getIsShippingTypeSEG().equalsIgnoreCase("Y")) {
//				pst.setLong(6, tiempoEntregaDLVY);
//			} else {
//				pst.setNull(6, java.sql.Types.NUMERIC);
//			}
			
			pst.setLong(6, tiempoEntregaDLVY);
			pst.setLong(7, tiempoEntregaZpDLVY);
			pst.setString(8, generalForm.getOrgienclave());//asignacion de bandera retencion obligatoria
			insertCount = pst.executeUpdate();

			if (insertCount > 0) {
				//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaExtra()_").append(generalForm.getOrgienclave()).append("WEB_GUIA_REFR INSERT SCUCESS").toString());
			} else {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBookGuiaExtra()_").append(generalForm.getOrgienclave()).append("WEB GUIA REFR INSERT FAILURE").toString());
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBookGuiaExtra()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		return insertCount;
	}
	
	private int updateBokGuiaHeadRound(Connection con, String genGuiaNumber) {//AAP13
		PreparedStatement pst = null;
		PreparedStatement pstRound = null;
		ResultSet rs = null;
		int insertCount = 0;
		try {	

			String query = 
			"SELECT importe, servicios, round((importe-servicios),2) " +
			"FROM (" +
			    "select GH_GUIA_NO, " +
			           "nvl(gh_guia_amnt,0) IMPORTE, "  +
			           "nvl( (select SUM((nvl(B.gs_sub_totl,0)-nvl(B.gs_disc,0))+B.GS_TAX-B.GS_TAX_RET) SERVICIOS " +
			            "from bok_guia_srvc B " +
			            "where B.gs_guia_no = GH_GUIA_NO),0)SERVICIOS " +
			    "from bok_guia_head " +
			    "WHERE gh_guia_no = ? " +
			    "and gh_actv_flag = ? " +
			")GUIAS " +
			"WHERE round(nvl(IMPORTE,0),2)<>round(nvl(SERVICIOS,0),2)";
			
			
			pst = con.prepareStatement(query);
			pst.setString(1, genGuiaNumber);
			pst.setString(2, "A");
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				query = "UPDATE BOK_GUIA_HEAD SET GH_GUIA_AMNT = ? WHERE gh_guia_no = ? AND gh_actv_flag = ?";
				pstRound = con.prepareStatement(query);
				
				pstRound.setDouble(1, rs.getDouble("servicios"));
				pstRound.setString(2, genGuiaNumber);
				pstRound.setString(3, "A");
				
				pstRound.executeUpdate();
			}

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateBokGuiaHeadRound()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pstRound);
			resources.closeResources(rs, pst);
		}
		return insertCount;
	}
	
	private int updateCentroCostoDefault(Connection con, 
			JavWebBookingGeneralMainForm generalForm) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		int insertCount = 0;
		int totalCC = 0;
		String ccDefault = "N";
		try {
			String insertQuery = "SELECT COUNT(CU_USER_ID) FROM SYS_CLNT_CCOSTO_USER WHERE CU_CLNT_ID = ? AND CU_USER_ID = ? GROUP BY CU_USER_ID";
			
			pst = con.prepareStatement(insertQuery);
			pst.setString(1, generalForm.getOrgienclave());
			pst.setString(2, generalForm.getOrigenUserClave());
			rs = pst.executeQuery();
			if (rs.next()) {
				totalCC = rs.getInt(1);
			}
			rs.close();
			pst.close();
			
			if (totalCC>1) {
				insertQuery = "SELECT NVL(CU_DFLT_FLAG,'N') FROM SYS_CLNT_CCOSTO_USER WHERE CU_CLNT_ID = ? AND CU_CCOSTO_ID = ? AND CU_USER_ID = ?";
				pst = con.prepareStatement(insertQuery);
				pst.setString(1, generalForm.getOrgienclave());
				pst.setString(2, generalForm.getCentrosCosto());
				pst.setString(3, generalForm.getOrigenUserClave());
				rs = pst.executeQuery();
				if (rs.next()) {
					ccDefault = rs.getString(1);
				}
				rs.close();
				pst.close();
				
				if (ccDefault.equals("N")) {
					insertQuery = "UPDATE SYS_CLNT_CCOSTO_USER SET CU_DFLT_FLAG = ? WHERE CU_CLNT_ID = ? AND CU_CCOSTO_ID = ? AND CU_USER_ID = ?";

					pst = con.prepareStatement(insertQuery);
					
					pst.setString(1, "Y");
					pst.setString(2, generalForm.getOrgienclave());
					pst.setString(3, generalForm.getCentrosCosto());
					pst.setString(4, generalForm.getOrigenUserClave());

					insertCount = pst.executeUpdate();
					
					pst.close();
					
					insertQuery = "UPDATE SYS_CLNT_CCOSTO_USER SET CU_DFLT_FLAG = ? WHERE CU_CLNT_ID = ? AND CU_CCOSTO_ID <> ? AND CU_USER_ID = ? AND CU_DFLT_FLAG = ?";

					pst = con.prepareStatement(insertQuery);
					
					pst.setString(1, "N");
					pst.setString(2, generalForm.getOrgienclave());
					pst.setString(3, generalForm.getCentrosCosto());
					pst.setString(4, generalForm.getOrigenUserClave());
					pst.setString(5, "Y");

					insertCount = pst.executeUpdate();
				}				
					
			}
						

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateCentroCostoDefault()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarResultSet(rs);
			resources.cerrarPreparedStatement(pst);			
		}
		return insertCount;
	}
	
	private int insertCtrlLabelPrn(Connection con, String genGuiaNumber) {
		
		int insertCount = 0;
		CallableStatement cst = null;
		
		try {			
			String query = "{call PACK_CP_PDF.PRO_LABEL_PRN(?)}";
			cst = con.prepareCall(query);
			
			cst.setString(1, genGuiaNumber);
			
			cst.executeQuery();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertCtrlLabelPrn()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return insertCount;
	}
	private String getCadenaImpresion(Connection con, HttpServletRequest request, Global global, String rastreo, String rastreo_de_retorno) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String cadenaEtiqueta = "";
		Clob myClob = null;
		try {
			String query = getFuncionEti(con, global);
			//System.out.println("Funcion: " + query);
			pst = con.prepareStatement(query);
			pst.setString(1, rastreo);
			pst.setString(2, "N");
			pst.setString(3, "0");
			pst.setString(4, "A");
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				myClob = rs.getClob(1);
				cadenaEtiqueta = ClobToString(myClob);
			}
			//Validamos si existe acuse de retorno
			if(global.isAcuseXT()== true) {
				//Cerramos el Statement y el Resultset
				resources.closeResources(rs, pst);
				if(!rastreo_de_retorno.isEmpty()) {
					//Si se genero el rastreo correctamente, debemos obtener la cadena de impresion
					pst = con.prepareStatement(query);
					pst.setString(1, rastreo_de_retorno);
					pst.setString(2, "N");
					pst.setString(3, "0");
					pst.setString(4, "A");
					//Ejecutamos la consulta
					rs = pst.executeQuery();
					//Si obtenemos resultados debemos concatenar a la cadena de etiqueta el resultado
					if (rs.next()) {
						myClob = rs.getClob(1);
						cadenaEtiqueta += "\n" + ClobToString(myClob);
					}
					global.setTrackingNoGenRet(rastreo_de_retorno);
				}
			}
			
			new PrintFileExport(request.getSession().getServletContext().getRealPath("/")).writeToGuiaFile(request, cadenaEtiqueta, rastreo, getTimeStamp(con).getTime());
			//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("getCadenaImpresion()_cadenaEtiqueta INFORMACION PARA IMPRESION ").append(cadenaEtiqueta).toString());		
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCadenaImpresion()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return cadenaEtiqueta;
	}
    
	private String ClobToString(Clob cl) {
		BufferedReader br = null;
		if (cl == null)
			return "";
		StringBuffer strOut = new StringBuffer();
		String aux;
		try {
			br = new BufferedReader(cl.getCharacterStream());
			while ((aux = br.readLine()) != null) {
				strOut.append(aux).append("\n");
			}
			br.close();
			br = null;
		} catch (SQLException e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("ClobToString()SQLException_Error:").append(e).toString());
			e.printStackTrace();
		} catch (IOException e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("ClobToString()IOException_Error:").append(e).toString());
			e.printStackTrace();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("ClobToString()Exception_Error:").append(e).toString());
			e.printStackTrace();
		}
		finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("ClobToString()Exception_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return strOut.toString();
	}
	
	@SuppressWarnings("rawtypes")
	private String getFuncionEti(Connection con, Global global) {
		String funcion = "CP_ETIQUETA_ZPL";
		ArrayList result = null;
		
		try {
			//SELECT CP_ETIQUETA_DELIMITED(?) FROM DUAL. CADENA DELIMITADA
			//SELECT CP_ETIQUETA(?) FROM DUAL. CADENA ZPL PARA ENVIAR A PUERTO DE IMPRESION
			ConsultaParametros parametros = new ConsultaParametros();
			result = parametros.QryMdulTypeParm1(con, "WEB", "PRINTGUIAWS", global.getClientId());
			
			if (!result.isEmpty()) {
				funcion = ((ArrayList)result.get(0)).get(4).toString();				
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getFuncionEti()Exception_Error:").append(e).toString());
			e.printStackTrace();
		}
		return cnct.delete(0,cnct.length()).append("SELECT ").append(funcion).append("(?, ?, ?, ?) FROM DUAL").toString();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap getInfoClientId(Connection con, Global global) {
		HashMap result = new HashMap(4);
		String billSiteId = "";
		String billBrncId = "";
		String billBrncLoc = "IN";
		String query = "";
		String clientName = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		PreparedStatement pst2 = null;
		ResultSet rs2 = null;
		String continuar = "Y";
		
		try {
			String orignClientId = global.getClientId();
			String orgionBorderBranch = global.getOrigionBorderBranch();
			String destinationBorderBranch = global.getDestinationBorderBranch();
			
			query = "SELECT CM_ASGN_TO_SITE, CM_CLNT_NAME, CM_TAX_ID FROM SYS_CLNT_MSTR WHERE CM_CLNT_ID = ?";
			pst = con.prepareStatement(query);
			pst.setString(1, orignClientId);
			rs = pst.executeQuery();

			if (rs.next()) {
				continuar = "Y";
				billSiteId = rs.getString(1);
				clientName = rs.getString(2);
				billBrncId = getBillBrncId(con, billSiteId);

				/*
				 * valida envio entre fronteras para no traspasar si la sucursal de cobranza no
				 * es FRONTERA
				 */
				if (orgionBorderBranch.equals("BR") && destinationBorderBranch.equals("BR")) {
					query = "SELECT nvl(BM_BRNC_LOC_TYPE, 'IN') FROM SYS_BRNC_MSTR WHERE BM_BRNC_ID = ? GROUP BY BM_BRNC_LOC_TYPE";
					pst2 = con.prepareStatement(query);
					pst2.setString(1, billBrncId);
					rs2 = pst.executeQuery();
					if (rs2.next()) {
						billBrncLoc = rs2.getString(1);
						if (!billBrncLoc.equals("BR")) {
							continuar = "N";
						}
					}
				}
				result.put("continuar", continuar);
			} else {
				result.put("continuar", "N");
			}

			result.put("billSiteId", billSiteId);
			result.put("billBrncId", billBrncId);
			result.put("clientName", clientName);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getInfoClientId()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
			resources.closeResources(rs2, pst2);
		}
		return result;
	}
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap getInfoGroupClientId(Connection con, Global global) {//AAP18
		HashMap result = new HashMap(4);
		String billSiteId = "";
		String billBrncId = "";
		String billBrncLoc = "IN";//AAP20
		String query = "";
		String groupClientName = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		PreparedStatement pst2 = null;
		ResultSet rs2 = null;
		String asignSede = "0"; //
		boolean exceptClntId = false;//AAP22
		String continuar = "Y";//AAP20
		try {
			String groupClientId = global.getGroupClientId();//AAP22
			String orignClientId = global.getClientId();//AAP22
			String orgionBorderBranch = global.getOrigionBorderBranch();//AAP20
			String destinationBorderBranch = global.getDestinationBorderBranch();//AAP20
			
			ConsultaParametros cons = new ConsultaParametros();
			ArrayList temp = cons.QryMdulTypeParm1(con, "WEB", "ASSIGN_GPOCLNT", groupClientId);
			if (!temp.isEmpty()) {
				asignSede = temp.isEmpty() ? "0" : ((ArrayList) temp .get(0)).get(3).toString();// obteniendo PM_VLUE2_ID	
			}
			
			/*busca cliente origen por excepcion para no realizar asignacion de cliente sede en BILLs*///AAP22
			if (asignSede.equals("1")) {//AAP22
				exceptClntId = cons.QryParmCustomerGLP(con, orignClientId, "GPOCLNTSKIP");//AAP22
				
				if (exceptClntId) {//AAP22
					asignSede = "0";//AAP22
				}//AAP22
			}//AAP22
			
			// asignSede = 0. No realiza asignacion de cliente sede en cobranza
			// asignSede = 1. busca informacion de cliente sede para asignarlo a la cobranza directamente
			if (asignSede.equals("1")) {
				query = "SELECT CM_ASGN_TO_SITE, CM_CLNT_NAME, CM_TAX_ID FROM SYS_CLNT_MSTR WHERE CM_CLNT_ID = ?";

				pst = con.prepareStatement(query);

				pst.setString(1, groupClientId);
				
				rs = pst.executeQuery();

				if (rs.next()) {
					continuar = "Y";//AAP20
					billSiteId = rs.getString(1);
					groupClientName = rs.getString(2);
					billBrncId = getBillBrncId(con, billSiteId);
					
					/*valida envio entre fronteras para no traspasar si la sucursal de cobranza no es FRONTERA*/
					if (orgionBorderBranch.equals("BR") && destinationBorderBranch.equals("BR")) {//AAP20
						query = "SELECT nvl(BM_BRNC_LOC_TYPE, 'IN') FROM SYS_BRNC_MSTR WHERE BM_BRNC_ID = ? GROUP BY BM_BRNC_LOC_TYPE";//AAP20

						pst2 = con.prepareStatement(query);//AAP20

						pst2.setString(1, billBrncId);//AAP20
						
						rs2 = pst.executeQuery();//AAP20
						
						if (rs2.next()) {//AAP20
							billBrncLoc = rs2.getString(1);//AAP20
							
							if (!billBrncLoc.equals("BR")) {//AAP20
								continuar = "N";//AAP20
							}//AAP20
						}//AAP20
					}//AAP20
					
					result.put("continuar", continuar);
				} else {				
					result.put("continuar", "N");
				}	
			} else {
				result.put("continuar", "N");
			}
			
			result.put("billSiteId", billSiteId);
			result.put("billBrncId", billBrncId);
			result.put("groupClientName", groupClientName);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getInfoGroupClientId()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
			resources.closeResources(rs2, pst2);
		}
		return result;
	}
	
	/*OBTIENE SUCURSAL DE FACTURACION. CUANDO SE ENVIA FACTURARCION A CLIENTE TERCERO*///AAP08
	private String getBillBrncId(Connection con, String site) {//AAP08
		String query = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		String billBrncId = "";
		try {
			query = "SELECT PM_PARM_CODE2 FROM SYS_PARM_MSTR where PM_PARM_TYPE ='DEST_TRANS' AND SUBSTR(PM_PARM_CODE2,1,3) = ? GROUP BY PM_PARM_CODE2";

			pst = con.prepareStatement(query);
			pst.setString(1, site);
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				billBrncId = rs.getString(1) == null ? site + "01" : rs.getString(1);
			} else {
				billBrncId = site + "01";
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getBillBrncId()_Error:").append(e).toString());
			e.printStackTrace();
		} finally{
			resources.closeResources(rs, pst);
		}
		return billBrncId;
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
	
	private String getCompanyHead(Connection con, String clntId) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String companyHead = null;
		try {			
			String guiaQuery = "SELECT FUN_GET_CMPY_INVC(?,SYSDATE,'01','N',4) FROM DUAL";
			pst = con.prepareStatement(guiaQuery);
			pst.setString(1, clntId);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				companyHead = rs.getString(1);
			}

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCompanyHead()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return companyHead;
	}
	
	/*OBTIENE PARAMETROS PARA GENERAR LOS ITEM DE LA 2FA FACTURA*/
	private HashMap <String, ParamDTO>  getConfigItemFacturaBycompany(Connection con, String company) {// AAP08
		String query = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		HashMap <String, ParamDTO> list = null;
		try {
			query = "SELECT PM_PARM_CODE1, PM_VLUE1_DESC, PM_VLUE1_ID FROM SYS_PARM_MSTR WHERE PM_MDUL_ID='ADM' AND PM_PARM_TYPE = 'INVOICE_SERVICE' and PM_VLUE2_DESC = ?";

			pst = con.prepareStatement(query);
			pst.setString(1, company);

			rs = pst.executeQuery();
			ParamDTO par = null;
			while (rs.next()) {
				if (list == null) {
					list = new HashMap <String, ParamDTO> ();
				}
				par = new ParamDTO();
				par.setParmCode1(rs.getString(1));
				par.setVlue1Desc(rs.getString(2));
				par.setVlue1Id(rs.getString(3));
				list.put(par.getParmCode1(), par);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getConfigItemFacturaBycompany()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return list;
	}
	
	/*OBTIENE INFO DEL CLIENTE PARA LA 2FA FACTURA*/
	private ParamDTO  getBillCustomerInvoice(Connection con, String company) {// AAP08
		String query = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		ParamDTO par = null;
		try {
			query = "SELECT PM_PARM_CODE1, PM_VLUE1_DESC, PM_VLUE1_ID,PM_PARM_CODE2 FROM SYS_PARM_MSTR WHERE PM_MDUL_ID='ADM' AND PM_PARM_TYPE = 'CLIENT_INVOICE' and PM_VLUE2_DESC = ?";

			pst = con.prepareStatement(query);
			pst.setString(1, company);

			rs = pst.executeQuery();
			if (rs.next()) {
				par = new ParamDTO();
				par.setParmCode1(rs.getString(1));
				par.setVlue1Desc(rs.getString(2));
				par.setVlue1Id(rs.getString(3));
				par.setParmCode2(rs.getString(4));
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getBillCustomerInvoice()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return par;
	}
	
	//Insercion de los registros para la 2da factura
	private void insertServicesRecordRinv(Connection con, HttpSession session, Timestamp sysDate, 
			Services services, String genGuiaNumber, String companyId, HashMap <String, ParamDTO> servicesCompanyNueva) {
		CallableStatement cst = null;
		Global global = (Global) session.getAttribute("sGlobal");
		String query = "{ call pack_web.PRO_INSRT_WEB_BOK_SRVC_RINV(?,?,?,?,?,?,?,?,?,?,?,?)}";
		try {
			if (servicesCompanyNueva != null && servicesCompanyNueva.containsKey(services.GS_SRVC_ID)) {
//				ParamDTO dto = servicesCompanyNueva.get(services.GS_SRVC_ID);
//				Integer porcentInvc = Integer.valueOf(dto.getVlue1Id());
//				BigDecimal bigSubTotal = new BigDecimal(services.GS_SUB_TOTL).setScale(2, BigDecimal.ROUND_HALF_EVEN);
//				BigDecimal bigPorcentaje = new BigDecimal(porcentInvc).divide(new BigDecimal(100));
//				bigSubTotal = bigSubTotal.multiply(bigPorcentaje).setScale(2, BigDecimal.ROUND_HALF_EVEN);
//				services.GS_SUB_TOTL = bigSubTotal.toString();
//				BigDecimal bigDesc = BigDecimal.ZERO;
//				if (services.GS_DISC != null && services.GS_DISC.length() > 0) {
//					bigDesc = new BigDecimal(services.GS_DISC).setScale(2, BigDecimal.ROUND_HALF_EVEN);
//					bigDesc = bigDesc.multiply(bigPorcentaje).setScale(2, BigDecimal.ROUND_HALF_EVEN);
//					services.GS_DISC = bigDesc.toString();
//				} else {
//					services.GS_DISC = "0";
//				}
//				String servicio=services.GS_SRVC_ID;
//				if( services.GS_SRVC_ID.equalsIgnoreCase("SHP-E")) {
//					servicio="ENVELOPES";
//				} else if(services.GS_SRVC_ID.equalsIgnoreCase("SHP-G")) {
//					servicio="PACKETS";
//					
//				}
//				String tax[] = getTax(con, global, servicio, bigSubTotal.subtract(bigDesc).toString());
//				services.GS_TAX = tax[0];
//				if (services.GS_TAX_RET != null && services.GS_TAX_RET.length() > 0
//						&& Double.parseDouble(services.GS_TAX_RET) > 0) {
//					services.GS_TAX_RET = tax[1];
//				}
//				cst = con.prepareCall(query);
//				// AAP//AccessLog.Log(query);
//				cst.setString(1, genGuiaNumber);
//				// AAP//AccessLog.Log(genGuiaNumber);
//				cst.setString(2, services.GS_SRVC_ID);
//				// AAP//AccessLog.Log(services.GS_SRVC_ID);
//				cst.setDouble(3, Double.parseDouble(services.GS_DISC));
//				// AAP//AccessLog.Log(services.GS_DISC);
//				cst.setDouble(4, Double.parseDouble(services.GS_TAX));
//				// AAP//AccessLog.Log(services.GS_TAX);
//				cst.setDouble(5, Double.parseDouble(services.GS_TAX_RET));
//				// AAP//AccessLog.Log(services.GS_TAX_RET);
//				// AAP//AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertServicesRecord()_").append("before
//				// insertion the amount is").append(services.GS_SUB_TOTL).toString());
//				cst.setDouble(6, Double.parseDouble(services.GS_SUB_TOTL));
//				cst.setTimestamp(7, sysDate);
//				// AAP//AccessLog.Log(sysDate + "");
//				cst.setString(8, global.clientId);
//				// AAP//AccessLog.Log(global.clientId);
//				cst.setTimestamp(9, sysDate);
//				// AAP//AccessLog.Log(sysDate + "");
//				cst.setString(10, global.clientId);
//				// AAP//AccessLog.Log(global.clientId);
//				cst.setString(11, companyId);
//				// JSA//AccessLog.Log(companyId);
//				cst.setInt(12, porcentInvc);
//				// JSA//AccessLog.Log(porcentInvc);
//				cst.executeQuery();
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertServicesRecordRinv()_").append( " BEFORE SERVICE SPECIAL").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		//AAP//AccessLog .Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertServicesRecord()_").append("****************************** BOOK_GUIA_SERVICES INSERTION SUCCESS *************************").toString());
	}

	private void insertBokInvcHeadRinv(Connection con, HttpSession session, Timestamp sysDate, String genGuiaNumber, ParamDTO servicesCompanyNueva) {
		CallableStatement cst = null;
		Global global = (Global) session.getAttribute("sGlobal");
		String query = "{ call pack_web.PRO_INSRT_BOK_INVC_HEAD_RINV(?,?,?,?,?,?,?)}";
		try {
			if (servicesCompanyNueva != null) {
//				cst = con.prepareCall(query);
//				// AAP//AccessLog.Log(query);
//				cst.setString(1, genGuiaNumber);
//				// AAP//AccessLog.Log(genGuiaNumber);
//				cst.setString(2, servicesCompanyNueva.getParmCode2());
//				// AAP//AccessLog.Log(servicesCompanyNueva.getParmCode1());
//				cst.setString(3, servicesCompanyNueva.getParmCode1());
//				cst.setTimestamp(4, sysDate);
//				// AAP//AccessLog.Log(sysDate + "");
//				cst.setString(5, global.clientId);
//				// AAP//AccessLog.Log(global.clientId);
//				cst.setTimestamp(6, sysDate);
//				// AAP//AccessLog.Log(sysDate + "");
//				cst.setString(7, global.clientId);
//				// AAP//AccessLog.Log(global.clientId);
//				cst.executeQuery();
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertBokInvcHeadRinv()_").append( " BEFORE SERVICE SPECIAL").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		//AAP//AccessLog .Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertServicesRecord()_").append("****************************** BOOK_GUIA_SERVICES INSERTION SUCCESS *************************").toString());
	}
	
	private String[] getTax(Connection con, Global global, String referenceServiceId, String taxableAmount) {
		CallableStatement cst = null;
		String tax[] = new String[2];
		try {
			String taxQuery = "{call pack_sipweb.PRO_CALC_TAX_AMT(?,?,?,?,?)}";
			
			//boolean isOrgionBorderBranch = global.isOrigionBorderBranch;//AAP20
			//boolean isDestinationBorderBranch = global.isDestinationBorderBranch;//AAP20
			String orgionBorderBranch = global.getOrigionBorderBranch();//AAP20
			String destinationBorderBranch = global.getDestinationBorderBranch();//AAP20
			//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getTax()_").append(global.clientId).append("-------- INSIDE GET TAX METHOD BEFORE PREPARING THE CALLABEL STATEMENT ").toString());
			cst = con.prepareCall(taxQuery);
			//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getTax()_").append(global.clientId).append("PE ------- AFTER PREPARING THE CALLABLE STATEMENT ").toString());

			//if (isOrgionBorderBranch && !isDestinationBorderBranch) {//AAP20
			if (orgionBorderBranch.equals("BR") && !destinationBorderBranch.equals("BR")) {//AAP20
				cst.setString(1, global.destinationBranchId);
				//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getTax()_").append(global.clientId).append("get tax global.destinationBranchId ").append(global.destinationBranchId).toString());
			} else {
				cst.setString(1, global.assignedBranch);
				//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getTax()_").append(global.clientId).append("get tax global.assignedBranch ").append(global.assignedBranch).toString());
			}

			//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getTax()_").append(global.clientId).append("get tax referenceServiceId ").append(referenceServiceId).toString());
			//AAP//AccessLog.Log(conct.delete(0,conct.length()).append(msgAvi).append("getTax()_").append(global.clientId).append("get tax taxableAmount ").append(taxableAmount).toString());

			cst.setString(2, referenceServiceId);
			cst.setDouble(3, Double.parseDouble(taxableAmount));
			cst.registerOutParameter(4, Types.NUMERIC);
			cst.registerOutParameter(5, Types.NUMERIC);
			cst.executeQuery();

			DecimalFormat df = new DecimalFormat("0.00");
			
			tax[0] = df.format(cst.getDouble(4));
			tax[1] = df.format(cst.getDouble(5));
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getTax()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return tax;
	}
//	//Obtiene el addrCode de cliente generico para cuando es FXC cambio por separacion de negocios.
//	private Map<String, String> getAddrCodeCustomerGenericFXC(Connection con) {//JSA02
//		String query = "";
//		PreparedStatement pst = null;
//		ResultSet rs = null;
//		Map<String, String> datos = new HashMap<String, String>();
//		try {
//			query = "select addr.Am_Enty_Id, clnt.Cm_Clnt_Name, addr.AM_ADDR_CODE from sys_addr_mstr addr inner join sys_clnt_mstr clnt on Addr.Am_Enty_Id = clnt.Cm_Clnt_Id where addr.Am_Enty_Id = FUN_GET_CLNT_PG and addr.Am_Defa_Flag = 'Y'";
//			pst = con.prepareStatement(query);
//			rs = pst.executeQuery();
//			if (rs.next()) {
//				datos.put("clntID", rs.getString(1));
//				datos.put("clntName", rs.getString(2));
//				datos.put("addrCode", rs.getString(3));
//			}
//		} catch (Exception e) {
//			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getAddrCodeCustomerGenericFXC()_Error:").append(e).toString());
//			e.printStackTrace();
//		} finally{
//			resources.closeResources(rs, pst);
//		}
//		return datos;
//	}
	private void insertBokHistErrorPediNumb(Connection con, String pediNumb, String guiaNo) {
		PreparedStatement pst = null;
		try {
			String insertQuery = "INSERT INTO BOK_HIST_ERROR_PEDI_NUMB(GUIA_NO,PEDI_NUMB) VALUES(?,?)";

			pst = con.prepareStatement(insertQuery);
			pst.setString(1, guiaNo);
			pst.setString(2, pediNumb);
			
			pst.executeUpdate();		
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBokHistErrorPediNumb()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		
	}	
}
