package beanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ActualizaFactorCostoAdicional {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			ActualizaFactorCostoAdicional dto = new ActualizaFactorCostoAdicional();
			dto.updaterPointToPoint();
			dto.updaterKmRange();
			
		} catch (Exception e) {
			System.out.println("main()_Error: "+e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
	}
	
	private void updaterPointToPoint() {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String orgnClnt = "";
			String orgnBrnc = "";
			String destBrnc = "";
			String trifSlab = "C";
			String servceId = "";
			String factorId = "";
			System.out.println("inicia consulta PUNTO A PUNTO");
			Connection con = ConnectDBDirect.getConection();
			//String query = "SELECT WC_CLNT_ID, WC_PSWD, WC_APRV_BRNC FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID='18025'";
			String query = 
					"SELECT WCP_ORGN_CLNT_ID, WCP_ORGN_BRNC_ID, WCP_DEST_BRNC_ID, WCP_TRIF_SLAB, WCP_SRVC_ID, WCP_FACTOR FROM WEB_CLNT_SRVC_TRIF_FACTOR WHERE WCP_SRVC_ID = 'SHP-G' " + 
					"GROUP BY WCP_ORGN_CLNT_ID, WCP_ORGN_BRNC_ID, WCP_DEST_BRNC_ID, WCP_TRIF_SLAB, WCP_SRVC_ID, WCP_FACTOR " + 
					"ORDER BY WCP_ORGN_CLNT_ID, WCP_ORGN_BRNC_ID";
			pst =  con.prepareStatement(query);
			rs =  pst.executeQuery();
			System.out.println("inicia proceso PUNTO A PUNTO");
			
			Double amnt = 0d;
			String var = "";
			while (rs.next()) {
				orgnClnt = rs.getString(1) == null ? "" : rs.getString(1);
				orgnBrnc = rs.getString(2) == null ? "" : rs.getString(2);
				destBrnc = rs.getString(3) == null ? "" : rs.getString(3);
				trifSlab = rs.getString(4) == null ? "" : rs.getString(4);
				servceId = rs.getString(5) == null ? "" : rs.getString(5);
				factorId = rs.getString(6) == null ? "" : rs.getString(6);
				
				var = "orgnClnt: "+ orgnClnt +
						" orgnBrnc: "+ orgnBrnc +
						" destBrnc: "+ destBrnc +
						" trifSlab: "+ trifSlab +
						" servceId: "+ servceId +
						" factorId: "+ factorId;
						
						
				System.out.println(var);
				
				amnt = getAditionalAmntPointToPoint(con, orgnClnt, orgnBrnc, destBrnc, trifSlab, servceId, factorId);
				if (amnt>=0){					
					int reg = updateAmntPointToPoint(con, orgnClnt, orgnBrnc, destBrnc, trifSlab, servceId, factorId, amnt);
					System.out.println("Se actualiza importe costo adicional PUNTO A PUNTO. Total registros "+reg);
				}
			}
			System.out.println("termina proceso PUNTO A PUNTO");
			rs.close();
			pst.close();
			//con.commit();
			con.close();
		} catch (Exception e) {
			System.out.println("updaterPointToPoint()_Error: "+e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
	}
	
	private Double getAditionalAmntPointToPoint(Connection con, String orgnClnt, String orgnBrnc, String destBrnc, String trifSlab, String servceId, String factorId) {
		Double amnt = -1d;
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = "SELECT WT_TRIF_AMNT FROM WEB_CLNT_SRVC_TRIF " + 
					"WHERE WT_ORGN_CLNT_ID = ? AND WT_ORGN_BRNC_ID = ?" + 
					"AND WT_DEST_BRNC_ID = ? AND WT_TRIF_SLAB = ? AND WT_SRVC_ID = ? AND WT_FACTOR = ?";
			
			pst = con.prepareStatement(query);
			
			pst.setString(1, orgnClnt);
			pst.setString(2, orgnBrnc);
			pst.setString(3, destBrnc);
			pst.setString(4, trifSlab);
			pst.setString(5, servceId);
			pst.setString(6, factorId);
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				amnt = rs.getDouble(1);
				System.out.println("importe a actualizar "+amnt);
			} else {
				System.out.println("No se encontró configuracion");
			}
			
		} catch (Exception e) {
			System.out.println("getAditionalAmntPointToPoint()_Error: "+e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}			
		}
		return amnt;		
	}
	
	private int updateAmntPointToPoint (Connection con, String orgnClnt, String orgnBrnc, String destBrnc, String trifSlab, String servceId, String factorId, Double amnt) {
		int reg = 0;
		PreparedStatement pst = null;
		try {
			String query = "UPDATE WEB_CLNT_SRVC_TRIF_FACTOR " + 
					"SET WCP_TRIF_AMNT_EXED = ? " + 
					"WHERE WCP_ORGN_CLNT_ID = ? AND WCP_ORGN_BRNC_ID = ? " + 
					"AND WCP_DEST_BRNC_ID = ? AND WCP_TRIF_SLAB = ? AND WCP_SRVC_ID = ? AND WCP_FACTOR = ?";
			
			pst = con.prepareStatement(query);
			
			pst.setDouble(1, amnt);
			pst.setString(2, orgnClnt);
			pst.setString(3, orgnBrnc);
			pst.setString(4, destBrnc);
			pst.setString(5, trifSlab);
			pst.setString(6, servceId);
			pst.setString(7, factorId);
			
			reg = pst.executeUpdate();

		} catch (Exception e) {
			System.out.println("updateAmntPointToPoint()_Error: "+e);
			e.printStackTrace();
		} finally {
			try {				
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}			
		}
		return reg;
	}
	
	private void updaterKmRange () {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String orgnClnt = "";
			String fromKm   = "";
			String toKm     = "";
			String trifSlab = "C";
			String servceId = "";
			String factorId = "";
			System.out.println("inicia consulta RANGO KM");
			Connection con = ConnectDBDirect.getConection();
			//String query = "SELECT WC_CLNT_ID, WC_PSWD, WC_APRV_BRNC FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID='18025'";
			String query = 
					"SELECT WCK_ORGN_CLNT_ID, WCK_DSTN_FROM_KM, WCK_DSTN_TO_KM, WCK_TRIF_SLAB, WCK_SRVC_ID, WCK_FACTOR FROM WEB_CLNT_SRVC_TRIF_FACTOR_KM WHERE WCK_SRVC_ID = 'SHP-G' " + 
					"GROUP BY WCK_ORGN_CLNT_ID, WCK_DSTN_FROM_KM, WCK_DSTN_TO_KM, WCK_TRIF_SLAB, WCK_SRVC_ID, WCK_FACTOR " + 
					"ORDER BY WCK_ORGN_CLNT_ID, WCK_DSTN_TO_KM, WCK_DSTN_TO_KM";
			pst =  con.prepareStatement(query);
			rs =  pst.executeQuery();
			System.out.println("inicia proceso RANGO KM");
			
			Double amnt = 0d;
			String var = "";
			while (rs.next()) {
				orgnClnt = rs.getString(1) == null ? "" : rs.getString(1);
				fromKm   = rs.getString(2) == null ? "" : rs.getString(2);
				toKm     = rs.getString(3) == null ? "" : rs.getString(3);
				trifSlab = rs.getString(4) == null ? "" : rs.getString(4);
				servceId = rs.getString(5) == null ? "" : rs.getString(5);
				factorId = rs.getString(6) == null ? "" : rs.getString(6);
				
				var = "orgnClnt: "+ orgnClnt +
						" fromKm: "+ fromKm +
						" toKm: "+ toKm +
						" trifSlab: "+ trifSlab +
						" servceId: "+ servceId +
						" factorId: "+ factorId;
						
				System.out.println(var);
				
				amnt = getAditionalAmntKmRange(con, orgnClnt, fromKm, toKm, trifSlab, servceId, factorId);
				if (amnt>=0){
					int reg = updateAmntKmRange(con, orgnClnt, fromKm, toKm, trifSlab, servceId, factorId, amnt);
					System.out.println("Se actualiza importe costo adicional RANGO KM. Total registros "+reg);
				}
			}
			System.out.println("termina proceso RANGO KM");
			rs.close();
			pst.close();
			//con.commit();
			con.close();
		} catch (Exception e) {
			System.out.println("updaterKmRange()_Error: "+e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
	}
	
	private Double getAditionalAmntKmRange(Connection con, String orgnClnt, String fromKm, String toKm, String trifSlab, String servceId, String factorId) {
		Double amnt = -1d;
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String query = "SELECT WT_TRIF_AMNT FROM WEB_CLNT_SRVC_TRIF_KM " + 
					"WHERE WT_ORGN_CLNT_ID = ? AND WT_DSTN_FROM_KM = ? AND WT_DSTN_TO_KM = ? " + 
					"AND WT_TRIF_SLAB = ? AND WT_SRVC_ID = ? AND WT_FACTOR = ?";
			
			pst = con.prepareStatement(query);
			
			pst.setString(1, orgnClnt);
			pst.setString(2, fromKm);
			pst.setString(3, toKm);
			pst.setString(4, trifSlab);
			pst.setString(5, servceId);
			pst.setString(6, factorId);
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				amnt = rs.getDouble(1);
				System.out.println("importe a actualizar "+amnt);
			} else {
				System.out.println("No se encontró configuracion");
			}
			
		} catch (Exception e) {
			System.out.println("getAditionalAmntKmRange()_Error: "+e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}			
		}
		return amnt;		
	}

	private int updateAmntKmRange(Connection con, String orgnClnt, String fromKm, String toKm, String trifSlab, String servceId, String factorId, Double amnt) {
		int reg = 0;
		PreparedStatement pst = null;
		try {
			String query = "UPDATE WEB_CLNT_SRVC_TRIF_FACTOR_KM " + 
					"SET WCK_TRIF_AMNT_EXED = ? " + 
					"WHERE WCK_ORGN_CLNT_ID = ? AND WCK_DSTN_FROM_KM = ? AND WCK_DSTN_TO_KM = ? " + 
					"AND WCK_TRIF_SLAB = ? AND WCK_SRVC_ID = ? AND WCK_FACTOR = ?";
			
			pst = con.prepareStatement(query);
			
			pst.setDouble(1, amnt);
			pst.setString(2, orgnClnt);
			pst.setString(3, fromKm);
			pst.setString(4, toKm);
			pst.setString(5, trifSlab);
			pst.setString(6, servceId);
			pst.setString(7, factorId);
			
			reg = pst.executeUpdate();

		} catch (Exception e) {
			System.out.println("updateAmntKmRange()_Error: "+e);
			e.printStackTrace();
		} finally {
			try {				
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}			
		}
		return reg;
	}
}
