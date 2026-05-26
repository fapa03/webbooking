
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import logger.AccessLog;

public class JavBranchAddress {
	private StringBuffer cnct = new StringBuffer();
	private final String msgError = cnct.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();

	public ArrayList getLovBranchAddressRecords(Connection con, String branchId) {
		CallableStatement cst = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		HashMap values = new HashMap(25);
		ArrayList result = new ArrayList();

		try {
			String query = cnct
					.delete(0, cnct.length())
					.append("SELECT AM_ADDR_CODE, AM_DRNR, AM_STRT_NAME, ")
					.append("AM_PHNO1, AM_SUIT_NO, AM_FLOR_NO,")
					.append("AM_ADDR_STYP, AM_ADDR_DEFN_TYPE, ")
					.append("AM_ADDR_REF_NO, AM_GETY_LEVL, ")
					.append("AM_GETY_TYPE, AM_GETY_CODE ")
					.append("FROM SYS_ADDR_MSTR WHERE AM_ENTY_ID = ? AND AM_DEFA_FLAG = ?")
					.toString();

			pst = con.prepareStatement(query);

			pst.setString(1, branchId);
			pst.setString(2, "Y");//AAP01

			rs = pst.executeQuery();
			String AM_ADDR_CODE = null, AM_DRNR = null, AM_STRT_NAME = null, AM_PHNO1 = null, AM_SUIT_NO = null, AM_FLOR_NO = null, AM_ADDR_STYP = null, AM_ADDR_DEFN_TYPE = null, AM_ADDR_REF_NO = null, AM_GETY_LEVL = null, AM_GETY_TYPE = null, AM_GETY_CODE = null;

			String u11 = null, u12 = null, u13 = null, u14 = null, u15 = null, u16 = null, u17 = null;
			String c11 = null, c12 = null, c13 = null, c14 = null, c15 = null, c16 = null, zipcode = null;
			
			while (rs.next()) {
				AM_ADDR_CODE = rs.getString(1);
				AM_DRNR = rs.getString(2);
				AM_STRT_NAME = rs.getString(3);
				AM_PHNO1 = rs.getString(4);
				AM_SUIT_NO = rs.getString(5);
				AM_FLOR_NO = rs.getString(6);
				AM_ADDR_STYP = rs.getString(7);
				AM_ADDR_DEFN_TYPE = rs.getString(8);
				AM_ADDR_REF_NO = rs.getString(9);
				AM_GETY_LEVL = rs.getString(10);
				AM_GETY_TYPE = rs.getString(11);
				AM_GETY_CODE = rs.getString(12);

				AM_ADDR_CODE = (AM_ADDR_CODE != null ? AM_ADDR_CODE : "");
				AM_DRNR = (AM_DRNR != null ? AM_DRNR : "");
				AM_STRT_NAME = (AM_STRT_NAME != null ? AM_STRT_NAME : "");
				AM_PHNO1 = (AM_PHNO1 != null ? AM_PHNO1 : "");
				AM_SUIT_NO = (AM_SUIT_NO != null ? AM_SUIT_NO : "");
				AM_FLOR_NO = (AM_FLOR_NO != null ? AM_FLOR_NO : "");
				AM_ADDR_STYP = (AM_ADDR_STYP != null ? AM_ADDR_STYP : "");
				AM_ADDR_DEFN_TYPE = (AM_ADDR_DEFN_TYPE != null ? AM_ADDR_DEFN_TYPE : "");
				AM_ADDR_REF_NO = (AM_ADDR_REF_NO != null ? AM_ADDR_REF_NO : "");
				AM_GETY_LEVL = (AM_GETY_LEVL != null ? AM_GETY_LEVL : "");
				AM_GETY_TYPE = (AM_GETY_TYPE != null ? AM_GETY_TYPE : "");
				AM_GETY_CODE = (AM_GETY_CODE != null ? AM_GETY_CODE : "");

				cst = con.prepareCall("{call pack_web.pro_ftch_addr(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

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
				c13 = (cst.getString(16) != null ? cst.getString(16) : "");
				c14 = (cst.getString(17) != null ? cst.getString(17) : "");
				c15 = (cst.getString(18) != null ? cst.getString(18) : "");
				c16 = (cst.getString(19) != null ? cst.getString(19) : "");
				
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
				
				cst.close();
			}
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getLovBranchAddressRecords()Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {				
				if (rs != null) {
					rs.close();
				}
				if (cst != null) {
					cst.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgError).append("getLovBranchAddressRecords()Error2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
}
