<%@page import="java.util.ArrayList, java.text.Normalizer, java.util.LinkedHashMap, beanUtil.GetBrnchOcurre, paquetexpress.internal.prepaidcajas.conversion.form.ConversionForm, mx.com.paquetexpress.general.model.dto.BranchDetailDTOResponse"%>
<%@ page info="WebBooking - General"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%String version = "00.00.29";
  ConversionForm cform = (ConversionForm) session.getAttribute("invoiceConversionFormCajas");%>
<html>

<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">

<title>Conversion de rastreos Prepago.</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel=stylesheet media=screen type=text/css href=Styles.css?v=<%=version%>>
<script type="text/javascript" src="barraEspera.js?v=<%=version%>"></script>
<script type="text/javascript" src="GuiaConversionCajas.js?v=<%=version%>"></script>
<script type="text/javascript">
function focus() {
	if(<%=session.getAttribute("zoneFocus")%> == '1') {
		document.forms[0].destnombre.focus();
	}
}
function descargaFactura(params) {	
	//alert('GenCartaPorte?trackingNoGen='+document.forms[0].trackingNoGen.value);
	link ='GetFacturaGenerada?'+params;
	window.open(link, '_blank');
}

	function goOut(page) {
		document.forms[0].action = "webBookinggeneralMain.do?includeattribute=yes&page="
				+ page;
		document.forms[0].submit();

	}
</script>

<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<link rel="stylesheet" media="screen" type="text/css" href="webbookingMonitor.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css" href="webbooking.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css" href="css/font-awesome.min.css?v=<%=version%>">
<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">

<script type="text/javascript" src="js/jquery/1.12.0.js?v=<%=version%>"></script>
<script type="text/javascript" src="js/v2/global.js?v=<%=version%>"></script>
<script type="text/javascript" src="js/ToastNotification.js?v=<%=version%>"></script>
</head>


<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onpageshow="init();" onunload="clearGuias();" class="backgroundStandard guiaConversionMargen">
<html:form name="invoiceConversionFormCajas"
	type="paquetexpress.internal.prepaidcajas.conversion.form.ConversionForm"
	action="guiaconversioncajas">
	<jsp:include page="nocache.jsp" flush="false" />
	
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
		
		<div class="MenuOverlay displayNone" style="z-index:1;">
		
	        <a class="btnCloseMenu" onclick="hideMenu()">
	            <div>
	                <span class="lineOne"></span>
	                <span class="lineTwo"></span>
	            </div>
 	        </a>
  
  			<div class="itemsMenu marginAutoCentro bodyWidth">
  	 			<a onclick="goOut('mainpage');hideMenu();" style="">Menú principal</a>
  	 			
				<logic:present name="admin">
					<a onclick="changebranch();hideMenu();" style="">Cambiar de sucursal</a> 
				</logic:present>   	 			
				
				<a onclick="Regresar();hideMenu();" style="">Regresar</a>
			   	<logic:present name="loginForm">
			   		<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion --> 		 			
						<a onclick="hideMenu();logout();" style="">Cerrar sesión</a>
					</logic:equal>
				</logic:present>		
			</div>
 			
		</div>		
	
	<table width="100%" border="1" cellspacing="0" cellpadding="0" class="bodyWidth marginAutoCentro" >
		<logic:present name="error">
			<table width="100%" border="1" cellspacing="0" cellpadding="0"
				ALIGN="CENTER">
				<tr>
					<td class="LabelError" align="center">
						<bean:write name="error"/>					
					</td>
				</tr>								
			</table>
		</logic:present>
		<logic:present name="invoiceConversionFormCajas" property="facturaMod">
			<logic:notEqual name="invoiceConversionFormCajas" property="facturaMod" value="">			
			<table width="100%" border="1" cellspacing="0" cellpadding="0" ALIGN="CENTER">				
				<tr>
					<td align="center" class="LabelError">
						Factura Generada<br>												
						<a href="javascript:descargaFactura('<bean:write name="invoiceConversionFormCajas" property="facturaMod"/>');" class="linkFact">
			           		Descargar Factura
			            </a>
					</td>
				</tr>				
			</table>
			</logic:notEqual>
		</logic:present>
		<table class="bodyWidth marginAutoCentro">
			<tr>
				<td>
				   <h2 class="titleSection fontBold">Documentar Rastreo de Prepago de Cajas y Tarimas</h2>
				</td>
			</tr>
			
			<tr>
	 			<td>
			<div>
				 <strong><font color="#000066"
						size="2" face="Arial">Nombre&nbsp;&nbsp;</font></strong> 
										
				<font color="#990000" size="2" face="Verdana">
					<logic:present name="sGlobal">
						<bean:write name="sGlobal" property ="origenUserNombre"/>											
					</logic:present>
				</font>
			 </div>		
			 
			 <div>
			 	<strong><font color="#000066"
					size="2" face="Arial">Sucursal&nbsp;</font></strong>

			<font color="#990000" size="2" face="Verdana">
			     <logic:present
					name="branchid">
					<bean:write name="branchName" />
				</logic:present>
			</font>
			
			</div>
			</td>
		 </tr>
	 		</table>	
			<table align="center">              	
				<tr>
					<td width="410" class="LabelForm" valign="bottom"> Centro de costo:
					 	<html:select property="centrosCosto" styleClass ="textinputmedium" onchange="cambioCentroCosto();"><!-- AAP05 -->
							<html:options property="centrosCostoValue" labelProperty="centrosCostoLabel" /><!-- AAP05 -->									
						</html:select><!-- AAP05 -->
					</td>
					<td class="LabelForm" valign="bottom" align="right" style="width: 180;visibility: hidden;"></td>
					<td width="120" style="visibility: hidden;">
						<html:hidden property="trackingNo"/>
						<html:hidden property="trackingNoEnd"/>					
								<%
								ArrayList detalleGuias = (ArrayList)request.getAttribute("detalleGuias");
								int height = 0;
								if (detalleGuias != null) {
									height = detalleGuias.size();
									if (detalleGuias.size() <=4) {
										height = 13 * detalleGuias.size(); 
									} else {
										height = 65;
									}
								}
								%>
						<div style="height: <%=height%>pt; width:120; overflow: auto;" id="DivMainContent02" >
							<table id="detalleGuias">							
								<%								
								if (height > 0) {																						
									for (int i = 0; i < detalleGuias.size(); i++) {
								%>
									<tr id="tablerow<%=i%>">											
										<td align="right" class="LabelForm"><%=detalleGuias.get(i).toString()%></td><!-- guia -->											
									</tr>														
								<% 			
									}
									
								}
								%>
							</table>
						</div>
					</td>
					
					<!--td><input type="button" VALUE="Ver Rastreo"
						onclick="loaddetail();" class="buttonnormal" colspan="3"
						styleClass="width: 48; height: 22"></td-->
				</tr>
			</table>
			<logic:present name="shippingtypes">
				<logic:present name="origin">
				<table align="center">
				<tr>
				<td>
				<div style="float:left; padding-top:5px;">Cliente: </div>
