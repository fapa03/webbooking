package beanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CargaInicialCCostos {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String idCliente = "";
			String pswCliente = "";
			String sucOrigen = "";
			String idCentroCosto = "0001";
			String descCentroCosto = "GENERAL";
			String userSipweb = "ABARJONA";
			
			Connection con = ConnectDB.getConnection();
			//String query = "SELECT WC_CLNT_ID, WC_PSWD, WC_APRV_BRNC FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID='18025'";
			String query = "SELECT WC_CLNT_ID, WC_PSWD, WC_APRV_BRNC FROM WEB_CLNT_MSTR";
			pst =  con.prepareStatement(query);
			rs =  pst.executeQuery();
			
			while (rs.next()) {
				idCliente =  rs.getString(1) == null ? "" : rs.getString(1);
				pswCliente =  rs.getString(2) == null ? "" : rs.getString(2);
				sucOrigen =  rs.getString(3) == null ? "" : rs.getString(3);
				
				System.out.println("Cliente: "+idCliente);
				System.out.println("sucursal: "+sucOrigen);
				/*inserta SYS_CLNT_USER*/
				insUsuario(con, idCliente, pswCliente, userSipweb);				
				/*inserta SYS_CLNT_CCOSTO*/
				insCCosto(con, idCliente, idCentroCosto, descCentroCosto, userSipweb);
				/*inserta SYS_CLNT_CCOSTO_USER*/
				insCCostoUsuario(con, idCliente, idCentroCosto, userSipweb);
				/*inserta BOK_GUIA_HEAD_EXTRA*/
				insBokGuiaHeadExtra(con, idCliente, idCentroCosto, sucOrigen);
				/*actualiza WEB_MNFT_DETL*/
				updateWebMnftDetl(con, idCliente, idCentroCosto);				
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
	
	
	private static int insUsuario(Connection con, String idCliente, String pswCliente, String userSipweb) {
		int reg = 0;
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		try {
			String query = "SELECT CU_CLNT_ID FROM SYS_CLNT_USER WHERE CU_CLNT_ID = ? AND CU_USER_ID = ?";
			pst =  con.prepareStatement(query);
			pst.setString(1, idCliente);
			pst.setString(2, idCliente);
			rs =  pst.executeQuery();
			
			
			if (!rs.next()) {
				query = "Insert into SYS_CLNT_USER "+
				   "(CU_CLNT_ID, CU_USER_ID, CU_USER_NAME, CU_USER_PWRD, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY, CU_PWRD_EXP, CU_WEB_FLAG, CU_PPG_FLAG) "+
				   "Values " +
				   "(?, ?, SUBSTR (Nvl(pack_web.fun_ftch_clnt_name(?),' '), 1, 50) , ?, SYSDATE, ?, SYSDATE, ?, SYSDATE, ?, ?)";
				pst2 = con.prepareStatement(query);
				pst2.setString(1, idCliente);
				pst2.setString(2, idCliente);
				pst2.setString(3, idCliente);
				pst2.setString(4, pswCliente);
				pst2.setString(5, userSipweb);
				pst2.setString(6, userSipweb);
				pst2.setString(7, "Y");
				pst2.setString(8, "N");
				
				reg = pst2.executeUpdate();
				pst2.close();
			}
			rs.close();
			pst.close();
		} catch (Exception e) {
			System.out.println("insUsuario()_Error: "+e);
			e.printStackTrace();
		}finally {
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
	
	private static int insCCosto(Connection con, String idCliente, String centroCosto, String descCentroCosto, String userSipweb) {
		int reg = 0;	
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		try {
			String query = "SELECT CC_CLNT_ID FROM SYS_CLNT_CCOSTO WHERE CC_CLNT_ID = ? AND CC_CCOSTO_ID = ?";
			pst =  con.prepareStatement(query);
			pst.setString(1, idCliente);
			pst.setString(2, centroCosto);
			rs =  pst.executeQuery();
			
			if (!rs.next()) {
				query = "Insert into SYS_CLNT_CCOSTO"+
				   "(CC_CLNT_ID, CC_CCOSTO_ID, CC_CCOSTO_NAME, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY)"+
				 "Values"+
				   "(?, ?, ?, SYSDATE, ?, SYSDATE, ?)";
				pst2 = con.prepareStatement(query);
				pst2.setString(1, idCliente);
				pst2.setString(2, centroCosto);
				pst2.setString(3, descCentroCosto);
				pst2.setString(4, userSipweb);
				pst2.setString(5, userSipweb);
				
				reg = pst2.executeUpdate();
				
				pst2.close();
			}
			rs.close();
			pst.close();
		} catch (Exception e) {
			System.out.println("insCCosto()_Error: "+e);
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
	
	private static int insCCostoUsuario(Connection con, String idCliente, String centroCosto, String userSipweb) {
		int reg = 0;	
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		try {
			String query = "SELECT CU_CLNT_ID FROM SYS_CLNT_CCOSTO_USER WHERE CU_CLNT_ID = ? AND CU_CCOSTO_ID = ? AND CU_USER_ID = ?";
			pst =  con.prepareStatement(query);
			pst.setString(1, idCliente);
			pst.setString(2, centroCosto);
			pst.setString(3, idCliente);
			
			rs =  pst.executeQuery();
			
			if (!rs.next()) {				
				query = "Insert into SYS_CLNT_CCOSTO_USER"+
				   "(CU_CLNT_ID, CU_CCOSTO_ID, CU_USER_ID, CU_DFLT_FLAG, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY)"+
				 "Values"+
				   "(?, ?, ?, ?, SYSDATE, ?, SYSDATE, ?)";
				
				pst2 = con.prepareStatement(query);
				pst2.setString(1, idCliente);
				pst2.setString(2, centroCosto);
				pst2.setString(3, idCliente);
				pst2.setString(4, "Y");
				pst2.setString(5, userSipweb);
				pst2.setString(6, userSipweb);
				
				reg = pst2.executeUpdate();
				
				pst2.close();
			}
			rs.close();
			pst.close();
		} catch (Exception e) {
			System.out.println("insCCosto()_Error: "+e);
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
	
	private static int insBokGuiaHeadExtra(Connection con, String idCliente, String centroCosto, String sucOrigen) {
		int reg = 0;	
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		try {
			String query = "SELECT GH_GUIA_NO FROM BOK_GUIA_HEAD WHERE GH_ORGN_BRNC_ID = ? AND GH_ORGN_CLNT_ID = ? AND GH_GUIA_REFR_NO IS NULL AND GH_GUIA_TYPE = ? AND GH_ACTV_FLAG = ?"+ 
					"and GH_GUIA_NO NOT IN (SELECT GE_GUIA_NO FROM BOK_GUIA_HEAD_EXTRA WHERE GE_CLNT_ID = ?)";
			pst =  con.prepareStatement(query);
			pst.setString(1, sucOrigen);
			pst.setString(2, idCliente);
			pst.setString(3, "H");
			pst.setString(4, "A");
			pst.setString(5, idCliente);
			
			rs =  pst.executeQuery();
			int count = 0;
			
			while (rs.next()) {
				count++;
				query = "Insert into BOK_GUIA_HEAD_EXTRA"+
				   "(GE_GUIA_NO, GE_CLNT_ID, GE_CLNT_CCOSTO_ID, GE_CLNT_USER_ID)"+
				 "Values"+
				   "(?, ?, ?, ?)";
				System.out.println("guia: "+rs.getString(1));
				pst2 = con.prepareStatement(query);
				pst2.setString(1, rs.getString(1));
				pst2.setString(2, idCliente);
				pst2.setString(3, centroCosto);
				pst2.setString(4, idCliente);
				
				reg = pst2.executeUpdate();
				
				pst2.close();
			}
			System.out.println("Total de guias: "+count+" Cliente "+idCliente);
			rs.close();
			pst.close();
		} catch (Exception e) {
			System.out.println("insBokGuiaHeadExtra()_error: "+e);
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
	
	private static int updateWebMnftDetl(Connection con, String idCliente, String centroCosto) {
		int reg = 0;		
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		try {
			//MD_FLAG_2 = Q (tipo de documento)
			//MD_FLAG_3 = '0001' (centro de costo)
			String query = "SELECT MD_MNFT_NO FROM WEB_MNFT_DETL WHERE MD_CLNT_ID = ? AND MD_INVC_NO IS NULL AND MD_FLAG_2 <> ? AND MD_FLAG_3 IS NULL";
			pst =  con.prepareStatement(query);
			pst.setString(1, idCliente);
			pst.setString(2, "P");
			
			rs =  pst.executeQuery();
			int count = 0;
			while (rs.next()) {
				count++;
				System.out.println("Manifiesto: "+rs.getString(1));
				query = "UPDATE WEB_MNFT_DETL SET MD_FLAG_2 = ?, MD_FLAG_3 = ? WHERE MD_MNFT_NO = ?";
				
				pst2 = con.prepareStatement(query);
				pst2.setString(1, "Q");
				pst2.setString(2, centroCosto);
				pst2.setString(3, rs.getString(1));			
				
				reg = pst2.executeUpdate();
				
				pst2.close();
			}
			System.out.println("Total de Manifiestos: "+count +" Cliente "+idCliente);
			rs.close();
			pst.close();
		} catch (Exception e) {
			System.out.println("updateWebMnftDetl()_Error: "+e);
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
