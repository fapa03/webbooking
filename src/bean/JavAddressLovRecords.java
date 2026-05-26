package bean;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import logger.AccessLog;

//import logger.AccessLog;


public class JavAddressLovRecords {
	private StringBuffer concatena = new StringBuffer();
	private final String msgErr = concatena.append("MSERR_B_").append(this.getClass().getName()).append(".").toString();
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getAddressLov(Connection con,String destinationClientId)throws Exception{
		CallableStatement cst = null;
		ResultSet rs = null;
        ResultSet crs = null;
		PreparedStatement pst = null;
        HashMap values = null;
        ArrayList result = new ArrayList();
		
		String addressType="CLNT";
		String query =	"SELECT AM_ADDR_CODE,AM_DRNR,AM_STRT_NAME, "+
						"       AM_PHNO1,AM_SUIT_NO,AM_FLOR_NO,"+
                        "       AM_ADDR_STYP,AM_ADDR_DEFN_TYPE,"+
						"       AM_ADDR_REF_NO,AM_GETY_LEVL,"+
                        "       AM_GETY_TYPE,AM_GETY_CODE "+
						"  FROM SYS_ADDR_MSTR "+
						" WHERE AM_ENTY_ID=?"+
                        "   AND AM_ADDR_TYPE=?";

		pst = con.prepareStatement(query);
		pst.setString(1,destinationClientId);
		pst.setString(2,addressType);
		rs=pst.executeQuery();
		String	AM_ADDR_CODE=null,AM_DRNR=null,AM_STRT_NAME=null,AM_PHNO1=null,AM_SUIT_NO=null,AM_FLOR_NO=null,
			AM_ADDR_STYP=null,AM_ADDR_DEFN_TYPE=null,AM_ADDR_REF_NO=null,AM_GETY_LEVL=null,AM_GETY_TYPE=null,AM_GETY_CODE=null;
		values = new HashMap();
		while(rs.next()){
			AM_ADDR_CODE=rs.getString(1);
			AM_DRNR=rs.getString(2);
			AM_STRT_NAME=rs.getString(3);
			AM_PHNO1=rs.getString(4);
			AM_SUIT_NO=rs.getString(5);
			AM_FLOR_NO=rs.getString(6);
			AM_ADDR_STYP=rs.getString(7);
			AM_ADDR_DEFN_TYPE=rs.getString(8);
			AM_ADDR_REF_NO=rs.getString(9);
			AM_GETY_LEVL=rs.getString(10);
			AM_GETY_TYPE=rs.getString(11);
			AM_GETY_CODE=rs.getString(12);
			
			//AAP//AccessLog.Log("AM_ADDR_DEFN_TYPE "+AM_ADDR_DEFN_TYPE);
			//AAP//AccessLog.Log("AM_ADDR_REF_NO "+AM_ADDR_REF_NO);
			//AAP//AccessLog.Log("AM_GETY_CODE "+AM_GETY_CODE);
			//AAP//AccessLog.Log("AM_GETY_LEVL "+AM_GETY_LEVL);
			//AAP//AccessLog.Log("AM_GETY_TYPE "+AM_GETY_TYPE);
            cst = con.prepareCall(	"{call pack_web.pro_ftch_addr(?,?,?,"+
								"?,?,"+
								"?,?,?,"+
								"?,?,?,?,?,"+
								"?,?,?,?,?,?) }");

			
			//AAP//AccessLog.Log("AM_ADDR_DEFN_TYPE "+AM_ADDR_DEFN_TYPE);
			//AAP//AccessLog.Log("AM_ADDR_REF_NO "+AM_ADDR_REF_NO);
			//AAP//AccessLog.Log("AM_GETY_CODE "+AM_GETY_CODE);
			//AAP//AccessLog.Log("AM_GETY_LEVL "+AM_GETY_LEVL);
			//AAP//AccessLog.Log("AM_GETY_TYPE "+AM_GETY_TYPE);
		    
			cst.setString(1,AM_ADDR_DEFN_TYPE);
    		cst.setString(2,AM_ADDR_REF_NO);
	    	cst.setString(3,AM_GETY_CODE);
		    cst.setString(4,AM_GETY_LEVL);
		    cst.setString(5,AM_GETY_TYPE);

    		cst.registerOutParameter(6,Types.VARCHAR);
	    	cst.registerOutParameter(7,Types.VARCHAR);
		    cst.registerOutParameter(8,Types.VARCHAR);
    		cst.registerOutParameter(9,Types.VARCHAR);
	    	cst.registerOutParameter(10,Types.VARCHAR);
		    cst.registerOutParameter(11,Types.VARCHAR);
    		cst.registerOutParameter(12,Types.VARCHAR);
	    	cst.registerOutParameter(13,Types.VARCHAR);
		    cst.registerOutParameter(14,Types.VARCHAR);
    	    cst.registerOutParameter(15,Types.VARCHAR);
	    	cst.registerOutParameter(16,Types.VARCHAR);
    		cst.registerOutParameter(17,Types.VARCHAR);
	    	cst.registerOutParameter(18,Types.VARCHAR);
		    cst.registerOutParameter(19,Types.VARCHAR);
    		//crs=cst.executeQuery();
    		cst.executeQuery();
			//if(crs.next()){
				
            values.put("AM_ADDR_CODE",AM_ADDR_CODE);
            values.put("AM_DRNR",AM_DRNR);
            values.put("AM_STRT_NAME",AM_STRT_NAME);
            values.put("AM_PHNO1",AM_PHNO1);
            values.put("AM_SUIT_NO",AM_SUIT_NO);
            values.put("AM_FLOR_NO",AM_FLOR_NO);
            values.put("AM_ADDR_STYP",AM_ADDR_STYP);
            values.put("AM_ADDR_DEFN_TYPE",AM_ADDR_DEFN_TYPE);
            values.put("AM_GETY_LEVL",AM_GETY_LEVL);
            values.put("AM_GETY_TYPE",AM_GETY_TYPE);
            values.put("AM_GETY_CODE",AM_GETY_CODE);
            values.put("u11",cst.getString(6));
            values.put("u12",cst.getString(7));
            values.put("u13",cst.getString(8));
            values.put("u14",cst.getString(9));
            values.put("u15",cst.getString(10));
            values.put("u16",cst.getString(11));
            values.put("u17",cst.getString(12));
            values.put("Zipcode",cst.getString(13));
            values.put("c11",cst.getString(14));
            values.put("c12",cst.getString(15));
            values.put("c13",cst.getString(16));
            values.put("c14",cst.getString(17));
            values.put("c15",cst.getString(18));
            values.put("c16",cst.getString(19));
            result.add(values.clone());
            values.clear();
				//AAP//AccessLog.Log("VALUES "+values);
            //}
			//crs.close();
            if(cst != null){
                cst.close();
            }
		}
		if (rs!=null){
			rs.close();
		}
		//if (crs!= null){
		//	crs.close();
		//}
        if(pst != null){
            pst.close();
        }        
        return result;
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap getOrgionClientAddressCC(Connection con, HttpSession session, String centroCosto) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Global global = new Global();
		String query = "";
		HashMap result =  new HashMap();
		
		try {
			global = (Global) session.getAttribute("sGlobal");
			String strClientId = global.getClientId();
			String desColonia = "";
			String desCiudad = "";
			String strAssignedBranchExcept = "";
			//aform.setOrgienrfc(global.getRfc());

			query = concatena
					.delete(0, concatena.length())
					.append("SELECT AM_ADDR_CODE,AM_DRNR,AM_STRT_NAME, ")
					.append("AM_PHNO1,AM_SUIT_NO,AM_FLOR_NO,AM_ADDR_STYP,AM_ADDR_DEFN_TYPE, ")
					.append("AM_ADDR_REF_NO,AM_GETY_LEVL,AM_GETY_TYPE,AM_GETY_CODE, AM_MAIL_ID, CC_BRNC_ORGN ")   
					.append("FROM SYS_ADDR_MSTR, SYS_CLNT_CCOSTO WHERE AM_ENTY_ID = ? and CC_CLNT_ID = ? AND CC_CCOSTO_ID = ? ")
					.append("and AM_ADDR_CODE = CC_ADDR_CODE")
					.toString();
			
			pst = con.prepareStatement(query);
			
			// AAP//AccessLog.Log(concatena.delete(0,concatena.length()).append(msgAvi).append("getOrgionClientAddress()_global.assignedsite").append(strAssignedSite).toString());
			// AAP//AccessLog.Log(concatena.delete(0,concatena.length()).append(msgAvi).append("getOrgionClientAddress()_global.strClientId").append(strClientId).toString());
			pst.setString(1, strClientId);
			pst.setString(2, strClientId);
			pst.setString(3, centroCosto);
			
			rs = pst.executeQuery();
			
			//30/12/2021 -- Se cambió la inicialización de NULL a ""
			String AM_ADDR_CODE = "", AM_DRNR = "", AM_STRT_NAME = "", AM_PHNO1 = "";
			String AM_GETY_LEVL = "", AM_GETY_TYPE = "", AM_GETY_CODE = "";
			String orignCityCode = "";

			while (rs.next()) {
				AM_ADDR_CODE = rs.getString(1) == null ? "" : rs.getString(1);
				AM_DRNR = rs.getString(2) == null ? "" : rs.getString(2);
				AM_STRT_NAME = rs.getString(3) == null ? "" : rs.getString(3);
				AM_PHNO1 = rs.getString(4) == null ? "" : rs.getString(4);	
				AM_GETY_LEVL = String.valueOf(rs.getInt(10));
				AM_GETY_TYPE = String.valueOf(rs.getInt(11));
				AM_GETY_CODE = rs.getString(12)== null ? "" : rs.getString(12);
				strAssignedBranchExcept = rs.getString(14) == null ? "" : rs.getString(14);
			}

			//resources.closeResources(rs, pst);
			rs.close();
			pst.close();
			// AAP//AccessLog.Log(concatena.delete(0,concatena.length()).append(msgAvi).append("getOrgionClientAddress()_Before finding ro_ftch_addr").toString());
			
			//query = "SELECT COLO_PLAZA, COLO_SUCURSAL, COLO_DES, ID_CIUD, CIUDAD, COD_DELE, DELMUN FROM DW_COBERTURA_VIEW WHERE COL_LEVEL = ? AND COL_TYPE = ? AND COLO_ID = ? ";
			query = "SELECT PLAZA, SUCURSAL, COL_DES, COD_CIUD, CIUDAD, COD_DELE, DELMUN FROM PCOBERTURA_VIEW WHERE COL_LEVL = ? AND COL_TYPE = ? AND COD_COLO = ? AND SUCURSAL IS NOT NULL";
			
			pst = con.prepareStatement(query);
			
			pst.setString(1, AM_GETY_LEVL);
			pst.setString(2, AM_GETY_TYPE);
			pst.setString(3, AM_GETY_CODE);
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				global.setAssignedSite(rs.getString(1));
				if (strAssignedBranchExcept.trim().length()>0) {
					global.setAssignedBranch(strAssignedBranchExcept);
				} else {
					global.setAssignedBranch(rs.getString(2));
				}
				desColonia = rs.getString(3);
				orignCityCode = rs.getString(4);
				desCiudad = rs.getString(5);				
			}
			result.put("orignCityCode", orignCityCode);
			result.put("originColinaCode", AM_GETY_CODE);
			
			result.put("orgien1", AM_STRT_NAME);
			result.put("orgien2", AM_DRNR);
			result.put("orgientelefono", AM_PHNO1);
			result.put("orgioncode", AM_ADDR_CODE);
			result.put("orgiencolonia1", desColonia);
			result.put("orgiencolonia2", desCiudad);
			
//			rs.close();
//			pst.close();
//			// AAP//AccessLog.Log(concatena.delete(0,concatena.length()).append(msgAvi).append("getOrgionClientAddress()_Before finding branch name").toString());
//			query = "select pack_web.fun_ftch_brnc_name(?) from dual";
//			pst = con.prepareStatement(query);
//			pst.setString(1, global.getAssignedBranch());
//			rs = pst.executeQuery();
//
//			String branchName = "";
//			if (rs.next()) {
//				branchName = rs.getString(1);
//			}
//
//			//resources.closeResources(rs, pst);
//			rs.close();
//			pst.close();
//
//			// AAP//AccessLog.Log(concatena.delete(0,concatena.length()).append(msgAvi).append("getOrgionClientAddress()_Before finding site name").toString());
//			query = "select pack_web.fun_ftch_site_name(?) from dual";
//			pst = con.prepareStatement(query);
//			pst.setString(1, global.getAssignedSite());
//
//			rs = pst.executeQuery();
//
//			String siteName = "";
//			if (rs.next()) {
//				siteName = rs.getString(1);
//			}
//
//			aform.setOrginbranchcode(strAssignedBranch);
//			aform.setOrgionbranch(branchName);
//			aform.setOrginsite(siteName);
			session.setAttribute("sGlobal", global);//asignacion de sGlobal por nuevos site y sucursal asignados.
			session.setAttribute("branchid", global.getAssignedBranch());//AAP VARIABLE PARA GUIAS DE PREPAGO
			
			// AAP//AccessLog.Log(concatena.delete(0,concatena.length()).append(msgAvi).append("getOrgionClientAddress()_aform.getOrgienclave()").append(aform.getOrgienclave()).toString());
			// AAP//AccessLog.Log(concatena.delete(0,concatena.length()).append(msgAvi).append("getOrgionClientAddress()_aform.getOrgiennombre()").append(aform.getOrgiennombre()).toString());
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0, concatena.length())
					.append(msgErr).append("getOrgionClientAddressCC()_Error:")
					.append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs!=null) {
					rs.close();
				}
		        if(pst != null){
		            pst.close();
		        }
			} catch (Exception e) {
				AccessLog.Log(concatena.delete(0, concatena.length())
						.append(msgErr).append("getOrgionClientAddressCC()_Error2:")
						.append(e).toString());
			}
		}
		return result;
	}
}
