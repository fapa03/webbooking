package beanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

public class CargaInicialWO_MBR_CLNT_ID02UserSipWeb {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		PreparedStatement pst2 = null;
		ResultSet rs2 = null;
		System.out.println("FECHA INCIAL "+new Date());
		try {
			String userWB = "";
			String mbrClntId = "";
			
			Connection con = ConnectDB.getConnection();
			//String query = "SELECT WC_CLNT_ID, WC_PSWD, WC_APRV_BRNC FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID='18025'";
			String query = "SELECT A.CRTD_BY, WO_ORGN_CLNT_ID FROM WEB_ORGN_DEST_CLNT A, SYS_USER_MSTR B WHERE A.CRTD_BY = UM_USER_ID AND WO_MBR_CLNT_ID IS NULL "+
							"GROUP BY A.CRTD_BY, WO_ORGN_CLNT_ID "+
							"ORDER BY A.CRTD_BY, WO_ORGN_CLNT_ID";
			pst =  con.prepareStatement(query);
			rs =  pst.executeQuery();
			int count = 0;
			int cada10 = 0;
			while (rs.next()) {
				userWB =  rs.getString(1) == null ? "" : rs.getString(1);
				mbrClntId =  rs.getString(2) == null ? "" : rs.getString(2);
				
				//System.out.println("userWB: "+userWB);
				//System.out.println("mbrClntId: "+mbrClntId);
				//System.out.println("---------------------------------------");
				/*actualiza WO_MBR_CLNT_ID*/
				
				pst2 = con.prepareStatement("UPDATE WEB_ORGN_DEST_CLNT SET WO_MBR_CLNT_ID = ? WHERE WO_ORGN_CLNT_ID = ? AND CRTD_BY = ? AND WO_MBR_CLNT_ID IS NULL");
				pst2.setString(1, mbrClntId);
				pst2.setString(2, mbrClntId);
				pst2.setString(3, userWB);
				
				pst2.executeUpdate();
				
				pst2.close();
				//updateWO_MBR_CLNT_ID(con, userWB, mbrClntId);
				count++;
				cada10++;
				if (cada10==10) {
					System.out.println(count +" Usuarios actualizados");
					cada10 = 0;
				}
			}
			System.out.println("registros totales "+count);
			System.out.println("FECHA Final "+new Date());
			rs.close();
			pst.close();
			con.commit();
			con.close();
		} catch (Exception e) {
			System.out.println("main()_Error: "+e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (rs2 != null)
					rs.close();
				if (pst2 != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
	}
	
	private static int updateWO_MBR_CLNT_ID(Connection con, String userWB, String mbrClntId) {
		int reg = 0;		
		PreparedStatement pst2 = null;
		try {
			String query = "UPDATE WEB_ORGN_DEST_CLNT SET WO_MBR_CLNT_ID = ? WHERE CRTD_BY = ? AND WO_MBR_CLNT_ID IS NULL";
			
			pst2 = con.prepareStatement(query);
			pst2.setString(1, mbrClntId);
			pst2.setString(2, userWB);			
			
			reg = pst2.executeUpdate();
			
			pst2.close();
		} catch (Exception e) {
			System.out.println("updateWO_MBR_CLNT_ID()_Error: "+e);
			e.printStackTrace();
		} finally {
			try {
				if (pst2 != null)
					pst2.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		return reg;		
	}
}
