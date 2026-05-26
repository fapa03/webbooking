/**
 * @author: Carlos Misael Muñoz Elenes
 * Fecha de Creación: 20/09/2021
 * Compañía: PAQUETEXPRESS.
 * Descripción del programa: Bean con métodos para catalogo de productos
 * FileName: JavCatProductos.java
 * SessionVariables:
 * Other Used Classes:
 * Function Names:
 */
package bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import beanUtil.CargaInicialProductsSAT;
import logger.AccessLog;
import mx.com.paquetexpress.general.model.dto.SysSspDesMstrDTO;

public class JavCatProducts {
	private final String msgError = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".")
			.toString();
	private StringBuffer concatena = new StringBuffer();
	private Resources resources = new Resources();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getLovRecords(Connection con, String clntId, String shipContent) {
		ArrayList result = new ArrayList();
		ArrayList tmp = new ArrayList();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String restricted = "";
		StringBuffer query = new StringBuffer();
		String productDes = "";
		try {
			if (clntId != null && !clntId.isEmpty()) {
				/* Se obtienen productos configurados del cliente */
				query = query.delete(0, query.length())
						.append("SELECT NVL(CP_RESTRICTED, 'N') FROM BOK_SCAT_CLNT_SAT_PRO_SRVC WHERE CP_CLNT_ID = ?");
				pst = con.prepareStatement(query.toString());
				pst.setString(1, clntId);
				rs = pst.executeQuery();
				while (rs.next()) {
					restricted = rs.getString(1);
				}
				pst.close();
				rs.close();
				/* Se agrega a la lista los productos configurados del cliente */
				query = query.delete(0, query.length())
						.append("SELECT PRODUCT_ID, DESCRIPTION, SIMILAR, SIMILAR_PQTX FROM BOK_CAT_PRODUCTS WHERE PRODUCT_ID IN (")
						.append("Select CP_PRODUCT_ID FROM BOK_SCAT_CLNT_SAT_PRO_SRVC_DTL WHERE CP_CLNT_ID = ?)"
								+ " AND ACTV_FLAG = 'Y' ")
						.append("ORDER BY PRODUCT_ID DESC, DESCRIPTION DESC, SIMILAR DESC, SIMILAR_PQTX DESC");
				pst = con.prepareStatement(query.toString());
				pst.setString(1, clntId);
				rs = pst.executeQuery();
				result = new ArrayList();
				while (rs.next()) {
					result.add(new SysSspDesMstrDTO(rs.getString("PRODUCT_ID"), rs.getString("DESCRIPTION"), rs.getString("SIMILAR"), rs.getString("SIMILAR_PQTX")));
				}
				pst.close();
				rs.close();
				shipContent = shipContent.toUpperCase();
				if (!restricted.equalsIgnoreCase("Y") && !shipContent.equalsIgnoreCase("FxcValidation")) {
					query = query.delete(0, query.length())
							.append("  Select * from (SELECT PRODUCT_ID, ")
							.append("         DESCRIPTION, ")
							.append("         SIMILAR, ")
							.append("         SIMILAR_PQTX, ")
							.append("         ACTV_FLAG ")
							.append("    FROM BOK_CAT_PRODUCTS ")
							.append("   WHERE	PRODUCT_ID LIKE '%' || :FIND_PRODUCT_SAT || '%' ")
							.append("         OR DESCRIPTION LIKE '%' || :FIND_PRODUCT_SAT || '%' ")
							.append("         OR SIMILAR LIKE '%' || :FIND_PRODUCT_SAT || '%' ")
							.append("         OR SIMILAR_PQTX LIKE '%' || :FIND_PRODUCT_SAT || '%' ")
							.append("            AND CASE ")
							.append("                   WHEN NVL ( (SELECT CP_RESTRICTED ")
							.append("                                 FROM BOK_SCAT_CLNT_SAT_PRO_SRVC ")
							.append("                                WHERE CP_CLNT_ID = :clientId AND ROWNUM = 1), ")
							.append("                             'N') = 'Y' ")
							.append("                        AND EXISTS ")
							.append("                               (SELECT 1 ")
							.append("                                  FROM BOK_SCAT_CLNT_SAT_PRO_SRVC_DTL ")
							.append("                                 WHERE CP_CLNT_ID = :clientId ")
							.append("                                       AND CP_PRODUCT_ID = PRODUCT_ID) ")
							.append("                   THEN ")
							.append("                      1 ")
							.append("                   WHEN NVL ( (SELECT CP_RESTRICTED ")
							.append("                                 FROM BOK_SCAT_CLNT_SAT_PRO_SRVC ")
							.append("                                WHERE CP_CLNT_ID = :clientId AND ROWNUM = 1), ")
							.append("                             'N') = 'N' ")
							.append("                   THEN ")
							.append("                      1 ")
							.append("                   ELSE ")
							.append("                      0 ")
							.append("                END = 1 ")
							.append("ORDER BY NVL ( ")
							.append("            (SELECT 1 ")
							.append("               FROM BOK_SCAT_CLNT_SAT_PRO_SRVC_DTL ")
							.append("              WHERE CP_CLNT_ID = :clientId AND CP_PRODUCT_ID = PRODUCT_ID), ")
							.append("            0) DESC, ")
							.append("         utl_match.jaro_winkler_similarity (DESCRIPTION, :contentDesc) DESC )")
							.append(" tablaProd where ACTV_FLAG = 'Y'");

					pst = con.prepareStatement(query.toString());
					if (shipContent.contains("-") && shipContent.length() > 1){
						//String[] parts = shipContent.trim().split("\\- ");
						//productDes = parts[1].substring(1, parts[1].length());
						productDes = shipContent.trim().substring(shipContent.indexOf("-")+1).trim();
					}else {
						//productId = "";
						productDes = shipContent;
					}
					
					pst.setString(1, productDes);
					pst.setString(2, productDes);
					pst.setString(3, productDes);
					pst.setString(4, productDes);
					pst.setString(5, clntId);
					pst.setString(6, clntId);
					pst.setString(7, clntId);
					pst.setString(8, clntId);
					pst.setString(9, productDes);
					
					rs = pst.executeQuery();
					result = new ArrayList();
					while (rs.next()) {
						result.add(new SysSspDesMstrDTO(rs.getString("PRODUCT_ID"), rs.getString("DESCRIPTION"), rs.getString("SIMILAR"), rs.getString("SIMILAR_PQTX")));
					}
					pst.close();
					rs.close();
				}
				
			} else {
				result.clear();
				result.addAll(CargaInicialProductsSAT.getInstance());
			}
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length()).append(msgError).append("getLovRecords()Error_:")
					.append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		if (result.contains(tmp)) {
			result.removeAll(tmp);
		}
		result.addAll(tmp);
		if (result.size() != tmp.size()) {
			result.removeAll(tmp);
		}
		return result;
	}
	
	public SysSspDesMstrDTO getProductById(Connection con, String prodId) {
		StringBuffer query = new StringBuffer();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			query = query.delete(0, query.length())
					.append("SELECT PRODUCT_ID, DESCRIPTION, SIMILAR, SIMILAR_PQTX ")
					.append(" FROM BOK_CAT_PRODUCTS WHERE PRODUCT_ID = :prodId");
			pst = con.prepareStatement(query.toString());
			pst.setString(1, prodId);
			rs = pst.executeQuery();
			while (rs.next()) {
				return new SysSspDesMstrDTO(rs.getString("PRODUCT_ID"), rs.getString("DESCRIPTION"), rs.getString("SIMILAR"), rs.getString("SIMILAR_PQTX"));
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return new SysSspDesMstrDTO();
	}
}
