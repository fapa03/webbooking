package beanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public class regeneraGL_SRVC_CHRG {

	/**
	 * @param args
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;	
				
		HashMap row = new HashMap(2);
		ArrayList result = new ArrayList();
		ArrayList result2 = new ArrayList();
		ArrayList mainList = new ArrayList();
		try {
			String rastreo = "";
			String GS_SRVC_ID = "";
			String GS_SUB_TOTL = "";
			String GL_SRVC_ID = "";
			String GL_REFR_SRVC_ID = "";
			String GL_SRVC_CHRG = "";
			String GL_DESC = "";
			String GL_SLAB_NO = "";
			
			String rowId = "";
			String rastreo_bk = "";
			String GL_SRVC_CHRG_bk = "";
			
			con = ConnectDB.getConnection();
			
			String query = "SELECT D_RASTREO FROM EXT_RESPALDO GROUP BY D_RASTREO";
			
			pst =  con.prepareStatement(query);
			rs =  pst.executeQuery();
			while (rs.next()) {
				rastreo_bk =  rs.getString(1) == null ? "" : rs.getString(1);				
				mainList.add(rastreo_bk);			
			}
			
			rs.close();
			pst.close();
			System.out.println("mainList "+mainList);
			for (int i=0;i<mainList.size();i++){
				query = "SELECT rownum, D_RASTREO, d_total FROM EXT_RESPALDO WHERE D_RASTREO = ?";
				
				pst =  con.prepareStatement(query);
				pst.setString(1, mainList.get(i).toString());
				rs =  pst.executeQuery();
				while (rs.next()) {
					rowId =  rs.getString(1) == null ? "" : rs.getString(1);
					rastreo_bk =  rs.getString(2) == null ? "" : rs.getString(2);
					GL_SRVC_CHRG_bk =  rs.getString(3) == null ? "" : rs.getString(3);					
					
					row.put("rowId", rowId);
					row.put("rastreo_bk", rastreo_bk);
					row.put("GL_SRVC_CHRG_bk", GL_SRVC_CHRG_bk);
					result.add(row.clone());
					row.clear();
				}
				rs.close();
				pst.close();
				
				
				query = 			
				"SELECT rownum, GL_GUIA_NO, GL_SRVC_ID, GL_REFR_SRVC_ID, GL_SRVC_CHRG, GL_DESC, GL_SLAB_NO FROM BOK_GUIA_SRVC_ITEM WHERE GL_GUIA_NO = ? and GL_REFR_SRVC_ID= 'SHP-G'";
				
				//System.out.println("query "+query);
				pst =  con.prepareStatement(query);
				//pst.setString(1, "WW");
				pst.setString(1, mainList.get(i).toString());
				
				rs =  pst.executeQuery();
				
				
				while (rs.next()) {
					rowId =  rs.getString(1) == null ? "" : rs.getString(1);
					rastreo =  rs.getString(2) == null ? "" : rs.getString(2);
					GL_SRVC_ID =  rs.getString(3) == null ? "" : rs.getString(3);
					GL_REFR_SRVC_ID =  rs.getString(4) == null ? "" : rs.getString(4);
					GL_SRVC_CHRG  =  rs.getString(5) == null ? "" : rs.getString(5);
					GL_DESC =  rs.getString(6) == null ? "" : rs.getString(6);
					GL_SLAB_NO =  rs.getString(7) == null ? "" : rs.getString(7);
					/*GL_DESC =  rs.getString(8) == null ? "" : rs.getString(8);
					GL_SLAB_NO =  rs.getString(9) == null ? "" : rs.getString(9);*/
					
					/*System.out.println(
							"rowId: "+rowId +
							"\nrastreo: "+rastreo +
							"\nGL_SRVC_ID: "+GL_SRVC_ID +
							"\nGL_REFR_SRVC_ID: "+GL_REFR_SRVC_ID +
							"\nGL_SRVC_CHRG: "+GL_SRVC_CHRG +
							"\nGL_REFR_SRVC_ID: "+GL_REFR_SRVC_ID +
							"\nGL_SRVC_CHRG: "+GL_SRVC_CHRG+
							"\nGL_DESC: "+GL_DESC+
							"\nGL_SLAB_NO: "+GL_SLAB_NO
											
							);*/
					
					row.put("rowId", rowId);
					row.put("rastreo", rastreo);
					row.put("GL_SRVC_ID", GL_SRVC_ID);
					row.put("GL_REFR_SRVC_ID", GL_REFR_SRVC_ID);
					row.put("GL_SRVC_CHRG", GL_SRVC_CHRG);
					row.put("GL_SRVC_ID", GL_SRVC_ID);
					row.put("GL_DESC", GL_DESC);
					row.put("GL_SLAB_NO", GL_SLAB_NO);
					
					result2.add(row.clone());
					row.clear();
				}
				
				rs.close();
				pst.close();
			/*rs.close();
			pst.close();*/
			//con.commit();
			//con.rollback();
				
				System.out.println("result "+result);
				System.out.println("result2 "+result2);
				
				for(int j = 0; j<result2.size();j++) {
								
					rowId = ((HashMap)result.get(j)).get("rowId").toString();
					rastreo = ((HashMap)result.get(j)).get("rastreo_bk").toString();					
					//GS_SRVC_ID = ((HashMap)result2.get(j)).get("GS_SRVC_ID").toString();
					GL_REFR_SRVC_ID = ((HashMap)result2.get(j)).get("GL_REFR_SRVC_ID").toString();
					GL_SRVC_CHRG_bk = ((HashMap)result.get(j)).get("GL_SRVC_CHRG_bk").toString();
					GL_DESC = ((HashMap)result2.get(j)).get("GL_DESC").toString();
					GL_SLAB_NO = ((HashMap)result2.get(j)).get("GL_SLAB_NO").toString();
					
					
					System.out.println(
							"rastreo: "+rastreo +
							"\nrowId: "+rowId +
							"\nGL_SRVC_CHRG_bk: "+GL_SRVC_CHRG_bk
							);	
					
					regenerateGuiasSrvcChrg(con, rastreo, GS_SRVC_ID, GL_SRVC_CHRG_bk, GL_REFR_SRVC_ID, GL_DESC, GL_SLAB_NO);
				}
				result.clear();
				result2.clear();
			}
			System.out.println("mainList.size() "+mainList.size());
			
