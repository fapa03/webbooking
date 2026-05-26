
/**
* File Name    : ManifestGeneration.java
* Description  : This is the Action Class For LoginForm
*				  to handle and Control the Inputs of that FormBean. 
* Date Written :  -2003
* @author 	   :  D.SivaKumar
* Updated On      Version    Updated By				  Summary
* ~~~~~~~~~~~~~   ~~~~~~~    ~~~~~~~~~~~~~~~~~		  ~~~~~~~~~~~~~~~
* 31-May-2001				 Kumaran Systems		  For grouping guia

*/

import java.sql.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import bean.Global;
import bean.JavManifestGenerationForm;
import bean.JavManifestRecord;

import java.sql.ResultSet;

import logger.AccessLog;

public class JavManifestGenerationBean1 {
	public JavManifestGenerationBean1(){		
		//AccessLog.Log("Inside JavManifestGenerationBean");			
	}
	
	String seqManifestNumber="";
	
	// Function to get the Credit Limit Of the client....
	
	public double getCreditLimit(Connection con,JavManifestGenerationForm mgf,String webClientId){
		String query="";
		double creditLimit=0.0;
		PreparedStatement pst=null;
		ResultSet rs=null;	
		
		try{
			//AAP//AccessLog.Log("Credit Limit ~~~~~~~~~~");
			query="SELECT pack_web.FUN_CLNT_CRDT_AMT(?) AS CREDITlIMIT FROM DUAL";
			pst=con.prepareStatement(query);				
			pst.setString(1,webClientId);
			rs=pst.executeQuery();
			if(rs!=null && rs.next()){
				
				creditLimit=rs.getDouble("CREDITlIMIT");
				mgf.setCreditLimit(creditLimit);
				//AAP//AccessLog.Log("Credit Limit ~~~~~~~~~~:"+creditLimit);
				//AAP//AccessLog.Log("Credit Limit ~~~~~~~~~~:"+mgf.getCreditLimit());
			}		
			
		}catch(Exception e){
			try{
				con.rollback();
			}catch(Exception ex){
				AccessLog.Log(ex);
			}
			AccessLog.Log("Exception in Function Credit Limit..");
			AccessLog.Log(e.getMessage());
			AccessLog.Log(e);
		}
		
		return creditLimit;
	}
//	Added By Amal on 21-Aug-2006 (Purpose: -->  To call a oracle function to find out branch types
	//returns 0 for internal ,1 for border ,2 for all
	public int  getType(Connection con,String clientid,String branchid)

