package beanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CargaSucursalOmisionCC {

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

			
			String query = "SELECT WC_CLNT_ID, WC_APRV_BRNC, WC_ASSN_SITE, AM_ADDR_CODE, AM_PE_SITE_ID, COLO_PLAZA, COLO_SUCURSAL, CC_CCOSTO_ID "+ 
			"FROM WEB_CLNT_MSTR , SYS_CLNT_MSTR,SYS_CLNT_CCOSTO, SYS_ADDR_MSTR, DW_COBERTURA_VIEW "+
			"WHERE WC_CLNT_ID= CC_CLNT_ID AND SYS_ADDR_MSTR.AM_ADDR_CODE = CC_ADDR_CODE AND DW_COBERTURA_VIEW.COLO_ID  = AM_GETY_CODE "+
			"AND WEB_CLNT_MSTR.WC_CLNT_ID  = SYS_CLNT_MSTR.CM_CLNT_ID and SYS_CLNT_CCOSTO.CC_bRNC_ORGN IS NULL "+
			"AND WC_APRV_BRNC <> COLO_SUCURSAL  "+
			"AND (SELECT COUNT(*) FROM BOK_GUIA_HEAD WHERE GH_ORGN_CLNT_ID= SYS_CLNT_MSTR.CM_CLNT_ID AND GH_GUIA_TYPE ='H' AND GH_ISSE_DATE>=SYSDATE-360) >0 ";

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
				System.out.println("sucAsignada: "+sucAsignada);
				System.out.println("coloSucursal: "+coloSucursal);
				System.out.println("reg: "+reg);
				System.out.println("************************************************************");
				contador++;
				/*update SYS_CLNT_CCOSTO*/
				reg = updateSucOmisionClntCCosto(con, idCliente, sucAsignada, centroCosto);				
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
	
	private static int updateSucOmisionClntCCosto(Connection con, String idCliente, String sucOmision, String centroCosto) {
		int reg = 0;
		PreparedStatement pst = null;
		try {
			String query = "UPDATE SYS_CLNT_CCOSTO SET CC_BRNC_ORGN = ?, CC_DELETE_FLAG = ? WHERE CC_CLNT_ID = ? and CC_CCOSTO_ID = ?";
			pst =  con.prepareStatement(query);
			pst.setString(1, sucOmision);
			//pst.setString(2, "1"); //1 clientes que concide la sucursal asignada con la sucursal de cobertura
			//pst.setString(2, "2"); //2 clientes que concide el site asignado con el site de cobertura
			//pst.setString(2, "3"); //3 clientes diferente site asignado con el site de cobertura
//			pst.setString(2, "4"); //4 clientes direccion estructurada sin cobertura.
			//pst.setString(2, "5"); //4 clientes direccion NO estructurada.
			pst.setString(2, "8"); //6 clientes que concide la sucursal asignada con la sucursal de cobertura 20151016
			pst.setString(3, idCliente);
			pst.setString(4, centroCosto);
			
			reg = pst.executeUpdate();
			
			pst.close();
		} catch (Exception e) {
			System.out.println("updateSucOmisionClntCCosto()_Error: "+e);
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
