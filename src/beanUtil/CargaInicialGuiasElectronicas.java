package beanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CargaInicialGuiasElectronicas {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String rastreo = "";
			String cliente = "679967";
			String userSipweb = "ABARJONA";
			
			Connection con = ConnectDB.getConnection();
			//String query = "SELECT WC_CLNT_ID, WC_PSWD, WC_APRV_BRNC FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID='18025'";
			String query = 
					"SELECT BGD_TRAC_NO FROM PPG_BOK_GUIA_DTL WHERE BGD_REF_NO IN ("
					+ "SELECT BGM_REF_NO FROM PPG_BOK_GUIA_MSTR WHERE BGM_CLINT_ID = ? AND BGM_VALID_FLG = ?"
					+ ")" 
					+ "AND BGD_CONV_DATE IS NULL /*AND ROWNUM <=1*/";
			
			pst =  con.prepareStatement(query);
			
			pst.setString(1, cliente);
			pst.setString(2, "A");
			
			rs =  pst.executeQuery();
			
			while (rs.next()) {
				rastreo =  rs.getString(1) == null ? "" : rs.getString(1);
				
				System.out.println("rastreo: "+rastreo);				
				insGuiasAsignadas(con, rastreo, cliente, userSipweb);
			}
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
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
	}
	
	
	private static int insGuiasAsignadas(Connection con, String rastreo, String cliente, String userSipweb) {
		int reg = 0;
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		try {
			String query = "SELECT BGAS_TRAC_NO FROM PPG_BOK_GUIA_ASIG WHERE BGAS_TRAC_NO = ?";
			pst =  con.prepareStatement(query);
			pst.setString(1, rastreo);
			
			rs =  pst.executeQuery();			
			
			if (!rs.next()) {
				
				query = "Insert into PPG_BOK_GUIA_ASIG "+
				   "(BGAS_TRAC_NO, BGAS_CLNT_MSTR, BGAS_CLNT_ASIG, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY) "+
				   "Values " +
				   "(?, ?, ?, SYSDATE, ?, SYSDATE, ?)";

				pst2 = con.prepareStatement(query);
				
				pst2.setString(1, rastreo);
				pst2.setString(2, cliente);
				pst2.setString(3, cliente);
				pst2.setString(4, userSipweb);
				pst2.setString(5, userSipweb);
				
				
				reg = pst2.executeUpdate();
				pst2.close();
			}
			rs.close();
			pst.close();
		} catch (Exception e) {
			System.out.println("insGuiasAsignadas()_Error: "+e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (pst2 != null)
					pst2.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}			
		}
		return reg;		
	}	
}
