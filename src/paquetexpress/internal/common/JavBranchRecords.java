package paquetexpress.internal.common;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bean.Resources;
import beanUtil.ConnectDB;
import logger.AccessLog;

public class JavBranchRecords{
	private Resources resource = new Resources();
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	
    
    
    public String branchLocationType(Connection con, String brncId){//AAP20
		String result = "";
		
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			pst = con.prepareStatement("SELECT BM_BRNC_LOC_TYPE FROM SYS_BRNC_MSTR WHERE BM_BRNC_ID = ?");
			pst.setString(1, brncId);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				result = rs.getString(1);
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("branchLocationType()1_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resource.closeResources(rs, pst);	
		}
		return result;
	}
    
    public String branchLocationType(String brncId){//AAP20
    	String result = "";
    	Connection con = null;
		try {
			con = ConnectDB.getConnection();			
			result = branchLocationType(con, brncId);

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("branchLocationType()2_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resource.cerrarConexion(con);			
		}
		return result;
    }
}