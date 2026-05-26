import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import logger.AccessLog;
import bean.Global;
import bean.SucursalesConfiguradas;

public class JavDestinationAddressRecords {
	private final String msgError  = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private StringBuffer concatena = new StringBuffer();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getLovRecords(Connection con,String addressType,String destClientId,String branchId,HttpSession session,String destSiteId) {
		CallableStatement cst = null;
		ResultSet rs = null;
		ResultSet rset = null;
		PreparedStatement pst = null;
		HashMap values = new HashMap(27);
		ArrayList result = new ArrayList();
		String addrType = "CLNT";
		SucursalesConfiguradas suc = new SucursalesConfiguradas();//AAP02
		try {
			//AAP//AccessLog.Log("Inside the Records fetching in JavDestinationAddressRecords");
			String query =	"	SELECT AM_ADDR_CODE,AM_DRNR,AM_STRT_NAME, "+
							"       AM_PHNO1,AM_SUIT_NO,AM_FLOR_NO,"+
							"       AM_ADDR_STYP,AM_ADDR_DEFN_TYPE,"+
							"       AM_ADDR_REF_NO,AM_GETY_LEVL,"+
							"       AM_GETY_TYPE,AM_GETY_CODE, AM_COMT, AM_MAIL_ID "+
							"	FROM SYS_ADDR_MSTR ";
			
			if(addressType.equalsIgnoreCase("DC")){
				query = query +  " WHERE AM_ENTY_ID=?"+
						"   AND AM_ADDR_TYPE=? AND AM_PE_SITE_ID = ?";
			}else{
				query = query + " WHERE AM_PE_SITE_ID = ?"+
						"   AND AM_ENTY_ID = ?"+
						"   AND AM_ADDR_TYPE = ? AND AM_PE_SITE_ID = ?";
			}

			pst = con.prepareStatement(query);
			if(addressType.equalsIgnoreCase("DC")){
				pst.setString(1,destClientId);
				pst.setString(2,addrType);
				pst.setString(3,destSiteId);
			}else{
				pst.setString(1,branchId);
				pst.setString(2,destClientId);
				pst.setString(3,addrType);
				pst.setString(4,destSiteId);
			}
			
			
			//AAP//AccessLog.Log("before while loop"+branchId+""+destClientId);
			//AAP//AccessLog.Log("before while loop query is"+""+destClientId);
			rs=pst.executeQuery();
			String	AM_ADDR_CODE=null,AM_DRNR=null,AM_STRT_NAME=null,AM_PHNO1=null,AM_SUIT_NO=null,AM_FLOR_NO=null,
				AM_ADDR_STYP=null,AM_ADDR_DEFN_TYPE=null,AM_ADDR_REF_NO=null,AM_GETY_LEVL=null,AM_GETY_TYPE=null,AM_GETY_CODE=null, AM_COMT = null, AM_MAIL_ID = null;
			
			String u11=null,u12=null,u13=null,u14=null,u15=null,u16=null,u17=null;
			String c11=null,c12=null,c13=null,c14=null,c15=null,c16=null,zipcode=null;
			//AAP//AccessLog.Log("before wjile loop");
			String entrega = "";//AAP02
			String nuevaSucursal = "";//AAP02
			while(rs.next()){
				//AAP//AccessLog.Log("inside while loop");
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
				AM_COMT = rs.getString(13) == null ? "" : rs.getString(13);//AM_COMT comentario de direccion
				AM_MAIL_ID = rs.getString(14) == null ? "" : rs.getString(14);//AM_MAIL_ID correo electronico destinatario
				
				AM_ADDR_CODE =(AM_ADDR_CODE!=null?AM_ADDR_CODE:"");
				AM_DRNR =(AM_DRNR!=null?AM_DRNR:"");
				AM_STRT_NAME =(AM_STRT_NAME!=null?AM_STRT_NAME:"");
				AM_PHNO1 =(AM_PHNO1!=null?AM_PHNO1:"");
				AM_SUIT_NO =(AM_SUIT_NO!=null?AM_SUIT_NO:"");
				AM_FLOR_NO =(AM_FLOR_NO!=null?AM_FLOR_NO:"");
				AM_ADDR_STYP =(AM_ADDR_STYP!=null?AM_ADDR_STYP:"");
				AM_ADDR_DEFN_TYPE =(AM_ADDR_DEFN_TYPE!=null?AM_ADDR_DEFN_TYPE:"");
				AM_ADDR_REF_NO=(AM_ADDR_REF_NO!=null?AM_ADDR_REF_NO:"");
				AM_GETY_LEVL =(AM_GETY_LEVL!=null?AM_GETY_LEVL:"");
				AM_GETY_TYPE =(AM_GETY_TYPE!=null?AM_GETY_TYPE:"");
				AM_GETY_CODE =(AM_GETY_CODE!=null?AM_GETY_CODE:"");				
				
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
				
				
				u11 =(cst.getString(6)!=null?cst.getString(6):"");
				u12 =(cst.getString(7)!=null?cst.getString(7):"");
				u13 =(cst.getString(8)!=null?cst.getString(8):"");
				u14 =(cst.getString(9)!=null?cst.getString(9):"");
				u15 =(cst.getString(10)!=null?cst.getString(10):"");
				u16 =(cst.getString(11)!=null?cst.getString(11):"");			
				u17 =(cst.getString(12)!=null?cst.getString(12):"");
				zipcode = (cst.getString(13)!=null?cst.getString(13):"");
				c11 =(cst.getString(14)!=null?cst.getString(14):"");
				c12 =(cst.getString(15)!=null?cst.getString(15):"");
				c13 =(cst.getString(16)!=null?cst.getString(16):"");
				c14 =(cst.getString(17)!=null?cst.getString(17):"");
				c15 =(cst.getString(18)!=null?cst.getString(18):"");
				c16 =(cst.getString(19)!=null?cst.getString(19):"");
				
				/*AccessLog.Log("CST 6 "+cst.getString(6));
				AccessLog.Log("CST 7 "+cst.getString(7));
				AccessLog.Log("CST 8 "+cst.getString(8));
				AccessLog.Log("CST 9 "+cst.getString(9));
				AccessLog.Log("CST 10 "+cst.getString(10));
				AccessLog.Log("CST 11 "+cst.getString(11));
				AccessLog.Log("CST 12 "+cst.getString(12));
				AccessLog.Log("CST 13 "+cst.getString(13));
				AccessLog.Log("CST 14 "+cst.getString(14));
				AccessLog.Log("CST 15 "+cst.getString(15));
				AccessLog.Log("CST 16 "+cst.getString(16));
				AccessLog.Log("CST 17 "+cst.getString(17));
				AccessLog.Log("CST 18 "+cst.getString(18));
				AccessLog.Log("CST 19 "+cst.getString(19));*/
				
				//values = new HashMap();			
				values.put("AM_ADDR_CODE",AM_ADDR_CODE);
				values.put("AM_DRNR",AM_DRNR);
				values.put("AM_STRT_NAME",AM_STRT_NAME);
				values.put("AM_PHNO1",AM_PHNO1);
				values.put("AM_SUIT_NO",AM_SUIT_NO);
				values.put("AM_FLOR_NO",AM_FLOR_NO);
				values.put("AM_ADDR_STYP",AM_ADDR_STYP);
				values.put("AM_ADDR_DEFN_TYPE",AM_ADDR_DEFN_TYPE);
				//AAP//System.out.println("AM_GETY_LEVL"+AM_GETY_LEVL);
				values.put("AM_GETY_LEVL",AM_GETY_LEVL);
				values.put("AM_GETY_TYPE",AM_GETY_TYPE);
				//AAP//System.out.println("AM_GETY_TYPE"+AM_GETY_TYPE);
				values.put("AM_GETY_CODE",AM_GETY_CODE);
				//AAP//System.out.println("AM_GETY_CODE"+AM_GETY_CODE);
				values.put("AM_COMT", AM_COMT);				
				values.put("AM_MAIL_ID", AM_MAIL_ID);//AAP05
				
				values.put("u11",u11);
				values.put("u12",u12);
				values.put("u13",u13);
				values.put("u14",u14);
				values.put("u15",u15);
				values.put("u16",u16);
				values.put("u17",u17);
				values.put("Zipcode",zipcode);
				values.put("c11",c11);
				values.put("c12",c12);
				values.put("c13",c13);
				values.put("c14",c14);
				values.put("c15",c15);
				values.put("c16",c16);
				if(cst != null){
					cst.close();
				}

			/*	if :DLRY_TYPE ='H' THEN
				   PRO_ASSIGN_DEST_BRNC (:BOK_GUIA_HEAD.GH_DEST_BRNC_ID );
			   elsif :DLRY_TYPE ='O' and fun_chk_phy_dc_exist=1 and fun_chk_no_of_br>1 then
			   	PRO_ASSIGN_DEST_BRNC (:BOK_GUIA_HEAD.GH_DEST_BRNC_ID );
			   elsif :DLRY_TYPE ='O' and (fun_chk_phy_dc_exist=0 or fun_chk_no_of_br=1) then
			   	PRO_assign_dest_branch;
			   end if;
				*/
			
				String deliveryType="";
				//AAP//AccessLog.Log("AfeterservicesGlobal ");
				deliveryType=getDeliveryType(con,session);
				//AAP//AccessLog.Log("After deliveryType "+deliveryType);
				//AAP//AccessLog.Log("AfeterservicesGlobal ");
				//AAP//AccessLog.Log("destSiteIddestSiteId---"+destSiteId);
				if (destSiteId.length() > 0) {
					//AAP02 SE ELIMINÓ CONDICION PORQUE AHORA SE REALIZA EN BASE A LA CONFIGURACION
					/*if (destSiteId.equalsIgnoreCase("GDL")) {
						String branch = "";
						if (deliveryType.equalsIgnoreCase("H"))
							branch = "GDL02";
						else
							branch = "GDL01";

						pst = con.prepareStatement("select BM_BRNC_NAME from sys_brnc_mstr where BM_BRNC_SITE_ID=?  AND BM_BRNC_ID=?");
						pst.setString(1, destSiteId);
						pst.setString(2, branch);
						rset = pst.executeQuery();
						if (rset.next()) {
							String brname = rset.getString(1);
							// AAP//AccessLog.Log("before adding branch"+branch);
							values.put("branch", branch);
							values.put("branchname", brname);
							// AAP//AccessLog.Log("before adding branch"+brname);
						}

						if (rset != null)
							rset.close();
						if (pst != null) {
							pst.close();
						}
					} else {*/
						String callFunction = "";
						int fun_chk_phy_dc_exist = 0;
						int fun_chk_no_of_br = 0;
						pst = con.prepareStatement("select pack_web.fun_chk_phy_dc_exist(?) from dual");
						pst.setString(1, destSiteId);
						rset = pst.executeQuery();
						if (rset.next()) {
							fun_chk_phy_dc_exist = rset.getInt(1);

						}
						if (rset != null)
							rset.close();
						if (pst != null)
							pst.close();

						pst = con.prepareStatement("select pack_web.fun_chk_no_of_br(?) from dual");
						pst.setString(1, destSiteId);
						rset = pst.executeQuery();
						if (rset.next()) {
							fun_chk_no_of_br = rset.getInt(1);
						}
						if (rset != null)
							rset.close();
						if (pst != null)
							pst.close();
						// AAP//AccessLog.Log("deliveryType"+deliveryType);
						if (deliveryType.equalsIgnoreCase("H")) {
							callFunction = "PRO_ASSIGN_DEST_BRNC";
						} else if (deliveryType.equalsIgnoreCase("O")
								&& (fun_chk_phy_dc_exist == 1)
								&& (fun_chk_no_of_br > 1)) {
							callFunction = "PRO_ASSIGN_DEST_BRNC";
						} else {
							callFunction = "DIRECTBRANCH";
						}
				
						// AAP//System.out.println(AM_ADDR_CODE+"AM_ADDR_CODE");
						// AAP//System.out.println(destClientId+"destClientId");
						String br = "";//AAP01
						String brname = "";//AAP01
						if (callFunction.equalsIgnoreCase("PRO_ASSIGN_DEST_BRNC")) {
							cst = con.prepareCall("{call pack_web.PRO_ASSIGN_DEST_BRNC(?,?,?,?,?,?)}");
							cst.setString(1, AM_GETY_CODE);
							cst.setString(2, AM_GETY_TYPE);
							cst.setString(3, AM_GETY_LEVL);
							cst.registerOutParameter(4, Types.VARCHAR);
							cst.registerOutParameter(5, Types.VARCHAR);
							cst.setString(6, destSiteId);
							cst.executeQuery();
							br = cst.getString(4);//AAP01
							brname = cst.getString(5);//AAP01
							//AccessLog.Log("the branch is"+br);
							//AccessLog.Log("the brname is"+brname);
							values.put("branch", br);
							values.put("branchname", brname);
							if (cst != null) {
								cst.close();
							}

						} else {
							// AAP//AccessLog.Log("Single Branch"+deliveryType);
							br = "";//AAP01
							brname = "";//AAP01
							pst = con.prepareStatement("select BM_BRNC_ID,BM_BRNC_NAME from sys_brnc_mstr where BM_BRNC_SITE_ID=?  AND BM_FLAG1='G'");
							pst.setString(1, destSiteId);
							rset = pst.executeQuery();
							if (rset.next()) {
								br = rset.getString(1);
								brname = rset.getString(2);
								values.put("branch", br);
								values.put("branchname", brname);
							}
							// AAP//AccessLog.Log("Single Branch"+br);
							// AAP//AccessLog.Log("Single Branch nmame"+brname);
							if (rset != null)
								rset.close();
							if (pst != null) {
								pst.close();
							}
						}

						/*
						 * se asigna el tipo de búsqueda de configuracion en base al
						 * tipo de entrega
						 */						
						if (deliveryType.equalsIgnoreCase("O")) {// AAP02
							entrega = "DEST_OCURRE";
						} else {
							if (br!= null && !br.substring(3).equals("70")) {
								//entrega = "DEST_EAD_YA_NO";//AAP08
								entrega = "";//AAP08
							}
						}
						if (entrega.length() > 0) {
							/* se busca configuracion */
							nuevaSucursal = suc.obtieneConfigSucursal(con, "BOK", entrega, branchId);// AAP02
							
							if (nuevaSucursal.length() > 0) {// AAP02
								if (entrega.equals("DEST_OCURRE")) {//AAP07
									/*valida sucursal de entrega ocurre de cliente por excepcion*///AAP07
									String nuevaSucursalOcu = suc.obtieneConfigSucursalOcurre(con, destClientId, AM_ADDR_CODE);//AAP07							
									
									if (!nuevaSucursalOcu.equals("")){//AAP07
										/*si coinciden los sites de la sucursa de excepcion y la sucursal destino original, 
										 * se asigna la nueva sucursal excepcion de ocurre*///AAP07
										if (nuevaSucursalOcu.substring(0,2).equals(nuevaSucursal.substring(0,2))) {//AAP07
											nuevaSucursal = nuevaSucursalOcu;//AAP07
										}//AAP07
									}//AAP07
								}//AAP07
								String branch = nuevaSucursal;
								
								// obtiene clave y descripcion de sucursal
								pst = con.prepareStatement("select BM_BRNC_NAME from sys_brnc_mstr where BM_BRNC_SITE_ID = ?  AND BM_BRNC_ID = ?");
								pst.setString(1, destSiteId);
								pst.setString(2, branch);
								rset = pst.executeQuery();
								if (rset.next()) {
									brname = rset.getString(1);//AAP01
									// AAP//AccessLog.Log("before adding branch"+branch);
									values.put("branch", branch);
									values.put("branchname", brname);
									// AAP//AccessLog.Log("before adding branch"+brname);
								}
							}
						}
					//}//AAP02
				}
				
				result.add(values.clone());
				values.clear();
			}
			if (rs != null) {
				rs.close();
			}			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getLovRecords()Error_:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rset != null) {
					rset.close();
				}
				if (cst != null) {
					cst.close();
				}
				if (pst != null) {
					pst.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getLovRecords()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}

	public String  getDeliveryType(Connection con,HttpSession session)
	{
		String deliveryType ="";
		Global global =(Global)session.getAttribute("sGlobal");
		ResultSet rs = null;
		try{
		String groupClientId = getGroupClientId(con,session);
		String query = "{call pack_web.pro_ftch_srvs_covd(?,?,?,?,?) }";
		CallableStatement cst = con.prepareCall(query);
		cst.setString(1,global.tarifType);
		cst.registerOutParameter(2,Types.VARCHAR);
		cst.registerOutParameter(3,Types.VARCHAR);
		cst.registerOutParameter(4,Types.VARCHAR);
		cst.registerOutParameter(5,Types.VARCHAR);
		
		//ResultSet rs=cst.executeQuery();
		cst.executeQuery();
		String ead=null,cod=null,ack=null,inv=null;
		
		//if(rs.next()){
		ead=cst.getString(2);
		ack=cst.getString(3);
		cod=cst.getString(4);
		inv=cst.getString(5);
		//}
		
		//if (rs!=null)
		//	rs.close();
		if(cst!=null)
			cst.close();
		
		
		
		//AAP//AccessLog.Log("EAD VALUE "+ead);
		
		if(ead==null){
			//AAP//AccessLog.Log("Inside ead null");
			String query1 = "select pack_web.fun_dflt_dlvy(?,?) from dual";
			PreparedStatement pst = con.prepareStatement(query1);
			pst.setString(1,groupClientId);
			pst.setString(2,ead);
			rs = pst.executeQuery();
			if(rs.next()){
				if(rs.getString(1)!=null && rs.getString(1).equalsIgnoreCase("TRUE"))
					deliveryType="H";
				else
					deliveryType="O";
				//AAP//AccessLog.Log("Inside deliveryType deliveryType "+deliveryType);
			}
			
			if(rs!=null)
				rs.close();
			if(pst!=null)
				pst.close();
		}
		else
			deliveryType="H";
		}catch(Exception e)
		{
			AccessLog.Log("exception "+e);
		}
		return deliveryType;
	}
	
	
	public String getGroupClientId(Connection con,HttpSession session)throws Exception {
		Global global = (Global)session.getAttribute("sGlobal");
		String groupIdQuery = "select pack_web.FUN_CHCK_GRUP_CLNT(?)as groupid from dual";
		PreparedStatement pst = con.prepareStatement(groupIdQuery);
		pst.setString(1,global.clientId);
		ResultSet rs = pst.executeQuery();
		String groupClientId="";
		while(rs.next()){
			groupClientId = rs.getString("groupid");
		}
		if(rs!=null)
			rs.close();
		if(pst!=null)
			pst.close();
		//AAP//AccessLog.Log("GROUPCLIENTID"+groupClientId);
		
		return groupClientId;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList getLovRecordsByCode(Connection con, String addressCode, String webOrigBranchId, String webClientId) {
		CallableStatement cst = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		HashMap values = new HashMap(27);
		ArrayList result = new ArrayList();
		try {
			//AAP//AccessLog.Log("Inside the Records fetching in JavDestinationAddressRecords");
			String query =	"	SELECT AM_ADDR_CODE,AM_DRNR,AM_STRT_NAME, "+
							"       AM_PHNO1,AM_SUIT_NO,AM_FLOR_NO,"+
							"       AM_ADDR_STYP,AM_ADDR_DEFN_TYPE,"+
							"       AM_ADDR_REF_NO,AM_GETY_LEVL,"+
							"       AM_GETY_TYPE,AM_GETY_CODE, AM_COMT, AM_MAIL_ID "+
							"	FROM SYS_ADDR_MSTR WHERE AM_ADDR_CODE = ?";
			
			pst = con.prepareStatement(query);
			
			pst.setString(1,addressCode);
					
			//AAP//AccessLog.Log("before while loop"+branchId+""+destClientId);
			//AAP//AccessLog.Log("before while loop query is"+""+destClientId);
			rs=pst.executeQuery();
			String	AM_ADDR_CODE=null,AM_DRNR=null,AM_STRT_NAME=null,AM_PHNO1=null,AM_SUIT_NO=null,AM_FLOR_NO=null,
				AM_ADDR_STYP=null,AM_ADDR_DEFN_TYPE=null,AM_ADDR_REF_NO=null,AM_GETY_LEVL=null,AM_GETY_TYPE=null,AM_GETY_CODE=null, AM_COMT = null, AM_MAIL_ID = null;
			
			String u11=null,u12=null,u13=null,u14=null,u15=null,u16=null,u17=null;
			String c11=null,c12=null,c13=null,c14=null,c15=null,c16=null,zipcode=null;

			while(rs.next()){
				//AAP//AccessLog.Log("inside while loop");
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
				AM_COMT = rs.getString(13) == null ? "" : rs.getString(13);//AM_COMT comentario de direccion
				AM_MAIL_ID = rs.getString(14) == null ? "" : rs.getString(14);//AM_MAIL_ID correo electronico destinatario
				
				AM_ADDR_CODE =(AM_ADDR_CODE!=null?AM_ADDR_CODE:"");
				AM_DRNR =(AM_DRNR!=null?AM_DRNR:"");
				AM_STRT_NAME =(AM_STRT_NAME!=null?AM_STRT_NAME:"");
				AM_PHNO1 =(AM_PHNO1!=null?AM_PHNO1:"");
				AM_SUIT_NO =(AM_SUIT_NO!=null?AM_SUIT_NO:"");
				AM_FLOR_NO =(AM_FLOR_NO!=null?AM_FLOR_NO:"");
				AM_ADDR_STYP =(AM_ADDR_STYP!=null?AM_ADDR_STYP:"");
				AM_ADDR_DEFN_TYPE =(AM_ADDR_DEFN_TYPE!=null?AM_ADDR_DEFN_TYPE:"");
				AM_ADDR_REF_NO=(AM_ADDR_REF_NO!=null?AM_ADDR_REF_NO:"");
				AM_GETY_LEVL =(AM_GETY_LEVL!=null?AM_GETY_LEVL:"");
				AM_GETY_TYPE =(AM_GETY_TYPE!=null?AM_GETY_TYPE:"");
				AM_GETY_CODE =(AM_GETY_CODE!=null?AM_GETY_CODE:"");				
				
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
				
				u11 =(cst.getString(6)!=null?cst.getString(6):"");
				u12 =(cst.getString(7)!=null?cst.getString(7):"");
				u13 =(cst.getString(8)!=null?cst.getString(8):"");
				u14 =(cst.getString(9)!=null?cst.getString(9):"");
				u15 =(cst.getString(10)!=null?cst.getString(10):"");
				u16 =(cst.getString(11)!=null?cst.getString(11):"");			
				u17 =(cst.getString(12)!=null?cst.getString(12):"");
				zipcode = (cst.getString(13)!=null?cst.getString(13):"");
				c11 =(cst.getString(14)!=null?cst.getString(14):"");
				c12 =(cst.getString(15)!=null?cst.getString(15):"");
				c13 =(cst.getString(16)!=null?cst.getString(16):"");
				c14 =(cst.getString(17)!=null?cst.getString(17):"");
				c15 =(cst.getString(18)!=null?cst.getString(18):"");
				c16 =(cst.getString(19)!=null?cst.getString(19):"");
				
				/*AccessLog.Log("CST 6 "+cst.getString(6));
				AccessLog.Log("CST 7 "+cst.getString(7));
				AccessLog.Log("CST 8 "+cst.getString(8));
				AccessLog.Log("CST 9 "+cst.getString(9));
				AccessLog.Log("CST 10 "+cst.getString(10));
				AccessLog.Log("CST 11 "+cst.getString(11));
				AccessLog.Log("CST 12 "+cst.getString(12));
				AccessLog.Log("CST 13 "+cst.getString(13));
				AccessLog.Log("CST 14 "+cst.getString(14));
				AccessLog.Log("CST 15 "+cst.getString(15));
				AccessLog.Log("CST 16 "+cst.getString(16));
				AccessLog.Log("CST 17 "+cst.getString(17));
				AccessLog.Log("CST 18 "+cst.getString(18));
				AccessLog.Log("CST 19 "+cst.getString(19));*/
				
				//values = new HashMap();			
				values.put("AM_ADDR_CODE",AM_ADDR_CODE);
				values.put("AM_DRNR",AM_DRNR);
				values.put("AM_STRT_NAME",AM_STRT_NAME);
				values.put("AM_PHNO1",AM_PHNO1);
				values.put("AM_SUIT_NO",AM_SUIT_NO);
				values.put("AM_FLOR_NO",AM_FLOR_NO);
				values.put("AM_ADDR_STYP",AM_ADDR_STYP);
				values.put("AM_ADDR_DEFN_TYPE",AM_ADDR_DEFN_TYPE);
				//AAP//System.out.println("AM_GETY_LEVL"+AM_GETY_LEVL);
				values.put("AM_GETY_LEVL",AM_GETY_LEVL);
				values.put("AM_GETY_TYPE",AM_GETY_TYPE);
				//AAP//System.out.println("AM_GETY_TYPE"+AM_GETY_TYPE);
				values.put("AM_GETY_CODE",AM_GETY_CODE);
				//AAP//System.out.println("AM_GETY_CODE"+AM_GETY_CODE);
				values.put("AM_COMT", AM_COMT);				
				values.put("AM_MAIL_ID", AM_MAIL_ID);//AAP05
				
				values.put("u11",u11);
				values.put("u12",u12);
				values.put("u13",u13);
				values.put("u14",u14);
				values.put("u15",u15);
				values.put("u16",u16);
				values.put("u17",u17);
				values.put("Zipcode",zipcode);
				values.put("c11",c11);
				values.put("c12",c12);
				values.put("c13",c13);
				values.put("c14",c14);
				values.put("c15",c15);
				values.put("c16",c16);
				if(cst != null){
					cst.close();
				}			
				
				query = "SELECT CC_BRNC_ORGN FROM SYS_CLNT_CCOSTO WHERE CC_CLNT_ID = ? AND CC_ADDR_CODE = ? AND ROWNUM = ?";
				
				pst1 = con.prepareStatement(query);
				
				pst1.setString(1, webClientId);
				pst1.setString(2, addressCode);
				pst1.setString(3, "1");
				
				rs1=pst1.executeQuery();
				
				String valBrncID = "";
				
				if (rs1.next()) {
					valBrncID = rs1.getString(1) == null ? "" : rs1.getString(1); 
				}
				
				if (rs1 != null) {
					rs1.close();
				}
				if (pst1 != null) {
					pst1.close();
				}
				
				if (valBrncID.length()==0) {
					if (AM_ADDR_DEFN_TYPE.equals("Y")) {
						//query = "SELECT COLO_SUCURSAL FROM DW_COBERTURA_VIEW WHERE COLO_ZIPCODE = ? AND COLO_ID = ? AND COL_LEVEL = ? AND COL_TYPE = ?";
						query = "SELECT nvl(SUC_RAD, SUCURSAL) FROM PCOBERTURA_VIEW WHERE COLO_ZIPCODE = ? AND COD_COLO = ? AND COL_LEVL = ? AND COL_TYPE = ? AND SUCURSAL IS NOT NULL";
						
						pst1 = con.prepareStatement(query);
						
						pst1.setString(1, zipcode);
						pst1.setString(2, AM_GETY_CODE);
						pst1.setString(3, AM_GETY_LEVL);
						pst1.setString(4, AM_GETY_TYPE);
						
						rs1=pst1.executeQuery();
						
						if (rs1.next()) {
							valBrncID = rs1.getString(1) == null ? "" : rs1.getString(1); 
						}
					} else {
						valBrncID = webOrigBranchId;
					}	
				}		
				values.put("branch", valBrncID);
				result.add(values.clone());
				values.clear();
			}			
		} catch (Exception e) {
			AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getLovRecordsByCode()Error_1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (rs1 != null) {
					rs1.close();
				}
				if (cst != null) {
					cst.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (pst1 != null) {
					pst1.close();
				}
			} catch (Exception e2) {
				AccessLog.Log(concatena.delete(0,concatena.length()).append(msgError).append("getLovRecordsByCode()Error_2:").append(e2).toString());
				e2.printStackTrace();
			}
		}
		return result;
	}
}