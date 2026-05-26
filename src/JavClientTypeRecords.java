import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;

public class JavClientTypeRecords {
	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();

	public ArrayList getLovRecords(Connection con) {
		HashMap values = new HashMap(2);
		ArrayList result = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			String query = "SELECT PM_PARM_CODE1 ,PM_VLUE1_DESC FROM SYS_PARM_MSTR where PM_PARM_TYPE = ? AND PM_MDUL_ID = ? AND PM_PARM_CODE1 IN(?, ?)";

			psmt = con.prepareStatement(query);
			
			psmt.setString(1, "CLIENT_TYPE");
			psmt.setString(2, "SYS");
			psmt.setString(3, "C");
			psmt.setString(4, "I");
			
			rs = psmt.executeQuery();

			while (rs.next()) {				
				values.put("PM_PARM_CODE1", rs.getString("PM_PARM_CODE1"));
				values.put("PM_VLUE1_DESC", rs.getString("PM_VLUE1_DESC"));
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