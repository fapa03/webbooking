<!DOCTYPE html>
<%--
Author		:	JOSE MANUEL ARMENTA LOPEZ
Date		:	01/07/2017
Description :	Módulo para reimprimir las guías o etiquetas que el usuario seleccione
 * -----------------------------------------------------------------
 * Clave: AAP01
 * Autor: ABRAHAM DANIEL ARJONA PERAZA.
 * Fecha: 03/12/2018
 * Descripción: Se agregan columas de fecha (ISSE_DATE) y referencias. 
 * Funcionalidad para marcar si quiere imprimir la cantidad de CP configuradas por cliente.
 * ----------------------------------------------------------------- 
--%>
<%@ page language="java"%>
<%@page
	import="java.util.List, java.util.ArrayList,
	mx.com.paquetexpress.reprintGuia.form.JavReprintGuiaForm,
	bean.Global,
	mx.com.paquetexpress.reprintGuia.dto.JavReprintEtiquetaDTO,
	mx.com.paquetexpress.reprintGuia.dto.JavReprintGuiaDTO,
	java.io.*,
	java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String version = "00.00.04";
	Global global = (Global) session.getAttribute("sGlobal");
%>
<html:html>
<HEAD>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<TITLE>Reimpresión de Guías</TITLE>
<META http-equiv=Content-Type content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="css/bootstrap.css?v=<%=version%>">
<link rel=stylesheet media=screen type=text/css
	href="webbooking.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css"
	href="css/stylev2.css?v=<%=version%>">
<link rel="stylesheet" media="screen" type="text/css"
	href="css/v2/materialDesign.css?v=<%=version%>">
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<script type="text/javascript" src="barraEspera.js?v=<%=version%>"></script>
<script type="text/javascript" src="js/jquery/1.12.0.js?v=<%=version%>"></script>
<script type="text/javascript" src="js/v2/global.js?v=<%=version%>"></script>
<script src="js/bootstrap.min.js?v=<%=version%>"></script>
<script type="text/javascript" src="js/JavReprintGuia.js?v=<%=version%>"></script>

<script type="text/javascript" charset="UTF-8"
	src="js/qz/bridgePrintQZ.js"></script>
<link rel="stylesheet" media="screen" type="text/css"
	href="css/QZTray.css">

