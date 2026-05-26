package paquetexpress.internal.prepaid.monitor.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import bean.Global;
import bean.Resources;
import beanUtil.ConnectDB;
import logger.AccessLog;
import paquetexpress.internal.prepaid.monitor.form.ConversionMonitorForm;

public class ConversionMonitorDao {
	
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	Connection con = null;

	PreparedStatement pstmt = null;	
	String tarifType = "";
	CallableStatement cst = null;
	ResultSet rs = null;
	String fetchShip="select PM_VLUE1_DESC FROM sys_parm_mstr WHERE PM_MDUL_ID ='PPG' AND PM_PARM_TYPE ='TRANS_MODE' and PM_PARM_CODE1 in (select SD_SHIP_TYPE from PPG_SHIP_DET WHERE SD_SHIP_GUIA_REF_NO=?)";//JASA01
	String fetchShipSEGByParmCode2="select PM_VLUE1_DESC, PM_PARM_CODE1,PM_PARM_CODE2 FROM sys_parm_mstr WHERE PM_MDUL_ID ='BOK' AND PM_PARM_TYPE ='SRVC_LOG' and PM_PARM_CODE2 = ?";
	String fetchShipSEG="select PM_VLUE1_DESC, PM_PARM_CODE1,PM_PARM_CODE2 FROM sys_parm_mstr WHERE PM_MDUL_ID ='BOK' AND PM_PARM_TYPE ='SRVC_LOG' and PM_PARM_CODE2 in (select SD_SHIP_TYPE from PPG_SHIP_DET WHERE SD_SHIP_GUIA_REF_NO=?)";

