

/**
* File Name    : ManifestGeneration.java
* Description  : This is the Action Class For LoginForm
*				  to handle and Control the Inputs of that FormBean. 
* Date Written :  -2003
* @author 	   :  D.SivaKumar
* Updated On      Version    Updated By				  Summary
* ~~~~~~~~~~~~~   ~~~~~~~    ~~~~~~~~~~~~~~~~~		  ~~~~~~~~~~~~~~~
* 31-May-2001				 Kumaran Systems		  For grouping guia
* ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * ----------------------------------------------------------------- 
 * Clave: AAP02
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 04/07/2013
 * Descripción: se agregó variable formaPago a metodo insertToManifestDetail(), a metodo getBookedGuiasDetails(). 
 * se agregó para realizar consulta de guias pendientes de generar manifiesto segun lo seleccionado ('PAID' o 'TO_PAY')
 * Se agregó método insertReferenceRecord() para insertar la referencia capturada para relacionar a manifiesto. 
 * ----------------------------------------------------------------- 
 */

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import logger.AccessLog;
import bean.Global;
import bean.JavManifestGenerationForm;
import bean.JavManifestRecord;
import bean.Resources;

public class JavManifestGenerationBean {
	private StringBuffer cnct = new StringBuffer();	
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private final String msgAvi = new StringBuffer("MSAVI_B_").append(this.getClass().getName()).append(".").toString();
	private Resources resources = new Resources();
	public JavManifestGenerationBean() {		
	}
	
	String seqManifestNumber="";
	
	// Function to get the Credit Limit Of the client....
	
