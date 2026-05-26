/**
 * @author: Murugesapandian T
 * Fecha de Creación: 13-May-2006
 * Compañía: KUMARAN.
 * Descripción del programa: This is the Action Class for Client destination Entry
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
 * Descripción: Se agregó validacion de cobertura. 
 * Se modificó metodo getError() para hacerlo reutilizable 
 * ------------------------------------------------------------------
 * Clave: AAP02
 * Autor: Abraham Daniel Arjona Peraza.
 * Fecha: 01/03/2013
 * Descripción: 
 * Se modificó obtencion de secuencia para registrar cliente. 
 * Ahora se obtiene cada que se ejecute el método saveRecords()
 * ------------------------------------------------------------------
 * Clave: AAP03
 * Autor: Abraham Daniel Arjona Peraza.
 * Fecha: 03/07/2013
 * Descripción: 
 * Se agregaron variables para registro de rfc, tipo de cliente (I,C) 
 * y el cliente destino acepta flete por cobrar cuando el cliente origen 
 * tiene prendida la bandera de envios flete por cobrar.  
 * se agregó metodo getClientInfo() para obtener informacion del cliente destino.
 * ------------------------------------------------------------------ 
 */
package beanAction;


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

import bean.Global;
import bean.Resources;
import beanForm.CentrosCostoForm;
import beanUtil.ConnectDB;

public class CentrosCostoAction extends Action {
	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	
	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		CentrosCostoForm clientDestEntryForm = (CentrosCostoForm) form;
		CentrosCostoForm clientDestEntryForm1 = (CentrosCostoForm) form;
		String mode = clientDestEntryForm.getMode();
		String path = "success";
		//String branchId = null;//AAP02
		String branchname = null;
		//String guiaSeq = null;//AAP02
		Connection con = null;
		//String passMode = null;//AAP02
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
			//passMode = request.getParameter("mode");//AAP02
			clientId = (String) session.getAttribute("sClientId");
			clientDestEntryForm.setErrorMessages("");

