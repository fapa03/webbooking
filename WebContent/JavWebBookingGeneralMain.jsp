<%--

/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creación: 15/11/2012
 * Compañía: KUMARAN.
 * Descripción del programa: consulta generica para SYS_PARM_MSTR
 * FileName	:	JavwebBookinggeneralMain.jsp
 * FormBean	:	JavWebBookingGeneralMainForm.class
 * ActionBean	:	JavwebBookinggeneralMainAction.class
 * OtherClasses	:	none
 * CSS FileName	:	webbooking.css
 * ----------------------------------------------------------------- 
 * MODIFICACIONES:  
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 31/05/2012
 * Descripción:  Se agregó variable pesoMax para validar peso maximo a capturar por linea en el detalle de la captura
 * ------------------------------------------------------------------
 * Clave: AAP02
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 05/06/2012
 * Descripción:  Se agregó variable clasifTarif para validar el tipo de tarifa nueva o vieja	
 * ------------------------------------------------------------------
 * Clave: AAP04
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 01/07/2013
 * Descripción:  Se agregaron validaciones para FLETE POR COBRAR
 * ------------------------------------------------------------------
 */
--%>
<%@ page info="WebBooking - General"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.text.Normalizer, java.util.LinkedHashMap, java.util.ArrayList, beanUtil.GetBrnchOcurre, bean.Global, bean.ShipmentServiceDetail, beanForm.JavWebBookingGeneralMainForm, bean.ServicesTotal, bean.AdditionalService, mx.com.paquetexpress.general.model.dto.BranchDetailDTOResponse"%>
<%
	String version = "00.00.47";
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
	
	Global global = (Global) session.getAttribute("sGlobal");
	String valordeclarado = (String) request.getAttribute("valordeclarado");
	String valorvalue = (String)session.getAttribute("valorvalue");
	if (valorvalue==null) {
		valorvalue = "";
	}
	JavWebBookingGeneralMainForm aform = (JavWebBookingGeneralMainForm) session.getAttribute("webBookinggeneralMain");
	GetBrnchOcurre gbo = new GetBrnchOcurre();
	String addr = ""; 
	String aditionalFlag = global.additionalServiceFlag.trim();
	String flete = "&nbsp;";
	String servicios = "&nbsp;";
	String descuento = "&nbsp;";
	String subTotal = "&nbsp;";
	String iva = "&nbsp;";
	String ivaRet = "&nbsp;";
	String total = "&nbsp;";
	String action = aform.getAccionServices();
	String errormsgentrega = (String) request.getAttribute("errormsgentrega");
	String errormsgcod = (String) request.getAttribute("errormsgcod");
	String errormsgstatus = (String) request.getAttribute("errormsgstatus");
	String erroravailcred = (String) request.getAttribute("erroravailcred");
	String errmsgenvelope = aform.getErrmsgenvelope();
	//AAPP
	if ((errormsgcod != null && errormsgcod.length() > 0)
			|| (errormsgstatus != null && errormsgstatus.length() > 0)
			|| (errormsgentrega != null && errormsgentrega.length() > 0)
			|| (erroravailcred != null && erroravailcred.length() > 0)
			|| (errmsgenvelope != null && errmsgenvelope.length() > 0)) {
		action = null;
		aform.setAccionServices("");
	}

	ServicesTotal servicesTotal = (ServicesTotal) session.getAttribute("servicestotal");
	if (action != null && (action.equals("calculate") || action.equals("generate") || action.equals("showAditional")) && servicesTotal != null) {
		flete = (servicesTotal.flete != null ? "$" + servicesTotal.flete : "");
		servicios = (servicesTotal.servicios != null ? "$" + servicesTotal.servicios : "");
		descuento = (servicesTotal.descuento != null ? "$" + servicesTotal.descuento : "");
		subTotal = (servicesTotal.subTotal != null ? "$" + servicesTotal.subTotal : "");
		iva = (servicesTotal.iva != null ? "$" + servicesTotal.iva : "");
		ivaRet = (servicesTotal.ivaRet != null ? "$" + servicesTotal.ivaRet : "");
		total = (servicesTotal.total != null ? "$" + servicesTotal.total : "");
	}
%>
<%!public String replaceNull(String original) {
		if (original == null || original.length() == 0) {
			original = "&nbsp;";
		}
		return original;
	}%>
<html:html>

<head>
	<meta http-equiv="Content-Type" content="text/html;">
	
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
	<script type="text/javascript" charset="UTF-8" src="js/qz/bridgePrintQZ.js"></script>
	<link rel="stylesheet" media="screen" type="text/css" href="css/QZTray.css">

<link rel="stylesheet" media="screen" type="text/css" href="css/font-awesome.min.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css?v=<%=version%>">
<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">

