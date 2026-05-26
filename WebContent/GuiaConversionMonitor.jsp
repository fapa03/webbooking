<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="ISO-8859-1" import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
	String version = "00.00.05";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<link rel="stylesheet" media="screen" type="text/css" href="webbookingMonitor.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css" href="webbooking.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css?v=<%=version%>">

<script type="text/javascript" src="GuiaConversionMonitor.js?v=<%=version%>"></script>
<script type="text/javascript" src="common.js?v=<%=version%>"></script>
<script type="text/javascript" src="js/jquery/1.12.0.js?v=<%=version%>"></script>
<script type="text/javascript" src="js/v2/global.js?v=<%=version%>"></script>
<script type="text/javascript" src="barraEspera.js?v=<%=version%>"></script>
<script type="text/javascript">
function goOut(page){
	if (page == "guiabooking") {
		page = "thispage";	
	
	}
	if (document.getElementById('mensaje').style.visibility=='hidden') {
 			showBarraEspera('mensaje','body','Espere por favor...Saliendo de documentacion');
			document.forms[0].action="webBookinggeneralMain.do?includeattribute=yes&page="+page;
			document.forms[0].submit();
	}
}
</script>
<title>Monitor de rastreos Electrónicos</title>
</head>
<body id="body" onload="initScript();//mostrarCapaCentro('mensaje');" onunload="clearGuias();" onkeydown="noBack(event);" class="backgroundStandard widthOverflow" style="margin:0px !important;">