			if ("saveAndBack".equalsIgnoreCase(mode)) {
				boolean flag = getErrorMessage(con, clientDestEntryForm, clientId);
				if (flag) { 
					path = "error_page";
				} else {
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
								updateRecords(clientDestEntryForm, con, session);
							}
						} catch (Exception exe) {
							AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("perform()Error1:").append(exe).toString());
							exe.printStackTrace();
						} finally {
							resources.closeResources(rs, psmt);						
						}
						//path = "main_page";
					} else {
						// save Destination client entry details to database
						clientDestEntryForm.setErrorMessages("");
						saveRecords(clientDestEntryForm, con, session);
						con.commit();												
					}
					path = "success";
				}
			} else if ("loaddetail".equalsIgnoreCase(mode)) {
				boolean buscarNumInt = false;
				boolean buscar = false;				
				String clientDestId = "";
				String clientDestTemp = "";
				String clientDestPQId = clientDestEntryForm.getCodigoInt();	
				String rfc = "";//AAP03
				String rfc1 = "";//AAP03
				String rfc2 = "";//AAP03
				String rfc3 = "";//AAP03
				String tipoCliente = "";//AAP03
				
				/*obtiene numero de paquetexpress con numero interno capturado en pantalla*/
				if (clientDestPQId.length() != 0) {
					clientDestTemp = getClienteDestino(con, clientId, clientDestPQId);						
					/*asigna el cliente destino obtenido con el cliente interno, a la variable clientDestId 
					 * para realizar busqueda en sys_clnt_mstr*/
					if (clientDestTemp.length() != 0) {
						clientDestId = clientDestTemp;
					}
				}
				
				/*si no obtuvo cliente paquetexpress (viene de buscar por numero interno) vuelve a asignar el codigo de cliente paquetexpress
				 * para validar si esta con datos para realizar búsqueda*/
				if (clientDestId.length()==0) {
					clientDestId = clientDestEntryForm.getCodigoCliente();
					
					/*si no viene capturado codigo de paquetexpress, envia mensaje de que no existe codigo interno... si no, activa bandera
					 * para realizar busqueda en sys_clnt_mstr*/
					if (clientDestId.length() == 0) {
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
						ArrayList datos = getClienteDestinoInt(con, clientId, clientDestId);
						
						/*si no obtiene registros, el cliente destino paquete express no existe en WEB_ORGN_DEST_CLNT para el cliente origen*/
						if (datos.isEmpty()) {
							clientDestEntryForm.setErrorMessages("Por favor, capture un número Paquetexpress de cliente VALIDO para modificar.");
							clientDestEntryForm = clientDestEntryForm1;
							buscar = false;// desactiva bandera para no realizar busqueda en sys_clnt_mstr
						} else {
							clientDestEntryForm.setCodigoInt(datos.get(0).toString());
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
								
								if (rfc.length()>0) {//AAP03
									rfc1 =  rfc.substring(0,rfc.indexOf('-') );//AAP03
									rfc  =  rfc.substring(rfc.indexOf('-')+1 );//AAP03
									rfc2 =  rfc.substring(0,rfc.indexOf('-') );//AAP03
									rfc  =  rfc.substring(rfc.indexOf('-')+1 );//AAP03
									rfc3 =  rfc;//AAP03
									
									clientDestEntryForm.setRfc1(rfc1);//AAP03
									clientDestEntryForm.setRfc2(rfc2);//AAP03
									clientDestEntryForm.setRfc3(rfc3);//AAP03
								}//AAP03
							}//AAP03
						}//AAP03
					}
				}
			} else if ("getProcedure".equalsIgnoreCase(mode)) {
				branchname = clientDestEntryForm.getSucursal();
				getProcedureValues(clientDestEntryForm, branchname);
			} else if ("postalDetails".equalsIgnoreCase(mode)) {
				getPostalDetails(clientDestEntryForm, con);
				path = "success";
			/*} else if (mode == null || mode.equals("") || passMode.equals("success")) {//AAP02
				branchId = (String) session.getAttribute("sAssignedBranch");//AAP02
				guiaSeq = "CLIENT";//AAP02
				generatSeqNum(clientDestEntryForm, branchId, guiaSeq);*///AAP02
			} else if ("main_page".equalsIgnoreCase(mode)) {
				path = "main_page";
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
	private String getClientValues(CentrosCostoForm clientDestEntryForm, Connection con, String client) {
		CallableStatement cst = null;
		
		String Email = "";
		String cityname = "";
		String colony = "";
		String streetname = "";
		String drnr = "";
		String telefono = "";
		String clientname = "";
		String municipal = "";
		String pais = "";
		String estada = "";
		String zone = "";
		String postal = "";
		String branchid = "";
		String branchname = "";
		String eadcheck = "";
		String GeLevl = "";
		String GeType = "";
		String GeCode = "";
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
			GeLevl = (cst.getString(16) != null ? cst.getString(16) : "");
			GeType = (cst.getString(17) != null ? cst.getString(17) : "");
			GeCode = (cst.getString(18) != null ? cst.getString(18) : "");
			postallevel = (cst.getString(19) != null ? cst.getString(19) : "");
			postaltype = (cst.getString(20) != null ? cst.getString(20) : "");
			postalcode = (cst.getString(21) != null ? cst.getString(21) : "");			
			Email = (cst.getString(22) != null ? cst.getString(22) : "");
			
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
			clientDestEntryForm.setGeLevl(GeLevl);// 18
			clientDestEntryForm.setGeCode(GeCode);// 19
			clientDestEntryForm.setGeType(GeType);// 20
			clientDestEntryForm.setLevel(postallevel);// 21
			clientDestEntryForm.setCode(postalcode);// 22
			clientDestEntryForm.setType(postaltype);// 23			
			clientDestEntryForm.setEmail(Email);// 24		
			clientDestEntryForm.setCodigoCliente(client);// 25
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getClientValues()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);			
		}
		return clientname;
	}

	// Method By Susheel V Rao
	private void getProcedureValues(CentrosCostoForm clientDestEntryForm, String branchName) {
		CallableStatement cst = null;
		Connection con = null;

		String cityName = "";
		String type = "";
		String code = "";
		String level = "";
		String refNo = "";

		try {
			con = ConnectDB.getConnection();

			cst = con.prepareCall("{ call pack_web.pro_get_city(?,?,?,?,?,?) }	");

			cst.setString(1, branchName);
			cst.registerOutParameter(2, Types.VARCHAR);
			cst.registerOutParameter(3, Types.VARCHAR);
			cst.registerOutParameter(4, Types.VARCHAR);
			cst.registerOutParameter(5, Types.VARCHAR);
			cst.registerOutParameter(6, Types.VARCHAR);

			cst.executeQuery();

			cityName = (cst.getString(2) != null ? cst.getString(2) : "");
			type = (cst.getString(3) != null ? cst.getString(3) : "");
			level = (cst.getString(4) != null ? cst.getString(4) : "");
			code = (cst.getString(5) != null ? cst.getString(5) : "");
			refNo = (cst.getString(6) != null ? cst.getString(6) : "");
			
			clientDestEntryForm.setCiudad(cityName);
			clientDestEntryForm.setType(type);
			clientDestEntryForm.setCode(code);
			clientDestEntryForm.setLevel(level);
			clientDestEntryForm.setRefNo(refNo);

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getProcedureValues()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
			resources.cerrarConexion(con);
		}
	}
	
	private void generatSeqNum(CentrosCostoForm clientDestEntryForm,
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
	private void getPostalDetails(
			CentrosCostoForm clientDestEntryForm, Connection con) {

		CallableStatement cst = null;
		// IN paratmeters
		String checkStruct = null;
		String refNo = null;
		String gcode = null;
		String glevel = null;
		String gtype = null;

		// OUT paratmeters
		String u11 = null;
		String u12 = null;
		String u13 = null;
		String u15 = null;

		try {
			checkStruct = clientDestEntryForm.getEstructurada();
			refNo = clientDestEntryForm.getRefNo();
			gcode = clientDestEntryForm.getGeCode();
			glevel = clientDestEntryForm.getGeLevl();
			gtype = clientDestEntryForm.getGeType();

			cst = con.prepareCall("{ call pack_web.pro_ftch_addr_postal(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			cst.setString(1, (checkStruct == null) ? "" : checkStruct);
			cst.setString(2, (refNo == null) ? "" : refNo);
			cst.setString(3, (gcode == null) ? "" : gcode);
			cst.setInt(4, (glevel == null || glevel.toString().trim().length() == 0) ? 0 : Integer.parseInt(glevel));
			cst.setInt(5, (gtype == null || gtype.toString().trim().length() == 0) ? 0 : Integer.parseInt(gtype));

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

			cst.executeQuery();

			u11 = (cst.getString(6) != null ? cst.getString(6) : "");
			u12 = (cst.getString(7) != null ? cst.getString(7) : "");
			u13 = (cst.getString(8) != null ? cst.getString(8) : "");
			u15 = (cst.getString(10) != null ? cst.getString(10) : "");

			// Below field values are going to show to the Clients
			clientDestEntryForm.setPais(u11);
			clientDestEntryForm.setZona(u12);
			clientDestEntryForm.setEstado(u13);
			clientDestEntryForm.setMunicipio(u15);

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getPostalDetails()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
	}	
	// This method is used to get Postal Details
	private void saveRecords(CentrosCostoForm clientDestEntryForm,
			Connection con, HttpSession session) {

		PreparedStatement psmt = null;
		ResultSet rs = null;
		String insertQuery = null;
		String seqNo = null;
		String clientName = null;
		String clientId = null;

		String CodigoClienteHid = null;
		// Added By Kavitha on MAY 18TH 2011
		String Email = null;
		String AssignedToBranch = null;
		String Telefono = null;
		String Calle = null;
		String Numero = null;
		String Sucursal = null;
		String GeLevl = null;
		String GeType = null;
		String GeCode = null;
		String siteId = "";
		
		try {
			Global global =(Global) session.getAttribute("sGlobal");//AAP03
			String branchId = (String) session.getAttribute("sAssignedBranch");//AAP02	
			generatSeqNum(clientDestEntryForm, branchId, "CLIENT");//AAP02
			CodigoClienteHid = clientDestEntryForm.getCodigoClienteHid();
			Email = clientDestEntryForm.getEmail();
			AssignedToBranch = clientDestEntryForm.getAssignedToBranch();
			Telefono = clientDestEntryForm.getTelefono();
			Calle = clientDestEntryForm.getCalle();
			Numero = clientDestEntryForm.getNumero();
			// Added By Sam.D.Jabeen[06-06-2006] , value of this field will always be as the AssignedToBranch
			Sucursal = AssignedToBranch;
			GeLevl = clientDestEntryForm.getGeLevl();
			GeType = clientDestEntryForm.getGeType();
			GeCode = clientDestEntryForm.getGeCode();

			clientId = (String) session.getAttribute("sClientId");
			clientName = getClientName(con, clientId);

			String sitequery = "select BM_BRNC_SITE_ID from SYS_BRNC_MSTR where BM_BRNC_ID = ?";
			
			psmt = con.prepareStatement(sitequery);
			psmt.setString(1, Sucursal);
			
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				siteId = rs.getString(1);
			}
			rs.close();
			psmt.close();
			insertQuery = cnct.delete(0, cnct.length())
					.append("INSERT INTO sys_addr_mstr (am_phno1, am_strt_name, am_drnr, am_pe_site_id, am_addr_code, am_addr_styp, am_addr_type, am_enty_id, am_gety_levl, am_gety_type, am_gety_code, am_addr_id, am_defa_flag, am_addr_defn_type, am_enty_desc, crtd_on, crtd_by, mdfd_on, mdfd_by, AM_MAIL_ID)")
					.append(" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, ?, sysdate, ?,?)").toString();

			seqNo = getSeqNo(con);
			psmt = con.prepareStatement(insertQuery);
			psmt.setString(1, Telefono == null ? "" : Telefono.toUpperCase());
			psmt.setString(2, Calle == null ? "" : Calle.toUpperCase());
			psmt.setString(3, Numero == null ? "" : Numero.toUpperCase());
			psmt.setString(4, siteId == null ? "" : siteId);
			psmt.setString(5, seqNo == null ? "" : seqNo);
			psmt.setString(6, "FISCAL");
			psmt.setString(7, "CLNT");
			psmt.setString(8, CodigoClienteHid == null ? "" : CodigoClienteHid);
			psmt.setString(9, GeLevl == null ? "" : GeLevl);
			psmt.setString(10, GeType == null ? "" : GeType);
			psmt.setString(11, GeCode == null ? "" : GeCode);			
			psmt.setString(12, "1");
			psmt.setString(13, "Y");
			psmt.setString(14, "Y");			
			psmt.setString(15, clientName == null ? "" : clientName);
			psmt.setString(16, clientId == null ? "" : clientId);
			psmt.setString(17, clientId == null ? "" : clientId);
			psmt.setString(18, Email == null ? "" : Email);

			psmt.executeUpdate();
			
			insertSys_clnt_mstr(con, clientDestEntryForm, clientId, siteId, global.getAllowedFXC());//AAP03

			insertWeb_orgin_dest_client(con, clientDestEntryForm, clientId, siteId);

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("saveRecords()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(psmt);
		}
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
	private void insertSys_clnt_mstr(Connection con,
			CentrosCostoForm clientDestEntryForm, 
			String clientId, String siteId, String allowedFXC) {//AAP03
		PreparedStatement psmt = null;
		
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
				.append("cm_bsns_line_id, cm_bsns_seg_id)")
				.append("VALUES (?,?, 0, NULL, 0, NULL, NULL, 'DIS',?, 'N',NULL, NULL, NULL, NULL,")
				.append(" ?, ?,sysdate,?, sysdate,?, 'Y', NULL, NULL, NULL, 'N', 'N', NULL, NULL, NULL, NULL, 'Y', ")
				.append(" NULL, NULL, ?, NULL, ?, ?, ?, ?, ?) ").toString();//AAP03

		try {
			// Added By Sam [06-06-2006] , The client type always will be type
			// “I” Individuals
			//String strTipoCliente = "I";//AAP03
			
			String strTipoCliente = clientDestEntryForm.getTipoCliente();//AAP03
			String rfc = "";//AAP03
			String retieneIva = "";//AAP03
			if (clientDestEntryForm.getTipoCliente().equals("I")) {//AAP03
				if (clientDestEntryForm.getRfc1().length() == 0) {//AAP03
					rfc = "XXXX-010101-XXX";//AAP03
					retieneIva = "N";//AAP03
				}//AAP03
			} else {//AAP03
				if (clientDestEntryForm.getRfc1().length() == 0) {//AAP03
					rfc = "XXX-010101-XXX";//AAP03
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
			psmt.setString(2, clientDestEntryForm.getNombre().toUpperCase());
			psmt.setString(3, strTipoCliente);
			psmt.setString(4, allowedFXC);//AAP03
			psmt.setString(5, clientDestEntryForm.getEadCheck());
			psmt.setString(6, clientId);
			psmt.setString(7, clientId);
			psmt.setString(8, siteId);
			psmt.setString(9, clientDestEntryForm.getCodigoInt());
			psmt.setString(10, rfc);//AAP03
			psmt.setString(11, retieneIva);//AAP03
			psmt.setString(12, "9999");//AAP03
			psmt.setString(13, "0099");//AAP03
			
			psmt.executeUpdate();

		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("insertSys_clnt_mstr()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(psmt);
		}
	}	
	
	// By Susheel insertWeb_orgin_dest_client

	private void insertWeb_orgin_dest_client(Connection con,
			CentrosCostoForm clientDestEntryForm, String clientId,
			String siteId) {
		PreparedStatement psmt = null;
		String strQuery = cnct.delete(0,cnct.length())
				.append("INSERT INTO WEB_ORGN_DEST_CLNT ( WO_ORGN_CLNT_ID, WO_DEST_CLNT_ID, CRTD_ON,")
				.append("CRTD_BY, MDFD_ON,MDFD_BY, WO_DEST_SITE_ID,WO_DEST_CUST_CLNT_ID ) VALUES ")
				.append("(?, ?,sysdate, ? ,sysdate, ?,?,?)").toString();

		try {
			psmt = con.prepareStatement(strQuery);

			psmt.setString(1, clientId);
			psmt.setString(2, clientDestEntryForm.getCodigoClienteHid());
			psmt.setString(3, clientId);
			psmt.setString(4, clientId);

			psmt.setString(5, siteId);
			psmt.setString(6, clientDestEntryForm.getCodigoInt());

			psmt.executeUpdate();

		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("insertWeb_orgin_dest_client()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(psmt);
		}
	}
	
	// By Susheel for Errror message
	private boolean getErrorMessage(Connection con, CentrosCostoForm clientDestEntryForm, String clienteOrigen) {		
		String numInt = null;
		String clientDestId = null;
		String Nombre = null;
		String Ciudad = null;
		String assign = null;
		String numero = null;
		String calle = null;
		String codigoPostal = null;
		boolean flag = false;

		try {
			numInt = clientDestEntryForm.getCodigoInt();
			Nombre = clientDestEntryForm.getNombre();
			Ciudad = clientDestEntryForm.getCiudad();
			assign = clientDestEntryForm.getAssignedToBranchRef();
			numero = clientDestEntryForm.getNumero();
			calle = clientDestEntryForm.getCalle();
			codigoPostal = clientDestEntryForm.getCodigoPostal();
			clientDestEntryForm.setErrorMessages("");
			if (numInt != null && numInt.length() > 0) {
				clientDestId = getClienteDestino(con,clienteOrigen,numInt);
				if (clientDestId.length()>0 && !clientDestId.trim().equals( clientDestEntryForm.getCodigoCliente().trim() )) {
					String msje = cnct.delete(0,cnct.length()).append("Número interno. El ").append(numInt).append(" ya existe en cliente Número ").append(clientDestId).append(".").toString();
					getError(con, "SYS", 800067, clientDestEntryForm, msje);//AAP01
					flag = true;					
				}
			}
			
			if (!flag) {
				if (Nombre == null || Nombre.length() == 0) {
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
				} else if (Ciudad == null || Ciudad.length() == 0) {
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
						//clientDestEntryForm.setErrorMessages("No hay cobertura para esta dirección. Favor de comunicarse a su sucursal PAQUETEXPRESS para verificar cobertura.");
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
	private void getError(Connection con, String modulo, int idError, CentrosCostoForm clientDestEntryForm,
			String tagID) {
		CallableStatement cst = null;		
		String msgtext = "";

		try {
			cst = con.prepareCall("{ call pack_web.PRO_SHOW_MESG(?,pack_web.language_id,?,1,?,NULL,NULL,?,?) }	");//AAP01

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
	
	
	private void getLabelEnteraDelClient(CentrosCostoForm clientDestEntryForm, Connection con) {
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
	
	private void updateRecords(CentrosCostoForm clientDestEntryForm,
			Connection con, HttpSession session) {

		PreparedStatement psmt = null;
		ResultSet rs = null;
		String clientName = null;
		String clientId = null;
		String pqId = null;

		String AssignedToBranch = null;
		String Telefono = null;
		String Calle = null;
		String Numero = null;
		String Sucursal = null;
		String EadCheck = null;
		String mailId = null;
		String GeLevl = null;
		String GeType = null;
		String GeCode = null;

		try {
			Global global = (Global) session.getAttribute("sGlobal");//AAP03
			pqId = clientDestEntryForm.getCodigoInt();
			AssignedToBranch = clientDestEntryForm.getAssignedToBranch();			
			Telefono = clientDestEntryForm.getTelefono();			
			Calle = clientDestEntryForm.getCalle();
			Numero = clientDestEntryForm.getNumero();

			// Added By Sam.D.Jabeen[06-06-2006] , value of this field will
			// always be as the AssignedToBranch
			Sucursal = AssignedToBranch;
			EadCheck = clientDestEntryForm.getEadCheck();
			mailId = clientDestEntryForm.getEmail();
			GeLevl = clientDestEntryForm.getGeLevl();
			GeType = clientDestEntryForm.getGeType();
			GeCode = clientDestEntryForm.getGeCode();
			clientId = (String) session.getAttribute("sClientId");
			// To Get Client Name from the data base
			clientName = getClientName(con, clientId);
			
			String siteId = "";
			String query = "select BM_BRNC_SITE_ID from SYS_BRNC_MSTR where BM_BRNC_ID = ?";
			
			String strTipoCliente = clientDestEntryForm.getTipoCliente();//AAP03
			String rfc = "";//AAP03
			if (clientDestEntryForm.getTipoCliente().equals("I")) {//AAP03
				if (clientDestEntryForm.getRfc1().length() == 0) {//AAP03
					rfc = "XXXX-010101-XXX";//AAP03
				}//AAP03
			} else {//AAP03
				if (clientDestEntryForm.getRfc1().length() == 0) {//AAP03
					rfc = "XXX-010101-XXX";//AAP03
				}//AAP03
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
			
			
			try {
				psmt = con.prepareStatement(query);
				psmt.setString(1, Sucursal);
				rs = psmt.executeQuery();				
				while (rs.next()) {
					siteId = rs.getString(1);
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("updateRecords()Error1:").append(e).toString());
				e.printStackTrace();
			} finally {
				resources.closeResources(rs, psmt);				
			}			

			String updateQuery = "update sys_addr_mstr set am_phno1 = ?, am_strt_name = ?, am_drnr = ?, am_pe_site_id = ?, am_gety_levl = ?, am_gety_type = ?, am_gety_code = ?, am_enty_desc = ?, mdfd_on = sysdate, mdfd_by = ?, am_mail_id = ? where am_enty_id= ? and am_defa_flag = ? and am_addr_type = ?";
			
			psmt = con.prepareStatement(updateQuery);
			psmt.setString(1, (Telefono == null) ? "" : Telefono.toUpperCase());
			psmt.setString(2, (Calle == null) ? "" : Calle.toUpperCase());
			psmt.setString(3, (Numero == null) ? "" : Numero.toUpperCase());
			psmt.setString(4, (siteId == null) ? "" : siteId);
			psmt.setString(5, (GeLevl == null) ? "" : GeLevl);
			psmt.setString(6, (GeType == null) ? "" : GeType);
			psmt.setString(7, (GeCode == null) ? "" : GeCode);
			psmt.setString(8, (clientName == null) ? "" : clientName);
			psmt.setString(9, (clientId == null) ? "" : clientId);
			psmt.setString(10, (mailId == null) ? "" : mailId); 
			psmt.setString(11, clientDestEntryForm.getCodigoCliente());
			psmt.setString(12, "Y");
			psmt.setString(13, "CLNT");

			psmt.executeUpdate();
			
			psmt.close();

			// update Sys_clnt_mstr
			updateQuery = "update sys_clnt_mstr set cm_clnt_name = ?, cm_ead_flag = ?, cm_asgn_to_site = ?, CM_CUST_CLNT_ID = ?, cm_dest_paid_flag = ?, CM_CLNT_TYPE = ?, CM_TAX_ID = ? where cm_clnt_id = ?";
			psmt = con.prepareStatement(updateQuery);
			psmt.setString(1, (clientDestEntryForm.getNombre() == null) ? "" : clientDestEntryForm.getNombre());
			psmt.setString(2, (EadCheck == null) ? "" : EadCheck);
			psmt.setString(3, (siteId == null) ? "" : siteId);
			psmt.setString(4, (pqId == null) ? "" : pqId);
			psmt.setString(5, global.getAllowedFXC());//AAP03
			psmt.setString(6, clientDestEntryForm.getTipoCliente());//AAP03
			psmt.setString(7, rfc);//AAP03
			psmt.setString(8, clientDestEntryForm.getCodigoCliente());

			psmt.executeUpdate();
			
			psmt.close();
			
			/*actualiza site en general para cliente destino,
			 * modifica todos los registros donde exista el cliente destino*/
			updateQuery = "UPDATE WEB_ORGN_DEST_CLNT SET WO_DEST_SITE_ID = ? WHERE WO_DEST_CLNT_ID = ?";
			psmt = con.prepareStatement(updateQuery);
			psmt.setString(1, (siteId == null) ? "" : siteId);
			psmt.setString(2, clientDestEntryForm.getCodigoCliente());
			
			psmt.executeUpdate();
			
			psmt.close();
			
			/*actualiza numero interno de cliente destino solo para el cliente origen que esta realizando la actualizacion,
			 * modifica modifica solamente el registro para el cliente origen*/
			updateQuery = "UPDATE WEB_ORGN_DEST_CLNT SET WO_DEST_CUST_CLNT_ID = ? WHERE WO_ORGN_CLNT_ID = ? AND WO_DEST_CLNT_ID = ?";
			
			psmt = con.prepareStatement(updateQuery);
			psmt.setString(1, (pqId == null) ? "" : pqId);
			psmt.setString(2, clientId);
			psmt.setString(3, clientDestEntryForm.getCodigoCliente());
			
			psmt.executeUpdate();
			
			con.commit();
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("updateRecords()Error2:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(psmt);			
		}
	}
	
	/****************************************************************************************
	 * Busca cliente destino con numero de cliente interno									*
	 ****************************************************************************************/
	private String getClienteDestino(Connection con, String clienteOrigen, String numIntDestino) {
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String clienteDestino = "";
		String query = "SELECT WO_DEST_CLNT_ID FROM WEB_ORGN_DEST_CLNT WHERE WO_ORGN_CLNT_ID = ? AND WO_DEST_CUST_CLNT_ID = ?";
		try{
			psmt = con.prepareStatement(query);
			psmt.setString(1, clienteOrigen);
			psmt.setString(2, numIntDestino);
			rs = psmt.executeQuery();
			
			while(rs.next()){				
				clienteDestino = rs.getString(1) == null ? "" : rs.getString(1); 			
			}			
		} catch(Exception e) {			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getClienteDestino()Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.closeResources(rs, psmt);
		}
		return clienteDestino;
	}
	
	/****************************************************************************************
	 * Busca cliente destino con numero de cliente interno									*
	 ****************************************************************************************/
	private ArrayList getClienteDestinoInt(Connection con, String clienteOrigen, String numDestino) {
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		ArrayList datos = new ArrayList(1);
		String query = "SELECT WO_DEST_CUST_CLNT_ID FROM WEB_ORGN_DEST_CLNT WHERE WO_ORGN_CLNT_ID = ? AND WO_DEST_CLNT_ID = ?";
		try{
			psmt = con.prepareStatement(query);
			psmt.setString(1, clienteOrigen);
			psmt.setString(2, numDestino);
			
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				datos.add(rs.getString(1) == null ? "" : rs.getString(1));				
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
	private String validaCobertura(Connection con, CentrosCostoForm clientDestEntryForm){
		PreparedStatement pst = null;
		ResultSet rs = null;
		String cobertura = "N";
		
		try {			
			pst = con.prepareStatement("SELECT BC_ZONE FROM DW_COBERTURA_VIEW WHERE COLO_ZIPCODE = ? AND COLO_DES = ?");
			
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
	
	private ArrayList getClientInfo(Connection con, String clientId) {//AAP03
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList result = null;
		String query = "SELECT CM_TAX_ID, CM_CLNT_TYPE FROM SYS_CLNT_MSTR WHERE CM_CLNT_ID = ?";

		try {
			pst = con.prepareStatement(query);
			pst.setString(1, clientId);

			rs = pst.executeQuery();

			if (rs.next()) {
				result = new ArrayList(rs.getFetchSize());
				result.add(rs.getString(1) == null ? "" : rs.getString(1));
				result.add(rs.getString(2) == null ? "I" : rs.getString(2));
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
}