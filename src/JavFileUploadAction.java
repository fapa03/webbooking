
/*
 * Created on Aug 24, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import logger.AccessLog;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import bean.Global;
import beanUtil.ConnectDB;

/**
 * @author kart1479
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class JavFileUploadAction extends Action {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.struts.action.Action#execute(org.apache.struts.action.
	 * ActionMapping, org.apache.struts.action.ActionForm,
	 * javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */

	Time sysTime = null;
	JavImportWebbookingForm WebbookingForm = new JavImportWebbookingForm();
	ArrayList listofNames = new ArrayList();
	ArrayList listofForms = new ArrayList();
	String fileName = "";
	String processfilename = "";

	public ActionForward perform(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws FileNotFoundException, IOException {
		Connection con = null;
		String resultExtract = "";
		String flow = "";
		HttpSession session = request.getSession(false);
		// if session expired ask him to login once again
		if (session == null || session.isNew()) {
			return mapping.findForward("nosession");
		}

		Global glo = (Global) session.getAttribute("sGlobal");

		if (glo == null)
			return mapping.findForward("nosession");

		JavFileUploadForm fileUpForm = new JavFileUploadForm();
		if (request.getParameter("action") != null)
			flow = request.getParameter("action");

		FormFile file = null;
		// Check user pressed confirm button after taking printout
		AccessLog.Log("flow " + flow);
		if ((!flow.equalsIgnoreCase("confirm"))
				&& (!flow.equalsIgnoreCase("log"))) {
			fileUpForm = (JavFileUploadForm) form;
			if (fileUpForm != null)
				file = fileUpForm.getTheFile();

			if ((fileUpForm == null) || (file == null))
				return mapping.findForward("UploadPage");
		}
		AccessLog.Log("paso1 ");
		try {
			con = ConnectDB.getConnection();
			con.setAutoCommit(false);
		} catch (SQLException e) {
			AccessLog.Log("Can not Connect" + e.getMessage());
		}
		String message = "";
		if (con == null)
			AccessLog.Log("Can not Connect");
		
		try {
			AccessLog.Log("paso2 ");
			// check user uploads file
			if (flow.equalsIgnoreCase("upload")) {
				String fname = "";
				fname = file.getFileName();
				fname = fname.substring(fname.length() - 3, fname.length());
				AccessLog.Log("file.getFileName() " + file.getFileName());
				AccessLog.Log("file.getFileName() index of " + file.getFileName().substring(file.getFileName().indexOf('.')));
				AccessLog.Log("fname " + fname);
				// check type of the file as txt
				if (!fname.equalsIgnoreCase("txt")) {
					request.setAttribute("errormsgprint",
							"Upload only TextFiles");
					return mapping.findForward("UploadPage");
				}
				// storing the content as a file
				resultExtract = saveFile(fileUpForm, request, con);

				if (resultExtract.equalsIgnoreCase("UploadPage")) {
					return mapping.findForward("UploadPage");
				} else if (resultExtract.equalsIgnoreCase("success")) {
					// before storing the content of the file check the file
					// already exists
					message = preUpload(con, glo.clientId, request,
							file.getFileName());
					if (message.equalsIgnoreCase("continue")) {
						WordCount wc = new WordCount();
						File f = new File(request.getRealPath("/")
								+ File.separator + "Guiaprint1");
						String fi = f.toString();
						fi = fi + File.separator + fileName;
						listofForms = wc.count(fi);
						// check for empty content
						if (listofForms.size() == 0) {
							request.setAttribute("errormsgprint",
									"Do not Upload Empty TextFiles");
							return mapping.findForward("UploadPage");
						}
						// AAP//AccessLog.Log(listofForms.size()+"");
						// AAP//AccessLog.Log("before calling db");
						// store in the table
						message = insertToTempDB1(con, file.getFileName(),
								glo.clientId, request);
						AccessLog.Log(message);
					} else {
						if (request.getAttribute("errormsgprint") != null)
							AccessLog.Log("can not Upload the file"
									+ request.getAttribute("errormsgprint"));
						// AAP//AccessLog.Log("can not Upload the file");
						return mapping.findForward("thispage");

					}
				}
			}
			// check the user pressed the process
			else if (flow.equalsIgnoreCase("process")) {
				// check the user already uploaded the file
				message = preProcess(con, glo.clientId, request);
				if (message.equalsIgnoreCase("continue"))
					// call the process method
					process(con, glo.clientId);
				else
					return mapping.findForward("thispage");
				try {
					con.commit();
				} catch (SQLException e) {
					AccessLog.Log("can not commit");
					e.printStackTrace();
				}
			}
			// check the user pressed the printguia file
			else if (flow.equalsIgnoreCase("print")) {
				message = readGuiaContent_WriteFile(request, con, glo.clientId);

				if (message.equalsIgnoreCase("thispage")) {
					request.setAttribute("guia", "No Guia is generated");
					// to store the error message
					message = logWrite(request, con, glo.clientId);
					if (message.equalsIgnoreCase("success")) {
						request.setAttribute("log", "Log data");
						return mapping.findForward("success");
					} else {
						request.setAttribute("errormsgprint",
								"Guia is Not Available For Print");
						return mapping.findForward("thispage");
					}
				} else {
					message = logWrite(request, con, glo.clientId);
					if (message.equalsIgnoreCase("success"))
						request.setAttribute("log", "Log data");
					return mapping.findForward("success");
				}
			}
			// check the user confirmed the print
			else if (flow.equalsIgnoreCase("confirm")) {
				// AAP//AccessLog.Log("inside the print confirm");
				message = printConfirm(con, glo.clientId);
				if (message.equalsIgnoreCase("failure"))
					request.setAttribute("errormsgprint",
							"Print flag can not be updated... ");
			}
			// check the user pressed log to take the error log file
			else if (flow.equalsIgnoreCase("log")) {
				message = logWrite(request, con, glo.clientId);
				if (message.equalsIgnoreCase("success"))
					return mapping.findForward("log");

			}
			// AAP//else
			// AAP//System.out.println("inside else");

		} catch (Exception e) {
			AccessLog.Log("General Exception caught" + e.getMessage());
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				AccessLog.Log("can not close connection" + e.getMessage());
			}
		}
		return mapping.findForward("thispage");
	}

	public String logWrite(HttpServletRequest request, Connection con,
			String clientId) {
		java.sql.CallableStatement cst = null;

		HttpSession session = request.getSession(false);
		try {
			PreparedStatement pst = con
					.prepareStatement("select BC_FILE_NAME,bc_eror_log from web_bach_guia_chck where bc_clnt_id = ? and BC_PRCS_FLAG = 'Y' and BC_PRNT_FLAG = 'N'");
			pst.setString(1, clientId);
			ResultSet rs1 = pst.executeQuery();
			// if(rs1.next())
			// {
			// ArrayList values=new ArrayList();
			// PreparedStatement
			// pst1=con.prepareStatement("select bc_eror_log from web_bach_guia where BG_ORGN_CLNT_ID = ? and BG_FILE_NAME =?");
			// pst1.setString(1,clientId);
			// pst1.setString(2,rs1.getString(1));
			// ResultSet rs=pst1.executeQuery();
			ArrayList values = new ArrayList();
			String store = "";
			while (rs1.next()) {
				store = rs1.getString(2);
				if ((store == null) || (store == "") || (store.length() < 0)) {
					request.setAttribute("errormsgprint",
							"Log Is Not Available To Print");
					break;
				}
				StringBuffer buffer = new StringBuffer(store);
				values.add(buffer);
			}
			if (values.size() > 0) {
				// AAP//AccessLog.Log("values size"+values.size());
				writeToLogFile(request, values, getTimeStamp(con).getTime());
				if (rs1 != null)
					rs1.close();
				pst.close();
				return "success";
			}

			// select bg_print_file from web_bach_guia where
			// nvl(bg_prnt_flag,'N')='N' and bg_orgn_clnt_id="+clientId );
			else {
				if (rs1 != null)
					rs1.close();
				pst.close();
				request.setAttribute("errormsgprint",
						"Log Is Not Available To Print");
				return "thispage";
			}

			// PreparedStatement
			// pst=con.prepareStatement("update web_bach_guia set bg_prnt_flag= ? where bg_orgn_clnt_id=?");
			// pst.setString(1,"Y");
			// pst.setString(2,clientId);
			// pst.executeUpdate();
			// pst.close();

		} catch (Exception e) {
			AccessLog
					.Log("Error while read content from db and write it into file"
							+ e.getMessage());
			e.printStackTrace();
			return "thispage";
		}

	}

	public String printConfirm(Connection con, String clientId) {
		PreparedStatement pst = null;
		try {
			// AAP//AccessLog.Log("inside print confirm");
			// pst=con.prepareStatement("update web_bach_guia_chck set BC_PRNT_FLAG='Y' where bc_clnt_id =? and BC_PRNT_FLAG='N' and BC_PRCS_FLAG='Y'");
			// pst=con.prepareStatement("delete from web_bach_guia_chck where bc_clnt_id =? and BC_PRNT_FLAG='N' and BC_PRCS_FLAG='Y' and bc_eror_log is not null");
			// pst.setString(1,clientId);
			// pst.executeUpdate();
			//
			// pst.close();

			// pst=con.prepareStatement("update web_bach_guia_chck set BC_PRNT_FLAG='Y' where bc_clnt_id =? and BC_PRNT_FLAG='N' and BC_PRCS_FLAG='Y' and bc_eror_log is null ");
			pst = con
					.prepareStatement("update web_bach_guia_chck set BC_PRNT_FLAG='Y' where bc_clnt_id =? and BC_PRNT_FLAG='N' and BC_PRCS_FLAG='Y' ");
			pst.setString(1, clientId);
			pst.executeUpdate();

			pst.close();
			con.commit();
			// AAP//AccessLog.Log("after commiting print confirm");
		} catch (Exception e) {
			AccessLog.Log("Could not Update Print flag" + e.getMessage());
			return "failure";
		}
		return "";
	}

	public String preUpload(Connection con, String clientId,
			HttpServletRequest request, String fileName) {
		PreparedStatement pst = null;
		try {
			pst = con
					.prepareStatement("select bc_prcs_flag from web_bach_guia_chck where bc_clnt_id = ? and bc_prcs_flag in('P','N') ");
			pst.setString(1, clientId);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				if (rs.getString(1).equalsIgnoreCase("N"))
					request.setAttribute("errormsgprint",
							"File is waiting without Processing...........Please Process it");
				else
					request.setAttribute("errormsgprint",
							"Middle of the Processing...........Please Wait");
				if (rs != null)
					rs.close();
				pst.close();

				return "thispage";

			} else {
				rs.close();
				pst.close();
				pst = con
						.prepareStatement("select 1 from web_bach_guia_chck where bc_clnt_id = ? and BC_PRNT_FLAG='N' ");
				pst.setString(1, clientId);

				rs = pst.executeQuery();

				if (rs.next()) {
					if (rs != null)
						rs.close();
					pst.close();

					request.setAttribute("errormsgprint",
							"File  Exists Without taking print...Finish Printing of This");
					return "thispage";

				}
				return "continue";

			}
		} catch (SQLException e) {
			AccessLog.Log("can not create Statement");
			e.printStackTrace();
		}

		return "";
	}

	public String preProcess(Connection con, String clientId,
			HttpServletRequest request) {
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		try {
			pst = con
					.prepareStatement("select BC_FILE_NAME, BC_PRCS_FLAG from web_bach_guia_chck where bc_clnt_id =? and BC_PRCS_FLAG in ('N','P')");
			pst.setString(1, clientId);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				processfilename = rs.getString(1);
				if (rs.getString(2).equalsIgnoreCase("N")) {

					pst1 = con
							.prepareStatement("update web_bach_guia_chck set BC_PRCS_FLAG='P' where bc_clnt_id = ? and BC_FILE_NAME = ? and BC_PRCS_FLAG='N'");
					pst1.setString(1, clientId);
					pst1.setString(2, processfilename);
					pst1.executeUpdate();
					pst1.close();
					rs.close();
					pst.close();
					con.commit();
					request.setAttribute("errormsgprint",
							"Processing Over........");
					return "continue";

				} else if (rs.getString(2).equalsIgnoreCase("P")) {
					request.setAttribute("errormsgprint",
							"Middle Of the Processing........Please Wait ");
					rs.close();
					pst.close();

					return "thispage";
				}

			} else {
				if (rs != null)
					rs.close();
				pst.close();
				request.setAttribute("errormsgprint",
						"No Files to Process...........");
				return "thispage";

			}
		} catch (SQLException e) {
			AccessLog.Log("can not create Statement");
			e.printStackTrace();
		}

		return "";
	}

	public void process(Connection con, String clientId) {
		CallableStatement cst = null;
		String query = "{call pack_web_guia_bach_prep.pro_read_bach_file(?,?)}";
		try {
			// AAP//AccessLog.Log("inside process");
			cst = con.prepareCall(query);
			cst.setString(1, clientId);
			cst.setString(2, processfilename);
			cst.executeQuery();
			cst.close();

		} catch (SQLException e) {
			AccessLog.Log(e.getMessage());

		}

	}

	// public void testGlobal(HttpServletRequest request)
	// {
	//
	// System.out.println("inside test global");
	// HttpSession session =request.getSession(false);
	// Global global=(Global)session.getAttribute("sGlobal");
	// System.out.println(global.assignedBranch);
	// System.out.println(global.clientId);
	// System.out.println(global.rfc);
	// System.out.println(global.destinationBranchId);
	// System.out.println(global.clientName);
	// System.out.println(global.commentText);
	// System.out.println(global.tarifType);
	// System.out.println(global.groupClientId);
	// System.out.println("outside test global");
	//
	//
	// }
	// Reading the File content from database and write it into the file
	//
	// public String insertToTempDB(Connection con,String nameFile,String
	// clientId,HttpServletRequest request)
	// {
	//
	// try {
	// int result=0;
	// String lineNumber="";
	// for(int i=0;i<(listofForms.size());i++)
	// {
	// JavImportWebbookingForm WebbookingFormverify=null;
	// JavImportWebbookingForm Form=(JavImportWebbookingForm)listofForms.get(i);
	// if(i>0)
	// {
	// WebbookingFormverify=(JavImportWebbookingForm)listofForms.get(i-1);
	//
	// }
	//
	// if(i==0)
	// {
	// PreparedStatement
	// pst=con.prepareStatement("insert into web_bach_guia values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	// pst.setString(1,nameFile);
	// pst.setString(2,Form.getRowNum());
	// pst.setString(3,clientId);
	// pst.setString(4,null);
	// pst.setString(5,null);
	// pst.setString(6,Form.getDestClient());
	// pst.setString(7,Form.getDestClientBranch());
	// pst.setString(8,Form.getDestClientName());
	// pst.setString(9,Form.getDestStreet());
	// pst.setString(10,Form.getDestDoorNo());
	// pst.setString(11,Form.getDestColony());
	// pst.setString(12,Form.getDestZipCode());
	// pst.setString(13,Form.getDestClientCity());
	// pst.setString(14,Form.getDestClientPhone());
	// pst.setString(15,Form.getServicesDeliveryType());
	// pst.setString(16,Form.getServicesAck());
	// pst.setString(17,Form.getServicesCodValue());
	// pst.setString(18,Form.getServicesDeclaredValue());
	// pst.setString(19,Form.getServicesCommentaries());
	// pst.setString(20,Form.getServicesReference());
	// pst.setString(21,null);
	// pst.setString(22,null);
	// pst.setString(23,null);
	// pst.setString(24,null);
	// pst.setString(25,null);
	// pst.setString(26,null);
	// pst.setString(27,null);
	// pst.setString(28,"N");
	// pst.setString(29,"N");
	//
	// lineNumber=Form.getRowNum();
	// result=pst.executeUpdate();
	// pst.close();
	// pst=con.prepareStatement("insert into web_bach_ship_srvc values(?,?,?,?,?,?,?,?,?,?,?)");
	// pst.setString(1,nameFile);
	// pst.setString(2,lineNumber);
	// pst.setString(3,null);
	// pst.setString(4,null);
	// pst.setString(5,null);
	// pst.setString(6,null);
	// pst.setString(7,Form.getShipQuantity());
	// pst.setString(8,Form.getShipDescription());
	// pst.setString(9,Form.getShipContent());
	// pst.setString(10,Form.getShipWeight());
	// pst.setString(11,Form.getShipVolume());
	// result=pst.executeUpdate();
	// pst.close();
	//
	// }
	// if(i>0)
	// {
	// if(Form.getDestClientBranch().equalsIgnoreCase(WebbookingFormverify.getDestClientBranch()))
	// {
	// PreparedStatement
	// pst=con.prepareStatement("insert into web_bach_ship_srvc values(?,?,?,?,?,?,?,?,?,?,?)");
	// pst.setString(1,nameFile);
	// pst.setString(2,lineNumber);
	// pst.setString(3,null);
	// pst.setString(4,null);
	// pst.setString(5,null);
	// pst.setString(6,null);
	// pst.setString(7,Form.getShipQuantity());
	// pst.setString(8,Form.getShipDescription());
	// pst.setString(9,Form.getShipContent());
	// pst.setString(10,Form.getShipWeight());
	// pst.setString(11,Form.getShipVolume());
	// result=pst.executeUpdate();
	// pst.close();
	//
	//
	// }
	//
	// else
	// {
	//
	// PreparedStatement
	// pst=con.prepareStatement("insert into web_bach_guia values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
	// pst.setString(1,nameFile);
	// pst.setString(2,Form.getRowNum());
	// pst.setString(3,clientId);
	// pst.setString(4,null);
	// pst.setString(5,null);
	// pst.setString(6,Form.getDestClient());
	// pst.setString(7,Form.getDestClientBranch());
	// pst.setString(8,Form.getDestClientName());
	// pst.setString(9,Form.getDestStreet());
	// pst.setString(10,Form.getDestDoorNo());
	// pst.setString(11,Form.getDestColony());
	// pst.setString(12,Form.getDestZipCode());
	// pst.setString(13,Form.getDestClientCity());
	// pst.setString(14,Form.getDestClientPhone());
	// pst.setString(15,Form.getServicesDeliveryType());
	// pst.setString(16,Form.getServicesAck());
	// pst.setString(17,Form.getServicesCodValue());
	// pst.setString(18,Form.getServicesDeclaredValue());
	// pst.setString(19,Form.getServicesCommentaries());
	// pst.setString(20,Form.getServicesReference());
	// pst.setString(21,null);
	// pst.setString(22,null);
	// pst.setString(23,null);
	// pst.setString(24,null);
	// pst.setString(25,null);
	// pst.setString(26,null);
	// pst.setString(27,null);
	// pst.setString(28,"N");
	// pst.setString(29,"N");
	//
	// lineNumber=Form.getRowNum();
	//
	// result=pst.executeUpdate();
	// pst.close();
	// pst=con.prepareStatement("insert into web_bach_ship_srvc values(?,?,?,?,?,?,?,?,?,?,?)");
	// pst.setString(1,nameFile);
	// pst.setString(2,lineNumber);
	// pst.setString(3,null);
	// pst.setString(4,null);
	// pst.setString(5,null);
	// pst.setString(6,null);
	// pst.setString(7,Form.getShipQuantity());
	// pst.setString(8,Form.getShipDescription());
	// pst.setString(9,Form.getShipContent());
	// pst.setString(10,Form.getShipWeight());
	// pst.setString(11,Form.getShipVolume());
	// result=pst.executeUpdate();
	// pst.close();
	// }
	// }
	//
	// }
	// try{
	// PreparedStatement
	// pst=con.prepareStatement("insert into web_bach_guia_chck  values(?,?,?,?,?,?,?,?,?)");
	// pst.setString(1,nameFile);
	// pst.setString(2,null);
	// pst.setString(3,clientId);
	// pst.setString(4,"N");
	// pst.setString(5,"N");
	// pst.setString(6,null);
	// pst.setString(7,null);
	// pst.setString(8,null);
	// pst.setString(9,null);
	// pst.executeUpdate();
	// con.commit();
	// }catch(Exception e)
	// {
	// request.setAttribute("errormsgprint","Problem in Commit");
	// AccessLog.Log(e.getMessage());
	// con.rollback();
	// }
	//
	//
	//
	// } catch (SQLException e) {
	// AccessLog.Log("Cannot create Prepared statement"+e.getMessage());
	//
	// request.setAttribute("errormsgprint","Problem in Insertion .....");
	//
	// }
	// return "success";
	// }

	public String insertToTempDB1(Connection con, String nameFile,
			String clientId, HttpServletRequest request) {

		try {
			int result = 0;
			String lineNumber = "";
			for (int i = 0; i < (listofForms.size()); i++) {
				// JavImportWebbookingForm WebbookingFormverify=null;
				JavImportWebbookingForm Form = (JavImportWebbookingForm) listofForms
						.get(i);
				// if(i>0)
				// {
				// WebbookingFormverify=(JavImportWebbookingForm)listofForms.get(i-1);

				// }

				// if(i==0)
				// {
				// PreparedStatement
				// pst=con.prepareStatement("insert into web_bach_guia values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				// pst.setString(1,nameFile);
				// pst.setString(2,Form.getRowNum());
				// pst.setString(3,clientId);
				// pst.setString(4,null);
				// pst.setString(5,null);
				// pst.setString(6,Form.getDestClient());
				// pst.setString(7,Form.getDestClientBranch());
				// pst.setString(8,Form.getDestClientName());
				// pst.setString(9,Form.getDestStreet());
				// pst.setString(10,Form.getDestDoorNo());
				// pst.setString(11,Form.getDestColony());
				// pst.setString(12,Form.getDestZipCode());
				// pst.setString(13,Form.getDestClientCity());
				// pst.setString(14,Form.getDestClientPhone());
				// pst.setString(15,Form.getServicesDeliveryType());
				// pst.setString(16,Form.getServicesAck());
				// pst.setString(17,Form.getServicesCodValue());
				// pst.setString(18,Form.getServicesDeclaredValue());
				// pst.setString(19,Form.getServicesCommentaries());
				// pst.setString(20,Form.getServicesReference());
				// pst.setString(21,null);
				// pst.setString(22,null);
				// pst.setString(23,null);
				// pst.setString(24,null);
				// pst.setString(25,null);
				// pst.setString(26,null);
				// pst.setString(27,null);
				// pst.setString(28,"N");
				// pst.setString(29,"N");
				//
				// lineNumber=Form.getRowNum();
				// result=pst.executeUpdate();
				// pst.close();
				// pst=con.prepareStatement("insert into web_bach_ship_srvc values(?,?,?,?,?,?,?,?,?,?,?)");
				// pst.setString(1,nameFile);
				// pst.setString(2,lineNumber);
				// pst.setString(3,null);
				// pst.setString(4,null);
				// pst.setString(5,null);
				// pst.setString(6,null);
				// pst.setString(7,Form.getShipQuantity());
				// pst.setString(8,Form.getShipDescription());
				// pst.setString(9,Form.getShipContent());
				// pst.setString(10,Form.getShipWeight());
				// pst.setString(11,Form.getShipVolume());
				// result=pst.executeUpdate();
				// pst.close();
				//
				// }
				// if(i>0)
				// {
				// if(Form.getDestClientBranch().equalsIgnoreCase(WebbookingFormverify.getDestClientBranch()))
				// {
				// PreparedStatement
				// pst=con.prepareStatement("insert into web_bach_ship_srvc values(?,?,?,?,?,?,?,?,?,?,?)");
				// pst.setString(1,nameFile);
				// pst.setString(2,lineNumber);
				// pst.setString(3,null);
				// pst.setString(4,null);
				// pst.setString(5,null);
				// pst.setString(6,null);
				// pst.setString(7,Form.getShipQuantity());
				// pst.setString(8,Form.getShipDescription());
				// pst.setString(9,Form.getShipContent());
				// pst.setString(10,Form.getShipWeight());
				// pst.setString(11,Form.getShipVolume());
				// result=pst.executeUpdate();
				// pst.close();
				//
				//
				// }

				// else
				// {

				PreparedStatement pst = con
						.prepareStatement("insert into web_bach_guia values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				pst.setString(1, nameFile);
				pst.setString(2, Form.getRowNum());
				pst.setString(3, clientId);
				pst.setString(4, Form.getNumberDePedimento());
				pst.setString(5, Form.getAgentsAduanal());
				pst.setString(6, Form.getDestClient());
				pst.setString(7, Form.getDestClientBranch());
				pst.setString(8, Form.getDestClientName());
				pst.setString(9, Form.getDestStreet());
				pst.setString(10, Form.getDestDoorNo());
				pst.setString(11, Form.getDestColony());
				pst.setString(12, Form.getDestZipCode());
				pst.setString(13, Form.getDestClientCity());
				pst.setString(14, Form.getDestClientPhone());
				pst.setString(15, Form.getServicesDeliveryType());
				pst.setString(16, Form.getServicesAck());
				pst.setString(17, Form.getServicesCodValue());
				pst.setString(18, Form.getServicesDeclaredValue());
				pst.setString(19, Form.getServicesCommentaries());
				pst.setString(20, Form.getServicesReference());
				pst.setString(21, null);
				pst.setString(22, null);
				pst.setString(23, null);
				pst.setString(24, null);
				pst.setString(25, null);
				pst.setString(26, null);
				pst.setString(27, null);
				pst.setString(28, "N");
				pst.setString(29, "N");
				pst.setString(30, Form.getSeqNumber());
				lineNumber = Form.getRowNum();

				result = pst.executeUpdate();
				pst.close();
				pst = con
						.prepareStatement("insert into web_bach_ship_srvc values(?,?,?,?,?,?,?,?,?,?,?)");
				pst.setString(1, nameFile);
				pst.setString(2, lineNumber);
				pst.setString(3, null);
				pst.setString(4, null);
				pst.setString(5, null);
				pst.setString(6, null);
				pst.setString(7, Form.getShipQuantity());
				pst.setString(8, Form.getShipDescription());
				pst.setString(9, Form.getShipContent());
				pst.setString(10, Form.getShipWeight());
				pst.setString(11, Form.getShipVolume());
				result = pst.executeUpdate();
				pst.close();
			}
			// }

			// }
			try {
				PreparedStatement pst = con
						.prepareStatement("insert into web_bach_guia_chck  values(?,?,?,?,?,?,?,?,?)");
				pst.setString(1, nameFile);
				pst.setString(2, null);
				pst.setString(3, clientId);
				pst.setString(4, "N");
				pst.setString(5, "N");
				pst.setString(6, null);
				pst.setString(7, null);
				pst.setString(8, null);
				pst.setString(9, null);
				pst.executeUpdate();
				con.commit();
			} catch (Exception e) {
				request.setAttribute("errormsgprint", "Problem in Commit");
				AccessLog.Log(e.getMessage());
				con.rollback();
			}

		} catch (SQLException e) {
			AccessLog.Log("Cannot create Prepared statement" + e.getMessage());

			if (e.getErrorCode() == 1400) {
				request.setAttribute("errormsgprint",
						"Problem in Insertion ,Do not give Null Values..");
			} else if (e.getErrorCode() == 1) {
				request.setAttribute("errormsgprint", "File Already Exists ..");
			} else if (e.getErrorCode() == 1722) {
				request.setAttribute("errormsgprint",
						"Problem in Insertion,Invalid Number ..");

			} else

				request.setAttribute("errormsgprint", "Problem in Insertion");
		}
		return "success";
	}

	public String readGuiaContent_WriteFile(HttpServletRequest request,
			Connection con, String clientId) {
		java.sql.CallableStatement cst = null;

		HttpSession session = request.getSession(false);
		try {
			PreparedStatement pst = con
					.prepareStatement("select BC_FILE_NAME from web_bach_guia_chck where bc_clnt_id = ? and BC_PRCS_FLAG = 'Y' and BC_PRNT_FLAG = 'N'");
			pst.setString(1, clientId);
			ResultSet rs1 = pst.executeQuery();
			String fil = "";
			while (rs1.next()) {
				ArrayList values = new ArrayList();
				PreparedStatement pst1 = con
						.prepareStatement("select BG_PRINT_FILE from web_bach_guia where bg_clnt_id = ? and BG_FILE_NAME =?");
				pst1.setString(1, clientId);
				fil = rs1.getString(1);
				// AAP//System.out.println("filename"+fil);

				pst1.setString(2, fil);

				ResultSet rs = pst1.executeQuery();
				while (rs.next()) {
					// AAP//System.out.println("inside the while loop");
					String value = rs.getString(1);
					// AAP//System.out.println("printing the file"+value);
					if ((value == null) || (value == "")
							|| (value.length() < 0)) {
						// request.setAttribute("errormsgprint","Guia Is Not Available To Print");
						continue;
					}
					StringBuffer buffer = new StringBuffer(value);

					values.add(buffer);
				}
				if (values.size() > 0) {
					// AAP//AccessLog.Log("values size"+values.size());
					writeToGuiaFile(request, values, getTimeStamp(con)
							.getTime());
					if (rs != null)
						rs.close();
					rs1.close();
					pst.close();
					pst1.close();
					return "success";
				}

				// select bg_print_file from web_bach_guia where
				// nvl(bg_prnt_flag,'N')='N' and bg_orgn_clnt_id="+clientId );
				else {
					if (rs != null)
						rs.close();
					pst.close();
					rs1.close();
					pst.close();

					request.setAttribute("errormsgprint",
							"Guia Is Not Available To Print");
					return "thispage";
				}

				// PreparedStatement
				// pst=con.prepareStatement("update web_bach_guia set bg_prnt_flag= ? where bg_orgn_clnt_id=?");
				// pst.setString(1,"Y");
				// pst.setString(2,clientId);
				// pst.executeUpdate();
				// pst.close();
			}

			{
				if (rs1 != null)
					rs1.close();
				pst.close();
				request.setAttribute("errormsgprint",
						"Guia Is Not Available To Print");
				return "thispage";

			}

		} catch (Exception e) {
			AccessLog
					.Log("Error while read content from db and write it into file"
							+ e.getMessage());
			e.printStackTrace();
			return "thispage";
		}

	}

	public java.sql.Timestamp getTimeStamp(Connection con) throws Exception {
		PreparedStatement pst = con
				.prepareStatement("select sysdate from dual");// Checked
		ResultSet rs = pst.executeQuery();
		java.sql.Timestamp sysTimeStamp = null;

		if (rs.next())
			// sysDate = rs.getString(1);
			sysTime = rs.getTime(1);
		sysTimeStamp = rs.getTimestamp(1);

		if (rs != null)
			rs.close();
		if (pst != null)
			pst.close();
		return sysTimeStamp;
	}

	public String saveFile(JavFileUploadForm form, HttpServletRequest request,
			Connection con) {
		HttpSession session = request.getSession(false);

		Global glo = (Global) session.getAttribute("sGlobal");

		FormFile myFile = form.getTheFile();
		AccessLog.Log("myFile " + myFile);
		if (myFile == null)
			return "UploadPage";
		String filname = "";
		filname = myFile.getFileName();

		filname = filname.substring(0, filname.indexOf('-'));
		System.out.println(filname);
		System.out.println(glo.clientId);

		if (!filname.equalsIgnoreCase(glo.clientId)) {
			request.setAttribute("errormsgprint", "The File Name is not valid");
			return "UploadPage";
		}
		// filname="";
		// filname=filname.substring((filname.indexOf(".")-1),2);
		// System.out.println("Extension"+filname);
		//

		byte[] fileData = null;

		try {
			fileData = myFile.getFileData();
		} catch (FileNotFoundException e1) {
			AccessLog.Log("File Not Found" + e1.getMessage());

		} catch (IOException e1) {
			AccessLog.Log("Input Output Error" + e1.getMessage());
		}
		File f = new File(request.getRealPath("/") + File.separator
				+ "Guiaprint1");
		if (!f.exists()) {
			f.mkdirs();
		}
		BufferedOutputStream buff = null;
		try {

			long syDate;
			byte date;
			String file = "";

			java.sql.Timestamp sysDate = getTimeStamp(con);
			syDate = sysDate.getTime();
			Long dat = new Long(syDate);
			date = dat.byteValue();
			file = f.toString() + File.separator + syDate + ".txt";
			buff = new BufferedOutputStream(
					new FileOutputStream(f.toString() + File.separator + syDate
							+ ".txt"));
			buff.write(fileData);

			buff.flush();
			buff.close(); // /Writing text file is over
			buff = null;
			fileName = syDate + ".txt";
		} catch (Exception e) {
			AccessLog.Log("Data Processing Error in Extract Method"
					+ e.getMessage());
		} finally {
			try {
				if (buff != null) {
					buff.flush();
					buff.close();
				}
			} catch (Exception e) {
				AccessLog.Log("saveFile()Exception_Error:"+e);
				e.printStackTrace();
			}
		}
		return "success";
	}

	public void writeToGuiaFile(HttpServletRequest request, ArrayList list,
			long sysdate) throws Exception {

		//Hashtable table = new Hashtable();
		HttpSession session = request.getSession(false);
		Global global = (Global) session.getAttribute("sGlobal");
		File f = new File(request.getRealPath("/") + File.separator
				+ "Guiaprint");
		if (!f.exists()) {
			f.mkdirs();
		}

		// below code added by E.Sundar on 21/10/2003 to avoid data mixing
		// hassle
		String clientId = global.clientId;
		// below code commented by E.Sundar on 21/10/2003
		// String filename = "BRPETIQ_"+sysdate+".ETI";
		String filename = "BRPETIQ_" + sysdate + clientId + ".ETI";
		// System.out.println("GUIA PRINT FILE NAME IN ACTION "+filename);//commented
		// and below added by B.Emerson on 30/04/2004
		AccessLog.Log("GUIA PRINT FILE NAME IN ACTION " + filename);
		// BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new
		// FileOutputStream(exportdirectory+File.separator+"BRPETIQ"+global.clientId+".ETI")));
		BufferedWriter out = null;
		LineNumberReader ln = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(f.toString() + File.separator + filename)));
			String data = null;
			int gone = 0;
			for (int counting = 0; counting < list.size(); counting++) {
				data = ((StringBuffer) list.get(counting)).toString();
				ln = new LineNumberReader(new StringReader(data));
				if ((counting > 0) && (gone == 1)) {
					out.write("******************************************************************************************");
					out.newLine();
				}
				while ((data = ln.readLine()) != null) {
					out.write(data);
					out.newLine();
					gone = 1;
				}
				ln.close();
				ln = null;
			}
			out.flush();
			out.close();
			out = null;
		} catch (Exception ex) {
			AccessLog.Log("writeToGuiaFile:" + ex.getMessage());
		} finally {
			try {
				if (ln != null) {
					ln.close();
				}
			} catch (Exception e) {
				AccessLog.Log("saveFile()Exception_Error1:"+e);
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				AccessLog.Log("saveFile()Exception_Error:"+e);
				e.printStackTrace();
			}
		}
		global.printfilename = filename;
		session.setAttribute("sGlobal", global);

	}

	public void writeToLogFile(HttpServletRequest request, ArrayList list,
			long sysdate) throws Exception {

		//Hashtable table = new Hashtable();
		HttpSession session = request.getSession(false);
		Global global = (Global) session.getAttribute("sGlobal");
		File f = new File(request.getRealPath("/") + File.separator
				+ "Guiaprint");
		if (!f.exists()) {
			f.mkdirs();
		}

		// below code added by E.Sundar on 21/10/2003 to avoid data mixing
		// hassle
		String clientId = global.clientId;
		// below code commented by E.Sundar on 21/10/2003
		// String filename = "BRPETIQ_"+sysdate+".ETI";
		String filename = "BRPETIQ_" + sysdate + clientId + "log" + ".ETI";
		// System.out.println("GUIA PRINT FILE NAME IN ACTION "+filename);//commented
		// and below added by B.Emerson on 30/04/2004
		AccessLog.Log("GUIA PRINT FILE NAME IN ACTION " + filename);
		// BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new
		// FileOutputStream(exportdirectory+File.separator+"BRPETIQ"+global.clientId+".ETI")));
		BufferedWriter out = null;
		LineNumberReader ln = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(f.toString() + File.separator + filename)));
			String data = null;
			for (int counting = 0; counting < list.size(); counting++) {
				data = ((StringBuffer) list.get(counting)).toString();
				ln = new LineNumberReader(new StringReader(data));
				if (counting > 01)
					out.write("******************************************************************************************");
				while ((data = ln.readLine()) != null) {
					out.write(data);
					out.newLine();
				}
				ln.close();
				ln = null;
			}
			out.flush();
			out.close();
			out = null;
		} catch (Exception ex) {
			AccessLog.Log("writeToGuiaFile:" + ex.getMessage());
		} finally {
			try {
				if (ln != null) {
					ln.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeToLogFile()Exception_Error1:"+e);
				e.printStackTrace();
			}
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				AccessLog.Log("writeToLogFile()Exception_Error:"+e);
				e.printStackTrace();
			}
		}
		global.printlogfilename = filename;
		session.setAttribute("sGlobal", global);

	}

}
