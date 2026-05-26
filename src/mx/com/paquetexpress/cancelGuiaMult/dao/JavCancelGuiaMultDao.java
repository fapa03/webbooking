/**
 * @author: PACHECO PARRA EVA MELISSA
 * Fecha de Creaci�n: 10/12/2023
 * Compa��a: PAQUETEXPRESS.
 * Descripci�n del programa: Bean DAO para pantalla de cancelacion de guias masivas.
 * FileName: JavCancelGuiaMultDao.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n: 
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n: 
 * 
 * ------------------------------------------------------------------ 
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n: 
 * 
 * ------------------------------------------------------------------ 
 */
package mx.com.paquetexpress.cancelGuiaMult.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bean.Global;
import bean.Resources;
import beanUtil.ConnectDB;
import logger.AccessLog;
import mx.com.paquetexpress.cancelGuiaMult.dto.JavCancelGuiaMultDTO;
import mx.com.paquetexpress.cancelGuiaMult.form.JavCancelGuiaMultForm;

public class JavCancelGuiaMultDao {
	private StringBuffer cnct = new StringBuffer();
	private Connection con;
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	@SuppressWarnings({ "rawtypes", "unchecked" })
	
	public JavCancelGuiaMultForm getGuiasByClient( String webClientId, String branchCCosto, String rangeDayI, String rangeDayF) {		
		String strSqlQuery = "";
		PreparedStatement pst = null;
		CallableStatement cst = null;
		ResultSet rs = null;
		ArrayList listReprintGuiaMultDTO = new ArrayList();
		JavCancelGuiaMultForm reprintGuiaMultForm = new JavCancelGuiaMultForm();
		JavCancelGuiaMultDTO reprintGuiaMultDTO = new JavCancelGuiaMultDTO();
		try {
			con = ConnectDB.getConnection();
			strSqlQuery = cnct.delete(0, cnct.length())
					.append("SELECT GH_FORM_NO, GH_GUIA_NO, GH_DEST_BRNC_ID, GH_DEST_CLNT_NAME, to_char(GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT, GH_NUMB_PACK, GH_ORGN_BRNC_ID, (to_char (GH_ISSE_DATE + NVL(FUN_GET_TIME_SITE(GH_ORGN_BRNC_ID),0)/86400 , 'DD/MM/YYYY HH24:MI:SS')) ISSE_DATE, FUN_GET_REFRS_GUIA(GH_GUIA_NO) REFERS ")
					.append("FROM BOK_GUIA_HEAD ")
					.append("WHERE GH_ORGN_CLNT_ID = ? ")
					.append("AND (GH_GUIA_TYPE = ? OR GH_DOCU_TYPE = ?) ")
					.append("AND GH_ACTV_FLAG = ? ")
					.append("AND GH_RAD_FLAG IN (?, ?) ")
					.append("AND GH_GUIA_REFR_NO IS NULL ")
					.append("AND GH_ORGN_BRNC_ID = ? ")
					.append("AND GH_ISSE_DATE >= TRUNC(TO_DATE( ?, 'DD/MM/YYYY'))  ")	
					.append("AND GH_ISSE_DATE < TRUNC(TO_DATE(?, 'DD/MM/YYYY')) + 1 ")
					.append("AND substr(GH_FORM_NO,0,2) <> ? ")
					.append("ORDER BY GH_ISSE_DATE DESC").toString();
			pst = con.prepareStatement(strSqlQuery);
			pst.setString(1, webClientId);
			pst.setString(2, "H");
			pst.setString(3, "Q");
			pst.setString(4, "A");
			pst.setString(5, "1"); 
			pst.setString(6, "6");
			pst.setString(7, branchCCosto);
			pst.setString(8, rangeDayI); 
			pst.setString(9, rangeDayF);
			pst.setString(10, "WE");
			 
			rs = pst.executeQuery();

			String strDestBranchName = "";
			while (rs.next()) {
				reprintGuiaMultDTO = new JavCancelGuiaMultDTO();
				reprintGuiaMultDTO.setFormNumber((rs.getString("GH_FORM_NO") == null ? "" : (rs.getString("GH_FORM_NO"))));	
				reprintGuiaMultDTO.setGuiaNumber((rs.getString("GH_GUIA_NO") == null ? "" : (rs.getString("GH_GUIA_NO"))));
				reprintGuiaMultDTO.setGuiaAmount((rs.getString("GH_GUIA_AMNT") == null ? "" : (rs.getString("GH_GUIA_AMNT"))));
				reprintGuiaMultDTO.setNumPack((rs.getString("GH_NUMB_PACK") == null ? "" : (rs.getString("GH_NUMB_PACK"))));
				reprintGuiaMultDTO.setDestClientName((rs.getString("GH_DEST_CLNT_NAME") == null ? "" : (rs.getString("GH_DEST_CLNT_NAME"))));
				reprintGuiaMultDTO.setOrigBranch((rs.getString("GH_ORGN_BRNC_ID") == null ? "" : (rs.getString("GH_ORGN_BRNC_ID"))));
				reprintGuiaMultDTO.setIsseDate( rs.getString("ISSE_DATE") == null ? "" : rs.getString("ISSE_DATE") );
				reprintGuiaMultDTO.setRefers( rs.getString("REFERS") == null ? "" : rs.getString("REFERS") );//AAP01
				
				String StrDestBrnachId = rs.getString("GH_DEST_BRNC_ID");

				cst = con.prepareCall("begin ? := pack_web.Fun_ftch_brnc_name(?); end;");
				cst.registerOutParameter(1, Types.VARCHAR);
				cst.setString(2, StrDestBrnachId);
				cst.execute();
				strDestBranchName = (cst.getString(1) == null ? "" : cst.getString(1));
				reprintGuiaMultDTO.setDestBranch(strDestBranchName);
				reprintGuiaMultDTO.setChecked(true);
				listReprintGuiaMultDTO.add(reprintGuiaMultDTO);
				cst.close();
			}	
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getGuiasByClient()_Error:").append(e)
					.toString());	
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
			resources.cerrarCallableStatement(cst);
			resources.cerrarConexion(con);
		}
		listReprintGuiaMultDTO = getEtiquetasByGuia(listReprintGuiaMultDTO);
		reprintGuiaMultForm.setListGuias(listReprintGuiaMultDTO);
		return reprintGuiaMultForm;
	}
	
	//extrae listado para guias WE y lo muestra en la tabla
	public JavCancelGuiaMultForm getGuiasByClientWE(String webClientId, Global global, String rangeDayI, String rangeDayF ) {		
		String strSqlQuery = "";
		PreparedStatement pst = null;
		CallableStatement cst = null;
		ResultSet rs = null;
		ArrayList listReprintGuiaMultDTO = new ArrayList();
		JavCancelGuiaMultForm reprintGuiaMultForm = new JavCancelGuiaMultForm();
		JavCancelGuiaMultDTO reprintGuiaMultDTO = new JavCancelGuiaMultDTO();
		try {
			con = ConnectDB.getConnection();
			strSqlQuery = cnct.delete(0, cnct.length())
					.append("SELECT GH_FORM_NO, GH_GUIA_NO, GH_DEST_BRNC_ID, GH_DEST_CLNT_NAME, to_char(GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT, GH_NUMB_PACK, GH_ORGN_BRNC_ID, (to_char (GH_ISSE_DATE + NVL(FUN_GET_TIME_SITE(GH_ORGN_BRNC_ID),0)/86400 , 'DD/MM/YYYY HH24:MI:SS')) ISSE_DATE, FUN_GET_REFRS_GUIA(GH_GUIA_NO) REFERS ")
					.append("FROM BOK_GUIA_HEAD A, BOK_GUIA_ADDR B, BOK_GUIA_HEAD_EXTRA C ")
					.append("WHERE A.GH_GUIA_TYPE = ? ")
					.append("AND A.GH_ACTV_FLAG = ? ")
					.append("AND A.GH_RAD_FLAG IN (?, ?) ")
					.append("AND A.GH_GUIA_REFR_NO IS NULL ")
					.append("AND A.GH_GUIA_NO = B.GA_GUIA_NO ")
					.append("AND B.GA_GUIA_NO = C.GE_GUIA_NO ")
					.append("AND B.GA_ADDR_TYPE = ? ")
					.append("AND C.GE_CLNT_REQT = ? ")	
					.append("AND substr(A.GH_FORM_NO,0,2) = ? ")
					.append("AND A.GH_ISSE_DATE >= TRUNC(TO_DATE( ?, 'DD/MM/YYYY')) ")	
					.append("AND A.GH_ISSE_DATE < TRUNC(TO_DATE(?, 'DD/MM/YYYY')) + 1 ")
					.append("ORDER BY A.GH_ISSE_DATE DESC").toString();
			pst = con.prepareStatement(strSqlQuery);
			pst.setString(1, "H");
			pst.setString(2, "A");
			pst.setString(3, "1"); 
			pst.setString(4, "6");
			pst.setString(5, "ORIGIN");
			pst.setString(6, global.getClientIdAgreement());
			pst.setString(7, "WE"); 
			pst.setString(8, rangeDayI); 
			pst.setString(9, rangeDayF); 
			
			rs = pst.executeQuery();
	
			String strDestBranchName = "";
			while (rs.next()) {
				reprintGuiaMultDTO = new JavCancelGuiaMultDTO();
				reprintGuiaMultDTO.setFormNumber((rs.getString("GH_FORM_NO") == null ? "" : (rs.getString("GH_FORM_NO"))));	
				reprintGuiaMultDTO.setGuiaNumber((rs.getString("GH_GUIA_NO") == null ? "" : (rs.getString("GH_GUIA_NO"))));
				reprintGuiaMultDTO.setGuiaAmount((rs.getString("GH_GUIA_AMNT") == null ? "" : (rs.getString("GH_GUIA_AMNT"))));
				reprintGuiaMultDTO.setNumPack((rs.getString("GH_NUMB_PACK") == null ? "" : (rs.getString("GH_NUMB_PACK"))));
				reprintGuiaMultDTO.setDestClientName((rs.getString("GH_DEST_CLNT_NAME") == null ? "" : (rs.getString("GH_DEST_CLNT_NAME"))));
				reprintGuiaMultDTO.setOrigBranch((rs.getString("GH_ORGN_BRNC_ID") == null ? "" : (rs.getString("GH_ORGN_BRNC_ID"))));
				reprintGuiaMultDTO.setIsseDate( rs.getString("ISSE_DATE") == null ? "" : rs.getString("ISSE_DATE") );
				reprintGuiaMultDTO.setRefers( rs.getString("REFERS") == null ? "" : rs.getString("REFERS") );//AAP01
				
				String StrDestBrnachId = rs.getString("GH_DEST_BRNC_ID");

				cst = con.prepareCall("begin ? := pack_web.Fun_ftch_brnc_name(?); end;");
				cst.registerOutParameter(1, Types.VARCHAR);
				cst.setString(2, StrDestBrnachId);
				cst.execute();
				strDestBranchName = (cst.getString(1) == null ? "" : cst.getString(1));
				reprintGuiaMultDTO.setDestBranch(strDestBranchName);
				reprintGuiaMultDTO.setChecked(true);
				listReprintGuiaMultDTO.add(reprintGuiaMultDTO);
				cst.close();
			}	
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getGuiasByClientWE()_Error:").append(e)
					.toString());	
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
			resources.cerrarCallableStatement(cst);
			resources.cerrarConexion(con);
		}
		listReprintGuiaMultDTO = getEtiquetasByGuia(listReprintGuiaMultDTO);
		reprintGuiaMultForm.setListGuias(listReprintGuiaMultDTO);
		return reprintGuiaMultForm;
	}
	
	@SuppressWarnings("rawtypes")
	
	//Se obtiene el listado que muestra enl
	private ArrayList getEtiquetasByGuia(ArrayList<JavCancelGuiaMultDTO> listReprintGuiaMultDTO) {		
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = ConnectDB.getConnection();
			for (int i = 0; i < listReprintGuiaMultDTO.size(); i++) {
				JavCancelGuiaMultDTO reprintGuiaMultDTO = (JavCancelGuiaMultDTO) listReprintGuiaMultDTO.get(i);
				listReprintGuiaMultDTO.set(i, reprintGuiaMultDTO);
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getEtiquetasByGuia()_Error:").append(e)
					.toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
			resources.cerrarConexion(con);
		}
		return listReprintGuiaMultDTO;
	}
		
	//Hace una validacion de la guia para establecer si es factible o no el rastreo
	public JavCancelGuiaMultDTO invokeFormNumValidityCheck(String rastreo, String brncOrgn, String type, 
			HttpServletRequest request) {
		Connection conn = null;
		CallableStatement cstmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs2 = null;
		String strTemp1 = "", strTemp2 = ""; 
		String idClient = request.getSession(false).getAttribute("sClientId") == null ? ""
				: request.getSession(false).getAttribute("sClientId").toString();; 

		JavCancelGuiaMultDTO cGuiaMultDTO = new JavCancelGuiaMultDTO(); 
		try {
			conn = ConnectDB.getConnection();
			if(type.equals("WE")) {
				String clientOrgn = validRastreoWE(conn, rastreo, idClient); 
				strTemp1 = clientOrgn;
				strTemp2 = brncOrgn;	
			}else {
				strTemp1 = request.getSession(false).getAttribute("sClientId") == null ? ""
					: request.getSession(false).getAttribute("sClientId").toString();
				strTemp2 = request.getSession(false).getAttribute("sAssignedBranch") == null ? ""
					: request.getSession(false).getAttribute("sAssignedBranch").toString();	
			}
			
			cstmt = conn.prepareCall("call pack_web.pro_ftch_guia_for_canc_DE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			cstmt.setString(1, strTemp1); // orgn_client_id
			cstmt.setString(2, strTemp2); // orgn_brnc_id
			cstmt.setNull(3, Types.VARCHAR); // form_no
			cstmt.setString(4, rastreo); // guia_no

			cstmt.registerOutParameter(2, java.sql.Types.VARCHAR); // orgn_brnc_id
			cstmt.registerOutParameter(3, java.sql.Types.VARCHAR); // form_no
			cstmt.registerOutParameter(4, java.sql.Types.VARCHAR); // guia_no
			cstmt.registerOutParameter(5, java.sql.Types.VARCHAR); // brnc_name
			cstmt.registerOutParameter(6, java.sql.Types.VARCHAR); // dest_clnt_name
			cstmt.registerOutParameter(7, java.sql.Types.VARCHAR); // isse_date
			cstmt.registerOutParameter(8, java.sql.Types.NUMERIC); // guia_amnt
			cstmt.registerOutParameter(9, java.sql.Types.VARCHAR); // mesg_type
			cstmt.registerOutParameter(10, java.sql.Types.VARCHAR); // mesg_text
			cstmt.registerOutParameter(11, java.sql.Types.VARCHAR); // mesg_text
			cstmt.registerOutParameter(12, java.sql.Types.VARCHAR); // DocuType
			cstmt.registerOutParameter(13, java.sql.Types.NUMERIC); // mesg_id

			cstmt.executeQuery();

			request.setAttribute("errorMessageText", cstmt.getString(10));

			if (cstmt.getString(10) == null) {
				request.setAttribute("PrepBrncId", cstmt.getString(2));
				
				cGuiaMultDTO.setOrigBranch(cstmt.getString(2));
				cGuiaMultDTO.setFormNumber(cstmt.getString(3));
				cGuiaMultDTO.setGuiaNumber(cstmt.getString(4));
				cGuiaMultDTO.setDestBranch(cstmt.getString(5));
				cGuiaMultDTO.setDestClientName(cstmt.getString(6));
				cGuiaMultDTO.setIsseDate(cstmt.getString(7));
				cGuiaMultDTO.setGuiaAmount(cstmt.getString(8));
				request.setAttribute("DestSiteName", cstmt.getString(11));
				cGuiaMultDTO.setDocuType(cstmt.getString(12));

				cGuiaMultDTO.setStatusRastreo("INFO");
				cGuiaMultDTO.setMsjStatus("PROCESO EXITOSO");
			} else {
				cGuiaMultDTO.setStatusRastreo("ERRO");
				cGuiaMultDTO.setMsjStatus("ERROR AL VALIDAR RASTREO");
			}

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("invokeFormNumValidityCheck()Error:")
					.append(e).toString());
			request.setAttribute("errorMessageText", e.getMessage());
		} finally {
			resources.closeResources(rs2, pstmt, cstmt);
			resources.cerrarResultSet(rs2);
			resources.cerrarConexion(conn);
		}
		return cGuiaMultDTO;
	}

	public void invokeGuiaCancellation( JavCancelGuiaMultDTO gcnclFrm, HttpServletRequest request) {
		Connection conn = null;
		CallableStatement cstmt = null;
		HttpSession session = request.getSession();
		String msje = "";
		try {
			String clienteOrigen = (String) session.getAttribute("sClientId");
			Global global = (Global) session.getAttribute("sGlobal");
			conn = ConnectDB.getConnection();
			
			if (gcnclFrm.getDocuType().equals("P")){
				if (cancelarGUIA_PP(conn, gcnclFrm)>0) {
					msje = getError(conn,"BOK", 900177, "");
				} else {
					msje = "Fallo en la Cancelación de la Guía";
				}
			} else {
				cancelarBOK_GUIA_SRVC(conn, gcnclFrm);
				insertToBokGuiaStatus(conn,gcnclFrm, clienteOrigen);
				cancelarBOK_GUIA_HEAD(conn, gcnclFrm);
				
				/*busca clave de autorizacion para zona extendida en BOK_EXT_DETL.
				 * Si encuentra registro, vuelve a poner disponible la clave para volverla a utilizar*/
				activateEspExtSrvc(conn, gcnclFrm, global);
				
				//BORRAR REFERENCIA DE ACUSE XT
				String desactiva = "{call PACK_ACUSES_XT.PRO_BORRA_GUI_RET(?)}";
				cstmt = conn.prepareCall(desactiva);
				cstmt.setString(1, gcnclFrm.getGuiaNumber());
				cstmt.executeQuery();
				cstmt.close();
			}
			conn.commit();	

			request.setAttribute("errorMessageText", msje);
			
		}catch(Exception e){
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("invokeGuiaCancellation()Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			resources.cerrarCallableStatement(cstmt);
			resources.cerrarConexion(conn);
		}
	}
	
	private String getError(Connection con, String modulo, int msjeID, String tagID) {
		CallableStatement cst = null;
		String msgtext = "";

		try {
			cst = con.prepareCall("{ call pack_web.PRO_SHOW_MESG(?,pack_web.language_id,?,1,?,NULL,NULL,?,?) }	");
			cst.setString(1, modulo);
			cst.setInt(2, msjeID);
			cst.setString(3, tagID);//complemento de mensaje			
			cst.registerOutParameter(4, Types.VARCHAR);
			cst.registerOutParameter(5, Types.VARCHAR);

			cst.executeQuery();
			
			msgtext = (cst.getString(5) != null ? cst.getString(5) : "");
			
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getError()Error:").append(exe).toString());
			exe.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return msgtext;
	}
	
	public int cancelarGUIA_PP(Connection conn, JavCancelGuiaMultDTO gcnclFrm) {
		PreparedStatement pst = null;
		int update = 0;
		CallableStatement cstmt = null;
		try {
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_HEAD WHERE GH_GUIA_NO = ?");
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR BOK_GUIA_ADDR
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_ADDR WHERE GA_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();

			// BORRAR BOK_GUIA_SRVC
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_SRVC WHERE GS_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR BOK_GUIA_SRVC_ITEM
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_SRVC_ITEM WHERE GL_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR TABLA DE REQUERIMIENTOS   BOK_GUIA_SERV_ITEM_REQ
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_SRVC_REQM WHERE GR_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR BOK_GUIA_STUS
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_STUS WHERE GS_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR BOK_EXT_DETL
			pst = conn.prepareStatement("DELETE FROM BOK_EXT_DETL WHERE GZ_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR WEB_GUIA_REFR
			pst = conn.prepareStatement("DELETE FROM WEB_GUIA_REFR WHERE GR_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR WEB_CONTROL_EMAIL
			pst = conn.prepareStatement("DELETE FROM WEB_CTRL_EMAIL WHERE CE_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
						
			//BORRAR PPG_BOK_GUIA_ADDR
			pst = conn.prepareStatement("DELETE FROM PPG_BOK_GUIA_ADDR WHERE BGA_REF_NO = ?");
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();

			// ACTUALIZAR PPG_BOK_GUIA_DTL (FECHA DE CONVERSION NULL) GUIAS SECUNDARIAS, GUIA PRINCIPAL
			pst = conn.prepareStatement("UPDATE PPG_BOK_GUIA_DTL SET BGD_CONV_DATE = NULL, BGM_DEST_BRN_ID = NULL, BGM_DEST_SITE_ID = NULL, BGM_DEST_CLNT_ID = NULL, BGD_GH_CONTENT = NULL WHERE BGD_TRAC_NO IN (SELECT GR_GUIA_REL FROM BOK_GUIA_REL WHERE GR_GUIA_NO = ?)");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// ACTUALIZAR PPG_BOK_GUIA_DTL (FECHA DE CONVERSION NULL) GUIA PRINCIPAL
			pst = conn.prepareStatement("UPDATE PPG_BOK_GUIA_DTL SET BGD_CONV_DATE = NULL, BGM_DEST_BRN_ID = NULL, BGM_DEST_SITE_ID = NULL, BGM_DEST_CLNT_ID = NULL, BGD_GH_CONTENT = NULL WHERE BGD_TRAC_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR BOK_GUIA_HEAD_EXTRA
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_HEAD_EXTRA WHERE GE_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();

			// BORRAR CTRL_LABEL_PRN
			pst = conn.prepareStatement("DELETE FROM CTRL_LABEL_PRN WHERE CL_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			// BORRAR CTRL_LABEL_PRN
			pst = conn.prepareStatement("DELETE FROM BOK_GUIA_REL WHERE GR_GUIA_NO = ?");			
			pst.setString(1,gcnclFrm.getGuiaNumber());
			pst.executeUpdate();
			pst.close();
			
			//BORRAR REFERENCIA DE ACUSE XT
			String desactiva = "{call PACK_ACUSES_XT.PRO_BORRA_GUI_RET(?)}";
			cstmt = conn.prepareCall(desactiva);
			cstmt.setString(1, gcnclFrm.getGuiaNumber());
			cstmt.executeQuery();
			cstmt.close();
			
			update = 1;
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("cancelarGUIA_PP()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
			resources.cerrarCallableStatement(cstmt);
		}
		return update;
	}
	
	public int cancelarBOK_GUIA_SRVC(Connection conn, JavCancelGuiaMultDTO gcnclFrm) {
		PreparedStatement pst = null;
		int update = 0;
		try {
			pst = conn.prepareStatement("UPDATE BOK_GUIA_SRVC SET GS_STUS_FLAG = ? WHERE GS_GUIA_NO = ? AND GS_GUIA_TYPE = ? AND GS_DOCU_TYPE = ?");
			pst.setString(1,"I");
			pst.setString(2, gcnclFrm.getGuiaNumber());
			pst.setString(3, "H");
			pst.setString(4, "Q");
			
			update = pst.executeUpdate();			

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("cancelarBOK_GUIA_SRVC()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		return update;
	}
	
	public int cancelarBOK_GUIA_HEAD(Connection conn, JavCancelGuiaMultDTO gcnclFrm) {
		PreparedStatement pst = null;
		int update = 0;
		try {
			pst = conn.prepareStatement("UPDATE BOK_GUIA_HEAD SET GH_GUIA_STUS = ?, GH_ACTV_FLAG = ? WHERE GH_GUIA_NO = ? AND GH_GUIA_TYPE = ? AND GH_DOCU_TYPE = ?");
			pst.setString(1,"CWB");
			pst.setString(2,"I");
			pst.setString(3, gcnclFrm.getGuiaNumber());
			pst.setString(4, "H");
			pst.setString(5, "Q");
			
			update = pst.executeUpdate();
			
			if (pst!=null)
				pst.close();
			
			pst = conn.prepareStatement("DELETE FROM WEB_CTRL_EMAIL WHERE CE_GUIA_NO = ?");
			pst.setString(1,gcnclFrm.getGuiaNumber());
			
			pst.executeUpdate();
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("cancelarBOK_GUIA_HEAD()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		return update;
	}
	
	public int activateEspExtSrvc(Connection conn, JavCancelGuiaMultDTO gcnclFrm, Global global) {
		PreparedStatement pst = null;
		ResultSet rst = null;
		int update = 0;
		String cveAutserv = "";
		try {
			pst = conn.prepareStatement("SELECT GZ_REFER_AUT_T7 FROM BOK_EXT_DETL WHERE GZ_GUIA_NO = ? AND GZ_REFER_AUT_T7 IS NOT NULL");
			pst.setString(1, gcnclFrm.getGuiaNumber());
			rst = pst.executeQuery();
			
			if (rst.next()) {
				cveAutserv = rst.getString(1) == null ? "" : rst.getString(1).trim(); 
			}
			
			resources.closeResources(rst, pst);
			
			if (cveAutserv.trim().length()>0) {
				pst = conn.prepareStatement("UPDATE OL_REFER_AUT SET RA_REFER_STUS = ?, MDFD_BY = ?, MDFD_ON = SYSDATE WHERE RA_REFER_ID = ?");
				pst.setString(1, "A");
				pst.setString(2, global.getOrigenUserClave());
				pst.setString(3, cveAutserv);				
				
				update = pst.executeUpdate();	
			}			
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("activateEspExtSrvc()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rst, pst);
		}
		return update;
	}
	
	public int insertToBokGuiaStatus(Connection con, JavCancelGuiaMultDTO gcnclFrm, String webClientId) {
		String query = "";
		PreparedStatement pst = null;
		PreparedStatement pstInsert = null;
		ResultSet rs = null;
		int guisinsCount = 0;
		int serialNumber = 0;
		String strGuiaLocation = null;
		String strGuiaDest = null;
		
		try {
			query = "SELECT GH_GUIA_NO, GH_CURR_LOCA, GH_CURR_DEST FROM BOK_GUIA_HEAD WHERE GH_GUIA_NO = ? AND GH_GUIA_TYPE = ? AND GH_ACTV_FLAG = ?";
			
			pst = con.prepareStatement(query);
			pst.setString(1, gcnclFrm.getGuiaNumber());
			pst.setString(2, "H");
			pst.setString(3, "A");
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				strGuiaLocation = rs.getString("GH_CURR_LOCA") == null ? "" : rs.getString("GH_CURR_LOCA");
				strGuiaDest = rs.getString("GH_CURR_DEST") == null ? "" : rs.getString("GH_CURR_DEST");
			}
			
			rs.close();
			pst.close();

			//REALIZA LA EXTRACCION DEL NUEVO SERIAL DEL RASTREO 
			String serialNumberQuery = "";
			// get Max of Serial Number....from Bok Guia Status...
			serialNumberQuery = "SELECT NVL(MAX(GS_SERL_NO),0) + 1 FROM BOK_GUIA_STUS WHERE GS_GUIA_NO = ? ";
			
			pst = con.prepareStatement(serialNumberQuery);
			pst.setString(1, gcnclFrm.getGuiaNumber());
			rs = pst.executeQuery();

			if (rs != null && rs.next()) {
				serialNumber = rs.getInt(1);
			}
			if (rs != null) {
				rs.close();	
			}			
			pst.close();
			
			//INGRESA EL NUEVO STATUS DEL RASTREO 
			String q = "";
			q = cnct.delete(0,cnct.length())
					.append("INSERT INTO BOK_GUIA_STUS (GS_GUIA_NO, ")
					.append("GS_STUS_CODE, GS_SERL_NO, ")
					.append("CRTD_ON, CRTD_BY, ")
					.append("MDFD_ON, MDFD_BY, GS_CTT_TRNS_TYPE, ")
					.append("GS_CURR_LOCA, GS_CURR_DEST) ")
					.append("VALUES(?,?,?,SYSDATE,?,SYSDATE,?,?,?,?)").toString();
			pstInsert = con.prepareStatement(q);
			pstInsert.setString(1, gcnclFrm.getGuiaNumber());
			pstInsert.setString(2, "CWB");
			pstInsert.setInt(3, serialNumber);
			pstInsert.setString(4, webClientId);
			pstInsert.setString(5, webClientId);
			pstInsert.setString(6, "CWB");
			pstInsert.setString(7, strGuiaLocation);
			pstInsert.setString(8, strGuiaDest);

			guisinsCount = pstInsert.executeUpdate();
			
			pstInsert.close();			

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertToBokGuiaStatus()Error:").append(e).toString());
			e.printStackTrace();		
		} finally {
			resources.closeResources(rs, pst);
		}
		return guisinsCount;
	}
 
	private String validRastreoWE (Connection con, String rastreo, String idClient) {
		String result= "";
		PreparedStatement pstmt = null;	
		String tarifType = "";
		CallableStatement cst = null;
		ResultSet rs = null;
		try {
			String query = cnct.delete(0, cnct.length())
				.append("SELECT GH_ORGN_CLNT_ID ")
				.append("FROM BOK_GUIA_HEAD ")
				.append("WHERE GH_GUIA_NO = ?").toString();
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, rastreo);
			//pstmt.setString(1, idClient);
			rs = pstmt.executeQuery();
			
			if (rs.next()) {	
				result =  (rs.getString(1) == null ? "" : rs.getString(1));
			} 
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validRastreo()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validRastreo()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result; 
	}
	
	public String fechaInicDayRange (String RangeDay, HttpServletRequest request) {
		PreparedStatement pstmt = null;	
		ResultSet rs = null;
		String result= "";
		
		try {
			String query = cnct.delete(0, cnct.length()).append("SELECT TO_CHAR(SYSDATE - ?, 'DD-MM-YYYY') FROM DUAL").toString();
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, RangeDay);
			
			rs = pstmt.executeQuery();	
			
			if (rs.next()) {	
				result =  (rs.getString(1) == null ? "" : rs.getString(1));
			} 
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fechaInicDayRange()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fechaInicDayRange()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result; 
	}
	
}