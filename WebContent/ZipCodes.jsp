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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<link rel="stylesheet" media="screen" type="text/css" href="webbookingMonitor.css">
<link rel="stylesheet" media="screen" type="text/css" href="webbooking.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/stylev2.css">
<link rel="stylesheet" media="screen" type="text/css" href="css/v2/materialDesign.css">

<script type="text/javascript" src="ZipCodes.js"></script>
<script type="text/javascript" src="common.js"></script>
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
<script type="text/javascript" src="barraEspera.js"></script>
<title>Consulta Codigos Postales</title>
</head>
<body id="body" onload="initScript();//mostrarCapaCentro('mensaje');" onkeydown="noBack(event);" class="backgroundStandard widthOverflow" style="margin:0px !important;">

<html:form action="zipCodes.do">
		
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
			<tr>
				<td>		 
					<table>
						<tr style="height: 40px;">
							<td align="left" class="titleSection fontBold" >
								B&uacute;squeda de c&oacute;digos postales								
							</td>
						</tr>
						<tr>
							<td title="Ingrese una palabra clave para buscar por:
Código postal
Ciudad o población
Delegación o Municipio.
También puede utilizar las siguientes combinaciones usando el símbolo más (+):
1.-Ciudad/Población/Delegación/Municipio + Estado.
2.-Colonia + Ciudad/Población/Delegación/Municipio + Estado.
							">
								B&uacute;queda R&aacute;pida: <html:text property="find" styleId="find" onkeypress="fastFindKey();" style="width: 300px;" styleClass="upper"></html:text><input type="button" name="findByZipCode" value="..." class="lov" onclick="fastFind();">
								<hr style="color: #0056b2;" />
							</td>
						</tr>
						<tr style="width: 100%">
							<td style="width: 100%">
								<button class="accordion" type="button" id="btnAcordeon">B&uacute;squeda a detalle</button>
								<div class="panel" id="divAcordeon">
								<table style="width: 100%">
									<tr style="width: 100%">
										<td>Pa&iacute;s&nbsp;</td>
										<td>Zona&nbsp;</td>
										<td>Estado&nbsp;</td>
										<td>Delegaci&oacute;n/Municipio&nbsp;</td>
										<td>Ciudad/Poblaci&oacute;n&nbsp;</td>
										<td>&nbsp;</td>
									</tr>
									<tr>
										<td>
											<html:select property="zcPais" styleClass= "labelText" onchange="" style="width: 150px;">
												<html:options property="zcPaisValue" labelProperty="zcPaisLabel" />
											</html:select>
											&nbsp;
										</td>
										<td>
											<html:select property="zcZona" styleClass= "labelText" onchange="" style="width: 150px;">
												<html:options property="zcZonaValue" labelProperty="zcZonaLabel" />
											</html:select>
											&nbsp;
										</td>
										<td>
											<html:select property="zcEstado" styleClass= "labelText" onchange="selectEstado();" style="width: 150px;">
												<html:options property="zcEstadoValue" labelProperty="zcEstadoLabel" />
											</html:select>
											&nbsp;
										</td>
										<td>
											<html:select property="zcDelMun" styleClass= "labelText" onchange="selectDelMun();" style="width: 150px;"> 
												<html:options property="zcDelMunValue" labelProperty="zcDelMunLabel" />
											</html:select>
											&nbsp;
										</td>
										<td>
											<html:select property="zcCiuPob" styleClass= "labelText" onchange="" style="width: 150px;">
												<html:options property="zcCiuPobValue" labelProperty="zcCiuPobLabel" />
											</html:select>
											&nbsp;
										</td>
										<td></td>
									</tr>
									<tr style="height: 40px;">
										<td align="center" colspan="6">
											Colonia / Asentamiento:&nbsp;<html:text property="colonia" onkeypress="findDetailsKey();" maxlength="150" style="width: 300px;" styleClass="upper" onblur="setBack('true');" onfocus="setBack('false');"></html:text>&nbsp;<input type="button" name="findByEntities" value="Buscar" class="button3 buttonLarge" onclick="findDetails();">
										</td>
									</tr>
								</table>
								</div>
							</td>
						</tr>
						
						<!--
						<tr style="height: 40px;">
							<td align="center" title="Capture palabras clave" >
								<hr style="color: #0056b2;" />
								B&uacute;squeda<html:text property="findText" maxlength="150" style="width: 300px;"></html:text>
							</td>
						</tr>
						-->
						<tr>
							<td>
								<div id="DivRoot01" align="left">
 
									<div style="overflow: hidden;height: 66px;" id="DivHeaderRow01"></div>
									<div style="overflow: scroll;" onscroll="OnScrollDiv(this,'01')" id="DivMainContent01" >									
																			
										<table id="conjuntoGuias" class="tablaDetallev2" style="width: 100%">
											<tr class="tablaDetallev2Header headerFFF" width="100%" style="background: #fff !important;height: 40px;">
												<th>C&oacute;digo Postal</th>
												<th>Colonia</th>
												<th>Sucursal</th>
												<th>Ciudad/Poblaci&oacute;n</th>
												<th>Delegaci&oacute;n/Municipio</th>
												<th>Estado</th>
												<th>Pais</th>												
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
											<td>&nbsp;</td><!-- Num Rastreos -->											
										</tr>	
									<%				
												}												
											} else {
												for (int i = 0; i < conjuntoGuias.size(); i++) {
										%>
											<tr id="tablerow<%=i%>" onmouseover="this.style.backgroundColor='#D0D2C5'; this.style.cursor='pointer'" 
												onmouseout="validColor(this);" onclick="changeColor(this);selectedSet('<%=i%>')" 
												title="Seleccione Set de Guias" class="tablaDetallev2Body" width="100%">
												
												<td align="center"><%=((ArrayList)conjuntoGuias.get(i)).get(0)%></td><!-- Codigo postal -->
												<td align="left"><%=((ArrayList)conjuntoGuias.get(i)).get(1)%></td><!-- colonia -->
												<td align="left"><%=((ArrayList)conjuntoGuias.get(i)).get(5)%></td><!-- sucursal -->
												<td align="left"><%=((ArrayList)conjuntoGuias.get(i)).get(6)%></td><!-- ciudad -->
												<td align="left"><%=((ArrayList)conjuntoGuias.get(i)).get(10)%></td><!-- delegacion/municipio -->
												<td align="left"><%=((ArrayList)conjuntoGuias.get(i)).get(14)%></td><!-- estado -->
												<td align="left"><%=((ArrayList)conjuntoGuias.get(i)).get(18)%> <!-- pais -->
													<!-- inicio hiddens en ultimo td -->
													<input type=hidden name="zipCode<%=i%>" id="zipCode<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(0)%>"/>
													<input type=hidden name="colDes<%=i%>" id="colDes<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(1)%>"/>
													<input type=hidden name="colCod<%=i%>" id="colCod<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(2)%>"/>
													<input type=hidden name="colLev<%=i%>" id="colLev<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(3)%>"/>
													<input type=hidden name="colType<%=i%>" id="colType<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(4)%>"/>
													<input type=hidden name="idSuc<%=i%>" id="idSuc<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(5)%>"/>
													<input type=hidden name="ciuDes<%=i%>" id="ciuDes<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(6)%>"/>
													<input type=hidden name="ciuCod<%=i%>" id="ciuCod<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(7)%>"/>
													<input type=hidden name="ciuLev<%=i%>" id="ciuLev<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(8)%>"/>
													<input type=hidden name="ciuType<%=i%>" id="ciuType<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(9)%>"/>
													<input type=hidden name="munDes<%=i%>" id="munDes<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(10)%>"/>
													<input type=hidden name="munCod<%=i%>" id="munCod<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(11)%>"/>
													<input type=hidden name="munLev<%=i%>" id="munLev<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(12)%>"/>
													<input type=hidden name="munType<%=i%>" id="munType<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(13)%>"/>
													<input type=hidden name="edoDes<%=i%>" id="edoDes<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(14)%>"/>
													<input type=hidden name="edoCod<%=i%>" id="edoCod<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(15)%>"/>
													<input type=hidden name="edoLev<%=i%>" id="edoLev<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(16)%>"/>
													<input type=hidden name="edoType<%=i%>" id="edoType<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(17)%>"/>
													<input type=hidden name="paisDes<%=i%>" id="paisDes<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(18)%>"/>
													<input type=hidden name="paisCod<%=i%>" id="paisCod<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(19)%>"/>
													<input type=hidden name="paisLev<%=i%>" id="paisLev<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(20)%>"/>
													<input type=hidden name="paisType<%=i%>" id="paisType<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(21)%>"/>
													<input type=hidden name="zonaDes<%=i%>" id="zonaDes<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(22)%>"/>
													<input type=hidden name="zonaCod<%=i%>" id="zonaCod<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(23)%>"/>
													<input type=hidden name="zonaLev<%=i%>" id="zonaLev<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(24)%>"/>
													<input type=hidden name="zonaType<%=i%>" id="zonaType<%=i%>" value="<%=((ArrayList)conjuntoGuias.get(i)).get(25)%>"/>
													<!-- fin hiddens en ultimo td -->
												</td>												
											</tr>										
										<% 			
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
						<tr align="center">
							<td style="height: 35px;">
								<input type="button" class="button3 buttonLarge" onClick="window.close();" value="Regresar" name="regresar">
								<input type="button" class="button3 buttonLarge" onClick="asigValselect();" value="Aceptar" name="aceptar">								
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
		<html:hidden property="currentTask"/>
		<html:hidden property="zipCodeSel"/>
		<html:hidden property="colDesSel"/>
		<html:hidden property="colCodSel"/>
		<html:hidden property="colLevSel"/>
		<html:hidden property="colTypeSel"/>
		<html:hidden property="idSucSel"/>
		<html:hidden property="ciuDesSel"/>
		<html:hidden property="ciuCodSel"/>
		<html:hidden property="ciuLevSel"/>
		<html:hidden property="ciuTypeSel"/>
		<html:hidden property="munDesSel"/>
		<html:hidden property="munCodSel"/>
		<html:hidden property="munLevSel"/>		
		<html:hidden property="munTypeSel"/>
		<html:hidden property="edoDesSel"/>
		<html:hidden property="edoCodSel"/>
		<html:hidden property="edoLevSel"/>
		<html:hidden property="edoTypeSel"/>
		<html:hidden property="paisDesSel"/>
		<html:hidden property="paisCodSel"/>
		<html:hidden property="paisLevSel"/>
		<html:hidden property="paisTypeSel"/>
		<html:hidden property="zonaDesSel"/>
		<html:hidden property="paisCodSel"/>
		<html:hidden property="paisLevSel"/>
		<html:hidden property="paisTypeSel"/>
		
		<html:hidden property="msjeErr"/>
		<html:hidden property="valAcordion"/>
	</html:form>
</body>
</html>