	@SuppressWarnings({ "rawtypes", "unchecked"})
	public ArrayList obtieneSetRegistros(HttpSession sesion) {		
		Global global = (Global) sesion.getAttribute("sGlobal");
		ArrayList result = new ArrayList();
		//ArrayList resultAsig = new ArrayList();	
		try {		
			
			if (global.getOrigenUserLevel().equals("0")) {		
				result = registrosUserLevel_0(sesion);
				
//				if (trackingNoAsigExists(global.getClientId(), global.getOrigenUserClave())) {
//					resultAsig = registrosUserLevel_0_Asig(sesion);
//					
//					if  (!resultAsig.isEmpty()) {
//						result.addAll(resultAsig);						
//					}
//					
//					Collections.sort(result, new SetComparatorByTarifZone(true));
//				}
				Collections.sort(result, new SetComparatorByTarifZone(true));
			} else {
				result = registrosUserLevel_1(sesion);
			}	
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("obtieneSetRegistros()_Error1:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList obtieneDetalleRegistro(String idSetSeleccionado, String ClickedItems, HttpSession sesion, String esPropietario, String filtraPor) {
		Global global = (Global) sesion.getAttribute("sGlobal");
		ArrayList result = new ArrayList();
		ArrayList row =  new ArrayList(6);
		ArrayList params = null;
		String query = "";
		try {
//			String query = cnct.delete(0, cnct.length())
//					.append("SELECT BGD_TRAC_NO")
//					.append(" FROM PPG_BOK_GUIA_DTL")
//					.append(" WHERE BGD_REF_NO = ? AND BGD_CONV_DATE IS NULL AND BGD_TRAC_NO NOT IN (SELECT LG_GUIA_NO FROM PPG_LOCK_GUIA WHERE LG_REF_NO = BGD_REF_NO AND CRTD_BY <> ? ) ")
//					.append(" ORDER BY BGD_TRAC_NO").toString();
//			 
//			con = ConnectDB.getConnection();			
//			pstmt = con.prepareStatement(query);
//			pstmt.setString(1, idSetSeleccionado);
//			pstmt.setString(2, ((Global)sesion.getAttribute("sGlobal")).getOrigenUserClave());
//			
//			rs = pstmt.executeQuery();
			
			Hashtable values = null;
			if (global.getOrigenUserLevel().equals("0")) {
				if (esPropietario.equals("Y")) {
					values = detalleRegistroUserLevel_0(idSetSeleccionado, sesion, filtraPor);	
				} else {
					values = detalleRegistroUserLevel_1(idSetSeleccionado, sesion);
				}
				//getFiltrosCombo(idSetSeleccionado, sesion);
			} else {
				values = detalleRegistroUserLevel_1(idSetSeleccionado, sesion);
			}
			
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
					
					if ( ClickedItems.indexOf(rs.getString(1)) != -1 )
						row.add("checked");										//checked
					else
						row.add("");
					
					row.add(rs.getString(2) == null ? "" : rs.getString(2));	//Num. cliente propietario.
					row.add(rs.getString(3) == null ? "" : rs.getString(3));	//Nombre cliente propietario.
					row.add(rs.getString(4) == null ? "" : rs.getString(4));	//id usuario administrador.
					row.add(rs.getString(5) == null ? "" : rs.getString(5));	//Nombre usuario administrador.
					row.add(rs.getString(6) == null ? "" : rs.getString(6));	//Num cliente asignado.
					row.add(rs.getString(7) == null ? "" : rs.getString(7));	//Nombre cliente asignado.
					row.add(rs.getString(8) == null ? "" : rs.getString(8));	//id usuario asignado.
					row.add(rs.getString(9) == null ? "" : rs.getString(9));	//Nombre usuario asignado.
					
										
					if (row.get(8).toString().length() ==0) {//Valida si hay usuario asignado
						
						if (row.get(6).toString().equals(global.getClientId())) {//Valida si hay cliente asignado
							row.add("N");										//Asignado	
						} else {
							row.add("Y");										//Asignado
						}						
					} else {						
						row.add("Y");											//Asignado						
					}					
					
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String lockGuia(String idSetSeleccionado, String guiaNo, HttpSession session) {
		ArrayList result = new ArrayList();
		ArrayList row =  new ArrayList(1);
		boolean continuar = false;
		String user = "";
		String userLog = ((Global)session.getAttribute("sGlobal")).getOrigenUserClave();
		String error = "";

		try {
			//String query = "SELECT BGM_REF_NO, BGM_ZONE, NVL (BGM_TARIFA,'S'), BGM_WGHT, BGM_VLUM, BGM_NO_OF_GUIS, NVL (BGM_ACK_SERVICE,'ACK-N'), NVL (BGM_INSUR_SERVICE,'0'), NVL (BGM_EAD_SERVICE,'S'), NVL (BGM_RAD_SERVICE,'S'), NVL (BGM_EXT_SERVICE,'N') FROM PPG_BOK_GUIA_MSTR WHERE BGM_PREP_BRNC_ID = ? AND BGM_CLINT_ID = ? AND BGM_VALID_FLG = ? AND BGM_REF_NO_OLD IS NULL ORDER BY BGM_TARIFA, BGM_ZONE";
			String query = cnct.delete(0, cnct.length())
					.append("SELECT BGD_TRAC_NO")
					.append(" FROM PPG_BOK_GUIA_DTL")
					.append(" WHERE BGD_REF_NO = ? AND BGD_TRAC_NO = ? AND BGD_CONV_DATE IS NULL").toString();
			 
			con = ConnectDB.getConnection();
			con.setAutoCommit(false);
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idSetSeleccionado);
			pstmt.setString(2, guiaNo);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				row.add(rs.getString(1));
				result.add(row.clone());
				row.clear();
				continuar = true;
			} else {
				//System.out.println("La Guia ya fue utilizada");
				error = "La Guia ya fue utilizada.";
			}
			//System.out.println("continuar "+continuar);
			if (continuar) {
				rs.close();
				pstmt.close();
				
				query = cnct.delete(0, cnct.length())
						.append("SELECT CRTD_BY FROM PPG_LOCK_GUIA WHERE LG_REF_NO = ? AND LG_GUIA_NO = ?")
						.toString();
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, idSetSeleccionado);
				pstmt.setString(2, guiaNo);
				
				rs = pstmt.executeQuery();
				//System.out.println("consult� ppg_lock_guia");
				if (rs.next()){
					user = rs.getString(1) == null ? "" : rs.getString(1);
					//System.out.println("encontro registro en ppg_lock_guia");
					if (!user.equals(userLog)) {
						//System.out.println("Guia "+guiaNo+" esta siendo utilizada por usuario "+user+".");
						error = "Guia "+guiaNo+" esta siendo utilizada por usuario "+user+".";
					}
				} else {
					//System.out.println("no encontro registro en ppg_lock_guia");
					rs.close();
					pstmt.close();
					
					query =	cnct.delete(0, cnct.length())
						.append("INSERT INTO PPG_LOCK_GUIA")
						.append("(LG_REF_NO, LG_GUIA_NO, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY, LG_VERSION_NO)")
						.append("Values")
						.append("(?, ?, SYSDATE, ?, SYSDATE, ?, ?)").toString();
					
					pstmt = con.prepareStatement(query);
					
					pstmt.setString(1, idSetSeleccionado);
					pstmt.setString(2, guiaNo);
					pstmt.setString(3, userLog);
					pstmt.setString(4, userLog);
					pstmt.setString(5, "0");
					
					pstmt.executeUpdate();
					con.commit();
					//System.out.println("insert� en ppg_lock_guia");
				}
			}
		} catch (Exception e) {
			try {
				con.rollback();	
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("lockGuia()_ErrorRollBack:").append(e).toString());
				e.printStackTrace();
			}			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("lockGuia()_Error1:").append(e).toString());
			e.printStackTrace();
			error = e.getMessage()+" ===>"+e.getCause();
		} finally {
			try {				
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("lockGuia()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return error;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap lockGuias(String idSetSeleccionado, String guiasNo, HttpSession session) {
		ArrayList result = new ArrayList();
		ArrayList row =  new ArrayList(1);
		ArrayList result2 = new ArrayList();
		ArrayList row2 =  new ArrayList(1);		
		HashMap retorno =  new HashMap(2);
		
		String userLog = ((Global)session.getAttribute("sGlobal")).getOrigenUserClave();
		String error = "";

		try {
			String guiaSelect = "";
			String gnoset = "";
			StringTokenizer st = new StringTokenizer(guiasNo);
			ArrayList values = null;
			values = new ArrayList(st.countTokens());
			while (st.hasMoreElements()) {				
				guiaSelect = st.nextToken();				
				gnoset = cnct.delete(0,cnct.length()).append( gnoset ).append( "?, ").toString();
				values.add(guiaSelect);
			}			
			gnoset = gnoset.substring(0, gnoset.length()-2);
			
			//String query = "SELECT BGM_REF_NO, BGM_ZONE, NVL (BGM_TARIFA,'S'), BGM_WGHT, BGM_VLUM, BGM_NO_OF_GUIS, NVL (BGM_ACK_SERVICE,'ACK-N'), NVL (BGM_INSUR_SERVICE,'0'), NVL (BGM_EAD_SERVICE,'S'), NVL (BGM_RAD_SERVICE,'S'), NVL (BGM_EXT_SERVICE,'N') FROM PPG_BOK_GUIA_MSTR WHERE BGM_PREP_BRNC_ID = ? AND BGM_CLINT_ID = ? AND BGM_VALID_FLG = ? AND BGM_REF_NO_OLD IS NULL ORDER BY BGM_TARIFA, BGM_ZONE";
			String query = cnct.delete(0, cnct.length())
					.append("SELECT BGD_TRAC_NO")
					.append(" FROM PPG_BOK_GUIA_DTL")
					.append(" WHERE BGD_REF_NO = ? AND BGD_TRAC_NO in (").append(gnoset).append(") AND BGD_CONV_DATE IS NOT NULL").toString();
			 
			con = ConnectDB.getConnection();
			con.setAutoCommit(false);
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idSetSeleccionado);			
			
			for (int i = 0; i < values.size(); i++) {
				pstmt.setString((i+2), values.get(i).toString());	
			}	
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				row.add(rs.getString(1));
				result.add(row.clone());
				row.clear();
			}			
			
			rs.close();
			pstmt.close();
			
			/*borra guias bloqueadas por el mismo usuario ini */
			query = cnct.delete(0, cnct.length())
					.append("DELETE FROM PPG_LOCK_GUIA WHERE LG_REF_NO = ? AND LG_GUIA_NO IN (").append(gnoset).append(") AND CRTD_BY =?")
					.toString();
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idSetSeleccionado);
			
			int i = 0;
			for (i = 0; i < values.size(); i++) {
				pstmt.setString((i+2), values.get(i).toString());	
			}
			pstmt.setString(i+2, userLog);
			
			pstmt.executeUpdate();
			pstmt.close();
			
			/*borra guias bloqueadas por el mismo usuario fin */
			
			
			query = cnct.delete(0, cnct.length())
					.append("SELECT LG_GUIA_NO FROM PPG_LOCK_GUIA WHERE LG_REF_NO = ? AND LG_GUIA_NO IN (").append(gnoset).append(") AND CRTD_BY <>?")
					.toString();
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idSetSeleccionado);
			
