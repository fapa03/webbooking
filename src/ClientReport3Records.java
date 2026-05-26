

import java.sql.*;
import java.util.*;

public class ClientReport3Records {
	
	public ClientReport3Records(){
	}
	
	public ArrayList getDefaultAddress(Connection con,
									   String destinationClientId)throws Exception {
		CallableStatement cst = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		HashMap values = null;
		ArrayList result = new ArrayList();
		
		String addressType="CLNT";
		String defaultAddressFlag = "Y";
		String query =	"SELECT AM_ADDR_CODE,AM_DRNR,AM_STRT_NAME, "+
						"       AM_PHNO1,AM_SUIT_NO,AM_FLOR_NO,"+
						"       AM_ADDR_STYP,AM_ADDR_DEFN_TYPE,"+
						"       AM_ADDR_REF_NO,AM_GETY_LEVL,"+
						"       AM_GETY_TYPE,AM_GETY_CODE "+
						"  FROM SYS_ADDR_MSTR "+
						" WHERE AM_ENTY_ID=?"+
						"   AND AM_ADDR_TYPE=?"+
						"   AND AM_DEFA_FLAG=?";

		pst = con.prepareStatement(query);
		pst.setString(1,destinationClientId);
		pst.setString(2,addressType);
		pst.setString(3,defaultAddressFlag);

		rs=pst.executeQuery();
		
		
		String	AM_ADDR_CODE=null,AM_DRNR=null,AM_STRT_NAME=null,AM_PHNO1=null,AM_SUIT_NO=null,AM_FLOR_NO=null,
			AM_ADDR_STYP=null,AM_ADDR_DEFN_TYPE=null,AM_ADDR_REF_NO=null,AM_GETY_LEVL=null,AM_GETY_TYPE=null,AM_GETY_CODE=null;
		
		while(rs.next()){
			AM_ADDR_CODE		=(rs.getString(1)== null?"":rs.getString(1));
			AM_DRNR				=(rs.getString(2)== null?"":rs.getString(2));
			AM_STRT_NAME		=(rs.getString(3)== null?"":rs.getString(3));
			AM_PHNO1			=(rs.getString(4)== null?"":rs.getString(4));
			AM_SUIT_NO			=(rs.getString(5)== null?"":rs.getString(5));
			AM_FLOR_NO			=(rs.getString(6)== null?"":rs.getString(6));
			AM_ADDR_STYP		=(rs.getString(7)== null?"":rs.getString(7));
			AM_ADDR_DEFN_TYPE	=rs.getString(8);
			AM_ADDR_REF_NO		=rs.getString(9);
			AM_GETY_LEVL		=rs.getString(10);
			AM_GETY_TYPE		=rs.getString(11);
			AM_GETY_CODE		=rs.getString(12);
			
			cst = con.prepareCall(	"{call pack_web.pro_ftch_addr(?,?,?,"+
									"?,?,"+
									"?,?,?,"+
									"?,?,?,?,?,"+
									"?,?,?,?,?,?) }");		    
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

			cst.executeQuery();
			//if(crs.next()){
			values = new HashMap();
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
			values.put("u11",(cst.getString(6)== null?"":cst.getString(6)));
			values.put("u12",(cst.getString(7)== null?"":cst.getString(7)));
			values.put("u13",(cst.getString(8)== null?"":cst.getString(8)));
			values.put("u14",(cst.getString(9)== null?"":cst.getString(9)));
			values.put("u15",(cst.getString(10)== null?"":cst.getString(10)));
			values.put("u16",(cst.getString(11)== null?"":cst.getString(11)));
			values.put("u17",(cst.getString(12)== null?"":cst.getString(12)));
			values.put("Zipcode",(cst.getString(13)== null?"":cst.getString(13)));
			values.put("c11",(cst.getString(14)== null?"":cst.getString(14)));
			values.put("c12",(cst.getString(15)== null?"":cst.getString(15)));
			values.put("c13",(cst.getString(16)== null?"":cst.getString(16)));
			values.put("c14",(cst.getString(17)== null?"":cst.getString(17)));
			values.put("c15",(cst.getString(18)== null?"":cst.getString(18)));
			values.put("c16",(cst.getString(19)== null?"":cst.getString(19)));
			result.add(values);
			//}
			//Added by rama for java.sql.SQLException: ORA-01000: maximum open cursors exceeded
			if(cst != null){
				cst.close();
			}
		}
		//Added by rama for java.sql.SQLException: ORA-01000: maximum open cursors exceeded
		if(pst!=null)
			pst.close();
		if(rs!=null)
			rs.close();
		return result;
	}
	
	
	/*public ArrayList fetchBranchAddress(Connection con,
										String destBranchId,
										String clientId)throws Exception{
		return null;
	}*/
	