<!-- 					<p align="center" class="LabelForm" valign="top">Cliente:</p> -->
					<div class="inputV2" width="100" style="float:right">
					<div class="group">       
						<html:text property="clientId" styleClass="textinput" />
																						      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
					</div>
					</td>
					
					<td>
					<div style="float:left; padding-top:5px;">Nombre: </div>
					<div class="inputV2" width="100" style="float:right">
					<div class="group">       
						<html:text property="clientName" styleClass="textboxbig" />
																						      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
					</div>
					</td>
					<td align="right">&nbsp;&nbsp;
					<input type="button" VALUE="Cliente" onclick="openLov('sipwebclientonly');" class="buttonnormal buttonLarge" colspan="3" styleClass="width: 48; height: 22">
					</td>
				</tr>
				</table>
				</logic:present>
				<logic:notPresent name="origin">
				<table align="center">
				<tr>
				<td>
				<div style="float:left; padding-top:5px;">Cliente: </div>
<!-- 					<p align="center" class="LabelForm" valign="top">Cliente:</p> -->
					<div class="inputV2" width="100" style="float:right">
					<div class="group">       
						<html:text property="clientId" styleClass="textinput"/>
																						      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
					</div>
					</td>
					
					<td>
					<div style="float:left; padding-top:5px;">Nombre: </div>
					<div class="inputV2" width="100" style="float:right">
					<div class="group">       
						<html:text property="clientName" styleClass="textboxbig" />
																						      
						<span class="highlight"></span>
						<span class="bar" ></span>
					</div>
					</div>
					</td>
					<td align="right">&nbsp;&nbsp;
					<input type="button" VALUE="Cliente" onclick="openLov('sipwebclientonly');" class="buttonnormal buttonLarge" colspan="3" styleClass="width: 48; height: 22">
					</td>
				</tr>
				</table>
				
						
						
				</logic:notPresent>
				
				<table class="bodyWidth marginAutoCentro" >
					<tr>
					<td >
					<fieldset class=""><legend >
					Datos Fiscales </legend>
					<table  height="58">
						<tr>
<!-- CALLE NUEVO -->
							<td align=right width="205" height="25">
								<span>Calle:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="fiscal1" styleClass="textinput" readonly="true"/>
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /CALLE NUEVO -->
<!--  CALLE VIEJO -->
<!-- 							<td width="205" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Calle: -->
<!-- 							</td> -->
<!-- 							<td width="150" height="25"> -->
<%-- 								<html:text property="fiscal1" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /CALLE VIEJO -->
<!-- NÚMERO NUEVO -->
							<td align=right width="104" height="25">
								<span>Número:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="fiscal2" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>

<!-- /NÚMERO NUEVO-->
<!-- NÚMERO VIEJO -->
<!-- 							<td width="104" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Número: -->
<!-- 							</td> -->
<!-- 							<td width="179" height="25"> -->
<%-- 								<html:text property="fiscal2" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- NÚMERO VIEJO -->
<!-- RFC NUEVO -->
							<td align=right width="164" height="25">
								<span>R.F.C.:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="fiscalrfc" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /RFC NUEVO -->
<!-- RFC VIEJO -->
<!-- 							<td width="164" height="25" class="LabelForm"> -->
<!-- 								<p align="right">&nbsp;&nbsp;&nbsp;&nbsp;R.F.C:</p> -->
<!-- 							</td> -->

<!-- 							<td width="247" height="25" class="LabelForm"> -->
<%-- 								<html:text property="fiscalrfc" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /RFC VIEJO -->
							<td width="50" height="25" />
						</tr>
						<tr>
<!-- TELÉFONO NUEVO -->
							<td align=right width="181" height="25">
								<span>Teléfono:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="fiscaltelefono" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /TELÉFONO NUEVO -->
<!-- TELÉFONO VIEJO -->
<!-- 							<td width="181" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Teléfono: -->
<!-- 							</td> -->
<!-- 							<td width="148" height="25"> -->
<%-- 								<html:text property="fiscaltelefono" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /TELÉFONO VIEJO -->
<!-- CIUDAD NUEVO -->
							<td align=right width="104" height="25">
								<span>Ciudad:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="fiscalcolonia1" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /CIUDAD NUEVO -->
<!-- CIUDAD VIEJO -->
<!-- 							<td width="104" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Ciudad: -->
<!-- 							</td> -->
<!-- 							<td width="179" height="25"> -->
<%-- 								<html:text property="fiscalcolonia1" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /CIUDAD VIEJO -->
<!-- COLONIA NUEVO -->
							<td align=right width="164" height="25">
								<span>Colonia:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="fiscalcolonia2" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /COLONIA NUEVO -->
<!-- COLONIA VIEJO -->
						
<!-- 							<td width="164" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Colonia: -->
<!-- 							</td> -->

<!-- 							<td width="247" height="25"> -->
<!-- 							<p align="left"> -->
<%-- 								<html:text property="fiscalcolonia2" styleClass="textinput" readonly="true" /> --%>
<!-- 							</p> -->
<!-- 							</td> -->
<!-- /COLONIA VIEJO -->							
							<td width="50" height="25">

							<logic:notPresent name="origin">
								<INPUT TYPE="BUTTON"
									name="fiscal" VALUE="Dirección Fiscal" class="buttonnormal buttonLarge"
									onclick="openLov('sipwebfiscaladdress');" colspan="3"
									styleClass="width: 55; height: 22">

							</logic:notPresent>
							</td>
								
<!-- 							<td width="50" height="25" /> -->
						</tr>
					</table>

					</fieldset>
					</td>
					</tr>
				</table>
				<table class="bodyWidth marginAutoCentro" >
					<tr>
					<td >
					<fieldset class=""><legend >
					Datos del Remitente </legend>

					<table  >
						<tr>
