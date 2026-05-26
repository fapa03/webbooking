<!DOCTYPE html>
<%--
Author		:	PACHECO PARRA EVA MELISSA 
Date		:	10/12/2023
Description :	Módulo para realizar la cancelacion de guias masivas 
 * -----------------------------------------------------------------
 * Clave: 
 * Autor: 
 * Fecha:
 * Descripción: 
 * ----------------------------------------------------------------- 
--%>
<%@ page language="java"%>
<%@page
	import="java.util.List, 
	java.util.ArrayList,
	mx.com.paquetexpress.cancelGuiaMult.form.JavCancelGuiaMultForm,
	bean.Global,
	mx.com.paquetexpress.cancelGuiaMult.dto.JavCancelGuiaMultDTO,
	java.io.*,
	java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%
	String version = "00.00.17";
	Global global = (Global) session.getAttribute("sGlobal");
%>
<html:html>
<HEAD>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<TITLE>Cancelaci&oacute;n de Gu&iacute;as M&uacute;ltiples</TITLE>
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
<script type="text/javascript" src="js/JavCancelGuiaMult.js?v=<%=version%>"></script>

<script type="text/javascript" charset="UTF-8"
	src="js/qz/bridgePrintQZ.js"></script>
<link rel="stylesheet" media="screen" type="text/css"
	href="css/QZTray.css">
