<%--
Authur		:	Ramachandran.V
Date		:	25-March-2003

FileName	:	JavWebBookingServicesAditional.jsp
FormBean	:	JavWebBookingServicesAditionalForm.class
ActionBean	:	JavWebBookingServicesAditionalAction.class
OtherClasses	:	none
CSS FileName	:	webbooking.css
--%>

<%@ page info="WebBooking - ServicesAditional"%>
<%-- Global added by palanivel 

--%>
<%@ page import="java.util.ArrayList,java.util.Hashtable,java.util.Set,java.util.Iterator,bean.Global,bean.Services,bean.AdditionalService,logger.AccessLog, beanForm.JavWebBookingGeneralMainForm"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html>

<head>
<title>Services</title>
<link rel="stylesheet" href="webbooking.css" type="text/css">
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/v2/global.js"></script>
</head>

<!-- body marginwidth="0" marginheight="0" topmargin=0 leftmargin=0 background="images/bg.gif" oncontextmenu="return false"-->
	<script type="text/javascript">

<%
	
	Global global = (Global) session.getAttribute("sGlobal");
	JavWebBookingGeneralMainForm aform = (JavWebBookingGeneralMainForm) session.getAttribute("webBookinggeneralMain");
%>
	function callServices() {
		document.getElementById('PantAdicionales').style.visibility='hidden';
		document.getElementById('PantAdicionales').style.position='absolute';
		//habilidad el boton servicios adicionales
		//document.forms[0].servicios.disabled = false;
		submitFormMainServices('calculate','Espere por favor... Realizando cálculo de envío');
	}
	</script>
	<!-- form name=frm-->
		<table width="772" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="77%"><b>Servicios Adicionales </b></td>
			</tr>
			<tr>
				<td width="77%">
					<table width="772" border="0" cellspacing="0" cellpadding="0"   >
						<tr>
							<td height="160">
								<table align=center border="0" cellpadding="0" cellspacing="0" width="97%">
									<tr>
										<td width="71%">&nbsp;</td>
									</tr>
									<tr>
										<td width="71%">
											<table  align=center border="0" cellpadding="0" cellspacing="1" width="97%">
												<tr>
													<td width="34%" height="23">&nbsp;Descripcion</td>
													<td width="21%" height="23">
														<p align="right">Sub-total</p>
													</td>
													<td width="17%" height="23">
														<p align="right">Descuento</p>
													</td>
													<td width="28%" height="23">
														<p align="right">Total</p>
													</td>
												</tr>
												<%
													ArrayList servicesList = (ArrayList) session.getAttribute("calculatedservicelist");
													String gsSubTotl = "";
													String gsDisc = "";
													String gsTotal = "";
													/*Available Services Object..
													1.shipE	2.shipG	3.rad	4.ead	5.ack	6.cod	7.codr	8.inv 9.Add 
													 */
													Hashtable servicesTable = null;
													ArrayList listOfservices = null;
													Hashtable serv_new = null;
													Set set_new = null;
													Iterator new_iterator = null;
													String key = null;
													Services servicesTemp = null;
													AdditionalService serviceRecordsRecs = null;
													ArrayList additionalServicesArray = null;
													int size = 0;
													if (servicesList!=null) {
														size = servicesList.size();
													}
													for (int i = 0; i < size; i++) {
														servicesTable = (Hashtable) servicesList.get(i);
														if (servicesTable.get("newservice") != null) {
															listOfservices = (ArrayList) servicesTable.get("newservice");
															for (int index = 0; index < listOfservices.size(); index++) {
																//AccessLog.Log(global.clientId + "INSIDE RAD");
																serv_new = (Hashtable) listOfservices.get(index);
																set_new = serv_new.keySet();
																new_iterator = set_new.iterator();
																for (; new_iterator.hasNext();) {
																	key = (String) new_iterator.next();
																	servicesTemp = (Services) serv_new.get(key);
												%>
												<tr>
													<td width="34%" class="td" height="21"><%=servicesTemp.descriptionAditional%></td>
													<td width="21%" class="td" height="21" align="right">$<%=servicesTemp.GS_SUB_TOTL%></td>
													<td width="17%" class="td" height="21" align="right">$<%=servicesTemp.GS_DISC%></td>
													<td width="28%" class="td" height="21" align="right">$<%=servicesTemp.TOTAL%></td>
												</tr>
												<%
																}

															}

														} else if ((Services) servicesTable.get("rad") != null) {
															servicesTemp = (Services) servicesTable.get("rad");
															if (servicesTemp.getMostrarDescuentos().equals("N")) {
																gsSubTotl = servicesTemp.TOTAL;
																gsDisc = "0.00";
																gsTotal = servicesTemp.TOTAL;
															} else {
																gsSubTotl = servicesTemp.GS_SUB_TOTL;
																gsDisc = servicesTemp.GS_DISC;
																gsTotal = servicesTemp.TOTAL;
															}
												%>
												<tr>
													<td width="34%" class="td" height="21"><%=servicesTemp.descriptionAditional%></td>
													<td width="21%" class="td" height="21" align="right">$<%=gsSubTotl%></td>
													<td width="17%" class="td" height="21" align="right">$<%=gsDisc%></td>
													<td width="28%" class="td" height="21" align="right">$<%=gsTotal%></td>
												</tr>
												<%
														} else if ((Services) servicesTable.get("ead") != null) {
															servicesTemp = (Services) servicesTable.get("ead");
															if (servicesTemp.getMostrarDescuentos().equals("N")) {
																gsSubTotl = servicesTemp.TOTAL;
																gsDisc = "0.00";
																gsTotal = servicesTemp.TOTAL;
															} else {
																gsSubTotl = servicesTemp.GS_SUB_TOTL;
																gsDisc = servicesTemp.GS_DISC;
																gsTotal = servicesTemp.TOTAL;
															}
												%>
												<tr>
													<td width="34%" class="td" height="21"><%=servicesTemp.descriptionAditional%></td>
													<td width="21%" class="td" height="21" align="right">$<%=gsSubTotl%></td>
													<td width="17%" class="td" height="21" align="right">$<%=gsDisc%></td>
													<td width="28%" class="td" height="21" align="right">$<%=gsTotal%></td>
												</tr>
												<%
														} else if ((Services) servicesTable.get("ack") != null) {
															servicesTemp = (Services) servicesTable.get("ack");
															if (servicesTemp.getMostrarDescuentos().equals("N")) {
																gsSubTotl = servicesTemp.TOTAL;
																gsDisc = "0.00";
																gsTotal = servicesTemp.TOTAL;
															} else {
																gsSubTotl = servicesTemp.GS_SUB_TOTL;
																gsDisc = servicesTemp.GS_DISC;
																gsTotal = servicesTemp.TOTAL;
															}
												%>
												<tr>
													<td width="34%" class="td" height="21"><%=servicesTemp.descriptionAditional%></td>
													<td width="21%" class="td" height="21" align="right">$<%=gsSubTotl%></td>
													<td width="17%" class="td" height="21" align="right">$<%=gsDisc%></td>
													<td width="28%" class="td" height="21" align="right">$<%=gsTotal%></td>
												</tr>
												<%
														} else if ((Services) servicesTable.get("cod") != null) {
															servicesTemp = (Services) servicesTable.get("cod");
															if (servicesTemp.getMostrarDescuentos().equals("N")) {
																gsSubTotl = servicesTemp.TOTAL;
																gsDisc = "0.00";
																gsTotal = servicesTemp.TOTAL;
															} else {
																gsSubTotl = servicesTemp.GS_SUB_TOTL;
																gsDisc = servicesTemp.GS_DISC;
																gsTotal = servicesTemp.TOTAL;
															}
															if (global.tarifType != null && global.tarifType.equalsIgnoreCase("C")) {
																if (((String) session.getAttribute("valorCod")) != null 
																		&& ((String) session.getAttribute("valorCod")).trim().length() > 0) {
												%>
												<tr>
													<td width="34%" class="td" height="21"><%=servicesTemp.descriptionAditional%></td>
													<td width="21%" class="td" height="21" align="right">$<%=gsSubTotl%></td>
													<td width="17%" class="td" height="21" align="right">$<%=gsDisc%></td>
													<td width="28%" class="td" height="21" align="right">$<%=gsTotal%></td>
												</tr>
												<%
																}
															} else {
												%>
												<tr>
													<td width="34%" class="td" height="21"><%=servicesTemp.descriptionAditional%></td>
													<td width="21%" class="td" height="21" align="right">$<%=gsSubTotl%></td>
													<td width="17%" class="td" height="21" align="right">$<%=gsDisc%></td>
													<td width="28%" class="td" height="21" align="right">$<%=gsTotal%></td>
												</tr>
												<%
															}
														} else if ((Services) servicesTable.get("codr") != null) {
															servicesTemp = (Services) servicesTable.get("codr");
															if (servicesTemp.getMostrarDescuentos().equals("N")) {
																gsSubTotl = servicesTemp.TOTAL;
																gsDisc = "0.00";
																gsTotal = servicesTemp.TOTAL;
															} else {
																gsSubTotl = servicesTemp.GS_SUB_TOTL;
																gsDisc = servicesTemp.GS_DISC;
																gsTotal = servicesTemp.TOTAL;
															}
												%>
												<tr>
													<td width="34%" class="td" height="21"><%=servicesTemp.descriptionAditional%></td>
													<td width="21%" class="td" height="21" align="right">$<%=gsSubTotl%></td>
													<td width="17%" class="td" height="21" align="right">$<%=gsDisc%></td>
													<td width="28%" class="td" height="21" align="right">$<%=gsTotal%></td>
												</tr>
												<%
														} else if ((Services) servicesTable.get("inv") != null) {
												%>
												<logic:notPresent name="NOINSURANCE">
												<%
														servicesTemp = (Services) servicesTable.get("inv");
												%>
													<tr>
														<td width="34%" class="td" height="21"><%=servicesTemp.descriptionAditional%></td>
														<td width="21%" class="td" height="21" align="right">$<%=servicesTemp.GS_SUB_TOTL%></td>
														<td width="17%" class="td" height="21" align="right">$<%=servicesTemp.GS_DISC%></td>
														<td width="28%" class="td" height="21" align="right">$<%=servicesTemp.TOTAL%></td>
													</tr>
												</logic:notPresent>
												<%
														} else if ((Services) servicesTable.get("shipLS") != null) {
															servicesTemp = (Services) servicesTable.get("shipLS");
															if (servicesTemp.getMostrarDescuentos().equals("N")) {
																gsSubTotl = servicesTemp.TOTAL;
																gsDisc = "0.00";
																gsTotal = servicesTemp.TOTAL;
															} else {
																gsSubTotl = servicesTemp.GS_SUB_TOTL;
																gsDisc = servicesTemp.GS_DISC;
																gsTotal = servicesTemp.TOTAL;
															}
												%>
												<tr>
													<td width="34%" class="td" height="21"><%=servicesTemp.descriptionAditional%></td>
													<td width="21%" class="td" height="21" align="right">$<%=gsSubTotl%></td>
													<td width="17%" class="td" height="21" align="right">$<%=gsDisc%></td>
													<td width="28%" class="td" height="21" align="right">$<%=gsTotal%></td>
												</tr>
												<%
														}
														//Code added by palanivel to display Additional service Values				
														//if (global.tarifType != null && global.tarifType.equalsIgnoreCase("C")) {//AAP01
															
														if (aform != null && aform.getShowAdditional() != null
																	&& aform.getShowAdditional().equalsIgnoreCase("Y")) {
															if (session.getAttribute("aditionalServicesDetail") != null) {
																additionalServicesArray = (ArrayList) session.getAttribute("aditionalServicesDetail");
																int arraySize = additionalServicesArray.size();
																for (int j = 0; j < arraySize; j++) {
																	serviceRecordsRecs = (AdditionalService) additionalServicesArray.get(j);
																	if ((Services) servicesTable.get(serviceRecordsRecs.service) != null) {
																		servicesTemp = (Services) servicesTable.get(serviceRecordsRecs.service);
												%>
												<tr>
													<td width="34%" class="td" height="21"><%=servicesTemp.descriptionAditional%></td>
													<td width="21%" class="td" height="21" align="right">$<%=servicesTemp.GS_SUB_TOTL%></td>
													<td width="17%" class="td" height="21" align="right">$<%=servicesTemp.GS_DISC%></td>
													<td width="28%" class="td" height="21" align="right">$<%=servicesTemp.TOTAL%></td>
												</tr>
												<%
																	}
																}
															}
														}
													}
												%>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td align="center"><html:button property="return" value="Ocultar" styleClass="button" onclick="callServices()" /></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	<!--  /form-->
<!-- /body-->
</html:html>
