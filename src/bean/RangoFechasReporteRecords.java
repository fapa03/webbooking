/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creación: 18/10/2011
 * Compañía: PAQUETEXPRESS.
 * Descripción del programa: Bean de datos de consulta para generar archivo
 * de servicios facturados.
 * FileName: RangoFechasReporteRecords.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción:  
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
import java.util.ArrayList;

import logger.AccessLog;

public class RangoFechasReporteRecords {
	StringBuffer concat = new StringBuffer();

	public String fetchFactRecords(Connection con,
										String clientId,
										String factura,
										String sucursalFact) {
		PreparedStatement psmt 	= null;
		ResultSet rs 			= null;
		String result 			= "";
		
		try {
			String registro		= null;
			
			String query =	concat.delete(0, concat.length()).append("SELECT TO_CHAR(GH_ISSE_DATE,'DD/MM/YYYY'), ")
					.append("GH_ORGN_BRNC_ID||GH_FORM_NO, ")
					.append("GH_GUIA_NO, ")
					.append("(SELECT SUM(GS_SUB_TOTL) FROM BOK_GUIA_SRVC WHERE GS_GUIA_NO=BOK_GUIA_HEAD.GH_GUIA_NO AND GS_SRVC_ID IN ('SHP-G','SHP-E')), ")
					.append("(SELECT SUM(GS_SUB_TOTL) FROM BOK_GUIA_SRVC WHERE GS_GUIA_NO=BOK_GUIA_HEAD.GH_GUIA_NO AND GS_SRVC_ID NOT IN ('INV','SHP-G','SHP-E')), ")
					.append("(SELECT SUM(GS_SUB_TOTL) FROM BOK_GUIA_SRVC WHERE GS_GUIA_NO=BOK_GUIA_HEAD.GH_GUIA_NO AND GS_SRVC_ID IN ('INV')), ")
					.append("(SELECT SUM(GS_SUB_TOTL) FROM BOK_GUIA_SRVC WHERE GS_GUIA_NO=BOK_GUIA_HEAD.GH_GUIA_NO), ")
					.append("(SELECT SUM(GS_TAX) FROM BOK_GUIA_SRVC WHERE GS_GUIA_NO=BOK_GUIA_HEAD.GH_GUIA_NO), ")
					.append("(SELECT SUM(GS_TAX_RET) FROM BOK_GUIA_SRVC WHERE GS_GUIA_NO=BOK_GUIA_HEAD.GH_GUIA_NO), ")
					.append("(SELECT SUM(GS_SUB_TOTL)-SUM(GS_TAX_RET) FROM BOK_GUIA_SRVC WHERE GS_GUIA_NO=BOK_GUIA_HEAD.GH_GUIA_NO), ")
					.append("GH_GUIA_AMNT, ")
					.append("GH_NUMB_PACK, ")
					.append("(SELECT SM_SITE_NAME FROM SYS_SITE_MSTR WHERE SM_SITE_ID=SUBSTR(BOK_GUIA_HEAD.GH_DEST_BRNC_ID,1,3)), ")
					.append("GH_COMT, ")
					.append("CASE GH_TOTL_DECL_VLUE WHEN 1000 THEN 0 ELSE GH_TOTL_DECL_VLUE END, ")
					.append("(SELECT SM_CC_NAME FROM WEB_SITE_MAP_CC WHERE SM_SITE_ID=SUBSTR(BOK_GUIA_HEAD.GH_DEST_BRNC_ID,1,3)), ")
					.append("(SELECT SM_CC_ID FROM WEB_SITE_MAP_CC WHERE SM_SITE_ID=SUBSTR(BOK_GUIA_HEAD.GH_DEST_BRNC_ID,1,3))")
					.append(" FROM BOK_GUIA_HEAD ")
					.append("WHERE GH_GUIA_REFR_NO IN (")
					.append("SELECT MD_MNFT_NO FROM WEB_MNFT_DETL WHERE MD_CLNT_ID = ? AND MD_INVC_NO = ? AND SUBSTR(MD_BRNC_ID, 1, 3) = SUBSTR(?, 1, 3)")
					.append(") AND GH_GUIA_TYPE = ?").toString();						   
			
			psmt = con.prepareStatement(query);

			psmt.setString(1,clientId);
			psmt.setString(2,factura);
			psmt.setString(3,sucursalFact);
			psmt.setString(4,"N");
			
			//System.out.println("query "+query);
			//System.out.println("clientId "+clientId);
			//System.out.println("factura "+factura);
			//System.out.println("sucursalFact "+sucursalFact);

			rs = psmt.executeQuery();
			
			while (rs.next()) {
				registro = 
						concat.delete(0, concat.length())
						.append(sucursalFact.substring(0,3)).append(factura).append("|")//factura
						.append(rs.getString(1) ).append("|")//fecha
						.append(rs.getString(2)).append("|")//guia
						.append(rs.getString(3)).append("|")//Rastreo
						.append(rs.getString(4)).append("|")//flete
						.append(rs.getString(5)).append("|")//Servicios
						.append(rs.getString(6)).append("|")//Seguros
						.append(rs.getString(7)).append("|")//Subtotal
						.append(rs.getString(8)).append("|")//iva
						.append(rs.getString(9)).append("|")//iva retenido
						.append(rs.getString(10)).append("|")//costo de felete
						.append(rs.getString(11)).append("|")//costo de guia
						.append(rs.getString(12)).append("|")//paquetes
						.append(rs.getString(13)).append("|")//destino
						.append(rs.getString(14)).append("|")//factura cliente
						.append(rs.getString(15)).append("|")//costo total factura
						.append(rs.getString(16)).append("|")//centro de costo
						.append(rs.getString(17))			 //id centro de costo
						.append("\n").toString() ;           //salto de linea
				
				
				/*a resultado se le concatena la factura.*/
				//registro = concat.delete(0, concat.length()).append(sucursalFact.substring(0,2)).append(factura).append("|").append(registro).append("\n").toString();
				//System.out.println("registro "+registro);
				//agrega todo al string final de los resultados de la factura
				result = concat.delete(0, concat.length()).append(result).append(registro).toString();
			}
		} catch (Exception e) {
			AccessLog.Log("fetchFactRecords() Error_1:"+e);
		} finally {
			try {
				if(psmt!=null)
					psmt.close();		
				if(rs!=null)
					rs.close();
			} catch (Exception e2) {
				AccessLog.Log("fetchFactRecords() Error_2:"+e2);
			}
		}		
		return result;
	}
	