</HEAD>
<BODY id="body" text=#000000 leftMargin=0 topMargin=0 marginheight="0"
	marginwidth="0" onload="initScript(); mostrarCapaCentro('mensaje');"
	onscroll="mostrarCapaCentro('mensaje');" class="backgroundStandard">
	<html:form action="cancelGuiaMult.do"
		type="mx.com.paquetexpress.cancelGuiaMult.form.JavCancelGuiaMultForm">
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
						<a onclick="goOut('mainpage');hideMenu();" style="">Men&uacute;
							Principal</a>
						<a onclick="goOut('cliententry');hideMenu();" style="">Registro
							de Cliente Destino</a>
						<logic:equal name="loginForm" property="showWeb" value="Y">
		         		<logic:equal name="loginForm" property="newBooking" value="Y">
		         			<a onclick="goOut('thispage');hideMenu();" style="">Elaboraci&oacute;n de gu&iacute;as</a>
  						</logic:equal>
	  					</logic:equal> 
	  					<logic:equal name="loginForm" property="showPpg" value="Y">
								<a onclick="goOut('guiaPP');hideMenu();">Elaboraci&oacute;n de gu&iacute;as prepago</a>
			  			</logic:equal>
						<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelaci&oacute;n
							de Gu&iacute;a</a>
						<a onclick="goOut('shipmenthistory');hideMenu();" style="">Hist&oacute;rico
							de Env&iacute;os</a>
						<a onclick="goOut('clientreport');hideMenu();" style="">Informaci&oacute;n
							del Cliente</a>
						<logic:present name="loginForm">
							<logic:equal name="loginForm" property="milliSeconds" value="">
								<!-- si no viene de Customer central, se muestra cerrar sesion -->
								<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar
									sesi&oacute;n</a>
							</logic:equal>
						</logic:present>
					</logic:equal>
				</logic:present>
				<logic:present name="loginForm">
					<html:hidden name="loginForm" property="userValidate" />
					<logic:notEqual name="loginForm" property="userValidate"
						value="validUser">

						<a onclick="goOut('mainpage');hideMenu();" style="">Men&uacute;
							principal</a>
						<logic:equal name="loginForm" property="showWeb" value="Y">
		    			<logic:equal name="loginForm" property="newBooking" value="Y">
		    				<a onclick="goOut('guiabooking');hideMenu();" style="">Elaboraci&oacute;n de gu&iacute;as</a>
	  					</logic:equal>
		  				</logic:equal> 
		  				<html:hidden name="loginForm" property="showPpg"></html:hidden>
						<logic:equal name="loginForm" property="showPpg" value="Y">
						<a onclick="goOut('guiaPP');hideMenu();">Elaboraci&oacute;n de gu&iacute;as prepago</a>
				</logic:equal>	
						<a onclick="goOut('guiacancel');hideMenu();" style="">Cancelaci&oacute;n
							de gu&iacute;as</a>
						<a onclick="goOut('shipmenthistory');hideMenu();" style="">Historico
							de envios</a>
						<a onclick="goOut('clientreport');hideMenu();" style="">Informaci&oacute;n
							de cliente</a>
						<a onclick="hideMenu();" href="login.do?logout=yes" style="">Cerrar
							sesi&oacute;n</a>
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
				<td><strong class="titleSection fontBold">Cancelaci&oacute;n de Gu&iacute;as M&uacute;ltiples</strong>
			</tr>
		</table>
		<%
			String disabledButtons = "";
		%>
		<div class="marginAutoCentro bodyWigth2" style="margin-bottom: 5px;">
			<label for="dateStart">Fecha inicial:</label>
			<input type="date" class="searchFecha" id="fechaInicio" 
				placeholder="Fecha de inicio"
				style="width: 10%;" /> 
		
			  <label for="dateEnd">Fecha final:</label>
			<input type="date" class="searchFecha" id="fechaFin"  
				placeholder="Fechafinal"
				style="width: 10%;" />
			<input
					id="buttonSearchFecha" type="button" class="button" style="width: auto;" value="Buscar Fechas"  name="buttonSearchFecha" 
					onclick="return buscarRangoFechas()"
					<%=disabledButtons%> /> 	
				
			<input type="text" class="search" 
				placeholder="Escriba para buscar una gu&iacute;a o rastreo"
				style="width: 50%; margin-left: 1%; float: right;" />
		</div>
		<div class="marginAutoCentro bodyWigth2" style="margin-bottom: 5px;">
			<label for="dateStart">Filtrar gu&iacute;as por:</label>
			<html:radio property="filtroPor" value="1" onchange="cambioFiltroPor();"></html:radio> Gu&iacute;as documentaci&oacute;n en linea.
			<html:radio property="filtroPor" value="2" onchange="cambioFiltroPor();"></html:radio> Gu&iacute;as ECommerce (WE) <bean:write name="sGlobal" property ="clientIdAgreement"/> - <bean:write name="sGlobal" property ="clientName"/> 
		</div>
		
		<div style="max-height: 65vh; overflow-y: auto;">
			<table border="0" width="100%" cellpadding="0"
				class="marginAutoCentro bodyWidth tablaDetallev2"
				style="max-height: 450px !important; overflow-y: auto;"
				id="mainTable">
				<tr class="tablaDetallev2Header">

					<th width="106" height="41" valign="bottom" align="left"><font
						face="Arial" size="2">No Gu&iacute;a</font></th>

					<th width="109" height="41" valign="bottom" align="left"><font
						face="Arial" size="2">No. Rastreo</font></th>
					
					<th width="169" height="41" valign="bottom" align="left"><font
						face="Arial" size="2">Referencias</font></th>
					
					<th width="106" height="41" valign="bottom" align="left"><font
						face="Arial" size="2">Fecha creaci&oacute;n</font></th>

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
							<font face="Arial" size="2">No.de<br> Paquetes	</font>
					</th>

					<th width="20" height="41" valign="bottom" align="right">
						<p align="center">
							<font face="Arial" size="2">Seleccionar todos </font> <input type="checkbox"
								id="checkAllguias" name="checkAllGuias" 
								onclick="toggleAllGuias()">
					</th>
					<th width="101" height="41"></th>
				</tr>
				<%
							ArrayList<JavCancelGuiaMultDTO> listGuias = (ArrayList<JavCancelGuiaMultDTO>) session
									.getAttribute("listGuias");
							String linkGenCartaPorte = (String) request.getAttribute("linkGenCartaPorte");
							int lenListGuia = listGuias.size();
							disabledButtons = lenListGuia > 0 ? "" : "disabled";
							for (int i = 0; i < lenListGuia; i++) {
								JavCancelGuiaMultDTO guia = (JavCancelGuiaMultDTO) listGuias.get(i);
								String checked = guia.isChecked() ? "checked" : "";
				%>

				<tr class="tablaDetallev2Body guiaRow">
					<td width="60" nowrap class="td" align="left" style="height: 41px !important;">
						<%=guia.getOrigBranch()%><%=guia.getFormNumber()%>
					</td>
					<td width="80" nowrap class="td" style="height: 41px !important; !important;inline-size: auto;">
						<a title="Generar carta porte PDF" href="<%=linkGenCartaPorte + guia.getGuiaNumber() + "&source=cancelGuiaMult"%>" target="_blank">
							<%=guia.getGuiaNumber()%>&nbsp;&nbsp;<img height="16" width="16" src="images/download_PDF_small.png">
						</a>
						<input type="hidden" name="Guias"
						value="<%=guia.getGuiaNumber()%>"></td>
					<td width="169" class="td" style="height: 41px !important;">&nbsp;<%=guia.getRefers()%></td>
					<td width="60" nowrap class="td" align="left"
						style="height: 41px !important;"><%=guia.getIsseDate()%></td>
					<td width="150" nowrap class="td" style="height: 41px !important;">&nbsp;<%=guia.getDestBranch()%></td>
					<td width="275" nowrap class="td" style="height: 41px !important;">&nbsp;<%=guia.getDestClientName()%></td>
					<td width="75" nowrap class="td" align="right"
						style="height: 41px !important;">&nbsp;<%=guia.getGuiaAmount()%></td>
					<td width="60" nowrap class="td" align="right"
						style="height: 41px !important;">&nbsp;<%=guia.getNumPack()%></td>
					<td width="20" nowrap class="td" style="height: 41px !important;"
						align="center"><input type="checkbox" name="checkGuia"
						value="<%=guia.getGuiaNumber() + "//" + guia.getNumPack() + "//" + guia.getRefers()  + "//" + guia.getOrigBranch() + "//" + guia.getFormNumber() + "//" + guia.getIsseDate() + "//" + guia.getDestBranch() + "//" + guia.getDestClientName() + "//" + guia.getGuiaAmount() %>" ></td>
				</tr>
				<%
					}
							if (lenListGuia < 10)
								for (int j = lenListGuia; j <= 10; j++) {
				%><tr>
					<td width="60" height="17" nowrap class="td" align="right">&nbsp;</td>
					<td width="80" height="17" nowrap class="td">&nbsp;</td>
					<td width="169" height="17" nowrap class="td">&nbsp;</td>
					<td width="60" height="17" nowrap class="td">&nbsp;</td>
					<td width="150" height="17" nowrap class="td">&nbsp;</td>
					<td width="275" height="17" nowrap class="td">&nbsp;</td>
					<td width="75" height="17" nowrap class="td">&nbsp;</td>
					<td width="60" height="17" nowrap class="td">&nbsp;</td>
					<td width="20" height="17" nowrap class="td">&nbsp;</td>
				</tr>
				<%
					}
				%>
			</table>
		</div>
		
		<div>
			<div align="center"> 
					<font style="font-size: 10pt; font-weight: bold"><bean:write name="cancelGuiaMultForm" property ="infoDate"/></font>
			</div>
			<div align="center" style=" margin-top: 5px; margin-bottom: 9px;">				
				<span style=" float: center; "> 
				<input
					id="buttonCancel" type="button" class="button" style="width: auto;" value="Confirmar Cancelación"  name="buttonCancel" 
					onclick="return cancelGuias()"
					<%=disabledButtons%> /> 
					<input id="buttonRefresh" type="button" class="button " value="Actualizar" name="buttonRefresh"
					onclick="return refreshGuias()" /> 
					<input id="buttonReinit" type="button" class="button " value="Reiniciar" name="buttonReinit"
					onclick="return reinitGuias()" /> 
				</span>
				<logic:present name="errormsgprint" scope="request">
					<tr>
						<td>
					 		<div align="center"> 
					 		<font style="font-size: 12pt; color: #ff0000; font-weight: bold"><bean:write name="errormsgprint" /></font></div>
						</td>
					</tr>
				</logic:present>

			
				
			</div>
		</div>
		<html:hidden property="currentTask" />
		<html:hidden property="fileName"/>
		<html:hidden property="pathFile"/>
		<html:hidden property="filtroPor" />
		<html:hidden property="daysRange" />
		<html:hidden property="infoDate" />
		<html:hidden property="fechaInicial" />
		<html:hidden property="fechaFinal" />
		<input type="hidden" id="allowPrintQZ" name="allowPrintQZ"
			value='<%=global.getAllowPrintQZ()%>'>
		<div id="zonePrintQZ"></div>
	</html:form>

	<div id="resumen_modal" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">	
				<div class="modal-header">
					<h4 class="modal-title">Cancelaci&oacute;n exitosa</h4>
				</div>
				<div class="modal-body">
				<p>Resumen de movimientos realizados.</p>
					<form id="formulario" accept-charset="utf-8">
						<fieldset>
							<div class="form-group">
								<label class="control-label">Registros cancelados:</label> <span class="txt-special-1"><bean:write name="cancelGuiaMultForm" property ="countGuiaSucess"/>
							</div>
							<div class="form-group">
								<label class="control-label">Movimientos fallidos : </label>  <span class="txt-special-1"><bean:write name="cancelGuiaMultForm" property ="countGuiaError"/>
							</div>
							<button id="btn_cambio" type="button" class="button" style="width: auto; border-radius: 5px; background: red; border: 1px solid red;" onclick="return descargarxls()">Descargar excel con resumen</button>
							<button id="btn_cambio" type="button" class="button"  style="width: auto; border-radius: 5px; background: red; border: 1px solid red;" onclick="return closeModal()">Cerrar ventana</button>
						</fieldset>
					</form>
				</div>
			</div>
			<!-- Modal body -->
		</div>
		<!-- Modal dialog -->
	</div>
	<!-- sitio_modal -->

</BODY>
</html:html>
