/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creación: 20/04/2012
 * Compañía: PAQUETEXPRESS.
 * Descripción del programa: Bean para obtener la sucursal configurada segun el tipo de entrega.
 * FileName: JavCatalogoClientes.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave:  AAP01
 * Autor: Abraham Daniel Arjona Peraza
 * Fecha:  22/11/2011
 * Descripción: Se le dió formato al codigo, se borraron algunos system.out de pruebas, se reacomodaron algunas concatenaciones 
 * ------------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción: 	
 * ------------------------------------------------------------------
 */
package bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import logger.AccessLog;

public class SucursalesConfiguradas {
	private final String msgError  = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private StringBuffer concatena = new StringBuffer();
	/************************************************************************************************************
	 * Metodo para obtener la configuracion de la sucursal para entregas ocurre
	 * y la sucursal para entregas EAD *
	 ************************************************************************************************************/
	// AAP02
	public String obtieneConfigSucursal(Connection con, String modulo,
			String entrega, String site) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sucursal = "";
		try {
			pst = con.prepareStatement("SELECT PM_PARM_CODE2 FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = ? AND PM_PARM_TYPE = ? AND PM_PARM_CODE1 = ?");
			pst.setString(1, modulo);
			pst.setString(2, entrega);
			pst.setString(3, site);
			rs = pst.executeQuery();

			if (rs.next()) {
				// sucursal obtenida
				if (rs.getString(1) != null) {
					sucursal = rs.getString(1);
				}
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgError).append("obtieneConfigSucursal()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("obtieneConfigSucursal()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return sucursal;
	}
	/************************************************************************************************************
	 * Metodo para obtener la configuracion de la sucursal para entregas ocurre
	 * y la sucursal para entregas EAD *
	 ************************************************************************************************************/
	// AAP02
	public String obtieneConfigSucursalOcurre(Connection con, String idDestClnt,
			String amAddrCode) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sucursal = "";
		try {
			//SELECT am_gety_code FROM sys_addr_mstr WHERE am_enty_id = ? AND AM_ADDR_CODE = ?
			pst = con.prepareStatement("SELECT AM_BRNC_DLY FROM SYS_ADDR_MSTR WHERE AM_ENTY_ID = ? AND AM_ADDR_CODE = ?");
			pst.setString(1, idDestClnt);
			pst.setString(2, amAddrCode);		
			
			rs = pst.executeQuery();

			if (rs.next()) {
				// sucursal obtenida
				if (rs.getString(1) != null) {
					sucursal = rs.getString(1);
				}
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgError).append("obtieneConfigSucursalOcurre()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("obtieneConfigSucursalOcurre()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return sucursal;
	}
	/************************************************************************************************************
	 * Metodo para obtener la configuracion de la sucursal especial para entregas ocurre
	 ************************************************************************************************************/
	// AAP02
	public String obtieneConfigSucursalEspec(Connection con, String modulo,
			String entrega, String site) {
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		String sucursal = "";
		try {
			pst = con.prepareStatement("SELECT PM_PARM_CODE2 FROM SYS_PARM_MSTR WHERE PM_MDUL_ID = ? AND PM_PARM_TYPE = ? AND PM_PARM_CODE1 = ?");
			pst.setString(1, modulo);
			pst.setString(2, entrega);
			pst.setString(3, site);
			rs = pst.executeQuery();

			if (rs.next()) {
				// sucursal obtenida
				if (rs.getString(1) != null) {
					sucursal = rs.getString(1);
				}
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgError).append("obtieneConfigSucursal()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("obtieneConfigSucursal()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return sucursal;
	}
}
