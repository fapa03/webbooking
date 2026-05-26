import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;

public class JavCitiesRecords {
	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();

	public ArrayList getLovRecords(Connection con, String currentDest) {
		HashMap values = new HashMap(4);
		ArrayList result = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		String query = null;
		try {
			if (currentDest != null && currentDest.trim().length() > 0) {				
				query = "SELECT GE_DESC, GE_CODE, ESTADO, GE_TYPE, GE_LEVL FROM FILTER_CITY_LOV WHERE PM_CODE = ? ORDER BY 1";
				psmt = con.prepareStatement(query);
				psmt.setString(1, currentDest);
			} else {				
				// Modified By Sam[07-06-2006] for getting GE_TYPE for Postal code generation
				query = "select GE_DESC,GE_CODE, ESTADO,GE_TYPE from all_city_lov order by 1";
				psmt = con.prepareStatement(query);
			}

			rs = psmt.executeQuery();

			while (rs.next()) {
				values.put("GE_DESC", rs.getString("GE_DESC"));
				values.put("GE_CODE", rs.getString("GE_CODE"));
				values.put("ESTADO", rs.getString("ESTADO"));
				// Added By Sam[07-06-2006] for Postal code getneration based on GE_TYPE of City
				values.put("GE_TYPE", rs.getString("GE_TYPE"));

				result.add(values.clone());
				values.clear();
			}			
		} catch (Exception exe) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getLovRecords()Error:").append(exe).toString());			
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
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getLovRecords()Error2:").append(e2).toString());
				e2.printStackTrace();
			}	
		}
		return result;
	}
}