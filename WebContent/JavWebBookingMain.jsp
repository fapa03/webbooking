<%--

/**
 * @author: Abraham Daniel Arjona Peraza
 * Fecha de Creación:
 * Compañía: KUMARAN.
 * Descripción del programa: menu principal
 * FileName	:
 * FormBean	:
 * ActionBean	:
 * OtherClasses	:	none
 * CSS FileName	:	webbooking.css
 * -----------------------------------------------------------------
 * MODIFICACIONES:
 * -----------------------------------------------------------------
 * Clave: AAP03
 * Autor: ABRAHAM DANIEL ARJONA PERAZA
 * Fecha: 01/07/2013
 * Descripción:  LLamado a accion para cargar historico de envios.
 * validacion de bandera para ocultar pantalla antigua de eleboracion de guias,
 * si esta activa la bandera de nueva elaboracion de guias.
 * ------------------------------------------------------------------
 * Clave:
 * Autor:
 * Fecha:
 * Descripción:
 * ------------------------------------------------------------------
 * Clave:
 * Autor:
 * Fecha:
 * Descripción:
 * ------------------------------------------------------------------
 */
--%>
<%@page import="bean.Global"%>
<%@page import="org.apache.axis.session.Session"%>
<%@page contentType="text/html"%>
<%@ taglib uri="WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="WEB-INF/struts-bean.tld" prefix="bean" %>
<%String version = "00.00.17";%>
<html>
<head>
<title>Menú Principal</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<style type="text/css">
.floatMensaje{
	position: absolute;
	top: 130px;
	right: 30px;
}
.floatMensaje *{
	color: #fff !important;
}
</style>
<script language="JavaScript">

	function goOut(page){
		if(page=="guiabooking") {
			page="thispage";
			if (document.forms[0].showWeb.value != 'Y') {
				alert('USUARIO NO AUTORIZADO PARA OPCION ELABORACION DE GUIA');
				return;
			}
		}

		if (page == 'comunicado'){
			window.open('Avisos.jsp','Avisos Paquetexpress',"width="+screen.availWidth+",height="+screen.availHeight);
			window.resizeTo(screen.availWidth, screen.availHeight);
			return;
		}


		if(confirm("DESEA PERDER LA INFORMACION?")){
			if (page=='shipmenthistory') {
				document.forms[0].action="shipmentHistory.do";
			} else {
				document.forms[0].action="webBookinggeneralMain.do?includeattribute=yes&page="+page;
			}
			document.forms[0].submit();
		}else{
			return;
		}
	}
	function openPrepaid() {
		if (document.getElementById('controlWait').value == 'N') {
			document.getElementById('controlWait').value = 'Y';
			document.forms[0].action="guiaConversionMonitor.do?currentTask=start";
			document.forms[0].submit();
		}
	}
	function openOption(opt) {
		if (document.getElementById('controlWait').value == 'N') {
			document.getElementById('controlWait').value = 'Y';
			document.forms[0].action = opt;
			document.forms[0].submit();
		}
	}
</script>
<link rel="stylesheet" href="css/bootstrap.css?v=<%=version%>">
<link rel="stylesheet" href="css/lobibox.css?v=<%=version%>"/>
<link rel="stylesheet" href="css/app.css?v=<%=version%>">
<link rel="stylesheet" href="css/stylev2.css?v=<%=version%>">
<link rel="stylesheet" href="css/popup.css?v=<%=version%>"> <!-- CSS DE POPUP TEMPORAL -->
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<script src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>

<script src="js/bootstrap.min.js"></script>
</head>

