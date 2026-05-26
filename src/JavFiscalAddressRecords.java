import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;

public class JavFiscalAddressRecords {
	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();

	public ArrayList getLovRecords(Connection con, String clientId) {
		ArrayList result = new ArrayList();
		HashMap values = new HashMap(25);
		PreparedStatement psmt = null;
		PreparedStatement psmt1 = null;
		ResultSet rs = null;
		ResultSet rsAddress = null;
		CallableStatement cst = null;
		String groupId = null;
		String query = null;
		String addrType = "CLNT";

		String AM_ADDR_CODE = null, AM_DRNR = null, AM_STRT_NAME = null, AM_PHNO1 = null, AM_SUIT_NO = null, AM_FLOR_NO = null, AM_ADDR_STYP = null, AM_ADDR_DEFN_TYPE = null, AM_ADDR_REF_NO = null, AM_GETY_LEVL = null, AM_GETY_TYPE = null, AM_GETY_CODE = null;
		String u11 = null, u12 = null, u13 = null, u14 = null, u15 = null, u16 = null, u17 = null;
		String c11 = null, c12 = null, c13 = null, c14 = null, c15 = null, c16 = null, zipcode = null;

		try {
			String groupIdQuery = "select pack_web.FUN_CHCK_GRUP_CLNT(?)as groupid from dual";
			psmt = con.prepareStatement(groupIdQuery);
			psmt.setString(1, clientId);
			rs = psmt.executeQuery();
			while (rs.next()) {
				groupId = rs.getString("groupid");
			}
			if (!(clientId.equalsIgnoreCase(groupId))) {
				query = cnct.delete(0, cnct.length())
						.append("SELECT AM_ADDR_CODE, AM_DRNR, AM_STRT_NAME, ")
						.append("AM_PHNO1, AM_SUIT_NO, AM_FLOR_NO, ")
						.append("AM_ADDR_STYP, AM_ADDR_DEFN_TYPE, ")
						.append("AM_ADDR_REF_NO, AM_GETY_LEVL, ")
						.append("AM_GETY_TYPE, AM_GETY_CODE ")
						.append("FROM SYS_ADDR_MSTR WHERE AM_ENTY_ID in (?,?) AND AM_ADDR_TYPE = ?")
						.toString();

				psmt1 = con.prepareStatement(query);
				psmt1.setString(1, clientId);
				psmt1.setString(2, groupId);
				psmt1.setString(3, addrType);

			} else {
				query = cnct.delete(0, cnct.length())
						.append("SELECT AM_ADDR_CODE,AM_DRNR,AM_STRT_NAME, ")
						.append("AM_PHNO1, AM_SUIT_NO, AM_FLOR_NO, ")
						.append("AM_ADDR_STYP, AM_ADDR_DEFN_TYPE, ")
						.append("AM_ADDR_REF_NO, AM_GETY_LEVL, ")
						.append("AM_GETY_TYPE, AM_GETY_CODE ")
						.append("FROM SYS_ADDR_MSTR WHERE AM_ENTY_ID = ? ")
						.append("AND AM_ADDR_TYPE = ?").toString();
				
				psmt1 = con.prepareStatement(query);
				psmt1.setString(1, clientId);
				psmt1.setString(2, addrType);
			}
			rsAddress = psmt1.executeQuery();
			while (rsAddress.next()) {
				AM_ADDR_CODE = rsAddress.getString(1) == null ? "" : rsAddress.getString(1);
				AM_DRNR = rsAddress.getString(2) == null ? "" : rsAddress.getString(2);
				AM_STRT_NAME = rsAddress.getString(3) == null ? "" : rsAddress.getString(3);
				AM_PHNO1 = rsAddress.getString(4) == null ? "" : rsAddress.getString(4);
				AM_SUIT_NO = rsAddress.getString(5) == null ? "" : rsAddress.getString(5);
				AM_FLOR_NO = rsAddress.getString(6) == null ? "" : rsAddress.getString(6);
				AM_ADDR_STYP = rsAddress.getString(7) == null ? "" : rsAddress.getString(7);
				AM_ADDR_DEFN_TYPE = rsAddress.getString(8) == null ? "" : rsAddress.getString(8);
				AM_ADDR_REF_NO = rsAddress.getString(9) == null ? "" : rsAddress.getString(9);
				AM_GETY_LEVL = rsAddress.getString(10) == null ? "" : rsAddress.getString(10);
				AM_GETY_TYPE = rsAddress.getString(11) == null ? "" : rsAddress.getString(11);
				AM_GETY_CODE = rsAddress.getString(12) == null ? "" : rsAddress.getString(2);

				cst = con.prepareCall("{call pack_web.pro_ftch_addr(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

				cst.setString(1, AM_ADDR_DEFN_TYPE);
				cst.setString(2, AM_ADDR_REF_NO);
				cst.setString(3, AM_GETY_CODE);
				cst.setString(4, AM_GETY_LEVL);
				cst.setString(5, AM_GETY_TYPE);

				cst.registerOutParameter(6, Types.VARCHAR);
				cst.registerOutParameter(7, Types.VARCHAR);
				cst.registerOutParameter(8, Types.VARCHAR);
				cst.registerOutParameter(9, Types.VARCHAR);
				cst.registerOutParameter(10, Types.VARCHAR);
				cst.registerOutParameter(11, Types.VARCHAR);
				cst.registerOutParameter(12, Types.VARCHAR);
				cst.registerOutParameter(13, Types.VARCHAR);
				cst.registerOutParameter(14, Types.VARCHAR);
				cst.registerOutParameter(15, Types.VARCHAR);
				cst.registerOutParameter(16, Types.VARCHAR);
				cst.registerOutParameter(17, Types.VARCHAR);
				cst.registerOutParameter(18, Types.VARCHAR);
				cst.registerOutParameter(19, Types.VARCHAR);

				cst.executeQuery();

				u11 = (cst.getString(6) != null ? cst.getString(6) : "");
				u12 = (cst.getString(7) != null ? cst.getString(7) : "");
				u13 = (cst.getString(8) != null ? cst.getString(8) : "");
				u14 = (cst.getString(9) != null ? cst.getString(9) : "");
				u15 = (cst.getString(10) != null ? cst.getString(10) : "");
				u16 = (cst.getString(11) != null ? cst.getString(11) : "");
				u17 = (cst.getString(12) != null ? cst.getString(12) : "");
				zipcode = (cst.getString(13) != null ? cst.getString(13) : "");
				c11 = (cst.getString(14) != null ? cst.getString(14) : "");
				c12 = (cst.getString(15) != null ? cst.getString(15) : "");
				c12 = (cst.getString(16) != null ? cst.getString(16) : "");
				c12 = (cst.getString(17) != null ? cst.getString(17) : "");
				c12 = (cst.getString(18) != null ? cst.getString(18) : "");
				c12 = (cst.getString(19) != null ? cst.getString(19) : "");

				values.put("AM_ADDR_CODE", AM_ADDR_CODE);
				values.put("AM_DRNR", AM_DRNR);
				values.put("AM_STRT_NAME", AM_STRT_NAME);
				values.put("AM_PHNO1", AM_PHNO1);
				values.put("AM_SUIT_NO", AM_SUIT_NO);
				values.put("AM_FLOR_NO", AM_FLOR_NO);
				values.put("AM_ADDR_STYP", AM_ADDR_STYP);
				values.put("AM_ADDR_DEFN_TYPE", AM_ADDR_DEFN_TYPE);
				values.put("AM_GETY_LEVL", AM_GETY_LEVL);
				values.put("AM_GETY_TYPE", AM_GETY_TYPE);
				values.put("AM_GETY_CODE", AM_GETY_CODE);
				values.put("u11", u11);
				values.put("u12", u12);
				values.put("u13", u13);
				values.put("u14", u14);
				values.put("u15", u15);
				values.put("u16", u16);
				values.put("u17", u17);
				values.put("Zipcode", zipcode);
				values.put("c11", c11);
				values.put("c12", c12);
				values.put("c13", c13);
				values.put("c14", c14);
				values.put("c15", c15);
				values.put("c16", c16);
				result.add(values.clone());
				values.clear();
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecords()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rsAddress != null) {
					rsAddress.close();
				}
				if (psmt != null) {
					psmt.close();
				}
				if (psmt1 != null) {
					psmt1.close();
				}
				if (cst != null) {
					cst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getLovRecords()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
	
	public ArrayList getClientrecords(Connection con, String siteId,
			String clientId) throws Exception {
		HashMap values = null;
		ArrayList result = new ArrayList();
		CallableStatement cst = null;
		PreparedStatement psmt = null;
		PreparedStatement psmt1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		// String query =
		// "select pack_web.fun_ftch_clnt_name(WO_DEST_CLNT_ID),"+
		// "WO_DEST_CLNT_ID from web_orgn_dest_clnt "+
		// "where WO_ORGN_CLNT_ID =?"+
		// "and WO_DEST_SITE_ID =? order by 1";//condition added by B.Emerson on
		// 20/06/2003
		String AM_ADDR_CODE = null, AM_DRNR = null, AM_STRT_NAME = null, AM_PHNO1 = null, AM_ADDR_STYP = null, AM_ADDR_DEFN_TYPE = null, AM_ADDR_REF_NO = null, AM_GETY_LEVL = null, AM_GETY_TYPE = null, AM_GETY_CODE = null;
		try {
			String query = "SELECT CM_CLNT_ID, CM_CLNT_NAME,AM_STRT_NAME,"
					+ "AM_GETY_CODE, AM_GETY_LEVL, AM_GETY_TYPE, AM_ADDR_CODE, AM_PHNO1,"
					+ "AM_DRNR, AM_ADDR_STYP, AM_ADDR_REF_NO, AM_ADDR_DEFN_TYPE FROM SYS_CLNT_VIEW WHERE "
					+ "AM_PE_BRNC_ID = ? AND CM_CLNT_ID = ?";
			psmt = con.prepareStatement(query);
			//AccessLog.Log(query);
			//AccessLog.Log(siteId);
			psmt.setString(1, siteId);
			//AccessLog.Log(clientId);
			clientId = clientId.trim();

			psmt.setString(2, clientId);
			rs = psmt.executeQuery();
			String clientName1 = "";

			String u11 = null, u12 = null, u13 = null, u14 = null, u15 = null, u16 = null, u17 = null;
			String c11 = null, c12 = null, c13 = null, c14 = null, c15 = null, c16 = null, c17 = null, zipcode = null;
			
			while (rs.next()) {
				clientId = rs.getString(1);
				clientName1 = rs.getString(2);
				AM_STRT_NAME = rs.getString(3);
				AM_GETY_CODE = rs.getString(4);
				AM_GETY_LEVL = rs.getString(5);
				AM_GETY_TYPE = rs.getString(6);
				AM_ADDR_CODE = rs.getString(7);
				AM_PHNO1 = rs.getString(8);
			
				AM_DRNR = rs.getString(9);
				AM_ADDR_STYP = rs.getString(10);
				AM_ADDR_REF_NO = rs.getString(11);
				AM_ADDR_DEFN_TYPE = rs.getString(12);
				// AccessLog.Log("inside where loop"+AM_DRNR);

				AM_ADDR_CODE = (AM_ADDR_CODE != null ? AM_ADDR_CODE : "");
				AM_DRNR = (AM_DRNR != null ? AM_DRNR : "");
				AM_STRT_NAME = (AM_STRT_NAME != null ? AM_STRT_NAME : "");
				AM_PHNO1 = (AM_PHNO1 != null ? AM_PHNO1 : "");
				// AM_SUIT_NO =(AM_SUIT_NO!=null?AM_SUIT_NO:"");
				// AM_FLOR_NO =(AM_FLOR_NO!=null?AM_FLOR_NO:"");
				AM_ADDR_STYP = (AM_ADDR_STYP != null ? AM_ADDR_STYP : "");
				AM_ADDR_DEFN_TYPE = (AM_ADDR_DEFN_TYPE != null ? AM_ADDR_DEFN_TYPE : "");
				AM_ADDR_REF_NO = (AM_ADDR_REF_NO != null ? AM_ADDR_REF_NO : "");
				AM_GETY_LEVL = (AM_GETY_LEVL != null ? AM_GETY_LEVL : "");
				AM_GETY_TYPE = (AM_GETY_TYPE != null ? AM_GETY_TYPE : "");
				AM_GETY_CODE = (AM_GETY_CODE != null ? AM_GETY_CODE : "");

				cst = con.prepareCall("{call pack_web.pro_ftch_addr(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

				cst.setString(1, AM_ADDR_DEFN_TYPE);
				cst.setString(2, AM_ADDR_REF_NO);
				cst.setString(3, AM_GETY_CODE);
				cst.setString(4, AM_GETY_LEVL);
				cst.setString(5, AM_GETY_TYPE);

				cst.registerOutParameter(6, Types.VARCHAR);
				cst.registerOutParameter(7, Types.VARCHAR);
				cst.registerOutParameter(8, Types.VARCHAR);
				cst.registerOutParameter(9, Types.VARCHAR);
				cst.registerOutParameter(10, Types.VARCHAR);
				cst.registerOutParameter(11, Types.VARCHAR);
				cst.registerOutParameter(12, Types.VARCHAR);
				cst.registerOutParameter(13, Types.VARCHAR);
				cst.registerOutParameter(14, Types.VARCHAR);
				cst.registerOutParameter(15, Types.VARCHAR);
				cst.registerOutParameter(16, Types.VARCHAR);
				cst.registerOutParameter(17, Types.VARCHAR);
				cst.registerOutParameter(18, Types.VARCHAR);
				cst.registerOutParameter(19, Types.VARCHAR);

				cst.executeQuery();

				// AccessLog.Log("inside where cst.executeQuery();");
				u11 = (cst.getString(6) != null ? cst.getString(6) : "");
				u12 = (cst.getString(7) != null ? cst.getString(7) : "");
				u13 = (cst.getString(8) != null ? cst.getString(8) : "");
				u14 = (cst.getString(9) != null ? cst.getString(9) : "");
				u15 = (cst.getString(10) != null ? cst.getString(10) : "");
				u16 = (cst.getString(11) != null ? cst.getString(11) : "");
				u17 = (cst.getString(12) != null ? cst.getString(12) : "");
				zipcode = (cst.getString(13) != null ? cst.getString(13) : "");
				c11 = (cst.getString(14) != null ? cst.getString(14) : "");
				c12 = (cst.getString(15) != null ? cst.getString(15) : "");
				c13 = (cst.getString(16) != null ? cst.getString(16) : "");
				c14 = (cst.getString(17) != null ? cst.getString(17) : "");
				c15 = (cst.getString(18) != null ? cst.getString(18) : "");
				c16 = (cst.getString(19) != null ? cst.getString(19) : "");

				/*
				 * AccessLog.Log("CST 6 "+cst.getString(6));
				 * AccessLog.Log("CST 7 "+cst.getString(7));
				 * AccessLog.Log("CST 8 "+cst.getString(8));
				 * AccessLog.Log("CST 9 "+cst.getString(9));
				 * AccessLog.Log("CST 10 "+cst.getString(10));
				 * AccessLog.Log("CST 11 "+cst.getString(11));
				 * AccessLog.Log("CST 12 "+cst.getString(12));
				 * AccessLog.Log("CST 13 "+cst.getString(13));
				 * AccessLog.Log("CST 14 "+cst.getString(14));
				 * AccessLog.Log("CST 15 "+cst.getString(15));
				 * AccessLog.Log("CST 16 "+cst.getString(16));
				 * AccessLog.Log("CST 17 "+cst.getString(17));
				 * AccessLog.Log("CST 18 "+cst.getString(18));
				 * AccessLog.Log("CST 19 "+cst.getString(19));
				 */
				if (cst != null)
					cst.close();
				String rfc = "";

				psmt1 = con.prepareStatement("select PACK_WEB.fun_ftch_rfc(?) FROM dual");
				psmt1.setString(1, clientId);
				rs1 = psmt1.executeQuery();
				while (rs1.next()) {
					rfc = rs1.getString(1);
					if (rfc == null) {
						rfc = "";
					}
				}
				//AccessLog.Log("after 4thwhl in JavFisAddR");
				if (rs1 != null)
					rs1.close();
				if (psmt1 != null)
					psmt1.close();
				//AccessLog.Log("after rfc" + rfc);
				values = new HashMap();
				values.put("clientName", clientName1);
				values.put("clientId", clientId);
				values.put("AM_ADDR_CODE", AM_ADDR_CODE);
				values.put("AM_DRNR", AM_DRNR);
				values.put("AM_STRT_NAME", AM_STRT_NAME);
				values.put("AM_PHNO1", AM_PHNO1);
				// values.put("AM_SUIT_NO",AM_SUIT_NO);
				// values.put("AM_FLOR_NO",AM_FLOR_NO);
				values.put("AM_ADDR_STYP", AM_ADDR_STYP);
				values.put("AM_ADDR_DEFN_TYPE", AM_ADDR_DEFN_TYPE);
				values.put("AM_GETY_LEVL", AM_GETY_LEVL);
				values.put("AM_GETY_TYPE", AM_GETY_TYPE);
				values.put("AM_GETY_CODE", AM_GETY_CODE);
				values.put("u11", u11);
				values.put("u12", u12);
				values.put("u13", u13);
				values.put("u14", u14);
				values.put("u15", u15);
				values.put("u16", u16);
				values.put("u17", u17);
				values.put("Zipcode", zipcode);
				values.put("c11", c11);
				values.put("c12", c12);
				values.put("c13", c13);
				values.put("c14", c14);
				values.put("c15", c15);
				values.put("c16", c16);
				values.put("rfc", rfc);
				
				result.add(values);
			}
			
			if (rs != null)
				rs.close();
			
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getClientrecords()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs1 != null)
					rs1.close();
				if (rs != null)
					rs.close();
				if (psmt1 != null)
					psmt1.close();				
				if (psmt != null)
					psmt.close();
				if (cst!=null) {
					cst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0, cnct.length()).append(msgError).append("getClientrecords()Error:").append(e2).toString());
				e2.printStackTrace();
			}
		}		
		return result;
	}	
}
