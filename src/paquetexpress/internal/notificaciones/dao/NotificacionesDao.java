package paquetexpress.internal.notificaciones.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import logger.AccessLog;
import bean.Resources;
import beanUtil.ConnectDB;

public class NotificacionesDao {
	
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	Connection con = null;

	PreparedStatement pstmt = null;	
	String tarifType = "";
	CallableStatement cst = null;
	ResultSet rs = null;
	public ArrayList validaGuia(String guia) {
		
		ArrayList result = new ArrayList();
		ArrayList row = new ArrayList(3);
		try {
			//String query = "SELECT BGM_REF_NO, BGM_ZONE, NVL (BGM_TARIFA,'S'), BGM_WGHT, BGM_VLUM, BGM_NO_OF_GUIS, NVL (BGM_ACK_SERVICE,'ACK-N'), NVL (BGM_INSUR_SERVICE,'0'), NVL (BGM_EAD_SERVICE,'S'), NVL (BGM_RAD_SERVICE,'S'), NVL (BGM_EXT_SERVICE,'N') FROM PPG_BOK_GUIA_MSTR WHERE BGM_PREP_BRNC_ID = ? AND BGM_CLINT_ID = ? AND BGM_VALID_FLG = ? AND BGM_REF_NO_OLD IS NULL ORDER BY BGM_TARIFA, BGM_ZONE";
			String query = cnct.delete(0, cnct.length())
					.append("SELECT GH_GUIA_NO, GH_ORGN_CLNT_ID, GH_DEST_CLNT_ID ")
					.append("FROM BOK_GUIA_HEAD ")
					.append("WHERE GH_GUIA_NO = ? AND GH_ACTV_FLAG = ?").toString();
			 
			con = ConnectDB.getConnection();
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, guia);
			pstmt.setString(2, "A");
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {				
				row.add(rs.getString(1) == null ? "" : rs.getString(1));
				row.add(rs.getString(2) == null ? "" : rs.getString(2));
				row.add(rs.getString(3) == null ? "" : rs.getString(3));
				result.add(row.clone());
				row.clear();
			} 
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validaGuia()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validaGuia()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return result;
	}	
	
	public int insertGuia(ArrayList datosGuia, String contacto, String eMail) {
		String serial = "1";
		
		int reg = 0;
		try {
			//String query = "SELECT BGM_REF_NO, BGM_ZONE, NVL (BGM_TARIFA,'S'), BGM_WGHT, BGM_VLUM, BGM_NO_OF_GUIS, NVL (BGM_ACK_SERVICE,'ACK-N'), NVL (BGM_INSUR_SERVICE,'0'), NVL (BGM_EAD_SERVICE,'S'), NVL (BGM_RAD_SERVICE,'S'), NVL (BGM_EXT_SERVICE,'N') FROM PPG_BOK_GUIA_MSTR WHERE BGM_PREP_BRNC_ID = ? AND BGM_CLINT_ID = ? AND BGM_VALID_FLG = ? AND BGM_REF_NO_OLD IS NULL ORDER BY BGM_TARIFA, BGM_ZONE";
			String query = cnct.delete(0, cnct.length())
					.append("SELECT MAX(NVL(SN_SERL_NO,0))+1 ")
					.append("FROM WEB_CTRL_NOTIFY_EMAIL ")
					.append("WHERE SN_GUIA_NO = ?").toString();
			 
			con = ConnectDB.getConnection();
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, datosGuia.get(0).toString());
			
			rs = pstmt.executeQuery();
			
			if (rs.next()) {
				serial = rs.getString(1) == null ? "1" : rs.getString(1);
			}
			//System.out.println("no encontro registro en ppg_lock_guia");
			rs.close();
			pstmt.close();
			
			query =	cnct.delete(0, cnct.length())
				.append("INSERT INTO WEB_CTRL_NOTIFY_EMAIL ")
				.append("(SN_GUIA_NO, SN_STUS_CODE, SN_SERL_NO, SN_STUS_DATE, SN_NOTIFIED, SN_NOTIFY_TYPE, SN_ORGN_CLNT_ID, SN_DEST_CLNT_ID, SN_EMAIL_1, SN_STUS_ENVIO, SN_CONTACTO, SN_ORGN_REQ, SN_PRIVACY_REQ, CRTD_ON, CRTD_BY) ")
				.append("Values ")
				.append("(?, ?, ?, TO_DATE(sysdate,'dd/mm/rrrr'), ?, ?, ?, ?, ?, ?, ?, ?, ?, TO_DATE(sysdate,'dd/mm/rrrr'), ?)").toString();
			
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, datosGuia.get(0).toString()); //guia
			pstmt.setString(2, "ENTREGA");
			pstmt.setString(3, serial);
			pstmt.setString(4, "N");
			pstmt.setString(5, "W");
			pstmt.setString(6, datosGuia.get(1).toString());//cliente origen
			pstmt.setString(7, datosGuia.get(2).toString());//cliente destino
			pstmt.setString(8, eMail);
			pstmt.setString(9, "P");
			pstmt.setString(10, contacto);
			pstmt.setString(11, "PW");
			pstmt.setString(12, "Y");
			pstmt.setString(13, "WEBNOTIFY");
			
			reg = pstmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertGuia()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				resources.closeResources(rs, pstmt);
				resources.cerrarConexion(con);
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertGuia()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return reg;
	}	
}