	public ArrayList fetchClientRecords(Connection con,
										String destClientId,
										String clientId,
										String fromDate,
										String toDate,
										String destBranchId)throws Exception {
		String guiaType  = "H";
		String docuType  = "Q";
		String destClnt  = null;
		String destBrnc  = null;
		String destBrnachName=null;
		String destClientName=null;
		String isseDate  = null;
		String GuiaNumb  = null;
		String GuiaCont  = null;
		String packages  = null;
		String shp_totl  = null;
		String ack_totl  = null;
		String rad_totl  = null;
		String ead_totl  = null;
		String ins_totl  = null;
		String cod_totl  = null;
		String add_totl  = null;
		String oth_totl  = null;
		HashMap values   = null;
		ArrayList result = new ArrayList();
		
		// Select branchname and Clien name also from here...
		
		//Commented by rama
		/*String query = "	SELECT GH_DEST_CLNT_ID, GH_DEST_BRNC_ID,"+
					   "	Pack_web.Fun_ftch_brnc_name(GH_DEST_BRNC_ID) DestBranchName, "+
					   "	gh_dest_clnt_name DestClientName, "+
					   "	TO_CHAR(GH_ISSE_DATE,'DD/MM/YYYY'), GH_GUIA_NO, count(gh_guia_no) guia_count,sum( GH_NUMB_PACK ) pack"+
					   "	FROM bok_guia_head"+ 
					   "	WHERE GH_ORGN_CLNT_ID = ?"+
					   "	AND GH_GUIA_TYPE    = ?"+
					   "	AND GH_DOCU_TYPE    = ?"+
					   "	AND GH_DEST_CLNT_ID = ?"+
					   "	AND TRUNC(GH_ISSE_DATE) BETWEEN (TO_DATE(TO_DATE(?,'DD/MM/YY'),'DD-MON-YY')) AND (TO_DATE(TO_DATE(?,'DD/MM/YY'),'DD-MON-YY'))"+
					   "	GROUP BY GH_DEST_CLNT_ID, GH_DEST_BRNC_ID, GH_ISSE_DATE, GH_GUIA_NO,GH_NUMB_PACK ,GH_DEST_CLNT_NAME";*/
		
		String query =	"	SELECT GH_DEST_CLNT_ID, GH_DEST_BRNC_ID,"+
						"	Pack_web.Fun_ftch_brnc_name(GH_DEST_BRNC_ID) DestBranchName, "+
						"	gh_dest_clnt_name DestClientName, "+
						"	TO_CHAR(GH_ISSE_DATE,'DD/MM/YYYY'), "+
						"	GH_GUIA_NO, gh_guia_no guia_count,GH_NUMB_PACK pack"+
						"	FROM bok_guia_head"+ 
						"	WHERE GH_ORGN_CLNT_ID = ?"+
						"	AND GH_GUIA_TYPE    = ?"+
						"	AND GH_DOCU_TYPE    = ?"+
						"	AND GH_DEST_CLNT_ID = ?"+
						"	AND GH_DEST_BRNC_ID = ?"+//Added by rama
						"	AND TRUNC(GH_ISSE_DATE) BETWEEN TO_DATE(?,'DD/MM/YYYY')"+
						"	AND TO_DATE(?,'DD/MM/YYYY')"+
						"	ORDER BY GH_ISSE_DATE ";////Added by rama
					   
		
		PreparedStatement psmt = con.prepareStatement(query);
		CallableStatement csmt = null;
		psmt.setString(1,clientId);
		psmt.setString(2,guiaType);
		psmt.setString(3,docuType);
		psmt.setString(4,destClientId);
		psmt.setString(5,destBranchId);//Added by rama
		psmt.setString(6,fromDate);
		psmt.setString(7,toDate);
		ResultSet rs = psmt.executeQuery();
		
		while(rs.next()) {
			destClnt = rs.getString(1);
			destBrnc = rs.getString(2);
			destBrnachName=rs.getString("DestBranchName");
			destClientName=rs.getString("DestClientName");
			isseDate = rs.getString(5);
			GuiaNumb = rs.getString(6);
			GuiaCont = rs.getString(7);
			packages = rs.getString(8);			 
			
			csmt = con.prepareCall("begin pack_web.Pro_Calc_Totl(?,?,?,?,?,?,?,?,?);end;");
			
			csmt.setString(1,GuiaNumb);
			
			csmt.registerOutParameter(2,Types.NUMERIC);
			csmt.registerOutParameter(3,Types.NUMERIC);
			csmt.registerOutParameter(4,Types.NUMERIC);
			csmt.registerOutParameter(5,Types.NUMERIC);
			csmt.registerOutParameter(6,Types.NUMERIC);
			csmt.registerOutParameter(7,Types.NUMERIC);
			csmt.registerOutParameter(8,Types.NUMERIC);
			csmt.registerOutParameter(9,Types.NUMERIC);
			
			csmt.executeQuery();
			
			/*shp_totl  = (csmt.getString(2)==null?"0":csmt.getString(2));
			ack_totl  = (csmt.getString(3)==null?"0":csmt.getString(3));
			rad_totl  = (csmt.getString(4)==null?"0":csmt.getString(4));
			ead_totl  = (csmt.getString(5)==null?"0":csmt.getString(5));
			ins_totl  = (csmt.getString(6)==null?"0":csmt.getString(6));
			cod_totl  = (csmt.getString(7)==null?"0":csmt.getString(7));
			add_totl  = (csmt.getString(8)==null?"0":csmt.getString(8));
			oth_totl  = (csmt.getString(9)==null?"0":csmt.getString(9));*/
			
			//AccessLog.Log("value from db shp_totl: "+csmt.getDouble(2));
			//AccessLog.Log("value from db ack_totl: "+csmt.getDouble(3));
			//AccessLog.Log("value from db rad_totl: "+csmt.getDouble(4));
			//AccessLog.Log("value from db ead_totl: "+csmt.getDouble(5));
			//AccessLog.Log("value from db ins_totl: "+csmt.getDouble(6));
			//AccessLog.Log("value from db cod_totl: "+csmt.getDouble(7));
			//AccessLog.Log("value from db add_totl: "+csmt.getDouble(8));
			//AccessLog.Log("value from db oth_totl: "+csmt.getDouble(9));
			
			java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
			shp_totl  = df.format(csmt.getDouble(2));
			ack_totl  = df.format(csmt.getDouble(3));
			rad_totl  = df.format(csmt.getDouble(4));
			ead_totl  = df.format(csmt.getDouble(5));
			ins_totl  = df.format(csmt.getDouble(6));
			cod_totl  = df.format(csmt.getDouble(7));
			add_totl  = df.format(csmt.getDouble(8));
			oth_totl  = df.format(csmt.getDouble(9));
			
			values = new HashMap();
			values.put("destClnt",destClnt);
			values.put("destBrnc",destBrnc);
			values.put("destBranchName",destBrnachName);
			values.put("destClientName",destClientName);
			values.put("isseDate",isseDate);
			values.put("GuiaNumb",GuiaNumb);
			values.put("GuiaCont",GuiaCont);
			values.put("packages",packages);
			
			values.put("shp_totl",shp_totl);
			values.put("ack_totl",ack_totl);
			values.put("rad_totl",rad_totl);
			values.put("ead_totl",ead_totl);
			values.put("ins_totl",ins_totl);
			values.put("cod_totl",cod_totl);
			values.put("add_totl",add_totl);
			values.put("oth_totl",oth_totl);
			result.add(values);
			
			//Added by rama for java.sql.SQLException: ORA-01000: maximum open cursors exceeded
			if(csmt!=null)
				csmt.close();
		}
		
		//Added by rama for java.sql.SQLException: ORA-01000: maximum open cursors exceeded
		if(psmt!=null)
			psmt.close();		
		if(rs!=null)
			rs.close();
		
		return result;
	}
	
