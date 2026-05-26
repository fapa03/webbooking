package mx.com.paquetexpress.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beanUtil.ConnectDB;
import logger.AccessLog;
import mx.com.paquetexpress.dto.SiteChangeDTO;

public class SiteChangeDAO {
	ResultSet rs = null;
	PreparedStatement stmt = null;
	Connection con = null;
	private StringBuffer cnct = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	
	/**
	 * Método que obtiene una lista de las plazas a las que tiene acceso un usuario
	 * @param username Nombre de usuario
	 * @return
	 */
	public ArrayList<SiteChangeDTO> getPlazasAsignadas(String username){
		ArrayList<SiteChangeDTO> lista = new ArrayList<>();
		SiteChangeDTO dto = null;
//		StringBuilder query = new StringBuilder("SELECT DISTINCT a.WC_CLNT_ID clienteConvenio, a.WC_MBR_ID clienteMiembro, ");
//		query.append("pack_web.fun_ftch_clnt_name(WC_MBR_ID) miembroNombre, ");
//		query.append("a.WC_SITE_ID sitio, Pack_Web.fun_ftch_site_name(WC_SITE_ID) siteName, D.AM_STRT_NAME || ' ' || D.AM_DRNR || ', ' || ");
//		query.append("(SELECT 'COLONIA: ' || COLO_DES || ', ' || CIUDAD || ', ' || ESTADO FROM dw_cobertura_view ");
//		query.append("WHERE col_level = D.AM_GETY_LEVL AND COL_TYPE = D.AM_GETY_TYPE AND colo_id = D.AM_GETY_CODE) direccion, ");
//		query.append("DECODE(NVL(CC_BRNC_ORGN,''),'',(SELECT COLO_SUCURSAL FROM dw_cobertura_view WHERE col_level = D.AM_GETY_LEVL AND COL_TYPE = D.AM_GETY_TYPE AND colo_id = D.AM_GETY_CODE),CC_BRNC_ORGN) sucursal, ");
//		query.append("PACK_WEB.Fun_ftch_brnc_name(DECODE(NVL(CC_BRNC_ORGN,''),'',(SELECT COLO_SUCURSAL FROM dw_cobertura_view WHERE col_level = D.AM_GETY_LEVL AND COL_TYPE = D.AM_GETY_TYPE AND colo_id = D.AM_GETY_CODE),CC_BRNC_ORGN) ) sucursalNombre ");
//		query.append("FROM WEB_CLNT_MBR a, SYS_CLNT_CCOSTO_USER b, SYS_CLNT_CCOSTO c, SYS_ADDR_MSTR d ");
//		query.append("WHERE A.WC_MBR_ID = B.CU_CLNT_ID AND B.CU_USER_ID = ? AND B.CU_DFLT_FLAG = 'Y' ");
//		query.append("AND B.CU_CCOSTO_ID = C.CC_CCOSTO_ID and C.CC_CLNT_ID = B.CU_CLNT_ID AND D.AM_ADDR_CODE = C.CC_ADDR_CODE ");
//		query.append("ORDER BY Pack_Web.fun_ftch_site_name(WC_SITE_ID) ASC");
		
		StringBuilder query = new StringBuilder("SELECT DISTINCT a.WC_CLNT_ID clienteConvenio, a.WC_MBR_ID clienteMiembro, ");
		query.append("pack_web.fun_ftch_clnt_name(WC_MBR_ID) miembroNombre, ");
		query.append("a.WC_SITE_ID sitio, Pack_Web.fun_ftch_site_name(WC_SITE_ID) siteName, D.AM_STRT_NAME || ' ' || D.AM_DRNR || ', ' || ");
		query.append("(SELECT 'COLONIA: ' || COL_DES || ', ' || CIUDAD || ', ' || ESTADO FROM PCOBERTURA_VIEW ");
		query.append("WHERE COL_LEVL = D.AM_GETY_LEVL AND COL_TYPE = D.AM_GETY_TYPE AND COD_COLO = D.AM_GETY_CODE) direccion, ");
		query.append("DECODE(NVL(CC_BRNC_ORGN,''),'',(SELECT SUCURSAL FROM PCOBERTURA_VIEW WHERE COL_LEVL = D.AM_GETY_LEVL AND COL_TYPE = D.AM_GETY_TYPE AND COD_COLO = D.AM_GETY_CODE AND SUCURSAL IS NOT NULL),CC_BRNC_ORGN) sucursal, ");
		query.append("PACK_WEB.Fun_ftch_brnc_name(DECODE(NVL(CC_BRNC_ORGN,''),'',(SELECT SUCURSAL FROM PCOBERTURA_VIEW WHERE COL_LEVL = D.AM_GETY_LEVL AND COL_TYPE = D.AM_GETY_TYPE AND COD_COLO = D.AM_GETY_CODE AND SUCURSAL IS NOT NULL),CC_BRNC_ORGN) ) sucursalNombre ");
		query.append("FROM WEB_CLNT_MBR a, SYS_CLNT_CCOSTO_USER b, SYS_CLNT_CCOSTO c, SYS_ADDR_MSTR d ");
		query.append("WHERE A.WC_MBR_ID = B.CU_CLNT_ID AND B.CU_USER_ID = ? AND B.CU_DFLT_FLAG = 'Y' ");
		query.append("AND B.CU_CCOSTO_ID = C.CC_CCOSTO_ID and C.CC_CLNT_ID = B.CU_CLNT_ID AND D.AM_ADDR_CODE = C.CC_ADDR_CODE ");
		query.append("ORDER BY Pack_Web.fun_ftch_site_name(WC_SITE_ID) ASC");
		try{
			con = ConnectDB.getConnection();
			stmt = con.prepareStatement(query.toString());
			stmt.setString(1, username);
			rs = stmt.executeQuery();
			while(rs.next()){
				dto = new SiteChangeDTO();
				dto.setClienteConvenio(rs.getString("clienteConvenio"));
				dto.setClienteMiembro(rs.getString("clienteMiembro"));
				dto.setMiembroNombre(rs.getString("miembroNombre"));
				dto.setSite(rs.getString("sitio"));
				dto.setSiteName(rs.getString("siteName"));
				dto.setDireccion(rs.getString("direccion"));
				dto.setSucursal(rs.getString("sucursal"));
				dto.setSucursalNombre(rs.getString("sucursalNombre"));
				lista.add(dto);
			}
		}catch(Exception e){
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getPlazasAsignadas()_Error:").append(e).toString());
			e.printStackTrace();
		}finally{
			try{
				if(rs!=null)
					rs.close();
				if(stmt!=null)
					stmt.close();
				if(con!=null)
					con.close();
			}catch(SQLException e){
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgErr).append("getPlazasAsignadas()_Error:").append(e).toString());
				e.printStackTrace();
			}
		}
		return lista;
	}
}
