import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;

public class JavDescription {
	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getLovRecords(Connection con, String clientId) {
		HashMap values = new HashMap(6);
		ArrayList result = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		PreparedStatement psmt1 = null;
		ResultSet rs1 = null;
		String query = "SELECT SYS_SHP_DESC_MSTR.SS_DESC, SYS_SHP_DESC_MSTR.SS_CODE, SYS_SHP_DESC_MSTR.SS_SRVC_ID, SYS_SHP_DESC_MSTR.SS_REFR_SRVC_ID FROM SYS_SHP_DESC_MSTR";
		String query1 = "SELECT WC_WEIGHT, WC_VOLUME FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID = ?";
		String refer_id = "";
		try {
			psmt = con.prepareStatement(query);
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				values.put("SS_DESC", rs.getString(1));
				values.put("SS_CODE", rs.getString(2));
				values.put("SS_SRVC_ID", rs.getString(3));
				refer_id = rs.getString(4);

				if (refer_id.equalsIgnoreCase("PACKETS")) {

					psmt1 = con.prepareStatement(query1);
					psmt1.setString(1, clientId);
					rs1 = psmt1.executeQuery();
					while (rs1.next()) {
						// AccessLog.Log("inside packets weight and volume");
						values.put("weight", rs1.getString(1));
						values.put("volume", rs1.getString(2));
					}
					rs1.close();
					psmt1.close();						
				}
				values.put("SS_REFR_SRVC_ID", refer_id);
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
				if (rs1 != null) {
					rs1.close();
				}
				if (psmt1 != null) {
					psmt1.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecords()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
}