			i = 0;
			for (i = 0; i < values.size(); i++) {
				pstmt.setString((i+2), values.get(i).toString());	
			}
			pstmt.setString(i+2, userLog);
			
			rs = pstmt.executeQuery();
						
			while (rs.next()) {
				row2.add(rs.getString(1));
				result2.add(row2.clone());
				row2.clear();				
			}
			
			String guiaResult = "";
			
			if (result.size()>0) {
				error = "EXISTEN GUIAS CONVERTIDAS EN EL SET SELECCIONADO.";
				for (int j = 0;j<values.size();j++) {
					for (int k = 0;k<result.size();k++) {
						guiaResult = ((ArrayList)result.get(k)).get(0).toString();
						if (values.get(j).toString().equals(guiaResult)) {
							values.remove(j);
							j--;
							break;
						}
					}
				}
			}
			if (result2.size()>0) {
				if (error.length()!=0) {
					error = error +" ";
				}
				error = error+"EXISTEN GUIAS QUE ESTAN SIENDO UTILIZADAS POR OTROS USUARIOS";
				for (int j = 0;j<values.size();j++) {
					for (int k = 0;k<result2.size();k++) {
						guiaResult = ((ArrayList)result2.get(k)).get(0).toString();
						if (values.get(j).toString().equals(guiaResult)) {
							values.remove(j);
							j--;							
							break;
						}
					}
				}
			}		
				
