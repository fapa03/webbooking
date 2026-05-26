package beanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CargaInicialCCostosDir {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			String idCliente = "";
			String sucAsignada = "";
			String siteAsignado = "";
			String userSipweb = "ABARJONA";
			String amAddrCode = "";
			String amPeSiteId = "";
			String coloPlaza = "";
			String coloSucursal = "";
			String centroCosto = "";
			
			con = ConnectDBDirect.getConection();
			//String query = "SELECT WC_CLNT_ID, WC_PSWD, WC_APRV_BRNC FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID='18025'";
			/*todos los clientes que coinciden sucursal asignada con sucursal de cobertura*/
			/*String query = "SELECT WC_CLNT_ID, WC_APRV_BRNC, WC_ASSN_SITE, AM_ADDR_CODE, AM_PE_SITE_ID, COLO_PLAZA, COLO_SUCURSAL "+
						"FROM WEB_CLNT_MSTR, SYS_ADDR_MSTR, DW_COBERTURA_VIEW WHERE WC_CLNT_ID = AM_ENTY_ID AND AM_GETY_CODE = COLO_ID "+
						"AND WC_APRV_BRNC=COLO_SUCURSAL";*/
			
			//String query = "SELECT WC_CLNT_ID, WC_PSWD, WC_APRV_BRNC FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID='18025'";
			/*todos los clientes que coinciden con plaza asignada y con plaza de cobertura*/ 
			/*String query = "SELECT WC_CLNT_ID, WC_APRV_BRNC, WC_ASSN_SITE, AM_ADDR_CODE, AM_PE_SITE_ID, COLO_PLAZA, COLO_SUCURSAL " +
								"FROM WEB_CLNT_MSTR, SYS_CLNT_CCOSTO,  SYS_ADDR_MSTR, DW_COBERTURA_VIEW WHERE WC_CLNT_ID = CC_CLNT_ID AND CC_ADDR_CODE IS NULL AND WC_CLNT_ID = AM_ENTY_ID AND AM_GETY_CODE = COLO_ID "+
								//--AND WC_APRV_BRNC=COLO_SUCURSAL
								"AND WC_ASSN_SITE = COLO_PLAZA";*/
			
			/*todos los clientes que coinciden sucursal asignada con sucursal de cobertura 20150825*/
			String query = "SELECT WC_CLNT_ID, WC_APRV_BRNC, WC_ASSN_SITE, AM_ADDR_CODE, AM_PE_SITE_ID, COLO_PLAZA, COLO_SUCURSAL, CC_CCOSTO_ID "+
			"FROM SYS_CLNT_USER A,  SYS_CLNT_CCOSTO_USER B, SYS_CLNT_CCOSTO C, WEB_CLNT_MSTR D, SYS_ADDR_MSTR E, DW_COBERTURA_VIEW F "+
			"WHERE A.CU_USER_ID = B.CU_USER_ID  AND B.CU_CLNT_ID = C.CC_CLNT_ID AND CC_CCOSTO_ID = B.CU_CCOSTO_ID AND C.CC_ADDR_CODE IS NULL "+
			"AND WC_CLNT_ID = A.CU_CLNT_ID "+
			"AND WC_CLNT_ID = AM_ENTY_ID AND AM_GETY_CODE = COLO_ID "+
			//"AND SUBSTR(WC_APRV_BRNC,1,3) <> COLO_PLAZA  "; //--POR SITE
			"AND WC_APRV_BRNC = COLO_SUCURSAL  ";//--POR SUCURSAL 
			
			/*direcciones con direccion estructurada sin cobertura o direccion no estructurada*/
			/*String query = "SELECT WC_CLNT_ID, WC_APRV_BRNC, WC_ASSN_SITE, AM_ADDR_CODE, AM_PE_SITE_ID, 'COLO_PLAZA', 'COLO_SUCURSAL', CC_CCOSTO_ID "+
			"FROM SYS_CLNT_USER A,  SYS_CLNT_CCOSTO_USER B, SYS_CLNT_CCOSTO C, WEB_CLNT_MSTR D, SYS_ADDR_MSTR E "+
			"WHERE A.CU_USER_ID = B.CU_USER_ID  AND B.CU_CLNT_ID = C.CC_CLNT_ID AND CC_CCOSTO_ID = B.CU_CCOSTO_ID AND C.CC_ADDR_CODE IS NULL "+
			"AND WC_CLNT_ID = A.CU_CLNT_ID "+
			"AND WC_CLNT_ID = AM_ENTY_ID "+
//			"and AM_GETY_CODE is not null ";
			"and AM_GETY_CODE is null ";*/
			
			System.out.println("query "+query);
			pst =  con.prepareStatement(query);
			rs =  pst.executeQuery();
			int reg = 0;
			int contador = 0;
			while (rs.next()) {
				idCliente =  rs.getString(1) == null ? "" : rs.getString(1);
				sucAsignada =  rs.getString(2) == null ? "" : rs.getString(2);
				siteAsignado =  rs.getString(3) == null ? "" : rs.getString(3);
				amAddrCode =  rs.getString(4) == null ? "" : rs.getString(4);
				amPeSiteId =  rs.getString(5) == null ? "" : rs.getString(5);
				coloPlaza =  rs.getString(6) == null ? "" : rs.getString(6);
				coloSucursal =  rs.getString(7) == null ? "" : rs.getString(7);
				centroCosto =  rs.getString(8) == null ? "" : rs.getString(8);
				
				System.out.println("Cliente: "+idCliente);
				System.out.println("sucursal: "+siteAsignado);
				System.out.println("amAddrCode: "+amAddrCode);
				System.out.println("reg: "+reg);
				System.out.println("************************************************************");
				contador++;
				/*update SYS_CLNT_CCOSTO*/
				reg = updateDirSysClntCCosto(con, idCliente, amAddrCode, centroCosto);				
			}
			rs.close();
			pst.close();
			con.commit();
			//con.rollback();
			con.close();
			
			System.out.println("contador: "+contador);
		} catch (Exception e) {
			System.out.println("main()_Error: "+e);
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (con != null)
					con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}			
		}
	}	
	
	private static int updateDirSysClntCCosto(Connection con, String idCliente, String amAddrCode, String centroCosto) {
		int reg = 0;
		PreparedStatement pst = null;
		try {
			String query = "UPDATE SYS_CLNT_CCOSTO SET CC_ADDR_CODE = ?, CC_DELETE_FLAG = ? WHERE CC_CLNT_ID = ? and CC_CCOSTO_ID = ?";
			pst =  con.prepareStatement(query);
			pst.setString(1, amAddrCode);
			//pst.setString(2, "1"); //1 clientes que concide la sucursal asignada con la sucursal de cobertura
			//pst.setString(2, "2"); //2 clientes que concide el site asignado con el site de cobertura
			//pst.setString(2, "3"); //3 clientes diferente site asignado con el site de cobertura
//			pst.setString(2, "4"); //4 clientes direccion estructurada sin cobertura.
			//pst.setString(2, "5"); //4 clientes direccion NO estructurada.
			pst.setString(2, "6"); //6 clientes que concide la sucursal asignada con la sucursal de cobertura 20151016
			pst.setString(3, idCliente);
			pst.setString(4, centroCosto);
			
			reg = pst.executeUpdate();
			
			pst.close();
		} catch (Exception e) {
			System.out.println("updateDirSysClntCCosto()_Error: "+e);
			e.printStackTrace();
		}finally {
			try {
				if (pst != null)
					pst.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		return reg;		
	}
	
}
