package bean;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import beanUtil.ConnectDB;

public class AcusesXT {
	CallableStatement cst = null;
	PreparedStatement pst = null;
	Connection con = null;
	/**
	 * Método que inserta los registros necesarios para la guia de retorno
	 * @param conexion Conexion abierta para la base de datos
	 * @param rastreo_original Número de rastreo original
	 * @return rastreo de retorno
	 */
	public String genAcuseXT(Connection conexion, String rastreo_original) {
		String retorno = "";
		try {
			//Ejecutmos el procedimiento que generará las guias de retorno
			String query_retorno = "{call PACK_ACUSES_XT.PRO_GEN_GUI_RET_XT(?,?,?)}";
			//System.out.println("RASTREO ORIGINAL: " + rastreo_original);
			cst = conexion.prepareCall(query_retorno);
			cst.setString(1, rastreo_original);
			cst.setString(2, "2");//2 por que es de tipo WW
			cst.registerOutParameter(3, Types.VARCHAR);
			cst.executeQuery();
			//Obtenemos la salida del Procedimiento
			retorno = cst.getString(3);
			//System.out.println("Rastreo de retorno: " + retorno);
		}catch(Exception ex) {
			System.out.println("clase: bean.AcuseXT Metodo: genAcuseXT(Connection conexion, String rastreo_original)\nError: " + ex.getMessage());
		}finally {
			try {
				if(cst!=null)
				cst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retorno;
	}
	
	/**
	 * Método que inserta los registros necesarios para la guia de retorno
	 * @param rastreo_original
	 * @return rastreo de retorno
	 */
	public String genAcuseXT(String rastreo_original) {
		String retorno = "";
		try {
			//Ejecutmos el procedimiento que generará las guias de retorno
			String query_retorno = "{call PACK_ACUSES_XT.PRO_GEN_GUI_RET_XT(?,?,?)}";
			//System.out.println("RASTREO ORIGINAL: " + rastreo_original);
			con = ConnectDB.getConnection();
			cst = con.prepareCall(query_retorno);
			cst.setString(1, rastreo_original);
			cst.setString(2, "2");//2 por que es de tipo WW
			cst.registerOutParameter(3, Types.VARCHAR);
			cst.executeQuery();
			//Obtenemos la salida del Procedimiento
			retorno = cst.getString(3);
			//System.out.println("Rastreo de retorno: " + retorno);
		}catch(Exception ex) {
			System.out.println("clase: bean.AcuseXT Metodo: genAcuseXT(Connection conexion, String rastreo_original)\nError: " + ex.getMessage());
		}finally {
			try {
				if(cst!=null)
				cst.close();
				if(con!=null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return retorno;
	}
	
	/**
	 * Método que desactiva la guia de retorno
	 * @param conexion
	 * @param rastreo_original
	 */
	public void desactivarGuiaDeRetorno(Connection conexion, String rastreo_original) {
		try {
			String desactiva = "{call PACK_ACUSES_XT.PRO_INAC_GUI_RET(?)}";
			pst = conexion.prepareStatement(desactiva);
			pst.setString(1, rastreo_original);
			pst.executeQuery();
		}catch(Exception ex) {
			System.out.println("clase: bean.AcuseXT Metodo: desactivarGuiaDeRetorno(Connection conexion, String rastreo_original)\nError: " + ex.getMessage());
		}finally {
			try {
				if(pst!=null)
				pst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Método que desactiva la guia de retorno
	 * @param rastreo_original
	 */
	public void desactivarGuiaDeRetorno(String rastreo_original) {
		try {
			String desactiva = "{call PACK_ACUSES_XT.PRO_INAC_GUI_RET(?)}";
			con = ConnectDB.getConnection();
			pst = con.prepareStatement(desactiva);
			pst.setString(1, rastreo_original);
			pst.executeQuery();
		}catch(Exception ex) {
			System.out.println("clase: bean.AcuseXT Metodo: desactivarGuiaDeRetorno(Connection conexion, String rastreo_original)\nError: " + ex.getMessage());
		}finally {
			try {
				if(pst!=null)
				pst.close();
				if(con!=null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Método que inserta los registros necesarios para la creacion del PDF de retorno
	 * @param conexion
	 * @param rastreo_retorno
	 */
	public void insertForPDFXT(Connection conexion, String rastreo_retorno) {
		try {
			String etiquetas_a_imprimir = "{call PACK_ACUSES_XT.PRO_ETIQ_PRN(?)}";
			cst = conexion.prepareCall(etiquetas_a_imprimir);
			cst.setString(1, rastreo_retorno);
			cst.execute();
		}catch(Exception ex) {
			System.out.println("clase: bean.AcuseXT Metodo: insertForPDFXT(Connection conexion, String rastreo_retorno)\nError: " + ex.getMessage());
		}finally {
			try {
				if(cst!=null)
				cst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Método que inserta los registros necesarios para la creacion del PDF de retorno
	 * @param rastreo_retorno
	 */
	public void insertForPDFXT(String rastreo_retorno) {
		try {
			String etiquetas_a_imprimir = "{call PACK_ACUSES_XT.PRO_ETIQ_PRN(?)}";
			con = ConnectDB.getConnection();
			cst = con.prepareCall(etiquetas_a_imprimir);
			cst.setString(1, rastreo_retorno);
			cst.execute();
		}catch(Exception ex) {
			System.out.println("clase: bean.AcuseXT Metodo: insertForPDFXT(String rastreo_retorno)\nError: " + ex.getMessage());
		}finally {
			try {
				if(cst!=null)
				cst.close();
				if(con!=null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
