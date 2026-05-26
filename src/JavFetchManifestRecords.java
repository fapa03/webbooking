
/**
 * @author: KUMARAN
 * Fecha de Creación: 
 * Compañía: KUMARAN.
 * Descripción del programa: Bean de consultas para aplicacion de manifiestos
 * SessionVariables:
 * Other Used Classes:
 * Function Names: 
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 04/07/2013
 * Descripción:  Se agregó logica para validar guias FXC pendientes de generar manifiesto.
 * se agregó metodo consultaGuiasSinMnft() para realizar consulta.
 * ------------------------------------------------------------------
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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;
import bean.Global;
import bean.JavManifestNotSent;
import bean.JavManifestSent;


public class JavFetchManifestRecords{
	public JavFetchManifestRecords(){}
	private StringBuffer cnct = new StringBuffer();	
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	
	public String getSystemDate(Connection con) {
		String query = "Select to_char(sysdate,'DD/MM/YYYY HH24:Mi:SS') as datetime from dual";
		String value = "";
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = con.prepareStatement(query);
			rs = psmt.executeQuery();
			while(rs.next()){
				value = rs.getString("datetime");
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getSystemDate()_Error2:").append(e).toString());
			e.printStackTrace();
		} finally {
			try{
				if(rs!=null)
					rs.close();
				if(psmt!=null) {
					psmt.close();
				}
				
			} catch (Exception e){
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDestinationBranchName()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return value;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList fetchManifestSent(Connection con,JavShipmentHistoryForm form,String clientId,String branchId)throws Exception{
		PreparedStatement psmt = null;
		ResultSet rs = null;
		JavManifestSent manifestSent = null;
		ArrayList values = new ArrayList();
		String query =
					  "SELECT MD_MNFT_NO,MD_NUMB_ORDR,MD_NUMB_PACK,MD_MNFT_AMNT,"+
					  "pack_web.FUN_MNFT_STUS_DESC('WEB','MANIFEST_STATUS',MD_MNFT_STUS)"+
					  "AS MNFT_STUS,to_char(MD_PLAN_COLL_DATE+pack_web.fun_ftch_time_diff(?)/86400,'DD/MM/YYYY HH24:MI') as MD_PLAN_COLL_DATE FROM WEB_MNFT_DETL WHERE"+
					  " md_trip_stus  IN('P','D','A','N') AND to_char(trunc(MD_ISSE_DATE),'DD/MM/YYYY') = ? and MD_CLNT_ID=? and SUBSTR(MD_BRNC_ID, 1, 3) = SUBSTR (?, 1, 3) AND NVL(MD_DELETE_FLAG,'N') = ? order by MD_MNFT_NO DESC";
		//('P','D','A') (MD_TRIP_STUS <> 'C')
		try{
			psmt = con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String manifestDate = form.getManifestDate();
			psmt.setString(1,branchId);//Added by rama
			psmt.setString(2,manifestDate);
			psmt.setString(3,clientId);
			psmt.setString(4,branchId);
			psmt.setString(5,"N");
			rs = psmt.executeQuery();
			int endIndex = form.getEndIndex();
			int startIndex = form.getStartIndex();
		
			if(endIndex == 0){
				endIndex = 6;
				form.setEndIndex(6);
			}
			if(startIndex == 0){
				startIndex = 1;
				form.setStartIndex(1);
			}
			int pageindex=0;
			while(rs.next()){
				pageindex++;
			}
			double pages = pageindex / 6.0;
			form.setMaxPageIndex(Math.ceil(pages));
			if(pageindex>0)
			rs.beforeFirst();
			if(pageindex>0)
			while(rs.next()){
				if(endIndex > pageindex){
					endIndex = pageindex;
				}
				if(startIndex <= endIndex){
					rs.absolute(startIndex);
					manifestSent = new JavManifestSent();
					manifestSent.setManifestNumber(rs.getString("MD_MNFT_NO"));
					manifestSent.setGuiaNumber(rs.getInt("MD_NUMB_ORDR"));
					manifestSent.setManifestAmount(rs.getDouble("MD_MNFT_AMNT"));
					manifestSent.setManifestStatus(rs.getString("MNFT_STUS"));
					manifestSent.setNoOfPackages(rs.getInt("MD_NUMB_PACK"));
					manifestSent.setPreferredCollectionTime(rs.getString("MD_PLAN_COLL_DATE"));
					values.add(manifestSent);
					startIndex++;
				}
			}			
			
		} catch(Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fetchManifestSent()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null)
					rs.close();
				if(psmt!=null)
					psmt.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fetchManifestSent()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		
		return values;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList fetchManifestNotSent(Connection con,JavShipmentHistoryForm form,String clientId,String branchId)throws Exception{
		JavManifestNotSent manifestNotSent = null;
		ArrayList values = new ArrayList();
		PreparedStatement psmt =null;
		ResultSet rs =null;
		String query=
					 "SELECT MD_MNFT_NO,MD_NUMB_ORDR,MD_NUMB_PACK,MD_MNFT_AMNT,"+
					 "pack_web.FUN_MNFT_STUS_DESC('WEB','MANIFEST_STATUS',MD_MNFT_STUS)"+
					 "AS MNFT_STUS,to_char(MD_PLAN_COLL_DATE+pack_web.fun_ftch_time_diff(?)/86400,'DD/MM/YYYY HH24:MI') as MD_PLAN_COLL_DATE, nvl(MD_DOCU_TYPE,'DL') DOCU_TYPE FROM WEB_MNFT_DETL WHERE"+
					 " md_trip_stus  IN('C') AND to_char(trunc(MD_ISSE_DATE),'DD/MM/YYYY') = ? and MD_CLNT_ID=? and SUBSTR(MD_BRNC_ID, 1, 3) = SUBSTR(?, 1, 3) AND NVL(MD_DELETE_FLAG,'N') = ? order by MD_MNFT_NO DESC";
		//('C','N')  MD_TRIP_STUS = 'C'
		try{
			psmt = con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String maniDate = form.getManifestDate();
			psmt.setString(1,branchId);//Added by rama
			psmt.setString(2,maniDate);
			psmt.setString(3,clientId);
			psmt.setString(4,branchId);
			psmt.setString(5,"N");
			rs= psmt.executeQuery();
			int endIndex = form.getEndNSIndex();
			int startIndex = form.getStartNSIndex();
			if(endIndex == 0){
				endIndex = 6;
				form.setEndNSIndex(6);
			}
			if(startIndex == 0){
				startIndex = 1;
				form.setStartNSIndex(1);
			}
			int pageindex=0;
			//AAP
			while(rs.next()){
				pageindex++;
			}
			double pages = pageindex / 6.0;
			form.setMaxPageNSIndex(Math.ceil(pages));
			if(pageindex>0)
			rs.beforeFirst();
			if(pageindex>0)
			while(rs.next()){
				if(endIndex > pageindex){
					endIndex = pageindex;
				}
				if(startIndex <= endIndex){
					rs.absolute(startIndex);
					manifestNotSent = new JavManifestNotSent();
					manifestNotSent.setManifestNotSentNumber(rs.getString("MD_MNFT_NO"));
					manifestNotSent.setGuiaNotSentNumber(rs.getInt("MD_NUMB_ORDR"));
					manifestNotSent.setManifestNotSentAmount(rs.getDouble("MD_MNFT_AMNT"));
					manifestNotSent.setManifestNotSentStatus(rs.getString("MNFT_STUS"));
					manifestNotSent.setNoOfNotSentPackages(rs.getInt("MD_NUMB_PACK"));
					manifestNotSent.setPreferredCollectionNotSentTime(rs.getString("MD_PLAN_COLL_DATE"));
					manifestNotSent.setDocuType(rs.getString("DOCU_TYPE"));
					values.add(manifestNotSent);
					startIndex++;
				}
			}
			
		} catch(Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fetchManifestNotSent()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {		
				if(rs!=null)
					rs.close();
				if(psmt!=null)
					psmt.close();
			} catch(Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("fetchManifestNotSent()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}		
		return values;
	}
	
	public void deleteManifestRecord(Connection con,String manifestNumber,String clientId, String usuario)  {
		CallableStatement cst=null;
		try{
			cst=con.prepareCall("begin pack_web.pro_del_mnft_LOG(?,?,?);end;");

			cst.setString(1,manifestNumber);
			cst.setString(2,clientId);
			cst.setString(3,usuario);
			cst.execute();
			con.commit();
		} catch(Exception e){
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("deleteManifestRecord()_Error1:").append(e).toString());
			e.printStackTrace();
			try {
				con.rollback();
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("deleteManifestRecord()_Error2:").append(e).toString());
				e.printStackTrace();
			}			
		} finally {
			 try {
				 if (cst!=null) cst.close();
			 }catch(Exception e){
				 AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("deleteManifestRecord()_Error3:").append(e).toString());
				e.printStackTrace();
				 try {
					 con.rollback();
				} catch (Exception e2) {
					AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("deleteManifestRecord()_Error4:").append(e).toString());
					e.printStackTrace();
				}
			 }
		 }
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList printManifestSent(Connection con,JavShipmentHistoryForm form,String clientId,String branchId) {
		JavManifestSent manifestSent = null;
		ArrayList values = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			String query =
					  "SELECT MD_MNFT_NO,MD_NUMB_ORDR,MD_NUMB_PACK,MD_MNFT_AMNT,"+
					  "pack_web.FUN_MNFT_STUS_DESC('WEB','MANIFEST_STATUS',MD_MNFT_STUS)"+
					  "AS MNFT_STUS,to_char(MD_PLAN_COLL_DATE,'DD/MM/YYYY HH24:MI:SS') as MD_PLAN_COLL_DATE FROM WEB_MNFT_DETL WHERE"+
					  " md_trip_stus  IN('P','D','A','N') AND to_char(trunc(MD_ISSE_DATE),'DD/MM/YYYY') = ? and MD_CLNT_ID=? and SUBSTR(MD_BRNC_ID, 1, 3) = SUBSTR(?, 1, 3) AND NVL(MD_DELETE_FLAG,'N') = ? order by MD_MNFT_NO DESC";
			psmt = con.prepareStatement(query);
			String manifestDate = form.getManifestDate();
			psmt.setString(1,manifestDate);
			psmt.setString(2,clientId);
			psmt.setString(3,branchId);
			psmt.setString(4, "N");
			rs = psmt.executeQuery();	
			
			while (rs.next()) {				
				manifestSent = new JavManifestSent();
				manifestSent.setManifestNumber(rs.getString("MD_MNFT_NO"));
				manifestSent.setGuiaNumber(rs.getInt("MD_NUMB_ORDR"));
				manifestSent.setManifestAmount(rs.getDouble("MD_MNFT_AMNT"));
				manifestSent.setManifestStatus(rs.getString("MNFT_STUS"));
				manifestSent.setNoOfPackages(rs.getInt("MD_NUMB_PACK"));
				manifestSent.setPreferredCollectionTime(rs.getString("MD_PLAN_COLL_DATE"));
				values.add(manifestSent);				
				
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("printManifestSent()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null)
					rs.close();
				if(psmt!=null)
					psmt.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("printManifestSent()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}		
		return values;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList printManifestNotSent(Connection con,JavShipmentHistoryForm form,String clientId,String branchId) {
		JavManifestNotSent manifestNotSent = null;
		ArrayList values = new ArrayList();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			String query=
					 "SELECT MD_MNFT_NO,MD_NUMB_ORDR,MD_NUMB_PACK,MD_MNFT_AMNT,"+
					 "pack_web.FUN_MNFT_STUS_DESC('WEB','MANIFEST_STATUS',MD_MNFT_STUS)"+
					 "AS MNFT_STUS,to_char(MD_PLAN_COLL_DATE,'DD/MM/YYYY HH24:MI:SS') as MD_PLAN_COLL_DATE FROM WEB_MNFT_DETL WHERE"+
					 " md_trip_stus  IN('C') AND to_char(trunc(MD_ISSE_DATE),'DD/MM/YYYY') = ? and MD_CLNT_ID=? and SUBSTR(MD_BRNC_ID,1,3) = SUBSTR(?,1,3) AND NVL(MD_DELETE_FLAG,'N') = ? order by MD_MNFT_NO DESC";
			psmt = con.prepareStatement(query);
			String maniDate = form.getManifestDate();
			psmt.setString(1,maniDate);
			psmt.setString(2,clientId);
			psmt.setString(3,branchId);
			psmt.setString(4, "N");
			
			rs = psmt.executeQuery();
			
			while(rs.next()){						
				
				manifestNotSent = new JavManifestNotSent();
				manifestNotSent.setManifestNotSentNumber(rs.getString("MD_MNFT_NO"));
				manifestNotSent.setGuiaNotSentNumber(rs.getInt("MD_NUMB_ORDR"));
				manifestNotSent.setManifestNotSentAmount(rs.getDouble("MD_MNFT_AMNT"));
				manifestNotSent.setManifestNotSentStatus(rs.getString("MNFT_STUS"));
				manifestNotSent.setNoOfNotSentPackages(rs.getInt("MD_NUMB_PACK"));
				manifestNotSent.setPreferredCollectionNotSentTime(rs.getString("MD_PLAN_COLL_DATE"));				
				values.add(manifestNotSent);
				
			}
			rs.close();//AAP01
			psmt.close();
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("printManifestNotSent()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if(rs!=null)
					rs.close();
				if(psmt!=null)
					psmt.close();
			} catch (Exception e) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("printManifestNotSent()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}		
		return values;
	}
	
	public String consultaGuiasSinMnft(Connection con, Global global, String clientId, String branchId, String pymtMode,String docuType, String filtroPor) {//AAP01	
		String result = "";	
		String filtroUsuario = "AND CRTD_BY = ? ";
		String usuario = global.getOrigenUserClave();
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {			
			/*condicion solo para guias flete por cobrar o guias Prepago*/
			if (filtroPor.equals("1")) {
				if ((pymtMode.equals("TO_PAY") && docuType.equals("Q")) || (pymtMode.equals("PAID") && docuType.equals("P"))) {
					filtroUsuario = "AND CRTD_BY <> ? ";
					usuario = " ";
				}
			}
			
			String query= cnct.delete(0,cnct.length())
					.append("SELECT GH_PYMT_MODE ")
					.append("FROM bok_guia_head ")
					.append("WHERE gh_orgn_brnc_id = ? ")
					.append("AND gh_orgn_clnt_id = ? ")
					.append("AND gh_guia_refr_no IS NULL ")
					.append("AND GH_GUIA_TYPE = ? ")
					.append("and GH_ACTV_FLAG = ? ")
					.append("AND GH_RAD_FLAG IN (?, ?) ")
					.append("AND GH_PYMT_MODE = ? ")
					.append("AND GH_DOCU_TYPE = ? ")
					.append(filtroUsuario)
					.append("AND ROWNUM = ?").toString();
			psmt = con.prepareStatement(query);
			
			psmt.setString(1, branchId);
			psmt.setString(2, clientId);
			psmt.setString(3, "H");
			psmt.setString(4, "A");
			psmt.setString(5, "1");
			psmt.setString(6, "6");
			psmt.setString(7, pymtMode);
			psmt.setString(8, docuType);
			psmt.setString(9, usuario);
			psmt.setString(10, "1");
			
			rs = psmt.executeQuery();
			
			if (rs.next()){						
				result = rs.getString(1) == null ? "" :rs.getString(1);				
			}	
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("consultaGuiasSinMnft()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs!= null) {
					rs.close();	
				}
				if (psmt!=null) {
					psmt.close();
				}	
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("consultaGuiasSinMnft()_Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}

	public String consultaGuiasSinMnftCCosto(Connection con, Global global, String clientId, String branchId, String pymtMode,String docuType) {
		String result = "";
		String query = cnct.delete(0, cnct.length())
				.append("SELECT A.GH_PYMT_MODE ")
				.append("FROM bok_guia_head A, bok_guia_head_extra B ")
				.append("WHERE A.gh_orgn_brnc_id = ? ")
				.append("AND A.gh_orgn_clnt_id = ? ")
				.append("AND A.gh_guia_refr_no IS NULL ")
				.append("AND A.GH_GUIA_TYPE = ? ")
				.append("AND A.GH_ACTV_FLAG = ? ")
				.append("AND A.GH_RAD_FLAG IN (?, ?) ")
				.append("AND A.GH_PYMT_MODE = ? ")
				.append("AND A.GH_DOCU_TYPE = ? ")				
				.append("AND A.GH_GUIA_NO = B.GE_GUIA_NO ")
				.append("AND B.GE_CLNT_CCOSTO_ID in (")
					.append("SELECT CU_CCOSTO_ID ")
					.append("FROM SYS_CLNT_CCOSTO_USER ")
					.append("WHERE CU_CLNT_ID = ? ")
					.append("AND CU_USER_ID = ?")
					.append(") ")
				.append("AND ROWNUM = ?")
				.toString();	
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, branchId);
			psmt.setString(2, clientId);
			psmt.setString(3, "H");
			psmt.setString(4, "A");
			psmt.setString(5, "1");
			psmt.setString(6, "6");
			psmt.setString(7, pymtMode);
			psmt.setString(8, docuType);
			psmt.setString(9, clientId);
			psmt.setString(10, global.getOrigenUserClave());
			psmt.setString(11, "1");
			
			rs = psmt.executeQuery();
			
			if (rs.next()){						
				result = rs.getString(1) == null ? "" :rs.getString(1);				
			}	
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("consultaGuiasSinMnftCCosto()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs!= null) {
					rs.close();	
				}				
				if (psmt!=null) {
					psmt.close();
				}	
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("consultaGuiasSinMnftCCosto()_Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList consultaGuiasSinMnftDirRecoleccion (Connection con, Global global, String clientId) {
		
		ArrayList result = new ArrayList(10);
		HashMap row = new HashMap(8);
		String query = cnct.delete(0, cnct.length())
				.append("SELECT ")
				.append("GA_ADDR_CODE, GA_STRT_NAME, GA_DRNR, GA_ADDR_LIN6, GA_ZIP_CODE, GA_ADDR_LIN4, GA_ADDR_LIN3, GA_ADDR_LIN1 ") 
				.append("FROM BOK_GUIA_ADDR WHERE GA_GUIA_NO IN (")
					.append("SELECT GH_GUIA_NO ")
					.append("FROM bok_guia_head ")
					.append("WHERE gh_orgn_clnt_id = ? ")					
					.append("AND GH_GUIA_TYPE = ? ")
					.append("and GH_ACTV_FLAG = ? ")
					.append("AND GH_RAD_FLAG IN (?, ?) ")
					.append("AND gh_guia_refr_no IS NULL ")
					.append("AND CRTD_BY = ? ")
					.append("AND substr(GH_ORGN_BRNC_ID,0,3) = ?  ")
				.append(") ")
				.append("AND GA_ADDR_TYPE = ? ")
				.append("GROUP BY GA_ADDR_CODE, GA_STRT_NAME, GA_DRNR, GA_ADDR_LIN6, GA_ZIP_CODE, GA_ADDR_LIN4, GA_ADDR_LIN3, GA_ADDR_LIN1")
				.toString();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, clientId);
			psmt.setString(2, "H");
			psmt.setString(3, "A");
			psmt.setString(4, "1");
			psmt.setString(5, "6");
			psmt.setString(6, global.getOrigenUserClave());
			psmt.setString(7, global.getAssignedSite());
			psmt.setString(8, "ORIGIN");
			
			rs = psmt.executeQuery();
			while (rs.next()){	
				row.put("GA_ADDR_CODE", rs.getString(1) == null ? "" :rs.getString(1));
				row.put("GA_STRT_NAME", rs.getString(2) == null ? "" :rs.getString(2));
				row.put("GA_DRNR", rs.getString(3) == null ? "" :rs.getString(3));
				row.put("GA_ADDR_LIN6", rs.getString(4) == null ? "" :rs.getString(4));
				row.put("GA_ZIP_CODE", rs.getString(5) == null ? "" :rs.getString(5));
				row.put("GA_ADDR_LIN4", rs.getString(6) == null ? "" :rs.getString(6));
				row.put("GA_ADDR_LIN3", rs.getString(7) == null ? "" :rs.getString(7));
				row.put("GA_ADDR_LIN1", rs.getString(8) == null ? "" :rs.getString(8));
				result.add(row.clone());
				row.clear();
			}	
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("consultaGuiasSinMnftDirRecoleccion()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs!= null) {
					rs.close();	
				}				
				if (psmt!=null) {
					psmt.close();
				}	
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("consultaGuiasSinMnftDirRecoleccion()_Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList consultaGuiasSinMnftDirRecoleccionCCostos(Connection con, Global global, String clientId) {
	
		ArrayList result = new ArrayList(10);
		HashMap row = new HashMap(8);
		String query = cnct.delete(0, cnct.length())
				.append("SELECT ")
				.append("GA_ADDR_CODE, GA_STRT_NAME, GA_DRNR, GA_ADDR_LIN6, GA_ZIP_CODE, GA_ADDR_LIN4, GA_ADDR_LIN3, GA_ADDR_LIN1 ") 
				.append("FROM BOK_GUIA_ADDR WHERE GA_GUIA_NO IN (")
					.append("SELECT GH_GUIA_NO ")
					.append("FROM bok_guia_head, bok_guia_head_extra ")
					.append("WHERE gh_orgn_clnt_id = ? ")					
					.append("AND GH_GUIA_TYPE = ? ")
					.append("and GH_ACTV_FLAG = ? ")
					.append("AND GH_RAD_FLAG IN (?, ?) ")
					.append("AND gh_guia_refr_no IS NULL ")
					.append("and GH_GUIA_NO = GE_GUIA_NO ")
					.append("and GE_CLNT_ID = ? ")
					.append("AND GE_CLNT_CCOSTO_ID IN ( SELECT CU_CCOSTO_ID FROM SYS_CLNT_CCOSTO_USER WHERE CU_CLNT_ID = ? AND CU_USER_ID = ?) ")
				.append(") ")
				.append("AND GA_ADDR_TYPE = ? ")
				.append("GROUP BY GA_ADDR_CODE, GA_STRT_NAME, GA_DRNR, GA_ADDR_LIN6, GA_ZIP_CODE, GA_ADDR_LIN4, GA_ADDR_LIN3, GA_ADDR_LIN1")
				.toString();
		
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = con.prepareStatement(query);
			
			psmt.setString(1, clientId);
			psmt.setString(2, "H");
			psmt.setString(3, "A");
			psmt.setString(4, "1");
			psmt.setString(5, "6");
			psmt.setString(6, clientId); 
			psmt.setString(7, clientId);
			psmt.setString(8, global.getOrigenUserClave());
			psmt.setString(9, "ORIGIN");
			
			rs = psmt.executeQuery();
			
			while (rs.next()) {
				row.put("GA_ADDR_CODE", rs.getString(1) == null ? "" :rs.getString(1));
				row.put("GA_STRT_NAME", rs.getString(2) == null ? "" :rs.getString(2));
				row.put("GA_DRNR", rs.getString(3) == null ? "" :rs.getString(3));
				row.put("GA_ADDR_LIN6", rs.getString(4) == null ? "" :rs.getString(4));
				row.put("GA_ZIP_CODE", rs.getString(5) == null ? "" :rs.getString(5));				
				row.put("GA_ADDR_LIN4", rs.getString(6) == null ? "" :rs.getString(6));
				row.put("GA_ADDR_LIN3", rs.getString(7) == null ? "" :rs.getString(7));
				row.put("GA_ADDR_LIN1", rs.getString(8) == null ? "" :rs.getString(8));
				result.add(row.clone());
				row.clear();
			}	
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("consultaGuiasSinMnftDirRecoleccionCCostos()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs!= null) {
					rs.close();	
				}
				if (psmt!=null) {
					psmt.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("consultaGuiasSinMnftDirRecoleccionCCostos()_Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList consultaGuiasSinMnftDirRecoleccionWE (Connection con, Global global, String clientId) {	
		ArrayList result = new ArrayList(10);
		HashMap row = new HashMap(8);
		String query = cnct.delete(0, cnct.length())
				.append("SELECT ")
				.append("GA_ADDR_CODE, GA_STRT_NAME, GA_DRNR, GA_ADDR_LIN6, GA_ZIP_CODE, GA_ADDR_LIN4, GA_ADDR_LIN3, GA_ADDR_LIN1 ") 
				.append("FROM BOK_GUIA_ADDR WHERE GA_GUIA_NO IN ( ")
					.append("SELECT GH_GUIA_NO ")
					.append("FROM bok_guia_head A, bok_guia_head_extra B WHERE ")
					.append("B.GE_CLNT_REQT = ? ")					
					.append("AND A.GH_GUIA_TYPE = ? ")
					.append("AND A.GH_ACTV_FLAG = ? ")
					.append("AND A.GH_RAD_FLAG IN (?, ?) ")					
					.append("AND substr(A.GH_ORGN_BRNC_ID,0,3) = ?  ")
					.append("AND A.gh_guia_refr_no IS NULL ")
					.append("AND substr(GH_FORM_NO,0,2) = ? ")
					.append("AND A.GH_GUIA_NO = B.GE_GUIA_NO ) ")
				.append("AND GA_ADDR_TYPE = ? ")
				.append("GROUP BY GA_ADDR_CODE, GA_STRT_NAME, GA_DRNR, GA_ADDR_LIN6, GA_ZIP_CODE, GA_ADDR_LIN4, GA_ADDR_LIN3, GA_ADDR_LIN1")
				.toString();
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, global.getClientIdAgreement());
			psmt.setString(2, "H");
			psmt.setString(3, "A");
			psmt.setString(4, "1");
			psmt.setString(5, "6");
			psmt.setString(6, global.getAssignedSite());
			psmt.setString(7, "WE");	
			psmt.setString(8, "ORIGIN");
			
			rs = psmt.executeQuery();
			while (rs.next()){	
				row.put("GA_ADDR_CODE", rs.getString(1) == null ? "" :rs.getString(1));
				row.put("GA_STRT_NAME", rs.getString(2) == null ? "" :rs.getString(2));
				row.put("GA_DRNR", rs.getString(3) == null ? "" :rs.getString(3));
				row.put("GA_ADDR_LIN6", rs.getString(4) == null ? "" :rs.getString(4));
				row.put("GA_ZIP_CODE", rs.getString(5) == null ? "" :rs.getString(5));
				row.put("GA_ADDR_LIN4", rs.getString(6) == null ? "" :rs.getString(6));
				row.put("GA_ADDR_LIN3", rs.getString(7) == null ? "" :rs.getString(7));
				row.put("GA_ADDR_LIN1", rs.getString(8) == null ? "" :rs.getString(8));
				result.add(row.clone());
				row.clear();
			}	
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("consultaGuiasSinMnftDirRecoleccionWE()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs!= null) {
					rs.close();	
				}				
				if (psmt!=null) {
					psmt.close();
				}	
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("consultaGuiasSinMnftDirRecoleccionWE()_Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
}