	public double getCreditLimit(Connection con, JavManifestGenerationForm mgf, String webClientId) {
		String query = "";
		double creditLimit = 0.0;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {
			query = "SELECT FUN_CLNT_CRDT_AMT_WW(?) AS CREDITlIMIT FROM DUAL";
			pst = con.prepareStatement(query);
			pst.setString(1, webClientId);
			rs = pst.executeQuery();
			if (rs != null && rs.next()) {
				creditLimit = rs.getDouble("CREDITlIMIT");
				mgf.setCreditLimit(creditLimit);
				// AAP
			}
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception ex) {
				AccessLog.Log(ex);
			}
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCreditLimit()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
		}
		return creditLimit;
	}
	
	public int getType(Connection con, Global global, JavShipmentHistoryForm manifestForms) {
		CallableStatement cst = null;
		int val = 0;
		try {
			//1:filtro por usuario
			//2:filtro por centro de costo
			if (manifestForms.getFiltroPor().equals("1")) {
				cst = con.prepareCall("{ ? = call pack_web.fun_chk_brdr_new_ccosto(?,?,?)}");	
			} else {
				cst = con.prepareCall("{ ? = call pack_web.fun_chk_brdr_new(?,?,?)}");				
			}
			
			cst.registerOutParameter(1, Types.INTEGER);
			cst.setString(2, global.getAssignedSite());
			cst.setString(3, global.getClientId());
			cst.setString(4, global.getOrigenUserClave());
			cst.executeUpdate();
			
			val = cst.getInt(1);
			
			//return val;
		} catch (SQLException e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getType()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarCallableStatement(cst);
		}
		return val;
	}
	
	public int insertToManifestDetail(HttpSession session,
			Connection con, JavManifestGenerationForm mgf, String webClientId,
			String strGuiaNumbers, String BranchId) {
		String query = "";
		String query1 = "";
		String gnoset = "";
		String cttNumQuery = "";
		String cttNumber = "";
		String strCollectionTime = "";
		StringTokenizer st = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		String manifestIssueDate = null;
		double sumAmnt = 0.0;
		int sumPack = 0;
		int numOrdered = 0;
		int insertCount = 0;
		Global global = (Global)session.getAttribute("sGlobal");
		try {// Get the Manifest Number From the Sequence..
			/*obtiene informacion de direccion origen seleccionada*/
			JavDestinationAddressRecords clientAddress = new JavDestinationAddressRecords();
			ArrayList records = clientAddress.getLovRecordsByCode(con, mgf.getPreferedAddressCode(), BranchId, webClientId);
			
			if (!records.isEmpty()) {
				HashMap values = (HashMap) records.get(0);
				mgf.setAddressCode(mgf.getPreferedAddressCode());
				mgf.setStreetName(values.get("AM_STRT_NAME").toString());
				mgf.setDoorNumber(values.get("AM_DRNR").toString());
				mgf.setPhoneNumber(values.get("AM_PHNO1").toString());
				mgf.setSuitNumber(values.get("AM_SUIT_NO").toString());
				mgf.setFloorNumber(values.get("AM_FLOR_NO").toString());
				mgf.setAddressLine1(values.get("u11").toString());
				mgf.setAddressLine2(values.get("u12").toString());
				mgf.setAddressLine3(values.get("u13").toString());
				mgf.setAddressLine4(values.get("u14").toString());
				mgf.setAddressLine5(values.get("u15").toString());
				mgf.setAddressLine6(values.get("u16").toString());
				mgf.setAddressLine7(values.get("u17").toString());
				mgf.setZipCode(values.get("Zipcode").toString());
				mgf.setAddressType(values.get("AM_ADDR_STYP").toString());
				BranchId = values.get("branch").toString();
		        
			}
			String query3 = "SELECT to_char('MF'||lpad(WEB_MNFT_NO.nextval,8,'0')) FROM Dual";
			pst = con.prepareStatement(query3);
			rs = pst.executeQuery();
			if (rs != null && rs.next()) {
				seqManifestNumber = rs.getString(1);
			}

			if (rs != null) {
				rs.close();
			}				
			if (pst != null) {
				pst.close();
			}				

			// GET THE CTTNO FROM THE SEQUENCE CTTNO..
			cttNumQuery = "SELECT CTTNO.NEXTVAL FROM DUAL";
			pst = con.prepareStatement(cttNumQuery);
			rs = pst.executeQuery();
			if (rs.next()) {
				cttNumber = cnct.delete(0,cnct.length()).append("").append(rs.getInt(1)).toString();
				int concttno = Integer.parseInt(cttNumber);
				mgf.setCttNumber(concttno);

			}
			if (rs != null) {
				rs.close();
			}				
			if (pst != null) {
				pst.close();
			}
			// Get the Sum Values....
			query = cnct.delete(0,cnct.length())
						.append("select sum(gh_guia_amnt),sum(gh_numb_pack),count(*) from BOK_GUIA_HEAD")
						.append(" WHERE gh_guia_no in(").toString();

			if (strGuiaNumbers != null || strGuiaNumbers.equals("")) {
				st = new StringTokenizer(strGuiaNumbers);
				while (st.hasMoreElements()) {
					gnoset = cnct.delete(0,cnct.length()).append( gnoset ).append( "'" ).append( st.nextToken() ).append( "',").toString();
				}
			}

			/*no se filtra por usuario de creacion, porque previamente en la consulta de guias, las guias seleccionadas ya vienen filtradas*/
			String newSet = gnoset.substring(0, gnoset.length() - 1);
			query = cnct.delete(0,cnct.length()).append( query ).append( newSet ).append( " )")
						.append(" AND GH_GUIA_TYPE = ? AND GH_ACTV_FLAG = ?")
						.toString();
			
			pst = con.prepareStatement(query);
			pst.setString(1, "H");
			pst.setString(2, "A");
			
			rs = pst.executeQuery();
			if (rs != null && rs.next()) {
				sumAmnt = rs.getDouble(1);
				sumPack = rs.getInt(2);
				numOrdered = rs.getInt(3);
				mgf.setManifestAmount(sumAmnt);
				mgf.setSumPack(sumPack);
				mgf.setNumOrdered(numOrdered);

				// Get the Address Details and Insert it into the DataBase....

				strCollectionTime = mgf.getCollectionTime();
				mgf.setCollectionTime(strCollectionTime);

				// Added by rama	// AAP		

				String strAddrCode = (mgf.getAddressCode() == null ? "" : mgf.getAddressCode());
				String strDoorNumber = (mgf.getDoorNumber() == null ? "" : mgf.getDoorNumber());
				String strStreetName = (mgf.getStreetName() == null ? "" : mgf.getStreetName());
				String strPhoneNumber = (mgf.getPhoneNumber() == null ? "" : mgf.getPhoneNumber());
				String strSuitNumber = (mgf.getSuitNumber() == null ? "" : mgf.getSuitNumber());
				String strFloorNumber = (mgf.getFloorNumber() == null ? "" : mgf.getFloorNumber());
				String strAddr1 = (mgf.getAddressLine1() == null ? "" : mgf.getAddressLine1());
				String strAddr2 = (mgf.getAddressLine2() == null ? "" : mgf.getAddressLine2());
				String strAddr3 = (mgf.getAddressLine3() == null ? "" : mgf.getAddressLine3());
				String strAddr4 = (mgf.getAddressLine4() == null ? "" : mgf.getAddressLine4());
				String strAddr5 = (mgf.getAddressLine5() == null ? "" : mgf.getAddressLine5());
				String strAddr6 = (mgf.getAddressLine6() == null ? "" : mgf.getAddressLine6());
				String strAddr7 = (mgf.getAddressLine7() == null ? "" : mgf.getAddressLine7());
				String strZipCode = (mgf.getZipCode() == null ? "" : mgf.getZipCode());

				// Modified By Sam.D.Jabeen on[01-Jun-2006] for adding manifesto type
				String strManifiestoType = "";
				if (mgf.getManifiestoType().equals("BR")) {
					strManifiestoType = "B";
				} else {
					strManifiestoType = "I";
				}

				query1 = cnct.delete(0,cnct.length())
							.append("INSERT INTO WEB_MNFT_DETL(MD_MNFT_NO,")
							.append(" MD_CLNT_ID, MD_BRNC_ID, MD_ISSE_DATE, ")
							.append("MD_NUMB_ORDR, MD_NUMB_PACK, MD_MNFT_AMNT, ")
							.append("MD_MNFT_STUS, MD_ACTV_FLAG, MD_TRIP_STUS, ")
							.append("MD_PLAN_COLL_DATE, CRTD_ON, CRTD_BY, ")
							.append("MDFD_ON, MDFD_BY, MD_ADDR_CODE,")
							.append("MD_DRNR, MD_STRT_NAME, MD_PHNO_1,")
							.append("MD_SUIT_NO, MD_FLOR_NO, MD_ADDR_LIN1,")
							.append("MD_ADDR_LIN2, MD_ADDR_LIN3, MD_ADDR_LIN4,")
							.append("MD_ADDR_LIN5, MD_ADDR_LIN6, MD_ADDR_LIN7,")
							.append("MD_ZIP_CODE,MD_CTT_REFR_NO, MD_FLAG_1, MD_PYMT_MODE, ")//AAP02
							.append("MD_FLAG_2, MD_FLAG_3,")//AAP03
							.append("MD_DELETE_FLAG, MD_DOCU_TYPE)")
							.append(" VALUES(?,?,?,sysdate,?,?,?,?,?,?,to_date(?,'dd/mm/yyyy hh24:mi')-(")
							.append( global.timediff)
							.append("/86400000),sysdate,?,sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?, ?, ?)").toString();//AAP02

				pst1 = con.prepareStatement(query1);

				pst1.setString(1, seqManifestNumber);
				pst1.setString(2, webClientId);
				pst1.setString(3, BranchId);
				pst1.setInt(4, numOrdered);
				pst1.setInt(5, sumPack);
				pst1.setDouble(6, sumAmnt);
				pst1.setString(7, "PRP");
				pst1.setString(8, "A");
				pst1.setString(9, "C");
				pst1.setString(10, strCollectionTime); // Rama
				pst1.setString(11, global.getOrigenUserClave());//AAP03
				pst1.setString(12, global.getOrigenUserClave());//AAP03
				pst1.setString(13, strAddrCode);
				pst1.setString(14, strDoorNumber);
				pst1.setString(15, strStreetName);
				pst1.setString(16, strPhoneNumber);
				pst1.setString(17, strSuitNumber);
				pst1.setString(18, strFloorNumber);
				pst1.setString(19, strAddr1);
				pst1.setString(20, strAddr2);
				pst1.setString(21, strAddr3);
				pst1.setString(22, strAddr4);
				pst1.setString(23, strAddr5);
				pst1.setString(24, strAddr6);
				pst1.setString(25, strAddr7);
				pst1.setString(26, strZipCode);
				pst1.setString(27, cttNumber);
				pst1.setString(28, strManifiestoType);
				pst1.setString(29, mgf.getFormaPago());//AAP02
				pst1.setString(30, mgf.getDocuType());//AAP03
				pst1.setString(31, mgf.getCentroCostoSel());//AAP03
				pst1.setString(32, "N");//AAP04
				pst1.setString(33, "DL");//AAP04

				insertCount = pst1.executeUpdate();
				if (insertCount > 0) {
					con.commit();
					String tempquery = "select to_char(sysdate,'dd/mm/yyyy hh24:mi') from dual ";
					pst2 = con.prepareStatement(tempquery);
					rs1 = pst2.executeQuery();
					if (rs1.next()) {
						manifestIssueDate = (rs1.getString(1) == null ? "" : rs1.getString(1));
						mgf.setManifestIssueDate(manifestIssueDate);
					}
					if (mgf.getReference() != null && mgf.getReference().length() > 0) {//AAP02
						insertReferenceRecord(con, session, seqManifestNumber, mgf.getReference(), mgf.getDocuType());//AAP02
					}//AAP02
					// get the Manifest number From Manifest Details and Set it into the Form Field..
					mgf.setManifestNumber(seqManifestNumber);
				}				
			}			
		} catch (Exception e) {
			try {
				con.rollback();	
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertToManifestDetail()_Error2:").append(e2).toString());
			}			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertToManifestDetail()_Error1:").append(e).toString());
			e.printStackTrace();
			mgf.setManifestNumber("");
		} finally {
			resources.closeResources(rs, pst);
			resources.closeResources(rs1, pst1);
			resources.cerrarPreparedStatement(pst2);
		}		
		return insertCount;		
	}
	
	public int updateBokGuiaHead(Connection con, JavManifestGenerationForm mgf,
			String webClientId, String strGuiaNumbers, String BranchId, Global global) {

		String query = "";
		String gnoset = "";
		StringTokenizer st = null;
		PreparedStatement pst = null;
		int updateCount = 0;
		try {
			// Update the BokGuia_Head...
			if (mgf.getFiltroPor().equals("3")) {
				query = cnct.delete(0,cnct.length())
						.append("UPDATE BOK_GUIA_HEAD SET GH_GUIA_REFR_NO = ?,GH_GUIA_STUS = ?,MDFD_ON = SYSDATE, MDFD_BY = ? ")
						.append("WHERE GH_GUIA_TYPE = ? AND GH_ACTV_FLAG = ? and GH_GUIA_REFR_NO is null AND ")
						.append("GH_GUIA_NO in(").toString();
			}else {
				query = cnct.delete(0,cnct.length())
						.append("UPDATE BOK_GUIA_HEAD SET GH_GUIA_REFR_NO = ?,GH_GUIA_STUS = ?,MDFD_ON = SYSDATE, MDFD_BY = ? ")
						.append("WHERE GH_ORGN_CLNT_ID = ? AND GH_GUIA_TYPE = ? AND GH_ACTV_FLAG = ? and GH_GUIA_REFR_NO is null AND ")
						.append("GH_GUIA_NO in(").toString();
			}
			
			if (strGuiaNumbers != null || strGuiaNumbers.equals("")) {

				st = new StringTokenizer(strGuiaNumbers);
				while (st.hasMoreElements()) {
					gnoset = cnct.delete(0,cnct.length()).append( gnoset ).append( "'" ).append( st.nextToken() ).append( "'," ).toString();
				}
			}

			String newSet = gnoset.substring(0, gnoset.length() - 1);
			query = cnct.delete(0,cnct.length()).append( query ).append( newSet ).append( " )").toString();

			if (mgf.getFiltroPor().equals("3")) {
				pst = con.prepareStatement(query);
				pst.setString(1, seqManifestNumber);
				pst.setString(2, "IIM");
				pst.setString(3, global.getOrigenUserClave());//AAP03
				pst.setString(4, "H");
				pst.setString(5, "A");
			}else {
				pst = con.prepareStatement(query);
				pst.setString(1, seqManifestNumber);
				pst.setString(2, "IIM");
				pst.setString(3, global.getOrigenUserClave());//AAP03
				pst.setString(4, webClientId);
				pst.setString(5, "H");
				pst.setString(6, "A");
			}
		
			updateCount = pst.executeUpdate();
			if (updateCount > 0) {
				con.commit();
			}			
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateBokGuiaHead()_Error2:").append(e2).toString());
			}			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("updateBokGuiaHead()_Error1:").append(e).toString());
			e.printStackTrace();
			mgf.setManifestNumber("");
		} finally {
			resources.cerrarPreparedStatement(pst);			
		}
		return updateCount;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList validGuiasBokGuiaHead(Connection con, JavManifestGenerationForm mgf,
			String webClientId, String strGuiaNumbers, String BranchId) {

		String query = "";
		String gnoset = "";
		StringTokenizer st = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList guiaNumbers = new ArrayList(1);
		ArrayList result = new ArrayList();
		try {
			// Update the BokGuia_Head...
			if (mgf.getFiltroPor().equals("3")) {
				query = cnct.delete(0,cnct.length())
						.append("SELECT GH_GUIA_NO FROM BOK_GUIA_HEAD ")
						.append("WHERE GH_GUIA_TYPE = ? AND GH_ACTV_FLAG = ? AND GH_GUIA_REFR_NO is NOT null AND ")
						.append("GH_GUIA_NO in(").toString();
			}else {
				query = cnct.delete(0,cnct.length())
						.append("SELECT GH_GUIA_NO FROM BOK_GUIA_HEAD ")
						.append("WHERE GH_ORGN_CLNT_ID = ? AND GH_GUIA_TYPE = ? AND GH_ACTV_FLAG = ? AND GH_GUIA_REFR_NO is NOT null AND ")
						.append("GH_GUIA_NO in(").toString();
			}
			
			if (strGuiaNumbers != null || strGuiaNumbers.equals("")) {
				st = new StringTokenizer(strGuiaNumbers);
				while (st.hasMoreElements()) {
					gnoset = cnct.delete(0,cnct.length()).append( gnoset ).append( "'" ).append( st.nextToken() ).append( "'," ).toString();
				}
			}

			String newSet = gnoset.substring(0, gnoset.length() - 1);
			query = cnct.delete(0,cnct.length()).append( query ).append( newSet ).append( " )").toString();
			
			pst = con.prepareStatement(query);
			if (mgf.getFiltroPor().equals("3")) {
				pst.setString(1, "H");
				pst.setString(2, "A");
			}else {
				pst.setString(1, webClientId);
				pst.setString(2, "H");
				pst.setString(3, "A");
			}

			rs = pst.executeQuery();
			while (rs.next()) {
				guiaNumbers.add( rs.getString(1) );
				result.add(guiaNumbers.clone());
				guiaNumbers.clear();
			}
			
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validGuiasBokGuiaHead()_Error2:").append(e2).toString());
			}
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("validGuiasBokGuiaHead()_Error1:").append(e).toString());
			e.printStackTrace();
			mgf.setManifestNumber("");
		} finally {
			resources.closeResources(rs, pst);			
		}
		return result;
	}
	
	public int insertToComCTT(Connection con, JavManifestGenerationForm mgf, String webClientId, String BranchId, Global global) {

		PreparedStatement pst = null;
		ResultSet rs = null;
		String query1 = "";
		int insCount = 0;

		String cttNumber = null;
		String sumAmnt = null;
		String sumPack = null;
		String numOrdered = null;

		String manifestIssueDate = null;
		String collectionDate = null;

		try {
			// get the Required FormFields In Order to Insert into cmm_ctt Table...

			sumAmnt = Double.toString( mgf.getManifestAmount() );
			sumPack = Integer.toString( mgf.getSumPack() );
			numOrdered = Integer.toString( mgf.getNumOrdered() );
			manifestIssueDate = mgf.getManifestIssueDate();
			collectionDate = mgf.getCollectionTime();
			cttNumber = Integer.toString( mgf.getCttNumber() );

			query1 = cnct.delete(0,cnct.length())
						.append("INSERT INTO COM_CTT(CT_TRNS_NO, CT_TRNS_TYPE, ")
						.append("CT_TRNS_DATE, CT_PRIM_ENTY, CT_SECO_ENTY, ")
						.append("CT_TEXT_02, CT_DATE_01, CT_DATE_02,")
						.append("CT_TEXT_05, CT_TEXT_06, CT_TEXT_07,")
						.append("CT_TEXT_13, CRTD_ON, CRTD_BY, MDFD_ON,")
						.append("MDFD_BY )")
						.append(" VALUES(?,?,SYSDATE,?,?,?,to_date(?,'dd/mm/yyyy hh24:mi'),to_date(?,'dd/mm/yyyy hh24:mi'),?,?,?,?,SYSDATE,?,SYSDATE,?)").toString();

			pst = con.prepareStatement(query1);

			pst.setString(1, cttNumber);
			pst.setString(2, "IIM");
			pst.setString(3, webClientId);
			pst.setString(4, seqManifestNumber);
			pst.setString(5, sumAmnt);
			// Added by rama
			Date manIssueDate = getDate(manifestIssueDate, con);
			Date colDate = getDate(collectionDate, con);			
			pst.setDate(6, manIssueDate);
			pst.setDate(7, colDate);
			pst.setString(8, webClientId);
			pst.setString(9, numOrdered);
			pst.setString(10, sumPack);
			pst.setString(11, BranchId);
			pst.setString(12, global.getOrigenUserClave());//AAP03		
			pst.setString(13, global.getOrigenUserClave());//AAP03
			insCount = pst.executeUpdate();
			if (insCount > 0) {
				con.commit();
			}
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertToComCTT()_Error2:").append(e2).toString());
			}			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertToComCTT()_Error1:").append(e).toString());
			e.printStackTrace();
			mgf.setManifestNumber("");
		} finally {
			resources.closeResources(rs, pst);	
		}
		return insCount;
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int insertToBokGuiaStatus(Connection con,
			JavManifestGenerationForm mgf, String strGuiaNumbers,
			String webClientId) {

		String query = "";
		String gnoset = "";
		StringTokenizer st = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		int guisinsCount = 0;
		ArrayList guiaNumbers = new ArrayList();
		ArrayList guiaLocation = new ArrayList();
		ArrayList guiaDest = new ArrayList();
		int serialNumber = 0;
		String cttNumber = "";
		String strGuiaNumber = null;
		String strGuiaLocation = null;
		String strGuiaDest = null;
		try {
			query = "select GH_GUIA_NO,GH_CURR_LOCA,GH_CURR_DEST FROM BOK_GUIA_HEAD WHERE GH_GUIA_NO IN(";
			
			if (strGuiaNumbers != null || strGuiaNumbers.equals("")) {
				st = new StringTokenizer(strGuiaNumbers);
				while (st.hasMoreElements()) {
					gnoset = cnct.delete(0,cnct.length()).append( gnoset ).append( "'" ).append( st.nextToken() ).append( "'," ).toString();
				}
			}

			String newSet = gnoset.substring(0, gnoset.length() - 1);
			query = cnct.delete(0,cnct.length())
						.append(query).append(newSet).append(" ) AND GH_GUIA_TYPE = ? AND GH_ACTV_FLAG = ?").toString();

			pst = con.prepareStatement(query);
			pst.setString(1, "H");
			pst.setString(2, "A");
			
			rs = pst.executeQuery();
			
			while (rs != null && rs.next()) {
				guiaNumbers.add(rs.getString("GH_GUIA_NO"));
				guiaLocation.add((rs.getString("GH_CURR_LOCA") == null ? "" : rs.getString("GH_CURR_LOCA")));
				guiaDest.add((rs.getString("GH_CURR_DEST") == null ? "" : rs.getString("GH_CURR_DEST")));
			}

			if (rs != null) {
				rs.close();
			}
			if (pst != null) {
				pst.close();
			}

			int i = 0;
			String q = "";
			String serialNumberQuery = "";
			while (i < guiaNumbers.size()) {

				q = cnct.delete(0,cnct.length())
						.append( "INSERT INTO BOK_GUIA_STUS(GS_GUIA_NO, ")
						.append("GS_STUS_CODE, GS_SERL_NO, ")
						.append("GS_COM_TRNS_REF_CODE, CRTD_ON, CRTD_BY, ")
						.append("MDFD_ON, MDFD_BY, GS_CTT_TRNS_TYPE, ")
						.append("GS_CURR_LOCA, GS_CURR_DEST)")
						.append("VALUES(?,?,?,?,SYSDATE,?,SYSDATE,?,?,?,?)").toString();
				
				// get Max of Serial Number....from Bok Guia Status...
				serialNumberQuery = "SELECT NVL(MAX(GS_SERL_NO),0)+1 FROM BOK_GUIA_STUS WHERE GS_GUIA_NO = ?";
				strGuiaNumber = (String) guiaNumbers.get(i);
				pst = con.prepareStatement(serialNumberQuery);
				pst.setString(1, strGuiaNumber);
				rs = pst.executeQuery();

				if (rs != null && rs.next()) {
					serialNumber = rs.getInt(1);
				}
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				cttNumber = Integer.toString(mgf.getCttNumber());
				strGuiaLocation = (String) guiaLocation.get(i);
				strGuiaDest = (String) guiaDest.get(i);

				pst = con.prepareStatement(q);

				pst.setString(1, strGuiaNumber);
				pst.setString(2, "IIM");
				pst.setInt(3, serialNumber);
				pst.setString(4, cttNumber);
				pst.setString(5, webClientId);
				pst.setString(6, webClientId);
				pst.setString(7, "IIM");
				pst.setString(8, strGuiaLocation);
				pst.setString(9, strGuiaDest);

				guisinsCount = pst.executeUpdate();
				i++;
				if (pst != null) {
					pst.close();
				}
			}
			if (i > 0) {
				con.commit();
				mgf.setManifestNumber(seqManifestNumber);
			} else {
				mgf.setManifestNumber("");
			}

		} catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertToBokGuiaStatus()_Error2:").append(e2).toString());
			}			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertToBokGuiaStatus()_Error:").append(e).toString());
			e.printStackTrace();
			mgf.setManifestNumber("");
		} finally {
			resources.closeResources(rs, pst);
		}
		return guisinsCount;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getBookedGuiasDetails(Connection con,  String webClientId, String orginClientId, String formaPago) {//AAP02
		String strSqlQuery = "";
		PreparedStatement pst = null;
		CallableStatement cst = null;
		ResultSet rs = null;
		JavManifestRecord manifestRecObj = null;
		ArrayList manifestArrayList = new ArrayList();

		try {
			strSqlQuery = cnct.delete(0,cnct.length())
						.append("SELECT GH_FORM_NO,")
						.append("GH_GUIA_NO,")
						.append("GH_DEST_BRNC_ID,")
						.append("GH_DEST_CLNT_NAME,")
						.append("GH_GUIA_AMNT,")
						.append("GH_NUMB_PACK, ")
						.append("GH_ORGN_BRNC_ID ")
						.append("FROM BOK_GUIA_HEAD ")
						.append("WHERE GH_ORGN_BRNC_ID = ? AND GH_ORGN_CLNT_ID = ? AND GH_GUIA_REFR_NO IS NULL AND GH_GUIA_TYPE = ? AND ")
						.append("GH_ACTV_FLAG = ? AND GH_RAD_FLAG IN (?, ?) ")
						.toString();

			pst = con.prepareStatement(strSqlQuery);
			pst.setString(1, orginClientId);
			pst.setString(2, webClientId);
			pst.setString(3, "H");
			pst.setString(4, "A");
			pst.setString(5, "1");
			pst.setString(6, "6");

			rs = pst.executeQuery();
			
			String StrDestBrnachId = "";
			String strDestBranchName = "";
			while (rs.next()) {
				manifestRecObj = new JavManifestRecord();

				manifestRecObj.setFormNumber((rs.getString("GH_FORM_NO") == null ? "" : (rs.getString("GH_FORM_NO"))));
				manifestRecObj.setGuiaNumber((rs.getString("GH_GUIA_NO") == null ? "" : (rs.getString("GH_GUIA_NO"))));
				manifestRecObj.setGuiaAmount((rs.getString("GH_GUIA_AMNT") == null ? "" : (rs.getString("GH_GUIA_AMNT"))));
				manifestRecObj.setNumPack((rs.getString("GH_NUMB_PACK") == null ? "" : (rs.getString("GH_NUMB_PACK"))));
				manifestRecObj.setDestClientName((rs.getString("GH_DEST_CLNT_NAME") == null ? "" : (rs.getString("GH_DEST_CLNT_NAME"))));
				manifestRecObj.setOrigBranch((rs.getString("GH_ORGN_BRNC_ID") == null ? "" : (rs.getString("GH_ORGN_BRNC_ID"))));				
				
				// Get the Branch Name Using GH_DEST_BRNC_ID...
				StrDestBrnachId = rs.getString("GH_DEST_BRNC_ID");

				/* get The Branch name (use pack_web.Fun_ftch_brnc_name function
				 * � Passed parameter is GH_DEST_BRNC_ID)
				 */
				cst = con.prepareCall("begin ? := pack_web.Fun_ftch_brnc_name(?); end;");
				cst.registerOutParameter(1, Types.VARCHAR);
				cst.setString(2, StrDestBrnachId);
				cst.execute();
				strDestBranchName = (cst.getString(1) == null ? "" : cst.getString(1));

				manifestRecObj.setDestBranch(strDestBranchName);

				manifestArrayList.add(manifestRecObj);
				manifestRecObj = null;
				cst.close();
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getBookedGuiasDetails()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pst);
			resources.cerrarCallableStatement(cst);
		}
		return manifestArrayList;
	}
	
	public Date getDate(String datestring, Connection con) {
		ResultSet rs = null;
		PreparedStatement pstm = null;
		Date retValue = null;		
		try {
			pstm = con.prepareStatement("SELECT	TO_DATE(?,'dd/mm/yyyy hh24:mi') FROM DUAL");
			pstm.setString(1, datestring);
			rs = pstm.executeQuery();
			while (rs.next()) {
				retValue = rs.getDate(1);
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDate()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.closeResources(rs, pstm);
		}

		return retValue;
	}

	 // Added by Sam.D.Jabeen on [31-May-2006]
	public String getBranchLocationType(String strSiteId, Connection con) {

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		String strBranchLocType = null;

		String query = "SELECT BM_BRNC_LOC_TYPE FROM SYS_BRNC_MSTR WHERE BM_BRNC_ID like ?";

		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1,cnct.delete(0,cnct.length()).append(strSiteId).append("%").toString());

			rs = pstmt.executeQuery();

			if (rs.next()) {
				strBranchLocType = rs.getString(1);
			}

		} catch (SQLException sqlException) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getBranchLocationType()_Error1:").append(sqlException).toString());
			sqlException.printStackTrace();
		} catch (Exception exception) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getBranchLocationType()_Error2:").append(exception).toString());
			exception.printStackTrace();
		} finally {
			resources.closeResources(rs, pstmt);
		}
		return strBranchLocType;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getManifestDetails(Connection con,
			JavManifestGenerationForm mgf, String webClientId,
			String orginBrncId, HttpServletRequest request, Global global) {

		String strSqlQuery = "";
		String strGuiaNo = "";// added by sundar on 30/07/2003 to handle Manifestgeneration using checking all
		
		String usuario = global.getOrigenUserClave();
		
		PreparedStatement pst = null;
		CallableStatement cst = null;
		ResultSet rs = null;
		JavManifestRecord manifestRecObj = null;
		ArrayList manifestArrayList = new ArrayList();

		int totRec = 0;
		int currentRec = 0;
		int endRec = 0;
		double maxPages = 0;
		int count = 1;// added by sundar on 30/07/2003 to handle Manifestgeneration using checking all
		
		String[] params= new String[]{};
		try {
			/*condicion PARA SELECCIONAR LA INFORMACION DE TODOS LOS USUARIOS*/
			if (mgf.getFiltroPor().equals("1")) {// filtra todas las direccion de centros de costo a donde tiene acceso el usuario
				strSqlQuery = cnct.delete(0,cnct.length())
					.append("SELECT GH_FORM_NO, GH_GUIA_NO, GH_DEST_BRNC_ID, GH_DEST_CLNT_NAME, to_char(GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT, GH_NUMB_PACK , PACK_WEB.fun_ftch_ccosto_guia(GH_GUIA_NO), GH_ORGN_BRNC_ID ")
					.append("FROM BOK_GUIA_HEAD A, BOK_GUIA_ADDR B, BOK_GUIA_HEAD_EXTRA C ") 
					.append("WHERE A.GH_ORGN_CLNT_ID = ? ") 
					.append("AND A.GH_GUIA_TYPE = ? ")
					.append("AND A.GH_ACTV_FLAG = ? ") 
					.append("AND A.GH_RAD_FLAG IN (?, ?) ") 
					.append("AND A.GH_GUIA_REFR_NO IS NULL ")
					.append("AND A.GH_GUIA_NO = B.GA_GUIA_NO ")
					.append("AND B.GA_ADDR_TYPE = ? ")
					.append("AND B.GA_ADDR_CODE = ? ")
					.append("and GH_GUIA_NO = GE_GUIA_NO ")
					.append("and GE_CLNT_ID = ? ")
					.append("AND GE_CLNT_CCOSTO_ID IN ( SELECT CU_CCOSTO_ID FROM SYS_CLNT_CCOSTO_USER WHERE CU_CLNT_ID = ? AND CU_USER_ID = ?) ")
					.toString();
				pst = con.prepareStatement(strSqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				pst.setString(1, webClientId);				
				pst.setString(2, "H");
				pst.setString(3, "A");
				pst.setString(4, "1");
				pst.setString(5, "6");
				pst.setString(6, "ORIGIN");
				pst.setString(7, mgf.getPreferedAddressCode());//codigo direccion
				pst.setString(8, webClientId);
				pst.setString(9, webClientId);
				pst.setString(10, usuario);
				params= new String[]{webClientId, "H", "A", "1", "6", "ORIGIN", webClientId, webClientId, usuario};

			} else if(mgf.getFiltroPor().equals("3")) {//filtro para buscar guias WE generadas por el convenio de clientes //EEMP
				
				strSqlQuery = cnct.delete(0,cnct.length())
						.append("SELECT GH_FORM_NO, GH_GUIA_NO, GH_DEST_BRNC_ID, GH_DEST_CLNT_NAME, to_char(GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT, GH_NUMB_PACK , PACK_WEB.fun_ftch_ccosto_guia(GH_GUIA_NO), GH_ORGN_BRNC_ID ")
						.append("FROM BOK_GUIA_HEAD A, BOK_GUIA_ADDR B, BOK_GUIA_HEAD_EXTRA C ") 
						.append("WHERE A.GH_GUIA_TYPE = ? ")
						.append("AND A.GH_ACTV_FLAG = ? ") 
						.append("AND A.GH_RAD_FLAG IN (?, ?) ") 
						.append("AND A.GH_GUIA_REFR_NO IS NULL ")
						.append("AND A.GH_GUIA_NO = B.GA_GUIA_NO ")
						.append("AND B.GA_GUIA_NO = C.GE_GUIA_NO ")
						.append("AND B.GA_ADDR_TYPE = ? ")
						.append("AND B.GA_ADDR_CODE = ? ")
						.append("AND C.GE_CLNT_REQT = ? ")
						.append("AND substr(A.GH_FORM_NO,0,2) = ? ")
						.toString();
					
					pst = con.prepareStatement(strSqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					pst.setString(1, "H");
					pst.setString(2, "A");
					pst.setString(3, "1");
					pst.setString(4, "6");
					pst.setString(5, "ORIGIN");
					pst.setString(6, mgf.getPreferedAddressCode());//codigo direccion
					pst.setString(7, global.getClientIdAgreement());	
					pst.setString(8, "WE");
					params= new String[]{"H", "A", "1", "6", "ORIGIN", mgf.getPreferedAddressCode(), global.getClientIdAgreement(), "WE"};

			}else { // filtra las direccion de centros de costo donde solo ha generado gu�as el usuario
				strSqlQuery = cnct.delete(0,cnct.length())
					.append("SELECT GH_FORM_NO, GH_GUIA_NO, GH_DEST_BRNC_ID, GH_DEST_CLNT_NAME, to_char(GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT, GH_NUMB_PACK , PACK_WEB.fun_ftch_ccosto_guia(GH_GUIA_NO), GH_ORGN_BRNC_ID ")
					.append("FROM BOK_GUIA_HEAD A, BOK_GUIA_ADDR B ") 
					.append("WHERE A.GH_ORGN_CLNT_ID = ?")
					.append("AND A.GH_GUIA_TYPE = ? ")
					.append("AND A.GH_ACTV_FLAG = ? ") 
					.append("AND A.GH_RAD_FLAG IN (?, ?) ") 
					.append("AND A.GH_GUIA_REFR_NO IS NULL ")
					.append("AND A.CRTD_BY = ? ")
					.append("AND A.GH_GUIA_NO = B.GA_GUIA_NO ")
					.append("AND B.GA_ADDR_TYPE = ? ")
					.append("AND B.GA_ADDR_CODE = ? ")
					.toString();
				
				
				pst = con.prepareStatement(strSqlQuery, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				
				pst.setString(1, webClientId);
				pst.setString(2, "H");
				pst.setString(3, "A");
				pst.setString(4, "1");
				pst.setString(5, "6");
				pst.setString(6, usuario);
				pst.setString(7, "ORIGIN");
				pst.setString(8, mgf.getPreferedAddressCode());//codigo direccion	
					
				params= new String[]{webClientId, "H", "A", "1", "6", usuario, "ORIGIN", mgf.getPreferedAddressCode()};
			}
			rs = pst.executeQuery();	
			boolean rsFlag = rs.next();

			// count the total number of Records.
			if (rsFlag) {
				rs.last();
				totRec = rs.getRow();
				count = mgf.getTotalRecord();// added by sundar on 30/07/2003 to handle Manifestgeneration using checking all
				mgf.setTotalRecord(totRec);
				rs.beforeFirst();
				mgf.setNoDataFound("N");
				request.setAttribute("sDataFlag", "No");
			} else if (!rsFlag) {
				mgf.setNoDataFound("Y");
				request.setAttribute("sDataFlag", "Yes");
			}
					
			if ((totRec % 10) == 0) {
				maxPages = totRec / 10;
			} else {
				maxPages = (totRec / 10) + 1;
			}
			
			mgf.setMaxPageIndex(maxPages);

			currentRec = mgf.getCurRow();
			endRec = mgf.getEndRow();

			if (endRec == 0) {
				endRec = 10;
				mgf.setEndRow(endRec);
			}

			if (currentRec == 0) {
				currentRec = 1;
				mgf.setCurRow(currentRec);
			}

			// added by sundar on 30/07/2003 to handle Manifestgeneration using
			// checking all
			rs.beforeFirst();

			if (count == 0) {
				while (rs.next()) {
					manifestRecObj = new JavManifestRecord();
					manifestRecObj.setGuiaNumber((rs.getString("GH_GUIA_NO") == null ? "" : (rs.getString("GH_GUIA_NO"))));
					strGuiaNo += (rs.getString("GH_GUIA_NO") == null ? "" : (rs.getString("GH_GUIA_NO"))) + " ";
				}
				mgf.setClickedItems(strGuiaNo);
			}

			rs.beforeFirst();
			String strDestBranchName = "";
			while (rs.next()) {
				if (currentRec > endRec) {
					break;
				}
				rs.absolute(currentRec);
				manifestRecObj = new JavManifestRecord();

				manifestRecObj.setFormNumber((rs.getString("GH_FORM_NO") == null ? "" : (rs.getString("GH_FORM_NO"))));
				manifestRecObj.setGuiaNumber((rs.getString("GH_GUIA_NO") == null ? "" : (rs.getString("GH_GUIA_NO"))));
				manifestRecObj.setGuiaAmount((rs.getString("GH_GUIA_AMNT") == null ? "" : (rs.getString("GH_GUIA_AMNT"))));
				manifestRecObj.setNumPack((rs.getString("GH_NUMB_PACK") == null ? "" : (rs.getString("GH_NUMB_PACK"))));
				manifestRecObj.setDestClientName((rs.getString("GH_DEST_CLNT_NAME") == null ? "" : (rs.getString("GH_DEST_CLNT_NAME"))));
				manifestRecObj.setOrigBranch((rs.getString("GH_ORGN_BRNC_ID") == null ? "" : (rs.getString("GH_ORGN_BRNC_ID"))));
				
				// Get the Branch Name Using GH_DEST_BRNC_ID...
				String StrDestBrnachId = rs.getString("GH_DEST_BRNC_ID");

				/*
				 * get The Branch name (use pack_web.Fun_ftch_brnc_name
				 * function � Passed parameter is GH_DEST_BRNC_ID)
				 */
				cst = con.prepareCall("begin ? := pack_web.Fun_ftch_brnc_name(?); end;");
				cst.registerOutParameter(1, Types.VARCHAR);
				cst.setString(2, StrDestBrnachId);
				cst.execute();
				
				strDestBranchName = (cst.getString(1) == null ? "" : cst.getString(1));					
				manifestRecObj.setDestBranch(strDestBranchName);		

				manifestArrayList.add(manifestRecObj);
				manifestRecObj = null;
				currentRec++;
				cst.close();
			}
				
		} catch (Exception e) {
			if (e instanceof SQLException && params.length > 0) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append("getManifestDetails()_Error:")
						.append("Params:").append( Arrays.toString(params))
						.append(" Query:").append(strSqlQuery).append(". ")
						.append(e).toString());
			}else {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getManifestDetails()_Error:").append(e).toString());
				e.printStackTrace();
			}
			mgf.setManifestNumber("");
		} finally {
			resources.closeResources(rs, pst);
			resources.cerrarCallableStatement(cst);
		}
		return manifestArrayList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getCentrosCostoPorUsuario(Connection con,	JavManifestGenerationForm mgf, String webClientId,	String orginBrnchClientId, Global global) {

		String strSqlQuery = "";
		String strSubSqlQuery = "";		
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList centrosCostoArrayList = new ArrayList();
		HashMap centroCosto = new HashMap(2);

		try {
			
			strSubSqlQuery = cnct.delete(0, cnct.length())
					.append("SELECT CC_CCOSTO_ID, CC_CCOSTO_NAME ")
					.append("FROM SYS_CLNT_CCOSTO ")
					.append("WHERE CC_CLNT_ID = ? ")
					.append("AND CC_CCOSTO_ID IN (")
						.append("SELECT GE_CLNT_CCOSTO_ID FROM BOK_GUIA_HEAD_EXTRA WHERE GE_GUIA_NO IN (")
							.append("SELECT GH_GUIA_NO ")
							.append("FROM BOK_GUIA_HEAD ")
							.append("WHERE GH_ORGN_BRNC_ID = ? ")
							.append("AND GH_ORGN_CLNT_ID = ? ")
							.append("AND GH_GUIA_REFR_NO IS NULL ")
							.append("AND GH_GUIA_TYPE = ? ")
							.append("AND GH_ACTV_FLAG = ? ")							
							.append("AND GH_RAD_FLAG IN (?, ?) ")
							.append("AND PACK_WEB.FUN_BRDR_BRNC(GH_DEST_BRNC_ID) = ? ")
							.append("AND GH_PYMT_MODE = ? ")
							.append("AND GH_DOCU_TYPE = ? ")
							.append("AND CRTD_BY = ? ")
						.append(")")
					.append(") ORDER BY CC_CCOSTO_ID")
					.toString();
			
			strSqlQuery = cnct.delete(0, cnct.length())
					.append("SELECT CC_CCOSTO_ID, CC_CCOSTO_NAME ")
					.append("FROM SYS_CLNT_CCOSTO ")
					.append("WHERE CC_CLNT_ID = ? ")
					.append("AND CC_CCOSTO_ID IN (")
						.append("SELECT GE_CLNT_CCOSTO_ID FROM BOK_GUIA_HEAD_EXTRA WHERE GE_GUIA_NO IN (")
							.append("SELECT GH_GUIA_NO ")
							.append("FROM BOK_GUIA_HEAD ")
							.append("WHERE GH_ORGN_BRNC_ID = ? ")
							.append("AND GH_ORGN_CLNT_ID = ? ")
							.append("AND GH_GUIA_REFR_NO IS NULL ")
							.append("AND GH_GUIA_TYPE = ? ")
							.append("AND GH_ACTV_FLAG = ? ")
							.append("AND GH_RAD_FLAG IN (?, ?) ")
							.append("AND GH_PYMT_MODE = ? ")
							.append("AND GH_DOCU_TYPE = ? ")
							.append("AND CRTD_BY = ? ")
						.append(")")
					.append(") ORDER BY CC_CCOSTO_ID")
					.toString();
			
			if (mgf.getManifiestoType().length() <= 0) {
				
				pst = con.prepareStatement(strSqlQuery);
				pst.setString(1, webClientId);
				pst.setString(2, orginBrnchClientId);
				pst.setString(3, webClientId);
				pst.setString(4, "H");
				pst.setString(5, "A");
				pst.setString(6, "1");
				pst.setString(7, "6");
				pst.setString(8, mgf.getFormaPago());
				pst.setString(9, mgf.getDocuType());
				pst.setString(10, global.getOrigenUserClave());
			} else {
				
				pst = con.prepareStatement(strSubSqlQuery);

				pst.setString(1, webClientId);
				pst.setString(2, orginBrnchClientId);
				pst.setString(3, webClientId);
				pst.setString(4, "H");
				pst.setString(5, "A");
				pst.setString(6, "1");			
				pst.setString(7, "6");
				pst.setString(8, mgf.getManifiestoType());
				pst.setString(9, mgf.getFormaPago());
				pst.setString(10, mgf.getDocuType());
				pst.setString(11, global.getOrigenUserClave());
			}

			rs = pst.executeQuery();

			while (rs.next()) {
				centroCosto.put("clave", rs.getString(1) == null ? "" : rs.getString(1));
				centroCosto.put("descr", rs.getString(2) == null ? "" : rs.getString(2));
				centrosCostoArrayList.add(centroCosto.clone());
				centroCosto.clear();
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCentrosCostoPorUsuario()_Error:").append(e).toString());
			e.printStackTrace();
			centrosCostoArrayList = new ArrayList(0);
		} finally {
			resources.closeResources(rs, pst);
		}
		return centrosCostoArrayList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList getCentrosCostoRelTodosUsuario(Connection con,
			JavManifestGenerationForm mgf, String webClientId,
			String orginBrnchClientId, Global global) {

		String strSqlQuery = "";
		String strSubSqlQuery = "";	
		
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList centrosCostoArrayList = new ArrayList();
		HashMap centroCosto = new HashMap(2);

		try {			
			strSubSqlQuery = cnct.delete(0, cnct.length())
					.append("SELECT CC_CCOSTO_ID, CC_CCOSTO_NAME ") 
					.append("FROM SYS_CLNT_CCOSTO ")  
					.append("WHERE CC_CLNT_ID = ? ")
					.append("AND CC_CCOSTO_ID IN (")
					    .append("SELECT b.GE_CLNT_CCOSTO_ID ")
					        .append("FROM bok_guia_head a, bok_guia_head_extra b ")
					       .append("WHERE ")
					         .append("a.gh_orgn_brnc_id = ? ")
					         .append("AND a.gh_orgn_clnt_id = ? ")
					         .append("AND a.gh_guia_refr_no IS NULL ")
					         .append("AND a.GH_GUIA_TYPE = ? ")
					         .append("AND a.GH_ACTV_FLAG = ? ")
					         .append("AND GH_RAD_FLAG IS NOT NULL ")
					         .append("AND PACK_WEB.FUN_BRDR_BRNC(GH_DEST_BRNC_ID) = ? ")
					         .append("AND a.GH_PYMT_MODE = ? ")
					         .append("AND a.GH_DOCU_TYPE = ? ") 
					         .append("AND a.GH_GUIA_NO = b.GE_GUIA_NO ")
					         .append("and b.GE_CLNT_CCOSTO_ID in (")
					              .append("SELECT CU_CCOSTO_ID ") 
					                  .append("FROM SYS_CLNT_CCOSTO_USER ") 
					                 .append("WHERE ") 
					                  .append("CU_CLNT_ID = ? ") 
					                  .append("AND CU_USER_ID = ? ")
					              .append(") ")
					.append(") ORDER BY CC_CCOSTO_ID").toString();
			
			strSqlQuery =  cnct.delete(0, cnct.length())
					.append("SELECT CC_CCOSTO_ID, CC_CCOSTO_NAME ") 
					.append("FROM SYS_CLNT_CCOSTO ")  
					.append("WHERE CC_CLNT_ID = ? ")
					.append("AND CC_CCOSTO_ID IN (")
					    .append("SELECT b.GE_CLNT_CCOSTO_ID ")
					        .append("FROM bok_guia_head a, bok_guia_head_extra b ")
					       .append("WHERE ")
					         .append("a.gh_orgn_brnc_id = ? ")
					         .append("AND a.gh_orgn_clnt_id = ? ")
					         .append("AND a.gh_guia_refr_no IS NULL ")
					         .append("AND a.GH_GUIA_TYPE = ? ")
					         .append("AND a.GH_ACTV_FLAG = ? ")
					         .append("AND GH_RAD_FLAG IS NOT NULL ")
					         .append("AND a.GH_PYMT_MODE = ? ")
					         .append("AND a.GH_DOCU_TYPE = ? ") 
					         .append("AND a.GH_GUIA_NO = b.GE_GUIA_NO ")
					         .append("and b.GE_CLNT_CCOSTO_ID in (")
					              .append("SELECT CU_CCOSTO_ID ") 
					                  .append("FROM SYS_CLNT_CCOSTO_USER ") 
					                 .append("WHERE ") 
					                  .append("CU_CLNT_ID = ? ") 
					                  .append("AND CU_USER_ID = ? ")
					              .append(") ")
					.append(") ORDER BY CC_CCOSTO_ID").toString();
			
			if (mgf.getManifiestoType().length() <= 0) {
				
				pst = con.prepareStatement(strSqlQuery);
				pst.setString(1, webClientId);
				pst.setString(2, orginBrnchClientId);
				pst.setString(3, webClientId);
				pst.setString(4, "H");
				pst.setString(5, "A");
				pst.setString(6, mgf.getFormaPago());
				pst.setString(7, mgf.getDocuType());
				pst.setString(8, webClientId);
				pst.setString(9, global.getOrigenUserClave());
			} else {				
				pst = con.prepareStatement(strSubSqlQuery);

				pst.setString(1, webClientId);
				pst.setString(2, orginBrnchClientId);
				pst.setString(3, webClientId);
				pst.setString(4, "H");
				pst.setString(5, "A");
				pst.setString(6, mgf.getManifiestoType());
				pst.setString(7, mgf.getFormaPago());
				pst.setString(8, mgf.getDocuType());
				pst.setString(9, webClientId);
				pst.setString(10, global.getOrigenUserClave());
			}

			rs = pst.executeQuery();

			while (rs.next()) {
				centroCosto.put("clave", rs.getString(1) == null ? "" : rs.getString(1));
				centroCosto.put("descr", rs.getString(2) == null ? "" : rs.getString(2));
				centrosCostoArrayList.add(centroCosto.clone());
				centroCosto.clear();
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getCentrosCosto()_Error:").append(e).toString());
			e.printStackTrace();
			centrosCostoArrayList = new ArrayList(0);
		} finally {
			resources.closeResources(rs, pst);
		}
		return centrosCostoArrayList;
	}
	
	
	private void insertReferenceRecord(Connection con, HttpSession session,
			String genMnftNumber, String reference, String docuType) {//AAP02
		PreparedStatement pst = null;
		try {
			Global global = (Global) session.getAttribute("sGlobal");

			String insertQuery = "INSERT INTO WEB_GUIA_REFR(GR_GUIA_NO, GR_GUIA_TYPE, GR_DOCU_TYPE, GR_GUIA_REFR, CRTD_ON, CRTD_BY, MDFD_ON, MDFD_BY) VALUES(?,?,?,?,SYSDATE,?,SYSDATE,?)";

			pst = con.prepareStatement(insertQuery);
			pst.setString(1, genMnftNumber);
			pst.setString(2, "H");
			pst.setString(3, docuType);
			pst.setString(4, reference.toUpperCase());
			pst.setString(5, global.getOrigenUserClave());//AAP03

			pst.setString(6, global.getOrigenUserClave());//AAP03
			int insertCount = pst.executeUpdate();

			if (insertCount > 0) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertReferenceRecord()_").append(global.clientId).append("WEB GUIA REFR INSERT SCUCESS").toString());
			} else {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgAvi).append("insertReferenceRecord()_").append(global.clientId).append("WEB GUIA REFR INSERT FALILURE").toString());
			}			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("insertReferenceRecord()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			resources.cerrarPreparedStatement(pst);
		}
		
	}
}