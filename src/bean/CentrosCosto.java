/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creaci�n: 02/08/2013
 * Compa��a: PAQUETEXPRESS.
 * Descripci�n del programa: Bean para obtencion de centros de costo
 * FileName: 
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n:  
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripci�n:  
 * ------------------------------------------------------------------
 */
package bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import logger.AccessLog;
import beanForm.JavWebBookingGeneralMainForm;

public class CentrosCosto {
	private StringBuffer concatena = new StringBuffer();
	//private final String msgAvi = new StringBuffer("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	
	public void fetchCentrosCosto(Connection con,
			JavWebBookingGeneralMainForm aform) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = null;		
		try {	
			
			query = "SELECT A.CU_CCOSTO_ID, B.CC_CCOSTO_NAME, A.CU_DFLT_FLAG FROM SYS_CLNT_CCOSTO_USER A, SYS_CLNT_CCOSTO B WHERE A.CU_CLNT_ID = ? AND A.CU_CLNT_ID = B.CC_CLNT_ID AND A.CU_CCOSTO_ID = B.CC_CCOSTO_ID AND A.CU_USER_ID = ?";			
			
			pst = con.prepareStatement(query);

			pst.setString(1, aform.getOrgienclave());
			pst.setString(2, aform.getOrigenUserClave());		

			rs = pst.executeQuery();

			aform.getCentrosCostoLabel().clear();
			aform.getCentrosCostoValue().clear();
			
			while (rs.next()) {
				aform.setCentrosCostoValue(rs.getString(1));
				aform.setCentrosCostoLabel(rs.getString(2));
				if (aform.getCentrosCosto().trim().length()>0){
					if (rs.getString(1).equals(aform.getCentrosCosto())) {
						aform.setCentrosCosto(rs.getString(1));
					}
				} else {
					if (rs.getString(3).equals("Y")) {
						aform.setCentrosCosto(rs.getString(1));
						aform.setCentrosCostoDefault(rs.getString(1));
					}
				}
			}
			//AccessLog.Log("fetchCentrosCosto() aform.getCentrosCosto() "+aform.getCentrosCosto());
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("fetchCentrosCosto()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList fetchCentrosCosto(Connection con, Global global) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String query = null;
		ArrayList result = new ArrayList(10);
		try {	
			
			query = "SELECT A.CU_CCOSTO_ID, B.CC_CCOSTO_NAME, A.CU_DFLT_FLAG FROM SYS_CLNT_CCOSTO_USER A, SYS_CLNT_CCOSTO B WHERE A.CU_CLNT_ID = ? AND A.CU_CLNT_ID = B.CC_CLNT_ID AND A.CU_CCOSTO_ID = B.CC_CCOSTO_ID AND A.CU_USER_ID = ?";			
			
			pst = con.prepareStatement(query);

			pst.setString(1, global.getClientId());
			pst.setString(2, global.getOrigenUserClave());		

			rs = pst.executeQuery();
			
			ArrayList row = new ArrayList(3);
			
			while (rs.next()) {
				row.add(rs.getString(1));
				row.add(rs.getString(2));
				row.add(rs.getString(3));
				result.add(row.clone());
				row.clear();
			}
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgErr).append("fetchCentrosCosto()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return result;
	}	
}