<body id="body" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="" style="background: #0081c6;">
<jsp:include page="nocache.jsp" flush="false" />
	<html:form action="/clientDestinationEntry" style="margin:0!important;">
		<!-- DIV POPUP TEMPORAL -->
		<logic:present scope="request" name="showPopup">
			<logic:equal name="loginForm" property="showPopup" value="1">
		        <div id="popup" class="overlay">
		            <div id="popupBody">
		                <h2>Comunicado Versión 3.0 CCP</h2>
		                <a id="cerrar" href="#popup">&times;</a>
		                <div class="popupContent" align="justify">

		                   <b> Estimado cliente.<br><br>

							Le informamos que a partir del 1 de enero de 2024 y en cumplimiento con las disposiciones oficiales del Servicio de Administración Tributaria (SAT), se estará emitiendo la versión 3.0 del complemento de carta porte.<br>
							Para conocer más detalles le invitamos a visitar el siguiente enlace: <a onclick="goOut('comunicado');">Comunicado</a> </b>
		                    <p><br>


							<div align="center"><b>ATENTAMENTE<br/><br>	OPERADORA DE SERVICIOS PAQUETEXPRESS, S.A. DE C.V.</b> </div>
    						</div>
		                </div>
		            </div>
		        </div>
	        </logic:equal>
        </logic:present>
		<div class="mainMenu bg_3_MainMenu bg_2_MainMenu bg_1_MainMenu">
		<img alt="logo" class="mainLogo" src="images/logos/LOGO-PQT-BL-WB-SB.png"> 

			<div class="tableMainMenu hoverEfectLeftToRight displayTable">
			 <div class="verticalAlginMiddle">
			 				<div onmouseenter="itemMenuBackground('bg_1_MainMenu')" style="display: table;" >
								<div class="menu_sitio menu_sitioTitle"  >
									Guías
								</div>
								<logic:present name="loginForm">
										<html:hidden name="loginForm" property="newBooking"></html:hidden>
										<logic:equal name="loginForm" property="newBooking" value="N">

													<div align="right">
														<a href="webBookinggeneral.do"
															onMouseOut="MM_swapImgRestore()"
															onMouseOver="MM_swapImage('ela','','images/elabo_o.gif',1)"><img
															name="ela" border="0" src="images/elabo_m.gif" width="207"
															height="25"></a>
													</div>

										</logic:equal>
									</logic:present>
									<logic:present name="loginForm">
										<html:hidden name="loginForm" property="newBooking"></html:hidden>
										<logic:equal name="loginForm" property="showWeb" value="Y">
											<logic:equal name="loginForm" property="newBooking" value="Y">

														<div class="menu_sitio" >
															<a href="webBookinggeneralMain.do">Elaboración de
																guías</a>
														</div>

											</logic:equal>
										</logic:equal>
									</logic:present>

									<logic:present name="loginForm">
										<html:hidden name="loginForm" property="showPpg"></html:hidden>
										<logic:equal name="loginForm" property="showPpg" value="Y">
											<div class="menu_sitio">
												<a href="javascript:openPrepaid();">Elaboración de guías prepago</a>
											</div>
										</logic:equal>
									</logic:present>
									<logic:equal name="sGlobal" property="showGlobalRR" value="1">
										<div class="menu_sitio">
												<a href="javascript:openOption('guiaReturn.do?currentTask=start');">Elaboración de guías de retorno</a>
											</div>
									</logic:equal>
									<logic:notEqual name="sGlobal" property="showGlobalRR" value="1">
										<logic:present name="loginForm">
											<html:hidden name="loginForm" property="showGuiasRR"></html:hidden>
											<logic:equal name="loginForm" property="showGuiasRR" value="1">
												<div class="menu_sitio"
												>
													<a href="javascript:openOption('guiaReturn.do?currentTask=start');">Elaboración de guías de retorno</a>
												</div>
											</logic:equal>
										</logic:present>
									</logic:notEqual>
									<div class="menu_sitio"  >
										<a href="javascript:openOption('JavGuiaCancellation.jsp');">Cancelación de guía</a>
									</div>
									<logic:equal name="sGlobal" property="showGlobalMultCncl" value="1">
										<div class="menu_sitio">
											<a href="javascript:openOption('cancelGuiaMult.do');">Cancelación de guías múltiples</a>
										</div>
									</logic:equal>
									<logic:notEqual name="sGlobal" property="showGlobalMultCncl" value="1">
									<logic:equal name="sGlobal" property="allowCancelGuiaMult" value="Y">
										<div class="menu_sitio">
											<a href="javascript:openOption('cancelGuiaMult.do');">Cancelación de guías múltiples</a>
										</div>
									</logic:equal>
									</logic:notEqual>
							 		<logic:equal name="sGlobal" property="showReprintGuia" value="1">
										<div class="menu_sitio">
											<a href="javascript:openOption('reprintGuia.do');">Reimpresión de guías</a>
										</div>
									</logic:equal>
									<div class="menu_sitio "  >
										<a href="javascript:openOption('shipmentHistory.do');">Histórico de envíos</a>
									</div>
					 		</div>
					 		<div onmouseenter="itemMenuBackground('bg_2_MainMenu')" style="display: table;">
									<div class="menu_sitio menu_sitioTitle"  >
										Cliente
									</div>

									<div class="menu_sitio" >
										<a href="javascript:openOption('JavReportMain.jsp');">Información del cliente</a>
									</div>

									<logic:present name="loginForm">
										<html:hidden name="loginForm" property="userValidate" />

										<logic:equal name="loginForm" property="userValidate"
											value="validUser">

													<div class="menu_sitio" >
														<a href="javascript:openOption('clientDestinationEntry.do');">Registro de cliente
															destino</a>
													</div>

										</logic:equal>
 									</logic:present>

									<div class="menu_sitio">
										<a href="javascript:openOption('webbookingFileUpload2.do');">Importar archivo de
											envios y generación de guía</a>
									</div>

									<div class="menu_sitio" >
										<a id="btn_modal" href="#">Cambiar plaza</a>
									</div>

							</div>

					</div>

			</div>

	</html:form>
	<!-- Modal -->
	<div id="sitio_modal" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Selección de plaza</h4>
				</div>
				<div class="modal-body">
					<p>Por favor selecciona una nueva plaza para documentar.</p>
					<form id="formulario" accept-charset="utf-8">
						<fieldset>
							<div class="form-group">
								<label class="control-label">Plaza actual:</label> <em
									id="lbl_plaza_actual"> </em>
							</div>
							<div class="form-group">
								<label class="control-label">Usuario: </label> <em
									id="lbl_usuario"> </em>
							</div>
							<div class="form-group">
								<label class="control-label">Cliente convenio:</label> <em
									id="lbl_cliente_convenio"> </em>
							</div>
							<div class="form-group">
								<label class="control-label">Cliente origen: </label> <em
									id="lbl_cliente_origen"> </em>
							</div>
							<div class="form-group">
								<label for="combo">Seleccione una nueva plaza:</label>
								<div class="dropdown">
									<button id="combo"
										class="btn btn-default form-control dropdown-toggle"
										type="button" data-toggle="dropdown">
										<span class="caret"></span>
									</button>
									<ul id="ul_combo" class="dropdown-menu">
									</ul>
								</div>
								<div class="form-group">
									<label class="control-label">Cliente miembro: </label> <em
										id="lbl_nuevo_cliente_miembro"></em>
								</div>
							</div>
							<div class="form-group">
								<label for="dialog_addr">Dirección:</label>
								<textarea id="dialog_addr" class="form-control" rows="2"
									readonly></textarea>
							</div>
							<button id="btn_cambio" type="button" class="btn btn-primary">Cambiar
								de plaza</button>
						</fieldset>
					</form>
				</div>
			</div>
			<!-- Modal body -->
		</div>
		<!-- Modal dialog -->
	</div>
	<!-- sitio_modal -->
	
	<!-- Modal -->
	<div id="sitio_modalAbout" class="modal fade" role="dialog">
		<div class="modal-dialog">
			<!-- Modal content-->
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">&times;</button>
					<h4 class="modal-title">Acerca de Webbooking</h4>
				</div>
				<div class="modal-body">
					<p>Más información sobre Webbooking.</p>
					<form id="formulario" accept-charset="utf-8">
						<fieldset>
							<div class="form-group">
								<label class="control-label">Versión:</label> <em
									id="lbl_serverName"> </em>
							</div>
						</fieldset>
					</form>
				</div>
			</div>
			<!-- Modal body -->
		</div>
		<!-- Modal dialog -->
	</div>
	<!-- sitio_modal -->
 
	<div class="sectionUser tableMainMenu hoverEfectLeftToRight" onmouseenter="itemMenuBackground('bg_3_MainMenu')" >
		<div class="menu_sitio menu_sitioTitle">
			Usuario

		</div>
		<div class="menu_sitio">
		  <a href="javascript:openOption('JavChangePassword.jsp');">Cambiar contraseña</a>
		</div>
		<div class="menu_sitio">
		<logic:present name="loginForm">
			<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->
		  	<a href="login.do?logout=yes">Terminar sesion</a>
		  	</logic:equal>
		</logic:present>												
		</div>
				
		<logic:present name="loginForm">
			<div class="menu_sitio">
				<a id="btn_modalAbout" name="about" href="#">Acerca de</a>
			</div>
		</logic:present>	
 		
	</div>

	<div id="mensaje2" class="floatMensaje">
		<label id="mensaje_lbl"></label>
	</div>
	<script type="text/javascript" src="js/app.js?v=<%=version%>"></script>
	<script type="text/javascript" src="js/lobibox.min.js"></script>
	<script type="text/javascript">
		<%String algo = "";
			if (request.getAttribute("sPwdUpdated") != null) {
				algo = (String) request.getAttribute("sPwdUpdated");
			}%>
		var pwdUpdated = "<%=algo%>";
		if(pwdUpdated=="yes"){
			Lobibox.notify('info', {
			    size: 'mini',
			    msg: 'La contraseña ha sido cambiada correctamente',
			    sound: 'sound1'
			});
		}

		$('#btn_modal').on('click', function (event) {
 			<%Global global = (Global) session.getAttribute("sGlobal");
			String clienteId = global.getClientId();
			String asignado = global.getAssignedSite();
			String usuario = global.getOrigenUserClave();
			String userName = global.getOrigenUserNombre();
			String cliente = global.getClientName();
			String siteName = global.getSiteName();
			String convenio = global.getClientIdAgreement();%>
			$("#sitio_modal").modal({
				backdrop:false,
				keyboard:false,
				show:true
			});
			if(jQuery.isEmptyObject(actual)){
				actual.clienteId = "<%=clienteId%>";
				actual.clienteNombre = "<%=cliente%>";
				actual.userClave = "<%=usuario%>";
				actual.userName = "<%=userName%>";
				actual.siteClave = "<%=asignado%>";
				actual.siteName = "<%=siteName%>";
				actual.clienteConvenio = "<%=convenio%>";
				actual.serverName = "<%=global.getServerName()%>";
							}
							$("#lbl_plaza_actual").text(
									actual.siteClave + " | " + actual.siteName);
							$("#lbl_usuario").text(
									actual.userClave + " | " + actual.userName);
							$("#lbl_cliente_origen").text(
									actual.clienteId + " | "
											+ actual.clienteNombre);
							$("#lbl_cliente_convenio").text(
									actual.clienteConvenio);

							getPlazas();
							$(document).ready(function() {
								$("#mensaje_lbl").text("Plaza: " + "<%=siteName%>");
							});
						});
		
		
		$('#btn_modalAbout').on('click', function (event) {
			$("#sitio_modalAbout").modal({
				backdrop:false,
				keyboard:false,
				show:true
			});
			if(jQuery.isEmptyObject(actual)){
				actual.clienteId = "<%=clienteId%>";
				actual.clienteNombre = "<%=cliente%>";
				actual.userClave = "<%=usuario%>";
				actual.userName = "<%=userName%>";
				actual.siteClave = "<%=asignado%>";
				actual.siteName = "<%=siteName%>";
				actual.clienteConvenio = "<%=convenio%>";
				actual.serverName = "<%=global.getServerName()%>";
			}
			$("#lbl_serverName").text(actual.serverName);
		});
	</script>
	</div>
	<input type="hidden" id="controlWait" value = "N">
</body>
</html>