			if (!values.isEmpty()) {
				//System.out.println("no encontro registro en ppg_lock_guia");
				rs.close();
				pstmt.close();
				
				cnct.delete(0, cnct.length())
						.append("INSERT ALL ");		
				
				
				for (int lf=0;lf<values.size();lf++) {
					cnct.append("INTO PPG_LOCK_GUIA")
					.append("(LG_REF_NO, LG_GUIA_NO, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY, LG_VERSION_NO)")
					.append("Values")
					.append("(?, ?, SYSDATE, ?, SYSDATE, ?, ?) ");
				}
				query = cnct.append(" SELECT * FROM DUAL").toString();
				
				pstmt = con.prepareStatement(query);
				
				int setIndex = 1;
				for (int lf=0;lf<values.size();lf++) {
							
					pstmt.setString(setIndex, idSetSeleccionado);
					pstmt.setString(setIndex+1, values.get(lf).toString());
					pstmt.setString(setIndex+2, userLog);
					pstmt.setString(setIndex+3, userLog);
					pstmt.setString(setIndex+4, "0");
					setIndex = setIndex + 5;
				}			
				
				pstmt.executeUpdate();
				con.commit();
				//System.out.println("insert� en ppg_lock_guia");
			}	
			retorno.put("error", error);
			retorno.put("listaFinal", values);
		} catch (Exception e) {
			try {
				con.rollback();	
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("lockGuias()_ErrorRollBack:").append(e).toString());
				e.printStackTrace();
			}			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("lockGuias()_Error1:").append(e).toString());
			e.printStackTrace();
			error = e.getMessage()+" ===>"+e.getCause();
		} finally {
			try {				
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("lockGuia()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return retorno;
	}
	public String unlockGuia(String idSetSeleccionado, String guiaNo, HttpSession session) {		
		String userLog = ((Global)session.getAttribute("sGlobal")).getOrigenUserClave();
		String error = "";
		
		try {			
			String query = cnct.delete(0, cnct.length())
					.append("DELETE FROM PPG_LOCK_GUIA WHERE LG_REF_NO = ? AND LG_GUIA_NO = ? AND CRTD_BY = ?")
					.toString();
			 
			con = ConnectDB.getConnection();			
					
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idSetSeleccionado);
			pstmt.setString(2, guiaNo);
			pstmt.setString(3, userLog);	
			
			//System.out.println("idSetSeleccionado "+idSetSeleccionado);
			//System.out.println("guiaNo "+guiaNo);
			//System.out.println("userLog "+userLog);
			
			pstmt.executeUpdate();
			//int reg = pstmt.executeUpdate();					
			
			//System.out.println("registro eliminado "+reg);
			con.commit();
			
		} catch (Exception e) {
			try {
				con.rollback();	
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("unlockGuia()_ErrorRollBack:").append(e).toString());
				e.printStackTrace();
			}			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("unlockGuia()_Error1:").append(e).toString());
			e.printStackTrace();
			error = e.getMessage()+" ===>"+e.getCause();
		} finally {
			try {				
				//resources.closeResources(rs, pstmt);
				resources.cerrarPreparedStatement(pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("unlockGuia()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return error;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap unlockGuias(String idSetSeleccionado, String guiasNo, HttpSession session) {		
		String userLog = ((Global)session.getAttribute("sGlobal")).getOrigenUserClave();
		String error = "";
		HashMap retorno = new HashMap();
		try {
			String guiaSelect = "";
			String gnoset = "";
			StringTokenizer st = new StringTokenizer(guiasNo);
			ArrayList values = null;
			values = new ArrayList(st.countTokens());
			while (st.hasMoreElements()) {				
				guiaSelect = st.nextToken();				
				gnoset = cnct.delete(0,cnct.length()).append( gnoset ).append( "?, ").toString();
				values.add(guiaSelect);
			}			
			gnoset = gnoset.substring(0, gnoset.length()-2);
			
			String query = cnct.delete(0, cnct.length())
					.append("DELETE FROM PPG_LOCK_GUIA WHERE LG_REF_NO = ? AND LG_GUIA_NO IN (").append(gnoset).append(") AND CRTD_BY = ?")
					.toString();
			 
			con = ConnectDB.getConnection();		
					
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idSetSeleccionado);
			pstmt.setString(2, guiasNo);			
			
			int i = 0;
			for (i = 0; i < values.size(); i++) {
				pstmt.setString((i+2), values.get(i).toString());	
			}			
			pstmt.setString(i+2, userLog);
					
			pstmt.executeUpdate();
			//int reg = pstmt.executeUpdate();					
			
			//System.out.println("registro eliminado "+reg);
			con.commit();
			
			retorno.put("error", error);			
			retorno.put("listaFinal", new ArrayList(0));
		} catch (Exception e) {
			try {
				con.rollback();	
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("unlockGuia()_ErrorRollBack:").append(e).toString());
				e.printStackTrace();
			}			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("unlockGuia()_Error1:").append(e).toString());
			e.printStackTrace();
			error = e.getMessage()+" ===>"+e.getCause();
			retorno.put("error", error);			
			retorno.put("listaFinal", new ArrayList(0));
		} finally {
			try {				
				//resources.closeResources(rs, pstmt);
				resources.cerrarPreparedStatement(pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("unlockGuia()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return retorno;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList registrosUserLevel_0(HttpSession sesion) {
		Global global = (Global) sesion.getAttribute("sGlobal");
		ArrayList result = new ArrayList();
		ArrayList params = new ArrayList(7);
		String query = "";		
		ArrayList row =  new ArrayList(11);		
		PreparedStatement pstInvc = null;
		ResultSet rsInvc = null;
		String vigencia = "";//AAP27
		try {
			
			cnct.setLength(0);
			String qryVer = getQueryVersion(con, "PPG_MONITOR");
			query = cnct
					.append("SELECT /*"+qryVer+"*/ BGM_REF_NO, BGM_ZONE, NVL (BGM_TARIFA,'S'), BGM_WGHT, BGM_VLUM, count(BGD_REF_NO)||'/' ||BGM_NO_OF_GUIS, NVL (BGM_ACK_SERVICE,'ACK-N'), NVL (BGM_INSUR_SERVICE,'0'), NVL (BGM_EAD_SERVICE,'S'), NVL (BGM_RAD_SERVICE,'S'), NVL (BGM_EXT_SERVICE,'N'), NVL (BGM_REF_NO_OLD,' '), NVL (BGM_TARIFA, 'Z')||BGM_ZONE||BGM_REF_NO ORDERBY, ")
					.append("BGM_LOC_TYPE ")//AAP20
					.append("FROM PPG_BOK_GUIA_DTL b , PPG_BOK_GUIA_MSTR c ")
					.append("WHERE BGD_TRAC_NO IN (")
						.append("select BGAS_TRAC_NO ")
						.append("from PPG_BOK_GUIA_ASIG INTER ")
						.append("where ")
						.append("INTER.BGAS_CLNT_MSTR = ? AND (INTER.BGAS_USER_MSTR = ? OR INTER.BGAS_USER_MSTR IS NULL) ")
						.append("OR (INTER.BGAS_CLNT_ASIG = ? ")
						.append("AND (INTER.BGAS_USER_ASIG = ? OR INTER.BGAS_USER_ASIG IS NULL)) ")
					.append(") ")
					.append("AND B.BGD_TRAC_NO NOT IN (")
						.append("SELECT LG_GUIA_NO ")
						.append("FROM PPG_LOCK_GUIA ")
						.append("WHERE LG_REF_NO = B.BGD_REF_NO AND CRTD_BY <> ? ")
					.append(") ")
					.append("and BGD_CONV_DATE is null ")
					.append("and BGD_ACTV_FLAG = ? ")
					.append("and b.BGD_REF_NO = c.BGM_REF_NO ")  
					.append("and BGM_VALID_FLG = ? ")
					
					.append("group by BGM_REF_NO, BGM_ZONE, NVL (BGM_TARIFA,'S'), BGM_WGHT, BGM_VLUM,BGM_NO_OF_GUIS,  NVL (BGM_ACK_SERVICE,'ACK-N'), NVL (BGM_INSUR_SERVICE,'0'), NVL (BGM_EAD_SERVICE,'S'), NVL (BGM_RAD_SERVICE,'S'), NVL (BGM_EXT_SERVICE,'N'), NVL (BGM_REF_NO_OLD,' '), NVL (BGM_TARIFA, 'Z')||BGM_ZONE, ")
					.append("BGM_LOC_TYPE ")//AAP20
					//.append("ORDER BY NVL (BGM_TARIFA, 'Z')|| BGM_ZONE, BGM_REF_NO")
					.toString(); 
		  
			
			params.add(global.getClientId());
			params.add(global.getOrigenUserClave());
			params.add(global.getClientId());
			params.add(global.getOrigenUserClave());
			params.add(global.getOrigenUserClave());
			
			params.add("A");
			params.add("A");
						
			
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
				query = "SELECT IPH_SITE_ID||IPH_BOK_INV_REF_NO, (SELECT TO_CHAR(ADD_MONTHS(IPH_INV_DATE,PM_VLUE1_ID),'DD/MM/RRRR') caducidad FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = 'PPG' AND PM_PARM_TYPE = 'VIGENCIA_PPG') VIGENCIA"//AAP27
						+ " FROM PPG_INV_PMT_HDR WHERE IPH_INV_NO IN (SELECT IPD_INV_NO FROM PPG_INV_PMT_DTL WHERE IPD_REF_NO = ?)";
				
				pstInvc = con.prepareStatement(query);
				pstInvc.setString(1, rs.getString(1));
				
				rsInvc = pstInvc.executeQuery();
				
				vigencia = "";//AAP27
				
				if (rsInvc.next()) {
					row.add(rsInvc.getString(1));
					vigencia = rsInvc.getString(2);//AAP27
				} else {
					row.add("");
				}				
				row.add(getShippingType(rs.getString(12), rs.getString(1)));
				pstInvc.close();
				rsInvc.close();
				
				row.add(rs.getString(13));//PARA ORDENAR POR ESTE ELEMENTO DEL ARRAYLIST
				row.add(rs.getString(14));//indicador BR / BRD / IN //AAP20
				
				row.add(vigencia);//AAP27
				
				result.add(row.clone());
				row.clear();
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("registrosUserLevel_0()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				resources.closeResources(rsInvc, pstInvc);
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("registrosUserLevel_0()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public String getQueryVersion(Connection con, String type) {
		String query = "SELECT PM_VLUE1_ID FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = ? AND PM_PARM_TYPE = ? AND PM_PARM_CODE1 = ? AND PM_PARM_CODE2 = ?";
		PreparedStatement psmt = null;
		ResultSet rs= null;
		String version = "";
		
		try (Connection localCon = ConnectDB.getConnection()){
			psmt = localCon.prepareStatement(query);
			psmt.setString(1, "WEB");
			psmt.setString(2, "QUERY_VERSION");
			psmt.setString(3, "WEB_PP");
			psmt.setString(4, type);
			
			rs = psmt.executeQuery();
			
			if (rs.next()) {
				version = rs.getString(1);	
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			resources.closeResources(rs, psmt);
		}
		return version;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList registrosUserLevel_1(HttpSession sesion) {
		Global global = (Global) sesion.getAttribute("sGlobal");
		ArrayList row =  new ArrayList(11);
		ArrayList result = new ArrayList();
		ArrayList params = new ArrayList(5);
		PreparedStatement pstInvc = null;
		ResultSet rsInvc = null;
		String query = "";
		String vigencia = "";//AAP27
		try {		

			cnct.setLength(0);
			String qryVer = getQueryVersion(con, "PPG_MONITOR");
			query =
					cnct.append("SELECT /*"+qryVer+"*/ t1.BGM_REF_NO, t1.BGM_ZONE, NVL (BGM_TARIFA,'S'), t1.BGM_WGHT, t1.BGM_VLUM, COUNT (BGD_TRAC_NO)||'/' ||t1.BGM_NO_OF_GUIS, NVL (t1.BGM_ACK_SERVICE,'ACK-N'), NVL (t1.BGM_INSUR_SERVICE,'0'), NVL (t1.BGM_EAD_SERVICE,'S'), NVL (t1.BGM_RAD_SERVICE,'S'), NVL (t1.BGM_EXT_SERVICE,'N'), NVL (t1.BGM_REF_NO_OLD,' '), ")
						.append( "t1.BGM_LOC_TYPE ")//AAP20 
						.append("from (")
							.append("select BGM_REF_NO,BGD_TRAC_NO,BGM_ZONE,BGM_TARIFA,BGM_WGHT,BGM_VLUM,BGM_NO_OF_GUIS,BGM_ACK_SERVICE,BGM_INSUR_SERVICE,BGM_EAD_SERVICE,BGM_RAD_SERVICE,BGM_EXT_SERVICE,BGM_REF_NO_OLD, BGM_LOC_TYPE ") 
							.append("from PPG_BOK_GUIA_MSTR A, PPG_BOK_GUIA_DTL B ")
							.append("where A.BGM_CLINT_ID in (")
							.append("select C.BGAS_CLNT_MSTR ") 
							.append("from PPG_BOK_GUIA_ASIG C ") 
							.append("where C.BGAS_CLNT_ASIG = ? AND (C.BGAS_USER_ASIG = ? OR C.BGAS_USER_ASIG IS NULL)")
							.append(") AND A.BGM_VALID_FLG = ? and A.BGM_REF_NO = B.BGD_REF_NO ") 
							
							.append("AND B.BGD_ACTV_FLAG = ? AND B.BGD_CONV_DATE IS NULL ") 
							.append("AND B.BGD_TRAC_NO NOT IN (")
								.append("SELECT LG_GUIA_NO ") 
								.append("FROM PPG_LOCK_GUIA ") 
								.append("WHERE LG_REF_NO = B.BGD_REF_NO AND CRTD_BY <> ? ) )t1, ") 
							.append("(select BGAS_TRAC_NO ") 
							.append("from PPG_BOK_GUIA_ASIG C ") 
							.append("where C.BGAS_CLNT_ASIG = ? AND (C.BGAS_USER_ASIG = ? OR C.BGAS_USER_ASIG IS NULL) )t2 ") 
						.append("where t1.BGD_TRAC_NO = t2.BGAS_TRAC_NO ") 
						.append("GROUP BY t1.BGM_REF_NO, t1.BGM_ZONE, t1.BGM_TARIFA, t1.BGM_WGHT, t1.BGM_VLUM, t1.BGM_NO_OF_GUIS, t1.BGM_ACK_SERVICE, t1.BGM_INSUR_SERVICE, t1.BGM_EAD_SERVICE, t1.BGM_RAD_SERVICE, t1.BGM_EXT_SERVICE, t1.BGM_REF_NO_OLD, t1.BGM_LOC_TYPE ") 
						.append("ORDER BY t1.BGM_TARIFA, t1.BGM_ZONE").toString();
			
			params.add(global.getClientId());
			params.add(global.getOrigenUserClave());
			
			params.add("A");

			params.add("A");			
			params.add(global.getOrigenUserClave());
			
			params.add(global.getClientId());
			params.add(global.getOrigenUserClave());
			
					
			if (!params.isEmpty()) {
				
				con = ConnectDB.getConnection();
				pstmt = con.prepareStatement(query);
				
				for (int i=0;i<params.size();i++) {
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
					
					query = "SELECT IPH_SITE_ID||IPH_BOK_INV_REF_NO, (SELECT TO_CHAR(ADD_MONTHS(IPH_INV_DATE,PM_VLUE1_ID),'DD/MM/RRRR') caducidad FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = 'PPG' AND PM_PARM_TYPE = 'VIGENCIA_PPG') VIGENCIA " //AAP27
							+ " FROM PPG_INV_PMT_HDR WHERE IPH_INV_NO IN (SELECT IPD_INV_NO FROM PPG_INV_PMT_DTL WHERE IPD_REF_NO = ?)";
					
					pstInvc = con.prepareStatement(query);
					pstInvc.setString(1, rs.getString(1));
					
					rsInvc = pstInvc.executeQuery();
					
					vigencia = "";//AAP27
					
					if (rsInvc.next()) {
						row.add(rsInvc.getString(1));
						vigencia = rsInvc.getString(2);//AAP27
					} else {
						row.add("");
					}				
					row.add(getShippingType(rs.getString(12), rs.getString(1)));
					pstInvc.close();
					rsInvc.close();
					row.add("");//Se agrega por compatibilidad con el metodo registrosUserLevel_0 //AAP20
					row.add(rs.getString(13));//indicador set BR / BRD / IN //AAP20
					row.add(vigencia);//AAP27
					result.add(row.clone());
					row.clear();
				}	
			}		
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("registrosUserLevel_1()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				resources.closeResources(rsInvc, pstInvc);
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("registrosUserLevel_1()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Hashtable detalleRegistroUserLevel_0(String idSetSeleccionado, HttpSession sesion, String filtraPor) {
		Global global = (Global) sesion.getAttribute("sGlobal");
		Hashtable result = new Hashtable(2);
		ArrayList params = new ArrayList(7);
		String query = "";
		String filtroCliente = "";
		String filtroUsuario = "";
		try {
			filtroCliente = filtraPor.substring(0, filtraPor.indexOf('|'));
			filtroUsuario = filtraPor.substring(filtraPor.indexOf('|')+1,filtraPor.length());
			
			//VERSION CON FILTRO
			String qryVer = getQueryVersion(con, "PPG_MONITOR");
			cnct.delete(0,cnct.length())				
			.append("SELECT /*"+qryVer+"*/ B.BGAS_TRAC_NO, B.BGAS_CLNT_MSTR, pack_web.fun_ftch_clnt_name(B.BGAS_CLNT_MSTR), B.BGAS_USER_MSTR, pack_web.fun_ftch_clnt_user_name(B.BGAS_USER_MSTR), B.BGAS_CLNT_ASIG, pack_web.fun_ftch_clnt_name(B.BGAS_CLNT_ASIG), B.BGAS_USER_ASIG, pack_web.fun_ftch_clnt_user_name(B.BGAS_USER_ASIG) ")
			.append("FROM PPG_BOK_GUIA_ASIG B ")
			.append("WHERE ") 

			//.append("((B.BGAS_CLNT_MSTR = ? AND (B.BGAS_USER_MSTR = ? OR B.BGAS_USER_MSTR IS NULL)) ")
			//.append("OR ") 
			//.append("(B.BGAS_CLNT_ASIG = ? AND (B.BGAS_USER_ASIG = ? OR B.BGAS_USER_ASIG IS NULL))) ")
			
			.append("B.BGAS_CLNT_ASIG = ? ");
			
			params.add(filtroCliente);
			
			if (filtroUsuario.trim().length()==0) {
				cnct.append("AND B.BGAS_USER_ASIG IS NULL ");
			} else {
				cnct.append("AND B.BGAS_USER_ASIG = ? ");
				params.add(filtroUsuario);
			}

			cnct.append("AND B.BGAS_TRAC_NO IN (")
			.append("SELECT A.BGD_TRAC_NO FROM PPG_BOK_GUIA_DTL A WHERE A.BGD_REF_NO = ? ")
				.append("AND A.BGD_TRAC_NO NOT IN (")
					.append("SELECT LG_GUIA_NO FROM PPG_LOCK_GUIA WHERE LG_REF_NO = A.BGD_REF_NO AND CRTD_BY <> ?")
					.append(") ")  
				.append("AND A.BGD_CONV_DATE IS NULL ")
				.append("AND A.BGD_ACTV_FLAG = ?")
	        .append(") AND ROWNUM <= 100 ")
	        .append("ORDER BY B.BGAS_TRAC_NO");
			
			params.add(idSetSeleccionado);
			params.add(global.getOrigenUserClave());
			params.add("A");			
			
			query = cnct.toString();
	        
			result.put("query", query);
			result.put("params", params);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("detalleRegistroUserLevel_0()_Error1:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Hashtable detalleRegistroUserLevel_1(String idSetSeleccionado, HttpSession sesion) {
		Global global = (Global) sesion.getAttribute("sGlobal");
		Hashtable result = new Hashtable(2);
		ArrayList params = new ArrayList(5);
		String query = "";
		try {
			
			String qryVer = getQueryVersion(con, "PPG_MONITOR");
				query =
						cnct.delete(0,cnct.length())				
						.append("SELECT /*"+qryVer+"*/ B.BGAS_TRAC_NO, B.BGAS_CLNT_MSTR, pack_web.fun_ftch_clnt_name(B.BGAS_CLNT_MSTR), B.BGAS_USER_MSTR, pack_web.fun_ftch_clnt_user_name(B.BGAS_USER_MSTR), B.BGAS_CLNT_ASIG, pack_web.fun_ftch_clnt_name(B.BGAS_CLNT_ASIG), B.BGAS_USER_ASIG, pack_web.fun_ftch_clnt_user_name(B.BGAS_USER_ASIG) ")
						.append("FROM PPG_BOK_GUIA_ASIG B ")
						.append("WHERE ") 
						.append("(B.BGAS_CLNT_ASIG = ? AND (B.BGAS_USER_ASIG = ? OR B.BGAS_USER_ASIG IS NULL)) ")
						.append("AND B.BGAS_TRAC_NO IN (")
							.append("SELECT A.BGD_TRAC_NO FROM PPG_BOK_GUIA_DTL A WHERE A.BGD_REF_NO = ? ")
								.append("AND A.BGD_TRAC_NO NOT IN (")
									.append("SELECT LG_GUIA_NO FROM PPG_LOCK_GUIA WHERE LG_REF_NO = A.BGD_REF_NO AND CRTD_BY <> ?")
									.append(") ")  
								.append("AND A.BGD_CONV_DATE IS NULL ")
								.append("AND A.BGD_ACTV_FLAG = ?")
				        .append(") AND ROWNUM <= 100 ")
				        .append("ORDER BY B.BGAS_TRAC_NO")
				        .toString();
				
				params.add(global.getClientId());
				params.add(global.getOrigenUserClave());
				params.add(idSetSeleccionado);
				params.add(global.getOrigenUserClave());
				params.add("A");
				
				result.put("query", query);
				result.put("params", params);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("detalleRegistroUserLevel_1()_Error1:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String asignaGuia(ConversionMonitorForm monitorForm, HttpSession session) {		
		Global global = (Global)session.getAttribute("sGlobal");
		String userLog = global.getOrigenUserClave();
		String error = "";
		//System.out.println("entro a metodo asignaGuia()");
		try {
			StringTokenizer st = null;
			String gnoset = "";
			String guiaSelect = "";
			ArrayList values = null;
			st = new StringTokenizer(monitorForm.getClickedItems());
			
			values = new ArrayList(st.countTokens());
			while (st.hasMoreElements()) {
				guiaSelect = st.nextToken();
				
				gnoset = cnct.delete(0,cnct.length()).append( gnoset ).append( "?, ").toString();
				values.add(guiaSelect);
			}
			
			gnoset = gnoset.substring(0, gnoset.length()-2);
			
			String query = cnct.delete(0, cnct.length())
					.append("UPDATE PPG_BOK_GUIA_ASIG SET BGAS_USER_MSTR = ?, BGAS_CLNT_ASIG = ?, BGAS_USER_ASIG = ?, MDFD_BY = ?, MDFD_ON = SYSDATE WHERE BGAS_TRAC_NO IN (").append(gnoset).append(")")
					.toString();
			 
			con = ConnectDB.getConnection();
			
			pstmt = con.prepareStatement(query);
			
			/*opcion para generacion de PDF*/
			//System.out.println("getGenPDF() "+monitorForm.getGenPDF());
			//System.out.println("getCveClienteAsig() "+monitorForm.getCveClienteAsig());
			//System.out.println("getCveUserAsig() "+monitorForm.getCveUserAsig());
			if (monitorForm.getGenPDF().equals("Y")) {
				pstmt.setString(1, userLog);
				pstmt.setString(2, monitorForm.getCveClienteAsig());
				pstmt.setString(3, monitorForm.getCveUserAsig());
			
				/*opciones para ASIGNACION DE GUIAS*/
			} else if (monitorForm.getTipoAsignacion().equals("sin")) {
				pstmt.setNull(1, Types.VARCHAR);
				pstmt.setString(2, global.getClientId());
				pstmt.setNull(3, Types.VARCHAR);
				
			} else if (monitorForm.getTipoAsignacion().equals("cte")) {
				pstmt.setString(1, userLog);
				pstmt.setString(2, monitorForm.getCveClienteAsig());
				if (monitorForm.getCveUserAsig().length() == 0){
					pstmt.setNull(3, Types.VARCHAR);	
				} else {
					pstmt.setString(3, monitorForm.getCveUserAsig());
				}
			} else if (monitorForm.getTipoAsignacion().equals("gpo")) {
				pstmt.setString(1, userLog);
				pstmt.setString(2, monitorForm.getCveClienteAsig());
				if (monitorForm.getCveUserAsig().length() == 0){
					pstmt.setNull(3, Types.VARCHAR);	
				} else {
					pstmt.setString(3, monitorForm.getCveUserAsig());
				}				
			}
			
			pstmt.setString(4, userLog);
			
			for (int i = 0; i < values.size(); i++) {
				pstmt.setString((i+5), values.get(i).toString());	
			}	
			
			//System.out.println("idSetSeleccionado "+idSetSeleccionado);
			//System.out.println("guiaNo "+guiaNo);
			//System.out.println("userLog "+userLog);
			
			pstmt.executeUpdate();
			//int reg = pstmt.executeUpdate();					
			
			//System.out.println("registro eliminado "+reg);
			con.commit();
			
		} catch (Exception e) {
			try {
				con.rollback();	
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("asignaGuia()_ErrorRollBack:").append(e).toString());
				e.printStackTrace();
			}			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("asignaGuia()_Error1:").append(e).toString());
			e.printStackTrace();
			error = e.getMessage()+" ===>"+e.getCause();
		} finally {
			try {				
				//resources.closeResources(rs, pstmt);
				resources.cerrarPreparedStatement(pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("asignaGuia()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return error;
	}
	
	public String asignaGuiaSetPDF(ConversionMonitorForm monitorForm, HttpSession session) {		
		Global global = (Global)session.getAttribute("sGlobal");
		String userLog = global.getOrigenUserClave();
		String error = "";
		//System.out.println("entro a metodo asignaGuia()");
		try {			
			
			String query = cnct.delete(0, cnct.length())
					.append("UPDATE PPG_BOK_GUIA_ASIG SET BGAS_USER_MSTR = ?, BGAS_CLNT_ASIG = ?, BGAS_USER_ASIG = ?, MDFD_BY = ?, MDFD_ON = SYSDATE WHERE BGAS_TRAC_NO IN (SELECT BGD_TRAC_NO FROM PPG_BOK_GUIA_DTL WHERE BGD_REF_NO = ? AND BGD_CONV_DATE IS NULL) AND BGAS_CLNT_ASIG = ? AND BGAS_USER_ASIG IS NULL")
					.toString();
			 
			con = ConnectDB.getConnection();
			
			pstmt = con.prepareStatement(query);
			
			/*opcion para generacion de PDF*/
			//System.out.println("getGenPDF() "+monitorForm.getGenPDF());
			//System.out.println("getCveClienteAsig() "+monitorForm.getCveClienteAsig());
			//System.out.println("getCveUserAsig() "+monitorForm.getCveUserAsig());
			
			pstmt.setString(1, userLog);
			pstmt.setString(2, monitorForm.getCveClienteAsig());
			pstmt.setString(3, monitorForm.getCveUserAsig());			
			
			pstmt.setString(4, userLog);
			
			
			pstmt.setString(5, monitorForm.getIdSetSelPDF());
			pstmt.setString(6, monitorForm.getClientePropietario());
			
			pstmt.executeUpdate();
			//int reg = pstmt.executeUpdate();					
			
			//System.out.println("registro eliminado "+reg);
			con.commit();
			
		} catch (Exception e) {
			try {
				con.rollback();	
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("asignaGuia()_ErrorRollBack:").append(e).toString());
				e.printStackTrace();
			}			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("asignaGuia()_Error1:").append(e).toString());
			e.printStackTrace();
			error = e.getMessage()+" ===>"+e.getCause();
		} finally {
			try {				
				//resources.closeResources(rs, pstmt);
				resources.cerrarPreparedStatement(pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("asignaGuia()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return error;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap getFiltrosCombo(String idSetSeleccionado, HttpSession sesion) {
		Global global = (Global) sesion.getAttribute("sGlobal");
		HashMap result = new HashMap(2);
		ArrayList valuesArr = new ArrayList();
		ArrayList labelArr = new ArrayList();
		String query = "";
		
		try {
			con = ConnectDB.getConnection();
			
			query =
					cnct.delete(0,cnct.length())
					.append("SELECT B.BGAS_CLNT_ASIG, pack_web.fun_ftch_clnt_name(B.BGAS_CLNT_ASIG) nomCte, B.BGAS_USER_ASIG, pack_web.fun_ftch_clnt_user_name(B.BGAS_USER_ASIG) nomUser ")
					.append("FROM PPG_BOK_GUIA_ASIG B ")
					.append("WHERE ") 
					.append("((B.BGAS_CLNT_MSTR = ? AND (B.BGAS_USER_MSTR = ? OR B.BGAS_USER_MSTR IS NULL)) ")
					.append("OR ") 
					.append("(B.BGAS_CLNT_ASIG = ? AND (B.BGAS_USER_ASIG = ? OR B.BGAS_USER_ASIG IS NULL))) ")
					.append("AND B.BGAS_TRAC_NO IN (")
						.append("SELECT A.BGD_TRAC_NO FROM PPG_BOK_GUIA_DTL A WHERE A.BGD_REF_NO = ? ")
							.append("AND A.BGD_TRAC_NO NOT IN (")
								.append("SELECT LG_GUIA_NO FROM PPG_LOCK_GUIA WHERE LG_REF_NO = A.BGD_REF_NO AND CRTD_BY <> ?")
								.append(") ")  
							.append("AND A.BGD_CONV_DATE IS NULL ")
							.append("AND A.BGD_ACTV_FLAG = ?")
			        .append(") ")
			        .append("GROUP BY BGAS_CLNT_MSTR, BGAS_USER_MSTR, BGAS_CLNT_ASIG, BGAS_USER_ASIG ")
			        //.append("ORDER BY BGAS_CLNT_MSTR, BGAS_USER_MSTR, BGAS_CLNT_ASIG, BGAS_USER_ASIG")
			        .append("ORDER BY nomCte, nomUser NULLS FIRST")
			        .toString();
			
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, global.getClientId());
			pstmt.setString(2, global.getOrigenUserClave());
			pstmt.setString(3, global.getClientId());
			pstmt.setString(4, global.getOrigenUserClave());
			pstmt.setString(5, idSetSeleccionado);
			pstmt.setString(6, global.getOrigenUserClave());
			pstmt.setString(7, "A");
		
			rs = pstmt.executeQuery();
			String value = "";
			String option = "";
			
			while (rs.next()) {
				option = 
						cnct.delete(0,cnct.length())
						.append(rs.getString(2)==null?"":rs.getString(2))
						.append(" ")
						.append(rs.getString(1)==null?"":rs.getString(1))
						.toString();
				if (rs.getString(3) != null) {
					option = 
							cnct.delete(0,cnct.length())
							.append(option)
							.append(" - ")
							.append(rs.getString(4)==null?"":rs.getString(4))
							.append(" ")
							.append(rs.getString(3)==null?"":rs.getString(3))
							.toString();
				}
				value = 
						cnct.delete(0,cnct.length())
						.append(rs.getString(1)==null?"":rs.getString(1))
						.append("|")
						.append(rs.getString(3)==null?" ":rs.getString(3))
						//.append("|")
						.toString();
				
				labelArr.add(option);
				valuesArr.add(value);			
			}
			result.put("label", labelArr);
			result.put("value", valuesArr);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getFiltrosCombo()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				//resources.closeResources(rs, pstmt);
				resources.cerrarPreparedStatement(pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getFiltrosCombo()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	public String esClientePropietario(String idSetSeleccionado, HttpSession session) {		
		String clientId = ((Global)session.getAttribute("sGlobal")).getClientId();
		String propietario = "";
		
		try {			
			String query = cnct.delete(0, cnct.length())
					.append("SELECT BGM_CLINT_ID FROM PPG_BOK_GUIA_MSTR WHERE BGM_REF_NO = ? AND BGM_CLINT_ID = ?")
					.toString();
			 
			con = ConnectDB.getConnection();
					
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, idSetSeleccionado);
			pstmt.setString(2, clientId);
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				propietario = "Y";
			} else {
				propietario = "N";
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("unlockGuia()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				//resources.closeResources(rs, pstmt);
				resources.cerrarPreparedStatement(pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("unlockGuia()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return propietario;
	}

	private String getShippingType(String shipType, String refNo){
		List<String> list=new ArrayList<String>();
		PreparedStatement pstShipping = null;
		ResultSet rsShipping = null;
		try {
			boolean validateExclusive1D=false;
			if(!shipType.equalsIgnoreCase("1D")) {
				pstShipping=con.prepareStatement(fetchShip);
				pstShipping.setString(1, refNo);
				rsShipping=pstShipping.executeQuery();
			     
			    while(rsShipping.next()){		
			    	list.add(rsShipping.getString(1));						
			    }
	
				if (rsShipping != null){
					rsShipping.close();
					rsShipping.close();
				}
				if (pstShipping != null){
					pstShipping.close();
					pstShipping =null;
				}
			} else {
			   	validateExclusive1D = true;
			}
			//Cuando no encontro nada de los tipos de envio (antes de los cambios del SEG) se procede a buscar en los nuevos tipos de envios
			if (list.isEmpty()) {
			    if(validateExclusive1D){
			    	pstShipping = con.prepareStatement(fetchShipSEGByParmCode2);
			    	pstShipping.setString(1, "DS");
			    } else {
			    	pstShipping = con.prepareStatement(fetchShipSEG);
			    	pstShipping.setString(1, refNo);
			    }
			    rsShipping = pstShipping.executeQuery();
				while (rsShipping.next()) {
					list.add(rsShipping.getString(1));
				}
	
				if (rsShipping != null){
					rsShipping.close();
					rsShipping.close();
				}
				if (pstShipping != null){
					pstShipping.close();
					pstShipping =null;
				}
				
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getShippingType()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				resources.closeResources(rsShipping, pstShipping);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getShippingType()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return list.isEmpty()?"":list.get(0);
	}
}