<html:form action="guiaConversionMonitor.do">
		<table border="0" cellspacing="0" cellpadding="0" class="width100porcent backgroudBluePtx">
			<tr>
				<td  >
					<div class="bodyWidth marginAutoCentro">
					        <img src="images/logos/logoW.png" border="0"  
					        onclick="hideMenu();mainpage();"       
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
  			
					<logic:present name="loginForm">
						<html:hidden name="loginForm" property="userValidate" />
						<logic:equal name="loginForm" property="userValidate" value="validUser">
  								
  								<a onclick="goOut('mainpage');hideMenu();" style="">Menú principal</a> 
								<a onclick="goOut('cliententry');hideMenu();" style="">Registro de cliente destino</a>
								<logic:equal name="loginForm" property="showWeb" value="Y">
									<logic:equal name="loginForm" property="newBooking" value="Y">
										<a onclick="goOut('guiabooking');hideMenu();" style="">Elaboración de guías</a>
									</logic:equal>
								</logic:equal>
								<html:hidden name="loginForm" property="showPpg"></html:hidden>
								<logic:equal name="loginForm" property="showPpg" value="Y">
									<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
								</logic:equal>
								<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelación de guías</a>
								<a onclick="goOut('shipmenthistory');hideMenu();" style="">Historico de envios</a>
								<a onclick="goOut('clientreport');hideMenu();" style="">Información de cliente</a>
								<logic:present name="loginForm">
									<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->									
										<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a>
									</logic:equal>
								</logic:present>						 
 						</logic:equal>
					</logic:present> 
 					<logic:present name="loginForm">
						<html:hidden name="loginForm" property="userValidate" />
						<logic:notEqual name="loginForm" property="userValidate" value="validUser">

 								<a onclick="goOut('mainpage');hideMenu();" style="">Menú principal</a> 
								<logic:equal name="loginForm" property="showWeb" value="Y">
									<logic:equal name="loginForm" property="newBooking" value="Y">
  										<a onclick="goOut('guiabooking');hideMenu();" style="">Elaboración de guías</a>
  									</logic:equal>
  								</logic:equal> 
  								<html:hidden name="loginForm" property="showPpg"></html:hidden>
								<logic:equal name="loginForm" property="showPpg" value="Y">
									<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
								</logic:equal>
								<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelación de guías</a> 
								<a onclick="goOut('shipmenthistory');hideMenu();" style="">Historico de envios</a> 
								<a onclick="goOut('clientreport');hideMenu();" style="">Información de cliente</a>
								<logic:present name="loginForm">
									<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->
										<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar sesión</a>
									</logic:equal>
								</logic:present> 
						</logic:notEqual>
					</logic:present>  			
  			
			
			</div>
 			
		</div>
		
		<table style="width: 100%; height: 100%">
			<tr style="width: 100%; height: 100%">
				<td style="height: 8px;">
					<textarea id="mensaje" class="textareacontenidonew" cols="60" readonly="readonly" rows="4" onblur="self.focus();" style="background-color: #EEEEEE; border-style: ridge; 
						overflow: hidden; text-align: center; vertical-align: middle; z-index: 2; visibility: hidden;">
					</textarea>
				</td>
			</tr>
		</table>
		
		<table class="bodyWidth marginAutoCentro">
			<tr >
				<td>
		 
					<table>
						<tr>
							<td align="left" class="titleSection fontBold" >
								Monitoreo de Rastreos Prepago
							</td>		
						</tr>
						<tr >
							<td>
								<div id="DivRoot01" align="left">
 
									<div style="overflow: hidden;height: 56px;" id="DivHeaderRow01"></div>
									<div style="overflow: scroll;" onscroll="OnScrollDiv(this,'01')" id="DivMainContent01" >									
																			
										<table id="conjuntoGuias" class="tablaDetallev2">
											<tr class="tablaDetallev2Header headerFFF" width="100%" style="background: #fff !important;">
												<th>ID set</th>
												<th>Zona Km</th>
												<th>Tarifa</th>
												<th>Peso (kg)</th>
												<th>Volumen</th>
												<th>Num Rastreos</th>
												<th>Acuse de recibo</th>												
												<th>Valor declarado</th>
												<th>EAD</th>
												<th>RAD</th>
												<th>Zona Ext.</th>
												<th>Set Original</th>
												<th>Factura</th>
												<th>Tipo envío</th>
												<th>Vigencia</th><!--AAP27-->
											</tr>
										<%
										ArrayList conjuntoGuias = (ArrayList)request.getAttribute("conjuntoGuias");
										if (conjuntoGuias != null) {
											if (conjuntoGuias.isEmpty()) {
												for(int i=0;i <= 9; i++){
									%>				
													
										<tr class="tablaDetallev2Body" width="100%">
											<td>&nbsp;</td><!-- ID set -->
											<td>&nbsp;</td><!-- Zona Km -->
											<td>&nbsp;</td><!-- Tarifa -->
											<td>&nbsp;</td><!-- Peso (kg) -->
											<td>&nbsp;</td><!-- Volumen -->
											<td>&nbsp;</td><!-- Num Rastreos -->
											<td>&nbsp;</td><!-- Acuse de recibo -->
											<td>&nbsp;</td><!-- Valor declarado -->
											<td>&nbsp;</td><!-- EAD -->
											<td>&nbsp;</td><!-- RAD -->
											<td>&nbsp;</td><!-- Zona Ext. -->
											<td>&nbsp;</td><!-- ID set original -->
											<td>&nbsp;</td><!-- Factura -->
											<td>&nbsp;</td><!-- Tipo envio -->
											<td>&nbsp;</td><!-- Vigencia --><!--AAP27-->
										</tr>	
									<%				
												}												
											} else {
												String leyendaBR = "";//AAP20
												for (int i = 0; i < conjuntoGuias.size(); i++) {
													if (((ArrayList)conjuntoGuias.get(i)).get(15).equals("BR") ) {//AAP20
														leyendaBR = "Set para uso solo en frontera";//AAP20
													} else {//AAP20
														leyendaBR = "Seleccione Set de Guias";//AAP20
													}
										%>
											<tr id="tablerow<%=i%>" onmouseover="this.style.backgroundColor='#D0D2C5'; this.style.cursor='pointer'" 
												onmouseout="this.style.backgroundColor='white';this.style.cursor='default';" onclick="selectedSet('<%=i%>')" 
												title="Seleccione Set de Guias" class="tablaDetallev2Body" width="100%">
												
												<td align="right" title="<%=leyendaBR/*AAP20*/%>"><%=((ArrayList)conjuntoGuias.get(i)).get(0)%><input type=hidden name="idSet<%=i%>" id="idSet<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(0)%>"/></td><!-- ID set -->
												<td align="right"><%=((ArrayList)conjuntoGuias.get(i)).get(1)%><input type=hidden name="zonaKm<%=i%>" id="zonaKm<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(1)%>"/></td><!-- Zona Km -->
												<td align="right"><%=((ArrayList)conjuntoGuias.get(i)).get(2)%><input type=hidden name="tarifa<%=i%>" id="tarifa<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(2)%>"/></td><!-- Tarifa -->
												<td align="right"><%=((ArrayList)conjuntoGuias.get(i)).get(3)%><input type=hidden name="pesoKg<%=i%>" id="pesoKg<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(3)%>"/></td><!-- Peso (kg) -->
												<td align="right"><%=((ArrayList)conjuntoGuias.get(i)).get(4)%><input type=hidden name="volumen<%=i%>" id="volumen<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(4)%>"/></td><!-- Volumen -->
												<td align="right"><%=((ArrayList)conjuntoGuias.get(i)).get(5)%><input type=hidden name="numRastreo<%=i%>" id="numRastreo<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(5)%>"/></td><!-- Num Rastreos -->
												<td align="center"><%=((ArrayList)conjuntoGuias.get(i)).get(6)%><input type=hidden name="acuse<%=i%>" id="acuse<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(6)%>"/></td><!-- Acuse de recibo -->
												<td align="right"><%=((ArrayList)conjuntoGuias.get(i)).get(7)%><input type=hidden name="valorDeclarado<%=i%>" id="valorDeclarado<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(7)%>"/></td><!-- Valor declarado -->
												<td align="center"><%=((ArrayList)conjuntoGuias.get(i)).get(8)%><input type=hidden name="EAD<%=i%>" id="EAD<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(8)%>"/></td><!-- EAD -->
												<td align="center"><%=((ArrayList)conjuntoGuias.get(i)).get(9)%><input type=hidden name="RAD<%=i%>" id="RAD<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(9)%>"/></td><!-- RAD -->
												<td align="center"><%=((ArrayList)conjuntoGuias.get(i)).get(10)%><input type=hidden name="EXT<%=i%>" id="EXT<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(10)%>"/></td><!-- Zona Ext. -->
												<td align="right"><%=((ArrayList)conjuntoGuias.get(i)).get(11)%><input type=hidden name="mainSet<%=i%>" id="mainSet<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(11)%>"/></td><!-- ID set original -->
												<td align="left"><%=((ArrayList)conjuntoGuias.get(i)).get(12)%><input type=hidden name="invc<%=i%>" id="invc<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(12)%>"/></td><!-- Factura -->
												<td align="left" style="width: 130px;"><%=((ArrayList)conjuntoGuias.get(i)).get(13)%>
													<input type=hidden name="shippingType<%=i%>" id="shippingType<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(13)%>"/><!-- tipo de envio -->
													<input type=hidden name="locationType<%=i%>" id="locationType<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(15)%>"/><!-- indicador BR / IN AAP20 -->
												
												</td>
												<td align="center" style="width: 80px;"><%=((ArrayList)conjuntoGuias.get(i)).get(16)%><input type=hidden name="vigencia<%=i%>" id="vigencia<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(16)%>"/></td> <!--AAP27-->
											</tr>										
										<% 			leyendaBR = "";
												}
												if (conjuntoGuias.size()<9) {
													for (int i=conjuntoGuias.size();i <= 9; i++) {
										%>

											<tr class="tablaDetallev2Body" width="100%">
												<td>&nbsp;</td><!-- ID set -->
												<td>&nbsp;</td><!-- Zona Km -->
												<td>&nbsp;</td><!-- Tarifa -->
												<td>&nbsp;</td><!-- Peso (kg) -->
												<td>&nbsp;</td><!-- Volumen -->
												<td>&nbsp;</td><!-- Num Rastreos -->
												<td>&nbsp;</td><!-- Acuse de recibo -->
												<td>&nbsp;</td><!-- Valor declarado -->
												<td>&nbsp;</td><!-- EAD -->
												<td>&nbsp;</td><!-- RAD -->
												<td>&nbsp;</td><!-- Zona Ext. -->
												<td>&nbsp;</td><!-- ID set original -->
												<td>&nbsp;</td><!-- Factura -->
												<td>&nbsp;</td><!-- tipo de envio -->
												<td>&nbsp;</td><!-- Vigencia --><!--AAP27-->
											</tr>	
										<%				
													}
												}												
											}
										}										
										%>
										</table>
									</div>
									<div id="DivFooterRow01" style="overflow: hidden;"></div>
								</div>
							</td>
						</tr>						
						<tr style="height: 40px;">
							<td align="left" class="titulo" id="detGuia01" style="visibility: hidden; position: absolute; top: 450px; left: 200px; ">
								Detalle Set de Guias Seleccionado
							</td>							
						</tr>
						<logic:present name="error">
							<tr>
								<td class="LabelError" align="left">
									<bean:write name="error"/>					
								</td>
							</tr>						
						</logic:present>
						<tr style="width: 100%;">
							<td style="width: 100%;">
								<div id="detGuia02" style="visibility: hidden; position: absolute; top: 450px; left: 200px;">
								<table style="width: 100%;" class="tablaDetallev2">
									<tr class="tablaDetallev2Header">
										<td>ID Set	</td>
										<td>Zona Km</td>
										<td>Tarifa</td>
										<td>Peso (Kg)</td>
										<td>Volumen</td>
										<td>Acuse de Recibo</td>
										<td>Valor Declarado</td>
										<td>EAD</td>
										<td>RAD</td>
										<td>Zona Ext.</td>
										<td>Set Original</td>
										<td>Factura</td>
										<td>Tipo envío</td>
									</tr>
									<tr class="tablaDetallev2Body">
										<td align="right">
											<bean:write name="invoiceConversionMonitorForm" property="idSetSel"/>
										</td>										
										<td align="right">
											<bean:write name="invoiceConversionMonitorForm" property="zonaKmSel"/>
										</td>
										<td align="right">
											<bean:write name="invoiceConversionMonitorForm" property="tarifaSel"/>
										</td>
										<td align="right">
											<bean:write name="invoiceConversionMonitorForm" property="pesoKgSel"/>
										</td>
										<td align="right">
											<bean:write name="invoiceConversionMonitorForm" property="volumenSel"/>
										</td>
										<td align="center">
											<bean:write name="invoiceConversionMonitorForm" property="acuseSel"/>
										</td>
										<td align="right">
											<bean:write name="invoiceConversionMonitorForm" property="valorDeclaradoSel"/>
										</td>
										<td align="center">
											<bean:write name="invoiceConversionMonitorForm" property="EADSel"/>
										</td>
										<td align="center">
											<bean:write name="invoiceConversionMonitorForm" property="RADSel"/>
										</td>
										<td align="center">
											<bean:write name="invoiceConversionMonitorForm" property="EXTSel"/>
										</td>
										<td align="right">
											<bean:write name="invoiceConversionMonitorForm" property="mainSetSel"/>
										</td>
										<td align="right">
											<bean:write name="invoiceConversionMonitorForm" property="invcSel"/>
										</td>
										<td align="right" style="width: 130px;">
											<bean:write name="invoiceConversionMonitorForm" property="shippingTypeSel"/>
										</td>
									</tr>
									<%
									ArrayList detalleGuias = (ArrayList)request.getAttribute("detalleGuias");
									String cveCteProp = "", desCteProp = "";									
									if (detalleGuias != null) {
										if (!detalleGuias.isEmpty()) {
											cveCteProp = ((ArrayList)detalleGuias.get(0)).get(2).toString();
											desCteProp = ((ArrayList)detalleGuias.get(0)).get(3).toString();
										}										
									}									
									%>
									<tr style="width: 100%;">
										<td colspan="13" style="width: 100%;">
											<table style="width: 100%;" class="tablaDetallev2">
												<tr style="height: 21px;">
													<th class="tablaDetallev2Header" align="right" style="width: 16%;">Cliente Propietario:</th>
													<td class="tablaDetallev2Body" align="left"><%=cveCteProp %></td>
													<td class="tablaDetallev2Body" align="left"><%=desCteProp %></td>													
												</tr>
											</table>
										</td>
									</tr>
									
									<logic:present name="invoiceConversionMonitorForm">
										<logic:equal name="invoiceConversionMonitorForm" property="esPropietario" value="Y">
											<tr style="width: 100%;">
												<td colspan="13" style="width: 100%;">
													<table style="width: 100%;">
														<tr style="width: 100%;">
															<td style="width: 16%;" class="tablaDetallev2Header" align="right">
																filtrar por:
															</td>
															<td style="width: 84%;" class= "tablaDetallev2Body">
																<html:select property="filtrarPor" styleClass= "labelText" onchange="cambiarFiltro();"><!-- AAP05 -->
																	<html:options property="filtrarPorValue" labelProperty="filtrarPorLabel" /><!-- AAP05 -->
																</html:select><!-- AAP05 -->
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</logic:equal>
									</logic:present>
									<logic:equal name="invoiceConversionMonitorForm" property="locationTypeSel" value="BR"><!-- AAP20 -->									
									<tr style="width: 100%;" align="center">
										<td colspan="13" style="width: 100%; height: 8px;">											
												Rastreos para uso solo en frontera											
										</td>										
									</tr>
									</logic:equal><!-- AAP20 -->
									<tr style="width: 100%;" align="center"><!-- AAP27 -->
										<th colspan="13" style="width: 100%; height: 8px;">
											Rastreos vigentes hasta <bean:write name="invoiceConversionMonitorForm" property="vigenciaSel"/> 
										</th>										
									</tr>
									<tr style="width: 100%;">
										<td style="width: 70%;" colspan="13">
											<div id="DivRoot02" align="left">
												<div style="overflow: hidden;" id="DivHeaderRow02" ></div>
												<div style="overflow: scroll;" onscroll="OnScrollDiv(this,'02')" id="DivMainContent02" >
													<table id="detalleGuias" class="tablaDetallev2" style="visibility: hidden; position: absolute; top: 500px; left: 200px;">
														<tr class="tablaDetallev2Header" bgcolor="#FFFFFF">
															<th style="width: 3%;">																
																<html:checkbox property="cbAll" onclick="checkUnCheckAllGuias();"></html:checkbox>
															</th>
															<th style="width: 10%;">
																Rastreos
															</th>
															<th style="width: 13%;">
																No. Cliente Asignado
															</th>
															<th style="width: 31%;">
																Nombre Cliente Asignado
															</th>
															<th style="width: 13%;">
																ID Usuario asignado
															</th>
															<th style="width: 30%;">
																Usuario asignado
															</th>
														</tr>
														<%
														String guia = "";
														String numCteAsig = "";
														String numUserAsig = "";
														String asignado = "";
														if (detalleGuias != null) {
															if (detalleGuias.isEmpty()) {																
																for(int i=0; i <= 12; i++){
														%>				
																		
															<tr class="tablaDetallev2Body" bgcolor="white">
																<td align="right">&nbsp;</td>																
																<td align="right">&nbsp;</td><!-- guia -->
																<td align="right">&nbsp;</td><!-- Num cliente asignado -->
																<td align="left">&nbsp;</td><!-- Nombre cliente asignado -->
																<td align="left">&nbsp;</td><!-- ID usuario asignado -->
																<td align="left">&nbsp;</td><!-- Nombre usuario asignado -->
															</tr>	
														<%				
																}
															} else 	{															
																for (int i = 0; i < detalleGuias.size(); i++) {
																	guia = ((ArrayList)detalleGuias.get(i)).get(0).toString();
																	numCteAsig = ((ArrayList)detalleGuias.get(i)).get(6).toString();
																	numUserAsig = ((ArrayList)detalleGuias.get(i)).get(8).toString();
																	asignado = ((ArrayList)detalleGuias.get(i)).get(10).toString();
														%>
															<tr id="tablerow<%=i%>" onmouseover="this.style.backgroundColor='#D0D2C5'; this.style.cursor='pointer'" 
																onmouseout="this.style.backgroundColor='white';this.style.cursor='default'; this.style.zIndex=0;" class="tablaDetallev2Body" bgcolor="white"
																title="Usuario Administrador: <%=((ArrayList)detalleGuias.get(i)).get(4).toString()%> <%=((ArrayList)detalleGuias.get(i)).get(5).toString()%>">
																<td align="center"><input type="checkbox" name="cb" value="<%=guia%>|<%=asignado%>|<%=numCteAsig%>|<%=numUserAsig%>|" 
																	onclick="getFinalList('<%=((ArrayList)detalleGuias.get(i)).get(0)%>', '<%=i%>',this);" <%=((ArrayList)detalleGuias.get(i)).get(1)%>/>
																</td>
																
																<td align="right"><%=((ArrayList)detalleGuias.get(i)).get(0)%></td><!-- guia -->
																<td align="right"><%=((ArrayList)detalleGuias.get(i)).get(6)%></td><!-- Num cliente asignado -->
																<td align="left"><%=((ArrayList)detalleGuias.get(i)).get(7)%></td><!-- Nombre cliente asignado -->
																<td align="left"><%=((ArrayList)detalleGuias.get(i)).get(8)%></td><!-- ID usuario asignado -->
																<td align="left"><%=((ArrayList)detalleGuias.get(i)).get(9)%></td><!-- Nombre usuario asignado -->
															</tr>
														<%
																}
																
																if (detalleGuias.size()<11) {
																	for(int i=detalleGuias.size();i <= 11; i++) {
														%>
															<tr class="tablaDetallev2Body">
																<td align="right">&nbsp;</td>																
																<td align="right">&nbsp;</td><!-- guia -->
																<td align="right">&nbsp;</td><!-- Num cliente asignado -->
																<td align="left">&nbsp;</td><!-- Nombre cliente asignado -->
																<td align="left">&nbsp;</td><!-- ID usuario asignado -->
																<td align="left">&nbsp;</td><!-- Nombre usuario asignado -->
															</tr>	
														<%				
																	}
																}
															}
														}
														%>
													</table>
												</div>
												<div id="DivFooterRow02" style="overflow: hidden;"></div>
											</div>
										</td>
									</tr>
									<tr style="tablaDetallev2Header">
										<td style="width: 70%;" colspan="13" align="left" class="tablaDetallev2Body"><html:text size="1" style="text-align: right;"
										styleClass="labelTitleFooter" readonly="true" property="contRastreos"/>Seleccionados.</td>
									</tr>
								</table>
								</div>
							</td>
						</tr>
						<tr style="width: 100%;">
							<td style="width: 100%;">
								<iframe name="monitorIn" style="visibility:hidden; width: 0px;height: 0px;"></iframe>
							</td>
						</tr>
						<logic:present name="sGlobal">
							<logic:equal name="sGlobal" property="origenUserLevel" value="0">
								<tr style="width: 100%;">
									<td style="width: 100%;"> 
										<table id="asignacionesGuias" style="width: 100%; visibility: hidden; position: absolute; top: 500px; left: 200px;" class="tablaDetallev2">
											<tr>
												<td class="tablaDetallev2Header" style="width: 15%;" align="right">Asignaciones:</td>
												<td class="labelText" style="width: 80%;" colspan="2">
													<html:radio property="tipoAsignacion" value="gpo" onchange="showAsig(this);"></html:radio> Grupo Compañia.
													<html:radio property="tipoAsignacion" value="cte" onchange="showAsig(this);"></html:radio> Cliente Tercero.
													<html:radio property="tipoAsignacion" value="sin" onchange="showAsig(this);"></html:radio> Sin Asignar.																									
												</td>
												<td class="labelText" style="width: 5%;"></td>
											</tr>
											<tr id="objSucursalAsig" style="width: 100%; visibility: hidden; position: absolute; top: 500px; left: 200px;">
												<td class="tablaDetallev2Header" align="right">Sucursal:</td>
												<td class="labelText"><html:text size="13" style="border:none;" readonly="true" property="cveSucursalAsig" /></td>
												<td class="labelText"><html:text size="48" style="border:none;" readonly="true" property="desSucursalAsig" /></td>
												<td class="labelText" align="center"><input type="button" name="sucursalAsig" id="sucursalAsig" value="..." class="lov buttonMore" onclick="openLov('brncAsig');"/> </td>
											</tr>
											<tr id="objClienteAsig" style="width: 100%; visibility: hidden; position: absolute; top: 500px; left: 200px;">
												<td class="tablaDetallev2Header" align="right">Cliente:</td>
												<td class="labelText"><html:text size="13" style="border:none;" readonly="true" property="cveClienteAsig" /></td>
												<td class="labelText"><html:text size="48" style="border:none;" readonly="true" property="desClienteAsig" /></td>
												<td class="labelText" align="center"><input type="button" name="clienteAsig" id="clienteAsig" value="..." class="lov buttonMore" onclick="openLov('clntAsig');"/></td>
											</tr>
											<tr id="objUserAsig" style="width: 100%; visibility: hidden; position: absolute; top: 500px; left: 200px;">
												<td class="tablaDetallev2Header" align="right">Usuario:</td>
												<td class="labelText"><html:text size="13" style="border:none;" readonly="true" property="cveUserAsig" /></td>
												<td class="labelText"><html:text size="48" style="border:none;" readonly="true" property="desUserAsig" /></td>
												<td class="labelText" align="center"><input type="button" name="usuarioAsig" id="usuarioAsig" value="..." class="lov buttonMore" onclick="openLov('userAsig');"/></td>
											</tr>
										</table>
									</td>
								</tr>
							</logic:equal>
						</logic:present>
						<tr align="center">
							<td style="height: 35px;">
								<input type="button" class="button3 buttonLarge" onClick="mainpage()" value="Regresar" name="regresar">
								<logic:notEqual name="invoiceConversionMonitorForm" property="idSetSel" value="">
									<input type="button" class="button3 buttonLarge" onClick="documentarGuia()" value="Documentar" name="documentar">
								</logic:notEqual>
								<logic:notEqual name="invoiceConversionMonitorForm" property="idSetSel" value="">
									<logic:present name="sGlobal">
										<logic:equal name="sGlobal" property="origenUserLevel" value="0">
											<input type="button" class="button3 buttonLarge" onClick="asignarGuias();" value="Asignar" name="asignar">
											<input type="button" class="button3 buttonLarge" onClick="asignarGuiasPDF('0');" value="Crear PDF" name="crearPDF">
											<input type="button" class="button3 buttonLarge" onClick="asignarGuiasPDF('1');" value="Descargar set PDF" name="crearPDF">
										</logic:equal>
									</logic:present>
								</logic:notEqual>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<html:hidden property="currentTask"/>
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
		<html:hidden property="accionDoc"/>
		<html:hidden property="clickedItems"/>
		<html:hidden property="currentGuia"/>
		<html:hidden property="banCerrar"/>
		<html:hidden property="idSetSelOld"/>
		<html:hidden property="guiasAsigFlag"/>
		<html:hidden property="clientePropietario"/>
		<html:hidden property="esPropietario"/>
		<html:hidden property="genPDF"/>
		<html:hidden property="clickedItemsPDF"/>
		<html:hidden property="urlRastreoPDF"/>
		<html:hidden property="shippingTypeSel"/>
		<html:hidden property="locationTypeSel"/><!-- AAP20 -->
		<html:hidden property="vigenciaSel"/><!-- AAP27 -->
		
		<html:hidden property="idSetSelPDF"/>
		<html:hidden property="branchId"/>
		<input type="hidden" name = "clientId" id = "clientId" value = '<bean:write name="sGlobal" property="clientId"/>'/>
		<input type="hidden" name = "origenUserClave" id = "origenUserClave" value = '<bean:write name="sGlobal" property="origenUserClave"/>'/>
	</html:form>
</body>
</html>