</HEAD>
<BODY id="body" text=#000000 leftMargin=0 topMargin=0 marginheight="0"
	marginwidth="0" onload="initScript(); mostrarCapaCentro('mensaje');"
	onscroll="mostrarCapaCentro('mensaje');" class="backgroundStandard">
	<html:form action="reprintGuia.do"
		type="mx.com.paquetexpress.reprintGuia.form.JavReprintGuiaForm">
		<table border="0" cellspacing="0" cellpadding="0"
			class="width100porcent backgroudBluePtx">
			<tr>
				<td>
					<div class="bodyWidth marginAutoCentro">
						<img src="images/logos/logoW.png" border="0"
							onclick="goOut('mainpage');hideMenu();"
							style="max-width: 138px; margin-top: 11px; margin-bottom: 9px; cursor: pointer;">


						<a id="menu-hamburger" class="Animated1" onclick="showMenu()">

							<div style="margin-top: 15px;">
				              <span id="menuline1" class="Animated05"></span> 
				              <span id="menuline2" class="Animated05"></span> 
				              <span id="menuline3" class="Animated05"></span>
				            </div>

						</a>
					</div>
				</td>
				<td></td>
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
					<logic:equal name="loginForm" property="userValidate"
						value="validUser">
						<a onclick="goOut('mainpage');hideMenu();" style="">Menú
							Principal</a>
						<a onclick="goOut('cliententry');hideMenu();" style="">Registro
							de Cliente Destino</a>
						<logic:equal name="loginForm" property="showWeb" value="Y">
		         		<logic:equal name="loginForm" property="newBooking" value="Y">
		         			<a onclick="goOut('thispage');hideMenu();" style="">Elaboración de guías</a>
  						</logic:equal>
	  					</logic:equal> 
	  					<logic:equal name="loginForm" property="showPpg" value="Y">
								<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
			  			</logic:equal>
						<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelación
							de Guía</a>
						<a onclick="goOut('shipmenthistory');hideMenu();" style="">Histórico
							de Envíos</a>
						<a onclick="goOut('clientreport');hideMenu();" style="">Información
							del Cliente</a>
						<logic:present name="loginForm">
							<logic:equal name="loginForm" property="milliSeconds" value="">
								<!-- si no viene de Customer central, se muestra cerrar sesion -->
								<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar
									sesión</a>
							</logic:equal>
						</logic:present>
					</logic:equal>
				</logic:present>
				<logic:present name="loginForm">
					<html:hidden name="loginForm" property="userValidate" />
					<logic:notEqual name="loginForm" property="userValidate"
						value="validUser">

						<a onclick="goOut('mainpage');hideMenu();" style="">Menú
							principal</a>
						<logic:equal name="loginForm" property="showWeb" value="Y">
		    			<logic:equal name="loginForm" property="newBooking" value="Y">
		    				<a onclick="goOut('guiabooking');hideMenu();" style="">Elaboración de guías</a>
	  					</logic:equal>
		  				</logic:equal> 
		  				<html:hidden name="loginForm" property="showPpg"></html:hidden>
						<logic:equal name="loginForm" property="showPpg" value="Y">
						<a onclick="goOut('guiaPP');hideMenu();">Elaboración de guías prepago</a>
				</logic:equal>	
						<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelación
							de guías</a>
						<a onclick="goOut('shipmenthistory');hideMenu();" style="">Historico
							de envios</a>
						<a onclick="goOut('clientreport');hideMenu();" style="">Información
							de cliente</a>
						<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar
							sesión</a>
					</logic:notEqual>
				</logic:present>
			</div>
		</div>
		<table>
			<tr>
				<td style="height: 8px;"><textarea id="mensaje"
						class="textareacontenido" cols="46" readonly="readonly" rows="4"
						onblur="self.focus();"
						style="background-color: #EEEEEE; border-style: ridge; overflow: hidden; text-align: center; vertical-align: middle; z-index: 2; visibility: hidden; position: absolute; top: 450px; left: 200px; width: 350px; height: 80px">
					</textarea></td>
			</tr>
		</table>

		<jsp:include page="nocache.jsp" flush="false" />

		<table class="bodyWigth2 marginAutoCentro">
			<tr>
				<td><strong class="titleSection fontBold">Reimpresión de Guías</strong>
			</tr>
		</table>
		<%
			String disabledButtons = "";
		%>
		<div class="marginAutoCentro bodyWigth2" style="margin-bottom: 5px;">
			<input type="text" class="search"
				placeholder="Escriba para buscar una guía, rastreo o referencia"
				style="width: 50%;" />
			<logic:present name="sGlobal">
				<!-- AAP01 -->
				<html:checkbox property="checkSStotal"></html:checkbox>
					Total de S.S.:&nbsp;<strong><bean:write name="sGlobal"
						property="SStotal" /></strong>
			</logic:present>

			<span style="float: right;"> <!-- AAP01 --> <input
				id="buttonPrint" type="button" class="button buttonMedium"
				value="Imprimir" name="buttonPrint" onclick="return printGuias()"
				<%=disabledButtons%> /> <input id="buttonRefresh" type="button"
				class="button buttonMedium" value="Actualiza" name="buttonRefresh"
				onclick="return refreshGuias()" /> <!-- AAP01 -->
			</span>
			<!-- AAP01 -->

		</div>
		<logic:present name="errormsgprint" scope="request">
			<tr>
				<td>
			 		<div align="center"> 
				 		<font style="font-size: 12pt; color: #ff0000; font-weight: bold"><bean:write name="errormsgprint" /></font>
				 	</div>
				</td>
			</tr>
		</logic:present>
				
		<div style="max-height: 75vh; overflow-y: auto;">

			<table border="0" width="100%" cellpadding="0"
				class="marginAutoCentro bodyWidth tablaDetallev2"
				style="max-height: 450px !important; overflow-y: auto;"
				id="mainTable">
				<tr class="tablaDetallev2Header">

					<th width="106" height="41" valign="bottom" align="left"><font
						face="Arial" size="2">No Guia</font></th>

					<th width="109" height="41" valign="bottom" align="left"><font
						face="Arial" size="2">No. Rastreo</font></th>

					<th width="169" height="41" valign="bottom" align="left"><font
						face="Arial" size="2">Referencias</font></th>

					<th width="106" height="41" valign="bottom" align="left"><font
						face="Arial" size="2">Fecha creacion</font></th>

					<th width="169" height="41" valign="bottom" align="left"><font
						face="Arial" size="2">Sucursal Destino</font></th>

					<th width="227" height="41" valign="bottom" align="left"><font
						face="Arial" size="2">Cliente Destino</font></th>

					<th width="101" height="41" valign="bottom" align="right">
						<p align="right">
							<font face="Arial" size="2">Importe</font>
					</th>

					<th width="110" height="41" valign="bottom" align="right">
						<p align="center">
							<font face="Arial" size="2">No.de<br> Paquetes
							</font>
					</th>

					<th width="20" height="41" valign="bottom" align="right">
						<p align="center">
							<font face="Arial" size="2">S.S. </font> <input type="checkbox"
								id="checkAllguias" name="checkAllGuias" checked="checked"
								onclick="toggleAllGuias()">
					</th>
					<th width="20" height="41" valign="bottom" align="right">
						<p align="center">
							<font face="Arial" size="2">Etiquetas<br />
							</font> <input type="checkbox" id="checkAllEtiquetas"
								name="checkAllEtiquetas" checked="checked"
								onclick="toggleAllEtiquetas()">
					</th>

					<th width="101" height="41"></th>
				</tr>
				<%
							ArrayList<JavReprintGuiaDTO> listGuias = (ArrayList<JavReprintGuiaDTO>) session
									.getAttribute("listGuias");
							String linkGenCartaPorte = (String) request.getAttribute("linkGenCartaPorte");
							int lenListGuia = listGuias.size();
							disabledButtons = lenListGuia > 0 ? "" : "disabled";
							String pdfFormat = "Y".equalsIgnoreCase(global.getPdfFormat()) ? "&measure=4x6" : "";
							for (int i = 0; i < lenListGuia; i++) {
								JavReprintGuiaDTO guia = (JavReprintGuiaDTO) listGuias.get(i);
								String checked = guia.isChecked() ? "checked" : "";
				%>

				<tr class="tablaDetallev2Body guiaRow">
					<td width="60" nowrap class="td" align="left" style="height: 41px !important;">
						<%=guia.getOrigBranch()%><%=guia.getFormNumber()%>
					</td>
					<td width="80" nowrap class="td" style="height: 41px !important; !important;inline-size: auto;">
						<a title="Generar carta porte PDF" href="<%=linkGenCartaPorte + guia.getGuiaNumber() + pdfFormat%>" target="_blank">
							<%=guia.getGuiaNumber()%>&nbsp;&nbsp;<img height="16" width="16" src="images/download_PDF_small.png">
						</a>
						<input type="hidden" name="Guias"
						value="<%=guia.getGuiaNumber()%>"></td>
					<td width="169" class="td" style="height: 41px !important;">&nbsp;<%=guia.getRefers()%></td>
					<!-- AAP01 -->
					<td width="60" nowrap class="td" align="left"
						style="height: 41px !important;"><%=guia.getIsseDate()%></td>
					<!-- AAP01 -->
					<td width="150" nowrap class="td" style="height: 41px !important;">&nbsp;<%=guia.getDestBranch()%></td>
					<td width="275" nowrap class="td" style="height: 41px !important;">&nbsp;<%=guia.getDestClientName()%></td>
					<td width="75" nowrap class="td" align="right"
						style="height: 41px !important;">&nbsp;<%=guia.getGuiaAmount()%></td>
					<td width="60" nowrap class="td" align="right"
						style="height: 41px !important;">&nbsp;<%=guia.getNumPack()%></td>
					<td width="20" nowrap class="td" style="height: 41px !important;"
						align="center"><input type="checkbox" name="checkGuia"
						value="<%=guia.getGuiaNumber()%>" checked="<%=checked%>"></td>
					<td width="20" nowrap class="td" style="height: 41px !important;"
						align="center"><input type="checkbox"
						name="checkEtiquetas<%=guia.getGuiaNumber()%>"
						id="checkEtiquetas<%=guia.getGuiaNumber()%>"
						value="<%=guia.getGuiaNumber()%>" checked="checked"
						onclick="toggleEtiqByGuia('<%=guia.getGuiaNumber()%>')">
						<div style="cursor: pointer; display: inline;"
							onclick="botonAcordeon('<%=guia.getGuiaNumber()%>')">
							<div id="totEtiq<%=guia.getGuiaNumber()%>"
								style="display: inline;"><%=guia.getListEtiquetas().size()%></div>
							/
							<div id="numEtiqGuia" style="display: inline;"><%=guia.getListEtiquetas().size()%></div>
						</div></td>
				</tr>
				<tr id="trEtiquetas<%=guia.getGuiaNumber()%>" style="display: none;"
					class="trEtiquetas">
					<td colspan="10" align="center">
						<table>
							<tr class="tablaDetallev2Header">
								<th width=2 height="30">&nbsp;</th>

								<th width="109" height="20" valign="bottom" align="left"><font
									face="Arial" size="2">No. Rastreo</font></th>

								<th width="169" height="20" valign="bottom" align="left"><font
									face="Arial" size="2">Descripción</font></th>

								<th width="227" height="20" valign="bottom" align="left"><font
									face="Arial" size="2">Contenido</font></th>

								<th width="101" height="20" valign="bottom" align="left"><font
									face="Arial" size="2">Etiqueta</font></th>
							</tr>
							<%
								ArrayList<JavReprintEtiquetaDTO> listEtiquetas = guia.getListEtiquetas();
											int lenListEtiquetas = listEtiquetas.size();
											for (int j = 0; j < lenListEtiquetas; j++) {
												JavReprintEtiquetaDTO etiqueta = (JavReprintEtiquetaDTO) listEtiquetas.get(j);
												String checkedEtiqueta = etiqueta.isChecked() ? "checked" : "";
							%>

							<tr>

								<td width=2 style="height: 41px !important;">&nbsp;</td>
								<td width="80" nowrap class="td"
									style="height: 41px !important;">&nbsp;<%=etiqueta.getGuiaNo()%>
								<td width="150" nowrap class="td"
									style="height: 41px !important;">&nbsp;<%=etiqueta.getGuiaDesc()%>
								<td width="275" nowrap class="td"
									style="height: 41px !important;">&nbsp;<%=etiqueta.getContent()%>
								<td width="75" nowrap class="td" align="right"
									style="height: 41px !important;">&nbsp;<%=etiqueta.getQuantity()%></td>

								<td width="20" nowrap class="td"
									style="height: 41px !important;" align="center"><input
									type="checkbox"
									id="checkEtiquetaDtl<%=guia.getGuiaNumber() + "-" + etiqueta.getQuantity()%>"
									name="checkEtiquetaDtl"
									value="<%=guia.getGuiaNumber() + "-" + etiqueta.getQuantity()%>"
									checked="<%=checkedEtiqueta%>"
									onclick="toggleEtiqueta('<%=guia.getGuiaNumber()%>','<%=etiqueta.getQuantity()%>')">
									<input type="hidden" name="etiquetas"
									value="<%=guia.getGuiaNumber()%>-<%=etiqueta.getQuantity()%>">
								</td>

							</tr>


							<%
								}
							%>
						</table>

					</td>
				</tr>
				<%
					}
							if (lenListGuia < 10)
								for (int j = lenListGuia; j <= 10; j++) {
				%><tr>


					<td width="60" height="17" nowrap class="td" align="right">&nbsp;</td>
					<td width="80" height="17" nowrap class="td">&nbsp;</td>
					<!-- AAP01 -->
					<td width="169" height="17" nowrap class="td">&nbsp;</td>
					<!-- AAP01 -->
					<td width="60" height="17" nowrap class="td">&nbsp;</td>
					<td width="150" height="17" nowrap class="td">&nbsp;</td>
					<td width="275" height="17" nowrap class="td">&nbsp;</td>
					<td width="75" height="17" nowrap class="td">&nbsp;</td>
					<td width="60" height="17" nowrap class="td">&nbsp;</td>
					<td width="20" height="17" nowrap class="td">&nbsp;</td>
					<td width="20" height="17" nowrap class="td">&nbsp;</td>
					<!-- AAP01 -->
				</tr>

				<%
					}
				%>
			</table>
		</div>
		<html:hidden property="currentTask" />
		<input type="hidden" id="cadena" name="cadena"
			value='<bean:write name="cadena"/>'>
		<input type="hidden" id="allowPrintQZ" name="allowPrintQZ"
			value='<%=global.getAllowPrintQZ()%>'>
		<div id="zonePrintQZ"></div>
	</html:form>


</BODY>
</html:html>
