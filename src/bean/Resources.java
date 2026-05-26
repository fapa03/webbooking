package bean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import logger.AccessLog;

public class Resources {
	private StringBuffer concatena = new StringBuffer();
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	
	public void cerrarConexion(Connection con){
		try {
			if (con != null){
				con.close();
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("cerrarConexion()_Error:").append(e).toString());
			e.printStackTrace();
			con = null;
		}
	}
	
	public void cerrarResultSet(ResultSet rs){
		try {
			if(rs!=null) {
				rs.close();
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("cerrarResultSet()_Error:").append(e).toString());
			e.printStackTrace();
			rs = null;
		}
	}
	
	public void cerrarPreparedStatement(PreparedStatement pst){
		try {
			if(pst!=null) {
				pst.close();
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("cerrarPreparedStatement()_Error:").append(e).toString());
			e.printStackTrace();
			pst = null;
		}
	}
	
	public void cerrarCallableStatement(CallableStatement cst){
		try {
			if(cst!=null) {
				cst.close();
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("cerrarPreparedStatement()_Error:").append(e).toString());
			e.printStackTrace();
			cst = null;
		}
	}
	
	public void closeResources(ResultSet rs, PreparedStatement pst){
		try {
			cerrarResultSet(rs);
			cerrarPreparedStatement(pst);
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("closeResources()_Error1:").append(e).toString());
			e.printStackTrace();
		}		
	}
	public void closeResources(ResultSet rs, PreparedStatement pst, CallableStatement cst){		
		try {
			cerrarResultSet(rs);
			cerrarPreparedStatement(pst);
			cerrarCallableStatement(cst);
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgErr).append("closeResources()_Error2:").append(e).toString());
			e.printStackTrace();
		}		
	}	
}
