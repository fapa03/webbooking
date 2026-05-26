package paquetexpress.internal.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Hashtable;
import logger.AccessLog;
import beanUtil.ConnectDB;

public class JavControlMail {
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();

	
	public int insertWebCntrlMail(Hashtable datos) {
		PreparedStatement pst = null;
		Connection con =  null;
		int registros = 0;
		
		try {
			con = ConnectDB.getConnection();
			/*CE_GUIA_NO       VARCHAR2(15 BYTE)            NOT NULL,
			  CE_ORGN_CLNT_ID  VARCHAR2(15 BYTE)            NOT NULL,
			  CE_DEST_CLNT_ID  VARCHAR2(15 BYTE)            NOT NULL,
			  CE_EMAIL_ORGN    VARCHAR2(200 BYTE),
			  CE_EMAIL_DEST    VARCHAR2(200 BYTE),
			  CE_STUS_ENVIO    VARCHAR2(1 BYTE)             NOT NULL,
			  CRTD_ON          DATE                         NOT NULL,
			  CRTD_BY          VARCHAR2(15 BYTE)            NOT NULL,
			  MDFD_ON          DATE,
			  MDFD_BY          VARCHAR2(15 BYTE)*/
			
			String query = "INSERT INTO WEB_CTRL_EMAIL (CE_GUIA_NO, CE_ORGN_CLNT_ID, CE_DEST_CLNT_ID, CE_EMAIL_ORGN, CE_EMAIL_DEST, CE_STUS_ENVIO, CRTD_ON, CRTD_BY,  MDFD_ON, MDFD_BY) VALUES (?, ?, ?, ?, ?, ?, SYSDATE, ?, SYSDATE, ?)";
			pst = con.prepareCall(query);
			
			pst.setString(1, datos.get("genGuiaNumber").toString());
			pst.setString(2, datos.get("clientId").toString());
			pst.setString(3, datos.get("destclave").toString());
			pst.setString(4, datos.get("eMailOrigText").toString());
			pst.setString(5, datos.get("eMailDestText").toString());
			pst.setString(6, datos.get("statusMail").toString());
			pst.setString(7, datos.get("documentador").toString());
			pst.setString(8, datos.get("documentador").toString());
			//pst.registerOutParameter(8, Types.VARCHAR);
			
			registros = pst.executeUpdate();
			con.commit();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertWebCntrlMail()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally{
			try{
				if(pst!=null) {
					pst.close();
				}					
				
				if(con!=null) {
					con.close();	
				}			
				
			} catch(Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertBookGuiaStatus()_Error2:").append(e).toString());
			}
		
		}
		return registros;
	}
}