			{
				CallableStatement cst=null;
		
				try {
					cst = con.prepareCall( "{  ? = call pack_web.fun_chk_brdr(?,?)}");
					cst.registerOutParameter(1,Types.INTEGER);
								cst.setString(2,clientid);
								cst.setString(3,branchid);
								cst.executeUpdate();
								int val=cst.getInt(1);
								
								return val;
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
	
			return 0;
			}
	
	public ArrayList getManifestDetails(Connection con,JavManifestGenerationForm mgf,String webClientId,String orginClientId,HttpServletRequest request) throws Exception{
		
		String strSqlQuery="";		
		String strGuiaNo="";// added by sundar on 30/07/2003 to handle Manifestgeneration using checking all
		PreparedStatement pst=null;	
		CallableStatement cst=null;		
		ResultSet rs=null;	
		JavManifestRecord manifestRecObj=null;				
		ArrayList manifestArrayList=new ArrayList();
		
		int totRec=0;
		//int totPages=0;
		//int pageIndex=0;
		int currentRec=0;
		int endRec=0;
		double maxPages=0;
		int count=1;// added by sundar on 30/07/2003 to handle Manifestgeneration using checking all
		//String operation=null;
		
		try{	
			
			//Query is changed by Rajkumar for Branch Location Type
			/*strSqlQuery="SELECT A.GH_FORM_NO, " +
						"A.GH_GUIA_NO, " +
						"A.GH_DEST_BRNC_ID, " +
						"A.GH_DEST_CLNT_NAME, " +
						"TO_CHAR (A.GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT, " +
						"A.GH_NUMB_PACK, " +
						"B.BM_BRNC_LOC_TYPE " +
						"FROM BOK_GUIA_HEAD A, SYS_BRNC_MSTR B " +
						"WHERE B.BM_BRNC_ID = A.GH_DEST_BRNC_ID AND A.GH_ORGN_CLNT_ID=? AND A.GH_GUIA_TYPE=?  AND A.GH_ACTV_FLAG=? AND " +
						"A.GH_ORGN_BRNC_ID=? AND A.GH_GUIA_REFR_NO IS NULL AND A.GH_RAD_FLAG =?";

			System.err.println( "TSI: " + ResultSet.TYPE_SCROLL_INSENSITIVE);
			pst = con.prepareStatement(strSqlQuery,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			pst.setString(1,webClientId);
			pst.setString(2,"H");
			pst.setString(3,"A");
			pst.setString(4,orginClientId);
			pst.setString(5,"1");*/
			strSqlQuery="SELECT A.GH_FORM_NO, " +
									"GH_GUIA_NO, " +
									"GH_DEST_BRNC_ID, " +
									"GH_DEST_CLNT_NAME, " +
									"TO_CHAR (GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT, " +
									"GH_NUMB_PACK, " +
									"FROM BOK_GUIA_HEAD "+
									"WHERE GH_ORGN_CLNT_ID=? AND A.GH_GUIA_TYPE=?  AND A.GH_ACTV_FLAG=? AND " +
									"GH_ORGN_BRNC_ID=? AND A.GH_GUIA_REFR_NO IS NULL AND A.GH_RAD_FLAG =? AND " +
									"fun_brdr_brnc(GH_DEST_BRNC_ID) = ? " ;
									
			strSqlQuery="SELECT  GH_FORM_NO,"+
												"GH_GUIA_NO,"+
												"GH_DEST_BRNC_ID,"+
												"GH_DEST_CLNT_NAME,"+
												"to_char(GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT ,"+
												"GH_NUMB_PACK "+
												"FROM BOK_GUIA_HEAD "+
												"WHERE GH_ORGN_CLNT_ID=? AND GH_GUIA_TYPE=?  AND GH_ACTV_FLAG=? AND "+
												"  gh_orgn_brnc_id=? and GH_GUIA_REFR_NO is null AND GH_RAD_FLAG =?";


									//AAP//System.err.println( "TSI: " + ResultSet.TYPE_SCROLL_INSENSITIVE);
									
									pst = con.prepareStatement(strSqlQuery,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
									pst.setString(1,webClientId);
									pst.setString(2,"H");
									pst.setString(3,"A");
									pst.setString(4,orginClientId);
									pst.setString(5,"1");
			//AccessLog.Log("Query......"+strSqlQuery);
			
			rs =pst.executeQuery();
			//AAP//System.err.println( rs);
			//AAP//System.err.println( rs.toString());
			
			boolean rsFlag=rs.next();
			//AAP//AccessLog.Log("RS FLAG"+rsFlag);
			
			// count the total number of Records.
			if(rsFlag){
				rs.last();
				totRec=rs.getRow();
				count=mgf.getTotalRecord();// added by sundar on 30/07/2003 to handle Manifestgeneration using checking all
				mgf.setTotalRecord(totRec);
				rs.beforeFirst();
				mgf.setNoDataFound("N");
				request.setAttribute("sDataFlag","No");
				
			}			
			else if(rsFlag==false){
				mgf.setNoDataFound("Y");
				request.setAttribute("sDataFlag","Yes");
			}
			
			if((totRec%10)==0)
				maxPages=totRec/10;
			else
				maxPages=(totRec/10)+1;
			
			mgf.setMaxPageIndex(maxPages);
			
			currentRec=mgf.getCurRow();
			endRec=mgf.getEndRow();
			
			if(endRec== 0){
				endRec = 10;
				mgf.setEndRow(endRec);					
			}
			
			if(currentRec == 0){
				currentRec = 1;
				mgf.setCurRow(currentRec);			
			}
			
		 // added by sundar on 30/07/2003 to handle Manifestgeneration using checking all 
			rs.beforeFirst();
			
			if(count==0)
			{
				while(rs.next())
				{
						manifestRecObj=new JavManifestRecord(); 
						manifestRecObj.setGuiaNumber((rs.getString("GH_GUIA_NO")==null?"":(rs.getString("GH_GUIA_NO"))));
						strGuiaNo+=(rs.getString("GH_GUIA_NO")==null?"":(rs.getString("GH_GUIA_NO")))+" ";
					
						
				}
				mgf.setClickedItems(strGuiaNo);
			}
			
			rs.beforeFirst();
			while(rs.next()){
				if(currentRec<=endRec){
					
					rs.absolute(currentRec);
					manifestRecObj=new JavManifestRecord(); 
					
					manifestRecObj.setFormNumber((rs.getString("GH_FORM_NO")==null?"":(rs.getString("GH_FORM_NO"))));
					manifestRecObj.setGuiaNumber((rs.getString("GH_GUIA_NO")==null?"":(rs.getString("GH_GUIA_NO"))));
					manifestRecObj.setGuiaAmount((rs.getString("GH_GUIA_AMNT")==null?"":(rs.getString("GH_GUIA_AMNT"))));
					manifestRecObj.setNumPack((rs.getString("GH_NUMB_PACK")==null?"":(rs.getString("GH_NUMB_PACK"))));
					manifestRecObj.setDestClientName((rs.getString("GH_DEST_CLNT_NAME")==null?"":(rs.getString("GH_DEST_CLNT_NAME"))));
					
					
					// Get the Branch Name Using GH_DEST_BRNC_ID...				
					String StrDestBrnachId=rs.getString("GH_DEST_BRNC_ID");
					
					/*
					*get The Branch name (use pack_web.Fun_ftch_brnc_name function – Passed parameter is GH_DEST_BRNC_ID)	
					*/
					cst=con.prepareCall("begin ? := pack_web.Fun_ftch_brnc_name(?); end;");
					cst.registerOutParameter(1,Types.VARCHAR );
					cst.setString(2,StrDestBrnachId);
					cst.execute();
					String strDestBranchName=(cst.getString(1)==null?"":cst.getString(1));
					
					//AccessLog.Log("Dest Branch Name......."+strDestBranchName);
					
					manifestRecObj.setDestBranch(strDestBranchName);
					//Added by Rajkumar for Branch Location Type
					manifestRecObj.setBranchLocType((rs.getString("BM_BRNC_LOC_TYPE")==null?"":(rs.getString("BM_BRNC_LOC_TYPE"))));

					manifestArrayList.add(manifestRecObj);
					manifestRecObj=null;
					currentRec++;
				}
			}
			
		}catch(Exception e){			
			AccessLog.Log(e.getMessage());
			AccessLog.Log(e.toString());
			AccessLog.Log(e);
			mgf.setManifestNumber("");
			
		} finally{
			try{						
				if(rs!=null)
					rs.close();
				if(pst!=null)
					pst.close();							 
				
			}
			catch(Exception e){
				AccessLog.Log(e);
			}
		}		
		
		return manifestArrayList;
	}
	
	public int insertToManifestDetail(javax.servlet.http.HttpSession session,
									  Connection con,
									  JavManifestGenerationForm mgf,
									  String webClientId,
									  String strGuiaNumbers,
									  String BranchId) throws Exception{
		String query="";
		String query1="";
		//String query2="";
		String gnoset="";
		String cttNumQuery="";
		String cttNumber="";
		//String seqManifestNumber="";
		String strCollectionTime="";
		StringTokenizer st=null;
		PreparedStatement pst=null;
		PreparedStatement pst1=null;
		PreparedStatement pst2=null;
		//CallableStatement cst=null;
		ResultSet rs=null;
		ResultSet rs1=null;
		
		//String guiaLocation=null;
		//String guiaDest=null;
		String manifestIssueDate=null;		
		double sumAmnt=0.0;
		int sumPack=0;
		int numOrdered=0;
		int insertCount=0;
		try{
			
			
			
			// Get the Manifest Number  From the Sequence..
			
			String query3="SELECT to_char('MF'||lpad(WEB_MNFT_NO.nextval,8,'0'))  FROM Dual";
			pst=con.prepareStatement(query3);		
			rs=pst.executeQuery();
			if(rs!=null && rs.next()){
				seqManifestNumber=rs.getString(1);
				//AccessLog.Log("Sequence Number is...."+seqManifestNumber);
			}
			
			if(rs!=null) rs.close();
			if(pst!=null) pst.close();
			
			//GET THE CTTNO FROM THE SEQUENCE CTTNO..
			cttNumQuery="SELECT CTTNO.NEXTVAL FROM DUAL";
			pst=con.prepareStatement(cttNumQuery);
			rs=pst.executeQuery();
			if(rs.next()){
				cttNumber=""+rs.getInt(1);
				int concttno=Integer.parseInt(cttNumber);
				//AccessLog.Log("CttNumber..."+cttNumber);
				mgf.setCttNumber(concttno);
				
			}
			if(rs!=null) rs.close();
			if(pst!=null) pst.close();
			
			// Get the Sum Values....
			query="select sum(gh_guia_amnt),sum(gh_numb_pack),count(*) from BOK_GUIA_HEAD";
			query=query+" WHERE  gh_guia_no in(";
			
			if( strGuiaNumbers!=null || strGuiaNumbers.equals("")  ){
				st= new StringTokenizer(strGuiaNumbers);
				while(st.hasMoreElements()){
					gnoset=gnoset+"'"+st.nextToken()+"',";
				}
			}
			
			String newSet=gnoset.substring(0,gnoset.length()-1);		
			query=query+newSet+" )";
			query=query+" AND GH_GUIA_TYPE='H' AND GH_ACTV_FLAG='A' ";
			
			pst=con.prepareStatement(query);
			
			rs=pst.executeQuery();
			if(rs!=null && rs.next()){
				
				sumAmnt=rs.getDouble(1);
				sumPack=rs.getInt(2);
				numOrdered=rs.getInt(3);				
				mgf.setManifestAmount(sumAmnt);
				mgf.setSumPack(sumPack);
				mgf.setNumOrdered(numOrdered);
				
				//AccessLog.Log("sumAmount..."+sumAmnt);
				//AccessLog.Log("sumPack..."+sumPack);
				//AccessLog.Log("numOrdered..."+numOrdered); 
				
				// Get  the Address Details and Insert it into the DataBase....
				
				
				strCollectionTime=mgf.getCollectionTime();
				mgf.setCollectionTime(strCollectionTime);
				
				//AAP//AccessLog.Log("STRING COLLECTION TIME "+strCollectionTime);
				
				//Added by rama
				Global global = (Global)session.getAttribute("sGlobal");
				
				/*long strCollTimeLong = new java.util.Date(strCollectionTime).getTime()-global.timediff;
				
				AccessLog.Log("------- JSP TIME LONG "+new java.util.Date(strCollectionTime).getTime());
				AccessLog.Log("------- ORIGINAL TIME LONG "+strCollTimeLong);
				
				java.sql.Timestamp collectionDate = new java.sql.Timestamp(strCollTimeLong);
				
				AccessLog.Log("------- COLLECTION DATE "+collectionDate);*/
				
				//java.sql.Date collectionDate =  getDate(strCollectionTime,con);//Added by rama
				
				
				String strAddrCode=(mgf.getAddressCode()==null?"":mgf.getAddressCode());
				//AAP//AccessLog.Log("Address Code..."+strAddrCode);
				String strDoorNumber=(mgf.getDoorNumber()==null?"":mgf.getDoorNumber());
				String strStreetName=(mgf.getStreetName()==null?"":mgf.getStreetName());
				String strPhoneNumber=(mgf.getPhoneNumber()==null?"":mgf.getPhoneNumber());
				String strSuitNumber=(mgf.getSuitNumber()==null?"":mgf.getSuitNumber());
				String strFloorNumber=(mgf.getFloorNumber()==null?"":mgf.getFloorNumber());
				String strAddr1=(mgf.getAddressLine1()==null?"":mgf.getAddressLine1());
				String strAddr2=(mgf.getAddressLine2()==null?"":mgf.getAddressLine2());
				String strAddr3=(mgf.getAddressLine3()==null?"":mgf.getAddressLine3());
				String strAddr4=(mgf.getAddressLine4()==null?"":mgf.getAddressLine4());
				String strAddr5=(mgf.getAddressLine5()==null?"":mgf.getAddressLine5());
				String strAddr6=(mgf.getAddressLine6()==null?"":mgf.getAddressLine6());
				String strAddr7=(mgf.getAddressLine7()==null?"":mgf.getAddressLine7());
				String strZipCode=(mgf.getZipCode()==null?"":mgf.getZipCode());

				//Modified By Sam.D.Jabeen on[01-Jun-2006] for adding manifesto type
				String strManifiestoType = (mgf.getManifiestoType() == null ? "" : mgf.getManifiestoType());

				
				query1="INSERT INTO WEB_MNFT_DETL("+
					   "MD_MNFT_NO,"+
					   "MD_CLNT_ID,"+
					   "MD_BRNC_ID,"+
					   "MD_ISSE_DATE,"+
					   "MD_NUMB_ORDR,"+
					   "MD_NUMB_PACK,"+
					   "MD_MNFT_AMNT,"+
					   "MD_MNFT_STUS,"+
					   "MD_ACTV_FLAG,"+
					   "MD_TRIP_STUS,"+
					   "MD_PLAN_COLL_DATE,"+
					   "CRTD_ON,"+
					   "CRTD_BY,"+
					   "MDFD_ON,"+
					   "MDFD_BY,"+
					   "MD_ADDR_CODE,"+
					   "MD_DRNR,"+
					   "MD_STRT_NAME,"+
					   "MD_PHNO_1,"+
					   "MD_SUIT_NO,"+
					   "MD_FLOR_NO,"+
					   "MD_ADDR_LIN1,"+ 
					   "MD_ADDR_LIN2,"+
					   "MD_ADDR_LIN3,"+
					   "MD_ADDR_LIN4,"+
					   "MD_ADDR_LIN5,"+
					   "MD_ADDR_LIN6,"+
					   "MD_ADDR_LIN7,"+
					   "MD_ZIP_CODE,MD_CTT_REFR_NO,"+
						"MD_FLAG_1) ";				
				query1=query1+" VALUES(?,?,?,sysdate,?,?,?,?,?,?,to_date(?,'dd/mm/yyyy hh24:mi')-("+global.timediff+"/86400000),sysdate,?,sysdate,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				
				
				pst1=con.prepareStatement(query1);
				
				pst1.setString(1,seqManifestNumber);
				pst1.setString(2,webClientId);
				pst1.setString(3,BranchId);
				pst1.setInt(4,numOrdered);
				pst1.setInt(5,sumPack);
				pst1.setDouble(6,sumAmnt);
				pst1.setString(7,"PRP");
				pst1.setString(8,"A");
				pst1.setString(9,"C");
				pst1.setString(10,strCollectionTime); //Rama
				//pst1.setTimestamp(10,collectionDate);//Added by rama
				pst1.setString(11,webClientId);
				pst1.setString(12,webClientId);
				pst1.setString(13,strAddrCode);
				pst1.setString(14,strDoorNumber);
				pst1.setString(15,strStreetName);
				pst1.setString(16,strPhoneNumber);
				pst1.setString(17,strSuitNumber);
				pst1.setString(18,strFloorNumber);
				pst1.setString(19,strAddr1);
				pst1.setString(20,strAddr2);
				pst1.setString(21,strAddr3);
				pst1.setString(22,strAddr4);
				pst1.setString(23,strAddr5);
				pst1.setString(24,strAddr6);
				pst1.setString(25,strAddr7);
				pst1.setString(26,strZipCode);
				pst1.setString(27,cttNumber);
				pst1.setString(28,strManifiestoType);
				
				insertCount=pst1.executeUpdate();
				
				if(insertCount>0) {
					con.commit();
					String tempquery=" select to_char(sysdate,'dd/mm/yyyy hh24:mi') from dual ";
					pst2=con.prepareStatement(tempquery);
					rs1=pst.executeQuery();
					if(rs.next()){
						manifestIssueDate=(rs.getString(1)==null?"":rs.getString(1));
						mgf.setManifestIssueDate(manifestIssueDate);
					}
					// get the  Manifest number From Manifest Details and Set it into the Form Field..
					
					if(rs1!=null) rs1.close();
					if(pst2!=null) pst2.close();
					mgf.setManifestNumber(seqManifestNumber);  
				}	
				
				if(rs!=null)
					rs.close();
				if(pst!=null)
					pst.close();
				if(pst1!=null)
					pst.close();
			}				
			
		}catch(Exception e){
			con.rollback();
			AccessLog.Log(e.getMessage());
			AccessLog.Log(e.toString());
			AccessLog.Log(e);
			mgf.setManifestNumber("");
		}finally{
			
			try{
				if(rs!=null)
					rs.close();
				if(pst!=null)
					pst.close();
				if(pst1!=null)
					pst.close();
			}catch(Exception e){
				AccessLog.Log(e);
			}
		}
		
		return insertCount;
		
	}
	
	
	public int updateBokGuiaHead(Connection con,
								 JavManifestGenerationForm mgf,
								 String webClientId,
								 String strGuiaNumbers,
								 String BranchId) throws Exception{
		
		String query="";
		String gnoset="";
		StringTokenizer st=null;
		PreparedStatement pst=null;	   
		ResultSet rs=null;	
		//int insertCount=0;
		int updateCount=0;
		//ArrayList guiaNumbers=new ArrayList();
		//ArrayList formNumber=new ArrayList();
		try{
			
			// Update the BokGuia_Head...
			query="UPDATE BOK_GUIA_HEAD SET GH_GUIA_REFR_NO=?,GH_GUIA_STUS=?,MDFD_ON =SYSDATE,MDFD_BY =? ";
			query=query+" WHERE GH_ORGN_CLNT_ID=? AND GH_GUIA_TYPE=?  AND GH_ACTV_FLAG=? AND  gh_orgn_brnc_id=? and GH_GUIA_REFR_NO is null AND";
			query=query+" GH_GUIA_NO in(";
			if( strGuiaNumbers!=null || strGuiaNumbers.equals("")  ){
				
				st= new StringTokenizer(strGuiaNumbers);
				while(st.hasMoreElements()){					
					gnoset=gnoset+"'"+st.nextToken()+"',";
					
				}
			}
			
			String newSet=gnoset.substring(0,gnoset.length()-1);		
			query=query+newSet+" )";
			
			//String seqManifestNumber=mgf.getManifestNumber();
			
			pst=con.prepareStatement(query);
			pst.setString(1,seqManifestNumber);
			pst.setString(2,"IIM");
			pst.setString(3,webClientId);
			pst.setString(4,webClientId);
			pst.setString(5,"H");
			pst.setString(6,"A");
			pst.setString(7,BranchId);
			
			updateCount=pst.executeUpdate();
			if(updateCount >0) con.commit();
			//AccessLog.Log("Records are Updated Successfully In Bok_Guia_Head....");
			if(pst!=null) pst.close();		
			
		}catch(Exception e){
			con.rollback();
			
			AccessLog.Log(e.toString());
			AccessLog.Log(e.getMessage());
			mgf.setManifestNumber("");
			
		}finally{
			try{						
				if(rs!=null) rs.close();
				if(pst!=null) pst.close();
			}
			catch(Exception e){
				AccessLog.Log(e);
			}
		}	
		
		return updateCount;
	}
	
	public int insertToComCTT(Connection con,
							  JavManifestGenerationForm mgf,
							  String webClientId,
							  String BranchId) throws Exception{
		
		PreparedStatement pst=null;
		ResultSet rs=null;
		//String query="";
		String query1="";
		int insCount=0;
		
		String cttNumber=null;
		String sumAmnt=null;
		String sumPack=null;
		String numOrdered=null;		
		String manifestNumber=null;		
		//String guiaLocation=null;
		//String guiaDest=null;
		String manifestIssueDate=null;
		String collectionDate=null;
		
		try{
			// get the Manifest Number From the Form Field
			manifestNumber=mgf.getManifestNumber();
			//get the Required FormFields In Order to Insert into cmm_ctt Table...
			
			manifestIssueDate=mgf.getManifestIssueDate();			
			sumAmnt=""+mgf.getManifestAmount();
			sumPack=""+mgf.getSumPack();
			numOrdered=""+mgf.getNumOrdered();
			manifestIssueDate=mgf.getManifestIssueDate();
			collectionDate=mgf.getCollectionTime();
			cttNumber=""+mgf.getCttNumber();

			//AAP//AccessLog.Log("Manifest Num..Seco_enty in ComCTT.."+manifestNumber);			
			//AAP//AccessLog.Log("Manifest IssueDate in ComCTT ...."+manifestIssueDate);
			//AAP//AccessLog.Log("CttNumber in Com Ctt...."+cttNumber);
			query1="INSERT INTO COM_CTT("+
				   "CT_TRNS_NO,"+				 
				   "CT_TRNS_TYPE,"+
				   "CT_TRNS_DATE,"+
				   "CT_PRIM_ENTY,"+
				   "CT_SECO_ENTY,"+
				   "CT_TEXT_02,"+
				   "CT_DATE_01,"+
				   "CT_DATE_02,"+
				   "CT_TEXT_05,"+
				   "CT_TEXT_06,"+
				   "CT_TEXT_07,"+
				   "CT_TEXT_13,"+
				   "CRTD_ON,"+
				   "CRTD_BY,"+
				   "MDFD_ON,"+
				   "MDFD_BY )";
			query1=query1+" VALUES(?,?,SYSDATE,?,?,?,to_date(?,'dd/mm/yyyy hh24:mi'),to_date(?,'dd/mm/yyyy hh24:mi'),?,?,?,?,SYSDATE,?,SYSDATE,?)";
			
			pst=con.prepareStatement(query1);
			
			pst.setString(1,cttNumber);
			pst.setString(2,"IIM");
			pst.setString(3,webClientId);
			pst.setString(4,seqManifestNumber);
			pst.setString(5,sumAmnt);
			//Added by rama
			java.sql.Date manIssueDate = getDate(manifestIssueDate,con);
			java.sql.Date colDate=getDate(collectionDate,con);
			//pst.setString(6,manifestIssueDate);
			//pst.setString(7,collectionDate);
			pst.setDate(6,manIssueDate);			
			pst.setDate(7,colDate);
			pst.setString(8,webClientId);
			pst.setString(9,numOrdered);
			pst.setString(10,sumPack);
			pst.setString(11,BranchId);
			pst.setString(12,webClientId);
			pst.setString(13,webClientId);			
			insCount=pst.executeUpdate();
			if(insCount >0) con.commit();
			//AAP//AccessLog.Log(" Records Inserted in ComCTT table SucessFully.."+insCount);
			
			
		}catch(Exception e){
			con.rollback();
			AccessLog.Log("Exception in    insertToComCTT function...");
			AccessLog.Log(e.toString());
			AccessLog.Log(e.getMessage());
			mgf.setManifestNumber("");
		}finally{
			
			if(rs!=null) rs.close();
			if(pst!=null) pst.close();
		}
		
		return insCount;
	}
	
	
	public int insertToBokGuiaStatus(Connection con,JavManifestGenerationForm mgf,String strGuiaNumbers,String webClientId) throws Exception{
		
		String query="";	
		String gnoset="";
		StringTokenizer st=null;
		PreparedStatement pst=null;	   			
		ResultSet rs=null;	
		//int insertCount=0;
		int guisinsCount=0;
		ArrayList guiaNumbers=new ArrayList();
		ArrayList guiaLocation=new ArrayList();
		ArrayList guiaDest=new ArrayList();
		//ArrayList formNumber=new ArrayList();
		int serialNumber=0;
		String cttNumber="";
		String strGuiaNumber=null;
		String strGuiaLocation=null;
		String strGuiaDest=null;
		try{
			query="select GH_GUIA_NO,GH_CURR_LOCA,GH_CURR_DEST FROM BOK_GUIA_HEAD";
			query=query+" WHERE  GH_GUIA_NO in(";
			
			
			if( strGuiaNumbers!=null || strGuiaNumbers.equals("")  ){
				
				st= new StringTokenizer(strGuiaNumbers);
				while(st.hasMoreElements()){					
					gnoset=gnoset+"'"+st.nextToken()+"',";
					
				}
			}
			
			String newSet=gnoset.substring(0,gnoset.length()-1);		
			query=query+newSet+" )";
			query=query+" AND GH_GUIA_TYPE='H' AND GH_ACTV_FLAG='A' ";
			
			//AccessLog.Log("shiperdetails query======="+query);
			
			pst=con.prepareStatement(query);
			
			rs=pst.executeQuery();
			
			while(rs!=null && rs.next()){
				guiaNumbers.add(rs.getString("GH_GUIA_NO"));
				guiaLocation.add((rs.getString("GH_CURR_LOCA")==null?"":rs.getString("GH_CURR_LOCA")));
				guiaDest.add((rs.getString("GH_CURR_DEST")==null?"":rs.getString("GH_CURR_DEST")));
			}
			
			if(rs!=null) rs.close();
			if(pst!=null) pst.close();			
			
			
			if(guiaNumbers!=null){
				int i=0;
				while(i<guiaNumbers.size()){
					
					String q="INSERT INTO BOK_GUIA_STUS(GS_GUIA_NO,"+
							 "GS_STUS_CODE,"+
							 "GS_SERL_NO,"+
							 "GS_COM_TRNS_REF_CODE,"+
							 "CRTD_ON, "+
							 "CRTD_BY, "+
							 "MDFD_ON,"+
							 "MDFD_BY,"+
							 "GS_CTT_TRNS_TYPE,"+
							 "GS_CURR_LOCA,"+
							 "GS_CURR_DEST"+
							 ")"+
							 "VALUES(?,?,?,?,SYSDATE,?,SYSDATE,?,?,?,?)";
					
					
					// get Max of Serial Number....from Bok Guia Status...
					String serialNumberQuery=" SELECT NVL(MAX(GS_SERL_NO),0)+1 FROM BOK_GUIA_STUS WHERE GS_GUIA_NO=?";
					strGuiaNumber=(String)guiaNumbers.get(i);
					pst=con.prepareStatement(serialNumberQuery);
					pst.setString(1,strGuiaNumber);
					rs=pst.executeQuery();
					
					if(rs!=null && rs.next())
						serialNumber=rs.getInt(1);	
					//AAP//AccessLog.Log("Serial Number ..."+serialNumber);  
					//AAP//AccessLog.Log("Serial Number DB..."+rs.getInt(1));  
					if(rs!=null) rs.close();
					if(pst!=null) pst.close();
					
					cttNumber=""+mgf.getCttNumber();
					//strGuiaNumber=(String)guiaNumbers.get(i);
					strGuiaLocation=(String)guiaLocation.get(i);
					strGuiaDest=(String)guiaDest.get(i);					
					
					pst=con.prepareStatement(q);
					
					pst.setString(1,strGuiaNumber);
					pst.setString(2,"IIM");
					pst.setInt(3,serialNumber);
					pst.setString(4,cttNumber);
					pst.setString(5,webClientId);
					pst.setString(6,webClientId);
					pst.setString(7,"IIM");
					pst.setString(8,strGuiaLocation);
					pst.setString(9,strGuiaDest);					
					guisinsCount=pst.executeUpdate();
					i++;
					
				}
				if(i>0){
					con.commit();
					mgf.setManifestNumber(seqManifestNumber);  
				}
				else 
					mgf.setManifestNumber("");
				
			}
			
			
		}catch(Exception e){
			con.rollback();
			AccessLog.Log(e.toString());
			AccessLog.Log(e.getMessage());
			AccessLog.Log(e);
			mgf.setManifestNumber("");
		}finally{
			try{
				if(rs!=null)
					rs.close();
				if(pst!=null)
					pst.close();
			}
			catch(Exception e){
				AccessLog.Log(e);
			}
		}
		
		return guisinsCount;
	}
	
	public ArrayList getBookedGuiasDetails(Connection con,
										   JavManifestGenerationForm mgf,
										   String webClientId,
										   String orginClientId,
										   HttpServletRequest request) throws Exception{
		
		String strSqlQuery="";
		PreparedStatement pst=null;
		CallableStatement cst=null;
		ResultSet rs=null;
		JavManifestRecord manifestRecObj=null;
		ArrayList manifestArrayList=new ArrayList();
		
		//int totRec=0;
		//int totPages=0;
		//int pageIndex=0;
		//int currentRec=0;
		//int endRec=0;
		//double maxPages=0;
		//String operation=null;
		
		try{	
			
			strSqlQuery="SELECT  GH_FORM_NO,"+
						"GH_GUIA_NO,"+
						"+GH_DEST_BRNC_ID,"+
						"GH_DEST_CLNT_NAME,"+
						"GH_GUIA_AMNT,"+
						"GH_NUMB_PACK "+						
						"FROM BOK_GUIA_HEAD "+
						"WHERE GH_ORGN_CLNT_ID=? AND GH_GUIA_TYPE=?  AND GH_ACTV_FLAG=? AND "+
						"  gh_orgn_brnc_id=? and GH_GUIA_REFR_NO is null AND GH_RAD_FLAG =?";
			
			
			pst = con.prepareStatement(strSqlQuery);					
			pst.setString(1,webClientId);
			pst.setString(2,"H");
			pst.setString(3,"A");
			pst.setString(4,orginClientId);
			pst.setString(5,"1");
			
			//AccessLog.Log("Query......"+strSqlQuery);
			
			rs =pst.executeQuery();	
			
			
			while(rs.next()){				
				
				manifestRecObj=new JavManifestRecord(); 
				
				manifestRecObj.setFormNumber((rs.getString("GH_FORM_NO")==null?"":(rs.getString("GH_FORM_NO"))));
				manifestRecObj.setGuiaNumber((rs.getString("GH_GUIA_NO")==null?"":(rs.getString("GH_GUIA_NO"))));
				manifestRecObj.setGuiaAmount((rs.getString("GH_GUIA_AMNT")==null?"":(rs.getString("GH_GUIA_AMNT"))));
				manifestRecObj.setNumPack((rs.getString("GH_NUMB_PACK")==null?"":(rs.getString("GH_NUMB_PACK"))));
				manifestRecObj.setDestClientName((rs.getString("GH_DEST_CLNT_NAME")==null?"":(rs.getString("GH_DEST_CLNT_NAME"))));
				
				
				// Get the Branch Name Using GH_DEST_BRNC_ID...				
				String StrDestBrnachId=rs.getString("GH_DEST_BRNC_ID");
				
				/*
				*get The Branch name (use pack_web.Fun_ftch_brnc_name function – Passed parameter is GH_DEST_BRNC_ID)	
				*/
				cst=con.prepareCall("begin ? := pack_web.Fun_ftch_brnc_name(?); end;");
				cst.registerOutParameter(1,Types.VARCHAR ); 	 	   
				cst.setString(2,StrDestBrnachId);
				cst.execute();
				String strDestBranchName=(cst.getString(1)==null?"":cst.getString(1));
				
				//AccessLog.Log("Dest Branch Name......."+strDestBranchName);
				
				manifestRecObj.setDestBranch(strDestBranchName);	
				
				manifestArrayList.add(manifestRecObj);	
				manifestRecObj=null;									
				
				
			}			
			
			
			
		}catch(Exception e){			
			AccessLog.Log(e.getMessage());
			AccessLog.Log(e.toString());
			AccessLog.Log(e);
			
			
		} finally{
			try{						
				if(rs!=null)
					rs.close();
				if(pst!=null)
					pst.close();							 
				
			}
			catch(Exception e){
				AccessLog.Log(e);
			}
		}		
		
		return manifestArrayList;
	}
	
	public java.sql.Date getDate(String datestring,
								 Connection con)throws Exception {
		ResultSet rs = null;
		PreparedStatement pstm = null;
		java.sql.Date retValue = null;		
		
		pstm = con.prepareStatement("SELECT	TO_DATE(?,'dd/mm/yyyy hh24:mi') FROM DUAL");
		pstm.setString(1,datestring);
		rs = pstm.executeQuery();		
		while( rs.next())
		{
			retValue = rs.getDate(1);			
		}						
		
		return retValue;
	}

	 // Added by Sam.D.Jabeen on [31-May-2006]

	public String getBranchLocationType(String strBranchId,Connection con) throws Exception{
		
		ResultSet	rs			= null;
		PreparedStatement pstmt	= null;
		String strBranchLocType	= null;

		String query = "SELECT bm_brnc_loc_type FROM sys_brnc_mstr WHERE bm_brnc_id = ?";

		try{

			pstmt = con.prepareStatement(query);

			pstmt.setString(1,strBranchId);

			rs =  pstmt.executeQuery();

			if(rs.next()){
				strBranchLocType = rs.getString(1); 
			}

		}catch(SQLException sqlException){
			AccessLog.Log(sqlException.getMessage());
		}catch(Exception exception){
			AccessLog.Log(exception.getMessage());
		}finally{
			close(pstmt,rs);
		}	
		
		return strBranchLocType;
	
	}

	 // Added by Sam.D.Jabeen on [31-May-2006]

	private void close(PreparedStatement pstmt, ResultSet rs) throws Exception{

		try{						
			if(rs!=null){
				rs.close();
			}
			if(pstmt!=null){
				pstmt.close();							 
			}
		}catch(Exception e){
			AccessLog.Log(e);
		}
	}
	
	
	public ArrayList getManifestDetails(Connection con,JavManifestGenerationForm mgf,String webClientId,String orginClientId,HttpServletRequest request,String typeofdest) throws Exception{
		
			String strSqlQuery="";
			String strSubSqlQuery="";  //Added By Amal on 21-Aug-2006  
			String strGuiaNo="";// added by sundar on 30/07/2003 to handle Manifestgeneration using checking all
			PreparedStatement pst=null;	
			CallableStatement cst=null;		
			ResultSet rs=null;	
			JavManifestRecord manifestRecObj=null;				
			ArrayList manifestArrayList=new ArrayList();
		
			int totRec=0;
			//int totPages=0;
			//int pageIndex=0;
			int currentRec=0;
			int endRec=0;
			double maxPages=0;
			int count=1;// added by sundar on 30/07/2003 to handle Manifestgeneration using checking all
			//String operation=null;
		
			try{	
			
				//Query is changed by Rajkumar for Branch Location Type
				/*strSqlQuery="SELECT A.GH_FORM_NO, " +
							"A.GH_GUIA_NO, " +
							"A.GH_DEST_BRNC_ID, " +
							"A.GH_DEST_CLNT_NAME, " +
							"TO_CHAR (A.GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT, " +
							"A.GH_NUMB_PACK, " +
							"B.BM_BRNC_LOC_TYPE " +
							"FROM BOK_GUIA_HEAD A, SYS_BRNC_MSTR B " +
							"WHERE B.BM_BRNC_ID = A.GH_DEST_BRNC_ID AND A.GH_ORGN_CLNT_ID=? AND A.GH_GUIA_TYPE=?  AND A.GH_ACTV_FLAG=? AND " +
							"A.GH_ORGN_BRNC_ID=? AND A.GH_GUIA_REFR_NO IS NULL AND A.GH_RAD_FLAG =?";

				System.err.println( "TSI: " + ResultSet.TYPE_SCROLL_INSENSITIVE);
				pst = con.prepareStatement(strSqlQuery,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				pst.setString(1,webClientId);
				pst.setString(2,"H");
				pst.setString(3,"A");
				pst.setString(4,orginClientId);
				pst.setString(5,"1");*/
				
				
				//	Added By Amal on 21-Aug-2006 (Purpose: --> If the branch source orgin id is border then the following 
				//query will be executed.
				strSubSqlQuery="SELECT GH_FORM_NO, " +
										"GH_GUIA_NO, " +
										"GH_DEST_BRNC_ID, " +
										"GH_DEST_CLNT_NAME, " +
										"TO_CHAR (GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT, " +
										"GH_NUMB_PACK " +
										"FROM BOK_GUIA_HEAD "+
										"WHERE GH_ORGN_CLNT_ID=? AND GH_GUIA_TYPE=?  AND GH_ACTV_FLAG=? AND " +
										"GH_ORGN_BRNC_ID=? AND GH_GUIA_REFR_NO IS NULL AND GH_RAD_FLAG =? AND " +
										"pack_web.FUN_BRDR_BRNC(GH_DEST_BRNC_ID) = ? " ;
				
									
				strSqlQuery="SELECT  GH_FORM_NO,"+
													"GH_GUIA_NO,"+
													"GH_DEST_BRNC_ID,"+
													"GH_DEST_CLNT_NAME,"+
													"to_char(GH_GUIA_AMNT, '$999990.99') GH_GUIA_AMNT ,"+
													"GH_NUMB_PACK "+
													"FROM BOK_GUIA_HEAD "+
													"WHERE GH_ORGN_CLNT_ID=? AND GH_GUIA_TYPE=?  AND GH_ACTV_FLAG=? AND "+
													"  gh_orgn_brnc_id=? and GH_GUIA_REFR_NO is null AND GH_RAD_FLAG =? ";


										//AAP//System.err.println( "TSI: " + ResultSet.TYPE_SCROLL_INSENSITIVE);
										//Added By Amal on 21-Aug-2006 (Purpose: --> to check which query to execute
									if(typeofdest.length()<=0)
									{
										//AAP//AccessLog.Log("Query......"+strSqlQuery);
										pst = con.prepareStatement(strSqlQuery,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
										pst.setString(1,webClientId);
										pst.setString(2,"H");
										pst.setString(3,"A");
										pst.setString(4,orginClientId);
										pst.setString(5,"1");
									
									}
									else
									{
										//AAP//AccessLog.Log("Query......"+strSubSqlQuery);
										
										//	Added By Amal on 21-Aug-2006 (Purpose: -->If the user select Border then the query should fetch Intenal likewise   
										
										if(typeofdest.equalsIgnoreCase("IN"))
										typeofdest="BR";
										else
										typeofdest="IN";
										
										pst = con.prepareStatement(strSubSqlQuery,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
										pst.setString(1,webClientId);
										pst.setString(2,"H");
										pst.setString(3,"A");
										pst.setString(4,orginClientId);
										pst.setString(5,"1");
										pst.setString(6,typeofdest);
									
																
									}
				
			
				rs =pst.executeQuery();
				//AAP//System.err.println( rs);
				//AAP//System.err.println( rs.toString());
			
				boolean rsFlag=rs.next();
				//AAP//AccessLog.Log("RS FLAG"+rsFlag);
			
				// count the total number of Records.
				if(rsFlag){
					rs.last();
					totRec=rs.getRow();
					count=mgf.getTotalRecord();// added by sundar on 30/07/2003 to handle Manifestgeneration using checking all
					mgf.setTotalRecord(totRec);
					rs.beforeFirst();
					mgf.setNoDataFound("N");
					request.setAttribute("sDataFlag","No");
				
				}			
				else if(rsFlag==false){
					mgf.setNoDataFound("Y");
					request.setAttribute("sDataFlag","Yes");
				}
			
				if((totRec%10)==0)
					maxPages=totRec/10;
				else
					maxPages=(totRec/10)+1;
			
				mgf.setMaxPageIndex(maxPages);
			
				currentRec=mgf.getCurRow();
				endRec=mgf.getEndRow();
			
				if(endRec== 0){
					endRec = 10;
					mgf.setEndRow(endRec);					
				}
			
				if(currentRec == 0){
					currentRec = 1;
					mgf.setCurRow(currentRec);			
				}
			
			 // added by sundar on 30/07/2003 to handle Manifestgeneration using checking all 
				rs.beforeFirst();
			
				if(count==0)
				{
					while(rs.next())
					{
							manifestRecObj=new JavManifestRecord(); 
							manifestRecObj.setGuiaNumber((rs.getString("GH_GUIA_NO")==null?"":(rs.getString("GH_GUIA_NO"))));
							strGuiaNo+=(rs.getString("GH_GUIA_NO")==null?"":(rs.getString("GH_GUIA_NO")))+" ";
					
						
					}
					mgf.setClickedItems(strGuiaNo);
				}
			
				rs.beforeFirst();
				while(rs.next()){
					if(currentRec<=endRec){
					
						rs.absolute(currentRec);
						manifestRecObj=new JavManifestRecord(); 
					
						manifestRecObj.setFormNumber((rs.getString("GH_FORM_NO")==null?"":(rs.getString("GH_FORM_NO"))));
						manifestRecObj.setGuiaNumber((rs.getString("GH_GUIA_NO")==null?"":(rs.getString("GH_GUIA_NO"))));
						manifestRecObj.setGuiaAmount((rs.getString("GH_GUIA_AMNT")==null?"":(rs.getString("GH_GUIA_AMNT"))));
						manifestRecObj.setNumPack((rs.getString("GH_NUMB_PACK")==null?"":(rs.getString("GH_NUMB_PACK"))));
						manifestRecObj.setDestClientName((rs.getString("GH_DEST_CLNT_NAME")==null?"":(rs.getString("GH_DEST_CLNT_NAME"))));
					
					
						// Get the Branch Name Using GH_DEST_BRNC_ID...				
						String StrDestBrnachId=rs.getString("GH_DEST_BRNC_ID");
					
						/*
						*get The Branch name (use pack_web.Fun_ftch_brnc_name function – Passed parameter is GH_DEST_BRNC_ID)	
						*/
						cst=con.prepareCall("begin ? := pack_web.Fun_ftch_brnc_name(?); end;");
						cst.registerOutParameter(1,Types.VARCHAR );
						cst.setString(2,StrDestBrnachId);
						cst.execute();
						String strDestBranchName=(cst.getString(1)==null?"":cst.getString(1));
					
						//AccessLog.Log("Dest Branch Name......."+strDestBranchName);
					
						manifestRecObj.setDestBranch(strDestBranchName);
						//Added by Rajkumar for Branch Location Type
						//manifestRecObj.setBranchLocType((rs.getString("BM_BRNC_LOC_TYPE")==null?"":(rs.getString("BM_BRNC_LOC_TYPE"))));

						manifestArrayList.add(manifestRecObj);
						manifestRecObj=null;
						currentRec++;
					}
				}
			
			}catch(Exception e){			
				AccessLog.Log(e.getMessage());
				AccessLog.Log(e.toString());
				AccessLog.Log(e);
				mgf.setManifestNumber("");
			
			} finally{
				try{						
					if(rs!=null)
						rs.close();
					if(pst!=null)
						pst.close();							 
				
				}
				catch(Exception e){
					AccessLog.Log(e);
				}
			}		
		
			return manifestArrayList;
		}
}