	public ArrayList fetchMasterRecords(Connection con,
										String clientId,
										String fromDate,
										String toDate) {
		PreparedStatement psmt 	= null;
		ResultSet rs 			= null;
		ArrayList values		= null;
		ArrayList result		= new ArrayList();
		try {			
			String prpBranch	= null;
			String formNo		= null;
			String regFactura	= null;
			
			String query = "SELECT IH_PREP_BRNC_ID, IH_FORM_NO FROM FIN_INVC_HEAD WHERE IH_BILL_CLNT_ID = ? AND trunc(IH_ISSE_DATE) between TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY') AND IH_ACTV_FLAG = ? AND IH_INVC_ORGN = ?";
			psmt = con.prepareStatement(query);
			psmt.setString(1,clientId);
			psmt.setString(2,fromDate);
			psmt.setString(3,toDate);
			psmt.setString(4,"A");
			psmt.setString(5,"8");
			
			//System.out.println("query "+ query);
			//System.out.println("clientId "+ clientId);
			//System.out.println("fromDate "+ fromDate);
			//System.out.println("toDate "+ toDate);
			
			rs = psmt.executeQuery();
			
			values = new ArrayList(1);
			while(rs.next()){
				prpBranch	= rs.getString(1)== null?"":rs.getString(1);
				formNo		= rs.getString(2)== null?"":rs.getString(2);
				//System.out.println("prpBranch.....: "+prpBranch);
				//System.out.println("formNo........: "+formNo);
				regFactura = fetchFactRecords(con,clientId,formNo,prpBranch);
				//System.out.println("regFactura........: "+regFactura);
				values.add(regFactura);
				result.add(values.clone());
				values.clear();
			}			
		} catch (Exception e) {
			AccessLog.Log("fetchMasterRecords() Error_1:"+e);
		} finally {
			try {
				if(psmt!=null)
					psmt.close();
				if(rs!=null)
					rs.close();	
			} catch (Exception e2) {
				AccessLog.Log("fetchMasterRecords() Error_2:"+e2);
			}
		}		
		return result;
	}
	
	public ArrayList fetchRecords(Connection con, String clientId, String fromDate, String toDate) {
		ArrayList factDetail = null;
		try {
			
			factDetail = fetchMasterRecords(con,clientId,fromDate,toDate);										
			
		} catch (Exception e) {
			AccessLog.Log("fetchRecords() Error:"+e);
		}		
		return factDetail;
	}
}