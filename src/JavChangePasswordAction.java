

/**
 * File Name    : ChangePasswordAction.java
 * Description  :This is the Action Class For LoginForm
 *				 to handle and Control the Inputs of that FormBean. 
 * Date Written :  25-Feb-2003
 * @author 	    :  D.SivaKumar
 */

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;
import mx.com.paquetexpress.dto.message.body.response.Response;
import mx.com.paquetexpress.general.model.dto.LgnUserMstrResponseDTO;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bean.ConsultaParametros;
import bean.Global;
import bean.Resources;
import beanUtil.ConnectDB;
import beanUtil.LoginLdap;
import beanUtil.ModifyPasswordLdap;

public class JavChangePasswordAction extends Action {
	private final String msgError = new StringBuffer("MSERR_B_").append(this.getClass().getName()).append(".").toString();
	private StringBuffer concatena = new StringBuffer();	
	Resources resources = new Resources();
	/** Creates a new instance of LoginAction */
	public JavChangePasswordAction() {
	}
	@SuppressWarnings("rawtypes")
	public ActionForward perform(ActionMapping am,ActionForm af,HttpServletRequest request,HttpServletResponse response)
		throws ServletException,IOException{
		
		HttpSession session=null;
		Connection con=null;		
		String returnPage="";
		String userType = "CUS";
		
		try{
			
			session=request.getSession(true);
			if(session==null || session.isNew()) {
				return am.findForward("nosession");
			}
			 
			con=ConnectDB.getConnection();
			if (af instanceof JavChangePasswordForm){
			
				JavChangePasswordForm cf = (JavChangePasswordForm)af;				
				
				// Validate the Password Entered..
				
				String strUserName =(String)session.getAttribute("sClientId");
				Global global =(Global)session.getAttribute("sGlobal");
						
				String strOldPassword =cf.getOldPassword();		
				String strConfirmPassword=cf.getConfirmPassword();				
				if (cf.getAction() == null || "".equals(cf.getAction())) {
					return am.findForward(returnPage);
				}else if ("modifyPassword".equals(cf.getAction())) {
					String useUrl = "";
					if (request.getRemoteAddr().trim().substring(0,3).equals("0:0") || 
							request.getRemoteAddr().trim().substring(0,8).equals("192.168.") || 
							request.getRemoteAddr().trim().substring(0,7).equals("172.16.")) {
						useUrl = "INT";//interno
					} else {
						useUrl = "EXT";//externo
					}
					
					/*valida usuario y conrase�a en LDAP*/
					boolean validateFalg = false;
					LoginLdap loginLdap = new LoginLdap();
					con.close();
					LgnUserMstrResponseDTO lgnUserMstrResponseDTO = loginLdap.login(global.getOrigenUserClave(), strOldPassword, userType, "ES",useUrl);
					validateFalg = validateLdapLogin(lgnUserMstrResponseDTO);
					if (!validateFalg) {
						lgnUserMstrResponseDTO = loginLdap.login(global.getOrigenUserClave(), strOldPassword.toUpperCase(), userType, "ES",useUrl);
					}
					con = ConnectDB.getConnection();
					
					String regexPassVal = getRegexPassVal(con);
					boolean validPass = Pattern.matches(regexPassVal, strConfirmPassword);
					cf.setValPassReq(validPass);
					/*FIN valida usuario y conrase�a en LDAP*/
					if(validateFalg && !(strOldPassword.equals(strConfirmPassword))){
						HashMap datos = getUserInfo(con, global.getOrigenUserClave(), global.getClientIdAgreement());
						
						if (datos!= null && validPass) {
							
							ModifyPasswordLdap modifyPasswordLdap = new ModifyPasswordLdap();
							con.close();
							modifyPasswordLdap.mdfdUserData(useUrl, global.getOrigenUserClave(), strConfirmPassword, 
											datos.get("userName").toString(), userType, global.getOrigenUserClave(), 
											datos.get("eMail").toString(), 
											global.getClientIdAgreement(), 
											datos.get("apiAction").toString(), "webbooking");
							con = ConnectDB.getConnection();
							Response msjeResponse = modifyPasswordLdap.getResponse();
							
							if (Boolean.TRUE.equals(msjeResponse.getSuccess())) {
								returnPage="success";	
							} else {
								returnPage="failed";
							}					
						}
						if (returnPage.equals("success")){
							int pwdUpdateFlag=changePassword(con,strUserName,strConfirmPassword,global.getOrigenUserClave());
							
							if(pwdUpdateFlag>0) {						
								returnPage="success";
								request.setAttribute("sPwdUpdated","yes");
								cf.setsPwdUpdated("yes");
								
							}else{
								returnPage="failed";
							}
						}
						//
					}else{
						returnPage="failed";
					}
					
					if (returnPage.equals("failed") || !validPass) {
						request.setAttribute("sPwdUpdated","no");
						cf.setsPwdUpdated("no");
						returnPage="failed";
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			resources.cerrarConexion(con);
		}		
		return am.findForward(returnPage);
	}
	private boolean validateLdapLogin(LgnUserMstrResponseDTO lgnUserMstrResponseDTO) {
		if (lgnUserMstrResponseDTO == null || lgnUserMstrResponseDTO.getSuccess() == null) {
			return false;
		} else if (lgnUserMstrResponseDTO.getSuccess().equals("true")) {
			return true;
		}
		return false;
	}
	
	public String getRegexPassVal(Connection con) {
		ConsultaParametros consulta = new ConsultaParametros();
		ArrayList<String> res = (ArrayList<String>)consulta.QryMdulTypeParm1(con, "SYS", "PWD_VAL", "REGEX").get(0);
		return res.get(4);
	}
	
	public int changePassword(Connection con,String username,String cinfirmpassword,String userlog) throws Exception{
		PreparedStatement pst=null;
		CallableStatement cst=null;
		ResultSet rs=null;
		String query;
		int updateFlag=0;		
		try{
			
		    cst=con.prepareCall("begin ? :=UPPER(pack_web.FUN_ENCR_DECR_PSWD(?,?)); end;");
			cst.registerOutParameter(1,Types.VARCHAR);
			cst.setString(2,cinfirmpassword.trim());
			cst.setString(3,"E");
			cst.execute();
			
			String strEncrPassword=cst.getString(1).toUpperCase();
			//AAP//AccessLog.Log("Encripted Password"+strEncrPassword);
			
			if(cst!=null)
				cst.close();
			  
			
			query="UPDATE SYS_CLNT_USER SET CU_USER_PWRD=? WHERE CU_USER_ID=?";
						
			pst=con.prepareStatement(query);
			pst.setString(1,strEncrPassword);
			pst.setString(2,userlog);
			updateFlag=pst.executeUpdate();		
			con.commit();
			
		}catch(Exception e){
			AccessLog.Log(concatena.delete(0, concatena.length())
					.append(msgError).append("changePassword()_Error:")
					.append(e).toString());
		}finally{
			resources.closeResources(rs, pst, cst);
		 }
		return updateFlag;
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap getUserInfo(Connection con, String user, String clnt) throws Exception{
		HashMap result = new HashMap(3);
		PreparedStatement pst=null;
		ResultSet rs=null;
		String query;
		String userName = "";
		String eMail = "";
		String apiAction = "UPDATE+CENT+PWD";
		try{
		    
			query = "SELECT CU_USER_NAME, CU_USER_MAIL FROM SYS_CLNT_USER WHERE CU_USER_ID = ? AND CU_CLNT_ID = ?";
						
			pst = con.prepareStatement(query);
			pst.setString(1,user);
			pst.setString(2,clnt);
			rs = pst.executeQuery();
			
			if (rs.next()) {
				userName = rs.getString(1) == null ? "" : rs.getString(1);
				eMail = rs.getString(2) == null ? "" : rs.getString(2);
			}
			
			if (rs!=null) {
				rs.close();
			}
			
			if (pst!=null) {
				pst.close();
			}
			
			query = "SELECT UM_USER_ACCESS FROM SYS_PARM_MSTR, LGN_USER_MSTR "
			           + "WHERE PM_MDUL_ID = ? "
			           + "AND PM_PARM_TYPE = ? "
			           + "AND ID_PROFILE = PM_VLUE1_ID "
			           + "AND PM_PARM_CODE1 = ? "
			           + "AND ID_CLNT_MSTR = ? "
			           + "AND UM_USER_ACCESS = ? ";
			
			pst = con.prepareStatement(query);
			pst.setString(1,"GLP");
			pst.setString(2,"PROFIL_CUSTOMER");
			pst.setString(3,"CENT+WEBBOOK");			
			pst.setString(4,clnt);
			pst.setString(5,user);
			
			rs = pst.executeQuery();
			
			if (rs.next()) {
				apiAction = "UPDATE+WEBBOOK+PWD";
			}
			result.put("userName", userName);
			result.put("eMail", eMail);
			result.put("apiAction", apiAction);
		}catch(Exception e){
			AccessLog.Log(concatena.delete(0, concatena.length())
					.append(msgError).append("getUserInfo()_Error:")
					.append(e).toString());
			e.printStackTrace();
		}finally{
			resources.closeResources(rs, pst);
		 }
		return result;		
	}
}
