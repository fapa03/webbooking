
/**
 * File Name    : GuiaDetailDataBean.java
 * Description  :This is the Action Class For LoginForm
 *				 to handle and Control the Inputs of that FormBean. 
 * Date Written :  28-Feb-2003
 * @author 	    :  D.SivaKumar
 */

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import logger.AccessLog;

class WebGuiaDetailDataBean
{
	public WebGuiaDetailDataBean(){		
		//System.out.println("Inside WebGuiaDetailDataBean");//commented and below added by B.Emerson on 30/04/2004
		//AAP//AccessLog.Log("Inside WebGuiaDetailDataBean");
		
	}
	
	public void showGuiaDetails(java.sql.Connection con,JavWebGuiaDetailForm gf,String gNumber) throws Exception{
		String strSqlQuery;
		String strSqlQuery1;
		PreparedStatement pst=null;		
		CallableStatement cst=null;		
		ResultSet rs=null;	
		
		
		/* Get All parameter values that are used to 
		 *Fetch the Related Information depending on these Parameters. ...
		*/	
		
		String pstrGuiaNo = gNumber;
		gf.setGuiaNumber(pstrGuiaNo);
		
		String pstrStatus;		
		String pstrOrginBranchId;
		String pstrDestBranchId;
		String pstrOriginClientId;
		String pstrGuiaType;
		String pstrGuiaActiveFalg;
		String pstrDeliverer;
		String pstrDestClientId;
		String pstrDestClientName;
		//System.out.println("Paramaeter GuiaNumber ..."+pstrGuiaNo);			
		
		try{			
			/* Get Guia Type and ActivFlag
			*This Guia Type and ActivFalg is Send as Paramater to the Function FUN_FTCH_DELY_TYPE
			*/
			
			strSqlQuery1="SELECT GH_GUIA_TYPE,GH_ACTV_FLAG  FROM BOK_GUIA_HEAD  WHERE GH_GUIA_NO = ?  AND GH_GUIA_TYPE=?  AND GH_ACTV_FLAG = ?";
			
			pst=con.prepareStatement(strSqlQuery1);
			pst.setString(1,pstrGuiaNo);
			pst.setString(2,"H");
			pst.setString(3,"A");
			rs=pst.executeQuery();			
			if(rs.next()){
				pstrGuiaType=rs.getString("GH_GUIA_TYPE");
				pstrGuiaActiveFalg=rs.getString("GH_ACTV_FLAG");
			}
			else{
				if(rs!=null)
					rs.close();
				if(pst!=null)
					pst.close();
				strSqlQuery1="SELECT GH_GUIA_TYPE,GH_ACTV_FLAG FROM BOK_GUIA_HEAD  WHERE GH_GUIA_NO =?  AND GH_GUIA_TYPE IN(?,?)  AND GH_ACTV_FLAG =?";
				pst=con.prepareStatement(strSqlQuery1);
				pst.setString(1,pstrGuiaNo);
				pst.setString(2,"N");
				pst.setString(3,"S");
				pst.setString(4,"A");
				rs=pst.executeQuery();				
				if(rs.next()) {
					
					pstrGuiaType=rs.getString("GH_GUIA_TYPE");
					pstrGuiaActiveFalg=rs.getString("GH_ACTV_FLAG");
					
				}else{
					pstrGuiaType="";
					pstrGuiaActiveFalg="";
				}
			}	
			
			if(rs!=null)
				rs.close();
			if(pst!=null)
				pst.close();
		
			
			/*
			*   Fetch Guia Details using  GuiaType and Guia Active Flag and Guia Number....
			*/
			strSqlQuery= "SELECT GH_FORM_NO,GH_GUIA_STUS,to_char(GH_ISSE_DATE,'YYYY/MM/DD') ISSE_DATE ,to_char(GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT , GH_TOTL_DECL_VLUE,GH_ORGN_BRNC_ID,GH_ORGN_CLNT_ID,GH_ORGN_CLNT_NAME,GH_DEST_BRNC_ID,GH_DEST_CLNT_ID,GH_DEST_CLNT_NAME,GH_DLVY_DATE,GH_ORG_SITE_ID,GH_DEST_SITE_ID FROM BOK_GUIA_HEAD WHERE GH_GUIA_NO=? AND GH_GUIA_TYPE=? AND GH_ACTV_FLAG=?";
			pst = con.prepareStatement(strSqlQuery);
			//System.out.println("GUIA NUMBER IN WEB BEAN "+pstrGuiaNo);//commented and below added by B.Emerson on 30/04/2004
			//AccessLog.Log("GUIA NUMBER IN WEB BEAN "+pstrGuiaNo);
			pst.setString(1,pstrGuiaNo);
			//System.out.println("GUIA TYPE IN WEB BEAN "+pstrGuiaType);//commented and below added by B.Emerson on 30/04/2004
			//AccessLog.Log("GUIA TYPE IN WEB BEAN "+pstrGuiaType);
			pst.setString(2,pstrGuiaType);
			//System.out.println("GUIA FLAG IN WEB BEAN "+pstrGuiaActiveFalg);//commented and below added by B.Emerson on 30/04/2004
			//AccessLog.Log("GUIA FLAG IN WEB BEAN "+pstrGuiaActiveFalg);
			pst.setString(3,pstrGuiaActiveFalg);
			//System.out.println("Query......"+strSqlQuery);
			rs =pst.executeQuery();	
			
			if(rs.next()){	
				
				gf.setFormNumber(rs.getString("GH_FORM_NO"));
				gf.setStatus(rs.getString("GH_GUIA_STUS"));
				gf.setIssueDate(rs.getString("ISSE_DATE"));
				gf.setGuiaAmount(rs.getString("GH_GUIA_AMNT"));
				gf.setTotalValue(rs.getString("GH_TOTL_DECL_VLUE"));	
				gf.setOrginBranchId(rs.getString("GH_ORGN_BRNC_ID"));				
				gf.setOrginClientId(rs.getString("GH_ORGN_CLNT_ID"));				
				gf.setOrginClientName(rs.getString("GH_ORGN_CLNT_NAME"));
				gf.setDestBranchId(rs.getString("GH_DEST_BRNC_ID"));				
				gf.setDestClientId(rs.getString("GH_DEST_CLNT_ID"));
				gf.setDestClientName(rs.getString("GH_DEST_CLNT_NAME"));
				gf.setDeliveryDate(rs.getString("GH_DLVY_DATE"));
				gf.setDestSite(rs.getString("GH_DEST_SITE_ID"));
				gf.setOrginSite(rs.getString("GH_ORG_SITE_ID"));
				
				//System.out.println("Delivary Date~~~~~~"+rs.getString("GH_DLVY_DATE"));
			
			} 
			
			// get the Parameter Values Which are used in Functions and Procedures...
			pstrStatus=gf.getStatus();
			pstrOrginBranchId=gf.getOrginBranchId();
			pstrDestBranchId=gf.getDestBranchId();
			pstrOriginClientId=gf.getOrginClientId();
			pstrDestClientId=gf.getDestClientId();
		    pstrDestClientName=gf.getDestClientName();
			
			//System.out.println("Origin Branch Id..."+pstrOrginBranchId);//commented and below added by B.Emerson on 30/04/2004
			//AAP//AccessLog.Log("Origin Branch Id..."+pstrOrginBranchId);
			//System.out.println("Dest Branch Id..."+pstrDestBranchId);//commented and below added by B.Emerson on 30/04/2004
			//AAP//AccessLog.Log("Dest Branch Id..."+pstrDestBranchId);
			
			if(rs!=null)
				rs.close();
			if(pst!=null)
				pst.close();
			
			
					
			/*
			 *use pack_web.Fun_ftch_guia_stus_desc   function – Passed parameter is Gh_guia_stus
			*/
			
			//Commented by rama
			cst=con.prepareCall("begin ? := pack_web.fun_ftch_guia_stus_desc(?); end;");
			cst.registerOutParameter(1,Types.VARCHAR);
			//System.out.println("GUIA STATUS "+pstrStatus);//commented and below added by B.Emerson on 30/04/2004
			//AAP//AccessLog.Log("GUIA STATUS "+pstrStatus);
			cst.setString(2,pstrStatus);
			cst.execute();
			String strStatusDesc=cst.getString(1);
			//System.out.println("GUIA STATUS DESCRIPTION "+strStatusDesc);//commented and below added by B.Emerson on 30/04/2004
			//AAP//AccessLog.Log("GUIA STATUS DESCRIPTION "+strStatusDesc);
			gf.setStatusDesc(strStatusDesc);
			if(cst!=null)
				cst.close();
			
			
			//Changed By rama
			/*String statusQuery =	"	select PM_VLUE1_DESC from sys_parm_mstr "+
									"	where PM_MDUL_ID = 'BOK' AND PM_PARM_TYPE = 'GUIA_STATUS' "+
									"	AND PM_PARM_CODE1 = ? ";
			PreparedStatement statusPst = con.prepareStatement(statusQuery);
			System.out.println("GUIA STATUS "+pstrStatus);
			statusPst.setString(1,pstrStatus);
			ResultSet statusRs = statusPst.executeQuery();
			String strStatusDesc=null;
			if(statusRs.next())
				strStatusDesc=statusRs.getString(1);
			gf.setStatusDesc(strStatusDesc);
			if(statusRs!=null)
				statusRs.close();
			if(statusPst!=null)
				statusPst.close();*/
			
			//AAP//System.out.println(" Status Description is ...."+strStatusDesc);
			
			/*
			 *use pack_web.Fun_ftch_cod_vlue   function – Passed parameter is pstrGuiaNo
			*/
			
			//Changed by rama
			cst=con.prepareCall("{call ? := pack_web.Fun_ftch_cod_vlue(?)}");			
			cst.registerOutParameter(1,Types.NUMERIC);
			cst.setString(2,pstrGuiaNo);
			cst.execute();
			
			String strCodeValue=new java.text.DecimalFormat("0.00").format(cst.getDouble(1));
			//System.out.println(" CodeValue is ...."+strCodeValue);				
			gf.setCodeValue(strCodeValue);
			if(cst!=null)
				cst.close();
			
			/* pack_web.pro_ftch_curr_addr procedure
			 * Passed parameters are address type (‘ORIGIN' and gh_guia_no)	 	
			*/   	
			
			cst= con.prepareCall("begin PACK_WEB.PRO_FTCH_CURR_GUIA_ADDR(?,?,?,?,?,?,?);end;");				
			//set the values to IN Parameters...
			cst.setString(1,"ORIGIN");
			cst.setString(2,pstrGuiaNo);
			
			// Register the Out Parameters.....
			
			cst.registerOutParameter(3,Types.VARCHAR);
			cst.registerOutParameter(4,Types.VARCHAR);	 
			cst.registerOutParameter(5,Types.VARCHAR);
			cst.registerOutParameter(6,Types.VARCHAR);
			cst.registerOutParameter(7,Types.VARCHAR);
			
			//Execute the Procedure..	  
			cst.execute();
			
			// Get The OUT Parameters From the Procedure	  
			
			String strStreet= cst.getString(3);				
			String strDoornumber= cst.getString(4);
			String strColonia= cst.getString(5);
			String strCity= cst.getString(6);
			String strPhone= cst.getString(7); 
			
			// set the Orgin Values...	
			gf.setOrginCity(strCity);
			gf.setOrginClientAddress(strStreet);
			gf.setOrginColony(strColonia);
			gf.setOrginDoorNumber(strDoornumber);
			gf.setOrginPhoneNumber(strPhone);
			
			if(cst!=null)
				cst.close();
			
			/* pack_web.pro_ftch_curr_addr procedure
			* Passed parameters are address type (‘DESTINATION' and gh_guia_no)	 	
			*/   	
			
			cst= con.prepareCall("begin PACK_WEB.PRO_FTCH_CURR_GUIA_ADDR(?,?,?,?,?,?,?);end;");
			//set the values to IN Parameters...
			cst.setString(1,"DESTINATION");
			cst.setString(2,pstrGuiaNo);
			
			// Register the Out Parameters.....
			
			cst.registerOutParameter(3,Types.VARCHAR);
			cst.registerOutParameter(4,Types.VARCHAR);	 
			cst.registerOutParameter(5,Types.VARCHAR);
			cst.registerOutParameter(6,Types.VARCHAR);
			cst.registerOutParameter(7,Types.VARCHAR);
			
			//Execute the Procedure..	  
			cst.execute();
			
			// Get The OUT Parameters From the Procedure	  
			
			String strDestStreet= cst.getString(3);				
			String strDestDoornumber=cst.getString(4);
			String strDestColonia=cst.getString(5);
			String strDestCity=cst.getString(6);
			String strDestPhone=cst.getString(7);  
			
			//set the Destination values...
			gf.setDestCity(strDestCity);
			gf.setDestClientAddress(strDestStreet);
			gf.setDestDoorNumber(strDestDoornumber);
			gf.setDestColony(strDestColonia);
			gf.setDestPhoneNumber(strDestPhone);
			if(cst!=null)
				cst.close();
			/*
			 *get The Branch name (use pack_web.Fun_ftch_brnc_name function – Passed parameter is GH_ORGN_BRNC_ID)	
			*/
			cst=con.prepareCall("begin ? := pack_web.Fun_ftch_brnc_name(?); end;");
			cst.registerOutParameter(1,Types.VARCHAR ); 	 	   
			cst.setString(2,pstrOrginBranchId);
			cst.execute();
			String strOriginBranchName=cst.getString(1);
			//System.out.println("Branch Name!!!!!!!!!!!!!!!"+strOriginBranchName);
			gf.setOrginBranchName(strOriginBranchName);	
			if(cst!=null)
				cst.close();
			/*
			 *get The Branch name (use pack_web.Fun_ftch_brnc_name function – Passed parameter is GH_ORGN_BRNC_ID)	
			*/
			cst=con.prepareCall("begin ? := pack_web.Fun_ftch_brnc_name(?); end;");
			cst.registerOutParameter(1,Types.VARCHAR ); 	 	   
			cst.setString(2,pstrDestBranchId);
			cst.execute();
			String strDestBranchName=cst.getString(1);
			//System.out.println("Dest branch Name!!!!!!!!!!!!!!!"+strDestBranchName);
			gf.setDestBranchName(strDestBranchName);
			
//			System.out.println("Dest branch Name!!!!!!!!!!!!!!!"+strOrgnSiteName);
			
			if(cst!=null)
				cst.close();
			cst=con.prepareCall("begin ? := pack_web.Fun_ftch_site_name(?); end;");
			cst.registerOutParameter(1,Types.VARCHAR ); 	 	   
			cst.setString(2,gf.getOrginSite());
			cst.execute();
			gf.setOrginSite(cst.getString(1));
			//System.out.println("Dest branch Name!!!!!!!!!!!!!!!"+strDestSiteName);
			
			if(cst!=null)
				cst.close();
			cst=con.prepareCall("begin ? := pack_web.Fun_ftch_site_name(?); end;");
			cst.registerOutParameter(1,Types.VARCHAR ); 	 	   
			cst.setString(2,gf.getDestSite());
			cst.execute();
			gf.setDestSite(cst.getString(1));
			//System.out.println("Dest branch Name!!!!!!!!!!!!!!!"+strDestBranchName);
			if(cst!=null)
				cst.close();
			
			/*
			 *get RFC (use pack_web. fun_ftch_rfc function – Passed parameter is gh_orgn_clnt_id)
			*/
			cst=con.prepareCall("begin ? :=pack_web. fun_ftch_rfc(?); end;");
			cst.registerOutParameter(1,Types.VARCHAR ); 	 	   
			cst.setString(2,pstrOriginClientId);
			cst.execute();
			String strRfc=cst.getString(1);
			//System.out.println("RFC~~~~~~~~"+strRfc);
			gf.setOrginRfc(strRfc);	
			if(cst!=null)
				cst.close();
			
			/*
			 * Get the Delevery Type 
			 * Passing Parameters Are GuiaNumber,GuiaType,Guia Activ Flag..
			*/
			
			cst=con.prepareCall("begin ? :=pack_web.fun_ftch_dely_type(?,?,?); end;");
			cst.registerOutParameter(1,Types.VARCHAR);
			cst.setString(2,pstrGuiaNo);
			cst.setString(3,pstrGuiaType);
			cst.setString(4,pstrGuiaActiveFalg);
			cst.execute();
			String strDeliveryType=cst.getString(1);
			//System.out.println("Dely Type..."+strDeliveryType);
			gf.setDeleiveryType(strDeliveryType);			
			
			if(cst!=null)
				cst.close();
			
			/*
			 *get the Deleverer- use pack_web.pack_web.fun_ftch_dlvy_conc Function and Passing Parameter is Guia Number..
			*/
			
			cst=con.prepareCall("begin ? :=pack_web.fun_ftch_dlvy_conc(?); end;");
			cst.registerOutParameter(1,Types.VARCHAR ); 	 	   
			cst.setString(2,pstrGuiaNo);
			cst.execute();
			pstrDeliverer=cst.getString(1);	
			//System.out.println("Deliverer..."+pstrDeliverer);
			gf.setDeliverer(pstrDeliverer);
			if(cst!=null)
				cst.close();
			
			/*
			 *get the Deleverer Name- use pack_web.fun_ftch_dlvy_conc_name Function 
			 * Passing Parameter is deliverer,destclientid,destclientname
			*/
		
			
			cst=con.prepareCall("begin ? :=pack_web.fun_ftch_dlvy_conc_name(?,?,?); end;");
			cst.registerOutParameter(1,Types.VARCHAR ); 	 	 
			cst.setString(2,pstrDeliverer);
			cst.setString(3,pstrDestClientId);
			cst.setString(4,pstrDestClientName);
			cst.execute();
			String strDelivererName=cst.getString(1);
			//System.out.println("strDelivererName~~~~~~~~"+strDelivererName);
			gf.setDelivererName(strDelivererName);
			if(cst!=null)
				cst.close();
			
			/* Get Fiacal Address Use pack_web.pro_ftch_curr_addr procedure
			 * Passed parameters are address type (‘FISCAL' and gh_guia_no)	 	
			*/   	
			
			cst= con.prepareCall("begin PACK_WEB.PRO_FTCH_CURR_GUIA_ADDR(?,?,?,?,?,?,?);end;");				
			//set the values to IN Parameters...
			cst.setString(1,"FISCAL");
			cst.setString(2,pstrGuiaNo);
			
			// Register the Out Parameters.....
			
			cst.registerOutParameter(3,Types.VARCHAR);
			cst.registerOutParameter(4,Types.VARCHAR);	 
			cst.registerOutParameter(5,Types.VARCHAR);
			cst.registerOutParameter(6,Types.VARCHAR);
			cst.registerOutParameter(7,Types.VARCHAR);
			
			//Execute the Procedure..	  
			cst.execute();
			
			// Get The OUT Parameters From the Procedure	  
			
			String strFisStreet= cst.getString(3);				
			String strFisDoornumber= cst.getString(4);
			String strFisColonia= cst.getString(5);
			String strFisCity= cst.getString(6);
			String strFisPhone= cst.getString(7); 
			
			// set the Orgin Values...	
			gf.setFiscalAddress(strFisStreet);
			gf.setFiscalDoorNumber(strFisDoornumber);
			gf.setFiscalColony(strFisColonia);
			gf.setFiscalStreet(strFisCity);
			gf.setFiscalPhoneNumber(strFisPhone);	
			
			if(cst!=null)
				cst.close();
			
			
		}catch(Exception e){	
			//System.out.println("Exception From  JavWebGuiaDetailsDataBean.....");//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log("Exception From  JavWebGuiaDetailsDataBean.....");			
			//System.out.println(e.getMessage());//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e.getMessage());
			//System.out.println(e.toString());//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e.toString());
			//e.printStackTrace();//commented and below added by B.Emerson on 30/04/2004
			AccessLog.Log(e);
			
		}
		
		 finally{
			 try{						
				 if(rs!=null)
					 rs.close();
				 if(pst!=null)
					 pst.close();
				 if(pst!=null)
					 pst.close();				 
				 if(cst!=null)
					 cst.close();				 
			 }
			 catch(Exception e){
				 e.printStackTrace();
			 }
		 }		
	}
	
}	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	

	
  
  
      
   