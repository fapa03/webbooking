/**
 * @author: Murugesapandian T
 * Fecha de Creaci?n: 13-May-2006
 * Compa??a: KUMARAN.
 * Descripci?n del programa: This is the Action Class for Client destination Entry
 * FileName: ClientDestinationEntryAction.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha: 26/07/2012
 * Descripci?n: Se agreg? validacion de cobertura. 
 * Se modific? metodo getError() para hacerlo reutilizable 
 * ------------------------------------------------------------------
 * Clave: AAP02
 * Autor: Abraham Daniel Arjona Peraza.
 * Fecha: 01/03/2013
 * Descripci?n: 
 * Se modific? obtencion de secuencia para registrar cliente. 
 * Ahora se obtiene cada que se ejecute el m?todo saveRecords()
 * ------------------------------------------------------------------
 * Clave: AAP03
 * Autor: Abraham Daniel Arjona Peraza.
 * Fecha: 03/07/2013
 * Descripci?n: 
 * Se agregaron variables para registro de rfc, tipo de cliente (I,C) 
 * y el cliente destino acepta flete por cobrar cuando el cliente origen 
 * tiene prendida la bandera de envios flete por cobrar.  
 * se agreg? metodo getClientInfo() para obtener informacion del cliente destino.
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;
import mx.com.paquetexpress.dto.CatClntFiscDTO;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bean.ConsultaParametros;
import bean.Global;
import bean.Resources;
import beanForm.ClientDestinationEntryForm;
import beanUtil.ConnectDB;

public class ClientDestinationEntryAction extends Action {
	private static final String SUCCESS = "success";
	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	
	@SuppressWarnings("rawtypes")
	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		ClientDestinationEntryForm clientDestEntryForm = (ClientDestinationEntryForm) form;
		ClientDestinationEntryForm clientDestEntryForm1 = (ClientDestinationEntryForm) form;
		String mode = clientDestEntryForm.getMode();
		String path = SUCCESS;
		Connection con = null;
		@SuppressWarnings("unused")
		String clientId = null;
		
		// added by Murugesapandian 19/05/06 reg. generate sequence numer
		HttpSession session = request.getSession(true);
		// added by Murugesapandian 19/05/06 reg. generate sequence numer

		// Added By Sam[20-06-2006]
		if (session == null || session.isNew()) {
			return mapping.findForward("nosession");
		}

		try {
			con = ConnectDB.getConnection();
			con.setAutoCommit(false);
			clientId = (String) session.getAttribute("sClientId");
			clientDestEntryForm.setErrorMessages("");
			Global global = (Global) session.getAttribute("sGlobal");
						
			//en caso de que la variable AssignedBranch contienga un valor de BrncIdWeb cambiara
			if ( !(global.getAssignedBranch() == null || global.getAssignedBranch().equals(""))) {
				global.setBrncIdWeb(global.getAssignedBranch());
			}
			
			if (request.getParameter("type") != null &&
					request.getParameter("type").equalsIgnoreCase("cliententry")) {
				clientDestEntryForm.setFastClntCap(true);
			}
			
			 if ("saveAndBack".equalsIgnoreCase(mode)) {
				boolean flag = getErrorMessage(con, clientDestEntryForm, global);
				
				/* Validaci?n RFC CFDI 4.0 */
				String rfcFull = clientDestEntryForm.getRfc1() + clientDestEntryForm.getRfc2() + clientDestEntryForm.getRfc3();
				if (rfcFull != null && !rfcFull.isEmpty()) {
					if (rfcFull.equalsIgnoreCase("XAXX010101000")) {
						clientDestEntryForm.setGenericRFC("Y");
					}
					clientDestEntryForm.setValidRFC("Y");

					/* Obtiene valores default cfdi si rfc es generico o invalido */
					if (rfcFull.equalsIgnoreCase("XAXX010101000") || 
							clientDestEntryForm.getValidRFC().equalsIgnoreCase("N")) {
						fiscalDefaultValues(con, clientDestEntryForm1);
					}
				}
				
				if (flag && clientDestEntryForm.getPartialUpdate().trim().length() == 0) { 
					path = "error_page";
				} else {
					/*para error parcial, actualizar solo numero interno*/
					if (flag) {
						path = "error_page";
					}
					if (((clientDestEntryForm.getCodigoCliente() != null) && (clientDestEntryForm.getCodigoCliente().length() > 0))) {
						PreparedStatement psmt = null;
						ResultSet rs = null;
						String clntid = null;
						int stest = 0;
						clntid = clientDestEntryForm.getCodigoCliente();
						String query = "select count(*) from sys_clnt_mstr where cm_clnt_id = ? and rownum = 1";

						try {
							psmt = con.prepareStatement(query);
							psmt.setString(1,clntid);
							rs = psmt.executeQuery();
							while (rs.next()) {
								stest = rs.getInt(1);
							}
							if (stest == 0) {
								saveRecords(clientDestEntryForm, con, session);
								clientDestEntryForm.setErrorMessages("");
								con.commit();
							} else if (stest == 1) {
								if (clientDestEntryForm.getPartialUpdate().equals("Y")) {
									partialUpdateRecords(clientDestEntryForm, con, session);
								} else {
									updateRecords(clientDestEntryForm, con, session);
								}
							}
						} catch (Exception exe) {
							AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("perform()Error1:").append(exe).toString());
							exe.printStackTrace();
						} finally {
							resources.closeResources(rs, psmt);						
						}
					} else {
						// save Destination client entry details to database
						clientDestEntryForm.setErrorMessages("");
						saveRecords(clientDestEntryForm, con, session);
						con.commit();												
					}
					path = SUCCESS;
				}
			} else if ("loaddetail".equalsIgnoreCase(mode)) {
				boolean buscarNumInt = false;
				boolean buscar = false;				
				String clientDestId = "";
				String clientDestTemp = "";
				String clientDestPQId = clientDestEntryForm.getCodigoInt() != null ? clientDestEntryForm.getCodigoInt().toUpperCase() : "";	
				String rfc = "";//AAP03
				String rfc1 = "";//AAP03
				String rfc2 = "";//AAP03
				String rfc3 = "";//AAP03
				String tipoCliente = "";//AAP03
				
				ConsultaParametros consulta = new ConsultaParametros();
				
				/*obtiene numero de paquetexpress con numero interno capturado en pantalla*/
				if (clientDestPQId.length() != 0) {
					clientDestTemp = getClienteDestino(con, global, clientDestPQId);	
					/*asigna el cliente destino obtenido con el cliente interno, a la variable clientDestId 
					 * para realizar busqueda en sys_clnt_mstr*/
					if (clientDestTemp.length() > 1) {
						clientDestId = clientDestTemp.split("\\|")[0];
						clientDestEntryForm.setCodigoInt(clientDestTemp.split("\\|")[1]);
					}
				}
				
				/*si no obtuvo cliente paquetexpress (viene de buscar por numero interno) vuelve a asignar el codigo de cliente paquetexpress
				 * para validar si esta con datos para realizar b?squeda*/
				if (clientDestId != null && clientDestId.length()==0) {
					clientDestId = clientDestEntryForm.getCodigoCliente();
					
					/*si no viene capturado codigo de paquetexpress, envia mensaje de que no existe codigo interno... si no, activa bandera
					 * para realizar busqueda en sys_clnt_mstr*/
					if (clientDestId != null && clientDestId.length() == 0) {
						clientDestEntryForm.setErrorMessages("Por favor, capture un número interno de cliente VALIDO para modificar.");
						clientDestEntryForm = clientDestEntryForm1;
					} else {
						buscar = true;
						buscarNumInt = true;//se activa la bandera para buscar numero interno con cliente destino... el capturado en pantalla no es valido
					}
				} else {
					buscar = true;
				}
				
				if (buscar) {
					/*obtiene numero interno de cliente con cliente de paquetexpress*/
					if (buscarNumInt) {
						ArrayList datos = getClienteDestinoInt(con, global, clientDestId);
						
						/*si no obtiene registros, el cliente destino paquete express no existe en WEB_ORGN_DEST_CLNT para el cliente origen*/
						if (datos.isEmpty()) {
							clientDestEntryForm.setErrorMessages("Por favor, capture un número Paquetexpress de cliente VALIDO para modificar.");
							clientDestEntryForm = clientDestEntryForm1;
							buscar = false;// desactiva bandera para no realizar busqueda en sys_clnt_mstr
						} else {
							clientDestEntryForm.setCodigoCliente(datos.get(0).toString());
							clientDestEntryForm.setCodigoInt(datos.get(1).toString());
						}
					}
					
					if (buscar) {
						String clientDestName = getClientValues(clientDestEntryForm, con, clientDestId);
						if (clientDestName.length()==0) {
							clientDestEntryForm.setErrorMessages("No existe cliente en Catálogo. Capture un número Paquetexpress de cliente VALIDO para modificar.");
							clientDestEntryForm = clientDestEntryForm1;
						} else {//AAP03
							ArrayList result  = getClientInfo(con, clientDestId);//AAP03
							if (!result.isEmpty()) {//AAP03
								rfc = result.get(0).toString();//AAP03
								tipoCliente = result.get(1).toString();//AAP03
								clientDestEntryForm.setTipoCliente(tipoCliente);//AAP03
								
								/* Obtiene configuraci?n CFDI de cliente */
								clientDestEntryForm.setRegimenFiscalId(result.get(3).toString() != null ? result.get(3).toString() : "");
								clientDestEntryForm.setUsoCfdiId(result.get(4).toString() != null ? result.get(4).toString() : "");
								
								if (clientDestEntryForm.getRegimenFiscalId() != null &&
										!clientDestEntryForm.getRegimenFiscalId().isEmpty() &&
										!clientDestEntryForm.getRegimenFiscalId().equalsIgnoreCase("null")) {
									/* Obtiene la descripci?n del Regimen fiscal y Uso CFDI*/
									String regimFiscalDes = ((ArrayList)((ArrayList) consulta.QryMdulTypeVlue(con, "FIN", "REGIM_FISCAL", "parm1", clientDestEntryForm.getRegimenFiscalId())).get(0)).get(2).toString();
									String usoCfdiDes = ((ArrayList)((ArrayList) consulta.QryMdulTypeMul(con, "FIN", "USO_CFDI", "parm", clientDestEntryForm.getUsoCfdiId(), clientDestEntryForm.getRegimenFiscalId())).get(0)).get(2).toString();
									
									clientDestEntryForm.setRegimenFiscalDes(clientDestEntryForm.getRegimenFiscalId()+" - "+regimFiscalDes);
									clientDestEntryForm.setUsoCfdiDes(clientDestEntryForm.getUsoCfdiId()+" - "+usoCfdiDes);
								}
								
								if (!rfc.isEmpty()) {//AAP03
									if (rfc.contains("-")) {
										rfc1 =  rfc.substring(0,rfc.indexOf('-') );//AAP03
										rfc  =  rfc.substring(rfc.indexOf('-')+1 );//A4P03
										rfc2 =  rfc.substring(0,rfc.indexOf('-') );//AAP03
										rfc  =  rfc.substring(rfc.indexOf('-')+1 );//AAP03
										rfc3 =  rfc;//AAP03
									}else {
										String[] formatedRFC = formatRFC(tipoCliente, rfc).split("-");
										rfc1 = formatedRFC[0];
										rfc2 = formatedRFC[1];
										rfc3 = formatedRFC[2];
									}
									
									String fullRFC = rfc1 + "-" + rfc2 + "-" + rfc3;
									
									/* Obtiene valores para RFC Generico */
									if ((fullRFC).equalsIgnoreCase("XAXX010101000") || (fullRFC).equalsIgnoreCase("XAXX-010101-000")) {
										clientDestEntryForm.setGenericRFC("Y");
										rfc1 = "XAXX";
										rfc2 = "010101";
										rfc3 = "000";
										fiscalDefaultValues(con, clientDestEntryForm);
									}else {
										clientDestEntryForm.setValidRFC("Y");
									}
									clientDestEntryForm.setRfc1(rfc1);//AAP03
									clientDestEntryForm.setRfc2(rfc2);//AAP03
									clientDestEntryForm.setRfc3(rfc3);//AAP03
									clientDestEntryForm.setCurrRFC(fullRFC);
									clientDestEntryForm.setCurrValFlag("clientId");
									CatClntFiscDTO datosFiscales = getDatosFiscales(con, fullRFC);
									clientDestEntryForm.setDatosFiscalesTmpString(datosFiscales.toString());
									clientDestEntryForm.setDatosFiscalesString(new CatClntFiscDTO().toString());
									clientDestEntryForm.setValidFiscal(datosFiscales.getValidFlag());
									clientDestEntryForm.setCurrValFlag(datosFiscales.getValidFlag());
									String credAvail = "N";
									if (Boolean.FALSE.equals(getSkipRFC(con, fullRFC))){
										credAvail = getCredAvail(con, fullRFC);
									}
									clientDestEntryForm.setTieneCredito(credAvail);
								}//AAP03
							}//AAP03
						}//AAP03
					}
				}
			} else if ("getDescBranch".equalsIgnoreCase(mode)) {
				clientDestEntryForm.setAssignedToBranchRef(getBranchDesc(con, clientDestEntryForm.getAssignedToBranch()));
				path = SUCCESS;				
			} else if ("main_page".equalsIgnoreCase(mode)) {
				path = "main_page";
			} else if ("validateRFC".equalsIgnoreCase(mode) || (mode != null && mode.contains("validateRFCFiscal")) ||
					(mode != null && mode.contains("Insertar"))) {
				String rfc = (clientDestEntryForm.getRfc1() + clientDestEntryForm.getRfc2() + clientDestEntryForm.getRfc3()).trim();
				clientDestEntryForm.setValidRFC("Y");
				path=SUCCESS;
				if (rfc.equalsIgnoreCase("XAXX010101000")) {
					clientDestEntryForm.setGenericRFC("Y");
					fiscalDefaultValues(con, clientDestEntryForm);
				}
				//Valida si el RFC pertenece a cliente con cr�dito
				//Se vuelven a obtener los datos fiscales al cambiar el rfc
				
				String rfcCompleto="";
				if(mode.contains("Fiscal")){
					rfcCompleto = mode.split("-")[1];
				}else {
					rfcCompleto = rfc;
				}
				rfcCompleto = formatRFC(clientDestEntryForm.getTipoCliente(), rfcCompleto).toUpperCase();
				String tieneCredito = getCredAvail(con, rfcCompleto);
				clientDestEntryForm.setTieneCredito(tieneCredito);
				CatClntFiscDTO datosFiscales = getDatosFiscales(con, rfcCompleto);
				clientDestEntryForm.setDatosFiscalesTmpString(datosFiscales.toString());
				clientDestEntryForm.setDatosFiscalesString(new CatClntFiscDTO().toString());
				clientDestEntryForm.setValidFiscal(datosFiscales.getValidFlag());
				
			}
			getLabelEnteraDelClient(clientDestEntryForm, con);
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("perform()Error3:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.cerrarConexion(con);			
		}
		return (mapping.findForward(path));
	}
	
	// Method by amal
	private String getClientValues(ClientDestinationEntryForm clientDestEntryForm, Connection con, String client) {
		CallableStatement cst = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		String email = "";
		String cityname = "";
		String colony = "";
		String streetname = "";
		String drnr = "";
		String telefono = "";
		String celular = "";
		String clientname = "";
		String municipal = "";
		String pais = "";
		String estada = "";
		String zone = "";
		String postal = "";
		String branchid = "";
		String branchname = "";
		String eadcheck = "";
		String geLevl = "";
		String geType = "";
		String geCode = "";
		String postalcode = "";
		String postaltype = "";
		String postallevel = "";
		try {
			cst = con.prepareCall("{ call pack_web.PRO_POST_ADDRQ(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

			cst.setString(1, client);
			cst.registerOutParameter(2, Types.VARCHAR);
			cst.registerOutParameter(3, Types.VARCHAR);
			cst.registerOutParameter(4, Types.VARCHAR);
			cst.registerOutParameter(5, Types.VARCHAR);
			cst.registerOutParameter(6, Types.VARCHAR);
			cst.registerOutParameter(7, Types.VARCHAR);
			cst.registerOutParameter(8, Types.VARCHAR);
			cst.registerOutParameter(9, Types.VARCHAR);
			cst.registerOutParameter(10, Types.VARCHAR);
			cst.registerOutParameter(11, Types.VARCHAR);
			cst.registerOutParameter(12, Types.VARCHAR);
			cst.registerOutParameter(13, Types.VARCHAR);
			cst.registerOutParameter(14, Types.VARCHAR);
			cst.registerOutParameter(15, Types.VARCHAR);
			cst.registerOutParameter(16, Types.VARCHAR);
			cst.registerOutParameter(17, Types.VARCHAR);
			cst.registerOutParameter(18, Types.VARCHAR);
			cst.registerOutParameter(19, Types.VARCHAR);
			cst.registerOutParameter(20, Types.VARCHAR);
			cst.registerOutParameter(21, Types.VARCHAR);
			cst.registerOutParameter(22, Types.VARCHAR);
			cst.registerOutParameter(23, Types.VARCHAR);			

			cst.executeQuery();			
			
			postal = (cst.getString(2) != null ? cst.getString(2) : "");
			colony = (cst.getString(3) != null ? cst.getString(3) : "");
			municipal = (cst.getString(4) != null ? cst.getString(4) : "");
			cityname = (cst.getString(5) != null ? cst.getString(5) : "");
			estada = (cst.getString(6) != null ? cst.getString(6) : "");
			zone = (cst.getString(7) != null ? cst.getString(7) : "");
			pais = (cst.getString(8) != null ? cst.getString(8) : "");
			clientname = (cst.getString(9) != null ? cst.getString(9) : "");
			branchid = (cst.getString(10) != null ? cst.getString(10) : "");
			telefono = (cst.getString(11) != null ? cst.getString(11) : "");
			streetname = (cst.getString(12) != null ? cst.getString(12) : "");
			drnr = (cst.getString(13) != null ? cst.getString(13) : "");
			branchname = (cst.getString(14) != null ? cst.getString(14) : "");
			eadcheck = (cst.getString(15) != null ? cst.getString(15) : "");
			geLevl = (cst.getString(16) != null ? cst.getString(16) : "");
			geType = (cst.getString(17) != null ? cst.getString(17) : "");
			geCode = (cst.getString(18) != null ? cst.getString(18) : "");
			postallevel = (cst.getString(19) != null ? cst.getString(19) : "");
			postaltype = (cst.getString(20) != null ? cst.getString(20) : "");
			postalcode = (cst.getString(21) != null ? cst.getString(21) : "");			
			email = (cst.getString(22) != null ? cst.getString(22) : "");
			celular = (cst.getString(23) != null ? cst.getString(23) : "");
			
			if (clientname.length() > 0) {//AAP04
				clientDestEntryForm.setCiudad(cityname);// 1
				clientDestEntryForm.setAssignedToBranch(branchid);// 2
				clientDestEntryForm.setSucursal(branchid);// 3
				clientDestEntryForm.setAssignedToBranchRef(branchname);// 4
				clientDestEntryForm.setSucursalRef(branchname);// 5
				clientDestEntryForm.setCalle(streetname);// 6
				clientDestEntryForm.setCodigoPostal(postal);// 7
				clientDestEntryForm.setColonia(colony);// 8
				clientDestEntryForm.setEstado(estada);// 9
				clientDestEntryForm.setMunicipio(municipal);// 10			
				clientDestEntryForm.setNombre(clientname);// 11			
				clientDestEntryForm.setNumero(drnr);// 12
				clientDestEntryForm.setPais(pais);// 13
				clientDestEntryForm.setTelefono(telefono);// 14
				clientDestEntryForm.setZona(zone);// 15
				clientDestEntryForm.setEadCheck(eadcheck);// 16
				clientDestEntryForm.setEad(eadcheck);// 17
				clientDestEntryForm.setGeLevl(geLevl);// 18
				clientDestEntryForm.setGeCode(geCode);// 19
				clientDestEntryForm.setGeType(geType);// 20
				clientDestEntryForm.setLevel(postallevel);// 21
				clientDestEntryForm.setCode(postalcode);// 22
				clientDestEntryForm.setType(postaltype);// 23			
				clientDestEntryForm.setEmail(email);// 24		
				clientDestEntryForm.setCodigoCliente(client);// 25
				clientDestEntryForm.setCelular(celular);// 26
				
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getClientValues()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);	
			resources.cerrarPreparedStatement(pst);
		}
		return clientname;
	}
	
	private void generatSeqNum(ClientDestinationEntryForm clientDestEntryForm,
			String branchName, String guiaSeq) {

		CallableStatement cst = null;
		Connection con = null;
		String guiaNo = null;
		try {
			con = ConnectDB.getConnection();

			cst = con.prepareCall("{ call PACK_SIPWEB.GEN_SEQN_NUM(?,?,?,?) }");

			cst.setString(1, branchName);
			cst.setString(2, guiaSeq);
			cst.registerOutParameter(3, Types.VARCHAR);
			cst.registerOutParameter(4, Types.VARCHAR);

			cst.executeQuery();

			guiaNo = (cst.getString(3) != null ? cst.getString(3) : "");
			// To Set the Guia Number
			clientDestEntryForm.setCodigoClienteHid(guiaNo);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("generatSeqNum()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
			resources.cerrarConexion(con);
		}
	}
	// This method is used to get Postal Details
	private void saveRecords(ClientDestinationEntryForm clientDestEntryForm,
			Connection con, HttpSession session) {
		String clientId = (String) session.getAttribute("sClientId");

		try {
			Global global = (Global) session.getAttribute("sGlobal");// AAP03
			String branchId = global.getBrncIdWeb();// AAP02
			generatSeqNum(clientDestEntryForm, branchId, "CLIENT");// AAP02
			String assignedToBranch = clientDestEntryForm.getAssignedToBranch();
			String sucursal = assignedToBranch;
			String rfc = clientDestEntryForm.getRfc1() + "-" + clientDestEntryForm.getRfc2() + "-"
					+ clientDestEntryForm.getRfc3();
			String siteId = getSiteId(con, sucursal);
			String eadCheck = clientDestEntryForm.getEadCheck();

			insrtSysAddrMstr(con, clientDestEntryForm, clientId, siteId, global);
			insertSysClntMstr(con, clientDestEntryForm, siteId, global);// AAP03///AAP04
			insrtOrUpdtSysCatClnt(con, global, clientDestEntryForm, rfc);
			if (clientDestEntryForm.getErrorMessages().contains("Datos Fiscales No")
					|| clientDestEntryForm.getErrorMessages().equalsIgnoreCase("Datos Fiscales Incorrectos")
					|| (clientDestEntryForm.getValidFiscal() == null 
					|| clientDestEntryForm.getValidFiscal().equalsIgnoreCase("N"))) {
				clientDestEntryForm.setTipoCliente("I");
				if(clientDestEntryForm.getValidFiscal().equalsIgnoreCase("N") && clientDestEntryForm.getErrorMessages().isEmpty()) {
					clientDestEntryForm.setErrorMessages("Datos Fiscales Capturados Incorrectos.");
				}
				updtSysclntMstr(con, clientDestEntryForm, eadCheck, siteId, clientId, global, "XAXX-010101-000");
			}
			insertWebOrginDestClient(con, clientDestEntryForm, siteId, global);// AAP04

		} catch (Exception e) {
			AccessLog.Log(
					cnct.delete(0, cnct.length()).append(msgError).append("saveRecords()Error:").append(e).toString());
			e.printStackTrace();
		}
	}

	private void insrtSysAddrMstr(Connection con, ClientDestinationEntryForm clientDestEntryForm, String clientId,
			String siteId, Global global) {
		PreparedStatement psmt = null;
		/*
		 * obtiene sucursal OCURRE por excepcion.
		 * Si existe la configuracion, la variable trae la sucursal, si no... se inserta
		 * vacio
		 */
		String exceptBrnc = getBrncOcuExcept(con, clientId, siteId);
		String telefono = clientDestEntryForm.getTelefono().trim();
		String celular = clientDestEntryForm.getCelular().trim();
		celular = (celular != null && celular.length() > 0 && celular.matches("\\d+")) ? celular : null;
		String calle = clientDestEntryForm.getCalle().trim();
		String numero = clientDestEntryForm.getNumero().trim();
		String codigoClienteHid = clientDestEntryForm.getCodigoClienteHid();
		String email = clientDestEntryForm.getEmail();
		String geLevl = clientDestEntryForm.getGeLevl();
		String geType = clientDestEntryForm.getGeType();
		String geCode = clientDestEntryForm.getGeCode();
		String clientName = getClientName(con, clientId);
		String insertQuery = cnct.delete(0, cnct.length())
				.append("INSERT INTO sys_addr_mstr (am_phno1, am_strt_name, am_drnr, am_pe_site_id, am_addr_code, ")
				.append("am_addr_styp, am_addr_type, am_enty_id, am_gety_levl, am_gety_type, am_gety_code, am_addr_id, am_defa_flag,")
				.append("am_addr_defn_type, am_enty_desc, crtd_on, crtd_by, mdfd_on, mdfd_by, AM_MAIL_ID, AM_BRNC_DLY, AM_MBNO)")
				.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, ?, sysdate, ?, ?, ?, ?)")
				.toString();
		try {
			String seqNo = getSeqNo(con);
			psmt = con.prepareStatement(insertQuery);
			psmt.setString(1, telefono == null ? "" : telefono.toUpperCase());
			psmt.setString(2, calle == null ? "" : calle.toUpperCase());
			psmt.setString(3, numero == null ? "" : numero.toUpperCase());
			psmt.setString(4, siteId == null ? "" : siteId);
			psmt.setString(5, seqNo == null ? "" : seqNo);
			psmt.setString(6, "FISCAL");
			psmt.setString(7, "CLNT");
			psmt.setString(8, codigoClienteHid == null ? "" : codigoClienteHid);
			psmt.setString(9, geLevl == null ? "" : geLevl);
			psmt.setString(10, geType == null ? "" : geType);
			psmt.setString(11, geCode == null ? "" : geCode);
			psmt.setString(12, "1");
			psmt.setString(13, "Y");
			psmt.setString(14, "Y");
			psmt.setString(15, clientName == null ? "" : clientName);
			psmt.setString(16, global.getOrigenUserClave());
			psmt.setString(17, global.getOrigenUserClave());
			psmt.setString(18, email == null ? "" : email);
			psmt.setString(19, exceptBrnc == null ? "" : exceptBrnc);
			psmt.setString(20, celular);

			psmt.executeUpdate();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("insrtSysAddrMstr()Error:")
					.append(e).toString());
		} finally {
			resources.cerrarPreparedStatement(psmt);
		}
	}

	private String getSiteId(Connection con, String sucursal){
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String siteId = "";
		String sitequery = "select BM_BRNC_SITE_ID from SYS_BRNC_MSTR where BM_BRNC_ID = ?";
		try{	
			psmt = con.prepareStatement(sitequery);
			psmt.setString(1, sucursal);
			
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				siteId = rs.getString(1);
			}
		}catch(Exception e){
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getSiteId()Error:")
					.append(e).toString());
		}finally{
			resources.closeResources(rs, psmt);
		}
		return siteId;
	}
	
	private void insrtOrUpdtSysCatClnt(Connection con, Global global,
			ClientDestinationEntryForm clientDestEntryForm, String rfc) {
		try {
			if (rfc.equalsIgnoreCase("XAXX-010101-000") || rfc.equalsIgnoreCase("XAXX010101000") ||
					clientDestEntryForm.getDatosFiscalesString().isEmpty()) {
				return;
			}

			clientDestEntryForm.setDatosFiscalesTmp(clientDestEntryForm.getDatosFiscalesTmpString());
			clientDestEntryForm.setDatosFiscales(clientDestEntryForm.getDatosFiscalesString());
			CatClntFiscDTO datoFiscOrgn = clientDestEntryForm.getDatosFiscalesTmp();
			CatClntFiscDTO datoFiscNew = clientDestEntryForm.getDatosFiscales();
			String taxId = datoFiscOrgn.getTaxId().replace("-", "");
			String newTaxId = datoFiscNew.getTaxId().replace("-", "");
			String currRFC = clientDestEntryForm.getCurrRFC().replace("-", "");
				
			boolean capNewFisc = diffOrgnNewFiscalData(clientDestEntryForm, datoFiscOrgn, datoFiscNew, taxId, newTaxId);
			boolean shouldUpdate = capNewFisc || (!currRFC.equalsIgnoreCase(newTaxId) && capNewFisc);

			String existeDatosFiscales = getExisteDatosFiscales(con, rfc);
			if (!existeDatosFiscales.equalsIgnoreCase("Y")) {
				if (capNewFisc) {
					insertSysCatClntFisc(con, clientDestEntryForm, global);
				}else {
					clientDestEntryForm.setErrorMessages("Datos Fiscales Capturados Incorrectos.");
				}
			}

			boolean taxIdEqualsNewTaxIdAndNotValid = isInvalidFiscal(datoFiscOrgn.getValidFlag());
			boolean taxIdNotEqualsNewTaxIdAndValidFiscalNotY = isInvalidFiscal(clientDestEntryForm.getValidFiscal());
			boolean haveCredAvail = clientDestEntryForm.getTieneCredito().equalsIgnoreCase("S");

			if (shouldUpdate && (taxIdEqualsNewTaxIdAndNotValid || taxIdNotEqualsNewTaxIdAndValidFiscalNotY) && !haveCredAvail) {
				updtInvalidTaxId(con, clientDestEntryForm, global);
			}else if (!currRFC.isEmpty() && !currRFC.equalsIgnoreCase("XAXX010101000")){
				CatClntFiscDTO datoFiscCurr = getDatosFiscales(con, clientDestEntryForm.getCurrRFC());
				if (datoFiscCurr != null && datoFiscCurr.getValidFlag().equalsIgnoreCase("N")) {
					clientDestEntryForm.setErrorMessages("Datos Fiscales Capturados Incorrectos.");
				}
				clientDestEntryForm.setValidFiscal(datoFiscCurr.getValidFlag());
			}
			
			clientDestEntryForm.setDatosFiscalesString(new CatClntFiscDTO().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean diffOrgnNewFiscalData(ClientDestinationEntryForm clientDestEntryForm, CatClntFiscDTO datoFiscOrgn,
			CatClntFiscDTO datoFiscNew, String taxId, String newTaxId) {
		boolean changeInfo = false;

		if (datoFiscNew.getZipCode().isEmpty() || datoFiscNew.getRegmFiscal().isEmpty()) {
			return changeInfo;
		}

		if (!datoFiscOrgn.getZipCode().isEmpty() && (datoFiscNew.getZipCode().equalsIgnoreCase("")
				|| !(taxId.equalsIgnoreCase(newTaxId) || datoFiscOrgn.getTaxId().equalsIgnoreCase(""))
				|| !datoFiscOrgn.getZipCode().equalsIgnoreCase(datoFiscNew.getZipCode())
				|| !datoFiscOrgn.getRegmFiscal().equalsIgnoreCase(datoFiscNew.getRegmFiscal())
				|| !datoFiscOrgn.getUsoCfdi().equalsIgnoreCase(datoFiscNew.getUsoCfdi())
				|| !datoFiscOrgn.getBusinessName().equalsIgnoreCase(datoFiscNew.getBusinessName())
				|| !datoFiscOrgn.getFirstName().equalsIgnoreCase(datoFiscNew.getFirstName()))) {
			clientDestEntryForm.setErrorMessages("Datos Fiscales Capturados Incorrectos. se asignó temporalmente el RFC PÚBLICO EN GENERAL (XAXX-010101-000).");
			changeInfo = true;
		}
		
		if (taxId.isEmpty() && !newTaxId.isEmpty() && !datoFiscNew.getZipCode().equalsIgnoreCase("")) {
			changeInfo = true;
		}
		return changeInfo;
	}

	private void updtInvalidTaxId(Connection con, ClientDestinationEntryForm clientDestEntryForm, Global global) {
		updateSysCatClntFisc(con, clientDestEntryForm, global, true);
		String updateErrors = updateSysCatClntFisc(con, clientDestEntryForm, global, false);
		if (!updateErrors.isEmpty() && !updateErrors.equalsIgnoreCase(SUCCESS)) {
			clientDestEntryForm.setErrorMessages("Datos Fiscales No V\u00E1lidos. " + updateErrors);
		}else {
			clientDestEntryForm.setErrorMessages("");
		}
	}

	private boolean isInvalidFiscal(String validFiscal) {
		return !validFiscal.equalsIgnoreCase("Y");
	}

	// To get the Client Name from the Database
	private String getClientName(Connection con, String clientId) {

		String clientName = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "SELECT CM_CLNT_NAME FROM SYS_CLNT_MSTR WHERE CM_CLNT_ID = ?";

		try {
			pst = con.prepareStatement(query);
			pst.setString(1, clientId);

			rs = pst.executeQuery();

			if (rs.next()) {
				clientName = rs.getString(1);
			}
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getClientName()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return clientName;
	}	
	
	// To get sequence from the Database
	private String getSeqNo(Connection con) {
		String seqNo = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		String query = "SELECT am_addr_code_s1.NEXTVAL FROM dual";
		
		try {
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			
			if (rs.next()) {				
				seqNo = rs.getString(1);				
			}			
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getSeqNo()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return seqNo;
	}
	
	// By Susheel insertSys_clnt_mstr
	private void insertSysClntMstr(Connection con,
			ClientDestinationEntryForm clientDestEntryForm, 
			String siteId, Global global) {//AAP03
		PreparedStatement psmt = null;
		
		String allowedFXC = global.getAllowedFXC();//AAP04
		
		String strQuery = cnct.delete(0,cnct.length())
				.append("INSERT INTO sys_clnt_mstr")
				.append("(cm_clnt_id, cm_clnt_name, ")
				.append("cm_cred_perd, cm_slmn_id, cm_cred_limt, cm_revu_day,")
				.append("cm_pymt_day, cm_cred_stus_id, cm_clnt_type, cm_supd_cheq_flag,")
				.append("cm_ftt_ref_no, cm_date_of_trns, cm_grup_clnt_id, cm_comt,")
				.append("cm_dest_paid_flag, cm_ead_flag, ")
				.append("crtd_on,crtd_by,mdfd_on,mdfd_by, cm_flag_1, cm_flag_2, cm_flag_3,")
				.append("cm_curp_id, cm_regd_clnt_flag, cm_rad_flag, cm_invc_guia_flag,")
				.append("cm_insu_plcy_no, cm_insu_text, cm_expy_date, cm_req_insu_flag,")
				.append("cm_send_potn, cm_recv_potn, cm_asgn_to_site, cm_ins_amnt, CM_CUST_CLNT_ID, CM_TAX_ID, CM_RET_FLAG,")
				.append("cm_bsns_line_id, cm_bsns_seg_id, CM_ACTV_FLAG, CM_ACTV_BY, CM_ACTV_ON)")//AAP04
				.append("VALUES (?,?, 0, NULL, 0, NULL, NULL, 'DIS',?, 'N',NULL, NULL, NULL, NULL,")
				.append(" ?, ?,sysdate,?, sysdate,?, 'Y', NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, NULL, 'Y', ")
				.append(" NULL, NULL, ?, NULL, ?, ?, ?, ?, ?, ?, ?, SYSDATE) ").toString();//AAP03//AAP04

		try {
			// Added By Sam [06-06-2006] , The client type always will be type
			// ?I? Individuals
			//String strTipoCliente = "I";//AAP03
			
			String strTipoCliente = clientDestEntryForm.getTipoCliente();//AAP03
			String rfc = "";//AAP03
			String retieneIva = "";//AAP03
			if (clientDestEntryForm.getTipoCliente().equals("I")) {//AAP03
				if (clientDestEntryForm.getRfc1().length() == 0) {//AAP03
					rfc = "XAXX-010101-000";//AAP03
					retieneIva = "N";//AAP03
				}//AAP03
			} else {//AAP03
				if (clientDestEntryForm.getRfc1().length() == 0) {//AAP03
					rfc = "XAXX-010101-000";//AAP03
				}//AAP03
				retieneIva = "Y";//AAP03
			}//AAP03

			if (rfc.length() == 0 ) {//AAP03
				rfc = cnct.delete(0,cnct.length())
				.append(clientDestEntryForm.getRfc1().toUpperCase())
				.append("-")
				.append(clientDestEntryForm.getRfc2().toUpperCase())
				.append("-")
				.append(clientDestEntryForm.getRfc3().toUpperCase())
				.toString();//AAP03
			}
			
			psmt = con.prepareStatement(strQuery);

			psmt.setString(1, clientDestEntryForm.getCodigoClienteHid());
			psmt.setString(2, clientDestEntryForm.getNombre().trim().toUpperCase());
			psmt.setString(3, strTipoCliente);
			psmt.setString(4, allowedFXC);//AAP03
			psmt.setString(5, clientDestEntryForm.getEadCheck());
			psmt.setString(6, global.getOrigenUserClave());//AAP04
			psmt.setString(7, global.getOrigenUserClave());//AAP04
			psmt.setString(8, siteId);
			psmt.setString(9, clientDestEntryForm.getCodigoInt());
			psmt.setString(10, rfc);//AAP03
			psmt.setString(11, retieneIva);//AAP03
			psmt.setString(12, "9999");//AAP03
			psmt.setString(13, "0099");//AAP03
			psmt.setString(14, "A");//AAP04
			psmt.setString(15, global.getOrigenUserClave());//AAP04
			
			psmt.executeUpdate();

		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("insertSys_clnt_mstr()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(psmt);
		}
	}	
	
	// By Susheel insertWeb_orgin_dest_client

	private void insertWebOrginDestClient(Connection con,
			ClientDestinationEntryForm clientDestEntryForm,
			String siteId, Global global) {
		PreparedStatement psmt = null;
		String strQuery = cnct.delete(0,cnct.length())
				.append("INSERT INTO WEB_ORGN_DEST_CLNT ( WO_ORGN_CLNT_ID, WO_DEST_CLNT_ID, CRTD_ON,")
				.append("CRTD_BY, MDFD_ON,MDFD_BY, WO_DEST_SITE_ID,WO_DEST_CUST_CLNT_ID, WO_MBR_CLNT_ID) VALUES ")
				.append("(?, ?,sysdate, ? ,sysdate, ?,?,?,?)").toString();

		try {
			psmt = con.prepareStatement(strQuery);

			//Cambiamos el valor que se guardara en el campo WO_ORGN_CLNT_ID para que siempre tome al cliente que tiene el convenio 
			psmt.setString(1, global.getClientIdAgreement());
			psmt.setString(2, clientDestEntryForm.getCodigoClienteHid());
			psmt.setString(3, global.getOrigenUserClave());//AAP04
			psmt.setString(4, global.getOrigenUserClave());//AAP04

			psmt.setString(5, siteId);
			psmt.setString(6, clientDestEntryForm.getCodigoInt());
			psmt.setString(7, global.getClientId());

			psmt.executeUpdate();

		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("insertWeb_orgin_dest_client()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(psmt);
		}
	}
	
	// By Susheel for Errror message
	@SuppressWarnings("rawtypes")
	private boolean getErrorMessage(Connection con, ClientDestinationEntryForm clientDestEntryForm, Global global) {		
		String numInt = null;
		String clientDestId = null;
		String nombre = null;
		String ciudad = null;
		String assign = null;
		String numero = null;
		String calle = null;
		String codigoPostal = null;
		boolean flag = false;
		String msje = "";
		try {
			numInt = clientDestEntryForm.getCodigoInt();
			nombre = clientDestEntryForm.getNombre();
			ciudad = clientDestEntryForm.getCiudad();
			assign = clientDestEntryForm.getAssignedToBranchRef();
			numero = clientDestEntryForm.getNumero();
			calle = clientDestEntryForm.getCalle();
			codigoPostal = clientDestEntryForm.getCodigoPostal();
			clientDestEntryForm.setErrorMessages("");
			
			if (numInt != null && numInt.length() > 0) {
				String clientDestIdTmp = getClienteDestino(con,global,numInt); 
				clientDestId = clientDestIdTmp.length() > 1 ? clientDestIdTmp.split("\\|")[0] : "";
				if (clientDestId.length()>0 && !clientDestId.trim().equals( clientDestEntryForm.getCodigoCliente().trim() )) {
					msje = cnct.delete(0,cnct.length()).append("Número interno. El ").append(numInt).append(" ya existe en cliente Número ").append(clientDestId).append(".").toString();
					getError(con, "SYS", 800067, clientDestEntryForm, msje);//AAP01
					flag = true;					
				}
			}
			/*validacion de cliente con credito*/
			if (!flag && clientDestEntryForm.getCodigoCliente().length()>0) {
				clientDestId = clientDestEntryForm.getCodigoCliente();					
				ArrayList result  = getClientInfo(con, clientDestId);
					
				if (result!=null && !result.isEmpty() &&
					result.get(2).toString().equals("ENA")) {
					msje = cnct.delete(0,cnct.length()).append("Cliente destino ").append(clientDestId).append(" ").append(clientDestEntryForm.getNombre()).append(" con crédito activo, contactarse con su ejecutivo de cuenta para poder modificarlo. Solo se actualizará código interno de cliente destino.").toString();
					clientDestEntryForm.setErrorMessages(msje);
					clientDestEntryForm.setPartialUpdate("Y");
					flag = true;								
							
				}
			}			
			
			if (!flag) {
				if (nombre == null || nombre.length() == 0) {
					getError(con, "SYS", 800067, clientDestEntryForm, "Nombre del Cliente Destino");//AAP01
					flag = true;
				} else if (assign == null || assign.length() == 0) {
					getError(con, "SYS", 800067, clientDestEntryForm, "Sucursal Asignada");//AAP01
					flag = true;
					// Added By Sam [08-06-2006] for validating calle and numero
				} else if (calle == null || calle.length() == 0) {
					getError(con, "SYS", 800067, clientDestEntryForm, "Calle. Es obligatoria."); //AAP01
					flag = true;
				} else if (numero == null || numero.length() == 0) {
					getError(con, "SYS", 800067, clientDestEntryForm, "Número de Domicilio. Es obligatorio.");//AAP01
					flag = true;
				} else if (ciudad == null || ciudad.length() == 0) {
					getError(con, "SYS", 800067, clientDestEntryForm, "Ciudad. Es obligatoria.");//AAP01
					flag = true;
				} else if (codigoPostal == null || codigoPostal.length() == 0) {
					getError(con, "SYS", 800067, clientDestEntryForm, "Código Postal. Es obligatorio.");//AAP01
					flag = true;
				}
				
				/*validacion de cobertura*/
				if (!flag) {//AAP01
					String cobertura = validaCobertura(con, clientDestEntryForm);
					if (cobertura.equals("N")) {
						getError(con, "WEB", 9900003, clientDestEntryForm, "");
						flag = true;
					}
				}
			}

		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getErrorMessage()Error:").append(exe).toString());
			exe.printStackTrace();
		}
		return flag;
	}
	
	
	// By Susheel for getting the Errror message by calling the procedure
	private void getError(Connection con, String modulo, int idError, ClientDestinationEntryForm clientDestEntryForm,
			String tagID) {
		CallableStatement cst = null;		
		String msgtext = "";

		try {
			cst = con.prepareCall("{call pack_web.PRO_SHOW_MESG(?,pack_web.language_id,?,1,?,NULL,NULL,?,?)}");//AAP01

			cst.setString(1, modulo);//AAP01
			cst.setInt(2, idError);//AAP01
			cst.setString(3, tagID);//AAP01
			cst.registerOutParameter(4, Types.VARCHAR);//AAP01
			cst.registerOutParameter(5, Types.VARCHAR);//AAP01

			cst.executeQuery();
			
			msgtext = (cst.getString(5) != null ? cst.getString(5) : "");//AAP01
			clientDestEntryForm.setErrorMessages(msgtext);
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getError()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
	}
	
	
	private void getLabelEnteraDelClient(ClientDestinationEntryForm clientDestEntryForm, Connection con) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		String pmparaCode1 = null;
		String pmparaCode2 = null;
		String pmvalueDesc = null;
		String delegation = null;
		String sector = null;

		try {
			String labelQuery = cnct.delete(0, cnct.length())
					.append("SELECT PM_PARM_CODE1, PM_PARM_CODE2, initcap(PM_VLUE1_DESC) PM_VLUE1_DESC ")
					.append("FROM SYS_PARM_MSTR WHERE PM_PARM_TYPE = ? AND PM_MDUL_ID = ?")
					.toString();
			
			pst = con.prepareStatement(labelQuery);

			pst.setString(1, "ADDR_STRUCT");
			pst.setString(2, "SYS");

			rs = pst.executeQuery();

			while (rs.next()) {
				pmparaCode1 = rs.getString("PM_PARM_CODE1");
				pmparaCode2 = rs.getString("PM_PARM_CODE2");
				pmvalueDesc = rs.getString("PM_VLUE1_DESC");

				if (pmparaCode1 != null && pmparaCode1.equals("1")
						&& pmparaCode2 != null && pmparaCode2.equals("1")) {
					clientDestEntryForm.setPaisLabel(pmvalueDesc);
				} else if (pmparaCode1 != null && pmparaCode1.equals("2")
						&& pmparaCode2 != null && pmparaCode2.equals("1")) {
					clientDestEntryForm.setZonaLabel(pmvalueDesc);
				} else if (pmparaCode1 != null && pmparaCode1.equals("3")
						&& pmparaCode2 != null && pmparaCode2.equals("1")) {
					clientDestEntryForm.setEstadoLabel(pmvalueDesc);
				} else if (pmparaCode1 != null && pmparaCode1.equals("4")
						&& pmparaCode2 != null && pmparaCode2.equals("1")) {
					clientDestEntryForm.setCiudadLabel(pmvalueDesc);
				} else if (pmparaCode1 != null && pmparaCode1.equals("5")
						&& pmparaCode2 != null && pmparaCode2.equals("1")) {
					clientDestEntryForm.setMunicipioLabel(cnct.delete(0, cnct.length()).append(pmvalueDesc).append("/").toString());
				} else if (pmparaCode1 != null && pmparaCode1.equals("5")
						&& pmparaCode2 != null && pmparaCode2.equals("2")) {
					clientDestEntryForm.setDelegacion(pmvalueDesc);
					delegation = pmvalueDesc;
				} else if (pmparaCode1 != null && pmparaCode1.equals("6")
						&& pmparaCode2 != null && pmparaCode2.equals("1")) {
					clientDestEntryForm.setColoniaLabel(pmvalueDesc);
				} else if (pmparaCode1 != null && pmparaCode1.equals("7")
						&& pmparaCode2 != null && pmparaCode2.equals("1")) {
					clientDestEntryForm.setCodigoPostalLabel(pmvalueDesc);
				} else if (pmparaCode1 != null && pmparaCode1.equals("5")
						&& pmparaCode2 != null && pmparaCode2.equals("3")) {
					sector = pmvalueDesc;
				}

				clientDestEntryForm.setDelegacionSecto(cnct.delete(0, cnct.length()).append(delegation).append("/").append(sector).toString());
			}
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getLabelEnteraDelClient()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
	}	
	
	private void updateRecords(ClientDestinationEntryForm clientDestEntryForm,
			Connection con, HttpSession session) {

		PreparedStatement psmt = null;
		String clientName = null;
		String clientId = null;
		String pqId = null;

		try {
			Global global = (Global) session.getAttribute("sGlobal");//AAP03
			pqId = clientDestEntryForm.getCodigoInt();
			String assignedToBranch = clientDestEntryForm.getAssignedToBranch();			
			

			// Added By Sam.D.Jabeen[06-06-2006] , value of this field will
			// always be as the AssignedToBranch
			String sucursal = assignedToBranch;
			String eadCheck = clientDestEntryForm.getEadCheck();
			clientId = (String) session.getAttribute("sClientId");
			// To Get Client Name from the data base
			clientName = getClientName(con, clientId);
			
			String siteId = getSiteId(con, sucursal);
			
			//String strTipoCliente = clientDestEntryForm.getTipoCliente();//AAP03
			String rfc = getRfc(clientDestEntryForm);
			
			updtSysAddrMstr(con, clientDestEntryForm, siteId, clientName, clientId);
			updtSysclntMstr(con, clientDestEntryForm, eadCheck, siteId, pqId, global, rfc);
			updtWebOrgnDestClnt(con, clientDestEntryForm, siteId, pqId, clientId);
			insrtOrUpdtSysCatClnt(con, global, clientDestEntryForm, rfc);
			if (clientDestEntryForm.getErrorMessages().contains("Datos Fiscales No")
					|| clientDestEntryForm.getErrorMessages().equalsIgnoreCase("Datos Fiscales Incorrectos")
					|| (clientDestEntryForm.getValidFiscal() == null 
					|| clientDestEntryForm.getValidFiscal().equalsIgnoreCase("N"))) {
				clientDestEntryForm.setTipoCliente("I");
				updtSysclntMstr(con, clientDestEntryForm, eadCheck, siteId, pqId, global, "XAXX-010101-000");
			}
			con.commit();
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("updateRecords()Error2:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(psmt);			
		}
	}

	private void updtSysAddrMstr(Connection con, ClientDestinationEntryForm clientDestEntryForm,String siteId,
			String clientName, String clientId) {
		PreparedStatement psmt = null;
		String telefono = clientDestEntryForm.getTelefono();			
		String celular = clientDestEntryForm.getCelular();
		celular = (celular != null && celular.length() > 0 && celular.matches("\\d+")) ? celular : null;
		String calle = clientDestEntryForm.getCalle();
		String numero = clientDestEntryForm.getNumero();
		String geLevl = clientDestEntryForm.getGeLevl();
		String geType = clientDestEntryForm.getGeType();
		String geCode = clientDestEntryForm.getGeCode();
		String mailId = clientDestEntryForm.getEmail();
		String codigoCliente = clientDestEntryForm.getCodigoCliente();
		String updateQuery = "update sys_addr_mstr set am_phno1 = ?, am_strt_name = ?, am_drnr = ?, am_pe_site_id = ?, am_gety_levl = ?, am_gety_type = ?, am_gety_code = ?, am_enty_desc = ?, mdfd_on = sysdate, mdfd_by = ?, am_mail_id = ?, AM_MBNO = ? where am_enty_id= ? and am_defa_flag = ? and am_addr_type = ?";
		try {
			psmt = con.prepareStatement(updateQuery);
			psmt.setString(1, (telefono == null) ? "" : telefono.toUpperCase());
			psmt.setString(2, (calle == null) ? "" : calle.toUpperCase());
			psmt.setString(3, (numero == null) ? "" : numero.toUpperCase());
			psmt.setString(4, (siteId == null) ? "" : siteId);
			psmt.setString(5, (geLevl == null) ? "" : geLevl);
			psmt.setString(6, (geType == null) ? "" : geType);
			psmt.setString(7, (geCode == null) ? "" : geCode);
			psmt.setString(8, (clientName == null) ? "" : clientName);
			psmt.setString(9, (clientId == null) ? "" : clientId);
			psmt.setString(10, (mailId == null) ? "" : mailId);
			psmt.setString(11, (celular == null) ? "" : celular.toUpperCase());
			psmt.setString(12, codigoCliente);
			psmt.setString(13, "Y");
			psmt.setString(14, "CLNT");

			psmt.executeUpdate();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("updtSysAddrMstr()Error:").append(e)
					.toString());
		} finally {
			resources.cerrarPreparedStatement(psmt);
		}
	}

	private void updtSysclntMstr(Connection con, ClientDestinationEntryForm clientDestEntryForm, String eadCheck,
			String siteId, String pqId, Global global, String rfc) {
		PreparedStatement psmt = null;
		Integer paramIndex=1;
		String updateQuery = cnct.delete(0, cnct.length()).append("update sys_clnt_mstr set cm_clnt_name = ?, ")
				.append("cm_ead_flag = ?, cm_asgn_to_site = ?, CM_CUST_CLNT_ID = ?, cm_dest_paid_flag = ?, ")
				.append("CM_CLNT_TYPE = ?, CM_TAX_ID = ?, MDFD_ON = SYSDATE, MDFD_BY = ? ")
				.append("where cm_clnt_id = ?").toString();
		try {
			psmt = con.prepareStatement(updateQuery);
			psmt.setString(paramIndex++, (clientDestEntryForm.getNombre() == null) ? "" : clientDestEntryForm.getNombre());
			psmt.setString(paramIndex++, (eadCheck == null) ? "" : eadCheck);
			psmt.setString(paramIndex++, (siteId == null) ? "" : siteId);
			psmt.setString(paramIndex++, (pqId == null) ? "" : pqId);
			psmt.setString(paramIndex++, global.getAllowedFXC());// AAP03
			psmt.setString(paramIndex++, clientDestEntryForm.getTipoCliente());// AAP03
			psmt.setString(paramIndex++, rfc);// AAP03
			psmt.setString(paramIndex++, global.getOrigenUserClave());
			String clntDestId =  clientDestEntryForm.getCodigoCliente().isEmpty() ? clientDestEntryForm.getCodigoClienteHid()
					: clientDestEntryForm.getCodigoCliente();
			psmt.setString(paramIndex++, clntDestId);

			psmt.executeUpdate();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("updtSysclntMstr()Error:").append(e)
					.toString());
		} finally {
			resources.cerrarPreparedStatement(psmt);
		}
	}
	
	private void updtWebOrgnDestClnt(Connection con, ClientDestinationEntryForm clientDestEntryForm, String siteId, String pqId, String clientId){
		PreparedStatement psmt = null;
		
		try{
			/*actualiza site en general para cliente destino,
			 * modifica todos los registros donde exista el cliente destino*/
			String updateQuery = "UPDATE WEB_ORGN_DEST_CLNT SET WO_DEST_SITE_ID = ? WHERE WO_DEST_CLNT_ID = ?";
			psmt = con.prepareStatement(updateQuery);
			psmt.setString(1, (siteId == null) ? "" : siteId);
			psmt.setString(2, clientDestEntryForm.getCodigoCliente());
			
			psmt.executeUpdate();
			psmt.close();

			/*actualiza numero interno de cliente destino solo para el cliente origen que esta realizando la actualizacion,
			 * modifica modifica solamente el registro para el cliente origen*/
			updateQuery = "UPDATE WEB_ORGN_DEST_CLNT SET WO_DEST_CUST_CLNT_ID = ? WHERE WO_ORGN_CLNT_ID = (SELECT WC_CLNT_ID FROM WEB_CLNT_MBR WHERE WC_MBR_ID = ? AND rownum = 1) AND WO_DEST_CLNT_ID = ?";
			
			psmt = con.prepareStatement(updateQuery);
			psmt.setString(1, (pqId == null) ? "" : pqId);
			psmt.setString(2, clientId);
			psmt.setString(3, clientDestEntryForm.getCodigoCliente());
			
			psmt.executeUpdate();
		}catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("updtWebOrgnDestClnt()Error:").append(e)
					.toString());
		} finally {
			resources.cerrarPreparedStatement(psmt);
		}
	}

	private String getRfc(ClientDestinationEntryForm clientDestEntryForm) {
		final String DEFAULT_RFC = "XAXX-010101-000";
		if (clientDestEntryForm.getRfc1().isEmpty()) {
			return DEFAULT_RFC;
		}
		return clientDestEntryForm.getTipoCliente().equalsIgnoreCase("I") && clientDestEntryForm.getRfc1().isEmpty()
				? DEFAULT_RFC
				: new StringBuilder().append(clientDestEntryForm.getRfc1().toUpperCase())
									.append("-")
									.append(clientDestEntryForm.getRfc2().toUpperCase())
									.append("-")
									.append(clientDestEntryForm.getRfc3().toUpperCase())
									.toString();
	}
	
	/****************************************************************************************
	 * Busca cliente destino con numero de cliente interno									*
	 ****************************************************************************************/
	private String getClienteDestino(Connection con, Global global, String numIntDestino) {
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String clienteDestino = "";
		String clienteDestinoInt = "";
		String whereClntOrign = "WO_ORGN_CLNT_ID";
		String clienteOrigen = global.getClientIdAgreement();
		String query = "";
		try{
			
			if (global.getCatalogMbr().equals("1")) {
				whereClntOrign = "WO_MBR_CLNT_ID";
				clienteOrigen = global.getClientId();
			}
			
			query = "SELECT WO_DEST_CLNT_ID, WO_DEST_CUST_CLNT_ID FROM WEB_ORGN_DEST_CLNT WHERE "+whereClntOrign+" = ? AND WO_DEST_CUST_CLNT_ID = ?";
			psmt = con.prepareStatement(query);
			psmt.setString(1, clienteOrigen);
			psmt.setString(2, numIntDestino);
			rs = psmt.executeQuery();
			
			while(rs.next()){				
				clienteDestino = rs.getString(1) == null ? "" : rs.getString(1);
				clienteDestinoInt = rs.getString(2) == null ? "" : rs.getString(2);
			}			
		} catch(Exception e) {			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getClienteDestino()Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.closeResources(rs, psmt);
		}
		return clienteDestino+"|"+clienteDestinoInt;
	}
	
	/****************************************************************************************
	 * Busca cliente destino con numero de cliente interno									*
	 ****************************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList getClienteDestinoInt(Connection con, Global global, String numDestino) {
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		ArrayList datos = new ArrayList(1);
		String whereClntOrign = "WO_ORGN_CLNT_ID";
		String query = "";
		String clienteOrigen = global.getClientIdAgreement();
		try{
			if (global.getCatalogMbr().equals("1")) {
				whereClntOrign = "WO_MBR_CLNT_ID";
				clienteOrigen = global.getClientId();
			}
			query = "SELECT WO_DEST_CLNT_ID, WO_DEST_CUST_CLNT_ID FROM WEB_ORGN_DEST_CLNT WHERE "+whereClntOrign+" = ? AND WO_DEST_CLNT_ID = ?";
			psmt = con.prepareStatement(query);
			psmt.setString(1, clienteOrigen);
			psmt.setString(2, numDestino);
			
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				datos.add(rs.getString(1) == null ? "" : rs.getString(1));
				datos.add(rs.getString(2) == null ? "" : rs.getString(2));
			}			
		} catch(Exception e) {			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getClienteDestino()Error:").append(e).toString());
			e.printStackTrace();
		}finally {
			resources.closeResources(rs, psmt);				
		}
		return datos;
	}
	
	/************************************************************
	 * metodo para validacion de cobertura						*
	 ************************************************************///AAP01
	private String validaCobertura(Connection con, ClientDestinationEntryForm clientDestEntryForm){
		PreparedStatement pst = null;
		ResultSet rs = null;
		String cobertura = "N";
		
		try {
			pst = con.prepareStatement("SELECT BC_ZONE FROM PCOBERTURA_VIEW WHERE COLO_ZIPCODE = ? AND COL_DES = ? AND SUCURSAL IS NOT NULL AND GE_SEPOMEX IS NOT NULL");
			
			pst.setString(1,clientDestEntryForm.getCodigoPostal());
			pst.setString(2,clientDestEntryForm.getColonia());
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				cobertura = rs.getString(1);
			}
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("validaCobertura()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return cobertura.trim();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ArrayList getClientInfo(Connection con, String clientId) {//AAP03
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList result = null;
		String query = "SELECT NVL(CM_TAX_ID,'') CM_TAX_ID, NVL(CM_CLNT_TYPE,'I') CM_CLNT_TYPE, NVL(CM_CRED_STUS_ID,'') CM_CRED_STUS_ID FROM SYS_CLNT_MSTR WHERE CM_CLNT_ID = ?";
		String queryFiscal = "SELECT NVL(REGM_FIS,'') REGM_FIS, NVL(USO_CFDI,'') USO__CFDI FROM SYS_CAT_CLNT_FISC WHERE TAX_ID = ?";
		try {
			pst = con.prepareStatement(query);
			pst.setString(1, clientId);

			rs = pst.executeQuery();

			if (rs.next()) {
				result = new ArrayList(rs.getFetchSize());
				result.add(rs.getString(1));
				result.add(rs.getString(2));
				result.add(rs.getString(3));
				pst.close();
				pst = con.prepareStatement(queryFiscal);
				pst.setString(1, result.get(0).toString());
				rs.close();
				
				rs = pst.executeQuery();
				
				if (rs.next()) {
					result.add(rs.getString(1));
					result.add(rs.getString(2));
				}else {
					result.add("");
					result.add("");
				}
			} else {
				result = new ArrayList(0);
			}
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getClientInfo()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return result;
	}
	private void partialUpdateRecords(ClientDestinationEntryForm clientDestEntryForm,
			Connection con, HttpSession session) {

		PreparedStatement psmt = null;

		String clientId = (String) session.getAttribute("sClientId");
		String pqId = null;

		try {
			pqId = clientDestEntryForm.getCodigoInt();
			
			/*actualiza numero interno de cliente destino solo para el cliente origen que esta realizando la actualizacion,
			 * modifica modifica solamente el registro para el cliente origen*/
			String updateQuery = "UPDATE WEB_ORGN_DEST_CLNT SET WO_DEST_CUST_CLNT_ID = ? WHERE WO_ORGN_CLNT_ID in (SELECT WC_CLNT_ID FROM WEB_CLNT_MBR WHERE WC_MBR_ID = ? AND rownum = 1) AND WO_DEST_CLNT_ID = ?";
			
			psmt = con.prepareStatement(updateQuery);
			psmt.setString(1, (pqId == null) ? "" : pqId);
			psmt.setString(2, clientId);
			psmt.setString(3, clientDestEntryForm.getCodigoCliente());
			
			psmt.executeUpdate();
			
			con.commit();
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("partialUpdateRecords()Error1:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(psmt);			
		}
	}

	private String getBranchDesc(Connection con, String brnc) {//AAP03
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = "SELECT BM_BRNC_NAME FROM SYS_BRNC_MSTR WHERE BM_BRNC_ID = ?";
		String name = "";
		try {
			pst = con.prepareStatement(query);
			pst.setString(1, brnc);

			rs = pst.executeQuery();

			if (rs.next()) {				
				name = rs.getString(1) == null ? "" : rs.getString(1);				
			}
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getBranchDesc()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return name;
	}
	
	private String getBrncOcuExcept(Connection con, String orgnClnt, String destSite) {
		String exceptBrnc = "";
		ResultSet rs = null;
		PreparedStatement pst = null;
		String query = "SELECT PM_PARM_CODE2 FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = ? AND PM_PARM_TYPE = ? AND PM_PARM_CODE1 = ? AND SUBSTR(PM_PARM_CODE2,1,3) = ?";
		
		try {
			/*verifica si el cliente origen tiene configurada sucursal destino para asignar A OCURRE POR EXCEPCION*/
			pst = con.prepareStatement(query);
			pst.setString(1, "WEB");
			pst.setString(2, "BRNC_OCU_EXCEPT");
			pst.setString(3, orgnClnt);
			pst.setString(4, destSite);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				exceptBrnc = rs.getString(1)== null ? "" :rs.getString(1);							
			}			
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getBrncOcuExcept()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return exceptBrnc;
	}	
	
	/* Obtiene valores default para RFC generico */
	@SuppressWarnings("rawtypes")
	private ClientDestinationEntryForm fiscalDefaultValues(Connection con, ClientDestinationEntryForm clientDestEntryForm) {
		ConsultaParametros consulta = new ConsultaParametros();
		
		ArrayList defVlues = consulta.QryMdulType(con, "FIN", "REGFI_UCFDI_GEN");
		
		String regimenFiscalId = ((ArrayList) defVlues.get(0)).get(0).toString();
		String regimenFiscalDes = regimenFiscalId+" - "+((ArrayList) defVlues.get(0)).get(4).toString();
		String usoCfdiId = ((ArrayList) defVlues.get(0)).get(1).toString();
		String usoCfdiDes = usoCfdiId +" - "+((ArrayList) defVlues.get(0)).get(5).toString();
		
		clientDestEntryForm.setRegimenFiscalId(regimenFiscalId);
		clientDestEntryForm.setRegimenFiscalDes(regimenFiscalDes);
		clientDestEntryForm.setUsoCfdiId(usoCfdiId);
		clientDestEntryForm.setUsoCfdiDes(usoCfdiDes);
		
		return clientDestEntryForm;
	}
	
	/* Verifica si el cliente tiene registrados los datos fiscales */
	private String getExisteDatosFiscales(Connection con, String rfc) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String existeDatoFiscal = "";
		try {
			pst = con.prepareStatement("SELECT 'Y' FROM SYS_CAT_CLNT_FISC WHERE TAX_ID = ?");
			
			pst.setString(1, rfc);
			rs = pst.executeQuery();
			if (rs.next()) {
				existeDatoFiscal = rs.getString(1);
			}
		}catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getExisteDatosFiscales()Error:").append(e).toString());
			e.printStackTrace();
		}finally {
			resources.closeResources(rs, pst);
		}
		return existeDatoFiscal;
	}
	
	/* Inserta en Sys CAT CLNT FISC */
	private void insertSysCatClntFisc(Connection con,
			ClientDestinationEntryForm clientDestEntryForm,
			Global global) {
		PreparedStatement psmt = null;
		String strQuery = cnct.delete(0, cnct.length())
				.append("Insert into SYS_CAT_CLNT_FISC ")
				.append("(TAX_ID, CLNT_TYPE, BUSINESS_NAME, CAPITAL_REGM, FIRST_NAME, LAST_NAME_1, LAST_NAME_2, ")
				.append("STREET, NO_INT, NO_EXT, ZIP_CODE, REGM_FIS, USO_CFDI, VALID_FLAG, CRTD_ON, CRTD_BY, MDFD_ON, ")
				.append("MDFD_BY, COUNTRY, STATE, MUNICIPALITY, CITY, COLONY, ADDR_CODE) ")
				.append("Values ")
				.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
				.append(" SYSDATE, ?, SYSDATE, ?, ?, ?, ?, ?, ?, ?)").toString();

		try {
			clientDestEntryForm.setDatosFiscales(clientDestEntryForm.getDatosFiscalesString());
			CatClntFiscDTO datosFiscales = clientDestEntryForm.getDatosFiscales();
			String strTipoCliente = datosFiscales.getClntType().equalsIgnoreCase("")
					? clientDestEntryForm.getTipoCliente()
					: datosFiscales.getClntType();
			String rfc = datosFiscales.getTaxId().equalsIgnoreCase("")
					? (clientDestEntryForm.getRfc1() + clientDestEntryForm.getRfc2() + clientDestEntryForm.getRfc3())
					: datosFiscales.getTaxId();
			String addrSeq = getSeqNo(con);
			if (strTipoCliente.equals("I")) {
				rfc = formatRFC(strTipoCliente, rfc);
				strTipoCliente = "F";
			} else {
				rfc = formatRFC(strTipoCliente, rfc);
				strTipoCliente = "M";
			}

			psmt = con.prepareStatement(strQuery);

			psmt.setString(1, rfc);
			psmt.setString(2, strTipoCliente);
			psmt.setString(3, datosFiscales.getBusinessName());
			psmt.setString(4, datosFiscales.getCapitalRegm());
			psmt.setString(5, datosFiscales.getFirstName());
			psmt.setString(6, datosFiscales.getLastName1());
			psmt.setString(7, datosFiscales.getLastName2());
			psmt.setString(8, datosFiscales.getStreet());
			psmt.setString(9, datosFiscales.getnInt());
			psmt.setString(10, datosFiscales.getnExt());
			psmt.setString(11, datosFiscales.getZipCode());
			psmt.setString(12, datosFiscales.getRegmFiscal());
			psmt.setString(13, datosFiscales.getUsoCfdi());
			psmt.setString(14, "N");
			psmt.setString(15, global.getOrigenUserClave());
			psmt.setString(16, global.getOrigenUserClave());
			psmt.setString(17, datosFiscales.getCountry());
			psmt.setString(18, datosFiscales.getState());
			psmt.setString(19, datosFiscales.getMunicipality());
			psmt.setString(20, datosFiscales.getCity());
			psmt.setString(21, datosFiscales.getColony());
			psmt.setString(22, addrSeq);

			psmt.executeUpdate();
			con.commit();
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("insertSysCatClntFisc()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(psmt);
		}
	}
	
	/* Actualiza en Sys CAT CLNT FISC */
	private String updateSysCatClntFisc(Connection con,
			ClientDestinationEntryForm clientDestEntryForm,
			Global global,
			Boolean preValFiscData) {
		PreparedStatement psmt = null;
		String result = "";
		Integer paramIndex = 1;
		String validFiscal="|";

		String strQuery = cnct.delete(0, cnct.length())
				.append("UPDATE SYS_CAT_CLNT_FISC SET ")
				.append("TAX_ID = ?, CLNT_TYPE = ?, BUSINESS_NAME = ?, CAPITAL_REGM = ?, FIRST_NAME = ?, LAST_NAME_1 = ?, LAST_NAME_2 = ?, ")
				.append("STREET = ? , NO_INT = ?, NO_EXT = ?, ZIP_CODE = ?, REGM_FIS = ?, USO_CFDI = ?, ")
				.append("VALID_FLAG = ?, MDFD_ON = SYSDATE, MDFD_BY = ?, COUNTRY = ?, STATE = ?, MUNICIPALITY = ?, COLONY = ?, CITY = ?")
				.append("WHERE TAX_ID = ?").toString();
		if (Boolean.TRUE.equals(preValFiscData))
			strQuery = strQuery.replace("VALID_FLAG = ?,", "");
		try {
			clientDestEntryForm.setDatosFiscales(
					clientDestEntryForm.getDatosFiscales() != null ? clientDestEntryForm.getDatosFiscales().toString()
							: clientDestEntryForm.getDatosFiscalesString());
			CatClntFiscDTO datosFiscales = clientDestEntryForm.getDatosFiscales();
			String strTipoCliente = datosFiscales.getClntType().equalsIgnoreCase("")
					? clientDestEntryForm.getTipoCliente()
					: datosFiscales.getClntType();
			String rfc = datosFiscales.getTaxId().equalsIgnoreCase("")
					? (clientDestEntryForm.getRfc1() + clientDestEntryForm.getRfc2() + clientDestEntryForm.getRfc3())
					: datosFiscales.getTaxId();

			if (strTipoCliente.equals("I")) {
				rfc = formatRFC(strTipoCliente, rfc);
				strTipoCliente = "F";
			} else {
				rfc = formatRFC(strTipoCliente, rfc);
				strTipoCliente = "M";
			}

			psmt = con.prepareStatement(strQuery);

			psmt.setString(paramIndex++, rfc);
			psmt.setString(paramIndex++, strTipoCliente);
			psmt.setString(paramIndex++, datosFiscales.getBusinessName());
			psmt.setString(paramIndex++, datosFiscales.getCapitalRegm());
			psmt.setString(paramIndex++, datosFiscales.getFirstName());
			psmt.setString(paramIndex++, datosFiscales.getLastName1());
			psmt.setString(paramIndex++, datosFiscales.getLastName2());
			psmt.setString(paramIndex++, datosFiscales.getStreet());
			psmt.setString(paramIndex++, datosFiscales.getnInt());
			psmt.setString(paramIndex++, datosFiscales.getnExt());
			psmt.setString(paramIndex++, datosFiscales.getZipCode());
			psmt.setString(paramIndex++, datosFiscales.getRegmFiscal());
			psmt.setString(paramIndex++, datosFiscales.getUsoCfdi());
			if (Boolean.FALSE.equals(preValFiscData)){
				String clntDestId =  clientDestEntryForm.getCodigoClienteHid().isEmpty() ?
						clientDestEntryForm.getCodigoInt() : clientDestEntryForm.getCodigoClienteHid();
				validFiscal = validaDatosFiscales(con, clntDestId, clientDestEntryForm.getSucursal());
				clientDestEntryForm.setValidFiscal(validFiscal.split("\\|")[0]);
				psmt.setString(paramIndex++, validFiscal.split("\\|")[0]);
			}
			psmt.setString(paramIndex++, global.getOrigenUserClave());
			psmt.setString(paramIndex++, datosFiscales.getCountry());
			psmt.setString(paramIndex++, datosFiscales.getState());
			psmt.setString(paramIndex++, datosFiscales.getMunicipality());
			psmt.setString(paramIndex++, datosFiscales.getColony());
			psmt.setString(paramIndex++, datosFiscales.getCity());
			psmt.setString(paramIndex++, rfc);

			psmt.executeUpdate();
			con.commit();
			try {
				String errors = validFiscal.split("\\|")[1];
				if (!errors.isEmpty()) {
					return errors;
				}	
			} catch (Exception e) {
				result = SUCCESS;
			}
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("updateSysCatClntFisc()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(psmt);
		}

		return result;
	}
	
	private String formatRFC(String tipoCliente, String rfc) {
		if (tipoCliente.equals("I")) {
			if (!rfc.contains("-")) {
				rfc = rfc.substring(0, 4) + "-" + rfc.substring(4, 10) + "-" + rfc.substring(10, rfc.length());
			}
		} else {
			if (!rfc.contains("-")) {
				rfc = rfc.substring(0, 3) + "-" + rfc.substring(3, 9) + "-" + rfc.substring(9, rfc.length());
			}
		}
		return rfc;
	}
	
	/* Valida Datos Ficales */
	private String validaDatosFiscales(Connection con, String clntId, String brncId) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String datosValidos = "N";
		String errors = "";
		try {
			pst = con.prepareStatement("SELECT PACK_JSON2.call_API_test_fisc_data(?,?) FROM DUAL");

			pst.setString(1, clntId);
			pst.setString(2, brncId);

			rs = pst.executeQuery();
			if (rs.next()) {
				datosValidos = rs.getString(1).split("\\|")[0].contains("0") ? "Y" : "N";
				try {
					errors = rs.getString(1).split("\\|")[2];
				} catch (Exception e) {

				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("validaDatosFiscales()Error:").append(e)
					.toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		if (!errors.isEmpty()) {
			datosValidos += "|" + errors;
		}
		return datosValidos;
	}
	
	/* Obtiene Datos Fiscales */
	private CatClntFiscDTO getDatosFiscales(Connection con, String rfc) throws SQLException {
		PreparedStatement pst = null;
		ResultSet rs = null;
		CatClntFiscDTO datosFiscales = new CatClntFiscDTO();
		try {
			pst = con.prepareStatement("SELECT NVL(TAX_ID,'') TAX_ID, NVL(CLNT_TYPE,'') CLNT_TYPE, NVL(BUSINESS_NAME,'') BUSINESS_NAME, NVL(CAPITAL_REGM,'') CAPITAL_REGM, NVL(FIRST_NAME,'') FIRST_NAME, "
					+ "NVL(LAST_NAME_1,'') LAST_NAME_1, NVL(LAST_NAME_2,'') LAST_NAME_2, NVL(STREET,'') STREET, NVL(NO_INT,'') NO_INT, NVL(NO_EXT,'') NO_EXT, NVL(ZIP_CODE,'') ZIP_CODE, NVL(REGM_FIS,'') REGM_FIS, "
					+ "NVL(USO_CFDI,'') USO_CFDI, NVL(VALID_FLAG,'N') VALID_FLAG, NVL(COUNTRY,'') COUNTRY, NVL(STATE,'') STATE, NVL(MUNICIPALITY,'') MUNICIPALITY, "
					+ " NVL(COLONY,'') COLONY, NVL(ADDR_CODE,'') ADDR_CODE, NVL(CITY,'') CITY FROM SYS_CAT_CLNT_FISC WHERE TAX_ID = ?");
			
			pst.setString(1, rfc);
			rs = pst.executeQuery();
			if (rs.next()) {
				datosFiscales.setTaxId(rs.getString("TAX_ID"));
				datosFiscales.setAddrCode(rs.getString("ADDR_CODE"));
				datosFiscales.setBusinessName(rs.getString("BUSINESS_NAME"));
				datosFiscales.setCapitalRegm(rs.getString("CAPITAL_REGM"));
				datosFiscales.setCity(rs.getString("CITY"));
				datosFiscales.setClntType(rs.getString("CLNT_TYPE"));
				datosFiscales.setColony(rs.getString("COLONY"));
				datosFiscales.setCountry(rs.getString("COUNTRY"));
				datosFiscales.setFirstName(rs.getString("FIRST_NAME"));
				datosFiscales.setLastName1(rs.getString("LAST_NAME_1"));
				datosFiscales.setLastName2(rs.getString("LAST_NAME_2"));
				datosFiscales.setMunicipality(rs.getString("MUNICIPALITY"));
				datosFiscales.setnExt(rs.getString("NO_EXT"));
				datosFiscales.setnInt(rs.getString("NO_INT"));
				datosFiscales.setRegmFiscal(rs.getString("REGM_FIS"));
				datosFiscales.setState(rs.getString("STATE"));
				datosFiscales.setStreet(rs.getString("STREET"));
				datosFiscales.setUsoCfdi(rs.getString("USO_CFDI"));
				datosFiscales.setValidFlag(rs.getString("VALID_FLAG"));
				datosFiscales.setZipCode(rs.getString("ZIP_CODE"));
				datosFiscales.setStreet(rs.getString("STREET"));
			}
		}catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getDatosFiscales()Error:").append(e).toString());
			e.printStackTrace();
		}finally {
			resources.closeResources(rs, pst);
		}
		return datosFiscales;
	}
	
	// To get the Client Available from the Database
	private String getCredAvail(Connection con, String taxId) {

		String credAvail = "N";
		PreparedStatement pst = null;
		ResultSet rs = null;
        String query = "SELECT CM_CRED_STUS_ID FROM SYS_CLNT_MSTR WHERE CM_TAX_ID = ? AND CM_CRED_STUS_ID = 'ENA' AND CM_ACTV_FLAG ='A' and rownum = 1";

		try {
			pst = con.prepareStatement(query);
			pst.setString(1, taxId);

			rs = pst.executeQuery();

			if (rs.next()) {
				credAvail = rs.getString(1).equalsIgnoreCase("ENA") ? "S" : "N";
			}
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getCredAvail()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return credAvail;
	}	
	
	// 	Methods to skip generic RFC
	private Boolean getSkipRFC(Connection con, String taxId) {

		Boolean skipRFC = false;
		PreparedStatement pst = null;
		ResultSet rs = null;
        String query = "SELECT  'Y' SKIP_RFC FROM SYS_PARM_MSTR  WHERE PM_MDUL_ID = 'SYS' AND PM_PARM_TYPE = 'SALESFORCE' AND PM_PARM_CODE1 = 'SKIP_RFC' AND PM_PARM_CODE2 = ?";

		try {
			pst = con.prepareStatement(query);
			pst.setString(1, taxId);

			rs = pst.executeQuery();

			if (rs.next()) {
				skipRFC = rs.getString(1).equalsIgnoreCase("Y");
			}
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getSkipRFC()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return skipRFC;
	}
}	