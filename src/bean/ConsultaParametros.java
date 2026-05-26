/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creación: 31/05/2012
 * Compañía: PAQUETEXPRESS.
 * Descripción del programa: consulta generica para SYS_PARM_MSTR
 * FileName: Consultaparametros.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave:  
 * Autor: 
 * Fecha: 
 * Descripción:  
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción: 	
 * ------------------------------------------------------------------
 */
package bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import logger.AccessLog;

public class ConsultaParametros {
	private final String msgError  = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private StringBuffer concatena = new StringBuffer();
	/*********************************************************
	 * Metodo para obtener la configuracion de SYS_PARM_MSTR *
	 * filtrando por modulo y tipo de parametro				 *
	 *********************************************************/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList QryMdulType(Connection con, String modulo, String tipo) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList detalle = new ArrayList(6);
		ArrayList resultado = null;
		try {
			pst = con.prepareStatement("SELECT PM_PARM_CODE1, PM_PARM_CODE2, PM_VLUE1_ID, PM_VLUE2_ID, PM_VLUE1_DESC, PM_VLUE2_DESC FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = ? AND PM_PARM_TYPE = ?");
			pst.setString(1, modulo);
			pst.setString(2, tipo);
			rs = pst.executeQuery();
			resultado = new ArrayList (rs.getFetchSize());
			while( rs.next() ) {
				
				detalle.add(rs.getString(1) == null ? "" : rs.getString(1).toString());// PM_PARM_CODE1
				detalle.add(rs.getString(2) == null ? "" : rs.getString(2).toString());// PM_PARM_CODE2
				detalle.add(rs.getString(3) == null ? "" : rs.getString(3).toString());// PM_VLUE1_ID
				detalle.add(rs.getString(4) == null ? "" : rs.getString(4).toString());// PM_VLUE2_ID
				detalle.add(rs.getString(5) == null ? "" : rs.getString(5).toString());// PM_VLUE1_DESC
				detalle.add(rs.getString(6) == null ? "" : rs.getString(6).toString());// PM_VLUE2_DESC
				resultado.add(detalle.clone());
				detalle.clear();
			}
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgError).append("QryMdulType()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("QryMdulType()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return resultado;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList QryMdulTypeParm1(Connection con, String modulo, String tipo, String parm1) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList detalle = new ArrayList(6);
		ArrayList resultado = null;
		try {
			pst = con.prepareStatement("SELECT PM_PARM_CODE1, PM_PARM_CODE2, PM_VLUE1_ID, PM_VLUE2_ID, PM_VLUE1_DESC, PM_VLUE2_DESC FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = ? AND PM_PARM_TYPE = ? AND PM_PARM_CODE1 = ?");
			pst.setString(1, modulo);
			pst.setString(2, tipo);
			pst.setString(3, parm1);
			rs = pst.executeQuery();
			resultado = new ArrayList (rs.getFetchSize());
			while( rs.next() ) {
				
				detalle.add(rs.getString(1) == null ? "" : rs.getString(1).toString());// PM_PARM_CODE1
				detalle.add(rs.getString(2) == null ? "" : rs.getString(2).toString());// PM_PARM_CODE2
				detalle.add(rs.getString(3) == null ? "" : rs.getString(3).toString());// PM_VLUE1_ID
				detalle.add(rs.getString(4) == null ? "" : rs.getString(4).toString());// PM_VLUE2_ID
				detalle.add(rs.getString(5) == null ? "" : rs.getString(5).toString());// PM_VLUE1_DESC
				detalle.add(rs.getString(6) == null ? "" : rs.getString(6).toString());// PM_VLUE2_DESC
				resultado.add(detalle.clone());
				detalle.clear();
			}
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgError).append("QryMdulTypeParm1()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("QryMdulTypeParm1()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return resultado;
	}
	//@SuppressWarnings({ "rawtypes" })
	public boolean QryParmCustomerGLP(Connection con, String strClient, String parm1) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		//ArrayList detalle = new ArrayList(6);
		boolean resultado = false;
		try {
			pst = con.prepareStatement("SELECT  COUNT(*) FROM GLP_CUSTOMER_CTRL WHERE C_Clnt_Id = ? AND C_Parm_Type = ? AND C_Actv_Flag = ? AND C_Deleted_Flag = ?");
			pst.setString(1, strClient);
			pst.setString(2, parm1);
			pst.setString(3, "A");
			pst.setString(4, "N");
			rs = pst.executeQuery();
			if (rs.next()) {
				if (rs.getLong(1) > 0) {
					resultado = true;
				}
			}
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgError).append("QryParmCustomerGLP()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("QryParmCustomerGLP()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return resultado;
	}
	
	/*********************************************************
	 * Metodo para obtener la configuracion de SYS_PARM_MSTR *
	 * filtrando por modulo, tipo de parametro y PM_VLUE	 *
	 * o PM_PARMCODE			 							 *
	 *********************************************************/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList QryMdulTypeVlue(Connection con, String modulo, String tipo, String field, String toSearch) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList detalle = new ArrayList(6);
		ArrayList resultado = null;
		String filterBy = null;
		
		try {
			if (field.equalsIgnoreCase("vlue1")) {
				filterBy = "PM_VLUE1_ID";
			}else if (field.equalsIgnoreCase("vlue2")) {
				filterBy = "PM_VLUE2_ID";
			}else if (field.equalsIgnoreCase("parm1")){
				filterBy = "PM_PARM_CODE1";
			}else if (field.equalsIgnoreCase("parm2")){
				filterBy = "PM_PARM_CODE2";
			}
			
			pst = con.prepareStatement("SELECT PM_PARM_CODE1, PM_PARM_CODE2, PM_VLUE1_DESC, PM_VLUE2_DESC FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = ? AND PM_PARM_TYPE = ? AND "+filterBy+" = ?");
			pst.setString(1, modulo);
			pst.setString(2, tipo);
			pst.setString(3, toSearch);
			rs = pst.executeQuery();
			resultado = new ArrayList (rs.getFetchSize());
			while( rs.next() ) {
				
				detalle.add(rs.getString(1) == null ? "" : rs.getString(1).toString());// PM_PARM_CODE1
				detalle.add(rs.getString(2) == null ? "" : rs.getString(2).toString());// PM_PARM_CODE2
				detalle.add(rs.getString(3) == null ? "" : rs.getString(3).toString());// PM_VLUE1_DESC
				detalle.add(rs.getString(4) == null ? "" : rs.getString(4).toString());// PM_VLUE2_DESC
				resultado.add(detalle.clone());
				detalle.clear();
			}
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgError).append("QryMdulType()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("QryMdulType()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return resultado;
	}
	
	/*********************************************************
	 * Metodo para obtener la configuracion de SYS_PARM_MSTR *
	 * filtrando por modulo, tipo de parametro y CODE/VLUE	 *				 							 *
	 *********************************************************/
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList QryMdulTypeMul(Connection con, String modulo, String tipo, String field, String toSearch1, String toSearch2) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList detalle = new ArrayList(6);
		ArrayList resultado = null;
		String filterBy1 = null, filterBy2 = null;
		
		try {
			if (field.equalsIgnoreCase("vlue")) {
				filterBy1 = "PM_VLUE1_ID";
				filterBy2 = "PM_VLUE2_ID";
			}else if (field.equalsIgnoreCase("parm")){
				filterBy1 = "PM_PARM_CODE1";
				filterBy2 = "PM_PARM_CODE2";
			}
			
			pst = con.prepareStatement("SELECT PM_PARM_CODE1, PM_PARM_CODE2, PM_VLUE1_DESC, PM_VLUE2_DESC FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = ? AND PM_PARM_TYPE = ? AND "+filterBy1+" = ? AND "+filterBy2+" = ?");
			pst.setString(1, modulo);
			pst.setString(2, tipo);
			pst.setString(3, toSearch1);
			pst.setString(4, toSearch2);
			rs = pst.executeQuery();
			resultado = new ArrayList (rs.getFetchSize());
			while( rs.next() ) {
				
				detalle.add(rs.getString(1) == null ? "" : rs.getString(1).toString());// PM_PARM_CODE1
				detalle.add(rs.getString(2) == null ? "" : rs.getString(2).toString());// PM_PARM_CODE2
				detalle.add(rs.getString(3) == null ? "" : rs.getString(3).toString());// PM_VLUE1_DESC
				detalle.add(rs.getString(4) == null ? "" : rs.getString(4).toString());// PM_VLUE2_DESC
				resultado.add(detalle.clone());
				detalle.clear();
			}
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgError).append("QryMdulType()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("QryMdulType()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return resultado;
	}
}