	public ArrayList fetchMasterRecords(Connection con,
										String clientId,
										String fromDate,
										String toDate)throws Exception {
		HashMap values			= null;
		ArrayList clAmounts		= null;
		String destClientId		= null;
		String destClientName	= null;
		String destBranchId		= null;
		String destBranchName   = null;
		String guiaCount		= null;
		String packages			= null;
		String guiaType			= "H";
		String docuType			= "Q";
		ArrayList result		= new ArrayList();
		String query="	SELECT GH_DEST_CLNT_ID,GH_DEST_CLNT_NAME, GH_DEST_BRNC_ID, "+
					 "	Pack_web.Fun_ftch_brnc_name(GH_DEST_BRNC_ID) DestBranchName,"+
					 "	count(GH_GUIA_NO) guia_count, sum(GH_NUMB_PACK) Packages"+ 
					 "	FROM bok_guia_head"+
					 "	WHERE GH_ORGN_CLNT_ID = ?"+
					 "  AND GH_GUIA_TYPE    = ?"+
					 "  AND GH_DOCU_TYPE	 = ?"+
					 "  AND trunc(GH_ISSE_DATE) between TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY')"+//Changed by rama
					 "	GROUP BY GH_DEST_CLNT_ID,GH_DEST_CLNT_NAME, GH_DEST_BRNC_ID";
		PreparedStatement psmt = con.prepareStatement(query);
		psmt.setString(1,clientId);
		psmt.setString(2,guiaType);
		psmt.setString(3,docuType);
		psmt.setString(4,fromDate);
		psmt.setString(5,toDate);
		ResultSet rs = psmt.executeQuery();
		while(rs.next()){
			destClientId	= rs.getString(1);
			destClientName	= (rs.getString(2)== null?"":rs.getString(2));
			destBranchId	= (rs.getString(3)== null?"":rs.getString(3));
			destBranchName	= (rs.getString(3)== null?"":rs.getString(4));
			guiaCount		= (rs.getString(4)== null?"":rs.getString(5));
			packages		= (rs.getString(5)== null?"":rs.getString(6));
			
			values = new HashMap();
			clAmounts = fetchClientRecords(con,destClientId,clientId,fromDate,toDate,destBranchId);
			values.put("destClientId",destClientId);
			values.put("destClientName",destClientName);
			values.put("destBranchId",destBranchId);
			values.put("destBranchName",destBranchName);
			values.put("guiaCount",guiaCount);
			values.put("packages",packages);
			values.put("clAmount",clAmounts);
			result.add(values);
		}
		
		//Added by rama for java.sql.SQLException: ORA-01000: maximum open cursors exceeded
		if(psmt!=null)
			psmt.close();
		if(rs!=null)
			rs.close();
		
		return result;
	}
	public HashMap fetchRecords(Connection con,
								String clientId,
								String fromDate,
								String toDate)throws Exception{
		ArrayList defaultAddress = getDefaultAddress(con,clientId);
		ArrayList destDetail = fetchMasterRecords(con,clientId,fromDate,toDate);										
		HashMap value = new HashMap();
		value.put("defaultAddress",defaultAddress);
		value.put("destDetail",destDetail);
		return value;
	}
}