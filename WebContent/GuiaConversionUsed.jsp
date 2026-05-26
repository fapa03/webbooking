<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="java.util.ArrayList"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<script src="js/jquery.min.js"></script>
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">
<script type="text/javascript" src="js/v2/global.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link rel=stylesheet media=screen type=text/css href=webbookingMonitor.css>
<link rel=stylesheet media=screen type=text/css href=webbooking.css>
<script type="text/javascript" src="barraEspera.js"></script>
<script type="text/javascript" src="GuiaConversionUsed.js"></script>


<link rel=stylesheet media=screen type=text/css href="./webbooking.css">
<script type="text/javascript" src="ClientDestinationEntry.js"></script>
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />


<title>Monitor de rastreos Electrónicos</title>
</head>
<body id="body" bgcolor="#FFFFFF" style="margin: 0;" onload="initScript();//mostrarCapaCentro('mensaje');" onkeydown="noBack(event);" class="backgroundStandard">
<html:form action="usedGuia.do">
	
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
  	 			<a onclick="goOut('mainpage');hideMenu();" style="">Menú principal</a>
  	 		 
 			   	<logic:present name="loginForm">
					<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion --> 		 			
						<a href="login.do?logout=yes">Terminar sesion</a>
					</logic:equal>
				</logic:present>				  
		   </div>
		  
		</div>	
		
		<table style="width: 100%; height: 100%" class="bodyWidth marginAutoCentro">
			<tr style="width: 100%; height: 100%">
				<td style="height: 8px;">
					<textarea id="mensaje" class="textareacontenidonew" cols="60" readonly="readonly" rows="4" onblur="self.focus();" style="background-color: #EEEEEE; border-style: ridge; 
						overflow: hidden; text-align: center; vertical-align: middle; z-index: 2; visibility: hidden;">
					</textarea>
				</td>
			</tr>
		</table>
		<div class="bodyWidth marginAutoCentro">
			<h2 class="titleSection fontBold">Rastreos Prepago Utilizados</h2>
		</div>
		<table style="width: 800px;" align="center">
			<tr style="width: 100%; height: 100%">
				<td style="width: 100%; height: 100%">
					
		 
					<map name="peMap">
						<area shape="rect" coords="13,19,235,71" href="http://www.paquetexpress.com.mx" target="_blank" alt="www.paquetexpress.com" title="www.paquetexpress.com">
					</map>

					<table class="bodyWidth marginAutoCentro"	 >
	 
						<tr>
							<td>
								<div id="DivRoot01" align="left">
									<div style="overflow: hidden;" id="DivHeaderRow01"></div>
									<div style="overflow: scroll;" onscroll="OnScrollDiv(this,'01')" id="DivMainContent01" >										
										<table id="conjuntoGuias" class="hovertable tablaDetallev2">
											<tr style="height: 40px;" class="tablaDetallev2Header">
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
											</tr>
										<%
										ArrayList conjuntoGuias = (ArrayList)request.getAttribute("conjuntoGuias");
										if (conjuntoGuias != null) {
											if (conjuntoGuias.isEmpty()) {
												for(int i=0;i <= 9; i++){
									%>				
													
										<tr>
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
										</tr>	
									<%				
												}												
											} else {
												for (int i = 0; i < conjuntoGuias.size(); i++) {
										%>
											<tr id="tablerow<%=i%>" onmouseover="this.style.backgroundColor='#FFF0CC'; this.style.cursor='pointer'" 
												onmouseout="this.style.backgroundColor='white';this.style.cursor='default'" onclick="selectedSet('<%=i%>')" 
												title="Seleccione Set de Guias">
												
												<td align="right"><%=((ArrayList)conjuntoGuias.get(i)).get(0)%><input type=hidden name="idSet<%=i%>" id="idSet<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(0)%>"/></td><!-- ID set -->
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
											</tr>										
										<% 			
												}
												if (conjuntoGuias.size()<=9) {
													for (int i=conjuntoGuias.size();i <= 9; i++) {
										%>

											<tr>
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
								<table style="width: 100%;">
									<tr>
										<td class="labelTitle" align="center">
											ID Set
										</td>
										<td class="labelTitle" align="center">
											Zona Km
										</td>
										<td class="labelTitle" align="center">
											Tarifa
										</td>
										<td class="labelTitle" align="center">
											Peso (Kg)
										</td>
										<td class="labelTitle" align="center">
											Volumen
										</td>
										<td class="labelTitle" align="center">
											Acuse de Recibo
										</td>
										<td class="labelTitle" align="center">
											Valor Declarado
										</td>
										<td class="labelTitle" align="center">
											EAD
										</td>
										<td class="labelTitle" align="center">
											RAD
										</td>
										<td class="labelTitle" align="center">
											Zona Ext.
										</td>
										<td class="labelTitle" align="center">
											Set Original
										</td>
										<td class="labelTitle" align="center">
											Factura
										</td>
									</tr>
									<tr>
										<td class="labelText" align="right">
											<bean:write name="usedGuiaForm" property="idSetSel"/>
										</td>										
										<td class="labelText" align="right">
											<bean:write name="usedGuiaForm" property="zonaKmSel"/>
										</td>
										<td class="labelText" align="right">
											<bean:write name="usedGuiaForm" property="tarifaSel"/>
										</td>
										<td class="labelText" align="right">
											<bean:write name="usedGuiaForm" property="pesoKgSel"/>
										</td>
										<td class="labelText" align="right">
											<bean:write name="usedGuiaForm" property="volumenSel"/>
										</td>
										<td class="labelText" align="center">
											<bean:write name="usedGuiaForm" property="acuseSel"/>
										</td>
										<td class="labelText" align="right">
											<bean:write name="usedGuiaForm" property="valorDeclaradoSel"/>
										</td>
										<td class="labelText" align="center">
											<bean:write name="usedGuiaForm" property="EADSel"/>
										</td>
										<td class="labelText" align="center">
											<bean:write name="usedGuiaForm" property="RADSel"/>
										</td>
										<td class="labelText" align="center">
											<bean:write name="usedGuiaForm" property="EXTSel"/>
										</td>
										<td class="labelText" align="right">
											<bean:write name="usedGuiaForm" property="mainSetSel"/>
										</td>
										<td class="labelText" align="right">
											<bean:write name="usedGuiaForm" property="invcSel"/>
										</td>
									</tr>
									<%
									ArrayList detalleGuias = (ArrayList)request.getAttribute("detalleGuias");
									String cveCteProp = "", desCteProp = "";
									if (detalleGuias != null) {
										cveCteProp = ((ArrayList)detalleGuias.get(0)).get(2).toString();
										desCteProp = ((ArrayList)detalleGuias.get(0)).get(3).toString();
									}
									%>
									<tr style="width: 100%;">
										<td colspan="12" style="width: 100%;">
											<table style="width: 100%;">
												<tr style="height: 21px;">
													<td class="labelTitle" align="right" style="width: 16%;">Cliente Propietario:</td>
													<td class="labelText" align="left"><%=cveCteProp %></td>
													<td class="labelText" align="left"><%=desCteProp %></td>													
												</tr>
											</table>
										</td>
									</tr>																	
									<tr style="width: 100%;" align="right">
										<td colspan="12" style="width: 100%; height: 8px;">	</td>										
									</tr>
									<tr style="width: 100%;">
										<td style="width: 70%;" colspan="12">
											<div id="DivRoot02" align="left">
												<div style="overflow: hidden;" id="DivHeaderRow02" ></div>
												<div style="overflow: scroll;" onscroll="OnScrollDiv(this,'02')" id="DivMainContent02" >
													<table id="detalleGuias" class="hovertable" style="visibility: hidden; position: absolute; top: 500px; left: 200px;">
														<tr style="height: 40px;">
															<th style="width: 3%;" nowrap>
																Sel
															</th>
															<th style="width: 10%;">
																Guias
															</th>
															<th style="width: 10%;">
																Guias Relacionadas
															</th>
															<th style="width: 20%;" nowrap>
																Cliente Asignado
															</th>
															<th style="width: 20%;" nowrap>
																Usuario asignado
															</th>
															<th style="width: 20%;" nowrap>
																Destino
															</th>
															<th style="width: 20%;" nowrap>
																Cliente Destino
															</th>
															<th style="width: 31%;" nowrap>
																Contenido
															</th>
														</tr>
														<%
														//ArrayList detalleGuias = (ArrayList)request.getAttribute("detalleGuias");
														String guia = "";
														String numCteAsig = "";
														String numUserAsig = "";

														if (detalleGuias != null) {
															if (detalleGuias.isEmpty()) {																
																for(int i=0; i <= 11; i++){
														%>				
																		
															<tr>
																<td align="right">&nbsp;</td>																
																<td align="right">&nbsp;</td><!-- guia -->
																<td align="right">&nbsp;</td><!-- Guias relacionadas -->
																<td align="left">&nbsp;</td><!-- Nombre cliente asignado -->																
																<td align="left">&nbsp;</td><!-- Nombre usuario asignado -->
																<td align="left">&nbsp;</td><!-- Destino -->
																<td align="left">&nbsp;</td><!-- Nombre Cliente destino -->
																<td align="left">&nbsp;</td><!-- Contenido -->
															</tr>	
														<%				
																}
															} else {															
																for (int i = 0; i < detalleGuias.size(); i++) {
																	guia = ((ArrayList)detalleGuias.get(i)).get(0).toString();
																	numCteAsig = ((ArrayList)detalleGuias.get(i)).get(6).toString();
																	numUserAsig = ((ArrayList)detalleGuias.get(i)).get(8).toString();																	
														%>
															<tr id="tablerow<%=i%>" onmouseover="this.style.backgroundColor='#FFF0CC'; this.style.cursor='pointer'" 
																onmouseout="this.style.backgroundColor='white';this.style.cursor='default'">
																<td align="right">
																</td>
																
																<td align="right"><%=((ArrayList)detalleGuias.get(i)).get(0)%></td><!-- guia -->
																<td align="right"><%=((ArrayList)detalleGuias.get(i)).get(1)%></td><!-- Guias Relacionadas -->
																<td align="left" nowrap title="Num. Cliente:<%=((ArrayList)detalleGuias.get(i)).get(6)%>"><%=((ArrayList)detalleGuias.get(i)).get(7)%></td><!-- Nombre cliente asignado -->																
																<td align="left" nowrap title="ID Usuario:<%=((ArrayList)detalleGuias.get(i)).get(8)%>"><%=((ArrayList)detalleGuias.get(i)).get(9)%></td><!-- Nombre usuario asignado -->
																<td align="left" nowrap><%=((ArrayList)detalleGuias.get(i)).get(13)%></td><!-- Destino -->
																<td align="left" nowrap><%=((ArrayList)detalleGuias.get(i)).get(15)%></td><!-- Nombre Cliente destino -->
																<td align="left" nowrap><%=((ArrayList)detalleGuias.get(i)).get(16)%></td><!-- Contenido -->
															</tr>
														<%
																}
																
																if (detalleGuias.size()<11) {
																	for(int i=detalleGuias.size();i <= 11; i++) {
														%>
															<tr>
																<td align="right">&nbsp;</td>																
																<td align="right">&nbsp;</td><!-- guia -->
																<td align="right">&nbsp;</td><!-- Guias relacionadas -->
																<td align="left">&nbsp;</td><!-- Nombre cliente asignado -->
																<td align="left">&nbsp;</td><!-- Nombre usuario asignado -->
																<td align="left">&nbsp;</td><!-- Destino -->
																<td align="left">&nbsp;</td><!-- Nombre Cliente destino -->
																<td align="left">&nbsp;</td><!-- Contenido -->
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
								</table>
								</div>
							</td>
						</tr>
						<tr style="width: 100%;">
							<td style="width: 100%;">
								<p>
							</td>
						</tr>						
						<tr align="center">
							<td>
								<input type="button" class="button buttonLarge" onClick="mainpage()" value="Regresar" name="regresar">								
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
	</html:form>
</body>
</html>