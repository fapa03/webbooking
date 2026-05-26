import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;

public class JavAssignedToBranchRecords{
	private StringBuffer concatena = new StringBuffer();
	private final String msgError  = concatena.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();	
    public ArrayList getLovRecords(Connection con) {
        HashMap values = new HashMap(2);
        ArrayList result = new ArrayList(100);
        PreparedStatement psmt = null;
        ResultSet rs = null;
        try {
        	//Modified By Sam[07-06-2006] for displaying alphabetical order
    		String query = "SELECT BM_BRNC_NAME,BM_BRNC_ID FROM SYS_BRNC_MSTR WHERE BM_WB_DEST = ? ORDER BY BM_BRNC_NAME";
    		psmt = con.prepareStatement(query);
    		psmt.setString(1, "Y");

    		rs = psmt.executeQuery();

            while (rs.next()) {   			
    			values.put("BM_BRNC_ID",rs.getString("BM_BRNC_ID"));
    			values.put("BM_BRNC_NAME",rs.getString("BM_BRNC_NAME"));
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
}