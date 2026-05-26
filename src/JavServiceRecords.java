/*
 *
 * FileName				   :	JavServiceRecords.java
 * Class Name            :    JavServiceRecords.class
 * Date					   :	16-May-2006
 * Purpose                 :    This file is used to populate values for Additional Service LOV
*/
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;

public class JavServiceRecords {
	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getLovRecords(Connection con, String clientId,String orginBranchId,String desBranchId) {
        HashMap values = new HashMap(3);
        ArrayList result = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
        	//Modified By Sam[23-06-2006] , for fixing duplicate Additional Service
            String query = "SELECT wt_srvc_id, wt_refr_srvc_id, wt_trif_amnt, pack_web.FUN_SRVC_NAME(wt_srvc_id) NOMBRE, NVL(WT_SRVC_EDIT,'N') EDIT FROM web_clnt_srvc_trif WHERE wt_orgn_clnt_id = ? AND wt_srvc_type = ? and substr(WT_ORGN_BRNC_ID,1,3) = substr(?,1,3) AND substr(WT_DEST_BRNC_ID,1,3) = substr(?,1,3)";

            pstmt = con.prepareStatement(query);

    		//Modified By Sam[23-06-2006] , for fixing duplicate Additional Service
    		pstmt.setString(1,clientId);
    		pstmt.setString(2, "A");
    		pstmt.setString(3,orginBranchId);
    		pstmt.setString(4,desBranchId);

    		rs = pstmt.executeQuery();

            while (rs.next()) {
                values.put("WT_SRVC_ID",rs.getString("WT_SRVC_ID"));
    			values.put("WT_TRIF_AMT",rs.getString("WT_TRIF_AMNT"));
    			values.put("WT_REFR_SRVC_ID",rs.getString("WT_REFR_SRVC_ID"));
    			values.put("NOMBRE",rs.getString("NOMBRE"));
    			values.put("WT_SRVC_EDIT",rs.getString("EDIT"));
                result.add(values.clone());
                values.clear();    			
            }            
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecords()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null) {
					pstmt.close();	
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecords()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}	
        return result;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getLovRecordsKm(Connection con, String clientId,String orginBranchId,String kmDist) {
        HashMap values = new HashMap(3);
        ArrayList result = new ArrayList();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
        	//Modified By Sam[23-06-2006] , for fixing duplicate Additional Service
            String query = "SELECT wt_srvc_id, wt_refr_srvc_id, wt_trif_amnt, pack_web.FUN_SRVC_NAME(wt_srvc_id) NOMBRE, NVL(WT_SRVC_EDIT,'N') EDIT FROM web_clnt_srvc_trif_km WHERE wt_orgn_clnt_id = ? AND wt_srvc_type = ? AND ? between WT_DSTN_FROM_KM and WT_DSTN_TO_KM";

            pstmt = con.prepareStatement(query);

    		//Modified By Sam[23-06-2006] , for fixing duplicate Additional Service
    		pstmt.setString(1,clientId);
    		pstmt.setString(2, "A");
    		pstmt.setString(3,kmDist);

    		rs = pstmt.executeQuery();

            while (rs.next()) {
                values.put("WT_SRVC_ID",rs.getString("WT_SRVC_ID"));
    			values.put("WT_TRIF_AMT",rs.getString("WT_TRIF_AMNT"));
    			values.put("WT_REFR_SRVC_ID",rs.getString("WT_REFR_SRVC_ID"));
    			values.put("NOMBRE",rs.getString("NOMBRE"));
    			values.put("WT_SRVC_EDIT",rs.getString("EDIT"));
                result.add(values.clone());
                values.clear();	
            }            
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecordsKm()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null) {
					rs.close();
				}
				if(pstmt!=null) {
					pstmt.close();	
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecordsKm()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}	
        return result;
    }
}