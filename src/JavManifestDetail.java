/**
 * File Name    : 
 * Description  :  
 * Date Written : 2/3/2003
 * @author 	    :  Angshuman Debnath
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * ----------------------------------------------------------------- 
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción:  
 * ----------------------------------------------------------------- 
 * Clave: 
 * Autor: 
 * Fecha: 
 * Descripción:  
 * ----------------------------------------------------------------- 
 */ 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bean.Global;
import logger.AccessLog;


public class JavManifestDetail {
	private StringBuffer cnct = new StringBuffer();	
	private final String msgErr = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	public JavManifestDetail(){
		//AAP//AccessLog.Log("JavManifestDetailAction .................");
	}
	/**
	 * This is the method called on by ActionServlet
	 * when a request is made.
	 */
	public String getManifestDetail(Connection con,  
									JavManifestDetailsForm mdform,
									HttpServletRequest request ){

		String strSqlQuery="";
		PreparedStatement pst=null;
		ResultSet rs=null;
		HttpSession session = request.getSession(false);//Added by rama
		
		Integer totRec=0;
		double maxPages=0;
		String tipoMnft = "";
		Global global = (Global) session.getAttribute("sGlobal");
		String displayAmount = "Y";
		String amount = "";
		try{
			if(global != null && (global.getDisplayAmountFlag() != null) ) {
				displayAmount = global.getDisplayAmountFlag();
			}
			
			if (displayAmount.equalsIgnoreCase("Y")) {
				amount = "to_char(GH_GUIA_AMNT, '$999990.99')";
			} else {
				amount = "'**********'";
			}
			//Changed by rama
			strSqlQuery= "select "+
						 "'<tr>\n'|| "+
						 "'    <td >&nbsp;</td>\n'|| "+
						 "'    <td class=\"mrbtd\" align=\"left\">'||GH_ORGN_BRNC_ID||GH_FORM_NO ||'</td>\n'|| "+
						 "'    <td class=\"mrbtd\" align=\"left\">'||GH_GUIA_NO ||'</td>\n'|| "+
						 "'    <td class=\"mrbtd\">'|| substr(pack_web.fun_ftch_brnc_name(GH_dest_brnc_id),1,25) ||'</td>\n'|| "+
						 "'    <td class=\"mrbtd\">'||substr(GH_DEST_CLNT_NAME,1,25)||'</td>\n'|| "+
						 "'    <td class=\"mrbtd\" align=\"right\">'||" +
						 //"to_char(GH_GUIA_AMNT, '$999990.99')||'</td>\n'|| "+  Commented and Updated below on 01/12/2009
						 //"decode((select WC_DISPLAY_AMOUNTS from WEB_CLNT_MSTR where WC_CLNT_ID = b_head.GH_ORGN_CLNT_ID),'Y',to_char(GH_GUIA_AMNT, '$999990.99'),'**********')||'</td>\n'|| "+
						 amount +"||'</td>\n'|| "+
						 "'    <td  class=\"mrbtd\" align=\"right\">'||GH_NUMB_PACK||'</td>\n'|| "+
						 "'    <td >\n'|| "+
						 "'      <a href=\"webguiadetails.do?fromFlag=MD&hidGuiaNumber='||GH_GUIA_NO ||'\">Detalle</a></td>\n'|| "+
						 "'  </tr> \n' "+
						 " from bok_guia_head b_head "+

						 " where GH_GUIA_REFR_NO = ? "+
						 " and gh_guia_type='H' ";

			try{

				pst = con.prepareStatement(strSqlQuery,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				pst.setString(1,request.getSession(false).getAttribute("MANIFEST_NUMBER").toString());
			} catch(Exception se) {
				AccessLog.Log(se);
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getManifestDetail()_ErrorCon1:").append(se).toString());
				se.printStackTrace();
			}



			//AccessLog.Log("Query......"+strSqlQuery);
			rs =pst.executeQuery();

			boolean rsFlag=rs.next();

			// count the total number of Records.
			if(rsFlag){
				rs.last();
				totRec=rs.getRow();
				//AAP//AccessLog.Log("TotalREcords....."+totRec);
				rs.beforeFirst();
			}

			maxPages=Math.ceil(totRec.doubleValue()/10);
			mdform.setMaxpageindex(((int)maxPages) +"");
			try{
				Integer.parseInt(mdform.getPageindex());
			}catch(Exception e)	{
				mdform.setPageindex("0");
				mdform.setCurrentpage("0");
				//AccessLog.Log(e);
			}


			rs.beforeFirst();
			StringBuffer mrbGuiaInfo = new StringBuffer();
			if (Integer.parseInt(mdform.getPageindex()) != 0){
				rs.absolute((Integer.parseInt(mdform.getPageindex())*10 ));
				//AAP//AccessLog.Log("rs.absolute  :"+((Integer.parseInt(mdform.getPageindex())*10 )));
			}
			int startIndex = 1;
			while(rs.next()){
				if(startIndex <= 10){
					mrbGuiaInfo.append( rs.getString(1));
					startIndex++;
				}else
					break;
			}
			while(startIndex <= 10){
				mrbGuiaInfo.append(" <tr>\n");
				mrbGuiaInfo.append("   <td >&nbsp;</td>\n");
				mrbGuiaInfo.append("   <td class=\"mrbtd\">&nbsp;</td>\n");
				mrbGuiaInfo.append("   <td class=\"mrbtd\">&nbsp;</td>\n");
				mrbGuiaInfo.append("   <td class=\"mrbtd\">&nbsp;</td>\n");
				mrbGuiaInfo.append("   <td class=\"mrbtd\">&nbsp;</td>\n");
				mrbGuiaInfo.append("   <td class=\"mrbtd\">&nbsp;</td>\n");
				mrbGuiaInfo.append("   <td  class=\"mrbtd\">&nbsp;</td>\n");
				mrbGuiaInfo.append("   <td >Detalle</td>\n");
				mrbGuiaInfo.append(" </tr>\n");
				startIndex++;
			}

			request.setAttribute("mrbGuiaInfo", mrbGuiaInfo.toString());

			if( rs!= null) rs.close();
			if(pst!=null)  pst.close();

			pst = con.prepareStatement(	"select MD_MNFT_STUS, MD_NUMB_ORDR, "+
										"to_char(MD_MNFT_AMNT, '$999990.99'), "+
										"MD_NUMB_PACK, to_char(MD_PLAN_COLL_DATE+pack_web.fun_ftch_time_diff(?)/86400,'DD/MM/YYYY HH24:MI'), "+
										"MD_DOCU_TYPE "+
										"from WEB_MNFT_DETL where MD_MNFT_NO = ?");
			
			pst.setString(1,(String)session.getAttribute("sAssignedBranch"));//Added by rama
			pst.setString(2,request.getSession(false).getAttribute("MANIFEST_NUMBER").toString());
			//AccessLog.Log("MANIFEST NUMBE "+request.getSession(false).getAttribute("MANIFEST_NUMBER").toString());
			rs =pst.executeQuery();
			if( rs.next() ) {
				//AccessLog.Log("MANIFEST_STATUS..........>"+rs.getString(1));
				request.setAttribute("MANIFEST_STATUS",rs.getString(1) != null ? rs.getString(1) :"&nbsp;");
				request.setAttribute("NUMBER_OF_GUIAS",rs.getString(2) != null ? rs.getString(2) :"&nbsp;");
				request.setAttribute("MANIFEST_AMOUNT",rs.getString(3) != null ? rs.getString(3) :"&nbsp;" );
				request.setAttribute("NUMBER_OF_PACKAGES",rs.getString(4) != null ? rs.getString(4) :"&nbsp;");
				//AAP//AccessLog.Log("INSIDE MANIFEST DETAIL 1 "+rs.getString(5));
				request.setAttribute("PREFERED_COLLECTION_TIME",rs.getString(5) != null ? rs.getString(5) :"&nbsp;");
				tipoMnft = rs.getString(6) != null ? rs.getString(6) :"";

			} else {
				request.setAttribute("MANIFEST_STATUS","&nbsp;");
				request.setAttribute("NUMBER_OF_GUIAS","&nbsp;");
				request.setAttribute("MANIFEST_AMOUNT","&nbsp;" );
				request.setAttribute("NUMBER_OF_PACKAGES","&nbsp;");
				request.setAttribute("PREFERED_COLLECTION_TIME","&nbsp;");
				return tipoMnft;
			}

			if( rs!= null) rs.close();
			if(pst!=null)  pst.close();

			pst = con.prepareStatement("select PM_VLUE1_DESC from sys_parm_mstr where PM_MDUL_ID = 'WEB' AND PM_PARM_TYPE = 'MANIFEST_STATUS' AND PM_PARM_CODE1 = ?");
			pst.setString(1,request.getAttribute("MANIFEST_STATUS").toString());
			rs =pst.executeQuery();
			if( rs.next() ) {
				request.setAttribute("MANIFEST_STATUS",rs.getString(1) != null ? rs.getString(1) :"&nbsp;");
			}

		}catch(Exception e){
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getManifestDetail()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally{
			try{
				if(rs!=null)
					rs.close();
				if(pst!=null)
					pst.close();

			} catch(Exception e){
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getManifestDetail()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}		
		return tipoMnft;
	}
	/*public String getOrderDetail(Connection con,  
			JavManifestDetailsForm mdform,
			HttpServletRequest request ) {
		String returnValue = "";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(	"select MD_MNFT_STUS, MD_NUMB_ORDR, "+
					"to_char(MD_MNFT_AMNT, '$999990.99'), "+
					"MD_NUMB_PACK, to_char(MD_PLAN_COLL_DATE+pack_web.fun_ftch_time_diff(?)/86400,'DD/MM/YYYY HH24:MI'), "+
					"MD_DOCU_TYPE "+
					"from WEB_MNFT_DETL where MD_MNFT_NO = ?");
		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getOrderDetail()_Error1:").append(e).toString());
			e.printStackTrace();
		}
		return returnValue;
	}*/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String printManifestDetail(Connection con,
									  JavManifestDetailsForm mdform,
									  HttpServletRequest request ){

		String strSqlQuery = "";
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList manifestDetails = new ArrayList();
		HttpSession session = request.getSession(false);// Added by rama
		HashMap manifestValues = null;		
		
		PreparedStatement refPst = null;
		ResultSet refRs = null;
		try {
			strSqlQuery = cnct.delete(0,cnct.length())
					.append("select ")
					.append(" GH_FORM_NO,")
					.append(" GH_GUIA_NO,")						 
					.append(" substr(GH_ORGN_CLNT_NAME,1,25) GH_ORGN_CLNT_NAME,")
					.append(" substr(GH_DEST_CLNT_NAME,1,25) GH_DEST_CLNT_NAME,")
					.append(" to_char(GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT,")
					.append(" GH_NUMB_PACK,")
					.append(" GH_DEST_BRNC_ID,")//added by rama
					.append(" GH_TOTL_WGHT,")
					.append(" GH_TOTL_VLUM,")
					.append(" GH_DOCU_TYPE,")
					.append(" GH_ORGN_CLNT_ID,")
					.append(" GH_PYMT_MODE,")
					.append(" GH_DOCU_TYPE")
					.append(" FROM bok_guia_head b_head")
					.append(" where GH_GUIA_REFR_NO = ?")
					.append(" and gh_guia_type = ?").toString();

			try {
				pst = con.prepareStatement(strSqlQuery);
				pst.setString(1,request.getSession(false).getAttribute("MANIFEST_NUMBER").toString());
				pst.setString(2,"H");
			} catch (Exception se) {				
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("printManifestDetail()_SQLError1:").append(se).toString());
				se.printStackTrace();
			}
			//AccessLog.Log("Query......"+strSqlQuery);
			rs =pst.executeQuery();

			
			
			//PreparedStatement refPst = null;
			String refQuery = cnct.delete(0,cnct.length())
					.append("SELECT GR_GUIA_REFR FROM WEB_GUIA_REFR ")
					.append("WHERE GR_GUIA_NO = ? AND ")
					.append("GR_GUIA_TYPE = ? AND ")
					.append("GR_DOCU_TYPE = ?").toString();
			//ResultSet refRs = null;
			
			manifestValues = new HashMap();
			String refNumber = "";	
			String destBranchName = "";
			String origClntId = "";
			while(rs.next()) {
				
				refPst = con.prepareStatement(refQuery);
				refPst.setString(1,rs.getString("GH_GUIA_NO"));
				refPst.setString(2,"H");
				//refPst.setString(3,"Q");
				refPst.setString(3,rs.getString("GH_DOCU_TYPE"));
				refRs = refPst.executeQuery();
				refNumber = "";	
				
				if (refRs.next()) {
					refNumber = refRs.getString(1);
				} else {
					refNumber = "&nbsp;";
				}
				
				manifestValues.put("GH_FORM_NO",rs.getString("GH_FORM_NO")==null?"":rs.getString("GH_FORM_NO"));
				manifestValues.put("GH_GUIA_NO",rs.getString("GH_GUIA_NO")==null?"":rs.getString("GH_GUIA_NO"));				
				manifestValues.put("GH_ORGN_CLNT_NAME",rs.getString("GH_ORGN_CLNT_NAME")==null?"":rs.getString("GH_ORGN_CLNT_NAME"));
				manifestValues.put("GH_DEST_CLNT_NAME",rs.getString("GH_DEST_CLNT_NAME")==null?"":rs.getString("GH_DEST_CLNT_NAME"));
				manifestValues.put("GH_GUIA_AMNT",rs.getString("GH_GUIA_AMNT")==null?"":rs.getString("GH_GUIA_AMNT"));
				manifestValues.put("GH_NUMB_PACK",rs.getString("GH_NUMB_PACK")==null?"":rs.getString("GH_NUMB_PACK"));
				manifestValues.put("GH_TOTL_WGHT",rs.getString("GH_TOTL_WGHT")==null?"":rs.getString("GH_TOTL_WGHT"));
				manifestValues.put("GH_TOTL_VLUM",rs.getString("GH_TOTL_VLUM")==null?"":rs.getString("GH_TOTL_VLUM"));
				manifestValues.put("GR_GUIA_REFR",refNumber);
				manifestValues.put("GH_PYMT_MODE",rs.getString("GH_PYMT_MODE")==null?"":rs.getString("GH_PYMT_MODE"));
				manifestValues.put("GH_DOCU_TYPE",rs.getString("GH_DOCU_TYPE")==null?"":rs.getString("GH_DOCU_TYPE"));
				//Added by rama
				destBranchName = getDestinationBranchName(con,rs.getString("GH_DEST_BRNC_ID"));
				
				manifestValues.put("DESTBRANCNHNAME",destBranchName);
				manifestDetails.add(manifestValues.clone());
				manifestValues.clear();
				
				origClntId = rs.getString("GH_ORGN_CLNT_ID");
				
				if( refRs != null) 
					refRs.close();
				if(refPst!=null)  
					refPst.close();
			}
			
			//AccessLog.Log("Manifestdtails Values ..."+manifestDetails.toString());
			request.setAttribute("printManifestDetails",manifestDetails);

			if( refRs != null) 
				refRs.close();
			if(refPst!=null)  
				refPst.close();
			
			
			if( rs!= null) 
				rs.close();
			if(pst!=null)  
				pst.close();

			pst = con.prepareStatement(	cnct.delete(0,cnct.length())
										.append("select MD_MNFT_STUS, MD_NUMB_ORDR,")
										.append(" to_char(MD_MNFT_AMNT, '$999990.99'),")
										.append(" MD_NUMB_PACK, to_char(MD_PLAN_COLL_DATE+pack_web.fun_ftch_time_diff(?)/86400,'DD/MM/YYYY HH24:MI'), ")
										.append(" PACK_WEB.fun_ftch_ccosto_descr(?, MD_FLAG_3), ")
										.append(" MD_FLAG_2, MD_PYMT_MODE ")
										.append(" from WEB_MNFT_DETL where MD_MNFT_NO = ?").toString());
			
			pst.setString(1, (String)session.getAttribute("sAssignedBranch"));//Added by rama
			pst.setString(2, origClntId);
			pst.setString(3, request.getSession(false).getAttribute("MANIFEST_NUMBER").toString());
			rs = pst.executeQuery();
			String docuType = "";
			String pymtMode = "";
			String tipoManifiesto = "";
			if( rs.next() ){
				
				request.setAttribute("MANIFEST_STATUS",rs.getString(1) != null ? rs.getString(1) :"&nbsp;");
				request.setAttribute("NUMBER_OF_GUIAS",rs.getString(2) != null ? rs.getString(2) :"&nbsp;");
				request.setAttribute("MANIFEST_AMOUNT",rs.getString(3) != null ? rs.getString(3) :"&nbsp;" );
				request.setAttribute("NUMBER_OF_PACKAGES",rs.getString(4) != null ? rs.getString(4) :"&nbsp;");				
				request.setAttribute("PREFERED_COLLECTION_TIME",rs.getString(5) != null ? rs.getString(5) :"&nbsp;");
				request.setAttribute("centroCosto",rs.getString(6) != null ? rs.getString(6) :"&nbsp;");
				docuType = rs.getString(7) == null ? "&nbsp;" :rs.getString(7);
				pymtMode = rs.getString(8) == null ? "&nbsp;" :rs.getString(8);
				
				if (pymtMode.equals("PAID") && docuType.equals("Q")) {
					tipoManifiesto = "GUIAS PAGADAS";
				} else if (pymtMode.equals("TO_PAY") && docuType.equals("Q")) {
					tipoManifiesto = "FLETE POR COBRAR";
				} else if (pymtMode.equals("PAID") && docuType.equals("P")) {
					tipoManifiesto = "PREPAGO";
				}
				request.setAttribute("tipoManifiesto", tipoManifiesto);

			}else{
				request.setAttribute("MANIFEST_STATUS","&nbsp;");
				request.setAttribute("NUMBER_OF_GUIAS","&nbsp;");
				request.setAttribute("MANIFEST_AMOUNT","&nbsp;" );
				request.setAttribute("NUMBER_OF_PACKAGES","&nbsp;");
				request.setAttribute("PREFERED_COLLECTION_TIME","&nbsp;");
				request.setAttribute("centroCosto","&nbsp;");
				request.setAttribute("tipoManifiesto", "&nbsp;");
				return "";
			}

			if( rs!= null) rs.close();
			if(pst!=null)  pst.close();

			pst = con.prepareStatement("select PM_VLUE1_DESC from sys_parm_mstr where PM_MDUL_ID = ? AND PM_PARM_TYPE = ? AND PM_PARM_CODE1 = ?");
			pst.setString(1,"WEB");
			pst.setString(2,"MANIFEST_STATUS");			
			pst.setString(3,request.getAttribute("MANIFEST_STATUS").toString());
			
			rs = pst.executeQuery();
			if( rs.next() ) {
				request.setAttribute("MANIFEST_STATUS",rs.getString(1) != null ? rs.getString(1) :"&nbsp;");
			}
		} catch(Exception e) {			
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("printManifestDetail()_Error1:").append(e).toString());
			e.printStackTrace();
		} finally {
			try{
				if(rs!=null)
					rs.close();
				if(pst!=null) {
					pst.close();
				}
				
				if (refRs!=null) {
					refRs.close();
				}
				
				if (refPst!=null) {
					refPst.close();
				}	
			} catch (Exception e){
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("printManifestDetail()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return "";
	}
	
	//Added by rama
	public String getDestinationBranchName(Connection con,String destBranchId) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		String branchName = "";
		try {
			String branchQuery = " select pack_web.fun_ftch_brnc_name(?) from dual ";
			pst = con.prepareStatement(branchQuery);
			pst.setString(1,destBranchId);
			rs = pst.executeQuery();			
			
			if(rs.next())
				branchName = rs.getString(1);

		} catch (Exception e) {
			AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDestinationBranchName()_Error:").append(e).toString());
			e.printStackTrace();
		} finally {
			try{
				if(rs!=null)
					rs.close();
				if(pst!=null) {
					pst.close();
				}
				
			} catch (Exception e){
				AccessLog.Log(cnct.delete(0,cnct.length()).append(msgErr).append("getDestinationBranchName()_Error2:").append(e).toString());
				e.printStackTrace();
			}
		}
		return branchName;
	}
}