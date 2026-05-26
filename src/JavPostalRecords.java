import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;

public class JavPostalRecords {
	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();

	public ArrayList getLovRecords(Connection con, String postalLevel,
			String postalCode, String postalType) {

		HashMap values = new HashMap(5);
		ArrayList result = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String query = null;

		try {

			// Modified by Sam [07-06-2006] , Postel code is based on Branch
			query = "select ZIP_CODE, ge_desc, ge_levl, ge_type, ge_code FROM SYS_GETY_MSTR WHERE (GE_LEVL,GE_TYPE,GE_CODE) in (select GR_LEVL, GR_TYPE, GR_CODE from sys_gety_RESP where (GR_LEVL_R, GR_TYPE_R, GR_CODE_R) in (select GR_LEVL, GR_TYPE, GR_CODE from sys_gety_resp where gr_levl_r = ? and gr_code_R = ? AND GR_TYPE_R = ?)) ORDER BY 1";
			
			psmt = con.prepareStatement(query);

			psmt.setString(1, (postalLevel == null) ? "" : postalLevel);
			psmt.setString(2, (postalCode == null) ? "" : postalCode);
			psmt.setString(3, (postalType == null) ? "" : postalType);

			rs = psmt.executeQuery();

			while (rs.next()) {
				values.put("ZIP_CODE", (rs.getString("ZIP_CODE") == null) ? "" : rs.getString("ZIP_CODE"));
				values.put("ge_desc", rs.getString("ge_desc"));
				values.put("ge_levl", String.valueOf(rs.getInt("ge_levl")));
				values.put("ge_type", String.valueOf(rs.getInt("ge_type")));
				values.put("ge_code", rs.getString("ge_code"));

				result.add(values.clone());
				values.clear();
			}
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecords()Error:").append(exe).toString());
			exe.printStackTrace();
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