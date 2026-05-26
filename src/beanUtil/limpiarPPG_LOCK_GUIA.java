package beanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;

public class limpiarPPG_LOCK_GUIA {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PreparedStatement pst = null;
		int mes = 5;
		int anio = 2018;
		try {
			System.out.println("MES "+ mes);
			System.out.println("inicia proceso "+ new Date());
			Connection con = ConnectDBDirect.getConection();
			//String query = "SELECT WC_CLNT_ID, WC_PSWD, WC_APRV_BRNC FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID='18025'";
			//String query = "DELETE FROM PPG_LOCK_GUIA WHERE EXTRACT(Year FROM crtd_on) = 2014";
			String query = "DELETE FROM PPG_LOCK_GUIA WHERE EXTRACT(Year FROM crtd_on) = ? and EXTRACT(month FROM crtd_on) = ?";
			pst =  con.prepareStatement(query);
			pst.setInt(1, anio);
			pst.setInt(2, mes);
			int total = pst.executeUpdate();
			
			System.out.println("total registros "+total);
			System.out.println("termina proceso "+ new Date());
			pst.close();
			con.commit();
			//con.rollback();
			con.close();
		} catch (Exception e) {
			System.out.println("main()_Error: "+e);
			e.printStackTrace();
		} finally {
			try {
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
	}

}