<!-- CALLE NUEVO -->
							<td align=right width="320" height="25">
								<span>Calle:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="orgien1" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /CALLE NUEVO -->
<!-- CALLE VIEJO -->
						
<!-- 							<td width="320" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Calle: -->
<!-- 							</td> -->
<!-- 							<td width="148" height="25"> -->
<%-- 								<html:text property="orgien1" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /CALLE VIEJO -->

<!-- NÚMERO NUEVO -->
							<td align=right width="140" height="25">
								<span>Número:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="orgien2" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /NÚMERO NUEVO -->
<!-- NÚMERO VIEJO -->						
<!-- 							<td width="140" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Número: -->
<!-- 							</td> -->
<!-- 							<td width="170" height="25"> -->
<%-- 								<html:text property="orgien2" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /NÚMERO VIEJO -->
<!-- RFC NUEVO -->
							<td align=right width="240" height="25">
								<span>R.F.C:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="orgienrfc" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /RFC NUEVO -->
<!-- RFC VIEJO -->
						
<!-- 							<td width="240" height="25" class="LabelForm"> -->
<!-- 								<p align="right">R.F.C:</p> -->
<!-- 							</td> -->
<!-- 							<td width="120" height="25" class="LabelForm"> -->
<!-- 								<p align="left"> -->
<%-- 								<html:text property="orgienrfc" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /RFC VIEJO -->	
							<td width="150" height="25" />
						</tr>
						<tr>
<!-- TELEFONO NUEVO -->
							<td align=right width="181" height="25">
								<span>Teléfono:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="orgientelefono" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /TELEFONO NUEVO -->
<!-- TELEFONO VIEJO -->
<!-- 							<td width="181" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Teléfono: -->
<!-- 							</td> -->
<!-- 							<td width="148" height="25"> -->
<%-- 								<html:text property="orgientelefono" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /TELEFONO VIEJO -->
<!-- CIUDAD NUEVO -->
							<td align=right width="125" height="25">
								<span>Ciudad:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="orgiencolonia1" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /CIUDAD NUEVO -->
<!-- CIUDAD VIEJO -->							
<!-- 							<td width="125" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Ciudad: -->
<!-- 							</td> -->
<!-- 							<td width="170" height="25"> -->
<%-- 								<html:text property="orgiencolonia1" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /CIUDAD VIEJO -->	
<!-- COLONIA NUEVO -->
							<td align=right width="171" height="25">
								<span>Colonia:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="orgiencolonia2" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>						
<!-- /COLONIA NUEVO -->
<!-- COLONIA VIEJO -->
<!-- 							<td width="171" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Colonia: -->
<!-- 							</td> -->
<!-- 							<td width="150" height="25"> -->
<%-- 								<html:text property="orgiencolonia2" styleClass="textinput" readonly="true" />								 --%>
<!-- 							</td> -->
<!-- /COLONIA VIEJO -->
							<td width="50" height="25">
							<logic:notPresent name="origin">
								<p align="right">
								<!-- 
								<INPUT TYPE="BUTTON" name="origin"
								VALUE="Origen" class="buttonorgin buttonMedium"
								onclick="openLov('sipweborginaddress');" colspan="3"
								styleClass="width: 55; height: 22">								
								 -->
							</logic:notPresent>
							</td>
						</tr>
					</table>

					</fieldset>
					</tr>
				</table>
				<table class="bodyWidth marginAutoCentro" >
					<td >
					<fieldset class=""><legend class="">
					Datos del Destinatario </legend>
					<table border="0" >
						<tr>
<!-- PLAZA DESTINO NUEVO -->
							<td align=right width="8%">
								<span>Plaza Destino:</span>
            				</td>
            				<td width="11%" align="right">
								<div class="inputV2">
									<div class="group">       
										<html:text property="destSite" styleClass="textsmall" onchange="clearDestData();"/>
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
							<td width="21%" align="right">
								<div class="inputV2">
									<div class="group">       
										<html:text property="destSiteName" styleClass="textinput" onchange="clearDestData();"/>
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /PLAZA DESTINO NUEVO -->
<!-- PLAZA DESTINO VIEJO-->						
<!-- 							<td width="8%" class="LabelForm" align="right">Plaza -->
<!-- 							Destino:</td> -->
<!-- 							<td width="11%" align="right"> -->
<%-- 								<html:text property="destSite" styleClass="textsmall" /> --%>
<!-- 							</td> -->
<!-- 							<td width="21%" align="right"> -->
<%-- 								<html:text property="destSiteName" styleClass="textinput" /> --%>
<!-- 							</td> -->
<!-- /PLAZA DESTINO VIEJO-->							
							<td width="11%">
								<INPUT TYPE="BUTTON" name="origin"
								VALUE="Plaza" class="buttonnormal buttonMedium" onclick="openLov('branchPP')"
								colspan="3" styleClass="width: 55; height: 22">
							</td>
<!-- CLIENTE NUEVO -->
							<td align=right width="17%">
								<span>Cliente:</span>
            				</td>
            				<td width="10%">
								<div class="inputV2">
									<div class="group">       
										<html:text property="destclave" styleClass="textsmall" onfocus="zoneverify();" onchange="clearDestData2();"/>
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
							<td width="35%" align="right">
								<div class="inputV2">
									<div class="group">       
										<html:text property="destnombre" styleClass="textinputmedium" onfocus="zoneverify();" onchange="clearDestData2();"/>
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /CLIENTE NUEVO -->
<!-- CLIENTE VIEJO -->
							
<!-- 							<td width="17%" class="LabelForm" align="right">&nbsp;Cliente:</td> -->
<!-- 							<td width="16%"> -->
<%-- 								<html:text property="destclave" styleClass="textsmall" onfocus="zoneverify();" /> --%>
<!-- 							</td> -->
<!-- 							<td width="51%"> -->
<%-- 								<html:text property="destnombre" styleClass="textinputmedium" onfocus="zoneverify();" /> --%>
<!-- 							</td> -->
<!-- /CLIENTE VIEJO -->							
							<td width="79%">
								<INPUT TYPE="BUTTON" name="origin"
								VALUE="Cliente" class="buttonnormal buttonMedium"
								onclick="openLov('clientwithsite')" colspan="3"
								styleClass="width: 55; height: 22">
							</td>
							<td width="1%" align="center">
								<html:button property="destinationclientBut" styleClass="button1 buttonMedium"
										value="Registrar" onclick="openLov('cliententry');" />
							</td>
						</tr>
					</table>

					<table  height="58">
						<tr>
