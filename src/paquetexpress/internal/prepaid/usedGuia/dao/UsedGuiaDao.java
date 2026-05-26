package paquetexpress.internal.prepaid.usedGuia.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpSession;

import logger.AccessLog;
import bean.Global;
import bean.Resources;
import beanUtil.ConnectDB;

public class UsedGuiaDao {
	
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	Connection con = null;

	PreparedStatement pstmt = null;	
	String tarifType = "";
	CallableStatement cst = null;
	ResultSet rs = null;
	
	public ArrayList obtieneSetRegistros(HttpSession sesion) {
		ArrayList result = new ArrayList();
		ArrayList row =  new ArrayList(11);
		ArrayList params = null;
		PreparedStatement pstInvc = null;
		ResultSet rsInvc = null;
		String query = "";
		
		try {
			
			Hashtable values = registrosUser(sesion);
			
			if (values != null) {
				query = values.get("query").toString();
				params = (ArrayList) values.get("params");
				
				con = ConnectDB.getConnection();
				pstmt = con.prepareStatement(query);
				
				for (int i=0;i<params.size();i++) {
					//System.out.println("params ["+(i+1)+"] "+params.get(i).toString());
					pstmt.setString((i+1), params.get(i).toString());
				}
				
				rs = pstmt.executeQuery();
				
				while (rs.next()) {
					row.add(rs.getString(1));
					row.add(rs.getString(2));
					row.add(rs.getString(3));
					row.add(rs.getString(4));
					row.add(rs.getString(5));
					row.add(rs.getString(6));
					row.add(rs.getString(7));
					row.add(rs.getString(8));
					row.add(rs.getString(9));
					row.add(rs.getString(10));
					row.add(rs.getString(11));
					row.add(rs.getString(12));
					
					//SELECT IPH_SITE_ID||IPH_BOK_INV_REF_NO 
					//FROM PPG_INV_PMT_HDR WHERE IPH_INV_NO IN (
					//SELECT IPD_INV_NO FROM PPG_INV_PMT_DTL WHERE IPD_REF_NO = '6094')
					query = "SELECT IPH_SITE_ID||IPH_BOK_INV_REF_NO FROM PPG_INV_PMT_HDR WHERE IPH_INV_NO IN (SELECT IPD_INV_NO FROM PPG_INV_PMT_DTL WHERE IPD_REF_NO = ?)";
					
					pstInvc = con.prepareStatement(query);
					pstInvc.setString(1, rs.getString(1));
					
					rsInvc = pstInvc.executeQuery();
					
					if (rsInvc.next()) {
						row.add(rsInvc.getString(1));
					} else {
						row.add("");
					}				
					
					pstInvc.close();
					rsInvc.close();
					
					result.add(row.clone());
					row.clear();
				}	
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("obtieneSetRegistros()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				resources.closeResources(rsInvc, pstInvc);
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("obtieneSetRegistros()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}

	public ArrayList obtieneDetalleRegistro(String idSetSeleccionado) {		
		ArrayList result = new ArrayList();
		ArrayList row =  new ArrayList(6);
		ArrayList params = null;
		String query = "";
		try {
			
			Hashtable values = detalleRegistroUser(idSetSeleccionado);			
			
			if (values != null) {				
				query = values.get("query").toString();
				params = (ArrayList) values.get("params");
				
				con = ConnectDB.getConnection();
				pstmt = con.prepareStatement(query);
				
				for (int i=0;i<params.size();i++) {
					//System.out.println("params ["+(i+1)+"] "+params.get(i).toString());
					pstmt.setString((i+1), params.get(i).toString());
				}
				
				rs = pstmt.executeQuery();
				while (rs.next()) {					
					row.add(rs.getString(1));									//rastreo
					row.add(rs.getString(10) == null ? "" : rs.getString(10));  //rastreos Relacionados.
					row.add(rs.getString(2) == null ? "" : rs.getString(2));	//Num. cliente propietario.
					row.add(rs.getString(3) == null ? "" : rs.getString(3));	//Nombre cliente propietario.
					row.add(rs.getString(4) == null ? "" : rs.getString(4));	//id usuario administrador.
					row.add(rs.getString(5) == null ? "" : rs.getString(5));	//Nombre usuario administrador.
					row.add(rs.getString(6) == null ? "" : rs.getString(6));	//Num cliente asignado.
					row.add(rs.getString(7) == null ? "" : rs.getString(7));	//Nombre cliente asignado.
					row.add(rs.getString(8) == null ? "" : rs.getString(8));	//id usuario asignado.
					row.add(rs.getString(9) == null ? "" : rs.getString(9));	//Nombre usuario asignado.
					
					row.add(rs.getString(11) == null ? "" : rs.getString(11));	//ID sucursal destino.
					row.add(rs.getString(12) == null ? "" : rs.getString(12));	//Nombre sucursal destino.
					row.add(rs.getString(13) == null ? "" : rs.getString(13));	//ID site destino.
					row.add(rs.getString(14) == null ? "" : rs.getString(14));	//Nombre site destino.
					row.add(rs.getString(15) == null ? "" : rs.getString(15));	//ID cliente destino.
					row.add(rs.getString(16) == null ? "" : rs.getString(16));	//Nombre cliente destino.
					row.add(rs.getString(17) == null ? "" : rs.getString(17));	//Contenido del envio.
					
					result.add(row.clone());
					row.clear();
				}
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("obtieneDetalleRegistro()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("obtieneDetalleRegistro()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}	
	
	private Hashtable registrosUser(HttpSession sesion) {
		Global global = (Global) sesion.getAttribute("sGlobal");
		Hashtable result = new Hashtable(2);
		ArrayList params = new ArrayList(7);
		String query = "";
		try {
			query =
				cnct.delete(0,cnct.length())
				.append("SELECT A.BGM_REF_NO, A.BGM_ZONE, NVL (BGM_TARIFA,'S'), A.BGM_WGHT, A.BGM_VLUM, COUNT (BGD_TRAC_NO)||'/'||A.BGM_NO_OF_GUIS, NVL (A.BGM_ACK_SERVICE,'ACK-N'), NVL (A.BGM_INSUR_SERVICE,'0'), NVL (A.BGM_EAD_SERVICE,'S'), NVL (A.BGM_RAD_SERVICE,'S'), NVL (A.BGM_EXT_SERVICE,'N'), NVL (A.BGM_REF_NO_OLD,' ') ")
				.append("FROM PPG_BOK_GUIA_ASIG C, PPG_BOK_GUIA_MSTR A, PPG_BOK_GUIA_DTL B ")
				.append("WHERE ")
				.append("A.BGM_CLINT_ID = ? ")
				.append("AND C.BGAS_TRAC_NO = B.BGD_TRAC_NO ")
				.append("AND A.BGM_REF_NO = B.BGD_REF_NO ")
				.append("AND A.BGM_VALID_FLG = ? ")
				.append("AND B.BGD_ACTV_FLAG = ? ")
				.append("AND B.BGD_CONV_DATE IS NOT NULL ") 
				.append("GROUP BY A.BGM_REF_NO, A.BGM_ZONE, A.BGM_TARIFA, A.BGM_WGHT, A.BGM_VLUM, A.BGM_NO_OF_GUIS, A.BGM_ACK_SERVICE, A.BGM_INSUR_SERVICE, A.BGM_EAD_SERVICE, A.BGM_RAD_SERVICE, A.BGM_EXT_SERVICE, A.BGM_REF_NO_OLD ")
				.append("ORDER BY A.BGM_TARIFA, A.BGM_ZONE").toString();
			
			params.add(global.getClientId());
			params.add("A");
			params.add("A");						
			
			result.put("query", query);
			result.put("params", params);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("registrosUser()_Error1:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	
	private Hashtable detalleRegistroUser(String idSetSeleccionado) {
		Hashtable result = new Hashtable(2);
		ArrayList params = new ArrayList(7);
		String query = "";
		try {			
			cnct.delete(0,cnct.length())				
			.append("SELECT B.BGAS_TRAC_NO, ")
			.append("B.BGAS_CLNT_MSTR, pack_web.fun_ftch_clnt_name(B.BGAS_CLNT_MSTR), ")
			.append("B.BGAS_USER_MSTR, pack_web.fun_ftch_clnt_user_name(B.BGAS_USER_MSTR), ")
			.append("B.BGAS_CLNT_ASIG, pack_web.fun_ftch_clnt_name(B.BGAS_CLNT_ASIG), ")
			.append("B.BGAS_USER_ASIG, pack_web.fun_ftch_clnt_user_name(B.BGAS_USER_ASIG), ")
			.append("C.GR_GUIA_REL, ")
			.append("A.BGM_DEST_BRN_ID, pack_web.Fun_ftch_brnc_name(A.BGM_DEST_BRN_ID), ")
			.append("A.BGM_DEST_SITE_ID, pack_web.Fun_ftch_site_name(A.BGM_DEST_SITE_ID), ")			
			.append("A.BGM_DEST_CLNT_ID, pack_web.fun_ftch_clnt_name(A.BGM_DEST_CLNT_ID), ")
			.append("A.BGD_GH_CONTENT ")
			.append("FROM PPG_BOK_GUIA_DTL A, PPG_BOK_GUIA_ASIG B, BOK_GUIA_REL C ")
			.append("WHERE ")
			.append("A.BGD_REF_NO = ? ")	
			.append("AND A.BGD_TRAC_NO = B.BGAS_TRAC_NO ")						 
			.append("AND A.BGD_CONV_DATE IS NOT NULL ")
			.append("AND A.BGD_ACTV_FLAG = ? ")
	        .append("AND B.BGAS_TRAC_NO = C.GR_GUIA_NO ")
	        .append("ORDER BY B.BGAS_TRAC_NO");
			
			params.add(idSetSeleccionado);
			params.add("A");			
			
			query = cnct.toString();
			
			result.put("query", query);
			result.put("params", params);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("detalleRegistroUser()_Error1:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}	
}