<link rel=stylesheet media=screen type=text/css href=webbooking.css?v=<%=version%>>
<script type="text/javascript" src="common.js?v=<%=version%>"></script>
<script type="text/javascript" src="barraEspera.js?v=<%=version%>"></script>
<script type="text/javascript" src="js/jquery/1.12.0.js?v=<%=version%>"></script>
<script type="text/javascript" src="JavWebBookingGeneralMain.js?v=<%=version%>"></script>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<script type="text/javascript" src="js/v2/global.js?v=<%=version%>"></script>
<script type="text/javascript" src="js/ToastNotification.js?v=<%=version%>"></script>
<title>Documentacion en Linea</title>
</head>
<body id="body" onpageshow="initScript();mostrarCapaCentro('mensaje');emptyTableCheck();" onscroll="mostrarCapaCentro('mensaje');" onkeydown="noBack(event);" 
	leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" class="backgroundStandard widthOverflow">
	<html:form action="webBookinggeneralMain.do">
		<!--jsp:include page="nocache.jsp" flush="false" /-->
		<logic:present name="loginForm">
			<html:hidden name="loginForm" property="userValidate" />
			<logic:equal name="loginForm" property="userValidate" value="validUser">
				<logic:notEqual name="loginForm" property="showWeb" value="Y">
					<jsp:forward page="mainpage.do"></jsp:forward>
				</logic:notEqual>
			</logic:equal>
		</logic:present> 
		<table border="0" cellspacing="0" cellpadding="0" class="width100porcent backgroudBluePtx">
			<tr>
				<td  >
					<div class="bodyWidth marginAutoCentro">
				        <img src="images/logos/logoW.png" border="0"  
				        onclick="goOut('mainpage');hideMenu();"       
				          style="max-width: 138px;margin-top: 11px;margin-bottom: 9px;cursor: pointer;"> 											
							
							<a id="menu-hamburger" class="Animated1" onclick="showMenu()">							
								<div style="margin-top: 15px;">
									<span id="menuline1" class="Animated05"></span> <span
										id="menuline2" class="Animated05"></span> <span id="menuline3"
										class="Animated05"></span>
								</div>
							</a>
					</div>
				</td>
				<td >			 
				</td>
			</tr>
		</table>
				
		<div class="MenuOverlay displayNone">
		
	        <a class="btnCloseMenu" onclick="hideMenu()">
	            <div>
	                <span class="lineOne"></span>
	                <span class="lineTwo"></span>
	            </div>
 	        </a> 
  			<div class="itemsMenu marginAutoCentro bodyWidth"> 			
					<logic:present name="loginForm">
						<html:hidden name="loginForm" property="userValidate" />
						<logic:equal name="loginForm" property="userValidate" value="validUser">
  								 
								<a onclick="goOut('cliententry');hideMenu();" style="">Registro de cliente destino</a>
								<a onclick="goOut('mainpage');hideMenu();" style="">Menú principal</a>
								<logic:equal name="loginForm" property="showWeb" value="Y">
									<logic:equal name="loginForm" property="newBooking" value="Y">
										<a onclick="goOut('thispage');hideMenu();" style="">Elaboración de guías</a>
									</logic:equal>
								</logic:equal>
								<html:hidden name="loginForm" property="showPpg"></html:hidden>
								<logic:equal name="loginForm" property="showPpg" value="Y">
									<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
								</logic:equal>
								<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelación de guías</a>
								<a onclick="goOut('shipmenthistory');hideMenu();" style="">Historico de envios</a>
								<a onclick="goOut('clientreport');hideMenu();" style="">Información de cliente</a>
								<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->
									<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a>									
								</logic:equal>
						 
 						</logic:equal>
					</logic:present> 
 					<logic:present name="loginForm">
						<html:hidden name="loginForm" property="userValidate" />
						<logic:notEqual name="loginForm" property="userValidate" value="validUser">
						
 								<a onclick="goOut('mainpage');hideMenu();" style="">Menú principal</a> 
								<logic:equal name="loginForm" property="showWeb" value="Y">
									<logic:equal name="loginForm" property="newBooking" value="Y">
										<a onclick="goOut('thispage');hideMenu();" style="">Elaboración de guías</a>
									</logic:equal>
								</logic:equal> 
								<html:hidden name="loginForm" property="showPpg"></html:hidden>
								<logic:equal name="loginForm" property="showPpg" value="Y">
									<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
								</logic:equal>
								<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelación de guías</a> 
								<a onclick="goOut('shipmenthistory');hideMenu();" style="">Historico de envios</a> 
								<a onclick="goOut('clientreport');hideMenu();" style="">Información de cliente</a> 
								<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->
									<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a>									
								</logic:equal>
						</logic:notEqual>
					</logic:present>  			  						
			</div> 			
		</div>				
		<table border="0" cellspacing="0" cellpadding="0" class="marginAutoCentro" >
			<tr>
				<td>
					<!--  New Image  --> 
					<logic:present name="loginForm">
						<html:hidden name="loginForm" property="userValidate" />
						<logic:equal name="loginForm" property="userValidate" value="validUser">
							<map name="mainMap">
								<!-- added by bala -->
								<area shape="rect" coords="565,5,680,18" href="javascript:goOut('clientreport')">
								<area shape="rect" coords="460,5,555,17" href="javascript:goOut('shipmenthistory')">
								<area shape="rect" coords="1,4,135,18" href="javascript:goOut('cliententry')">
								<area shape="rect" coords="145,5,226,18" href="javascript:goOut('mainpage')">
								<area shape="rect" coords="236,5,342,18" href="javascript:goOut('thispage')">
								<area shape="rect" coords="352,5,450,18" href="javascript:goOut('guiacancel')">
								<area shape="rect" coords="690,4,772,18" href="login.do?logout=yes" alt="Terminar Sesión" title="Terminar Sesión">
								<!-- ended  by bala -->
							</map>
						</logic:equal>
					</logic:present> 
					<!--  Old Image  --> 
					<logic:present name="loginForm">
						<html:hidden name="loginForm" property="userValidate" />
						<logic:notEqual name="loginForm" property="userValidate" value="validUser">

							<img src="images/toplink1.gif" width="771" height="22" usemap="#mainMap" border="0">
							<map name="mainMap">
								<area shape="rect" coords="67,6,151,18" href="javascript:goOut('mainpage')">
								<area shape="rect" coords="165,6,275,17" href="javascript:goOut('thispage')">
								<area shape="rect" coords="288,6,390,17" href="javascript:goOut('guiacancel')">
								<area shape="rect" coords="404,6,506,17" href="javascript:goOut('shipmenthistory')">
								<area shape="rect" coords="520,6,644,17" href="javascript:goOut('clientreport')">
								<area shape="rect" coords="676,6,764,17" href="login.do?logout=yes" alt="Terminar Sesión" title="Terminar Sesión">
							</map>
						</logic:notEqual>
					</logic:present>
				</td>
			</tr>
		</table>
		<map name="peMap">
			<area shape="rect" coords="13,19,235,71" href="http://www.paquetexpress.com.mx" target="_blank" alt="www.paquetexpress.com" title="www.paquetexpress.com">
		</map>
		<table border="0" align=center cellspacing="0" cellpadding="0" >
			<logic:present name="shipmentdestination" scope="request">
				<tr>
					<td align=center>
						<font style="font-size: 12pt; color: #ff0000; font-weight: bold">
							<bean:write name="shipmentdestination" />
						</font>
					</td>
				</tr>
			</logic:present>
		</table>
		<table class="marginAutoCentro bodyWidth">
			<tr>
				<td style="height: 8px;">
					<textarea id="mensaje" class="textareacontenido" cols="46" readonly="readonly" rows="4" onblur="self.focus();" style="background-color: #EEEEEE; border-style: ridge; 
						overflow: hidden; text-align: center; vertical-align: middle; z-index: 2; visibility: hidden; position: absolute; top: 450px; left: 200px; width: 350px; height: 80px">
					</textarea>
				</td>
			</tr> 
		</table>
		<table border=0 cellspacing=1 cellpadding=0 class="marginAutoCentro bodyWidth">
			<tr>
				<td width="774">
					
					<table width=774 border=0 cellspacing=1 cellpadding=0>
						<tr>
							<td style="height: 45px !important;" ><strong class="titleSection fontBold">Documentación</strong></td>
							<td colspan="2" align="right" ><strong>Plaza:</strong> <%=global.getAssignedSite()%> - <%=global.getSiteName()%></td>
						</tr>
						<tr>
							<td width="130"><strong >Centro Costo</strong></td>
							<td width="125"><strong class="fontBold">Cliente Origen</strong></td>
							<td width="480">
								<font size="3"> 
									<html:text size="13" style="border:none;" readonly="true" property="orgienclave" />
									<html:text size="48" style="border:none;" readonly="true" property="orgiennombre" />
								</font>
							</td>
						</tr>
						<tr>
							<td width="130" title="<bean:write name="dirOrigenTitle" />">
								<html:select property="centrosCosto" onchange="cambioCentroCosto();"><!-- AAP05 -->
									<html:options property="centrosCostoValue" labelProperty="centrosCostoLabel"/><!-- AAP05 -->									
								</html:select><!-- AAP05 -->
							</td>
							<td width="125"><strong class="fontBold">Usuario</strong></td><!-- AAP05 -->
							<td width="480"><!-- AAP05 -->
								<font size="3"> <!-- AAP05 -->
									<html:text size="13" style="border:none;" readonly="true" property="origenUserClave" /><!-- AAP05 -->
									<html:text size="48" style="border:none;" readonly="true" property="origenUserNombre" /><!-- AAP05 -->
								</font><!-- AAP05 -->
							</td><!-- AAP05 -->
						</tr>
						<tr>
							<td colspan="3">
							 	<fieldset>
  									<legend>Cliente Destino</legend>
  								
  								
								<table width=774 border="0" cellspacing="0" cellpadding="0" >							 
									<tr>
										<td colspan="6">
											<table width=774 border="0" cellspacing="0" 
												class="tableRowSeparator"
												cellpadding="0" >
												<tr>
													<td align="right" style="width: 0;">Clave</td>
													<td width="4%" align="left">
 														
														  <div class="inputV2">
															  <div class="group">       
															      <html:text size="27" property="destinationclave" styleClass="upper inputMarginRight" readonly="false" onkeypress="openKeyLov('clientNew')" onblur="setBack('true');" onfocus="setBack('false');validaSelect(this);" onclick="textoSeleccionado()"/>
															      <span class="highlight"></span>
															      <span class="bar"></span>
 															    </div>
														    </div>														 
													</td>
													<td width="1%" align="center">
														<html:button property="destinationclientBut" styleClass="button1 buttonMedium"
																value="Cliente" onclick="openLov('clientNew');" />&nbsp;
													</td>
													<td width="1%" align="center">
														<html:button property="destinationclientBut" styleClass="button1 buttonMedium"
																value="Registrar" onclick="openLov('cliententry');" />
													</td>
													
													<td width="8%" align="left">
														<font size="3">
															Nombre													
						  									<div class="inputV2">
															  <div class="group">       
															      <html:text property="destinationnombre" styleClass="upper width100" readonly="false" onkeypress="openKeyLov('clientNew')" onblur="setBack('true');" onfocus="setBack('false');validaSelect(this);" onclick="textoSeleccionado()"/>
															      <span class="highlight"></span>
															      <span class="bar"></span>
 															    </div>
														    </div>																													
														</font>
													</td>
												</tr>
												<tr>
													<td width="4%" align="left">
														<html:button property="destinobutton" styleClass="button1 buttonMedium"  
															onclick="openLov('destinationaddress')" >Destino</html:button>
													</td>
													<td width="15%" align="left">
													
						  									<div class="inputV2">
															  <div class="group">       
															      <html:text size="27" property="destino1" readonly="true" onclick="openLov('destinationaddress')" />
															      <span class="highlight"></span>
															      <span class="bar"></span>
 															    </div>
														    </div>														
													 
													</td>
													<td width="5%" align="left">
													
	  													<div class="inputV2">
														  <div class="group">       
														      <html:text size="11" property="destino2" readonly="true" />
														      <span class="highlight"></span>
														      <span class="bar"></span>
															    </div>
													    </div>														
													 
													</td>
													<td width="1%" align="right">&nbsp;Colonia&nbsp;</td>
													<td width="36%">
														<table border="0" cellpadding="0" cellspacing="0" width="70%">
															<tr>
																<td width="4%" align="left">
																
																	<div class="inputV2">
																	  <div class="group">       
																	      <html:text size="26" property="destinationcolonia1" styleClass="inputMarginRight" readonly="true" />
																	      <span class="highlight"></span>
																	      <span class="bar"></span>
																		    </div>
																    </div>																		
															 
																</td>
																<td width="4%" align="left">
																
																	<div class="inputV2">
																	  <div class="group">       
																	      <html:text size="57" property="destinationcolonia2" readonly="true" />
																	      <span class="highlight"></span>
																	      <span class="bar"></span>
																		    </div>
																    </div>																																
																</td>
																<td width="0%" align="left"></td>
															<tr>
														</table>
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<table border="0" cellpadding="0" cellspacing="0" width="100% " class="tableDiv tableRowSeparator "  >
												<tr>
													<td width="" class="width5 textAlignRight" ><font size="3"> RFC&nbsp; </font></td>
													<td width="" class="width10" >
 														
														<div class="inputV2">
														  <div class="group">       
														      <html:text size="18" readonly="true" property="destinationrfc" />
														      <span class="highlight"></span>
														      <span class="bar"></span>
															    </div>
													    </div>																																												
 													</td>
													<td width="" class="width5 textAlignRight" ><font size="3">&nbsp;Telefono&nbsp;</font></td>
													<td width="" class="width10">
													
														<div class="inputV2">
														  <div class="group">       
														      <html:text styleClass="textcontenido" readonly="true" property="destinationtelefono" maxlength="10"
														      style="width:100px;"
															onblur="isNumber22(this);if (!this.readOnly)setBack('true');" onfocus="setBack('false');"/>
														      <span class="highlight"></span>
														      <span class="bar"></span>
															    </div>
													    </div>																																											
													</td>																										
													<td width="" class="width5 textAlignRight" ><font size="3"> Plaza&nbsp;</font></td>
													<td width="" class="width5" >
											 
													  <div class="inputV2">
														  <div class="group">       
																<html:text size="8" readonly="true"
																    styleClass="inputMarginRight"
																	property="destinationsitecode" />
																												      
														      <span class="highlight"></span>
														      <span class="bar"></span>
														   </div>
													    </div>	
													 </td>
													 <td width="" class="width10" >
													    	
													    <div class="inputV2">
														  <div class="group">       
																<html:text size="24" readonly="true"
																	property="destinationsite" />																												      
														      <span class="highlight"></span>
														      <span class="bar"></span>
														   </div>
													    </div>																		
													 </td>																										 
													<td width="" class="width5 textAlignRight" ><font size="3"> Sucursal&nbsp;</font></td>
													<td width="" class="width5" >
											 
														<div class="inputV2">
															  <div class="group">       
																 <html:text styleClass="inputMarginRight"
																	size="8" readonly="true"
																	property="destinationcode" /> 
																													      
															      <span class="highlight"></span>
															      <span class="bar"></span>
															   </div>
														    </div>	
													 </td>
													 <td width="" class="width10" >
													    	
														<div class="inputV2">
																	  <div class="group">       
																		<html:text
																			size="25" readonly="true"
																			property="destinationbranch" /> 
																															      
																	      <span class="highlight"></span>
																	      <span class="bar"></span>
																	   </div>
																    </div>	
													    </div>																		
													 </td>											 													
												</tr>
											</table>
										</td>
									</tr>
									<tr>
										<td>
											<div id="divRefDom" style="visibility: hidden; position: absolute; top: 450px; left: 200px; ">
											<table style="border: 0; padding: 0; border-spacing: 0; width: 70%;">
												<tr>
													<td width="15%"><font size="3"> Referencia de domicilio&nbsp; </font></td>
													<td width="85%">													
													  <div class="inputV2">
														  <div class="group">       
																<html:textarea rows="2" cols="46" readonly="false" property="destinationRefDom" onkeypress="ismaxlength(this,50)" onkeyup="validChar(this,'special')"
																onblur="setBack('true');" onfocus="setBack('false');" styleClass="textarea"/>																												      
														      <span class="highlight"></span>
														      <span class="bar"></span>
															    </div>
													    </div>															
													</td>
												</tr>
											</table>
											</div>
										</td>
									</tr>									
								</table>
							</fieldset>
							</td>
						</tr>				
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<table border="0" align=center cellspacing="0" cellpadding="0"
						width="75%">
						<logic:present name="zeroexist" scope="request">
							<tr>
								<td align=center><font
									style="font-size: 12pt; color: #ff0000; font-weight: bold">
										<bean:write name="zeroexist" />
								</font></td>
							</tr>
						</logic:present>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<fieldset>
  						<legend>Tipo de envío</legend>  									
						<table width="99%" border="0" cellpadding="0" cellspacing="0" class="tableRowSeparator" >
							<tr>
								<td class="LabelForm" align="left">Tipo de Envío:
									<html:select property="shippingType" onchange="setShippingType()" onclick="eventOnClicShippingType()">
										<html:options property="shippingTypeTO" labelProperty="shippingTypesName" />
									</html:select>
									<div align="center" ><span class="txt-special-1"><bean:write name="webBookinggeneralMain" property ="msjShippingCbtr"/></span></div>
									
								</td>
							</tr>
						</table>
				    </fieldset>	
				</td>
			</tr>
			<tr>
				<td>
					<fieldset>
  						<legend>Detalle de envío</legend>  									
  								
						<table width="99%" border="0" cellpadding="0" cellspacing="0" class="tableRowSeparator" >
						<tr>
							<td ><div align="right" style="padding-right: 1px;">Cantidad</div></td>
							<td style="padding-right: 10px;">
								<div class="inputV2" style="width: 100px;">
								  <div class="group">       
									<html:text styleClass="text" property="cantidad" onblur="isNumber22(this);setBack('true');validateMaxQty();" onfocus="setBack('false');"  style="width: 100%;"/>
																						      
								      <span class="highlight"></span>
								      <span class="bar"></span>
								   </div>
							    </div>								
																						
							</td>
							<td ><div align="right" style="padding-right: 1px;">Descripcion</div></td>
							<td style="padding-right: 20px;">
								<table cellspacing="0" cellpadding="0">
									<tr>
										<td style="padding-right: 5px;">
											<div class="inputV2" style="width: 300px;">
											  <div class="group">       
												<html:text styleClass="textdescrip" property="descripcion" readonly="true" onclick="openLov('description')" style="width: 100%;"/>
																									      
											      <span class="highlight"></span>
											      <span class="bar"></span>
											   </div>
										    </div>												   
											
										</td>
										<td>
										<html:button property="lovbutton1" style="width: 50px;" styleClass="lov buttonMore" onclick="openLov('description')">
											. . .
										</html:button>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td><div align="right" style="padding-right: 1px;">Contenido</div></td>
							<td > 								
								<div class="inputV2" style="width: 340px;"> <!-- 340px -->
								  <div class="group">       
									 <html:text styleClass="textcontenido" property="contenido" onblur="setBack('true');" onfocus="setBack('false');" style="width: 100%;" maxlength="100"/>
																						      
								      <span class="highlight"></span>
								      <span class="bar"></span>
								   </div>
							    </div>
							</td>
							<td width="60px"><div align="right" style="padding-right: 1px;">Cat. productos</div></td>
							<td style="padding-right: 20px;" width="300px"> 
								<table>
									<tr>
										<td>
											<div class="inputV2" style="width: 300px;">
											  <div class="group">       
												 <html:text styleClass="textcontenido" property="productDescSat" onblur="setBack('true');" onfocus="setBack('false');" onkeydown="openKeyLov('catProducts')" onkeypress="openKeyLov('catProducts')" style="width: 100%;" maxlength="100"/>
																									      
											      <span class="highlight"></span>
											      <span class="bar"></span>
											   </div>
										    </div>
										</td>
										<td>
										<html:button property="lovbutton3" value=". . ."
										styleClass="lov buttonMore" onclick="openLov('catProducts')" />
									</td>
									</tr>
								</table>
							</td>
							<td id="infTarifa1" width="50px"><div align="right" style="padding-right: 1px;">Tarifa</div></td>
							<td id="infTarifa3">
								<table cellspacing="0" cellpadding="0" id="req">
									<tr>
										<td>
										
											<div class="inputV2" style="width: 50px;">
											  <div class="group">       
												 <html:text styleClass="text" property="tarifa" readonly="true" onclick="openLov('tariff')" style="width: 100%;" />
																									      
											      <span class="highlight"></span>
											      <span class="bar"></span>
											   </div>
										    </div>																					
										
										</td>
										<td>
											<html:button property="lovbutton2" value=". . ." 
											styleClass="lov buttonMore" onclick="openLov('tariff')"/>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							
								<td colspan="7">
									<table width="100%" cellspacing="0" cellpadding="0">
										<tr>
										<td ><div align="right" style="padding-right: 1px;">Peso<em style="color: gray;">(kg)</em></div></td>
										<td style="padding-right 20px;" width="100px">
										
											<div class="inputV2" style="width: 100px;">
											  <div class="group">       
												 <html:text styleClass="text" property="peso" onkeypress="return isNumber(event, this)" size="15" onblur="setBack('true');calcVolWeight();" onfocus="if (!document.forms[0].peso.readOnly)setBack('false');" style="width: 100%;"/>
																									      
											      <span class="highlight"></span>
											      <span class="bar"></span>
											   </div>
										    </div>										
										
										</td>
											<td><div align="left" style="">Volumen</div></td>
											<td style="padding-right: 20px;">

												<div class="inputV2" style="width: 100px;">
													<div class="group">
														<html:text styleClass="text" property="volumen"
															onblur="setBack('true');" onkeypress="return isNumber(event, this)" onchange="onchangeVolumen();"
															onfocus="if (!document.forms[0].volumen.readOnly)setBack('false');"
															style="width: 100%;" />

														<span class="highlight"></span> <span class="bar"></span>
													</div>
												</div>

											</td>
											<td><div align="right" style="padding-right: 1px;">Largo<em style="color: gray;">(cm)</em></div></td>
											<td style="padding-right: 20px;">
												<div class="inputV2" style="width: 100px;">
													<div class="group">
														<html:text styleClass="text" property="volL"
															onkeypress="return isNumber(event, this);" size="15"
															onkeyup="return isInteger(event, this, document.forms[0].tarifa.value);"
															onblur="isNumber22(this);setBack('true');calcVolWeight();"
															onfocus="setBack('false');" style="width: 100%;" />
														<span class="highlight"></span> <span class="bar"></span>
													</div>
												</div>
											</td>
											<td><div align="right" style="padding-right: 1px;">Alto<em style="color: gray;">(cm)</em></div></td>
											<td style="padding-right: 20px;">
												<div class="inputV2" style="width: 100px;">
													<div class="group">
														<html:text styleClass="text" property="volH"
															onkeypress="isInteger(event, this);return isNumber(event, this);" size="15"
															onkeyup="return isInteger(event, this, document.forms[0].tarifa.value);"
															onblur="setBack('true');calcVolWeight();" onfocus="setBack('false');"
															style="width: 100%;" />
														<span class="highlight"></span> <span class="bar"></span>
													</div>
												</div>
											</td>
											<td><div align="right" style="padding-right: 1px;">Ancho<em style="color: gray;">(cm)</em></div></td>
											<td style="padding-right: 20px;">
												<div class="inputV2" style="width: 100px;">
													<div class="group">
														<html:text styleClass="text" property="volW"
															onkeypress="isInteger(event, this);return isNumber(event, this);" size="15"
															onkeyup="return isInteger(event, this, document.forms[0].tarifa.value);"
															onblur="setBack('true');calcVolWeight();" onfocus="setBack('false');"
															style="width: 100%;" />
														<span class="highlight"></span> <span class="bar"></span>
													</div>
												</div>
											</td>
											<td><div align="right" style="padding-right: 1px;">Peso volumétrico</div></td>
											<td>
												<div class="inputV2" style="width: 100px;">
													<div class="group">
														<html:text styleClass="text" property="weightVolumetric"
															readonly="true" style="width: 100%;" />
														<span class="highlight"></span> <span class="bar"></span>
													</div>
												</div>
											</td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
							<td width="73%" colspan="9">
								<table width="100%" cellspacing="0" cellpadding="0">
									<tr>
										<td width="40%"><div align="right"></div></td>
										<td width="7%">
											<div align="center">
												<html:button property="addbutton" value="Agregar" styleClass="button2 buttonMedium fa fa-search" onclick="submitForm(this)" />
											</div>
										</td>
										<td width="46%">&nbsp;</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
						<logic:present name="msgDimInfo" scope="session">  
		                	<tr>  
			                    <td align=center><font  
			                        style="font-size: 12pt; color: #ff0000; font-weight: bold">  
			                            <bean:write name="msgDimInfo" scope="session" />  
			                    </font></td>  
		                	</tr>  
	            		</logic:present>  
					</fieldset>	
				</td>
			</tr>
			<tr>
				<td>
						<fieldset>
  						<legend>Detalles de Embarque</legend>			
						<table class="tablaDetallev2" id="tablaDetalle" cellSpacing=1 cellPadding=0 width="99%" border=0>
						<tbody>
							<tr class="tablaDetallev2Header" >
								<td width="7%">Cantidad</td>
								<td width="10%">Descripcion</td>
								<td width="43%">Contenido</td>
								<td width="5%">Clave Producto</td>
								<td width="5%">Desc Producto</td>
								<td width="6%"><P align=left>Tarifa</P></td>
								<td width="8%"><P align=right>Peso</P></td>
								<td width="8%"><P align=right>Volumen</P></td>
								<td width="8%"><P align=right>Largo</P></td>
								<td width="8%"><P align=right>Alto</P></td>
								<td width="8%"><P align=right>Ancho</P></td>
								<td width="8%"><P align=right>Peso vol.</P></td>
								<td width="16%"><P align=right>Importe</P></td>
								<td width="2%">&nbsp;</td>
							</tr>
							<%
								ArrayList servicesDetailArray = (ArrayList) session.getAttribute("servicesDetail");
								int size = 0;

								if (servicesDetailArray != null) {
									size = servicesDetailArray.size();
								}
								int i = 0;
								ShipmentServiceDetail ssd = null;
								for (i = 0; i < size; i++) {
									ssd = (ShipmentServiceDetail) servicesDetailArray.get(i);									
							%>
							<tr class="tablaDetallev2Body">
								<td width="5%" height=20 class="td"><%=replaceNull(ssd.cantidad)%></td>
								<td width="10%" height=20 class="td"><%=replaceNull(ssd.descripcion)%></td>
								<td width="43%" height=20 class="td"><%=replaceNull(ssd.contenido)%></td>
								<td width="8%" height=20 class="td"><%=replaceNull(ssd.getProductIdSat())%></td>
								<td hidden="hidden" class="td"><%=replaceNull(ssd.getProductDescSat())%></td>
								<td width="8%" height=20 class="td"><%=replaceNull(ssd.getProductDescSat().split("\\ - ")[1])%></td>
								<td width="6%" height=20 class="td"><%=replaceNull(ssd.tarifa)%></td>
								<td width="8%" height=20 class="td" align=right><%=replaceNull(ssd.peso)%></td>
								<td width="8%" height=20 class="td" align=right><%=replaceNull(ssd.volumen)%></td>
								<td width="8%" height=20 class="td" align=right><%=replaceNull(ssd.volL)%></td>
								<td width="8%" height=20 class="td" align=right><%=replaceNull(ssd.volH)%></td>
								<td width="8%" height=20 class="td" align=right><%=replaceNull(ssd.volW)%></td>
								<td width="8%" height=20 class="td" align=right><%=replaceNull(ssd.weightVolumetric)%></td>
								<td width="16%" height=20 class="td" align=right>$<%=replaceNull(ssd.importe)%></td>
								<td width="2%" height=20><input type=radio
									name="radioselect" value="<%=i%>" onclick="selectRenglon()"></td>
							</tr>
							<%
								}
								for (int j = i; j < 6; j++) {
							%>
							<tr class="tablaDetallev2Body" >
								<td width="5%" height=20 class="td">&nbsp;</td>
								<td width="10%" height=20 class="td">&nbsp;</td>
								<td width="43%" height=20 class="td">&nbsp;</td>
								<td width="8%" height=20 class="td">&nbsp;</td>
								<td width="8%" height=20 class="td">&nbsp;</td>
								<td width="6%" height=20 class="td">&nbsp;</td>
								<td width="8%" height=20 class="td">&nbsp;</td>
								<td width="8%" height=20 class="td">&nbsp;</td>
								<td width="8%" height=20 class="td">&nbsp;</td>
								<td width="8%" height=20 class="td">&nbsp;</td>
								<td width="8%" height=20 class="td">&nbsp;</td>
								<td width="8%" height=20 class="td">&nbsp;</td>
								<td width="17%" height=20 class="td">&nbsp;</td>
								<td width="2%" height=20><input type=radio
									name="radioselect" disabled="disabled"></td>
							</tr>
							<%
								}
							%>							
						</tbody>
					</table>
					</fieldset>	
				</td>
			</tr>
			<tr>
				<td width="73%" >
					<table width="99%" cellspacing="0" cellpadding="0">
						<tr >
							<td width="37%" >&nbsp;</td>
							<td width="14%">
								<div align="center">
									<html:button property="editbutton" value="Editar" styleClass="button2 buttonMedium" onclick="editRow(this);" />
								</div>
							</td>
							<td width="49%">
								<html:button property="deletebutton" value="Borrar" styleClass="button2 buttonMedium" onclick="deleteRow(this)" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr align="center">
				<td align="center">
					<table border="0" align=center cellspacing="0" cellpadding="0" width="95%">
						<logic:present name="errormsgentrega" scope="request">
							<tr>
								<td align="center">
									<font style="font-size: 10pt; color: #ff0000; font-weight: bold">
										<bean:write name="errormsgentrega" />
									</font>
								</td>
							</tr>
						</logic:present>
						<logic:present name="errormsgcod" scope="request">
							<tr>
								<td align="center">
									<font style="font-size: 10pt; color: #ff0000; font-weight: bold">
										<bean:write name="errormsgcod" />
									</font>
								</td>
							</tr>
						</logic:present>
						<logic:present name="erroravailcred" scope="request">
							<tr>
								<td align="center">
									<font style="font-size: 10pt; color: #ff0000; font-weight: bold">
										<bean:write name="erroravailcred" />
									</font>
								</td>
							</tr>
						</logic:present>
						<logic:present name="errormsgstatus" scope="request">
							<tr>
								<td align="center">
								<font style="font-size: 10pt; color: #ff0000; font-weight: bold">
										<bean:write name="errormsgstatus" />
								</font></td>
							</tr>
						</logic:present>
						<logic:present name="errMsgAdditional" scope="request">
							<tr>
								<td align="center">
									<font style="font-size: 10pt; color: #ff0000; font-weight: bold">
										<bean:write name="errMsgAdditional" />
									</font>
								</td>
							</tr>
						</logic:present>
						<logic:present name="errMsgBrncVrtl" scope="request">
							<tr>
								<td align="center">
									<font style="font-size: 10pt; color: #ff0000; font-weight: bold">
										<bean:write name="errMsgBrncVrtl" />
									</font>
								</td>
							</tr>
						</logic:present>
						<logic:present name="errorMsg" scope="request">
							<tr>
								<td align="center">
									<font style="font-size: 10pt; color: #ff0000; font-weight: bold">
											<bean:write name="errorMsg" />
									</font>
								</td>
							</tr>
						</logic:present>
					</table>
				</td>
			</tr>
			<tr>
				<td width="73%"><strong>Servicios</strong></td>
			</tr>
			<tr>
				<td>					 
					<table width="99%" border="0" cellspacing="0" cellpadding="0" >
						<tr>
							<td height="200">
								<table  align=center border="0" cellpadding="0" cellspacing="0" width="100%">
									<tr>
										<td width="71%">
											<table border="0" cellpadding="0" cellspacing="1" width="100%">
												<tr><td colspan=6>&nbsp;</td></tr>
												<tr>
													<td width="19%"><p align="right">Forma de Pago</p></td><!-- AAP04 -->
													<td width="1%">&nbsp;</td>
													<td width="50%">
														<html:select size="1" property="formaPago" onchange="validacionesFXC(this, 'TO_PAY');" disabled="true"><!-- AAP04 -->
															<html:option value="PAID" >PAGADA</html:option><!-- AAP04 -->
															<html:option value="TO_PAY">POR COBRAR</html:option><!-- AAP04 -->
														</html:select><!-- AAP04 -->
													</td>
													<td width="11%"><div align="right">Flete</div></td>
													<td width="0%">&nbsp;</td>
													<td width="20%" align=right class="td" height="21" id="importeFlete"><%=flete%></td>
												</tr>
												<tr>
													<td width="19%"><div align="right">Entrega</div></td>
													<td width="1%">&nbsp;</td>
													<td width="11%">
														<html:select size="1" property="entrega" onchange="return changeBranch(this);">
															<html:option value="1">OCURRE</html:option>
															<html:option value="2">EAD</html:option>
														</html:select>
														<label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
														<label>Opciones Ocurre:</label>
														<html:checkbox property="opcOcurre" onchange="changeDeliveryOcurre('')" disabled="true"/>
														<html:select property="brnchOcurre" disabled="true" onchange="changeSelectedAddr(document.forms[0].brnchOcurre.value)">
															<%if (aform.getOpcOcurre() && aform.getBrnchOcurre().length() > 0){ %>
																<option value="<%=aform.getBrnchOcurre()%>" selected="selected"><%=aform.getBrnchOcurre().contains("|") ? aform.getBrnchOcurre().split("\\|")[4] : "" %></option>													
															<%}
																if (aform.getEntrega().equals("1")){
																	servicesDetailArray = (ArrayList) session.getAttribute("servicesDetail");
																	if (servicesDetailArray != null && servicesDetailArray.size() > 0) {
																		for (BranchDetailDTOResponse sucursal : aform.getFilteredBrnch()) {
																			addr = "";
																			if (aform.getOpcOcurre()){
																				addr = sucursal.calle + " " + sucursal.numero + " " + sucursal.colonia + " "+ sucursal.ciudad + " C.P. " + sucursal.codigoPostal;
																				addr = Normalizer.normalize(addr, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
																				String selectData = sucursal.clave.concat("|").concat(sucursal.estado).concat("|").concat(addr).concat("|").
																						concat(sucursal.localizaLatitud).concat("%2C").concat(sucursal.localizaLongitud).concat("|").
																						concat(sucursal.nombre + " " + sucursal.clave.substring(3, 5));
																				if (aform.getBrnchOcurre().equalsIgnoreCase("")){
															%>						<option title="<%=addr%>" value="<%=selectData%>"><%=sucursal.nombre + " " + sucursal.clave.substring(3, 5) %></option>
															<%					} else if (aform.getBrnchOcurre().length() > 0 && aform.getBrnchOcurre().contains("|") && !aform.getBrnchOcurre().split("\\|")[4].equalsIgnoreCase(sucursal.nombre + " " + sucursal.clave.substring(3, 5))){
															%>						<option title="<%=addr%>" value="<%=selectData%>"><%=sucursal.nombre + " " + sucursal.clave.substring(3, 5) %></option>					
															<%					}
																			}else{break;}
																		}
																	}
																}
															%>
														</html:select>
														
														<a id="btnSucursales" hidden="hidden" class="buttonMedium tagButton" title="Ver sucursales" onclick="searchBrnch(document.forms[0].brnchOcurre.value)"><i class="fa fa-map-marker"></i></a>
													</td>
													<td width="11%"><div align="right">Descuento</div></td>
													<td width="0%">&nbsp;</td>
													<td width="20%" align=right class="td" height="21" id="importeDescuento"><%=descuento%></td>
												</tr>
												<tr>
													<td width="1%">&nbsp;</td>
													<td width="1%">&nbsp;</td>
													<td width="50%" align="left"><a id="addr" onclick="brnchOnMap(document.forms[0].brnchOcurre.value)" style="text-decoration: underline; color: #2196F3"></a></td>
												</tr>
												<tr>
													<td width="19%"><p align="right">Acuse de Recibo</p></td>
													<td width="1%">&nbsp;</td>
													<td width="50%">
														<html:select size="1" property="acusederecibo" onchange="valAckCliente(this,'ACK-C')">
															<html:options property="ackvalue" labelProperty="acklabel" />
														</html:select>
													</td>
													<td width="11%"><div align="right">Servicios</div></td>
													<td width="0%">&nbsp;</td>
													<td width="20%" align=right class="td" height="21" id="importeServicios"><%=servicios%></td>
												</tr>
												<tr>
													<td width="19%"><div align="right">Valor COD</div></td>
													<td width="1%">&nbsp;</td>
													<td width="50%">
														<p align="left">
														
														<div class="inputV2">
														  <div class="group">       
															 <html:text property="valorcod" size="16" styleClass="text" onblur="isNumber22(this);setBack('true');" onfocus="if (!this.readOnly) setBack('false');"/>
																												      
														      <span class="highlight"></span>
														      <span class="bar" style="width: 90px;"></span>
														   </div>
													    </div>
														</p>													
													</td>
													<td width="11%"><div align="right">Sub-Total</div></td>
													<td width="0%">&nbsp;</td>
													<td width="20%" align=right class="td" height="21" id="importeSubTotal"><%=subTotal%></td>
												</tr>
												<tr>
													<td width="19%"><p align="right">Valor Declarado</p></td>
													<td width="1%">&nbsp;</td>
													<td width="50%"><html:text property="valordeclarado" styleClass="text" onkeypress="return formatoPesos(event,this);" onchange="submitFormValorDeclarado(this)" onblur="setBack('true');" onfocus="if (document.forms[0].valorvalue.value != 'N') setBack('false');"/></td>
													<td width="11%"><div align="right">I.V.A</div></td>
													<td width="0%"></td>
													<td width="20%" align=right class="td" height="21" id="importeIva"><%=iva%></td>
												</tr>
												<tr>
													<td width="19%"><div align="right">Cobertura</div></td>
													<td width="1%">&nbsp;</td>
													<td width="50%">
														<div align="left">
															<html:select size="1" property="cobertura" onchange="setSeguro()" onclick="getIndiceActual()">
																<html:options property="insurancetype" labelProperty="insurancetypelabel" />
															</html:select>
														</div>
													</td>
													<td width="11%"><div align="right">I.V.A. Ret.</div></td>
													<td width="0%">&nbsp;</td>
													<td width="20%" align=right class="td" height="21" id="importeIvaRet"><%=ivaRet%></td>
												</tr>
												<tr>
													<td width="19%"><div align="right">Informacion del Seguro</div></td>
													<td width="1%">&nbsp;</td>
													<td width="50%"><html:textarea property="seguro" style="HEIGHT: 20px; WIDTH: 261px" readonly="true" /></td>
													<td width="11%"><div align="right">Total</div></td>
													<td width="0%">&nbsp;</td>
													<td width="20%" align=right class="td" height="21" id="importeTotal"><%=total%></td>
												</tr>
												<!--Code added by  palanivel  For Displaying Additional Service Lov  -->
												<%
													if (aform != null && aform.getShowAdditional() != null
														&& aform.getShowAdditional().equalsIgnoreCase("Y")) {
												%>
												<tr>
													<td width="11%" align="right" valign="top">Servicios adicionales</td>
													<td width="0%">&nbsp;</td>
													<td>
														<html:text property="serviceAdditional" styleClass="text" readonly="true" onclick="openLov('service')" /> 
														<html:button property="servicebut" style="WIDTH:35px" value="..." styleClass="button1" onclick="openLov('service')" />
														&nbsp;Importe 
														<html:text property="importeValue" styleClass="text" readonly="true" onkeypress="return formatoPesos(event,this);" 
														onblur="setBack('true');" onfocus="setBack('false');" maxlength="10"/> 
														<html:button property="AgregarServ" styleClass="bookbutton" value="Agregar" onclick="submitFormServicesAditional(this)" />
													</td>
												</tr>
												<tr>
													<td width="1%">&nbsp;</td>
													<td colspan=2 align="left">
														<table>
															<tr>
																<td width="52%">Servicio adicional</td>
																<td width="48%" align="left">Importe</td>
																<td width="20%">&nbsp;</td>
															</tr>
														</table>
													</td>
												</tr>
												<tr>
													<td width="1%">&nbsp;</td>
													<td colspan=2 align="left">
														<div style='width: 300; height: 80; overflow: auto; border: 0 solid black' align="center">
															<table id="tablaAdicionales">
															<%
																size = 0;
																servicesDetailArray = (ArrayList) session.getAttribute("aditionalServicesDetail");
																if (servicesDetailArray != null) {
																	size = servicesDetailArray.size();

																	for (i = 0; i < size; i++) {
																		AdditionalService serviceRecordsRecs = (AdditionalService) servicesDetailArray.get(i);
															%>
																<tr>
																	<td width="60%" height=20 class="td"><%=serviceRecordsRecs.service%></td>
																	<td width="60%" height=20 class="td" align="right"><%=serviceRecordsRecs.importe%></td>
																	<td width="10%">
																		<a href="javascript:doDelete('<%=i%>')">
																			<img src='images/delete.jpg' width="17" border="0" height="17">
																		</a>
																	</td>
																</tr>
															<%
																	}

																} 
																for (i = size; i < 3; i++) {
															%>
																<tr>
																	<td width="60%" height=20 class="td"></td>
																	<td width="60%" height=20 class="td"></td>
																	<td width="10%"><img src='images/delete.jpg' width="17" border="0" height="17"></td>
																</tr>
															<%
																}
															%>
															</table>
														</div>
													</td>
												</tr>
												<%
													}
												%>
												<tr>
													<td>
														<!-- Removed GUIA NO field row and added it as hidden field on 25-11-2010-->
														<html:hidden property="guiano" />
														<div align="right">Comentarios</div>
													</td>
													<td>&nbsp;</td>
													<td colspan=4>
													
														<div class="inputV2">
														  <div class="group">       
															 <html:text styleClass="comment" maxlength="255" property="comments" onblur="setBack('true');" onfocus="setBack('false');" onkeyup="validChar(this,'special')"/>
																												      
														      <span class="highlight"></span>
														      <span class="bar"></span>
														   </div>
													    </div>	
													    													
													</td>
												</tr>
												<tr>
													<td><div align="right">Referencia</div></td>
													<td>&nbsp;</td>
													<td colspan=4>
													 
														<html:text styleClass="comment" maxlength="65" property="reference" onblur="setBack('true');" onfocus="setBack('false');" onkeypress="return add_Key_li(this.value);"/>
														<input type="button" onclick="return add_li_submit(document.forms[0].reference.value);" value="Añadir referencia" class="bookbutton buttonLarge">
													</td>
												</tr>
												<tr>
													<td></td>
													<td></td>
													<td colspan=3 class='td'>
														<div id="dataRefdiv" class="divReference">
															<dl id="listaDesordenada" style="width: 100%; margin: 0px; padding: 0px;" >																
	        												</dl>
														</div>
													</td>
													<td></td>
												</tr>
												<tr>
													<td><div align="right">Número de Pedimento</div></td>
													<td>&nbsp;</td>
													<td colspan=2>
														<html:text styleClass="textcontenido" property="pedinumber" onblur="setBack('true');" onfocus="setBack('false');" style="width: 30%;"/>
														<label>&nbsp;Agente Aduanal&nbsp;</label>
														<html:text styleClass="textcontenido" property="custagent" onblur="setBack('true');" onfocus="setBack('false');" style="width: 30%;"/>
													</td>
												</tr>
												<tr>
													<td colspan=6>
														<div class="divButtons">
														
															<html:button property="Calculate" value="Calcular" styleClass="button buttonMedium" onclick="generateGuia('calculate')" />
															<html:button property="generale" styleClass="bookbutton buttonMedium" value="Generar" onclick="generateGuia('generate')" />
															<html:button property="servicios" styleClass="bookbutton buttoneExtraLarge" 
															value="Servicios Adicionales" onclick="showAditionalServices()" />
														</div> 
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>					
					<div id="PantAdicionales" style="visibility: hidden; position: absolute; top: 450px; left: 200px; ">
						<jsp:include page="JavWebBookingGeneralMainServicesAditional.jsp" flush="false" />
					</div>
					<div>
						<html:hidden property="destinationaddresscode" />
						<html:hidden property="destinationcoloniacode" />
						<html:hidden property="citycode" />
						<html:hidden property="fiscaladdresscode" />
						<html:hidden property="orgionaddresscode" />
						<html:hidden property="borderbranchcheck" />
						<html:hidden property="ship_seqn" />
						<html:hidden property="no_ship" />
						<html:hidden property="cur_loc" />
						<html:hidden property="cur_dest" />
						<html:hidden property="lc_rout" />
						<html:hidden property="shiperrmsg" />
						<html:hidden property="getycode" />
						<html:hidden property="getylevl" />
						<html:hidden property="getytype" />
						<html:hidden property="destinationzipcode" />						
						<html:hidden property="orgioncode"/>
						<html:hidden property="orginsite"/>
						<html:hidden property="orginbranchcode"/>
						<html:hidden property="orgionbranch"/>
						<html:hidden property="orgien1"/>
						<html:hidden property="orgien2"/>
						<html:hidden property="orgiencolonia1"/>
						<html:hidden property="orgiencolonia2"/>
						<html:hidden property="orgienrfc"/>
						<html:hidden property="orgientelefono"/>
						<html:hidden property="fiscal1" />
						<html:hidden property="fiscal2" />
						<html:hidden property="fiscalcolonia1" />
						<html:hidden property="fiscalcolonia2" />
						<html:hidden property="fiscaltelefono" />
						<input type="hidden" name="changeCCosto">
						<html:hidden property="changeShippingType" />
						<html:hidden property="changeShippingTypeValorOLD" />
						
						<!-- hiddens de captura de detalle -->
						<input type="hidden" name="actionfordup">
						<html:hidden property="tarifType" />
						<html:hidden property="ss_srvc_id" />
						<html:hidden property="ss_refr_srvc_id" />
						<html:hidden property="descripcioncode" />
						<html:hidden property="specialTariff" />
						<html:hidden property="accion" />
						<html:hidden property="defaultservicescreen" />
						<html:hidden property="defaultservicescreenKm" /><!-- AAP08 -->
						<html:hidden property="srvcConfigHasAmntC" />	
						<html:hidden property="hitCount" />
						<html:hidden property="calculationdone" />
						<html:hidden property="pesoDB" />
						<html:hidden property="volumenDB" />
						<html:hidden property="displayAmountFlag" />
						<html:hidden property="volLMAXSEG" /><!--JSA01 VARIABLES PARA EL SERVICIO EXPRESS -->
						<html:hidden property="volWMAXSEG" /><!--JSA01 VARIABLES PARA EL SERVICIO EXPRESS -->
						<html:hidden property="volHMAXSEG" /><!--JSA01 VARIABLES PARA EL SERVICIO EXPRESS -->
						<html:hidden property="pesoMAXSEG" /><!--JSA01 VARIABLES PARA EL SERVICIO EXPRESS -->
						<html:hidden property="volLMAXSEGSobre" /><!--JSA01 VARIABLES PARA EL SERVICIO EXPRESS -->
						<html:hidden property="volWMAXSEGSobre" /><!--JSA01 VARIABLES PARA EL SERVICIO EXPRESS -->
						<html:hidden property="volHMAXSEGSobre" /><!--JSA01 VARIABLES PARA EL SERVICIO EXPRESS -->
						<html:hidden property="pesoMAXSEGSobre" /><!--JSA01 VARIABLES PARA EL SERVICIO EXPRESS -->
						<html:hidden property="isShippingTypeSEG" /><!--JSA01 VARIABLES PARA SABER SI EL TIPO DE ENVIO ES EXPRESS -->
						<html:hidden property="shippingTypeSEGActive" /><!--JSA01 VARIABLES OBTENER CUALES SON LOS SERVICIOS TIPO EXPRESS -->
						<html:hidden property="factorDividorPesoVol" /><!--JSA01 VARIABLE PARA REALIZAR CALCULO DEL PESO VOLUMETRICO LADO DEL CLIENTE -->
						<html:hidden property="cantPesoVolDecimales" /><!--JSA01 VARIABLE PARA TRUNCAR EL CALCULO DEL PESO VOLUMETRICO -->
						<html:hidden property="forceCaptureDimensions" /><!--JSA03 VARIABLE PARA SABER SI SE FORZA A CAPTURAR LAS DIMENSIONES -->
						<html:hidden property="volMAX" /><!--JSA03 VARIABLE PARA EL TOPE DEL VOLUMEN EN LO STANDARD -->
						<html:hidden property="volMAXSobre" /><!--JSA03 VARIABLE PARA EL TOPE DEL VOLUMEN EN LO STANDARD PARA SOBRE-->
						<html:hidden property="volLMinPaq" />
						<html:hidden property="volWMinPaq" />
						<html:hidden property="volHMinPaq" />
						<html:hidden property="wghtMinPaq" />
						<html:hidden property="volMinPaq" />
						<html:hidden property="volLMinEnv" />
						<html:hidden property="volWMinEnv" />
						<html:hidden property="volHMinEnv" />
						<html:hidden property="wghtMinEnv" />
						<html:hidden property="volMinEnv" />
						
						<!-- calculo de servicios -->
						<html:hidden property="serviceshitcount" />
						<html:hidden property="duplicateguianumber" />
						<html:hidden property="successmessage" />
						<html:hidden property="confirmgenerate" />
						<html:hidden property="errmsgenvelope" />
						<html:hidden property="showAdditional" />
						<html:hidden property="referenceId" />
						<html:hidden property="zonaExtendida" />
						<html:hidden property="tarifaInvalida" />
						<html:hidden property="tarifaInvalidaZp" />
						<html:hidden property="accionServices" />
						<html:hidden property="clasifTarif" /><!-- AAP02 -->
						<html:hidden property="montoMaxOl" /><!-- AAP03 -->
						<html:hidden property="checkRefDir" /><!-- AAP03 -->
						<html:hidden property="checkTelDir" /><!-- AAP03 -->
						<html:hidden property="reqAcuse" /><!-- AAP03 -->
						<html:hidden property="reqAcuseXT"></html:hidden>
						<html:hidden property="allowedFXC" /><!-- AAP04 -->
						<html:hidden property="permiteDestino" /><!-- AAP04 -->
						<html:hidden property="isSoloSobre" /><!-- AAP04 -->
						<html:hidden property="eMailOrigBD" /><!-- AAP05 -->
						<html:hidden property="eMailDestBD" /><!-- AAP05 -->
						<html:hidden property="sendeMailOrigBD" /><!-- AAP05 -->
						<html:hidden property="sendeMailDestBD" /><!-- AAP05 -->						
						<html:hidden property="destinationcodeIni" /><!-- AAP06 -->
						<html:hidden property="destinationbranchIni" /><!-- AAP06 -->
						<html:hidden property="listReferences" /><!-- AAP07 -->			
						<html:hidden property="brncVrtl" />
						<html:hidden property="hasEnvelope" /><!-- AAPXX -->
						<html:hidden property="flagValidRefrClnt" />
						
						<input type="hidden" name="aditionalFlag" value=<%=aditionalFlag%>>
						<input type="hidden" name="valorvalue" value=<%=valorvalue%>>
						<input type="hidden" name="indiceActual">
						<html:hidden property="radioselectCurrent"/><!-- JSA02 SET RADIOBUTTON CUANDO SE EDITA--> 
						
						<!-- Sucursal default addr -->
						<html:hidden property="defaultBrnchAddr" />
						
						<!-- Documentación de guías T7 con peso = 0 -->
						<html:hidden property="tarifDefaultChck"></html:hidden>

						<!-- Propiedades para Complemento Carta Porte -->
						<html:hidden property="productIdSat" />
						<html:hidden property="errMsgAdditional" />
						<html:hidden property="flagValidProductId" />
						
						<!-- Captura de Datos Fiscales -->
						<html:hidden property="validFiscal" />
						
						<html:hidden property="maxDeclAmnt" />
						
						<!-- Captura de Número de Pedimento -->
						<html:hidden property="errMsgPediNum" />
						
						<!-- Captura si el cliente tiene destinada una sucursal origen -->
						<html:hidden property="flagCCAssignedBrnc" />
						
						<html:hidden property="validCCAddrCvge" />
						
						<!-- Documentación de RAD Zona Plus -->
						<html:hidden property="allowRadZe"></html:hidden>
						<html:hidden property="allowRadZeT7"></html:hidden>
						<html:hidden property="prevCCSelect"></html:hidden>
						<html:hidden property="centrosCostoDefault"></html:hidden>
						
						<html:hidden property="maxQtyPack"></html:hidden>
						<html:hidden property="msjShippingCbtr" />
					</div>
				</td>
			</tr>
			
			<tr>
			
				<td width="73%"><div id="divTitMail" style="visibility: visible;"><strong>Notificaciones por correo electrónico</strong> (Para envios a varias cuentas, favor de separar correos  con caracter ';')</div>
				</td>
			
			</tr>
			<tr>
				<td>
				<div id="divDetMail" style="visibility: visible;">
					<table table width="99%" border="0" cellpadding="0" cellspacing="0" >
						<tr>
							<td align="right">Notificación de Entrega</td>
							<td style="display: flex;">
				  							
								<html:checkbox property="eMailOrigCheck" onclick="cleanEmail(this, document.forms[0].eMailOrigText, document.forms[0].eMailOrigBD)"/>
								<html:text property="eMailOrigText" styleClass="textinput" size="95" maxlength="200" readonly="true" onblur="setBack('true');" onfocus="setBack('false');"/>
							</td>
						</tr>
						<tr>
							<td align="right">Notificación de Envio</td>
							<td style="display: flex;">
								<html:checkbox property="eMailDestCheck" onclick="cleanEmail(this, document.forms[0].eMailDestText, document.forms[0].eMailDestBD)"/>
								<html:text property="eMailDestText" styleClass="textinput" size="95" maxlength="200" readonly="true" onblur="setBack('true');" onfocus="setBack('false');"/>
							</td>
						</tr>					
					</table>
				</div>
				</td>
			</tr>
			</div>
		</table>			 
	 
	</html:form>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
</body>
</html:html>