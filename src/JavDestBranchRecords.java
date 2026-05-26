import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;

public class JavDestBranchRecords {
	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();

	public ArrayList getLovRecords(Connection con, String clientId) {
		HashMap values = new HashMap(2);
		ArrayList result = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			String query = cnct
					.delete(0, cnct.length())
					.append("SELECT BM_BRNC_NAME, BM_BRNC_ID FROM SYS_BRNC_MSTR WHERE BM_BRNC_ID NOT IN (")
					.append("SELECT WC_ASSN_BRNC FROM WEB_CLNT_MSTR WHERE  WC_CLNT_ID = ?")
					.append(") AND BM_FLAG1 NOT IN (?) ORDER BY BM_BRNC_NAME ").toString();

			psmt = con.prepareStatement(query);
			psmt.setString(1, clientId);
			psmt.setString(2, "T");
			
			rs = psmt.executeQuery();

			while (rs.next()) {			
				values.put("BM_BRNC_ID", rs.getString("BM_BRNC_ID"));
				values.put("BM_BRNC_NAME", rs.getString("BM_BRNC_NAME"));
				result.add(values.clone());
				values.clear();			
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecords()Error:").append(e).toString());
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
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecords()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}	
		return result;
	}
}