//			//String query = "SELECT WC_CLNT_ID, WC_PSWD, WC_APRV_BRNC FROM WEB_CLNT_MSTR WHERE WC_CLNT_ID='18025'";
//			query = 			
//			"SELECT GS_GUIA_NO, GS_SRVC_ID, GS_SUB_TOTL, GL_SRVC_ID, GL_REFR_SRVC_ID, GL_SRVC_CHRG, GL_DESC, GL_SLAB_NO FROM BOK_GUIA_SRVC, BOK_GUIA_SRVC_ITEM WHERE GS_GUIA_NO=GL_GUIA_NO and GL_GUIA_NO IN ("
//					+" select GH_GUIA_NO from bok_guia_head where "
//					+" SUBSTR (GH_FORM_NO, 1,2) in (?) "
//					+" AND TRUNC(GH_ISSE_DATE)>=TRUNC(SYSDATE)-2) "
//					+" AND GS_SRVC_ID = GL_REFR_SRVC_ID "
//					+" and GS_SRVC_ID = 'SHP-G' "
//					//+" AND GS_SUB_TOTL> GL_SRVC_CHRG ";
//					+" AND GS_GUIA_NO = ? ";			
//			System.out.println("query "+query);
//			pst =  con.prepareStatement(query);
//			pst.setString(1, "WW");
//			pst.setString(2, "19134972014");
//			
//			rs =  pst.executeQuery();			
//			
//			while (rs.next()) {
//				rastreo =  rs.getString(1) == null ? "" : rs.getString(1);
//				GS_SRVC_ID =  rs.getString(2) == null ? "" : rs.getString(2);
//				GS_SUB_TOTL =  rs.getString(3) == null ? "" : rs.getString(3);
//				GL_SRVC_ID  =  rs.getString(4) == null ? "" : rs.getString(4);
//				GL_REFR_SRVC_ID =  rs.getString(5) == null ? "" : rs.getString(5);
//				GL_SRVC_CHRG =  rs.getString(6) == null ? "" : rs.getString(6);
//				GL_DESC =  rs.getString(7) == null ? "" : rs.getString(7);
//				GL_SLAB_NO =  rs.getString(8) == null ? "" : rs.getString(8);
//				
//				System.out.println(
//						"rastreo: "+rastreo +
//						"\nGS_SRVC_ID: "+GS_SRVC_ID +
//						"\nGS_SUB_TOTL: "+GS_SUB_TOTL +
//						"\nGL_SRVC_ID: "+GL_SRVC_ID +
//						"\nGL_REFR_SRVC_ID: "+GL_REFR_SRVC_ID +
//						"\nGL_SRVC_CHRG: "+GL_SRVC_CHRG+
//						"\nGL_DESC: "+GL_DESC+
//						"\nGL_SLAB_NO: "+GL_SLAB_NO
//						);				
//				regenerateGuiasSrvcChrg(con, rastreo, GS_SRVC_ID, GL_SRVC_CHRG_bk, GL_REFR_SRVC_ID, GL_DESC, GL_SLAB_NO);
//			}
			/*rs.close();
			pst.close();*/
			con.commit();
			//con.rollback();
			//con.close();
		} catch (Exception e) {
			System.out.println("main()_Error1: "+e);
			e.printStackTrace();
			try {
				con.rollback();
			} catch (Exception e2) {
				System.out.println("main()_Error2: "+e2);
				e2.printStackTrace();
			}
			
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pst != null)
					pst.close();
				if (con!= null)
					con.close();
			} catch (Exception e2) {
				System.out.println("main()_Error3: "+e2);
				e2.printStackTrace();
			}
			
		}
	}	
	
	private static int regenerateGuiasSrvcChrg(Connection con, String rastreo, String GS_SRVC_ID, String GS_SUB_TOTL, String GL_REFR_SRVC_ID, String GL_DESC, String GL_SLAB_NO) {
		int reg = 0;
		PreparedStatement pst = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		try {
			String query = 
					"UPDATE BOK_GUIA_SRVC_ITEM "+
					"SET GL_SRVC_CHRG = ? "+
					"WHERE GL_GUIA_NO = ? "+
					"AND GL_REFR_SRVC_ID = ?" +
					"AND GL_DESC = ?" +
					"AND GL_SLAB_NO = ?";
			
			System.out.println(
					"actualiza\n GS_SUB_TOTL: "+GS_SUB_TOTL+
					"\n rastreo: "+rastreo+
					"\n GS_SRVC_ID: "+GS_SRVC_ID+
					"\n GL_REFR_SRVC_ID: "+GL_REFR_SRVC_ID +
					"\n GL_DESC: "+GL_DESC +
					"\n GL_SLAB_NO: "+GL_SLAB_NO
					);
			

			pst2 = con.prepareStatement(query);
			
			pst2.setString(1, GS_SUB_TOTL);
			pst2.setString(2, rastreo);
			
			//pst2.setString(3, rowId);
			
			pst2.setString(3, GL_REFR_SRVC_ID);
			pst2.setString(4, GL_DESC);
			pst2.setString(5, GL_SLAB_NO);
		
			reg = pst2.executeUpdate();
			
			pst2.close();
			System.out.println("numero de actualizaciones "+reg);
			
		} catch (Exception e) {
			System.out.println("regenerateGuiasSrvcChrg()_Error: "+e);
			e.printStackTrace();
			try {
				con.rollback();				
			} catch (Exception e2) {
				// TODO: handle exception
			}
			
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
