
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import bean.ConsultaParametros;
import bean.Global;
import bean.JavCatProducts;
import bean.JavCatalogoClientes;
import bean.JavTariff;
import beanUtil.ConnectDB;
import logger.AccessLog;


public class LovAction extends Action{
	public LovAction(){
		//AAP//AccessLog.Log("CALLING LOV ACTION ");
	}
	@SuppressWarnings("rawtypes")
	public ActionForward perform(ActionMapping map,ActionForm form,HttpServletRequest request,HttpServletResponse response){
		String returnvalue="thispage";
		Connection con = null;

		try{
			HttpSession session = request.getSession(true);
			con = ConnectDB.getConnection();
			String redirect = request.getParameter("type");
			//AAP//AccessLog.Log("redirect :-"+redirect);
			String branchId = (String)session.getAttribute("sAssignedBranch");
			String siteId=(String)session.getAttribute("sSiteId");
			String clientId = (String)session.getAttribute("sClientId");
			
			//For Nosession
			if (clientId==null) {
				return map.findForward("nosession");
			}

			//added by Murugesapandian 18/05/06 related Destionation Client Entry
			//to get current destination branch id
			String currentDest = request.getParameter("sucursal");

			//added by Murugesapandian 18/05/06 related Destionation Client Entry

			//To get Postal related things 
			String postalLevel = request.getParameter("postalLevel");
			String postalCode = request.getParameter("postalCode");
			String postalType = request.getParameter("postalType");
			String desBranchId = request.getParameter("destinationsitecode");//added by B.Emerson on 20/06/2003
			
			//For Destination Address LOV
			
			String addressType = request.getParameter("addresstype");
			//AAP//AccessLog.Log(" Address Type (Class-LovAction) ---"+ addressType );
			String destinationClientId = request.getParameter("destClId");
			//AAP//AccessLog.Log(" destinationClientId (Class-LovAction) ---"+ destinationClientId );
			destinationClientId = (destinationClientId!=null?destinationClientId:"");
			
			//For Tariff LOV
			String serviceId = request.getParameter("serviceid");
			
			//AAP//AccessLog.Log("TARIFF LOV SERVICE ID "+serviceId);		

			//Added by Sam[23-06-2006] , For fixing duplicating Additional Services						
			Global global = (Global)session.getAttribute("sGlobal");				
			String destinationBranchId = null;
			if(global!=null){
				destinationBranchId = global.destinationBranchId ;
			}
			
			if(redirect!=null && redirect.trim().equalsIgnoreCase("branch")){
				JavBranchRecords branchRecords = new JavBranchRecords();
				//AAP//System.out.println(branchId+"branchId");
				ArrayList records = branchRecords.getLovRecords(con,clientId);//passed client id - by B.Emerson on 20/06/2003
				request.setAttribute("branchRecords",records);
				//return map.findForward("branch");
				returnvalue="branch";
			} else if(redirect!=null && redirect.trim().equalsIgnoreCase("branchaddress")){
				JavBranchAddress branchAddress = new JavBranchAddress();
				ArrayList records = branchAddress.getLovBranchAddressRecords(con,currentDest);//passed client id - by B.Emerson on 20/06/2003
				request.setAttribute("branchAddress",records);
				//return map.findForward("branch");
				returnvalue="branchAddress";
			} else if(redirect!=null && redirect.equalsIgnoreCase("client")){
				JavClientRecords clientRecords = new JavClientRecords();
				ArrayList records  = clientRecords.getLovRecords(con,clientId,desBranchId);//passed destination branch id - by B.Emerson on 20/06/2003
				request.setAttribute("clientRecords",records);
				//return map.findForward("client");
				returnvalue="client";
			} else if(redirect!=null && redirect.equalsIgnoreCase("clientNew")){ //AAP01
				String clntName = request.getParameter("destNombre");
				String destId = request.getParameter("destClave");
				String addressType1 = "DC";
				JavCatalogoClientes destinationAddressRecordsNew = new JavCatalogoClientes();
				ArrayList recordsDestAdd = destinationAddressRecordsNew.getLovRecordsNew(con,addressType1,destId,clntName,siteId,session,global);
				//AAP//AccessLog.Log(" from LovAction get records " + addressType  + destinationClientId   + branchId + con);
				//AAP//AccessLog.Log(" DestinationAddressNew from LOVACTION  "+recordsDestAdd);
				request.setAttribute("destnationAddressRecords",recordsDestAdd);
				returnvalue="clientNew";
			} else if(redirect!=null && (redirect.equalsIgnoreCase("destinationaddress")||redirect.equalsIgnoreCase("destinationaddressPP"))){
				if (redirect.equalsIgnoreCase("destinationaddressPP")) {
					siteId = (String)session.getAttribute("siteid");
					desBranchId = request.getParameter("destsite");
					clientId = request.getParameter("clientId");	
				}
				
				JavDestinationAddressRecords destinationAddressRecords = new JavDestinationAddressRecords();
				ArrayList records = destinationAddressRecords.getLovRecords(con,addressType,destinationClientId,siteId,session,desBranchId);
				//AAP//AccessLog.Log( " from LovAction get records " + addressType  + destinationClientId   + branchId + con); 
				request.setAttribute("destnationAddressRecords",records);
				returnvalue = redirect;
			} else if(redirect != null && redirect.equalsIgnoreCase("clientaddress")){
				JavDestinationAddressRecords clientAddress = new JavDestinationAddressRecords();
				//AAP//AccessLog.Log("destSiteId in lov"+desBranchId);
				ArrayList records = clientAddress.getLovRecords(con,addressType,clientId,siteId,session,siteId);
				request.setAttribute("clientAddressRecords",records);
				returnvalue="clientaddress";
			} else if(redirect !=null && redirect.equalsIgnoreCase("fiscaladdress")){
				JavFiscalAddressRecords fiscalAddress = new JavFiscalAddressRecords();
				ArrayList records = fiscalAddress.getLovRecords(con,clientId);
				request.setAttribute("fiscalAddressRecords",records);
				returnvalue="fiscaladdress";
			} else if(redirect !=null && redirect.equalsIgnoreCase("description")){
				//System.out.println("clientId "+clientId);
				JavDescription description = new JavDescription();
				ArrayList records = description.getLovRecords(con,/*clientId*/ global.getClientIdAgreement());
				request.setAttribute("descriptionRecords",records);
				returnvalue="description";
			}
			//added by Murugesapandian 17/05/06 related Destionation Client Entry
			else if(redirect !=null && redirect.equalsIgnoreCase("clientType")){
				//JavClientTypeRecords clientTypeRecords = new JavClientTypeRecords();
				JavClientTypeRecords clientTypeRecords = new JavClientTypeRecords();
				ArrayList records = clientTypeRecords.getLovRecords(con);
				request.setAttribute("clientTypeRecords",records);
				//System.out.println("LOV  Action Client Type :  "+redirect);
				returnvalue="clientType";
			}
			//added by Murugesapandian 18/05/06 related Destionation Client Entry
			else if(redirect !=null && redirect.equalsIgnoreCase("assignedToBranch")){
				JavAssignedToBranchRecords assignedToBranchRecords = new JavAssignedToBranchRecords();
				ArrayList records = assignedToBranchRecords.getLovRecords(con);
				request.setAttribute("assignedToBranchRecords",records);
				//System.out.println("LOV  Action Client Type :  "+redirect);
				returnvalue="assignedToBranch";
			}
			//added by Murugesapandian 18/05/06 related Destionation Client Entry
			else if(redirect !=null && redirect.equalsIgnoreCase("destBranch")){
				JavDestBranchRecords destBranchRecords = new JavDestBranchRecords();
				ArrayList records = destBranchRecords.getLovRecords(con,/*clientId*/ global.getClientIdAgreement());
				request.setAttribute("destBranchRecords",records);
				//System.out.println("LOV  Action Client Type :  "+redirect);
				//System.out.println("Client Id   :  "+clientId);
				returnvalue="destBranch";
			}
			//added by Murugesapandian 18/05/06 related Destionation Client Entry
			else if(redirect !=null && redirect.equalsIgnoreCase("cities")){
					JavCitiesRecords citiesRecords = new JavCitiesRecords();
					//System.out.println("Scssssssssssssssss ===> : "+currentDest);
					ArrayList records = citiesRecords.getLovRecords(con,currentDest);
					request.setAttribute("citiesRecords",records);
					returnvalue="cities";
			}
			//added by Murugesapandian 18/05/06 related Destionation Client Entry
			else if(redirect !=null && redirect.equalsIgnoreCase("postal")){
					//AAP//AccessLog.Log("postalLevel,postalCode,postalType"+postalLevel+""+""+postalCode+""+postalType);
					JavPostalRecords postalRecords = new JavPostalRecords();
					ArrayList records = postalRecords.getLovRecords(con,postalLevel,postalCode,postalType);
					request.setAttribute("postalRecords",records);
					returnvalue="postal";
			}

			else if(redirect !=null && redirect.equalsIgnoreCase("tariff")){
				String clasifTarif = request.getParameter("clasifTarif");
				String defaultservicescreenKm = request.getParameter("defaultservicescreenKm")== null ? "N" : request.getParameter("defaultservicescreenKm");//AAPXX
				String kmDist = global.getKmTarifType();//AAPXX
				JavTariff tariff = new JavTariff();
				ArrayList records = null;
				HashMap<String, String> pesoVolConvenio = null;
				
				if (clasifTarif == null || clasifTarif.equals("0")){
					records = tariff.getLovRecords(con,serviceId);	
				} else {
					if (defaultservicescreenKm.equals("Y")) {//AAPXX
						records = tariff.getLovNewTariffKm(con,serviceId, global.getClientIdAgreement(),kmDist);
					} else {//AAPXX
						records = tariff.getLovNewTariff(con,serviceId, global.getClientIdAgreement(),desBranchId, global.getAssignedSite());	
					}
					
					pesoVolConvenio = tariff.getPesoVolConvenio(con, global.getClientIdAgreement());
					
					if (pesoVolConvenio!=null) {						
						request.setAttribute("pesoConvenio",pesoVolConvenio.get("pesoConvenio").toString());
						request.setAttribute("voluConvenio",pesoVolConvenio.get("voluConvenio").toString());
					}
				}
				
				request.setAttribute("tariffRecords",records);				
				if(serviceId!=null && serviceId.equalsIgnoreCase("SHP-E"))
					returnvalue="tariffenvelope";
				else if(serviceId!=null && serviceId.equalsIgnoreCase("SHP-G"))
					returnvalue="tariffpackets";
			}
			//Code  Added  By palanivel R for AdditionalService LOV
			else if(redirect !=null && redirect.equalsIgnoreCase("service")){
				String defaultservicescreenKm = request.getParameter("defaultservicescreenKm")== null ? "N" : request.getParameter("defaultservicescreenKm");//AAPXX
				String kmDist = global.getKmTarifType();//AAPXX
				JavServiceRecords service = new JavServiceRecords();
				ArrayList records = null;
				if (defaultservicescreenKm.equals("Y")) {//AAPXX
					records = service.getLovRecordsKm(con, global.getClientIdAgreement(),branchId,kmDist);
				} else {//AAPXX
					records = service.getLovRecords(con, global.getClientIdAgreement(),branchId,destinationBranchId);
				}
				request.setAttribute("serviceRecords",records);
				returnvalue="service";
			} else if(redirect !=null && redirect.equalsIgnoreCase("regClientDest")) {
				String nombreDest = request.getParameter("nombreDest");
				JavCatalogoClientes destinationAddressRecordsNew = new JavCatalogoClientes();
				
				//ArrayList recordsDestAdd = destinationAddressRecordsNew.getListaClientesDestino(con, clientId, nombreDest.toUpperCase());
				ArrayList recordsDestAdd = destinationAddressRecordsNew.getListaClientesDestino(con, global/*AAP18*/, nombreDest.toUpperCase());
				ArrayList addressList = (ArrayList) recordsDestAdd;
				if (!recordsDestAdd.isEmpty() && ((ArrayList)addressList.get(0)).get(0).toString().contains(("ERROR"))) {
					request.setAttribute("errorMessages", ((ArrayList)addressList.get(0)).get(1).toString());
				}
				request.setAttribute("clientesDest",recordsDestAdd);
				returnvalue="regClientDest";
			} else if (redirect != null && redirect.equalsIgnoreCase("sipweborginaddress")) {
				siteId=(String)session.getAttribute("siteid");
				clientId = request.getParameter("clientId");
				JavFiscalAddressRecords fiscalAddress = new JavFiscalAddressRecords();
				ArrayList records = fiscalAddress.getClientrecords(con, siteId, clientId);

				request.setAttribute("fiscalAddressRecords", records);
				String rfc = getRFC(con, clientId);
				request.setAttribute("clientrfc", rfc);
				returnvalue = "sipweborginaddress";
			} else if (redirect != null && redirect.equalsIgnoreCase("sipwebclientonly")) {				

				JavClientRecords clientAddress = new JavClientRecords();
				// for selected site
				
				siteId = (String)session.getAttribute("siteid");
				clientId = request.getParameter("clientId");
				String clientName = request.getParameter("clientname");
				if (clientName == null)
					clientName = "";
				String groupClientId = ((Global)session.getAttribute("sGlobal")).getGroupClientId();
				
				ArrayList records = clientAddress.getClientrecordsOnly(con, siteId, clientName, clientId, groupClientId);

				request.setAttribute("clientRecords", records);
				returnvalue = "sipwebclientonly";
			} if(redirect !=null && redirect.equalsIgnoreCase("sipwebfiscaladdress")) {
				siteId=(String)session.getAttribute("siteid");
				clientId = request.getParameter("clientId");
				JavFiscalAddressRecords fiscalAddress = new JavFiscalAddressRecords();
				ArrayList records = fiscalAddress.getClientrecords(con,siteId,clientId);
			
				request.setAttribute("fiscalAddressRecords",records);
				//AccessLog.Log("after fetch");
			//	String rfc=getRFC(con,clientId);
				//request.setAttribute("clientrfc",rfc);
				returnvalue="sipwebfiscaladdress";
			} if( redirect!=null && (redirect.trim().equalsIgnoreCase("branchPP")/*ppg web*/ || redirect.trim().equalsIgnoreCase("brncAsig") /*monitoreo ppg web*/)) {
				JavBranchRecords branchRecords = new JavBranchRecords();
				String siteid="";
				String sitename="";
				siteid=request.getParameter("destsite");
				sitename=request.getParameter("destsitename");
				ArrayList records = branchRecords.getLovRecords(con,clientId,siteid,sitename);
				request.setAttribute("branchRecords",records);
				//return map.findForward("branch");
				//returnvalue="branchPP";
				returnvalue = redirect;
			} else if (redirect != null && redirect.equalsIgnoreCase("clientwithsite")) {
				JavClientRecords clientAddress = new JavClientRecords();
				// for selected site
				String site = "";
				if (request.getParameter("siteId") != null)
					site = request.getParameter("siteId");
				String destinationclientname = "";
				destinationclientname = request.getParameter("destinationclientname");
				if (request.getParameter("destinationclient")!=null) {
					destinationClientId = request.getParameter("destinationclient").toString();
				}					 
				//ArrayList records = clientAddress.getClientrecordsOnly(con, site, destinationclientname, destinationClientId);
				ArrayList records = clientAddress.getClientDestRecordsOnly(con, site, destinationclientname, destinationClientId, /*clientId*/global.getClientIdAgreement(), global.getOrigenUserClave());
				
				request.setAttribute("clientRecords", records);
				returnvalue = "clientPP";
			} else if (redirect != null && redirect.equalsIgnoreCase("clntAsig")) {//monitoreo prepago
				JavClientRecords clientAddress = new JavClientRecords();
				String opcSelAsig = request.getParameter("opcSelAsig");
			
				String asigClientName = request.getParameter("asigClientName");
				
				//System.out.println("opcSelAsig "+opcSelAsig);
				ArrayList records = null;
				if (opcSelAsig.equals("cte")){
					// para clientes terceros
					
					String site = "";
					if (request.getParameter("siteId") != null)
						site = request.getParameter("siteId");
						
					asigClientName = request.getParameter("asigClientName");
					//System.out.println("asigClientName "+asigClientName);
					if (asigClientName != null ) {
						records = clientAddress.getClientDestRecordsOnly(con, site, asigClientName, destinationClientId, clientId, global.getOrigenUserClave());	
					}
				} else {					
					// para clientes del grupo
					String groupClientId = ((Global)session.getAttribute("sGlobal")).getGroupClientId();
					siteId = (String)session.getAttribute("siteid");
					clientId = request.getParameter("clientId");
					asigClientName = request.getParameter("asigClientName");					
					
					if (clientId != null || asigClientName != null ){
						records = clientAddress.getClientGpoAsig(con, siteId, asigClientName, clientId, groupClientId);
					}
				}
				
				if (records == null) {
					records = new ArrayList(0);
				}
				request.setAttribute("clientRecords", records);
				returnvalue = redirect;				
			} else if (redirect != null && redirect.equalsIgnoreCase("userAsig")) {//monitoreo prepago
				JavClientRecords clientRecords = new JavClientRecords();
				String cveClienteAsig = request.getParameter("cveClienteAsig") == null ? "" : request.getParameter("cveClienteAsig");
				String asigUserName = request.getParameter("asigUserName") == null ? "" : request.getParameter("asigUserName");
				
				ArrayList records  = clientRecords.getUserClientAsig(con, cveClienteAsig, asigUserName);
				request.setAttribute("clientRecords",records);
			
				returnvalue = redirect;
			} else if (redirect != null && redirect.equalsIgnoreCase("catProducts")) { //Complemento Carta Porte
				JavCatProducts catProducts = new JavCatProducts();
				String clntDestId = request.getParameter("clntDestId");
				ArrayList records = null;
				
				if (clntDestId != null && !clntDestId.equals("")) {
					records = catProducts.getLovRecords(con, clntDestId, request.getParameter("contenido"));
				}else {
					records = catProducts.getLovRecords(con, global.getClientIdAgreement(), request.getParameter("contenido"));
				}
				
				request.setAttribute("catProductsRecords", records);
				returnvalue = redirect;
			} else if (redirect != null && redirect.equalsIgnoreCase("regimFiscal")) { //CFDI 4.0 Obtener regimen fiscal
				ConsultaParametros regimFiscal = new ConsultaParametros();

				String rfc = request.getParameter("rfc");
				ArrayList records = new ArrayList();
				String modul = "FIN";
				
				if (rfc.equalsIgnoreCase("XAXX010101000")) {
					records = regimFiscal.QryMdulType(con, modul, "REGFI_UCFDI_GEN");
				}else {
					if (rfc.length() == 12) {
						records = regimFiscal.QryMdulTypeVlue(con, modul, "REGIM_FISCAL", "vlue2", "1");
					}else {
						records = regimFiscal.QryMdulTypeVlue(con, modul, "REGIM_FISCAL", "vlue1", "1");
					}
				}
				
				request.setAttribute("regimFiscalRecords", records);
				returnvalue = redirect;
			} else if (redirect != null && redirect.equalsIgnoreCase("usoCfdi")) { //CFDI 4.0 Obtener Uso CFDI
				ConsultaParametros usoCfdi = new ConsultaParametros();

				String rfc = request.getParameter("rfc");
				String regimFiscalId = request.getParameter("regimFiscalId");
				ArrayList records = new ArrayList();
				String modul = "FIN";
				
				records = usoCfdi.QryMdulTypeVlue(con, modul, "USO_CFDI", "parm2", regimFiscalId);
				
				request.setAttribute("usoCfdiRecords", records);
				returnvalue = redirect;
			} else if (redirect != null && redirect.equalsIgnoreCase("datosFiscalesForm")) { //CFDI 4.0 Obtener Uso CFDI
				ConsultaParametros usoCfdi = new ConsultaParametros();
				returnvalue = redirect;
			} else if (redirect != null && redirect.equalsIgnoreCase("cliententry")) { //Registro de clientes destino
				returnvalue = redirect;
			}
		}
		catch(Exception e) {
			AccessLog.Log(e);
		} finally {
			try {
				if(con != null) {
					con.close();
				}
			} catch(Exception e) {
				AccessLog.Log( "IGNORE THE FOLLOWING EXCEPTION:");
				AccessLog.Log(e);
			}
		}
		return map.findForward(returnvalue);
	}
	
	private String getRFC(Connection con,String clientId) {
		 PreparedStatement psmt = null;
		  ResultSet rs =null;
		 String query = null;
		 String rfc="";
		 //AccessLog.Log("Entered in to get RFC of logAction.java");
		 query="select PACK_WEB.fun_ftch_rfc(?)  FROM WEB_CLNT_MSTR where wc_clnt_id=?";
		 try{
		  psmt = con.prepareStatement(query);
		  psmt.setString(1,clientId);
		  psmt.setString(2,clientId);
	        rs = psmt.executeQuery();
	        while(rs.next())
	        {
	        	rfc=rs.getString(1);
	        	if(rfc==null){
	        		rfc="";
	        		//AccessLog.Log("rfc ==="+rfc);
	        	}
	        }
	        //AccessLog.Log("after whl in LogA");
	        
		 } catch(SQLException e) {
			 AccessLog.Log(e.getMessage());
		 } finally{
			try{
			 if(psmt!=null)
				 psmt.close();
			 if(rs!=null)
				 rs.close();
			} catch(SQLException e) {
				AccessLog.Log(e.getMessage());
			}
		 }		 
		return rfc;
	}
}