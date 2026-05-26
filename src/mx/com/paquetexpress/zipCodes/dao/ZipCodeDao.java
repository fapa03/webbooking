package mx.com.paquetexpress.zipCodes.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;
import bean.Resources;
import beanUtil.ConnectDB;

public class ZipCodeDao {
	
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	Connection con = null;

	PreparedStatement pstmt = null;	
	String tarifType = "";
	CallableStatement cst = null;
	ResultSet rs = null;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getRowsByZipCode(String find, String type) {
		ArrayList result = new ArrayList();
		ArrayList row =  new ArrayList(20);
		String query = "";
		
		try {
			
			query = cnct.delete(0, cnct.length())
					.append("SELECT COLO_ZIPCODE, COL_DES, COD_COLO, COL_LEVL, COL_TYPE, SUCURSAL, CIUDAD, COD_CIUD, CIU_LEVEL, CIU_TYPE, DELMUN, COD_DELE, DEL_LEVEL, DELETYPE, ESTADO, COD_ESTA, EST_LEVEL, EST_TYPE, PAIS, COD_PAIS, PAIS_LEVEL, PAIS_TYPE, ZONA, COD_ZONA, ZONA_LEVL, ZONA_TYPE ") 
					.append("FROM PCOBERTURA_VIEW ")
					.append("WHERE COLO_ZIPCODE = ? AND SUCURSAL IS NOT NULL AND GE_SEPOMEX IS NOT NULL  ")
					.append("ORDER BY COL_DES").toString();

			con = ConnectDB.getConnection();			
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, find);
			
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
				row.add(rs.getString(13));
				row.add(rs.getString(14));
				row.add(rs.getString(15));
				row.add(rs.getString(16));
				row.add(rs.getString(17));
				row.add(rs.getString(18));
				row.add(rs.getString(19));
				row.add(rs.getString(20));
				row.add(rs.getString(21));
				row.add(rs.getString(22));
				row.add(rs.getString(23));
				row.add(rs.getString(24));
				row.add(rs.getString(25));
				row.add(rs.getString(26));
				
				result.add(row.clone());
				row.clear();
			}			
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRowsByZipCode()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRowsByZipCode()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getRowsByLocalidad(String find) {
		ArrayList result = new ArrayList();
		ArrayList row =  new ArrayList(20);
		String query = "";
		
		try {
			
			query = cnct.delete(0, cnct.length())
					.append("SELECT COLO_ZIPCODE, COL_DES, COD_COLO, COL_LEVL, COL_TYPE, SUCURSAL, CIUDAD, COD_CIUD, CIU_LEVEL, CIU_TYPE, DELMUN, COD_DELE, DEL_LEVEL, DELETYPE, ESTADO, COD_ESTA, EST_LEVEL, EST_TYPE, PAIS, COD_PAIS, PAIS_LEVEL, PAIS_TYPE, ZONA, COD_ZONA, ZONA_LEVL, ZONA_TYPE ") 
					.append("FROM PCOBERTURA_VIEW ")
					//.append("WHERE COLO_ZIPCODE LIKE ? OR COL_DES LIKE ? OR CIUDAD LIKE ? AND SUCURSAL IS NOT NULL ")
					.append("WHERE (DELMUN LIKE ? OR CIUDAD LIKE ?) AND SUCURSAL IS NOT NULL AND GE_SEPOMEX IS NOT NULL  ")
					.append("ORDER BY COL_DES").toString();

			con = ConnectDB.getConnection();			
			pstmt = con.prepareStatement(query);

			pstmt.setString(1, "%"+find+"%");
			pstmt.setString(2, "%"+find+"%");
			
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
				row.add(rs.getString(13));
				row.add(rs.getString(14));
				row.add(rs.getString(15));
				row.add(rs.getString(16));
				row.add(rs.getString(17));
				row.add(rs.getString(18));
				row.add(rs.getString(19));
				row.add(rs.getString(20));
				row.add(rs.getString(21));
				row.add(rs.getString(22));
				row.add(rs.getString(23));
				row.add(rs.getString(24));
				row.add(rs.getString(25));
				row.add(rs.getString(26));
				
				result.add(row.clone());
				row.clear();
			}			
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRowsByLocalidad()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRowsByLocalidad()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getRowsByColLocEst(String col, String loc, String edo) {
		ArrayList result = new ArrayList();
		ArrayList row =  new ArrayList(20);
		String query = "";
		
		try {
			
			query = cnct.delete(0, cnct.length())
					.append("SELECT COLO_ZIPCODE, COL_DES, COD_COLO, COL_LEVL, COL_TYPE, SUCURSAL, CIUDAD, COD_CIUD, CIU_LEVEL, CIU_TYPE, DELMUN, COD_DELE, DEL_LEVEL, DELETYPE, ESTADO, COD_ESTA, EST_LEVEL, EST_TYPE, PAIS, COD_PAIS, PAIS_LEVEL, PAIS_TYPE, ZONA, COD_ZONA, ZONA_LEVL, ZONA_TYPE ") 
					.append("FROM PCOBERTURA_VIEW ")
					//.append("WHERE COLO_ZIPCODE LIKE ? OR COL_DES LIKE ? OR CIUDAD LIKE ? AND SUCURSAL IS NOT NULL ")
					.append("WHERE (DELMUN LIKE NVL(?, DELMUN) OR CIUDAD LIKE NVL(?, CIUDAD)) AND ESTADO LIKE NVL(?, ESTADO) AND COL_DES LIKE NVL(?, COL_DES) AND SUCURSAL IS NOT NULL  AND GE_SEPOMEX IS NOT NULL ")
					.append("ORDER BY COL_DES").toString();

			con = ConnectDB.getConnection();			
			pstmt = con.prepareStatement(query);

			pstmt.setString(1, "%"+loc+"%");
			pstmt.setString(2, "%"+loc+"%");
			pstmt.setString(3, "%"+edo+"%");
			pstmt.setString(4, "%"+col+"%");		
			
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
				row.add(rs.getString(13));
				row.add(rs.getString(14));
				row.add(rs.getString(15));
				row.add(rs.getString(16));
				row.add(rs.getString(17));
				row.add(rs.getString(18));
				row.add(rs.getString(19));
				row.add(rs.getString(20));
				row.add(rs.getString(21));
				row.add(rs.getString(22));
				row.add(rs.getString(23));
				row.add(rs.getString(24));
				row.add(rs.getString(25));
				row.add(rs.getString(26));
				
				result.add(row.clone());
				row.clear();
			}			
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRowsByColLocEst()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRowsByColLocEst()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap fillComboPaises() {		
		HashMap result = new HashMap(2);
		ArrayList valuesArr = new ArrayList();
		ArrayList labelArr = new ArrayList();
		//String query = "";
		
		try {
			
//			query = "SELECT PAIS, COD_PAIS, PAIS_LEVEL, PAIS_TYPE FROM PCOBERTURA_VIEW "
//					+ "GROUP BY PAIS, COD_PAIS, PAIS_LEVEL, PAIS_TYPE";
//
//			con = ConnectDB.getConnection();			
//			pstmt = con.prepareStatement(query);
//
//			rs = pstmt.executeQuery();
//							
//			String value = "";
//			String option = "";
//			
//			while (rs.next()) {
//				option = rs.getString(1)==null?"":rs.getString(1);						
//				
//				value = 
//						cnct.delete(0,cnct.length())
//						.append(rs.getString(2)==null?"":rs.getString(2))
//						.append("|")
//						.append(rs.getString(3)==null?" ":rs.getString(3))
//						.append("|")
//						.append(rs.getString(4)==null?" ":rs.getString(4))
//						.toString();
//				
//				labelArr.add(option);
//				valuesArr.add(value);
//			}
			//Para evitar hacer la consulta, si desea ejecutar consula eliminar las dos lineas abajo y habilitar la consulta 
			labelArr.add("MEXICO");
			valuesArr.add("1|1|1");
			
			result.put("label", labelArr);
			result.put("value", valuesArr);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fillComboPaises()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				
				//resources.closeResources(rs, pstmt);
				//resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fillComboPaises()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap fillComboZonas(String cod, String level, String type) {	
		HashMap result = new HashMap(2);
		ArrayList valuesArr = new ArrayList();
		ArrayList labelArr = new ArrayList();
		//String query = "";
		
		try {
			
//			query = "SELECT ZONA, COD_ZONA, ZONA_LEVL, ZONA_TYPE FROM PCOBERTURA_VIEW WHERE COD_PAIS = ? AND PAIS_LEVEL = ? AND PAIS_TYPE = ? "
//					+ "GROUP BY ZONA, COD_ZONA, ZONA_LEVL, ZONA_TYPE "
//					+ "ORDER BY ZONA, COD_ZONA, ZONA_LEVL, ZONA_TYPE";
//
//			con = ConnectDB.getConnection();
//			pstmt = con.prepareStatement(query);
//			
//			pstmt.setString(1, cod);
//			pstmt.setString(2, level);
//			pstmt.setString(3, type);
//
//			rs = pstmt.executeQuery();
//							
//			String value = "";
//			String option = "";
//			
//			while (rs.next()) {
//				option = rs.getString(1)==null?"":rs.getString(1);						
//				
//				value = 
//						cnct.delete(0,cnct.length())
//						.append(rs.getString(2)==null?"":rs.getString(2))
//						.append("|")
//						.append(rs.getString(3)==null?" ":rs.getString(3))
//						.append("|")
//						.append(rs.getString(4)==null?" ":rs.getString(4))
//						.toString();
//				
//				labelArr.add(option);
//				valuesArr.add(value);
//			}
			//Para evitar hacer la consulta, si desea ejecutar consula eliminar las dos lineas abajo y habilitar la consulta
			labelArr.add("REPUBLICA MEXICANA");
			valuesArr.add("100000|2|1");
			
			result.put("label", labelArr);
			result.put("value", valuesArr);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fillComboZonas()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				//resources.closeResources(rs, pstmt);
				//resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRowsZonas()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap fillComboEstados(String cod, String level, String type) {		
		HashMap result = new HashMap(2);
		ArrayList valuesArr = new ArrayList();
		ArrayList labelArr = new ArrayList();
		String query = "";
		
		try {
			valuesArr.add("SEL");
			labelArr.add("SELECCIONE ESTADO");
			
			query = "SELECT ESTADO, COD_ESTA, EST_LEVEL, EST_TYPE FROM PCOBERTURA_VIEW WHERE COD_ZONA = ? AND ZONA_LEVL = ? AND ZONA_TYPE = ? AND GE_SEPOMEX IS NOT NULL "
					+ "GROUP BY ESTADO, COD_ESTA, EST_LEVEL, EST_TYPE "
					+ "ORDER BY ESTADO, COD_ESTA, EST_LEVEL, EST_TYPE";

			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, cod);
			pstmt.setString(2, level);
			pstmt.setString(3, type);

			rs = pstmt.executeQuery();
							
			String value = "";
			String option = "";
			
			while (rs.next()) {
				option = rs.getString(1)==null?"":rs.getString(1);						
				
				value = 
						cnct.delete(0,cnct.length())
						.append(rs.getString(2)==null?"":rs.getString(2))
						.append("|")
						.append(rs.getString(3)==null?" ":rs.getString(3))
						.append("|")
						.append(rs.getString(4)==null?" ":rs.getString(4))
						.toString();
				
				labelArr.add(option);
				valuesArr.add(value);
			}
			result.put("label", labelArr);
			result.put("value", valuesArr);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fillComboEstados()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fillComboEstados()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap fillComboDeleMuni(String cod, String level, String type) {
		HashMap result = new HashMap(2);
		ArrayList valuesArr = new ArrayList();
		ArrayList labelArr = new ArrayList();
		String query = "";
		
		try {
			
			valuesArr.add("SEL");
			labelArr.add("SELECCIONE DELEGACION / MUNICIPIO");
			
			query = "SELECT DELMUN, COD_DELE, DEL_LEVEL, DELETYPE FROM PCOBERTURA_VIEW WHERE COD_ESTA = ? AND EST_LEVEL = ? AND EST_TYPE = ? AND SUCURSAL IS NOT NULL  AND GE_SEPOMEX IS NOT NULL "
					+ "GROUP BY DELMUN, COD_DELE, DEL_LEVEL, DELETYPE "
					+ "ORDER BY DELMUN, COD_DELE, DEL_LEVEL, DELETYPE";

			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, cod);
			pstmt.setString(2, level);
			pstmt.setString(3, type);

			rs = pstmt.executeQuery();
							
			String value = "";
			String option = "";
			
			while (rs.next()) {
				option = rs.getString(1)==null?"":rs.getString(1);						
				
				value = 
						cnct.delete(0,cnct.length())
						.append(rs.getString(2)==null?"":rs.getString(2))
						.append("|")
						.append(rs.getString(3)==null?" ":rs.getString(3))
						.append("|")
						.append(rs.getString(4)==null?" ":rs.getString(4))
						.toString();
				
				labelArr.add(option);
				valuesArr.add(value);
			}
			result.put("label", labelArr);
			result.put("value", valuesArr);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fillComboDeleMuni()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fillComboDeleMuni()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap fillComboCiuPob(String cod, String level, String type) {		
		HashMap result = new HashMap(2);
		ArrayList valuesArr = new ArrayList();
		ArrayList labelArr = new ArrayList();
		String query = "";
		
		try {
			
			valuesArr.add("SEL");
			labelArr.add("SELECCIONE CIUDAD / POBLACION");
			
			query = "SELECT CIUDAD, COD_CIUD, CIU_LEVEL, CIU_TYPE FROM PCOBERTURA_VIEW WHERE COD_DELE = ? AND DEL_LEVEL = ? AND DELETYPE = ? AND SUCURSAL IS NOT NULL AND GE_SEPOMEX IS NOT NULL "
					+ "GROUP BY CIUDAD, COD_CIUD, CIU_LEVEL, CIU_TYPE "
					+ "ORDER BY CIUDAD, COD_CIUD, CIU_LEVEL, CIU_TYPE";

			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, cod);
			pstmt.setString(2, level);
			pstmt.setString(3, type);

			rs = pstmt.executeQuery();
							
			String value = "";
			String option = "";
			
			while (rs.next()) {
				option = rs.getString(1)==null?"":rs.getString(1);						
				
				value = 
						cnct.delete(0,cnct.length())
						.append(rs.getString(2)==null?"":rs.getString(2))
						.append("|")
						.append(rs.getString(3)==null?" ":rs.getString(3))
						.append("|")
						.append(rs.getString(4)==null?" ":rs.getString(4))
						.toString();
				
				labelArr.add(option);
				valuesArr.add(value);
			}
			result.put("label", labelArr);
			result.put("value", valuesArr);			
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fillComboCiuPob()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fillComboCiuPob()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap fillComboCiuMunByEdo(String cod, String level, String type) {
		HashMap result = new HashMap(2);
		ArrayList valuesArr = new ArrayList();
		ArrayList labelArr = new ArrayList();
		String query = "";
		
		try {
			
			valuesArr.add("SEL");
			labelArr.add("SELECCIONE CIUDAD / POBLACION");
			
			query = "SELECT CIUDAD FROM PCOBERTURA_VIEW WHERE COD_ESTA = ? AND EST_LEVEL = ? AND EST_TYPE = ? AND SUCURSAL IS NOT NULL AND GE_SEPOMEX IS NOT NULL "
					+ "GROUP BY CIUDAD "
					+ "ORDER BY CIUDAD";
			
			con = ConnectDB.getConnection();
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, cod);
			pstmt.setString(2, level);
			pstmt.setString(3, type);

			rs = pstmt.executeQuery();
							
			String value = "";
			String option = "";
			
			while (rs.next()) {
				option = rs.getString(1)==null?"":rs.getString(1);						
				
				value = 
						cnct.delete(0,cnct.length())
						.append("ALL")
						.append("|")
						.append(rs.getString(1)==null?" ":rs.getString(1))
						.append("|")
						.append("ALL")
						.toString();
				
				labelArr.add(option);
				valuesArr.add(value);
			}
			result.put("label", labelArr);
			result.put("value", valuesArr);
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fillComboCiuMunByEdo()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fillComboCiuMunByEdo()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}	
	
	@SuppressWarnings({ "rawtypes" })
	public ArrayList getRowsByEstado(String find, String valuesEstado) {		
		ArrayList result = new ArrayList();
		String query = "";
		
		try {
			query = cnct.delete(0, cnct.length())
					.append("SELECT COLO_ZIPCODE, COL_DES, COD_COLO, COL_LEVL, COL_TYPE, SUCURSAL, CIUDAD, COD_CIUD, CIU_LEVEL, CIU_TYPE, DELMUN, COD_DELE, DEL_LEVEL, DELETYPE, ESTADO, COD_ESTA, EST_LEVEL, EST_TYPE, PAIS, COD_PAIS, PAIS_LEVEL, PAIS_TYPE, ZONA, COD_ZONA, ZONA_LEVL, ZONA_TYPE ") 
					.append("FROM PCOBERTURA_VIEW ")
					.append("WHERE COL_DES LIKE ? AND COD_ESTA = ? AND EST_LEVEL = ? AND EST_TYPE = ? AND SUCURSAL IS NOT NULL AND GE_SEPOMEX IS NOT NULL ")
					.append("ORDER BY CIUDAD, COL_DES").toString();

			result = getRows(query, find, valuesEstado);		
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRowsByEstado()_Error1:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public ArrayList getRowsByMunicipio(String find, String valuesMunicipio) {		
		ArrayList result = new ArrayList();
		String query = "";
		
		try {
			query = cnct.delete(0, cnct.length())
					.append("SELECT COLO_ZIPCODE, COL_DES, COD_COLO, COL_LEVL, COL_TYPE, SUCURSAL, CIUDAD, COD_CIUD, CIU_LEVEL, CIU_TYPE, DELMUN, COD_DELE, DEL_LEVEL, DELETYPE, ESTADO, COD_ESTA, EST_LEVEL, EST_TYPE, PAIS, COD_PAIS, PAIS_LEVEL, PAIS_TYPE, ZONA, COD_ZONA, ZONA_LEVL, ZONA_TYPE ") 
					.append("FROM PCOBERTURA_VIEW ")
					.append("WHERE COL_DES LIKE ? AND COD_DELE = ? AND DEL_LEVEL = ? AND DELETYPE = ?  AND SUCURSAL IS NOT NULL AND GE_SEPOMEX IS NOT NULL ")
					.append("ORDER BY CIUDAD, COL_DES").toString();

			result = getRows(query, find, valuesMunicipio);			
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRowsByMunicipio()_Error1:").append(e).toString());
			e.printStackTrace();
		} 
		return result;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public ArrayList getRowsByCiudad(String find, String valuesCiudad) {		
		ArrayList result = new ArrayList();		
		String query = "";
		
		try {			
			query = cnct.delete(0, cnct.length())
					.append("SELECT COLO_ZIPCODE, COL_DES, COD_COLO, COL_LEVL, COL_TYPE, SUCURSAL, CIUDAD, COD_CIUD, CIU_LEVEL, CIU_TYPE, DELMUN, COD_DELE, DEL_LEVEL, DELETYPE, ESTADO, COD_ESTA, EST_LEVEL, EST_TYPE, PAIS, COD_PAIS, PAIS_LEVEL, PAIS_TYPE, ZONA, COD_ZONA, ZONA_LEVL, ZONA_TYPE ") 
					.append("FROM PCOBERTURA_VIEW ")
					.append("WHERE COL_DES LIKE ? AND COD_CIUD = ? AND CIU_LEVEL = ? AND CIU_TYPE = ? AND SUCURSAL IS NOT NULL AND GE_SEPOMEX IS NOT NULL ")
					.append("ORDER BY CIUDAD, COL_DES").toString();
			
			result = getRows(query, find, valuesCiudad);
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRowsByCiudad()_Error1:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	public ArrayList getRowsByCiuEdo(String find, String ciudad, String valuesEstado) {		
		ArrayList result = new ArrayList();
		String query = "";
		
		try {
			query = cnct.delete(0, cnct.length())
					.append("SELECT COLO_ZIPCODE, COL_DES, COD_COLO, COL_LEVL, COL_TYPE, SUCURSAL, CIUDAD, COD_CIUD, CIU_LEVEL, CIU_TYPE, DELMUN, COD_DELE, DEL_LEVEL, DELETYPE, ESTADO, COD_ESTA, EST_LEVEL, EST_TYPE, PAIS, COD_PAIS, PAIS_LEVEL, PAIS_TYPE, ZONA, COD_ZONA, ZONA_LEVL, ZONA_TYPE ") 
					.append("FROM PCOBERTURA_VIEW ")
					.append("WHERE COL_DES LIKE ? AND CIUDAD = ? AND COD_ESTA = ? AND EST_LEVEL = ? AND EST_TYPE = ? AND SUCURSAL IS NOT NULL AND GE_SEPOMEX IS NOT NULL ")
					.append("ORDER BY CIUDAD, COL_DES").toString();

			result = getRows2(query, find, ciudad, valuesEstado);
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRowsByEstado()_Error1:").append(e).toString());
			e.printStackTrace();
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getRows(String query, String find, String values) {		
		ArrayList result = new ArrayList();
		ArrayList row =  new ArrayList(20);
		
		
		try {
			String valuesDataArray[] = values.split("\\|");			
			
			con = ConnectDB.getConnection();			
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, "%"+find+"%");
			
			pstmt.setString(2, valuesDataArray[0]);
			pstmt.setString(3, valuesDataArray[1]);
			pstmt.setString(4, valuesDataArray[2]);			
			
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
				row.add(rs.getString(13));
				row.add(rs.getString(14));
				row.add(rs.getString(15));
				row.add(rs.getString(16));
				row.add(rs.getString(17));
				row.add(rs.getString(18));
				row.add(rs.getString(19));
				row.add(rs.getString(20));
				row.add(rs.getString(21));
				row.add(rs.getString(22));
				row.add(rs.getString(23));
				row.add(rs.getString(24));
				row.add(rs.getString(25));
				row.add(rs.getString(26));
				
				result.add(row.clone());
				row.clear();
			}			
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRows()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRows()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getRows2(String query, String find, String values1, String values2) {		
		ArrayList result = new ArrayList();
		ArrayList row =  new ArrayList(20);
		
		
		try {
			String valuesDataArray[] = values2.split("\\|");			
			
			con = ConnectDB.getConnection();			
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, "%"+find+"%");
			
			pstmt.setString(2, values1);
			pstmt.setString(3, valuesDataArray[0]);
			pstmt.setString(4, valuesDataArray[1]);
			pstmt.setString(5, valuesDataArray[2]);			
			
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
				row.add(rs.getString(13));
				row.add(rs.getString(14));
				row.add(rs.getString(15));
				row.add(rs.getString(16));
				row.add(rs.getString(17));
				row.add(rs.getString(18));
				row.add(rs.getString(19));
				row.add(rs.getString(20));
				row.add(rs.getString(21));
				row.add(rs.getString(22));
				row.add(rs.getString(23));
				row.add(rs.getString(24));
				row.add(rs.getString(25));
				row.add(rs.getString(26));
				
				result.add(row.clone());
				row.clear();
			}			
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRows()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getRows()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}
}