<!-- CALLE NUEVO -->
							<td align=right width="275" height="25">
								<span>Calle:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="dest1" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /CALLE NUEVO -->
<!-- CALLE VIEJO -->					
<!-- 							<td width="275" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Calle: -->
<!-- 							</td> -->
<!-- 							<td width="148" height="25"> -->
<%-- 								<html:text property="dest1" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /CALLE VIEJO -->
<!-- NÚMERO NUEVO -->
							<td align=right width="105" height="25">
								<span>Número:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="dest2" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /NÚMERO NUEVO -->	
<!-- NUMERO VIEJO -->						
<!-- 							<td width="105" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Número: -->
<!-- 							</td> -->
<!-- 							<td width="170" height="25"> -->
<%-- 								<html:text property="dest2" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /NUMERO VIEJO -->
<!-- RFC NUEVO -->
							<td align=right width="160" height="25">
								<span>R.F.C:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="destrfc" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /RFC NUEVO -->
<!-- RFC NUEVO -->
<!-- 							<td width="160" height="25" class="LabelForm"> -->
<!-- 								<p align="right">R.F.C:</p> -->
<!-- 							</td> -->
<!-- 							<td width="120" height="25" class="LabelForm"> -->
<!-- 								<p align="right"> -->
<%-- 								<html:text property="destrfc" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /RFC VIEJO -->
							<td width="160" height="25" />
						</tr>

						<tr>
<!-- TELÉFONO NUEVO -->
							<td align=right width="275" height="25">
								<span>Teléfono:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="desttelefono" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /TELÉFONO NUEVO -->
<!-- TELÉFONO VIEJO -->						
<!-- 							<td width="275" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Teléfono: -->
<!-- 							</td> -->
<!-- 							<td width="148" height="25"> -->
<%-- 								<html:text property="desttelefono" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /TELÉFONO VIEJO -->
<!-- COLONIA NUEVO -->
							<td align=right width="105" height="25">
								<span>Ciudad:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="destcolonia1" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /COLONIA NUEVO -->
<!-- COLONIA VIEJO -->							
<!-- 							<td width="105" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Colonia: -->
<!-- 							</td> -->
<!-- 							<td width="170" height="25"> -->
<%-- 								<html:text property="destcolonia1" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /COLONIA VIEJO -->
<!-- CIUDAD NUEVO -->
							<td align=right width="160" height="25">
								<span>Colonia:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="destcolonia2" styleClass="textinput" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /CIUDAD NUEVO -->
<!-- CIUDAD VIEJO -->
						
<!-- 							<td width="160" height="25"> -->
<!-- 								<p align="right" class="LabelForm">Ciudad: -->
<!-- 							</td> -->
<!-- 							<td width="120" height="25"> -->
<!-- 								<p align="right"> -->
<%-- 								<html:text property="destcolonia2" styleClass="textinput" readonly="true" /> --%>
<!-- 							</td> -->
<!-- /CIUDAD VIEJO -->	
							<td width="160" height="25">
								<p align="right">
								<INPUT TYPE="BUTTON" name="origin" VALUE="Dirección" class="buttondir buttonLarge" onclick="openLov('destinationaddressPP')" colspan="3" styleClass="width: 55; height: 22">
							</td>
						</tr>
					</table>
					<table id="divRefDom" style="visibility: hidden; position: absolute; border: 0px; padding: 0px; border-spacing: 0px;"><!-- AAP02 -->					
						<tr>
