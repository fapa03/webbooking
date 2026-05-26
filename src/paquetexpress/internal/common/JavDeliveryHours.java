package paquetexpress.internal.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import logger.AccessLog;

public class JavDeliveryHours {

	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	
	/*public String getDeliveryHoursRecord(Connection con, String sucOrigen, String sucDest, String ocuEadFlag, String codCol) {
		String result = "";
		PreparedStatement psmt = null;
		ResultSet rs = null;

		try {
			String query = "select FUN_GET_TIEMPO_ENTREGA(?,?,?,?) from dual";

			psmt = con.prepareStatement(query);
			psmt.setString(1, sucOrigen);
			psmt.setString(2, sucDest);
			psmt.setString(3, ocuEadFlag);
			psmt.setString(4, codCol);
			rs = psmt.executeQuery();

			if (rs.next()) {
				result = rs.getString(1)==null?"0":rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDeliveryHoursRecord()_Error1: ").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (psmt != null) {
					psmt.close();
				}				
			} catch (Exception e2) {
				AccessLog.Log(e2);
				e2.printStackTrace();
			}			
		}
		return result;
	}*/

	public String getDeliveryHoursRecord(Connection con, String sucOrigen, String sucDest, String ocuEadFlag, String codCol, String horaDoc, String srvcType, String clntId) {
		String result = "";
		PreparedStatement psmt = null;
		ResultSet rs = null;

		try {
			String query = "SELECT FUN_GET_PROMESA(?, ?, ?, ?, ?, ?, ?) FROM DUAL";

			psmt = con.prepareStatement(query);			
			psmt.setString(1, srvcType);
			psmt.setString(2, sucOrigen);
			psmt.setString(3, sucDest);
			psmt.setString(4, ocuEadFlag);
			psmt.setString(5, codCol);
			psmt.setString(6, horaDoc);
			psmt.setString(7, clntId);
			
			rs = psmt.executeQuery();

			if (rs.next()) {
				result = rs.getString(1)==null?"0":rs.getString(1);
				   /*if fdiasem ='6' and serviReg in ('SEG-2D') then
					       diaAdicional:= 1;
					   else*/
				
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDeliveryHoursRecord()_Error3: ").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDeliveryHoursRecord()_Error4: ").append(e).toString());
				e.printStackTrace();
			}			
		}
		return result;
	}
	
	public Timestamp getDeliveryDateRecord(Connection con, Timestamp FECHA_DOC , Long diaPromesa ) {
		Timestamp result = null;
		PreparedStatement psmt = null;
		ResultSet rs = null;

		try {
			String query = "SELECT FUN_FECHA_ESTIDLVYOFF(?, ?) FROM DUAL";

			psmt = con.prepareStatement(query);			
			psmt.setTimestamp(1, FECHA_DOC);
			psmt.setLong(2, diaPromesa);
			
			rs = psmt.executeQuery();

			if (rs.next()) {
				result = rs.getTimestamp(1)==null?null:rs.getTimestamp(1);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDeliveryDateRecord()_Error1: ").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDeliveryDateRecord()_Error2: ").append(e).toString());
				e.printStackTrace();
			}			
		}
		return result;
	}
	
	public String getDeliveryHoursRecordNew(Connection con, String sucOrigen, String sucDest, String ocuEadFlag, String codCol, String srvcType, String clntId, String tipoMerc) {
		String result = "";
		PreparedStatement psmt = null;
		ResultSet rs = null;

		try {
			String query = "SELECT FUN_GET_PROMESA_NEW(?, ?, ?, ?, ?, ?, to_char (SYSDATE,'HH24:MI'), ?) FROM DUAL";

			psmt = con.prepareStatement(query);
			psmt.setString(1, srvcType);
			psmt.setString(2, tipoMerc);
			psmt.setString(3, sucOrigen);
			psmt.setString(4, sucDest);
			psmt.setString(5, ocuEadFlag);
			psmt.setString(6, codCol);
			//psmt.setString(7, horaDoc);
			psmt.setString(7, clntId);
			
			rs = psmt.executeQuery();

			if (rs.next()) {
				result = rs.getString(1)==null?"0":rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDeliveryHoursRecordNew()_Error1: ").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDeliveryHoursRecordNew()_Error2: ").append(e).toString());
				e.printStackTrace();
			}			
		}
		return result;
	}
	
	public String getDeliveryHoursZPRecord(Connection con, String sucOrigen, String sucDest, String ocuEadFlag, String codCol, String srvcType, String clntId, String tipoMerc) {
		String result = "";
		PreparedStatement psmt = null;
		ResultSet rs = null;

		try {
			String query = "SELECT FUN_GET_PROMESA_RZP(?, ?, ?, ?, ?, ?, SYSDATE, ?) FROM DUAL";

			psmt = con.prepareStatement(query);
			psmt.setString(1, srvcType);
			psmt.setString(2, tipoMerc);
			psmt.setString(3, sucOrigen);
			psmt.setString(4, sucDest);
			psmt.setString(5, ocuEadFlag);
			psmt.setString(6, codCol);
			//psmt.setString(7, horaDoc);
			psmt.setString(7, clntId);
			
			rs = psmt.executeQuery();

			if (rs.next()) {
				result = rs.getString(1)==null?"0":rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDeliveryHoursZPRecord()_Error1: ").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (psmt != null) {
					psmt.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDeliveryHoursZPRecord()_Error2: ").append(e).toString());
				e.printStackTrace();
			}			
		}
		return result;
	}
}
