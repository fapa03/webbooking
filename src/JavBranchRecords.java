import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;

public class JavBranchRecords{
	private StringBuffer concatena = new StringBuffer();
	private final String msgError  = concatena.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
    public ArrayList getLovRecords(Connection con,String clientId) {		

        HashMap values = new HashMap(2);
        ArrayList result = new ArrayList();
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
    		/*added by B.Emerson on 20/06/2003 - 
    		only available destination branch for the web client should be displayed*/

    		String query = "select distinct pack_web.Fun_ftch_site_name(WO_DEST_SITE_ID) BM_SITE_NAME, WO_DEST_SITE_ID BM_SITE_ID from web_orgn_dest_clnt where WO_ORGN_CLNT_ID = ?  group by WO_DEST_SITE_ID order by 1";
            psmt = con.prepareStatement(query);
            psmt.setString(1,clientId);
    		
            rs = psmt.executeQuery();
    		
            while(rs.next()){                
                values.put("siteName",rs.getString("BM_SITE_NAME"));
                values.put("siteId",rs.getString("BM_SITE_ID"));
                result.add(values.clone());
                values.clear();
            }
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getLovRecords()Error:").append(e).toString());
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
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getLovRecords()Error2:").append(e2).toString());
				e2.printStackTrace();
			}			
		}        
        return result;
    }
    
	public ArrayList getLovRecords(Connection con, String clientId,
			String siteId, String sitename) {

		HashMap values = new HashMap(2);
		ArrayList result = new ArrayList();
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			String query1 = concatena.delete(0,concatena.length()).append("select SM_SITE_ID,SM_SITE_NAME from sys_site_mstr where SM_SITE_ID like '")
					.append(siteId)
					.append("%' and SM_SITE_NAME like '")
					.append(sitename)
					.append("%'  order by SM_SITE_NAME").toString();
			if (((sitename != null) && (sitename.length() > 0))
					&& ((siteId != null) && (siteId.length() > 0)))
				query1 = query1;
			else if ((siteId != null) && ((siteId.length() > 0)))
				query1 = "select SM_SITE_ID,SM_SITE_NAME from sys_site_mstr where SM_SITE_ID like '"
						+ siteId + "%'  order by SM_SITE_NAME";
			else if ((sitename != null) && ((sitename.length() > 0)))
				query1 = "select SM_SITE_ID,SM_SITE_NAME from sys_site_mstr where SM_SITE_NAME like '"
						+ sitename + "%'  order by SM_SITE_NAME";
			else
				query1 = "select SM_SITE_ID,SM_SITE_NAME from sys_site_mstr order by SM_SITE_NAME";
			psmt = con.prepareStatement(query1);
			
			rs = psmt.executeQuery();
			
			while (rs.next()) {				
				values.put("siteName", rs.getString("SM_SITE_NAME"));
				values.put("siteId", rs.getString("SM_SITE_ID"));
				result.add(values.clone());
				values.clear();
			}
			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getLovRecords2()Error:").append(e).toString());
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
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getLovRecords2()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}		
		return result;
	}    
}