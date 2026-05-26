package beanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class actualizaGL_SRVC_CHRG {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String rastreo = "";
			String GS_SRVC_ID = "";
			String GS_SUB_TOTL = "";
			String GL_REFR_SRVC_ID = "";
			String GL_SRVC_CHRG = "";
			
			con = ConnectDB.getConnection();
			//String query = "SELECT WC_CLNT_ID, WC_PSWD, WC_APRV_BRNC FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID='18025'";
			String query = 			
			"SELECT GS_GUIA_NO, GS_SRVC_ID, GS_SUB_TOTL, GL_SRVC_ID, GL_REFR_SRVC_ID, GL_SRVC_CHRG FROM BOK_GUIA_SRVC, BOK_GUIA_SRVC_ITEM WHERE GS_GUIA_NO=GL_GUIA_NO and GL_GUIA_NO IN ("
					+" select GH_GUIA_NO from bok_guia_head where "
					+" SUBSTR (GH_FORM_NO, 1,2) in (?) "
					+" AND TRUNC(GH_ISSE_DATE)>=TRUNC(SYSDATE)-2) "
					+" AND GS_SRVC_ID = GL_REFR_SRVC_ID "
					+" AND GS_SUB_TOTL> GL_SRVC_CHRG ";
					//+" AND GS_GUIA_NO = ? ";			
			System.out.println("query "+query);
			pst =  con.prepareStatement(query);
			pst.setString(1, "WW");
			//pst.setString(2, "19134965754");
			
			rs =  pst.executeQuery();			
			
			while (rs.next()) {
				rastreo =  rs.getString(1) == null ? "" : rs.getString(1);
				GS_SRVC_ID =  rs.getString(2) == null ? "" : rs.getString(2);
				GS_SUB_TOTL =  rs.getString(3) == null ? "" : rs.getString(3);
				GL_REFR_SRVC_ID =  rs.getString(4) == null ? "" : rs.getString(4);
				GL_SRVC_CHRG =  rs.getString(5) == null ? "" : rs.getString(5);
				
				System.out.println(
						"rastreo: "+rastreo +
						"\nGS_SRVC_ID: "+GS_SRVC_ID +
						"\nGS_SUB_TOTL: "+GS_SUB_TOTL +
						"\nGL_REFR_SRVC_ID: "+GL_REFR_SRVC_ID +
						"\nGL_SRVC_CHRG: "+GL_SRVC_CHRG
						);				
				updateGuiasSrvcChrg(con, rastreo, GS_SRVC_ID, GS_SUB_TOTL, GL_REFR_SRVC_ID);
			}
			/*rs.close();
			pst.close();*/
			con.commit();
			//con.close();
		} catch (Exception e) {
			System.out.println("main()_Error1: "+e);
			e.printStackTrace();
			try {
				con.rollback();
			} catch (Exception e2) {
				System.out.println("main()_Error2: "+e2);
				e2.printStackTrace();
			}
			
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (con!= null)
					con.close();
			} catch (Exception e2) {
				System.out.println("main()_Error3: "+e2);
				e2.printStackTrace();
			}
			
		}
	}	
	
	private static int updateGuiasSrvcChrg(Connection con, String rastreo, String GS_SRVC_ID, String GS_SUB_TOTL, String GL_REFR_SRVC_ID) {
		int reg = 0;
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		try {
			String query = 
					"UPDATE BOK_GUIA_SRVC_ITEM "+
					"SET GL_SRVC_CHRG = ? "+
					"WHERE GL_GUIA_NO = ? "+
					"AND GL_REFR_SRVC_ID = ?";

			pst2 = con.prepareStatement(query);
			
			pst2.setString(1, GS_SUB_TOTL);
			pst2.setString(2, rastreo);
			pst2.setString(3, GS_SRVC_ID);
						
			
			reg = pst2.executeUpdate();
			
			pst2.close();
			
			System.out.println(
					"actualiza\n GS_SUB_TOTL: "+GS_SUB_TOTL+
					"\n rastreo: "+rastreo+
					"\n GS_SRVC_ID: "+GS_SRVC_ID
					);
		} catch (Exception e) {
			System.out.println("updateGuiasSrvcChrg()_Error: "+e);
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