<!-- REFERENCIA DE DIRECCION NUEVO -->
							<td align=right height="25">
								<span>Referencia de Dirección:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:textarea property="destRefDom" styleClass="textArea" cols="70" rows="2"/>
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /REFERENCIA DE DIRECCION NUEVO -->
<!-- REFERENCIA DE DIRECCION VIEJO-->
<!-- 							<td> -->
<!-- 								<p align="right" class="LabelForm">Referencia de Dirección: -->
<!-- 							</td> -->
<!-- 							<td> -->
<%-- 								<html:textarea property="destRefDom" styleClass="textArea" cols="70" rows="2"/> --%>
<!-- 							</td> -->
<!-- /REFERENCIA DE DIRECCION VIEJO-->							
						</tr>
					</table><!-- AAP02 -->					
					</fieldset>
				</table>
				<table id="msjeAvi" align="center">
					<tr>
						<td></td>
					</tr>
					<logic:present name="msjeDim">
						<tr>
							<td align="center" class="LabelMsjeAvi"><bean:write name="msjeDim" /></td>
						</tr>
					</logic:present>
				</table>
				<table class="bodyWidth marginAutoCentro">
					<tr>
					<td>
					<fieldset class="SubHead"><legend class="SubHead">
					Detalle de Rastreo </legend>

					<table class="marginAutoCentro">
						<tr>
							<td style="width:100%;">
								<table style="width:100%;">
									<tr>							
										<td class="LabelForm" align="left" width="8%">Entrega:</td>
										<td align="left" width="15%">
											<html:select property="deliveryType" multiple="true" size="3" styleClass="textinput" onchange="verifyDelvryType()">
												<html:options name="delvryTypeCode" labelName="delvryTypeDesc" />
											</html:select>
										</td>
										<td width="50%" class="LabelForm" align="left">Opciones Ocurre:
											<html:checkbox property="opcOcurre"  onchange="changeDeliveryOcurre('')" disabled="true"/>
											<html:select property="brnchOcurre" disabled="true" onchange="changeSelectedAddr(document.forms[0].brnchOcurre.value)">
												<%if (cform.getOpcOcurre() && cform.getBrnchOcurre().length() > 0){ %>
													<option value="<%=cform.getBrnchOcurre()%>" selected="selected"><%=cform.getBrnchOcurre().contains("|") ? cform.getBrnchOcurre().split("\\|")[4] : "" %></option>
												<%} %>
												<%
												if (cform.getDestcode() != "" && cform.getDeliveryType().equalsIgnoreCase("O")){
													for (BranchDetailDTOResponse sucursal : cform.getFilteredBrnch()) {
														String addr = sucursal.calle + " " + sucursal.numero + " " + sucursal.colonia + " "+ sucursal.ciudad + " C.P. " + sucursal.codigoPostal;
														addr = Normalizer.normalize(addr, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
														String selectData = sucursal.clave.concat("|").concat(sucursal.estado).concat("|").concat(addr).concat("|").
																concat(sucursal.localizaLatitud).concat("%2C").concat(sucursal.localizaLongitud).concat("|").
																concat(sucursal.nombre + " " + sucursal.clave.substring(3, 5));
														if (cform.getOpcOcurre()){
															if (cform.getBrnchOcurre().equalsIgnoreCase("")){
												%>				<option title="<%=addr%>" value="<%=selectData%>"><%=sucursal.nombre + " " + sucursal.clave.substring(3, 5) %></option>
												<%			} else if (cform.getBrnchOcurre().length() > 0 && cform.getBrnchOcurre().contains("|") && !cform.getBrnchOcurre().split("\\|")[4].equalsIgnoreCase(sucursal.nombre + " " + sucursal.clave.substring(3, 5))){
												%>				<option title="<%=addr%>" value="<%=selectData%>"><%=sucursal.nombre + " " + sucursal.clave.substring(3, 5) %></option>					
												<%			}
														}else{break;}
													}
												}
												%>
											</html:select>
											<a id="btnSucursales" hidden="hidden" class="buttonMedium tagButton" title="Ver sucursales" onclick="searchBrnch(document.forms[0].brnchOcurre.value)"><i class="fa fa-map-marker"></i></a>
										</td>
										
										<td class="LabelForm" align="right" width="20%">Generar una guía:</td>
										<td align="center" align="left" width="8%">
											<html:checkbox property="genMultiCaja"></html:checkbox>
										</td>
									
										<td class="LabelForm" align="left" width="10%">Rastreos:</td>
										<td class="LabelForm" align="left"><bean:write name="invoiceConversionFormCajas" property="contRastreos"/></td>
										
									</tr>	
									<tr>
										<td colspan="1" align="left">
											
										</td>
										<td colspan="3">
											<a id="addr" onclick="brnchOnMap(document.forms[0].brnchOcurre.value)" style="text-decoration: underline; color: #2196F3"></a>
										</td>
									</tr>					 
								</table>
							</td>
						</tr>			
						<tr>
							<td>
								<table style="width:100%">
									<tr>
									<td>
										<table>
											<tr>
<!-- ZONA NUEVO -->
							<td align=right height="25">
								<span>Zona:</span>
            				</td>
            				<td height="25" >
								<div class="inputV2">
									<div class="group">       
										<html:text property="zone" readonly="true"/>
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /ZONA NUEVO -->
<!-- ZONA VIEJO -->									
<!-- 										<td class="LabelForm" align="right">Zona:</td> -->
<!-- 										<td align="center"> -->
<%-- 											<html:text property="zone" styleClass="textinput" readonly="true" /> --%>
<!-- 										</td> -->
<!-- /ZONA VIEJO -->
<!-- TARIFA NUEVO -->
							<td align=right height="25" style="padding-left: 10px;">
								<span>Tarifa:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="tarifa" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /TARIFA NUEVO -->					
<!-- TARIFA VIEJO -->					
<!-- 										<td class="LabelForm" align="right">Tarifa:</td> -->
<!-- 										<td align="center"> -->
<%-- 											<html:text property="tarifa" styleClass="textinput" readonly="true" /> --%>
<!-- 										</td> -->
<!-- TARIFA VIEJO -->							
<!-- PESO NUEVO -->
							<td align=right height="25" style="padding-left: 10px;">
								<span>Peso<em style="color: gray;">(kg)</em>:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="weight" onblur="updateBrnchOcu()" onkeypress="return isNumber(event, this)" maxlength="15"/>
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /PESO NUEVO -->					
<!-- PESO VIEJO -->
			
<!-- 										<td class="LabelForm" align="right">Peso:</td> -->
<!-- 										<td align="center"> -->
<%-- 											<html:text property="weight" styleClass="textinput" readonly="true" /> --%>
<!-- 										</td> -->
<!-- /PESO VIEJO -->				
<!-- VOLUMEN NUEVO -->
							<td align=right height="25" style="padding-left: 10px;">
								<span>Volumen:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text property="volume" readonly="true" />
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /VOLUMEN NUEVO -->
<!-- VOLUMEN VIEJO -->
<!-- 										<td class="LabelForm" align="right">Volumen:</td> -->
<!-- 										<td align="center"> -->
<%-- 											<html:text property="volume" styleClass="textinput" readonly="true" /> --%>
<!-- 										</td> -->
<!-- /VOLUMEN VIEJO -->
											</tr>
										</table>
									</td>
									<tr>
									<td>
										<table>
											<tr>
												<td align=right height="25">
													<span>Largo<em style="color: gray;">(cm)</em>:</span>
					            				</td>
					            				<td height="25" >
													<div class="inputV2">
														<div class="group">       
															<html:text property="volL" onchange="updateBrnchOcu()"
															onkeyup="return isInteger(event, this);"
															onkeypress="return isNumber(event, this)"  maxlength="15"/>
															<span class="highlight"></span>
															<span class="bar" ></span>
														</div>
													</div>
												</td>
												<td align=right height="25" style="padding-left: 10px;">
													<span>Alto<em style="color: gray;">(cm)</em>:</span>
					            				</td>
					            				<td height="25" >
													<div class="inputV2">
														<div class="group">
															<html:text property="volH" onchange="updateBrnchOcu()" 
															onkeyup="return isInteger(event, this);"
															onkeypress="return isNumber(event, this)"  onblur="calcVolWeight();" maxlength="15"/>
															<span class="highlight"></span>
															<span class="bar" ></span>
														</div>
													</div>
												</td>
												<td align=right height="25" style="padding-left: 10px;">
													<span>Ancho<em style="color: gray;">(cm)</em>:</span>
					            				</td>
					            				<td height="25" >
													<div class="inputV2">
														<div class="group">
															<html:text property="volW" onchange="updateBrnchOcu()"
															onkeyup="return isInteger(event, this);"
															onkeypress="return isNumber(event, this)"  onblur="calcVolWeight();" maxlength="15"/>
															<span class="highlight"></span>
															<span class="bar" ></span>
														</div>
													</div>
												</td>
												<td align=right height="25" style="padding-left: 10px;">
													<span>Peso volumétrico:</span>
					            				</td>
					            				<td height="25" >
													<div class="inputV2">
														<div class="group">       
															<html:text property="weightVolumetric" onchange="updateBrnchOcu()" onkeypress="return isNumber(event, this)"  onblur="calcVolWeight();" readonly="true" />
															<span class="highlight"></span>
															<span class="bar" ></span>
														</div>
													</div>
												</td>
											</tr>
										</table>
									</td>
									</tr>
									<tr>
									<td>
										<table>
											<tr>
												<td class="LabelForm" align="right">Descripción:</td>
												<td>
													<html:select property="packType" styleClass="textinput">
														<html:options name="packTypeName" labelName="packTypeLabel" />
													</html:select>
												</td>
		<!-- CONTENIDO NUEVO -->
									<td align=right height="25" style="padding-left: 10px;">
										
		            				</td>
		<!-- /CONTENIDO NUEVO -->			
		<!-- CONTENIDO VIEJO -->							
		<!-- 										<td class="LabelForm" align="right">Contenido:</td> -->
		<!-- 										<td> -->
		<%-- 											<html:text property="content" styleClass="textinput" style="width: 200" maxlength="50"/> --%>
		<!-- 										</td> -->
		<!-- /CONTENIDO VIEJO -->
		<!-- 										<td></td> -->
											</tr>
										</table>
									</td>
									</tr>
									<tr>
										<td  >
											<table>
												<tr>
													<td height="25" style="padding-right: 20px;">
															<div class="inputV2">
																<div class="group">      
																	<span>Contenido:</<span> 
																	<html:text property="content" styleClass="textinput" style="width: 200" maxlength="50"/>
																	<span class="highlight"></span>
																	<span class="bar" ></span>
																</div>
															</div>
														</td>
													<td width="20px"><div align="right">Cat. productos:</div></td>
													<td > 
														
														<div class="inputV2" style="width: 200px;">
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
													<td class="LabelForm" align="right" style="padding-left: 10px;">Tipo de Envío:</td>
												<td>
													<html:select property="shippingType" multiple="true" size="1" styleClass="textinput">
														<html:options name="shippingtypes" />
													</html:select>
												</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr>
									<tr><td style="height:5px"></td></tr>
									<table class="marginAutoCentro">
										<tr width="100%">
											<html:hidden property="ead"></html:hidden>
											<td class="LabelForm" align="right">Incluye EAD:
												<html:checkbox property="eadCheck" disabled="true" onclick="eadChk();">
												</html:checkbox>
											</td>
											<html:hidden property="rad"></html:hidden>
											<td class="LabelForm" align="right" style="padding-left: 10px;" >Incluye RAD:
												<html:checkbox property="radCheck" disabled="true" onclick="radChk();">
												</html:checkbox>
											</td>
											<html:hidden property="ext"></html:hidden>
											<td class="LabelForm" align="right" style="padding-left: 10px;">Incluye Zona Ext.:
												<html:checkbox property="extCheck" disabled="true" onclick="extChk();"> <!--  -->
												</html:checkbox>
											</td>
											<td class="LabelForm" align="right" style="width: 280px">Nueva Zona:
												<html:select property="zoneCombo" disabled="true" onchange="javascript:onChangeZona(document.forms[0].zoneCombo.value);">
													<html:options name="zonesTO" labelName="zones" />
												</html:select>
											</td>

											<td class="LabelForm" align="right" style="padding-left: 10px;">Incremento peso:
												<html:text property="weightUpdate" onkeypress="return isNumber(event, this)" styleClass="textinput" style="width: 70px;" disabled="true" />
													<html:checkbox property="weightCheck"	disabled="true" onclick="weightEventCheck();">
												</html:checkbox>
											</td>
										</tr>
										<tr>
											<td class="LabelForm" align="right">Nueva Tarifa:
												<html:select property="tarifaCombo" disabled="true" onchange="javascript:onChangeTarifa(document.forms[0].tarifaCombo.value);">
													<html:options name="zoneids" labelName="zoneids" />
												</html:select>
											</td>
											<!-- Indicar los Pesos Maximos de la tarifa en el input type hidden -->
											<%
						   String valorlista="";
						   ArrayList locallista=(ArrayList)request.getAttribute("maximosvol");
						   ArrayList locallista2=(ArrayList)request.getAttribute("maximoskg");
						   ArrayList locallistaTarifas=(ArrayList)request.getAttribute("zoneids");
				           for (int i=1;i<=8;i++)
				           {
				              valorlista=(String)locallistaTarifas.get(i);// + "vol";
				              valorlista=valorlista.toLowerCase();
  				        %>
											<html:hidden property="<%=valorlista%>"
												value="<%=(String)locallista.get(i-1)%>">
											</html:hidden>
											<%
						   }    // end for Volumen
				           for (int i=1;i<=8;i++)
				           {
				           valorlista=(String)locallistaTarifas.get(i) + "kg";
				           valorlista=valorlista.toLowerCase();
  				        %>
											<html:hidden property="<%=valorlista%>"
												value="<%=(String)locallista2.get(i-1)%>">
											</html:hidden>
											<%
						   }    // end for Kilos
 						%>
											<!-- Fin Indicar los Pesos Maximos de la tarifa en el input type hidden -->
										<td class="LabelForm" align="right">Valor Asegurado:</td>
										<td align="right">
											<html:text property="insAmt" styleClass="textinput" style="width: 100" disabled="true"/>
										    <html:checkbox property="insCheck"	disabled="true" onclick="valorCheck();"></html:checkbox>
									    </td>
										<td class="LabelForm" align="left" style="padding-left: 10px;" colspan="2">Acuse de Recibo:
											<html:text property="actType" styleClass="textinput" style="width: 50" disabled="true"/>								   
									        	<html:checkbox property="actCheck"	disabled="true" onclick="acuseCheck();"></html:checkbox>
									        	<html:select size="1" property="acusederecibo"	styleClass="textinput" disabled="true" onclick="setAcuseText();">
							                		<html:options name="ackvalue" labelName="acklabel" />
												</html:select>
									    </td>
										</tr>
									</table>
								</tr>
							</table>
							</td>
							</tr>
						
				</table>
				
				</fieldset>
				</td>
				</tr>
				</table>
				<table align="center">
						<tr>
							<td colspan="8">
								<table align="center">
									<tr>
<!-- OBSERVACIONES NUEVO -->
							<td align=right height="25">
								<span>Observaciones:</span>
            				</td>
            				<td height="25">
								<div class="inputV2">
									<div class="group">       
										<html:text styleClass="textinput" size="95" maxlength="255" property="comments" onblur="setBack('true');" onfocus="setBack('false');"/>
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /OBSERVACIONES NUEVO -->
<!-- OBSERVACIONES VIEJO -->
<!-- 										<td class="LabelForm">Observaciones:</td> -->
<!-- 										<td> -->
<%-- 											<html:text styleClass="textinput" size="95" maxlength="255" property="comments" onblur="setBack('true');" onfocus="setBack('false');"/> --%>
<!-- 										</td> -->
<!-- /OBSERVACIONES VIEJO -->	
										<td></td>
									</tr>
									<tr>
<!-- REFERENCIA NUEVO -->
										<td align=right height="25">
											<span>Referencia:</span>
			            				</td>
			            				<td height="25">
											<div class="inputV2">
												<div class="group">       
													<html:text styleClass="textinput" size="95" maxlength="65" property="reference" onkeypress="return add_Key_li(this.value);"/>
													<span class="highlight"></span>
													<span class="bar" ></span>
												</div>
											</div>
										</td>
<!-- /REFERENCIA NUEVO -->
<!-- REFERENCIA VIEJO -->									
<!-- 										<td class="LabelForm">Referencia:</td> -->
<!-- 										<td> -->
<%-- 											<html:text styleClass="textinput" size="95" maxlength="65" property="reference" onblur="setBack('true');" onfocus="setBack('false');"/> --%>
<!-- 										</td> -->
<!-- //REFERENCIA VIEJO -->										
										<td>
											<input type="button" onclick="return add_li_submit(document.forms[0].reference.value);" value="Añadir referencia" class="bookbutton buttonLarge">
										</td>
									</tr>
									<tr>
										<td>
										</td>
										<td >
											<div id="dataRefdiv" class="divReference">
												<dl id="listaDesordenada" style="width: 100%; margin: 0px; padding: 0px;" >																
      											</dl>
											</div>
										</td>
										<td>
										</td>
									</tr>
									<TR class="LabelForm">
										<TD align=right height="25">Número de pedimento:&nbsp;</TD>
										<TD>
											<html:text styleClass="textcontenido" property="pedinumber" onblur="setBack('true');" onfocus="setBack('false');" style="width: 30%;"/>
											Agente Aduanal:&nbsp;
											<html:text styleClass="textcontenido" property="custagent" onblur="setBack('true');" onfocus="setBack('false');" style="width: 30%;"/>
										</TD>
									</TR>
								</table>
							</td>
														
						</tr>	
						<tr>
						<td>
							<logic:present name="status">
								<table>
									<tr class="LabelForm" style="visibility: hidden;">
										<td>Tipo de Pago:</td>
										<td>Efectivo
											<input type="checkbox" name="cash" onclick="callCashMode();" >
										</td>
										<td>Crédito
											<input type="checkbox" name="credit" onclick="callCreditMode();" checked="yes">
										</td>
									</tr>
								</table>
							</logic:present>
						</td>
						</tr>
						<tr>
							<td style="height:50px; !important"></td>
							<td align="center">
								<INPUT TYPE="button" onclick="changeGuia();" VALUE="Modificar Documentacion" name="modificar" class="buttonnormal1"/> 
								<INPUT TYPE="button" onclick="facturarCambios();" VALUE="Facturar Cambios" name="facturar" class="buttonnormal1" />
								<INPUT TYPE="button" onclick="convert();" VALUE="Confirmar Documentación" name="confirmar" class="buttonnormal1"/> 
								<INPUT TYPE="BUTTON" VALUE="Limpiar Datos" class="buttonnormal" name="limpiar" onclick="resetfields();"/>
							</td>
						</tr>
					</table>
					<table class="bodyWidth marginAutoCentro"><!-- AAP04 -->
						<tr>
							<td>
								<fieldset>
									<legend> Notificaciones por Correo Electrónico </legend><!-- AAP04 -->
									<table >
										<tr class="LabelForm">
<!-- NOTIFICACION DE ENTREGA NUEVO -->
							<td align=right height="25">
								<span>Notificación de Entrega:</span>
            				</td>
            				<td>
            					<html:checkbox property="eMailOrigCheck" onclick="cleanEmail(this, document.forms[0].eMailOrigText)"/>
            				</td>
            				<td height="25">
            				
								<div class="inputV2">
									<div class="group">       
										<html:text property="eMailOrigText" styleClass="textinput" size="95" maxlength="200" readonly="true"/>
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /NOTIFICACION DE ENTREGA NUEVO -->		
<!-- NOTIFICACION DE ENTREGA VIEJO -->							
<!-- 											<td align="right">Notificación de Entrega</td>AAP04 -->
<!-- 											<td> -->
<%-- 												<html:checkbox property="eMailOrigCheck" onclick="cleanEmail(this, document.forms[0].eMailOrigText)"/> --%>
<%-- 												<html:text property="eMailOrigText" styleClass="textinput" size="95" maxlength="200" readonly="true"/> --%>
<!-- 											</td> -->
<!-- /NOTIFICACION DE ENTREGA VIEJO -->
										</tr>
										<tr class="LabelForm">
<!-- NOTIFICACION DE ENVÍO NUEVO -->
							<td align=right height="25">
								<span>Notificación de Envío:</span>
            				</td>
            				<td>
            					<html:checkbox property="eMailDestCheck" onclick="cleanEmail(this, document.forms[0].eMailDestText)"/>
            				</td>
            				<td height="25">
            				
								<div class="inputV2">
									<div class="group">       
										<html:text property="eMailDestText" styleClass="textinput" size="95" maxlength="200" readonly="true"/>
										<span class="highlight"></span>
										<span class="bar" ></span>
									</div>
								</div>
							</td>
<!-- /NOTIFICACION DE ENVÍO NUEVO -->	
<!-- NOTIFICACION DE ENVÍO VIEJO -->									
<!-- 											<td align="right">Notificación de Envío</td>AAP04 -->
<!-- 											<td> -->
<%-- 												<html:checkbox property="eMailDestCheck" onclick="cleanEmail(this, document.forms[0].eMailDestText)"/> --%>
<%-- 												<html:text property="eMailDestText" styleClass="textinput" size="95" maxlength="200" readonly="true"/> --%>
<!-- 											</td> -->
<!-- /NOTIFICACION DE ENVÍO VIEJO -->											
										</tr>
									</table>
								</fieldset>
							</td>
						</tr>
					</table><!-- AAP04 -->
				
			</logic:present>
		 
		</table>
	
	<html:hidden property="fiscaladdresscode" />
	<html:hidden property="refNo" />
	<html:hidden property="orgioncode" />
	<html:hidden property="destcode" />
	<html:hidden property="currentTask" />
	<html:hidden property="checkOriginAddress" />
	<html:hidden property="oldDestSite" />
	<html:hidden property="oldDestSiteName" />
	<html:hidden property="guiaHasErrors" />
	<html:hidden property="guiaErrorType" />
	<html:hidden property="isExtendedZone" />
	<html:hidden property="oldRefNo"></html:hidden>
	<html:hidden property="nuevoValorEnvio" />
	<html:hidden property="nuevoValorSeguro" />
	<html:hidden property="nuevoValorAcuse" />
	<html:hidden property="nuevoValorEAD" />
	<html:hidden property="nuevoValorRAD" />
	<html:hidden property="nuevoValorEXT" />
	<html:hidden property="aceptarNuevosCargos" />
	<html:hidden property="valordeclarado" />
	<html:hidden property="payMode" />
	<html:hidden property="clientType" />
	<html:hidden property="tarifaT7" />
	<html:hidden property="valorRET" />
	<html:hidden property="valorIVA" />
	<html:hidden property="valorRetAmount" />
	<html:hidden property="ivaRetTemplate" />
	<html:hidden property="clientHasLocalCredit" />
	<html:hidden property="clienteIDLocal" />
	<html:hidden property="allowNewInvoice" />
	<html:hidden property="pesoMaximoT7" />
	<html:hidden property="dest_am_gety_code" />
	<html:hidden property="horasEntregaOcu" />
	<html:hidden property="horasEntregaEad" />
	<html:hidden property="checkRefDir" /><!-- AAP02 -->
	<html:hidden property="checkTelDir" /><!-- AAP02 -->
	<html:hidden property="reqAcuse" /><!-- AAP02 -->
	<html:hidden property="reqAcuseXT" /><!-- AAP02 -->
	<html:hidden property="montoMaxOl" /><!-- AAP02 -->
	<html:hidden property="destBranch" /><!-- AAP02 -->
	<html:hidden property="actTypeCurrent" />
	
	<html:hidden property="idSetSel"/>
	<html:hidden property="zonaKmSel"/>
	<html:hidden property="tarifaSel"/>
	<html:hidden property="pesoKgSel"/>
	<html:hidden property="volumenSel"/>
	<html:hidden property="numRastreoSel"/>
	<html:hidden property="acuseSel"/>
	<html:hidden property="valorDeclaradoSel"/>
	<html:hidden property="EADSel"/>
	<html:hidden property="RADSel"/>
	<html:hidden property="EXTSel"/>
	<html:hidden property="mainSetSel"/>
	<html:hidden property="invcSel"/>
	<html:hidden property="clickedItems"/>
	<html:hidden property="banCerrar"/>
	<html:hidden property="reLoad"/>
	<html:hidden property="facturaMod"/>
	<html:hidden property="filtrarPor"/>
	<html:hidden property="brncVrtl"/>	
	<html:hidden property="cbAll"/>
	<html:hidden property="contRastreos"/>
	<html:hidden property="shipType"/>
	<html:hidden property="neededConfirmationService2D"/><!-- JSA01 -->
	<html:hidden property="confirmationService2D"/><!-- JSA01 -->
	<html:hidden property="shipTypeSEGDescChange"/><!-- JSA01 -->
	<html:hidden property="shipTypeSEGChange"/><!-- JSA01 -->
	<html:hidden property="msjConfirmationService"/><!-- JSA01 -->
	<html:hidden property="alertNOConfirmationService"/><!-- JSA01 -->		
	<html:hidden property="volLMAX"/><!-- JSA01 -->
	<html:hidden property="volHMAX"/><!-- JSA01 -->
	<html:hidden property="volWMAX"/><!-- JSA01 -->
	<html:hidden property="pesoMAX"/><!-- JSA01 -->
	<html:hidden property="weightOriginal"/><!-- JSA01 -->
	<html:hidden property="weightVolumetricMAX"/><!-- JSA01 -->
	<html:hidden property="listReferences" /><!-- AAP13 -->
	<html:hidden property="flagValidRefrClnt" /><!-- AAP13 -->
	<html:hidden property="retieneCmpy" /><!-- JSA03 -->
	<html:hidden property="factorDividorPesoVol" /> <!--JSA01 VARIABLE PARA REALIZAR CALCULO DEL PESO VOLUMETRICO LADO DEL CLIENTE -->
	<html:hidden property="cantPesoVolDecimales" /><!--JSA01 VARIABLE PARA TRUNCAR EL CALCULO DEL PESO VOLUMETRICO -->
	<html:hidden property="tarifaOriginal"/><!-- JSA04 -->
	<html:hidden property="volumeOriginal"/><!-- JSA04 -->
	<html:hidden property="forceCaptureDimensions" /><!--JSA04 VARIABLE PARA SABER SI SE FORZA A CAPTURAR LAS DIMENSIONES -->
	<html:hidden property="weightAfterModification" />
	<html:hidden property="volumeAfterModification" />
	<html:hidden property="volMAX"/><!-- JSA02 -->	
	<html:hidden property="borderBranch" /><!-- AAP20 -->
	<html:hidden property="locationType" /><!-- AAP20 -->
	<html:hidden property="volLMin" />
	<html:hidden property="volWMin" />
	<html:hidden property="volHMin" />
	<html:hidden property="wghtMin" />
	<html:hidden property="volMin" />
	
	<!-- Sucursal default addr -->
	<html:hidden property="defaultBrnchAddr" />
	
	<!-- Propiedades para Complemento Carta Porte -->
	<html:hidden property="productIdSat" />
	
	<!-- Propiedades para validación ModRastreo -->
	<html:hidden property="modFormnoFlag" />
	<html:hidden property="eadOrigVal" />
	<html:hidden property="radOrigVal" />
	<html:hidden property="extOrigVal" />
	<html:hidden property="zoneOrigVal" />
	<html:hidden property="tarifOrigVal" />
	<html:hidden property="insOrigVal" />
	<html:hidden property="ackOrigVal" />
	
	<html:hidden property="maxDeclAmnt" />
	
	<!-- Validación del número de pedimento -->
	<html:hidden property="borderbranchcheck" />
	<html:hidden property="msgInfo" />

	<html:hidden property="assignedBranch" />
	
	<html:hidden property="destClaveAux" />
	<html:hidden property="destNombreAux" />
	</html:form>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>	
</body>
</html>