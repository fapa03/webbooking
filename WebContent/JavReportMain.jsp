<%@page contentType="text/html"%>
<%@page import="bean.Global"%>
<%@ taglib uri="WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="WEB-INF/struts-bean.tld" prefix="bean"%>
<%String version = "00.00.10"; %>
<html>
<head>
<title>Información del cliente</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/bootstrap.css?v=<%=version%>">

<link rel="stylesheet" href="css/app.css?v=<%=version%>">

<link rel="stylesheet" href="css/stylev2.css?v=<%=version%>">
<script type="text/javascript" src="js/jquery/1.12.0.js"></script>
<script type="text/javascript" src="js/v2/global.js"></script>
<link href="images/logos/favicon.ico" rel="icon" type="image/x-icon" />
<style type="text/css">
.floatMensaje{
	position: absolute !important;
	top: 130px !important;
	right: 30px !important;
	left: inherit !important;
}
.floatMensaje *{
	color: #fff !important;
}
</style>
<script type="text/javascript">

	function callReport(forma) {
		newWindow = window
	    	.open(
	        	forma,
	            	"ManifestReport",
	                "scrollbars=auto,menubar=no,toolbar=no,status=no,directories=no,location=no,width=400,height=250,resizable=yes,top=185,left=200,screenY=185,screenX=200");
	    newWindow.focus()
	}
	
	function callGuiasPrepagoUtil() {
	    document.forms[0].action = "usedGuia.do?currentTask=start";
	    document.forms[0].submit();
	}
</script>
</head>

<body  leftmargin="0" topmargin="0" "
	marginwidth="0" marginheight="0"  style="background: #0081c6;"
	> 
	
	<html:form action="/clientDestinationEntry" style="margin:0!important;">

		<div class="mainMenu">
		<img alt="logo" class="mainLogo" src="images/logos/Logo_Pqt_Blanco.png">
 
			<map name="peMap">
				<area shape="rect" coords="13,19,235,71"
					href="http://www.paquetexpress.com.mx" target="_blank"
					alt="www.paquetexpress.com" title="www.paquetexpress.com">
			</map>
			
		 	<div class="tableMainMenu hoverEfectLeftToRight displayTable" onmouseenter="itemMenuBackground('bg_3_MainMenu')" >
			 <div class="verticalAlginMiddle"> 
										
									<div class="menu_sitio menu_sitioTitle">
										Información de cliente
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
							


											<div class="menu_sitio">
													<a href="javascript:callReport('RangoFechasReporte.jsp');">Informe
														de Servicios Facturados</a>
											</div>					
											
											<div class="menu_sitio">
												<a href="javascript:callGuiasPrepagoUtil()">Guias prepago
												utilizadas</a>
											</div>
											
											<logic:present name="loginForm">
												<html:hidden name="loginForm" property="userValidate" />
				
												<logic:equal name="loginForm" property="userValidate"
													value="validUser">
				 
															<div class="menu_sitio">
																<a href="clientDestinationEntry.do">Registro de cliente
																	destino</a>
															</div> 
											 
												</logic:equal>
												<!-- ended by bala -->
											</logic:present>	
											
																																<div class="menu_sitio">
											<a href="JavWebBookingMain.jsp">
												Menú principal
											</a>
											</div>
											
					 
						<div align="left"></div>
						</div>
		 </div>
		 
	<div class="sectionUser tableMainMenu hoverEfectLeftToRight">
		<div class="menu_sitio menu_sitioTitle">
			Usuario
		</div>
		<div class="menu_sitio">
		  <a href="JavChangePassword.jsp">Cambiar contraseña</a>												
		</div>		
		<div class="menu_sitio">
			<logic:present name="loginForm">
				<logic:equal name="loginForm" property="milliSeconds" value="">		<!-- si no viene de Customer central, se muestra cerrar sesion -->
		  			<a href="login.do?logout=yes">Terminar sesion</a>
		  		</logic:equal>
		  	</logic:present>												
		</div>				
 
	</div>		 
		 
	 </div>
	</html:form>
	

	
	<div id="mensaje" class="floatMensaje"><label id="mensaje_lbl"></label></div>
	<script src="js/jquery.min.js"></script>
	<script type="text/javascript">
		<% 
		   Global global = (Global)session.getAttribute("sGlobal"); 
		   String siteName = global.getSiteName();
	 	%>
		$(document).ready(function(){
			$("#mensaje_lbl").text("Plaza: "  + "<%=siteName%>");
		});
	</script>
	</div>
</body>
</html>
